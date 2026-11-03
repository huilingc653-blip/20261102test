-- 修改日期：2026-04-30
-- 修改人员：zhangx
-- 修改内容：客商账号息查询接口视图
CREATE OR REPLACE VIEW ERP_EXTERNAL_CORP_ACC_QUERY_VIEW AS
SELECT b.id AS "id", c.TENANT_ID AS "tenantId", c.code AS "code", c.name AS "name",
       c.external_type AS "externalType", c.status AS "status", b.external_acc AS "externalAcc",
       b.bank_sourcecode AS "bankCode", b.bank AS "bankName"
FROM sys_external_corp c, sys_external_corp_bankacc b
WHERE c.id = b.external_corp_id;
