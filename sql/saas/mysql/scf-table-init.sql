CREATE TABLE IF NOT EXISTS `scf_payable_voucher` (
   `id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
   `voucher_no` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '单据编号',
   `voucher_code` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '应付债券凭证编号',
   `template_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '债权凭证模版id',
   `version_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '预留 可后续添加模板版本id',
   `platform_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台',
   `product_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品ID',
   `corp_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '企业ID',
   `open_date` datetime DEFAULT NULL COMMENT '开立日期',
   `expire_date` datetime DEFAULT NULL COMMENT '到期日期',
   `core_corp` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '核心企业ID',
   `core_corp_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '核心企业名称',
   `cur_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '币种ID',
   `voucher_money` decimal(18,6) DEFAULT NULL COMMENT '凭证金额',
  `supply_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '供应商ID',
  `supply_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '供应商名称',
  `receive_account` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收款账户',
  `receive_bank` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收款银行',
  `online_sign` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '线上签章',
  `quota_code` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '额度编码',
  `quota_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '额度名称',
  `quota_balance` decimal(18,6) DEFAULT NULL COMMENT '额度余额',
  `contract_code` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '合同编码',
  `contract_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '合同名称',
  `contract_money` decimal(18,6) DEFAULT NULL COMMENT '合同金额',
  `contract_date` datetime DEFAULT NULL COMMENT '合同日期',
  `contract_file` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '合同文件',
  `invoice_file` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '发票文件',
  `voucher_source` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '单据来源(手工新增：MANUAL，ERP，供应链金融平台:SUPPLY_PLATFORM)',
  `interest_mode` varchar(32) COLLATE utf8mb4_bin DEFAULT '1' COMMENT '融资付息方式(1:卖方付息，2:买方付息，默认卖方付息)',
  `promise_pay_date` datetime DEFAULT NULL COMMENT '承诺付款日',
  `supply_credit_code` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '供应商统一社会信用代码',
   `approval_status` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '审批状态',
   `voucher_status` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务状态',
  `pay_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '关联付款单ID(付款单审批通过后写入，选票时排除已关联凭证)',
  `pay_no` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '关联付款单号(付款单审批通过后写入，选票时排除已关联凭证)',
  `cancel_reason` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '作废原因',
  `cancel_date` datetime DEFAULT NULL COMMENT '作废时间',
  `create_by` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `tenant_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='应付债权凭证表';


CREATE TABLE IF NOT EXISTS scf_voucher_custom_field (
    id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
    voucher_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '凭证id',
    field_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字段id',
    field_code varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '自定义字段编码',
    field_name varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '自定义字段名称',
    field_type varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '自定义字段类型',
    field_value varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '自定义字段值',
    create_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
    create_date datetime DEFAULT NULL COMMENT '创建日期',
    update_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新人',
    update_date datetime DEFAULT NULL COMMENT '更新日期',
    tenant_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (id) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='应付债权凭证数据表自定义字段数据';


CREATE TABLE IF NOT EXISTS `scf_voucher_redemption` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `voucher_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '应付凭证ID',
  `cash_no` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '兑付单号',
  `platform_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台id',
  `product_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '融资产品',
  `corp_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开立单位',
  `open_date` datetime DEFAULT NULL COMMENT '开立日期',
  `expire_date` datetime DEFAULT NULL COMMENT '到期日',
  `core_corp` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '核心企业',
  `cur_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '币别',
  `voucher_money` decimal(18,6) DEFAULT NULL COMMENT '凭证金额',
  `supply_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '供应商',
  `supply_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '供应商',
  `pay_account` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '付款账号',
  `cash_money` decimal(18,6) DEFAULT NULL COMMENT '兑付金额',
  `pay_date` datetime DEFAULT NULL COMMENT '付款日期',
  `receive_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收款方名称',
  `receive_account` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收款方账号',
  `receive_bank` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收款开户行',
  `approval_status` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '审批状态',
  `tenant_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='凭证兑付记录表';

CREATE TABLE IF NOT EXISTS SCF_INSTRUCTION
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='指令表';

-- 供应链金融管理（顶级）
    INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '45ee4780ab564ff7a4117ae17bedc052v1', '1', '供应链金融管理', '供应链金融管理', NULL, NULL, 10, 1, NULL, NULL, 'icon-jggxgl', NULL, NULL, NULL, NULL, 1, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '45ee4780ab564ff7a4117ae17bedc052v1' LIMIT 1);

-- 基础配置
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', '2', '基础配置', '基础配置', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = 'dfcea55a6c734e25bb8c62c6ba6e4dedv1' LIMIT 1);

-- 参数配置
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '9bb2d2fe514c421aa862d01ba5db9137v1', '3', '参数配置', '参数配置', 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/baseConfig/parameterSetting', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '9bb2d2fe514c421aa862d01ba5db9137v1' LIMIT 1);

-- 平台直联设置
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '157d094324234e2ab7673a373e5883ffv1', '4', '平台直联设置', '平台直联设置', 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/baseConfig/platformDirectSetting', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '157d094324234e2ab7673a373e5883ffv1' LIMIT 1);

-- 查询资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '395734ec80e2787b932154ce9d3383e6v1', '查询资源', '查询资源', 'scf:ent:platform:query', 1, '157d094324234e2ab7673a373e5883ffv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '395734ec80e2787b932154ce9d3383e6v1' LIMIT 1);

--  应用资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '8v5734ec80e2497b932154ce9d3383e8v1', '应用资源', '应用资源', 'scf:ent:platform:apply', 1, '157d094324234e2ab7673a373e5883ffv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '8v5734ec80e2497b932154ce9d3383e8v1' LIMIT 1);


-- 资金方
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT 'ff2db81e59d9483eb525fc978b52bdc9v1', '5', '资金方', '资金方', 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', NULL, 3, 1, '/sub-finance/#/supplyChainFinance/baseConfig/funder', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = 'ff2db81e59d9483eb525fc978b52bdc9v1' LIMIT 1);

-- 查询资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'dc101004d4ee40c5b1696cdb0c1c4b41', '查询资源', '查询资源', 'scf:funder:info:query', 1, 'ff2db81e59d9483eb525fc978b52bdc9v1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'dc101004d4ee40c5b1696cdb0c1c4b41' LIMIT 1);

--  应用资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '90243552241343278f9e25ff13f1dcce', '应用资源', '应用资源', 'scf:funder:info:apply', 1, 'ff2db81e59d9483eb525fc978b52bdc9v1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '90243552241343278f9e25ff13f1dcce' LIMIT 1);


-- 电子债权凭证
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '4dac33e0226249eab31cb7313382174fv1', '6', '电子债权凭证', '电子债权凭证', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '4dac33e0226249eab31cb7313382174fv1' LIMIT 1);

-- 应付债权凭证管理
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT 'd2c496569f454cdea106158935f0d676v1', '7', '应付债权凭证管理', '应付债权凭证管理', '4dac33e0226249eab31cb7313382174fv1', NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = 'd2c496569f454cdea106158935f0d676v1' LIMIT 1);

-- 应付债权凭证
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '9b856832b17243b19d317d4dd2766eb9v1', '8', '应付债权凭证', '应付债权凭证', 'd2c496569f454cdea106158935f0d676v1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/payable-voucher', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '9b856832b17243b19d317d4dd2766eb9v1' LIMIT 1);

-- 应付债权凭证查询资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f60001v1', '应付债权凭证查询', '应付债权凭证查询', 'scf:payable:voucher:query', 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f60001v1' LIMIT 1);

-- 应付债权凭证修改资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '4e5f6a1b2c3d4789a1b2c3d4e5f60002v1', '应付债权凭证修改', '应付债权凭证修改', 'scf:payable:voucher:modify', 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '4e5f6a1b2c3d4789a1b2c3d4e5f60002v1' LIMIT 1);


-- 兑付记录
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '2a765b4789cc483ea87aa6f36a5d54f5v1', '9', '兑付记录', '兑付记录', 'd2c496569f454cdea106158935f0d676v1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/voucher-redemption', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '2a765b4789cc483ea87aa6f36a5d54f5v1' LIMIT 1);


-- 应付债权凭证查询资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '64789aa1b2c3d4e5f1b2c3d4e5f60001v1', '兑付记录查询', '兑付记录查询', 'scf:payable:redemption:query', 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '64789aa1b2c3d4e5f1b2c3d4e5f60001v1' LIMIT 1);

-- 应付债权凭证修改资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '789a1b2c3d4e4e5f6a1b2c3d45f60002v1', '兑付记录修改', '兑付记录修改', 'scf:payable:redemption:modify', 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '789a1b2c3d4e4e5f6a1b2c3d45f60002v1' LIMIT 1);

-- 应收债权凭证管理
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '37e5540ee2f24ca2b6d39ffb2cc37072v1', '8', '应收债权凭证管理', '应收债权凭证管理', '4dac33e0226249eab31cb7313382174fv1', NULL, 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '37e5540ee2f24ca2b6d39ffb2cc37072v1' LIMIT 1);

-- 应收债权凭证
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '5a5166c7a21645efaa2c3fcdf446bc71v1', '10', '应收债权凭证', '应收债权凭证', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 1, 1, '/sub-finance/#/electronicDebt/receiveGroup/list', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '5a5166c7a21645efaa2c3fcdf446bc71v1' LIMIT 1);

-- 转让记录
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '5cff354932f341ccbe9f1f6a26f78a56v1', '11', '转让记录', '转让记录', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 2, 1, '/sub-finance/#/electronicDebt/receiveGroup/transfer', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '5cff354932f341ccbe9f1f6a26f78a56v1' LIMIT 1);

-- 融资记录（原重复 ID，现分配新 ID）
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '5a5166c7a21645efaa2c3fcdf446bc72v1', '12', '融资记录', '融资记录', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 3, 1, '/sub-finance/#/electronicDebt/receiveGroup/loan', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '5a5166c7a21645efaa2c3fcdf446bc72v1' LIMIT 1);

-- 到期收款记录
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '656ac5b9fbe242eabb2bdcdd37a382c8v1', '13', '到期收款记录', '到期收款记录', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 4, 1, '/sub-finance/#/electronicDebt/receiveGroup/collect', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '656ac5b9fbe242eabb2bdcdd37a382c8v1' LIMIT 1);

-- 指令管理
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '4c1d2c58435349e299f1c6c8548e6e55v1', '16', '指令管理', '指令管理', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 3, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '4c1d2c58435349e299f1c6c8548e6e55v1' LIMIT 1);

-- 应付债权指令管理
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '5600fa64f75d4cf39384d478907441bav1', '17', '应付债权指令管理', '应付债权指令管理', '4c1d2c58435349e299f1c6c8548e6e55v1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/instruction-management/payable', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '5600fa64f75d4cf39384d478907441bav1' LIMIT 1);

-- 查询资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a72e491cf36d428791082d756c340268', '查询资源', '查询资源', 'scf:payable:instruction:query', 1, '5600fa64f75d4cf39384d478907441bav1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a72e491cf36d428791082d756c340268' LIMIT 1);

-- 应用资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'b15c8367290e4742a89d63f50e71294c', '应用资源', '应用资源', 'scf:payable:instruction:apply', 1, '5600fa64f75d4cf39384d478907441bav1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'b15c8367290e4742a89d63f50e71294c' LIMIT 1);


-- 应收债权指令管理
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '8f5cf98f46184954b4f9c526d0bb6519v1', '18', '应收债权指令管理', '应收债权指令管理', '4c1d2c58435349e299f1c6c8548e6e55v1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/instruction-management/receivable', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '8f5cf98f46184954b4f9c526d0bb6519v1' LIMIT 1);

-- 查询资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'c9402d58e7314176b38510f497263da7', '查询资源', '查询资源', 'scf:receivable:instruction:query', 1, '8f5cf98f46184954b4f9c526d0bb6519v1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'c9402d58e7314176b38510f497263da7' LIMIT 1);

-- 应用资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'd37f61894520481195e7ac241b80563e', '应用资源', '应用资源', 'scf:receivable:instruction:apply', 1, '8f5cf98f46184954b4f9c526d0bb6519v1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'd37f61894520481195e7ac241b80563e' LIMIT 1);

-- 查询统计
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT 'ee4803a99b3b478ea9e331fb600c7f1av1', '16', '查询统计', '查询统计', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 4, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = 'ee4803a99b3b478ea9e331fb600c7f1av1' LIMIT 1);

-- 应付债权查询（原重复 ID，现分配新 ID）
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '5600fa64f75d4cf39384d478907441bbv1', '17', '应付债权查询', '应付债权查询', 'ee4803a99b3b478ea9e331fb600c7f1av1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/query-statistics/payable-voucher', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '5600fa64f75d4cf39384d478907441bbv1' LIMIT 1);

-- 查看资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '81195e7ad37f618945204c241b80563e', '查看资源', '查看资源', 'scf:payable:query', 1, '5600fa64f75d4cf39384d478907441bbv1', NULL, NULL, NULL, NULL, '', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '81195e7ad37f618945204c241b80563e' LIMIT 1);

-- 应收债权查询
INSERT INTO `tenant_pre_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`, IS_INIT, UI_VERSION)
SELECT '34d23e06d8fa4bcba42c5939b6f4ccf4v1', '18', '应收债权查询', '应收债权查询', 'ee4803a99b3b478ea9e331fb600c7f1av1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/query-statistics/receivable-voucher', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_menu` WHERE ID = '34d23e06d8fa4bcba42c5939b6f4ccf4v1' LIMIT 1);

-- 查看资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'ad37f618945281195e704c241b80563e', '查看资源', '查看资源', 'scf:receivable:query', 1, '34d23e06d8fa4bcba42c5939b6f4ccf4v1', NULL, NULL, NULL, NULL, '', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'ad37f618945281195e704c241b80563e' LIMIT 1);

