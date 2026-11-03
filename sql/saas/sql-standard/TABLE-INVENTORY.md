# SQL 标准化脚本表信息汇总

- 生成日期：2026-09-23
- 数据范围：`sql/saas/sql-standard/{mysql,oracle}` 的 `NN-*.sql`
- 口径：对象名按未加引号标识符大小写归一；字段数=建表括号内字段定义数；主键数=CREATE/ALTER 中 PRIMARY KEY 净计数；索引数=二级索引数；字段新增/修改/删除=ALTER TABLE 的 ADD/MODIFY|CHANGE|RENAME/DROP 语句项；数据新增/修改/删除=INSERT/UPDATE/DELETE 语句数
- 来源范围：`sql/saas/{mysql,oracle}` 全部 `.sql`；产物范围：`sql/saas/sql-standard/{mysql,oracle}` 的 `01~09` SQL（排除执行入口 `00-run-all.sql`）
- 限制：这是静态正则盘点；复杂动态 SQL、PL/SQL、表达式索引及跨方言类型等价性可能需要人工复核，数量相同不代表内容完全等价

## 总览

| 方言 | 产物对象数 | 主键净数 | 二级索引 | 字段 ADD | 字段 MODIFY/CHANGE/RENAME | 字段 DROP | INSERT | UPDATE | DELETE |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| mysql | 1158 | 1061 | 1475 | 445 | 149 | 0 | 11325 | 814 | 91 |
| oracle | 1158 | 1068 | 1445 | 467 | 125 | 0 | 12162 | 831 | 92 |

## MySQL 来源到产物覆盖

- 来源引用对象：1136；产物对象：1158；source-only：0；generated-only：22

### source-only（来源有引用、产物未识别）

（无）

### generated-only（产物有对象、来源未识别）

- TP_ACCOUNT_TYPE
- TP_BAIL_BILL_JOB
- TP_DEWU_DTL
- TP_DOUYIN_DTL
- TP_DOUYIN_QUERY_TRACKER
- TP_KUAISHOU_BAL
- TP_KUAISHOU_FUND_DTL
- tp_merchant_auth
- TP_OFFLINE_BAL
- TP_OFFLINE_BOND_BAL
- TP_OFFLINE_BOND_FLOW_DTL
- TP_OFFLINE_FLOW_DTL
- TP_PINDUODUO_DTL
- tp_platform_interface_info
- TP_PLATFORM_PARAM
- TP_TMALL_BAIL_BILL
- TP_TMALL_BAL
- TP_TMALL_FUND_DTL
- TP_WEIXINSTORE_BAL
- TP_WEIXINSTORE_FUND_BILL
- TP_WEIXINSTORE_RELATED_INFO
- TP_XIAOHONGSHU_DTL

## Oracle 来源到产物覆盖

- 来源引用对象：1158；产物对象：1158；source-only：0；generated-only：0

### source-only（来源有引用、产物未识别）

（无）

### generated-only（产物有对象、来源未识别）

（无）

## MySQL 表清单

