##修改人:zrs
##修改内容:新增银行账户导入，如果是票据账户，流通方式不能为空
##修改时间:2024-07-08
##增加国际化
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
VALUES ('37879be43d0511efbff9005056a84efa', 'zh_CN', '流通方式不能为空', 'nis.netbank.message.circulationMode.null');
##GOGO

##修改国际化
UPDATE sys_multi_lang SET LANG_CONTEXT = '利率浮动百分比格式不正确，最大值为1'
where LANG_CODE = 'zh_CN' and LANG_KEY = 'nis.netbank.message.ratesfloatper.format';
##GOGO

##修改人:zhaohc
##修改内容:添加资源表数据
##修改时间:2024-07-08
INSERT IGNORE INTO
sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('34fdbaaddaf741ec899b2f6aaa5f85d7', '费用报销', '费用报销', 'netbank:expense:nisExpenseDetail:handleQuery', 1,'4028802f5c2eac34015c2ec1b9c40036', NULL, NULL, NULL, NULL,NULL);
##GOGO
INSERT IGNORE INTO
sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('5b96e6f244bd479dad3529e21b1db26f', '指令查看资源', '指令查看资源', 'netbank:netBankQuery:nisBillHead:bisQuery',1, '4028802f5c2eac34015c2ec1b9c40036', '2024-06-12 19:04:01', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL);
##GOGO
INSERT IGNORE INTO
sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ( 'b391cf847730432285380e9c51cdc3fa', '', '同URL的permission', 'netbank:expense:nisExpenseDetail:query', 1,'4028802f5c2eac34015c2ec1b9c40036', '2024-06-13 19:35:45', '402880425b6b0a58015b6b0abeb10000', NULL, NULL,NULL);
##GOGO
INSERT IGNORE INTO
sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('f5faa0af19974926be93430f440ddb97', '付款执行,付款查询', '付款执行', 'netbank:netBank:nisBillHead:executeQuery',1, '4028802f5c2eac34015c2ec1b9c40036', NULL, NULL, NULL, NULL,'/netbank/nisbillhead/datatable');
##GOGO
##修改人:zrs
##修改内容:修改国际化
##修改时间:2024-07-09
UPDATE sys_multi_lang SET LANG_CONTEXT = '利率浮动百分比格式不正确，最大值为100'
where LANG_CODE = 'zh_CN' and LANG_KEY = 'nis.netbank.message.ratesfloatper.format';
##GOGO

UPDATE sys_multi_lang SET LANG_CONTEXT = '利率格式不正确，最大值为1'
where LANG_CODE = 'zh_CN' and LANG_KEY = 'nis.netbank.message.rates.format';
##GOGO

##修改人:qijh
##修改内容:修改字段类型,保留两位小数
##修改时间:2024-07-12
ALTER TABLE FC_BOND_BAL MODIFY COLUMN BOND_MONEY decimal(20,2) DEFAULT NULL COMMENT '存入金额';
ALTER TABLE FC_BOND_BAL MODIFY COLUMN ADD_MONEY decimal(20,2) DEFAULT NULL COMMENT '追加金额';
ALTER TABLE FC_BOND_BAL MODIFY COLUMN APPLY_MONEY decimal(20,2) DEFAULT NULL COMMENT '占用金额';
ALTER TABLE FC_BOND_BAL MODIFY COLUMN FREEZE_MONEY decimal(20,2) DEFAULT NULL COMMENT '在途占用金额';
ALTER TABLE FC_BOND_BAL MODIFY COLUMN BACK_MONEY decimal(20,2) DEFAULT NULL COMMENT '转回金额';

##增加数据字典【是否推送共享】
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '24ae9f66a0ea443kkjj74483ff4ebb9a', 'nisBillhead.isSendGx', '是否推送共享', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '24ae9f66a0ea443kkjj74483ff4ebb9a' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '719cb5dcf8544f0da3d6lpkj9e64a5db', 'S', '是', '24ae9f66a0ea443kkjj74483ff4ebb9a', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '719cb5dcf8544f0da3d6lpkj9e64a5db' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'ce42vbfg5efb497dba707510cd327d95', 'F', '否', '24ae9f66a0ea443kkjj74483ff4ebb9a', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'ce42vbfg5efb497dba707510cd327d95' LIMIT 1
    );
##GOGO

##增加字段语句 NIS_BILLHEAD 添加 sync_status
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'SYNC_STATUS') THEN
ALTER TABLE NIS_BILLHEAD ADD COLUMN SYNC_STATUS VARCHAR(1) NULL COMMENT '付款单是否推送共享';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##付款单是否推送共享同步状态初始化
##update nis_billhead n set n.sync_status = 'S' where
##    exists (select 1 from  DSL_INTEFACE_FLAG flag where flag.TABLE_SOURCE = 'dsl_payment_result' AND flag.SYNC_STATUS = 'S'
##            AND flag.SOURCE_ID like CONCAT_WS( ';',n.bill_no,'%'));
##update nis_billhead n set n.sync_status = 'F' where
##    not exists (select 1 from  DSL_INTEFACE_FLAG flag where flag.TABLE_SOURCE = 'dsl_payment_result' AND flag.SYNC_STATUS = 'S'
##                AND flag.SOURCE_ID like CONCAT_WS( ';',n.bill_no,'%'));

##修改日期：2024-10-12
##修改人员：zhangx
##修改内容：大参林银行流水推送视图
CREATE OR REPLACE VIEW DSL_BIS_ACC_DTL_VIEW AS
SELECT dtl.id AS SEQ_ID,
       '01' AS DJZT_CODE,
       '正常' AS DJZT,
       dtl.ID AS YHLSH,
       CASE WHEN  bc.code  = '100' THEN  '100000'
            ELSE bc.code END AS DWBH,
       bc.name AS DWMC,
       CASE
           WHEN dtl.cd_sign =  '1' THEN '02'
           ELSE '01' END AS DJLX_CODE,
       CASE
           WHEN dtl.cd_sign =  '1' THEN '付款'
           ELSE '收款' END AS DJLX_DESC,
       CASE WHEN bi.bif_code = '9302' THEN dtl.USES ELSE CONCAT_WS('&',CONCAT_WS('&',dtl.REMARK,dtl.USES),dtl.POSTSCRIPT) END AS YT,
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
         LEFT JOIN BIS_BIF_INIT bi ON bi.id = dtl.bif_code
WHERE 1=1
  AND ((`dtl`.`TRANS_TIME` > '2024-07-20' AND `dtl`.`bif_code`!='aef6125b6ea2497db16038e7f9e087e7') OR (`dtl`.`TRANS_TIME` > '2024-07-24' AND `dtl`.`bif_code`='aef6125b6ea2497db16038e7f9e087e7'))
  AND (IFNULL(dtl.DTL_SYNC_STATUS,'F') <> 'S' OR (IFNULL(nb.BURSAR_BILL_NO,'') <> IFNULL(dtl.SOURCE_NUM,'')));


##修改日期：2024-07-17
##修改人员：luozw
##修改内容：大参林票据信息同步视图
INSERT IGNORE INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('f5faa0af19974926be93430f440ddb97', '付款执行,付款查询', '付款执行', 'netbank:netBank:nisBillHead:executeQuery',
       1, '4028802f5c2eac34015c2ec1b9c40036', NULL, NULL, NULL, NULL,'/netbank/nisbillhead/datatable');











INSERT IGNORE INTO sys_menu
(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
VALUES ('2a9b7b7435494fa29bdbd174e7121905', '', '资金需求清单', '资金需求清单', '2a164d43b1d042bdbd8eabe715fd72e4', NULL, 3, 1, '/netbank/nisdemand/list', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL);

INSERT IGNORE INTO sys_menu
(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
VALUES ('e8cfb8f69bd744e3b2a95360e76b9c2d', '', '资金需求清单查询', '资金需求清单查询', '2a164d43b1d042bdbd8eabe715fd72e4', NULL, 4, 1, '/netbank/nisdemand/listQuery', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL);


INSERT IGNORE INTO sys_resources
(ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('0d44980104ea417187678e671e8b31ed', '资金需求清单查询资源', '查询资源', 'netbank:nisDemandQuery:query', 1, 'e8cfb8f69bd744e3b2a95360e76b9c2d', NULL, NULL, NULL, NULL, NULL);

INSERT IGNORE INTO sys_resources
(ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('6feada8fc33f4481baa6089a69be2ebc', '生成上级需求单资源', '生成上级需求单资源', 'netbank:nisDemand:super', 1, '2a9b7b7435494fa29bdbd174e7121905', NULL, NULL, NULL, NULL, NULL);

INSERT IGNORE INTO sys_resources
(ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('abdaf7d01cc64b8694e6622cedd3984e', '查询资源', '查询资源', 'netbank:nisDemand:query', 1, '2a9b7b7435494fa29bdbd174e7121905', NULL, NULL, NULL, NULL, NULL);

INSERT IGNORE INTO sys_resources
(ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('b1324160c61f478dac40a5bfee050803', '申请资源', '申请资源', 'netbank:nisDemand:apply', 1, '2a9b7b7435494fa29bdbd174e7121905', NULL, NULL, NULL, NULL, NULL);

INSERT IGNORE INTO sys_resources
(ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('d96e66588d5646319e7dfcf69c7be68f', '生成下拨指令资源', '生成下拨指令资源', 'netbank:nisDemand:allocation', 1, '2a9b7b7435494fa29bdbd174e7121905', NULL, NULL, NULL, NULL, NULL);

##修改日期：2024-07-19
##修改人员：zhaohc
##修改内容：变更菜单资源权限地址
update sys_resources set url = '/bankacc/btbankaccfreeze/datatable' where id = 'cbede0c4cec94bd7a011c46150758b6c';
update sys_resources set url = '/bankacc/btbankaccfreeze/query' where id = '1e1ed3d6efca4bf4863dc0f9fdf3d40f';


##修改日期：2024-07-19
##修改人员：jiangw
##修改内容：增加理财赎回明细表
CREATE TABLE IF NOT EXISTS CHO_PRODUCT_REDEEM_DETAIL
(
   `ID` varchar(32) NOT NULL,
  `CPR_ID` varchar(32) DEFAULT NULL COMMENT '赎回Id',
  `REQ_ID` varchar(32) DEFAULT NULL COMMENT '产品登记单号',
  `REDEEM_MONEY` decimal(15,2) DEFAULT NULL COMMENT '赎回金额',
  `REDEEM_DATE` datetime DEFAULT NULL COMMENT '赎回日期',
  `TRANSFER_DATE` datetime DEFAULT NULL COMMENT '到账日期',
  `INCOME_MONEY` decimal(15,2) DEFAULT NULL COMMENT '收益金额',
  `INCOME_RATE` decimal(10,8) DEFAULT NULL COMMENT '实际收益率',
  `RMK` varchar(200) DEFAULT NULL COMMENT '备注',
  `SUB_FEE_MONEY` decimal(15,2) DEFAULT NULL COMMENT '申购费',
  `REDEEM_CHARGE_MONEY` decimal(15,2) DEFAULT NULL COMMENT '赎回费',
  `CHARGE_MONEY` decimal(15,2) DEFAULT NULL COMMENT '其他手续费',
  `COLLECTION_CONFIRM` varchar(1) DEFAULT '0' COMMENT '银行流水认领组件挂接确认',
  `REDEEM_PORTION` decimal(19,6) DEFAULT NULL COMMENT '赎回份额',
  `REDEEM_DAILY_NET_VAL` decimal(19,6) DEFAULT NULL COMMENT '赎回日净值',
  PRIMARY KEY (`ID`)
);
ALTER TABLE CHO_PRODUCT_REDEEM_DETAIL COMMENT '理财产品赎回明细表';
##GOGO

##理财产品类型表增加“续存模式”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CHO_PRODUCT_TYPE' AND COLUMN_NAME = 'CERTIFICATE_WAY') THEN
      ALTER TABLE CHO_PRODUCT_TYPE ADD COLUMN CERTIFICATE_WAY decimal(2,0) comment '续存模式 1-本金续存 2-本息续存';
ELSE
    ALTER TABLE CHO_PRODUCT_TYPE MODIFY COLUMN CERTIFICATE_WAY decimal(2,0) comment '续存模式 1-本金续存 2-本息续存';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##理财产品登记表增加“续存模式”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CHO_PRODUCT_REQ' AND COLUMN_NAME = 'CERTIFICATE_WAY') THEN
      ALTER TABLE CHO_PRODUCT_REQ ADD COLUMN CERTIFICATE_WAY decimal(2,0) comment '续存模式 1-本金续存 2-本息续存';
ELSE
    ALTER TABLE CHO_PRODUCT_REQ MODIFY COLUMN CERTIFICATE_WAY decimal(2,0) comment '续存模式 1-本金续存 2-本息续存';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##理财产品赎回表增加“BANK_NAME”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CHO_PRODUCT_REDEEM' AND COLUMN_NAME = 'BANK_NAME') THEN
      ALTER TABLE CHO_PRODUCT_REDEEM ADD COLUMN BANK_NAME varchar(200);
ELSE
    ALTER TABLE CHO_PRODUCT_REDEEM MODIFY COLUMN BANK_NAME varchar(200);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##理财产品赎回表增加“PRODUCT_CODE”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CHO_PRODUCT_REDEEM' AND COLUMN_NAME = 'PRODUCT_CODE') THEN
      ALTER TABLE CHO_PRODUCT_REDEEM ADD COLUMN PRODUCT_CODE varchar(60);
ELSE
    ALTER TABLE CHO_PRODUCT_REDEEM MODIFY COLUMN PRODUCT_CODE varchar(60);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##理财产品赎回表增加“BANK_ACC”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CHO_PRODUCT_REDEEM' AND COLUMN_NAME = 'BANK_ACC') THEN
      ALTER TABLE CHO_PRODUCT_REDEEM ADD COLUMN BANK_ACC varchar(32);
ELSE
    ALTER TABLE CHO_PRODUCT_REDEEM MODIFY COLUMN BANK_ACC varchar(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##理财产品赎回表增加“IS_STORE”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CHO_PRODUCT_REDEEM' AND COLUMN_NAME = 'IS_STORE') THEN
      ALTER TABLE CHO_PRODUCT_REDEEM ADD COLUMN IS_STORE char(1)  comment '到期是否续存';
ELSE
    ALTER TABLE CHO_PRODUCT_REDEEM MODIFY COLUMN IS_STORE char(1)  comment '到期是否续存';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##理财产品赎回表增加“续存模式”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CHO_PRODUCT_REDEEM' AND COLUMN_NAME = 'CERTIFICATE_WAY') THEN
      ALTER TABLE CHO_PRODUCT_REDEEM ADD COLUMN CERTIFICATE_WAY decimal(2,0) comment '续存模式 1-本金续存 2-本息续存';
ELSE
    ALTER TABLE CHO_PRODUCT_REDEEM MODIFY COLUMN CERTIFICATE_WAY decimal(2,0) comment '续存模式 1-本金续存 2-本息续存';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2bc402c43cac453bba52e8868474c783', 'zh_CN', '同单位、同产品（产品代码）、同理财账号才能批量赎回', 'common.message.choProductRedeem.batch' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.message.choProductRedeem.batch' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##增加国际化
insert ignore into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('2bc402c43cac453bba52e8868474c783', 'zh_CN', '同单位、同产品（产品代码）、同理财账号才能批量赎回', 'common.message.choProductRedeem.batch');
##GOGO

##增加国际化
insert ignore into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('6c9f04ed58584c7d9805963a9b3b9cc2', 'zh_CN', '赎回申购费', 'table.choProductReq.subFeeMoneyRedeem');
##GOGO

##增加国际化
insert ignore into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('323bf3a7ba38484596920eced9702a35', 'zh_CN', '赎回其他手续费', 'table.choProductReq.chargeMoneyRedeem');
##GOGO

##增加国际化
insert ignore into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('5a1e24a66c9645c39c7188add310f3bf', 'zh_CN', '上次赎回日期', 'table.choProductRedeem.lastRedeemDate');
##GOGO


##增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2bc402c43cac453bba52mnbg6474c783', 'zh_CN', '该付款单还没有进行付款明细勾对', 'nisbillhead.message.no.blend' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'nisbillhead.message.no.blend' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改日期：2024-07-20
##修改人员：qijh
##付款与流水自动勾对定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 336, 4, '付款与流水自动勾对', '2024-05-09 15:33:55', '2024-05-09 15:33:55', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'BisPaymentBlendingService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-05-09 15:33:55', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='付款与流水自动勾对' limit 1);
commit;
##GOGO


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
##GOGO

 ##修改需求清单单号长度
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'PAY_BILL_NO') THEN
       alter table NIS_DEMAND modify  PAY_BILL_NO varchar(100);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2024-07-22
##修改人员:zhaohc
##变更菜单资源地址
update sys_resources set url = '/inneraccount/fcacc/dataTableAll' where id = '3e36cc4cf6744f0c95bfbfc90d3b61df';
update sys_resources set url = '/netbank/nisbillhead/datatable' where id = 'a96e35c98afd4cc3874b536866ef915d';
update sys_resources set url = '/bdlink/bisgathercmd/datatable' where id = '2a9be3f852994e04908bca0e9fb60fd2';



##修改日期:2024-07-29
##修改人员:linqp
##变更记账凭证导入菜单地址
UPDATE sys_menu set URL='/finacial/bankcollect2/voucherimport/improtData' WHERE ID='dfef1344c4034ed9b53c11c804adf6b7';
update sys_resources set url='/finacial/bankcollect2/voucherimport/improtData' where PERMISSION='finacial:voucherimport:apply';


##修改日期：2024-07-25
##修改人员：zhengg
##修改内容：批次开票流水表添加批次明细编号
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_ACCRECORDS' AND COLUMN_NAME = 'BATCH_DTL_NO') THEN
ALTER TABLE AB_ACCRECORDS ADD BATCH_DTL_NO decimal(6,0);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##GOGO
##修改日期:2024-07-30
##修改人员:linqp
##添加国际化
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
VALUES ('e5baffcd4e1b11efb114bc2411339ecc', 'zh_CN', '流水ID', 'bis.bank.dtl.id');

##GOGO
##修改日期:2024-08-01
##修改人员:zhaohc
##添加菜单资源权限
INSERT IGNORE INTO
sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('81adee7685bf479f990231c85911e841', '操作资源', '操作资源', 'sys:datamanage:bank:btinputbankinfo:operate', 1,'402880275c3365c6015c336967470002', NULL, NULL, NULL, NULL,NULL);


##修改日期:2024-08-02
##修改人员:jiangw
##添加国际化
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
VALUES ('34942f804307491caf15463563490332', 'zh_CN', '收益金额不能为空！', 'common.message.choProductRedeem.incomeMoney.isnotnull');

##修改日期:2024-08-02
##修改人员:jiangw
##修改CHO_PRODUCT_REDEEM_DETAIL中INCOME_RATE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CHO_PRODUCT_REDEEM_DETAIL' AND COLUMN_NAME = 'INCOME_RATE') THEN
      ALTER TABLE CHO_PRODUCT_REDEEM_DETAIL ADD COLUMN INCOME_RATE decimal(16,8);
ELSE
    ALTER TABLE CHO_PRODUCT_REDEEM_DETAIL MODIFY COLUMN INCOME_RATE decimal(16,8);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;



##修改日期:2024-08-08
##修改人员:qijh
##修改CHO_PRODUCT_REDEEM_DETAIL中INCOME_RATE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'IS_CONVERGE') THEN
      ALTER TABLE NIS_DEMAND ADD COLUMN IS_CONVERGE VARCHAR (10) DEFAULT '0';
ELSE
    ALTER TABLE NIS_DEMAND MODIFY COLUMN IS_CONVERGE VARCHAR(10) DEFAULT '0';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改人:huangyl
##修改内容:修改字段长度
##修改时间:2024-08-08
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'fc_accbook' AND COLUMN_NAME = 'ORIG_VOUCHER_NO') THEN
ALTER TABLE fc_accbook ADD COLUMN ORIG_VOUCHER_NO VARCHAR(500);
ELSE
ALTER TABLE fc_accbook MODIFY COLUMN ORIG_VOUCHER_NO VARCHAR(500);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人:huangyl
##修改内容:修改字段长度
##修改时间:2024-08-08
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'fc_accbook_dtl' AND COLUMN_NAME = 'ORIG_VOUCHER_NO') THEN
ALTER TABLE fc_accbook_dtl ADD COLUMN ORIG_VOUCHER_NO VARCHAR(500);
ELSE
ALTER TABLE fc_accbook_dtl MODIFY COLUMN ORIG_VOUCHER_NO VARCHAR(500);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人:huangyl
##修改内容:修改字段长度
##修改时间:2024-08-08
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'fc_accbook_blned' AND COLUMN_NAME = 'ORIG_VOUCHER_NO') THEN
ALTER TABLE fc_accbook_blned ADD COLUMN ORIG_VOUCHER_NO VARCHAR(500);
ELSE
ALTER TABLE fc_accbook_blned MODIFY COLUMN ORIG_VOUCHER_NO VARCHAR(500);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人:huangyl
##修改内容:修改字段长度
##修改时间:2024-08-08
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'fc_accbook_blned_dtl' AND COLUMN_NAME = 'ORIG_VOUCHER_NO') THEN
ALTER TABLE fc_accbook_blned_dtl ADD COLUMN ORIG_VOUCHER_NO VARCHAR(500);
ELSE
ALTER TABLE fc_accbook_blned_dtl MODIFY COLUMN ORIG_VOUCHER_NO VARCHAR(500);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2024-08-13
##修改人员:luozw
##添加国际化
INSERT IGNORE INTO sys_multi_lang
(ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
VALUES('12e7c2db37114711bf614664f6fe5ac3', 'en_US', 'nis pay auto no fcacc', 'nis.pay.auto.no.fcacc', '2024-08-13 15:02:40', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL);
INSERT IGNORE INTO sys_multi_lang
(ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
VALUES('38c7bcb4a387465ca13f57e2076baf7b', 'zh_CN', '自动记账没有关联内部账户', 'nis.pay.auto.no.fcacc', '2024-08-13 15:02:33', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL);
INSERT IGNORE INTO sys_multi_lang
(ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
VALUES('c7231239150d4d19bf56562d04bae375', 'en_US', 'nis pay no over less than paymoney', 'nis.pay.no.over.less.than.paymoney', '2024-08-13 15:04:10', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL);
INSERT IGNORE INTO sys_multi_lang
(ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
VALUES('22d5f656e7d3498594c9457caf3144db', 'zh_CN', '内部账号不能透支的情况下，余额小于付款金额', 'nis.pay.no.over.less.than.paymoney', '2024-08-13 15:04:07', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL);
INSERT IGNORE INTO sys_multi_lang
(ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
VALUES('37d1f77897d240188a42590f01d3ec4b', 'en_US', 'nis pay permit over less than paymoney', 'nis.pay.permit.over.less.than.paymoney', '2024-08-13 15:05:01', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL);
INSERT IGNORE INTO sys_multi_lang
(ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
VALUES('76449b08b67b4242a133bebb08a89978', 'zh_CN', '内部账户允许透支,可透支余额小于付款金额', 'nis.pay.permit.over.less.than.paymoney', '2024-08-13 15:05:44', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL);


###修改人:Luoyj
###修改日期:2024-8-13
###修改内容:更新初始化脚本
update act_extend_category set SERVICE_ID = 'bytter-service-bdlink' where CATE_KEY = 'netbankBankaccBal';

###修改人:zhengg
###修改日期:2024-8-18
###修改内容:添加承兑汇票-已到期持有票据正面信息同步已结清自动托收申请定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 3, '承兑汇票-已到期持有票据正面信息同步已结清自动托收申请', '2024-08-15 13:43:48', '2024-08-15 13:44:18', 'XXL', '', 'CRON', '0 0 9,12,20 * * ? *', 'DO_NOTHING', 'FIRST', 'AbDraftExcService', 'autoQueryBillInfoToPayByHold', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-08-15 13:43:48', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='承兑汇票-已到期持有票据正面信息同步已结清自动托收申请' limit 1);
commit;
##GOGO

##修改日期:2024-08-15
##修改人员:zhangx
##修改内容:DSL_INTEFACE_FLAG 增加索引
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'DSL_INTEFACE_FLAG' AND INDEX_NAME = 'IDX_DSL_INTEFACE_FLAG_SOURCE_ID') THEN
ALTER TABLE DSL_INTEFACE_FLAG ADD INDEX IDX_DSL_INTEFACE_FLAG_SOURCE_ID (`SOURCE_ID`);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2024-08-15
##修改人员:zhangx
##修改内容:NIS_BILL_BANK_SERIAL_NO 增加索引
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'NIS_BILL_BANK_SERIAL_NO' AND INDEX_NAME = 'IDX_NIS_BILL_BANK_SERIAL_NO_DTL_ID') THEN
ALTER TABLE NIS_BILL_BANK_SERIAL_NO ADD INDEX IDX_NIS_BILL_BANK_SERIAL_NO_DTL_ID (`DTL_ID`);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2024-08-16
##修改人员:zhous
##修改内容:增加数据字典【认领条件业务类型】
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '927ccfc964134688a4a30ad69b1a33e6', 'AB_BILL_CLAIM_CONDITION_TYPE', '认领条件业务类型', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '927ccfc964134688a4a30ad69b1a33e6' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '5f19943353664caf8df18a2eb1008eee', '011,6012', '贴现', '927ccfc964134688a4a30ad69b1a33e6', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '5f19943353664caf8df18a2eb1008eee' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'a0eac35695554cfab31003a6db849412', '020,021,6020', '托收', '927ccfc964134688a4a30ad69b1a33e6', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'a0eac35695554cfab31003a6db849412' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'eb916ff655bc4fb08fe4eacca4561950', '6101,003,001,6120', '兑付', '927ccfc964134688a4a30ad69b1a33e6', '3' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'eb916ff655bc4fb08fe4eacca4561950' LIMIT 1
    );
##GOGO

##修改日期:2024-08-19
##修改人员:qijh
##修改内容:增加收款指令追踪设为支付成功/失败资源
INSERT INTO SYS_RESOURCES  (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'c34dbbda43d44d79b4487ab4b9796c62', '指令设为支付成功/失败资源', '指令设为支付成功/失败资源', 'netbank:bdLink:bisGatherCmd:setResult', 1, '71d206406ef74170933abc75176c59ee'  FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES  WHERE ID = 'c34dbbda43d44d79b4487ab4b9796c62' LIMIT 1
);
##GOGO


##修改日期:2024-08-20
##修改人员:zhous
##修改内容:增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'dc46f4dd99264de8a33db73b8a223787', 'zh_CN', '开票区间控制', 'fcVoucherType.openBill.section.control' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'fcVoucherType.openBill.section.control' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6aa82152a8244dde865a3a4570373a21', 'en_US', 'openBill section control', 'fcVoucherType.openBill.section.control' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'fcVoucherType.openBill.section.control' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##增加字段语句 fc_voucher_type 添加 BIll_SECTION_CONTROL
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'fc_voucher_type' AND COLUMN_NAME = 'BIll_SECTION_CONTROL') THEN
ALTER TABLE fc_voucher_type ADD COLUMN BIll_SECTION_CONTROL VARCHAR(1) NULL COMMENT '开票区间控制标识 1-三个月 2-六个月';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改内容:增加数据字典【开票区间控制】
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '7dd90c1714a34cbca8c79572c703c1bf', 'BILL_SECTION_CONTROL', '开票区间控制', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '7dd90c1714a34cbca8c79572c703c1bf' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '7e9e59bba0724eeabdc8a12bc45fbc01', '1', '三个月', '7dd90c1714a34cbca8c79572c703c1bf', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '7e9e59bba0724eeabdc8a12bc45fbc01' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '3acf2dde04a0449690551e866ed49e1e', '2', '六个月', '7dd90c1714a34cbca8c79572c703c1bf', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '3acf2dde04a0449690551e866ed49e1e' LIMIT 1
    );
##GOGO

###修改人:zhous
###修改日期:2024-8-21
###修改内容:添加授信占用电票业务单号同步定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 6, '授信占用电票业务单号同步定时任务', '2024-08-20 16:30:15', '2024-08-20 16:30:15', 'XXL', '', 'CRON', '0 0 * * * ?', 'DO_NOTHING', 'FIRST', 'RatBillNoSupplyTaskService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-08-15 13:43:48', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='授信占用电票业务单号同步定时任务' limit 1);
commit;
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2024-08-23
##新增菜单“退款勾对执行策略”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, IS_MODULE,IS_EXTEND)
SELECT '6ffd125b83184d4ebacd591c76d0522f', null, '退款勾对执行策略', '退款勾对执行策略', 'd61f91e27ed9473ab9e8183343ea4507', null, 21, 1, '/netbank/strategy/listRefund', null, null,0,0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = '6ffd125b83184d4ebacd591c76d0522f' LIMIT 1
);
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2024-08-23
##资源权限 查询资源
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '023ffe3ce27f4a26aaa2d498c6563a27', '查询资源', '查询资源', 'netbank:receipt:bisBankExecutionStrategy:refund:query', 1, '6ffd125b83184d4ebacd591c76d0522f' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='023ffe3ce27f4a26aaa2d498c6563a27' LIMIT 1
);
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2024-08-23
##资源权限 查询资源
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'c0f9d65ff3844b70a6d0f5d46be2e49b', '应用资源', '应用资源', 'netbank:receipt:bisBankExecutionStrategy:refund:apply', 1, '6ffd125b83184d4ebacd591c76d0522f' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='c0f9d65ff3844b70a6d0f5d46be2e49b' LIMIT 1
);
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2024-08-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ce79ccbfd73f4ba7b4e8c979bab335d8', 'zh_CN', '定时任务流水取数范围', 'config.nis.timedTasksDays' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='config.nis.timedTasksDays' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2024-08-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '74609275be72435da3108642a8ba2726', 'zh_CN', '退款勾对自动预匹配', 'table.bisBankExecution.refund' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bisBankExecution.refund' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2024-08-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '843fb9e3aedf4f0c89901bed33b61b74', 'zh_CN', '该策略只进行预匹配处理（数据匹配成功勾对标识为“勾对待确认”），如需进行最后退款确认请在“退款勾对”菜单进行处理', 'table.bisBankExecutionStrategy.refund.point' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bisBankExecutionStrategy.refund.point' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：qijh
##修改时间：2024-08-23
##新增定时任务 退款勾对自动预匹配
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 4, '退款勾对自动预匹配', '2024-08-23 16:30:15', '2024-08-23 16:30:15', 'XXL', '', 'CRON', '0 0 * * * ?', 'DO_NOTHING', 'ROUND', 'RefundBlendingTaskService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-08-23 13:43:48', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='退款勾对自动预匹配' limit 1);
commit;
##GOGO

###修改人:zhous
###修改日期:2024-8-23
##修改内容：增加异常告警表
CREATE TABLE IF NOT EXISTS bt_warn_exception_data
(
    `ID` varchar(32) NOT NULL,
    `SEND_DATE` datetime DEFAULT NULL COMMENT '发送时间',
    `SEND_STATUS` varchar(1) DEFAULT NULL COMMENT '发送状态 0-发送成功 1-发送失败',
    `CHANNEL` varchar(1) DEFAULT NULL COMMENT ' 渠道 0-企业微信 1-钉钉',
    `WARN_TYPE` varchar(1) DEFAULT NULL COMMENT '告警类型 0-应用接口 1-前置机服务',
    `WARN_CONTENT` TEXT DEFAULT NULL COMMENT '告警内容',
    `REQ_MSG` TEXT DEFAULT NULL COMMENT '请求报文',
    `RESP_MSG` TEXT DEFAULT NULL COMMENT '响应报文',
    PRIMARY KEY (`ID`)
    ) COMMENT '异常告警表';
##GOGO

##修改内容:增加数据字典【异常告警类型】
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '2f29b66b50de4a88968a3c2a091dc60a', 'EXCEPTION_WARN_DATA_TYPE', '异常告警类型', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '2f29b66b50de4a88968a3c2a091dc60a' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'bdecfea3e646404a92bc7a0578ec23c4', '0', '应用接口', '2f29b66b50de4a88968a3c2a091dc60a', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'bdecfea3e646404a92bc7a0578ec23c4' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'ad41faacdc6744e2bb067005f38b4981', '1', '前置机服务', '2f29b66b50de4a88968a3c2a091dc60a', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'ad41faacdc6744e2bb067005f38b4981' LIMIT 1
    );
##GOGO

##修改内容:增加数据字典【异常告警渠道】
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '6618fe49a9f149e9a630be956f8b5965', 'EXCEPTION_WARN_DATA_CHANNEL', '异常告警渠道', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '6618fe49a9f149e9a630be956f8b5965' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'd9f3ab746f2e4ecc8ab3bc8b591b984f', '0', '企业微信', '6618fe49a9f149e9a630be956f8b5965', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'd9f3ab746f2e4ecc8ab3bc8b591b984f' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '2e00940d9f90425b9e9d3715eccad851', '1', '钉钉', '6618fe49a9f149e9a630be956f8b5965', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '2e00940d9f90425b9e9d3715eccad851' LIMIT 1
    );
##GOGO

##修改内容:增加数据字典【异常告警渠道】
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '80cfaa4b79e94d7c9554c43caddeb14e', 'EXCEPTION_WARN_DATA_SEND_STATUS', '异常告警推送状态', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '80cfaa4b79e94d7c9554c43caddeb14e' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'a1e3a8b7de554e3c9bd7ec6d55deed08', '0', '成功', '80cfaa4b79e94d7c9554c43caddeb14e', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'a1e3a8b7de554e3c9bd7ec6d55deed08' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '8d5458b766a44cef8c9395ee6dbb74bb', '1', '失败', '80cfaa4b79e94d7c9554c43caddeb14e', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '8d5458b766a44cef8c9395ee6dbb74bb' LIMIT 1
    );
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0e27246ef56e4c1b89d632a8f9155816', 'zh_CN', '渠道', 'table.warndata.channel' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.warndata.channel' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6703b50c1dce49cfb38055702e58d0e5', 'zh_CN', '告警类型', 'table.warndata.warnType' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.warndata.warnType' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b59e81bd71194e9e80424c47971957c7', 'zh_CN', '发送时间', 'table.warndata.sendDate' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.warndata.sendDate' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f1e5847547244b3684ece7cd475aea7d', 'zh_CN', '告警内容', 'table.warndata.content' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.warndata.content' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9a97d750e31048ccbbdfea59dea081c9', 'zh_CN', '发送状态', 'table.warndata.sendStatus' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.warndata.sendStatus' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '55e7f1cdd9fd49259e75e05ac1897d66', 'zh_CN', '请求报文', 'table.warndata.reqmsg' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.warndata.reqmsg' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3501b3edaaa645c9b19cf7e60c336b24', 'zh_CN', '响应报文', 'table.warndata.respmsg' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.warndata.respmsg' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

###修改内容:添加异常告警-余额异常告警定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 11, '异常告警-余额异常告警定时任务', '2024-08-22 16:30:15', '2024-08-22 16:30:15', 'XXL', '', 'CRON', '0 0 * * * ?', 'DO_NOTHING', 'FIRST', 'BisAccBalWarnTaskService', '20', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-08-15 13:43:48', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='异常告警-余额异常告警定时任务' limit 1);
commit;
##GOGO

###修改内容:添加异常告警查询菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '409cb6d24c514120abf3469b6f9f63c8', null, '异常告警查询', '异常告警查询', '5a1b112888cd4e60b639197c09a08d53', null, 500, 1, '/fundwarn/exceptionData/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '409cb6d24c514120abf3469b6f9f63c8' LIMIT 1
    );

