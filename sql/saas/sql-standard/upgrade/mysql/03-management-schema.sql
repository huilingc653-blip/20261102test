-- ============================================================================
-- SaaS 供应链金融内管相关脚本
-- 数据库：MySQL
-- 脚本：2026-09-20-management-应付债权模板配置表、租户定时任务分配表
-- 变更：新增 10 张表：
--       一、应付债权模板配置（来源 scf_mysql.sql）
--         1. scf_pay_config_version     应付债权模板配置版本表
--         2. scf_pay_template_config    应付债权模板配置
--         3. scf_pay_field_config       应付债权字段预设表
--         4. scf_cust_field_config      应付债权自定义字段配置表
--         5. scf_pay_config             应付债权页面字段配置表
--         6. scf_voucher_custom_field   应付债权凭证数据表自定义字段数据
--         7. SCF_PLATFORM               供应链金融平台表
--         8. SCF_PRODUCT                供应链融资产品表
--         9. SCF_BANK_OPEN              接口开通情况表
--       二、租户定时任务（来源 dev_20260930_mysql.sql）
--         10. SYS_TENANT_SCHEDULED_TASK 租户定时任务分配表
-- 来源：bt-saas-management/bt-saas-management-web/src/main/webapp/sql/
--         scf_mysql.sql、dev_20260930_mysql.sql
-- 生成日期：2026-09-20
-- 注意：面向存量环境，可重复执行；禁止反向覆盖源脚本。
--       本批 4 个源脚本均来自内管工程 bt-saas-management，故内容全部落在本目录
--       03-management-schema.sql / 04-management-data.sql；01/02 本批无内容，保持空文件。
--       表名/列名大小写与源脚本保持一致，避免在区分大小写的 MySQL 上建出重名表。
--       源脚本 SCF_PLATFORM / SCF_PRODUCT / SCF_BANK_OPEN 缺表级 ENGINE/CHARSET 尾巴，
--       本脚本按模板补齐（引擎与字符集在存量环境本已是同值，属声明补全，非变更）。
--
--       【与基线/源脚本的口径说明】
--       scf_voucher_custom_field 已存在于基线 sql-standard/mysql/06-business-schema.sql，
--       其定义源自 sql/saas/mysql/scf-table-init.sql（列宽 field_name varchar(200)、
--       field_value varchar(255)、field_id NOT NULL）。而 scf_mysql.sql 内同名表是较旧的
--       分叉副本（field_name varchar(255)、field_value varchar(500)、field_id 可空）。
--       本脚本以基线/规范源为口径。存量环境若已按旧副本建表，IF NOT EXISTS 会跳过建表、
--       保留现有较宽列，不做收窄——收窄属破坏性变更，模板明令禁止。
-- ============================================================================

-- ---------------------------------------------------------------------------
-- 第一部分：应付债权模板配置相关表（来源 scf_mysql.sql）
-- ---------------------------------------------------------------------------


