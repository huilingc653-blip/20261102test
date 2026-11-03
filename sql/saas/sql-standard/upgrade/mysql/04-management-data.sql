-- ============================================================================
-- SaaS 供应链金融内管相关脚本
-- 数据库：MySQL
-- 脚本：2026-09-20-management-应付债权模板配置预置数据、自定义字段菜单下线
-- 变更：一、预置「供应链金融 / 应付债权模板配置」相关数据，共 205 条 INSERT + 17 条 UPDATE：
--         scf_pay_field_config             字段预设 36 条 + 字段排序修正 17 条
--         scf_platform                     供应链金融平台 4 条
--         sys_dict                         字典 29 条
--         sys_dict_group                   字典组 8 条
--         sys_multi_lang                   多语言 59 条
--         sys_menu_manager                 菜单 3 条
--         sys_resources_manager            资源 4 条
--         tenant_pre_menu                  企业端菜单 20 条
--         tenant_pre_resources             企业端资源 28 条
--         tenant_pre_act_extend_category   审批分类 9 条
--         tenant_pre_config                租户配置 1 条
--         tenant_pre_fc_voucher_type       凭证类型 2 条
--         bt_pay_type                      支付类型 2 条
--       二、下线「自定义字段」维护入口：按 URL / 权限码清理 角色-菜单关联、用户资源授权、
--           资源、菜单 四类数据（均为有界删除），并补 2 条多语言 common.button.custFieldMaintain。
-- 来源：bt-saas-management/bt-saas-management-web/src/main/webapp/sql/
--         scf_mysql.sql、dev_20260930_mysql.sql
-- 生成日期：2026-09-20
-- 注意：面向存量环境，可重复执行；禁止反向覆盖源脚本。
--       表结构见同目录 03-management-schema.sql。
--       表名/列名大小写与源脚本保持一致，避免在区分大小写的 MySQL 上指向不同的表。
--
--       【幂等手段】
--       全部为 INSERT + SELECT ... FROM DUAL WHERE NOT EXISTS（固定主键/业务唯一键），
--       以及带主键 WHERE 的 UPDATE，可重复执行。
--       未采用 INSERT /*+ IGNORE_ROW_ON_DUPKEY_INDEX */ 提示：本批对象在存量库中的唯一索引
--       无法逐一确认，而 NOT EXISTS 守卫本身已足够且与环境无关，故统一只用守卫。
--       源脚本 not exists 子查询里的 limit 1 已按模板移除（EXISTS 本身即短路）。
--
--       【修正源脚本缺陷（MySQL / Oracle 两侧一致）】
--       1. tenant_pre_resources 的主键 470d8f37a2ec49b083937dbae4bcd3e2v1 被用了两次
--          （PERMISSION 分别为 scf:flow:approve:scfFlowLoan 与 scf:flow:approve:scfFlowPayment），
--          两行都带 WHERE NOT EXISTS(ID=...) 守卫，导致第二条**永远插不进去**，
--          而 scf:flow:approve:scfFlowPayment 确实被 tenant_pre_act_extend_category 引用。
--          现将第二条改用新主键 470d8f37a2ec49b083937dbae4bcd3e2v2（守卫同步改），
--          并修正其名称/描述：源脚本两行都误写成「应收债权凭证融资审批资源」，
--          第二条实为到期收款 →「应收债权凭证到期收款审批资源」。
--       2. sys_multi_lang 中 manager.sys.corp.message.corp.only 一行用 UUID()/SYS_GUID()
--          现场生成主键，每次执行主键都不同、无法幂等。改为固定主键
--          e6b2f0a1c3d44f5e8a7b9c0d1e2f3a40。
--
--       【与最新脚本的冲突处理：以最新脚本为准】
--       scf_mysql.sql 还插入「自定义字段」菜单与资源共 3 行，而更新的 dev_20260930_mysql.sql
--       （2026-09-30）删除的正是这些对象。按「以最新脚本为准」，本文件**不插入**这 3 条：
--         sys_menu_manager      b7e1730128554807a60e6e3b63bc77ff（/enterpriseBusiness/scfCustFieldConfig/list）
--         sys_resources_manager b6ba7c729e65437884fb1693b0869aa4（scf:custfield:config:query）
--         sys_resources_manager 2dfd2972bdc24b51b9ddc2ba2491104c（scf:custfield:config:apply）
--       改由本文件第二部分清理存量残留。注意 scf_cust_field_config 表本身不下线
--       （仍由 03-management-schema.sql 建表），该表在源脚本中本就没有预置数据，
--       下线的只是上面那两个页面入口。
-- ============================================================================

-- ---------------------------------------------------------------------------
-- 第一部分：应付债权模板配置预置数据（来源 scf_mysql.sql）
-- ---------------------------------------------------------------------------


-- 初始化配置
-- baseInfo（按 field_sort 升序）
INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '6f4e2d8c9a1b3e7f5d6c4a2b8e9f0d1c', 1, 'corpId', '开立单位', 'component', 'baseInfo', NULL, NULL, 'text', NULL, NULL, 'corpcom', NULL, NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '6f4e2d8c9a1b3e7f5d6c4a2b8e9f0d1c');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'f47ac10b58cc4372a5670e02b2c3d479', 2, 'voucherCode', '债权凭证编号', 'string', 'baseInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'f47ac10b58cc4372a5670e02b2c3d479');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6', 3, 'onlineSign', '线上线下标识', 'dict', 'baseInfo', NULL, NULL, 'text', 'scf.common.onlineSign', NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '9b1deb4d3b7d4bad9bdd2b0d7b3dcb6d', 4, 'platformId', '平台', 'select', 'baseInfo', NULL, NULL, 'text', NULL, '/getPlatformJson', NULL, 'productId', 'platformName', 'id', NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '9b1deb4d3b7d4bad9bdd2b0d7b3dcb6d');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '5e884898da28047151d0e56f8dc62927', 5, 'productId', '融资产品', 'select', 'baseInfo', NULL, NULL, 'text', NULL, '/getProductJson', NULL, NULL, 'productName', 'id', NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '5e884898da28047151d0e56f8dc62927');
##GOGO

-- 融资付息方式（字典）
INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '01a2b3c4d5e60718293a4b5c6d7e8f9a', 6, 'interestMode', '融资付息方式', 'dict', 'baseInfo', '1', NULL, 'text', 'scf.voucher.interestMode', NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '01a2b3c4d5e60718293a4b5c6d7e8f9a');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '7d6c5b4a3f2e1d0c9b8a7f6e5d4c3b2a', 7, 'coreCorp', '核心企业', 'component', 'baseInfo', NULL, NULL, 'text', NULL, NULL, 'corpcom', NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '7d6c5b4a3f2e1d0c9b8a7f6e5d4c3b2a');
##GOGO

UPDATE scf_pay_field_config
SET field_type = 'component'
WHERE id = '7d6c5b4a3f2e1d0c9b8a7f6e5d4c3b2a';
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '2e4f6a8c0d1e3b5d7f9a2c4e6b8d0f1a', 8, 'curId', '币别', 'component', 'baseInfo', '4028802e5c346968015c346c18850008', NULL, 'text', NULL, NULL, 'curcom', NULL, NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '2e4f6a8c0d1e3b5d7f9a2c4e6b8d0f1a');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '1a2b3c4d5e6f7890abcdef1234567890', 9, 'voucherMoney', '开立金额', 'money', 'baseInfo', NULL, NULL, 'money', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '1a2b3c4d5e6f7890abcdef1234567890');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '3a7b9c1d2e4f5a6b7c8d9e0f1a2b3c4d', 10, 'openDate', '开立日期', 'date', 'baseInfo', NULL, NULL, 'date', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '3a7b9c1d2e4f5a6b7c8d9e0f1a2b3c4d');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '8c3b5d7e9f1a2b4c6d8e0f1a2b3c4d5e', 11, 'expireDate', '到期日', 'date', 'baseInfo', NULL, NULL, 'date', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '8c3b5d7e9f1a2b4c6d8e0f1a2b3c4d5e');
##GOGO

-- 承诺付款日（日期）
INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '11b2c3d4e5f60718293a4b5c6d7e8f9b', 12, 'promisePayDate', '承诺付款日', 'date', 'baseInfo', NULL, NULL, 'date', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '11b2c3d4e5f60718293a4b5c6d7e8f9b');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9', 13, 'supplyId', '供应商', 'popup', 'baseInfo', NULL, NULL, 'text', NULL, NULL, NULL, 'payAccount,payBank', NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5', 14, 'receiveAccount', '收款账号', 'string', 'baseInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'd0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5', 15, 'receiveBank', '收款银行', 'string', 'baseInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'd0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5');
##GOGO

-- 供应商统一社会信用代码（字符串）
INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '21c2d3e4f5a60718293a4b5c6d7e8f9c', 16, 'supplyCreditCode', '供应商统一社会信用代码', 'string', 'baseInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '21c2d3e4f5a60718293a4b5c6d7e8f9c');
##GOGO

