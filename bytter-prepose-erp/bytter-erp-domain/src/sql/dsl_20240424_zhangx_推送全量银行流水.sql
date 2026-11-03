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
       dtl.BANK_SERIAL_NO AS YHLSH,
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