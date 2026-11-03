-- ===== table: sys_multi_lang =====
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'c243da999d25435a97ca037e42182198';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('c243da999d25435a97ca037e42182198', 'zh_CN', '开立单位', 'scf.table.issuingUnit');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'b9cd4c0a21c2420faa5487df2d052bbd';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('b9cd4c0a21c2420faa5487df2d052bbd', 'zh_CN', '平台', 'scf.table.platform');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'f0c7c669576a496895b0b066d01e74e6';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('f0c7c669576a496895b0b066d01e74e6', 'zh_CN', '融资产品', 'scf.table.financeProduct');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '36a9e58918174b5ab8d65c4269798620';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('36a9e58918174b5ab8d65c4269798620', 'zh_CN', '债权凭证编号', 'scf.table.creditorCertNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '9b5997a18a494a66995f63a956540a5e';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('9b5997a18a494a66995f63a956540a5e', 'zh_CN', '开立金额', 'scf.table.issueAmount');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'd0a29a24a9c74c068f7a0e8c72944c40';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('d0a29a24a9c74c068f7a0e8c72944c40', 'zh_CN', '开立日期', 'scf.table.issueDate');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'e0a5a6a6a22c4b6690a440a029999999';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('e0a5a6a6a22c4b6690a440a029999999', 'zh_CN', '业务状态', 'scf.table.businessStatus');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '6dbbae20c30b41a889283dc7b659d9c3';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('6dbbae20c30b41a889283dc7b659d9c3', 'zh_CN', '单据状态', 'scf.table.docStatus');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7c3c152640d44276b767d70660306201';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7c3c152640d44276b767d70660306201', 'zh_CN', '到期日', 'scf.table.maturityDate');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'a7b4c1b3e1f94e68820c4e5661662615';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('a7b4c1b3e1f94e68820c4e5661662615', 'zh_CN', '单据编号', 'scf.table.docNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'b0f0a7425e3b4f619cf067d95a900000';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('b0f0a7425e3b4f619cf067d95a900000', 'zh_CN', '线上线下标志', 'scf.table.onlineOfflineFlag');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'c0f0a7425e3b4f619cf067d95a900001';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('c0f0a7425e3b4f619cf067d95a900001', 'zh_CN', '单据来源', 'scf.table.docSource');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'd0f0a7425e3b4f619cf067d95a900002';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('d0f0a7425e3b4f619cf067d95a900002', 'zh_CN', '核心企业', 'scf.table.coreEnterprise');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'e0f0a7425e3b4f619cf067d95a900003';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('e0f0a7425e3b4f619cf067d95a900003', 'zh_CN', '收款账号', 'scf.table.payeeAccount');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'f0f0a7425e3b4f619cf067d95a900004';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('f0f0a7425e3b4f619cf067d95a900004', 'zh_CN', '收款银行', 'scf.table.payeeBank');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '677ebd59de7548bfaf1e9eec1f6d03ec';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('677ebd59de7548bfaf1e9eec1f6d03ec', 'zh_CN', '基础信息', 'scf.table.basicInfo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '589d20a05a214587b88eb753634f05b3';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('589d20a05a214587b88eb753634f05b3', 'zh_CN', '额度信息', 'scf.table.creditInfo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7f78764f962748c39a5ab01fbb30c8eb';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7f78764f962748c39a5ab01fbb30c8eb', 'zh_CN', '额度编号', 'scf.table.creditNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'd8512cc72a244676b8ecc2bee82a97a7';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('d8512cc72a244676b8ecc2bee82a97a7', 'zh_CN', '可用额度', 'scf.table.availableCredit');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'eea86ac2378d42649fb1f7d4d3c95be5';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('eea86ac2378d42649fb1f7d4d3c95be5', 'zh_CN', '额度名称', 'scf.table.creditName');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '0e48daf411b440078ef83144a508c35a';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('0e48daf411b440078ef83144a508c35a', 'zh_CN', '贸易合同信息', 'scf.table.tradeContractInfo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'b09a6759d7b340fab4d9b269ce33cd85';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('b09a6759d7b340fab4d9b269ce33cd85', 'zh_CN', '交易合同编号', 'scf.table.contractNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'scf.table.contractName' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('19ad9d8c366e4bada1fb7f4c43f24089', 'zh_CN', '交易合同名称', 'scf.table.contractName');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '18a3cfff3200488da76de52d2f2a5304';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('18a3cfff3200488da76de52d2f2a5304', 'zh_CN', '交易合同金额', 'scf.table.contractAmount');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'scf.table.contractDate' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('3f65d8293b634bee9bef3a6f675e6f28', 'zh_CN', '交易合同日期', 'scf.table.contractDate');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'ffd244f8df0a45a998939dadc8893bbc';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('ffd244f8df0a45a998939dadc8893bbc', 'zh_CN', '发票信息', 'scf.table.invoiceInfo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'a066db3db7b44105bc4cbcbdd55b1da9';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('a066db3db7b44105bc4cbcbdd55b1da9', 'zh_CN', '兑付结果查询', 'scf.table.paymentResultQuery');
END IF;
commit;
END;
/

-- ============================================
-- 多语言初始化（使用指定UUID）
-- 前缀: scf.table.
-- 共48条，对应前48个UUID
-- ============================================

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '1388461723604fccbc33827dccbd2a4b';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('1388461723604fccbc33827dccbd2a4b', 'zh_CN', '额度单选择', 'scf.table.creditOrderSelect');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '39abb3bc4e654909866321cf1ea23bac';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('39abb3bc4e654909866321cf1ea23bac', 'zh_CN', '序号', 'scf.table.serialNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '4ef6f16c70c64dcc98437d51c1463d98';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('4ef6f16c70c64dcc98437d51c1463d98', 'zh_CN', '发票类型', 'scf.table.invoiceType');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'ad4e73743bd6489aa58870448aa4aa51';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('ad4e73743bd6489aa58870448aa4aa51', 'zh_CN', '发票号码', 'scf.table.invoiceNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'a75739b551844eefbe787c4465d0c5e4';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('a75739b551844eefbe787c4465d0c5e4', 'zh_CN', '发票代码', 'scf.table.invoiceCode');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7023bfbaabe84208afebfb2a9a0b92ac';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7023bfbaabe84208afebfb2a9a0b92ac', 'zh_CN', '开票日期', 'scf.table.invoiceDate');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'afaee821a8ad46179cab2278e29de120';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('afaee821a8ad46179cab2278e29de120', 'zh_CN', '币种', 'scf.table.currency');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'b9f806b767004734a020abe6f379fb65';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('b9f806b767004734a020abe6f379fb65', 'zh_CN', '价税合计', 'scf.table.totalPriceTax');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '4f527ffe6c254bf2b2545f476bfe0f53';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('4f527ffe6c254bf2b2545f476bfe0f53', 'zh_CN', '税率', 'scf.table.taxRate');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '6b04803a2ceb4c39907443606ca5508d';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('6b04803a2ceb4c39907443606ca5508d', 'zh_CN', '税额', 'scf.table.taxAmount');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '4d4449a972184f1b9dff25439398afe5';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('4d4449a972184f1b9dff25439398afe5', 'zh_CN', '金额', 'scf.table.amount');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '96b588ba48b04e00a880c5ce27416300';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('96b588ba48b04e00a880c5ce27416300', 'zh_CN', '销方名称', 'scf.table.sellerName');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '3817085a19f84c29b14cc843a392752e';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('3817085a19f84c29b14cc843a392752e', 'zh_CN', '购方名称', 'scf.table.buyerName');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '039adaa128cc4f86a378ece14ce94d31';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('039adaa128cc4f86a378ece14ce94d31', 'zh_CN', '发票附件', 'scf.table.invoiceAttachment');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'f56748de1d3246e593ad1e126231ae87';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('f56748de1d3246e593ad1e126231ae87', 'zh_CN', '交易合同附件', 'scf.table.contractAttachment');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'b4d66b0dcdea4a1880346fe0d022b25c';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('b4d66b0dcdea4a1880346fe0d022b25c', 'zh_CN', '授信机构名称', 'scf.table.creditOrgName');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '506720888ff047f8a7b4fb04ef5f819c';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('506720888ff047f8a7b4fb04ef5f819c', 'zh_CN', '额度类型', 'scf.table.creditType');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'd720907540674b4d9b442d8de5010f55';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('d720907540674b4d9b442d8de5010f55', 'zh_CN', '担保机构名称', 'scf.table.guaranteeOrgName');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'd74e9b62d6f24da2b3a05e8950b9e00b';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('d74e9b62d6f24da2b3a05e8950b9e00b', 'zh_CN', '是否循环使用', 'scf.table.isRevolving');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'f994f3579c7e4e9db0db1668fc99df6d';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('f994f3579c7e4e9db0db1668fc99df6d', 'zh_CN', '兑付金额', 'scf.table.paymentAmount');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '1543b34304314a1daa8dfbd56a879e62';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('1543b34304314a1daa8dfbd56a879e62', 'zh_CN', '兑付记录编号', 'scf.table.paymentRecordNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '8d1e21766c5c4d62a40d63207d810fd3';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('8d1e21766c5c4d62a40d63207d810fd3', 'zh_CN', '付款日', 'scf.table.paymentDate');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7d811fbc52e74adcbf163ad27e04b6aa';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7d811fbc52e74adcbf163ad27e04b6aa', 'zh_CN', '兑付单号', 'scf.table.paymentOrderNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'e665b84b9f5145e0bd1e0f5d1f075081';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('e665b84b9f5145e0bd1e0f5d1f075081', 'zh_CN', '付款账号', 'scf.table.payAccount');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '828312ed27984892aed1d981df6c43d4';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('828312ed27984892aed1d981df6c43d4', 'zh_CN', '供应商', 'scf.table.supplier');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '0bd2b25803354f3aa4e2772df2d15cc8';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('0bd2b25803354f3aa4e2772df2d15cc8', 'zh_CN', '兑付信息', 'scf.table.paymentInfo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'scf.table.signDate' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('6a9542d22bb247468ce696d99e00ea51', 'zh_CN', '签收日期', 'scf.table.signDate');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '83e47fca08ff4f1293cb3839f7d7859b';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('83e47fca08ff4f1293cb3839f7d7859b', 'zh_CN', '待签收凭证查询', 'scf.table.pendingSignCertQuery');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '686d6ca86a3d4fb993c8822e8c3fc0b1';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('686d6ca86a3d4fb993c8822e8c3fc0b1', 'zh_CN', '签收管理', 'scf.table.signManage');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7b127974ab5d4387bd57f167de84c8ba';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7b127974ab5d4387bd57f167de84c8ba', 'zh_CN', '签收', 'scf.table.sign');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7105d6d8bdc546f0aa2ffbaedcbc3ace';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7105d6d8bdc546f0aa2ffbaedcbc3ace', 'zh_CN', '拒签', 'scf.table.rejectSign');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '2040466c022643918709dfb738324a98';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('2040466c022643918709dfb738324a98', 'zh_CN', '批量转让', 'scf.table.batchTransfer');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '3bc43553cbe246f8ada6a1314345803d';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('3bc43553cbe246f8ada6a1314345803d', 'zh_CN', '交易结果查询', 'scf.table.tradeResultQuery');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '194e39fb534c4c0da8ea5046d2034ff8';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('194e39fb534c4c0da8ea5046d2034ff8', 'zh_CN', '作废', 'scf.table.cancel');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '6bed1f429f354edcb004157b3ed4adef';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('6bed1f429f354edcb004157b3ed4adef', 'zh_CN', '融资结果查询', 'scf.table.financeResultQuery');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'a000385e11884b50a051aae6a012d087';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('a000385e11884b50a051aae6a012d087', 'zh_CN', '到期收款查询', 'scf.table.maturityCollectionQuery');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'f12b48abee924b97ae260f7df80566e0';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('f12b48abee924b97ae260f7df80566e0', 'zh_CN', '转让', 'scf.table.transfer');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '8161377313d94604a74050cbda7fa15e';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('8161377313d94604a74050cbda7fa15e', 'zh_CN', '融资', 'scf.table.finance');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7e4eb8f76572440e83923c2ef72b0f75';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7e4eb8f76572440e83923c2ef72b0f75', 'zh_CN', '收款', 'scf.table.collection');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'a0aec908175245c2adde7e06b8709a0e';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('a0aec908175245c2adde7e06b8709a0e', 'zh_CN', '补录', 'scf.table.supplementaryEntry');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'a99cc43ffb55404393c61426e98c9945';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('a99cc43ffb55404393c61426e98c9945', 'zh_CN', '凭证类别', 'scf.table.certType');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '50440fb89d8f49e3a2165858b485fd52';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('50440fb89d8f49e3a2165858b485fd52', 'zh_CN', '凭证金额', 'scf.table.certAmount');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7d33824c01d746c6a3a7aa65e725c430';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7d33824c01d746c6a3a7aa65e725c430', 'zh_CN', '根凭证编号', 'scf.table.rootCertNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '0ab059c811cd49c4a7f4fbe50fd5a14b';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('0ab059c811cd49c4a7f4fbe50fd5a14b', 'zh_CN', '上级凭证编号', 'scf.table.parentCertNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'e258650743144cdfb6e2c260edccb684';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('e258650743144cdfb6e2c260edccb684', 'zh_CN', '是否允许拆分', 'scf.table.isAllowSplit');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'a9a5745ee4324896b541dd1c10507c72';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('a9a5745ee4324896b541dd1c10507c72', 'zh_CN', '转让记录编号', 'scf.table.transferRecordNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '0fdd2001258c4826a76abb780bf7e0e9';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('0fdd2001258c4826a76abb780bf7e0e9', 'zh_CN', '融资单号', 'scf.table.financeOrderNo');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'scf.data.id.null' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('3f200001258c4826a76abb780bf7e0e9', 'zh_CN', '传输数据ID为空', 'scf.data.id.null');
END IF;
commit;
END;
/

-- 应付债权业务状态字典分组
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = '76ae7188d8f64487a058991d505ea2f4';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('76ae7188d8f64487a058991d505ea2f4', 'scf.payable.approvalStatus', '应付债权审批状态');
END IF;
commit;
END;
/

-- 1. 暂存 (0)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '7150bf637c944fd18cd8d30309b8e4db';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('7150bf637c944fd18cd8d30309b8e4db', '0', '暂存', NULL, '76ae7188d8f64487a058991d505ea2f4', 1);
END IF;
commit;
END;
/

-- 2. 打回 (-10)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'e83f1da0b1e74ec5b016091d28b6df7b';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('e83f1da0b1e74ec5b016091d28b6df7b', '-10', '打回', NULL, '76ae7188d8f64487a058991d505ea2f4', 2);
END IF;
commit;
END;
/

-- 3. 已作废 (-2)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '608b32d32115482ca8bbc04476a5609a';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('608b32d32115482ca8bbc04476a5609a', '-2', '已作废', NULL, '76ae7188d8f64487a058991d505ea2f4', 3);
END IF;
commit;
END;
/

-- 4. 审批中 (11)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '0bc8525af3e1423a88c11b8c1396751a';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('0bc8525af3e1423a88c11b8c1396751a', '11', '审批中', NULL, '76ae7188d8f64487a058991d505ea2f4', 4);
END IF;
commit;
END;
/

-- 5. 审批通过 (95)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '97739f2be1c6456b9663880ae5f69f68';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('97739f2be1c6456b9663880ae5f69f68', '95', '审批通过', NULL, '76ae7188d8f64487a058991d505ea2f4', 5);
END IF;
commit;
END;
/

-- 存量库矫正（幂等，可重复执行）：
-- a) 老字典行名称改为通用名称（按 ID 判断的 INSERT 不会覆盖老名称）
-- b) 删除不再维护的字典值：
--    -11 兑付打回 / 12 兑付审批中 / 96 兑付审批通过（码值仍会被后端写入 approval_status，
--    显示名统一由 ScfPayableVoucherConstant.PROCESS_STATUS_MAP 按“打回/审批中/审批通过”输出）
--    -3 已删除（代码中无任何写入）
BEGIN
    UPDATE sys_dict SET TYPE_NAME = '打回' WHERE ID = 'e83f1da0b1e74ec5b016091d28b6df7b';
    UPDATE sys_dict SET TYPE_NAME = '审批中' WHERE ID = '0bc8525af3e1423a88c11b8c1396751a';
    UPDATE sys_dict SET TYPE_NAME = '审批通过' WHERE ID = '97739f2be1c6456b9663880ae5f69f68';
    DELETE FROM sys_dict WHERE ID IN ('a1061f4010c947df975370fc8ea97150', 'fcdc72e25af04a4182f3dcc4b762dfd9', '17e3d04f28d34cdebdcccffdc28b227d', 'bc855e247ec64eeeb19f5cc5463be005');
    COMMIT;
END;
/

-- ============================================================
-- 2. 新增字典组：应付债权单据状态 (scf.payable.voucherStatus)
--    组ID: 9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d
-- ============================================================

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 'scf.payable.voucherStatus', '应付债权业务状态');
END IF;
commit;
END;
/

-- 未开立 (SORT=1)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'b2a1c3d4e5f6a7b8c9d0e1f2a3b4c5d6';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('b2a1c3d4e5f6a7b8c9d0e1f2a3b4c5d6', '0', '未开立', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 1);
END IF;
commit;
END;
/

-- 开立中 (SORT=2)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7', '1', '开立中', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 2);
END IF;
commit;
END;
/

-- 已开立 (SORT=3)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8', '2', '已开立', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 3);
END IF;
commit;
END;
/

-- 已拒签 (SORT=4)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'd4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9', '3', '已拒签', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 4);
END IF;
commit;
END;
/

-- 已撤销 (SORT=5)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0', '4', '已撤销', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 5);
END IF;
commit;
END;
/

-- 已兑付 (SORT=6)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1', '5', '已兑付', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 6);
END IF;
commit;
END;
/

-- 兑付中 (SORT=7)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2', '6', '兑付中', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 7);
END IF;
commit;
END;
/

-- 已作废 (SORT=8)
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3', '7', '已作废', NULL, '9e3f6e7f4c5d6a7b8c9d0e1f2a3b4c5d', 8);
END IF;
commit;
END;
/

-- 业务状态字典只保留 0~7 共 8 条（未开立/开立中/已开立/已拒签/已撤销/已兑付/兑付中/已作废），
-- 8 待兑付不再维护，存量库执行下面的 DELETE 清理
BEGIN
    DELETE FROM sys_dict WHERE ID = 'c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4';
    COMMIT;
END;
/

-- 1. 插入字典分组：单据审批状态
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = 'f2e89d7c3b6a41509ceb74321dc09651';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('f2e89d7c3b6a41509ceb74321dc09651', 'scf.rec.approvalStatus', '单据审批状态');
END IF;
commit;
END;
/

-- ====================== 签收单据审批状态 ======================
-- 开立暂存
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc01';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc01', 'open-draft', '签收暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 10);
END IF;
commit;
END;
/

-- 开立审批中
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc02';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc02', 'open-approval-ing', '签收审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 20);
END IF;
commit;
END;
/

-- 开立审批通过
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc03';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc03', 'open-approval-pass', '签收审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 30);
END IF;
commit;
END;
/

-- 开立打回
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc04';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc04', 'open-rejected', '签收打回', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 40);
END IF;
commit;
END;
/

-- 开立已作废
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc05';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc05', 'open-cancelled', '签收已作废', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 50);
END IF;
commit;
END;
/

