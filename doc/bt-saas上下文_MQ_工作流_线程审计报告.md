# bt-saas 登录上下文、Dubbo、MQ、工作流与线程审计报告

> 源码范围：`D:\WorkSpaces\Bytter\bt-saas` 下全部模块  
> 审计方式：基于当前工作区源码静态分析，审计日期 2026-07-25  
> 重要说明：仓库当前有用户未提交修改 `TenantProperties.java`，本次仅只读分析，未修改源码。

## 1. 执行摘要

项目采用“共享数据库、共享表、`TENANT_ID` 行级隔离”方案。租户上下文最终由
`TenantConnection` 在 JDBC 层读取并改写 SQL。因此，所有风险最终都汇聚到同一个
关键事实：

> `UserContextHelper.getTenantId()` 返回 `null` 时，`TenantConnection` 不会拒绝
> SQL，而是直接执行原始 SQL。上下文丢失不是普通功能异常，而可能变成静默的跨租户
> 查询、更新或插入。

综合结论：

| 领域 | 当前结论 | 风险 |
|---|---|---|
| HTTP 登录链路 | OAuth2/JWT → Gateway → Header → MVC 拦截器 → TTL，主链路完整 | 中高 |
| Dubbo 同步调用 | Consumer attachment 传递完整用户或租户，Provider 同步线程可回退读取 | 中 |
| Dubbo 内部异步 | Provider 未把 attachment 写入 TTL，新线程无法读取 RpcContext | **高** |
| MQ | 5 类实际业务链路；生产、消费均依赖手工约定，空租户仍继续处理 | **高** |
| 工作流 | Camunda 部署带 tenantId，但引擎租户校验关闭，很多查询不带 tenant 条件 | **高** |
| XXL-Job | 138 个活跃任务被全局多租户切面包装，主流程会逐租户设置并清理上下文 | 低到中 |
| 自建线程池/`@Async` | 通用 `ThreadPoolTaskExecutor` 无 TTL 包装或 TaskDecorator；大量固定池/裸线程 | **高** |

必须优先处理的四项：

1. `TenantConnection` 对空租户改为 fail-fast，跨租户任务必须走显式授权接口。
2. MQ 消费者缺少/非法 tenant header 时拒绝消费，不能“warn 后继续”。
3. Camunda 开启租户校验，并给所有 Task/Runtime/History 查询补 tenant 条件。
4. 全项目异步入口统一使用 TTL 包装执行器；禁止业务代码自建固定池和裸线程。

## 2. 登录、用户上下文与租户隔离

### 2.1 登录及上下文主流程

```text
客户端
  → Authorization / Access-Token
Gateway.AccessGatewayFilter
  → 校验 Token、过期时间和 URL 权限
  → x-client-token-user = JWT subject 中的 ShiroUser JSON
  → X-Tenant-ID = JWT tenantId claim
业务服务 UserInterceptor
  → 解析 x-client-token-user
  → UserContextHelper.setUser(ShiroUser)
  → 同时写入用户 TTL 和租户 TTL
业务 DAO
  → TenantConnection.getTenantId()
  → TenantSQLHandler 给 SQL 注入 TENANT_ID 条件
请求结束
  → UserInterceptor.afterCompletion.removeUser()
```

关键实现：

| 环节 | 文件与位置 | 行为 |
|---|---|---|
| JWT 写入用户、租户 | `CustomTokenEnhancer.java:34-44` | `sub`、`uid`、`tenantId` 写入 token |
| 网关鉴权 | `AccessGatewayFilter.java:67-122` | 校验 token/权限并下发两个 Header |
| Web 上下文装配 | `UserInterceptor.java:37-56` | 请求开始先清理，再解析用户并 `setUser` |
| Web 上下文清理 | `UserInterceptor.java:74-77` | `afterCompletion` 清理用户和租户 TTL |
| 上下文载体 | `UserContextHelper.java:23-32` | 两个 `TransmittableThreadLocal` |
| 租户解析链 | `UserContextHelper.java:163-175` | tenant TTL → Dubbo ttlTenant → ShiroUser |
| SQL 拦截 | `TenantConnection.java:41-52` 等 | tenant 非空才改写，空值直接放行 |

### 2.2 上下文语义

`UserContextHelper` 内有两个上下文：