-- 应付债权凭证开立资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '395734ec80e2497b932154ce9d3383e6v1', '应付债权凭证开立', '应付债权凭证开立', 'scf:flow:approve:scfPayableOpened', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '395734ec80e2497b932154ce9d3383e6v1' LIMIT 1);

-- 应付债权凭证兑付资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '2290a7673aba492e9e2777d5d60e7725v1', '应付债权凭证兑付', '应付债权凭证兑付', 'scf:flow:approve:scfVoucherRedemption', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '2290a7673aba492e9e2777d5d60e7725v1' LIMIT 1);

-- 应收债权凭证开立资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'bdf5ea4f099e4458990b34195d52ad6dv1', '应收债权凭证开立审批资源', '应收债权凭证开立审批资源', 'scf:flow:approve:scfFlowReceivableVoucher', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'bdf5ea4f099e4458990b34195d52ad6dv1' LIMIT 1);

-- 应收债权凭证融资资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '470d8f37a2ec49b083937dbae4bcd3e2v1', '应收债权凭证融资审批资源', '应收债权凭证融资审批资源', 'scf:flow:approve:scfFlowLoan', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '470d8f37a2ec49b083937dbae4bcd3e2v1' LIMIT 1);

-- 应收债权凭证转让资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '625e288c60b348c294c850d14b25267fv1', '应收债权凭证转让审批资源', '应收债权凭证转让审批资源', 'scf:flow:approve:scfFlowTransfer', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '625e288c60b348c294c850d14b25267fv1' LIMIT 1);

