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
  AND v.START_DATE > '2024-07-19'
  AND a.brief_id IN('001','6001','9010','6004','9003','6120','004','010')
  AND IFNULL(ef.SYNC_STATUS, 'F') <> 'S';