- `threadLocal<ICurrentUser>`：当前用户；
- `tenantThreadLocal<String>`：当前租户。

几个方法不能混用：

- `getTTLTenant()` 只读取 tenant TTL；
- `getTenantId()` 会继续回退到 Dubbo attachment 和 ShiroUser；
- `getCurrentUser()` 会回退到 Dubbo `shiroUser` attachment；
- `getLocalUser()` 只读本地 TTL；
- `removeUser()` 同时清理用户和租户；
- `clearTTLTenant()` 只清理租户。

因此 Provider 同步线程中可能出现：

```text
getTenantId()    → 有值
getTTLTenant()   → null
```

业务代码或基础组件若使用 `getTTLTenant()` 作为统一租户读取接口，会误判为没有租户。
`TenantAwareStreamBridge.send(data, headers)` 当前也使用 `getTTLTenant()`，所以在 Dubbo
Provider 同步方法中直接发送 MQ，即使 `getTenantId()` 有值，也可能漏掉租户头。

### 2.3 租户隔离实现及失效条件

项目通过 `TenantDataSource` 包装连接，再由 `TenantConnection` 拦截
`prepareStatement`、`prepareCall` 和 `createStatement`。`TenantSQLHandler` 使用
jsqlparser 给业务 SQL 注入租户条件。

高危逻辑位于：

- `TenantConnection.java:41-52`
- `TenantConnection.java:56-66`
- `TenantConnection.java:164-174`
- `TenantConnection.java:240-307`

这些重载均采用：

```java
if (tenantId != null) {
    // 改写 SQL
}
return delegate.prepareStatement(sql);
```

租户丢失后不会报错，也不会自动使用“无结果租户”，而是绕开隔离执行原 SQL。

### 2.4 登录链路风险

#### L1 高危：JWT 验签密钥硬编码

`bytter-gateway/.../AuthServiceImpl.java` 使用硬编码 `"ByTtEr"` 解析 JWT，而不是已注入
的配置值。获得源码或密钥的人可伪造任意用户和租户。

#### L2 高危：服务可绕过网关时可伪造用户头

`UserInterceptor.java:41-54` 直接信任 `x-client-token-user`，业务服务不验签。
如果服务端口可被直接访问，调用方可以构造任意 `ShiroUser`。

#### L3 高危：空租户 SQL 直接放行

这是 MQ、线程、定时任务和工作流所有上下文丢失问题的放大器。

#### L4 中：网关下发两个租户来源但 Web 只使用一个

网关同时下发 `X-Tenant-ID` 和包含 tenantId 的 `x-client-token-user`，Web 拦截器只解析
后者。两个 Header 不一致时没有校验。

#### L5 中：白名单请求无上下文仍可能进入业务数据层

`BytterWebMvcConfig.java:13-17` 排除 `/login`、`/oauth/**`、静态资源等。若这些路径或
后续新增白名单接口访问业务表，将处于无租户状态。

## 3. Dubbo 上下文传递

### 3.1 Consumer 侧

`DubboTokenFilter.java:17-42`：

1. 读取 `getCurrentUser()` 和 `getTTLTenant()`；
2. 有用户时只写 `shiroUser` attachment；
3. 没有用户、但有 TTL tenant 时写 `ttlTenant`；
4. 调用结束清空 ClientAttachment。

有用户时虽然没有单独写 `ttlTenant`，Provider 可从 `shiroUser.tenantId` 回退获取，
所以同步调用通常正常。

### 3.2 Provider 侧

`DubboUserProviderFilter.java:19-35` 当前生效代码只读取 attachment，未执行：

```java
UserContextHelper.helper().setUser(...)
UserContextHelper.helper().setTTLTenant(...)
```

业务同步执行时，`UserContextHelper` 通过 `RpcContext.getServerAttachment()` 回退读取。
方法返回后 `RpcContext.removeContext()`。

### 3.3 风险结论

