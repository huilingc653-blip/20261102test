-- 2026-06-16 银行端新增Oracle脚本
UPDATE SYS_MULTI_LANG SET LANG_CONTEXT = '源单计划付款日期' WHERE LANG_KEY = 'NIS.PAYMENT.SHARED.PLAN.PAYDATE';

UPDATE tenant_p_notification_category SET NAME='自营付款'  where id = '2b8ad5cf399c4821a98b029b28857273';

UPDATE tenant_pre_act_extend_category SET NAME='自营付款'  where id = '180392ca4988425591faa261b0140327';

update bt_m_bpm_flow_category set name = '系统管理' where id = '24fe7756278447aca8934976e9d84c2f';

DECLARE
v_COUNT NUMBER;
BEGIN
--查看该流程分类是否存在
SELECT COUNT(*) INTO v_COUNT FROM BT_M_BPM_FLOW_CATEGORY
WHERE ID = '78c9f3fef02e11f08cd7005056b9095g';
--如果小于1则说明不存在，则新增流程分类
IF v_COUNT < 1 THEN
    insert into BT_M_BPM_FLOW_CATEGORY (ID, PARENT_ID, NAME, KEY_, "TYPE", URL, SORT, MOBILE_URL, NOTIFICATION_ID, MUST_APPROVE, BATCH_REVOKABLE, MENU_ID, MOBILE_APPROVAL_FLAG, RESOURCE_CONTENT, REVOKE_NOTIFICATION_ID)
    values ('78c9f3fef02e11f08cd7005056b9095g', '24fe7756278447aca8934976e9d84c2f', '消息管理', 'message', '1', null, 40, null, null, '0', 1, null, 1, null, null);
END IF;
commit;
END;
/

update BT_M_BPM_FLOW_CATEGORY set PARENT_ID = '78c9f3fef02e11f08cd7005056b9095g' where KEY_ = 'article';

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'a1f3c8e24b5d4e6a9c0d1e2f3a4b5c6d', 'zh_CN', '归还日期不能为空', 'cms.precative.repaydate.not.null' from dual
where not exists (
    select 1 from sys_multi_lang
    where LANG_KEY = 'cms.precative.repaydate.not.null' AND LANG_CODE = 'zh_CN'
);

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'b2e4d9f35c6e4f7b0d1e2f3a4b5c6d7e', 'en_US', 'Repayment date cannot be empty', 'cms.precative.repaydate.not.null' from dual
where not exists (
    select 1 from sys_multi_lang
    where LANG_KEY = 'cms.precative.repaydate.not.null' AND LANG_CODE = 'en_US'
);
commit;
/




DECLARE
v_COUNT NUMBER;
BEGIN
--查看该表中该国际化是否存在
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'bpm.message.delete.user.start.flow.unrecoverable' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values (SYS_GUID(), 'zh_CN', '流程删除后不可恢复的，确定要批量删除吗？', 'bpm.message.delete.user.start.flow.unrecoverable' );
END IF;
commit;
END;
/



--新增表格不能隐藏所有列内容国际化
DECLARE
v_COUNT NUMBER;
BEGIN
--查看该表中该国际化是否存在
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'common.button.theadHide.msg' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN
     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('457fe42c06154baca6a91e3e9bc1ba66', 'zh_CN', '表格不能隐藏所有列内容', 'common.button.theadHide.msg' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
--查看该表中该国际化是否存在
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'common.button.theadHide.msg' and LANG_CODE = 'en_US';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN
     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('547fe42c06154baca6a91e3e9bc1ba66', 'en_US', 'The table cannot hide all column contents', 'common.button.theadHide.msg' );
END IF;
commit;
END;
/


DECLARE
v_COUNT NUMBER;
BEGIN
--查看该表中该国际化是否存在
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'sys.corp.message.verify,bankcode.not.null' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values (SYS_GUID(), 'zh_CN', '银行行号不能为空！', 'sys.corp.message.verify,bankcode.not.null' );
END IF;
commit;
END;
/

DECLARE
v_COUNT NUMBER;
BEGIN
--查看该表中该国际化是否存在
SELECT COUNT(*) INTO v_COUNT FROM sys_multi_lang
WHERE LANG_KEY = 'cms.exguarantee.repeated' and LANG_CODE = 'zh_CN';
--如果小于1则说明不存在，则新增国际化
IF v_COUNT < 1 THEN

     insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
values ('4adcbad9baef45009cee355f5c51b550', 'zh_CN', '担保编号已存在，请勿重复录入！', 'cms.exguarantee.repeated' );
END IF;
commit;
END;
/

update SYS_INTERNATION_BASE set tenant_id = null where key_id = '402880385c15400e015c154cd3450006';
update SYS_INTERNATION_BASE set LANG_CONTEXT = '系统管理' where key_id = '402880385c15400e015c154cd3450006' and lang_code = 'zh_CN';


update SYS_INTERNATION_BASE set tenant_id = null where key_id = '49db09315da743d8a410bfcc86f7f01f';
update SYS_INTERNATION_BASE set LANG_CONTEXT = '机构管理' where key_id = '49db09315da743d8a410bfcc86f7f01f' and lang_code = 'zh_CN';

update SYS_INTERNATION_BASE set tenant_id = null where key_id = '402880425b951777015b951fd12b0004';
update SYS_INTERNATION_BASE set LANG_CONTEXT = '机构信息管理' where key_id = '402880425b951777015b951fd12b0004' and lang_code = 'zh_CN';

update SYS_INTERNATION_BASE set tenant_id = null where key_id = '402880425b951777015b951f77320003';
update SYS_INTERNATION_BASE set LANG_CONTEXT = '机构角色管理' where key_id = '402880425b951777015b951f77320003' and lang_code = 'zh_CN';


update SYS_INTERNATION_BASE set tenant_id = null where key_id = '402880425b951777015b95203d670005';
update SYS_INTERNATION_BASE set LANG_CONTEXT = '机构用户管理' where key_id = '402880425b951777015b95203d670005' and lang_code = 'zh_CN';
commit;
/

-- 更新投资理财相关预菜单
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/config/choBankConfig' WHERE URL = '/chowmatistic/config/choBankConfig' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/config/choProductConfig' WHERE URL = '/chowmatistic/config/choProductConfig' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/config/choInnerConfig' WHERE URL = '/chowmatistic/config/choInnerConfig' AND ui_version = '1';

UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/chobankfinance/transferToFixed' WHERE URL = '/chowmatistic/chobankfinance/list/10' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/chobankfinance/transferToFixedBl' WHERE URL = '/chowmatistic/chobankfinance/listBl/10' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/chobankfinance/fixedToTransfer' WHERE URL = '/chowmatistic/chobankfinance/list/11' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/chobankfinance/transferToNotice' WHERE URL = '/chowmatistic/chobankfinance/list/12' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/chobankfinance/transferToNoticeBl' WHERE URL = '/chowmatistic/chobankfinance/listBl/12' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/chobankfinance/noticeToTransfer' WHERE URL = '/chowmatistic/chobankfinance/list/13' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/chobankfinance/query' WHERE URL = '/chowmatistic/chobankfinance/list/0' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/chobankfinance/listQuery' WHERE URL = '/chowmatistic/chobankfinance/listQuery' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/chobankfinance/listReport' WHERE URL = '/chowmatistic/chobankfinance/listReport' AND ui_version = '1';

UPDATE tenant_pre_menu SET URL = '/sub-finance/#/choproduct/list' WHERE URL = '/chowmatistic/choproduct/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/choproduct/listChoProductBuyApply' WHERE URL = '/chowmatistic/choproductBuyApply/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/choproduct/listChoProductReq' WHERE URL = '/chowmatistic/choproductreq/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/choproduct/listChoProductMaintain' WHERE URL = '/chowmatistic/choproductMaintain/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/choproduct/listChoProductRedeem' WHERE URL = '/chowmatistic/choproductredeem/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/choproduct/listChoProductRenewal' WHERE URL = '/chowmatistic/choproductRenewal/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/choproduct/listChoProductNetVal' WHERE URL = '/chowmatistic/choproductNetVal/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/choproduct/listChoProductReqQueryDir' WHERE URL = '/chowmatistic/choproductreqquery/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/choproduct/listChoProductReqUncollectedDir' WHERE URL = '/chowmatistic/choproductreqquery/uncollectedList' AND ui_version = '1';

UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/listRegCho' WHERE URL = '/agreeddeposit/choinneragreeddeposit/listRegCho' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/listPosCho' WHERE URL = '/agreeddeposit/choinneragreeddeposit/listPosCho' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/listTermCho' WHERE URL = '/agreeddeposit/choinneragreeddeposit/listTermCho' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/transferToFixed' WHERE URL = '/chowmatistic/choinnerfinance/list/10' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/fixedToTransfer' WHERE URL = '/chowmatistic/choinnerfinance/list/11' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/transferToNotice' WHERE URL = '/chowmatistic/choinnerfinance/list/12' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/notifyAppointment' WHERE URL = '/chowmatistic/choinnerfinance/list/14' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/cancelNotifyAppointment' WHERE URL = '/chowmatistic/choinnerfinance/list/15' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/noticeToTransfer' WHERE URL = '/chowmatistic/choinnerfinance/list/13' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/listQuery' WHERE URL = '/chowmatistic/choinnerfinance/listQuery' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/query' WHERE URL = '/agreeddeposit/choinneragreeddeposit/query' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/choinneragreeddeposit/listReport' WHERE URL = '/chowmatistic/choinnerfinance/listReport' AND ui_version = '1';

UPDATE tenant_pre_menu SET URL = '/sub-finance/#/pricing/basedata/paramconfig/pricingconfig' WHERE URL = '/pricing/basedata/paramconfig/pricingconfig' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/depositRate/interestRateType' WHERE URL = '/pricing/depositRateType/interestRateType/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/depositRate/interestRatePreserve' WHERE URL = '/pricing/depositRate/interestRatePreserve/list' AND ui_version = '1';
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/chowmatistic/depositRate/interestRateReCheck' WHERE URL = '/pricing/depositRate/interestRateReCheck/list' AND ui_version = '1';

-- 修改 PROPORTION 列
ALTER TABLE gua_exguarantee_amount MODIFY PROPORTION NUMBER(10,6) NULL;
COMMENT ON COLUMN gua_exguarantee_amount.PROPORTION IS '对外担保限额占比';

-- 修改 ASSETS 列
ALTER TABLE gua_exguarantee_amount MODIFY ASSETS NUMBER(16,4) NULL;
COMMENT ON COLUMN gua_exguarantee_amount.ASSETS IS '当期净资产';

-- SYS_LOG_MANAGER 表增加 ID 主键
DECLARE
    v_table_count NUMBER;
    v_pk_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_table_count
    FROM user_tables
    WHERE table_name = 'SYS_LOG_MANAGER';

    SELECT COUNT(*) INTO v_pk_count
    FROM user_constraints
    WHERE table_name = 'SYS_LOG_MANAGER'
      AND constraint_type = 'P';

    IF v_table_count > 0 AND v_pk_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE SYS_LOG_MANAGER ADD CONSTRAINT PK_SYS_LOG_MANAGER PRIMARY KEY (ID)';
    END IF;
END;
/

-- 添加工作流相关多语言
INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '1f9c13e3924911f19fca005056b97e85', 'zh_CN', '未找到注册回调服务请检查配置或者路径', 'workflow.no.complete.err' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'workflow.no.complete.err' AND LANG_CODE = 'zh_CN');

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT '48f838c7924911f19fca005056b97e85', 'en_US', 'Registration callback service not found. Please check the configuration or path', 'workflow.no.complete.err' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_multi_lang WHERE LANG_KEY = 'workflow.no.complete.err' AND LANG_CODE = 'en_US');


