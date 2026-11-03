##修改说明：限额规则管理国际化（sys_multi_lang）
##约定：可重复执行（INSERT ... SELECT ... WHERE NOT EXISTS）
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e669dd4e40f18b347cb89e226b9e7342', 'zh_CN', '机构', 'tr.limit.corp' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.corp' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8a3071c5b00c7d0b590d023c0263b8f4', 'en_US', 'Institution', 'tr.limit.corp' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.corp' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '25f9d56abfd2fc003e08683f6df28d73', 'zh_CN', '签约客户', 'tr.limit.signedCust' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.signedCust' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4ef081b7c5cec23770b72cb924418625', 'en_US', 'Signed Customer', 'tr.limit.signedCust' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.signedCust' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7542f4fee6874c5226c0537461cb9d42', 'zh_CN', '单位', 'tr.limit.unit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unit' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a7b9e2739ab9baf43423f906b8fa6a6a', 'en_US', 'Unit', 'tr.limit.unit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unit' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '61bd223b3763a282f329ca66f2263f7d', 'zh_CN', '账号', 'tr.limit.bankAcc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.bankAcc' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4716f47d7b3c0d2d85a012ecde6c8c37', 'en_US', 'Account No.', 'tr.limit.bankAcc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.bankAcc' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '35c8b91e9af2e745e93fb825b1044e6e', 'zh_CN', '限额规则', 'tr.limit.rule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.rule' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c8fd19d7a21fc964f445f9c8dea03aa9', 'en_US', 'Limit Rule', 'tr.limit.rule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.rule' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '19000a9e6143d5224676afb472dd5fbb', 'zh_CN', '启用标志', 'tr.limit.enableFlag' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.enableFlag' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3d5ba5bf1a966de866d14942c876fd57', 'en_US', 'Enable Flag', 'tr.limit.enableFlag' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.enableFlag' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '18e9fd16759445bbdfa3d190e6f49759', 'zh_CN', '管控渠道', 'tr.limit.channel' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.channel' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ade184cfcef81b727318f0d4c7a91bb3', 'en_US', 'Control Channel', 'tr.limit.channel' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.channel' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a8504ed715d988ca7f3052f80b7ad74b', 'zh_CN', '规则编码', 'tr.limit.ruleCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.ruleCode' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '094eb2f1e41315ebe5c40eb4f37b7371', 'en_US', 'Rule Code', 'tr.limit.ruleCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.ruleCode' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'dd504587ca8de88fb3d44297abe2dd96', 'zh_CN', '规则名称', 'tr.limit.ruleName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.ruleName' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b4bdf3b6ad6d5b25cdbddaf2899f1720', 'en_US', 'Rule Name', 'tr.limit.ruleName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.ruleName' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fede044e2aff964e369e9316b33ad36b', 'zh_CN', '支付限额摘要', 'tr.limit.paySummary' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.paySummary' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c5d06d277b3bc991a90637fc6b70865b', 'en_US', 'Payment Limit Summary', 'tr.limit.paySummary' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.paySummary' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '86705cd5b942a8b2fb4ed14676e7d02e', 'zh_CN', '落地限额摘要', 'tr.limit.landSummary' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.landSummary' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e352d548f0cd5e3f9eedaaec7bf39dad', 'en_US', 'Landing Limit Summary', 'tr.limit.landSummary' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.landSummary' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '76cb2757cd1118d38b58997217c37ffd', 'zh_CN', '创建人', 'tr.limit.createBy' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.createBy' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c8eedd39d354290c6e8a58a45480a2bf', 'en_US', 'Creator', 'tr.limit.createBy' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.createBy' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '53f418606f3bed1f690b0f1ba2f6c306', 'zh_CN', '创建时间', 'tr.limit.createTime' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.createTime' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '099f0a25ad9c72f5b5acff82a2f12348', 'en_US', 'Created Time', 'tr.limit.createTime' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.createTime' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6e8278a6ce3bb07d8cdf3c0ac7ac664a', 'zh_CN', '请选择机构', 'tr.limit.selectCorp' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectCorp' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7ca4f6055576f934cdab6afd6b5ce8c0', 'en_US', 'Please select institution', 'tr.limit.selectCorp' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectCorp' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cf97387558abb8aeaa66f3eaba18d207', 'zh_CN', '请选择签约客户', 'tr.limit.selectCust' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectCust' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '58fb17ad86320c576aaff7633430295f', 'en_US', 'Please select signed customer', 'tr.limit.selectCust' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectCust' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '75f5f4747da6d71386d9efa5c07d1ccf', 'zh_CN', '单位选择', 'tr.limit.selectUnit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectUnit' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '95a217e9f66b4470e8ba3d0f65fd4a15', 'en_US', 'Select Unit', 'tr.limit.selectUnit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectUnit' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3ddaa93ab262d9dfcad5344c2c925b46', 'zh_CN', '账号选择', 'tr.limit.selectBankAcc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectBankAcc' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e13eeb443f281d957ff0fbbe270e9dc6', 'en_US', 'Select Account', 'tr.limit.selectBankAcc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectBankAcc' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a64c163c7748bb9d15f7f81c756351d0', 'zh_CN', '规则编码或名称', 'tr.limit.ruleCodeOrName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.ruleCodeOrName' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd6f7640d66170921f5cd2f53c2d0f2b1', 'en_US', 'Rule code or name', 'tr.limit.ruleCodeOrName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.ruleCodeOrName' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '92340995e4f5b2cbed7519cdf5cc2bbb', 'zh_CN', '请选择', 'tr.limit.pleaseSelect' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.pleaseSelect' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8ffc36d69a0280dc90332650ae2edff8', 'en_US', 'Please select', 'tr.limit.pleaseSelect' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.pleaseSelect' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '923785beacc7e1b3f05a2c8ea8d61df8', 'zh_CN', '限额基础信息', 'tr.limit.basicInfo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.basicInfo' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '20cdc37295737c03bd6adbba7388382c', 'en_US', 'Limit Basic Info', 'tr.limit.basicInfo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.basicInfo' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7c00920b15f3ddfe68cbf5f57c8e1504', 'zh_CN', '限制维度', 'tr.limit.limitDim' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitDim' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b3afff4c333e962bd5803699043c98c0', 'en_US', 'Limit Dimension', 'tr.limit.limitDim' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitDim' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c5018eb03c90dd514c9b822ee2ed47fa', 'zh_CN', '银企直联', 'tr.limit.channelDirect' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.channelDirect' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6d99644f77812bd2ef898e21f8fe96da', 'en_US', 'Bank-Enterprise Direct', 'tr.limit.channelDirect' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.channelDirect' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e23558042444a562fb323e8c064c19e5', 'zh_CN', '单位范围', 'tr.limit.unitScope' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unitScope' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b44c12de6e8baffe3d3e5c18b60437', 'en_US', 'Unit Scope', 'tr.limit.unitScope' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unitScope' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6897f5b02152f3dffcffb007fb8fea1d', 'zh_CN', '账号范围', 'tr.limit.accScope' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.accScope' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd74c54815a1c4c4dbf015aecaa966eb1', 'en_US', 'Account Scope', 'tr.limit.accScope' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.accScope' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5e570dceea525384cb2cf42f5dab2e12', 'zh_CN', '全部单位', 'tr.limit.allUnit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.allUnit' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '39179a4a64a86327d174c56f96a8f14c', 'en_US', 'All Units', 'tr.limit.allUnit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.allUnit' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a3a12f6982166c17bfc9267c23e7a5cd', 'zh_CN', '全部账号', 'tr.limit.allAcc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.allAcc' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f0c275bf5d5c33ccfac49d13ca8452fc', 'en_US', 'All Accounts', 'tr.limit.allAcc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.allAcc' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd7dbd9c52d56a2562d67b7d2ab6849e5', 'zh_CN', '自定义', 'tr.limit.custom' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.custom' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd00a5d88a544324d813509b8292021c6', 'en_US', 'Custom', 'tr.limit.custom' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.custom' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '18de3756891db6bd12d7f3cc95ec1df8', 'zh_CN', '币别', 'tr.limit.currency' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.currency' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0f4f147888cbe9912a202ce20d1cf4ae', 'en_US', 'Currency', 'tr.limit.currency' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.currency' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b3cf89115adacda85fdddbf5d1c6d5a3', 'zh_CN', '备注', 'tr.limit.remark' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.remark' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '13bac159103d0b82b92f44183f2c1b98', 'en_US', 'Remark', 'tr.limit.remark' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.remark' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0be4b77acb4f286f7682410460a834a3', 'zh_CN', '限额类型', 'tr.limit.limitType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitType' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b5c204d2e4212ad7686a405f78effdaa', 'en_US', 'Limit Type', 'tr.limit.limitType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitType' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9cb1bd3e51eb81d47e603e3ed32cd48a', 'zh_CN', '支付限额', 'tr.limit.payLimit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payLimit' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '26f74a4070391938e731815f595d863d', 'en_US', 'Payment Limit', 'tr.limit.payLimit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payLimit' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '37115f673229f1fd4c835f908b0b0b24', 'zh_CN', '落地限额', 'tr.limit.landLimit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.landLimit' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd1551413896c34ac289a94426625ea21', 'en_US', 'Landing Limit', 'tr.limit.landLimit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.landLimit' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '32e18455cb19f95639df3f7e26840ca7', 'zh_CN', '(超额拦截)', 'tr.limit.payHint' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payHint' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b752c796928c9e26b88cf8343512d08e', 'en_US', '(Block if exceeded)', 'tr.limit.payHint' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payHint' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '01123298033d3d7a2e3537930833b2c2', 'zh_CN', '(超额银行落地审批)', 'tr.limit.landHint' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.landHint' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '724be99eff35e025272c359ca27f3e6d', 'en_US', '(Bank landing approval if exceeded)', 'tr.limit.landHint' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.landHint' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '100e2469589556a72719368184d6a24e', 'zh_CN', '支付限额条件', 'tr.limit.payCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payCond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9aae6a87ccbea5449f23fd922ad88a3b', 'en_US', 'Payment Limit Conditions', 'tr.limit.payCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payCond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '038f461c8abda03be2f4a2804de60ef4', 'zh_CN', '落地限额条件', 'tr.limit.landCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.landCond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bf237c5e59dc5e6031512259dd10c7b3', 'en_US', 'Landing Limit Conditions', 'tr.limit.landCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.landCond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fb70f401fd45f0cd1c179c52cec09969', 'zh_CN', '+ 添加条件', 'tr.limit.addCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.addCond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '29753f16aa0772ffdaa545ff7a03b111', 'en_US', '+ Add Condition', 'tr.limit.addCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.addCond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c988967ea49da6ea3ae6a0073ab28686', 'zh_CN', '限定方式', 'tr.limit.limitMode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitMode' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8a83db897d33d3b729d6a0916302eca4', 'en_US', 'Limit Mode', 'tr.limit.limitMode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitMode' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1cec56e2ff0a755ef896d41b9cee19d2', 'zh_CN', '限定值', 'tr.limit.limitValue' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitValue' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '96e0c8a7284f36421c7f6face4bc8966', 'en_US', 'Limit Value', 'tr.limit.limitValue' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitValue' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '62e7f2265f945a9f82e36f802b110947', 'zh_CN', '删除', 'tr.limit.delete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.delete' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3e21178940354c24ab94e68eb3a4c0ad', 'en_US', 'Delete', 'tr.limit.delete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.delete' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '817142a868ccd155fd2963ad74f434ae', 'zh_CN', '范围模式', 'tr.limit.scopeMode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.scopeMode' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '992c2484d4f15d22187115adec8bb740', 'en_US', 'Scope Mode', 'tr.limit.scopeMode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.scopeMode' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0dfb6503f242ff1b74abae037e32fbc3', 'zh_CN', '全部', 'tr.limit.all' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.all' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'edbacdf3bce7963f9ff9c9c3d2ad50d9', 'en_US', 'All', 'tr.limit.all' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.all' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cbd087cd4d9969e8afd2a14cc614da78', 'zh_CN', '单位编码', 'tr.limit.unitCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unitCode' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8e398b6acda4a0b9279ed16804584a32', 'en_US', 'Unit Code', 'tr.limit.unitCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unitCode' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a18f8f5caacb0cf85bf1cf5cb0f24a2c', 'zh_CN', '单位名称', 'tr.limit.unitName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unitName' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '29faff42a4b44425169988d64dac4bfc', 'en_US', 'Unit Name', 'tr.limit.unitName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unitName' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9595c96fca2db0d7007c2d3f9cf7db36', 'zh_CN', '账户名称', 'tr.limit.accName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.accName' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e8b61531bc76fa2cf3754be589a7374a', 'en_US', 'Account Name', 'tr.limit.accName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.accName' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7c683b22871ca846906594958cc30086', 'zh_CN', '开户行', 'tr.limit.bankName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.bankName' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '68542beec41c7679ed1c7fef9db518cd', 'en_US', 'Bank Name', 'tr.limit.bankName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.bankName' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '30a59f25c4dfe05b2b36a65a41081652', 'zh_CN', '暂无数据', 'tr.limit.noData' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noData' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9508be801db9ed746d4c69d0fced6f74', 'en_US', 'No data', 'tr.limit.noData' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noData' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd90600109173b8f9486b301fe57f2353', 'zh_CN', '暂无条件', 'tr.limit.noCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noCond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0229469722b9ebd4da4dc498fe4e6c58', 'en_US', 'No conditions', 'tr.limit.noCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noCond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e47e98a1b0e4651a36510d25f024a7ba', 'zh_CN', '限额条件', 'tr.limit.cond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.cond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '00dad7969d05aa1aee986d96251dc443', 'en_US', 'Limit Conditions', 'tr.limit.cond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.cond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5f7036ae8afc38317b531f6521d2ba6d', 'zh_CN', '银行账户', 'tr.limit.bankAccount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.bankAccount' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '46486beea1f5676ddc4814dea94e0e82', 'en_US', 'Bank Account', 'tr.limit.bankAccount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.bankAccount' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ca6a20bfba7b5d90915077c0f8341d98', 'zh_CN', '启用', 'tr.limit.enable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.enable' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'df776bc8da7caa7bd509491de69e099d', 'en_US', 'Enable', 'tr.limit.enable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.enable' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1e2f01b902c8b021745f244b270ec11', 'zh_CN', '禁用', 'tr.limit.disable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.disable' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b12a0e57432b297d22aef63830699f9c', 'en_US', 'Disable', 'tr.limit.disable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.disable' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ac2bce2a9bf66844bbf5da7fba18b47d', 'zh_CN', '已选：', 'tr.limit.selected' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selected' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bb0eb10f77680920c1b764efc9193757', 'en_US', 'Selected: ', 'tr.limit.selected' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selected' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '29f786fe4332c2ef3c0f09cbdb4639fd', 'zh_CN', '新增限额规则', 'tr.limit.addRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.addRule' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8cc08383a238d1c0c785e62d6cc04380', 'en_US', 'Add Limit Rule', 'tr.limit.addRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.addRule' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '09de73a25c308b00e5b2282c4de8e030', 'zh_CN', '查看限额规则', 'tr.limit.viewRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.viewRule' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '92ed554620085efd754ed0e3be7e65e0', 'en_US', 'View Limit Rule', 'tr.limit.viewRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.viewRule' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7faed70ef2ae4482b800f07b4c314c57', 'zh_CN', '编辑限额规则', 'tr.limit.editRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.editRule' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3f96305599254157b7a9ba40046816b5', 'en_US', 'Edit Limit Rule', 'tr.limit.editRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.editRule' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ed51c5a7a1dc9c69493bbe61a9a0256e', 'zh_CN', '确认启用该限额规则？', 'tr.limit.confirmEnable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.confirmEnable' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3c44fa83d72882c1ad61c4935ef71759', 'en_US', 'Enable this limit rule?', 'tr.limit.confirmEnable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.confirmEnable' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4696e136ddf78a71a4d3630df39b22ca', 'zh_CN', '确认禁用该限额规则？', 'tr.limit.confirmDisable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.confirmDisable' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '30f364a44dd5cbd7a5827b86cc4c5dd5', 'en_US', 'Disable this limit rule?', 'tr.limit.confirmDisable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.confirmDisable' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f6fff35b08e6cd6aab64ebab79cf6717', 'zh_CN', '至少保留一条条件', 'tr.limit.keepOneCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.keepOneCond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e6cd51af96ca51b096156ee3ccaee588', 'en_US', 'Keep at least one condition', 'tr.limit.keepOneCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.keepOneCond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '02002f84273079e72e2cdcd971b0e219', 'zh_CN', '请先选择机构', 'tr.limit.selectCorpFirst' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectCorpFirst' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '41f1e0b8af91a01864e6a2cb2c14927c', 'en_US', 'Please select institution first', 'tr.limit.selectCorpFirst' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectCorpFirst' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c8af22b19bd0235be8d909173decd6b8', 'zh_CN', '请先选择签约客户', 'tr.limit.selectCustFirst' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectCustFirst' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f18c6b78e4bb16ea6accf70f998a6570', 'en_US', 'Please select signed customer first', 'tr.limit.selectCustFirst' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectCustFirst' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fe9db78336ede13bad46a5b2c4348d10', 'zh_CN', '请至少选择一种限额类型', 'tr.limit.selectLimitType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectLimitType' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8bc27d311d14817c97ad9ca25632bece', 'en_US', 'Select at least one limit type', 'tr.limit.selectLimitType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectLimitType' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2bff75deeea4e590b322fb237e0f7f0a', 'zh_CN', '请选择单位', 'tr.limit.selectUnitRequired' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectUnitRequired' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '954f23d17a132458077d49e804bb1842', 'en_US', 'Please select unit', 'tr.limit.selectUnitRequired' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectUnitRequired' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e02c9eacf23a5c71d7a342f69979de66', 'zh_CN', '请选择账号', 'tr.limit.selectAccRequired' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectAccRequired' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8d987609c776a33660d44f5207ee77b8', 'en_US', 'Please select account', 'tr.limit.selectAccRequired' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectAccRequired' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '393440b42e72b2e0db3c7180c24addd8', 'zh_CN', '请至少选择一个单位', 'tr.limit.selectOneUnit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectOneUnit' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'faf56470a81d8a1dd6b867c853b0bb3e', 'en_US', 'Select at least one unit', 'tr.limit.selectOneUnit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectOneUnit' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'dcba5848f45b8045c4060ee687de2f5b', 'zh_CN', '请至少选择一个账号', 'tr.limit.selectOneAcc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectOneAcc' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4d40f4924f6f1f5dc081eb6e69169c96', 'en_US', 'Select at least one account', 'tr.limit.selectOneAcc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectOneAcc' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cd7a5976796ac7d6d704bf0ae575b45b', 'zh_CN', '请至少添加一条支付限额条件', 'tr.limit.addPayCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.addPayCond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '71e98ed90e9d49e1232aa11ead113826', 'en_US', 'Add at least one payment condition', 'tr.limit.addPayCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.addPayCond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5ad64ae677ee21737d7e75c19b95d354', 'zh_CN', '请至少添加一条落地限额条件', 'tr.limit.addLandCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.addLandCond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9a3020c6dfd2ad2a588552d6ede06d62', 'en_US', 'Add at least one landing condition', 'tr.limit.addLandCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.addLandCond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a776d715388540ec233ca651d65c6730', 'zh_CN', '请完整填写支付限额条件', 'tr.limit.fillPayCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.fillPayCond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '94cb2c9fd2266c054129a1940344da4b', 'en_US', 'Please complete payment conditions', 'tr.limit.fillPayCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.fillPayCond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '17737ca4fee4621cee39720b9b1e9930', 'zh_CN', '请完整填写落地限额条件', 'tr.limit.fillLandCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.fillLandCond' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8c39da28f7006b18e50854ed37548e38', 'en_US', 'Please complete landing conditions', 'tr.limit.fillLandCond' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.fillLandCond' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3b56b5d9d9ad73545fa610b79b988ac0', 'zh_CN', '支付限额条件下限定方式不能重复', 'tr.limit.modeDupPay' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.modeDupPay' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '153412d92ff9e380026a6e762814c376', 'en_US', 'Duplicate payment limit mode', 'tr.limit.modeDupPay' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.modeDupPay' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '78285457e7f7ef6ee5e33f0f7bdf250e', 'zh_CN', '落地限额条件下限定方式不能重复', 'tr.limit.modeDupLand' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.modeDupLand' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'dfccace226fa22362e2e53186b6b7489', 'en_US', 'Duplicate landing limit mode', 'tr.limit.modeDupLand' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.modeDupLand' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO



INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd65d85c71ff59c728bc7384a02ad893a', 'zh_CN', '单笔限额', 'tr.limit.mode.SINGLE_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.SINGLE_AMT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4cae4008828944a4098aa6830848fc0a', 'en_US', 'Single Amount', 'tr.limit.mode.SINGLE_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.SINGLE_AMT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ef86461aba7eb41cff5f845330a7fe0d', 'zh_CN', '日累计限额', 'tr.limit.mode.DAY_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.DAY_AMT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4ffc6ae62579bac62ab2a0d8bcc718b0', 'en_US', 'Daily Amount', 'tr.limit.mode.DAY_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.DAY_AMT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '74c080c282bc7f625a237ce35cb328a0', 'zh_CN', '日累计笔数', 'tr.limit.mode.DAY_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.DAY_CNT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f730e9686a9936816eb81864792764fb', 'en_US', 'Daily Count', 'tr.limit.mode.DAY_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.DAY_CNT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'eed8e9757cdfa25749a7346bc6675dc1', 'zh_CN', '周累计限额', 'tr.limit.mode.WEEK_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.WEEK_AMT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd5eeeae656cc691a3ea5db6924ca8135', 'en_US', 'Weekly Amount', 'tr.limit.mode.WEEK_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.WEEK_AMT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '117752e32c614f03057f2c6197031972', 'zh_CN', '周累计笔数', 'tr.limit.mode.WEEK_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.WEEK_CNT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1510016e973b330e2a1b531ec19f0ee4', 'en_US', 'Weekly Count', 'tr.limit.mode.WEEK_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.WEEK_CNT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b5217c0a21894813d6612580731f4707', 'zh_CN', '月累计限额', 'tr.limit.mode.MONTH_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.MONTH_AMT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'afa0a2ebec1b0679a82e21ae56f58049', 'en_US', 'Monthly Amount', 'tr.limit.mode.MONTH_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.MONTH_AMT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '56dec9ab84d3ed0c7e1479cede409666', 'zh_CN', '月累计笔数', 'tr.limit.mode.MONTH_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.MONTH_CNT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3cf4a43b5579cd8cdc0a80d2db0b6627', 'en_US', 'Monthly Count', 'tr.limit.mode.MONTH_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.MONTH_CNT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6d1553026c264e940968aab424ea8ee5', 'zh_CN', '季累计限额', 'tr.limit.mode.QUARTER_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.QUARTER_AMT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'df6f88357371c340c813926303b8f626', 'en_US', 'Quarterly Amount', 'tr.limit.mode.QUARTER_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.QUARTER_AMT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1f29aecbdd04cfc5393aaa6894502095', 'zh_CN', '季累计笔数', 'tr.limit.mode.QUARTER_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.QUARTER_CNT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2418a36aae4bb4ba9dea44194a97f9e9', 'en_US', 'Quarterly Count', 'tr.limit.mode.QUARTER_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.QUARTER_CNT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '93fc0af91fb2885ef4bd3c02247ce8d9', 'zh_CN', '年累计限额', 'tr.limit.mode.YEAR_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.YEAR_AMT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b3cdeb7186674b5182b13d37094f5fbc', 'en_US', 'Yearly Amount', 'tr.limit.mode.YEAR_AMT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.YEAR_AMT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '94e9b4765ccc825b5ab09762b7c26f72', 'zh_CN', '年累计笔数', 'tr.limit.mode.YEAR_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.YEAR_CNT' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f0b0fd62bf5164ff369d9979ea0cab66', 'en_US', 'Yearly Count', 'tr.limit.mode.YEAR_CNT' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.mode.YEAR_CNT' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e8cd7584b251be55fc78f7208a674196', 'zh_CN', '限额规则不存在', 'tr.limit.message.rule.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.rule.not.exist' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '710db59369e505a766fd1287d347f28a', 'en_US', 'Limit rule not found', 'tr.limit.message.rule.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.rule.not.exist' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4f237c4d3dac41277faf29aa0ad4cf48', 'zh_CN', '同一签约客户下该限制维度的「全部」范围规则已存在', 'tr.limit.message.all.scope.exists' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.all.scope.exists' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4d41b53909b722b186c3aa2c8b042105', 'en_US', 'ALL-scope rule already exists for this customer and dimension', 'tr.limit.message.all.scope.exists' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.all.scope.exists' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9a1126a5a10539ff950ecd2297adac8f', 'zh_CN', '规则ID不能为空', 'tr.limit.message.id.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.id.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0910f3fe625c186bb8ae47b6f372eaf9', 'en_US', 'Rule ID is required', 'tr.limit.message.id.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.id.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '88e80dbd827c26c69670cf6248bc38c2', 'zh_CN', '启用标志不正确', 'tr.limit.message.enable.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.enable.invalid' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '43700852334da5d033c7e71fe67ed582', 'en_US', 'Invalid enable flag', 'tr.limit.message.enable.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.enable.invalid' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1222612b4e786329421fb37faab3ce85', 'zh_CN', '保存参数不能为空', 'tr.limit.message.save.empty' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.save.empty' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fda87a0b3816ff339b4c27e8d5cc810c', 'en_US', 'Save parameter is empty', 'tr.limit.message.save.empty' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.save.empty' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'de3b6355eabcf2489325a6bda103b3b2', 'zh_CN', '规则名称不能为空', 'tr.limit.message.rule.name.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.rule.name.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '92c88aa0793736ae1ae6c1a5385aca64', 'en_US', 'Rule name is required', 'tr.limit.message.rule.name.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.rule.name.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b7679e87bda9221b93c5af4a3a64cb42', 'zh_CN', '限制维度不能为空', 'tr.limit.message.limit.dim.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.dim.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1e9c9c84f19e75619edc4f030517195c', 'en_US', 'Limit dimension is required', 'tr.limit.message.limit.dim.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.dim.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '57c1074727ed207e400cc65d4888f5cb', 'zh_CN', '机构不能为空', 'tr.limit.message.corp.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.corp.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f0d2fc9a9aed959d58e1af06135c87a5', 'en_US', 'Institution is required', 'tr.limit.message.corp.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.corp.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '660c91e434575719d6fb1fa598551bab', 'zh_CN', '签约客户不能为空', 'tr.limit.message.cust.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.cust.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cb372b9685844c165ad4d4ab93bc7f41', 'en_US', 'Signed customer is required', 'tr.limit.message.cust.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.cust.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a9dc1ae8553f6e231c0b3e319e597db0', 'zh_CN', '范围模式不能为空', 'tr.limit.message.scope.mode.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.scope.mode.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f5b9f74effb3d8b7e7bdc53bc65066bf', 'en_US', 'Scope mode is required', 'tr.limit.message.scope.mode.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.scope.mode.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '229c5002d1bc544918cb626c523e6bf0', 'zh_CN', '币别不能为空', 'tr.limit.message.currency.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.currency.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'da23c67b2e626c61bd28f6819bb4234e', 'en_US', 'Currency is required', 'tr.limit.message.currency.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.currency.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a2c339b8df6582d00740e69d3b2099ba', 'zh_CN', '请至少选择一种限额类型', 'tr.limit.message.limit.type.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.type.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0c88babd65e3e25dfa6ef20afd02e5e3', 'en_US', 'Select at least one limit type', 'tr.limit.message.limit.type.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.type.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bd3d2083cedd7193b86fd6c176d349e1', 'zh_CN', '请配置限额条件', 'tr.limit.message.cond.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.cond.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6187ccf88d9288a5f46df9b6a57cdfba', 'en_US', 'Please configure limit conditions', 'tr.limit.message.cond.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.cond.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f4e70201fa5b381fa874c14b637bc836', 'zh_CN', '同一限额类型下限定方式不能重复', 'tr.limit.message.mode.dup' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.mode.dup' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8bd17090a7e26fc64911946fd372e7c6', 'en_US', 'Duplicate limit mode under same type', 'tr.limit.message.mode.dup' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.mode.dup' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd2fa003e0c19823e60daacbc5a5a42d4', 'zh_CN', '限定值不能为空', 'tr.limit.message.limit.value.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.value.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '32eb86f5046d59011bfbb3e1dbd0c964', 'en_US', 'Limit value is required', 'tr.limit.message.limit.value.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.value.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'aea8824ca10d9030e34afbee988e7531', 'zh_CN', '请选择单位', 'tr.limit.message.unit.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.unit.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1a134c4b313c16020a59b55a218abc94', 'en_US', 'Please select unit', 'tr.limit.message.unit.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.unit.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a029a808e2390e76f8be7c02d60f3408', 'zh_CN', '请选择账号', 'tr.limit.message.acc.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.acc.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e2e24b08de25d46aa419536bc509cf12', 'en_US', 'Please select account', 'tr.limit.message.acc.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.acc.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2a4ea689d0ebfc7044174cce0c8eb490', 'zh_CN', '单位编码或名称', 'tr.limit.unitCodeOrName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unitCodeOrName' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b21390df6c06e3dcc497f3566e58fa28', 'en_US', 'Unit code or name', 'tr.limit.unitCodeOrName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.unitCodeOrName' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c95b7fb73f5e6e715ff96c44c440754c', 'zh_CN', '单位/账号已绑定其他有效限额规则', 'tr.limit.message.scope.exists' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.scope.exists' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c545367475a73b189f4e1a402e8bd8ee', 'en_US', 'Unit/account already bound to another active limit rule', 'tr.limit.message.scope.exists' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.scope.exists' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '995d849d4208c7077adcce3504c8332c', 'zh_CN', '限制维度不正确', 'tr.limit.message.limit.dim.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.dim.invalid' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ce56a0e46a9f56282615f170769fe52e', 'en_US', 'Invalid limit dimension', 'tr.limit.message.limit.dim.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.dim.invalid' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3f91cd69e6bd8dd9209d0e1cc4a15f30', 'zh_CN', '范围模式不正确', 'tr.limit.message.scope.mode.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.scope.mode.invalid' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9c916e53c3e9ae4cfad36bb6e0f36f18', 'en_US', 'Invalid scope mode', 'tr.limit.message.scope.mode.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.scope.mode.invalid' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7bd45a787fa5266b442027753cf05a05', 'zh_CN', '限定方式不正确', 'tr.limit.message.limit.mode.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.mode.invalid' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c74bf2aa05ff8426529c314289beaef4', 'en_US', 'Invalid limit mode', 'tr.limit.message.limit.mode.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.mode.invalid' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6bb759444ea2b06d0f6000d795758737', 'zh_CN', '限定值必须大于0', 'tr.limit.message.limit.value.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.value.invalid' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd7cb1255927c4313ebe74199ab246628', 'en_US', 'Limit value must be greater than 0', 'tr.limit.message.limit.value.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.limit.value.invalid' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5fbc3374320cb8162e3f051df2c3eb43', 'zh_CN', '{0}单位，已设置了限额规则，不能重复设置', 'tr.limit.message.unit.scope.dup' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.unit.scope.dup' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '80a3982878db50709f1affa3bfa88b0a', 'en_US', 'Unit(s) {0} already have a limit rule and cannot be set again', 'tr.limit.message.unit.scope.dup' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.unit.scope.dup' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '69a89695424cc9cc5800f6bc6d7a52ee', 'zh_CN', '{0}单位，已设置了限额规则', 'tr.limit.message.unit.scope.block.all' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.unit.scope.block.all' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '89d1a89902fabdd1cb9c2ff2511e44f1', 'en_US', 'Unit(s) {0} already have a limit rule', 'tr.limit.message.unit.scope.block.all' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.unit.scope.block.all' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6e1ae201483df4f066df27c874e8f28a', 'zh_CN', '{0}银行账号，已设置了限额规则，不能重复设置', 'tr.limit.message.acc.scope.dup' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.acc.scope.dup' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cf2298d91092bc6ec90116219defa406', 'en_US', 'Bank account(s) {0} already have a limit rule and cannot be set again', 'tr.limit.message.acc.scope.dup' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.acc.scope.dup' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