-- ====================== 融资单据审批状态 ======================
-- 融资暂存
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc11';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc11', 'loan-draft', '融资暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 60);
END IF;
commit;
END;
/

-- 融资审批中
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc12';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc12', 'loan-approval-ing', '融资审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 70);
END IF;
commit;
END;
/

-- 融资审批通过
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc13';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc13', 'loan-approval-pass', '融资审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 80);
END IF;
commit;
END;
/

-- ====================== 转让单据审批状态 ======================
-- 转让暂存
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc21';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc21', 'transfer-draft', '转让暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 110);
END IF;
commit;
END;
/

-- 转让审批中
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc22';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc22', 'transfer-approval-ing', '转让审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 120);
END IF;
commit;
END;
/

-- 转让审批通过
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc23';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc23', 'transfer-approval-pass', '转让审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 130);
END IF;
commit;
END;
/

-- ====================== 提示付款审批状态 ======================
-- 提示付款暂存
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc31';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc31', 'payment-draft', '提示付款暂存', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 160);
END IF;
commit;
END;
/

-- 提示付款审批中
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc32';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc32', 'payment-approval-ing', '提示付款审批中', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 170);
END IF;
commit;
END;
/

-- 提示付款审批通过
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '001a112233445566778899aa00bbcc33';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('001a112233445566778899aa00bbcc33', 'payment-approval-pass', '提示付款审批通过', NULL, 'f2e89d7c3b6a41509ceb74321dc09651', 180);
END IF;
commit;
END;
/

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

