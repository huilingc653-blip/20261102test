-- 修改日期：2026-04-28
-- 修改人员：zhangx
-- 修改内容：预算科目查询接口视图
create or replace view ERP_ITEM_QUERY_VIEW as
SELECT
       i.ID AS id,
       i.TENANT_ID AS tenantId,
       i.ITEM_CODE AS itemCode,
       i.ITEM_NAME AS itemName
FROM fbs_item i
;