| 场景 | 用户 | 租户 | 结论 |
|---|---|---|---|
| Web → 同步 Dubbo Provider | 可从 attachment 读取 | 可从 attachment/用户读取 | 通常安全 |
| 只有 tenant TTL 的内部调用 | 无用户 | `ttlTenant` 可读取 | 租户安全、审计用户缺失 |
| Provider → 下一跳同步 Dubbo | Consumer filter 可用 `getCurrentUser()` 回退 | 通常可继续携带 | 通常安全 |
| Provider → `@Async`/固定池 | 丢失 | **丢失** | 高危 |
| Provider → `TenantAwareStreamBridge.send(data)` | 无法用 `getTTLTenant()` 回退 | 可能不写 Header | 高危 |
| Provider 内业务使用 `getTTLTenant()` | — | 返回 null | 中高 |

核心问题是：`TransmittableThreadLocal` 只能传递已经写入 TTL 的数据。Provider 当前
只有 RpcContext attachment，TTL 为空，所以即使下游执行器正确进行了 TTL 包装，
也没有内容可捕获。

建议 Provider filter 在进入业务前把 attachment 显式转换为 TTL，finally 中按快照
恢复；保存“原始上下文”时必须用 `getLocalUser()`/`getTTLTenant()`，不能使用会回退
RpcContext 的 getter。

## 4. MQ 场景与风险

### 4.1 技术方案

- Spring Cloud Stream 函数式模型；
- RabbitMQ Binder；
- 租户 Header：`X-Tenant-Id`；
- 推荐生产入口：`TenantAwareStreamBridge`；
- 消费者目前逐个手写 `setTTLTenant`/`clearTTLTenant`；
- 自动包装消费者的 `TenantAwareFunction*` 方案均已注释或未生效。

### 4.2 全量业务场景

| 场景 | Exchange / binding | 生产者 | 消费者 | 核心业务 | 租户结论 |
|---|---|---|---|---|---|
| 日结初始化 | `system-initNetDayClose-exchange` | `InitNetDayCloseMQProducer` | `InitNetDayCloseEvent` | 系统通知账务初始化网点日结日期 | 手工头+手工恢复 |
| 账务→付款 | `finacial-exchange` | 财务 `ProducerMessageRecordJob` | `FinacialToPaymentEvent` | 账务事务消息驱动付款侧处理 | 记录中的 tenantId 发头 |
| 付款→账务 | `payment-finacial-exchange` | 付款 `ProducerMessageRecordJob`、测试控制器 | `RecordBookingEvent` | 自动记账/回传 | 主要链路有 tenantId |
| 付款→票据 | `payment-bill-exchange` | 付款 `ProducerMessageRecordJob` | `BillEndorsementToPaymentEvent` | 付款结果联动票据背书 | 记录中的 tenantId 发头 |
| 工作流→业务服务 | `workflow-mq-direct` | `FlowMessageService` | 各业务模块公共 `WorkflowCallBackEvent` | 审批完成/撤回回调业务单据 | 裸 StreamBridge 手工设头 |
| 指令状态变更（弃用） | `bis-exc-topic` | increment `BisMQService` 整体注释 | payment `BisExcStatusChangeEvent` | 指令状态联动付款业务 | 消费者无租户恢复 |

额外配置异常：

- `bytter-prepose-accountReceivable/.../application.yml:23-35` 配置
  `billEndorsementToPayment`，但该模块源码中没有对应 Consumer Bean；
- `bytter-prepose-bill/.../application.yml:23-35` 才有实际
  `BillEndorsementToPaymentEvent`；
- `bisExcEvent` Bean 仍然存在，但 payment 的 `function.definition` 未包含它，生产者又
  已整类注释，属于残留接线；
- `BankAccConfigController`、`FcAccbookBlnedController` 中存在裸
  `streamBridge.send` 测试入口，消息没有租户头。若接口可达，会直接触发无租户消费。

### 4.3 各链路核心代码

#### 日结

```text
InitNetDayCloseMQProducer
  → TenantAwareStreamBridge.send(..., getTenantId(), headers)
  → InitNetDayCloseEvent
  → header 读取 tenant
  → setTTLTenant
  → fcDayCloseService.initNetCloseDate
  → finally clearTTLTenant
```

#### 财务与付款可靠消息

```text
业务事务
  → ProducerMessageRecordService 保存消息、tenantId、exchange、routingKey
XXL-Job ProducerMessageRecordJob
  → 读取未发送记录
  → TenantAwareStreamBridge.send(..., record.tenantId, headers)
消费者
  → 幂等消息记录
  → 业务 handler
  → 更新处理状态
```

