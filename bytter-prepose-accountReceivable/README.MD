# 应收清收模块（bytter-prepose-accountReceivable）

> 模块描述：T6 微服务「应收款清收」，负责销售应收单的登记、审批、核销与收款接力，业务域包名为 `com.bytter.accountReceivable`。

## 模块介绍

### 主要业务
- 应收单管理：应收单登记 / 变更 / 作废 / 审批、Excel 批量导入、发票信息挂接（`DOC_INVOICE`）
- 应收核销：以银行收付流水（bdlink）、票据（bill）、信用证到单（credential）等为核销对象，支持试算、确认核销、取消核销、强制核销、核销审批与自动核销规则
- 账龄分析与逾期催收：账龄区间分析、应收单逾期催收登记
- 应收查询与 BI：应收单列表 / 明细查询（含核销历史与收款单反查）、首页应收指标（余额、周转率、趋势、单据类型 / 账龄分布）
- 基础配置：核销相关基础配置与恢复初始化

主流程：应收单登记 → 提交 → 审批（通过 / 打回）→ 生效 → 选择核销收款项 → 确认核销 → 核销审批 → 核销入账 → 收款单生成（接力 `bytter-service-payment`）。可视化索引见文末「业务流交付索引」。

### 代码分层
```
- bytter-prepose-accountReceivable

    - bytter-accountReceivable-adapter

      适配层，对应 MVC 的 Controller，按包名隔离；VM 资源存放于此层。
      saas/ 子包另提供 /v1-* 前缀的 REST(JSON) 版控制器（返回体裁与路由前缀不同，业务一致）
    - bytter-accountReceivable-client

      定义暴露给其他服务调用的 API 契约（当前仅资源常量；本服务不对外暴露 Dubbo 接口）
    - bytter-accountReceivable-domain

      主要业务处理接口实现、数据库访问与数据组装，后续 DDD 改造升级重点重构模块
    - bytter-accountReceivable-start

      启动类、yml 配置文件
```

### 数据库
- 数据库：Oracle（`ojdbc8` + Druid 连接池，依赖见根 `pom.xml`）；连接串与账号由 Nacos 共享配置 `common.yml` 下发，各环境地址见 `bootstrap.yml`。

### 领域内业务表
> 口径：仅列本服务自有 JPA `@Entity` 映射的业务表；类型基表、历史表、日志表及跨模块只读引用表不计入。说明取自实体类字段注释。

| 表名 | 所属子域 | 描述 |
| --- | --- | --- |
| `DOC_MANAGE` | docManage | 应收单主表（单据编号、客商、应收金额、币种、结算方式、单据 / 到期 / 账龄起算日期、核销状态与应收余额） |
| `DOC_INVOICE` | docManage | 应收发票信息表（发票代码 / 号码、票面与不含税金额、开票日期、挂接业务单据） |
| `DOC_WIRTE_OFF_RELATION` | writeOff | 应收核销关系表（核销批次、执行金额、核销前余额、核销类型与状态；表名保留源码拼写 `WIRTE`） |
| `DOC_OVERDUE_NOTICE` | query | 应收单逾期催收登记表（逾期原因、催收人 / 时间 / 结果） |

> 核销对象数据源为跨模块同库只读直查（非本服务自有表）：银行收付流水 `bis_acc_dtl`（bdlink）、票据 `ab_voucherinfo` / `ab_accrecords`（`bytter-prepose-bill`）、信用证到单 `lc_bill_arrival` / `lc_mortgage` / `lc_incoming`（`bytter-prepose-credential`）。

### 接口能力
对外仅通过 adapter 层 HTTP Controller 暴露（web 管理端 + `/v1-*` REST 版），无对外 Dubbo 提供者，也不对外发送 MQ：

- `/accountReceivable/docManage`：应收单登记 / 编辑 / 变更 / 作废 / 审批 / 批量删除与导入
- `/accountReceivable/writeOff`：核销主控（核销数据源、试算、确认 / 取消 / 强制核销、核销审批、自动核销规则）
- `/accountReceivable/agingAnalysis`：账龄分析查询
- `/accountReceivable/query/docManage`：应收单查询（含核销历史与收款单反查）
- `/accountReceivable/query/docOverdueNotice`：逾期催收登记与列表
- `/bi/receivables`：应收首页 BI（AR 余额 / 周转率 / 趋势 / 账龄与单据类型分布）
- `/preposeAccountReceivable/bpm/task`：审批任务处理（待办 / 认领 / 完成 / 撤销）
- `/accountReceivable/businessConfig/baseConfig`：基础配置与恢复初始化
- `/accountReceivable/import/excel`：Excel 导入（模板缓存 + 分页读取）
- `/accountReceivable/guide`：使用指引
- saas 版：`/v1-accountReceivable/accountReceivable/import/excel`、`/preposeAccountReceivable/v1-workflow/bpm/task`

### 依赖服务
- `bytter-system`（Dubbo 消费）：单位、币种、字典、客商、银行账号、授权、配置等基础资料
- `bytter-service-payment`（Dubbo 消费）：核销入账生成收款单（`INisReceivableService`）、业务消息 / 操作日志（`INisMessageService`）、结算方式 / 凭证类型（`IFcVoucherTypeService`）
- `bytter-service-bdlink`（Dubbo 消费）：银企直连银行收付流水 `bis_acc_dtl`（核销对象）
- `bytter-manager-plan`（Dubbo 消费）：应收单保存时的预算过程控制（预算占用 / 校验）
- `bytter-prepose-bill`、`bytter-prepose-credential`：票据、信用证到单核销对象（同库直查）
- 工作流：内嵌 `bytter-workflow-domain`（工作流引擎合并进各服务），`IWorkFlowService` 本地注入；审批事件经 MQ 绑定 `workflowEvent` 消费

> 遗留说明：`start/application.yml` 中存在 `billEndorsementToPayment`（目标交换机 `payment-billendorsement-exchange`）的 MQ 绑定，但本模块源码无对应 Consumer Bean，疑为从票据模块拷贝遗留的配置，业务上未使用。

## 业务流交付索引
- 业务主流程（节点 × 涉及表透镜，9 节点 / 14 表引用）：`tools/service-db-diagram/work/accountReceivable_business_flow.workflow.tables.html`
- 自有表关系佐证（4 表 / 3 边）：`tools/service-db-diagram/work/accountReceivable_ledger_erd.png`