-- 应收债权凭证融资资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT '470d8f37a2ec49b083937dbae4bcd3e2v1', '应收债权凭证融资审批资源', '应收债权凭证融资审批资源', 'scf:flow:approve:scfFlowPayment', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = '470d8f37a2ec49b083937dbae4bcd3e2v1' LIMIT 1);





-- 应收债权凭证-凭证资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f61001v1', '应收债权凭证查询', '应收债权凭证查询', 'scf:rec:voucher:query', 1, '5a5166c7a21645efaa2c3fcdf446bc71v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f61001v1' LIMIT 1);

-- 应收债权凭证-转让记录资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f61002v1', '转让记录查询', '转让记录查询', 'scf:rec:transfer:query', 1, '5cff354932f341ccbe9f1f6a26f78a56v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f61002v1' LIMIT 1);

-- 应收债权凭证-融资记录资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f61003v1', '融资记录查询', '融资记录查询', 'scf:rec:loan:query', 1, '5a5166c7a21645efaa2c3fcdf446bc72v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f61003v1' LIMIT 1);

-- 应收债权凭证-到期收款记录资源
INSERT INTO `tenant_pre_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f61004v1', '到期收款记录查询', '到期收款记录查询', 'scf:rec:payment:query', 1, '656ac5b9fbe242eabb2bdcdd37a382c8v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f61004v1' LIMIT 1);


-- 应收债权凭证-凭证修改
INSERT INTO `tenant_pre_resources`
(`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f62001v1', '应收债权凭证修改', '应收债权凭证修改', 'scf:rec:voucher:update', 1, '5a5166c7a21645efaa2c3fcdf446bc71v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f62001v1' LIMIT 1);

