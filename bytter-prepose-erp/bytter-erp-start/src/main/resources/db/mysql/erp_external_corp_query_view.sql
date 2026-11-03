-- 修改日期：2026-04-30
-- 修改人员：zhangx
-- 修改内容：客商信息查询接口视图
create or replace view ERP_EXTERNAL_CORP_QUERY_VIEW as
SELECT c.id as id,
       c.TENANT_ID AS tenantId,
       c.code as code,
       c.name as name,
       c.external_type as externalType,
       c.bw_type as bwType,
       c.status as status
FROM sys_external_corp c
ORDER BY c.id
;