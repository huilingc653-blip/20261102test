## 基础数据模块

### T6项目bt-biz-sys迁移

#### 模块介绍

**定位**：bt-saas 的**平台底座 / 基础数据服务**——维护「单位与组织 + 用户与权限」访问控制体系，
以及币别汇率定价、银行与账户、会计科目与内部账户、字典参数区域附件、通知与任务调度等公共主数据，
并以 Dubbo 向全平台其余服务输出能力。**无本服务自有审批流**（敏感操作走双管理员复核），消息通道为
Spring Cloud Stream（企业日终初始化）+ Redis List（操作日志异步）。

```
- bytter-system

    - bytter-system-adapter

      适配层对应mvc的controller层使用web|mobile|wap按包名隔离，vm资源存放此层
      （web/ 主入口，另含 bankacc/ rate/ pricing/ period/ subject/ mobile/ excel/ poi/ saas/ config/）

    - bytter-system-client

      定义暴露的api接口给其他服务调用（ICorpService / IUserService / IMenuService / IBtCurrencyService /
      IDictService / IConfigService / IAttachmentService / INotificationService / IFcSubject* /
      IPricingDepositRateService / IAccountGrantService / ISysTenantManagerService / TenantProvider 等）

    - bytter-system-domain

      主要业务处理接口实现、数据库访问与数据组装后续DDD改造升级重点重构模块
      （com.bytter.system 平台能力 · com.bytter.basic 基础资料 · com.bytter.subject 科目 ·
        com.bytter.pricing 定价 · com.bytter.dual 双管理员 · com.bytter.third 第三方对接）

    - bytter-system-start

      启动类、yml配置文件
```

#### 领域业务表（119 张）

> 2026-09-11 按 JPA 实体逐表核对：本服务实体最多（119 张），中文说明取实体 `@Description`；
> 账本中 29 张无实体注释的表按表名/实体语义命名（词库中标记来源）。其中 `TB_CREDIT_PERIOD` 在
> system（定价域＝写入方）注释为「存款利率类型」，在 report（只读）注释为「贷款期限」——本 README 取**写入方口径**。
> 关系边 76 条：10 条经 DAO SQL 核实为 `observed`，其余 66 条为扫描器按列名推断（`*_ID → ID` 形态，指向
> `SYS_USER` / `SYS_CORP` / `SYS_MENU` / `SYS_ROLE` / `SYS_COUNTRY` / `BT_BANK_TYPE` / `SYS_ATTACHMENT` 等主表）。

**单位与组织（18 张）**

| 表名 | 中文说明 |
|---|---|
| SYS_CORP | 单位（网点）表 |
| SYS_CORP_CM_MAP | 单位现金管理映射表 |
| SYS_EQUITY_MAINTENANCE | 单位股权维护表 |
| SYS_EXTERNALCORP_CORP | 客商单位中间表 |
| SYS_EXTERNAL_CORP | 客商 |
| SYS_EXTERNAL_CORP_AUDIT_LOG | 客商审核表 |
| SYS_EXTERNAL_CORP_BANKACC | 客商与银行账号关系 |
| SYS_EXTERNAL_CORP_QUICK_PAY | 客商快捷支付信息实体 |
| SYS_OPERATION_AREA | 经营区域表 |
| SYS_ORGANTYPE | 组织架构表 |
| SYS_ORGANTYPE_CORP | 组织架构单位表 |
| SYS_ORGANTYPE_CORP_RELEASE | 组织架构单位发布表 |
| SYS_ORGANTYPE_MODULE | 组织架构模块表 |
| SYS_ORGANTYPE_RELEASE | 组织架构发布表 |
| SYS_PROVINCE | 省份表 |
| SYS_REGION | 区域表 |
| SYS_TENANT_MANAGER | 租户实体类 |
| SYS_UNIT_STORE_MAINTENANCE | 单位仓库维护表 |

**用户与权限（20 张）**

| 表名 | 中文说明 |
|---|---|
| SYS_ACCOUNT_GRANT | 账号授权 |
| SYS_ACCOUNT_GRANT_HIS | 账号授权历史记录 |
| SYS_DUAL_MANAGER | 双管理员 |
| SYS_HOME | 用户首页组件布局 |
| SYS_HOME_WIDGET | 首页组件 |
| SYS_MENU | 菜单 |
| SYS_MENU_COLLECT | 用户菜单收藏 |
| SYS_PERMISSION | 权限表 |
| SYS_RESOURCES | 资源表 |
| SYS_ROLE | 角色表 |
| SYS_ROLE_MENU | 角色菜单关联关系表 |
| SYS_TABLE_RECORD | 用户表格使用习惯记录 |
| SYS_USER | 用户表 |
| SYS_USER_CONFIG | 记录用户配置 |
| SYS_USER_CORP_ROLE | 用户单位角色表 |
| SYS_USER_FACE_DETECTION | 人脸识别存储 |
| SYS_USER_HI_PWD | 用户历史密码 |
| SYS_USER_LOGIN_LOG | 用户登录日志 |
| SYS_USER_ROLE | 用户角色关联关系表 |
| USER_MOBILE_CHART_MODULE | 用户-APP报表模块关联状态表 |

