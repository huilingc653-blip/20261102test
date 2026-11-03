##修改人：zhours
##修改时间：2025.03.14
##添加 国际化
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select newid(), 'zh_CN', '审批失败：工资单{0}获取明细失败，无法生成指令！', 'nis.wage.message.detail.fail.exc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='nis.wage.message.detail.fail.exc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
