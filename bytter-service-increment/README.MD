## 增值服务模块

### 迁移旧代码模块说明
- T6 项目 bt-biz-chowmatistic 理财管理
- T6 项目 bt-biz-pricing 定价管理

### 模块介绍

#### 主要业务
- 存款投资（银行理财、协定存款）
- 理财投资（理财产品登记/申购/赎回/续期、净值维护）
- 内部存款投资（单位内部账户间活期/定期/通知互转）
- 定价管理

**定位**：财务/资金域的「增值」服务——管理企业自有资金的存款与理财增值业务，并对接银行直连生成资金指令。
运行模式为 **Dubbo 双向**（被 creditRating / bdlink / payment 调用，同时消费 payment / finacial / system）+ 工作流审批 + XXL-Job。

##### 代码分层
```
- bytter-service-increment

    - bytter-increment-adapter

      适配层对应mvc的controller层使用web|mobile|wap按包名隔离，vm资源存放此层
      （web/ 为旧 JSP 视图入口，saas/ 为前后端分离接口，flow/ 为审批待办入口）

    - bytter-increment-client

      定义暴露的api接口给其他服务调用（IChoInnerFinanceService / IChoDepositRateService /
      IChoProductRedeemService / ICBFService / IBisFinanceExcService 等 12 个）

    - bytter-increment-domain

      主要业务处理接口实现、数据库访问与数据组装后续DDD改造升级重点重构模块

    - bytter-increment-start

      启动类、yml配置文件
```

### 领域内业务表（14 张）

> 2026-09-11 按 JPA 实体逐表核对修正：**原表清单中的 `TB_LOAN_RATE_MAINTENCE`（存款利率维护表）与 `TB_CREDIT_PERIOD`（存款利率类型）不属于本服务**（本服务仅通过 system 的 `IPricingDepositRateService` / `ICreditPeriodService` 消费定价数据），已移出。

| 分组 | 表名 | 描述 |
|---|---|---|
| 理财产品 | CHO_PRODUCT_REQ | 理财产品登记表 |
| 理财产品 | CHO_PRODUCT_TYPE | 理财产品类型表 |
| 理财产品 | CHO_PRODUCT_REDEEM | 理财产品赎回表 |
| 理财产品 | CHO_PRODUCT_REDEEM_DETAIL | 理财赎回明细表 |
| 理财产品 | CHO_PRODUCT_RENEWAL | 理财产品续期表 |
| 理财产品 | CHO_PRODUCT_NETVAL | 理财产品净值维护表 |
| 存款业务 | CHO_BANK_FINANCE | 银行理财表 |
| 存款业务 | CHO_INNER_FINANCE | 内部理财信息表 |
| 存款业务 | FC_AGREED_DEPOSIT | 协定存款登记表 |
| 存款业务 | FC_AGREED_DETAIL | 协定存款登记明细表 |
| 指令与日志 | BIS_FINANCE_EXC | 理财指令表 |
| 指令与日志 | CHO_MESSAGE | 理财日志表 |
| 基础维护 | BI_REMAIN_TERM | 剩余期限维护表 |
| 基础维护 | BI_REMAIN_TERM_DETAIL | 剩余期限维护明细表 |

> 关系（8 条，均带 DAO/实体证据）：`CHO_PRODUCT_REDEEM.REQ_ID → CHO_PRODUCT_REQ.ID`、
> `CHO_PRODUCT_REDEEM_DETAIL.CPR_ID → CHO_PRODUCT_REDEEM.ID`、`CHO_PRODUCT_NETVAL.REQ_ID → CHO_PRODUCT_REQ.ID`、
> `CHO_PRODUCT_TYPE.PRODUCT_CODE → CHO_PRODUCT_REQ.PRODUCT_CODE`、`BIS_FINANCE_EXC.CBS_BILL_ID → CHO_BANK_FINANCE.ID`、
> `BI_REMAIN_TERM_DETAIL.REMAIN_TERM_ID → BI_REMAIN_TERM.ID`（以上 observed），
> `CHO_PRODUCT_RENEWAL.REDEEM_ID → CHO_PRODUCT_REDEEM.ID`、`FC_AGREED_DETAIL.AGREED_DEPOSIT_ID → FC_AGREED_DEPOSIT.ID`（列名 inferred）。
> 另有若干**仅在 SQL 中出现、无实体**的对象（如 `CHO_BANK_FINANCE_DETAIL`），不纳入账本与 ERD。

### 业务主流程

