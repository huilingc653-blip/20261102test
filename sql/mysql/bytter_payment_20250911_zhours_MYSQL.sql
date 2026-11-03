
##修改人：zrs
##修改内容：新增多级资金收支表
##修改时间：2025-09-11
INSERT INTO sys_menu (ID, DESCRIPTION, NAME, PARENT_ID,  SORT, STATUS, URL, IS_MODULE, IS_EXTEND)
SELECT 'a6a67c588ed511f0a544005056a84efa','多级资金收支表', '多级资金收支表', '07cbbb8132de427eb53c9d624fb7ee22',  32, 1, '/report/incomeAndExpenditureOfFunds/multiLeveList', 0, 1 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_menu WHERE ID = 'a6a67c588ed511f0a544005056a84efa' LIMIT 1
);
commit;
##GOGO

##修改人：zrs
##修改内容：新增多级资金收支表查询资源
##修改时间：2025-09-11
insert into SYS_RESOURCES (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'ae0095858ed511f0a544005056a84efa', '查询资源', '查询资源', 'report:incomeAndExp:multiLeve:query', 1, 'a6a67c588ed511f0a544005056a84efa' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM SYS_RESOURCES WHERE ID='ae0095858ed511f0a544005056a84efa' LIMIT 1
);
##GOGO
