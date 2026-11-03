# bytter-prepose-erp（票证 / ERP 对接服务）

> 2026-09-10 依据代码审读与 DDL 事实新建：本服务原先无 README。运行模式为 **Dubbo 纯消费 + REST + CXF WebService** 混合，
> 无 DataSource 自动装配，业务落库主要发生在对端域服务（payment / system / bill / credit）。

## 模块介绍

ERP（企业资源计划）系统与票证平台的**入站对接网关**：以统一报文入口接收 ERP 侧请求，按服务码路由到对应策略，
完成付款单/代发工资的推送落库、处理结果回执、ERP 基础主数据与电子回单查询，并把付款单/银行账户/票据/授信等单据
经 Dubbo 转发给对应域服务。

### 代码分层

```
- bytter-prepose-erp
    - bytter-erp-adapter   REST 入口（/erp-basic、/erp/*、/erp-bill、/erp-user 等）
    - bytter-erp-client    对外 DTO/常量/枚举/WebService 接口
    - bytter-erp-domain    业务策略与编排（bizStrategy / service / dao / entity / vo / utils）
    - bytter-erp-start     启动类、yml 配置（dubbo.scan.base-packages=com.bytter.erp.service）
```

### 子域职责

| 子域（包） | 职责 |
|---|---|
| `com.bytter.biz.erp.controller` | REST 入口：统一报文 `execute`、各域单据同步（付款单/账户/票据/授信/用户/单位） |
| `com.bytter.biz.erp.bizStrategy(.impl)` | **ERP 统一接口策略**：按 `ServiceCodeEnum`（19 个服务码）路由到具体处理策略 |
| `com.bytter.biz.erp.service(.impl)` | 编排服务：付款单/代发工资推送与结果查询、基础资料查询、电子回单、CXF WebService 实现 |
| `com.bytter.biz.erp.dao / entity` | 数据访问与实体（含查询视图） |
| `com.bytter.biz.erp.utils / component / vo / dto` | 工具、公共参数与出入参 |

## 领域业务表（22 张 = 15 张 JPA 实体表 + 7 个非实体视图/对象）

| 表名 | 中文说明 |
|---|---|
| NIS_BILLHEAD | 付款单主表 |
| NIS_WAGE | 代发工资单表 |
| NIS_WAGE_DETAIL | 代发工资明细表 |
| ERP_PAYINFO | 付款数据转移表（**与 bytter-prepose-check 共用**，两侧为并行落库路径） |
| erp_user_info | ERP 登录用户表 |
| bis_server_param | 银企服务参数表 |
| ERP_TEMPLATE_DTL | 数据交换凭证模版分录表（**遗留孤儿实体**，实现主体在 check） |
| ERP_TEMPLATE_DTL_STORE | 数据交换凭证模版分录和业务数据关联表（**遗留孤儿实体**，实现主体在 check） |
| ERP_BANK_ACC_QUERY_VIEW | 银行账户查询视图 |
| ERP_BANK_INPUT_QUERY_VIEW | 联行号查询视图 |
| ERP_CORP_QUERY_VIEW | 单位查询视图 |
| ERP_CUR_QUERY_VIEW | 币别查询视图 |
| ERP_EXTERNAL_CORP_QUERY_VIEW | 客商查询视图 |
| ERP_EXTERNAL_CORP_ACC_QUERY_VIEW | 客商账号查询视图 |
| ERP_ITEM_QUERY_VIEW | 预算科目查询视图 |

> 另有 **7 个纯 SQL 视图 / 对象（POJO 承载，非 JPA 实体）**——已补入账本，ERD 与透镜同样可见（中文说明齐全）：
>
> | 对象名 | 中文说明 | 载体 |
> |---|---|---|
> | ERP_BANK_RECEIPT_VIEW | 电子回单查询视图 | DAO 原生 SQL（回单查询） |
> | ERP_BANK_RECEIPT_IMG_VIEW | 电子回单影像视图 | DAO 原生 SQL（回单影像逐条下载） |
> | ERP_BIS_ACC_BAL_VIEW | 银行账户余额查询视图 | DAO 原生 SQL（账户余额下拉） |
> | ERP_NIS_WAGE_RESULT_VIEW | 代发工资结果查询视图 | DAO 原生 SQL（代发工资结果回执） |
> | erp_payment_result_view | 付款结果查询视图 | DAO 原生 SQL（付款单结果回执） |
> | v_acc_list | 账户列表视图 | DAO 原生 SQL（账户列表下拉） |
> | BIS_ACC_DTL | 银行账户流水表 | bytter-service-bdlink 实体表，ERP 侧只读（银行流水查询） |

### 表归属提示（跨服务核对结论）

- **仅 3 张表与 `bytter-prepose-check` 重叠**：`ERP_PAYINFO`（**共用**：check 侧 `dataexchange.basic.ErpPayInfoService.transfer`
  读表转移，本服务侧接收即写）、`ERP_TEMPLATE_DTL` 与 `ERP_TEMPLATE_DTL_STORE`（本服务为**无 DAO/Service/Controller 的遗留孤儿实体**，
  实现主体在 check 的 `transfer.swap`）。
- 其余 ERP_* 表（`ERP_TEMPLATE`、`ERP_HIS_DTL_TEMPLATE`、`ERP_INTER_CONFIG*`、`ERP_DATA_TYPE`、`ERP_DYNAMIC_DATA*`、
  `ERP_BASIC_DATA`、`ERP_TYPE`、`ERP_WAGE_DETAIL`、`ERP_DATATYPE_FIELD` 等）**仅存在于 check**。
