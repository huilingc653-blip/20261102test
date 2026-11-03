# 原始脚本同步映射

本清单用于核对 `sql/saas/mysql`、`sql/saas/oracle` 的所有原脚本均已进入新基线或存量升级快照。

| MySQL 原脚本 | 新位置 |
| --- | --- |
| `1.Init-tables.sql` | `mysql/01-base-schema.sql` |
| `1.Init-data-tables-ng.sql` | `mysql/02-ng-schema-data.sql` |
| `2.Init-data.sql` | `mysql/03-base-data.sql` |
| `3.Init-data-append.sql` | `mysql/03-base-data.sql` |
| `4.Init-data-bt_input_bank_info.sql` | `mysql/04-bank-input-data.sql` |
| `5.Init-functions-views.sql` | `mysql/05-functions-views.sql` |
| `6.DbUpdate.sql` | 最终结果进入 `mysql/01-base-schema.sql`、`mysql/03-base-data.sql`；原文件保留存量升级能力 |
| `7.DbUpdate.sql` | 最终结果进入 `mysql/03-base-data.sql`；原文件保留存量升级能力 |
| `2.DbUpdate-ng.sql` | 空脚本；仅保留原文件 |
| `bank-enterprise-ddl.sql` | `mysql/09-bank-schema-data.sql` |
| `bytter-service-foreign_ddl.sql` | `mysql/06-business-schema.sql` |
| `bytter-service-foreign_dml.sql` | `mysql/07-business-data.sql` |
| `scf-table-init.sql` | `mysql/06-business-schema.sql`、`mysql/07-business-data.sql` |
| `scf-data.sql` | `mysql/07-business-data.sql` |
| `bytter-management-ddl.sql` | 空脚本；仅保留原文件 |
| `bytter-management-dml.sql` | `mysql/08-management-data.sql` |
| `bytter-service-bill_mysql_dml.sql` | `mysql/07-business-data.sql` |
| `bytter-service-paymentlimit_mysql_ddl.sql` | `mysql/06-business-schema.sql` |
| `bytter-service-paymentlimit_mysql_dml.sql` | `mysql/07-business-data.sql` |
| `init_ng_management_dml.sql` | `mysql/08-management-data.sql` |

| Oracle 原脚本 | 新位置 |
| --- | --- |
| `1.Init-tables-oracle.sql` | `oracle/01-base-schema.sql` |
| `1.Init-data-tables-ng-oracle.sql` | `oracle/02-ng-schema-data.sql` |
| `2.Init-data-oracle.sql` | DDL 进入 `oracle/01-base-schema.sql`；DML 进入 `oracle/03-base-data.sql` |
| `3.Init-data-append-oracle.sql` | DDL 进入 `oracle/01-base-schema.sql`；DML 进入 `oracle/03-base-data.sql` |
| `4.Init-data-bt_input_bank_info-oracle.sql` | DDL 进入 `oracle/01-base-schema.sql`；DML 进入 `oracle/04-bank-input-data.sql` |
| `5.Init-functions-views.sql` | `oracle/05-functions-views.sql` |
| `6.DbUpdate-oracle.sql` | 最终结果进入 `oracle/01-base-schema.sql`、`oracle/03-base-data.sql`；原文件保留存量升级能力 |
| `7.DbUpdate-oracle.sql` | 最终结果进入 `oracle/03-base-data.sql`；原文件保留存量升级能力 |
| `2.DbUpdate-ng-oracle.sql` | 空脚本；仅保留原文件 |
| `bank-enterprise-ddl.sql` | `oracle/09-bank-schema-data.sql` |
| `bytter-service-foreign_ddl.sql` | `oracle/06-business-schema.sql` |
| `bytter-service-foreign_dml.sql` | `oracle/07-business-data.sql` |
| `scf-table-init-oracle.sql` | `oracle/06-business-schema.sql`、`oracle/07-business-data.sql` |
| `scf-data-oracle.sql` | `oracle/07-business-data.sql` |
| `bytter-management-ddl.sql` | 空脚本；仅保留原文件 |
| `bytter-management-dml.sql` | `oracle/08-management-data.sql` |
| `bytter-manager-account-oracle-ddl.sql` | `oracle/06-business-schema.sql` |
| `bytter-manager-account-oracle-dml.sql` | `oracle/07-business-data.sql` |
| `bytter-service-bill_dml.sql` | `oracle/07-business-data.sql` |
| `bytter-service-fundwarn.sql` | `oracle/06-business-schema.sql`、`oracle/07-business-data.sql` |
| `bytter-service-increment-oracle.sql` | 空脚本；仅保留原文件 |
| `bytter-service-paymentlimit_oracle_ddl.sql` | `oracle/06-business-schema.sql` |
| `bytter-service-paymentlimit_oracle_dml.sql` | `oracle/07-business-data.sql` |
| `bytter-system-oracle-dml.sql` | `oracle/03-base-data.sql` |

## 核对规则

- 基线只吸收最终结构和可幂等的数据语义，不复制破坏性 `DROP TABLE`。
- 03 阶段用 `BEGIN/END UPGRADE MERGE` 标明每个原始升级脚本来源。
- 混合脚本中的 DDL 与 DML分别进入 schema、data 阶段。
- 空的 DbUpdate-ng 脚本不进入执行入口。
- `bytter-*` 业务增量交付脚本（含存量结构升级、破坏性结构演进或动态建表语义）保留原文件，不吸收进初始化基线；新装环境如需对应业务模块，按原脚本在存量增量路径执行。
- 每次重新生成后运行 `.aiassistant/skills/sql-standardize/scripts/Test-IdempotentSql.ps1`，检查本表覆盖原目录全部文件。
## 中文乱码待确认清单

当前无待确认的中文乱码字段。