-- 表说明：应付债权模板配置版本表
CREATE TABLE IF NOT EXISTS `scf_pay_config_version`
(
    `id`            VARCHAR(32)  NOT NULL COMMENT '模版版本ID',
    `version_no`    VARCHAR(32)  NULL COMMENT '版本编号',
    `template_code` VARCHAR(100) NULL COMMENT '模板编号',
    `template_name` VARCHAR(200) NULL COMMENT '模板名称',
    `template_desc` VARCHAR(500) NULL COMMENT '模板描述',
    `remark`        VARCHAR(500) NULL COMMENT '备注',
    `valid_sign`    VARCHAR(1)   NULL COMMENT '状态 0启用 1禁用',
    `create_by`     VARCHAR(32)  NULL COMMENT '创建人',
    `create_date`   DATETIME     NULL COMMENT '创建时间',
    `update_by`     VARCHAR(32)  NULL COMMENT '修改人',
    `update_date`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应付债权模板配置版本表';
##GOGO


-- 表说明：应付债权模板配置
CREATE TABLE IF NOT EXISTS `scf_pay_template_config`
(
    `id`          VARCHAR(32) NOT NULL COMMENT 'ID',
    `version_id`  VARCHAR(32) NOT NULL COMMENT '模版版本ID',
    `platform_id` VARCHAR(32) NULL COMMENT '融资平台ID',
    `product_id`  VARCHAR(32) NULL COMMENT '融资产品ID',
    `is_delete`   VARCHAR(1)  NULL DEFAULT '0' COMMENT '是否删除 0否 1是',
    `create_by`   VARCHAR(32) NULL COMMENT '创建人',
    `create_date` DATETIME    NULL COMMENT '创建时间',
    `update_by`   VARCHAR(32) NULL COMMENT '修改人',
    `update_date` DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `sort`        INT         NULL COMMENT '排序',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应付债权模板配置';
##GOGO


-- 表说明：应付债权字段预设表
CREATE TABLE IF NOT EXISTS `scf_pay_field_config`
(
    `id`              VARCHAR(32)  NOT NULL,
    `field_sort`      INT          NULL COMMENT '默认排序',
    `field_code`      VARCHAR(100) NULL COMMENT '字段编码',
    `field_name`      VARCHAR(255) NULL COMMENT '字段名称',
    `field_type`      VARCHAR(100) NULL COMMENT '字段类型',
    `field_area`      VARCHAR(50)  NULL COMMENT '字段所属区域',
    `field_def_val`   VARCHAR(500) NULL COMMENT '字段预设默认值',
    `field_length`    INT          NULL COMMENT '字段预设长度限制',
    `data_type`       VARCHAR(255) NULL COMMENT '数据类型',
    `data_group`      VARCHAR(255) NULL COMMENT '字典组',
    `data_url`        VARCHAR(255) NULL COMMENT '数据来源URL',
    `data_component`  VARCHAR(50)  NULL COMMENT '前端组件标识',
    `data_linkage`    VARCHAR(255) NULL COMMENT '前端数据联动',
    `data_textfield`  VARCHAR(50)  NULL COMMENT '下拉框数据展示值',
    `data_valuefield` VARCHAR(50)  NULL COMMENT '下拉框数据选择值',
    `is_limit_length` TINYINT      NULL COMMENT '是否限制长度',
    `is_visible`      TINYINT      NOT NULL DEFAULT '0' COMMENT '是否可见',
    `is_required`     TINYINT      NOT NULL DEFAULT '0' COMMENT '是否必填',
    `valid_sign`      CHAR(50)     NULL DEFAULT '1' COMMENT '有效标志',
    `is_sys`          CHAR(50)     NULL DEFAULT '0' COMMENT '是否系统字段',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应付债权字段预设表';
##GOGO


-- 表说明：应付债权自定义字段配置表
CREATE TABLE IF NOT EXISTS `scf_cust_field_config`
(
    `id`              VARCHAR(32)  NOT NULL,
    `FIELD_CODE`      VARCHAR(100) NULL COMMENT '字段编码',
    `field_name`      VARCHAR(200) NULL COMMENT '字段名称',
    `field_desc`      VARCHAR(500) NULL COMMENT '字段描述',
    `field_type`      VARCHAR(10)  NULL COMMENT '类型',
    `DICT_GROUP`      VARCHAR(60)  NULL COMMENT '字典组',
    `field_length`    INT          NULL COMMENT '长度限制',
    `is_limit_length` TINYINT      NULL COMMENT '是否限制长度',
    `is_visible`      TINYINT      NULL COMMENT '是否可见',
    `is_required`     TINYINT      NULL COMMENT '是否必填',
    `default_val`     VARCHAR(500) NULL COMMENT '预设默认值',
    `valid_sign`      VARCHAR(1)   NULL DEFAULT '1' COMMENT '有效标识',
    `create_date`     DATETIME     NULL,
    `create_by`       VARCHAR(32)  NULL,
    `update_date`     DATETIME     NULL,
    `update_by`       VARCHAR(32)  NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应付债权自定义字段配置表';
##GOGO


-- 表说明：应付债权页面字段配置表
CREATE TABLE IF NOT EXISTS `scf_pay_config`
(
    `id`                  VARCHAR(32)   NOT NULL,
    `template_version_id` VARCHAR(32)   NULL COMMENT '模板配置版本ID',
    `template_id`         VARCHAR(32)   NOT NULL COMMENT '模板ID',
    `field_id`            VARCHAR(32)   NULL COMMENT '预设字段ID',
    `field_code`          VARCHAR(100)  NULL COMMENT '字段编码',
    `field_name`          VARCHAR(255)  NULL COMMENT '字段名称',
    `field_type`          VARCHAR(50)   NULL COMMENT '字段类型',
    `field_sort`          INT           NULL COMMENT '排序',
    `field_length`        INT           NULL DEFAULT '0' COMMENT '长度限制',
    `field_limit_val`     VARCHAR(255)  NULL COMMENT '限制值',
    `field_def_val`       VARCHAR(255)  NULL COMMENT '默认值',
    `field_dict_group`    VARCHAR(255)  NULL COMMENT '枚举组',
    `field_limit_dict`    VARCHAR(255)  NULL COMMENT '限制枚举值',
    `field_area`          VARCHAR(50)   NULL COMMENT '字段所属区域',
    `field_content`       VARCHAR(4000) NULL,
    `data_type`           VARCHAR(50)   NULL COMMENT '数据类型',
    `is_sys`              TINYINT       NULL DEFAULT '0' COMMENT '是否预设字段（1-预设字段;2-自定义字段）',
    `is_visible`          TINYINT       NULL DEFAULT '0' COMMENT '是否可见',
    `is_required`         TINYINT       NULL DEFAULT '0' COMMENT '是否必填',
    `is_limit_length`     TINYINT       NULL DEFAULT '0' COMMENT '是否控制长度',
    `is_limit_dict`       TINYINT       NULL DEFAULT '0' COMMENT '是否限制枚举值',
    `is_deleted`          TINYINT       NULL DEFAULT '0',
    `status`              VARCHAR(10)   NULL COMMENT '状态',
    `CREATE_BY`           VARCHAR(32)   NULL,
    `CREATE_DATE`         DATETIME      NULL,
    `UPDATE_BY`           VARCHAR(32)   NULL,
    `UPDATE_DATE`         DATETIME      NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应付债权页面字段配置表';
##GOGO


-- 表说明：应付债权凭证数据表自定义字段数据
CREATE TABLE IF NOT EXISTS `scf_voucher_custom_field`
(
    `id`          VARCHAR(32)  NOT NULL COMMENT 'id',
    `voucher_id`  VARCHAR(32)  NOT NULL COMMENT '凭证id',
    `field_id`    VARCHAR(32)  NOT NULL COMMENT '字段id',
    `field_code`  VARCHAR(100) NOT NULL COMMENT '自定义字段编码',
    `field_name`  VARCHAR(200) NOT NULL COMMENT '自定义字段名称',
    `field_type`  VARCHAR(50)  NOT NULL COMMENT '自定义字段类型',
    `field_value` VARCHAR(255) NOT NULL COMMENT '自定义字段值',
    `create_by`   VARCHAR(32)  NULL COMMENT '创建人',
    `create_date` DATETIME     NULL COMMENT '创建日期',
    `update_by`   VARCHAR(32)  NULL COMMENT '更新人',
    `update_date` DATETIME     NULL COMMENT '更新日期',
    `tenant_id`   VARCHAR(32)  NULL COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应付债权凭证数据表自定义字段数据';
##GOGO


-- 表说明：供应链金融平台表
CREATE TABLE IF NOT EXISTS `SCF_PLATFORM`
(
    `ID`            VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `PLATFORM_CODE` VARCHAR(32)  NULL COMMENT '平台编码',
    `PLATFORM_NAME` VARCHAR(100) NULL COMMENT '平台名称',
    `VALID`         VARCHAR(1)   NULL DEFAULT '1' COMMENT '有效标识 1-有效 0-无效',
    `RMK`           VARCHAR(500) NULL COMMENT '备注',
    `CREATE_BY`     VARCHAR(32)  NULL COMMENT '创建人',
    `CREATE_DATE`   DATETIME     NULL COMMENT '创建日期',
    `UPDATE_BY`     VARCHAR(32)  NULL COMMENT '更新人',
    `UPDATE_DATE`   DATETIME     NULL COMMENT '更新日期',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '供应链金融平台表';
##GOGO


-- 表说明：供应链融资产品表
CREATE TABLE IF NOT EXISTS `SCF_PRODUCT`
(
    `ID`           VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `PRODUCT_CODE` VARCHAR(32)  NULL COMMENT '产品编号',
    `PRODUCT_NAME` VARCHAR(200) NULL COMMENT '产品名称',
    `PRODUCT_TYPE` VARCHAR(2)   NULL COMMENT '融资类型',
    `VALID_SIGN`   VARCHAR(1)   NULL DEFAULT '1' COMMENT '有效标识 1-有效 0-无效',
    `PLATFORM_ID`  VARCHAR(32)  NULL COMMENT '所属平台ID',
    `CREATE_BY`    VARCHAR(32)  NULL COMMENT '创建人',
    `CREATE_DATE`  DATETIME     NULL COMMENT '创建日期',
    `UPDATE_BY`    VARCHAR(32)  NULL COMMENT '更新人',
    `UPDATE_DATE`  DATETIME     NULL COMMENT '更新日期',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '供应链融资产品表';
##GOGO


-- 表说明：接口开通情况表
CREATE TABLE IF NOT EXISTS `SCF_BANK_OPEN`
(
    `ID`              VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `TYPE`            VARCHAR(20)  NULL COMMENT '接口类型',
    `API_NAME_CN`     VARCHAR(100) NULL COMMENT '接口名称（中文）',
    `API_NAME`        VARCHAR(100) NULL COMMENT '接口名称',
    `SCF_PLATFORM_ID` VARCHAR(32)  NULL COMMENT '所属平台ID',
    `CREATE_BY`       VARCHAR(32)  NULL COMMENT '创建人',
    `CREATE_DATE`     DATETIME     NULL COMMENT '创建日期',
    `UPDATE_BY`       VARCHAR(32)  NULL COMMENT '更新人',
    `UPDATE_DATE`     DATETIME     NULL COMMENT '更新日期',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '接口开通情况表';
##GOGO

-- ---------------------------------------------------------------------------
-- 第二部分：租户定时任务分配表（来源 dev_20260930_mysql.sql，2026-09-30）
-- ---------------------------------------------------------------------------

-- 表说明：租户定时任务分配表
CREATE TABLE IF NOT EXISTS `SYS_TENANT_SCHEDULED_TASK`
(
    `ID`          VARCHAR(32) NOT NULL COMMENT '主键ID',
    `TENANT_ID`   VARCHAR(32) NOT NULL COMMENT '租户ID(标识哪个客户)',
    `JOB_ID`      INT         NOT NULL COMMENT '定时任务ID(xxl_job_info.id)',
    `CREATE_BY`   VARCHAR(32) NULL COMMENT '创建人',
    `CREATE_TIME` DATETIME    NULL COMMENT '创建时间',
    `UPDATE_BY`   VARCHAR(32) NULL COMMENT '更新人',
    `UPDATE_TIME` DATETIME    NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_TENANT_JOB` (`TENANT_ID`, `JOB_ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '租户定时任务分配表';
##GOGO
