##修改人:zhaohc
##修改内容:付款办理增强-应收票据批量背书
##修改时间:2024-05-09

##新增菜单资源 查询资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '647113a1cae74c4aa2d875b9b7817688', '应收选票资源', '应收选票资源', 'netbank:netBank:nisBillHead:batchSelectAccebill', 1, '4028802f5c2eac34015c2ec1b9c40036' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_resources WHERE ID = '647113a1cae74c4aa2d875b9b7817688' LIMIT 1);
##GOGO

##国际化数据
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a28bdfd5402840908d5772ef3884b9ca', 'zh_CN', '应收选票', 'netbank.button.batchSelectAccebill' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='netbank.button.batchSelectAccebill' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a7a52ea12b9245bfac757c51dcc28e97', 'zh_CN', '应收选票,系统结算方式必须是应收票据', 'nis.selectaccebill.message.vouchertype' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.selectaccebill.message.vouchertype' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'c63697aa97744e1784ad1d3f52969250', 'zh_CN', '应收选票,付款单位需一致', 'nis.selectaccebill.message.corp.same' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.selectaccebill.message.corp.same' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'd2c7ab00b785410f98c330acca264bd6', 'zh_CN', '应收选票,是否已付款必须为否', 'nis.selectaccebill.message.ispayed' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.selectaccebill.message.ispayed' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '0788b171f2cc47599044a85d64622256', 'zh_CN', '是否已选票', 'table.nisBillhead.isSelectedAcceBill' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.nisBillhead.isSelectedAcceBill' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO































