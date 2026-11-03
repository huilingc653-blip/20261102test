CREATE OR REPLACE VIEW erp_bis_acc_bal_view AS
SELECT bab.id AS "SERIAL_ID", -- 唯一键
       bab.tenant_id AS "tenant_id", sc.code AS "CORP_CODE", -- 本方单位代码
       sc.name AS "corp_name", bab.bank_acc AS "BANK_ACC", -- 账号
       bab.bal AS "BAL", -- 账户余额
       TO_CHAR(bab.bal_date, 'YYYY-MM-DD') AS "BAL_DATE", -- 余额日期
       bab.avail_bal AS "AVAIL_BAL", bab.frz_bal AS "freeze_bal",
       bc.english_code AS "cur_code", bc.cur_name AS "cur_name"
FROM BIS_ACC_BAL bab
LEFT JOIN SYS_CORP sc ON bab.corp_id = sc.id AND bab.tenant_id = sc.tenant_id
LEFT JOIN bt_currency bc ON bc.id = bab.cur_id AND bab.tenant_id = bc.tenant_id
WHERE bab.bal_date > SYSDATE - 365;
