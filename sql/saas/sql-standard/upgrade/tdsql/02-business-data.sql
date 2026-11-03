

##流程分类（风险管理）：对齐菜单树；叶子 KEY_ 必须与 PaymentLimitWorkFlowConstant.PAYMENT_LIMIT_CASE=paymentLimitCheck 一致
##实体表 BT_M_BPM_FLOW_CATEGORY；若库中实际表名为 bt_manager_bpm_flow_category（签约脚本写法），把下方表名替换即可
##树：风险管理 → 限额管理 → 限额落地处理（审批列表 /paymentLimit/case/listApprove）

##流程分类-风险管理（一级）
INSERT INTO bt_m_bpm_flow_category (ID, PARENT_ID, NAME, KEY_, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKABLE, MENU_ID, MOBILE_APPROVAL_FLAG, RESOURCE_CONTENT)
SELECT '206a90c8276bb243bdfc05adb68e36e2', NULL, '风险管理', 'riskManage', '1', NULL, 1, NULL, NULL, '0', NULL, NULL, NULL, NULL
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM bt_m_bpm_flow_category WHERE ID = '206a90c8276bb243bdfc05adb68e36e2' LIMIT 1)
  AND NOT EXISTS (SELECT 1 FROM bt_m_bpm_flow_category WHERE KEY_ = 'riskManage' LIMIT 1);
##GOGO

##流程分类-限额管理（二级）
INSERT INTO bt_m_bpm_flow_category (ID, PARENT_ID, NAME, KEY_, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKABLE, MENU_ID, MOBILE_APPROVAL_FLAG, RESOURCE_CONTENT)
SELECT 'd70d2862ece7f42ef034ee9f69c04a9a', '206a90c8276bb243bdfc05adb68e36e2', '限额管理', 'limitManage', '1', NULL, 1, NULL, NULL, '0', NULL, NULL, NULL, NULL
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM bt_m_bpm_flow_category WHERE ID = 'd70d2862ece7f42ef034ee9f69c04a9a' LIMIT 1)
  AND NOT EXISTS (SELECT 1 FROM bt_m_bpm_flow_category WHERE KEY_ = 'limitManage' LIMIT 1);
##GOGO

##流程分类-限额落地处理（叶子：挂审批流 KEY_=paymentLimitCheck）
INSERT INTO bt_m_bpm_flow_category (ID, PARENT_ID, NAME, KEY_, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKABLE, MENU_ID, MOBILE_APPROVAL_FLAG, RESOURCE_CONTENT)
SELECT '28122cecd087b5cf17abaacd99cffb1c', 'd70d2862ece7f42ef034ee9f69c04a9a', '限额落地处理', 'paymentLimitCheck', '1',
       '/paymentLimit/case/listApprove', 1, NULL, NULL, '0', NULL, '0ac75c19351840a19a53ef94cd7b35fb', NULL, NULL
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM bt_m_bpm_flow_category WHERE ID = '28122cecd087b5cf17abaacd99cffb1c' LIMIT 1)
  AND NOT EXISTS (SELECT 1 FROM bt_m_bpm_flow_category WHERE KEY_ = 'paymentLimitCheck' LIMIT 1);
##GOGO


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '20d85756898215344dfab15b65a7db3a', 'zh_CN', '批量处理仅支持待处理的单据', 'tr.limit.onlyPendingBatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.onlyPendingBatch' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
UPDATE sys_multi_lang SET LANG_CONTEXT = '批量处理仅支持待处理的单据' WHERE LANG_KEY = 'tr.limit.onlyPendingBatch' AND LANG_CODE = 'zh_CN';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1e29017408dd56f10cfafcbdb1164182', 'en_US', 'Batch only supports pending cases', 'tr.limit.onlyPendingBatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.onlyPendingBatch' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO
UPDATE sys_multi_lang SET LANG_CONTEXT = 'Batch only supports pending cases' WHERE LANG_KEY = 'tr.limit.onlyPendingBatch' AND LANG_CODE = 'en_US';
##GOGO

##修改说明：限额规则管理 / 限额落地处理 菜单资源权限（sys_resources_manager）
##菜单：限额规则管理 222d2e78c94a41e2819a602ccf1d51e5；限额落地处理 0ac75c19351840a19a53ef94cd7b35fb

##【限额规则管理】查询资源
INSERT INTO sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'dd54805ee8d668873a7634ead0ed4671', '查询资源', '查询资源', 'paymentLimit:rule:query', 1, '222d2e78c94a41e2819a602ccf1d51e5' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID = 'dd54805ee8d668873a7634ead0ed4671' LIMIT 1
);
##GOGO

##【限额规则管理】应用资源
INSERT INTO sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '30d42150e39b0eaf8bf0bd9fcfb52914', '应用资源', '应用资源', 'paymentLimit:rule:apply', 1, '222d2e78c94a41e2819a602ccf1d51e5' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID = '30d42150e39b0eaf8bf0bd9fcfb52914' LIMIT 1
);
##GOGO

##【限额落地处理】查询资源
INSERT INTO sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '345898f2bb73f38853235b567a971e84', '查询资源', '查询资源', 'paymentLimit:case:query', 1, '0ac75c19351840a19a53ef94cd7b35fb' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID = '345898f2bb73f38853235b567a971e84' LIMIT 1
);
##GOGO

##【限额落地处理】应用资源
INSERT INTO sys_resources_manager (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'abd2f09a015fdce47fab8c8e4f08dced', '应用资源', '应用资源', 'paymentLimit:case:apply', 1, '0ac75c19351840a19a53ef94cd7b35fb' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_resources_manager WHERE ID = 'abd2f09a015fdce47fab8c8e4f08dced' LIMIT 1
);
##GOGO

-- 损益管理服务端校验提示
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8c21c862d47a4b76b3dc443c99839b01', 'zh_CN', '必填项未填写，请检查损益币种、参考汇率和损益金额！', 'fex.profitLoss.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'fex.profitLoss.required' AND LANG_CODE = 'zh_CN');
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8c21c862d47a4b76b3dc443c99839b02', 'en_US', 'Required fields are missing. Check the profit/loss currency, reference rate, and profit/loss amounts.', 'fex.profitLoss.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'fex.profitLoss.required' AND LANG_CODE = 'en_US');
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8c21c862d47a4b76b3dc443c99839b03', 'zh_CN', '损益币种必须为交易货币对中的币种！', 'fex.profitLoss.cur.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'fex.profitLoss.cur.invalid' AND LANG_CODE = 'zh_CN');
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8c21c862d47a4b76b3dc443c99839b04', 'en_US', 'The profit/loss currency must be one of the currencies in the transaction pair.', 'fex.profitLoss.cur.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'fex.profitLoss.cur.invalid' AND LANG_CODE = 'en_US');
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8c21c862d47a4b76b3dc443c99839b05', 'zh_CN', '交易已有生命周期处理，损益币种不可修改！', 'fex.profitLoss.cur.locked' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'fex.profitLoss.cur.locked' AND LANG_CODE = 'zh_CN');
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8c21c862d47a4b76b3dc443c99839b06', 'en_US', 'The profit/loss currency cannot be changed after lifecycle processing has started.', 'fex.profitLoss.cur.locked' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'fex.profitLoss.cur.locked' AND LANG_CODE = 'en_US');
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8c21c862d47a4b76b3dc443c99839b07', 'zh_CN', '损益币种与单位本位币不一致时，折本位币汇率不能为空！', 'fex.profitLoss.convertRate.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'fex.profitLoss.convertRate.required' AND LANG_CODE = 'zh_CN');
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8c21c862d47a4b76b3dc443c99839b08', 'en_US', 'The base-currency conversion rate is required when the profit/loss currency differs from the corporation base currency.', 'fex.profitLoss.convertRate.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'fex.profitLoss.convertRate.required' AND LANG_CODE = 'en_US');
##GOGO

-- 交易占用前累计快照：用于识别首次将周期累计推过限额的交易
-- TDSQL 不支持存储过程，字段已并入基线建表脚本：sql/saas/sql-standard/tdsql/06-business-schema.sql
--   -> BT_TR_LIMIT_TXN_BUCKET.BEFORE_OCCUPIED_AMOUNT
--   -> BT_TR_LIMIT_TXN_BUCKET.BEFORE_OCCUPIED_COUNT
##GOGO