-- 1. 插入字典分组：业务状态
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = '8d72f90c456e41b396ac7821ef5034b6';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('8d72f90c456e41b396ac7821ef5034b6', 'scf.rec.businessStatus', '业务状态');
END IF;
commit;
END;
/

-- ====================== 应收债权凭证业务状态 ======================
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc01';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc01', 'unsigned', '未签收', NULL, '8d72f90c456e41b396ac7821ef5034b6', 10);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc02';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc02', 'sign_ing', '签收中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 20);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc03';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc03', 'signed', '已签收', NULL, '8d72f90c456e41b396ac7821ef5034b6', 30);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc04';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc04', 'rejected', '已拒签', NULL, '8d72f90c456e41b396ac7821ef5034b6', 40);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc05';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc05', 'cancel', '已作废', NULL, '8d72f90c456e41b396ac7821ef5034b6', 50);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc06';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc06', 'split', '已拆分', NULL, '8d72f90c456e41b396ac7821ef5034b6', 60);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc07';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc07', 'un_transfer', '未转让', NULL, '8d72f90c456e41b396ac7821ef5034b6', 70);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc08';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc08', 'transfer_ing', '转让中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 80);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc09';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc09', 'transferred', '已转让', NULL, '8d72f90c456e41b396ac7821ef5034b6', 90);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc99';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc99', 'transfer_failed', '转让失败', NULL, '8d72f90c456e41b396ac7821ef5034b6', 90);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc10';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc10', 'un_loan', '未融资', NULL, '8d72f90c456e41b396ac7821ef5034b6', 100);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc11';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc11', 'loan_ing', '融资中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 110);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc12';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc12', 'loan_success', '已融资', NULL, '8d72f90c456e41b396ac7821ef5034b6', 120);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc13';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc13', 'loan_failed', '融资失败', NULL, '8d72f90c456e41b396ac7821ef5034b6', 130);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc14';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc14', 'un_payment', '未收款', NULL, '8d72f90c456e41b396ac7821ef5034b6', 140);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc15';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc15', 'payment_ing', '收款中', NULL, '8d72f90c456e41b396ac7821ef5034b6', 150);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc16';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc16', 'payment_success', '已收款', NULL, '8d72f90c456e41b396ac7821ef5034b6', 160);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc17';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc17', 'payment_failed', '收款失败', NULL, '8d72f90c456e41b396ac7821ef5034b6', 170);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9101001122334455667788aa00ffcc18';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9101001122334455667788aa00ffcc18', 'data_supplement', '数据待补录', NULL, '8d72f90c456e41b396ac7821ef5034b6', 180);
END IF;
commit;
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

