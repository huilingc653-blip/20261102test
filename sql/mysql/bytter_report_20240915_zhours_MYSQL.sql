
##修改人：zhours
##修改时间：2024.09.15
##修改原因：增加字典

insert into sys_dict_group (ID, TYPE_GROUP_CODE, TYPE_GROUP_NAME)
select '3ee20ebd736b11efb777005056a84efa', 'report.showType', '报表显示方式' from dual
where not exists (select 1 from sys_dict_group where ID ='3ee20ebd736b11efb777005056a84efa' limit 1);
##GOGO

insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'b460fe1a736b11efb777005056a84efa', '0', '汇总',null,'3ee20ebd736b11efb777005056a84efa',1  from dual
where not exists (select 1 from sys_dict where ID ='b460fe1a736b11efb777005056a84efa' limit 1);
##GOGO
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select 'b9151f43736b11efb777005056a84efa', '1', '明细',null,'3ee20ebd736b11efb777005056a84efa',2  from dual
where not exists (select 1 from sys_dict where ID ='b9151f43736b11efb777005056a84efa' limit 1);
##GOGO