UPDATE sys_multi_lang SET LANG_CONTEXT = '{0}银行账号，已设置了限额规则，不能重复设置'
WHERE LANG_KEY = 'tr.limit.message.acc.scope.dup' AND LANG_CODE = 'zh_CN';
##GOGO

UPDATE sys_multi_lang SET LANG_CONTEXT = 'Bank account(s) {0} already have a limit rule and cannot be set again'
WHERE LANG_KEY = 'tr.limit.message.acc.scope.dup' AND LANG_CODE = 'en_US';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1df0a62c959083f400788e1145577f08', 'zh_CN', '{0}银行账号，已设置了限额规则', 'tr.limit.message.acc.scope.block.all' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.acc.scope.block.all' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4960d987081ef186ead1ed6609882350', 'en_US', 'Bank account(s) {0} already have a limit rule', 'tr.limit.message.acc.scope.block.all' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.acc.scope.block.all' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

UPDATE sys_multi_lang SET LANG_CONTEXT = '{0}银行账号，已设置了限额规则'
WHERE LANG_KEY = 'tr.limit.message.acc.scope.block.all' AND LANG_CODE = 'zh_CN';
##GOGO

UPDATE sys_multi_lang SET LANG_CONTEXT = 'Bank account(s) {0} already have a limit rule'
WHERE LANG_KEY = 'tr.limit.message.acc.scope.block.all' AND LANG_CODE = 'en_US';
##GOGO

##修改说明：限定方式字典（sys_dict_group / sys_dict）
##GOGO

INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000001', 'tr.limit.mode', '限额限定方式', NULL, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE TYPE_GROUP_CODE = 'tr.limit.mode' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000101', 'SINGLE_AMT', '单笔限额', NULL, g.ID, 1, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'SINGLE_AMT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000102', 'DAY_AMT', '日累计限额', NULL, g.ID, 2, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'DAY_AMT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000103', 'DAY_CNT', '日累计笔数', NULL, g.ID, 3, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'DAY_CNT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000104', 'WEEK_AMT', '周累计限额', NULL, g.ID, 4, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'WEEK_AMT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000105', 'WEEK_CNT', '周累计笔数', NULL, g.ID, 5, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'WEEK_CNT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000106', 'MONTH_AMT', '月累计限额', NULL, g.ID, 6, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'MONTH_AMT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000107', 'MONTH_CNT', '月累计笔数', NULL, g.ID, 7, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'MONTH_CNT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000108', 'QUARTER_AMT', '季累计限额', NULL, g.ID, 8, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'QUARTER_AMT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000109', 'QUARTER_CNT', '季累计笔数', NULL, g.ID, 9, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'QUARTER_CNT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000110', 'YEAR_AMT', '年累计限额', NULL, g.ID, 10, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'YEAR_AMT' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plmode000000000000000000000111', 'YEAR_CNT', '年累计笔数', NULL, g.ID, 11, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.mode'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'YEAR_CNT' LIMIT 1);
##GOGO

