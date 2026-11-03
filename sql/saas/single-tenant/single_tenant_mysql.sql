-- =============================================================
-- bt-saas 单租户专属部署：银企端直连库表 TENANT_ID 默认值脚本（MySQL 版）
-- 生成时间：2026-09-12
-- 适用前提：
--   1) 单租户专属独立库（部署时区分单/多租户，用单租户即不再切换）；
--   2) 生产库兼容 Oracle 与 MySQL（本文件为 MySQL 版，另见 single_tenant_oracle.sql）；
--   3) 银企端 INSERT 不显式列 TENANT_ID；4) 银企端除新增也 UPDATE 这些表。
-- 方案：对银企端直连库写入、企业端读取的 13 张表，建立
--       BEFORE INSERT / BEFORE UPDATE 触发器，当 TENANT_ID 为 NULL 时
--       强制置为默认租户 ID，覆盖银企端写入与存量触碰回填。
-- 取值：默认租户 ID 见下方函数 fn_saas_default_tenant（仅需改这一处即可全脚本生效）。
-- 说明：本脚本幂等，可重复执行；触发器/函数仅作用于单租户专属库，不影响多租户库。
-- =============================================================

-- -------------------- 【配置】默认租户 ID --------------------
-- 部署时如默认租户 ID 不是 '1000'，修改下方 RETURN 后的常量即可，全脚本生效。
DROP FUNCTION IF EXISTS fn_saas_default_tenant;
DELIMITER $$
CREATE FUNCTION fn_saas_default_tenant() RETURNS VARCHAR(32) DETERMINISTIC
BEGIN
  RETURN '1000';   -- ← 默认租户 ID（单租户专属部署的 bytter.tenant.default-id）
END $$
DELIMITER ;

-- ==================== 一、触发器（13 表） ====================

-- ---------- bis_acc_bal ----------
DROP TRIGGER IF EXISTS trg_bis_acc_bal_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_acc_bal_tenant_ins BEFORE INSERT ON `bis_acc_bal` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_acc_bal_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_acc_bal_tenant_upd BEFORE UPDATE ON `bis_acc_bal` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bis_acc_dtl ----------
DROP TRIGGER IF EXISTS trg_bis_acc_dtl_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_acc_dtl_tenant_ins BEFORE INSERT ON `bis_acc_dtl` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_acc_dtl_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_acc_dtl_tenant_upd BEFORE UPDATE ON `bis_acc_dtl` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bis_bank_month_statement_dtl ----------
DROP TRIGGER IF EXISTS trg_bis_bank_month_statement_dtl_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_month_statement_dtl_tenant_ins BEFORE INSERT ON `bis_bank_month_statement_dtl` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_bank_month_statement_dtl_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_month_statement_dtl_tenant_upd BEFORE UPDATE ON `bis_bank_month_statement_dtl` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bis_bank_month_statement_pdf ----------
DROP TRIGGER IF EXISTS trg_bis_bank_month_statement_pdf_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_month_statement_pdf_tenant_ins BEFORE INSERT ON `bis_bank_month_statement_pdf` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_bank_month_statement_pdf_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_month_statement_pdf_tenant_upd BEFORE UPDATE ON `bis_bank_month_statement_pdf` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bis_bank_receipt_dtl ----------
DROP TRIGGER IF EXISTS trg_bis_bank_receipt_dtl_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_receipt_dtl_tenant_ins BEFORE INSERT ON `bis_bank_receipt_dtl` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_bank_receipt_dtl_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_receipt_dtl_tenant_upd BEFORE UPDATE ON `bis_bank_receipt_dtl` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bis_bank_receipt_pdf ----------
DROP TRIGGER IF EXISTS trg_bis_bank_receipt_pdf_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_receipt_pdf_tenant_ins BEFORE INSERT ON `bis_bank_receipt_pdf` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_bank_receipt_pdf_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_receipt_pdf_tenant_upd BEFORE UPDATE ON `bis_bank_receipt_pdf` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bp_dubious_trade ----------
DROP TRIGGER IF EXISTS trg_bp_dubious_trade_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bp_dubious_trade_tenant_ins BEFORE INSERT ON `bp_dubious_trade` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bp_dubious_trade_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bp_dubious_trade_tenant_upd BEFORE UPDATE ON `bp_dubious_trade` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bp_check_trade ----------
DROP TRIGGER IF EXISTS trg_bp_check_trade_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bp_check_trade_tenant_ins BEFORE INSERT ON `bp_check_trade` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bp_check_trade_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bp_check_trade_tenant_upd BEFORE UPDATE ON `bp_check_trade` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bis_gather_cmd ----------
DROP TRIGGER IF EXISTS trg_bis_gather_cmd_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_gather_cmd_tenant_ins BEFORE INSERT ON `bis_gather_cmd` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_gather_cmd_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_gather_cmd_tenant_upd BEFORE UPDATE ON `bis_gather_cmd` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bis_exc_proc ----------
DROP TRIGGER IF EXISTS trg_bis_exc_proc_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_exc_proc_tenant_ins BEFORE INSERT ON `bis_exc_proc` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_exc_proc_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_exc_proc_tenant_upd BEFORE UPDATE ON `bis_exc_proc` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bt_net_chalk ----------
DROP TRIGGER IF EXISTS trg_bt_net_chalk_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bt_net_chalk_tenant_ins BEFORE INSERT ON `bt_net_chalk` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bt_net_chalk_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bt_net_chalk_tenant_upd BEFORE UPDATE ON `bt_net_chalk` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bis_bank_branch（非全局租户表，纳入触发器） ----------
DROP TRIGGER IF EXISTS trg_bis_bank_branch_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_branch_tenant_ins BEFORE INSERT ON `bis_bank_branch` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_bank_branch_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_bank_branch_tenant_upd BEFORE UPDATE ON `bis_bank_branch` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- bt_swift_info（非全局租户表，纳入触发器） ----------
DROP TRIGGER IF EXISTS trg_bt_swift_info_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bt_swift_info_tenant_ins BEFORE INSERT ON `bt_swift_info` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bt_swift_info_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bt_swift_info_tenant_upd BEFORE UPDATE ON `bt_swift_info` FOR EACH ROW
BEGIN
  IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
  END IF;