-- ==================== 前后端分离菜单 URL 更新（来源: doc/产研任务看板.xlsx 第一个sheet N原url->O现url） ====================
-- Oracle 版本，与 mysql/7.DbUpdate.sql 菜单部分保持一致；已排除已更新过的旧URL
-- ============ sub-netbank ============
UPDATE tenant_pre_menu SET URL = '/sub-netbank/#/bdlink/bisgathercmd/listSend' WHERE URL = '/bdlink/bisgathercmd/listSend' AND ui_version = '1';  -- 银企直联 / 收款指令管理/指令发送

-- ============ sub-other ============
UPDATE tenant_pre_menu SET URL = '/sub-other/#/fbsbudgetsuper/yearPlan' WHERE URL = '/budget/fbsbudget/superPre/1' AND ui_version = '1';  -- 计划追加 / 年计划
UPDATE tenant_pre_menu SET URL = '/sub-other/#/fbsbudgetsuper/quarterPlan' WHERE URL = '/budget/fbsbudget/superPre/2' AND ui_version = '1';  -- 计划追加 / 季计划
UPDATE tenant_pre_menu SET URL = '/sub-other/#/fbsbudgetsuper/montPlan' WHERE URL = '/budget/fbsbudget/superPre/3' AND ui_version = '1';  -- 计划追加 / 月计划
UPDATE tenant_pre_menu SET URL = '/sub-other/#/fbsbudgetsuper/weekPlan' WHERE URL = '/budget/fbsbudget/superPre/4' AND ui_version = '1';  -- 计划追加 / 周计划
UPDATE tenant_pre_menu SET URL = '/sub-other/#/fbsbudgetsuper/dayPlan' WHERE URL = '/budget/fbsbudget/superPre/5' AND ui_version = '1';  -- 计划追加 / 天计划

