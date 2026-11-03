##修改人:zhaohc
##修改内容:添加国际化
##修改时间:2024-05-07

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '61d8df940f524862b7cdc2f4cadf3ead', 'zh_CN', '银行类别', 'table.fcInterestDtlRule.bankTypeId' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.fcInterestDtlRule.bankTypeId' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd5a628570a3c4813ab769f6448dbbdde', 'en_US', 'Bank Category', 'table.fcInterestDtlRule.bankTypeId' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.fcInterestDtlRule.bankTypeId' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '962c9ef6676944ffb64362bb713a0bcc', 'zh_CN', '用途/摘要', 'table.fcInterestDtlRule.remark' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.fcInterestDtlRule.remark' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9ab5e12cbd6548519fca282b3471fd9b', 'en_US', 'Purpose / abstract', 'table.fcInterestDtlRule.remark' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.fcInterestDtlRule.remark' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '88ffab9979364dc0906238d1deb1d57f', 'zh_CN', '入账日期', 'table.fcInterestDtlRule.transTime' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.fcInterestDtlRule.transTime' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e11818b8b7c4442497a574d3b30bbcaf', 'en_US', 'Date of receipt', 'table.fcInterestDtlRule.transTime' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.fcInterestDtlRule.transTime' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7f86ca55666343e79486a80c62fdc24d', 'zh_CN', '状态', 'table.fcInterestDtlRule.validSign' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.fcInterestDtlRule.validSign' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1a60778989004751b93046fa49e588c1', 'en_US', 'status', 'table.fcInterestDtlRule.validSign' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.fcInterestDtlRule.validSign' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'bdbdb85d5dc1428c98cd3c9bea143896', 'zh_CN', '一个银行只能一条规则！', 'fin.fcinterestdtlrule.banktype.unique' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='fin.fcinterestdtlrule.banktype.unique' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9c331ce0960c4e448cb3141ee537c04e', 'en_US', 'A bank can only have one rule！', 'fin.fcinterestdtlrule.banktype.unique' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='fin.fcinterestdtlrule.banktype.unique' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人:zhaohc
##修改内容:添加数据字典
##修改时间:2024-05-07

insert into SYS_DICT_GROUP (ID,TYPE_GROUP_CODE,TYPE_GROUP_NAME)
select '5d695d73be8a401ea7c1db2163c158c3', 'fcInterestDtlRule.validSign', '状态' from dual
where not exists (select 1 from SYS_DICT_GROUP where id = '5d695d73be8a401ea7c1db2163c158c3' limit 1);
##GOGO

insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '0f39e56868594db1884c2ab271e183b7', '0', '禁用', '', '5d695d73be8a401ea7c1db2163c158c3', '1' from dual
where not exists (select 1 from SYS_DICT where id = '0f39e56868594db1884c2ab271e183b7');
##GOGO
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select 'c2952f25fa8046c8b6156ba5631f5ca2', '1', '有效', '', '5d695d73be8a401ea7c1db2163c158c3', '1' from dual
where not exists (select 1 from SYS_DICT where id = 'c2952f25fa8046c8b6156ba5631f5ca2');
##GOGO

##修改人:zhaohc
##修改内容:添加菜单权限
##修改时间:2024-05-07
##新增菜单 "结算管理》基础数据维护》利息流水规则维护"
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, IS_MODULE, IS_EXTEND)
SELECT '7ce4f856312a4200a44522bf79aca189', '', '利息流水规则维护', '利息流水规则维护', 'd61f91e27ed9473ab9e8183343ea4507', null, 24, 1, '/finacial/interest/fcinterestdtlrule/list', 0, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE ID = '7ce4f856312a4200a44522bf79aca189' LIMIT 1);
##GOGO

##新增菜单资源 查询资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '7147d0b95e334c278b70dd4bb3423c87', '应用资源', '应用资源', 'interest:fcinterestdtlrule:apply', 1, '7ce4f856312a4200a44522bf79aca189' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_resources WHERE ID = '7147d0b95e334c278b70dd4bb3423c87' LIMIT 1);
##GOGO


##修改人:zhaohc
##修改内容:添加业务类别表
##修改时间:2024-05-07
INSERT INTO nis_bus_type (ID, BUS_TYPE, BUS_TYPE_NAME, CD_SIGN, VALID_SIGN)
SELECT 'f7c5dbb8ac2d4dcba72dd769a85a19be', 'bs004', '利息收入', '0', '1' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM nis_bus_type WHERE ID = 'f7c5dbb8ac2d4dcba72dd769a85a19be' LIMIT 1);
##GOGO
INSERT INTO nis_bus_type (ID, BUS_TYPE, BUS_TYPE_NAME, CD_SIGN, VALID_SIGN)
SELECT '3e47ec272a8a48bca9d2894dbe50ee5b', 'bs005', '上划-资金系统自动', '0', '1' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM nis_bus_type WHERE ID = '3e47ec272a8a48bca9d2894dbe50ee5b' LIMIT 1);
##GOGO
INSERT INTO nis_bus_type (ID, BUS_TYPE, BUS_TYPE_NAME, CD_SIGN, VALID_SIGN)
SELECT 'ba4c7cfb747440938f642fef2f282f8d', 'bs006', '上划-银行系统自动', '0', '1' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM nis_bus_type WHERE ID = 'ba4c7cfb747440938f642fef2f282f8d' LIMIT 1);
##GOGO
INSERT INTO nis_bus_type (ID, BUS_TYPE, BUS_TYPE_NAME, CD_SIGN, VALID_SIGN)
SELECT 'ec40b524497a443cb157d894e6b9a92a', 'bs007', '上划-人工', '0', '1' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM nis_bus_type WHERE ID = 'ec40b524497a443cb157d894e6b9a92a' LIMIT 1);
##GOGO
INSERT INTO nis_bus_type (ID, BUS_TYPE, BUS_TYPE_NAME, CD_SIGN, VALID_SIGN)
SELECT '5f877f40e93c4c1880728deb8d0a80c4', 'bs008', '下拨-资金系统自动', '0', '1' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM nis_bus_type WHERE ID = '5f877f40e93c4c1880728deb8d0a80c4' LIMIT 1);
##GOGO
INSERT INTO nis_bus_type (ID, BUS_TYPE, BUS_TYPE_NAME, CD_SIGN, VALID_SIGN)
SELECT 'b71f0ae740d94ce1a238783f466850b3', 'bs009', '下拨-银行系统自动', '0', '1' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM nis_bus_type WHERE ID = 'b71f0ae740d94ce1a238783f466850b3' LIMIT 1);
##GOGO
INSERT INTO nis_bus_type (ID, BUS_TYPE, BUS_TYPE_NAME, CD_SIGN, VALID_SIGN)
SELECT 'fed53fc6d65d4c50b59593d18ac1ca16', 'bs010', '下拨-人工', '0', '1' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM nis_bus_type WHERE ID = 'fed53fc6d65d4c50b59593d18ac1ca16' LIMIT 1);
##GOGO


##修改人:zhaohc
##修改内容:添加定时任务
##修改时间:2024-05-16
INSERT INTO xxl_job_group (id, app_name, title, address_type, address_list, update_time)
select 17, 'bytter-manager-account', 'accountjob', 0, null, now() from dual
where not exists (select 1 from xxl_job_group where app_name ='bytter-manager-account' limit 1);
##GOGO
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 17, '银行账户利息流水自动认领', now(), now(), 'XXL', '', 'CRON', '0 0 22 1/1 * ?', 'DO_NOTHING', 'FIRST', 'bankAccInterestDtlAutoClaimJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='银行账户利息流水自动认领' limit 1);
##GOGO