重发时不依赖 Job 线程当前租户，而使用持久化记录的 tenantId，这一点设计正确。
但原始记录落库时 tenantId 为空，错误会被持久化并持续重放。

#### 工作流回调

```text
Camunda ProcessEventListener
  → FlowMessageService
  → 按 cateKey 查 serviceId
  → StreamBridge.send
     Header service-name
     Header X-Tenant-Id = getTenantId()
业务服务 WorkflowCallBackEvent
  → setTTLTenant
  → bt_message_repeat 幂等
  → WorkFlowHelper 找到 IWorkFlowListener
  → flowCompleted / flowRevoke
  → 更新工作流消息状态
  → finally clearTTLTenant
```

### 4.4 MQ 风险

#### MQ1 高危：空租户只告警，仍继续业务

所有主要消费者在 tenantId 为空时都是 `log.warn` 后继续。随后发生的查询、幂等记录
和业务更新可能全部绕过租户隔离。

#### MQ2 高危：生产出口不统一

项目同时存在 `TenantAwareStreamBridge` 和裸 `StreamBridge`。后者依赖开发者手工设
Header。工作流当前手工设置正确，但没有框架强制。

#### MQ3 高危：默认 send 方法使用 `getTTLTenant()`

Provider 同步 Dubbo 场景可能只有 RpcContext tenant，没有 tenant TTL。调用
`TenantAwareStreamBridge.send(binding, data)` 可能仍然漏头。应改用统一
`getTenantId()` 并强制非空。

#### MQ4 中高：只恢复租户，不恢复用户

消费者中的 `getCurrentUser()`、`getUserId()` 为空，审计字段、权限校验或业务操作人
逻辑可能异常。`WorkflowRemoteEvent` 的 payload 带 `CurrentUser`，但没有统一写回
用户上下文。

#### MQ5 中：消费者提前 return 的清理不一致

大多数消费者把业务处理包在 try/finally 中；应统一由框架外层清理，避免未来新增
return/异常路径遗漏。

#### MQ6 中：accountReceivable 接线与代码不一致

同名函数和不同 exchange 的配置容易造成启动期函数查找错误、误消费或维护人员将
错误链路重新启用。

## 5. 工作流方案与上下文风险

### 5.1 技术和模块

- 引擎：Camunda 7；
- 接口：`IWorkFlowService` 通过 Dubbo 暴露；
- 模型发布：`FlowModelService`；
- 业务适配：`IWorkFlowStartedListener`、`IWorkFlowListener`；
- 回调：Spring Cloud Stream + RabbitMQ；
- 注册：`WorkFlowHelper` 按流程分类 key 找到业务 Listener。

全仓共有约 212 个业务类直接实现工作流 Listener 接口，主要分布：

| 模块 | Listener 数量 |
|---|---:|
| bytter-prepose-bill | 135 |
| bytter-service-payment | 31 |
| bytter-service-creditRating | 12 |
| bytter-prepose-credential | 10 |
| bytter-manager-plan | 9 |
| 其他模块 | 15 |

### 5.2 启动流程

```text
业务 Service 实现 IWorkFlowStartedListener
  → getStartHandler().handler(cateKey, businessKey, variables, corpId, userId)
WorkFlowStartHandler
  → hasApproveModel(cateKey, corpId)
  ├─ 无模型：本地构造 PASS，直接 flowCompleted
  └─ 有模型：
      → 捕获 currentUser 和 tenantId
      → 注册事务 afterCommit
      → afterCommit 显式 setTTLTenant
      → Dubbo IWorkFlowService.start
      → finally clearTTLTenant
WorkFlowServiceImpl.start
  → 查分类和已发布模型
  → identityService.setAuthenticatedUserId
  → runtimeService.startProcessInstanceById
```

`afterCommit` 对 tenantId 的捕获和恢复是正确的；但用户不是写入 TTL，而是通过
`startUserId` 显式传参，属于“租户上下文 + 用户参数”的混合方案。

### 5.3 审批与回调

```text
TaskController / MobileApiController
  → IWorkFlowService.complete(input, currentUser)
WorkFlowServiceImpl.complete
  → 查询 task
  → 设置 authenticated user
  → taskService.complete / reject / terminate
Camunda ProcessEventListener
  → FlowMessageService.sendWorkFlowEvent
RabbitMQ
  → 业务服务 WorkflowCallBackEvent
  → IWorkFlowListener.flowCompleted / flowRevoke
```