**银行与账户（27 张）**

| 表名 | 中文说明 |
|---|---|
| BIS_ACC_ATTRIBUTE | 账户属性表 |
| BIS_ACC_DTL_IMPORT_CONFIG | 银行流水导入配置表 |
| BIS_ACC_DTL_IMPORT_CONFIGDTL | 银行流水导入配置明细表 |
| BIS_ACC_GATHER_POLICY | 银行归集策略关联表 |
| BIS_BANK_BRANCH | 办理机构表 |
| BIS_BIF_INIT | 接口代码表 |
| BIS_BIF_RULE | 接口代码业务规则表 |
| BIS_GATHER_POLICY | 银行归集策略表 |
| BIS_VIRTUAL_ACC_EXC | 虚拟账户指令表 |
| BT_BANK | 开户行信息 |
| BT_BANKACC_MESSAGE | 银行账户消息日志表 |
| BT_BANK_ACC | 银行账户表 |
| BT_BANK_ACC_APP | 银行账号申请记录表 |
| BT_BANK_ACC_APP_CUR | 账号币别对应表（离线） |
| BT_BANK_ACC_CUR | 账号币别对应表 |
| BT_BANK_ACC_DTL | 银行账户明细表 |
| BT_BANK_ACC_FREEZE | 银行账户冻结表 |
| BT_BANK_ACC_VIRTUAL | 银行虚拟账户表 |
| BT_BANK_TYPE | 银行类型表 |
| BT_BANK_TYPE_CUSTOM | 银行类型自定义表 |
| BT_BANK_TYPE_OTHER_CUSTOM | 银行类型其他自定义表 |
| BT_INPUT_BANK_INFO | 联行号 |
| BT_INPUT_BANK_SWIFT | 联行号 SWIFT 表 |
| BT_NET_UKEY | 网银信息表 |
| BT_NET_UKEY_BANKACC | 网银信息账号关联表 |
| BT_SWIFT_INFO | 境外银行行号信息表 |
| NIS_BINDING_BANKACC | 银行账号绑定账号中间表 |

**币别汇率与定价（8 张）**

| 表名 | 中文说明 |
|---|---|
| BT_CURRENCY | 币别表 |
| BT_CURRENCY_INIT | 系统币别表 |
| BT_CURRENCY_PAIR | 货币对管理表 |
| BT_RATE_TYPE | 汇率类型表 |
| FC_EXCRATE_ADJUST | 币别汇率表 |
| TB_BASE_RATE | 基准利率表 |
| TB_CREDIT_PERIOD | 存款利率类型表 |
| TB_LOAN_RATE_MAINTENCE | 贷款利率维护 |

**科目与内部账户（13 张）**

| 表名 | 中文说明 |
|---|---|
| FC_ACC | 内部账户信息表 |
| FC_ACC_HISTORY | 内部账户历史表 |
| FC_ACC_LOG | 内部账户日志表 |
| FC_CORP_POOL | 资金池单位表 |
| FC_INIT_ACC | 内部初始账户表 |
| FC_INSTRATE | 科目利率数据维护表 |
| FC_INSTRATE_ADJUST | 内部利率调整表 |
| FC_INTEREST_TYPE | 利息类型表 |
| FC_SUBJECT | 会记科目表 |
| FC_SUBJECT_DAY | 科目日结单表 |
| FC_SUBJECT_INIT | 科目初始化表 |
| FC_SUBJECT_POOL | 资金池会计科目表 |
| FC_SUBJECT_SORT | 系统科目表 |

**字典参数与附件（11 张）**

| 表名 | 中文说明 |
|---|---|
| SYS_ARTICLE | 政策法规,重要信息 |
| SYS_ATTACHMENT | 附件表 |
| SYS_CALENDAR | 日历表 |
| SYS_CONFIG | 系统参数配置 |
| SYS_CONFIG_LOG | 系统参数配置更改日志 |
| SYS_COUNTRY | 国家表 |
| SYS_COUNTRY_CUST | 国家地区表 |
| SYS_DICT | 字典表 |
| SYS_DICT_GROUP | 字典组 |
| SYS_MULTI_LANG | 国际化信息表 |
| SYS_TIME_ZONE | 系统时区表 |