##修改内容:运行中流程表增加联合唯一键
##修改时间:2024-08-22
DROP PROCEDURE IF EXISTS PRO_ADD_PK;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_PK()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME = 'ACT_RU_EXECUTION' AND INDEX_NAME = 'ACT_UK_BUSKEY_DEF_ID') THEN
alter table act_ru_execution
    add constraint
        unique ACT_UK_BUSKEY_DEF_ID(PROC_DEF_ID_, BUSINESS_KEY_);

END IF;
END$$
DELIMITER ;
CALL PRO_ADD_PK();
DROP PROCEDURE IF EXISTS PRO_ADD_PK;
##GOGO


##GOGO
##修改日期:2024-08-22
##修改人员: meifs
##修改国际化
delete from sys_multi_lang where LANG_KEY='work.flow.was.exists';
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
VALUES ('e5baffcd4e1b11efb114bc2411339eba', 'zh_CN', '业务{0}待办流程中已存在相同的业务，请勿重复提交', 'work.flow.was.exists');











##修改人：fangcf
##修改内容：增加国际化
##修改时间：2024-2-28
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '66a177333ae044a1b117bae0a63c87f8', 'zh_CN', '结算中心必须一致', 'fin.collect.netid.notsame' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'fin.collect.netid.notsame' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '21acb9846e7d475790e26511333f2340', 'zh_CN', '必须是同一个月内的交易流水', 'fin.collect.transtime.notsame' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'fin.collect.transtime.notsame' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b88d8afb138d4c58b4dc13de3be14646', 'zh_CN', '必须是同一币种', 'fin.collect.curid.notsame' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'fin.collect.curid.notsame' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：fangcf
##修改内容：增加国际化
##修改时间：2024-1-16
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f491baa018bf4d84bb6d017739dce98f', 'zh_CN', '合并记账', 'Finacial.accbook.common.button.mergeAdd' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'Finacial.accbook.common.button.mergeAdd' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6ee52e2de5164439ae2c082901d592a3', 'zh_CN', '确定要合并记账吗？', 'common.message.confirm.mergeTally' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.message.confirm.mergeTally' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zhous
##修改内容：付款办理点击编辑进去，背书选择票据时，将付款单的金额在下面显示 增加国际化
##修改时间：2024-9-3
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e2b254ac393f4a62890482ece70bc36b', 'zh_CN', '当前付款单金额：{0}', 'sys.common.currentPayMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'sys.common.currentPayMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：zhous
##修改内容：T24090014723-开票登记界面优化调整 增加国际化
##修改时间：2024-9-4
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b650a3bd769d4b3d810b6ab24dd6c14d', 'zh_CN', '授信合同编码', 'table.abVoucherinfoVice.ratContractCode' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.abVoucherinfoVice.ratContractCode' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '459391e12282478db586e4645efcfc75', 'zh_CN', '是否敞口', 'table.abVoucherinfoVice.isOpenSign' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.abVoucherinfoVice.isOpenSign' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：qijh
##修改内容：银行账号增加是否参与资金需求计算
##修改时间：2024-9-5
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'IS_DEMAND') THEN
ALTER TABLE BT_BANK_ACC ADD COLUMN IS_DEMAND varchar(1) default '1' comment '是否参与资金需求计算';
ELSE
ALTER TABLE BT_BANK_ACC MODIFY COLUMN IS_DEMAND varchar(1) default '1' comment '是否参与资金需求计算';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：zhous
##修改内容：T24090015021-开票登记增加查询条件
##修改时间：2024-9-5
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '8eb2b4fed80f403b9c6a79e7b9ff1662', 'zh_CN', '共享系统单号', 'table.abVoucherinfoVice.bursarBillNo' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.abVoucherinfoVice.bursarBillNo' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：zhous
##修改内容：T24090015023-应收票据台账查询完善
##修改时间：2024-9-5
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a32c942c187c483791cfd3f5b7c7e214', 'zh_CN', '背书人（前手）账号', 'bill.accebill.backOrRec.acc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.backOrRec.acc' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0930bf1f118a4423a8a6d5aea9ee51ce', 'zh_CN', '背书人（前手）开户行', 'bill.accebill.backOrRec.bankName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.backOrRec.bankName' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e9ef41af5d7b415da90b845a72b363a4', 'zh_CN', '到期天数', 'bill.accebill.expireDays' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.expireDays' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：luozw
##修改内容：资金调拨审批流前置校验
##修改时间：2024-9-10
INSERT IGNORE INTO sys_multi_lang
(ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
VALUES('7258585e76864c4d93c40dc347b64ab6', 'en_US', 'nis pay auto no sf fcacc', 'nis.pay.auto.no.sf.fcacc', '2024-09-04 17:22:48', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL);
INSERT IGNORE INTO sys_multi_lang
(ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
VALUES('d226b0daef0b47258e0aee77322fa5d4', 'zh_CN', '自动记账没有关联内部账户,请检查收付款方是否关联内部账户!', 'nis.pay.auto.no.sf.fcacc', '2024-09-04 17:22:02', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL);


update sys_multi_lang set LANG_CONTEXT = '未设置结算中心自动复核人参数，无法复核' where lang_key='fin.book.tally.autochecker.null' and lang_code = 'zh_CN';

update sys_multi_lang set LANG_CONTEXT = '未设置结算中心自动复核人参数，无法复核' where lang_key='fin.book.tally.autochecker.netnull' and lang_code = 'zh_CN';

update sys_multi_lang set LANG_CONTEXT = '审批未设置结算中心自动复核人参数，无法复核' where lang_key='zijindiaobo.fin.book.tally.autochecker.netnull' and lang_code = 'zh_CN';

##修改人：zhous
##修改内容：T24090015027-出票登记-增加加保证金关联功能 / 开票登记（付款开票登记、票款开票登记）招商银行保证金账号选择功能
##修改时间：2024-9-9
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd0df383db0f248dc96067b7d78b92256', 'zh_CN', '招行保证金列表', 'table.abAccrecords.zhBondAcc.list' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.abAccrecords.zhBondAcc.list' AND LANG_CODE = 'zh_CN' limit 1);
commit;

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c0d87a596be64f778aae0e0b3a3d53b4', 'zh_CN', '起息时间', 'common.interest.time' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.interest.time' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '48acb288dc694aab8b6d95b1416ea3b1', 'zh_CN', '到期时间', 'common.end.data' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.end.data' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##增加字段语句 AB_VOUCHERINFO 添加 CMB_BAIL_FLAG
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'cmb_bail_flag') THEN
ALTER TABLE AB_VOUCHERINFO ADD COLUMN cmb_bail_flag VARCHAR(4) NULL COMMENT '招行列表保证金选择标识 0-否 1-是';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##添加招行保证金账户信息表
CREATE TABLE IF NOT EXISTS `cmb_bail_account_info` (
    `ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID',
    `ACCNBR` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '账号',
    `ACCNAM` varchar(62) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '户名',
    `HMGTYP` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '保证金种类',
    `ACCBRN` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '开户机构',
    `CCYNBR` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '币种',
    `OPNDAT` datetime DEFAULT NULL COMMENT '开户日期',
    `VALDAT` datetime DEFAULT NULL COMMENT '起息日期',
    `MUTDAT` datetime DEFAULT NULL COMMENT '到期日期',
    `STSCOD` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
    `ONLBAL` decimal(15,2) DEFAULT NULL COMMENT '联机余额',
    `AVLBAL` decimal(15,2) DEFAULT NULL COMMENT '可用余额',
    `FRZAMT` decimal(15,2) DEFAULT NULL COMMENT '冻结金额',
    `BUSPRD` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务分类码',
    PRIMARY KEY (`ID`),
    UNIQUE KEY `AK_ACCNBR_CMB_BAIL_ACCOUNT_INFO` (`ACCNBR`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='招行保证金账户信息表';
##GOGO

##修改人：zhous
##修改内容：T24090015028-【定时任务】保证金到期账户自动销户
##修改时间：2024-9-10
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 8, '保证金账户过期销户', '2024-09-10 15:54:10', '2024-09-10 15:54:10', 'XXL', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'FcBondDelHandleServiceJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-09-10 15:54:10', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='保证金账户过期销户' limit 1);
commit;
##GOGO

##修改人：zhous
##修改内容：T24090015558- 应付票据台账增加已选金额、已选保证金合计
##修改时间：2024-9-11
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '50e66c48138940aeb403c5506b6f186b', 'zh_CN', '已选保证金：{0}', 'sys.common.isSelectBailMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'sys.common.isSelectBailMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：zhaohc
##修改内容：T24090015811- 新增定时任务“通用sql调用”
##修改时间：2024-9-13
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 15, '通用sql调用', now(), now(), 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'AutomaticExecutionSqlJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='通用sql调用' limit 1);
commit;
##GOGO

##修改人：zhous
##修改内容：T24090015982-新增月度还银承表
##修改时间：2024-9-20
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '001fdcbea1af43e9a998d51f040864e5', 'zh_CN', '大区', 'bill.accebill.region' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.region' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2db7431c6c8e40548b7d07d116c85caf', 'zh_CN', '营运区', 'bill.accebill.operatingArea' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.operatingArea' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '772e09548943474998e854bd184992ad', 'zh_CN', '股权', 'bill.accebill.equity' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.equity' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a56ad0f3225d4ef986eb86cc61c38efa', 'zh_CN', '申请还款金额', 'bill.accebill.applyRepayMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.applyRepayMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'be22aab9b1a94668ac9b690e705bb3a3', 'zh_CN', '结算金额', 'bill.accebill.settleMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.settleMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'b3ded0253f0d4085801a7f73ede6ee09', null, '月度还银承表', '月度还银承表', '4d6a6759cde6429cbfadd1f4a7c640c1', null, 9, 1, '/bill/accebill/abbillQuery/listAbBillBabByMonth', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'b3ded0253f0d4085801a7f73ede6ee09' LIMIT 1
    );
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '9c141e0f11c14705b0d0d7c1de07f7e0', '月度还银承表查询', '月度还银承表查询', 'bill:accebill:billbab:query:corpop', 1, 'b3ded0253f0d4085801a7f73ede6ee09' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM SYS_RESOURCES WHERE ID='9c141e0f11c14705b0d0d7c1de07f7e0' LIMIT 1
    );
commit;
##GOGO


##修改人：zhours
##修改时间：2024.09.15
##修改原因：增加字典

insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '3ee20ebd736b11efb777005056a84efa', 'report.showType', '报表显示方式' from dual
where not exists (select 1 from sys_dict_group where ID ='3ee20ebd736b11efb777005056a84efa' limit 1);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'b460fe1a736b11efb777005056a84efa', '0', '汇总',null,'3ee20ebd736b11efb777005056a84efa',1  from dual
where not exists (select 1 from sys_dict where ID ='b460fe1a736b11efb777005056a84efa' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'b9151f43736b11efb777005056a84efa', '1', '明细',null,'3ee20ebd736b11efb777005056a84efa',2  from dual
where not exists (select 1 from sys_dict where ID ='b9151f43736b11efb777005056a84efa' limit 1);
##GOGO

##修改人：jiangw
##修改内容：T24080010937- 新增账户管理统计报表菜单
##修改时间：2024-9-18
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '9728335cf9074f9ba1c346a2ac5f77bd', null, '账户管理统计表', '账户管理统计表', '07cbbb8132de427eb53c9d624fb7ee22', null, 22, 1, '/report/accountManagementStatistics/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '9728335cf9074f9ba1c346a2ac5f77bd' LIMIT 1
    );
##GOGO

##修改人：jiangw
##修改内容：T24080010938- 新增支付统计报表菜单
##修改时间：2024-9-18
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'aeb2c64f9bf447e78f8e3c95d59e67e4', null, '支付统计表', '支付统计表', '07cbbb8132de427eb53c9d624fb7ee22', null, 23, 1, '/report/paymentStatistics/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'aeb2c64f9bf447e78f8e3c95d59e67e4' LIMIT 1
    );
##GOGO

CREATE TABLE IF NOT EXISTS  `sys_user_corp_role` (
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

##修改人：jiangw
##修改内容：T24090014832- 开户办理成功消息提醒增加‘账户具体开户行’
##修改时间：2024-9-18
update sys_notification_category set CONTENT='您有一笔开户申请办理成功，账户名$!{accName}，账号$!{bankAcc}，账户具体开户行$!{bankName}，请阅知！' where id='9dfcb21be6134ac7acd64d2f1dfa881f';
##GOGO

##修改日期：2024-09-20
##修改人员：qijh
##企业微信-结算管理-请款审批消息提醒
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT '3154d965f77141219809ae901208b3f9', '183fc820962c411e9581ce3a7db5f552', '请款审批企微消息提醒', 'netbank:allocation:ask:approve', '', '', null, '', 0, 9, '消息提醒内容：\\n业务类型：资金平台请款审批\\n日期：$!{date}\\n请款笔数：$!{count}笔', 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category WHERE ID = '3154d965f77141219809ae901208b3f9' LIMIT 1
);
##GOGO
##修改人：zhous
##修改内容：T24090017194-招商银行开票挂保证金，选择保证金账户时，过滤显示开票账户关联的保证金账户
##修改时间：2024-9-20
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMB_BAIL_ACCOUNT_INFO' AND COLUMN_NAME = 'CURACC') THEN
ALTER TABLE CMB_BAIL_ACCOUNT_INFO ADD CURACC VARCHAR(35);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
commit;
##GOGO


##修改日期：2024-09-20
##修改人员：qijh
##请款审批企微消息提醒定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '请款审批企微消息提醒', '2024-09-20 14:06:46', '2024-09-20 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'NisallocationReqApproveJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-09-20 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='请款审批企微消息提醒' limit 1);
commit;
##GOGO

##修改日期：2024-09-21
##修改人员：jiangw
##企业微信-结算管理-付款失败消息提醒
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE, WEIXIN_IND)
SELECT '63f9ea27ffe84bb18c259d6e48d24290', 'd57cc0c78cf54212b179741b41e2dca8', '付款失败', 'netbank:bdlink:payment:fail', '', '', null, '', null, 2, '付款公司：$!{payerAccName}，收款人：$!{payeeAccName}，金额：$!{payMoney}，付款失败，失败原因：$!{failReason}。业务单据号：$!{cbsBillNo}。', 1 , 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category WHERE ID = '63f9ea27ffe84bb18c259d6e48d24290' LIMIT 1
);
##GOGO

##修改日期：2024-09-21
##修改人员：jiangw
##付款失败消息提醒定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '付款失败企微消息提醒', '2024-09-21 14:06:46', '2024-09-21 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'AutoPaymentBisExcFail', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-09-21 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='付款失败企微消息提醒' limit 1);
commit;
##GOGO

##修改日期：2024-09-23
##修改人员：qijh
##增加数据字典 勾兑状态
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'bd341a55d86948f0b882909c9c5b2d15', '4', '无需勾对', '', 'fa2834f845d14ec4a308cc08de92879c', 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='bd341a55d86948f0b882909c9c5b2d15' LIMIT 1
);
##GOGO

##修改日期：2024-09-24
##修改人员：zrs
## 退款勾兑增加交易日期范围
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BIS_BANK_EXECUTION_STRATEGY' AND COLUMN_NAME = 'TRANS_DAYS') THEN
ALTER TABLE BIS_BANK_EXECUTION_STRATEGY ADD COLUMN TRANS_DAYS INT ;
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期：2024-09-25
##修改人员：qijh
##修改内容:增加字段语句 AGAIN_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'AGAIN_SIGN') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN AGAIN_SIGN VARCHAR(1) DEFAULT 0;
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：lijq
##修改内容：增加国际化
##修改时间：2024-10-09
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f491baa018bf4d8mkjnh017739dce98f', 'zh_CN', '汇总记账', 'Finacial.accbook.common.button.totalAdd' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'Finacial.accbook.common.button.totalAdd' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b88d8afb138d4c58b4dc13de3bbgvfd6', 'zh_CN', '必须是同一本方账号', 'fin.collect.bankacc.notsame' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'fin.collect.bankacc.notsame' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b88d8afb13gtrf58b4dc13de3be14646', 'zh_CN', '必须是同一交易日期', 'fin.collect.transdate.notsame' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'fin.collect.transdate.notsame' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：zhous
##修改内容：T24100019229-新增-票据解付与解付账户前一天余额对比表
##修改时间：2024-10-10
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '12f31a5331a04b9b827deb1d94503d8f', 'zh_CN', '公司全称', 'bill.accebill.corpName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.corpName' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6ef2ea3ff3f6496798bf10b3946191a7', 'zh_CN', '公司类型', 'bill.accebill.corpType' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.corpType' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '92b3ceb93c1c4f76bd93a430d152f4ce', 'zh_CN', '承兑票面金额', 'bill.accebill.acceptBillMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.acceptBillMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3ecd4ee74a7c473db54919942115e738', 'zh_CN', '解付账户前一天实时余额', 'bill.accebill.preDayBal' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.preDayBal' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '682e8bc0409c4450a2141262f1a8a66b', 'zh_CN', '差异', 'bill.accebill.discrepancy' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.discrepancy' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'abb5913114ab47cd8ee50caf55e6ef55', null, '票据解付与解付账户前一天余额对比表', '票据解付与解付账户前一天余额对比表', '4d6a6759cde6429cbfadd1f4a7c640c1', null, 10, 1, '/bill/accebill/abbillQuery/listBankAccBalCompare', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'abb5913114ab47cd8ee50caf55e6ef55' LIMIT 1
    );
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'f1f1266f6d1a4b77a64d122e1dcbcd71', '票据解付与解付账户前一天余额对比表查询', '票据解付与解付账户前一天余额对比表查询', 'bill:accebill:bankaccBalCompare:query:corpop', 1, 'abb5913114ab47cd8ee50caf55e6ef55' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM SYS_RESOURCES WHERE ID='f1f1266f6d1a4b77a64d122e1dcbcd71' LIMIT 1
    );
commit;
##GOGO


##修改日期：2024-10-11
##修改人员：zhangx
##修改内容：流水表 BIS_ACC_DTL 增加 DTL_SYNC_STATUS 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'DTL_SYNC_STATUS') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN DTL_SYNC_STATUS VARCHAR(5) COMMENT '数据发送状态 F失败 S成功';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;


##修改日期：2024-10-11
##修改人员：zhangx
##修改内容：流水表 BIS_ACC_DTL 增加 OP_TIME 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'OP_TIME') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN OP_TIME DATETIME COMMENT '流水推送时间';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;


##修改日期：2024-10-11
##修改人员：zhangx
##修改内容：流水表 BIS_ACC_DTL 增加 SOURCE_NUM 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'SOURCE_NUM') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN SOURCE_NUM VARCHAR(100) COMMENT '共享单号';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;


##修改日期：2024-10-11
##修改人员：zhangx
##修改内容：流水表 BIS_ACC_DTL 增加 BFS_REQUEST_NO 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'BFS_REQUEST_NO') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN BFS_REQUEST_NO VARCHAR(100) COMMENT '资金推送批次';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改日期：2024-10-14
##修改人员：zhaohc
##变更数据表属性
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'fc_bond_back' AND COLUMN_NAME = 'BACK_MONEY') THEN
        ALTER TABLE fc_bond_back MODIFY COLUMN BACK_MONEY decimal(20,2) DEFAULT NULL COMMENT '转回保证金';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
commit;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'fc_bond_back' AND COLUMN_NAME = 'BACK_INTEREST') THEN
        ALTER TABLE fc_bond_back MODIFY COLUMN BACK_INTEREST decimal(20,2) DEFAULT NULL COMMENT '转回利息';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
commit;
##GOGO

##修改日期：2024-10-14
##修改人员：zhangx
##修改内容：大参林银行流水推送视图
CREATE OR REPLACE VIEW DSL_BIS_ACC_DTL_VIEW AS
SELECT dtl.id AS SEQ_ID,
       '01' AS DJZT_CODE,
       '正常' AS DJZT,
       dtl.ID AS YHLSH,
       CASE WHEN  bc.code  = '100' THEN  '100000'
            ELSE bc.code END AS DWBH,
       bc.name AS DWMC,
       CASE
           WHEN dtl.cd_sign =  '1' THEN '02'
           ELSE '01' END AS DJLX_CODE,
       CASE
           WHEN dtl.cd_sign =  '1' THEN '付款'
           ELSE '收款' END AS DJLX_DESC,
       CASE WHEN bi.bif_code = '9302' THEN dtl.USES ELSE CONCAT_WS('&',CONCAT_WS('&',dtl.REMARK,dtl.USES),dtl.POSTSCRIPT) END AS YT,
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
         LEFT JOIN BIS_BIF_INIT bi ON bi.id = dtl.bif_code
WHERE 1=1
  AND ((`dtl`.`TRANS_TIME` > '2024-07-20' AND `dtl`.`bif_code`!='aef6125b6ea2497db16038e7f9e087e7') OR (`dtl`.`TRANS_TIME` > '2024-07-24' AND `dtl`.`bif_code`='aef6125b6ea2497db16038e7f9e087e7'))
  AND (IFNULL(dtl.DTL_SYNC_STATUS,'F') <> 'S' OR (IFNULL(nb.BURSAR_BILL_NO,'') <> IFNULL(dtl.SOURCE_NUM,'')));

##修改人：zhours
##修改时间：2024.10.21
##修改原因：新增【月度贷款明细表】

INSERT INTO `sys_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`)
select 'da8a1a178f7211efaac1005056a84efa', '7', '月度贷款明细表', '月度贷款明细表', '8210f38c7eb045ba88578c3086ab434e', NULL, 7, 1, '/cms/provide/loaninfo/monthlyLoanDtl', NULL, NULL, NULL, NULL, NULL, NULL, 0, 1, NULL from dual
where not exists (select 1 from sys_menu where ID ='da8a1a178f7211efaac1005056a84efa' );
##GOGO

INSERT INTO `sys_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
select 'f3467fe78f7411efaac1005056a84efa', '查询资源', '查询资源', 'cms:provide:info:monthlyLoan', 1, 'da8a1a178f7211efaac1005056a84efa' from dual
where not exists (select 1 from sys_resources where ID ='f3467fe78f7411efaac1005056a84efa' );
##GOGO

##修改日期:2024-10-23
##修改人员:zhous
##修改内容:T24100020795-新增银承开票金额统计表
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c9c86afdcd17498e9162787f4bab8196', 'zh_CN', '6个月银承占比', 'bill.accebill.sixMonthBillRate' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.sixMonthBillRate' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e8a9c6a951814ac3aead157b76631aac', 'zh_CN', '开票占比', 'bill.accebill.billTotalRate' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.billTotalRate' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ec92010c5ced45f598870ffd2d557de3', 'zh_CN', '商承：六个月', 'bill.accebill.businessBill.sixMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.businessBill.sixMonth' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'cb865de25564465da49722cf132e971b', 'zh_CN', '商承：三个月', 'bill.accebill.businessBill.threeMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.businessBill.threeMonth' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3ba53394817547a49811b28ed6774542', 'zh_CN', '低风险：六个月', 'bill.accebill.lowRisk.sixMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.lowRisk.sixMonth' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'cfb9b21048a4482685f9fa5a117de19b', 'zh_CN', '低风险：三个月', 'bill.accebill.lowRisk.threeMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.lowRisk.threeMonth' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'dc7f473a5a474e2ebe72e5a75801aa3e', 'zh_CN', '敞口：六个月', 'bill.accebill.open.sixMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.open.sixMonth' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9ef21ee4ded84e63b306e582bcd622b8', 'zh_CN', '敞口：三个月', 'bill.accebill.open.threeMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.open.threeMonth' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ee78c40dd595401690cf8a52c4d87a2c', 'zh_CN', '其中：开票总额', 'bill.accebill.among.openBillTotal' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.among.openBillTotal' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9630730a173a4c94a72070bd7da0079f', 'zh_CN', '其中：电汇总额', 'bill.accebill.among.ebankTotal' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.among.ebankTotal' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '78ad1b620ff74f208f4444ec666b0df5', 'zh_CN', '支付总额', 'bill.accebill.payTotal' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.payTotal' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '057a6355fb854668bc47020485ddac78', 'zh_CN', '本月止保证金余额', 'bill.accebill.unexpiredBailMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.unexpiredBailMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b773b1946ec1454ea756f9dad0cb2976', 'zh_CN', '本月末授信剩余额度', 'bill.accebill.leftQuota' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.leftQuota' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '55740b808ab143f5a3e760fb0210c7dd', 'zh_CN', '本月止未到期开票金额', 'bill.accebill.unexpiredBillMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.unexpiredBillMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '79129fdbccc841a2bf07b8da53537f5d', 'zh_CN', '流贷金额', 'bill.accebill.loanMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.loanMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '4dc15cf9308348bf8f38fe461d7d6877', 'zh_CN', '合同到期日', 'bill.accebill.contract.endDate' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.contract.endDate' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0b87a255364e46fba98bf1ba4518eff8', 'zh_CN', '其中:低风险', 'bill.accebill.among.lowRisk' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.among.lowRisk' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '32b0fc84643248d8a3adb30913ec5b54', 'zh_CN', '其中:敞口', 'bill.accebill.among.open' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.among.open' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2cf8863d8a5c4c8bad6ad4c1ff3a672e', 'zh_CN', '区域', 'bill.accebill.query.region' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.query.region' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'bb9e05f5d1c544bf9899a68d110abd8e', null, '银承开票金额统计表', '银承开票金额统计表', '4d6a6759cde6429cbfadd1f4a7c640c1', null, 11, 1, '/bill/accebill/abbillQuery/listBankOpenBillAmtStatistics', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'bb9e05f5d1c544bf9899a68d110abd8e' LIMIT 1
    );
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '438268b08cbd4178b27be031d97281b9', '银承开票金额统计表查询', '银承开票金额统计表查询', 'bill:accebill:BankOpenBillAmtStatistics:query:corpop', 1, 'bb9e05f5d1c544bf9899a68d110abd8e' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM SYS_RESOURCES WHERE ID='438268b08cbd4178b27be031d97281b9' LIMIT 1
    );
commit;
##GOGO



## 新增账号用途维护菜单SQL

##账号用途表
CREATE TABLE IF NOT EXISTS BT_BANK_ACC_USE
(
    ID              varchar(32)      not null comment 'id' primary key,
    USE_CODE        varchar(32)      not null comment '用途编码',
    USE_NAME        varchar(200)     not null comment '用途名称',
    USE_RMK         varchar(200)     null comment '备注',
    CREATE_DATE     datetime         null,
    CREATE_BY       varchar(32)      null,
    UPDATE_DATE     datetime         null,
    UPDATE_BY       varchar(32)      null
    );
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select 'e62741fc90e811efaac1005056a84efa', '', '账号用途维护', '账号用途维护', 'd61f91e27ed9473ab9e8183343ea4507', '', 23, 1, '/netbank/bankAccUse/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='账号用途维护' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select 'dcbed92790ea11efaac1005056a84efa', '查询资源', '查询资源', 'netbank:bankAccUse:query', 1, 'e62741fc90e811efaac1005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='netbank:bankAccUse:query' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '4329959590eb11efaac1005056a84efa', '编辑资源', '编辑资源', 'netbank:bankAccUse:edit', 1, 'e62741fc90e811efaac1005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='netbank:bankAccUse:edit' limit 1);
commit;
##GOGO

## 账号表增加账号用途
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'ACC_USE_CODE') THEN
alter table BT_BANK_ACC add column ACC_USE_CODE varchar(320) ;
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '该用途编码已存在，请修改！', 'netbank.message.bankaccuse.unique' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.message.bankaccuse.unique' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '该用途已经存在绑定账号，不能删除，请确认！', 'netbank.message.bankaccuse.use' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.message.bankaccuse.use' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改日期:2024-10-23
##修改人员:zhous
##修改内容:T24100021137-票据解付与解付账户前一天余额对比表逻辑调整
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7ab913cb83ef41d79756ce10af85e156', 'zh_CN', '贷款应还本息', 'table.cmsLoanInfo.loan.reapyInterest' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.cmsLoanInfo.loan.reapyInterest' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改日期：2024-11-28
##修改人员：lijq
##修改内容：大参林联行号查询视图
CREATE OR REPLACE VIEW DSL_BT_INPUT_BANK_INFO_VIEW AS
SELECT b.SYS_BANK_CODE     AS SYS_BANK_CODE,
       b.BANK_NAME         AS BANK_NAME,
       b.UPDATE_DATE       AS UPDATE_DATE,
       b.CREATE_DATE       AS CREATE_DATE,
       t.BANK_TYPE         AS BANK_TYPE_CODE,
       t.TYPE_NAME         AS BANK_TYPE_NAME
FROM BT_INPUT_BANK_INFO b
    LEFT JOIN BT_BANK_TYPE t ON b.BANK_PREFIX = t.BANK_PREFIX AND b.bank_name LIKE CONCAT(t.type_name, '%');


##修改日期：2024-10-24
##修改人员：zrs
##修改内容：增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '用途为“营收账号”的数量超过最大配置数量', 'basic.business.message.btbankacc.1004.max' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='basic.business.message.btbankacc.1004.max' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO


##修改日期：2024-10-25
##修改人员：zmc
##修改内容：增加菜单
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '用途为“营收账号”的数量超过最大配置数量', 'basic.business.message.btbankacc.1004.max' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='basic.business.message.btbankacc.1004.max' AND LANG_CODE = 'zh_CN' limit 1);

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON,
                      CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '402880275c0a9e3a015czmc406a00000', '3396', '业务种类维护', '差额下拨启用单位', 'd61f91e27ed9473ab9e8183343ea4507',
        '4028802f5bf6b978015bf6bb00190000,d61f91e27ed9473ab9e8183343ea4507,402880275c0a9e3a015c0aa406a00000', 10, 1,
        '/netbank/chaeOpenCorp/list', null, null, null, null, null, null, 0, 1, null from dual where not exists (select 1 from sys_menu where id = '402880275c0a9e3a015czmc406a00000');

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY,
                                      UPDATE_TIME, UPDATE_BY, URL)
select '00bb2781ezmc4580a4b546c8e801cf8a', '查询资源', '查询资源', 'netbank:chaeOpenCorp:list', 1,
        '402880275c0a9e3a015czmc406a00000', null, null, null, null, '/netbank/chaeOpenCorp/list' from dual where not exists (select 1 from sys_resources where id = '00bb2781ezmc4580a4b546c8e801cf8a');
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY,
                                      UPDATE_TIME, UPDATE_BY, URL)
select '00bb2781ezmc4580a4b546c8e801cf8b', '新增', '新增', 'netbank:chaeOpenCorp:save', 1,
        '402880275c0a9e3a015czmc406a00000', null, null, null, null, '/netbank/chaeOpenCorp/save' from dual where not exists (select 1 from sys_resources where id = '00bb2781ezmc4580a4b546c8e801cf8b');
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY,
                                      UPDATE_TIME, UPDATE_BY, URL)
select '00bb2781ezmc4580a4b546c8e801cf8c', '删除', '删除', 'netbank:chaeOpenCorp:delete', 1,
        '402880275c0a9e3a015czmc406a00000', null, null, null, null, '/netbank/chaeOpenCorp/delete' from dual where not exists (select 1 from sys_resources where id = '00bb2781ezmc4580a4b546c8e801cf8c');
##GOGO
##修改日期:2024-10-25
##修改人员:zhous
##修改内容:T24100020746-新增每周资金到期用款规划表
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '41c0d0486b704ac2b88a4c2f1073b435', 'zh_CN', '资金下拨日期', 'bill.accebill.fundsDisbursementDate' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.fundsDisbursementDate' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '926cbe3c93574069bc843fbe7c5da059', null, '每周资金到期用款规划表', '每周资金到期用款规划表', '07cbbb8132de427eb53c9d624fb7ee22', null, 24, 1, '/report/weeklyFundsDueSchedule/listWeeklyFundsDueSchedule', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '926cbe3c93574069bc843fbe7c5da059' LIMIT 1
    );
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'b9cf30727ca54f25a72145337ffd5470', '每周资金到期用款规划表查询资源', '每周资金到期用款规划表查询资源', 'netbank:report:weeklyFundsDueSchedule:query', 1, '926cbe3c93574069bc843fbe7c5da059' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM SYS_RESOURCES WHERE ID='b9cf30727ca54f25a72145337ffd5470' LIMIT 1
    );
commit;
##GOGO



##修改日期：2024-11-26
##修改人员：cheny
##修改内容：大参林票据信息同步视图-增加应付退票查询条件
CREATE OR REPLACE VIEW DSL_AB_BILL_VIEW AS
SELECT
    a.ID AS FMS_ID,
    'M' AS RECEIPT_TYPE,
    'BFS' AS RECEIPT_SOURCE,
    v.ORIG_NO AS RECEIPT_NUM,
    v.SBBLL_RNG_STRT_SN AS SUB_RECEIPT_FROM,
    v.SBBLL_RNG_END_SN AS SUB_RECEIPT_TO,
    CASE
        WHEN vv.TYPE = '0' THEN 'AP'
        ELSE 'AR' END AS RECEIPT_PROPERTY,
    CASE
        WHEN v.BILL_TYPE = 10 AND v.BILL_FLAG = 10 THEN '银行承兑汇票-纸质'
        WHEN v.BILL_TYPE = 10 AND v.BILL_FLAG = 11 THEN '银行承兑汇票-电子'
        WHEN v.BILL_TYPE = 11 AND v.BILL_FLAG = 10 THEN '商业承兑汇票'
        WHEN v.BILL_TYPE = 11 AND v.BILL_FLAG = 11 THEN '商业承兑汇票-电子'
        END AS RECEIPT_NAME,
    v.BILL_MONEY AS RECEIPT_COST,
    'CNY' AS CURRENCY_CODE,
    CASE WHEN a.brief_id='002' AND acc.BANK_TYPE_ID='0e5a53a00fcf467e9fd42125614a9232'  THEN a.BUSINESS_DATE   ##线上开票取承兑时间
         ELSE v.START_DATE END AS CREATE_RECEIPT_DATE,
    v.END_DATE AS RECEIPT_DUE_DATE,
    v.PAYER AS CREATE_RECEIPT_NAME,
    v.PAYERACC AS CREATE_REC_ACCOUNT,
    v.PAYER_BANK_NAME AS CREATE_REC_BANK,
    v.PAYEE AS PAYEE_NAME,
    v.PAYEEACC AS PAYEE_ACCOUNT,
    v.PAYEE_BANK_NAME AS PAYEE_BANK,
    CONCAT((vv.BAIL_PERCENT * 100), '%') AS CASH_DEPOSIT_SCALE,
    vv.BAIL_MONEY AS CASH_DEPOSIT_COST,
    CASE WHEN (`a`.`BRIEF_ID` IN ('010')) THEN  n.`PURPOSE`
         WHEN a.brief_id='9010' THEN CONCAT ('原出票人：',v.payer) ELSE vv.`RMK` END AS `COMMT`,
    CASE
        WHEN a.brief_id IN ('010') THEN 'Y'
        ELSE 'N' END AS ENDORSE_FLAG,
    CASE
        WHEN a.brief_id IN ('010') THEN a.`REC_NM`
        ELSE NULL END AS ENDORSER,
    CASE
        WHEN a.brief_id IN ('010') THEN a.CREATE_DATE
        ELSE NULL END AS ENDOSE_DATE,
    v.BILL_MONEY - IFNULL(vv.BAIL_MONEY,0) AS ACTUAL_COST,
    CASE
        WHEN a.brief_id = '6001' THEN '11'
        WHEN a.brief_id = '9010' THEN '12'
        WHEN a.brief_id = '6004' THEN '13'
        WHEN a.brief_id = '9003' THEN '26'
        WHEN a.brief_id = '6120' THEN '28'
        WHEN (a.brief_id IN ('004') AND v.bill_status NOT IN ('CS05')) THEN '29'
        END AS ACTION_TYPE,
    a.BUSINESS_DATE AS CASHING_DATE,
    CASE WHEN (`a`.`BRIEF_ID` = '010' AND `nb`.`ID` IS NOT NULL) THEN `su`.`USERNAME`
         WHEN `a`.`BRIEF_ID` = '002' THEN v.`CREATE_NAME`
         ELSE IFNULL(`vv`.`CREATE_NAME`,`a`.`CREATE_NAME`) END AS `MAKER`,
    'R' AS PUSH_STATUS_FLG,
    'R' AS PROCESSED_FLAG,
    CASE
        WHEN a.brief_id IN ('9003','9010','6001') THEN '11' ##应收库存
        WHEN a.brief_id IN ('010') THEN '12' ##票据背书
        WHEN a.brief_id IN ('6004') THEN '13'  ##应收作废
        WHEN a.brief_id IN ('001','002') THEN '27'  ##已开票
        WHEN (a.brief_id IN ('004') AND v.bill_status NOT IN ('CS05')) THEN '29'   ##退票
        END AS STATUS,
    CASE WHEN a.brief_id IN ('9010','010') THEN v.BILL_MONEY
         ELSE NULL END AS ENDOSE_AMOUNT,
    CASE WHEN a.brief_id IN ('6120') THEN a.BUSINESS_DATE
         ELSE NULL END AS RECEIPT_CASH_DATE,
    CASE WHEN sc.code = '100' THEN '100000' ELSE sc.code END AS attribute1,
    NULL AS attribute2,
    NULL AS attribute3,
    NULL AS attribute4,
    NULL AS attribute5
