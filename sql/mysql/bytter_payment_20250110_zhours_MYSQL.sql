## 开户办理增加绑定人权限
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, URL)
select '96a7b544cf2911efaf25005056a84efa', '绑定人权限', '绑定人权限', 'netbank:bankAcc:btBankAcc:binder', 1, '402880265c67cc3c015c6837f65e0045', null, null, null, null, null from dual
where not exists (select 1 from sys_resources where PERMISSION ='netbank:bankAcc:btBankAcc:binder' limit 1);
commit;
##GOGO

## 添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '登录密码或支付密码加密失败！', 'basic.business.message.btbankacc.encryption.failure' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='basic.business.message.btbankacc.encryption.failure' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
