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
       dtl.bank_serial_no      as flowNo,
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