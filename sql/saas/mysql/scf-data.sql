INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182198', 'zh_CN', '开立单位', 'scf.table.issuingUnit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182198' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b9cd4c0a21c2420faa5487df2d052bbd', 'zh_CN', '平台', 'scf.table.platform' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'b9cd4c0a21c2420faa5487df2d052bbd' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f0c7c669576a496895b0b066d01e74e6', 'zh_CN', '融资产品', 'scf.table.financeProduct' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'f0c7c669576a496895b0b066d01e74e6' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '36a9e58918174b5ab8d65c4269798620', 'zh_CN', '债权凭证编号', 'scf.table.creditorCertNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '36a9e58918174b5ab8d65c4269798620' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9b5997a18a494a66995f63a956540a5e', 'zh_CN', '开立金额', 'scf.table.issueAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '9b5997a18a494a66995f63a956540a5e' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd0a29a24a9c74c068f7a0e8c72944c40', 'zh_CN', '开立日期', 'scf.table.issueDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'd0a29a24a9c74c068f7a0e8c72944c40' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e0a5a6a6a22c4b6690a440a029999999', 'zh_CN', '业务状态', 'scf.table.businessStatus' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'e0a5a6a6a22c4b6690a440a029999999' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6dbbae20c30b41a889283dc7b659d9c3', 'zh_CN', '单据状态', 'scf.table.docStatus' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '6dbbae20c30b41a889283dc7b659d9c3' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7c3c152640d44276b767d70660306201', 'zh_CN', '到期日', 'scf.table.maturityDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '7c3c152640d44276b767d70660306201' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a7b4c1b3e1f94e68820c4e5661662615', 'zh_CN', '单据编号', 'scf.table.docNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'a7b4c1b3e1f94e68820c4e5661662615' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b0f0a7425e3b4f619cf067d95a900000', 'zh_CN', '线上线下标志', 'scf.table.onlineOfflineFlag' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'b0f0a7425e3b4f619cf067d95a900000' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c0f0a7425e3b4f619cf067d95a900001', 'zh_CN', '单据来源', 'scf.table.docSource' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c0f0a7425e3b4f619cf067d95a900001' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd0f0a7425e3b4f619cf067d95a900002', 'zh_CN', '核心企业', 'scf.table.coreEnterprise' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'd0f0a7425e3b4f619cf067d95a900002' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e0f0a7425e3b4f619cf067d95a900003', 'zh_CN', '收款账号', 'scf.table.payeeAccount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'e0f0a7425e3b4f619cf067d95a900003' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f0f0a7425e3b4f619cf067d95a900004', 'zh_CN', '收款银行', 'scf.table.payeeBank' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'f0f0a7425e3b4f619cf067d95a900004' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '677ebd59de7548bfaf1e9eec1f6d03ec', 'zh_CN', '基础信息', 'scf.table.basicInfo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '677ebd59de7548bfaf1e9eec1f6d03ec' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '589d20a05a214587b88eb753634f05b3', 'zh_CN', '额度信息', 'scf.table.creditInfo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '589d20a05a214587b88eb753634f05b3' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7f78764f962748c39a5ab01fbb30c8eb', 'zh_CN', '额度编号', 'scf.table.creditNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '7f78764f962748c39a5ab01fbb30c8eb' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd8512cc72a244676b8ecc2bee82a97a7', 'zh_CN', '可用额度', 'scf.table.availableCredit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'd8512cc72a244676b8ecc2bee82a97a7' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'eea86ac2378d42649fb1f7d4d3c95be5', 'zh_CN', '额度名称', 'scf.table.creditName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'eea86ac2378d42649fb1f7d4d3c95be5' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0e48daf411b440078ef83144a508c35a', 'zh_CN', '贸易合同信息', 'scf.table.tradeContractInfo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '0e48daf411b440078ef83144a508c35a' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b09a6759d7b340fab4d9b269ce33cd85', 'zh_CN', '交易合同编号', 'scf.table.contractNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'b09a6759d7b340fab4d9b269ce33cd85' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '19ad9d8c366e4bada1fb7f4c43f24089', 'zh_CN', '交易合同名称', 'scf.table.contractName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.contractName' AND LANG_CODE = 'zh_CN' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '18a3cfff3200488da76de52d2f2a5304', 'zh_CN', '交易合同金额', 'scf.table.contractAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '18a3cfff3200488da76de52d2f2a5304' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3f65d8293b634bee9bef3a6f675e6f28', 'zh_CN', '交易合同日期', 'scf.table.contractDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.contractDate' AND LANG_CODE = 'zh_CN' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ffd244f8df0a45a998939dadc8893bbc', 'zh_CN', '发票信息', 'scf.table.invoiceInfo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'ffd244f8df0a45a998939dadc8893bbc' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a066db3db7b44105bc4cbcbdd55b1da9', 'zh_CN', '兑付结果查询', 'scf.table.paymentResultQuery' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'a066db3db7b44105bc4cbcbdd55b1da9' LIMIT 1);


