-- 修改日期：2026-04-29
-- 修改人员：zhangx
-- 修改内容：银行联行号查询接口视图
CREATE OR REPLACE VIEW ERP_BANK_INPUT_QUERY_VIEW AS
SELECT b.TENANT_ID AS "tenantId", b.ID AS "id", b.SYS_BANK_CODE AS "bankCode",
       b.BANK_NAME AS "bankName", r.BANK_PROV AS "provCode", r.PROV_INPUT_NAME AS "provName",
       r.BANK_INPUT_CITY AS "cityCode", r.NAME AS "cityName",
       TO_CHAR(NVL(b.UPDATE_DATE, SYSDATE), 'YYYY-MM-DD') AS "updateDate"
FROM bt_input_bank_info b
INNER JOIN sys_bank_region br ON b.BANK_CITY_CODE = br.BANK_CITY AND b.TENANT_ID = br.TENANT_ID
INNER JOIN sys_region r ON br.BANK_INPUT_CITY = r.BANK_INPUT_CITY AND br.TENANT_ID = r.TENANT_ID;