-- baseInfo 区域顺序调整（存量环境修正，按主键 id 定位；curId 在 invoiceInfo 区域同名，不能用 field_code 定位）
UPDATE scf_pay_field_config
SET field_sort = 1
WHERE id = '6f4e2d8c9a1b3e7f5d6c4a2b8e9f0d1c';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 2
WHERE id = 'f47ac10b58cc4372a5670e02b2c3d479';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 3
WHERE id = 'e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 4
WHERE id = '9b1deb4d3b7d4bad9bdd2b0d7b3dcb6d';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 5
WHERE id = '5e884898da28047151d0e56f8dc62927';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 6
WHERE id = '01a2b3c4d5e60718293a4b5c6d7e8f9a';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 7
WHERE id = '7d6c5b4a3f2e1d0c9b8a7f6e5d4c3b2a';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 8
WHERE id = '2e4f6a8c0d1e3b5d7f9a2c4e6b8d0f1a';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 9
WHERE id = '1a2b3c4d5e6f7890abcdef1234567890';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 10
WHERE id = '3a7b9c1d2e4f5a6b7c8d9e0f1a2b3c4d';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 11
WHERE id = '8c3b5d7e9f1a2b4c6d8e0f1a2b3c4d5e';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 12
WHERE id = '11b2c3d4e5f60718293a4b5c6d7e8f9b';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 13
WHERE id = 'b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 14
WHERE id = 'c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 15
WHERE id = 'd0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5';
##GOGO

UPDATE scf_pay_field_config
SET field_sort = 16
WHERE id = '21c2d3e4f5a60718293a4b5c6d7e8f9c';
##GOGO

-- quotaInfo（按 field_sort 升序）
INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7', 1, 'quotaCode', '额度编码', 'input', 'quotaInfo', NULL, NULL, 'text', NULL, '在线查询接口', NULL, 'quotaName,quotaBalance', NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8', 2, 'quotaName', '额度名称', 'string', 'quotaInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'c5c5d6e7f8a9b0c1d2e3f4a5b6c79999', 3, 'quotaBalance', '可用额度', 'money', 'quotaInfo', NULL, NULL, 'money', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'c5c5d6e7f8a9b0c1d2e3f4a5b6c79999');
##GOGO

-- contractInfo（按 field_sort 升序）
INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0', 1, 'contractCode', '交易合同编码', 'string', 'contractInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'd6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1', 2, 'contractName', '交易合同名称', 'string', 'contractInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'd6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2', 3, 'contractMoney', '交易合同金额', 'money', 'contractInfo', NULL, NULL, 'money', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3', 4, 'contractDate', '交易合同日期', 'date', 'contractInfo', NULL, NULL, 'date', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4', 5, 'contractFile', '交易合同附件', 'component', 'contractInfo', NULL, NULL, 'text', NULL, NULL, 'fileCom', NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4');
##GOGO

-- invoiceInfo（按 field_sort 升序）
INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5', 1, 'invoiceType', '发票类型', 'string', 'invoiceInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6', 2, 'invoiceNumber', '发票号码', 'string', 'invoiceInfo', NULL, NULL, 'string', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'd2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7', 3, 'invoiceCode', '发票代码', 'string', 'invoiceInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'd2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8', 4, 'billDate', '开票日期', 'date', 'invoiceInfo', NULL, NULL, 'date', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9', 5, 'curId', '币别', 'component', 'invoiceInfo', NULL, NULL, 'text', NULL, NULL, 'curcom', NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0', 6, 'totalAmt', '价税合计', 'money', 'invoiceInfo', NULL, NULL, 'money', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1', 7, 'taxRate', '税率', 'money', 'invoiceInfo', NULL, NULL, 'money', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2', 8, 'taxAmt', '税额', 'money', 'invoiceInfo', NULL, NULL, 'money', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'd8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3', 9, 'billAmt', '金额', 'money', 'invoiceInfo', NULL, NULL, 'money', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'd8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4', 10, 'sellerName', '销售名称', 'string', 'invoiceInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT 'f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5', 11, 'buyerName', '购方名称', 'string', 'invoiceInfo', NULL, NULL, 'text', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = 'f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5');
##GOGO

INSERT IGNORE INTO scf_pay_field_config (id, field_sort, field_code, field_name, field_type, field_area, field_def_val, field_length, data_type, data_group, data_url, data_component, data_linkage, data_textfield, data_valuefield, is_limit_length, is_visible, is_required, valid_sign, is_sys)
SELECT '0fa1b2c3d4e5f6a7b8c9d0e1f2a35bc4', 12, 'invoiceFile', '发票附件', 'component', 'invoiceInfo', NULL, NULL, 'text', NULL, NULL, 'fileCom', NULL, NULL, NULL, NULL, 1, 0, '1', '0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_pay_field_config WHERE id = '0fa1b2c3d4e5f6a7b8c9d0e1f2a35bc4');
##GOGO

-- 国际化配置
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e6b2f0a1c3d44f5e8a7b9c0d1e2f3a40', 'zh_CN', '机构类型只能填成员单位', 'manager.sys.corp.message.corp.only'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'manager.sys.corp.message.corp.only' AND LANG_CODE = 'zh_CN'
);
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd266cc1d93334165a8228a3a7f400862', 'zh_CN', '有效标识', 'scf.validSign'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.validSign' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8345bfbffce44188888dbfa15f8975c6', 'zh_CN', '融资产品', 'scf.platform.product'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.platform.product' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '511a79e060724fdcb890ce01fe0eca28', 'zh_CN', '供应链金融平台', 'scf.platform.name'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.platform.name' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '94549e4abfd948d2ba08181fc7baf408', 'zh_CN', '模版名称', 'scf.template.name'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.template.name' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3bc86df544e34267b6ef69e60948ea05', 'zh_CN', '模版编码', 'scf.template.code'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.template.code' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3bc86df544e34267b6ef69e609484485', 'zh_CN', '页面配置', 'scf.template.config'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.template.config' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3bc86df544e34267b6ef69e609654312', 'zh_CN', '基本信息', 'scf.template.baseinfo'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.template.baseinfo' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '49c86df544e34267b6ef69e609654321', 'zh_CN', '模版描述', 'scf.template.desc'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.template.desc' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c9586df544e34267b6ef69e609654321', 'zh_CN', '备注', 'scf.template.rmk'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.template.rmk' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3bc86df544e34267b6ef69e609654321', 'zh_CN', '明细信息', 'scf.template.details'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.template.details' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0d2d64a411f1997c005056b98765', 'zh_CN', '应付债权凭证页面配置', 'scf.payTemplate.config'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.payTemplate.config' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4a411df544e34267b6ef69e609654321', 'zh_CN', '批量添加自定义字段', 'scf.template.addBatchCustomField'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.template.addBatchCustomField' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 字段名称
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '313d313d0d2d64a411f1997c005056b9', 'zh_CN', '字段名称', 'table.scf.fieldName'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldName' AND LANG_CODE = 'zh_CN');
##GOGO

-- 是否可见
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c', 'zh_CN', '是否可见', 'table.scf.isVisible'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.isVisible' AND LANG_CODE = 'zh_CN');
##GOGO

-- 是否必填
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d', 'zh_CN', '是否必填', 'table.scf.isRequired'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.isRequired' AND LANG_CODE = 'zh_CN');
##GOGO

-- 是否控制长度
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1a2b3c4d5e6f7890abcdef1234567890', 'zh_CN', '是否控制长度', 'table.scf.isLength'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.isLength' AND LANG_CODE = 'zh_CN');
##GOGO

-- 长度限制
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9a8b7c6d5e4f3a2b1c0d9e8f7a6b5c4d', 'zh_CN', '长度限制', 'table.scf.fieldLength'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldLength' AND LANG_CODE = 'zh_CN');
##GOGO

-- 是否限定枚举值
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e', 'zh_CN', '是否限定枚举值', 'table.scf.isLimitedDict'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.isLimitedDict' AND LANG_CODE = 'zh_CN');
##GOGO

-- 枚举值
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f', 'zh_CN', '枚举值', 'table.scf.fieldLimitedDict'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldLimitedDict' AND LANG_CODE = 'zh_CN');
##GOGO

-- 默认值
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a', 'zh_CN', '默认值', 'table.scf.fieldDefVal'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldDefVal' AND LANG_CODE = 'zh_CN');
##GOGO

-- 区域
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b', 'zh_CN', '区域', 'table.scf.fieldArea'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldArea' AND LANG_CODE = 'zh_CN');
##GOGO

-- 排序
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c', 'zh_CN', '排序', 'table.scf.fieldSort'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldSort' AND LANG_CODE = 'zh_CN');
##GOGO

-- 字段类型
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0b1c8b9c0d1e2f3a4b5c6d7e8f9a6f7a', 'zh_CN', '字段类型', 'table.scf.fieldType'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldType' AND LANG_CODE = 'zh_CN');
##GOGO

