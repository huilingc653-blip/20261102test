### 定时任务调度中心服务概述

**定位**：本服务是 **xxl-job 2.3.1 admin 二次开发**的**调度中心（调度与管理台）**，**不是执行器**——
负责定时任务的**配置管理、周期调度触发、执行器注册发现、执行结果回调落库、失败重试与邮件告警、调度日志与报表审计**。
业务任务的执行代码全部在各业务服务（执行器）中，本服务只负责「何时、由谁、以什么策略」触发。

- 本模块内**没有任何 `@XxlJob` 注解**（业务 `@XxlJob` 均在业务服务侧，全仓 ≥133 处）；
- **无 Dubbo、无 MQ**：与执行器之间的全部沟通走 **HTTP**（xxl-job 自有 `/api/*` 回调协议）。

#### 代码分层

```
- bytter-job（单模块服务，与其它 bytter-* 的 DDD 四模块结构不同）

    src/main/java/com/bytter/job/admin
      - controller/ 管理端 REST（jobinfo / jobgroup / joblog / jobcode / jobpermission / user / loginlog /
                    operatelog）+ JobApiController（/api/{callback|registry|registryRemove}）
                    + annotation/（@OperationLog、@PermissionLimit）、interceptor/（登录与权限拦截）、resolver/
      - core/       调度核心：scheduler（XxlJobScheduler 统一启动）、thread（6 个守护线程）、trigger（XxlJobTrigger）、
                    route/strategy（路由策略）、alarm/impl（邮件告警）、complete/（XxlJobCompleter）、conf/、cron/、
                    enums/、old/（历史 Quartz/xxl-rpc 版本，整文件注释、未启用）
      - dao/        @Mapper 接口（11 个，对应 mysql-mapper/ 与 oracle-mapper/ 两套 XML）
      - service/    编排服务（XxlJobServiceImpl、JobInfoSortServiceImpl、LoginService 等）

    src/main/resources
      - mysql-mapper/ 、oracle-mapper/   MyBatis XML（双数据库方言）
      - i18n/（中/繁/英）、static/、application*.yml
```

#### 领域业务表（11 张）

> 全部为 **MyBatis Mapper + 原生 SQL** 承载（**无 JPA 实体、无 MyBatis-Plus 实体**），故**扫描账本为 0 张**，
> 下表由建表脚本 `sql/saas/mysql/1.Init-tables.sql:22101-22295`（Oracle 侧 `sql/oracle/other-model_ddl.sql:3739-3885`）
> **手工补入**账本；11 张表**均带 `TENANT_ID` + `IDX_TENANT_ID`**（多租户二次开发）。

| 分组 | 表名 | 中文说明 | 备注 |
|---|---|---|---|
| 任务与执行器 | XXL_JOB_INFO | 调度任务表 | 调度类型/配置、过期策略、路由与阻塞策略、handler/param、超时、失败重试、GLUE 源码、child_jobid 子任务、trigger_status |
| 任务与执行器 | XXL_JOB_GROUP | 执行器组表 | app_name、address_type（0 自动注册/1 手动录入）、address_list |
| 任务与执行器 | XXL_JOB_LOGGLUE | GLUE 源码历史表 | GLUE 源码热更新备份（JobCodeController.save） |
| 任务与执行器 | XXL_JOB_LOCK | 调度分布式锁表 | `schedule_lock`，仅 `JobScheduleHelper` 原生 SQL 使用（无 Mapper/实体） |
| 调度日志与注册 | XXL_JOB_LOG | 调度日志表 | 每次触发行：trigger_code/msg → handle_code/msg、executor_address、alarm_status |
| 调度日志与注册 | XXL_JOB_LOG_REPORT | 调度日志报表 | 每日 running/suc/fail 统计，`JobLogReportHelper` 每分钟刷新 |
| 调度日志与注册 | XXL_JOB_REGISTRY | 执行器注册表 | registry_group=AppName、registry_value=注册地址、心跳刷新 |
| 用户与审计 | XXL_JOB_USER | 调度中心用户表 | username/password、role（0 普通/1 管理员）、permission（执行器 ID 列表） |
| 用户与审计 | XXL_JOB_PERMISSION | 任务权限表 | **二次开发新增**：permission_type 0 不可见/1 只读/2 读写 |
| 用户与审计 | XXL_JOB_OPERATE_LOG | 操作日志表 | **二次开发新增**：`@OperationLog` 切面写入 |
| 用户与审计 | XXL_JOB_LOGIN_LOG | 登录日志表 | **二次开发新增**：login_time、status 0 失败/1 成功 |