### 5.4 工作流租户隔离现状

正面措施：

- `FlowModelService.java:187` 发布 deployment 时设置当前 tenantId；
- `ProcessInstanceService.java:58` 有一处显式 `tenantIdIn`；
- 回调消息显式携带 tenant Header；
- `WorkFlowStartHandler.afterCommit` 显式恢复 tenant。

高危缺口：

1. `application.yml:25` 配置 `camunda.bpm.authorization.tenant-check-enabled: off`；
2. `WorkFlowServiceImpl.start` 按 businessKey + processDefinitionId 查重复，没有 tenant；
3. `complete` 按调用方提交的 taskId 查询，没有 tenant；
4. `getTaskIdsByBusinessKeysAndCateKey` 没有 tenant；
5. `listAllCommentsByBusinessId` 的历史查询没有 tenant；
6. 多个 Runtime/Task/History 查询只按业务 ID、任务 ID、用户 ID过滤；
7. 工作流 ACT 表通常需要从 JDBC 通用租户 SQL 改写中排除，否则 Camunda 自身 SQL
   会被错误改写；仓库内实际排除配置来自 Nacos `common.yml`，无法仅凭仓库确认。

这意味着即使 deployment 写了 tenantId，引擎层也没有形成一致的强制隔离。若攻击者
可提交其他租户的 taskId/businessKey，或业务 ID 在租户间重复，存在跨租户查看、
审批、撤回或重复判断的风险。

### 5.5 工作流上下文丢失风险

| 风险 | 等级 | 说明 |
|---|---|---|
| Camunda tenant-check 关闭 | 高 | 引擎不会自动限制当前认证租户 |
| 查询普遍未带 tenant 条件 | 高 | taskId/businessKey 查询可跨租户命中 |
| 回调空租户继续执行 | 高 | MQ Header 缺失时业务回调绕过 JDBC 隔离 |
| 流程事件依赖当前线程 tenant | 中高 | Camunda 异步 Job Executor 启用后不会天然有 Web/Dubbo 上下文 |
| `identityService` 只设置用户未设置认证租户 | 中高 | 引擎授权上下文不完整 |
| 用户与租户传递方式混合 | 中 | 一部分显式参数，一部分 TTL/attachment，维护易遗漏 |
| 事务后回调异常无法回滚原业务 | 中 | afterCommit 启动失败只能补偿，不能回滚原单据 |

建议给 `IWorkFlowService` 所有入口增加统一租户参数/上下文断言，所有 Camunda Query
统一调用 `tenantIdIn(currentTenant)`，并在接口层校验任务/实例的 tenantId 与当前租户
完全一致。

## 6. 线程、异步和定时任务审计

### 6.1 上下文传递的真实能力

项目用了 `TransmittableThreadLocal`，但仅声明 TTL 变量并不足以让已有线程池自动
传递上下文。必须满足以下之一：

- 执行器经 `TtlExecutors` 包装；
- 提交任务时使用 `TtlRunnable`/`TtlCallable`；
- Spring 执行器配置捕获/恢复上下文的 `TaskDecorator`；
- JVM 通过 TTL agent 做增强。

仓库仅发现 `SmartThreadPoolManager` 使用 `TtlExecutors`，未发现 TTL javaagent，也未
发现 `TaskDecorator`。所以普通 `ThreadPoolTaskExecutor`、`Executors.newFixedThreadPool`
和裸线程不能视为安全。

### 6.2 安全或相对安全的线程方案

#### SmartThreadPoolManager

`SmartThreadPoolManager.java:69-90` 创建 fast/normal/slow 三组池，并通过
`TtlExecutors.getTtlExecutorService` 包装。`submitForTenant(s)` 还显式设置、清理 tenant。

注意：`submitForTenant` finally 直接 `clearTTLTenant()`，没有恢复调用前旧值。嵌套
租户任务或 CallerRunsPolicy 在调用线程执行时可能清掉父上下文，建议使用快照恢复。

#### TenantShardingExecutor + MultiTenantJobAspect

