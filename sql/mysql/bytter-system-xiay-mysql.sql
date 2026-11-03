##修改人:xiay
##修改内容:国际化
##修改时间:2026-02-10

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '074506deee195adabd2fda890472ef1a', 'zh_CN', '规则不能为空或逻辑条件有误，请检查!', 'nis.assign.message.bustype.matchrule.parenthesis.error' from dual
where not exists (select 1 from sys_multi_lang where ID = '074506deee195adabd2fda890472ef1a' limit 1);
##GOGO


insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '177a8358cf604a5296b782d5fc2212ca', 'zh_CN', '该策略已存在，不可重复提交！', 'table.receipt.execute.strategyNotRepeat' from dual
where not exists (select 1 from sys_multi_lang where ID = '177a8358cf604a5296b782d5fc2212ca' limit 1);
##GOGO



insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2776e88590cc4a21932c0c741a08798e', 'zh_CN', '还款在途本金', 'table.cmsProvideLoanRepay.moneyPassage' from dual
where not exists (select 1 from sys_multi_lang where ID = '2776e88590cc4a21932c0c741a08798e' limit 1);
##GOGO