-- ============================================
-- 多语言初始化（使用指定UUID）
-- 前缀: scf.table.
-- 共48条，对应前48个UUID
-- ============================================

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1388461723604fccbc33827dccbd2a4b', 'zh_CN', '额度单选择', 'scf.table.creditOrderSelect' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '1388461723604fccbc33827dccbd2a4b' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '39abb3bc4e654909866321cf1ea23bac', 'zh_CN', '序号', 'scf.table.serialNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '39abb3bc4e654909866321cf1ea23bac' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4ef6f16c70c64dcc98437d51c1463d98', 'zh_CN', '发票类型', 'scf.table.invoiceType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '4ef6f16c70c64dcc98437d51c1463d98' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ad4e73743bd6489aa58870448aa4aa51', 'zh_CN', '发票号码', 'scf.table.invoiceNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'ad4e73743bd6489aa58870448aa4aa51' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a75739b551844eefbe787c4465d0c5e4', 'zh_CN', '发票代码', 'scf.table.invoiceCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'a75739b551844eefbe787c4465d0c5e4' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7023bfbaabe84208afebfb2a9a0b92ac', 'zh_CN', '开票日期', 'scf.table.invoiceDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '7023bfbaabe84208afebfb2a9a0b92ac' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'afaee821a8ad46179cab2278e29de120', 'zh_CN', '币种', 'scf.table.currency' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'afaee821a8ad46179cab2278e29de120' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b9f806b767004734a020abe6f379fb65', 'zh_CN', '价税合计', 'scf.table.totalPriceTax' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'b9f806b767004734a020abe6f379fb65' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4f527ffe6c254bf2b2545f476bfe0f53', 'zh_CN', '税率', 'scf.table.taxRate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '4f527ffe6c254bf2b2545f476bfe0f53' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6b04803a2ceb4c39907443606ca5508d', 'zh_CN', '税额', 'scf.table.taxAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '6b04803a2ceb4c39907443606ca5508d' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '4d4449a972184f1b9dff25439398afe5', 'zh_CN', '金额', 'scf.table.amount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '4d4449a972184f1b9dff25439398afe5' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '96b588ba48b04e00a880c5ce27416300', 'zh_CN', '销方名称', 'scf.table.sellerName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '96b588ba48b04e00a880c5ce27416300' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3817085a19f84c29b14cc843a392752e', 'zh_CN', '购方名称', 'scf.table.buyerName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '3817085a19f84c29b14cc843a392752e' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '039adaa128cc4f86a378ece14ce94d31', 'zh_CN', '发票附件', 'scf.table.invoiceAttachment' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '039adaa128cc4f86a378ece14ce94d31' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f56748de1d3246e593ad1e126231ae87', 'zh_CN', '交易合同附件', 'scf.table.contractAttachment' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'f56748de1d3246e593ad1e126231ae87' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b4d66b0dcdea4a1880346fe0d022b25c', 'zh_CN', '授信机构名称', 'scf.table.creditOrgName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'b4d66b0dcdea4a1880346fe0d022b25c' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '506720888ff047f8a7b4fb04ef5f819c', 'zh_CN', '额度类型', 'scf.table.creditType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '506720888ff047f8a7b4fb04ef5f819c' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd720907540674b4d9b442d8de5010f55', 'zh_CN', '担保机构名称', 'scf.table.guaranteeOrgName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'd720907540674b4d9b442d8de5010f55' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd74e9b62d6f24da2b3a05e8950b9e00b', 'zh_CN', '是否循环使用', 'scf.table.isRevolving' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'd74e9b62d6f24da2b3a05e8950b9e00b' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f994f3579c7e4e9db0db1668fc99df6d', 'zh_CN', '兑付金额', 'scf.table.paymentAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'f994f3579c7e4e9db0db1668fc99df6d' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1543b34304314a1daa8dfbd56a879e62', 'zh_CN', '兑付记录编号', 'scf.table.paymentRecordNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '1543b34304314a1daa8dfbd56a879e62' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8d1e21766c5c4d62a40d63207d810fd3', 'zh_CN', '付款日', 'scf.table.paymentDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '8d1e21766c5c4d62a40d63207d810fd3' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7d811fbc52e74adcbf163ad27e04b6aa', 'zh_CN', '兑付单号', 'scf.table.paymentOrderNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '7d811fbc52e74adcbf163ad27e04b6aa' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e665b84b9f5145e0bd1e0f5d1f075081', 'zh_CN', '付款账号', 'scf.table.payAccount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'e665b84b9f5145e0bd1e0f5d1f075081' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '828312ed27984892aed1d981df6c43d4', 'zh_CN', '供应商', 'scf.table.supplier' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '828312ed27984892aed1d981df6c43d4' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0bd2b25803354f3aa4e2772df2d15cc8', 'zh_CN', '兑付信息', 'scf.table.paymentInfo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '0bd2b25803354f3aa4e2772df2d15cc8' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6a9542d22bb247468ce696d99e00ea51', 'zh_CN', '签收日期', 'scf.table.signDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.signDate' AND LANG_CODE = 'zh_CN' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '83e47fca08ff4f1293cb3839f7d7859b', 'zh_CN', '待签收凭证查询', 'scf.table.pendingSignCertQuery' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '83e47fca08ff4f1293cb3839f7d7859b' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '686d6ca86a3d4fb993c8822e8c3fc0b1', 'zh_CN', '签收管理', 'scf.table.signManage' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '686d6ca86a3d4fb993c8822e8c3fc0b1' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7b127974ab5d4387bd57f167de84c8ba', 'zh_CN', '签收', 'scf.table.sign' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '7b127974ab5d4387bd57f167de84c8ba' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7105d6d8bdc546f0aa2ffbaedcbc3ace', 'zh_CN', '拒签', 'scf.table.rejectSign' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '7105d6d8bdc546f0aa2ffbaedcbc3ace' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '2040466c022643918709dfb738324a98', 'zh_CN', '批量转让', 'scf.table.batchTransfer' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '2040466c022643918709dfb738324a98' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3bc43553cbe246f8ada6a1314345803d', 'zh_CN', '交易结果查询', 'scf.table.tradeResultQuery' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '3bc43553cbe246f8ada6a1314345803d' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '194e39fb534c4c0da8ea5046d2034ff8', 'zh_CN', '作废', 'scf.table.cancel' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '194e39fb534c4c0da8ea5046d2034ff8' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '6bed1f429f354edcb004157b3ed4adef', 'zh_CN', '融资结果查询', 'scf.table.financeResultQuery' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '6bed1f429f354edcb004157b3ed4adef' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a000385e11884b50a051aae6a012d087', 'zh_CN', '到期收款查询', 'scf.table.maturityCollectionQuery' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'a000385e11884b50a051aae6a012d087' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'f12b48abee924b97ae260f7df80566e0', 'zh_CN', '转让', 'scf.table.transfer' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'f12b48abee924b97ae260f7df80566e0' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '8161377313d94604a74050cbda7fa15e', 'zh_CN', '融资', 'scf.table.finance' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '8161377313d94604a74050cbda7fa15e' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7e4eb8f76572440e83923c2ef72b0f75', 'zh_CN', '收款', 'scf.table.collection' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '7e4eb8f76572440e83923c2ef72b0f75' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a0aec908175245c2adde7e06b8709a0e', 'zh_CN', '补录', 'scf.table.supplementaryEntry' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'a0aec908175245c2adde7e06b8709a0e' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a99cc43ffb55404393c61426e98c9945', 'zh_CN', '凭证类别', 'scf.table.certType' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'a99cc43ffb55404393c61426e98c9945' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '50440fb89d8f49e3a2165858b485fd52', 'zh_CN', '凭证金额', 'scf.table.certAmount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '50440fb89d8f49e3a2165858b485fd52' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7d33824c01d746c6a3a7aa65e725c430', 'zh_CN', '根凭证编号', 'scf.table.rootCertNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '7d33824c01d746c6a3a7aa65e725c430' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0ab059c811cd49c4a7f4fbe50fd5a14b', 'zh_CN', '上级凭证编号', 'scf.table.parentCertNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '0ab059c811cd49c4a7f4fbe50fd5a14b' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'e258650743144cdfb6e2c260edccb684', 'zh_CN', '是否允许拆分', 'scf.table.isAllowSplit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'e258650743144cdfb6e2c260edccb684' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a9a5745ee4324896b541dd1c10507c72', 'zh_CN', '转让记录编号', 'scf.table.transferRecordNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'a9a5745ee4324896b541dd1c10507c72' LIMIT 1);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '0fdd2001258c4826a76abb780bf7e0e9', 'zh_CN', '融资单号', 'scf.table.financeOrderNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '0fdd2001258c4826a76abb780bf7e0e9' LIMIT 1);


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '3f200001258c4826a76abb780bf7e0e9', 'zh_CN', '传输数据ID为空', 'scf.data.id.null' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.data.id.null' AND LANG_CODE = 'zh_CN' LIMIT 1);


