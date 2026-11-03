-- ============================================================================
-- SaaS 标准化初始化脚本
-- 数据库：MySQL
-- 阶段：08 内管业务数据
-- 来源：bytter-management-dml.sql + init_ng_management_dml.sql（手工固化）
-- 生成日期：2026-07-29
-- 注意：本文件由原始脚本整理生成；禁止反向覆盖 sql/saas/mysql。
-- ============================================================================

INSERT INTO sys_menu_manager(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '96ebc4bde88940e283cb504fb6c3dce5', NULL, '客户银行借款明细查询', '客户银行借款明细查询', 'dac60fdbaec84e869bd78f07dbf0e2f7', NULL, 500, 1, '/report/loanInfoReport/list', NULL, NUll, NULL, NULL, NULL, NULL, 0, 0, NULL FROM DUAL
WHERE NOT EXISTS(
    SELECT 1 FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dce5' LIMIT 1
);

insert into sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '108cbeee0a1443d6b0fcf8b00f52002f', '查询资源', '查询资源', 'sys:loanInfo:report:query', 1, '96ebc4bde88940e283cb504fb6c3dce5' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID='108cbeee0a1443d6b0fcf8b00f52002f' LIMIT 1
);

INSERT INTO sys_menu_manager(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '96ebc4bde88940e283cb504fb6c3dcff', NULL, '客户账户交易明细查询', '客户账户交易明细查询', 'dac60fdbaec84e869bd78f07dbf0e2f7', NULL, 500, 1, '/report/bankAccDtlReport/list', NULL, NUll, NULL, NULL, NULL, NULL, 0, 0, NULL FROM DUAL
WHERE NOT EXISTS(
    SELECT 1 FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dcff' LIMIT 1
);

insert into sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '108cbeee0a1443d6b0fcf8b00f5200ff', '查询资源', '查询资源', 'sys:bankAccDtl:report:query', 1, '96ebc4bde88940e283cb504fb6c3dcff' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID='108cbeee0a1443d6b0fcf8b00f5200ff' LIMIT 1
);

INSERT INTO sys_menu_manager(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '96ebc4bde88940e283cb504fb6c3dckk', NULL, '客户账户余额查询', '客户账户余额查询', 'dac60fdbaec84e869bd78f07dbf0e2f7', NULL, 500, 1, '/report/bisAccBalReport/list', NULL, NUll, NULL, NULL, NULL, NULL, 0, 0, NULL FROM DUAL
WHERE NOT EXISTS(
    SELECT 1 FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dckk' LIMIT 1
);

insert into sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '108cbeee0a1443d6b0fcf8b00f5200kk', '查询资源', '查询资源', 'sys:bisaccbal:report:query', 1, '96ebc4bde88940e283cb504fb6c3dckk' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID='108cbeee0a1443d6b0fcf8b00f5200kk' LIMIT 1
);

INSERT INTO sys_menu_manager(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '96ebc4bde88940e283cb504fb6c3dcpp', NULL, '用户活跃度统计', '用户活跃度统计', 'dac60fdbaec84e869bd78f07dbf0e2f7', NULL, 500, 1, '/report/userActivityReport/list', NULL, NUll, NULL, NULL, NULL, NULL, 0, 0, NULL FROM DUAL
WHERE NOT EXISTS(
    SELECT 1 FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dcpp' LIMIT 1
);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8ad2a64b4cdb4e738dd9da5aef8a42b9', 'zh_CN', '标记为已读', 'sys.notification.markRead'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'sys.notification.markRead' AND LANG_CODE = 'zh_CN'
);

insert into sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '108cbeee0a1443d6b0fcf8b00f5200pp', '查询资源', '查询资源', 'sys:userActivity:report:query', 1, '96ebc4bde88940e283cb504fb6c3dcpp' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID='108cbeee0a1443d6b0fcf8b00f5200pp' LIMIT 1
);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f391208573a4497891726bc847202179','zh_CN','是否关闭多租户模式','sys.config.i18n.isCloseMultiTenantMode'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang
    WHERE LANG_KEY = 'sys.config.i18n.isCloseMultiTenantMode' AND LANG_CODE = 'zh_CN'
);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ecc2db206667462b84664b3bbaaf3c88', 'en_US', 'Unit special permission review', 'replenish.UnitPermReview' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UnitPermReview' AND LANG_CODE = 'en_US' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f067fdcfa6354d62a5d0ab68352bcee4', 'zh_CN', '单位特殊权限复核', 'replenish.UnitPermReview' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UnitPermReview' AND LANG_CODE = 'zh_CN' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1644ee62b4a74ff1abd356a0a9835d91', 'zh_CN', '用户进行', 'replenish.UserInitiated' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UserInitiated' AND LANG_CODE = 'zh_CN' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'df1f7c081fcc439dbdb7ad0b5e65b864', 'en_US', 'User initiated', 'replenish.UserInitiated' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UserInitiated' AND LANG_CODE = 'en_US' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '82ba140d48da4e23ab82bb101f840012', 'zh_CN', '用户密码', 'replenish.UserPwd' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UserPwd' AND LANG_CODE = 'zh_CN' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '8e10dd101c0c4780bd0c7c9a13ef1f38', 'en_US', 'User password', 'replenish.UserPwd' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UserPwd' AND LANG_CODE = 'en_US' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '11b9a765ab9b49778704c499630bb634', 'en_US', 'Mark as new', 'replenish.MarkNew' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.MarkNew' AND LANG_CODE = 'en_US' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'fa4b86cb6c594b45b4fc3c7a04c71933', 'zh_CN', '为新增', 'replenish.MarkNew' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.MarkNew' AND LANG_CODE = 'zh_CN' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '806f286039fe478ba997dc94a95659b3', 'zh_CN', '绿色', 'replenish.Green' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Green' AND LANG_CODE = 'zh_CN' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9a4728b9992a4016ba94c1cd94a60b92', 'en_US', 'Green', 'replenish.Green' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Green' AND LANG_CODE = 'en_US' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '539be1efebf24b398099af1438b8db9c', 'zh_CN', '红色', 'replenish.Red' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Red' AND LANG_CODE = 'zh_CN' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7a13a3466f2f4d9fb42527dd3176d029', 'en_US', 'Red', 'replenish.Red' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Red' AND LANG_CODE = 'en_US' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '8bcdadeb2bfd4f4381d6bf43489ea0d4', 'en_US', 'Mark for deletion', 'replenish.MarkDelete' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.MarkDelete' AND LANG_CODE = 'en_US' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f27f58b53919425cb70c1051eed5b4d5', 'zh_CN', '为删除', 'replenish.MarkDelete' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.MarkDelete' AND LANG_CODE = 'zh_CN' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()), 'zh_CN', '流程删除后不可恢复的，确定要批量删除吗？', 'bpm.message.delete.user.start.flow.unrecoverable' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bpm.message.delete.user.start.flow.unrecoverable' AND LANG_CODE = 'zh_CN' limit 1);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()), 'zh_CN', '租户已存在单位数量：{0},不能小于这个数', 'sys.tenant.corp.number.min' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.corp.number.min' AND LANG_CODE = 'zh_CN');

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()), 'zh_CN', '租户已存在用户数量：{0},不能小于这个数', 'sys.tenant.user.number.min' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.user.number.min' AND LANG_CODE = 'zh_CN');

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()), 'zh_CN', '租户已存在银行账户数量：{0},不能小于这个数', 'sys.tenant.bankacc.number.min' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.bankacc.number.min' AND LANG_CODE = 'zh_CN');

INSERT INTO sys_notification_category_m (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT 'a4ebd29193c544abb60e51447256812c', '3d6dd6736e2b466e91d85b715b82a9c3', '系统内管端配置修改', 'sys:tenant:record:update', 'bda2047eb9cb42e39d7670e43ea5e828', '/sys/tenant/list', null,
       '尊敬的管理员，系统内部管理配置已更新： &bull; 用户数量上限：$!{userNumber} &bull; 单位数量上限：$!{corpNumber} &bull; 银行账户数量上限：$!{accountNumber}', 1, 1, '您有一条系统内管端配置修改,请查看!', 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category_m WHERE ID = 'a4ebd29193c544abb60e51447256812c' LIMIT 1
);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()),
       'zh_CN',
       '当前租户网点/单位数量：{0}; 需要添加网点/单位数量：{1};客户签约时设置的网点/单位数量：{2};已超出客户签约时设置的网点/单位数量，请确认',
       'sys.tenant.corp.num.over.limit'
from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.corp.num.over.limit' AND LANG_CODE = 'zh_CN');

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()),
       'zh_CN',
       '当前租户账户数量：{0}; 需要添加账户数量：{1}; 客户签约时设置的账户数量：{2}; 已超出客户签约时设置的账户数量，请确认',
       'sys.tenant.account.num.over.limit'
from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.account.num.over.limit' AND LANG_CODE = 'zh_CN');

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()),
       'zh_CN',
       '当前租户用户数量：{0}; 需要添加用户数量：{1}; 客户签约时设置的用户数量：{2}; 已超出客户签约时设置的用户数量，请确认',
       'sys.tenant.user.num.over.limit'
from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.user.num.over.limit' AND LANG_CODE = 'zh_CN');