| 表名 | 结构文件 | 字段数 | 主键 | 索引 | 字段 ADD | 字段 MODIFY/CHANGE/RENAME | 字段 DROP | INSERT | UPDATE | DELETE |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| ab_accbook_info | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_accrecords | 01-base-schema | 97 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_accrecords_his | 01-base-schema | 73 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_add_credit_info | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_backbillinfo | 01-base-schema | 64 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_acc_bal | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_acc_dtl | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_attachment | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_bank_auto_dtl | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_credit_info_maintain | 01-base-schema | 14 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_middle | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_pay_middle | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_plan_amt_maintain | 01-base-schema | 14 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_receivebiz | 01-base-schema | 109 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_rulebilldtlrmk | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_rulebilldtlrmk_dtl | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_rulebillno | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_sign_strategy | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_sign_strategy_data | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_source_dtl | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| ab_bill_status | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_transfer | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_bill_transfer_dtl | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_brief_status | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| ab_claim_account | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_claim_middle | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_creditinfo_response | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_discount | 01-base-schema | 30 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_draft_exc | 01-base-schema | 18 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_emit_bail_interest_info | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_emit_bill_target_ratio_info | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_emitbill_pay | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_exc | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_field_ismdf | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_intellect_element | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_intellect_rule | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_intellect_rule_dtl | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_intellect_rule_group | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_intellect_rule_group_dtl | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_log | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_bail | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_bail_book | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_bail_dtl | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_bank_exc | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_bustype | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_contract | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_corp | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_dtl | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_dtl_book | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_finaapply_book | 01-base-schema | 40 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_finaapply_dtl | 01-base-schema | 32 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_hold_book | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_into_repeal | 01-base-schema | 54 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_limit | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_limit_allot | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_limit_allotcheck | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_limit_apply | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_limit_applycheck | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_limit_dtl | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_pledgee | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_query_exc | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_pool_sign_info | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_poolif | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_short_funds_time_config | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_voucherinfo | 01-base-schema | 75 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_voucherinfo_his | 01-base-schema | 38 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_voucherinfo_vice | 01-base-schema | 84 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ab_voucherinfo_vice_his | 01-base-schema | 58 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_evt_log | 01-base-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_extend_category | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_extend_delegation | 01-base-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_extend_delegation_rel | 01-base-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_extend_model | 01-base-schema | 23 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_extend_msg_record | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_extend_option | 01-base-schema | 15 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ge_bytearray | 01-base-schema | 11 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ge_property | 01-base-schema | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ge_schema_log | 01-base-schema | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_actinst | 01-base-schema | 21 | 1 | 10 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_attachment | 01-base-schema | 14 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_batch | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_caseactinst | 01-base-schema | 16 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_caseinst | 01-base-schema | 12 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_comment | 01-base-schema | 12 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_dec_in | 01-base-schema | 14 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_dec_out | 01-base-schema | 17 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_decinst | 01-base-schema | 21 | 1 | 13 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_detail | 01-base-schema | 28 | 1 | 14 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_ext_task_log | 01-base-schema | 20 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_identitylink | 01-base-schema | 13 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_incident | 01-base-schema | 21 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_job_log | 01-base-schema | 24 | 1 | 9 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_op_log | 01-base-schema | 27 | 1 | 9 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_procinst | 01-base-schema | 19 | 1 | 9 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_taskinst | 01-base-schema | 26 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_hi_varinst | 01-base-schema | 24 | 1 | 11 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_bytearray | 01-base-schema | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_group | 01-base-schema | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_info | 01-base-schema | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_membership | 01-base-schema | 2 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_priv | 01-base-schema | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_priv_mapping | 01-base-schema | 4 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_property | 01-base-schema | 3 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_tenant | 01-base-schema | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_tenant_member | 01-base-schema | 4 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_token | 01-base-schema | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_id_user | 01-base-schema | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_procdef_info | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_re_camformdef | 01-base-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_re_case_def | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_re_decision_def | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_re_decision_req_def | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_re_deployment | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_re_model | 01-base-schema | 13 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_re_procdef | 01-base-schema | 15 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_authorization | 01-base-schema | 10 | 1 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_batch | 01-base-schema | 16 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_case_execution | 01-base-schema | 13 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_case_sentry_part | 01-base-schema | 13 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_deadletter_job | 01-base-schema | 15 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_event_subscr | 01-base-schema | 10 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_execution | 01-base-schema | 20 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_ext_task | 01-base-schema | 18 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_filter | 01-base-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_history_job | 01-base-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_identitylink | 01-base-schema | 8 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_incident | 01-base-schema | 16 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_job | 01-base-schema | 27 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_jobdef | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_meter_log | 01-base-schema | 6 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_suspended_job | 01-base-schema | 16 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_task | 01-base-schema | 22 | 1 | 10 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_task_meter_log | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_timer_job | 01-base-schema | 18 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| act_ru_variable | 01-base-schema | 20 | 1 | 10 | 0 | 0 | 0 | 0 | 0 | 0 |
| au_param_info | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| au_patch_update | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bi_remain_term | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bi_remain_term_detail | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bill_acc_keep_amt | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bill_consumer_record | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bip_corp_mapping | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bip_wage_dtl | 01-base-schema | 22 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_acc_attribute | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_acc_bal | 01-base-schema | 22 | 1 | 9 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_acc_bal_auto | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_acc_dtl | 01-base-schema | 82 | 1 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_acc_dtl_check | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_acc_dtl_claim | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_acc_dtl_import_config | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_acc_dtl_import_configdtl | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_acc_gather_policy | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_branch | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_config | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_config_bill | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_ebill | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_execution_strategy | 01-base-schema | 17 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_month_statement_dtl | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_month_statement_pdf | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_receipt_dtl | 01-base-schema | 39 | 1 | 5 | 0 | 1 | 0 | 0 | 0 | 0 |
| bis_bank_receipt_dtl_dispose | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_receipt_dtl_his | 01-base-schema | 27 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_receipt_exc | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bank_receipt_pdf | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bif_init | 01-base-schema | 14 | 0 | 0 | 0 | 0 | 0 | 56 | 0 | 2 |
| bis_bif_monitor | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_bif_rule | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| bis_check_dtl | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_check_exc | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_check_reason | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_corp_pool_bal | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_corp_pool_config | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_corp_pool_config_rule | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_corp_pool_overdraft | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_data_cert | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_exc | 01-base-schema | 76 | 1 | 8 | 1 | 1 | 0 | 0 | 0 | 0 |
| bis_exc_mid_acc | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_exc_proc | 01-base-schema | 52 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_exc_send | 01-base-schema | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_fail_reason | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_finance_exc | 01-base-schema | 39 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_foreign_exc | 01-base-schema | 144 | 1 | 1 | 67 | 32 | 0 | 0 | 0 | 0 |
| bis_foreign_exc_send | 01-base-schema | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_gather_cmd | 01-base-schema | 48 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_gather_cmd_send | 01-base-schema | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_gather_policy | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_gather_policy_account | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_gather_policy_agreement | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_pay_policy | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_pay_policy_bustype | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_pay_policy_payee | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_pay_policy_payeracc | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_pay_policy_sort | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_policy_filter | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_query_cmd | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_rec_msg | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_receipt_dtl | 01-base-schema | 5 | 0 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_serial | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_server_param | 01-base-schema | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bis_virtual_acc_exc | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_CONFIG_TYPE_DATA | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_FILE_CONFIG | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_PACKAGE_INFO | 09-bank-schema-data | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_PARAM_CONFIG | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_C_BCODE | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_CCODE | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_CONFIG_TYPE | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_ROUTE_POLICY | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_ROUTE_RULES | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_RULE_CONFIG | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_SUPPORT | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_DICT_TYPE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_RULE_FIELD_TYPE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_SCHEDULED_TASK_BASE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_TXN_CONFIG_TYPE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_TXN_SCHEDULED_TASK | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_TXN_SUPPORTED_INFO | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_CONFIG_TYPE_DATA | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_DICT_TYPE_CONFIG | 09-bank-schema-data | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_DICT_TYPE_DATA | 09-bank-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_FILE_CONFIG | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_PARAM_CONFIG | 09-bank-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_C_BCODE | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_CCODE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_CONFIG_TYPE | 09-bank-schema-data | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_ROUTE_POLICY | 09-bank-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_ROUTE_RULES | 09-bank-schema-data | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_RULE_CONFIG | 09-bank-schema-data | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_SUPPORT | 09-bank-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_VERSION | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_bankinfo_register | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_bankinfo_register_bill | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_bankinft_trade | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_bankinft_trade_bill | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_batch_payment | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_check_trade | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_client_consumer | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_client_consumer_base | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_client_consumer_base_bill | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_client_consumer_bill | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_dubious_trade | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_excute_bank | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_excute_bank_bill | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_param | 01-base-schema | 12 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_param_bill | 01-base-schema | 12 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_queue_baseinfo | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_queue_baseinfo_bill | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_BANK_APP_MANAGER | 09-bank-schema-data | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_BANK_APP_MONITOR | 09-bank-schema-data | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_CONFIG_MANAGER | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_CONFIG_TYPE_DATA | 09-bank-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_FILE_CONFIG | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_OPERATION | 09-bank-schema-data | 18 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_OPERATION_LOG | 09-bank-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_PARAM_CONFIG | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_SCHEDULED_TASK | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_CONFIG_TYPE | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_ROUTE_POLICY | 09-bank-schema-data | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_ROUTE_RULES | 09-bank-schema-data | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_RULE_CONFIG | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_SCHEDULED_TASK | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_SUPPORT | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_VERSION_MANAGER | 09-bank-schema-data | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_trade_baseinfo | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_trade_baseinfo_bill | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_trans_cust_login | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bp_trans_cust_login_bill | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bpm_approval_node | 01-base-schema | 15 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bpm_approval_node_his | 01-base-schema | 17 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bpm_delegation | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bpm_delegation_category | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bpm_model_config | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bpm_option | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| br_relation_grade_index | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| branch_table | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_acc_nature | 01-base-schema | 10 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_account_fc_subject_day | 01-base-schema | 2 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_account_fc_subject_init | 01-base-schema | 2 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_account_fc_subject_sort | 01-base-schema | 2 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_app_manager | 02-ng-schema-data | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_assign_pay_way | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_assign_pay_way_user | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_acc | 01-base-schema | 90 | 1 | 4 | 2 | 2 | 0 | 0 | 0 | 0 |
| bt_bank_acc_app | 01-base-schema | 36 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_acc_app_cur | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_acc_bal | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_acc_cur | 01-base-schema | 8 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_acc_dtl | 01-base-schema | 29 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_acc_freeze | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_acc_plan_freeze | 01-base-schema | 21 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_acc_use | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_acc_virtual | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_bank_type | 01-base-schema | 22 | 1 | 2 | 6 | 6 | 0 | 2480 | 1 | 1 |
| bt_bank_type_custom | 06-business-schema | 9 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 1 |
| BT_BANK_TYPE_OTHER_CUSTOM | 06-business-schema | 0 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| bt_bankacc_message | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_ca_info | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_charge_back_type | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_currency | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_currency_init | 02-ng-schema-data | 4 | 1 | 1 | 0 | 0 | 0 | 173 | 0 | 0 |
| bt_currency_manager | 02-ng-schema-data | 0 | 1 | 1 | 0 | 0 | 0 | 25 | 0 | 0 |
| bt_currency_pair | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_inner_acc_app | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_input_bank_info | 01-base-schema | 14 | 1 | 3 | 1 | 0 | 0 | 1 | 0 | 0 |
| bt_input_bank_swift | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_input_bank_swift_manager | 02-ng-schema-data | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_interface_res | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_interface_res_bill | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_interface_sys | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_interface_sys_bill | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ge_bytearray | 02-ng-schema-data | 6 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ge_property | 02-ng-schema-data | 3 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| bt_m_act_hi_actinst | 02-ng-schema-data | 16 | 1 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_hi_attachment | 02-ng-schema-data | 11 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_hi_comment | 02-ng-schema-data | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_hi_detail | 02-ng-schema-data | 15 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_hi_identitylink | 02-ng-schema-data | 7 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_hi_procinst | 02-ng-schema-data | 15 | 1 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_hi_taskinst | 02-ng-schema-data | 22 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_hi_varinst | 02-ng-schema-data | 14 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_id_bytearray | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_id_group | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_id_info | 02-ng-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_id_membership | 02-ng-schema-data | 2 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_id_priv | 02-ng-schema-data | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_id_priv_mapping | 02-ng-schema-data | 4 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_id_property | 02-ng-schema-data | 3 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| bt_m_act_id_token | 02-ng-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_id_user | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_procdef_info | 02-ng-schema-data | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_re_deployment | 02-ng-schema-data | 7 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| bt_m_act_re_model | 02-ng-schema-data | 13 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_re_procdef | 02-ng-schema-data | 15 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_deadletter_job | 02-ng-schema-data | 15 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_event_subscr | 02-ng-schema-data | 11 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_execution | 02-ng-schema-data | 31 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_history_job | 02-ng-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_identitylink | 02-ng-schema-data | 8 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_job | 02-ng-schema-data | 18 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_suspended_job | 02-ng-schema-data | 16 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_task | 02-ng-schema-data | 23 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_timer_job | 02-ng-schema-data | 18 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_act_ru_variable | 02-ng-schema-data | 12 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_bpm_approval_node | 02-ng-schema-data | 15 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_bpm_approval_node_his | 02-ng-schema-data | 17 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_bpm_delegation | 02-ng-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_bpm_delegation_category | 02-ng-schema-data | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_bpm_flow_category | 02-ng-schema-data | 15 | 1 | 0 | 0 | 0 | 0 | 2 | 2 | 0 |
| bt_m_bpm_model_config | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_bpm_option | 02-ng-schema-data | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_m_rate_type | 02-ng-schema-data | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_message_repeat | 01-base-schema | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_net_chalk | 01-base-schema | 39 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_net_ukey | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_net_ukey_bankacc | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_pay_type | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 1 |
| bt_purpose | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_purpose_bank_type | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_purpose_corp | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_purpose_country | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_purpose_cur | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_rate_type | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_rate_type_manager | 02-ng-schema-data | 0 | 1 | 1 | 0 | 0 | 0 | 2 | 0 | 0 |
| bt_summary | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_swift_info | 01-base-schema | 15 | 1 | 4 | 3 | 3 | 0 | 0 | 0 | 0 |
| bt_swift_info_manager | 02-ng-schema-data | 15 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_CASE | 06-business-schema | 30 | 1 | 3 | 0 | 0 | 0 | 0 | 2 | 0 |
| BT_TR_LIMIT_CHECK_LOG | 06-business-schema | 13 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_COND | 06-business-schema | 13 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_HIT_DETAIL | 06-business-schema | 21 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_RULE | 06-business-schema | 20 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_RULE_AUDIT | 06-business-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_SCOPE | 06-business-schema | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_TXN | 06-business-schema | 28 | 1 | 4 | 0 | 0 | 0 | 0 | 1 | 0 |
| BT_TR_LIMIT_TXN_BUCKET | 06-business-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_USAGE | 06-business-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_warn_condition | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_warn_data | 01-base-schema | 34 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_warn_data_dtl | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_warn_exception_data | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bt_warn_strategy | 01-base-schema | 25 | 1 | 1 | 1 | 1 | 0 | 0 | 0 | 0 |
| bt_warning_log | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_log | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_payee_cheque | 01-base-schema | 29 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_print_format | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_print_format_dtl | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_print_match_rule | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_print_vou_opinfo | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_storage_box | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_vou_employ_info | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_vou_info_dtl | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_vou_input | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_voucher_type | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_voutype_data_field | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| bv_voutype_scene | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cas_user | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cgs_bill | 01-base-schema | 53 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| charge_type_bank_type_rel | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| charge_type_charge_rule_rel | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_bank_finance | 01-base-schema | 46 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_bank_finance_detail | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_bank_finance_history | 01-base-schema | 52 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_inner_finance | 01-base-schema | 47 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_message | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_product_netval | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_product_redeem | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_product_redeem_detail | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_product_renewal | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_product_req | 01-base-schema | 55 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_product_req_history | 01-base-schema | 69 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| cho_product_type | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cmb_bail_account_info | 01-base-schema | 16 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| cmb_bnkbilneg | 01-base-schema | 7 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| cmb_crlcon | 01-base-schema | 21 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| cmb_crlinfo | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| cmbc_bail_account_info | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| cme_guarantee_plan | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_accrual_type | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_extend | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_interest | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_issue_info | 01-base-schema | 64 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_pay | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_register_info | 01-base-schema | 31 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_repay_plan | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_repay_plan_dtl | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_swap | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_transfer | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_bond_type | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_contract | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_equity_info | 01-base-schema | 22 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_equity_info_detail | 01-base-schema | 16 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_equity_pay_plan | 01-base-schema | 12 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_equity_pay_plan_detail | 01-base-schema | 17 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_equity_pay_the | 01-base-schema | 31 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_equity_repo | 01-base-schema | 26 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_equity_repo_plan | 01-base-schema | 13 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_equity_repo_plan_detail | 01-base-schema | 16 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_equity_types | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_financing_lease_info | 01-base-schema | 65 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_floating_mode | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_in_history | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_inl_abolish | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_inl_adjustrate | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_inl_deferred | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_inl_info | 01-base-schema | 59 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_inl_repay | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_inl_repayplan_change | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_inl_send | 01-base-schema | 47 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_interest_way | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_leas_interest | 01-base-schema | 34 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_lease_capital_plan | 01-base-schema | 20 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_lease_capital_plandel | 01-base-schema | 19 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_lease_continue | 01-base-schema | 11 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_lease_extended | 01-base-schema | 17 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_lease_pay | 01-base-schema | 23 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_lease_refund | 01-base-schema | 11 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_lease_repo | 01-base-schema | 12 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_lease_sub_mater | 01-base-schema | 23 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_lease_update | 01-base-schema | 66 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_loan_deferred | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_loan_info | 01-base-schema | 66 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_loan_repay | 01-base-schema | 32 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_loan_send | 01-base-schema | 53 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_loan_type | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_loan_type_manager | 02-ng-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 5 | 0 | 0 |
| cms_precative_loan_deferred | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_precative_loan_info | 01-base-schema | 54 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_precative_loan_repay | 01-base-schema | 29 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_precative_loan_send | 01-base-schema | 46 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_process_msg | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_history | 01-base-schema | 24 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_interest | 01-base-schema | 34 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_interest_temp | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_loan_abolish | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_loan_deferred | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_loan_info | 01-base-schema | 112 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_loan_rate | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_loan_repay | 01-base-schema | 48 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_loan_send | 01-base-schema | 65 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_syndicated | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_syndicated_send | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_provide_syndicateddtl | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_repay_type | 01-base-schema | 32 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_repayment_schedule | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_repayment_scheduledetailed | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_schedule_dtl_temp | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_asset_type | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_info | 01-base-schema | 25 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_issue | 01-base-schema | 65 | 0 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_level | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_level_info | 01-base-schema | 12 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_plan | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_plan_detail | 01-base-schema | 17 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_repay_sure | 01-base-schema | 24 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_to_account | 01-base-schema | 16 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_security_type | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cms_tenancy_items_types | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| computational_model | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| con_bus_hook | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| con_info | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| con_info_busdtl | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| con_info_record | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| con_log | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| con_pd_map | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| con_pd_mapdtl | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| con_type | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| con_type_field | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| con_warn_rules | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| contact_address_book | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| cr_data_source | 01-base-schema | 5 | 1 | 0 | 0 | 0 | 0 | 9 | 0 | 0 |
| cr_qua_method_for_scoring_ind | 01-base-schema | 18 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| cr_scoring_indicator | 01-base-schema | 20 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| cr_scoring_org_contrast | 01-base-schema | 18 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| cr_task_corp_info | 01-base-schema | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| cr_task_info | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| cr_task_org_info | 01-base-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| crt_scoring_corp_contrast | 01-base-schema | 20 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| data_source_category | 01-base-schema | 6 | 1 | 0 | 0 | 0 | 0 | 3 | 0 | 0 |
| df_business_type | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| df_cost_type | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| df_direct_finance_info | 01-base-schema | 45 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| df_direct_finance_repay | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| df_direct_finance_repay_tmp | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| df_intermediate_cost | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| df_third_party_agency | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| distributed_lock | 01-base-schema | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| doc_invoice | 01-base-schema | 15 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| doc_manage | 01-base-schema | 43 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| doc_overdue_notice | 01-base-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| doc_wirte_off_relation | 01-base-schema | 23 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| dsl_inteface_flag | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| dsl_interface_log | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_accept_bill | 01-base-schema | 44 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_accrual_bill | 01-base-schema | 43 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_bill_message | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_bill_print | 01-base-schema | 11 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_compare_bill | 01-base-schema | 33 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_consign_bill | 01-base-schema | 38 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_draft_bill_temp | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_draft_organ | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_exchange_bill | 01-base-schema | 45 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_loan_bill | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_notice_bill | 01-base-schema | 41 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_poundage_bill | 01-base-schema | 41 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_precative_loan_bill | 01-base-schema | 39 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_receipt_type | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| e_trans_bill | 01-base-schema | 39 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| e_visa_type | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_basic_data | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_data_type | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_datatype_field | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_dynamic_data | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_dynamic_data_type | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_his_dtl_comfield | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_his_dtl_template | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_inter_config | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_inter_config_fill | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_inter_config_set | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_payinfo | 01-base-schema | 45 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_template | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_template_datadefine | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| erp_template_dtl | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_template_dtl_store | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_template_store | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_template_type | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_template_type_dtl | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_type | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_variable | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| erp_wage_detail | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| evaluated_data_for_report | 01-base-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| evaluation_batch | 01-base-schema | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| evaluation_log | 01-base-schema | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| evaluation_report | 01-base-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget | 01-base-schema | 19 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_day | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_detail | 01-base-schema | 27 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_month | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_period | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_process | 01-base-schema | 34 | 1 | 4 | 0 | 1 | 0 | 0 | 0 | 0 |
| fbs_budget_quarter | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_resolve_month | 01-base-schema | 30 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_resolve_year | 01-base-schema | 48 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_superadd | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_superadd_detail | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_week | 01-base-schema | 12 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_budget_year | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_collect_corp | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_corp_budget | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_corp_item | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_excrate_adjust | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_item | 01-base-schema | 28 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_message | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_process_rule | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fbs_salesreturn_analyse | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_acc | 01-base-schema | 64 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_acc_balance | 01-base-schema | 26 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_acc_history | 01-base-schema | 65 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_acc_log | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_acc_month_balance | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_acc_writeoff | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook | 01-base-schema | 33 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_blned | 01-base-schema | 33 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_blned_dtl | 01-base-schema | 27 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_blned_ext | 01-base-schema | 35 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_dtl | 01-base-schema | 27 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_dtl_his | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_ext | 01-base-schema | 35 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_ext_his | 01-base-schema | 35 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_his | 01-base-schema | 34 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_import | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accbook_msg | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_accu_balance | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_agreed_deposit | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_agreed_detail | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_auto_check_config | 01-base-schema | 15 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_auto_check_corp | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_auto_check_result | 01-base-schema | 17 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_balance_sheet | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_acc_dtl | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_acc_group | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_acc_group_area | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_balance_adjust | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_balance_adjust_detail | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_bill | 01-base-schema | 33 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_bill_proc | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_bill_temp | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_check_dtl | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bank_check_log | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bond | 01-base-schema | 29 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bond_add | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bond_apply | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bond_back | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bond_bal | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bond_log | 01-base-schema | 9 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bond_type | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bus_race | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_bus_race_pool | 01-base-schema | 12 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_business_rule_define | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_business_rule_define_pool | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_business_rule_init | 01-base-schema | 12 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| fc_business_rule_init_temp | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_carry | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_cash_flow | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_cash_item | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_charge_accrue | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_charge_rule | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_charge_type | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_charge_type_dtl | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_check_init | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_check_type_dtl | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_compare_acc | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_compare_acc_ext | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_compare_acc_proc | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_compare_acc_temp | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_compare_check_type | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_corp_compare_bill | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_corp_interest_dtl | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_corp_pool | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_day_close | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_day_start | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_excrate_adjust | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_excrate_adjust_manager | 02-ng-schema-data | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_freeze | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_income_statement | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_init_acc | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_instrate | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_instrate_adjust | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_interest_dtl | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_interest_dtl_rule | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_interest_dtl_temp | 01-base-schema | 37 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_interest_single | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_interest_single_nod_book | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_interest_type | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_less_pay_bill | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_loan_balance_dtl | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_loan_repay | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_manual_finacial_column | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| fc_manual_finacial_type | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| fc_month_ledger | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_nis_bus_type_rule | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_online_ledger | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_other_business_rule_define | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_out_acc_balance | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_out_accbook | 01-base-schema | 31 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_out_accbook_dtl | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_out_accbook_dtl_his | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_out_subject_day | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_pre_out_accbook | 01-base-schema | 31 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_predraw_dtl | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_predraw_dtl_temp | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_predraw_rush_dtl | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_receipt_type | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_report_format | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_set_month | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_subject | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_subject_day | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_subject_init | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_subject_pool | 01-base-schema | 20 | 1 | 1 | 0 | 2 | 0 | 0 | 0 | 0 |
| fc_subject_sort | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| fc_sys_bus_race | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| fc_sys_bus_race_pool | 01-base-schema | 5 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_tally_remind_dtl | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_union_accbook | 01-base-schema | 31 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_voucher_cash | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_voucher_entry_define | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_voucher_entry_define_pool | 01-base-schema | 16 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_voucher_entry_init | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| fc_voucher_entry_init_back | 01-base-schema | 16 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| fc_voucher_type | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_add_word | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_cash_flow | 01-base-schema | 40 | 1 | 2 | 6 | 0 | 0 | 0 | 0 | 0 |
| fex_cash_flow_cust_info | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_corp_bankacc | 01-base-schema | 15 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| fex_corp_bankacc_dtl | 01-base-schema | 11 | 1 | 1 | 4 | 1 | 0 | 0 | 0 | 0 |
| fex_corp_exposure | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_corp_exposure_template | 01-base-schema | 10 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| fex_corp_pool | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_corp_pool_bankacc | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_corp_pool_limit | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_corp_pool_tran | 01-base-schema | 11 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| fex_cotp | 01-base-schema | 25 | 1 | 1 | 19 | 4 | 0 | 0 | 0 | 0 |
| fex_cotp_bankacc | 01-base-schema | 23 | 1 | 1 | 3 | 2 | 0 | 0 | 0 | 0 |
| FEX_COTP_BANKACC_CUR | 06-business-schema | 4 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| fex_cotp_cust_info | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_cust_field_rule | 01-base-schema | 18 | 0 | 1 | 6 | 0 | 0 | 1 | 1 | 0 |
| FEX_CUST_FIELD_RULE_TENANT | 06-business-schema | 21 | 1 | 1 | 0 | 2 | 0 | 0 | 0 | 0 |
| FEX_CUST_INFO_TENANT | 06-business-schema | 5 | 1 | 1 | 1 | 1 | 0 | 0 | 0 | 0 |
| fex_exposure | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_exposure_detail | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_exposure_item | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_exposure_main | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_exposure_template | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_exposure_template_item | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_foreign_req | 01-base-schema | 65 | 1 | 1 | 8 | 0 | 0 | 0 | 0 | 0 |
| fex_linkman_info | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_message | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_payer_info | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_product | 01-base-schema | 35 | 1 | 1 | 3 | 2 | 0 | 0 | 1 | 0 |
| FEX_PROFIT_LOSS_MANAGE | 06-business-schema | 33 | 0 | 0 | 14 | 5 | 0 | 0 | 0 | 0 |
| FEX_PROFIT_LOSS_MANAGE_DTL | 06-business-schema | 17 | 0 | 0 | 4 | 1 | 0 | 0 | 0 | 0 |
| fex_rate_exc | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_rate_info | 01-base-schema | 15 | 1 | 1 | 9 | 6 | 0 | 0 | 0 | 0 |
| fex_rate_type | 01-base-schema | 12 | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| fex_settle_fill_rule | 01-base-schema | 17 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| fex_settle_fill_rule_add | 01-base-schema | 5 | 1 | 1 | 0 | 2 | 0 | 0 | 0 | 0 |
| fex_tran_app | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_tran_app_dtl | 01-base-schema | 83 | 1 | 1 | 63 | 10 | 0 | 0 | 0 | 0 |
| fex_tran_app_dtl_tmp | 01-base-schema | 82 | 1 | 1 | 62 | 10 | 0 | 0 | 0 | 0 |
| fex_tran_config_operation_log | 06-business-schema | 10 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| fex_tran_cust_info | 01-base-schema | 5 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| fex_tran_deal | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_tran_group | 01-base-schema | 14 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| fex_tran_group_rule | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| fex_tran_group_rule_dtl | 01-base-schema | 9 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| fex_tran_info | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 1 |
| FEX_TRAN_LIFE_CYCLE | 06-business-schema | 56 | 0 | 0 | 3 | 4 | 0 | 0 | 0 | 0 |
| fex_tran_option_deal | 01-base-schema | 23 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| fex_tran_req | 01-base-schema | 144 | 1 | 1 | 93 | 18 | 0 | 0 | 0 | 0 |
| fex_tran_req_config | 06-business-schema | 0 | 1 | 0 | 1 | 0 | 0 | 4412 | 21 | 0 |
| fex_tran_req_config_version | 06-business-schema | 8 | 0 | 0 | 1 | 0 | 0 | 39 | 0 | 0 |
| FEX_TRAN_REQ_EXT | 06-business-schema | 20 | 0 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| fex_tran_req_field | 06-business-schema | 19 | 0 | 0 | 1 | 1 | 0 | 138 | 55 | 1 |
| fex_tran_template_config | 06-business-schema | 18 | 0 | 0 | 1 | 0 | 0 | 33 | 14 | 1 |
| finacial_consumer_record | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| finacial_producer_record | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| global_table | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_conrrant_change_track | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_conrrant_occupy | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_conrrant_occupy_detail | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_contract_info | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_contract_occup | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_contract_pledge | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_contract_way | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_exguarantee_amount | 01-base-schema | 11 | 0 | 1 | 0 | 3 | 0 | 0 | 0 | 0 |
| gua_exguarantee_corp | 01-base-schema | 8 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_guarantee_change_info | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_guarantee_info | 01-base-schema | 44 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_log | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_main_type | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_main_type_way | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_perform_reg | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_pledge_info | 01-base-schema | 47 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_pledge_info_his | 01-base-schema | 44 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_pledge_occup | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_pledge_type | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| gua_way | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| indicator_category_fix_report | 01-base-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| indicator_category_solidifica | 01-base-schema | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| institutional_raw_data | 01-base-schema | 27 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| lb_billhead | 01-base-schema | 47 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_bill_arrival | 01-base-schema | 40 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_deposit | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_deposit_delay | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_deposit_repay | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_enrol | 01-base-schema | 87 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_incoming | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_message | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_mortgage | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_payment | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lc_payment_cost | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| lock_table | 01-base-schema | 10 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| model_batch | 01-base-schema | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| model_formula | 01-base-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| model_formula_fix_report | 01-base-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| model_solidification_report | 01-base-schema | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_allocation_req | 01-base-schema | 39 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_bill | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_bill_bank_serial_no | 01-base-schema | 6 | 0 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_bill_bank_serial_no_refund | 01-base-schema | 7 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_billhead | 01-base-schema | 83 | 1 | 11 | 9 | 0 | 0 | 0 | 0 | 0 |
| nis_billhead_pool | 01-base-schema | 67 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_binding_bankacc | 01-base-schema | 3 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_bus_type | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_bus_type_rule | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_business_type | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| nis_capital_record | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_corp_bus | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_corp_cashier | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_corp_cashier_user | 01-base-schema | 7 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_demand | 01-base-schema | 38 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_expense_detail | 01-base-schema | 38 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_flow_rule | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_flow_rule_purpose | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_message | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_operation_type | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| nis_pay_scheduling_elements | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_personnel_detail | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_project | 01-base-schema | 14 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_receivable | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_related_party | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_scf_info | 06-business-schema | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_share_bus_type | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_wage | 01-base-schema | 23 | 1 | 2 | 1 | 0 | 0 | 0 | 0 | 0 |
| nis_wage_detail | 01-base-schema | 29 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_wage_detail_back | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_wage_float_ratio_config | 01-base-schema | 9 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_wage_release_config | 01-base-schema | 22 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| oauth_access_token | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| oauth_approvals | 01-base-schema | 7 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| oauth_client_details | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| oauth_client_token | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| oauth_code | 01-base-schema | 3 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| oauth_refresh_token | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_board | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_board_param | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_board_role_permission | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_board_user_permission | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_category | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_dataset | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_datasource | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_role_permission | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_user_permission | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| olap_widget | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ownership_structure_data | 01-base-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| payment_consumer_record | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| payment_producer_record | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| pk_fex_settle_fill_rule_add | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| q_m_scoring_i_fix_report | 01-base-schema | 21 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_blob_triggers | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_calendars | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_cron_triggers | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_fired_triggers | 01-base-schema | 14 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_job_details | 01-base-schema | 11 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_locks | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_paused_trigger_grps | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_scheduler_state | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_simple_triggers | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_simprop_triggers | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| qrtz_triggers | 01-base-schema | 17 | 1 | 14 | 0 | 0 | 0 | 0 | 0 | 0 |
| quantitative_method_for_sis | 01-base-schema | 19 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_audit_info | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_audit_info_his | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_audit_inter | 01-base-schema | 11 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_audit_strategy | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_audit_task | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_back_pay | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_bank_card_binding | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_import_conf | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_import_conf_dtl | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_log | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_other_card_binding | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_pos_binding | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_pos_borrow | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_received_dtl | 01-base-schema | 29 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_received_his_dtl | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_received_su_dtl | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_revenue_dtl | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_revenue_his_dtl | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_revenue_su_dtl | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_shop | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ras_shop_grant | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bk_stat_report_history | 01-base-schema | 21 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration | 01-base-schema | 48 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_add | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_add_detail | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_adjust | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_adjust_detail | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_delay | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_detail | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_dis | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_dis_detail | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_relief | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_bkration_relief_detail | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_business_race | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_credit_cooperation_bank | 01-base-schema | 9 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_credit_type | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_item_custom | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_item_type | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_process_log | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_ration_detail_dishis | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_ration_log | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_sys_item | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| rat_uration | 01-base-schema | 39 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_uration_apply | 01-base-schema | 46 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_uration_apply_dtl | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| rat_yearfin_plan_register | 01-base-schema | 14 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| raw_data | 01-base-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_ent_bank_open | 06-business-schema | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_funder_info | 06-business-schema | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCF_INSTRUCTION | 06-business-schema | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_invoice_info | 06-business-schema | 21 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_loan_info | 06-business-schema | 42 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_payable_voucher | 06-business-schema | 43 | 1 | 0 | 10 | 0 | 0 | 0 | 0 | 0 |
| scf_receivable_payment_info | 06-business-schema | 30 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_receivable_transfer_info | 06-business-schema | 28 | 1 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| scf_receivable_voucher_info | 06-business-schema | 43 | 1 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| scf_voucher_custom_field | 06-business-schema | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_voucher_redemption | 06-business-schema | 21 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_grade | 01-base-schema | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_grade_fix_report | 01-base-schema | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_grade_le_fix_report | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_grade_level | 01-base-schema | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_grade_level_solid | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_grade_solid | 01-base-schema | 15 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_indicator_fix_report | 01-base-schema | 22 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_indicator_solidificati | 01-base-schema | 22 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_indicator_value | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scoring_metric_val_fix_report | 01-base-schema | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_account_grant | 01-base-schema | 9 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_account_grant_his | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_account_mapping | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_api_server_config | 01-base-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_article | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_article_manager | 02-ng-schema-data | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_article_role | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_article_role_manager | 02-ng-schema-data | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_article_user | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_article_user_his | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_article_user_his_manager | 02-ng-schema-data | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_article_user_manager | 02-ng-schema-data | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_attachment | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_attachment_manager | 02-ng-schema-data | 0 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| sys_bank_region | 01-base-schema | 4 | 1 | 3 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_black_white_list | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_ca_info | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_calendar | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_calendar_manager | 02-ng-schema-data | 0 | 1 | 0 | 0 | 0 | 0 | 351 | 0 | 0 |
| sys_cell_manage | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_config | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_config_back | 01-base-schema | 6 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_config_log | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_config_log_manager | 02-ng-schema-data | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_config_manager | 02-ng-schema-data | 1 | 1 | 2 | 0 | 0 | 0 | 436 | 0 | 0 |
| sys_corp | 01-base-schema | 41 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_corp_cm_map | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_corp_cm_map_manager | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_corp_manage_manager | 02-ng-schema-data | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_corp_manager | 02-ng-schema-data | 39 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_corp_to_login_name | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_corp_user_resource | 01-base-schema | 7 | 0 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_corp_user_resource_manager | 02-ng-schema-data | 6 | 0 | 7 | 0 | 0 | 0 | 141 | 0 | 0 |
| sys_country | 02-ng-schema-data | 13 | 1 | 1 | 0 | 0 | 0 | 252 | 0 | 0 |
| sys_country_cust | 01-base-schema | 15 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_country_cust_manager | 02-ng-schema-data | 15 | 1 | 1 | 0 | 0 | 0 | 5 | 0 | 0 |
| sys_dict | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 264 | 18 | 6 |
| sys_dict_group | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 49 | 0 | 0 |
| sys_dual_manager | 02-ng-schema-data | 7 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_equity_maintenance | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_error_log_manager | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_external_corp | 01-base-schema | 44 | 1 | 3 | 6 | 6 | 0 | 0 | 0 | 0 |
| sys_external_corp_audit_log | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_external_corp_bankacc | 01-base-schema | 22 | 1 | 3 | 12 | 12 | 0 | 0 | 0 | 0 |
| sys_external_corp_quick_pay | 06-business-schema | 14 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| sys_externalcorp_corp | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_home | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_home_category | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_home_manager | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_home_widget | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_home_widget_manager | 02-ng-schema-data | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_internation | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_internation_base | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 14 | 23 | 0 |
| sys_log | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_log_manager | 02-ng-schema-data | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_menu | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 6 | 1 |
| sys_menu_collect | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_menu_collect_manager | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_menu_manager | 02-ng-schema-data | 18 | 1 | 0 | 0 | 0 | 0 | 15 | 11 | 2 |
| sys_module | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_module_manager | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_module_menu | 01-base-schema | 3 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_module_menu_manager | 02-ng-schema-data | 2 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_multi_lang | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 1356 | 48 | 1 |
| sys_notification | 01-base-schema | 11 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_category | 01-base-schema | 17 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_category_m | 02-ng-schema-data | 15 | 1 | 1 | 0 | 0 | 0 | 2 | 0 | 0 |
| sys_notification_ext | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_historic | 01-base-schema | 13 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_historic_m | 02-ng-schema-data | 12 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_manager | 02-ng-schema-data | 10 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_message | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_message_m | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_role | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_role_manager | 02-ng-schema-data | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_user | 01-base-schema | 4 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_notification_user_manager | 02-ng-schema-data | 3 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_oauth_access_token | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_oauth_access_token_manager | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_oauth_client_details | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_oauth_client_details_m | 02-ng-schema-data | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_oauth_code | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_oauth_code_manager | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_operation_area | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_organtype | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_organtype_corp | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_organtype_corp_manager | 02-ng-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_organtype_corp_release | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_organtype_corp_release_m | 02-ng-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_organtype_manager | 02-ng-schema-data | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_organtype_module | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_organtype_module_manager | 02-ng-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_organtype_release | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_organtype_release_manager | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_permission | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_permission_manager | 02-ng-schema-data | 3 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_province | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_region | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 705 | 0 | 0 |
| sys_resources | 01-base-schema | 12 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 1 |
| sys_resources_manager | 02-ng-schema-data | 10 | 1 | 2 | 0 | 0 | 0 | 10 | 0 | 0 |
| sys_role | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_role_manager | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_role_menu | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_role_menu_manager | 02-ng-schema-data | 3 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_service_production | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_sign_record_manager | 02-ng-schema-data | 17 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_sub_admin_corp_manager | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_supplier_type | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_table_record | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_TABLE_RECORD_MANAGER | 06-business-schema | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_task | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_task_audit | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_tenant | 01-base-schema | 17 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_tenant_mac | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_tenant_manager | 02-ng-schema-data | 39 | 1 | 1 | 3 | 0 | 0 | 0 | 0 | 0 |
| sys_tenant_record_manager | 02-ng-schema-data | 37 | 1 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| sys_tenant_user_record_manager | 02-ng-schema-data | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_time_zone | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_unit_store_maintenance | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user | 01-base-schema | 25 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_config | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_config_manager | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_corp_role | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_face_detection | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_face_detection_m | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_hi_pwd | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_hi_pwd_manager | 02-ng-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_login_log | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_login_log_manager | 02-ng-schema-data | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_manager | 02-ng-schema-data | 22 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| sys_user_message | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_message_manager | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_permission_log_m | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_role | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| sys_user_role_manager | 02-ng-schema-data | 3 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| tb_base_rate | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tb_credit_period | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tb_generator | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| tb_loan_rate_maintence | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tcc_fence_log | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| tenant_bis_bif_init | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| tenant_p_bi_remain_term_detail | 02-ng-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| tenant_p_fex_product | 06-business-schema | 37 | 1 | 0 | 0 | 0 | 0 | 2 | 1 | 0 |
| tenant_p_notification_category | 02-ng-schema-data | 17 | 0 | 0 | 0 | 0 | 0 | 1 | 1 | 1 |
| tenant_pre_acc_attribute | 02-ng-schema-data | 15 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| tenant_pre_acc_nature | 02-ng-schema-data | 11 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| tenant_pre_act_extend_category | 02-ng-schema-data | 21 | 0 | 0 | 0 | 0 | 0 | 14 | 3 | 2 |
| tenant_pre_bi_remain_term | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| tenant_pre_bpm_flow_category | 02-ng-schema-data | 15 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| tenant_pre_cms_loan_type | 02-ng-schema-data | 15 | 0 | 0 | 0 | 0 | 0 | 5 | 1 | 0 |
| tenant_pre_config | 02-ng-schema-data | 5 | 0 | 0 | 0 | 0 | 0 | 4 | 0 | 0 |
| tenant_pre_fbs_item | 02-ng-schema-data | 28 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| tenant_pre_fc_subject | 02-ng-schema-data | 25 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| tenant_pre_fc_voucher_type | 02-ng-schema-data | 20 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 1 |
| tenant_pre_menu | 02-ng-schema-data | 20 | 0 | 0 | 0 | 0 | 0 | 44 | 603 | 8 |
| tenant_pre_menu_1 | 02-ng-schema-data | 20 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tenant_pre_resources | 02-ng-schema-data | 13 | 0 | 0 | 0 | 0 | 0 | 61 | 0 | 60 |
| tenant_pre_resources_1 | 02-ng-schema-data | 13 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TENANT_PRE_TABLE_RECORD | 06-business-schema | 0 | 1 | 1 | 0 | 0 | 0 | 2 | 0 | 0 |
| tenant_pre_tb_credit_period | 02-ng-schema-data | 13 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| TP_ACCOUNT_TYPE | 06-business-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_alipay_bal | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_alipay_dtl | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_alipay_dtl_his | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_BAIL_BILL_JOB | 06-business-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_bill_job | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_bill_view | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| tp_bill_view_dtl | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| tp_business | 01-base-schema | 15 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_business_corp | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_business_text | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_DEWU_DTL | 06-business-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_DOUYIN_DTL | 06-business-schema | 50 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_DOUYIN_QUERY_TRACKER | 06-business-schema | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_KUAISHOU_BAL | 06-business-schema | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_KUAISHOU_FUND_DTL | 06-business-schema | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_log | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_merchant | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_merchant_auth | 06-business-schema | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_merchant_var | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_OFFLINE_BAL | 06-business-schema | 19 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_OFFLINE_BOND_BAL | 06-business-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_OFFLINE_BOND_FLOW_DTL | 06-business-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_OFFLINE_FLOW_DTL | 06-business-schema | 19 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_payment | 01-base-schema | 34 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_PINDUODUO_DTL | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_platform_interface_info | 06-business-schema | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_PLATFORM_PARAM | 06-business-schema | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_quota | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_refund_order | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_shoptype | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_TMALL_BAIL_BILL | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_TMALL_BAL | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_TMALL_FUND_DTL | 06-business-schema | 18 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_weixinpay_bal | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_weixinpay_dtl | 01-base-schema | 31 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_WEIXINSTORE_BAL | 06-business-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_WEIXINSTORE_FUND_BILL | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_WEIXINSTORE_RELATED_INFO | 06-business-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_XIAOHONGSHU_DTL | 06-business-schema | 19 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_ylpay_dtl | 01-base-schema | 39 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| undo_log | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| user_mobile_chart_module | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| version_current | 01-base-schema | 6 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| visit_record_registration | 01-base-schema | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| workflow_bus_error_log | 01-base-schema | 10 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| xxl_job_group | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 2 | 0 | 0 |
| xxl_job_info | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 158 | 1 | 0 |
| xxl_job_lock | 01-base-schema | 2 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| xxl_job_log | 01-base-schema | 16 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| xxl_job_log_report | 01-base-schema | 7 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| xxl_job_logglue | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| xxl_job_login_log | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| xxl_job_operate_log | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| xxl_job_permission | 01-base-schema | 10 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| xxl_job_registry | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| xxl_job_user | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| year_bank_credit_plan | 01-base-schema | 17 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |

## Oracle 表清单

| 表名 | 结构文件 | 字段数 | 主键 | 索引 | 字段 ADD | 字段 MODIFY/CHANGE/RENAME | 字段 DROP | INSERT | UPDATE | DELETE |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| AB_ACCBOOK_INFO | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_ACCRECORDS | 01-base-schema | 97 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_ACCRECORDS_HIS | 01-base-schema | 73 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_ADD_CREDIT_INFO | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BACKBILLINFO | 01-base-schema | 64 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_ACC_BAL | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_ACC_DTL | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_ATTACHMENT | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_BANK_AUTO_DTL | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_CREDIT_INFO_MAINTAIN | 01-base-schema | 14 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_MIDDLE | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_PAY_MIDDLE | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_PLAN_AMT_MAINTAIN | 01-base-schema | 14 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_RECEIVEBIZ | 01-base-schema | 109 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_RULEBILLDTLRMK | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_RULEBILLDTLRMK_DTL | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_RULEBILLNO | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_SIGN_STRATEGY | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_SIGN_STRATEGY_DATA | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_SOURCE_DTL | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| AB_BILL_STATUS | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_TRANSFER | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BILL_TRANSFER_DTL | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_BRIEF_STATUS | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| AB_CLAIM_ACCOUNT | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_CLAIM_MIDDLE | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_CREDITINFO_RESPONSE | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_DISCOUNT | 01-base-schema | 30 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_DRAFT_EXC | 01-base-schema | 18 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_EMIT_BAIL_INTEREST_INFO | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_EMIT_BILL_TARGET_RATIO_INFO | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_EMITBILL_PAY | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_EXC | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_FIELD_ISMDF | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_INTELLECT_ELEMENT | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_INTELLECT_RULE | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_INTELLECT_RULE_DTL | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_INTELLECT_RULE_GROUP | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_INTELLECT_RULE_GROUP_DTL | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_LOG | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_BAIL | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_BAIL_BOOK | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_BAIL_DTL | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_BANK_EXC | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_BUSTYPE | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_CONTRACT | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_CORP | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_DTL | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_DTL_BOOK | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_FINAAPPLY_BOOK | 01-base-schema | 40 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_FINAAPPLY_DTL | 01-base-schema | 32 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_HOLD_BOOK | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_INTO_REPEAL | 01-base-schema | 54 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_LIMIT | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_LIMIT_ALLOT | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_LIMIT_ALLOTCHECK | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_LIMIT_APPLY | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_LIMIT_APPLYCHECK | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_LIMIT_DTL | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_PLEDGEE | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_QUERY_EXC | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOL_SIGN_INFO | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_POOLIF | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_SHORT_FUNDS_TIME_CONFIG | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_VOUCHERINFO | 01-base-schema | 75 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_VOUCHERINFO_HIS | 01-base-schema | 38 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_VOUCHERINFO_VICE | 01-base-schema | 84 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| AB_VOUCHERINFO_VICE_HIS | 01-base-schema | 58 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_EVT_LOG | 01-base-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_EXTEND_CATEGORY | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_EXTEND_DELEGATION | 01-base-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_EXTEND_DELEGATION_REL | 01-base-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_EXTEND_MODEL | 01-base-schema | 23 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_EXTEND_MSG_RECORD | 01-base-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_EXTEND_OPTION | 01-base-schema | 15 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_GE_BYTEARRAY | 01-base-schema | 11 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_GE_PROPERTY | 01-base-schema | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_GE_SCHEMA_LOG | 01-base-schema | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_ACTINST | 01-base-schema | 21 | 1 | 10 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_ATTACHMENT | 01-base-schema | 14 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_BATCH | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_CASEACTINST | 01-base-schema | 16 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_CASEINST | 01-base-schema | 12 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_COMMENT | 01-base-schema | 12 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_DEC_IN | 01-base-schema | 14 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_DEC_OUT | 01-base-schema | 17 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_DECINST | 01-base-schema | 21 | 1 | 13 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_DETAIL | 01-base-schema | 28 | 1 | 14 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_EXT_TASK_LOG | 01-base-schema | 20 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_IDENTITYLINK | 01-base-schema | 13 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_INCIDENT | 01-base-schema | 21 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_JOB_LOG | 01-base-schema | 24 | 1 | 9 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_OP_LOG | 01-base-schema | 27 | 1 | 9 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_PROCINST | 01-base-schema | 19 | 1 | 9 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_TASKINST | 01-base-schema | 26 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_HI_VARINST | 01-base-schema | 24 | 1 | 11 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_BYTEARRAY | 01-base-schema | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_GROUP | 01-base-schema | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_INFO | 01-base-schema | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_MEMBERSHIP | 01-base-schema | 2 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_PRIV | 01-base-schema | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_PRIV_MAPPING | 01-base-schema | 4 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_PROPERTY | 01-base-schema | 3 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_TENANT | 01-base-schema | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_TENANT_MEMBER | 01-base-schema | 4 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_TOKEN | 01-base-schema | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_ID_USER | 01-base-schema | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_PROCDEF_INFO | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RE_CAMFORMDEF | 01-base-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RE_CASE_DEF | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RE_DECISION_DEF | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RE_DECISION_REQ_DEF | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RE_DEPLOYMENT | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RE_MODEL | 01-base-schema | 13 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RE_PROCDEF | 01-base-schema | 15 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_AUTHORIZATION | 01-base-schema | 10 | 1 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_BATCH | 01-base-schema | 16 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_CASE_EXECUTION | 01-base-schema | 13 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_CASE_SENTRY_PART | 01-base-schema | 13 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_DEADLETTER_JOB | 01-base-schema | 15 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_EVENT_SUBSCR | 01-base-schema | 10 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_EXECUTION | 01-base-schema | 20 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_EXT_TASK | 01-base-schema | 18 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_FILTER | 01-base-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_HISTORY_JOB | 01-base-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_IDENTITYLINK | 01-base-schema | 8 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_INCIDENT | 01-base-schema | 16 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_JOB | 01-base-schema | 27 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_JOBDEF | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_METER_LOG | 01-base-schema | 6 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_SUSPENDED_JOB | 01-base-schema | 16 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_TASK | 01-base-schema | 22 | 1 | 10 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_TASK_METER_LOG | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_TIMER_JOB | 01-base-schema | 18 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| ACT_RU_VARIABLE | 01-base-schema | 20 | 1 | 10 | 0 | 0 | 0 | 0 | 0 | 0 |
| AU_PARAM_INFO | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| AU_PATCH_UPDATE | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BI_REMAIN_TERM | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BI_REMAIN_TERM_DETAIL | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BILL_ACC_KEEP_AMT | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BILL_CONSUMER_RECORD | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIP_CORP_MAPPING | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIP_WAGE_DTL | 01-base-schema | 22 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_ACC_ATTRIBUTE | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_ACC_BAL | 01-base-schema | 22 | 1 | 9 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_ACC_BAL_AUTO | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_ACC_DTL | 01-base-schema | 82 | 1 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_ACC_DTL_CHECK | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_ACC_DTL_CLAIM | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_ACC_DTL_IMPORT_CONFIG | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_ACC_DTL_IMPORT_CONFIGDTL | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_ACC_GATHER_POLICY | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_BRANCH | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_CONFIG | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_CONFIG_BILL | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_EBILL | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_EXECUTION_STRATEGY | 01-base-schema | 17 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_MONTH_STATEMENT_DTL | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_MONTH_STATEMENT_PDF | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_RECEIPT_DTL | 01-base-schema | 39 | 1 | 5 | 0 | 1 | 0 | 0 | 0 | 0 |
| BIS_BANK_RECEIPT_DTL_DISPOSE | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_RECEIPT_DTL_HIS | 01-base-schema | 27 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_RECEIPT_EXC | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BANK_RECEIPT_PDF | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BIF_INIT | 01-base-schema | 14 | 0 | 0 | 0 | 0 | 0 | 56 | 0 | 2 |
| BIS_BIF_MONITOR | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_BIF_RULE | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| BIS_CHECK_DTL | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_CHECK_EXC | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_CHECK_REASON | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_CORP_POOL_BAL | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_CORP_POOL_CONFIG | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_CORP_POOL_CONFIG_RULE | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_CORP_POOL_OVERDRAFT | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_DATA_CERT | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_EXC | 01-base-schema | 76 | 1 | 8 | 1 | 1 | 0 | 0 | 0 | 0 |
| BIS_EXC_MID_ACC | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_EXC_PROC | 01-base-schema | 52 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_EXC_SEND | 01-base-schema | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_FAIL_REASON | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_FINANCE_EXC | 01-base-schema | 39 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_FOREIGN_EXC | 01-base-schema | 144 | 1 | 1 | 66 | 26 | 0 | 0 | 0 | 0 |
| BIS_FOREIGN_EXC_SEND | 01-base-schema | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_GATHER_CMD | 01-base-schema | 48 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_GATHER_CMD_SEND | 01-base-schema | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_GATHER_POLICY | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_GATHER_POLICY_ACCOUNT | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_GATHER_POLICY_AGREEMENT | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_PAY_POLICY | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_PAY_POLICY_BUSTYPE | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_PAY_POLICY_PAYEE | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_PAY_POLICY_PAYERACC | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_PAY_POLICY_SORT | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_POLICY_FILTER | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_QUERY_CMD | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_REC_MSG | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_RECEIPT_DTL | 01-base-schema | 5 | 0 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_SERIAL | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_SERVER_PARAM | 01-base-schema | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BIS_VIRTUAL_ACC_EXC | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_CONFIG_TYPE_DATA | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_FILE_CONFIG | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_PACKAGE_INFO | 09-bank-schema-data | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_PARAM_CONFIG | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_C_BCODE | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_CCODE | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_CONFIG_TYPE | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_ROUTE_POLICY | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_ROUTE_RULES | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_RULE_CONFIG | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_APP_TXN_SUPPORT | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_DICT_TYPE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_RULE_FIELD_TYPE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_SCHEDULED_TASK_BASE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_TXN_CONFIG_TYPE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_TXN_SCHEDULED_TASK | 09-bank-schema-data | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_TXN_SUPPORTED_INFO | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_CONFIG_TYPE_DATA | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_DICT_TYPE_CONFIG | 09-bank-schema-data | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_DICT_TYPE_DATA | 09-bank-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_FILE_CONFIG | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_PARAM_CONFIG | 09-bank-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_C_BCODE | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_CCODE | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_CONFIG_TYPE | 09-bank-schema-data | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_ROUTE_POLICY | 09-bank-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_ROUTE_RULES | 09-bank-schema-data | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_RULE_CONFIG | 09-bank-schema-data | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_V_TXN_SUPPORT | 09-bank-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANK_VERSION | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANKINFO_REGISTER | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANKINFO_REGISTER_BILL | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANKINFT_TRADE | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BANKINFT_TRADE_BILL | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_BATCH_PAYMENT | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_CHECK_TRADE | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_CLIENT_CONSUMER | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_CLIENT_CONSUMER_BASE | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_CLIENT_CONSUMER_BASE_BILL | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_CLIENT_CONSUMER_BILL | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_DUBIOUS_TRADE | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_EXCUTE_BANK | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_EXCUTE_BANK_BILL | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_PARAM | 01-base-schema | 12 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_PARAM_BILL | 01-base-schema | 12 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_QUEUE_BASEINFO | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_QUEUE_BASEINFO_BILL | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_BANK_APP_MANAGER | 09-bank-schema-data | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_BANK_APP_MONITOR | 09-bank-schema-data | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_CONFIG_MANAGER | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_CONFIG_TYPE_DATA | 09-bank-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_FILE_CONFIG | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_OPERATION | 09-bank-schema-data | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_OPERATION_LOG | 09-bank-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_PARAM_CONFIG | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_SCHEDULED_TASK | 09-bank-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_CONFIG_TYPE | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_ROUTE_POLICY | 09-bank-schema-data | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_ROUTE_RULES | 09-bank-schema-data | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_RULE_CONFIG | 09-bank-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_SCHEDULED_TASK | 09-bank-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_TXN_SUPPORT | 09-bank-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TENANT_VERSION_MANAGER | 09-bank-schema-data | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TRADE_BASEINFO | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TRADE_BASEINFO_BILL | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TRANS_CUST_LOGIN | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BP_TRANS_CUST_LOGIN_BILL | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BPM_APPROVAL_NODE | 01-base-schema | 15 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BPM_APPROVAL_NODE_HIS | 01-base-schema | 17 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BPM_DELEGATION | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BPM_DELEGATION_CATEGORY | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BPM_MODEL_CONFIG | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BPM_OPTION | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BR_RELATION_GRADE_INDEX | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BRANCH_TABLE | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_ACC_NATURE | 01-base-schema | 10 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_ACCOUNT_FC_SUBJECT_DAY | 01-base-schema | 2 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_ACCOUNT_FC_SUBJECT_INIT | 01-base-schema | 2 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_ACCOUNT_FC_SUBJECT_SORT | 01-base-schema | 2 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_APP_MANAGER | 02-ng-schema-data | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_ASSIGN_PAY_WAY | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_ASSIGN_PAY_WAY_USER | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC | 01-base-schema | 90 | 1 | 4 | 5 | 3 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC_APP | 01-base-schema | 36 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC_APP_CUR | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC_BAL | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC_CUR | 01-base-schema | 8 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC_DTL | 01-base-schema | 29 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC_FREEZE | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC_PLAN_FREEZE | 01-base-schema | 21 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC_USE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_ACC_VIRTUAL | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_BANK_TYPE | 01-base-schema | 22 | 1 | 2 | 6 | 3 | 0 | 2480 | 1 | 1 |
| bt_bank_type_custom | 06-business-schema | 9 | 1 | 0 | 3 | 0 | 0 | 0 | 0 | 1 |
| BT_BANK_TYPE_OTHER_CUSTOM | (ALTER only) | 0 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| BT_BANKACC_MESSAGE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_CA_INFO | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_CHARGE_BACK_TYPE | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_CURRENCY | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_CURRENCY_INIT | 02-ng-schema-data | 4 | 1 | 1 | 0 | 0 | 0 | 173 | 0 | 0 |
| BT_CURRENCY_MANAGER | 02-ng-schema-data | 19 | 1 | 1 | 0 | 0 | 0 | 25 | 0 | 0 |
| BT_CURRENCY_PAIR | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_INNER_ACC_APP | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_INPUT_BANK_INFO | 01-base-schema | 14 | 1 | 3 | 1 | 0 | 0 | 297 | 0 | 0 |
| BT_INPUT_BANK_SWIFT | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_INPUT_BANK_SWIFT_MANAGER | 02-ng-schema-data | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_INTERFACE_RES | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_INTERFACE_RES_BILL | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_INTERFACE_SYS | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_INTERFACE_SYS_BILL | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_GE_BYTEARRAY | 02-ng-schema-data | 6 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_GE_PROPERTY | 02-ng-schema-data | 3 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| BT_M_ACT_HI_ACTINST | 02-ng-schema-data | 16 | 1 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_HI_ATTACHMENT | 02-ng-schema-data | 11 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_HI_COMMENT | 02-ng-schema-data | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_HI_DETAIL | 02-ng-schema-data | 15 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_HI_IDENTITYLINK | 02-ng-schema-data | 7 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_HI_PROCINST | 02-ng-schema-data | 15 | 1 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_HI_TASKINST | 02-ng-schema-data | 22 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_HI_VARINST | 02-ng-schema-data | 14 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_ID_BYTEARRAY | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_ID_GROUP | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_ID_INFO | 02-ng-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_ID_MEMBERSHIP | 02-ng-schema-data | 2 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_ID_PRIV | 02-ng-schema-data | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_ID_PRIV_MAPPING | 02-ng-schema-data | 4 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_ID_PROPERTY | 02-ng-schema-data | 3 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| BT_M_ACT_ID_TOKEN | 02-ng-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_ID_USER | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_PROCDEF_INFO | 02-ng-schema-data | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RE_DEPLOYMENT | 02-ng-schema-data | 7 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| BT_M_ACT_RE_MODEL | 02-ng-schema-data | 13 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RE_PROCDEF | 02-ng-schema-data | 15 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_DEADLETTER_JOB | 02-ng-schema-data | 15 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_EVENT_SUBSCR | 02-ng-schema-data | 11 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_EXECUTION | 02-ng-schema-data | 31 | 1 | 8 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_HISTORY_JOB | 02-ng-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_IDENTITYLINK | 02-ng-schema-data | 8 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_JOB | 02-ng-schema-data | 18 | 1 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_SUSPENDED_JOB | 02-ng-schema-data | 16 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_TASK | 02-ng-schema-data | 23 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_TIMER_JOB | 02-ng-schema-data | 18 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_ACT_RU_VARIABLE | 02-ng-schema-data | 12 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_BPM_APPROVAL_NODE | 02-ng-schema-data | 15 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_BPM_APPROVAL_NODE_HIS | 02-ng-schema-data | 17 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_BPM_DELEGATION | 02-ng-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_BPM_DELEGATION_CATEGORY | 02-ng-schema-data | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_BPM_FLOW_CATEGORY | 02-ng-schema-data | 15 | 1 | 0 | 0 | 0 | 0 | 2 | 2 | 0 |
| BT_M_BPM_MODEL_CONFIG | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_BPM_OPTION | 02-ng-schema-data | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_M_RATE_TYPE | 02-ng-schema-data | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_MESSAGE_REPEAT | 01-base-schema | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_NET_CHALK | 01-base-schema | 39 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_NET_UKEY | 01-base-schema | 19 | 1 | 1 | 5 | 5 | 0 | 0 | 0 | 0 |
| BT_NET_UKEY_BANKACC | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_PAY_TYPE | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 2 | 0 | 1 |
| BT_PURPOSE | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_PURPOSE_BANK_TYPE | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_PURPOSE_CORP | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_PURPOSE_COUNTRY | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_PURPOSE_CUR | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_RATE_TYPE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_RATE_TYPE_MANAGER | 02-ng-schema-data | 10 | 1 | 1 | 0 | 0 | 0 | 2 | 0 | 0 |
| BT_SUMMARY | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_SWIFT_INFO | 01-base-schema | 15 | 1 | 4 | 3 | 0 | 0 | 0 | 0 | 0 |
| BT_SWIFT_INFO_MANAGER | 02-ng-schema-data | 15 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_CASE | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 0 |
| BT_TR_LIMIT_CHECK_LOG | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_COND | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_HIT_DETAIL | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_RULE | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_RULE_AUDIT | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_SCOPE | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_TXN | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 0 |
| BT_TR_LIMIT_TXN_BUCKET | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_TR_LIMIT_USAGE | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_WARN_CONDITION | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_WARN_DATA | 01-base-schema | 34 | 1 | 1 | 2 | 2 | 0 | 0 | 0 | 0 |
| BT_WARN_DATA_DTL | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_WARN_EXCEPTION_DATA | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BT_WARN_STRATEGY | 01-base-schema | 25 | 1 | 1 | 1 | 1 | 0 | 0 | 0 | 0 |
| BT_WARNING_LOG | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_LOG | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_PAYEE_CHEQUE | 01-base-schema | 29 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_PRINT_FORMAT | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_PRINT_FORMAT_DTL | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_PRINT_MATCH_RULE | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_PRINT_VOU_OPINFO | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_STORAGE_BOX | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_VOU_EMPLOY_INFO | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_VOU_INFO_DTL | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_VOU_INPUT | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_VOUCHER_TYPE | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_VOUTYPE_DATA_FIELD | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| BV_VOUTYPE_SCENE | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CAS_USER | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CGS_BILL | 01-base-schema | 53 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHARGE_TYPE_BANK_TYPE_REL | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHARGE_TYPE_CHARGE_RULE_REL | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_BANK_FINANCE | 01-base-schema | 46 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_BANK_FINANCE_DETAIL | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_BANK_FINANCE_HISTORY | 01-base-schema | 52 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_INNER_FINANCE | 01-base-schema | 47 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_MESSAGE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_PRODUCT_NETVAL | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_PRODUCT_REDEEM | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_PRODUCT_REDEEM_DETAIL | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_PRODUCT_RENEWAL | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_PRODUCT_REQ | 01-base-schema | 55 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_PRODUCT_REQ_HISTORY | 01-base-schema | 69 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CHO_PRODUCT_TYPE | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMB_BAIL_ACCOUNT_INFO | 01-base-schema | 16 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMB_BNKBILNEG | 01-base-schema | 7 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMB_CRLCON | 01-base-schema | 21 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMB_CRLINFO | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMBC_BAIL_ACCOUNT_INFO | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| CME_GUARANTEE_PLAN | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_ACCRUAL_TYPE | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_EXTEND | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_INTEREST | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_ISSUE_INFO | 01-base-schema | 64 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_PAY | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_REGISTER_INFO | 01-base-schema | 31 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_REPAY_PLAN | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_REPAY_PLAN_DTL | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_SWAP | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_TRANSFER | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_BOND_TYPE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_CONTRACT | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_EQUITY_INFO | 01-base-schema | 22 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_EQUITY_INFO_DETAIL | 01-base-schema | 16 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_EQUITY_PAY_PLAN | 01-base-schema | 12 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_EQUITY_PAY_PLAN_DETAIL | 01-base-schema | 17 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_EQUITY_PAY_THE | 01-base-schema | 31 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_EQUITY_REPO | 01-base-schema | 26 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_EQUITY_REPO_PLAN | 01-base-schema | 13 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_EQUITY_REPO_PLAN_DETAIL | 01-base-schema | 16 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_EQUITY_TYPES | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_FINANCING_LEASE_INFO | 01-base-schema | 65 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_FLOATING_MODE | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_IN_HISTORY | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_INL_ABOLISH | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_INL_ADJUSTRATE | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_INL_DEFERRED | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_INL_INFO | 01-base-schema | 59 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_INL_REPAY | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_INL_REPAYPLAN_CHANGE | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_INL_SEND | 01-base-schema | 47 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_INTEREST_WAY | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEAS_INTEREST | 01-base-schema | 34 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEASE_CAPITAL_PLAN | 01-base-schema | 20 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEASE_CAPITAL_PLANDEL | 01-base-schema | 19 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEASE_CONTINUE | 01-base-schema | 11 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEASE_EXTENDED | 01-base-schema | 17 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEASE_PAY | 01-base-schema | 23 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEASE_REFUND | 01-base-schema | 11 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEASE_REPO | 01-base-schema | 12 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEASE_SUB_MATER | 01-base-schema | 23 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LEASE_UPDATE | 01-base-schema | 66 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LOAN_DEFERRED | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LOAN_INFO | 01-base-schema | 66 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LOAN_REPAY | 01-base-schema | 32 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LOAN_SEND | 01-base-schema | 53 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LOAN_TYPE | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_LOAN_TYPE_MANAGER | 02-ng-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 5 | 0 | 0 |
| CMS_PRECATIVE_LOAN_DEFERRED | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PRECATIVE_LOAN_INFO | 01-base-schema | 54 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PRECATIVE_LOAN_REPAY | 01-base-schema | 29 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PRECATIVE_LOAN_SEND | 01-base-schema | 46 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROCESS_MSG | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_HISTORY | 01-base-schema | 24 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_INTEREST | 01-base-schema | 34 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_INTEREST_TEMP | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_LOAN_ABOLISH | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_LOAN_DEFERRED | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_LOAN_INFO | 01-base-schema | 112 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_LOAN_RATE | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_LOAN_REPAY | 01-base-schema | 48 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_LOAN_SEND | 01-base-schema | 65 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_SYNDICATED | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_SYNDICATED_SEND | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_PROVIDE_SYNDICATEDDTL | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_REPAY_TYPE | 01-base-schema | 32 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_REPAYMENT_SCHEDULE | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_REPAYMENT_SCHEDULEDETAILED | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SCHEDULE_DTL_TEMP | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_ASSET_TYPE | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_INFO | 01-base-schema | 25 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_ISSUE | 01-base-schema | 65 | 0 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_LEVEL | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_LEVEL_INFO | 01-base-schema | 12 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_PLAN | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_PLAN_DETAIL | 01-base-schema | 17 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_REPAY_SURE | 01-base-schema | 24 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_TO_ACCOUNT | 01-base-schema | 16 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_SECURITY_TYPE | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CMS_TENANCY_ITEMS_TYPES | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| COMPUTATIONAL_MODEL | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CON_BUS_HOOK | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CON_INFO | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CON_INFO_BUSDTL | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CON_INFO_RECORD | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CON_LOG | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CON_PD_MAP | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| CON_PD_MAPDTL | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| CON_TYPE | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| CON_TYPE_FIELD | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| CON_WARN_RULES | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CONTACT_ADDRESS_BOOK | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CR_DATA_SOURCE | 01-base-schema | 5 | 1 | 0 | 0 | 0 | 0 | 9 | 0 | 0 |
| CR_QUA_METHOD_FOR_SCORING_IND | 01-base-schema | 18 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CR_SCORING_INDICATOR | 01-base-schema | 20 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CR_SCORING_ORG_CONTRAST | 01-base-schema | 18 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CR_TASK_CORP_INFO | 01-base-schema | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CR_TASK_INFO | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| CR_TASK_ORG_INFO | 01-base-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CRT_SCORING_CORP_CONTRAST | 01-base-schema | 20 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| DATA_SOURCE_CATEGORY | 01-base-schema | 6 | 1 | 0 | 0 | 0 | 0 | 3 | 0 | 0 |
| DF_BUSINESS_TYPE | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| DF_COST_TYPE | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| DF_DIRECT_FINANCE_INFO | 01-base-schema | 45 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| DF_DIRECT_FINANCE_REPAY | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| DF_DIRECT_FINANCE_REPAY_TMP | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| DF_INTERMEDIATE_COST | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| DF_THIRD_PARTY_AGENCY | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| DISTRIBUTED_LOCK | 01-base-schema | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| DOC_INVOICE | 01-base-schema | 15 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| DOC_MANAGE | 01-base-schema | 43 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| DOC_OVERDUE_NOTICE | 01-base-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| DOC_WIRTE_OFF_RELATION | 01-base-schema | 23 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| DSL_INTEFACE_FLAG | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| DSL_INTERFACE_LOG | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_ACCEPT_BILL | 01-base-schema | 44 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_ACCRUAL_BILL | 01-base-schema | 43 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_BILL_MESSAGE | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_BILL_PRINT | 01-base-schema | 11 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_COMPARE_BILL | 01-base-schema | 33 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_CONSIGN_BILL | 01-base-schema | 38 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_DRAFT_BILL_TEMP | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_DRAFT_ORGAN | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_EXCHANGE_BILL | 01-base-schema | 45 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_LOAN_BILL | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_NOTICE_BILL | 01-base-schema | 41 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_POUNDAGE_BILL | 01-base-schema | 41 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_PRECATIVE_LOAN_BILL | 01-base-schema | 39 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_RECEIPT_TYPE | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| E_TRANS_BILL | 01-base-schema | 39 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| E_VISA_TYPE | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_BASIC_DATA | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_DATA_TYPE | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_DATATYPE_FIELD | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_DYNAMIC_DATA | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_DYNAMIC_DATA_TYPE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_HIS_DTL_COMFIELD | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_HIS_DTL_TEMPLATE | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_INTER_CONFIG | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_INTER_CONFIG_FILL | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_INTER_CONFIG_SET | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_PAYINFO | 01-base-schema | 45 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_TEMPLATE | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_TEMPLATE_DATADEFINE | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| ERP_TEMPLATE_DTL | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_TEMPLATE_DTL_STORE | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_TEMPLATE_STORE | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_TEMPLATE_TYPE | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_TEMPLATE_TYPE_DTL | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_TYPE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_VARIABLE | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| ERP_WAGE_DETAIL | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| EVALUATED_DATA_FOR_REPORT | 01-base-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| EVALUATION_BATCH | 01-base-schema | 6 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| EVALUATION_LOG | 01-base-schema | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| EVALUATION_REPORT | 01-base-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET | 01-base-schema | 19 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_DAY | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_DETAIL | 01-base-schema | 27 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_MONTH | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_PERIOD | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_PROCESS | 01-base-schema | 34 | 1 | 4 | 0 | 1 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_QUARTER | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_RESOLVE_MONTH | 01-base-schema | 30 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_RESOLVE_YEAR | 01-base-schema | 48 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_SUPERADD | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_SUPERADD_DETAIL | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_WEEK | 01-base-schema | 12 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_BUDGET_YEAR | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_COLLECT_CORP | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_CORP_BUDGET | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_CORP_ITEM | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_EXCRATE_ADJUST | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_ITEM | 01-base-schema | 28 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_MESSAGE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_PROCESS_RULE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FBS_SALESRETURN_ANALYSE | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACC | 01-base-schema | 64 | 1 | 5 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACC_BALANCE | 01-base-schema | 26 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACC_HISTORY | 01-base-schema | 65 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACC_LOG | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACC_MONTH_BALANCE | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACC_WRITEOFF | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK | 01-base-schema | 33 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_BLNED | 01-base-schema | 33 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_BLNED_DTL | 01-base-schema | 27 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_BLNED_EXT | 01-base-schema | 35 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_DTL | 01-base-schema | 27 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_DTL_HIS | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_EXT | 01-base-schema | 35 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_EXT_HIS | 01-base-schema | 35 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_HIS | 01-base-schema | 34 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_IMPORT | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCBOOK_MSG | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ACCU_BALANCE | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_AGREED_DEPOSIT | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_AGREED_DETAIL | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_AUTO_CHECK_CONFIG | 01-base-schema | 15 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_AUTO_CHECK_CORP | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_AUTO_CHECK_RESULT | 01-base-schema | 17 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BALANCE_SHEET | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_ACC_DTL | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_ACC_GROUP | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_ACC_GROUP_AREA | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_BALANCE_ADJUST | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_BALANCE_ADJUST_DETAIL | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_BILL | 01-base-schema | 33 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_BILL_PROC | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_BILL_TEMP | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_CHECK_DTL | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BANK_CHECK_LOG | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BOND | 01-base-schema | 29 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BOND_ADD | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BOND_APPLY | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BOND_BACK | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BOND_BAL | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BOND_LOG | 01-base-schema | 9 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BOND_TYPE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BUS_RACE | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BUS_RACE_POOL | 01-base-schema | 12 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BUSINESS_RULE_DEFINE | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BUSINESS_RULE_DEFINE_POOL | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_BUSINESS_RULE_INIT | 01-base-schema | 12 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| FC_BUSINESS_RULE_INIT_TEMP | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CARRY | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CASH_FLOW | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CASH_ITEM | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CHARGE_ACCRUE | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CHARGE_RULE | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CHARGE_TYPE | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CHARGE_TYPE_DTL | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CHECK_INIT | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CHECK_TYPE_DTL | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_COMPARE_ACC | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_COMPARE_ACC_EXT | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_COMPARE_ACC_PROC | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_COMPARE_ACC_TEMP | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_COMPARE_CHECK_TYPE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CORP_COMPARE_BILL | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CORP_INTEREST_DTL | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_CORP_POOL | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_DAY_CLOSE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_DAY_START | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_EXCRATE_ADJUST | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_EXCRATE_ADJUST_MANAGER | 02-ng-schema-data | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_FREEZE | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INCOME_STATEMENT | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INIT_ACC | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INSTRATE | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INSTRATE_ADJUST | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INTEREST_DTL | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INTEREST_DTL_RULE | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INTEREST_DTL_TEMP | 01-base-schema | 37 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INTEREST_SINGLE | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INTEREST_SINGLE_NOD_BOOK | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_INTEREST_TYPE | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_LESS_PAY_BILL | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_LOAN_BALANCE_DTL | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_LOAN_REPAY | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_MANUAL_FINACIAL_COLUMN | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| FC_MANUAL_FINACIAL_TYPE | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| FC_MONTH_LEDGER | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_NIS_BUS_TYPE_RULE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_ONLINE_LEDGER | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_OTHER_BUSINESS_RULE_DEFINE | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_OUT_ACC_BALANCE | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_OUT_ACCBOOK | 01-base-schema | 31 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_OUT_ACCBOOK_DTL | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_OUT_ACCBOOK_DTL_HIS | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_OUT_SUBJECT_DAY | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_PRE_OUT_ACCBOOK | 01-base-schema | 31 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_PREDRAW_DTL | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_PREDRAW_DTL_TEMP | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_PREDRAW_RUSH_DTL | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_RECEIPT_TYPE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_REPORT_FORMAT | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_SET_MONTH | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_SUBJECT | 01-base-schema | 25 | 1 | 1 | 0 | 1 | 0 | 0 | 1 | 0 |
| FC_SUBJECT_DAY | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_SUBJECT_INIT | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_SUBJECT_POOL | 01-base-schema | 20 | 1 | 1 | 0 | 2 | 0 | 0 | 0 | 0 |
| FC_SUBJECT_SORT | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| FC_SYS_BUS_RACE | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| FC_SYS_BUS_RACE_POOL | 01-base-schema | 5 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_TALLY_REMIND_DTL | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_UNION_ACCBOOK | 01-base-schema | 31 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_VOUCHER_CASH | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_VOUCHER_ENTRY_DEFINE | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_VOUCHER_ENTRY_DEFINE_POOL | 01-base-schema | 16 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_VOUCHER_ENTRY_INIT | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| FC_VOUCHER_ENTRY_INIT_BACK | 01-base-schema | 16 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| FC_VOUCHER_TYPE | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| FEX_ADD_WORD | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_CASH_FLOW | 01-base-schema | 40 | 1 | 2 | 6 | 0 | 0 | 0 | 0 | 0 |
| FEX_CASH_FLOW_CUST_INFO | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_CORP_BANKACC | 01-base-schema | 15 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| FEX_CORP_BANKACC_DTL | 01-base-schema | 11 | 1 | 1 | 4 | 1 | 0 | 0 | 0 | 0 |
| FEX_CORP_EXPOSURE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_CORP_EXPOSURE_TEMPLATE | 01-base-schema | 10 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| FEX_CORP_POOL | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_CORP_POOL_BANKACC | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_CORP_POOL_LIMIT | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_CORP_POOL_TRAN | 01-base-schema | 11 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| FEX_COTP | 01-base-schema | 25 | 1 | 1 | 19 | 1 | 0 | 0 | 0 | 0 |
| FEX_COTP_BANKACC | 01-base-schema | 23 | 1 | 1 | 2 | 1 | 0 | 0 | 0 | 0 |
| FEX_COTP_BANKACC_CUR | 06-business-schema | 4 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| FEX_COTP_CUST_INFO | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_CUST_FIELD_RULE | 01-base-schema | 18 | 0 | 1 | 6 | 0 | 0 | 1 | 1 | 0 |
| FEX_CUST_FIELD_RULE_TENANT | 06-business-schema | 21 | 1 | 1 | 0 | 2 | 0 | 0 | 0 | 0 |
| FEX_CUST_INFO_TENANT | 06-business-schema | 5 | 1 | 1 | 1 | 1 | 0 | 0 | 0 | 0 |
| FEX_EXPOSURE | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_EXPOSURE_DETAIL | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_EXPOSURE_ITEM | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_EXPOSURE_MAIN | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_EXPOSURE_TEMPLATE | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_EXPOSURE_TEMPLATE_ITEM | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_FOREIGN_REQ | 01-base-schema | 65 | 1 | 1 | 8 | 0 | 0 | 0 | 0 | 0 |
| FEX_LINKMAN_INFO | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_MESSAGE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_PAYER_INFO | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_PRODUCT | 01-base-schema | 35 | 1 | 1 | 3 | 2 | 0 | 0 | 1 | 0 |
| FEX_PROFIT_LOSS_MANAGE | 06-business-schema | 33 | 1 | 0 | 15 | 3 | 0 | 0 | 0 | 0 |
| FEX_PROFIT_LOSS_MANAGE_DTL | 06-business-schema | 17 | 1 | 0 | 4 | 1 | 0 | 0 | 0 | 0 |
| FEX_RATE_EXC | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_RATE_INFO | 01-base-schema | 15 | 1 | 1 | 8 | 4 | 0 | 0 | 0 | 0 |
| FEX_RATE_TYPE | 01-base-schema | 12 | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| FEX_SETTLE_FILL_RULE | 01-base-schema | 17 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| FEX_SETTLE_FILL_RULE_ADD | 01-base-schema | 5 | 1 | 1 | 0 | 2 | 0 | 0 | 0 | 0 |
| FEX_TRAN_APP | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_TRAN_APP_DTL | 01-base-schema | 83 | 1 | 1 | 64 | 7 | 0 | 0 | 0 | 0 |
| FEX_TRAN_APP_DTL_TMP | 01-base-schema | 82 | 1 | 1 | 64 | 7 | 0 | 0 | 0 | 0 |
| fex_tran_config_operation_log | 06-business-schema | 10 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| FEX_TRAN_CUST_INFO | 01-base-schema | 5 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| FEX_TRAN_DEAL | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_TRAN_GROUP | 01-base-schema | 14 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| FEX_TRAN_GROUP_RULE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FEX_TRAN_GROUP_RULE_DTL | 01-base-schema | 9 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| FEX_TRAN_INFO | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 1 |
| FEX_TRAN_LIFE_CYCLE | 06-business-schema | 56 | 1 | 0 | 3 | 4 | 0 | 0 | 0 | 0 |
| FEX_TRAN_OPTION_DEAL | 01-base-schema | 23 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 |
| FEX_TRAN_REQ | 01-base-schema | 144 | 1 | 1 | 92 | 19 | 0 | 0 | 0 | 0 |
| FEX_TRAN_REQ_CONFIG | 06-business-schema | 32 | 1 | 0 | 1 | 0 | 0 | 4412 | 19 | 0 |
| fex_tran_req_config_version | 06-business-schema | 8 | 1 | 0 | 1 | 0 | 0 | 39 | 0 | 0 |
| FEX_TRAN_REQ_EXT | 06-business-schema | 20 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| fex_tran_req_field | 06-business-schema | 19 | 1 | 0 | 1 | 1 | 0 | 136 | 54 | 1 |
| fex_tran_template_config | 06-business-schema | 18 | 1 | 0 | 1 | 0 | 0 | 33 | 14 | 1 |
| FINACIAL_CONSUMER_RECORD | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| FINACIAL_PRODUCER_RECORD | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GLOBAL_TABLE | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_CONRRANT_CHANGE_TRACK | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_CONRRANT_OCCUPY | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_CONRRANT_OCCUPY_DETAIL | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_CONTRACT_INFO | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_CONTRACT_OCCUP | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_CONTRACT_PLEDGE | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_CONTRACT_WAY | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_EXGUARANTEE_AMOUNT | 01-base-schema | 11 | 0 | 1 | 0 | 3 | 0 | 0 | 0 | 0 |
| GUA_EXGUARANTEE_CORP | 01-base-schema | 8 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_GUARANTEE_CHANGE_INFO | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_GUARANTEE_INFO | 01-base-schema | 44 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_LOG | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_MAIN_TYPE | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_MAIN_TYPE_WAY | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_PERFORM_REG | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_PLEDGE_INFO | 01-base-schema | 47 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_PLEDGE_INFO_HIS | 01-base-schema | 44 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_PLEDGE_OCCUP | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_PLEDGE_TYPE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| GUA_WAY | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| INDICATOR_CATEGORY_FIX_REPORT | 01-base-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| INDICATOR_CATEGORY_SOLIDIFICA | 01-base-schema | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| INSTITUTIONAL_RAW_DATA | 01-base-schema | 27 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| LB_BILLHEAD | 01-base-schema | 47 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_BILL_ARRIVAL | 01-base-schema | 40 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_DEPOSIT | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_DEPOSIT_DELAY | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_DEPOSIT_REPAY | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_ENROL | 01-base-schema | 87 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_INCOMING | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_MESSAGE | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_MORTGAGE | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_PAYMENT | 01-base-schema | 26 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LC_PAYMENT_COST | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| LOCK_TABLE | 01-base-schema | 10 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| MODEL_BATCH | 01-base-schema | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| MODEL_FORMULA | 01-base-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| MODEL_FORMULA_FIX_REPORT | 01-base-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| MODEL_SOLIDIFICATION_REPORT | 01-base-schema | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_ALLOCATION_REQ | 01-base-schema | 39 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_BILL | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_BILL_BANK_SERIAL_NO | 01-base-schema | 6 | 0 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_BILL_BANK_SERIAL_NO_REFUND | 01-base-schema | 7 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_BILLHEAD | 01-base-schema | 83 | 1 | 11 | 12 | 3 | 0 | 0 | 0 | 0 |
| NIS_BILLHEAD_POOL | 01-base-schema | 67 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_BINDING_BANKACC | 01-base-schema | 3 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_BUS_TYPE | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_BUS_TYPE_RULE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_BUSINESS_TYPE | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| NIS_CAPITAL_RECORD | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_CORP_BUS | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_CORP_CASHIER | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_CORP_CASHIER_USER | 01-base-schema | 7 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_DEMAND | 01-base-schema | 38 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_EXPENSE_DETAIL | 01-base-schema | 38 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_FLOW_RULE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_FLOW_RULE_PURPOSE | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_MESSAGE | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_OPERATION_TYPE | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| NIS_PAY_SCHEDULING_ELEMENTS | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_PERSONNEL_DETAIL | 01-base-schema | 23 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_PROJECT | 01-base-schema | 14 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_RECEIVABLE | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_RELATED_PARTY | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| nis_scf_info | 06-business-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_SHARE_BUS_TYPE | 01-base-schema | 10 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_WAGE | 01-base-schema | 23 | 1 | 2 | 1 | 0 | 0 | 0 | 0 | 0 |
| NIS_WAGE_DETAIL | 01-base-schema | 29 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_WAGE_DETAIL_BACK | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_WAGE_FLOAT_RATIO_CONFIG | 01-base-schema | 9 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| NIS_WAGE_RELEASE_CONFIG | 01-base-schema | 22 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| OAUTH_ACCESS_TOKEN | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| OAUTH_APPROVALS | 01-base-schema | 7 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OAUTH_CLIENT_DETAILS | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| OAUTH_CLIENT_TOKEN | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OAUTH_CODE | 01-base-schema | 3 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OAUTH_REFRESH_TOKEN | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_BOARD | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_BOARD_PARAM | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_BOARD_ROLE_PERMISSION | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_BOARD_USER_PERMISSION | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_CATEGORY | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_DATASET | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_DATASOURCE | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_ROLE_PERMISSION | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_USER_PERMISSION | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OLAP_WIDGET | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| OWNERSHIP_STRUCTURE_DATA | 01-base-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| PAYMENT_CONSUMER_RECORD | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| PAYMENT_PRODUCER_RECORD | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| PK_FEX_SETTLE_FILL_RULE_ADD | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| Q_M_SCORING_I_FIX_REPORT | 01-base-schema | 21 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_BLOB_TRIGGERS | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_CALENDARS | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_CRON_TRIGGERS | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_FIRED_TRIGGERS | 01-base-schema | 14 | 1 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_JOB_DETAILS | 01-base-schema | 11 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_LOCKS | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_PAUSED_TRIGGER_GRPS | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_SCHEDULER_STATE | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_SIMPLE_TRIGGERS | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_SIMPROP_TRIGGERS | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| QRTZ_TRIGGERS | 01-base-schema | 17 | 1 | 14 | 0 | 0 | 0 | 0 | 0 | 0 |
| QUANTITATIVE_METHOD_FOR_SIS | 01-base-schema | 19 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_AUDIT_INFO | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_AUDIT_INFO_HIS | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_AUDIT_INTER | 01-base-schema | 11 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_AUDIT_STRATEGY | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_AUDIT_TASK | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_BACK_PAY | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_BANK_CARD_BINDING | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_IMPORT_CONF | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_IMPORT_CONF_DTL | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_LOG | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_OTHER_CARD_BINDING | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_POS_BINDING | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_POS_BORROW | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_RECEIVED_DTL | 01-base-schema | 29 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_RECEIVED_HIS_DTL | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_RECEIVED_SU_DTL | 01-base-schema | 27 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_REVENUE_DTL | 01-base-schema | 22 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_REVENUE_HIS_DTL | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_REVENUE_SU_DTL | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_SHOP | 01-base-schema | 20 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAS_SHOP_GRANT | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BK_STAT_REPORT_HISTORY | 01-base-schema | 21 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION | 01-base-schema | 48 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_ADD | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_ADD_DETAIL | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_ADJUST | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_ADJUST_DETAIL | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_DELAY | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_DETAIL | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_DIS | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_DIS_DETAIL | 01-base-schema | 30 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_RELIEF | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BKRATION_RELIEF_DETAIL | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_BUSINESS_RACE | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_CREDIT_COOPERATION_BANK | 01-base-schema | 9 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_CREDIT_TYPE | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_ITEM_CUSTOM | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_ITEM_TYPE | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_PROCESS_LOG | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_RATION_DETAIL_DISHIS | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_RATION_LOG | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_SYS_ITEM | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| RAT_URATION | 01-base-schema | 39 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_URATION_APPLY | 01-base-schema | 46 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_URATION_APPLY_DTL | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAT_YEARFIN_PLAN_REGISTER | 01-base-schema | 14 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| RAW_DATA | 01-base-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_ent_bank_open | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_funder_info | 06-business-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCF_INSTRUCTION | 06-business-schema | 26 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_invoice_info | 06-business-schema | 21 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_loan_info | 06-business-schema | 42 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_payable_voucher | 06-business-schema | 43 | 1 | 0 | 10 | 0 | 0 | 0 | 0 | 0 |
| scf_receivable_payment_info | 06-business-schema | 30 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_receivable_transfer_info | 06-business-schema | 28 | 1 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| scf_receivable_voucher_info | 06-business-schema | 43 | 1 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| scf_voucher_custom_field | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| scf_voucher_redemption | 06-business-schema | 21 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_GRADE | 01-base-schema | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_GRADE_FIX_REPORT | 01-base-schema | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_GRADE_LE_FIX_REPORT | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_GRADE_LEVEL | 01-base-schema | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_GRADE_LEVEL_SOLID | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_GRADE_SOLID | 01-base-schema | 15 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_INDICATOR_FIX_REPORT | 01-base-schema | 22 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_INDICATOR_SOLIDIFICATI | 01-base-schema | 22 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_INDICATOR_VALUE | 01-base-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SCORING_METRIC_VAL_FIX_REPORT | 01-base-schema | 10 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ACCOUNT_GRANT | 01-base-schema | 9 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ACCOUNT_GRANT_HIS | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ACCOUNT_MAPPING | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_API_SERVER_CONFIG | 01-base-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ARTICLE | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ARTICLE_MANAGER | 02-ng-schema-data | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ARTICLE_ROLE | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ARTICLE_ROLE_MANAGER | 02-ng-schema-data | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ARTICLE_USER | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ARTICLE_USER_HIS | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ARTICLE_USER_HIS_MANAGER | 02-ng-schema-data | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ARTICLE_USER_MANAGER | 02-ng-schema-data | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ATTACHMENT | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ATTACHMENT_MANAGER | 02-ng-schema-data | 16 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| SYS_BANK_REGION | 01-base-schema | 4 | 1 | 3 | 0 | 0 | 0 | 5 | 0 | 0 |
| SYS_BLACK_WHITE_LIST | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CA_INFO | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CALENDAR | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CALENDAR_MANAGER | 02-ng-schema-data | 10 | 1 | 0 | 0 | 0 | 0 | 351 | 0 | 0 |
| SYS_CELL_MANAGE | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_CONFIG | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_CONFIG_BACK | 01-base-schema | 6 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CONFIG_LOG | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CONFIG_LOG_MANAGER | 02-ng-schema-data | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CONFIG_MANAGER | 02-ng-schema-data | 6 | 1 | 2 | 0 | 0 | 0 | 436 | 0 | 0 |
| SYS_CORP | 01-base-schema | 41 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CORP_CM_MAP | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CORP_CM_MAP_MANAGER | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CORP_MANAGE_MANAGER | 02-ng-schema-data | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CORP_MANAGER | 02-ng-schema-data | 39 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_CORP_TO_LOGIN_NAME | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CORP_USER_RESOURCE | 01-base-schema | 7 | 0 | 7 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_CORP_USER_RESOURCE_MANAGER | 02-ng-schema-data | 6 | 0 | 7 | 0 | 0 | 0 | 141 | 0 | 0 |
| SYS_COUNTRY | 02-ng-schema-data | 13 | 1 | 1 | 0 | 0 | 0 | 252 | 0 | 0 |
| SYS_COUNTRY_CUST | 01-base-schema | 15 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_COUNTRY_CUST_MANAGER | 02-ng-schema-data | 15 | 1 | 1 | 0 | 0 | 0 | 5 | 0 | 0 |
| SYS_DICT | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 367 | 33 | 10 |
| SYS_DICT_GROUP | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 63 | 1 | 0 |
| SYS_DUAL_MANAGER | 02-ng-schema-data | 7 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_EQUITY_MAINTENANCE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ERROR_LOG_MANAGER | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_EXTERNAL_CORP | 01-base-schema | 44 | 1 | 3 | 6 | 0 | 0 | 0 | 0 | 0 |
| SYS_EXTERNAL_CORP_AUDIT_LOG | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_EXTERNAL_CORP_BANKACC | 01-base-schema | 22 | 1 | 3 | 13 | 1 | 0 | 0 | 0 | 0 |
| SYS_EXTERNAL_CORP_QUICK_PAY | (ALTER only) | 0 | 0 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| SYS_EXTERNALCORP_CORP | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_HOME | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_HOME_CATEGORY | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_HOME_MANAGER | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_HOME_WIDGET | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_HOME_WIDGET_MANAGER | 02-ng-schema-data | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_INTERNATION | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 7 | 0 | 0 |
| SYS_INTERNATION_BASE | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 96 | 23 | 0 |
| SYS_LOG | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_LOG_MANAGER | 02-ng-schema-data | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_MENU | 01-base-schema | 19 | 1 | 1 | 0 | 0 | 0 | 21 | 10 | 1 |
| SYS_MENU_COLLECT | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_MENU_COLLECT_MANAGER | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_MENU_MANAGER | 02-ng-schema-data | 18 | 1 | 0 | 0 | 0 | 0 | 15 | 11 | 2 |
| SYS_MODULE | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_MODULE_MANAGER | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_MODULE_MENU | 01-base-schema | 3 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_MODULE_MENU_MANAGER | 02-ng-schema-data | 2 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_MULTI_LANG | 01-base-schema | 11 | 1 | 2 | 0 | 0 | 0 | 1577 | 49 | 1 |
| SYS_NOTIFICATION | 01-base-schema | 11 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_CATEGORY | 01-base-schema | 17 | 1 | 2 | 0 | 0 | 0 | 3 | 0 | 0 |
| SYS_NOTIFICATION_CATEGORY_M | 02-ng-schema-data | 15 | 1 | 1 | 0 | 0 | 0 | 2 | 0 | 0 |
| SYS_NOTIFICATION_EXT | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_HISTORIC | 01-base-schema | 13 | 1 | 4 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_HISTORIC_M | 02-ng-schema-data | 12 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_MANAGER | 02-ng-schema-data | 10 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_MESSAGE | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_MESSAGE_M | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_ROLE | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_ROLE_MANAGER | 02-ng-schema-data | 3 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_USER | 01-base-schema | 4 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_NOTIFICATION_USER_MANAGER | 02-ng-schema-data | 3 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_OAUTH_ACCESS_TOKEN | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_OAUTH_ACCESS_TOKEN_MANAGER | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_OAUTH_CLIENT_DETAILS | 01-base-schema | 17 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_OAUTH_CLIENT_DETAILS_M | 02-ng-schema-data | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_OAUTH_CODE | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_OAUTH_CODE_MANAGER | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_OPERATION_AREA | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ORGANTYPE | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ORGANTYPE_CORP | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ORGANTYPE_CORP_MANAGER | 02-ng-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ORGANTYPE_CORP_RELEASE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ORGANTYPE_CORP_RELEASE_M | 02-ng-schema-data | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ORGANTYPE_MANAGER | 02-ng-schema-data | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ORGANTYPE_MODULE | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ORGANTYPE_MODULE_MANAGER | 02-ng-schema-data | 6 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_ORGANTYPE_RELEASE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ORGANTYPE_RELEASE_MANAGER | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_PERMISSION | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_PERMISSION_MANAGER | 02-ng-schema-data | 3 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_PROVINCE | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_REGION | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 705 | 0 | 0 |
| SYS_RESOURCES | 01-base-schema | 12 | 1 | 3 | 0 | 0 | 0 | 17 | 0 | 1 |
| SYS_RESOURCES_MANAGER | 02-ng-schema-data | 10 | 1 | 2 | 0 | 0 | 0 | 10 | 0 | 0 |
| SYS_ROLE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ROLE_MANAGER | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_ROLE_MENU | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_ROLE_MENU_MANAGER | 02-ng-schema-data | 3 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_SERVICE_PRODUCTION | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_SIGN_RECORD_MANAGER | 02-ng-schema-data | 17 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_SUB_ADMIN_CORP_MANAGER | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_SUPPLIER_TYPE | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_TABLE_RECORD | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_TABLE_RECORD_MANAGER | 06-business-schema | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_TASK | 01-base-schema | 24 | 1 | 1 | 0 | 0 | 0 | 2 | 0 | 0 |
| SYS_TASK_AUDIT | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_TENANT | 01-base-schema | 17 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_TENANT_MAC | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_TENANT_MANAGER | 02-ng-schema-data | 39 | 1 | 1 | 3 | 0 | 0 | 0 | 0 | 0 |
| SYS_TENANT_RECORD_MANAGER | 02-ng-schema-data | 37 | 1 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| SYS_TENANT_USER_RECORD_MANAGER | 02-ng-schema-data | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_TIME_ZONE | 01-base-schema | 3 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_UNIT_STORE_MAINTENANCE | 01-base-schema | 12 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER | 01-base-schema | 25 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_CONFIG | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_CONFIG_MANAGER | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_CORP_ROLE | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_FACE_DETECTION | 01-base-schema | 5 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_FACE_DETECTION_M | 02-ng-schema-data | 4 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_HI_PWD | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_HI_PWD_MANAGER | 02-ng-schema-data | 5 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_LOGIN_LOG | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_LOGIN_LOG_MANAGER | 02-ng-schema-data | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_MANAGER | 02-ng-schema-data | 22 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| SYS_USER_MESSAGE | 01-base-schema | 10 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_MESSAGE_MANAGER | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_PERMISSION_LOG_M | 02-ng-schema-data | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_ROLE | 01-base-schema | 4 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| SYS_USER_ROLE_MANAGER | 02-ng-schema-data | 3 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| TB_BASE_RATE | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TB_CREDIT_PERIOD | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TB_GENERATOR | 01-base-schema | 9 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| TB_LOAN_RATE_MAINTENCE | 01-base-schema | 21 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TCC_FENCE_LOG | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| TENANT_BIS_BIF_INIT | 01-base-schema | 14 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| TENANT_P_BI_REMAIN_TERM_DETAIL | 02-ng-schema-data | 12 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| TENANT_P_FEX_PRODUCT | 06-business-schema | 37 | 1 | 0 | 0 | 0 | 0 | 2 | 1 | 0 |
| TENANT_P_NOTIFICATION_CATEGORY | 02-ng-schema-data | 17 | 0 | 0 | 0 | 0 | 0 | 1 | 1 | 1 |
| TENANT_PRE_ACC_ATTRIBUTE | 02-ng-schema-data | 15 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| TENANT_PRE_ACC_NATURE | 02-ng-schema-data | 11 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| TENANT_PRE_ACT_EXTEND_CATEGORY | 02-ng-schema-data | 21 | 0 | 0 | 0 | 0 | 0 | 14 | 3 | 2 |
| TENANT_PRE_BI_REMAIN_TERM | 02-ng-schema-data | 9 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| TENANT_PRE_BPM_FLOW_CATEGORY | 02-ng-schema-data | 15 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| TENANT_PRE_CMS_LOAN_TYPE | 02-ng-schema-data | 15 | 0 | 0 | 0 | 0 | 0 | 5 | 1 | 0 |
| TENANT_PRE_CONFIG | 02-ng-schema-data | 5 | 0 | 0 | 0 | 0 | 0 | 4 | 0 | 0 |
| TENANT_PRE_FBS_ITEM | 02-ng-schema-data | 28 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| TENANT_PRE_FC_SUBJECT | 02-ng-schema-data | 25 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| TENANT_PRE_FC_VOUCHER_TYPE | 02-ng-schema-data | 20 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 1 |
| TENANT_PRE_MENU | 02-ng-schema-data | 20 | 0 | 0 | 0 | 0 | 0 | 48 | 600 | 5 |
| TENANT_PRE_MENU_1 | 02-ng-schema-data | 20 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TENANT_PRE_RESOURCES | 02-ng-schema-data | 13 | 0 | 0 | 0 | 0 | 0 | 68 | 0 | 60 |
| TENANT_PRE_RESOURCES_1 | 02-ng-schema-data | 13 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TENANT_PRE_TABLE_RECORD | 06-business-schema | 5 | 1 | 0 | 0 | 0 | 0 | 2 | 0 | 0 |
| TENANT_PRE_TB_CREDIT_PERIOD | 02-ng-schema-data | 13 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 |
| TP_ACCOUNT_TYPE | 06-business-schema | 13 | 1 | 0 | 0 | 0 | 0 | 14 | 0 | 0 |
| TP_ALIPAY_BAL | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_ALIPAY_DTL | 01-base-schema | 18 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_ALIPAY_DTL_HIS | 01-base-schema | 28 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_BAIL_BILL_JOB | 06-business-schema | 7 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_BILL_JOB | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_BILL_VIEW | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| TP_BILL_VIEW_DTL | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| TP_BUSINESS | 01-base-schema | 15 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_BUSINESS_CORP | 01-base-schema | 4 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_BUSINESS_TEXT | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_DEWU_DTL | 06-business-schema | 16 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_DOUYIN_DTL | 06-business-schema | 50 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_DOUYIN_QUERY_TRACKER | 06-business-schema | 8 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_KUAISHOU_BAL | 06-business-schema | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_KUAISHOU_FUND_DTL | 06-business-schema | 14 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_LOG | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_MERCHANT | 01-base-schema | 16 | 1 | 1 | 6 | 6 | 0 | 0 | 0 | 0 |
| tp_merchant_auth | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_MERCHANT_VAR | 01-base-schema | 9 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_OFFLINE_BAL | 06-business-schema | 19 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_OFFLINE_BOND_BAL | 06-business-schema | 18 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_OFFLINE_BOND_FLOW_DTL | 06-business-schema | 18 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_OFFLINE_FLOW_DTL | 06-business-schema | 19 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_PAYMENT | 01-base-schema | 34 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_PINDUODUO_DTL | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| tp_platform_interface_info | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 8 | 0 | 0 |
| TP_PLATFORM_PARAM | 06-business-schema (dynamic CREATE; manual-review) | 0 | 1 | 0 | 0 | 0 | 0 | 35 | 1 | 0 |
| TP_QUOTA | 01-base-schema | 16 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_REFUND_ORDER | 01-base-schema | 36 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_SHOPTYPE | 01-base-schema | 11 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_TMALL_BAIL_BILL | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_TMALL_BAL | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_TMALL_FUND_DTL | 06-business-schema | 18 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_WEIXINPAY_BAL | 01-base-schema | 6 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_WEIXINPAY_DTL | 01-base-schema | 31 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_WEIXINSTORE_BAL | 06-business-schema | 9 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_WEIXINSTORE_FUND_BILL | 06-business-schema | 12 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_WEIXINSTORE_RELATED_INFO | 06-business-schema | 13 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_XIAOHONGSHU_DTL | 06-business-schema | 19 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| TP_YLPAY_DTL | 01-base-schema | 39 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| UNDO_LOG | 01-base-schema | 8 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| USER_MOBILE_CHART_MODULE | 01-base-schema | 5 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| VERSION_CURRENT | 01-base-schema | 6 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| VISIT_RECORD_REGISTRATION | 01-base-schema | 11 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| WORKFLOW_BUS_ERROR_LOG | 01-base-schema | 10 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| XXL_JOB_GROUP | 01-base-schema | 7 | 1 | 1 | 0 | 0 | 0 | 2 | 0 | 0 |
| XXL_JOB_INFO | 01-base-schema | 25 | 1 | 1 | 0 | 0 | 0 | 158 | 1 | 0 |
| XXL_JOB_LOCK | 01-base-schema | 2 | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 0 |
| XXL_JOB_LOG | 01-base-schema | 16 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |
| XXL_JOB_LOG_REPORT | 01-base-schema | 7 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| XXL_JOB_LOGGLUE | 01-base-schema | 8 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| XXL_JOB_LOGIN_LOG | 01-base-schema | 13 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| XXL_JOB_OPERATE_LOG | 01-base-schema | 14 | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 |
| XXL_JOB_PERMISSION | 01-base-schema | 10 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| XXL_JOB_REGISTRY | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 0 | 0 | 0 |
| XXL_JOB_USER | 01-base-schema | 6 | 1 | 2 | 0 | 0 | 0 | 1 | 0 | 0 |
| YEAR_BANK_CREDIT_PLAN | 01-base-schema | 17 | 1 | 3 | 0 | 0 | 0 | 0 | 0 | 0 |