> 关系（12 条，7 observed + 5 inferred）：`XXL_JOB_INFO.ID ↔ XXL_JOB_PERMISSION.JOB_ID`（`XxlJobInfoMapper.xml:70` left join）、
> `XXL_JOB_OPERATE_LOG.JOB_ID → XXL_JOB_INFO.ID`（`XxlJobOperateLogMapper.xml:70` left join）、
> `XXL_JOB_LOG.EXECUTOR_ADDRESS → XXL_JOB_REGISTRY.REGISTRY_VALUE`（`XxlJobLogMapper.xml:254` join，丢失任务判定执行器是否下线）、
> `XXL_JOB_LOG.JOB_ID/JOB_GROUP`、`XXL_JOB_INFO.JOB_GROUP` 按**列注释**（「任务，主键ID」「执行器主键ID」）确认为 observed；
> `XXL_JOB_LOGGLUE.JOB_ID`、三张审计/权限表的 `USER_ID → XXL_JOB_USER.ID`、`XXL_JOB_REGISTRY.REGISTRY_GROUP → XXL_JOB_GROUP.APP_NAME` 为列名 inferred。

#### 业务主流程

1. **任务配置**：管理端新增/更新/删除/启停任务（`JobInfoController.add/update/remove/start/pause`）→ 写 `xxl_job_info`，
   新增时同步写当前用户的任务权限（`xxl_job_permission`）；GLUE 模式另存源码历史（`xxl_job_logglue`）。
2. **执行器注册**：业务服务执行器经 `POST /api/registry` 心跳注册（下线走 `registryRemove`）→ 写 `xxl_job_registry`；
   管理端维护执行器组（`xxl_job_group`）并在「自动注册」模式下按 AppName 刷新地址列表。
3. **周期调度触发**：`JobScheduleHelper#scheduleThread`（每 1s）持 `xxl_job_lock`（`select ... for update`）抢占调度，
   查出 `trigger_status=1 且 trigger_next_time <= now+5s` 的任务；**过期 >5s** 按 `misfire_strategy` 处理
   （`DO_NOTHING` 跳过并顺延 / `FIRE_ONCE_NOW` 立即补跑），**未到期**压入时间轮 `ringThread` 到点触发。
4. **执行器分发**：`JobTriggerPoolHelper.trigger` → `XxlJobTrigger.trigger` → 按路由策略选执行器地址
   （分片广播逐实例）→ HTTP 调 `ExecutorBiz.run`；先落一条 `xxl_job_log`，再回填 `executor_address/trigger_code/trigger_msg`。
5. **结果回调落库**：执行器 `POST /api/callback` → `JobCompleteHelper.callback`（**幂等**：`handle_code>0` 直接忽略重复回调）
   → `XxlJobCompleter.finishJob` 回填 `handle_time/handle_code/handle_msg`；`handle_code=200` 时按 `child_jobid` 触发子任务（`PARENT`）。
6. **失败重试与告警**：`JobFailMonitorHelper`（10s）扫 `alarm_status=0` 的失败日志 → 有重试次数则 `trigger(RETRY)`，
   否则邮件告警并回写 `alarm_status`（2 成功/3 失败）；`JobCompleteHelper`（60s）对「已调度未回调且执行器已下线」的日志兜底判失败。
7. **报表与审计（旁路）**：`JobLogReportHelper`（1min）刷新 `xxl_job_log_report`，并按 `logretentiondays` 每日清理过期日志
   （另有人工入口 `JobLogController.clearLog` 按月/按条数清理、`logKill` 终止运行中任务）；操作与登录日志分别由
   `@OperationLog` 切面与 `LoginService` 写入 `xxl_job_operate_log` / `xxl_job_login_log`。

| 守护线程 | 周期 | 作用 |
|---|---|---|
| `JobScheduleHelper`（scheduleThread + ringThread） | 1s / 时间轮 | 调度扫描、过期策略处理、时间轮到点触发 |
| `JobTriggerPoolHelper` | 常驻 | fast/slow 双线程池执行触发，慢任务隔离 |
| `JobRegistryHelper` | 30s | 心跳超时剔除、执行器地址列表刷新 |
| `JobFailMonitorHelper` | 10s | 失败重试与告警 |
| `JobCompleteHelper` | 60s | 回调线程池 + 丢失任务兜底 |
| `JobLogReportHelper` | 1min / 每日 | 日报表刷新、过期日志清理 |

#### 状态与枚举

- **调度类型** `ScheduleTypeEnum`：`NONE` / `CRON` / `FIX_RATE`；**过期策略**：`DO_NOTHING` / `FIRE_ONCE_NOW`（预读窗口 `PRE_READ_MS=5000`）。
- **触发方式** `TriggerTypeEnum`：`MANUAL` 手动 / `CRON` 周期 / `RETRY` 重试 / `PARENT` 父子 / `API` / `MISFIRE` 过期补跑。
- **任务状态** `xxl_job_info.trigger_status`：`0 停止 / 1 运行`；`trigger_last_time`、`trigger_next_time` 为毫秒时间戳。
- **调度结果** `trigger_code`/`handle_code`：`200 成功`、`500 失败`、`502 执行器地址为空`、`503 阻塞`、`504 超时`、`505 路由失败`等；
  `handle_code>0` 表示回调已到达（回调幂等判据）；`handle_code=200` 才触发子任务。