-- 应付债权业务状态字典分组
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '76ae7188d8f64487a058991d505ea2f4', 'scf.payable.approvalStatus', '应付债权审批状态' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = '76ae7188d8f64487a058991d505ea2f4' LIMIT 1
);

-- 1. 暂存 (0)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '7150bf637c944fd18cd8d30309b8e4db', '0', '暂存', NULL, '76ae7188d8f64487a058991d505ea2f4', 1
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '7150bf637c944fd18cd8d30309b8e4db' LIMIT 1
);

-- 2. 打回 (-10)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'e83f1da0b1e74ec5b016091d28b6df7b', '-10', '打回', NULL, '76ae7188d8f64487a058991d505ea2f4', 2
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = 'e83f1da0b1e74ec5b016091d28b6df7b' LIMIT 1
);

-- 3. 已作废 (-2)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '608b32d32115482ca8bbc04476a5609a', '-2', '已作废', NULL, '76ae7188d8f64487a058991d505ea2f4', 3
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '608b32d32115482ca8bbc04476a5609a' LIMIT 1
);

-- 4. 审批中 (11)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '0bc8525af3e1423a88c11b8c1396751a', '11', '审批中', NULL, '76ae7188d8f64487a058991d505ea2f4', 4
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '0bc8525af3e1423a88c11b8c1396751a' LIMIT 1
);

-- 5. 审批通过 (95)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '97739f2be1c6456b9663880ae5f69f68', '95', '审批通过', NULL, '76ae7188d8f64487a058991d505ea2f4', 5
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '97739f2be1c6456b9663880ae5f69f68' LIMIT 1
);

-- 存量库矫正（幂等，可重复执行）：
-- a) 已存在的老字典行名称改为通用名称（INSERT ... WHERE NOT EXISTS 按 ID 判断，不会覆盖老名称）
UPDATE sys_dict SET TYPE_NAME = '打回' WHERE ID = 'e83f1da0b1e74ec5b016091d28b6df7b';
UPDATE sys_dict SET TYPE_NAME = '审批中' WHERE ID = '0bc8525af3e1423a88c11b8c1396751a';
UPDATE sys_dict SET TYPE_NAME = '审批通过' WHERE ID = '97739f2be1c6456b9663880ae5f69f68';
-- b) 删除不再维护的字典值：
--    -11 兑付打回 / 12 兑付审批中 / 96 兑付审批通过（码值仍会被后端写入 approval_status，
--    显示名统一由 ScfPayableVoucherConstant.PROCESS_STATUS_MAP 按“打回/审批中/审批通过”输出）
--    -3 已删除（代码中无任何写入）
DELETE FROM sys_dict WHERE ID IN ('a1061f4010c947df975370fc8ea97150', 'fcdc72e25af04a4182f3dcc4b762dfd9', '17e3d04f28d34cdebdcccffdc28b227d', 'bc855e247ec64eeeb19f5cc5463be005');


