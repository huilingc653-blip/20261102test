-- 修改日期：2026-04-28
-- 修改人员：zhangx
-- 修改内容：银行账户查询接口视图
CREATE OR REPLACE VIEW ERP_BANK_ACC_QUERY_VIEW AS
SELECT a.TENANT_ID AS "tenantId", a.ID AS "id", a.BANK_ACC AS "bankAcc",
       a.ACC_NAME AS "accName", sc.CODE AS "corpCode", sc.NAME AS "corpName",
       d.CUR_CODE AS "curCode", d.CUR_NAME AS "curName", a.BANK_CODE AS "bankCode",
       a.BANK_NAME AS "bankName", a.IS_ONLINE AS "isOnline", a.VALID_SIGN AS "validSign",
       TO_CHAR(NVL(a.UPDATE_DATE, SYSDATE), 'YYYY-MM-DD') AS "updateDate"
FROM bt_bank_acc a
LEFT JOIN sys_corp sc ON a.CORP_ID = sc.ID AND a.TENANT_ID = sc.TENANT_ID
LEFT JOIN bt_bank_acc_cur c ON a.ID = c.BANK_ACC_ID AND a.TENANT_ID = c.TENANT_ID
LEFT JOIN bt_currency d ON c.CUR_ID = d.ID AND c.TENANT_ID = d.TENANT_ID;
