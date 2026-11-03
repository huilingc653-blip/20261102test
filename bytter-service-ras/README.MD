# bytter-service-ras（营收稽核）

> 2026-09-11 依据代码审读与 DDL/实体事实新建（原无 README）：含 16 张领域表清单、营收稽核主流程、状态机、
> 渠道与配置维护、调度与依赖边界。
> **命名澄清**：POM 与启动类注释写作「T6微服务 增值相关服务 / 增值服务」，但按客户包常量类注释（`RasConstant`：营收稽核常量类）
> 与业务事实，**RAS = 营收稽核（Revenue Audit System）**，资源码前缀统一 `ras:`。本 README 按实际业务口径书写。

## 模块介绍

**零售/门店营收稽核服务**：把门店「**营收**」（应缴/POS 流水）与「**实收**」（到账款项）按
**门店 + 缴款方式 + 日期**匹配核销，产出稽核单、差异稽核、反稽核与退款单；并维护门店、门店授权、
POS 机绑定/借入、银行卡与第三方账号绑定、导入模板配置等基础数据。

运行模式：**无 Dubbo 导出、无 MQ、无审批流**——以「状态机 + 操作日志」驱动，定时任务走 XXL-Job /
system 任务开关；对外仅通过网关 `/ras/**` 暴露 HTTP（`bytter-gateway/application-dev.yml`）。

### 代码分层

```
- bytter-service-ras
    - bytter-ras-adapter  16 个 Controller（页面跳转 + REST）：稽核/营收/实收/任务/策略/退款/POS/绑卡/门店/授权/导入配置/查询/配置/日志
    - bytter-ras-client   常量与枚举（RasConstant / RasMessageEnum）与 IExcelCacheService 存根；无远程 API 接口
    - bytter-ras-domain   entity（16 张表）/ dao（含数据迁移）/ service（编排）/ vo / utils
    - bytter-ras-start    启动类、yml
```

### 子域职责

| 子域（类族） | 职责 |
|---|---|
| `RasRevenueService` + `RasRevenueController` | 营收数据导入、查询、批量作废与归档迁移 |
| `RasReceivedService` + `RasReceivedController` | 实收数据导入、**认领（单个/批量/全量自动）**、作废与归档迁移 |
| `RasAuditService` + `RasAuditDao` | **稽核执行**（手工/自动）、稽核单产出、差异稽核、**反稽核** |
| `RasAuditTaskService` / `RasAuditStrategyService` | 稽核任务配置与匹配策略维护、执行扫描 |
| `RasTaskService` | 任务调度命令入口（由 system 任务开关触发） |
| `RasBackPayService` | 订单退款单提交 / 打回 / 重发 / 查询 |
| `RasPosBindingService` / `RasPosBorrowService` | POS 机绑定、借出、借入确认与归还 |
| `RasBankCardBindingService` / `RasOtherCardBindingService` | 银行卡、第三方账号绑定（认领的渠道依据） |
| `RasShopService` / `RasShopGrantService` | 门店维护与门店授权（控制可见范围） |
| `RasImportConfService` | 营收/实收导入模板配置 |
| `RasDataMoveService` / `RasHisDataMoveService` | 营收实收、历史与作废数据迁移（XXL-Job） |

## 领域业务表（16 张）

| 分组 | 表名 | 中文说明 |
|---|---|---|
| 门店与授权 | RAS_SHOP | 门店信息表 |
| 门店与授权 | RAS_SHOP_GRANT | 门店授权表 |
| 稽核作业 | RAS_AUDIT_INFO | 稽核信息表 |
| 稽核作业 | RAS_AUDIT_INTER | 稽核营收实收中间表 |
| 稽核作业 | RAS_AUDIT_TASK | 稽核任务配置表 |
| 稽核作业 | RAS_AUDIT_STRATEGY | 稽核策略维护表 |
| 营收与实收 | RAS_REVENUE_DTL | 营收数据明细表 |
| 营收与实收 | RAS_RECEIVED_DTL | 实收数据明细表 |
| 缴款渠道 | RAS_POS_BINDING | POS机绑定表 |
| 缴款渠道 | RAS_POS_BORROW | POS机借入表 |
| 缴款渠道 | RAS_BANK_CARD_BINDING | 银行卡绑定表 |
| 缴款渠道 | RAS_OTHER_CARD_BINDING | 第三方账户绑定表 |
| 退款与配置 | RAS_BACK_PAY | 订单退款信息表 |
| 退款与配置 | RAS_IMPORT_CONF | 导入配置表 |
| 退款与配置 | RAS_IMPORT_CONF_DTL | 导入配置明细表 |
| 运行日志 | RAS_LOG | 营收稽核日志表 |