-- ============================================================
-- 2. 新增字典组：应付债权单据状态 (scf.payable.DocumentStatus)
--    组ID: 9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d6e7f
-- ============================================================

INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 'scf.payable.voucherStatus', '应付债权业务状态'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_group WHERE ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d' LIMIT 1
);

-- 未开立 (SORT=1)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'b2a1c3d4e5f6a7b8c9d0e1f2a3b4c5d6', '0', '未开立', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'b2a1c3d4e5f6a7b8c9d0e1f2a3b4c5d6' LIMIT 1);

-- 开立中 (SORT=2)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7', '1', '开立中', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 2
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7');

-- 已开立 (SORT=3)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8', '2', '已开立', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 3
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8' LIMIT 1);

-- 已拒签 (SORT=4)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'd4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9', '3', '已拒签', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 4
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'd4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9' LIMIT 1);

-- 已撤销 (SORT=5)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0', '4', '已撤销', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 5
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0' LIMIT 1);

-- 已兑付 (SORT=6)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1', '5', '已兑付', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 6
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1' LIMIT 1);

-- 兑付中 (SORT=7)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2', '6', '兑付中', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 7
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2' LIMIT 1);

-- 已作废 (SORT=8)
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3', '7', '已作废', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 8
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = 'b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3' LIMIT 1);

-- 业务状态字典只保留 0~7 共 8 条（未开立/开立中/已开立/已拒签/已撤销/已兑付/兑付中/已作废），
-- 8 待兑付不再维护，存量库执行下面的 DELETE 清理
DELETE FROM sys_dict WHERE ID = 'c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4';

-- 1. 插入字典分组：单据审批状态
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT 'f2e89d7c3b6a41509ceb74321dc09651', 'scf.rec.approvalStatus', '单据审批状态' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = 'f2e89d7c3b6a41509ceb74321dc09651' LIMIT 1
);

-- ====================== 签收单据审批状态 ======================
-- 开立暂存
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc01', 'open-draft', '签收暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 10
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc01' limit 1);

-- 开立审批中
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc02', 'open-approval-ing', '签收审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 20
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc02' limit 1);

-- 开立审批通过
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc03', 'open-approval-pass', '签收审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 30
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc03' limit 1);

-- 开立打回
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc04', 'open-rejected', '签收打回', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 40
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc04' limit 1);

-- 开立已作废
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc05', 'open-cancelled', '签收已作废', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 50
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc05' limit 1);

-- ====================== 融资单据审批状态 ======================
-- 融资暂存
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc11', 'loan-draft', '融资暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 60
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc11' limit 1);

-- 融资审批中
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc12', 'loan-approval-ing', '融资审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 70
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc12' limit 1);

-- 融资审批通过
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc13', 'loan-approval-pass', '融资审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 80
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc13' limit 1);

-- ====================== 转让单据审批状态 ======================
-- 转让暂存
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc21', 'transfer-draft', '转让暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 110
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc21' limit 1);

-- 转让审批中
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc22', 'transfer-approval-ing', '转让审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 120
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc22' limit 1);

-- 转让审批通过
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc23', 'transfer-approval-pass', '转让审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 130
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc23' limit 1);


-- ====================== 提示付款审批状态 ======================
-- 提示付款暂存
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc31', 'payment-draft', '提示付款暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 160
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc31' limit 1);

-- 提示付款审批中
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc32', 'payment-approval-ing', '提示付款审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 170
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc32' limit 1);

-- 提示付款审批通过
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '001a112233445566778899aa00bbcc33', 'payment-approval-pass', '提示付款审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 180
from dual where not exists (select 1 from sys_dict where ID = '001a112233445566778899aa00bbcc33' limit 1);

-- 审批状态统一为通用五态，清理旧的按业务场景拆分状态
DELETE FROM sys_dict
WHERE TYPE_GROUP_ID = 'f2e89d7c3b6a41509ceb74321dc09651'
  AND TYPE_CODE IN ('open-draft','open-approval-ing','open-approval-pass','open-rejected','open-cancelled',
                    'loan-draft','loan-approval-ing','loan-approval-pass',
                    'transfer-draft','transfer-approval-ing','transfer-approval-pass',
                    'payment-draft','payment-approval-ing','payment-approval-pass');
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '001a112233445566778899aa00bbcc01', 'draft', '暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 10 FROM DUAL;
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '001a112233445566778899aa00bbcc02', 'approval-ing', '审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 20 FROM DUAL;
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '001a112233445566778899aa00bbcc03', 'rejected', '打回', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 30 FROM DUAL;
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '001a112233445566778899aa00bbcc04', 'approval-pass', '审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 40 FROM DUAL;
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '001a112233445566778899aa00bbcc05', 'cancelled', '已作废', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 50 FROM DUAL;


-- 1. 插入字典分组：业务状态
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '8d72f90c456e41b396ac7821ef5034b6', 'scf.rec.businessStatus', '业务状态' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = '8d72f90c456e41b396ac7821ef5034b6' LIMIT 1
);

