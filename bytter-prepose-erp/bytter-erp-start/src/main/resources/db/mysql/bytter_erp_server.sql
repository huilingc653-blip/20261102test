-- ----------------------------
-- 初始化参数数据
-- ----------------------------

INSERT INTO bis_server_param (ID, param_code, param_value, description,param_type)
select newid(), 'dataSize', '50', '批量数据条数','com' from dual
where not exists(select 1 from bis_server_param where param_code = 'dataSize');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description,param_type)
select newid(), 'dateRange', '33', '查询日期区间(天)','erp' from dual
where not exists(select 1 from bis_server_param where param_code = 'dateRange');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description,param_type)
select newid(), 'curCacheTime', '86400', '币别信息缓存时间（默认1天）：单位秒','erp' from dual
where not exists(select 1 from bis_server_param where param_code = 'curCacheTime');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description,param_type)
select newid(), 'voucherTypeCacheTime', '86400', '币别信息缓存时间（默认1天）：单位秒','erp' from dual
where not exists(select 1 from bis_server_param where param_code = 'voucherTypeCacheTime');
##GOGO

INSERT INTO bis_server_param (ID, param_code, param_value, description,param_type)
select newid(), 'bankCodeCacheTime', '86400', '联行号缓存时间（默认1天）：单位秒','erp' from dual
where not exists(select 1 from bis_server_param where param_code = 'bankCodeCacheTime');
##GOGO

INSERT INTO bis_server_param (ID, param_code, param_value, description,param_type)
select newid(), 'sysCorpCacheTime', '86400', '单位信息缓存时间（默认1天）：单位秒','erp' from dual
where not exists(select 1 from bis_server_param where param_code = 'sysCorpCacheTime');
##GOGO

INSERT INTO bis_server_param (ID, param_code, param_value, description,param_type)
select newid(), 'sysRegionCacheTime', '86400', '省市信息缓存时间（默认1天）：单位秒','erp' from dual
where not exists(select 1 from bis_server_param where param_code = 'sysRegionCacheTime');
##GOGO

## 添加 tenant_id 字段到 erp_payinfo 表
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
IF NOT EXISTS
(SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()  AND TABLE_NAME = 'erp_payinfo' AND COLUMN_NAME = 'tenant_id')
THEN
ALTER TABLE erp_payinfo ADD COLUMN tenant_id VARCHAR(32);
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

DROP TABLE IF EXISTS ERP_USER_INFO;
CREATE TABLE IF NOT  EXISTS ERP_USER_INFO  (
    ID varchar(32) COLLATE utf8mb4_bin NOT NULL,
    LOGIN_NAME varchar(100) COLLATE utf8mb4_bin NOT NULL,
    PASSWORD varchar(200) COLLATE utf8mb4_bin NOT NULL  COMMENT 'base64加密',
    TENANT_ID varchar(32) COLLATE utf8mb4_bin NOT NULL,
    STATUS decimal(1,0) NOT NULL DEFAULT 1 COMMENT '状态：1-正常；0-禁用',
    CREATE_TIME DATETIME NULL DEFAULT NULL,
    CREATE_BY VARCHAR(32) NULL DEFAULT NULL COLLATE utf8mb4_bin,
    UPDATE_TIME DATETIME NULL DEFAULT NULL,
    UPDATE_BY VARCHAR(32) NULL DEFAULT NULL COLLATE utf8mb4_bin,
    PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB COLLATE = utf8mb4_bin COMMENT = 'erp用户登录表' ROW_FORMAT = Dynamic;

INSERT INTO ERP_USER_INFO (ID, LOGIN_NAME, PASSWORD, TENANT_ID)
select 'db9725dc056811f1b212000c2921934f', 'bytter', 'QWRtaW5AMTIz', '402880425b8abcfb015b8ac16e980000' from dual
where not exists(select 1 from ERP_USER_INFO where id = 'db9725dc056811f1b212000c2921934f');
##GOGO

-- ----------------------------
-- 按 bfs_test 数据库当前 bis_server_param 内容重置初始化数据
-- ----------------------------
DELETE FROM bis_server_param;
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a1b4be384c011f19683005056a86feb', 'accDtlIsUpdate', '1', '账户明细查询：已存在的是否更新', 'trade');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a30a4b984c011f19683005056a86feb', 'bankAccCacheTime', '300', '账号信息缓存时间（默认5分钟）：单位秒', 'com');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a156a6e84c011f19683005056a86feb', 'dataSize', '50', '批量数据条数', 'com');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a2601d184c011f19683005056a86feb', 'isTermOfValidity', '0', '是否仅处理有效期内数据：1:是;0:否', 'trade');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a2279f484c011f19683005056a86feb', 'isVerifyPayment', '0', '是否支付验证：1:是;0:否', 'trade');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a1ed2a384c011f19683005056a86feb', 'isWishPayDate', '0', '是否期望支付日期：1:是;0:否', 'trade');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a2d3a6b84c011f19683005056a86feb', 'tenantCacheTime', '86400', '租户信息缓存时间（默认1天）：单位秒', 'com');
##GOGO
INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a29ad7384c011f19683005056a86feb', 'validityDay', '2', '仅处理有效天数内的付款数据', 'trade');
##GOGO
