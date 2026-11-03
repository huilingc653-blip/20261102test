-- ============================================================================
-- SaaS 标准化初始化脚本
-- 数据库：MySQL
-- 阶段：09 银企直连表结构
-- 来源：bank-enterprise-ddl.sql
-- 生成日期：2026-07-29
-- 注意：本文件由原始脚本整理生成；禁止反向覆盖 sql/saas/mysql。
-- ============================================================================


-- 表说明：银行字典类型基础表
CREATE TABLE IF NOT EXISTS `BP_BANK_DICT_TYPE`
(
    `CONFIG_TYPE`             VARCHAR(64)  NOT NULL COMMENT '配置类型',
    `CONFIG_TYPE_NAME`        VARCHAR(64)  NOT NULL COMMENT '配置名称',
    `CONFIG_TYPE_DESC`        VARCHAR(256) COMMENT '配置说明',
    `CONFIG_ITEM_KEY_LABEL`   VARCHAR(32)  NOT NULL COMMENT '配置项key label',
    `CONFIG_ITEM_VALUE_LABEL` VARCHAR(32)  NOT NULL COMMENT '配置项value label',
    `CREATED_BY`              VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`            DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATED_BY`              VARCHAR(32)  COMMENT '更新人',
    `UPDATED_TIME`            DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`CONFIG_TYPE`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行字典类型基础表';


-- 表说明：银行通用交易配置类型
CREATE TABLE IF NOT EXISTS `BP_BANK_TXN_CONFIG_TYPE`
(
    `CONFIG_TYPE`             VARCHAR(64)  NOT NULL COMMENT '配置类型',
    `CONFIG_TYPE_NAME`        VARCHAR(64)  NOT NULL COMMENT '配置名称',
    `CONFIG_TYPE_DESC`        VARCHAR(256) COMMENT '配置说明',
    `CONFIG_ITEM_KEY_LABEL`   VARCHAR(32)  NOT NULL COMMENT '配置项key label',
    `CONFIG_ITEM_VALUE_LABEL` VARCHAR(32)  NOT NULL COMMENT '配置项value label',
    `CREATED_BY`              VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`            DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATED_BY`              VARCHAR(32)  COMMENT '更新人',
    `UPDATED_TIME`            DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`CONFIG_TYPE`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行通用交易配置类型';


-- 表说明：平台交易支持表
CREATE TABLE IF NOT EXISTS `BP_BANK_TXN_SUPPORTED_INFO`
(
    `TRADE_NAME`    VARCHAR(32)  NOT NULL COMMENT '平台交易码',
    `TRADE_CN_NAME` VARCHAR(64)  COMMENT '交易中文名称',
    `REMARKS`       VARCHAR(128) COMMENT '备注',
    `SORT`          INT          COMMENT '排序',
    `TRADE_TYPE`    INT          COMMENT '交易类型：0-平台，1-银行交易',
    `CREATED_BY`    VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`TRADE_NAME`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '平台交易支持表';


-- 表说明：银行接口定时任务基础表
CREATE TABLE IF NOT EXISTS `BP_BANK_SCHEDULED_TASK_BASE`
(
    `TASK_CODE`       VARCHAR(64)  NOT NULL COMMENT '定时任务唯一编码',
    `TASK_NAME`       VARCHAR(64)  NOT NULL COMMENT '定时任务名称',
    `CRON_EXPRESSION` VARCHAR(32)  COMMENT 'cron表达式',
    `HANDLE_CLASS`    VARCHAR(255) NOT NULL COMMENT '任务处理器类的全限定名（Spring Bean名称或类路径）',
    `HANDLE_PARAMS`   VARCHAR(255) COMMENT '任务参数（JSON格式，如 {"serviceCode":"BankInfoAcc"})',
    `TASK_TYPE`       INT          COMMENT '定时任务类型：0-平台，1-银行交易',
    `SORT`            INT          NOT NULL COMMENT '排序',
    `CREATED_BY`      VARCHAR(128) COMMENT '创建人',
    `CREATED_TIME`    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`TASK_CODE`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行接口定时任务基础表';


-- 表说明：平台交易与定时任务关系表
CREATE TABLE IF NOT EXISTS `BP_BANK_TXN_SCHEDULED_TASK`
(
    `ID`           VARCHAR(32) NOT NULL COMMENT '主键ID',
    `TRADE_NAME`   VARCHAR(32) NOT NULL COMMENT '平台交易码',
    `TASK_CODE`    VARCHAR(64) NOT NULL COMMENT '定时任务唯一编码',
    `CREATED_BY`   VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME` DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '平台交易与定时任务关系表';

CREATE UNIQUE INDEX `IDX_SM_TN_TC` ON BP_BANK_TXN_SCHEDULED_TASK (
                                                                  `TRADE_NAME` ASC,
                                                                  `TASK_CODE` ASC
    );


-- 表说明：银行接口规则字段表
CREATE TABLE IF NOT EXISTS `BP_BANK_RULE_FIELD_TYPE`
(
    `ID`               VARCHAR(32)   NOT NULL COMMENT 'ID',
    `FIELD_NAME`       VARCHAR(64)   NOT NULL COMMENT '字段名称',
    `FIELD_MARK`       VARCHAR(32)   NOT NULL COMMENT '字段标志',
    `DATA_TYPE`        VARCHAR(32)   NOT NULL COMMENT '字段类型  1-数字; 2-字符串; 3-日期; 4-枚举;',
    `CRITERIA`         VARCHAR(1000) NOT NULL COMMENT '查询条件json',
    `FIELD_VALUE_ENUM` VARCHAR(255)  COMMENT '字段值枚举',
    `SORT`             INT           COMMENT '排序',
    `CREATED_BY`       VARCHAR(32)   COMMENT '创建人',
    `CREATED_TIME`     DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行接口规则字段表';


-- 表说明：银行接口版本表
CREATE TABLE IF NOT EXISTS `BP_BANK_VERSION`
(
    `BANK_CODE`         VARCHAR(32)  NOT NULL COMMENT '银行版本',
    `BANK_VERSION_NAME` VARCHAR(32)  NOT NULL COMMENT '银行版本名称',
    `BANK_NAME`         VARCHAR(32)  NOT NULL COMMENT '银行名称',
    `VERSION_DESC`      VARCHAR(128) COMMENT '版本说明',
    `LOGON_FLAG`        INT          NOT NULL COMMENT '银行前置机登录标识：1-需要登录，0-不需要登录',
    `CREATED_BY`        VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATED_BY`        VARCHAR(32)  COMMENT '更新人',
    `UPDATED_TIME`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`BANK_CODE`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行接口版本表';


-- 表说明：银行版本与银行字典类型关系表(BP_BANK_VERSION_DICT_TYPE_CONFIG)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_DICT_TYPE_CONFIG`
(
    `ID`                      VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `BANK_CODE`               VARCHAR(32)  NOT NULL COMMENT '银行版本简称',
    `SYS_MODE`                VARCHAR(8)   NOT NULL COMMENT '系统模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `CONFIG_TYPE`             VARCHAR(64)  NOT NULL COMMENT '配置类型',
    `CONFIG_TYPE_NAME`        VARCHAR(64)  COMMENT '配置名称',
    `CONFIG_TYPE_DESC`        VARCHAR(256) COMMENT '配置说明',
    `CONFIG_ITEM_KEY_LABEL`   VARCHAR(32)  COMMENT '配置项key label',
    `CONFIG_ITEM_VALUE_LABEL` VARCHAR(32)  COMMENT '配置项value label',
    `CREATED_BY`              VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`            DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行版本与银行字典类型关系表(BP_BANK_VERSION_DICT_TYPE_CONFIG)';


-- 表说明：银行版本与银行字典类型数据配置(BP_BANK_VERSION_DICT_TYPE_DATA)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_DICT_TYPE_DATA`
(
    `ID`                         VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_V_DICT_TYPE_CONFIG` VARCHAR(32) NOT NULL COMMENT '银行版本与银行字典类型关系表ID，来源：BP_BANK_V_DICT_TYPE_CONFIG',
    `CONFIG_ITEM_KEY`            VARCHAR(64) NOT NULL COMMENT '配置项key',
    `CONFIG_ITEM_VALUE`          VARCHAR(64) NOT NULL COMMENT '配置项value',
    `CREATED_BY`                 VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`               DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行版本与银行字典类型数据配置(BP_BANK_VERSION_DICT_TYPE_DATA)';


-- 表说明：银行版本与交易配置类型关系表(BP_BANK_VERSION_TXN_CONFIG_TYPE)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_TXN_CONFIG_TYPE`
(
    `ID`                      VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `BANK_CODE`               VARCHAR(32)  NOT NULL COMMENT '银行版本简称',
    `SYS_MODE`                VARCHAR(8)   NOT NULL COMMENT '系统模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `TRADE_NAME`              VARCHAR(32)  NOT NULL COMMENT '平台交易码',
    `CONFIG_TYPE`             VARCHAR(64)  COMMENT '配置类型',
    `CONFIG_TYPE_NAME`        VARCHAR(64)  COMMENT '配置名称',
    `CONFIG_TYPE_DESC`        VARCHAR(256) COMMENT '配置说明',
    `CONFIG_ITEM_KEY_LABEL`   VARCHAR(32)  COMMENT '配置项key label',
    `CONFIG_ITEM_VALUE_LABEL` VARCHAR(32)  COMMENT '配置项value label',
    `CREATED_BY`              VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`            DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行版本与交易配置类型关系表(BP_BANK_VERSION_TXN_CONFIG_TYPE)';


-- 表说明：银行版本与交易类型数据配置(BP_BANK_VERSION_CONFIG_TYPE_DATA)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_CONFIG_TYPE_DATA`
(
    `ID`                        VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_V_TXN_CONFIG_TYPE` VARCHAR(32) NOT NULL COMMENT '银行版本与交易配置类型关系表ID，来源：BP_BANK_V_TXN_CONFIG_TYPE',
    `CONFIG_ITEM_KEY`           VARCHAR(64) NOT NULL COMMENT '配置项key',
    `CONFIG_ITEM_VALUE`         VARCHAR(64) NOT NULL COMMENT '配置项value',
    `CREATED_BY`                VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`              DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATED_BY`                VARCHAR(32) COMMENT '更新人',
    `UPDATED_TIME`              DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行版本与交易类型数据配置(BP_BANK_VERSION_CONFIG_TYPE_DATA)';


-- 表说明：银行版本交易参数配置字典表(BP_BANK_VERSION_PARAM_CONFIG)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_PARAM_CONFIG`
(
    `ID`                 VARCHAR(32)   NOT NULL COMMENT '主键ID',
    `SYS_MODE`           VARCHAR(8)    NOT NULL COMMENT '系统模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `BANK_CODE`          VARCHAR(32)   NOT NULL COMMENT '银行版本',
    `CONFIG_PARAM_NAME`  VARCHAR(32)   NOT NULL COMMENT '参数值中文说明',
    `CONFIG_PARAM_CODE`  VARCHAR(128)  NOT NULL COMMENT '参数值英文code',
    `CONFIG_PARAM_VALUE` VARCHAR(2000) COMMENT '参数值value',
    `CONFIG_PARAM_DESC`  VARCHAR(100)  COMMENT '参数值说明',
    `PARAM_TYPE`         VARCHAR(2)    NOT NULL COMMENT '参数类型（1:文本框，2:数字，3:金额，4: 日期，5: 下拉框，6:单选框，7:复选框, 8:开关）',
    `OPTION_VALUES`      VARCHAR(255)  COMMENT '参数可选值（针对下拉框、单选及复选框，值如：1:是;0:否。其它可为空）',
    `SORT`               INT           COMMENT '排序',
    `CREATED_BY`         VARCHAR(32)   COMMENT '创建人',
    `CREATED_TIME`       DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行版本交易参数配置字典表(BP_BANK_VERSION_PARAM_CONFIG)';


-- 表说明：银行配置文件内容配置(BP_BANK_VERSION_FILE_CONFIG)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_FILE_CONFIG`
(
    `ID`                   VARCHAR(32) NOT NULL COMMENT '主键ID',
    `SYS_MODE`             VARCHAR(8)  NOT NULL COMMENT '系统模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `BANK_CODE`            VARCHAR(32) NOT NULL COMMENT '银行版本',
    `CONFIG_FILE_ENCODING` VARCHAR(32) COMMENT '配置文件编码',
    `CONFIG_FILE_NAME`     VARCHAR(32) NOT NULL COMMENT '配置文件名称',
    `CONFIG_FILE_CONENT`   TEXT        NOT NULL COMMENT '配置文件内容',
    `CREATED_BY`           VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`         DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行配置文件内容配置(BP_BANK_VERSION_FILE_CONFIG)';


-- 表说明：银行版本交易支持表(BP_BANK_VERSION_TXN_SUPPORT)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_TXN_SUPPORT`
(
    `ID`           VARCHAR(32) NOT NULL COMMENT '主键ID',
    `BANK_CODE`    VARCHAR(32) NOT NULL COMMENT '银行版本',
    `SYS_MODE`     VARCHAR(8)  NOT NULL COMMENT '系统模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `TRADE_NAME`   VARCHAR(32) NOT NULL COMMENT '平台交易码',
    `CREATED_BY`   VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME` DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行版本交易支持表(BP_BANK_VERSION_TXN_SUPPORT)';


-- 表说明：银行支持交易规则配置表(BP_BANK_VERSION_TXN_RULE_CONFIG)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_TXN_RULE_CONFIG`
(
    `ID`                    VARCHAR(32)  NOT NULL COMMENT 'ID',
    `ID_BANK_V_TXN_SUPPORT` VARCHAR(32)  NOT NULL COMMENT '银行版本交易表ID，来源：BP_BANK_VERSION_TXN_SUPPORT',
    `PARAM_NAME`            VARCHAR(32)  NOT NULL COMMENT '交易规则参数名',
    `PARAM_CODE`            VARCHAR(32)  NOT NULL COMMENT '交易规则参数CODE',
    `PARAM_VALUE`           VARCHAR(100) COMMENT '交易规则参数值',
    `PARAM_TYPE`            VARCHAR(2)   NOT NULL COMMENT '参数类型（1:文本框，2:数字，3:金额，4: 日期，5: 下拉框，6:单选框，7:复选框, 8:开关）',
    `OPTION_VALUES`         VARCHAR(255) COMMENT '参数可选值（针对下拉框、单选及复选框，值如：1:是;0:否。其它可为空）',
    `RULE_DESC`             VARCHAR(255) COMMENT '交易规则描述',
    `CREATED_BY`            VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`          DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行支持交易规则配置表(BP_BANK_VERSION_TXN_RULE_CONFIG)';


-- 表说明：平台交易和客户端交易码关系表(BP_BANK_VERSION_TXN_CCODE);银行端交易为单接口调用时，客户端交易码与银行交易码一致，当需要多接口编排时，为多接口交易码拼接
CREATE TABLE IF NOT EXISTS `BP_BANK_V_TXN_CCODE`
(
    `ID`                VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `BANK_CODE`         VARCHAR(16)  NOT NULL COMMENT '银行版本',
    `SYS_MODE`          VARCHAR(8)   NOT NULL COMMENT '系统模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `TRADE_NAME`        VARCHAR(32)  NOT NULL COMMENT '平台交易码',
    `CLIENT_TRANS_CODE` VARCHAR(64)  NOT NULL COMMENT '客户端交易码',
    `CLIENT_TRANS_NAME` VARCHAR(128) NOT NULL COMMENT '客户端交易码名称',
    `CLIENT_TRANS_DESC` VARCHAR(255) COMMENT '客户端交易码交易码描述',
    `CREATED_BY`        VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '平台交易和客户端交易码关系表(BP_BANK_VERSION_TXN_CCODE);银行端交易为单接口调用时，客户端交易码与银行交易码一致，当需要多接口编排时，为多接口交易码拼接';


-- 表说明：客户端交易码和银行交易码关系表(BP_BANK_VERSION_TXN_C_BCODE)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_TXN_C_BCODE`
(
    `ID`                  VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `ID_BANK_V_TXN_CCODE` VARCHAR(32)  NOT NULL COMMENT '平台交易和客户端交易码关系表ID，来源：BP_BANK_V_TXN_CCODE',
    `BANK_TRANS_CODE`     VARCHAR(64)  NOT NULL COMMENT '银行交易码',
    `BANK_TRANS_NAME`     VARCHAR(64)  NOT NULL COMMENT '银行交易码名称',
    `BANK_TRANS_DESC`     VARCHAR(255) COMMENT '银行交易码描述',
    `CREATED_BY`          VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`        DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '客户端交易码和银行交易码关系表(BP_BANK_VERSION_TXN_C_BCODE)';


-- 表说明：银行版本交易路由策略表(BP_BANK_VERSION_TXN_ROUTING_POLICY)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_TXN_ROUTE_POLICY`
(
    `ID`                  VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `BANK_CODE`           VARCHAR(16)  NOT NULL COMMENT '银行版本',
    `SYS_MODE`            VARCHAR(8)   NOT NULL COMMENT '系统模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `TRADE_NAME`          VARCHAR(32)  NOT NULL COMMENT '平台交易码',
    `STATUS`              INT          NOT NULL COMMENT '状态：0-禁用，1-启用',
    `POLICY_NAME`         VARCHAR(64)  NOT NULL COMMENT '策略名称',
    `ID_BANK_V_TXN_CCODE` VARCHAR(32)  NOT NULL COMMENT '平台交易和客户端交易码关系表ID，来源：BP_BANK_V_TXN_CCODE',
    `POLICY_EXPRESSION`   VARCHAR(255) COMMENT '策略表达式',
    `CREATED_BY`          VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`        DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY`           VARCHAR(32)  COMMENT '更新人',
    `UPDATE_TIME`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行版本交易路由策略表(BP_BANK_VERSION_TXN_ROUTING_POLICY)';


-- 表说明：银行版本交易路由规则表(BP_BANK_VERSION_TXN_ROUTING_RULES)
CREATE TABLE IF NOT EXISTS `BP_BANK_V_TXN_ROUTE_RULES`
(
    `ID`                           VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_V_TXN_ROUTING_POLICY` VARCHAR(32) NOT NULL COMMENT '银行版本交易路由策略表表ID，来源：BP_BANK_V_TXN_ROUTING_POLICY',
    `LEFT_SYMBOL`                  VARCHAR(10) COMMENT '左括号',
    `RIGHT_SYMBOL`                 VARCHAR(10) COMMENT '右括号',
    `FIELD_MARK`                   VARCHAR(32) NOT NULL COMMENT '字段',
    `FIELD_CONDITIONAL`            VARCHAR(10) NOT NULL COMMENT '条件',
    `FIELD_VALUE`                  VARCHAR(64) NOT NULL COMMENT '规则值',
    `FIELD_LOGIC`                  VARCHAR(16) COMMENT '逻辑',
    `RULE_INDEX`                   INT         COMMENT '规则序号',
    `CREATED_BY`                   VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`                 DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY`                    VARCHAR(32) COMMENT '更新人',
    `UPDATE_TIME`                  DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '银行版本交易路由规则表(BP_BANK_VERSION_TXN_ROUTING_RULES)';


-- 表说明：应用包管理表;应用包的控制：控制应用包的使用状态，若是停用，该应用不能使用，控制使用该应用的所有应用
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_PACKAGE_INFO`
(
    `ID`             VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `SYS_MODE`       VARCHAR(8)   NOT NULL COMMENT '系统业务模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `BANK_CODE`      VARCHAR(32)  NOT NULL COMMENT '银行版本',
    `APP_NAME`       VARCHAR(64)  NOT NULL COMMENT '应用包名称',
    `APP_VERSION`    VARCHAR(8)   NOT NULL COMMENT '应用包版本号',
    `APP_DESC`       VARCHAR(255) COMMENT '应用包描述',
    `APP_FILE_PATH`  VARCHAR(255) NOT NULL COMMENT '应用包文件存放路径',
    `FILE_SAVE_TYPE` INT          COMMENT '文件存储方式：1-sftp，2-minio',
    `ATTACHMENT_ID`  VARCHAR(32)  NOT NULL COMMENT '文件表ID，来源：sys_attachment_manager',
    `APP_STATUS`     INT          NOT NULL COMMENT '应用包状态：0-停用，1-启用',
    `CREATED_BY`     VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATED_BY`     VARCHAR(32)  COMMENT '更新人',
    `UPDATED_TIME`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包管理表;应用包的控制：控制应用包的使用状态，若是停用，该应用不能使用，控制使用该应用的所有应用';


-- 表说明：应用包银行版本交易参数配置字典表
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_PARAM_CONFIG`
(
    `ID`                       VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_APP_PACKAGE_INFO` VARCHAR(32) NOT NULL COMMENT '应用包ID，来源：BP_BANK_APP_PACKAGE_INFO',
    `ID_BANK_V_PARAM_CONFIG`   VARCHAR(32) NOT NULL COMMENT '银行版本交易参数配置字典表ID，来源：BP_BANK_V_PARAM_CONFIG',
    `CREATED_BY`               VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`             DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包银行版本交易参数配置字典表';


-- 表说明：应用包版本银行配置文件内容配置
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_FILE_CONFIG`
(
    `ID`                       VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_APP_PACKAGE_INFO` VARCHAR(32) NOT NULL COMMENT '应用包ID，来源：BP_BANK_APP_PACKAGE_INFO',
    `ID_BANK_V_FILE_CONFIG`    VARCHAR(32) NOT NULL COMMENT '银行版本银行配置文件内容配置ID，来源：BP_BANK_V_FILE_CONFIG',
    `CREATED_BY`               VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`             DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包版本银行配置文件内容配置';


-- 表说明：应用包银行版本交易支持表
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_TXN_SUPPORT`
(
    `ID`                       VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_APP_PACKAGE_INFO` VARCHAR(32) NOT NULL COMMENT '应用包ID，来源：BP_BANK_APP_PACKAGE_INFO',
    `ID_BANK_V_TXN_SUPPORT`    VARCHAR(32) NOT NULL COMMENT '银行版本交易支持表ID，来源：BP_BANK_V_TXN_SUPPORT',
    `CREATED_BY`               VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`             DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包银行版本交易支持表';


-- 表说明：应用包银行支持交易规则配置表
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_TXN_RULE_CONFIG`
(
    `ID`                        VARCHAR(32) NOT NULL COMMENT 'ID',
    `ID_BANK_APP_TXN_SUPPORT`   VARCHAR(32) NOT NULL COMMENT '应用包银行版本交易支持表ID，来源：BP_BANK_APP_TXN_SUPPORT',
    `ID_BANK_V_TXN_RULE_CONFIG` VARCHAR(32) NOT NULL COMMENT '银行支持交易规则配置表ID，来源：BP_BANK_V_TXN_RULE_CONFIG',
    `CREATED_BY`                VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`              DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包银行支持交易规则配置表';


-- 表说明：应用包银行版本与交易配置类型关系表
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_TXN_CONFIG_TYPE`
(
    `ID`                        VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_APP_PACKAGE_INFO`  VARCHAR(32) NOT NULL COMMENT '应用包ID，来源：BP_BANK_APP_PACKAGE_INFO',
    `ID_BANK_V_TXN_CONFIG_TYPE` VARCHAR(32) NOT NULL COMMENT '银行版本与交易配置类型关系表表ID，来源：BP_BANK_V_TXN_CONFIG_TYPE',
    `CREATED_BY`                VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`              DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包银行版本与交易配置类型关系表';


-- 表说明：应用包银行版本与交易类型数据配置
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_CONFIG_TYPE_DATA`
(
    `ID`                          VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_APP_TXN_CONFIG_TYPE` VARCHAR(32) NOT NULL COMMENT '应用包银行版本与交易配置类型关系表ID，来源：BP_BANK_APP_TXN_CONFIG_TYPE',
    `ID_BANK_V_CONFIG_TYPE_DATA`  VARCHAR(32) NOT NULL COMMENT '银行版本与交易类型数据配置表ID，来源：BP_BANK_V_CONFIG_TYPE_DATA',
    `CREATED_BY`                  VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`                DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包银行版本与交易类型数据配置';


-- 表说明：应用包平台交易和客户端交易码关系表;用于客户端进行交易路由，客户端交易码名称为平台聚合业务自定义交易码
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_TXN_CCODE`
(
    `ID`                       VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_APP_PACKAGE_INFO` VARCHAR(32) NOT NULL COMMENT '应用包ID，来源：BP_BANK_APP_PACKAGE_INFO',
    `ID_BANK_V_TXN_CCODE`      VARCHAR(32) NOT NULL COMMENT '平台交易和客户端交易码关系表ID，来源：BP_BANK_V_TXN_CCODE',
    `CREATED_BY`               VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`             DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包平台交易和客户端交易码关系表;用于客户端进行交易路由，客户端交易码名称为平台聚合业务自定义交易码';


-- 表说明：应用包客户端交易码和银行交易码关系表
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_TXN_C_BCODE`
(
    `ID`                    VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_APP_TXN_CCODE` VARCHAR(32) NOT NULL COMMENT '应用包平台交易和客户端交易码关系表ID，来源：BP_BANK_APP_TXN_CCODE',
    `ID_BANK_V_TXN_C_BCODE` VARCHAR(32) NOT NULL COMMENT '客户端交易码和银行交易码关系表ID，来源：BP_BANK_V_TXN_C_BCODE',
    `CREATED_BY`            VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`          DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包客户端交易码和银行交易码关系表';


-- 表说明：应用包交易路由策略表(BP_BANK_APP_TXN_ROUTING_POLICY)
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_TXN_ROUTE_POLICY`
(
    `ID`                         VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_APP_PACKAGE_INFO`   VARCHAR(32) NOT NULL COMMENT '应用包ID，来源：BP_BANK_APP_PACKAGE_INFO',
    `ID_BANK_V_TXN_ROUTE_POLICY` VARCHAR(32) NOT NULL COMMENT '银行版本交易路由策略表ID，来源：BP_BANK_V_TXN_ROUTE_POLICY',
    `CREATED_BY`                 VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`               DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY`                  VARCHAR(32) COMMENT '更新人',
    `UPDATE_TIME`                DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包交易路由策略表(BP_BANK_APP_TXN_ROUTING_POLICY)';


-- 表说明：应用包交易路由规则表(BP_BANK_APP_TXN_ROUTING_RULES)
CREATE TABLE IF NOT EXISTS `BP_BANK_APP_TXN_ROUTE_RULES`
(
    `ID`                             VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_BANK_APP_TXN_ROUTING_POLICY` VARCHAR(32) NOT NULL COMMENT '应用包交易路由策略表ID，来源：BP_BANK_APP_TXN_ROUTING_POLICY',
    `ID_BANK_V_TXN_ROUTE_RULES`      VARCHAR(32) NOT NULL COMMENT '银行版本交易路由规则表ID，来源：BP_BANK_V_TXN_ROUTE_RULES',
    `CREATED_BY`                     VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`                   DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY`                      VARCHAR(32) COMMENT '更新人',
    `UPDATE_TIME`                    DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包交易路由规则表(BP_BANK_APP_TXN_ROUTING_RULES)';


-- 表说明：企业配置管理表
CREATE TABLE IF NOT EXISTS `BP_TENANT_CONFIG_MANAGER`
(
    `ID`           VARCHAR(32) NOT NULL COMMENT '主键ID',
    `TENANT_CODE`  VARCHAR(32) NOT NULL COMMENT '企业客户号，来源于sys_tenant_manager',
    `BIF_CODE`     VARCHAR(8)  NOT NULL COMMENT '接口代码',
    `DEPLOY_NODE`  VARCHAR(8)  NOT NULL COMMENT '部署节点',
    `CREATED_BY`   VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME` DATETIME    COMMENT '创建时间',
    `UPDATED_BY`   VARCHAR(32) COMMENT '更新人',
    `UPDATED_TIME` DATETIME    COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业配置管理表';


-- 表说明：企业版本管理表
CREATE TABLE IF NOT EXISTS `BP_TENANT_VERSION_MANAGER`
(
    `ID`                         VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_TENANT_CONFIG_MANAGER`   VARCHAR(32) NOT NULL COMMENT '企业配置管理表ID，来源：BP_TENANT_CONFIG_MANAGER',
    `ID_TENANT_BANK_APP_MANAGER` VARCHAR(32) NOT NULL COMMENT '企业应用包管理表ID，来源：BP_TENANT_BANK_APP_MANGER',
    `SYS_MODE`                   VARCHAR(8)  NOT NULL COMMENT '系统业务模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `TENANT_CODE`                VARCHAR(32) NOT NULL COMMENT '企业客户号，来源于sys_tenant_manager',
    `BIF_CODE`                   VARCHAR(8)  NOT NULL COMMENT '接口代码',
    `DEPLOY_NODE`                VARCHAR(8)  NOT NULL COMMENT '部署节点',
    `CREATED_BY`                 VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`               DATETIME    COMMENT '创建时间',
    `UPDATED_BY`                 VARCHAR(32) COMMENT '更新人',
    `UPDATED_TIME`               DATETIME    COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业版本管理表';


-- 表说明：企业应用包管理表
CREATE TABLE IF NOT EXISTS `BP_TENANT_BANK_APP_MANAGER`
(
    `ID`                       VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_TENANT_CONFIG_MANAGER` VARCHAR(32) NOT NULL COMMENT '企业配置管理表ID，来源：BP_TENANT_CONFIG_MANAGER',
    `ID_BANK_APP_PACKAGE_INFO` VARCHAR(32) NOT NULL COMMENT '应用包版本ID，来源于BP_BANK_APP_PACKAGE_INFO',
    `SYS_MODE`                 VARCHAR(8)  NOT NULL COMMENT '系统业务模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `TENANT_CODE`              VARCHAR(32) NOT NULL COMMENT '企业客户号，来源于sys_tenant_manager',
    `BIF_CODE`                 VARCHAR(8)  NOT NULL COMMENT '接口代码',
    `DEPLOY_NODE`              VARCHAR(8)  NOT NULL COMMENT '部署节点',
    `APP_STATUS`               INT         NOT NULL DEFAULT -1 COMMENT '业务状态：-1：待生效 0：停用，1：启用，2-部署失败',
    `FIRST_DEPLOY_TIME`        DATE        COMMENT '第一次成功部署时间',
    `CREATED_BY`               VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`             DATETIME    COMMENT '创建时间',
    `UPDATED_BY`               VARCHAR(32) COMMENT '更新人',
    `UPDATED_TIME`             DATETIME    COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业应用包管理表';


-- 表说明：企业银行版本交易参数配置表（BP_TENANT_BANK_APP_PARAM_CONFIG）
CREATE TABLE IF NOT EXISTS `BP_TENANT_PARAM_CONFIG`
(
    `ID`                         VARCHAR(32)   NOT NULL COMMENT '主键ID',
    `ID_TENANT_BANK_APP_MANAGER` VARCHAR(32)   NOT NULL COMMENT '企业应用包管理表ID，来源：BP_TENANT_BANK_APP_MANAGER',
    `ID_BANK_APP_PARAM_CONFIG`   VARCHAR(32)   NOT NULL COMMENT '应用包银行版本交易参数配置字典表ID企业应用包管理表ID，来源：BP_BANK_APP_PARAM_CONFIG',
    `CONFIG_PARAM_VALUE`         VARCHAR(2000) COMMENT '参数值value',
    `CREATED_BY`                 VARCHAR(32)   COMMENT '创建人',
    `CREATED_TIME`               DATETIME      COMMENT '创建时间',
    `UPDATED_BY`                 VARCHAR(32)   COMMENT '更新人',
    `UPDATED_TIME`               DATETIME      COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业银行版本交易参数配置表（BP_TENANT_BANK_APP_PARAM_CONFIG）';


-- 表说明：企业银行版本银行配置文件内容配置表（BP_TENANT_BANK_APP_FILE_CONFIG）
CREATE TABLE IF NOT EXISTS `BP_TENANT_FILE_CONFIG`
(
    `ID`                         VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_TENANT_BANK_APP_MANAGER` VARCHAR(32) NOT NULL COMMENT '企业应用包管理表ID，来源：BP_TENANT_BANK_APP_MANAGER',
    `ID_BANK_APP_FILE_CONFIG`    VARCHAR(32) NOT NULL COMMENT '应用包版本银行配置文件内容配置表ID，来源：BP_BANK_APP_FILE_CONFIG',
    `CONFIG_FILE_CONENT`         TEXT        NOT NULL COMMENT '配置文件内容',
    `CREATED_BY`                 VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`               DATETIME    COMMENT '创建时间',
    `UPDATED_BY`                 VARCHAR(32) COMMENT '更新人',
    `UPDATED_TIME`               DATETIME    COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业银行版本银行配置文件内容配置表（BP_TENANT_BANK_APP_FILE_CONFIG）';


-- 表说明：企业银行APP与交易配置类型关系表（BP_TENANT_BANK_APP_TXN_CONFIG_TYPE）
CREATE TABLE IF NOT EXISTS `BP_TENANT_TXN_CONFIG_TYPE`
(
    `ID`                          VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_TENANT_BANK_APP_MANAGER`  VARCHAR(32) NOT NULL COMMENT '企业应用包管理表ID，来源：BP_TENANT_BANK_APP_MANAGER',
    `ID_BANK_APP_TXN_CONFIG_TYPE` VARCHAR(32) COMMENT '应用包银行版本与交易配置类型关系表ID，来源：BP_BANK_APP_TXN_CONFIG_TYPE',
    `CREATED_BY`                  VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`                DATETIME    COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业银行APP与交易配置类型关系表（BP_TENANT_BANK_APP_TXN_CONFIG_TYPE）';


-- 表说明：企业银行应用与交易类型数据配置（BP_TENANT_BANK_APP_CONFIG_TYPE_DATA）
CREATE TABLE IF NOT EXISTS `BP_TENANT_CONFIG_TYPE_DATA`
(
    `ID`                        VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_TENANT_TXN_CONFIG_TYPE` VARCHAR(32) NOT NULL COMMENT '企业银行APP与交易配置类型关系表ID，来源：BP_TENANT_TXN_CONFIG_TYPE',
    `CONFIG_ITEM_KEY`           VARCHAR(64) NOT NULL COMMENT '配置项key',
    `CONFIG_ITEM_VALUE`         VARCHAR(64) NOT NULL COMMENT '配置项value',
    `CREATED_BY`                VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`              DATETIME    COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业银行应用与交易类型数据配置（BP_TENANT_BANK_APP_CONFIG_TYPE_DATA）';


-- 表说明：企业银行应用包交易支持表（BP_TENANT_BANK_APP_TXN_SUPPORT）
CREATE TABLE IF NOT EXISTS `BP_TENANT_TXN_SUPPORT`
(
    `ID`                         VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_TENANT_BANK_APP_MANAGER` VARCHAR(32) NOT NULL COMMENT '企业应用包管理表ID，来源于BP_TENANT_BANK_APP_MANAGER',
    `ID_BANK_APP_TXN_SUPPORT`    VARCHAR(32) COMMENT '应用包银行版本交易支持表ID，来源：BP_BANK_APP_TXN_SUPPORT',
    `CREATED_BY`                 VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`               DATETIME    COMMENT '创建时间',
    `UPDATED_BY`                 VARCHAR(32) COMMENT '更新人',
    `UPDATED_TIME`               DATETIME    COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业银行应用包交易支持表（BP_TENANT_BANK_APP_TXN_SUPPORT）';


-- 表说明：应用包银行支持交易规则配置表（BP_TENANT_BANK_APP_TXN_RULE_CONFIG）
CREATE TABLE IF NOT EXISTS `BP_TENANT_TXN_RULE_CONFIG`
(
    `ID`                          VARCHAR(32)  NOT NULL COMMENT 'ID',
    `ID_TENANT_TXN_SUPPORT`       VARCHAR(32)  NOT NULL COMMENT '企业银行版本交易支持表ID，来源：BP_TENANT_TXN_SUPPORT',
    `ID_BANK_APP_TXN_RULE_CONFIG` VARCHAR(32)  NOT NULL COMMENT '应用包银行支持交易规则配置表ID，来源：BP_BANK_APP_TXN_RULE_CONFIG',
    `PARAM_VALUE`                 VARCHAR(100) COMMENT '交易规则参数值',
    `CREATED_BY`                  VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`                DATETIME     COMMENT '创建时间',
    `UPDATED_BY`                  VARCHAR(32)  COMMENT '更新人',
    `UPDATED_TIME`                DATETIME     COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '应用包银行支持交易规则配置表（BP_TENANT_BANK_APP_TXN_RULE_CONFIG）';


-- 表说明：企业银行应用定时任务表（BP_TENANT_BANK_APP_SCHEDULED_TASK）
CREATE TABLE IF NOT EXISTS `BP_TENANT_SCHEDULED_TASK`
(
    `ID`                         VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `ID_TENANT_BANK_APP_MANAGER` VARCHAR(32)  NOT NULL COMMENT '企业应用包管理表ID，来源于BP_TENANT_BANK_APP_MANAGER',
    `TASK_CODE`                  VARCHAR(32)  NOT NULL COMMENT '银行接口定时任务基础表ID，来源：BP_BANK_SCHEDULED_TASK_BASE',
    `TAST_STATUS`                INT          NOT NULL COMMENT '定时任务状态：0-关闭，1-开启',
    `CRON_EXPRESSION`            VARCHAR(32)  COMMENT 'cron表达式',
    `CREATED_BY`                 VARCHAR(128) COMMENT '创建人',
    `CREATED_TIME`               DATETIME     COMMENT '创建时间',
    `UPDATED_BY`                 VARCHAR(32)  COMMENT '更新人',
    `UPDATED_TIME`               DATETIME     COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业银行应用定时任务表（BP_TENANT_BANK_APP_SCHEDULED_TASK）';


-- 表说明：企业应用包支持交易与定时任务关系表（BP_TENANT_BANK_APP_TXN_SCHEDULED_TASK）
CREATE TABLE IF NOT EXISTS `BP_TENANT_TXN_SCHEDULED_TASK`
(
    `ID`                       VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_TENANT_TXN_SUPPORT`    VARCHAR(32) NOT NULL COMMENT '企业银行应用包交易支持表ID，来源：BP_TENANT_TXN_SUPPORT',
    `ID_TENANT_SCHEDULED_TASK` VARCHAR(32) NOT NULL COMMENT '企业银行应用定时任务表ID，来源：BP_TENANT_SCHEDULED_TASK',
    `CREATED_BY`               VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`             DATETIME    COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业应用包支持交易与定时任务关系表（BP_TENANT_BANK_APP_TXN_SCHEDULED_TASK）';


-- 表说明：企业银行应用包交易路由策略表（BP_TENANT_BANK_APP_TXN_ROUTING_POLICY）
CREATE TABLE IF NOT EXISTS `BP_TENANT_TXN_ROUTE_POLICY`
(
    `ID`                         VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `ID_TENANT_BANK_APP_MANAGER` VARCHAR(32)  NOT NULL COMMENT '企业应用包管理表ID，来源于BP_TENANT_BANK_APP_MANAGER',
    `TRADE_NAME`                 VARCHAR(32)  NOT NULL COMMENT '平台交易码',
    `STATUS`                     INT          NOT NULL COMMENT '状态：0-禁用，1-启用',
    `RULES_TYPE`                 INT          NOT NULL COMMENT '规则类型：0-兜底规则;1-定制规则',
    `POLICY_NAME`                VARCHAR(64)  NOT NULL COMMENT '策略名称',
    `PRIORITY`                   INT          COMMENT '优先级',
    `PARENT_ID`                  VARCHAR(32)  COMMENT '父ID',
    `BANK_TRANS_CODE`            VARCHAR(64)  NOT NULL COMMENT '银行交易码',
    `BANK_TRANS_NAME`            VARCHAR(64)  NOT NULL COMMENT '银行交易码名称',
    `BANK_TRANS_DESC`            VARCHAR(255) COMMENT '银行交易码描述',
    `POLICY_EXPRESSION`          VARCHAR(255) COMMENT '策略表达式',
    `CREATED_BY`                 VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`               DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY`                  VARCHAR(32)  COMMENT '更新人',
    `UPDATE_TIME`                DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业银行应用包交易路由策略表（BP_TENANT_BANK_APP_TXN_ROUTING_POLICY）';


-- 表说明：企业银行应用包交易路由规则表（BP_TENANT_BANK_APP_TXN_ROUTING_RULES）
CREATE TABLE IF NOT EXISTS `BP_TENANT_TXN_ROUTE_RULES`
(
    `ID`                         VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_TENANT_TXN_ROUTE_POLICY` VARCHAR(32) NOT NULL COMMENT '企业银行应用包交易路由策略表ID，来源：BP_TENANT_TXN_ROUTE_POLICY',
    `LEFT_SYMBOL`                VARCHAR(10) COMMENT '左括号',
    `RIGHT_SYMBOL`               VARCHAR(10) COMMENT '右括号',
    `FIELD_MARK`                 VARCHAR(32) NOT NULL COMMENT '字段',
    `FIELD_CONDITIONAL`          VARCHAR(10) NOT NULL COMMENT '条件',
    `FIELD_VALUE`                VARCHAR(64) NOT NULL COMMENT '规则值',
    `FIELD_LOGIC`                VARCHAR(16) COMMENT '逻辑',
    `RULE_INDEX`                 INT         COMMENT '规则序号',
    `CREATED_BY`                 VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`               DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY`                  VARCHAR(32) COMMENT '更新人',
    `UPDATE_TIME`                DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业银行应用包交易路由规则表（BP_TENANT_BANK_APP_TXN_ROUTING_RULES）';


-- 表说明：企业应用包运维管理
CREATE TABLE IF NOT EXISTS `BP_TENANT_OPERATION`
(
    `ID`                             VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `TENANT_CODE`                    VARCHAR(32)  NOT NULL COMMENT '企业客户号，来源：sys_tenant_manager',
    `BIF_CODE`                       VARCHAR(8)   NOT NULL COMMENT '接口代码',
    `DEPLOY_NODE`                    VARCHAR(32)  NOT NULL COMMENT '部署节点',
    `SYS_MODE`                       VARCHAR(8)   NOT NULL COMMENT '系统业务模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `ID_TENANT_BANK_APP_MANAGER_OLD` VARCHAR(100) COMMENT '版本切换任务，记录升级前企业应用包管理表ID，来源bp_tenant_bank_app_manager',
    `ID_TENANT_BANK_APP_MANAGER`     VARCHAR(32)  NOT NULL COMMENT '企业应用包管理表ID，来源于BP_TENANT_BANK_APP_MANAGER',
    `OPERATION_TYPE`                 INT          NOT NULL COMMENT '类型：0-应用包配置；1-参数配置；2-版本切换；3-重启；4-启用；5-禁用；6-撤销；7-首次部署',
    `OPERATION_STATUS`               INT          NOT NULL COMMENT '状态：-1-未发布，0-已发布，1-下载中，2-下载失败，3-下载成功，4-校验成功，5-校验失败，6-待执行，7-执行中，8-执行成功，9-执行失败，10-撤销',
    `PLAN_TIME`                      DATETIME     COMMENT '操作时间',
    `FINISH_TIME`                    DATETIME     COMMENT '完成时间',
    `ERROR_LOG_ATACHMENT_ID`         VARCHAR(64)  COMMENT '错误日志附件ID',
    `REVOKE_OPERATION_ID`            VARCHAR(32)  COMMENT '撤销运维记录id',
    `REVOKE_FLAG`                    VARCHAR(10)  COMMENT '撤销标识:1-已撤销，空未撤销',
    `CREATED_BY`                     VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`                   DATETIME     COMMENT '创建时间',
    `UPDATED_BY`                     VARCHAR(32)  COMMENT '更新人',
    `UPDATED_TIME`                   DATETIME     COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业应用包运维管理';


-- 表说明：企业应用包运维日志表
CREATE TABLE IF NOT EXISTS `BP_TENANT_OPERATION_LOG`
(
    `ID`                  VARCHAR(32) NOT NULL COMMENT '主键ID',
    `ID_TENANT_OPERATION` VARCHAR(32) NOT NULL COMMENT '企业应用包运维管理ID，来源：BP_TENANT_BANK_APP_OPERATION',
    `OPERATION_STATUS`    INT         NOT NULL COMMENT '状态：0-已发布，1-下载中，2-下载失败，3-下载成功，4-校验成功，5-校验失败，6-待执行，7-执行中，8-执行成功，9-执行失败',
    `ERROR_REASON`        TEXT        COMMENT '错误描述',
    `CREATED_BY`          VARCHAR(32) COMMENT '创建人',
    `CREATED_TIME`        DATETIME    COMMENT '创建时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业应用包运维日志表';


-- 表说明：企业应用监控管理
CREATE TABLE IF NOT EXISTS `BP_TENANT_BANK_APP_MONITOR`
(
    `ID`                         VARCHAR(32)  NOT NULL COMMENT '主键ID',
    `TENANT_CODE`                VARCHAR(32)  NOT NULL COMMENT '企业客户号，来源：sys_tenant_manager',
    `BIF_CODE`                   VARCHAR(8)   NOT NULL COMMENT '接口代码',
    `DEPLOY_NODE`                VARCHAR(32)  NOT NULL COMMENT '部署节点',
    `SYS_MODE`                   VARCHAR(8)   NOT NULL COMMENT '系统业务模块：settle-结算，ebill-回单，bill-票据，foreign-外汇',
    `ID_TENANT_BANK_APP_MANAGER` VARCHAR(32)  NOT NULL COMMENT '企业应用包管理表ID，来源于BP_TENANT_BANK_APP_MANAGER',
    `HEARTBEAT_STATUS`           INT          NOT NULL COMMENT '心跳状态：0-离线，1-在线，2-延迟',
    `HEARTBEAT_TIME`             DATETIME     NOT NULL COMMENT '心跳时间',
    `NODE_ADDRESS`               VARCHAR(200) NOT NULL COMMENT '节点地址',
    `CREATED_BY`                 VARCHAR(32)  COMMENT '创建人',
    `CREATED_TIME`               DATETIME     COMMENT '创建时间',
    `UPDATED_BY`                 VARCHAR(32)  COMMENT '更新人',
    `UPDATED_TIME`               DATETIME     COMMENT '更新时间',
    PRIMARY KEY (`ID`)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '企业应用监控管理';

CREATE INDEX ID_TENANT_MANAGER_IDX USING BTREE ON bp_tenant_operation (ID_TENANT_BANK_APP_MANAGER);


CREATE INDEX ID_TENANT_BANK_APP_MANAGER_IDX USING BTREE ON bp_tenant_version_manager (ID_TENANT_BANK_APP_MANAGER);


CREATE INDEX TNT_BIF_DP_ND_SYS_MD_IDX USING BTREE ON bp_tenant_version_manager (TENANT_CODE,BIF_CODE,DEPLOY_NODE,SYS_MODE);


CREATE INDEX TNT_BIF_DP_ND_SYS_MD_IDX USING BTREE ON bp_tenant_bank_app_monitor (TENANT_CODE,BIF_CODE,DEPLOY_NODE,SYS_MODE);
