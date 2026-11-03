
CREATE OR REPLACE VIEW erp_nis_wage_result_view AS
SELECT
    ndtl.wage_dtl_no AS WAGE_DTL_NO, -- 明细唯一健
    wa.wage_no AS WAGE_NO,-- 批次号
    wa.tenant_id as tenantId,
    ndtl.work_no AS work_no, -- 员工工号
    wa.payer_acc AS PAYER_ACC_NO,-- 付款账号
    v.ACC_NAME AS PAYER_ACC_NAME,-- 付款户名
    v.bank_name AS PAYER_BANK_NAME,-- 付款银行
    ndtl.wages_verify AS AMT,-- 金额
    ndtl.bank_acc AS PAYEE_ACC_NO, -- 收款帐号
    ndtl.user_name AS PAYEE_ACC_NAME,-- 收款户名
    ndtl.bank_name AS PAYEE_BANK_NAME,-- 收款银行
    CASE WHEN wa.status=-2 THEN '-1'
         WHEN ndtl.voucher_stat='0' OR ndtl.voucher_stat='2' THEN ndtl.voucher_stat
         ELSE '3' END AS VOUCHER_STAT,-- 付款状态 -1删除 0付款成功 2付款失败 3付款待处理
    CASE WHEN wa.status=-2  THEN DATE_FORMAT(wa.wage_day,'%Y-%m-%d')
         WHEN ndtl.voucher_stat='0' OR ndtl.voucher_stat='2' THEN DATE_FORMAT(b.trans_time,'%Y-%m-%d')
         ELSE DATE_FORMAT(wa.wage_day,'%Y-%m-%d') END AS trans_time,-- 交易日期
    CASE WHEN wa.status=-2 THEN '单据已删除'
         WHEN ndtl.voucher_stat='0' OR ndtl.voucher_stat='2' THEN ndtl.next_return_msg
         ELSE '付款待处理' END AS RETURN_MSG -- 交易消息
FROM nis_wage_detail ndtl
         inner JOIN nis_wage wa ON ndtl.wage_id=wa.id
         LEFT JOIN v_acc_list v ON wa.payer_acc=v.BANK_ACC
         LEFT JOIN  nis_billhead a ON a.voucher_no = ndtl.voucher_no
         LEFT JOIN bis_exc b ON b.voucher_no = ndtl.voucher_no
WHERE  COALESCE(wa.erp_id, '') != ''  AND wa.create_date>SUBDATE(SYSDATE(),365)
;
