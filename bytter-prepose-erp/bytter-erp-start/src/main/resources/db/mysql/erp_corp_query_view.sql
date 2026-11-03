-- 修改日期：2026-04-30
-- 修改人员：zhangx
-- 修改内容：单位信息查询接口视图
create or replace view ERP_CORP_QUERY_VIEW as
SELECT
    c.TENANT_ID AS tenantId,
    c.CODE AS code,
    c.NAME AS name,
    c.STATUS AS status,
    DATE_FORMAT(IFNULL(c.UPDATE_TIME, c.CREATE_TIME), '%Y-%m-%d') AS updateDate
FROM sys_corp c
;