## 差异比对

### 仅 MySQL 存在

（无）

### 仅 Oracle 存在

（无）

### 字段数不一致

| 表名 | MySQL | Oracle | 差异 |
| --- | ---: | ---: | ---: |
| bt_currency_manager | 0 | 19 | -19 |
| bt_rate_type_manager | 0 | 10 | -10 |
| BT_TR_LIMIT_CASE | 30 | 0 | 30 |
| BT_TR_LIMIT_CHECK_LOG | 13 | 0 | 13 |
| BT_TR_LIMIT_COND | 13 | 0 | 13 |
| BT_TR_LIMIT_HIT_DETAIL | 21 | 0 | 21 |
| BT_TR_LIMIT_RULE | 20 | 0 | 20 |
| BT_TR_LIMIT_RULE_AUDIT | 8 | 0 | 8 |
| BT_TR_LIMIT_SCOPE | 11 | 0 | 11 |
| BT_TR_LIMIT_TXN | 28 | 0 | 28 |
| BT_TR_LIMIT_TXN_BUCKET | 14 | 0 | 14 |
| BT_TR_LIMIT_USAGE | 14 | 0 | 14 |
| fex_tran_req_config | 0 | 32 | -32 |
| scf_ent_bank_open | 0 | 12 | -12 |
| scf_funder_info | 0 | 13 | -13 |
| SCF_INSTRUCTION | 0 | 26 | -26 |
| scf_voucher_custom_field | 0 | 12 | -12 |
| sys_attachment_manager | 0 | 16 | -16 |
| sys_calendar_manager | 0 | 10 | -10 |
| sys_config_manager | 1 | 6 | -5 |
| sys_external_corp_quick_pay | 14 | 0 | 14 |
| TENANT_PRE_TABLE_RECORD | 0 | 5 | -5 |
| tp_merchant_auth | 11 | 0 | 11 |
| tp_platform_interface_info | 10 | 0 | 10 |
| TP_PLATFORM_PARAM | 5 | 0 | 5 |