-- ====================== 单据来源 ======================
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = 'a7d9f2c0e4b681357ce290418dc73502';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('a7d9f2c0e4b681357ce290418dc73502', 'scf.voucher.source', '单据来源');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '91c375802df44961a0eb5c287d0f641e';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('91c375802df44961a0eb5c287d0f641e', 'MANUAL', '手工新增', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 10);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '25fb601879dc43ae90c517482efd306b';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('25fb601879dc43ae90c517482efd306b', 'ERP', 'ERP', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 20);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '740e8d1c36af4259bcf02915d78634eb';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('740e8d1c36af4259bcf02915d78634eb', 'SUPPLY_PLATFORM', '供应链金融平台', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 30);
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'b01c2d3e5c4f5a60718293a4b6e8f901';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('b01c2d3e5c4f5a60718293a4b6e8f901', 'PAYMENT', '付款单', NULL, 'a7d9f2c0e4b681357ce290418dc73502', 15);
END IF;
commit;
END;
/

-- ====================== 融资付息方式 ======================
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = 'b7c3d5e6f7890123456789abcdef4567';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('b7c3d5e6f7890123456789abcdef4567', 'scf.voucher.interestMode', '融资付息方式');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'c8d4e6f7a890123456789abcdef56789';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('c8d4e6f7a890123456789abcdef56789', '1', '卖方付息', NULL, 'b7c3d5e6f7890123456789abcdef4567', 10);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'd9e5f7a8b90123456789abcdef67890';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('d9e5f7a8b90123456789abcdef67890', '2', '买方付息', NULL, 'b7c3d5e6f7890123456789abcdef4567', 20);
END IF;
commit;
END;
/

