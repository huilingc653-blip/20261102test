CREATE OR REPLACE VIEW erp_bis_acc_bal_view AS
SELECT bab.id        AS SERIAL_ID, -- 唯一键
       bab.tenant_id ,
       sc.code       AS CORP_CODE, -- 本方单位代码
       sc.name   as corp_name,
       bab.bank_acc BANK_ACC, -- 账号
       bab.bal BAL, -- 账户余额
       DATE_FORMAT(bab.bal_date,'%Y-%m-%d') AS BAL_DATE, -- 余额日期
       bab.avail_bal AVAIL_BAL, -- 可用余额
       bab.frz_bal  as freeze_bal,
       bc.english_code cur_code, -- 英文币别简码
       bc.cur_name cur_name
FROM BIS_ACC_BAL bab
         LEFT JOIN SYS_CORP sc ON bab.corp_id = sc.id and bab.tenant_id = sc.tenant_id
         LEFT JOIN bt_currency bc ON bc.id = bab.cur_id and bab.tenant_id = bc.tenant_id
WHERE bab.bal_date > SUBDATE(SYSDATE(),365)
;
