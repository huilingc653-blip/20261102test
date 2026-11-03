-- 资金结算量统计表：补齐四个查询入口共用取数接口的权限地址。
-- 保留现有资源 ID、权限码、原 URL 和用户授权关系；不新增权限、不修改其他模块规则。
-- 本脚本供存量 SaaS 环境单独执行，不执行初始化建表脚本。
-- SQL*Plus / SQLcl 执行；执行前保存下方查询结果。
WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK
SET DEFINE OFF

UPDATE SYS_RESOURCES
SET URL = CASE
    WHEN TRIM(URL) IS NULL THEN '/report/allocationsettlement/getData'
    ELSE TRIM(URL) || ',' || '/report/allocationsettlement/getData'
END
WHERE PERMISSION IN (
    'netbank:report:allocationsettlement:query',
    'report:allocationsettlement:acclist:query',
    'report:allocationsettlement:accountbanklist:query'
)
AND INSTR(',' || REPLACE(COALESCE(URL, ''), ' ', '') || ',', ',/report/allocationsettlement/getData,') = 0;

-- 同步已部署环境的租户预置资源，后续开通租户继续沿用原授权结构。
UPDATE TENANT_PRE_RESOURCES
SET URL = CASE
    WHEN TRIM(URL) IS NULL THEN '/report/allocationsettlement/getData'
    ELSE TRIM(URL) || ',' || '/report/allocationsettlement/getData'
END
WHERE PERMISSION IN (
    'netbank:report:allocationsettlement:query',
    'report:allocationsettlement:acclist:query',
    'report:allocationsettlement:accountbanklist:query'
)
AND INSTR(',' || REPLACE(COALESCE(URL, ''), ' ', '') || ',', ',/report/allocationsettlement/getData,') = 0;

COMMIT;
