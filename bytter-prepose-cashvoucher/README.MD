# 现金凭证模块（bytter-prepose-cashvoucher）

> 模块描述：T6 微服务「现金/空白凭证管理」，负责空白凭证的种类与套打配置、入库与明细台账、领用使用与流转、凭证套打占用，以及支票收款登记与复核台账；业务域包名为 `com.bytter.cashVoucher`。

## 模块介绍

### 主要业务
- 凭证种类与套打配置：凭证种类维护、打印场景、凭证种类取数字段；打印格式主表 / 明细与匹配规则；空白凭证库箱维护
- 空白凭证入库：购入登记（单位 / 库箱 / 账户 / 种类 / 起止号），按编码规则逐号拆分生成明细台账
- 凭证使用与流转：领用（手工登记或打印自动占用）、出售、退票、丢失、作废、移库，回写明细状态
- 支票收款：支票收款登记、服务内置复核（通过入库 / 打回）、台账处理（退票 / 作废 / 托收 / 到账）
- 凭证套打：按凭证种类匹配打印格式 → 取号 → 占用（写套打操作信息），完成后回调票据模块
- 操作日志：入库 / 流转 / 打印等关键动作写 `BV_LOG`

主流程：**空白凭证线** 种类与格式配置 → 空白凭证入库 → 拆号生成明细台账 → 凭证使用与流转 → 凭证号占用与套打 → 回调业务方（`bytter-prepose-bill`）；**支票线** 支票收款登记 → 内置复核 → 复核通过入库 → 支票台账处理 → 凭证号占用与套打 → 通知 / 回调。可视化索引见文末「业务流交付索引」。

### 代码分层
```
- bytter-prepose-cashvoucher

    - bytter-cashvoucher-adapter

      适配层，对应 MVC 的 Controller，按包名隔离；VM 资源存放于此层。
      saas/ 子包另提供 /v1-* 前缀的 REST(JSON) 版控制器（返回体裁与路由前缀不同，业务一致）
    - bytter-cashvoucher-client

      对外 API 契约模块（当前不含对外 Dubbo 提供者实现）
    - bytter-cashvoucher-domain

      主要业务处理接口实现、数据库访问与数据组装，后续 DDD 改造升级重点重构模块
    - bytter-cashvoucher-start

      启动类、yml 配置文件
```

### 数据库
- 数据库：Oracle（`ojdbc8` + Druid 连接池，依赖见根 `pom.xml`）；连接串与账号由 Nacos 共享配置下发，各环境地址见 `bootstrap.yml`。

### 领域内业务表
> 口径：仅列本服务自有 JPA `@Entity` 映射的 13 张业务表。中文名取自实体类 `@Description` 与 DDL 表注释（`sql/saas/oracle/1.Init-tables-oracle.sql`），两者不一致时以实体说明为准。

| 表名 | 所属子域 | 描述 |
| --- | --- | --- |
| `BV_VOUCHER_TYPE` | voucherType | 凭证种类表（凭证种类维护，含取数来源标识） |
| `BV_VOUTYPE_SCENE` | voucherType | 凭证类型打印场景表（场景名称、排序、有效标志） |
| `BV_VOUTYPE_DATA_FIELD` | voucherType | 凭证种类取数字段表（打印取值字段定义） |
| `BV_VOU_INPUT` | vouInput | 凭证入库信息表（购入登记：单位 / 库箱 / 账户 / 种类 / 起止号 / 入库状态） |
| `BV_VOU_INFO_DTL` | vouInput | 凭证入库明细表（逐号台账：凭证号、状态、库箱、来源入库单） |
| `BV_VOU_EMPLOY_INFO` | vouEmploy | 凭证使用信息表（领用 / 使用登记，来源手工或打印占用） |
| `BV_STORAGE_BOX` | storage | 空白凭证库箱表（库箱与保管员维护） |
| `BV_PRINT_FORMAT` | print | 凭证打印格式主信息（套打格式与场景 / 种类绑定） |
| `BV_PRINT_FORMAT_DTL` | print | 凭证打印格式明细（格式取值字段明细） |
| `BV_PRINT_MATCH_RULE` | print | 凭证打印格式匹配规则（匹配字段与匹配值） |
| `BV_PRINT_VOU_OPINFO` | print | 凭证打印操作信息（套打占用记录：业务数据、凭证号、种类） |
| `BV_PAYEE_CHEQUE` | cheque | 收款支票信息（支票收款登记与复核 / 台账状态机） |
| `BV_LOG` | log | 空白凭证日志（入库 / 流转 / 打印等操作留痕） |