-- ====================== 应收债权凭证业务状态 ======================
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc01', 'unsigned', '未签收', NULL, '8d72f90c456e41b396ac7821ef5034b6', 10 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc01' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc02', 'sign_ing', '签收中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 20 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc02' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc03', 'signed', '已签收', NULL, '8d72f90c456e41b396ac7821ef5034b6', 30 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc03' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc04', 'rejected', '已拒签', NULL, '8d72f90c456e41b396ac7821ef5034b6', 40 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc04' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc05', 'cancel', '已作废', NULL, '8d72f90c456e41b396ac7821ef5034b6', 50 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc05' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc06', 'split', '已拆分', NULL, '8d72f90c456e41b396ac7821ef5034b6', 60 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc06' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc07', 'un_transfer', '未转让', NULL, '8d72f90c456e41b396ac7821ef5034b6', 70 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc07' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc08', 'transfer_ing', '转让中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 80 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc08' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc09', 'transferred', '已转让', NULL, '8d72f90c456e41b396ac7821ef5034b6', 90 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc09' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc99', 'transfer_failed', '转让失败', NULL, '8d72f90c456e41b396ac7821ef5034b6', 90 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc99' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc10', 'un_loan', '未融资', NULL, '8d72f90c456e41b396ac7821ef5034b6', 100 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc10' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc11', 'loan_ing', '融资中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 110 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc11' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc12', 'loan_success', '已融资', NULL, '8d72f90c456e41b396ac7821ef5034b6', 120 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc12' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc13', 'loan_failed', '融资失败', NULL, '8d72f90c456e41b396ac7821ef5034b6', 130 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc13' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc14', 'un_payment', '未收款', NULL, '8d72f90c456e41b396ac7821ef5034b6', 140 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc14' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc15', 'payment_ing', '收款中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 150 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc15' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc16', 'payment_success', '已收款', NULL, '8d72f90c456e41b396ac7821ef5034b6', 160 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc16' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc17', 'payment_failed', '收款失败', NULL, '8d72f90c456e41b396ac7821ef5034b6', 170 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc17' LIMIT 1);

INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9101001122334455667788aa00ffcc18', 'data_supplement', '数据待补录', NULL, '8d72f90c456e41b396ac7821ef5034b6', 180 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc18' LIMIT 1);

DELETE FROM sys_dict
WHERE TYPE_GROUP_ID = '8d72f90c456e41b396ac7821ef5034b6'
  AND TYPE_CODE IN ('sign_ing','transfer_failed','loan_failed','payment_failed');
UPDATE sys_dict SET TYPE_NAME = '待转让'
WHERE TYPE_GROUP_ID = '8d72f90c456e41b396ac7821ef5034b6' AND TYPE_CODE = 'un_transfer';
UPDATE sys_dict SET TYPE_NAME = '待融资'
WHERE TYPE_GROUP_ID = '8d72f90c456e41b396ac7821ef5034b6' AND TYPE_CODE = 'un_loan';
UPDATE sys_dict SET TYPE_NAME = '待收款'
WHERE TYPE_GROUP_ID = '8d72f90c456e41b396ac7821ef5034b6' AND TYPE_CODE = 'un_payment';

-- ====================== 单据来源 ======================
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT 'a7d9f2c0e4b681357ce290418dc73502', 'scf.voucher.source', '单据来源' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = 'a7d9f2c0e4b681357ce290418dc73502' LIMIT 1
);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '91c375802df44961a0eb5c287d0f641e', 'MANUAL', '手工新增', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 10
from dual where not exists (select 1 from sys_dict where ID = '91c375802df44961a0eb5c287d0f641e' limit 1);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'b01c2d3e5c4f5a60718293a4b6e8f901', 'PAYMENT', '付款单', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 15
from dual where not exists (select 1 from sys_dict where ID = 'b01c2d3e5c4f5a60718293a4b6e8f901' limit 1);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '25fb601879dc43ae90c517482efd306b', 'ERP', 'ERP', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 20
from dual where not exists (select 1 from sys_dict where ID = '25fb601879dc43ae90c517482efd306b' limit 1);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '740e8d1c36af4259bcf02915d78634eb', 'SUPPLY_PLATFORM', '供应链金融平台', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 30
from dual where not exists (select 1 from sys_dict where ID = '740e8d1c36af4259bcf02915d78634eb' limit 1);

-- ====================== 融资付息方式 ======================
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT 'b7c3d5e6f7890123456789abcdef4567', 'scf.voucher.interestMode', '融资付息方式' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = 'b7c3d5e6f7890123456789abcdef4567' LIMIT 1
);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'c8d4e6f7a890123456789abcdef56789', '1', '卖方付息', NULL, 'b7c3d5e6f7890123456789abcdef4567', 10
from dual where not exists (select 1 from sys_dict where ID = 'c8d4e6f7a890123456789abcdef56789' limit 1);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'd9e5f7a8b90123456789abcdef67890', '2', '买方付息', NULL, 'b7c3d5e6f7890123456789abcdef4567', 20
from dual where not exists (select 1 from sys_dict where ID = 'd9e5f7a8b90123456789abcdef67890' limit 1);

-- ====================== 机构类型 ======================
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT 'b7c3d5e6f7890987456789abcdef4567', 'scf.fund.institution.type', '机构类型' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = 'b7c3d5e6f7890987456789abcdef4567' LIMIT 1
);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '56789c8d4e6f7a890123456789abcdef', '01', '保理公司', NULL, 'b7c3d5e6f7890987456789abcdef4567', 10
from dual where not exists (select 1 from sys_dict where ID = '56789c8d4e6f7a890123456789abcdef' limit 1);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '67890d9e5f7a8b90123456789abcdef', '02', '银行', NULL, 'b7c3d5e6f7890987456789abcdef4567', 20
from dual where not exists (select 1 from sys_dict where ID = '67890d9e5f7a8b90123456789abcdef' limit 1);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '67abcdef890d9e5f7a8b90123456789', '03', '其他', NULL, 'b7c3d5e6f7890987456789abcdef4567', 30
from dual where not exists (select 1 from sys_dict where ID = '67abcdef890d9e5f7a8b90123456789' limit 1);


INSERT INTO sys_dict_group  (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '5ea76ae7188d8f64487a058991d502f4', 'scf.common.onlineSign', '债权凭证线上线下标识' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID ='5ea76ae7188d8f64487a058991d502f4' LIMIT 1
);