1. **理财（产品侧）**：登记/购买申请 `ChoProductReqService.saveChoProductReq/batchSubmit` → 审批 `choProductReq` →
   通过置 `95`（非保本浮动类生成 `CHO_PRODUCT_NETVAL` 净值序列）→ 赎回 `ChoProductRedeemService.saveChoProductRedeem`
   （写赎回单+明细并回写登记 `bizStatus/redeemStatus`）→ 审批 `choProductRedeem` → **全赎则登记置 `110` 业务结束**，
   续存（`isStore=1`）按凭证方式生成新登记；另有续期 `CHO_PRODUCT_RENEWAL` 独立流程。
2. **银行理财（指令侧）**：`saveBankFinance` → `choBankC2T/T2C/C2N/N2C` → 通过且**在线账号**才 `saveBisFinanceExc`
   生成理财指令 `BIS_FINANCE_EXC`（`voucherStat=WAIT_FILL`）→ 指令发送/退回/重发/作废；退回或作废回写银行理财为 `-1/-2`；
   银行流水认领可用于赎回确认（`collectionConfirmProductRedeem`）。
3. **内部存款与协定存款**：`saveInnerFinance`（6 种业务类型 10~15，按 `busType` 路由流程）→ 审批通过后发起「内部存款记账」通知
   → 到期由 XXL-Job 按到期处理方式自动生成取款/续存单、**计提利息**（本金×天数×利率/360）并生成记账凭证；
   协定存款登记/延期/终止经 `choAgreeddepositReg/Pos/Term` 审批，到期按配置自动延期或终止。

### 状态与审批

- **通用状态**：`0 暂存 / 11 待审批 / 95 通过 / -1 打回 / -2 删除`；理财登记另有 `110 业务结束`，
  内部理财另有记账态 `96~99`、已取款 `101`、取消预约 `-3`；协定存款 `billStatus`：`10 登记 / 11 延期·终止`。
- **理财 bizStatus 1~20**：赎回 5~8、续期 9~12、购买申请 13~16、结束 20。
- **指令状态**：复用 bdlink `BdlinkConstant`（`WAIT_FILL / PRE_SEND / BACK / DELETE / INVALIDATE`）。
- **审批流（工作流 key）**：`choProductReq` / `choProductRedeem` / `choProductRenewal` / `choProductBuyApply`；
  银行侧 `choBankC2T/T2C/C2N/N2C`；内部侧 `choInnerC2T/T2C/C2N/N2C/N2B/B2N`；协定侧 `choAgreeddepositReg/Pos/Term`。
  回调 `flowCompleted` 是**业务后置动作的唯一落点**（生成指令 / 建净值 / 记账通知 / 状态收口），`flowRevoke` 统一回置 `0`。

### 接口能力

- **Dubbo 导出（12 个）**，其中被外部实际引用：`IChoInnerFinanceService`（bdlink、payment）、
  `IChoDepositRateService` / `IChoProductRedeemService`（creditRating）、`ICBFService` / `IBisFinanceExcService`（payment）。
- **Dubbo 引用**：system（单位/账户/币别/字典/通知/用户/授权/定价利率/日结/内部台账等）、
  finacial（`IInnerAccountService`/`IFcAccService`/`IFcInterestService`/`IFcBusinessRuleDefineService` 等，内部存款记账与分录）、
  payment（`INisMessageService`）、bdlink（`IBisAccDtlService` 银行流水）。
- **XXL-Job（4 个）**：理财到期提醒、内部存款到期处理（含利息计提与记账凭证）、协定存款到期提醒与自动延期/终止、指令交易结果提醒。
- **MQ**：配置存在（binding `bis-exc-to-payment-out-0` → `bis-exc-topic`，group `bytter-service-payment`），
  但**生产者代码整体被注释**（`BisMQService`、调用点同注释）、无消费者，实际以 Dubbo + 库表 + XXL-Job 为主。

### 依赖服务

- 被调方向：**creditRating → 本服务**（利率/赎回数据）、**bdlink → 本服务**（内部财务）、**payment → 本服务**（指令与内部财务）。
- 主动调用：**本服务 → payment**（消息与回执）、**→ finacial**（内部账户/分录/利息）、**→ system**（主数据与通知）。
- 对 creditRating **无主动调用**（未见证据）。

### 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `increment_erd.png` | bytter-service-increment•存款与理财•数据库表关系图 | 14 表 / 8 边（4 个业务分组：理财产品 6 / 存款业务 4 / 指令与日志 2 / 基础维护 2） |
| `increment_business_flow.workflow.tables.html` | 增值业务（存款与理财）主流程 | 7 节点 / 30 表引用（交互式节点×表透镜） |

渲染源见 `tools/service-db-diagram/sources/increment/`（账本 + `*.workflow.json` + 节点×表 map + 分组 JSON + 规格文件）。
