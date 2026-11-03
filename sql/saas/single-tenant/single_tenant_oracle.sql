-- =============================================================
-- bt-saas 单租户专属部署：银企端直连库表 TENANT_ID 默认值脚本（Oracle 版）
-- 生成时间：2026-09-12
-- 适用前提：
--   1) 单租户专属独立库（部署时区分单/多租户，用单租户即不再切换）；
--   2) 生产库兼容 Oracle 与 MySQL（本文件为 Oracle 版，另见 single_tenant_mysql.sql）；
--   3) 银企端 INSERT 不显式列 TENANT_ID；4) 银企端除新增也 UPDATE 这些表。
-- 方案：对银企端直连库写入、企业端读取的 13 张表，建立
--       BEFORE INSERT OR UPDATE 组合触发器，当 TENANT_ID 为 NULL 时
--       强制置为默认租户 ID，覆盖银企端写入与存量触碰回填。
-- 取值：默认租户 ID 见下方函数 fn_saas_default_tenant（仅需改这一处即可全脚本生效）。
-- 说明：本脚本幂等，可重复执行；触发器/函数仅作用于单租户专属库，不影响多租户库。
-- =============================================================

-- -------------------- 【配置】默认租户 ID --------------------
-- 部署时如默认租户 ID 不是 '1000'，修改下方 RETURN 后的常量即可，全脚本生效。
CREATE OR REPLACE FUNCTION fn_saas_default_tenant RETURN VARCHAR2 DETERMINISTIC IS
BEGIN
  RETURN '1000';   -- ← 默认租户 ID（单租户专属部署的 bytter.tenant.default-id）
END;
/

-- ==================== 一、触发器（13 表） ====================

-- ---------- BIS_ACC_BAL ----------
CREATE OR REPLACE TRIGGER TRG_BIS_ACC_BAL_TENANT
BEFORE INSERT OR UPDATE ON BIS_ACC_BAL
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BIS_ACC_DTL ----------
CREATE OR REPLACE TRIGGER TRG_BIS_ACC_DTL_TENANT
BEFORE INSERT OR UPDATE ON BIS_ACC_DTL
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BIS_BANK_MONTH_STATEMENT_DTL ----------
CREATE OR REPLACE TRIGGER TRG_BIS_BANK_MS_DTL_TENANT
BEFORE INSERT OR UPDATE ON BIS_BANK_MONTH_STATEMENT_DTL
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BIS_BANK_MONTH_STATEMENT_PDF ----------
CREATE OR REPLACE TRIGGER TRG_BIS_BANK_MS_PDF_TENANT
BEFORE INSERT OR UPDATE ON BIS_BANK_MONTH_STATEMENT_PDF
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BIS_BANK_RECEIPT_DTL ----------
CREATE OR REPLACE TRIGGER TRG_BIS_BANK_RCPT_DTL_TENANT
BEFORE INSERT OR UPDATE ON BIS_BANK_RECEIPT_DTL
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BIS_BANK_RECEIPT_PDF ----------
CREATE OR REPLACE TRIGGER TRG_BIS_BANK_RCPT_PDF_TENANT
BEFORE INSERT OR UPDATE ON BIS_BANK_RECEIPT_PDF
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BP_DUBIOUS_TRADE ----------
CREATE OR REPLACE TRIGGER TRG_BP_DUBIOUS_TRADE_TENANT
BEFORE INSERT OR UPDATE ON BP_DUBIOUS_TRADE
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BP_CHECK_TRADE ----------
CREATE OR REPLACE TRIGGER TRG_BP_CHECK_TRADE_TENANT
BEFORE INSERT OR UPDATE ON BP_CHECK_TRADE
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BIS_GATHER_CMD ----------
CREATE OR REPLACE TRIGGER TRG_BIS_GATHER_CMD_TENANT
BEFORE INSERT OR UPDATE ON BIS_GATHER_CMD
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BIS_EXC_PROC ----------
CREATE OR REPLACE TRIGGER TRG_BIS_EXC_PROC_TENANT
BEFORE INSERT OR UPDATE ON BIS_EXC_PROC
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BT_NET_CHALK ----------
CREATE OR REPLACE TRIGGER TRG_BT_NET_CHALK_TENANT
BEFORE INSERT OR UPDATE ON BT_NET_CHALK
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BIS_BANK_BRANCH（非全局租户表，纳入触发器） ----------
CREATE OR REPLACE TRIGGER TRG_BIS_BANK_BRANCH_TENANT
BEFORE INSERT OR UPDATE ON BIS_BANK_BRANCH
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- BT_SWIFT_INFO（非全局租户表，纳入触发器） ----------
CREATE OR REPLACE TRIGGER TRG_BT_SWIFT_INFO_TENANT
BEFORE INSERT OR UPDATE ON BT_SWIFT_INFO
FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
  END IF;
END;
/

