-- ============================================================================
-- SaaS 标准化初始化脚本
-- 数据库：Oracle
-- 阶段：05 函数与视图
-- 来源：5.Init-functions-views.sql
-- 生成日期：2026-07-29
-- 注意：本文件由原始脚本整理生成；禁止反向覆盖 sql/saas/oracle。
-- ============================================================================

--------------------------------------------------------
--  DDL for Function BT_ABS
--------------------------------------------------------


-- 函数说明：BT_ABS
CREATE OR REPLACE FUNCTION BT_ABS (in_str1 in number) return number is
  vn_result number;
begin
  vn_result := abs(in_str1);
return(vn_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_ASCII
--------------------------------------------------------


-- 函数说明：BT_ASCII
CREATE OR REPLACE FUNCTION BT_ASCII (in_str1 in varchar2) return int is
  vn_Result int;
begin
  vn_Result := ascii(in_str1);
return(vn_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_CEIL
--------------------------------------------------------


-- 函数说明：BT_CEIL
CREATE OR REPLACE FUNCTION BT_CEIL (in_str1 in number) return number is
  vn_result number;
begin
  vn_result := ceil(in_str1);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_CHAR
--------------------------------------------------------


-- 函数说明：BT_CHAR
CREATE OR REPLACE FUNCTION BT_CHAR (in_str1 in int) return varchar2 is
  vc_Result varchar2(100);
begin
  vc_Result := chr(in_str1);
return(vc_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT
--------------------------------------------------------


-- 函数说明：BT_CONCAT
CREATE OR REPLACE FUNCTION BT_CONCAT (in_str1 in varchar2, in_str2 in varchar2) return varchar2 is
  vc_Result varchar2(8000);
begin
  vc_Result := in_str1 || in_str2;
return(vc_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT10
--------------------------------------------------------


-- 函数说明：BT_CONCAT10
CREATE OR REPLACE FUNCTION BT_CONCAT10 (in_str1 in varchar2,
                                      in_str2 in varchar2,
                                      in_str3 in varchar2,
                                      in_str4 in varchar2,
                                      in_str5 in varchar2,
                                      in_str6 in varchar2,
                                      in_str7 in varchar2,
                                      in_str8 in varchar2,
                                      in_str9 in varchar2,
                                      in_str10 in varchar2 ) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := in_str1 || in_str2||in_str3||in_str4||in_str5||in_str6||in_str7||in_str8||in_str9||in_str10;
return(vc_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT2
--------------------------------------------------------


-- 函数说明：BT_CONCAT2
CREATE OR REPLACE FUNCTION BT_CONCAT2 (in_str1 in varchar2, in_str2 in varchar2) return varchar2 is
  vc_Result varchar2(8000);
begin
  vc_Result := in_str1 || in_str2;
return(vc_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT3
--------------------------------------------------------


-- 函数说明：BT_CONCAT3
CREATE OR REPLACE FUNCTION BT_CONCAT3 (in_str1 in varchar2,
                                      in_str2 in varchar2,
                                      in_str3 in varchar2 ) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := in_str1 || in_str2||in_str3;
return(vc_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT4
--------------------------------------------------------


-- 函数说明：BT_CONCAT4
CREATE OR REPLACE FUNCTION BT_CONCAT4 (in_str1 in varchar2,
                                      in_str2 in varchar2,
                                      in_str3 in varchar2,
                                      in_str4 in varchar2 ) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := in_str1 || in_str2||in_str3||in_str4;
return(vc_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT5
--------------------------------------------------------


-- 函数说明：BT_CONCAT5
CREATE OR REPLACE FUNCTION BT_CONCAT5 (in_str1 in varchar2,
                                      in_str2 in varchar2,
                                      in_str3 in varchar2,
                                      in_str4 in varchar2,
                                      in_str5 in varchar2 ) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := in_str1 || in_str2||in_str3||in_str4||in_str5;
return(vc_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT6
--------------------------------------------------------


-- 函数说明：BT_CONCAT6
CREATE OR REPLACE FUNCTION BT_CONCAT6 (in_str1 in varchar2,
                                      in_str2 in varchar2,
                                      in_str3 in varchar2,
                                      in_str4 in varchar2,
                                      in_str5 in varchar2,
                                      in_str6 in varchar2 ) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := in_str1 || in_str2||in_str3||in_str4||in_str5||in_str6;
return(vc_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT7
--------------------------------------------------------


-- 函数说明：BT_CONCAT7
CREATE OR REPLACE FUNCTION BT_CONCAT7 (in_str1 in varchar2,
                                      in_str2 in varchar2,
                                      in_str3 in varchar2,
                                      in_str4 in varchar2,
                                      in_str5 in varchar2,
                                      in_str6 in varchar2,
                                      in_str7 in varchar2 ) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := in_str1 || in_str2||in_str3||in_str4||in_str5||in_str6||in_str7;
return(vc_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT8
--------------------------------------------------------


-- 函数说明：BT_CONCAT8
CREATE OR REPLACE FUNCTION BT_CONCAT8 (in_str1 in varchar2,
                                      in_str2 in varchar2,
                                      in_str3 in varchar2,
                                      in_str4 in varchar2,
                                      in_str5 in varchar2,
                                      in_str6 in varchar2,
                                      in_str7 in varchar2,
                                      in_str8 in varchar2 ) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := in_str1 || in_str2||in_str3||in_str4||in_str5||in_str6||in_str7||in_str8;
return(vc_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_CONCAT9
--------------------------------------------------------


-- 函数说明：BT_CONCAT9
CREATE OR REPLACE FUNCTION BT_CONCAT9 (in_str1 in varchar2,
                                      in_str2 in varchar2,
                                      in_str3 in varchar2,
                                      in_str4 in varchar2,
                                      in_str5 in varchar2,
                                      in_str6 in varchar2,
                                      in_str7 in varchar2,
                                      in_str8 in varchar2,
                                      in_str9 in varchar2 ) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := in_str1 || in_str2||in_str3||in_str4||in_str5||in_str6||in_str7||in_str8||in_str9;
return(vc_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_FLOOR
--------------------------------------------------------


-- 函数说明：BT_FLOOR
CREATE OR REPLACE FUNCTION BT_FLOOR (in_str1 in number) return number is
  vn_result number;
begin
  vn_result := floor(in_str1);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_INITCAP
--------------------------------------------------------


-- 函数说明：BT_INITCAP
CREATE OR REPLACE FUNCTION BT_INITCAP (in_str1 in varchar2) return varchar2 is
  vc_Result varchar2(8000);
begin
  vc_Result := INITCAP(in_str1);
return(vc_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_INSTR
--------------------------------------------------------


-- 函数说明：BT_INSTR
CREATE OR REPLACE FUNCTION BT_INSTR (in_str1 in varchar2,
                                    in_str2 in varchar2) return int is
  vn_result varchar2(8000);
begin
  vn_result := instr(in_str1,in_str2);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_LENGTH
--------------------------------------------------------


-- 函数说明：BT_LENGTH
CREATE OR REPLACE FUNCTION BT_LENGTH (in_str1 in varchar2) return int is
  vn_result varchar2(8000);
begin
  vn_result := length(in_str1);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_LOG
--------------------------------------------------------


-- 函数说明：BT_LOG
CREATE OR REPLACE FUNCTION BT_LOG (in_str1 in number, IN_str2  in  number) return number is
  vn_result number;
begin
  vn_result := log(in_str1,in_str2);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_LOWER
--------------------------------------------------------


-- 函数说明：BT_LOWER
CREATE OR REPLACE FUNCTION BT_LOWER (in_str1 in varchar2) return varchar2 is
  vc_Result varchar2(8000);
begin
  vc_Result := LOWER(in_str1);
return(vc_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_LTRIM
--------------------------------------------------------


-- 函数说明：BT_LTRIM
CREATE OR REPLACE FUNCTION BT_LTRIM (in_str1 in varchar2) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := ltrim(in_str1);
return(vc_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_NVL
--------------------------------------------------------


-- 函数说明：BT_NVL
CREATE OR REPLACE FUNCTION BT_NVL (in_str1 in varchar2, in_str2 in varchar2) return varchar2 is
  vc_Result varchar2(8000);
begin
  vc_Result := nvl(in_str1,in_str2);
return(vc_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_NVL_CHAR
--------------------------------------------------------


-- 函数说明：BT_NVL_CHAR
CREATE OR REPLACE FUNCTION BT_NVL_CHAR (in_str1 in varchar2, in_str2 in varchar2) return varchar2 is
  vc_Result varchar2(8000);
begin
  vc_Result := nvl(in_str1,in_str2);
return(vc_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_NVL_DATE
--------------------------------------------------------


-- 函数说明：BT_NVL_DATE
CREATE OR REPLACE FUNCTION BT_NVL_DATE (in_str1 in varchar2, in_str2 in date) return date is
  vd_Result date;
begin
  vd_Result := nvl(in_str1,in_str2);
return(vd_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_NVL_NUM
--------------------------------------------------------


-- 函数说明：BT_NVL_NUM
CREATE OR REPLACE FUNCTION BT_NVL_NUM (in_str1 in varchar2, in_str2 in number) return number is
  vn_Result number;
begin
  vn_Result := nvl(in_str1,in_str2);
return(vn_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_POWER
--------------------------------------------------------


-- 函数说明：BT_POWER
CREATE OR REPLACE FUNCTION BT_POWER (in_str1 in number, IN_str2  in  number) return number is
  vn_result number;
begin
  vn_result := POWER(in_str1,in_str2);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_REPLACE
--------------------------------------------------------


-- 函数说明：BT_REPLACE
CREATE OR REPLACE FUNCTION BT_REPLACE (in_str1 in varchar2,
                                      in_str2 in varchar2,
                                      in_str3 varchar2) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := replace(in_str1,in_str2, in_str3);
return(vc_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_ROUND
--------------------------------------------------------


-- 函数说明：BT_ROUND
CREATE OR REPLACE FUNCTION BT_ROUND (in_str1 in number, IN_str2  in  number) return number is
  vn_result number;
begin
  vn_result := ROUND(in_str1,in_str2);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_RTRIM
--------------------------------------------------------


-- 函数说明：BT_RTRIM
CREATE OR REPLACE FUNCTION BT_RTRIM (in_str1 in varchar2) return varchar2 is
  vc_result varchar2(8000);
begin
  vc_result := rtrim(in_str1);
return(vc_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_SIGN
--------------------------------------------------------


-- 函数说明：BT_SIGN
CREATE OR REPLACE FUNCTION BT_SIGN (in_str1 in number) return int is
  vn_result int;
begin
  vn_result := sign(in_str1);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_SQRT
--------------------------------------------------------


-- 函数说明：BT_SQRT
CREATE OR REPLACE FUNCTION BT_SQRT (in_str1 in number) return number is
  vn_result number;
begin
  vn_result := sqrt(in_str1);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_SUBSTR
--------------------------------------------------------


-- 函数说明：BT_SUBSTR
CREATE OR REPLACE FUNCTION BT_SUBSTR (in_str1 in varchar2,
                                     in_str2 in int,
                                     in_str3 int) return varchar2 is
  vc_result varchar2(8000);
begin
  if in_str3 = -1 then
    vc_result := substr(in_str1,in_str2);
else
    vc_result := substr(in_str1,in_str2, in_str3);
end if;
return(vc_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_SYSDATE
--------------------------------------------------------


-- 函数说明：BT_SYSDATE
CREATE OR REPLACE FUNCTION BT_SYSDATE return date is
  vd_result date;
begin
select sysdate into vd_result from dual;
return(vd_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_DATEDIFF
--------------------------------------------------------


-- 函数说明：BT_DATEDIFF
CREATE OR REPLACE FUNCTION BT_DATEDIFF(date_val IN DATE) RETURN NUMBER
IS
BEGIN
    IF date_val IS NULL THEN
        RETURN NULL;
END IF;
RETURN TRUNC(date_val) - TRUNC(SYSDATE);
END ;

/
--------------------------------------------------------
--  DDL for Function BT_DATEDIFF2
--------------------------------------------------------


-- 函数说明：BT_DATEDIFF2
CREATE OR REPLACE FUNCTION BT_DATEDIFF2(date1 IN DATE, date2 IN DATE) RETURN NUMBER
IS
BEGIN
    -- 如果任一参数为 NULL，返回 NULL
    IF date1 IS NULL OR date2 IS NULL THEN
        RETURN NULL;
END IF;
    -- 返回天数差（截断时间部分，仅比较日期）
RETURN TRUNC(date1) - TRUNC(date2);
END;
/
--------------------------------------------------------
--  DDL for Function BT_TO_CHAR
--------------------------------------------------------


-- 函数说明：BT_TO_CHAR
CREATE OR REPLACE FUNCTION BT_TO_CHAR (in_str1 in date, in_str2 in varchar2) return varchar2 is
  vc_Result varchar2(50);
begin
  vc_Result := to_char(in_str1,in_str2);
return(vc_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_TO_DATE
--------------------------------------------------------


-- 函数说明：BT_TO_DATE
CREATE OR REPLACE FUNCTION BT_TO_DATE (in_str1 in varchar2, in_str2 in varchar2) return date is
  vc_Result date;
begin
  vc_Result := to_date(in_str1,in_str2);
return(vc_Result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_TRUNC
--------------------------------------------------------


-- 函数说明：BT_TRUNC
CREATE OR REPLACE FUNCTION BT_TRUNC (in_str1 in number) return number is
  vn_result number;
begin
  vn_result := trunc(in_str1);
return(vn_result);
end ;

/
--------------------------------------------------------
--  DDL for Function BT_TRUNC_DATE
--------------------------------------------------------


-- 函数说明：BT_TRUNC_DATE
CREATE OR REPLACE FUNCTION BT_TRUNC_DATE(in_str1 in date) return date is
  vn_result date;
begin
  vn_result := trunc(in_str1);
return(vn_result);
end;

/
--------------------------------------------------------
--  DDL for Function BT_TRUNC_NUM
--------------------------------------------------------


-- 函数说明：BT_TRUNC_NUM
CREATE OR REPLACE FUNCTION BT_TRUNC_NUM(in_str1 in number) return number is
  vn_result number;
begin
  vn_result := trunc(in_str1);
return(vn_result);
end;

/

--------------------------------------------------------
--  DDL for Function BT_UPPER
--------------------------------------------------------


-- 函数说明：BT_UPPER
CREATE OR REPLACE FUNCTION BT_UPPER (in_str1 in varchar2) return varchar2 is
  vc_Result varchar2(8000);
begin
  vc_Result := UPPER(in_str1);
return(vc_Result);
end ;

/

--------------------------------------------------------
--  DDL for Function NEWID
--------------------------------------------------------


-- 函数说明：NEWID
CREATE OR REPLACE FUNCTION NEWID return varchar is
  vc_result varchar2(32);
begin
  vc_result := sys_guid();
return(vc_result);
end ;

/


-- 1. DSL_AB_BILL_SHARE_SYN_VIEW


-- 视图说明：DSL_AB_BILL_SHARE_SYN_VIEW
CREATE OR REPLACE VIEW DSL_AB_BILL_SHARE_SYN_VIEW AS
SELECT a.ID                                                              AS id,
       a.TENANT_ID                                                       AS TENANT_ID,
       CASE WHEN sc.CODE = '100' THEN '100000' ELSE sc.CODE END          AS companyCode,
       sc.NAME                                                           AS companyName,
       vv.BANK_ACC                                                       AS ticketUnitAccount,
       vv.TYPE_NAME                                                      AS ticketUnitBankCategory,
       vv.VALID_SIGN                                                     AS isEnable,
       v.ORIG_NO                                                         AS docNo,
       '01'                                                              AS issuanceFlag,
       CASE WHEN v.BLGRDBAGCRCL_PRMT_IND = '1' THEN '0' ELSE '1' END     AS allowSubpackage,
       v.SBBLL_RNG_STRT_SN                                               AS subTicketStartNo,
       v.SBBLL_RNG_END_SN                                                AS subTicketEndNo,
       vv.BATCH_NO                                                       AS issuanceBatchNo,
       CASE
           WHEN a.BRIEF_ID IN ('001','003') THEN '1'
           WHEN a.BRIEF_ID IN ('6001','9003','9010') THEN '11'
           WHEN a.BRIEF_ID = '010' THEN '17'
           WHEN a.BRIEF_ID = '6120' THEN '10'
           WHEN a.BRIEF_ID = '004' THEN '36'
           END                                                               AS businessType,
       sd.TYPE_CODE                                                      AS ticketSource,
       CASE
           WHEN v.BILL_FLAG = 10 THEN '2'
           WHEN v.BILL_FLAG = 11 THEN '1'
           END                                                               AS circulationMedium,
       CASE
           WHEN v.BILL_TYPE = 10 THEN '1'
           WHEN v.BILL_TYPE = 11 THEN '2'
           END                                                               AS ticketType,
       v.BILL_MONEY                                                      AS faceValue,
       a.CREATE_BY                                                       AS applicant,
       a.APPLY_DATE                                                      AS applyDate,
       a.BUSINESS_DATE                                                   AS businessTime,
       v.START_DATE                                                      AS issuanceTime,
       v.END_DATE                                                        AS maturityTime,
       d.INTEREST_RATES                                                  AS discountRate,
       d.INTEREST                                                        AS discountInterest,
       v.PAYER                                                           AS drawer,
       v.PAYER_BANK_NAME                                                 AS issuingBank,
       v.PAYERACC                                                        AS drawerAccount,
       v.PAYERBANKNO                                                     AS drawerBankCode,
       v.PAYEE                                                           AS payee,
       v.PAYEEACC                                                        AS payeeAccount,
       v.PAYEE_BANK_NAME                                                 AS payeeBank,
       v.PAYEEBANKNO                                                     AS payeeBankCode,
       v.ACCEPTOR                                                        AS acceptor,
       v.ACCEPTORACC                                                     AS acceptorAccount,
       v.ACCEPTOR_BANK_NAME                                              AS acceptorBank,
       v.ACCEPTORBANKNO                                                  AS acceptorBankCode,
       vv.BAIL_ACC                                                       AS marginAccount,
       acc.ACC_NAME                                                      AS marginAccountName,
       acc.BANK_NAME                                                     AS marginBankName,
       sc2.NAME                                                          AS marginUnitName,
       NVL(vv.BAIL_MONEY, 0)                                             AS marginAmount,
       vv.BAIL_PERCENT                                                   AS marginRatio,
       vv.BAIL_RATE                                                      AS marginRate,
       '01'                                                              AS marginType,
       CASE
           WHEN a.BRIEF_ID = '6001' THEN a.CN_ACCRECORDS
           WHEN bs.BUS_TYPE IN ('6001','9002','9011','9017','9018','9020','9023') THEN a.REC_NM
           END                                                               AS previousParty,
       CASE
           WHEN bs.BUS_TYPE IN ('001','017','002','003','010','018','019','020','011','012','022','6201','6203','6004','023')
               THEN a.REC_NM
           END                                                               AS nextParty,
       CASE
           WHEN a.SEND_SIGN = '0' THEN '2'
           WHEN a.SEND_SIGN = '1' THEN '1'
           END                                                               AS onlineOfflineFlag,
       '3'                                                               AS businessStatus,
       CASE WHEN a.OPT_STATUS = '19' THEN '9' END                        AS instructionStatus,
       CASE
           WHEN vv.TYPE = '0' THEN 'YF'
           WHEN vv.TYPE = '1' THEN 'YS'
           END                                                               AS receivablePayableFlag,
       v.BILL_STATUS                                                     AS ticketStatus,
       v.EBILL_BL_CRCL_INDC                                              AS circulationStatus,
       (v.BILL_MONEY - NVL(vv.BAIL_MONEY,0))                             AS maturityPaymentAmount
FROM ab_accrecords a
         LEFT JOIN ab_voucherinfo v ON a.VID = v.ID
         LEFT JOIN ab_voucherinfo_vice vv ON a.VVID = vv.ID AND a.TENANT_ID = vv.TENANT_ID
         LEFT JOIN sys_corp sc ON sc.ID = vv.CORP_ID AND a.TENANT_ID = sc.TENANT_ID
         LEFT JOIN ab_bill_source_dtl sd ON sd.BRIEF_ID = a.BRIEF_ID
    AND ((vv.SOURCE = sd.SOURCE)
        OR (vv.SOURCE IS NULL AND a.BILL_SOURCE = sd.SOURCE)
        OR (vv.SOURCE IS NULL AND a.BILL_SOURCE IS NULL AND sd.SOURCE IS NULL))
    AND a.TENANT_ID = sd.TENANT_ID
         LEFT JOIN ab_discount d ON d.RECORD_ID = a.ID AND a.TENANT_ID = d.TENANT_ID
         LEFT JOIN bt_bank_acc acc ON acc.BANK_ACC = vv.BAIL_ACC AND a.TENANT_ID = acc.TENANT_ID
         LEFT JOIN sys_corp sc2 ON sc2.ID = acc.CORP_ID AND a.TENANT_ID = sc2.TENANT_ID
         LEFT JOIN ab_brief_status bs ON bs.STATUS = a.BRIEF_ID AND a.TENANT_ID = bs.TENANT_ID
         LEFT JOIN dsl_inteface_flag ef ON ef.SOURCE_ID = a.ID AND ef.TABLE_SOURCE = 'DSL_AB_BILL_SHARE_SYN_VIEW' AND a.TENANT_ID = ef.TENANT_ID
WHERE 1 = 1
  AND a.STATUS IN ('95','96','100')
  AND a.OPT_STATUS = '19'
  AND v.ORIG_NO IS NOT NULL
  AND v.ORIG_NO <> ''
  AND ((a.BRIEF_ID = '001' AND a.SEND_SIGN = '0')
    OR (a.BRIEF_ID = '003' AND a.SEND_SIGN = '1' AND a.STATUS >= '95' AND a.OPT_STATUS = '19')
    OR (a.BRIEF_ID IN ('6001','9003','9010') AND vv.OLD_DRAFT_ID IS NULL)
    OR a.BRIEF_ID IN ('010','6120','004'))
  AND NVL(ef.SYNC_STATUS, 'F') <> 'S'
  AND a.BUSINESS_DATE >= TO_DATE('2025-02-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS');
/

-- 2. DSL_AB_BILL_VIEW


-- 视图说明：DSL_AB_BILL_VIEW
CREATE OR REPLACE VIEW DSL_AB_BILL_VIEW AS
SELECT a.ID                                                         AS FMS_ID,
       a.TENANT_ID                                                  AS TENANT_ID,
       'M'                                                          AS RECEIPT_TYPE,
       'BFS'                                                        AS RECEIPT_SOURCE,
       v.ORIG_NO                                                    AS RECEIPT_NUM,
       v.SBBLL_RNG_STRT_SN                                          AS SUB_RECEIPT_FROM,
       v.SBBLL_RNG_END_SN                                           AS SUB_RECEIPT_TO,
       CASE WHEN vv.TYPE = '0' THEN 'AP' ELSE 'AR' END              AS RECEIPT_PROPERTY,
       CASE
           WHEN v.BILL_TYPE = 10 AND v.BILL_FLAG = 10 THEN '银行承兑汇票-纸质'
           WHEN v.BILL_TYPE = 10 AND v.BILL_FLAG = 11 THEN '银行承兑汇票-电子'
           WHEN v.BILL_TYPE = 11 AND v.BILL_FLAG = 10 THEN '商业承兑汇票'
           WHEN v.BILL_TYPE = 11 AND v.BILL_FLAG = 11 THEN '商业承兑汇票-电子'
           END                                                          AS RECEIPT_NAME,
       v.BILL_MONEY                                                 AS RECEIPT_COST,
       'CNY'                                                        AS CURRENCY_CODE,
       CASE
           WHEN a.BRIEF_ID = '002' AND acc.BANK_TYPE_ID = '0e5a53a00fcf467e9fd42125614a9232'
               THEN a.BUSINESS_DATE
           ELSE v.START_DATE
           END                                                          AS CREATE_RECEIPT_DATE,
       v.END_DATE                                                   AS RECEIPT_DUE_DATE,
       v.PAYER                                                      AS CREATE_RECEIPT_NAME,
       v.PAYERACC                                                   AS CREATE_REC_ACCOUNT,
       v.PAYER_BANK_NAME                                            AS CREATE_REC_BANK,
       v.PAYEE                                                      AS PAYEE_NAME,
       v.PAYEEACC                                                   AS PAYEE_ACCOUNT,
       v.PAYEE_BANK_NAME                                            AS PAYEE_BANK,
       TO_CHAR(vv.BAIL_PERCENT * 100) || '%'                        AS CASH_DEPOSIT_SCALE,
       vv.BAIL_MONEY                                                AS CASH_DEPOSIT_COST,
       CASE
           WHEN a.BRIEF_ID = '010' THEN n.PURPOSE
           WHEN a.BRIEF_ID = '9010' THEN '原出票人：' || v.PAYER
           ELSE vv.RMK
           END                                                          AS COMMT,
       CASE WHEN a.BRIEF_ID = '010' THEN 'Y' ELSE 'N' END           AS ENDORSE_FLAG,
       CASE WHEN a.BRIEF_ID = '010' THEN a.REC_NM ELSE NULL END     AS ENDORSER,
       CASE WHEN a.BRIEF_ID = '010' THEN a.CREATE_DATE ELSE NULL END AS ENDOSE_DATE,
       (v.BILL_MONEY - NVL(vv.BAIL_MONEY,0))                        AS ACTUAL_COST,
       CASE
           WHEN a.BRIEF_ID = '6001' THEN '11'
           WHEN a.BRIEF_ID = '9010' THEN '12'
           WHEN a.BRIEF_ID = '6004' THEN '13'
           WHEN a.BRIEF_ID = '9003' THEN '26'
           WHEN a.BRIEF_ID = '6120' THEN '28'
           WHEN a.BRIEF_ID = '004' AND v.BILL_STATUS <> 'CS05' THEN '29'
           END                                                          AS ACTION_TYPE,
       a.BUSINESS_DATE                                              AS CASHING_DATE,
       CASE
           WHEN a.BRIEF_ID = '010' AND nb.ID IS NOT NULL THEN su.USERNAME
           WHEN a.BRIEF_ID = '002' THEN v.CREATE_NAME
           ELSE NVL(vv.CREATE_NAME, a.CREATE_NAME)
           END                                                          AS MAKER,
       'R'                                                          AS PUSH_STATUS_FLG,
       'R'                                                          AS PROCESSED_FLAG,
       CASE
           WHEN a.BRIEF_ID IN ('9003','9010','6001') THEN '11'
           WHEN a.BRIEF_ID = '010' THEN '12'
           WHEN a.BRIEF_ID = '6004' THEN '13'
           WHEN a.BRIEF_ID IN ('001','002') THEN '27'
           WHEN a.BRIEF_ID = '004' AND v.BILL_STATUS <> 'CS05' THEN '29'
           END                                                          AS STATUS,
       CASE WHEN a.BRIEF_ID IN ('9010','010') THEN v.BILL_MONEY ELSE NULL END AS ENDOSE_AMOUNT,
       CASE WHEN a.BRIEF_ID = '6120' THEN a.BUSINESS_DATE ELSE NULL END AS RECEIPT_CASH_DATE,
       CASE WHEN sc.CODE = '100' THEN '100000' ELSE sc.CODE END      AS attribute1,
       NULL                                                         AS attribute2,
       NULL                                                         AS attribute3,
       NULL                                                         AS attribute4,
       NULL                                                         AS attribute5
FROM ab_accrecords a
         LEFT JOIN ab_voucherinfo v ON a.VID = v.ID AND a.TENANT_ID = v.TENANT_ID
         LEFT JOIN ab_voucherinfo_vice vv ON a.VVID = vv.ID AND a.TENANT_ID = vv.TENANT_ID
         LEFT JOIN sys_corp sc ON sc.ID = vv.CORP_ID AND a.TENANT_ID = sc.TENANT_ID
         LEFT JOIN dsl_inteface_flag ef ON ef.SOURCE_ID = a.ID AND ef.TABLE_SOURCE = 'DSL_AB_BILL_VIEW' AND a.TENANT_ID = ef.TENANT_ID
         LEFT JOIN nis_bill nb ON nb.BILL_ID = a.ID AND a.TENANT_ID = nb.TENANT_ID
         LEFT JOIN nis_billhead n ON nb.PAY_ID = n.ID AND a.TENANT_ID = n.TENANT_ID
         LEFT JOIN sys_user su ON su.LOGIN_NAME = n.CREATE_BY AND a.TENANT_ID = su.TENANT_ID
         LEFT JOIN bt_bank_acc acc ON acc.BANK_ACC = v.PAYERACC AND a.TENANT_ID = acc.TENANT_ID
WHERE 1 = 1
  AND a.BRIEF_ID NOT IN ('010','9010','6001','9003','011')
  AND ((a.BRIEF_ID IN ('9010','010') AND a.OPT_STATUS IN ('18','19'))
    OR (a.BRIEF_ID NOT IN ('9010','010') AND a.OPT_STATUS = '19'))
  AND a.STATUS IN ('95','96')
  AND ((a.BRIEF_ID NOT IN ('9010','010') AND a.OLD_ACC_ID IS NULL) OR a.BRIEF_ID IN ('9010','010'))
  AND ((a.BRIEF_ID NOT IN ('9010','010') AND vv.OLD_DRAFT_ID IS NULL) OR a.BRIEF_ID IN ('9010','010'))
  AND v.ORIG_NO IS NOT NULL AND v.ORIG_NO <> ''
  AND ((vv.TYPE = '1' AND a.BUSINESS_DATE > TO_DATE('2024-08-31','YYYY-MM-DD') AND a.APPLY_DATE > TO_DATE('2024-08-31','YYYY-MM-DD'))
    OR (vv.TYPE <> '1' AND a.BUSINESS_DATE > TO_DATE('2024-08-09','YYYY-MM-DD') AND a.APPLY_DATE > TO_DATE('2024-08-09','YYYY-MM-DD')))
  AND ((a.BRIEF_ID IN ('6001','9003','9010') AND vv.OLD_DRAFT_ID IS NULL)
    OR a.BRIEF_ID IN ('002','6004','010','011')
    OR (a.BRIEF_ID = '004' AND v.BILL_STATUS <> 'CS05')
    OR (a.BRIEF_ID = '001' AND a.SEND_SIGN = '0'))
  AND NVL(ef.SYNC_STATUS,'F') <> 'S'
ORDER BY a.CREATE_DATE;
/

-- 3. DSL_AB_EMIT_BAIL_INTEREST_SYNC_VIEW


-- 视图说明：DSL_AB_BAIL_INTEREST_SYNC_VIEW
CREATE OR REPLACE VIEW DSL_AB_BAIL_INTEREST_SYNC_VIEW AS
SELECT ae.ID AS orderId,
       ae.TENANT_ID AS TENANT_ID,
       CASE WHEN sc.CODE = '100' THEN '100000' ELSE sc.CODE END AS companyCode,
       sc.NAME AS companyName,
       ae.BANK_ACC AS receiptAccount,
       ae.INTEREST_MARGIN AS marginAmount,
       TO_CHAR(TO_DATE(ae.CURRENT_MONTH || '-01', 'YYYY-MM-DD'), 'YYYY-MM-DD') AS accountingTime
FROM ab_emit_bail_interest_info ae
         LEFT JOIN sys_corp sc ON ae.CORP_ID = sc.ID AND ae.TENANT_ID = sc.TENANT_ID
WHERE ae.STATUS = '1' AND ae.PUSH_STATUS = '1';
/

-- 4. DSL_ALL_BILL_VIEW


-- 视图说明：DSL_ALL_BILL_VIEW
CREATE OR REPLACE VIEW DSL_ALL_BILL_VIEW AS
SELECT n.ID AS id,
       n.TENANT_ID AS TENANT_ID,
       n.BILL_NO AS originalBillNo,
       n.BILL_NO AS bill_no,
       n.STATUS AS status,
       n.ERP_ID AS erp_id,
       n.WISH_PAYDAY AS wish_payday,
       n.PAY_MONEY AS pay_money,
       n.PAYER_ACC AS payer_acc,
       n.CUR_ID AS cur_id,
       n.CREATE_BY AS create_by,
       n.IS_PAYED AS is_payed,
       n.BANK_SERIAL_NO AS bank_serial_no,
       n.PURPOSE AS purpose,
       n.VOUCHER_TYPE_ID AS voucher_type_id
FROM nis_billhead n
         LEFT JOIN nis_billhead n2 ON n2.SPLIT_ID = n.ID AND n.TENANT_ID = n2.TENANT_ID
         LEFT JOIN nis_billhead n3 ON n.MERGE_ID = n3.ID AND n.TENANT_ID = n3.TENANT_ID
WHERE 1 = 1
  AND n.ORIG_SPLIT_ID IS NULL
  AND n2.ID IS NULL
  AND n3.ID IS NULL
  AND n.ERP_ID IS NOT NULL
UNION ALL
SELECT n2.ID AS id,
       n.TENANT_ID AS TENANT_ID,
       n.BILL_NO AS originalBillNo,
       n2.BILL_NO AS bill_no,
       n2.STATUS AS status,
       n.ERP_ID AS erp_id,
       n2.WISH_PAYDAY AS wish_payday,
       n2.PAY_MONEY AS pay_money,
       n2.PAYER_ACC AS payer_acc,
       n2.CUR_ID AS cur_id,
       n2.CREATE_BY AS create_by,
       n2.IS_PAYED AS is_payed,
       n2.BANK_SERIAL_NO AS bank_serial_no,
       n2.PURPOSE AS purpose,
       n2.VOUCHER_TYPE_ID AS voucher_type_id
FROM nis_billhead n
         LEFT JOIN nis_billhead n2 ON n2.ORIG_SPLIT_ID = n.ID AND n.TENANT_ID = n2.TENANT_ID
WHERE 1 = 1
  AND n2.ID IS NOT NULL
  AND n2.SPLIT_ID IS NOT NULL
  AND n.STATUS = -2
  AND NOT EXISTS (SELECT 1 FROM nis_billhead t WHERE t.SPLIT_ID = n2.ID)
  AND n.ERP_ID IS NOT NULL;
/

-- 5. DSL_BANK_ACC_INFO_VIEW


-- 视图说明：DSL_BANK_ACC_INFO_VIEW
CREATE OR REPLACE VIEW DSL_BANK_ACC_INFO_VIEW AS
SELECT a.ID AS BANK_ACC_ID,
       a.TENANT_ID AS TENANT_ID,
       a.BANK_ACC AS BANK_ACC,
       CASE WHEN s.CODE = '100' THEN '100000' ELSE s.CODE END AS CORP_CODE,
       a.ACC_NAME AS BANK_ACC_NAME,
       c.cur AS CUR,
       a.ELECTRIC_BILL AS IS_BILL_ACC,
       a.CIRCULATION_MODE AS CIRCULATION_MODE,
       CASE a.IS_DOMESTIC_BANK WHEN '0' THEN '1' WHEN '1' THEN '0' END AS AREA,
       d1.name AS ACC_TYPE,
       n.NATURE_NAME AS ACC_NATURE,
       n.NATURE_CODE AS ACC_NATURE_CODE,
       att.ATTRIBUTE_NAME AS ACC_ATTRIBUTE,
       att.ATTRIBUTE_CODE AS ACC_ATTRIBUTE_CODE,
       a.BANK_CODE AS BANK_CODE,
       a.BANK_NAME AS BANK_NAME,
       t.BANK_TYPE AS BANK_TYPE_CODE,
       t.TYPE_NAME AS BANK_TYPE,
       r1.NAME AS PROV,
       r2.NAME AS CITY,
       a.RATES AS rates,
       TO_CHAR(a.REG_DATE, 'YYYY-MM-DD') AS REG_DATE,
       TO_CHAR(a.UPDATE_DATE, 'YYYY-MM-DD HH24:MI:SS') AS UPDATE_DATE,
       TO_CHAR(a.CLOSE_DATE, 'YYYY-MM-DD') AS CLOSE_DATE,
       CASE WHEN a.STATUS = -2 THEN '0' ELSE '1' END AS STATUS,
       TO_CHAR(a.CHECK_EXPIRE_DATE, 'YYYY-MM-DD') AS CHECK_EXPIRE_DATE,
       a.CREATE_BY AS CREATE_BY,
       a.VALID_SIGN AS VALID_SIGN,
       a.IS_ONLINE AS IS_ONLINE,
       a.IS_CAPITAL_POOL AS IS_CAPITAL_POOL,
       a.RMK AS rmk
FROM bt_bank_acc a
         LEFT JOIN (SELECT bc.BANK_ACC_ID, LISTAGG(c.ENGLISH_CODE, ',') WITHIN GROUP (ORDER BY c.ENGLISH_CODE) AS cur
                    FROM bt_bank_acc_cur bc
                        LEFT JOIN bt_currency c ON c.ID = bc.CUR_ID AND bc.TENANT_ID = c.TENANT_ID
                    GROUP BY bc.BANK_ACC_ID) c ON c.BANK_ACC_ID = a.ID
         LEFT JOIN sys_corp s ON s.ID = a.CORP_ID AND a.TENANT_ID = s.TENANT_ID
         LEFT JOIN bis_acc_attribute att ON a.ATTRIBUTE_ID = att.ID AND a.TENANT_ID = att.TENANT_ID
         LEFT JOIN bt_acc_nature n ON a.NATURE_ID = n.ID AND a.TENANT_ID = n.TENANT_ID
         LEFT JOIN bt_bank_type t ON t.ID = a.BANK_TYPE_ID AND a.TENANT_ID = t.TENANT_ID
         LEFT JOIN sys_region r1 ON r1.ID = a.PROV AND a.TENANT_ID = r1.TENANT_ID
         LEFT JOIN sys_region r2 ON r2.ID = a.CITY AND a.TENANT_ID = r2.TENANT_ID
         LEFT JOIN (SELECT d.TYPE_CODE AS code, d.TYPE_NAME AS name
                    FROM sys_dict_group g
                             JOIN sys_dict d ON g.ID = d.TYPE_GROUP_ID AND g.TENANT_ID = d.TENANT_ID
                    WHERE g.TYPE_GROUP_CODE = 'btBankAcc.accType') d1 ON d1.code = a.ACC_TYPE;
/

-- 6. DSL_BIS_ACC_DTL_VIEW


-- 视图说明：DSL_BIS_ACC_DTL_VIEW
CREATE OR REPLACE VIEW DSL_BIS_ACC_DTL_VIEW AS
SELECT dtl.ID AS SEQ_ID,
       dtl.TENANT_ID AS TENANT_ID,
       '01' AS DJZT_CODE,
       '正常' AS DJZT,
       dtl.ID AS YHLSH,
       CASE WHEN bc.CODE = '100' THEN '100000' ELSE bc.CODE END AS DWBH,
       bc.NAME AS DWMC,
       CASE WHEN dtl.CD_SIGN = '1' THEN '02' ELSE '01' END AS DJLX_CODE,
       CASE WHEN dtl.CD_SIGN = '1' THEN '付款' ELSE '收款' END AS DJLX_DESC,
       CASE
           WHEN bi.BIF_CODE = '9302' THEN dtl.USES
           ELSE (NVL(dtl.REMARK,'') || '&' || NVL(dtl.USES,'') || '&' || NVL(dtl.POSTSCRIPT,''))
           END AS YT,
       dtl.BANK_ACC AS ZHBH,
       dtl.ACC_NAME AS ZHMC,
       acc.BANK_CODE AS LHH,
       dtl.BANK_NAME AS KHH,
       dtl.AMT AS JE,
       dtl.OPP_ACC_NO AS DFZH,
       dtl.OPP_ACC_NAME AS DFHM,
       dtl.OPP_ACC_BANK AS DFKHH,
       TO_CHAR(dtl.TRANS_TIME, 'YYYYMMDD') AS JYRQ,
       TO_CHAR(dtl.TRANS_TIME, 'HH24MISS') AS JYSJ,
       '01' AS SJLY_CODE,
       '银企直联获取' AS SJLY_DESC,
       'POST' AS ACTION_TYPE,
       nb.BURSAR_BILL_NO AS SOURCE_NUM
FROM bis_acc_dtl dtl
         LEFT JOIN nis_bill_bank_serial_no ns ON dtl.ID = ns.DTL_ID
         LEFT JOIN nis_billhead nb ON nb.ID = ns.BILL_ID AND dtl.TENANT_ID = nb.TENANT_ID
         LEFT JOIN bt_bank_acc acc ON dtl.BANK_ACC = acc.BANK_ACC AND dtl.TENANT_ID = acc.TENANT_ID
         LEFT JOIN sys_corp bc ON bc.ID = acc.CORP_ID AND bc.TENANT_ID = dtl.TENANT_ID
         LEFT JOIN bis_bif_init bi ON bi.ID = dtl.BIF_CODE AND bi.TENANT_ID = dtl.TENANT_ID
WHERE 1 = 1
  AND ((dtl.TRANS_TIME > TO_DATE('2024-07-20','YYYY-MM-DD') AND dtl.BIF_CODE <> 'aef6125b6ea2497db16038e7f9e087e7')
    OR (dtl.TRANS_TIME > TO_DATE('2024-07-24','YYYY-MM-DD') AND dtl.BIF_CODE = 'aef6125b6ea2497db16038e7f9e087e7'))
  AND (NVL(dtl.DTL_SYNC_STATUS,'F') <> 'S' OR NVL(nb.BURSAR_BILL_NO,'') <> NVL(dtl.SOURCE_NUM,''));
/

-- 7. DSL_BIS_BANK_RECEIPT_DTL_VIEW


-- 视图说明：DSL_BIS_BANK_RECEIPT_DTL_VIEW
CREATE OR REPLACE VIEW DSL_BIS_BANK_RECEIPT_DTL_VIEW AS
SELECT b.ID AS id,
       b.TENANT_ID AS TENANT_ID,
       d.ID AS serialId,
       pay.SERIAL_NO_ERP AS serialNoErp,
       b.RECEIPT_NO AS receiptNo,
       d.ID AS transSerialNo,
       CASE WHEN b.CD_SIGN = '1' THEN '02' ELSE '01' END AS fileType,
       'http://ten-dsl-cw-zijin-cdn.dslbuy.com/' || b.FILE_PATH AS filePath,
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
         LEFT JOIN bt_bank_acc acc ON acc.BANK_ACC = b.PAY_ACC_NO AND b.TENANT_ID = acc.TENANT_ID
         LEFT JOIN bt_bank_acc acc2 ON acc2.BANK_ACC = b.PAYEE_ACC_NO AND b.TENANT_ID = acc2.TENANT_ID
         LEFT JOIN bt_currency bc ON b.CUR_ID = bc.ID AND b.TENANT_ID = bc.TENANT_ID
         LEFT JOIN bis_receipt_dtl brd ON brd.RECEIPT_ID = b.ID AND b.TENANT_ID = brd.TENANT_ID
         LEFT JOIN bis_acc_dtl d ON d.ID = brd.DTL_ID AND d.CD_SIGN = b.CD_SIGN AND d.BLEND_SIGN = '1' AND d.TENANT_ID = b.TENANT_ID
         LEFT JOIN nis_bill_bank_serial_no nb ON nb.DTL_ID = d.ID AND b.TENANT_ID = nb.TENANT_ID
         LEFT JOIN nis_billhead n ON n.ID = nb.BILL_ID AND b.TENANT_ID = n.TENANT_ID
         LEFT JOIN erp_payinfo pay ON n.ERP_ID = pay.ID AND b.TENANT_ID = pay.TENANT_ID;
/

-- 8. DSL_BT_INPUT_BANK_INFO_VIEW


-- 视图说明：DSL_BT_INPUT_BANK_INFO_VIEW
CREATE OR REPLACE VIEW DSL_BT_INPUT_BANK_INFO_VIEW AS
SELECT b.SYS_BANK_CODE AS SYS_BANK_CODE,
       b.TENANT_ID AS TENANT_ID,
       b.BANK_NAME AS BANK_NAME,
       b.UPDATE_DATE AS UPDATE_DATE,
       b.CREATE_DATE AS CREATE_DATE,
       CASE WHEN t.BANK_TYPE IS NULL THEN 'DSL01' ELSE t.BANK_TYPE END AS BANK_TYPE_CODE,
       CASE WHEN t.TYPE_NAME IS NULL THEN '村镇银行' ELSE t.TYPE_NAME END AS BANK_TYPE_NAME
FROM bt_input_bank_info b
         LEFT JOIN bt_bank_type t ON b.BANK_PREFIX = t.BANK_PREFIX AND b.TENANT_ID = t.TENANT_ID
    AND b.BANK_NAME LIKE '%' || NVL(t.BANK_NAME_PREFIX,'') || '%'
WHERE t.BANK_NAME_PREFIX IS NULL OR b.BANK_NAME LIKE '%' || t.BANK_NAME_PREFIX || '%';
/

-- 9. DSL_GX_BIS_ACC_DTL_VIEW


-- 视图说明：DSL_GX_BIS_ACC_DTL_VIEW
CREATE OR REPLACE VIEW DSL_GX_BIS_ACC_DTL_VIEW AS
SELECT dtl.ID AS seqId,
       dtl.TENANT_ID AS TENANT_ID,
       dtl.BANK_SERIAL_NO AS yhlsh,
       CASE WHEN bc.CODE = '100' THEN '100000' ELSE bc.CODE END AS dwbh,
       bc.NAME AS dwmc,
       CASE WHEN dtl.CD_SIGN = '1' THEN '02' ELSE '01' END AS incomeExpenseType,
       dtl.BANK_ACC AS zhbh,
       dtl.ACC_NAME AS zhmc,
       acc.BANK_CODE AS lhh,
       dtl.BANK_NAME AS khh,
       dtl.AMT AS je,
       dtl.OPP_ACC_NO AS dfzh,
       dtl.OPP_ACC_NAME AS dfhm,
       dtl.OPP_ACC_BANK AS dfkhh,
       CASE WHEN c.ENGLISH_CODE = 'RMB' THEN 'CNY' ELSE c.ENGLISH_CODE END AS currencyCode,
       TO_CHAR(dtl.TRANS_TIME, 'YYYY/MM/DD') AS jyrq,
       TO_CHAR(dtl.TRANS_TIME, 'YYYY/MM/DD HH24:MI:SS') AS jysj,
       dtl.REMARK AS yt,
       dtl.POSTSCRIPT AS summary,
       dtl.USES AS purpose,
       nt.BUS_TYPE AS barCodeType,
       nt.BUS_TYPE_NAME AS billTypeDesc,
       nb.BURSAR_BILL_NO AS sourceNum,
       NVL(ns.isReturn,'N') AS isReturn
FROM bis_acc_dtl dtl
         LEFT JOIN (SELECT dtl_id, TENANT_ID, bill_id, 'Y' AS isReturn FROM nis_bill_bank_serial_no_refund
                    UNION
                    SELECT dtl_id, TENANT_ID, bill_id, 'N' AS isReturn FROM nis_bill_bank_serial_no) ns
                   ON dtl.ID = ns.dtl_id AND dtl.TENANT_ID = ns.TENANT_ID
         LEFT JOIN nis_billhead nb ON nb.ID = ns.bill_id AND nb.TENANT_ID = dtl.TENANT_ID
         LEFT JOIN nis_bus_type nt ON nb.BUS_TYPE = nt.ID AND nt.TENANT_ID = dtl.TENANT_ID
         LEFT JOIN bt_bank_acc acc ON dtl.BANK_ACC = acc.BANK_ACC AND acc.TENANT_ID = dtl.TENANT_ID
         LEFT JOIN sys_corp bc ON bc.ID = acc.CORP_ID AND bc.TENANT_ID = dtl.TENANT_ID
         LEFT JOIN bis_bif_init bi ON bi.ID = dtl.BIF_CODE AND bi.TENANT_ID = dtl.TENANT_ID
         LEFT JOIN bt_currency c ON c.ID = dtl.CUR_ID AND c.TENANT_ID = dtl.TENANT_ID
WHERE 1 = 1
  AND dtl.TRANS_TIME >= TO_TIMESTAMP('2025-02-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')
  AND dtl.CREATE_DATE > TO_TIMESTAMP('2025-02-07 15:00:00', 'YYYY-MM-DD HH24:MI:SS')
  AND NVL(dtl.DTL_SYNC_STATUS,'F') <> 'S'
  AND dtl.BANK_ACC <> '15999329560036'
  AND (NVL(dtl.GX_DTL_SYNC_STATUS,'F') <> 'S' OR NVL(nb.BURSAR_BILL_NO,'') <> NVL(dtl.GX_SOURCE_NUM,''));
/

-- 10. DSL_PAYMENT_RESULT_VIEW
-- 注意: 该视图较长，此处提供转换后的完整版本


-- 视图说明：DSL_PAYMENT_RESULT_VIEW
CREATE OR REPLACE VIEW DSL_PAYMENT_RESULT_VIEW AS
-- 第一部分 (PaymentStatus = 'C' 或 'R')
SELECT erp.SERIAL_NO_ERP AS paymentNo,
       erp.TENANT_ID AS TENANT_ID,
       n.bill_no AS paymentVoucherNo,
       NULL AS flowNo,
       erp.GX_VOUCHER_TYPE AS paymentType,
       CASE
           WHEN nd.SYNC_STATUS = 'S' AND ft.VOUCHER_TYPE IN ('66','67','68','69','70','71') THEN 'R'
           ELSE 'C'
           END AS paymentStatus,
       NULL AS actualPaymentAmount,
       NULL AS actualPaymentDate,
       NULL AS payBankAccountNo,
       NULL AS payBankAccountName,
       erp.SETTLE_CORP_CODE AS company,
       erp.CORP_CODE AS actualPaymentCompany,
       (SELECT LISTAGG(u.USER_CODE || '+' || u.USER_NAME, '#') WITHIN GROUP (ORDER BY u.USER_CODE)
FROM nis_corp_cashier c
    JOIN nis_corp_cashier_user u ON c.ID = u.CORP_CASHIER_ID AND c.TENANT_ID = u.TENANT_ID
WHERE c.STATUS = 1 AND erp.CORP_CODE = c.CORP_CODE) AS paymentCashierUserNo,
    n.purpose AS summary,
    NULL AS billNo,
    NULL AS intervalBeginNo,
    NULL AS intervalEndNo,
    NULL AS billAmount,
    NULL AS billStatus,
    NULL AS customerCode,
    NULL AS customerName,
    NULL AS billDate,
    NULL AS paymentErrReason,
    CASE
    WHEN nd.SYNC_STATUS = 'S' AND ft.VOUCHER_TYPE IN ('66','67','68','69','70','71') THEN nd.BILL_NO
    ELSE NULL
END AS refundBankFlowNo,
       CASE
           WHEN nd.SYNC_STATUS = 'S' AND ft.VOUCHER_TYPE IN ('66','67','68','69','70','71') THEN nd.PAY_MONEY
           ELSE NULL
END AS refundAmount,
       CASE
           WHEN nd.SYNC_STATUS = 'S' AND ft.VOUCHER_TYPE IN ('66','67','68','69','70','71') THEN TO_CHAR(nd.UPDATE_DATE, 'YYYY-MM-DD HH24:MI:SS')
           ELSE NULL
END AS refundFlowTradeDate,
       CASE
           WHEN nd.SYNC_STATUS = 'S' AND ft.VOUCHER_TYPE IN ('66','67','68','69','70','71') THEN nd.PAYEE_ACC_NAME
           ELSE NULL
END AS refundRemitter,
       CASE
           WHEN nd.SYNC_STATUS = 'S' AND ft.VOUCHER_TYPE IN ('66','67','68','69','70','71') THEN nd.PAYEE_ACC
           ELSE NULL
END AS refundRemitterAccount,
       CASE
           WHEN nd.SYNC_STATUS = 'S' AND ft.VOUCHER_TYPE IN ('66','67','68','69','70','71') THEN '撤票'
           ELSE NULL
END AS refundFlowSummary,
       CASE
           WHEN nd.SYNC_STATUS = 'S' AND ft.VOUCHER_TYPE IN ('66','67','68','69','70','71') THEN nd.UPDATE_BY
           ELSE NULL
END AS refundFlowBlendingUser,
       CASE
           WHEN nd.SYNC_STATUS = 'S' AND ft.VOUCHER_TYPE IN ('66','67','68','69','70','71') THEN TO_CHAR(nd.UPDATE_DATE, 'YYYY-MM-DD HH24:MI:SS')
           ELSE NULL
END AS refundFlowBlendingDate,
       CASE
           WHEN nd.SYNC_STATUS = 'S' THEN '撤票'
           ELSE NVL(bt.BUS_TYPE_NAME, erp.BUS_TYPE)
END AS revokePaymentReason
FROM erp_payinfo erp
JOIN dsl_all_bill_view n ON n.erp_id = erp.ID AND erp.TENANT_ID = n.TENANT_ID
LEFT JOIN nis_billhead nd ON nd.ID = n.id AND nd.TENANT_ID = erp.TENANT_ID
LEFT JOIN fc_voucher_type ft ON ft.ID = nd.VOUCHER_TYPE_ID AND ft.TENANT_ID = erp.TENANT_ID
LEFT JOIN bis_exc exc ON exc.CBS_BILL_ID = n.id AND exc.TENANT_ID = erp.TENANT_ID
LEFT JOIN bt_charge_back_type bt ON bt.BUS_TYPE = erp.BUS_TYPE AND bt.TENANT_ID = erp.TENANT_ID
WHERE 1 = 1
  AND n.status = -2
  AND NOT EXISTS (SELECT 1 FROM dsl_inteface_flag flag
                  WHERE flag.SOURCE_ID = n.bill_no || ';C'
                    AND flag.TABLE_SOURCE = 'dsl_payment_result'
                    AND flag.SYNC_STATUS = 'S')
  AND NOT EXISTS (SELECT 1 FROM dsl_inteface_flag flag
                  WHERE flag.SOURCE_ID = n.bill_no || ';R'
                    AND flag.TABLE_SOURCE = 'dsl_payment_result'
                    AND flag.SYNC_STATUS = 'S')
UNION ALL
-- 第二部分 (PaymentStatus = 'S')
SELECT erp.SERIAL_NO_ERP AS paymentNo,
       erp.TENANT_ID AS TENANT_ID,
       n.bill_no AS paymentVoucherNo,
       dtl.ID AS flowNo,
       erp.GX_VOUCHER_TYPE AS paymentType,
       'S' AS paymentStatus,
       CASE
           WHEN av.ID IS NOT NULL OR n.is_payed = '1' THEN n.pay_money
           ELSE dtl.AMT
           END AS actualPaymentAmount,
       CASE
           WHEN f.SYS_VOUCHER_CODE = '04' AND n.status = 95 THEN TO_CHAR(dtl.TRANS_TIME, 'YYYY-MM-DD HH24:MI:SS')
           WHEN dtl.BLEND_SIGN = '1' THEN TO_CHAR(dtl.TRANS_TIME, 'YYYY-MM-DD HH24:MI:SS')
           WHEN aaa.BRIEF_ID = '002' THEN TO_CHAR(aaa.BUSINESS_DATE, 'YYYY-MM-DD HH24:MI:SS')
           WHEN aa.BRIEF_ID = '001' THEN TO_CHAR(aa.BUSINESS_DATE, 'YYYY-MM-DD HH24:MI:SS')
           WHEN (aa.BRIEF_ID = '010' AND aa.SEND_SIGN = '1') THEN TO_CHAR(aa.APPLY_DATE, 'YYYY-MM-DD HH24:MI:SS')
           WHEN (aa.BRIEF_ID = '010' AND aa.SEND_SIGN = '0') THEN TO_CHAR(aa.BUSINESS_DATE, 'YYYY-MM-DD HH24:MI:SS')
           ELSE TO_CHAR(aa.BUSINESS_DATE, 'YYYY-MM-DD HH24:MI:SS')
           END AS actualPaymentDate,
       dtl.BANK_ACC AS payBankAccountNo,
       dtl.BANK_NAME AS payBankAccountName,
       erp.SETTLE_CORP_CODE AS company,
       erp.CORP_CODE AS actualPaymentCompany,
       (SELECT LISTAGG(u.USER_CODE || '+' || u.USER_NAME, '#') WITHIN GROUP (ORDER BY u.USER_CODE)
FROM nis_corp_cashier c
    JOIN nis_corp_cashier_user u ON c.ID = u.CORP_CASHIER_ID AND c.TENANT_ID = u.TENANT_ID
WHERE c.STATUS = 1 AND erp.CORP_CODE = c.CORP_CODE) AS paymentCashierUserNo,
    n.purpose AS summary,
    av.ORIG_NO AS billNo,
    av.SBBLL_RNG_STRT_SN AS intervalBeginNo,
    av.SBBLL_RNG_END_SN AS intervalEndNo,
    av.BILL_MONEY AS billAmount,
    CASE WHEN av.SOURCE_TYPE = '0' THEN '1' WHEN av.SOURCE_TYPE = '1' THEN '2' END AS billStatus,
       av.PAYEEACC AS customerCode,
       av.PAYEE AS customerName,
       TO_CHAR(av.START_DATE, 'YYYY-MM-DD') AS billDate,
       NULL AS paymentErrReason,
       NULL AS refundBankFlowNo,
       NULL AS refundAmount,
       NULL AS refundFlowTradeDate,
       NULL AS refundRemitter,
       NULL AS refundRemitterAccount,
       NULL AS refundFlowSummary,
       NULL AS refundFlowBlendingUser,
       NULL AS refundFlowBlendingDate,
       NULL AS revokePaymentReason
FROM erp_payinfo erp
JOIN dsl_all_bill_view n ON n.erp_id = erp.ID AND erp.TENANT_ID = n.TENANT_ID
LEFT JOIN fc_voucher_type f ON n.voucher_type_id = f.ID AND erp.TENANT_ID = f.TENANT_ID
LEFT JOIN bis_exc exc ON exc.CBS_BILL_ID = n.id AND erp.TENANT_ID = exc.TENANT_ID
LEFT JOIN bt_charge_back_type bt ON bt.BUS_TYPE = erp.BUS_TYPE AND bt.TENANT_ID = erp.TENANT_ID
LEFT JOIN nis_bill_bank_serial_no ns ON n.id = ns.BILL_ID AND erp.TENANT_ID = ns.TENANT_ID
LEFT JOIN bis_acc_dtl dtl ON ns.DTL_ID = dtl.ID AND erp.TENANT_ID = dtl.TENANT_ID
LEFT JOIN nis_bill nb ON nb.PAY_ID = n.id AND erp.TENANT_ID = nb.TENANT_ID
LEFT JOIN ab_accrecords aa ON aa.ID = nb.BILL_ID AND erp.TENANT_ID = aa.TENANT_ID
LEFT JOIN ab_voucherinfo av ON av.ID = aa.VID AND erp.TENANT_ID = av.TENANT_ID
LEFT JOIN ab_accrecords aaa ON aa.VVID = aaa.VVID AND aaa.BRIEF_ID = '002' AND aaa.OPT_STATUS = '19'
WHERE 1 = 1
  AND ((aa.ID IS NOT NULL
        AND ((aa.SEND_SIGN = '1' AND aaa.ID IS NOT NULL AND n.status IN (95,99) AND aaa.RETURN_CODE IN ('0','99'))
             OR (aa.SEND_SIGN = '0' AND aa.BRIEF_ID = '001' AND aa.OPT_STATUS = '19' AND n.status IN (95,99))
             OR (aa.SEND_SIGN = '1' AND aa.BRIEF_ID = '010' AND aa.OPT_STATUS IN ('18','19') AND n.status IN (95,99))
             OR (aa.SEND_SIGN = '0' AND aa.BRIEF_ID = '010' AND aa.OPT_STATUS = '19' AND n.status IN (95,99))))
       OR (f.SYS_VOUCHER_CODE = '04' AND n.status = 95 AND dtl.BLEND_SIGN = '1')
       OR ((n.is_payed = '0' AND exc.VOUCHER_STAT = '0' OR (n.is_payed = '1' AND n.status = 95)) AND dtl.BLEND_SIGN = '1' AND dtl.ID IS NOT NULL))
  AND NOT EXISTS (SELECT 1 FROM dsl_inteface_flag flag
                  WHERE flag.SOURCE_ID = n.bill_no || ';S'
                    AND flag.TABLE_SOURCE = 'dsl_payment_result'
                    AND flag.SYNC_STATUS = 'S')
UNION ALL
-- 第三部分 (PaymentStatus = 'E')
SELECT erp.SERIAL_NO_ERP AS paymentNo,
       erp.TENANT_ID AS TENANT_ID,
       n.bill_no AS paymentVoucherNo,
       NULL AS flowNo,
       erp.GX_VOUCHER_TYPE AS paymentType,
       'E' AS paymentStatus,
       NULL AS actualPaymentAmount,
       NULL AS actualPaymentDate,
       NULL AS payBankAccountNo,
       NULL AS payBankAccountName,
       erp.SETTLE_CORP_CODE AS company,
       erp.CORP_CODE AS actualPaymentCompany,
       (SELECT LISTAGG(u.USER_CODE || '+' || u.USER_NAME, '#') WITHIN GROUP (ORDER BY u.USER_CODE)
FROM nis_corp_cashier c
    JOIN nis_corp_cashier_user u ON c.ID = u.CORP_CASHIER_ID AND c.TENANT_ID = u.TENANT_ID
WHERE c.STATUS = 1 AND erp.CORP_CODE = c.CORP_CODE) AS paymentCashierUserNo,
    n.purpose AS summary,
    av.ORIG_NO AS billNo,
    av.SBBLL_RNG_STRT_SN AS intervalBeginNo,
    av.SBBLL_RNG_END_SN AS intervalEndNo,
    av.BILL_MONEY AS billAmount,
    av.BILL_STATUS AS billStatus,
    av.PAYEEACC AS customerCode,
    av.PAYEE AS customerName,
    TO_CHAR(av.START_DATE, 'YYYY-MM-DD') AS billDate,
    exc.NEXT_RETURN_MSG AS paymentErrReason,
    NULL AS refundBankFlowNo,
    NULL AS refundAmount,
    NULL AS refundFlowTradeDate,
    NULL AS refundRemitter,
    NULL AS refundRemitterAccount,
    NULL AS refundFlowSummary,
    NULL AS refundFlowBlendingUser,
    NULL AS refundFlowBlendingDate,
    NULL AS revokePaymentReason
FROM erp_payinfo erp
    JOIN dsl_all_bill_view n ON n.erp_id = erp.ID AND erp.TENANT_ID = n.TENANT_ID
    LEFT JOIN bis_exc exc ON exc.CBS_BILL_ID = n.id AND erp.TENANT_ID = exc.TENANT_ID
    LEFT JOIN nis_bill nb ON nb.PAY_ID = n.id AND erp.TENANT_ID = nb.TENANT_ID
    LEFT JOIN ab_accrecords aa ON aa.ID = nb.BILL_ID AND erp.TENANT_ID = aa.TENANT_ID
    LEFT JOIN ab_voucherinfo av ON av.ID = aa.VID AND erp.TENANT_ID = av.TENANT_ID
WHERE 1 = 1
  AND exc.VOUCHER_STAT = '7'
  AND NOT EXISTS (SELECT 1 FROM dsl_inteface_flag flag
    WHERE flag.SOURCE_ID = n.bill_no || ';E'
  AND flag.TABLE_SOURCE = 'dsl_payment_result'
  AND flag.SYNC_STATUS = 'S')
UNION ALL
-- 第四部分 (PaymentStatus = 'R')
SELECT erp.SERIAL_NO_ERP AS paymentNo,
       erp.TENANT_ID AS TENANT_ID,
       n.bill_no AS paymentVoucherNo,
       NULL AS flowNo,
       erp.GX_VOUCHER_TYPE AS paymentType,
       'R' AS paymentStatus,
       NULL AS actualPaymentAmount,
       NULL AS actualPaymentDate,
       NULL AS payBankAccountNo,
       NULL AS payBankAccountName,
       erp.SETTLE_CORP_CODE AS company,
       erp.CORP_CODE AS actualPaymentCompany,
       (SELECT LISTAGG(u.USER_CODE || '+' || u.USER_NAME, '#') WITHIN GROUP (ORDER BY u.USER_CODE)
FROM nis_corp_cashier c
    JOIN nis_corp_cashier_user u ON c.ID = u.CORP_CASHIER_ID AND c.TENANT_ID = u.TENANT_ID
WHERE c.STATUS = 1 AND erp.CORP_CODE = c.CORP_CODE) AS paymentCashierUserNo,
    n.purpose AS summary,
    av.ORIG_NO AS billNo,
    av.SBBLL_RNG_STRT_SN AS intervalBeginNo,
    av.SBBLL_RNG_END_SN AS intervalEndNo,
    av.BILL_MONEY AS billAmount,
    av.BILL_STATUS AS billStatus,
    av.PAYEEACC AS customerCode,
    av.PAYEE AS customerName,
    TO_CHAR(av.START_DATE, 'YYYY-MM-DD') AS billDate,
    NULL AS paymentErrReason,
    dtl.ID AS refundBankFlowNo,
    dtl.AMT AS refundAmount,
    TO_CHAR(dtl.TRANS_TIME, 'YYYY-MM-DD HH24:MI:SS') AS refundFlowTradeDate,
    dtl.OPP_ACC_NAME AS refundRemitter,
    dtl.OPP_ACC_NO AS refundRemitterAccount,
    dtl.REMARK AS refundFlowSummary,
    dtl.REFUND_BLEND_BY AS refundFlowBlendingUser,
    TO_CHAR(dtl.REFUND_BLEND_TIME, 'YYYY-MM-DD HH24:MI:SS') AS refundFlowBlendingDate,
    NULL AS revokePaymentReason
FROM erp_payinfo erp
    JOIN dsl_all_bill_view n ON n.erp_id = erp.ID AND erp.TENANT_ID = n.TENANT_ID
    LEFT JOIN bis_exc exc ON exc.CBS_BILL_ID = n.id AND erp.TENANT_ID = exc.TENANT_ID
    LEFT JOIN nis_bill_bank_serial_no_refund r ON r.BILL_ID = n.id AND erp.TENANT_ID = r.TENANT_ID
    LEFT JOIN bis_acc_dtl dtl ON dtl.ID = r.DTL_ID AND erp.TENANT_ID = dtl.TENANT_ID
    LEFT JOIN nis_bill nb ON nb.PAY_ID = n.id AND erp.TENANT_ID = nb.TENANT_ID
    LEFT JOIN ab_accrecords aa ON aa.ID = nb.BILL_ID AND erp.TENANT_ID = aa.TENANT_ID
    LEFT JOIN ab_voucherinfo av ON av.ID = aa.VID AND erp.TENANT_ID = av.TENANT_ID
WHERE 1 = 1
  AND dtl.ID IS NOT NULL
  AND dtl.BLEND_SIGN = '1'
  AND EXISTS (SELECT 1 FROM dsl_inteface_flag flag
    WHERE flag.SOURCE_ID = n.bill_no || ';S'
  AND flag.TABLE_SOURCE = 'dsl_payment_result'
  AND flag.SYNC_STATUS = 'S')
  AND NOT EXISTS (SELECT 1 FROM dsl_inteface_flag flag
    WHERE flag.SOURCE_ID = n.bill_no || ';R'
  AND flag.TABLE_SOURCE = 'dsl_payment_result'
  AND flag.SYNC_STATUS = 'S');
/

-- 11. STANDARD_BANKACC_VIEW


-- 视图说明：STANDARD_BANKACC_VIEW
CREATE OR REPLACE VIEW STANDARD_BANKACC_VIEW AS
SELECT a.TENANT_ID AS TENANT_ID,
       a.ID AS id,
       a.BANK_ACC AS bankAcc,
       a.ACC_NAME AS accName,
       sc.CODE AS corpCode,
       sc.NAME AS corpName,
       d.CUR_CODE AS curCode,
       d.CUR_NAME AS curName,
       a.BANK_CODE AS bankCode,
       a.BANK_NAME AS bankName,
       a.IS_ONLINE AS isOnline,
       a.VALID_SIGN AS validSign,
       TO_CHAR(NVL(a.UPDATE_DATE, SYSDATE), 'YYYY-MM-DD') AS updateDate
FROM bt_bank_acc a
         LEFT JOIN sys_corp sc ON a.CORP_ID = sc.ID AND a.TENANT_ID = sc.TENANT_ID
         LEFT JOIN bt_bank_acc_cur c ON a.ID = c.BANK_ACC_ID AND a.TENANT_ID = c.TENANT_ID
         LEFT JOIN bt_currency d ON c.CUR_ID = d.ID AND c.TENANT_ID = d.TENANT_ID;
/

-- 12. STANDARD_BANKINPUT_VIEW


-- 视图说明：STANDARD_BANKINPUT_VIEW
CREATE OR REPLACE VIEW STANDARD_BANKINPUT_VIEW AS
SELECT b.TENANT_ID AS TENANT_ID,
       b.ID AS id,
       b.SYS_BANK_CODE AS bankCode,
       b.BANK_NAME AS bankName,
       r.BANK_PROV AS provCode,
       r.PROV_INPUT_NAME AS provName,
       r.BANK_INPUT_CITY AS cityCode,
       r.NAME AS cityName,
       TO_CHAR(NVL(b.UPDATE_DATE, SYSDATE), 'YYYY-MM-DD') AS updateDate
FROM bt_input_bank_info b
         JOIN sys_bank_region br ON b.BANK_CITY_CODE = br.BANK_CITY AND b.TENANT_ID = br.TENANT_ID
         JOIN sys_region r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY AND br.TENANT_ID = r.TENANT_ID;
/

-- 13. STANDARD_CORP_VIEW


-- 视图说明：STANDARD_CORP_VIEW
CREATE OR REPLACE VIEW STANDARD_CORP_VIEW AS
SELECT c.TENANT_ID AS TENANT_ID,
       c.CODE AS code,
       c.NAME AS name,
       c.STATUS AS status,
       TO_CHAR(NVL(c.UPDATE_TIME, c.CREATE_TIME), 'YYYY-MM-DD') AS updateDate
FROM sys_corp c;
/

-- 14. STANDARD_ITEM_VIEW


-- 视图说明：STANDARD_ITEM_VIEW
CREATE OR REPLACE VIEW STANDARD_ITEM_VIEW AS
SELECT i.TENANT_ID AS TENANT_ID,
       i.ID AS id,
       i.ITEM_CODE AS item_code,
       i.ITEM_NAME AS item_name,
       TO_CHAR(NVL(i.UPDATE_DATE, i.CREATE_DATE), 'YYYY-MM-DD') AS updateDate
FROM fbs_item i;
/

-- 15. V_ACC_LIST


-- 视图说明：V_ACC_LIST
-- SaaS 租户关联 SYS_TENANT_MANAGER，TENANT_STATUS = 2 表示有效租户。
CREATE OR REPLACE VIEW V_ACC_LIST AS
-- 第一部分（国内）
SELECT a.ID AS bank_acc_id,
       a.BANK_ACC AS bank_acc,
       a.ACC_NAME AS acc_name,
       (SELECT b.BANK_ACC FROM bt_bank_acc b
        WHERE b.PARENT_SIGN = '1' AND c.PRENT_ACC_ID = b.ID AND c.CUR_ID = d.ID) AS prent_acc,
       (SELECT b.BANK_ACC FROM bt_bank_acc b WHERE a.ENTITY_ACC_ID = b.ID) AS entity_acc,
       a.CORP_ID AS corp_id,
       d.CUR_CODE AS cur_code,
       c.CUR_ID AS cur_id,
       c.COUNTER_ACC AS counter_acc,
       c.SUPER_COUNTER_ACC AS super_counter_acc,
       a.PARENT_SIGN AS parent_sign,
       a.ACC_TYPE AS acc_type,
       a.BANK_CODE AS bank_code,
       a.BANK_NAME AS bank_name,
       e.NAME AS prov,
       e.ID AS provId,
       e.BANK_PROV AS provCode,
       f.NAME AS city,
       f.ID AS cityId,
       e.BANK_INPUT_CITY AS cityCode,
       a.PRIVATE_NO AS private_no,
       a.ORG_CODE AS org_code,
       b.BIF_CODE AS bif_code,
       a.IS_ONLINE AS is_online,
       a.VALID_SIGN AS valid_sign,
       b.ID AS bifCodeId,
       a.IS_CAPITAL_POOL AS is_capital_pool,
       '' AS abc_secondary_account,
       '' AS primary_account,
       '' AS basic_account_sign,
       a.IS_DOMESTIC_BANK AS is_domestic_bank,
       j.NATURE_CODE AS nature_code,
       a.BANK_ORG_CODE AS bank_org_code,
       a.ATTRIBUTE_ID AS attribute_id,
       a.ELECTRIC_BILL AS electric_bill,
       a.TENANT_ID AS tenant_id,
       t.tenant_code AS tenant_code,
       a.BANK_TYPE_ID AS bank_type_id,
       k.TYPE_NAME AS bank_type_name
FROM bt_bank_acc a
         JOIN bis_bif_init b ON a.BIF_CODE = b.ID
         JOIN bt_bank_acc_cur c ON a.ID = c.BANK_ACC_ID AND a.TENANT_ID = c.TENANT_ID
         JOIN bt_currency d ON c.CUR_ID = d.ID AND a.TENANT_ID = d.TENANT_ID
         JOIN sys_region e ON a.PROV = e.ID
         JOIN sys_region f ON a.CITY = f.ID
         JOIN bt_acc_nature j ON a.NATURE_ID = j.ID AND a.TENANT_ID = j.TENANT_ID
         JOIN sys_tenant_manager t ON a.TENANT_ID = t.id
         JOIN bt_bank_type k ON a.BANK_TYPE_ID = k.ID
WHERE a.STATUS = '95' AND t.TENANT_STATUS = 2
UNION ALL
-- 第二部分（境外）
SELECT a.ID AS bank_acc_id,
       a.BANK_ACC AS bank_acc,
       a.ACC_NAME AS acc_name,
       (SELECT b.BANK_ACC FROM bt_bank_acc b
        WHERE b.PARENT_SIGN = '1' AND c.PRENT_ACC_ID = b.ID AND c.CUR_ID = d.ID) AS prent_acc,
       (SELECT b.BANK_ACC FROM bt_bank_acc b WHERE a.ENTITY_ACC_ID = b.ID) AS entity_acc,
       a.CORP_ID AS corp_id,
       d.CUR_CODE AS cur_code,
       c.CUR_ID AS cur_id,
       c.COUNTER_ACC AS counter_acc,
       c.SUPER_COUNTER_ACC AS super_counter_acc,
       a.PARENT_SIGN AS parent_sign,
       a.ACC_TYPE AS acc_type,
       a.BANK_CODE AS bank_code,
       a.BANK_NAME AS bank_name,
       e.NAME_ZH AS prov,
       e.ID AS provId,
       e.CODE AS provCode,
       f.NAME_ZH AS city,
       f.ID AS cityId,
       f.CODE AS cityCode,
       a.PRIVATE_NO AS private_no,
       a.ORG_CODE AS org_code,
       b.BIF_CODE AS bif_code,
       a.IS_ONLINE AS is_online,
       a.VALID_SIGN AS valid_sign,
       b.ID AS bifCodeId,
       a.IS_CAPITAL_POOL AS is_capital_pool,
       '' AS abc_secondary_account,
       '' AS primary_account,
       '' AS basic_account_sign,
       a.IS_DOMESTIC_BANK AS is_domestic_bank,
       j.NATURE_CODE AS nature_code,
       a.BANK_ORG_CODE AS bank_org_code,
       a.ATTRIBUTE_ID AS attribute_id,
       a.ELECTRIC_BILL AS electric_bill,
       a.TENANT_ID AS tenant_id,
       t.tenant_code AS tenant_code,
       a.BANK_TYPE_ID AS bank_type_id,
       k.TYPE_NAME AS bank_type_name
FROM bt_bank_acc a
         JOIN bis_bif_init b ON a.BIF_CODE = b.ID
         JOIN bt_bank_acc_cur c ON a.ID = c.BANK_ACC_ID AND a.TENANT_ID = c.TENANT_ID
         JOIN bt_currency d ON c.CUR_ID = d.ID AND a.TENANT_ID = d.TENANT_ID
         JOIN sys_country_cust e ON a.PROV = e.ID AND a.TENANT_ID = e.TENANT_ID
         JOIN sys_country_cust f ON a.CITY = f.ID AND a.TENANT_ID = f.TENANT_ID
         JOIN bt_acc_nature j ON a.NATURE_ID = j.ID AND a.TENANT_ID = j.TENANT_ID
         JOIN sys_tenant_manager t ON a.TENANT_ID = t.id
         JOIN bt_bank_type k ON a.BANK_TYPE_ID = k.ID
WHERE a.STATUS = '95' AND t.TENANT_STATUS = 2;
/

-- 16. V_BILL_MERCHANTS_INFO


-- 视图说明：V_BILL_MERCHANTS_INFO
CREATE OR REPLACE VIEW V_BILL_MERCHANTS_INFO AS
SELECT vv.TENANT_ID AS TENANT_ID,
       vv.ID AS id,
       vv.CORP_ID AS corp_id,
       c.NAME AS corp_name,
       v.BILL_MONEY AS bill_money,
       v.START_DATE AS start_date,
       v.END_DATE AS end_date,
       v.PAYER AS payer,
       v.PAYERACC AS payeracc,
       v.PAYER_BANK_NAME AS payer_bank_name,
       v.PAYEE AS payee,
       v.PAYEEACC AS payeeacc,
       v.PAYEE_BANK_NAME AS payee_bank_name,
       v.ACCEPTOR AS acceptor,
       v.ACCEPTORACC AS acceptoracc,
       v.ACCEPTORBANKNO AS acceptorbankno,
       v.ACCEPTORBANKADDRESS AS acceptorbankaddress,
       v.A_CONTRACT_NO AS a_contract_no,
       'abEmitBillPrintServiceImpl' AS servicebean
FROM ab_voucherinfo_vice vv
         LEFT JOIN ab_voucherinfo v ON vv.VID = v.ID AND vv.TENANT_ID = v.TENANT_ID
         LEFT JOIN ab_accrecords a ON a.VVID = vv.ID AND a.TENANT_ID = vv.TENANT_ID
         LEFT JOIN sys_corp c ON c.ID = vv.CORP_ID AND c.TENANT_ID = vv.TENANT_ID
WHERE a.BRIEF_ID = '001'
  AND a.STATUS = 95
  AND v.BILL_TYPE = '11'
  AND v.BILL_FLAG = '10'
  AND vv.VALID_SIGN = '1';
/

-- 17. V_BIS_ACC_GATHER_POLICY


-- 视图说明：V_BIS_ACC_GATHER_POLICY
CREATE OR REPLACE VIEW V_BIS_ACC_GATHER_POLICY AS
SELECT a.TENANT_ID AS TENANT_ID,
       a.ID AS policyId,
       b.BANK_ACC AS bank_acc,
       a.BANK_ACC_ID AS bank_acc_id,
       c.GATHER_POLICY_CODE AS gather_policy_code,
       c.ID AS id,
       c.GATHER_POLICY_NAME AS gather_policy_name,
       c.DEF_DATE AS def_date,
       c.LAUNCH_TIME AS launch_time,
       c.GATHER_TYPE AS gather_type,
       c.FRQ_MODE AS frq_mode,
       c.SERIAL_DAY AS serial_day,
       c.MIN_AMT AS min_amt,
       c.MAX_AMT AS max_amt,
       c.GATHER_MODE AS gather_mode,
       c.GATHER_AMT AS gather_amt,
       c.DOWN_FIXED_AMT AS down_fixed_amt,
       c.APP_NEXT_TIME AS app_next_time,
       c.DIFFER_SCALE AS differ_scale,
       c.IS_QUICK AS is_quick,
       c.VALID_SIGN AS valid_sign,
       a.BIF_CODE AS bif_code_id,
       e.BIF_CODE AS bif_code,
       f.BANK_ACC AS parent_acc,
       a.PARENT_ACC_ID AS parent_acc_id,
       a.FIRST_TIME AS first_time,
       a.NEXT_TIME AS next_time,
       a.APP_NEXT_TIME AS appnext_time,
       a.BANK_SOURCECODE AS bank_sourcecode,
       c.CUR_ID AS cur_id,
       c.AUTO_FILTER_HOLIDAYS AS auto_filter_holidays,
       c.PURPOSE AS purpose,
       c.ADD_WORD AS add_word
FROM bis_acc_gather_policy a
         JOIN bt_bank_acc b ON a.BANK_ACC_ID = b.ID
         JOIN bis_gather_policy c ON a.GATHER_POLICY_ID = c.ID
         JOIN bis_bif_init e ON a.BIF_CODE = e.ID
         JOIN bt_bank_acc f ON a.PARENT_ACC_ID = f.ID
WHERE b.VALID_SIGN = '1' AND c.VALID_SIGN = '1';
/

-- 18. V_BIS_EXC


-- 视图说明：V_BIS_EXC
CREATE OR REPLACE VIEW V_BIS_EXC AS
SELECT a.ID AS id,
       a.VOUCHER_NO AS voucher_no,
       a.CBS_BILL_ID AS cbs_bill_id,
       a.CORP_ID AS corp_id,
       a.REQ_DATE AS req_date,
       a.PAYER_ACC AS payer_acc,
       a.PAYER_ACC_NAME AS payer_acc_name,
       a.PAYER_BANK AS payer_bank,
       b.NAME AS payer_prov,
       c.NAME AS payer_city,
       e.CUR_CODE AS cur_code,
       a.CUR_ID AS cur_id,
       a.PAY_MONEY AS pay_money,
       f.SYS_VOUCHER_CODE AS sys_voucher_code,
       a.VOUCHER_TYPE_ID AS voucher_type_id,
       a.PAYEE_ACC AS payee_acc,
       a.PAYEE_ACC_NAME AS payee_acc_name,
       a.PAYEE_BANK AS payee_bank,
       a.PAYEE_BANK_CODE AS payee_bank_code,
       g.NAME AS payee_prov,
       h.NAME AS payee_city,
       a.WISH_PAYDAY AS wish_payday,
       a.IS_QUICK AS is_quick,
       a.TRANS_MODE AS trans_mode,
       d.BIF_CODE AS bif_code,
       a.BIF_CODE AS bif_code_id,
       a.DIF_BANK AS dif_bank,
       a.AREA_SIGN AS area_sign,
       a.PURPOSE AS purpose,
       a.add_word AS add_word,
       a.VOUCHER_FROM AS voucher_from,
       a.BANK_SERIAL_NO AS bank_serial_no,
       a.TRANS_TIME AS trans_time,
       a.COUNTER_ACC AS counter_acc,
       a.VOUCHER_STAT AS voucher_stat,
       a.MID_PAYER_ACC_NO AS mid_payer_acc_no,
       a.MID_PAYER_ACC_NAME AS mid_payer_acc_name,
       a.MID_PAYER_BANK_NAME AS mid_payer_bank_name,
       a.RESEND_SIGN AS resend_sign,
       a.DELAY_TIME AS delay_time,
       a.CBS_BILL_NO AS cbs_bill_no,
       a.TENANT_ID AS TENANT_ID,
       i.tenant_code AS TENANT_CODE
FROM bis_exc a
         LEFT JOIN sys_region b ON b.ID = a.PAYER_PROV
         LEFT JOIN sys_region c ON c.ID = a.PAYER_CITY
         JOIN bis_bif_init d ON d.ID = a.BIF_CODE
         JOIN bt_currency e ON e.ID = a.CUR_ID AND a.TENANT_ID = e.TENANT_ID
         JOIN fc_voucher_type f ON f.ID = a.VOUCHER_TYPE_ID AND a.TENANT_ID = f.TENANT_ID
         JOIN sys_region g ON g.ID = a.PAYEE_PROV
         JOIN sys_region h ON h.ID = a.PAYEE_CITY
         JOIN sys_tenant i ON i.id = a.TENANT_ID
WHERE i.status = 1;
/

-- 19. V_BIS_EXC_PAYOFF


-- 视图说明：V_BIS_EXC_PAYOFF
CREATE OR REPLACE VIEW V_BIS_EXC_PAYOFF AS
SELECT a.TENANT_ID AS TENANT_ID,
       a.VOUCHER_NO AS voucher_no,
       a.CBS_BILL_ID AS cbs_bill_id,
       a.CORP_ID AS corp_id,
       a.REQ_DATE AS req_date,
       a.PAYER_ACC AS payer_acc,
       a.PAYER_ACC_NAME AS payer_acc_name,
       a.PAYER_BANK AS payer_bank,
       b.NAME AS payerProv,
       c.NAME AS payerCity,
       e.CUR_CODE AS cur_code,
       a.CUR_ID AS cur_id,
       a.PAY_MONEY AS pay_money,
       f.SYS_VOUCHER_CODE AS sys_voucher_code,
       a.VOUCHER_TYPE_ID AS voucher_type_id,
       a.WISH_PAYDAY AS wish_payday,
       a.IS_QUICK AS is_quick,
       a.TRANS_MODE AS trans_mode,
       d.BIF_CODE AS bif_code,
       a.BIF_CODE AS bif_code_id,
       a.DIF_BANK AS dif_bank,
       a.AREA_SIGN AS area_sign,
       a.PURPOSE AS purpose,
       a.add_word AS add_word,
       a.VOUCHER_FROM AS voucher_from,
       a.BANK_SERIAL_NO AS bank_serial_no,
       a.TRANS_TIME AS trans_time,
       a.COUNTER_ACC AS counter_acc,
       a.VOUCHER_STAT AS voucher_stat
FROM bis_exc a
         JOIN sys_region b ON b.ID = a.PAYER_PROV
         JOIN sys_region c ON c.ID = a.PAYER_CITY
         JOIN bis_bif_init d ON d.ID = a.BIF_CODE
         JOIN bt_currency e ON e.ID = a.CUR_ID
         JOIN fc_voucher_type f ON f.ID = a.VOUCHER_TYPE_ID
WHERE b.TENANT_ID = a.TENANT_ID AND c.TENANT_ID = a.TENANT_ID
  AND e.TENANT_ID = a.TENANT_ID AND f.TENANT_ID = a.TENANT_ID AND d.TENANT_ID = a.TENANT_ID;
/

-- 20. V_BIS_FINANCE_EXC


-- 视图说明：V_BIS_FINANCE_EXC
CREATE OR REPLACE VIEW V_BIS_FINANCE_EXC AS
SELECT a.TENANT_ID AS TENANT_ID,
       a.ID AS id,
       a.VOUCHER_NO AS voucher_no,
       a.CBS_BILL_ID AS cbs_bill_id,
       a.CBS_BILL_NO AS cbs_bill_no,
       a.CORP_ID AS corp_id,
       a.REQ_DATE AS req_date,
       b.BIF_CODE AS bif_code,
       a.PAYER_ACC AS payer_acc,
       a.PAYER_ACC_NAME AS payer_acc_name,
       a.PAYER_BANK AS payer_bank,
       a.CUR_ID AS cur_id,
       c.CUR_CODE AS cur_code,
       a.PAY_MONEY AS pay_money,
       a.PURPOSE AS purpose,
       a.WISH_PAYDAY AS wish_payday,
       a.DAY_LIMIT AS day_limit,
       a.DATE_UNIT AS date_unit,
       a.BURSAR_BILL_NO AS bursar_bill_no,
       a.INTEREST_RATE AS interest_rate,
       a.BANK_SERIAL_NO AS bank_serial_no,
       a.CERTIFICATE_WAY AS certificate_way,
       a.BUS_TYPE AS bus_type,
       a.OPEN_DATE AS open_date,
       a.PAYEE_ACC AS payee_acc,
       a.PAYEE_ACC_NAME AS payee_acc_name,
       a.CURR_BALANCE AS curr_balance,
       a.TIME_BALANCE AS time_balance,
       a.VOUCHER_STAT AS voucher_stat,
       a.TRANS_TIME AS trans_time
FROM bis_finance_exc a
         JOIN bis_bif_init b ON a.BIF_CODE = b.ID
         JOIN bt_currency c ON a.CUR_ID = c.ID
WHERE a.TENANT_ID = b.TENANT_ID AND a.TENANT_ID = c.TENANT_ID;
/

-- 21. V_BIS_FOREIGN_EXC (大幅简化，保留关键字段转换)


-- 视图说明：V_BIS_FOREIGN_EXC
CREATE OR REPLACE VIEW V_BIS_FOREIGN_EXC AS
SELECT a.VOUCHER_NO AS voucher_no,
       a.CBS_BILL_ID AS cbs_bill_id,
       a.CBS_BILL_NO AS cbs_bill_no,
       a.CORP_ID AS corp_id,
       a.REQ_DATE AS req_date,
       a.PAY_DATE AS pay_date,
       a.REMIT_TYPE AS remit_type,
       a.BANK_TYPE_ID AS bank_type_id,
       b.BIF_CODE AS bif_code,
       a.BIF_CODE AS bif_code_id,
       a.PAYER_ACC AS payer_acc,
       a.PAYER_ACC_NAME AS payer_acc_name,
       a.PAYER_BANK AS payer_bank,
       a.PAYER_NAME AS payer_name,
       a.PAYER_TEL AS payer_tel,
       a.CUR_ID AS cur_id,
       c.CUR_CODE AS cur_code,
       a.PAY_MONEY AS pay_money,
       a.PAYEE_ACC AS payee_acc,
       a.PAYEE_ACC_NAME AS payee_acc_name,
       a.PAYEE_BANK AS payee_bank,
       a.PAYEE_SWIFT_CODE AS payee_swift_code,
       d.CODE AS payee_country_code,
       a.PAYEE_AGENT_ACC AS payee_agent_acc,
       a.PAYEE_AGENT_BANK AS payee_agent_bank,
       a.PAYEE_AGENT_BANK_ADD AS payee_agent_bank_add,
       a.QUICK_SIGN AS quick_sign,
       a.REMIT_NATURE AS remit_nature,
       a.REMIT_METHOD AS remit_method,
       a.COST_SIGN AS cost_sign,
       a.PAY_TYPE AS pay_type,
       a.SHIP_DATE AS ship_date,
       a.IS_BONDED AS is_bonded,
       a.BUS_NO AS bus_no,
       a.ADD_WORD AS add_word,
       a.RMK AS rmk,
       a.CONTRACT_NO AS contract_no,
       a.INVOICE_NO AS invoice_no,
       a.TRAN_AMT1 AS tran_amt1,
       a.TRAN_CODE1 AS tran_code1,
       a.TRAN_CUR_ID1 AS tran_cur_id1,
       f.CUR_CODE AS tran_cur_code1,
       a.TRAN_ADD_WORD1 AS tran_add_word1,
       a.TRAN_CODE2 AS tran_code2,
       a.TRAN_AMT2 AS tran_amt2,
       a.TRAN_CUR_ID2 AS tran_cur_id2,
       g.CUR_CODE AS tran_cur_code2,
       a.TRAN_ADD_WORD2 AS tran_add_word2,
       a.COST_MODE AS cost_mode,
       a.DED_ACC AS ded_acc,
       a.BANK_CUR_CODE AS bank_cur_code,
       a.INTEREST_DATE AS interest_date,
       a.BANK_SERIAL_NO AS bank_serial_no,
       a.VOUCHER_STAT AS voucher_stat,
       a.VERIFY_CODE AS verify_code,
       a.VOUCHER_FROM AS voucher_from,
       a.FEX_BUS_TYPE AS fex_bus_type,
       a.DIF_BANK AS dif_bank,
       a.PAYER_NAME_EN AS payer_name_en,
       a.PAYER_ADDR_EN AS payer_addr_en,
       a.PAYEE_NAME_EN AS payee_name_en,
       a.PAYEE_ADDR_EN AS payee_addr_en,
       a.BANK_ORG_CODE AS bank_Org_Code,
       a.PAYEE_BANK_CODE AS payee_bank_code,
       a.PAYER_CUR_ID AS payer_cur_id,
       e.CUR_CODE AS payerCurCode,
       a.PAYEE_PROV AS payee_prov,
       a.PAYEE_CITY AS payee_city,
       a.RESEND_SIGN AS resend_sign,
       a.ACC_TYPE AS acc_type,
       a.PAYEE_TYPE AS payee_type,
       a.REMIT_ROUTE AS remit_route,
       a.REMIT_USE AS remit_use,
       a.FILE_NAME AS file_name,
       a.BANK_FILE_ID AS bank_file_id,
       a.ARRIVAL_DATE AS arrival_date,
       a.DECLARE_NO AS declare_no,
       a.PAYEE_AGENT_SWIFT_CODE AS payee_agent_swift_code,
       a.TRANS_TIME AS trans_time,
       a.CERT_TYPE AS cert_type,
       a.CERT_NO AS cert_no,
       d.NAME_ZH AS name_zh,
       a.REMIT_ATTR AS remit_attr,
       a.BUY_CUR_ID AS buy_cur_id,
       buy.CUR_CODE AS buyCurCode,
       a.BUY_AMOUNT AS buy_amount,
       a.SELL_CUR_ID AS sell_cur_id,
       sell.CUR_CODE AS sellCurCode,
       a.FEX_TRAN_TYPE AS fex_tran_type,
       a.SETTLE_BUS_TYPE AS settle_bus_type,
       a.ACC_NATURE AS acc_nature,
       a.DELEGATION_TYPE AS delegationType,
       a.MAJOR_SOURCE AS majorSource,
       a.DETAIL_SOURCE AS detailSource,
       a.BUS_MODE AS busMode,
       a.SUB_MODE AS subMode,
       a.CONTRACT_RATE AS contract_rate,
       a.PAYER_COUNTRY_CODE AS payer_country_code,
       a.LOCAL_CLEAR_NO AS local_clear_no,
       a.PAYER_SWIFT_CODE AS payer_swift_code,
       a.BANK_PAY_TYPE AS bank_pay_type,
       a.PAYEE_TEL AS payee_tel,
       a.PAYER_ADDR AS payer_addr,
       a.FIXED_MONEY AS fixed_money,
       a.FOREIGN_CONTRACT_NO AS foreign_contract_no,
       a.PAYER_PROV_NAME AS payer_prov_name,
       a.PAYER_CITY_NAME AS payer_city_name,
       a.PAYER_POST_CODE AS payer_post_code,
       a.PAYER_BUILDING_NUMBER AS payer_building_number,
       a.PAYER_STREET_NAME AS payer_street_name,
       a.PAYEE_PROV_NAME AS payee_prov_name,
       a.PAYEE_CITY_NAME AS payee_city_name,
       a.PAYEE_POST_CODE AS payee_post_code,
       a.PAYEE_BUILDING_NUMBER AS payee_building_number,
       a.PAYEE_STREET_NAME AS payee_street_name,
       a.SAVE_FIELDS AS save_fields,
       a.PAYEE_EMAIL AS payee_email,
       a.PAYEE_CUR_ID AS payee_cur_id,
       payee.CUR_CODE AS payeeCurCode,
       a.BNF_RELATE AS bnf_relate,
       a.RMK_LOCAL AS rmk_local,
       a.PAYER_LOCAL_CLEAR_NO AS payer_local_clear_no,
       a.short_name AS short_name,
       a.PAYEE_BANK_ADD AS payee_bank_add,
       a.payer_id AS payer_id,
       a.service_type_id AS service_type_id,
       a.service_type_name AS service_type_name,
       a.service_id AS service_id,
       a.service_name AS service_name,
       scc.CODE AS payee_bank_country_code,
       a.ded_acc_cur_id AS ded_acc_cur_id,
       ded.CUR_CODE AS dedAccCur,
       t.id AS tenant_id,
       t.tenant_code AS tenant_code
FROM bis_foreign_exc a
         JOIN bis_bif_init b ON a.BIF_CODE = b.ID
         JOIN bt_currency c ON a.CUR_ID = c.ID AND a.TENANT_ID = c.TENANT_ID
         LEFT JOIN sys_country_cust d ON a.payee_country_code = d.ID AND a.TENANT_ID = d.TENANT_ID
         LEFT JOIN bt_currency e ON a.PAYER_CUR_ID = e.ID AND a.TENANT_ID = e.TENANT_ID
         LEFT JOIN bt_currency f ON a.TRAN_CUR_ID1 = f.ID AND a.TENANT_ID = f.TENANT_ID
         LEFT JOIN bt_currency g ON a.TRAN_CUR_ID2 = g.ID AND a.TENANT_ID = g.TENANT_ID
         LEFT JOIN bt_currency buy ON a.BUY_CUR_ID = buy.ID AND a.TENANT_ID = buy.TENANT_ID
         LEFT JOIN bt_currency sell ON a.SELL_CUR_ID = sell.ID AND a.TENANT_ID = sell.TENANT_ID
         LEFT JOIN bt_currency payee ON a.PAYEE_CUR_ID = payee.ID AND a.TENANT_ID = payee.TENANT_ID
         LEFT JOIN bt_currency ded ON a.ded_acc_cur_id = ded.ID AND a.TENANT_ID = ded.TENANT_ID
         LEFT JOIN sys_country_cust scc ON a.payee_bank_country_code = scc.ID AND a.TENANT_ID = scc.TENANT_ID
         JOIN sys_tenant t ON a.TENANT_ID = t.id
WHERE t.status = 1;
/

-- 22. V_BIS_GATHER_CMD


-- 视图说明：V_BIS_GATHER_CMD
CREATE OR REPLACE VIEW V_BIS_GATHER_CMD AS
SELECT a.ID AS id,
       a.VOUCHER_NO AS voucher_no,
       a.BIF_CODE AS bif_code_id,
       b.BIF_CODE AS bif_code,
       a.CBS_BILL_ID AS cbs_bill_id,
       a.CHILD_ACC_ID AS child_acc_id,
       c.BANK_ACC AS child_acc,
       a.PARENT_ACC_ID AS parent_acc_id,
       d.BANK_ACC AS parent_acc,
       a.GATHER_AMT AS gather_amt,
       a.GATHER_POLICY_ID AS gather_policy_id,
       a.CUR_ID AS cur_id,
       e.CUR_CODE AS cur_code,
       a.RUN_TIME AS run_time,
       a.IS_QUICK AS is_quick,
       a.DIF_BANK AS dif_bank,
       a.AREA_SIGN AS area_sign,
       a.PURPOSE AS purpose,
       a.ADD_WORD AS add_word,
       a.VOUCHER_FROM AS voucher_from,
       a.DTL_ID AS dtl_id,
       a.CMD_STAT AS cmd_stat,
       a.TRANS_TIME AS trans_time,
       a.BANK_SERIAL_NO AS bank_serial_no,
       a.TENANT_ID AS tenant_id,
       t.tenant_code AS tenant_code
FROM bis_gather_cmd a
         JOIN bis_bif_init b ON a.BIF_CODE = b.ID
         JOIN bt_bank_acc c ON a.CHILD_ACC_ID = c.ID AND a.TENANT_ID = c.TENANT_ID
         JOIN bt_bank_acc d ON a.PARENT_ACC_ID = d.ID AND a.TENANT_ID = d.TENANT_ID
         JOIN bt_currency e ON a.CUR_ID = e.ID AND a.TENANT_ID = e.TENANT_ID
         JOIN sys_tenant t ON a.TENANT_ID = t.id
WHERE t.status = 1;
/


-- 24. V_ERP_RECEIVE_EMIT


-- 视图说明：V_ERP_RECEIVE_EMIT
CREATE OR REPLACE VIEW V_ERP_RECEIVE_EMIT AS
SELECT a.TENANT_ID AS TENANT_ID,
       a.BUSINESS_DATE AS BILL_OP_VESTING_DATE,
       a.APPLY_DATE AS transact_date,
       abs.BUS_TYPE AS operation_Id,
       a.ID AS id,
       v.ORIG_NO AS code,
       c.CODE AS dept_Code,
       vv.CORP_ID AS corp_id,
       vv.ABS AS abs,
       vv.RMK AS rmk,
       c.NAME AS corp_name,
       NVL(a.FEEMONEY, 0) AS feemoney,
       v.BILL_TYPE AS bill_type,
       v.BILL_FLAG AS bill_flag,
       v.BILL_MONEY AS money,
       v.ACCEPTOR AS acceptor,
       v.ACCEPTORBANKNO AS acceptorbankno,
       v.ACCEPTORACC AS acceptoracc,
       v.ACCEPTOR_BANK_NAME AS acceptor_bank_name,
       v.START_DATE AS trans_time,
       v.END_DATE AS maturity_date,
       v.PAYER AS payer,
       v.PAYER_BANK_NAME AS payer_bank_name,
       v.PAYERACC AS payer_bank_acc,
       v.PAYEE AS receiver,
       v.PAYEEACC AS receiver_bank_acc,
       v.PAYEE_BANK_NAME AS receiver_bank_name,
       a.BILL_SOURCE AS origin,
       vv.TYPE AS receOremit,
       CASE WHEN abs.BUS_TYPE IN ('6001','9002','9011','9017','9018','9020','9023') THEN a.REQ_NM ELSE '' END AS pre_hand,
       CASE WHEN abs.BUS_TYPE IN ('001','017','002','003','010','018','019','020','011','012','022','6201','6203','6004','023')
                THEN a.REC_NM ELSE '' END AS next_hand,
       v.A_CONTRACT_NO AS pact_no,
       v.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd,
       v.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd,
       v.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn,
       v.SBBLL_RNG_END_SN AS sbbllRngEndSn,
       v.BL_STD_AMT AS blStdAmt,
       v.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo
FROM ab_voucherinfo_vice vv
         LEFT JOIN ab_voucherinfo v ON vv.VID = v.ID AND vv.TENANT_ID = v.TENANT_ID
         LEFT JOIN ab_accrecords a ON a.VID = v.ID AND a.VVID = vv.ID AND a.TENANT_ID = vv.TENANT_ID
         LEFT JOIN sys_corp c ON c.ID = vv.CORP_ID AND c.TENANT_ID = vv.TENANT_ID
         LEFT JOIN ab_brief_status abs ON abs.STATUS = a.BRIEF_ID AND abs.TENANT_ID = a.TENANT_ID
WHERE a.STATUS = 95 AND abs.BUS_TYPE IN ('6001','010','6012','6020','6120','004','6004','6101');
/

-- 23. V_ERP_BILL


-- 视图说明：V_ERP_BILL
CREATE OR REPLACE VIEW V_ERP_BILL AS
SELECT hp.TENANT_ID AS TENANT_ID,
       hp.BILL_OP_VESTING_DATE AS BILL_OP_VESTING_DATE,
       hp.transact_date AS TRANSACT_DATE,
       hp.operation_Id AS OPERATION_ID,
       hp.id AS ID,
       hp.code AS CODE,
       hp.dept_Code AS DEPT_CODE,
       hp.corp_id AS CORP_ID,
       hp.corp_name AS CORP_NAME,
       hp.abs AS ABS,
       hp.rmk AS RMK,
       hp.bill_type AS BILL_TYPE,
       hp.bill_flag AS BILL_FLAG,
       hp.money AS MONEY,
       hp.acceptor AS ACCEPTOR,
       hp.acceptorbankno AS ACCEPTORBANKNO,
       hp.acceptoracc AS ACCEPTORACC,
       hp.acceptor_bank_name AS ACCEPTOR_BANK_NAME,
       hp.trans_time AS TRANS_TIME,
       hp.maturity_date AS MATURITY_DATE,
       hp.payer AS PAYER,
       hp.payer_bank_name AS PAYER_BANK_NAME,
       hp.payer_bank_acc AS PAYER_BANK_ACC,
       hp.receiver AS RECEIVER,
       hp.receiver_bank_acc AS RECEIVER_BANK_ACC,
       hp.receiver_bank_name AS RECEIVER_BANK_NAME,
       hp.receOremit AS RECEOREMIT,
       hp.pre_hand AS PRE_HAND,
       hp.next_hand AS NEXT_HAND,
       hp.pact_no AS PACT_NO,
       temp.TEMPLATE_ID AS template_id,
       NVL(temp.STATUS, '0') AS nz_status,
       temp.SEND_RESON AS send_reson,
       temp.SEND_BACK_NO AS send_back_no,
       temp.REVERSE2 AS business_type,
       temp.REVERSE1 AS recode_op,
       hp.newStmIssuofpprInd AS newStmIssuofpprInd,
       hp.blgrdbagcrclPrmtInd AS blgrdbagcrclPrmtInd,
       hp.sbbllRngStrtSn AS sbbllRngStrtSn,
       hp.sbbllRngEndSn AS sbbllRngEndSn,
       hp.blStdAmt AS blStdAmt,
       hp.blRmtrUnnIdNo AS blRmtrUnnIdNo
FROM v_erp_receive_emit hp
         LEFT JOIN erp_his_dtl_template temp ON hp.id = temp.SERIAL_ID AND hp.TENANT_ID = temp.TENANT_ID;
/

-- 25. V_NIS_EXPENSE_DETAIL


-- 视图说明：V_NIS_EXPENSE_DETAIL
CREATE OR REPLACE VIEW V_NIS_EXPENSE_DETAIL AS
SELECT a.ID AS id,
       a.EXPENSE_ID AS expense_id,
       a.BANK_ACC AS bank_acc,
       a.ACC_NAME AS acc_name,
       a.BANK_NAME AS bank_name,
       a.BANK_CODE AS bank_code,
       a.purpose AS purpose,
       a.RMK AS rmk,
       a.EXPENSE_MONEY AS expense_money,
       a.DIF_BANK AS dif_bank,
       a.AREA_SIGN AS area_sign,
       d.BIF_CODE AS bif_code,
       e.NAME AS prov,
       f.NAME AS city,
       a.VOUCHER_STAT AS voucher_stat,
       a.CARD_TYPE AS card_type,
       c.VOUCHER_NO AS voucher_no
FROM nis_expense_detail a
         JOIN nis_billhead b ON a.BILL_ID = b.ID AND a.TENANT_ID = b.TENANT_ID
         JOIN bis_exc c ON a.BILL_ID = c.CBS_BILL_ID AND a.TENANT_ID = c.TENANT_ID
         LEFT JOIN bis_bif_init d ON a.BIF_CODE = d.ID
         LEFT JOIN sys_region e ON a.PROV = e.ID
         LEFT JOIN sys_region f ON a.CITY = f.ID;
/

-- 26. V_NIS_PAY_BILL_ALLOCATION


-- 视图说明：V_NIS_PAY_BILL_ALLOCATION
CREATE OR REPLACE VIEW V_NIS_PAY_BILL_ALLOCATION AS
SELECT n.ID AS id,
       n.BILL_NO AS billNo,
       n.CORP_ID AS corpId,
       f.VOUCHER_NAME AS voucherName,
       n.PAYER_ACC AS payerAcc,
       n.PAYER_ACC_NAME AS payerAccName,
       n.CUR_ID AS curId,
       n.WISH_PAYDAY AS wishPayday,
       n.PAY_MONEY AS payMoney,
       n.PAYEE_ACC AS payeeAcc,
       n.PAYEE_ACC_NAME AS payeeAccName,
       n.PURPOSE AS purpose,
       n.BUS_TYPE AS busType,
       n.STATUS AS status,
       'nis_billhead' AS isFrom
FROM nis_billhead n
         LEFT JOIN fc_voucher_type f ON n.VOUCHER_TYPE_ID = f.ID
         LEFT JOIN nis_bus_type t ON n.BUS_TYPE = t.ID
         LEFT JOIN bis_exc b ON n.ID = b.CBS_BILL_ID AND b.VOUCHER_FROM = '1'
         LEFT JOIN bt_bank_acc c ON n.DOWN_ACC_ID = c.ID
WHERE 1 = 1
  AND n.STATUS = CASE WHEN f.IS_ACCOUNT = 1 THEN 100 ELSE 95 END
  AND (n.IS_PAYED = 1 OR (n.IS_PAYED = 0 AND (b.VOUCHER_STAT = '0' OR b.VOUCHER_STAT IS NULL)))
UNION
SELECT n.ID AS id,
       n.WAGE_NO AS billNo,
       n.CORP_ID AS corpId,
       '代发工资' AS voucherName,
       n.PAYER_ACC AS payerAcc,
       n.PAYER_ACC_NAME AS payerAccName,
       n.CUR_ID AS curId,
       n.WISH_PAYDAY AS wishPayday,
       n.WAGES AS payMoney,
       '' AS payeeAcc,
       '' AS payeeAccName,
       '代发工资' AS purpose,
       n.BUS_TYPE AS busType,
       n.STATUS AS status,
       'nis_wage' AS isFrom
FROM nis_wage n
WHERE 1 = 1 AND n.STATUS = 95
UNION
SELECT n.ID AS id,
       n.BILL_NO AS billNo,
       n.CORP_ID AS corpId,
       f.VOUCHER_NAME AS voucherName,
       n.CORP_BANK_ACC AS payerAcc,
       n.CORP_ACC_NAME AS payerAccName,
       n.CUR_ID AS curId,
       NULL AS wishPayday,
       n.PAY_MONEY AS payMoney,
       n.COTP_BANK_ACC AS payeeAcc,
       n.COTP_ACC_NAME AS payeeAccName,
       req.PURPOSE AS purpose,
       pro.PRO_NAME AS busType,
       n.STATUS AS status,
       'fex_cash_flow' AS isFrom
FROM fex_cash_flow n
         LEFT JOIN fc_voucher_type f ON n.VOUCHER_TYPE_ID = f.ID
         LEFT JOIN fex_tran_req req ON req.ID = n.TRAN_BILL_ID
         LEFT JOIN fex_product pro ON req.PRO_ID = pro.ID
WHERE 1 = 1 AND n.STATUS = 95 AND n.PAY_STATUS = '1' AND n.CD_SIGN = '1'
UNION
SELECT n.ID AS id,
       n.BILL_NO AS billNo,
       n.CORP_ID AS corpId,
       CASE n.TYPE WHEN 10 THEN '上划' WHEN 11 THEN '下拨' WHEN 12 THEN '请款' WHEN 13 THEN '头寸调拨' WHEN 14 THEN '上缴' END AS voucherName,
       n.PAYER_ACC AS payerAcc,
       n.PAYER_ACC_NAME AS payerAccName,
       n.CUR_ID AS curId,
       n.WISH_PAYDAY AS wishPayday,
       n.MONEY AS payMoney,
       n.PAYEE_ACC AS payeeAcc,
       n.PAYEE_ACC_NAME AS payeeAccName,
       n.purpose AS purpose,
       NULL AS busType,
       n.STATUS AS status,
       'nis_allocation_req' AS isFrom
FROM nis_allocation_req n
WHERE 1 = 1 AND n.STATUS = '95';
/

-- 27. V_NIS_WAGE


-- 视图说明：V_NIS_WAGE
CREATE OR REPLACE VIEW V_NIS_WAGE AS
SELECT a.ID AS id,
       a.VOUCHER_NO AS voucher_no,
       a.CBS_BILL_ID AS cbs_bill_id,
       a.CORP_ID AS corp_id,
       a.REQ_DATE AS req_date,
       a.PAYER_ACC AS payer_acc,
       a.PAYER_ACC_NAME AS payer_acc_name,
       a.PAYER_BANK AS payer_bank,
       b.NAME AS payer_prov,
       c.NAME AS payer_city,
       a.PAYEE_ACC AS payee_acc,
       e.CUR_CODE AS cur_code,
       a.CUR_ID AS cur_id,
       a.PAY_MONEY AS pay_money,
       f.SYS_VOUCHER_CODE AS sys_voucher_code,
       a.VOUCHER_TYPE_ID AS voucher_type_id,
       a.WISH_PAYDAY AS wish_payday,
       a.IS_QUICK AS is_quick,
       a.TRANS_MODE AS trans_mode,
       d.BIF_CODE AS bif_code,
       a.BIF_CODE AS bif_code_id,
       a.DIF_BANK AS dif_bank,
       a.AREA_SIGN AS area_sign,
       a.PURPOSE AS purpose,
       a.add_word AS add_word,
       a.VOUCHER_FROM AS voucher_from,
       a.BANK_SERIAL_NO AS bank_serial_no,
       a.TRANS_TIME AS trans_time,
       a.COUNTER_ACC AS counter_acc,
       a.VOUCHER_STAT AS voucher_stat,
       a.RESEND_SIGN AS resend_sign,
       a.DELAY_TIME AS delay_time,
       a.CBS_BILL_NO AS cbs_bill_no,
       a.TENANT_ID AS TENANT_ID,
       i.tenant_code AS TENANT_CODE
FROM bis_exc a
         LEFT JOIN sys_region b ON b.ID = a.PAYER_PROV
         LEFT JOIN sys_region c ON c.ID = a.PAYER_CITY
         JOIN bis_bif_init d ON d.ID = a.BIF_CODE
         JOIN bt_currency e ON e.ID = a.CUR_ID AND a.TENANT_ID = e.TENANT_ID
         JOIN fc_voucher_type f ON f.ID = a.VOUCHER_TYPE_ID AND a.TENANT_ID = f.TENANT_ID
         JOIN sys_tenant i ON i.id = a.TENANT_ID
WHERE i.status = 1;
/

-- 28. V_NIS_WAGE_DETAIL


-- 视图说明：V_NIS_WAGE_DETAIL
CREATE OR REPLACE VIEW V_NIS_WAGE_DETAIL AS
SELECT a.ID AS id,
       a.WAGE_DTL_NO AS wage_dtl_no,
       a.BANK_ACC AS bank_acc,
       a.ACC_NAME AS acc_name,
       a.BANK_NAME AS bank_name,
       a.BANK_CODE AS bank_code,
       a.purpose AS purpose,
       a.WAGES_VERIFY AS wages_verify,
       a.DIF_BANK AS dif_bank,
       a.AREA_SIGN AS area_sign,
       d.BIF_CODE AS bif_code,
       e.NAME AS prov,
       f.NAME AS city,
       a.VOUCHER_STAT AS voucher_stat,
       a.WORK_NO AS work_no,
       a.ID_NO AS id_no,
       a.ID_TYPE AS id_type,
       c.VOUCHER_NO AS voucher_no
FROM nis_wage_detail a
         JOIN nis_wage b ON a.WAGE_ID = b.ID AND a.TENANT_ID = b.TENANT_ID
         JOIN bis_exc c ON a.VOUCHER_NO = c.VOUCHER_NO AND a.TENANT_ID = c.TENANT_ID
         LEFT JOIN bis_bif_init d ON c.BIF_CODE = d.ID
         LEFT JOIN sys_region e ON a.PROV = e.ID
         LEFT JOIN sys_region f ON a.CITY = f.ID;
/

-- 29. VIEW_CREATEBUSINESS_INFO


-- 视图说明：VIEW_CREATEBUSINESS_INFO
CREATE OR REPLACE VIEW VIEW_CREATEBUSINESS_INFO AS
SELECT a.TENANT_ID AS TENANT_ID,
       a.BANKINTF_CODE AS bankintf_code,
       a.TRADE_CODE AS trade_code,
       b.TRADE_NAME AS trade_name,
       a.RUNDATE_RANGE AS rundate_range,
       a.RUNTIME_RANGE AS runtime_range,
       a.RUNSECOND_RANGE AS runsecond_range,
       a.TRIGGER_INTEVAL AS trigger_inteval,
       a.TRADE_INTEVAL AS trade_inteval,
       a.THREAD_NUM AS thread_num,
       a.BATCH_NUM AS batch_num,
       a.BEFORE_RANGE AS before_range,
       a.RUN_FLAG AS run_flag,
       b.MONITOR_LQ AS monitor_lq,
       b.DEFAULT_LQ AS default_lq,
       b.TRIGG_IMPLCLASS AS trigg_implclass,
       b.EXCEPTION_TIMERANGE AS exception_timerange,
       b.TRADE_TYPE AS trade_type,
       b.INUSE_FLAG AS inuse_flag,
       b.BATCH_FLAG AS BATCH_FLAG,
       b.MUXTHREAD_FLAG AS MUXTHREAD_FLAG,
       b.TIMERANGE_FLAG AS TIMERANGE_FLAG,
       b.TRADEOF_BANKINTF AS TRADEOF_BANKINTF,
       b.SHOW_ORDER AS show_order
FROM bp_bankinft_trade a
         JOIN bp_trade_baseinfo b ON a.TRADE_CODE = b.TRADE_CODE AND a.TENANT_ID = b.TENANT_ID
ORDER BY b.SHOW_ORDER;
/

-- 30. VIEW_CREATEBUSINESS_INFO_BILL


-- 视图说明：VIEW_CREATEBUSINESS_INFO_BILL
CREATE OR REPLACE VIEW VIEW_CREATEBUSINESS_INFO_BILL AS
SELECT a.TENANT_ID AS TENANT_ID,
       a.BANKINTF_CODE AS bankintf_code,
       a.TRADE_CODE AS trade_code,
       b.TRADE_NAME AS trade_name,
       a.RUNDATE_RANGE AS rundate_range,
       a.RUNTIME_RANGE AS runtime_range,
       a.RUNSECOND_RANGE AS runsecond_range,
       a.TRIGGER_INTEVAL AS trigger_inteval,
       a.TRADE_INTEVAL AS trade_inteval,
       a.THREAD_NUM AS thread_num,
       a.BATCH_NUM AS batch_num,
       a.BEFORE_RANGE AS before_range,
       a.RUN_FLAG AS run_flag,
       b.MONITOR_LQ AS monitor_lq,
       b.DEFAULT_LQ AS default_lq,
       b.TRIGG_IMPLCLASS AS trigg_implclass,
       b.EXCEPTION_TIMERANGE AS exception_timerange,
       b.TRADE_TYPE AS trade_type,
       b.INUSE_FLAG AS inuse_flag,
       b.BATCH_FLAG AS BATCH_FLAG,
       b.MUXTHREAD_FLAG AS MUXTHREAD_FLAG,
       b.TIMERANGE_FLAG AS TIMERANGE_FLAG,
       b.TRADEOF_BANKINTF AS TRADEOF_BANKINTF,
       b.SHOW_ORDER AS show_order
FROM bp_bankinft_trade_bill a
         JOIN bp_trade_baseinfo_bill b ON a.TRADE_CODE = b.TRADE_CODE AND a.TENANT_ID = b.TENANT_ID
ORDER BY b.SHOW_ORDER;
/

-- 31. VIEW_ERP_PAYINFO


-- 视图说明：VIEW_ERP_PAYINFO
CREATE OR REPLACE VIEW VIEW_ERP_PAYINFO AS
SELECT e.TENANT_ID AS TENANT_ID,
       e.SERIAL_NO_ERP AS serial_no_erp,
       e.VOUCHER_NO_ERP AS voucher_no_erp,
       n.BILL_NO AS bill_no,
       n.PAYER_ACC AS payer_acc,
       n.PAYER_ACC_NAME AS payer_acc_name,
       n.PAYER_BANK AS payer_bank,
       n.PAY_MONEY AS pay_money,
       b.TRANS_TIME AS trans_time,
       b.RETURN_TIME AS return_time,
       CASE
           WHEN b.VOUCHER_STAT = '0' THEN 'S'
           WHEN n.STATUS = -2 THEN e.BUS_TYPE
           ELSE 'P'
           END AS status,
       t.RMK AS state_desc,
       e.BACK_DATE AS back_date
FROM erp_payinfo e
         LEFT JOIN nis_billhead n ON n.ERP_ID = e.ID AND n.TENANT_ID = e.TENANT_ID
         LEFT JOIN bis_exc b ON n.ID = b.CBS_BILL_ID AND n.TENANT_ID = b.TENANT_ID
         LEFT JOIN bt_charge_back_type t ON e.BUS_TYPE = t.BUS_TYPE AND e.TENANT_ID = t.TENANT_ID;
/


-- 37. VIEW_WAGE_TEMP


-- 视图说明：VIEW_WAGE_TEMP
CREATE OR REPLACE VIEW VIEW_WAGE_TEMP AS
SELECT t.TENANT_ID AS TENANT_ID,
       t.ID AS ID,
       t.WAGE_DTL_NO AS WAGE_DTL_NO,
       t.WAGE_ID AS WAGE_ID,
       t.WORK_NO AS WORK_NO,
       t.USER_NAME AS USER_NAME,
       t.BANK_ACC AS BANK_ACC,
       t.ACC_NAME AS ACC_NAME,
       t.WAGES_VERIFY AS WAGES_VERIFY,
       t.purpose AS purpose,
       t.PROV AS PROV,
       t.CITY AS CITY,
       t.BANK_CODE AS BANK_CODE,
       t.BANK_NAME AS BANK_NAME,
       t.DIF_BANK AS DIF_BANK,
       t.AREA_SIGN AS AREA_SIGN,
       t.VOUCHER_STAT AS VOUCHER_STAT,
       t.RETURN_TIME AS RETURN_TIME,
       t.FIRST_RETURN_MSG AS FIRST_RETURN_MSG,
       t.NEXT_RETURN_MSG AS NEXT_RETURN_MSG,
       t.CREATE_BY AS CREATE_BY,
       t.CREATE_DATE AS CREATE_DATE,
       t.UPDATE_BY AS UPDATE_BY,
       t.UPDATE_DATE AS UPDATE_DATE,
       t.VOUCHER_NO AS VOUCHER_NO,
       s.WAGES AS amt
FROM nis_wage_detail t
         JOIN erp_wage_detail s ON t.ID = s.ID AND t.TENANT_ID = s.TENANT_ID;
/

-- 32. VIEW_ERP_WAGE


-- 视图说明：VIEW_ERP_WAGE
CREATE OR REPLACE VIEW VIEW_ERP_WAGE AS
SELECT e.TENANT_ID AS TENANT_ID,
       e.SERIAL_NO_ERP AS serial_no_erp,
       e.VOUCHER_NO_ERP AS voucher_no_erp,
       n.WAGE_NO AS wage_no,
       d.WAGE_DTL_NO AS wage_dtl_no,
       d.ID AS id,
       d.amt AS amt,
       n.PAYER_ACC AS payer_acc,
       n.PAYER_ACC_NAME AS payer_acc_name,
       e.PAYER_BANK_NAME AS payer_bank,
       n.WAGES AS wages,
       b.TRANS_TIME AS trans_time,
       b.RETURN_TIME AS return_time,
       CASE
           WHEN d.VOUCHER_STAT = '0' THEN 'S'
           WHEN n.STATUS = -2 THEN e.BUS_TYPE
           ELSE 'P'
           END AS status,
       t.RMK AS state_desc,
       e.BACK_DATE AS back_date
FROM erp_payinfo e
         LEFT JOIN nis_wage n ON n.ERP_ID = e.ID AND n.TENANT_ID = e.TENANT_ID
         LEFT JOIN bt_charge_back_type t ON e.BUS_TYPE = t.BUS_TYPE AND e.TENANT_ID = t.TENANT_ID
         LEFT JOIN bis_exc b ON n.ID = b.CBS_BILL_ID AND n.TENANT_ID = b.TENANT_ID
         LEFT JOIN view_wage_temp d ON n.ID = d.WAGE_ID AND n.TENANT_ID = d.TENANT_ID
WHERE e.VOUCHER_TYPE = '48';
/

-- 33. VIEW_NIS_CASH_BILL


-- 视图说明：VIEW_NIS_CASH_BILL
CREATE OR REPLACE VIEW VIEW_NIS_CASH_BILL AS
SELECT n.TENANT_ID AS TENANT_ID,
       n.ID AS id,
       n.BILL_NO AS bill_no,
       n.CORP_ID AS corp_id,
       c.NAME AS corp_name,
       n.VOUCHER_TYPE_ID AS voucher_type_id,
       t.VOUCHER_TYPE AS voucher_type,
       t.VOUCHER_NAME AS voucher_name,
       n.ACC_ID AS acc_id,
       n.PAYER_ACC AS payer_acc,
       n.PAYER_ACC_NAME AS payer_acc_name,
       n.PAYER_BANK AS payer_bank,
       n.CUR_ID AS cur_id,
       cur.CUR_NAME AS cur_name,
       n.PAY_MONEY AS pay_money,
       n.PAYEE_ACC AS payee_acc,
       n.PAYEE_ACC_NAME AS payee_acc_name,
       n.PAYEE_BANK_CODE AS payee_bank_code,
       n.PAYEE_BANK AS payee_bank,
       r.PROV_INPUT_NAME AS prov,
       r.NAME AS city,
       n.WISH_PAYDAY AS wish_payday,
       n.IS_QUICK AS is_quick,
       n.IS_FORINDIVIDUAL AS is_forindividual,
       n.BIF_CODE AS bif_code,
       b.NAME AS bif_name,
       n.DIF_BANK AS dif_bank,
       n.AREA_SIGN AS area_sign,
       n.BURSAR_BILL_NO AS bursar_bill_no,
       n.PURPOSE AS purpose,
       'nisBillheadPrintServiceImpl' AS servicebean
FROM nis_billhead n
         LEFT JOIN sys_corp c ON n.CORP_ID = c.ID AND n.TENANT_ID = c.TENANT_ID
         LEFT JOIN fc_voucher_type t ON n.VOUCHER_TYPE_ID = t.ID AND n.TENANT_ID = t.TENANT_ID
         LEFT JOIN bt_currency cur ON n.CUR_ID = cur.ID AND n.TENANT_ID = cur.TENANT_ID
         LEFT JOIN sys_region r ON n.CITY = r.ID AND n.TENANT_ID = r.TENANT_ID
         LEFT JOIN bis_bif_init b ON n.BIF_CODE = b.ID AND n.TENANT_ID = b.TENANT_ID
WHERE n.STATUS = '95' AND t.SYS_VOUCHER_CODE = '62';
/

-- 34. VIEW_NIS_TELE_BILL


-- 视图说明：VIEW_NIS_TELE_BILL
CREATE OR REPLACE VIEW VIEW_NIS_TELE_BILL AS
SELECT n.TENANT_ID AS TENANT_ID,
       n.ID AS id,
       n.BILL_NO AS bill_no,
       n.CORP_ID AS corp_id,
       c.NAME AS corp_name,
       n.VOUCHER_TYPE_ID AS voucher_type_id,
       t.VOUCHER_TYPE AS voucher_type,
       t.VOUCHER_NAME AS voucher_name,
       n.ACC_ID AS acc_id,
       n.PAYER_ACC AS payer_acc,
       n.PAYER_ACC_NAME AS payer_acc_name,
       n.PAYER_BANK AS payer_bank,
       n.CUR_ID AS cur_id,
       cur.CUR_NAME AS cur_name,
       n.PAY_MONEY AS pay_money,
       n.PAYEE_ACC AS payee_acc,
       n.PAYEE_ACC_NAME AS payee_acc_name,
       n.PAYEE_BANK_CODE AS payee_bank_code,
       n.PAYEE_BANK AS payee_bank,
       r.PROV_INPUT_NAME AS prov,
       r.NAME AS city,
       n.WISH_PAYDAY AS wish_payday,
       n.IS_QUICK AS is_quick,
       n.IS_FORINDIVIDUAL AS is_forindividual,
       n.BIF_CODE AS bif_code,
       b.NAME AS bif_name,
       n.DIF_BANK AS dif_bank,
       n.AREA_SIGN AS area_sign,
       n.BURSAR_BILL_NO AS bursar_bill_no,
       n.PURPOSE AS purpose,
       'nisBillheadPrintServiceImpl' AS servicebean
FROM nis_billhead n
         LEFT JOIN sys_corp c ON n.CORP_ID = c.ID AND n.TENANT_ID = c.TENANT_ID
         LEFT JOIN fc_voucher_type t ON n.VOUCHER_TYPE_ID = t.ID AND n.TENANT_ID = t.TENANT_ID
         LEFT JOIN bt_currency cur ON n.CUR_ID = cur.ID AND n.TENANT_ID = cur.TENANT_ID
         LEFT JOIN sys_region r ON n.CITY = r.ID AND n.TENANT_ID = r.TENANT_ID
         LEFT JOIN bis_bif_init b ON n.BIF_CODE = b.ID AND n.TENANT_ID = b.TENANT_ID
WHERE n.STATUS = '95' AND t.SYS_VOUCHER_CODE = '06';
/

-- 35. VIEW_NIS_TRAN_BILL


-- 视图说明：VIEW_NIS_TRAN_BILL
CREATE OR REPLACE VIEW VIEW_NIS_TRAN_BILL AS
SELECT n.TENANT_ID AS TENANT_ID,
       n.ID AS id,
       n.BILL_NO AS bill_no,
       n.CORP_ID AS corp_id,
       c.NAME AS corp_name,
       n.VOUCHER_TYPE_ID AS voucher_type_id,
       t.VOUCHER_TYPE AS voucher_type,
       t.VOUCHER_NAME AS voucher_name,
       n.ACC_ID AS acc_id,
       n.PAYER_ACC AS payer_acc,
       n.PAYER_ACC_NAME AS payer_acc_name,
       n.PAYER_BANK AS payer_bank,
       n.CUR_ID AS cur_id,
       cur.CUR_NAME AS cur_name,
       n.PAY_MONEY AS pay_money,
       n.PAYEE_ACC AS payee_acc,
       n.PAYEE_ACC_NAME AS payee_acc_name,
       n.PAYEE_BANK_CODE AS payee_bank_code,
       n.PAYEE_BANK AS payee_bank,
       r.PROV_INPUT_NAME AS prov,
       r.NAME AS city,
       n.WISH_PAYDAY AS wish_payday,
       n.IS_QUICK AS is_quick,
       n.IS_FORINDIVIDUAL AS is_forindividual,
       n.BIF_CODE AS bif_code,
       b.NAME AS bif_name,
       n.DIF_BANK AS dif_bank,
       n.AREA_SIGN AS area_sign,
       n.BURSAR_BILL_NO AS bursar_bill_no,
       n.PURPOSE AS purpose,
       'nisBillheadPrintServiceImpl' AS servicebean
FROM nis_billhead n
         LEFT JOIN sys_corp c ON n.CORP_ID = c.ID AND n.TENANT_ID = c.TENANT_ID
         LEFT JOIN fc_voucher_type t ON n.VOUCHER_TYPE_ID = t.ID AND n.TENANT_ID = t.TENANT_ID
         LEFT JOIN bt_currency cur ON n.CUR_ID = cur.ID AND n.TENANT_ID = cur.TENANT_ID
         LEFT JOIN sys_region r ON n.CITY = r.ID AND n.TENANT_ID = r.TENANT_ID
         LEFT JOIN bis_bif_init b ON n.BIF_CODE = b.ID AND n.TENANT_ID = b.TENANT_ID
WHERE n.STATUS = '95' AND t.SYS_VOUCHER_CODE = '63';
/

-- 36. VIEW_PAYEE_CHEQUE


-- 视图说明：VIEW_PAYEE_CHEQUE
CREATE OR REPLACE VIEW VIEW_PAYEE_CHEQUE AS
SELECT p.TENANT_ID AS TENANT_ID,
       p.ID AS id,
       p.CORP_ID AS corp_id,
       c.CODE AS corpCode,
       c.NAME AS corpName,
       p.ORIG_NO AS origNo,
       p.BILL_MONEY AS billMoney,
       p.INPUT_DATE AS inputDate,
       p.PAYER AS payer,
       p.PAYER_ACC AS payerAcc,
       p.PAYER_BANK_NO AS payerBankNo,
       p.PAYER_BANK_NAME AS payerBankName,
       p.PAYEE AS payee,
       p.PAYEE_ACC AS payeeAcc,
       p.PAYEE_BANK_NO AS payeeBankNo,
       p.PAYEE_BANK_NAME AS payeeBankName,
       p.USE AS USE_RMK,
       p.START_DATE AS startDate,
       p.END_DATE AS endDate,
       cur.CUR_NAME AS curName,
       p.CUR_EXRATE AS curExrate,
       (NVL(p.CUR_EXRATE, 1) * p.BILL_MONEY) AS curMoney
FROM bv_payee_cheque p
         LEFT JOIN sys_corp c ON p.CORP_ID = c.ID AND p.TENANT_ID = c.TENANT_ID
         LEFT JOIN bt_currency cur ON cur.ID = p.CUR_ID AND cur.TENANT_ID = p.TENANT_ID
WHERE p.STATUS = 5;
/