-- 字段编码
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2f3c8b9c0d1ea4b5c6d0b17e8f9a6f7a', 'zh_CN', '字段编码', 'table.scf.fieldCode'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldCode' AND LANG_CODE = 'zh_CN');
##GOGO

-- 字段描述
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7e8c8b9c0d1e2f3a4b5ob1c6df9a6f7a', 'zh_CN', '字段描述', 'table.scf.fieldDesc'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldDesc' AND LANG_CODE = 'zh_CN');
##GOGO

-- 字段名称
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9c0d1ea4b5c62f3c8bd0b17e8f9a6f7a', 'zh_CN', '字段名称', 'scf.field.name'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.field.name' AND LANG_CODE = 'zh_CN');
##GOGO

-- 字段类型
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '29c0d1ea4b5c6f3c8bd0b17e8f9a6f7a', 'zh_CN', '字段类型', 'scf.field.type'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.field.type' AND LANG_CODE = 'zh_CN');
##GOGO

-- 字段属性
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ob17e8c8b9f7ac0d1e2f3a4b5c6df9a6', 'zh_CN', '字段属性', 'table.scf.fieldAttr'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.scf.fieldAttr' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '1ace9c50617d421483d4bb140af0035d', 'scf.templateField.dataArea', '供应链金融字段区域'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = '1ace9c50617d421483d4bb140af0035d');
##GOGO

-- 1. 基本信息
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '1d4004272a4c4520ac89c9ed2aaa3fad', 'baseInfo', '基本信息', NULL, '1ace9c50617d421483d4bb140af0035d', 10
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '1d4004272a4c4520ac89c9ed2aaa3fad');
##GOGO

-- 2. 额度信息
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '0b0a485a12db4a6bb4fc3b298767654f', 'quotaInfo', '额度信息', NULL, '1ace9c50617d421483d4bb140af0035d', 20
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '0b0a485a12db4a6bb4fc3b298767654f');
##GOGO

-- 3. 贸易合同信息
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '8f68da555654492c874bdb8277d71e43', 'contractInfo', '贸易合同信息', NULL, '1ace9c50617d421483d4bb140af0035d', 30
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '8f68da555654492c874bdb8277d71e43');
##GOGO

-- 4. 发票信息
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'b8bb1bd7eef34815b79c4058bf6b8bc7', 'invoiceInfo', '发票信息', NULL, '1ace9c50617d421483d4bb140af0035d', 40
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'b8bb1bd7eef34815b79c4058bf6b8bc7');
##GOGO

-- 5. 其他信息
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9e16729e445345a09d16bd04445b6559', 'otherInfo', '自定义信息', NULL, '1ace9c50617d421483d4bb140af0035d', 50
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9e16729e445345a09d16bd04445b6559');
##GOGO

INSERT IGNORE INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '035d9c50617d421483d4bb140af01ace', 'scf.field.type', '字段类型'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = '035d9c50617d421483d4bb140af01ace');
##GOGO

-- 1. 文本
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'b7cf54ebdb70460ca271948505e1fac4', 'text', '文本', NULL, '035d9c50617d421483d4bb140af01ace', 10
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'b7cf54ebdb70460ca271948505e1fac4');
##GOGO

-- 2. 数字
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '4afe2f86541c46269c2099f210e0111f', 'number', '数字', NULL, '035d9c50617d421483d4bb140af01ace', 20
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '4afe2f86541c46269c2099f210e0111f');
##GOGO

-- 3. 金额
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '7c8ea6dc25d140bfbded5c75c27038e8', 'money', '金额', NULL, '035d9c50617d421483d4bb140af01ace', 30
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '7c8ea6dc25d140bfbded5c75c27038e8');
##GOGO

-- 4. 日期
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'e5a1c084feaf400da98f48e54a1df100', 'date', '日期', NULL, '035d9c50617d421483d4bb140af01ace', 40
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'e5a1c084feaf400da98f48e54a1df100');
##GOGO

-- 5. 时间
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '7a538bb56d1e47819265c00760d160ab', 'datetime', '时间', NULL, '035d9c50617d421483d4bb140af01ace', 50
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '7a538bb56d1e47819265c00760d160ab');
##GOGO

-- 6. 字典组
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'a28ea98fa7ba43a8b2c6562dcf9edf15', 'dict', '字典组', NULL, '035d9c50617d421483d4bb140af01ace', 50
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'a28ea98fa7ba43a8b2c6562dcf9edf15');
##GOGO

-- 交易配置模板编号已存在
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b2c3d4e5f678901234567890123456', 'zh_CN', '交易配置模板编号已存在', 'scf.config.template.no.existed'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.config.template.no.existed' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 配置数据不能为空，请检查！
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b2c3d4e5f67890123456789012345678', 'zh_CN', '配置数据不能为空，请检查！', 'scf.not.config.null.data'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.not.config.null.data' AND LANG_CODE = 'zh_CN');
##GOGO

-- 请勿配置重复数据
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c3d4e5f6789012345678901234567890', 'zh_CN', '请勿配置重复数据', 'scf.not.config.duplicate.data'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.not.config.duplicate.data' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 一个平台只能配置一个模板
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd4e5f6789012345678901234567890ab', 'zh_CN', '一个平台只能配置一个模板', 'scf.not.config.platform.only.one'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.not.config.platform.only.one' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 自定义字段已被模版使用，无法修改为无效！
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e5f6789012345678901234567890abcd', 'zh_CN', '自定义字段已被模版使用，无法修改为无效！', 'scf.cust.field.used.not.update'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.cust.field.used.not.update' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 自定义字段已被模版使用，无法删除！
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f6789012345678901234567890abcde', 'zh_CN', '自定义字段已被模版使用，无法删除！', 'scf.cust.field.used.not.delete'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.cust.field.used.not.delete' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 该产品已被使用，无法删除！
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b2c3d4e5f67890abcdef1234567890', 'zh_CN', '该产品已被使用，无法删除！', 'scf.product.used.not.delete'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.product.used.not.delete' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 该平台下存在有效的融资产品，无法设为无效！
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b2c3d4e5f67890abcdef1234567890ab', 'zh_CN', '该平台下存在有效的融资产品，无法设为无效！', 'scf.platform.has.valid.product'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.platform.has.valid.product' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 请选择同一平台下的产品
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c3d4e5f67890abcdef1234567890abcd', 'zh_CN', '请选择同一平台下的产品', 'scf.not.same.platform'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.not.same.platform' AND LANG_CODE = 'zh_CN');
##GOGO

-- 该模板已被应付债权凭证引用，无法设置无效或者删除
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd4e5f67890abcdef1234567890abcde', 'zh_CN', '该模板已被应付债权凭证引用，无法设置无效或者删除', 'scf.template.has.pay.config'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.template.has.pay.config' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 字段编码已存在，请重新输入！
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e5f67890abcdef1234567890abcdef12', 'zh_CN', '字段编码已存在，请重新输入！', 'scf.cust.field.code.exist'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.cust.field.code.exist' AND LANG_CODE = 'zh_CN');
##GOGO

-- 自定义字段已被模版使用，不能修改编码！
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cb138bd5c66549f69dcc01fd8f2bdf34', 'zh_CN', '自定义字段已被模版使用，不能修改编码！', 'scf.cust.field.code.used.not.modify'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.cust.field.code.used.not.modify' AND LANG_CODE = 'zh_CN'
);
##GOGO

-- 自定义字段已被模版使用，不能修改类型！
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9ad35c1e5ccc4d8a96e2eb9ff0c9bb46', 'zh_CN', '自定义字段已被模版使用，不能修改类型！', 'scf.cust.field.type.used.not.modify'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.cust.field.type.used.not.modify' AND LANG_CODE = 'zh_CN'
);
##GOGO

INSERT IGNORE INTO tenant_pre_config (ID, TYPE, NAME, VALUE, IS_INIT)
SELECT '842e46592c104a03bc3bda63f8a65ea7', 'scf', 'instructionMode', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_config WHERE ID = '842e46592c104a03bc3bda63f8a65ea7');
##GOGO

-- ##【供应链金融】父菜单
INSERT IGNORE INTO sys_menu_manager (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '431d46957cce4fd0b5cdf332abc65af8', NULL, '供应链金融', '供应链金融', '1b9abd52a56d4b858a2682f811af0a6b', NULL, 100, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu_manager WHERE ID = '431d46957cce4fd0b5cdf332abc65af8');
##GOGO

-- ##【供应链金融平台】菜单
INSERT IGNORE INTO sys_menu_manager (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT 'bcce4938c87c4cf282cfb9a195433d7d', NULL, '供应链金融平台', '供应链金融平台', '431d46957cce4fd0b5cdf332abc65af8', NULL, 1, 1, '/enterpriseBusiness/scfPlatformManager/list', NULL, NULL, NULL, NULL, NULL, NULL, 0, 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu_manager WHERE ID = 'bcce4938c87c4cf282cfb9a195433d7d');
##GOGO

