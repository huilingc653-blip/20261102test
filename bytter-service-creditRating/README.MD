# bytter-service-creditRating（信用评级）

> 2026-09-10 依据代码审读与实体/DAO 事实新建：本服务原先无 README，`sql/` 下也**没有对应 DDL**
> （`bytter-prepose-credit_ddl.sql` 是授信额度域 RAT_* 的脚本，与本服务 39 张表无关），表名与关系边证据全部来自实体注释与 DAO 原生 SQL。

## 模块介绍

对**金融机构/单位**做信用评估与评级：采集机构原始数据 → 配置评分指标/计算模型/评级标准 → 创建评价任务 →
逐机构自动取数评分 → 汇总定级并生成信用评估报告，为「授信/授信额度」决策提供评级依据。
本服务对外只暴露 **HTTP Controller**（无 `@DubboService` 导出），对下游（bytter-prepose-bill 等）以 **Dubbo 消费**方式取数。

### 代码分层

```
- bytter-service-creditRating
    - bytter-creditRating-adapter  Controller（含 controller/saas/** 与 flow/controller 工作流待办入口）
    - bytter-creditRating-client   常量（CreditRatingConstant / CreWorkFlowConstant）、DTO
    - bytter-creditRating-domain   entity / dao / service / flow
    - bytter-creditRating-start    启动类（@EnableDubbo）
```

### 子域职责

| 子域（包/类族） | 职责 |
|---|---|
| `.entity` `CrdRawData*` / `CrInstitutionalRawData` / `CrOwnershipStructureData` / `CrContactAddressBook` / `CrVisitRecordRegistration` | 机构原始数据采集与准入 |
| `.entity` `CrScoringIndicator` / `CrIndicatorCategory` / `CrQuaMethodForScoringInd` / `CrdScoringIndicatorValue` / `CrDataSource` | 评分指标与数据源定义 |
| `CrmComputationalModelService` / `CrmScoringIndicatorSolidService` / `CrmScoringGradeService` | 计算模型、指标固化、评级标准 |
| `CrTaskInfoService` / `CrTaskCorpService` / `CrTaskOrgService` / `CtrScoringCorpContrastService` / `CrScoringOrgContrastService` | 评价任务与指标对照 |
| `EvaluationAnalysisService`（评分引擎）/ `EvaluationReportService` / `EvaluationLogService` | 评估分析、报告与操作日志 |
| `CreditLimitService` / `FinancingService` / `LcGuaranteeBillBondService` / `ChoDepositRateService` / `BankAccountService` | 跨服务数据源取数 |

## 领域业务表（39 张）