### 主键净数不一致

| 表名 | MySQL | Oracle | 差异 |
| --- | ---: | ---: | ---: |
| BT_BANK_TYPE_OTHER_CUSTOM | 1 | 0 | 1 |
| FEX_COTP_BANKACC_CUR | 0 | 1 | -1 |
| FEX_PROFIT_LOSS_MANAGE | 0 | 1 | -1 |
| FEX_PROFIT_LOSS_MANAGE_DTL | 0 | 1 | -1 |
| fex_tran_config_operation_log | 0 | 1 | -1 |
| FEX_TRAN_LIFE_CYCLE | 0 | 1 | -1 |
| fex_tran_req_config_version | 0 | 1 | -1 |
| FEX_TRAN_REQ_EXT | 0 | 1 | -1 |
| fex_tran_req_field | 0 | 1 | -1 |
| fex_tran_template_config | 0 | 1 | -1 |
| sys_external_corp_quick_pay | 1 | 0 | 1 |

### 索引数不一致

| 表名 | MySQL | Oracle | 差异 |
| --- | ---: | ---: | ---: |
| ab_pool_into_repeal | 2 | 1 | 1 |
| act_extend_msg_record | 1 | 0 | 1 |
| BP_BANK_TXN_SCHEDULED_TASK | 0 | 1 | -1 |
| BP_TENANT_BANK_APP_MONITOR | 0 | 1 | -1 |
| BP_TENANT_OPERATION | 0 | 1 | -1 |
| BP_TENANT_VERSION_MANAGER | 0 | 2 | -2 |
| bt_m_act_hi_varinst | 4 | 5 | -1 |
| bt_m_act_ru_job | 5 | 6 | -1 |
| bt_m_act_ru_variable | 4 | 5 | -1 |
| BT_TR_LIMIT_CASE | 3 | 0 | 3 |
| BT_TR_LIMIT_CHECK_LOG | 2 | 0 | 2 |
| BT_TR_LIMIT_COND | 2 | 0 | 2 |
| BT_TR_LIMIT_HIT_DETAIL | 2 | 0 | 2 |
| BT_TR_LIMIT_RULE | 2 | 0 | 2 |
| BT_TR_LIMIT_RULE_AUDIT | 1 | 0 | 1 |
| BT_TR_LIMIT_SCOPE | 2 | 0 | 2 |
| BT_TR_LIMIT_TXN | 4 | 0 | 4 |
| BT_TR_LIMIT_TXN_BUCKET | 2 | 0 | 2 |
| BT_TR_LIMIT_USAGE | 2 | 0 | 2 |
| nis_scf_info | 2 | 0 | 2 |
| sys_internation | 2 | 1 | 1 |
| sys_internation_base | 2 | 1 | 1 |
| TENANT_PRE_TABLE_RECORD | 1 | 0 | 1 |
| tp_business | 2 | 1 | 1 |
| tp_business_corp | 2 | 1 | 1 |
| tp_business_text | 2 | 1 | 1 |
| TP_OFFLINE_BAL | 2 | 0 | 2 |
| TP_OFFLINE_BOND_BAL | 1 | 0 | 1 |
| TP_OFFLINE_BOND_FLOW_DTL | 1 | 0 | 1 |
| TP_OFFLINE_FLOW_DTL | 2 | 0 | 2 |

