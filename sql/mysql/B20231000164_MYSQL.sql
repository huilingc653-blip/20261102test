##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-01
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY) 
select 'af22a2a4e4c746e5a7e7afea8d06c1ad', 'zh_CN', '密码组', 'table.bankacc.btnetukey.rpa.cipherSuite' from dual 
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bankacc.btnetukey.rpa.cipherSuite' AND LANG_CODE = 'zh_CN' limit 1); 
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-01
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY) 
select '43b7fc33c1ca4f13bae09be2d7617845', 'zh_CN', '机器号/IP地址', 'table.bankacc.btnetukey.rpa.ipAddress' from dual 
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bankacc.btnetukey.rpa.ipAddress' AND LANG_CODE = 'zh_CN' limit 1); 
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-01
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY) 
select '7bbdb5b638b64c8ca16e8dac37e81ed0', 'zh_CN', '网银登陆账号', 'table.bankacc.btnetukey.rpa.loginAccount' from dual 
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bankacc.btnetukey.rpa.loginAccount' AND LANG_CODE = 'zh_CN' limit 1); 
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-01
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY) 
select 'f92401ffc65744e7939262ba7d2039d5', 'zh_CN', '密码设备号', 'table.bankacc.btnetukey.rpa.passwordNo' from dual 
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bankacc.btnetukey.rpa.passwordNo' AND LANG_CODE = 'zh_CN' limit 1); 
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-01
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY) 
select 'f23ebd530828437db030668a4d840945', 'zh_CN', '端口号', 'table.bankacc.btnetukey.rpa.port' from dual 
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bankacc.btnetukey.rpa.port' AND LANG_CODE = 'zh_CN' limit 1); 
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-01
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY) 
select 'fe0433f757dd41ca9d2c26dd06126d33', 'zh_CN', 'RPA信息', 'table.bankacc.btnetukey.rpaInfo' from dual 
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bankacc.btnetukey.rpaInfo' AND LANG_CODE = 'zh_CN' limit 1); 
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-01
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY) 
select '85450ca7b0f2414eb1ee40ef9d2cddb1', 'zh_CN', '是否代管账户', 'table.bankacc.ukey.isRpaEscrow' from dual 
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bankacc.ukey.isRpaEscrow' AND LANG_CODE = 'zh_CN' limit 1); 
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-01
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY) 
select '6f2e05ba81434511ac5e78f8c51be793', 'zh_CN', 'RPA功能', 'table.bankacc.ukey.rpaFunction' from dual 
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.bankacc.ukey.rpaFunction' AND LANG_CODE = 'zh_CN' limit 1); 
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-06
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '235cd3feff8d4e299b0069eab959ff1f', 'zh_CN', '交易状态修改', 'netbank.button.batchUpdateVoucherStat' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.button.batchUpdateVoucherStat' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-06
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'cc499224d2aa494aba1935b912e7f352', 'zh_CN', '只有交易状态为银行处理失败的付款单允许打回，请重新选择！', 'netbank.message.rpa.voucherStat.fail.back' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.message.rpa.voucherStat.fail.back' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-06
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '33f5b76dc7084a8198d2063881043811', 'zh_CN', '只有交易状态为银行处理失败的付款单允许修改交易状态，请重新选择！', 'netbank.message.rpa.voucherStat.fail.update' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.message.rpa.voucherStat.fail.update' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-06
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ac18911738bb4be1a761f0c24fddaea8', 'zh_CN', '确定要修改交易状态吗？', 'nis.message.confirm.updateVoucherStat' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.message.confirm.updateVoucherStat' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典组"Rpa功能"
##修改时间：2023-11-01
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select 'a77b0ccfcbb545f4b2bc1507647716b4', 'ukey.rpaFunction', 'Rpa功能'from dual
where not exists (select 1 from sys_dict_group where ID ='a77b0ccfcbb545f4b2bc1507647716b4' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"支付功能"
##修改时间：2023-11-01
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'e64359ba48ec45598121847af286ef64', '1', '支付功能',null,'a77b0ccfcbb545f4b2bc1507647716b4',1 from dual
where not exists (select 1 from sys_dict where ID ='e64359ba48ec45598121847af286ef64' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"余额查询"
##修改时间：2023-11-01
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'afb014e117da4dc8ba1d6a2cdc0a8e0e', '2', '余额查询',null,'a77b0ccfcbb545f4b2bc1507647716b4',2 from dual
where not exists (select 1 from sys_dict where ID ='afb014e117da4dc8ba1d6a2cdc0a8e0e' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"流水查询"
##修改时间：2023-11-01
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '37793d6e4a6d4dd1a0e644eeb2d0b88c', '3', '流水查询',null,'a77b0ccfcbb545f4b2bc1507647716b4',3 from dual
where not exists (select 1 from sys_dict where ID ='37793d6e4a6d4dd1a0e644eeb2d0b88c' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"电子回单查询"
##修改时间：2023-11-01
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '3ddb3e8b882e4a15a090434e2cf511e6', '4', '电子回单查询',null,'a77b0ccfcbb545f4b2bc1507647716b4',4 from dual
where not exists (select 1 from sys_dict where ID ='3ddb3e8b882e4a15a090434e2cf511e6' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"对账"
##修改时间：2023-11-01
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'a0977627bbb042c3a4c506ab64b2638c', '5', '对账',null,'a77b0ccfcbb545f4b2bc1507647716b4',5 from dual
where not exists (select 1 from sys_dict where ID ='a0977627bbb042c3a4c506ab64b2638c' limit 1);
##GOGO

##修改人：huangx
##修改时间：2023-10-30
##修改原因：RPA付款

##新增系统结算方式"RPA付款"
insert into bt_pay_type (ID, SYS_VOUCHER_CODE, SYS_VOUCHER_NAME, RMK, VALID_SIGN)
SELECT 'e7e940632cda4ab29b4cfed19791d206', '07', 'RPA付款', 'RPA付款', '1' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM bt_pay_type WHERE ID = 'e7e940632cda4ab29b4cfed19791d206' LIMIT 1
);
##GOGO

##新增付款方式"RPA付款"
insert into fc_voucher_type (ID, VOUCHER_TYPE, VOUCHER_NAME, SYS_VOUCHER_CODE, CASH_TRANS_SIGN, SETTL_TYPE, IS_ELECTRON_PAY, IS_ACCOUNT, IS_BUDGET, RMK, VALID_SIGN, IS_SYSTEM)
SELECT 'be4e3d51d3ff41dca41ba5e03b1703b3', '07', 'RPA付款', '07', 'T', '0', '0', '0', '1', 'RPA付款', '1', '1' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM fc_voucher_type WHERE ID = 'be4e3d51d3ff41dca41ba5e03b1703b3' LIMIT 1
);
##GOGO

##银行账号表增加“RPA功能”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'RPA_FUNCTION') THEN
      ALTER TABLE BT_BANK_ACC ADD COLUMN RPA_FUNCTION VARCHAR(60);
ELSE
    ALTER TABLE BT_BANK_ACC MODIFY COLUMN RPA_FUNCTION VARCHAR(60);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##银行账号表增加“是否代管账户”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'IS_RPA_ESCROW') THEN
      ALTER TABLE BT_BANK_ACC ADD COLUMN IS_RPA_ESCROW VARCHAR(1);
ELSE
    ALTER TABLE BT_BANK_ACC MODIFY COLUMN IS_RPA_ESCROW VARCHAR(1);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##网银信息表增加“网银登陆账号”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_NET_UKEY' AND COLUMN_NAME = 'LOGIN_ACCOUNT') THEN
      ALTER TABLE BT_NET_UKEY ADD COLUMN LOGIN_ACCOUNT VARCHAR(60);
ELSE
    ALTER TABLE BT_NET_UKEY MODIFY COLUMN LOGIN_ACCOUNT VARCHAR(60);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##网银信息表增加“机器号/IP地址”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_NET_UKEY' AND COLUMN_NAME = 'IP_ADDRESS') THEN
      ALTER TABLE BT_NET_UKEY ADD COLUMN IP_ADDRESS VARCHAR(60);
ELSE
    ALTER TABLE BT_NET_UKEY MODIFY COLUMN IP_ADDRESS VARCHAR(60);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##网银信息表增加“端口号”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_NET_UKEY' AND COLUMN_NAME = 'PORT') THEN
      ALTER TABLE BT_NET_UKEY ADD COLUMN PORT VARCHAR(30);
ELSE
    ALTER TABLE BT_NET_UKEY MODIFY COLUMN PORT VARCHAR(30);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##网银信息表增加“密码设备号”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_NET_UKEY' AND COLUMN_NAME = 'PASSWORD_NO') THEN
      ALTER TABLE BT_NET_UKEY ADD COLUMN PASSWORD_NO VARCHAR(60);
ELSE
    ALTER TABLE BT_NET_UKEY MODIFY COLUMN PASSWORD_NO VARCHAR(60);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##网银信息表增加“密码组”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_NET_UKEY' AND COLUMN_NAME = 'CIPHER_SUITE') THEN
      ALTER TABLE BT_NET_UKEY ADD COLUMN CIPHER_SUITE VARCHAR(60);
ELSE
    ALTER TABLE BT_NET_UKEY MODIFY COLUMN CIPHER_SUITE VARCHAR(60);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##付款表增加“交易状态”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'VOUCHER_STAT') THEN
      ALTER TABLE NIS_BILLHEAD ADD COLUMN VOUCHER_STAT VARCHAR(1);
ELSE
    ALTER TABLE NIS_BILLHEAD MODIFY COLUMN VOUCHER_STAT VARCHAR(1);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##付款表增加“交易时间”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'TRANS_TIME') THEN
      ALTER TABLE NIS_BILLHEAD ADD COLUMN TRANS_TIME datetime(6);
ELSE
    ALTER TABLE NIS_BILLHEAD MODIFY COLUMN TRANS_TIME datetime(6);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##付款表增加“返回信息”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'RETURN_MSG') THEN
      ALTER TABLE NIS_BILLHEAD ADD COLUMN RETURN_MSG VARCHAR(2000);
ELSE
    ALTER TABLE NIS_BILLHEAD MODIFY COLUMN RETURN_MSG VARCHAR(2000);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：huangx
##修改内容：增加国际化
##修改时间：2023-11-03
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '907e7a65ddde4301a805d4dded63a700', 'zh_CN', '余额不足提示', 'netbank.message.balance.title' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.message.balance.title' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##新增菜单“结算管理>>支付结算>>RPA付款业务跟踪”
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE)
SELECT '06af9ff05d274cda8294a036aa6fde4b', '4', 'RPA付款业务跟踪', 'RPA付款业务跟踪', 'e5966e6088954cf4996811b12916b91f', null, 4, 1, '/netbank/nisbillhead/listRPA', null, null, null, null, null, null, 0 FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_menu WHERE ID = '06af9ff05d274cda8294a036aa6fde4b' LIMIT 1
);
##GOGO

##新增菜单资源“RPA付款业务跟踪-应用资源”
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'b19b87880f004fd09a9e1bb9da127b55', '应用资源', '应用资源', 'netbank:netBank:nisBillHead:rpa:apply', 1, '06af9ff05d274cda8294a036aa6fde4b' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'b19b87880f004fd09a9e1bb9da127b55' LIMIT 1
);
##GOGO

