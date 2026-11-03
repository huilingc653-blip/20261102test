##修改人：zhours
##修改时间：2025.02.12
##增加：内部资金归集管理协议签订

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '4cd5fad5e8dc11ef8a4d005056a84efa', '', '内部资金归集管理协议签订', '内部资金归集管理协议签订', 'd61f91e27ed9473ab9e8183343ea4507', '', 30, 1, '/allocation/bisgatherpolicy/agreementList', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='内部资金归集管理协议签订' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '55d0686be8dc11ef8a4d005056a84efa', '应用资源', '应用资源', 'allocation:bisgatherpolicy:agreement:query', 1, '4cd5fad5e8dc11ef8a4d005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='allocation:bisgatherpolicy:agreement:query' limit 1);
commit;
##GOGO

##内部资金归集管理协议签订表
CREATE TABLE IF NOT EXISTS BIS_GATHER_POLICY_AGREEMENT
(
    ID              varchar(32)      not null comment 'id' primary key,
    CORP_ID         varchar(32)      not null comment '单位ID',
    CORP_NAME       varchar(300)     not null comment '单位名称',
    IS_AGREEMENT    varchar(1)       not null comment '是否签订协议：1是；0否',
    CREATE_DATE     datetime         null,
    CREATE_BY       varchar(32)      null,
    UPDATE_DATE     datetime         null,
    UPDATE_BY       varchar(32)      null
    );
##GOGO

## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '单位：{0}已存在，不允许重复添加！', 'nis.allocation.policy.agreement.exist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.allocation.policy.agreement.exist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