| 分组 | 表名 | 中文说明 |
|---|---|---|
| 机构原始数据 | RAW_DATA | 原始数据 |
| 机构原始数据 | INSTITUTIONAL_RAW_DATA | 机构数据 |
| 机构原始数据 | SUPPLIER_RAW_DATA | 供应商数据 |
| 机构原始数据 | OWNERSHIP_STRUCTURE_DATA | 股权结构数据 |
| 机构原始数据 | CONTACT_ADDRESS_BOOK | 联系人通讯录 |
| 机构原始数据 | VISIT_RECORD_REGISTRATION | 拜访登记记录 |
| 机构原始数据 | DATA_SOURCE_CATEGORY | 数据来源类别 |
| 指标与数据源 | CR_SCORING_INDICATOR | 评分指标 |
| 指标与数据源 | CR_QUA_METHOD_FOR_SCORING_IND | 评分指标定量方法 |
| 指标与数据源 | CR_INDICATOR_CATEGORY | 指标类别（已弃用） |
| 指标与数据源 | CR_DATA_SOURCE | 评分指标数据源 |
| 指标与数据源 | SCORING_INDICATOR_VALUE | 评分指标数值 |
| 模型与固化 | COMPUTATIONAL_MODEL | 计算模型 |
| 模型与固化 | MODEL_BATCH | 模型批次（实体注释误写「模型公式」，实际字段为批次号） |
| 模型与固化 | MODEL_FORMULA | 模型公式 |
| 模型与固化 | SCORING_INDICATOR_SOLIDIFICATI | 评分指标固化 |
| 模型与固化 | QUANTITATIVE_METHOD_FOR_SIS | 评分指标定量方法固化 |
| 模型与固化 | INDICATOR_CATEGORY_SOLIDIFICA | 指标类别固化（DAO 空实现，当前无生效证据） |
| 评级标准 | SCORING_GRADE | 评分等级 |
| 评级标准 | SCORING_GRADE_LEVEL | 评分等级级别 |
| 评级标准 | SCORING_GRADE_SOLID | 评分等级固化 |
| 评级标准 | SCORING_GRADE_LEVEL_SOLID | 评分等级级别固化 |
| 评价任务 | CR_TASK_INFO | 评价任务主表 |
| 评价任务 | CR_TASK_CORP_INFO | 评价任务组织表 |
| 评价任务 | CR_TASK_ORG_INFO | 评价任务机构表 |
| 评价任务 | CRT_SCORING_CORP_CONTRAST | 单位评分指标对照表 |
| 评价任务 | CR_SCORING_ORG_CONTRAST | 机构评分指标对照表 |
| 评估报告 | EVALUATION_REPORT | 评估报告 |
| 评估报告 | EVALUATION_BATCH | 评估批次 |
| 评估报告 | EVALUATED_DATA_FOR_REPORT | 报告中已评数据 |
| 评估报告 | EVALUATION_LOG | 评估操作日志 |
| 评估报告 | INDICATOR_CATEGORY_FIX_REPORT | 报告中指标类别固化 |
| 评估报告 | MODEL_SOLIDIFICATION_REPORT | 报告中计算模型固化 |
| 评估报告 | MODEL_FORMULA_FIX_REPORT | 报告中模型公式固化 |
| 评估报告 | SCORING_INDICATOR_FIX_REPORT | 报告中评分指标固化 |
| 评估报告 | Q_M_SCORING_I_FIX_REPORT | 报告定量方法固化 |
| 评估报告 | SCORING_GRADE_FIX_REPORT | 报告中评分等级固化 |
| 评估报告 | SCORING_GRADE_LE_FIX_REPORT | 报告中评分级别固化 |
| 评估报告 | SCORING_METRIC_VAL_FIX_REPORT | 报告中评分指标值固化 |

## 业务主流程

1. **机构数据采集**：`CrdRawDataService.dataCollection/registerInstitution` 写 `RAW_DATA`，机构主体写 `INSTITUTIONAL_RAW_DATA`，
   股权结构 / 联系人通讯录 / 拜访登记三张子表按 `institutional_raw_data_id` 挂靠。
2. **机构准入审批**（流程 `creditInstitutionAdd`）：`RAW_DATA.status` 置 `11 新增审批中 / 12 修改审批中`，
   `INSTITUTIONAL_RAW_DATA.status` 走 `000 未准入 → 001 审批中 → 095 已准入`；打回置 `-1`。
3. **指标与模型配置**：评分指标（含定量方法）→ 计算模型（生成 `MODEL_BATCH` 批次并固化指标到
   `SCORING_INDICATOR_SOLIDIFICATI` + `QUANTITATIVE_METHOD_FOR_SIS`）→ 评级标准（`SCORING_GRADE` + `SCORING_GRADE_LEVEL`）。
4. **建单与任务审批**（流程 `crTaskInfoFlow`）：`CrTaskInfoService.submit` 写 `CR_TASK_INFO` / `CR_TASK_CORP_INFO` /
   `CR_TASK_ORG_INFO` / 两侧指标对照表，置 `11 待审批`；审批通过置 `95` 并触发生成评估报告，打回置 `-1`。
5. **逐机构评价评分**（流程 `creditOrgEvaluation`）：评分引擎按机构取数（跨服务）+ 计算指标得分，落 `EVALUATION_BATCH`
   与 `EVALUATED_DATA_FOR_REPORT`；`flowCompleted` 通过后按通过计数递减，**全部通过**才进入定级。
6. **汇总定级**：`EvaluationAnalysisService.finalScoreLevel/calculateScore` 按分数区间映射评级等级，写
   `SCORING_GRADE_FIX_REPORT` / `SCORING_GRADE_LE_FIX_REPORT` / `SCORING_METRIC_VAL_FIX_REPORT` 并回写 `EVALUATION_REPORT`。
