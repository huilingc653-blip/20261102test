##修改人：zhours
##修改时间：2025.02.11
##增加：为nis_demand表增加索引

DROP PROCEDURE IF EXISTS PRO_ADD_IND;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_IND()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME = 'NIS_DEMAND' AND INDEX_NAME = 'IDX_DEMAND_BILL_ID_STATUS') THEN
ALTER TABLE NIS_DEMAND ADD INDEX IDX_DEMAND_BILL_ID_STATUS (PAY_BILL_ID,STATUS);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_IND();
DROP PROCEDURE IF EXISTS PRO_ADD_IND;
##GOGO


##增加 资金中心调拨账户配置

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select 'c176de51e81b11ef8277005056a84efa', '', '资金中心调拨账户配置', '资金中心调拨账户配置', 'd61f91e27ed9473ab9e8183343ea4507', '', 29, 1, '/allocation/bisgatherpolicy/accountList', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='资金中心调拨账户配置' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '0b72d868e83f11ef8277005056a84efa', '应用资源', '应用资源', 'allocation:bisgatherpolicy:account:query', 1, 'c176de51e81b11ef8277005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='allocation:bisgatherpolicy:account:query' limit 1);
commit;
##GOGO

##资金中心调拨账户配置表
CREATE TABLE IF NOT EXISTS BIS_GATHER_POLICY_ACCOUNT
(
    ID              varchar(32)      not null comment 'id' primary key,
    CORP_NAME       varchar(300)     not null comment '单位名称',
    BANK_ACC        varchar(200)     not null comment '银行账号',
    BANK_NAME       varchar(200)     not null comment '开户行',
    PRIORITY        int              not null comment '优先级',
    IS_DOWN         varchar(1)       not null comment '是否允许下拨：1允许；0不允许',
    STATUS          varchar(1)       not null comment '状态：1正常；0禁用',
    CREATE_DATE     datetime         null,
    CREATE_BY       varchar(32)      null,
    UPDATE_DATE     datetime         null,
    UPDATE_BY       varchar(32)      null
    );
##GOGO


## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '银行账号{0}已存在！', 'nis.allocation.policy.account.exist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.allocation.policy.account.exist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '优先级{0}已存在！', 'nis.allocation.policy.priority.exist' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.allocation.policy.priority.exist' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select 'c1f0df82e84111ef8277005056a84efa', 'status_yn', '状态' from dual
where not exists (select 1 from sys_dict_group where ID ='c1f0df82e84111ef8277005056a84efa' limit 1);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'd21ebb6ee84111ef8277005056a84efa', '1', '正常',null,'c1f0df82e84111ef8277005056a84efa',1  from dual
where not exists (select 1 from sys_dict where ID ='d21ebb6ee84111ef8277005056a84efa' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'd66fd1fbe84111ef8277005056a84efa', '0', '禁用',null,'c1f0df82e84111ef8277005056a84efa',2  from dual
where not exists (select 1 from sys_dict where ID ='d66fd1fbe84111ef8277005056a84efa' limit 1);
##GOGO
