## 统计报表模块

### 迁移旧代码模块说明
- T6项目 bt-biz-bi 报表

### 模块介绍

#### 主要业务
- 结算管理报表 / 核算管理报表 / 承兑汇票报表 / 信贷融资报表 / 资金计划报表 / 账户管理报表 / 理财管理报表 / 综合查询报表
- 自助 BI（CBoard）：看板、数据集、Widget 与看板权限

**定位**：`T6微服务 报表相关服务`——**以「报表/统计」为主**（绝大多数动作是只读查询 + 内存汇总/折比 + Excel 导出），
另含少量基础资料维护（剩余期限、贷款期限）与报表快照落库。运行模式为 **Dubbo 双向**（被 payment 调用，同时消费 6 个服务），
无 MQ、无审批流，定时任务走 XXL-Job。

##### 代码分层
```
- bytter-service-report

    - bytter-report-adapter

      适配层对应mvc的controller层使用web|mobile|wap按包名隔离，vm资源存放此层
      （web/ 为 PC 端报表入口，mobile/ 为移动端包装，olap/ 为自助 BI 接口）

    - bytter-report-client

      定义暴露的api接口给其他服务调用（IHomeBankAccBalDistributeService 被 payment 使用；
      IBiRemainTermService 为内部使用）

    - bytter-report-domain

      主要业务处理接口实现、数据库访问与数据组装后续DDD改造升级重点重构模块
      （domain/{billl,cms,netbank,stock,financial,synthesize,reminterm} 报表子域 + olap 自助 BI）

    - bytter-report-start

      启动类、yml配置文件
```

### 领域内业务表（15 张 = 8 张 JPA 实体表 + 7 张 BI 表）

| 分组 | 表名 | 描述 | 载体 |
|---|---|---|---|
| 报表与基础资料 | RAT_BK_STAT_REPORT_HISTORY | 授信额度统计历史表 | JPA 实体（本服务快照，先删后插） |
| 报表与基础资料 | BI_REMAIN_TERM | 剩余期限维护表 | JPA 实体（在 DDL 脚本内） |
| 报表与基础资料 | BI_REMAIN_TERM_DETAIL | 剩余期限维护明细表 | JPA 实体（在 DDL 脚本内） |
| 报表与基础资料 | TB_CREDIT_PERIOD | 贷款期限表 | JPA 实体（在 DDL 脚本内） |
| 信贷融资（跨域只读） | CMS_LOAN_INFO | 贷款信息表 | JPA 只读实体（真实归属 bytter-prepose-bill） |
| 信贷融资（跨域只读） | CMS_LOAN_REPAY | 贷款还款表 | JPA 只读实体（同上） |
| 信贷融资（跨域只读） | CMS_PRECATIVE_LOAN_REPAY | 委托贷款还款信息表 | JPA 只读实体（同上） |
| 信贷融资（跨域只读） | CMS_FINANCING_LEASE_INFO | 融资租赁信息表 | JPA 只读实体（同上） |
| 自助 BI 看板 | OLAP_BOARD | 看板表 | **MyBatis 映射**（无 JPA 实体，本项目按 mapper 补入账本） |
| 自助 BI 看板 | OLAP_BOARD_PARAM | 看板参数表 | MyBatis 映射 |
| 自助 BI 看板 | OLAP_CATEGORY | 看板分类表 | MyBatis 映射 |
| 自助 BI 看板 | OLAP_DATASET | 数据集表 | MyBatis 映射 |
| 自助 BI 看板 | OLAP_WIDGET | 看板部件表 | MyBatis 映射 |
| 自助 BI 看板 | OLAP_ROLE_PERMISSION | 看板角色权限表 | MyBatis 映射 |
| 自助 BI 看板 | OLAP_USER_PERMISSION | 看板用户权限表 | MyBatis 映射 |