`MultiTenantJobAspect.java:43-63` 拦截所有活跃 `@XxlJob`。默认将任务包装为多租户任务，
`TenantShardingExecutor.java:409-451` 对每个租户：

1. 创建独立事务；
2. `setTTLTenant(tenantId)`；
3. 执行业务 Job；
4. flush；
5. finally 清理 tenant。

当前多租户执行主路径是串行循环；被注释的并行方案和 `executeForTenantAsync` 使用
`SmartThreadPoolManager`。

仓库共发现 138 个活跃 `@XxlJob`：

| 模块 | 数量 |
|---|---:|
| bytter-service-payment | 51 |
| bytter-prepose-bill | 41 |
| bytter-manager-account | 11 |
| bytter-system | 8 |
| bytter-prepose-check | 8 |
| bytter-service-bdlink | 7 |
| 其他 8 个模块 | 12 |

风险边界：

- 标注 `@MultiTenantJob(enabled=false)` 的任务会直接执行原方法，没有租户；
- AOP 未生效、自调用或 Bean 未被 Spring 代理时可能绕过切面；
- Job 内再次使用不安全线程池，子任务仍会丢租户；
- Job Admin 自身监控线程属于调度基础设施，通常不访问 SaaS 业务表。

### 6.3 通用 Spring 异步池

`ExecuteConfig.java:50-70` 的 `asyncThreadPoolExecutor` 和
`SystemExecuteConfig.java:51-96` 的 `asyncDslPoolExecutor`、`logPoolExecutor`
都是普通 `ThreadPoolTaskExecutor`，没有 TTL 包装和 TaskDecorator。

直接使用它们的主要业务：

| 文件 | 场景 | 风险 |
|---|---|---|
| `FcInterestDtlRuleService.java:88-96` | 利息明细自动认领并行 | 高 |
| `AutoSyncSysFrzBalJob.java:58-65` | 冻结余额并行同步 | 高 |
| `BisCorpPoolConfigRuleService.java:77-86` | 资金池规则并行 | 高 |
| `NisFlowRuleService.java:335-337` | 资金划转自动认领 | 高 |
| `DslSsoSyncUserService.java:124-135,297-310` | SSO 用户同步 | 中高 |

若这些任务在 XXL-Job tenant 上下文中启动子任务，父线程 tenant 不会可靠进入池线程。

### 6.4 `@Async`

活跃调用点：

| 文件 | 位置 | 业务 |
|---|---:|---|
| `LogAopService.java` | 22 | 异步日志，指定 `logPoolExecutor` |
| `UserPermissionAsyncService.java` | 42 | 用户权限并行计算 |
| `NotificationServiceImpl.java` | 246、262 | 异步完成/发送通知 |

这些执行器没有 TTL 装饰。日志和通知可能只依赖显式参数，但一旦内部 DAO 或 Dubbo
调用依赖 `UserContextHelper`，会发生租户或用户丢失。`@Async` 未指定 executor 时还
存在默认执行器选择不明确的问题。

### 6.5 业务自建固定线程池

以下是明确的业务线程池创建点，均未 TTL 包装：

| 文件 | 位置 | 场景 |
|---|---:|---|
| `FbsCorpItemDao.java` | 39、277 | 预算项目并行查询 |
| `FbsBudgetService.java` | 117、1599 | 预算流程/任务并行 |
| `FbsCorpItemService.java` | 89、212 | 预算项目处理 |
| `RefundBlendingTaskService.java` | 61 | 退款核对 |
| `BisAccBalAutoServiceImpl.java` | 67、113 | 余额自动处理 |
| `BisBankReceiptBlendingServer.java` | 221、281 | 回单核对 |
| 两套 `NisBillheadController.java` | 163/164、1211-1261 | 付款单批量处理 |
| 两套 `NisBillheadPoolController.java` | 97/106、517/556 | 付款池批量处理 |
| `TpPaymentController.java` | 94、561 | 第三方支付批处理 |
| `RasAuditService.java` | 702-730 | RAS 审计并行 |
| `RasAuditTaskService.java` | 705-726 | RAS 审计任务并行 |
| `JdbcDataProvider.java` | 167-187 | 报表数据加载 |
| `OssAttachmentStrategyImpl.java` | 283 | 附件并行处理 |

