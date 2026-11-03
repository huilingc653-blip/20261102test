##修改人：zhours
##修改时间：2025.06.26
##增加：增加消息提醒模板
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT 'fc9d09dc525011f0a25e005056a84efa', '183fc820962c411e9581ce3a7db5f552', '付款类需求清单差额下拨提醒', 'netbank:allocation:failure:difference', '', '', null, '', null, 10,
       '您有一笔需求清单下拨失败，请关注。\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n下拨金额：$!{downAmt}\n需求金额：$!{amt}\n差异金额：$!{diffAmt}',
       1,0 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category WHERE ID = 'fc9d09dc525011f0a25e005056a84efa' LIMIT 1
);
