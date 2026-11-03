##修改人：zhours
##修改时间：2024.10.21
##修改原因：新增【月度贷款明细表】

INSERT INTO `sys_menu` (`ID`, `CODE`, `DESCRIPTION`, `NAME`, `PARENT_ID`, `PARENT_IDS`, `SORT`, `STATUS`, `URL`, `NET_ID`, `ICON`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`, `IS_MODULE`, `IS_EXTEND`, `OLD_ICON`)
select 'da8a1a178f7211efaac1005056a84efa', '7', '月度贷款明细表', '月度贷款明细表', '8210f38c7eb045ba88578c3086ab434e', NULL, 7, 1, '/cms/provide/loaninfo/monthlyLoanDtl', NULL, NULL, NULL, NULL, NULL, NULL, 0, 1, NULL from dual
where not exists (select 1 from sys_menu where ID ='da8a1a178f7211efaac1005056a84efa' );
##GOGO

INSERT INTO `sys_resources` (`ID`, `DESCRIPTION`, `NAME`, `PERMISSION`, `STATUS`, `MENU_ID`)
select 'f3467fe78f7411efaac1005056a84efa', '查询资源', '查询资源', 'cms:provide:info:monthlyLoan', 1, 'da8a1a178f7211efaac1005056a84efa' from dual
where not exists (select 1 from sys_resources where ID ='f3467fe78f7411efaac1005056a84efa' );
##GOGO