##修改说明：限额规则字典（sys_dict_group / sys_dict）— channel / dim / enable
##GOGO

INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plchan000000000000000000000001', 'tr.limit.channel', '管控渠道', NULL, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE TYPE_GROUP_CODE = 'tr.limit.channel' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plchan000000000000000000000101', 'EBANK', '银企直联', NULL, g.ID, 1, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.channel'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'EBANK' LIMIT 1);
##GOGO

INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1pldim0000000000000000000000001', 'tr.limit.dim', '限制维度', NULL, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE TYPE_GROUP_CODE = 'tr.limit.dim' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1pldim0000000000000000000000101', 'U', '单位', NULL, g.ID, 1, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.dim'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'U' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1pldim0000000000000000000000102', 'A', '银行账户', NULL, g.ID, 2, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.dim'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'A' LIMIT 1);
##GOGO

INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plena0000000000000000000000001', 'tr.limit.enable', '启用标志', NULL, NULL, NULL FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE TYPE_GROUP_CODE = 'tr.limit.enable' LIMIT 1
);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plena0000000000000000000000101', '1', '启用', NULL, g.ID, 1, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.enable'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '1' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a1plena0000000000000000000000102', '0', '禁用', NULL, g.ID, 2, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.enable'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '0' LIMIT 1);
##GOGO

##修改说明：落地限额处理国际化/字典（sys_multi_lang / sys_dict）
##约定：ID=md5(key|lang) 去横线小写；可重复执行
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '388c5f4232bb08eb3117b756406ca001', 'zh_CN', '付款账号', 'tr.limit.payAccount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payAccount' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8b0ea89877a636488555722436c16384', 'en_US', 'Payment Account', 'tr.limit.payAccount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payAccount' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cf8119d3701bae44c72e99326abf0579', 'zh_CN', '业务单号', 'tr.limit.businessNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.businessNo' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5a26f31ae86c30736ceae9eb8c46b8b5', 'en_US', 'Business No.', 'tr.limit.businessNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.businessNo' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ad0c08e7a1c482c69c2dc3df30829348', 'zh_CN', '单据编号', 'tr.limit.businessNoHint' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.businessNoHint' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0382e61b981e31bd262f05ee90a26fcc', 'en_US', 'Document No.', 'tr.limit.businessNoHint' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.businessNoHint' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6921c74eeea5e2bc0a7178b966ea4ee5', 'zh_CN', '落地处理状态', 'tr.limit.caseStatus' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.caseStatus' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c8978449a03c71c43c9654c496f08f19', 'en_US', 'Landing Status', 'tr.limit.caseStatus' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.caseStatus' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f2d108d2b8614e3883989c88cce23e7b', 'zh_CN', '来源业务类型', 'tr.limit.sourceBizType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.sourceBizType' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'aeae1511433a2427178547a066b548f9', 'en_US', 'Source Business Type', 'tr.limit.sourceBizType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.sourceBizType' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '222ea2ca240cc944033c3d6172125eb8', 'zh_CN', '业务类型', 'tr.limit.bizType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.bizType' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '507d5bde3ce540af69029a6994fd6866', 'en_US', 'Business Type', 'tr.limit.bizType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.bizType' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3361f694182983468ca46779bc33aece', 'zh_CN', '申请时间', 'tr.limit.applyTime' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.applyTime' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '381e8118abd6f1c01b41b5a5a9d77cbd', 'en_US', 'Apply Time', 'tr.limit.applyTime' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.applyTime' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '525bd6efb3260a02da344b33b80c94ce', 'zh_CN', '付款金额', 'tr.limit.payAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payAmount' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '72bc454d13d505e941548410323fa8ef', 'en_US', 'Payment Amount', 'tr.limit.payAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.payAmount' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6c8fa2b315fc1962303a9b8f226cf24c', 'zh_CN', '触发规则', 'tr.limit.triggerRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.triggerRule' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd7fcfee6c51f01a2c6a5d8154e2c22c1', 'en_US', 'Triggered Rule', 'tr.limit.triggerRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.triggerRule' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b7f433caa64840575fac46f97501830c', 'zh_CN', '申请人', 'tr.limit.applicant' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.applicant' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c2769c077dede6fb8c5db8e1732310cb', 'en_US', 'Applicant', 'tr.limit.applicant' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.applicant' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cca6bcc03b2379799439e194a807d332', 'zh_CN', '处理结果', 'tr.limit.handleResult' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleResult' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '87b94a712871b1c4dab67f8521f33498', 'en_US', 'Handle Result', 'tr.limit.handleResult' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleResult' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '629aefd1a765aa64f3b1d130511a44a8', 'zh_CN', '限额处理', 'tr.limit.handleCase' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleCase' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '91426012d40c87161ba6b4abf3e1f5f2', 'en_US', 'Handle Limit', 'tr.limit.handleCase' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleCase' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd010993bd58fc06f8a85f7e3d03b3fef', 'zh_CN', '流程查看', 'tr.limit.viewFlow' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.viewFlow' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5a61e9fbd209d8b428944f307752de02', 'en_US', 'View Process', 'tr.limit.viewFlow' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.viewFlow' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c3fec9ab2d07749811c1d6a49b299445', 'zh_CN', '批量处理', 'tr.limit.batchHandle' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.batchHandle' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c1985b7b920e39109fe7e425ed905351', 'en_US', 'Batch Handle', 'tr.limit.batchHandle' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.batchHandle' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd977130672fd9ba06884fe8d0f00cfef', 'zh_CN', '落地限额处理', 'tr.limit.caseBizTypeLand' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.caseBizTypeLand' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bed8c51ed15c89c34c555632641948b0', 'en_US', 'Landing Limit Case', 'tr.limit.caseBizTypeLand' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.caseBizTypeLand' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bb27ed5b4ee03673734d6ff296042ba4', 'zh_CN', '查看', 'tr.limit.viewTriggerRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.viewTriggerRule' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b7e7a2815fca3cf60827ceb7d76b1a32', 'en_US', 'View', 'tr.limit.viewTriggerRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.viewTriggerRule' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7872a7de025c6c97a7d0e7e510824e4d', 'zh_CN', '暂无审批流程', 'tr.limit.noProcessInstance' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noProcessInstance' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f56cd96618417ff169e345eb755f4ea3', 'en_US', 'No process instance', 'tr.limit.noProcessInstance' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noProcessInstance' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'db3876ef4a8b144cf2987c75425706e2', 'zh_CN', '单据尚未限额处理/提审，暂无审批流程', 'tr.limit.noProcessInstancePending' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noProcessInstancePending' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
UPDATE sys_multi_lang SET LANG_CONTEXT = '单据尚未限额处理/提审，暂无审批流程' WHERE LANG_KEY = 'tr.limit.noProcessInstancePending' AND LANG_CODE = 'zh_CN';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fa1b2b997e4819b22efe83c357950b2e', 'en_US', 'Case has not been submitted for approval yet', 'tr.limit.noProcessInstancePending' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noProcessInstancePending' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c31d22ce7cefa2b0fad505b84f97eabb', 'zh_CN', '拒绝支付不走审批流，暂无审批流程', 'tr.limit.noProcessInstanceRefuse' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noProcessInstanceRefuse' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '10688b82248a0e73c07239fda8591e90', 'en_US', 'Refuse payment does not start a workflow', 'tr.limit.noProcessInstanceRefuse' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.noProcessInstanceRefuse' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '12a685890e4d2e4ced0b2c21a5fbaa24', 'zh_CN', '请选择待处理的落地限额单据', 'tr.limit.selectPendingCase' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectPendingCase' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
UPDATE sys_multi_lang SET LANG_CONTEXT = '请选择待处理的落地限额单据' WHERE LANG_KEY = 'tr.limit.selectPendingCase' AND LANG_CODE = 'zh_CN';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '94ceea4e3a3e1bc7e12037328f0f746d', 'en_US', 'Please select pending approval cases', 'tr.limit.selectPendingCase' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectPendingCase' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '20d85756898215344dfab15b65a7db3a', 'zh_CN', '批量处理仅支持待处理且未提审的单据', 'tr.limit.onlyPendingBatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.onlyPendingBatch' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
UPDATE sys_multi_lang SET LANG_CONTEXT = '批量处理仅支持待处理且未提审的单据' WHERE LANG_KEY = 'tr.limit.onlyPendingBatch' AND LANG_CODE = 'zh_CN';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1e29017408dd56f10cfafcbdb1164182', 'en_US', 'Batch only supports pending status', 'tr.limit.onlyPendingBatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.onlyPendingBatch' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1b564ddc78b789f4111191e6f1a06b4b', 'zh_CN', '请选择处理结果', 'tr.limit.selectHandleResult' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectHandleResult' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1baf790e14db2f79fe57606bd3d4b7a9', 'en_US', 'Please select handle result', 'tr.limit.selectHandleResult' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.selectHandleResult' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b745cc0464cbd60185eb5f581b6af995', 'zh_CN', '同意支付', 'tr.limit.agreePay' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.agreePay' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '09dc6073f6e12867338426d561ff3551', 'en_US', 'Agree to Pay', 'tr.limit.agreePay' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.agreePay' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e0bc4f01f7b0ca67d021e14c0d7e5ec8', 'zh_CN', '拒绝支付', 'tr.limit.refusePay' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.refusePay' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c492d618891f5b84e2424d0a227b434b', 'en_US', 'Refuse to Pay', 'tr.limit.refusePay' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.refusePay' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c39997011baddcc792809e478bb1f2dc', 'zh_CN', '业务单据信息', 'tr.limit.caseBasicInfo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.caseBasicInfo' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '33adce9379705dca1ca2630d56053a9d', 'en_US', 'Business Document Info', 'tr.limit.caseBasicInfo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.caseBasicInfo' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

## 已存在环境：单据信息 → 业务单据信息
UPDATE sys_multi_lang SET LANG_CONTEXT = '业务单据信息'
WHERE LANG_KEY = 'tr.limit.caseBasicInfo' AND LANG_CODE = 'zh_CN';
##GOGO

