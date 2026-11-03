##修改日期：2024-04-26
##修改人员：zhangx
##修改内容：大参林电子回单查询视图
create or replace view DSL_BIS_BANK_RECEIPT_DTL_VIEW as
SELECT
		b.id AS id,
		d.id AS serialId,
		pay.SERIAL_NO_ERP AS serialNoErp,
		b.RECEIPT_NO AS receiptNo,
		d.BANK_SERIAL_NO AS transSerialNo,
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