> 关联说明：表间 16 条引用关系均有 DAO 代码证据（如 `BV_VOU_INFO_DTL.INPUT_ID → BV_VOU_INPUT.ID`、`BV_VOU_EMPLOY_INFO.VOUDTL_ID → BV_VOU_INFO_DTL.ID`、`BV_PRINT_FORMAT_DTL.PRINT_ID → BV_PRINT_FORMAT.ID`），可视化见 `cashvoucher_erd.png`。`CORP_ID` / `BANK_ID` / `CUR_ID` / `KEEPER_UID` 为 Dubbo 取数的远端冗余字段，不构成本地表关联；`BV_LOG.QUOTE_ID` 为多态引用（指向入库单 / 明细 / 套打操作 / 支票）。

### 接口能力
对外仅通过 adapter 层 HTTP Controller 暴露（web 管理端 + `/v1-*` REST 版），无对外 Dubbo 提供者，也不对外发送 MQ：

- `/bill/blankvou/bvvouchertype`：凭证种类 / 打印场景 / 取数字段维护（删除前校验被入库与明细引用）
- `/bill/blankvou/bvvouinput`：空白凭证入库登记、列表与状态流转
- `/bill/blankvou/bvvouinfodtl`：入库明细台账（出售 / 退票 / 丢失 / 作废 / 批量移库）
- `/bill/blankvou/bvvouemployinfo`：凭证领用 / 使用登记
- `/bill/blankvou/bvstoragebox`：库箱维护（启用 / 停用 / 删除）
- `/bill/blankvou/bvprintformat`：打印格式与匹配规则维护（含格式复制）
- `/bill/blankvou/bvprintvouopinfo`：套打操作与凭证号取号占用
- `/bill/blankvou/bvpayeecheque`：支票收款登记
- `/bill/blankvou/bvpayeechequecheck`：支票复核（通过 / 打回）
- `/bill/blankvou/bvpayeechequemanage`：支票台账处理（退票 / 作废 / 托收 / 到账）
- `/cashVoucher/import/excel`、saas 版 `/v1-cashVoucher/cashVoucher/import/excel`：Excel 导入

### 依赖服务
- `bytter-system`（Dubbo 消费）：单位（`ICorpService`）、银行账号（`IBtBankAccService`）、币种（`IBtCurrencyService`）、汇率调整（`IFcExcrateAdjustService`）、用户（`IUserService`）、字典（`IDictService`）、通知（`INotificationService`）、国际化（`Ii18nService` / `IMultiLangService`）
- `bytter-prepose-bill`（Dubbo 消费）：套打完成后回调 `IVoucherPrintService.printComplete`
- `bytter-service-payment`（Dubbo 消费）：凭证打印相关 `IVoucherPrintingService`
- 无 `bytter-workflow` 审批（支票复核为服务内置状态机 `BvPayeeChequeCheckService`）、无 MQ、无定时任务

## 业务流交付索引
- 空白凭证业务主流程（节点 × 涉及表透镜，7 节点 / 20 表引用）：`tools/service-db-diagram/work/cashvoucher_business_flow.workflow.tables.html`
- 支票收款业务主流程（节点 × 涉及表透镜，6 节点 / 12 表引用）：`tools/service-db-diagram/work/cashvoucher_cheque_business_flow.workflow.tables.html`
- 自有表关系佐证（13 表 / 16 条代码证据边）：`tools/service-db-diagram/work/cashvoucher_erd.png`
