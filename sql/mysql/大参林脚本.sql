###########################################
##票据最新功能同步和批量产品功能提交
##郑刚
###########################################

##修改人:zmc
##修改内容:
##修改时间:2023-11-28
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'AB_VOUCHERINFO_VICE' AND COLUMN_NAME = 'BILL_STATUS') THEN
ALTER TABLE AB_VOUCHERINFO_VICE ADD COLUMN BILL_STATUS VARCHAR(10) comment '线下票据状态，只对线下票据有效，线上票据以主表为准';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人:zmc
##修改内容:
##修改时间:2023-11-28
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'AB_VOUCHERINFO_VICE' AND COLUMN_NAME = 'EBILL_BL_CRCL_INDC') THEN
ALTER TABLE AB_VOUCHERINFO_VICE ADD COLUMN EBILL_BL_CRCL_INDC VARCHAR(10) comment '线下票据流转标识，只对线下票据有效，线上票据以主表为准';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

## 修改人：zhengg
## 修改内容：添加批量拆分
## 修改时间：2023-1204
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'ORG_SBBLL_RNG_STRT_SN') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN ORG_SBBLL_RNG_STRT_SN VARCHAR(12);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'ORG_SBBLL_RNG_END_SN') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN ORG_SBBLL_RNG_END_SN VARCHAR(12);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO_VICE' AND COLUMN_NAME = 'PAY_LOCK_MONEY') THEN
        ALTER TABLE AB_VOUCHERINFO_VICE ADD COLUMN PAY_LOCK_MONEY decimal(15,2);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'ENDORSEMENT_COUNT') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN ENDORSEMENT_COUNT decimal(12,0);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO_VICE' AND COLUMN_NAME = 'OLD_DRAFT_ID') THEN
        ALTER TABLE AB_VOUCHERINFO_VICE ADD COLUMN OLD_DRAFT_ID VARCHAR(32);
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


## 修改人：zhengg
## 修改内容：添加附加信息的内容
## 修改时间：2023-12-25
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'RISK_BILL_TYPE') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN RISK_BILL_TYPE VARCHAR(100);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'DISOUNT_INC_IF') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN DISOUNT_INC_IF VARCHAR(10);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'CASH_INC_IF') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN CASH_INC_IF VARCHAR(10);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'AVOID_CLAIM_DEBTS_IF') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN AVOID_CLAIM_DEBTS_IF VARCHAR(10);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'PAYER_CHANNEL_NAME') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN PAYER_CHANNEL_NAME VARCHAR(200);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'ACCEPTOR_CHANNEL_NAME') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN ACCEPTOR_CHANNEL_NAME VARCHAR(200);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'PAYEE_CHANNEL_NAME') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN PAYEE_CHANNEL_NAME VARCHAR(200);
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

CREATE TABLE IF NOT EXISTS AB_ADD_CREDIT_INFO
(
    id          VARCHAR(32) not null,
    VID          VARCHAR(32) ,
    TYPE_NAME     VARCHAR(100),
    NAME       VARCHAR(200),
    BL_UNN_ID_NO  VARCHAR(60),
    GRA_ADDRESS   VARCHAR(500),
    BILL_ACC_NAME  VARCHAR(200),
    CHANNEL_NAME   VARCHAR(200),
    BILL_ACC_NO VARCHAR(200),
    BANK_NAME    VARCHAR(200),
    BANK_NO         VARCHAR(60),
    BANK_ACC_NO   VARCHAR(200),
    create_time DATETIME,
    constraint AB_ADD_CREDIT_INFO_ID primary key (ID)
);
##GOGO

##修改人：liuhb
##修改内容：新一代票据跨行贴现结算户名取值
##修改时间：2023-12-28
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_DISCOUNT' AND COLUMN_NAME = 'AOACCNINF_NAME') THEN
        ALTER TABLE AB_DISCOUNT ADD COLUMN AOACCNINF_NAME VARCHAR(200);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_ACCRECORDS' AND COLUMN_NAME = 'INSIDE_ENDORSE_AUTO_REG_SIGN') THEN
        ALTER TABLE AB_ACCRECORDS ADD COLUMN INSIDE_ENDORSE_AUTO_REG_SIGN CHAR(1) comment '对内背书线上背给线下自动登记标志：1-已登记 0-未登记';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO



##zmc
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_ACCRECORDS' AND COLUMN_NAME = 'EXC_ID') THEN
        ALTER TABLE AB_ACCRECORDS ADD COLUMN EXC_ID VARCHAR(100) comment '指令id';
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
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_DRAFT_EXC' AND COLUMN_NAME = 'IS_NO_VALUE') THEN
        ALTER TABLE AB_DRAFT_EXC ADD COLUMN IS_NO_VALUE VARCHAR(1) comment '是否确认无此子票信息，1确认无信息，其他为有';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882111', 'zh_CN', '不可拆分票据不允许做子票区间拆分！', 'ab.bill.excel.errormsg.not.split' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.not.split' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882112', 'zh_CN', '开始纸票区间必须小于结束子票区间！', 'ab.bill.excel.errormsg.split.start.be.end' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.split.start.be.end' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882113', 'zh_CN', '持有票据子票必须包含拆分子票区间！', 'ab.bill.excel.errormsg.old.not.include.split' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.old.not.include.split' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882114', 'zh_CN', '拆分金额和子票区间不匹配！', 'ab.bill.excel.errormsg.transmoney.not.sbbllrng' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.transmoney.not.sbbllrng' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882115', 'zh_CN', '拆分子票区间存在交叉！', 'ab.bill.excel.errormsg.split.sbbllrng.rep' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.split.sbbllrng.rep' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882116', 'zh_CN', '交易金额不能大于票面金额！', 'accebill.ticket.money.gre.bill.money.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.ticket.money.gre.bill.money.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882117', 'zh_CN', '子票区间必须重开始进行拆分！', 'accebill.ticket.sbbllrng.not.bigin.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.ticket.sbbllrng.not.bigin.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882118', 'zh_CN', '交易金额不允许超过可拆分金额！', 'accebill.ticket.transmoney.gt.billmoney.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.ticket.transmoney.gt.billmoney.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882119', 'zh_CN', '原电票详细', 'bill.accebill.electronbill.oldinfo' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.accebill.electronbill.oldinfo' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO


##修改人：liuhb
##修改内容：增加国际化
##修改时间：2023-11-29
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1a272a13cdfa468f972e43aae736c00e', 'zh_CN', '入账户名', 'table.abDiscount.aoaccninfIdName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.abDiscount.aoaccninfIdName' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e1ebd5e939df4c32994c46b771f74abf', 'en_US', 'Account name', 'table.abDiscount.aoaccninfIdName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.abDiscount.aoaccninfIdName' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人:zhous
##修改内容:V23090144 添加定时任务 线上账户票据对内背书给线下账户自动登记
##修改时间:2022-10-07
INSERT INTO sys_task (ID, TASKNAME, ACTIVE, DELETEDSW, TYPE, COMMAND, ARGUMENTS, PERIOD, SECOND, MINUTE, HOUR, DAYS, MONTH, WEEK, STARTDTM, ENDDTM, TASKOWNER, EMAIL_REMINDER)
SELECT '61f5b84413574a2382615dbf92646782', '线上账户票据对内背书给线下账户自动登记', 0, 0, null, 'com.bytter.biz.bill.accebill.service.AbBillTaskService', '2023-10-01', 1, '0', '0/1', '*', '*', '*', '?', '2023-10-07', '2050-10-07', null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_task WHERE ID = '61f5b84413574a2382615dbf92646782' LIMIT 1
);
COMMIT;
##GOGO

##修改人：zhengg
##修改内容：票据认领国际化异常完善
##修改时间：2023-10-09
UPDATE sys_multi_lang SET lang_key='bill.accebill.abClaim.transferClaim',lang_context='确认转待认领吗？' WHERE lang_key = 'bill.accebill.transferClaim';
##GOGO

UPDATE sys_multi_lang SET lang_key='bill.accebill.abClaim.confirmClaim',lang_context='确认认领吗？' WHERE lang_key = 'bill.accebill.confirmClaim';
##GOGO

##修改人：zhous
##修改内容：V23090144 【洋河股份】持票账户为线上，背书给内部线下账户后需要收票登记，建议根据票据状态自动更新库存票据信息
##修改时间：2023-10-07
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'bd8907f7278b43bb8873f9e2a95a1805', 'zh_CN', '日期参数未设置或格式异常，应为yyyy-MM-dd格式', 'date.format.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='date.format.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f3d', 'zh_CN', '拆票结果查询', 'bill.button.newBillSplitResultSearch' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.button.newBillSplitResultSearch' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f4d', 'en_US', 'New Bill Split Result Search', 'bill.button.newBillSplitResultSearch' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.button.newBillSplitResultSearch' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f5d', 'zh_CN', '是否需要合并', 'bill.accebill.isneedmerge' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.accebill.isneedmerge' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f6d', 'en_US', 'Is_Need_Merge', 'bill.accebill.isneedmerge' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.accebill.isneedmerge' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f7d', 'zh_CN', '可以根据此列内容判断是否需要合并操作', 'bill.isneedmerge.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.isneedmerge.desc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f8d', 'en_US', 'You can determine whether a merge operation is needed based on the content of this column', 'bill.isneedmerge.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.isneedmerge.desc' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f9d', 'zh_CN', '将新一代票据合并后回退。可根据列表中“是否需要合并”一列的提示判断是否需要此操作', 'bill.mergeReturn.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.mergeReturn.desc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f0f', 'en_US', 'Merge and roll back the new generation of bills. You can determine whether this operation is needed based on the prompt in the "Do you need to merge" column in the list', 'bill.mergeReturn.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.mergeReturn.desc' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f1f', 'zh_CN', '手动发送指令同步银行拆票结果，结果在列表“是否需要合并”一列中展示', 'bill.newBillSplitResultSearch.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.newBillSplitResultSearch.desc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f2f', 'en_US', 'Manually send instructions to synchronize the bank split results, and the results are displayed in the "Do you need to merge" column in the list', 'bill.newBillSplitResultSearch.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.newBillSplitResultSearch.desc' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f3f', 'zh_CN', '新一代票据会拆分后回退。可根据列表中“是否需要合并”一列的提示判断是否需要此操作', 'bill.repulse.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.repulse.desc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f4f', 'en_US', 'The new generation of bills will be split and refunded. You can determine whether this operation is needed based on the prompt in the "Do you need to merge" column in the list', 'bill.repulse.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.repulse.desc' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：
##修改时间：2023-07-19
insert into SYS_DICT_GROUP (ID,TYPE_GROUP_CODE,TYPE_GROUP_NAME)
select '195916229fa84e9a9412b44e0d8d5fd1', 'NEW_BILL_MERGEPROPOSAL', '新票是否需要合并' from dual
where not exists (select 1 from SYS_DICT_GROUP where id = '195916229fa84e9a9412b44e0d8d5fd1' limit 1);
##GOGO

##修改人：zmc
##修改内容：
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd1', '0', '此票据不需合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '1' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd1');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd2', '1', '此业务不需合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '2' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd2');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd3', '2', '此阶段不需合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '3' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd3');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd4', '3', '查询失败，请重发指令查询', '', '195916229fa84e9a9412b44e0d8d5fd1', '4' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd4');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd5', '4', '建议合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '5' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd5');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd6', '5', '不需合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '6' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd6');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd7', '6', '正在同步银行结果', '', '195916229fa84e9a9412b44e0d8d5fd1', '7' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd7');
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2023-07-27
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7ae93de3577c4799b4711557846381d1', 'zh_CN', '流水与单据金额不符，请核实', 'bankacc.message.paymentblending.money.diff' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bankacc.message.paymentblending.money.diff' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2023-07-27
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '25e0aeb4234742009680f60dcde90d2d', 'zh_CN', '回单与流水金额不符，请核实', 'bankacc.message.receiptBlending.money.diff' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bankacc.message.receiptBlending.money.diff' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b81g3f', 'zh_CN', '请注意！第$张票的建议操作是“建议合并”。', 'newbill.operate.prompt_1' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_1' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b82g3f', 'en_US', 'Please pay attention! The recommended action for the $th ticket is "Suggest merger".', 'newbill.operate.prompt_1' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_1' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b83g3f', 'zh_CN', '请注意！第$张票的建议操作是“不需合并”。', 'newbill.operate.prompt_2' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_2' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b84g3f', 'en_US', 'Please pay attention! The recommended action for the $th ticket is "no need to merge".', 'newbill.operate.prompt_2' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_2' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b85g3f', 'zh_CN', '请注意！第$张票拆票结果同步失败。', 'newbill.operate.prompt_3' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_3' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b86g3f', 'en_US', 'Please pay attention! The synchronization of the splitting result of the $th ticket failed.', 'newbill.operate.prompt_3' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_3' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b87g3f', 'zh_CN', '请注意！第$张票拆票正在同步银行结果。', 'newbill.operate.prompt_4' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_4' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b88g3f', 'en_US', 'Please pay attention! The opening of the $th ticket is synchronizing the bank results.', 'newbill.operate.prompt_4' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_4' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人:zhumc
##修改时间:2023-06-29
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

IF not EXISTS (select 1 from SYS_MULTI_LANG where id = 'C807A3B073B949E1BDF8F0450442C940') THEN
    insert into SYS_MULTI_LANG (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
values ('C807A3B073B949E1BDF8F0450442C940', 'en_US', 'Drawer Bank Category', 'table.bill.bankType.payerBankType', null, null, null, null, null, null);
END IF;

IF not EXISTS (select 1 from SYS_MULTI_LANG where id = 'C807A3B073B949E1BDF8F0450442C941') THEN
    insert into SYS_MULTI_LANG (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
values ('C807A3B073B949E1BDF8F0450442C941', 'zh_CN', '出票人银行类别', 'table.bill.bankType.payerBankType', null, null, null, null, null, null);
END IF;

END $$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：zhous
##修改内容：V23080138【深南电路】-票据查询条件优化 增加国际化
##修改时间：2023-08-22

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '66d77a8e6b294d599cee2b86dfb3bbc6', 'zh_CN', '子票区间', 'bill.accebill.sti' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.accebill.sti' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zhous
##修改内容：V23080115 【青岛宜博铜业】增加开票登记快速选择到期日期及同步出票数量功能  增加国际化
##修改时间：2023-08-23

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'fc6f7d50e9b6431d9381642d36626055', 'zh_CN', '3个月', 'table.abVoucherinfo.endDate.threeMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.abVoucherinfo.endDate.threeMonth' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3bd458fc88ed4d3fad70544fd2301280', 'zh_CN', '6个月', 'table.abVoucherinfo.endDate.sixMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.abVoucherinfo.endDate.sixMonth' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '67f15ec6638b4533b0f28490ee0fcfda', 'zh_CN', '请先选择出票日期！', 'accebill.message.startDate.isnotselect' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.message.startDate.isnotselect' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##完善应收票据台账针对入池票据做为质押出去 zhengg 20230829
INSERT INTO ab_brief_status (ID, STATUS, STATUSNAME, BUS_TYPE)
select 103, '8018', '入池申请', '018' from dual
where not exists (select 1 from ab_brief_status where STATUS ='8018' limit 1);
##GOGO


##修改人：qijh
##修改时间：2024.04.24
##修改原因：大参林项目脚本

##新增参数:未产生资金需求付款单付款控制
INSERT INTO SYS_CONFIG (ID, TYPE, NAME, VALUE)
SELECT '4a48b9c72def4b768f082beccc364ace', 'nis', 'demandControl', '1'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM SYS_CONFIG WHERE ID = '4a48b9c72def4b768f082beccc364ace' LIMIT 1
);
##GOGO

###########################################
##新增资金需求清单
###########################################

##新增“资金需求清单”
CREATE TABLE IF NOT EXISTS NIS_DEMAND (
    ID                VARCHAR(32) not null,
    BILL_NO           varchar(32),
    CORP_ID           varchar(32),
    BANK_ACC          varchar(50),
    BANK_ACC_ID       varchar(32),
    ACC_NAME          varchar(150),
    BANK_NAME         varchar(150),
    DEMAND_TYPE       varchar(10),
    DEMAND_DATE       datetime,
    DEMAND_MONEY      decimal(15,2),
    DEMAND_ALLOCATION_MONEY decimal(15,2),
    PAY_BILL_ID       varchar(32),
    PAY_BILL_NO       varchar(32),
    PAY_BILL_DATE     datetime,
    SUPER_CORP_ID     varchar(32),
    SUPER_BANK_ACC    varchar(32),
    SUPER_BANK_ACC_ID varchar(32),
    SUPER_ACC_NAME    varchar(150),
    SUPER_BANK_NAME   varchar(150),
    STATUS            decimal(10,0),
    ALLOCATION_NO     varchar(32),
    ALLOCATION_ID     varchar(32),
    ALLOCATION_DATE   datetime,
    IS_SUMMARY        varchar(10) default 0,
    CREATE_BY         VARCHAR(64),
    CREATE_DATE       DATETIME(6),
    UPDATE_BY         VARCHAR(64),
    UPDATE_DATE       DATETIME(6),
   constraint PK_NIS_DEMAND primary key (ID)
) COMMENT '资金需求清单';
##GOGO

##新增菜单“结算管理》资金调拨》资金需求清单”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT '2a9b7b7435494fa29bdbd174e7121905', '', '资金需求清单', '资金需求清单', '2a164d43b1d042bdbd8eabe715fd72e4', null, 3, 1, '/netbank/nisdemand/list', null, null, null, null, null, null, 0 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '2a9b7b7435494fa29bdbd174e7121905' LIMIT 1
);
##GOGO

##新增菜单资源 查询资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'abdaf7d01cc64b8694e6622cedd3984e', '查询资源', '查询资源', 'netbank:nisDemand:query', 1, '2a9b7b7435494fa29bdbd174e7121905' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'abdaf7d01cc64b8694e6622cedd3984e' LIMIT 1
);
##GOGO

##新增菜单资源 查询资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'b1324160c61f478dac40a5bfee050803', '申请资源', '申请资源', 'netbank:nisDemand:apply', 1, '2a9b7b7435494fa29bdbd174e7121905' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'b1324160c61f478dac40a5bfee050803' LIMIT 1
);
##GOGO

## 新增字典 nisDemand.demandType
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '32d5e07c4efd4d8b9c14c3a3a1f7d77f', 'nisDemand.demandType', '资金需求类型' from dual
where not exists (select 1 from sys_dict_group where ID ='32d5e07c4efd4d8b9c14c3a3a1f7d77f' limit 1);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '453dee4a74494abebf3166785dfcfab5', '01', '付款',null,'32d5e07c4efd4d8b9c14c3a3a1f7d77f',1  from dual
where not exists (select 1 from sys_dict where ID ='453dee4a74494abebf3166785dfcfab5' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '0748030eb71d4d20818173bdda87283a', '02', '承兑到期',null,'32d5e07c4efd4d8b9c14c3a3a1f7d77f',2  from dual
where not exists (select 1 from sys_dict where ID ='0748030eb71d4d20818173bdda87283a' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '17d5e8950b8a40968a3113c82222287f', '03', '融资到期',null,'32d5e07c4efd4d8b9c14c3a3a1f7d77f',3  from dual
where not exists (select 1 from sys_dict where ID ='17d5e8950b8a40968a3113c82222287f' limit 1);
##GOGO

## 新增字典 nisDemand.status
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '684df687d537429ca8c478f5dff41105', 'nisDemand.status', '资金需求状态' from dual
where not exists (select 1 from sys_dict_group where ID ='684df687d537429ca8c478f5dff41105' limit 1);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '28ff42c646534d0a8d3cb8044be33244', '0', '暂存',null,'684df687d537429ca8c478f5dff41105',1  from dual
where not exists (select 1 from sys_dict where ID ='28ff42c646534d0a8d3cb8044be33244' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'd6bd91814f084060b125383f86137934', '-1', '打回',null,'684df687d537429ca8c478f5dff41105',2  from dual
where not exists (select 1 from sys_dict where ID ='d6bd91814f084060b125383f86137934' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '961de35fd4bc4e9bb84ad38abe040092', '-2', '作废',null,'684df687d537429ca8c478f5dff41105',3  from dual
where not exists (select 1 from sys_dict where ID ='961de35fd4bc4e9bb84ad38abe040092' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '07296442da2244099848b4316d233928', '1', '已确认',null,'684df687d537429ca8c478f5dff41105',4  from dual
where not exists (select 1 from sys_dict where ID ='07296442da2244099848b4316d233928' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '13c6114a41914777a268066f0cf6ba0b', '2', '下拨中',null,'684df687d537429ca8c478f5dff41105',5  from dual
where not exists (select 1 from sys_dict where ID ='13c6114a41914777a268066f0cf6ba0b' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '34d0ad61f4a34b74919622e5a1df3cce', '3', '已下拨',null,'684df687d537429ca8c478f5dff41105',6  from dual
where not exists (select 1 from sys_dict where ID ='34d0ad61f4a34b74919622e5a1df3cce' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '2b0615b80858479694925631d7bcb73d', '4', '下拨失败',null,'684df687d537429ca8c478f5dff41105',7  from dual
where not exists (select 1 from sys_dict where ID ='2b0615b80858479694925631d7bcb73d' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '8d8de5dfb26248ddbd0eb0f012b0e2bc', '4', '下拨状态未确认',null,'684df687d537429ca8c478f5dff41105',8  from dual
where not exists (select 1 from sys_dict where ID ='8d8de5dfb26248ddbd0eb0f012b0e2bc' limit 1);
##GOGO

###########################################
##添加利息流水规则管理
###########################################
##修改人:zhaohc
##修改内容:添加利息流水规则表
##修改时间:2024-05-07

CREATE TABLE IF NOT EXISTS `fc_interest_dtl_rule`
(
    id           varchar(32)  not null comment '主键ID' primary key,
    bank_type_id varchar(32)  null comment '银行类别',
    bank_name    varchar(150) null comment '银行名称',
    remark       varchar(150) null comment '用途/摘要',
    trans_time   datetime     null comment '入账日期',
    valid_sign   varchar(1)   null comment '有效标志',
    create_date  datetime     null comment '创建时间',
    create_by    varchar(32)  null comment '创建人',
    update_date  datetime     null comment '修改时间',
    update_by    varchar(32)  null comment '修改人',
    KEY `idx_user_id` (`bank_type_id`) USING BTREE
) collate = utf8mb3_bin
  row_format = DYNAMIC COMMENT ='利息流水规则表';

##GOGO




###########################################
##资金需求未产生下拨指令消息提醒
###########################################
##修改人：qijh
##修改时间：2024.04.24
##修改原因：大参林项目脚本

## 需求单表增加级别字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'CORP_LEVEL') THEN
      ALTER TABLE NIS_DEMAND ADD COLUMN CORP_LEVEL decimal(10,0);
ELSE
    ALTER TABLE NIS_DEMAND MODIFY COLUMN CORP_LEVEL decimal(10,0);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

## 需求单表增加上级Id
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'SUPER_ID') THEN
      ALTER TABLE NIS_DEMAND ADD COLUMN SUPER_ID varchar(32);
ELSE
    ALTER TABLE NIS_DEMAND MODIFY COLUMN SUPER_ID varchar(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##付款指令来源 增加资金需求下拨
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '52fa32ada6b2437bbe5d626b755890ca', '12', '资金需求下拨',null,'7a3aeef2c27c4dec909351ac815f81ff',12  from dual
where not exists (select 1 from sys_dict where ID ='52fa32ada6b2437bbe5d626b755890ca' limit 1);
##GOGO

##资金需求未产生下拨指令消息提醒
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT 'cf8756d4dda943e08d36e85679be0dab', '183fc820962c411e9581ce3a7db5f552', '资金需求未产生下拨指令消息提醒', 'netbank:allocation:nisDemand:notBisExc', '2a9b7b7435494fa29bdbd174e7121905', '', null, 'netbank:nisDemand:apply', 0, 6, '存在未产生下拨指令的资金需求（$!{count}）', 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = 'cf8756d4dda943e08d36e85679be0dab' LIMIT 1
);
##GOGO

###########################################
##自动产生下拨指令、自动生产上级账户需求权限资源控制
###########################################

##修改人：qijh
##修改时间：2024.05.13
##修改原因：大参林项目脚本

##新增菜单资源 生成上级需求单资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '6feada8fc33f4481baa6089a69be2ebc', '生成上级需求单资源', '生成上级需求单资源', 'netbank:nisDemand:super', 1, '2a9b7b7435494fa29bdbd174e7121905' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '6feada8fc33f4481baa6089a69be2ebc' LIMIT 1
);
##GOGO

##新增菜单资源 生成上级需求单资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'd96e66588d5646319e7dfcf69c7be68f', '生成下拨指令资源', '生成下拨指令资源', 'netbank:nisDemand:allocation', 1, '2a9b7b7435494fa29bdbd174e7121905' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'd96e66588d5646319e7dfcf69c7be68f' LIMIT 1
);
##GOGO


###########################################
##付款办理增强-应收票据批量背书
###########################################

##修改人:zhaohc
##修改内容:付款办理增强-应收票据批量背书
##修改时间:2024-05-09

##新增菜单资源 查询资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '647113a1cae74c4aa2d875b9b7817688', '应收选票资源', '应收选票资源', 'netbank:netBank:nisBillHead:batchSelectAccebill', 1, '4028802f5c2eac34015c2ec1b9c40036' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_resources WHERE ID = '647113a1cae74c4aa2d875b9b7817688' LIMIT 1);
##GOGO

##国际化数据
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a28bdfd5402840908d5772ef3884b9ca', 'zh_CN', '应收选票', 'netbank.button.batchSelectAccebill' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.button.batchSelectAccebill' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a7a52ea12b9245bfac757c51dcc28e97', 'zh_CN', '应收选票,系统结算方式必须是应收票据', 'nis.selectaccebill.message.vouchertype' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.selectaccebill.message.vouchertype' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c63697aa97744e1784ad1d3f52969250', 'zh_CN', '应收选票,付款单位需一致', 'nis.selectaccebill.message.corp.same' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.selectaccebill.message.corp.same' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd2c7ab00b785410f98c330acca264bd6', 'zh_CN', '应收选票,是否已付款必须为否', 'nis.selectaccebill.message.ispayed' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.selectaccebill.message.ispayed' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0788b171f2cc47599044a85d64622256', 'zh_CN', '是否已选票', 'table.nisBillhead.isSelectedAcceBill' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.nisBillhead.isSelectedAcceBill' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO


###########################################
##修改sql脚本
##邹雄枢
###########################################

##新增菜单“基础平台>>数据管理>>营运区维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT 'cba9942728054ebeada976f043343f8b', null, '营运区维护', '营运区维护', '833a203b6d2c42bba154e79a11b0e2f6', null, 90, 1, '/sys/operationArea/list', null, null, null, null, null, null, 0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'cba9942728054ebeada976f043343f8b' LIMIT 1
    );
##GOGO

##新增菜单“基础平台>>数据管理>>股权维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT 'eaad708ac22b4994b1f7442000f05256', null, '股权维护', '股权维护', '833a203b6d2c42bba154e79a11b0e2f6', null, 100, 1, '/sys/equityMaintenance/list', null, null, null, null, null, null, 0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'eaad708ac22b4994b1f7442000f05256' LIMIT 1
    );
##GOGO

##新增菜单“基础平台>>数据管理>>单位仓库维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT 'cd3ca6b3c2d847c4aff434005b9d0216', null, '单位仓库维护', '单位仓库维护', '833a203b6d2c42bba154e79a11b0e2f6', null, 500,1, '/sys/unitStore/list', null, null, null, null, null, null, 0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'cd3ca6b3c2d847c4aff434005b9d0216' LIMIT 1
    );
##GOGO

CREATE TABLE IF NOT EXISTS SYS_OPERATION_AREA
(
    ID  VARCHAR(32) COMMENT '主键',
    OPERATION_AREA_CODE  VARCHAR(64) COMMENT '营运区编码',
    OPERATION_AREA_NAME  VARCHAR(128) COMMENT '营运区名称',
    STATUS VARCHAR(2) DEFAULT '1' NOT NULL COMMENT '状态:1-正常;0-删除',
    CREATE_TIME DATETIME NULL DEFAULT NULL,
    CREATE_BY VARCHAR(32) NULL DEFAULT NULL,
    UPDATE_TIME DATETIME NULL DEFAULT NULL,
    UPDATE_BY VARCHAR(32) NULL DEFAULT NULL,
    CONSTRAINT SYS_OPERATION_AREA PRIMARY KEY (ID)
    ) COMMENT '营运区维护';
##GOGO


CREATE TABLE IF NOT EXISTS SYS_EQUITY_MAINTENANCE
(
    ID  VARCHAR(32) COMMENT '主键',
    Equity_code VARCHAR(64) COMMENT '股权编码',
    Equity_NAME VARCHAR(128) COMMENT '股权名称',
    STATUS VARCHAR(2) DEFAULT '1' NOT NULL COMMENT '状态:1-正常;0-删除',
    CREATE_TIME DATETIME NULL DEFAULT NULL,
    CREATE_BY VARCHAR(32) NULL DEFAULT NULL,
    UPDATE_TIME DATETIME NULL DEFAULT NULL,
    UPDATE_BY VARCHAR(32) NULL DEFAULT NULL,
    CONSTRAINT SYS_EQUITY_MAINTENANCE PRIMARY KEY (ID)
    )COMMENT '股权维护';
##GOGO


CREATE TABLE IF NOT EXISTS SYS_UNIT_STORE_MAINTENANCE
(
    id          varchar(32) comment '主键',
    STORE_CODE VARCHAR(64) COMMENT '仓库编码',
    STORE_NAME VARCHAR(128) COMMENT '仓库名称',
    CORP_ID VARCHAR(32) CHARACTER SET UTF8MB3 COLLATE UTF8MB3_BIN NOT NULL COMMENT '单位ID',
    CORP_CODE VARCHAR(32) CHARACTER SET UTF8MB3 COLLATE UTF8MB3_BIN DEFAULT NULL COMMENT '单位编码',
    CORP_NAME VARCHAR(200) CHARACTER SET UTF8MB3 COLLATE UTF8MB3_BIN DEFAULT NULL COMMENT '单位名称',
    STATUS VARCHAR(2) DEFAULT '1' NOT NULL COMMENT '状态:1-正常;0-删除',
    CREATE_TIME DATETIME NULL DEFAULT NULL,
    CREATE_BY VARCHAR(32) NULL DEFAULT NULL,
    UPDATE_TIME DATETIME NULL DEFAULT NULL,
    UPDATE_BY VARCHAR(32) NULL DEFAULT NULL,
    CONSTRAINT SYS_EQUITY_MAINTENANCE PRIMARY KEY (ID)
    )COMMENT '单位仓库维护';
##GOGO



##增加字段语句 operationArea_ID
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
      IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
      DATABASE() AND TABLE_NAME = 'sys_corp' AND COLUMN_NAME = 'operationArea_ID') THEN
ALTER TABLE sys_corp ADD COLUMN operationArea_ID VARCHAR(1024) DEFAULT NULL COMMENT '营运区维护编码';
ELSE
ALTER TABLE sys_corp MODIFY COLUMN operationArea_ID VARCHAR(1024) DEFAULT NULL COMMENT '营运区维护编码';
END IF;
   END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##增加字段语句 equityMaintenance_ID
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
      IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
      DATABASE() AND TABLE_NAME = 'sys_corp' AND COLUMN_NAME = 'equityMaintenance_ID') THEN
ALTER TABLE sys_corp ADD COLUMN equityMaintenance_ID VARCHAR(32) DEFAULT NULL COMMENT '股权维护Id';
ELSE
ALTER TABLE sys_corp MODIFY COLUMN equityMaintenance_ID VARCHAR(32) DEFAULT NULL COMMENT '股权维护Id';
END IF;
   END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##增加字段语句 equityMaintenance_ID
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
      IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
      DATABASE() AND TABLE_NAME = 'sys_corp' AND COLUMN_NAME = 'unitStoreMaintenance_ID') THEN
ALTER TABLE sys_corp ADD COLUMN unitStoreMaintenance_ID VARCHAR(1024) DEFAULT NULL COMMENT '单位仓库维护Id';
ELSE
ALTER TABLE sys_corp MODIFY COLUMN unitStoreMaintenance_ID VARCHAR(1024) DEFAULT NULL COMMENT '单位仓库维护Id';
END IF;
   END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO



###########################################
##提交SQL
##林钦鹏
###########################################

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



###########################################
##添加XXL定时任务脚本
##赵红超
###########################################
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



##新增银企上线占比表菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '42a34d180bad4cd782c633b33084602b', '', '银企上线占比表', '银企上线占比表', '07cbbb8132de427eb53c9d624fb7ee22', '', 4, 1, '/report/enterpriseBankGoOnline/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='银企上线占比表' limit 1);
commit;
##GOGO


##新增资金收支表菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '4e48bc81fa754c40874571474390404f', '', '资金收支表', '资金收支表', '07cbbb8132de427eb53c9d624fb7ee22', '', 500, 1, '/report/revExp/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='资金收支表' limit 1);
commit;
##GOGO



###########################################
##补全银行借款申请新增界面缺少还款账号选择
##周荣生
###########################################
##修改人：zhours
##修改时间：2024.05.16
##修改原因：银行借款表增加字段

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_PROVIDE_LOAN_INFO' AND COLUMN_NAME = 'ONLINE_PAYMENT_ACC') THEN
alter table CMS_PROVIDE_LOAN_INFO add column ONLINE_PAYMENT_ACC varchar(60) ;
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
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_PROVIDE_LOAN_INFO' AND COLUMN_NAME = 'ONLINE_PAYMENT_ACC_ID') THEN
alter table CMS_PROVIDE_LOAN_INFO add column ONLINE_PAYMENT_ACC_ID varchar(32) ;
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
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_PROVIDE_LOAN_INFO' AND COLUMN_NAME = 'ONLINE_ACC_NAME') THEN
alter table CMS_PROVIDE_LOAN_INFO add column ONLINE_ACC_NAME varchar(140) ;
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
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_PROVIDE_LOAN_INFO' AND COLUMN_NAME = 'ONLINE_BANK_NAME') THEN
alter table CMS_PROVIDE_LOAN_INFO add column ONLINE_BANK_NAME varchar(150) ;
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改原因:添加 还款账号名称 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '还款账号名称', 'table.cmsPrecativeLoanRepay.repayAccName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.cmsPrecativeLoanRepay.repayAccName' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改原因:添加 还款开户行名称 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '还款开户行名称', 'table.cmsPrecativeLoanRepay.repayAcc.bankName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.cmsPrecativeLoanRepay.repayAcc.bankName' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改原因: 放款表增加字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_PROVIDE_LOAN_SEND' AND COLUMN_NAME = 'ONLINE_PAYMENT_ACC') THEN
alter table CMS_PROVIDE_LOAN_SEND add column ONLINE_PAYMENT_ACC varchar(60) ;
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
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_PROVIDE_LOAN_SEND' AND COLUMN_NAME = 'ONLINE_PAYMENT_ACC_ID') THEN
alter table CMS_PROVIDE_LOAN_SEND add column ONLINE_PAYMENT_ACC_ID varchar(32) ;
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
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_PROVIDE_LOAN_SEND' AND COLUMN_NAME = 'ONLINE_ACC_NAME') THEN
alter table CMS_PROVIDE_LOAN_SEND add column ONLINE_ACC_NAME varchar(140) ;
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
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_PROVIDE_LOAN_SEND' AND COLUMN_NAME = 'ONLINE_BANK_NAME') THEN
alter table CMS_PROVIDE_LOAN_SEND add column ONLINE_BANK_NAME varchar(150) ;
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


###########################################
##增加脚本
##罗永佳
###########################################
## Mysql
## 修改内容:增加消息记录表
## 修改时间:2024-5-20

CREATE TABLE IF NOT EXISTS `PAYMENT_PRODUCER_RECORD` (
    MSG_ID VARCHAR(36) NOT NULL COMMENT '消息ID',
    BUSINESS_TYPE VARCHAR(100) NOT NULL COMMENT '业务类型',
    BUSINESS_ID VARCHAR(100) NOT NULL COMMENT '业务ID',
    EXCHANGE_NAME VARCHAR(100) NOT NULL COMMENT '交换机名称',
    ROUTING_KEY VARCHAR(100) NOT NULL COMMENT '路由key',
    MESSAGE TEXT NOT NULL COMMENT '消息内容',
    SEND_STATUS VARCHAR(100) NOT NULL COMMENT '消息投递状态',
    CREATE_TIME TIMESTAMP COMMENT '创建时间',
    CREATE_BY VARCHAR(36) COMMENT '创建用户ID',
    UPDATE_TIME TIMESTAMP COMMENT '修改时间',
    UPDATE_BY VARCHAR(36) COMMENT '修改用户ID',
    PRIMARY KEY(MSG_ID)
) COLLATE = utf8mb3_bin COMMENT ='生产者消息记录表(结算发送消息给核算)';
##GOGO

CREATE TABLE IF NOT EXISTS `PAYMENT_CONSUMER_RECORD` (
    MSG_ID VARCHAR(36) NOT NULL COMMENT '消息ID',
    BUSINESS_TYPE VARCHAR(100) NOT NULL COMMENT '业务类型',
    BUSINESS_ID VARCHAR(100) NOT NULL COMMENT '业务ID',
    MESSAGE TEXT NOT NULL COMMENT '消息内容',
    MSG_HANDLE_STATUS VARCHAR(100) NOT NULL COMMENT '消息处理状态',
    EXCEPTION_MSG TEXT DEFAULT NULL COMMENT '异常日志',
    CREATE_TIME TIMESTAMP COMMENT '创建时间',
    UPDATE_TIME TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY(MSG_ID)
) COLLATE = utf8mb3_bin COMMENT ='消费者消息记录表(监听核算推送给结算的消息)';
##GOGO

