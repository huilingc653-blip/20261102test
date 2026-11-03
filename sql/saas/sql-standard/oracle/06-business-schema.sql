-- ============================================================================
-- SaaS 标准化初始化脚本
-- 数据库：Oracle
-- 阶段：06 业务模块结构
-- 来源：bytter-service-foreign_ddl.sql + scf-table-init-oracle.sql/DDL 段 + 限额/担保账户/资金预警（手工固化）
-- 生成日期：2026-07-29
-- 注意：本文件由原始脚本整理生成；禁止反向覆盖 sql/saas/oracle。
-- ============================================================================

-- 表说明：FEX_COTP_BANKACC_CUR
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE FEX_COTP_BANKACC_CUR';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE FEX_COTP_BANKACC_CUR
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    BANK_ACC_ID VARCHAR2(32) NULL,
    CUR_ID VARCHAR2(32) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE FEX_COTP_BANKACC_CUR IS 'FEX_COTP_BANKACC_CUR';
COMMENT ON COLUMN FEX_COTP_BANKACC_CUR.TENANT_ID IS '租户ID';
COMMENT ON COLUMN FEX_COTP_BANKACC_CUR.ID IS '主键';
COMMENT ON COLUMN FEX_COTP_BANKACC_CUR.BANK_ACC_ID IS '交易对手账号ID';
COMMENT ON COLUMN FEX_COTP_BANKACC_CUR.CUR_ID IS '币别ID';

-- 租户交易自定义信息表


-- 表说明：租户交易自定义信息表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE FEX_CUST_INFO_TENANT';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE FEX_CUST_INFO_TENANT
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    BUS_ID VARCHAR2(32) NULL,
    FIELD_ID VARCHAR2(32) NULL,
    FIELD_VAL VARCHAR2(200) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE FEX_CUST_INFO_TENANT IS '租户交易自定义信息表';
COMMENT ON COLUMN FEX_CUST_INFO_TENANT.ID IS '主键';
COMMENT ON COLUMN FEX_CUST_INFO_TENANT.BUS_ID IS '业务主表ID';
COMMENT ON COLUMN FEX_CUST_INFO_TENANT.FIELD_ID IS '自定义字段规则ID';
COMMENT ON COLUMN FEX_CUST_INFO_TENANT.FIELD_VAL IS '字段值';
COMMENT ON COLUMN FEX_CUST_INFO_TENANT.TENANT_ID IS '租户ID';
CREATE INDEX IDX_FEX_CUST_INFO_TENANT ON FEX_CUST_INFO_TENANT (TENANT_ID);

-- 租户自定义字段规则表


-- 表说明：租户自定义字段规则表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE FEX_CUST_FIELD_RULE_TENANT';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE FEX_CUST_FIELD_RULE_TENANT
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    FIELD_CODE VARCHAR2(30) NULL,
    FIELD_NAME VARCHAR2(60) NULL,
    FIELD_DESC VARCHAR2(100) NULL,
    FIELD_TYPE VARCHAR2(1) NULL,
    APP_RANGE_1 VARCHAR2(1) NULL,
    APP_RANGE_2 VARCHAR2(32) NULL,
    CONTROL_RULE VARCHAR2(30) NULL,
    PRESET_VAL VARCHAR2(500) NULL,
    DECIMAL_DIGIT NUMBER(10,0) NULL,
    MIN_VAL NUMBER(15,6) NULL,
    MAXT_VAL NUMBER(15,6) NULL,
    CUR_ID VARCHAR2(32) NULL,
    VALID_SIGN VARCHAR2(1) DEFAULT '1' NULL,
    CREATE_DATE DATE NULL,
    CREATE_BY VARCHAR2(32) NULL,
    UPDATE_DATE DATE NULL,
    UPDATE_BY VARCHAR2(32) NULL,
    DICT_GROUP VARCHAR2(200) NULL,
    LABEL_BANK VARCHAR2(255) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE FEX_CUST_FIELD_RULE_TENANT IS '租户自定义字段规则表';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.ID IS '主键';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.FIELD_CODE IS '字段编码';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.FIELD_NAME IS '字段名称';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.FIELD_DESC IS '字段描述';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.FIELD_TYPE IS '字段类型';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.APP_RANGE_1 IS '应用范围1';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.APP_RANGE_2 IS '应用范围2';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.CONTROL_RULE IS '录入控制规则';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.PRESET_VAL IS '预设值';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.DECIMAL_DIGIT IS '小数位数';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.MIN_VAL IS '最小值';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.MAXT_VAL IS '最大值';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.CUR_ID IS '币种ID';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.VALID_SIGN IS '有效标志';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.CREATE_DATE IS '创建时间';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.CREATE_BY IS '创建人';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.UPDATE_DATE IS '更新时间';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.UPDATE_BY IS '更新人';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.DICT_GROUP IS '字典组代码';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.LABEL_BANK IS '银行标签';
COMMENT ON COLUMN FEX_CUST_FIELD_RULE_TENANT.TENANT_ID IS '租户ID';
CREATE INDEX IDX_FEX_CUST_FIELD_RULE_T_T ON FEX_CUST_FIELD_RULE_TENANT (TENANT_ID);


-- 表说明：银行类型自定义配置项
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE bt_bank_type_custom';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE bt_bank_type_custom
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    BANK_TYPE_ID VARCHAR2(32) NULL,
    BIF_CODE VARCHAR2(32) NULL,
    COUNTRY_ID VARCHAR2(255) NULL,
    BANK_PAY_TYPE VARCHAR2(255) NULL,
    CUSTOM_CODE VARCHAR2(255) NULL,
    CUSTOM_NAME VARCHAR2(255) NULL,
    CUSTOM_TYPE VARCHAR2(50) NULL,
    IS_DELETED VARCHAR2(1) DEFAULT '0' NULL
);

COMMENT ON TABLE bt_bank_type_custom IS '银行类型自定义配置项';
COMMENT ON COLUMN bt_bank_type_custom.ID IS '主键';
COMMENT ON COLUMN bt_bank_type_custom.BANK_TYPE_ID IS '银行类型ID';
COMMENT ON COLUMN bt_bank_type_custom.BIF_CODE IS '接口代码ID';
COMMENT ON COLUMN bt_bank_type_custom.COUNTRY_ID IS '适用国家';
COMMENT ON COLUMN bt_bank_type_custom.BANK_PAY_TYPE IS '支付类型';
COMMENT ON COLUMN bt_bank_type_custom.CUSTOM_CODE IS '自定义上送代码';
COMMENT ON COLUMN bt_bank_type_custom.CUSTOM_NAME IS '自定义名称';
COMMENT ON COLUMN bt_bank_type_custom.CUSTOM_TYPE IS '自定义类型';
COMMENT ON COLUMN bt_bank_type_custom.IS_DELETED IS '是否删除';


-- 表说明：FexProfitLossManage
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE FEX_PROFIT_LOSS_MANAGE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE FEX_PROFIT_LOSS_MANAGE
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    TRAN_TYPE VARCHAR2(1) NULL,
    NET_REAL_MONEY_TOTAL NUMBER(19,2) NULL,
    NET_CONVERT_REAL_MONEY_TOTAL NUMBER(19,2) NULL,
    FLOAT_MONEY_TOTAL NUMBER(19,2) NULL,
    CONVERT_FLOAT_MONEY_TOTAL NUMBER(19,2) NULL,
    QUOTATION_CUR_ID VARCHAR2(255) NULL,
    QUOTATION_REFER_CUR_ID VARCHAR2(255) NULL,
    CONVERT_CUR_ID VARCHAR2(32) NULL,
    CONVERT_CUR_REFER_ID VARCHAR2(255) NULL,
    QUOTATION_METHOD VARCHAR2(255) NULL,
    CONVERT_CUR_EN_CODE VARCHAR2(255) NULL,
    CONVERT_DATE DATE NULL,
    REFERENCE_INTEREST_RATE NUMBER(19,2) NULL,
    REQ_ID VARCHAR2(32) NULL,
    CUR_ID VARCHAR2(32) NULL,
    CORP_ID VARCHAR2(32) NULL,
    LIFE_CYCLE VARCHAR2(255) NULL,
    QUOTATION_DATE DATE NULL,
    SETTLE_DATE DATE NULL,
    CONTRACT_RATE NUMBER(14,6) NULL,
    VALUATION_RATE NUMBER(19,2) NULL,
    CONVERT_RATE NUMBER(8,6) NULL,
    FLOAT_MONEY NUMBER(19,2) NULL,
    CONVERT_FLOAT_MONEY NUMBER(19,2) NULL,
    REAL_MONEY_TOTAL NUMBER(19,2) NULL,
    CONVERT_REAL_MONEY_TOTAL NUMBER(19,2) NULL,
    STATUS NUMBER(10,0) NULL,
    CREATE_BY VARCHAR2(32) NULL,
    CREATE_DATE DATE NULL,
    UPDATE_BY VARCHAR2(32) NULL,
    UPDATE_DATE DATE NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE FEX_PROFIT_LOSS_MANAGE IS 'FexProfitLossManage';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.TENANT_ID IS '租户ID';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.ID IS '主键';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.TRAN_TYPE IS '交易类型';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.NET_REAL_MONEY_TOTAL IS '已实现净损益合计';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.NET_CONVERT_REAL_MONEY_TOTAL IS '折本位币已实现净损益合计';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.FLOAT_MONEY_TOTAL IS '浮动净损益';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CONVERT_FLOAT_MONEY_TOTAL IS '折本位币浮动净损益';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.QUOTATION_CUR_ID IS '报价方式标准币种';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.QUOTATION_REFER_CUR_ID IS '报价方式标准币种';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CONVERT_CUR_ID IS '折本位币标准币种';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CONVERT_CUR_REFER_ID IS '折本位币报价币种';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.QUOTATION_METHOD IS '报价方式';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CONVERT_CUR_EN_CODE IS '折本位币报价方式';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CONVERT_DATE IS '折本位币报价时间';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.REFERENCE_INTEREST_RATE IS '参考利率';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.REQ_ID IS '交易表id';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CUR_ID IS '损益币种id';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CORP_ID IS '单位id';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.LIFE_CYCLE IS '生命周期id';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.QUOTATION_DATE IS '报价日期';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.SETTLE_DATE IS '结算日期';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CONTRACT_RATE IS '合同汇率';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.VALUATION_RATE IS '估值利率';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CONVERT_RATE IS '折本位币汇率';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.FLOAT_MONEY IS '浮动损益';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CONVERT_FLOAT_MONEY IS '折本位币浮动损益';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.REAL_MONEY_TOTAL IS '已实现损益';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CONVERT_REAL_MONEY_TOTAL IS '折本位币已实现损益';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.STATUS IS '状态';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CREATE_BY IS '创建人';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.CREATE_DATE IS '创建日期';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.UPDATE_BY IS '修改人';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE.UPDATE_DATE IS '创建日期';


-- 表说明：FexProfitLossManageDtl
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE FEX_PROFIT_LOSS_MANAGE_DTL';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE FEX_PROFIT_LOSS_MANAGE_DTL
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    LIFE_CYCLE_BILL_NO VARCHAR2(255) NULL,
    REQ_ID VARCHAR2(32) NULL,
    FEX_ID VARCHAR2(255) NULL,
    LIFE_CYCLE_ID VARCHAR2(255) NULL,
    OP_TYPE CHAR(1) NULL,
    ORDER_NO VARCHAR2(32) NULL,
    DELIVERLY_DATE DATE NULL,
    REFER_RATE NUMBER(12,8) NULL,
    CONTRACT_RATE NUMBER(14,6) NULL,
    MONEY NUMBER(15,2) NULL,
    CONVERT_MONEY NUMBER(19,2) NULL,
    CREATE_BY VARCHAR2(32) NULL,
    CREATE_DATE DATE NULL,
    UPDATE_BY VARCHAR2(32) NULL,
    UPDATE_DATE DATE NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE FEX_PROFIT_LOSS_MANAGE_DTL IS 'FexProfitLossManageDtl';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.TENANT_ID IS '租户ID';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.ID IS '主键';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.LIFE_CYCLE_BILL_NO IS '生命周期id';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.REQ_ID IS '交易表id';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.FEX_ID IS '损益id';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.LIFE_CYCLE_ID IS '生命周期id';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.OP_TYPE IS '操作类型';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.ORDER_NO IS '序号';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.DELIVERLY_DATE IS '交割日期、操作日';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.REFER_RATE IS '参考汇率';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.CONTRACT_RATE IS '合同汇率';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.MONEY IS '浮动损益（已实现损益）';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.CONVERT_MONEY IS '折本位币浮动损益（已实现损益）';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.CREATE_BY IS '创建人';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.CREATE_DATE IS '浮动损益';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.UPDATE_BY IS '折本位币浮动损益';
COMMENT ON COLUMN FEX_PROFIT_LOSS_MANAGE_DTL.UPDATE_DATE IS '已实现损益';


-- 表说明：FexTranConfigOperationLog
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE fex_tran_config_operation_log';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE fex_tran_config_operation_log
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    create_date DATE NULL,
    operation_page VARCHAR2(255) NULL,
    operation_content VARCHAR2(255) NULL,
    before_data CLOB NULL,
    after_data CLOB NULL,
    create_by VARCHAR2(32) NULL,
    create_name VARCHAR2(50) NULL,
    is_delete CHAR(1) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE fex_tran_config_operation_log IS 'FexTranConfigOperationLog';
COMMENT ON COLUMN fex_tran_config_operation_log.TENANT_ID IS '租户ID';
COMMENT ON COLUMN fex_tran_config_operation_log.ID IS '主键';
COMMENT ON COLUMN fex_tran_config_operation_log.create_date IS '创建时间';
COMMENT ON COLUMN fex_tran_config_operation_log.operation_page IS '操作页面  0:交易页面配置  1:交易指令配置';
COMMENT ON COLUMN fex_tran_config_operation_log.operation_content IS '操作内容';
COMMENT ON COLUMN fex_tran_config_operation_log.before_data IS '修改前数据';
COMMENT ON COLUMN fex_tran_config_operation_log.after_data IS '修改后数据';
COMMENT ON COLUMN fex_tran_config_operation_log.create_by IS '创建人ID';
COMMENT ON COLUMN fex_tran_config_operation_log.create_name IS '创建人名称';
COMMENT ON COLUMN fex_tran_config_operation_log.is_delete IS '是否删除 0否 1是';


-- 表说明：生命周期表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE FEX_TRAN_LIFE_CYCLE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE FEX_TRAN_LIFE_CYCLE
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    REQ_ID VARCHAR2(32) NULL,
    CORP_ID VARCHAR2(32) NULL,
    BILL_NO VARCHAR2(32) NULL,
    OP_TYPE CHAR(1) NULL,
    OP_DATE DATE NULL,
    EXPIRES_DATE DATE NULL,
    SETTLE_DATE DATE NULL,
    CONTRACT_RATE NUMBER(14,6) NULL,
    SPOT_RATE NUMBER(14,6) NULL,
    PROCESS_RATE NUMBER(19,8) NULL,
    TRAN_SETTLE_METHOD VARCHAR2(1) NULL,
    IS_PAYMENT_AMOUNT VARCHAR2(10) NULL,
    BUY_CUR_ID VARCHAR2(32) NULL,
    SELL_CUR_ID VARCHAR2(32) NULL,
    OP_MONEY NUMBER(15,2) NULL,
    BUY_MONEY NUMBER(15,2) NULL,
    SELL_MONEY NUMBER(15,2) NULL,
    BUY_BAL NUMBER(19,6) NULL,
    SELL_BAL NUMBER(19,6) NULL,
    MONEY NUMBER(15,2) NULL,
    PROFIT_LOSS_CUR VARCHAR2(32) NULL,
    PROFIT_LOSS_MONEY NUMBER(19,6) NULL,
    RECORD_RATE NUMBER(19,8) NULL,
    BANK_ACC VARCHAR2(50) NULL,
    CUR_ID VARCHAR2(32) NULL,
    EARLY_RATE NUMBER(19,8) NULL,
    EARLY_TYPE VARCHAR2(10) NULL,
    AVG_RATE NUMBER(14,6) NULL,
    DEFERRED_RATE NUMBER(8,6) NULL,
    DEFERRED_TYPE VARCHAR2(10) NULL,
    CLOSE_SETTLE_DATE DATE NULL,
    CLOSE_RATE NUMBER(19,8) NULL,
    CLOSE_FORWARD_RATE NUMBER(19,8) NULL,
    CONVERT_CUR_TYPE VARCHAR2(10) NULL,
    CONVERT_RATE NUMBER(8,6) NULL,
    CONVERT_LOSS_MONEY NUMBER(19,6) NULL,
    PAYEE_ACC VARCHAR2(50) NULL,
    PAYER_ACC VARCHAR2(50) NULL,
    EARLY_SETTLEMENT_TYPE VARCHAR2(10) NULL,
    EARLY_SETTLEMENT_RATE NUMBER(19,8) NULL,
    CLOSE_LOSS_MONEY NUMBER(19,6) NULL,
    DELIVERY_LOSS_MONEY NUMBER(19,6) NULL,
    ALL_LOSS_MONEY NUMBER(19,6) NULL,
    TRAN_ADDR_ID VARCHAR2(64) NULL,
    TRAN_ADDR VARCHAR2(100) NULL,
    RMK VARCHAR2(200) NULL,
    CREATE_BY VARCHAR2(32) NULL,
    CREATE_DATE DATE NULL,
    UPDATE_BY VARCHAR2(32) NULL,
    UPDATE_DATE DATE NULL,
    LIFE_STATUS VARCHAR2(50) NULL,
    REAL_BUY_MONEY NUMBER(19,6) NULL,
    REAL_SELL_MONEY NUMBER(19,6) NULL,
    POSITION_MONEY NUMBER(19,2) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE FEX_TRAN_LIFE_CYCLE IS '生命周期表';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.TENANT_ID IS '租户ID';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.ID IS '主键';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.REQ_ID IS '交易信息ID';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CORP_ID IS '单位';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.BILL_NO IS '编号';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.OP_TYPE IS '操作类型';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.OP_DATE IS '操作日期';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.EXPIRES_DATE IS '到期日期';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.SETTLE_DATE IS '结算日期';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CONTRACT_RATE IS '合同汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.SPOT_RATE IS '即期汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.PROCESS_RATE IS '执行汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.TRAN_SETTLE_METHOD IS '结算方式（更名为交割方式：差额、全额）';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.IS_PAYMENT_AMOUNT IS '金额是否自动计算';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.BUY_CUR_ID IS '买入币种';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.SELL_CUR_ID IS '卖出币种';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.OP_MONEY IS '操作金额';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.BUY_MONEY IS '买入金额';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.SELL_MONEY IS '卖出金额';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.BUY_BAL IS '买入余额';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.SELL_BAL IS '卖出余额';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.MONEY IS '损益、结算金额';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.PROFIT_LOSS_CUR IS '损益币种';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.PROFIT_LOSS_MONEY IS '净损益';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.RECORD_RATE IS '操作日记账汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.BANK_ACC IS '结算账号';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CUR_ID IS '结算币种';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.EARLY_RATE IS '提前汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.EARLY_TYPE IS '提前方式';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.AVG_RATE IS '平均汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.DEFERRED_RATE IS '展期汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.DEFERRED_TYPE IS '展期方式';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CLOSE_SETTLE_DATE IS '平仓结算日期';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CLOSE_RATE IS '平仓汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CLOSE_FORWARD_RATE IS '平仓远期汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CONVERT_CUR_TYPE IS '折本位币报价方式';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CONVERT_RATE IS '折本位币汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CONVERT_LOSS_MONEY IS '折本位币净损益';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.PAYEE_ACC IS '收款账号';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.PAYER_ACC IS '付款账号';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.EARLY_SETTLEMENT_TYPE IS '提前交割方式';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.EARLY_SETTLEMENT_RATE IS '提前交割汇率';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CLOSE_LOSS_MONEY IS '平仓损益';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.DELIVERY_LOSS_MONEY IS '交割损益';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.ALL_LOSS_MONEY IS '总损益';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.TRAN_ADDR_ID IS '交易地点ID';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.TRAN_ADDR IS '交易地点';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.RMK IS '备注';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CREATE_BY IS '创建人';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.CREATE_DATE IS '创建日期';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.UPDATE_BY IS '修改人';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.UPDATE_DATE IS '修改日期';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.LIFE_STATUS IS '状态';
COMMENT ON COLUMN FEX_TRAN_LIFE_CYCLE.POSITION_MONEY IS '持仓金额';


-- 表说明：外汇交易模板配置版本表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE fex_tran_req_config_version';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE fex_tran_req_config_version
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    template_id VARCHAR2(32) NULL,
    version_no VARCHAR2(255) NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE NULL,
    update_by VARCHAR2(32) NULL,
    update_date DATE NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE fex_tran_req_config_version IS '外汇交易模板配置版本表';
COMMENT ON COLUMN fex_tran_req_config_version.TENANT_ID IS '租户ID';
COMMENT ON COLUMN fex_tran_req_config_version.ID IS '主键';
COMMENT ON COLUMN fex_tran_req_config_version.template_id IS '模板ID';
COMMENT ON COLUMN fex_tran_req_config_version.version_no IS '版本号';
COMMENT ON COLUMN fex_tran_req_config_version.create_by IS '创建人ID';
COMMENT ON COLUMN fex_tran_req_config_version.create_date IS '创建时间';
COMMENT ON COLUMN fex_tran_req_config_version.update_by IS '修改人ID';
COMMENT ON COLUMN fex_tran_req_config_version.update_date IS '修改时间';


-- 表说明：交易管理扩展表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE FEX_TRAN_REQ_EXT';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE FEX_TRAN_REQ_EXT
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    REQ_ID VARCHAR2(32) NULL,
    TRAN_CUR_PAIR_ID VARCHAR2(32) NULL,
    CONVERT_CUR_ID VARCHAR2(32) NULL,
    CONVERT_CUR_REFER_ID VARCHAR2(32) NULL,
    IS_PAYMENT_AMOUNT VARCHAR2(10) NULL,
    TRAN_RATE_TYPE VARCHAR2(10) NULL,
    QUOTATION_METHOD VARCHAR2(100) NULL,
    QUOTATION_CUR_ID VARCHAR2(32) NULL,
    QUOTATION_REFER_CUR_ID VARCHAR2(32) NULL,
    POSITION_MONEY NUMBER(19,2) NULL,
    PROFIT_LOSS_CUR VARCHAR2(32) NULL,
    PROFIT_LOSS_MONEY NUMBER(19,2) NULL,
    CONVERT_CUR_TYPE VARCHAR2(10) NULL,
    CONVERT_RATE NUMBER(8,6) NULL,
    CONVERT_MONEY NUMBER(19,2) NULL,
    TRAN_CUR_PAIR_BASE_ID VARCHAR2(32) NULL,
    TRAN_CUR_PAIR_REFER_ID VARCHAR2(32) NULL,
    LIFE_CYCLE_STATE VARCHAR2(20) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE FEX_TRAN_REQ_EXT IS '交易管理扩展表';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.TENANT_ID IS '租户ID';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.ID IS '主键';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.REQ_ID IS '交易id';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.TRAN_CUR_PAIR_ID IS '交易货币对id';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.CONVERT_CUR_ID IS '折本位币标准币种';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.CONVERT_CUR_REFER_ID IS '折本位币报价币种';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.IS_PAYMENT_AMOUNT IS '金额是否自动计算';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.TRAN_RATE_TYPE IS '交易汇率类型';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.QUOTATION_METHOD IS '报价方式';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.QUOTATION_CUR_ID IS '标准币种';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.QUOTATION_REFER_CUR_ID IS '报价币种';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.POSITION_MONEY IS '持仓金额';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.PROFIT_LOSS_CUR IS '损益币种';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.PROFIT_LOSS_MONEY IS '损益';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.CONVERT_CUR_TYPE IS '折本位币报价方式';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.CONVERT_RATE IS '折本位币汇率';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.CONVERT_MONEY IS '折本位币损益、期权费金额';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.TRAN_CUR_PAIR_BASE_ID IS '交易货币对标准币种';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.TRAN_CUR_PAIR_REFER_ID IS '交易货币对报价币种';
COMMENT ON COLUMN FEX_TRAN_REQ_EXT.LIFE_CYCLE_STATE IS '生命周期状态';


-- 表说明：交易请求字段配置表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE fex_tran_req_field';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE fex_tran_req_field
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    field_sort NUMBER(10) NULL,
    field_code VARCHAR2(30) NULL,
    field_name VARCHAR2(30) NULL,
    field_type VARCHAR2(10) NULL,
    data_type VARCHAR2(20) NULL,
    data_group VARCHAR2(255) NULL,
    data_class VARCHAR2(255) NULL,
    data_url VARCHAR2(255) NULL,
    data_callback VARCHAR2(255) NULL,
    FIELD_AREA VARCHAR2(255) NULL,
    FIELD_PARENT_CODE VARCHAR2(255) NULL,
    FIELD_CONTENT VARCHAR2(4000) NULL,
    valid_sign VARCHAR2(1) NULL,
    is_sys VARCHAR2(1) NULL,
    data_beforeinit VARCHAR2(255) NULL,
    data_valuefield VARCHAR2(255) NULL,
    data_textfield VARCHAR2(255) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE fex_tran_req_field IS '交易请求字段配置表';
COMMENT ON COLUMN fex_tran_req_field.TENANT_ID IS '租户ID';
COMMENT ON COLUMN fex_tran_req_field.ID IS '主键';
COMMENT ON COLUMN fex_tran_req_field.field_code IS '字段编码';
COMMENT ON COLUMN fex_tran_req_field.field_name IS '字段名称';
COMMENT ON COLUMN fex_tran_req_field.field_type IS '字段类型';
COMMENT ON COLUMN fex_tran_req_field.data_type IS '数据类型';
COMMENT ON COLUMN fex_tran_req_field.data_group IS '字典组';
COMMENT ON COLUMN fex_tran_req_field.data_class IS '数据分类';
COMMENT ON COLUMN fex_tran_req_field.data_url IS '数据URL';
COMMENT ON COLUMN fex_tran_req_field.data_callback IS '数据回调';
COMMENT ON COLUMN fex_tran_req_field.FIELD_AREA IS '数据区域';
COMMENT ON COLUMN fex_tran_req_field.FIELD_CONTENT IS '字段内容';
COMMENT ON COLUMN fex_tran_req_field.valid_sign IS '有效标识';
COMMENT ON COLUMN fex_tran_req_field.is_sys IS '是否系统预设';
COMMENT ON COLUMN fex_tran_req_field.data_beforeinit IS '初始化前事件';
COMMENT ON COLUMN fex_tran_req_field.data_valuefield IS '值字段';
COMMENT ON COLUMN fex_tran_req_field.data_textfield IS '文本字段';


-- 表说明：FexTranTemplateConfig
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE fex_tran_template_config';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE fex_tran_template_config
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    parent_id VARCHAR2(32) NULL,
    template_no VARCHAR2(255) NULL,
    template_name VARCHAR2(100) NULL,
    template_desc VARCHAR2(255) NULL,
    bank_type_id VARCHAR2(32) NULL,
    bif_id VARCHAR2(32) NULL,
    country_id VARCHAR2(65) NULL,
    pay_type_id VARCHAR2(255) NULL,
    remark VARCHAR2(200) NULL,
    valid_sign VARCHAR2(1) NULL,
    is_delete CHAR(1) NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE NULL,
    update_by VARCHAR2(32) NULL,
    update_date DATE NULL,
    sort NUMBER(10) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE fex_tran_template_config IS 'FexTranTemplateConfig';
COMMENT ON COLUMN fex_tran_template_config.TENANT_ID IS '租户ID';
COMMENT ON COLUMN fex_tran_template_config.ID IS '主键';
COMMENT ON COLUMN fex_tran_template_config.parent_id IS '外键.评分指标表（Scoring_Indicator）的主键';
COMMENT ON COLUMN fex_tran_template_config.template_no IS '模板编号';
COMMENT ON COLUMN fex_tran_template_config.template_name IS '模板名称';
COMMENT ON COLUMN fex_tran_template_config.template_desc IS '模板描述';
COMMENT ON COLUMN fex_tran_template_config.bank_type_id IS '银行类型ID';
COMMENT ON COLUMN fex_tran_template_config.bif_id IS '银行接口ID';
COMMENT ON COLUMN fex_tran_template_config.country_id IS '国家ID';
COMMENT ON COLUMN fex_tran_template_config.pay_type_id IS '支付类型';
COMMENT ON COLUMN fex_tran_template_config.remark IS '客户摘要';
COMMENT ON COLUMN fex_tran_template_config.valid_sign IS '有效标志 1-有效 0-无效';
COMMENT ON COLUMN fex_tran_template_config.is_delete IS '是否删除 0否 1是';
COMMENT ON COLUMN fex_tran_template_config.create_by IS '创建人ID';
COMMENT ON COLUMN fex_tran_template_config.create_date IS '创建时间';
COMMENT ON COLUMN fex_tran_template_config.update_by IS '修改人ID';
COMMENT ON COLUMN fex_tran_template_config.update_date IS '修改时间';
COMMENT ON COLUMN fex_tran_template_config.sort IS '排序';

-- ============================================================
-- Missing columns
-- ============================================================

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'ACTUAL_TRANS_MONEY';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add ACTUAL_TRANS_MONEY NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.ACTUAL_TRANS_MONEY is ''实际买入（卖出）金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'ACTUAL_TRANS_RATE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add ACTUAL_TRANS_RATE NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.ACTUAL_TRANS_RATE is ''实际成交汇率''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'ADVANCE_CHARGE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add ADVANCE_CHARGE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.ADVANCE_CHARGE_AMOUNT is ''预付货款项下金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'ADVANCE_CONTRACT_RATE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add ADVANCE_CONTRACT_RATE NUMBER(15,6) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.ADVANCE_CONTRACT_RATE is ''预付货款项下所占合同金额比例''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'ADVANCE_RATE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add ADVANCE_RATE NUMBER(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.ADVANCE_RATE is ''预收款比例''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'BANK_EXTRA';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add BANK_EXTRA VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.BANK_EXTRA is ''银行额外参数''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'BORDER_TRADE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add BORDER_TRADE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.BORDER_TRADE_AMOUNT is ''边境贸易金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'CHECK_DAY';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add CHECK_DAY NUMBER(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.CHECK_DAY is ''结账期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'CUSTOM_AMOUNT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add CUSTOM_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.CUSTOM_AMOUNT is ''海关特殊监管区域及保税监管场所进出境物流货物金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'DELIVERY_DEADLINE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add DELIVERY_DEADLINE VARCHAR2(50) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.DELIVERY_DEADLINE is ''交割期限''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'DELIVERY_TYPE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add DELIVERY_TYPE VARCHAR2(1) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.DELIVERY_TYPE is ''交割类型 0-本金交割、1-无本金交割''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'DIRECT_INVESTMENT_CERT_NO';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add DIRECT_INVESTMENT_CERT_NO VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.DIRECT_INVESTMENT_CERT_NO is ''直接投资-批准证书号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'EXC_SOURCE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add EXC_SOURCE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.EXC_SOURCE is ''外汇指令来源：REQ-外汇交易，CASH_FLOW-现金流''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'FEED_AMOUNT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add FEED_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.FEED_AMOUNT is ''进料加工金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'GENERAL_TRADE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add GENERAL_TRADE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.GENERAL_TRADE_AMOUNT is ''一般贸易金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'GOODS_AMOUNT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add GOODS_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.GOODS_AMOUNT is ''货物贸易金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'INVEST_AMT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add INVEST_AMT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.INVEST_AMT is ''投资收益金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'INVEST_APPLY_NBR';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add INVEST_APPLY_NBR VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.INVEST_APPLY_NBR is ''投资收益-批准证书号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'INVEST_NO';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add INVEST_NO VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.INVEST_NO is ''投资收益-国际收支编码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'OFFSHORE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add OFFSHORE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OFFSHORE_AMOUNT is ''离岸转手买卖金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'OFTEN_AMT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add OFTEN_AMT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OFTEN_AMT is ''经常转移金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'OFTEN_NO';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add OFTEN_NO VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OFTEN_NO is ''经常转移-国际收支编码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'OLD_VOUCHER_NO';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add OLD_VOUCHER_NO VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OLD_VOUCHER_NO is ''原指令编号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'OTHER_AMOUNT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add OTHER_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OTHER_AMOUNT is ''其他金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'OTHER_TRADE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add OTHER_TRADE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OTHER_TRADE_AMOUNT is ''其他贸易金额''';
end if;
end;
/

-- NOTE: Entity column annotation contains leading/trailing whitespace: 'PAYEE_STREET_NAME '. Generated SQL uses 'PAYEE_STREET_NAME'.
declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'PAYEE_STREET_NAME';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add PAYEE_STREET_NAME VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.PAYEE_STREET_NAME is ''收款方街道名称''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'PURPOSE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add PURPOSE VARCHAR2(120) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.PURPOSE is ''结汇资金用途(针对外汇买卖交易)''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'REGULATORY_TYPE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add REGULATORY_TYPE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.REGULATORY_TYPE is ''监管类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'REM_CHG_TOL_AMT';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add REM_CHG_TOL_AMT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.REM_CHG_TOL_AMT is ''费用总额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'REM_CHG_TOL_AMT_CUR';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add REM_CHG_TOL_AMT_CUR VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.REM_CHG_TOL_AMT_CUR is ''费用币种''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'RPT_GATE_DATE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add RPT_GATE_DATE NUMBER(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.RPT_GATE_DATE is ''预计X天后报关''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'RPT_GATE_ENT_NAME';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add RPT_GATE_ENT_NAME VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.RPT_GATE_ENT_NAME is ''报关经营单位名称''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'RPT_GATE_ENT_NO';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add RPT_GATE_ENT_NO VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.RPT_GATE_ENT_NO is ''组织机构代码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'RPT_GATE_TYPE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add RPT_GATE_TYPE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.RPT_GATE_TYPE is ''报关方式 R-人民币；O-外币''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'SIGNATURE_CODE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add SIGNATURE_CODE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.SIGNATURE_CODE is ''签名''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'TRAN_RATE_TYPE';
if v_count < 1 then
        v_str := 'alter table BIS_FOREIGN_EXC add TRAN_RATE_TYPE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.TRAN_RATE_TYPE is ''交易汇率类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CASH_FLOW'
  and column_name = 'BANK_SERIAL_NO';
if v_count < 1 then
        v_str := 'alter table FEX_CASH_FLOW add BANK_SERIAL_NO VARCHAR2(60) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CASH_FLOW.BANK_SERIAL_NO is ''银行流水号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CASH_FLOW'
  and column_name = 'BILL_FROM';
if v_count < 1 then
        v_str := 'alter table FEX_CASH_FLOW add BILL_FROM VARCHAR2(2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CASH_FLOW.BILL_FROM is ''单据来源 0-交易生成（默认） 1-退款勾对生成''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CASH_FLOW'
  and column_name = 'CASH_ITEM';
if v_count < 1 then
        v_str := 'alter table FEX_CASH_FLOW add CASH_ITEM VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CASH_FLOW.CASH_ITEM is ''现金流项目''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CASH_FLOW'
  and column_name = 'PAYEE_CUR_ID';
if v_count < 1 then
        v_str := 'alter table FEX_CASH_FLOW add PAYEE_CUR_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CASH_FLOW.PAYEE_CUR_ID is ''收款账号对应的币种''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CASH_FLOW'
  and column_name = 'REL_BILL_NO';
if v_count < 1 then
        v_str := 'alter table FEX_CASH_FLOW add REL_BILL_NO VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CASH_FLOW.REL_BILL_NO is ''关联现金流号码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CORP_BANKACC_DTL'
  and column_name = 'PAY_MODEL';
if v_count < 1 then
        v_str := 'alter table FEX_CORP_BANKACC_DTL add PAY_MODEL VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CORP_BANKACC_DTL.PAY_MODEL is ''收款账户类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CORP_BANKACC_DTL'
  and column_name = 'PAY_VALUE';
if v_count < 1 then
        v_str := 'alter table FEX_CORP_BANKACC_DTL add PAY_VALUE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CORP_BANKACC_DTL.PAY_VALUE is ''快捷支付信息''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'AIIN';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add AIIN VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.AIIN is ''信用卡号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'BUILDING_NUMBER';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add BUILDING_NUMBER VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.BUILDING_NUMBER is ''楼栋编号(地址2)''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'CITY';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add CITY VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.CITY is ''所属市''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'CITY_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add CITY_NAME VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.CITY_NAME is ''所在城市''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'E_MAIL';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add E_MAIL VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.E_MAIL is ''邮箱''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'FPS_ID';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add FPS_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.FPS_ID is ''FPS ID''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'HKID';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add HKID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.HKID is ''香港身份证号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'ISD_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add ISD_CODE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.ISD_CODE is ''国家代码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'LOCAL_CLEAR_NO';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add LOCAL_CLEAR_NO VARCHAR2(30) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.LOCAL_CLEAR_NO is ''本地清算号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'OPP_ID';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add OPP_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.OPP_ID is ''交易对手数据源id''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'PHONE_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add PHONE_CODE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.PHONE_CODE is ''电话号码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'PRODUCT_ID';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add PRODUCT_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.PRODUCT_ID is ''支付产品''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'PROV';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add PROV VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.PROV is ''所属省''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'PROV_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add PROV_NAME VARCHAR2(30) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.PROV_NAME is ''省名称''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'STATE';
if v_count < 1 then
        v_str := 'alter table FEX_COTP add STATE NUMBER(3,0) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.STATE is ''省/州''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP_BANKACC'
  and column_name = 'BANK_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_COTP_BANKACC add BANK_NAME VARCHAR2(200) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP_BANKACC.BANK_NAME is ''交易账户所属银行''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CUST_FIELD_RULE'
  and column_name = 'DICT_GROUP';
if v_count < 1 then
        v_str := 'alter table FEX_CUST_FIELD_RULE add DICT_GROUP VARCHAR2(200) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CUST_FIELD_RULE.DICT_GROUP is ''字典组代码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CUST_FIELD_RULE'
  and column_name = 'FIELD_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_CUST_FIELD_RULE add FIELD_CODE VARCHAR2(30) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CUST_FIELD_RULE.FIELD_CODE is ''字段编码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CUST_FIELD_RULE'
  and column_name = 'LABEL_BANK';
if v_count < 1 then
        v_str := 'alter table FEX_CUST_FIELD_RULE add LABEL_BANK VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CUST_FIELD_RULE.LABEL_BANK is ''银行标签''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_FOREIGN_REQ'
  and column_name = 'BANK_PAY_TYPE';
if v_count < 1 then
        v_str := 'alter table FEX_FOREIGN_REQ add BANK_PAY_TYPE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_FOREIGN_REQ.BANK_PAY_TYPE is ''支付类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_FOREIGN_REQ'
  and column_name = 'BILL_FROM';
if v_count < 1 then
        v_str := 'alter table FEX_FOREIGN_REQ add BILL_FROM VARCHAR2(2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_FOREIGN_REQ.BILL_FROM is ''单据来源''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_FOREIGN_REQ'
  and column_name = 'BNF_RELATE';
if v_count < 1 then
        v_str := 'alter table FEX_FOREIGN_REQ add BNF_RELATE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_FOREIGN_REQ.BNF_RELATE is ''受益人关联关系''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_FOREIGN_REQ'
  and column_name = 'CASH_ITEM';
if v_count < 1 then
        v_str := 'alter table FEX_FOREIGN_REQ add CASH_ITEM VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_FOREIGN_REQ.CASH_ITEM is ''现金流项目''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_FOREIGN_REQ'
  and column_name = 'DIF_BANK';
if v_count < 1 then
        v_str := 'alter table FEX_FOREIGN_REQ add DIF_BANK VARCHAR2(1) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_FOREIGN_REQ.DIF_BANK is ''跨行标识''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_FOREIGN_REQ'
  and column_name = 'ERP_ID';
if v_count < 1 then
        v_str := 'alter table FEX_FOREIGN_REQ add ERP_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_FOREIGN_REQ.ERP_ID is ''ERP编号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_FOREIGN_REQ'
  and column_name = 'IS_PAYED';
if v_count < 1 then
        v_str := 'alter table FEX_FOREIGN_REQ add IS_PAYED VARCHAR2(1) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_FOREIGN_REQ.IS_PAYED is ''是否已付款''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_FOREIGN_REQ'
  and column_name = 'PAYEE_AGENT_SWIFT_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_FOREIGN_REQ add PAYEE_AGENT_SWIFT_CODE VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_FOREIGN_REQ.PAYEE_AGENT_SWIFT_CODE is ''收款银行之代理行swift code''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PRODUCT'
  and column_name = 'IS_AUTO_DELIVERY';
if v_count < 1 then
        v_str := 'alter table FEX_PRODUCT add IS_AUTO_DELIVERY VARCHAR2(1)  NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PRODUCT.IS_AUTO_DELIVERY is ''即期是否自动交割 1-是 0-否''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_RATE_INFO'
  and column_name = 'CUR_UNIT';
if v_count < 1 then
        v_str := 'alter table FEX_RATE_INFO add CUR_UNIT VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_RATE_INFO.CUR_UNIT is ''报价金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_RATE_INFO'
  and column_name = 'EFFECT_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_RATE_INFO add EFFECT_DATE DATE NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_RATE_INFO.EFFECT_DATE is ''发布时间''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_RATE_INFO'
  and column_name = 'EXCHANGE_RATE_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_RATE_INFO add EXCHANGE_RATE_DATE DATE NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_RATE_INFO.EXCHANGE_RATE_DATE is ''汇率日期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_RATE_INFO'
  and column_name = 'TERM';
    if v_count < 1 then
        v_str := 'alter table FEX_RATE_INFO add TERM VARCHAR2(50) NULL';
    EXECUTE IMMEDIATE v_str;
    ELSE
        v_str := 'ALTER TABLE FEX_RATE_INFO MODIFY TERM VARCHAR2(50)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_RATE_INFO.TERM is ''期限''';
end if;
end;
/

-- NOTE: Entity column annotation contains leading/trailing whitespace: 'TYPE_NAME '. Generated SQL uses 'TYPE_NAME'.
declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_RATE_TYPE'
  and column_name = 'TYPE_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_RATE_TYPE add TYPE_NAME VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_RATE_TYPE.TYPE_NAME is ''名称''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'AIIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add AIIN VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.AIIN is ''信用卡号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'BANK_PAY_TYPE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add BANK_PAY_TYPE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

-- NOTE: Entity column annotation contains leading/trailing whitespace: 'BANK_PAY_TYPE_ID '. Generated SQL uses 'BANK_PAY_TYPE_ID'.
declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'BANK_PAY_TYPE_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add BANK_PAY_TYPE_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.BANK_PAY_TYPE_ID is ''支付类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'BANK_PAY_TYPE_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add BANK_PAY_TYPE_NAME VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'CORP_ACC_CUR_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add CORP_ACC_CUR_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.CORP_ACC_CUR_ID is ''主体单位账号币种''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'CORP_ADDR';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add CORP_ADDR VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.CORP_ADDR is ''主体单位地址（中文）''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'COTP_ACC_CUR_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add COTP_ACC_CUR_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.COTP_ACC_CUR_ID is ''交易对手银行账户币种Id''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'COTP_ADDRESS';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add COTP_ADDRESS VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.COTP_ADDRESS is ''交易对手地点''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'COTP_COUNTRY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add COTP_COUNTRY VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.COTP_COUNTRY is ''交易对手国家地区''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'CREATE_BY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add CREATE_BY VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.CREATE_BY is ''创建人登录名称''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'CREATE_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add CREATE_DATE DATE NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.CREATE_DATE is ''创建日期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'DEDUCTION_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add DEDUCTION_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.DEDUCTION_AMOUNT is ''抵扣金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'E_MAIL';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add E_MAIL VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.E_MAIL is ''邮箱''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'END_SETTLE_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add END_SETTLE_DATE DATE NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.END_SETTLE_DATE is ''远端结算日期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'END_WISH_RATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add END_WISH_RATE NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.END_WISH_RATE is ''期望汇率''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'FEX_TRAN_TYPE_REMOTE_END';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add FEX_TRAN_TYPE_REMOTE_END VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.FEX_TRAN_TYPE_REMOTE_END is ''远端交易类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'FPS_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add FPS_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.FPS_ID is ''FPS ID''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'HKID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add HKID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.HKID is ''香港身份证号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'INVOICE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add INVOICE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.INVOICE_AMOUNT is ''发票净额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'INVOICE_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add INVOICE_DATE DATE NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.INVOICE_DATE is ''发票日期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'INVOICE_TOTAL';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add INVOICE_TOTAL NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.INVOICE_TOTAL is ''发票总额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'IS_PAYED';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add IS_PAYED VARCHAR2(1) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.IS_PAYED is ''是否线下付款''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'IS_PAYMENT_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add IS_PAYMENT_AMOUNT VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.IS_PAYMENT_AMOUNT is ''金额是否自动计算''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'ISD_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add ISD_CODE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.ISD_CODE is ''国家代码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'PAYEE_TYPE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add PAYEE_TYPE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PAYEE_TYPE is ''收款人类型：0-对公居民、1-对公非居民、2-对私居民、3-对私非居民''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'PHONE_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add PHONE_CODE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PHONE_CODE is ''电话号码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'PREMIUM_MONEY_MAX';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add PREMIUM_MONEY_MAX NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PREMIUM_MONEY_MAX is ''期权费金额最大值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'PREMIUM_MONEY_MIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add PREMIUM_MONEY_MIN NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PREMIUM_MONEY_MIN is ''期权费金额最小值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'PREMIUM_POINT_MAX';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add PREMIUM_POINT_MAX NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PREMIUM_POINT_MAX is ''期权费点数最大值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'PREMIUM_POINT_MIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add PREMIUM_POINT_MIN NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PREMIUM_POINT_MIN is ''期权费点数最小值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'QUOTATION_METHOD';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add QUOTATION_METHOD VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.QUOTATION_METHOD is ''报价方式''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'RAT_MONEY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add RAT_MONEY NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'REMOTE_END_WISH_RATE_MAX';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add REMOTE_END_WISH_RATE_MAX NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.REMOTE_END_WISH_RATE_MAX is ''远端期望汇率''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'REMOTE_END_WISH_RATE_MIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add REMOTE_END_WISH_RATE_MIN NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.REMOTE_END_WISH_RATE_MIN is ''远端期望汇率''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'SAVE_FIELDS';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add SAVE_FIELDS VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.SAVE_FIELDS is ''交易对手银行账号保存字段''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'SWAP_POINT_MAX';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add SWAP_POINT_MAX NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.SWAP_POINT_MAX is ''掉期点最小值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'SWAP_POINT_MIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add SWAP_POINT_MIN NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.SWAP_POINT_MIN is ''掉期点最大值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'TEMPLATE_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add TEMPLATE_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.TEMPLATE_ID is ''模板ID''';
end if;
end;
/

-- NOTE: Entity column annotation contains leading/trailing whitespace: 'TEMPLATE_NAME '. Generated SQL uses 'TEMPLATE_NAME'.
declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'TEMPLATE_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add TEMPLATE_NAME VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.TEMPLATE_NAME is ''评价模板名称''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'TRAN_CUR_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL add TRAN_CUR_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.TRAN_CUR_ID is ''交易货币对''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'AIIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add AIIN VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.AIIN is ''信用卡号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'BANK_PAY_TYPE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add BANK_PAY_TYPE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'BANK_PAY_TYPE_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add BANK_PAY_TYPE_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.BANK_PAY_TYPE_ID is ''支付类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'BANK_PAY_TYPE_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add BANK_PAY_TYPE_NAME VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'CORP_ACC_CUR_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add CORP_ACC_CUR_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.CORP_ACC_CUR_ID is ''主体单位账号币种''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'CORP_ADDR';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add CORP_ADDR VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.CORP_ADDR is ''主体单位地址（中文）''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'COTP_ACC_CUR_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add COTP_ACC_CUR_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.COTP_ACC_CUR_ID is ''交易对手银行账户币种Id''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'COTP_ADDRESS';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add COTP_ADDRESS VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.COTP_ADDRESS is ''交易对手地点''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'COTP_COUNTRY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add COTP_COUNTRY VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.COTP_COUNTRY is ''交易对手国家地区''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'CREATE_BY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add CREATE_BY VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.CREATE_BY is ''创建人登录名称''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'CREATE_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add CREATE_DATE DATE NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.CREATE_DATE is ''创建日期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'DEDUCTION_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add DEDUCTION_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.DEDUCTION_AMOUNT is ''抵扣金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'E_MAIL';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add E_MAIL VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.E_MAIL is ''邮箱''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'END_SETTLE_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add END_SETTLE_DATE DATE NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.END_SETTLE_DATE is ''远端结算日期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'END_WISH_RATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add END_WISH_RATE NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.END_WISH_RATE is ''期望汇率''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'FEX_TRAN_TYPE_REMOTE_END';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add FEX_TRAN_TYPE_REMOTE_END VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.FEX_TRAN_TYPE_REMOTE_END is ''远端交易类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'FPS_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add FPS_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.FPS_ID is ''FPS ID''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'HKID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add HKID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.HKID is ''香港身份证号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'INVOICE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add INVOICE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.INVOICE_AMOUNT is ''发票净额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'INVOICE_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add INVOICE_DATE DATE NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.INVOICE_DATE is ''发票日期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'INVOICE_TOTAL';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add INVOICE_TOTAL NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.INVOICE_TOTAL is ''发票总额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'IS_PAYED';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add IS_PAYED VARCHAR2(1) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.IS_PAYED is ''是否线下付款''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'IS_PAYMENT_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add IS_PAYMENT_AMOUNT VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.IS_PAYMENT_AMOUNT is ''金额是否自动计算''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'ISD_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add ISD_CODE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.ISD_CODE is ''国家代码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'PAYEE_TYPE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PAYEE_TYPE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PAYEE_TYPE is ''收款人类型：0-对公居民、1-对公非居民、2-对私居民、3-对私非居民''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'PHONE_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PHONE_CODE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PHONE_CODE is ''电话号码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'PREMIUM_MONEY_MAX';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PREMIUM_MONEY_MAX NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PREMIUM_MONEY_MAX is ''期权费金额最大值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'PREMIUM_MONEY_MIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PREMIUM_MONEY_MIN NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PREMIUM_MONEY_MIN is ''期权费金额最小值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'PREMIUM_POINT_MAX';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PREMIUM_POINT_MAX NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PREMIUM_POINT_MAX is ''期权费点数最大值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'PREMIUM_POINT_MIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PREMIUM_POINT_MIN NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PREMIUM_POINT_MIN is ''期权费点数最小值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'QUOTATION_METHOD';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add QUOTATION_METHOD VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.QUOTATION_METHOD is ''报价方式''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'REMOTE_END_WISH_RATE_MAX';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add REMOTE_END_WISH_RATE_MAX NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.REMOTE_END_WISH_RATE_MAX is ''远端期望汇率''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'REMOTE_END_WISH_RATE_MIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add REMOTE_END_WISH_RATE_MIN NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.REMOTE_END_WISH_RATE_MIN is ''远端期望汇率''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'SAVE_FIELDS';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add SAVE_FIELDS VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.SAVE_FIELDS is ''交易对手银行账号保存字段''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'SWAP_POINT_MAX';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add SWAP_POINT_MAX NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.SWAP_POINT_MAX is ''掉期点最小值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'SWAP_POINT_MIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add SWAP_POINT_MIN NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.SWAP_POINT_MIN is ''掉期点最大值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'TEMPLATE_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add TEMPLATE_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.TEMPLATE_ID is ''模板ID''';
end if;
end;
/

-- NOTE: Entity column annotation contains leading/trailing whitespace: 'TEMPLATE_NAME '. Generated SQL uses 'TEMPLATE_NAME'.
declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'TEMPLATE_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add TEMPLATE_NAME VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.TEMPLATE_NAME is ''评价模板名称''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'TRAN_CUR_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_APP_DTL_TMP add TRAN_CUR_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.TRAN_CUR_ID is ''交易货币对''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'ADD_WORD';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add ADD_WORD VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.ADD_WORD is ''汇款附言''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'ADVANCE_CHARGE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add ADVANCE_CHARGE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.ADVANCE_CHARGE_AMOUNT is ''预付货款项下金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'ADVANCE_CONTRACT_RATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add ADVANCE_CONTRACT_RATE NUMBER(15,6) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.ADVANCE_CONTRACT_RATE is ''预付货款项下所占合同金额比例''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'ADVANCE_RATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add ADVANCE_RATE NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.ADVANCE_RATE is ''预收款比例''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'AIIN';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add AIIN VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.AIIN is ''信用卡号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'BANK_PAY_TYPE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add BANK_PAY_TYPE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.BANK_PAY_TYPE is ''支付类型（渣打银行）''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'BANK_PAY_TYPE_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add BANK_PAY_TYPE_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'BANK_PAY_TYPE_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add BANK_PAY_TYPE_NAME VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'BILL_FROM';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add BILL_FROM VARCHAR2(2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.BILL_FROM is ''单据来源''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'BNF_RELATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add BNF_RELATE VARCHAR2(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.BNF_RELATE is ''受益人关联关系''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'BORDER_TRADE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add BORDER_TRADE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.BORDER_TRADE_AMOUNT is ''边境贸易金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'CASH_ITEM';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add CASH_ITEM VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.CASH_ITEM is ''现金流项目''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'CHECK_DAY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add CHECK_DAY NUMBER(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.CHECK_DAY is ''结账期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'CORP_ADDR';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add CORP_ADDR VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.CORP_ADDR is ''主体单位地址（中文）''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'COST_CUR_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add COST_CUR_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.COST_CUR_ID is ''费用币种''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'COST_MONEY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add COST_MONEY NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.COST_MONEY is ''费用总额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'COTP_ACC_CUR_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add COTP_ACC_CUR_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'COTP_ADDRESS';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add COTP_ADDRESS VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.COTP_ADDRESS is ''交易对手地点''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'COTP_CITY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add COTP_CITY VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.COTP_CITY is ''交易对手市（国家地区为中国时需要）''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'COTP_COUNTRY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add COTP_COUNTRY VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.COTP_COUNTRY is ''交易对手国家地区''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'COTP_PROV';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add COTP_PROV VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.COTP_PROV is ''交易对手省（国家地区为中国时需要）''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'CUSTOM_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add CUSTOM_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.CUSTOM_AMOUNT is ''海关特殊监管区域及保税监管场所进出境物流货物金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'DEDUCTION_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add DEDUCTION_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.DEDUCTION_AMOUNT is ''抵扣金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'DIRECT_INVESTMENT_CERT_NO';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add DIRECT_INVESTMENT_CERT_NO VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.DIRECT_INVESTMENT_CERT_NO is ''直接投资-批准证书号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'E_MAIL';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add E_MAIL VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.E_MAIL is ''邮箱''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'EXCEPT_VAL';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add EXCEPT_VAL NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.EXCEPT_VAL is ''障碍值''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'FEED_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add FEED_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.FEED_AMOUNT is ''进料加工金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'FOREIGN_CONTRACT_NO';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add FOREIGN_CONTRACT_NO VARCHAR2(50) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.FOREIGN_CONTRACT_NO is ''外汇合同号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'FPS_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add FPS_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.FPS_ID is ''FPS ID''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'GENERAL_TRADE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add GENERAL_TRADE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.GENERAL_TRADE_AMOUNT is ''一般贸易金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'GOODS_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add GOODS_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.GOODS_AMOUNT is ''货物贸易金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'HKID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add HKID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.HKID is ''香港身份证号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'INVEST_AMT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add INVEST_AMT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.INVEST_AMT is ''投资收益金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'INVEST_APPLY_NBR';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add INVEST_APPLY_NBR VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.INVEST_APPLY_NBR is ''投资收益-批准证书号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'INVEST_NO';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add INVEST_NO VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.INVEST_NO is ''投资收益-国际收支编码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'INVOICE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add INVOICE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.INVOICE_AMOUNT is ''发票净额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'INVOICE_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add INVOICE_DATE DATE NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.INVOICE_DATE is ''发票日期''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'INVOICE_TOTAL';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add INVOICE_TOTAL NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.INVOICE_TOTAL is ''发票总额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'ISD_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add ISD_CODE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.ISD_CODE is ''国家代码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'MERGE_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add MERGE_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.MERGE_ID is ''合并后单据号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'OFFSHORE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add OFFSHORE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.OFFSHORE_AMOUNT is ''离岸转手买卖金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'OFTEN_AMT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add OFTEN_AMT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.OFTEN_AMT is ''经常转移金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'OFTEN_NO';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add OFTEN_NO VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.OFTEN_NO is ''经常转移-国际收支编码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'ORIG_BILL_NO';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add ORIG_BILL_NO VARCHAR2(500) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.ORIG_BILL_NO is ''原单据号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'ORIGIN_COUNTRY';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add ORIGIN_COUNTRY VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.ORIGIN_COUNTRY is ''起运国（兴业银行）''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'OTHER_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add OTHER_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.OTHER_AMOUNT is ''其他金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'OTHER_TRADE_AMOUNT';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add OTHER_TRADE_AMOUNT NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.OTHER_TRADE_AMOUNT is ''其他贸易金额''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'PHONE_CODE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add PHONE_CODE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.PHONE_CODE is ''电话号码''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'PREMIUM_MONEY_OF_PREMIUM_CUR';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add PREMIUM_MONEY_OF_PREMIUM_CUR NUMBER(19,2) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.PREMIUM_MONEY_OF_PREMIUM_CUR is ''期权费金额（期权费币种）''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'REGULATORY_TYPE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add REGULATORY_TYPE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.REGULATORY_TYPE is ''监管类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'RMK_LOCAL';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add RMK_LOCAL VARCHAR2(200) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.RMK_LOCAL is ''备注(本地语言)''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'RPT_GATE_DATE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add RPT_GATE_DATE NUMBER(10) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.RPT_GATE_DATE is ''预计X天后报关''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'RPT_GATE_ENT_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add RPT_GATE_ENT_ID VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.RPT_GATE_ENT_ID is ''报关经营单位ID''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'RPT_GATE_TYPE';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add RPT_GATE_TYPE VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.RPT_GATE_TYPE is ''报关方式 R-人民币；O-外币''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'SAVE_FIELDS';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add SAVE_FIELDS VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.SAVE_FIELDS is ''交易对手银行账号保存字段''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'SPLIT_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add SPLIT_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.SPLIT_ID is ''拆分前单据号''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'TEMPLATE_ID';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add TEMPLATE_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.TEMPLATE_ID is ''魔板ID''';
end if;
end;
/

-- NOTE: Entity column annotation contains leading/trailing whitespace: 'TEMPLATE_NAME '. Generated SQL uses 'TEMPLATE_NAME'.
declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'TEMPLATE_NAME';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add TEMPLATE_NAME VARCHAR2(100) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.TEMPLATE_NAME is ''评价模板名称''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'TIME_STAMP';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add TIME_STAMP VARCHAR2(20) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.TIME_STAMP is ''时间戳''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'VERSION_NO';
if v_count < 1 then
        v_str := 'alter table FEX_TRAN_REQ add VERSION_NO VARCHAR2(255) NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.VERSION_NO is ''模板版本号''';
end if;
end;
/

DECLARE
  VN_COUNT NUMBER;
  VC_STR   VARCHAR2(10000);
BEGIN
  SELECT COUNT(*)
    INTO VN_COUNT
    FROM USER_TAB_COLUMNS
   WHERE TABLE_NAME = 'BT_BANK_ACC'
     AND COLUMN_NAME = 'IS_CHECK_RECEIPT';
  IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE BT_BANK_ACC ADD IS_CHECK_RECEIPT VARCHAR2(1)';
    EXECUTE IMMEDIATE VC_STR;
  ELSE
    VC_STR := 'ALTER TABLE BT_BANK_ACC MODIFY IS_CHECK_RECEIPT VARCHAR2(1)';
    EXECUTE IMMEDIATE VC_STR;
  END IF;
END;
/

DECLARE
  VN_COUNT NUMBER;
BEGIN
  SELECT COUNT(*)
    INTO VN_COUNT
    FROM USER_INDEXES
   WHERE INDEX_NAME = 'IDX_NIS_BILLHEAD_SPLIT_ID'
     AND TABLE_NAME = 'NIS_BILLHEAD';
  IF VN_COUNT = 0 THEN
    EXECUTE IMMEDIATE 'create index IDX_NIS_BILLHEAD_SPLIT_ID on NIS_BILLHEAD(SPLIT_ID)';
  END IF;
  COMMIT;
END;
/

DECLARE
  VN_COUNT NUMBER;
BEGIN
  SELECT COUNT(*)
    INTO VN_COUNT
    FROM USER_INDEXES
   WHERE INDEX_NAME = 'IDX_NIS_BILLHEAD_MERGE_ID'
     AND TABLE_NAME = 'NIS_BILLHEAD';
  IF VN_COUNT = 0 THEN
    EXECUTE IMMEDIATE 'create index IDX_NIS_BILLHEAD_MERGE_ID on NIS_BILLHEAD(MERGE_ID)';
  END IF;
  COMMIT;
END;
/

DECLARE
  VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
  --增加长度
  SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'bis_bank_receipt_dtl' AND COLUMN_NAME = 'FILE_PATH';
  IF VN_COUNT = 1 THEN
    VC_STR := 'alter table bis_bank_receipt_dtl modify FILE_PATH VARCHAR2(4000)';
    EXECUTE IMMEDIATE VC_STR;
  END IF;
END;
/

--增加字段语句 IS_AUTO_DELIVERY
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_PRODUCT'
     and column_name = 'IS_AUTO_DELIVERY';
  if v_count < 1 then
    v_str := 'alter table FEX_PRODUCT add IS_AUTO_DELIVERY VARCHAR2(10)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_PRODUCT.IS_AUTO_DELIVERY
        is ''即期是否自动交割''';
  end if;
end;
/

--增加字段语句 TERM
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_RATE_INFO'
     and column_name = 'TERM';
  if v_count < 1 then
    v_str := 'alter table FEX_RATE_INFO add TERM VARCHAR2(50)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_RATE_INFO.TERM
        is ''期限''';
  end if;
end;
/

--增加字段语句 EXCHANGE_RATE_DATE
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_RATE_INFO'
     and column_name = 'EXCHANGE_RATE_DATE';
  if v_count < 1 then
    v_str := 'alter table FEX_RATE_INFO add EXCHANGE_RATE_DATE DATE  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_RATE_INFO.EXCHANGE_RATE_DATE
        is ''汇率日期''';
  end if;
end;
/

--增加字段语句 TRAN_CUR_ID
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'TRAN_CUR_ID';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add TRAN_CUR_ID VARCHAR2(32)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.TRAN_CUR_ID
        is ''交易货币对ID''';
  end if;
end;
/

--增加字段语句 QUOTATION_METHOD
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'QUOTATION_METHOD';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add QUOTATION_METHOD VARCHAR2(100)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.QUOTATION_METHOD
        is ''报价方式''';
  end if;
end;
/

--增加字段语句 END_WISH_RATE
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'END_WISH_RATE';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add END_WISH_RATE NUMBER(14,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.END_WISH_RATE
        is ''近端期望汇率''';
  end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'REMOTE_END_WISH_RATE_MIN';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add REMOTE_END_WISH_RATE_MIN NUMBER(14,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.REMOTE_END_WISH_RATE_MIN
        is ''远端期望汇率''';
  end if;
  commit;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'REMOTE_END_WISH_RATE_MAX';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add REMOTE_END_WISH_RATE_MAX NUMBER(14,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.REMOTE_END_WISH_RATE_MAX
        is ''远端期望汇率''';
  end if;
end;
/

--增加字段语句 END_SETTLE_DATE
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'END_SETTLE_DATE';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add END_SETTLE_DATE DATE ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.END_SETTLE_DATE
        is ''远端结算日期''';
  end if;
end;
/

--增加字段语句 SWAP_POINT_MIN
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'SWAP_POINT_MIN';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add SWAP_POINT_MIN NUMBER(15,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.SWAP_POINT_MIN
        is ''掉期点最小值''';
  end if;
end;
/

--增加字段语句 SWAP_POINT_MAX
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'SWAP_POINT_MAX';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add SWAP_POINT_MAX NUMBER(15,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.SWAP_POINT_MAX
        is ''掉期点最大值''';
  end if;
end;
/

--增加字段语句 IS_PAYMENT_AMOUNT
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'IS_PAYMENT_AMOUNT';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add IS_PAYMENT_AMOUNT VARCHAR2(10)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.IS_PAYMENT_AMOUNT
        is ''金额是否自动计算''';
  end if;
end;
/

--增加字段语句 PREMIUM_CUR
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'PREMIUM_CUR';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add PREMIUM_CUR VARCHAR2(32)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PREMIUM_CUR
        is ''期权费币种''';
  end if;
end;
/

--增加字段语句 PREMIUM_POINT_MIN
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'PREMIUM_POINT_MIN';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add PREMIUM_POINT_MIN NUMBER(15,6)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PREMIUM_POINT_MIN
        is ''期权费点数最小值''';
  end if;
end;
/

--增加字段语句 PREMIUM_POINT_MAX
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'PREMIUM_POINT_MAX';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add PREMIUM_POINT_MAX NUMBER(15,6)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PREMIUM_POINT_MAX
        is ''期权费点数最大值''';
  end if;
end;
/

--增加字段语句 PREMIUM_MONEY_MIN
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'PREMIUM_MONEY_MIN';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add PREMIUM_MONEY_MIN NUMBER(15,6)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PREMIUM_MONEY_MIN
        is ''期权费最小值''';
  end if;
end;
/

--增加字段语句 PREMIUM_MONEY_MAX
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'PREMIUM_MONEY_MAX';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add PREMIUM_MONEY_MAX NUMBER(15,6)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.PREMIUM_MONEY_MAX
        is ''期权费最大值''';
  end if;
end;
/

--增加字段语句 TRAN_TYPE_REMOTE_END
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'FEX_TRAN_TYPE_REMOTE_END';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add FEX_TRAN_TYPE_REMOTE_END VARCHAR2(1)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.FEX_TRAN_TYPE_REMOTE_END
        is ''远端外汇交易类型''';
  end if;
end;
/

--增加字段语句 TRAN_TYPE_REMOTE_END
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'FEX_TRAN_TYPE_REMOTE_END';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add FEX_TRAN_TYPE_REMOTE_END VARCHAR2(1)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.FEX_TRAN_TYPE_REMOTE_END
        is ''远端外汇交易类型''';
  end if;
end;
/

--交易需求申请暂存表增加字段语句

--增加字段语句 TRAN_CUR_ID
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'TRAN_CUR_ID';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add TRAN_CUR_ID VARCHAR2(32)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.TRAN_CUR_ID
        is ''交易货币对ID''';
  end if;
end;
/

--增加字段语句 QUOTATION_METHOD
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'QUOTATION_METHOD';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add QUOTATION_METHOD VARCHAR2(100)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.QUOTATION_METHOD
        is ''报价方式''';
  end if;
end;
/

--增加字段语句 END_WISH_RATE
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'END_WISH_RATE';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add END_WISH_RATE NUMBER(14,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.END_WISH_RATE
        is ''远端期望汇率''';
  end if;
end;
/

--增加字段语句 END_WISH_RATE
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'END_WISH_RATE';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add END_WISH_RATE NUMBER(14,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.END_WISH_RATE
        is ''近端期望汇率''';
  end if;
end;
/

--增加字段语句 END_WISH_RATE
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'REMOTE_END_WISH_RATE_MIN';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add REMOTE_END_WISH_RATE_MIN NUMBER(14,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.REMOTE_END_WISH_RATE_MIN
        is ''远端期望汇率''';
  end if;
end;
/

--增加字段语句 REMOTE_END_WISH_RATE_MAX
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'REMOTE_END_WISH_RATE_MAX';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add REMOTE_END_WISH_RATE_MAX NUMBER(14,6) ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.REMOTE_END_WISH_RATE_MAX
        is ''远端结算日期''';
  end if;
end;
/

--增加字段语句 SWAP_POINT_MIN
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'SWAP_POINT_MIN';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add SWAP_POINT_MIN NUMBER(15,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.SWAP_POINT_MIN
        is ''掉期点最小值''';
  end if;
end;
/

--增加字段语句 SWAP_POINT_MAX
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'SWAP_POINT_MAX';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add SWAP_POINT_MAX NUMBER(15,6)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.SWAP_POINT_MAX
        is ''掉期点最大值''';
  end if;
end;
/

--增加字段语句 IS_PAYMENT_AMOUNT
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'IS_PAYMENT_AMOUNT';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add IS_PAYMENT_AMOUNT VARCHAR2(10)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.IS_PAYMENT_AMOUNT
        is ''金额是否自动计算''';
  end if;
end;
/

--增加字段语句 PREMIUM_CUR
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'PREMIUM_CUR';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PREMIUM_CUR VARCHAR2(32)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PREMIUM_CUR
        is ''期权费币种''';
  end if;
end;
/

--增加字段语句 PREMIUM_POINT_MIN
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'PREMIUM_POINT_MIN';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PREMIUM_POINT_MIN NUMBER(15,6)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PREMIUM_POINT_MIN
        is ''期权费点数最小值''';
  end if;
end;
/

--增加字段语句 PREMIUM_POINT_MAX
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'PREMIUM_POINT_MAX';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PREMIUM_POINT_MAX NUMBER(15,6)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PREMIUM_POINT_MAX
        is ''期权费点数最大值''';
  end if;
end;
/

--增加字段语句 PREMIUM_MONEY_MIN
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'PREMIUM_MONEY_MIN';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PREMIUM_MONEY_MIN NUMBER(15,6)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PREMIUM_MONEY_MIN
        is ''期权费最小值''';
  end if;
end;
/

--增加字段语句 PREMIUM_MONEY_MAX
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'PREMIUM_MONEY_MAX';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add PREMIUM_MONEY_MAX NUMBER(15,6)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.PREMIUM_MONEY_MAX
        is ''期权费最大值''';
  end if;
end;
/

--增加字段语句 PREMIUM_MONEY_OF_PREMIUM_CUR
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_REQ'
     and column_name = 'PREMIUM_MONEY_OF_PREMIUM_CUR';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_REQ add PREMIUM_MONEY_OF_PREMIUM_CUR NUMBER(20,6) ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.PREMIUM_MONEY_OF_PREMIUM_CUR
        is ''期权费金额（期权费币种）''';
  end if;
  commit;
end;
/

-- 3. 为FEX_TRAN_REQ表添加或修改PREMIUM_MONEY_OF_PREMIUM_CUR列
DECLARE
v_col_exists NUMBER;
BEGIN
    -- 检查列是否存在
SELECT COUNT(*) INTO v_col_exists
FROM user_tab_columns
WHERE table_name = 'FEX_TRAN_REQ'
  AND column_name = 'PREMIUM_MONEY_OF_PREMIUM_CUR';

IF v_col_exists = 0 THEN
        -- 列不存在则添加
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ ADD PREMIUM_MONEY_OF_PREMIUM_CUR NUMBER(20,6)';
ELSE
        -- 列存在则修改
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY PREMIUM_MONEY_OF_PREMIUM_CUR NUMBER(20,6)';
END IF;
COMMIT;
END;
/

DECLARE
v_col_exists NUMBER;
BEGIN
    -- 检查列是否存在
SELECT COUNT(*) INTO v_col_exists
FROM user_tab_columns
WHERE table_name = 'FEX_TRAN_REQ'
  AND column_name = 'EXCEPT_VAL';

IF v_col_exists = 0 THEN
        -- 列不存在则添加
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ ADD EXCEPT_VAL NUMBER(12,4)';
ELSE
        -- 列存在则修改
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY EXCEPT_VAL NUMBER(12,4)';
END IF;
COMMIT;
END;
/

-- 4. 为FEX_TRAN_LIFE_CYCLE表添加或修改AVG_RATE列
DECLARE
v_col_exists NUMBER;
BEGIN
    -- 检查列是否存在
SELECT COUNT(*) INTO v_col_exists
FROM user_tab_columns
WHERE table_name = 'FEX_TRAN_LIFE_CYCLE'
  AND column_name = 'AVG_RATE';

IF v_col_exists = 0 THEN
        -- 列不存在则添加
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_LIFE_CYCLE ADD AVG_RATE NUMBER(20,8)';
ELSE
        -- 列存在则修改
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_LIFE_CYCLE MODIFY AVG_RATE NUMBER(20,8)';
END IF;
COMMIT;
END;
/

DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
  --查看该表中该字段是否存在
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'IS_CHECK_RECEIPT';
--如果小于1则说明不存在,则新增此字段
IF VN_COUNT < 1 THEN
    VC_STR := ' ALTER TABLE bt_bank_acc add IS_CHECK_RECEIPT VARCHAR2(10)';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'comment on column bt_bank_acc.IS_CHECK_RECEIPT is ''是否核查''';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

--增加字段语句 IS_AUTO_DELIVERY
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_PROFIT_LOSS_MANAGE'
     and column_name = 'REFERENCE_INTEREST_RATE';
  if v_count < 1 then
    v_str := 'alter table FEX_PROFIT_LOSS_MANAGE add REFERENCE_INTEREST_RATE  NUMBER(20,8)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.REFERENCE_INTEREST_RATE
        is ''参考利率''';
  end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'CONVERT_DATE';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE ADD CONVERT_DATE TIMESTAMP(6)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.CONVERT_DATE is ''折本位币报价时间''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'CONVERT_CUR_EN_CODE';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE ADD CONVERT_CUR_EN_CODE VARCHAR2(50)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.CONVERT_CUR_EN_CODE is ''折本位币报价方式''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'QUOTATION_METHOD';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE ADD QUOTATION_METHOD VARCHAR2(50)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.QUOTATION_METHOD is ''报价方式''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE_DTL'
  and column_name = 'LIFE_CYCLE_ID';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE_DTL ADD LIFE_CYCLE_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE_DTL.LIFE_CYCLE_ID is ''生命周期id''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE_DTL'
  and column_name = 'LIFE_CYCLE_BILL_NO';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE_DTL ADD LIFE_CYCLE_BILL_NO VARCHAR2(50)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE_DTL.LIFE_CYCLE_BILL_NO is ''生命周期单号''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE_DTL'
  and column_name = 'REQ_ID';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE_DTL ADD REQ_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE_DTL.LIFE_CYCLE_ID is ''交易单据id''';
end if;
end;
/

DECLARE
v_col_exists NUMBER;
BEGIN
    -- 检查列是否存在
SELECT COUNT(*) INTO v_col_exists
FROM user_tab_columns
WHERE table_name = 'FEX_TRAN_APP_DTL_TMP'
  AND column_name = 'BUY_MONEY';
IF v_col_exists = 0 THEN
        -- 列不存在则添加
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP ADD BUY_MONEY NUMBER(20,6)';
ELSE
        -- 列存在则修改
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP MODIFY BUY_MONEY NUMBER(20,6)';
END IF;
COMMIT;
END;
/

DECLARE
v_col_exists NUMBER;
BEGIN
    -- 检查列是否存在
SELECT COUNT(*) INTO v_col_exists
FROM user_tab_columns
WHERE table_name = 'FEX_TRAN_APP_DTL_TMP'
  AND column_name = 'SELL_MONEY';
IF v_col_exists = 0 THEN
        -- 列不存在则添加
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP ADD SELL_MONEY NUMBER(20,6)';
ELSE
        -- 列存在则修改
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP MODIFY SELL_MONEY NUMBER(20,6)';
END IF;
COMMIT;
END;
/

DECLARE
v_col_exists NUMBER;
BEGIN
    -- 检查列是否存在
SELECT COUNT(*) INTO v_col_exists
FROM user_tab_columns
WHERE table_name = 'FEX_TRAN_APP_DTL'
  AND column_name = 'BUY_MONEY';
IF v_col_exists = 0 THEN
        -- 列不存在则添加
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL ADD BUY_MONEY NUMBER(20,6)';
ELSE
        -- 列存在则修改
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL MODIFY BUY_MONEY NUMBER(20,6)';
END IF;
COMMIT;
END;
/

DECLARE
v_col_exists NUMBER;
BEGIN
    -- 检查列是否存在
SELECT COUNT(*) INTO v_col_exists
FROM user_tab_columns
WHERE table_name = 'FEX_TRAN_APP_DTL'
  AND column_name = 'SELL_MONEY';
IF v_col_exists = 0 THEN
        -- 列不存在则添加
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL ADD SELL_MONEY NUMBER(20,6)';
ELSE
        -- 列存在则修改
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL MODIFY SELL_MONEY NUMBER(20,6)';
END IF;
COMMIT;
END;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'QUOTATION_CUR_ID';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE ADD  QUOTATION_CUR_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.QUOTATION_CUR_ID is ''报价方式标准币种''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'QUOTATION_REFER_CUR_ID';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE ADD  QUOTATION_REFER_CUR_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.QUOTATION_REFER_CUR_ID is ''报价方式报价币种''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'CONVERT_CUR_ID';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE ADD  CONVERT_CUR_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.CONVERT_CUR_ID is ''折本位币标准币种''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'CONVERT_CUR_REFER_ID';
if v_count < 1 then
    v_str := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE ADD  CONVERT_CUR_REFER_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.CONVERT_CUR_REFER_ID is ''折本位币报价币种''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'CREATE_BY';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add CREATE_BY VARCHAR2(100)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.CREATE_BY
        is ''创建人''';
  end if;
  commit;
end;
/

--增加字段语句 CREATE_DATE
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL_TMP'
     and column_name = 'CREATE_DATE';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add CREATE_DATE TIMESTAMP';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.CREATE_DATE
        is ''创建时间''';
  end if;
  commit;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'CREATE_BY';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add CREATE_BY VARCHAR2(100)';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.CREATE_BY
        is ''创建人''';
  end if;
  commit;
end;
/

--增加字段语句 QUOTATION_METHOD
declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_TRAN_APP_DTL'
     and column_name = 'CREATE_DATE';
  if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL add CREATE_DATE TIMESTAMP';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL.CREATE_DATE
        is ''''';
  end if;
  commit;
end;
/

--增加字段语句 TERM
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_LIFE_CYCLE'
  and column_name = 'POSITION_MONEY';
if v_count < 1 then
    v_str := 'alter table FEX_TRAN_LIFE_CYCLE add POSITION_MONEY NUMBER(20,6) ';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_LIFE_CYCLE.POSITION_MONEY
        is ''持仓金额''';
end if;
end;
/

-- ===== migrated from fex/BFS.T3.4.0000.2025112901.Beta_ORCL.sql : DDL =====
-- -00062025110967_ORCL.sql-
--修改人:chenqin
--修改时间:2025-11-29
--修改内容:损益表字段新增

declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_PROFIT_LOSS_MANAGE'
     and column_name = 'NET_REAL_MONEY_TOTAL';
  if v_count < 1 then
    v_str := 'alter table FEX_PROFIT_LOSS_MANAGE add NET_REAL_MONEY_TOTAL NUMBER(20,2)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.NET_REAL_MONEY_TOTAL
        is ''已实现浮动净损益''';
  end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_PROFIT_LOSS_MANAGE'
     and column_name = 'NET_CONVERT_REAL_MONEY_TOTAL';
  if v_count < 1 then
    v_str := 'alter table FEX_PROFIT_LOSS_MANAGE add NET_CONVERT_REAL_MONEY_TOTAL NUMBER(20,2)  ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.NET_CONVERT_REAL_MONEY_TOTAL
        is ''折本位币已实现净损益''';
  end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
  select count(*)
    into v_count
    from user_tab_columns
   where table_name = 'FEX_PROFIT_LOSS_MANAGE'
     and column_name = 'TRAN_TYPE';
  if v_count < 1 then
    v_str := 'alter table FEX_PROFIT_LOSS_MANAGE add TRAN_TYPE VARCHAR2(10) ';
    EXECUTE IMMEDIATE v_str;
    EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.TRAN_TYPE
        is ''交易类型''';
  end if;
end;
/

-- =00062025110967_ORCL.sql=

-- -00062025110968_ORCL.sql-
--修改人:wuxc
--修改时间:2025-11-29
--修改内容:交易需求表字段新增

--增加字段语句 END_SETTLE_DATE
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'END_SETTLE_DATE';
if v_count < 1 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP add END_SETTLE_DATE DATE ';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_APP_DTL_TMP.END_SETTLE_DATE
        is ''远端结算日期''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'SETTLE_DATE';
if v_count < 1 then
    v_str := 'alter table FEX_PROFIT_LOSS_MANAGE add SETTLE_DATE DATE ';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.SETTLE_DATE
        is ''结算日期''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'LIFE_CYCLE';
if v_count < 1 then
    v_str := 'alter table FEX_PROFIT_LOSS_MANAGE add LIFE_CYCLE VARCHAR2(32) ';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.LIFE_CYCLE
        is ''生命周期ID''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PROFIT_LOSS_MANAGE'
  and column_name = 'CONTRACT_RATE';
if v_count < 1 then
    v_str := 'alter table FEX_PROFIT_LOSS_MANAGE add CONTRACT_RATE DECIMAL(17,8) ';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PROFIT_LOSS_MANAGE.CONTRACT_RATE
        is ''合同汇率''';
end if;
end;
/

-- ===== migrated from fex/BFS.T3.4.0000.2026061701.Beta_ORCL.sql : DDL =====
-- -00062026063001_ORCL.sql-
-- 修改人:wuxc
-- 修改内容:境外资金需求
-- 修改时间:2026年5月11日


-- 表说明：交易页面配置
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE FEX_TRAN_REQ_CONFIG';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE FEX_TRAN_REQ_CONFIG
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    template_config_version_id VARCHAR2(32)  NULL,
    template_id VARCHAR2(32)  NULL,
    version_no VARCHAR2(32)  NULL,
    field_id VARCHAR2(32) NULL,
    field_code VARCHAR2(50) NULL,
    field_name VARCHAR2(255) NULL,
    field_sort NUMBER(11) NULL,
    field_length NUMBER(11) DEFAULT 0 NULL,
    field_limit_val VARCHAR2(255) NULL,
    field_def_val VARCHAR2(255) NULL,
    field_dict_group VARCHAR2(255) NULL,
    field_limited_dict VARCHAR2(255) NULL,
    field_area VARCHAR2(50) NULL,
    field_parent_code VARCHAR2(50) NULL,
    field_content VARCHAR2(4000) NULL,
    data_area_layout VARCHAR2(50) NULL,
    data_type VARCHAR2(50) NULL,
    data_class VARCHAR2(50) NULL,
    is_sys NUMBER(4)  DEFAULT 0 NULL,
    is_use NUMBER(4)  DEFAULT 0 NULL,
    is_visible NUMBER(4)  DEFAULT 0 NOT NULL,
    is_required NUMBER(4)  DEFAULT 0 NOT NULL,
    is_length NUMBER(4)  DEFAULT 0 NOT NULL,
    is_limited_dict NUMBER(4)  DEFAULT 0 NULL,
    is_exc NUMBER(4)  DEFAULT 0,
    is_deleted NUMBER(4)  DEFAULT 0 NULL,
    status VARCHAR2(10) NULL,
    CREATE_BY VARCHAR2(32) NULL,
    CREATE_DATE DATE NULL,
    UPDATE_BY VARCHAR2(32) NULL,
    UPDATE_DATE DATE NULL
);

COMMENT ON TABLE FEX_TRAN_REQ_CONFIG IS '交易页面配置';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.template_config_version_id IS '模板配置版本ID';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.template_id IS '模板ID';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.version_no IS '版本号';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_id IS '自定义字段ID';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_code IS '字段编码';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_name IS '字段名称';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_sort IS '排序';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_length IS '长度限制';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_limit_val IS '限制值';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_def_val IS '默认值';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_dict_group IS '枚举组';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_limited_dict IS '限制枚举值';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_area IS '字段所属区域';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.field_parent_code IS '父级编码';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.data_area_layout IS '字段所属区域布局';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.data_type IS '字段类型';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.data_class IS '字段类';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.is_sys IS '是否预设字段（1-预设字段;2-自定义字段）';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.is_use IS '是否启用';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.is_visible IS '是否可见';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.is_required IS '是否必填';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.is_length IS '是否控制长度';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.is_limited_dict IS '是否限制枚举值';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.is_exc IS '是否发送银行指令';
COMMENT ON COLUMN FEX_TRAN_REQ_CONFIG.status IS '状态';

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ' and column_name = 'TEMPLATE_ID';
if v_count < 1 then
    v_str := 'alter table FEX_TRAN_REQ add TEMPLATE_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.TEMPLATE_ID is ''模版ID''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CUST_FIELD_RULE' and column_name = 'FIELD_CODE';
if v_count < 1 then
    v_str := 'alter table FEX_CUST_FIELD_RULE add FIELD_CODE VARCHAR2(255)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CUST_FIELD_RULE.TEMPLATE_ID is ''字段编码''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CUST_FIELD_RULE' and column_name = 'DICT_GROUP';
if v_count < 1 then
    v_str := 'alter table FEX_CUST_FIELD_RULE add DICT_GROUP VARCHAR2(100)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CUST_FIELD_RULE.TEMPLATE_ID is ''字典组''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'BANK_EXTRA';
if v_count < 1 then
    EXECUTE IMMEDIATE 'alter table BIS_FOREIGN_EXC add BANK_EXTRA clob';
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.BANK_EXTRA is ''银行额外参数（数据格式：JSON）''';
end if;
end;
/

declare
v_count number;
begin
    -- TEMPLATE_NAME
select count(*) into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ' and column_name = 'TEMPLATE_NAME';
if v_count < 1 then
        execute immediate 'alter table FEX_TRAN_REQ add TEMPLATE_NAME VARCHAR2(32) NULL';
execute immediate 'comment on column FEX_TRAN_REQ.TEMPLATE_NAME is ''模版名称''';
end if;

    -- VERSION_NO
select count(*) into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ' and column_name = 'VERSION_NO';
if v_count < 1 then
        execute immediate 'alter table FEX_TRAN_REQ add VERSION_NO VARCHAR2(32) NULL';
execute immediate 'comment on column FEX_TRAN_REQ.VERSION_NO is ''模板版本号''';
end if;

    -- BANK_PAY_TYPE_NAME
select count(*) into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ' and column_name = 'BANK_PAY_TYPE_NAME';
if v_count < 1 then
        execute immediate 'alter table FEX_TRAN_REQ add BANK_PAY_TYPE_NAME VARCHAR2(32) NULL';
execute immediate 'comment on column FEX_TRAN_REQ.BANK_PAY_TYPE_NAME is ''支付类型ID''';
end if;
end;
/

UPDATE tenant_pre_menu SET NAME = '银行支付类型配置',DESCRIPTION = '银行支付类型配置'
WHERE id = 'becda80945744bc5971ad814866a7055';

UPDATE sys_internation_base SET LANG_CONTEXT='银行支付类型配置' WHERE KEY_ID='becda80945744bc5971ad814866a7055' AND LANG_CODE='zh_CN';
UPDATE sys_internation_base SET LANG_CONTEXT='Bank Payment Type Configuration' WHERE KEY_ID='becda80945744bc5971ad814866a7055' AND LANG_CODE='en_US';


UPDATE fex_tran_req_field SET field_name = '跨行标识' WHERE id = 'e7aca230d5550356048e427f9dceba01';
UPDATE fex_tran_req_field SET field_name='主体单位' WHERE id = '7d0a1591d5f654fccfb01919e13aad14';
UPDATE fex_tran_req_field SET field_name='主体单位账户币种' WHERE id='55034c00a7c6bbef2b564b90ff64f694';
UPDATE fex_tran_req_field SET field_name='主体单位账户类别' WHERE id='4393ebf5304de05e3ac56a0eaee58aed';
UPDATE fex_tran_req_field SET field_name='被代收/代付单位' WHERE id='f075aeaa732c2b3640c7349bc8082a0f';
COMMIT;


-- 创建或替换存储过程
DECLARE
    v_count NUMBER;
    v_str VARCHAR2(1000);
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'BANK_PAY_TYPE_ID';
    IF v_count < 1 THEN
                v_str := 'alter table FEX_TRAN_REQ add BANK_PAY_TYPE_ID VARCHAR2(32) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.BANK_PAY_TYPE_ID is ''支付类型ID''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查GOODS_AMOUNT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'GOODS_AMOUNT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add GOODS_AMOUNT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.GOODS_AMOUNT is ''货物贸易金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查ADVANCE_CHARGE_AMOUNT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'ADVANCE_CHARGE_AMOUNT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add ADVANCE_CHARGE_AMOUNT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.ADVANCE_CHARGE_AMOUNT is ''预付货款项下金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查ADVANCE_CONTRACT_RATE字段是否存在（比例按小数入库，需保留6位小数，否则百分位小数被截断）
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'ADVANCE_CONTRACT_RATE';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add ADVANCE_CONTRACT_RATE NUMBER(15,6) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.ADVANCE_CONTRACT_RATE is ''预付货款项下所占合同金额比例''';
    EXECUTE IMMEDIATE v_str;
    ELSE
            v_str := 'alter table FEX_TRAN_REQ modify ADVANCE_CONTRACT_RATE NUMBER(15,6)';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查RPT_GATE_DATE字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'RPT_GATE_DATE';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add RPT_GATE_DATE DECIMAL(10, 0) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.RPT_GATE_DATE is ''预计X天后报关''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查RPT_GATE_ENT_ID字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'RPT_GATE_ENT_ID';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add RPT_GATE_ENT_ID VARCHAR2(32) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.RPT_GATE_ENT_ID is ''（已报关）报关经营单位名称''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查RPT_GATE_TYPE字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'RPT_GATE_TYPE';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add RPT_GATE_TYPE VARCHAR2(1) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.RPT_GATE_TYPE is ''（已报关）报关方式：人民币报关/外币报关''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查GENERAL_TRADE_AMOUNT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'GENERAL_TRADE_AMOUNT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add GENERAL_TRADE_AMOUNT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.GENERAL_TRADE_AMOUNT is ''一般贸易金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查FEED_AMOUNT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'FEED_AMOUNT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add FEED_AMOUNT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.FEED_AMOUNT is ''进料加工金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查OTHER_TRADE_AMOUNT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'OTHER_TRADE_AMOUNT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add OTHER_TRADE_AMOUNT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.OTHER_TRADE_AMOUNT is ''其他贸易金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查BORDER_TRADE_AMOUNT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'BORDER_TRADE_AMOUNT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add BORDER_TRADE_AMOUNT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.BORDER_TRADE_AMOUNT is ''边境贸易金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查CUSTOM_AMOUNT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'CUSTOM_AMOUNT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add CUSTOM_AMOUNT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.CUSTOM_AMOUNT is ''海关特殊监管区域及保税监管场所进出境物流货物金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查OFFSHORE_AMOUNT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'OFFSHORE_AMOUNT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add OFFSHORE_AMOUNT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.OFFSHORE_AMOUNT is ''离岸转手买卖金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查OTHER_AMOUNT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'OTHER_AMOUNT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add OTHER_AMOUNT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.OTHER_AMOUNT is ''其他金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查INVEST_AMT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'INVEST_AMT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add INVEST_AMT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.INVEST_AMT is ''投资收益金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查INVEST_NO字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'INVEST_NO';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add INVEST_NO VARCHAR2(20) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.INVEST_NO is ''投资收益 - 国际收支编码''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查INVEST_APPLY_NBR字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'INVEST_APPLY_NBR';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add INVEST_APPLY_NBR VARCHAR2(30) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.INVEST_APPLY_NBR is ''投资收益 - 批准证书号''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查OFTEN_AMT字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'OFTEN_AMT';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add OFTEN_AMT DECIMAL(15, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.OFTEN_AMT is ''经常转移金额''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查OFTEN_NO字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'OFTEN_NO';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add OFTEN_NO VARCHAR2(20) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.OFTEN_NO is ''经常转移 - 国际收支编码''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查DIRECT_INVESTMENT_CERT_NO字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'DIRECT_INVESTMENT_CERT_NO';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add DIRECT_INVESTMENT_CERT_NO VARCHAR2(20) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.DIRECT_INVESTMENT_CERT_NO is ''直接投资 - 批准证书号''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查ADVANCE_RATE字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'ADVANCE_RATE';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add ADVANCE_RATE DECIMAL(10, 2) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.ADVANCE_RATE is ''预收款比例（涉及服务贸易时填写）''';
    EXECUTE IMMEDIATE v_str;
    END IF;

    -- 检查CHECK_DAY字段是否存在
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'CHECK_DAY';
    IF v_count < 1 THEN
            v_str := 'alter table FEX_TRAN_REQ add CHECK_DAY DECIMAL(10, 0) NULL';
    EXECUTE IMMEDIATE v_str;
    v_str := 'comment on column FEX_TRAN_REQ.CHECK_DAY is ''结账期（涉及服务贸易时填写）''';
    EXECUTE IMMEDIATE v_str;
    END IF;
END;
/

DECLARE
v_count NUMBER;
BEGIN
    -- 添加 TEMPLATE_ID 字段
SELECT COUNT(*) INTO v_count FROM user_tab_cols WHERE table_name = 'FEX_TRAN_APP_DTL' AND column_name = 'TEMPLATE_ID';
IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL ADD TEMPLATE_ID VARCHAR2(32) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL.TEMPLATE_ID IS ''模板ID''';
END IF;

    -- 添加 TEMPLATE_NAME 字段
SELECT COUNT(*) INTO v_count FROM user_tab_cols WHERE table_name = 'FEX_TRAN_APP_DTL' AND column_name = 'TEMPLATE_NAME';
IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL ADD TEMPLATE_NAME VARCHAR2(200) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL.TEMPLATE_NAME IS ''模板名称''';
END IF;

    -- 添加 BANK_PAY_TYPE 字段
SELECT COUNT(*) INTO v_count FROM user_tab_cols WHERE table_name = 'FEX_TRAN_APP_DTL' AND column_name = 'BANK_PAY_TYPE';
IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL ADD BANK_PAY_TYPE VARCHAR2(100) NULL';
        EXECUTE IMMEDIATE  'COMMENT ON COLUMN FEX_TRAN_APP_DTL.BANK_PAY_TYPE IS ''支付类型编码（对应银行自定义上送代码字段）''';
END IF;

    -- 添加 BANK_PAY_TYPE_NAME 字段
SELECT COUNT(*) INTO v_count FROM user_tab_cols WHERE table_name = 'FEX_TRAN_APP_DTL' AND column_name = 'BANK_PAY_TYPE_NAME';
IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL ADD BANK_PAY_TYPE_NAME VARCHAR2(100) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL.BANK_PAY_TYPE_NAME IS ''支付类型名称''';
END IF;
END;
/

DECLARE
v_count NUMBER;
BEGIN
    -- 添加 TEMPLATE_ID 字段
SELECT COUNT(*) INTO v_count FROM user_tab_cols WHERE table_name = 'FEX_TRAN_APP_DTL_TMP' AND column_name = 'TEMPLATE_ID';
IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP ADD TEMPLATE_ID VARCHAR2(32) NULL';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL_TMP.TEMPLATE_ID IS ''模板ID''';
END IF;

    -- 添加 TEMPLATE_NAME 字段
SELECT COUNT(*) INTO v_count FROM user_tab_cols WHERE table_name = 'FEX_TRAN_APP_DTL_TMP' AND column_name = 'TEMPLATE_NAME';
IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP ADD TEMPLATE_NAME VARCHAR2(200) NULL';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL_TMP.TEMPLATE_NAME IS ''模板名称''';
END IF;

    -- 添加 BANK_PAY_TYPE 字段
SELECT COUNT(*) INTO v_count FROM user_tab_cols WHERE table_name = 'FEX_TRAN_APP_DTL_TMP' AND column_name = 'BANK_PAY_TYPE';
IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP ADD BANK_PAY_TYPE VARCHAR2(100) NULL';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL_TMP.BANK_PAY_TYPE IS ''支付类型编码（对应银行自定义上送代码字段）''';
END IF;

    -- 添加 BANK_PAY_TYPE_NAME 字段
SELECT COUNT(*) INTO v_count FROM user_tab_cols WHERE table_name = 'FEX_TRAN_APP_DTL_TMP' AND column_name = 'BANK_PAY_TYPE_NAME';
IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP ADD BANK_PAY_TYPE_NAME VARCHAR2(100) NULL';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL_TMP.BANK_PAY_TYPE_NAME IS ''支付类型名称''';
END IF;
END;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CUST_FIELD_RULE' and column_name = 'LABEL_BANK';
if v_count < 1 then
    v_str := 'alter table FEX_CUST_FIELD_RULE add LABEL_BANK VARCHAR2(255)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CUST_FIELD_RULE.LABEL_BANK is ''银行标签''';
end if;
end;
/

-- 创建添加字段的存储过程


-- 存储过程说明：add_column_if_not_exists
CREATE OR REPLACE PROCEDURE add_column_if_not_exists(
    p_table_name IN VARCHAR2,
    p_column_name IN VARCHAR2,
    p_column_def IN VARCHAR2
) IS
    v_count NUMBER;
    v_sql VARCHAR2(2000);
BEGIN
    -- 检查字段是否存在
SELECT COUNT(*)
INTO v_count
FROM user_tab_cols
WHERE table_name = UPPER(p_table_name)
  AND column_name = UPPER(p_column_name);

-- 如果字段不存在,则添加
IF v_count = 0 THEN
        v_sql := 'ALTER TABLE ' || p_table_name || ' ADD (' || p_column_name || ' ' || p_column_def || ')';
EXECUTE IMMEDIATE v_sql;
DBMS_OUTPUT.PUT_LINE('字段 ' || p_column_name || ' 已添加到表 ' || p_table_name);
ELSE
        DBMS_OUTPUT.PUT_LINE('字段 ' || p_column_name || ' 已存在,跳过添加');
END IF;
END;
/

-- 修改fex_tran_app_dtl BANK_PAY_TYPE_ID字段
declare
v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL'
  and column_name = 'BANK_PAY_TYPE_ID';
if v_count > 0 then
    v_str := 'alter table FEX_TRAN_APP_DTL modify BANK_PAY_TYPE_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

-- 修改fex_tran_app_dtl_tmp BANK_PAY_TYPE_ID字段
declare
v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_APP_DTL_TMP'
  and column_name = 'BANK_PAY_TYPE_ID';
if v_count > 0 then
    v_str := 'alter table FEX_TRAN_APP_DTL_TMP modify BANK_PAY_TYPE_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

-- 修改fex_tran_req TEMPLATE_NAME字段
declare
v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'TEMPLATE_NAME';
if v_count > 0 then
    v_str := 'alter table FEX_TRAN_REQ modify TEMPLATE_NAME VARCHAR2(200)';
EXECUTE IMMEDIATE v_str;
end if;
end;
/

-- 外汇指令表新增字段：货物贸易金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'GOODS_AMOUNT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add GOODS_AMOUNT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify GOODS_AMOUNT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.GOODS_AMOUNT is ''货物贸易金额''';
end;
/

-- 外汇指令表新增字段：预付货款项下金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'ADVANCE_CHARGE_AMOUNT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add ADVANCE_CHARGE_AMOUNT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify ADVANCE_CHARGE_AMOUNT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.ADVANCE_CHARGE_AMOUNT is ''预付货款项下金额''';
end;
/

-- 外汇指令表新增字段：预付货款项下所占合同金额比例
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'ADVANCE_CONTRACT_RATE';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add ADVANCE_CONTRACT_RATE NUMBER(15,6)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify ADVANCE_CONTRACT_RATE NUMBER(15,6)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.ADVANCE_CONTRACT_RATE is ''预付货款项下所占合同金额比例''';
end;
/

-- 外汇指令表新增字段：预计X天后报关
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'RPT_GATE_DATE';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add RPT_GATE_DATE NUMBER(10,0)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify RPT_GATE_DATE NUMBER(10,0)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.RPT_GATE_DATE is ''预计X天后报关''';
end;
/

-- 外汇指令表新增字段：报关经营单位
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'RPT_GATE_ENT_NAME';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add RPT_GATE_ENT_NAME VARCHAR2(100)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify RPT_GATE_ENT_NAME VARCHAR2(100)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.RPT_GATE_ENT_NAME is ''报关经营单位''';
end;
/

-- 外汇指令表新增字段：组织机构代码
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'RPT_GATE_ENT_NO';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add RPT_GATE_ENT_NO VARCHAR2(10)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify RPT_GATE_ENT_NO VARCHAR2(10)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.RPT_GATE_ENT_NO is ''组织机构代码''';
end;
/

-- 外汇指令表新增字段：报关方式 R-人民币;O-外币
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'RPT_GATE_TYPE';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add RPT_GATE_TYPE VARCHAR2(1)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify RPT_GATE_TYPE VARCHAR2(1)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.RPT_GATE_TYPE is ''报关方式 R-人民币;O-外币''';
end;
/

-- 外汇指令表新增字段：一般贸易金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'GENERAL_TRADE_AMOUNT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add GENERAL_TRADE_AMOUNT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify GENERAL_TRADE_AMOUNT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.GENERAL_TRADE_AMOUNT is ''一般贸易金额''';
end;
/

-- 外汇指令表新增字段：进料加工金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'FEED_AMOUNT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add FEED_AMOUNT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify FEED_AMOUNT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.FEED_AMOUNT is ''进料加工金额''';
end;
/

-- 外汇指令表新增字段：其他贸易金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'OTHER_TRADE_AMOUNT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add OTHER_TRADE_AMOUNT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify OTHER_TRADE_AMOUNT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OTHER_TRADE_AMOUNT is ''其他贸易金额''';
end;
/

-- 外汇指令表新增字段：边境贸易金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'BORDER_TRADE_AMOUNT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add BORDER_TRADE_AMOUNT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify BORDER_TRADE_AMOUNT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.BORDER_TRADE_AMOUNT is ''边境贸易金额''';
end;
/

-- 外汇指令表新增字段：海关特殊监管区域及保税监管场所进出境物流货物金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'CUSTOM_AMOUNT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add CUSTOM_AMOUNT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify CUSTOM_AMOUNT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.CUSTOM_AMOUNT is ''海关特殊监管区域及保税监管场所进出境物流货物金额''';
end;
/

-- 外汇指令表新增字段：离岸转手买卖金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'OFFSHORE_AMOUNT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add OFFSHORE_AMOUNT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify OFFSHORE_AMOUNT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OFFSHORE_AMOUNT is ''离岸转手买卖金额''';
end;
/

-- 外汇指令表新增字段：其他金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'OTHER_AMOUNT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add OTHER_AMOUNT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify OTHER_AMOUNT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OTHER_AMOUNT is ''其他金额''';
end;
/

-- 外汇指令表新增字段：投资收益金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'INVEST_AMT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add INVEST_AMT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify INVEST_AMT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.INVEST_AMT is ''投资收益金额''';
end;
/

-- 外汇指令表新增字段：投资收益-国际收支编码
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'INVEST_NO';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add INVEST_NO VARCHAR2(20)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify INVEST_NO VARCHAR2(20)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.INVEST_NO is ''投资收益-国际收支编码''';
end;
/

-- 外汇指令表新增字段：投资收益-批准证书号
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'INVEST_APPLY_NBR';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add INVEST_APPLY_NBR VARCHAR2(30)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify INVEST_APPLY_NBR VARCHAR2(30)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.INVEST_APPLY_NBR is ''投资收益-批准证书号''';
end;
/

-- 外汇指令表新增字段：经常转移金额
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'OFTEN_AMT';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add OFTEN_AMT NUMBER(15,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify OFTEN_AMT NUMBER(15,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OFTEN_AMT is ''经常转移金额''';
end;
/

-- 外汇指令表新增字段：经常转移-国际收支编码
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'OFTEN_NO';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add OFTEN_NO VARCHAR2(20)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify OFTEN_NO VARCHAR2(20)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.OFTEN_NO is ''经常转移-国际收支编码''';
end;
/

-- 外汇指令表新增字段：直接投资-批准证书号
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'DIRECT_INVESTMENT_CERT_NO';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add DIRECT_INVESTMENT_CERT_NO VARCHAR2(20)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify DIRECT_INVESTMENT_CERT_NO VARCHAR2(20)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.DIRECT_INVESTMENT_CERT_NO is ''直接投资-批准证书号''';
end;
/

-- 外汇指令表新增字段：预收款比例
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'ADVANCE_RATE';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add ADVANCE_RATE NUMBER(10,2)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify ADVANCE_RATE NUMBER(10,2)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.ADVANCE_RATE is ''预收款比例''';
end;
/

-- 外汇指令表新增字段：结账期
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC' and column_name = 'CHECK_DAY';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add CHECK_DAY NUMBER(10,0)';
else
    v_str := 'alter table BIS_FOREIGN_EXC modify CHECK_DAY NUMBER(10,0)';
end if;
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.CHECK_DAY is ''结账期''';
end;
/

DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ' AND COLUMN_NAME = 'RMK';
IF VN_COUNT = 1 THEN
    VC_STR := ' ALTER TABLE FEX_TRAN_REQ MODIFY RMK VARCHAR2(500)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ' AND COLUMN_NAME = 'RMK_LOCAL';
IF VN_COUNT = 1 THEN
    VC_STR := ' ALTER TABLE FEX_TRAN_REQ MODIFY RMK_LOCAL VARCHAR2(500)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FBS_BUDGET_PROCESS' AND COLUMN_NAME = 'PURPOSE';
IF VN_COUNT = 1 THEN
    VC_STR := ' ALTER TABLE FBS_BUDGET_PROCESS MODIFY PURPOSE VARCHAR2(500)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

--外汇牌价表增加“生效日期”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_RATE_INFO'
  and column_name = 'EFFECT_DATE';
if v_count < 1 then
    v_str := 'alter table FEX_RATE_INFO add EFFECT_DATE TIMESTAMP';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_RATE_INFO.EFFECT_DATE is ''生效日期''';
end if;
end;
/

--外汇牌价表增加“报价金额”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_RATE_INFO'
  and column_name = 'CUR_UNIT';
if v_count < 1 then
    v_str := 'alter table FEX_RATE_INFO add CUR_UNIT VARCHAR2(50)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_RATE_INFO.CUR_UNIT is ''报价金额''';
end if;
end;
/

--外汇指令表增加“扣费币种”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'DED_ACC_CUR_ID';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add DED_ACC_CUR_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.DED_ACC_CUR_ID is ''扣费币种''';
end if;
end;
/

--银行类别表增加“是否配置业务模式编号”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BT_BANK_TYPE'
  and column_name = 'IS_SET_SUB_MODE';
if v_count < 1 then
    v_str := 'alter table BT_BANK_TYPE add IS_SET_SUB_MODE VARCHAR2(10)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE.IS_SET_SUB_MODE is ''是否配置业务模式编号''';
end if;
end;
/

--银行类别表增加“业务模式编号应用范围”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BT_BANK_TYPE'
  and column_name = 'APPLAY_SCOPE_SUB_MODE';
if v_count < 1 then
    v_str := 'alter table BT_BANK_TYPE add APPLAY_SCOPE_SUB_MODE VARCHAR2(60)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE.APPLAY_SCOPE_SUB_MODE is ''业务模式编号应用范围''';
end if;
end;
/

--银行类别表增加“业务模式编号是否必填”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BT_BANK_TYPE'
  and column_name = 'REQUIRED_SUB_MODE';
if v_count < 1 then
    v_str := 'alter table BT_BANK_TYPE add REQUIRED_SUB_MODE VARCHAR2(10)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE.REQUIRED_SUB_MODE is ''业务模式编号应用范围''';
end if;
end;
/

--外汇交易表增加“业务模式编号”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_TRAN_REQ'
  and column_name = 'SUB_MODE';
if v_count < 1 then
    v_str := 'alter table FEX_TRAN_REQ add SUB_MODE VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_TRAN_REQ.SUB_MODE is ''业务模式编号''';
end if;
end;
/

--外汇指令表增加“业务模式编号”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'SUB_MODE';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add SUB_MODE VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.SUB_MODE is ''业务模式编号''';
end if;
end;
/

--客商资料表增加“账户所在国家”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'BANK_COUNTRY_ID';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add BANK_COUNTRY_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.BANK_COUNTRY_ID is ''账户所在国家''';
end if;
end;
/

-- 修改字段长度
DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
  --查看该表中该字段是否存在
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_RATE_INFO' AND COLUMN_NAME = 'PURCHASE_RATE';
--如果等于1则说明存在,则修复此字段长度
IF VN_COUNT = 1 THEN
    VC_STR := 'ALTER TABLE FEX_RATE_INFO modify (PURCHASE_RATE NUMBER(20,8))';
EXECUTE IMMEDIATE VC_STR;
END IF;
commit;
END;
/

DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
  --查看该表中该字段是否存在
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_RATE_INFO' AND COLUMN_NAME = 'SELL_RATE';
--如果等于1则说明存在,则修复此字段长度
IF VN_COUNT = 1 THEN
    VC_STR := 'ALTER TABLE FEX_RATE_INFO modify (SELL_RATE NUMBER(20,8))';
EXECUTE IMMEDIATE VC_STR;
END IF;
commit;
END;
/

DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
  --查看该表中该字段是否存在
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_RATE_INFO' AND COLUMN_NAME = 'MIDDLE_RATE';
--如果等于1则说明存在,则修复此字段长度
IF VN_COUNT = 1 THEN
    VC_STR := 'ALTER TABLE FEX_RATE_INFO modify (MIDDLE_RATE NUMBER(20,8))';
EXECUTE IMMEDIATE VC_STR;
END IF;
commit;
END;
/

--外汇指令表增加交易汇率类型字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'TRAN_RATE_TYPE';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add TRAN_RATE_TYPE VARCHAR2(10)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.TRAN_RATE_TYPE is ''交易汇率类型''';
end if;
end;
/


--外汇指令表添加指令来源字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'EXC_SOURCE';
if v_count < 1 then
    v_str := 'alter table BIS_FOREIGN_EXC add EXC_SOURCE VARCHAR2(10)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.EXC_SOURCE is ''指令来源 REQ-外汇交易,CASH_FLOW-现金流''';
end if;
end;
/

-- ===== migrated from fex/BFS.T3.4.0000.2026062201.Beta_ORCL.sql : DDL =====
-- 为 fex_tran_app_dtl 表添加字段（如果不存在）
DECLARE
v_count NUMBER;
    v_sql VARCHAR2(2000);
BEGIN
SELECT COUNT(*)
INTO v_count
FROM user_tab_cols
WHERE table_name = UPPER('fex_tran_app_dtl')
  AND column_name = UPPER('BANK_PAY_TYPE_ID');

IF v_count = 0 THEN
        v_sql := 'ALTER TABLE fex_tran_app_dtl ADD BANK_PAY_TYPE_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_sql;
DBMS_OUTPUT.PUT_LINE('BANK_PAY_TYPE_ID 新增成功');
ELSE
        DBMS_OUTPUT.PUT_LINE('字段已存在,无需新增');
END IF;
END;
/

-- 为 fex_tran_app_dtl_tmp 表添加字段（如果不存在）
DECLARE
v_count NUMBER;
    v_sql VARCHAR2(2000);
BEGIN
SELECT COUNT(*)
INTO v_count
FROM user_tab_cols
WHERE table_name = UPPER('fex_tran_app_dtl_tmp')
  AND column_name = UPPER('BANK_PAY_TYPE_ID');

IF v_count = 0 THEN
        v_sql := 'ALTER TABLE fex_tran_app_dtl_tmp ADD BANK_PAY_TYPE_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_sql;
DBMS_OUTPUT.PUT_LINE('BANK_PAY_TYPE_ID 新增成功');
ELSE
        DBMS_OUTPUT.PUT_LINE('字段已存在,无需新增');
END IF;
END;
/

-- 修改 fex_tran_req 表的字段类型（如果字段存在）
DECLARE
v_count NUMBER;
    v_sql VARCHAR2(2000);
BEGIN
SELECT COUNT(*)
INTO v_count
FROM user_tab_cols
WHERE table_name = UPPER('fex_tran_req')
  AND column_name = UPPER('TEMPLATE_NAME');

IF v_count > 0 THEN
        v_sql := 'ALTER TABLE fex_tran_req MODIFY (TEMPLATE_NAME VARCHAR2(200))';
EXECUTE IMMEDIATE v_sql;
DBMS_OUTPUT.PUT_LINE('TEMPLATE_NAME 修改成功');
ELSE
        DBMS_OUTPUT.PUT_LINE('字段不存在,跳过修改');
END IF;
END;
/

-- 为 bt_bank_type_custom 表添加字段（如果不存在）
DECLARE
v_count NUMBER;
    v_sql VARCHAR2(2000);
BEGIN
SELECT COUNT(*)
INTO v_count
FROM user_tab_cols
WHERE table_name = UPPER('bt_bank_type_custom')
  AND column_name = UPPER('IS_DELETED');

IF v_count = 0 THEN
        v_sql := 'ALTER TABLE bt_bank_type_custom ADD IS_DELETED VARCHAR2(1)';
EXECUTE IMMEDIATE v_sql;
DBMS_OUTPUT.PUT_LINE('IS_DELETED 新增成功');
ELSE
        DBMS_OUTPUT.PUT_LINE('字段已存在,无需新增');
END IF;
END;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'SWIFT_CODE';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add SWIFT_CODE VARCHAR2(100)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.SWIFT_CODE is ''SWIFT CODE''';
end if;
end;
/

-- ===== migrated from fex/BFS.T3.4.0000.2026062701.Beta_ORCL.sql : DDL =====
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'SWIFT_CODE';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add SWIFT_CODE VARCHAR2(100)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.SWIFT_CODE is ''SWIFT CODE''';
end if;
end;
/


DECLARE
v_count NUMBER;
    v_str VARCHAR2(1000);
BEGIN
SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'BIS_FOREIGN_EXC' AND column_name = 'ADVANCE_RATE';
IF v_count > 0 THEN
        v_str := 'alter table BIS_FOREIGN_EXC modify ADVANCE_RATE NUMBER(10,0)';
EXECUTE IMMEDIATE v_str;
-- 更新字段注释
v_str := 'comment on column BIS_FOREIGN_EXC.ADVANCE_RATE is ''预收款比例（涉及服务贸易时填写）''';
EXECUTE IMMEDIATE v_str;
END IF;
END;
/

DECLARE
v_count NUMBER;
    v_str VARCHAR2(1000);
BEGIN
SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'FEX_TRAN_REQ' AND column_name = 'ADVANCE_RATE';
IF v_count > 0 THEN
        v_str := 'alter table FEX_TRAN_REQ modify ADVANCE_RATE NUMBER(10,2)';
EXECUTE IMMEDIATE v_str;
-- 更新字段注释
v_str := 'comment on column FEX_TRAN_REQ.ADVANCE_RATE is ''预收款比例（涉及服务贸易时填写）''';
EXECUTE IMMEDIATE v_str;
END IF;
END;
/

-- ====== From sql/saas/oracle/fex/1.sql (DDL) ======

-- 修改交易对手账户表新增中转行字段长度
DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
  --查看该表中该字段是否存在
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_COTP_BANKACC' AND COLUMN_NAME = 'TRAN_BANK';
--如果小于1则说明不存在,则新增此字段
IF VN_COUNT > 0 THEN
    VC_STR := ' alter table FEX_COTP_BANKACC MODIFY TRAN_BANK VARCHAR2(255)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/
--交易对手账户表新增“银行名称”
DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_COTP_BANKACC' AND COLUMN_NAME = 'BANK_NAME';
IF VN_COUNT < 1 THEN
    VC_STR := ' ALTER TABLE FEX_COTP_BANKACC ADD BANK_NAME VARCHAR2(255)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/
--交易对手表新增“省名称”字段
DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_COTP' AND COLUMN_NAME = 'PROV_NAME';
IF VN_COUNT < 1 THEN
    VC_STR := ' ALTER TABLE FEX_COTP ADD PROV_NAME VARCHAR2(255)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/
--交易对手表新增“市名称”字段
DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_COTP' AND COLUMN_NAME = 'CITY_NAME';
IF VN_COUNT < 1 THEN
    VC_STR := ' ALTER TABLE FEX_COTP ADD CITY_NAME VARCHAR2(255)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/
--修改人:tongqr
--修改时间:2022-05-19
--修改内容:增加国际化

--在FEX_COTP表中增加字段:OPP_ID(交易对手数据源id)
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_COTP'
  and column_name = 'OPP_ID';
if v_count < 1 then
    v_str := 'alter table FEX_COTP add OPP_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_COTP.OPP_ID
        is ''交易对手数据源id''';
end if;
end;
/
--修改字段长度
DECLARE
VN_COUNT     NUMBER;
  VC_STR       VARCHAR2(1000);
BEGIN
SELECT COUNT(*)
INTO VN_COUNT
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'FEX_COTP' AND COLUMN_NAME = 'COTP_NAME';
IF VN_COUNT = 1 THEN
    VC_STR := 'ALTER TABLE FEX_COTP MODIFY COTP_NAME VARCHAR2(300)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/
--在FEX_CORP_BANKACC_DTL表中增加字段:PAY_MODEL
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CORP_BANKACC_DTL'
  and column_name = 'PAY_MODEL';
if v_count < 1 then
    v_str := 'alter table FEX_CORP_BANKACC_DTL add PAY_MODEL VARCHAR2(10)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CORP_BANKACC_DTL.PAY_MODEL
        is ''收款账户类型''';
end if;
end;
/
--在FEX_CORP_BANKACC_DTL表中增加字段:PAY_VALUE
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_CORP_BANKACC_DTL'
  and column_name = 'PAY_VALUE';
if v_count < 1 then
    v_str := 'alter table FEX_CORP_BANKACC_DTL add PAY_VALUE VARCHAR2(100)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_CORP_BANKACC_DTL.PAY_VALUE
        is ''快捷支付信息''';
end if;
end;
/
--客商信息表新增“客商名称（英文）”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP'
  and column_name = 'NAME_EN';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP add NAME_EN VARCHAR2(200) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP.NAME_EN is ''账户名称（英文）''';
end if;
end;
/
--客商信息表新增“地址（英文）”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP'
  and column_name = 'ADDRESS_EN';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP add ADDRESS_EN VARCHAR2(200) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP.ADDRESS_EN is ''地址（英文）''';
end if;
end;
/
--客商信息表新增“地址1”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP'
  and column_name = 'ADDRESS1';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP add ADDRESS1 VARCHAR2(200) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP.ADDRESS1 is ''地址1''';
end if;
end;
/
--客商信息表新增“地址2”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP'
  and column_name = 'ADDRESS2';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP add ADDRESS2 VARCHAR2(200) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP.ADDRESS2 is ''地址2''';
end if;
end;
/
--客商信息表新增“所在省/州”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP'
  and column_name = 'PROVINCE_STATE';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP add PROVINCE_STATE VARCHAR2(200) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP.PROVINCE_STATE is ''所在省/州''';
end if;
end;
/
--客商信息表新增“邮编”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP'
  and column_name = 'ZIP_CODE';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP add ZIP_CODE VARCHAR2(50) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP.ZIP_CODE is ''邮编''';
end if;
end;
/
--客商信息关联账号表新增“关联账户名称”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'EXTERNAL_ACC_NAME';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add EXTERNAL_ACC_NAME VARCHAR2(200) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.EXTERNAL_ACC_NAME is ''关联账户名称''';
end if;
end;
/
--客商信息关联账号表新增“账户币种”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'CUR_ID';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add CUR_ID VARCHAR2(32)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.CUR_ID is ''账户币种''';
end if;
end;
/
--客商信息关联账号表新增“支付指令类型”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'PAY_EXC_TYPE';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add PAY_EXC_TYPE CHAR(1) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.PAY_EXC_TYPE is ''支付指令类型 1-标准结算指令''';
end if;
end;
/
--客商信息关联账号表新增“中转行”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'INTERMEDIARY_BANK';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add INTERMEDIARY_BANK VARCHAR2(200) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.INTERMEDIARY_BANK is ''中转行''';
end if;
end;
/
--客商信息关联账号表新增“中转行SWIFT CODE”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'INTERMEDIARY_BANK_SWIFT_CODE';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add INTERMEDIARY_BANK_SWIFT_CODE VARCHAR2(100) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.INTERMEDIARY_BANK_SWIFT_CODE is ''中转行SWIFT CODE''';
end if;
end;
/
--客商信息关联账号表新增“中转行账号”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'INTERMEDIARY_BANK_ACC';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add INTERMEDIARY_BANK_ACC VARCHAR2(50) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.INTERMEDIARY_BANK_ACC is ''中转行账号''';
end if;
end;
/
--客商信息关联账号表新增“SWIFT CODE”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'SWIFT_CODE';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add SWIFT_CODE VARCHAR2(100) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.SWIFT_CODE is ''SWIFT CODE''';
end if;
end;
/
--客商信息关联账号表新增“本地清算号”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_EXTERNAL_CORP_BANKACC'
  and column_name = 'LOCAL_CLEAR_NO';
if v_count < 1 then
    v_str := 'alter table SYS_EXTERNAL_CORP_BANKACC add LOCAL_CLEAR_NO VARCHAR2(30) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column SYS_EXTERNAL_CORP_BANKACC.LOCAL_CLEAR_NO is ''本地清算号''';
end if;
end;
/
--境外银行行号信息表新增“银行类型”字段
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BT_SWIFT_INFO'
  and column_name = 'BANK_TYPE_ID';
if v_count < 1 then
    v_str := 'alter table BT_SWIFT_INFO add BANK_TYPE_ID VARCHAR2(32) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BT_SWIFT_INFO.BANK_TYPE_ID is ''银行类型''';
end if;
end;
/
-- 修改内容：新增客商快捷支付信息表


-- 表说明：客商资料快捷支付信息表
DECLARE
VN_COUNT NUMBER;
    VC_STR VARCHAR2(2000);
BEGIN
    -- 检查表是否已存在
SELECT COUNT(*)
INTO VN_COUNT
FROM user_tables
WHERE TABLE_NAME = 'SYS_EXTERNAL_CORP_QUICK_PAY';

-- 如果表不存在则创建
IF VN_COUNT < 1 THEN
        VC_STR := 'CREATE TABLE sys_external_corp_quick_pay  (
            ID VARCHAR2(32) NOT NULL,
            EXTERNAL_CORP_ID VARCHAR2(32) NULL,
            PRO_ID VARCHAR2(32) DEFAULT NULL,
            COUNTRY_CODE VARCHAR2(50) DEFAULT NULL,
            EMAIL VARCHAR2(40) DEFAULT NULL,
            TEL VARCHAR2(50) DEFAULT NULL,
            FPS_ID VARCHAR2(50) DEFAULT NULL,
            CREDIT_CARD VARCHAR2(50) DEFAULT NULL,
            HK_ID VARCHAR2(50) DEFAULT NULL,
            IS_DEFAULT_ACC CHAR(1) DEFAULT NULL,
            CREATE_DATE TIMESTAMP(6) DEFAULT NULL,
            CREATE_BY VARCHAR2(50) DEFAULT NULL,
            UPDATE_DATE TIMESTAMP(6) DEFAULT NULL,
            UPDATE_BY VARCHAR2(50) DEFAULT NULL,
            constraint pk_sys_external_corp_quick_pay primary key (id)
        )';
EXECUTE IMMEDIATE VC_STR;

-- 添加表注释
VC_STR := 'COMMENT ON TABLE  sys_external_corp_quick_pay IS ''客商资料快捷支付信息表''';
EXECUTE IMMEDIATE VC_STR;

-- 添加列注释
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.ID IS ''主键''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.EXTERNAL_CORP_ID IS ''客商ID''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.PRO_ID IS ''资金收付产品ID''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.COUNTRY_CODE IS ''国家代码''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.EMAIL IS ''邮箱''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.TEL IS ''电话号码''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.FPS_ID IS ''FPS ID''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.CREDIT_CARD IS ''信用卡号''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.HK_ID IS ''香港身份证ID''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.IS_DEFAULT_ACC IS ''首选账号''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.CREATE_BY IS ''创建人''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.CREATE_DATE IS ''创建时间''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.UPDATE_BY IS ''更新人''';
EXECUTE IMMEDIATE VC_STR;
VC_STR := 'COMMENT ON COLUMN sys_external_corp_quick_pay.UPDATE_DATE IS ''更新时间''';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/
-- 客商资料关联账号表新增“中转行地址”字段
DECLARE
VN_COUNT NUMBER;
  VC_STR   VARCHAR2(1000);
BEGIN
  -- 查看该表中该字段是否存在
SELECT COUNT(*)
INTO VN_COUNT
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'SYS_EXTERNAL_CORP_BANKACC'
  AND COLUMN_NAME = 'INTERMEDIARY_BANK_ADDRESS';

-- 如果小于1则说明不存在，则新增此字段
IF VN_COUNT < 1 THEN
    -- 添加新字段（注意Oracle不需要DEFAULT NULL）
    VC_STR := 'ALTER TABLE SYS_EXTERNAL_CORP_BANKACC ADD INTERMEDIARY_BANK_ADDRESS VARCHAR2(200)';
EXECUTE IMMEDIATE VC_STR;

-- 添加列注释（注意双单引号转义）
VC_STR := 'COMMENT ON COLUMN SYS_EXTERNAL_CORP_BANKACC.INTERMEDIARY_BANK_ADDRESS IS ''中转行地址''';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/
--在BIS_FOREIGN_EXC表中增加字段:SETTLE_BUS_TYPE(结汇业务类型)
declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BIS_FOREIGN_EXC'
  and column_name = 'SETTLE_BUS_TYPE';
if v_count < 1 then
    v_str := 'ALTER TABLE BIS_FOREIGN_EXC ADD  SETTLE_BUS_TYPE VARCHAR2(10) DEFAULT NULL';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column BIS_FOREIGN_EXC.SETTLE_BUS_TYPE is ''结汇业务类型''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PRODUCT'
  and column_name = 'IS_ALLOWED_DEL';
if v_count < 1 then
    v_str := 'alter table FEX_PRODUCT add IS_ALLOWED_DEL VARCHAR2(1) DEFAULT 1';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FEX_PRODUCT.IS_ALLOWED_DEL is ''是否可以删除 1-是 0-否''';
end if;
end;
/
-- ====== 新增TENANT_ID租户字段 ======

-- BT_BANK_TYPE_CUSTOM 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'BT_BANK_TYPE_CUSTOM' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE BT_BANK_TYPE_CUSTOM ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_COTP_BANKACC_CUR 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_COTP_BANKACC_CUR' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_COTP_BANKACC_CUR ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_PROFIT_LOSS_MANAGE 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_PROFIT_LOSS_MANAGE' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_PROFIT_LOSS_MANAGE_DTL 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_PROFIT_LOSS_MANAGE_DTL' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE_DTL ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_TRAN_CONFIG_OPERATION_LOG 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_CONFIG_OPERATION_LOG' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_CONFIG_OPERATION_LOG ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_TRAN_LIFE_CYCLE 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_LIFE_CYCLE' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_LIFE_CYCLE ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_TRAN_REQ_CONFIG 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ_CONFIG' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_REQ_CONFIG ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_TRAN_REQ_CONFIG_VERSION 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ_CONFIG_VERSION' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_REQ_CONFIG_VERSION ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_TRAN_REQ_EXT 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ_EXT' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_REQ_EXT ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_TRAN_REQ_FIELD 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ_FIELD' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_REQ_FIELD ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_TRAN_TEMPLATE_CONFIG 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_TEMPLATE_CONFIG' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_TEMPLATE_CONFIG ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- SYS_EXTERNAL_CORP_QUICK_PAY 新增TENANT_ID
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'SYS_EXTERNAL_CORP_QUICK_PAY' AND COLUMN_NAME = 'TENANT_ID';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE SYS_EXTERNAL_CORP_QUICK_PAY ADD TENANT_ID VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/


-- SYS_EXTERNAL_CORP_BANKACC.BANK_COUNTRY_ID 长度改为65
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'SYS_EXTERNAL_CORP_BANKACC' AND COLUMN_NAME = 'BANK_COUNTRY_ID' AND DATA_TYPE = 'VARCHAR2' AND CHAR_LENGTH < 65;
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE SYS_EXTERNAL_CORP_BANKACC MODIFY BANK_COUNTRY_ID VARCHAR2(65)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- BT_INPUT_BANK_INFO 新增SWIFT_CODE字段
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'BT_INPUT_BANK_INFO' AND COLUMN_NAME = 'SWIFT_CODE';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE BT_INPUT_BANK_INFO ADD SWIFT_CODE VARCHAR2(100)';
EXECUTE IMMEDIATE VC_STR;
EXECUTE IMMEDIATE 'comment on column BT_INPUT_BANK_INFO.SWIFT_CODE is ''SWIFT代码''';
END IF;
END;
/


-- 表说明：银行自定义其他信息配置表
declare
v_count number;
  v_str varchar2(1000);
begin
select count(*) into v_count from user_tables where table_name = 'BT_BANK_TYPE_OTHER_CUSTOM';
if v_count < 1 then
    v_str := 'create table BT_BANK_TYPE_OTHER_CUSTOM  (
        ID                   VARCHAR2(32)                   not null,
        BANK_TYPE_ID         VARCHAR2(32),
        BIF_CODE             VARCHAR2(32),
        APPLAY_SCOPE         VARCHAR2(30),
        COUNTRY_ID           VARCHAR2(500),
        REMIT_TYPE           VARCHAR2(30),
        NEED_FIELD           VARCHAR2(100),
        REQUIRED_FIELD       VARCHAR2(100),
        TENANT_ID            VARCHAR2(32),
        constraint PK_BT_BANK_TYPE_OTHER_CUSTOM primary key(ID)
      )';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on table BT_BANK_TYPE_OTHER_CUSTOM is ''银行自定义其他信息配置表''';
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE_OTHER_CUSTOM.BANK_TYPE_ID is ''银行类型ID''';
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE_OTHER_CUSTOM.BIF_CODE is ''接口代码ID''';
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE_OTHER_CUSTOM.APPLAY_SCOPE is ''适用页面''';
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE_OTHER_CUSTOM.COUNTRY_ID is ''适用国家ID''';
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE_OTHER_CUSTOM.REMIT_TYPE is ''汇款类型''';
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE_OTHER_CUSTOM.NEED_FIELD is ''所需字段''';
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE_OTHER_CUSTOM.REQUIRED_FIELD is ''必填字段''';
EXECUTE IMMEDIATE 'comment on column BT_BANK_TYPE_OTHER_CUSTOM.TENANT_ID is ''租户ID''';
end if;
end;
/

declare
v_count number;
begin
select count(*) into v_count from user_tab_columns
where table_name = 'BT_BANK_TYPE_OTHER_CUSTOM'
  and column_name = 'TENANT_ID';
if v_count < 1 then
    execute immediate 'alter table BT_BANK_TYPE_OTHER_CUSTOM add TENANT_ID VARCHAR2(32)';
    execute immediate 'comment on column BT_BANK_TYPE_OTHER_CUSTOM.TENANT_ID is ''租户ID''';
end if;
end;
/

declare v_count number;
v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BT_SWIFT_INFO'
  and column_name = 'LOCAL_CLEAR_NO';
if v_count < 1 then
    EXECUTE IMMEDIATE 'alter table BT_SWIFT_INFO add LOCAL_CLEAR_NO VARCHAR2(30)';
EXECUTE IMMEDIATE 'comment on column BT_SWIFT_INFO.LOCAL_CLEAR_NO is ''本地清算号''';
end if;
end;
/


declare
v_count number;
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'BT_BANK_TYPE'
  and column_name = 'APPLAY_SCOPE_BANK_PAY_TYPE';
if v_count < 1 then
        EXECUTE IMMEDIATE 'alter table BT_BANK_TYPE add APPLAY_SCOPE_BANK_PAY_TYPE VARCHAR2(60)';
else
        EXECUTE IMMEDIATE 'alter table BT_BANK_TYPE modify APPLAY_SCOPE_BANK_PAY_TYPE VARCHAR2(60)';
end if;


select count(*)
into v_count
from user_tab_columns
where table_name = 'BT_BANK_TYPE'
  and column_name = 'REQUIRED_BANK_PAY_TYPE';
if v_count < 1 then
        EXECUTE IMMEDIATE 'alter table BT_BANK_TYPE add REQUIRED_BANK_PAY_TYPE VARCHAR2(10)';
else
        EXECUTE IMMEDIATE 'alter table BT_BANK_TYPE modify REQUIRED_BANK_PAY_TYPE VARCHAR2(10)';
end if;


select count(*)
into v_count
from user_tab_columns
where table_name = 'BT_BANK_TYPE'
  and column_name = 'IS_SET_BANK_PAY_TYPE';
if v_count < 1 then
        EXECUTE IMMEDIATE 'alter table BT_BANK_TYPE add IS_SET_BANK_PAY_TYPE VARCHAR2(10)';
else
        EXECUTE IMMEDIATE 'alter table BT_BANK_TYPE modify IS_SET_BANK_PAY_TYPE VARCHAR2(10)';
end if;
end;
/

declare
v_count number;
v_length number;
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PRODUCT'
  and column_name = 'ID';
if v_count = 1 then
    select char_length
    into v_length
    from user_tab_columns
    where table_name = 'FEX_PRODUCT'
      and column_name = 'ID';
    if v_length < 65 then
        EXECUTE IMMEDIATE 'alter table FEX_PRODUCT modify ID VARCHAR2(65)';
    end if;
end if;

select count(*)
into v_count
from user_tab_columns
where table_name = 'FEX_PRODUCT'
  and column_name = 'PRO_ID';
if v_count = 1 then
    select char_length
    into v_length
    from user_tab_columns
    where table_name = 'FEX_PRODUCT'
      and column_name = 'PRO_ID';
    if v_length < 65 then
        EXECUTE IMMEDIATE 'alter table FEX_PRODUCT modify PRO_ID VARCHAR2(65)';
    end if;
end if;
end;
/


-- 表说明：租户外汇产品预置表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE TENANT_P_FEX_PRODUCT';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE TENANT_P_FEX_PRODUCT
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    PRO_LEVEL VARCHAR2(1) NOT NULL,
    TRAN_TYPE VARCHAR2(1) NOT NULL,
    PRO_NAME VARCHAR2(60) NOT NULL,
    PRO_DESC VARCHAR2(100) NULL,
    PRO_ID VARCHAR2(65) NULL,
    FEX_BUS_TYPE VARCHAR2(1) NULL,
    FEX_TRAN_TYPE VARCHAR2(1) NULL,
    DELIVERY_TYPE VARCHAR2(1) NULL,
    DELIVERY_CUR_ID VARCHAR2(32) NULL,
    IS_ALLOWED_DELIVERY VARCHAR2(1) DEFAULT '0' NULL,
    IS_ALLOWED_SETTLE VARCHAR2(1) DEFAULT '0' NULL,
    BUY_CUR_ID VARCHAR2(32) NULL,
    SELL_CUR_ID VARCHAR2(32) NULL,
    NON_WORKDAY_RULE VARCHAR2(30) NULL,
    TRANGROUP_RULE_ID VARCHAR2(32) NULL,
    IS_ALLOWED_CHANGE VARCHAR2(1) NULL,
    VALID_SIGN VARCHAR2(1) DEFAULT '1' NULL,
    RMK VARCHAR2(200) NULL,
    CREATE_DATE DATE NULL,
    CREATE_BY VARCHAR2(50) NULL,
    UPDATE_DATE DATE NULL,
    UPDATE_BY VARCHAR2(50) NULL,
    FEX_OPTION1 VARCHAR2(1) NULL,
    FEX_OPTION2 VARCHAR2(1) NULL,
    FEX_OPTION3 VARCHAR2(1) NULL,
    FEX_OPTION_TYPE VARCHAR2(1) NULL,
    FEX_OPTION_RATE VARCHAR2(1) NULL,
    REFUND_TIME VARCHAR2(1) NULL,
    DAYS_RULE VARCHAR2(1) NULL,
    VALID_STANDARD VARCHAR2(1) NULL,
    TRAN_SETTLE_METHOD VARCHAR2(1) NULL,
    IS_SHOW_AGENT VARCHAR2(1) NULL,
    IS_SHOW_CLEAR_HOUSE VARCHAR2(1) NULL,
    IS_AUTO_DELIVERY VARCHAR2(1) DEFAULT '0' NOT NULL,
    IS_ALLOWED_DEL VARCHAR2(1) DEFAULT '1' NULL,
    IS_INIT CHAR(1) DEFAULT '1' NULL
);

COMMENT ON TABLE TENANT_P_FEX_PRODUCT IS '租户外汇产品预置表';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.PRO_LEVEL IS '层级 0-产品组 1-产品';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.TRAN_TYPE IS '交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.PRO_NAME IS '名称';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.PRO_DESC IS '描述';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.PRO_ID IS '所属组ID';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.FEX_BUS_TYPE IS '外汇业务类型 1-结汇、2-售汇、3-外汇买卖';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.FEX_TRAN_TYPE IS '外汇交易类型 0-即期、1-远期';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.DELIVERY_TYPE IS '交割类型 0-本金交割、1-无本金交割';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.DELIVERY_CUR_ID IS '交割币种';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.IS_ALLOWED_DELIVERY IS '是否允许多次交割 1-是 0-否';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.IS_ALLOWED_SETTLE IS '非工作日是否允许结算 1-是 0-否';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.NON_WORKDAY_RULE IS '非工作日适用原则 1-币种、2-交易主体单位所在地、3-主体银行所在地、4-交易对手所在地';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.IS_ALLOWED_CHANGE IS '交易组规则是否可以在交易组中更改 1-是 0-否';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.VALID_SIGN IS '有效标志 1-是 0-否';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.IS_AUTO_DELIVERY IS '即期是否自动交割 1-是 0-否';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.IS_ALLOWED_DEL IS '是否可以删除 1-是 0-否';
COMMENT ON COLUMN TENANT_P_FEX_PRODUCT.IS_INIT IS '是否初始化';

-- SYS_EXTERNAL_CORP_QUICK_PAY、FEX_TRAN_REQ、FEX_TRAN_APP_DTL.PRO_ID长度改为65
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'SYS_EXTERNAL_CORP_QUICK_PAY' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE SYS_EXTERNAL_CORP_QUICK_PAY MODIFY PRO_ID VARCHAR2(65)';
EXECUTE IMMEDIATE VC_STR;
END IF;

SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_REQ MODIFY PRO_ID VARCHAR2(65)';
EXECUTE IMMEDIATE VC_STR;
END IF;

SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_APP_DTL' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_APP_DTL MODIFY PRO_ID VARCHAR2(65)';
EXECUTE IMMEDIATE VC_STR;
END IF;

SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_CORP_BANKACC' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE FEX_CORP_BANKACC MODIFY PRO_ID VARCHAR2(65)';
EXECUTE IMMEDIATE VC_STR;
END IF;

SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'BIS_FOREIGN_EXC' AND COLUMN_NAME = 'PAYEE_PROV' AND CHAR_LENGTH < 65;
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE BIS_FOREIGN_EXC MODIFY PAYEE_PROV VARCHAR2(65)';
EXECUTE IMMEDIATE VC_STR;
END IF;

SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_CORP_BANKACC_DTL' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE FEX_CORP_BANKACC_DTL MODIFY PRO_ID VARCHAR2(65)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_TRAN_LIFE_CYCLE、FEX_PROFIT_LOSS_MANAGE_DTL.OP_TYPE改为VARCHAR2(32)
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_LIFE_CYCLE' AND COLUMN_NAME = 'OP_TYPE' AND (DATA_TYPE <> 'VARCHAR2' OR CHAR_LENGTH < 32);
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_LIFE_CYCLE MODIFY OP_TYPE VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;

SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_PROFIT_LOSS_MANAGE_DTL' AND COLUMN_NAME = 'OP_TYPE' AND (DATA_TYPE <> 'VARCHAR2' OR CHAR_LENGTH < 32);
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE_DTL MODIFY OP_TYPE VARCHAR2(32)';
EXECUTE IMMEDIATE VC_STR;
END IF;

SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ_FIELD' AND COLUMN_NAME = 'FIELD_NAME' AND CHAR_LENGTH < 100;
IF VN_COUNT > 0 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_REQ_FIELD MODIFY FIELD_NAME VARCHAR2(100)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/

-- FEX_TRAN_REQ.ADVANCE_CONTRACT_RATE精度修正：比例按小数入库，NUMBER(19,2)会导致百分位小数被截断
DECLARE
VN_COUNT NUMBER;
  VC_STR VARCHAR2(1000);
BEGIN
SELECT COUNT(1) INTO VN_COUNT FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ' AND COLUMN_NAME = 'ADVANCE_CONTRACT_RATE';
IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_TRAN_REQ ADD ADVANCE_CONTRACT_RATE NUMBER(15,6)';
EXECUTE IMMEDIATE VC_STR;
EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_REQ.ADVANCE_CONTRACT_RATE IS ''预付货款项下所占合同金额比例''';
ELSE
    VC_STR := 'ALTER TABLE FEX_TRAN_REQ MODIFY ADVANCE_CONTRACT_RATE NUMBER(15,6)';
EXECUTE IMMEDIATE VC_STR;
END IF;
END;
/


-- CONVERT_RATE 折本位币汇率字段精度调整：NUMBER(8,6) -> NUMBER(20,8)
-- 修复外汇期权交易中折本位币汇率输入过长导致提交报错的问题
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_PROFIT_LOSS_MANAGE'
      AND COLUMN_NAME = 'CONVERT_RATE'
      AND DATA_PRECISION = 8;

    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE MODIFY CONVERT_RATE NUMBER(20,8)';
    END IF;

    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_LIFE_CYCLE'
      AND COLUMN_NAME = 'CONVERT_RATE'
      AND DATA_PRECISION = 8;

    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_LIFE_CYCLE MODIFY CONVERT_RATE NUMBER(20,8)';
    END IF;

    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_REQ_EXT'
      AND COLUMN_NAME = 'CONVERT_RATE'
      AND DATA_PRECISION = 8;

    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ_EXT MODIFY CONVERT_RATE NUMBER(20,8)';
    END IF;
END;
/
-- 扩大自定义字段值长度，支持长文本输入
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_CUST_INFO' AND COLUMN_NAME = 'FIELD_VAL' AND DATA_LENGTH = 200;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_CUST_INFO MODIFY FIELD_VAL VARCHAR2(2000)';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_CUST_INFO_TENANT' AND COLUMN_NAME = 'FIELD_VAL' AND DATA_LENGTH = 200;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_CUST_INFO_TENANT MODIFY FIELD_VAL VARCHAR2(2000)';
    END IF;
END;
/

-- 扩大结算自动填充规则产品ID长度，支持多产品拼接
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_SETTLE_FILL_RULE' AND COLUMN_NAME = 'PRO_ID' AND DATA_LENGTH = 32;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_SETTLE_FILL_RULE MODIFY PRO_ID VARCHAR2(65)';
    END IF;
END;
/

-- 扩大投资收益/经常转移/直接投资编码字段长度，支持更长编码/证书号输入
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ' AND COLUMN_NAME = 'INVEST_NO' AND DATA_LENGTH = 20;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY INVEST_NO VARCHAR2(200)';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ' AND COLUMN_NAME = 'INVEST_APPLY_NBR' AND DATA_LENGTH = 30;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY INVEST_APPLY_NBR VARCHAR2(200)';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ' AND COLUMN_NAME = 'OFTEN_NO' AND DATA_LENGTH = 20;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY OFTEN_NO VARCHAR2(200)';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_TRAN_REQ' AND COLUMN_NAME = 'DIRECT_INVESTMENT_CERT_NO' AND DATA_LENGTH = 20;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY DIRECT_INVESTMENT_CERT_NO VARCHAR2(200)';
    END IF;
END;
/

-- 扩大结算自动填充规则补充字段名称/字段内容长度，支持更长输入
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_SETTLE_FILL_RULE_ADD' AND COLUMN_NAME = 'FIELD_NAME' AND DATA_LENGTH = 60;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_SETTLE_FILL_RULE_ADD MODIFY FIELD_NAME VARCHAR2(200)';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'FEX_SETTLE_FILL_RULE_ADD' AND COLUMN_NAME = 'FIELD_VAL' AND DATA_LENGTH = 100;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_SETTLE_FILL_RULE_ADD MODIFY FIELD_VAL VARCHAR2(200)';
    END IF;
END;
/

-- 客商资料关联账号表新增“银行地址”字段
DECLARE
VN_COUNT NUMBER;
  VC_STR   VARCHAR2(1000);
BEGIN
  SELECT COUNT(*)
  INTO VN_COUNT
  FROM USER_TAB_COLUMNS
  WHERE TABLE_NAME = 'SYS_EXTERNAL_CORP_BANKACC'
    AND COLUMN_NAME = 'BANK_ADDR';

  IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE SYS_EXTERNAL_CORP_BANKACC ADD BANK_ADDR VARCHAR2(200)';
    EXECUTE IMMEDIATE VC_STR;
    VC_STR := 'COMMENT ON COLUMN SYS_EXTERNAL_CORP_BANKACC.BANK_ADDR IS ''银行地址''';
    EXECUTE IMMEDIATE VC_STR;
  END IF;
END;
/

-- FEX_CUST_INFO_TENANT 增加“字段名称”快照，规则删除后详情仍可回显
DECLARE
VN_COUNT NUMBER;
  VC_STR   VARCHAR2(1000);
BEGIN
  SELECT COUNT(*)
  INTO VN_COUNT
  FROM USER_TAB_COLUMNS
  WHERE TABLE_NAME = 'FEX_CUST_INFO_TENANT'
    AND COLUMN_NAME = 'FIELD_NAME';

  IF VN_COUNT < 1 THEN
    VC_STR := 'ALTER TABLE FEX_CUST_INFO_TENANT ADD FIELD_NAME VARCHAR2(60)';
    EXECUTE IMMEDIATE VC_STR;
    VC_STR := 'COMMENT ON COLUMN FEX_CUST_INFO_TENANT.FIELD_NAME IS ''字段名称''';
    EXECUTE IMMEDIATE VC_STR;
  END IF;
END;
/

-- 扩大单位敞口模板币别ID长度，支持币别全选
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_CORP_EXPOSURE_TEMPLATE'
      AND COLUMN_NAME = 'CUR_ID'
      AND CHAR_LENGTH < 4000;

    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_CORP_EXPOSURE_TEMPLATE MODIFY CUR_ID VARCHAR2(4000)';
    END IF;
END;
/

-- 其余仍为varchar2(32)的PRO_ID长度改为65
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_GROUP_RULE_DTL' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_GROUP_RULE_DTL MODIFY PRO_ID VARCHAR2(65)';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_GROUP' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_GROUP MODIFY PRO_ID VARCHAR2(65)';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_OPTION_DEAL' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_OPTION_DEAL MODIFY PRO_ID VARCHAR2(65)';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_CORP_POOL_TRAN' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_CORP_POOL_TRAN MODIFY PRO_ID VARCHAR2(65)';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_APP_DTL_TMP' AND COLUMN_NAME = 'PRO_ID' AND CHAR_LENGTH < 65;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP MODIFY PRO_ID VARCHAR2(65)';
    END IF;
END;
/

-- 内管用户表格使用习惯记录


-- 表说明：内管用户表格使用习惯记录
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE SYS_TABLE_RECORD_MANAGER';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE SYS_TABLE_RECORD_MANAGER
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    USER_ID VARCHAR2(32) NULL,
    TABLE_KEY VARCHAR2(255) NULL,
    CONTENT CLOB NULL
);

COMMENT ON TABLE SYS_TABLE_RECORD_MANAGER IS '内管用户表格使用习惯记录';
COMMENT ON COLUMN SYS_TABLE_RECORD_MANAGER.ID IS '主键';
COMMENT ON COLUMN SYS_TABLE_RECORD_MANAGER.USER_ID IS '用户ID';
COMMENT ON COLUMN SYS_TABLE_RECORD_MANAGER.TABLE_KEY IS '表格的URL作为KEY';
COMMENT ON COLUMN SYS_TABLE_RECORD_MANAGER.CONTENT IS '用户习惯记录JSON结构';

-- 扩大资金收付交易关联交易号码长度，支持多单据合并
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_REQ'
      AND COLUMN_NAME = 'REL_BILL_NO'
      AND CHAR_LENGTH < 500;

    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY REL_BILL_NO VARCHAR2(500)';
    END IF;
END;
/


-- 表说明：租户前台表格默认配置预置表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE TENANT_PRE_TABLE_RECORD';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE TENANT_PRE_TABLE_RECORD
(
    ID        VARCHAR2(32)  NOT NULL PRIMARY KEY,
    USER_ID   VARCHAR2(32)  NULL,
    TABLE_KEY VARCHAR2(255) NOT NULL,
    CONTENT   CLOB          NULL,
    IS_INIT   VARCHAR2(1)   DEFAULT '1' NOT NULL,
    CONSTRAINT UK_TP_TABLE_RECORD_KEY UNIQUE (TABLE_KEY)
);

COMMENT ON TABLE TENANT_PRE_TABLE_RECORD IS '租户前台表格默认配置预置表';
COMMENT ON COLUMN TENANT_PRE_TABLE_RECORD.ID IS '主键';
COMMENT ON COLUMN TENANT_PRE_TABLE_RECORD.USER_ID IS '用户ID';
COMMENT ON COLUMN TENANT_PRE_TABLE_RECORD.TABLE_KEY IS '表格的URL作为KEY';
COMMENT ON COLUMN TENANT_PRE_TABLE_RECORD.CONTENT IS '用户习惯记录JSON结构';
COMMENT ON COLUMN TENANT_PRE_TABLE_RECORD.IS_INIT IS '是否需要给租户初始化';

-- 扩大资金收付交易原单据号、关联交易号码长度，支持最多100笔单据合并
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_REQ'
      AND COLUMN_NAME = 'ORIG_BILL_NO'
      AND CHAR_LENGTH < 4000;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY ORIG_BILL_NO VARCHAR2(4000)';
    END IF;

    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_REQ'
      AND COLUMN_NAME = 'REL_BILL_NO'
      AND CHAR_LENGTH < 4000;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY REL_BILL_NO VARCHAR2(4000)';
    END IF;
END;
/

-- 统一外汇掉期近远端期望汇率为6位小数
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_APP_DTL'
      AND COLUMN_NAME = 'END_WISH_RATE'
      AND (DATA_PRECISION <> 14 OR DATA_SCALE <> 6);
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL MODIFY END_WISH_RATE NUMBER(14,6)';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL.END_WISH_RATE IS ''近端期望汇率''';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_APP_DTL'
      AND COLUMN_NAME = 'REMOTE_END_WISH_RATE_MIN'
      AND (DATA_PRECISION <> 14 OR DATA_SCALE <> 6);
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL MODIFY REMOTE_END_WISH_RATE_MIN NUMBER(14,6)';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL.REMOTE_END_WISH_RATE_MIN IS ''远端期望汇率下限''';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_APP_DTL'
      AND COLUMN_NAME = 'REMOTE_END_WISH_RATE_MAX'
      AND (DATA_PRECISION <> 14 OR DATA_SCALE <> 6);
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL MODIFY REMOTE_END_WISH_RATE_MAX NUMBER(14,6)';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL.REMOTE_END_WISH_RATE_MAX IS ''远端期望汇率上限''';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_APP_DTL_TMP'
      AND COLUMN_NAME = 'END_WISH_RATE'
      AND (DATA_PRECISION <> 14 OR DATA_SCALE <> 6);
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP MODIFY END_WISH_RATE NUMBER(14,6)';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL_TMP.END_WISH_RATE IS ''近端期望汇率''';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_APP_DTL_TMP'
      AND COLUMN_NAME = 'REMOTE_END_WISH_RATE_MIN'
      AND (DATA_PRECISION <> 14 OR DATA_SCALE <> 6);
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP MODIFY REMOTE_END_WISH_RATE_MIN NUMBER(14,6)';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL_TMP.REMOTE_END_WISH_RATE_MIN IS ''远端期望汇率下限''';
    END IF;

    SELECT COUNT(*) INTO v_count FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_APP_DTL_TMP'
      AND COLUMN_NAME = 'REMOTE_END_WISH_RATE_MAX'
      AND (DATA_PRECISION <> 14 OR DATA_SCALE <> 6);
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_APP_DTL_TMP MODIFY REMOTE_END_WISH_RATE_MAX NUMBER(14,6)';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_TRAN_APP_DTL_TMP.REMOTE_END_WISH_RATE_MAX IS ''远端期望汇率上限''';
    END IF;
END;
/

-- 扩大起运国字段长度
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'BIS_FOREIGN_EXC'
      AND COLUMN_NAME = 'ORIGIN_COUNTRY'
      AND CHAR_LENGTH < 65;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE BIS_FOREIGN_EXC MODIFY ORIGIN_COUNTRY VARCHAR2(65)';
    END IF;

    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_REQ'
      AND COLUMN_NAME = 'ORIGIN_COUNTRY'
      AND CHAR_LENGTH < 65;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY ORIGIN_COUNTRY VARCHAR2(65)';
    END IF;
END;
/

-- 损益管理估值汇率不精准：参考利率/估值利率精度扩为 NUMBER(20,6)，
-- 支持牌价中间价5位小数（如 USD/CNY 7.12225），原 NUMBER(19,2) 落库会截断为 7.12
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_PROFIT_LOSS_MANAGE'
      AND COLUMN_NAME = 'REFERENCE_INTEREST_RATE'
      AND DATA_SCALE < 6;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE MODIFY REFERENCE_INTEREST_RATE NUMBER(20,6)';
    END IF;

    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_PROFIT_LOSS_MANAGE'
      AND COLUMN_NAME = 'VALUATION_RATE'
      AND DATA_SCALE < 6;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_PROFIT_LOSS_MANAGE MODIFY VALUATION_RATE NUMBER(20,6)';
    END IF;
END;
/

-- 外汇生命周期结算金额精度不足：MONEY(结算金额)精度扩为 NUMBER(19,6)，
-- 保证"损益=结算金额*记账汇率"可复现（如结算金额 4.168333333 取整为 4.17 后损益会由 29.71 错算成 29.72）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'FEX_TRAN_LIFE_CYCLE'
      AND COLUMN_NAME = 'MONEY'
      AND DATA_SCALE < 6;
    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_LIFE_CYCLE MODIFY MONEY NUMBER(19,6)';
    END IF;
END;
/

-- ============================================
-- Oracle 版本: scf-table-init-oracle.sql
-- 说明: 供应链金融相关表结构初始化脚本
-- ============================================

-- ============================================
-- 1. 应付债权凭证表
-- ============================================


-- 表说明：应付债权凭证表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_payable_voucher';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_payable_voucher
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    voucher_no VARCHAR2(50) NULL,
    voucher_code VARCHAR2(200) NULL,
    template_id VARCHAR2(50) NULL,
    version_id VARCHAR2(32) NULL,
    platform_id VARCHAR2(32) NULL,
    product_id VARCHAR2(32) NULL,
    corp_id VARCHAR2(32) NULL,
    open_date DATE NULL,
    expire_date DATE NULL,
    core_corp VARCHAR2(32) NULL,
    core_corp_name VARCHAR2(100) NULL,
    cur_id VARCHAR2(32) NULL,
    voucher_money NUMBER(18,6) NULL,
    supply_id VARCHAR2(32) NULL,
    supply_name VARCHAR2(200) NULL,
    receive_account VARCHAR2(200) NULL,
    receive_bank VARCHAR2(200) NULL,
    online_sign VARCHAR2(1) NULL,
    quota_code VARCHAR2(50) NULL,
    quota_name VARCHAR2(200) NULL,
    quota_balance NUMBER(18,6) NULL,
    contract_code VARCHAR2(200) NULL,
    contract_name VARCHAR2(200) NULL,
    contract_money NUMBER(18,6) NULL,
    contract_date DATE NULL,
    contract_file VARCHAR2(32) NULL,
    invoice_file VARCHAR2(32) NULL,
    voucher_source VARCHAR2(20) NULL,
    interest_mode VARCHAR2(20) NULL,
    promise_pay_date DATE NULL,
    supply_credit_code VARCHAR2(50) NULL,
    approval_status VARCHAR2(10) NULL,
    voucher_status VARCHAR2(10) NULL,
    pay_id VARCHAR2(32) NULL,
    pay_no VARCHAR2(64) NULL,
    cancel_reason VARCHAR2(500) NULL,
    cancel_date DATE NULL,
    create_by VARCHAR2(50) NULL,
    create_date DATE NULL,
    update_by VARCHAR2(50) NULL,
    update_date DATE NULL,
    tenant_id VARCHAR2(32) NULL
);

COMMENT ON TABLE scf_payable_voucher IS '应付债权凭证表';
COMMENT ON COLUMN scf_payable_voucher.id IS 'id';
COMMENT ON COLUMN scf_payable_voucher.voucher_no IS '单据编号';
COMMENT ON COLUMN scf_payable_voucher.voucher_code IS '应付债券凭证编号';
COMMENT ON COLUMN scf_payable_voucher.template_id IS '债权凭证模版id';
COMMENT ON COLUMN scf_payable_voucher.version_id IS '预留 可后续添加模板版本id';
COMMENT ON COLUMN scf_payable_voucher.platform_id IS '平台';
COMMENT ON COLUMN scf_payable_voucher.product_id IS '产品ID';
COMMENT ON COLUMN scf_payable_voucher.corp_id IS '企业ID';
COMMENT ON COLUMN scf_payable_voucher.open_date IS '开立日期';
COMMENT ON COLUMN scf_payable_voucher.expire_date IS '到期日期';
COMMENT ON COLUMN scf_payable_voucher.core_corp IS '核心企业';
COMMENT ON COLUMN scf_payable_voucher.core_corp_name IS '核心企业名称';
COMMENT ON COLUMN scf_payable_voucher.cur_id IS '币种ID';
COMMENT ON COLUMN scf_payable_voucher.voucher_money IS '凭证金额';
COMMENT ON COLUMN scf_payable_voucher.supply_id IS '供应商ID';
COMMENT ON COLUMN scf_payable_voucher.supply_name IS '供应商名称';
COMMENT ON COLUMN scf_payable_voucher.receive_account IS '收款账户';
COMMENT ON COLUMN scf_payable_voucher.receive_bank IS '收款银行';
COMMENT ON COLUMN scf_payable_voucher.online_sign IS '线上签章';
COMMENT ON COLUMN scf_payable_voucher.quota_code IS '额度编码';
COMMENT ON COLUMN scf_payable_voucher.quota_name IS '额度名称';
COMMENT ON COLUMN scf_payable_voucher.quota_balance IS '额度余额';
COMMENT ON COLUMN scf_payable_voucher.contract_code IS '合同编码';
COMMENT ON COLUMN scf_payable_voucher.contract_name IS '合同名称';
COMMENT ON COLUMN scf_payable_voucher.contract_money IS '合同金额';
COMMENT ON COLUMN scf_payable_voucher.contract_date IS '合同日期';
COMMENT ON COLUMN scf_payable_voucher.contract_file IS '合同文件';
COMMENT ON COLUMN scf_payable_voucher.invoice_file IS '发票文件';
COMMENT ON COLUMN scf_payable_voucher.voucher_source IS '单据来源(手工新增：MANUAL，ERP，供应链金融平台:SUPPLY_PLATFORM)';
COMMENT ON COLUMN scf_payable_voucher.interest_mode IS '融资付息方式(1:卖方付息，2:买方付息，默认卖方付息)';
COMMENT ON COLUMN scf_payable_voucher.promise_pay_date IS '承诺付款日';
COMMENT ON COLUMN scf_payable_voucher.supply_credit_code IS '供应商统一社会信用代码';
COMMENT ON COLUMN scf_payable_voucher.approval_status IS '审批状态';
COMMENT ON COLUMN scf_payable_voucher.voucher_status IS '业务状态';
COMMENT ON COLUMN scf_payable_voucher.pay_id IS '关联付款单ID(付款单审批通过后写入，选票时排除已关联凭证)';
COMMENT ON COLUMN scf_payable_voucher.pay_no IS '关联付款单号(付款单审批通过后写入，选票时排除已关联凭证)';
COMMENT ON COLUMN scf_payable_voucher.cancel_reason IS '作废原因';
COMMENT ON COLUMN scf_payable_voucher.cancel_date IS '作废时间';
COMMENT ON COLUMN scf_payable_voucher.create_by IS '创建人';
COMMENT ON COLUMN scf_payable_voucher.create_date IS '创建时间';
COMMENT ON COLUMN scf_payable_voucher.update_by IS '修改人';
COMMENT ON COLUMN scf_payable_voucher.update_date IS '修改时间';
COMMENT ON COLUMN scf_payable_voucher.tenant_id IS '租户ID';

-- ============================================
-- 2. 应付债权凭证自定义字段数据表
-- ============================================


-- 表说明：应付债权凭证数据表自定义字段数据
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_voucher_custom_field';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_voucher_custom_field
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    voucher_id VARCHAR2(32) NOT NULL,
    field_id VARCHAR2(32) NOT NULL,
    field_code VARCHAR2(100) NOT NULL,
    field_name VARCHAR2(200) NOT NULL,
    field_type VARCHAR2(50) NOT NULL,
    field_value VARCHAR2(255) NOT NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE NULL,
    update_by VARCHAR2(32) NULL,
    update_date DATE NULL,
    tenant_id VARCHAR2(32) NULL
);

COMMENT ON TABLE scf_voucher_custom_field IS '应付债权凭证数据表自定义字段数据';
COMMENT ON COLUMN scf_voucher_custom_field.id IS 'id';
COMMENT ON COLUMN scf_voucher_custom_field.voucher_id IS '凭证id';
COMMENT ON COLUMN scf_voucher_custom_field.field_id IS '字段id';
COMMENT ON COLUMN scf_voucher_custom_field.field_code IS '自定义字段编码';
COMMENT ON COLUMN scf_voucher_custom_field.field_name IS '自定义字段名称';
COMMENT ON COLUMN scf_voucher_custom_field.field_type IS '自定义字段类型';
COMMENT ON COLUMN scf_voucher_custom_field.field_value IS '自定义字段值';
COMMENT ON COLUMN scf_voucher_custom_field.create_by IS '创建人';
COMMENT ON COLUMN scf_voucher_custom_field.create_date IS '创建日期';
COMMENT ON COLUMN scf_voucher_custom_field.update_by IS '更新人';
COMMENT ON COLUMN scf_voucher_custom_field.update_date IS '更新日期';
COMMENT ON COLUMN scf_voucher_custom_field.tenant_id IS '租户ID';

-- ============================================
-- 3. 凭证兑付记录表
-- ============================================


-- 表说明：凭证兑付记录表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_voucher_redemption';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_voucher_redemption
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    voucher_id VARCHAR2(32) NULL,
    cash_no VARCHAR2(50) NULL,
    platform_id VARCHAR2(32) NULL,
    product_id VARCHAR2(32) NULL,
    corp_id VARCHAR2(32) NULL,
    open_date DATE NULL,
    expire_date DATE NULL,
    core_corp VARCHAR2(32) NULL,
    cur_id VARCHAR2(32) NULL,
    voucher_money NUMBER(18,6) NULL,
    supply_id VARCHAR2(32) NULL,
    supply_name VARCHAR2(200) NULL,
    pay_account VARCHAR2(200) NULL,
    cash_money NUMBER(18,6) NULL,
    pay_date DATE NULL,
    receive_name VARCHAR2(200) NULL,
    receive_account VARCHAR2(200) NULL,
    receive_bank VARCHAR2(200) NULL,
    approval_status VARCHAR2(10) NULL,
    tenant_id VARCHAR2(32) NULL
);