> 关系（15 条，13 observed + 2 inferred）：所有业务单据以 `SHOP_ID → RAS_SHOP.ID` 归属门店
> （营收/实收/稽核单/退款/POS 绑定与借入/两类绑卡/门店授权，均有 DAO SQL join 证据）；
> `RAS_REVENUE_DTL.AUDIT_ID`、`RAS_RECEIVED_DTL.AUDIT_ID → RAS_AUDIT_INFO.ID` 在稽核后回填；
> `RAS_POS_BORROW.BD_ID → RAS_POS_BINDING.ID`；`RAS_IMPORT_CONF_DTL.IC_ID → RAS_IMPORT_CONF.ID`；
> `RAS_AUDIT_INTER.REVENUE_ID → RAS_REVENUE_DTL.ID`、`.RECEIVED_ID → RAS_RECEIVED_DTL.ID`（中间表按列名推断，另以 `UN_ID` 唯一号参与匹配）。
> `RAS_LOG.QUOTE_ID` 为**多态引用**（外部业务对象 ID，未建边）；`RAS_AUDIT_TASK` / `RAS_AUDIT_STRATEGY` **无 `*_ID` 外键**（按缴款方式等业务键关联，未建边）。

## 业务主流程

### 1. 营收/实收稽核主链

1. **导入**：营收/实收明细按导入模板落库（默认 `status=1 待稽核`），支持批量作废。
2. **认领**：实收明细按门店 + 缴款方式补齐渠道信息（银行卡绑定 / POS 绑定 / POS 借入 / 第三方账号绑定），
   `claimType 0 待认领 → 1 已认领`；单个、批量与全量自动认领三种入口。
3. **稽核**：先**锁定待稽核数据**（`1 → 2 稽核中`）→ 按缴款方式逐策略取数并读中间表辅助匹配 →
   产出稽核单 `3 已稽核` 或 `4 差异稽核` → `finally` 解锁；现金差异触发 `INotificationService` 通知。
4. **反稽核**：稽核单置 `-1`，营收/实收明细退回 `1 待稽核`，并清理中间表对应关系（支持单个与全部）。
5. **退款**：订单退款单提交 / 打回 / 重发，`00 待处理 → 11 处理中 / 10 待打回 → -1 已打回 / 95 结束`，
   `sendSign` 记录指令发送态。

### 2. 基础数据维护（旁路）

门店维护与启停、门店授权（门店-用户）、POS 机绑定与借出/借入归还、银行卡与第三方账号绑定、导入模板配置；
均写 `RAS_LOG` 留痕。任务与策略：配置缴款方式、启动日期、运行态，支持手工执行扫描，定时由 system 任务开关触发。

### 3. 数据迁移

XXL-Job `RasDataMoveService`（营收/实收数据迁移）与 `RasHisDataMoveService`（历史与作废数据迁移）——
把明细迁入 `*_su_dtl` / `*_his_dtl` 归档表并在迁移中校验稽核单关联。

### 状态与枚举

| 对象 | 字段 | 取值 |
|---|---|---|
| 稽核单 / 营收 / 实收 | `status` | `1 待稽核 / 2 稽核中 / 3 已稽核 / 4 差异稽核 / 5 稽核完成 / 6 已汇总 / 0 待确认 / -1 打回 / -2 作废` |
| 实收明细 | `claimType` | `0 待认领 / 1 已认领` |
| POS 绑定 | `status` | `01 正常 / 02 待确认 / 03 已借出` |
| POS 借入 | `status` | `-1 打回 / 01 待确认 / 02 借用 / 03 已归还` |
| 退款单 | `status` / `sendSign` | `00 待处理 / 11 处理中 / 10 待打回 / -1 已打回 / 95 结束`；发送 `00 失败 / 01 成功 / 11 待发送 / 16 处理中` |
| 稽核任务 | `validSign` / `runStatus` | 启用 `1` / 禁用 `0`；运行态 `S 成功 / F 失败 / R 处理中` |

> **无审批流**：全服务未见 `flowStarted/flowCompleted/flowRevoke` 与流程 key，业务推进靠状态机 + `RAS_LOG`。

## 接口能力与依赖

- **Dubbo 导出**：未见证据（`bytter-ras-client` 仅常量/枚举与 `IExcelCacheService` 存根），
  其它服务**未见引用** `com.bytter.ras` 业务类；接入面为网关 `/ras/**`。
- **对外依赖**：**仅 bytter-service-system** —— `ICorpService`（单位）、`IDictService`（字典）、`IConfigService`（参数）、
  `IBtBankAccService`（银行账户）、`IBtCurrencyService`（币别）、`IUserService`（用户）、`INotificationService`（通知）、
  `ITaskService` / `ITaskSwitchService`（任务调度开关）、`IMultiLangService`（多语言）、`ICorpDeleteVerifyService`（单位删除校验）。
- **MQ**：未见证据。
- **定时任务**：`@XxlJob("RasDataMoveService")`、`@XxlJob("RasHisDataMoveService")`；
  另有调度命令 `com.bytter.ras.service.RasTaskService` 由 system 任务开关触发。

## 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `ras_erd.png` | bytter-service-ras•营收稽核•数据库表关系图 | 16 表 / 15 边（6 个业务分组：门店与授权 2 / 稽核作业 4 / 营收与实收 2 / 缴款渠道 4 / 退款与配置 3 / 运行日志 1） |
| `ras_business_flow.workflow.tables.html` | 营收稽核（营收-实收对账）主流程 | 7 节点 / 28 表引用（交互式节点×表透镜） |

渲染源见 `tools/service-db-diagram/sources/ras/`（账本 + `*.workflow.json` + 节点×表 map + 分组 JSON + 规格文件）。
