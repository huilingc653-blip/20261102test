##修改说明：票据/资金收付相关字典及国际化数据（sys_dict、sys_multi_lang），与 oracle/bytter-service-bill_dml.sql 对应
##约定：可重复执行（INSERT ... SELECT ... FROM DUAL WHERE NOT EXISTS）
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '44a64316402d47219a02ff738c479549', '6120', '兑付确认', NULL, '5158a781a4074d048e2dc738a1b498b3', 14, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '44a64316402d47219a02ff738c479549' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '45a64316402d47219a02ff738c479549', '6101', '开票补录', NULL, '5158a781a4074d048e2dc738a1b498b3', 14, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '45a64316402d47219a02ff738c479549' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT '46a64316402d47219a02ff738c479549', '9004', '票据删除', NULL, '5158a781a4074d048e2dc738a1b498b3', 14, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '46a64316402d47219a02ff738c479549' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT 'mg9iu1f5e1ce442fa9052ed590f58df0', '9019', '托收', NULL, '42iu50cb3d2e421c9e4a7343fe5f38bc', 14, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'mg9iu1f5e1ce442fa9052ed590f58df0' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT 'ma9iu1f5e1ce442fa9052ed590f58df0', '6019', '托收', NULL, '42iu50cb3d2e421c9e4a7343fe5f38bc', 14, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'ma9iu1f5e1ce442fa9052ed590f58df0' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
SELECT 'md9iu1f5e1ce442fa9052ed590f58df0', '018', '托收', NULL, '42iu50cb3d2e421c9e4a7343fe5f38bc', 14, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'md9iu1f5e1ce442fa9052ed590f58df0' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'vv47e6de84c64b6384c2bf047cf38904', 'zh_CN', '是否同意', 'table.endorseResponse.isAgree' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.endorseResponse.isAgree' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'abda8b9c0d1e2f3a4b5c6d7e8f9056rt', 'zh_CN', '下拨状态', 'nis.payment.allocation.status' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'nis.payment.allocation.status' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'abd28b9c0d1e2f3a4b5c6d7e8f9056rt', 'en_US', 'Allocation Status', 'nis.payment.allocation.status' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'nis.payment.allocation.status' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'asba8b9c0d1e2f3a4b5c6d7e8f9056rt', 'zh_CN', '下拨需求清单编码', 'nis.payment.downAllocation.requireListCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'nis.payment.downAllocation.requireListCode' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'asb28b9c0d1e2f3a4b5c6d7e8f9056rt', 'en_US', 'Allocation Status', 'nis.payment.downAllocation.requireListCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'nis.payment.downAllocation.requireListCode' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

-- 7. nis.contract.multiConTypeSelect en_US
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'asb781c20d1e2f3a4b5c6d7e8f901117', 'en_US', 'Please select the corresponding contract when calling the contract', 'nis.contract.multiConTypeSelect'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'nis.contract.multiConTypeSelect' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO
-- 8. nis.contract.multiConTypeSelect zh_CN
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'asb781c20d1e2f3a4b5c6d7e8f901118', 'zh_CN', '调用合同请选择对应合同', 'nis.contract.multiConTypeSelect'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'nis.contract.multiConTypeSelect' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
