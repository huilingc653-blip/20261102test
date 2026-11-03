##修改人：qijh
##修改时间：2024.05.17
##修改原因：大参林项目脚本

## 新增字典 nisBillhead.status
insert into sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT)
select '5afc233d1f944589ad2220d829dcbe37', '-3', '暂挂',null,'4028802f5c808b3b015c808ecf020007',15  from dual
where not exists (select 1 from sys_dict where ID ='5afc233d1f944589ad2220d829dcbe37' limit 1);
##GOGO

