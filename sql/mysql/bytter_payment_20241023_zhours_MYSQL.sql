
## 新增账号用途维护菜单SQL

##账号用途表
CREATE TABLE IF NOT EXISTS BT_BANK_ACC_USE
(
    ID              varchar(32)      not null comment 'id' primary key,
    USE_CODE        varchar(32)      not null comment '用途编码',
    USE_NAME        varchar(200)     not null comment '用途名称',
    USE_RMK         varchar(200)     null comment '备注',
    CREATE_DATE     datetime         null,
    CREATE_BY       varchar(32)      null,
    UPDATE_DATE     datetime         null,
    UPDATE_BY       varchar(32)      null
    );
##GOGO

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select 'e62741fc90e811efaac1005056a84efa', '', '账号用途维护', '账号用途维护', 'd61f91e27ed9473ab9e8183343ea4507', '', 23, 1, '/netbank/bankAccUse/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='账号用途维护' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select 'dcbed92790ea11efaac1005056a84efa', '查询资源', '查询资源', 'netbank:bankAccUse:query', 1, 'e62741fc90e811efaac1005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='netbank:bankAccUse:query' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '4329959590eb11efaac1005056a84efa', '编辑资源', '编辑资源', 'netbank:bankAccUse:edit', 1, 'e62741fc90e811efaac1005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='netbank:bankAccUse:edit' limit 1);
commit;
##GOGO

## 账号表增加账号用途
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'ACC_USE_CODE') THEN
alter table BT_BANK_ACC add column ACC_USE_CODE varchar(320) ;
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '该用途编码已存在，请修改！', 'netbank.message.bankaccuse.unique' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.message.bankaccuse.unique' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '该用途已经存在绑定账号，不能删除，请确认！', 'netbank.message.bankaccuse.use' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.message.bankaccuse.use' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