-- 应收债权凭证-转让记录修改
INSERT INTO `tenant_pre_resources`
(`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f62002v1', '转让记录修改', '转让记录修改', 'scf:rec:transfer:update', 1, '5cff354932f341ccbe9f1f6a26f78a56v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f62002v1' LIMIT 1);

-- 应收债权凭证-融资记录修改
INSERT INTO `tenant_pre_resources`
(`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f62003v1', '融资记录修改', '融资记录修改', 'scf:rec:loan:update', 1, '5a5166c7a21645efaa2c3fcdf446bc72v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f62003v1' LIMIT 1);

-- 应收债权凭证-到期收款记录修改
INSERT INTO `tenant_pre_resources`
(`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `URL`, `IS_INIT`, `ui_version`)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f62004v1', '到期收款记录修改', '到期收款记录修改', 'scf:rec:payment:update', 1, '656ac5b9fbe242eabb2bdcdd37a382c8v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_resources` WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f62004v1' LIMIT 1);







-- 供应链金融管理（顶级分类）
INSERT INTO `tenant_pre_act_extend_category` (`ID`, `PARENT_ID`, `NAME`, `CATE_KEY`, `TYPE`, `URL`, `SORT`, `MOBILE_URL`, `NOTIFICATION_ID`, `MUST_APPROVE`, `BATCH_REVOKE_ABLE`, `MENU_ID`, `MOBILE_AVE_ABLE`, `RESOURCE_CONTENT`, `CREATE_BY`, `UPDATE_BY`, `CREATE_DATE`, `UPDATE_DATE`, `SERVICE_ID`, `MENU_NAME`, `IS_INIT`)
SELECT '5c4a3ad1e02a475fb4e5c2b2cf19d8a4', '', '供应链金融管理', 'scfKey', '1', NULL, 10, NULL, NULL, 0, 1, '45ee4780ab564ff7a4117ae17bedc052v1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '供应链金融管理', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_act_extend_category` WHERE ID = '5c4a3ad1e02a475fb4e5c2b2cf19d8a4' LIMIT 1);

-- 应付债权凭证
INSERT INTO `tenant_pre_act_extend_category` (`ID`, `PARENT_ID`, `NAME`, `CATE_KEY`, `TYPE`, `URL`, `SORT`, `MOBILE_URL`, `NOTIFICATION_ID`, `MUST_APPROVE`, `BATCH_REVOKE_ABLE`, `MENU_ID`, `MOBILE_AVE_ABLE`, `RESOURCE_CONTENT`, `CREATE_BY`, `UPDATE_BY`, `CREATE_DATE`, `UPDATE_DATE`, `SERVICE_ID`, `MENU_NAME`, `IS_INIT`)
SELECT 'ae932b51bc5349e69fcd9c13f1fb5adc', '5c4a3ad1e02a475fb4e5c2b2cf19d8a4', '应付债权凭证', 'scfPayableKey', '1', NULL, 1, NULL, NULL, 0, 1, '4dac33e0226249eab31cb7313382174fv1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '电子债权凭证', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_act_extend_category` WHERE ID = 'ae932b51bc5349e69fcd9c13f1fb5adc' LIMIT 1);


-- 应付债权凭证开立流程分类
INSERT INTO `tenant_pre_act_extend_category` (`ID`, `PARENT_ID`, `NAME`, `CATE_KEY`, `TYPE`, `URL`, `SORT`, `MOBILE_URL`, `NOTIFICATION_ID`, `MUST_APPROVE`, `BATCH_REVOKE_ABLE`, `MENU_ID`, `MOBILE_AVE_ABLE`, `RESOURCE_CONTENT`, `CREATE_BY`, `UPDATE_BY`, `CREATE_DATE`, `UPDATE_DATE`, `SERVICE_ID`, `MENU_NAME`, `IS_INIT`)
SELECT '0a7759af278a41a9ae6718785ab698d4', 'ae932b51bc5349e69fcd9c13f1fb5adc', '应付债权凭证开立流程', 'scfVoucherOpened', '1', '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/payable-approval', 1, NULL, NULL, 0, 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, 'scf:flow:approve:scfPayableOpened', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应付债权凭证开立流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_act_extend_category` WHERE ID = '0a7759af278a41a9ae6718785ab698d4' LIMIT 1);

-- 应付债权凭证兑付流程分类
INSERT INTO `tenant_pre_act_extend_category` (`ID`, `PARENT_ID`, `NAME`, `CATE_KEY`, `TYPE`, `URL`, `SORT`, `MOBILE_URL`, `NOTIFICATION_ID`, `MUST_APPROVE`, `BATCH_REVOKE_ABLE`, `MENU_ID`, `MOBILE_AVE_ABLE`, `RESOURCE_CONTENT`, `CREATE_BY`, `UPDATE_BY`, `CREATE_DATE`, `UPDATE_DATE`, `SERVICE_ID`, `MENU_NAME`, `IS_INIT`)
SELECT '88f534b57c0841dbadc508093cf83e2d', 'ae932b51bc5349e69fcd9c13f1fb5adc', '应付债权凭证兑付流程', 'scfVoucherRedemption', '1', '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/voucher-redemption-approval', 1, NULL, NULL, 0, 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, 'scf:flow:approve:scfVoucherRedemption', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应付债权凭证兑付流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_act_extend_category` WHERE ID = '88f534b57c0841dbadc508093cf83e2d' LIMIT 1);
-- 付息方式
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '6e219f47c83045db9a72ce0158df3b9c', 'scf.interest.mode', '付息方式'
    WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = '6e219f47c83045db9a72ce0158df3b9c' LIMIT 1
);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '18c4072e956a43fb81dc790e5b326f71', '1', '卖方付息', NULL, '6e219f47c83045db9a72ce0158df3b9c', 10
    where not exists (select 1 from sys_dict where ID = '18c4072e956a43fb81dc790e5b326f71' limit 1);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '7d09c2eb563441fa98e715c03d862f4b', '2', '买方付息', NULL, '6e219f47c83045db9a72ce0158df3b9c', 20
    where not exists (select 1 from sys_dict where ID = '7d09c2eb563441fa98e715c03d862f4b' limit 1);