-- ##【供应链金融平台】查询资源
INSERT IGNORE INTO sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '549caa3501b94d49a05a2899117675d3', '查询资源', '查询资源', 'scf:platform:query', 1, 'bcce4938c87c4cf282cfb9a195433d7d'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_resources_manager WHERE ID = '549caa3501b94d49a05a2899117675d3');
##GOGO

-- ##【供应链金融平台】应用资源
INSERT IGNORE INTO sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'd485a3d8540a4fd99eb69208d17a6cc9', '应用资源', '应用资源', 'scf:platform:apply', 1, 'bcce4938c87c4cf282cfb9a195433d7d'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_resources_manager WHERE ID = 'd485a3d8540a4fd99eb69208d17a6cc9');
##GOGO

-- ##【应付债权凭证模版】菜单
INSERT IGNORE INTO sys_menu_manager (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND)
SELECT '53cf27026fa547da9557e61e386bd343', NULL, '应付债权凭证模版', '应付债权凭证模版', '431d46957cce4fd0b5cdf332abc65af8', NULL, 2, 1, '/enterpriseBusiness/scfPayConfigVersion/list', NULL, NULL, NULL, NULL, NULL, NULL, 0, 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu_manager WHERE ID = '53cf27026fa547da9557e61e386bd343');
##GOGO

-- ##【应付债权模板配置】查询资源
INSERT IGNORE INTO sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '50a6f4f8ef4247f8aa07d05337720501', '查询资源', '查询资源', 'scf:paytemplate:config:query', 1, '53cf27026fa547da9557e61e386bd343'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_resources_manager WHERE ID = '50a6f4f8ef4247f8aa07d05337720501');
##GOGO

-- ##【应付债权模板配置】应用资源
INSERT IGNORE INTO sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '4bf462f95068417abcf3b42a6d56abcb', '应用资源', '应用资源', 'scf:paytemplate:config:apply', 1, '53cf27026fa547da9557e61e386bd343'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_resources_manager WHERE ID = '4bf462f95068417abcf3b42a6d56abcb');
##GOGO

-- 供应链金融平台
-- 融资类型字典组
INSERT IGNORE INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '7143820545414e07bb66dbba3d831bd7', 'finance.type', '融资类型'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = '7143820545414e07bb66dbba3d831bd7');
##GOGO

-- 1.电子债权凭证
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '6aa6039089884bb9a7e6fd300631dd4a', '1', '电子债权凭证', NULL, '7143820545414e07bb66dbba3d831bd7', 500
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '6aa6039089884bb9a7e6fd300631dd4a');
##GOGO

-- 接口开通情况字典组
-- 额度管理
INSERT IGNORE INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '5490f65b97e9461faba92f0bf8054c58', 'scf.interface.credit', '额度管理'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = '5490f65b97e9461faba92f0bf8054c58');
##GOGO

-- 1.额度管理-额度查询
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '4a53bf5011ef4e9a8d51c66d83965d03', '60', '额度查询', NULL, '5490f65b97e9461faba92f0bf8054c58', 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '4a53bf5011ef4e9a8d51c66d83965d03');
##GOGO

-- 应付债权凭证
INSERT IGNORE INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '80d073f58b464a48a3281af07bad5d66', 'scf.interface.payable', '应付债卷凭证'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = '80d073f58b464a48a3281af07bad5d66');
##GOGO

-- 1.应付债卷凭证-凭证开立
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'b4eb852d9a634fcfac2a196e46eecd10', '61', '凭证开立', NULL, '80d073f58b464a48a3281af07bad5d66', 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'b4eb852d9a634fcfac2a196e46eecd10');
##GOGO

-- 2.应付债卷凭证-开立撤销
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '07bb56b2e795429eb4d261337ae92dd8', '62', '开立撤销', NULL, '80d073f58b464a48a3281af07bad5d66', 2
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '07bb56b2e795429eb4d261337ae92dd8');
##GOGO

-- 3.应付债卷凭证-兑付结果查询
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'f741560a63334e4d9b410a831632f895', '63', '兑付结果查询', NULL, '80d073f58b464a48a3281af07bad5d66', 3
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'f741560a63334e4d9b410a831632f895');
##GOGO

-- 应收债权凭证
INSERT IGNORE INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT 'e92c0cd8b4e14befbafeebc94921d1a7', 'scf.interface.receivable', '应收债权凭证'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = 'e92c0cd8b4e14befbafeebc94921d1a7');
##GOGO

-- 1.应收债权凭证-债权凭证查询
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '193d4b4e899944d6b56371bf6d07eae0', '1', '债权凭证查询', NULL, 'e92c0cd8b4e14befbafeebc94921d1a7', 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '193d4b4e899944d6b56371bf6d07eae0');
##GOGO

-- 2.应收债权凭证-债权凭证签收
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'a06e8994dd3d4ceeae007803e00b85ce', '2', '债权凭证签收', NULL, 'e92c0cd8b4e14befbafeebc94921d1a7', 2
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'a06e8994dd3d4ceeae007803e00b85ce');
##GOGO

-- 3.应收债权凭证-债权凭证转让
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '02603e260c8849359069ec238c2cd0bc', '3', '债权凭证转让', NULL, 'e92c0cd8b4e14befbafeebc94921d1a7', 3
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '02603e260c8849359069ec238c2cd0bc');
##GOGO

-- 4.应收债权凭证-转让结果
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '416c1e4129fe4c35bcf74a142c324f64', '4', '转让结果', NULL, 'e92c0cd8b4e14befbafeebc94921d1a7', 4
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '416c1e4129fe4c35bcf74a142c324f64');
##GOGO

-- 5.应收债权凭证-融资申请
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '864339de602f43508bfc794885ec4907', '5', '融资申请', NULL, 'e92c0cd8b4e14befbafeebc94921d1a7', 5
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '864339de602f43508bfc794885ec4907');
##GOGO

-- 6.应收债权凭证-融资结果查询
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '880d94d980aa4825bae0d9830411dcae', '6', '融资结果查询', NULL, 'e92c0cd8b4e14befbafeebc94921d1a7', 6
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '880d94d980aa4825bae0d9830411dcae');
##GOGO

-- 7.应收债权凭证-到期收款查询
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'dd431c583b914ddbaf0c67fcec2951b7', '7', '到期收款查询', NULL, 'e92c0cd8b4e14befbafeebc94921d1a7', 7
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'dd431c583b914ddbaf0c67fcec2951b7');
##GOGO

-- 接口配置字典组
INSERT IGNORE INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT 'ae1b2e36d863444dbe4fa4672c028b91', 'scf.interface.type', '接口配置'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = 'ae1b2e36d863444dbe4fa4672c028b91');
##GOGO

-- 1.接口配置-额度管理
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '88103ad7880e465b8d6c0c2027ddbd25', 'creditApis', '额度管理', NULL, 'ae1b2e36d863444dbe4fa4672c028b91', 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '88103ad7880e465b8d6c0c2027ddbd25');
##GOGO

-- 2.接口配置-应付债权凭证
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'ad854dbd1ec44b50abe62b7a7b872c3a', 'payableApis', '应付债权凭证', NULL, 'ae1b2e36d863444dbe4fa4672c028b91', 2
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'ad854dbd1ec44b50abe62b7a7b872c3a');
##GOGO

-- 3.接口配置-应收债权凭证
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '17945483bd2f40c999174552fbaad06d', 'receivableApis', '应收债权凭证', NULL, 'ae1b2e36d863444dbe4fa4672c028b91', 3
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '17945483bd2f40c999174552fbaad06d');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8188c5bd37c046a7bf781629be6d9dab', 'zh_CN', '额度管理：', 'interface.credit'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'interface.credit' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd7806bcb9299433f822b1d83aa0ea669', 'zh_CN', '应收债权凭证：', 'interface.receivable'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'interface.receivable' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '509012f513de40cab52c9a1bf27b6035', 'zh_CN', '应付债权凭证：', 'interface.payable'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'interface.payable' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b1e7794e72c64b4499b4bcc4b6fdd18d', 'zh_CN', '接口开通情况', 'scf.Interface'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.Interface' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f061b8f3aa7f4e428a249574719ae3b3', 'zh_CN', '产品编号', 'scf.product.code'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.product.code' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'deea9db5310d4a928fc53b8ecaade68f', 'zh_CN', '供应链融资产品', 'scf.finance.product'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.finance.product' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1ef39435a619441cbe6bf6cbdaa6b967', 'zh_CN', '平台编码', 'nis.platform.code'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'nis.platform.code' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5cefd130e9684b73a40a03a2863383a5', 'zh_CN', '平台名称', 'nis.platform.name'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'nis.platform.name' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3f0d0934c6da44fe99e10182d422001e', 'zh_CN', '该平台为系统预设，无法删除！', 'scf.platform.protected.not.delete'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.platform.protected.not.delete' AND LANG_CODE = 'zh_CN'
);
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '35abb452e7c443e5a9905d96d3f4e76e', 'zh_CN', '该平台下存在有效的融资产品，无法设为无效！', 'scf.platform.has.valid.product'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.platform.has.valid.product' AND LANG_CODE = 'zh_CN'
);
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f283f0ac015b4669af887d877325d823', 'zh_CN', '该产品已被使用，无法删除！', 'scf.product.used.not.delete'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.product.used.not.delete' AND LANG_CODE = 'zh_CN'
);
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1242460a22d34828940a8e7625543c00', 'zh_CN', '产品名称不能重复!', 'scf.product.non.repetitive'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.product.non.repetitive' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6565ab25ed9d473f8d82f78494c5a7d0', 'zh_CN', '该平台已有在途数据，不能修改！', 'scf.platform.used.not.modify'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.platform.used.not.modify' AND LANG_CODE = 'zh_CN'
);
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cfebd7cc83ab48169e6a8288704b2c19', 'zh_CN', '字', 'sys.form.verify.unit'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'sys.form.verify.unit' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ecb13d343b3f48768a3d1c2552513c78', 'zh_CN', '至少保留一行', 'scf.product'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.product' AND LANG_CODE = 'zh_CN');
##GOGO

