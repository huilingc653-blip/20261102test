##修改人：zhoup
##修改时间：2024.05.06
##修改原因：添加单据类型和账户组两个字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'RECEIPT_ID') THEN
      alter table BT_BANK_ACC add column RECEIPT_ID varchar(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_ACC' AND COLUMN_NAME = 'FC_ACC_GROUP_CODE') THEN
      alter table BT_BANK_ACC add column FC_ACC_GROUP_CODE varchar(200);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


##修改原因:添加 是否直连银行 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'ae5d22b9f7eb452f80376623a53acb16', 'zh_CN', '是否直连银行', 'table.whether.connect.bank' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.whether.connect.bank' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改原因:添加 是否直连银行 字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BT_BANK_TYPE' AND COLUMN_NAME = 'WHETHER_CONNECT_BANK') THEN
      alter table BT_BANK_TYPE add column WHETHER_CONNECT_BANK varchar(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##新增银企上线占比表菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '42a34d180bad4cd782c633b33084602b', '', '银企上线占比表', '银企上线占比表', '07cbbb8132de427eb53c9d624fb7ee22', '', 4, 1, '/report/enterpriseBankGoOnline/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='银企上线占比表' limit 1);
commit;
##GOGO


##新增资金收支表菜单
INSERT INTO sys_menu (ID, CODE, DESCRIPTION, NAME, PARENT_ID, PARENT_IDS, SORT, STATUS, URL, NET_ID, ICON, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, IS_MODULE, IS_EXTEND, OLD_ICON)
select '4e48bc81fa754c40874571474390404f', '', '资金收支表', '资金收支表', '07cbbb8132de427eb53c9d624fb7ee22', '', 500, 1, '/report/incomeAndExpenditureOfFunds/list', '', '', null, null, null, null, 0, 1, null from dual
where not exists (select 1 from sys_menu where NAME ='资金收支表' limit 1);
commit;
##GOGO