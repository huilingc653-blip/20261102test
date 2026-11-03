##修改人：huangx
##修改时间：2025-06-20
##修改内容：00062025060764多级单位余额核算系列需求

##新增菜单“多级单位余额核算”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '3fbf540bedba41f2919d9697028f6659', '', '多级单位余额核算', '多级单位余额核算', 'd5b09cb914304f8987cb8e6e0ecb3ff3', null, 8, 1, '', null, null, null, null, null, null, 1, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '3fbf540bedba41f2919d9697028f6659' LIMIT 1
);
##GOGO

##新增菜单“多级单位余额核算>>单位归集余额维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '44aba7c192d141df9a04348cefc0369a', '', '单位归集余额维护', '单位归集余额维护', '3fbf540bedba41f2919d9697028f6659', null, 1, 1, '/bankacc/biscorppoolbal/list', null, null, null, null, null, null, 0, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '44aba7c192d141df9a04348cefc0369a' LIMIT 1
);
##GOGO

##新增菜单资源(单位归集余额维护)
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '772f98b075ed410f8735f58929b16008', '查询资源', '查询资源', 'bankacc:biscorppoolbal:query', 1, '44aba7c192d141df9a04348cefc0369a' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '772f98b075ed410f8735f58929b16008' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '888a31d7f80d4b04baa7481ee86b1551', '应用资源', '应用资源', 'bankacc:biscorppoolbal:apply', 1, '44aba7c192d141df9a04348cefc0369a' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '888a31d7f80d4b04baa7481ee86b1551' LIMIT 1
);
##GOGO

##新增菜单“多级单位余额核算>>单位归集透支金额维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'd94a7cf4362e45749f291b6d7838532b', '', '单位归集透支金额维护', '单位归集透支金额维护', '3fbf540bedba41f2919d9697028f6659', null, 2, 1, '/bankacc/biscorppooloverdraft/list', null, null, null, null, null, null, 0, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'd94a7cf4362e45749f291b6d7838532b' LIMIT 1
);
##GOGO

##新增菜单资源(单位归集透支金额维护)
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '452f98b075ed410f8735f58929b16048', '查询资源', '查询资源', 'bankacc:biscorppooloverdraft:query', 1, 'd94a7cf4362e45749f291b6d7838532b' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '452f98b075ed410f8735f58929b16048' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '788a31d7f80d4b04baa7481ee86b1351', '应用资源', '应用资源', 'bankacc:biscorppooloverdraft:apply', 1, 'd94a7cf4362e45749f291b6d7838532b' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '788a31d7f80d4b04baa7481ee86b1351' LIMIT 1
);
##GOGO

##新增菜单“多级单位余额核算>>单位归集配置维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '97d40c130fcf4c1692fbc81ed1cbdbc5', '', '单位归集配置维护', '单位归集配置维护', '3fbf540bedba41f2919d9697028f6659', null, 3, 1, '/bankacc/biscorppoolconfig/list', null, null, null, null, null, null, 0, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '97d40c130fcf4c1692fbc81ed1cbdbc5' LIMIT 1
);
##GOGO

##新增菜单资源(单位归集配置维护)
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '66d40c130fcf4c1692fbc81ed1cbdbc5', '查询资源', '查询资源', 'bankacc:biscorppoolconfig:query', 1, '97d40c130fcf4c1692fbc81ed1cbdbc5' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '66d40c130fcf4c1692fbc81ed1cbdbc5' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '97d30c130fcf4c1692fbc81ed1cbdbc5', '应用资源', '应用资源', 'bankacc:biscorppoolconfig:apply', 1, '97d40c130fcf4c1692fbc81ed1cbdbc5' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '97d30c130fcf4c1692fbc81ed1cbdbc5' LIMIT 1
);
##GOGO

##新增菜单“多级单位余额核算>>单位归集流水维护”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'f73d886533794f508382b8bead018216', '', '单位归集流水维护', '单位归集流水维护', '3fbf540bedba41f2919d9697028f6659', null, 4, 1, '/bankacc/biscorppooldtl/list', null, null, null, null, null, null, 0, 1 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = 'f73d886533794f508382b8bead018216' LIMIT 1
);
##GOGO

##新增菜单资源(单位归集流水维护)
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'cc3d886533794f508382b8bead018216', '查询资源', '查询资源', 'bankacc:biscorppooldtl:query', 1, 'f73d886533794f508382b8bead018216' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'cc3d886533794f508382b8bead018216' LIMIT 1
);
##GOGO

INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'de3d886533794f508382b8bead018221', '应用资源', '应用资源', 'bankacc:biscorppooldtl:apply', 1, 'f73d886533794f508382b8bead018216' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'de3d886533794f508382b8bead018221' LIMIT 1
);
##GOGO

##修改内容:新增多级单位余额核算-单位归集余额表
CREATE TABLE IF NOT EXISTS BIS_CORP_POOL_BAL  (
    ID                  VARCHAR(32)                    NOT NULL,
    CORP_ID          	VARCHAR(32) COMMENT '单位ID',
    CUR_ID          	VARCHAR(32) COMMENT '币别ID',
    BAL          		decimal(19,2) COMMENT '余额',
    BAL_DATE       		datetime COMMENT '余额日期',
    GET_DATE        	datetime(6) COMMENT '获取时间',
    INIT_SIGN       	VARCHAR(2) COMMENT '初始化标志0-否 1-是',
    CREATE_BY             VARCHAR(50) COMMENT '创建人登录名称',
    CREATE_TIME          DATETIME(6) COMMENT '创建日期',
    UPDATE_BY            VARCHAR(50) COMMENT '更新人登录名称',
    UPDATE_TIME          DATETIME(6) COMMENT '更新日期',
    IS_DELETED          VARCHAR(2) COMMENT '是否删除',
    CONSTRAINT PK_BIS_CORP_POOL_BAL PRIMARY KEY (ID)
) COMMENT '多级单位余额核算-单位归集余额表';
##GOGO

##修改内容:新增多级单位余额核算-单位归集透支金额表
CREATE TABLE IF NOT EXISTS BIS_CORP_POOL_OVERDRAFT  (
    ID                   VARCHAR(32)                    NOT NULL,
    CORP_ID          	 VARCHAR(32) COMMENT '单位ID',
    CUR_ID          	 VARCHAR(32) COMMENT '币别ID',
    OVERDRAFT_AMT        decimal(19,2) COMMENT '透支金额',
    CREATE_BY            VARCHAR(50) COMMENT '创建人登录名称',
    CREATE_TIME          DATETIME(6) COMMENT '创建日期',
    UPDATE_BY            VARCHAR(50) COMMENT '更新人登录名称',
    UPDATE_TIME          DATETIME(6) COMMENT '更新日期',
    IS_DELETED          VARCHAR(2) COMMENT '是否删除',
    CONSTRAINT PK_BIS_CORP_POOL_OVERDRAFT PRIMARY KEY (ID)
) COMMENT '多级单位余额核算-单位归集透支金额表';
##GOGO

##修改内容:新增多级单位余额核算-单位归集配置表
CREATE TABLE IF NOT EXISTS BIS_CORP_POOL_CONFIG  (
    ID                   VARCHAR(32)                    NOT NULL,
    CONFIG_CODE          VARCHAR(32) COMMENT '规则编码',
    CONFIG_NAME          VARCHAR(100) COMMENT '规则名称',
    BUS_TYPE        	 VARCHAR(2) COMMENT '类型',
    VALID_SIGN       	 VARCHAR(2) COMMENT '有效标志 1-有效 0-无效',
    CREATE_BY            VARCHAR(50) COMMENT '创建人登录名称',
    CREATE_TIME          DATETIME(6) COMMENT '创建日期',
    UPDATE_BY            VARCHAR(50) COMMENT '更新人登录名称',
    UPDATE_TIME          DATETIME(6) COMMENT '更新日期',
    IS_DELETED          VARCHAR(2) COMMENT '是否删除',
    CONSTRAINT PK_BIS_CORP_POOL_CONFIG PRIMARY KEY (ID)
) COMMENT '多级单位余额核算-单位归集配置表';
##GOGO