###########################################
##增加脚本1
##罗永佳
###########################################
## Mysql
## 修改内容:增加消息记录表
## 修改时间:2024-5-20
CREATE TABLE IF NOT EXISTS `FINACIAL_PRODUCER_RECORD` (
    MSG_ID VARCHAR(36) NOT NULL COMMENT '消息ID',
    BUSINESS_TYPE VARCHAR(100) NOT NULL COMMENT '业务类型',
    BUSINESS_ID VARCHAR(100) NOT NULL COMMENT '业务ID',
    EXCHANGE_NAME VARCHAR(100) NOT NULL COMMENT '交换机名称',
    ROUTING_KEY VARCHAR(100) NOT NULL COMMENT '路由key',
    MESSAGE TEXT NOT NULL COMMENT '消息内容',
    SEND_STATUS VARCHAR(100) NOT NULL COMMENT '消息投递状态',
    CREATE_TIME TIMESTAMP COMMENT '创建时间',
    CREATE_BY VARCHAR(36) COMMENT '创建用户ID',
    UPDATE_TIME TIMESTAMP COMMENT '修改时间',
    UPDATE_BY VARCHAR(36) COMMENT '修改用户ID',
    PRIMARY KEY(MSG_ID)
) COLLATE = utf8mb3_bin COMMENT ='生产者消息记录表(核算发送消息给结算)';
##GOGO

CREATE TABLE IF NOT EXISTS `FINACIAL_CONSUMER_RECORD` (
    MSG_ID VARCHAR(36) NOT NULL COMMENT '消息ID',
    BUSINESS_TYPE VARCHAR(100) NOT NULL COMMENT '业务类型',
    BUSINESS_ID VARCHAR(100) NOT NULL COMMENT '业务ID',
    MESSAGE TEXT NOT NULL COMMENT '消息内容',
    MSG_HANDLE_STATUS VARCHAR(100) NOT NULL COMMENT '消息处理状态',
    EXCEPTION_MSG TEXT DEFAULT NULL COMMENT '异常日志',
    CREATE_TIME TIMESTAMP COMMENT '创建时间',
    UPDATE_TIME TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY(MSG_ID)
) COLLATE = utf8mb3_bin COMMENT ='消费者消息记录表(监听结算推送给核算的消息)';
##GOGO



###########################################
##结算管理付款办理界面新增暂挂和解除暂挂功能
##祁继红
###########################################

##修改人：qijh
##修改时间：2024.05.17
##修改原因：大参林项目脚本

## 新增字典 nisBillhead.status
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '5afc233d1f944589ad2220d829dcbe37', '-3', '暂挂',null,'4028802f5c808b3b015c808ecf020007',15  from dual
where not exists (select 1 from sys_dict where ID ='5afc233d1f944589ad2220d829dcbe37' limit 1);
##GOGO


###########################################
##脚本报错
##朱茂才
###########################################
## zmc 票据到期提醒

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'sys_notification_category' and COLUMN_NAME in ('CONTENT')) THEN
ALTER TABLE sys_notification_category MODIFY CONTENT VARCHAR(500) comment '消息模版内容';
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

    IF not EXISTS (SELECT 1 FROM sys_notification_category WHERE id = '5ff2fe20e7f248bzmc488f6eb312f902') THEN
        INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL,
                                               RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE, STAR_TARGET)
        VALUES ('5ff2fe20e7f248bzmc488f6eb312f902', '5ff2fe20e7f248b093488f6eb312f9a5', '到期票据汇总提醒', 'bill:accebill:emitbillpaySummary',
                '15dfb2276b9e4a7dbd01024c709c2873', '', null,
                'bill:accebill:abemitbillpay:query', 0, 4,
                '单位:$!{corpName}
        公司类型：$!{companyTYpeName}
        银行账号:$!{bankAcc}
        账户名:$!{reqNm}
        开户行:$!{bankName}
        当前余额:$!{bankAccBalance}
        应付票据总笔数：$!{billNums}笔
        票面汇总金额：$!{billMoney}
        票据到期解付汇总金额：$!{releasePaymentMoney}
        到期日期：$!{endDateStr}',
                1, null);
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

    IF not EXISTS (SELECT 1 FROM sys_notification_category WHERE id = '5ff2fe20e7f248bzmc488f6eb312f903') THEN
        INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE, STAR_TARGET) VALUES ('5ff2fe20e7f248bzmc488f6eb312f903', '5ff2fe20e7f248b093488f6eb312f9a5', '承兑账户资金余额不足承兑扣款消息提醒', 'bill:accebill:emitbillpaybalance', '15dfb2276b9e4a7dbd01024c709c2873', '', null, 'bill:accebill:abemitbillpay:query', 0, 4, '单位:$!{corpName}
公司类型：$!{companyTYpeName}
银行账号:$!{bankAcc}
账户名:$!{reqNm}
开户行:$!{bankName}
当前账户可用余额:$!{bankAccBalance}
当前开票的银行账户余额不够兑付
应付票据总笔数：$!{billNums}笔
票面汇总金额：$!{billMoney}
票据到期解付汇总金额：$!{releasePaymentMoney}
到期日期：$!{endDateStr}
需下拨金额：$!{xbMoney}', 1, null);
END IF;


END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


## zmc 票据到期提醒

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('risk_contract_number')) THEN
ALTER TABLE ab_voucherinfo ADD risk_contract_number VARCHAR(500) comment '风险合同编号';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

## zmc 票据到期提醒

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('cvm_limit_no')) THEN
ALTER TABLE ab_voucherinfo ADD cvm_limit_no VARCHAR(500) comment 'CVM额度编号';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

## zmc 票据到期提醒

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('accptncagrmt_branch')) THEN
ALTER TABLE ab_voucherinfo ADD accptncagrmt_branch VARCHAR(500) comment '银承合作协议分行';
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
    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('accptncagrmt_branch_no')) THEN
ALTER TABLE ab_voucherinfo ADD accptncagrmt_branch_no VARCHAR(50) comment '银承合作协议分行行号';
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
    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('CMB_BNKBILNEG_ID')) THEN
ALTER TABLE ab_voucherinfo ADD CMB_BNKBILNEG_ID VARCHAR(50) comment '招行合作框架协议表id';
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
    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('CMB_CRLCON_ID')) THEN
ALTER TABLE ab_voucherinfo ADD CMB_CRLCON_ID VARCHAR(50) comment '招行风险合同信息表id';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##新增人:方杨标
##新增时间:2024-04-22
##新增内容: 添加
CREATE TABLE IF NOT EXISTS CMB_CRLINFO
(
    ID             VARCHAR(32)  COMMENT '主键ID',
    CREDITLINEID   VARCHAR(100) COMMENT 'CVM额度编号',
    CONTRACTTEXTNO VARCHAR(100) COMMENT '文本协议编号',
    CREDITLINETYPE VARCHAR(60)  COMMENT '授信类型',
    CRLNUM         VARCHAR(100) COMMENT '信贷额度编号',
    CREATE_DATE    DATETIME     COMMENT '创建时间',
    UPDATE_DATE    DATETIME     COMMENT '更新时间',
    CONSTRAINT PK_CMB_CRLINFO_ID PRIMARY KEY (ID),
    constraint AK_CREDITLINEID unique (creditLineId)
    ) COMMENT ='招行协议文本信息表';
##GOGO


##新增人:方杨标
##新增时间:2024-04-22
##新增内容: 添加招行合作框架协议表
CREATE TABLE IF NOT EXISTS CMB_BNKBILNEG
(
    ID      VARCHAR(32)  COMMENT '主键ID',
    TEXTNO  VARCHAR(100) COMMENT '银承合作协议编号',
    ORGNAME VARCHAR(100) COMMENT '银承合作协议分行名称',
    BRNNBR  VARCHAR(60)  COMMENT '银承合作协议分行号',
    CREATE_DATE    DATETIME     COMMENT '创建时间',
    UPDATE_DATE    DATETIME     COMMENT '更新时间',
    CONSTRAINT PK_CMB_BNKBILNEG PRIMARY KEY (ID),
    constraint AK_TEXTNO unique (TEXTNO)
    ) COMMENT ='招行合作框架协议表';
##GOGO


##新增人:方杨标
##新增时间:2024-04-22
##新增内容: 添加招行风险合同信息表
CREATE TABLE IF NOT EXISTS CMB_CRLCON
(
    ID      VARCHAR(32)  COMMENT '主键ID',
    ACRCON  VARCHAR(100) COMMENT '风险合同编号',
    CONNUM  VARCHAR(100) COMMENT '信贷合同编号',
    CONNAM  VARCHAR(200) COMMENT '合同文本号',
    GRTTYP  VARCHAR(60)  COMMENT '担保物类型',
    CONCCY  VARCHAR(30)  COMMENT '合同币种',
    CONBRN  VARCHAR(100) COMMENT '合同机构号',
    BUSTYP  VARCHAR(60)  COMMENT '业务品种',
    GRTCCY  VARCHAR(30)  COMMENT '担保物币种',
    ZBRDNAM VARCHAR(600) COMMENT '合同机构行名',
    ZCONBRD VARCHAR(600) COMMENT '合同机构行号',
    GRTAVE  DECIMAL(17, 2) COMMENT '担保物可用金额',
    CONAMT  DECIMAL(17, 2) COMMENT '合同金额',
    CONBAL  DECIMAL(17, 2) COMMENT '合同业务余额',
    CONAVE  DECIMAL(17, 2) COMMENT '合同可用额度',
    GRTAMT  DECIMAL(17, 2) COMMENT '担保物登记金额',
    GRTPER  DECIMAL(17, 2) COMMENT '合同保证金比例',
    CREATE_DATE    DATETIME     COMMENT '创建时间',
    UPDATE_DATE    DATETIME     COMMENT '更新时间',
    CREDITLINEID   VARCHAR(100) COMMENT 'CVM额度编号',
    CONSTRAINT PK_CMB_CRLCON PRIMARY KEY (ID),
    constraint AK_ACRCON_CMB_CRLCON unique (ACRCON)
    ) COMMENT ='招行风险合同信息表';
##GOGO


###########################################
##新增加资金收支表菜单以及界面
##周鹏
###########################################
##修改人：zhoup
##修改时间：2024.05.06
##修改原因：添加单据类型和账户组两个字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'RECEIPT_ID') THEN
      alter table BT_BANK_ACC add column RECEIPT_ID varchar(32);
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
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'FC_ACC_GROUP_CODE') THEN
      alter table BT_BANK_ACC add column FC_ACC_GROUP_CODE varchar(200);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##修改原因:添加 是否直连银行 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ae5d22b9f7eb452f80376623a53acb16', 'zh_CN', '是否直连银行', 'table.whether.connect.bank' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.whether.connect.bank' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改原因:添加 是否直连银行 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_TYPE' AND COLUMN_NAME = 'WHETHER_CONNECT_BANK') THEN
      alter table BT_BANK_TYPE add column WHETHER_CONNECT_BANK varchar(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##新增银企上线占比表菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '42a34d180bad4cd782c633b33084602b', '', '银企上线占比表', '银企上线占比表', '07cbbb8132de427eb53c9d624fb7ee22', '', 4, 1, '/report/enterpriseBankGoOnline/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='银企上线占比表' limit 1);
commit;
##GOGO


##新增资金收支表菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '4e48bc81fa754c40874571474390404f', '', '资金收支表', '资金收支表', '07cbbb8132de427eb53c9d624fb7ee22', '', 500, 1, '/report/incomeAndExpenditureOfFunds/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='资金收支表' limit 1);
commit;
##GOGO




###########################################
##添加SSO单点鉴权开发
##赵红超
###########################################
##修改人:zhaohc
##修改内容:添加用户表字段
##修改时间:2024-05-16

##增加字段语句 employee_Id
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_user' AND COLUMN_NAME = 'EMPLOYEE_ID') THEN
        ALTER TABLE sys_user ADD COLUMN EMPLOYEE_ID VARCHAR(32) DEFAULT NULL COMMENT '第三方平台员工编号';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##调拨策略维护新增字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BIS_GATHER_POLICY' AND COLUMN_NAME = 'END_DATE') THEN
	  ALTER TABLE BIS_GATHER_POLICY add COLUMN END_DATE DATETIME  COMMENT '策略结束日期（yyyy/MM/dd）';
ELSE
	ALTER TABLE BIS_GATHER_POLICY MODIFY COLUMN END_DATE DATETIME  COMMENT '策略结束日期（yyyy/MM/dd）';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##调拨策略维护新增字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BIS_GATHER_POLICY' AND COLUMN_NAME = 'END_TIME') THEN
	  ALTER TABLE BIS_GATHER_POLICY add COLUMN END_TIME VARCHAR(6) COMMENT '策略结束时间';
ELSE
	ALTER TABLE BIS_GATHER_POLICY MODIFY COLUMN END_TIME VARCHAR(6) COMMENT '策略结束时间';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO



##资金调拨流水规则维护
##新增资金调拨流水规则表
CREATE TABLE
    if not exists NIS_FLOW_RULE
    (
        ID VARCHAR(32) COLLATE utf8_bin NOT NULL COMMENT 'id',
        TRANSFER_TYPE VARCHAR(10) COLLATE utf8_bin NOT NULL COMMENT '调拨类型',
        TRANSFER_MODE VARCHAR(10) COLLATE utf8_bin NOT NULL COMMENT '调拨方式',
        STATUS VARCHAR(1) COLLATE utf8_bin NOT NULL COMMENT '状态',
        CREATE_DATE DATETIME,
        CREATE_BY VARCHAR(32) COLLATE utf8_bin,
        UPDATE_DATE DATETIME,
        UPDATE_BY VARCHAR(32) COLLATE utf8_bin,
        PRIMARY KEY (ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金调拨流水规则表';


##资金调拨流水规则用途表
CREATE TABLE
    if not exists NIS_FLOW_RULE_PURPOSE
    (
        ID VARCHAR(32) COLLATE utf8_bin NOT NULL COMMENT 'id',
        RULE_ID VARCHAR(32) COLLATE utf8_bin NOT NULL COMMENT '规则id',
        PURPOSE VARCHAR(200) COLLATE utf8_bin NOT NULL COMMENT '用途/摘要',
        BANK_TYPE_ID VARCHAR(32) COLLATE utf8_bin COMMENT '银行类别',
        PRIMARY KEY (ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金调拨流水规则用途表';


##新增资金调拨流水规则维护菜单
INSERT INTO sys_menu
(`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`)
SELECT '4e90bf4d54514c81b6451a89cd6d7da9', '', '资金调拨流水规则维护', '资金调拨流水规则维护', 'd61f91e27ed9473ab9e8183343ea4507', NULL, 26, 1, '/netbank/nisFlowRule/list', NULL, '', '2024-05-08 16:53:33', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, 0, 1, NULL FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '4e90bf4d54514c81b6451a89cd6d7da9' LIMIT 1
);
##GOGO

INSERT INTO sys_dict_group
(`ID`, `TYPE_GROUP_CODE`, `TYPE_GROUP_NAME`)
SELECT 'd0dc0eb3eca740738575feff875567fb', 'nisFlowRule.transferType', '调拨类型'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = 'd0dc0eb3eca740738575feff875567fb' LIMIT 1
);
##GOGO


INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '80fd9ce44b2e4862b0b79230c16dd012', '10', '上划', NULL, 'd0dc0eb3eca740738575feff875567fb', 1  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '80fd9ce44b2e4862b0b79230c16dd012' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'd1d911a7044d43e28903a439eedab52a', '11', '下拨', NULL, 'd0dc0eb3eca740738575feff875567fb', 2  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'd1d911a7044d43e28903a439eedab52a' LIMIT 1
);
##GOGO


INSERT INTO sys_dict_group
(`ID`, `TYPE_GROUP_CODE`, `TYPE_GROUP_NAME`)
SELECT 'bb67a0bf86574fcebe4cafb8dc8798a9', 'nisFlowRule.transferMode', '调拨方式'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = 'bb67a0bf86574fcebe4cafb8dc8798a9' LIMIT 1
);
##GOGO


INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '198313a99c7f4782bcca035400953408', '3', '人工', NULL, 'bb67a0bf86574fcebe4cafb8dc8798a9', 3  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '198313a99c7f4782bcca035400953408' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '411ca5d668e74193bd215c84756a33d8', '1', '资金系统自动', NULL, 'bb67a0bf86574fcebe4cafb8dc8798a9', 1  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '411ca5d668e74193bd215c84756a33d8' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '5e7b534dba8e42e2a94dbd7c03ac6cf1', '2', '银行自动', NULL, 'bb67a0bf86574fcebe4cafb8dc8798a9', 2  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '5e7b534dba8e42e2a94dbd7c03ac6cf1' LIMIT 1
);
##GOGO


INSERT INTO sys_dict_group
(`ID`, `TYPE_GROUP_CODE`, `TYPE_GROUP_NAME`)
SELECT 'dfffe876bebe4e87a832e15bcfce1a8d', 'nisFlowRule.status', '规则状态'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = 'dfffe876bebe4e87a832e15bcfce1a8d' LIMIT 1
);
##GOGO


INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '76a657aeda0244b2991f2adf4877b59a', '0', '禁用', NULL, 'dfffe876bebe4e87a832e15bcfce1a8d', 2  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '76a657aeda0244b2991f2adf4877b59a' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'c702b01e3bf44c7eb4b7b60d3602964e', '1', '有效', NULL, 'dfffe876bebe4e87a832e15bcfce1a8d', 1  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'c702b01e3bf44c7eb4b7b60d3602964e' LIMIT 1
);
##GOGO






##### 银行账户查询界面修改
INSERT INTO sys_dict_group
(`ID`, `TYPE_GROUP_CODE`, `TYPE_GROUP_NAME`)
SELECT '6849f5e2f62744deb91f5b784bb596b6', 'btBankAcc.attachmentSign', '是否存在附件'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '6849f5e2f62744deb91f5b784bb596b6' LIMIT 1
);
##GOGO


INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '3b63b9869ad4481bb60672f4500e58f4', '1', '是', NULL, '6849f5e2f62744deb91f5b784bb596b6', 1  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '3b63b9869ad4481bb60672f4500e58f4' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '88bdf13746cf492cb1bf3fdd94279663', '0', '否', NULL, '6849f5e2f62744deb91f5b784bb596b6', 2  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '88bdf13746cf492cb1bf3fdd94279663' LIMIT 1
);
##GOGO




#####银行账户表新增销户原因字段 CANCEL_REASON
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'CANCEL_REASON') THEN
      ALTER TABLE BT_BANK_ACC ADD COLUMN CANCEL_REASON VARCHAR(500);
ELSE
    ALTER TABLE BT_BANK_ACC MODIFY COLUMN CANCEL_REASON VARCHAR(500);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##修改内容:资金日报表菜单排序、状态、URL
UPDATE sys_menu SET  SORT=14, STATUS=1, URL='/report/allocationDaily/list' WHERE ID='d2620a61c4674d408ac8a734b2e7430c';
COMMIT;
#GOGO