END $$
DELIMITER ;

-- ---------- ab_bill_receivebiz（票据签收表，纳入触发器） ----------
DROP TRIGGER IF EXISTS trg_ab_bill_receivebiz_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_ab_bill_receivebiz_tenant_ins BEFORE INSERT ON `ab_bill_receivebiz` FOR EACH ROW
BEGIN
    IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_ab_bill_receivebiz_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_ab_bill_receivebiz_tenant_upd BEFORE UPDATE ON `ab_bill_receivebiz` FOR EACH ROW
BEGIN
    IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
END IF;
END $$
DELIMITER ;

-- ---------- bis_exc（指令表表，纳入触发器） ----------
DROP TRIGGER IF EXISTS trg_bis_exc_tenant_ins;
DELIMITER $$
CREATE TRIGGER trg_bis_exc_tenant_ins BEFORE INSERT ON `bis_exc` FOR EACH ROW
BEGIN
    IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
END IF;
END $$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_bis_exc_tenant_upd;
DELIMITER $$
CREATE TRIGGER trg_bis_exc_tenant_upd BEFORE UPDATE ON `bis_exc` FOR EACH ROW
BEGIN
    IF NEW.TENANT_ID IS NULL THEN
    SET NEW.TENANT_ID = fn_saas_default_tenant();
END IF;
END $$
DELIMITER ;



-- ==================== 二、存量数据回填（一次性执行，幂等） ====================
-- 仅将历史 TENANT_ID 为 NULL 的行补为默认租户 ID；已非 NULL 的行不受影响。
-- 触发器 ON UPDATE 会在后续银企端触碰存量行时自动回填，此处为一次性兜底。
UPDATE `bis_acc_bal` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bis_acc_dtl` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bis_bank_month_statement_dtl` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bis_bank_month_statement_pdf` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bis_bank_receipt_dtl` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bis_bank_receipt_pdf` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bp_dubious_trade` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bp_check_trade` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bis_gather_cmd` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bis_exc_proc` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bt_net_chalk` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bis_bank_branch` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bt_swift_info` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `ab_bill_receivebiz` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE `bis_exc` SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;

-- 插入默认租户
INSERT IGNORE INTO sys_tenant_manager
(id,TENANT_CODE,AM_USER_ID, TENANT_NAME,  TENANT_CERT_TYPE, TENANT_CERT_CODE, TENANT_CERT_INDATE,
 TENANT_PHONE, LEGAL_PERSON_NAME, LEGAL_PERSON_MOBILE, LEGAL_PERSON_CERT_TYPE, LEGAL_PERSON_CERT_CODE,
 LEGAL_PERSON_CERT_INDATE, SIGN_CHANNELS, IS_SAME_SUB_BANK,corp_id,
 TENANT_STATUS, SIGN_DATE, ENABLE_DATE, STATUS, TENANT_ID, CHARGE_MODE, CREATE_TIME, corp_number, user_number, account_number)
select fn_saas_default_tenant(),fn_saas_default_tenant(),
       '402880425b6b0a58015b6b0abeb10000',
       '单租户模式默认租户',
       '1',
       '88888888',
       '2099-12-31',
       '18888888888',
       '单租户模式默认租户',
       '18888888888',
       '03',
       '88888888',
       '2099-12-31',
       '01',
       '1',
       fn_saas_default_tenant(),
       1,
       '2026-04-09 00:00:00',
       '2026-04-14 15:24:15',
       0,
       fn_saas_default_tenant(),
       '0',
       '2026-04-09 16:11:19',
       12,
       10,
       10
from dual
where not exists(select 1 from sys_tenant_manager where id = fn_saas_default_tenant());