UPDATE sys_multi_lang SET LANG_CONTEXT = 'Business Document Info'
WHERE LANG_KEY = 'tr.limit.caseBasicInfo' AND LANG_CODE = 'en_US';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fdc935d420a04ef7ac11ec4dac33b5e3', 'zh_CN', '限额处理', 'tr.limit.handlePanel' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handlePanel' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1d850c36c3ee9f545d3094bba2d8cea', 'en_US', 'Limit Handle', 'tr.limit.handlePanel' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handlePanel' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '66069787d48285c51ffef8fd47495df3', 'zh_CN', '处理说明', 'tr.limit.handleDesc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleDesc' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ad600d42cd0fb13ecf1ccfac06b395e1', 'en_US', 'Handle Remark', 'tr.limit.handleDesc' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleDesc' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b81d97ae1692908eab6582b8b1dcf03f', 'zh_CN', '限额规则', 'tr.limit.limitRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitRule' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '47cee49d55184e1cce6fb9f6a47e0150', 'en_US', 'Limit Rule', 'tr.limit.limitRule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitRule' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fb177332adc5f60dd89958be1274ab84', 'zh_CN', '规则', 'tr.limit.rulePrefix' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.rulePrefix' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1730ad1a43f5914d77cc393571b80408', 'en_US', 'Rule ', 'tr.limit.rulePrefix' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.rulePrefix' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6f7b2adb3dbe172f5ecd51ba85781b66', 'zh_CN', '说明处理结果原因', 'tr.limit.handleDescHint' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleDescHint' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3dd4ddabcab52a45e4288cf49ea4f3c0', 'en_US', 'Explain the handle result', 'tr.limit.handleDescHint' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleDescHint' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0299ef4aa7f0ff1254b0df6ba19ee282', 'zh_CN', '请填写处理说明', 'tr.limit.handleDescRequired' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleDescRequired' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '85cf15c7beaa64442e13ee947fe658c6', 'en_US', 'Handle description is required', 'tr.limit.handleDescRequired' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.handleDescRequired' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6fbd50034f9bc98cf6ff5a531a2ce67a', 'zh_CN', '限定方式', 'tr.limit.limitMode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitMode' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3f5a391b6dab7c468389df887ad9c227', 'en_US', 'Limit Mode', 'tr.limit.limitMode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitMode' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a29a9d88c739cc3b449bdb3cdf649687', 'zh_CN', '限定金额', 'tr.limit.limitAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitAmount' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '99cc17d65a5a8b0814a166747c112a6d', 'en_US', 'Limit Amount', 'tr.limit.limitAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitAmount' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '271bcbce79e9db00b2be031adc83652c', 'zh_CN', '实际金额', 'tr.limit.actualAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.actualAmount' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '285e14f95e0d6b27b39556f5222b0547', 'en_US', 'Actual Amount', 'tr.limit.actualAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.actualAmount' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '176900cc268b43a7fb7856346006aa2b', 'zh_CN', '超出金额', 'tr.limit.excessAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessAmount' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f7b4d7fe1ecdee02b37aa1ac01691144', 'en_US', 'Excess Amount', 'tr.limit.excessAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessAmount' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c4e4110e5e923c602309440c94fc0922', 'zh_CN', '限定笔数', 'tr.limit.limitCount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitCount' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ace40dfb67a6b2a18749470f0a669d1c', 'en_US', 'Limit Count', 'tr.limit.limitCount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.limitCount' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cd1998cf2f957ce140e960df2d122607', 'zh_CN', '实际笔数', 'tr.limit.actualCount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.actualCount' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '07022b150188e625f7d1dc85c9dd8dba', 'en_US', 'Actual Count', 'tr.limit.actualCount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.actualCount' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c5bb86c76fa77b031f7704d8a007843f', 'zh_CN', '超出笔数', 'tr.limit.excessCount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessCount' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7b4284abcdcc3fa6754dd783b417af9c', 'en_US', 'Excess Count', 'tr.limit.excessCount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessCount' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '14e7542cfd3189e93a2339d0612acd75', 'zh_CN', '超出比例', 'tr.limit.excessRatio' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessRatio' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '56d25260ee41f7d51dd61155a52cee05', 'en_US', 'Excess Ratio', 'tr.limit.excessRatio' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessRatio' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e59326a8beba00b4de12d8c29d721262', 'zh_CN', '落地限额处理单不存在', 'tr.limit.message.case.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.not.exist' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0b785d002bfa9553d0d6e93f0872d2ab', 'en_US', 'Landing limit case does not exist', 'tr.limit.message.case.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.not.exist' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '336f44289714dde842a81d5dd0ac5695', 'zh_CN', '仅待处理且未提审状态可做限额处理', 'tr.limit.message.case.status.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.status.invalid' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
UPDATE sys_multi_lang SET LANG_CONTEXT = '仅待处理且未提审状态可做限额处理' WHERE LANG_KEY = 'tr.limit.message.case.status.invalid' AND LANG_CODE = 'zh_CN';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1edd5b1730bf7fb01404765d00396ea7', 'en_US', 'Only pending cases can be handled', 'tr.limit.message.case.status.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.status.invalid' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2c7316cb3f6b2a522b184bdec552f948', 'zh_CN', '请选择处理结果', 'tr.limit.message.handle.result.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.handle.result.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bc3130ad2cec421124e010583f36fa56', 'en_US', 'Please select handle result', 'tr.limit.message.handle.result.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.handle.result.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bae8b5d34395d99133a51bbef5465021', 'zh_CN', '处理结果不正确', 'tr.limit.message.handle.result.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.handle.result.invalid' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fd25a03ece141206e5ee493093210bfe', 'en_US', 'Invalid handle result', 'tr.limit.message.handle.result.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.handle.result.invalid' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6cc4b4758289f0686492824bd231794a', 'zh_CN', '请选择待处理的落地限额单据', 'tr.limit.message.case.select.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.select.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
UPDATE sys_multi_lang SET LANG_CONTEXT = '请选择待处理的落地限额单据' WHERE LANG_KEY = 'tr.limit.message.case.select.required' AND LANG_CODE = 'zh_CN';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'dc026be76c7c6e063acdc6054e799d96', 'en_US', 'Please select pending cases', 'tr.limit.message.case.select.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.select.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '621024b8be705586623bd31c37d14c07', 'zh_CN', '处理单ID不能为空', 'tr.limit.message.case.id.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.id.required' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7070af26070e631354f1f5588d6b1d25', 'en_US', 'Case id is required', 'tr.limit.message.case.id.required' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.id.required' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '71840fc675053cf63ef886be0ba28ccc', 'tr.limit.caseStatus', '落地处理状态', NULL, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE TYPE_GROUP_CODE = 'tr.limit.caseStatus' LIMIT 1);
##GOGO

-- 落地处理状态：0待处理/1待审批/2审批通过/3打回（需求 3.1.2）
-- 若历史脚本删过「0」，此处幂等补回；「1」名称统一为待审批
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '7a235daab2f708f1fa610b5ff941ee37', '0', '待处理', NULL, g.ID, 0, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '0' LIMIT 1);
##GOGO

UPDATE sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
SET d.TYPE_NAME = '待处理', d.SORT = 0
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus' AND d.TYPE_CODE = '0';
##GOGO

-- 无流程实例的「1」视为尚未限额处理完成，回退为待处理（兼容曾误把待处理并入待审批的数据）
UPDATE BT_TR_LIMIT_CASE
SET CASE_STATUS = '0'
WHERE CASE_STATUS = '1'
  AND (PROCESS_INSTANCE_ID IS NULL OR PROCESS_INSTANCE_ID = '');
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '2687539040f8ad99c41dbf40988315f2', '1', '待审批', NULL, g.ID, 1, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '1' LIMIT 1);
##GOGO

UPDATE sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
SET d.TYPE_NAME = '待审批', d.SORT = 1
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus' AND d.TYPE_CODE = '1';
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '7344778b7068a7dd78b52877ee1a5900', '2', '审批通过', NULL, g.ID, 2, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '2' LIMIT 1);
##GOGO

UPDATE sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
SET d.TYPE_NAME = '审批通过', d.SORT = 2
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus' AND d.TYPE_CODE = '2';
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '97eba4932ecaa65714dc41ff01257d6a', '3', '打回', NULL, g.ID, 3, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '3' LIMIT 1);
##GOGO

UPDATE sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
SET d.TYPE_NAME = '打回', d.SORT = 3
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus' AND d.TYPE_CODE = '3';
##GOGO

INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '873adbe5daa6e0c3a741fa23947cdf04', 'tr.limit.handleResult', '限额处理结果', NULL, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE TYPE_GROUP_CODE = 'tr.limit.handleResult' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '2d00188a676c8c2623ce01dca0c32230', 'A', '同意支付', NULL, g.ID, 1, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.handleResult'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'A' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'dabfe353d5ae652465d0e1924ad3bb45', 'R', '拒绝支付', NULL, g.ID, 2, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.handleResult'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'R' LIMIT 1);
##GOGO

-- 来源业务类型：0付款办理/1资金收付/2代发工资（不再维护 3-费用报销）
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '24339d0ecbe8dfb9461e9d3c2b077df9', 'tr.limit.sourceBizType', '来源业务类型', NULL, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE TYPE_GROUP_CODE = 'tr.limit.sourceBizType' LIMIT 1);
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '03fadeee4f2dd81f59bf3e9e61363a9c', '0', '付款办理', NULL, g.ID, 1, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.sourceBizType'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '0' LIMIT 1);
##GOGO

UPDATE sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
SET d.TYPE_NAME = '付款办理', d.SORT = 1
WHERE g.TYPE_GROUP_CODE = 'tr.limit.sourceBizType' AND d.TYPE_CODE = '0';
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'c6d90c23a21ae37f04f3b1b9ef2b3b12', '1', '资金收付', NULL, g.ID, 2, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.sourceBizType'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '1' LIMIT 1);
##GOGO

UPDATE sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
SET d.TYPE_NAME = '资金收付', d.SORT = 2
WHERE g.TYPE_GROUP_CODE = 'tr.limit.sourceBizType' AND d.TYPE_CODE = '1';
##GOGO

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '2d4a30a3a409091168f894a0ef79f6e4', '2', '代发工资', NULL, g.ID, 3, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.sourceBizType'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '2' LIMIT 1);
##GOGO

UPDATE sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
SET d.TYPE_NAME = '代发工资', d.SORT = 3
WHERE g.TYPE_GROUP_CODE = 'tr.limit.sourceBizType' AND d.TYPE_CODE = '2';
##GOGO

-- 清理历史字典项：费用报销（TYPE_CODE=3）
DELETE d FROM sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
WHERE g.TYPE_GROUP_CODE = 'tr.limit.sourceBizType' AND d.TYPE_CODE = '3';
##GOGO

##修改说明：落地限额处理工作流相关消息
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a55819d03e076aaad31292d2fa9deb8e', 'zh_CN', '工作流服务不可用或未找到待办任务', 'tr.limit.message.flow.unavailable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.flow.unavailable' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '478628b1e019b18d4df0f6608541212f', 'en_US', 'Workflow unavailable or no active task', 'tr.limit.message.flow.unavailable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.flow.unavailable' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO




-- 企业端银行落地限额处理结果国际化
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f601', 'zh_CN', '落地限额处理结果', 'table.enterpriseLimit.landingResult' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.enterpriseLimit.landingResult' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f602', 'en_US', 'Landing limit result', 'table.enterpriseLimit.landingResult' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'table.enterpriseLimit.landingResult' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f603', 'zh_CN', '银行同意支付', 'enterpriseLimit.landingResult.approved' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'enterpriseLimit.landingResult.approved' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f604', 'en_US', 'Bank approved payment', 'enterpriseLimit.landingResult.approved' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'enterpriseLimit.landingResult.approved' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f605', 'zh_CN', '银行拒绝支付', 'enterpriseLimit.landingResult.rejected' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'enterpriseLimit.landingResult.rejected' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f606', 'en_US', 'Bank rejected payment', 'enterpriseLimit.landingResult.rejected' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'enterpriseLimit.landingResult.rejected' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

-- 企业端银行落地限额处理结果字典（0 等待银行限额审批 / A 银行同意支付 / R 银行拒绝支付）
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f701', 'enterpriseLimit.landingResult', '落地限额处理结果' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = 'a1b2c3d4e5f6478890a1b2c3d4e5f701' LIMIT 1);
##GOGO
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f702', '0', '等待银行限额审批', '', 'a1b2c3d4e5f6478890a1b2c3d4e5f701', 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'a1b2c3d4e5f6478890a1b2c3d4e5f702' LIMIT 1);
##GOGO
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f703', 'A', '银行同意支付', '', 'a1b2c3d4e5f6478890a1b2c3d4e5f701', 2 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'a1b2c3d4e5f6478890a1b2c3d4e5f703' LIMIT 1);
##GOGO
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'a1b2c3d4e5f6478890a1b2c3d4e5f704', 'R', '银行拒绝支付', '', 'a1b2c3d4e5f6478890a1b2c3d4e5f701', 3 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'a1b2c3d4e5f6478890a1b2c3d4e5f704' LIMIT 1);
##GOGO



INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '付款限额校验上下文不完整', 'limit.control.context.incomplete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.context.incomplete' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Incomplete payment limit validation context', 'limit.control.context.incomplete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.context.incomplete' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '付款限额台账来源标识不完整', 'limit.control.source.identity.incomplete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.source.identity.incomplete' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Incomplete payment limit ledger source identifier', 'limit.control.source.identity.incomplete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.source.identity.incomplete' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '当前租户未维护签约银行机构', 'limit.control.tenant.relation.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.tenant.relation.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'No contracted bank organization is maintained for the current tenant', 'limit.control.tenant.relation.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.tenant.relation.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '付款超出银行限额规则【{0}】', 'limit.control.amount.exceeds.rule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.amount.exceeds.rule' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Payment exceeds bank limit rule [{0}]', 'limit.control.amount.exceeds.rule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.amount.exceeds.rule' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '限额币别【{0}】不存在', 'limit.control.limit.currency.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.limit.currency.not.exist' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Limit currency [{0}] does not exist', 'limit.control.limit.currency.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.limit.currency.not.exist' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '付款币别【{0}】到限额币别【{1}】的折算汇率不存在', 'limit.control.exchange.rate.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.exchange.rate.not.exist' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Exchange rate from payment currency [{0}] to limit currency [{1}] does not exist', 'limit.control.exchange.rate.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.exchange.rate.not.exist' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '不支持的限额方式【{0}】', 'limit.control.unsupported.limit.mode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.unsupported.limit.mode' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Unsupported limit mode [{0}]', 'limit.control.unsupported.limit.mode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.unsupported.limit.mode' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '报销成功金额或付款信息与限额占用快照不一致', 'limit.control.retain.snapshot.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.retain.snapshot.mismatch' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Successful reimbursement amount or payment information does not match the limit occupation snapshot', 'limit.control.retain.snapshot.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.retain.snapshot.mismatch' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额台账未记录企业端申请人，无法续办业务', 'limit.control.txn.applicant.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Bank limit ledger does not record the enterprise applicant, unable to continue', 'limit.control.txn.applicant.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额台账申请人不存在，无法续办业务', 'limit.control.txn.applicant.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.not.exist' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Bank limit ledger applicant does not exist, unable to continue', 'limit.control.txn.applicant.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.not.exist' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额台账申请人与回调租户不一致', 'limit.control.txn.applicant.tenant.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.tenant.mismatch' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Bank limit ledger applicant does not match callback tenant', 'limit.control.txn.applicant.tenant.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.tenant.mismatch' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '不支持的银行限额回调业务类型', 'limit.control.unsupported.callback.biz.type' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.unsupported.callback.biz.type' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Unsupported bank limit callback business type', 'limit.control.unsupported.callback.biz.type' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.unsupported.callback.biz.type' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '未找到当前业务单据的有效限额占用', 'limit.control.callback.active.txn.not.found' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.active.txn.not.found' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'No valid limit occupation found for the current business document', 'limit.control.callback.active.txn.not.found' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.active.txn.not.found' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额审批结果属于旧付款尝试，已拒绝处理', 'limit.control.callback.stale.attempt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.stale.attempt' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Bank limit approval result belongs to an old payment attempt and was rejected', 'limit.control.callback.stale.attempt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.stale.attempt' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额审批回调机构与付款限额台账不一致', 'limit.control.callback.bank.org.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.bank.org.mismatch' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Callback bank organization does not match the payment limit ledger', 'limit.control.callback.bank.org.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.bank.org.mismatch' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '未找到当前付款尝试对应的银行限额审批案例', 'limit.control.callback.case.not.found' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.case.not.found' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'No bank limit approval case found for the current payment attempt', 'limit.control.callback.case.not.found' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.case.not.found' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额审批回调案例号与企业端不一致', 'limit.control.callback.case.no.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.case.no.mismatch' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Callback bank limit case number does not match the enterprise', 'limit.control.callback.case.no.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.case.no.mismatch' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额审批案例已有不同的最终处理结果', 'limit.control.callback.result.conflict' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.result.conflict' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Bank limit approval case already has a different final result', 'limit.control.callback.result.conflict' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.result.conflict' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额审批回调参数不完整', 'limit.control.callback.param.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.param.invalid' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Incomplete bank limit approval callback parameters', 'limit.control.callback.param.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.param.invalid' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额审批结果仅支持A同意或R拒绝', 'limit.control.callback.unsupported.result' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.unsupported.result' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Bank limit approval result supports only A for approved or R for rejected', 'limit.control.callback.unsupported.result' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.unsupported.result' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '银行限额回调eventId已被其他审批结果使用', 'limit.control.callback.event.conflict' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.event.conflict' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Bank limit callback eventId has been used by another approval result', 'limit.control.callback.event.conflict' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.event.conflict' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '付款单状态已变化，不能续办银行限额审批结果', 'limit.control.nis.continue.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.continue.status.changed' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Payment order status has changed; cannot continue bank limit approval result', 'limit.control.nis.continue.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.continue.status.changed' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '付款单结算方式不支持银行限额审批后续办指令', 'limit.control.nis.unsupported.settle.type' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.unsupported.settle.type' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Payment order settlement method does not support continuing instruction after bank limit approval', 'limit.control.nis.unsupported.settle.type' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.unsupported.settle.type' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '付款单状态已变化，不能处理银行限额审批拒绝结果', 'limit.control.nis.reject.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.reject.status.changed' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Payment order status has changed; cannot process bank limit approval rejection result', 'limit.control.nis.reject.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.reject.status.changed' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '境外付款单状态已变化，不能续办银行限额审批结果', 'limit.control.fex.continue.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.continue.status.changed' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Foreign payment order status has changed; cannot continue bank limit approval result', 'limit.control.fex.continue.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.continue.status.changed' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '境外付款单状态已变化，不能处理银行限额审批拒绝结果', 'limit.control.fex.reject.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.reject.status.changed' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Foreign payment order status has changed; cannot process bank limit approval rejection result', 'limit.control.fex.reject.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.reject.status.changed' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '境外付款限额校验无法取得当前操作人', 'limit.control.fex.operator.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.operator.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Current operator cannot be obtained for foreign payment limit validation', 'limit.control.fex.operator.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.operator.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '境外付款限额校验无法取得币别英文编码', 'limit.control.fex.currency.code.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.currency.code.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Currency English code cannot be obtained for foreign payment limit validation', 'limit.control.fex.currency.code.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.currency.code.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '付款限额校验无法取得当前操作人', 'limit.control.nis.operator.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.operator.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Current operator cannot be obtained for payment limit validation', 'limit.control.nis.operator.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.operator.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '付款限额校验无法取得币别英文编码', 'limit.control.nis.currency.code.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.currency.code.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Currency English code cannot be obtained for payment limit validation', 'limit.control.nis.currency.code.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.currency.code.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '自动付款限额校验无法取得付款单申请人', 'limit.control.auto.pay.applicant.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.auto.pay.applicant.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Payment order applicant cannot be obtained for automatic payment limit validation', 'limit.control.auto.pay.applicant.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.auto.pay.applicant.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '费用报销存在等待银行结果的明细，请待交易结果明确后再打回', 'limit.control.expense.wait.result' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.expense.wait.result' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Expense reimbursement contains details waiting for bank result; cannot reject until the transaction result is clear', 'limit.control.expense.wait.result' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.expense.wait.result' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'zh_CN', '报销成功明细金额超过汇总付款金额，不能打回', 'limit.control.expense.success.amount.exceeds' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.expense.success.amount.exceeds' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT MD5(UUID()), 'en_US', 'Successful reimbursement detail amount exceeds the summary payment amount; cannot reject', 'limit.control.expense.success.amount.exceeds' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.expense.success.amount.exceeds' AND LANG_CODE = 'en_US');
##GOGO

##风险管理（一级）
##注意：一级菜单 PARENT_ID 必须为 NULL（不能写 ''），否则菜单管理 treeGrid(adjacency) 不认根节点，整棵子树不展示。
INSERT INTO sys_menu_manager (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT 'd65f397e75a54a0a9de38edda520b0f4', NULL, '风险管理', '风险管理', NULL, NULL, 500, 1, NULL, NULL, NULL,
       '2026-09-01 16:23:39', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, 1, 0, NULL
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu_manager WHERE ID = 'd65f397e75a54a0a9de38edda520b0f4' LIMIT 1
);
##GOGO
-- 已入库但 PARENT_ID='' 的存量数据修复（与客户管理等一级菜单一致）
UPDATE sys_menu_manager
SET PARENT_ID = NULL, IS_MODULE = 1
WHERE ID = 'd65f397e75a54a0a9de38edda520b0f4';
##GOGO

##限额管理（二级）
INSERT INTO sys_menu_manager (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT 'f7644a0231134bffb5e034f4cc9ec647', NULL, '限额管理', '限额管理', 'd65f397e75a54a0a9de38edda520b0f4', NULL, 500, 1, NULL, NULL, NULL,
       '2026-09-01 16:26:06', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, 0, 0, NULL
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu_manager WHERE ID = 'f7644a0231134bffb5e034f4cc9ec647' LIMIT 1
);
##GOGO

##限额规则管理
INSERT INTO sys_menu_manager (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '222d2e78c94a41e2819a602ccf1d51e5', NULL, '限额规则管理', '限额规则管理', 'f7644a0231134bffb5e034f4cc9ec647', NULL, 500, 1,
       '/paymentLimit/rule/list', NULL, NULL,
       '2026-09-01 16:26:59', '402880425b6b0a58015b6b0abeb10000', NULL, NULL, 0, 0, NULL
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu_manager WHERE ID = '222d2e78c94a41e2819a602ccf1d51e5' LIMIT 1
);
##GOGO

##限额落地处理
INSERT INTO sys_menu_manager (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
SELECT '0ac75c19351840a19a53ef94cd7b35fb', '', '限额落地处理', '限额落地处理', 'f7644a0231134bffb5e034f4cc9ec647', NULL, 500, 1,
       '/paymentLimit/case/list', NULL, '',
       '2026-09-02 17:19:42', '402880425b6b0a58015b6b0abeb10000',
       '2026-09-03 10:15:45', '402880425b6b0a58015b6b0abeb10000', 0, 0, NULL
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu_manager WHERE ID = '0ac75c19351840a19a53ef94cd7b35fb' LIMIT 1
);
##GOGO

-- 限额跨服务来源业务类型统一为 tr.limit.sourceBizType 字典码值。
UPDATE BT_TR_LIMIT_TXN
SET SOURCE_BIZ_TYPE = CASE SOURCE_BIZ_TYPE
    WHEN 'NIS_BILLHEAD' THEN '0'
    WHEN 'FEX_CASH_FLOW' THEN '1'
    WHEN 'NIS_WAGE' THEN '2'
    ELSE SOURCE_BIZ_TYPE
END
WHERE SOURCE_BIZ_TYPE IN ('NIS_BILLHEAD', 'FEX_CASH_FLOW', 'NIS_WAGE');
##GOGO
##补全落地处理状态「待处理」(0)，供列表查询条件下拉展示
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '7a235daab2f708f1fa610b5ff941ee37', '0', '待处理', NULL, g.ID, 0, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = '0' LIMIT 1);
##GOGO
UPDATE sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
SET d.TYPE_NAME = '待处理', d.SORT = 0
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseStatus' AND d.TYPE_CODE = '0';
##GOGO