-- ============ sub-finance ============
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/guarantee/guaGuaranteeInfo/guaGuaranteeInfo/list' WHERE URL = '/letter/guarantee/guaGuaranteeInfo/list' AND ui_version = '1';  -- 保函管理 / 保函申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/guarantee/guaGuaranteeInfo/guaGuaranteeInfo/guaGuaranteeRecive' WHERE URL = '/letter/guarantee/guaGuaranteeRecive/list' AND ui_version = '1';  -- 保函管理 / 保函领取
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/guarantee/guaGuaranteeInfo/guaGuaranteeInfo/guaPerformReg' WHERE URL = '/letter/guarantee/guaPerformReg/list' AND ui_version = '1';  -- 保函管理 / 保函履行登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/paramconfig/list' WHERE URL = '/cms/basedata/paramconfig/list' AND ui_version = '1';  -- 基础数据 / 信贷管理系统参数
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/loantype/list' WHERE URL = '/cms/credit/loantype/list' AND ui_version = '1';  -- 基础数据 / 信贷基础数据 / 贷款类型维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/borrowingtype/list' WHERE URL = '/cms/credit/borrowingtype/list' AND ui_version = '1';  -- 基础数据 / 信贷基础数据 / 借款类型维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/externalPricing/baseRate' WHERE URL = '/rate/tbbaserate/list' AND ui_version = '1';  -- 基础数据 / 外部定价 / 利率类型维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/externalPricing/creditPeriod' WHERE URL = '/period/tbcreditperiod/list' AND ui_version = '1';  -- 基础数据 / 外部定价 / 贷款期限维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/externalPricing/loanRate' WHERE URL = '/cms/loanrate/maintence/listBase' AND ui_version = '1';  -- 基础数据 / 外部定价 / 人民币贷款利率维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/externalPricing/loanRate/notBase' WHERE URL = '/cms/loanrate/maintence/listNotBase' AND ui_version = '1';  -- 基础数据 / 外部定价 / 外币贷款利率维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/externalPricing/loanRate/baseCheck' WHERE URL = '/cms/loanrate/maintence/listCheckBase' AND ui_version = '1';  -- 基础数据 / 外部定价 / 人民币贷款利率复核
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/externalPricing/loanRate/notBaseCheck' WHERE URL = '/cms/loanrate/maintence/listCheckNotBase' AND ui_version = '1';  -- 基础数据 / 外部定价 / 外币贷款利率复核
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/externalPricing/thirdPartyCostType' WHERE URL = '/cms/dfCostType/listCostType' AND ui_version = '1';  -- 基础数据 / 外部定价 / 第三方费用类型维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/externalPricing/accrualType' WHERE URL = '/cms/accrual/accrualtype/list' AND ui_version = '1';  -- 基础数据 / 外部定价 / 计提方式维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/basedata/externalPricing/repayType' WHERE URL = '/cms/repay/repaytype/list' AND ui_version = '1';  -- 基础数据 / 外部定价 / 还款方式维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloaninfoApply/list' WHERE URL = '/cms/loan/cmsloaninfoApply/list' AND ui_version = '1';  -- 内部贷款 / 贷款申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloaninfo/repair' WHERE URL = '/cms/loan/cmsloaninfo/repair' AND ui_version = '1';  -- 内部贷款 / 贷款申请补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloaninfo/send' WHERE URL = '/cms/loan/cmsloaninfo/send' AND ui_version = '1';  -- 内部贷款 / 贷款发放
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloansend/list' WHERE URL = '/cms/loan/cmsloansend/list' AND ui_version = '1';  -- 内部贷款 / 贷款合同生成
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloansend/listStayRepay' WHERE URL = '/cms/loan/cmsloansend/listStayRepay' AND ui_version = '1';  -- 内部贷款 / 贷款还款申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloanrepay/list' WHERE URL = '/cms/loan/cmsloanrepay/list' AND ui_version = '1';  -- 内部贷款 / 贷款还款修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloanrepay/listInteretAffirm' WHERE URL = '/cms/loan/cmsloanrepay/listInteretAffirm' AND ui_version = '1';  -- 内部贷款 / 贷款利息确认
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloansend/listDeferred' WHERE URL = '/cms/loan/cmsloansend/listDeferred' AND ui_version = '1';  -- 内部贷款 / 贷款展期申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloandeferred/list' WHERE URL = '/cms/loan/cmsloandeferred/list' AND ui_version = '1';  -- 内部贷款 / 贷款展期修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloandeferred/listProcess' WHERE URL = '/cms/loan/cmsloandeferred/listProcess' AND ui_version = '1';  -- 内部贷款 / 贷款展期处理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmscontract/contractSet' WHERE URL = '/cms/loan/cmscontract/contractSet' AND ui_version = '1';  -- 内部贷款 / 贷款合同设置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloansend/listStayRepayEdit' WHERE URL = '/cms/loan/cmsloansend/listStayRepay?repayPlan=1' AND ui_version = '1';  -- 内部贷款 / 贷款还款计划修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/provide/loanInfoApply/list' WHERE URL = '/cms/provide/loanInfoApply/list' AND ui_version = '1';  -- 银行借款 / 借款申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/provide/loaninfo/listRepair' WHERE URL = '/cms/provide/loaninfo/listRepair' AND ui_version = '1';  -- 银行借款 / 借款申请补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/provide/loansend/list' WHERE URL = '/cms/provide/loansend/list' AND ui_version = '1';  -- 银行借款 / 借款发放
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/repay-apply' WHERE URL = '/cms/provide/loanrepay/listAppTab' AND ui_version = '1';  -- 银行借款 / 借款还款申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/repay-modify' WHERE URL = '/cms/provide/loanrepay/listUpOrRep?upRepSing=1' AND ui_version = '1';  -- 银行借款 / 借款还款修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/repay-supplement' WHERE URL = '/cms/provide/loanrepay/listUpOrRep' AND ui_version = '1';  -- 银行借款 / 借款还款补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/extension-apply' WHERE URL = '/cms/provide/provideloandeferred/list' AND ui_version = '1';  -- 银行借款 / 借款展期申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/extension-modify' WHERE URL = '/cms/provide/provideloandeferred/listUpdate' AND ui_version = '1';  -- 银行借款 / 借款展期修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/extension-supplement' WHERE URL = '/cms/provide/provideloandeferred/listAdditional' AND ui_version = '1';  -- 银行借款 / 借款展期补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/extension-handle' WHERE URL = '/cms/provide/provideloandeferred/listhandle' AND ui_version = '1';  -- 银行借款 / 借款展期处理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/repay-plan-modify' WHERE URL = '/cms/provide/loanrepay/listApp/?repayPlan=1' AND ui_version = '1';  -- 银行借款 / 借款还款计划修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/cancel-apply' WHERE URL = '/cms/provide/abolish/listCancellation' AND ui_version = '1';  -- 银行借款 / 借款作废申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/cancel-handle' WHERE URL = '/cms/provide/abolish/listCancellationStart' AND ui_version = '1';  -- 银行借款 / 借款作废办理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/interest-way-modify' WHERE URL = '/cms/provide/loanrepay/listApp/?repayPlan=2' AND ui_version = '1';  -- 银行借款 / 计息方式修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/rate-adjust' WHERE URL = '/cms/provide/loaninfo/listLoanRate' AND ui_version = '1';  -- 银行借款 / 借款利率调整
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/float-way-modify' WHERE URL = '/cms/provide/loanrepay/listApp/?repayPlan=3' AND ui_version = '1';  -- 银行借款 / 浮动方式修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bank-loan/bank-loan-change' WHERE URL = '/cms/provide/loaninfo/listChange' AND ui_version = '1';  -- 银行借款 / 银行借款变更
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/record' WHERE URL = '/cms/precative/loaninfo/list' AND ui_version = '1';  -- 委托贷款 / 委托贷款录入
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/edit' WHERE URL = '/cms/precative/loaninfo/changeList/Edit' AND ui_version = '1';  -- 委托贷款 / 委托贷款修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/supplement' WHERE URL = '/cms/precative/loaninfo/changeList/repair' AND ui_version = '1';  -- 委托贷款 / 委托贷款补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/grant' WHERE URL = '/cms/precative/loansend/list' AND ui_version = '1';  -- 委托贷款 / 委托贷款发放
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/repay' WHERE URL = '/cms/precative/loanrepay/list' AND ui_version = '1';  -- 委托贷款 / 委托贷款归还
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/repay-edit' WHERE URL = '/cms/precative/loanrepay/editList' AND ui_version = '1';  -- 委托贷款 / 委托贷款归还修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/interest-confirm' WHERE URL = '/cms/precative/loanrepay/listInteretAffirm' AND ui_version = '1';  -- 委托贷款 / 委托贷款利息确认
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/repay-supplement' WHERE URL = '/cms/precative/loanrepay/listRepayAdditional' AND ui_version = '1';  -- 委托贷款 / 委托贷款还款补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/extend-apply' WHERE URL = '/cms/precative/precativeloandeferred/listApply' AND ui_version = '1';  -- 委托贷款 / 委托贷款展期申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/extend-edit' WHERE URL = '/cms/precative/precativeloandeferred/listupdate' AND ui_version = '1';  -- 委托贷款 / 委托贷款展期修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/extend-supplement' WHERE URL = '/cms/precative/precativeloandeferred/listAdditional' AND ui_version = '1';  -- 委托贷款 / 委托贷款展期补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/extend-handle' WHERE URL = '/cms/precative/precativeloandeferred/listHandle' AND ui_version = '1';  -- 委托贷款 / 委托贷款展期处理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/entrust-loan/repay-plan-modify' WHERE URL = '/cms/precative/loanrepay/list?repayPlan=1' AND ui_version = '1';  -- 委托贷款 / 委托贷款还款计划修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/config/ratConfig' WHERE URL = '/cms/rat/config/ratConfig' AND ui_version = '1';  -- 授信管理 / 授信基础数据 / 授信系统管理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/config/ratitemtype' WHERE URL = '/cms/rat/ratitemtype/list' AND ui_version = '1';  -- 授信管理 / 授信基础数据 / 授信品种类型维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/config/ratcredittype' WHERE URL = '/cms/rat/ratcredittype/list' AND ui_version = '1';  -- 授信管理 / 授信基础数据 / 授信额度类型维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/config/itemcustom' WHERE URL = '/cms/rat/ratitemcustom/list' AND ui_version = '1';  -- 授信管理 / 授信基础数据 / 授信品种维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/config/businessrace' WHERE URL = '/cms/rat/ratBusinessRace/list' AND ui_version = '1';  -- 授信管理 / 授信基础数据 / 业务品种维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/config/corpRation' WHERE URL = '/cms/rat/ratbkration/queryCorp' AND ui_version = '1';  -- 授信管理 / 授信基础数据 / 单位授信主体设置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/ratbkration/list' WHERE URL = '/cms/rat/ratbkration/list' AND ui_version = '1';  -- 授信管理 / 银行授信管理 / 银行授信额度录入
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/ratbkrationdis/list' WHERE URL = '/cms/rat/ratbkrationdis/list' AND ui_version = '1';  -- 授信管理 / 银行授信管理 / 银行授信额度分配
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/ratbkrationadd/cplist' WHERE URL = '/cms/rat/ratbkrationadd/cplist' AND ui_version = '1';  -- 授信管理 / 银行授信管理 / 集团授信额度追加
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/ratbkrationadd/list' WHERE URL = '/cms/rat/ratbkrationadd/list' AND ui_version = '1';  -- 授信管理 / 银行授信管理 / 银行授信额度追加
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/ratbkrationrelief/list' WHERE URL = '/cms/rat/ratbkrationrelief/list' AND ui_version = '1';  -- 授信管理 / 银行授信管理 / 银行授信额度调剂
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/ratbkrationadjust/ratAdjust' WHERE URL = '/cms/rat/ratbkrationadjust/ratAdjust' AND ui_version = '1';  -- 授信管理 / 银行授信管理 / 最高额度调整
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/raturationapply/list' WHERE URL = '/cms/rat/raturationapply/list' AND ui_version = '1';  -- 授信管理 / 银行授信管理 / 银行额度系统占用
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/ratbkrationdelay/list' WHERE URL = '/cms/rat/ratbkrationdelay/list' AND ui_version = '1';  -- 授信管理 / 银行授信管理 / 授信延期
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/raturation/list' WHERE URL = '/cms/rat/raturation/list' AND ui_version = '1';  -- 授信管理 / 银行授信管理 / 授信结转
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/enterpriseCredit/entry' WHERE URL = '/cms/rat/ratcpration/list' AND ui_version = '1';  -- 授信管理 / 企业授信管理 / 内部授信额度录入
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/enterpriseCredit/addition' WHERE URL = '/cms/rat/ratcprationadd/list' AND ui_version = '1';  -- 授信管理 / 企业授信管理 / 内部授信额度追加
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/enterpriseCredit/distribution' WHERE URL = '/cms/rat/ratcprationdis/list' AND ui_version = '1';  -- 授信管理 / 企业授信管理 / 内部授信额度分配
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/enterpriseCredit/occupancy' WHERE URL = '/cms/rat/cprationapply/list' AND ui_version = '1';  -- 授信管理 / 企业授信管理 / 内部授信额度占用
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/enterpriseCredit/adjustment' WHERE URL = '/cms/rat/ratcprationrelief/list' AND ui_version = '1';  -- 授信管理 / 企业授信管理 / 内部授信额度调剂
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/credit/rat/enterpriseCredit/transfer' WHERE URL = '/cms/rat/ratcpuration/list' AND ui_version = '1';  -- 授信管理 / 企业授信管理 / 内部授信额度转移
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/loan/cmsloaninfo/listQuery' WHERE URL = '/cms/loan/cmsloaninfo/listQuery' AND ui_version = '1';  -- 查询 / 内部贷款查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/provide/loaninfo/queryLoan' WHERE URL = '/cms/provide/loaninfo/queryLoan' AND ui_version = '1';  -- 查询 / 银行借款查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/precative/precativeQuery/list' WHERE URL = '/cms/precative/precativeQuery/list' AND ui_version = '1';  -- 查询 / 委托贷款查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/report/listReportInterest' WHERE URL = '/inl/report/listReportInterest' AND ui_version = '1';  -- 查询 / 资金拆借查询 / 内部拆借利息统计
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/report/listReport' WHERE URL = '/inl/report/listReport' AND ui_version = '1';  -- 查询 / 资金拆借查询 / 内部拆借台账
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/raturation/rationQuery' WHERE URL = '/cms/rat/raturation/rationQuery' AND ui_version = '1';  -- 查询 / 银行授信额度明细查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/ratcprationcount/rationQuery' WHERE URL = '/cms/rat/ratcprationcount/rationQuery' AND ui_version = '1';  -- 查询 / 内部授信查询 / 内部授信额度明细查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/ratcprationcount/rationCountQuery' WHERE URL = '/cms/rat/ratcprationcount/rationCountQuery' AND ui_version = '1';  -- 查询 / 内部授信查询 / 内部授信额度统计
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/rat/raturation/RaturationUsedItemDetailQuery' WHERE URL = '/cms/rat/raturation/RaturationUsedItemDetailQuery' AND ui_version = '1';  -- 查询 / 银行授信业务品种明细查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/inlConfig/showConfig' WHERE URL = '/inl/inlConfig/showConfig' AND ui_version = '1';  -- 资金拆借 / 基础数据 / 参数设置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/info/list' WHERE URL = '/inl/info/list' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/send/listSendApp' WHERE URL = '/inl/send/listSendApp' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借放款
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/send/listSendUpdate' WHERE URL = '/inl/send/listSendUpdate' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借放款修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/send/listSendConfirm' WHERE URL = '/inl/send/listSendConfirm' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借放款确认
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/repay/listInlRepay' WHERE URL = '/inl/repay/listInlRepay' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借还款
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/repay/listRepayUpdate' WHERE URL = '/inl/repay/listRepayUpdate' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借还款修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/repay/listRepayConfirm' WHERE URL = '/inl/repay/listRepayConfirm' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借还款确认
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/deferred/listInlDeferred' WHERE URL = '/inl/deferred/listInlDeferred' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借展期
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/abolish/listInlAbolish' WHERE URL = '/inl/abolish/listInlAbolish' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借作废
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/change/listInlChange' WHERE URL = '/inl/change/listInlChange' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借还款计划修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/inl/adjustrate/listInlAdjustrate' WHERE URL = '/inl/adjustrate/listInlAdjustrate' AND ui_version = '1';  -- 资金拆借 / 内部资金拆借 / 内部拆借利率调整
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/dfDirectFinanceInfo/list' WHERE URL = '/cms/dfDirectFinanceInfo/list' AND ui_version = '1';  -- 直融业务管理 / 直融业务登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/dfDirectFinanceRepay/listDfDirectFinanceRepay' WHERE URL = '/cms/dfDirectFinanceRepay/listDfDirectFinanceRepay' AND ui_version = '1';  -- 直融业务管理 / 直融业务放款
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/dfDirectFinanceRepay/listTempOrback' WHERE URL = '/cms/dfDirectFinanceRepay/listTempOrback' AND ui_version = '1';  -- 直融业务管理 / 直融业务还款修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/dfDirectFinanceInfo/listDfDirectFinanceReport' WHERE URL = '/cms/dfDirectFinanceInfo/listDfDirectFinanceReport' AND ui_version = '1';  -- 直融业务管理 / 直融业务查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/dfBusinessType/listBusinessType' WHERE URL = '/cms/dfBusinessType/listBusinessType' AND ui_version = '1';  -- 直融业务管理 / 直融类别维护
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/baseData/leaseType' WHERE URL = '/cms/lease/baseData/leaseType' AND ui_version = '1';  -- 融资租赁 / 基础数据 / 租赁物类别
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/financing/info/list' WHERE URL = '/cms/lease/financing/info/list' AND ui_version = '1';  -- 融资租赁 / 融资租赁申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/financing/info/listRepair' WHERE URL = '/cms/lease/financing/info/listRepair' AND ui_version = '1';  -- 融资租赁 / 融资租赁补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/LeaseObjectInfo/list' WHERE URL = '/cms/LeaseObjectInfo/list' AND ui_version = '1';  -- 融资租赁 / 融资租赁标的物登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/UpdateObjectInfo/list' WHERE URL = '/cms/lease/UpdateObjectInfo/list' AND ui_version = '1';  -- 融资租赁 / 融资租赁标的物修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/leasecapitalplan/list/1' WHERE URL = '/cms/lease/leasecapitalplan/list/1' AND ui_version = '1';  -- 融资租赁 / 融资租赁租金计划登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/leasecapitalplan/list/0' WHERE URL = '/cms/lease/leasecapitalplan/list/0' AND ui_version = '1';  -- 融资租赁 / 融资租赁租金计划修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/leasepay/list' WHERE URL = '/cms/lease/leasepay/list' AND ui_version = '1';  -- 融资租赁 / 融资租赁租金支付申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/LeaseExtend/list' WHERE URL = '/cms/LeaseExtend/list' AND ui_version = '1';  -- 融资租赁 / 融资租赁展期
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/update/list' WHERE URL = '/cms/lease/update/list' AND ui_version = '1';  -- 融资租赁 / 融资租赁变更
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/after-lease-manage/surrender' WHERE URL = '/cms/afterLease/refund/list' AND ui_version = '1';  -- 融资租赁 / 租后管理 / 融资租赁退租
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/after-lease-manage/renew' WHERE URL = '/cms/afterLease/continue/list' AND ui_version = '1';  -- 融资租赁 / 租后管理 / 融资租赁续租
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/after-lease-manage/buy-back' WHERE URL = '/cms/afterLease/buyBack/list' AND ui_version = '1';  -- 融资租赁 / 租后管理 / 融资租赁回购
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/query/apply-query' WHERE URL = '/cms/queryLease/financingInfo/list' AND ui_version = '1';  -- 融资租赁 / 融资租赁查询 / 融资租赁申请查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/lease/query/rent-query' WHERE URL = '/cms/lease/leasecapitalplan/listQuery' AND ui_version = '1';  -- 融资租赁 / 融资租赁查询 / 融资租赁租金查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/equity/type/list' WHERE URL = '/cms/equity/type/list' AND ui_version = '1';  -- 股权投资 / 基础数据 / 股权投资类型
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/equity/info/list' WHERE URL = '/cms/equity/info/list' AND ui_version = '1';  -- 股权投资 / 股权投资登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/equity/payPlan/list' WHERE URL = '/cms/equity/payPlan/list' AND ui_version = '1';  -- 股权投资 / 缴付 / 过户计划登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/equity/payPlan/payPlanUpdatelist' WHERE URL = '/cms/equity/payPlan/payPlanUpdatelist' AND ui_version = '1';  -- 股权投资 / 缴付 / 过户计划修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/equity/payThe/list' WHERE URL = '/cms/equity/payThe/list' AND ui_version = '1';  -- 股权投资 / 股权投资缴付到账 / 过户
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/equity/repoPlan/list' WHERE URL = '/cms/equity/repoPlan/list' AND ui_version = '1';  -- 股权投资 / 股权投资回购计划登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/equity/repoPlan/updateList' WHERE URL = '/cms/equity/repoPlan/updateList' AND ui_version = '1';  -- 股权投资 / 股权投资回购计划修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/equity/repo/list' WHERE URL = '/cms/equity/repo/list' AND ui_version = '1';  -- 股权投资 / 股权投资回购
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/equity/info/query' WHERE URL = '/cms/equity/info/query' AND ui_version = '1';  -- 股权投资 / 股权投资登记查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/type/list' WHERE URL = '/cms/security/type/list' AND ui_version = '1';  -- 资产证券化 / 基础数据 / 资产证券化类型
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/level/list' WHERE URL = '/cms/security/level/list' AND ui_version = '1';  -- 资产证券化 / 基础数据 / 资产证券化层级
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/assetType/list' WHERE URL = '/cms/security/assetType/list' AND ui_version = '1';  -- 资产证券化 / 基础数据 / 基础资产类型
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/info/list' WHERE URL = '/cms/security/info/list' AND ui_version = '1';  -- 资产证券化 / 资产证券化登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/issue/list' WHERE URL = '/cms/security/issue/list' AND ui_version = '1';  -- 资产证券化 / 资产证券化发行
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/toAccount/list' WHERE URL = '/cms/security/toAccount/list' AND ui_version = '1';  -- 资产证券化 / 发行到账确认
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/plan/list' WHERE URL = '/cms/security/plan/list' AND ui_version = '1';  -- 资产证券化 / 还款计划登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/plan/listUpdate' WHERE URL = '/cms/security/plan/listUpdate' AND ui_version = '1';  -- 资产证券化 / 还款计划修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/repaySure/list' WHERE URL = '/cms/security/repaySure/list' AND ui_version = '1';  -- 资产证券化 / 还款确认
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/security/issue/queryList' WHERE URL = '/cms/security/issue/queryList' AND ui_version = '1';  -- 资产证券化 / 资产证券化查询 / 发行查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/base-data/type' WHERE URL = '/cms/bond/type/list' AND ui_version = '1';  -- 债券 / 基础数据 / 债券类型
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/register' WHERE URL = '/cms/bond/register/list' AND ui_version = '1';  -- 债券 / 债券注册
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/issue' WHERE URL = '/cms/bond/issue/list' AND ui_version = '1';  -- 债券 / 债券发行
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/repay-plan-register' WHERE URL = '/cms/bond/cmsbondrepayplan/list/1' AND ui_version = '1';  -- 债券 / 债券还款计划登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/repay-plan-modify' WHERE URL = '/cms/bond/cmsbondrepayplan/list/0' AND ui_version = '1';  -- 债券 / 债券还款计划修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/convertible-bond-register' WHERE URL = '/cms/bond/transfer/listTransferRegisterInfo' AND ui_version = '1';  -- 债券 / 可转债登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/exchangeable-bond-register' WHERE URL = '/cms/bond/swap/listSwapRegister' AND ui_version = '1';  -- 债券 / 可交换债登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/repay-handle' WHERE URL = '/cms/bond/cmsbondpay/list' AND ui_version = '1';  -- 债券 / 债券还款处理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/interest-handle' WHERE URL = '/cms/bond/cmsbondpayinterest/list' AND ui_version = '1';  -- 债券 / 债券付息处理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/extend' WHERE URL = '/cms/bond/extend/listBondExtendRegisterInfo' AND ui_version = '1';  -- 债券 / 债券展期
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/cms/bond/query/issue' WHERE URL = '/cms/bond/issue/queryList' AND ui_version = '1';  -- 债券 / 债券发行查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/contract/condraft/list' WHERE URL = '/contract/condraft/list' AND ui_version = '1';  -- 合同管理 / 合同起草
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/contract/signed/list' WHERE URL = '/contract/signed/list' AND ui_version = '1';  -- 合同管理 / 合同签署
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/contract/conregister/list' WHERE URL = '/contract/conregister/list' AND ui_version = '1';  -- 合同管理 / 合同登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/contract/conchange/list' WHERE URL = '/contract/conchange/list' AND ui_version = '1';  -- 合同管理 / 合同变更
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/contract/conperformance/list' WHERE URL = '/contract/conperformance/list' AND ui_version = '1';  -- 合同管理 / 合同履行
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/contract/suspend/list' WHERE URL = '/contract/suspend/list' AND ui_version = '1';  -- 合同管理 / 合同终止
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/contract/suspend/queryList' WHERE URL = '/contract/suspend/queryList' AND ui_version = '1';  -- 合同管理 / 合同查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/config/accebillConfig' WHERE URL = '/bill/accebill/config/accebillConfig' AND ui_version = '1';  -- 基础配置 / 承兑汇票参数配置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillbankautodtl/list' WHERE URL = '/bill/accebill/abbillbankautodtl/list' AND ui_version = '1';  -- 基础配置 / 银行票据业务配置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillsignstrategy/list' WHERE URL = '/bill/accebill/abbillsignstrategy/list' AND ui_version = '1';  -- 基础配置 / 电票自动签收配置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillaccbal/balReset' WHERE URL = '/bill/accebill/abbillaccbal/balReset' AND ui_version = '1';  -- 基础配置 / 票据余额重置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/accKeepAmt/list' WHERE URL = '/bill/accebill/accKeepAmt/list' AND ui_version = '1';  -- 基础配置 / 银承账户保底金额配置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/bankInsufficientFundsTimeConfig/list' WHERE URL = '/bill/accebill/bankInsufficientFundsTimeConfig/list' AND ui_version = '1';  -- 基础配置 / 票据银行余额不足时间配置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abemitbill/list' WHERE URL = '/bill/accebill/abemitbill/list' AND ui_version = '1';  -- 应付票据 / 开票登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abEmitBillSupply/list' WHERE URL = '/bill/accebill/abEmitBillSupply/list' AND ui_version = '1';  -- 应付票据 / 开票补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abemitbillback/list' WHERE URL = '/bill/accebill/abemitbillback/list' AND ui_version = '1';  -- 应付票据 / 未用退票
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abemitbillpay/list' WHERE URL = '/bill/accebill/abemitbillpay/list' AND ui_version = '1';  -- 应付票据 / 兑付确认
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abreceivebill/list' WHERE URL = '/bill/accebill/abreceivebill/list' AND ui_version = '1';  -- 应收票据 / 收票登记
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillinventransfer/list' WHERE URL = '/bill/accebill/abbillinventransfer/list' AND ui_version = '1';  -- 应收票据 / 库存管理 / 票据移存
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillinvenfetch/list' WHERE URL = '/bill/accebill/abbillinvenfetch/list' AND ui_version = '1';  -- 应收票据 / 库存管理 / 票据取票
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbilltransferupapply/list' WHERE URL = '/bill/accebill/abbilltransferupapply/list' AND ui_version = '1';  -- 应收票据 / 票据划拨 / 票据上划
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbilltransfercollection/list' WHERE URL = '/bill/accebill/abbilltransfercollection/list' AND ui_version = '1';  -- 应收票据 / 票据划拨 / 票据归集
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbilltransferdownapply/list' WHERE URL = '/bill/accebill/abbilltransferdownapply/list' AND ui_version = '1';  -- 应收票据 / 票据划拨 / 下拨申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbilltransferdownmanage/list' WHERE URL = '/bill/accebill/abbilltransferdownmanage/list' AND ui_version = '1';  -- 应收票据 / 票据划拨 / 票据下拨
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillendorse/list' WHERE URL = '/bill/accebill/abbillendorse/list' AND ui_version = '1';  -- 应收票据 / 票据背书
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abBillDiscountApply/list' WHERE URL = '/bill/accebill/abBillDiscountApply/list' AND ui_version = '1';  -- 应收票据 / 票据贴现 / 贴现申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abBillDiscountManager/list' WHERE URL = '/bill/accebill/abBillDiscountManager/list' AND ui_version = '1';  -- 应收票据 / 票据贴现 / 贴现办理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abBillDiscountAccount/list' WHERE URL = '/bill/accebill/abBillDiscountAccount/list' AND ui_version = '1';  -- 应收票据 / 票据贴现 / 贴现到账
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbilldiscountcollect/list' WHERE URL = '/bill/accebill/abbilldiscountcollect/list' AND ui_version = '1';  -- 应收票据 / 票据贴现 / 贴现收回
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abBillDiscountRelease/list' WHERE URL = '/bill/accebill/abBillDiscountRelease/list' AND ui_version = '1';  -- 应收票据 / 票据贴现 / 贴现额度释放
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbilldiscountransom/list' WHERE URL = '/bill/accebill/abbilldiscountransom/list' AND ui_version = '1';  -- 应收票据 / 票据贴现 / 贴现赎回申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillpledgeapply/list' WHERE URL = '/bill/accebill/abbillpledgeapply/list' AND ui_version = '1';  -- 应收票据 / 票据质押 / 质押申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillpledgerelieve/list' WHERE URL = '/bill/accebill/abbillpledgerelieve/list' AND ui_version = '1';  -- 应收票据 / 票据质押 / 解除质押
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillpledgecollect/list' WHERE URL = '/bill/accebill/abbillpledgecollect/list' AND ui_version = '1';  -- 应收票据 / 票据质押 / 质押收回
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillcollectionapply/list' WHERE URL = '/bill/accebill/abbillcollectionapply/list' AND ui_version = '1';  -- 应收票据 / 票据托收 / 托收申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillcollectionaccount/list' WHERE URL = '/bill/accebill/abbillcollectionaccount/list' AND ui_version = '1';  -- 应收票据 / 票据托收 / 托收到账
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abreceivebillback/list' WHERE URL = '/bill/accebill/abreceivebillback/list' AND ui_version = '1';  -- 应收票据 / 退票申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abReceiveBillLock/list' WHERE URL = '/bill/accebill/abReceiveBillLock/list' AND ui_version = '1';  -- 应收票据 / 票据税分解锁
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillreceivebiz/list' WHERE URL = '/bill/accebill/abbillreceivebiz/list' AND ui_version = '1';  -- 电票业务 / 票据签收
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillSignSupply/list' WHERE URL = '/bill/accebill/abbillSignSupply/list' AND ui_version = '1';  -- 电票业务 / 签收补录
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillGuaranteeApply/list' WHERE URL = '/bill/accebill/abbillGuaranteeApply/list' AND ui_version = '1';  -- 电票业务 / 提示保证
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillAcceptApply/list' WHERE URL = '/bill/accebill/abbillAcceptApply/list' AND ui_version = '1';  -- 电票业务 / 提示承兑
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillReceiveApply/list' WHERE URL = '/bill/accebill/abbillReceiveApply/list' AND ui_version = '1';  -- 电票业务 / 提示收票
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillRecall/list' WHERE URL = '/bill/accebill/abbillRecall/list' AND ui_version = '1';  -- 电票业务 / 通用撤回
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillSearchforApply/list' WHERE URL = '/bill/accebill/abbillSearchforApply/list' AND ui_version = '1';  -- 电票业务 / 追索通知
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/nonassignableCancel/list' WHERE URL = '/bill/accebill/nonassignableCancel/list' AND ui_version = '1';  -- 电票业务 / 不可转让撤销
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillSearchforManage/list' WHERE URL = '/bill/accebill/abbillSearchforManage/list' AND ui_version = '1';  -- 电票业务 / 追索清偿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillInstructionManage/list' WHERE URL = '/bill/accebill/abbillInstructionManage/list' AND ui_version = '1';  -- 电票业务 / 指令处理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillInstructionManage/listTest' WHERE URL = '/bill/accebill/abbillInstructionManage/listTest' AND ui_version = '1';  -- 电票业务 / 指令测试
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillQuery/listQueryBus' WHERE URL = '/bill/accebill/abbillQuery/listQueryBus' AND ui_version = '1';  -- 票据查询 / 承兑汇票业务表
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillTransferDown/listQueryDtl' WHERE URL = '/bill/accebill/abbillTransferDown/listQueryDtl' AND ui_version = '1';  -- 票据查询 / 票据划拨业务表
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillaccbal/list' WHERE URL = '/bill/accebill/abbillaccbal/list' AND ui_version = '1';  -- 票据查询 / 票据账户余额表
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillcorpbal/list' WHERE URL = '/bill/accebill/abbillcorpbal/list' AND ui_version = '1';  -- 票据查询 / 票据单位余额表
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillQuery/listBillPayAccount' WHERE URL = '/bill/accebill/abbillQuery/listBillPayAccount' AND ui_version = '1';  -- 票据查询 / 应付票据台账查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbillQuery/listBillReceAccount' WHERE URL = '/bill/accebill/abbillQuery/listBillReceAccount' AND ui_version = '1';  -- 票据查询 / 应收票据台账查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abbilldelete/list' WHERE URL = '/bill/accebill/abbilldelete/list' AND ui_version = '1';  -- 票据维护 / 票据状态修改
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/claim/rule-config/param' WHERE URL = '/bill/accebillClaim/config/claimParam' AND ui_version = '1';  -- 票据认领 / 规则配置 / 认领参数
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/claim/rule-config/date-rule' WHERE URL = '/bill/accebillClaim/config/dateConfig' AND ui_version = '1';  -- 票据认领 / 规则配置 / 日期规则
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/claim/rule-config/amount-rule' WHERE URL = '/bill/accebillClaim/config/moneyConfig' AND ui_version = '1';  -- 票据认领 / 规则配置 / 金额规则
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/claim/rule-config/ticket-no-rule' WHERE URL = '/bill/accebillClaim/config/ruleBillNo' AND ui_version = '1';  -- 票据认领 / 规则配置 / 票号规则
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/claim/rule-config/account-rule' WHERE URL = '/bill/accebillClaim/config/accountConfig' AND ui_version = '1';  -- 票据认领 / 规则配置 / 账号规则
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/claim/rule-config/flow-remark-rule' WHERE URL = '/bill/accebillClaim/config/ruleBillDtlRmk' AND ui_version = '1';  -- 票据认领 / 规则配置 / 票据流水备注规则
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abClaim/list' WHERE URL = '/bill/accebill/abClaim/list' AND ui_version = '1';  -- 票据认领 / 认领处理
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/abClaim/queryList' WHERE URL = '/bill/accebill/abClaim/queryList' AND ui_version = '1';  -- 票据认领 / 认领查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/intellect/element/list' WHERE URL = '/bill/intellect/element/list' AND ui_version = '1';  -- 智能选票 / 选票要素
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/intellect/rule/list' WHERE URL = '/bill/intellect/rule/list' AND ui_version = '1';  -- 智能选票 / 选票规则
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/intellect/ruleGroup/list' WHERE URL = '/bill/intellect/ruleGroup/list' AND ui_version = '1';  -- 智能选票 / 选票规则组
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/endorseRequest/list' WHERE URL = '/bill/accebill/endorseRequest/list' AND ui_version = '1';  -- 背书请求 / 背书请求
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/accebill/endorseResponse/list' WHERE URL = '/bill/accebill/endorseResponse/list' AND ui_version = '1';  -- 背书请求 / 背书请求应答
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billpool/poolif/list' WHERE URL = '/billpool/poolif/list' AND ui_version = '1';  -- 集团票据池 / 基础设置 / 池接口
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/abpool/pledgee/list' WHERE URL = '/abpool/pledgee/list' AND ui_version = '1';  -- 集团票据池 / 基础设置 / 质押人设置
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billpool/pool/list' WHERE URL = '/billpool/pool/list' AND ui_version = '1';  -- 集团票据池 / 基础设置 / 票据池
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/abpool/bustype/list' WHERE URL = '/abpool/bustype/list' AND ui_version = '1';  -- 集团票据池 / 基础设置 / 业务类型
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billPool/corp/list' WHERE URL = '/billPool/corp/list' AND ui_version = '1';  -- 集团票据池 / 基础设置 / 票据池单位
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/abpool/limitApply/list' WHERE URL = '/abpool/limitApply/list' AND ui_version = '1';  -- 集团票据池 / 额度管理 / 额度申领
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billPool/applyCheck/list' WHERE URL = '/billPool/applyCheck/list' AND ui_version = '1';  -- 集团票据池 / 额度管理 / 额度申领核准
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billpool/limitAllot/list' WHERE URL = '/billpool/limitAllot/list' AND ui_version = '1';  -- 集团票据池 / 额度管理 / 额度分配
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billpool/limitAllotCheck/list' WHERE URL = '/billpool/limitAllotCheck/list' AND ui_version = '1';  -- 集团票据池 / 额度管理 / 额度分配核准
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/deposit-manage/deposit-in' WHERE URL = '/billPool/bail/inList' AND ui_version = '1';  -- 集团票据池 / 保证金管理 / 保证金存入
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/deposit-manage/deposit-out' WHERE URL = '/billPool/bail/outList' AND ui_version = '1';  -- 集团票据池 / 保证金管理 / 保证金提取
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/pool-manage/pool-in-apply' WHERE URL = '/billPool/intoApply/list' AND ui_version = '1';  -- 集团票据池 / 池管理 / 入池申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/pool-manage/pool-out-apply' WHERE URL = '/billPool/outApply/list' AND ui_version = '1';  -- 集团票据池 / 池管理 / 出池申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/pool-manage/pledge-trust-convert' WHERE URL = '/billPool/pdtst/list' AND ui_version = '1';  -- 集团票据池 / 池管理 / 质押托管转换
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/pool-manage/pool-in-cancel' WHERE URL = '/billPool/intoRepeal/list' AND ui_version = '1';  -- 集团票据池 / 池管理 / 入池撤销
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/fin-apply-manage/bill-fin-apply' WHERE URL = '/billpool/emitFinaApply/list' AND ui_version = '1';  -- 集团票据池 / 融资申请管理 / 出票融资申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/pool-contract-info' WHERE URL = '/billPool/signInfo/listQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 票据池签约信息
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/pool-quota-info' WHERE URL = '/billPool/limitInfo/listQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 票据池额度信息
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/in-out-pool-record' WHERE URL = '/billPool/dtlBook/listInoutQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 出入池登记簿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/pledge-custody-convert-record' WHERE URL = '/billPool/dtlBook/listPdTstQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 质押托管转换登记簿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/issue-finance-apply-record' WHERE URL = '/billPool/finaApplyBook/list' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 出票融资申请登记簿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/issue-apply-record' WHERE URL = '/billPool/dtlBook/listEmitQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 出票申请登记簿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/discount-apply-record' WHERE URL = '/billPool/dtlBook/listDiscountQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 贴现申请登记簿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/endorse-apply-record' WHERE URL = '/billPool/dtlBook/listEndorseQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 背书申请登记簿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/present-pay-record' WHERE URL = '/billPool/dtlBook/listPaymentQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 提示付款登记簿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/in-pool-record' WHERE URL = '/billPool/holdBook/listQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 在池登记簿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/margin-trade-record' WHERE URL = '/billPool/bailBook/listQuery' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 保证金交易登记簿
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/bill/groupBillPool/query-manage/quota-adjust-query' WHERE URL = '/billpool/limitAllot/queryList' AND ui_version = '1';  -- 集团票据池 / 查询管理 / 额度调剂查询
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billPool/abSign/list' WHERE URL = '/billPool/abSign/list' AND ui_version = '1';  -- 集团票据池 / 票据管理 / 电票签收
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billPool/discount/list' WHERE URL = '/billPool/discount/list' AND ui_version = '1';  -- 集团票据池 / 票据管理 / 贴现申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billPool/endorse/list' WHERE URL = '/billPool/endorse/list' AND ui_version = '1';  -- 集团票据池 / 票据管理 / 背书申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billpool/emitApply/list' WHERE URL = '/billpool/emitApply/list' AND ui_version = '1';  -- 集团票据池 / 票据管理 / 出票申请
UPDATE tenant_pre_menu SET URL = '/sub-finance/#/billpool/collection/list' WHERE URL = '/billpool/collection/list' AND ui_version = '1';  -- 集团票据池 / 票据管理 / 提示付款