-- 应收债权凭证
INSERT INTO `tenant_pre_act_extend_category` (`ID`, `PARENT_ID`, `NAME`, `CATE_KEY`, `TYPE`, `URL`, `SORT`, `MOBILE_URL`, `NOTIFICATION_ID`, `MUST_APPROVE`, `BATCH_REVOKE_ABLE`, `MENU_ID`, `MOBILE_AVE_ABLE`, `RESOURCE_CONTENT`, `CREATE_BY`, `UPDATE_BY`, `CREATE_DATE`, `UPDATE_DATE`, `SERVICE_ID`, `MENU_NAME`, `IS_INIT`)
SELECT '8f42adb580f9435fa2fe83362e339e7e', '5c4a3ad1e02a475fb4e5c2b2cf19d8a4', '应收债权凭证', 'scfReceivableKey', '1', NULL, 2, NULL, NULL, 0, 1, '4dac33e0226249eab31cb7313382174fv1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '应收债权凭证', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_act_extend_category` WHERE ID = '8f42adb580f9435fa2fe83362e339e7e' LIMIT 1);


-- 应收债权凭证开立流程
INSERT INTO `tenant_pre_act_extend_category` (`ID`, `PARENT_ID`, `NAME`, `CATE_KEY`, `TYPE`, `URL`, `SORT`, `MOBILE_URL`, `NOTIFICATION_ID`, `MUST_APPROVE`, `BATCH_REVOKE_ABLE`, `MENU_ID`, `MOBILE_AVE_ABLE`, `RESOURCE_CONTENT`, `CREATE_BY`, `UPDATE_BY`, `CREATE_DATE`, `UPDATE_DATE`, `SERVICE_ID`, `MENU_NAME`, `IS_INIT`)
SELECT '8f9671c31de14531aa26a477475089d0', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证开立流程', 'scfFlowReceivableVoucher', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowReceivableVoucher', 1, NULL, NULL, 0, 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, 'scf:flow:approve:scfFlowReceivableVoucher', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证开立流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_act_extend_category` WHERE ID = '8f9671c31de14531aa26a477475089d0' LIMIT 1);

-- 应收债权凭证融资流程
INSERT INTO `tenant_pre_act_extend_category` (`ID`, `PARENT_ID`, `NAME`, `CATE_KEY`, `TYPE`, `URL`, `SORT`, `MOBILE_URL`, `NOTIFICATION_ID`, `MUST_APPROVE`, `BATCH_REVOKE_ABLE`, `MENU_ID`, `MOBILE_AVE_ABLE`, `RESOURCE_CONTENT`, `CREATE_BY`, `UPDATE_BY`, `CREATE_DATE`, `UPDATE_DATE`, `SERVICE_ID`, `MENU_NAME`, `IS_INIT`)
SELECT '6522dd2a73214baab5b35418ba96b23b', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证融资流程', 'scfFlowLoan', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowLoan', 2, NULL, NULL, 0, 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, 'scf:flow:approve:scfFlowLoan', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证融资流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_act_extend_category` WHERE ID = '6522dd2a73214baab5b35418ba96b23b' LIMIT 1);

-- 应收债权凭证转让流程
INSERT INTO `tenant_pre_act_extend_category` (`ID`, `PARENT_ID`, `NAME`, `CATE_KEY`, `TYPE`, `URL`, `SORT`, `MOBILE_URL`, `NOTIFICATION_ID`, `MUST_APPROVE`, `BATCH_REVOKE_ABLE`, `MENU_ID`, `MOBILE_AVE_ABLE`, `RESOURCE_CONTENT`, `CREATE_BY`, `UPDATE_BY`, `CREATE_DATE`, `UPDATE_DATE`, `SERVICE_ID`, `MENU_NAME`, `IS_INIT`)
SELECT '793a2b618a124f0ba34f2664420a1ac8', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证转让流程', 'scfFlowTransfer', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowTransfer', 3, NULL, NULL, 0, 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, 'scf:flow:approve:scfFlowTransfer', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证转让流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_act_extend_category` WHERE ID = '793a2b618a124f0ba34f2664420a1ac8' LIMIT 1);

-- 应收债权凭证到期收款流程
INSERT INTO `tenant_pre_act_extend_category` (`ID`, `PARENT_ID`, `NAME`, `CATE_KEY`, `TYPE`, `URL`, `SORT`, `MOBILE_URL`, `NOTIFICATION_ID`, `MUST_APPROVE`, `BATCH_REVOKE_ABLE`, `MENU_ID`, `MOBILE_AVE_ABLE`, `RESOURCE_CONTENT`, `CREATE_BY`, `UPDATE_BY`, `CREATE_DATE`, `UPDATE_DATE`, `SERVICE_ID`, `MENU_NAME`, `IS_INIT`)
SELECT 'c5537632864b4b16ba9706b7edd563e0', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证到期收款', 'scfFlowPayment', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowPayment', 4, NULL, NULL, 0, 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, 'scf:flow:approve:scfFlowPayment', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证到期收款', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `tenant_pre_act_extend_category` WHERE ID = 'c5537632864b4b16ba9706b7edd563e0' LIMIT 1);


CREATE TABLE IF NOT EXISTS scf_ent_bank_open
(
    id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
    type varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '接口类型',
    api_name_cn varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '接口名称（中文）',
    api_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '接口名称',
    has_direct varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '是否具备直连接口(0-否 1-是)',
    direct varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '直连接口',
    ent_platform_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台ID',
    tenant_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户ID',
    create_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
    create_date datetime DEFAULT NULL COMMENT '创建时间',
    update_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
    update_date datetime DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='平台接口开通配置表';
##GOGO

CREATE TABLE IF NOT EXISTS scf_funder_info
(
    id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
    code varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资金方编码',
    type varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '机构类型 01-保理公司 02-银行 03-其他',
    bank_no varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '银行行号',
    name varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资金方名称',
    sign varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资金方标识',
    valid varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '1' COMMENT '有效标识 1-有效 0-无效',
    rmk varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
    tenant_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户id',
    create_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
    create_date datetime DEFAULT NULL COMMENT '创建日期',
    update_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新人',
    update_date datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (id) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='资金方信息表';

CREATE TABLE IF NOT EXISTS `scf_loan_info` (
     `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
     `loan_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资单号(RZ+年月日+3位流水号)',
     `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户id',
     `loan_company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资企业id',
     `loan_company_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资企业名称',
     `voucher_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '债权凭证编号',
     `core_company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '核心企业',
     `core_company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '核心企业id',
     `funder_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资金方id',
     `funder_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资金方名称',
     `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资产品id',
     `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资产品名称',
     `platform_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台ID',
     `platform_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台名称',
     `cur_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '币别ID',
     `cur_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '币别名称',
     `loan_apply_date` date DEFAULT NULL COMMENT '融资申请日',
     `loan_amt` decimal(15,2) DEFAULT '0.00' COMMENT '融资金额',
     `interest_mode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '付息方式(1:卖方付息，2:买方付息，默认卖方付息)',
     `loan_lend_date` date DEFAULT NULL COMMENT '放款日(默认系统当前日期)',
     `loan_actual_date` date DEFAULT NULL COMMENT '实际放款日',
     `loan_due_date` date DEFAULT NULL COMMENT '到期日',
     `loan_deadline` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '期限(到期日-放款日) 天',
     `loan_rate` decimal(15,2) DEFAULT '0.00' COMMENT '融资利率',
     `loan_interest_amt` decimal(15,2) DEFAULT '0.00' COMMENT '利息金额(融资金额*融资利率)',
     `loan_hand_fee_rate` decimal(15,2) DEFAULT '0.00' COMMENT '手续费率',
     `loan_hand_fee_amt` decimal(15,2) DEFAULT '0.00' COMMENT '手续费(融资金额*手续费率)',
     `loan_service_fee_rate` decimal(15,2) DEFAULT '0.00' COMMENT '服务费率',
     `loan_service_fee_amt` decimal(15,2) DEFAULT '0.00' COMMENT '服务费(融资金额*服务费率)',
     `loan_cost_amt` decimal(15,2) DEFAULT '0.00' COMMENT '融资成本(利息金额+手续费+服务费)',
     `loan_actual_amt` decimal(15,2) DEFAULT '0.00' COMMENT '融资到账金额(融资金额-融资成本)',
     `loan_payment_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款账号',
     `loan_payment_account_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款开户名',
     `loan_payment_account_bank` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款开户行',
     `receivable_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应收ID',
     `loan_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'draft' COMMENT '融资审批状态(DRAFT:暂存; APPROVAL_ING:审批中; REJECTED:打回; APPROVAL_PASS:审批通过)',
     `online_sign` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '线上线下标识(0:线下；1:线上)',
     `enable` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '是否有效(0：无效；1：有效)',
     `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
     `create_date` datetime DEFAULT NULL COMMENT '创建时间',
     `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
     `update_date` datetime DEFAULT NULL COMMENT '修改时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='融资业务主信息表';