COMMENT ON TABLE scf_voucher_redemption IS '凭证兑付记录表';
COMMENT ON COLUMN scf_voucher_redemption.id IS 'id';
COMMENT ON COLUMN scf_voucher_redemption.voucher_id IS '应付凭证ID';
COMMENT ON COLUMN scf_voucher_redemption.cash_no IS '兑付单号';
COMMENT ON COLUMN scf_voucher_redemption.platform_id IS '平台id';
COMMENT ON COLUMN scf_voucher_redemption.product_id IS '融资产品';
COMMENT ON COLUMN scf_voucher_redemption.corp_id IS '开立单位';
COMMENT ON COLUMN scf_voucher_redemption.open_date IS '开立日期';
COMMENT ON COLUMN scf_voucher_redemption.expire_date IS '到期日';
COMMENT ON COLUMN scf_voucher_redemption.core_corp IS '核心企业';
COMMENT ON COLUMN scf_voucher_redemption.cur_id IS '币别';
COMMENT ON COLUMN scf_voucher_redemption.voucher_money IS '凭证金额';
COMMENT ON COLUMN scf_voucher_redemption.supply_id IS '供应商';
COMMENT ON COLUMN scf_voucher_redemption.supply_name IS '供应商';
COMMENT ON COLUMN scf_voucher_redemption.pay_account IS '付款账号';
COMMENT ON COLUMN scf_voucher_redemption.cash_money IS '兑付金额';
COMMENT ON COLUMN scf_voucher_redemption.pay_date IS '付款日期';
COMMENT ON COLUMN scf_voucher_redemption.receive_name IS '收款方名称';
COMMENT ON COLUMN scf_voucher_redemption.receive_account IS '收款方账号';
COMMENT ON COLUMN scf_voucher_redemption.receive_bank IS '收款开户行';
COMMENT ON COLUMN scf_voucher_redemption.approval_status IS '审批状态';
COMMENT ON COLUMN scf_voucher_redemption.tenant_id IS '租户ID';