-- scf_platform 系统预设平台
INSERT IGNORE INTO scf_platform (ID, PLATFORM_CODE, PLATFORM_NAME, VALID, RMK, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE)
SELECT '56fb258e695e43a8bdd29cb61551d6eb', 'SCF004', '建信融通', 1, NULL, '402880425b6b0a58015b6b0abeb10000', '2026-07-20 10:05:11.0', '402880425b6b0a58015b6b0abeb10000', '2026-07-20 10:05:15.0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_platform WHERE ID = '56fb258e695e43a8bdd29cb61551d6eb');
##GOGO

INSERT IGNORE INTO scf_platform (ID, PLATFORM_CODE, PLATFORM_NAME, VALID, RMK, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE)
SELECT '7b9f14165f164f6282929a9b82ed847d', 'SCF002', '中企云链', 1, NULL, '402880425b6b0a58015b6b0abeb10000', '2026-07-20 10:03:31.0', '402880425b6b0a58015b6b0abeb10000', '2026-07-20 10:03:31.0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_platform WHERE ID = '7b9f14165f164f6282929a9b82ed847d');
##GOGO

INSERT IGNORE INTO scf_platform (ID, PLATFORM_CODE, PLATFORM_NAME, VALID, RMK, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE)
SELECT '7f5d1c4999c3403fa27278f2c4066db6', 'SCF003', '简单汇', 1, NULL, '402880425b6b0a58015b6b0abeb10000', '2026-07-20 10:04:15.0', '402880425b6b0a58015b6b0abeb10000', '2026-07-20 10:04:15.0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_platform WHERE ID = '7f5d1c4999c3403fa27278f2c4066db6');
##GOGO

INSERT IGNORE INTO scf_platform (ID, PLATFORM_CODE, PLATFORM_NAME, VALID, RMK, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE)
SELECT '9fa14df53cfd4cf7876b2ab80a144838', 'SCF001', '联易融', 1, NULL, '402880425b6b0a58015b6b0abeb10000', '2026-07-20 10:01:32.0', '402880425b6b0a58015b6b0abeb10000', '2026-07-20 10:01:32.0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM scf_platform WHERE ID = '9fa14df53cfd4cf7876b2ab80a144838');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c10dd1bb7b1d465aa436bc5ce25b1775', 'zh_CN', '平台名称已存在，请重新输入！', 'scf.platform.name.exist'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.platform.name.exist' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'df7a2c1e4b6d4c8f9e1a2b3c4d5e6f78', 'zh_CN', '平台编码已被占用，请重新打开页面！', 'scf.platform.code.exist'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.platform.code.exist' AND LANG_CODE = 'zh_CN');
##GOGO

-- 供应链金融管理（顶级）
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '45ee4780ab564ff7a4117ae17bedc052v1', '1', '供应链金融管理', '供应链金融管理', NULL, NULL, 10, 1, NULL, NULL, 'icon-jggxgl', NULL, NULL, NULL, NULL, 1, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '45ee4780ab564ff7a4117ae17bedc052v1');
##GOGO

-- 基础配置
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', '2', '基础配置', '基础配置', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = 'dfcea55a6c734e25bb8c62c6ba6e4dedv1');
##GOGO

-- 参数配置
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '9bb2d2fe514c421aa862d01ba5db9137v1', '3', '参数配置', '参数配置', 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/baseConfig/parameterSetting', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '9bb2d2fe514c421aa862d01ba5db9137v1');
##GOGO

-- 平台直联设置
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '157d094324234e2ab7673a373e5883ffv1', '4', '平台直联设置', '平台直联设置', 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/baseConfig/platformDirectSetting', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '157d094324234e2ab7673a373e5883ffv1');
##GOGO

-- 查询资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '395734ec80e2787b932154ce9d3383e6v1', '查询资源', '查询资源', 'scf:ent:platform:query', 1, '157d094324234e2ab7673a373e5883ffv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '395734ec80e2787b932154ce9d3383e6v1');
##GOGO

--  应用资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '8v5734ec80e2497b932154ce9d3383e8v1', '应用资源', '应用资源', 'scf:ent:platform:apply', 1, '157d094324234e2ab7673a373e5883ffv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '8v5734ec80e2497b932154ce9d3383e8v1');
##GOGO

-- 资金方
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT 'ff2db81e59d9483eb525fc978b52bdc9v1', '5', '资金方', '资金方', 'dfcea55a6c734e25bb8c62c6ba6e4dedv1', NULL, 3, 1, '/sub-finance/#/supplyChainFinance/baseConfig/funder', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = 'ff2db81e59d9483eb525fc978b52bdc9v1');
##GOGO

-- 查询资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'dc101004d4ee40c5b1696cdb0c1c4b41', '查询资源', '查询资源', 'scf:funder:info:query', 1, 'ff2db81e59d9483eb525fc978b52bdc9v1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'dc101004d4ee40c5b1696cdb0c1c4b41');
##GOGO

--  应用资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '90243552241343278f9e25ff13f1dcce', '应用资源', '应用资源', 'scf:funder:info:apply', 1, 'ff2db81e59d9483eb525fc978b52bdc9v1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '90243552241343278f9e25ff13f1dcce');
##GOGO

-- 电子债权凭证
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '4dac33e0226249eab31cb7313382174fv1', '6', '电子债权凭证', '电子债权凭证', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '4dac33e0226249eab31cb7313382174fv1');
##GOGO

-- 应付债权凭证管理
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT 'd2c496569f454cdea106158935f0d676v1', '7', '应付债权凭证管理', '应付债权凭证管理', '4dac33e0226249eab31cb7313382174fv1', NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = 'd2c496569f454cdea106158935f0d676v1');
##GOGO

-- 应付债权凭证
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '9b856832b17243b19d317d4dd2766eb9v1', '8', '应付债权凭证', '应付债权凭证', 'd2c496569f454cdea106158935f0d676v1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/payable-voucher', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '9b856832b17243b19d317d4dd2766eb9v1');
##GOGO

-- 应付债权凭证查询资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f60001v1', '应付债权凭证查询', '应付债权凭证查询', 'scf:payable:voucher:query', 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f60001v1');
##GOGO

-- 应付债权凭证修改资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '4e5f6a1b2c3d4789a1b2c3d4e5f60002v1', '应付债权凭证修改', '应付债权凭证修改', 'scf:payable:voucher:modify', 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '4e5f6a1b2c3d4789a1b2c3d4e5f60002v1');
##GOGO

-- 兑付记录
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '2a765b4789cc483ea87aa6f36a5d54f5v1', '9', '兑付记录', '兑付记录', 'd2c496569f454cdea106158935f0d676v1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/voucher-redemption', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '2a765b4789cc483ea87aa6f36a5d54f5v1');
##GOGO

-- 应付债权凭证查询资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '64789aa1b2c3d4e5f1b2c3d4e5f60001v1', '兑付记录查询', '兑付记录查询', 'scf:payable:redemption:query', 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '64789aa1b2c3d4e5f1b2c3d4e5f60001v1');
##GOGO

-- 应付债权凭证修改资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '789a1b2c3d4e4e5f6a1b2c3d45f60002v1', '兑付记录修改', '兑付记录修改', 'scf:payable:redemption:modify', 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '789a1b2c3d4e4e5f6a1b2c3d45f60002v1');
##GOGO

-- 应收债权凭证管理
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '37e5540ee2f24ca2b6d39ffb2cc37072v1', '8', '应收债权凭证管理', '应收债权凭证管理', '4dac33e0226249eab31cb7313382174fv1', NULL, 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '37e5540ee2f24ca2b6d39ffb2cc37072v1');
##GOGO

-- 应收债权凭证
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '5a5166c7a21645efaa2c3fcdf446bc71v1', '10', '应收债权凭证', '应收债权凭证', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 1, 1, '/sub-finance/#/electronicDebt/receiveGroup/list', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '5a5166c7a21645efaa2c3fcdf446bc71v1');
##GOGO