CREATE TABLE IF NOT EXISTS `scf_receivable_voucher_info` (
   `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
   `voucher_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单据编号(系统自动创建AR+年月日+3位流水号)',
   `voucher_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单据来源(手工新增：MANUAL，ERP，供应链金融平台:SUPPLY_PLATFORM)',
   `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户id',
   `voucher_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '债权凭证编号',
   `voucher_amt` decimal(15,2) DEFAULT '0.00' COMMENT '凭证金额',
   `platform_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台ID',
   `platform_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台名称',
   `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资产品id',
   `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资产品名称',
   `corp_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '持有单位',
   `corp_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '持有单位名称',
   `sign_date` date DEFAULT NULL COMMENT '签收日期',
   `sign_operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签收操作(签收,拒签,作废)',
   `voucher_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '凭证类别(开立凭证,流转凭证)',
   `expire_date` date DEFAULT NULL COMMENT '凭证到期日',
   `voucher_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '凭证业务状态(未签收,已签收,已拒签,已拆分)',
   `approval_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单据状态(暂存,审批中,打回,审批通过,已作废)',
   `core_company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '核心企业',
   `core_company_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '核心企业名称',
   `cur_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '币种ID',
   `cur_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '币别名称',
   `issue_transfer_company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开立/转让 企业',
   `issue_transfer_company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开立/转让 企业id',
   `source_voucher_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '根凭证编号(开立成功时生成的债权凭证编号)',
   `forehand_voucher_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上级凭证编号',
   `online_sign` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '线上线下标识',
   `allow_split` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '是否允许拆分(0:否, 1:是)',
   `contract_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '合同编码',
   `contract_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '合同名称',
   `contract_amt` decimal(15,2) DEFAULT '0.00' COMMENT '合同金额',
   `contract_date` date DEFAULT NULL COMMENT '合同日期',
   `contract_file_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '合同文件id',
   `invoice_file_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票文件id',
   `interest_mode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '融资付息方式(1:卖方付息，2:买方付息)',
   `promise_pay_date` date DEFAULT NULL COMMENT '承诺付款日',
   `transfer_company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '流转单位',
   `transfer_company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '流转单位ID',
   `enable` tinyint(1) DEFAULT '1' COMMENT '是否有效',
   `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
   `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
   `update_date` datetime DEFAULT NULL COMMENT '修改时间',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='应收债权凭证表';

CREATE TABLE IF NOT EXISTS `scf_receivable_transfer_info` (
    `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
    `transfer_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '凭证转让记录编号(系统自动创建ZR+年月日+3位流水号)',
    `transfer_batch_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '转让批次号',
    `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户id',
    `voucher_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '债权凭证编号',
    `transfer_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '凭证转让状态',
    `transfer_flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '转让标识(1:一对多-凭证拆分转让多家企业;2:多对一-多张凭证转让一家企业)',
    `platform_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台',
    `platform_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台名称',
    `product_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资产品',
    `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资产品id',
    `issue_transfer_company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开立/转让单位',
    `issue_transfer_company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开立/转让 企业id',
    `cur_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'CNY' COMMENT '币别',
    `cur_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '币别名称',
    `receive_company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收单位id',
    `receive_company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收单位名称',
    `receiver_credit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收方统一社会信用代码',
    `transfer_date` date DEFAULT NULL COMMENT '转让日期',
    `transfer_amt` decimal(15,2) DEFAULT '0.00' COMMENT '转让金额',
    `purpose` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用途',
    `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
    `pay_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联付款单ID(付款单审批通过后写入)',
    `pay_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联付款单号(付款单审批通过后写入)',
    `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
    `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
    `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='债权凭证转让信息表';

CREATE TABLE IF NOT EXISTS `scf_receivable_payment_info` (
   `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
   `voucher_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '债权凭证编号',
   `payment_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收款单号',
   `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户id',
   `payment_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款状态',
   `platform_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台',
   `platform_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '平台名称',
   `product_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资产品',
   `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资产品id',
   `receipt_company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款单位',
   `receipt_company_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款单位id',
   `sign_date` date DEFAULT NULL COMMENT '签收日期',
   `expire_date` date DEFAULT NULL COMMENT '到期日',
   `issue_transfer_company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开立/转让单位',
   `issue_transfer_company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开立/转让 企业id',
   `cur_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'CNY' COMMENT '币别',
   `cur_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '币别名称',
   `voucher_amt` decimal(15,2) DEFAULT '0.00' COMMENT '凭证金额',
   `core_company_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '核心企业id',
   `core_company_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '核心企业',
   `rec_account_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款账号',
   `rec_account_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款户名',
   `rec_bank` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款开户行',
   `rec_amt` decimal(15,2) DEFAULT '0.00' COMMENT '收款金额',
   `receipt_date` date DEFAULT NULL COMMENT '收款日期',
   `enable` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '是否有效(0：无效；1：有效)',
   `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
   `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
   `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='债权凭证收款明细表';

CREATE TABLE IF NOT EXISTS `scf_invoice_info` (
    `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'id',
    `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户id',
    `invoice_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票类型',
    `invoice_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票号码',
    `invoice_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票代码',
    `bill_date` datetime DEFAULT NULL COMMENT '开票日期',
    `cur_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '币种名称',
    `cur_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '币种',
    `total_amt` decimal(15,2) DEFAULT '0.00' COMMENT '价税合计',
    `tax_rate` decimal(15,2) DEFAULT '0.00' COMMENT '税率',
    `tax_amt` decimal(15,2) DEFAULT '0.00' COMMENT '税额',
    `bill_amt` decimal(15,2) DEFAULT '0.00' COMMENT '金额',
    `seller_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '销方名称',
    `buyer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '购方名称',
    `pay_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应付凭证id',
    `receivable_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应收凭证id',
    `sign` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应付应收标识(0为应付，1为应收)',
    `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
    `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
    `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='应付/应收债券发票信息';

-- ============================================
-- 付款单-供应链金融债权凭证关联表（中间表 nis_scf_info）
-- 承载付款单与应付/应收债权凭证的关联（替代在凭证表直接写 pay_no/pay_id）
-- 凭证类型：1=应付债权凭证（付款方式51），2=应收债权凭证（付款方式52）
-- ============================================
CREATE TABLE IF NOT EXISTS `nis_scf_info` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `pay_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '付款单ID',
  `pay_no` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '付款单号',
  `voucher_type` varchar(2) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '凭证类型(1=应付债权凭证，2=应收债权凭证)',
  `voucher_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '债权凭证ID',
  `transfer_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '转让草稿ID(52-应收债权凭证转让暂存)',
  `platform_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '供应链金融平台ID(51-未开立生成暂存凭证用)',
  `product_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '融资产品ID(51-未开立生成暂存凭证用)',
    `tenant_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_nis_scf_info_pay` (`pay_id`),
  KEY `idx_nis_scf_info_voucher` (`voucher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='付款单-供应链金融债权凭证关联表';


-- ============================================
-- 付款单表 nis_billhead 新增供应链金融标志（幂等：字段不存在才添加）
-- SCF_IS_OPENED：是否已有开立的应付债权凭证（1=是 0=否）
-- SCF_TRANSFER_FLAG：是否批量转让应收债权凭证（1=是 0=否）
-- ============================================
SET @nis_scf_opened_exists := (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'nis_billhead' AND COLUMN_NAME = 'SCF_IS_OPENED');
SET @nis_scf_ddl := IF(@nis_scf_opened_exists = 0,
    'ALTER TABLE `nis_billhead` ADD COLUMN `SCF_IS_OPENED` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''供应链金融-是否已有开立的应付债权凭证(1=是 0=否)''',
    'SELECT 1');
PREPARE nis_scf_stmt FROM @nis_scf_ddl;
EXECUTE nis_scf_stmt;
DEALLOCATE PREPARE nis_scf_stmt;

SET @nis_scf_transfer_exists := (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'nis_billhead' AND COLUMN_NAME = 'SCF_TRANSFER_FLAG');
SET @nis_scf_ddl := IF(@nis_scf_transfer_exists = 0,
    'ALTER TABLE `nis_billhead` ADD COLUMN `SCF_TRANSFER_FLAG` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''供应链金融-是否批量转让应收债权凭证(1=是 0=否)''',
    'SELECT 1');
PREPARE nis_scf_stmt FROM @nis_scf_ddl;
EXECUTE nis_scf_stmt;
DEALLOCATE PREPARE nis_scf_stmt;

-- SCF_PAYABLE_OPEN_AMT：应付债权凭证开立金额（51：未开立录入/已开立取所选凭证合计）
SET @nis_scf_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'nis_billhead' AND COLUMN_NAME = 'SCF_PAYABLE_OPEN_AMT') = 0,
    'ALTER TABLE `nis_billhead` ADD COLUMN `SCF_PAYABLE_OPEN_AMT` decimal(15,2) DEFAULT NULL COMMENT ''供应链金融-应付债权凭证开立金额(51:未开立录入/已开立取所选凭证合计)''',
    'SELECT 1');
PREPARE nis_scf_stmt FROM @nis_scf_ddl;
EXECUTE nis_scf_stmt;
DEALLOCATE PREPARE nis_scf_stmt;

-- SCF_TRANSFER_AMT：应收转让凭证金额（52：应收债权凭证转让记录金额，预留）
SET @nis_scf_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'nis_billhead' AND COLUMN_NAME = 'SCF_TRANSFER_AMT') = 0,
    'ALTER TABLE `nis_billhead` ADD COLUMN `SCF_TRANSFER_AMT` decimal(15,2) DEFAULT NULL COMMENT ''供应链金融-应收转让凭证金额(52:应收债权凭证转让记录金额)''',
    'SELECT 1');