### 字段新增(ALTER ADD)不一致

| 表名 | MySQL | Oracle | 差异 |
| --- | ---: | ---: | ---: |
| bis_foreign_exc | 67 | 66 | 1 |
| bt_bank_acc | 2 | 5 | -3 |
| bt_bank_type_custom | 1 | 3 | -2 |
| bt_net_ukey | 0 | 5 | -5 |
| bt_warn_data | 0 | 2 | -2 |
| fex_cotp_bankacc | 3 | 2 | 1 |
| FEX_PROFIT_LOSS_MANAGE | 14 | 15 | -1 |
| fex_rate_info | 9 | 8 | 1 |
| fex_tran_app_dtl | 63 | 64 | -1 |
| fex_tran_app_dtl_tmp | 62 | 64 | -2 |
| fex_tran_req | 93 | 92 | 1 |
| nis_billhead | 9 | 12 | -3 |
| sys_external_corp_bankacc | 12 | 13 | -1 |
| tp_merchant | 0 | 6 | -6 |

### 字段修改(ALTER MODIFY/CHANGE/RENAME)不一致

| 表名 | MySQL | Oracle | 差异 |
| --- | ---: | ---: | ---: |
| bis_foreign_exc | 32 | 26 | 6 |
| bt_bank_acc | 2 | 3 | -1 |
| bt_bank_type | 6 | 3 | 3 |
| bt_net_ukey | 0 | 5 | -5 |
| bt_swift_info | 3 | 0 | 3 |
| bt_warn_data | 0 | 2 | -2 |
| fc_subject | 0 | 1 | -1 |
| fex_cotp | 4 | 1 | 3 |
| fex_cotp_bankacc | 2 | 1 | 1 |
| FEX_PROFIT_LOSS_MANAGE | 5 | 3 | 2 |
| fex_rate_info | 6 | 4 | 2 |
| fex_tran_app_dtl | 10 | 7 | 3 |
| fex_tran_app_dtl_tmp | 10 | 7 | 3 |
| fex_tran_req | 18 | 19 | -1 |
| nis_billhead | 0 | 3 | -3 |
| sys_external_corp | 6 | 0 | 6 |
| sys_external_corp_bankacc | 12 | 1 | 11 |
| tp_merchant | 0 | 6 | -6 |