FROM ab_accrecords a
         LEFT JOIN ab_voucherinfo v  ON a.vid = v.id
         LEFT JOIN ab_voucherinfo_vice vv ON a.vvid = vv.id
         LEFT JOIN sys_corp sc ON sc.id = vv.CORP_ID
         LEFT JOIN DSL_INTEFACE_FLAG ef ON ef.source_id = a.id AND ef.table_source = 'DSL_AB_BILL_VIEW'
         LEFT JOIN nis_bill nb ON nb.bill_id = a.id
         LEFT JOIN nis_billhead n ON nb.pay_id = n.id
         LEFT JOIN sys_user su ON su.LOGIN_NAME = n.CREATE_BY
         LEFT JOIN bt_bank_acc acc ON acc.bank_acc=v.PAYERACC
WHERE 1=1 and a.brief_id not in ('010','9010','6001','9003','011')    ##920确认暂停收票数据推送
  AND ((a.brief_id IN ('9010','010') AND a.opt_status IN ('18','19')) OR ((a.brief_id NOT IN ('9010','010') AND a.opt_status='19')))
  AND a.status IN ('95','96')
  AND ((a.brief_id NOT IN ('9010','010') AND a.OLD_ACC_ID IS NULL) OR a.brief_id IN ('9010','010'))
  AND ((a.brief_id NOT IN ('9010','010') AND vv.OLD_DRAFT_ID IS NULL) OR a.brief_id IN ('9010','010'))
  AND (v.ORIG_NO IS NOT NULL AND v.ORIG_NO <> '')
  AND ((vv.type = '1' AND a.BUSINESS_DATE> '2024-08-31' AND a.apply_date> '2024-08-31') OR (vv.type <> '1' AND a.BUSINESS_DATE> '2024-08-09' AND a.apply_date> '2024-08-09'))
  AND (((a.brief_id IN('6001','9003','9010') AND vv.OLD_DRAFT_ID IS NULL )   ##票据签收6001收票登记，9003收票签收，9010背书签收
   OR a.brief_id IN('002','6004','010','011')     ##003线上开票，010背书，6120兑付确认不推送，011贴现申请
   OR (a.brief_id='004' AND v.bill_status NOT IN ('CS05'))      ## 004应付退票
   OR (a.brief_id='001' AND a.send_sign='0')))     ##001线下开票
  AND (IFNULL(ef.SYNC_STATUS,'F') <> 'S')
ORDER BY a.CREATE_DATE;

##修改日期:2024-11-04
##修改人员:zhous
##修改内容:T24100022275-贴现办理增加业务完是否成字段以及逻辑控制
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9e5760ed66664941bc8ea04fa0c811bf', 'zh_CN', '业务是否完成', 'ab.bill.abbilldiscount.business.isFinish' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'ab.bill.abbilldiscount.business.isFinish' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改日期:2024-11-05
##修改人员:qijh
##系统参数需求清单是否自动提交头寸调拨
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT '2e749bafe2b4419abc048b055aef16ee', 'allot', 'aotuSubmitPos', '0'  FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_config WHERE ID = '2e749bafe2b4419abc048b055aef16ee' LIMIT 1
);
##GOGO

##修改日期:2024-11-05
##修改人员:qijh
##需求清单自动产生头寸调拨指令定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '需求清单自动产生头寸调拨指令', '2024-11-05 14:06:46', '2024-11-05 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'AutoNisDemandForPosJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-11-05 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='需求清单自动产生头寸调拨指令' limit 1);
commit;
##GOGO

##修改日期:2024-11-11
##修改人员:lijq
## 创建菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '409117f06d3243c0a01be7333mnhg512', null, '其他收付款自动记账规则维护', '其他收付款自动记账规则维护', '1d49ea7ce45242c2aaf464d745bea5a0', null, 500, 1, '/paramconfig/fcotherbusinessruledefine/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE ID = '409117f06d3243c0a01be7333mnhg512' LIMIT 1);

##修改日期:2024-11-11
##修改人员:lijq
## 添加资源
INSERT INTO SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'ea046eec81a24a32mkji8491313796a9', '应用资源', '应用资源', 'finacial:basicdata:fcotherbusinessruledefine:apply', 1, '409117f06d3243c0a01be7333mnhg512' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM SYS_RESOURCES  WHERE ID = 'ea046eec81a24a32mkji8491313796a9' LIMIT 1);

##修改日期:2024-11-11
##修改人员:lijq
## 创建 其他收付款记账规则定义 表
CREATE TABLE IF NOT EXISTS `fc_other_business_rule_define` (
    `ID` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '业务规则ID',
    `CODE` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '规则编码',
    `NAME` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '规则名称',
    `NET_ID` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '所属网点',
    `SYS_RACE_CODE` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '业务种类编号',
    `CD_SIGN` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '收支标识',
    `CUR_CODE` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '币别代码',
    `VOUCHER_TYPE` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '凭证种类',
    `VALID_SIGN` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '有效标志',
    `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
    `CREATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '创建人',
    `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
    `UPDATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='其他收付款记账规则定义';

##修改日期:2024-11-11
##修改人员:lijq
## 创建 其他收付款记账匹配规则表
CREATE TABLE IF NOT EXISTS `fc_nis_bus_type_rule` (
    `ID` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'ID',
    `BUS_ID` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'fc_other_business_rule_define规则表ID',
    `MATCH_RULE` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '匹配规则  01-或者  02-并且',
    `MATCH_ELEMENTS` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '匹配要素 remark-摘要、bankAcc-本方账户、accName-本方账户名、oppAccNo-对方账户、oppAccName-对方账户名、curId-币别、corpId-单位名称、uses-用途、postscript-附言',
    `MATCH_RELATIONS` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '匹配关系 1-包含、2-不包含、3-等于、4-不等于',
    `MATCH_CONTENT` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '匹配内容',
    `SORT` int DEFAULT NULL COMMENT '排序',
    `LEFT_PARENTHESIS` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '左括号',
    `RIGHT_PARENTHESIS` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '右括号',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='其他收付款记账匹配规则表';


##修改日期：2024-11-11
##修改人员：lijq
##总账户其他收款流水自动记账定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 17, '总账户其他收款流水自动记账', '2024-11-11 14:06:46', '2024-11-11 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'BtNetOtherReceiveDtlAutoTallyService', '10', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-11-11 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='总账户其他收款流水自动记账' limit 1);
commit;
##GOGO

##修改日期：2024-11-11
##修改人员：lijq
##总账户其他付款流水自动记账定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 17, '总账户其他付款流水自动记账', '2024-11-11 14:06:46', '2024-11-11 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'BtNetOtherPayDtlAutoTallyService', '10', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-11-11 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='总账户其他付款流水自动记账' limit 1);
commit;
##GOGO

##修改日期：2024-11-11
##修改人员：lijq
##总账户资金下拨流水自动记账定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 17, '总账户资金下拨流水自动记账', '2024-11-11 14:06:46', '2024-11-11 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'BtNetDtlDownAutoTallyService', '10', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-11-11 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='总账户资金下拨流水自动记账' limit 1);
commit;
##GOGO

##修改日期:2024-11-11
##修改人员:lijq
##增加字段语句 order_no
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND COLUMN_NAME = 'order_no') THEN
ALTER TABLE BT_NET_CHALK ADD COLUMN order_no VARCHAR(32) NULL COMMENT '流水号';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
##增加字段语句 voucher_no
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND COLUMN_NAME = 'voucher_no') THEN
ALTER TABLE BT_NET_CHALK ADD COLUMN voucher_no VARCHAR(32) NULL COMMENT '记账凭证号';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
##增加字段语句 book_id
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND COLUMN_NAME = 'book_id') THEN
ALTER TABLE BT_NET_CHALK ADD COLUMN book_id VARCHAR(32) NULL COMMENT '记账凭证ID';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
##增加字段语句 tally_date
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND COLUMN_NAME = 'tally_date') THEN
ALTER TABLE BT_NET_CHALK ADD COLUMN tally_date DATETIME NULL COMMENT '记账日期';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
##增加字段语句 operator_code
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND COLUMN_NAME = 'operator_code') THEN
ALTER TABLE BT_NET_CHALK ADD COLUMN operator_code VARCHAR(32) NULL COMMENT '记账员';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
##增加字段语句 Bus_Global_Id
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND COLUMN_NAME = 'Bus_Global_Id') THEN
ALTER TABLE BT_NET_CHALK ADD COLUMN Bus_Global_Id VARCHAR(50) NULL COMMENT '全局业务ID';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
##增加字段语句 checker_code
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND COLUMN_NAME = 'checker_code') THEN
ALTER TABLE BT_NET_CHALK ADD COLUMN checker_code VARCHAR(32) NULL COMMENT '复核员';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
##增加字段语句 check_time
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND COLUMN_NAME = 'check_time') THEN
ALTER TABLE BT_NET_CHALK ADD COLUMN check_time DATETIME NULL COMMENT '复核时间';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
##增加字段语句 TALLY_FAIL_MSG
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND COLUMN_NAME = 'TALLY_FAIL_MSG') THEN
ALTER TABLE BT_NET_CHALK ADD COLUMN TALLY_FAIL_MSG TEXT(20000) NULL COMMENT '自动记账失败原因';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
## BIS_ACC_DTL 增加字段语句 BOOK_ID
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'BOOK_ID') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN BOOK_ID VARCHAR(32) NULL COMMENT '记账凭证FC_ACCBOOK的ID';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2024-11-11
##修改人员:lijq
## BIS_ACC_DTL 增加字段语句 BUS_GLOBAL_ID
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'BUS_GLOBAL_ID') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN BUS_GLOBAL_ID VARCHAR(50) NULL COMMENT '全局业务ID';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2024-11-11
##修改人员:lijq
## BIS_ACC_DTL 增加字段语句 ESTATE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'ESTATE') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN ESTATE VARCHAR(10) NULL COMMENT '记账状态';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期:2024-11-11
##修改人员:lijq
## BIS_ACC_DTL 增加字段语句 TALLY_VOUCHER_NO
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'TALLY_VOUCHER_NO') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN TALLY_VOUCHER_NO VARCHAR(32) NULL COMMENT '自动记账凭证号';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2024-11-11
##修改人员:lijq
## BIS_ACC_DTL 增加字段语句 ORDER_NO
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'ORDER_NO') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN ORDER_NO VARCHAR(32)  NULL COMMENT '流水号';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2024-11-11
##修改人员:lijq
## BIS_ACC_DTL 增加字段语句 TALLY_FAIL_MSG
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'TALLY_FAIL_MSG') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN TALLY_FAIL_MSG TEXT(20000)  NULL COMMENT '自动记账失败原因';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期:2024-11-11
##修改人员:lijq
## BIS_ACC_DTL 增加字段语句 TALLY_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'TALLY_SIGN') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN TALLY_SIGN VARCHAR(1)  NULL COMMENT '记账标识';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO



UPDATE sys_multi_lang SET LANG_CONTEXT = '上划' WHERE LANG_CODE = 'zh_CN' AND LANG_KEY = 'table.btNetChalk.payeeAcc';
UPDATE sys_menu SET URL='/finacial/bankCollect/btnetchalk/list/InitEditTab' WHERE URL='/finacial/bankCollect/btnetchalk/list/Edit';
UPDATE sys_menu SET URL='/finacial/bankCollect/btnetchalk/list/InitCheckTab' WHERE URL='/finacial/bankCollect/btnetchalk/list/Check';


##修改日期：2024-10-30
##修改人员：zhangx
##修改内容：增加网络记账-下拨记账规则
INSERT INTO FC_SYS_BUS_RACE (SYS_RACE_CODE, SYS_RACE_NAME, VALID_SIGN, RMK, ID)
SELECT '6001','网络记账-下拨','1','网络记账-下拨','8b6d4a8da0a011efbd9f005056a84efa' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM FC_SYS_BUS_RACE WHERE ID = '8b6d4a8da0a011efbd9f005056a84efa' LIMIT 1
    );
##GOGO


##修改日期：2024-10-30
##修改人员：zhangx
##修改内容：增加网络记账-下拨字典
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '2a5caf73a09f11efbd9f005056a84efa', '6001', '网络记账-下拨', '1fdfa4327ce2428db845ef57fd22c760', '500' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '2a5caf73a09f11efbd9f005056a84efa' LIMIT 1
    );
##GOGO



##修改日期：2024-10-30
##修改人员：zhangx
##修改内容：流水表 BIS_ACC_DTL 增加 BUS_GLOBAL_ID 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'BUS_GLOBAL_ID') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN BUS_GLOBAL_ID VARCHAR(200) COMMENT '全局业务ID';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;


##修改日期：2024-10-30
##修改人员：zhangx
##修改内容：流水表 BIS_ACC_DTL 增加 ESTATE 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'ESTATE') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN ESTATE VARCHAR(20) COMMENT '记账状态';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;


##修改日期：2024-10-30
##修改人员：zhangx
##修改内容：流水表 BIS_ACC_DTL 增加 OPERATOR_CODE 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'OPERATOR_CODE') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN OPERATOR_CODE VARCHAR(200) COMMENT '记账人';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;


##修改日期：2024-10-30
##修改人员：zhangx
##修改内容：流水表 BIS_ACC_DTL 增加 BOOK_ID 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'BOOK_ID') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN BOOK_ID VARCHAR(120) COMMENT '记账凭证id';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改银企接口收款菜单名为银企接口记账
UPDATE SYS_INTERNATION_BASE SET LANG_CONTEXT = '银企接口记账' WHERE key_id = (SELECT id FROM sys_menu WHERE NAME = '银企接口收款')

##修改日期：2024-11-12
##修改人员：jiangw
##修改内容：银行账号表 BT_BANK_ACC 增加 BINDING_BY_NAME 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'BINDING_BY_NAME') THEN
ALTER TABLE BT_BANK_ACC ADD COLUMN BINDING_BY_NAME VARCHAR(32) COMMENT '绑定人名称';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改日期：2024-11-12
##修改人员：jiangw
##修改内容：银行账号表 BT_BANK_ACC 增加 BINDING_BY_MOBILE 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'BINDING_BY_MOBILE') THEN
ALTER TABLE BT_BANK_ACC ADD COLUMN BINDING_BY_MOBILE VARCHAR(32) COMMENT '绑定人手机';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改日期：2024-11-12
##修改人员：jiangw
##修改内容：银行账号表 BT_BANK_ACC 增加 LOGIN_PASSWORD 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'LOGIN_PASSWORD') THEN
ALTER TABLE BT_BANK_ACC ADD COLUMN LOGIN_PASSWORD VARCHAR(32) COMMENT '登录密码';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改日期：2024-11-12
##修改人员：jiangw
##修改内容：银行账号表 BT_BANK_ACC 增加 PAY_PASSWORD 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'PAY_PASSWORD') THEN
ALTER TABLE BT_BANK_ACC ADD COLUMN PAY_PASSWORD VARCHAR(32) COMMENT '支付密码';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改日期：2024-11-14
##修改人员：zhaohc
##修改内容：添加单位迁移权限资源
INSERT IGNORE INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
VALUES ('7d22b46dbd104d51bf77a85beb010eb9', '网点单位批量迁移', '网点单位批量迁移', 'sys:corp:transfer',
       1, '402880425b951777015b951fd12b0004', NULL, NULL, NULL, NULL,'/sys/corp/transfer');
##GOGO


CREATE TABLE IF NOT EXISTS SYS_PROVINCE
(
    ID  VARCHAR(32) COMMENT '主键',
    PROVINCE_CODE  VARCHAR(64) COMMENT '省区编码',
    PROVINCE_NAME  VARCHAR(128) COMMENT '省区名称',
    STATUS VARCHAR(2) DEFAULT '1' NOT NULL COMMENT '状态:1-启用;0-禁用',
    CREATE_TIME DATETIME NULL DEFAULT NULL,
    CREATE_BY VARCHAR(32) NULL DEFAULT NULL,
    UPDATE_TIME DATETIME NULL DEFAULT NULL,
    UPDATE_BY VARCHAR(32) NULL DEFAULT NULL,
    CONSTRAINT SYS_OPERATION_AREA PRIMARY KEY (ID)
    ) COMMENT '省区维护';
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON,
                                 CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select 'cba9942728054ebeada976fzmc343f8b', null, '省区维护', '省区维护', '833a203b6d2c42bba154e79a11b0e2f6', null, 90, 1,
        '/sys/systemProvince/list', null, null, null, null, null, null, 0, 1, null
from dual where not exists (select 1 from sys_menu where id = 'cba9942728054ebeada976fzmc343f8b');
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '3dab7721dfc642eba70d309763f5e830', '', '省区添加', 'sys:systemProvince:add', 1, 'cba9942728054ebeada976fzmc343f8b', null, '402880425b6b0a58015b6b0abeb10000', null, null, ''
from dual where not exists (select 1 from sys_resources where id = '3dab7721dfc642eba70d309763f5e830');
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '57110927417643cdb2882278de943c7d', '', '省区查询', 'sys:systemProvince:list', 1, 'cba9942728054ebeada976fzmc343f8b', null, '402880425b6b0a58015b6b0abeb10000', null, null, ''
from dual where not exists (select 1 from sys_resources where id = '57110927417643cdb2882278de943c7d');
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '87b6fa96da1a488b95d992e7ca76ace5', '', '省区删除', 'sys:systemProvince:delete', 1, 'cba9942728054ebeada976fzmc343f8b', null, '402880425b6b0a58015b6b0abeb10000', null, null, ''
from dual where not exists (select 1 from sys_resources where id = '87b6fa96da1a488b95d992e7ca76ace5');
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select 'd26bbc67a9104ceab7c27fc70f085c4a', '', '省区修改', 'sys:systemProvince:update', 1, 'cba9942728054ebeada976fzmc343f8b', null, '402880425b6b0a58015b6b0abeb10000', null, null, ''
from dual where not exists (select 1 from sys_resources where id = 'd26bbc67a9104ceab7c27fc70f085c4a');
##GOGO


##修改人:zmc
##修改内容:
##修改时间:
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
                                                                  DATABASE() AND TABLE_NAME = 'SYS_OPERATION_AREA' AND COLUMN_NAME = 'PROVINCE_ID') THEN
ALTER TABLE SYS_OPERATION_AREA ADD COLUMN PROVINCE_ID text comment '省区id';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人:zmc
##修改内容:
##修改时间:
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
                                                                  DATABASE() AND TABLE_NAME = 'SYS_CORP' AND COLUMN_NAME = 'PROVINCE_ID') THEN
ALTER TABLE SYS_CORP ADD COLUMN PROVINCE_ID text comment '省区id';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##修改日期：2024-11-15
##修改人员：zhangx
##修改内容 FC_ACCBOOK_DTL_HIS 修改 ORIG_VOUCHER_NO 字段长度
ALTER TABLE FC_ACCBOOK_DTL_HIS MODIFY ORIG_VOUCHER_NO VARCHAR(100);
ALTER TABLE FC_ACCBOOK_HIS MODIFY ORIG_VOUCHER_NO VARCHAR(100);


##修改日期：2024-11-15
##修改人员：zhangx
##修改内容：增加国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a806cca2a31411efa501005056a84efa', 'zh_CN', '必须是同一交易日期', 'fin.collect.transdate.notsame' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'fin.collect.transdate.notsame' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人:lijq
##修改内容:调整为update_time校验
##修改时间:2025-04-08
CREATE OR REPLACE VIEW DSL_BANK_ACC_INFO_VIEW AS
select a.id                       as BANK_ACC_ID,
       a.bank_acc                 as BANK_ACC,
       case when s.code='100' then '100000' else  s.code end  as CORP_CODE,
       a.acc_name                 as BANK_ACC_NAME,
       c.cur                      as CUR,
       a.ELECTRIC_BILL            as IS_BILL_ACC,
       a.CIRCULATION_MODE         as CIRCULATION_MODE,
       case a.IS_DOMESTIC_BANK when '0' then '1' when '1' then '0' end
                                  as AREA,
       d1.name                    as ACC_TYPE,
       n.nature_name              as ACC_NATURE,
       n.NATURE_CODE              as ACC_NATURE_CODE,
       att.attribute_name         as ACC_ATTRIBUTE,
       att.ATTRIBUTE_CODE         as ACC_ATTRIBUTE_CODE,
       a.BANK_CODE                as BANK_CODE,
       a.BANK_NAME                as BANK_NAME,
       t.BANK_TYPE                as BANK_TYPE_CODE,
       t.TYPE_NAME                as BANK_TYPE,
       r1.name                    as PROV,
       r2.name                    as CITY,
       a.rates                    as rates,
       DATE_FORMAT(a.REG_DATE,'%Y-%m-%d')                 as REG_DATE,
       DATE_FORMAT(a.UPDATE_DATE,'%Y-%m-%d %H:%i:%s')     as UPDATE_DATE,
       DATE_FORMAT(a.CLOSE_DATE,'%Y-%m-%d')               as CLOSE_DATE,
       case when a.status = -2 then '0' else '1' end      as STATUS,
       DATE_FORMAT(a.CHECK_EXPIRE_DATE,'%Y-%m-%d') as CHECK_EXPIRE_DATE,
       a.CREATE_BY                as CREATE_BY,
       a.VALID_SIGN               as VALID_SIGN,
       a.IS_ONLINE                as IS_ONLINE,
       a.IS_CAPITAL_POOL          as IS_CAPITAL_POOL,
       a.rmk                      as rmk
from bt_bank_acc a
         left join (select bc.BANK_ACC_ID,group_concat(c.ENGLISH_CODE) as cur from bt_bank_acc_cur bc left join bt_currency c on c.id = bc.cur_id group by bc.BANK_ACC_ID) c on c.BANK_ACC_ID = a.id
         left join sys_corp s on s.id = a.corp_id
         left join bis_acc_attribute att on a.attribute_id = att.id
         left join bt_acc_nature n on a.nature_id = n.id
         left join bt_bank_type t on t.id = a.BANK_TYPE_ID
         left join sys_region r1 on r1.id = a.prov
         left join sys_region r2 on r2.id = a.city
         left join (select d.TYPE_CODE as code, d.TYPE_NAME as name from sys_dict_group g inner join sys_dict d on g.id = d.TYPE_GROUP_ID where g.TYPE_GROUP_CODE = 'btBankAcc.accType') d1 on d1.code = a.ACC_TYPE
;

##修改日期:2024-11-19
##修改人员:qijh
##修改CHO_PRODUCT_REDEEM_DETAIL中INCOME_RATE
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_DEMAND' AND COLUMN_NAME = 'IS_POS') THEN
ALTER TABLE NIS_DEMAND ADD COLUMN IS_POS VARCHAR (10) DEFAULT '0';
ELSE
ALTER TABLE NIS_DEMAND MODIFY COLUMN IS_POS VARCHAR(10) DEFAULT '0';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改日期:2024-11-25
##修改人员:jiangw
##退款勾兑中间表
CREATE TABLE IF NOT EXISTS `nis_bill_bank_serial_no_refund` (
  `ID` int UNSIGNED auto_increment NOT NULL COMMENT 'id',
  `BILL_ID` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '付款单id',
  `BANK_SERIAL_NO` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '银行流水号',
  `IS_FROM` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '来源',
  `DTL_ID` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '流水id',
  `PAYMENTBLENDING_DATE` datetime(6) DEFAULT null COMMENT '勾兑时间',
  PRIMARY KEY (`ID`),
  KEY `IDX_NIS_BILL_BANK_SERIAL_NO_B_SNO` (`BANK_SERIAL_NO`) USING BTREE,
  KEY `IDX_NIS_BILL_BANK_SERIAL_NO_B_ID` (`BILL_ID`,`IS_FROM`) USING BTREE,
  KEY `IDX_NIS_BILL_BANK_SERIAL_NO_DTL_ID` (`DTL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='退款勾兑中间表'
##GOGO

##修改日期 2024-12-11
##修改人员 zhengg
##修改内容 票据主表添加备注字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'RMK') THEN
ALTER TABLE AB_VOUCHERINFO ADD RMK VARCHAR(500);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##修改日期：2024-12-11
##修改人员：lijq
##修改内容：流水表 BIS_ACC_DTL 增加 GX_DTL_SYNC_STATUS 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'GX_DTL_SYNC_STATUS') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN GX_DTL_SYNC_STATUS VARCHAR(5) COMMENT '共享数据发送状态 F失败 S成功';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改日期：2024-12-11
##修改人员：lijq
##修改内容：流水表 BIS_ACC_DTL 增加 GX_OP_TIME 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'GX_OP_TIME') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN GX_OP_TIME DATETIME COMMENT '共享流水推送时间';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;

##修改日期：2024-12-11
##修改人员：lijq
##修改内容：流水表 BIS_ACC_DTL 增加 GX_SOURCE_NUM 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'GX_SOURCE_NUM') THEN
ALTER TABLE BIS_ACC_DTL ADD COLUMN GX_SOURCE_NUM VARCHAR(100) COMMENT '共享共享单号';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;


##修改日期：2024-12-30
##修改人员：lijq
##修改内容：大参林共享全量流水同步接口视图, 退款勾兑没有进行数据推送 问题处理
CREATE OR REPLACE VIEW DSL_GX_BIS_ACC_DTL_VIEW AS
SELECT dtl.id                                AS seqId,
       dtl.BANK_SERIAL_NO                    AS yhlsh,
       CASE WHEN  bc.code  = '100' THEN  '100000'
            ELSE bc.code END                 AS dwbh,
       bc.name                               AS dwmc,
       CASE
           WHEN dtl.cd_sign =  '1' THEN '02'
           ELSE '01' END                     AS incomeExpenseType,
       dtl.BANK_ACC                          AS zhbh,
       dtl.ACC_NAME                          AS zhmc,
       acc.BANK_CODE                         AS lhh,
       dtl.BANK_NAME                         AS khh,
       dtl.amt                               AS je,
       dtl.OPP_ACC_NO                        AS dfzh,
       dtl.OPP_ACC_NAME                      AS dfhm,
       dtl.OPP_ACC_BANK                      AS dfkhh,
       c.ENGLISH_CODE                        as currencyCode,
       DATE_FORMAT(dtl.TRANS_TIME, '%Y/%m/%d') AS jyrq,
       DATE_FORMAT(dtl.TRANS_TIME, '%Y/%m/%d %H:%i:%s') AS jysj,
       dtl.REMARK                            AS yt,
       dtl.POSTSCRIPT                        as summary,
       dtl.USES                              as purpose,
       nt.BUS_TYPE                           as barCodeType,
       nt.BUS_TYPE_NAME                      as billTypeDesc,
       nb.BURSAR_BILL_NO                     AS sourceNum,
       case when r.bill_id is not null then 'Y' else 'N' end as isReturn
FROM bis_acc_dtl dtl
         LEFT JOIN nis_bill_bank_serial_no_refund r on r.dtl_id = dtl.id
         LEFT JOIN NIS_BILL_BANK_SERIAL_NO ns ON dtl.id=ns.dtl_id
         LEFT JOIN nis_billhead nb ON (nb.id=ns.bill_id or nb.id=r.bill_id)
         LEFT JOIN nis_bus_type nt on nb.bus_type = nt.id
         LEFT JOIN bt_bank_acc acc ON dtl.bank_acc = acc.bank_acc
         LEFT JOIN sys_corp bc ON bc.id = acc.corp_id
         LEFT JOIN BIS_BIF_INIT bi ON bi.id = dtl.bif_code
         LEFT JOIN bt_currency c on c.id = dtl.cur_id
WHERE 1=1
  AND ((`dtl`.`TRANS_TIME` > '2024-07-20' AND `dtl`.`bif_code`!='aef6125b6ea2497db16038e7f9e087e7') OR (`dtl`.`TRANS_TIME` > '2024-07-24' AND `dtl`.`bif_code`='aef6125b6ea2497db16038e7f9e087e7'))
  AND (IFNULL(dtl.GX_DTL_SYNC_STATUS,'F') <> 'S' OR (IFNULL(nb.BURSAR_BILL_NO,'') <> IFNULL(dtl.GX_SOURCE_NUM,'')));

##修改日期：2024-12-13
##修改人员：fangcf
##修改内容：还款方式新增还本时还息字段

##修还款方式表CMS_REPAY_TYPE,添加字段 INTEREST_REPAY_TYPE
##增加字段语句
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'CMS_REPAY_TYPE' AND COLUMN_NAME = 'INTEREST_REPAY_TYPE') THEN
ALTER TABLE CMS_REPAY_TYPE ADD COLUMN INTEREST_REPAY_TYPE VARCHAR(5) COMMENT '还本时还息';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2311448b1c2740ec918f30421c129089', 'zh_CN', '还本金时支付利息', 'table.repayType.interestRepayType' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.repayType.interestRepayType' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9af0f5f38bb24ae3967c1f1d7579be38', 'zh_CN', '利息计算方式', 'table.repayType.interestWay' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.repayType.interestWay' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##字典组
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '4fb743bc0cab445c8e825d6af460d7bc', 'repayType.interestRepayType', '还本金时支付利息', null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID='4fb743bc0cab445c8e825d6af460d7bc' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT 'bf6bc8830ac14333856dd8c125ea5f10', '0', '否', null, '4fb743bc0cab445c8e825d6af460d7bc', 1, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='bf6bc8830ac14333856dd8c125ea5f10' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT 'c9f51e9959894e8f9f2a29b641b6087f', '1', '是', null, '4fb743bc0cab445c8e825d6af460d7bc', 2, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='c9f51e9959894e8f9f2a29b641b6087f' LIMIT 1
);
##GOGO

##字典组
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT 'f0dd091e22a54fa28faf335f81516ded', 'repayType.interestWay', '还款方式利息计算方式', null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID='f0dd091e22a54fa28faf335f81516ded' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '31512fa9b3944c81bd66e53cd5803c0f', '0', '按实际天数', null, 'f0dd091e22a54fa28faf335f81516ded', 1, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='31512fa9b3944c81bd66e53cd5803c0f' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '0370afe05a334efe933af93227ec0089', '1', '按算数平均', null, 'f0dd091e22a54fa28faf335f81516ded', 2, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='0370afe05a334efe933af93227ec0089' LIMIT 1
);
##GOGO

##修改时间: 2024-12-13
##修改人: fangcf
##修改原因: 还本时付息,还款利息明细

##修还款方式表CMS_REPAY_TYPE,添加字段 INTEREST_REPAY_TYPE_NEXT
##增加字段语句
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'CMS_REPAY_TYPE' AND COLUMN_NAME = 'INTEREST_REPAY_TYPE_NEXT') THEN
ALTER TABLE CMS_REPAY_TYPE ADD COLUMN INTEREST_REPAY_TYPE_NEXT VARCHAR(5) COMMENT '还本时还息详情';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##更新还款方式新加字段还本时还息的原模式
update CMS_REPAY_TYPE set INTEREST_REPAY_TYPE_NEXT='1' where INTEREST_REPAY_TYPE='1' and INTEREST_REPAY_TYPE_NEXT is null;
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6f714c84d90a4d5384715d7f2918b2d1', 'zh_CN', '剩余应还本金(原币)', 'bi.interest.report.repayRemainMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bi.interest.report.repayRemainMoney' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人: fangcf
##修改内容: 增加国际化
##修改时间: 2024-12-13
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6f714c84d90a4d5384715d7f2918b2d2', 'zh_CN', '还本方式', 'bi.interest.report.repayWay' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bi.interest.report.repayWay' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人: fangcf
##修改内容: 增加国际化
##修改时间: 2024-12-13
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6f714c84d90a4d5384715d7f2918b2d3', 'zh_CN', '计息利率(%)', 'bi.interest.report.rate' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bi.interest.report.rate' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人: fangcf
##修改内容: 增加国际化
##修改时间: 2024-12-13
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6f714c84d90a4d5384715d7f2918b2d4', 'zh_CN', '一次性还本', 'bi.interest.report.repayWayOne' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bi.interest.report.repayWayOne' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人: fangcf
##修改内容: 增加国际化
##修改时间: 2024-12-13
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6f714c84d90a4d5384715d7f2918b2d5', 'zh_CN', '分期还本', 'bi.interest.report.repayWayMore' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bi.interest.report.repayWayMore' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO


##字典组
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '21d7da02b7724b92945f1abde8470705', 'repayType.interestRepayType.next', '还本时是否另外付息详情', null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID='21d7da02b7724b92945f1abde8470705' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '755e4cb89b314282bd449f8e4170f32b', '0', '付剩余本金产生的利息', null, '21d7da02b7724b92945f1abde8470705', 1, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='755e4cb89b314282bd449f8e4170f32b' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT 'e3210e752f264e1ebd9ecd1e618917ab', '1', '付还款本金产生的利息', null, '21d7da02b7724b92945f1abde8470705', 2, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='e3210e752f264e1ebd9ecd1e618917ab' LIMIT 1
);
##GOGO

##修改人: fangcf
##修改时间: 2024.12.13
##修改原因: 还款方式新增本月/本季是否计息

##修还款方式表CMS_REPAY_TYPE,添加字段 INTEREST_FREQUENCY_THIS
##增加字段语句
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'CMS_REPAY_TYPE' AND COLUMN_NAME = 'INTEREST_FREQUENCY_THIS') THEN
ALTER TABLE CMS_REPAY_TYPE ADD COLUMN INTEREST_FREQUENCY_THIS VARCHAR(2) COMMENT '是否当月/当季计息';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

UPDATE CMS_REPAY_TYPE set INTEREST_FREQUENCY_THIS='0' where INTEREST_FREQUENCY_THIS is null and INTEREST_FREQUENCY_NEXT in ('3');
COMMIT;
#GOGO

UPDATE CMS_REPAY_TYPE set INTEREST_FREQUENCY_THIS='1' where INTEREST_FREQUENCY_THIS is null and INTEREST_FREQUENCY_NEXT in ('4');
COMMIT;
#GOGO

##字典组
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '4fb743bc0cab445c8e825d6af460d7b1', 'accrual.month.thisinterest', '还款方式本月是否计息', null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID='4fb743bc0cab445c8e825d6af460d7b1' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '4fb743bc0cab445c8e825d6af460d7b2', '0', '当月不计息', null, '4fb743bc0cab445c8e825d6af460d7b1', 1, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='4fb743bc0cab445c8e825d6af460d7b2' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '4fb743bc0cab445c8e825d6af460d7b3', '1', '当月计息', null, '4fb743bc0cab445c8e825d6af460d7b1', 2, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='4fb743bc0cab445c8e825d6af460d7b3' LIMIT 1
);
##GOGO


##字典组
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '4fb743bc0cab445c8e825d6af460d7b5', 'accrual.season.thisinterest', '还款方式本季是否计息', null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID='4fb743bc0cab445c8e825d6af460d7b5' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '4fb743bc0cab445c8e825d6af460d7b6', '0', '当季不计息', null, '4fb743bc0cab445c8e825d6af460d7b5', 1, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='4fb743bc0cab445c8e825d6af460d7b6' LIMIT 1
);
##GOGO
##字典
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '4fb743bc0cab445c8e825d6af460d7b7', '1', '当季计息', null, '4fb743bc0cab445c8e825d6af460d7b5', 2, null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID='4fb743bc0cab445c8e825d6af460d7b7' LIMIT 1
);
##GOGO

##修改人: fangcf
##修改时间: 2024.12.13
##修改原因: 增加字段

##在CMS_REPAY_TYPE表中增加字段:CAPITAL_FREQUENCY_THIRD 本金支付频率第三列(年专用)
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'CMS_REPAY_TYPE' AND COLUMN_NAME = 'CAPITAL_FREQUENCY_THIRD') THEN
ALTER TABLE CMS_REPAY_TYPE ADD COLUMN CAPITAL_FREQUENCY_THIRD VARCHAR(32) COMMENT '本金支付频率(第三列年专用)';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##在CMS_REPAY_TYPE表中增加字段:INTEREST_FREQUENCY_THIRD 利息支付频率第三列(年专用)
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'CMS_REPAY_TYPE' AND COLUMN_NAME = 'INTEREST_FREQUENCY_THIRD') THEN
ALTER TABLE CMS_REPAY_TYPE ADD COLUMN INTEREST_FREQUENCY_THIRD VARCHAR(32) COMMENT '利息支付频率(第三列年专用)';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT  'a1fe7f72f6c7490396d84dd532ab9ca1', 'accrual.yearType', '还款方式年类型', NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = 'a1fe7f72f6c7490396d84dd532ab9ca1' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT 'e1e127ff63544d50ad62a0c785339764', '6', '半年（6月，12月）', NULL, 'a1fe7f72f6c7490396d84dd532ab9ca1', 1, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = 'e1e127ff63544d50ad62a0c785339764' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT 'bcdfc7c87097485fae2d5c4e50e46fc0', '12', '年末（12月）', NULL, 'a1fe7f72f6c7490396d84dd532ab9ca1', 2, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = 'bcdfc7c87097485fae2d5c4e50e46fc0' LIMIT 1
);
##GOGO

INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT  '3659dfe13b074c9599c7bd94a673251e', 'accrual.year.thisinterest', '还款方式年当期是否计息', NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = '3659dfe13b074c9599c7bd94a673251e' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '83d47774ad8745b891614c89a51dd9a9', '0', '当期不计息', NULL, '3659dfe13b074c9599c7bd94a673251e', 1, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '83d47774ad8745b891614c89a51dd9a9' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '92778d5c35a040c4834d5a7e4b9c679d', '1', '当期计息', NULL, '3659dfe13b074c9599c7bd94a673251e', 2, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '92778d5c35a040c4834d5a7e4b9c679d' LIMIT 1
);
##GOGO

##删除还款方式季中的半年
delete FROM sys_dict where type_group_id='a317802682214725a3e0723484e98d9c' and id='185416a01c5849d1b99f1c05db72645d';
COMMIT;
##GOGO

##将半年从季改为年历史数据处理

update CMS_REPAY_TYPE set CAPITAL_FREQUENCY_NEXT='5',CAPITAL_FREQUENCY_THIRD='6' where  CAPITAL_FREQUENCY_TYPE='D' and CAPITAL_FREQUENCY_NEXT='4' and CAPITAL_FREQUENCY='2';
COMMIT;
##GOGO
update CMS_REPAY_TYPE set INTEREST_FREQUENCY_NEXT='5',INTEREST_FREQUENCY_THIRD='6' where  INTEREST_FREQUENCY_TYPE='D' and INTEREST_FREQUENCY_NEXT='4' and INTEREST_FREQUENCY='2';
COMMIT;
##GOGO
update CMS_REPAY_TYPE set CAPITAL_FREQUENCY_THIRD='12' where CAPITAL_FREQUENCY_TYPE='D' and  CAPITAL_FREQUENCY_NEXT='5' and CAPITAL_FREQUENCY_THIRD is null;
COMMIT;
##GOGO
update CMS_REPAY_TYPE set INTEREST_FREQUENCY_THIRD='12' where INTEREST_FREQUENCY_TYPE='D' and  INTEREST_FREQUENCY_NEXT='5' and INTEREST_FREQUENCY_THIRD is null;
COMMIT;
##GOGO

insert into SYS_DICT (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'cb7038ae5b4a43ce91472790b47eba86', '0', '季初（1月，4月，7月，10月）', null, 'a317802682214725a3e0723484e98d9c', 1 from dual
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT WHERE id = 'cb7038ae5b4a43ce91472790b47eba86' LIMIT 1
);
##GOGO


##修改人: fangcf
##修改时间: 2024-12-13
##修改内容: 还款方式脚本

insert into SYS_DICT (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'cb7038ae5b4a43ce91472790b47eba87', '3', '季中（2月，5月，8月，11月）', null, 'a317802682214725a3e0723484e98d9c', 5 from dual
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT WHERE id = 'cb7038ae5b4a43ce91472790b47eba87' LIMIT 1
);
##GOGO

UPDATE sys_dict SET  TYPE_NAME = '季末（3月，6月，9月，12月）' WHERE ID = '8bccaf6bd63b465487c1827d19f9a75d';
COMMIT;
##GOGO

INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT  '7d92c86232d94fd89376b38b9e6d842b', 'cms.repayType.interestDay', '还款方式计息日', NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = '7d92c86232d94fd89376b38b9e6d842b' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc01', '1', '第1天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 1, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc01' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc02', '2', '第2天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 2, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc02' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc03', '3', '第3天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 3, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc03' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc04', '4', '第4天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 4, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc04' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc05', '5', '第5天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 5, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc05' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc06', '6', '第6天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 6, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc06' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc07', '7', '第7天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 7, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc07' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc08', '8', '第8天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 8, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc08' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc09', '9', '第9天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 9, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc09' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc10', '10', '第10天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 10, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc10' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc11', '11', '第11天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 11, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc11' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc12', '12', '第12天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 12, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc12' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc13', '13', '第13天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 13, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc13' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc14', '14', '第14天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 14, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc14' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc15', '15', '第15天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 15, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc15' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc16', '16', '第16天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 16, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc16' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc17', '17', '第17天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 17, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc17' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc18', '18', '第18天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 18, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc18' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc19', '19', '第19天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 19, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc19' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc20', '20', '第20天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 20, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc20' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc21', '21', '第21天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 21, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc21' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc22', '22', '第22天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 22, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc22' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc23', '23', '第23天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 23, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc23' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc24', '24', '第24天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 24, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc24' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc25', '25', '第25天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 25, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc25' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc26', '26', '第26天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 26, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc26' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc27', '27', '第27天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 27, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc27' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc28', '28', '第28天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 28, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc28' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc29', '29', '第29天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 29, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc29' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc30', '30', '第30天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 30, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc30' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc31', '31', '月末最后一天', NULL, '7d92c86232d94fd89376b38b9e6d842b', 31, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc31' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '47822575232b449997dca0e7ab8fbc35', '35', '同发放日', NULL, '7d92c86232d94fd89376b38b9e6d842b', 35, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '47822575232b449997dca0e7ab8fbc35' LIMIT 1
);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
SELECT 'b50f3fc5ef064c6293d44e3fd8cfadb1', 'zh_CN', '利息按本金计算', 'table.repay.capitalAndInterest.money', null, null, null, null, null, null  FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = 'b50f3fc5ef064c6293d44e3fd8cfadb1' LIMIT 1
);
##GOGO



##修改日期 2024-12-16
##修改人员 zhengg
##修改内容 网银信息维护添加网银登录名字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_NET_UKEY' AND COLUMN_NAME = 'EBANK_LOGIN_NAME') THEN
ALTER TABLE BT_NET_UKEY ADD EBANK_LOGIN_NAME VARCHAR(100);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##修改人:zrs
##修改内容:修改字段类型
##修改时间:2024-12-16
ALTER TABLE ERP_PAYINFO MODIFY COLUMN BUS_TYPE varchar (100) DEFAULT NULL;
##GOGO

##修改人员：lijq
##共享全量银行流水推送 定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 7, '共享流水同步接口', '2024-12-12 14:06:46', '2024-12-12 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'DSLGXBisAccDtl', '{"dsl_gx_bis_acc_dtl_url": "","dsl_gx_bis_acc_dtl_page_size": 300}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-12-12 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='共享流水同步接口' limit 1);
commit;
##GOGO

##修改日期 2024-12-18
##修改人员 zhous
##修改内容 查询指令表添加票据类型字段 查询指令表添加签收类型字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_DRAFT_EXC' AND COLUMN_NAME = 'BILL_TYPE') THEN
ALTER TABLE AB_DRAFT_EXC ADD BILL_TYPE VARCHAR(5);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_DRAFT_EXC' AND COLUMN_NAME = 'SIGN_TYPE') THEN
ALTER TABLE AB_DRAFT_EXC ADD SIGN_TYPE VARCHAR(10);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '048067f042df460bba53f54457344203', 'zh_CN', '发起指令查询', 'table.abVoucherinfo.sendCommand' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.abVoucherinfo.sendCommand' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '86fa2031e9b04715a8ebb79478f752e3', 'zh_CN', '查询账号为空', 'payerAcc.is.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'payerAcc.is.null' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'dcefceed272f474bbffa0687d38dc4ce', 'zh_CN', '票据类型为空', 'billType.is.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'billType.is.null' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '49d01532ef0a45188a5b9d84b22e096b', 'zh_CN', '签收类型为空', 'signType.is.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'signType.is.null' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7ac44be3782f4b37ab011b87cbf9677a', 'zh_CN', '票据账号', 'table.abVoucherinfo.billBankAcc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.abVoucherinfo.billBankAcc' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改日期：2024-12-25
##修改人员：zrs
##头寸调拨审批企微消息提醒定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '头寸调拨审批企微消息提醒', null, null, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'NisallocationPosApproveJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='头寸调拨审批企微消息提醒' limit 1);
commit;
##GOGO

##企业微信-结算管理-请款审批消息提醒
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT '01b14b96c26111ef889a005056a84efa', '183fc820962c411e9581ce3a7db5f552', '头寸调拨审批企微消息提醒', 'netbank:allocation:pos:approve', '', '', null, '', 0, 10, '消息提醒内容：\\n业务类型：资金平台头寸调拨审批\\n日期：$!{date}\\n请款笔数：$!{count}笔', 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '01b14b96c26111ef889a005056a84efa' LIMIT 1
);

##修改日期 2024-12-25
##修改人员 xionggf
##修改内容 其他收付款自动记账规则维护添加内部账号字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'FC_VOUCHER_ENTRY_DEFINE' AND COLUMN_NAME = 'BLNCTR_ACC') THEN
ALTER TABLE FC_VOUCHER_ENTRY_DEFINE ADD BLNCTR_ACC VARCHAR(50) COMMENT '内部账号';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期：2024-12-27
##修改人员 qijh
##保证金占用表新增释放日期
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'FC_BOND_APPLY' AND COLUMN_NAME = 'RELEASE_DATE') THEN
ALTER TABLE FC_BOND_APPLY ADD COLUMN RELEASE_DATE datetime comment '释放日期';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期：2024-12-27
##修改人员：qijh
##释放保证金自动转回定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 8, '释放保证金自动转回', '2024-12-27 14:06:46', '2024-12-27 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'AutoFcBondRotationJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-12-27 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='释放保证金自动转回' limit 1);
commit;
##GOGO

##修改日期：2024-12-27
##修改人员 qijh
##保证金占用表新增自动回转标志
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'FC_BOND_APPLY' AND COLUMN_NAME = 'AUTO_SIGN') THEN
ALTER TABLE FC_BOND_APPLY ADD COLUMN AUTO_SIGN varchar(1) comment '自动回转标志';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期：2024-01-02
##修改人员 jiangw
##修改“网银U-key到期提醒”
update sys_notification_category set CONTENT='业务类型:账户管理\\n业务描述:账户Ukey到期提醒\\n到期日期:$!{expireDate}\\n账户名称:$!{accName}\\n银行账号:$!{bankAcc}\\n账号银行:$!{bankName}\\n个人权限:$!{personalLimit}\\nUkey编号:$!{ukeyNo}' where id='e7246d21c3e14e50a98dd2ae8f99ec91';
##GOGO

##修改日期 2025-01-07
##修改人员：lijq
##授信合同同步定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 7, '授信合同同步定时任务', '2025-01-07 14:06:46', '2025-01-07 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'DSLRatBkration', '{"dsl_rat_bkration_url": "","validDate": "2025-01-07"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-01-07 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='授信合同同步定时任务' limit 1);
commit;
##GOGO

##修改日期：2025-01-07
##修改人员：zhangx
##修改内容：增加大参林银行借款合同同步接口
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info),'7','大参林银行借款合同同步接口','2025-01-07 17:36:09','2025-01-07 17:38:16','XXL','','CRON','0 0 9 * * ?','DO_NOTHING','FIRST','DSLCmsProvideLoanInfoSync','{\r\n    \"dsl_cms_provide_loan_info_sync_url\": \"ddd\",\r\n    \"dsl_cms_provide_loan_info_sync_date\": \"2025-01-01\"\r\n}','SERIAL_EXECUTION','0','0','BEAN','','GLUE代码初始化','2025-01-07 17:36:09','','0','0','0' from dual
where not exists (select 1 from xxl_job_info where job_desc ='大参林银行借款合同同步接口' limit 1);
commit;
##GOGO


##修改日期 2025-01-08
##修改人员：lijq
##理财合同同步定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 7, '理财合同同步定时任务', '2025-01-08 14:06:46', '2025-01-08 14:06:46', 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'DSLChoProductReq', '{"dsl_cho_product_req_url": "","validDate": "2025-01-08"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-01-08 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='理财合同同步定时任务' limit 1);
commit;
##GOGO

##修改日期 2025-01-08
##修改人员：lijq
##增加字段语句 CHO_PRODUCT_REQ 添加 FINAN_ORG
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'CHO_PRODUCT_REQ' AND COLUMN_NAME = 'FINAN_ORG') THEN
ALTER TABLE CHO_PRODUCT_REQ ADD COLUMN FINAN_ORG VARCHAR(50) NULL COMMENT '购买机构 001银行 002券商';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##修改日期：2025-01-10
##修改人员：zhangx
##修改内容：共享系统票据同步接口
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select(select max(id)+1 from xxl_job_info),'7','共享系统票据同步接口','2025-01-10 17:24:08','2025-01-10 17:24:50','XXL','','CRON','0 0 9 * * ?','DO_NOTHING','FIRST','DSLAbBillShareSync','{\r\n    \"dsl_ab_bill_share_sync_url\": \"ddd\",\r\n    \"dsl_ab_bill_share_sync_page_size\": 5\r\n}','SERIAL_EXECUTION','0','0','BEAN','','GLUE代码初始化','2025-01-10 17:24:08','','0','0','0' from dual
where not exists (select 1 from xxl_job_info where job_desc ='共享系统票据同步接口' limit 1);
commit;
##GOGO





##修改日期 2025-01-10
##修改人员：zrs
##开户办理增加绑定人权限
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '96a7b544cf2911efaf25005056a84efa', '绑定人权限', '绑定人权限', 'netbank:bankAcc:btBankAcc:binder', 1, '402880265c67cc3c015c6837f65e0045', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='netbank:bankAcc:btBankAcc:binder' limit 1);
commit;
##GOGO

## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '登录密码或支付密码加密失败！', 'basic.business.message.btbankacc.encryption.failure' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='basic.business.message.btbankacc.encryption.failure' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改日期 2025-01-14
##修改人员：zrs
## 增加资源权限
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select 'd65f6a95d23b11ef9ced005056a84efa', '查询资源', '查询资源', 'netbank:bankAcc:statistics:query', 1, '9728335cf9074f9ba1c346a2ac5f77bd', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='netbank:bankAcc:statistics:query' limit 1);
commit;
##GOGO

##修改人:zrs
##修改内容:修改字段类型
##修改时间:2025-1-21
ALTER TABLE BT_BANK_ACC MODIFY COLUMN LOGIN_PASSWORD varchar (200) DEFAULT NULL;
ALTER TABLE BT_BANK_ACC MODIFY COLUMN PAY_PASSWORD varchar (200) DEFAULT NULL;
##GOGO

##修改日期：2025-02-07
##修改人员：zhous
##修改内容：兑付状态自动认领成功的票据，自动进行到期线下解付定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select(select max(id)+1 from xxl_job_info),'3','票据到期自动解付','2025-02-07 17:24:08','2025-02-07 17:24:50','XXL','','CRON','0 0 0 * * ?','DO_NOTHING','FIRST','AbEmitBillPayService','claimPay','SERIAL_EXECUTION','0','0','BEAN','','GLUE代码初始化','2025-02-07 17:24:08','','0','0','0' from dual
where not exists (select 1 from xxl_job_info where job_desc ='票据到期自动解付' limit 1);
commit;
##GOGO

##修改日期:2025-02-07
##修改人员:lijq
##其他收付款记账规则定义表，编码字段长度加长、设置为必填
ALTER TABLE `fc_other_business_rule_define`
    CHANGE COLUMN `CODE` `CODE` VARCHAR(15) NOT NULL COMMENT '规则编码' ;

##修改日期 2025-02-06
##修改人员:chenk
## 增加菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '9471dcd57a924d72955e3a3cf4e90e87', NULL, '账户业务笔数汇总表', '账户业务笔数汇总表', '07cbbb8132de427eb53c9d624fb7ee22', NULL, 500, 1, '/report/bisaccdtlCount/list', NULL, NULL, NULL, NULL, NULL, NULL, 0, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE ID = '9471dcd57a924d72955e3a3cf4e90e87' LIMIT 1);


##修改日期 2025-02-06
##修改人员:chenk
## 增加菜单资源权限
INSERT INTO SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'fa443820a5ee4d859fe6ca60fcf7299c', '查询资源', '查询资源', 'report:bisaccdtl:count:query', 1, '9471dcd57a924d72955e3a3cf4e90e87' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM SYS_RESOURCES  WHERE ID = 'fa443820a5ee4d859fe6ca60fcf7299c' LIMIT 1);

##修改人：zhours
##修改时间：2025.02.07
##增加：集团资金划拨年度分析表

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '609b6f48e51911efb223005056a84efa', '', '集团资金划拨年度分析表', '集团资金划拨年度分析表', '07cbbb8132de427eb53c9d624fb7ee22', '', 24, 1, '/report/nisAllocationReq/yearList', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='集团资金划拨年度分析表' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '6d48663de51911efb223005056a84efa', '查询资源', '查询资源', 'report:groupTransfer:year', 1, '609b6f48e51911efb223005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='report:groupTransfer:year' limit 1);
commit;
##GOGO

## 增加索引
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_IND()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME = 'NIS_BUS_TYPE' AND INDEX_NAME = 'IDX_TYPE_ID_BUS') THEN
ALTER TABLE NIS_BUS_TYPE ADD INDEX IDX_TYPE_ID_BUS (BUS_TYPE, ID);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_IND();
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_IND;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_IND()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND INDEX_NAME = 'IDX_DTL_CORP_TRANS') THEN
ALTER TABLE BIS_ACC_DTL ADD INDEX IDX_DTL_CORP_TRANS  (CORP_ID, TRANS_TIME);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_IND();
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
##GOGO


##修改日期 2025-02-07
##修改人员:chenk
##增加字段语句 BT_NET_UKEY 添加 IS_HEAD_CUSTODIAN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BT_NET_UKEY' AND COLUMN_NAME = 'IS_HEAD_CUSTODIAN') THEN
ALTER TABLE BT_NET_UKEY ADD COLUMN IS_HEAD_CUSTODIAN VARCHAR(2) NULL COMMENT '是否总部保管';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改日期：2025-02-10
##修改人员：lijq
##修改内容：大参林共享全量流水同步接口视图-增加补推逻辑
CREATE OR REPLACE VIEW DSL_GX_BIS_ACC_DTL_VIEW AS
SELECT dtl.id                                AS seqId,
       dtl.BANK_SERIAL_NO                    AS yhlsh,
       CASE WHEN  bc.code  = '100' THEN  '100000'
            ELSE bc.code END                 AS dwbh,
       bc.name                               AS dwmc,
       CASE
           WHEN dtl.cd_sign =  '1' THEN '02'
           ELSE '01' END                     AS incomeExpenseType,
       dtl.BANK_ACC                          AS zhbh,
       dtl.ACC_NAME                          AS zhmc,
       acc.BANK_CODE                         AS lhh,
       dtl.BANK_NAME                         AS khh,
       dtl.amt                               AS je,
       dtl.OPP_ACC_NO                        AS dfzh,
       dtl.OPP_ACC_NAME                      AS dfhm,
       dtl.OPP_ACC_BANK                      AS dfkhh,
       c.ENGLISH_CODE                        AS currencyCode,
       DATE_FORMAT(dtl.TRANS_TIME, '%Y/%m/%d') AS jyrq,
       DATE_FORMAT(dtl.TRANS_TIME, '%Y/%m/%d %H:%i:%s') AS jysj,
       dtl.REMARK                            AS yt,
       dtl.POSTSCRIPT                        AS summary,
       dtl.USES                              AS purpose,
       nt.BUS_TYPE                           AS barCodeType,
       nt.BUS_TYPE_NAME                      AS billTypeDesc,
       nb.BURSAR_BILL_NO                     AS sourceNum,
       CASE WHEN ns.isReturn IS NULL THEN 'N' ELSE ns.isReturn END AS isReturn
FROM bis_acc_dtl dtl
         LEFT JOIN (SELECT dtl_id,bill_id,'Y' AS isReturn FROM nis_bill_bank_serial_no_refund UNION SELECT dtl_id,bill_id, 'N' AS isReturn FROM NIS_BILL_BANK_SERIAL_NO) ns ON dtl.id=ns.dtl_id
         LEFT JOIN nis_billhead nb ON nb.id=ns.bill_id
         LEFT JOIN nis_bus_type nt ON nb.bus_type = nt.id
         LEFT JOIN bt_bank_acc acc ON dtl.bank_acc = acc.bank_acc
         LEFT JOIN sys_corp bc ON bc.id = acc.corp_id
         LEFT JOIN BIS_BIF_INIT bi ON bi.id = dtl.bif_code
         LEFT JOIN bt_currency c ON c.id = dtl.cur_id
WHERE 1=1
  AND (`dtl`.`TRANS_TIME` >='2025-02-01 00:00:00.000000' )
  AND dtl.`CREATE_DATE`>'2025-02-07 15:00:00'
  AND (IFNULL(dtl.`DTL_SYNC_STATUS`,'F') )<> 'S'
  AND dtl.BANK_ACC<>'15999329560036'
  AND (IFNULL(dtl.GX_DTL_SYNC_STATUS,'F') <> 'S' OR (IFNULL(nb.BURSAR_BILL_NO,'') <> IFNULL(dtl.GX_SOURCE_NUM,'')));

##修改日期：2025-02-10
##修改人员：lijq
##修改内容：大参林联行号查询视图，银行类型名称和编码没有匹配上的加了个默认值
CREATE OR REPLACE VIEW DSL_BT_INPUT_BANK_INFO_VIEW AS
SELECT
    `b`.`SYS_BANK_CODE` AS `SYS_BANK_CODE`,
    `b`.`BANK_NAME`     AS `BANK_NAME`,
    `b`.`UPDATE_DATE`   AS `UPDATE_DATE`,
    `b`.`CREATE_DATE`   AS `CREATE_DATE`,
    CASE WHEN `t`.`BANK_TYPE` IS NULL THEN 'DSL01'  else `t`.`BANK_TYPE`  END  AS `BANK_TYPE_CODE`,
    CASE WHEN `t`.`TYPE_NAME`  IS NULL THEN '村镇银行' else `t`.`TYPE_NAME` END  AS `BANK_TYPE_NAME`
FROM (((`bt_input_bank_info` `b`))
         LEFT JOIN `bt_bank_type` `t`
                   ON ((`b`.`BANK_PREFIX` = `t`.`BANK_PREFIX`) AND (`b`.`BANK_NAME` LIKE CONCAT('%',IFNULL(`t`.`BANK_NAME_PREFIX`,''),'%')) ))
WHERE ((`t`.`BANK_NAME_PREFIX` IS NULL)
    OR (`b`.`BANK_NAME` LIKE CONCAT('%',`t`.`BANK_NAME_PREFIX`,'%')));

##修改人:zrs
##修改内容:修改字段类型
##修改时间:2025-2-10
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_IND()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME = 'BIS_PAY_POLICY' AND COLUMN_NAME = 'IS_WORK_DAY') THEN
        ALTER TABLE BIS_PAY_POLICY ADD COLUMN IS_WORK_DAY varchar (1) DEFAULT '0';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_IND();
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
##GOGO

UPDATE sys_multi_lang SET  LANG_CONTEXT='单位：{0}存在相同币种相同收款人的策略,不允许存在同时启用多个自动支付策略!'
WHERE ID='849f6b7490594cef9610c810aa837ac6';
##GOGO

##修改人：zhours
##修改时间：2025.02.11
##增加：为nis_demand表增加索引

DROP PROCEDURE IF EXISTS PRO_ADD_IND;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_IND()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME = 'NIS_DEMAND' AND INDEX_NAME = 'IDX_DEMAND_BILL_ID_STATUS') THEN
ALTER TABLE NIS_DEMAND ADD INDEX IDX_DEMAND_BILL_ID_STATUS (PAY_BILL_ID,STATUS);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_IND();
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
##GOGO


##增加 资金中心调拨账户配置

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select 'c176de51e81b11ef8277005056a84efa', '', '资金中心调拨账户配置', '资金中心调拨账户配置', 'd61f91e27ed9473ab9e8183343ea4507', '', 29, 1, '/allocation/bisgatherpolicy/accountList', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='资金中心调拨账户配置' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '0b72d868e83f11ef8277005056a84efa', '应用资源', '应用资源', 'allocation:bisgatherpolicy:account:query', 1, 'c176de51e81b11ef8277005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='allocation:bisgatherpolicy:account:query' limit 1);
commit;
##GOGO

##资金中心调拨账户配置表
CREATE TABLE IF NOT EXISTS BIS_GATHER_POLICY_ACCOUNT
(
    ID              varchar(32)      not null comment 'id' primary key,
    CORP_NAME       varchar(300)     not null comment '单位名称',
    BANK_ACC        varchar(200)     not null comment '银行账号',
    BANK_NAME       varchar(200)     not null comment '开户行',
    PRIORITY        int              not null comment '优先级',
    IS_DOWN         varchar(1)       not null comment '是否允许下拨：1允许；0不允许',
    STATUS          varchar(1)       not null comment '状态：1正常；0禁用',
    CREATE_DATE     datetime         null,
    CREATE_BY       varchar(32)      null,
    UPDATE_DATE     datetime         null,
    UPDATE_BY       varchar(32)      null
    );
##GOGO


## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '银行账号{0}已存在！', 'nis.allocation.policy.account.exist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.allocation.policy.account.exist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '优先级{0}已存在！', 'nis.allocation.policy.priority.exist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.allocation.policy.priority.exist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select 'c1f0df82e84111ef8277005056a84efa', 'status_yn', '状态' from dual
where not exists (select 1 from sys_dict_group where ID ='c1f0df82e84111ef8277005056a84efa' limit 1);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'd21ebb6ee84111ef8277005056a84efa', '1', '正常',null,'c1f0df82e84111ef8277005056a84efa',1  from dual
where not exists (select 1 from sys_dict where ID ='d21ebb6ee84111ef8277005056a84efa' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'd66fd1fbe84111ef8277005056a84efa', '0', '禁用',null,'c1f0df82e84111ef8277005056a84efa',2  from dual
where not exists (select 1 from sys_dict where ID ='d66fd1fbe84111ef8277005056a84efa' limit 1);
##GOGO

##修改人：zhours
##修改时间：2025.02.12
##增加：内部资金归集管理协议签订

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '4cd5fad5e8dc11ef8a4d005056a84efa', '', '内部资金归集管理协议签订', '内部资金归集管理协议签订', 'd61f91e27ed9473ab9e8183343ea4507', '', 30, 1, '/allocation/bisgatherpolicy/agreementList', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='内部资金归集管理协议签订' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '55d0686be8dc11ef8a4d005056a84efa', '应用资源', '应用资源', 'allocation:bisgatherpolicy:agreement:query', 1, '4cd5fad5e8dc11ef8a4d005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='allocation:bisgatherpolicy:agreement:query' limit 1);
commit;
##GOGO

##内部资金归集管理协议签订表
CREATE TABLE IF NOT EXISTS BIS_GATHER_POLICY_AGREEMENT
(
    ID              varchar(32)      not null comment 'id' primary key,
    CORP_ID         varchar(32)      not null comment '单位ID',
    CORP_NAME       varchar(300)     not null comment '单位名称',
    IS_AGREEMENT    varchar(1)       not null comment '是否签订协议：1是；0否',
    CREATE_DATE     datetime         null,
    CREATE_BY       varchar(32)      null,
    UPDATE_DATE     datetime         null,
    UPDATE_BY       varchar(32)      null
    );
##GOGO

## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '单位：{0}已存在，不允许重复添加！', 'nis.allocation.policy.agreement.exist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.allocation.policy.agreement.exist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zhours
##修改时间：2025.02.13
##增加：增加消息提醒模板
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT '20b70e6be91911ef94f3005056a84efa', '183fc820962c411e9581ce3a7db5f552', '付款类需求清单下拨失败提醒', 'netbank:allocation:dialing:failure', '', '', null, '', null, 10,
       '您有一笔需求清单下拨失败，请关注。\\n资金需求单号：$!{billNo}\\n付款人：$!{payer}\\n付款账号：$!{payerAcc}\\n付款账号开户行：$!{payerBank}\\n收款人：$!{payee}\\n收款账号：$!{payeeAcc}\\n收款账号开户行：$!{payeeBank}\\n下拨金额：$!{amt}', 1,0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '20b70e6be91911ef94f3005056a84efa' LIMIT 1
    );


INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT '5cf07b09e91911ef94f3005056a84efa', '183fc820962c411e9581ce3a7db5f552', '承兑到期类需求单下拨失败提醒', 'netbank:allocation:bill:failure', '', '', null, '', null, 11,
       '您有一笔承兑到期类需求单下拨失败，资金中心账号余额不足，请关注。\\n资金需求单号：$!{billNo}\\n付款人：$!{payer}\\n付款账号：$!{payerAcc}\\n付款账号开户行：$!{payerBank}\\n收款人：$!{payee}\\n收款账号：$!{payeeAcc}\\n收款账号开户行：$!{payeeBank}\\n下拨金额：$!{amt}', 1,0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '5cf07b09e91911ef94f3005056a84efa' LIMIT 1
    );

INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT '814f2aede91911ef94f3005056a84efa', '183fc820962c411e9581ce3a7db5f552', '融资到期类需求单下拨失败提醒', 'netbank:allocation:cms:failure', '', '', null, '', null, 12,
       '您有一笔融资到期类需求单下拨失败，资金中心账号余额不足，请关注。\\n资金需求单号：$!{billNo}\\n付款人：$!{payer}\\n付款账号：$!{payerAcc}\\n付款账号开户行：$!{payerBank}\\n收款人：$!{payee}\\n收款账号：$!{payeeAcc}\\n收款账号开户行：$!{payeeBank}\\n下拨金额：$!{amt}', 1,0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '814f2aede91911ef94f3005056a84efa' LIMIT 1
    );



##修改日期：2025-01-09
##修改人员：zhangx
##修改内容：保证金金额为空时，传0，剔除掉拆分的票
CREATE OR REPLACE VIEW DSL_AB_BILL_SHARE_SYN_VIEW AS
SELECT a.id AS id,
       CASE WHEN sc.code = '100' THEN '100000'
            ELSE sc.code END AS companyCode,
       sc.name AS companyName,
       vv.bank_acc AS ticketUnitAccount,
       vv.type_name AS ticketUnitBankCategory,
       vv.valid_Sign AS isEnable,
       v.orig_no AS docNo,
       '01' AS issuanceFlag,
       CASE WHEN v.blgrdbagcrcl_prmt_ind = '1' THEN '0'
            ELSE '1' END AS allowSubpackage,
       v.SBBLL_RNG_STRT_SN AS subTicketStartNo,
       v.SBBLL_RNG_END_SN AS subTicketEndNo,
       vv.batch_no AS issuanceBatchNo,
       CASE WHEN a.brief_id IN ('001','003') THEN '1'
            WHEN a.brief_id IN ('6001','9003','9010') THEN '11'
            WHEN a.brief_id IN ('010') THEN '17'
            WHEN a.brief_id IN ('6120') THEN '10'
            WHEN a.brief_id IN ('004') THEN '36'
           END AS businessType,
       sd.type_code AS ticketSource,
       CASE WHEN v.BILL_FLAG = 10 THEN '2'
            WHEN v.BILL_FLAG = 11 THEN '1'
           END AS circulationMedium,
       CASE WHEN v.BILL_TYPE = 10 THEN '1'
            WHEN v.BILL_TYPE = 11 THEN '2'
           END AS ticketType,
       v.bill_money AS faceValue,
       -- a.create_name AS applicant,
       a.CREATE_BY AS applicant, ## 改成取值为登录账号
    a.apply_date AS applyDate,
        a.business_date AS businessTime,
       v.start_date AS issuanceTime,
       v.end_date AS maturityTime,
       d.interest_rates AS discountRate,
       d.interest AS discountInterest,
       v.payer AS drawer,
       v.payer_bank_name AS issuingBank,
       v.PAYERACC AS drawerAccount,
       v.PAYERBANKNO AS drawerBankCode,
       v.payee AS payee,
       v.PAYEEACC AS payeeAccount,
       v.PAYEE_BANK_NAME AS payeeBank,
       v.PAYEEBANKNO AS payeeBankCode,
       v.acceptor AS acceptor,
       v.ACCEPTORACC AS acceptorAccount,
       v.ACCEPTOR_BANK_NAME AS acceptorBank,
       v.ACCEPTORBANKNO AS acceptorBankCode,
       vv.BAIL_ACC AS marginAccount,
       acc.ACC_NAME AS marginAccountName,
       acc.BANK_NAME AS marginBankName,
       sc2.name AS marginUnitName,
       ifnull(vv.BAIL_MONEY,0) AS marginAmount,
       vv.BAIL_PERCENT AS marginRatio,
       vv.BAIL_RATE AS marginRate,
       '01' AS marginType,
       CASE
           WHEN a.brief_id = '6001' THEN a.cn_accrecords
           WHEN bs.bus_type IN ('6001', '9002', '9011', '9017', '9018', '9020', '9023') THEN a.rec_nm
           END AS previousParty,
       CASE
           WHEN bs.bus_type IN ('001', '017', '002', '003', '010', '018', '019', '020', '011', '012', '022', '6201', '6203', '6004', '023') THEN a.rec_nm
           END AS nextParty,
       CASE WHEN a.send_sign = '0' THEN '2'
            WHEN a.send_sign = '1' THEN '1'
           END AS onlineOfflineFlag,
       '3' AS businessStatus,
       CASE WHEN a.opt_status = '19' THEN '9'
           END AS instructionStatus,
       CASE WHEN vv.TYPE = '0' THEN 'YF'
            WHEN vv.TYPE = '1' THEN 'YS'
           END AS receivablePayableFlag,
       v.bill_status AS ticketStatus,
       v.ebill_Bl_Crcl_Indc AS circulationStatus,
       v.bill_money - IFNULL(vv.BAIL_MONEY,0) AS maturityPaymentAmount
FROM ab_accrecords a
         LEFT JOIN ab_voucherinfo v  ON a.vid = v.id
         LEFT JOIN ab_voucherinfo_vice vv ON a.vvid = vv.id
         LEFT JOIN sys_corp sc ON sc.id = vv.CORP_ID
         LEFT JOIN AB_BILL_SOURCE_DTL sd ON sd.BRIEF_ID = a.brief_id
    AND (vv.source = sd.source OR
         (vv.source IS NULL AND a.bill_source = sd.source) OR
         (vv.source IS NULL AND a.bill_source IS NULL AND sd.source IS NULL))
         LEFT JOIN ab_discount d ON d.record_id = a.id
         LEFT JOIN bt_bank_acc acc ON acc.bank_acc = vv.BAIL_ACC
         LEFT JOIN sys_corp sc2 ON sc2.id = acc.corp_id
         LEFT JOIN ab_brief_status bs ON bs.status = a.brief_id
         LEFT JOIN DSL_INTEFACE_FLAG ef ON ef.source_id = a.id AND ef.table_source = 'DSL_AB_BILL_SHARE_SYN_VIEW'
WHERE 1=1
  AND a.status IN ('95','96','100')
  AND a.opt_status = '19'
  AND (v.ORIG_NO IS NOT NULL AND v.ORIG_NO <> '')
  AND ((a.brief_id='001' AND a.send_sign='0') ##开票登记 线下
   OR (a.brief_id='003' AND a.send_sign='1' AND a.status >= '95' AND a.opt_status ='19')
   OR (a.brief_id IN('6001','9003','9010') AND vv.OLD_DRAFT_ID IS NULL)
   OR a.brief_id IN ('010','6120','004'))
  AND (IFNULL(ef.SYNC_STATUS,'F') <> 'S')
  and a.business_date>='2025-02-01 00:00:00' ##增加业务日期判断
;



##修改人：fangcf
##修改内容：新增银行账户计划冻结申请表
##修改时间：2025-02-12
CREATE TABLE IF NOT EXISTS BT_BANK_ACC_PLAN_FREEZE
(
   ID                   	VARCHAR(32) NOT NULL COMMENT '主键',
   PLAN_FREEZE_NO			VARCHAR(32) COMMENT '计划冻结单号',
   BANK_ACC_ID       		VARCHAR(32) COMMENT '冻结账号ID',
   BANK_ACC       			VARCHAR(30) COMMENT '冻结账号',
   CORP_ID					VARCHAR(32) COMMENT '单位id',
   APPLY_FREEZE_MONEY		DECIMAL(19,2) COMMENT '申请冻结金额',
   REAL_FREEZE_MONEY		DECIMAL(19,2) COMMENT '实际已冻结金额',
   FREEZE_START_DATE        DATETIME COMMENT '冻结开始时间',
   FREEZE_END_DATE         	DATETIME COMMENT '冻结结束时间',
   FREEZE_RESSION			TEXT COMMENT '冻结原因',
   CUR_ID					VARCHAR(32) COMMENT '币别id',
   STATUS           		DECIMAL(10,0) COMMENT '状态',
   FREEZE_STATUS           	DECIMAL(10,0) COMMENT '冻结状态：0-未冻结，1-部分冻结，2-全部冻结',
   IS_FREEZE_COMPLETE		CHAR(1) COMMENT '是否冻结完成提醒：0-否，1-是',
   IS_UNFREEZE				CHAR(1) COMMENT '是否解冻：0-否，1-是',
   CREATE_BY            	VARCHAR(32) COMMENT '创建人',
   CREATE_DATE         		DATETIME COMMENT '创建时间',
   UPDATE_BY            	VARCHAR(32) COMMENT '修改人',
   UPDATE_DATE         		DATETIME COMMENT '修改时间'
);
ALTER TABLE BT_BANK_ACC_PLAN_FREEZE COMMENT '计划冻结申请表';
##GOGO

##修改人：fangcf
##修改内容：余额表新增“系统冻结金额”字段
##修改时间：2025-02-12
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BIS_PAY_POLICY' AND COLUMN_NAME = 'IS_WORK_DAY') THEN
        ALTER TABLE BIS_PAY_POLICY ADD COLUMN IS_WORK_DAY varchar (1) DEFAULT '0';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：fangcf
##修改内容：新增银行账户计划冻结申请菜单
##修改时间：2025-02-12
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '5046a942b5634db0b6eee4f58a532a6b', '6', '银行账户计划冻结申请', '银行账户计划冻结申请', '878b298d23564fd68312a7dfd3a08d9b', null, 6, 1, '/bankacc/btbankaccplanfreeze/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '5046a942b5634db0b6eee4f58a532a6b' LIMIT 1
    );
commit;
##GOGO

##修改人：fangcf
##修改内容：新增银行账户计划冻结申请菜单查询资源
##修改时间：2025-02-12
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '0b26db75ab0241c9bac8cc47a2b7bf3e', '查询资源', '查询资源', 'netbank:bankAcc:btBankAccPlanFreeze:query', 1, '5046a942b5634db0b6eee4f58a532a6b' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='0b26db75ab0241c9bac8cc47a2b7bf3e' LIMIT 1
);
##GOGO

##修改人：fangcf
##修改内容：新增银行账户计划冻结申请菜单应用资源
##修改时间：2025-02-12
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '77de8ed7cef1448289bc13633fea1571', '应用资源', '应用资源', 'netbank:bankAcc:btBankAccPlanFreeze:apply', 1, '5046a942b5634db0b6eee4f58a532a6b' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='77de8ed7cef1448289bc13633fea1571' LIMIT 1
);
##GOGO

##修改人员：fangcf
##修改内容：新增定时任务【银行账户同步冻结金额】
##修改日期 2025-02-12
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '银行账户同步冻结金额定时任务', '2025-02-12 14:06:46', '2025-02-12 14:06:46', 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalFreezeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-02-12 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='银行账户同步冻结金额定时任务' limit 1);
commit;
##GOGO

##修改人员：fangcf
##修改内容：新增定时任务【银行账户资金自动解冻】
##修改日期 2025-02-12
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '银行账户资金自动解冻定时任务', '2025-02-12 14:06:46', '2025-02-12 14:06:46', 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalUnfreezeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-02-12 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='银行账户资金自动解冻定时任务' limit 1);
commit;
##GOGO

##修改人员：fangcf
##修改内容：新增定时任务【冻结申请全部冻结成功提醒】
##修改日期 2025-02-17
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '冻结申请全部冻结成功提醒', '2025-02-12 14:06:46', '2025-02-12 14:06:46', 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalAllFreezeNoticeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-02-12 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='冻结申请全部冻结成功提醒' limit 1);
commit;
##GOGO

##修改人员：fangcf
##修改内容：新增定时任务【冻结申请部分冻结成功提醒】
##修改日期 2025-02-17
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '冻结申请部分冻结成功提醒', '2025-02-12 14:06:46', '2025-02-12 14:06:46', 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalPartFreezeNoticeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-02-12 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='冻结申请部分冻结成功提醒' limit 1);
commit;
##GOGO

##修改人员:fangcf
##修改内容:增加数据字典【冻结状态】
##修改日期:2025-02-12
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '92ba3ad3c08548b79fbdfe6a9ca54d62', 'btBankAcc.planFreezeStatus', '冻结状态', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '92ba3ad3c08548b79fbdfe6a9ca54d62' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '6535220b3c2842a6958ca2c4e350317a', '0', '未冻结', '92ba3ad3c08548b79fbdfe6a9ca54d62', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '6535220b3c2842a6958ca2c4e350317a' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'a09f99c51cda4f558732fd78a325bd56', '1', '部分冻结', '92ba3ad3c08548b79fbdfe6a9ca54d62', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'a09f99c51cda4f558732fd78a325bd56' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'a680b60ff7204b938c2c95c82a52f7ac', '2', '全部冻结', '92ba3ad3c08548b79fbdfe6a9ca54d62', '3' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'a680b60ff7204b938c2c95c82a52f7ac' LIMIT 1
    );
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-02-12
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '762ea4faa6e746c4a2e895a05bfa3360', 'zh_CN', '已存在该时间范围内的冻结记录，请检查后重新操作！', 'nis.bankacc.message.freeze.record.exist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'nis.bankacc.message.freeze.record.exist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-02-12
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c9bf6a4fa7644c4cb947ef887db72303', 'zh_CN', '计划冻结单号：【{0}】不符合解冻条件', 'nis.bankacc.message.not.unfreeze' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'nis.bankacc.message.not.unfreeze' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加消息提醒【冻结申请全部冻结成功提醒】
##修改日期:2025-02-18
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT 'ceb40fa6812b40fb90dcf71e853e89fe', 'e583ddaa25ed45098c672e99bf0c961a', '冻结申请全部冻结成功提醒', 'netbank:bankacc:allfreeze:notice', '5046a942b5634db0b6eee4f58a532a6b', null, null, null, null, 19,
		'该笔账户冻结申请已全部冻结成功，请关注。\n计划冻结单号：$!{planFreezeNo}\n单位：$!{corpName}\n冻结账号：$!{bankAcc}\n冻结账户开户行：$!{bankName}\n申请冻结金额：$!{applyFreezeMoney}\n已冻结金额：$!{realFreezeMoney}\n未冻结金额：$!{subFreezeMoney}', 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = 'ceb40fa6812b40fb90dcf71e853e89fe' LIMIT 1
);
##GOGO

##修改人员:fangcf
##修改内容:增加消息提醒【冻结申请部分冻结成功提醒】
##修改日期:2025-02-18
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT '8b62a9ccb7554f3d95a5362ff0550ca4', 'e583ddaa25ed45098c672e99bf0c961a', '冻结申请部分冻结成功提醒', 'netbank:bankacc:partfreeze:notice', '5046a942b5634db0b6eee4f58a532a6b', null, null, null, null, 20,
		'该笔账户冻结申请未全部冻结成功，请及时补款。\n计划冻结单号：$!{planFreezeNo}\n单位：$!{corpName}\n冻结账号：$!{bankAcc}\n冻结账户开户行：$!{bankName}\n申请冻结金额：$!{applyFreezeMoney}\n已冻结金额：$!{realFreezeMoney}\n未冻结金额：$!{subFreezeMoney}', 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '8b62a9ccb7554f3d95a5362ff0550ca4' LIMIT 1
);
##GOGO

##修改人：zhous
##修改内容：【票据流水备注规则】优化调整
##修改时间：2025-02-24
CREATE TABLE IF NOT EXISTS AB_BILL_RULEBILLDTLRMK
(
    ID                   	VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键',
    BANK_TYPE_ID VARCHAR(32) COMMENT '银行类型ID' ,
    BANK_TYPE_NAME VARCHAR(200) COMMENT '银行类型名称' ,
    BUSTYPE VARCHAR(10) COMMENT '业务类型：001托收,002贴现,003兑付' ,
    STATUS           		DECIMAL(10,0) COMMENT '状态 0无效,1有效',
    CREATE_BY            	VARCHAR(32) COMMENT '创建人',
    CREATE_DATE         		DATETIME COMMENT '创建时间',
    UPDATE_BY            	VARCHAR(32) COMMENT '修改人',
    UPDATE_DATE         		DATETIME COMMENT '修改时间'
    );
ALTER TABLE AB_BILL_RULEBILLDTLRMK COMMENT '票据流水备注规则表';
##GOGO

CREATE TABLE IF NOT EXISTS AB_BILL_RULEBILLDTLRMK_DTL
(
    ID                   	VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键',
    BUS_ID VARCHAR(32) NOT NULL COMMENT '主表规则业务ID' ,
    MATCH_RULE VARCHAR(10) NOT NULL  COMMENT '匹配规则  01-或者  02-并且',
    MATCH_ELEMENTS VARCHAR(30) NOT NULL  COMMENT '匹配要素 remark-摘要、bankAcc-本方账户、accName-本方账户名、oppAccNo-对方账户、oppAccName-对方账户名、curId-币别、corpId-单位名称、uses-用途、postscript-附言、amt-金额',
    MATCH_RELATIONS VARCHAR(10)  COMMENT '匹配关系 1-包含、2-不包含、3-等于、4-不等于',
    MATCH_CONTENT VARCHAR(100) COMMENT '匹配内容'  ,
    SORT int  COMMENT '排序' ,
    LEFT_PARENTHESIS VARCHAR(1) COMMENT '左括号' ,
    RIGHT_PARENTHESIS VARCHAR(1) COMMENT '右括号'
    );
ALTER TABLE AB_BILL_RULEBILLDTLRMK_DTL COMMENT '票据流水备注规则明细表';
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '43f5bce28d434c759fbb711b5b5d47dd', '', '票据流水备注规则', '票据流水备注规则', '7a5ee7ee2def4499896143209e800052', null, 2, 1, '/bill/accebillClaim/config/ruleBillDtlRmk', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '43f5bce28d434c759fbb711b5b5d47dd' LIMIT 1
    );
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'e228bfcd3cb340aaaaf8e948ac10b21a', '查询资源', '查询资源', 'ab:bill:resource:rulebilltlrmk:query', 1, '43f5bce28d434c759fbb711b5b5d47dd' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM SYS_RESOURCES WHERE ID='e228bfcd3cb340aaaaf8e948ac10b21a' LIMIT 1
    );
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '144cd3527afc4a91856272ec6b55d928', 'zh_CN', '批量禁用', 'common.button.batchDisable' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.button.batchDisable' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a59fcca628a0473ba8d3f80639f47bc7', 'zh_CN', '确认要禁用吗', 'common.message.confirm.disable' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.message.confirm.disable' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9418f766e3e547f49e8512e962b77e98', 'zh_CN', '匹配规则不能为空！', 'accebill.message.error.claim.rule.isnull' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.error.claim.rule.isnull' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '65f576064ded405e92c5fe6f7c36895d', 'billClaimRule.matchElements', '票据认领业务类别匹配要素', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '65f576064ded405e92c5fe6f7c36895d' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '25783a159f724e8db2714aa54b4fb58e', 'remark', '摘要', '65f576064ded405e92c5fe6f7c36895d', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '25783a159f724e8db2714aa54b4fb58e' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'de73e8e70f1147fdb653bcaf466f64a8', 'bank_acc', '本方账户', '65f576064ded405e92c5fe6f7c36895d', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'de73e8e70f1147fdb653bcaf466f64a8' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '3890521229364317b82ac7f95060fbc2', 'acc_name', '本方账户名', '65f576064ded405e92c5fe6f7c36895d', '3' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '3890521229364317b82ac7f95060fbc2' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '926452de010b42c0929717960bbeb6d7', 'opp_acc_no', '对方账户', '65f576064ded405e92c5fe6f7c36895d', '4' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '926452de010b42c0929717960bbeb6d7' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'b24367b961ae4d1d837f1a5ee471b63a', 'opp_acc_name', '对方账户名', '65f576064ded405e92c5fe6f7c36895d', '5' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'b24367b961ae4d1d837f1a5ee471b63a' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '66118c81c9c0406ca193b1183fa72ca5', 'uses', '用途', '65f576064ded405e92c5fe6f7c36895d', '6' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '66118c81c9c0406ca193b1183fa72ca5' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT 'ff71c147c85e443386a2fd141f16fa44', 'postscript', '附言', '65f576064ded405e92c5fe6f7c36895d', '7' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'ff71c147c85e443386a2fd141f16fa44' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '896dc77992ce4f00a98d580809198331', 'corp_name', '单位名称', '65f576064ded405e92c5fe6f7c36895d', '8' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '896dc77992ce4f00a98d580809198331' LIMIT 1
    );
##GOGO

##修改日期：2025-02-25
##修改人员：lijq
##修改内容：大参林共享全量流水同步接口视图-当币种为RMB时，将它改成CNY
CREATE OR REPLACE VIEW DSL_GX_BIS_ACC_DTL_VIEW AS
SELECT dtl.id                                AS seqId,
       dtl.BANK_SERIAL_NO                    AS yhlsh,
       CASE WHEN  bc.code  = '100' THEN  '100000'
            ELSE bc.code END                 AS dwbh,
       bc.name                               AS dwmc,
       CASE
           WHEN dtl.cd_sign =  '1' THEN '02'
           ELSE '01' END                     AS incomeExpenseType,
       dtl.BANK_ACC                          AS zhbh,
       dtl.ACC_NAME                          AS zhmc,
       acc.BANK_CODE                         AS lhh,
       dtl.BANK_NAME                         AS khh,
       dtl.amt                               AS je,
       dtl.OPP_ACC_NO                        AS dfzh,
       dtl.OPP_ACC_NAME                      AS dfhm,
       dtl.OPP_ACC_BANK                      AS dfkhh,
       CASE WHEN c.ENGLISH_CODE='RMB' THEN 'CNY'  ELSE     c.ENGLISH_CODE END  AS currencyCode,
       DATE_FORMAT(dtl.TRANS_TIME, '%Y/%m/%d') AS jyrq,
       DATE_FORMAT(dtl.TRANS_TIME, '%Y/%m/%d %H:%i:%s') AS jysj,
       dtl.REMARK                            AS yt,
       dtl.POSTSCRIPT                        AS summary,
       dtl.USES                              AS purpose,
       nt.BUS_TYPE                           AS barCodeType,
       nt.BUS_TYPE_NAME                      AS billTypeDesc,
       nb.BURSAR_BILL_NO                     AS sourceNum,
       CASE WHEN ns.isReturn IS NULL THEN 'N' ELSE ns.isReturn END AS isReturn
FROM bis_acc_dtl dtl
         LEFT JOIN (SELECT dtl_id,bill_id,'Y' AS isReturn FROM nis_bill_bank_serial_no_refund UNION SELECT dtl_id,bill_id, 'N' AS isReturn FROM NIS_BILL_BANK_SERIAL_NO) ns ON dtl.id=ns.dtl_id
         LEFT JOIN nis_billhead nb ON nb.id=ns.bill_id
         LEFT JOIN nis_bus_type nt ON nb.bus_type = nt.id
         LEFT JOIN bt_bank_acc acc ON dtl.bank_acc = acc.bank_acc
         LEFT JOIN sys_corp bc ON bc.id = acc.corp_id
         LEFT JOIN BIS_BIF_INIT bi ON bi.id = dtl.bif_code
         LEFT JOIN bt_currency c ON c.id = dtl.cur_id
WHERE 1=1
  AND (`dtl`.`TRANS_TIME` >='2025-02-01 00:00:00.000000' )
  AND dtl.`CREATE_DATE`>'2025-02-07 15:00:00'
  AND (IFNULL(dtl.`DTL_SYNC_STATUS`,'F') )<> 'S'
  AND dtl.BANK_ACC<>'15999329560036'
  AND (IFNULL(dtl.GX_DTL_SYNC_STATUS,'F') <> 'S' OR (IFNULL(nb.BURSAR_BILL_NO,'') <> IFNULL(dtl.GX_SOURCE_NUM,'')));

##记账作废复核性能慢优化 添加 明细表索引 BOOK_ID 20250228 zhengg
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_IND()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND INDEX_NAME = 'IDX_DTL_BOOK_ID') THEN
ALTER TABLE BIS_ACC_DTL ADD INDEX IDX_DTL_BOOK_ID  (BOOK_ID);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_IND();
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_IND;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_IND()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME = 'BT_NET_CHALK' AND INDEX_NAME = 'IDX_NET_CHALK_BOOK_ID') THEN
ALTER TABLE BT_NET_CHALK ADD INDEX IDX_NET_CHALK_BOOK_ID  (BOOK_ID);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_IND();
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
##GOGO

##修改人：zhous
##修改内容：新增票据保证金利息维护功能
##修改时间：2025-03-07
CREATE TABLE IF NOT EXISTS AB_EMIT_BAIL_INTEREST_INFO
(
    ID                   	VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键',
    CORP_ID VARCHAR(32) COMMENT '票据单位id' ,
    CORP_NAME VARCHAR(200) COMMENT '票据单位名称',
    BANK_ACC VARCHAR(100) COMMENT '票据账号',
    BANK_TYPE_ID VARCHAR(32)  COMMENT '银行类型id',
    BANK_TYPE_NAME VARCHAR(200)  COMMENT '银行类型名称',
    CURRENT_MONTH VARCHAR(32) COMMENT '当前月份'  ,
    BILL_MONEY	DECIMAL(20,2) COMMENT '票面金额',
    BAIL_MONEY  DECIMAL(20,2) COMMENT '保证金金额' ,
    DUE_SETTLEMENT_AMOUNT DECIMAL(20,2) COMMENT '到期解付金额',
    ACTUAL_SETTLEMENT_AMOUNT DECIMAL(20,2) COMMENT '实际结算金额',
    INTEREST_MARGIN DECIMAL(20,2) COMMENT '利息差额',
    STATUS  DECIMAL(10,0) COMMENT '单据状态 0-暂存,1-已确认',
    PUSH_STATUS  DECIMAL(10,0) COMMENT '推送状态 0-未推送,1-待推送,2-已推送',
    RECORD_SOURCE char(1) COMMENT '记录来源 0-自动生成 1-手动增加',
    CREATE_BY            	VARCHAR(32) COMMENT '创建人',
    CREATE_DATE         		DATETIME COMMENT '创建时间',
    UPDATE_BY            	VARCHAR(32) COMMENT '修改人',
    UPDATE_DATE         		DATETIME COMMENT '修改时间'
    );
ALTER TABLE AB_EMIT_BAIL_INTEREST_INFO COMMENT '票据保证金利息维护表';
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'cf2748ec82604dd4a60e703a28f41d20', '', '票据保证金利息维护', '票据保证金利息维护', '38d6293b5fbf4597aa52222b429724ae', null, 500, 1, '/bill/accebill/bailInterestInfo/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'cf2748ec82604dd4a60e703a28f41d20' LIMIT 1
    );
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '5964be0892d04cbabd24ddc1409fba89', '查询资源', '查询资源', 'bill:accebill:abemitbill:bailInterestInfo:query', 1, 'cf2748ec82604dd4a60e703a28f41d20' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM SYS_RESOURCES WHERE ID='5964be0892d04cbabd24ddc1409fba89' LIMIT 1
    );
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'f5448d6a59024af3af8bddfda1a18dc2', '操作资源', '操作资源', 'bill:accebill:abemitbill:bailInterestInfo:opreate', 1, 'cf2748ec82604dd4a60e703a28f41d20' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM SYS_RESOURCES WHERE ID='f5448d6a59024af3af8bddfda1a18dc2' LIMIT 1
    );
commit;
##GOGO

insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '930f1e0f1e1842b7b063efb38fecbca9', 'AB_EMIT_BILL_BAIL_INTEREST_STATUS', '应付票据保证金利息维护状态', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '930f1e0f1e1842b7b063efb38fecbca9' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '806eb20cd5eb40fa8a2fb5ca01e361c3', '0', '暂存', '930f1e0f1e1842b7b063efb38fecbca9', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '806eb20cd5eb40fa8a2fb5ca01e361c3' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '4b97c567fd604e4fb6db0bd82b18180b', '1', '已确认', '930f1e0f1e1842b7b063efb38fecbca9', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '4b97c567fd604e4fb6db0bd82b18180b' LIMIT 1
    );
##GOGO

insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME)
SELECT '91a1a7f2f5db4697a8f7ac6d50ad71e0', 'AB_EMIT_BILL_BAIL_INTEREST_PUSH_STATUS', '应付票据保证金利息维护推送状态', null, null FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '91a1a7f2f5db4697a8f7ac6d50ad71e0' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '8a5e832d920846d695b4fd52ad7ef40b', '0', '未推送', '91a1a7f2f5db4697a8f7ac6d50ad71e0', '1' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '8a5e832d920846d695b4fd52ad7ef40b' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '480349dda18748569c80c611b65bd8cf', '1', '待推送', '91a1a7f2f5db4697a8f7ac6d50ad71e0', '2' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '480349dda18748569c80c611b65bd8cf' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '05b00632a43d461481bcaf1939704b0e', '2', '已推送', '91a1a7f2f5db4697a8f7ac6d50ad71e0', '3' FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '05b00632a43d461481bcaf1939704b0e' LIMIT 1
    );
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '95b31acd987e4aecbc1f6401442449a1', 'zh_CN', '推送状态', 'table.bill.sendStatus' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.bill.sendStatus' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'bafcff5070df4d94be52017c4617ba74', 'zh_CN', '利息ID', 'table.emitBill.bailInterest.id' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.id' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c43e40257e0844d382ea921d1e93b286', 'zh_CN', '票据单位名称', 'table.emitBill.bailInterest.corpName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.corpName' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'af9e4302a08444aab3447179ac921d42', 'zh_CN', '到期解付金额', 'table.emitBill.bailInterest.dueSettlementAmount' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.dueSettlementAmount' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '982c9564b1e847f8832576973a9ff141', 'zh_CN', '实际结算金额', 'table.emitBill.bailInterest.actualSettlementAmount' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.actualSettlementAmount' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '29adeed113764a1fbde8de7471014f99', 'zh_CN', '利息差额', 'table.emitBill.bailInterest.interest.margin' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.interest.margin' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'cd85b004546b406e8eef851e46f11db1', 'zh_CN', '最后操作人', 'table.emitBill.bailInterest.lastOperator' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.lastOperator' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '08a5176562a148d3b7809a27b3e2c8c1', 'zh_CN', '计算利息', 'table.emitBill.bailInterest.calculatedInterest' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.calculatedInterest' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '4827198d2f8d4418bc030c9d674206f4', 'zh_CN', '批量确认', 'table.emitBill.bailInterest.calculatedInterest.batchSure' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.calculatedInterest.batchSure' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ede579dc99f74d51bdb3caa8da03b2e8', 'zh_CN', '批量反确认', 'table.emitBill.bailInterest.calculatedInterest.batchNotSure' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.calculatedInterest.batchNotSure' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b37be632fc2f47a0b5d8fc89a716f0ed', 'zh_CN', '推送共享', 'table.emitBill.bailInterest.calculatedInterest.pushSharing' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.bailInterest.calculatedInterest.pushSharing' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e88b908114ca4f2890e50d2e6da65667', 'zh_CN', '当前记录是已确认状态，不可以进行删除', 'accebill.message.error.isSureInfo.can.not.delete' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.error.isSureInfo.can.not.delete' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ceeb21941cb542ccab2cafd2e32b69ac', 'zh_CN', '月份未选择,不能为空!', 'accebill.bail.interest.info.current.month.is.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.bail.interest.info.current.month.is.null' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '06980e4c96f6422da29eda5dd76628fa', 'zh_CN', '是否批量确认数据?', 'common.message.confirm.bailInterestInfo.batchSure' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.message.confirm.bailInterestInfo.batchSure' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b97982a5d1644f649561ecee8c40e6f9', 'zh_CN', '是否批量反确认?', 'common.message.confirm.bailInterestInfo.batchNotSure' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.message.confirm.bailInterestInfo.batchNotSure' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1d3a3aa5a65d49a9916a095bbd690732', 'zh_CN', '数据已推送共享，不能进行反确认', 'common.message.confirm.bailInterestInfo.has.push' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.message.confirm.bailInterestInfo.has.push' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '32e02f8fff494585bad13efb6c048acb', 'zh_CN', '请选择未推送的数据', 'bailInterestInfo.pushStatus.is.not.temp' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bailInterestInfo.pushStatus.is.not.temp' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a33d61b6d0c941b5bfe090b6425d4ee2', 'zh_CN', '是否进行推送共享?', 'common.message.confirm.bailInterestInfo.pushSharing' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'common.message.confirm.bailInterestInfo.pushSharing' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '40471d1dcab44b29a056d2c9a9e7ed9b', 'zh_CN', '记录来源', 'table.bill.recordSource' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.bill.recordSource' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改授信占用业务名称字典：“信贷管理”修改成“银行借款”
UPDATE SYS_INTERNATION_BASE SET LANG_CONTEXT = '银行借款' WHERE KEY_ID = 'dfde08a6b20949468055e4d0c9b65c96' and LANG_CONTEXT = '信贷管理';
##GOGO

##修改日期：2025-03-10
##修改人员：zhous
##修改内容：保证金利息待同步数据视图
CREATE OR REPLACE VIEW DSL_AB_EMIT_BAIL_INTEREST_SYNC_VIEW AS
SELECT
    ae.id AS orderId,
    case when sc.code='100' then '100000'
         else sc.code end AS companyCode,
    sc.name AS companyName,
    ae.bank_acc as receiptAccount,
    ae.interest_margin as marginAmount,
    concat(ae.current_month,'-','01') as accountingTime
FROM ab_emit_bail_interest_info ae
         LEFT JOIN sys_corp sc on ae.corp_id = sc.id where ae.status ='1' and ae.push_status ='1' ;
##GOGO
##GOGO

INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 7, '保证金利息同步接口', now(), now(), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'DSLAbEmitBailInterestSync', '{
    "dsl_ab_emit_bail_interest_sync_url": " ",
    "dsl_ab_emit_bail_interest_sync_page_size": 30}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='保证金利息同步接口' limit 1);
##GOGO

##修改日期：2025-03-14
##修改人员：zhous
##修改内容：记账凭证导入时未校验内部账号状态，注销状态下的内部账号也导入了凭证
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b63f859db8c04d4eaeebf571306654d7', 'zh_CN', '该内部账号已注销', 'fin.book.tally.account.has.destory' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'fin.book.tally.account.has.destory' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：zhours
##修改时间：2025.03.14
##添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '审批失败：工资单{0}获取明细失败，无法生成指令！', 'nis.wage.message.detail.fail.exc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.wage.message.detail.fail.exc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：fangcf
##修改内容：新增银行账户计划冻结申请查询菜单
##修改时间：2025-03-18
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '7e744965716b4355a4ea42f1114ac852', '6', '银行账户计划冻结申请查询', '银行账户计划冻结申请查询', 'bbc970ff559a405fb7c36918cc0d73cf', null, 6, 1, '/bankacc/btbankaccplanfreeze/queryList', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '7e744965716b4355a4ea42f1114ac852' LIMIT 1
    );
commit;
##GOGO

##修改人：fangcf
##修改内容：新增银行账户计划冻结申请查询菜单查询资源
##修改时间：2025-03-18
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '7c1c01bbe95c42e39fc3edec639a25d0', '查询资源', '查询资源', 'netbank:bankAcc:btBankAccPlanFreezeQuery:query', 1, '7e744965716b4355a4ea42f1114ac852' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='7c1c01bbe95c42e39fc3edec639a25d0' LIMIT 1
);
##GOGO

##修改人：fangcf
##修改内容：新增银行账户计划冻结申请查询菜单应用资源
##修改时间：2025-03-18
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'f84a43c15d9047518cfbdd27f559bdf8', '应用资源', '应用资源', 'netbank:bankAcc:btBankAccPlanFreezeQuery:apply', 1, '7e744965716b4355a4ea42f1114ac852' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='f84a43c15d9047518cfbdd27f559bdf8' LIMIT 1
);
##GOGO

##修改人：zhous
##修改内容：T25030036463- 票据认领时，当票据较多时报错
##修改时间：2025-03-25
ALTER TABLE AB_CLAIM_MIDDLE MODIFY COLUMN rmk TEXT;
commit;
##GOGO

##修改人：zhous
##修改内容：T25030036464-票据余额不足提醒增加“节假日”判断
##修改时间：2025-03-26
UPDATE sys_notification_category SET CONTENT = '单位:$!{corpName}
公司类型：$!{companyTYpeName}
银行账号:$!{bankAcc}
账户名:$!{reqNm}
开户行:$!{bankName}
当前账户可用余额:$!{bankAccBalance}
当前开票的银行账户余额不够兑付
应付票据总笔数：$!{billNums}笔
票面汇总金额：$!{billMoney}
票据到期解付汇总金额：$!{releasePaymentMoney}
到期日期：#if($!{endDate2Str} && $!{endDate2Str} ne $!{endDateStr}) $!{endDateStr},$!{endDate2Str} #else $!{endDateStr} #end $!{variable}
需下拨金额：$!{xbMoney}'
where tag = 'bill:accebill:emitbillpaybalance';
commit;
##GOGO


##修改人员：fangcf
##修改内容：新增定时任务【直联账户系统冻结金额同步】
##修改日期 2025-03-27
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 11, '直联账户系统冻结金额同步', '2025-03-26 14:06:46', '2025-03-26 14:06:46', 'XXL', '', 'CRON', '0 0 6 * * ?', 'DO_NOTHING', 'ROUND', 'AutoSyncSysFrzBalJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-03-26 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='直联账户系统冻结金额同步' limit 1);
commit;
##GOGO

##修改人：zhours
##修改时间：2025.03.26
##增加：银承账户留底金额配置 功能

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '0465766309e511f0a597005056a84efa', '', '银承账户留底金额配置', '银承账户留底金额配置', '0c93d0aa1ea849fd86d3a0f42c3dab17', '', 29, 1, '/bill/accebill/accKeepAmt/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='银承账户留底金额配置' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '0bf0da0309e511f0a597005056a84efa', '应用资源', '应用资源', 'bill:accebill:accKeepAmt:controls', 1, '0465766309e511f0a597005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='bill:accebill:accKeepAmt:controls' limit 1);
commit;
##GOGO

## 银承账户留底金额配置
CREATE TABLE IF NOT EXISTS BILL_ACC_KEEP_AMT
(
    ID              varchar(32)      not null comment 'id' primary key,
    CORP_ID         varchar(32)      not null comment '单位ID',
    CORP_NAME       varchar(300)     not null comment '单位名称',
    BANK_ACC        varchar(200)     not null comment '账号',
    ACC_NAME        varchar(200)     not null comment '户名',
    BANK_TYPE       varchar(32)      not null comment '银行类型',
    BANK_TYPE_NAME  varchar(200)     not null comment '银行类型名称',
    BANK_CODE       varchar(30)      not null comment '开户行号',
    BANK_NAME       varchar(200)     not null comment '开户行名',
    KEEP_AMT        DECIMAL(15,2)    not null comment '留存金额',
    STATUS          varchar(1)       not null comment '状态：1正常；0禁用',
    CREATE_DATE     datetime         null,
    CREATE_BY       varchar(32)      null,
    UPDATE_DATE     datetime         null,
    UPDATE_BY       varchar(32)      null
    );
##GOGO


## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '银行账号{0}已存在！', 'accebill.acc.keep.amt.exists' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.acc.keep.amt.exists' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:zrs
##修改内容:修改付款类需求清单下拨失败提醒
##修改日期:2025-04-07

update sys_notification_category
set CONTENT = '您有一笔需求清单下拨失败，请关注。\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n下拨金额：$!{amt}'
where TAG = 'netbank:allocation:dialing:failure';
##GOGO

update sys_notification_category
set CONTENT = '您有一笔承兑到期类需求单下拨失败，资金中心账号余额不足，请关注。\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n下拨金额：$!{amt}'
where TAG = 'netbank:allocation:bill:failure';
##GOGO

update sys_notification_category
set CONTENT = '您有一笔融资到期类需求单下拨失败，资金中心账号余额不足，请关注。\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n下拨金额：$!{amt}'
where TAG = 'netbank:allocation:cms:failure';
##GOGO

###修改人:zhous
###修改日期:2025-4-14
###修改内容:添加保证金账户自动销户定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '保证金账户自动销户', '2024-08-20 16:30:15', '2024-08-20 16:30:15', 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'autoAccCancellTaskService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-08-15 13:43:48', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='保证金账户自动销户' limit 1);
commit;
##GOGO

##修改人:zrs
##修改内容:修改字段类型
##修改时间:2025-4-15
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'IS_CLOSE') THEN
        ALTER TABLE BT_BANK_ACC ADD COLUMN IS_CLOSE varchar (1) DEFAULT '0'  COMMENT '是否注销';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO



##修改日期:2025-04-11
##修改人员:zhengg
##修改内容:ab_voucherinfo 调整索引
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'AB_VOUCHERINFO' AND INDEX_NAME = 'IDX_AB_VOUCHERINFO_ORIGNOSBBLNO' and COLUMN_NAME='SBBLL_RNG_END_SN') THEN
        IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'AB_VOUCHERINFO' AND INDEX_NAME = 'IDX_AB_VOUCHERINFO_ORIGNOSBBLNO' ) THEN
drop index IDX_AB_VOUCHERINFO_ORIGNOSBBLNO on AB_VOUCHERINFO;
END IF;
ALTER TABLE AB_VOUCHERINFO ADD INDEX IDX_AB_VOUCHERINFO_ORIGNOSBBLNO (`ORIG_NO`, `SBBLL_RNG_STRT_SN`, `SBBLL_RNG_END_SN`);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改内容:AB_ACCRECORDS 创建索引
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'AB_ACCRECORDS' AND INDEX_NAME = 'AB_ACCRECORDS_LASTID_IDX' ) THEN
ALTER TABLE AB_ACCRECORDS ADD INDEX AB_ACCRECORDS_LASTID_IDX (`LASTID`);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'AB_ACCRECORDS' AND INDEX_NAME = 'IDX_AC_RECORDS_BRIEFID' ) THEN
ALTER TABLE AB_ACCRECORDS ADD INDEX IDX_AC_RECORDS_BRIEFID (`BRIEF_ID`);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'AB_ACCRECORDS' AND INDEX_NAME = 'IDX_AC_RECORDS_BRIEF_ID' ) THEN
ALTER TABLE AB_ACCRECORDS ADD INDEX IDX_AC_RECORDS_BRIEF_ID (`BRIEF_ID`,`CREATE_DATE`);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'AB_ACCRECORDS' AND INDEX_NAME = 'IDX_AC_RECORDS_BUSINESS' ) THEN
ALTER TABLE AB_ACCRECORDS ADD INDEX IDX_AC_RECORDS_BUSINESS (`BUSINESS_DATE`);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'AB_VOUCHERINFO_VICE' AND INDEX_NAME = 'CREATE_DATE' ) THEN
ALTER TABLE AB_VOUCHERINFO_VICE ADD INDEX CREATE_DATE (`CREATE_DATE`);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'AB_VOUCHERINFO_VICE' AND INDEX_NAME = 'AB_VOUCHERINFO_VICE_CORP_ID' ) THEN
ALTER TABLE AB_VOUCHERINFO_VICE ADD INDEX AB_VOUCHERINFO_VICE_CORP_ID (`CORP_ID`);
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
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'AB_VOUCHERINFO_VICE' AND INDEX_NAME = 'AB_VOUCHERINFO_VICE_BANK_ACC_INDEX' ) THEN
ALTER TABLE AB_VOUCHERINFO_VICE ADD INDEX AB_VOUCHERINFO_VICE_BANK_ACC_INDEX (`BANK_ACC`);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改日期：2025-04-23
##修改人员：zhous
##修改内容：月度贷款明细表新增最新利率
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ad26185d0f294f77b9eea7e9d20b7c65', 'zh_CN', '最新利率', 'table.cmsProvideLoanInfo.latestRate' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.cmsProvideLoanInfo.latestRate' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改日期：2025-04-24
##修改人员：zhengg
##修改票据号匹配规则表分隔符长度
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'AB_BILL_RULEBILLNO' AND COLUMN_NAME = 'BILLNO_SEPARATED') THEN
alter table ab_bill_rulebillno
    modify BILLNO_SEPARATED varchar(100) null;
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：zhous
##修改内容：新增银承开票占比目标维护功能  新增银承占比及额度使用情况统计表
##修改时间：2025-04-25
CREATE TABLE IF NOT EXISTS AB_EMIT_BILL_TARGET_PROPORTION_INFO
(
    ID                      VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键',
    CORP_ID VARCHAR(32) COMMENT '单位id' ,
    CORP_NAME VARCHAR(200) COMMENT '单位名称',
    CORP_CODE VARCHAR(200) COMMENT '单位编码',
    CURRENT_YEAR VARCHAR(32) COMMENT '当前年份'  ,
    TARGET_PROPORTION DECIMAL(8,6) COMMENT '目标',
    IS_DELETE VARCHAR(1) COMMENT '是否删除 1-是' ,
    CREATE_BY               VARCHAR(32) COMMENT '创建人',
    CREATE_DATE                DATETIME COMMENT '创建时间',
    UPDATE_BY               VARCHAR(32) COMMENT '修改人',
    UPDATE_DATE                DATETIME COMMENT '修改时间'
    );
ALTER TABLE AB_EMIT_BILL_TARGET_PROPORTION_INFO COMMENT '银承开票占比目标维护维护表';
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '5b188edabbb64f7cb4dc76865c5791c4', 'zh_CN', '公司名称', 'table.emitBill.billTargetProportion.companyName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.billTargetProportion.companyName' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a525dba2975c4003837a4ac9662eaf81', 'zh_CN', '年份', 'table.emitBill.billTargetProportion.year' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.billTargetProportion.year' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '209de5998e9b47b0b3f35fd957ad7e24', 'zh_CN', '目标值不能为负数!', 'table.emitBill.targetProportion.is.negative' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.targetProportion.is.negative' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'da761a234f894b9292d0efa2426d6c22', 'zh_CN', '已存在数据，请确认!', 'accebill.message.error.billtargetproportion.repeat' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.error.billtargetproportion.repeat' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'df55dd06ea1f4540b9e25d1fa595b4bb', 'zh_CN', '目标', 'table.emitBill.billTargetProportion.target' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.emitBill.billTargetProportion.target' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'eca6bd57bb174659a0ca7e74f384eeb4', 'zh_CN', '仓库', 'bill.accebill.query.warehouse' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.query.warehouse' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e84b2e4d67bb4b628754dc85bf37dfcc', 'zh_CN', '银承开票占比情况', 'targetProportionAndQuotaUsage.targetProportion' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.targetProportion' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f8f32a8e7ce34091ac419daf9de8f6b6', 'zh_CN', '银行授信情况', 'targetProportionAndQuotaUsage.bankCredit' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.bankCredit' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c986b24b4469426d8446b24c270c62d0', 'zh_CN', '开票情况', 'targetProportionAndQuotaUsage.openBill' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.openBill' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ee2fc8fd03cd4a0298de35779db00bda', 'zh_CN', '实际', 'targetProportionAndQuotaUsage.actual' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.actual' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3b91371816084b68a87848b87504a430', 'zh_CN', '差异', 'targetProportionAndQuotaUsage.difference' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.difference' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'eaae595cc72d41d7b8ba0380c709727b', 'zh_CN', '对比上期提升比例', 'targetProportionAndQuotaUsage.increaseRatio' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.increaseRatio' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'fecbbc841e5b4d799c9fce9e037394ec', 'zh_CN', '银行额度', 'targetProportionAndQuotaUsage.bankCreditLimit' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.bankCreditLimit' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '512f227e38d5413fa253ad5d0301bbca', 'zh_CN', '本月货款支付总额', 'targetProportionAndQuotaUsage.totalPay' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.totalPay' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3b45dcc2f77e453fb1b2bd22ae21b06a', 'zh_CN', '本月货款电汇金额', 'targetProportionAndQuotaUsage.eBankPay' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.eBankPay' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7edbd6ebddd34c248dc576c1af84ce7a', 'zh_CN', '本月总开票金额', 'targetProportionAndQuotaUsage.openBillPay' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'targetProportionAndQuotaUsage.openBillPay' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '09b63ca8274c4d9e87d788b8b06d49c1', '', '银承开票占比目标维护', '银承开票占比目标维护', '38d6293b5fbf4597aa52222b429724ae', null, 500, 1, '/bill/accebill/billTargetProportion/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = '09b63ca8274c4d9e87d788b8b06d49c1' LIMIT 1
);
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'f3779f27503e4c7c96273114e8360e80', '查询资源', '查询资源', 'bill:accebill:abemitbill:billTargetProportion:query', 1, '09b63ca8274c4d9e87d788b8b06d49c1' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='f3779f27503e4c7c96273114e8360e80' LIMIT 1
);
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '7762c5548c144026aefc851e43fcb6a5', '操作权限', '操作权限', 'bill:accebill:abemitbill:billTargetProportion:operate', 1, '09b63ca8274c4d9e87d788b8b06d49c1' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='7762c5548c144026aefc851e43fcb6a5' LIMIT 1
);
commit;
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '5b2f2173a63946aebce5606615fc40ea', '', '银承占比及额度使用情况统计表', '银承占比及额度使用情况统计表', '4d6a6759cde6429cbfadd1f4a7c640c1', null, 500, 1, '/bill/accebill/abbillQuery/listTargetProportionAndQuotaUsageStatistics', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = '5b2f2173a63946aebce5606615fc40ea' LIMIT 1
);
commit;
##GOGO

insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '0bc44eeb1dff4202acba0600b1a64ae8', '银承占比及额度使用情况统计表数据查询权限', '银承占比及额度使用情况统计表数据查询权限', 'bill:accebill:targetProportionAndQuotaUsage:query:corpop', 1, '5b2f2173a63946aebce5606615fc40ea' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='0bc44eeb1dff4202acba0600b1a64ae8' LIMIT 1
);
commit;
##GOGO

##修改人：zhous
##修改内容：新增银承开票占比目标维护功能  新增银承占比及额度使用情况统计表
##修改时间：2025-05-14
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '598785c81fba48de9fc43e6508bb5fdc', 'zh_CN', '省区', 'bill.accebill.province' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.province' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '11e306d0fda641d3a78297298d5608d2', 'zh_CN', '资金下拨/划拨日期', 'bill.accebill.fundsDisbursementDateOrAllocationDate' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.fundsDisbursementDateOrAllocationDate' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：zhous
##修改内容：00062025050301-票据自动认领优化需求
##修改时间：2025-05-19
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'AB_BILL_RULEBILLNO' AND COLUMN_NAME = 'IS_SUMMARY_CLAIM_BILL') THEN
ALTER TABLE AB_BILL_RULEBILLNO ADD COLUMN IS_SUMMARY_CLAIM_BILL varchar(1) COMMENT '是否汇总认领流水 0-否 1-是';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '4243bc0fa24c4de5b6ddbd964503a4e6', 'zh_CN', '上浮金额', 'config.accebill.upMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'config.accebill.upMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f21108425a3d415c978e3eb08b665344', 'zh_CN', '下浮金额', 'config.accebill.downMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'config.accebill.downMoney' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'fff7b3abb19a49edbd14a1d34e6d66f9', 'zh_CN', '是否汇总认领流水', 'bill.accebill.isSummaryClaimBill' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.isSummaryClaimBill' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

##修改人：zhous
##修改内容：00062025050372-应付票据和应收票据台账查询中“能否转让”未正常显示
##修改时间：2025-05-20
update sys_dict set TYPE_CODE = '0' where TYPE_GROUP_ID = (select id from sys_dict_group  where TYPE_GROUP_CODE = 'AB_BILL_BLGRDBAGCRCL_PRMTIND_IF') and TYPE_NAME = '不允许分包流转';
commit;
##GOGO

update sys_dict set TYPE_CODE = '1' where TYPE_GROUP_ID = (select id from sys_dict_group  where TYPE_GROUP_CODE = 'AB_BILL_BLGRDBAGCRCL_PRMTIND_IF') and TYPE_NAME = '允许分包流转';
commit;
##GOGO



##修改人：fangcf
##修改内容：新增共享业务类型表
##修改时间：2025-05-15
CREATE TABLE IF NOT EXISTS NIS_SHARE_BUS_TYPE
(
   ID                   	VARCHAR(32) NOT NULL COMMENT '主键',
   SHARE_BUS_TYPE			VARCHAR(10) COMMENT '共享业务类型编码',
   SHARE_BUS_TYPE_NAME      VARCHAR(200) COMMENT '共享业务类型名称',
   BUS_TYPE_ID       		VARCHAR(32) COMMENT '关联业务类别：业务类别表主键id',
   STATUS					VARCHAR(2) COMMENT '状态：1-正常，0-禁用',
   CREATE_BY            	VARCHAR(32) COMMENT '创建人',
   CREATE_DATE         		DATETIME COMMENT '创建时间',
   UPDATE_BY            	VARCHAR(32) COMMENT '修改人',
   UPDATE_DATE         		DATETIME COMMENT '修改时间'
);
ALTER TABLE NIS_SHARE_BUS_TYPE COMMENT '共享业务类型表';
##GOGO

##修改人：fangcf
##修改内容：新增共享业务类型维护菜单
##修改时间：2025-05-15
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'b78a7fb415f84760890d9ba446ed1205', '31', '共享业务类型维护', '共享业务类型维护', 'd61f91e27ed9473ab9e8183343ea4507', null, 31, 1, '/netbank/nissharebustype/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'b78a7fb415f84760890d9ba446ed1205' LIMIT 1
    );
commit;
##GOGO

##修改人：fangcf
##修改内容：新增共享业务类型维护查询资源
##修改时间：2025-05-15
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'ee1f1076ee2d41b4afb041358382842c', '查询资源', '查询资源', 'netbank:basicdata:nissharebustype:query', 1, 'b78a7fb415f84760890d9ba446ed1205' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='ee1f1076ee2d41b4afb041358382842c' LIMIT 1
);
##GOGO

##修改人：fangcf
##修改内容：新增共享业务类型维护应用资源
##修改时间：2025-05-15
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '1a02d7f1388443129221f3c4b35f0f63', '应用资源', '应用资源', 'netbank:basicdata:nissharebustype:apply', 1, 'b78a7fb415f84760890d9ba446ed1205' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='1a02d7f1388443129221f3c4b35f0f63' LIMIT 1
);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-05-15
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '04f836e706c34c4899bee91bdb9bd693', 'zh_CN', '共享业务类型编码【{0}】已存在', 'nis.netbank.message.sharebustype.exist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'nis.netbank.message.sharebustype.exist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '757e9bedd6d648cf84da6a08baf7e588', 'zh_CN', '共享业务类型【{0}】已使用，不能删除！', 'nis.netbank.message.sharebustypename.use' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'nis.netbank.message.sharebustypename.use' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '820b074e17af46ae92bfc3ebc0f8859c', 'zh_CN', '共享业务类型【{0}】已使用，不能禁用！', 'nis.netbank.message.sharebustypename.use.no.validsign' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'nis.netbank.message.sharebustypename.use.no.validsign' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO


##修改人：fangcf
##修改内容：付款通知单表新增“共享业务类型”字段
##修改时间：2025-05-15
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'SHARE_BUS_TYPE_ID') THEN
      ALTER TABLE NIS_BILLHEAD ADD COLUMN SHARE_BUS_TYPE_ID VARCHAR(32) COMMENT '共享业务类型';
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：fangcf
##修改内容：付款中间表新增“共享业务类型”字段
##修改时间：2025-05-15
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ERP_PAYINFO' AND COLUMN_NAME = 'SHARE_BUS_TYPE_ID') THEN
      ALTER TABLE ERP_PAYINFO ADD COLUMN SHARE_BUS_TYPE_ID VARCHAR(32) COMMENT '共享业务类型';
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：fangcf
##修改内容：自动支付策略表新增“不支付用途”字段
##修改时间：2025-05-20
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_PAY_POLICY' AND COLUMN_NAME = 'NO_PAY_PURPOSE') THEN
      ALTER TABLE BIS_PAY_POLICY ADD COLUMN NO_PAY_PURPOSE VARCHAR(500) COMMENT '不支付用途';
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：fangcf
##修改内容：自动支付策略业务类别关联表新增“共享业务类型”字段
##修改时间：2025-05-20
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_PAY_POLICY_BUSTYPE' AND COLUMN_NAME = 'SHARE_BUS_TYPE_ID') THEN
      ALTER TABLE BIS_PAY_POLICY_BUSTYPE ADD COLUMN SHARE_BUS_TYPE_ID VARCHAR(32) COMMENT '共享业务类型';
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：fangcf
##修改内容：自动支付策略收款方关联表新增“是否支付收款人”字段
##修改时间：2025-05-20
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_PAY_POLICY_PAYEE' AND COLUMN_NAME = 'IS_PAYEE') THEN
      ALTER TABLE BIS_PAY_POLICY_PAYEE ADD COLUMN IS_PAYEE VARCHAR(1) DEFAULT '1' COMMENT '是否支付收款人：1-支付收款人，0-不支付收款人';
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：fangcf
##修改内容：自动支付策略收款方关联表新增“收款人户名”字段
##修改时间：2025-05-27
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_PAY_POLICY_PAYEE' AND COLUMN_NAME = 'PAYEE_NAME') THEN
      ALTER TABLE BIS_PAY_POLICY_PAYEE ADD COLUMN PAYEE_NAME VARCHAR(200) COMMENT '收款人户名' AFTER PAYEE_ID;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：fangcf
##修改内容：自动支付策略收款方关联表新增“收款人账号”字段
##修改时间：2025-05-27
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_PAY_POLICY_PAYEE' AND COLUMN_NAME = 'PAYEE_ACC') THEN
      ALTER TABLE BIS_PAY_POLICY_PAYEE ADD COLUMN PAYEE_ACC VARCHAR(30) COMMENT '收款人账号' AFTER PAYEE_ID;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：fangcf
##修改内容：自动支付策略收款方关联表新增“收款人账号”字段
##修改时间：2025-06-23
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0f4eeef054844f8ba5f4bcafc1352f36', 'zh_CN', '敞口额度已使用总额（票面口径）', 'bill.accebill.useBillMoneyByBill' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.useBillMoneyByBill' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '381e60d00f184f73b4e06451a256fea5', 'zh_CN', '敞口额度已使用总额（银行口径）', 'bill.accebill.useBillMoneyByBank' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.useBillMoneyByBank' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'aa6585985bff43aa8355e11d5be6ac76', 'zh_CN', '本月未到期（其中：敞口）', 'bill.accebill.openUnexpiredBillMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.openUnexpiredBillMoney' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e3e492dff5ef41b7891a798fc1285de5', 'zh_CN', '本月未到期（其中：低风险）', 'bill.accebill.lowRiskUnexpiredBillMoney' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'bill.accebill.lowRiskUnexpiredBillMoney' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人:huangyl
##修改时间:2023-03-29
##修改内容:添加国际化、新增还款计划明细暂存表、预计利息明细暂存表。


CREATE TABLE IF NOT EXISTS cms_schedule_dtl_temp  (
                                                      ID varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
                                                      BILL_CODE varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '业务单据号',
                                                      REPAY_CYCEL varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '还款周期 0:月 1:季度 2:半年度 3:一次性',
                                                      PREDICE_REPAY_DATE datetime(0) NULL DEFAULT NULL COMMENT '预计还款日',
                                                      REPAYMENT_MONEY decimal(20, 2) NULL DEFAULT NULL COMMENT '还款金额',
                                                      REPAYMENT_IST decimal(20, 2) NULL DEFAULT NULL COMMENT '还款利息',
                                                      CURRENT_REPAY_MONEY decimal(20, 2) NULL DEFAULT NULL COMMENT '本期还款金额',
                                                      REALITY_REPAY_DATE datetime(0) NULL DEFAULT NULL COMMENT '实际还款日',
                                                      REALITY_REPAY_MONEY decimal(20, 2) NULL DEFAULT NULL COMMENT '实际还款本金',
                                                      SURPLUS_MONEY decimal(20, 2) NULL DEFAULT NULL COMMENT '剩余本金',
                                                      IS_REPAY varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '归还标识 0:否 1:是',
                                                      REMARK varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
                                                      REPAYMENT_BEGIN_DATE datetime(0) NULL DEFAULT NULL COMMENT '还款开始日',
                                                      VALID_SIGN varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '有效标志',
                                                      INTERNAL_SIGN varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '内部标志:A1 -内部贷款录入  A2 -- 内部贷款发放\nB1 -银行借款录入  B2 -- 银行借款发放',
                                                      RELATION_ID varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '与主表关联id',
                                                      CREATE_DATE datetime(0) NULL DEFAULT NULL,
                                                      CREATE_BY varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                                                      CREATE_NAME varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                                                      UPDATE_DATE datetime(0) NULL DEFAULT NULL,
                                                      UPDATE_BY varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                                                      UPDATE_NAME varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                                                      REPAY_SIGN varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                                                      PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '还款计划表' ROW_FORMAT = Dynamic;
##GOGO

CREATE TABLE IF NOT EXISTS cms_provide_interest_temp  (
                                                          ID varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'id',
                                                          INTEREST_TIME datetime(0) NULL DEFAULT NULL COMMENT '计息时间',
                                                          CONTRACT_CODE varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '合同号',
                                                          BILL_CODE varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '单据编号',
                                                          BILL_TYPE varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '业务类型',
                                                          NET_ID varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '网点',
                                                          CORP_ID varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '单位',
                                                          CUR_ID varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '币别',
                                                          INTEREST_TYPE varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '计息类型',
                                                          REPAY_DATE datetime(0) NULL DEFAULT NULL COMMENT '还款日期',
                                                          INTEREST_START_DATE datetime(0) NULL DEFAULT NULL COMMENT '起息日期',
                                                          BLN_INST_DATE datetime(0) NULL DEFAULT NULL COMMENT '结束日期',
                                                          DAYS decimal(6, 0) NULL DEFAULT NULL COMMENT '计息天数',
                                                          CUMULATION decimal(16, 2) NULL DEFAULT NULL COMMENT '积数',
                                                          RATE decimal(8, 6) NULL DEFAULT NULL COMMENT '基准利率',
                                                          FLOAT_POINT decimal(10, 2) NULL DEFAULT NULL COMMENT '浮动比例点数',
                                                          INTEREST_RATE decimal(8, 6) NULL DEFAULT NULL COMMENT '计息利率',
                                                          RATE_FAX decimal(8, 6) NULL DEFAULT NULL COMMENT '税率',
                                                          LOAN_MONEY decimal(16, 2) NULL DEFAULT NULL COMMENT '本金',
                                                          REPAY_MONEY decimal(16, 2) NULL DEFAULT NULL COMMENT '归还本金',
                                                          LEFT_MONEY decimal(16, 2) NULL DEFAULT NULL COMMENT '剩余本金',
                                                          INTEREST decimal(16, 2) NULL DEFAULT NULL COMMENT '归还本金利息',
                                                          PAY_INTEREST decimal(16, 2) NULL DEFAULT NULL COMMENT '支付利息',
                                                          TOTAL_INTEREST decimal(16, 2) NULL DEFAULT NULL COMMENT '全部利息',
                                                          UN_PAY_INTEREST decimal(16, 2) NULL DEFAULT NULL COMMENT '未结利息',
                                                          INTEREST_FAX decimal(16, 2) NULL DEFAULT NULL COMMENT '利息税率',
                                                          REAL_INTEREST decimal(16, 2) NULL DEFAULT NULL COMMENT '实际付息',
                                                          INTEREST_SIGN varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '计息标识',
                                                          STATUS decimal(10, 0) NULL DEFAULT NULL COMMENT '还款状态',
                                                          RMK varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
                                                          REPAYMENT_SCHEDULE_ID varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '生成利息预计的还款计划ID',
                                                          SIGN_REPAY_DATE datetime(0) NULL DEFAULT NULL COMMENT '还款日期标识_标识分段属于这个时间的还款',
                                                          REPAY_SIGN varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                                                          PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '借款利息预计明细' ROW_FORMAT = Dynamic;
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
SELECT 'a37d83a65c064bbca2888ce62d2bb5a9', 'zh_CN', '试算', 'cms.button.repayPlanTest', NULL, '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL  FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = 'a37d83a65c064bbca2888ce62d2bb5a9' LIMIT 1
);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
SELECT '456946a31de5461191ff661130b802e6', 'zh_CN', '请选择还款计划或者进行试算！', 'cms.provide.loan.repay.select.repayPlanAndTest', NULL, '402880425b6b0a58015b6b0abeb10000', NULL, NULL, NULL, NULL  FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = '456946a31de5461191ff661130b802e6' LIMIT 1
);
##GOGO


-- 修改人:zhoup
-- 修改内容:银行借款还款申请线下提前还款试算脚本
-- 修改时间:2023-04-27
-- 在CMS_PROVIDE_INTEREST_TEMP表中增加字段:CHOOSE_REPAY_DATE(提前还款时间)

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'CMS_PROVIDE_INTEREST_TEMP' AND COLUMN_NAME = 'CHOOSE_REPAY_DATE') THEN
        alter table CMS_PROVIDE_INTEREST_TEMP add column CHOOSE_REPAY_DATE DATE ;
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

-- 在CMS_PROVIDE_INTEREST_TEMP表中增加字段:CHOOSE_REPAY_MONEY(提前还款金额)
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'CMS_PROVIDE_INTEREST_TEMP' AND COLUMN_NAME = 'CHOOSE_REPAY_MONEY') THEN
        ALTER TABLE CMS_PROVIDE_INTEREST_TEMP ADD COLUMN CHOOSE_REPAY_MONEY DECIMAL(16);
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

#####################################增加国际化#########################################
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '8cbf6bc806e2477dbae3abd372676c01', 'zh_CN', '还款本金金额不等于放款金额，请重新修改', 'cms.provide.loan.sumTotalError' from dual
where not exists (select 1 from sys_multi_lang where ID = '8cbf6bc806e2477dbae3abd372676c01' limit 1);
##GOGO

##修改人：zhous
##修改内容：新增票据银行余额不足时间配置表
##修改时间：2025-06-24
CREATE TABLE IF NOT EXISTS AB_INSUFFICIENT_FUNDS_TIME_CONFIG
(
    ID                      VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键',
    BANK_TYPE_ID VARCHAR(32) COMMENT '银行类型' ,
    BANK_TYPE_NAME VARCHAR(200) COMMENT '银行名称',
    TIME VARCHAR(200) COMMENT '时间',
    STATUS VARCHAR(1) COMMENT '状态 1-正常 0-禁用',
    IS_DELETED VARCHAR(1) COMMENT '是否删除 1-是' ,
    CREATE_BY               VARCHAR(32) COMMENT '创建人',
    CREATE_TIME                DATETIME COMMENT '创建时间',
    UPDATE_BY               VARCHAR(32) COMMENT '修改人',
    UPDATE_TIME                DATETIME COMMENT '修改时间'
    );
ALTER TABLE AB_INSUFFICIENT_FUNDS_TIME_CONFIG COMMENT '票据银行余额不足时间配置表';
##GOGO

###修改人:zhous
###修改内容:承兑到期和借款汇总余额不足提醒定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 3, '承兑到期和借款汇总余额不足提醒', '2024-08-20 16:30:15', '2024-08-20 16:30:15', 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillAccInsufficientFundsTaskService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-08-15 13:43:48', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='承兑到期和借款汇总余额不足提醒' limit 1);
commit;
##GOGO

INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE, STAR_TARGET)
SELECT 'bb642c6d268249aeb2ac834efe78c166', '5ff2fe20e7f248b093488f6eb312f9a5', '承兑到期和借款汇总余额不足提醒', 'bill:accebill:accInsufficientFundsRemind', null, '', null, 'bill:accebill:abemitbillpay:query', 0, 7, '单位:$!{corpName}\n公司类型：$!{companyTYpeName}\n银行账号:$!{bankAcc}\n账户名:$!{reqNm}\n开户行:$!{bankName}\n当前账户可用余额:$!{bankAccBalance}\n当前开票的银行账户余额不够兑付\n应付票据总笔数：$!{billNums}笔\n票面汇总金额：$!{billMoney}\n票据到期解付汇总金额：$!{releasePaymentMoney}\n到期日期：#if( $!{endDate2Str} && $!{endDateStr} ne $!{endDate2Str}) $!{endDateStr},$!{endDate2Str} #else $!{endDateStr} #end $!{variable}\n应付借款金额：$!{repayMoney}\n应付总资金：$!{totalPayMoney}\n需下拨金额：$!{xbMoney}', 1, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category WHERE ID = 'bb642c6d268249aeb2ac834efe78c166' LIMIT 1
);
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'RAT_BKRATION' AND COLUMN_NAME = 'GUARANTEE_BEGIN_DATE') THEN
ALTER TABLE RAT_BKRATION ADD COLUMN GUARANTEE_BEGIN_DATE DATE NULL COMMENT '担保开始时间';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'RAT_BKRATION' AND COLUMN_NAME = 'GUARANTEE_END_DATE') THEN
ALTER TABLE RAT_BKRATION ADD COLUMN GUARANTEE_END_DATE DATE NULL COMMENT '担保结束时间';
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'edcc33a560384066bf93e6de1357e052', 'zh_CN', '担保合同结束时间', 'table.ratBkration.garanteeContract.enddate' from dual
where not exists (select 1 from sys_multi_lang where ID = 'edcc33a560384066bf93e6de1357e052' limit 1);
#GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '022d6e4361e6444f8a5f4fe7bd35d996', 'zh_CN', '担保合同开始时间', 'table.ratBkration.garanteeContract.begindate' from dual
where not exists (select 1 from sys_multi_lang where ID = '022d6e4361e6444f8a5f4fe7bd35d996' limit 1);
#GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3216213be0d844dbb47e9d95d77febbb', 'zh_CN', '确定要批量禁用吗？', 'common.message.confirm.batchDisable' from dual
where not exists (select 1 from sys_multi_lang where ID = '3216213be0d844dbb47e9d95d77febbb' limit 1);
#GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '939207b03f974364a1f567c15ce16b14', 'zh_CN', '最终审批人', 'table.abAccrecords.finalApprover' from dual
where not exists (select 1 from sys_multi_lang where ID = '939207b03f974364a1f567c15ce16b14' limit 1);
#GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '58803184face4e93a5f4ee718613aeb4', '', '票据银行余额不足时间配置', '票据银行余额不足时间配置', '0c93d0aa1ea849fd86d3a0f42c3dab17', null, 500, 1, '/bill/accebill/bankInsufficientFundsTimeConfig/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = '58803184face4e93a5f4ee718613aeb4' LIMIT 1
);
commit;
##GOGO

##修改人：zhours
##修改时间：2025.06.26
##增加：增加消息提醒模板
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT 'fc9d09dc525011f0a25e005056a84efa', '183fc820962c411e9581ce3a7db5f552', '付款类需求清单差额下拨提醒', 'netbank:allocation:failure:difference', '', '', null, '', null, 10,
       '您有一笔需求清单下拨失败，请关注。\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n下拨金额：$!{downAmt}\n需求金额：$!{amt}\n差异金额：$!{diffAmt}',
       1,0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category WHERE ID = 'fc9d09dc525011f0a25e005056a84efa' LIMIT 1
);




##修改人：huangx
##修改时间：2025-06-20
##修改内容：00062025060764多级单位余额核算系列需求

##新增菜单“多级单位余额核算”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '3fbf540bedba41f2919d9697028f6659', '', '多级单位余额核算', '多级单位余额核算', 'd5b09cb914304f8987cb8e6e0ecb3ff3', null, 8, 1, '', null, null, null, null, null, null, 1, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '3fbf540bedba41f2919d9697028f6659' LIMIT 1
);
##GOGO

##新增菜单“多级单位余额核算>>单位归集余额维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '44aba7c192d141df9a04348cefc0369a', '', '单位归集余额维护', '单位归集余额维护', '3fbf540bedba41f2919d9697028f6659', null, 1, 1, '/bankacc/biscorppoolbal/list', null, null, null, null, null, null, 0, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '44aba7c192d141df9a04348cefc0369a' LIMIT 1
);
##GOGO

##新增菜单资源(单位归集余额维护)
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '772f98b075ed410f8735f58929b16008', '查询资源', '查询资源', 'bankacc:biscorppoolbal:query', 1, '44aba7c192d141df9a04348cefc0369a' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '772f98b075ed410f8735f58929b16008' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '888a31d7f80d4b04baa7481ee86b1551', '应用资源', '应用资源', 'bankacc:biscorppoolbal:apply', 1, '44aba7c192d141df9a04348cefc0369a' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '888a31d7f80d4b04baa7481ee86b1551' LIMIT 1
);
##GOGO

##新增菜单“多级单位余额核算>>单位归集透支金额维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'd94a7cf4362e45749f291b6d7838532b', '', '单位归集透支金额维护', '单位归集透支金额维护', '3fbf540bedba41f2919d9697028f6659', null, 2, 1, '/bankacc/biscorppooloverdraft/list', null, null, null, null, null, null, 0, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'd94a7cf4362e45749f291b6d7838532b' LIMIT 1
);
##GOGO

##新增菜单资源(单位归集透支金额维护)
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '452f98b075ed410f8735f58929b16048', '查询资源', '查询资源', 'bankacc:biscorppooloverdraft:query', 1, 'd94a7cf4362e45749f291b6d7838532b' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '452f98b075ed410f8735f58929b16048' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '788a31d7f80d4b04baa7481ee86b1351', '应用资源', '应用资源', 'bankacc:biscorppooloverdraft:apply', 1, 'd94a7cf4362e45749f291b6d7838532b' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '788a31d7f80d4b04baa7481ee86b1351' LIMIT 1
);
##GOGO

##新增菜单“多级单位余额核算>>单位归集配置维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '97d40c130fcf4c1692fbc81ed1cbdbc5', '', '单位归集配置维护', '单位归集配置维护', '3fbf540bedba41f2919d9697028f6659', null, 3, 1, '/bankacc/biscorppoolconfig/list', null, null, null, null, null, null, 0, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '97d40c130fcf4c1692fbc81ed1cbdbc5' LIMIT 1
);
##GOGO

##新增菜单资源(单位归集配置维护)
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '66d40c130fcf4c1692fbc81ed1cbdbc5', '查询资源', '查询资源', 'bankacc:biscorppoolconfig:query', 1, '97d40c130fcf4c1692fbc81ed1cbdbc5' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '66d40c130fcf4c1692fbc81ed1cbdbc5' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '97d30c130fcf4c1692fbc81ed1cbdbc5', '应用资源', '应用资源', 'bankacc:biscorppoolconfig:apply', 1, '97d40c130fcf4c1692fbc81ed1cbdbc5' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '97d30c130fcf4c1692fbc81ed1cbdbc5' LIMIT 1
);
##GOGO

##新增菜单“多级单位余额核算>>单位归集流水维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'f73d886533794f508382b8bead018216', '', '单位归集流水维护', '单位归集流水维护', '3fbf540bedba41f2919d9697028f6659', null, 4, 1, '/bankacc/biscorppooldtl/list', null, null, null, null, null, null, 0, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'f73d886533794f508382b8bead018216' LIMIT 1
);
##GOGO

##新增菜单资源(单位归集流水维护)
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'cc3d886533794f508382b8bead018216', '查询资源', '查询资源', 'bankacc:biscorppooldtl:query', 1, 'f73d886533794f508382b8bead018216' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'cc3d886533794f508382b8bead018216' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'de3d886533794f508382b8bead018221', '应用资源', '应用资源', 'bankacc:biscorppooldtl:apply', 1, 'f73d886533794f508382b8bead018216' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'de3d886533794f508382b8bead018221' LIMIT 1
);
##GOGO

##修改内容:新增多级单位余额核算-单位归集余额表
CREATE TABLE IF NOT EXISTS BIS_CORP_POOL_BAL  (
    ID                  VARCHAR(32)                    NOT NULL,
    CORP_ID          	VARCHAR(32) COMMENT '单位ID',
    CUR_ID          	VARCHAR(32) COMMENT '币别ID',
    BAL          		decimal(19,2) COMMENT '余额',
    BAL_DATE       		datetime COMMENT '余额日期',
    GET_DATE        	datetime(6) COMMENT '获取时间',
    INIT_SIGN       	VARCHAR(2) COMMENT '初始化标志0-否 1-是',
    CREATE_BY             VARCHAR(50) COMMENT '创建人登录名称',
    CREATE_TIME          DATETIME(6) COMMENT '创建日期',
    UPDATE_BY            VARCHAR(50) COMMENT '更新人登录名称',
    UPDATE_TIME          DATETIME(6) COMMENT '更新日期',
    IS_DELETED          VARCHAR(2) COMMENT '是否删除',
    CONSTRAINT PK_BIS_CORP_POOL_BAL PRIMARY KEY (ID)
) COMMENT '多级单位余额核算-单位归集余额表';
##GOGO

##修改内容:新增多级单位余额核算-单位归集透支金额表
CREATE TABLE IF NOT EXISTS BIS_CORP_POOL_OVERDRAFT  (
    ID                   VARCHAR(32)                    NOT NULL,
    CORP_ID          	 VARCHAR(32) COMMENT '单位ID',
    CUR_ID          	 VARCHAR(32) COMMENT '币别ID',
    OVERDRAFT_AMT        decimal(19,2) COMMENT '透支金额',
    CREATE_BY            VARCHAR(50) COMMENT '创建人登录名称',
    CREATE_TIME          DATETIME(6) COMMENT '创建日期',
    UPDATE_BY            VARCHAR(50) COMMENT '更新人登录名称',
    UPDATE_TIME          DATETIME(6) COMMENT '更新日期',
    IS_DELETED          VARCHAR(2) COMMENT '是否删除',
    CONSTRAINT PK_BIS_CORP_POOL_OVERDRAFT PRIMARY KEY (ID)
) COMMENT '多级单位余额核算-单位归集透支金额表';
##GOGO

##修改内容:新增多级单位余额核算-单位归集配置表
CREATE TABLE IF NOT EXISTS BIS_CORP_POOL_CONFIG  (
    ID                   VARCHAR(32)                    NOT NULL,
    CONFIG_CODE          VARCHAR(32) COMMENT '规则编码',
    CONFIG_NAME          VARCHAR(100) COMMENT '规则名称',
    BUS_TYPE        	 VARCHAR(2) COMMENT '类型',
    VALID_SIGN       	 VARCHAR(2) COMMENT '有效标志 1-有效 0-无效',
    CREATE_BY            VARCHAR(50) COMMENT '创建人登录名称',
    CREATE_TIME          DATETIME(6) COMMENT '创建日期',
    UPDATE_BY            VARCHAR(50) COMMENT '更新人登录名称',
    UPDATE_TIME          DATETIME(6) COMMENT '更新日期',
    IS_DELETED          VARCHAR(2) COMMENT '是否删除',
    CONSTRAINT PK_BIS_CORP_POOL_CONFIG PRIMARY KEY (ID)
) COMMENT '多级单位余额核算-单位归集配置表';
##GOGO

##修改内容:新增多级单位余额核算-单位归集配置规则表
CREATE TABLE IF NOT EXISTS BIS_CORP_POOL_CONFIG_RULE  (
    ID                   VARCHAR(32)                    NOT NULL,
    CONFIG_ID			 VARCHAR(32) COMMENT '配置ID',
    MATCH_RULE           VARCHAR(10) COMMENT '匹配规则',
    MATCH_ELEMENTS       VARCHAR(30) COMMENT '匹配要素',
    MATCH_RELATIONS      VARCHAR(10) COMMENT '匹配关系类型',
    MATCH_CONTENT      	 VARCHAR(100) COMMENT '匹配内容',
    LEFT_PARENTHESIS     VARCHAR(2) COMMENT '左括号',
    RIGHT_PARENTHESIS    VARCHAR(2) COMMENT '右括号',
    SORT				 INT COMMENT '排序',
    CREATE_BY            VARCHAR(50) COMMENT '创建人登录名称',
    CREATE_TIME          DATETIME(6) COMMENT '创建日期',
    UPDATE_BY            VARCHAR(50) COMMENT '更新人登录名称',
    UPDATE_TIME          DATETIME(6) COMMENT '更新日期',
    IS_DELETED          VARCHAR(2) COMMENT '是否删除',
    CONSTRAINT PK_BIS_CORP_POOL_CONFIG_RULEG PRIMARY KEY (ID)
) COMMENT '多级单位余额核算-单位归集配置规则表';
##GOGO

##单位归集配置类型
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT 'abd46c5116cdd79d1697eb6ba4ffb7f6', 'bisCorpPoolConfig.busType', '单位归集配置类型' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = 'abd46c5116cdd79d1697eb6ba4ffb7f6' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '10f4e2c65ca2ca2bc565e731c5b06c39', '0', '归集', NULL, 'abd46c5116cdd79d1697eb6ba4ffb7f6', 500 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '10f4e2c65ca2ca2bc565e731c5b06c39' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '52a8648d19e370e0ebab65274992f86d', '1', '下拨', 'abd46c5116cdd79d1697eb6ba4ffb7f6', '1' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '52a8648d19e370e0ebab65274992f86d' LIMIT 1
);
##GOGO

##流水增加 类型 1-归集 2-下拨 3-其他
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'POOL_TYPE') THEN
      ALTER TABLE BIS_ACC_DTL ADD COLUMN POOL_TYPE VARCHAR(2);
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##流水增加 认领标志
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'POOL_BLEND_SIGN') THEN
      ALTER TABLE BIS_ACC_DTL ADD COLUMN POOL_BLEND_SIGN VARCHAR(2) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##多级单位归集余额核算-类型
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '7f1b890389304c92a8282682b823cad1', 'bisCorpPoolDtl.poolType', '多级单位归集余额核算-类型' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '7f1b890389304c92a8282682b823cad1' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'b765fb5cdcf74a9a9410cfc90083c9db', '1', '归集', NULL, '7f1b890389304c92a8282682b823cad1', 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'b765fb5cdcf74a9a9410cfc90083c9db' LIMIT 1
    );
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'ff0f0b99bf5b4c56a5abd55e346d11d1', '2', '下拨', NULL, '7f1b890389304c92a8282682b823cad1', 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'ff0f0b99bf5b4c56a5abd55e346d11d1' LIMIT 1
    );
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '0da325e0046c457fbda512f38d88d176', '3', '其他', NULL, '7f1b890389304c92a8282682b823cad1', 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '0da325e0046c457fbda512f38d88d176' LIMIT 1
    );
##GOGO

##定时任务：单位归集余额自动计算
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '单位归集余额自动计算', '2025-06-26 19:20:18', '2025-06-26 19:20:18', 'XXL', '', 'CRON', '0 0 1,15 * * ?', 'DO_NOTHING', 'FIRST', 'BisCorpPoolDtlService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-06-26 19:20:18', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='总账户资金下拨流水自动记账' limit 1);
commit;
##GOGO



##定时任务：单位归集流水自动认领
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '单位归集流水自动认领', '2025-06-26 19:20:18', '2025-06-26 19:20:18', 'XXL', '', 'CRON', '0 0 30 * * ?', 'DO_NOTHING', 'FIRST', 'BisCorpPoolConfigRuleService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-06-26 19:20:18', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='单位归集流水自动认领' limit 1);
commit;
##GOGO


##修改人：fangcf
##修改内容：银行开票授信资料维护表
##修改时间：2025-06-30
CREATE TABLE IF NOT EXISTS AB_BILL_CREDIT_INFO_MAINTAIN
(
   ID                   	VARCHAR(32) NOT NULL COMMENT '主键',
   CORP_ID					VARCHAR(32) COMMENT '单位id',
   BANK_TYPE_ID       		VARCHAR(32) COMMENT '银行类型',
   CREDIT_TYPE				VARCHAR(100) COMMENT '授信类型：敞口和低风险',
   BOND_RATIO				DECIMAL(18,2) COMMENT '保证金比例',
   OPEN_BILL_RATE			DECIMAL(18,2) COMMENT '开票手续费',
   BOND_RATE				VARCHAR(200) COMMENT '保证金利率',
   IS_USED					VARCHAR(2) COMMENT '是否能正常使用：1-是，0-否',
   IS_DELETED				VARCHAR(2) COMMENT '',
   CREATE_BY            	VARCHAR(32) COMMENT '创建人',
   CREATE_TIME         		DATETIME COMMENT '创建时间',
   UPDATE_BY            	VARCHAR(32) COMMENT '修改人',
   UPDATE_TIME         		DATETIME COMMENT '修改时间'
);
ALTER TABLE AB_BILL_CREDIT_INFO_MAINTAIN COMMENT '银行开票授信资料维护表';
##GOGO

##修改人：fangcf
##修改内容：新增银行开票授信资料维护菜单
##修改时间：2025-06-30
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'db323249554343068e23bb32177e1d1b', null, '银行开票授信资料维护', '银行开票授信资料维护', '0c93d0aa1ea849fd86d3a0f42c3dab17', null, 600, 1, '/bill/accebill/creditMaintain/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'db323249554343068e23bb32177e1d1b' LIMIT 1
    );
commit;
##GOGO

##修改人：fangcf
##修改内容：新增共享业务类型维护查询资源
##修改时间：2025-06-30
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '9b33f9457c6449da88d65e459a8ff92a', '查询资源', '查询资源', 'bill:accebill:credit:maintain:query', 1, 'db323249554343068e23bb32177e1d1b' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='9b33f9457c6449da88d65e459a8ff92a' LIMIT 1
);
##GOGO

##修改人：fangcf
##修改内容：新增共享业务类型维护应用资源
##修改时间：2025-06-30
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '8062f7c78bbb489fa6fb780deec19572', '应用资源', '应用资源', 'bill:accebill:credit:maintain:apply', 1, 'db323249554343068e23bb32177e1d1b' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='8062f7c78bbb489fa6fb780deec19572' LIMIT 1
);
##GOGO

##修改人：fangcf
##修改内容：新增字典组"授信类型"
##修改时间：2025-06-30
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '3bdedaed6bef4f83811014f9196f3ed9', 'bill:accebill.creditType', '授信类型'from dual
where not exists (select 1 from sys_dict_group where ID ='3bdedaed6bef4f83811014f9196f3ed9' limit 1);
##GOGO

##修改人：fangcf
##修改内容：新增字典"敞口"
##修改时间：2025-06-30
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '1459cd1b1a94487bbf457fcf2a0e73ac', '1', '敞口',null,'3bdedaed6bef4f83811014f9196f3ed9',1 from dual
where not exists (select 1 from sys_dict where ID ='1459cd1b1a94487bbf457fcf2a0e73ac' limit 1);
##GOGO

##修改人：fangcf
##修改内容：新增字典"低风险"
##修改时间：2025-06-30
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'a8ff9e2f3cab4283a5b2f196708c3e27', '2', '低风险',null,'3bdedaed6bef4f83811014f9196f3ed9',2 from dual
where not exists (select 1 from sys_dict where ID ='a8ff9e2f3cab4283a5b2f196708c3e27' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd85948b4e22c4777be96d08be34718b1', 'zh_CN', '单位不能为空', 'accebill.message.creditmaintain.corpid.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.creditmaintain.corpid.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '71d7a026e2924425bfdb9cc37eeb6851', 'zh_CN', '银行名称不能为空', 'accebill.message.creditmaintain.banktypeid.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.creditmaintain.banktypeid.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f221318aad8740f3b6e61e890f61968e', 'zh_CN', '授信类型不能为空', 'accebill.message.creditmaintain.credittype.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.creditmaintain.credittype.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '4ef42690e96a4210958af90d9dd4cdf2', 'zh_CN', '银行开票授信资料在相同单位、相同银行、相同授信类型下必须唯一', 'accebill.message.creditmaintain.not.unique' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.creditmaintain.not.unique' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：fangcf
##修改内容：银行计划开票金额维护表
##修改时间：2025-06-30
CREATE TABLE IF NOT EXISTS AB_BILL_PLAN_AMT_MAINTAIN
(
   ID                   	VARCHAR(32) NOT NULL COMMENT '主键',
   CORP_ID					VARCHAR(32) COMMENT '单位id',
   BANK_TYPE_ID       		VARCHAR(32) COMMENT '银行类型',
   CREDIT_TYPE				VARCHAR(100) COMMENT '授信类型：敞口和低风险',
   WEEK_START_DAY			DATE COMMENT '周开始日',
   WEEK_END_DAY				DATE COMMENT '周结束日',
   CURR_WEEK_BILL_MONEY		DECIMAL(18,2) COMMENT '本周计划开票金额',
   NEXT_WEEK_BILL_MONEY		DECIMAL(18,2) COMMENT '下周计划开票金额',
   IS_DELETED				VARCHAR(2) COMMENT '',
   CREATE_BY            	VARCHAR(32) COMMENT '创建人',
   CREATE_TIME         		DATETIME COMMENT '创建时间',
   UPDATE_BY            	VARCHAR(32) COMMENT '修改人',
   UPDATE_TIME         		DATETIME COMMENT '修改时间'
);
ALTER TABLE AB_BILL_PLAN_AMT_MAINTAIN COMMENT '银行计划开票金额维护表';
##GOGO

##修改人：fangcf
##修改内容：新增银行计划开票金额维护菜单
##修改时间：2025-06-30
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'cb11bfc186bf4caa8f2e3d563267f8e6', null, '银行计划开票金额维护', '银行计划开票金额维护', '0c93d0aa1ea849fd86d3a0f42c3dab17', null, 700, 1, '/bill/accebill/planAmtMaintain/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'cb11bfc186bf4caa8f2e3d563267f8e6' LIMIT 1
    );
commit;
##GOGO

##修改人：fangcf
##修改内容：新增银行计划开票金额维护查询资源
##修改时间：2025-06-30
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'a3a75d3dbe4642eabb31d44f46b671a7', '查询资源', '查询资源', 'bill:accebill:planamt:maintain:query', 1, 'cb11bfc186bf4caa8f2e3d563267f8e6' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='a3a75d3dbe4642eabb31d44f46b671a7' LIMIT 1
);
##GOGO

##修改人：fangcf
##修改内容：新增银行计划开票金额维护应用资源
##修改时间：2025-06-30
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'c8ba7c5965c5470da0c495e8da5782f3', '应用资源', '应用资源', 'bill:accebill:planamt:maintain:apply', 1, 'cb11bfc186bf4caa8f2e3d563267f8e6' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='c8ba7c5965c5470da0c495e8da5782f3' LIMIT 1
);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7adeb24ab4ad475cab737908178d2f5c', 'zh_CN', '银行计划开票金额在相同单位、相同银行、相同周开始日、相同周结束日下必须唯一', 'accebill.message.planamtmaintain.not.unique' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.planamtmaintain.not.unique' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '435c625da4604ec6b47393ed9974a62a', 'zh_CN', '周开始日必须是周一', 'accebill.message.planamtmaintain.must.monday' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.planamtmaintain.must.monday' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6408e44e252a42ad99241c56d0d69ad9', 'zh_CN', '周结束日必须是对应周开始日的周日', 'accebill.message.planamtmaintain.must.sunday' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.planamtmaintain.must.sunday' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9fc76563745946dab9756ce29e5df638', 'zh_CN', '周开始日不能为空', 'accebill.message.planamtmaintain.weekstartday.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.planamtmaintain.weekstartday.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '02c7adcbf90c4e54936e573fdc198118', 'zh_CN', '周结束日不能为空', 'accebill.message.planamtmaintain.weekendday.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.planamtmaintain.weekendday.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '6b8212e187d94722800ce0118b46dc5f', 'zh_CN', '本周计划开票金额不能为空', 'accebill.message.planamtmaintain.currweekbillmoney.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.planamtmaintain.currweekbillmoney.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9348b9d6c2fa49fc8e72d3a92be3b1dd', 'zh_CN', '下周计划开票金额不能为空', 'accebill.message.planamtmaintain.nextweekbillmoney.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'accebill.message.planamtmaintain.nextweekbillmoney.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO


##修改人员:xiay
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c7c0dfdb20337ce2061ec94627e18f3a', 'zh_CN', '同单位、同币种、同余额日期的归集余额数据需唯一！', 'netbank.message.biscorppoolbal.repeat' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'netbank.message.biscorppoolbal.repeat' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##票据流水表增加 最终审批人
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'ab_accrecords' AND COLUMN_NAME = 'final_approver') THEN
ALTER TABLE ab_accrecords ADD COLUMN final_approver VARCHAR(100);
END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改人员:huangx
##修改内容:增加国际化
##修改日期:2025-07-02
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c4c0dfdb20337ce2061ec94627e18f3a', 'zh_CN', '同单位、同币种的透支金额数据需唯一！', 'netbank.message.biscorppooloverdraft.repeat' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'netbank.message.biscorppooloverdraft.repeat' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
##修改人：fangcf
##修改内容：新增银行授信余额表菜单
##修改时间：2025-07-02
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'a3429f7469da401f961448b497304e41', null, '银行授信余额表', '银行授信余额表', 'b163200da8f44fb7807d33833ff684cb', null, 600, 1, '/cms/report/creditBalance/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'a3429f7469da401f961448b497304e41' LIMIT 1
    );
commit;
##GOGO

##修改人：fangcf
##修改内容：新增银行计划开票金额维护查询资源
##修改时间：2025-07-02
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'd7d66da7a8d7448c80d51fb4eede9d65', '查询资源', '查询资源', 'cms:report:creditbalance:query', 1, 'a3429f7469da401f961448b497304e41' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='d7d66da7a8d7448c80d51fb4eede9d65' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'dq65fb5cdcf74a9a9410cfc90083c9db', '0', '待确认', NULL, '7f1b890389304c92a8282682b823cad1', 0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'dq65fb5cdcf74a9a9410cfc90083c9db' LIMIT 1
    );
##GOGO

##修改日期：2025-7-10
##修改人员：zrs
##头寸调拨审批企微消息提醒定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '自动同步多级单位内部账号余额', null, null, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoSyncLevelCorpBalancesJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='自动同步多级单位内部账号余额' limit 1);
commit;
##GOGO

##修改人员:zhous
##修改内容:民生开票需求优化
##修改日期:2025-07-15
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '09d13142c83d4fc6889b306d14fa7d88', 'zh_CN', '民生保证金列表', 'table.abAccrecords.msBondAcc.list' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'table.abAccrecords.msBondAcc.list' AND LANG_CODE = 'zh_CN' limit 1);
commit;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'AB_VOUCHERINFO' AND COLUMN_NAME = 'CMB_BAIL_FLAG') THEN
        ALTER TABLE AB_VOUCHERINFO ADD COLUMN CMB_BAIL_FLAG VARCHAR(4) DEFAULT NULL COMMENT '特定银行保证金选择标识 0-否 1-招行 2-民生';
    END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##新增内容: 添加民生银行保证金账户信息表
CREATE TABLE IF NOT EXISTS CMBC_BAIL_ACCOUNT_INFO
(
    ID      VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not NULL COMMENT '主键ID',
    ACCT_NO VARCHAR(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '保证金账号',
    ACCT_NAME  VARCHAR(62) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '账户名称',
    CUR  VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL  COMMENT '币种',
    OPEN_DATE  DATETIME COMMENT '开户日期',
    EXPIRE_DATE  DATETIME  COMMENT '到期日期',
    DEPT_NAME  VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '开户机构',
    RATE  DECIMAL(15, 2) COMMENT '利率',
    TIM_TYPE  VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '保证金类型',
    BAL  DECIMAL(15, 2) COMMENT '账户余额',
    AVAIL_BAL  DECIMAL(15, 2) COMMENT '可用余额',
    CREATE_DATE    DATETIME     COMMENT '创建时间',
    UPDATE_DATE    DATETIME     COMMENT '更新时间',
    CONSTRAINT PK_CMBC_BAIL_ACCOUNT_INFO PRIMARY KEY (ID),
    constraint AK_ACCT_NO_CMB_BAIL_ACCOUNT_INFO unique (ACCT_NO)
    ) COMMENT ='民生银行保证金账户信息表';
##GOGO

##修改人：zrs
##修改内容：新增单位出纳维护菜单
##修改时间：2025-07-11
INSERT INTO sys_menu (ID, DESCRIPTION, NAME, PARENT_ID,  SORT, STATUS, URL, IS_MODULE, IS_EXTEND)
SELECT '67540d805e0311f0a25e005056a84efa','单位出纳维护', '单位出纳维护', 'd61f91e27ed9473ab9e8183343ea4507',  32, 1, '/netbank/corpCashier/list', 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = '67540d805e0311f0a25e005056a84efa' LIMIT 1
);
commit;
##GOGO

##修改人：zrs
##修改内容：新增单位出纳维护查询资源
##修改时间：2025-07-11
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'ae4bc09d5e0311f0a25e005056a84efa', '查询资源', '查询资源', 'netbank:corpCashier:query', 1, '67540d805e0311f0a25e005056a84efa' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='ae4bc09d5e0311f0a25e005056a84efa' LIMIT 1
);
##GOGO

##修改人：zrs
##修改内容：新增单位出纳维护查询资源
##修改时间：2025-07-11
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'b67d4f045e0511f0a25e005056a84efa', '编辑资源', '编辑资源', 'netbank:corpCashier:edit', 1, '67540d805e0311f0a25e005056a84efa' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='b67d4f045e0511f0a25e005056a84efa' LIMIT 1
);
##GOGO

CREATE TABLE IF NOT EXISTS NIS_CORP_CASHIER
(
    ID                   	VARCHAR(32) NOT NULL COMMENT '主键',
    CORP_ID			        VARCHAR(32) COMMENT '单位ID',
    CORP_CODE			    VARCHAR(32) COMMENT '单位CODE',
    CORP_NAME               VARCHAR(200) COMMENT '单位名称',
    STATUS					VARCHAR(2) COMMENT '状态：1-正常，0-禁用',
    CREATE_BY            	VARCHAR(32) COMMENT '创建人',
    CREATE_DATE         	DATETIME COMMENT '创建时间',
    UPDATE_BY            	VARCHAR(32) COMMENT '修改人',
    UPDATE_DATE         	DATETIME COMMENT '修改时间'
);
ALTER TABLE NIS_CORP_CASHIER COMMENT '单位出纳表';

CREATE TABLE IF NOT EXISTS NIS_CORP_CASHIER_USER
(
    ID                   	VARCHAR(32) NOT NULL COMMENT '主键',
    CORP_CASHIER_ID			VARCHAR(32) COMMENT '主表ID',
    CORP_ID			        VARCHAR(32) COMMENT '单位ID',
    USER_ID			        VARCHAR(32) COMMENT '用户ID',
    USER_CODE			    VARCHAR(100) COMMENT '用户登录名',
    USER_NAME               VARCHAR(200) COMMENT '用户名称'
);
ALTER TABLE NIS_CORP_CASHIER_USER COMMENT '单位出纳表用户表';

##修改人:zrs
##修改内容:付款交易结果视图，付款单记录提交人改成改单位维护的出纳人
##修改时间:2025-07-14
CREATE OR REPLACE VIEW dsl_payment_result_view AS
-- 撤销付款
select erp.serial_no_erp       as paymentNo,
       n.bill_no               as paymentVoucherNo,
       null                    as flowNo,
       erp.GX_VOUCHER_TYPE     as paymentType,
       CASE WHEN (nd.sync_status='S' AND ft.voucher_type IN ('66','67','68','69','70','71'))  THEN 'R'
            ELSE 'C' END AS  paymentStatus,  -- 撤销付款,回传过成功在撤销，均回传退票R
       null                    as actualPaymentAmount,
       null                    as actualPaymentDate,
       null                    as payBankAccountNo,
       null                    as payBankAccountName,
       erp.SETTLE_CORP_CODE    as company,
       erp.corp_code           as actualPaymentCompany,
       -- (select CONCAT_WS( '+',nm.op,nm.op_name) from nis_message nm left join nis_billhead nis on nm.BILL_NO = nis.id where nis.BILL_NO = n.bill_no and nm.MESSAGE_TYPE = '2' limit 1)
       (SELECT
            GROUP_CONCAT(
                    CONCAT(u.USER_CODE, '+', u.USER_NAME)
                    ORDER BY u.USER_CODE SEPARATOR '#'
            ) AS user_info
        FROM nis_corp_cashier c
                 JOIN nis_corp_cashier_user u ON c.ID = u.CORP_CASHIER_ID
        WHERE c.`STATUS` = 1 AND erp.corp_code = c.CORP_CODE
        GROUP BY c.CORP_CODE ) as paymentCashierUserNo,  -- 付款单记录提交人
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
       CASE WHEN (nd.sync_status='S' AND ft.voucher_type IN ('66','67','68','69','70','71')) THEN nd.bill_no
            ELSE NULL END AS refundBankFlowNo,   -- 撤票则取付款单做退汇流水号
       CASE WHEN (nd.sync_status='S' AND ft.voucher_type IN ('66','67','68','69','70','71')) THEN nd.pay_money
            ELSE NULL END AS  refundAmount,      -- 撤票取付款单金额做退汇金额
       CASE WHEN (nd.sync_status='S' AND ft.voucher_type IN ('66','67','68','69','70','71')) THEN nd.update_date
            ELSE NULL END AS refundFlowTradeDate,    -- 撤票取操作时间做退汇交易时间
       CASE WHEN (nd.sync_status='S' AND ft.voucher_type IN ('66','67','68','69','70','71')) THEN nd.payee_acc_name
            ELSE NULL END  AS refundRemitter,    -- 撤票取对方账户名称
       CASE WHEN (nd.sync_status='S' AND ft.voucher_type IN ('66','67','68','69','70','71')) THEN nd.payee_acc
            ELSE NULL END   AS refundRemitterAccount,    -- 撤票取对方账户信息做退汇账户
       CASE WHEN (nd.sync_status='S' AND ft.voucher_type IN ('66','67','68','69','70','71')) THEN '撤票'
            ELSE NULL END  AS refundFlowSummary,   -- 摘要默认为【撤票】
       CASE WHEN (nd.sync_status='S' AND ft.voucher_type IN ('66','67','68','69','70','71')) THEN nd.update_by
            ELSE NULL END  AS refundFlowBlendingUser,   -- 撤票取操作人做退汇用户
       CASE WHEN (nd.sync_status='S' AND ft.voucher_type IN ('66','67','68','69','70','71')) THEN DATE_FORMAT(nd.update_date,'%Y-%m-%d %H:%i:%s')
            ELSE NULL END  AS refundFlowBlendingDate,   -- 撤票取操作时间做退汇时间
       CASE WHEN nd.sync_status='S' THEN '撤票'
            ELSE IFNULL(bt.bus_type_name,erp.bus_type) END  AS revokePaymentReason   -- 撤票默认类型为【撤票】
from erp_payinfo erp
         inner join dsl_all_bill_view n on n.erp_id = erp.id
         LEFT JOIN nis_billhead nd ON nd.id=n.id
         LEFT JOIN fc_voucher_type ft ON ft.id=nd.voucher_type_id
         left join bis_exc exc on exc.cbs_bill_id = n.id
         left join bt_charge_back_type bt ON bt.bus_type = erp.bus_type
where 1=1
  AND n.status = -2
  AND not exists( -- 付款删除
    select 1 from DSL_INTEFACE_FLAG flag where flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'C')
                                           AND flag.TABLE_SOURCE = 'dsl_payment_result' AND flag.SYNC_STATUS = 'S')
  AND not exists( -- 付款撤票
    select 1 from DSL_INTEFACE_FLAG flag where flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'R')
                                           AND flag.TABLE_SOURCE = 'dsl_payment_result' AND flag.SYNC_STATUS = 'S')
UNION ALL

-- 付款成功
select erp.serial_no_erp       as paymentNo,
       n.bill_no               as paymentVoucherNo,
       dtl.ID                  as flowNo,
       erp.GX_VOUCHER_TYPE     as paymentType,
       'S'                     as paymentStatus, -- 付款成功
       case when (av.id is not null or n.is_payed='1') then n.pay_money else dtl.amt end
                               as actualPaymentAmount,
       case when (f.SYS_VOUCHER_CODE = '04' AND n.status = 95) then DATE_FORMAT(dtl.trans_time,'%Y-%m-%d %H:%i:%s') -- 付款方式为现付，审批通过
            when dtl.blend_sign = '1' then DATE_FORMAT(dtl.trans_time,'%Y-%m-%d %H:%i:%s') -- 线上交易成功或线下审批通过，进行了付款勾兑
            WHEN aaa.BRIEF_ID='002' THEN aaa.BUSINESS_DATE
           -- WHEN aa.BRIEF_ID in ('001','010') THEN aa.BUSINESS_DATE
            WHEN aa.BRIEF_ID='001' THEN aa.BUSINESS_DATE
            WHEN (aa.BRIEF_ID='010' AND aa.SEND_SIGN = '1') THEN aa.apply_date
            WHEN (aa.BRIEF_ID='010' AND aa.SEND_SIGN = '0') THEN aa.BUSINESS_DATE
            else DATE_FORMAT(aa.BUSINESS_DATE,'%Y-%m-%d %H:%i:%s') end -- 票据付款，取业务办理日期
                               as actualPaymentDate,
       dtl.bank_acc            as payBankAccountNo,
       dtl.bank_name           as payBankAccountName,
       erp.SETTLE_CORP_CODE    as company,
       erp.corp_code           as actualPaymentCompany,
       -- (select CONCAT_WS( '+',nm.op,nm.op_name) from nis_message nm left join nis_billhead nis on nm.BILL_NO = nis.id where nis.BILL_NO = n.bill_no and nm.MESSAGE_TYPE = '2' limit 1)
       (SELECT
            GROUP_CONCAT(
                    CONCAT(u.USER_CODE, '+', u.USER_NAME)
                    ORDER BY u.USER_CODE SEPARATOR '#'
            ) AS user_info
        FROM nis_corp_cashier c
                 JOIN nis_corp_cashier_user u ON c.ID = u.CORP_CASHIER_ID
        WHERE c.`STATUS` = 1 AND erp.corp_code = c.CORP_CODE
        GROUP BY c.CORP_CODE ) as paymentCashierUserNo,  -- 付款单记录提交人
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
         left join ab_accrecords aaa on aa.vvid = aaa.vvid and aaa.BRIEF_ID='002' and aaa.opt_status='19'
where 1=1
  AND (  -- 票据付款 BRIEF_ID 001开票登记,003提示收票,010票据背书;  SEND_SIGN 0线下,1线上;   BRIEF_ID 010票据背书;  opt_status 18待签收,19已完成
    (aa.id IS NOT NULL and
     (  (aa.SEND_SIGN = '1' and aaa.id is not null and n.status in (95,99) and aaa.return_code in ('0','99'))
         or (aa.SEND_SIGN = '0' and aa.BRIEF_ID='001' and aa.opt_status='19' and n.status in (95,99))
         or (aa.SEND_SIGN = '1' and aa.BRIEF_ID='010' and aa.opt_status in ('18','19') and n.status in (95,99))
         or (aa.SEND_SIGN = '0' and aa.BRIEF_ID='010' and aa.opt_status='19' and n.status in (95,99)))  )

        or (f.SYS_VOUCHER_CODE='04' AND n.status = 95 and dtl.blend_sign = '1')    -- 付款方式为现付，审批通过，并且已勾兑
        or (((n.is_payed='0' AND exc.voucher_stat='0') OR (n.is_payed='1' AND n.status = 95)) AND dtl.blend_sign = '1' AND dtl.id is not null) -- 线上交易成功或线下审批通过，进行了付款勾兑
    )
  AND not exists(
    select 1 from DSL_INTEFACE_FLAG flag where flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'S')
                                           AND flag.TABLE_SOURCE = 'dsl_payment_result' AND flag.SYNC_STATUS = 'S')
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
       -- (select CONCAT_WS( '+',nm.op,nm.op_name) from nis_message nm left join nis_billhead nis on nm.BILL_NO = nis.id where nis.BILL_NO = n.bill_no and nm.MESSAGE_TYPE = '2' limit 1)
       (SELECT
            GROUP_CONCAT(
                    CONCAT(u.USER_CODE, '+', u.USER_NAME)
                    ORDER BY u.USER_CODE SEPARATOR '#'
            ) AS user_info
        FROM nis_corp_cashier c
                 JOIN nis_corp_cashier_user u ON c.ID = u.CORP_CASHIER_ID
        WHERE c.`STATUS` = 1 AND erp.corp_code = c.CORP_CODE
        GROUP BY c.CORP_CODE ) as paymentCashierUserNo,  -- 付款单记录提交人
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
where 1=1
  AND exc.voucher_stat = '7'
  AND not exists(
    select 1 from DSL_INTEFACE_FLAG flag where flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'E')
                                           AND flag.TABLE_SOURCE = 'dsl_payment_result' AND flag.SYNC_STATUS = 'S')
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
       -- (select CONCAT_WS( '+',nm.op,nm.op_name) from nis_message nm left join nis_billhead nis on nm.BILL_NO = nis.id where nis.BILL_NO = n.bill_no and nm.MESSAGE_TYPE = '2' limit 1)
       (SELECT
            GROUP_CONCAT(
                    CONCAT(u.USER_CODE, '+', u.USER_NAME)
                    ORDER BY u.USER_CODE SEPARATOR '#'
            ) AS user_info
        FROM nis_corp_cashier c
                 JOIN nis_corp_cashier_user u ON c.ID = u.CORP_CASHIER_ID
        WHERE c.`STATUS` = 1 AND erp.corp_code = c.CORP_CODE
        GROUP BY c.CORP_CODE ) as paymentCashierUserNo,  -- 付款单记录提交人
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
       dtl.ID                  as refundBankFlowNo,
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
         left join nis_bill_bank_serial_no_refund r on r.bill_id = n.id
         left join bis_acc_dtl dtl ON dtl.id = r.dtl_id
         left join nis_bill nb on nb.pay_id = n.id
         left join ab_accrecords aa on aa.id = nb.bill_id
         left join ab_voucherinfo av on av.id = aa.vid
where 1=1
  AND dtl.id is not null
  AND dtl.blend_sign = '1'
  AND exists(   -- 推送退汇状态前，必须先把付款成功的状态推送过去
    select 1 from DSL_INTEFACE_FLAG flag where flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'S')
                                           AND flag.TABLE_SOURCE = 'dsl_payment_result' AND flag.SYNC_STATUS = 'S')
  AND not exists(
    select 1 from DSL_INTEFACE_FLAG flag where flag.SOURCE_ID = CONCAT_WS( ';',n.bill_no,'R')
                                           AND flag.TABLE_SOURCE = 'dsl_payment_result' AND flag.SYNC_STATUS = 'S')
;


##修改人：fangcf
##修改内容：增加索引
##修改时间：2025-07-30
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'BIS_ACC_DTL' AND index_name = 'idx_bis_acc_dtl_trans_time_status') THEN
CREATE INDEX idx_bis_acc_dtl_trans_time_status ON BIS_ACC_DTL(trans_time, STATUS, CUR_ID, cd_sign);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;

DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'BIS_ACC_BAL' AND index_name = 'idx_bis_acc_bal_date_status_cur') THEN
CREATE INDEX idx_bis_acc_bal_date_status_cur ON BIS_ACC_BAL(bal_Date, STATUS, cur_id);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;

DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'BIS_ACC_BAL' AND index_name = 'idx_bis_acc_bal_date_corp_status_cur') THEN
CREATE INDEX idx_bis_acc_bal_date_corp_status_cur ON BIS_ACC_BAL(bal_Date, corp_id, STATUS, cur_id);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;

DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'BT_BANK_ACC' AND index_name = 'idx_bt_bank_acc_bank_acc_acc_type') THEN
CREATE INDEX idx_bt_bank_acc_bank_acc_acc_type ON BT_BANK_ACC(bank_acc, acc_type);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;

##修改人：fangcf
##修改内容：增加索引
##修改时间：2025-07-31
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'FC_ACC_BALANCE' AND index_name = 'idx_fc_acc_balance_acc_cur_date') THEN
CREATE INDEX idx_fc_acc_balance_acc_cur_date ON FC_ACC_BALANCE(account, CUR_ID, balance_date);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;

DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'FC_ACC' AND index_name = 'idx_fc_acc_acc_corp') THEN
CREATE INDEX idx_fc_acc_acc_corp ON FC_ACC(blnctr_acc, corp_Id);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;

##修改人：zhaohc
##修改内容：xxl-job优化
##修改时间：2025-07-30
CREATE TABLE IF NOT EXISTS `xxl_job_permission` (
    `id`              int UNSIGNED auto_increment NOT NULL COMMENT 'id',
    `job_id`          int(11) NOT NULL COMMENT '任务ID',
    `user_id`         int(11) NOT NULL COMMENT '用户ID',
    `permission_type` tinyint(4) NOT NULL COMMENT '权限类型：0-不可见 1-只读 2-读写',
    `create_by`       varchar(64)       DEFAULT '' COMMENT '创建者',
    `create_time`     datetime          DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64)       DEFAULT '' COMMENT '更新者',
    `update_time`     datetime          DEFAULT NULL COMMENT '更新时间',
    `is_deleted`      char(1)           DEFAULT '0' COMMENT '删除标志（0-存在 1-删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_job_user` (`job_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 操作日志表
CREATE TABLE IF NOT EXISTS `xxl_job_operate_log` (
    `id`           int UNSIGNED auto_increment NOT NULL COMMENT 'id',
    `operate_time` datetime    NOT NULL COMMENT '操作时间',
    `user_id`      int(11) NOT NULL COMMENT '用户ID',
    `username`     varchar(50) NOT NULL COMMENT '用户名',
    `job_id`       int(11) DEFAULT NULL COMMENT '任务ID',
    `operate_type` varchar(20) NOT NULL COMMENT '操作类型',
    `ip`           varchar(50) NOT NULL COMMENT 'IP',
    `description`  varchar(255) DEFAULT NULL COMMENT '描述',
    `create_by`       varchar(64)       DEFAULT '' COMMENT '创建者',
    `create_time`     datetime          DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64)       DEFAULT '' COMMENT '更新者',
    `update_time`     datetime          DEFAULT NULL COMMENT '更新时间',
    `is_deleted`      char(1)           DEFAULT '0' COMMENT '删除标志（0-存在 1-删除）',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 登录日志表
CREATE TABLE IF NOT EXISTS `xxl_job_login_log` (
    `id`         int UNSIGNED auto_increment NOT NULL COMMENT 'id',
    `login_time` datetime    NOT NULL COMMENT '登录时间',
    `user_id`    int(11) NOT NULL COMMENT '用户ID',
    `username`   varchar(50) NOT NULL COMMENT '用户名',
    `ip`         varchar(50) NOT NULL COMMENT 'IP',
    `status`     tinyint(4) NOT NULL COMMENT '0:失败 1:成功',
    `reason`      varchar(255)  DEFAULT NULL comment '原因说明',
    `create_by`       varchar(64)       DEFAULT '' COMMENT '创建者',
    `create_time`     datetime          DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64)       DEFAULT '' COMMENT '更新者',
    `update_time`     datetime          DEFAULT NULL COMMENT '更新时间',
    `is_deleted`      char(1)           DEFAULT '0' COMMENT '删除标志（0-存在 1-删除）',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



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


##修改日期：2025-8-20
##wade
##结算优化
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'bis_acc_dtl' AND index_name = 'idx_bis_acc_dtl_transtime_accname_oppaccname') THEN
CREATE INDEX idx_bis_acc_dtl_transtime_accname_oppaccname ON bis_acc_dtl (trans_time,acc_name,opp_acc_name,status,id);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;



DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'bis_acc_dtl' AND index_name = 'idx_nis_Billhead_transtime_oppaccname') THEN
CREATE INDEX idx_nis_Billhead_transtime_oppaccname ON bis_acc_dtl (trans_time,opp_acc_name);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;

DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'bis_acc_dtl' AND index_name = 'idx_nis_Billhead_transtime_accname') THEN
CREATE INDEX idx_nis_Billhead_transtime_accname ON bis_acc_dtl (trans_time,acc_name);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;


DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'nis_Billhead' AND index_name = 'idx_nis_Billhead_corpid_vouchertypeid_wishpayday_id') THEN
CREATE INDEX idx_nis_Billhead_corpid_vouchertypeid_wishpayday_id ON nis_Billhead (corp_id,wish_Payday,bus_type,voucher_Type_Id,id);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;


DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_INDEX()
BEGIN
IF NOT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'bis_exc' AND index_name = 'idx_bis_exc_cbsbillid_voucherfrom_transtime') THEN
CREATE INDEX idx_bis_exc_cbsbillid_voucherfrom_transtime ON bis_exc (voucher_From,cbs_bill_id,trans_time);
END IF;
 END$$
DELIMITER ;
CALL PRO_ADD_INDEX();
DROP PROCEDURE IF EXISTS PRO_ADD_INDEX;

##新增菜单“基础平台>>权限管理>>用户权限表”
INSERT INTO sys_menu (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`)
SELECT '0198c50a794c77a6b3a992d4f6c5ddaf', NULL, '用户权限表', '用户权限表', '49db09315da743d8a410bfcc86f7f01f', NULL, 80, 1, '/sys/user/list/permission', NULL, NULL, NULL, NULL, NULL, NULL, 0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = '0198c50a794c77a6b3a992d4f6c5ddaf' LIMIT 1
);
##GOGO

DROP PROCEDURE IF EXISTS ADD_COL;
DELIMITER $$
CREATE PROCEDURE ADD_COL()
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'sys_corp_user_resource'
          AND COLUMN_NAME = 'CREATE_TIME'
    ) THEN
ALTER TABLE sys_corp_user_resource
    ADD COLUMN CREATE_TIME DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';
END IF;
END$$

DELIMITER ;
-- 执行存储过程
CALL ADD_COL();
-- 删掉存储过程
DROP PROCEDURE IF EXISTS ADD_COL;

##修改人：fangcf
##修改内容：增加资源
##修改时间：2025-09-03

##修改内容：变更菜单资源权限地址
update sys_resources set url = '/chowmatistic/choproductreq/datatable' where id = 'c693f6032515480aa2c881bab6a9e798';

##增加资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select 'c77003c6c7c24c86b350fa9cd4cb19e3', '理财产品查询资源', '理财产品查询资源', 'cho:productRedeem:choProductRedeem:reqquery', 1, 'c244aac85ae54411bd285b4ae028ebb1', null, null, null, null, '/chowmatistic/choproductreq/listValidSign' from dual
where not exists (select 1 from sys_resources where PERMISSION ='cho:productRedeem:choProductRedeem:reqquery' limit 1);
commit;
##GOGO

-- 单位表增加报表是否展示字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'SYS_CORP' AND COLUMN_NAME = 'REPORT_DISPLAY') THEN
ALTER TABLE SYS_CORP ADD COLUMN REPORT_DISPLAY VARCHAR(2) NOT NULL DEFAULT '1' COMMENT '报表是否显示：1-是，0-否';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##修改人：zrs
##修改内容：新增多级资金日报表
##修改时间：2025-09-10
INSERT INTO sys_menu (ID, DESCRIPTION, NAME, PARENT_ID,  SORT, STATUS, URL, IS_MODULE, IS_EXTEND)
SELECT 'ccde2f9a8df711f0a544005056a84efa','多级资金日报表', '多级资金日报表', '07cbbb8132de427eb53c9d624fb7ee22',  32, 1, '/report/allocationDaily/multiLeveList', 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = 'ccde2f9a8df711f0a544005056a84efa' LIMIT 1
);
commit;
##GOGO

##修改人：zrs
##修改内容：新增多级资金日报表查询资源
##修改时间：2025-09-10
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'd0db8d3d8df711f0a544005056a84efa', '查询资源', '查询资源', 'netbank:nisAllocationDaily:multiLeve:query', 1, 'ccde2f9a8df711f0a544005056a84efa' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='d0db8d3d8df711f0a544005056a84efa' LIMIT 1
);
##GOGO


##修改人：zrs
##修改内容：新增多级资金收支表
##修改时间：2025-09-11
INSERT INTO sys_menu (ID, DESCRIPTION, NAME, PARENT_ID,  SORT, STATUS, URL, IS_MODULE, IS_EXTEND)
SELECT 'a6a67c588ed511f0a544005056a84efa','多级资金收支表', '多级资金收支表', '07cbbb8132de427eb53c9d624fb7ee22',  32, 1, '/report/incomeAndExpenditureOfFunds/multiLeveList', 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = 'a6a67c588ed511f0a544005056a84efa' LIMIT 1
);
commit;
##GOGO

##修改人：zrs
##修改内容：新增多级资金收支表查询资源
##修改时间：2025-09-11
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'ae0095858ed511f0a544005056a84efa', '查询资源', '查询资源', 'report:incomeAndExp:multiLeve:query', 1, 'a6a67c588ed511f0a544005056a84efa' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='ae0095858ed511f0a544005056a84efa' LIMIT 10.
);
##GOGO


