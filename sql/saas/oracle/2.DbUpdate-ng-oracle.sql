declare
v_count number;
begin
select count(*) into v_count from sys_multi_lang where id = '1745e9d05b0b462586cd8de70c882cba';
if v_count < 1 then
    insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
    values ('1745e9d05b0b462586cd8de70c882cba', 'zh_CN', '该流程无任何设计！', 'bpm.flow.not.designed', '', '', null, null, null, null);
commit;
end if;
end;
/

-- 1. sys_tenant_manager
INSERT INTO sys_tenant_manager (
    id,TENANT_CODE,AM_USER_ID, TENANT_NAME,
    TENANT_CERT_TYPE, TENANT_CERT_CODE, TENANT_CERT_INDATE,
    TENANT_PHONE, LEGAL_PERSON_NAME, LEGAL_PERSON_MOBILE, LEGAL_PERSON_CERT_TYPE, LEGAL_PERSON_CERT_CODE,
    LEGAL_PERSON_CERT_INDATE, SIGN_CHANNELS, IS_SAME_SUB_BANK,corp_id,
    TENANT_STATUS, SIGN_DATE, ENABLE_DATE, STATUS, TENANT_ID, CHARGE_MODE, CREATE_TIME, corp_number, user_number, account_number
)
select
    '1000',
    '1000',
    '402880425b6b0a58015b6b0abeb10000',
    '单租户模式默认租户',
    '1',
    '88888888',
    to_date('9999-12-31','yyyy-mm-dd'),
    '18888888888',
    '单租户模式默认租户',
    '18888888888',
    '03',
    '88888888',
    to_date('9999-12-31','yyyy-mm-dd'),
    '01',
    '1',
    '1000',
    2,
    to_date('2026-04-09 00:00:00','yyyy-mm-dd hh24:mi:ss'),
    to_date('2026-04-14 15:24:15','yyyy-mm-dd hh24:mi:ss'),
    95,
    '1000',
    '0',
    to_date('2026-04-09 16:11:19','yyyy-mm-dd hh24:mi:ss'),
    10000,
    10000,
    10000
from dual
where not exists(select 1 from sys_tenant_manager where id = '1000');

INSERT INTO sys_tenant_user_record_manager (
    ID, TENANT_RECORD_ID, TENANT_ID, IS_ADMIN, LOGIN_NAME,USERNAME, CARD_TYPE, CARD_CODE, PHONE, EMAIL, STATUS
)
select
    'f114ff13628743e189d59535184d4bed',
    '7d6b8d0ccb3d48bfbb7fa454ead300a8',
    '1000',
    1,
    'admin',
    '超级管理员',
    '03',
    '123456',
    null,
    null,
    1
from dual
where not exists(select 1 from sys_tenant_user_record_manager where id = 'f114ff13628743e189d59535184d4bed');

INSERT INTO sys_corp (
    ID, ACCOUNT_LICENCE, ADDRESS, CITY, CODE, COLLECT_FLAG, CTL_BUDGET, HEAD_PERSON, LISTED_COMPANY, NAME, PARENT_ID,
    PARENT_IDS, PROVINCE, SORT, STATUS, "TYPE", USE_ACCOUNT_CODE, NET_ID, RAT_GROUP, CREATE_TIME, CREATE_BY, UPDATE_TIME,
    UPDATE_BY, IS_LIMIT_QUOTA, DAY_LIMIT_QUOTA, MONTH_LIMIT_QUOTA, NAME_EN, ADDRESS_EN, CUR_ID, UNIT_ATTRIBUTE, UNIT_CM,
    COUNTRY_ID, CERT_TYPE, CERT_NO, SOC_CODE, operationArea_ID, equityMaintenance_ID, unitStoreMaintenance_ID, PROVINCE_ID,
    REPORT_DISPLAY, TENANT_ID
)
select
    '1000',
    NULL,
    NULL,
    NULL,
    '1000',
    NULL,
    NULL,
    '22',
    1,
    '单租户模式默认网点',
    NULL,
    '1000',
    NULL,
    1,
    1,
    0,
    NULL,
    '0011',
    NULL,
    to_date('2026-03-19 13:43:48','yyyy-mm-dd hh24:mi:ss'),
    'system',
    NULL,
    NULL,
    0.00,
    NULL,
    NULL,
    NULL,
    NULL,
    '4028802e5c346968015c346c18850008',
    '01',
    NULL,
    NULL,
    '01',
    '2134253213421',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    '1',
    '1000'
from dual
where not exists (select 1 from sys_corp where id = '1000');



INSERT INTO sys_user (ID, BEGIN_USE_TIME, CODE, EMAIL, IS_ADMIN, LOGIN_NAME, PASSWORD, PHONE, SALT, SEX, STATUS,
                      USERNAME, CORP_ID, USER_CADN, LOGIN_TYPE, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY,
                      SHOW_REPORT, SYS_SKIN, EMPLOYEE_ID, TENANT_ID, CARD_TYPE, CARD_CODE)
select 'e355f972e7d34d53acee173298d6aefa',
       null,
       null,
       '',
       0,
       '1000',
       '1a5d8dbd74dda7e21cc69e5dc1926231',
       '',
       'bd31bdd091b9b00e02d48fb218fe4c95f4fa2b96365a40c1b578316c7c04604a',
       1,
       1,
       '管理员',
       '1000',
       null,
       null,
       to_date('2026-04-09 17:09:06', 'yyyy-mm-dd hh24:mi:ss'),
       'e573f2bc537c4025bb4b4c308baf2882',
       to_date('2026-04-09 18:03:28', 'yyyy-mm-dd hh24:mi:ss'),
       'e573f2bc537c4025bb4c308baf2882',
       0,
       null,
       null,
       '1000',
       null,
       null
from dual
where not exists(select 1 from sys_user where id = 'e355f972e7d34d53acee173298d6aefa');

commit;



















