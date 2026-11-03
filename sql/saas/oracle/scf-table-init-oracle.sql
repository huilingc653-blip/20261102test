-- ============================================
-- Oracle 版本: scf-table-init-oracle.sql
-- 说明: 供应链金融相关表结构初始化脚本
-- ============================================

-- ============================================
-- 1. 应付债权凭证表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_PAYABLE_VOUCHER';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_payable_voucher (
            id VARCHAR2(32) NOT NULL,
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
            tenant_id VARCHAR2(32) NULL,
            CONSTRAINT PK_SCF_PAYABLE_VOUCHER PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_payable_voucher IS ''应付债权凭证表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.id IS ''id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.voucher_no IS ''单据编号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.voucher_code IS ''应付债券凭证编号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.template_id IS ''债权凭证模版id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.version_id IS ''预留 可后续添加模板版本id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.platform_id IS ''平台''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.product_id IS ''产品ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.corp_id IS ''企业ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.open_date IS ''开立日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.expire_date IS ''到期日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.core_corp IS ''核心企业''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.core_corp_name IS ''核心企业名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.cur_id IS ''币种ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.voucher_money IS ''凭证金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.supply_id IS ''供应商ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.supply_name IS ''供应商名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.receive_account IS ''收款账户''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.receive_bank IS ''收款银行''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.online_sign IS ''线上签章''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.quota_code IS ''额度编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.quota_name IS ''额度名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.quota_balance IS ''额度余额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.contract_code IS ''合同编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.contract_name IS ''合同名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.contract_money IS ''合同金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.contract_date IS ''合同日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.contract_file IS ''合同文件''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.invoice_file IS ''发票文件''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.voucher_source IS ''单据来源(手工新增：MANUAL，ERP，供应链金融平台:SUPPLY_PLATFORM)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.interest_mode IS ''融资付息方式(1:卖方付息，2:买方付息，默认卖方付息)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.promise_pay_date IS ''承诺付款日''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.supply_credit_code IS ''供应商统一社会信用代码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.approval_status IS ''审批状态''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.voucher_status IS ''业务状态''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.pay_id IS ''关联付款单ID(付款单审批通过后写入，选票时排除已关联凭证)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.pay_no IS ''关联付款单号(付款单审批通过后写入，选票时排除已关联凭证)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.cancel_reason IS ''作废原因''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.cancel_date IS ''作废时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.create_by IS ''创建人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.create_date IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.update_by IS ''修改人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.update_date IS ''修改时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_payable_voucher.tenant_id IS ''租户ID''';
    END IF;
END;
/

-- ============================================
-- 2. 应付债权凭证自定义字段数据表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_VOUCHER_CUSTOM_FIELD';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_voucher_custom_field (
            id VARCHAR2(32) NOT NULL,
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
            tenant_id VARCHAR2(32) NULL,
            CONSTRAINT PK_SCF_VOUCHER_CUSTOM_FIELD PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_voucher_custom_field IS ''应付债权凭证数据表自定义字段数据''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.id IS ''id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.voucher_id IS ''凭证id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.field_id IS ''字段id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.field_code IS ''自定义字段编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.field_name IS ''自定义字段名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.field_type IS ''自定义字段类型''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.field_value IS ''自定义字段值''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.create_by IS ''创建人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.create_date IS ''创建日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.update_by IS ''更新人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.update_date IS ''更新日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_custom_field.tenant_id IS ''租户ID''';
    END IF;
END;
/

-- ============================================
-- 3. 凭证兑付记录表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_VOUCHER_REDEMPTION';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_voucher_redemption (
            id VARCHAR2(32) NOT NULL,
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
            tenant_id VARCHAR2(32) NULL,
            CONSTRAINT PK_SCF_VOUCHER_REDEMPTION PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_voucher_redemption IS ''凭证兑付记录表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.id IS ''id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.voucher_id IS ''应付凭证ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.cash_no IS ''兑付单号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.platform_id IS ''平台id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.product_id IS ''融资产品''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.corp_id IS ''开立单位''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.open_date IS ''开立日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.expire_date IS ''到期日''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.core_corp IS ''核心企业''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.cur_id IS ''币别''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.voucher_money IS ''凭证金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.supply_id IS ''供应商''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.supply_name IS ''供应商''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.pay_account IS ''付款账号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.cash_money IS ''兑付金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.pay_date IS ''付款日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.receive_name IS ''收款方名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.receive_account IS ''收款方账号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.receive_bank IS ''收款开户行''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.approval_status IS ''审批状态''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_voucher_redemption.tenant_id IS ''租户ID''';
    END IF;
END;
/

-- ============================================
-- 4. 指令表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_INSTRUCTION';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE SCF_INSTRUCTION (
            ID VARCHAR2(32) NOT NULL,
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
            TENANT_ID VARCHAR2(32) NULL,
            CONSTRAINT PK_SCF_INSTRUCTION PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE SCF_INSTRUCTION IS ''指令表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.ID IS ''主键ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_CODE IS ''指令编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_TYPE IS ''指令类型''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_TYPE_NAME IS ''指令类型名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.PLATFORM_ID IS ''供应链平台ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.PLATFORM_NAME IS ''供应链平台名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.PRODUCT_ID IS ''融资产品ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.PRODUCT_NAME IS ''融资产品名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.DIRECT_IF_ID IS ''直联接口ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.VOUCHER_CODE IS ''债权凭证编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.RELATE_NO IS ''关联编号(转让记录编号/融资单号等)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.CORP_ID IS ''开立单位ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.CORP_NAME IS ''开立单位名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.CUR_ID IS ''币别ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.CUR_NAME IS ''币别名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_AMT IS ''指令金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.INSTR_DATE IS ''指令日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.EXPIRE_DATE IS ''到期日''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.TRANS_STATUS IS ''交易状态''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.TRANS_REASON IS ''交易原因''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.SIGN IS ''应付应收标识(0为应付，1为应收)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.CREATE_BY IS ''创建人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.CREATE_DATE IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.UPDATE_BY IS ''修改人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.UPDATE_DATE IS ''修改时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN SCF_INSTRUCTION.TENANT_ID IS ''租户ID''';
    END IF;
END;
/