-- 修改人：zmc
-- 修改时间：2026-08-30
-- 修改内容：签约客户配额字段及单租户模式初始化数据（Oracle）

-- 签约客户管理增加单位、用户、银行账号数量
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'SYS_TENANT_MANAGER'
  AND COLUMN_NAME = 'CORP_NUMBER';
IF v_COUNT < 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE SYS_TENANT_MANAGER ADD (CORP_NUMBER NUMBER(10) DEFAULT 100 NOT NULL)';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN SYS_TENANT_MANAGER.CORP_NUMBER IS ''单位数量''';
END IF;

SELECT COUNT(*) INTO v_COUNT
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'SYS_TENANT_MANAGER'
  AND COLUMN_NAME = 'USER_NUMBER';
IF v_COUNT < 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE SYS_TENANT_MANAGER ADD (USER_NUMBER NUMBER(10) DEFAULT 100 NOT NULL)';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN SYS_TENANT_MANAGER.USER_NUMBER IS ''用户数量''';
END IF;

SELECT COUNT(*) INTO v_COUNT
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'SYS_TENANT_MANAGER'
  AND COLUMN_NAME = 'ACCOUNT_NUMBER';
IF v_COUNT < 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE SYS_TENANT_MANAGER ADD (ACCOUNT_NUMBER NUMBER(10) DEFAULT 100 NOT NULL)';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN SYS_TENANT_MANAGER.ACCOUNT_NUMBER IS ''银行账号数量''';
END IF;
END;
/

