##修改人：qijh
##修改时间：2024.05.30
##修改原因：三单关联脚本

##系统参数付款明细勾对“后台任务每批次执行笔数”
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT 'cfeb319840b54b5380cdf52c9a91c1d9', 'receivable', 'autoCount', '600'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE ID = 'cfeb319840b54b5380cdf52c9a91c1d9' LIMIT 1
);
##GOGO

##系统参数付款明细勾对“付款明细滚动匹配天数”
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT '8a8f37852c46489b97715bb3abb356cb', 'receivable', 'rollMate', '3'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE ID = '8a8f37852c46489b97715bb3abb356cb' LIMIT 1
);
##GOGO

##三单关联执行策略表增加“相差天数”字段
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'BIS_BANK_EXECUTION_STRATEGY' AND COLUMN_NAME = 'TRANS_DAYS') THEN
      ALTER TABLE BIS_BANK_EXECUTION_STRATEGY ADD COLUMN TRANS_DAYS decimal(4,0);
ELSE
    ALTER TABLE BIS_BANK_EXECUTION_STRATEGY MODIFY COLUMN TRANS_DAYS decimal(4,0);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 
    DATABASE() AND TABLE_NAME = 'NIS_BILLHEAD' AND COLUMN_NAME = 'BLEND_SIGN') THEN
      ALTER TABLE NIS_BILLHEAD ADD COLUMN BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 
    DATABASE() AND TABLE_NAME = 'NIS_WAGE' AND COLUMN_NAME = 'BLEND_SIGN') THEN
      ALTER TABLE NIS_WAGE ADD COLUMN BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 
    DATABASE() AND TABLE_NAME = 'FEX_CASH_FLOW' AND COLUMN_NAME = 'BLEND_SIGN') THEN
      ALTER TABLE FEX_CASH_FLOW ADD COLUMN BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 
    DATABASE() AND TABLE_NAME = 'NIS_ALLOCATION_REQ' AND COLUMN_NAME = 'BLEND_SIGN') THEN
      ALTER TABLE NIS_ALLOCATION_REQ ADD COLUMN BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO


##历史数据更新。。把已勾对的单据修改勾对标志为已勾对
update nis_billhead set blend_sign = '1' where id in (select bill_id from NIS_BILL_BANK_SERIAL_NO where is_from = 'nis_billhead');
update nis_wage set blend_sign = '1' where id in (select bill_id from NIS_BILL_BANK_SERIAL_NO where is_from = 'nis_wage');
update fex_cash_flow set blend_sign = '1' where id in (select bill_id from NIS_BILL_BANK_SERIAL_NO where is_from = 'fex_cash_flow');
update nis_allocation_req set blend_sign = '1' where id in (select bill_id from NIS_BILL_BANK_SERIAL_NO where is_from = 'nis_allocation_req');
commit;
##GOGO

##新增参数:收款流水认领天数
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT 'e570035e514d4474aa2fa56c76a3549a', 'receivable', 'dayCount', '3' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE type = 'receivable' and name = 'dayCount' LIMIT 1
);
##GOGO

##新增参数:后台任务每批次执行笔数
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT '401086c96dc24957b50be80e053c6555', 'receivable', 'collectionAutoCount', '600' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE type = 'receivable' and name = 'collectionAutoCount' LIMIT 1
);
##GOGO


##更新参数“退款勾对天数”
update SYS_CONFIG set TYPE='receivable',VALUE='2' where ID='2c3176b78bc54162907ca6d25c04ab8b';
commit;
##GOGO

##新增参数:退款勾对定时任务取数天数
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT '3f23dec034cc4cd9acc604dcba2729fd', 'receivable', 'timedTasksDays', '30' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE type = 'receivable' and name = 'timedTasksDays' LIMIT 1
);
##GOGO

##修改字典
UPDATE sys_dict SET TYPE_NAME='付款与流水自动勾对' WHERE id = 'a48fc5d8fc134246ad037e2074907aca';
UPDATE sys_dict SET TYPE_NAME='流水与回单自动勾对' WHERE id = '93f4c38fdb15492a82f14725862a23e1';
##GOGO