-- ============================================
-- 4. 指令表
-- ============================================


-- 表说明：指令表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE SCF_INSTRUCTION';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE SCF_INSTRUCTION
(
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    INSTR_CODE VARCHAR2(64) NULL,
    INSTR_TYPE VARCHAR2(32) NULL,
    INSTR_TYPE_NAME VARCHAR2(200) NULL,
    PLATFORM_ID VARCHAR2(32) NULL,
    PLATFORM_NAME VARCHAR2(200) NULL,
    PRODUCT_ID VARCHAR2(64) NULL,
    PRODUCT_NAME VARCHAR2(200) NULL,
    DIRECT_IF_ID VARCHAR2(32) NULL,
    VOUCHER_CODE VARCHAR2(64) NULL,
    RELATE_NO VARCHAR2(64) NULL,
    CORP_ID VARCHAR2(32) NULL,
    CORP_NAME VARCHAR2(32) NULL,
    CUR_ID VARCHAR2(32) NULL,
    CUR_NAME VARCHAR2(100) NULL,
    INSTR_AMT NUMBER(16,6) NULL,
    INSTR_DATE DATE NULL,
    EXPIRE_DATE DATE NULL,
    TRANS_STATUS VARCHAR2(32) NULL,
    TRANS_REASON VARCHAR2(500) NULL,
    SIGN VARCHAR2(1) NULL,
    CREATE_BY VARCHAR2(32) NULL,
    CREATE_DATE DATE NULL,
    UPDATE_BY VARCHAR2(32) NULL,
    UPDATE_DATE DATE NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE SCF_INSTRUCTION IS '指令表';
COMMENT ON COLUMN SCF_INSTRUCTION.ID IS '主键ID';
COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_CODE IS '指令编码';
COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_TYPE IS '指令类型';
COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_TYPE_NAME IS '指令类型名称';
COMMENT ON COLUMN SCF_INSTRUCTION.PLATFORM_ID IS '供应链平台ID';
COMMENT ON COLUMN SCF_INSTRUCTION.PLATFORM_NAME IS '供应链平台名称';
COMMENT ON COLUMN SCF_INSTRUCTION.PRODUCT_ID IS '融资产品ID';
COMMENT ON COLUMN SCF_INSTRUCTION.PRODUCT_NAME IS '融资产品名称';
COMMENT ON COLUMN SCF_INSTRUCTION.DIRECT_IF_ID IS '直联接口ID';
COMMENT ON COLUMN SCF_INSTRUCTION.VOUCHER_CODE IS '债权凭证编码';
COMMENT ON COLUMN SCF_INSTRUCTION.RELATE_NO IS '关联编号(转让记录编号/融资单号等)';
COMMENT ON COLUMN SCF_INSTRUCTION.CORP_ID IS '开立单位ID';
COMMENT ON COLUMN SCF_INSTRUCTION.CORP_NAME IS '开立单位名称';
COMMENT ON COLUMN SCF_INSTRUCTION.CUR_ID IS '币别ID';
COMMENT ON COLUMN SCF_INSTRUCTION.CUR_NAME IS '币别名称';
COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_AMT IS '指令金额';
COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_DATE IS '指令日期';
COMMENT ON COLUMN SCF_INSTRUCTION.EXPIRE_DATE IS '到期日';
COMMENT ON COLUMN SCF_INSTRUCTION.TRANS_STATUS IS '交易状态';
COMMENT ON COLUMN SCF_INSTRUCTION.TRANS_REASON IS '交易原因';
COMMENT ON COLUMN SCF_INSTRUCTION.SIGN IS '应付应收标识(0为应付，1为应收)';
COMMENT ON COLUMN SCF_INSTRUCTION.CREATE_BY IS '创建人';
COMMENT ON COLUMN SCF_INSTRUCTION.CREATE_DATE IS '创建时间';
COMMENT ON COLUMN SCF_INSTRUCTION.UPDATE_BY IS '修改人';
COMMENT ON COLUMN SCF_INSTRUCTION.UPDATE_DATE IS '修改时间';
COMMENT ON COLUMN SCF_INSTRUCTION.TENANT_ID IS '租户ID';

-- ============================================
-- 5. 平台接口开通配置表
-- ============================================


-- 表说明：平台接口开通配置表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_ent_bank_open';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_ent_bank_open
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    type VARCHAR2(20) NULL,
    api_name_cn VARCHAR2(100) NULL,
    api_name VARCHAR2(100) NULL,
    has_direct VARCHAR2(1) NULL,
    direct VARCHAR2(32) NULL,
    ent_platform_id VARCHAR2(32) NULL,
    tenant_id VARCHAR2(32) NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE NULL,
    update_by VARCHAR2(32) NULL,
    update_date DATE NULL
);

