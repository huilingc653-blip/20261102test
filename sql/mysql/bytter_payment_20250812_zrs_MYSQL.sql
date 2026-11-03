
##修改日期：2025-8-12
##修改人员：zrs
##需求单下拨/头寸指令失败提醒定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '需求单下拨/头寸指令失败提醒', null, null, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAllocationNisDemandResultJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='需求单下拨/头寸指令失败提醒' limit 1);
commit;
##GOGO


##增加：头寸调拨失败提醒
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT '079532a3772711f0a25e005056a84efa', '183fc820962c411e9581ce3a7db5f552', '头寸调拨失败提醒', 'netbank:allocation:position:failure', '', '', null, '', null, 10,
       '该笔账户资金需求头寸调拨失败，请及时补款。\n失败原因：$!{reason}\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n需求金额：$!{amt}\n调拨金额：$!{downAmt}', 1,0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category WHERE ID = '079532a3772711f0a25e005056a84efa' LIMIT 1
);

##增加：不紧急需求清单下拨失败提醒
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT '484fb6a0772711f0a25e005056a84efa', '183fc820962c411e9581ce3a7db5f552', '不紧急需求清单下拨失败提醒', 'netbank:allocation:not:urgent:failure', '', '', null, '', null, 10,
       '该笔账户资金需求未下拨/未全部下拨，请及时补款。\n失败原因：$!{reason}\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n需求金额：$!{amt}\n已下拨金额：$!{downAmt}', 1,0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category WHERE ID = '484fb6a0772711f0a25e005056a84efa' LIMIT 1
);

##修改：紧急需求清单下拨失败提醒
update sys_notification_category set CONTENT = '该笔账户资金需求未下拨/未全部下拨，请及时补款。\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n下拨金额：$!{downAmt}\n需求金额：$!{amt}\n差异金额：$!{diffAmt}' ,name='紧急需求清单下拨失败提醒'
where ID = 'fc9d09dc525011f0a25e005056a84efa';

## nis_demand 表增加失败原因

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'FAIL_REASON') THEN
        ALTER TABLE NIS_DEMAND ADD COLUMN FAIL_REASON VARCHAR(500) COMMENT '失败原因';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'POS_STATUS') THEN
        ALTER TABLE NIS_DEMAND ADD COLUMN POS_STATUS VARCHAR(1) COMMENT '调拨状态';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'POS_NUM') THEN
        ALTER TABLE NIS_DEMAND ADD COLUMN POS_NUM int COMMENT '调拨数量';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO
