# 项目中 MQ 场景梳理与租户信息丢失风险分析

> 分析对象：`bt-saas` 全量代码（静态分析，基于 2026-07-24 代码快照）
> 前置结论：本项目租户隔离采用「共享库 + 行级 `TENANT_ID` + JDBC 层 `TenantConnection`/jsqlparser 改写强制过滤」，且 **`tenantId == null` 时 `TenantConnection` 直接放行 SQL 不过滤**（见《登录与租户隔离方案说明.md》）。因此 **MQ 链路一旦丢失租户身份，后果是静默的跨租户数据泄露**，风险等级最高。

---

## 1. MQ 技术栈与消息模型

- **中间件**：Spring Cloud Stream + RabbitMQ Binder（`spring.cloud.stream.bindings` + `rabbit.bindings`）。
- **消息体模型**：
  - `CommonMqMsg<TransactionMqMsg>`：事务类消息（账务→付款、付款→记账、票据背书等）的通用信封。
  - `String`：纯 JSON 串（日结关账 `InitNetDayClose`）。
  - `WorkflowRemoteEvent`：工作流回调事件。
- **租户标识载体**：消息 **Header** `X-Tenant-Id`（常量 `TenantAwareStreamBridge.TENANT_ID_HEADER = "X-Tenant-Id"`，见 `bytter-framework/.../queue/TenantAwareStreamBridge.java:23`）。**消息体本身不冗余携带租户**（除 `ProducerMessageRecordJob` 从持久化记录读取外）。

---

## 2. 租户上下文跨 MQ 传递机制（核心事实）

### 2.1 生产者侧：仅有 `TenantAwareStreamBridge` 是"启用"的自动设头组件

`TenantAwareStreamBridge.send(binding, payload, tenantId, headers)` 在发送前把 `X-Tenant-Id` 写入消息头（来自 `UserContextHelper.getTTLTenant()` 或显式入参）。这是**唯一被激活**的自动设头能力。

### 2.2 消费者侧：所有"自动还原"机制均被禁用（关键）

框架曾经设计了一套消费者自动还原租户上下文的机制，但**目前全部注释 / 失效**：

| 类 | 状态 | 说明 |
|---|---|---|
| `TenantAwareFunctionWrapper` | 代码整体注释 | 本应在函数外层包一层：从 header 取 `X-Tenant-Id` 并 `setTTLTenant` |
| `TenantAwareFunctionInterceptor` | 代码整体注释 | 同上，拦截式还原 |
| `TenantAwareFunctionPostProcessor` | 构造函数被注释 | `@ConditionalOnMissingBean` 无 `@Primary`，**永不生效** |
| `TenantAwareFunctionRegistrationListener` | 注释"无法启动" | 注册监听后还原 tenant |
| `TenantAwareFunctionCatalogConfig` | 注释"无效果" | 包装 `FunctionCatalog` 中所有 Consumer |

> 结论：**当前租户上下文在消费者侧的还原，100% 依赖每个 listener 在业务方法里手写 `setTTLTenant(tenantId)` + `finally clearTTLTenant()`**，没有任何框架级兜底。

### 2.3 还原范围：只还原"租户"，不还原"用户"

所有活跃消费者只调用 `UserContextHelper.setTTLTenant(tenantId)`，**从不设置 `ICurrentUser`/登录用户**。因此消费者线程内 `getCurrentUser()` / `getUserId()` 一律返回 `null`（或 `NoLoginUser`）。

---

## 3. 全量 MQ 场景清单