COMMENT ON TABLE scf_ent_bank_open IS '平台接口开通配置表';
COMMENT ON COLUMN scf_ent_bank_open.id IS '主键ID';
COMMENT ON COLUMN scf_ent_bank_open.type IS '接口类型';
COMMENT ON COLUMN scf_ent_bank_open.api_name_cn IS '接口名称（中文）';
COMMENT ON COLUMN scf_ent_bank_open.api_name IS '接口名称';
COMMENT ON COLUMN scf_ent_bank_open.has_direct IS '是否具备直连接口(0-否 1-是)';
COMMENT ON COLUMN scf_ent_bank_open.direct IS '直连接口';
COMMENT ON COLUMN scf_ent_bank_open.ent_platform_id IS '平台ID';
COMMENT ON COLUMN scf_ent_bank_open.tenant_id IS '租户ID';
COMMENT ON COLUMN scf_ent_bank_open.create_by IS '创建人';
COMMENT ON COLUMN scf_ent_bank_open.create_date IS '创建时间';
COMMENT ON COLUMN scf_ent_bank_open.update_by IS '修改人';
COMMENT ON COLUMN scf_ent_bank_open.update_date IS '修改时间';

-- ============================================
-- 6. 资金方信息表
-- ============================================


-- 表说明：资金方信息表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_funder_info';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_funder_info
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    code VARCHAR2(50) NULL,
    type VARCHAR2(10) NULL,
    bank_no VARCHAR2(50) NULL,
    name VARCHAR2(200) NULL,
    sign VARCHAR2(100) NULL,
    valid VARCHAR2(10) DEFAULT '1' NULL,
    rmk VARCHAR2(500) NULL,
    tenant_id VARCHAR2(32) NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE NULL,
    update_by VARCHAR2(32) NULL,
    update_date DATE NULL
);

COMMENT ON TABLE scf_funder_info IS '资金方信息表';
COMMENT ON COLUMN scf_funder_info.id IS '主键ID';
COMMENT ON COLUMN scf_funder_info.code IS '资金方编码';
COMMENT ON COLUMN scf_funder_info.type IS '机构类型 01-保理公司 02-银行 03-其他';
COMMENT ON COLUMN scf_funder_info.bank_no IS '银行行号';
COMMENT ON COLUMN scf_funder_info.name IS '资金方名称';
COMMENT ON COLUMN scf_funder_info.sign IS '资金方标识';
COMMENT ON COLUMN scf_funder_info.valid IS '有效标识 1-有效 0-无效';
COMMENT ON COLUMN scf_funder_info.rmk IS '备注';
COMMENT ON COLUMN scf_funder_info.tenant_id IS '租户id';
COMMENT ON COLUMN scf_funder_info.create_by IS '创建人';
COMMENT ON COLUMN scf_funder_info.create_date IS '创建日期';
COMMENT ON COLUMN scf_funder_info.update_by IS '更新人';
COMMENT ON COLUMN scf_funder_info.update_date IS '更新日期';

-- ============================================
-- 7. 融资业务主信息表
-- ============================================


-- 表说明：融资业务主信息表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_loan_info';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_loan_info
(
    id VARCHAR2(64) NOT NULL PRIMARY KEY,
    loan_no VARCHAR2(64) NULL,
    tenant_id VARCHAR2(64) NULL,
    loan_company_id VARCHAR2(32) NULL,
    loan_company_name VARCHAR2(200) NULL,
    voucher_code VARCHAR2(64) NULL,
    core_company VARCHAR2(200) NULL,
    core_company_id VARCHAR2(64) NULL,
    funder_id VARCHAR2(32) NULL,
    funder_name VARCHAR2(200) NULL,
    product_id VARCHAR2(32) NULL,
    product_name VARCHAR2(200) NULL,
    platform_id VARCHAR2(32) NULL,
    platform_name VARCHAR2(200) NULL,
    cur_id VARCHAR2(64) NULL,
    cur_name VARCHAR2(32) NULL,
    loan_apply_date DATE NULL,
    loan_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    interest_mode VARCHAR2(32) DEFAULT '1' NULL,
    loan_lend_date DATE NULL,
    loan_actual_date DATE NULL,
    loan_due_date DATE NULL,
    loan_deadline VARCHAR2(10) NULL,
    loan_rate NUMBER(15,2) DEFAULT 0.00 NULL,
    loan_interest_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    loan_hand_fee_rate NUMBER(15,2) DEFAULT 0.00 NULL,
    loan_hand_fee_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    loan_service_fee_rate NUMBER(15,2) DEFAULT 0.00 NULL,
    loan_service_fee_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    loan_cost_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    loan_actual_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    loan_payment_account VARCHAR2(64) NULL,
    loan_payment_account_name VARCHAR2(64) NULL,
    loan_payment_account_bank VARCHAR2(64) NULL,
    receivable_id VARCHAR2(32) NULL,
    loan_status VARCHAR2(30) DEFAULT 'UN_LOAN' NULL,
    online_sign VARCHAR2(1) NULL,
    enable VARCHAR2(2) DEFAULT '1' NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE NULL,
    update_by VARCHAR2(32) NULL,
    update_date DATE NULL
);

COMMENT ON TABLE scf_loan_info IS '融资业务主信息表';
COMMENT ON COLUMN scf_loan_info.id IS '主键ID';
COMMENT ON COLUMN scf_loan_info.loan_no IS '融资单号(RZ+年月日+3位流水号)';
COMMENT ON COLUMN scf_loan_info.tenant_id IS '租户id';
COMMENT ON COLUMN scf_loan_info.loan_company_id IS '融资企业id';
COMMENT ON COLUMN scf_loan_info.loan_company_name IS '融资企业名称';
COMMENT ON COLUMN scf_loan_info.voucher_code IS '债权凭证编号';
COMMENT ON COLUMN scf_loan_info.core_company IS '核心企业';
COMMENT ON COLUMN scf_loan_info.core_company_id IS '核心企业id';
COMMENT ON COLUMN scf_loan_info.funder_id IS '资金方id';
COMMENT ON COLUMN scf_loan_info.funder_name IS '资金方名称';
COMMENT ON COLUMN scf_loan_info.product_id IS '融资产品id';
COMMENT ON COLUMN scf_loan_info.product_name IS '融资产品名称';
COMMENT ON COLUMN scf_loan_info.platform_id IS '平台ID';
COMMENT ON COLUMN scf_loan_info.platform_name IS '平台名称';
COMMENT ON COLUMN scf_loan_info.cur_id IS '币别ID';
COMMENT ON COLUMN scf_loan_info.cur_name IS '币别名称';
COMMENT ON COLUMN scf_loan_info.loan_apply_date IS '融资申请日';
COMMENT ON COLUMN scf_loan_info.loan_amt IS '融资金额';
COMMENT ON COLUMN scf_loan_info.interest_mode IS '付息方式(1:卖方付息，2:买方付息，默认卖方付息)';
COMMENT ON COLUMN scf_loan_info.loan_lend_date IS '放款日(默认系统当前日期)';
COMMENT ON COLUMN scf_loan_info.loan_actual_date IS '实际放款日';
COMMENT ON COLUMN scf_loan_info.loan_due_date IS '到期日';
COMMENT ON COLUMN scf_loan_info.loan_deadline IS '期限(到期日-放款日) 天';
COMMENT ON COLUMN scf_loan_info.loan_rate IS '融资利率';
COMMENT ON COLUMN scf_loan_info.loan_interest_amt IS '利息金额(融资金额*融资利率)';
COMMENT ON COLUMN scf_loan_info.loan_hand_fee_rate IS '手续费率';
COMMENT ON COLUMN scf_loan_info.loan_hand_fee_amt IS '手续费(融资金额*手续费率)';
COMMENT ON COLUMN scf_loan_info.loan_service_fee_rate IS '服务费率';
COMMENT ON COLUMN scf_loan_info.loan_service_fee_amt IS '服务费(融资金额*服务费率)';
COMMENT ON COLUMN scf_loan_info.loan_cost_amt IS '融资成本(利息金额+手续费+服务费)';
COMMENT ON COLUMN scf_loan_info.loan_actual_amt IS '融资到账金额(融资金额-融资成本)';
COMMENT ON COLUMN scf_loan_info.loan_payment_account IS '收款账号';
COMMENT ON COLUMN scf_loan_info.loan_payment_account_name IS '收款开户名';
COMMENT ON COLUMN scf_loan_info.loan_payment_account_bank IS '收款开户行';
COMMENT ON COLUMN scf_loan_info.receivable_id IS '应收ID';
COMMENT ON COLUMN scf_loan_info.loan_status IS '融资审批状态(DRAFT:暂存; APPROVAL_ING:审批中; REJECTED:打回; APPROVAL_PASS:审批通过)';
COMMENT ON COLUMN scf_loan_info.online_sign IS '线上线下标识(0:线下；1:线上)';
COMMENT ON COLUMN scf_loan_info.enable IS '是否有效(0：无效；1：有效)';
COMMENT ON COLUMN scf_loan_info.create_by IS '创建人';
COMMENT ON COLUMN scf_loan_info.create_date IS '创建时间';
COMMENT ON COLUMN scf_loan_info.update_by IS '修改人';
COMMENT ON COLUMN scf_loan_info.update_date IS '修改时间';

-- ============================================
-- 8. 应收债权凭证表
-- ============================================


-- 表说明：应收债权凭证表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_receivable_voucher_info';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_receivable_voucher_info
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    voucher_no VARCHAR2(50) NULL,
    voucher_source VARCHAR2(50) NULL,
    tenant_id VARCHAR2(64) NULL,
    voucher_code VARCHAR2(50) NULL,
    voucher_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    platform_id VARCHAR2(32) NULL,
    platform_name VARCHAR2(200) NULL,
    product_id VARCHAR2(32) NULL,
    product_name VARCHAR2(200) NULL,
    corp_id VARCHAR2(32) NULL,
    corp_name VARCHAR2(64) NULL,
    sign_date DATE NULL,
    sign_operation VARCHAR2(100) NULL,
    voucher_type VARCHAR2(32) NULL,
    expire_date DATE NULL,
    voucher_status VARCHAR2(32) NULL,
    approval_status VARCHAR2(32) NULL,
    core_company_id VARCHAR2(32) NULL,
    core_company_name VARCHAR2(200) NULL,
    cur_id VARCHAR2(32) NULL,
    cur_name VARCHAR2(16) NULL,
    issue_transfer_company VARCHAR2(200) NULL,
    issue_transfer_company_id VARCHAR2(32) NULL,
    source_voucher_code VARCHAR2(50) NULL,
    forehand_voucher_code VARCHAR2(50) NULL,
    online_sign VARCHAR2(2) NULL,
    allow_split VARCHAR2(2) DEFAULT '1' NULL,
    contract_code VARCHAR2(50) NULL,
    contract_name VARCHAR2(200) NULL,
    contract_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    contract_date DATE NULL,
    contract_file_id VARCHAR2(32) NULL,
    invoice_file_id VARCHAR2(32) NULL,
    interest_mode VARCHAR2(32) DEFAULT '1' NULL,
    promise_pay_date DATE NULL,
    transfer_company VARCHAR2(200) NULL,
    transfer_company_id VARCHAR2(32) NULL,
    enable NUMBER(1) DEFAULT 1 NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE DEFAULT SYSDATE NULL,
    update_by VARCHAR2(32) NULL,
    update_date DATE NULL
);

COMMENT ON TABLE scf_receivable_voucher_info IS '应收债权凭证表';
COMMENT ON COLUMN scf_receivable_voucher_info.id IS '主键';
COMMENT ON COLUMN scf_receivable_voucher_info.voucher_no IS '单据编号(系统自动创建AR+年月日+3位流水号)';
COMMENT ON COLUMN scf_receivable_voucher_info.voucher_source IS '单据来源(手工新增：MANUAL，ERP，供应链金融平台:SUPPLY_PLATFORM)';
COMMENT ON COLUMN scf_receivable_voucher_info.tenant_id IS '租户id';
COMMENT ON COLUMN scf_receivable_voucher_info.voucher_code IS '债权凭证编号';
COMMENT ON COLUMN scf_receivable_voucher_info.voucher_amt IS '凭证金额';
COMMENT ON COLUMN scf_receivable_voucher_info.platform_id IS '平台ID';
COMMENT ON COLUMN scf_receivable_voucher_info.platform_name IS '平台名称';
COMMENT ON COLUMN scf_receivable_voucher_info.product_id IS '融资产品id';
COMMENT ON COLUMN scf_receivable_voucher_info.product_name IS '融资产品名称';
COMMENT ON COLUMN scf_receivable_voucher_info.corp_id IS '持有单位';
COMMENT ON COLUMN scf_receivable_voucher_info.corp_name IS '持有单位名称';
COMMENT ON COLUMN scf_receivable_voucher_info.sign_date IS '签收日期';
COMMENT ON COLUMN scf_receivable_voucher_info.sign_operation IS '签收操作(签收,拒签,作废)';
COMMENT ON COLUMN scf_receivable_voucher_info.voucher_type IS '凭证类别(开立凭证,流转凭证)';
COMMENT ON COLUMN scf_receivable_voucher_info.expire_date IS '凭证到期日';
COMMENT ON COLUMN scf_receivable_voucher_info.voucher_status IS '凭证业务状态(未签收,已签收,已拒签,已拆分)';
COMMENT ON COLUMN scf_receivable_voucher_info.approval_status IS '单据状态(暂存,审批中,打回,审批通过,已作废)';
COMMENT ON COLUMN scf_receivable_voucher_info.core_company_id IS '核心企业';
COMMENT ON COLUMN scf_receivable_voucher_info.core_company_name IS '核心企业名称';
COMMENT ON COLUMN scf_receivable_voucher_info.cur_id IS '币种ID';
COMMENT ON COLUMN scf_receivable_voucher_info.cur_name IS '币别名称';
COMMENT ON COLUMN scf_receivable_voucher_info.issue_transfer_company IS '开立/转让 企业';
COMMENT ON COLUMN scf_receivable_voucher_info.issue_transfer_company_id IS '开立/转让 企业id';
COMMENT ON COLUMN scf_receivable_voucher_info.source_voucher_code IS '根凭证编号(开立成功时生成的债权凭证编号)';
COMMENT ON COLUMN scf_receivable_voucher_info.forehand_voucher_code IS '上级凭证编号';
COMMENT ON COLUMN scf_receivable_voucher_info.online_sign IS '线上线下标识';
COMMENT ON COLUMN scf_receivable_voucher_info.allow_split IS '是否允许拆分(0:否, 1:是)';
COMMENT ON COLUMN scf_receivable_voucher_info.contract_code IS '合同编码';
COMMENT ON COLUMN scf_receivable_voucher_info.contract_name IS '合同名称';
COMMENT ON COLUMN scf_receivable_voucher_info.contract_amt IS '合同金额';
COMMENT ON COLUMN scf_receivable_voucher_info.contract_date IS '合同日期';
COMMENT ON COLUMN scf_receivable_voucher_info.contract_file_id IS '合同文件id';
COMMENT ON COLUMN scf_receivable_voucher_info.invoice_file_id IS '发票文件id';
COMMENT ON COLUMN scf_receivable_voucher_info.interest_mode IS '融资付息方式(1:卖方付息,2:买方付息)';
COMMENT ON COLUMN scf_receivable_voucher_info.promise_pay_date IS '承诺付款日';
COMMENT ON COLUMN scf_receivable_voucher_info.transfer_company IS '流转单位';
COMMENT ON COLUMN scf_receivable_voucher_info.transfer_company_id IS '流转单位ID';
COMMENT ON COLUMN scf_receivable_voucher_info.enable IS '是否有效';
COMMENT ON COLUMN scf_receivable_voucher_info.create_by IS '创建人';
COMMENT ON COLUMN scf_receivable_voucher_info.create_date IS '创建时间';
COMMENT ON COLUMN scf_receivable_voucher_info.update_by IS '修改人';
COMMENT ON COLUMN scf_receivable_voucher_info.update_date IS '修改时间';

-- ============================================
-- 9. 债权凭证转让信息表
-- ============================================


-- 表说明：债权凭证转让信息表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_receivable_transfer_info';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_receivable_transfer_info
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    transfer_no VARCHAR2(32) NULL,
    transfer_batch_no VARCHAR2(32) NULL,
    tenant_id VARCHAR2(64) NULL,
    voucher_code VARCHAR2(64) NOT NULL,
    transfer_status VARCHAR2(32) NULL,
    transfer_flag VARCHAR2(2) NULL,
    platform_id VARCHAR2(64) NULL,
    platform_name VARCHAR2(200) NULL,
    product_id VARCHAR2(64) NULL,
    product_name VARCHAR2(200) NULL,
    issue_transfer_company VARCHAR2(200) NULL,
    issue_transfer_company_id VARCHAR2(32) NULL,
    cur_id VARCHAR2(32) DEFAULT 'CNY' NULL,
    cur_name VARCHAR2(16) NULL,
    receive_company VARCHAR2(200) NULL,
    receive_company_id VARCHAR2(64) NULL,
    receiver_credit_code VARCHAR2(32) NULL,
    transfer_date DATE NULL,
    transfer_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    purpose VARCHAR2(255) NULL,
    remark VARCHAR2(512) NULL,
    pay_id VARCHAR2(32) NULL,
    pay_no VARCHAR2(64) NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE DEFAULT SYSDATE NULL,
    update_by VARCHAR2(32) NULL,
    update_date DATE DEFAULT SYSDATE NULL
);

COMMENT ON TABLE scf_receivable_transfer_info IS '债权凭证转让信息表';
COMMENT ON COLUMN scf_receivable_transfer_info.id IS '主键';
COMMENT ON COLUMN scf_receivable_transfer_info.transfer_no IS '凭证转让记录编号(系统自动创建ZR+年月日+3位流水号)';
COMMENT ON COLUMN scf_receivable_transfer_info.transfer_batch_no IS '转让批次号';
COMMENT ON COLUMN scf_receivable_transfer_info.tenant_id IS '租户id';
COMMENT ON COLUMN scf_receivable_transfer_info.voucher_code IS '债权凭证编号';
COMMENT ON COLUMN scf_receivable_transfer_info.transfer_status IS '凭证转让状态';
COMMENT ON COLUMN scf_receivable_transfer_info.transfer_flag IS '转让标识(1:一对多-凭证拆分转让多家企业;2:多对一-多张凭证转让一家企业)';
COMMENT ON COLUMN scf_receivable_transfer_info.platform_id IS '平台';
COMMENT ON COLUMN scf_receivable_transfer_info.platform_name IS '平台名称';
COMMENT ON COLUMN scf_receivable_transfer_info.product_id IS '融资产品id';
COMMENT ON COLUMN scf_receivable_transfer_info.product_name IS '融资产品';
COMMENT ON COLUMN scf_receivable_transfer_info.issue_transfer_company IS '开立/转让单位';
COMMENT ON COLUMN scf_receivable_transfer_info.issue_transfer_company_id IS '开立/转让 企业id';
COMMENT ON COLUMN scf_receivable_transfer_info.cur_id IS '币别';
COMMENT ON COLUMN scf_receivable_transfer_info.cur_name IS '币别名称';
COMMENT ON COLUMN scf_receivable_transfer_info.receive_company IS '接收单位';
COMMENT ON COLUMN scf_receivable_transfer_info.receive_company_id IS '接收单位名称id';
COMMENT ON COLUMN scf_receivable_transfer_info.receiver_credit_code IS '接收方统一社会信用代码';
COMMENT ON COLUMN scf_receivable_transfer_info.transfer_date IS '转让日期';
COMMENT ON COLUMN scf_receivable_transfer_info.transfer_amt IS '转让金额';
COMMENT ON COLUMN scf_receivable_transfer_info.purpose IS '用途';
COMMENT ON COLUMN scf_receivable_transfer_info.remark IS '备注';
COMMENT ON COLUMN scf_receivable_transfer_info.pay_id IS '关联付款单ID(付款单审批通过后写入，选票时排除已关联转让)';
COMMENT ON COLUMN scf_receivable_transfer_info.pay_no IS '关联付款单号(付款单审批通过后写入，选票时排除已关联转让)';
COMMENT ON COLUMN scf_receivable_transfer_info.create_by IS '创建人';
COMMENT ON COLUMN scf_receivable_transfer_info.create_date IS '创建时间';
COMMENT ON COLUMN scf_receivable_transfer_info.update_by IS '修改人';
COMMENT ON COLUMN scf_receivable_transfer_info.update_date IS '更新时间';

-- ============================================
-- 10. 债权凭证收款明细表
-- ============================================


-- 表说明：债权凭证收款明细表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_receivable_payment_info';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_receivable_payment_info
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    voucher_code VARCHAR2(64) NOT NULL,
    payment_no VARCHAR2(64) NOT NULL,
    tenant_id VARCHAR2(64) NULL,
    payment_status VARCHAR2(32) NULL,
    platform_id VARCHAR2(64) NULL,
    platform_name VARCHAR2(200) NULL,
    product_id VARCHAR2(64) NULL,
    product_name VARCHAR2(200) NULL,
    receipt_company VARCHAR2(200) NULL,
    receipt_company_id VARCHAR2(128) NULL,
    sign_date DATE NULL,
    expire_date DATE NULL,
    issue_transfer_company VARCHAR2(200) NULL,
    issue_transfer_company_id VARCHAR2(32) NULL,
    cur_id VARCHAR2(32) DEFAULT 'CNY' NULL,
    cur_name VARCHAR2(16) NULL,
    voucher_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    core_company_id VARCHAR2(128) NULL,
    core_company_name VARCHAR2(200) NULL,
    rec_account_no VARCHAR2(64) NULL,
    rec_account_name VARCHAR2(128) NULL,
    rec_bank VARCHAR2(128) NULL,
    rec_amt NUMBER(15,2) DEFAULT 0.00 NULL,
    receipt_date DATE NULL,
    enable VARCHAR2(2) DEFAULT '1' NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE DEFAULT SYSDATE NULL,
    update_by VARCHAR2(32) NULL,
    update_date DATE DEFAULT SYSDATE NULL
);