-- 转让记录
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '5cff354932f341ccbe9f1f6a26f78a56v1', '11', '转让记录', '转让记录', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 2, 1, '/sub-finance/#/electronicDebt/receiveGroup/transfer', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '5cff354932f341ccbe9f1f6a26f78a56v1');
##GOGO

-- 融资记录（原重复 ID，现分配新 ID）
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '5a5166c7a21645efaa2c3fcdf446bc72v1', '12', '融资记录', '融资记录', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 3, 1, '/sub-finance/#/electronicDebt/receiveGroup/loan', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '5a5166c7a21645efaa2c3fcdf446bc72v1');
##GOGO

-- 到期收款记录
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '656ac5b9fbe242eabb2bdcdd37a382c8v1', '13', '到期收款记录', '到期收款记录', '37e5540ee2f24ca2b6d39ffb2cc37072v1', NULL, 4, 1, '/sub-finance/#/electronicDebt/receiveGroup/collect', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '656ac5b9fbe242eabb2bdcdd37a382c8v1');
##GOGO

-- 指令管理
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '4c1d2c58435349e299f1c6c8548e6e55v1', '16', '指令管理', '指令管理', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 3, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '4c1d2c58435349e299f1c6c8548e6e55v1');
##GOGO

-- 应付债权指令管理
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '5600fa64f75d4cf39384d478907441bav1', '17', '应付债权指令管理', '应付债权指令管理', '4c1d2c58435349e299f1c6c8548e6e55v1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/instruction-management/payable', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '5600fa64f75d4cf39384d478907441bav1');
##GOGO

-- 查询资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a72e491cf36d428791082d756c340268', '查询资源', '查询资源', 'scf:payable:instruction:query', 1, '5600fa64f75d4cf39384d478907441bav1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a72e491cf36d428791082d756c340268');
##GOGO

-- 应用资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'b15c8367290e4742a89d63f50e71294c', '应用资源', '应用资源', 'scf:payable:instruction:apply', 1, '5600fa64f75d4cf39384d478907441bav1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'b15c8367290e4742a89d63f50e71294c');
##GOGO

-- 应收债权指令管理
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '8f5cf98f46184954b4f9c526d0bb6519v1', '18', '应收债权指令管理', '应收债权指令管理', '4c1d2c58435349e299f1c6c8548e6e55v1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/instruction-management/receivable', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '8f5cf98f46184954b4f9c526d0bb6519v1');
##GOGO

-- 查询资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'c9402d58e7314176b38510f497263da7', '查询资源', '查询资源', 'scf:receivable:instruction:query', 1, '8f5cf98f46184954b4f9c526d0bb6519v1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'c9402d58e7314176b38510f497263da7');
##GOGO

-- 应用资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'd37f61894520481195e7ac241b80563e', '应用资源', '应用资源', 'scf:receivable:instruction:apply', 1, '8f5cf98f46184954b4f9c526d0bb6519v1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'd37f61894520481195e7ac241b80563e');
##GOGO

-- 查询统计
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT 'ee4803a99b3b478ea9e331fb600c7f1av1', '16', '查询统计', '查询统计', '45ee4780ab564ff7a4117ae17bedc052v1', NULL, 4, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = 'ee4803a99b3b478ea9e331fb600c7f1av1');
##GOGO

-- 应付债权查询（原重复 ID，现分配新 ID）
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '5600fa64f75d4cf39384d478907441bbv1', '17', '应付债权查询', '应付债权查询', 'ee4803a99b3b478ea9e331fb600c7f1av1', NULL, 1, 1, '/sub-finance/#/supplyChainFinance/query-statistics/payable-voucher', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '5600fa64f75d4cf39384d478907441bbv1');
##GOGO

-- 查看资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '81195e7ad37f618945204c241b80563e', '查看资源', '查看资源', 'scf:payable:query', 1, '5600fa64f75d4cf39384d478907441bbv1', NULL, NULL, NULL, NULL, '', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '81195e7ad37f618945204c241b80563e');
##GOGO

-- 应收债权查询
INSERT IGNORE INTO tenant_pre_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON, IS_INIT, UI_VERSION)
SELECT '34d23e06d8fa4bcba42c5939b6f4ccf4v1', '18', '应收债权查询', '应收债权查询', 'ee4803a99b3b478ea9e331fb600c7f1av1', NULL, 2, 1, '/sub-finance/#/supplyChainFinance/query-statistics/receivable-voucher', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_menu WHERE ID = '34d23e06d8fa4bcba42c5939b6f4ccf4v1');
##GOGO

-- 查看资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'ad37f618945281195e704c241b80563e', '查看资源', '查看资源', 'scf:receivable:query', 1, '34d23e06d8fa4bcba42c5939b6f4ccf4v1', NULL, NULL, NULL, NULL, '', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'ad37f618945281195e704c241b80563e');
##GOGO

-- 应付债权凭证开立资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '395734ec80e2497b932154ce9d3383e6v1', '应付债权凭证开立', '应付债权凭证开立', 'scf:flow:approve:scfPayableOpened', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '395734ec80e2497b932154ce9d3383e6v1');
##GOGO

-- 应付债权凭证兑付资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '2290a7673aba492e9e2777d5d60e7725v1', '应付债权凭证兑付', '应付债权凭证兑付', 'scf:flow:approve:scfVoucherRedemption', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '2290a7673aba492e9e2777d5d60e7725v1');
##GOGO

-- 应收债权凭证开立资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'bdf5ea4f099e4458990b34195d52ad6dv1', '应收债权凭证开立审批资源', '应收债权凭证开立审批资源', 'scf:flow:approve:scfFlowReceivableVoucher', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'bdf5ea4f099e4458990b34195d52ad6dv1');
##GOGO

-- 应收债权凭证融资资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '470d8f37a2ec49b083937dbae4bcd3e2v1', '应收债权凭证融资审批资源', '应收债权凭证融资审批资源', 'scf:flow:approve:scfFlowLoan', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '470d8f37a2ec49b083937dbae4bcd3e2v1');
##GOGO

-- 应收债权凭证转让资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '625e288c60b348c294c850d14b25267fv1', '应收债权凭证转让审批资源', '应收债权凭证转让审批资源', 'scf:flow:approve:scfFlowTransfer', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '625e288c60b348c294c850d14b25267fv1');
##GOGO

-- 应收债权凭证到期收款资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT '470d8f37a2ec49b083937dbae4bcd3e2v2', '应收债权凭证到期收款审批资源', '应收债权凭证到期收款审批资源', 'scf:flow:approve:scfFlowPayment', 1, 'dd98d9dc5dd84f55a279e7679e320d5cv1', NULL, NULL, NULL, NULL, 'URL', '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = '470d8f37a2ec49b083937dbae4bcd3e2v2');
##GOGO

-- 应收债权凭证-凭证资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f61001v1', '应收债权凭证查询', '应收债权凭证查询', 'scf:rec:voucher:query', 1, '5a5166c7a21645efaa2c3fcdf446bc71v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f61001v1');
##GOGO

-- 应收债权凭证-转让记录资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f61002v1', '转让记录查询', '转让记录查询', 'scf:rec:transfer:query', 1, '5cff354932f341ccbe9f1f6a26f78a56v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f61002v1');
##GOGO

-- 应收债权凭证-融资记录资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f61003v1', '融资记录查询', '融资记录查询', 'scf:rec:loan:query', 1, '5a5166c7a21645efaa2c3fcdf446bc72v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f61003v1');
##GOGO

-- 应收债权凭证-到期收款记录资源
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f61004v1', '到期收款记录查询', '到期收款记录查询', 'scf:rec:payment:query', 1, '656ac5b9fbe242eabb2bdcdd37a382c8v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f61004v1');
##GOGO

-- 应收债权凭证-凭证修改
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f62001v1', '应收债权凭证修改', '应收债权凭证修改', 'scf:rec:voucher:update', 1, '5a5166c7a21645efaa2c3fcdf446bc71v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f62001v1');
##GOGO

-- 应收债权凭证-转让记录修改
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f62002v1', '转让记录修改', '转让记录修改', 'scf:rec:transfer:update', 1, '5cff354932f341ccbe9f1f6a26f78a56v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f62002v1');
##GOGO

-- 应收债权凭证-融资记录修改
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f62003v1', '融资记录修改', '融资记录修改', 'scf:rec:loan:update', 1, '5a5166c7a21645efaa2c3fcdf446bc72v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f62003v1');
##GOGO

-- 应收债权凭证-到期收款记录修改
INSERT IGNORE INTO tenant_pre_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL, IS_INIT, ui_version)
SELECT 'a1b2c3d4e5f64789a1b2c3d4e5f62004v1', '到期收款记录修改', '到期收款记录修改', 'scf:rec:payment:update', 1, '656ac5b9fbe242eabb2bdcdd37a382c8v1', NULL, NULL, NULL, NULL, NULL, '1', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_resources WHERE ID = 'a1b2c3d4e5f64789a1b2c3d4e5f62004v1');
##GOGO

