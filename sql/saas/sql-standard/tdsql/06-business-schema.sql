
-- 表说明：FEX_COTP_BANKACC_CUR
CREATE TABLE IF NOT EXISTS `FEX_COTP_BANKACC_CUR`
(
    `ID`          VARCHAR(32) NOT NULL COMMENT '主键',
    `BANK_ACC_ID` VARCHAR(32) NOT NULL COMMENT '交易对手账号ID',
    `CUR_ID`      VARCHAR(32) NOT NULL COMMENT '币别ID',
    `TENANT_ID`   VARCHAR(32) NULL DEFAULT NULL COMMENT '租户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = 'FEX_COTP_BANKACC_CUR';


-- 表说明：FexProfitLossManage
CREATE TABLE IF NOT EXISTS `FEX_PROFIT_LOSS_MANAGE`
(
    `ID`                           VARCHAR(32)   NOT NULL COMMENT '主键',
    `TRAN_TYPE`                    VARCHAR(1)    NULL DEFAULT NULL COMMENT '交易类型',
    `NET_REAL_MONEY_TOTAL`         DECIMAL(19,2) NULL DEFAULT NULL COMMENT '已实现净损益合计',
    `NET_CONVERT_REAL_MONEY_TOTAL` DECIMAL(19,2) NULL DEFAULT NULL COMMENT '折本位币已实现净损益合计',
    `FLOAT_MONEY_TOTAL`            DECIMAL(19,2) NULL DEFAULT NULL COMMENT '浮动净损益',
    `CONVERT_FLOAT_MONEY_TOTAL`    DECIMAL(19,2) NULL DEFAULT NULL COMMENT '折本位币浮动净损益',
    `QUOTATION_CUR_ID`             VARCHAR(255)  NULL DEFAULT NULL COMMENT '报价方式标准币种',
    `QUOTATION_REFER_CUR_ID`       VARCHAR(255)  NULL DEFAULT NULL COMMENT '报价方式标准币种',
    `CONVERT_CUR_ID`               VARCHAR(32)   NULL DEFAULT NULL COMMENT '折本位币标准币种',
    `CONVERT_CUR_REFER_ID`         VARCHAR(255)  NULL DEFAULT NULL COMMENT '折本位币报价币种',
    `QUOTATION_METHOD`             VARCHAR(255)  NULL DEFAULT NULL COMMENT '报价方式',
    `CONVERT_CUR_EN_CODE`          VARCHAR(255)  NULL DEFAULT NULL COMMENT '折本位币报价方式',
    `CONVERT_DATE`                 DATETIME      NULL DEFAULT NULL COMMENT '折本位币报价时间',
    `REFERENCE_INTEREST_RATE`      DECIMAL(20,6) NULL DEFAULT NULL COMMENT '参考利率',
    `REQ_ID`                       VARCHAR(32)   NULL DEFAULT NULL COMMENT '交易表id',
    `CUR_ID`                       VARCHAR(32)   NULL DEFAULT NULL COMMENT '损益币种id',
    `CORP_ID`                      VARCHAR(32)   NULL DEFAULT NULL COMMENT '单位id',
    `LIFE_CYCLE`                   VARCHAR(255)  NULL DEFAULT NULL COMMENT '生命周期id',
    `QUOTATION_DATE`               DATETIME      NULL DEFAULT NULL COMMENT '报价日期',
    `SETTLE_DATE`                  DATETIME      NULL DEFAULT NULL COMMENT '结算日期',
    `CONTRACT_RATE`                DECIMAL(14,6) NULL DEFAULT NULL COMMENT '合同汇率',
    `VALUATION_RATE`               DECIMAL(20,6) NULL DEFAULT NULL COMMENT '估值利率',
    `CONVERT_RATE`                 DECIMAL(20,8) NULL DEFAULT NULL COMMENT '折本位币汇率',
    `FLOAT_MONEY`                  DECIMAL(19,2) NULL DEFAULT NULL COMMENT '浮动损益',
    `CONVERT_FLOAT_MONEY`          DECIMAL(19,2) NULL DEFAULT NULL COMMENT '折本位币浮动损益',
    `REAL_MONEY_TOTAL`             DECIMAL(19,2) NULL DEFAULT NULL COMMENT '已实现损益',
    `CONVERT_REAL_MONEY_TOTAL`     DECIMAL(19,2) NULL DEFAULT NULL COMMENT '折本位币已实现损益',
    `STATUS`                       DECIMAL(10,0) NULL DEFAULT NULL COMMENT '状态',
    `CREATE_BY`                    VARCHAR(32)   NULL DEFAULT NULL COMMENT '创建人',
    `CREATE_DATE`                  DATETIME      NULL DEFAULT NULL COMMENT '创建日期',
    `UPDATE_BY`                    VARCHAR(32)   NULL DEFAULT NULL COMMENT '修改人',
    `UPDATE_DATE`                  DATETIME      NULL DEFAULT NULL COMMENT '创建日期',
    `TENANT_ID`                    VARCHAR(32)   NULL DEFAULT NULL COMMENT '租户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = 'FexProfitLossManage';


-- 表说明：FexProfitLossManageDtl
CREATE TABLE IF NOT EXISTS `FEX_PROFIT_LOSS_MANAGE_DTL`
(
    `ID`                 VARCHAR(32)   NOT NULL COMMENT '主键',
    `LIFE_CYCLE_BILL_NO` VARCHAR(255)  NULL DEFAULT NULL COMMENT '生命周期id',
    `REQ_ID`             VARCHAR(32)   NULL DEFAULT NULL COMMENT '交易表id',
    `FEX_ID`             VARCHAR(255)  NULL DEFAULT NULL COMMENT '损益id',
    `LIFE_CYCLE_ID`      VARCHAR(255)  NULL DEFAULT NULL COMMENT '生命周期id',
    `OP_TYPE`            VARCHAR(32)   DEFAULT NULL COMMENT '操作类型',
    `ORDER_NO`           VARCHAR(32)   NULL DEFAULT NULL COMMENT '序号',
    `DELIVERLY_DATE`     DATETIME      NULL DEFAULT NULL COMMENT '交割日期、操作日',
    `REFER_RATE`         DECIMAL(12,8) NULL DEFAULT NULL COMMENT '参考汇率',
    `CONTRACT_RATE`      DECIMAL(14,6) NULL DEFAULT NULL COMMENT '合同汇率',
    `MONEY`              DECIMAL(15,2) NULL DEFAULT NULL COMMENT '浮动损益（已实现损益）',
    `CONVERT_MONEY`      DECIMAL(19,2) NULL DEFAULT NULL COMMENT '折本位币浮动损益（已实现损益）',
    `CREATE_BY`          VARCHAR(32)   NULL DEFAULT NULL COMMENT '创建人',
    `CREATE_DATE`        DATETIME      NULL DEFAULT NULL COMMENT '浮动损益',
    `UPDATE_BY`          VARCHAR(32)   NULL DEFAULT NULL COMMENT '折本位币浮动损益',
    `UPDATE_DATE`        DATETIME      NULL DEFAULT NULL COMMENT '已实现损益',
    `TENANT_ID`          VARCHAR(32)   NULL DEFAULT NULL COMMENT '租户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = 'FexProfitLossManageDtl';


-- 表说明：FexTranConfigOperationLog
CREATE TABLE IF NOT EXISTS `fex_tran_config_operation_log`
(
    `ID`                VARCHAR(32)  NOT NULL COMMENT '主键',
    `create_date`       DATETIME     NULL DEFAULT NULL COMMENT '创建时间',
    `operation_page`    VARCHAR(255) NULL DEFAULT NULL COMMENT '操作页面 0:交易页面配置 1:交易指令配置',
    `operation_content` VARCHAR(255) NULL DEFAULT NULL COMMENT '操作内容',
    `before_data`       LONGTEXT     NULL DEFAULT NULL COMMENT '修改前数据',
    `after_data`        LONGTEXT     NULL DEFAULT NULL COMMENT '修改后数据',
    `create_by`         VARCHAR(32)  NULL DEFAULT NULL COMMENT '创建人ID',
    `create_name`       VARCHAR(50)  NULL DEFAULT NULL COMMENT '创建人名称',
    `is_delete`         CHAR(1)      NULL DEFAULT NULL COMMENT '是否删除 0否 1是',
    `TENANT_ID`         VARCHAR(32)  NULL DEFAULT NULL COMMENT '租户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = 'FexTranConfigOperationLog';


-- 表说明：生命周期表
CREATE TABLE IF NOT EXISTS `FEX_TRAN_LIFE_CYCLE`
(
    `ID`                    VARCHAR(32)   NOT NULL COMMENT '主键',
    `REQ_ID`                VARCHAR(32)   NULL DEFAULT NULL COMMENT '交易信息ID',
    `CORP_ID`               VARCHAR(32)   NULL DEFAULT NULL COMMENT '单位',
    `BILL_NO`               VARCHAR(32)   NULL DEFAULT NULL COMMENT '编号',
    `OP_TYPE`               VARCHAR(32)   DEFAULT NULL COMMENT '操作类型',
    `OP_DATE`               DATETIME      NULL DEFAULT NULL COMMENT '操作日期',
    `EXPIRES_DATE`          DATETIME      NULL DEFAULT NULL COMMENT '到期日期',
    `SETTLE_DATE`           DATETIME      NULL DEFAULT NULL COMMENT '结算日期',
    `CONTRACT_RATE`         DECIMAL(14,6) NULL DEFAULT NULL COMMENT '合同汇率',
    `SPOT_RATE`             DECIMAL(14,6) NULL DEFAULT NULL COMMENT '即期汇率',
    `PROCESS_RATE`          DECIMAL(19,8) NULL DEFAULT NULL COMMENT '执行汇率',
    `TRAN_SETTLE_METHOD`    VARCHAR(1)    NULL DEFAULT NULL COMMENT '结算方式（更名为交割方式：差额、全额）',
    `IS_PAYMENT_AMOUNT`     VARCHAR(10)   NULL DEFAULT NULL COMMENT '金额是否自动计算',
    `BUY_CUR_ID`            VARCHAR(32)   NULL DEFAULT NULL COMMENT '买入币种',
    `SELL_CUR_ID`           VARCHAR(32)   NULL DEFAULT NULL COMMENT '卖出币种',
    `OP_MONEY`              DECIMAL(15,2) NULL DEFAULT NULL COMMENT '操作金额',
    `BUY_MONEY`             DECIMAL(15,2) NULL DEFAULT NULL COMMENT '买入金额',
    `SELL_MONEY`            DECIMAL(15,2) NULL DEFAULT NULL COMMENT '卖出金额',
    `BUY_BAL`               DECIMAL(19,6) NULL DEFAULT NULL COMMENT '买入余额',
    `SELL_BAL`              DECIMAL(19,6) NULL DEFAULT NULL COMMENT '卖出余额',
    `MONEY`                 DECIMAL(19,6) NULL DEFAULT NULL COMMENT '损益、结算金额',
    `PROFIT_LOSS_CUR`       VARCHAR(32)   NULL DEFAULT NULL COMMENT '损益币种',
    `PROFIT_LOSS_MONEY`     DECIMAL(19,6) NULL DEFAULT NULL COMMENT '净损益',
    `RECORD_RATE`           DECIMAL(19,8) NULL DEFAULT NULL COMMENT '操作日记账汇率',
    `BANK_ACC`              VARCHAR(50)   NULL DEFAULT NULL COMMENT '结算账号',
    `CUR_ID`                VARCHAR(32)   NULL DEFAULT NULL COMMENT '结算币种',
    `EARLY_RATE`            DECIMAL(19,8) NULL DEFAULT NULL COMMENT '提前汇率',
    `EARLY_TYPE`            VARCHAR(10)   NULL DEFAULT NULL COMMENT '提前方式',
    `AVG_RATE`              DECIMAL(14,6) NULL DEFAULT NULL COMMENT '平均汇率',
    `DEFERRED_RATE`         DECIMAL(8,6)  NULL DEFAULT NULL COMMENT '展期汇率',
    `DEFERRED_TYPE`         VARCHAR(10)   NULL DEFAULT NULL COMMENT '展期方式',
    `CLOSE_SETTLE_DATE`     DATETIME      NULL DEFAULT NULL COMMENT '平仓结算日期',
    `CLOSE_RATE`            DECIMAL(19,8) NULL DEFAULT NULL COMMENT '平仓汇率',
    `CLOSE_FORWARD_RATE`    DECIMAL(19,8) NULL DEFAULT NULL COMMENT '平仓远期汇率',
    `CONVERT_CUR_TYPE`      VARCHAR(10)   NULL DEFAULT NULL COMMENT '折本位币报价方式',
    `CONVERT_RATE`          DECIMAL(20,8) NULL DEFAULT NULL COMMENT '折本位币汇率',
    `CONVERT_LOSS_MONEY`    DECIMAL(19,6) NULL DEFAULT NULL COMMENT '折本位币净损益',
    `PAYEE_ACC`             VARCHAR(50)   NULL DEFAULT NULL COMMENT '收款账号',
    `PAYER_ACC`             VARCHAR(50)   NULL DEFAULT NULL COMMENT '付款账号',
    `EARLY_SETTLEMENT_TYPE` VARCHAR(10)   NULL DEFAULT NULL COMMENT '提前交割方式',
    `EARLY_SETTLEMENT_RATE` DECIMAL(19,8) NULL DEFAULT NULL COMMENT '提前交割汇率',
    `CLOSE_LOSS_MONEY`      DECIMAL(19,6) NULL DEFAULT NULL COMMENT '平仓损益',
    `DELIVERY_LOSS_MONEY`   DECIMAL(19,6) NULL DEFAULT NULL COMMENT '交割损益',
    `ALL_LOSS_MONEY`        DECIMAL(19,6) NULL DEFAULT NULL COMMENT '总损益',
    `TRAN_ADDR_ID`          VARCHAR(64)   NULL DEFAULT NULL COMMENT '交易地点ID',
    `TRAN_ADDR`             VARCHAR(100)  NULL DEFAULT NULL COMMENT '交易地点',
    `RMK`                   VARCHAR(200)  NULL DEFAULT NULL COMMENT '备注',
    `CREATE_BY`             VARCHAR(32)   NULL DEFAULT NULL COMMENT '创建人',
    `CREATE_DATE`           DATETIME      NULL DEFAULT NULL COMMENT '创建日期',
    `UPDATE_BY`             VARCHAR(32)   NULL DEFAULT NULL COMMENT '修改人',
    `UPDATE_DATE`           DATETIME      NULL DEFAULT NULL COMMENT '修改日期',
    `LIFE_STATUS`           VARCHAR(50)   NULL DEFAULT NULL COMMENT '状态',
    `REAL_BUY_MONEY`        DECIMAL(19,6) NULL DEFAULT NULL,
    `REAL_SELL_MONEY`       DECIMAL(19,6) NULL DEFAULT NULL,
    `POSITION_MONEY`        DECIMAL(19,2) NULL DEFAULT NULL COMMENT '持仓金额',
    `TENANT_ID`             VARCHAR(32)   NULL DEFAULT NULL COMMENT '租户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '生命周期表';


-- 表说明：外汇交易模板配置版本表
CREATE TABLE IF NOT EXISTS `fex_tran_req_config_version`
(
    `ID`          VARCHAR(32)  NOT NULL COMMENT '主键',
    `template_id` VARCHAR(32)  NULL DEFAULT NULL COMMENT '模板ID',
    `version_no`  VARCHAR(255) NULL DEFAULT NULL COMMENT '版本号',
    `create_by`   VARCHAR(32)  NULL DEFAULT NULL COMMENT '创建人ID',
    `create_date` DATETIME     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   VARCHAR(32)  NULL DEFAULT NULL COMMENT '修改人ID',
    `update_date` DATETIME     NULL DEFAULT NULL COMMENT '修改时间',
    `TENANT_ID`   VARCHAR(32)  NULL DEFAULT NULL COMMENT '租户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '外汇交易模板配置版本表';


-- 表说明：交易管理扩展表
CREATE TABLE IF NOT EXISTS `FEX_TRAN_REQ_EXT`
(
    `ID`                     VARCHAR(32)   NOT NULL COMMENT '主键',
    `REQ_ID`                 VARCHAR(32)   NULL DEFAULT NULL COMMENT '交易id',
    `TRAN_CUR_PAIR_ID`       VARCHAR(32)   NULL DEFAULT NULL COMMENT '交易货币对id',
    `CONVERT_CUR_ID`         VARCHAR(32)   NULL DEFAULT NULL COMMENT '折本位币标准币种',
    `CONVERT_CUR_REFER_ID`   VARCHAR(32)   NULL DEFAULT NULL COMMENT '折本位币报价币种',
    `IS_PAYMENT_AMOUNT`      VARCHAR(10)   NULL DEFAULT NULL COMMENT '金额是否自动计算',
    `TRAN_RATE_TYPE`         VARCHAR(10)   NULL DEFAULT NULL COMMENT '交易汇率类型',
    `QUOTATION_METHOD`       VARCHAR(100)  NULL DEFAULT NULL COMMENT '报价方式',
    `QUOTATION_CUR_ID`       VARCHAR(32)   NULL DEFAULT NULL COMMENT '标准币种',
    `QUOTATION_REFER_CUR_ID` VARCHAR(32)   NULL DEFAULT NULL COMMENT '报价币种',
    `POSITION_MONEY`         DECIMAL(19,2) NULL DEFAULT NULL COMMENT '持仓金额',
    `PROFIT_LOSS_CUR`        VARCHAR(32)   NULL DEFAULT NULL COMMENT '损益币种',
    `PROFIT_LOSS_MONEY`      DECIMAL(19,2) NULL DEFAULT NULL COMMENT '损益',
    `CONVERT_CUR_TYPE`       VARCHAR(10)   NULL DEFAULT NULL COMMENT '折本位币报价方式',
    `CONVERT_RATE`           DECIMAL(20,8) NULL DEFAULT NULL COMMENT '折本位币汇率',
    `CONVERT_MONEY`          DECIMAL(19,2) NULL DEFAULT NULL COMMENT '折本位币损益、期权费金额',
    `TRAN_CUR_PAIR_BASE_ID`  VARCHAR(32)   NULL DEFAULT NULL COMMENT '交易货币对标准币种',
    `TRAN_CUR_PAIR_REFER_ID` VARCHAR(32)   NULL DEFAULT NULL COMMENT '交易货币对报价币种',
    `LIFE_CYCLE_STATE`       VARCHAR(20)   NULL DEFAULT NULL COMMENT '生命周期状态',
    `TENANT_ID`              VARCHAR(32)   NULL DEFAULT NULL COMMENT '租户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '交易管理扩展表';


-- 表说明：银行类型自定义配置项
CREATE TABLE IF NOT EXISTS `bt_bank_type_custom`
(
    `ID`            VARCHAR(32)  NOT NULL COMMENT '主键',
    `BANK_TYPE_ID`  VARCHAR(32)  NULL DEFAULT NULL COMMENT '银行类型ID',
    `BIF_CODE`      VARCHAR(32)  NULL DEFAULT NULL COMMENT '接口代码ID',
    `COUNTRY_ID`    VARCHAR(255) NULL DEFAULT NULL COMMENT '适用国家',
    `BANK_PAY_TYPE` VARCHAR(255) NULL DEFAULT NULL COMMENT '支付类型',
    `CUSTOM_CODE`   VARCHAR(255) NULL DEFAULT NULL COMMENT '自定义上送代码',
    `CUSTOM_NAME`   VARCHAR(255) NULL DEFAULT NULL COMMENT '自定义名称',
    `CUSTOM_TYPE`   VARCHAR(50)  NULL DEFAULT NULL COMMENT '自定义类型',
    `IS_DELETED`    VARCHAR(1)   NULL DEFAULT '0' COMMENT '是否删除',
    `TENANT_ID`     VARCHAR(32)  DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '银行类型自定义配置项';


-- 表说明：交易请求字段配置表
CREATE TABLE IF NOT EXISTS `fex_tran_req_field`
(
    `ID`                VARCHAR(32)   NOT NULL COMMENT '主键',
    `field_sort`        INT           NULL DEFAULT NULL,
    `field_code`        VARCHAR(30)   NULL DEFAULT NULL COMMENT '字段编码',
    `field_name`        VARCHAR(100)  DEFAULT NULL COMMENT '操作类型',
    `field_type`        VARCHAR(10)   NULL DEFAULT NULL COMMENT '字段类型',
    `data_type`         VARCHAR(20)   NULL DEFAULT NULL COMMENT '数据类型',
    `data_group`        VARCHAR(255)  NULL DEFAULT NULL COMMENT '字典组',
    `data_class`        VARCHAR(255)  NULL DEFAULT NULL COMMENT '数据分类',
    `data_url`          VARCHAR(255)  NULL DEFAULT NULL COMMENT '数据URL',
    `data_callback`     VARCHAR(255)  NULL DEFAULT NULL COMMENT '数据回调',
    `FIELD_AREA`        VARCHAR(255)  NULL DEFAULT NULL COMMENT '数据区域',
    `FIELD_PARENT_CODE` VARCHAR(255)  NULL DEFAULT NULL,
    `FIELD_CONTENT`     VARCHAR(4000) NULL DEFAULT NULL COMMENT '字段内容',
    `valid_sign`        VARCHAR(1)    NULL DEFAULT NULL COMMENT '有效标识',
    `is_sys`            VARCHAR(1)    NULL DEFAULT NULL COMMENT '是否系统预设',
    `data_beforeinit`   VARCHAR(255)  NULL DEFAULT NULL COMMENT '初始化前事件',
    `data_valuefield`   VARCHAR(255)  NULL DEFAULT NULL COMMENT '值字段',
    `data_textfield`    VARCHAR(255)  NULL DEFAULT NULL COMMENT '文本字段',
    `TENANT_ID`         VARCHAR(32)   NULL DEFAULT NULL COMMENT '租户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '交易请求字段配置表';


-- 表说明：FexTranTemplateConfig
CREATE TABLE IF NOT EXISTS `fex_tran_template_config`
(
    `ID`            VARCHAR(32)  NOT NULL COMMENT '主键',
    `parent_id`     VARCHAR(32)  NULL DEFAULT NULL COMMENT '外键.评分指标表（Scoring_Indicator）的主键',
    `template_no`   VARCHAR(255) NULL DEFAULT NULL COMMENT '模板编号',
    `template_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '模板名称',
    `template_desc` VARCHAR(255) NULL DEFAULT NULL COMMENT '模板描述',
    `bank_type_id`  VARCHAR(32)  NULL DEFAULT NULL COMMENT '银行类型ID',
    `bif_id`        VARCHAR(32)  NULL DEFAULT NULL COMMENT '银行接口ID',
    `country_id`    VARCHAR(65)  NULL DEFAULT NULL COMMENT '国家ID',
    `pay_type_id`   VARCHAR(255) NULL DEFAULT NULL COMMENT '支付类型',
    `remark`        VARCHAR(200) NULL DEFAULT NULL COMMENT '客户摘要',
    `valid_sign`    VARCHAR(1)   NULL DEFAULT NULL COMMENT '有效标志 1-有效 0-无效',
    `is_delete`     CHAR(1)      NULL DEFAULT NULL COMMENT '是否删除 0否 1是',
    `create_by`     VARCHAR(32)  NULL DEFAULT NULL COMMENT '创建人ID',
    `create_date`   DATETIME     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     VARCHAR(32)  NULL DEFAULT NULL COMMENT '修改人ID',
    `update_date`   DATETIME     NULL DEFAULT NULL COMMENT '修改时间',
    `sort`          INT          NULL DEFAULT NULL COMMENT '排序',
    `TENANT_ID`     VARCHAR(32)  NULL DEFAULT NULL COMMENT '租户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = 'FexTranTemplateConfig';


-- 表说明：外汇交易页面字段配置表
CREATE TABLE IF NOT EXISTS `fex_tran_req_config`
(
    `id`                         VARCHAR(32)   NOT NULL,
    `template_config_version_id` VARCHAR(32)   NOT NULL COMMENT '模板配置版本ID',
    `template_id`                VARCHAR(32)   NOT NULL COMMENT '模板ID',
    `version_no`                 VARCHAR(32)   NOT NULL COMMENT '版本号',
    `field_id`                   VARCHAR(32)   NULL DEFAULT NULL COMMENT '自定义字段ID',
    `field_code`                 VARCHAR(50)   NULL DEFAULT NULL COMMENT '字段编码',
    `field_name`                 VARCHAR(255)  NULL DEFAULT NULL COMMENT '字段名称',
    `field_sort`                 INT(11)       NULL DEFAULT NULL COMMENT '排序',
    `field_length`               INT(11)       NULL DEFAULT '0' COMMENT '长度限制',
    `field_limit_val`            VARCHAR(255)  NULL DEFAULT NULL COMMENT '限制值',
    `field_def_val`              VARCHAR(255)  NULL DEFAULT NULL COMMENT '默认值',
    `field_dict_group`           VARCHAR(255)  NULL DEFAULT NULL COMMENT '枚举组',
    `field_limited_dict`         VARCHAR(255)  NULL DEFAULT NULL COMMENT '限制枚举值',
    `field_area`                 VARCHAR(50)   NULL DEFAULT NULL COMMENT '字段所属区域',
    `field_parent_code`          VARCHAR(50)   NULL DEFAULT NULL COMMENT '父级编码',
    `field_content`              VARCHAR(4000) NULL DEFAULT NULL,
    `data_area_layout`           VARCHAR(50)   NULL DEFAULT NULL COMMENT '字段所属区域布局',
    `data_type`                  VARCHAR(50)   NULL DEFAULT NULL COMMENT '字段类型',
    `data_class`                 VARCHAR(50)   NULL DEFAULT NULL COMMENT '字段类',
    `is_sys`                     TINYINT(4)    NULL DEFAULT '0' COMMENT '是否预设字段（1-预设字段;2-自定义字段）',
    `is_use`                     TINYINT(4)    NULL DEFAULT '0' COMMENT '是否启用',
    `is_visible`                 TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '是否可见',
    `is_required`                TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '是否必填',
    `is_length`                  TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '是否控制长度',
    `is_limited_dict`            TINYINT(4)    NULL DEFAULT '0' COMMENT '是否限制枚举值',
    `is_exc`                     TINYINT(4)    NULL DEFAULT '0' COMMENT '是否发送银行指令',
    `is_deleted`                 TINYINT(4)    NULL DEFAULT '0',
    `status`                     VARCHAR(10)   NULL DEFAULT NULL COMMENT '状态',
    `CREATE_BY`                  VARCHAR(32)   NULL DEFAULT NULL,
    `CREATE_DATE`                DATETIME      NULL DEFAULT NULL,
    `UPDATE_BY`                  VARCHAR(32)   NULL DEFAULT NULL,
    `UPDATE_DATE`                DATETIME      NULL DEFAULT NULL,
    `TENANT_ID`                  VARCHAR(32)   DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '外汇交易页面字段配置表';


-- 表说明：客商快捷支付信息表
CREATE TABLE IF NOT EXISTS `sys_external_corp_quick_pay`
(
    `ID`               varchar(32) NOT NULL COMMENT '主键',
    `EXTERNAL_CORP_ID` varchar(32) NOT NULL COMMENT '客商ID',
    `PRO_ID`           VARCHAR(65) DEFAULT NULL COMMENT '资金收付产品ID',
    `COUNTRY_CODE`     varchar(50) DEFAULT NULL COMMENT '国家代码',
    `EMAIL`            varchar(40) DEFAULT NULL COMMENT '邮箱',
    `TEL`              varchar(50) DEFAULT NULL COMMENT '电话号码',
    `FPS_ID`           varchar(50) DEFAULT NULL COMMENT 'FPS ID',
    `CREDIT_CARD`      varchar(50) DEFAULT NULL COMMENT '信用卡号',
    `HK_ID`            varchar(50) DEFAULT NULL COMMENT '香港身份证ID',
    `IS_DEFAULT_ACC`   char(1)     DEFAULT NULL COMMENT '首选账号',
    `CREATE_DATE`      datetime    DEFAULT NULL COMMENT '创建日期',
    `CREATE_BY`        varchar(50) DEFAULT NULL COMMENT '创建人',
    `UPDATE_DATE`      datetime    DEFAULT NULL COMMENT '更新日期',
    `UPDATE_BY`        varchar(50) DEFAULT NULL COMMENT '创建人',
    `TENANT_ID`        VARCHAR(32) DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '客商快捷支付信息表';
COMMIT;


-- 表说明：添加银行自定义其他信息配置表
CREATE TABLE IF NOT EXISTS `BT_BANK_TYPE_OTHER_CUSTOM`
(
    `ID`             VARCHAR(32)  not null,
    `BANK_TYPE_ID`   VARCHAR(32)  COMMENT '银行类型ID',
    `BIF_CODE`       VARCHAR(32)  COMMENT '接口代码ID',
    `APPLAY_SCOPE`   VARCHAR(30)  COMMENT '适用页面',
    `COUNTRY_ID`     VARCHAR(500) COMMENT '适用国家ID',
    `REMIT_TYPE`     VARCHAR(30)  COMMENT '汇款类型',
    `NEED_FIELD`     VARCHAR(100) COMMENT '所需字段',
    `REQUIRED_FIELD` VARCHAR(100) COMMENT '必填字段',
    `TENANT_ID`      VARCHAR(32)  DEFAULT NULL COMMENT '租户ID',
    constraint PK_BT_BANK_TYPE_OTHER_CUSTOM primary key(ID)
)
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  COMMENT = '添加银行自定义其他信息配置表';


-- 表说明：租户外汇产品预置表
CREATE TABLE IF NOT EXISTS `tenant_p_fex_product`
(
    `ID`                  VARCHAR(32)  NOT NULL COMMENT '主键',
    `PRO_LEVEL`           VARCHAR(1)   NOT NULL COMMENT '层级 0-产品组 1-产品',
    `TRAN_TYPE`           VARCHAR(1)   NOT NULL COMMENT '交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易',
    `PRO_NAME`            VARCHAR(60)  NOT NULL COMMENT '名称',
    `PRO_DESC`            VARCHAR(100) NULL DEFAULT NULL COMMENT '描述',
    `PRO_ID`              VARCHAR(65)  NULL DEFAULT NULL COMMENT '所属组ID',
    `FEX_BUS_TYPE`        VARCHAR(1)   NULL DEFAULT NULL COMMENT '外汇业务类型 1-结汇、2-售汇、3-外汇买卖',
    `FEX_TRAN_TYPE`       VARCHAR(1)   NULL DEFAULT NULL COMMENT '外汇交易类型 0-即期、1-远期',
    `DELIVERY_TYPE`       VARCHAR(1)   NULL DEFAULT NULL COMMENT '交割类型 0-本金交割、1-无本金交割',
    `DELIVERY_CUR_ID`     VARCHAR(32)  NULL DEFAULT NULL COMMENT '交割币种',
    `IS_ALLOWED_DELIVERY` VARCHAR(1)   NULL DEFAULT '0' COMMENT '是否允许多次交割 1-是 0-否',
    `IS_ALLOWED_SETTLE`   VARCHAR(1)   NULL DEFAULT '0' COMMENT '非工作日是否允许结算 1-是 0-否',
    `BUY_CUR_ID`          VARCHAR(32)  NULL DEFAULT NULL COMMENT '买入币种',
    `SELL_CUR_ID`         VARCHAR(32)  NULL DEFAULT NULL COMMENT '卖出币种',
    `NON_WORKDAY_RULE`    VARCHAR(30)  NULL DEFAULT NULL COMMENT '非工作日适用原则 1-币种、2-交易主体单位所在地、3-主体银行所在地、4-交易对手所在地',
    `TRANGROUP_RULE_ID`   VARCHAR(32)  NULL DEFAULT NULL COMMENT '交易组规则ID',
    `IS_ALLOWED_CHANGE`   VARCHAR(1)   NULL DEFAULT NULL COMMENT '交易组规则是否可以在交易组中更改 1-是 0-否',
    `VALID_SIGN`          VARCHAR(1)   NULL DEFAULT '1' COMMENT '有效标志 1-是 0-否',
    `RMK`                 VARCHAR(200) NULL DEFAULT NULL COMMENT '备注',
    `CREATE_DATE`         DATETIME     NULL DEFAULT NULL COMMENT '创建日期',
    `CREATE_BY`           VARCHAR(50)  NULL DEFAULT NULL COMMENT '创建人登录名称',
    `UPDATE_DATE`         DATETIME     NULL DEFAULT NULL COMMENT '更新日期',
    `UPDATE_BY`           VARCHAR(50)  NULL DEFAULT NULL COMMENT '更新人登录名称',
    `FEX_OPTION1`         VARCHAR(1)   NULL DEFAULT NULL COMMENT '外汇期权品种1',
    `FEX_OPTION2`         VARCHAR(1)   NULL DEFAULT NULL COMMENT '外汇期权品种2',
    `FEX_OPTION3`         VARCHAR(1)   NULL DEFAULT NULL COMMENT '外汇期权品种3',
    `FEX_OPTION_TYPE`     VARCHAR(1)   NULL DEFAULT NULL COMMENT '外汇期权类型',
    `FEX_OPTION_RATE`     VARCHAR(1)   NULL DEFAULT NULL COMMENT '期权费汇率',
    `REFUND_TIME`         VARCHAR(1)   NULL DEFAULT NULL COMMENT '退款时间点',
    `DAYS_RULE`           VARCHAR(1)   NULL DEFAULT NULL COMMENT '加权天数规则',
    `VALID_STANDARD`      VARCHAR(1)   NULL DEFAULT NULL COMMENT '交易时障碍验证标准',
    `TRAN_SETTLE_METHOD`  VARCHAR(1)   NULL DEFAULT NULL COMMENT '交易结算方式',
    `IS_SHOW_AGENT`       VARCHAR(1)   NULL DEFAULT NULL COMMENT '在交易时展示经纪人 0-否、1-是',
    `IS_SHOW_CLEAR_HOUSE` VARCHAR(1)   NULL DEFAULT NULL COMMENT '在交易时展示清算所 0-否、1-是',
    `IS_AUTO_DELIVERY`    VARCHAR(1)   NOT NULL DEFAULT '0' COMMENT '即期是否自动交割 1-是 0-否',
    `IS_ALLOWED_DEL`      VARCHAR(1)   NULL DEFAULT '1' COMMENT '是否可以删除 1-是 0-否',
    `IS_INIT`             CHAR(1)      NULL DEFAULT '1' COMMENT '是否初始化',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '租户外汇产品预置表';


-- 表说明：租户交易自定义信息表
CREATE TABLE IF NOT EXISTS `FEX_CUST_INFO_TENANT`
(
    `ID`        VARCHAR(32)   NOT NULL COMMENT '主键',
    `BUS_ID`    VARCHAR(32)   NULL DEFAULT NULL COMMENT '业务主表ID',
    `FIELD_ID`  VARCHAR(32)   NULL DEFAULT NULL COMMENT '自定义字段规则ID',
    `FIELD_VAL` VARCHAR(2000) NULL DEFAULT NULL COMMENT '字段值',
    `TENANT_ID` VARCHAR(32)   NULL DEFAULT NULL COMMENT '租户ID',
    `FIELD_NAME` VARCHAR(60)   DEFAULT NULL COMMENT '字段名称',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `IDX_FEX_CUST_INFO_TENANT` (`TENANT_ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '租户交易自定义信息表';


-- 表说明：租户自定义字段规则表
CREATE TABLE IF NOT EXISTS `FEX_CUST_FIELD_RULE_TENANT`
(
    `ID`            VARCHAR(32)   NOT NULL COMMENT '主键',
    `FIELD_CODE`    VARCHAR(100)  DEFAULT NULL COMMENT '字段编码',
    `FIELD_NAME`    VARCHAR(100)  DEFAULT NULL COMMENT '字段名称',
    `FIELD_DESC`    VARCHAR(100)  NULL DEFAULT NULL COMMENT '字段描述',
    `FIELD_TYPE`    VARCHAR(1)    NULL DEFAULT NULL COMMENT '字段类型',
    `APP_RANGE_1`   VARCHAR(1)    NULL DEFAULT NULL COMMENT '应用范围1',
    `APP_RANGE_2`   VARCHAR(32)   NULL DEFAULT NULL COMMENT '应用范围2',
    `CONTROL_RULE`  VARCHAR(30)   NULL DEFAULT NULL COMMENT '录入控制规则',
    `PRESET_VAL`    VARCHAR(500)  NULL DEFAULT NULL COMMENT '预设值',
    `DECIMAL_DIGIT` DECIMAL(10,0) NULL DEFAULT NULL COMMENT '小数位数',
    `MIN_VAL`       DECIMAL(15,6) NULL DEFAULT NULL COMMENT '最小值',
    `MAXT_VAL`      DECIMAL(15,6) NULL DEFAULT NULL COMMENT '最大值',
    `CUR_ID`        VARCHAR(32)   NULL DEFAULT NULL COMMENT '币种ID',
    `VALID_SIGN`    VARCHAR(1)    NULL DEFAULT '1' COMMENT '有效标志',
    `CREATE_DATE`   DATETIME      NULL DEFAULT NULL COMMENT '创建时间',
    `CREATE_BY`     VARCHAR(32)   NULL DEFAULT NULL COMMENT '创建人',
    `UPDATE_DATE`   DATETIME      NULL DEFAULT NULL COMMENT '更新时间',
    `UPDATE_BY`     VARCHAR(32)   NULL DEFAULT NULL COMMENT '更新人',
    `DICT_GROUP`    VARCHAR(200)  NULL DEFAULT NULL COMMENT '字典组代码',
    `LABEL_BANK`    VARCHAR(255)  NULL DEFAULT NULL COMMENT '银行标签',
    `TENANT_ID`     VARCHAR(32)   NULL DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `IDX_FEX_CUST_FIELD_RULE_T` (`TENANT_ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '租户自定义字段规则表';


-- 表说明：内管用户表格使用习惯记录
CREATE TABLE IF NOT EXISTS `SYS_TABLE_RECORD_MANAGER`
(
    `ID`        VARCHAR(32)  NOT NULL COMMENT '主键',
    `USER_ID`   VARCHAR(32)  NULL DEFAULT NULL COMMENT '用户ID',
    `TABLE_KEY` VARCHAR(255) NULL DEFAULT NULL COMMENT '表格的URL作为KEY',
    `CONTENT`   LONGTEXT     NULL COMMENT '用户习惯记录JSON结构',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '内管用户表格使用习惯记录';


-- 表说明：租户前台表格默认配置预置表
CREATE TABLE IF NOT EXISTS `TENANT_PRE_TABLE_RECORD`
(
    ID        VARCHAR(32)  NOT NULL COMMENT '主键',
    USER_ID   VARCHAR(32)  NULL COMMENT '用户ID',
    TABLE_KEY VARCHAR(255) NOT NULL COMMENT '表格的URL作为KEY',
    CONTENT   TEXT         NULL COMMENT '用户习惯记录JSON结构',
    IS_INIT   VARCHAR(1)   NOT NULL DEFAULT '1' COMMENT '是否需要给租户初始化',
    PRIMARY KEY (ID),
    UNIQUE KEY UK_TP_TABLE_RECORD_KEY (TABLE_KEY)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '租户前台表格默认配置预置表';


-- 表说明：应付债权凭证表
CREATE TABLE IF NOT EXISTS `scf_payable_voucher`
(
    `id`                 varchar(32)   NOT NULL COMMENT 'id',
    `voucher_no`         varchar(50)   DEFAULT NULL COMMENT '单据编号',
    `voucher_code`       varchar(100)  DEFAULT NULL COMMENT '应付债券凭证编号',
    `template_id`        varchar(50)   DEFAULT NULL COMMENT '债权凭证模版id',
    `version_id`         varchar(32)   DEFAULT NULL COMMENT '预留 可后续添加模板版本id',
    `platform_id`        varchar(32)   DEFAULT NULL COMMENT '平台',
    `product_id`         varchar(32)   DEFAULT NULL COMMENT '产品ID',
    `corp_id`            varchar(32)   DEFAULT NULL COMMENT '企业ID',
    `open_date`          datetime      DEFAULT NULL COMMENT '开立日期',
    `expire_date`        datetime      DEFAULT NULL COMMENT '到期日期',
    `core_corp`          varchar(32)   DEFAULT NULL COMMENT '核心企业ID',
    `core_corp_name`     varchar(100)  DEFAULT NULL COMMENT '核心企业名称',
    `cur_id`             varchar(32)   DEFAULT NULL COMMENT '币种ID',
    `voucher_money`      decimal(18,6) DEFAULT NULL COMMENT '凭证金额',
    `supply_id`          varchar(32)   DEFAULT NULL COMMENT '供应商ID',
    `supply_name`        varchar(200)  DEFAULT NULL COMMENT '供应商名称',
    `receive_account`    varchar(200)  DEFAULT NULL COMMENT '收款账户',
    `receive_bank`       varchar(200)  DEFAULT NULL COMMENT '收款银行',
    `online_sign`        varchar(1)    DEFAULT NULL COMMENT '线上签章',
    `quota_code`         varchar(100)  DEFAULT NULL COMMENT '额度编码',
    `quota_name`         varchar(200)  DEFAULT NULL COMMENT '额度名称',
    `quota_balance`      decimal(18,6) DEFAULT NULL COMMENT '额度余额',
    `contract_code`      varchar(100)  DEFAULT NULL COMMENT '合同编码',
    `contract_name`      varchar(200)  DEFAULT NULL COMMENT '合同名称',
    `contract_money`     decimal(18,6) DEFAULT NULL COMMENT '合同金额',
    `contract_date`      datetime      DEFAULT NULL COMMENT '合同日期',
    `contract_file`      varchar(32)   DEFAULT NULL COMMENT '合同文件',
    `invoice_file`       varchar(32)   DEFAULT NULL COMMENT '发票文件',
    `voucher_source`     varchar(50)   DEFAULT NULL COMMENT '单据来源(手工新增：MANUAL，ERP，供应链金融平台:SUPPLY_PLATFORM)',
    `interest_mode`      varchar(32)   DEFAULT '1' COMMENT '融资付息方式(1:卖方付息，2:买方付息，默认卖方付息)',
    `promise_pay_date`   datetime      DEFAULT NULL COMMENT '承诺付款日',
    `supply_credit_code` varchar(50)   DEFAULT NULL COMMENT '供应商统一社会信用代码',
    `approval_status`    varchar(10)   DEFAULT NULL COMMENT '审批状态',
    `voucher_status`     varchar(10)   DEFAULT NULL COMMENT '业务状态',
    `pay_id`             varchar(32)   DEFAULT NULL COMMENT '关联付款单ID(付款单审批通过后写入，选票时排除已关联凭证)',
    `pay_no`             varchar(64)   DEFAULT NULL COMMENT '关联付款单号(付款单审批通过后写入，选票时排除已关联凭证)',
    `cancel_reason`      varchar(500)  DEFAULT NULL COMMENT '作废原因',
    `cancel_date`        datetime      DEFAULT NULL COMMENT '作废时间',
    `create_by`          varchar(50)   DEFAULT NULL COMMENT '创建人',
    `create_date`        datetime      DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(50)   DEFAULT NULL COMMENT '修改人',
    `update_date`        datetime      DEFAULT NULL COMMENT '修改时间',
    `tenant_id`          varchar(32)   DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '应付债权凭证表';


-- 表说明：应付债权凭证数据表自定义字段数据
CREATE TABLE IF NOT EXISTS `scf_voucher_custom_field`
(
    id varchar(32) NOT NULL COMMENT 'id',
    voucher_id varchar(32) NOT NULL COMMENT '凭证id',
    field_id varchar(32) NOT NULL COMMENT '字段id',
    field_code varchar(100) NOT NULL COMMENT '自定义字段编码',
    field_name varchar(200) NOT NULL COMMENT '自定义字段名称',
    field_type varchar(50) NOT NULL COMMENT '自定义字段类型',
    field_value varchar(255) NOT NULL COMMENT '自定义字段值',
    create_by varchar(32) DEFAULT NULL COMMENT '创建人',
    create_date datetime DEFAULT NULL COMMENT '创建日期',
    update_by varchar(32) DEFAULT NULL COMMENT '更新人',
    update_date datetime DEFAULT NULL COMMENT '更新日期',
    tenant_id varchar(32) DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '应付债权凭证数据表自定义字段数据';


-- 表说明：凭证兑付记录表
CREATE TABLE IF NOT EXISTS `scf_voucher_redemption`
(
    `id`              varchar(32)   NOT NULL COMMENT 'id',
    `voucher_id`      varchar(32)   DEFAULT NULL COMMENT '应付凭证ID',
    `cash_no`         varchar(50)   DEFAULT NULL COMMENT '兑付单号',
    `platform_id`     varchar(32)   DEFAULT NULL COMMENT '平台id',
    `product_id`      varchar(32)   DEFAULT NULL COMMENT '融资产品',
    `corp_id`         varchar(32)   DEFAULT NULL COMMENT '开立单位',
    `open_date`       datetime      DEFAULT NULL COMMENT '开立日期',
    `expire_date`     datetime      DEFAULT NULL COMMENT '到期日',
    `core_corp`       varchar(32)   DEFAULT NULL COMMENT '核心企业',
    `cur_id`          varchar(32)   DEFAULT NULL COMMENT '币别',
    `voucher_money`   decimal(18,6) DEFAULT NULL COMMENT '凭证金额',
    `supply_id`       varchar(32)   DEFAULT NULL COMMENT '供应商',
    `supply_name`     varchar(200)  DEFAULT NULL COMMENT '供应商',
    `pay_account`     varchar(200)  DEFAULT NULL COMMENT '付款账号',
    `cash_money`      decimal(18,6) DEFAULT NULL COMMENT '兑付金额',
    `pay_date`        datetime      DEFAULT NULL COMMENT '付款日期',
    `receive_name`    varchar(200)  DEFAULT NULL COMMENT '收款方名称',
    `receive_account` varchar(200)  DEFAULT NULL COMMENT '收款方账号',
    `receive_bank`    varchar(200)  DEFAULT NULL COMMENT '收款开户行',
    `approval_status` varchar(10)   DEFAULT NULL COMMENT '审批状态',
    `tenant_id`       varchar(32)   DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '凭证兑付记录表';


-- 表说明：指令表
CREATE TABLE IF NOT EXISTS `SCF_INSTRUCTION`
(
    ID               VARCHAR(32)  NOT NULL COMMENT '主键ID',
    INSTR_CODE       VARCHAR(64)  COMMENT '指令编码',
    INSTR_TYPE       VARCHAR(32)  COMMENT '指令类型',
    INSTR_TYPE_NAME  VARCHAR(200)  COMMENT '指令类型名称',
    PLATFORM_ID      VARCHAR(32)  COMMENT '供应链平台ID',
    PLATFORM_NAME    VARCHAR(200)  COMMENT '供应链平台名称',
    PRODUCT_ID       VARCHAR(64)  COMMENT '融资产品ID',
    PRODUCT_NAME     VARCHAR(200)  COMMENT '融资产品名称',
    DIRECT_IF_ID     VARCHAR(32)  COMMENT '直联接口ID',
    VOUCHER_CODE     VARCHAR(64)  COMMENT '债权凭证编码',
    RELATE_NO        VARCHAR(64)  COMMENT '关联编号(转让记录编号/融资单号等)',
    CORP_ID          VARCHAR(32)  COMMENT '开立单位ID',
    CORP_NAME        VARCHAR(100)  COMMENT '开立单位名称',
    CUR_ID           VARCHAR(32)  COMMENT '币别ID',
    CUR_NAME         VARCHAR(32)  COMMENT '币别名称',
    INSTR_AMT        DECIMAL(16,6) COMMENT '指令金额',
    INSTR_DATE       DATETIME     COMMENT '指令日期',
    EXPIRE_DATE      DATETIME     COMMENT '到期日',
    TRANS_STATUS     VARCHAR(32)  COMMENT '交易状态',
    TRANS_REASON     VARCHAR(500) COMMENT '交易原因',
    SIGN             VARCHAR(1)   COMMENT '应付应收标识(0为应付，1为应收)',
    CREATE_BY        VARCHAR(32)  COMMENT '创建人',
    CREATE_DATE      DATETIME     COMMENT '创建时间',
    UPDATE_BY        VARCHAR(32)  COMMENT '修改人',
    UPDATE_DATE      DATETIME     COMMENT '修改时间',
    TENANT_ID        VARCHAR(32)  COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '指令表';


-- 表说明：平台接口开通配置表
CREATE TABLE IF NOT EXISTS `scf_ent_bank_open`
(
    id varchar(32) NOT NULL COMMENT '主键ID',
    type varchar(20) DEFAULT NULL COMMENT '接口类型',
    api_name_cn varchar(100) DEFAULT NULL COMMENT '接口名称（中文）',
    api_name varchar(100) DEFAULT NULL COMMENT '接口名称',
    has_direct varchar(1) DEFAULT NULL COMMENT '是否具备直连接口(0-否 1-是)',
    direct varchar(32) DEFAULT NULL COMMENT '直连接口',
    ent_platform_id varchar(32) DEFAULT NULL COMMENT '平台ID',
    tenant_id varchar(32) DEFAULT NULL COMMENT '租户ID',
    create_by varchar(32) DEFAULT NULL COMMENT '创建人',
    create_date datetime DEFAULT NULL COMMENT '创建时间',
    update_by varchar(32) DEFAULT NULL COMMENT '修改人',
    update_date datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '平台接口开通配置表';


-- 表说明：资金方信息表
CREATE TABLE IF NOT EXISTS `scf_funder_info`
(
    id varchar(32) NOT NULL COMMENT '主键ID',
    code varchar(50) DEFAULT NULL COMMENT '资金方编码',
    type varchar(10) DEFAULT NULL COMMENT '机构类型 01-保理公司 02-银行 03-其他',
    bank_no varchar(50) DEFAULT NULL COMMENT '银行行号',
    name varchar(200) DEFAULT NULL COMMENT '资金方名称',
    sign varchar(100) DEFAULT NULL COMMENT '资金方标识',
    valid varchar(10) DEFAULT '1' COMMENT '有效标识 1-有效 0-无效',
    rmk varchar(500) DEFAULT NULL COMMENT '备注',
    tenant_id varchar(32) DEFAULT NULL COMMENT '租户id',
    create_by varchar(32) DEFAULT NULL COMMENT '创建人',
    create_date datetime DEFAULT NULL COMMENT '创建日期',
    update_by varchar(32) DEFAULT NULL COMMENT '更新人',
    update_date datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '资金方信息表';


-- 表说明：融资业务主信息表
CREATE TABLE IF NOT EXISTS `scf_loan_info`
(
    `id`                        varchar(64)   NOT NULL COMMENT '主键ID',
    `loan_no`                   varchar(64)   DEFAULT NULL COMMENT '融资单号(RZ+年月日+3位流水号)',
    `tenant_id`                 varchar(64)   DEFAULT NULL COMMENT '租户id',
    `loan_company_id`           varchar(32)   DEFAULT NULL COMMENT '融资企业id',
    `loan_company_name`         varchar(200)  DEFAULT NULL COMMENT '融资企业名称',
    `voucher_code`              varchar(64)   DEFAULT NULL COMMENT '债权凭证编号',
    `core_company`              varchar(200)  DEFAULT NULL COMMENT '核心企业',
    `core_company_id`           varchar(64)   DEFAULT NULL COMMENT '核心企业id',
    `funder_id`                 varchar(32)   DEFAULT NULL COMMENT '资金方id',
    `funder_name`               varchar(200)  DEFAULT NULL COMMENT '资金方名称',
    `product_id`                varchar(32)   DEFAULT NULL COMMENT '融资产品id',
    `product_name`              varchar(200)  DEFAULT NULL COMMENT '融资产品名称',
    `platform_id`               varchar(32)   DEFAULT NULL COMMENT '平台ID',
    `platform_name`             varchar(200)  DEFAULT NULL COMMENT '平台名称',
    `cur_id`                    varchar(64)   DEFAULT NULL COMMENT '币别ID',
    `cur_name`                  varchar(32)   DEFAULT NULL COMMENT '币别名称',
    `loan_apply_date`           date          DEFAULT NULL COMMENT '融资申请日',
    `loan_amt`                  decimal(15,2) DEFAULT '0.00' COMMENT '融资金额',
    `interest_mode`             varchar(32)   DEFAULT '1' COMMENT '付息方式(1:卖方付息，2:买方付息，默认卖方付息)',
    `loan_lend_date`            date          DEFAULT NULL COMMENT '放款日(默认系统当前日期)',
    `loan_actual_date`          date          DEFAULT NULL COMMENT '实际放款日',
    `loan_due_date`             date          DEFAULT NULL COMMENT '到期日',
    `loan_deadline`             varchar(10)   DEFAULT NULL COMMENT '期限(到期日-放款日) 天',
    `loan_rate`                 decimal(15,2) DEFAULT '0.00' COMMENT '融资利率',
    `loan_interest_amt`         decimal(15,2) DEFAULT '0.00' COMMENT '利息金额(融资金额*融资利率)',
    `loan_hand_fee_rate`        decimal(15,2) DEFAULT '0.00' COMMENT '手续费率',
    `loan_hand_fee_amt`         decimal(15,2) DEFAULT '0.00' COMMENT '手续费(融资金额*手续费率)',
    `loan_service_fee_rate`     decimal(15,2) DEFAULT '0.00' COMMENT '服务费率',
    `loan_service_fee_amt`      decimal(15,2) DEFAULT '0.00' COMMENT '服务费(融资金额*服务费率)',
    `loan_cost_amt`             decimal(15,2) DEFAULT '0.00' COMMENT '融资成本(利息金额+手续费+服务费)',
    `loan_actual_amt`           decimal(15,2) DEFAULT '0.00' COMMENT '融资到账金额(融资金额-融资成本)',
    `loan_payment_account`      varchar(64)   DEFAULT NULL COMMENT '收款账号',
    `loan_payment_account_name` varchar(64)   DEFAULT NULL COMMENT '收款开户名',
    `loan_payment_account_bank` varchar(64)   DEFAULT NULL COMMENT '收款开户行',
    `receivable_id`             varchar(32)   DEFAULT NULL COMMENT '应收ID',
    `loan_status`               varchar(30)   DEFAULT 'draft' COMMENT '融资审批状态(DRAFT:暂存; APPROVAL_ING:审批中; REJECTED:打回; APPROVAL_PASS:审批通过)',
    `online_sign`               varchar(1)    DEFAULT NULL COMMENT '线上线下标识(0:线下；1:线上)',
    `enable`                    varchar(2)    DEFAULT '1' COMMENT '是否有效(0：无效；1：有效)',
    `create_by`                 varchar(32)   DEFAULT NULL COMMENT '创建人',
    `create_date`               datetime      DEFAULT NULL COMMENT '创建时间',
    `update_by`                 varchar(32)   DEFAULT NULL COMMENT '修改人',
    `update_date`               datetime      DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '融资业务主信息表';


-- 表说明：应收债权凭证表
CREATE TABLE IF NOT EXISTS `scf_receivable_voucher_info`
(
    `id`                        varchar(32)   NOT NULL COMMENT '主键',
    `voucher_no`                varchar(50)   DEFAULT NULL COMMENT '单据编号(系统自动创建AR+年月日+3位流水号)',
    `voucher_source`            varchar(50)   DEFAULT NULL COMMENT '单据来源(手工新增：MANUAL，ERP，供应链金融平台:SUPPLY_PLATFORM)',
    `tenant_id`                 varchar(64)   DEFAULT NULL COMMENT '租户id',
    `voucher_code`              varchar(50)   DEFAULT NULL COMMENT '债权凭证编号',
    `voucher_amt`               decimal(15,2) DEFAULT '0.00' COMMENT '凭证金额',
    `platform_id`               varchar(32)   DEFAULT NULL COMMENT '平台ID',
    `platform_name`             varchar(200)  DEFAULT NULL COMMENT '平台名称',
    `product_id`                varchar(32)   DEFAULT NULL COMMENT '融资产品id',
    `product_name`              varchar(200)  DEFAULT NULL COMMENT '融资产品名称',
    `corp_id`                   varchar(32)   DEFAULT NULL COMMENT '持有单位',
    `corp_name`                 varchar(64)   DEFAULT NULL COMMENT '持有单位名称',
    `sign_date`                 date          DEFAULT NULL COMMENT '签收日期',
    `sign_operation`            varchar(100)  DEFAULT NULL COMMENT '签收操作(签收,拒签,作废)',
    `voucher_type`              varchar(32)   DEFAULT NULL COMMENT '凭证类别(开立凭证,流转凭证)',
    `expire_date`               date          DEFAULT NULL COMMENT '凭证到期日',
    `voucher_status`            varchar(32)   DEFAULT NULL COMMENT '凭证业务状态(未签收,已签收,已拒签,已拆分)',
    `approval_status`           varchar(32)   DEFAULT NULL COMMENT '单据状态(暂存,审批中,打回,审批通过,已作废)',
    `core_company_id`           varchar(32)   DEFAULT NULL COMMENT '核心企业',
    `core_company_name`         varchar(200)  DEFAULT NULL COMMENT '核心企业名称',
    `cur_id`                    varchar(32)   DEFAULT NULL COMMENT '币种ID',
    `cur_name`                  varchar(16)   DEFAULT NULL COMMENT '币别名称',
    `issue_transfer_company`    varchar(200)  DEFAULT NULL COMMENT '开立/转让 企业',
    `issue_transfer_company_id` varchar(32)   DEFAULT NULL COMMENT '开立/转让 企业id',
    `source_voucher_code`       varchar(50)   DEFAULT NULL COMMENT '根凭证编号(开立成功时生成的债权凭证编号)',
    `forehand_voucher_code`     varchar(50)   DEFAULT NULL COMMENT '上级凭证编号',
    `online_sign`               varchar(2)    DEFAULT NULL COMMENT '线上线下标识',
    `allow_split`               varchar(2)    DEFAULT '1' COMMENT '是否允许拆分(0:否, 1:是)',
    `contract_code`             varchar(50)   DEFAULT NULL COMMENT '合同编码',
    `contract_name`             varchar(100)  DEFAULT NULL COMMENT '合同名称',
    `contract_amt`              decimal(15,2) DEFAULT '0.00' COMMENT '合同金额',
    `contract_date`             date          DEFAULT NULL COMMENT '合同日期',
    `contract_file_id`          varchar(32)   DEFAULT NULL COMMENT '合同文件id',
    `invoice_file_id`           varchar(32)   DEFAULT NULL COMMENT '发票文件id',
    `interest_mode`             varchar(32)   DEFAULT '1' COMMENT '融资付息方式(1:卖方付息，2:买方付息)',
    `promise_pay_date`          date          DEFAULT NULL COMMENT '承诺付款日',
    `transfer_company`          varchar(200)  DEFAULT NULL COMMENT '流转单位',
    `transfer_company_id`       varchar(32)   DEFAULT NULL COMMENT '流转单位ID',
    `enable`                    tinyint(1)    DEFAULT '1' COMMENT '是否有效',
    `create_by`                 varchar(32)   DEFAULT NULL COMMENT '创建人',
    `create_date`               datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`                 varchar(32)   DEFAULT NULL COMMENT '修改人',
    `update_date`               datetime      DEFAULT NULL COMMENT '修改时间',
    `cancel_date`               datetime      DEFAULT NULL COMMENT '作废时间',
    `cancel_reason`             varchar(500)  DEFAULT NULL COMMENT '作废原因',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '应收债权凭证表';


-- 表说明：债权凭证转让信息表
CREATE TABLE IF NOT EXISTS `scf_receivable_transfer_info`
(
    `id`                        varchar(32)   NOT NULL COMMENT '主键',
    `transfer_no`               varchar(32)   DEFAULT NULL COMMENT '凭证转让记录编号(系统自动创建ZR+年月日+3位流水号)',
    `transfer_batch_no`         varchar(32)   DEFAULT NULL COMMENT '转让批次号',
    `tenant_id`                 varchar(64)   DEFAULT NULL COMMENT '租户id',
    `voucher_code`              varchar(64)   NOT NULL COMMENT '债权凭证编号',
    `transfer_status`           varchar(32)   DEFAULT NULL COMMENT '凭证转让状态',
    `transfer_flag`             varchar(2)    DEFAULT NULL COMMENT '转让标识(1:一对多-凭证拆分转让多家企业;2:多对一-多张凭证转让一家企业)',
    `platform_id`               varchar(64)   DEFAULT NULL COMMENT '平台',
    `platform_name`             varchar(200)  DEFAULT NULL COMMENT '平台名称',
    `product_id`                varchar(64)   DEFAULT NULL COMMENT '融资产品',
    `product_name`              varchar(200)  DEFAULT NULL COMMENT '融资产品id',
    `issue_transfer_company`    varchar(200)  DEFAULT NULL COMMENT '开立/转让单位',
    `issue_transfer_company_id` varchar(32)   DEFAULT NULL COMMENT '开立/转让 企业id',
    `cur_id`                    varchar(32)   DEFAULT 'CNY' COMMENT '币别',
    `cur_name`                  varchar(16)   DEFAULT NULL COMMENT '币别名称',
    `receive_company`           varchar(200)  DEFAULT NULL COMMENT '接收单位id',
    `receive_company_id`        varchar(64)   DEFAULT NULL COMMENT '接收单位名称',
    `receiver_credit_code`      varchar(32)   DEFAULT NULL COMMENT '接收方统一社会信用代码',
    `transfer_date`             date          DEFAULT NULL COMMENT '转让日期',
    `transfer_amt`              decimal(15,2) DEFAULT '0.00' COMMENT '转让金额',
    `purpose`                   varchar(255)  DEFAULT NULL COMMENT '用途',
    `remark`                    varchar(512)  DEFAULT NULL COMMENT '备注',
    `pay_id`                    varchar(32)   DEFAULT NULL COMMENT '关联付款单ID(付款单审批通过后写入)',
    `pay_no`                    varchar(64)   DEFAULT NULL COMMENT '关联付款单号(付款单审批通过后写入)',
    `create_by`                 varchar(32)   DEFAULT NULL COMMENT '创建人',
    `create_date`               datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`                 varchar(32)   DEFAULT NULL COMMENT '修改人',
    `update_date`               datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '债权凭证转让信息表';


-- 表说明：债权凭证收款明细表
CREATE TABLE IF NOT EXISTS `scf_receivable_payment_info`
(
    `id`                        varchar(32)   NOT NULL COMMENT '主键',
    `voucher_code`              varchar(64)   NOT NULL COMMENT '债权凭证编号',
    `payment_no`                varchar(64)   NOT NULL COMMENT '收款单号',
    `tenant_id`                 varchar(64)   DEFAULT NULL COMMENT '租户id',
    `payment_status`            varchar(32)   DEFAULT NULL COMMENT '收款状态',
    `platform_id`               varchar(64)   DEFAULT NULL COMMENT '平台',
    `platform_name`             varchar(200)  DEFAULT NULL COMMENT '平台名称',
    `product_id`                varchar(64)   DEFAULT NULL COMMENT '融资产品',
    `product_name`              varchar(200)  DEFAULT NULL COMMENT '融资产品id',
    `receipt_company`           varchar(200)  DEFAULT NULL COMMENT '收款单位',
    `receipt_company_id`        varchar(128)  DEFAULT NULL COMMENT '收款单位id',
    `sign_date`                 date          DEFAULT NULL COMMENT '签收日期',
    `expire_date`               date          DEFAULT NULL COMMENT '到期日',
    `issue_transfer_company`    varchar(200)  DEFAULT NULL COMMENT '开立/转让单位',
    `issue_transfer_company_id` varchar(32)   DEFAULT NULL COMMENT '开立/转让 企业id',
    `cur_id`                    varchar(32)   DEFAULT 'CNY' COMMENT '币别',
    `cur_name`                  varchar(32)   DEFAULT NULL COMMENT '币别名称',
    `voucher_amt`               decimal(15,2) DEFAULT '0.00' COMMENT '凭证金额',
    `core_company_id`           varchar(128)  DEFAULT NULL COMMENT '核心企业id',
    `core_company_name`         varchar(200)  DEFAULT NULL COMMENT '核心企业',
    `rec_account_no`            varchar(64)   DEFAULT NULL COMMENT '收款账号',
    `rec_account_name`          varchar(128)  DEFAULT NULL COMMENT '收款户名',
    `rec_bank`                  varchar(128)  DEFAULT NULL COMMENT '收款开户行',
    `rec_amt`                   decimal(15,2) DEFAULT '0.00' COMMENT '收款金额',
    `receipt_date`              date          DEFAULT NULL COMMENT '收款日期',
    `enable`                    varchar(2)    DEFAULT '1' COMMENT '是否有效(0：无效；1：有效)',
    `create_by`                 varchar(32)   DEFAULT NULL COMMENT '创建人',
    `create_date`               datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`                 varchar(32)   DEFAULT NULL COMMENT '修改人',
    `update_date`               datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '债权凭证收款明细表';


-- 表说明：应付/应收债券发票信息
CREATE TABLE IF NOT EXISTS `scf_invoice_info`
(
    `id`             varchar(32)   NOT NULL COMMENT 'id',
    `tenant_id`      varchar(64)   DEFAULT NULL COMMENT '租户id',
    `invoice_type`   varchar(50)   DEFAULT NULL COMMENT '发票类型',
    `invoice_number` varchar(50)   DEFAULT NULL COMMENT '发票号码',
    `invoice_code`   varchar(50)   DEFAULT NULL COMMENT '发票代码',
    `bill_date`      datetime      DEFAULT NULL COMMENT '开票日期',
    `cur_name`       varchar(100)  DEFAULT NULL COMMENT '币种名称',
    `cur_id`         varchar(32)   DEFAULT NULL COMMENT '币种',
    `total_amt`      decimal(15,2) DEFAULT '0.00' COMMENT '价税合计',
    `tax_rate`       decimal(15,2) DEFAULT '0.00' COMMENT '税率',
    `tax_amt`        decimal(15,2) DEFAULT '0.00' COMMENT '税额',
    `bill_amt`       decimal(15,2) DEFAULT '0.00' COMMENT '金额',
    `seller_name`    varchar(100)  DEFAULT NULL COMMENT '销方名称',
    `buyer_name`     varchar(100)  DEFAULT NULL COMMENT '购方名称',
    `pay_id`         varchar(32)   DEFAULT NULL COMMENT '应付凭证id',
    `receivable_id`  varchar(32)   DEFAULT NULL COMMENT '应收凭证id',
    `sign`           varchar(1)    DEFAULT NULL COMMENT '应付应收标识(0为应付，1为应收)',
    `create_by`      varchar(32)   DEFAULT NULL COMMENT '创建人',
    `create_date`    datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(32)   DEFAULT NULL COMMENT '修改人',
    `update_date`    datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '应付/应收债券发票信息';


-- 表说明：付款单-供应链金融债权凭证关联表
CREATE TABLE IF NOT EXISTS `nis_scf_info`
(
    `id`           varchar(32) NOT NULL COMMENT 'id',
    `pay_id`       varchar(32) DEFAULT NULL COMMENT '付款单ID',
    `pay_no`       varchar(50) DEFAULT NULL COMMENT '付款单号',
    `voucher_type` varchar(2)  DEFAULT NULL COMMENT '凭证类型(1=应付债权凭证，2=应收债权凭证)',
    `voucher_id`   varchar(32) DEFAULT NULL COMMENT '债权凭证ID',
    `transfer_id`  varchar(32) DEFAULT NULL COMMENT '转让草稿ID(52-应收债权凭证转让暂存)',
    `platform_id`  varchar(32) DEFAULT NULL COMMENT '供应链金融平台ID(51-未开立生成暂存凭证用)',
    `product_id`   varchar(32) DEFAULT NULL COMMENT '融资产品ID(51-未开立生成暂存凭证用)',
    `tenant_id`    varchar(32) DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_nis_scf_info_pay` (`pay_id`),
    KEY `idx_nis_scf_info_voucher` (`voucher_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '付款单-供应链金融债权凭证关联表';


-- 表说明：司库银行端限额规则主表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_RULE` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `RULE_CODE` VARCHAR(32) NOT NULL COMMENT '规则编码，PL+4位流水号',
    `RULE_NAME` VARCHAR(128) NOT NULL COMMENT '规则名称',
    `BANK_ORG_ID` VARCHAR(32) NOT NULL COMMENT '签约机构ID',
    `TENANT_ID` VARCHAR(32) NOT NULL COMMENT '租户ID',
    `SIGNED_CUST_NAME` VARCHAR(200) NULL COMMENT '签约客户名称快照',
    `DIMENSION_TYPE` CHAR(1) NOT NULL COMMENT '限制维度：U单位/A银行账户',
    `SELECT_MODE` CHAR(1) NOT NULL COMMENT '范围模式：A全部/C自定义',
    `CHANNEL_CODE` VARCHAR(32) NOT NULL DEFAULT 'EBANK' COMMENT '管控渠道，默认银企直联',
    `CURRENCY_ID` VARCHAR(32) NOT NULL COMMENT '限额币种ID',
    `PAY_LIMIT_FLAG` CHAR(1) NOT NULL DEFAULT '0' COMMENT '支付限额标志：0否/1是',
    `LAND_LIMIT_FLAG` CHAR(1) NOT NULL DEFAULT '0' COMMENT '落地限额标志：0否/1是',
    `ENABLE_FLAG` CHAR(1) NOT NULL DEFAULT '1' COMMENT '启用标志：0禁用/1启用',
    `REMARK` VARCHAR(1000) NULL COMMENT '备注',
    `VERSION_NO` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `DEL_FLAG` CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常/1删除',
    `CREATE_BY` VARCHAR(32) NULL COMMENT '创建人ID',
    `CREATE_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY` VARCHAR(32) NULL COMMENT '更新人ID',
    `UPDATE_DATE` DATETIME NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_TR_LR_TEN_CODE` (`TENANT_ID`, `RULE_CODE`) USING BTREE,
    KEY `IX_TR_LR_QUERY` (`TENANT_ID`, `BANK_ORG_ID`, `ENABLE_FLAG`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '司库银行端限额规则主表';

-- 表说明：限额规则自定义适用范围表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_SCOPE` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `RULE_ID` VARCHAR(32) NOT NULL COMMENT '限额规则ID',
    `DIMENSION_TYPE` CHAR(1) NOT NULL COMMENT '限制维度：U单位/A银行账户',
    `SCOPE_REF_ID` VARCHAR(32) NOT NULL COMMENT '单位ID或银行账户ID',
    `SCOPE_CODE` VARCHAR(128) NULL COMMENT '单位编码或脱敏账号快照',
    `SCOPE_NAME` VARCHAR(200) NULL COMMENT '单位名称或账户名称快照',
    `DEL_FLAG` CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标志；删除后恢复原记录',
    `CREATE_BY` VARCHAR(32) NULL COMMENT '创建人ID',
    `CREATE_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY` VARCHAR(32) NULL COMMENT '更新人ID',
    `UPDATE_DATE` DATETIME NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_TR_LRS_SCOPE` (`RULE_ID`, `DIMENSION_TYPE`, `SCOPE_REF_ID`) USING BTREE,
    KEY `IX_TR_LRS_RULE` (`RULE_ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '限额规则自定义适用范围表';

-- 表说明：限额规则条件表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_COND` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `RULE_ID` VARCHAR(32) NOT NULL COMMENT '限额规则ID',
    `LIMIT_TYPE` CHAR(1) NOT NULL COMMENT '限额类型：P支付/L落地',
    `LIMIT_MODE` VARCHAR(24) NOT NULL COMMENT '限定方式编码',
    `VALUE_TYPE` CHAR(1) NOT NULL COMMENT '阈值类型：A金额/C笔数',
    `LIMIT_AMOUNT` DECIMAL(20,6) NULL COMMENT '限定金额',
    `LIMIT_COUNT` BIGINT NULL COMMENT '限定笔数',
    `SORT_NO` INT NOT NULL DEFAULT 0 COMMENT '条件排序号',
    `DEL_FLAG` CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标志；删除后恢复原记录',
    `CREATE_BY` VARCHAR(32) NULL COMMENT '创建人ID',
    `CREATE_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY` VARCHAR(32) NULL COMMENT '更新人ID',
    `UPDATE_DATE` DATETIME NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_TR_LC_RULE_MODE` (`RULE_ID`, `LIMIT_TYPE`, `LIMIT_MODE`) USING BTREE,
    KEY `IX_TR_LC_RULE` (`RULE_ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '限额规则条件表';

-- 表说明：限额计数交易台账表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_TXN` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `SOURCE_BIZ_TYPE` VARCHAR(32) NOT NULL COMMENT '来源业务类型：付款办理/资金收付',
    `SOURCE_BIZ_ID` VARCHAR(32) NOT NULL COMMENT '支付指令或最小付款明细ID',
    `ATTEMPT_VERSION` INT NOT NULL DEFAULT 1 COMMENT '付款尝试版本：同一业务单据重新提交时递增',
    `BUSINESS_NO` VARCHAR(64) NOT NULL COMMENT '业务单号',
    `BANK_ORG_ID` VARCHAR(32) NOT NULL COMMENT '签约机构ID',
    `TENANT_ID` VARCHAR(32) NOT NULL COMMENT '租户ID',
    `UNIT_ID` VARCHAR(32) NOT NULL COMMENT '付款单位ID',
    `ACCOUNT_ID` VARCHAR(32) NOT NULL COMMENT '付款账户ID',
    `ACCOUNT_MASK` VARCHAR(64) NULL COMMENT '脱敏付款账号快照',
    `CHANNEL_CODE` VARCHAR(32) NOT NULL DEFAULT 'EBANK' COMMENT '管控渠道',
    `ORIGINAL_CURRENCY_ID` VARCHAR(32) NOT NULL COMMENT '原币币种ID',
    `ORIGINAL_AMOUNT` DECIMAL(20,6) NOT NULL DEFAULT 0 COMMENT '原币金额',
    `LIMIT_CURRENCY_ID` VARCHAR(32) NOT NULL COMMENT '折算后限额币种ID',
    `LIMIT_AMOUNT` DECIMAL(20,6) NOT NULL DEFAULT 0 COMMENT '折算后限额金额',
    `FX_RATE` DECIMAL(20,10) NULL COMMENT '折算汇率',
    `FX_SOURCE` CHAR(1) NULL COMMENT '汇率来源：D单据/F折算汇率',
    `SUBMIT_TIME` DATETIME NOT NULL COMMENT '提交时间',
    `BIZ_STATUS` VARCHAR(24) NOT NULL COMMENT '业务单据状态',
    `TRANS_STATUS` VARCHAR(24) NOT NULL COMMENT '交易状态',
    `COUNTER_STATE` CHAR(1) NOT NULL DEFAULT 'N' COMMENT '计数状态：N未占用/T在途/S成功/R释放',
    `LANDING_RESULT` CHAR(1) NULL COMMENT '落地结果：A同意/R拒绝',
    `VERSION_NO` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `LAST_EVENT_TIME` DATETIME NULL COMMENT '最近事件时间',
    `CREATE_BY` VARCHAR(32) NULL COMMENT '创建人ID',
    `CREATE_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY` VARCHAR(32) NULL COMMENT '更新人ID',
    `UPDATE_DATE` DATETIME NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_TR_LT_SOURCE` (`TENANT_ID`, `SOURCE_BIZ_TYPE`, `SOURCE_BIZ_ID`, `ATTEMPT_VERSION`) USING BTREE,
    KEY `IX_TR_LT_STATE` (`TENANT_ID`, `COUNTER_STATE`, `SUBMIT_TIME`) USING BTREE,
    KEY `IX_TR_LT_BIZNO` (`TENANT_ID`, `BUSINESS_NO`) USING BTREE,
    KEY `IX_TR_LT_BACKFILL` (`SOURCE_BIZ_TYPE`, `COUNTER_STATE`, `CREATE_DATE`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '限额计数交易台账表';

-- 表说明：限额周期用量汇总表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_USAGE` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `TENANT_ID` VARCHAR(32) NOT NULL COMMENT '租户ID',
    `SCOPE_TYPE` CHAR(1) NOT NULL COMMENT '统计维度：U单位/A银行账户',
    `SCOPE_REF_ID` VARCHAR(32) NOT NULL COMMENT '单位ID或银行账户ID',
    `CURRENCY_ID` VARCHAR(32) NOT NULL COMMENT '统计币种ID',
    `CHANNEL_CODE` VARCHAR(32) NOT NULL DEFAULT 'EBANK' COMMENT '管控渠道',
    `PERIOD_TYPE` CHAR(1) NOT NULL COMMENT '周期：D日/W周/M月/Q季/Y年',
    `PERIOD_START` DATE NOT NULL COMMENT '周期起始日期',
    `INTRANSIT_AMOUNT` DECIMAL(20,6) NOT NULL DEFAULT 0 COMMENT '在途金额',
    `INTRANSIT_COUNT` BIGINT NOT NULL DEFAULT 0 COMMENT '在途笔数',
    `SUCCESS_AMOUNT` DECIMAL(20,6) NOT NULL DEFAULT 0 COMMENT '成功金额',
    `SUCCESS_COUNT` BIGINT NOT NULL DEFAULT 0 COMMENT '成功笔数',
    `VERSION_NO` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `UPDATE_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_TR_LU_BUCKET` (`TENANT_ID`, `SCOPE_TYPE`, `SCOPE_REF_ID`, `CURRENCY_ID`, `CHANNEL_CODE`, `PERIOD_TYPE`, `PERIOD_START`) USING BTREE,
    KEY `IX_TR_LU_SCOPE` (`TENANT_ID`, `SCOPE_REF_ID`, `PERIOD_START`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '限额周期用量汇总表';

-- 表说明：限额校验批次日志表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_CHECK_LOG` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `CHECK_BATCH_NO` VARCHAR(64) NOT NULL COMMENT '校验批次号/幂等键',
    `TXN_ID` VARCHAR(32) NOT NULL COMMENT '限额交易台账ID',
    `CHECK_TYPE` CHAR(1) NOT NULL COMMENT '校验类型：P支付/L落地',
    `CHECK_TIME` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '校验时间',
    `CHECK_RESULT` CHAR(1) NOT NULL COMMENT '结果：P通过/B拦截/L落地',
    `UNIT_RULE_ID` VARCHAR(32) NULL COMMENT '命中的单位规则ID',
    `ACCOUNT_RULE_ID` VARCHAR(32) NULL COMMENT '命中的账户规则ID',
    `REQUEST_AMOUNT` DECIMAL(20,6) NOT NULL DEFAULT 0 COMMENT '本次校验限额币别金额',
    `CURRENCY_ID` VARCHAR(32) NOT NULL COMMENT '付款币种ID',
    `HIT_COUNT` INT NOT NULL DEFAULT 0 COMMENT '触发条件数',
    `ERROR_CODE` VARCHAR(32) NULL COMMENT '异常码',
    `ERROR_MESSAGE` VARCHAR(500) NULL COMMENT '异常说明',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_TR_LCL_BATCH` (`CHECK_BATCH_NO`) USING BTREE,
    KEY `IX_TR_LCL_TXN` (`TXN_ID`, `CHECK_TIME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '限额校验批次日志表';

-- 表说明：落地限额处理单主表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_CASE` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `CASE_NO` VARCHAR(64) NOT NULL COMMENT '落地限额处理单号',
    `TXN_ID` VARCHAR(32) NOT NULL COMMENT '限额交易台账ID',
    `CHECK_LOG_ID` VARCHAR(32) NOT NULL COMMENT '落地校验日志ID',
    `SOURCE_BIZ_TYPE` VARCHAR(32) NOT NULL COMMENT '来源业务类型',
    `SOURCE_BIZ_ID` VARCHAR(32) NOT NULL COMMENT '来源业务ID',
    `ATTEMPT_VERSION` INT NOT NULL DEFAULT 1 COMMENT '付款尝试版本：同一业务单据重新提交时递增',
    `BIZ_TYPE` VARCHAR(32) NOT NULL DEFAULT 'LAND_CASE' COMMENT '业务类型：LAND_CASE落地限额处理',
    `BUSINESS_NO` VARCHAR(64) NOT NULL COMMENT '业务单号',
    `BANK_ORG_ID` VARCHAR(32) NOT NULL COMMENT '签约机构ID',
    `TENANT_ID` VARCHAR(32) NOT NULL COMMENT '租户ID',
    `UNIT_ID` VARCHAR(32) NOT NULL COMMENT '付款单位ID',
    `ACCOUNT_ID` VARCHAR(32) NOT NULL COMMENT '付款账户ID',
    `ACCOUNT_MASK` VARCHAR(64) NULL COMMENT '脱敏付款账号快照',
    `PAY_AMOUNT` DECIMAL(20,6) NOT NULL DEFAULT 0 COMMENT '付款金额',
    `CURRENCY_ID` VARCHAR(32) NOT NULL COMMENT '付款币种ID',
    `APPLICANT_ID` VARCHAR(32) NOT NULL COMMENT '申请人ID',
    `APPLICANT_NAME` VARCHAR(100) NULL COMMENT '申请人名称快照',
    `APPLY_TIME` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `CASE_STATUS` CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态：0待处理/1待审批/2审批通过/3打回',
    `HANDLE_RESULT` CHAR(1) NULL COMMENT '处理结果：A同意/R拒绝',
    `HANDLE_DESC` VARCHAR(1000) NULL COMMENT '处理说明',
    `HANDLE_BY` VARCHAR(32) NULL COMMENT '处理人ID',
    `HANDLE_TIME` DATETIME NULL COMMENT '处理时间',
    `PROCESS_INSTANCE_ID` VARCHAR(64) NULL COMMENT '审批流程实例ID',
    `VERSION_NO` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `CREATE_BY` VARCHAR(32) NULL COMMENT '创建人ID',
    `CREATE_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY` VARCHAR(32) NULL COMMENT '更新人ID',
    `UPDATE_DATE` DATETIME NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_TR_LCASE_NO` (`TENANT_ID`, `CASE_NO`) USING BTREE,
    UNIQUE KEY `UK_TR_LCASE_TXN` (`TENANT_ID`, `TXN_ID`) USING BTREE,
    KEY `IX_TR_LCASE_QUERY` (`TENANT_ID`, `BANK_ORG_ID`, `CASE_STATUS`, `APPLY_TIME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '落地限额处理单主表';

-- 表说明：限额触发条件快照明细表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_HIT_DETAIL` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `CHECK_LOG_ID` VARCHAR(32) NOT NULL COMMENT '校验日志ID',
    `CASE_ID` VARCHAR(32) NULL COMMENT '落地限额处理单ID',
    `TXN_ID` VARCHAR(32) NOT NULL COMMENT '限额交易台账ID',
    `CONDITION_ID` VARCHAR(32) NOT NULL COMMENT '触发条件ID',
    `RULE_ID` VARCHAR(32) NOT NULL COMMENT '规则ID',
    `RULE_CODE` VARCHAR(32) NOT NULL COMMENT '规则编码快照',
    `RULE_NAME` VARCHAR(128) NOT NULL COMMENT '规则名称快照',
    `RULE_SCOPE_TYPE` CHAR(1) NOT NULL COMMENT '规则维度：U单位/A银行账户',
    `LIMIT_TYPE` CHAR(1) NOT NULL COMMENT '限额类型：P支付/L落地',
    `LIMIT_MODE` VARCHAR(24) NOT NULL COMMENT '限定方式',
    `LIMIT_AMOUNT` DECIMAL(20,6) NULL COMMENT '限定金额快照',
    `ACTUAL_AMOUNT` DECIMAL(20,6) NULL COMMENT '实际金额',
    `EXCESS_AMOUNT` DECIMAL(20,6) NULL COMMENT '超出金额',
    `LIMIT_COUNT` BIGINT NULL COMMENT '限定笔数快照',
    `ACTUAL_COUNT` BIGINT NULL COMMENT '实际笔数',
    `EXCESS_COUNT` BIGINT NULL COMMENT '超出笔数',
    `EXCESS_RATIO` DECIMAL(18,6) NULL COMMENT '超出比例，保存小数',
    `CHANNEL_CODE` VARCHAR(32) NOT NULL DEFAULT 'EBANK' COMMENT '管控渠道',
    `DISPLAY_SEQ` INT NOT NULL DEFAULT 0 COMMENT '展示顺序',
    `CREATE_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_TR_LHD_LOG_COND` (`CHECK_LOG_ID`, `CONDITION_ID`) USING BTREE,
    KEY `IX_TR_LHD_CASE` (`CASE_ID`, `DISPLAY_SEQ`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '限额触发条件快照明细表';

-- 表说明：限额规则变更审计表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_RULE_AUDIT` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `RULE_ID` VARCHAR(32) NOT NULL COMMENT '限额规则ID',
    `OPERATION_TYPE` VARCHAR(16) NOT NULL COMMENT '操作类型：CREATE/UPDATE/DELETE/ENABLE/DISABLE',
    `BEFORE_SNAPSHOT` TEXT NULL COMMENT '变更前JSON快照',
    `AFTER_SNAPSHOT` TEXT NULL COMMENT '变更后JSON快照',
    `OPERATOR_ID` VARCHAR(32) NOT NULL COMMENT '操作人ID',
    `OPERATOR_NAME` VARCHAR(100) NULL COMMENT '操作人名称快照',
    `OPERATION_TIME` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `IX_TR_LRA_RULE` (`RULE_ID`, `OPERATION_TIME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '限额规则变更审计表';

-- 表说明：限额交易用量桶快照表
CREATE TABLE IF NOT EXISTS `BT_TR_LIMIT_TXN_BUCKET` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `TXN_ID` VARCHAR(32) NOT NULL COMMENT '限额交易台账ID',
    `TENANT_ID` VARCHAR(32) NOT NULL COMMENT '租户ID',
    `SCOPE_TYPE` CHAR(1) NOT NULL COMMENT '统计维度：U单位/A银行账户',
    `SCOPE_REF_ID` VARCHAR(32) NOT NULL COMMENT '单位ID或银行账户ID',
    `CURRENCY_ID` VARCHAR(32) NOT NULL COMMENT '统计币种ID',
    `CHANNEL_CODE` VARCHAR(32) NOT NULL DEFAULT 'EBANK' COMMENT '管控渠道',
    `PERIOD_TYPE` CHAR(1) NOT NULL COMMENT '周期：D日/W周/M月/Q季/Y年',
    `PERIOD_START` DATE NOT NULL COMMENT '周期起始日期',
    `OCCUPIED_AMOUNT` DECIMAL(20,6) NOT NULL DEFAULT 0 COMMENT '该交易占用的折算金额',
    `OCCUPIED_COUNT` BIGINT NOT NULL DEFAULT 0 COMMENT '该交易占用的笔数',
    `BEFORE_OCCUPIED_AMOUNT` DECIMAL(20,6) NULL COMMENT '本交易占用前的累计金额',
    `BEFORE_OCCUPIED_COUNT` BIGINT NULL COMMENT '本交易占用前的累计笔数',
    `COUNTER_STATE` CHAR(1) NOT NULL DEFAULT 'T' COMMENT '计数状态：T在途/S成功/R释放',
    `CREATE_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_DATE` DATETIME NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `IX_TR_LTB_TXN` (`TENANT_ID`, `TXN_ID`, `COUNTER_STATE`) USING BTREE,
    KEY `IX_TR_LTB_BACKFILL` (`TENANT_ID`, `PERIOD_START`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic COMMENT = '限额交易用量桶快照表';


-- BEGIN GENERATED MYSQL FUNDWARN SCHEMA
-- 表说明：账户类型表
CREATE TABLE IF NOT EXISTS `TP_ACCOUNT_TYPE` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键ID',
    `CFC_CODE` VARCHAR(64) NOT NULL COMMENT '平台代码',
    `ACCOUNT_TYPE_CODE` VARCHAR(64) NOT NULL COMMENT '账户类型代码',
    `ACCOUNT_TYPE_NAME` VARCHAR(255) NOT NULL COMMENT '账户类型名称',
    `VALID_SIGN` CHAR(1) NOT NULL COMMENT '是否生效; Y/N',
    `SYSTEM_SIGN` CHAR(1) NOT NULL COMMENT '是否系统预设; Y/N',
    `IS_DELETED` CHAR(1) DEFAULT 'N' COMMENT '是否删除; Y/N',
    `REMARKS` VARCHAR(255) COMMENT '备注',
    `CONF_TYPE` VARCHAR(255) NOT NULL COMMENT '电商平台/支付平台',
    `CREATE_DATE` DATETIME COMMENT '创建时间',
    `UPDATE_DATE` DATETIME COMMENT '修改时间',
    `CREATE_BY` VARCHAR(64) COMMENT '创建人',
    `UPDATE_BY` VARCHAR(64) COMMENT '修改人',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '账户类型表';

-- 表说明：电商离线账户余额表
CREATE TABLE IF NOT EXISTS `TP_OFFLINE_BAL` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键ID',
    `PLATFORM_CODE` VARCHAR(64) NOT NULL COMMENT '平台代码',
    `APP_ID` VARCHAR(64) COMMENT '应用ID',
    `MERCHANT_NO` VARCHAR(64) NOT NULL COMMENT '商户号',
    `ACCOUNT_TYPE_CODE` VARCHAR(64) COMMENT '账户类型代码',
    `PF_ACCOUNT_TYPE_CODE` VARCHAR(64) COMMENT '开放平台账户类型代码',
    `BALANCE_DATE` DATETIME NOT NULL COMMENT '余额日期',
    `BALANCE` DECIMAL(20,2) NOT NULL COMMENT '余额',
    `ENABLE_AMOUNT` DECIMAL(20,2) COMMENT '可用金额',
    `DISABLE_AMOUNT` DECIMAL(20,2) COMMENT '冻结金额',
    `CURRENCY_TYPE` VARCHAR(10) COMMENT '货币类型',
    `DESCRIPTION` VARCHAR(255) COMMENT '描述',
    `REMARKS` VARCHAR(255) COMMENT '备注',
    `INIT_DATE` DATETIME COMMENT '初始日期',
    `INIT_BALANCE` DECIMAL(20,2) COMMENT '初始余额',
    `CREATE_DATE` DATETIME COMMENT '创建时间',
    `UPDATE_DATE` DATETIME COMMENT '修改时间',
    `CREATE_BY` VARCHAR(64) COMMENT '创建人',
    `UPDATE_BY` VARCHAR(64) COMMENT '修改人',
    PRIMARY KEY (`ID`),
    KEY `IDX_BALANCE_DATE` (`BALANCE_DATE`),
    KEY `IDX_MCNO_DATE` (`MERCHANT_NO`, `BALANCE_DATE`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '电商离线账户余额表';

-- 表说明：电商离线保证金余额表
CREATE TABLE IF NOT EXISTS `TP_OFFLINE_BOND_BAL` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键ID',
    `PLATFORM_CODE` VARCHAR(64) NOT NULL COMMENT '平台代码',
    `APP_ID` VARCHAR(64) COMMENT '应用ID',
    `MERCHANT_NO` VARCHAR(64) NOT NULL COMMENT '商户号',
    `ACCOUNT_TYPE_CODE` VARCHAR(64) COMMENT '账户类型代码',
    `PF_ACCOUNT_TYPE_CODE` VARCHAR(64) COMMENT '开放平台账户类型代码',
    `BALANCE_DATE` DATETIME NOT NULL COMMENT '余额日期',
    `BALANCE` DECIMAL(20,2) NOT NULL COMMENT '余额',
    `ENABLE_AMOUNT` DECIMAL(20,2) COMMENT '可用金额',
    `DISABLE_AMOUNT` DECIMAL(20,2) COMMENT '冻结金额',
    `PAYABLE_AMOUNT` DECIMAL(20,2) COMMENT '应缴金额',
    `CURRENCY_TYPE` VARCHAR(10) COMMENT '货币类型',
    `DESCRIPTION` VARCHAR(255) COMMENT '描述',
    `REMARKS` VARCHAR(255) COMMENT '备注',
    `CREATE_DATE` DATETIME COMMENT '创建时间',
    `UPDATE_DATE` DATETIME COMMENT '修改时间',
    `CREATE_BY` VARCHAR(64) COMMENT '创建人',
    `UPDATE_BY` VARCHAR(64) COMMENT '修改人',
    PRIMARY KEY (`ID`),
    KEY `IDX_MERCHANTNO_BALANCEDATE` (`MERCHANT_NO`, `BALANCE_DATE`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '电商离线保证金余额表';

-- 表说明：电商离线保证金流水表
CREATE TABLE IF NOT EXISTS `TP_OFFLINE_BOND_FLOW_DTL` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键ID',
    `PLATFORM_CODE` VARCHAR(64) NOT NULL COMMENT '平台代码',
    `APP_ID` VARCHAR(64) COMMENT '应用ID',
    `MERCHANT_NO` VARCHAR(64) NOT NULL COMMENT '商户号',
    `SERIAL_NO` VARCHAR(64) NOT NULL COMMENT '流水号',
    `ACCOUNT_TYPE_CODE` VARCHAR(64) COMMENT '账户类型代码',
    `PF_ACCOUNT_TYPE_CODE` VARCHAR(64) COMMENT '开放平台账户类型代码',
    `DIRECTION_TYPE` VARCHAR(20) NOT NULL COMMENT '动账方向;收入/支出=IN/OUT',
    `TRADE_TYPE` VARCHAR(64) COMMENT '交易类型',
    `TRADE_TIME` DATETIME NOT NULL COMMENT '交易时间',
    `TRADE_MONEY` DECIMAL(20,2) NOT NULL COMMENT '交易金额',
    `CURRENCY_TYPE` VARCHAR(10) COMMENT '货币类型',
    `DESCRIPTION` VARCHAR(255) COMMENT '描述',
    `REMARKS` VARCHAR(255) COMMENT '备注',
    `CREATE_DATE` DATETIME COMMENT '创建时间',
    `UPDATE_DATE` DATETIME COMMENT '修改时间',
    `CREATE_BY` VARCHAR(64) COMMENT '创建人',
    `UPDATE_BY` VARCHAR(64) COMMENT '修改人',
    PRIMARY KEY (`ID`),
    KEY `IDX_MERCHANTNO_TRADETIME` (`MERCHANT_NO`, `TRADE_TIME`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '电商离线保证金流水表';

-- 表说明：电商离线账户流水表
CREATE TABLE IF NOT EXISTS `TP_OFFLINE_FLOW_DTL` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键ID',
    `PLATFORM_CODE` VARCHAR(64) NOT NULL COMMENT '平台代码',
    `APP_ID` VARCHAR(64) COMMENT '应用ID',
    `MERCHANT_NO` VARCHAR(64) NOT NULL COMMENT '商户号',
    `SERIAL_NO` VARCHAR(64) NOT NULL COMMENT '流水号',
    `TRADE_NO` VARCHAR(64) NOT NULL COMMENT '交易号',
    `ACCOUNT_TYPE_CODE` VARCHAR(64) COMMENT '账户类型代码',
    `PF_ACCOUNT_TYPE_CODE` VARCHAR(64) COMMENT '开放平台账户类型代码',
    `DIRECTION_TYPE` VARCHAR(20) NOT NULL COMMENT '动账方向;收入/支出=IN/OUT',
    `TRADE_TYPE` VARCHAR(255) COMMENT '交易类型',
    `TRADE_TIME` DATETIME NOT NULL COMMENT '交易时间',
    `TRADE_MONEY` DECIMAL(20,2) NOT NULL COMMENT '交易金额',
    `CURRENCY_TYPE` VARCHAR(10) COMMENT '货币类型',
    `DESCRIPTION` VARCHAR(255) COMMENT '描述',
    `REMARKS` VARCHAR(255) COMMENT '备注',
    `CREATE_DATE` DATETIME COMMENT '创建时间',
    `UPDATE_DATE` DATETIME COMMENT '修改时间',
    `CREATE_BY` VARCHAR(64) COMMENT '创建人',
    `UPDATE_BY` VARCHAR(64) COMMENT '修改人',
    PRIMARY KEY (`ID`),
    KEY `IDX_TRADE_TIME` (`TRADE_TIME`),
    KEY `IDX_MCNO_TIME` (`MERCHANT_NO`, `TRADE_TIME`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '电商离线账户流水表';

-- 表说明：平台接口开通信息表
CREATE TABLE IF NOT EXISTS `tp_platform_interface_info` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `PLATFORM_CODE` VARCHAR(10) NOT NULL COMMENT '平台代码',
    `PLATFORM_NAME` VARCHAR(100) NOT NULL COMMENT '平台名称',
    `IS_ONLINE` VARCHAR(1) NOT NULL COMMENT '是否具备直连接口 Y-具备 N-不具备',
    `BAL_QUERY` VARCHAR(1) NOT NULL COMMENT '余额查询 Y-开通 N-未开通',
    `DTL_QUERY` VARCHAR(1) NOT NULL COMMENT '流水查询 Y-开通 N-未开通',
    `BOND_BAL_QUERY` VARCHAR(1) NOT NULL,
    `BOND_DTL_QUERY` VARCHAR(1) NOT NULL COMMENT '保证金流水查询 Y-开通 N-未开通',
    `NEED_AUTHORIZATION` VARCHAR(1) NOT NULL COMMENT '是否需要授权 Y-需要 N-不需要',
    `AUTHORIZATION_URL` VARCHAR(500) COMMENT '授权地址',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '平台接口开通信息表';

-- 表说明：商户号授权表
CREATE TABLE IF NOT EXISTS `tp_merchant_auth` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `MERCHANT_ID` VARCHAR(32) COMMENT '商户号id',
    `AUTH_STATUS` CHAR(1) COMMENT '授权状态',
    `VALID_PERIOD` DECIMAL(11,0) COMMENT '有效周期',
    `EXPIRATION_DATE` DATETIME(6) COMMENT '过期时间',
    `ACCESS_TOKEN` VARCHAR(500) COMMENT '访问令牌',
    `REFRESH_TOKEN` VARCHAR(500) COMMENT '刷新令牌',
    `CREATE_BY` VARCHAR(50) COMMENT '创建人',
    `CREATE_DATE` DATETIME(6) COMMENT '创建日期',
    `UPDATE_BY` VARCHAR(50) COMMENT '更新人',
    `UPDATE_DATE` DATETIME(6) COMMENT '更新日期',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '商户号授权表';

-- 表说明：平台参数信息表
CREATE TABLE IF NOT EXISTS `TP_PLATFORM_PARAM` (
    `ID` VARCHAR(32) NOT NULL COMMENT '主键',
    `PLATFORM_CODE` VARCHAR(10) NOT NULL COMMENT '平台代码',
    `PLATFORM_PARAM` VARCHAR(50) NOT NULL COMMENT '平台参数',
    `PLATFORM_PARAM_NAME` VARCHAR(50) NOT NULL COMMENT '平台参数名称',
    `PLATFORM_TYPE` CHAR(1) DEFAULT NULL COMMENT '平台类型',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '平台参数信息表';

-- 表说明：快手账务明细表
CREATE TABLE IF NOT EXISTS `TP_KUAISHOU_FUND_DTL` (
    `ID` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `APP_KEY` VARCHAR(64) COMMENT '应用key',
    `MCH_ID` VARCHAR(64) COMMENT '商户号',
    `AMOUNT` DECIMAL(15,2) COMMENT '记账金额',
    `BALANCE` DECIMAL(15,2) COMMENT '记账后余额',
    `IN_OUT_TYPE` DECIMAL(1,0) COMMENT '财务方向，1收，2支',
    `BOOK_TRANS_NO` VARCHAR(64) COMMENT '账务流水号',
    `BIZ_TYPE` VARCHAR(64) COMMENT '业务类型',
    `OUT_ORDER_ID` VARCHAR(64) COMMENT '业务订单号',
    `REMARK` VARCHAR(1000) COMMENT '描述',
    `ATTACH` VARCHAR(1000) COMMENT '备注',
    `BILL_TIME` DATETIME(6) COMMENT '交易时间',
    `BIZ_TYPE_DESC` VARCHAR(100) COMMENT '业务类型描述',
    `CREATED_TIME` DATETIME(6) COMMENT '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '快手账务明细表';

-- 表说明：快手余额表
CREATE TABLE IF NOT EXISTS `TP_KUAISHOU_BAL` (
    `ID` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `APP_KEY` VARCHAR(64) COMMENT '应用key',
    `MCH_ID` VARCHAR(64) COMMENT '商户号',
    `BAL_DATE` DATETIME(6) COMMENT '余额日期',
    `TOTAL_AMOUNT` DECIMAL(18,2) COMMENT '账户余额（元）',
    `AVAILABLE_AMOUNT` DECIMAL(18,2) COMMENT '账户可用余额（元）',
    `FREEZE_AMOUNT` DECIMAL(18,2) COMMENT '冻结金额（元）',
    `STATUS` VARCHAR(10) COMMENT '账户状态(ENABLE-启用，DISABLE-未启用，FROZE-冻结，UNBIND-已解绑)',
    `ACCOUNT_CHANNEL` VARCHAR(10) COMMENT '账户渠道(1-小店余额 2-微信 3-支付宝 4-安心钱包 5-保证金)',
    `SECUTITY_DEPOSIT_TYPE` DECIMAL(1,0) COMMENT '保证金类型(1-自建保证金，2-推广保证金，7-跨境保证金)',
    `DEPOSIT_LOW_BALANCE` DECIMAL(18,2) COMMENT '保证金阈值(元)',
    `IF_DEPOSIT_BALANCE_ENOUGH` DECIMAL(1,0) COMMENT '保证金余额是否充足(1-是，0-否)',
    `CREATED_TIME` DATETIME(6) COMMENT '创建时间',
    `UPDATED_TIME` DATETIME(6) COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '快手余额表';

-- 表说明：保证金账单任务表
CREATE TABLE IF NOT EXISTS `TP_BAIL_BILL_JOB` (
    `ID` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `MERCHANT_NO` VARCHAR(30) COMMENT 'appId/商户号',
    `PAY_TYPE` VARCHAR(10) COMMENT '支付类型',
    `BILL_DATE` VARCHAR(10) COMMENT '账单日期',
    `STATUS` DECIMAL(2,0) COMMENT '状态：-1未开始，0执行失败，1执行成功',
    `CREATE_TIME` DATETIME(6) COMMENT '创建日期',
    `CORP_ID` VARCHAR(32)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '保证金账单任务表';

-- 表说明：天猫支付宝商家账户保证金明细表
CREATE TABLE IF NOT EXISTS `TP_TMALL_BAIL_BILL` (
    `ID` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `APP_ID` VARCHAR(64) COMMENT '应用ID',
    `MCH_ID` VARCHAR(64) COMMENT '商户号',
    `TRANS_DT` DATETIME(6) COMMENT '业务发生时间',
    `TRANS_LOG_ID` VARCHAR(255) COMMENT '保证金业务流水号',
    `BAIL_TYPE` VARCHAR(255) COMMENT '保证金类型描述',
    `BAIL_AMOUNT` DECIMAL(15,2) COMMENT '保证金收支金额',
    `BAIL_BALANCE` DECIMAL(15,2) COMMENT '本次操作后的保证金余额',
    `BAIL_MEMO` VARCHAR(255) COMMENT '保证金说明',
    `BIZ_DESC` VARCHAR(255) COMMENT '业务描述',
    `BIZ_ORIG_NO` VARCHAR(255) COMMENT '业务基础订单号',
    `CREATED_TIME` DATETIME(6) COMMENT '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '天猫支付宝商家账户保证金明细表';

-- 表说明：支付宝余额表
CREATE TABLE IF NOT EXISTS `TP_TMALL_BAL` (
    `ID` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `MCH_ID` VARCHAR(64) COMMENT '商户ID',
    `APP_ID` VARCHAR(64) COMMENT '应用ID',
    `BAL_TYPE` VARCHAR(2) COMMENT '余额类型：1-支付宝账户余额',
    `BAL_DATE` DATETIME(6) COMMENT '余额日期',
    `TOTAL_AMOUNT` DECIMAL(18,2) COMMENT '支付宝账户余额（元）',
    `AVAILABLE_AMOUNT` DECIMAL(18,2) COMMENT '账户可用余额（元）',
    `FREEZE_AMOUNT` DECIMAL(18,2) COMMENT '冻结金额（元）',
    `BEGINNING_AMOUNT` DECIMAL(15,2) COMMENT '期初余额',
    `SETTLE_AMOUNT` DECIMAL(15,2) COMMENT '待结算余额（元）',
    `CREATED_TIME` DATETIME(6) COMMENT '创建时间',
    `UPDATED_TIME` DATETIME(6) COMMENT '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '支付宝余额表';

-- 表说明：天猫资金账单明细表
CREATE TABLE IF NOT EXISTS `TP_TMALL_FUND_DTL` (
    `ID` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `APP_ID` VARCHAR(64) COMMENT '应用ID',
    `ACCOUNT_ID` VARCHAR(64) COMMENT '账务流水号',
    `BUSINESS_ID` VARCHAR(64) COMMENT '业务流水号',
    `TRADE_NO` VARCHAR(64) COMMENT '商户订单号',
    `TRADE_TYPE` VARCHAR(20) COMMENT '账务类型',
    `GOODS_NAME` VARCHAR(100) COMMENT '商品名称',
    `HAPPEN_TIME` DATETIME(6) COMMENT '发生时间',
    `PAY_ACC` VARCHAR(100) COMMENT '对方账号',
    `PAYEE_MONEY` DECIMAL(15,2) COMMENT '收入金额（+元）',
    `PAYER_MONEY` DECIMAL(15,2) COMMENT '支出金额（-元）',
    `BALANCE` DECIMAL(15,2) COMMENT '账户余额（元）',
    `TRANS_CHANNEL` VARCHAR(100) COMMENT '交易渠道',
    `REMARK` VARCHAR(1000) COMMENT '备注',
    `FBS_STATUS` VARCHAR(1) COMMENT '资金计划挂接状态 0未确认 1已确认 2已删除',
    `BUS_TYPE` VARCHAR(32) COMMENT '业务类别id',
    `MCH_ID` VARCHAR(64) COMMENT '商户号',
    `SHOP_NAME` VARCHAR(200) COMMENT '门店名称'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '天猫资金账单明细表';

-- 表说明：抖音账单明细表
CREATE TABLE IF NOT EXISTS `TP_DOUYIN_DTL` (
    `id` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `mch_id` VARCHAR(64) COMMENT '商户号 ',
    `app_key` VARCHAR(64) COMMENT '应用ID ',
    `app_secret` VARCHAR(64) COMMENT '应用密钥 ',
    `shop_id` VARCHAR(64) COMMENT '店铺id',
    `bill_time` DATETIME(6) COMMENT '动账时间',
    `account_trade_no` VARCHAR(64) COMMENT '动账流水号(唯一)',
    `fund_flow` VARCHAR(2) COMMENT '动账方向 0:入账 1:出账',
    `fund_flow_desc` VARCHAR(150) COMMENT '动账方向描述 出账、入账 ',
    `account_amount` DECIMAL(15,2) COMMENT '动账金额(分)',
    `account_type` VARCHAR(2) COMMENT '动账账户 1: 微信 2:支付宝 3:合众支付 4:聚合支付',
    `account_type_desc` VARCHAR(100) COMMENT '动账账户',
    `account_bill_desc` VARCHAR(100) COMMENT '动账摘要',
    `account_bill_desc_tag` VARCHAR(2) COMMENT '动账摘要 0：其他 1：达人带货佣金 2：达人佣金退款 3：订单结算 4：部分结算 5：运费单结算 6：服务费返还 7：平台补贴扣回 8：退款失败退票 9：结算重复扣款调账 10：保证金退款 11：提现 12：提现退票 13：极速退款分账 14：小额打款(原因:补差价) 15：小额打款(原因:其他) 16：小额打款(原因:商品补偿) 17：小额打款(原因:运费补偿) 18：小额打款退票 19：已退款 20：保费扣除（x笔保单扣费，明细详见保费扣除明细） 21：支付BIC服务费 22：BIC服务费退票 23: 原路退 ',
    `biz_type` VARCHAR(2) COMMENT '计费类型 0:全部 1:鲁班广告 2:精选联盟 3:值点商城 4:小店自卖 5:橙子建站 6:POI 7:抖+ 8:穿山甲 9:服务市场 10:服务市场外包客服 11:学浪',
    `biz_type_desc` VARCHAR(150) COMMENT '计费类型描述',
    `shop_order_id` VARCHAR(64) COMMENT '店铺单号',
    `order_id` VARCHAR(64) COMMENT '订单号',
    `after_sale_service_no` VARCHAR(64) COMMENT '【商家无需使用】售后编号非售后接口的售后单id；该字段为账单售后编号id；',
    `business_order_create_time` DATETIME(6) COMMENT '下单时间',
    `product_id` VARCHAR(64) COMMENT '商品ID',
    `order_type` VARCHAR(2) COMMENT '订单类型 0：普通订单 1：定金(已退款) 2：定金(尾款已支付) 3：定金(尾款未支付) 4：尾款(尾款已支付) 5：尾款(已退款)',
    `order_type_desc` VARCHAR(100) COMMENT '订单类型描述',
    `pay_amount` DECIMAL(15,2) COMMENT '订单实付应结，单位：分；只包含用户实际支付的金额，不含各类补贴（如：平台补贴、达人补贴、支付补贴等）',
    `post_amount` DECIMAL(15,2) COMMENT '运费(分)',
    `promotion_amount` DECIMAL(15,2) COMMENT '实际平台补贴(分)',
    `author_coupon_subsidy` DECIMAL(15,2) COMMENT '实际达人补贴(分)',
    `actual_zt_pay_promotion` DECIMAL(15,2) COMMENT '实际抖音支付补贴(分)',
    `actual_zr_pay_promotion` DECIMAL(15,2) COMMENT '实际DOU分期营销补贴(分)',
    `refund_amount` DECIMAL(15,2) COMMENT '订单退款(分)',
    `platform_service_fee` DECIMAL(15,2) COMMENT '平台服务费(分)',
    `commission` DECIMAL(15,2) COMMENT '佣金(分)',
    `channel_fee` DECIMAL(15,2) COMMENT '渠道分成(分)',
    `colonel_service_fee` DECIMAL(15,2) COMMENT '招商服务费(分)',
    `channel_promotion_fee` DECIMAL(15,2) COMMENT '直播间站外推广(分)',
    `other_sharing_amount` DECIMAL(15,2) COMMENT '其他分成金额(分)',
    `trans_scene_tag` VARCHAR(2) COMMENT '动账场景 枚举值对应含义请参考文档：https://bytedance.larkoffice.com/docx/FefRdbxoPoSUBYx4YHhc5a5rnrh',
    `trans_scene` VARCHAR(150) COMMENT '动账场景',
    `remark` VARCHAR(150) COMMENT '备注',
    `free_commission_flag` VARCHAR(10) COMMENT '是否免佣,枚举值为"是/否"或为空',
    `real_free_commission_amount` DECIMAL(15,2) COMMENT '免佣金额(分)',
    `recycler_amount` DECIMAL(15,2) COMMENT '以旧换新抵扣(分) ',
    `bank_pay_promotion_amount` DECIMAL(15,2) COMMENT '银行补贴(分) ',
    `post_promotion_amount` DECIMAL(15,2) COMMENT '平台补贴运费(分) ',
    `partner_commission` DECIMAL(15,2) COMMENT '服务商佣金(分) ',
    `gov_promotion_amount` DECIMAL(15,2) COMMENT '政府补贴平台垫资(分) ',
    `product_name` VARCHAR(150) COMMENT '商品名称 ',
    `author_id` VARCHAR(64) COMMENT '达人id ',
    `author_name` VARCHAR(150) COMMENT '达人名称 ',
    `other_pla_promo_amt` DECIMAL(15,2) COMMENT '其他平台补贴(分) '
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '抖音账单明细表';

-- 表说明：抖音账单查询跟踪器表
CREATE TABLE IF NOT EXISTS `TP_DOUYIN_QUERY_TRACKER` (
    `id` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `mch_id` VARCHAR(64) COMMENT '商户号 ',
    `pay_type` VARCHAR(10) COMMENT '交易渠道 ',
    `bill_date` VARCHAR(10) COMMENT '账单日期 ',
    `next_start_index` VARCHAR(64) COMMENT '下次查询开始下标',
    `next_start_time` DATETIME(6) COMMENT '下次查询开始时间',
    `create_time` DATETIME(6) COMMENT '创建时间',
    `update_time` DATETIME(6) COMMENT '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '抖音账单查询跟踪器表';

-- 表说明：拼多多对账单表
CREATE TABLE IF NOT EXISTS `TP_PINDUODUO_DTL` (
    `id` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `client_id` VARCHAR(60) NOT NULL COMMENT 'clientId',
    `merchant_order_no` VARCHAR(64) COMMENT '商户订单号',
    `occur_time` DATETIME(6) COMMENT '发生时间',
    `income_amount` DECIMAL(20,2) COMMENT '收入金额',
    `expense_amount` DECIMAL(20,2) COMMENT '支出金额',
    `account_type` VARCHAR(200) COMMENT '财务类型',
    `remark` VARCHAR(2000) COMMENT '备注',
    `bus_desc` VARCHAR(2000) COMMENT '业务描述',
    `create_time` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    `update_time` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '对账单更新时间',
    `merchant_no` VARCHAR(64) COMMENT '商户号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '拼多多对账单表';

-- 表说明：小红书账单明细表
CREATE TABLE IF NOT EXISTS `TP_XIAOHONGSHU_DTL` (
    `id` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT ' 主键 ID',
    `mchId` VARCHAR(64) COMMENT ' 商户号 ',
    `appKey` VARCHAR(64) COMMENT ' 应用ID ',
    `appSecret` VARCHAR(64) COMMENT ' 应用密钥 ',
    `accountId` VARCHAR(64) COMMENT ' 账户唯一id ',
    `sellerId` VARCHAR(64) COMMENT ' 店铺id ',
    `tradeNo` VARCHAR(64) COMMENT ' 动账流水号 ',
    `incomeAmount` DECIMAL(15,2) COMMENT ' 收入（元） ',
    `outcomeAmount` DECIMAL(15,2) COMMENT ' 支出（元） ',
    `balanceAmount` DECIMAL(15,2) COMMENT ' 账户余额（元） ',
    `businessNo` VARCHAR(64) COMMENT ' 业务单号 ',
    `remark` VARCHAR(150) COMMENT ' 备注 ',
    `type` VARCHAR(20) COMMENT ' 交易类型 ',
    `typeDesc` VARCHAR(150) COMMENT ' 交易类型中文描述 ',
    `createdTime` DATETIME(6) COMMENT ' 创建时间 ',
    `businessType` VARCHAR(20) COMMENT ' 动账业务子类型 ',
    `balanceBefore` DECIMAL(15,2) COMMENT ' 交易前金额（元） ',
    `fundType` VARCHAR(20) COMMENT ' 账户类型：NOT_ER_QING(店铺余额)ER_QING_WECHAT(微信)ER_QING_ALIPAY(支付宝) ',
    `disableBusinessNoLink` VARCHAR(2) COMMENT ' 是否屏蔽业务跳转 '
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '小红书账单明细表';

-- 表说明：得物对账单表
CREATE TABLE IF NOT EXISTS `TP_DEWU_DTL` (
    `id` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `app_key` VARCHAR(60) NOT NULL COMMENT '应用标识',
    `bill_no` VARCHAR(64) COMMENT '账单编号',
    `bill_start_time` DATETIME(6) COMMENT '账单开始时间',
    `bill_end_time` DATETIME(6) COMMENT '账单结束时间',
    `order_receive_amount` DECIMAL(20,2) COMMENT '应收订单金额（分）',
    `platform_service_fee` DECIMAL(20,2) COMMENT '应收平台服务费(分',
    `advance_fee` DECIMAL(20,2) COMMENT '预付款金额（分）',
    `seller_subsidies_amount` DECIMAL(20,2) COMMENT '卖家补贴金额（分）',
    `platform_difference_amount` DECIMAL(20,2) COMMENT '平台差错调整（分）',
    `refund_amount` DECIMAL(20,2) COMMENT '退款金额（分）',
    `settle_amount` DECIMAL(20,2) COMMENT '应结金额（分',
    `status` VARCHAR(20) COMMENT '结算状态',
    `create_time` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    `update_time` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '对账单更新时间',
    `merchant_no` VARCHAR(64) COMMENT '商户号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '得物对账单表';

-- 表说明：微信小店余额表
CREATE TABLE IF NOT EXISTS `TP_WEIXINSTORE_BAL` (
    `ID` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID，使用MD5生成的唯一标识',
    `APP_ID` VARCHAR(32) NOT NULL COMMENT '应用ID，微信小店的appid，用于标识不同的微信小店应用',
    `GRANT_TYPE` VARCHAR(32) NOT NULL COMMENT '授权类型，通常为client_credential，用于获取微信API的访问令牌',
    `MCH_ID` VARCHAR(64) COMMENT '商户号，用于标识不同的商户',
    `BAL_DATE` DATETIME(6) NOT NULL COMMENT '余额查询的日期时间，记录查询余额的具体时间点',
    `AVAILABLE_AMOUNT` DECIMAL(18,2) DEFAULT 0.00 COMMENT '可用余额，单位为元，可做提现操作的金额',
    `PENDING_AMOUNT` DECIMAL(18,2) DEFAULT 0.00 COMMENT '待结算余额，单位为元，暂不可提现的金额',
    `SUB_MCHID` VARCHAR(32) COMMENT '二级商户号，用于服务商模式下标识子商户',
    `CREATE_TIME` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间，系统自动生成'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '微信小店余额表';

-- 表说明：微信小店资金流水明细表
CREATE TABLE IF NOT EXISTS `TP_WEIXINSTORE_FUND_BILL` (
    `ID` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID，使用流水ID和其他信息的MD5生成的唯一标识',
    `APP_ID` VARCHAR(32) NOT NULL COMMENT '应用ID，微信小店的appid，用于标识不同的微信小店应用',
    `GRANT_TYPE` VARCHAR(32) NOT NULL COMMENT '授权类型，通常为client_credential，用于获取微信API的访问令牌',
    `MCH_ID` VARCHAR(64) COMMENT '商户号，用于标识不同的商户',
    `FLOW_ID` VARCHAR(64) NOT NULL COMMENT '微信小店资金流水的唯一标识符，由微信平台生成',
    `AMOUNT` DECIMAL(18,2) DEFAULT 0.00 COMMENT '流水金额，单位为元，正数表示收入，负数表示支出',
    `BALANCE` DECIMAL(18,2) DEFAULT 0.00 COMMENT '账户余额，单位为元，该笔流水发生后的账户余额',
    `FLOW_TYPE` VARCHAR(2) COMMENT '流水类型，1表示收入，2表示支出',
    `FUNDS_TYPE` VARCHAR(32) COMMENT '资金类型，枚举值：1:订单支付收入，2:订单手续费，3:退款，4:提现，5:提现失败退票，10:联盟抽佣，11:平台抽佣，12:团长抽佣，13:返佣人气卡，14:极速退款垫资金，15:极速退款垫资回补，16:运费险',
    `BOOKKEEPING_TIME` DATETIME(6) COMMENT '记账时间，微信平台记录的资金流水发生时间',
    `REMARK` VARCHAR(255) COMMENT '备注信息，对该笔资金流水的说明',
    `CREATE_TIME` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间，系统自动生成'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '微信小店资金流水明细表';

-- 表说明：微信小店资金流水关联信息表
CREATE TABLE IF NOT EXISTS `TP_WEIXINSTORE_RELATED_INFO` (
    `ID` VARCHAR(32) NOT NULL PRIMARY KEY COMMENT '主键ID，使用流水ID和关联信息的MD5生成的唯一标识',
    `APP_ID` VARCHAR(32) NOT NULL COMMENT '应用ID，微信小店的appid，用于标识不同的微信小店应用',
    `GRANT_TYPE` VARCHAR(32) NOT NULL COMMENT '授权类型，通常为client_credential，用于获取微信API的访问令牌',
    `BILL_ID` VARCHAR(32) NOT NULL COMMENT '关联的资金流水表主键ID，与TP_WEIXINSTORE_FUND_BILL表的ID字段关联',
    `RELATED_TYPE` VARCHAR(32) COMMENT '关联类型, 1 订单， 2售后，3 提现，4 运费险， 5 服务保障， 6 送礼物, 7 群送礼关联订单号列表',
    `ORDER_ID` VARCHAR(64) COMMENT '订单ID，当关联类型为ORDER时有值',
    `AFTERSALE_ID` VARCHAR(64) COMMENT '售后单ID，当关联类型为AFTERSALE时有值',
    `WITHDRAW_ID` VARCHAR(64) COMMENT '提现单ID，当关联类型为WITHDRAW时有值',
    `TRANSACTION_ID` VARCHAR(64) COMMENT '交易ID，微信支付平台的交易流水号',
    `GUARANTEE_ID` VARCHAR(64) COMMENT '担保单ID，当涉及担保交易时有值',
    `PRESENT_ID` VARCHAR(64) COMMENT '赠送单ID，当涉及赠送交易时有值',
    `GROUP_PRESENT_LIST` VARCHAR(640) COMMENT '群送礼关联订单号列表（群礼物与订单之间是一对多关系，因此多个订单号通过","来分隔）',
    `CREATE_TIME` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间，系统自动生成'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT = '微信小店资金流水关联信息表';

-- END GENERATED MYSQL FUNDWARN SCHEMA