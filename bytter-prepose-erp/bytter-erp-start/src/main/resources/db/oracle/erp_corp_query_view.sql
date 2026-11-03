-- 修改日期：2026-04-30
-- 修改人员：zhangx
-- 修改内容：单位信息查询接口视图
CREATE OR REPLACE VIEW ERP_CORP_QUERY_VIEW AS
SELECT c.TENANT_ID AS "tenantId", c.CODE AS "code", c.NAME AS "name", c.STATUS AS "status",
       TO_CHAR(NVL(c.UPDATE_TIME, c.CREATE_TIME), 'YYYY-MM-DD') AS "updateDate"
FROM sys_corp c;