-- 线下
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '64ea46bc26e9ef0ef742bea136ae17ed', '0', '线下', NULL, '5ea76ae7188d8f64487a058991d502f4', 1
from dual where not exists (select 1 from sys_dict where ID = '64ea46bc26e9ef0ef742bea136ae17ed' limit 1);

-- 线上
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'aaa2d2efffa5b6c7d8e9f0c3d4e5f6a7', '1', '线上', NULL, '5ea76ae7188d8f64487a058991d502f4', 2
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM sys_dict WHERE ID = 'aaa2d2efffa5b6c7d8e9f0c3d4e5f6a7' LIMIT 1);

-- 凭证类别
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '5f82c9d17e3b4068a21f9c7430de1b65', 'scf.voucher.type', '凭证类别' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = '5f82c9d17e3b4068a21f9c7430de1b65' LIMIT 1
);

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '30ad67c29f1e4580b27d9c1e540f862b', 'open', '开立凭证', NULL, '5f82c9d17e3b4068a21f9c7430de1b65', 10
from dual where not exists (select 1 from sys_dict where ID = '30ad67c29f1e4580b27d9c1e540f862b' limit 1);


insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '72ef91c03d5b468a802f1c9d357e064b', 'transfer', '流转凭证', NULL, '5f82c9d17e3b4068a21f9c7430de1b65', 20
from dual where not exists (select 1 from sys_dict where ID = '72ef91c03d5b468a802f1c9d357e064b' limit 1);

-- 供应链指令状态字典分组
INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
SELECT '740b62c37c8f4dc89226389e426e6718', 'scf.exc.voucherStat', '供应链指令状态' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_group WHERE ID = '740b62c37c8f4dc89226389e426e6718' LIMIT 1
);


-- 待发送 (TYPE_CODE = '1')
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'e47e945648494d3b81e6eb76e6d86098', '1', '待发送', NULL, '740b62c37c8f4dc89226389e426e6718', 2
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = 'e47e945648494d3b81e6eb76e6d86098'
);

-- 发送失败 (TYPE_CODE = '2')
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '9b2fb960082342d9a11e7ef79bb23a6c', '2', '发送失败', NULL, '740b62c37c8f4dc89226389e426e6718', 3
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '9b2fb960082342d9a11e7ef79bb23a6c'
);

-- 发送成功 (TYPE_CODE = '3')
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '1a13be1680a24406bf5f8e77d8a9e66e', '3', '发送成功', NULL, '740b62c37c8f4dc89226389e426e6718', 4
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '1a13be1680a24406bf5f8e77d8a9e66e'
);

-- 打回 (TYPE_CODE = '4')
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '4a26448a2efd4e048ab1334075eb4ab2', '4', '打回', NULL, '740b62c37c8f4dc89226389e426e6718', 5
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '4a26448a2efd4e048ab1334075eb4ab2'
);

-- 等待结果 (TYPE_CODE = '5')
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'a6bd2df07d014459a033f064ed83bae6', '5', '等待结果', NULL, '740b62c37c8f4dc89226389e426e6718', 6
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = 'a6bd2df07d014459a033f064ed83bae6'
);

-- 交易成功 (TYPE_CODE = '6')
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT '719c3d2761db4c30852806c42a001d10', '6', '交易成功', NULL, '740b62c37c8f4dc89226389e426e6718', 7
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = '719c3d2761db4c30852806c42a001d10'
);

-- 交易失败 (TYPE_CODE = '7')
INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
SELECT 'f55f95d70da341d2a966936f1210c084', '7', '交易失败', NULL, '740b62c37c8f4dc89226389e426e6718', 8
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict WHERE ID = 'f55f95d70da341d2a966936f1210c084'
);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'af79bfc50cf54bc38e94825631858a1e', 'zh_CN', '指令发送模式', 'scf.exc.instructionMode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'af79bfc50cf54bc38e94825631858a1e' LIMIT 1);


-- 1. 资金方编码不能为空
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5081e36ddf644193aab52a3987ac5952', 'zh_CN', '资金方编码不能为空', 'scf.fund.provider.code.null' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '5081e36ddf644193aab52a3987ac5952' LIMIT 1);

-- 2. 资金方编码已存在
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1a05e512078f41179734318da9ddeb15', 'zh_CN', '资金方编码已存在', 'scf.fund.provider.code.exist' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '1a05e512078f41179734318da9ddeb15' LIMIT 1);

-- 3. 机构类型不能为空
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'bf824b71897d49e49fcbb4c385c1cc7c', 'zh_CN', '机构类型不能为空', 'scf.fund.provider.org.type.null' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'bf824b71897d49e49fcbb4c385c1cc7c' LIMIT 1);

-- 4. 资金方名称不能为空
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '9d9598761de54c0bb8133fb148b32b1e', 'zh_CN', '资金方名称不能为空', 'scf.fund.provider.name.null' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '9d9598761de54c0bb8133fb148b32b1e' LIMIT 1);

-- 5. 银行行号不能为空
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '08cb87d4e51a4b15a9eda46ea3991021', 'zh_CN', '银行行号不能为空', 'scf.fund.provider.bank.code.null' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '08cb87d4e51a4b15a9eda46ea3991021' LIMIT 1);

-- 6. 该供应链金融平台未配置应付债权凭证页面模版，请联系管理端配置相应模版
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '518174d7efb241f8b97b45e2d7c4b617', 'zh_CN', '该供应链金融平台未配置应付债权凭证页面模版，请联系管理端配置相应模版', 'scf.not.config.platform' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '518174d7efb241f8b97b45e2d7c4b617' LIMIT 1);

