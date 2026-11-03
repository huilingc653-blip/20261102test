##修改人：qijh
##修改时间：2024.05.13
##修改原因：大参林项目脚本

##新增菜单资源 生成上级需求单资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT '6feada8fc33f4481baa6089a69be2ebc', '生成上级需求单资源', '生成上级需求单资源', 'netbank:nisDemand:super', 1, '2a9b7b7435494fa29bdbd174e7121905' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = '6feada8fc33f4481baa6089a69be2ebc' LIMIT 1
);
##GOGO

##新增菜单资源 生成上级需求单资源
INSERT INTO sys_resources (ID, DESCRIPTION, NAME, PERMISSION, STATUS, MENU_ID)
SELECT 'd96e66588d5646319e7dfcf69c7be68f', '生成下拨指令资源', '生成下拨指令资源', 'netbank:nisDemand:allocation', 1, '2a9b7b7435494fa29bdbd174e7121905' FROM DUAL
 WHERE NOT EXISTS (
        SELECT 1 FROM sys_resources WHERE ID = 'd96e66588d5646319e7dfcf69c7be68f' LIMIT 1
);
##GOGO