| # | 通道 (binding) | 方向 (模块→模块) | 消息类型 | 生产者 | 消费者 | 设租户头 | 还原租户 | 风险 |
|---|---|---|---|---|---|---|---|---|
| 1 | `init-net-day-close` | system → finacial | String(JSON) | `InitNetDayCloseMQProducer` (**TenantAwareStreamBridge**) | `InitNetDayCloseEvent.initNetDayClose` | ✅ | ✅手动 | 低 |
| 2 | `finacial-exchange` (`finacialToPayment`) | finacial → payment | `CommonMqMsg<TransactionMqMsg>` | `ProducerMessageRecordJob` (持久化重发) / finacial 业务 | `FinacialToPaymentEvent.finacialToPayment` | ✅(来自记录) | ✅手动 | 低 |
| 3 | `payment-finacial-exchange` (`recordBooking`) | payment → finacial | `CommonMqMsg<TransactionMqMsg>` | `ProducerMessageRecordJob` / payment 业务 | `RecordBookingEvent.recordBooking` | ✅(来自记录) | ✅手动 | 低 |
| 4 | `payment-billendorsement-exchange` | 付款→票据 (bill) | `CommonMqMsg<TransactionMqMsg>` | NisBillheadService 等（受 `local.massage.billendorsement=open` 开关控制，header 设置需核实） | `BillEndorsementToPaymentEvent.billEndorsementToPayment` | ⚠️需核实 | ✅手动 | **中** |
| 5 | `workflow-mq-direct` (`workflowEvent`) | workflow → 业务模块 | `WorkflowRemoteEvent` | `FlowMessageService.sendWorkFlowEvent`（**裸 streamBridge.send，但手动设头**） | `WorkflowCallBackEvent.workflowEvent` | ✅(手动) | ✅手动 | 低 |
| 6 | `ProducerMessageRecordJob` 重发 | 跨服务可靠投递 | 同 2/3 | 定时任务从 `producer_message_record` 重投 | 同上 2/3 的消费者 | ✅(record.getTenantId()) | ✅ | 低(设计良好) |
| 7 | `bis-exc-topic` (`bisExcEvent`) | 业务异常→payment | — | `BisMQService` (**整体注释/弃用**) | `bisExcEvent`（代码中未找到活跃 Bean，疑似空转） | — | — | **低(空转)** |
| 8 | `BillRabbitMessageConsumer` | （Rabbit 直连尝试） | — | — | 类体**整体注释** | — | — | 无(死代码) |

> 说明：场景 4 的生产者出口在 `bytter-service-payment/.../NisBillheadService`（`local.massage.billendorsement` 开关处）触发，但本次未定位到其具体 `send` 调用是否经 `TenantAwareStreamBridge`。**只要任一生产者裸发且漏设头，消费者即收到 null 租户**——详见 §4 R1。

---

## 4. 各场景核心业务与代码梳理

### 场景 1：日结关账 `init-net-day-close`（system → finacial）
- **业务**：系统触发/银行账户配置后，通知账务模块执行网点日结关账。
- **生产者** `InitNetDayCloseMQProducer`：调用 `tenantAwareStreamBridge.send("init-net-day-close-out-0", json, ...)` —— **走安全通道，自动带 `X-Tenant-Id`**。
- **消费者** `InitNetDayCloseEvent`：读取 header，非空则 `setTTLTenant` 后调用 `fcDayCloseService.initNetCloseDate(...)`，`finally clearTTLTenant()`；**`tenantId==null` 仅 warn 后继续**。

### 场景 2 / 3：账务↔付款 双向事务消息
- **业务**：记账结果回传付款（`recordBooking`）、付款指令下发账务（`finacialToPayment`）。
- **生产者** `ProducerMessageRecordJob`（finacial / payment 各一份）：从持久化 `ProducerMessageRecord` 读取 `record.getTenantId()` 经 `TenantAwareStreamBridge` 重发。**设计良好：租户来自持久化记录而非当前线程，重放安全。**
- **消费者** `FinacialToPaymentEvent` / `RecordBookingEvent`：从 header 取租户还原；空则 warn 继续。

