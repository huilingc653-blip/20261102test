INSERT INTO sys_menu_manager(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '96ebc4bde88940e283cb504fb6c3dce5', NULL, '客户银行借款明细查询', '客户银行借款明细查询', 'dac60fdbaec84e869bd78f07dbf0e2f7', NULL, 500, 1, '/report/loanInfoReport/list', NULL, NUll, NULL, NULL, NULL, NULL, 0, 0, NULL FROM DUAL
WHERE NOT EXISTS(
    SELECT 1 FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dce5' LIMIT 1
);
commit;
##GOGO
;


insert into sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '108cbeee0a1443d6b0fcf8b00f52002f', '查询资源', '查询资源', 'sys:loanInfo:report:query', 1, '96ebc4bde88940e283cb504fb6c3dce5' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID='108cbeee0a1443d6b0fcf8b00f52002f' LIMIT 1
);
##GOGO





INSERT INTO sys_menu_manager(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '96ebc4bde88940e283cb504fb6c3dcff', NULL, '客户账户交易明细查询', '客户账户交易明细查询', 'dac60fdbaec84e869bd78f07dbf0e2f7', NULL, 500, 1, '/report/bankAccDtlReport/list', NULL, NUll, NULL, NULL, NULL, NULL, 0, 0, NULL FROM DUAL
WHERE NOT EXISTS(
    SELECT 1 FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dcff' LIMIT 1
);
commit;
##GOGO
;


insert into sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '108cbeee0a1443d6b0fcf8b00f5200ff', '查询资源', '查询资源', 'sys:bankAccDtl:report:query', 1, '96ebc4bde88940e283cb504fb6c3dcff' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID='108cbeee0a1443d6b0fcf8b00f5200ff' LIMIT 1
);
##GOGO




INSERT INTO sys_menu_manager(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '96ebc4bde88940e283cb504fb6c3dckk', NULL, '客户账户余额查询', '客户账户余额查询', 'dac60fdbaec84e869bd78f07dbf0e2f7', NULL, 500, 1, '/report/bisAccBalReport/list', NULL, NUll, NULL, NULL, NULL, NULL, 0, 0, NULL FROM DUAL
WHERE NOT EXISTS(
    SELECT 1 FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dckk' LIMIT 1
);
commit;
##GOGO
;


insert into sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '108cbeee0a1443d6b0fcf8b00f5200kk', '查询资源', '查询资源', 'sys:bisaccbal:report:query', 1, '96ebc4bde88940e283cb504fb6c3dckk' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID='108cbeee0a1443d6b0fcf8b00f5200kk' LIMIT 1
);
##GOGO



INSERT INTO sys_menu_manager(ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '96ebc4bde88940e283cb504fb6c3dcpp', NULL, '用户活跃度统计', '用户活跃度统计', 'dac60fdbaec84e869bd78f07dbf0e2f7', NULL, 500, 1, '/report/userActivityReport/list', NULL, NUll, NULL, NULL, NULL, NULL, 0, 0, NULL FROM DUAL
WHERE NOT EXISTS(
    SELECT 1 FROM sys_menu_manager WHERE ID = '96ebc4bde88940e283cb504fb6c3dcpp' LIMIT 1
);
commit;
##GOGO
;

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
##GOGO


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
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f067fdcfa6354d62a5d0ab68352bcee4', 'zh_CN', '单位特殊权限复核', 'replenish.UnitPermReview' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UnitPermReview' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1644ee62b4a74ff1abd356a0a9835d91', 'zh_CN', '用户进行', 'replenish.UserInitiated' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UserInitiated' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'df1f7c081fcc439dbdb7ad0b5e65b864', 'en_US', 'User initiated', 'replenish.UserInitiated' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UserInitiated' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '82ba140d48da4e23ab82bb101f840012', 'zh_CN', '用户密码', 'replenish.UserPwd' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UserPwd' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '8e10dd101c0c4780bd0c7c9a13ef1f38', 'en_US', 'User password', 'replenish.UserPwd' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UserPwd' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '11b9a765ab9b49778704c499630bb634', 'en_US', 'Mark as new', 'replenish.MarkNew' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.MarkNew' AND LANG_CODE = 'en_US' limit 1);
##GOGO


insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'fa4b86cb6c594b45b4fc3c7a04c71933', 'zh_CN', '为新增', 'replenish.MarkNew' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.MarkNew' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '806f286039fe478ba997dc94a95659b3', 'zh_CN', '绿色', 'replenish.Green' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Green' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '9a4728b9992a4016ba94c1cd94a60b92', 'en_US', 'Green', 'replenish.Green' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Green' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '539be1efebf24b398099af1438b8db9c', 'zh_CN', '红色', 'replenish.Red' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Red' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7a13a3466f2f4d9fb42527dd3176d029', 'en_US', 'Red', 'replenish.Red' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Red' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '8bcdadeb2bfd4f4381d6bf43489ea0d4', 'en_US', 'Mark for deletion', 'replenish.MarkDelete' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.MarkDelete' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f27f58b53919425cb70c1051eed5b4d5', 'zh_CN', '为删除', 'replenish.MarkDelete' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.MarkDelete' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'aeb35bfcbbe44c7cb2135912e155c99e', 'en_US', 'Role', 'replenish.Role' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Role' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1d20042d78524bf0813c5472783809b0', 'zh_CN', '角色', 'replenish.Role' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.Role' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '68b592e9421848478e9ace0f474cefa2', 'zh_CN', '授权用户', 'replenish.AuthorizedUser' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.AuthorizedUser' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '98e72e96f09a4f6ab24efff67d214b5b', 'en_US', 'Authorized user', 'replenish.AuthorizedUser' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.AuthorizedUser' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a308a4bcfd9646ee8ce75d571c0ef932', 'zh_CN', '新增用户', 'replenish.AddUser' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.AddUser' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b37ee9f227a84aba9f795b8d241d6011', 'en_US', 'Add new user', 'replenish.AddUser' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.AddUser' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b9c3e20e389b455f806b4487dffab598', 'zh_CN', '未发生改变用户', 'replenish.UnmodUsers' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UnmodUsers' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'f19ac0bc0878469c877f6d299d425bf5', 'en_US', 'Unmodified Users', 'replenish.UnmodUsers' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.UnmodUsers' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '16f6c902d9dc44f2b15e403e55717b60', 'zh_CN', '删除用户', 'replenish.DeleteUser' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.DeleteUser' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e519db9c47ec4bdcba184b7c7dc3f6a9', 'en_US', 'Delete user', 'replenish.DeleteUser' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.DeleteUser' AND LANG_CODE = 'en_US' limit 1);
##GOGO
