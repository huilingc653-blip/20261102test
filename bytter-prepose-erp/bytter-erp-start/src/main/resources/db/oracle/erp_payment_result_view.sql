CREATE OR REPLACE VIEW erp_payment_result_view AS
SELECT n.id AS "serialNoErp", -- 流水号
       n.bill_no AS "voucherNoErp", -- 凭证号
       n.tenant_id AS "tenantId", n.payer_acc AS "payerAccNo", -- 付款账号
       n.payer_acc_name AS "payerBankName", -- 付款户名
       n.payer_bank AS "payerAccName", -- 付款银行
       n.pay_money AS "amt", -- 金额
       n.payee_acc AS "payeeAccNo", -- 收款帐号
       n.payee_acc_name AS "payeeAccName", -- 收款户名
       n.payee_bank AS "payeeBank", -- 收款银行
       CASE WHEN n.status = -2 THEN '-1'
            WHEN b.voucher_stat = '0' OR b.voucher_stat = '2' THEN b.voucher_stat
            WHEN n.is_payed = '1' AND n.status > '94' THEN '0'
            ELSE '3' END AS "voucherStat", -- 付款状态 -1删除 0付款成功 2付款失败 3付款待处理
       CASE WHEN n.status = -2 THEN TO_CHAR(n.update_date, 'YYYY-MM-DD HH24:MI:SS')
            WHEN b.voucher_stat = '0' OR b.voucher_stat = '2' THEN TO_CHAR(b.return_time, 'YYYY-MM-DD HH24:MI:SS')
            ELSE TO_CHAR(n.create_date, 'YYYY-MM-DD HH24:MI:SS') END AS "transTime", -- 交易日期
       CASE WHEN n.status = -2 THEN NVL(bt.bus_type_name, '单据已删除')
            WHEN b.voucher_stat = '0' OR b.voucher_stat = '2' THEN b.next_return_msg
            WHEN n.is_payed = '1' AND n.status > '94' THEN '已经付款完成'
            ELSE '付款待处理' END AS "returnMsg" -- 交易消息
FROM nis_billhead n
INNER JOIN erp_payinfo e ON e.id = n.erp_id
LEFT JOIN bt_charge_back_type bt ON bt.bus_type = e.bus_type
LEFT JOIN bis_exc b ON n.bill_no = b.cbs_bill_no
WHERE NVL(n.erp_id, ' ') <> ' ' AND n.create_date > SYSDATE - 365;
