DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'basic.business.message.btswiftinfo.swiftcode.max' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('nm567374f31848eed755a7c537c9af02', 'zh_CN', 'SWIFTCODE导入长度不允许超过30', 'basic.business.message.btswiftinfo.swiftcode.max');
END IF;
commit;
END;
/

/
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'table.btswiftinfo.bankType.notExist' AND LANG_CODE = 'en_US';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('ab567374f31848eed755a7c537c9af02', 'en_US', 'The length of imported SWIFTCODE cannot exceed 30.', 'basic.business.message.btswiftinfo.swiftcode.max');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.userMessage.alert.conentLength' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('wc567374f31848eed755a7c537c9af02', 'zh_CN', '消息内容应控制在1500个字符之间', 'sys.userMessage.alert.conentLength');
END IF;
commit;
END;
/

/
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.userMessage.alert.conentLength' AND LANG_CODE = 'en_US';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('wb567374f31848eed755a7c537c9af02', 'en_US', 'Message content must be limited to 1500 characters.', 'sys.userMessage.alert.conentLength');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.form.verify.nameError' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('fw567374f31848eed755a7c537c9af02', 'zh_CN', '姓名不能为空', 'sys.form.verify.nameError');
END IF;
commit;
END;
/

/
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.form.verify.nameError' AND LANG_CODE = 'en_US';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('wf567374f31848eed755a7c537c9af02', 'en_US', 'Name cannot be empty.', 'sys.form.verify.nameError');
END IF;
commit;
END;
/



DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'bt.fcExcrateAdust.message.preTime.null' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('ll567374f31848eed755a7c537c9af02', 'zh_CN', '执行日期不能为空', 'bt.fcExcrateAdust.message.preTime.null');
END IF;
commit;
END;
/

/
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'bt.fcExcrateAdust.message.preTime.null' AND LANG_CODE = 'en_US';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('wl567374f31848eed755a7c537c9af02', 'en_US', 'Execution date cannot be empty.', 'bt.fcExcrateAdust.message.preTime.null');
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'bt.fcExcrateAdust.message.newExchangeRate.null' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('nn567374f31848eed755a7c537c9af02', 'zh_CN', '即期汇率不能为空', 'bt.fcExcrateAdust.message.newExchangeRate.null');
END IF;
commit;
END;
/

/
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'bt.fcExcrateAdust.message.newExchangeRate.null' AND LANG_CODE = 'en_US';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('nl567374f31848eed755a7c537c9af02', 'en_US', 'Spot exchange rate is required.', 'bt.fcExcrateAdust.message.newExchangeRate.null');
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'bt.fcExcrateAdust.message.referCur.null' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('aa567374f31848eed755a7c537c9af02', 'zh_CN', '报价币种不能为空', 'bt.fcExcrateAdust.message.referCur.null');
END IF;
commit;
END;
/

/
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'bt.fcExcrateAdust.message.referCur.null' AND LANG_CODE = 'en_US';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('bb567374f31848eed755a7c537c9af02', 'en_US', 'Quotation currency is required.', 'bt.fcExcrateAdust.message.referCur.null');
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'bt.fcExcrateAdust.message.cur.null' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('cc567374f31848eed755a7c537c9af02', 'zh_CN', '标准币种不能为空', 'bt.fcExcrateAdust.message.cur.null');
END IF;
commit;
END;
/

/
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'bt.fcExcrateAdust.message.cur.null' AND LANG_CODE = 'en_US';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('dd567374f31848eed755a7c537c9af02', 'en_US', 'Standard  currency is required.', 'bt.fcExcrateAdust.message.cur.null');
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
--查看该表中该国际化是否存在
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.notification.markRead' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN
     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('8ad2a64b4cdb4e738dd9da5aef8a42b9', 'zh_CN', '标记为已读', 'sys.notification.markRead' );
END IF;
commit;
END;
/




DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.login.button.password' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('ll567374f31848eed755a7c537c9af11', 'zh_CN', '密码', 'sys.login.button.password');
END IF;
commit;
END;
/
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.login.button.password' AND LANG_CODE = 'en_US';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('ll567374f31848eed755a7c537c9af22', 'en_US', 'password', 'sys.login.button.password');
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.login.button.account' AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('ll567374f31848eed755a7c537c9af33', 'zh_CN', '账户', 'sys.login.button.account');
END IF;
commit;
END;
/
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.login.button.account' AND LANG_CODE = 'en_US';
IF v_COUNT < 1 THEN
    INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
    VALUES ('ll567374f31848eed755a7c537c9af44', 'en_US', 'account', 'sys.login.button.account');
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
--查看该表中该国际化是否存在
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'replenish.Today' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN
     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('43971581b1d2405e9212964b13aea8b1', 'zh_CN', '今', 'replenish.Today' );
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT
FROM sys_multi_lang
WHERE LANG_KEY = 'sys.config.i18n.isCloseMultiTenantMode'
  AND LANG_CODE = 'zh_CN';
IF v_COUNT < 1 THEN
        insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
        values ('f391208573a4497891726bc847202179', 'zh_CN', '是否关闭多租户模式', 'sys.config.i18n.isCloseMultiTenantMode' );
END IF;
commit;
END;
/