UPDATE tenant_pre_menu
SET URL = '/sub-finance/#/bank-loan/repay-supplement'
WHERE ID = '96216f1e906b4d47abb9cb7b32f2fa4ev1';
##GOGO


##公告信息（企业端 bytter-system）附件必填校验提示
##对应改动：views/sys/article/form.vm 提交/暂存前校验附件是否已上传
INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f93c1a2b3d4e4f5a6b7c8d9e0f1a2b3c', 'zh_CN', '请上传附件！', 'sys.article.alert.attachmentEmpty' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'sys.article.alert.attachmentEmpty' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
UPDATE sys_multi_lang SET LANG_CONTEXT = '请上传附件！' WHERE LANG_KEY = 'sys.article.alert.attachmentEmpty' AND LANG_CODE = 'zh_CN';
##GOGO

INSERT IGNORE INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f93c2b3c4d5e4f6a7b8c9d0e1f2a3b4c', 'en_US', 'Please upload the attachment!', 'sys.article.alert.attachmentEmpty' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'sys.article.alert.attachmentEmpty' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO
UPDATE sys_multi_lang SET LANG_CONTEXT = 'Please upload the attachment!' WHERE LANG_KEY = 'sys.article.alert.attachmentEmpty' AND LANG_CODE = 'en_US';
##GOGO

UPDATE sys_country SET NAME_ZH = '中国香港' WHERE CODE = '344' AND NAME_ZH = '香港';
##GOGO

UPDATE sys_country SET NAME_ZH = '中国台湾' WHERE CODE = '158' AND NAME_ZH = '台湾';
##GOGO


##删除即远掉菜单,目前已有新的外汇即远掉产品外汇菜单
-- 第一段：业务库，覆盖当前库所有租户，并清理关联记录。
DELETE FROM sys_permission WHERE RESOURCES_ID IN (
    SELECT r.ID FROM sys_resources r JOIN sys_menu m ON m.ID = r.MENU_ID
    WHERE m.URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
);
##GOGO
DELETE FROM sys_corp_user_resource WHERE MENU_ID IN (
    SELECT ID FROM sys_menu WHERE URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
) OR RESOURCE_ID IN (
    SELECT r.ID FROM sys_resources r JOIN sys_menu m ON m.ID = r.MENU_ID
    WHERE m.URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
);
##GOGO
DELETE FROM sys_role_menu WHERE MENU_ID IN (
    SELECT ID FROM sys_menu WHERE URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
);
##GOGO
DELETE FROM sys_module_menu WHERE MENU_ID IN (
    SELECT ID FROM sys_menu WHERE URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
);
##GOGO
DELETE FROM sys_menu_collect WHERE MENU_ID IN (
    SELECT ID FROM sys_menu WHERE URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
) OR URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9');
##GOGO
DELETE FROM sys_internation_base WHERE KEY_ID IN (
    SELECT ID FROM sys_menu WHERE URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
) OR KEY_ID IN (
    SELECT r.ID FROM sys_resources r JOIN sys_menu m ON m.ID = r.MENU_ID
    WHERE m.URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
);
##GOGO
DELETE FROM sys_resources WHERE MENU_ID IN (
    SELECT ID FROM sys_menu WHERE URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
);
##GOGO
DELETE FROM sys_menu WHERE URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9');
##GOGO
-- 第二段：管理库，清理新租户初始化使用的菜单模板。
DELETE FROM tenant_pre_resources WHERE MENU_ID IN (
    SELECT ID FROM tenant_pre_menu WHERE URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9')
);
##GOGO
DELETE FROM tenant_pre_menu WHERE URL IN ('/foreign/fexproduct/list/7', '/foreign/fexproduct/list/8', '/foreign/fexproduct/list/9');

UPDATE tenant_pre_config
SET VALUE = '2'
WHERE ID = '1a485cea8b7a40dba7bbc2fb776deb75';
##GOGO
