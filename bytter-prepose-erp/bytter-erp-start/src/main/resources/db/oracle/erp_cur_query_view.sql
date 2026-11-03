-- 修改日期：2026-05-06
-- 修改人员：zhangx
-- 修改内容：币别信息查询接口视图
CREATE OR REPLACE VIEW ERP_CUR_QUERY_VIEW AS
SELECT c.id AS "id", c.TENANT_ID AS "tenantId", c.cur_code AS "curCode", c.cur_name AS "curName",
       c.english_code AS "englishCode", c.cur_exrate AS "curExrate", c.valid_sign AS "validSign",
       TO_CHAR(NVL(c.update_date, NVL(c.create_date, SYSDATE)), 'YYYY-MM-DD') AS "updateDate"
FROM BT_CURRENCY c;