##业务类型字典（列表展示，默认落地限额处理）；与来源业务类型 SOURCE_BIZ_TYPE 区分
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT 'a8a503eaa98d71dc15d4bef9d734281d', 'tr.limit.caseBizType', '落地处理业务类型', NULL, NULL, NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE TYPE_GROUP_CODE = 'tr.limit.caseBizType' LIMIT 1);
##GOGO
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME, TENANT_ID)
SELECT '92325a81352c442aa207fce2c4e860dd', 'LAND_CASE', '落地限额处理', NULL, g.ID, 1, NULL, NULL, NULL FROM sys_dict_group g
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseBizType'
  AND NOT EXISTS (SELECT 1 FROM sys_dict d WHERE d.TYPE_GROUP_ID = g.ID AND d.TYPE_CODE = 'LAND_CASE' LIMIT 1);
##GOGO
UPDATE sys_dict d
INNER JOIN sys_dict_group g ON g.ID = d.TYPE_GROUP_ID
SET d.TYPE_NAME = '落地限额处理', d.SORT = 1
WHERE g.TYPE_GROUP_CODE = 'tr.limit.caseBizType' AND d.TYPE_CODE = 'LAND_CASE';
##GOGO

UPDATE BT_TR_LIMIT_CASE
SET SOURCE_BIZ_TYPE = CASE SOURCE_BIZ_TYPE
    WHEN 'NIS_BILLHEAD' THEN '0'
    WHEN 'FEX_CASH_FLOW' THEN '1'
    WHEN 'NIS_WAGE' THEN '2'
    ELSE SOURCE_BIZ_TYPE
END
WHERE SOURCE_BIZ_TYPE IN ('NIS_BILLHEAD', 'FEX_CASH_FLOW', 'NIS_WAGE');
##GOGO

-- 修复 CURRENCY_CODE 重命名后仍保存 CNY 等编码的存量限额规则。
UPDATE BT_TR_LIMIT_RULE r
SET CURRENCY_ID = (SELECT MIN(c.ID) FROM BT_CURRENCY_MANAGER c
                      WHERE c.ENGLISH_CODE = r.CURRENCY_ID OR c.CUR_NAME = r.CURRENCY_ID)
WHERE NOT EXISTS (SELECT 1 FROM BT_CURRENCY_MANAGER i WHERE i.ID = r.CURRENCY_ID)
  AND EXISTS (SELECT 1 FROM BT_CURRENCY_MANAGER c
               WHERE c.ENGLISH_CODE = r.CURRENCY_ID OR c.CUR_NAME = r.CURRENCY_ID);
##GOGO

##修改说明：落地限额处理单建单幂等提示
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3f0f6195eae5469917f2c95f579180fc', 'zh_CN', '该业务单已有未完成的落地限额处理单，不能重复创建', 'tr.limit.message.case.in.progress' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.in.progress' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'aebde3024f8585dde668393f430b3c45', 'en_US', 'An unfinished landing limit case already exists for this business bill', 'tr.limit.message.case.in.progress' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.message.case.in.progress' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO

-- 统一内管接口与企业端展示的支付超限友好提示。
UPDATE sys_multi_lang
SET LANG_CONTEXT = '付款金额已超过银行限额规则【{0}】，请调整付款金额后重新提交。'
WHERE LANG_KEY = 'limit.control.amount.exceeds.rule' AND LANG_CODE = 'zh_CN';
##GOGO

UPDATE sys_multi_lang
SET LANG_CONTEXT = 'The payment amount exceeds bank limit rule [{0}]. Please adjust the amount and submit again.'
WHERE LANG_KEY = 'limit.control.amount.exceeds.rule' AND LANG_CODE = 'en_US';
##GOGO



-- 企业端支付超限友好提示
UPDATE sys_multi_lang
SET LANG_CONTEXT = '付款金额已超过银行限额规则【{0}】，请调整付款金额后重新提交。'
WHERE LANG_KEY = 'limit.control.amount.exceeds.rule' AND LANG_CODE = 'zh_CN';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8113573d53284c22980381891a96bfd8', 'zh_CN', '付款金额已超过银行限额规则【{0}】，请调整付款金额后重新提交。', 'limit.control.amount.exceeds.rule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.amount.exceeds.rule' AND LANG_CODE = 'zh_CN');
##GOGO

UPDATE sys_multi_lang
SET LANG_CONTEXT = 'The payment amount exceeds bank limit rule [{0}]. Please adjust the amount and submit again.'
WHERE LANG_KEY = 'limit.control.amount.exceeds.rule' AND LANG_CODE = 'en_US';
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '563c17dbbdf345f898743c204c06ac3b', 'en_US', 'The payment amount exceeds bank limit rule [{0}]. Please adjust the amount and submit again.', 'limit.control.amount.exceeds.rule' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.amount.exceeds.rule' AND LANG_CODE = 'en_US');
##GOGO

-- 企业端支付限额业务消息（08001～08033）

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6a21687a25b4488b07655c263ea189a1', 'zh_CN', '付款限额校验上下文不完整', 'limit.control.context.incomplete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.context.incomplete' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '07d71c99b22b286e612527bfec0c5466', 'en_US', 'Incomplete payment limit validation context', 'limit.control.context.incomplete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.context.incomplete' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'edee0fac45ed9f98df4be5196c48ce97', 'zh_CN', '付款限额台账来源标识不完整', 'limit.control.source.identity.incomplete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.source.identity.incomplete' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b20f275f6f7cfaab94c400ccc64c8661', 'en_US', 'Incomplete payment limit ledger source identifier', 'limit.control.source.identity.incomplete' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.source.identity.incomplete' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bd4842604c0ef5e4ed039770cf6805d7', 'zh_CN', '当前租户未维护签约银行机构', 'limit.control.tenant.relation.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.tenant.relation.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '63b367ed17a15a6e60781f0cee8deb56', 'en_US', 'No contracted bank organization is maintained for the current tenant', 'limit.control.tenant.relation.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.tenant.relation.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ba393bfe3722bf4fc0d7b28cd73830bc', 'zh_CN', '限额币别【{0}】不存在', 'limit.control.limit.currency.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.limit.currency.not.exist' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '82b3b9932767f730d83aa777e4bd159a', 'en_US', 'Limit currency [{0}] does not exist', 'limit.control.limit.currency.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.limit.currency.not.exist' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '787e2942b61af16f2809f563d7d7a3f9', 'zh_CN', '付款币别【{0}】到限额币别【{1}】的折算汇率不存在', 'limit.control.exchange.rate.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.exchange.rate.not.exist' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5055410272df3ba15084049bff788fdc', 'en_US', 'Exchange rate from payment currency [{0}] to limit currency [{1}] does not exist', 'limit.control.exchange.rate.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.exchange.rate.not.exist' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '54065504708bd6abfc455759fc77f6c7', 'zh_CN', '不支持的限额方式【{0}】', 'limit.control.unsupported.limit.mode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.unsupported.limit.mode' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7dfa6fad37fcd09e685e537ec4c78728', 'en_US', 'Unsupported limit mode [{0}]', 'limit.control.unsupported.limit.mode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.unsupported.limit.mode' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3fef55d9e16db190acecfd49c8548464', 'zh_CN', '报销成功金额或付款信息与限额占用快照不一致', 'limit.control.retain.snapshot.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.retain.snapshot.mismatch' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7a8c725316593a8139629f80109b7908', 'en_US', 'Successful reimbursement amount or payment information does not match the limit occupation snapshot', 'limit.control.retain.snapshot.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.retain.snapshot.mismatch' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f1d2f327d0edf68379e9dedf9070d8ec', 'zh_CN', '银行限额台账未记录企业端申请人，无法续办业务', 'limit.control.txn.applicant.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a71f3a697824796e3b2f4a745af23aec', 'en_US', 'Bank limit ledger does not record the enterprise applicant, unable to continue', 'limit.control.txn.applicant.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '88c685cdc71c065943d209f55f050bf9', 'zh_CN', '银行限额台账申请人不存在，无法续办业务', 'limit.control.txn.applicant.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.not.exist' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3ee731f6d93c589e5bdc261350fffe6b', 'en_US', 'Bank limit ledger applicant does not exist, unable to continue', 'limit.control.txn.applicant.not.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.not.exist' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f99c8f04b842ffdb230ae72a5b2d9d25', 'zh_CN', '银行限额台账申请人与回调租户不一致', 'limit.control.txn.applicant.tenant.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.tenant.mismatch' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '612e2b0c04ae7cc91f4e81ad4d83fe1f', 'en_US', 'Bank limit ledger applicant does not match callback tenant', 'limit.control.txn.applicant.tenant.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.txn.applicant.tenant.mismatch' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '444b3970111b2720156f11d22e2f51d6', 'zh_CN', '不支持的银行限额回调业务类型', 'limit.control.unsupported.callback.biz.type' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.unsupported.callback.biz.type' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ea715b4efb6586a84befee7993c383dd', 'en_US', 'Unsupported bank limit callback business type', 'limit.control.unsupported.callback.biz.type' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.unsupported.callback.biz.type' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1b94ba240f4fd526610d4464bdc595f1', 'zh_CN', '未找到当前业务单据的有效限额占用', 'limit.control.callback.active.txn.not.found' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.active.txn.not.found' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '034f2d0abbf4223b84928c5c17046b1f', 'en_US', 'No valid limit occupation found for the current business document', 'limit.control.callback.active.txn.not.found' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.active.txn.not.found' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'da14d0cc4683f717df52f0807d39d05f', 'zh_CN', '银行限额审批结果属于旧付款尝试，已拒绝处理', 'limit.control.callback.stale.attempt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.stale.attempt' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0a31689a5e07df0844aa8364d8189a0a', 'en_US', 'Bank limit approval result belongs to an old payment attempt and was rejected', 'limit.control.callback.stale.attempt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.stale.attempt' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0b7a916dd76f51bd1cfea1269aa90b22', 'zh_CN', '银行限额审批回调机构与付款限额台账不一致', 'limit.control.callback.bank.org.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.bank.org.mismatch' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '80f699f1dafaea763a4cc43870a95ce2', 'en_US', 'Callback bank organization does not match the payment limit ledger', 'limit.control.callback.bank.org.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.bank.org.mismatch' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ddc50a642d28aeec92fabcc327b4b8dc', 'zh_CN', '未找到当前付款尝试对应的银行限额审批案例', 'limit.control.callback.case.not.found' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.case.not.found' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b18ea9e730b5f794d73a2e855c353a73', 'en_US', 'No bank limit approval case found for the current payment attempt', 'limit.control.callback.case.not.found' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.case.not.found' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8276f3415c512b662d6a082aecceb2da', 'zh_CN', '银行限额审批回调案例号与企业端不一致', 'limit.control.callback.case.no.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.case.no.mismatch' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e8bcd30a75152b67443a99ea190bcd09', 'en_US', 'Callback bank limit case number does not match the enterprise', 'limit.control.callback.case.no.mismatch' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.case.no.mismatch' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a4de6fa98b1c12633692fcb290f1dddc', 'zh_CN', '银行限额审批案例已有不同的最终处理结果', 'limit.control.callback.result.conflict' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.result.conflict' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '63d655ecea704aeac01aa3ee9fa395a9', 'en_US', 'Bank limit approval case already has a different final result', 'limit.control.callback.result.conflict' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.result.conflict' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '18cf43387ced758a3759835acea8b3e2', 'zh_CN', '银行限额审批回调参数不完整', 'limit.control.callback.param.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.param.invalid' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6b29c7e920d3e74070bc173b9eef7a7d', 'en_US', 'Incomplete bank limit approval callback parameters', 'limit.control.callback.param.invalid' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.param.invalid' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9d86cec9ee56a74389dafbea9d3ee248', 'zh_CN', '银行限额审批结果仅支持A同意或R拒绝', 'limit.control.callback.unsupported.result' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.unsupported.result' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd7e16eca243d20776cb2a3cd4cbfd041', 'en_US', 'Bank limit approval result supports only A for approved or R for rejected', 'limit.control.callback.unsupported.result' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.unsupported.result' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f4a48925ef1aee4c2d65dfde27e06e43', 'zh_CN', '银行限额回调eventId已被其他审批结果使用', 'limit.control.callback.event.conflict' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.event.conflict' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c2fabc25864f604dcad6c67f88e23ac7', 'en_US', 'Bank limit callback eventId has been used by another approval result', 'limit.control.callback.event.conflict' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.callback.event.conflict' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ba21a45df94d1a2e9d8e510c175dd439', 'zh_CN', '付款单状态已变化，不能续办银行限额审批结果', 'limit.control.nis.continue.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.continue.status.changed' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7654977cf2ba0fd802a675601f83e92a', 'en_US', 'Payment order status has changed; cannot continue bank limit approval result', 'limit.control.nis.continue.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.continue.status.changed' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e33d999c89f6e63b3454e9cea892c476', 'zh_CN', '付款单结算方式不支持银行限额审批后续办指令', 'limit.control.nis.unsupported.settle.type' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.unsupported.settle.type' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fc185ef00313c95ebf32b64595e88ca1', 'en_US', 'Payment order settlement method does not support continuing instruction after bank limit approval', 'limit.control.nis.unsupported.settle.type' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.unsupported.settle.type' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5c75f83f341ae19c68ba5d1d836e1bb7', 'zh_CN', '付款单状态已变化，不能处理银行限额审批拒绝结果', 'limit.control.nis.reject.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.reject.status.changed' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6c3193f40ca8ffccd725832deee00bb3', 'en_US', 'Payment order status has changed; cannot process bank limit approval rejection result', 'limit.control.nis.reject.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.reject.status.changed' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a635c5c694026f630360d647cb6bc4b5', 'zh_CN', '境外付款单状态已变化，不能续办银行限额审批结果', 'limit.control.fex.continue.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.continue.status.changed' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6aaa3c2b1b03d74a3a6513f14ef7e8e0', 'en_US', 'Foreign payment order status has changed; cannot continue bank limit approval result', 'limit.control.fex.continue.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.continue.status.changed' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'facfa629b902f17b686ab6f827be90cb', 'zh_CN', '境外付款单状态已变化，不能处理银行限额审批拒绝结果', 'limit.control.fex.reject.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.reject.status.changed' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '965bcd9f893060a74d58f4d902b8d2e0', 'en_US', 'Foreign payment order status has changed; cannot process bank limit approval rejection result', 'limit.control.fex.reject.status.changed' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.reject.status.changed' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '45ab702e668ea419a22e5625ebe03f73', 'zh_CN', '境外付款限额校验无法取得当前操作人', 'limit.control.fex.operator.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.operator.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '193255b97a745cb9b97a6c1510aee8a5', 'en_US', 'Current operator cannot be obtained for foreign payment limit validation', 'limit.control.fex.operator.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.operator.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b85eca6e12a5d912955071feafe3daed', 'zh_CN', '境外付款限额校验无法取得币别英文编码', 'limit.control.fex.currency.code.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.currency.code.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2e117083786e19d0a865309a44b6831f', 'en_US', 'Currency English code cannot be obtained for foreign payment limit validation', 'limit.control.fex.currency.code.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.fex.currency.code.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f71727eba762d8e5d0e68b43db9e5297', 'zh_CN', '付款限额校验无法取得当前操作人', 'limit.control.nis.operator.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.operator.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0f37aa24601d67a68689592bfaf73d67', 'en_US', 'Current operator cannot be obtained for payment limit validation', 'limit.control.nis.operator.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.operator.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '184ed9e05b310f80004cbdda543f3c41', 'zh_CN', '付款限额校验无法取得币别英文编码', 'limit.control.nis.currency.code.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.currency.code.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ea8ca113efb7cd60388fb7a4a10153da', 'en_US', 'Currency English code cannot be obtained for payment limit validation', 'limit.control.nis.currency.code.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.nis.currency.code.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b656215f17a80e13e20524853a61bd45', 'zh_CN', '自动付款限额校验无法取得付款单申请人', 'limit.control.auto.pay.applicant.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.auto.pay.applicant.missing' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e1286c7ede8321656200b8ebfb28856b', 'en_US', 'Payment order applicant cannot be obtained for automatic payment limit validation', 'limit.control.auto.pay.applicant.missing' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.auto.pay.applicant.missing' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a5b7ccd556815101a15e6584a4fd019e', 'zh_CN', '费用报销存在等待银行结果的明细，请待交易结果明确后再打回', 'limit.control.expense.wait.result' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.expense.wait.result' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6718dcf4e980c13dd3452c35c0696da8', 'en_US', 'Expense reimbursement contains details waiting for bank result; cannot reject until the transaction result is clear', 'limit.control.expense.wait.result' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.expense.wait.result' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5f06d1c2ec638e7239f43b52bf4838f3', 'zh_CN', '报销成功明细金额超过汇总付款金额，不能打回', 'limit.control.expense.success.amount.exceeds' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.expense.success.amount.exceeds' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f342c29e59ce3e1333f6bb95f448f53e', 'en_US', 'Successful reimbursement detail amount exceeds the summary payment amount; cannot reject', 'limit.control.expense.success.amount.exceeds' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'limit.control.expense.success.amount.exceeds' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '933bf84df19641c5a4f908834de01d6c', 'zh_CN', '支付限额拦截', 'tr.limit.interceptTitle' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.interceptTitle' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '41eaf381c39a48c081aa086b34ba4a75', 'en_US', 'Payment Limit Interception', 'tr.limit.interceptTitle' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.interceptTitle' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a7f98bafb69e40168524613a16f53a97', 'zh_CN', '本笔支付金额超出限额，系统已自动拦截', 'tr.limit.interceptMessage' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.interceptMessage' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4273c68e34844472a44f34db1843209f', 'en_US', 'This payment exceeds the limit and has been automatically blocked by the system.', 'tr.limit.interceptMessage' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.interceptMessage' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'cf0d599af23f40e08ad27839492e45bc', 'zh_CN', '批量提交存在支付金额超出限额，系统已自动拦截', 'tr.limit.batchInterceptMessage' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.batchInterceptMessage' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2451ce31d79741dfbb1056c14533aec5', 'en_US', 'The batch contains payments that exceed the limit and have been automatically blocked by the system.', 'tr.limit.batchInterceptMessage' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.batchInterceptMessage' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a7cf32a804f4450799be773017f038bd', 'zh_CN', '超额策略', 'tr.limit.excessStrategy' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessStrategy' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f9bf3fa273254989816cab47ce33677e', 'en_US', 'Excess Strategy', 'tr.limit.excessStrategy' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessStrategy' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1d12985ca8bb4a03859422dc028f9b56', 'zh_CN', '超额拦截（拒绝支付）', 'tr.limit.excessStrategyReject' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessStrategyReject' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9889ca01d889496db5b2a90e88392e2e', 'en_US', 'Block excess (reject payment)', 'tr.limit.excessStrategyReject' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.excessStrategyReject' AND LANG_CODE = 'en_US');
##GOGO