-- ====================== 机构类型 ======================
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = 'b7c3d5e6f7890987456789abcdef4567';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('b7c3d5e6f7890987456789abcdef4567', 'scf.fund.institution.type', '机构类型');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '56789c8d4e6f7a890123456789abcdef';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('56789c8d4e6f7a890123456789abcdef', '01', '保理公司', NULL, 'b7c3d5e6f7890987456789abcdef4567', 10);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '67890d9e5f7a8b90123456789abcdef';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('67890d9e5f7a8b90123456789abcdef', '02', '银行', NULL, 'b7c3d5e6f7890987456789abcdef4567', 20);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '67abcdef890d9e5f7a8b90123456789';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('67abcdef890d9e5f7a8b90123456789', '03', '其他', NULL, 'b7c3d5e6f7890987456789abcdef4567', 30);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = '5ea76ae7188d8f64487a058991d502f4';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('5ea76ae7188d8f64487a058991d502f4', 'scf.common.onlineSign', '债权凭证线上线下标识');
END IF;
commit;
END;
/

-- 线下
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '64ea46bc26e9ef0ef742bea136ae17ed';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('64ea46bc26e9ef0ef742bea136ae17ed', '0', '线下', NULL, '5ea76ae7188d8f64487a058991d502f4', 1);
END IF;
commit;
END;
/