**通知任务与用途（15 张）**

| 表名 | 中文说明 |
|---|---|
| BT_PURPOSE | 用途表 |
| BT_PURPOSE_BANK_TYPE | 用途银行类别中间表 |
| BT_PURPOSE_CORP | 用途单位中间表 |
| BT_PURPOSE_COUNTRY | 用途地区中间表 |
| BT_PURPOSE_CUR | 用途币种中间表 |
| SYS_BLACK_WHITE_LIST | 黑白名单信息表 |
| SYS_LOG | 日志表 |
| SYS_NOTIFICATION | 消息通知表 |
| SYS_NOTIFICATION_CATEGORY | 消息通知分类表 |
| SYS_NOTIFICATION_EXT | 通知扩展表 |
| SYS_NOTIFICATION_HISTORIC | 消息通知历史 |
| SYS_NOTIFICATION_MESSAGE | 通知消息表 |
| SYS_NOTIFICATION_ROLE | 消息角色过滤 |
| SYS_NOTIFICATION_USER | 通知用户表 |
| SYS_TASK | 后台任务表 |

**其他主数据（7 张）**

| 表名 | 中文说明 |
|---|---|
| BT_ACC_NATURE | 账户性质表 |
| SYS_ACCOUNT_MAPPING | 用户账号映射关系表 |
| SYS_BANK_REGION | BT_INPUT_BANK_INFO与sys_region对应关系表 |
| SYS_CA_INFO | CA信息表 |
| SYS_CELL_MANAGE | 管理单元表 |
| SYS_MODULE | 模块管理 |
| SYS_SUPPLIER_TYPE | 供应商类别表 |

#### 业务主流程

1. **单位与组织**：租户签约（`SYS_TENANT_MANAGER`，容量/有效期校验）→ 单位（网点）建立与启停/上级迁移
   （`CorpService.save/update/transferCorp`，网点改单位触发日结 MQ）→ 组织架构发布与单位层级维护
   （`OrgantypeService.release/init`，迁移仅保留三级以内）→ 经营区域/股权/仓库/省份等配套档案。
2. **用户与权限授权**：用户建立（校验租户用户数上限 +「单位最多 2 名管理员」）→ 用户分配角色（`SYS_USER_ROLE`）
   → 角色-菜单/资源授权（`SYS_ROLE_MENU` / `SYS_PERMISSION`）→ 用户特殊权限（`sys_corp_user_resource`）
   → 账号授权（`SYS_ACCOUNT_GRANT`，查询/余额/转账权限位 + 历史留痕）；授权变更后清 Redis 权限缓存并重算。
3. **双管理员复核（dual）**：重置密码/授权/角色分配等敏感操作包装为命令 + `SYS_DUAL_MANAGER` 台账 →
   走 `sysDualManager` 工作流 → `flowCompleted` 后执行真实命令；配置 `dualManager` 关闭则直接执行。
4. **银行与账户**：银行与银行类型、联行号/SWIFT（含 Excel 导入 + 行名不一致同步）→ 银行账户申请-审核-建账
   （`BT_BANK_ACC_APP → BT_BANK_ACC`，含账户币种/明细/虚拟账户）→ 冻结走工作流 `netbankBankaccFreeze` →
   UKey 与 CFCA 证书到期由 XXL-Job 提醒。
5. **币别汇率与定价**：币别（本位币切换、系统币种关联）→ 汇率类型与币种对 → 汇率调整复核
   （`FcExcrateAdjust` 提交/批量复核/打回）→ 基准利率与存款利率维护及复核（`TB_BASE_RATE` / `TB_LOAN_RATE_MAINTENCE`）。
6. **科目与内部账户**：会计科目（树/类别/初始化科目导入）→ 资金池科目（自初始化科目导入并去重）→
   科目日结/月结（`FC_SUBJECT_DAY` 按科目/单位/币别汇总）→ 科目利率与内部利率调整复核 → 内部账户（含历史与日志）。
7. **通知与任务调度**：`INotificationService` 按分类/角色/用户推送 → `SYS_NOTIFICATION`（历史按 `NOTIFICAT_SAVE_DAYS` 清理）；
   XXL-Job 8 个 + 自研 `SYS_TASK`（`runnow` 立即执行 / 启停）；阈值与开关由 `SYS_CONFIG` / `SYS_DICT` 驱动。
8. **对外服务**：以 Dubbo 向 payment / workflow / ras / report / increment / check / plan / account 等提供
   单位、用户、菜单、币别、字典、参数、附件、区域、通知、科目、定价、账号授权、租户等能力；
   `SystemReferenceConfig` 以 `@Bean` 统一暴露常用引用供各服务注入。