-- 供应链金融管理（顶级分类）
INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT '5c4a3ad1e02a475fb4e5c2b2cf19d8a4', '', '供应链金融管理', 'scfKey', '1', NULL, 10, NULL, NULL, 0, 1, '45ee4780ab564ff7a4117ae17bedc052v1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '供应链金融管理', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = '5c4a3ad1e02a475fb4e5c2b2cf19d8a4');
##GOGO

-- 应付债权凭证
INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT 'ae932b51bc5349e69fcd9c13f1fb5adc', '5c4a3ad1e02a475fb4e5c2b2cf19d8a4', '应付债权凭证', 'scfPayableKey', '1', NULL, 1, NULL, NULL, 0, 1, '4dac33e0226249eab31cb7313382174fv1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '电子债权凭证', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = 'ae932b51bc5349e69fcd9c13f1fb5adc');
##GOGO

-- 应付债权凭证开立流程分类
INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT '0a7759af278a41a9ae6718785ab698d4', 'ae932b51bc5349e69fcd9c13f1fb5adc', '应付债权凭证开立流程', 'scfVoucherOpened', '1', '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/payable-approval', 1, NULL, NULL, 0, 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, 'scf:flow:approve:scfPayableOpened', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应付债权凭证开立流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = '0a7759af278a41a9ae6718785ab698d4');
##GOGO

-- 应付债权凭证兑付流程分类
INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT '88f534b57c0841dbadc508093cf83e2d', 'ae932b51bc5349e69fcd9c13f1fb5adc', '应付债权凭证兑付流程', 'scfVoucherRedemption', '1', '/sub-finance/#/supplyChainFinance/electronic-voucher/payable-voucher/voucher-redemption-approval', 1, NULL, NULL, 0, 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, 'scf:flow:approve:scfVoucherRedemption', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应付债权凭证兑付流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = '88f534b57c0841dbadc508093cf83e2d');
##GOGO

-- 付息方式
INSERT IGNORE INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '6e219f47c83045db9a72ce0158df3b9c', 'scf.interest.mode', '付息方式'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = '6e219f47c83045db9a72ce0158df3b9c');
##GOGO

INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '18c4072e956a43fb81dc790e5b326f71', '1', '卖方付息', NULL, '6e219f47c83045db9a72ce0158df3b9c', 10
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '18c4072e956a43fb81dc790e5b326f71');
##GOGO

INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '7d09c2eb563441fa98e715c03d862f4b', '2', '买方付息', NULL, '6e219f47c83045db9a72ce0158df3b9c', 20
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '7d09c2eb563441fa98e715c03d862f4b');
##GOGO

-- 应收债权凭证
INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT '8f42adb580f9435fa2fe83362e339e7e', '5c4a3ad1e02a475fb4e5c2b2cf19d8a4', '应收债权凭证', 'scfReceivableKey', '1', NULL, 2, NULL, NULL, 0, 1, '4dac33e0226249eab31cb7313382174fv1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '应收债权凭证', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = '8f42adb580f9435fa2fe83362e339e7e');
##GOGO

-- 应收债权凭证开立流程
INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT '8f9671c31de14531aa26a477475089d0', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证开立流程', 'scfFlowReceivableVoucher', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowReceivableVoucher', 1, NULL, NULL, 0, 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, 'scf:flow:approve:scfFlowReceivableVoucher', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证开立流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = '8f9671c31de14531aa26a477475089d0');
##GOGO

-- 应收债权凭证融资流程
INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT '6522dd2a73214baab5b35418ba96b23b', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证融资流程', 'scfFlowLoan', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowLoan', 2, NULL, NULL, 0, 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, 'scf:flow:approve:scfFlowLoan', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证融资流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = '6522dd2a73214baab5b35418ba96b23b');
##GOGO

-- 应收债权凭证转让流程
INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT '793a2b618a124f0ba34f2664420a1ac8', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证转让流程', 'scfFlowTransfer', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowTransfer', 3, NULL, NULL, 0, 1, '9b856832b17243b19d317d4dd2766eb9v1', NULL, 'scf:flow:approve:scfFlowTransfer', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证转让流程', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = '793a2b618a124f0ba34f2664420a1ac8');
##GOGO

-- 应收债权凭证到期收款流程
INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT 'c5537632864b4b16ba9706b7edd563e0', '8f42adb580f9435fa2fe83362e339e7e', '应收债权凭证到期收款', 'scfFlowPayment', '1', '/sub-finance/#/electronicDebt/receiveGroup/approval?type=scfFlowPayment', 4, NULL, NULL, 0, 1, '2a765b4789cc483ea87aa6f36a5d54f5v1', NULL, 'scf:flow:approve:scfFlowPayment', NULL, NULL, NULL, NULL, 'bytter-prepose-bill', '应收债权凭证到期收款', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = 'c5537632864b4b16ba9706b7edd563e0');
##GOGO

-- 增添结算类型 电子债权凭证
INSERT IGNORE INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '272a41d4004c4520ac89c9ed2aaa3fad', '3', '电子债权凭证', NULL, '402880425c0f6507015c0f7430a90008', 501
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '272a41d4004c4520ac89c9ed2aaa3fad');
##GOGO

-- 增添系统付款方式
INSERT IGNORE INTO BT_PAY_TYPE (SYS_VOUCHER_NAME, ID, RMK, SYS_VOUCHER_CODE, VALID_SIGN, TENANT_ID)
SELECT '应付债权凭证', '15c04028802f5c0ad6dc0b242e2b002f', '应付债权凭证', '51', '1', '1000'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM BT_PAY_TYPE WHERE ID = '15c04028802f5c0ad6dc0b242e2b002f');
##GOGO

INSERT IGNORE INTO BT_PAY_TYPE (SYS_VOUCHER_NAME, ID, RMK, SYS_VOUCHER_CODE, VALID_SIGN, TENANT_ID)
SELECT '应收债权凭证', '15c04028802f5c0ad6dc0b245a030032', '应收债权凭证', '52', '1', '1000'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM BT_PAY_TYPE WHERE ID = '15c04028802f5c0ad6dc0b245a030032');
##GOGO

-- 结算管理 挂接 应付应收债权凭证
INSERT IGNORE INTO tenant_pre_fc_voucher_type (ID, VOUCHER_TYPE, VOUCHER_NAME, SYS_VOUCHER_CODE, CASH_TRANS_SIGN, SETTL_TYPE, IS_ELECTRON_PAY, IS_ACCOUNT, IS_BUDGET, RMK, VALID_SIGN, IS_SYSTEM, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, SEND_MODE, BIll_SECTION_CONTROL, TENANT_ID, IS_INIT)
SELECT '99b04bd592304f72a9c14e8f60b489aa', '51', '应付债权凭证', '51', 'T', '3', '0', '0', '0', '应付债权凭证', '1', '1', null, null, null, null, '0', null, '000', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_fc_voucher_type WHERE ID = '99b04bd592304f72a9c14e8f60b489aa');
##GOGO

INSERT IGNORE INTO tenant_pre_fc_voucher_type (ID, VOUCHER_TYPE, VOUCHER_NAME, SYS_VOUCHER_CODE, CASH_TRANS_SIGN, SETTL_TYPE, IS_ELECTRON_PAY, IS_ACCOUNT, IS_BUDGET, RMK, VALID_SIGN, IS_SYSTEM, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, SEND_MODE, BIll_SECTION_CONTROL, TENANT_ID, IS_INIT)
SELECT '5441cd3ccafc491abd87cd5060531227', '52', '应收债权凭证', '52', 'T', '3', '0', '0', '0', '应收债权凭证', '1', '1', null, null, null, null, '0', null, '000', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_fc_voucher_type WHERE ID = '5441cd3ccafc491abd87cd5060531227');
##GOGO

-- ---------------------------------------------------------------------------
-- 第二部分：自定义字段菜单下线（来源 dev_20260930_mysql.sql，2026-09-30）
-- ---------------------------------------------------------------------------

-- 1. 删除角色-菜单关联（必须先于菜单删除，否则残留孤儿关联）
DELETE FROM sys_role_menu_manager
WHERE MENU_ID IN (
    SELECT ID
    FROM sys_menu_manager
    WHERE URL IN ('/enterpriseBusiness/scfCustFieldConfig/list', '/foreign/fexcustfieldrule/list')
);
COMMIT;
##GOGO

-- 2. 删除用户资源授权（必须先于资源删除）
DELETE FROM sys_corp_user_resource_manager
WHERE RESOURCE_ID IN (
    SELECT ID
    FROM sys_resources_manager
    WHERE PERMISSION IN ('scf:custfield:config:query', 'scf:custfield:config:apply')
       OR MENU_ID IN (
              SELECT ID
              FROM sys_menu_manager
              WHERE URL IN ('/enterpriseBusiness/scfCustFieldConfig/list', '/foreign/fexcustfieldrule/list')
          )
);
COMMIT;
##GOGO