-- 7. 传输数据ID为空
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b937932ded8c49e3863e7d9c79ac0513', 'zh_CN', '传输数据ID为空', 'scf.data.id.null' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.data.id.null' AND LANG_CODE = 'zh_CN' LIMIT 1);

-- 8. 只允许业务状态为“开立中”的应付债权凭证进行撤销操作
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'ca3dff51f01848bc8d411c97d39119b9', 'zh_CN', '只允许业务状态为“开立中”的应付债权凭证进行撤销操作', 'scf.data.status.not.allow.revoke' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'ca3dff51f01848bc8d411c97d39119b9' LIMIT 1);

-- 9. 该平台”{0}“在线接口未开通 请联系相应人员进行配置
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '18361ec100254b9db4577d4f87428235', 'zh_CN', '该平台”{0}“在线接口未开通 请联系相应人员进行配置', 'scf.online.interface.not.open' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '18361ec100254b9db4577d4f87428235' LIMIT 1);

-- 10. {0}平台 没有开通“{1}”在线接口 请联系相应人员进行配置
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '92b1a78913f24d6bb9adaa207d26659f', 'zh_CN', '{0}平台 没有开通“{1}”在线接口 请联系相应人员进行配置', 'scf.online.platform.interface.not.open' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '92b1a78913f24d6bb9adaa207d26659f' LIMIT 1);

-- 11. 只允许对已开立的应付债权凭证进行兑付结果查询，当前凭证状态不符合要求
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7891392b1af24d6bb9adaa207d26659f', 'zh_CN', '只允许对已开立的应付债权凭证进行兑付结果查询，当前凭证状态不符合要求', 'scf.voucher.not.opened.for.redemption' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '7891392b1af24d6bb9adaa207d26659f' LIMIT 1);


-- 12. 该操作只允许线上凭证进行,请选择线上凭证
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1a7892b913f24d6bb9adaa207d26659f', 'zh_CN', '该操作只允许线上凭证进行,请选择线上凭证', 'scf.voucher.only.online.operate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = '1a7892b913f24d6bb9adaa207d26659f' LIMIT 1);

-- 13. 该应付凭证存在审批中或已审批的兑付记录，无法重复提交兑付
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '7137fe0ffb54475b8cc0a102a1234caa', 'zh_CN', '该应付凭证存在审批中或已审批的兑付记录，无法重复提交兑付', 'scf.voucher.has.conflict.redemption'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = '7137fe0ffb54475b8cc0a102a1234caa' LIMIT 1
);

-- 14. 该凭证已生成撤销指令，无法重复撤销
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'faf8c10585274e92bacc55b8f74b5c82', 'zh_CN', '该凭证已生成撤销指令，无法重复撤销', 'scf.voucher.has.cancel.instruction'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = 'faf8c10585274e92bacc55b8f74b5c82' LIMIT 1
);

-- 15. 选择的凭证都已生成撤销指令，无法重复撤销
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'fdf2a6ae718e46968a6ddad90918b64c', 'zh_CN', '选择的凭证都已生成撤销指令，无法重复撤销', 'scf.voucher.batch.all.has.cancel.instruction'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = 'fdf2a6ae718e46968a6ddad90918b64c' LIMIT 1
);

-- 16. 该平台已配置过直联接口，请勿重复提交
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'a1b2c3d4e5f64738b9c0d1e2f3a4b5c6', 'zh_CN', '该平台已配置过直联接口，请勿重复提交', 'scf.ent.platform.config.exist'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = 'a1b2c3d4e5f64738b9c0d1e2f3a4b5c6' LIMIT 1
);

-- 17. 债权凭证编号已存在，请勿重复录入
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '5a9e3c7d1b4f6a8e9c2b7d4e1f6a8c93', 'zh_CN', '债权凭证编号已存在，请勿重复录入', 'scf.voucher.code.exist'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = '5a9e3c7d1b4f6a8e9c2b7d4e1f6a8c93' LIMIT 1
);

-- 19. 只允许业务状态为“已开立/已撤销/已拒签”的应付债权凭证进行作废
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'b3f7c1a9d5e2486fa7c9b1e3d5f7a9c1', 'zh_CN', '只允许业务状态为“已开立/已撤销/已拒签”的应付债权凭证进行作废', 'scf.voucher.status.not.allow.cancel'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = 'b3f7c1a9d5e2486fa7c9b1e3d5f7a9c1' LIMIT 1
);

-- 20. 作废原因不能为空
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c4e8d2b1a6f35970b8d1e2f3a4b5c6d7', 'zh_CN', '作废原因不能为空', 'scf.voucher.cancel.reason.required'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = 'c4e8d2b1a6f35970b8d1e2f3a4b5c6d7' LIMIT 1
);

-- 21. 作废时间不能为空
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd5f9e3c2b7a46081c9e2f3a4b5c6d7e8', 'zh_CN', '作废时间不能为空', 'scf.voucher.cancel.date.required'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang WHERE ID = 'd5f9e3c2b7a46081c9e2f3a4b5c6d7e8' LIMIT 1
);

-- 应收债权凭证表 国际化中文配置

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182102', 'zh_CN', '单据编号', 'scf.table.voucherNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182102');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182103', 'zh_CN', '单据来源', 'scf.table.voucherSource' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182103');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182105', 'zh_CN', '债权凭证编号', 'scf.table.voucherCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182105');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182106', 'zh_CN', '凭证金额', 'scf.table.voucherAmt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182106');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182108', 'zh_CN', '平台名称', 'scf.table.platformName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182108');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e4218210b', 'zh_CN', '融资产品名称', 'scf.table.productName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.productName' AND LANG_CODE = 'zh_CN');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182112', 'zh_CN', '持有单位名称', 'scf.table.corpName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182112');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e4218210c', 'zh_CN', '签收日期', 'scf.table.signDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.signDate' AND LANG_CODE = 'zh_CN');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182114', 'zh_CN', '签收操作', 'scf.table.signOperation' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182114');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182116', 'zh_CN', '凭证到期日', 'scf.table.expireDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182116');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182117', 'zh_CN', '凭证业务状态', 'scf.table.voucherStatusName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182117');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182118', 'zh_CN', '单据状态', 'scf.table.approvalStatusName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182118');