- **告警状态** `alarm_status`：`0 默认（待告警）/ 1 无需告警 / 2 告警成功 / 3 告警失败`。
- **权限与角色**：`xxl_job_permission.permission_type` = `0 不可见 / 1 只读 / 2 读写`；`xxl_job_user.role` = `0 普通用户 / 1 管理员`；
  另有 `xxl_job_login_log.status` = `0 失败 / 1 成功`。

#### 接口能力与依赖

- **入站（执行器 → 调度中心）**：`POST /api/{uri}`（`JobApiController.api`，`accessToken` 校验）——
  `callback` 执行结果回调、`registry` 心跳注册、`registryRemove` 下线。
- **管理端 REST**：`/jobinfo/**`（含二次开发新增 `/jobinfo/save` 存在即更新、`/jobinfo/loadByHandler`、`/jobinfo/pageListSort` 动态排序分页）、
  `/jobgroup/**`、`/joblog/**`、`/jobcode/**`、`/jobpermission/**`、`/user/**`、`/loginlog/**`、`/operatelog/**`。
  其中 `/jobinfo/save`、`/jobinfo/loadByHandler` 标 `@PermissionLimit(limit=false)`，供服务间调用。
- **出站（调度中心 → 执行器）**：`XxlJobScheduler.getExecutorBiz` 按地址缓存 `ExecutorBizClient`，
  `XxlJobTrigger.runExecutor` 调 `run()`；日志页与终止操作调 `log()` / `kill()`。
- **本服务的 Dubbo / MQ**：**均无**（无 `@DubboService`/`@DubboReference`/MQ 监听）。
- **被谁反向管理**：`bytter-system` 的 `IXxlJobTemplate`（`XxlJobTemplateImpl`，`@DubboService` 导出）以 HTTP 登录本服务并
  增删改查任务与执行器组（`XxlJobPathEnum` 列举了所用路径）——即「内管端/租户定时任务分配」经 bytter-system 转调本服务。
- **业务执行器侧**：各业务服务经 bytter-framework 的 `XxlJobConfig`（读取 `xxl.job.admin.addresses`、`xxl.job.executor.appname`、
  `xxl.job.accessToken`）自动装配 `XxlJobSpringExecutor` 完成注册发现；框架 `MultiTenantJobAspect`
  （`@Around("@annotation(xxlJob)")`）拦截 `@XxlJob`，按租户分片执行，失败以 `XxlJobHelper.handleFail` 上报。

#### 域边界与注意

- **表全部为 MyBatis 承载**：**无 JPA `@Table`、无 MyBatis-Plus `@TableName`**，因此
  `scan_jpa_ledger.py` 扫描结果为 **0 张表**；本 README 与 ERD 的 11 张表按建表脚本**手工补入账本**（`note` 已标记，重扫会丢）。
- **建表脚本位置**：本模块 `src/main/resources` **无 `.sql`**；脚本在仓库根 `sql/`——
  MySQL `sql/saas/mysql/1.Init-tables.sql`（另有 `3.Init-data-append.sql` 初始数据）、
  Oracle `sql/saas/oracle/1.Init-tables-oracle.sql` 与 `sql/oracle/other-model_ddl.sql`。
- **双数据库方言**：Mapper XML 分 `mysql-mapper/` 与 `oracle-mapper/` 两套，改 SQL 需同步两边。
- **多租户**：11 张表均含 `TENANT_ID`，并有 `IDX_TENANT_ID` 索引；任务在下发后由框架切面按租户分片执行。
- **`xxl_job_lock` 特殊**：无 Mapper、无实体，仅 `JobScheduleHelper` 用原生 SQL（`for update`）加锁，保证多 Admin 实例单点调度。
- **历史代码**：`core/old/` 下 `XxlJobDynamicScheduler`、`RemoteHttpJobBean` 为**整文件注释**的历史 Quartz / xxl-rpc 版本，未启用；
  另有 `core/*/old` 相关的废弃分支（如旧版 `XxlJobGroupController` 逻辑）保留仅作参考。
- **不读写其它服务的表**：本服务 SQL 仅涉及 `xxl_job_*`，与业务服务只通过 HTTP 协议交互。

#### 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `job_erd.png` | bytter-job•定时任务调度•数据库表关系图 | 11 表 / 12 边（3 个业务分组：任务与执行器 4 / 调度日志与注册 3 / 用户与审计 4） |
| `job_business_flow.workflow.tables.html` | 定时任务调度中心主流程 | 7 节点 / 21 表引用（交互式节点×表透镜） |

渲染源见 `tools/service-db-diagram/sources/job/`（账本 + `*.workflow.json` + 节点×表 map + 分组 JSON + 规格文件）。