##新增资金日报表查询资源
insert into sys_resources  (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'b13cccdedcc54a019cbc6c48a06cd29d', '资金日报表查询资源', '资金日报表查询资源', 'cho:choproduct:nisAllocationDailyReport:query', 1, 'd2620a61c4674d408ac8a734b2e7430c'
from dual  WHERE NOT EXISTS (SELECT 1 FROM sys_resources WHERE ID='b13cccdedcc54a019cbc6c48a06cd29d' LIMIT 1);
COMMIT;
##GOGO


##新增资集团月度资金划拨分析表菜单
insert into sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND,OLD_ICON)
select '429bb697f26642bd896b028a314839bd', '', '集团月度资金划拨分析表', '集团月度资金划拨分析表', '07cbbb8132de427eb53c9d624fb7ee22', NULL, 17, 1, '/report/groupMonthFundaAllocation/list', NULL, '', NULL, NULL, NULL, NULL, 0, 0, NULL
from dual where not exists (select 1 from SYS_MENU where ID = '429bb697f26642bd896b028a314839bd' limit 1);
##GOGO

##新增集团月度资金划拨分析表查询资源
insert into sys_resources  (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '7afeb2b909fa4de785f618c1c544d059', '集团月度资金划拨分析表查询资源', '集团月度资金划拨分析表查询资源', 'group:month:fund:allocation:report:query', 1, '429bb697f26642bd896b028a314839bd'
from dual  WHERE NOT EXISTS (SELECT 1 FROM sys_resources WHERE ID='7afeb2b909fa4de785f618c1c544d059' LIMIT 1);
COMMIT;
##GOGO

##修复银行状态状态错误问题
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
select '708011ff6d2ddf4c0b535563a3f441ae', 'bankAcc.handleStatu.query', '账户状态', NULL, NULL from dual
where not exists (select 1 from sys_dict_group where ID ='708011ff6d2ddf4c0b535563a3f441ae'  limit 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
select '29c7fd010a51a3f62f9c75c7b824114f', '95', '正常', NULL, '708011ff6d2ddf4c0b535563a3f441ae', 3, NULL, NULL from dual
where not exists (select 1 from sys_dict where ID ='29c7fd010a51a3f62f9c75c7b824114f'  limit 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
select '67961ed4b16800b86054f7ccfba2f9fb', '11', '正常', NULL, '708011ff6d2ddf4c0b535563a3f441ae', 2, NULL, NULL from dual
where not exists (select 1 from sys_dict where ID ='67961ed4b16800b86054f7ccfba2f9fb'  limit 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
select '763797d4cb1534f5adb04b833178e902', '-2', '已注销', NULL, '708011ff6d2ddf4c0b535563a3f441ae', 5, NULL, NULL from dual
where not exists (select 1 from sys_dict where ID ='763797d4cb1534f5adb04b833178e902'  limit 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
select '829520bb4aa9f3611708e347ad955ee1', '-1', '正常', NULL, '708011ff6d2ddf4c0b535563a3f441ae', 4, NULL, NULL from dual
where not exists (select 1 from sys_dict where ID ='829520bb4aa9f3611708e347ad955ee1'  limit 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
select '8d79c44188bef7a4c59eced775fbd4a8', '0', '正常', NULL, '708011ff6d2ddf4c0b535563a3f441ae', 1, NULL, NULL from dual
where not exists (select 1 from sys_dict where ID ='8d79c44188bef7a4c59eced775fbd4a8'  limit 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
select 'bc27b9165776428bb3cbd386aab6fac5', '79', '释放', NULL, 'ad7349c140a748439f11bda8fc875e65', 1, NULL, NULL from dual
where not exists (select 1 from sys_dict where ID ='bc27b9165776428bb3cbd386aab6fac5'  limit 1);
##GOGO

#####流水表增加退款勾对人  REFUND_BLEND_BY
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'bis_acc_dtl' AND COLUMN_NAME = 'REFUND_BLEND_BY') THEN
      ALTER TABLE bis_acc_dtl ADD COLUMN REFUND_BLEND_BY VARCHAR(32) COMMENT '退款勾对人';
ELSE
    ALTER TABLE bis_acc_dtl MODIFY COLUMN REFUND_BLEND_BY VARCHAR(32) COMMENT '退款勾对人';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

#####流水表增加退款勾对时间  REFUND_BLEND_TIME
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'bis_acc_dtl' AND COLUMN_NAME = 'REFUND_BLEND_TIME') THEN
      ALTER TABLE bis_acc_dtl ADD COLUMN REFUND_BLEND_TIME DATETIME COMMENT '退款勾对时间';
ELSE
    ALTER TABLE bis_acc_dtl MODIFY COLUMN REFUND_BLEND_TIME DATETIME COMMENT '退款勾对时间';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人:zhaohc
##修改内容:添加定时任务
##修改时间:2024-06-03
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 15, '大参林SSO员工信息同步', now(), now(), 'XXL', '', 'CRON', '0 0 22 1/1 * ?', 'DO_NOTHING', 'FIRST', 'dslSsoSyncUserJob', '{"startDate":"2024-05-15","endDate":"2024-05-18"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='大参林SSO员工信息同步' limit 1);
##GOGO

## 添加SSO授权登陆相关
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
select '8b028c98f8ce4e6181313e3ade3e726f', '3', 'SSO授权登陆', NULL, '319ca67447904f9ca2b91995e31160a0', 500, NULL, NULL from dual
where not exists (select 1 from sys_dict where ID ='8b028c98f8ce4e6181313e3ade3e726f'  limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '5e7a40dfe8494a38b8d3519b20be7557', 'zh_CN', 'SSO授权方式', 'sys.user.loginType.three' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.user.loginType.three' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '4246fc6c49254b0abee05408a36eb88a', 'en_US', 'SSO Authorization', 'sys.user.loginType.three' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.user.loginType.three' AND LANG_CODE = 'en_US' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '4ee826316e9f4ad69816e777bbf70dc3', 'zh_CN', 'SSO授权', 'sys.login.button.sso' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.login.button.sso' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '4b002d70afbe4eb88e13abf25e9b569d', 'en_US', 'SSO Authorization', 'sys.login.button.sso' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.login.button.sso' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人:zhoup
##修改内容:修改单据类型字段名称
##修改时间:2024-06-03
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'RECEIPT_CODE') then
    ALTER TABLE BT_BANK_ACC add RECEIPT_CODE VARCHAR(500);
END IF;
commit;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：qijh
##修改时间：2024.05.20
##修改原因：大参林项目脚本

##企业微信-资金调拨-下拨资金付款失败提醒
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT 'cbd0388493934a9785bb67349394e9c3', '183fc820962c411e9581ce3a7db5f552', '下拨资金付款失败提醒', 'netbank:allocation:nisDemand:bisExcFail', 'f1445907f43944e68b8fb7ce844daf28', '/bdlink/bisexc/list', null, 'netbank:bdLink:bisExc:traceApply', 0, 7, '您有一笔金额为$!currencyTag.getCurSign($!{curId})$!{payMoney}的付款指令交易失败，请处理！', 2 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = 'cbd0388493934a9785bb67349394e9c3' LIMIT 1
);
##GOGO

##企业微信-结算管理-付款成功提醒
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT '7ba91dd5753e4f3dbb8b2419da6677da', '183fc820962c411e9581ce3a7db5f552', '付款成功提醒', 'netbank:allocation:nisDemand:success', '', '', null, '', 0, 8, '您有一笔金额为$!currencyTag.getCurSign($!{curId})$!{payMoney}的付款成功！', 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '7ba91dd5753e4f3dbb8b2419da6677da' LIMIT 1
);
##GOGO

##修改人：qijh
##修改时间：2024.05.30
##修改原因：三单关联脚本

##系统参数付款明细勾对“后台任务每批次执行笔数”
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT 'cfeb319840b54b5380cdf52c9a91c1d9', 'receivable', 'autoCount', '600'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE ID = 'cfeb319840b54b5380cdf52c9a91c1d9' LIMIT 1
);
##GOGO

##系统参数付款明细勾对“付款明细滚动匹配天数”
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT '8a8f37852c46489b97715bb3abb356cb', 'receivable', 'rollMate', '3'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE ID = '8a8f37852c46489b97715bb3abb356cb' LIMIT 1
);
##GOGO

##三单关联执行策略表增加“相差天数”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BIS_BANK_EXECUTION_STRATEGY' AND COLUMN_NAME = 'TRANS_DAYS') THEN
      ALTER TABLE BIS_BANK_EXECUTION_STRATEGY ADD COLUMN TRANS_DAYS decimal(4,0);
ELSE
    ALTER TABLE BIS_BANK_EXECUTION_STRATEGY MODIFY COLUMN TRANS_DAYS decimal(4,0);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'BLEND_SIGN') THEN
      ALTER TABLE NIS_BILLHEAD ADD COLUMN BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'NIS_WAGE' AND COLUMN_NAME = 'BLEND_SIGN') THEN
      ALTER TABLE NIS_WAGE ADD COLUMN BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'FEX_CASH_FLOW' AND COLUMN_NAME = 'BLEND_SIGN') THEN
      ALTER TABLE FEX_CASH_FLOW ADD COLUMN BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'NIS_ALLOCATION_REQ' AND COLUMN_NAME = 'BLEND_SIGN') THEN
      ALTER TABLE NIS_ALLOCATION_REQ ADD COLUMN BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##历史数据更新。。把已勾对的单据修改勾对标志为已勾对
update nis_billhead set blend_sign = '1' where id in (select bill_id from NIS_BILL_BANK_SERIAL_NO where is_from = 'nis_billhead');
update nis_wage set blend_sign = '1' where id in (select bill_id from NIS_BILL_BANK_SERIAL_NO where is_from = 'nis_wage');
update fex_cash_flow set blend_sign = '1' where id in (select bill_id from NIS_BILL_BANK_SERIAL_NO where is_from = 'fex_cash_flow');
update nis_allocation_req set blend_sign = '1' where id in (select bill_id from NIS_BILL_BANK_SERIAL_NO where is_from = 'nis_allocation_req');
commit;
##GOGO

##新增参数:收款流水认领天数
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT 'e570035e514d4474aa2fa56c76a3549a', 'receivable', 'dayCount', '3' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE type = 'receivable' and name = 'dayCount' LIMIT 1
);
##GOGO

##新增参数:后台任务每批次执行笔数
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT '401086c96dc24957b50be80e053c6555', 'receivable', 'collectionAutoCount', '600' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE type = 'receivable' and name = 'collectionAutoCount' LIMIT 1
);
##GOGO


##更新参数“退款勾对天数”
update SYS_CONFIG set TYPE='receivable',VALUE='2' where ID='2c3176b78bc54162907ca6d25c04ab8b';
commit;
##GOGO

##新增参数:退款勾对定时任务取数天数
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT '3f23dec034cc4cd9acc604dcba2729fd', 'receivable', 'timedTasksDays', '30' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE type = 'receivable' and name = 'timedTasksDays' LIMIT 1
);
##GOGO

##修改字典
UPDATE sys_dict SET TYPE_NAME='付款与流水自动勾对' WHERE id = 'a48fc5d8fc134246ad037e2074907aca';
UPDATE sys_dict SET TYPE_NAME='流水与回单自动勾对' WHERE id = '93f4c38fdb15492a82f14725862a23e1';
##GOGO

