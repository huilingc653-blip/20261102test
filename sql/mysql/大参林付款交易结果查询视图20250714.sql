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