7. **报告与查询**：`EvaluationReportService` 按报告 → 各固化从表（模型/公式/指标/定量方法/等级/指标值）组装查询，
   `EvaluationLogService` 记录操作日志。

### 状态机

- **评价任务 `CR_TASK_INFO.status`**：`0 暂存 / 11 待审批 / 95 通过 / -1 打回 / -2 删除`。
- **任务单位 `CR_TASK_CORP_INFO.status`**：`95 通过 / -1 打回`（`CreditRatingConstant.APPROVED/APPROVAL_REJECTED`）。
- **机构原始数据 `RAW_DATA.status`**：`11 新增审批中 / 12 修改审批中 / 95 通过 / -1 打回 / -2 删除`。
- **机构主体 `INSTITUTIONAL_RAW_DATA.status`**：`000 未准入 / 001 准入审批中 / 095 已准入 / -1 打回 / -2 删除`。

### 审批流

| 流程 key | 归属类 | 回调 |
|---|---|---|
| `crTaskInfoFlow` | `CrTaskInfoService` | `flowCompleted` 通过→95、打回→-1，并触发 `createEvaluationReport` |
| `creditOrgEvaluation` | `EvaluationAnalysisService` | `flowCompleted` 逐机构评价状态流转，全通过后定级 |
| `creditInstitutionAdd` | `CrdRawDataService` | `flowCompleted` → `approvePassEnd` 机构准入落状态 |

> 三者均只实现 `flowStarted`（空）与 `flowCompleted`；**撤回（flowRevoke）未见实现**，走工作流侧 `batchRevoke` 统一接口。

## 接口能力与依赖

- **Dubbo 导出**：未见证据（无 `@DubboService`）；对外仅 HTTP Controller + 工作流待办 Controller。
- **Dubbo 引用**：`bytter-prepose-bill`（`ICreditLimitService` 授信敞口/增减、`IFinancingService` 融资、`ILcGuaranteeBillBondService`/`IFcBondHookService` 保证金）、
  `bytter-service-bdlink`（`IBisAccBalService`/`IBisAccDtlService` 账户余额与流水）、`bytter-increment`（`IChoDepositRateService`/`IChoProductRedeemService` 存款利率）、
  `bytter-system`（`ICorpService`/`IDictService`/`ISysCountryCustService`/`IRegionService`/`IBankTypeService`/`IExternalCorpService`/`IBtInputBankInfoService`/`IMultiLangService`）、
  `bytter-workflow`（`IWorkFlowService`）。
- **MQ**：未见证据。 **定时任务（XxlJob/Scheduled/Quartz）**：未见证据。

## 域边界核对

- **授信额度**：本服务**只读**授信敞口与授信增减作为评分数据源（`CreditLimitService` 全部方法只查不写）；
  额度录入/调整在 **bytter-prepose-bill**（`RAT_BKRATION*`/`RAT_URATION*` 表）——**无表级重叠**（全仓检索本服务 39 张表名在 prepose-bill 内 0 命中）。
- **接口级循环依赖**：prepose-bill 的 `LcGuaranteeBillBondDao` 反向 `import com.bytter.biz.creditRating.api.constant.CreditRatingConstant`（依赖本服务 client 常量类）。
- **已知实现瑕疵（待业务复核）**：`CRT_SCORING_CORP_CONTRAST` / `CR_SCORING_ORG_CONTRAST` 的列 `SCORING_INDICATOR_SOLIDIFICATI`
  实际写入的是**源指标 `CR_SCORING_INDICATOR.id`**；`EVALUATION_REPORT.evaluated_data_id` 写入机构 id，而 SQL 侧
  `EVALUATED_DATA_FOR_REPORT.data_id` 关联 `RAW_DATA.id`，两链口径不一致。

## 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `creditRating_erd.png` | bytter-service-creditRating•信用评级•数据库表关系图 | 39 表 / 39 边（6 个业务分组；关系边为 DAO 原生 SQL JOIN 证据，2 条为列名推断） |
| `creditRating_business_flow.workflow.tables.html` | 信用评级与评估报告主流程 | 7 节点 / 51 表引用（交互式节点×表透镜） |

渲染源见 `tools/service-db-diagram/sources/creditRating/`（账本 + `*.workflow.json` + 节点×表 map + 分组 JSON + 规格文件）。
