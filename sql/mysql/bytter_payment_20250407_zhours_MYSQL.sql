##修改人员:zrs
##修改内容:修改付款类需求清单下拨失败提醒
##修改日期:2025-04-07

update sys_notification_category
set CONTENT = '您有一笔需求清单下拨失败，请关注。\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n下拨金额：$!{amt}'
where TAG = 'netbank:allocation:dialing:failure';
##GOGO

update sys_notification_category
set CONTENT = '您有一笔承兑到期类需求单下拨失败，资金中心账号余额不足，请关注。\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n下拨金额：$!{amt}'
where TAG = 'netbank:allocation:bill:failure';
##GOGO

update sys_notification_category
set CONTENT = '您有一笔融资到期类需求单下拨失败，资金中心账号余额不足，请关注。\n资金需求单号：$!{billNo}\n付款人：$!{payer}\n付款账号：$!{payerAcc}\n付款账号开户行：$!{payerBank}\n收款人：$!{payee}\n收款账号：$!{payeeAcc}\n收款账号开户行：$!{payeeBank}\n下拨金额：$!{amt}'
where TAG = 'netbank:allocation:cms:failure';
##GOGO