-- 线上
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'aaa2d2efffa5b6c7d8e9f0c3d4e5f6a7' AND TYPE_CODE = '1';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('aaa2d2efffa5b6c7d8e9f0c3d4e5f6a7', '1', '线上', NULL, '5ea76ae7188d8f64487a058991d502f4', 2);
END IF;
commit;
END;
/

-- 凭证类别
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = '5f82c9d17e3b4068a21f9c7430de1b65';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('5f82c9d17e3b4068a21f9c7430de1b65', 'scf.voucher.type', '凭证类别');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '30ad67c29f1e4580b27d9c1e540f862b';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('30ad67c29f1e4580b27d9c1e540f862b', 'open', '开立凭证', NULL, '5f82c9d17e3b4068a21f9c7430de1b65', 10);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '72ef91c03d5b468a802f1c9d357e064b';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('72ef91c03d5b468a802f1c9d357e064b', 'transfer', '流转凭证', NULL, '5f82c9d17e3b4068a21f9c7430de1b65', 20);
END IF;
commit;
END;
/

-- 供应链指令状态字典分组
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict_group WHERE ID = '740b62c37c8f4dc89226389e426e6718';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
    VALUES ('740b62c37c8f4dc89226389e426e6718', 'scf.exc.voucherStat', '供应链指令状态');
END IF;
commit;
END;
/