> 关系（7 条，5 observed + 2 inferred）：`BI_REMAIN_TERM_DETAIL.REMAIN_TERM_ID → BI_REMAIN_TERM.ID`；
> `CMS_LOAN_REPAY.SEND_CODE → CMS_LOAN_INFO.BILL_CODE`（经 `cms_loan_send.loan_code` 关联，该中间表属信贷域、不在本服务账本）；
> BI 侧 `OLAP_BOARD.CATEGORY_ID → OLAP_CATEGORY.CATEGORY_ID`、`OLAP_USER_PERMISSION/OLAP_ROLE_PERMISSION.BOARD_ID → OLAP_BOARD.BOARD_ID`（BoardDao.xml 权限过滤），
> `OLAP_BOARD_PARAM.BOARD_ID → OLAP_BOARD.BOARD_ID`、`OLAP_WIDGET.DATASET_ID → OLAP_DATASET.DATASET_ID`（列名推断）。
> ⚠️ `sql/oracle/bytter-service-report_ddl.sql` 只含 3 张表（BI_REMAIN_TERM / BI_REMAIN_TERM_DETAIL / TB_CREDIT_PERIOD）；CMS_* 与 RAT_* 表由所属域维护，OLAP_* 表由 MyBatis 映射、DDL 不在本仓库。

### 业务主流程

1. **报表入口与基础资料**：PC `web/*` 与移动 `mobile/*` 双端进入；本服务内少数的写操作是基础资料维护
   （剩余期限维护 `BiRemainTermService.remainTermSubmit`、贷款期限 `TbCreditPeriodService.savePeriod`）。
2. **权限与维度前置**：按登录用户取可见单位集合（`ICorpService.getUserCorpIds`），补齐单位名/币别/汇率/字典。
3. **跨域取数**：各报表 DAO 拼跨库 SQL（多表 union / left join）读信贷融资、票据、账户、预算等他域数据
   （本服务的 `CMS_*` 实体即为此类只读镜像）。
4. **汇总与折比**：内存合计、折合本币（`RatBkStatisticsReportHisService.setBeanMoneyType`）、授信占用/可用测算。
5. **输出**：当前页 / 全量导出 Excel（模板 `start/resources/export/cms/*.xls`）；移动端包装同一批数据。
6. **快照落库（定时）**：`@XxlJob("RatBkStatisticsReportHisService")` 每日取「前一日」综合授信额度统计，
   **先删后插**写入 `RAT_BK_STAT_REPORT_HISTORY`（幂等重跑）；查询为空则仅记日志不落库。
7. **自助 BI**：看板 / 数据集 / Widget 与看板权限（角色、用户）维护，供业务自建分析视图。

### 状态与审批

- **无本服务自有状态枚举**：带状态字段的实体（`RAT_BK_STAT_REPORT_HISTORY.status`、`CmsLoanInfo.status/bizStatus`）语义归属源域，
  报表 SQL 以状态值过滤（如 `cms_loan_info.status = 102` 在贷、票据 `status='95'`）。
- **无审批流**：`CmsLeaseReportService` 虽实现 `IWorkFlowListener`，但 `flowStarted/flowCompleted` 为空实现、无流程 key。
- **无重试补偿**：报表异常仅 Controller 层 try/catch 记日志并返回空表。

### 接口能力与依赖

- **Dubbo 导出**：`IHomeBankAccBalDistributeService`（被 **bytter-service-payment** 的移动端资金余额看板调用）、
  `IBiRemainTermService`（本服务内部使用；注意与 bytter-service-increment 的同名接口为**双源契约**、包名不同）。
- **Dubbo 引用**：system（单位/币别/字典/汇率/用户/银行/账户/参数等，接口占比最大）、
  bytter-prepose-bill（授信/票据/放款查询）、payment（凭证类型/账户余额）、increment（剩余期限/银行理财）、
  bdlink（账户余额）、bytter-manager-plan（预算执行）、workflow（API 类型引用）。
- **MQ**：未见证据。 **定时任务**：`@XxlJob("RatBkStatisticsReportHisService")`（前一日授信统计快照）。
- **与 bytter-service-ras**：无依赖证据。

### 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `report_erd.png` | bytter-service-report•报表与看板•数据库表关系图 | 15 表 / 7 边（3 个业务分组：报表与基础资料 4 / 信贷融资（跨域只读）4 / 自助 BI 看板 7） |
| `report_business_flow.workflow.tables.html` | 报表查询与自助看板主流程 | 7 节点 / 18 表引用（交互式节点×表透镜） |

渲染源见 `tools/service-db-diagram/sources/report/`（账本 + `*.workflow.json` + 节点×表 map + 分组 JSON + 规格文件）。