##修改人：qijh
##修改内容：新增字典组"交易日期匹配规则"
##修改时间：2023-11-10
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '77dfbff5260247fe884ff3d22c33cea7', 'bisBankExecutionStrategy.transTimeRule', '交易日期匹配规则'from dual
where not exists (select 1 from sys_dict_group where ID ='77dfbff5260247fe884ff3d22c33cea7' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"相等"
##修改时间：2023-11-10
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '0d7ec7fd1b494d93818691d1eebbb515', '0', '相等',null,'77dfbff5260247fe884ff3d22c33cea7',1 from dual
where not exists (select 1 from sys_dict where ID ='0d7ec7fd1b494d93818691d1eebbb515' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"不相等"
##修改时间：2023-11-10
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '07bf996a73d24b85886fe928e8a9b757', '1', '不相等',null,'77dfbff5260247fe884ff3d22c33cea7',2 from dual
where not exists (select 1 from sys_dict where ID ='07bf996a73d24b85886fe928e8a9b757' limit 1);
##GOGO


##修改人：qijh
##修改内容：新增字典组"银行流水唯一标识"
##修改时间：2023-10-08
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '11ed0f83332a43718e11b0d2d6754dc7', 'bisBankExecutionStrategy.bankUnique', '银行流水唯一标识'from dual
where not exists (select 1 from sys_dict_group where ID ='11ed0f83332a43718e11b0d2d6754dc7' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BIS_DTL_NO"
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '169c0288022345aea7054a5e91af0ea6', 'BIS_DTL_NO', 'BIS_DTL_NO',null,'11ed0f83332a43718e11b0d2d6754dc7',1 from dual
where not exists (select 1 from sys_dict where ID ='169c0288022345aea7054a5e91af0ea6' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BANK_SERIAL_NO"
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'bc9c1f939b7d4c18836eda1e3c8662c8', 'BANK_SERIAL_NO', 'BANK_SERIAL_NO',null,'11ed0f83332a43718e11b0d2d6754dc7',2 from dual
where not exists (select 1 from sys_dict where ID ='bc9c1f939b7d4c18836eda1e3c8662c8' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BIS_EBILL_NO"
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '4eb55cc8f23e423eaa0d522301774059', 'BIS_EBILL_NO', 'BIS_EBILL_NO',null,'11ed0f83332a43718e11b0d2d6754dc7',3 from dual
where not exists (select 1 from sys_dict where ID ='4eb55cc8f23e423eaa0d522301774059' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"ACC_NO"
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '127d301a138f446ca1ef115a0faf0860', 'ACC_NO', 'ACC_NO',null,'11ed0f83332a43718e11b0d2d6754dc7',4 from dual
where not exists (select 1 from sys_dict where ID ='127d301a138f446ca1ef115a0faf0860' limit 1);
##GOGO


##修改人：qijh
##修改内容：新增字典组"付款单唯一标识"
##修改时间：2023-10-08
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select 'b5f573885fc54f55bbcd33f9ae8be6f5', 'bisBankExecutionStrategy.payUnique', '付款单唯一标识'from dual
where not exists (select 1 from sys_dict_group where ID ='b5f573885fc54f55bbcd33f9ae8be6f5' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BIS_DTL_NO"
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'bb2e253011c54b3ea85aa5888176693e', 'BIS_DTL_NO', 'BIS_DTL_NO',null,'b5f573885fc54f55bbcd33f9ae8be6f5',1 from dual
where not exists (select 1 from sys_dict where ID ='bb2e253011c54b3ea85aa5888176693e' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BANK_SERIAL_NO"
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '26513d58521046c2a0b4dcaf46b5e07e', 'BANK_SERIAL_NO', 'BANK_SERIAL_NO',null,'b5f573885fc54f55bbcd33f9ae8be6f5',2 from dual
where not exists (select 1 from sys_dict where ID ='26513d58521046c2a0b4dcaf46b5e07e' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BILL_NO"
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'ad9f620ffafe4920aaae35eeaa99e5e6', 'BILL_NO', 'BILL_NO',null,'b5f573885fc54f55bbcd33f9ae8be6f5',3 from dual
where not exists (select 1 from sys_dict where ID ='ad9f620ffafe4920aaae35eeaa99e5e6' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典组"电子回单唯一标识"
##修改时间：2023-10-09
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '0e876062729f4ff4bfdfaa64971d12e4', 'bisBankExecutionStrategy.eReceiptUnique', '电子回单唯一标识'from dual
where not exists (select 1 from sys_dict_group where ID ='0e876062729f4ff4bfdfaa64971d12e4' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BIS_EBILL_NO"
##修改时间：2023-10-09
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'ea934b312701429abecfd3dc4b46a508', 'BIS_EBILL_NO', 'BIS_EBILL_NO',null,'0e876062729f4ff4bfdfaa64971d12e4',1 from dual
where not exists (select 1 from sys_dict where ID ='ea934b312701429abecfd3dc4b46a508' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BANK_SERIAL_NO"
##修改时间：2023-10-09
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '65c6ef668ab94abc8711fc6093a64da3', 'BANK_SERIAL_NO', 'BANK_SERIAL_NO',null,'0e876062729f4ff4bfdfaa64971d12e4',2 from dual
where not exists (select 1 from sys_dict where ID ='65c6ef668ab94abc8711fc6093a64da3' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BILL_NO"
##修改时间：2023-10-09
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '81b3bb52533c44809ff5b6af5fd26623', 'BILL_NO', 'BILL_NO',null,'0e876062729f4ff4bfdfaa64971d12e4',3 from dual
where not exists (select 1 from sys_dict where ID ='81b3bb52533c44809ff5b6af5fd26623' limit 1);
##GOGO

##流水增加电子回单勾对标志字段 RECEIPT_BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'RECEIPT_BLEND_SIGN') THEN
      ALTER TABLE BIS_ACC_DTL ADD COLUMN RECEIPT_BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##系统参数电子回单勾对“后台任务每批次执行笔数”
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT 'c4c8f8d5b68247bfb102c880e93bd4d1', 'ele', 'autoCount', '600'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE ID = 'c4c8f8d5b68247bfb102c880e93bd4d1' LIMIT 1
);
##GOGO

##系统参数电子回单勾对“电子回单勾对滚动匹配天数”
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT 'e9d0e06f38074076affe09f87839a7ae', 'ele', 'rollMate', '3'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE ID = 'e9d0e06f38074076affe09f87839a7ae' LIMIT 1
);
##GOGO

##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_BANK_RECEIPT_DTL' AND COLUMN_NAME = 'AUTO_SIGN') THEN
      ALTER TABLE BIS_BANK_RECEIPT_DTL ADD COLUMN AUTO_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##修改人：zhaohc
##修改内容：新增国际化数据
##修改时间：2024-06-04
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '4c5b2929224111efa9fe005056a84efa', 'zh_CN', '付款票据关联信息不能为空', 'nis.assign.message.nisbill.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.assign.message.nisbill.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '63ac1e47224111efa9fe005056a84efa', 'zh_CN', '付款开票登记信息不能为空', 'nis.assign.message.abemitbill.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.assign.message.abemitbill.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

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

##数据发送状态记录中间表
CREATE TABLE IF NOT EXISTS DSL_INTEFACE_FLAG(
    ID             VARCHAR(50) NOT NULL COMMENT '主键id',
    SOURCE_ID      VARCHAR(50) NOT NULL COMMENT '外表主键',
    RETURN_MESSAGE VARCHAR(300) COMMENT 'ERP系统返回消息',
    RETURN_CODE    VARCHAR(30) COMMENT 'ERP系统返回状态',
    TABLE_SOURCE   VARCHAR(50) COMMENT '数据源标识',
    OP_TIME        DATETIME DEFAULT NULL COMMENT 'ERP系统返回信息时间',
    SYNC_STATUS    VARCHAR(1) COMMENT '数据发送状态 F失败 S成功',
    REVERSE1       VARCHAR(50) COMMENT '备用字段1',
    REVERSE2       VARCHAR(50) COMMENT '备用字段2',
    REVERSE3       VARCHAR(50) COMMENT '备用字段3',
    REVERSE4       VARCHAR(50) COMMENT '备用字段4',
    REVERSE5       VARCHAR(50) COMMENT '备用字段5',
    CONSTRAINT PK_DSL_INTEFACE_FLAG PRIMARY KEY (ID)
    );
##GOGO

##修改日期：2024-06-06
##修改人员：zhangx
##修改内容：DSL_INTEFACE_FLAG 添加 SOURCE_NUM 共享单号字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'DSL_INTEFACE_FLAG' AND COLUMN_NAME = 'SOURCE_NUM') THEN
ALTER TABLE DSL_INTEFACE_FLAG ADD COLUMN SOURCE_NUM VARCHAR(100) comment '全量流水接口共享单号字段';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改日期：2024-04-24
##修改人员：zhangx
##修改内容：大参林银行流水推送视图
CREATE OR REPLACE VIEW DSL_BIS_ACC_DTL_VIEW AS
SELECT dtl.id AS SEQ_ID,
       '01' AS DJZT_CODE,
       '正常' AS DJZT,
       dtl.ID AS YHLSH,
       bc.code AS DWBH,
       bc.name AS DWMC,
       CASE
           WHEN dtl.cd_sign =  '1' THEN '02'
           ELSE '01' END AS DJLX_CODE,
       CASE
           WHEN dtl.cd_sign =  '1' THEN '付款'
           ELSE '收款' END AS DJLX_DESC,
       dtl.REMARK AS YT,
       dtl.BANK_ACC AS ZHBH,
       dtl.ACC_NAME AS ZHMC,
       acc.BANK_CODE AS LHH,
       dtl.BANK_NAME AS KHH,
       dtl.amt AS JE,
       dtl.OPP_ACC_NO AS DFZH,
       dtl.OPP_ACC_NAME AS DFHM,
       dtl.OPP_ACC_BANK AS DFKHH,
       DATE_FORMAT(dtl.TRANS_TIME, '%Y%m%d') AS JYRQ,
       DATE_FORMAT(dtl.TRANS_TIME, '%H%i%s') AS JYSJ,
       '01' AS SJLY_CODE,
       '银企直联获取' AS SJLY_DESC,
       'POST' AS ACTION_TYPE,
       nb.BURSAR_BILL_NO AS SOURCE_NUM
FROM bis_acc_dtl dtl
         LEFT JOIN NIS_BILL_BANK_SERIAL_NO ns ON dtl.id=ns.dtl_id
         LEFT JOIN nis_billhead nb ON nb.id=ns.bill_id
         LEFT JOIN bt_bank_acc acc ON dtl.bank_acc = acc.bank_acc
         LEFT JOIN sys_corp bc ON bc.id = acc.corp_id
         LEFT JOIN DSL_INTEFACE_FLAG ef ON ef.source_id = dtl.id AND ef.table_source = 'DSL_BIS_ACC_DTL_VIEW'
WHERE 1=1
  AND dtl.TRANS_TIME > '2024-07-19'
  AND NOT EXISTS(SELECT 1 FROM DSL_INTEFACE_FLAG f WHERE f.source_id = dtl.id AND f.table_source = 'DSL_BIS_ACC_DTL_VIEW' AND IFNULL(f.SYNC_STATUS,'F') = 'S' AND (IFNULL(nb.BURSAR_BILL_NO,'') = IFNULL(f.SOURCE_NUM,'')));

##修改日期：2024-04-25
##修改人员：zhangx
##修改内容：大参林票据信息同步视图
CREATE OR REPLACE VIEW DSL_AB_BILL_VIEW AS
SELECT
    a.ID AS FMS_ID,
    'M' AS RECEIPT_TYPE,
    'BFS' AS RECEIPT_SOURCE,
    v.ORIG_NO AS RECEIPT_NUM,
    v.SBBLL_RNG_STRT_SN AS SUB_RECEIPT_FROM,
    v.SBBLL_RNG_END_SN AS SUB_RECEIPT_TO,
    CASE
        WHEN v.SOURCE_TYPE = '0' THEN 'AP'
        ELSE 'AR' END AS RECEIPT_PROPERTY,
    CASE
        WHEN v.BILL_TYPE = 10 AND v.BILL_FLAG = 10 THEN '银行承兑汇票-纸质'
        WHEN v.BILL_TYPE = 10 AND v.BILL_FLAG = 11 THEN '银行承兑汇票-电子'
        WHEN v.BILL_TYPE = 11 AND v.BILL_FLAG = 10 THEN '商业承兑汇票'
        WHEN v.BILL_TYPE = 11 AND v.BILL_FLAG = 11 THEN '商业承兑汇票-电子'
        END AS RECEIPT_NAME,
    v.BILL_MONEY AS RECEIPT_COST,
    'CNY' AS CURRENCY_CODE,
    v.START_DATE AS CREATE_RECEIPT_DATE,
    v.END_DATE AS RECEIPT_DUE_DATE,
    v.PAYER AS CREATE_RECEIPT_NAME,
    v.PAYERACC AS CREATE_REC_ACCOUNT,
    v.PAYER_BANK_NAME AS CREATE_REC_BANK,
    v.PAYEE AS PAYEE_NAME,
    v.PAYEEACC AS PAYEE_ACCOUNT,
    v.PAYEE_BANK_NAME AS PAYEE_BANK,
    CONCAT((vv.BAIL_PERCENT * 100), '%') AS CASH_DEPOSIT_SCALE,
    vv.BAIL_MONEY AS CASH_DEPOSIT_COST,
    vv.RMK AS COMMT,
    CASE
        WHEN a.brief_id IN ('010','9010') THEN 'Y'
        ELSE 'N' END AS ENDORSE_FLAG,
    CASE
        WHEN a.brief_id IN ('010','9010') THEN sc.name
        ELSE NULL END AS ENDORSER,
    CASE
        WHEN a.brief_id IN ('010','9010') THEN a.CREATE_DATE
        ELSE NULL END AS ENDOSE_DATE,
    v.BILL_MONEY - IFNULL(vv.BAIL_MONEY,0) AS ACTUAL_COST,
    CASE
        WHEN a.brief_id = '6001' THEN '11'
        WHEN a.brief_id = '9010' THEN '12'
        WHEN a.brief_id = '6004' THEN '13'
        WHEN a.brief_id = '9003' THEN '26'
        WHEN a.brief_id = '6120' THEN '28'
        WHEN a.brief_id = '004' THEN '29'
        END AS ACTION_TYPE,
    a.CREATE_DATE AS CASHING_DATE,
    a.CREATE_NAME AS MAKER,
    'R' AS PUSH_STATUS_FLG,
    'R' AS PROCESSED_FLAG,
    CASE
        WHEN a.brief_id IN ('9003','6001') THEN '11'
        WHEN a.brief_id IN ('9010','010') THEN '12'
        WHEN a.brief_id IN ('6004') THEN '13'
        WHEN a.brief_id IN ('001') THEN '27'
        WHEN a.brief_id IN ('6120') THEN '28'
        WHEN a.brief_id IN ('004') THEN '29'
        END AS STATUS,
    CASE WHEN a.brief_id IN ('9010','010') THEN v.BILL_MONEY
         ELSE NULL END AS ENDOSE_AMOUNT,
    CASE WHEN a.brief_id IN ('6120') THEN a.BUSINESS_DATE
         ELSE NULL END AS RECEIPT_CASH_DATE
FROM ab_accrecords a
         LEFT JOIN ab_voucherinfo v  ON a.vid = v.id
         LEFT JOIN ab_voucherinfo_vice vv ON a.vvid = vv.id
         LEFT JOIN sys_corp sc ON sc.id = vv.CORP_ID
         LEFT JOIN DSL_INTEFACE_FLAG ef ON ef.source_id = a.id AND ef.table_source = 'DSL_AB_BILL_VIEW'
WHERE 1=1
  AND a.opt_status = 19
  AND a.status = 95
  AND ((a.brief_id not in ('9010','010') and a.OLD_ACC_ID is null) or a.brief_id in ('9010','010'))
  AND ((a.brief_id not in ('9010','010') and vv.OLD_DRAFT_ID is null) or a.brief_id in ('9010','010'))
  AND (v.ORIG_NO IS NOT NULL AND v.ORIG_NO <> '')
  AND v.START_DATE > '2024-07-17'
  AND a.brief_id IN('001','6001','9010','6004','9003','6120','004','010')
  AND IFNULL(ef.SYNC_STATUS, 'F') <> 'S';

##修改日期：2024-04-26
##修改人员：zhangx
##修改内容：大参林电子回单查询视图
create or replace view DSL_BIS_BANK_RECEIPT_DTL_VIEW as
SELECT
    b.id AS id,
    d.id AS serialId,
    pay.SERIAL_NO_ERP AS serialNoErp,
    b.RECEIPT_NO AS receiptNo,
    d.ID AS transSerialNo,
    CASE
        WHEN b.CD_SIGN = '1' THEN '02'
        ELSE '01' END AS fileType,
    CONCAT('http://ten-dsl-cdn.dslbuy.com/', b.FILE_PATH) AS filePath,
    b.CHARAGE_DATE AS fileDate,
    b.PAYEE_ACC_NO AS PAYEE_Acc_No,
    b.PAYEE_ACC_NAME AS PAYEE_Name,
    b.PAYEE_BRACN_NAME AS PAYEE_Bank,
    acc2.BANK_CODE AS PAYEE_Code,
    b.PAY_ACC_NO AS PAYER_Acc_No,
    b.PAY_ACC_NAME AS PAYER_Acc_Name,
    b.PAY_BRANCH_NAME AS PAYER_Bank_Name,
    acc.BANK_CODE AS PAYER_Code,
    bc.ENGLISH_CODE AS Cur,
    b.TRANS_AMT AS Amt
FROM bis_bank_receipt_dtl b
         LEFT JOIN bt_bank_acc acc ON acc.bank_acc = b.PAY_ACC_NO
         LEFT JOIN bt_bank_acc acc2 ON acc2.bank_acc = b.PAYEE_ACC_NO
         LEFT JOIN bt_currency bc ON b.cur_id = bc.id
         LEFT JOIN BIS_RECEIPT_DTL brd ON brd.RECEIPT_ID=b.ID
         LEFT JOIN bis_acc_dtl d ON d.id=brd.DTL_ID AND d.cd_sign=b.cd_sign  AND d.blend_sign='1' ##关联银行流水
	LEFT JOIN nis_bill_bank_serial_no nb ON nb.dtl_id=d.id
    LEFT JOIN nis_billhead n ON n.id=nb.bill_id ##关联付款单
    LEFT JOIN erp_payinfo pay ON n.erp_id=pay.id   ##关联付款中间表
;

##修改日期：2024-04-26
##修改人员：zhangx
##修改内容：大参林联行号查询视图
CREATE OR REPLACE VIEW DSL_BT_INPUT_BANK_INFO_VIEW AS
SELECT
    b.SYS_BANK_CODE AS SYS_BANK_CODE,
    b.BANK_NAME AS BANK_NAME,
    b.UPDATE_DATE AS UPDATE_DATE,
    b.CREATE_DATE as CREATE_DATE,
    t.BANK_TYPE as BANK_TYPE_CODE,
    t.TYPE_NAME as BANK_TYPE_NAME
FROM bt_input_bank_info b
    LEFT JOIN BT_BANK_TYPE t on t.BANK_PREFIX = b.BANK_PREFIX ;

##创建人:lijq
##创建时间:2024年4月24日

##erp_payinfo 的 ABS 设置为非空
ALTER TABLE `erp_payinfo` CHANGE COLUMN `VOUCHER_NO_ERP` `VOUCHER_NO_ERP` VARCHAR(50) NULL ;
ALTER TABLE `erp_payinfo` CHANGE COLUMN `PAYER_ACC_NO` `PAYER_ACC_NO` VARCHAR(30) NULL ;
ALTER TABLE `erp_payinfo` CHANGE COLUMN `ABS` `ABS` VARCHAR(200) NULL ;


##增加字段语句 ERP_PAYINFO 添加 GX_VOUCHER_TYPE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ERP_PAYINFO' AND COLUMN_NAME = 'GX_VOUCHER_TYPE') THEN
ALTER TABLE ERP_PAYINFO ADD COLUMN GX_VOUCHER_TYPE VARCHAR(32) NULL COMMENT '共享系统传送的付款方式编码';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##增加字段语句 ERP_PAYINFO 添加 BUS_BILL_TYPE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ERP_PAYINFO' AND COLUMN_NAME = 'BUS_BILL_TYPE') THEN
ALTER TABLE ERP_PAYINFO ADD COLUMN BUS_BILL_TYPE VARCHAR(32) NULL COMMENT '业务单据类型编码';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##增加字段语句 ERP_PAYINFO 添加 PURPOSE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ERP_PAYINFO' AND COLUMN_NAME = 'PURPOSE') THEN
ALTER TABLE ERP_PAYINFO ADD COLUMN PURPOSE VARCHAR(20) NULL COMMENT '转账摘要或用途';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##增加字段语句 ERP_PAYINFO 添加 IS_RAISE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ERP_PAYINFO' AND COLUMN_NAME = 'IS_RAISE') THEN
ALTER TABLE ERP_PAYINFO ADD COLUMN IS_RAISE VARCHAR(1) NULL COMMENT '是否募集账户 Y:是，N:否';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##增加字段语句 ERP_PAYINFO 添加 ADD_WORD
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ERP_PAYINFO' AND COLUMN_NAME = 'ADD_WORD') THEN
ALTER TABLE ERP_PAYINFO ADD COLUMN ADD_WORD VARCHAR(60) NULL COMMENT '附言';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##增加字段语句 ERP_PAYINFO 添加 OP_CODE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ERP_PAYINFO' AND COLUMN_NAME = 'OP_CODE') THEN
ALTER TABLE ERP_PAYINFO ADD COLUMN OP_CODE VARCHAR(20) NULL COMMENT '操作员';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##增加字段语句 ERP_PAYINFO 添加 SETTLE_CORP_CODE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ERP_PAYINFO' AND COLUMN_NAME = 'SETTLE_CORP_CODE') THEN
ALTER TABLE ERP_PAYINFO ADD COLUMN SETTLE_CORP_CODE VARCHAR(32) NULL COMMENT '结算单位代码';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##增加字段语句 ERP_PAYINFO 添加 SHARING_DATE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ERP_PAYINFO' AND COLUMN_NAME = 'SHARING_DATE') THEN
ALTER TABLE ERP_PAYINFO ADD COLUMN SHARING_DATE DATETIME NULL COMMENT '共享计划付款日期';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##增加字段语句 NIS_BILLHEAD 添加 SETTLE_CORP_CODE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'SETTLE_CORP_CODE') THEN
ALTER TABLE NIS_BILLHEAD ADD COLUMN SETTLE_CORP_CODE VARCHAR(32) NULL COMMENT '结算单位代码';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##增加字段语句 NIS_BILLHEAD 添加 OP_CODE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'OP_CODE') THEN
ALTER TABLE NIS_BILLHEAD ADD COLUMN OP_CODE VARCHAR(32) NULL COMMENT '操作员';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


-- 单据（包括原单和拆分单据）
CREATE OR REPLACE VIEW DSL_ALL_BILL_VIEW AS
-- 未拆分、未合并单据
SELECT n.id,
       n.bill_no         AS originalBillNo,
       n.bill_no,
       n.status,
       n.erp_id,
       n.wish_payday,
       n.pay_money,
       n.payer_acc,
       n.cur_id ,
       n.create_by,
       n.is_payed,
       n.bank_serial_no,
       n.purpose,
       n.voucher_type_id
FROM nis_billhead n
         LEFT JOIN nis_billhead n2 ON n2.split_id = n.id
         LEFT JOIN nis_billhead n3 ON n.merge_id = n3.id
WHERE 1 = 1
  AND n.orig_split_id IS NULL
  AND n2.id IS NULL -- 未拆分
  AND n3.id IS NULL -- 未合并
  AND n.erp_id IS NOT NULL
UNION all
-- 拆分单据
SELECT n2.id,
       n.bill_no         AS originalBillNo,
       n2.bill_no,
       n2.status,
       n.erp_id,
       n2.wish_payday,
       n2.pay_money,
       n2.payer_acc,
       n2.cur_id ,
       n2.create_by,
       n2.is_payed,
       n2.bank_serial_no,
       n2.purpose,
       n2.voucher_type_id
FROM nis_billhead n  -- 原始单据
         LEFT JOIN nis_billhead n2  ON n2.orig_split_id = n.id  -- 拆分后的单据
WHERE 1 = 1
  AND n2.id IS NOT NULL
  AND n2.split_id IS NOT NULL
  AND n.status = -2
  AND NOT EXISTS
    (SELECT 1 FROM nis_billhead t WHERE t.split_id = n2.id)
  AND n.erp_id IS NOT NULL
;

CREATE OR REPLACE VIEW dsl_payment_result_view AS
-- 撤销付款
select erp.serial_no_erp       as paymentNo,
       n.bill_no               as paymentVoucherNo,
       null                    as flowNo,
       erp.GX_VOUCHER_TYPE     as paymentType,
       'C'                     as paymentStatus, -- 撤销付款
       null                    as actualPaymentAmount,
       null                    as actualPaymentDate,
       null                    as payBankAccountNo,
       null                    as payBankAccountName,
       erp.SETTLE_CORP_CODE    as company,
       erp.corp_code           as actualPaymentCompany,
       (select CONCAT_WS( '+',nm.op,nm.op_name) from nis_message nm left join nis_billhead nis on nm.BILL_NO = nis.id where nis.BILL_NO = n.bill_no and nm.MESSAGE_TYPE = '2' limit 1)
                               as paymentCashierUserNo,  -- 付款单未记录提交人，待二开
       n.purpose               as summary,
       null                    as billNo,
       null                    as intervalBeginNo,
       null                    as intervalEndNo,
       null                    as billAmount,
       null                    as billStatus,
       null                    as customerCode,
       null                    as customerName,
       null                    as billDate,
       null                    as paymentErrReason,
       null                    as refundBankFlowNo,
       null                    as refundAmount,
       null                    as refundFlowTradeDate,
       null                    as refundRemitter,
       null                    as refundRemitterAccount,
       null                    as refundFlowSummary,
       null                    as refundFlowBlendingUser,
       null                    as refundFlowBlendingDate,
       ifNULL(bt.bus_type_name,'作废')
                               as revokePaymentReason
from erp_payinfo erp
    inner join dsl_all_bill_view n on n.erp_id = erp.id
    left join bis_exc exc on exc.cbs_bill_id = n.id
    left join bt_charge_back_type bt ON bt.bus_type = erp.bus_type
    left join DSL_INTEFACE_FLAG flag ON flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'C')
    AND flag.TABLE_SOURCE = 'dsl_payment_result'
where 1=1
  AND n.status = -2
  AND IFNULL(flag.SYNC_STATUS,'F') = 'F'
UNION ALL

-- 付款成功
select erp.serial_no_erp       as paymentNo,
       n.bill_no               as paymentVoucherNo,
       dtl.ID                  as flowNo,
       erp.GX_VOUCHER_TYPE     as paymentType,
       'S'                     as paymentStatus, -- 付款成功
       case when (av.id is not null or n.is_payed='1') then n.pay_money else dtl.amt end
                               as actualPaymentAmount,
       case when (av.id is not null or n.is_payed='1') then DATE_FORMAT(n.wish_payday,'%Y-%m-%d %H:%i:%s') else DATE_FORMAT(dtl.trans_time,'%Y-%m-%d %H:%i:%s') end
                               as actualPaymentDate,
       dtl.bank_acc            as payBankAccountNo,
       dtl.bank_name           as payBankAccountName,
       erp.SETTLE_CORP_CODE    as company,
       erp.corp_code           as actualPaymentCompany,
       (select CONCAT_WS( '+',nm.op,nm.op_name) from nis_message nm left join nis_billhead nis on nm.BILL_NO = nis.id where nis.BILL_NO = n.bill_no and nm.MESSAGE_TYPE = '2' limit 1)
           as paymentCashierUserNo,  -- 付款单未记录提交人，待二开
       n.purpose               as summary,
       av.orig_no              as billNo,
       av.SBBLL_RNG_STRT_SN    as intervalBeginNo,
       av.SBBLL_RNG_END_SN     as intervalEndNo,
       av.BILL_MONEY           as billAmount,
       case when av.SOURCE_TYPE = '0' then '1' when av.SOURCE_TYPE = '1' then '2' end
                               as billStatus,
       av.PAYEEACC             as customerCode,
       av.PAYEE                as customerName,
       DATE_FORMAT(av.START_DATE,'%Y-%m-%d')
                               as billDate,
       null                    as paymentErrReason,
       null                    as refundBankFlowNo,
       null                    as refundAmount,
       null                    as refundFlowTradeDate,
       null                    as refundRemitter,
       null                    as refundRemitterAccount,
       null                    as refundFlowSummary,
       null                    as refundFlowBlendingUser,
       null                    as refundFlowBlendingDate,
       null                    as revokePaymentReason
from erp_payinfo erp
         inner join  dsl_all_bill_view n on n.erp_id = erp.id
         left join Fc_Voucher_Type f on n.voucher_Type_Id = f.id
         left join bis_exc exc on exc.cbs_bill_id = n.id
         left join bt_charge_back_type  bt ON bt.bus_type = erp.bus_type
         LEFT JOIN NIS_BILL_BANK_SERIAL_NO ns ON n.id = ns.bill_id
         LEFT JOIN bis_acc_dtl dtl  ON ns.dtl_id = dtl.id
         left join nis_bill nb on nb.pay_id = n.id
         left join ab_accrecords aa on aa.id = nb.bill_id
         left join ab_voucherinfo av on av.id = aa.vid
         left join DSL_INTEFACE_FLAG flag ON flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'S')
    AND flag.TABLE_SOURCE = 'dsl_payment_result'
where 1=1
  AND (
        (av.orig_no IS NOT NULL AND av.orig_no <> '' and n.status in (95,99))       -- 票据付款
        or (f.SYS_VOUCHER_CODE='04' AND n.status = 95)    -- 付款方式为现付，审批通过
        or (((n.is_payed='0' AND exc.voucher_stat='0') OR (n.is_payed='1' AND n.status = 95)) AND dtl.blend_sign = '1' AND dtl.id is not null) -- 线上交易成功或线下审批通过，进行了付款勾兑
      )
  AND IFNULL(flag.SYNC_STATUS,'F') = 'F'
UNION ALL

-- 付款失败
select erp.serial_no_erp       as paymentNo,
       n.bill_no               as paymentVoucherNo,
       null                    as flowNo,
       erp.GX_VOUCHER_TYPE     as paymentType,
       'E'                     as paymentStatus, -- 付款失败
       null                    as actualPaymentAmount,
       null                    as actualPaymentDate,
       null                    as payBankAccountNo,
       null                    as payBankAccountName,
       erp.SETTLE_CORP_CODE    as company,
       erp.corp_code           as actualPaymentCompany,
       (select CONCAT_WS( '+',nm.op,nm.op_name) from nis_message nm left join nis_billhead nis on nm.BILL_NO = nis.id where nis.BILL_NO = n.bill_no and nm.MESSAGE_TYPE = '2' limit 1)
           as paymentCashierUserNo,  -- 付款单未记录提交人，待二开
       n.purpose               as summary,
       av.orig_no              as billNo,
       av.SBBLL_RNG_STRT_SN    as intervalBeginNo,
       av.SBBLL_RNG_END_SN     as intervalEndNo,
       av.BILL_MONEY           as billAmount,
       av.BILL_STATUS          as billStatus,
       av.PAYEEACC             as customerCode,
       av.PAYEE                as customerName,
       DATE_FORMAT(av.START_DATE,'%Y-%m-%d')
                               as billDate,
       exc.next_return_msg     as paymentErrReason,
       null                    as refundBankFlowNo,
       null                    as refundAmount,
       null                    as refundFlowTradeDate,
       null                    as refundRemitter,
       null                    as refundRemitterAccount,
       null                    as refundFlowSummary,
       null                    as refundFlowBlendingUser,
       null                    as refundFlowBlendingDate,
       null                    as revokePaymentReason
from erp_payinfo erp
    inner join  dsl_all_bill_view n on n.erp_id = erp.id
    left join bis_exc exc on exc.cbs_bill_id = n.id
    left join nis_bill nb on nb.pay_id = n.id
    left join ab_accrecords aa on aa.id = nb.bill_id
    left join ab_voucherinfo av on av.id = aa.vid
    left join DSL_INTEFACE_FLAG flag ON flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'E')
    AND flag.TABLE_SOURCE = 'dsl_payment_result'
where 1=1
  AND exc.voucher_stat = '7'
  AND IFNULL(flag.SYNC_STATUS,'F') = 'F'
UNION ALL

-- 退汇
select erp.serial_no_erp       as paymentNo,
       n.bill_no               as paymentVoucherNo,
       null                    as flowNo,
       erp.GX_VOUCHER_TYPE     as paymentType,
       'R'                     as paymentStatus, -- 退汇
       null                    as actualPaymentAmount,
       null                    as actualPaymentDate,
       null                    as payBankAccountNo,
       null                    as payBankAccountName,
       erp.SETTLE_CORP_CODE    as company,
       erp.corp_code           as actualPaymentCompany,
       (select CONCAT_WS( '+',nm.op,nm.op_name) from nis_message nm left join nis_billhead nis on nm.BILL_NO = nis.id where nis.BILL_NO = n.bill_no and nm.MESSAGE_TYPE = '2' limit 1)
           as paymentCashierUserNo,  -- 付款单未记录提交人，待二开
       n.purpose               as summary,
       av.orig_no              as billNo,
       av.SBBLL_RNG_STRT_SN    as intervalBeginNo,
       av.SBBLL_RNG_END_SN     as intervalEndNo,
       av.BILL_MONEY           as billAmount,
       av.BILL_STATUS          as billStatus,
       av.PAYEEACC             as customerCode,
       av.PAYEE                as customerName,
       DATE_FORMAT(av.START_DATE,'%Y-%m-%d')
                               as billDate,
       null                    as paymentErrReason,
       dtl.bank_serial_no      as refundBankFlowNo,
       dtl.amt                 as refundAmount,
       DATE_FORMAT(dtl.trans_time,'%Y-%m-%d %H:%i:%s')
                               as refundFlowTradeDate,
       dtl.opp_acc_name        as refundRemitter,
       dtl.opp_acc_no          as refundRemitterAccount,
       dtl.Remark              as refundFlowSummary,
       dtl.REFUND_BLEND_BY     as refundFlowBlendingUser, -- 勾兑人，待二开
       DATE_FORMAT(dtl.REFUND_BLEND_TIME,'%Y-%m-%d %H:%i:%s')
                               as refundFlowBlendingDate, -- 勾兑时间，待二开
       null                    as revokePaymentReason
from erp_payinfo erp
    inner join  dsl_all_bill_view n on n.erp_id = erp.id
    left join bis_exc exc on exc.cbs_bill_id = n.id
    left join bis_acc_dtl dtl ON (n.bank_serial_no <> '' and (dtl.bank_serial_no = n.bank_serial_no or dtl.id = n.bank_serial_no))
    left join nis_bill nb on nb.pay_id = n.id
    left join ab_accrecords aa on aa.id = nb.bill_id
    left join ab_voucherinfo av on av.id = aa.vid
    left join DSL_INTEFACE_FLAG flag ON flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'R')
    AND flag.TABLE_SOURCE = 'dsl_payment_result'
where 1=1
  AND dtl.id is not null
  AND dtl.blend_sign = '1'
  AND IFNULL(flag.SYNC_STATUS,'F') = 'F'
;

##创建人:lijq
##创建时间:2024年4月29日

CREATE TABLE IF NOT EXISTS `DSL_INTERFACE_LOG` (
                                     `ID`                         varchar(32) COLLATE utf8_bin NOT NULL COMMENT '主键id',
                                     `SOURCE_ID`                   VARCHAR(50) COLLATE utf8_bin DEFAULT NULL COMMENT '来源主键',
                                     `TYPE`                       varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '日志类型（1:成功、0:失败）',
                                     `INTERFACE_NAME`             varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '接口名称',
                                     `INTERFACE_CODE`             varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '接口编号',
                                     `LOG_TIME`                   DATETIME COLLATE utf8_bin DEFAULT NULL COMMENT '日志时间',
                                     `LOG`                        longtext COLLATE utf8_bin COMMENT '接口日志',
                                     PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='ERP接口日志表';

## 创建接口日志记录表菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '870b09b5b005407e940b381df196abb8', null, '接口日志记录表', '接口日志记录表', 'c1fb5fbb835e4303852804517e8ea6c9', null, 500, 1, '/dataexchange/interfacelog/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '870b09b5b005407e940b381df196abb8' LIMIT 1
    );
##GOGO

##修改内容:增加数据字典
##修改时间:2024-05-07
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select 'ef569124d9b943de972d47f95902d890', 'dsl.interface.logType', '接口日志类型' from dual
where not exists (select 1 from sys_dict_group where id ='ef569124d9b943de972d47f95902d890' limit 1);
##GOGO

##修改内容:增加数据字典
##修改时间:2024-05-07
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
select '5c55b75ec6f04ac6b927366b05691007', 'DSL_PAYMENT_SYCN','付款信息同步接口', 'ef569124d9b943de972d47f95902d890',1 from dual
where not exists (select 1 from sys_dict where id ='5c55b75ec6f04ac6b927366b05691007' limit 1);
##GOGO

##修改内容:增加数据字典
##修改时间:2024-05-07
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
select '5c55b75ec6f04ac6b927366b05691opi', 'DSL_PAYMENT_RESULT','付款交易结果回传接口', 'ef569124d9b943de972d47f95902d890',1 from dual
where not exists (select 1 from sys_dict where id ='5c55b75ec6f04ac6b927366b05691opi' limit 1);
##GOGO

INSERT INTO SYS_USER (ID, BEGIN_USE_TIME, CODE, EMAIL, IS_ADMIN, LOGIN_NAME, PASSWORD, PHONE, SALT, SEX, STATUS,
                      USERNAME, CORP_ID, USER_CADN, LOGIN_TYPE, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY,
                      SHOW_REPORT, SYS_SKIN)
select 'scheduler', null, null, null, 0, 'scheduler', '0c50613d4b81b63c38c64bd9cbd3458d', null,
        '524e789729a743dda0e36bbdfd861e87', 1, 4, '系统发起', '402880425b8abcfb015b8ac16e980000', null, 0, null, null,
        null, null, 0, null from dual
where not exists(select 1 from sys_user where id='scheduler' limit 1);
##GOGO

##修改人:zhaohc
##修改内容:变更定时任务执行器
##修改时间:2024-06-03
-- 变更执行器位置
update xxl_job_info set job_group = 14 where executor_handler = 'BisExcService';
update xxl_job_info set job_group = 14 where executor_handler = 'BisGatherCmdService';
##GOGO

##修改内容:增加国际化
##修改时间:2024-06-07
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '24bdaeab247211ef8c8f005056a84efa', 'zh_CN', '跨行标志有误', 'nis.netbank.message.difBank.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.netbank.message.difBank.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
##修改人:jiangw
##修改内容:增加菜单-统计报表-结算管理-月度资金划拨明细表
##修改时间:2024-06-07
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT 'eb216f837d9f49849c2564d39d47cec7', '', '月度资金划拨明细表', '月度资金划拨明细表', '07cbbb8132de427eb53c9d624fb7ee22', null, 20, 1, '/report/monthFundAllocation/list', null, null, null, null, null, null, 0 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'eb216f837d9f49849c2564d39d47cec7' LIMIT 1
);
##GOGO

##修改人:jiangw
##修改内容:增加菜单资源 查询资源
##修改时间:2024-06-07
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '9969471136254f95bebe972c677f2786', '月度资金划拨明细表查询资源', '月度资金划拨明细表查询资源', 'month:fund:allocation:report:query', 1, 'eb216f837d9f49849c2564d39d47cec7' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '9969471136254f95bebe972c677f2786' LIMIT 1
);
##GOGO

##修改人:jiangw
##修改内容:增加菜单-统计报表-结算管理-月度资金划拨汇总报表
##修改时间:2024-06-07
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT '76889958864e4cc8b1e8e64eb2648888', '', '月度资金划拨汇总报表', '月度资金划拨汇总报表', '07cbbb8132de427eb53c9d624fb7ee22', null, 21, 1, '/report/monthFundAllocationCollect/list', null, null, null, null, null, null, 0 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '76889958864e4cc8b1e8e64eb2648888' LIMIT 1
);
##GOGO

##修改人:jiangw
##修改内容:增加菜单资源 查询资源
##修改时间:2024-06-07
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '5fba423512a9417fa52d1bbd28607295', '月度资金划拨汇总报表查询资源', '月度资金划拨汇总报表查询资源', 'month:fund:allocation:collect:report:query', 1, '76889958864e4cc8b1e8e64eb2648888' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '5fba423512a9417fa52d1bbd28607295' LIMIT 1
);
##GOGO

##修改人:jiangw
##修改内容:修改信息提醒内容-存出保证金审批打回
##修改时间:2024-06-07
update sys_notification_category set CONTENT='您有一笔存入保证金被审批打回,申请单号为${billNo},金额为$number.formatNumeric(${bondMoney}),请处理！', NAME='存入保证金审批打回' where id='faea227b7dc54c2badc7748ae1efe6b4';
update sys_internation set LANG_CONTEXT='存入保证金审批打回' where KEY_ID='faea227b7dc54c2badc7748ae1efe6b4' and LANG_CODE='zh_CN';
##GOGO

##修改人:jiangw
##修改内容:修改信息提醒内容-存出保证金审批通过
##修改时间:2024-06-07
update sys_notification_category set CONTENT='您有一笔存入保证金已审批通过,申请单号为${billNo},金额为$number.formatNumeric(${bondMoney}),请阅知！', NAME='存入保证金审批通过' where id='a1dffe7be67a452d9100d1937f07b159';
update sys_internation set LANG_CONTEXT='存入保证金审批通过' where KEY_ID='a1dffe7be67a452d9100d1937f07b159' and LANG_CODE='zh_CN';
##GOGO


##修改人:zhaohc
##修改内容:变更定时任务执行器
##修改时间:2024-06-07
-- 添加执行器
INSERT INTO xxl_job_group (id, app_name, title, address_type, address_list, update_time)
select 18, 'stream-line-flow-keeper', 'workflowjob', 0, null, now() from dual
where not exists (select 1 from xxl_job_group where app_name ='stream-line-flow-keeper' limit 1);
##GOGO
-- 变更执行器位置
update xxl_job_info set job_group = 4 where executor_handler = 'BisAccDtlBudgetRuleService';
update xxl_job_info set job_group = 4 where executor_handler = 'BisAccDtlBudgetRulePayService';
update xxl_job_info set job_group = 18 where executor_handler = 'DelegationService';
##GOGO

## 完善指令失败消息提醒
update SYS_NOTIFICATION_CATEGORY set content='你有一张$!number.formatNumeric($!{billMoney})元的$!dictTag.get(''AB_BILL_TYPE'',$!{billType})$!{signerType}指令处理失败($!dictTag.get(''AB_BILL_FLAG'',$!{billFlag}))待处理！'
where  tag='bill:accebill:instructionmanage';
##GOGO

##修改人:zhaohc
##修改内容:添加定时任务脚本-资金调拨流水自动认领
##修改时间:2024-06-13
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 14, '资金调拨流水自动认领', now(), now(), 'XXL', '', 'CRON', '0 0 1,15 * * ?', 'DO_NOTHING', 'FIRST', 'fundTansferFlowAutoClaimJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='资金调拨流水自动认领' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-05-16
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f1a9f240e46e496b8914f73cbd81cd54', 'zh_CN', '付款指令中间状态停留超时预警', 'config.nis.consumptionOverTime' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='config.nis.consumptionOverTime' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-05-16
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a5f71bf308ae4b93b38a05db4d2cd851', 'zh_CN', '说明：付款指令长时间处于中间状态（指令已消费）的付款数据提醒', 'config.nis.consumptionOverTime.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='config.nis.consumptionOverTime.desc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

INSERT INTO sys_task (ID, TASKNAME, ACTIVE, DELETEDSW, TYPE, COMMAND, ARGUMENTS, PERIOD, SECOND, MINUTE, HOUR, DAYS, MONTH, WEEK, STARTDTM, ENDDTM, TASKOWNER, EMAIL_REMINDER)
SELECT 'b1953e460bda46bc93fffc3197eeb791', '付款指令中间状态停留超时预警', 1, 0, null, 'com.bytter.biz.bdlink.service.BisExcService', 'netbank:bdlink:bisexc:consumption:overtime', 1, '0', '0', '9', '*', '*', '?', to_date('1-1-2017', 'dd-mm-yyyy'), to_date('1-1-2050', 'dd-mm-yyyy'), '', '' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_task WHERE ID = 'b1953e460bda46bc93fffc3197eeb791' LIMIT 1
);
##GOGO

INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT '27bb970cbdae4d96a2a8ec7e0efd0d25', '61f6ecd439634bd19566b520656f7f7d', '付款指令中间状态停留超时预警', 'netbank:bdlink:bisexc:consumption:overtime', 'f1445907f43944e68b8fb7ce844daf28', '/bdlink/bisexc/listTrace', null, 'netbank:bdLink:bisExc:traceApply', 0, 12, '您有一笔金额为$!currencyTag.getCurSign($!{curId})$!{payMoney}的付款未处理成功，请检查！', 2 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category WHERE ID = '27bb970cbdae4d96a2a8ec7e0efd0d25' LIMIT 1
);
##GOGO

insert into sys_config (ID, TYPE, NAME, VALUE)
SELECT '2c5c283357c4416eb40398dff96c903d', 'nis', 'consumptionOverTime', '1' from dual
WHERE NOT EXISTS (
    SELECT 1 FROM sys_config WHERE type = 'nis' and name = 'consumptionOverTime' LIMIT 1
);
##GOGO

##zhoup
##新增银企上线占比表查询资源
##2024-06-14
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '8b4a633cbbdd4d4b98f9c326b4930608', '银企上线占比表查询', '银企上线占比表查询', 'report.enterpriseBankGoOnline.query', 1, '42a34d180bad4cd782c633b33084602b' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID ='8b4a633cbbdd4d4b98f9c326b4930608' LIMIT 1
);
COMMIT;
##GOGO

