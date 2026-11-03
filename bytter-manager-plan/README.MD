# 资金计划模块（bytter-manager-plan）

## 模块介绍

### 主要业务
- 资金计划编制与上报：按计划周期类型（年/季/月/周/天）编制计划主档与上报明细，科目取自计划科目树；支持单位上报（汇总后占用）与单位受控开关、自定义周期参照、计划汇率调整口径；上报提审后进入 `fbsBudgetInput` 工作流。
- 计划审批与落地：平台流程审批通过/打回；通过后按预算模式落地——固定模式直接生效；年分解/月分解生成分解明细，月滚动/滚动+双月周/月分解周生成周期计划。
- 追加计划：预算不足时走 `fbsBudgetSuperadd` 追加计划独立提审审批，审批通过后将追加金额回写主计划明细（`BSUPER_MONEY`/`BTEMP_MONEY`）。
- 执行占用与校验：外部单据占用前做单位受控判断与规则链匹配（优先级：1 临时→2 未编→3 父周期→4 上期比例→5 先执行不控制），计算可执行金额；单据提交写计划执行占用（`FBS_BUDGET_PROCESS` 0 暂存→11 在途）并回写明细执行金额；业务单据审批联动置 95 占用、打回置 -1；单据打回/退单/删除时逆向释放占用并还原明细执行金额。
- 执行台账与分析：执行台账按单据/科目/单位查看执行占用，销售回款差异录入与差异分析。

### 代码分层
```
- bytter-manager-plan

    - bytter-plan-adapter

      适配层，对应 MVC 的 Controller，使用 web|mobile|wap 按包名隔离，VM 资源存放于此层
    - bytter-plan-client

      定义暴露的 API 接口，供其他服务调用
    - bytter-plan-domain

      主要业务处理接口实现、数据库访问与数据组装，后续 DDD 改造升级重点重构模块
    - bytter-plan-start

      启动类、yml 配置文件
```

### 数据库

- 数据库：Oracle，schema 用户 `bank430`（`jdbc:oracle:thin:@192.168.0.217:1521/orcl`），与全平台其他服务共用同一模式，无独立 schema。
- 本服务代码 `@Table` 注解共映射 21 张表，均为业务表（无历史归档表/临时表）。

### 领域内业务表

> 业务表口径：与 increment 一致，历史归档表（`*_HIS`）、临时表（`*_TMP`/`*_TEMP`）单列于本页末尾；表名为代码 `@Table` 实体注解映射值，`说明`取自实体类 Javadoc `@Description` 或 SQL `COMMENT ON TABLE`，空项以 `—` 表示（对应表可查 Oracle `ALL_TAB_COMMENTS` 或实体注释确认语义）。同表被多实体映射时，实体以 `/` 分隔。

业务表按计划业务子域分组，共 21 张：

#### 计划主档与明细（2 张）

| 表名 | 说明 |
| --- | --- |
| `FBS_BUDGET` | 资金计划主表 |
| `FBS_BUDGET_DETAIL` | 资金计划明细表 |

#### 周期上报明细（6 张）

| 表名 | 说明 |
| --- | --- |
| `FBS_BUDGET_YEAR` | 年计划上报明细表 |
| `FBS_BUDGET_QUARTER` | 季计划上报明细表 |
| `FBS_BUDGET_MONTH` | 月计划上报明细表 |
| `FBS_BUDGET_WEEK` | 周计划上报明细表 |
| `FBS_BUDGET_DAY` | 天计划上报明细表 |
| `FBS_BUDGET_PERIOD` | 资金计划自定义周期表 |

#### 计划基础资料（4 张）

| 表名 | 说明 |
| --- | --- |
| `FBS_ITEM` | 计划科目表 |
| `FBS_CORP_ITEM` | 单位项目对照表 |
| `FBS_CORP_BUDGET` | 单位资金计划控制表 |
| `FBS_EXCRATE_ADJUST` | 计划汇率调整表 |

#### 计划追加（2 张）

| 表名 | 说明 |
| --- | --- |
| `FBS_BUDGET_SUPERADD` | 资金计划追加主表 |
| `FBS_BUDGET_SUPERADD_DETAIL` | 资金计划追加明细表 |

#### 计划分解与汇总（3 张）

| 表名 | 说明 |
| --- | --- |
| `FBS_BUDGET_RESOLVE_YEAR` | 年计划分解表 |
| `FBS_BUDGET_RESOLVE_MONTH` | 月计划分解表 |
| `FBS_COLLECT_CORP` | 预算汇总本单位 |

#### 计划执行与规则（3 张）

| 表名 | 说明 |
| --- | --- |
| `FBS_BUDGET_PROCESS` | 资金计划执行表 |
| `FBS_PROCESS_RULE` | 计划执行规则表 |
| `FBS_SALESRETURN_ANALYSE` | 销售回款差异分析表 |

#### 计划日志（1 张）

| 表名 | 说明 |
| --- | --- |
| `FBS_MESSAGE` | 计划日志表 |

### 接口能力

> 说明：本服务对外 API 定义于 `-client` 模块（见仓库代码），此处不再重复罗列。

### 依赖服务

> 说明：跨服务调用关系见仓库实现（Dubbo 引用），此处不再重复罗列。
