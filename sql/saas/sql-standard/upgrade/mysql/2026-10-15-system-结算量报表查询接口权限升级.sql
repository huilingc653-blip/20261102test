-- 资金结算量统计表：补齐四个查询入口共用取数接口的权限地址。
-- 保留现有资源 ID、权限码、原 URL 和用户授权关系；不新增权限、不修改其他模块规则。
-- 本脚本供存量 SaaS 环境单独执行，不执行初始化建表脚本。
-- 执行前保存下方查询结果；执行工具遇错应停止并回滚事务。

START TRANSACTION;

UPDATE sys_resources
SET URL = CONCAT_WS(',', NULLIF(TRIM(URL), ''), '/report/allocationsettlement/getData')
WHERE PERMISSION IN (
    'netbank:report:allocationsettlement:query',
    'report:allocationsettlement:acclist:query',
    'report:allocationsettlement:accountbanklist:query'
)
AND FIND_IN_SET('/report/allocationsettlement/getData', REPLACE(COALESCE(URL, ''), ' ', '')) = 0;

-- 同步已部署环境的租户预置资源，后续开通租户继续沿用原授权结构。
UPDATE tenant_pre_resources
SET URL = CONCAT_WS(',', NULLIF(TRIM(URL), ''), '/report/allocationsettlement/getData')
WHERE PERMISSION IN (
    'netbank:report:allocationsettlement:query',
    'report:allocationsettlement:acclist:query',
    'report:allocationsettlement:accountbanklist:query'
)
AND FIND_IN_SET('/report/allocationsettlement/getData', REPLACE(COALESCE(URL, ''), ' ', '')) = 0;

COMMIT;