-- 待发送 (TYPE_CODE = '1')
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'e47e945648494d3b81e6eb76e6d86098';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('e47e945648494d3b81e6eb76e6d86098', '1', '待发送', NULL, '740b62c37c8f4dc89226389e426e6718', 2);
END IF;
commit;
END;
/

-- 发送失败 (TYPE_CODE = '2')
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '9b2fb960082342d9a11e7ef79bb23a6c';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('9b2fb960082342d9a11e7ef79bb23a6c', '2', '发送失败', NULL, '740b62c37c8f4dc89226389e426e6718', 3);
END IF;
commit;
END;
/

-- 发送成功 (TYPE_CODE = '3')
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '1a13be1680a24406bf5f8e77d8a9e66e';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('1a13be1680a24406bf5f8e77d8a9e66e', '3', '发送成功', NULL, '740b62c37c8f4dc89226389e426e6718', 4);
END IF;
commit;
END;
/

-- 打回 (TYPE_CODE = '4')
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '4a26448a2efd4e048ab1334075eb4ab2';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('4a26448a2efd4e048ab1334075eb4ab2', '4', '打回', NULL, '740b62c37c8f4dc89226389e426e6718', 5);
END IF;
commit;
END;
/

-- 等待结果 (TYPE_CODE = '5')
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'a6bd2df07d014459a033f064ed83bae6';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('a6bd2df07d014459a033f064ed83bae6', '5', '等待结果', NULL, '740b62c37c8f4dc89226389e426e6718', 6);
END IF;
commit;
END;
/

-- 交易成功 (TYPE_CODE = '6')
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '719c3d2761db4c30852806c42a001d10';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('719c3d2761db4c30852806c42a001d10', '6', '交易成功', NULL, '740b62c37c8f4dc89226389e426e6718', 7);
END IF;
commit;
END;
/

-- 交易失败 (TYPE_CODE = '7')
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'f55f95d70da341d2a966936f1210c084';
IF v_COUNT < 1 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
    VALUES ('f55f95d70da341d2a966936f1210c084', '7', '交易失败', NULL, '740b62c37c8f4dc89226389e426e6718', 8);
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'af79bfc50cf54bc38e94825631858a1e';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('af79bfc50cf54bc38e94825631858a1e', 'zh_CN', '指令发送模式', 'scf.exc.instructionMode');
END IF;
commit;
END;
/

-- 1. 资金方编码不能为空
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '5081e36ddf644193aab52a3987ac5952';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('5081e36ddf644193aab52a3987ac5952', 'zh_CN', '资金方编码不能为空', 'scf.fund.provider.code.null');
END IF;
commit;
END;
/

-- 2. 资金方编码已存在
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '1a05e512078f41179734318da9ddeb15';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('1a05e512078f41179734318da9ddeb15', 'zh_CN', '资金方编码已存在', 'scf.fund.provider.code.exist');
END IF;
commit;
END;
/

-- 3. 机构类型不能为空
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'bf824b71897d49e49fcbb4c385c1cc7c';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('bf824b71897d49e49fcbb4c385c1cc7c', 'zh_CN', '机构类型不能为空', 'scf.fund.provider.org.type.null');
END IF;
commit;
END;
/

-- 4. 资金方名称不能为空
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '9d9598761de54c0bb8133fb148b32b1e';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('9d9598761de54c0bb8133fb148b32b1e', 'zh_CN', '资金方名称不能为空', 'scf.fund.provider.name.null');
END IF;
commit;
END;
/

-- 5. 银行行号不能为空
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '08cb87d4e51a4b15a9eda46ea3991021';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('08cb87d4e51a4b15a9eda46ea3991021', 'zh_CN', '银行行号不能为空', 'scf.fund.provider.bank.code.null');
END IF;
commit;
END;
/

-- 6. 该供应链金融平台未配置应付债权凭证页面模版，请联系管理端配置相应模版
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '518174d7efb241f8b97b45e2d7c4b617';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('518174d7efb241f8b97b45e2d7c4b617', 'zh_CN', '该供应链金融平台未配置应付债权凭证页面模版，请联系管理端配置相应模版', 'scf.not.config.platform');
END IF;
commit;
END;
/

-- 7. 传输数据ID为空
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'scf.data.id.null' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('b937932ded8c49e3863e7d9c79ac0513', 'zh_CN', '传输数据ID为空', 'scf.data.id.null');
END IF;
commit;
END;
/

-- 8. 只允许业务状态为"开立中"的应付债权凭证进行撤销操作
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'ca3dff51f01848bc8d411c97d39119b9';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('ca3dff51f01848bc8d411c97d39119b9', 'zh_CN', '只允许业务状态为"开立中"的应付债权凭证进行撤销操作', 'scf.data.status.not.allow.revoke');
END IF;
commit;
END;
/

