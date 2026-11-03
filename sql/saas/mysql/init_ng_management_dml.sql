insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()), 'zh_CN', '流程删除后不可恢复的，确定要批量删除吗？', 'bpm.message.delete.user.start.flow.unrecoverable' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bpm.message.delete.user.start.flow.unrecoverable' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()), 'zh_CN', '租户已存在单位数量：{0},不能小于这个数', 'sys.tenant.corp.number.min' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.corp.number.min' AND LANG_CODE = 'zh_CN');

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()), 'zh_CN', '租户已存在用户数量：{0},不能小于这个数', 'sys.tenant.user.number.min' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.user.number.min' AND LANG_CODE = 'zh_CN');

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()), 'zh_CN', '租户已存在银行账户数量：{0},不能小于这个数', 'sys.tenant.bankacc.number.min' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.bankacc.number.min' AND LANG_CODE = 'zh_CN');
##GOGO


INSERT INTO sys_notification_category_m (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE)
SELECT 'a4ebd29193c544abb60e51447256812c', '3d6dd6736e2b466e91d85b715b82a9c3', '系统内管端配置修改', 'sys:tenant:record:update', 'bda2047eb9cb42e39d7670e43ea5e828', '/sys/tenant/list', null,
       '尊敬的管理员，系统内部管理配置已更新： &bull; 用户数量上限：$!{userNumber} &bull; 单位数量上限：$!{corpNumber} &bull; 银行账户数量上限：$!{accountNumber}', 1, 1, '您有一条系统内管端配置修改,请查看!', 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_notification_category_m WHERE ID = 'a4ebd29193c544abb60e51447256812c' LIMIT 1
);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()),
       'zh_CN',
       '当前租户网点/单位数量：{0}; 需要添加网点/单位数量：{1};客户签约时设置的网点/单位数量：{2};已超出客户签约时设置的网点/单位数量，请确认',
       'sys.tenant.corp.num.over.limit'
from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.corp.num.over.limit' AND LANG_CODE = 'zh_CN');
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()),
       'zh_CN',
       '当前租户账户数量：{0}; 需要添加账户数量：{1}; 客户签约时设置的账户数量：{2}; 已超出客户签约时设置的账户数量，请确认',
       'sys.tenant.account.num.over.limit'
from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.account.num.over.limit' AND LANG_CODE = 'zh_CN');
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select md5(uuid()),
       'zh_CN',
       '当前租户用户数量：{0}; 需要添加用户数量：{1}; 客户签约时设置的用户数量：{2}; 已超出客户签约时设置的用户数量，请确认',
       'sys.tenant.user.num.over.limit'
from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='sys.tenant.user.num.over.limit' AND LANG_CODE = 'zh_CN');
##GOGO