-- XXL-JOB：内管服务执行器
INSERT INTO `xxl_job_group`
    (`app_name`, `title`, `address_type`, `address_list`, `update_time`, `TENANT_ID`)
SELECT 'bytter-saas-management', 'manageJob', 0, '', NOW(), '000' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM `xxl_job_group`
     WHERE `app_name` = 'bytter-saas-management'
     LIMIT 1
);
##GOGO

-- XXL-JOB：支付限额历史用量补录（默认停止，启用后每5分钟每批处理100条）
INSERT INTO `xxl_job_info`
    (`job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`,
     `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`,
     `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`,
     `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`,
     `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`, `TENANT_ID`)
SELECT (SELECT MIN(`id`) FROM `xxl_job_group` WHERE `app_name` = 'bytter-saas-management'),
       '支付限额历史用量补录',
       NOW(),
       NOW(),
       'XXL',
       '',
       'CRON',
       '0 */5 * * * ?',
       'DO_NOTHING',
       'FIRST',
       'PaymentLimitUsageBackfillJob',
       '100',
       'SERIAL_EXECUTION',
       0,
       0,
       'BEAN',
       '',
       'GLUE代码初始化',
       NOW(),
       '',
       0,
       0,
       0,
       '000'
  FROM DUAL
 WHERE NOT EXISTS (
    SELECT 1 FROM `xxl_job_info`
     WHERE `executor_handler` = 'PaymentLimitUsageBackfillJob'
     LIMIT 1
 );
##GOGO

-- XXL-JOB：为管理员授予支付限额历史用量补录任务读写权限
INSERT INTO `xxl_job_permission`
    (`job_id`, `user_id`, `permission_type`, `create_by`, `create_time`,
     `update_by`, `update_time`, `is_deleted`, `TENANT_ID`)
SELECT j.`id`, u.`id`, 2, 'admin', NOW(), '', NULL, '0', u.`TENANT_ID`
  FROM `xxl_job_info` j
  JOIN `xxl_job_user` u ON u.`username` = 'admin'
 WHERE j.`executor_handler` = 'PaymentLimitUsageBackfillJob'
   AND NOT EXISTS (
       SELECT 1
         FROM `xxl_job_permission` p
        WHERE p.`job_id` = j.`id`
          AND p.`user_id` = u.`id`
        LIMIT 1
   );
##GOGO

-- 支付限额拦截弹窗：拦截详情小标题
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bd5c6a4bd07141b585910102d73de1b7', 'zh_CN', '拦截详情', 'tr.limit.interceptDetail' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.interceptDetail' AND LANG_CODE = 'zh_CN');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd02164e37b9e413a91f27e98139649e6', 'en_US', 'Interception Details', 'tr.limit.interceptDetail' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'tr.limit.interceptDetail' AND LANG_CODE = 'en_US');
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6', 'zh_CN', '机构下存在签约客户，不允许删除！', 'sys.tenant.signed.delete.error' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'sys.tenant.signed.delete.error' AND LANG_CODE = 'zh_CN' LIMIT 1);
##GOGO

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6', 'en_US', 'The institution has signed customers and cannot be deleted.', 'sys.tenant.signed.delete.error' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'sys.tenant.signed.delete.error' AND LANG_CODE = 'en_US' LIMIT 1);
##GOGO


-- 内管-机构管理：删除/禁用/变更上级单位 校验弹窗标题（对应企业端 sys.corp.verify.* ，内管缺 manager. 前缀条目）
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ffade5acb4ef468893c38b5fab233060', 'zh_CN', '删除单位校验异常！', 'manager.sys.corp.verify.delete' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='manager.sys.corp.verify.delete' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '30ae34f1c330482fb2e679a4bc1ae2f6', 'en_US', 'Delete corp verification exception!', 'manager.sys.corp.verify.delete' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='manager.sys.corp.verify.delete' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '4a8604a5a62d4545a0e52c71475be20f', 'zh_CN', '禁用单位校验异常！', 'manager.sys.corp.verify.disable' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='manager.sys.corp.verify.disable' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7e74b58a8637487795dc7acab11b51e6', 'en_US', 'Disable corp verification exception!', 'manager.sys.corp.verify.disable' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='manager.sys.corp.verify.disable' AND LANG_CODE = 'en_US' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '5b731d93a77442eca99bb175eaf0a443', 'zh_CN', '变更上级单位校验异常！', 'manager.sys.corp.verify.parentChang' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='manager.sys.corp.verify.parentChang' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '506ac0b61272477facfcc065d52e339e', 'en_US', 'Change parent corp verification exception!', 'manager.sys.corp.verify.parentChang' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='manager.sys.corp.verify.parentChang' AND LANG_CODE = 'en_US' limit 1);
##GOGO