##zhoup
##新增资金收支表查询资源
##2024-06-14
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '1bf30718e19b4ac89738c0d992f234bc', '资金收支表查询', '资金收支表查询', 'report.incomeAndExp.query', 1, '4e48bc81fa754c40874571474390404f' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID ='1bf30718e19b4ac89738c0d992f234bc' LIMIT 1
);
COMMIT;
##GOGO



##修改人：zhaohc
##修改内容：修改消息分类
##修改时间：2024-06-15
update SYS_NOTIFICATION_CATEGORY set RESOURCE_CONTENT = 'cho:productReq:choProductReq:query' where id = 'bbe7a391d9cc4501a73c5cfa6708e9d6';
update sys_notification_category set CONTENT = '您有一笔账号为${bankAcc}的银行保证金,将于$dateTag.date2StrCNY($!{endDate})到期！' where id = '7c431cc5b9f24d39b24dddf601a23e9b';
##定时任务执行参数
update xxl_job_info set executor_param = 'chowmatistic:choproductreq:expire' where id = 294;
##GOGO


## 当审批流网关直接通过不走审批任务 默认传递用户
INSERT INTO SYS_USER (ID, BEGIN_USE_TIME, CODE, EMAIL, IS_ADMIN, LOGIN_NAME, PASSWORD, PHONE, SALT, SEX, STATUS,
                      USERNAME, CORP_ID, USER_CADN, LOGIN_TYPE, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY,
                      SHOW_REPORT, SYS_SKIN)