-- 3. 删除菜单下的资源权限（按权限码 + 按菜单关联，兼容菜单ID未知的环境）
DELETE FROM sys_resources_manager
WHERE PERMISSION IN ('scf:custfield:config:query', 'scf:custfield:config:apply')
   OR MENU_ID IN (
          SELECT ID
          FROM sys_menu_manager
          WHERE URL IN ('/enterpriseBusiness/scfCustFieldConfig/list', '/foreign/fexcustfieldrule/list')
      );
COMMIT;
##GOGO

-- 4. 删除菜单
DELETE FROM sys_menu_manager
WHERE URL IN ('/enterpriseBusiness/scfCustFieldConfig/list', '/foreign/fexcustfieldrule/list');
COMMIT;
##GOGO


-- 5. 多语言：自定义字段维护
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c7f1a2b3d4e5467f8a9b0c1d2e3f4051', 'zh_CN', '自定义字段维护', 'common.button.custFieldMaintain'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang
    WHERE LANG_KEY = 'common.button.custFieldMaintain' AND LANG_CODE = 'zh_CN'
);
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c7f1a2b3d4e5467f8a9b0c1d2e3f4052', 'en_US', 'Custom Fields Maintenance', 'common.button.custFieldMaintain'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang
    WHERE LANG_KEY = 'common.button.custFieldMaintain' AND LANG_CODE = 'en_US'
);
COMMIT;
##GOGO

-- 支付限额拦截弹窗国际化
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bd5c6a4bd07141b585910102d73de1b7', 'zh_CN', '限额拦截详情', 'tr.limit.interceptDetail' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang
    WHERE LANG_KEY = 'tr.limit.interceptDetail' AND LANG_CODE = 'zh_CN'
);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd02164e37b9e413a91f27e98139649e6', 'en_US', 'Limit Interception Details', 'tr.limit.interceptDetail' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang
    WHERE LANG_KEY = 'tr.limit.interceptDetail' AND LANG_CODE = 'en_US'
);
##GOGO


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cf0d599af23f40e08ad27839492e45bc', 'zh_CN', '以下付款单超出支付限额，系统已自动拦截', 'tr.limit.batchInterceptMessage' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang
    WHERE LANG_KEY = 'tr.limit.batchInterceptMessage' AND LANG_CODE = 'zh_CN'
);
##GOGO


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2451ce31d79741dfbb1056c14533aec5', 'en_US', 'The following payment orders exceed the payment limit and have been automatically blocked.', 'tr.limit.batchInterceptMessage' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang
    WHERE LANG_KEY = 'tr.limit.batchInterceptMessage' AND LANG_CODE = 'en_US'
);
COMMIT;
##GOGO

UPDATE sys_menu_manager
SET ICON = 'icon-fxgl'
WHERE ID = 'd65f397e75a54a0a9de38edda520b0f4';
##GOGO


##通知分类：风险管理 → 限额管理 → 落地限额处理。按 ID 幂等插入并修正已有配置；不覆盖其他分类。
INSERT INTO sys_notification_category_m (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE, STAR_TARGET, NOTIFY_OBJECT_TYPE, EMAIL_IND)
SELECT '3394cd86d7e841d0842fd9ee5451ea85', NULL, '风险管理', NULL, NULL, NULL, NULL, NULL, 1, 500, NULL, NULL, NULL, 0, 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_notification_category_m WHERE ID = '3394cd86d7e841d0842fd9ee5451ea85');
##GOGO
UPDATE sys_notification_category_m SET PARENT_ID = NULL, NAME = '风险管理', TAG = NULL, MENU_ID = NULL, LIST_URL = NULL, DETAIL_URL = NULL, RESOURCE_CONTENT = NULL, ROLE_FILTER = 1, SORT = 500, CONTENT = NULL, TYPE = NULL, STAR_TARGET = NULL, NOTIFY_OBJECT_TYPE = 0, EMAIL_IND = 0
WHERE ID = '3394cd86d7e841d0842fd9ee5451ea85';
##GOGO

INSERT INTO sys_notification_category_m (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE, STAR_TARGET, NOTIFY_OBJECT_TYPE, EMAIL_IND)
SELECT '0793616fdd284978a310f5fb7e7113bb', '3394cd86d7e841d0842fd9ee5451ea85', '限额管理', NULL, NULL, NULL, NULL, NULL, 1, 500, NULL, NULL, NULL, 0, 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_notification_category_m WHERE ID = '0793616fdd284978a310f5fb7e7113bb');
##GOGO
UPDATE sys_notification_category_m SET PARENT_ID = '3394cd86d7e841d0842fd9ee5451ea85', NAME = '限额管理', TAG = NULL, MENU_ID = NULL, LIST_URL = NULL, DETAIL_URL = NULL, RESOURCE_CONTENT = NULL, ROLE_FILTER = 1, SORT = 500, CONTENT = NULL, TYPE = NULL, STAR_TARGET = NULL, NOTIFY_OBJECT_TYPE = 0, EMAIL_IND = 0
WHERE ID = '0793616fdd284978a310f5fb7e7113bb';
##GOGO

##待处理消息的 LIST_URL 指向专用待处理查询页；DETAIL_URL 保留单笔处理入口。
INSERT INTO sys_notification_category_m (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE, STAR_TARGET, NOTIFY_OBJECT_TYPE, EMAIL_IND)
SELECT '78b122a150004e0aa8c81ae5cd655413', '0793616fdd284978a310f5fb7e7113bb', '落地限额处理', 'paymentLimit:case:pending', '0ac75c19351840a19a53ef94cd7b35fb', '/paymentLimit/case/listPending', '/paymentLimit/case/handle', 'paymentLimit:case:apply', 1, 500, '您有一笔落地限额申请需办理，签约客户 $!{tenantName}，付款账号 $!{accountMask}，付款金额 $number.formatDouble($!{payAmount})，请处理！', 2, NULL, 0, 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_notification_category_m WHERE ID = '78b122a150004e0aa8c81ae5cd655413');
##GOGO
UPDATE sys_notification_category_m SET PARENT_ID = '0793616fdd284978a310f5fb7e7113bb', NAME = '落地限额处理', TAG = 'paymentLimit:case:pending', MENU_ID = '0ac75c19351840a19a53ef94cd7b35fb', LIST_URL = '/paymentLimit/case/listPending', DETAIL_URL = '/paymentLimit/case/handle', RESOURCE_CONTENT = 'paymentLimit:case:apply', ROLE_FILTER = 1, SORT = 500, CONTENT = '您有一笔落地限额申请需办理，签约客户 $!{tenantName}，付款账号 $!{accountMask}，付款金额 $number.formatDouble($!{payAmount})，请处理！', TYPE = 2, STAR_TARGET = NULL, NOTIFY_OBJECT_TYPE = 0, EMAIL_IND = 0
WHERE ID = '78b122a150004e0aa8c81ae5cd655413';
##GOGO

INSERT IGNORE INTO tenant_pre_act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY, UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME, IS_INIT)
SELECT '87200abbd1aa40cfb1d04becc2a39c96', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账号注销办理', 'btbankaccdelhandle', '1', '/bankacc/btbankaccdelhandle/listApprove', 110, '/bankacc/btbankaccdelhandle/mobileApprove', 'c6a59de4eb974b34853776f321828021', 0, 1, '402880265c9b0796015c9b08bf7d0000', NULL, NULL, NULL, NULL, NULL, NULL, 'bytter-service-payment', '银行账号注销办理', '1'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM tenant_pre_act_extend_category WHERE ID = '87200abbd1aa40cfb1d04becc2a39c96');
##GOGO


insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'bfadbe2c67e343b391def89467fc76fa', 'zh_CN', '授权菜单资源', 'replenish.AuthMenu' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.AuthMenu' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c38e2b438d9f43f6a58b5aa31954c3fb', 'en_US', 'Authorized menu resources', 'replenish.AuthMenu' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.AuthMenu' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0e3cabd27ab845388107155f63bd12f1', 'zh_CN', '新增角色', 'replenish.AddRole' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.AddRole' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '10859312099b44c7a0147c0d144c639a', 'en_US', 'Add role', 'replenish.AddRole' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.AddRole' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '20bc6cf82fab46bcb65ba364d3b49869', 'en_US', 'Role unchanged', 'replenish.RoleUnchanged' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.RoleUnchanged' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0509422587af49d2b906d115a6a088b3', 'zh_CN', '未发生改变角色', 'replenish.RoleUnchanged' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.RoleUnchanged' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2f556c9e948148e1948a30c585858929', 'zh_CN', '删除角色', 'replenish.DeleteRole' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.DeleteRole' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '627d2bc04e94446387c605859d7c8805', 'en_US', 'Delete role', 'replenish.DeleteRole' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='replenish.DeleteRole' AND LANG_CODE = 'en_US' limit 1);
##GOGO