##修改内容:新增多级单位余额核算-单位归集配置规则表
CREATE TABLE IF NOT EXISTS BIS_CORP_POOL_CONFIG_RULE  (
    ID                   VARCHAR(32)                    NOT NULL,
    CONFIG_ID			 VARCHAR(32) COMMENT '配置ID',
    MATCH_RULE           VARCHAR(10) COMMENT '匹配规则',
    MATCH_ELEMENTS       VARCHAR(30) COMMENT '匹配要素',
    MATCH_RELATIONS      VARCHAR(10) COMMENT '匹配关系类型',
    MATCH_CONTENT      	 VARCHAR(100) COMMENT '匹配内容',
    LEFT_PARENTHESIS     VARCHAR(2) COMMENT '左括号',
    RIGHT_PARENTHESIS    VARCHAR(2) COMMENT '右括号',
    SORT				 INT COMMENT '排序',
    CREATE_BY            VARCHAR(50) COMMENT '创建人登录名称',
    CREATE_TIME          DATETIME(6) COMMENT '创建日期',
    UPDATE_BY            VARCHAR(50) COMMENT '更新人登录名称',
    UPDATE_TIME          DATETIME(6) COMMENT '更新日期',
    IS_DELETED          VARCHAR(2) COMMENT '是否删除',
    CONSTRAINT PK_BIS_CORP_POOL_CONFIG_RULEG PRIMARY KEY (ID)
) COMMENT '多级单位余额核算-单位归集配置规则表';
##GOGO

##单位归集配置类型
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT 'abd46c5116cdd79d1697eb6ba4ffb7f6', 'bisCorpPoolConfig.busType', '单位归集配置类型' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = 'abd46c5116cdd79d1697eb6ba4ffb7f6' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '10f4e2c65ca2ca2bc565e731c5b06c39', '0', '归集', NULL, 'abd46c5116cdd79d1697eb6ba4ffb7f6', 500 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '10f4e2c65ca2ca2bc565e731c5b06c39' LIMIT 1
    );
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_GROUP_ID, SORT)
SELECT '52a8648d19e370e0ebab65274992f86d', '1', '下拨', 'abd46c5116cdd79d1697eb6ba4ffb7f6', '1' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '52a8648d19e370e0ebab65274992f86d' LIMIT 1
);
##GOGO

##流水增加 类型 1-归集 2-下拨 3-其他
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'POOL_TYPE') THEN
      ALTER TABLE BIS_ACC_DTL ADD COLUMN POOL_TYPE VARCHAR(2);
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##流水增加 认领标志
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA =
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'POOL_BLEND_SIGN') THEN
      ALTER TABLE BIS_ACC_DTL ADD COLUMN POOL_BLEND_SIGN VARCHAR(2) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##多级单位归集余额核算-类型
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '7f1b890389304c92a8282682b823cad1', 'bisCorpPoolDtl.poolType', '多级单位归集余额核算-类型' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict_group WHERE ID = '7f1b890389304c92a8282682b823cad1' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'b765fb5cdcf74a9a9410cfc90083c9db', '1', '归集', NULL, '7f1b890389304c92a8282682b823cad1', 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'b765fb5cdcf74a9a9410cfc90083c9db' LIMIT 1
    );
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'ff0f0b99bf5b4c56a5abd55e346d11d1', '2', '下拨', NULL, '7f1b890389304c92a8282682b823cad1', 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = 'ff0f0b99bf5b4c56a5abd55e346d11d1' LIMIT 1
    );
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '0da325e0046c457fbda512f38d88d176', '3', '其他', NULL, '7f1b890389304c92a8282682b823cad1', 1 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_dict WHERE ID = '0da325e0046c457fbda512f38d88d176' LIMIT 1
    );
##GOGO

##定时任务：单位归集余额自动计算
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '单位归集余额自动计算', '2025-06-26 19:20:18', '2025-06-26 19:20:18', 'XXL', '', 'CRON', '0 0 1,15 * * ?', 'DO_NOTHING', 'FIRST', 'BisCorpPoolDtlService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-06-26 19:20:18', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='总账户资金下拨流水自动记账' limit 1);
commit;
##GOGO



##定时任务：单位归集流水自动认领
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '单位归集流水自动认领', '2025-06-26 19:20:18', '2025-06-26 19:20:18', 'XXL', '', 'CRON', '0 0 30 * * ?', 'DO_NOTHING', 'FIRST', 'BisCorpPoolConfigRuleService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2025-06-26 19:20:18', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='单位归集流水自动认领' limit 1);
commit;
##GOGO



##修改人员:xiay
##修改内容:增加国际化
##修改日期:2025-06-30
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c5c0dfdb20337ce2061ec94627e18f3a', 'zh_CN', '同单位、同币种、同余额日期的归集余额数据需唯一！', 'netbank.message.biscorppoolbal.repeat' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY = 'netbank.message.biscorppoolbal.repeat' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO







