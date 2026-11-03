## zmc 票据到期提醒

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'sys_notification_category' and COLUMN_NAME in ('CONTENT')) THEN
ALTER TABLE sys_notification_category MODIFY CONTENT VARCHAR(500) comment '消息模版内容';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF not EXISTS (SELECT 1 FROM sys_notification_category WHERE id = '5ff2fe20e7f248bzmc488f6eb312f902') THEN
        INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL,
                                               RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE, STAR_TARGET)
        VALUES ('5ff2fe20e7f248bzmc488f6eb312f902', '5ff2fe20e7f248b093488f6eb312f9a5', '到期票据汇总提醒', 'bill:accebill:emitbillpaySummary',
                '15dfb2276b9e4a7dbd01024c709c2873', '', null,
                'bill:accebill:abemitbillpay:query', 0, 4,
                '单位:$!{corpName}
        公司类型：$!{companyTYpeName}
        银行账号:$!{bankAcc}
        账户名:$!{reqNm}
        开户行:$!{bankName}
        当前余额:$!{bankAccBalance}
        应付票据总笔数：$!{billNums}笔
        票面汇总金额：$!{billMoney}
        票据到期解付汇总金额：$!{releasePaymentMoney}
        到期日期：$!{endDateStr}',
                1, null);
END IF;


END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF not EXISTS (SELECT 1 FROM sys_notification_category WHERE id = '5ff2fe20e7f248bzmc488f6eb312f903') THEN
        INSERT INTO sys_notification_category (ID, PARENT_ID, NAME, TAG, MENU_ID, LIST_URL, DETAIL_URL, RESOURCE_CONTENT, ROLE_FILTER, SORT, CONTENT, TYPE, STAR_TARGET) VALUES ('5ff2fe20e7f248bzmc488f6eb312f903', '5ff2fe20e7f248b093488f6eb312f9a5', '承兑账户资金余额不足承兑扣款消息提醒', 'bill:accebill:emitbillpaybalance', '15dfb2276b9e4a7dbd01024c709c2873', '', null, 'bill:accebill:abemitbillpay:query', 0, 4, '单位:$!{corpName}
公司类型：$!{companyTYpeName}
银行账号:$!{bankAcc}
账户名:$!{reqNm}
开户行:$!{bankName}
当前账户可用余额:$!{bankAccBalance}
当前开票的银行账户余额不够兑付
应付票据总笔数：$!{billNums}笔
票面汇总金额：$!{billMoney}
票据到期解付汇总金额：$!{releasePaymentMoney}
到期日期：$!{endDateStr}
需下拨金额：$!{xbMoney}', 1, null);
END IF;


END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO


## zmc 票据到期提醒

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('risk_contract_number')) THEN
ALTER TABLE ab_voucherinfo ADD risk_contract_number VARCHAR(500) comment '风险合同编号';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

## zmc 票据到期提醒

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('cvm_limit_no')) THEN
ALTER TABLE ab_voucherinfo ADD cvm_limit_no VARCHAR(500) comment 'CVM额度编号';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

## zmc 票据到期提醒

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('accptncagrmt_branch')) THEN
ALTER TABLE ab_voucherinfo ADD accptncagrmt_branch VARCHAR(500) comment '银承合作协议分行';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('accptncagrmt_branch_no')) THEN
ALTER TABLE ab_voucherinfo ADD accptncagrmt_branch_no VARCHAR(50) comment '银承合作协议分行行号';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('CMB_BNKBILNEG_ID')) THEN
ALTER TABLE ab_voucherinfo ADD CMB_BNKBILNEG_ID VARCHAR(50) comment '招行合作框架协议表id';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = (select database()) and TABLE_NAME = 'ab_voucherinfo' and COLUMN_NAME in ('CMB_CRLCON_ID')) THEN
ALTER TABLE ab_voucherinfo ADD CMB_CRLCON_ID VARCHAR(50) comment '招行风险合同信息表id';
END IF;
END$$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##新增人:方杨标
##新增时间:2024-04-22
##新增内容: 添加
CREATE TABLE IF NOT EXISTS CMB_CRLINFO
(
    ID             VARCHAR(32)  COMMENT '主键ID',
    CREDITLINEID   VARCHAR(100) COMMENT 'CVM额度编号',
    CONTRACTTEXTNO VARCHAR(100) COMMENT '文本协议编号',
    CREDITLINETYPE VARCHAR(60)  COMMENT '授信类型',
    CRLNUM         VARCHAR(100) COMMENT '信贷额度编号',
    CREATE_DATE    DATETIME     COMMENT '创建时间',
    UPDATE_DATE    DATETIME     COMMENT '更新时间',
    CONSTRAINT PK_CMB_CRLINFO_ID PRIMARY KEY (ID),
    constraint AK_CREDITLINEID unique (creditLineId)
    ) COMMENT ='招行协议文本信息表';
##GOGO


##新增人:方杨标
##新增时间:2024-04-22
##新增内容: 添加招行合作框架协议表
CREATE TABLE IF NOT EXISTS CMB_BNKBILNEG
(
    ID      VARCHAR(32)  COMMENT '主键ID',
    TEXTNO  VARCHAR(100) COMMENT '银承合作协议编号',
    ORGNAME VARCHAR(100) COMMENT '银承合作协议分行名称',
    BRNNBR  VARCHAR(60)  COMMENT '银承合作协议分行号',
    CREATE_DATE    DATETIME     COMMENT '创建时间',
    UPDATE_DATE    DATETIME     COMMENT '更新时间',
    CONSTRAINT PK_CMB_BNKBILNEG PRIMARY KEY (ID),
    constraint AK_TEXTNO unique (TEXTNO)
    ) COMMENT ='招行合作框架协议表';
##GOGO


##新增人:方杨标
##新增时间:2024-04-22
##新增内容: 添加招行风险合同信息表
CREATE TABLE IF NOT EXISTS CMB_CRLCON
(
    ID      VARCHAR(32)  COMMENT '主键ID',
    ACRCON  VARCHAR(100) COMMENT '风险合同编号',
    CONNUM  VARCHAR(100) COMMENT '信贷合同编号',
    CONNAM  VARCHAR(200) COMMENT '合同文本号',
    GRTTYP  VARCHAR(60)  COMMENT '担保物类型',
    CONCCY  VARCHAR(30)  COMMENT '合同币种',
    CONBRN  VARCHAR(100) COMMENT '合同机构号',
    BUSTYP  VARCHAR(60)  COMMENT '业务品种',
    GRTCCY  VARCHAR(30)  COMMENT '担保物币种',
    ZBRDNAM VARCHAR(600) COMMENT '合同机构行名',
    ZCONBRD VARCHAR(600) COMMENT '合同机构行号',
    GRTAVE  DECIMAL(17, 2) COMMENT '担保物可用金额',
    CONAMT  DECIMAL(17, 2) COMMENT '合同金额',
    CONBAL  DECIMAL(17, 2) COMMENT '合同业务余额',
    CONAVE  DECIMAL(17, 2) COMMENT '合同可用额度',
    GRTAMT  DECIMAL(17, 2) COMMENT '担保物登记金额',
    GRTPER  DECIMAL(17, 2) COMMENT '合同保证金比例',
    CREATE_DATE    DATETIME     COMMENT '创建时间',
    UPDATE_DATE    DATETIME     COMMENT '更新时间',
    CREDITLINEID   VARCHAR(100) COMMENT 'CVM额度编号',
    CONSTRAINT PK_CMB_CRLCON PRIMARY KEY (ID),
    constraint AK_ACRCON_CMB_CRLCON unique (ACRCON)
    ) COMMENT ='招行风险合同信息表';
##GOGO