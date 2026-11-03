
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '用途为“营收账号”的数量超过最大配置数量', 'basic.business.message.btbankacc.1004.max' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='basic.business.message.btbankacc.1004.max' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