COMMENT ON TABLE scf_receivable_payment_info IS '债权凭证收款明细表';
COMMENT ON COLUMN scf_receivable_payment_info.id IS '主键';
COMMENT ON COLUMN scf_receivable_payment_info.voucher_code IS '债权凭证编号';
COMMENT ON COLUMN scf_receivable_payment_info.payment_no IS '收款单号';
COMMENT ON COLUMN scf_receivable_payment_info.tenant_id IS '租户id';
COMMENT ON COLUMN scf_receivable_payment_info.payment_status IS '收款状态';
COMMENT ON COLUMN scf_receivable_payment_info.platform_id IS '平台';
COMMENT ON COLUMN scf_receivable_payment_info.platform_name IS '平台名称';
COMMENT ON COLUMN scf_receivable_payment_info.product_id IS '融资产品';
COMMENT ON COLUMN scf_receivable_payment_info.product_name IS '融资产品id';
COMMENT ON COLUMN scf_receivable_payment_info.receipt_company IS '收款单位';
COMMENT ON COLUMN scf_receivable_payment_info.receipt_company_id IS '收款单位id';
COMMENT ON COLUMN scf_receivable_payment_info.sign_date IS '签收日期';
COMMENT ON COLUMN scf_receivable_payment_info.expire_date IS '到期日';
COMMENT ON COLUMN scf_receivable_payment_info.issue_transfer_company IS '开立/转让单位';
COMMENT ON COLUMN scf_receivable_payment_info.issue_transfer_company_id IS '开立/转让 企业id';
COMMENT ON COLUMN scf_receivable_payment_info.cur_id IS '币别';
COMMENT ON COLUMN scf_receivable_payment_info.cur_name IS '币别名称';
COMMENT ON COLUMN scf_receivable_payment_info.voucher_amt IS '凭证金额';
COMMENT ON COLUMN scf_receivable_payment_info.core_company_id IS '核心企业id';
COMMENT ON COLUMN scf_receivable_payment_info.core_company_name IS '核心企业';
COMMENT ON COLUMN scf_receivable_payment_info.rec_account_no IS '收款账号';
COMMENT ON COLUMN scf_receivable_payment_info.rec_account_name IS '收款户名';
COMMENT ON COLUMN scf_receivable_payment_info.rec_bank IS '收款开户行';
COMMENT ON COLUMN scf_receivable_payment_info.rec_amt IS '收款金额';
COMMENT ON COLUMN scf_receivable_payment_info.receipt_date IS '收款日期';
COMMENT ON COLUMN scf_receivable_payment_info.enable IS '是否有效(0：无效；1：有效)';
COMMENT ON COLUMN scf_receivable_payment_info.create_by IS '创建人';
COMMENT ON COLUMN scf_receivable_payment_info.create_date IS '创建时间';
COMMENT ON COLUMN scf_receivable_payment_info.update_by IS '修改人';
COMMENT ON COLUMN scf_receivable_payment_info.update_date IS '更新时间';

-- ============================================
-- 11. 应付/应收债券发票信息表
-- ============================================


-- 表说明：应付应收债券发票信息
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE scf_invoice_info';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE scf_invoice_info
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    tenant_id VARCHAR2(64) NULL,
    invoice_type VARCHAR2(50) NULL,
    invoice_number VARCHAR2(50) NULL,
    invoice_code VARCHAR2(50) NULL,
    bill_date DATE NULL,
    cur_name VARCHAR2(100) NULL,
    cur_id VARCHAR2(32) NULL,
    total_amt NUMBER(15,2) DEFAULT 0.00,
    tax_rate NUMBER(15,2) DEFAULT 0.00,
    tax_amt NUMBER(15,2) DEFAULT 0.00,
    bill_amt NUMBER(15,2) DEFAULT 0.00,
    seller_name VARCHAR2(100) NULL,
    buyer_name VARCHAR2(100) NULL,
    pay_id VARCHAR2(32) NULL,
    receivable_id VARCHAR2(32) NULL,
    sign VARCHAR2(1) NULL,
    create_by VARCHAR2(32) NULL,
    create_date DATE DEFAULT SYSDATE,
    update_by VARCHAR2(32) NULL,
    update_date DATE DEFAULT SYSDATE
);

COMMENT ON TABLE scf_invoice_info IS '应付应收债券发票信息';
COMMENT ON COLUMN scf_invoice_info.id IS 'id';
COMMENT ON COLUMN scf_invoice_info.tenant_id IS '租户id';
COMMENT ON COLUMN scf_invoice_info.invoice_type IS '发票类型';
COMMENT ON COLUMN scf_invoice_info.invoice_number IS '发票号码';
COMMENT ON COLUMN scf_invoice_info.invoice_code IS '发票代码';
COMMENT ON COLUMN scf_invoice_info.bill_date IS '开票日期';
COMMENT ON COLUMN scf_invoice_info.cur_name IS '币种名称';
COMMENT ON COLUMN scf_invoice_info.cur_id IS '币种';
COMMENT ON COLUMN scf_invoice_info.total_amt IS '价税合计';
COMMENT ON COLUMN scf_invoice_info.tax_rate IS '税率';
COMMENT ON COLUMN scf_invoice_info.tax_amt IS '税额';
COMMENT ON COLUMN scf_invoice_info.bill_amt IS '金额';
COMMENT ON COLUMN scf_invoice_info.seller_name IS '销方名称';
COMMENT ON COLUMN scf_invoice_info.buyer_name IS '购方名称';
COMMENT ON COLUMN scf_invoice_info.pay_id IS '应付凭证id';
COMMENT ON COLUMN scf_invoice_info.receivable_id IS '应收凭证id';
COMMENT ON COLUMN scf_invoice_info.sign IS '应付应收标识(0为应付，1为应收)';
COMMENT ON COLUMN scf_invoice_info.create_by IS '创建人';
COMMENT ON COLUMN scf_invoice_info.create_date IS '创建时间';
COMMENT ON COLUMN scf_invoice_info.update_by IS '修改人';
COMMENT ON COLUMN scf_invoice_info.update_date IS '更新时间';

-- ============================================
-- 12. 菜单与资源（tenant_pre_menu + tenant_pre_resources）
-- ============================================

-- 供应链金融管理（顶级）

-- 基础配置

-- 参数配置

-- 平台直联设置

-- 查询资源（平台直联）

-- 应用资源（平台直联）

-- 资金方（菜单）

-- 查询资源（资金方）

-- 应用资源（资金方）

-- 电子债权凭证

-- 应付债权凭证管理

-- 应付债权凭证

-- 应付债权凭证查询资源

-- 应付债权凭证修改资源

-- 兑付记录

-- 兑付记录查询资源

-- 兑付记录修改资源

-- 应收债权凭证管理

-- 应收债权凭证

-- 转让记录

-- 融资记录

-- 到期收款记录

-- 指令管理

-- 应付债权指令管理

-- 查询资源（应付债权指令）

-- 应用资源（应付债权指令）

-- 应收债权指令管理

-- 查询资源（应收债权指令）

-- 应用资源（应收债权指令）

-- 查询统计

-- 应付债权查询


-- 应收债权查询


-- 应付债权凭证开立资源

-- 应付债权凭证兑付资源

-- 应收债权凭证开立审批资源

-- 应收债权凭证融资审批资源

-- 应收债权凭证转让审批资源

-- 应收债权凭证付款审批资源

-- 应收债权凭证查询资源

-- 转让记录查询资源

-- 融资记录查询资源

-- 到期收款记录查询资源

-- 应收债权凭证修改资源

-- 转让记录修改资源

-- 融资记录修改资源

-- 到期收款记录修改资源

-- ============================================
-- 13. 供应链流程分类（tenant_pre_act_extend_category）
-- ============================================

-- 供应链金融管理（顶级分类）

-- 应付债权凭证（流程分类）

-- 应付债权凭证开立流程

-- 应付债权凭证兑付流程

-- 应收债权凭证（流程分类）

-- 应收债权凭证开立流程

-- 应收债权凭证融资流程

-- 应收债权凭证转让流程

-- 应收债权凭证到期收款流程

-- ============================================
-- 14. 应付债权审批状态字典
-- ============================================


-- ============================================
-- 15. 应付债权业务状态字典
-- ============================================


-- ============================================
-- 16. 单据审批状态字典
-- ============================================

-- 开立暂存

-- 开立审批中

-- 开立审批通过

-- 开立打回

-- 开立已作废

-- 融资暂存

-- 融资审批中

-- 融资审批通过

-- 融资打回

-- 融资已作废

-- 转让暂存

-- 转让审批中

-- 转让审批通过

-- 转让打回

-- 转让已作废

-- 提示付款暂存

-- 提示付款审批中

-- 提示付款审批通过

-- 提示付款打回

-- 提示付款已作废

-- ============================================
-- 17. 应收债权凭证业务状态字典
-- ============================================


-- ============================================
-- 18. 单据来源字典
-- ============================================


-- ============================================
-- 19. 债权凭证线上线下标识字典
-- ============================================

-- 线下

-- 线上

-- ============================================
-- 20. 凭证类别字典
-- ============================================


-- ============================================
-- 21. 供应链指令状态字典
-- ============================================

-- 待发送

-- 发送失败

-- 发送成功

-- 打回

-- 等待结果

-- 交易成功

-- 交易失败

-- ============================================
-- 22. 付息方式字典
-- ============================================


-- ============================================
-- 付款单-供应链金融债权凭证关联表（中间表 nis_scf_info）
-- 承载付款单与应付/应收债权凭证的关联（替代在凭证表直接写 pay_no/pay_id）
-- 凭证类型：1=应付债权凭证（付款方式51），2=应收债权凭证（付款方式52）
-- ============================================


-- 表说明：付款单-供应链金融债权凭证关联表
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE nis_scf_info';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE nis_scf_info
(
    id VARCHAR2(32) NOT NULL PRIMARY KEY,
    pay_id VARCHAR2(32) NULL,
    pay_no VARCHAR2(50) NULL,
    voucher_type VARCHAR2(2) NULL,
    voucher_id VARCHAR2(32) NULL,
    transfer_id VARCHAR2(32) NULL,
    platform_id VARCHAR2(32) NULL,
    product_id VARCHAR2(32) NULL,
    tenant_id VARCHAR2(32) NULL
);

COMMENT ON TABLE nis_scf_info IS '付款单-供应链金融债权凭证关联表';
COMMENT ON COLUMN nis_scf_info.id IS 'id';
COMMENT ON COLUMN nis_scf_info.pay_id IS '付款单ID';
COMMENT ON COLUMN nis_scf_info.pay_no IS '付款单号';
COMMENT ON COLUMN nis_scf_info.voucher_type IS '凭证类型(1=应付债权凭证，2=应收债权凭证)';
COMMENT ON COLUMN nis_scf_info.voucher_id IS '债权凭证ID';
COMMENT ON COLUMN nis_scf_info.transfer_id IS '转让草稿ID(52-应收债权凭证转让暂存)';
COMMENT ON COLUMN nis_scf_info.platform_id IS '供应链金融平台ID';
COMMENT ON COLUMN nis_scf_info.product_id IS '融资产品ID';
COMMENT ON COLUMN nis_scf_info.tenant_id IS '租户ID';

-- ============================================
-- 付款单表 nis_billhead 新增供应链金融标志（幂等：字段不存在才添加）
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'NIS_BILLHEAD' AND column_name = 'SCF_IS_OPENED';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE nis_billhead ADD SCF_IS_OPENED VARCHAR2(1) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_billhead.SCF_IS_OPENED IS ''供应链金融-是否已有开立的应付债权凭证(1=是 0=否)''';
    END IF;
    COMMIT;
END;
/

DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'NIS_BILLHEAD' AND column_name = 'SCF_TRANSFER_FLAG';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE nis_billhead ADD SCF_TRANSFER_FLAG VARCHAR2(1) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_billhead.SCF_TRANSFER_FLAG IS ''供应链金融-是否批量转让应收债权凭证(1=是 0=否)''';
    END IF;
    COMMIT;
END;
/

DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'NIS_BILLHEAD' AND column_name = 'SCF_PAYABLE_OPEN_AMT';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE nis_billhead ADD SCF_PAYABLE_OPEN_AMT NUMBER(15,2) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_billhead.SCF_PAYABLE_OPEN_AMT IS ''供应链金融-应付债权凭证开立金额(51:未开立录入/已开立取所选凭证合计)''';
    END IF;
    COMMIT;
END;
/

DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'NIS_BILLHEAD' AND column_name = 'SCF_TRANSFER_AMT';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE nis_billhead ADD SCF_TRANSFER_AMT NUMBER(15,2) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_billhead.SCF_TRANSFER_AMT IS ''供应链金融-应收转让凭证金额(52:应收债权凭证转让记录金额)''';
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 付款单标识 pay_id / pay_no 迁移（幂等：字段不存在才新增）
-- 应付债权凭证 scf_payable_voucher
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'SCF_PAYABLE_VOUCHER' AND column_name = 'PAY_ID';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_payable_voucher ADD PAY_ID VARCHAR2(32) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.PAY_ID IS ''关联付款单ID(付款单审批通过后写入，选票时排除已关联凭证)''';
    END IF;
    COMMIT;
END;
/

DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'SCF_PAYABLE_VOUCHER' AND column_name = 'PAY_NO';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_payable_voucher ADD PAY_NO VARCHAR2(64) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.PAY_NO IS ''关联付款单号(付款单审批通过后写入，选票时排除已关联凭证)''';
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 作废原因 / 作废时间 迁移（幂等：字段不存在才新增）
-- 应付债权凭证 scf_payable_voucher
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'SCF_PAYABLE_VOUCHER' AND column_name = 'CANCEL_REASON';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_payable_voucher ADD CANCEL_REASON VARCHAR2(500) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.CANCEL_REASON IS ''作废原因''';
    END IF;
    COMMIT;
END;
/

DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'SCF_PAYABLE_VOUCHER' AND column_name = 'CANCEL_DATE';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_payable_voucher ADD CANCEL_DATE DATE NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.CANCEL_DATE IS ''作废时间''';
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 付款单标识 pay_id / pay_no 迁移（幂等：字段不存在才新增）
-- 应收债权凭证转让 scf_receivable_transfer_info
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'SCF_RECEIVABLE_TRANSFER_INFO' AND column_name = 'PAY_ID';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_receivable_transfer_info ADD PAY_ID VARCHAR2(32) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.PAY_ID IS ''关联付款单ID(付款单审批通过后写入，选票时排除已关联转让)''';
    END IF;
    COMMIT;
END;
/

DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'SCF_RECEIVABLE_TRANSFER_INFO' AND column_name = 'PAY_NO';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_receivable_transfer_info ADD PAY_NO VARCHAR2(64) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.PAY_NO IS ''关联付款单号(付款单审批通过后写入，选票时排除已关联转让)''';
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证转让 scf_receivable_transfer_info 转让标识（幂等：列不存在才新增）
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'SCF_RECEIVABLE_TRANSFER_INFO' AND column_name = 'TRANSFER_FLAG';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_receivable_transfer_info ADD TRANSFER_FLAG VARCHAR2(2) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.TRANSFER_FLAG IS ''转让标识(1:一对多-凭证拆分转让多家企业;2:多对一-多张凭证转让一家企业)''';
    END IF;
    COMMIT;
END;
/


-- ============================================
-- 应付债权凭证 scf_payable_voucher 核心企业名称（幂等：列不存在才新增）
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tab_columns
    WHERE table_name = 'SCF_PAYABLE_VOUCHER' AND column_name = 'CORE_CORP_NAME';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_payable_voucher ADD CORE_CORP_NAME VARCHAR2(100) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.CORE_CORP_NAME IS ''核心企业名称''';
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 存量数据回填：核心企业名称取原 core_corp 对应的企业名称
-- （核心企业改为手工录入后不再按ID反查，历史数据需一次性补齐，可重复执行）
-- ============================================
COMMIT;
/

-- 应收债权凭证作废时间、作废原因（幂等新增）
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count FROM user_tab_columns
    WHERE table_name = 'SCF_RECEIVABLE_VOUCHER_INFO' AND column_name = 'CANCEL_DATE';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_receivable_voucher_info ADD CANCEL_DATE TIMESTAMP NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.CANCEL_DATE IS ''作废时间''';
    END IF;
    SELECT COUNT(*) INTO vn_count FROM user_tab_columns
    WHERE table_name = 'SCF_RECEIVABLE_VOUCHER_INFO' AND column_name = 'CANCEL_REASON';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE scf_receivable_voucher_info ADD CANCEL_REASON VARCHAR2(500) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.CANCEL_REASON IS ''作废原因''';
    END IF;
    COMMIT;
END;
/

-- 表说明：司库银行端限额规则主表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_RULE';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_RULE (
            ID VARCHAR2(32) NOT NULL,
            RULE_CODE VARCHAR2(32) NOT NULL,
            RULE_NAME VARCHAR2(128) NOT NULL,
            BANK_ORG_ID VARCHAR2(32) NOT NULL,
            TENANT_ID VARCHAR2(32) NOT NULL,
            SIGNED_CUST_NAME VARCHAR2(200) NULL,
            DIMENSION_TYPE CHAR(1) NOT NULL,
            SELECT_MODE CHAR(1) NOT NULL,
            CHANNEL_CODE VARCHAR2(32) DEFAULT ''EBANK'' NOT NULL,
            CURRENCY_ID VARCHAR2(32) NOT NULL,
            PAY_LIMIT_FLAG CHAR(1) DEFAULT ''0'' NOT NULL,
            LAND_LIMIT_FLAG CHAR(1) DEFAULT ''0'' NOT NULL,
            ENABLE_FLAG CHAR(1) DEFAULT ''1'' NOT NULL,
            REMARK VARCHAR2(1000) NULL,
            VERSION_NO NUMBER(10) DEFAULT 0 NOT NULL,
            DEL_FLAG CHAR(1) DEFAULT ''0'' NOT NULL,
            CREATE_BY VARCHAR2(32) NULL,
            CREATE_DATE DATE DEFAULT SYSDATE NOT NULL,
            UPDATE_BY VARCHAR2(32) NULL,
            UPDATE_DATE DATE NULL,
            CONSTRAINT PK_TR_LIMIT_RULE PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UK_TR_LR_TEN_CODE ON BT_TR_LIMIT_RULE (TENANT_ID, RULE_CODE)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LR_QUERY ON BT_TR_LIMIT_RULE (TENANT_ID, BANK_ORG_ID, ENABLE_FLAG)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_RULE IS ''司库银行端限额规则主表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.RULE_CODE IS ''规则编码，PL+4位流水号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.RULE_NAME IS ''规则名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.BANK_ORG_ID IS ''签约机构ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.TENANT_ID IS ''租户ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.SIGNED_CUST_NAME IS ''签约客户名称快照''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.DIMENSION_TYPE IS ''限制维度：U单位/A银行账户''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.SELECT_MODE IS ''范围模式：A全部/C自定义''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.CHANNEL_CODE IS ''管控渠道，默认银企直联''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.CURRENCY_ID IS ''限额币种ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.PAY_LIMIT_FLAG IS ''支付限额标志：0否/1是''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.LAND_LIMIT_FLAG IS ''落地限额标志：0否/1是''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.ENABLE_FLAG IS ''启用标志：0禁用/1启用''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.REMARK IS ''备注''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.VERSION_NO IS ''乐观锁版本号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.DEL_FLAG IS ''删除标志：0正常/1删除''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.CREATE_BY IS ''创建人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.CREATE_DATE IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.UPDATE_BY IS ''更新人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE.UPDATE_DATE IS ''更新时间''';
    END IF;
END;
/

-- 表说明：限额规则自定义适用范围表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_SCOPE';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_SCOPE (
            ID VARCHAR2(32) NOT NULL,
            RULE_ID VARCHAR2(32) NOT NULL,
            DIMENSION_TYPE CHAR(1) NOT NULL,
            SCOPE_REF_ID VARCHAR2(32) NOT NULL,
            SCOPE_CODE VARCHAR2(128) NULL,
            SCOPE_NAME VARCHAR2(200) NULL,
            DEL_FLAG CHAR(1) DEFAULT ''0'' NOT NULL,
            CREATE_BY VARCHAR2(32) NULL,
            CREATE_DATE DATE DEFAULT SYSDATE NOT NULL,
            UPDATE_BY VARCHAR2(32) NULL,
            UPDATE_DATE DATE NULL,
            CONSTRAINT PK_TR_LIMIT_SCOPE PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UK_TR_LRS_SCOPE ON BT_TR_LIMIT_SCOPE (RULE_ID, DIMENSION_TYPE, SCOPE_REF_ID)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LRS_RULE ON BT_TR_LIMIT_SCOPE (RULE_ID)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_SCOPE IS ''限额规则自定义适用范围表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.RULE_ID IS ''限额规则ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.DIMENSION_TYPE IS ''限制维度：U单位/A银行账户''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.SCOPE_REF_ID IS ''单位ID或银行账户ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.SCOPE_CODE IS ''单位编码或脱敏账号快照''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.SCOPE_NAME IS ''单位名称或账户名称快照''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.DEL_FLAG IS ''删除标志；删除后恢复原记录''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.CREATE_BY IS ''创建人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.CREATE_DATE IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.UPDATE_BY IS ''更新人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_SCOPE.UPDATE_DATE IS ''更新时间''';
    END IF;
END;
/

-- 表说明：限额规则条件表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_COND';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_COND (
            ID VARCHAR2(32) NOT NULL,
            RULE_ID VARCHAR2(32) NOT NULL,
            LIMIT_TYPE CHAR(1) NOT NULL,
            LIMIT_MODE VARCHAR2(24) NOT NULL,
            VALUE_TYPE CHAR(1) NOT NULL,
            LIMIT_AMOUNT NUMBER(20,6) NULL,
            LIMIT_COUNT NUMBER(19) NULL,
            SORT_NO NUMBER(10) DEFAULT 0 NOT NULL,
            DEL_FLAG CHAR(1) DEFAULT ''0'' NOT NULL,
            CREATE_BY VARCHAR2(32) NULL,
            CREATE_DATE DATE DEFAULT SYSDATE NOT NULL,
            UPDATE_BY VARCHAR2(32) NULL,
            UPDATE_DATE DATE NULL,
            CONSTRAINT PK_TR_LIMIT_COND PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UK_TR_LC_RULE_MODE ON BT_TR_LIMIT_COND (RULE_ID, LIMIT_TYPE, LIMIT_MODE)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LC_RULE ON BT_TR_LIMIT_COND (RULE_ID)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_COND IS ''限额规则条件表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.RULE_ID IS ''限额规则ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.LIMIT_TYPE IS ''限额类型：P支付/L落地''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.LIMIT_MODE IS ''限定方式编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.VALUE_TYPE IS ''阈值类型：A金额/C笔数''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.LIMIT_AMOUNT IS ''限定金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.LIMIT_COUNT IS ''限定笔数''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.SORT_NO IS ''条件排序号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.DEL_FLAG IS ''删除标志；删除后恢复原记录''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.CREATE_BY IS ''创建人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.CREATE_DATE IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.UPDATE_BY IS ''更新人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_COND.UPDATE_DATE IS ''更新时间''';
    END IF;
END;
/

-- 表说明：限额计数交易台账表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_TXN';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_TXN (
            ID VARCHAR2(32) NOT NULL,
            SOURCE_BIZ_TYPE VARCHAR2(32) NOT NULL,
            SOURCE_BIZ_ID VARCHAR2(32) NOT NULL,
            ATTEMPT_VERSION NUMBER(10) DEFAULT 1 NOT NULL,
            BUSINESS_NO VARCHAR2(64) NOT NULL,
            BANK_ORG_ID VARCHAR2(32) NOT NULL,
            TENANT_ID VARCHAR2(32) NOT NULL,
            UNIT_ID VARCHAR2(32) NOT NULL,
            ACCOUNT_ID VARCHAR2(32) NOT NULL,
            ACCOUNT_MASK VARCHAR2(64) NULL,
            CHANNEL_CODE VARCHAR2(32) DEFAULT ''EBANK'' NOT NULL,
            ORIGINAL_CURRENCY_ID VARCHAR2(32) NOT NULL,
            ORIGINAL_AMOUNT NUMBER(20,6) DEFAULT 0 NOT NULL,
            LIMIT_CURRENCY_ID VARCHAR2(32) NOT NULL,
            LIMIT_AMOUNT NUMBER(20,6) DEFAULT 0 NOT NULL,
            FX_RATE NUMBER(20,10) NULL,
            FX_SOURCE CHAR(1) NULL,
            SUBMIT_TIME DATE NOT NULL,
            BIZ_STATUS VARCHAR2(24) NOT NULL,
            TRANS_STATUS VARCHAR2(24) NOT NULL,
            COUNTER_STATE CHAR(1) DEFAULT ''N'' NOT NULL,
            LANDING_RESULT CHAR(1) NULL,
            VERSION_NO NUMBER(10) DEFAULT 0 NOT NULL,
            LAST_EVENT_TIME DATE NULL,
            CREATE_BY VARCHAR2(32) NULL,
            CREATE_DATE DATE DEFAULT SYSDATE NOT NULL,
            UPDATE_BY VARCHAR2(32) NULL,
            UPDATE_DATE DATE NULL,
            CONSTRAINT PK_TR_LIMIT_TXN PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UK_TR_LT_SOURCE ON BT_TR_LIMIT_TXN (TENANT_ID, SOURCE_BIZ_TYPE, SOURCE_BIZ_ID, ATTEMPT_VERSION)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LT_STATE ON BT_TR_LIMIT_TXN (TENANT_ID, COUNTER_STATE, SUBMIT_TIME)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LT_BIZNO ON BT_TR_LIMIT_TXN (TENANT_ID, BUSINESS_NO)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LT_BACKFILL ON BT_TR_LIMIT_TXN (SOURCE_BIZ_TYPE, COUNTER_STATE, CREATE_DATE)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_TXN IS ''限额计数交易台账表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.SOURCE_BIZ_TYPE IS ''来源业务类型：付款办理/资金收付''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.SOURCE_BIZ_ID IS ''支付指令或最小付款明细ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.ATTEMPT_VERSION IS ''付款尝试版本：同一业务单据重新提交时递增''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.BUSINESS_NO IS ''业务单号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.BANK_ORG_ID IS ''签约机构ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.TENANT_ID IS ''租户ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.UNIT_ID IS ''付款单位ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.ACCOUNT_ID IS ''付款账户ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.ACCOUNT_MASK IS ''脱敏付款账号快照''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.CHANNEL_CODE IS ''管控渠道''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.ORIGINAL_CURRENCY_ID IS ''原币币种ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.ORIGINAL_AMOUNT IS ''原币金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.LIMIT_CURRENCY_ID IS ''折算后限额币种ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.LIMIT_AMOUNT IS ''折算后限额金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.FX_RATE IS ''折算汇率''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.FX_SOURCE IS ''汇率来源：D单据/F折算汇率''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.SUBMIT_TIME IS ''提交时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.BIZ_STATUS IS ''业务单据状态''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.TRANS_STATUS IS ''交易状态''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.COUNTER_STATE IS ''计数状态：N未占用/T在途/S成功/R释放''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.LANDING_RESULT IS ''落地结果：A同意/R拒绝''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.VERSION_NO IS ''乐观锁版本号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.LAST_EVENT_TIME IS ''最近事件时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.CREATE_BY IS ''创建人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.CREATE_DATE IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.UPDATE_BY IS ''更新人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN.UPDATE_DATE IS ''更新时间''';
    END IF;
END;
/

-- 表说明：限额周期累计用量表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_USAGE';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_USAGE (
            ID VARCHAR2(32) NOT NULL,
            TENANT_ID VARCHAR2(32) NOT NULL,
            SCOPE_TYPE CHAR(1) NOT NULL,
            SCOPE_REF_ID VARCHAR2(32) NOT NULL,
            CURRENCY_ID VARCHAR2(32) NOT NULL,
            CHANNEL_CODE VARCHAR2(32) NOT NULL,
            PERIOD_TYPE VARCHAR2(16) NOT NULL,
            PERIOD_START DATE NOT NULL,
            INTRANSIT_AMOUNT NUMBER(20,6) DEFAULT 0 NOT NULL,
            INTRANSIT_COUNT NUMBER(19) DEFAULT 0 NOT NULL,
            SUCCESS_AMOUNT NUMBER(20,6) DEFAULT 0 NOT NULL,
            SUCCESS_COUNT NUMBER(19) DEFAULT 0 NOT NULL,
            VERSION_NO NUMBER(10) DEFAULT 0 NOT NULL,
            UPDATE_DATE DATE NULL,
            CONSTRAINT PK_TR_LIMIT_USAGE PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UK_TR_LU_BUCKET ON BT_TR_LIMIT_USAGE (TENANT_ID, SCOPE_TYPE, SCOPE_REF_ID, CURRENCY_ID, CHANNEL_CODE, PERIOD_TYPE, PERIOD_START)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LU_SCOPE ON BT_TR_LIMIT_USAGE (TENANT_ID, SCOPE_REF_ID, PERIOD_START)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_USAGE IS ''限额周期累计用量表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.TENANT_ID IS ''租户ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.SCOPE_TYPE IS ''范围类型：R规则/U单位/A账户''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.SCOPE_REF_ID IS ''范围引用ID（规则ID/单位ID/账户ID）''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.CURRENCY_ID IS ''币种ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.CHANNEL_CODE IS ''渠道编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.PERIOD_TYPE IS ''周期类型''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.PERIOD_START IS ''周期开始时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.INTRANSIT_AMOUNT IS ''在途累计金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.INTRANSIT_COUNT IS ''在途累计笔数''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.SUCCESS_AMOUNT IS ''成功累计金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.SUCCESS_COUNT IS ''成功累计笔数''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.VERSION_NO IS ''乐观锁版本号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_USAGE.UPDATE_DATE IS ''更新时间''';
    END IF;
END;
/

-- 表说明：限额校验日志表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_CHECK_LOG';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_CHECK_LOG (
            ID VARCHAR2(32) NOT NULL,
            CHECK_BATCH_NO VARCHAR2(40) NOT NULL,
            TXN_ID VARCHAR2(32) NOT NULL,
            CHECK_TYPE VARCHAR2(16) NOT NULL,
            CHECK_TIME DATE DEFAULT SYSDATE NOT NULL,
            CHECK_RESULT CHAR(1) NOT NULL,
            UNIT_RULE_ID VARCHAR2(32) NULL,
            ACCOUNT_RULE_ID VARCHAR2(32) NULL,
            REQUEST_AMOUNT NUMBER(20,6) NOT NULL,
            CURRENCY_ID VARCHAR2(32) NOT NULL,
            HIT_COUNT NUMBER(10) DEFAULT 0 NOT NULL,
            ERROR_CODE VARCHAR2(32) NULL,
            ERROR_MESSAGE VARCHAR2(500) NULL,
            CONSTRAINT PK_TR_LIMIT_CHECK_LOG PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UK_TR_LCL_BATCH ON BT_TR_LIMIT_CHECK_LOG (CHECK_BATCH_NO)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LCL_TXN ON BT_TR_LIMIT_CHECK_LOG (TXN_ID, CHECK_TIME)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_CHECK_LOG IS ''限额校验日志表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.CHECK_BATCH_NO IS ''校验批次号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.TXN_ID IS ''交易台账ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.CHECK_TYPE IS ''校验类型''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.CHECK_TIME IS ''校验时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.CHECK_RESULT IS ''校验结果：P通过/H命中限额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.UNIT_RULE_ID IS ''命中单位级规则ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.ACCOUNT_RULE_ID IS ''命中账户级规则ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.REQUEST_AMOUNT IS ''请求金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.CURRENCY_ID IS ''币种ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.HIT_COUNT IS ''命中条件数量''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.ERROR_CODE IS ''错误码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CHECK_LOG.ERROR_MESSAGE IS ''错误信息''';
    END IF;
END;
/

-- 表说明：落地限额处理单表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_CASE';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_CASE (
            ID VARCHAR2(32) NOT NULL,
            CASE_NO VARCHAR2(40) NOT NULL,
            TXN_ID VARCHAR2(32) NOT NULL,
            CHECK_LOG_ID VARCHAR2(32) NULL,
            SOURCE_BIZ_TYPE VARCHAR2(32) NOT NULL,
            SOURCE_BIZ_ID VARCHAR2(32) NOT NULL,
            ATTEMPT_VERSION NUMBER(10) DEFAULT 1 NOT NULL,
            BIZ_TYPE VARCHAR2(32) DEFAULT ''LAND_CASE'' NOT NULL,
            BUSINESS_NO VARCHAR2(64) NOT NULL,
            BANK_ORG_ID VARCHAR2(32) NOT NULL,
            TENANT_ID VARCHAR2(32) NOT NULL,
            UNIT_ID VARCHAR2(32) NOT NULL,
            ACCOUNT_ID VARCHAR2(32) NOT NULL,
            ACCOUNT_MASK VARCHAR2(64) NULL,
            PAY_AMOUNT NUMBER(20,6) NOT NULL,
            CURRENCY_ID VARCHAR2(32) NOT NULL,
            APPLICANT_ID VARCHAR2(32) NULL,
            APPLICANT_NAME VARCHAR2(200) NULL,
            APPLY_TIME DATE DEFAULT SYSDATE NOT NULL,
            CASE_STATUS CHAR(1) DEFAULT ''0'' NOT NULL,
            HANDLE_RESULT CHAR(1) NULL,
            HANDLE_DESC VARCHAR2(1000) NULL,
            HANDLE_BY VARCHAR2(32) NULL,
            HANDLE_TIME DATE NULL,
            PROCESS_INSTANCE_ID VARCHAR2(64) NULL,
            VERSION_NO NUMBER(10) DEFAULT 0 NOT NULL,
            CREATE_BY VARCHAR2(32) NULL,
            CREATE_DATE DATE DEFAULT SYSDATE NOT NULL,
            UPDATE_BY VARCHAR2(32) NULL,
            UPDATE_DATE DATE NULL,
            CONSTRAINT PK_TR_LIMIT_CASE PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UK_TR_LCASE_NO ON BT_TR_LIMIT_CASE (TENANT_ID, CASE_NO)';
        EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UK_TR_LCASE_TXN ON BT_TR_LIMIT_CASE (TENANT_ID, TXN_ID)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LCASE_QUERY ON BT_TR_LIMIT_CASE (TENANT_ID, BANK_ORG_ID, CASE_STATUS, APPLY_TIME)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_CASE IS ''落地限额处理单表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.CASE_NO IS ''落地处理单号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.TXN_ID IS ''交易台账ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.CHECK_LOG_ID IS ''校验日志ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.SOURCE_BIZ_TYPE IS ''来源业务类型''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.SOURCE_BIZ_ID IS ''来源业务ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.ATTEMPT_VERSION IS ''付款尝试版本''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.BIZ_TYPE IS ''业务类型：LAND_CASE落地处理单''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.BUSINESS_NO IS ''业务单号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.BANK_ORG_ID IS ''签约机构ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.TENANT_ID IS ''租户ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.UNIT_ID IS ''付款单位ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.ACCOUNT_ID IS ''付款账户ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.ACCOUNT_MASK IS ''脱敏付款账号快照''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.PAY_AMOUNT IS ''支付金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.CURRENCY_ID IS ''支付币种ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.APPLICANT_ID IS ''申请人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.APPLICANT_NAME IS ''申请人名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.APPLY_TIME IS ''申请时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.CASE_STATUS IS ''处理状态：0待处理/A同意/R拒绝''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.HANDLE_RESULT IS ''处理结果''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.HANDLE_DESC IS ''处理说明''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.HANDLE_BY IS ''处理人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.HANDLE_TIME IS ''处理时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.PROCESS_INSTANCE_ID IS ''流程实例ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.VERSION_NO IS ''乐观锁版本号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.CREATE_BY IS ''创建人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.CREATE_DATE IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.UPDATE_BY IS ''更新人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_CASE.UPDATE_DATE IS ''更新时间''';
    END IF;
END;
/

-- 表说明：限额命中明细表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_HIT_DETAIL';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_HIT_DETAIL (
            ID VARCHAR2(32) NOT NULL,
            CHECK_LOG_ID VARCHAR2(32) NOT NULL,
            CASE_ID VARCHAR2(32) NULL,
            TXN_ID VARCHAR2(32) NOT NULL,
            CONDITION_ID VARCHAR2(32) NOT NULL,
            RULE_ID VARCHAR2(32) NOT NULL,
            RULE_CODE VARCHAR2(32) NULL,
            RULE_NAME VARCHAR2(128) NULL,
            RULE_SCOPE_TYPE CHAR(1) NULL,
            LIMIT_TYPE CHAR(1) NOT NULL,
            LIMIT_MODE VARCHAR2(24) NOT NULL,
            LIMIT_AMOUNT NUMBER(20,6) NULL,
            ACTUAL_AMOUNT NUMBER(20,6) NULL,
            EXCESS_AMOUNT NUMBER(20,6) NULL,
            LIMIT_COUNT NUMBER(19) NULL,
            ACTUAL_COUNT NUMBER(19) NULL,
            EXCESS_COUNT NUMBER(19) NULL,
            EXCESS_RATIO NUMBER(18,6) NULL,
            CHANNEL_CODE VARCHAR2(32) NULL,
            DISPLAY_SEQ NUMBER(10) DEFAULT 0 NOT NULL,
            CREATE_DATE DATE DEFAULT SYSDATE NOT NULL,
            CONSTRAINT PK_TR_LIMIT_HIT_DETAIL PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UK_TR_LHD_LOG_COND ON BT_TR_LIMIT_HIT_DETAIL (CHECK_LOG_ID, CONDITION_ID)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LHD_CASE ON BT_TR_LIMIT_HIT_DETAIL (CASE_ID, DISPLAY_SEQ)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_HIT_DETAIL IS ''限额命中明细表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.CHECK_LOG_ID IS ''校验日志ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.CASE_ID IS ''落地处理单ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.TXN_ID IS ''交易台账ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.CONDITION_ID IS ''限额条件ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.RULE_ID IS ''限额规则ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.RULE_CODE IS ''规则编码快照''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.RULE_NAME IS ''规则名称快照''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.RULE_SCOPE_TYPE IS ''规则范围类型：U单位/A账户''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.LIMIT_TYPE IS ''限额类型：P支付/L落地''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.LIMIT_MODE IS ''限定方式编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.LIMIT_AMOUNT IS ''限额阈值金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.ACTUAL_AMOUNT IS ''实际发生金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.EXCESS_AMOUNT IS ''超出金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.LIMIT_COUNT IS ''限额阈值笔数''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.ACTUAL_COUNT IS ''实际发生笔数''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.EXCESS_COUNT IS ''超出笔数''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.EXCESS_RATIO IS ''超出比例''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.CHANNEL_CODE IS ''渠道编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.DISPLAY_SEQ IS ''展示顺序''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_HIT_DETAIL.CREATE_DATE IS ''创建时间''';
    END IF;
