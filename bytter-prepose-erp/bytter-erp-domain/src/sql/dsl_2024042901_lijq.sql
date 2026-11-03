##创建人:lijq
##创建时间:2024年4月29日

CREATE TABLE `DSL_INTERFACE_LOG` (
`ID`                         varchar(32) COLLATE utf8_bin NOT NULL COMMENT '主键id',
`SOURCE_ID`                   VARCHAR(50) COLLATE utf8_bin DEFAULT NULL COMMENT '来源主键',
`TYPE`                       varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '日志类型（1:成功、0:失败）',
`INTERFACE_NAME`             varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '接口名称',
`INTERFACE_CODE`             varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '接口编号',
`LOG_TIME`                   DATETIME COLLATE utf8_bin DEFAULT NULL COMMENT '日志时间',
`LOG`                        longtext COLLATE utf8_bin COMMENT '接口日志',
PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='ERP接口日志表';

## 创建接口日志记录表菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '870b09b5b005407e940b381df196abb8', null, '接口日志记录表', '接口日志记录表', 'c1fb5fbb835e4303852804517e8ea6c9', null, 500, 1, '/dataexchange/interfacelog/list', null, null, null, null, null, null, 0, 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '870b09b5b005407e940b381df196abb8' LIMIT 1
    );
##GOGO

##修改内容:增加数据字典
##修改时间:2024-05-07
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select 'ef569124d9b943de972d47f95902d890', 'dsl.interface.logType', '接口日志类型' from dual
where not exists (select 1 from sys_dict_group where id ='ef569124d9b943de972d47f95902d890' limit 1);
##GOGO

##修改内容:增加数据字典
##修改时间:2024-05-07
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
select '5c55b75ec6f04ac6b927366b05691007', 'DSL_PAYMENT_SYCN','付款信息同步接口', 'ef569124d9b943de972d47f95902d890',1 from dual
where not exists (select 1 from sys_dict where id ='5c55b75ec6f04ac6b927366b05691007' limit 1);
##GOGO

##修改内容:增加数据字典
##修改时间:2024-05-07
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
select '5c55b75ec6f04ac6b927366b05691opi', 'DSL_PAYMENT_RESULT','付款交易结果回传接口', 'ef569124d9b943de972d47f95902d890',1 from dual
where not exists (select 1 from sys_dict where id ='5c55b75ec6f04ac6b927366b05691opi' limit 1);
##GOGO