#### 状态与审批

- **状态/枚举（节选）**：`SYS_CORP.status` 0 停用/1 启用、`type` 0 网点/1 单位；`SYS_USER.status` 0 删除/1 正常/2 禁止/3 锁定/4 T2 初始化；
  `SYS_ROLE.useable=2` 为系统角色（不可改删）；`SYS_MENU.status` 1 启用；`SYS_RESOURCES` 启用/停用；
  `SYS_ORGANTYPE.validSign` 0 禁用/1 启用/2 删除；`SYS_TENANT_MANAGER.tenantStatus` 1 未签约/2 已签约/3 禁用/4 已解约；
  `BT_*` 主数据普遍用 `VALID_SIGN` / `NATURAL_SIGN`（本位币）标志。
- **审批流**：本服务**无自有业务审批流**；仅 ①`BtBankAccFreezeService` 绑定 `netbankBankaccFreeze`（账户冻结）；
  ②`dual` 双管理员以 `sysDualManager` 工作流承载敏感操作复核。账户「处理/开户」的工作流启动代码当前为**注释分支**。
- **失败/异常**：主数据维护以校验为主（唯一性、引用校验 `checkAssociated`、行号 12 位数字、账户/币别关联校验）；
  报表与导出无重试补偿。

#### 接口能力与依赖

- **Dubbo 导出（节选）**：`ICorpService` / `IUserService` / `IMenuService` / `IOrgantypeService` / `IAccountGrantService` /
  `ISysTenantManagerService` / `TenantProvider`（组织与权限）；`IBtCurrencyService` / `IBtRateTypeService` / `IBtCurrencyPairService` /
  `IBtBankService` / `IBtBankAcc*` / `IBtInputBankInfoService` / `IBtSwiftInfoService` / `IBtPurposeService` / `ISysCountryCustService`（基础资料）；
  `IDictService` / `IConfigService` / `IRegionService` / `IAttachmentService` / `INotificationService` / `IMultiLangService` / `Ii18nService` / `ICalendarService`（平台能力）；
  `IFcSubjectService` / `IFcSubjectDayService` / `IFcSubjectPoolService` / `IFcInstrateService`（科目）、`IPricingDepositRateService` / `IDepositRateTypeService`（定价）；
  `IFcExcrateAdjustService` / `IFcInstrateAdjustService` / `IRateMaintenceService`。`com.bytter.dual` 与 `com.bytter.third` **未导出接口**。
- **Dubbo 引用**：`com.bytter.dual` 无；`com.bytter.third` 的 `DslSsoSyncUserService` 消费 `IUserService` / `ICorpService` 写回人员。
- **被消费证据（节选）**：payment（`IBtCurrencyService`/`IConfigService`/`INotificationService`/`IDictService`/`IAttachmentService`）、
  workflow（`ICorpService`/`IUserService`/`INotificationService`）、manager-plan（`ICorpService`/`IOrgantypeService`/`IUserService`）、
  report（各类主数据）、check（`IBtCurrencyService`/`IFcExcrateAdjustService`）、increment（`IPricingDepositRateService`）。
- **定时任务（8 个）**：`AutomaticExecutionSqlJob`（SQL 作业，拦截 delete/truncate）、`SysCalendarService`（日历初始化）、
  `NotificationService`（历史通知清理）、`BtBankAccService`（账户到期提醒）、`BtNetUKeyService`（UKey 到期）、
  `BtBankTypeService`（CFCA 证书到期）、`BtInputBankInfoService`（联行号户名同步）、`dslSsoSyncUserJob`（SSO 用户同步）。
- **消息**：Spring Cloud Stream 发送 `init-net-day-close-out-0`（企业日终初始化，租户维度）；Redis List `oprator-log` 异步落操作日志；
  **未见 RocketMQ/RabbitMQ 消费者**。`ITaskSwitchService` 有接口**未见实现**。

#### 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `system_erd.png` | bytter-system•主数据与权限•数据库表关系图 | 119 表 / 76 边（8 个业务分组：单位与组织 18 / 用户与权限 20 / 银行与账户 27 / 币别汇率与定价 8 / 科目与内部账户 13 / 字典参数与附件 11 / 通知任务与用途 15 / 其他主数据 7） |
| `system_business_flow.workflow.tables.html` | 平台底座（主数据与权限）主流程 | 7 节点 / 65 表引用（交互式节点×表透镜） |

渲染源见 `tools/service-db-diagram/sources/system/`（账本 + `*.workflow.json` + 节点×表 map + 分组 JSON + 规格文件）。