其中控制器静态线程池最危险：任务由 Web 请求提交，但池线程长期存在，无法继承每次
请求的 tenant TTL；若任务内部访问数据库，将进入无租户 SQL。

### 6.6 裸线程

业务相关裸线程：

| 文件 | 位置 | 场景 | 租户风险 |
|---|---:|---|---|
| `AbBillAccBalService.java` | 1334 | 票据余额后台处理 | 高 |
| `FundWarnTimelyService.java` | 60 | 实时预警 | 中；代码手工 set/clear tenant |
| `JdbcDataProvider.java` | 180 | 报表加载 | 高 |
| `PersistService.java` | 80 | 报表持久化 | 高 |
| `MessageConsumer.java` | 48、74 | 系统消息消费 | 中高 |
| `PushService.java` | 205 | 推送 | 中 |
| `WebSocketManager.java` | 69 | Redis 跨节点订阅 | 通常无请求租户，业务处理须显式携带 |

基础设施裸线程：

- `bytter-job/.../core/thread/*`：XXL-Job Admin 调度、回调、注册和监控线程；
- `TimeoutUtils`：超时调度线程；
- `TenantShardingExecutor`：JVM shutdown hook；
- `SmartThreadPoolManager`：线程工厂。

基础设施线程本身通常不应携带租户，关键要求是它们不能直接访问租户业务数据。

### 6.7 Parallel Stream

发现 4 处 `.parallel()`：

- framework `XssCleanRuleUtils.java:114,147`
- gateway `XssCleanRuleUtils.java:123`
- gateway `SqlInjectionUtil.java:63`

这些只做字符串安全检查，不访问租户数据，当前风险低。但 ForkJoin commonPool 不传
上下文，不应把该写法复制到 DAO 或业务处理。

### 6.8 线程风险分级

#### T1 高危：普通线程池未 TTL 包装

项目名称上使用了 `TransmittableThreadLocal`，但大多数池没有接入 TTL 捕获机制。
“已经用了 TTL”会造成错误安全感。

#### T2 高危：Web 控制器静态线程池

请求上下文不会随任务进入长期存在的固定池。批量付款类任务尤其需要逐条验证 DAO
是否显式带 tenant。

#### T3 高危：Dubbo Provider 内切线程

Provider 本地 TTL 本来就是空的，任何异步执行器都无法自动捕获 RpcContext tenant。

#### T4 中高：清理而不是恢复

多个组件 finally 使用 `clearTTLTenant()`，如果任务在 CallerRunsPolicy、嵌套调用或
已有父上下文线程中执行，会清掉外层上下文。应保存旧值并恢复。

#### T5 中：异步任务只传 tenant，不传 user

即使 tenant 隔离正确，`createBy/updateBy`、权限、审计日志仍可能缺操作人。

## 7. 整改方案

### P0：立即阻断跨租户静默执行

1. `TenantConnection` tenant 为空时抛出 `MissingTenantContextException`。
2. 为确需跨租户的系统任务提供显式 `runWithoutTenant(reason, permission)`，不能以
   “没有上下文”代表“超级权限”。
3. MQ Consumer Header 缺失或非法时抛异常进入 DLQ，并告警。
4. 工作流 complete/revoke/query 接口按当前 tenant 校验 task/process/deployment。

### P1：统一跨边界上下文

1. 定义不可变 `ContextSnapshot {tenantId,userId,loginName,traceId}`。
2. HTTP、Dubbo、MQ、线程和 Job 都用同一套 capture/install/restore API。
3. Dubbo Provider filter 将 attachment 安装到 TTL，finally 恢复快照。
4. `TenantAwareStreamBridge` 改用 `getTenantId()`，tenant 为空禁止发送。
5. 使用全局 Spring Cloud Stream interceptor 包装所有消费者。

### P1：线程池治理

1. 给所有 `ThreadPoolTaskExecutor` 设置 TTL `TaskDecorator`。
2. 封装唯一 `ContextAwareExecutor`，禁止 `Executors.newFixedThreadPool` 和裸线程。
3. 替换控制器静态池。
4. finally 从“clear”改为“恢复提交前快照”。
5. 为所有异步入口增加“任务内 tenant 与提交线程一致”的测试。

### P1：Camunda 租户治理