END;
/

-- 表说明：限额规则操作审计表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_RULE_AUDIT';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_RULE_AUDIT (
            ID VARCHAR2(32) NOT NULL,
            RULE_ID VARCHAR2(32) NOT NULL,
            OPERATION_TYPE VARCHAR2(16) NOT NULL,
            BEFORE_SNAPSHOT CLOB NULL,
            AFTER_SNAPSHOT CLOB NULL,
            OPERATOR_ID VARCHAR2(32) NULL,
            OPERATOR_NAME VARCHAR2(200) NULL,
            OPERATION_TIME DATE DEFAULT SYSDATE NOT NULL,
            CONSTRAINT PK_TR_LIMIT_RULE_AUDIT PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LRA_RULE ON BT_TR_LIMIT_RULE_AUDIT (RULE_ID, OPERATION_TIME)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_RULE_AUDIT IS ''限额规则操作审计表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE_AUDIT.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE_AUDIT.RULE_ID IS ''限额规则ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE_AUDIT.OPERATION_TYPE IS ''操作类型：CREATE/UPDATE/DELETE/ENABLE/DISABLE''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE_AUDIT.BEFORE_SNAPSHOT IS ''操作前规则快照JSON''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE_AUDIT.AFTER_SNAPSHOT IS ''操作后规则快照JSON''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE_AUDIT.OPERATOR_ID IS ''操作人ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE_AUDIT.OPERATOR_NAME IS ''操作人名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_RULE_AUDIT.OPERATION_TIME IS ''操作时间''';
    END IF;
END;
/

-- 表说明：限额交易周期占用桶表
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tables
    WHERE table_name = 'BT_TR_LIMIT_TXN_BUCKET';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE BT_TR_LIMIT_TXN_BUCKET (
            ID VARCHAR2(32) NOT NULL,
            TXN_ID VARCHAR2(32) NOT NULL,
            TENANT_ID VARCHAR2(32) NOT NULL,
            SCOPE_TYPE CHAR(1) NOT NULL,
            SCOPE_REF_ID VARCHAR2(32) NOT NULL,
            CURRENCY_ID VARCHAR2(32) NOT NULL,
            CHANNEL_CODE VARCHAR2(32) NOT NULL,
            PERIOD_TYPE VARCHAR2(16) NOT NULL,
            PERIOD_START DATE NOT NULL,
            OCCUPIED_AMOUNT NUMBER(20,6) DEFAULT 0 NOT NULL,
            OCCUPIED_COUNT NUMBER(19) DEFAULT 0 NOT NULL,
            COUNTER_STATE CHAR(1) DEFAULT ''T'' NOT NULL,
            CREATE_DATE DATE DEFAULT SYSDATE NOT NULL,
            UPDATE_DATE DATE NULL,
            CONSTRAINT PK_TR_LIMIT_TXN_BUCKET PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LTB_TXN ON BT_TR_LIMIT_TXN_BUCKET (TENANT_ID, TXN_ID, COUNTER_STATE)';
        EXECUTE IMMEDIATE 'CREATE INDEX IX_TR_LTB_BACKFILL ON BT_TR_LIMIT_TXN_BUCKET (TENANT_ID, PERIOD_START)';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE BT_TR_LIMIT_TXN_BUCKET IS ''限额交易周期占用桶表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.ID IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.TXN_ID IS ''交易台账ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.TENANT_ID IS ''租户ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.SCOPE_TYPE IS ''范围类型：R规则/U单位/A账户''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.SCOPE_REF_ID IS ''范围引用ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.CURRENCY_ID IS ''币种ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.CHANNEL_CODE IS ''渠道编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.PERIOD_TYPE IS ''周期类型''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.PERIOD_START IS ''周期开始时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.OCCUPIED_AMOUNT IS ''占用金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.OCCUPIED_COUNT IS ''占用笔数''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.COUNTER_STATE IS ''计数状态：T在途/S成功/R释放''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.CREATE_DATE IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_TR_LIMIT_TXN_BUCKET.UPDATE_DATE IS ''更新时间''';
    END IF;
END;
/

-- 付款办理、资金收付增加银行落地限额处理结果
DECLARE
    VN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tab_columns
    WHERE table_name = 'NIS_BILLHEAD' AND column_name = 'LANDING_LIMIT_RESULT';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE NIS_BILLHEAD ADD LANDING_LIMIT_RESULT CHAR(1) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN NIS_BILLHEAD.LANDING_LIMIT_RESULT IS ''落地限额处理结果(0=待执行/A=银行同意支付/R=银行拒绝支付)''';
    END IF;

    SELECT COUNT(*) INTO VN_COUNT
    FROM user_tab_columns
    WHERE table_name = 'FEX_CASH_FLOW' AND column_name = 'LANDING_LIMIT_RESULT';
    IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_CASH_FLOW ADD LANDING_LIMIT_RESULT CHAR(1) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN FEX_CASH_FLOW.LANDING_LIMIT_RESULT IS ''落地限额处理结果(0=待执行/A=银行同意支付/R=银行拒绝支付)''';
    END IF;
END;
/

-- 代发工资增加落地限额处理结果
DECLARE
    VN_TABLE_COUNT NUMBER;
    VN_COLUMN_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO VN_TABLE_COUNT
    FROM user_tables
    WHERE table_name = 'NIS_WAGE';
    SELECT COUNT(*) INTO VN_COLUMN_COUNT
    FROM user_tab_columns
    WHERE table_name = 'NIS_WAGE' AND column_name = 'LANDING_LIMIT_RESULT';
    IF VN_TABLE_COUNT > 0 AND VN_COLUMN_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE NIS_WAGE ADD LANDING_LIMIT_RESULT CHAR(1) NULL';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN NIS_WAGE.LANDING_LIMIT_RESULT IS ''落地限额处理结果(0=待执行/A=银行同意支付/R=银行拒绝支付)''';
    END IF;
END;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FC_SUBJECT'
  and column_name = 'ACC_TYPE';
if v_count = 1 then
        v_str := 'ALTER TABLE FC_SUBJECT MODIFY ACC_TYPE VARCHAR2(6)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FC_SUBJECT.ACC_TYPE is ''账户类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'GUA_EXGUARANTEE_AMOUNT'
  and column_name = 'PROPORTION';
if v_count = 1 then
        v_str := 'ALTER TABLE GUA_EXGUARANTEE_AMOUNT MODIFY PROPORTION NUMBER(8, 6)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column GUA_EXGUARANTEE_AMOUNT.PROPORTION is ''比例''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'SYS_ATTACHMENT_MANAGER'
  and column_name = 'MD5';
if v_count < 1 then
        v_str := 'ALTER TABLE SYS_ATTACHMENT_MANAGER ADD MD5 varchar2(64)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'COMMENT ON COLUMN SYS_ATTACHMENT_MANAGER.MD5 is ''MD5''';
end if;
end;
/

--修改人：luoyj
--修改时间：2025年6月9日
--修改原因：00062025060244 第三方账号管理需求初始化脚本
-- 表说明：账户类型表
DECLARE
v_table_exists NUMBER;
BEGIN
SELECT COUNT(*) INTO v_table_exists
FROM user_tables
WHERE table_name = 'TP_ACCOUNT_TYPE';

IF v_table_exists = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_ACCOUNT_TYPE (
                ID                   VARCHAR2(32) NOT NULL,
                CFC_CODE             VARCHAR2(64) NOT NULL,
                ACCOUNT_TYPE_CODE    VARCHAR2(64) NOT NULL,
                ACCOUNT_TYPE_NAME    VARCHAR2(255) NOT NULL,
                VALID_SIGN           CHAR(1) NOT NULL,
                SYSTEM_SIGN          CHAR(1) NOT NULL,
                IS_DELETED            CHAR(1) DEFAULT ''N'',
                REMARKS              VARCHAR2(255),
                CONF_TYPE            VARCHAR(255) NOT NULL,
                CREATE_DATE          DATE,
                UPDATE_DATE          DATE,
                CREATE_BY            VARCHAR2(64),
                UPDATE_BY            VARCHAR2(64),
                CONSTRAINT PK_TP_ACCOUNT_TYPE PRIMARY KEY (ID)
            )
        ';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_ACCOUNT_TYPE IS ''账户类型表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.CFC_CODE IS ''平台代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.ACCOUNT_TYPE_CODE IS ''账户类型代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.ACCOUNT_TYPE_NAME IS ''账户类型名称''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.VALID_SIGN IS ''是否生效; Y/N''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.SYSTEM_SIGN IS ''是否系统预设; Y/N''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.IS_DELETED IS ''是否删除; Y/N''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.REMARKS IS ''备注''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.CONF_TYPE IS ''电商平台/支付平台''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.CREATE_DATE IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.UPDATE_DATE IS ''修改时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.CREATE_BY IS ''创建人''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_ACCOUNT_TYPE.UPDATE_BY IS ''修改人''';
END IF;
COMMIT;
END;
/


-- 表说明：电商离线账户余额表
DECLARE
v_table_exists NUMBER;
BEGIN
SELECT COUNT(*) INTO v_table_exists
FROM user_tables
WHERE table_name = 'TP_OFFLINE_BAL';

IF v_table_exists = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_OFFLINE_BAL (
                ID                   VARCHAR2(32) NOT NULL,
                PLATFORM_CODE        VARCHAR2(64) NOT NULL,
                APP_ID               VARCHAR2(64) ,
                MERCHANT_NO          VARCHAR2(64) NOT NULL,
                ACCOUNT_TYPE_CODE VARCHAR2(64) ,
                PF_ACCOUNT_TYPE_CODE VARCHAR2(64) ,
                BALANCE_DATE         DATE NOT NULL,
                BALANCE              NUMBER(20,2) NOT NULL,
                ENABLE_AMOUNT        NUMBER(20,2),
                DISABLE_AMOUNT       NUMBER(20,2),
                CURRENCY_TYPE        VARCHAR2(10),
                DESCRIPTION          VARCHAR2(255),
                REMARKS              VARCHAR2(255),
                INIT_DATE            DATE,
                INIT_BALANCE         NUMBER(20,2),
                CREATE_DATE          DATE,
                UPDATE_DATE          DATE,
                CREATE_BY            VARCHAR2(64),
                UPDATE_BY            VARCHAR2(64),
                CONSTRAINT PK_TP_OFFLINE_BAL PRIMARY KEY (ID)
            )
        ';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_OFFLINE_BAL IS ''电商离线账户余额表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.PLATFORM_CODE IS ''平台代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.APP_ID IS ''应用ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.MERCHANT_NO IS ''商户号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.ACCOUNT_TYPE_CODE IS ''账户类型代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.PF_ACCOUNT_TYPE_CODE IS ''开放平台账户类型代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.BALANCE_DATE IS ''余额日期''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.BALANCE IS ''余额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.ENABLE_AMOUNT IS ''可用金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.DISABLE_AMOUNT IS ''冻结金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.CURRENCY_TYPE IS ''货币类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.DESCRIPTION IS ''描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.REMARKS IS ''备注''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.INIT_DATE IS ''初始日期''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.INIT_BALANCE IS ''初始余额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.CREATE_DATE IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.UPDATE_DATE IS ''修改时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.CREATE_BY IS ''创建人''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BAL.UPDATE_BY IS ''修改人''';
END IF;
COMMIT;
END;
/



-- 表说明：电商离线保证金余额表
DECLARE
v_table_exists NUMBER;
BEGIN
SELECT COUNT(*) INTO v_table_exists
FROM user_tables
WHERE table_name = 'TP_OFFLINE_BOND_BAL';

IF v_table_exists = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_OFFLINE_BOND_BAL (
                ID                   VARCHAR2(32) NOT NULL,
                PLATFORM_CODE        VARCHAR2(64) NOT NULL,
                APP_ID               VARCHAR2(64) ,
                MERCHANT_NO          VARCHAR2(64) NOT NULL,
                ACCOUNT_TYPE_CODE VARCHAR2(64) ,
                PF_ACCOUNT_TYPE_CODE VARCHAR2(64) ,
                BALANCE_DATE         DATE NOT NULL,
                BALANCE              NUMBER(20,2) NOT NULL,
                ENABLE_AMOUNT        NUMBER(20,2),
                DISABLE_AMOUNT       NUMBER(20,2),
                PAYABLE_AMOUNT       NUMBER(20,2),
                CURRENCY_TYPE        VARCHAR2(10),
                DESCRIPTION          VARCHAR2(255),
                REMARKS              VARCHAR2(255),
                CREATE_DATE          DATE,
                UPDATE_DATE          DATE,
                CREATE_BY            VARCHAR2(64),
                UPDATE_BY            VARCHAR2(64),
                CONSTRAINT PK_TP_OFFLINE_BOND_BAL PRIMARY KEY (ID)
            )
        ';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_OFFLINE_BOND_BAL IS ''电商离线保证金余额表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.PLATFORM_CODE IS ''平台代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.APP_ID IS ''应用ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.MERCHANT_NO IS ''商户号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.ACCOUNT_TYPE_CODE IS ''账户类型代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.PF_ACCOUNT_TYPE_CODE IS ''开放平台账户类型代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.BALANCE_DATE IS ''余额日期''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.BALANCE IS ''余额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.ENABLE_AMOUNT IS ''可用金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.DISABLE_AMOUNT IS ''冻结金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.PAYABLE_AMOUNT IS ''应缴金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.CURRENCY_TYPE IS ''货币类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.DESCRIPTION IS ''描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.REMARKS IS ''备注''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.CREATE_DATE IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.UPDATE_DATE IS ''修改时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.CREATE_BY IS ''创建人''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_BAL.UPDATE_BY IS ''修改人''';
END IF;
COMMIT;
END;
/


-- 表说明：电商离线保证金流水表
DECLARE
v_table_exists NUMBER;
BEGIN
SELECT COUNT(*) INTO v_table_exists
FROM user_tables
WHERE table_name = 'TP_OFFLINE_BOND_FLOW_DTL';

IF v_table_exists = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_OFFLINE_BOND_FLOW_DTL (
                ID                   VARCHAR2(32) NOT NULL,
                PLATFORM_CODE        VARCHAR2(64) NOT NULL,
                APP_ID               VARCHAR2(64) ,
                MERCHANT_NO          VARCHAR2(64) NOT NULL,
                SERIAL_NO            VARCHAR2(64) NOT NULL,
                ACCOUNT_TYPE_CODE VARCHAR2(64) ,
                PF_ACCOUNT_TYPE_CODE VARCHAR2(64) ,
                DIRECTION_TYPE       VARCHAR2(20) NOT NULL,
                TRADE_TYPE           VARCHAR2(64) ,
                TRADE_TIME           DATE NOT NULL,
                TRADE_MONEY          NUMBER(20,2) NOT NULL,
                CURRENCY_TYPE        VARCHAR2(10),
                DESCRIPTION          VARCHAR2(255),
                REMARKS              VARCHAR2(255),
                CREATE_DATE          DATE,
                UPDATE_DATE          DATE,
                CREATE_BY            VARCHAR2(64),
                UPDATE_BY            VARCHAR2(64),
                CONSTRAINT PK_TP_OFFLINE_BOND_FLOW_DTL PRIMARY KEY (ID)
            )
        ';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_OFFLINE_BOND_FLOW_DTL IS ''电商离线保证金流水表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.PLATFORM_CODE IS ''平台代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.APP_ID IS ''应用ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.MERCHANT_NO IS ''商户号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.SERIAL_NO IS ''流水号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.ACCOUNT_TYPE_CODE IS ''账户类型代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.PF_ACCOUNT_TYPE_CODE IS ''开放平台账户类型代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.DIRECTION_TYPE IS ''动账方向;收入/支出=IN/OUT''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.TRADE_TYPE IS ''交易类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.TRADE_TIME IS ''交易时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.TRADE_MONEY IS ''交易金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.CURRENCY_TYPE IS ''货币类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.DESCRIPTION IS ''描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.REMARKS IS ''备注''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.CREATE_DATE IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.UPDATE_DATE IS ''修改时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.CREATE_BY IS ''创建人''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_BOND_FLOW_DTL.UPDATE_BY IS ''修改人''';
END IF;
COMMIT;
END;
/



-- 表说明：电商离线账户流水表
DECLARE
v_table_exists NUMBER;
BEGIN
SELECT COUNT(*) INTO v_table_exists
FROM user_tables
WHERE table_name = 'TP_OFFLINE_FLOW_DTL';

IF v_table_exists = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_OFFLINE_FLOW_DTL (
                ID                   VARCHAR2(32) NOT NULL,
                PLATFORM_CODE        VARCHAR2(64) NOT NULL,
                APP_ID               VARCHAR2(64) ,
                MERCHANT_NO          VARCHAR2(64) NOT NULL,
                SERIAL_NO            VARCHAR2(64) NOT NULL,
                TRADE_NO             VARCHAR2(64) NOT NULL,
                ACCOUNT_TYPE_CODE VARCHAR2(64) ,
                PF_ACCOUNT_TYPE_CODE VARCHAR2(64) ,
                DIRECTION_TYPE       VARCHAR2(20) NOT NULL,
                TRADE_TYPE           VARCHAR2(255) ,
                TRADE_TIME           DATE NOT NULL,
                TRADE_MONEY          NUMBER(20,2) NOT NULL,
                CURRENCY_TYPE        VARCHAR2(10),
                DESCRIPTION          VARCHAR2(255),
                REMARKS              VARCHAR2(255),
                CREATE_DATE          DATE,
                UPDATE_DATE          DATE,
                CREATE_BY            VARCHAR2(64),
                UPDATE_BY            VARCHAR2(64),
                CONSTRAINT PK_TP_OFFLINE_FLOW_DTL PRIMARY KEY (ID)
            )
        ';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_OFFLINE_FLOW_DTL IS ''电商离线账户流水表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.PLATFORM_CODE IS ''平台代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.APP_ID IS ''应用ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.MERCHANT_NO IS ''商户号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.SERIAL_NO IS ''流水号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.TRADE_NO IS ''交易号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.ACCOUNT_TYPE_CODE IS ''账户类型代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.PF_ACCOUNT_TYPE_CODE IS ''开放平台账户类型代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.DIRECTION_TYPE IS ''动账方向;收入/支出=IN/OUT''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.TRADE_TYPE IS ''交易类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.TRADE_TIME IS ''交易时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.TRADE_MONEY IS ''交易金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.CURRENCY_TYPE IS ''货币类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.DESCRIPTION IS ''描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.REMARKS IS ''备注''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.CREATE_DATE IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.UPDATE_DATE IS ''修改时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.CREATE_BY IS ''创建人''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_OFFLINE_FLOW_DTL.UPDATE_BY IS ''修改人''';
END IF;
COMMIT;
END;
/


--修改人：chenqin
--修改内容：新增表   tp_platform_interface_info  平台接口开通信息表
--修改时间：2025-07-14
-- 表说明：平台接口开通信息表
DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT from user_tables where TABLE_NAME = 'TP_PLATFORM_INTERFACE_INFO';
  IF v_COUNT = 0 THEN
	--创建表
	 EXECUTE IMMEDIATE 'CREATE TABLE tp_platform_interface_info  (
   ID VARCHAR2(32) NOT NULL,
    PLATFORM_CODE VARCHAR2(10) NOT NULL,
    PLATFORM_NAME VARCHAR2(100) NOT NULL,
    IS_ONLINE VARCHAR2(1) NOT NULL,
    BAL_QUERY VARCHAR2(1) NOT NULL,
    DTL_QUERY VARCHAR2(1) NOT NULL,
    BOND_BAL_QUERY VARCHAR2(1) NOT NULL,
    BOND_DTL_QUERY VARCHAR2(1) NOT NULL,
    NEED_AUTHORIZATION VARCHAR2(1) NOT NULL,
    AUTHORIZATION_URL VARCHAR2(500),
    CONSTRAINT PK_TP_PLATFORM_INTERFACE_INFO PRIMARY KEY (ID)
)';
EXECUTE IMMEDIATE 'COMMENT ON TABLE tp_platform_interface_info IS ''平台接口开通信息表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.ID IS ''主键''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.PLATFORM_CODE IS ''平台代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.PLATFORM_NAME IS ''平台名称''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.IS_ONLINE IS ''是否具备直连接口 Y-具备 N-不具备''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.BAL_QUERY IS ''余额查询 Y-开通 N-未开通''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.DTL_QUERY IS ''流水查询 Y-开通 N-未开通''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.DTL_QUERY IS ''流水查询 Y-开通 N-未开通''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.BOND_DTL_QUERY IS ''保证金流水查询 Y-开通 N-未开通''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.NEED_AUTHORIZATION IS ''是否需要授权 Y-需要 N-不需要''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_platform_interface_info.AUTHORIZATION_URL IS ''授权地址''';
  END IF;
commit;
END;
/




--修改人：chenqin
--修改内容：新增表   tp_merchant_auth   商户号授权表
--修改时间：2025-07-14
-- 表说明：商户号授权表
DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT from user_tables where TABLE_NAME = 'TP_MERCHANT_AUTH';
  IF v_COUNT = 0 THEN
	--创建表
	 EXECUTE IMMEDIATE 'CREATE TABLE tp_merchant_auth   (
	ID VARCHAR2(32) NOT NULL,
	MERCHANT_ID VARCHAR2(32),
	AUTH_STATUS CHAR(1),
	VALID_PERIOD NUMBER(11),
	EXPIRATION_DATE TIMESTAMP,
	ACCESS_TOKEN VARCHAR2(500),
	REFRESH_TOKEN VARCHAR2(500),
	CREATE_BY VARCHAR2(50),
	CREATE_DATE TIMESTAMP,
	UPDATE_BY VARCHAR2(50),
	UPDATE_DATE TIMESTAMP,
	CONSTRAINT PK_TP_MERCHANT_AUTH PRIMARY KEY (ID)
)';
EXECUTE IMMEDIATE 'COMMENT ON TABLE tp_merchant_auth IS ''商户号授权表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.ID IS ''主键''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.MERCHANT_ID IS ''商户号id''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.AUTH_STATUS IS ''授权状态''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.VALID_PERIOD IS ''有效周期''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.EXPIRATION_DATE IS ''过期时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.ACCESS_TOKEN IS ''访问令牌''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.REFRESH_TOKEN IS ''刷新令牌''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.CREATE_BY IS ''创建人''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.CREATE_DATE IS ''创建日期''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.UPDATE_BY IS ''更新人''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN tp_merchant_auth.UPDATE_DATE IS ''更新日期''';

  END IF;
commit;
END;
/


--修改人：chenqin
--修改内容：新增表   TP_PLATFORM_PARAM   平台参数信息表
--修改时间：2025-07-14
-- 表说明：平台参数信息表
DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT from user_tables where TABLE_NAME = 'TP_PLATFORM_PARAM';
  IF v_COUNT = 0 THEN
	--创建表
	 EXECUTE IMMEDIATE 'CREATE TABLE TP_PLATFORM_PARAM   (
	ID VARCHAR2(32) NOT NULL,
	PLATFORM_CODE VARCHAR2(10) NOT NULL,
	PLATFORM_PARAM VARCHAR2(50) NOT NULL,
	PLATFORM_PARAM_NAME VARCHAR2(50) NOT NULL,
	PLATFORM_TYPE CHAR(1) DEFAULT NULL,
	CONSTRAINT PK_TP_PLATFORM_PARAM PRIMARY KEY (ID)
)';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_PLATFORM_PARAM IS ''平台参数信息表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PLATFORM_PARAM.ID IS ''主键''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PLATFORM_PARAM.PLATFORM_CODE IS ''平台代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PLATFORM_PARAM.PLATFORM_PARAM IS ''平台参数''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PLATFORM_PARAM.PLATFORM_PARAM_NAME IS ''平台参数名称''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PLATFORM_PARAM.PLATFORM_TYPE IS ''平台类型''';
  END IF;
commit;
END;
/




--修改人：chenqin
--修改内容：tp_merchant 商户号表，增加 PLATFORM_TYPE 字段
--修改时间：2025-07-14
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM USER_TAB_COLUMNS
  WHERE TABLE_NAME = 'TP_MERCHANT' AND COLUMN_NAME = 'PLATFORM_TYPE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT ADD PLATFORM_TYPE VARCHAR2(10) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.PLATFORM_TYPE IS ''平台类型 ZF/DS''';
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT MODIFY PLATFORM_TYPE VARCHAR2(10) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.PLATFORM_TYPE IS ''平台类型 ZF/DS''';
  END IF;
  commit;
END;
/


--修改人：chenqin
--修改内容：tp_merchant 商户号表，增加 BAL_QUERY 字段
--修改时间：2025-07-14
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM USER_TAB_COLUMNS
  WHERE TABLE_NAME = 'TP_MERCHANT' AND COLUMN_NAME = 'BAL_QUERY';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT ADD BAL_QUERY CHAR(1) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.BAL_QUERY IS ''余额查询 Y-开通 N-未开通''';
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT MODIFY BAL_QUERY CHAR(1) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.BAL_QUERY IS ''余额查询 Y-开通 N-未开通''';
  END IF;
  commit;
END;
/


--修改人：chenqin
--修改内容：tp_merchant 商户号表，增加 DTL_QUERY 字段
--修改时间：2025-07-14
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM USER_TAB_COLUMNS
  WHERE TABLE_NAME = 'TP_MERCHANT' AND COLUMN_NAME = 'DTL_QUERY';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT ADD DTL_QUERY CHAR(1) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.DTL_QUERY IS ''流水查询 Y-开通 N-未开通''';
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT MODIFY DTL_QUERY CHAR(1) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.DTL_QUERY IS ''流水查询 Y-开通 N-未开通''';
  END IF;
  commit;
END;
/


--修改人：chenqin
--修改内容：tp_merchant 商户号表，增加 BOND_BAL_QUERY 字段
--修改时间：2025-07-14
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM USER_TAB_COLUMNS
  WHERE TABLE_NAME = 'TP_MERCHANT' AND COLUMN_NAME = 'BOND_BAL_QUERY';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT ADD BOND_BAL_QUERY CHAR(1) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.BOND_BAL_QUERY IS ''保证金余额查询 Y-开通 N-未开通''';
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT MODIFY BOND_BAL_QUERY CHAR(1) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.BOND_BAL_QUERY IS ''保证金余额查询 Y-开通 N-未开通''';
  END IF;
  commit;
END;
/


--修改人：chenqin
--修改内容：tp_merchant 商户号表，增加 BOND_DTL_QUERY 字段
--修改时间：2025-07-14
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM USER_TAB_COLUMNS
  WHERE TABLE_NAME = 'TP_MERCHANT' AND COLUMN_NAME = 'BOND_DTL_QUERY';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT ADD BOND_DTL_QUERY CHAR(1) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.BOND_DTL_QUERY IS ''保证金流水查询 Y-开通 N-未开通''';
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT MODIFY BOND_DTL_QUERY CHAR(1) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.BOND_DTL_QUERY IS ''保证金流水查询 Y-开通 N-未开通''';
  END IF;
  commit;
END;
/


--修改人：chenqin
--修改内容：tp_merchant 商户号表，增加 DIRECT_CONN_INTERFACE 字段
--修改时间：2025-07-14
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM USER_TAB_COLUMNS
  WHERE TABLE_NAME = 'TP_MERCHANT' AND COLUMN_NAME = 'DIRECT_CONN_INTERFACE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT ADD DIRECT_CONN_INTERFACE VARCHAR2(20) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.DIRECT_CONN_INTERFACE IS ''直连接口''';
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE TP_MERCHANT MODIFY DIRECT_CONN_INTERFACE VARCHAR2(20) DEFAULT NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_MERCHANT.DIRECT_CONN_INTERFACE IS ''直连接口''';
  END IF;
  commit;
END;
/



--修改内容：BT_WARN_DATA 表，增加 PLATFORM_CODE、MERCHANT_NO 字段
--修改时间：2025-07-14
DECLARE
v_count NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'BT_WARN_DATA' AND COLUMN_NAME = 'PLATFORM_CODE';

IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE BT_WARN_DATA ADD PLATFORM_CODE VARCHAR2(1000) DEFAULT NULL';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_WARN_DATA.PLATFORM_CODE IS ''电商平台编码''';
ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE BT_WARN_DATA MODIFY PLATFORM_CODE VARCHAR2(1000) DEFAULT NULL';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_WARN_DATA.PLATFORM_CODE IS ''电商平台编码''';
END IF;
commit;
END;
/


DECLARE
v_count NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'BT_WARN_DATA' AND COLUMN_NAME = 'MERCHANT_NO';

IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE BT_WARN_DATA ADD MERCHANT_NO VARCHAR2(1000) DEFAULT NULL';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_WARN_DATA.MERCHANT_NO IS ''电商商户号''';
ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE BT_WARN_DATA MODIFY MERCHANT_NO VARCHAR2(1000) DEFAULT NULL';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN BT_WARN_DATA.MERCHANT_NO IS ''电商商户号''';
END IF;
commit;
END;
/



-- 电商接口
-- 快手账务明细表
-- 表说明：快手账务明细表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_KUAISHOU_FUND_DTL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_KUAISHOU_FUND_DTL (
                ID            VARCHAR2(32) NOT NULL PRIMARY KEY,
                APP_KEY       VARCHAR2(64),
                MCH_ID        VARCHAR2(64),
                AMOUNT        NUMBER(15, 2),
                BALANCE       NUMBER(15, 2),
                IN_OUT_TYPE   NUMBER(1),
                BOOK_TRANS_NO VARCHAR2(64),
                BIZ_TYPE      VARCHAR2(64),
                OUT_ORDER_ID  VARCHAR2(64),
                REMARK        VARCHAR2(1000),
                ATTACH        VARCHAR2(1000),
                BILL_TIME     TIMESTAMP,
                BIZ_TYPE_DESC VARCHAR2(100),
                CREATED_TIME  TIMESTAMP
            )';
END IF;
END;
/


BEGIN
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_KUAISHOU_FUND_DTL IS ''快手账务明细表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.APP_KEY IS ''应用key''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.MCH_ID IS ''商户号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.AMOUNT IS ''记账金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.BALANCE IS ''记账后余额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.IN_OUT_TYPE IS ''财务方向，1收，2支''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.BOOK_TRANS_NO IS ''账务流水号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.BIZ_TYPE IS ''业务类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.OUT_ORDER_ID IS ''业务订单号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.REMARK IS ''描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.ATTACH IS ''备注''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.BILL_TIME IS ''交易时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.BIZ_TYPE_DESC IS ''业务类型描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_FUND_DTL.CREATED_TIME IS ''创建时间''';
END;
/

-- 快手余额表
-- 表说明：快手余额表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_KUAISHOU_BAL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_KUAISHOU_BAL (
                ID                        VARCHAR2(32) NOT NULL PRIMARY KEY,
                APP_KEY                   VARCHAR2(64),
                MCH_ID                    VARCHAR2(64),
                BAL_DATE                  TIMESTAMP,
                TOTAL_AMOUNT              NUMBER(18, 2),
                AVAILABLE_AMOUNT          NUMBER(18, 2),
                FREEZE_AMOUNT             NUMBER(18, 2),
                STATUS                    VARCHAR2(10),
                ACCOUNT_CHANNEL           VARCHAR2(10),
                SECUTITY_DEPOSIT_TYPE     NUMBER(1),
                DEPOSIT_LOW_BALANCE       NUMBER(18, 2),
                IF_DEPOSIT_BALANCE_ENOUGH NUMBER(1),
                CREATED_TIME              TIMESTAMP,
                UPDATED_TIME              TIMESTAMP
            )';
END IF;
END;
/


BEGIN
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_KUAISHOU_BAL IS ''快手余额表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.APP_KEY IS ''应用key''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.MCH_ID IS ''商户号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.BAL_DATE IS ''余额日期''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.TOTAL_AMOUNT IS ''账户余额（元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.AVAILABLE_AMOUNT IS ''账户可用余额（元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.FREEZE_AMOUNT IS ''冻结金额（元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.status IS ''账户状态(ENABLE-启用，DISABLE-未启用，FROZE-冻结，UNBIND-已解绑)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.ACCOUNT_CHANNEL IS ''账户渠道(1-小店余额 2-微信 3-支付宝 4-安心钱包 5-保证金)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.SECUTITY_DEPOSIT_TYPE IS ''保证金类型(1-自建保证金，2-推广保证金，7-跨境保证金)''';
EXECUTE IMMEDIATE 'COMMENT ON column TP_KUAISHOU_BAL.DEPOSIT_LOW_BALANCE IS ''保证金阈值(元)''';
EXECUTE IMMEDIATE 'COMMENT ON column TP_KUAISHOU_BAL.IF_DEPOSIT_BALANCE_ENOUGH IS ''保证金余额是否充足(1-是，0-否)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.CREATED_TIME IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_KUAISHOU_BAL.UPDATED_TIME IS ''更新时间''';
END;
/

-- 保证金账单任务表
-- 表说明：保证金账单任务表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_BAIL_BILL_JOB';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_BAIL_BILL_JOB (
                ID          VARCHAR2(32) NOT NULL PRIMARY KEY,
                MERCHANT_NO VARCHAR2(30),
                PAY_TYPE    VARCHAR2(10),
                BILL_DATE   VARCHAR2(10),
                STATUS      NUMBER(2, 0),
                CREATE_TIME TIMESTAMP,
                CORP_ID     VARCHAR2(32)
            )';
END IF;
END;
/


BEGIN
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_BAIL_BILL_JOB IS ''保证金账单任务表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_BAIL_BILL_JOB.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_BAIL_BILL_JOB.MERCHANT_NO IS ''appId/商户号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_BAIL_BILL_JOB.PAY_TYPE IS ''支付类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_BAIL_BILL_JOB.BILL_DATE IS ''账单日期''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_BAIL_BILL_JOB.STATUS IS ''状态：-1未开始，0执行失败，1执行成功''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_BAIL_BILL_JOB.CREATE_TIME IS ''创建日期''';
END;
/


-- 天猫支付宝商家账户保证金明细表
-- 表说明：天猫支付宝商家账户保证金明细表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_TMALL_BAIL_BILL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_TMALL_BAIL_BILL (
                ID           VARCHAR2(32) NOT NULL PRIMARY KEY,
                APP_ID       VARCHAR2(64),
                MCH_ID       VARCHAR2(64),
                TRANS_DT     TIMESTAMP,
                TRANS_LOG_ID VARCHAR2(255),
                BAIL_TYPE    VARCHAR2(255),
                BAIL_AMOUNT  NUMBER(15, 2),
                BAIL_BALANCE NUMBER(15, 2),
                BAIL_MEMO    VARCHAR2(255),
                BIZ_DESC     VARCHAR2(255),
                BIZ_ORIG_NO  VARCHAR2(255),
                CREATED_TIME TIMESTAMP
            )';
END IF;
END;
/


BEGIN
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_TMALL_BAIL_BILL IS ''天猫支付宝商家账户保证金明细表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.MCH_ID IS ''商户号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.APP_ID IS ''应用ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.TRANS_DT IS ''业务发生时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.TRANS_LOG_ID IS ''保证金业务流水号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.BAIL_TYPE IS ''保证金类型描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.BAIL_AMOUNT IS ''保证金收支金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.BAIL_BALANCE IS ''本次操作后的保证金余额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.BAIL_MEMO IS ''保证金说明''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.BIZ_DESC IS ''业务描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.BIZ_ORIG_NO IS ''业务基础订单号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAIL_BILL.CREATED_TIME IS ''创建时间''';
END;
/

-- 支付宝余额表
-- 表说明：支付宝余额表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_TMALL_BAL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_TMALL_BAL (
                ID               VARCHAR2(32) NOT NULL PRIMARY KEY,
                MCH_ID           VARCHAR2(64),
                APP_ID           VARCHAR2(64),
                BAL_TYPE         VARCHAR2(2),
                BAL_DATE         TIMESTAMP,
                TOTAL_AMOUNT     NUMBER(18, 2),
                AVAILABLE_AMOUNT NUMBER(18, 2),
                FREEZE_AMOUNT    NUMBER(18, 2),
                BEGINNING_AMOUNT NUMBER(15, 2),
                SETTLE_AMOUNT    NUMBER(15, 2),
                CREATED_TIME     TIMESTAMP,
                UPDATED_TIME     TIMESTAMP
            )';
END IF;
END;
/


