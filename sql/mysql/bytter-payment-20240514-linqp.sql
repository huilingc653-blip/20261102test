##RPA模板导入SQL
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b696272d060d11efa0be005056a84efa', 'zh_CN', '单据类型为空', 'nis.netbank.message.receiptType.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.netbank.message.receiptType.null' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ca89602d060d11efa0be005056a84efa', 'zh_CN', '交易日期为空', 'nis.netbank.message.transdate.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.netbank.message.transdate.null' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ce301f98060d11efa0be005056a84efa', 'zh_CN', '交易时间为空', 'nis.netbank.message.transdtltime.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.netbank.message.transdtltime.null' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd11cc9e3060d11efa0be005056a84efa', 'zh_CN', '摘要为空', 'nis.netbank.message.remark.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.netbank.message.remark.null' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd3a88b27060d11efa0be005056a84efa', 'zh_CN', '单据类型有误', 'nis.netbank.message.receiptType.format' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.netbank.message.receiptType.format' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd68f7e2c060d11efa0be005056a84efa', 'zh_CN', '导入数据重复', 'nis.netbank.message.isRepeat.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.netbank.message.isRepeat.null' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##单据类型菜单和银行账户组菜单SQL

##单据类型表
CREATE TABLE IF NOT EXISTS FC_RECEIPT_TYPE
(
    ID               varchar(32)      not null comment 'id'
    primary key,
    receipt_code     varchar(32)       not null comment '单据类型编码',
    receipt_name     varchar(128)      not null comment '单据类型名称',
    RMK              varchar(128)      null comment '备注',
    VALID_SIGN       varchar(1)       null comment '有效标志',
    CREATE_DATE      datetime         null,
    CREATE_BY        varchar(32)      null,
    UPDATE_DATE      datetime         null,
    UPDATE_BY        varchar(32)      null
    );
##GOGO

create table  IF NOT EXISTS FC_BANK_ACC_GROUP
(
    ID               varchar(32)      not null comment 'id'
    primary key,
    ACC_GROUP_CODE     varchar(32)       not null comment '账户组编码',
    ACC_GROUP_NAME     varchar(128)      not null comment '账户组名称',
    CREATE_DATE      datetime         null,
    CREATE_BY        varchar(32)      null,
    UPDATE_DATE      datetime         null,
    UPDATE_BY        varchar(32)      null
    );
##GOGO

create table  IF NOT EXISTS FC_BANK_ACC_GROUP_AREA
(
    ID               varchar(32)      not null comment 'id'
    primary key,
    GROUP_ID        varchar(32) not null comment '账户组编码ID',
    AREA_CODE varchar(32)     not null comment '所属营运区CODE',
    CREATE_DATE      datetime         null,
    CREATE_BY        varchar(32)      null,
    UPDATE_DATE      datetime         null,
    UPDATE_BY        varchar(32)      null
    );
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '4408afd806b311efa0be005056a84efa', '', '单据类型维护', '单据类型维护', 'd61f91e27ed9473ab9e8183343ea4507', '', 23, 1, '/netbank/receipttype/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='单据类型维护' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select 'f64e742206b311efa0be005056a84efa', '单据类型维护应用资源', '应用资源', 'netbank:basicdata:receipt:apply', 1, '4408afd806b311efa0be005056a84efa', null, null, null, null, '/netbank/receipttype/save' from dual
where not exists (select 1 from sys_resources where PERMISSION ='netbank:basicdata:receipt:apply' limit 1);
commit;
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '63f38da20b7011efa0be005056a84efa', '', '银行账户组维护', '银行账户组维护', 'd61f91e27ed9473ab9e8183343ea4507', '', 24, 1, '/netbank/bankAccGroup/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='银行账户组维护' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select 'f64e7422061311efa0be005056a84efa', '银行账户组维护应用资源', '银行账户组维护', 'netbank:bankaccgroup:apply', 1, '63f38da20b7011efa0be005056a84efa', null, null, null, null, '/netbank/bankAccGroup/save' from dual
where not exists (select 1 from sys_resources where PERMISSION ='netbank:bankaccgroup:apply' limit 1);
commit;
##GOGO


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '60f32f3f0b7a11efa0be005056a84efa', 'zh_CN', '账户组编码', 'fcBankAccGroup.code' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='fcBankAccGroup.code' limit 1);
commit;
##GOGO


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6442a33a0b7a11efa0be005056a84efa', 'zh_CN', '账户组名称', 'fcBankAccGroup.name' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='fcBankAccGroup.name' limit 1);
commit;
##GOGO


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6b2d3c510b7a11efa0be005056a84efa', 'zh_CN', '所属营运区', 'fcBankAccGroup.operation.area' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='fcBankAccGroup.operation.area' limit 1);
commit;
##GOGO


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '8f79787706b911efa0be005056a84efa', 'zh_CN', '单据类型名称', 'fcReceiptType.name' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='fcReceiptType.name' limit 1);
commit;
##GOGO


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '5d110af706b911efa0be005056a84efa', 'zh_CN', '单据类型编码', 'fcReceiptType.code' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='fcReceiptType.code' limit 1);
commit;
##GOGO


##消息提醒SQL
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'RAT_BKRATION' AND COLUMN_NAME = 'SEND_SIGN_1') THEN
ALTER TABLE RAT_BKRATION ADD COLUMN SEND_SIGN_1  varchar(1);
ELSE
ALTER TABLE RAT_BKRATION MODIFY COLUMN SEND_SIGN_1 varchar(1) ;
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 333, 2, '内部借款到期提醒', '2024-05-09 15:33:55', '2024-05-09 15:33:55', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'CmsLoanSendService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-05-09 15:33:55', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='内部借款到期提醒' limit 1);
commit;
##GOGO

update xxl_job_info set schedule_conf='0 0 9 1/1 * ?' where job_desc='银行授信合同到期提醒';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ad8e97730de011efa0be005056a84efa', 'zh_CN', '内部借款到期提醒提前天数', 'cms.innerLoanExpirationWarn' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='cms.innerLoanExpirationWarn' limit 1);
commit;
##GOGO

INSERT INTO sys_config (ID, TYPE, NAME, VALUE, `CHANGE`)
select '5c95fa4c0de111efa0be005056a84efa', 'cms', 'innerLoanExpirationWarn', '30', null from dual
where not exists (select 1 from sys_config where NAME ='innerLoanExpirationWarn' limit 1);
commit;
##GOGO

update sys_notification_category set CONTENT='业务类型：内部内款；业务描述：借款到期；到期日期：${repayDateStr}；借款主体：${corpName}；借款金额：${loanMoney}' where id='01db637453b9403888b1555522d3da0b';
##GOGO

update sys_notification_category set CONTENT='业务类型：授信管理；业务描述：授信合同到期；合同号：${contractNo}；到期日期：${endDateStr}；授信主体：${corpName}；授信银行：${bankName}！' where id='f14db705242747d898e6aa6d4daff85a';
commit;
##GOGO