### 字段删除(ALTER DROP)不一致

| 表名 | MySQL | Oracle | 差异 |
| --- | ---: | ---: | ---: |
| （一致） | | | |

### 新增(INSERT)不一致

| 表名 | MySQL | Oracle | 差异 |
| --- | ---: | ---: | ---: |
| bt_input_bank_info | 1 | 297 | -296 |
| bt_pay_type | 1 | 2 | -1 |
| fc_voucher_type | 0 | 1 | -1 |
| fex_tran_req_field | 138 | 136 | 2 |
| sys_bank_region | 1 | 5 | -4 |
| sys_dict | 264 | 367 | -103 |
| sys_dict_group | 49 | 63 | -14 |
| sys_internation | 1 | 7 | -6 |
| sys_internation_base | 14 | 96 | -82 |
| sys_menu | 0 | 21 | -21 |
| sys_multi_lang | 1356 | 1577 | -221 |
| sys_notification_category | 0 | 3 | -3 |
| sys_resources | 0 | 17 | -17 |
| sys_task | 0 | 2 | -2 |
| tenant_pre_menu | 44 | 48 | -4 |
| tenant_pre_resources | 61 | 68 | -7 |
| TP_ACCOUNT_TYPE | 0 | 14 | -14 |
| tp_platform_interface_info | 0 | 8 | -8 |
| TP_PLATFORM_PARAM | 0 | 35 | -35 |

### 修复(UPDATE)不一致

| 表名 | MySQL | Oracle | 差异 |
| --- | ---: | ---: | ---: |
| fc_subject | 0 | 1 | -1 |
| fex_tran_req_config | 21 | 19 | 2 |
| fex_tran_req_field | 55 | 54 | 1 |
| sys_dict | 18 | 33 | -15 |
| sys_dict_group | 0 | 1 | -1 |
| sys_menu | 6 | 10 | -4 |
| sys_multi_lang | 48 | 49 | -1 |
| tenant_pre_menu | 603 | 600 | 3 |
| TP_PLATFORM_PARAM | 0 | 1 | -1 |

### 删除(DELETE)不一致

| 表名 | MySQL | Oracle | 差异 |
| --- | ---: | ---: | ---: |
| sys_dict | 6 | 10 | -4 |
| tenant_pre_menu | 8 | 5 | 3 |
