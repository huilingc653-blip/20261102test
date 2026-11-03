-- ============================================================================
-- SaaS 标准化初始化脚本
-- 数据库：Oracle
-- 阶段：08 内管业务数据
-- 来源：bytter-management-dml.sql + init_ng_management_dml.sql（手工固化）
-- 生成日期：2026-07-29
-- 注意：本文件由原始脚本整理生成；禁止反向覆盖 sql/saas/oracle。
-- ============================================================================

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dce5';
IF v_COUNT < 1 THEN
        INSERT INTO SYS_MENU_MANAGER (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
        VALUES ('96ebc4bde88940e283cb504fb6c3dce5', null, '客户银行借款明细查询', '客户银行借款明细查询', 'dac60fdbaec84e869bd78f07dbf0e2f7', null, 500, 1, '/report/loanInfoReport/list', null, null, TIMESTAMP '2026-08-26 17:26:04', '402880425b6b0a58015b6b0abeb10000', TIMESTAMP '2026-08-26 17:38:20', '402880425b6b0a58015b6b0abeb10000', 0, 0, null);

   END IF;
COMMIT;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_resources_manager WHERE PERMISSION = 'sys:loanInfo:report:query';
IF v_COUNT < 1 THEN
        INSERT INTO SYS_RESOURCES_MANAGER (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY)
   VALUES ('108cbeee0a1443d6b0fcf8b00f52002f', null, '查询资源', 'sys:loanInfo:report:query', 1, '96ebc4bde88940e283cb504fb6c3dce5', TIMESTAMP '2026-08-26 17:32:01', '402880425b6b0a58015b6b0abeb10000', null, null);

END IF;
COMMIT;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dcff';
IF v_COUNT < 1 THEN
        INSERT INTO SYS_MENU_MANAGER (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
        VALUES ('96ebc4bde88940e283cb504fb6c3dcff', null, '客户账户交易明细查询', '客户账户交易明细查询', 'dac60fdbaec84e869bd78f07dbf0e2f7', null, 500, 1, '/report/bankAccDtlReport/list', null, null, TIMESTAMP '2026-08-26 17:26:04', '402880425b6b0a58015b6b0abeb10000', TIMESTAMP '2026-08-26 17:38:20', '402880425b6b0a58015b6b0abeb10000', 0, 0, null);

END IF;
COMMIT;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_resources_manager WHERE PERMISSION = 'sys:bankAccDtl:report:query';
IF v_COUNT < 1 THEN
        INSERT INTO SYS_RESOURCES_MANAGER (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY)
   VALUES ('108cbeee0a1443d6b0fcf8b00f5200ff', null, '查询资源', 'sys:bankAccDtl:report:query', 1, '96ebc4bde88940e283cb504fb6c3dcff', TIMESTAMP '2026-08-26 17:32:01', '402880425b6b0a58015b6b0abeb10000', null, null);

END IF;
COMMIT;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dckk';
IF v_COUNT < 1 THEN
        INSERT INTO SYS_MENU_MANAGER (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
        VALUES ('96ebc4bde88940e283cb504fb6c3dckk', null, '客户账户余额查询', '客户账户余额查询', 'dac60fdbaec84e869bd78f07dbf0e2f7', null, 500, 1, '/report/bisAccBalReport/list', null, null, TIMESTAMP '2026-08-26 17:26:04', '402880425b6b0a58015b6b0abeb10000', TIMESTAMP '2026-08-26 17:38:20', '402880425b6b0a58015b6b0abeb10000', 0, 0, null);

END IF;
COMMIT;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_resources_manager WHERE PERMISSION = 'sys:bisaccbal:report:query';
IF v_COUNT < 1 THEN
        INSERT INTO SYS_RESOURCES_MANAGER (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY)
   VALUES ('108cbeee0a1443d6b0fcf8b00f5200kk', null, '查询资源', 'sys:bisaccbal:report:query', 1, '96ebc4bde88940e283cb504fb6c3dckk', TIMESTAMP '2026-08-26 17:32:01', '402880425b6b0a58015b6b0abeb10000', null, null);

END IF;
COMMIT;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dcpp';
IF v_COUNT < 1 THEN
        INSERT INTO SYS_MENU_MANAGER (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
        VALUES ('96ebc4bde88940e283cb504fb6c3dcpp', null, '用户活跃度统计', '用户活跃度统计', 'dac60fdbaec84e869bd78f07dbf0e2f7', null, 500, 1, '/report/userActivityReport/list', null, null, TIMESTAMP '2026-08-26 17:26:04', '402880425b6b0a58015b6b0abeb10000', TIMESTAMP '2026-08-26 17:38:20', '402880425b6b0a58015b6b0abeb10000', 0, 0, null);

END IF;
COMMIT;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_resources_manager WHERE PERMISSION = 'sys:userActivity:report:query';
IF v_COUNT < 1 THEN
        INSERT INTO SYS_RESOURCES_MANAGER (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY)
   VALUES ('108cbeee0a1443d6b0fcf8b00f5200pp', null, '查询资源', 'sys:userActivity:report:query', 1, '96ebc4bde88940e283cb504fb6c3dcpp', TIMESTAMP '2026-08-26 17:32:01', '402880425b6b0a58015b6b0abeb10000', null, null);

END IF;
COMMIT;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY = 'sys.tenant.corp.num.over.limit' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
        INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
        VALUES (LOWER(RAWTOHEX(SYS_GUID())), 'zh_CN', '当前租户网点/单位数量：{0}; 需要添加网点/单位数量：{1};客户签约时设置的网点/单位数量：{2};已超出客户签约时设置的网点/单位数量，请确认', 'sys.tenant.corp.num.over.limit');
END IF;
COMMIT;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY = 'sys.tenant.account.num.over.limit' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
        INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
        VALUES (LOWER(RAWTOHEX(SYS_GUID())), 'zh_CN', '当前租户账户数量：{0}; 需要添加账户数量：{1}; 客户签约时设置的账户数量：{2}; 已超出客户签约时设置的账户数量，请确认', 'sys.tenant.account.num.over.limit');
END IF;
COMMIT;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY = 'sys.tenant.user.num.over.limit' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
        INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
        VALUES (LOWER(RAWTOHEX(SYS_GUID())), 'zh_CN', '当前租户用户数量：{0}; 需要添加用户数量：{1}; 客户签约时设置的用户数量：{2}; 已超出客户签约时设置的用户数量，请确认', 'sys.tenant.user.num.over.limit');
END IF;
COMMIT;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
    -- sys.tenant.corp.number.min
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY = 'sys.tenant.corp.number.min' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
        INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
        VALUES (LOWER(RAWTOHEX(SYS_GUID())), 'zh_CN', '租户已存在单位数量：{0},不能小于这个数', 'sys.tenant.corp.number.min');
END IF;

    -- sys.tenant.user.number.min
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY = 'sys.tenant.user.number.min' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
        INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
        VALUES (LOWER(RAWTOHEX(SYS_GUID())), 'zh_CN', '租户已存在用户数量：{0},不能小于这个数', 'sys.tenant.user.number.min');
END IF;

    -- sys.tenant.bankacc.number.min
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY = 'sys.tenant.bankacc.number.min' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
        INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
        VALUES (LOWER(RAWTOHEX(SYS_GUID())), 'zh_CN', '租户已存在银行账户数量：{0},不能小于这个数', 'sys.tenant.bankacc.number.min');
END IF;

COMMIT;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.UnitPermReview' and LANG_CODE = 'en_US';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('ecc2db206667462b84664b3bbaaf3c88', 'en_US', 'Unit special permission review', 'replenish.UnitPermReview' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.UnitPermReview' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('f067fdcfa6354d62a5d0ab68352bcee4', 'zh_CN', '单位特殊权限复核', 'replenish.UnitPermReview' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.UserInitiated' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('1644ee62b4a74ff1abd356a0a9835d91', 'zh_CN', '用户进行', 'replenish.UserInitiated' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.UserInitiated' and LANG_CODE = 'en_US';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('df1f7c081fcc439dbdb7ad0b5e65b864', 'en_US', 'User initiated', 'replenish.UserInitiated' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.UserPwd' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('82ba140d48da4e23ab82bb101f840012', 'zh_CN', '用户密码', 'replenish.UserPwd' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.UserPwd' and LANG_CODE = 'en_US';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('8e10dd101c0c4780bd0c7c9a13ef1f38', 'en_US', 'User password', 'replenish.UserPwd' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.MarkNew' and LANG_CODE = 'en_US';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('11b9a765ab9b49778704c499630bb634', 'en_US', 'Mark as new', 'replenish.MarkNew' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.MarkNew' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('fa4b86cb6c594b45b4fc3c7a04c71933', 'zh_CN', '为新增', 'replenish.MarkNew' );
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.Green' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('806f286039fe478ba997dc94a95659b3', 'zh_CN', '绿色', 'replenish.Green' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.Green' and LANG_CODE = 'en_US';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('9a4728b9992a4016ba94c1cd94a60b92', 'en_US', 'Green', 'replenish.Green' );
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.Red' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('539be1efebf24b398099af1438b8db9c', 'zh_CN', '红色', 'replenish.Red' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.Red' and LANG_CODE = 'en_US';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('7a13a3466f2f4d9fb42527dd3176d029', 'en_US', 'Red', 'replenish.Red' );
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.MarkDelete' and LANG_CODE = 'en_US';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('8bcdadeb2bfd4f4381d6bf43489ea0d4', 'en_US', 'Mark for deletion', 'replenish.MarkDelete' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.MarkDelete' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('f27f58b53919425cb70c1051eed5b4d5', 'zh_CN', '为删除', 'replenish.MarkDelete' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM SYS_NOTIFICATION_CATEGORY_M WHERE ID = 'a4ebd29193c544abb60e51447256812c';
IF v_COUNT < 1 THEN
    INSERT INTO SYS_NOTIFICATION_CATEGORY_M (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, "TYPE", STAR_TARGET, NOTIFY_OBJECT_TYPE, EMAIL_IND)
    VALUES ('a4ebd29193c544abb60e51447256812c', '3d6dd6736e2b466e91d85b715b82a9c3', '系统内管端配置修改', 'sys:tenant:record:update', 'bda2047eb9cb42e39d7670e43ea5e828', '/sys/tenant/list', NULL, '尊敬的管理员，系统内部管理配置已更新： &bull; 用户数量上限：$!{userNumber} &bull; 单位数量上限：$!{corpNumber} &bull; 银行账户数量上限：$!{accountNumber}', 1, 1, '您有一条系统内管端配置修改,请查看!', 1, NULL, 0, 0);
END IF;
COMMIT;
END;
/