PREPARE nis_scf_stmt FROM @nis_scf_ddl;
EXECUTE nis_scf_stmt;
DEALLOCATE PREPARE nis_scf_stmt;

-- PAY_ID：应付债权凭证关联付款单ID
SET @scf_pv_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_payable_voucher' AND COLUMN_NAME = 'PAY_ID') = 0,
    'ALTER TABLE `scf_payable_voucher` ADD COLUMN `pay_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''关联付款单ID(付款单审批通过后写入，选票时排除已关联凭证)''',
    'SELECT 1');
PREPARE scf_pv_stmt FROM @scf_pv_ddl;
EXECUTE scf_pv_stmt;
DEALLOCATE PREPARE scf_pv_stmt;

-- PAY_NO：应付债权凭证关联付款单号
SET @scf_pv_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_payable_voucher' AND COLUMN_NAME = 'PAY_NO') = 0,
    'ALTER TABLE `scf_payable_voucher` ADD COLUMN `pay_no` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''关联付款单号(付款单审批通过后写入，选票时排除已关联凭证)''',
    'SELECT 1');
PREPARE scf_pv_stmt FROM @scf_pv_ddl;
EXECUTE scf_pv_stmt;
DEALLOCATE PREPARE scf_pv_stmt;

-- CANCEL_REASON：应付债权凭证作废原因
SET @scf_pv_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_payable_voucher' AND COLUMN_NAME = 'CANCEL_REASON') = 0,
    'ALTER TABLE `scf_payable_voucher` ADD COLUMN `cancel_reason` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''作废原因''',
    'SELECT 1');
