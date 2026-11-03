##修改人：zhours
##修改时间：2025.02.13
##增加：增加消息提醒模板
INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT '20b70e6be91911ef94f3005056a84efa', '183fc820962c411e9581ce3a7db5f552', '付款类需求清单下拨失败提醒', 'netbank:allocation:dialing:failure', '', '', null, '', null, 10,
       '您有一笔需求清单下拨失败，请关注。\\n资金需求单号：$!{billNo}\\n付款人：$!{payer}\\n付款账号：$!{payerAcc}\\n付款账号开户行：$!{payerBank}\\n收款人：$!{payee}\\n收款账号：$!{payeeAcc}\\n收款账号开户行：$!{payeeBank}\\n下拨金额：$!{amt}', 1,0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '20b70e6be91911ef94f3005056a84efa' LIMIT 1
    );


INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT '5cf07b09e91911ef94f3005056a84efa', '183fc820962c411e9581ce3a7db5f552', '承兑到期类需求单下拨失败提醒', 'netbank:allocation:bill:failure', '', '', null, '', null, 11,
       '您有一笔承兑到期类需求单下拨失败，资金中心账号余额不足，请关注。\\n资金需求单号：$!{billNo}\\n付款人：$!{payer}\\n付款账号：$!{payerAcc}\\n付款账号开户行：$!{payerBank}\\n收款人：$!{payee}\\n收款账号：$!{payeeAcc}\\n收款账号开户行：$!{payeeBank}\\n下拨金额：$!{amt}', 1,0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '5cf07b09e91911ef94f3005056a84efa' LIMIT 1
    );

INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE,WEIXIN_IND)
SELECT '814f2aede91911ef94f3005056a84efa', '183fc820962c411e9581ce3a7db5f552', '融资到期类需求单下拨失败提醒', 'netbank:allocation:cms:failure', '', '', null, '', null, 12,
       '您有一笔融资到期类需求单下拨失败，资金中心账号余额不足，请关注。\\n资金需求单号：$!{billNo}\\n付款人：$!{payer}\\n付款账号：$!{payerAcc}\\n付款账号开户行：$!{payerBank}\\n收款人：$!{payee}\\n收款账号：$!{payeeAcc}\\n收款账号开户行：$!{payeeBank}\\n下拨金额：$!{amt}', 1,0 FROM DUAL
WHERE NOT EXISTS (
        SELECT 1 FROM sys_notification_category WHERE ID = '814f2aede91911ef94f3005056a84efa' LIMIT 1
    );