select 'bpmAuto', null, null, null, 0, 'bpmAuto', '0c50613d4b81b63c38c64bd9cbd3458d', null,
       '524e789729a743dda0e36bbdfd861e87', 1, 4, '系统发起', '402880425b8abcfb015b8ac16e980000', null, 0, null, null,
       null, null, 0, null from dual
where not exists(select 1 from sys_user where id='bpmAuto' limit 1);
##GOGO


##修改人：qijh
##修改时间：2024.06.15
##修改原因：需求下拨自动记账脚本

##付款需求清单增加记账标志
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'TALLY_SIGN') THEN
      ALTER TABLE NIS_DEMAND ADD COLUMN TALLY_SIGN varchar(1);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##付款需求清单增加记账凭证号码
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'VOUCHER_NO') THEN
      ALTER TABLE NIS_DEMAND ADD COLUMN VOUCHER_NO varchar(40);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改内容:增加字段语句 共享计划付款日期 SHARING_DATE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'SHARING_DATE') THEN
      ALTER TABLE NIS_BILLHEAD ADD COLUMN SHARING_DATE DATETIME;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##手续费比例字段长度调整 zhengg 20240618
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_ACCRECORDS' AND COLUMN_NAME = 'FEEMONEY_SCALE') THEN
ALTER TABLE AB_ACCRECORDS ADD COLUMN FEEMONEY_SCALE decimal(8,6) null comment '手续费比例';
ELSE
ALTER TABLE AB_ACCRECORDS MODIFY COLUMN FEEMONEY_SCALE decimal(8,6) null comment '手续费比例';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：qijh
##修改时间：2024.06.18
##修改原因：增加字段是否为第三方平台

##修改内容:增加字段语句 是否为第三方平台 IS_THIRD_PARTY
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_BANK_TYPE' AND COLUMN_NAME = 'IS_THIRD_PARTY') THEN
      ALTER TABLE BT_BANK_TYPE ADD COLUMN IS_THIRD_PARTY char(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改内容:增加字段语句 绑定账号 BINDING_BANK_ACC
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'BINDING_BANK_ACC') THEN
      ALTER TABLE BT_BANK_ACC ADD COLUMN BINDING_BANK_ACC varchar(32);
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO



##保证金占用表保证金金额字段长度调整 zhengg 20240619
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'FC_BOND_APPLY' AND COLUMN_NAME = 'APPLY_MONEY') THEN
ALTER TABLE FC_BOND_APPLY ADD COLUMN APPLY_MONEY decimal(18,2) null comment '占用金额';
ELSE
ALTER TABLE FC_BOND_APPLY MODIFY COLUMN APPLY_MONEY decimal(18,2) null comment '占用金额';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##保证金占用表保证金比例字段长度调整 zhengg 20240619
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'FC_BOND_APPLY' AND COLUMN_NAME = 'BOND_PERCENT') THEN
ALTER TABLE FC_BOND_APPLY ADD COLUMN BOND_PERCENT decimal(8,6) null comment '保证金比例';
ELSE
ALTER TABLE FC_BOND_APPLY MODIFY COLUMN BOND_PERCENT decimal(8,6) null comment '保证金比例';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人:zhaohc
##修改时间:2024.06.19
##修改内容:添加定时任务
##新增定时任务:计息到期提醒
INSERT INTO SYS_NOTIFICATION_CATEGORY  (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT '3893b314eb0a41d1b8060dc25d51bd05', '3b7d5736b6ab45dba3f1193ae2990694', '计息到期提醒', 'finacial:interest:calcbybatch:hand', 'c4982b15a5c74bb89e051bd05f313953', '/finacial/interest/fcinterest/listBatchInterest', null, '', 1, 400, '您在$!{interestTime}有计息业务待处理，请阅悉！', 2 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_NOTIFICATION_CATEGORY WHERE ID ='3893b314eb0a41d1b8060dc25d51bd05' LIMIT 1
);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b16d5d71a40b42ffa11c3212c72cf302', 'zh_CN', '计息提前提醒天数', 'finacial.interest.warn' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='finacial.interest.warn' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into SYS_CONFIG (ID, TYPE, NAME, VALUE)
SELECT '82187bb4777c4ba685128f211a53d429', 'ist', 'insWarnDays', '0' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM SYS_CONFIG WHERE ID='82187bb4777c4ba685128f211a53d429' LIMIT 1);
##GOGO
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 17, '计息到期提醒', now(), now(), 'XXL', '', 'CRON', '0 0 10,15 * * ?', 'DO_NOTHING', 'FIRST', 'FcInterestService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='计息到期提醒' limit 1);
##GOGO
update xxl_job_info set job_desc = '根据流水自动更新离线账户每日余额' where executor_handler = 'BisAccBalAutoService';
##GOGO

##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '846f154885924152afbee6289ba16219', 'zh_CN', '批量设置核查', 'bisAccDtlCheck.button.batchCheck' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bisAccDtlCheck.button.batchCheck' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2181ebacb7a9400fbb39105ce7b81f2d', 'zh_CN', '批量取消核查', 'bisAccDtlCheck.button.batchUnCheck' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bisAccDtlCheck.button.batchUnCheck' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '410e80c0d0af4d89bb0281776ed99d16', 'zh_CN', '设置已处理', 'common.button.batchHandled' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='common.button.batchHandled' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7371696af8864533b324d8bab7edec37', 'zh_CN', '设置未处理', 'common.button.batchUnHandled' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='common.button.batchUnHandled' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '525c5fcef4e54eda9ab21660329242f4', 'zh_CN', '确定设置已处理？', 'common.message.confirm.batchHandled' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='common.message.confirm.batchHandled' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '76c1ef35b4eb40039ad6334af737027f', 'zh_CN', '确定设置未处理?', 'common.message.confirm.batchUnHandled' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='common.message.confirm.batchUnHandled' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b520cdd435ad4457bf1d0ed5f56480d2', 'zh_CN', '核查账户设置', 'netbank.button.setCheckBankAcc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.button.setCheckBankAcc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f53a1cf6b76c498ea0be354a2c0a2079', 'zh_CN', '核查余额', 'table.bisAccDtlCheck.checkBal' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bisAccDtlCheck.checkBal' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '5460869367f442739c55fd9ccd97c4d9', 'zh_CN', '异常业务日期', 'table.bisAccDtlCheck.checkDate' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bisAccDtlCheck.checkDate' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ae061db2b0d5480da78e4b9aef547f0b', 'zh_CN', '差额', 'table.bisAccDtlCheck.diffBal' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bisAccDtlCheck.diffBal' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9d4f3eef6cb04687be5aba8915699911', 'zh_CN', '是否核查', 'table.bisAccDtlCheck.isCheck' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bisAccDtlCheck.isCheck' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3bdba63f43754afd9e3bf479b0eba569', 'zh_CN', '已销户', 'table.bisAccDtlCheck.statusDel' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bisAccDtlCheck.statusDel' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-02-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7d0abdb146e44b85baee8c63649b6f90', 'zh_CN', '正常', 'table.bisAccDtlCheck.statusNormal' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bisAccDtlCheck.statusNormal' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典组"核查状态"
##修改时间：2023-02-23
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '6db120542dab4363b556c432cc484913', 'bisAccDtlCheck.status', '核查状态'from dual
where not exists (select 1 from sys_dict_group where ID ='6db120542dab4363b556c432cc484913' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"未处理"
##修改时间：2023-02-23
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '15d064bc44974f808c566070adf9dfb3', '0', '未处理',null,'6db120542dab4363b556c432cc484913',1 from dual
where not exists (select 1 from sys_dict where ID ='15d064bc44974f808c566070adf9dfb3' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"已处理"
##修改时间：2023-02-23
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'ddf053e5556440359ca4c8a741bcba03', '1', '已处理',null,'6db120542dab4363b556c432cc484913',2 from dual
where not exists (select 1 from sys_dict where ID ='ddf053e5556440359ca4c8a741bcba03' limit 1);
##GOGO

##银行账户表增加“是否核查”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'IS_CHECK') THEN
ALTER TABLE BT_BANK_ACC ADD COLUMN IS_CHECK VARCHAR(1) default '0';
ELSE
ALTER TABLE BT_BANK_ACC MODIFY COLUMN IS_CHECK VARCHAR(1) default '0';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO
##新增"余额流水异常核查表"
CREATE TABLE IF NOT EXISTS BIS_ACC_DTL_CHECK (
    ID                   VARCHAR(32)                    not null,
    CORP_ID              VARCHAR(32)                    not null,
    BANK_ACC             VARCHAR(30),
    CUR_ID               VARCHAR(32),
    CHECK_DATE           DATE,
    BAL                  DECIMAL(15,2),
    CHECK_BAL            DECIMAL(15,2),
    STATUS               VARCHAR(1)                    default '0',
    CREATE_BY     VARCHAR(32),
    CREATE_DATE   DATETIME(6),
    UPDATE_BY     VARCHAR(32),
    UPDATE_DATE   DATETIME(6),
    constraint PK_BIS_ACC_DTL_CHECK primary key (ID)
    ) COMMENT '余额流水异常核查表';
##GOGO

CREATE TABLE IF NOT EXISTS `BILL_CONSUMER_RECORD` (
    MSG_ID VARCHAR(36) NOT NULL COMMENT '消息ID',
    BUSINESS_TYPE VARCHAR(100) NOT NULL COMMENT '业务类型',
    BUSINESS_ID VARCHAR(100) NOT NULL COMMENT '业务ID',
    MESSAGE TEXT NOT NULL COMMENT '消息内容',
    MSG_HANDLE_STATUS VARCHAR(100) NOT NULL COMMENT '消息处理状态',
    EXCEPTION_MSG TEXT DEFAULT NULL COMMENT '异常日志',
    CREATE_TIME TIMESTAMP COMMENT '创建时间',
    UPDATE_TIME TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY(MSG_ID)
    ) COLLATE = utf8mb3_bin COMMENT ='消费者消息记录表(监听付款推送给票据的消息)';
##GOGO

##新增菜单"结算管理>>账户管理>>银行账户管理>>余额流水异常核查"
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT '68f22a8f13f04e5faa2db2c89685a66d', '7', '余额流水异常核查', '余额流水异常核查', 'd5b09cb914304f8987cb8e6e0ecb3ff3', null, 7, 1, '/bankacc/bisaccdtlcheck/list', null, null, null, null, null, null, 0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = '68f22a8f13f04e5faa2db2c89685a66d' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'c0abbed849b248a5ab43db4efdd6c11e', '查询资源', '查询资源', 'netbank:bankAcc:bisAccDtlCheck:query', 1, '68f22a8f13f04e5faa2db2c89685a66d' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources WHERE ID = 'c0abbed849b248a5ab43db4efdd6c11e' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'b202da00bd034407b5baf50dde37d789', '应用资源', '应用资源', 'netbank:bankAcc:bisAccDtlCheck:apply', 1, '68f22a8f13f04e5faa2db2c89685a66d' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources WHERE ID = 'b202da00bd034407b5baf50dde37d789' LIMIT 1
);
##GOGO

## 新增消息分类
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT 'b85acc5e482448b791f037dacd923b98', 'e583ddaa25ed45098c672e99bf0c961a', '银行账户余额流水数据异常提示', 'netbank:bankacc:dtl:check',
       '68f22a8f13f04e5faa2db2c89685a66d', '/bankacc/bisaccdtlcheck/list', null, 'netbank:bankAcc:bisAccDtlCheck:apply', 0, 15, '开户单位$!{corpName}，银行账号为$!{bankAcc}的账户，$!{checkDate}日余额或流水存在异常，请核查！', 2 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category WHERE ID = 'b85acc5e482448b791f037dacd923b98' LIMIT 1
);
##GOGO
## 新增定时任务(银行账户余额流水异常核查)
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 11, '银行账户余额流水异常核查', now(), now(), 'XXL', '', 'CRON', '0 0 8 * * ?', 'DO_NOTHING', 'FIRST', 'BisAccDtlCheckService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='银行账户余额流水异常核查' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-03-18
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '5b4b752e651241929ac56741d3361781', 'zh_CN', '确定设置核查账户吗？', 'netbank.message.confirm.batchCheck' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.message.confirm.batchCheck' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-03-18
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c06200f8e6a14439aff05be682c5c38e', 'zh_CN', '确定取消账户核查吗？', 'netbank.message.confirm.batchUnCheck' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.message.confirm.batchUnCheck' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：linqp
##修改时间：2024.06.19
##修改原因：添加贷款到期提醒的任务
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 2, '内部贷款到期展期提醒', now(), now(), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'CmsLoanSendService', 'loanDeferredWarn', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='贷款到期提醒' limit 1);
##GOGO

##修改人：linqp
##修改时间：2024-06-19
##修改原因：修改内部借款到期提醒
update xxl_job_info set executor_param='innerLoanExpirationWarn' where id=333;
##GOGO

insert into sys_config (ID, TYPE, NAME, VALUE)
SELECT 'f0fd0bc8f9784c7e9633eb0fd2bd2665', 'cms', 'loanDeferredWarn', '10' from dual
WHERE NOT EXISTS (SELECT 1 FROM sys_config WHERE type = 'cms' and name = 'loanDeferredWarn' LIMIT 1);
COMMIT;
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8403121dced04ef38141af0cec20e9a5', 'zh_CN', '贷款到期展期提醒提前天数', 'cms.loan.deferred.warn' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_CODE = 'zh_CN' and LANG_KEY = 'cms.loan.deferred.warn' LIMIT 1);

##修改人：linqp
##修改时间：2024-06-20
##修改原因：添加表
CREATE TABLE IF NOT EXISTS `SYS_NOTIFICATION_EXT` (
      ID          VARCHAR(32)  NOT NULL  PRIMARY KEY,
      CATEGORY_ID VARCHAR(32)  NULL,
      USER_ID     VARCHAR(32)  NULL,
      CORP_ID     VARCHAR(32)  NULL,
      TYPE       VARCHAR(32)   NULL COMMENT '企业微信:WX',
      BUSINESS_ID VARCHAR(32)  NULL,
      CREATE_DATE DATETIME     NULL,
      CONTENT     VARCHAR(1024) NULL COMMENT '消息内容',
      SEND_BODY   VARCHAR(4096)  NULL COMMENT '请求体',
      RESPONE_BODY VARCHAR(4096)  NULL COMMENT '返回报文体',
      RESULT VARCHAR(1024)  COMMENT '结果'
) COLLATE = utf8mb3_bin ;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'SYS_NOTIFICATION_CATEGORY' AND COLUMN_NAME = 'WEIXIN_IND') THEN
        ALTER TABLE SYS_NOTIFICATION_CATEGORY ADD COLUMN WEIXIN_IND  SMALLINT;
    ELSE
        ALTER TABLE SYS_NOTIFICATION_CATEGORY MODIFY COLUMN WEIXIN_IND SMALLINT ;
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##修改内容:增加国际化
##修改人：linqp
##修改时间：2024-06-20
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c11c04192de111ef8c8f005056a84efa', 'zh_CN', '推送企业微信', 'sys.notification.push.weixin' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.notification.push.weixin' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改日期：2024-06-22
##修改人员：fangcf
##修改内容：授信管理

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0a50d561bad94cd593690ff42115f339', 'zh_CN', '授信合同是否必须', 'btBankType.creditContract' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'btBankType.creditContract' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##银行类型表增加“授信合同是否必须”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_TYPE' AND COLUMN_NAME = 'CREDIT_CONTRACT_REQUIRED') THEN
      ALTER TABLE BT_BANK_TYPE ADD COLUMN CREDIT_CONTRACT_REQUIRED char(1) comment '授信合同是否必须';
ELSE
    ALTER TABLE BT_BANK_TYPE MODIFY COLUMN CREDIT_CONTRACT_REQUIRED char(1) comment '授信合同是否必须';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3b8a6fdef48647d782c87b8ae142cfd9', 'zh_CN', '合同附件', 'table.ratBkration.contractAttach' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.ratBkration.contractAttach' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '8dfc807ed8d449508dcddd4a7901f081', 'zh_CN', '请上传合同附件', 'table.ratBkration.contractAttachFilenull' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.ratBkration.contractAttachFilenull' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##授信额度录入表增加“合同附件”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'RAT_BKRATION' AND COLUMN_NAME = 'CONTRACT_ATTACHFILEPATH') THEN
      ALTER TABLE RAT_BKRATION ADD COLUMN CONTRACT_ATTACHFILEPATH varchar(32) comment '合同附件';