##修改人：qijh
##修改内容：新增字典组"交易日期匹配规则"
##修改时间：2023-11-10
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '77dfbff5260247fe884ff3d22c33cea7', 'bisBankExecutionStrategy.transTimeRule', '交易日期匹配规则'from dual
where not exists (select 1 from sys_dict_group where ID ='77dfbff5260247fe884ff3d22c33cea7' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"相等"
##修改时间：2023-11-10
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '0d7ec7fd1b494d93818691d1eebbb515', '0', '相等',null,'77dfbff5260247fe884ff3d22c33cea7',1 from dual
where not exists (select 1 from sys_dict where ID ='0d7ec7fd1b494d93818691d1eebbb515' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"不相等"
##修改时间：2023-11-10
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '07bf996a73d24b85886fe928e8a9b757', '1', '不相等',null,'77dfbff5260247fe884ff3d22c33cea7',2 from dual
where not exists (select 1 from sys_dict where ID ='07bf996a73d24b85886fe928e8a9b757' limit 1);
##GOGO


##修改人：qijh
##修改内容：新增字典组"银行流水唯一标识" 
##修改时间：2023-10-08
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME) 
select '11ed0f83332a43718e11b0d2d6754dc7', 'bisBankExecutionStrategy.bankUnique', '银行流水唯一标识'from dual 
where not exists (select 1 from sys_dict_group where ID ='11ed0f83332a43718e11b0d2d6754dc7' limit 1); 
##GOGO

##修改人：qijh
##修改内容：新增字典"BIS_DTL_NO" 
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) 
select '169c0288022345aea7054a5e91af0ea6', 'BIS_DTL_NO', 'BIS_DTL_NO',null,'11ed0f83332a43718e11b0d2d6754dc7',1 from dual 
where not exists (select 1 from sys_dict where ID ='169c0288022345aea7054a5e91af0ea6' limit 1); 
##GOGO

##修改人：qijh
##修改内容：新增字典"BANK_SERIAL_NO" 
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) 
select 'bc9c1f939b7d4c18836eda1e3c8662c8', 'BANK_SERIAL_NO', 'BANK_SERIAL_NO',null,'11ed0f83332a43718e11b0d2d6754dc7',2 from dual 
where not exists (select 1 from sys_dict where ID ='bc9c1f939b7d4c18836eda1e3c8662c8' limit 1); 
##GOGO

##修改人：qijh
##修改内容：新增字典"BIS_EBILL_NO" 
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) 
select '4eb55cc8f23e423eaa0d522301774059', 'BIS_EBILL_NO', 'BIS_EBILL_NO',null,'11ed0f83332a43718e11b0d2d6754dc7',3 from dual 
where not exists (select 1 from sys_dict where ID ='4eb55cc8f23e423eaa0d522301774059' limit 1); 
##GOGO

##修改人：qijh
##修改内容：新增字典"ACC_NO" 
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) 
select '127d301a138f446ca1ef115a0faf0860', 'ACC_NO', 'ACC_NO',null,'11ed0f83332a43718e11b0d2d6754dc7',4 from dual 
where not exists (select 1 from sys_dict where ID ='127d301a138f446ca1ef115a0faf0860' limit 1); 
##GOGO


##修改人：qijh
##修改内容：新增字典组"付款单唯一标识" 
##修改时间：2023-10-08
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME) 
select 'b5f573885fc54f55bbcd33f9ae8be6f5', 'bisBankExecutionStrategy.payUnique', '付款单唯一标识'from dual 
where not exists (select 1 from sys_dict_group where ID ='b5f573885fc54f55bbcd33f9ae8be6f5' limit 1); 
##GOGO

##修改人：qijh
##修改内容：新增字典"BIS_DTL_NO" 
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) 
select 'bb2e253011c54b3ea85aa5888176693e', 'BIS_DTL_NO', 'BIS_DTL_NO',null,'b5f573885fc54f55bbcd33f9ae8be6f5',1 from dual 
where not exists (select 1 from sys_dict where ID ='bb2e253011c54b3ea85aa5888176693e' limit 1); 
##GOGO