UPDATE sys_multi_lang SET LANG_CONTEXT = '单据审批状态'
WHERE LANG_KEY = 'scf.table.approvalStatusName' AND LANG_CODE = 'zh_CN';


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182120', 'zh_CN', '核心企业名称', 'scf.table.coreCompanyName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182120');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182122', 'zh_CN', '币别名称', 'scf.table.curName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182122');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e4218210d', 'zh_CN', '开立/转让企业', 'scf.table.issueTransferCompany' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.issueTransferCompany' AND LANG_CODE = 'zh_CN');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182125', 'zh_CN', '根凭证编号', 'scf.table.sourceVoucherCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182125');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182126', 'zh_CN', '上级凭证编号', 'scf.table.forehandVoucherCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182126');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182127', 'zh_CN', '线上线下标识', 'scf.table.onlineSign' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182127');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182128', 'zh_CN', '是否允许拆分', 'scf.table.allowSplit' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182128');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182129', 'zh_CN', '合同编码', 'scf.table.contractCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182129');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e4218210e', 'zh_CN', '合同名称', 'scf.table.contractName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.contractName' AND LANG_CODE = 'zh_CN');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182131', 'zh_CN', '合同金额', 'scf.table.contractAmt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182131');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e4218210f', 'zh_CN', '合同日期', 'scf.table.contractDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.contractDate' AND LANG_CODE = 'zh_CN');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182135', 'zh_CN', '是否有效', 'scf.table.enable' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182135');

-- ====================== 2. 债权凭证转让信息表 scf_receivable_transfer_info 中文国际化配置 ======================
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182165', 'zh_CN', '凭证转让记录编号', 'scf.table.transferNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182165');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182166', 'zh_CN', '转让批次号', 'scf.table.transferBatchNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182166');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182167', 'zh_CN', '凭证转让状态', 'scf.table.transferStatus' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182167');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182111', 'zh_CN', '融资产品名称', 'scf.table.productName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.productName' AND LANG_CODE = 'zh_CN');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182115', 'zh_CN', '开立/转让单位', 'scf.table.issueTransferCompany' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'scf.table.issueTransferCompany' AND LANG_CODE = 'zh_CN');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182173', 'zh_CN', '接收单位名称', 'scf.table.receiveCompany' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182173');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182175', 'zh_CN', '接收方统一社会信用代码', 'scf.table.receiverCreditCode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182175');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182176', 'zh_CN', '转让日期', 'scf.table.transferDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182176');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182177', 'zh_CN', '转让金额', 'scf.table.transferAmt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182177');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182178', 'zh_CN', '用途', 'scf.table.purpose' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182178');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182179', 'zh_CN', '备注', 'scf.table.remark' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182179');


-- ====================== 3. 融资业务主信息表 scf_loan_info 中文国际化配置（仅新增独有字段） ======================

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182181', 'zh_CN', '融资单号', 'scf.table.loanNo' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182181');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182183', 'zh_CN', '融资企业名称', 'scf.table.loanCompanyName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182183');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182185', 'zh_CN', '资金方名称', 'scf.table.funderName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182185');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182186', 'zh_CN', '融资申请日', 'scf.table.loanApplyDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182186');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182187', 'zh_CN', '融资金额', 'scf.table.loanAmt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182187');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182188', 'zh_CN', '付息方式', 'scf.table.interestMode' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182188');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182189', 'zh_CN', '放款日', 'scf.table.loanLendDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182189');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182190', 'zh_CN', '实际放款日', 'scf.table.loanActualDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182190');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182191', 'zh_CN', '到期日', 'scf.table.loanDueDate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182191');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182192', 'zh_CN', '期限(天)', 'scf.table.loanDeadline' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182192');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182193', 'zh_CN', '融资利率', 'scf.table.loanRate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182193');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182194', 'zh_CN', '利息金额', 'scf.table.loanInterestAmt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182194');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182195', 'zh_CN', '手续费率', 'scf.table.loanHandFeeRate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182195');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182196', 'zh_CN', '手续费', 'scf.table.loanHandFeeAmt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182196');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182197', 'zh_CN', '服务费率', 'scf.table.loanServiceFeeRate' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182197');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e4218210a', 'zh_CN', '服务费', 'scf.table.loanServiceFeeAmt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e4218210a');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182199', 'zh_CN', '融资成本', 'scf.table.loanCostAmt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182199');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182100', 'zh_CN', '融资到账金额', 'scf.table.loanActualAmt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182100');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182101', 'zh_CN', '收款账号', 'scf.table.loanPaymentAccount' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182101');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182104', 'zh_CN', '收款开户名', 'scf.table.loanPaymentAccountName' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182104');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182107', 'zh_CN', '收款开户行', 'scf.table.loanPaymentAccountBank' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182107');


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c243da999d25435a97ca037e42182109', 'zh_CN', '融资状态', 'scf.table.loanStatus' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE ID = 'c243da999d25435a97ca037e42182109');


UPDATE sys_multi_lang
SET LANG_CONTEXT = '凭证在线查询'
WHERE ID = '83e47fca08ff4f1293cb3839f7d7859b';
