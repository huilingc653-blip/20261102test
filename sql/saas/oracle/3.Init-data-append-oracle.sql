--
-- Table structure for table 'OAUTH_CLIENT_DETAILS'
--
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE OAUTH_CLIENT_DETAILS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE OAUTH_CLIENT_DETAILS (
    client_id VARCHAR2(256) NOT NULL PRIMARY KEY,
    resource_ids VARCHAR2(256) NULL,
    client_secret VARCHAR2(256) NULL,
    scope VARCHAR2(256) NULL,
    authorized_grant_types VARCHAR2(256) NULL,
    web_server_redirect_uri VARCHAR2(256) NULL,
    authorities VARCHAR2(256) NULL,
    access_token_validity NUMBER(10) NULL,
    refresh_token_validity NUMBER(10) NULL,
    additional_information VARCHAR2(4000) NULL,
    autoapprove VARCHAR2(256) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE OAUTH_CLIENT_DETAILS IS '客户端信息';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.client_id IS '客户端ID';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.resource_ids IS '资源ID集合,多个资源时用逗号(,) 分隔';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.client_secret IS '客户端密匙';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.scope IS '客户端申请的权限范围';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.authorized_grant_types IS '客户端支持的grant_type';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.web_server_redirect_uri IS '重定向URI';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.authorities IS '客户端所拥有的Spring Security的权限值，多个用逗号(,) 分隔';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.access_token_validity IS '访问令牌有效时间值(单位:秒)';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.refresh_token_validity IS '更新令牌有效时间值(单位:秒)';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.additional_information IS '预留字段';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.autoapprove IS '用户是否自动Approval操作';
COMMENT ON COLUMN OAUTH_CLIENT_DETAILS.TENANT_ID IS '租户ID';

CREATE INDEX IDX_OAUTH_CLIENT_DETAILS_T ON OAUTH_CLIENT_DETAILS (TENANT_ID);

-- Dump completed for table 'OAUTH_CLIENT_DETAILS'


--
-- Dumping data for table 'OAUTH_CLIENT_DETAILS'
--
INSERT INTO OAUTH_CLIENT_DETAILS (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, TENANT_ID)
  SELECT 'h5_client', NULL, 'e989d46fdbc1c376c19a43aaf85227a4', 'read', 'client_credentials,mobile,password,refresh_token', NULL, NULL, 43200, 108000, NULL, NULL, '1000' FROM DUAL UNION ALL
  SELECT 'mobile', NULL, '{bcrypt}$2a$10$g43U8ArVUCCY9/ATbLN2BuFUds8dgtX3WNu//Oq7PPbf.b6q1xerO', 'read', 'client_credentials,authorization_code,mobile,password,refresh_token,sso_token,certificate,phone', ' ', NULL, 7200, 108000, NULL, NULL, '1000' FROM DUAL UNION ALL
  SELECT 'test_client', NULL, '{bcrypt}$2a$10$g43U8ArVUCCY9/ATbLN2BuFUds8dgtX3WNu//Oq7PPbf.b6q1xerO', 'read', 'client_credentials,authorization_code,mobile,password,refresh_token,sso_token,certificate', 'http://baidu.com', NULL, 7200, 108000, NULL, NULL, '1000' FROM DUAL;COMMIT;


--
-- Table structure for table 'SYS_CONFIG'
--
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE SYS_CONFIG';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE SYS_CONFIG (
    ID VARCHAR2(32) NOT NULL PRIMARY KEY,
    "TYPE" VARCHAR2(50) NULL,
    NAME VARCHAR2(50) NULL,
    VALUE VARCHAR2(500) NULL,
    CHANGE VARCHAR2(1) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE SYS_CONFIG IS '系统参数配置';
COMMENT ON COLUMN SYS_CONFIG."TYPE" IS '配置类型';
COMMENT ON COLUMN SYS_CONFIG.NAME IS '配置名';
COMMENT ON COLUMN SYS_CONFIG.VALUE IS '配置值';
COMMENT ON COLUMN SYS_CONFIG.TENANT_ID IS '租户ID';

CREATE UNIQUE INDEX TYPE ON SYS_CONFIG (TYPE, NAME, TENANT_ID);
CREATE INDEX IDX_SYS_CONFIG_TENANT_ID ON SYS_CONFIG (TENANT_ID);

-- Dump completed for table 'SYS_CONFIG'


--
-- Dumping data for table 'SYS_CONFIG'
--
INSERT INTO SYS_CONFIG (ID, "TYPE", NAME, VALUE, CHANGE, TENANT_ID)
  SELECT '003ec77ebf7b4fe7a402b8af12d5e425', 'sys', 'scfCode', '', '', '000' FROM DUAL UNION ALL
  SELECT '050edb0b3f3e4532a67c0cda84f81ae9', 'sys', 'scfUrl', '', '', '000' FROM DUAL UNION ALL
  SELECT '0ab73eec00c34720a961c1895765f04f', 'sys', 'faceVerify', '0', '', '000' FROM DUAL UNION ALL
  SELECT '0e8963af4ef14a1e8b810cf8156233c3', 'sys', 'i18nEnable', '1', '', '000' FROM DUAL UNION ALL
  SELECT '13cfb9fe52c743908438d2dd13a920d4', 'sys', 'cspList', '', '', '000' FROM DUAL UNION ALL
  SELECT '2a4c9e54dc4948ab8f86babb6ee9b3c5', 'sys', 'webServiceNeedAuth', '1', '', '000' FROM DUAL UNION ALL
  SELECT '2dcac588d5c94cb7b5d7280ffd03fbbb', 'sys', 'uiStyle', '1', '', '000' FROM DUAL UNION ALL
  SELECT '402880415c81e9d8015c81f4162e0000', 'sys', 'taskManage', '1', '', '000' FROM DUAL UNION ALL
  SELECT '402880425c3d138e015c3d264c0e000c', 'sys', 'companyName', '拜特科技-开发版本库', '', '000' FROM DUAL UNION ALL
  SELECT '402880425c3d138e015c3d264c210011', 'sys', 'passwordLimit', '6', '', '000' FROM DUAL UNION ALL
  SELECT '402880425c3d138e015c3d264c250012', 'sys', 'loginOpenIpLimit', '0', '', '000' FROM DUAL UNION ALL
  SELECT '402880425c3d138e015c3d264c390016', 'sys', 'encrypt', '0', '', '000' FROM DUAL UNION ALL
  SELECT '402880425c3d138e015c3d264c3f0018', 'sys', 'attachmentSize', '4', '', '000' FROM DUAL UNION ALL
  SELECT '402880425c3d138e015c3dab9d9b001f', 'sys', 'licenceOverdueNotice', '0', '', '000' FROM DUAL UNION ALL
  SELECT '402880425c3d138e015c3dab9d9e0020', 'sys', 'licenceOverdueNoticeDay', '3', '', '000' FROM DUAL UNION ALL
  SELECT '4191ae379d5945598e39e0d9765550b2', 'sys', 'passwordRepetitions', '3', '', '000' FROM DUAL UNION ALL
  SELECT '56188119acce44cba5de124843863104', 'sys', 'appPageSize', '15', '', '000' FROM DUAL UNION ALL
  SELECT '64c2e0a7941147c3adf686121d0f2422', 'sys', 'jpushSecretKey', '6a1e7608a50177ce9d4d9c3b', '', '000' FROM DUAL UNION ALL
  SELECT '75786246d4564f80bfacaddf570f9bbb', 'sys', 'jpushKey', '0095af821d1cb4125b5c9dd1', '', '000' FROM DUAL UNION ALL
  SELECT '7726aa7de22a11f0a464005056b9095f', 'license', 'corpNumber', '0', '', '000' FROM DUAL UNION ALL
  SELECT '772cf645e22a11f0a464005056b9095f', 'license', 'companyNameForeign', 'Bytter Company', '', '000' FROM DUAL UNION ALL
  SELECT '7735acc7e22a11f0a464005056b9095f', 'license', 'companyName', '深圳市拜特科技股份有限公司', '', '000' FROM DUAL UNION ALL
  SELECT '773d582ae22a11f0a464005056b9095f', 'license', 'endDate', '2026-12-30', '', '000' FROM DUAL UNION ALL
  SELECT '7742ecdde22a11f0a464005056b9095f', 'license', 'controlUserType', 'O', '', '000' FROM DUAL UNION ALL
  SELECT '774821b6e22a11f0a464005056b9095f', 'license', 'mobile', '0,1', '', '000' FROM DUAL UNION ALL
  SELECT '774e4c35e22a11f0a464005056b9095f', 'license', 'netNumber', '0', '', '000' FROM DUAL UNION ALL
  SELECT '77569b3ae22a11f0a464005056b9095f', 'license', 'bankAccountNumber', '0', '', '000' FROM DUAL UNION ALL
  SELECT '775b876ce22a11f0a464005056b9095f', 'license', 'type', 'R', '', '000' FROM DUAL UNION ALL
  SELECT '7760bc78e22a11f0a464005056b9095f', 'license', 'controlUserNumber', '200', '', '000' FROM DUAL UNION ALL
  SELECT '804904fee4684893967da996ad3688e7', 'sys', 'loginType', '0', '', '000' FROM DUAL UNION ALL
  SELECT '8eee7133235448b0a0f1fdca26c2bcc0', 'sys', 'faceLogin', '0', '', '000' FROM DUAL UNION ALL
  SELECT 'a9bb1f066e0544708e72b764e772bad5', 'sys', 'attachmentFormat', '', '', '000' FROM DUAL UNION ALL
  SELECT 'ade4318ef32045a98935866bf245706d', 'sys', 'issuer', '', '', '000' FROM DUAL UNION ALL
  SELECT 'aee7efd20c194c648ab1278bc4a5b370', 'sys', 'dualManager', '0', '', '000' FROM DUAL UNION ALL
  SELECT 'ba4ba9abc1b04b4190cda95dfa1fd3ff', 'sys', 'ipWhiteList', '192.168.0.\n192.168.1.10-192.168.1.100\n10.247.120.', '', '000' FROM DUAL UNION ALL
  SELECT 'be0f3989126145c7ab335238c88b2915', 'sys', 'attachmentMaxSize', '50', '', '000' FROM DUAL UNION ALL
  SELECT 'c2175906e8a04b339e24c796ff0eea7f', 'sys', 'kickOut', '0', '', '000' FROM DUAL UNION ALL
  SELECT 'c479400be7de49c1b5e19be5637b6de8', 'sys', 'scfState', '0', '', '000' FROM DUAL UNION ALL
  SELECT 'd9a623d6e6cb4648b41080c227e74364', 'sys', 'licenceOverNoticeDay', '30', '', '000' FROM DUAL;COMMIT;


--
-- Table structure for table 'XXL_JOB_GROUP'
--
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE XXL_JOB_GROUP';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE XXL_JOB_GROUP (
    id NUMBER(10) NOT NULL PRIMARY KEY,
    app_name VARCHAR2(64) NOT NULL,
    title VARCHAR2(12) NOT NULL,
    address_type NUMBER DEFAULT '0' NOT NULL,
    address_list CLOB NULL,
    update_time DATE NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON COLUMN XXL_JOB_GROUP.app_name IS '执行器AppName';
COMMENT ON COLUMN XXL_JOB_GROUP.title IS '执行器名称';
COMMENT ON COLUMN XXL_JOB_GROUP.address_type IS '执行器地址类型：0=自动注册、1=手动录入';
COMMENT ON COLUMN XXL_JOB_GROUP.address_list IS '执行器地址列表，多地址逗号分隔';
COMMENT ON COLUMN XXL_JOB_GROUP.TENANT_ID IS '租户ID';

CREATE INDEX IDX_XXL_JOB_GROUP_TENANT_ID ON XXL_JOB_GROUP (TENANT_ID);

-- Dump completed for table 'XXL_JOB_GROUP'


--
-- Dumping data for table 'XXL_JOB_GROUP'
--
INSERT INTO XXL_JOB_GROUP (id, app_name, title, address_type, address_list, update_time, TENANT_ID)
  SELECT 3, 'bytter-prepose-bill', 'billjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 4, 'bytter-prepose-check', 'checkjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 5, 'bytter-prepose-credential', 'credential', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 7, 'bytter-prepose-erp', 'erpjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 9, 'bytter-prepose-letter', 'letterjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 11, 'bytter-service-bdlink', 'bdlinkjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 12, 'bytter-service-fundwarn', 'fundwarn', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 13, 'bytter-service-increment', 'increment', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 14, 'bytter-service-payment', 'payment', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 15, 'bytter-system', 'systemjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 16, 'bytter-service-ras', 'rasjob', 0, NULL, SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 17, 'bytter-manager-account', 'accountjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 18, 'bytter-workflow', 'workflowjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 19, 'bytter-manager-plan', 'planjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL UNION ALL
  SELECT 20, 'bytter-service-report', 'reportjob', 0, TO_CLOB(''), SYSDATE, '000' FROM DUAL;COMMIT;


--
-- Table structure for table 'XXL_JOB_INFO'
--
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE XXL_JOB_INFO';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE XXL_JOB_INFO (
    id NUMBER(10) NOT NULL PRIMARY KEY,
    job_group NUMBER(10) NOT NULL,
    job_desc VARCHAR2(255) NOT NULL,
    add_time DATE NULL,
    update_time DATE NULL,
    author VARCHAR2(64) NULL,
    alarm_email VARCHAR2(255) NULL,
    schedule_type VARCHAR2(50) DEFAULT 'NONE' NOT NULL,
    schedule_conf VARCHAR2(128) NULL,
    misfire_strategy VARCHAR2(50) DEFAULT 'DO_NOTHING' NOT NULL,
    executor_route_strategy VARCHAR2(50) NULL,
    executor_handler VARCHAR2(255) NULL,
    executor_param VARCHAR2(512) NULL,
    executor_block_strategy VARCHAR2(50) NULL,
    executor_timeout NUMBER(10) DEFAULT '0' NOT NULL,
    executor_fail_retry_count NUMBER(10) DEFAULT '0' NOT NULL,
    glue_type VARCHAR2(50) NOT NULL,
    glue_source CLOB NULL,
    glue_remark VARCHAR2(128) NULL,
    glue_updatetime DATE NULL,
    child_jobid VARCHAR2(255) NULL,
    trigger_status NUMBER DEFAULT '0' NOT NULL,
    trigger_last_time NUMBER(19) DEFAULT '0' NOT NULL,
    trigger_next_time NUMBER(19) DEFAULT '0' NOT NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON COLUMN XXL_JOB_INFO.job_group IS '执行器主键ID';
COMMENT ON COLUMN XXL_JOB_INFO.author IS '作者';
COMMENT ON COLUMN XXL_JOB_INFO.alarm_email IS '报警邮件';
COMMENT ON COLUMN XXL_JOB_INFO.schedule_type IS '调度类型';
COMMENT ON COLUMN XXL_JOB_INFO.schedule_conf IS '调度配置，值含义取决于调度类型';
COMMENT ON COLUMN XXL_JOB_INFO.misfire_strategy IS '调度过期策略';
COMMENT ON COLUMN XXL_JOB_INFO.executor_route_strategy IS '执行器路由策略';
COMMENT ON COLUMN XXL_JOB_INFO.executor_handler IS '执行器任务handler';
COMMENT ON COLUMN XXL_JOB_INFO.executor_param IS '执行器任务参数';
COMMENT ON COLUMN XXL_JOB_INFO.executor_block_strategy IS '阻塞处理策略';
COMMENT ON COLUMN XXL_JOB_INFO.executor_timeout IS '任务执行超时时间，单位秒';
COMMENT ON COLUMN XXL_JOB_INFO.executor_fail_retry_count IS '失败重试次数';
COMMENT ON COLUMN XXL_JOB_INFO.glue_type IS 'GLUE类型';
COMMENT ON COLUMN XXL_JOB_INFO.glue_source IS 'GLUE源代码';
COMMENT ON COLUMN XXL_JOB_INFO.glue_remark IS 'GLUE备注';
COMMENT ON COLUMN XXL_JOB_INFO.glue_updatetime IS 'GLUE更新时间';
COMMENT ON COLUMN XXL_JOB_INFO.child_jobid IS '子任务ID，多个逗号分隔';
COMMENT ON COLUMN XXL_JOB_INFO.trigger_status IS '调度状态：0-停止，1-运行';
COMMENT ON COLUMN XXL_JOB_INFO.trigger_last_time IS '上次调度时间';
COMMENT ON COLUMN XXL_JOB_INFO.trigger_next_time IS '下次调度时间';
COMMENT ON COLUMN XXL_JOB_INFO.TENANT_ID IS '租户ID';

CREATE INDEX IDX_XXL_JOB_INFO_TENANT_ID ON XXL_JOB_INFO (TENANT_ID);

-- Dump completed for table 'XXL_JOB_INFO'


INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (202, 17, '内部贷款发放记账提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:36:55', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0/1 * * * * ?', 'DO_NOTHING', 'FIRST', 'EnterpriseLoanService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (203, 3, '承兑汇票-自动认领银行流水', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-05 16:18:39', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 2 * * ?', 'DO_NOTHING', 'FIRST', 'AbClaimService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765954800000, 1765994400000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (204, 14, '收款指令交易成功', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:00:31', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'BisGatherCmdService', 'netbank:bdlink:bisgathercmd:success', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (205, 14, '电子回单历史数据迁移', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:00:27', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '59 0/1 * 1/1 * ?', 'DO_NOTHING', 'FIRST', 'EleAotoBisBankReceiptDtlHisService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (206, 3, '合同管理-合同预警消息提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:35:27', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'ConWarnNotificationService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (207, 11, '汇款指令交易失败', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:34:59', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'BisForeignExcService', 'foreign:foreignexc:bisforeignexc:fail', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (208, 3, '对外担保合同到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:34:23', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 1/1 * ?', 'DO_NOTHING', 'FIRST', 'GuaExtContractInfoService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (209, 17, '内部贷款还款记账提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:33:56', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0/1 * * * * ?', 'DO_NOTHING', 'FIRST', 'EnterpriseRepaymentService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (210, 16, '营收稽核-历史、作废数据迁移', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:38:24', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 * * * ?', 'DO_NOTHING', 'FIRST', 'RasHisDataMoveService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (211, 11, '银行接口掉线提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:05:58', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 * * * ?', 'DO_NOTHING', 'FIRST', 'BisBifMonitorService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (213, 9, '保函到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:31:55', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'GuaGuaranteeInfoService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (214, 3, '银行借款还款到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:17:15', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/1 * * * ? *', 'DO_NOTHING', 'FIRST', 'CmsProvideInterestService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (215, 11, '银行回单自动勾对流水', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-07-20 09:29:00', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9/1 * * ?', 'DO_NOTHING', 'FIRST', 'BisBankReceiptBlendingServer', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1721440800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (216, 3, '承兑汇票-余额设置-票据历史余额设置', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:29:37', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillAccBalService', 'autoTodayBillAccBal', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (217, 15, '基础平台-联行号表和客商账户表/银行账号表行名不一样同步任务', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:27:51', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 7 * * ?', 'DO_NOTHING', 'FIRST', 'BtInputBankInfoService', 'externalAcc=true,bankAcc=true', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (218, 3, '承兑汇票-正面信息同步-待签收数据来源', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:25:54', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/30 8-20 * * ?', 'DO_NOTHING', 'FIRST', 'AbDraftExcService', 'autoQueryBillInfoBySign', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (219, 3, '承兑汇票-正面背面信息同步-应付未结清、应收持有业务数据来源', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-08 13:45:47', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/5 8-21 * * ?', 'DO_NOTHING', 'FIRST', 'AbDraftExcService', 'autoQueryBillInfoByHold', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765971000000, 1765971300000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (220, 3, '承兑汇票-把所有待签收业务数据变为撤回状态重新同步待签收票据', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:24:54', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 3 * * ?', 'DO_NOTHING', 'FIRST', 'AbDraftExcService', 'autoPendingToRecall', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (221, 3, '承兑汇票-电票承兑自动签收-商票出票人和承兑人相同', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:23:33', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 8-21 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillReceivebizService', 'autoSignAccept', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (222, 3, '承兑汇票-电票自动签收-按策略执行', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-07 08:57:52', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 8-21 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillReceivebizService', 'autoSignStrategy', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765936800000, 1765976400000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (224, 14, '自动支付', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-02-12 16:13:19', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 10,15 * * ?', 'DO_NOTHING', 'FIRST', 'BisPayPolicyService', 'netbank:pay:bispaypolicy:autopay', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (225, 14, '付款指令交易失败', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:00:21', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'BisExcService', 'netbank:bdlink:bisexc:fail', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (226, 3, '承兑汇票-应付票据付款-未用退票指令成功把付款单改为打回', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-09-04 10:27:32', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/2 * * * ?', 'DO_NOTHING', 'FIRST', 'AbEmitBillBackService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765971240000, 1765971360000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (227, 3, '内部拆借还款到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-06 17:29:54', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 * * * * ?', 'DO_NOTHING', 'FIRST', 'InlRepayService', 'cms:inl:repay:notify', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (228, 3, '承兑汇票-余额设置-电票业务完成余额设置', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:15:23', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/5 7-23 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillAccBalService', 'electBusBillAccBal', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (229, 3, '票据池-应收票据付款-票据池背书成功或失败结果返回结算系统', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:14:32', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 8-20 * * ?', 'DO_NOTHING', 'FIRST', 'AbPoolEndorseService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (230, 3, '承兑汇票-电票银承票据到期自动线下兑付确认', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-07 09:01:13', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '16 0/1 * * * ?', 'DO_NOTHING', 'FIRST', 'AbEmitBillPayService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765971256000, 1765971316000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (231, 3, '承兑汇票-电票商票提示付款签收完成自动线下兑付确认', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:12:40', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/5 8-20 * * ?', 'DO_NOTHING', 'FIRST', 'AbEmitBillPayService', 'all', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (232, 3, '承兑汇票-电票到期线上兑付自动确认已付款', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-05 17:06:30', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 8 * * ?', 'DO_NOTHING', 'FIRST', 'AbEmitBillPayService', 'confrimePay', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765890000000, 1765976400000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (233, 3, '承兑汇票-应收票据付款-背书失败结果返回结算系统', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:10:25', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/5 8-20 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillEndorseService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (235, 11, '理财指令交易失败', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:09:41', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'BisFinanceExcService', 'netbank:bdlink:bisfinanceexc:fail', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (236, 14, '待付款池自动转移到付款单', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:09:07', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'NisBillheadPoolService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (237, 3, '承兑汇票-电票拒绝签收后对于挂接的资源释放', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-08 14:40:59', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/5 * * * ?', 'DO_NOTHING', 'FIRST', 'AbBillReceivebizService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765971000000, 1765971300000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (238, 3, '银行借款自动还款', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:17:12', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 */30 * * * ?', 'DO_NOTHING', 'FIRST', 'CmsProvideLoanRepayService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (239, 3, '承兑汇票-消息提醒-电票指令失败消息提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:06:49', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/10 7-23 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillNotificationService', 'bill:accebill:instructionmanage', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (240, 13, '协定存款到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:06:02', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 8 * * ?', 'DO_NOTHING', 'FIRST', 'ChoInnerAgreedDepositService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (241, 4, '自动认领其他付款流水', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-07-25 15:42:20', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'BisAccDtlBudgetRulePayService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (242, 14, '电子回单历史数据迁移', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-05-28 14:56:05', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 * 1/1 * ?', 'DO_NOTHING', 'FIRST', 'EleAotoBisBankReceiptDtlHisService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (243, 3, '承兑汇票-余额设置-质押未解除已到期截止票据账户余额余额设置', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:00:27', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 2 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillAccBalService', 'pledgeBusBillAccBal', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (244, 14, '付款指令交易成功', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:00:12', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'BisExcService', 'netbank:bdlink:bisexc:success', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (246, 3, '直融业务还款到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:57:14', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 * * * * ?', 'DO_NOTHING', 'FIRST', 'DfDirectFinanceRepayService', 'df.direct.finance.repay', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (247, 3, '承兑汇票-消息提醒-托收提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:55:54', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 2 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillNotificationService', 'bill:accebill:collectionapply', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (248, 3, '承兑汇票-消息提醒-票据待签收提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:55:14', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/10 7-23 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillNotificationService', 'bill:accebill:billsign', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (249, 3, '承兑汇票-消息提醒-追索清偿提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:54:29', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/10 7-23 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillNotificationService', 'bill:accebill:searchformanage', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (250, 3, '承兑汇票-消息提醒-自开票据兑付提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:53:06', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 2 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillNotificationService', 'bill:accebill:emitbillpay', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (251, 5, '信用证到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:44:59', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '* 0/3 3 * * ?', 'DO_NOTHING', 'FIRST', 'CreatedCreditService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (252, 13, '定期存款到期处理', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:44:25', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'ChoInnerFinanceService', 'chowmatistic:innerfinance:c2t:expiration', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (253, 3, '承兑汇票-电票自动提示承兑', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-05 16:36:12', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillAcceptApplyService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (254, 4, '付款明细自动勾对', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-07-13 14:00:25', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0,18 * * ?', 'DO_NOTHING', 'FIRST', 'BisPaymentBlendingService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (255, 7, 'ERP付款待办提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:38:37', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'ErpPayInfoService', 'netbank:erp:pay:apply', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (257, 7, 'ERP费用报销待办提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:36:43', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'ErpPayInfoService', 'netbank:erp:expense:apply', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (258, 7, 'ERP代发工资待办提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:35:59', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'ErpPayInfoService', 'netbank:erp:wage:apply', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (259, 4, '自动认领收款流水', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-07-25 15:42:20', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 18 * * ?', 'DO_NOTHING', 'FIRST', 'BisAccDtlBudgetRuleService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (260, 14, '付款支付超时提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:00:09', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'BisExcService', 'netbank:bdlink:bisexc:overtime', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (261, 11, '汇款支付超时提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:32:12', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'BisForeignExcService', 'foreign:foreignexc:bisforeignexc:overtime', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (262, 14, '支付等待结果超时提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:00:06', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'BisExcService', 'netbank:bdlink:bisexc:overtime', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (263, 16, '营收稽核-稽核完成数据迁移', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 17:39:23', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 * * * ?', 'DO_NOTHING', 'FIRST', 'RasDataMoveService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (264, 15, '网银证书到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:25:26', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 1/1 * ?', 'DO_NOTHING', 'FIRST', 'BtBankTypeService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (265, 14, '账户年检到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-05-28 14:50:55', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '* 0/1 * * * ?', 'DO_NOTHING', 'FIRST', 'BtBankAccHandleService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (266, 3, '银行授信-手工占用明细释放', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:24:26', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 6 * * ?', 'DO_NOTHING', 'FIRST', 'RatUrationService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (268, 3, '银行保证金到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:23:27', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 1/1 * ?', 'DO_NOTHING', 'FIRST', 'FcBondDetailService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (269, 3, '内部授信合同到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:22:56', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 1/1 * ?', 'DO_NOTHING', 'FIRST', 'RatCprationService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (270, 4, '银行账户管理-付款明细勾对', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-05-27 12:05:30', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 8 * * ?', 'DO_NOTHING', 'FIRST', 'PaymentBlendingService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (271, 14, '付款可疑指令发送确认提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:00:02', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'BisExcService', 'netbank:bdlink:bisexc:doubtful:overtime', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (272, 11, '汇款可疑指令发送确认提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:20:05', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'BisForeignExcService', 'foreign:foreignexc:bisforeignexc:doubtful:overtime', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (273, 18, '流程转授-自动转授流程审批人', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-07-25 15:42:20', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'DelegationService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (274, 3, '被担保合同到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:17:33', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 1/1 * ?', 'DO_NOTHING', 'FIRST', 'GuaContractChangeService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (275, 15, '中国大陆标准公共日历初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 16:17:04', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 12 31 12 ?', 'DO_NOTHING', 'FIRST', 'SysCalendarService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (276, 14, '收款指令交易失败', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 15:59:50', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'BisGatherCmdService', 'netbank:bdlink:bisgathercmd:fail', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (277, 15, '账户到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 15:54:19', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '*/5 * * * * ?', 'FIRE_ONCE_NOW', 'ROUND', 'BtBankAccService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (279, 3, '承兑汇票-电票自动提示收票', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-07 16:37:27', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 * * * * ?', 'DO_NOTHING', 'FIRST', 'AbBillReceiveApplyService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (280, 17, '银企接口收款自动记账', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-10-29 09:43:02', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/2 * * * ?', 'DO_NOTHING', 'FIRST', 'BtNetChalkAutoTallyService', '5', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1723017120000, 1723017240000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (281, 17, '银企接口收款记账提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-11 15:52:37', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0/1 * * * * ?', 'DO_NOTHING', 'FIRST', 'BtNetChalkService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (282, 14, '银行账户实时余额更新', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-04-10 16:52:45', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1,15 * * ?', 'DO_NOTHING', 'FIRST', 'BtBankAccBalService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765954800000, 1765990800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (283, 3, '内部贷款还款到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 18:06:34', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/1 * * * ?', 'DO_NOTHING', 'FIRST', 'RepaymentScheduleService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (284, 3, '承兑汇票-电票自动托收申请', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 18:02:16', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 3 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillCollectionApplyService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (285, 11, '理财指令交易成功', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 18:00:20', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'BisFinanceExcService', 'netbank:bdlink:bisfinanceexc:success', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (286, 3, '内部授信占用到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 17:59:12', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 1/1 * ?', 'DO_NOTHING', 'FIRST', 'RatCpUrationService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (287, 11, '根据流水自动更新离线账户每日余额', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-07-25 20:12:47', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/15 0/1 * * ?', 'DO_NOTHING', 'FAILOVER', 'BisAccBalAutoService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (288, 13, '通知预约到期处理', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 17:57:31', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'ChoInnerFinanceService', 'chowmatistic:innerfinance:n2b:expiration', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (290, 3, '承兑汇票-消息提醒-票据补录提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 16:02:20', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '1 0 0/10 7-23 * ?', 'DO_NOTHING', 'FIRST', 'AbBillNotificationService', 'bill:accebill:emitbillsupply', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (291, 15, '网银U-key到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-07-13 12:00:08', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '* 0/2 * 1/1 * ?', 'DO_NOTHING', 'FIRST', 'BtNetUKeyService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (292, 11, '汇款指令交易成功', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 10:39:13', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'BisForeignExcService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (293, 3, '委托贷款还款到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 10:38:41', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/1 * * * ?', 'DO_NOTHING', 'FIRST', 'RepaymentScheduleService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (294, 13, '理财到期提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 10:36:32', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'ChoProductReqQueryService', 'chowmatistic:choproductreq:expire', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (295, 17, '核算网络记账提醒', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 10:36:11', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 * * * ?', 'DO_NOTHING', 'FIRST', 'FinacialTallyRemindService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 1690300800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (297, 3, '内部贷款自动还款', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 09:56:19', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '* */5 * * * ?', 'FIRE_ONCE_NOW', 'ROUND', 'CmsLoanAutoRepayService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2018-11-03 22:21:31', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1699581359000, 1699581600000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (316, 3, '银行授信合同到期提醒', TO_DATE('2023-11-10 17:28:55', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2023-11-10 17:28:55', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 1/1 * ?', 'DO_NOTHING', 'FIRST', 'RatBkrationService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2023-11-10 17:28:55', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (317, 14, '付款指令中间状态停留超时预警', TO_DATE('2023-11-10 17:30:21', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-04 17:01:32', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'BisExcService', 'netbank:bdlink:bisexc:consumption:overtime', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2023-11-10 17:30:21', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (321, 14, '扫描消息记录表发送消息', TO_DATE('2024-04-25 14:27:17', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-03-10 11:25:34', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0/10 * * * * ?', 'DO_NOTHING', 'LAST', 'ProducerMessageRecordJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-04-25 14:27:17', 'YYYY-MM-DD HH24:MI:SS'), '', 1, 1774253280000, 1774253290000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (324, 3, '票据到期提醒', TO_DATE('2024-04-28 14:51:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-12 09:11:35', 'YYYY-MM-DD HH24:MI:SS'), 'xxl', '', 'CRON', '0 10 0/1 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillNotificationService', 'bill:accebill:emitbillpaySummary', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-04-28 14:51:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765969800000, 1765973400000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (325, 7, '付款交易结果回传', TO_DATE('2024-04-28 17:38:28', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-09-10 13:23:19', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/2 * * * ?', 'DO_NOTHING', 'FAILOVER', 'DSLPaymentResult', '{"dsl_payment_result_page_size":"100","dsl_payment_result_url":"https://fsc-test.dslyy.com/cbs/api/bfs/callBack/result"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-04-28 17:38:28', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (326, 14, '自动产生资金需求清单', TO_DATE('2024-04-29 10:57:04', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-11-29 10:28:43', 'YYYY-MM-DD HH24:MI:SS'), 'qijh', '', 'CRON', '0 0/5 * * * ?', 'DO_NOTHING', 'ROUND', 'GenerateFundingRequirements', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-04-29 10:57:04', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (327, 3, '承兑账户资金余额不足承兑扣款消息提醒', TO_DATE('2024-04-29 19:04:38', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-12 09:10:11', 'YYYY-MM-DD HH24:MI:SS'), 'xxl', '', 'CRON', '30 0/15 * * * ?', 'DO_NOTHING', 'FAILOVER', 'AbBillNotificationService', 'bill:accebill:emitbillpaybalance', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-04-29 19:04:38', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765971030000, 1765971930000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (329, 14, '自动根据付款类需求单生成下拨指令', TO_DATE('2024-05-08 14:56:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-11-29 10:28:47', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/15 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAllocationNisDemandForNisJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-05-08 14:56:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (333, 3, '内部借款到期提醒', TO_DATE('2024-05-09 15:33:55', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-05-09 15:33:55', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'CmsLoanSendService', 'innerLoanExpirationWarn', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-05-09 15:33:55', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (334, 17, '银行账户利息流水自动认领', TO_DATE('2024-05-23 17:51:41', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-05-23 17:51:41', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 22 1/1 * ?', 'DO_NOTHING', 'FIRST', 'bankAccInterestDtlAutoClaimJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-05-23 17:51:41', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (335, 14, '下拨资金付款失败提醒', TO_DATE('2024-05-25 14:41:30', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-11 15:59:29', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '* 0/1 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoNIsDemandBisExcFail', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-05-25 14:41:30', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (336, 14, '付款成功提醒', TO_DATE('2024-05-25 14:42:15', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-29 11:14:18', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/2 * * * ?', 'DO_NOTHING', 'FAILOVER', 'AutoNIsDemandBisExcSuccess', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-05-25 14:42:15', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (337, 14, '资金需求未产生下拨指令消息提醒', TO_DATE('2024-05-28 11:22:35', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-08-20 21:42:20', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '* * 0 * * ?', 'DO_NOTHING', 'FIRST', 'AutoNisDemandNotBisExcJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-05-28 11:22:35', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (338, 14, '自动根据承兑到期类需求单生成下拨指令', TO_DATE('2024-05-28 11:23:13', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-08-22 15:04:35', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/15 * * * ?', 'DO_NOTHING', 'FIRST', 'AutoAllocationNisDemandForBillJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-05-28 11:23:13', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765971000000, 1765971900000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (339, 14, '自动根据融资到期类需求单生成下拨指令', TO_DATE('2024-05-28 11:23:59', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-08-22 15:04:38', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/15 * * * ?', 'DO_NOTHING', 'FIRST', 'AutoAllocationNisDemandForCmsJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-05-28 11:23:59', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765971000000, 1765971900000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (340, 14, '自动汇总需求单', TO_DATE('2024-05-28 11:57:27', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-11-29 12:05:47', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/5 * * * ?', 'DO_NOTHING', 'FIRST', 'AutoSummaryNisDemandJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-05-28 11:57:27', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (341, 4, '付款与流水自动勾对', TO_DATE('2024-06-03 13:58:43', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-11-28 14:33:55', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/1 * * * ? *', 'DO_NOTHING', 'ROUND', 'BisPaymentBlendingService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-06-03 13:58:43', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (343, 14, '资金调拨流水自动认领', TO_DATE('2024-06-13 10:15:08', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-13 10:15:08', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1,15 * * ?', 'DO_NOTHING', 'FIRST', 'fundTansferFlowAutoClaimJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-06-13 10:15:08', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (344, 17, '计息到期提醒', TO_DATE('2024-06-19 19:27:33', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-19 19:27:33', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 10,15 * * ?', 'DO_NOTHING', 'FIRST', 'FcInterestService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-06-19 19:27:33', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (345, 11, '银行账户余额流水异常核查', TO_DATE('2024-06-20 14:34:41', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-20 14:34:41', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 8 * * ?', 'DO_NOTHING', 'FIRST', 'BisAccDtlCheckService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-06-20 14:34:41', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (346, 3, '内部贷款到期展期提醒', TO_DATE('2024-06-20 15:01:09', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-20 15:01:09', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'CmsLoanSendService', 'loanDeferredWarn', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-06-20 15:01:09', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (350, 15, '历史消息清理', TO_DATE('2024-06-28 08:54:30', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-06-28 08:54:30', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0 1 * ?', 'DO_NOTHING', 'FIRST', 'NotificationService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-06-28 08:54:30', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (357, 14, '自动发送付款执行', TO_DATE('2024-07-02 14:35:09', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-07-02 14:35:09', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1,15 * * ?', 'DO_NOTHING', 'FIRST', 'AutoSendPaymentJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-07-02 14:35:09', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (361, 3, '承兑汇票-已到期持有票据正面信息同步已结清自动托收申请', TO_DATE('2024-08-15 13:43:48', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-15 13:44:18', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9,12,20 * * ? *', 'DO_NOTHING', 'FIRST', 'AbDraftExcService', 'autoQueryBillInfoToPayByHold', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-08-15 13:43:48', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (362, 3, '授信占用电票业务单号同步定时任务', TO_DATE('2024-08-20 16:30:15', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-22 11:22:05', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 * * * ?', 'DO_NOTHING', 'FIRST', 'RatBillNoSupplyTaskService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-08-15 13:43:48', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765969200000, 1765972800000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (363, 4, '退款勾对自动预匹配', TO_DATE('2024-08-23 10:07:07', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-11-14 15:42:14', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/1 * * * ? *', 'DO_NOTHING', 'ROUND', 'RefundBlendingTaskService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-08-23 10:07:07', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (364, 11, '异常告警-余额异常告警定时任务', TO_DATE('2024-08-22 16:30:15', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-29 14:19:40', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 * * * ?', 'DO_NOTHING', 'FAILOVER', 'BisAccBalWarnTaskService', '20', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-08-15 13:43:48', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (365, 3, '保证金账户过期销户', TO_DATE('2024-09-10 15:54:10', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-09-13 11:20:52', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'FcBondDelHandleServiceJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-09-10 15:54:10', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765947600000, 1765987200000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (366, 15, '通用sql调用', TO_DATE('2024-09-13 14:49:51', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-09-13 14:49:51', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'AutomaticExecutionSqlJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-09-13 14:49:51', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (367, 14, '付款失败企微消息提醒', TO_DATE('2024-09-21 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-09-21 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'AutoPaymentBisExcFail', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-09-21 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (368, 14, '需求清单自动产生头寸调拨指令（作废）', TO_DATE('2024-11-05 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-08-21 10:39:34', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'AutoNisDemandForPosJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-11-05 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (371, 17, '总账户其他付款流水自动记账', TO_DATE('2024-11-11 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-11-11 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'BtNetOtherPayDtlAutoTallyService', '10', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-11-11 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (372, 17, '总账户资金下拨流水自动记账', TO_DATE('2024-11-11 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-11-11 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'BtNetDtlDownAutoTallyService', '10', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-11-11 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (373, 17, '总账户其他收款流水自动记账', TO_DATE('2024-11-11 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-11-11 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'BtNetOtherReceiveDtlAutoTallyService', '10', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-11-11 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (374, 14, '请款审批企微消息提醒', TO_DATE('2024-09-20 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-09-20 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'NisallocationReqApproveJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-09-20 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (380, 3, '释放保证金自动转回', TO_DATE('2024-12-27 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-12-27 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'AutoFcBondRotationJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-12-27 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (381, 7, '共享流水同步接口', TO_DATE('2024-12-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-12-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'DSLGXBisAccDtl', '{"dsl_gx_bis_acc_dtl_url": "","dsl_gx_bis_acc_dtl_page_size": 300}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-12-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (382, 7, '授信合同同步定时任务', TO_DATE('2025-01-07 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-07 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'DSLRatBkration', '{"dsl_rat_bkration_url": "","validDate": "2025-01-07"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-01-07 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (384, 7, '理财合同同步定时任务', TO_DATE('2025-01-08 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-08 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'ROUND', 'DSLChoProductReq', '{"dsl_cho_product_req_url": "","validDate": "2025-01-08"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-01-08 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (385, 7, '共享系统票据同步接口', TO_DATE('2025-01-10 17:24:08', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-01-10 17:24:50', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'DSLAbBillShareSync', '{
    "dsl_ab_bill_share_sync_url": "ddd",
    "dsl_ab_bill_share_sync_page_size": 5
}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-01-10 17:24:08', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (386, 3, '票据到期自动解付', TO_DATE('2025-02-07 17:24:08', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-02-07 17:24:50', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'AbEmitBillPayService', 'claimPay', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-02-07 17:24:08', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (387, 14, '银行账户同步冻结金额定时任务', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalFreezeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (388, 14, '银行账户资金自动解冻定时任务', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalUnfreezeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (389, 14, '冻结申请全部冻结成功提醒', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalAllFreezeNoticeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (390, 14, '冻结申请部分冻结成功提醒', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalPartFreezeNoticeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (391, 7, '保证金利息同步接口', TO_DATE('2025-03-11 16:51:11', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-03-11 16:51:11', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 9 * * ?', 'DO_NOTHING', 'FIRST', 'DSLAbEmitBailInterestSync', '{
    "dsl_ab_emit_bail_interest_sync_url": " ",
    "dsl_ab_emit_bail_interest_sync_page_size": 30}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-03-11 16:51:11', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (392, 11, '直联账户系统冻结金额同步', TO_DATE('2025-03-26 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-03-26 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 6 * * ?', 'DO_NOTHING', 'ROUND', 'AutoSyncSysFrzBalJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-03-26 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (393, 14, '头寸调拨审批企微消息提醒', NULL, NULL, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'NisallocationPosApproveJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-04-14 10:37:15', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (394, 14, '保证金账户自动销户', TO_DATE('2024-08-20 16:30:15', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-20 16:30:15', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'autoAccCancellTaskService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-08-15 13:43:48', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (395, 3, '承兑到期和借款汇总余额不足提醒', TO_DATE('2024-08-20 16:30:15', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-08-20 16:30:15', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'AbBillAccInsufficientFundsTaskService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2024-08-15 13:43:48', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (398, 14, '需求单下拨/头寸指令失败提醒', NULL, TO_DATE('2025-08-22 15:03:49', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0/5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAllocationNisDemandResultJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-08-19 10:46:51', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 1765971000000, 1765971300000, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (399, 14, '冻结申请无冻结金额提醒', TO_DATE('2025-10-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-10-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 */5 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoAccBalNoFreezeNoticeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-10-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (400, 14, '自动新增票据计划冻结申请', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 2 ? * THU', 'DO_NOTHING', 'ROUND', 'AutoBillBalFreezeJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-02-12 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (401, 14, '代发工资配置表自动生成需求清单', NULL, NULL, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryConfigGenerateDemandJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-11-26 14:35:27', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (402, 14, '代发工资单据自动生成需求清单', NULL, NULL, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryTableGenerateDemandJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-11-26 14:35:27', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (403, 14, '代发工资配置表自动汇总需求清单', NULL, NULL, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryConfigSummaryDemandJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-11-26 14:35:27', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (404, 14, '代发工资单据自动汇总需求清单', NULL, NULL, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryTableSummaryDemandJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-11-26 14:35:27', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (405, 14, '代发下拨金额自动同步', TO_DATE('2025-11-20 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2025-11-20 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 2 1 * ?', 'DO_NOTHING', 'ROUND', 'AutoSyncWageAllocationJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-11-20 14:06:46', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (406, 14, '代发工资配置表自动生成下拨指令', NULL, NULL, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryConfigAllocationFundsJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-11-26 14:35:27', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (407, 14, '代发工资单据自动生成下拨指令', NULL, NULL, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'SalaryTableAllocationFundsJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2025-11-26 14:35:27', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, '1000' );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (421, 19, '查询执行计划表，并清理付款办理的脏数据', TO_DATE('2026-03-05 15:06:02', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-03-05 15:06:15', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 0 6 * * ?', 'DO_NOTHING', 'RANDOM', 'fbsBudgetProcessService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2026-03-05 15:06:02', 'YYYY-MM-DD HH24:MI:SS'), '', 1, 1774216800000, 1774303200000, NULL );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (425, 17, '生产者消息记录表 定时扫描发送消息', TO_DATE('2026-03-09 14:03:29', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-03-16 10:47:05', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0/10 * * * * ?', 'DO_NOTHING', 'FIRST', 'ProducerMessageRecordJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2026-03-09 14:03:29', 'YYYY-MM-DD HH24:MI:SS'), '', 1, 1774253280000, 1774253290000, NULL );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (427, 3, '票据事务补偿任务', TO_DATE('2026-03-12 14:59:10', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-03-12 15:07:05', 'YYYY-MM-DD HH24:MI:SS'), 'XXL', '', 'CRON', '0 * * * * ?', 'DO_NOTHING', 'FIRST', 'BillAffairNisRecordJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2026-03-12 14:59:10', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, NULL );

INSERT INTO XXL_JOB_INFO (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time, TENANT_ID) VALUES (428, 20, '综合授信额度查询历史报表数据保存', NULL, NULL, 'XXL', '', 'CRON', '0 30 0 * * ?', 'DO_NOTHING', 'ROUND', 'RatBkStatisticsReportHisService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', TO_CLOB(''), 'GLUE代码初始化', TO_DATE('2026-03-18 15:38:07', 'YYYY-MM-DD HH24:MI:SS'), '', 0, 0, 0, NULL );

--
-- Table structure for table 'XXL_JOB_USER'
--
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE XXL_JOB_USER';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE XXL_JOB_USER (
    id NUMBER(10) NOT NULL PRIMARY KEY,
    username VARCHAR2(50) NOT NULL,
    password VARCHAR2(50) NOT NULL,
    role NUMBER NOT NULL,
    permission VARCHAR2(255) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON COLUMN XXL_JOB_USER.username IS '账号';
COMMENT ON COLUMN XXL_JOB_USER.password IS '密码';
COMMENT ON COLUMN XXL_JOB_USER.role IS '角色：0-普通用户、1-管理员';
COMMENT ON COLUMN XXL_JOB_USER.permission IS '权限：执行器ID列表，多个逗号分割';
COMMENT ON COLUMN XXL_JOB_USER.TENANT_ID IS '租户ID';

CREATE UNIQUE INDEX i_username ON XXL_JOB_USER (username);
CREATE INDEX IDX_XXL_JOB_USER_TENANT_ID ON XXL_JOB_USER (TENANT_ID);

-- Dump completed for table 'XXL_JOB_USER'


INSERT INTO XXL_JOB_USER VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL, '1000' );

--
-- Table structure for table 'XXL_JOB_LOCK'
--
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE XXL_JOB_LOCK';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE XXL_JOB_LOCK (
    lock_name VARCHAR2(50) NOT NULL PRIMARY KEY,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE XXL_JOB_LOCK IS '任务锁表';
COMMENT ON COLUMN XXL_JOB_LOCK.lock_name IS '锁名称';
COMMENT ON COLUMN XXL_JOB_LOCK.TENANT_ID IS '租户ID';


-- Dump completed for table 'XXL_JOB_LOCK'


INSERT INTO XXL_JOB_LOCK (lock_name) VALUES (('schedule_lock') );

--
-- Table structure for table 'CR_DATA_SOURCE'
--
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE CR_DATA_SOURCE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE CR_DATA_SOURCE (
    ID VARCHAR2(32) NOT NULL,
    DATA_SOURCE_CODE VARCHAR2(32) NULL,
    DATA_SOURCE_NAME VARCHAR2(100) NULL,
    DATA_SOURCE_ORIGIN VARCHAR2(1000) NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE CR_DATA_SOURCE IS '数据来源表';
COMMENT ON COLUMN CR_DATA_SOURCE.ID IS 'id';
COMMENT ON COLUMN CR_DATA_SOURCE.DATA_SOURCE_CODE IS '数据来源的代码';
COMMENT ON COLUMN CR_DATA_SOURCE.DATA_SOURCE_NAME IS '数据来源的名称';
COMMENT ON COLUMN CR_DATA_SOURCE.DATA_SOURCE_ORIGIN IS '数据来源的来源';
COMMENT ON COLUMN CR_DATA_SOURCE.TENANT_ID IS '租户id';


-- Dump completed for table 'CR_DATA_SOURCE'


INSERT INTO CR_DATA_SOURCE (ID, DATA_SOURCE_CODE, DATA_SOURCE_NAME, DATA_SOURCE_ORIGIN, TENANT_ID) VALUES ('0a22c2ad16ea11f1afff005056b9095f', '07', '贷款利率（人民币）', '在《银行借款查询》表中，获取申请单位=评价组织，借款银行=评价机构，币种=人民币，非暂存状态，且排除发放到期日期<评价开始日期、发放开始日期>评价结束日期的发放数据，计算利率平均数，若评价机构为总行则计算总行项下所有支行借款利率的取平均数；', '000' );

INSERT INTO CR_DATA_SOURCE (ID, DATA_SOURCE_CODE, DATA_SOURCE_NAME, DATA_SOURCE_ORIGIN, TENANT_ID) VALUES ('0a234f3216ea11f1afff005056b9095f', '01', '授信敞口额度', '在统计报表《授信额度综合统计》中，获取评价到期日=报表日期，评价组织=授信单位，银行信息=评价机构，币种=人民币，状态非关闭、非已到期、非关闭审批中、非全部结转数据的授信额度之和，若评价机构为总行则计算总行项下所有支行最新的授信额度人民币金额之和。', '000' );

INSERT INTO CR_DATA_SOURCE (ID, DATA_SOURCE_CODE, DATA_SOURCE_NAME, DATA_SOURCE_ORIGIN, TENANT_ID) VALUES ('0a23caa416ea11f1afff005056b9095f', '02', '授信的增减变化', '在统计报表《授信额度综合统计》表中，获取授信单位=评价组织，授信银行=评价机构，币种=人民币，状态非关闭、非已到期、非关闭审批中、非全部结转数据的授信数据，计算在报表日期=评价开始日期、报表日期=评价结束日期授信额度的差值，若评价机构为总行则计算总行项下所有支行在评价开始日期、评价结束日期授信额度折合人民币金额的差值之和。', '000' );

INSERT INTO CR_DATA_SOURCE (ID, DATA_SOURCE_CODE, DATA_SOURCE_NAME, DATA_SOURCE_ORIGIN, TENANT_ID) VALUES ('0a242b9516ea11f1afff005056b9095f', '03', '融资成本(美元)', '在《融资成本测算表》中，获取评价组织=申请单位，评价机构=借款单位，币别为美元，发放开始日期在评价区间发放数据的融资成本的平均值，若评价机构为总行则计算总行项下所有支行的融资成本的平均值；', '000' );

INSERT INTO CR_DATA_SOURCE (ID, DATA_SOURCE_CODE, DATA_SOURCE_NAME, DATA_SOURCE_ORIGIN, TENANT_ID) VALUES ('0a2488c816ea11f1afff005056b9095f', '04', '融资成本(人民币)', '在《融资成本测算表》中，获取评价组织=申请单位，评价机构=借款单位，币别为人民币，发放开始日期在评价区间发放数据的融资成本平均值，若评价机构为总行则计算总行项下所有支行的融资成本的平均值；', '000' );

INSERT INTO CR_DATA_SOURCE (ID, DATA_SOURCE_CODE, DATA_SOURCE_NAME, DATA_SOURCE_ORIGIN, TENANT_ID) VALUES ('0a250c9116ea11f1afff005056b9095f', '05', '贷款余额', '在《银行借款查询表》中，获取单位=评价组织，借款银行=评价机构，币种=人民币，报表日期=评价结束日期的借款余额之和，若评价机构为总行则计算总行项下所有支行贷款余额之和；', '000' );

INSERT INTO CR_DATA_SOURCE (ID, DATA_SOURCE_CODE, DATA_SOURCE_NAME, DATA_SOURCE_ORIGIN, TENANT_ID) VALUES ('0a2580d016ea11f1afff005056b9095f', '06', '新增贷款', '在《银行借款查询》中，获取发放日期在评价区间、申请单位=评价组织，币种=人民币，借款银行=评价机构数据的已发放金额之和，若评价机构为总行则计算总行项下所有支行已发放金额折合人民币金额之和；', '000' );

INSERT INTO CR_DATA_SOURCE (ID, DATA_SOURCE_CODE, DATA_SOURCE_NAME, DATA_SOURCE_ORIGIN, TENANT_ID) VALUES ('273449a816ea11f1afff005056b9095f', '08', '承兑、信用证业务保证金比例', '     1、承兑汇票：在《承兑汇票业务表》中，获取出票人=评价组织，币种=人民币，承兑人=评价机构，开票日期在评价区间之间，业务类型=出票登记且业务状态=业务完成的所有票据的保证金比例平均值；
    2、信用证：在《开出信用证查询》表中，获取开证人=评价组织，币别=人民币，开证行=评价机构，开证日期在评价日期期间，非暂存状态的信用证保证金比例平均值；
     3、计算该评价组织合作的评价机构的承兑汇票、信用证的保证金比例平均值，若评价机构为总行则计算总行项下所有支行保证金比例平均值；', '000' );

INSERT INTO CR_DATA_SOURCE (ID, DATA_SOURCE_CODE, DATA_SOURCE_NAME, DATA_SOURCE_ORIGIN, TENANT_ID) VALUES ('2734c4ac16ea11f1afff005056b9095f', '09', '存款利率（人民币）', '取单位=评价组织，开户行名称=评价机构，币别=人民币，排除理财/存款的到期日<评价开始日期、理财/存款的开始日期>评价结束日期的数据在下列表中利率平均数。
1、存款类业务查询：取“利率”，若为协定存款且为阶梯利率则取协定利率平均值）
2、理财类业务查询：
    2.1保本类型：取“平均收益率”
    2.2非保本类型：取“持有收益率”（若持有收益率为空，则该笔理财不统计）
若评价机构为总行则计算总行项下所有支行存款利率平均值；', '000' );

--
-- Table structure for table 'DATA_SOURCE_CATEGORY'
--
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE DATA_SOURCE_CATEGORY';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE DATA_SOURCE_CATEGORY (
    ID VARCHAR2(32) NOT NULL,
    DATA_SOURCE_CATEGORY_NAME VARCHAR2(100) NULL,
    DATA_SOURCE_CATEGORY_CODE VARCHAR2(4) NULL,
    CREATOR VARCHAR2(100) NOT NULL,
    CREATE_DATE DATE NOT NULL,
    TENANT_ID VARCHAR2(32) NULL
);

COMMENT ON TABLE DATA_SOURCE_CATEGORY IS '数据来源类别';
COMMENT ON COLUMN DATA_SOURCE_CATEGORY.ID IS '主键';
COMMENT ON COLUMN DATA_SOURCE_CATEGORY.DATA_SOURCE_CATEGORY_NAME IS '数据来源类别名称';
COMMENT ON COLUMN DATA_SOURCE_CATEGORY.DATA_SOURCE_CATEGORY_CODE IS '数据来源类别代码:D001-供应商;D002-子企业;D003-机构';
COMMENT ON COLUMN DATA_SOURCE_CATEGORY.CREATOR IS '创建人';
COMMENT ON COLUMN DATA_SOURCE_CATEGORY.CREATE_DATE IS '创建日期时间:yyyy-MM-dd hh24:mm:ss';
COMMENT ON COLUMN DATA_SOURCE_CATEGORY.TENANT_ID IS '租户id';


-- Dump completed for table 'DATA_SOURCE_CATEGORY'


INSERT INTO DATA_SOURCE_CATEGORY (ID, DATA_SOURCE_CATEGORY_NAME, DATA_SOURCE_CATEGORY_CODE, CREATOR, CREATE_DATE, TENANT_ID) VALUES ('60f92636699f11efa72f005056a8c64c', '机构', 'D003', 'admin', TO_DATE('2024-09-24', 'YYYY-MM-DD'), '000' );

INSERT INTO DATA_SOURCE_CATEGORY (ID, DATA_SOURCE_CATEGORY_NAME, DATA_SOURCE_CATEGORY_CODE, CREATOR, CREATE_DATE, TENANT_ID) VALUES ('696f127c699f11efa72f005056a8c64c', '供应商', 'D001', 'admin', TO_DATE('2024-09-24', 'YYYY-MM-DD'), '000' );

INSERT INTO DATA_SOURCE_CATEGORY (ID, DATA_SOURCE_CATEGORY_NAME, DATA_SOURCE_CATEGORY_CODE, CREATOR, CREATE_DATE, TENANT_ID) VALUES ('7e35d725699f11efa72f005056a8c64c', '子企业', 'D002', 'admin', TO_DATE('2024-09-24', 'YYYY-MM-DD'), '000' );

-- XXL-JOB 序列生成器
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_GROUP_ID START WITH 30 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_INFO_ID START WITH 500 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_LOG_ID START WITH 1 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_LOGGLUE_ID START WITH 1 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_LOG_REPORT_ID START WITH 1 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_LOGIN_LOG_ID START WITH 1 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_REGISTRY_ID START WITH 1 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_USER_ID START WITH 10 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_OPERATE_LOG_ID START WITH 1 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE XXL_JOB_PERMISSION_ID START WITH 10 INCREMENT BY 1';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN NULL; ELSE RAISE; END IF;
END;
/