-- 9. 该平台"{0}"在线接口未开通 请联系相应人员进行配置
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '18361ec100254b9db4577d4f87428235';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('18361ec100254b9db4577d4f87428235', 'zh_CN', '该平台"{0}"在线接口未开通 请联系相应人员进行配置', 'scf.online.interface.not.open');
END IF;
commit;
END;
/

-- 10. {0}平台 没有开通"{1}"在线接口 请联系相应人员进行配置
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '92b1a78913f24d6bb9adaa207d26659f';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('92b1a78913f24d6bb9adaa207d26659f', 'zh_CN', '{0}平台 没有开通"{1}"在线接口 请联系相应人员进行配置', 'scf.online.platform.interface.not.open');
END IF;
commit;
END;
/

-- 11. 只允许对已开立的应付债权凭证进行兑付结果查询，当前凭证状态不符合要求
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7891392b1af24d6bb9adaa207d26659f';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7891392b1af24d6bb9adaa207d26659f', 'zh_CN', '只允许对已开立的应付债权凭证进行兑付结果查询，当前凭证状态不符合要求', 'scf.voucher.not.opened.for.redemption');
END IF;
commit;
END;
/

-- 12. 该操作只允许线上凭证进行,请选择线上凭证
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '1a7892b913f24d6bb9adaa207d26659f';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('1a7892b913f24d6bb9adaa207d26659f', 'zh_CN', '该操作只允许线上凭证进行,请选择线上凭证', 'scf.voucher.only.online.operate');
END IF;
commit;
END;
/

-- 13. 该应付凭证存在审批中或已审批的兑付记录，无法重复提交兑付
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '7137fe0ffb54475b8cc0a102a1234caa';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('7137fe0ffb54475b8cc0a102a1234caa', 'zh_CN', '该应付凭证存在审批中或已审批的兑付记录，无法重复提交兑付', 'scf.voucher.has.conflict.redemption');
END IF;
commit;
END;
/

-- 14. 该凭证已生成撤销指令，无法重复撤销
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'faf8c10585274e92bacc55b8f74b5c82';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('faf8c10585274e92bacc55b8f74b5c82', 'zh_CN', '该凭证已生成撤销指令，无法重复撤销', 'scf.voucher.has.cancel.instruction');
END IF;
commit;
END;
/

-- 15. 选择的凭证都已生成撤销指令，无法重复撤销
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'fdf2a6ae718e46968a6ddad90918b64c';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('fdf2a6ae718e46968a6ddad90918b64c', 'zh_CN', '选择的凭证都已生成撤销指令，无法重复撤销', 'scf.voucher.batch.all.has.cancel.instruction');
END IF;
commit;
END;
/

-- 16. 该平台已配置过直联接口，请勿重复提交
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'a1b2c3d4e5f64738b9c0d1e2f3a4b5c6';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('a1b2c3d4e5f64738b9c0d1e2f3a4b5c6', 'zh_CN', '该平台已配置过直联接口，请勿重复提交', 'scf.ent.platform.config.exist');
END IF;
commit;
END;
/

-- 19. 只允许业务状态为“已开立/已撤销/已拒签”的应付债权凭证进行作废
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'b3f7c1a9d5e2486fa7c9b1e3d5f7a9c1';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('b3f7c1a9d5e2486fa7c9b1e3d5f7a9c1', 'zh_CN', '只允许业务状态为“已开立/已撤销/已拒签”的应付债权凭证进行作废', 'scf.voucher.status.not.allow.cancel');
END IF;
commit;
END;
/

-- 20. 作废原因不能为空
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'c4e8d2b1a6f35970b8d1e2f3a4b5c6d7';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('c4e8d2b1a6f35970b8d1e2f3a4b5c6d7', 'zh_CN', '作废原因不能为空', 'scf.voucher.cancel.reason.required');
END IF;
commit;
END;
/

-- 21. 作废时间不能为空
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = 'd5f9e3c2b7a46081c9e2f3a4b5c6d7e8';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('d5f9e3c2b7a46081c9e2f3a4b5c6d7e8', 'zh_CN', '作废时间不能为空', 'scf.voucher.cancel.date.required');
END IF;
commit;
END;
/

-- 17. 债权凭证编号已存在，请勿重复录入
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE ID = '5a9e3c7d1b4f6a8e9c2b7d4e1f6a8c93';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('5a9e3c7d1b4f6a8e9c2b7d4e1f6a8c93', 'zh_CN', '债权凭证编号已存在，请勿重复录入', 'scf.voucher.code.exist');
END IF;
commit;
END;
/


UPDATE sys_multi_lang
SET LANG_CONTEXT = '凭证在线查询'
WHERE ID = '83e47fca08ff4f1293cb3839f7d7859b';