##修改人：qijh
##修改内容：新增字典"BANK_SERIAL_NO" 
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) 
select '26513d58521046c2a0b4dcaf46b5e07e', 'BANK_SERIAL_NO', 'BANK_SERIAL_NO',null,'b5f573885fc54f55bbcd33f9ae8be6f5',2 from dual 
where not exists (select 1 from sys_dict where ID ='26513d58521046c2a0b4dcaf46b5e07e' limit 1); 
##GOGO

##修改人：qijh
##修改内容：新增字典"BILL_NO" 
##修改时间：2023-10-08
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT) 
select 'ad9f620ffafe4920aaae35eeaa99e5e6', 'BILL_NO', 'BILL_NO',null,'b5f573885fc54f55bbcd33f9ae8be6f5',3 from dual 
where not exists (select 1 from sys_dict where ID ='ad9f620ffafe4920aaae35eeaa99e5e6' limit 1); 
##GOGO

##修改人：qijh
##修改内容：新增字典组"电子回单唯一标识"
##修改时间：2023-10-09
insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '0e876062729f4ff4bfdfaa64971d12e4', 'bisBankExecutionStrategy.eReceiptUnique', '电子回单唯一标识'from dual
where not exists (select 1 from sys_dict_group where ID ='0e876062729f4ff4bfdfaa64971d12e4' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BIS_EBILL_NO"
##修改时间：2023-10-09
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'ea934b312701429abecfd3dc4b46a508', 'BIS_EBILL_NO', 'BIS_EBILL_NO',null,'0e876062729f4ff4bfdfaa64971d12e4',1 from dual
where not exists (select 1 from sys_dict where ID ='ea934b312701429abecfd3dc4b46a508' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BANK_SERIAL_NO"
##修改时间：2023-10-09
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '65c6ef668ab94abc8711fc6093a64da3', 'BANK_SERIAL_NO', 'BANK_SERIAL_NO',null,'0e876062729f4ff4bfdfaa64971d12e4',2 from dual
where not exists (select 1 from sys_dict where ID ='65c6ef668ab94abc8711fc6093a64da3' limit 1);
##GOGO

##修改人：qijh
##修改内容：新增字典"BILL_NO"
##修改时间：2023-10-09
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '81b3bb52533c44809ff5b6af5fd26623', 'BILL_NO', 'BILL_NO',null,'0e876062729f4ff4bfdfaa64971d12e4',3 from dual
where not exists (select 1 from sys_dict where ID ='81b3bb52533c44809ff5b6af5fd26623' limit 1);
##GOGO

##流水增加电子回单勾对标志字段 RECEIPT_BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 
    DATABASE() AND TABLE_NAME = 'BIS_ACC_DTL' AND COLUMN_NAME = 'RECEIPT_BLEND_SIGN') THEN
      ALTER TABLE BIS_ACC_DTL ADD COLUMN RECEIPT_BLEND_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
#GOGO

##系统参数电子回单勾对“后台任务每批次执行笔数”
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT 'c4c8f8d5b68247bfb102c880e93bd4d1', 'ele', 'autoCount', '600'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE ID = 'c4c8f8d5b68247bfb102c880e93bd4d1' LIMIT 1
);
##GOGO

##系统参数电子回单勾对“电子回单勾对滚动匹配天数”
INSERT INTO sys_config (ID, TYPE, NAME, VALUE)
SELECT 'e9d0e06f38074076affe09f87839a7ae', 'ele', 'rollMate', '3'  FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_config WHERE ID = 'e9d0e06f38074076affe09f87839a7ae' LIMIT 1
);
##GOGO

##修改内容:增加字段语句 BLEND_SIGN
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
	BEGIN
		IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 
    DATABASE() AND TABLE_NAME = 'BIS_BANK_RECEIPT_DTL' AND COLUMN_NAME = 'AUTO_SIGN') THEN
      ALTER TABLE BIS_BANK_RECEIPT_DTL ADD COLUMN AUTO_SIGN VARCHAR(1) DEFAULT 0;
    END IF;
  END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

