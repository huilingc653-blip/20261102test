
##修改人：zrs
##修改内容：新增多级资金日报表
##修改时间：2025-09-10
INSERT INTO sys_menu (ID, DESCRIPTION, NAME, PARENT_ID,  SORT, STATUS, URL, IS_MODULE, IS_EXTEND)
SELECT 'ccde2f9a8df711f0a544005056a84efa','多级资金日报表', '多级资金日报表', '07cbbb8132de427eb53c9d624fb7ee22',  32, 1, '/report/allocationDaily/multiLeveList', 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = 'ccde2f9a8df711f0a544005056a84efa' LIMIT 1
);
commit;
##GOGO

##修改人：zrs
##修改内容：新增多级资金日报表查询资源
##修改时间：2025-09-10
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'd0db8d3d8df711f0a544005056a84efa', '查询资源', '查询资源', 'netbank:nisAllocationDaily:multiLeve:query', 1, 'ccde2f9a8df711f0a544005056a84efa' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='d0db8d3d8df711f0a544005056a84efa' LIMIT 1
);
##GOGO