-- ============================================
-- 5. 平台接口开通配置表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_ENT_BANK_OPEN';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_ent_bank_open (
            id VARCHAR2(32) NOT NULL,
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
            update_date DATE NULL,
            CONSTRAINT PK_SCF_ENT_BANK_OPEN PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_ent_bank_open IS ''平台接口开通配置表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.id IS ''主键ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.type IS ''接口类型''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.api_name_cn IS ''接口名称（中文）''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.api_name IS ''接口名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.has_direct IS ''是否具备直连接口(0-否 1-是)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.direct IS ''直连接口''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.ent_platform_id IS ''平台ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.tenant_id IS ''租户ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.create_by IS ''创建人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.create_date IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.update_by IS ''修改人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_ent_bank_open.update_date IS ''修改时间''';
    END IF;
END;
/

-- ============================================
-- 6. 资金方信息表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_FUNDER_INFO';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_funder_info (
            id VARCHAR2(32) NOT NULL,
            code VARCHAR2(50) NULL,
            type VARCHAR2(10) NULL,
            bank_no VARCHAR2(50) NULL,
            name VARCHAR2(200) NULL,
            sign VARCHAR2(100) NULL,
            valid VARCHAR2(10) DEFAULT ''1'' NULL,
            rmk VARCHAR2(500) NULL,
            tenant_id VARCHAR2(32) NULL,
            create_by VARCHAR2(32) NULL,
            create_date DATE NULL,
            update_by VARCHAR2(32) NULL,
            update_date DATE NULL,
            CONSTRAINT PK_SCF_FUNDER_INFO PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_funder_info IS ''资金方信息表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.id IS ''主键ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.code IS ''资金方编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.type IS ''机构类型 01-保理公司 02-银行 03-其他''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.bank_no IS ''银行行号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.name IS ''资金方名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.sign IS ''资金方标识''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.valid IS ''有效标识 1-有效 0-无效''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.rmk IS ''备注''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.tenant_id IS ''租户id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.create_by IS ''创建人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.create_date IS ''创建日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.update_by IS ''更新人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_funder_info.update_date IS ''更新日期''';
    END IF;
END;
/

-- ============================================
-- 7. 融资业务主信息表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_LOAN_INFO';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_loan_info (
            id VARCHAR2(64) NOT NULL,
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
            interest_mode VARCHAR2(32) DEFAULT ''1'' NULL,
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
            loan_status VARCHAR2(30) DEFAULT ''UN_LOAN'' NULL,
            online_sign VARCHAR2(1) NULL,
            enable VARCHAR2(2) DEFAULT ''1'' NULL,
            create_by VARCHAR2(32) NULL,
            create_date DATE NULL,
            update_by VARCHAR2(32) NULL,
            update_date DATE NULL,
            CONSTRAINT PK_SCF_LOAN_INFO PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_loan_info IS ''融资业务主信息表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.id IS ''主键ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_no IS ''融资单号(RZ+年月日+3位流水号)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.tenant_id IS ''租户id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_company_id IS ''融资企业id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_company_name IS ''融资企业名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.voucher_code IS ''债权凭证编号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.core_company IS ''核心企业''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.core_company_id IS ''核心企业id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.funder_id IS ''资金方id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.funder_name IS ''资金方名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.product_id IS ''融资产品id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.product_name IS ''融资产品名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.platform_id IS ''平台ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.platform_name IS ''平台名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.cur_id IS ''币别ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.cur_name IS ''币别名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_apply_date IS ''融资申请日''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_amt IS ''融资金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.interest_mode IS ''付息方式(1:卖方付息，2:买方付息，默认卖方付息)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_lend_date IS ''放款日(默认系统当前日期)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_actual_date IS ''实际放款日''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_due_date IS ''到期日''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_deadline IS ''期限(到期日-放款日) 天''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_rate IS ''融资利率''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_interest_amt IS ''利息金额(融资金额*融资利率)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_hand_fee_rate IS ''手续费率''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_hand_fee_amt IS ''手续费(融资金额*手续费率)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_service_fee_rate IS ''服务费率''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_service_fee_amt IS ''服务费(融资金额*服务费率)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_cost_amt IS ''融资成本(利息金额+手续费+服务费)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_actual_amt IS ''融资到账金额(融资金额-融资成本)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_payment_account IS ''收款账号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_payment_account_name IS ''收款开户名''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_payment_account_bank IS ''收款开户行''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.receivable_id IS ''应收ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.loan_status IS ''融资审批状态(DRAFT:暂存; APPROVAL_ING:审批中; REJECTED:打回; APPROVAL_PASS:审批通过)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.online_sign IS ''线上线下标识(0:线下；1:线上)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.enable IS ''是否有效(0：无效；1：有效)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.create_by IS ''创建人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.create_date IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.update_by IS ''修改人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_loan_info.update_date IS ''修改时间''';
    END IF;
END;
/

-- ============================================
-- 8. 应收债权凭证表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_RECEIVABLE_VOUCHER_INFO';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_receivable_voucher_info (
            id VARCHAR2(32) NOT NULL,
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
            allow_split VARCHAR2(2) DEFAULT ''1'' NULL,
            contract_code VARCHAR2(50) NULL,
            contract_name VARCHAR2(200) NULL,
            contract_amt NUMBER(15,2) DEFAULT 0.00 NULL,
            contract_date DATE NULL,
            contract_file_id VARCHAR2(32) NULL,
            invoice_file_id VARCHAR2(32) NULL,
            interest_mode VARCHAR2(32) DEFAULT ''1'' NULL,
            promise_pay_date DATE NULL,
            transfer_company VARCHAR2(200) NULL,
            transfer_company_id VARCHAR2(32) NULL,
            enable NUMBER(1) DEFAULT 1 NULL,
            create_by VARCHAR2(32) NULL,
            create_date DATE DEFAULT SYSDATE NULL,
            update_by VARCHAR2(32) NULL,
            update_date DATE NULL,
            CONSTRAINT PK_SCF_RECEIVABLE_VOUCHER_INFO PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_receivable_voucher_info IS ''应收债权凭证表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.id IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.voucher_no IS ''单据编号(系统自动创建AR+年月日+3位流水号)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.voucher_source IS ''单据来源(手工新增：MANUAL，ERP，供应链金融平台:SUPPLY_PLATFORM)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.tenant_id IS ''租户id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.voucher_code IS ''债权凭证编号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.voucher_amt IS ''凭证金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.platform_id IS ''平台ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.platform_name IS ''平台名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.product_id IS ''融资产品id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.product_name IS ''融资产品名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.corp_id IS ''持有单位''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.corp_name IS ''持有单位名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.sign_date IS ''签收日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.sign_operation IS ''签收操作(签收,拒签,作废)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.voucher_type IS ''凭证类别(开立凭证,流转凭证)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.expire_date IS ''凭证到期日''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.voucher_status IS ''凭证业务状态(未签收,已签收,已拒签,已拆分)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.approval_status IS ''单据状态(暂存,审批中,打回,审批通过,已作废)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.core_company_id IS ''核心企业''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.core_company_name IS ''核心企业名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.cur_id IS ''币种ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.cur_name IS ''币别名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.issue_transfer_company IS ''开立/转让 企业''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.issue_transfer_company_id IS ''开立/转让 企业id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.source_voucher_code IS ''根凭证编号(开立成功时生成的债权凭证编号)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.forehand_voucher_code IS ''上级凭证编号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.online_sign IS ''线上线下标识''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.allow_split IS ''是否允许拆分(0:否, 1:是)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.contract_code IS ''合同编码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.contract_name IS ''合同名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.contract_amt IS ''合同金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.contract_date IS ''合同日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.contract_file_id IS ''合同文件id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.invoice_file_id IS ''发票文件id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.interest_mode IS ''融资付息方式(1:卖方付息,2:买方付息)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.promise_pay_date IS ''承诺付款日''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.transfer_company IS ''流转单位''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.transfer_company_id IS ''流转单位ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.enable IS ''是否有效''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.create_by IS ''创建人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.create_date IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.update_by IS ''修改人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_voucher_info.update_date IS ''修改时间''';
    END IF;
END;
/