### 场景 4：票据背书联动 `payment-billendorsement-exchange`
- **业务**：付款成功后联动票据模块做背书指令（`abBillEndorseService.paymentBillEndorsement`）。
- **消费者** `BillEndorsementToPaymentEvent`：读取 `X-Tenant-Id` → `setTTLTenant` → `billEndorsementMessageHandler.handler(data)`。
- **风险点**：消费者防御性读取头，但**生产者出口（`NisBillheadService` 受开关控制的分支）是否经 `TenantAwareStreamBridge` 需核实**。若该分支用裸 `streamBridge.send` 且未设头 → 票据侧租户丢失。

### 场景 5：工作流回调 `workflow-mq-direct`
- **业务**：工作流引擎回调业务系统推进单据状态 (`WorkflowRemoteEvent`)。
- **生产者** `FlowMessageService.sendWorkFlowEvent`：**裸 `streamBridge.send`，但手动 `setHeader(TenantAwareStreamBridge.TENANT_ID_HEADER, getTenantId())`**——依赖开发者记得加头（已是"正确但不强制"的写法）。
- **消费者** `WorkflowCallBackEvent.workflowEvent`：从 header 取租户还原。

### 场景 6：可靠消息表重发 `ProducerMessageRecordJob`
- **业务**：对发送失败的消息按持久化记录重试投递。
- **优点**：租户取自 DB 记录 `record.getTenantId()`，与运行线程解耦，重放不会错位租户。
- **注意**：若原始记录落库时 `tenantId` 即错误/为空，则错误被固化重发——根因仍在生产者落库环节。

### 场景 7 / 8：遗留空转 (`bis-exc-topic` / `BillRabbitMessageConsumer`)
- `BisMQService`（payment + increment）**整体注释/标注"弃用"**；`bisExcEvent` 消费者 Bean 未找到活跃定义 → 空转。`BillRabbitMessageConsumer` 类体整体注释，Rabbit 直连方式已废弃。
- **风险**：若未来重新启用 `BisMQService`，必须保证其生产者经 `TenantAwareStreamBridge`，否则会复现 R1。

---

## 5. 租户信息丢失风险分析（核心）

### R1 🔴【高危】租户传递无框架级强制，完全依赖人工约定
生产者"设头" + 消费者"setTTLTenant"都是**手写约定**，而本应兜底的所有自动还原机制（`TenantAwareFunctionWrapper`/`Interceptor`/`PostProcessor`/`CatalogConfig`）**全部被注释/失效**。任何一处遗漏（新增裸生产者、新增忘写 `setTTLTenant` 的消费者）即导致 `tenantId == null` → 结合《隔离方案》中"`tenantId==null` 时 `TenantConnection` 不过滤 SQL"的事实 → **静默跨租户数据泄露**。

> 反例实证：仓库中存在 `BankAccConfigController.test()` 与 `FcAccbookBlnedController.test()`，二者均用**裸 `streamBridge.send(..., "finacial-to-payment-out-0"...)` 且未设置 `X-Tenant-Id` 头**（现已注释，属死代码）。这正是"漏设头"的真实失败形态——只因被注释才未引爆。

### R2 🔴【高危】消费者 null 租户为"warn + 继续"而非"fail-fast"
全部消费者对 `tenantId == null` 仅 `log.warn` 后**照常执行业务**。这把"租户丢失"从「可被监控发现的故障」变成了「无感知的安全事故」，且不会进死信/重试告警。

### R3 🟠【中高】消费者只还原租户、不还原用户
所有消费者从不设置 `ICurrentUser`。下游 `getCurrentUser()` / `getUserId()` 返回 `null`/`NoLoginUser`。若 handler 内部依赖当前用户做**审计字段写入（createBy / updateBy）、权限判断或业务归属**，会得到空值或 NPE（具体取决于各 handler 实现，需逐个核验 `BillEndorsementMessageHandler` / `FinacialMessageHandler` 等是否引用当前用户）。