- 本服务 EntityScan 仍保留 `com.bytter.biz.*.*.entity` 通配规则与上述两条孤儿实体，推断为 dataexchange/transfer 包迁出
  `bytter-prepose-check` 时的残留（**方向仅有间接证据**：通配扫描 + 孤儿实体 + 旧稿源码编码）。

## 业务主流程

统一入口 `POST /erp-basic/execute` → 租户号校验（`ISysTenantManagerService`）→ 按 `serviceCode + version` 取策略
（`BizStrategyContext`，无法匹配即拒绝）→ 策略执行 → 返回 `status/msg/data`。

1. **付款单推送**（`paymentSingle` / `paymentBatch`）：`checkData` 校验 → 写 `ERP_PAYINFO` + `NIS_BILLHEAD`；
   逐条独立成败，校验失败回 `status=F` 与原因。
2. **代发工资推送**（`nisWage`）：明细逐条校验，**任一明细不通过则整单失败不落库**（全或无）；总金额与明细合计必须一致；
   成功后写 `ERP_PAYINFO` + `NIS_WAGE` + `NIS_WAGE_DETAIL`。
3. **结果回执**（`paymentSingleResult` / `paymentBatchResult` / `nisWageResult`）：读 `erp_payment_result_view` /
   `ERP_NIS_WAGE_RESULT_VIEW` 返回处理结果。
4. **基础主数据查询**：账户、联行号、单位、币别、客商、客商账号、预算科目、账户余额等视图，供 ERP 侧下拉与校验。
5. **电子回单**：查询回单与影像（`ERP_BANK_RECEIPT_VIEW` / `ERP_BANK_RECEIPT_IMG_VIEW`），逐条下载、单条失败仅记日志继续。
6. **单据同步（REST → Dubbo 转发，本服务不落库）**：付款单 `INisBillheadService.syncBatchSave/Delete`、
   银行账户 `IBtBankAccService.syncBatchSave/Update/Delete`、票据 `IAccebillService.*ERP`、授信 `IRatUrationService.addUseRation/backUseRation`。

### 幂等与异常分支

- **幂等**：同「租户 + 流水号」重复请求判为已处理并拒绝（`ErpService.checkSerialNoRepeat`）——即失败重发防护。
- 租户号无效 / 策略缺失 / serviceCode 无效 → 抛错拒绝；`BtVerificationException` 回 `status=F` + 原消息；
  其他异常统一回「业务处理异常，请联系银行相关人员」。
- 打回/释放：付款单删除打回 `syncBatchDelete`、授信审批打回 `backUseRation`、单位/用户/账户删除同理。

## 接口能力与依赖

- **入站**：`POST /erp-basic/execute`（统一报文，19 个服务码）；另有各域 REST 同步接口与 CXF WebService 实现类
  （`NisBillheadWsService` / `BankAccountWsService` / `BillWsService` / `CorpWsService` / `UserWsService`）。
  > 注：CXF 实现类未见 `@Endpoint`/`CXFServlet` 配置，实际导出端点未在本仓库证实（可能在 Nacos 外部配置）。
- **Dubbo 引用**：`INisBillheadService`（payment）、`IBtBankAccService`/`ICorpService`/`ICurrencyService`/`IUserService`/`IDictService`/
  `IInputBankInfoService`/`IBillFileService`/`ISysTenantManagerService`（system）、`IAccebillService`（bill）、
  `IBisBankReceiptDtlService`（bdlink）、`IRatUrationService`（credit）、`IErpPayInfoService`（payment）。
- **Dubbo 导出**：未见（无 `@DubboService`）。
- **MQ**：未见证据（无 MQ 相关依赖与注解）。
- **定时任务**：未见证据（无 `@XxlJob`）。

## 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `erp_erd.png` | bytter-prepose-erp•ERP对接•数据库表关系图 | **22 张表（15 张 JPA 实体 + 7 个非实体视图）全部带中文说明** / 1 边 |
| `erp_business_flow.workflow.tables.html` | ERP 入站对接业务主流程 | 7 节点 / 18 表引用（含非实体视图，透镜中文 **18/18** 无缺口） |

渲染源见 `tools/service-db-diagram/sources/erp/`（账本 + `*.workflow.json` + 节点×表 map + 规格文件）。

### 图与本文表清单的口径说明

两类「表」名**现在都进图**：

1. **JPA 实体表（15 张）**——有 `@Table` 实体、扫描账本自动产出；ERD 与透镜中文名取项目词库并去「（…）」注释
   （如 `ERP_PAYINFO` → 付款数据转移表）。
2. **非实体视图 / 纯 SQL 对象（7 个）**——没有实体类，**扫描器不产出**；本项目按本文与 DAO 代码证据**手工补入账本**
   （`erp_ledger.json` 中带 `note` 标记「账本扫描不产出」，`columns` 为空），使 ERD/透镜与本文表清单**口径一致**。
   > ⚠️ 维护提示：账本若被重新扫描，这 7 条会丢失，需按本节表格重新补入。

透镜侧的配套修复：渲染脚本 `render_workflow_tables.py` 现在会把**节点×表 map 里的 `cn` 合并进临时词库**再烘焙透镜
（此前 enrich 只认账本/词库，导致视图类表显示「— 未在账本/词库中采集中文名」）。