1. 开启 `tenant-check-enabled`。
2. 建立 Camunda `IdentityService` 认证用户和认证租户。
3. 封装所有 Runtime/Task/History/Repository 查询，默认强制 `tenantIdIn`。
4. 流程定义、实例、任务和业务表 tenant 必须交叉校验。
5. Camunda 异步 Job Executor 若启用，把 tenant 存入流程变量，并在 Job 执行前恢复。

### P2：认证与可观测性

1. 移除 JWT 硬编码密钥，使用配置中心或非对称密钥。
2. 业务服务限制只能经网关访问，或验证内部签名/mTLS。
3. 日志统一输出 tenantId、userId、traceId、线程池名、MQ msgId。
4. 建立跨租户安全测试：null tenant、伪造 Header、其他租户 taskId、异步子线程、
   MQ 漏头、Dubbo Provider 异步。

## 8. 关键文件索引

### 登录、租户、Dubbo

- `bytter-auth-parent/bytter-auth/.../CustomTokenEnhancer.java`
- `bytter-gateway/.../AccessGatewayFilter.java`
- `bytter-gateway/.../AuthServiceImpl.java`
- `bytter-common-base/bytter-framework/.../UserInterceptor.java`
- `bytter-common-base/bytter-framework/.../UserContextHelper.java`
- `bytter-common-base/bytter-framework/.../DubboTokenFilter.java`
- `bytter-common-base/bytter-framework/.../DubboUserProviderFilter.java`
- `bytter-common-base/bytter-framework/.../TenantConnection.java`
- `bytter-common-base/bytter-framework/.../TenantSQLHandler.java`
- `bytter-common-base/bytter-framework/.../TenantProperties.java`

### MQ

- `bytter-common-base/bytter-framework/.../TenantAwareStreamBridge.java`
- `bytter-common-base/bytter-framework/.../TenantAwareFunction*.java`
- `bytter-manager-account/.../InitNetDayCloseEvent.java`
- `bytter-manager-account/.../RecordBookingEvent.java`
- `bytter-manager-account/.../ProducerMessageRecordJob.java`
- `bytter-service-payment/.../FinacialToPaymentEvent.java`
- `bytter-service-payment/.../ProducerMessageRecordJob.java`
- `bytter-prepose-bill/.../BillEndorsementToPaymentEvent.java`
- `bytter-workflow/.../FlowMessageService.java`
- `bytter-common-base/bytter-framework/.../WorkflowCallBackEvent.java`

### 工作流

- `bytter-common-base/bytter-framework/.../WorkFlowStartHandler.java`
- `bytter-common-base/bytter-framework/.../WorkFlowHelper.java`
- `bytter-workflow-client/.../IWorkFlowService.java`
- `bytter-workflow-domain/.../WorkFlowServiceImpl.java`
- `bytter-workflow-domain/.../ProcessEventListener.java`
- `bytter-workflow-domain/.../FlowModelService.java`
- `bytter-workflow-domain/.../ProcessInstanceService.java`
- `bytter-workflow-start/src/main/resources/application.yml`

### 线程与任务

- `bytter-common-base/bytter-framework/.../ExecuteConfig.java`
- `bytter-common-base/bytter-framework/.../SmartThreadPoolManager.java`
- `bytter-common-base/bytter-framework/.../TenantShardingExecutor.java`
- `bytter-common-base/bytter-framework/.../MultiTenantJobAspect.java`
- `bytter-system/.../SystemExecuteConfig.java`
- 第 6.5、6.6 节所列全部业务文件。

## 9. 最终判断

项目主链路已经具备租户传递意识，尤其是 Web 拦截器、Dubbo attachment、可靠消息表
tenantId 持久化和 XXL-Job 多租户切面。但当前隔离安全仍然依赖“每个开发者在每个
异步边界手工做对”，缺少 fail-safe：

- 有上下文时多数场景正确；
- 上下文缺失时系统不会阻断；
- JDBC、MQ、工作流和线程池会把一次遗漏放大为跨租户风险。

所以本项目确实存在租户丢失问题，而且主要集中在 Dubbo Provider 异步、自建线程池、
`@Async`、MQ 空 Header 和 Camunda 未带 tenant 的查询。整改应先建立“空租户绝不执行
租户业务 SQL”的底线，再逐步统一上下文传播。