-- 签约客户记录管理增加单位、用户、银行账号数量
DECLARE
v_COUNT NUMBER;
BEGIN
SELECT COUNT(*) INTO v_COUNT
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'SYS_TENANT_RECORD_MANAGER'
  AND COLUMN_NAME = 'CORP_NUMBER';
IF v_COUNT < 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE SYS_TENANT_RECORD_MANAGER ADD (CORP_NUMBER NUMBER(10) DEFAULT 100 NOT NULL)';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN SYS_TENANT_RECORD_MANAGER.CORP_NUMBER IS ''单位数量''';
END IF;

SELECT COUNT(*) INTO v_COUNT
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'SYS_TENANT_RECORD_MANAGER'
  AND COLUMN_NAME = 'USER_NUMBER';
IF v_COUNT < 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE SYS_TENANT_RECORD_MANAGER ADD (USER_NUMBER NUMBER(10) DEFAULT 100 NOT NULL)';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN SYS_TENANT_RECORD_MANAGER.USER_NUMBER IS ''用户数量''';
END IF;

SELECT COUNT(*) INTO v_COUNT
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'SYS_TENANT_RECORD_MANAGER'
  AND COLUMN_NAME = 'ACCOUNT_NUMBER';
IF v_COUNT < 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE SYS_TENANT_RECORD_MANAGER ADD (ACCOUNT_NUMBER NUMBER(10) DEFAULT 100 NOT NULL)';
EXECUTE IMMEDIATE 'COMMENT ON COLUMN SYS_TENANT_RECORD_MANAGER.ACCOUNT_NUMBER IS ''银行账号数量''';
END IF;
END;
/


INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'c8a1b2d3e4f5467a8b9c0d1e2f304152', 'zh_CN', '该数据暂无发行信息', 'common.noData' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang
    WHERE LANG_KEY = 'common.noData' AND LANG_CODE = 'zh_CN'
);

INSERT INTO sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
SELECT 'd9b2c3e4f506578b9c0d1e2f30426363', 'en_US', 'This record has no issue information', 'common.noData' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_multi_lang
    WHERE LANG_KEY = 'common.noData' AND LANG_CODE = 'en_US'
);
commit;
/

update TENANT_PRE_MENU set PARENT_ID='b132ba019e2a4139815203b96b3de76c',SORT='3' where id='5b25274f401a4345894539c06cb89b71';
update TENANT_PRE_MENU set PARENT_ID='b132ba019e2a4139815203b96b3de76cv1',SORT='3' where id='5b25274f401a4345894539c06cb89b71v1';
##GOGO