-- ----------------------------
-- 初始化参数数据
-- ----------------------------
MERGE INTO bis_server_param dst
USING (SELECT SYS_GUID() id, 'dataSize' param_code, '50' param_value, '批量数据条数' description, 'com' param_type FROM dual) src
ON (dst.param_code = src.param_code)
WHEN NOT MATCHED THEN INSERT (id, param_code, param_value, description, param_type)
VALUES (src.id, src.param_code, src.param_value, src.description, src.param_type);
/
MERGE INTO bis_server_param dst
USING (SELECT SYS_GUID() id, 'dateRange' param_code, '33' param_value, '查询日期区间(天)' description, 'erp' param_type FROM dual) src
ON (dst.param_code = src.param_code)
WHEN NOT MATCHED THEN INSERT (id, param_code, param_value, description, param_type)
VALUES (src.id, src.param_code, src.param_value, src.description, src.param_type);
/
MERGE INTO bis_server_param dst
USING (SELECT SYS_GUID() id, 'curCacheTime' param_code, '86400' param_value, '币别信息缓存时间（默认1天）：单位秒' description, 'erp' param_type FROM dual) src
ON (dst.param_code = src.param_code)
WHEN NOT MATCHED THEN INSERT (id, param_code, param_value, description, param_type)
VALUES (src.id, src.param_code, src.param_value, src.description, src.param_type);
/
MERGE INTO bis_server_param dst
USING (SELECT SYS_GUID() id, 'voucherTypeCacheTime' param_code, '86400' param_value, '币别信息缓存时间（默认1天）：单位秒' description, 'erp' param_type FROM dual) src
ON (dst.param_code = src.param_code)
WHEN NOT MATCHED THEN INSERT (id, param_code, param_value, description, param_type)
VALUES (src.id, src.param_code, src.param_value, src.description, src.param_type);
/
MERGE INTO bis_server_param dst
USING (SELECT SYS_GUID() id, 'bankCodeCacheTime' param_code, '86400' param_value, '联行号缓存时间（默认1天）：单位秒' description, 'erp' param_type FROM dual) src
ON (dst.param_code = src.param_code)
WHEN NOT MATCHED THEN INSERT (id, param_code, param_value, description, param_type)
VALUES (src.id, src.param_code, src.param_value, src.description, src.param_type);
/
MERGE INTO bis_server_param dst
USING (SELECT SYS_GUID() id, 'sysCorpCacheTime' param_code, '86400' param_value, '单位信息缓存时间（默认1天）：单位秒' description, 'erp' param_type FROM dual) src
ON (dst.param_code = src.param_code)
WHEN NOT MATCHED THEN INSERT (id, param_code, param_value, description, param_type)
VALUES (src.id, src.param_code, src.param_value, src.description, src.param_type);
/
MERGE INTO bis_server_param dst
USING (SELECT SYS_GUID() id, 'sysRegionCacheTime' param_code, '86400' param_value, '省市信息缓存时间（默认1天）：单位秒' description, 'erp' param_type FROM dual) src
ON (dst.param_code = src.param_code)
WHEN NOT MATCHED THEN INSERT (id, param_code, param_value, description, param_type)
VALUES (src.id, src.param_code, src.param_value, src.description, src.param_type);
/

-- 为 erp_payinfo 表补充 tenant_id 字段（字段不存在时）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_tab_columns
    WHERE table_name = 'ERP_PAYINFO' AND column_name = 'TENANT_ID';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE erp_payinfo ADD (tenant_id VARCHAR2(32))';
    END IF;
END;
/

-- ERP 用户登录表（表不存在时创建）
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'ERP_USER_INFO';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE ERP_USER_INFO (
            ID VARCHAR2(32) NOT NULL,
            LOGIN_NAME VARCHAR2(100) NOT NULL,
            PASSWORD VARCHAR2(200) NOT NULL,
            TENANT_ID VARCHAR2(32) NOT NULL,
            STATUS NUMBER(1,0) DEFAULT 1 NOT NULL,
            CREATE_TIME DATE,
            CREATE_BY VARCHAR2(32),
            UPDATE_TIME DATE,
            UPDATE_BY VARCHAR2(32),
            CONSTRAINT PK_ERP_USER_INFO PRIMARY KEY (ID)
        )';
        EXECUTE IMMEDIATE 'COMMENT ON TABLE ERP_USER_INFO IS ''erp用户登录表''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN ERP_USER_INFO.PASSWORD IS ''base64加密''';
        EXECUTE IMMEDIATE 'COMMENT ON COLUMN ERP_USER_INFO.STATUS IS ''状态：1-正常；0-禁用''';
    END IF;
END;
/

MERGE INTO ERP_USER_INFO dst
USING (SELECT 'db9725dc056811f1b212000c2921934f' id,
              'bytter' login_name, 'QWRtaW5AMTIz' password,
              '402880425b8abcfb015b8ac16e980000' tenant_id FROM dual) src
ON (dst.id = src.id)
WHEN NOT MATCHED THEN INSERT (ID, LOGIN_NAME, PASSWORD, TENANT_ID)
VALUES (src.id, src.login_name, src.password, src.tenant_id);
/
COMMIT;
/

-- ----------------------------
-- 按 bfs_test 数据库当前 bis_server_param 内容重置初始化数据
-- ----------------------------
BEGIN
    DELETE FROM bis_server_param;
    INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a1b4be384c011f19683005056a86feb', 'accDtlIsUpdate', '1', '账户明细查询：已存在的是否更新', 'trade');
    INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a30a4b984c011f19683005056a86feb', 'bankAccCacheTime', '300', '账号信息缓存时间（默认5分钟）：单位秒', 'com');
    INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a156a6e84c011f19683005056a86feb', 'dataSize', '50', '批量数据条数', 'com');
    INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a2601d184c011f19683005056a86feb', 'isTermOfValidity', '0', '是否仅处理有效期内数据：1:是;0:否', 'trade');
    INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a2279f484c011f19683005056a86feb', 'isVerifyPayment', '0', '是否支付验证：1:是;0:否', 'trade');
    INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a1ed2a384c011f19683005056a86feb', 'isWishPayDate', '0', '是否期望支付日期：1:是;0:否', 'trade');
    INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a2d3a6b84c011f19683005056a86feb', 'tenantCacheTime', '86400', '租户信息缓存时间（默认1天）：单位秒', 'com');
    INSERT INTO bis_server_param (ID, param_code, param_value, description, param_type) VALUES ('5a29ad7384c011f19683005056a86feb', 'validityDay', '2', '仅处理有效天数内的付款数据', 'trade');
    COMMIT;
END;
/
