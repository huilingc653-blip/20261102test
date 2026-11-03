# SaaS 标准化初始化脚本

本目录是 `sql/saas/mysql` 与 `sql/saas/oracle` 的标准化版。原始脚本不修改，便于追溯历史；新装环境使用本目录，存量环境仍按需使用原目录中的 `DbUpdate` 脚本。

## 执行原则

1. 执行前备份数据库，并确认使用空库或可接受补齐预置对象的环境。
2. MySQL 使用命令行客户端进入 `mysql/` 后执行 `SOURCE 00-run-all.sql;`。
3. Oracle 使用 SQL*Plus/SQLcl 进入 `oracle/` 后执行 `@00-run-all.sql`。
4. 阶段脚本面向空库/新装环境可重复执行：MySQL 表和字段采用存在性保护，Oracle 表采用「先删后建」（`DROP TABLE` 吞异常 + `CREATE TABLE`），固定数据采用主键/业务唯一键保护，函数和视图采用替换定义。
5. 可重复执行表示在空库/初始化场景下重复执行结果一致；**Oracle 的「先删后建」会重建表结构，仅适用于空库/新装环境，禁止在已有业务数据的环境执行本初始化脚本**。

## 阶段

| 阶段 | 文件 | 内容 |
| --- | --- | --- |
| 00 | `00-run-all.sql` | 执行入口（MySQL `SOURCE` / Oracle `@`） |
| 01 | `01-base-schema.sql` | 基础表结构 |
| 02 | `02-ng-schema-data.sql` | 内管表结构与预置数据 |
| 03 | `03-base-data.sql` | 基础预置数据与最终修正 |
| 04 | `04-bank-input-data.sql` | 银行机构数据 |
| 05 | `05-functions-views.sql` | 函数与视图 |
| 06 | `06-business-schema.sql` | 业务模块结构 |
| 07 | `07-business-data.sql` | 业务模块数据 |
| 08 | `08-management-data.sql` | 内管业务数据 |
| 09 | `09-bank-schema-data.sql` | 银企直连表结构 |

## 维护约束

- 新字段优先合并到所属 `CREATE TABLE`；存量升级继续使用原目录中的 `DbUpdate`，整理目录不再保存重复副本。
- 新预置数据必须使用固定主键或明确业务唯一键，并带存在性保护。
- MySQL 和 Oracle 必须同步增加同阶段脚本，业务对象保持对应。
- MySQL 禁止在基线脚本中加入无条件 `DROP TABLE` 或 `TRUNCATE TABLE`；Oracle 建表按先删后建方案使用 `DROP TABLE`（`EXCEPTION WHEN OTHERS THEN NULL` 吞异常）。
- `.aiassistant/skills/sql-standardize/scripts/Test-IdempotentSql.ps1` 是静态门禁；真实发布前仍应在 MySQL、Oracle 临时库各连续执行两次并对比对象和数据摘要。

## 已知边界

- 原始数据体量较大，本目录通过静态转换保留原值；没有数据库实例时无法证明方言级执行成功。
- Oracle 对固定数据使用 `IGNORE_ROW_ON_DUPKEY_INDEX` 提示，依赖目标表对应键存在唯一索引；无唯一索引的数据仍应由业务 `NOT EXISTS` 条件保护。
- 原目录中的历史 `DbUpdate` 不能假设全部可重复执行，也不要在本初始化基线执行后再次执行。