-- ============================================
-- 9. 债权凭证转让信息表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_RECEIVABLE_TRANSFER_INFO';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_receivable_transfer_info (
            id VARCHAR2(32) NOT NULL,
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
            cur_id VARCHAR2(32) DEFAULT ''CNY'' NULL,
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
            update_date DATE DEFAULT SYSDATE NULL,
            CONSTRAINT PK_SCF_RECEIVABLE_TRANSFER PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_receivable_transfer_info IS ''债权凭证转让信息表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.id IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.transfer_no IS ''凭证转让记录编号(系统自动创建ZR+年月日+3位流水号)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.transfer_batch_no IS ''转让批次号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.tenant_id IS ''租户id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.voucher_code IS ''债权凭证编号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.transfer_status IS ''凭证转让状态''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.transfer_flag IS ''转让标识(1:一对多-凭证拆分转让多家企业;2:多对一-多张凭证转让一家企业)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.platform_id IS ''平台''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.platform_name IS ''平台名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.product_id IS ''融资产品id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.product_name IS ''融资产品''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.issue_transfer_company IS ''开立/转让单位''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.issue_transfer_company_id IS ''开立/转让 企业id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.cur_id IS ''币别''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.cur_name IS ''币别名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.receive_company IS ''接收单位''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.receive_company_id IS ''接收单位名称id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.receiver_credit_code IS ''接收方统一社会信用代码''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.transfer_date IS ''转让日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.transfer_amt IS ''转让金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.purpose IS ''用途''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.remark IS ''备注''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.pay_id IS ''关联付款单ID(付款单审批通过后写入，选票时排除已关联转让)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.pay_no IS ''关联付款单号(付款单审批通过后写入，选票时排除已关联转让)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.create_by IS ''创建人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.create_date IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.update_by IS ''修改人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_transfer_info.update_date IS ''更新时间''';
    END IF;
END;
/

-- ============================================
-- 10. 债权凭证收款明细表
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'SCF_RECEIVABLE_PAYMENT_INFO';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_receivable_payment_info (
            id VARCHAR2(32) NOT NULL,
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
            cur_id VARCHAR2(32) DEFAULT ''CNY'' NULL,
            cur_name VARCHAR2(16) NULL,
            voucher_amt NUMBER(15,2) DEFAULT 0.00 NULL,
            core_company_id VARCHAR2(128) NULL,
            core_company_name VARCHAR2(200) NULL,
            rec_account_no VARCHAR2(64) NULL,
            rec_account_name VARCHAR2(128) NULL,
            rec_bank VARCHAR2(128) NULL,
            rec_amt NUMBER(15,2) DEFAULT 0.00 NULL,
            receipt_date DATE NULL,
            enable VARCHAR2(2) DEFAULT ''1'' NULL,
            create_by VARCHAR2(32) NULL,
            create_date DATE DEFAULT SYSDATE NULL,
            update_by VARCHAR2(32) NULL,
            update_date DATE DEFAULT SYSDATE NULL,
            CONSTRAINT PK_SCF_RECEIVABLE_PAYMENT PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_receivable_payment_info IS ''债权凭证收款明细表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.id IS ''主键''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.voucher_code IS ''债权凭证编号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.payment_no IS ''收款单号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.tenant_id IS ''租户id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.payment_status IS ''收款状态''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.platform_id IS ''平台''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.platform_name IS ''平台名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.product_id IS ''融资产品''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.product_name IS ''融资产品id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.receipt_company IS ''收款单位''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.receipt_company_id IS ''收款单位id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.sign_date IS ''签收日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.expire_date IS ''到期日''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.issue_transfer_company IS ''开立/转让单位''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.issue_transfer_company_id IS ''开立/转让 企业id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.cur_id IS ''币别''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.cur_name IS ''币别名称''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.voucher_amt IS ''凭证金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.core_company_id IS ''核心企业id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.core_company_name IS ''核心企业''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.rec_account_no IS ''收款账号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.rec_account_name IS ''收款户名''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.rec_bank IS ''收款开户行''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.rec_amt IS ''收款金额''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.receipt_date IS ''收款日期''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.enable IS ''是否有效(0：无效；1：有效)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.create_by IS ''创建人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.create_date IS ''创建时间''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.update_by IS ''修改人''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_receivable_payment_info.update_date IS ''更新时间''';
    END IF;
END;
/

-- ============================================
-- 11. 应付/应收债券发票信息表
-- ============================================
DECLARE
vn_count NUMBER;
BEGIN
SELECT COUNT(*) INTO vn_count
FROM user_tables
WHERE table_name = 'SCF_INVOICE_INFO';
IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE scf_invoice_info (
            id VARCHAR2(32) NOT NULL,
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
            update_date DATE DEFAULT SYSDATE,
            CONSTRAINT PK_SCF_INVOICE_INFO PRIMARY KEY (id)
        )';