### R4 🟠【中】异步/线程池子任务可能丢租户
- 框架 `SmartThreadPoolManager` 已用 `TtlExecutors.getTtlExecutorService(...)` 包装（见 `bytter-framework/.../tenant/SmartThreadPoolManager.java:77-90`），`UserContextHelper` 底层为 `TransmittableThreadLocal`——**经托管线程池可正确传播**。
- **但**：若 handler 使用 `@Async`（默认 `SimpleAsyncTaskExecutor` 未经 TTL 包装）或自建 `ExecutorService`/裸 `new Thread`，子线程将丢失租户；且 `finally clearTTLTenant()` 之后发起的异步任务同样丢失租户 → 子任务 `TenantConnection` 不过滤。

### R5 🟠【中】生产者出口不统一
安全通道 `TenantAwareStreamBridge` 与"裸 `streamBridge.send` + 手动设头"（`FlowMessageService`）混用。前者安全，后者**仅靠开发者自觉**。新增消息若误用裸通道且忘设头，即触发 R1。

### R6 🟡【低】头常量一致性隐患
`TenantHeaderConstant`（`bytter-framework/.../queue/TenantHeaderConstant.java`）已**整段注释**，活跃头统一为 `TenantAwareStreamBridge.TENANT_ID_HEADER = "X-Tenant-Id"`，当前一致。但若有人重新启用被注释的 `TenantAwareFunctionWrapper`（其内部引用 `TenantHeaderConstant.TENANT_ID_HEADER`），必须先修复该常量，否则还原读取的是空常量。

### R7 🟡【低】`bis-exc` 链路空转
`bisExcEvent` 消费者疑似无活跃生产者（生产者 `BisMQService` 已注释）。空转无直接风险，但属"遗留接线"隐患，重启用时需补设头。

### R8 🟢【低，正面】消息重放安全性尚可
`ProducerMessageRecordJob` 以持久化记录的 `tenantId` 重发，租户与运行线程解耦，重放不易错位（根因仍在落库环节）。

---

## 6. 改进建议（按优先级）

### P0（立即，阻断静默泄露）
1. **消费者 fail-fast**：`tenantId == null || tenantId 非法` 时直接拒绝消息（Nack / 抛异常进死信队列 + 告警），**绝不让业务在"无租户上下文"下执行**。这能在 R1 触发时把"安全事故"转为"可观测故障"。
2. **统一生产者出口**：将 `TenantAwareStreamBridge` 设为**唯一**消息发送入口，封装 `streamBridge` 注入为内部（禁止业务直接 `@Autowired StreamBridge` 裸发）；或在 `TenantAwareStreamBridge` 内对缺失 `X-Tenant-Id` 的发送做启动期/运行期断言。

### P1（短期，消除人工依赖）
3. **重新启用可用的消费者自动还原 + 强制校验**：修复 `TenantAwareFunctionCatalogConfig` / `TenantAwareFunctionWrapper`（或改用 Spring Cloud Stream 的全局 `ChannelInterceptor` / `GlobalChannelInterceptor`），在**框架层**从 header 取租户并 `setTTLTenant`，与 P0 的 fail-fast 校验合并，使新消费者无需手写还原。
4. **按需还原用户上下文**：业务确实需要当前用户时，由生产者在消息体（`CommonMqMsg`）中携带操作用户信息，消费者 `setCurrentUser(...)`，保证审计字段完整。

### P2（中期，加固异步与冗余）
5. **统一异步出口**：业务侧异步一律走 `SmartThreadPoolManager`（已 TTL 包装），禁止 handler 内 `@Async` 默认池 / 裸线程池；`clearTTLTenant()` 之前确保不发起跨 `finally` 的异步任务。
6. **消息体冗余携带租户**：在 `CommonMqMsg` 中追加 `tenantId` 字段，作为 header 丢失的兜底，消费者优先用 header、缺失时回退 payload 并告警。

### P3（长期，防回归）
7. **契约/集成测试**：为所有生产者增加"消息必须带 `X-Tenant-Id`"的断言测试，CI 拦截漏设头的改动；为所有消费者增加"null 租户被拒绝"的测试。
8. **清理遗留接线**：删除 `BisMQService` / `BillRabbitMessageConsumer` / 各 `test()` 死代码，避免误启用时复现 R1/R7。