##新增菜单资源“RPA付款业务跟踪-查询资源”
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'f892960254cf42bb9040eccbbb0467ac', '查询资源', '查询资源', 'netbank:netBank:nisBillHead:rpa:query', 1, '06af9ff05d274cda8294a036aa6fde4b' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'f892960254cf42bb9040eccbbb0467ac' LIMIT 1
);
##GOGO

##修改人：huangx
##修改内容：新增字典组"RPA交易状态"
##修改时间：2023-11-06
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '50c328820f6749af941651314fd95b70', 'rpa.voucherStat', 'RPA交易状态'from dual
where not exists (select 1 from sys_dict_group where ID ='50c328820f6749af941651314fd95b70' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"RPA未接收"
##修改时间：2023-11-06
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'ff6f1c6f6fa14ed0a9a4a1cae5cd2a6d', '3', 'RPA未接收',null,'50c328820f6749af941651314fd95b70',1 from dual
where not exists (select 1 from sys_dict where ID ='ff6f1c6f6fa14ed0a9a4a1cae5cd2a6d' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"RPA已接收"
##修改时间：2023-11-06
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'be822881f78b48f9b1d6c98e16fbf03e', 'W', 'RPA已接收',null,'50c328820f6749af941651314fd95b70',2 from dual
where not exists (select 1 from sys_dict where ID ='be822881f78b48f9b1d6c98e16fbf03e' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"等待结果"
##修改时间：2023-11-06
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '4e97522beab64d3ab81eec87f9fe5a12', '1', '等待结果',null,'50c328820f6749af941651314fd95b70',3 from dual
where not exists (select 1 from sys_dict where ID ='4e97522beab64d3ab81eec87f9fe5a12' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"银行处理失败"
##修改时间：2023-11-06
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '62778b8133284c3b95b78aa673a398ae', '2', '银行处理失败',null,'50c328820f6749af941651314fd95b70',4 from dual
where not exists (select 1 from sys_dict where ID ='62778b8133284c3b95b78aa673a398ae' limit 1);
##GOGO

##修改人：huangx
##修改内容：新增字典"交易成功"
##修改时间：2023-11-06
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '989b8fbd44b545febdc42ec457a69513', '0', '交易成功',null,'50c328820f6749af941651314fd95b70',5 from dual
where not exists (select 1 from sys_dict where ID ='989b8fbd44b545febdc42ec457a69513' limit 1);
##GOGO