EXECUTE IMMEDIATE 'COMMENT ON TABLE scf_invoice_info IS ''应付应收债券发票信息''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.id IS ''id''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.tenant_id IS ''租户id''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.invoice_type IS ''发票类型''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.invoice_number IS ''发票号码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.invoice_code IS ''发票代码''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.bill_date IS ''开票日期''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.cur_name IS ''币种名称''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.cur_id IS ''币种''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.total_amt IS ''价税合计''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.tax_rate IS ''税率''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.tax_amt IS ''税额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.bill_amt IS ''金额''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.seller_name IS ''销方名称''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.buyer_name IS ''购方名称''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.pay_id IS ''应付凭证id''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.receivable_id IS ''应收凭证id''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.sign IS ''应付应收标识(0为应付，1为应收)''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.create_by IS ''创建人''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.create_date IS ''创建时间''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.update_by IS ''修改人''';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN scf_invoice_info.update_date IS ''更新时间''';
END IF;
END;
/


-- ============================================
-- 12. 菜单与资源（tenant_pre_menu + tenant_pre_resources）
-- ============================================

-- 供应链金融管理（顶级）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '45ee4780ab564ff7a4117ae17bedc052v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('45ee4780ab564ff7a4117ae17bedc052v1', '1', '供应链金融管理', '供应链金融管理', NULL, NULL, 10, 1, NULL, NULL, 'icon-jggxgl', NULL, NULL, NULL, NULL, 1, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 基础配置
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = 'dfcea55a6c734e25bb8c62c6ba6e4dedv1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('dfcea55a6c734e25bb8c62c6ba6e4dedv1', '2', '基础配置', '基础配置', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 参数配置
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '9bb2d2fe514c421aa862d01ba5db9137v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('9bb2d2fe514c421aa862d01ba5db9137v1', '3', '参数配置', '参数配置', 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/baseConfig/parameterSetting', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 平台直联设置
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '157d094324234e2ab7673a373e5883ffv1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('157d094324234e2ab7673a373e5883ffv1', '4', '平台直联设置', '平台直联设置', 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/baseConfig/platformDirectSetting', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 查询资源（平台直联）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '395734ec80e2787b932154ce9d3383e6v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('395734ec80e2787b932154ce9d3383e6v1', '查询资源', '查询资源', 'scf:ent:platform:query', 1, '157d094324234e2ab7673a373e5883ffv1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应用资源（平台直联）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '8v5734ec80e2497b932154ce9d3383e8v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('8v5734ec80e2497b932154ce9d3383e8v1', '应用资源', '应用资源', 'scf:ent:platform:apply', 1, '157d094324234e2ab7673a373e5883ffv1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 资金方（菜单）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = 'ff2db81e59d9483eb525fc978b52bdc9v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('ff2db81e59d9483eb525fc978b52bdc9v1', '5', '资金方', '资金方', 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', NULL, 3, 1, '/sub-finance/#/supplyChainFinance/baseConfig/funder', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 查询资源（资金方）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'dc101004d4ee40c5b1696cdb0c1c4b41';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('dc101004d4ee40c5b1696cdb0c1c4b41', '查询资源', '查询资源', 'scf:funder:info:query', 1, 'ff2db81e59d9483eb525fc978b52bdc9v1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应用资源（资金方）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '90243552241343278f9e25ff13f1dcce';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('90243552241343278f9e25ff13f1dcce', '应用资源', '应用资源', 'scf:funder:info:apply', 1, 'ff2db81e59d9483eb525fc978b52bdc9v1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 电子债权凭证
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '4dac33e0226249eab31cb7313382174fv1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('4dac33e0226249eab31cb7313382174fv1', '6', '电子债权凭证', '电子债权凭证', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权凭证管理
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = 'd2c496569f454cdea106158935f0d676v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('d2c496569f454cdea106158935f0d676v1', '7', '应付债权凭证管理', '应付债权凭证管理', '4dac33e0226249eab31cb7313382174fv1', NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权凭证
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '9b856832b17243b19d317d4dd2766eb9v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('9b856832b17243b19d317d4dd2766eb9v1', '8', '应付债权凭证', '应付债权凭证', 'd2c496569f454cdea106158935f0d676v1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/payable-voucher', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权凭证查询资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a1b2c3d4e5f64789a1b2c3d4e5f60001v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a1b2c3d4e5f64789a1b2c3d4e5f60001v1', '应付债权凭证查询', '应付债权凭证查询', 'scf:payable:voucher:query', 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权凭证修改资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '4e5f6a1b2c3d4789a1b2c3d4e5f60002v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('4e5f6a1b2c3d4789a1b2c3d4e5f60002v1', '应付债权凭证修改', '应付债权凭证修改', 'scf:payable:voucher:modify', 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 兑付记录
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '2a765b4789cc483ea87aa6f36a5d54f5v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('2a765b4789cc483ea87aa6f36a5d54f5v1', '9', '兑付记录', '兑付记录', 'd2c496569f454cdea106158935f0d676v1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/voucher-redemption', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 兑付记录查询资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '64789aa1b2c3d4e5f1b2c3d4e5f60001v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('64789aa1b2c3d4e5f1b2c3d4e5f60001v1', '兑付记录查询', '兑付记录查询', 'scf:payable:redemption:query', 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 兑付记录修改资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '789a1b2c3d4e4e5f6a1b2c3d45f60002v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('789a1b2c3d4e4e5f6a1b2c3d45f60002v1', '兑付记录修改', '兑付记录修改', 'scf:payable:redemption:modify', 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证管理
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '37e5540ee2f24ca2b6d39ffb2cc37072v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('37e5540ee2f24ca2b6d39ffb2cc37072v1', '8', '应收债权凭证管理', '应收债权凭证管理', '4dac33e0226249eab31cb7313382174fv1', NULL, 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '5a5166c7a21645efaa2c3fcdf446bc71v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('5a5166c7a21645efaa2c3fcdf446bc71v1', '10', '应收债权凭证', '应收债权凭证', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 1, 1, '/sub-finance/#/electronicDebt/receiveGroup/list', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 转让记录
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '5cff354932f341ccbe9f1f6a26f78a56v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('5cff354932f341ccbe9f1f6a26f78a56v1', '11', '转让记录', '转让记录', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 2, 1, '/sub-finance/#/electronicDebt/receiveGroup/transfer', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 融资记录
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '5a5166c7a21645efaa2c3fcdf446bc72v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('5a5166c7a21645efaa2c3fcdf446bc72v1', '12', '融资记录', '融资记录', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 3, 1, '/sub-finance/#/electronicDebt/receiveGroup/loan', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 到期收款记录
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '656ac5b9fbe242eabb2bdcdd37a382c8v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('656ac5b9fbe242eabb2bdcdd37a382c8v1', '13', '到期收款记录', '到期收款记录', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 4, 1, '/sub-finance/#/electronicDebt/receiveGroup/collect', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 指令管理
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '4c1d2c58435349e299f1c6c8548e6e55v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('4c1d2c58435349e299f1c6c8548e6e55v1', '16', '指令管理', '指令管理', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 3, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权指令管理
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '5600fa64f75d4cf39384d478907441bav1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('5600fa64f75d4cf39384d478907441bav1', '17', '应付债权指令管理', '应付债权指令管理', '4c1d2c58435349e299f1c6c8548e6e55v1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/instruction-management/payable', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 查询资源（应付债权指令）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a72e491cf36d428791082d756c340268';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a72e491cf36d428791082d756c340268', '查询资源', '查询资源', 'scf:payable:instruction:query', 1, '5600fa64f75d4cf39384d478907441bav1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应用资源（应付债权指令）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'b15c8367290e4742a89d63f50e71294c';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('b15c8367290e4742a89d63f50e71294c', '应用资源', '应用资源', 'scf:payable:instruction:apply', 1, '5600fa64f75d4cf39384d478907441bav1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权指令管理
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '8f5cf98f46184954b4f9c526d0bb6519v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('8f5cf98f46184954b4f9c526d0bb6519v1', '18', '应收债权指令管理', '应收债权指令管理', '4c1d2c58435349e299f1c6c8548e6e55v1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/instruction-management/receivable', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 查询资源（应收债权指令）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'c9402d58e7314176b38510f497263da7';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('c9402d58e7314176b38510f497263da7', '查询资源', '查询资源', 'scf:receivable:instruction:query', 1, '8f5cf98f46184954b4f9c526d0bb6519v1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应用资源（应收债权指令）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'd37f61894520481195e7ac241b80563e';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('d37f61894520481195e7ac241b80563e', '应用资源', '应用资源', 'scf:receivable:instruction:apply', 1, '8f5cf98f46184954b4f9c526d0bb6519v1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 查询统计
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = 'ee4803a99b3b478ea9e331fb600c7f1av1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('ee4803a99b3b478ea9e331fb600c7f1av1', '16', '查询统计', '查询统计', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 4, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权查询
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '5600fa64f75d4cf39384d478907441bbv1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('5600fa64f75d4cf39384d478907441bbv1', '17', '应付债权查询', '应付债权查询', 'ee4803a99b3b478ea9e331fb600c7f1av1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/query-statistics/payable-voucher', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/


DECLARE
v_count NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '81195e7ad37f618945204c241b80563e';
IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('81195e7ad37f618945204c241b80563e', '查看资源', '查看资源', 'scf:payable:query', 1, '5600fa64f75d4cf39384d478907441bbv1', NULL, NULL, NULL, NULL, '', '1', '1');
END IF;
COMMIT;
END;
/

-- 应收债权查询
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_menu WHERE id = '34d23e06d8fa4bcba42c5939b6f4ccf4v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
        VALUES ('34d23e06d8fa4bcba42c5939b6f4ccf4v1', '18', '应收债权查询', '应收债权查询', 'ee4803a99b3b478ea9e331fb600c7f1av1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/query-statistics/receivable-voucher', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

DECLARE
v_count NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'ad37f618945281195e704c241b80563e';
IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('ad37f618945281195e704c241b80563e', '查看资源', '查看资源', 'scf:receivable:query', 1, '34d23e06d8fa4bcba42c5939b6f4ccf4v1', NULL, NULL, NULL, NULL, '', '1', '1');
END IF;
COMMIT;
END;
/
-- 应付债权凭证开立资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '395734ec80e2497b932154ce9d3383e6v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('395734ec80e2497b932154ce9d3383e6v1', '应付债权凭证开立', '应付债权凭证开立', 'scf:flow:approve:scfPayableOpened', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权凭证兑付资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '2290a7673aba492e9e2777d5d60e7725v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('2290a7673aba492e9e2777d5d60e7725v1', '应付债权凭证兑付', '应付债权凭证兑付', 'scf:flow:approve:scfVoucherRedemption', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证开立审批资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'bdf5ea4f099e4458990b34195d52ad6dv1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('bdf5ea4f099e4458990b34195d52ad6dv1', '应收债权凭证开立审批资源', '应收债权凭证开立审批资源', 'scf:flow:approve:scfFlowReceivableVoucher', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证融资审批资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '470d8f37a2ec49b083937dbae4bcd3e2v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('470d8f37a2ec49b083937dbae4bcd3e2v1', '应收债权凭证融资审批资源', '应收债权凭证融资审批资源', 'scf:flow:approve:scfFlowLoan', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证转让审批资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '625e288c60b348c294c850d14b25267fv1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('625e288c60b348c294c850d14b25267fv1', '应收债权凭证转让审批资源', '应收债权凭证转让审批资源', 'scf:flow:approve:scfFlowTransfer', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证付款审批资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = '470d8f37a2ec49b083937dbae4bcd3e2v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('470d8f37a2ec49b083937dbae4bcd3e2v1', '应收债权凭证付款审批资源', '应收债权凭证付款审批资源', 'scf:flow:approve:scfFlowPayment', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证查询资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a1b2c3d4e5f64789a1b2c3d4e5f61001v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a1b2c3d4e5f64789a1b2c3d4e5f61001v1', '应收债权凭证查询', '应收债权凭证查询', 'scf:rec:voucher:query', 1, '5a5166c7a21645efaa2c3fcdf446bc71v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 转让记录查询资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a1b2c3d4e5f64789a1b2c3d4e5f61002v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a1b2c3d4e5f64789a1b2c3d4e5f61002v1', '转让记录查询', '转让记录查询', 'scf:rec:transfer:query', 1, '5cff354932f341ccbe9f1f6a26f78a56v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 融资记录查询资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a1b2c3d4e5f64789a1b2c3d4e5f61003v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a1b2c3d4e5f64789a1b2c3d4e5f61003v1', '融资记录查询', '融资记录查询', 'scf:rec:loan:query', 1, '5a5166c7a21645efaa2c3fcdf446bc72v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 到期收款记录查询资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a1b2c3d4e5f64789a1b2c3d4e5f61004v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a1b2c3d4e5f64789a1b2c3d4e5f61004v1', '到期收款记录查询', '到期收款记录查询', 'scf:rec:payment:query', 1, '656ac5b9fbe242eabb2bdcdd37a382c8v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证修改资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a1b2c3d4e5f64789a1b2c3d4e5f62001v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a1b2c3d4e5f64789a1b2c3d4e5f62001v1', '应收债权凭证修改', '应收债权凭证修改', 'scf:rec:voucher:update', 1, '5a5166c7a21645efaa2c3fcdf446bc71v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 转让记录修改资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a1b2c3d4e5f64789a1b2c3d4e5f62002v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a1b2c3d4e5f64789a1b2c3d4e5f62002v1', '转让记录修改', '转让记录修改', 'scf:rec:transfer:update', 1, '5cff354932f341ccbe9f1f6a26f78a56v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 融资记录修改资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a1b2c3d4e5f64789a1b2c3d4e5f62003v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a1b2c3d4e5f64789a1b2c3d4e5f62003v1', '融资记录修改', '融资记录修改', 'scf:rec:loan:update', 1, '5a5166c7a21645efaa2c3fcdf446bc72v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- 到期收款记录修改资源
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_resources WHERE id = 'a1b2c3d4e5f64789a1b2c3d4e5f62004v1';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
        VALUES ('a1b2c3d4e5f64789a1b2c3d4e5f62004v1', '到期收款记录修改', '到期收款记录修改', 'scf:rec:payment:update', 1, '656ac5b9fbe242eabb2bdcdd37a382c8v1', NULL, NULL, NULL, NULL, NULL, '1', '1');
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 13. 供应链流程分类（tenant_pre_act_extend_category）
-- ============================================

-- 供应链金融管理（顶级分类）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_act_extend_category WHERE id = '5c4a3ad1e02a475fb4e5c2b2cf19d8a4';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
        VALUES ('5c4a3ad1e02a475fb4e5c2b2cf19d8a4', '', '供应链金融管理', 'scfKey', '1', NULL, 10, NULL, NULL, 0, 1, '45ee4780ab564ff7a4117ae17bedc052v1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '供应链金融管理', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权凭证（流程分类）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_act_extend_category WHERE id = 'ae932b51bc5349e69fcd9c13f1fb5adc';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
        VALUES ('ae932b51bc5349e69fcd9c13f1fb5adc', '5c4a3ad1e02a475fb4e5c2b2cf19d8a4', '应付债权凭证', 'scfPayableKey', '1', NULL, 1, NULL, NULL, 0, 1, '4dac33e0226249eab31cb7313382174fv1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '电子债权凭证', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权凭证开立流程
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_act_extend_category WHERE id = '0a7759af278a41a9ae6718785ab698d4';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
        VALUES ('0a7759af278a41a9ae6718785ab698d4', 'ae932b51bc5349e69fcd9c13f1fb5adc', '应付债权凭证开立流程', 'scfVoucherOpened', '1', '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/payable-approval', 1, NULL, NULL, 0, 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, 'scf:flow:approve:scfPayableOpened', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应付债权凭证开立流程', '1');
    END IF;
    COMMIT;
END;
/

-- 应付债权凭证兑付流程
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_act_extend_category WHERE id = '88f534b57c0841dbadc508093cf83e2d';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
        VALUES ('88f534b57c0841dbadc508093cf83e2d', 'ae932b51bc5349e69fcd9c13f1fb5adc', '应付债权凭证兑付流程', 'scfVoucherRedemption', '1', '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/voucher-redemption-approval', 1, NULL, NULL, 0, 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, 'scf:flow:approve:scfVoucherRedemption', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应付债权凭证兑付流程', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证（流程分类）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_act_extend_category WHERE id = '8f42adb580f9435fa2fe83362e339e7e';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
        VALUES ('8f42adb580f9435fa2fe83362e339e7e', '5c4a3ad1e02a475fb4e5c2b2cf19d8a4', '应收债权凭证', 'scfReceivableKey', '1', NULL, 2, NULL, NULL, 0, 1, '4dac33e0226249eab31cb7313382174fv1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '应收债权凭证', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证开立流程
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_act_extend_category WHERE id = '8f9671c31de14531aa26a477475089d0';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
        VALUES ('8f9671c31de14531aa26a477475089d0', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证开立流程', 'scfFlowReceivableVoucher', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowReceivableVoucher', 1, NULL, NULL, 0, 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, 'scf:flow:approve:scfFlowReceivableVoucher', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证开立流程', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证融资流程
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_act_extend_category WHERE id = '6522dd2a73214baab5b35418ba96b23b';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
        VALUES ('6522dd2a73214baab5b35418ba96b23b', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证融资流程', 'scfFlowLoan', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowLoan', 2, NULL, NULL, 0, 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, 'scf:flow:approve:scfFlowLoan', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证融资流程', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证转让流程
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_act_extend_category WHERE id = '793a2b618a124f0ba34f2664420a1ac8';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
        VALUES ('793a2b618a124f0ba34f2664420a1ac8', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证转让流程', 'scfFlowTransfer', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowTransfer', 3, NULL, NULL, 0, 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, 'scf:flow:approve:scfFlowTransfer', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证转让流程', '1');
    END IF;
    COMMIT;
END;
/

-- 应收债权凭证到期收款流程
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM tenant_pre_act_extend_category WHERE id = 'c5537632864b4b16ba9706b7edd563e0';
    IF v_count < 1 THEN
        INSERT INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
        VALUES ('c5537632864b4b16ba9706b7edd563e0', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证到期收款', 'scfFlowPayment', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowPayment', 4, NULL, NULL, 0, 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, 'scf:flow:approve:scfFlowPayment', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证到期收款', '1');
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 14. 应付债权审批状态字典
-- ============================================
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict_group WHERE id = '76ae7188d8f64487a058991d505ea2f4';
    IF v_count < 1 THEN
        INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
        VALUES ('76ae7188d8f64487a058991d505ea2f4', 'scf.payable.approvalStatus', '应付债权审批状态');
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '76ae7188d8f64487a058991d505ea2f4' AND TYPE_CODE = '0';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('7150bf637c944fd18cd8d30309b8e4db', '0', '暂存', NULL, '76ae7188d8f64487a058991d505ea2f4', 1);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '76ae7188d8f64487a058991d505ea2f4' AND TYPE_CODE = '-10';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('e83f1da0b1e74ec5b016091d28b6df7b', '-10', '打回', NULL, '76ae7188d8f64487a058991d505ea2f4', 2);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '76ae7188d8f64487a058991d505ea2f4' AND TYPE_CODE = '-2';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('608b32d32115482ca8bbc04476a5609a', '-2', '已作废', NULL, '76ae7188d8f64487a058991d505ea2f4', 3);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '76ae7188d8f64487a058991d505ea2f4' AND TYPE_CODE = '11';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('0bc8525af3e1423a88c11b8c1396751a', '11', '审批中', NULL, '76ae7188d8f64487a058991d505ea2f4', 4);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '76ae7188d8f64487a058991d505ea2f4' AND TYPE_CODE = '95';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('97739f2be1c6456b9663880ae5f69f68', '95', '审批通过', NULL, '76ae7188d8f64487a058991d505ea2f4', 5);
    END IF;
    COMMIT;
END;
/

-- 存量库矫正：名称改通用、删除不再维护的字典值
-- （-11 兑付打回 / 12 兑付审批中 / 96 兑付审批通过 / -3 已删除）
BEGIN
    UPDATE sys_dict SET TYPE_NAME = '打回' WHERE ID = 'e83f1da0b1e74ec5b016091d28b6df7b';
    UPDATE sys_dict SET TYPE_NAME = '审批中' WHERE ID = '0bc8525af3e1423a88c11b8c1396751a';
    UPDATE sys_dict SET TYPE_NAME = '审批通过' WHERE ID = '97739f2be1c6456b9663880ae5f69f68';
    DELETE FROM sys_dict WHERE ID IN ('a1061f4010c947df975370fc8ea97150', 'fcdc72e25af04a4182f3dcc4b762dfd9', '17e3d04f28d34cdebdcccffdc28b227d', 'bc855e247ec64eeeb19f5cc5463be005');
    COMMIT;
END;
/

-- ============================================
-- 15. 应付债权业务状态字典
-- ============================================
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict_group WHERE id = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d';
    IF v_count < 1 THEN
        INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
        VALUES ('9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 'scf.payable.voucherStatus', '应付债权业务状态');
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d' AND TYPE_CODE = '0';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('b2a1c3d4e5f6a7b8c9d0e1f2a3b4c5d6', '0', '未开立', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 1);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d' AND TYPE_CODE = '1';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7', '1', '开立中', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 2);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d' AND TYPE_CODE = '2';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8', '2', '已开立', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 3);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d' AND TYPE_CODE = '3';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9', '3', '已拒签', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 4);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d' AND TYPE_CODE = '4';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0', '4', '已撤销', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 5);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d' AND TYPE_CODE = '5';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1', '5', '已兑付', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 6);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d' AND TYPE_CODE = '6';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2', '6', '兑付中', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 7);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d' AND TYPE_CODE = '7';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3', '7', '已作废', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 8);
    END IF;
    COMMIT;
END;
/

-- 业务状态字典只保留 0~7 共 8 条，8 待兑付不再维护（存量库清理）
BEGIN
    DELETE FROM sys_dict WHERE ID = 'c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4';
    COMMIT;
END;
/

-- ============================================
-- 16. 单据审批状态字典
-- ============================================
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict_group WHERE id = 'f2e89d7c3b6a41509ceb74321dc09651';
    IF v_count < 1 THEN
        INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
        VALUES ('f2e89d7c3b6a41509ceb74321dc09651', 'scf.rec.approvalStatus', '单据审批状态');
    END IF;
    COMMIT;
END;
/

-- 开立暂存
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc01';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc01', 'open-draft', '开立暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 10);
    END IF;
    COMMIT;
END;
/

-- 开立审批中
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc02';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc02', 'open-approval-ing', '开立审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 20);
    END IF;
    COMMIT;
END;
/

-- 开立审批通过
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc03';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc03', 'open-approval-pass', '开立审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 30);
    END IF;
    COMMIT;
END;
/

-- 开立打回
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc04';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc04', 'open-rejected', '开立打回', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 40);
    END IF;
    COMMIT;
END;
/

-- 开立已作废
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc05';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc05', 'open-cancelled', '开立已作废', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 50);
    END IF;
    COMMIT;
END;
/

-- 融资暂存
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc11';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc11', 'loan-draft', '融资暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 60);
    END IF;
    COMMIT;
END;
/

-- 融资审批中
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc12';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc12', 'loan-approval-ing', '融资审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 70);
    END IF;
    COMMIT;
END;
/

-- 融资审批通过
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc13';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc13', 'loan-approval-pass', '融资审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 80);
    END IF;
    COMMIT;
END;
/

-- 融资打回
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc14';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc14', 'loan-rejected', '融资打回', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 90);
    END IF;
    COMMIT;
END;
/

-- 融资已作废
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc15';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc15', 'loan-cancelled', '融资已作废', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 100);
    END IF;
    COMMIT;
END;
/

-- 转让暂存
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc21';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc21', 'transfer-draft', '转让暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 110);
    END IF;
    COMMIT;
END;
/

-- 转让审批中
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc22';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc22', 'transfer-approval-ing', '转让审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 120);
    END IF;
    COMMIT;
END;
/

-- 转让审批通过
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc23';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc23', 'transfer-approval-pass', '转让审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 130);
    END IF;
    COMMIT;
END;
/

-- 转让打回
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc24';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc24', 'transfer-rejected', '转让打回', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 140);
    END IF;
    COMMIT;
END;
/

-- 转让已作废
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc25';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc25', 'transfer-cancelled', '转让已作废', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 150);
    END IF;
    COMMIT;
END;
/

-- 提示付款暂存
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc31';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc31', 'payment-draft', '提示付款暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 160);
    END IF;
    COMMIT;
END;
/

-- 提示付款审批中
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc32';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc32', 'payment-approval-ing', '提示付款审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 170);
    END IF;
    COMMIT;
END;
/

-- 提示付款审批通过
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc33';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc33', 'payment-approval-pass', '提示付款审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 180);
    END IF;
    COMMIT;
END;
/

-- 提示付款打回
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc34';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc34', 'payment-rejected', '提示付款打回', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 190);
    END IF;
    COMMIT;
END;
/

-- 提示付款已作废
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '001a112233445566778899aa00bbcc35';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('001a112233445566778899aa00bbcc35', 'payment-cancelled', '提示付款已作废', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 200);
    END IF;
    COMMIT;
END;
/

-- ============================================
BEGIN
    DELETE FROM sys_dict WHERE TYPE_GROUP_ID = 'f2e89d7c3b6a41509ceb74321dc09651'
      AND TYPE_CODE IN ('open-draft','open-approval-ing','open-approval-pass','open-rejected','open-cancelled',
                        'loan-draft','loan-approval-ing','loan-approval-pass',
                        'transfer-draft','transfer-approval-ing','transfer-approval-pass',
                        'payment-draft','payment-approval-ing','payment-approval-pass');
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) VALUES ('001a112233445566778899aa00bbcc01', 'draft', '暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 10);
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) VALUES ('001a112233445566778899aa00bbcc02', 'approval-ing', '审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 20);
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) VALUES ('001a112233445566778899aa00bbcc03', 'rejected', '打回', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 30);
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) VALUES ('001a112233445566778899aa00bbcc04', 'approval-pass', '审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 40);
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) VALUES ('001a112233445566778899aa00bbcc05', 'cancelled', '已作废', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 50);
    COMMIT;
END;
/

-- 17. 应收债权凭证业务状态字典
-- ============================================
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict_group WHERE id = '8d72f90c456e41b396ac7821ef5034b6';
    IF v_count < 1 THEN
        INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
        VALUES ('8d72f90c456e41b396ac7821ef5034b6', 'scf.rec.businessStatus', '业务状态');
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc01';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc01', 'unsigned', '未签收', NULL, '8d72f90c456e41b396ac7821ef5034b6', 10);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc02';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc02', 'sign_ing', '签收中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 20);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc03';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc03', 'signed', '已签收', NULL, '8d72f90c456e41b396ac7821ef5034b6', 30);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc04';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc04', 'rejected', '已拒签', NULL, '8d72f90c456e41b396ac7821ef5034b6', 40);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc05';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc05', 'cancel', '已作废', NULL, '8d72f90c456e41b396ac7821ef5034b6', 50);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc06';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc06', 'split', '已拆分', NULL, '8d72f90c456e41b396ac7821ef5034b6', 60);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc07';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc07', 'un_transfer', '未转让', NULL, '8d72f90c456e41b396ac7821ef5034b6', 70);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc08';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc08', 'transfer_ing', '转让中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 80);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc09';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc09', 'transferred', '已转让', NULL, '8d72f90c456e41b396ac7821ef5034b6', 90);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc10';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc10', 'un_loan', '未融资', NULL, '8d72f90c456e41b396ac7821ef5034b6', 100);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc11';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc11', 'loan_ing', '融资中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 110);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc12';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc12', 'loan_success', '已融资', NULL, '8d72f90c456e41b396ac7821ef5034b6', 120);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc13';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc13', 'loan_failed', '融资失败', NULL, '8d72f90c456e41b396ac7821ef5034b6', 130);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc14';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc14', 'un_payment', '未收款', NULL, '8d72f90c456e41b396ac7821ef5034b6', 140);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc15';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc15', 'payment_ing', '收款中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 150);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc16';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc16', 'payment_success', '已收款', NULL, '8d72f90c456e41b396ac7821ef5034b6', 160);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc17';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc17', 'payment_failed', '收款失败', NULL, '8d72f90c456e41b396ac7821ef5034b6', 170);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '9101001122334455667788aa00ffcc18';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9101001122334455667788aa00ffcc18', 'data_supplement', '数据待补录', NULL, '8d72f90c456e41b396ac7821ef5034b6', 180);
    END IF;
    COMMIT;
END;
/

BEGIN
    DELETE FROM sys_dict WHERE TYPE_GROUP_ID = '8d72f90c456e41b396ac7821ef5034b6'
      AND TYPE_CODE IN ('sign_ing','transfer_failed','loan_failed','payment_failed');
    UPDATE sys_dict SET TYPE_NAME = '待转让' WHERE TYPE_GROUP_ID = '8d72f90c456e41b396ac7821ef5034b6' AND TYPE_CODE = 'un_transfer';
    UPDATE sys_dict SET TYPE_NAME = '待融资' WHERE TYPE_GROUP_ID = '8d72f90c456e41b396ac7821ef5034b6' AND TYPE_CODE = 'un_loan';
    UPDATE sys_dict SET TYPE_NAME = '待收款' WHERE TYPE_GROUP_ID = '8d72f90c456e41b396ac7821ef5034b6' AND TYPE_CODE = 'un_payment';
    COMMIT;
END;
/

-- ============================================
-- 18. 单据来源字典
-- ============================================
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict_group WHERE id = 'a7d9f2c0e4b681357ce290418dc73502';
    IF v_count < 1 THEN
        INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
        VALUES ('a7d9f2c0e4b681357ce290418dc73502', 'scf.voucher.source', '单据来源');
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '91c375802df44961a0eb5c287d0f641e';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('91c375802df44961a0eb5c287d0f641e', 'MANUAL', '手工新增', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 10);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '25fb601879dc43ae90c517482efd306b';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('25fb601879dc43ae90c517482efd306b', 'ERP', 'ERP', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 20);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '740e8d1c36af4259bcf02915d78634eb';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('740e8d1c36af4259bcf02915d78634eb', 'SUPPLY_PLATFORM', '供应链金融平台', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 30);
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 19. 债权凭证线上线下标识字典
-- ============================================
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict_group WHERE id = '5ea76ae7188d8f64487a058991d502f4';
    IF v_count < 1 THEN
        INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
        VALUES ('5ea76ae7188d8f64487a058991d502f4', 'scf.common.onlineSign', '债权凭证线上线下标识');
    END IF;
    COMMIT;
END;
/

-- 线下
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '64ea46bc26e9ef0ef742bea136ae17ed';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('64ea46bc26e9ef0ef742bea136ae17ed', '0', '线下', NULL, '5ea76ae7188d8f64487a058991d502f4', 1);
    END IF;
    COMMIT;
END;
/

-- 线上
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '5ea76ae7188d8f64487a058991d502f4' AND TYPE_CODE = '1';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('a1b2d2e3f4a5b6c7d8e9f0c3d4e5f6a7', '1', '线上', NULL, '5ea76ae7188d8f64487a058991d502f4', 2);
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 20. 凭证类别字典
-- ============================================
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict_group WHERE id = '5f82c9d17e3b4068a21f9c7430de1b65';
    IF v_count < 1 THEN
        INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
        VALUES ('5f82c9d17e3b4068a21f9c7430de1b65', 'scf.voucher.type', '凭证类别');
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '30ad67c29f1e4580b27d9c1e540f862b';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('30ad67c29f1e4580b27d9c1e540f862b', 'open', '开立凭证', NULL, '5f82c9d17e3b4068a21f9c7430de1b65', 10);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '72ef91c03d5b468a802f1c9d357e064b';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('72ef91c03d5b468a802f1c9d357e064b', 'transfer', '流转凭证', NULL, '5f82c9d17e3b4068a21f9c7430de1b65', 20);
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 21. 供应链指令状态字典
-- ============================================
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict_group WHERE id = '740b62c37c8f4dc89226389e426e6718';
    IF v_count < 1 THEN
        INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
        VALUES ('740b62c37c8f4dc89226389e426e6718', 'scf.exc.voucherStat', '供应链指令状态');
    END IF;
    COMMIT;
END;
/

-- 待发送
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '740b62c37c8f4dc89226389e426e6718' AND TYPE_CODE = '1';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('e47e945648494d3b81e6eb76e6d86098', '1', '待发送', NULL, '740b62c37c8f4dc89226389e426e6718', 2);
    END IF;
    COMMIT;
END;
/

-- 发送失败
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '740b62c37c8f4dc89226389e426e6718' AND TYPE_CODE = '2';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('9b2fb960082342d9a11e7ef79bb23a6c', '2', '发送失败', NULL, '740b62c37c8f4dc89226389e426e6718', 3);
    END IF;
    COMMIT;
END;
/

-- 发送成功
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '740b62c37c8f4dc89226389e426e6718' AND TYPE_CODE = '3';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('1a13be1680a24406bf5f8e77d8a9e66e', '3', '发送成功', NULL, '740b62c37c8f4dc89226389e426e6718', 4);
    END IF;
    COMMIT;
END;
/

-- 打回
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '740b62c37c8f4dc89226389e426e6718' AND TYPE_CODE = '4';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('4a26448a2efd4e048ab1334075eb4ab2', '4', '打回', NULL, '740b62c37c8f4dc89226389e426e6718', 5);
    END IF;
    COMMIT;
END;
/

-- 等待结果
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '740b62c37c8f4dc89226389e426e6718' AND TYPE_CODE = '5';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('a6bd2df07d014459a033f064ed83bae6', '5', '等待结果', NULL, '740b62c37c8f4dc89226389e426e6718', 6);
    END IF;
    COMMIT;
END;
/

-- 交易成功
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '740b62c37c8f4dc89226389e426e6718' AND TYPE_CODE = '6';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('719c3d2761db4c30852806c42a001d10', '6', '交易成功', NULL, '740b62c37c8f4dc89226389e426e6718', 7);
    END IF;
    COMMIT;
END;
/

-- 交易失败
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE TYPE_GROUP_ID = '740b62c37c8f4dc89226389e426e6718' AND TYPE_CODE = '7';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('f55f95d70da341d2a966936f1210c084', '7', '交易失败', NULL, '740b62c37c8f4dc89226389e426e6718', 8);
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 22. 付息方式字典
-- ============================================
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict_group WHERE id = '6e219f47c83045db9a72ce0158df3b9c';
    IF v_count < 1 THEN
        INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
        VALUES ('6e219f47c83045db9a72ce0158df3b9c', 'scf.interest.mode', '付息方式');
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '18c4072e956a43fb81dc790e5b326f71';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('18c4072e956a43fb81dc790e5b326f71', '1', '卖方付息', NULL, '6e219f47c83045db9a72ce0158df3b9c', 10);
    END IF;
    COMMIT;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM sys_dict WHERE id = '7d09c2eb563441fa98e715c03d862f4b';
    IF v_count < 1 THEN
        INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
        VALUES ('7d09c2eb563441fa98e715c03d862f4b', '2', '买方付息', NULL, '6e219f47c83045db9a72ce0158df3b9c', 20);
    END IF;
    COMMIT;
END;
/

-- ============================================
-- 付款单-供应链金融债权凭证关联表（中间表 nis_scf_info）
-- 承载付款单与应付/应收债权凭证的关联（替代在凭证表直接写 pay_no/pay_id）
-- 凭证类型：1=应付债权凭证（付款方式51），2=应收债权凭证（付款方式52）
-- ============================================
DECLARE
    vn_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO vn_count
    FROM user_tables
    WHERE table_name = 'NIS_SCF_INFO';
    IF vn_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE nis_scf_info (
            id VARCHAR2(32) NOT NULL,
            pay_id VARCHAR2(32) NULL,
            pay_no VARCHAR2(50) NULL,
            voucher_type VARCHAR2(2) NULL,
            voucher_id VARCHAR2(32) NULL,
            transfer_id VARCHAR2(32) NULL,
            platform_id VARCHAR2(32) NULL,
            product_id VARCHAR2(32) NULL,
            tenant_id VARCHAR2(32) NULL,
            CONSTRAINT PK_NIS_SCF_INFO PRIMARY KEY (id)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE nis_scf_info IS ''付款单-供应链金融债权凭证关联表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_scf_info.id IS ''id''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_scf_info.pay_id IS ''付款单ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_scf_info.pay_no IS ''付款单号''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_scf_info.voucher_type IS ''凭证类型(1=应付债权凭证，2=应收债权凭证)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_scf_info.voucher_id IS ''债权凭证ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_scf_info.transfer_id IS ''转让草稿ID(52-应收债权凭证转让暂存)''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_scf_info.platform_id IS ''供应链金融平台ID''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN nis_scf_info.product_id IS ''融资产品ID''';
    END IF;
    COMMIT;
END;
/

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
UPDATE scf_payable_voucher c
SET c.core_corp_name = (SELECT s.name FROM sys_corp s WHERE s.id = c.core_corp)
WHERE (c.core_corp_name IS NULL OR c.core_corp_name = '')
  AND c.core_corp IS NOT NULL;
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
