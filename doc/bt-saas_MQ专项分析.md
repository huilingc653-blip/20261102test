# bt-saas MQ 场景与租户信息丢失风险专项分析

> 分析范围：`D:\WorkSpaces\Bytter\bt-saas` 全部模块  
> 审计日期：2026-07-25

## 1. 核心结论

项目使用 Spring Cloud Stream + RabbitMQ Binder，以消息 Header `X-Tenant-Id` 传递
租户。现有主要链路大多携带并恢复租户，但没有框架级强制：

- 生产者既有 `TenantAwareStreamBridge`，也有裸 `StreamBridge`；
- 消费者统一拦截方案均已注释或失效；
- 每个消费者手工读取 Header、设置 TTL、finally 清理；
- tenantId 为空时只记录 warn，仍继续执行业务；
- `TenantConnection` 在 tenantId 为空时直接执行原 SQL，不做租户过滤。

因此结论是：**现有正常链路多数可用，但任何一次漏设 Header 或漏恢复上下文，都可能
静默绕过租户隔离，整体属于高风险、非 fail-safe 设计。**

## 2. MQ 场景清单

| # | Exchange | binding | 方向 | 核心业务 | 生产者 | 消费者 |
|---:|---|---|---|---|---|---|
| 1 | `system-initNetDayClose-exchange` | `init-net-day-close-out-0` → `initNetDayClose-in-0` | system → finacial | 初始化网点日结日期 | `InitNetDayCloseMQProducer` | `InitNetDayCloseEvent` |
| 2 | `finacial-exchange` | `finacial-to-payment-out-0` → `finacialToPayment-in-0` | finacial → payment | 账务事务结果通知付款 | 财务 `ProducerMessageRecordJob` | `FinacialToPaymentEvent` |
| 3 | `payment-finacial-exchange` | `record-booking-out-0` → `recordBooking-in-0` | payment → finacial | 付款业务自动记账 | 付款 `ProducerMessageRecordJob`、测试控制器 | `RecordBookingEvent` |
| 4 | `payment-bill-exchange` | `payment-to-bill-out-0` → `billEndorsementToPayment-in-0` | payment → bill | 付款联动票据背书 | 付款 `ProducerMessageRecordJob` | `BillEndorsementToPaymentEvent` |
| 5 | `workflow-mq-direct` | `workflow-msg-out-0` → 各服务 `workflowEvent` | workflow → 各业务服务 | 审批完成/撤回回调 | `FlowMessageService` | `WorkflowCallBackEvent` |
| 6 | `bis-exc-topic` | `bis-exc-to-payment-out-0` | increment → payment | 指令状态变更 | `BisMQService` 整体注释 | `BisExcStatusChangeEvent` 残留 Bean |

## 3. 场景详解

### 3.1 日结初始化

配置：

- system `application.yml:15-25`
- finacial `application.yml:22-25,37-40`

链路：

```text
InitNetDayCloseMQProducer
  → tenantAwareStreamBridge.send(
       "init-net-day-close-out-0",
       json,
       UserContextHelper.getTenantId(),
       InitNetDay routing header)
  → InitNetDayCloseEvent
  → Header tenantId
  → setTTLTenant
  → fcDayCloseService.initNetCloseDate
  → finally clearTTLTenant
```

当前主链路租户传递正确。风险是消费者空 Header 只 warn 后继续。

### 3.2 财务到付款

配置：

- finacial `application.yml:16-21,32-36`
- payment `application.yml:36-55`

财务侧可靠消息记录保存 tenantId，`ProducerMessageRecordJob.java:73` 使用记录中的
tenantId 经 `TenantAwareStreamBridge` 重发。付款侧 `FinacialToPaymentEvent` 手工恢复
tenant，维护消费记录并调用业务 Handler。

优点：重放不依赖 Job 当前线程上下文。

风险：原始记录 tenantId 为空时会把错误固化；消费者仍未 fail-fast。

### 3.3 付款到财务

配置：

- payment `application.yml:26-30,42-46`
- finacial `application.yml:26-29,41-45`

付款可靠消息任务按 routingKey 选择 `record-booking-out-0`，使用记录 tenantId 发头。
财务 `RecordBookingEvent` 恢复 tenant 并执行记账业务。

`BisAccGatherPolicyController.java:80` 也有直接发送路径，使用
`UserContextHelper.getTenantId()` 显式传租户，当前正确。

但 `FcAccbookBlnedController.java:80` 存在裸 `streamBridge.send` 测试接口，只有
`ftopkey`，没有 tenant Header。若接口可达，将产生无租户消息。

### 3.4 付款到票据

配置：

- payment `application.yml:31-35,47-51`
- bill `application.yml:23-35`

付款可靠消息任务在 routingKey 为 `bill` 时选择 `payment-to-bill-out-0`，发送
`payment-bill-exchange`。票据 `BillEndorsementToPaymentEvent` 恢复 tenant，维护
`ConsumerMessageRecord` 幂等和处理状态，再调用 `BillEndorsementMessageHandler`。

注意：历史文档中写成 `payment-billendorsement-exchange`，当前源码实际主链路是
`payment-bill-exchange`。

### 3.5 工作流回调

配置：

- workflow `application.yml:6-15`
- 各业务服务 `function.definition` 中的 `workflowEvent`

`FlowMessageService.java:50-55` 使用裸 `StreamBridge`，但手工添加：

- `service-name`：Rabbit routing key；
- `X-Tenant-Id`：当前 tenant。

业务模块中的公共 `WorkflowCallBackEvent`：

1. 恢复 tenant；
2. 写 `bt_message_repeat` 做幂等；
3. 按 cateKey 查 `IWorkFlowListener`；
4. 调用 `flowCompleted`/`flowRevoke`；
5. 更新工作流消息记录；
6. finally 清理 tenant。

风险：

- 工作流发送时当前 tenant 为空，仍会构造无租户消息；
- 消费时 Header 为空仍继续写幂等表和执行业务；
- 回调只恢复 tenant，不恢复 payload 中的 CurrentUser。

### 3.6 弃用的指令状态链路

increment 的 `BisMQService.java` 文件整体处于块注释内，payment
`BisExcStatusChangeEvent` 标注“弃用”。payment 当前 `function.definition` 也没有
`bisExcEvent`。

该消费者没有读取/恢复 tenant。如果未来重新启用，会直接处于高风险状态，必须先接入
统一上下文包装。

## 4. 配置和接线异常

1. accountReceivable 的 `application.yml:23-35` 配置
   `billEndorsementToPayment` 和 `payment-billendorsement-exchange`，但模块中未找到
   对应 Consumer Bean。
2. bill 模块有真正的 `BillEndorsementToPaymentEvent`，其 exchange 是
   `payment-bill-exchange`。
3. `bisExcEvent` Bean 存在、生产者和函数配置却已停用，属于残留代码。
4. `BankAccConfigController`、`FcAccbookBlnedController` 的裸发送测试入口没有租户头。
5. `TenantAwareFunctionWrapper`、`Interceptor`、`CatalogConfig`、
   `RegistrationListener` 等消费者自动恢复方案均未生效。

## 5. 租户风险

### MQ-R1 高危：空租户继续消费

主要消费者均采用：

```java
if (tenantId != null) {
    setTTLTenant(tenantId);
} else {
    log.warn(...);
}
// 仍然继续业务
```

tenant 为空时，消费记录查询、幂等写入和业务 Handler 都可能执行无租户 SQL。

### MQ-R2 高危：生产者出口混用

`TenantAwareStreamBridge` 与裸 `StreamBridge` 并存，新增生产者很容易漏头。

### MQ-R3 高危：默认 TenantAware send 读取接口不完整

`TenantAwareStreamBridge.send(data, headers)` 使用 `getTTLTenant()`，不会回退 Dubbo
attachment。Dubbo Provider 同步线程中可能 `getTenantId()` 有值但 `getTTLTenant()`
为空，从而漏头。

### MQ-R4 中高：消费者只恢复租户

MQ 线程没有当前用户。依赖用户的审计、权限、通知或 Dubbo 下游调用会拿到 null。

### MQ-R5 中：可靠消息只保证“记录中的 tenant”

重放设计正确，但没有防止空 tenant 记录入库。应在保存
`ProducerMessageRecord` 时做非空和合法性校验。

### MQ-R6 中：工作流裸发送

当前手工头正确，但没有统一组件约束，维护时容易被删除或漏加。

### MQ-R7 中：消费者清理逻辑分散

每个 Listener 自己管理 set/clear，新增 return、异常分支或嵌套消费可能造成污染或
清掉外层上下文。

## 6. 整改优先级

### P0

1. Consumer 缺少/非法 tenant 时直接抛异常，进入 DLQ，不执行业务。
2. `TenantConnection` tenant 为空时拒绝租户业务 SQL。
3. 保存可靠消息记录时强制 tenantId 非空并验证租户存在。

### P1

1. 禁止业务注入裸 `StreamBridge`，统一使用 `TenantAwareStreamBridge`。
2. `TenantAwareStreamBridge` 改用 `getTenantId()` 并对空值 fail-fast。
3. 使用全局 Channel/Function interceptor 自动安装并恢复 tenant/user 上下文。
4. 上下文清理改为“保存旧快照并恢复”，而不是无条件 clear。
5. `CommonMqMsg` 增加 tenantId、operator、traceId，Header 和 payload 交叉校验。

### P2

1. 清理 accountReceivable 错误 binding 和弃用 `bisExcEvent`。
2. 删除或隔离测试发送控制器。
3. 为每个 binding 增加契约测试：
   - 生产消息必须有 tenant Header；
   - 空 tenant 必须拒绝；
   - 消费线程内 tenant 与消息一致；
   - 消费结束后上下文恢复；
   - 重试和重复消费不跨租户。

## 7. 关键文件

- `bytter-common-base/bytter-framework/.../TenantAwareStreamBridge.java`
- `bytter-common-base/bytter-framework/.../TenantAwareFunction*.java`
- `bytter-common-base/bytter-framework/.../WorkflowCallBackEvent.java`
- `bytter-manager-account/.../InitNetDayCloseEvent.java`
- `bytter-manager-account/.../RecordBookingEvent.java`
- `bytter-manager-account/.../ProducerMessageRecordJob.java`
- `bytter-service-payment/.../FinacialToPaymentEvent.java`
- `bytter-service-payment/.../ProducerMessageRecordJob.java`
- `bytter-prepose-bill/.../BillEndorsementToPaymentEvent.java`
- `bytter-workflow/.../FlowMessageService.java`
- 各 start 模块 `src/main/resources/application.yml`

## 8. 最终结论

当前主链路的租户 Header 大多存在，但安全依赖人工约定，且空租户不阻断。在
`TenantConnection` 空租户直接放行 SQL 的背景下，MQ 属于明确的高风险边界。应优先
完成“发送强制带租户、消费强制验租户、数据层空租户拒绝”三道防线。