BEGIN
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_TMALL_BAL IS ''支付宝余额表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.MCH_ID IS ''商户ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.APP_ID IS ''应用ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.BAL_TYPE IS ''余额类型：1-支付宝账户余额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.BAL_DATE IS ''余额日期''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.TOTAL_AMOUNT IS ''支付宝账户余额（元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.AVAILABLE_AMOUNT IS ''账户可用余额（元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.FREEZE_AMOUNT IS ''冻结金额（元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.BEGINNING_AMOUNT IS ''期初余额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.SETTLE_AMOUNT IS ''待结算余额（元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.CREATED_TIME IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_BAL.UPDATED_TIME IS ''修改时间''';
END;
/


-- 天猫资金账单明细表
-- 表说明：天猫资金账单明细表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_TMALL_FUND_DTL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_TMALL_FUND_DTL (
                ID            VARCHAR2(32) NOT NULL PRIMARY KEY,
                APP_ID        VARCHAR2(64),
                ACCOUNT_ID    VARCHAR2(64),
                BUSINESS_ID   VARCHAR2(64),
                TRADE_NO      VARCHAR2(64),
                TRADE_TYPE    VARCHAR2(20),
                GOODS_NAME    VARCHAR2(100),
                HAPPEN_TIME   TIMESTAMP,
                PAY_ACC       VARCHAR2(100),
                PAYEE_MONEY   NUMBER(15, 2),
                PAYER_MONEY   NUMBER(15, 2),
                BALANCE       NUMBER(15, 2),
                TRANS_CHANNEL VARCHAR2(100),
                REMARK        VARCHAR2(1000),
                FBS_STATUS    VARCHAR2(1),
                BUS_TYPE      VARCHAR2(32),
                MCH_ID        VARCHAR2(64),
                SHOP_NAME     VARCHAR2(200)
            )';
END IF;
END;
/


BEGIN
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_TMALL_FUND_DTL IS ''天猫资金账单明细表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.APP_ID IS ''应用ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.ACCOUNT_ID IS ''账务流水号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.BUSINESS_ID IS ''业务流水号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.TRADE_NO IS ''商户订单号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.TRADE_TYPE IS ''账务类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.GOODS_NAME IS ''商品名称''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.HAPPEN_TIME IS ''发生时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.PAY_ACC IS ''对方账号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.PAYEE_MONEY IS ''收入金额（+元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.PAYER_MONEY IS ''支出金额（-元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.BALANCE IS ''账户余额（元）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.TRANS_CHANNEL IS ''交易渠道''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.REMARK IS ''备注''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.FBS_STATUS IS ''资金计划挂接状态 0未确认 1已确认 2已删除''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.BUS_TYPE IS ''业务类别id''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.MCH_ID IS ''商户号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_TMALL_FUND_DTL.SHOP_NAME IS ''门店名称''';
END;
/


-- 抖音账单明细表
-- 表说明：抖音账单明细表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_DOUYIN_DTL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_DOUYIN_DTL(
                id                              VARCHAR2(32) NOT NULL PRIMARY KEY,
                mch_id                          VARCHAR2(64),
                app_key                         VARCHAR2(64),
                app_secret                      VARCHAR2(64),
                shop_id                         VARCHAR2(64),
                bill_time                       TIMESTAMP,
                account_trade_no                VARCHAR2(64),
                fund_flow                       VARCHAR2(2),
                fund_flow_desc                  VARCHAR2(150),
                account_amount                  NUMBER(15, 2),
                account_type                    VARCHAR2(2),
                account_type_desc               VARCHAR2(100),
                account_bill_desc               VARCHAR2(100),
                account_bill_desc_tag           VARCHAR2(2),
                biz_type                        VARCHAR2(2),
                biz_type_desc                   VARCHAR2(150),
                shop_order_id                   VARCHAR2(64),
                order_id                        VARCHAR2(64),
                after_sale_service_no           VARCHAR2(64),
                business_order_create_time      TIMESTAMP,
                product_id                      VARCHAR2(64),
                order_type                      VARCHAR2(2),
                order_type_desc                 VARCHAR2(100),
                pay_amount                      NUMBER(15, 2),
                post_amount                     NUMBER(15, 2),
                promotion_amount                NUMBER(15, 2),
                author_coupon_subsidy           NUMBER(15, 2),
                actual_zt_pay_promotion         NUMBER(15, 2),
                actual_zr_pay_promotion         NUMBER(15, 2),
                refund_amount                   NUMBER(15, 2),
                platform_service_fee            NUMBER(15, 2),
                commission                      NUMBER(15, 2),
                channel_fee                     NUMBER(15, 2),
                colonel_service_fee             NUMBER(15, 2),
                channel_promotion_fee           NUMBER(15, 2),
                other_sharing_amount            NUMBER(15, 2),
                trans_scene_tag                 VARCHAR2(2),
                trans_scene                     VARCHAR2(150),
                remark                          VARCHAR2(150),
                free_commission_flag            VARCHAR2(10),
                real_free_commission_amount     NUMBER(15, 2),
                recycler_amount                 NUMBER(15, 2),
                bank_pay_promotion_amount       NUMBER(15, 2),
                post_promotion_amount           NUMBER(15, 2),
                partner_commission              NUMBER(15, 2),
                gov_promotion_amount            NUMBER(15, 2),
                product_name                    VARCHAR2(150),
                author_id                       VARCHAR2(64),
                author_name                     VARCHAR2(150),
                other_pla_promo_amt             NUMBER(15, 2)
            )';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_DOUYIN_DTL IS ''抖音账单明细表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.id IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.mch_id IS ''商户号 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.app_key IS ''应用ID ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.app_secret IS ''应用密钥 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.shop_id IS ''店铺id''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.bill_time IS ''动账时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.account_trade_no IS ''动账流水号(唯一)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.fund_flow IS ''动账方向 0:入账 1:出账''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.fund_flow_desc IS ''动账方向描述 出账、入账 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.account_amount IS ''动账金额(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.account_type IS ''动账账户 1: 微信 2:支付宝 3:合众支付 4:聚合支付''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.account_type_desc IS ''动账账户''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.account_bill_desc IS ''动账摘要''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.account_bill_desc_tag IS ''动账摘要 0：其他 1：达人带货佣金 2：达人佣金退款 3：订单结算 4：部分结算 5：运费单结算 6：服务费返还 7：平台补贴扣回 8：退款失败退票 9：结算重复扣款调账 10：保证金退款 11：提现 12：提现退票 13：极速退款分账 14：小额打款(原因:补差价) 15：小额打款(原因:其他) 16：小额打款(原因:商品补偿) 17：小额打款(原因:运费补偿) 18：小额打款退票 19：已退款 20：保费扣除（x笔保单扣费，明细详见保费扣除明细） 21：支付BIC服务费 22：BIC服务费退票 23: 原路退 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.biz_type IS ''计费类型 0:全部 1:鲁班广告 2:精选联盟 3:值点商城 4:小店自卖 5:橙子建站 6:POI 7:抖+ 8:穿山甲 9:服务市场 10:服务市场外包客服 11:学浪''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.biz_type_desc IS ''计费类型描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.shop_order_id IS ''店铺单号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.order_id IS ''订单号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.after_sale_service_no IS ''【商家无需使用】售后编号非售后接口的售后单id；该字段为账单售后编号id；''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.business_order_create_time IS ''下单时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.product_id IS ''商品ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.order_type IS ''订单类型 0：普通订单 1：定金(已退款) 2：定金(尾款已支付) 3：定金(尾款未支付) 4：尾款(尾款已支付) 5：尾款(已退款)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.order_type_desc IS ''订单类型描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.pay_amount IS ''订单实付应结，单位：分；只包含用户实际支付的金额，不含各类补贴（如：平台补贴、达人补贴、支付补贴等）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.post_amount IS ''运费(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.promotion_amount IS ''实际平台补贴(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.author_coupon_subsidy IS ''实际达人补贴(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.actual_zt_pay_promotion IS ''实际抖音支付补贴(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.actual_zr_pay_promotion IS ''实际DOU分期营销补贴(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.refund_amount IS ''订单退款(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.platform_service_fee IS ''平台服务费(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.commission IS ''佣金(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.channel_fee IS ''渠道分成(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.colonel_service_fee IS ''招商服务费(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.channel_promotion_fee IS ''直播间站外推广(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.other_sharing_amount IS ''其他分成金额(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.trans_scene_tag IS ''动账场景 枚举值对应含义请参考文档：https://bytedance.larkoffice.com/docx/FefRdbxoPoSUBYx4YHhc5a5rnrh''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.trans_scene IS ''动账场景''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.remark IS ''备注''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.free_commission_flag IS ''是否免佣,枚举值为"是/否"或为空''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.real_free_commission_amount IS ''免佣金额(分)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.recycler_amount IS ''以旧换新抵扣(分) ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.bank_pay_promotion_amount IS ''银行补贴(分) ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.post_promotion_amount IS ''平台补贴运费(分) ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.partner_commission IS ''服务商佣金(分) ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.gov_promotion_amount IS ''政府补贴平台垫资(分) ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.product_name IS ''商品名称 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.author_id IS ''达人id ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.author_name IS ''达人名称 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_DTL.other_pla_promo_amt IS ''其他平台补贴(分) ''';
END IF;
END;
/


-- 抖音账单查询跟踪器表
-- 表说明：抖音账单查询跟踪器表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_DOUYIN_QUERY_TRACKER';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_DOUYIN_QUERY_TRACKER(
                id                VARCHAR2(32) NOT NULL PRIMARY KEY,
                mch_id            VARCHAR2(64),
                pay_type          VARCHAR2(10),
                bill_date         VARCHAR2(10),
                next_start_index  VARCHAR2(64),
                next_start_time   TIMESTAMP,
                create_time       TIMESTAMP,
                update_time       TIMESTAMP
            )';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_DOUYIN_QUERY_TRACKER IS ''抖音账单查询跟踪器表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_QUERY_TRACKER.id IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_QUERY_TRACKER.mch_id IS ''商户号 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_QUERY_TRACKER.pay_type IS ''交易渠道 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_QUERY_TRACKER.bill_date IS ''账单日期 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_QUERY_TRACKER.next_start_index IS ''下次查询开始下标''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_QUERY_TRACKER.next_start_time IS ''下次查询开始时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_QUERY_TRACKER.create_time IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DOUYIN_QUERY_TRACKER.update_time IS ''修改时间''';
END IF;
END;
/


-- 拼多多对账单表
-- 表说明：拼多多对账单表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_PINDUODUO_DTL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_PINDUODUO_DTL(
                id                VARCHAR2(32) NOT NULL PRIMARY KEY,
                client_id         VARCHAR2(60) NOT NULL,
                merchant_order_no VARCHAR2(64),
                occur_time        TIMESTAMP,
                income_amount     NUMBER(20,2),
                expense_amount    NUMBER(20,2),
                account_type      VARCHAR2(200),
                remark            VARCHAR2(2000),
                bus_desc          VARCHAR2(2000),
                create_time       TIMESTAMP DEFAULT SYSTIMESTAMP,
                update_time       TIMESTAMP DEFAULT SYSTIMESTAMP,
                merchant_no       VARCHAR2(64)
            )';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_PINDUODUO_DTL IS ''拼多多对账单表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.client_id IS ''clientId''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.merchant_order_no IS ''商户订单号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.occur_time IS ''发生时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.income_amount IS ''收入金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.expense_amount IS ''支出金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.account_type IS ''财务类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.remark IS ''备注''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.bus_desc IS ''业务描述''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.create_time IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.update_time IS ''对账单更新时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_PINDUODUO_DTL.merchant_no IS ''商户号''';
END IF;
END;
/


-- 小红书账单明细表
-- 表说明：小红书账单明细表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_XIAOHONGSHU_DTL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_XIAOHONGSHU_DTL(
                id                   VARCHAR2(32) NOT NULL PRIMARY KEY,
                mchId                VARCHAR2(64),
                appKey               VARCHAR2(64),
                appSecret            VARCHAR2(64),
                accountId            VARCHAR2(64),
                sellerId             VARCHAR2(64),
                tradeNo              VARCHAR2(64),
                incomeAmount         NUMBER(15, 2),
                outcomeAmount        NUMBER(15, 2),
                balanceAmount        NUMBER(15, 2),
                businessNo           VARCHAR2(64),
                remark               VARCHAR2(150),
                type                 VARCHAR2(20),
                typeDesc             VARCHAR2(150),
                createdTime          TIMESTAMP,
                businessType         VARCHAR2(20),
                balanceBefore        NUMBER(15, 2),
                fundType             VARCHAR2(20),
                disableBusinessNoLink VARCHAR2(2)
            )';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_XIAOHONGSHU_DTL IS ''小红书账单明细表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.id  IS '' 主键 ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.mchId  IS '' 商户号 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.appKey  IS '' 应用ID ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.appSecret  IS '' 应用密钥 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.accountId  IS '' 账户唯一id ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.sellerId  IS '' 店铺id ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.tradeNo  IS '' 动账流水号 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.incomeAmount  IS '' 收入（元） ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.outcomeAmount  IS '' 支出（元） ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.balanceAmount  IS '' 账户余额（元） ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.businessNo  IS '' 业务单号 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.remark  IS '' 备注 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.type  IS '' 交易类型 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.typeDesc  IS '' 交易类型中文描述 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.createdTime  IS '' 创建时间 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.businessType  IS '' 动账业务子类型 ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.balanceBefore  IS '' 交易前金额（元） ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.fundType  IS '' 账户类型：NOT_ER_QING(店铺余额)ER_QING_WECHAT(微信)ER_QING_ALIPAY(支付宝) ''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_XIAOHONGSHU_DTL.disableBusinessNoLink  IS '' 是否屏蔽业务跳转 ''';
END IF;
END;
/


-- 得物对账单表
-- 表说明：得物对账单表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_DEWU_DTL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_DEWU_DTL(
                id                         VARCHAR2(32) NOT NULL PRIMARY KEY,
                app_key                    VARCHAR2(60) NOT NULL,
                bill_no                    VARCHAR2(64),
                bill_start_time            TIMESTAMP,
                bill_end_time              TIMESTAMP,
                order_receive_amount       NUMBER(20,2),
                platform_service_fee       NUMBER(20,2),
                advance_fee                NUMBER(20,2),
                seller_subsidies_amount    NUMBER(20,2),
                platform_difference_amount NUMBER(20,2),
                refund_amount              NUMBER(20,2),
                settle_amount              NUMBER(20,2),
                status                     VARCHAR2(20),
                create_time                TIMESTAMP DEFAULT SYSTIMESTAMP,
                update_time                TIMESTAMP DEFAULT SYSTIMESTAMP,
                merchant_no                VARCHAR2(64)
            )';
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_DEWU_DTL IS ''得物对账单表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.ID IS ''主键ID''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.app_key IS ''应用标识''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.bill_no IS ''账单编号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.bill_start_time IS ''账单开始时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.bill_end_time IS ''账单结束时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.order_receive_amount IS ''应收订单金额（分）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.platform_service_fee IS ''应收平台服务费(分''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.advance_fee IS ''预付款金额（分）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.seller_subsidies_amount IS ''卖家补贴金额（分）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.platform_difference_amount IS ''平台差错调整（分）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.refund_amount IS ''退款金额（分）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.settle_amount IS ''应结金额（分''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.status IS ''结算状态''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.create_time IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.update_time IS ''对账单更新时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_DEWU_DTL.merchant_no IS ''商户号''';
END IF;
END;
/



-- 微信小店余额表
-- 表说明：微信小店余额表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_WEIXINSTORE_BAL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_WEIXINSTORE_BAL (
                ID               VARCHAR2(32) NOT NULL PRIMARY KEY,
                APP_ID           VARCHAR2(32) NOT NULL,
                GRANT_TYPE       VARCHAR2(32) NOT NULL,
                MCH_ID           VARCHAR2(64),
                BAL_DATE         TIMESTAMP NOT NULL,
                AVAILABLE_AMOUNT NUMBER(18,2) DEFAULT 0.00,
                PENDING_AMOUNT   NUMBER(18,2) DEFAULT 0.00,
                SUB_MCHID        VARCHAR2(32),
                CREATE_TIME      TIMESTAMP DEFAULT SYSTIMESTAMP
            )';
END IF;
END;
/


BEGIN
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_WEIXINSTORE_BAL IS ''微信小店余额表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_BAL.ID IS ''主键ID，使用MD5生成的唯一标识''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_BAL.APP_ID IS ''应用ID，微信小店的appid，用于标识不同的微信小店应用''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_BAL.GRANT_TYPE IS ''授权类型，通常为client_credential，用于获取微信API的访问令牌''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_BAL.MCH_ID IS ''商户号，用于标识不同的商户''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_BAL.BAL_DATE IS ''余额查询的日期时间，记录查询余额的具体时间点''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_BAL.AVAILABLE_AMOUNT IS ''可用余额，单位为元，可做提现操作的金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_BAL.PENDING_AMOUNT IS ''待结算余额，单位为元，暂不可提现的金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_BAL.SUB_MCHID IS ''二级商户号，用于服务商模式下标识子商户''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_BAL.CREATE_TIME IS ''记录创建时间，系统自动生成''';
END;
/


-- 微信小店资金流水明细表
-- 表说明：微信小店资金流水明细表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_WEIXINSTORE_FUND_BILL';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_WEIXINSTORE_FUND_BILL (
                ID               VARCHAR2(32) NOT NULL PRIMARY KEY,
                APP_ID           VARCHAR2(32) NOT NULL,
                GRANT_TYPE       VARCHAR2(32) NOT NULL,
                MCH_ID           VARCHAR2(64),
                FLOW_ID          VARCHAR2(64) NOT NULL,
                AMOUNT           NUMBER(18,2) DEFAULT 0.00,
                BALANCE          NUMBER(18,2) DEFAULT 0.00,
                FLOW_TYPE        VARCHAR2(2),
                FUNDS_TYPE       VARCHAR2(32),
                BOOKKEEPING_TIME TIMESTAMP,
                REMARK           VARCHAR2(255),
                CREATE_TIME      TIMESTAMP DEFAULT SYSTIMESTAMP
            )';
END IF;
END;
/


BEGIN
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_WEIXINSTORE_FUND_BILL IS ''微信小店资金流水明细表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.ID IS ''主键ID，使用流水ID和其他信息的MD5生成的唯一标识''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.APP_ID IS ''应用ID，微信小店的appid，用于标识不同的微信小店应用''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.MCH_ID IS ''商户号，用于标识不同的商户''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.GRANT_TYPE IS ''授权类型，通常为client_credential，用于获取微信API的访问令牌''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.FLOW_ID IS ''微信小店资金流水的唯一标识符，由微信平台生成''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.AMOUNT IS ''流水金额，单位为元，正数表示收入，负数表示支出''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.BALANCE IS ''账户余额，单位为元，该笔流水发生后的账户余额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.FLOW_TYPE IS ''流水类型，1表示收入，2表示支出''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.FUNDS_TYPE IS ''资金类型，枚举值：1:订单支付收入，2:订单手续费，3:退款，4:提现，5:提现失败退票，10:联盟抽佣，11:平台抽佣，12:团长抽佣，13:返佣人气卡，14:极速退款垫资金，15:极速退款垫资回补，16:运费险''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.BOOKKEEPING_TIME IS ''记账时间，微信平台记录的资金流水发生时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.REMARK IS ''备注信息，对该笔资金流水的说明''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_FUND_BILL.CREATE_TIME IS ''记录创建时间，系统自动生成''';
END;
/


-- 微信小店资金流水关联信息表
-- 表说明：微信小店资金流水关联信息表
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT FROM USER_TABLES WHERE TABLE_NAME = 'TP_WEIXINSTORE_RELATED_INFO';
IF VN_COUNT = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE TP_WEIXINSTORE_RELATED_INFO (
                ID                 VARCHAR2(32) NOT NULL PRIMARY KEY,
                APP_ID             VARCHAR2(32) NOT NULL,
                GRANT_TYPE         VARCHAR2(32) NOT NULL,
                BILL_ID            VARCHAR2(32) NOT NULL,
                RELATED_TYPE       VARCHAR2(32),
                ORDER_ID           VARCHAR2(64),
                AFTERSALE_ID       VARCHAR2(64),
                WITHDRAW_ID        VARCHAR2(64),
                TRANSACTION_ID     VARCHAR2(64),
                GUARANTEE_ID       VARCHAR2(64),
                PRESENT_ID         VARCHAR2(64),
                GROUP_PRESENT_LIST VARCHAR2(640),
                CREATE_TIME        TIMESTAMP DEFAULT SYSTIMESTAMP
            )';
END IF;
END;
/


BEGIN
EXECUTE IMMEDIATE 'COMMENT ON TABLE TP_WEIXINSTORE_RELATED_INFO IS ''微信小店资金流水关联信息表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.ID IS ''主键ID，使用流水ID和关联信息的MD5生成的唯一标识''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.APP_ID IS ''应用ID，微信小店的appid，用于标识不同的微信小店应用''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.GRANT_TYPE IS ''授权类型，通常为client_credential，用于获取微信API的访问令牌''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.BILL_ID IS ''关联的资金流水表主键ID，与TP_WEIXINSTORE_FUND_BILL表的ID字段关联''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.RELATED_TYPE IS ''关联类型, 1 订单， 2售后，3 提现，4 运费险， 5 服务保障， 6 送礼物, 7 群送礼关联订单号列表''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.ORDER_ID IS ''订单ID，当关联类型为ORDER时有值''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.AFTERSALE_ID IS ''售后单ID，当关联类型为AFTERSALE时有值''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.WITHDRAW_ID IS ''提现单ID，当关联类型为WITHDRAW时有值''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.TRANSACTION_ID IS ''交易ID，微信支付平台的交易流水号''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.GUARANTEE_ID IS ''担保单ID，当涉及担保交易时有值''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.PRESENT_ID IS ''赠送单ID，当涉及赠送交易时有值''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.GROUP_PRESENT_LIST IS ''群送礼关联订单号列表（群礼物与订单之间是一对多关系，因此多个订单号通过","来分隔）''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN TP_WEIXINSTORE_RELATED_INFO.CREATE_TIME IS ''记录创建时间，系统自动生成''';
END;
/



-- 电商账户流水视图
CREATE OR REPLACE VIEW V_TP_DS_FLOW_DTL AS
SELECT ID,'DS001' AS PLATFORM_CODE,APP_ID,MCH_ID AS MERCHANT_NO,BUSINESS_ID AS SERIAL_NO,TRADE_NO, '002' AS ACCOUNT_TYPE_CODE,
       CASE WHEN PAYEE_MONEY > 0 THEN 'IN'ELSE 'OUT'END AS DIRECTION_TYPE,TRADE_TYPE,HAPPEN_TIME AS TRADE_TIME,
       CASE WHEN PAYEE_MONEY > 0 THEN PAYEE_MONEY ELSE -PAYER_MONEY END AS TRADE_MONEY,'RMB' AS CURRENCY_TYPE, '' AS DESCRIPTION,REMARK AS REMARKS
FROM TP_TMALL_FUND_DTL
UNION ALL
SELECT ID,'DS003' AS PLATFORM_CODE,APP_KEY AS APP_ID,MCH_ID AS MERCHANT_NO,ACCOUNT_TRADE_NO AS SERIAL_NO,ORDER_ID AS TRADE_NO,CASE
                                                                                                                                  WHEN ACCOUNT_TYPE = '1' THEN '003'  -- 微信
                                                                                                                                  WHEN ACCOUNT_TYPE = '2' THEN '002'  -- 支付宝
                                                                                                                                  WHEN ACCOUNT_TYPE = '3' THEN '006'  -- 合众支付
                                                                                                                                  WHEN ACCOUNT_TYPE = '4' THEN '001'  -- 聚合支付
                                                                                                                                  ELSE ACCOUNT_TYPE
    END   AS ACCOUNT_TYPE_CODE,
       CASE WHEN FUND_FLOW = '0' THEN 'IN'ELSE 'OUT'END AS DIRECTION_TYPE,ORDER_TYPE_DESC AS TRADE_TYPE,BILL_TIME AS TRADE_TIME ,
       ACCOUNT_AMOUNT AS TRADE_MONEY,'RMB' AS CURRENCY_TYPE, ACCOUNT_BILL_DESC AS DESCRIPTION,REMARK AS REMARKS
FROM TP_DOUYIN_DTL
UNION ALL
SELECT ID,'DS004' AS PLATFORM_CODE,CLIENT_ID AS APP_ID,  MERCHANT_NO,'' AS SERIAL_NO,MERCHANT_ORDER_NO AS TRADE_NO, '001' AS ACCOUNT_TYPE_CODE,
       CASE WHEN INCOME_AMOUNT > 0  THEN 'IN'ELSE 'OUT'END AS DIRECTION_TYPE,ACCOUNT_TYPE AS TRADE_TYPE,OCCUR_TIME AS TRADE_TIME ,
       CASE WHEN INCOME_AMOUNT > 0 THEN INCOME_AMOUNT ELSE -EXPENSE_AMOUNT END AS TRADE_MONEY,'RMB' AS CURRENCY_TYPE, BUS_DESC AS DESCRIPTION,REMARK AS REMARKS
FROM TP_PINDUODUO_DTL
UNION ALL
SELECT ID,'DS005' AS PLATFORM_CODE,APP_KEY AS APP_ID,MCH_ID AS MERCHANT_NO,BOOK_TRANS_NO AS SERIAL_NO,OUT_ORDER_ID AS TRADE_NO, '005' AS ACCOUNT_TYPE_CODE,
       CASE WHEN IN_OUT_TYPE = 1 THEN 'IN'ELSE 'OUT'END AS DIRECTION_TYPE,BIZ_TYPE_DESC,BILL_TIME AS TRADE_TIME,
       AMOUNT AS TRADE_MONEY,'RMB' AS CURRENCY_TYPE, '' AS DESCRIPTION,REMARK AS REMARKS
FROM TP_KUAISHOU_FUND_DTL
UNION ALL
SELECT WF.ID,'DS007' AS PLATFORM_CODE,WF.APP_ID,MCH_ID AS MERCHANT_NO,FLOW_ID AS SERIAL_NO,WI.ORDER_ID AS TRADE_NO, '001' AS ACCOUNT_TYPE_CODE,
       CASE WHEN FLOW_TYPE = '1' THEN 'IN'ELSE 'OUT'END AS DIRECTION_TYPE,CASE FUNDS_TYPE
                                                                              WHEN '1'  THEN '订单支付收入'
                                                                              WHEN '2'  THEN '订单手续费'
                                                                              WHEN '3'  THEN '退款'
                                                                              WHEN '4'  THEN '提现'
                                                                              WHEN '5'  THEN '提现失败退票'
                                                                              WHEN '10' THEN '联盟抽佣'
                                                                              WHEN '11' THEN '平台抽佣'
                                                                              WHEN '12' THEN '团长抽佣'
                                                                              WHEN '13' THEN '返佣人气卡'
                                                                              WHEN '14' THEN '极速退款垫资金'
                                                                              WHEN '15' THEN '极速退款垫资回补'
                                                                              WHEN '16' THEN '运费险'
                                                                              ELSE FUNDS_TYPE
           END AS TRADE_TYPE,BOOKKEEPING_TIME AS TRADE_TIME,
       CASE WHEN AMOUNT > 0 THEN AMOUNT ELSE -AMOUNT END AS TRADE_MONEY,'RMB' AS CURRENCY_TYPE, '' AS DESCRIPTION,REMARK AS REMARKS
FROM TP_WEIXINSTORE_FUND_BILL WF
         LEFT JOIN
     TP_WEIXINSTORE_RELATED_INFO WI ON WF.ID = WI.BILL_ID where WI.RELATED_TYPE = '1'
UNION ALL
SELECT ID,'DS006' AS PLATFORM_CODE,APPKEY AS APP_ID,MCHID AS MERCHANT_NO,TRADENO AS SERIAL_NO,BUSINESSNO AS TRADE_NO,CASE
                                                                                                                         WHEN FUNDTYPE = 'NOT_ER_QING' THEN '001'  -- 店铺余额
                                                                                                                         WHEN FUNDTYPE = 'ER_QING_WECHAT' THEN '003'  -- 微信
                                                                                                                         WHEN FUNDTYPE = 'ER_QING_ALIPAY' THEN '002'  -- 支付宝
                                                                                                                         ELSE FUNDTYPE
    END AS ACCOUNT_TYPE_CODE,
       CASE WHEN INCOMEAMOUNT > 0  THEN 'IN'ELSE 'OUT'END AS DIRECTION_TYPE,TYPEDESC AS TRADE_TYPE,CREATEDTIME AS TRADE_TIME ,
       CASE WHEN INCOMEAMOUNT > 0 THEN INCOMEAMOUNT ELSE OUTCOMEAMOUNT END AS TRADE_MONEY,'RMB' AS CURRENCY_TYPE, '' AS DESCRIPTION,REMARK AS REMARKS
FROM TP_XIAOHONGSHU_DTL
UNION ALL
SELECT ID,'DS008' AS PLATFORM_CODE,APP_KEY AS APP_ID,MERCHANT_NO AS MERCHANT_NO,BILL_NO AS SERIAL_NO,'' AS TRADE_NO, '001' AS ACCOUNT_TYPE_CODE,
       CASE WHEN SETTLE_AMOUNT > 0  THEN 'IN'ELSE 'OUT'END AS DIRECTION_TYPE,'' AS TRADE_TYPE,BILL_START_TIME AS TRADE_TIME ,
       CASE WHEN SETTLE_AMOUNT > 0 THEN SETTLE_AMOUNT ELSE -SETTLE_AMOUNT END AS TRADE_MONEY,'RMB' AS CURRENCY_TYPE, '' AS DESCRIPTION,STATUS AS REMARKS
FROM TP_DEWU_DTL;
/


--余额电商账户余额视图
CREATE OR REPLACE VIEW V_TP_DS_BAL AS
SELECT ID,'DS001' AS PLATFORM_CODE,APP_ID,MCH_ID AS MERCHANT_NO, '002' AS ACCOUNT_TYPE_CODE,BAL_DATE AS BALANCE_DATE , TOTAL_AMOUNT AS BALANCE,
       AVAILABLE_AMOUNT AS ENABLE_AMOUNT,FREEZE_AMOUNT AS DISABLE_AMOUNT,'RMB' AS CURRENCY_TYPE
FROM TP_TMALL_BAL
UNION ALL
SELECT ID,'DS005' AS PLATFORM_CODE,APP_KEY as APP_ID,MCH_ID AS MERCHANT_NO, CASE
                                                                                WHEN ACCOUNT_CHANNEL = '1' THEN '001'  -- 小店余额  聚合结算账户
                                                                                WHEN ACCOUNT_CHANNEL = '2' THEN '003'  -- 微信
                                                                                WHEN ACCOUNT_CHANNEL = '3' THEN '002'  -- 支付宝
                                                                                WHEN ACCOUNT_CHANNEL = '4' THEN '005'  -- 安心钱包
                                                                                ELSE ACCOUNT_CHANNEL  -- 保留其他值不变
    END   AS ACCOUNT_TYPE_CODE,BAL_DATE AS BALANCE_DATE , TOTAL_AMOUNT AS BALANCE,
       AVAILABLE_AMOUNT AS ENABLE_AMOUNT,FREEZE_AMOUNT AS DISABLE_AMOUNT,'RMB' AS CURRENCY_TYPE
FROM TP_KUAISHOU_BAL WHERE ACCOUNT_CHANNEL <> '5'
UNION ALL
SELECT ID,'DS007' AS PLATFORM_CODE,APP_ID,MCH_ID AS MERCHANT_NO, '001' AS ACCOUNT_TYPE_CODE,BAL_DATE AS BALANCE_DATE , AVAILABLE_AMOUNT + PENDING_AMOUNT AS BALANCE,
       AVAILABLE_AMOUNT AS ENABLE_AMOUNT,PENDING_AMOUNT AS DISABLE_AMOUNT,'RMB' AS CURRENCY_TYPE
FROM TP_WEIXINSTORE_BAL;
/


--电商账户保证金余额视图
CREATE OR REPLACE VIEW V_TP_DS_BOND_BAL AS
SELECT ID,'DS005' AS PLATFORM_CODE,APP_KEY as APP_ID,MCH_ID AS MERCHANT_NO, CASE
                                                                                WHEN SECUTITY_DEPOSIT_TYPE = 1 THEN '007'  -- 自建保证金
                                                                                WHEN SECUTITY_DEPOSIT_TYPE = 2 THEN '009'  -- 推广保证金
                                                                                WHEN SECUTITY_DEPOSIT_TYPE = 7 THEN '010'  -- 跨境保证金
                                                                                ELSE TO_CHAR(SECUTITY_DEPOSIT_TYPE)  -- 保留其他值不变
    END   AS ACCOUNT_TYPE_CODE,BAL_DATE AS BALANCE_DATE , TOTAL_AMOUNT AS BALANCE,
       TOTAL_AMOUNT - DEPOSIT_LOW_BALANCE AS ENABLE_AMOUNT,DEPOSIT_LOW_BALANCE AS DISABLE_AMOUNT,'RMB' AS CURRENCY_TYPE
FROM TP_KUAISHOU_BAL WHERE ACCOUNT_CHANNEL = '5';
/


--电商账户保证金流水视图
CREATE OR REPLACE VIEW V_TP_DS_BOND_FLOW_DTL AS
SELECT ID,'DS001' AS PLATFORM_CODE,APP_ID,MCH_ID AS MERCHANT_NO,TRANS_LOG_ID AS SERIAL_NO,'' AS TRADE_NO, '007' AS ACCOUNT_TYPE_CODE,
       CASE WHEN BAIL_AMOUNT > 0 THEN 'IN'ELSE 'OUT'END AS DIRECTION_TYPE,BAIL_TYPE as TRADE_TYPE,TRANS_DT AS TRADE_TIME,
       CASE WHEN BAIL_AMOUNT > 0 THEN BAIL_AMOUNT ELSE -BAIL_AMOUNT END AS TRADE_MONEY,'RMB' AS CURRENCY_TYPE, BIZ_DESC AS DESCRIPTION,BAIL_MEMO AS REMARKS FROM TP_TMALL_BAIL_BILL;

commit;
/



-- 创建索引
DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT
FROM USER_INDEXES
WHERE table_name = 'TP_OFFLINE_BAL'
  AND index_name = 'IDX_BALANCE_DATE';
IF VN_COUNT = 0 THEN
   EXECUTE IMMEDIATE 'CREATE INDEX IDX_BALANCE_DATE ON TP_OFFLINE_BAL (BALANCE_DATE)';
END IF;
COMMIT;
END;
/



DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT
FROM USER_INDEXES
WHERE table_name = 'TP_OFFLINE_BAL'
  AND index_name = 'IDX_MCNO_DATE';
IF VN_COUNT = 0 THEN
   EXECUTE IMMEDIATE 'CREATE INDEX IDX_MCNO_DATE ON TP_OFFLINE_BAL (MERCHANT_NO, BALANCE_DATE)';
END IF;
COMMIT;
END;
/


DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT
FROM USER_INDEXES
WHERE table_name = 'TP_OFFLINE_FLOW_DTL'
  AND index_name = 'IDX_TRADE_TIME';
IF VN_COUNT = 0 THEN
   EXECUTE IMMEDIATE 'CREATE INDEX IDX_TRADE_TIME ON TP_OFFLINE_FLOW_DTL (TRADE_TIME)';
END IF;
COMMIT;
END;
/



DECLARE
VN_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO VN_COUNT
FROM USER_INDEXES
WHERE table_name = 'TP_OFFLINE_FLOW_DTL'
  AND index_name = 'IDX_MCNO_TIME';
IF VN_COUNT = 0 THEN
   EXECUTE IMMEDIATE 'CREATE INDEX IDX_MCNO_TIME ON TP_OFFLINE_FLOW_DTL (MERCHANT_NO, TRADE_TIME)';
END IF;
COMMIT;
END;
/


DECLARE
  VN_COUNT NUMBER;
BEGIN
  SELECT COUNT(*)
    INTO VN_COUNT
    FROM USER_INDEXES
   WHERE INDEX_NAME = 'IDX_MERCHANTNO_TRADETIME'
     AND TABLE_NAME = 'TP_OFFLINE_BOND_FLOW_DTL';
  IF VN_COUNT =0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX IDX_MERCHANTNO_TRADETIME ON TP_OFFLINE_BOND_FLOW_DTL(MERCHANT_NO, TRADE_TIME)';
  END IF;
  COMMIT;
END;
/


DECLARE
  VN_COUNT NUMBER;
BEGIN
  SELECT COUNT(*)
    INTO VN_COUNT
    FROM USER_INDEXES
   WHERE INDEX_NAME = 'IDX_MERCHANTNO_BALANCEDATE'
     AND TABLE_NAME = 'TP_OFFLINE_BOND_BAL';
  IF VN_COUNT =0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX IDX_MERCHANTNO_BALANCEDATE ON TP_OFFLINE_BOND_BAL(MERCHANT_NO, BALANCE_DATE)';
  END IF;
  COMMIT;
END;
/


DECLARE
v_count NUMBER;
BEGIN
    -- 查询列是否存在
SELECT COUNT(1)
INTO v_count
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'BT_WARN_STRATEGY'
  AND COLUMN_NAME = 'NET_ID';

IF v_count = 0 THEN
        -- 不存在，新增列
        EXECUTE IMMEDIATE 'ALTER TABLE BT_WARN_STRATEGY ADD (NET_ID VARCHAR2(500))';
ELSE
        -- 已存在，修改列长度
        EXECUTE IMMEDIATE 'ALTER TABLE BT_WARN_STRATEGY MODIFY (NET_ID VARCHAR2(500))';
END IF;
END;
/


DECLARE
v_count NUMBER;
BEGIN
    -- 查询列是否存在
SELECT COUNT(1)
INTO v_count
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'FEX_TRAN_REQ'
  AND COLUMN_NAME = 'BANK_PAY_TYPE';

IF v_count = 0 THEN
        -- 不存在，新增列
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ ADD (BANK_PAY_TYPE VARCHAR2(50))';
ELSE
        -- 已存在，修改列长度
        EXECUTE IMMEDIATE 'ALTER TABLE FEX_TRAN_REQ MODIFY (BANK_PAY_TYPE VARCHAR2(50))';
END IF;
END;
/

DECLARE
v_count NUMBER;
BEGIN
    -- 查询列是否存在
SELECT COUNT(1)
INTO v_count
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'BIS_FOREIGN_EXC'
  AND COLUMN_NAME = 'BANK_PAY_TYPE';

IF v_count = 0 THEN
        -- 不存在，新增列
        EXECUTE IMMEDIATE 'ALTER TABLE BIS_FOREIGN_EXC ADD (BANK_PAY_TYPE VARCHAR2(50))';
ELSE
        -- 已存在，修改列长度
        EXECUTE IMMEDIATE 'ALTER TABLE BIS_FOREIGN_EXC MODIFY (BANK_PAY_TYPE VARCHAR2(50))';
END IF;
END;
/

DECLARE
v_count NUMBER;
BEGIN
    -- 查询列是否存在
SELECT COUNT(1)
INTO v_count
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'BIS_EXC'
  AND COLUMN_NAME = 'BANK_PAY_TYPE';

IF v_count = 0 THEN
        -- 不存在，新增列
        EXECUTE IMMEDIATE 'ALTER TABLE BIS_EXC ADD (BANK_PAY_TYPE VARCHAR2(50))';
ELSE
        -- 已存在，修改列长度
        EXECUTE IMMEDIATE 'ALTER TABLE BIS_EXC MODIFY (BANK_PAY_TYPE VARCHAR2(50))';
END IF;
END;
/