##修改人：huao
##修改内容：新增银行授信年度计划维护页面及资源权限
##修改时间：2025-09-18
CREATE TABLE IF NOT EXISTS `year_bank_credit_plan` (
     `id` char(32) COLLATE utf8mb3_bin NOT NULL COMMENT '主键id',
     `corp_id` char(32) COLLATE utf8mb3_bin NOT NULL COMMENT '单位id',
     `year` int NOT NULL COMMENT '年份',
     `credit_type` varchar(12) COLLATE utf8mb3_bin NOT NULL COMMENT '授信类型',
     `cur_id` char(32) COLLATE utf8mb3_bin NOT NULL COMMENT '币别id',
     `credit_amount` decimal(18,2) NOT NULL COMMENT '计划授信金额',
     `bank_id` char(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '银行编号',
     `bank_code` char(12) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '银行类型',
     `bank_name` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
     `status` tinyint DEFAULT NULL COMMENT '是否支持使用 0-否 1-是',
     `create_by` char(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT '创建人id',
     `create_name` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL COMMENT '创建人名称',
     `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_by` char(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT '更新人id',
     `update_name` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL COMMENT '更新人名称',
     `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`),
     KEY `idx_corpId` (`corp_id`) USING BTREE,
     KEY `idx_curId` (`cur_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

INSERT INTO SYS_MENU (ID, DESCRIPTION, NAME, PARENT_ID,  SORT, STATUS, URL, IS_MODULE)
SELECT '32c9d5d7024942c9aded4ea123fffe05','银行授信年度计划维护', '银行授信年度计划维护', '5826504d04f448eea95c3a9ba881a1e6',  100, 1, '/cms/bank/credit/plan/list', 0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_MENU WHERE ID = '32c9d5d7024942c9aded4ea123fffe05' LIMIT 1
);

INSERT INTO SYS_RESOURCES (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
SELECT 'd7554f4edef943b7afcddfac801d405c', '银行授信年度计划查询', '银行授信年度计划查询', 'cms:bank:credit:plan:query', 1, '32c9d5d7024942c9aded4ea123fffe05' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID = 'd7554f4edef943b7afcddfac801d405c' LIMIT 1
);
INSERT INTO SYS_RESOURCES (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
SELECT 'bb21321c5b0e401c95fbcbf62a11d298', '银行授信年度计划新增', '银行授信年度计划新增', 'cms:bank:credit:plan:add', 1, '32c9d5d7024942c9aded4ea123fffe05' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID = 'bb21321c5b0e401c95fbcbf62a11d298' LIMIT 1
);
INSERT INTO SYS_RESOURCES (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
SELECT '67c24d9a1c1b4ea290f0830fea02f3e9', '银行授信年度计划编辑', '银行授信年度计划编辑', 'cms:bank:credit:plan:edit', 1, '32c9d5d7024942c9aded4ea123fffe05' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID = '67c24d9a1c1b4ea290f0830fea02f3e9' LIMIT 1
);
INSERT INTO SYS_RESOURCES (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
SELECT '56380f6456de4de0b5a47a975e924e5b', '银行授信年度计划删除', '银行授信年度计划删除', 'cms:bank:credit:plan:delete', 1, '32c9d5d7024942c9aded4ea123fffe05' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID = '56380f6456de4de0b5a47a975e924e5b' LIMIT 1
);
##GOGO


##修改人：huao
##修改内容：新增授信类型字典
##修改时间：2025-09-18
INSERT INTO SYS_DICT_GROUP (`ID`, `TYPE_GROUP_CODE`, `TYPE_GROUP_NAME`)
SELECT '685939e5d6cc4b4cabc71b0558a67cb2', 'cms.bank.credit.plan', '银行授信年度计划授信类型' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT_GROUP WHERE ID = '685939e5d6cc4b4cabc71b0558a67cb2' LIMIT 1
);
INSERT INTO SYS_DICT (`ID`, `TYPE_CODE`, `TYPE_NAME`, `TYPE_GROUP_ID`, `SORT`)
SELECT '29f9797fd5c541789b2755425afa5fe9', '1', '敞口', '685939e5d6cc4b4cabc71b0558a67cb2', 100 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT WHERE ID = '29f9797fd5c541789b2755425afa5fe9' LIMIT 1
);
INSERT INTO SYS_DICT (`ID`, `TYPE_CODE`, `TYPE_NAME`, `TYPE_GROUP_ID`, `SORT`)
SELECT '9f11f9d13f724a4a8264ac86af317400', '2', '低风险', '685939e5d6cc4b4cabc71b0558a67cb2', 200 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT WHERE ID = '9f11f9d13f724a4a8264ac86af317400' LIMIT 1
);
##GOGO

##修改人：huao
##修改内容：新增 信贷融资-查询-银行授信规划及月度跟踪表
##修改时间：2025-09-29
INSERT INTO SYS_MENU (ID, DESCRIPTION, NAME, PARENT_ID,  SORT, STATUS, URL, IS_MODULE)
SELECT 'd2496b34eb8344c5a299467194f2b2a0','银行授信规划及月度跟踪表', '银行授信规划及月度跟踪表', '8210f38c7eb045ba88578c3086ab434e',  200, 1, '/cms/bank/credit/plan/trace/list', 0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_MENU WHERE ID = 'd2496b34eb8344c5a299467194f2b2a0' LIMIT 1
);
##GOGO

##增加字段语句 NIS_BILLHEAD 添加 REFUND_ORG_ID
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'REFUND_ORG_ID') THEN
ALTER TABLE NIS_BILLHEAD ADD COLUMN REFUND_ORG_ID VARCHAR(32) NULL COMMENT '退款勾兑原单ID';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

## 增加BIP代发工资明细表
CREATE TABLE IF NOT EXISTS BIP_WAGE_DTL
(
    ID              varchar(32)      not null comment 'id' primary key,
    CORP_ID         varchar(32)      not null comment '单位ID',
    CORP_CODE       varchar(100)     not null comment '单位编码',
    WAGE_MONTH      varchar(8)       not null comment '工资发放月份：YYYYMM',
    WISH_DAY        date             not null comment '期望付款日期：YYYY-MM-DD',
    WAGE_DTL_NO     varchar(32)      not null comment '薪资批次号',
    WAGE_NAME       varchar(200)     not null comment '薪资发放标题',
    WAGE_TYPE       varchar(5)       not null comment '代发工资类型：1001：代发工资，1002：参基金',
    ROW_DTL_ID      varchar(32)      unique not null comment '明细行ID',
    WORK_NO         varchar(30)      null comment '员工编码',
    WORKER_TYPE     varchar(3)       not null comment '员工类型编码：11:普通员工 12：残疾人',
    WAGES           varchar(200)     not null comment '实发薪资',
    BANK_ACC        varchar(64)      not null comment '收款方账号',
    ACC_NAME        varchar(200)     not null comment '收款方户名',
    BANK_CODE       varchar(30)      not null comment '开户行号',
    BANK_NAME       varchar(200)     not null comment '开户行名',
    STATUS          tinyint(4)       not null COMMENT '状态',
    CREATE_DATE     datetime         null,
    CREATE_BY       varchar(32)      null,
    UPDATE_DATE     datetime         null,
    UPDATE_BY       varchar(32)      null
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='BIP代发工资明细表';
##GOGO

## 创建BIP单位映射表 BIP_CORP_MAPPING
CREATE TABLE IF NOT EXISTS BIP_CORP_MAPPING
(
    ID                  varchar(32)      not null comment 'id' primary key,
    CONTRACT_NAME       varchar(300)     not null comment '薪酬单位名称',
    CONTRACT_NUMBER     varchar(50)      not null comment '薪酬单位编码',
    COMPANY_NO          varchar(50)      not null comment '资金系统单位编码',
    COMPANY_NAME        varchar(300)     not null comment '资金系统单位名称'
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='BIP单位映射表';
##GOGO




##修改人员：xiay
##修改内容：新增定时任务【冻结申请无冻结金额提醒】
##修改日期 2025-02-17
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '冻结申请无冻结金额提醒', '2025-10-12 14:06:46', '2025-10-12 14:06:46', 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalNoFreezeNoticeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-10-12 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='冻结申请无冻结金额提醒' limit 1);
commit;
##GOGO

##修改人员:xiay
##修改内容:增加消息提醒【冻结申请无资金可冻结提醒】
##修改日期:2025-02-18
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT '99690ac57ee4bab8f72144d9cb14bd84', 'e583ddaa25ed45098c672e99bf0c961a', '冻结申请无冻结金额提醒', 'netbank:bankacc:nofreeze:notice', '5046a942b5634db0b6eee4f58a532a6b', null, null, null, null, 21,
		'该笔账户冻结申请无资金可冻结，请及时补款。\n计划冻结单号：$!{planFreezeNo}\n单位：$!{corpName}\n冻结账号：$!{bankAcc}\n冻结账户开户行：$!{bankName}\n申请冻结金额：$!{applyFreezeMoney}\n已冻结金额：$!{realFreezeMoney}\n未冻结金额：$!{subFreezeMoney}', 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '99690ac57ee4bab8f72144d9cb14bd84' LIMIT 1
);
##GOGO

## 创建关联方维护表
CREATE TABLE IF NOT EXISTS NIS_RELATED_PARTY
(
    ID                  varchar(32)      not null comment 'id' primary key,
    RELATED_CODE        varchar(300)     not null comment '关联方编码',
    RELATED_NAME        varchar(50)      not null comment '关联方名称',
    STATUS              tinyint(4)       not null COMMENT '状态',
    CREATE_DATE         datetime         null,
    CREATE_BY           varchar(32)      null,
    UPDATE_DATE         datetime         null,
    UPDATE_BY           varchar(32)      null
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='关联方维护表';
##GOGO

##修改人：zrs
##修改内容：新增关联方维护菜单
##修改时间：2025-10-17
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'fadd19c5aafa11f08424005056a84efa', '33', '关联方维护', '关联方维护', 'd61f91e27ed9473ab9e8183343ea4507', null, 31, 1, '/netbank/relatedParty/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = 'fadd19c5aafa11f08424005056a84efa' LIMIT 1
);
commit;
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2253807eaafb11f08424005056a84efa', 'zh_CN', '关联方编码【{0}】已存在！', 'netbank.message.relatedparty.unique' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'netbank.message.relatedparty.unique' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改内容：新增关联方收付报表
INSERT INTO sys_menu (ID, DESCRIPTION, NAME, PARENT_ID,  SORT, STATUS, URL, IS_MODULE, IS_EXTEND)
SELECT '406b4781ab2d11f08424005056a84efa','关联方收付报表', '关联方收付报表', '07cbbb8132de427eb53c9d624fb7ee22',  32, 1, '/netbank/relatedParty/reportList', 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = '406b4781ab2d11f08424005056a84efa' LIMIT 1
);
##GOGO

##修改内容：新增关联方收付报表查询资源
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '4a7b8887ab2d11f08424005056a84efa', '查询资源', '查询资源', 'report:netbank:relatedParty:query', 1, '406b4781ab2d11f08424005056a84efa' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='4a7b8887ab2d11f08424005056a84efa' LIMIT 1
);
##GOGO

##修改人员：fangcf
##修改内容：新增定时任务【自动新增票据计划冻结申请】
##修改日期 2025-10-22
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '自动新增票据计划冻结申请', '2025-02-12 14:06:46', '2025-02-12 14:06:46', 'XXL', '', 'CRON', '0 0 2 ? * THU', 'DO_NOTHING', 'ROUND', 'AutoBillBalFreezeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-02-12 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='自动新增票据计划冻结申请' limit 1);
commit;
##GOGO

##修改人员：fangcf
##修改内容：新增表【单位代码与登录账号关联表】
##修改日期 2025-10-24
CREATE TABLE IF NOT EXISTS SYS_CORP_TO_LOGIN_NAME
(
   ID           VARCHAR(32) NOT NULL COMMENT '主键',
   CODE			VARCHAR(32) NOT NULL COMMENT '单位代码',
   LOGIN_NAME   VARCHAR(100) NOT NULL COMMENT '登录账号',
   CREATE_TIME  DATETIME DEFAULT NULL COMMENT '创建时间',
   UPDATE_TIME  DATETIME DEFAULT NULL COMMENT '修改时间',
   IS_DELETED   VARCHAR(2) DEFAULT NULL COMMENT '是否删除',
   CONSTRAINT PK_SYS_CORP_TO_LOGIN_NAME PRIMARY KEY (ID)
);
ALTER TABLE SYS_CORP_TO_LOGIN_NAME COMMENT '单位代码与登录账号关联表';
##GOGO

##修改人员：fangcf
##修改内容：新增配置：冻结开始时间
##修改日期 2025-10-24
INSERT INTO SYS_CONFIG (ID, TYPE, NAME, VALUE)
SELECT 'a8292d4f12944a618b133a064e1498e6', 'nis', 'freezeStartDate', '11:00:00'  FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_CONFIG WHERE ID = 'a8292d4f12944a618b133a064e1498e6' LIMIT 1
);
##GOGO

##修改内容：新增配置：冻结结束时间
INSERT INTO SYS_CONFIG (ID, TYPE, NAME, VALUE)
SELECT 'b6e93bca24654781bfad7caa2c3f8667', 'nis', 'freezeEndDate', '16:00:00'  FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_CONFIG WHERE ID = 'b6e93bca24654781bfad7caa2c3f8667' LIMIT 1
);
##GOGO


##修改人员：xiay
##修改内容：理财到期提前调整
##修改日期 2025-11-10
update sys_notification_category set CONTENT = '您有一笔$!{productCode}金额为$!{buyMoney}的理财产品即将到期！\\n 银行账号:$!{bankAcc}\\n理财主体:$!{corpName} \\n 银行类型：$!{bankTypeName}\\n到期日：$dateTag.date2StrCNY($!{deadlineDate})\\n起始合同利率：$!{startContractRate}\\n结束合同利率：$!{endContractRate}\\n金额为$!{buyMoney}\\n 银行账号:$!{bankAcc}' where id = 'bbe7a391d9cc4501a73c5cfa6708e9d6';
##GOGO


##修改人：fangcf
##修改内容：代发工资浮动比率配置
##修改时间：2025-11-20
CREATE TABLE IF NOT EXISTS NIS_WAGE_FLOAT_RATIO_CONFIG
(
   ID                   	VARCHAR(32) NOT NULL COMMENT '主键',
   MONTH					VARCHAR(10) NOT NULL COMMENT '月份',
   FLOAT_RATE       		DECIMAL(10,8) NOT NULL COMMENT '浮动比率',
   IS_DELETED				VARCHAR(2) DEFAULT '0' COMMENT '是否已删除：1-是，0-否',
   CREATE_BY            	VARCHAR(32) COMMENT '创建人',
   CREATE_TIME         		DATETIME COMMENT '创建时间',
   UPDATE_BY            	VARCHAR(32) COMMENT '修改人',
   UPDATE_TIME         		DATETIME COMMENT '修改时间'
);
ALTER TABLE NIS_WAGE_FLOAT_RATIO_CONFIG COMMENT '代发工资浮动比率配置';
##GOGO

##修改人：fangcf
##修改内容：新增代发工资浮动比率配置菜单
##修改时间：2025-11-20
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'c9c04af5af62425e84d8145d2deac29f', null, '代发工资浮动比率配置', '代发工资浮动比率配置', 'd61f91e27ed9473ab9e8183343ea4507', null, 33, 1, '/wage/wageFloatRatio/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'c9c04af5af62425e84d8145d2deac29f' LIMIT 1
    );
commit;
##GOGO

##修改人：fangcf
##修改内容：新增代发工资浮动比率配置查询资源
##修改时间：2025-11-20
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'f803f66c39de41999b2a2ee948a275ee', '查询资源', '查询资源', 'netbank:wage:floatRatio:query', 1, 'c9c04af5af62425e84d8145d2deac29f' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='f803f66c39de41999b2a2ee948a275ee' LIMIT 1
);
##GOGO

##修改人：fangcf
##修改内容：新增代发工资浮动比率配置应用资源
##修改时间：2025-11-20
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '3432815cecb74e5f8ee69b1666c1430d', '应用资源', '应用资源', 'netbank:wage:floatRatio:apply', 1, 'c9c04af5af62425e84d8145d2deac29f' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='3432815cecb74e5f8ee69b1666c1430d' LIMIT 1
);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-11-20
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '889dc7c866ae4f63b3c79aeb0c16e279', 'zh_CN', '月份不能为空', 'nis.wage.message.month.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'nis.wage.message.month.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-11-20
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd04915dee67b42c79e48ca1a729f8b76', 'zh_CN', '浮动比率不能为空', 'nis.wage.message.floatrate.null' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'nis.wage.message.floatrate.null' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人员:fangcf
##修改内容:增加国际化
##修改日期:2025-11-20
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0e4084eb597043469e60af37e3dcaeb3', 'zh_CN', '代发工资浮动比率在相同月份下必须唯一', 'nis.wage.message.month.not.unique' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'nis.wage.message.month.not.unique' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##【结算日志操作类型】增加字典值
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '442056a7808f4a1198c241ae4712ab2a', '42', '新增', '9f2b62cd98f94a859a0b909df334f14b', 42 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '442056a7808f4a1198c241ae4712ab2a' LIMIT 1
    );
##GOGO

##【结算日志操作类型】增加字典值
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '86cc85d8a3814b1388ff77269fb0bd09', '43', '修改', '9f2b62cd98f94a859a0b909df334f14b', 43 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '86cc85d8a3814b1388ff77269fb0bd09' LIMIT 1
    );
##GOGO



##修改人：huao
##修改内容：新增代发工资下拨金额配置页面、资源权限、数据字典
##修改时间：2025-11-21
CREATE TABLE IF NOT EXISTS `nis_wage_release_config` (
       `id` char(32) COLLATE utf8mb3_bin NOT NULL COMMENT '主键id',
       `corp_id` char(32) COLLATE utf8mb3_bin NOT NULL COMMENT '单位id',
       `bank_acc` varchar(60) COLLATE utf8mb3_bin NOT NULL COMMENT '银行账号',
       `bank_acc_name` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT '银行账号名称',
       `bank_code` char(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '开户行行号',
       `bank_name` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT '开户行银行名称',
       `release_date` date NOT NULL COMMENT '下拨日期',
       `release_amount` decimal(18,2) NOT NULL COMMENT '本月下拨金额',
       `last_month_release_amount` decimal(18,2) NOT NULL COMMENT '上月下拨金额',
       `total_release_money` decimal(18,2) DEFAULT NULL COMMENT '本月单据累计下拨金额',
       `status` int NOT NULL COMMENT '状态 0-暂存 1-已确认',
       `confirm_id` char(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '确认人id',
       `confirm_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '确认人名称',
       `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
       `generate_require` tinyint DEFAULT NULL COMMENT '是否生成需求清单 0-否 1-是',
       `generate_time` datetime DEFAULT NULL COMMENT '需求清单生成时间',
       `is_deleted`	VARCHAR(2) DEFAULT NULL COMMENT '',
       `create_by` char(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT '创建人',
       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
       `update_by` char(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT '更新人',
       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
       PRIMARY KEY (`id`),
       KEY `idx_corp` (`corp_id`) USING BTREE,
       KEY `idx_bank_acc` (`bank_acc`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='代发工资下拨金额配置表';

##菜单及资源权限
INSERT INTO SYS_MENU (ID, DESCRIPTION, NAME, PARENT_ID,  SORT, STATUS, URL, IS_MODULE)
SELECT 'a163dfcb023043c3805b208f1ce507eb','代发工资下拨金额配置', '代发工资下拨金额配置', 'd61f91e27ed9473ab9e8183343ea4507',  1000, 1, '/wage/wageReleaseConfig/list', 0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_MENU WHERE ID = 'a163dfcb023043c3805b208f1ce507eb' LIMIT 1
);

INSERT INTO SYS_RESOURCES (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
SELECT '3868403d3b304795a8f5cec8c700b13d', '代发工资下拨金额配置查询', '代发工资下拨金额配置查询', 'wage:release:config:query', 1, 'a163dfcb023043c3805b208f1ce507eb' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID = '3868403d3b304795a8f5cec8c700b13d' LIMIT 1
);
INSERT INTO SYS_RESOURCES (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
SELECT '237325636e0e4d48962fb3d7dda6d121', '代发工资下拨金额配置新增', '代发工资下拨金额配置新增', 'wage:release:config:add', 1, 'a163dfcb023043c3805b208f1ce507eb' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID = '237325636e0e4d48962fb3d7dda6d121' LIMIT 1
);
INSERT INTO SYS_RESOURCES (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
SELECT 'd279ddf714c94e4381717cf4aff60d00', '代发工资下拨金额配置编辑', '代发工资下拨金额配置编辑', 'wage:release:config:edit', 1, 'a163dfcb023043c3805b208f1ce507eb' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID = 'd279ddf714c94e4381717cf4aff60d00' LIMIT 1
);
INSERT INTO SYS_RESOURCES (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
SELECT 'e9aad20c566a414f995df1a2178f57c5', '代发工资下拨金额配置删除', '代发工资下拨金额配置删除', 'wage:release:config:delete', 1, 'a163dfcb023043c3805b208f1ce507eb' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID = 'e9aad20c566a414f995df1a2178f57c5' LIMIT 1
);


##数据字典
INSERT INTO SYS_DICT_GROUP (`ID`, `TYPE_GROUP_CODE`, `TYPE_GROUP_NAME`)
SELECT 'edd063ca1ded481b9266b7db67460b43', 'nis.wage.release.status', '代发工资下拨金额配置状态' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT_GROUP WHERE ID = 'edd063ca1ded481b9266b7db67460b43' LIMIT 1
);
INSERT INTO SYS_DICT (`ID`, `TYPE_CODE`, `TYPE_NAME`, `TYPE_GROUP_ID`, `SORT`)
SELECT 'bb140fcd7df5491baf90b3847c42485f', '0', '暂存', 'edd063ca1ded481b9266b7db67460b43', 100 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT WHERE ID = 'bb140fcd7df5491baf90b3847c42485f' LIMIT 1
);
INSERT INTO SYS_DICT (`ID`, `TYPE_CODE`, `TYPE_NAME`, `TYPE_GROUP_ID`, `SORT`)
SELECT '54becf3677ea40f9a38924478e54d3d2', '1', '已确认', 'edd063ca1ded481b9266b7db67460b43', 200 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT WHERE ID = '54becf3677ea40f9a38924478e54d3d2' LIMIT 1
);


##数据字典
INSERT INTO SYS_DICT_GROUP (`ID`, `TYPE_GROUP_CODE`, `TYPE_GROUP_NAME`)
SELECT 'd985429810db4686b45ad9d7d274da90', 'nis.wage.release.type', '代发工资下拨金额批量设置类型' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT_GROUP WHERE ID = 'd985429810db4686b45ad9d7d274da90' LIMIT 1
);
INSERT INTO SYS_DICT (`ID`, `TYPE_CODE`, `TYPE_NAME`, `TYPE_GROUP_ID`, `SORT`)
SELECT '8aabf9e1229c499cb2fb486875a52a8b', '0', '按金额', 'd985429810db4686b45ad9d7d274da90', 100 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT WHERE ID = '8aabf9e1229c499cb2fb486875a52a8b' LIMIT 1
);
INSERT INTO SYS_DICT (`ID`, `TYPE_CODE`, `TYPE_NAME`, `TYPE_GROUP_ID`, `SORT`)
SELECT 'b4e4bcaa106541fc81225f7dec606608', '1', '按比率', 'd985429810db4686b45ad9d7d274da90', 200 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_DICT WHERE ID = 'b4e4bcaa106541fc81225f7dec606608' LIMIT 1
);
##GOGO

##修改日期：2025-11-23
##修改人员：zrs
-- 修改nis_wage_release_config表中generate_require默认值为0
ALTER TABLE nis_wage_release_config MODIFY COLUMN generate_require tinyint(1) DEFAULT 0 COMMENT '是否生成需求清单 0-否 1-是';
-- 增加工资生成需求清单日期，方便测试
DELETE FROM sys_config WHERE id = '15ff866297844a9d94f79177c7fdc6dc';
INSERT INTO `sys_config` (`ID`, `TYPE`, `NAME`, `VALUE`, `CHANGE`)
VALUES ('15ff866297844a9d94f79177c7fdc6dc', 'wage', 'demandDate', '15', NULL);

## 工资配置表自动生成需求清单
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '代发工资配置表自动生成需求清单', null, null, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryConfigGenerateDemandJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='代发工资配置表自动生成需求清单' limit 1);
commit;
##GOGO

## 工资配置表自动生成需求清单
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '代发工资单据自动生成需求清单', null, null, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryTableGenerateDemandJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='代发工资单据自动生成需求清单' limit 1);
commit;
##GOGO

## 代发工资配置表自动汇总需求清单
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '代发工资配置表自动汇总需求清单', null, null, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryConfigSummaryDemandJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='代发工资配置表自动汇总需求清单' limit 1);
commit;
##GOGO

## 代发工资单据自动汇总需求清单
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '代发工资单据自动汇总需求清单', null, null, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryTableSummaryDemandJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='代发工资单据自动汇总需求清单' limit 1);
commit;
##GOGO

##修改人员：fangcf
##修改内容：新增定时任务【代发下拨金额自动同步】
##修改日期 2025-11-24
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '代发下拨金额自动同步', '2025-11-20 14:06:46', '2025-11-20 14:06:46', 'XXL', '', 'CRON', '0 0 2 1 * ?', 'DO_NOTHING', 'ROUND', 'AutoSyncWageAllocationJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-11-20 14:06:46', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='代发下拨金额自动同步' limit 1);
commit;
##GOGO

##修改人员：fangcf
##修改内容：新增字典值
##修改日期 2025-11-24
##【资金需求类型】增加字典值
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '635f62b650274bbb9253de128e665c5d', '04', '代发工资配置表',null,'32d5e07c4efd4d8b9c14c3a3a1f7d77f',4 from dual
where not exists (select 1 from sys_dict where ID ='635f62b650274bbb9253de128e665c5d' limit 1);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '003dea90c1c441afa0f3e737c41ca03d', '05', '代发工资单据表',null,'32d5e07c4efd4d8b9c14c3a3a1f7d77f',5 from dual
where not exists (select 1 from sys_dict where ID ='003dea90c1c441afa0f3e737c41ca03d' limit 1);
##GOGO