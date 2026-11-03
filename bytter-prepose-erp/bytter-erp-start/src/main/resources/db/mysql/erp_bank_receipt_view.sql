
CREATE OR REPLACE VIEW erp_bank_receipt_view
AS
SELECT b.id AS serial_id, -- 电子回单唯一键
       b.tenant_id as tenant_id,
       b.pay_acc_no AS payer_acc_no, -- 付款账号
       b.pay_acc_name AS payer_acc_name, -- 付款户名
       b.pay_branch_name AS payer_bank_name, -- 付款行
       b.payee_acc_no AS payee_acc_no, -- 收款方账号
       b.payee_acc_name AS payee_acc_name, -- 收款方户名
       b.payee_bracn_name AS payee_bank_name, -- 收款行
       b.trans_amt AS AMT, -- 金额
       bc.english_code AS cur_code, -- 英文币别编码
       b.receipt_no AS receipt_no, -- 回单编号
       b.trans_serial_no AS trans_serial_no, -- 交易流水号(可用于流水匹配)

       b.cd_sign AS cd_sign, -- 收付标识 0收 1付
       DATE_FORMAT(b.charage_date, '%Y-%m-%d') AS charge_date -- 记账日期
FROM bis_bank_receipt_dtl b
         LEFT JOIN bt_currency bc ON b.cur_id = bc.id and b.tenant_id = bc.tenant_id
WHERE b.charage_date > SUBDATE(SYSDATE(),200)
order by b.charage_date;



CREATE OR REPLACE VIEW erp_bank_receipt_img_view
AS
SELECT b.id AS serial_id, -- 电子回单唯一键
       b.tenant_id as tenant_id,


       b.SAVE_TYPE as save_type,--
       b.filetype AS file_type, -- 文件类型: pdf,png,jpg
       b.file_Path as file_path,-- 文件路径
       bb.ebillimg, -- 图片内容
       bb.pdf_to_img as pdf_to_img -- PDF转图片
FROM bis_bank_receipt_dtl b
         LEFT JOIN bis_bank_receipt_pdf bb ON b.id = bb.receipt_id and b.tenant_id = bb.tenant_id
WHERE b.charage_date > SUBDATE(SYSDATE(),200) ;