---

## 7. 关键文件索引

| 文件 | 角色 |
|---|---|
| `bytter-framework/.../queue/TenantAwareStreamBridge.java` | 唯一启用的生产者自动设头组件（`X-Tenant-Id`） |
| `bytter-framework/.../queue/TenantAwareFunctionWrapper.java` | 消费者自动还原（**注释失效**） |
| `bytter-framework/.../queue/TenantAwareFunctionInterceptor.java` | 同上（注释） |
| `bytter-framework/.../queue/TenantAwareFunctionPostProcessor.java` | 同上（`@ConditionalOnMissingBean` 无 `@Primary`，永不生效） |
| `bytter-framework/.../queue/TenantAwareFunctionRegistrationListener.java` | 同上（"无法启动"） |
| `bytter-framework/.../queue/TenantAwareFunctionCatalogConfig.java` | 同上（"无效果"） |
| `bytter-framework/.../queue/TenantHeaderConstant.java` | 头常量（**整段注释**） |
| `bytter-framework/.../tenant/SmartThreadPoolManager.java` | TTL 包装线程池（L77-90） |
| `bytter-framework/.../common/context/UserContextHelper.java` | `TransmittableThreadLocal` 用户/租户上下文 |
| `bytter-framework/.../jdbc/TenantConnection.java` | JDBC 层租户改写（`tenantId==null` 不过滤） |
| `bytter-system/.../producer/InitNetDayCloseMQProducer.java` | 场景1 生产者（安全通道） |
| `bytter-manager-account/.../consumer/InitNetDayCloseEvent.java` | 场景1 消费者 |
| `bytter-manager-account/.../mq/listener/RecordBookingEvent.java` | 场景3 消费者 |
| `bytter-service-payment/.../mq/listener/FinacialToPaymentEvent.java` | 场景2 消费者 |
| `bytter-prepose-bill/.../mq/listener/BillEndorsementToPaymentEvent.java` | 场景4 消费者 |
| `bytter-common-base/.../bpm/context/WorkflowCallBackEvent.java` | 场景5 消费者 |
| `bytter-workflow/.../service/FlowMessageService.java` | 场景5 生产者（裸发+手动设头） |
| `bytter-manager-account/.../mq/message/service/ProducerMessageRecordJob.java` | 场景6 可靠重发 |
| `bytter-service-payment/.../netbank/service/NisBillheadService.java` | 场景4 生产者出口（`local.massage.billendorsement` 开关） |
| `*.yml` (`spring.cloud.stream.bindings`) | 各通道/分组/路由配置 |

---

## 8. 结论

1. **当前"活跃" MQ 链路（场景 1–3、5、6）租户传递是健康的**：全部经由 `TenantAwareStreamBridge` 或显式设头，消费者均手写 `setTTLTenant` + `finally` 清理。
2. **但整条链路的"安全性"完全建立在人工约定之上**：框架本应提供的消费者自动还原与强制校验**已全部禁用**，一旦新增消息或重构漏写任意一环，即触发 **R1 静默跨租户泄露**（因 `TenantConnection` 对 null 租户不过滤）。
3. **最紧迫的两项加固**：① 消费者 `tenantId==null` 改为 **fail-fast 拒绝**（P0）；② 收敛生产者出口为 `TenantAwareStreamBridge` 唯一通道（P0）。二者即可把"无感知安全事故"转为"可观测故障"。
4. **次要但需关注**：消费者无用户上下文（R3）、异步子线程丢租户（R4）、生产者出口不统一（R5）。

> 与《登录与租户隔离方案说明.md》的结论一致：租户隔离的"最后一道闸"是 `TenantConnection` 的 null 处理，**任何导致 `tenantId==null` 进入数据层的路径都必须视为高危并 fail-fast**，MQ 链路是其中最易遗漏的入口之一。