PREPARE scf_pv_stmt FROM @scf_pv_ddl;
EXECUTE scf_pv_stmt;
DEALLOCATE PREPARE scf_pv_stmt;

-- CANCEL_DATE：应付债权凭证作废时间
SET @scf_pv_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_payable_voucher' AND COLUMN_NAME = 'CANCEL_DATE') = 0,
    'ALTER TABLE `scf_payable_voucher` ADD COLUMN `cancel_date` datetime DEFAULT NULL COMMENT ''作废时间''',
    'SELECT 1');
PREPARE scf_pv_stmt FROM @scf_pv_ddl;
EXECUTE scf_pv_stmt;
DEALLOCATE PREPARE scf_pv_stmt;

-- PAY_ID：应收债权凭证转让 - 关联付款单ID
SET @scf_tr_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_receivable_transfer_info' AND COLUMN_NAME = 'PAY_ID') = 0,
    'ALTER TABLE `scf_receivable_transfer_info` ADD COLUMN `pay_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT ''关联付款单ID(付款单审批通过后写入，选票时排除已关联转让)''',
    'SELECT 1');
PREPARE scf_tr_stmt FROM @scf_tr_ddl;
EXECUTE scf_tr_stmt;
DEALLOCATE PREPARE scf_tr_stmt;

-- PAY_NO：应收债权凭证转让 - 关联付款单号
SET @scf_tr_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_receivable_transfer_info' AND COLUMN_NAME = 'PAY_NO') = 0,
    'ALTER TABLE `scf_receivable_transfer_info` ADD COLUMN `pay_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT ''关联付款单号(付款单审批通过后写入，选票时排除已关联转让)''',
    'SELECT 1');
PREPARE scf_tr_stmt FROM @scf_tr_ddl;
EXECUTE scf_tr_stmt;
DEALLOCATE PREPARE scf_tr_stmt;

-- TRANSFER_FLAG：应收债权凭证转让标识
SET @scf_tr_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_receivable_transfer_info' AND COLUMN_NAME = 'TRANSFER_FLAG') = 0,
    'ALTER TABLE `scf_receivable_transfer_info` ADD COLUMN `transfer_flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT ''转让标识(1:一对多-凭证拆分转让多家企业;2:多对一-多张凭证转让一家企业)''',
    'SELECT 1');
PREPARE scf_tr_stmt FROM @scf_tr_ddl;
EXECUTE scf_tr_stmt;
DEALLOCATE PREPARE scf_tr_stmt;

-- CORE_CORP_NAME：应付债权凭证核心企业名称
SET @scf_cc_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_payable_voucher' AND COLUMN_NAME = 'CORE_CORP_NAME') = 0,
    'ALTER TABLE `scf_payable_voucher` ADD COLUMN `core_corp_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''核心企业名称''',
    'SELECT 1');
PREPARE scf_cc_stmt FROM @scf_cc_ddl;
EXECUTE scf_cc_stmt;
DEALLOCATE PREPARE scf_cc_stmt;

-- 应收债权凭证作废时间、作废原因（幂等新增）
SET @scf_cancel_date_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_receivable_voucher_info' AND COLUMN_NAME = 'cancel_date') = 0,
    'ALTER TABLE `scf_receivable_voucher_info` ADD COLUMN `cancel_date` datetime DEFAULT NULL COMMENT ''作废时间''',
    'SELECT 1');
PREPARE scf_cancel_date_stmt FROM @scf_cancel_date_ddl;
EXECUTE scf_cancel_date_stmt;
DEALLOCATE PREPARE scf_cancel_date_stmt;

SET @scf_cancel_reason_ddl := IF((SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scf_receivable_voucher_info' AND COLUMN_NAME = 'cancel_reason') = 0,
    'ALTER TABLE `scf_receivable_voucher_info` ADD COLUMN `cancel_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT ''作废原因''',
    'SELECT 1');
PREPARE scf_cancel_reason_stmt FROM @scf_cancel_reason_ddl;
EXECUTE scf_cancel_reason_stmt;
DEALLOCATE PREPARE scf_cancel_reason_stmt;

