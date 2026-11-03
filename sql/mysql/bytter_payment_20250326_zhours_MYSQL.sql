##修改人：zhours
##修改时间：2025.03.26
##增加：银承账户留底金额配置 功能

INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '0465766309e511f0a597005056a84efa', '', '银承账户留底金额配置', '银承账户留底金额配置', '0c93d0aa1ea849fd86d3a0f42c3dab17', '', 29, 1, '/bill/accebill/accKeepAmt/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='银承账户留底金额配置' limit 1);
commit;
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '0bf0da0309e511f0a597005056a84efa', '应用资源', '应用资源', 'bill:accebill:accKeepAmt:controls', 1, '0465766309e511f0a597005056a84efa', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='bill:accebill:accKeepAmt:controls' limit 1);
commit;
##GOGO

## 银承账户留底金额配置
CREATE TABLE IF NOT EXISTS BILL_ACC_KEEP_AMT
(
    ID              varchar(32)      not null comment 'id' primary key,
    CORP_ID         varchar(32)      not null comment '单位ID',
    CORP_NAME       varchar(300)     not null comment '单位名称',
    BANK_ACC        varchar(200)     not null comment '账号',
    ACC_NAME        varchar(200)     not null comment '户名',
    BANK_TYPE       varchar(32)      not null comment '银行类型',
    BANK_TYPE_NAME  varchar(200)     not null comment '银行类型名称',
    BANK_CODE       varchar(30)      not null comment '开户行号',
    BANK_NAME       varchar(200)     not null comment '开户行名',
    KEEP_AMT        DECIMAL(15,2)    not null comment '留存金额',
    STATUS          varchar(1)       not null comment '状态：1正常；0禁用',
    CREATE_DATE     datetime         null,
    CREATE_BY       varchar(32)      null,
    UPDATE_DATE     datetime         null,
    UPDATE_BY       varchar(32)      null
    );
##GOGO


## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '银行账号{0}已存在！', 'accebill.acc.keep.amt.exists' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.acc.keep.amt.exists' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
