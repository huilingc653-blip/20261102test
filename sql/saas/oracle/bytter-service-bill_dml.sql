DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '44a64316402d47219a02ff738c479549';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
    VALUES ('44a64316402d47219a02ff738c479549', '6120', '兑付确认', NULL, '5158a781a4074d048e2dc738a1b498b3', 14, NULL, NULL);
  END IF;
  COMMIT;
END;
/


DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '45a64316402d47219a02ff738c479549';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
    VALUES ('45a64316402d47219a02ff738c479549', '6101', '开票补录', NULL, '5158a781a4074d048e2dc738a1b498b3', 14, NULL, NULL);
  END IF;
  COMMIT;
END;
/

DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = '46a64316402d47219a02ff738c479549';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
    VALUES ('46a64316402d47219a02ff738c479549', '9004', '票据删除', NULL, '5158a781a4074d048e2dc738a1b498b3', 14, NULL, NULL);
  END IF;
  COMMIT;
END;
/


declare
	v_count number;
begin
select count(*) into v_count from sys_multi_lang where id = 'vv47e6de84c64b6384c2bf047cf38904';
if v_count < 1 then
    insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
    values ('vv47e6de84c64b6384c2bf047cf38904', 'zh_CN', '是否同意', 'table.endorseResponse.isAgree', '', '', null, null, null, null);
commit;
end if;
end;
/

DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'mg9iu1f5e1ce442fa9052ed590f58df0';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
    VALUES ('mg9iu1f5e1ce442fa9052ed590f58df0', '9019', '托收', NULL, '42iu50cb3d2e421c9e4a7343fe5f38bc', 14, NULL, NULL);
  END IF;
  COMMIT;
END;
/

DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'ma9iu1f5e1ce442fa9052ed590f58df0';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
    VALUES ('ma9iu1f5e1ce442fa9052ed590f58df0', '6019', '托收', NULL, '42iu50cb3d2e421c9e4a7343fe5f38bc', 14, NULL, NULL);
  END IF;
  COMMIT;
END;
/


DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_dict WHERE ID = 'md9iu1f5e1ce442fa9052ed590f58df0';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_dict (ID, TYPE_CODE, TYPE_NAME, TYPE_ID, TYPE_GROUP_ID, SORT, CREATE_DATE, CREATE_NAME)
    VALUES ('md9iu1f5e1ce442fa9052ed590f58df0', '018', '托收', NULL, '42iu50cb3d2e421c9e4a7343fe5f38bc', 14, NULL, NULL);
  END IF;
  COMMIT;
END;
/



DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY='nis.payment.allocation.status' AND LANG_CODE='zh_CN';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('abda8b9c0d1e2f3a4b5c6d7e8f9056rt', 'zh_CN', '下拨状态', 'nis.payment.allocation.status');
  END IF;
  COMMIT;
END;
/


DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY='nis.payment.allocation.status' AND LANG_CODE='en_US';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('abd28b9c0d1e2f3a4b5c6d7e8f9056rt', 'en_US', 'Allocation Status', 'nis.payment.allocation.status');
  END IF;
  COMMIT;
END;
/



DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY='nis.payment.downAllocation.requireListCode' AND LANG_CODE='zh_CN';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('asba8b9c0d1e2f3a4b5c6d7e8f9056rt', 'zh_CN', '下拨需求清单编码', 'nis.payment.downAllocation.requireListCode');
  END IF;
  COMMIT;
END;
/


DECLARE
  v_COUNT NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY='nis.payment.downAllocation.requireListCode' AND LANG_CODE='en_US';
  IF v_COUNT = 0 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('asb28b9c0d1e2f3a4b5c6d7e8f9056rt', 'en_US', 'Allocation Status', 'nis.payment.downAllocation.requireListCode');
  END IF;
  COMMIT;
END;
/

-- 7. nis.contract.multiConTypeSelect zh_CN
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY='nis.contract.multiConTypeSelect' AND LANG_CODE='zh_CN';
IF v_COUNT = 0 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('asb781c20d1e2f3a4b5c6d7e8f901118', 'zh_CN', '调用合同请选择对应合同', 'nis.contract.multiConTypeSelect');
END IF;
COMMIT;
END;
/

-- 8. nis.contract.multiConTypeSelect en_US
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang WHERE LANG_KEY='nis.contract.multiConTypeSelect' AND LANG_CODE='en_US';
IF v_COUNT = 0 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('asb781c20d1e2f3a4b5c6d7e8f901117', 'en_US', 'Please select the corresponding contract when calling the contract', 'nis.contract.multiConTypeSelect');
END IF;
COMMIT;
END;
/