ELSE
    ALTER TABLE RAT_BKRATION MODIFY COLUMN CONTRACT_ATTACHFILEPATH varchar(32) comment '合同附件';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '400acc6494184fd08c5e7f3f55caa098', 'zh_CN', '授信合同必须控制', 'cms.config.creditContract.control' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.config.creditContract.control' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '28cbf5951e764447af8334f6f6f13769', 'zh_CN', '未上传合同附件，还继续提交嘛？', 'table.ratBkration.contractAttachFile.noUpload' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.ratBkration.contractAttachFile.noUpload' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c4c7e6438482417990b88590157a008c', 'zh_CN', '上传附件', 'common.button.uploadAttachFile' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.button.uploadAttachFile' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##新增【授信合作银行维护】菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '47ef3099904245f0a47b9e82343b1577', null, '授信合作银行维护', '授信合作银行维护', '5826504d04f448eea95c3a9ba881a1e6', null, 6, 1, '/cms/rat/creditCooperationBank/list', null, null, null, null, null, null, 0, 1, null FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '47ef3099904245f0a47b9e82343b1577' LIMIT 1
);
##GOGO

#新增【授信合作银行维护】菜单资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY)
SELECT 'a7f90e232d4042dcb3859667f3a72b60', '应用资源', '应用资源', 'cms:rat:creditCooperationBank:apply', 1, '47ef3099904245f0a47b9e82343b1577', NULL, '402880425b6b0a58015b6b0abeb10000', NULL, NULL FROM DUAL
 WHERE NOT EXISTS (
	SELECT 1 FROM sys_resources WHERE id ='a7f90e232d4042dcb3859667f3a72b60' LIMIT 1
);
##GOGO

##增加授信合作银行维护表
CREATE TABLE IF NOT EXISTS RAT_CREDIT_COOPERATION_BANK
(
   ID                   VARCHAR(32) NOT NULL COMMENT '主键',
   BANK_CODE			VARCHAR(32) COMMENT '开户行行号',
   BANK_NAME       		VARCHAR(200) COMMENT '开户行名称',
   COOPERATION_TYPE		VARCHAR(2) COMMENT '授信银行合作情况：0-未合作、1-已合作、2-计划合作',
   CREATE_BY            VARCHAR(32) COMMENT '创建人',
   CREATE_DATE         	DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(32) COMMENT '修改人',
   UPDATE_DATE         	DATETIME COMMENT '修改时间'
);
ALTER TABLE RAT_CREDIT_COOPERATION_BANK COMMENT '授信合作银行维护表';
##GOGO

##增加数据字典【授信银行合作情况】
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '24ae9f66a0ea443193504483ff4ebb9a', 'rat.cooperation.type', '授信银行合作情况', null, null FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '24ae9f66a0ea443193504483ff4ebb9a' LIMIT 1
);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '719cb5dcf8544f0da3d63871fe64a5db', '0', '未合作', '24ae9f66a0ea443193504483ff4ebb9a', '1' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '719cb5dcf8544f0da3d63871fe64a5db' LIMIT 1
);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'ce425ee5aefb497dba707510cd327d95', '1', '已合作', '24ae9f66a0ea443193504483ff4ebb9a', '2' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'ce425ee5aefb497dba707510cd327d95' LIMIT 1
);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'a6bf8f41a22b4c1bbd762d592813b522', '2', '计划合作', '24ae9f66a0ea443193504483ff4ebb9a', '3' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'a6bf8f41a22b4c1bbd762d592813b522' LIMIT 1
);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '836656edadfe4b829a055ec33329a0b1', 'zh_CN', '授信银行名称', 'table.ratBkration.creditBankName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.ratBkration.creditBankName' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'fd1c94a616444831ac72adbefa5bd818', 'zh_CN', '合作情况', 'table.ratBkration.cooperationType' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.ratBkration.cooperationType' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3615dff8190f4872b5fb0f9e517347d7', 'zh_CN', '该授信银行已存在合作记录，不能重复新增！', 'cms.rat.creditcooperationbank.exist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.creditcooperationbank.exist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '314df56382fe43f4a159cb0d84fb2b7c', 'zh_CN', '授信银行不能为空', 'cms.rat.creditcooperationbank.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.creditcooperationbank.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd06aed6df10a412ca81f7ae875f76c60', 'zh_CN', '合作情况不能为空', 'cms.rat.cooperationtype.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.cooperationtype.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3397ccf29ca844a69409017e9322a33f', 'zh_CN', '合作情况不存在，请参照对应注释填入对应值', 'cms.rat.cooperationtype.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.cooperationtype.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改日期:2024-06-24
##修改人员:zhaohc
##修改内容:添加定时任务"历史消息清理"
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 15, '历史消息清理', now(), now(), 'XXL', '', 'CRON', '0 0 0 1 * ?', 'DO_NOTHING', 'FIRST', 'NotificationService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='历史消息清理' limit 1);
##GOGO


##修改人：linqp
##修改内容：增加国际化
##修改时间：2023-12-08
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '5c0e3e039d7d11ee917ffefcfe637e7a', 'zh_CN', '消息提醒对象', 'sys.notification.notify.object' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.notification.notify.object' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：linqp
##修改内容：增加国际化
##修改时间：2023-12-08
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '67be2ed29d7d11ff917ffefcfe637e7a', 'zh_CN', '按用户发送', 'sys.notification.userSend' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.notification.userSend' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：linqp
##修改内容：增加字段
##修改时间：2023-12-08
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'SYS_NOTIFICATION_CATEGORY' AND COLUMN_NAME = 'NOTIFY_OBJECT_TYPE') THEN
    ALTER TABLE SYS_NOTIFICATION_CATEGORY ADD COLUMN NOTIFY_OBJECT_TYPE  INT COMMENT '提醒对象类型 0-所有角色；1-按角色发送； 2-按用户发送';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：linqp
##修改内容：增加表
##修改时间：2023-12-08
CREATE TABLE IF NOT EXISTS SYS_NOTIFICATION_USER (
    ID varchar(32) not null,
    CATEGORY_ID varchar(32),
    user_ID varchar(32),
    PRIMARY KEY (ID),
    KEY sys_notification_user_CATEGORY_ID_IDX (CATEGORY_ID) USING BTREE,
    KEY sys_notification_user_user_ID_IDX (user_ID) USING BTREE
    );
##GOGO

##修改日期:2024-06-26
##修改人员:linqp
##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'fb2c2e56338911efbeed005056a84efa', 'zh_CN', '收入金额', 'table.btBankAccDtl.amtIn' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.btBankAccDtl.amtIn' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '004b5bed338a11efbeed005056a84efa', 'zh_CN', '支出金额', 'table.btBankAccDtl.amtOut' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.btBankAccDtl.amtOut' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO


##修改人：qijh
##修改时间：2024.06.27
##修改原因：增加银行账号绑定账号中间表 nis_binding_bankacc

##新增“银行账号绑定账号中间表”
CREATE TABLE IF NOT EXISTS NIS_BINDING_BANKACC (
    BANK_ID                  VARCHAR(32),
    BINDING_BANK_ID          varchar(32)
) COMMENT '银行账号绑定账号中间表';
##GOGO

##修改“企业微信-下拨资金付款失败提醒”
update sys_notification_category set CONTENT='业务类型:资金调拨\\n业务描述:$!{businessDescription}\\n付款单位:$!{payerCorp}\\n付款账号:$!{payerBankAcc}\\n付款银行类型:$!{payerBankType}\\n收款单位:$!{payeeCorp}\\n收款账号:$!{payeeBankAcc}\\n收款银行类型:$!{payeeBankType}\\n金额:$!{payMoney}\\n失败原因:$!{returnMsg}' where id='cbd0388493934a9785bb67349394e9c3';
##GOGO

##修改“网银U-key到期提醒”
update sys_notification_category set CONTENT='业务类型:账户管理\\n业务描述:账户Ukey到期更新\\n到期日期:$!{expireDate}\\n账号银行:$!{bankTypeName}\\nUkey编号:$!{ukeyNo}' where id='e7246d21c3e14e50a98dd2ae8f99ec91';
##GOGO

##修改“付款成功提醒”
update sys_notification_category set CONTENT='您的单据号:$!{bursarBillNo}\\n付款方:$!{payerAccName}\\n收款方:银行账号-$!{payeeAcc}\\n收款银行户名-$!{payeeAccName}\\n金额:$!{payMoney}\\n用途:$!{purpose}\\n已付款成功。' where id='7ba91dd5753e4f3dbb8b2419da6677da';
##GOGO

##修改内容:增加字段语句 结算中心实际付款账号 REAL_PAYER_ACC_ID
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'REAL_PAYER_ACC_ID') THEN
      ALTER TABLE NIS_BILLHEAD ADD COLUMN REAL_PAYER_ACC_ID varchar(32);
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改人:qijh
##修改内容:添加定时任务脚本-自动发送付款执行
##修改时间:2024-07-2
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 14, '自动发送付款执行', now(), now(), 'XXL', '', 'CRON', '0 0 1,15 * * ?', 'DO_NOTHING', 'FIRST', 'AutoSendPaymentJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='自动发送付款执行' limit 1);
##GOGO

##修改日期：2024-07-05
##修改人员：fangcf
##修改内容：授信管理

##新增【年度融资规划登记】菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '75c536a3293a488ea67e38c70c913567', null, '年度融资规划登记', '年度融资规划登记', 'c7ddd7ff11f845589427a76347c2c77d', null, 9, 1, '/cms/rat/yearFinPlanRegister/list', null, null, null, null, null, null, 0, 1, null FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '75c536a3293a488ea67e38c70c913567' LIMIT 1
);
##GOGO

#新增【年度融资规划登记】菜单资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY)
SELECT '18c5226657b4460eaaf9da611cef4a75', '应用资源', '应用资源', 'cms:rat:yearFinPlanRegister:apply', 1, '75c536a3293a488ea67e38c70c913567', NULL, '402880425b6b0a58015b6b0abeb10000', NULL, NULL FROM DUAL
 WHERE NOT EXISTS (
	SELECT 1 FROM sys_resources WHERE id ='18c5226657b4460eaaf9da611cef4a75' LIMIT 1
);
##GOGO

#新增【年度融资规划登记】菜单资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY)
SELECT 'cb6636c6927d499f8c71cba39ce4140d', '查询资源', '查询资源', 'cms:rat:yearFinPlanRegister:query', 1, '75c536a3293a488ea67e38c70c913567', NULL, '402880425b6b0a58015b6b0abeb10000', NULL, NULL FROM DUAL
 WHERE NOT EXISTS (
	SELECT 1 FROM sys_resources WHERE id ='cb6636c6927d499f8c71cba39ce4140d' LIMIT 1
);
##GOGO

##增加年度融资规划登记表
CREATE TABLE IF NOT EXISTS RAT_YEARFIN_PLAN_REGISTER
(
   ID                   	VARCHAR(32) NOT NULL COMMENT '主键',
   CORP_ID					VARCHAR(32) COMMENT '单位代码',
   CORP_NAME				VARCHAR(200) COMMENT '单位名称',
   YEARS       				VARCHAR(10) COMMENT '年度',
   YEAR_PURCHASE_AMOUNT		decimal(18,2) COMMENT '年采购量',
   MERGE_INVESTMENT_AMOUNT  decimal(18,2) COMMENT '并购投资',
   SOLVE_MEASURE       		VARCHAR(1000) COMMENT '解决措施',
   EXIST_DIFFICULTY			VARCHAR(1000) COMMENT '存在的困难',
   STATUS					decimal(10,0) COMMENT '状态',
   CREATE_BY            	VARCHAR(32) COMMENT '创建人',
   CREATE_DATE         		DATETIME COMMENT '创建时间',
   UPDATE_BY            	VARCHAR(32) COMMENT '修改人',
   UPDATE_DATE         		DATETIME COMMENT '修改时间'
);
ALTER TABLE RAT_YEARFIN_PLAN_REGISTER COMMENT '年度融资规划登记表';
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'af9d5d93408d4562a505c9187389d911', 'zh_CN', '年度', 'table.yearFinPlanRegister.years' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.yearFinPlanRegister.years' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd01921a4a6754e50979d61aab76e1523', 'zh_CN', '年采购量', 'table.yearFinPlanRegister.yearPurchaseAmount' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.yearFinPlanRegister.yearPurchaseAmount' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '8c1b432e0e53480785dcf5cd3fc1d04c', 'zh_CN', '年采购量（大写）', 'table.yearFinPlanRegister.yearPurchaseAmountUpper' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.yearFinPlanRegister.yearPurchaseAmountUpper' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '851b47d2e9ef406392c1d69bc142bfe5', 'zh_CN', '并购投资', 'table.yearFinPlanRegister.mergeInvestmentAmount' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.yearFinPlanRegister.mergeInvestmentAmount' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'be8a66d1d0d446d1adecaee000ca72f4', 'zh_CN', '并购投资（大写）', 'table.yearFinPlanRegister.mergeInvestmentAmountUpper' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.yearFinPlanRegister.mergeInvestmentAmountUpper' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO


##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '80631a5bac8e4dfeba1048b17f5d295e', 'zh_CN', '解决措施', 'table.yearFinPlanRegister.solveMeasure' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.yearFinPlanRegister.solveMeasure' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e9d83730dfc04329b3e61dfa27713a53', 'zh_CN', '存在的困难', 'table.yearFinPlanRegister.existDifficulty' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.yearFinPlanRegister.existDifficulty' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '621a5d7b19604f538896420c4b25a12d', 'zh_CN', '单位名称不能为空', 'cms.rat.registercorpname.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.registercorpname.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '658ee8ac8e474c19868e82d7ea399023', 'zh_CN', '单位名称不存在', 'cms.rat.registercorpname.notexist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.registercorpname.notexist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '61dc40e8228e4fa692fc5c777a3c04f7', 'zh_CN', '用户不存在该单位权限', 'cms.rat.registercorpname.notauth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.registercorpname.notauth' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '860f21c07d984c878a308853160ec6b4', 'zh_CN', '年度不能为空', 'cms.rat.registeryears.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.registeryears.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1fa6ea2bbd944fdb945dbed55e0661b2', 'zh_CN', '年度格式错误', 'cms.rat.registeryears.formaterror' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.registeryears.formaterror' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '675d032d6d994da8876611fdfb490aae', 'zh_CN', '年采购量不能为空', 'cms.rat.registeryearpurchaseamount.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.registeryearpurchaseamount.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '414ef1e941164edbaa22230908164d25', 'zh_CN', '并购投资不能为空', 'cms.rat.registermergeinvestmentamount.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.registermergeinvestmentamount.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b592a2578e564da1b5faf10f0535fa7b', 'zh_CN', '解决措施填写文本长度不能超过500', 'cms.rat.registersolvemeasure.length' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.registersolvemeasure.length' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '221adc1bf5354fda8bbaa69b489d19ab', 'zh_CN', '存在的困难填写文本长度不能超过500', 'cms.rat.registerexistdifficulty.length' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'cms.rat.registerexistdifficulty.length' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##新增菜单“结算管理》资金调拨》资金需求清单查询”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT 'e8cfb8f69bd744e3b2a95360e76b9c2d', '', '资金需求清单查询', '资金需求清单查询', '2a164d43b1d042bdbd8eabe715fd72e4', null, 4, 1, '/netbank/nisdemand/listQuery', null, null, null, null, null, null, 0 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'e8cfb8f69bd744e3b2a95360e76b9c2d' LIMIT 1
);
##GOGO

##新增菜单资源 查询资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '0d44980104ea417187678e671e8b31ed', '资金需求清单查询资源', '查询资源', 'netbank:nisDemandQuery:query', 1, 'e8cfb8f69bd744e3b2a95360e76b9c2d' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '0d44980104ea417187678e671e8b31ed' LIMIT 1
);
##GOGO

##修改人:tongqr
##修改内容:修改字段长度
##修改时间:2024-06-28
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_LOAN_INFO' AND COLUMN_NAME = 'REPAY_TYPE') THEN
      ALTER TABLE CMS_LOAN_INFO ADD COLUMN REPAY_TYPE VARCHAR(32);
ELSE
    ALTER TABLE CMS_LOAN_INFO MODIFY COLUMN REPAY_TYPE VARCHAR(32);
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
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_LOAN_SEND' AND COLUMN_NAME = 'REPAY_TYPE') THEN
       alter table CMS_LOAN_SEND modify  REPAY_TYPE varchar(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##内部拆借申请表还款方式
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_INL_INFO' AND COLUMN_NAME = 'REPAY_TYPE') THEN
       alter table CMS_INL_INFO modify  REPAY_TYPE varchar(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##内部拆借发放表还款方式
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_INL_SEND' AND COLUMN_NAME = 'REPAY_TYPE') THEN
       alter table CMS_INL_SEND modify  REPAY_TYPE varchar(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2023-06-02
##修改人员:tongqr
##修改内容:增加审批流

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_INL_REPAYPLAN_CHANGE' AND COLUMN_NAME = 'REPAY_TYPE') THEN
ALTER TABLE CMS_INL_REPAYPLAN_CHANGE ADD COLUMN REPAY_TYPE  varchar(32);
ELSE
ALTER TABLE CMS_INL_REPAYPLAN_CHANGE MODIFY COLUMN REPAY_TYPE varchar(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
SELECT  'dd64c1f881e14a48b67da563e80bc435', '5a3270cba98144ccac5fa886d3454a1b', '内部资金拆借还款计划修改',
        'cmsInlRepayChange', '1', '/inl/change/listApprove', null, null, null, 0, 1, null, 1,
        'sys:flow:approve:cmsInlRepayChange', null, null, null, null, 'bytter-service-finance', NULL FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM act_extend_category WHERE ID='dd64c1f881e14a48b67da563e80bc435'  LIMIT 1
);
##GOGO

delete from act_extend_category where id='1729388183627317249' and CATE_KEY='cmsInlRepayChange';


CREATE TABLE `sys_user_corp_role` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL,
  `user_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `corp_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `role_id` varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sys_corp_user_role_ucr_key` (`user_id`,`corp_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户单位角色 目前主要用在工作流处';


INSERT IGNORE INTO sys_multi_lang
(ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
VALUES('9f417922f408492789b5a6b81fafdac1', 'zh_CN', '用户单位角色', 'sys.dual.userCorpRoleComponent', '2024-07-16 15:41:04', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL);

##修改日期：2025-03-10
##修改人员：zhous
##修改内容：保证金利息待同步数据视图
CREATE OR REPLACE VIEW DSL_AB_EMIT_BAIL_INTEREST_SYNC_VIEW AS
SELECT
    ae.id AS orderId,
    sc.code AS companyCode,
    sc.name AS companyName,
    ae.bank_acc as receiptAccount,
    ae.interest_margin as marginAmount,
    concat(ae.current_month,'-','01') as accountingTime
FROM ab_emit_bail_interest_info ae
         LEFT JOIN sys_corp sc on ae.corp_id = sc.id where ae.status ='1' and ae.push_status ='1' ;
##GOGO

INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 7, '保证金利息同步接口', now(), now(), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'DSLAbEmitBailInterestSync', '{
    "dsl_ab_emit_bail_interest_sync_url": " ",
    "dsl_ab_emit_bail_interest_sync_page_size": 30}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='保证金利息同步接口' limit 1);
##GOGO













