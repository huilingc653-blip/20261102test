-- 修改日期：2026-04-30
-- 修改人员：zhangx
-- 修改内容：客商信息查询接口视图
CREATE OR REPLACE VIEW ERP_EXTERNAL_CORP_QUERY_VIEW AS
SELECT c.id AS "id", c.TENANT_ID AS "tenantId", c.code AS "code", c.name AS "name",
       c.external_type AS "externalType", c.bw_type AS "bwType", c.status AS "status"
FROM sys_external_corp c;
