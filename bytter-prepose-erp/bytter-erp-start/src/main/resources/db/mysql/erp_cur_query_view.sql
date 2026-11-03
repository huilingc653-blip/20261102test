-- 修改日期：2026-05-06
-- 修改人员：zhangx
-- 修改内容：币别信息查询接口视图
CREATE OR REPLACE VIEW ERP_CUR_QUERY_VIEW AS
SELECT
    c.id as id,
    c.TENANT_ID AS tenantId,
    c.cur_code as curCode,
    c.cur_name as curName,
    c.english_code as englishCode,
    c.cur_exrate as curExrate,
    c.valid_sign as validSign,
    TO_CHAR(
            IFNULL(c.update_date, IFNULL(c.create_date, SYSDATE())),
            'yyyy-mm-dd'
    ) AS updateDate
FROM BT_CURRENCY c
;