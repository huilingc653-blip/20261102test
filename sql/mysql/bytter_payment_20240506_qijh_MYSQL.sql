##修改人：qijh
##修改时间：2024.04.24
##修改原因：大参林项目脚本

##新增参数:未产生资金需求付款单付款控制
INSERT INTO SYS_CONFIG (ID, TYPE, NAME, VALUE)
SELECT '4a48b9c72def4b768f082beccc364ace', 'nis', 'demandControl', '1'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM SYS_CONFIG WHERE ID = '4a48b9c72def4b768f082beccc364ace' LIMIT 1
);
##GOGO

##新增“资金需求清单”
CREATE TABLE IF NOT EXISTS NIS_DEMAND (
    ID                VARCHAR(32) not null,
    BILL_NO           varchar(32),
    CORP_ID           varchar(32),
    BANK_ACC          varchar(50),
    BANK_ACC_ID       varchar(32),
    ACC_NAME          varchar(150),
    BANK_NAME         varchar(150),
    DEMAND_TYPE       varchar(10),
    DEMAND_DATE       datetime,
    DEMAND_MONEY      decimal(15,2),
    DEMAND_ALLOCATION_MONEY decimal(15,2),
    PAY_BILL_ID       varchar(32),
    PAY_BILL_NO       varchar(32),
    PAY_BILL_DATE     datetime,
    SUPER_CORP_ID     varchar(32),
    SUPER_BANK_ACC    varchar(32),
    SUPER_BANK_ACC_ID varchar(32),
    SUPER_ACC_NAME    varchar(150),
    SUPER_BANK_NAME   varchar(150),
    STATUS            decimal(10,0),
    ALLOCATION_NO     varchar(32),
    ALLOCATION_ID     varchar(32),
    ALLOCATION_DATE   datetime,
    IS_SUMMARY        varchar(10) default 0,
    CREATE_BY         VARCHAR(64),
    CREATE_DATE       DATETIME(6),
    UPDATE_BY         VARCHAR(64),
    UPDATE_DATE       DATETIME(6),
   constraint PK_NIS_DEMAND primary key (ID)
) COMMENT '资金需求清单';
##GOGO

##新增菜单“结算管理》资金调拨》资金需求清单”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT '2a9b7b7435494fa29bdbd174e7121905', '', '资金需求清单', '资金需求清单', '2a164d43b1d042bdbd8eabe715fd72e4', null, 3, 1, '/netbank/nisdemand/list', null, null, null, null, null, null, 0 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '2a9b7b7435494fa29bdbd174e7121905' LIMIT 1
);
##GOGO

##新增菜单资源 查询资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'abdaf7d01cc64b8694e6622cedd3984e', '查询资源', '查询资源', 'netbank:nisDemand:query', 1, '2a9b7b7435494fa29bdbd174e7121905' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'abdaf7d01cc64b8694e6622cedd3984e' LIMIT 1
);
##GOGO

##新增菜单资源 查询资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'b1324160c61f478dac40a5bfee050803', '申请资源', '申请资源', 'netbank:nisDemand:apply', 1, '2a9b7b7435494fa29bdbd174e7121905' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'b1324160c61f478dac40a5bfee050803' LIMIT 1
);
##GOGO

## 新增字典 nisDemand.demandType
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '32d5e07c4efd4d8b9c14c3a3a1f7d77f', 'nisDemand.demandType', '资金需求类型' from dual
where not exists (select 1 from sys_dict_group where ID ='32d5e07c4efd4d8b9c14c3a3a1f7d77f' limit 1);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '453dee4a74494abebf3166785dfcfab5', '01', '付款',null,'32d5e07c4efd4d8b9c14c3a3a1f7d77f',1  from dual
where not exists (select 1 from sys_dict where ID ='453dee4a74494abebf3166785dfcfab5' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '0748030eb71d4d20818173bdda87283a', '02', '承兑到期',null,'32d5e07c4efd4d8b9c14c3a3a1f7d77f',2  from dual
where not exists (select 1 from sys_dict where ID ='0748030eb71d4d20818173bdda87283a' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '17d5e8950b8a40968a3113c82222287f', '03', '融资到期',null,'32d5e07c4efd4d8b9c14c3a3a1f7d77f',3  from dual
where not exists (select 1 from sys_dict where ID ='17d5e8950b8a40968a3113c82222287f' limit 1);
##GOGO

## 新增字典 nisDemand.status
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '684df687d537429ca8c478f5dff41105', 'nisDemand.status', '资金需求状态' from dual
where not exists (select 1 from sys_dict_group where ID ='684df687d537429ca8c478f5dff41105' limit 1);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '28ff42c646534d0a8d3cb8044be33244', '0', '暂存',null,'684df687d537429ca8c478f5dff41105',1  from dual
where not exists (select 1 from sys_dict where ID ='28ff42c646534d0a8d3cb8044be33244' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'd6bd91814f084060b125383f86137934', '-1', '打回',null,'684df687d537429ca8c478f5dff41105',2  from dual
where not exists (select 1 from sys_dict where ID ='d6bd91814f084060b125383f86137934' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '961de35fd4bc4e9bb84ad38abe040092', '-2', '作废',null,'684df687d537429ca8c478f5dff41105',3  from dual
where not exists (select 1 from sys_dict where ID ='961de35fd4bc4e9bb84ad38abe040092' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '07296442da2244099848b4316d233928', '1', '已确认',null,'684df687d537429ca8c478f5dff41105',4  from dual
where not exists (select 1 from sys_dict where ID ='07296442da2244099848b4316d233928' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '13c6114a41914777a268066f0cf6ba0b', '2', '下拨中',null,'684df687d537429ca8c478f5dff41105',5  from dual
where not exists (select 1 from sys_dict where ID ='13c6114a41914777a268066f0cf6ba0b' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '34d0ad61f4a34b74919622e5a1df3cce', '3', '已下拨',null,'684df687d537429ca8c478f5dff41105',6  from dual
where not exists (select 1 from sys_dict where ID ='34d0ad61f4a34b74919622e5a1df3cce' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '2b0615b80858479694925631d7bcb73d', '4', '下拨失败',null,'684df687d537429ca8c478f5dff41105',7  from dual
where not exists (select 1 from sys_dict where ID ='2b0615b80858479694925631d7bcb73d' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '8d8de5dfb26248ddbd0eb0f012b0e2bc', '4', '下拨状态未确认',null,'684df687d537429ca8c478f5dff41105',8  from dual
where not exists (select 1 from sys_dict where ID ='8d8de5dfb26248ddbd0eb0f012b0e2bc' limit 1);
##GOGO