-- ---------- AB_BILL_RECEIVEBIZ（非全局租户表，纳入触发器） ----------
CREATE OR REPLACE TRIGGER TRG_AB_BILL_RECEIVEBIZ_TENANT
BEFORE INSERT OR UPDATE ON AB_BILL_RECEIVEBIZ
                            FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
END IF;
END;
/

-- ---------- BIS_EXC（非全局租户表，纳入触发器） ----------
CREATE OR REPLACE TRIGGER TRG_BIS_EXC_TENANT
BEFORE INSERT OR UPDATE ON BIS_EXC
                            FOR EACH ROW
BEGIN
  IF :NEW.TENANT_ID IS NULL THEN
    :NEW.TENANT_ID := fn_saas_default_tenant();
END IF;
END;
/

-- ==================== 二、存量数据回填（一次性执行，幂等） ====================
-- 仅将历史 TENANT_ID 为 NULL 的行补为默认租户 ID；已非 NULL 的行不受影响。
-- 触发器 ON UPDATE 会在后续银企端触碰存量行时自动回填，此处为一次性兜底。
UPDATE BIS_ACC_BAL SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BIS_ACC_DTL SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BIS_BANK_MONTH_STATEMENT_DTL SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BIS_BANK_MONTH_STATEMENT_PDF SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BIS_BANK_RECEIPT_DTL SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BIS_BANK_RECEIPT_PDF SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BP_DUBIOUS_TRADE SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BP_CHECK_TRADE SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BIS_GATHER_CMD SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BIS_EXC_PROC SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BT_NET_CHALK SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BIS_BANK_BRANCH SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BT_SWIFT_INFO SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE AB_BILL_RECEIVEBIZ SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;
UPDATE BIS_EXC SET TENANT_ID = fn_saas_default_tenant() WHERE TENANT_ID IS NULL;


-- ==================== 三、插入默认租户数据（一次性执行，幂等） ====================
DECLARE
v_count NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count FROM sys_tenant_manager WHERE ID = fn_saas_default_tenant();
IF v_count = 0 THEN
        INSERT /*+ IGNORE_ROW_ON_DUPKEY_INDEX (sys_tenant_manager (ID)) */ INTO sys_tenant_manager (ID,
            TENANT_CODE,
            TENANT_NAME,
            TENANT_ENGLISH_NAME,
            POST_CODE,
            TENANT_CERT_TYPE,
            TENANT_CERT_CODE,
            TENANT_CERT_INDATE,
            TENANT_PHONE,
            TENANT_ADDR,
            LEGAL_PERSON_NAME,
            LEGAL_PERSON_MOBILE,
            LEGAL_PERSON_CERT_TYPE,
            LEGAL_PERSON_CERT_CODE,
            LEGAL_PERSON_CERT_INDATE,
            ATTN,
            ATTN_PHONE,
            ATTN_EMAIL,
            CORP_ID,
            SIGN_CHANNELS,
            IS_SAME_SUB_BANK,
            AM_USER_ID,
            ATTN_USER_ID,
            TENANT_STATUS,
            SIGN_DATE,
            ENABLE_DATE,
            STATUS,
            TENANT_ID,
            CHARGE_MODE,
            BILL_CODE,
            SOURCE_BILL_CODE,
            ATTACHMENT_ID,
            CREATE_BY,
            CREATE_TIME,
            UPDATE_BY,
            UPDATE_TIME,
            corp_number,
            user_number,
            account_number
        ) VALUES (
            fn_saas_default_tenant(),
            fn_saas_default_tenant(),
            '单租户模式默认租户',
            null,
            null,
            '1',
            '88888888',
            TO_DATE('9999-12-31','yyyy-mm-dd'),
            '18888888888',
            null,
            '单租户模式默认租户',
            '18888888888',
            '03',
            '88888888',
            TO_DATE('2099-12-31','yyyy-mm-dd'),
            null,
            null,
            null,
            '402880425b8abcfb015b8ac16e980000',
            '01',
            '1',
            '402880425b6b0a58015b6b0abeb10000',
            null,
            1,
            TO_DATE('2026-04-09 00:00:00','yyyy-mm-dd hh24:mi:ss'),
            TO_DATE('2026-04-09 15:37:40','yyyy-mm-dd hh24:mi:ss'),
            0,
            fn_saas_default_tenant(),
            '0',
            null,
            null,
            null,
            null,
            TO_DATE('2026-04-09 16:11:19','yyyy-mm-dd hh24:mi:ss'),
            null,
            TO_DATE('2026-09-09 16:47:28','yyyy-mm-dd hh24:mi:ss'),
            12,
            10,
            10
        );
COMMIT;
DBMS_OUTPUT.PUT_LINE('插入成功');
ELSE
        DBMS_OUTPUT.PUT_LINE('记录已存在，跳过插入');
END IF;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('异常：'||SQLERRM);
        RAISE;
END;
/




