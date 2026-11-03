package com.bytter.guarantee.constant;
import java.io.Serializable;
@SuppressWarnings("serial")
/**
 * 担保模块常量类
 */
public class GuaranteeConstant implements Serializable {
    /**
     * 担保额度类型 一次额度
     */
    public final static String GUARANTOR_MONEY_TYPE_1 = "1";
    /**
     * 担保额度类型 一次额度
     */
    public final static String GUARANTOR_MONEY_TYPE_2 = "2";
    /**
     * 抵质押物编号前缀
     */
    public static final String GUA_PLEDGE_CODE = "DZY";
    /**
     * 保函更改编号前缀
     */
    public static final String GUA_GUARANTEE_CHANGE_CODE = "LGC";
    /**
     * 申请状态 -2 删除 ,-1 打回,0 暂存,-3 领取失败, 11 待审批 ,95审批通过,101 完成更改,102 拒绝更改,103 更改中,100 注销,104 终止中,105 已终止,106 终止打回
     */
    public static final Integer STATUS_DELETE = -2;
    public static final Integer STATUS_BACK = -1;
    public static final Integer STATUS_FAIL = -3;
    public static final Integer STATUS_TEMP = 0;
    public static final Integer STATUS_WAIT_CHECK = 11;
    public static final Integer STATUS_CHECK = 95;
    public static final Integer STATUS_CHANGE_CHECK = 101;
    public static final Integer STATUS_CHANGE_REFUSE = 102;
    public static final Integer STATUS_CHANGEING = 103;
    public static final Integer STATUS_LOGOUT = 100;
    public static final Integer STATUS_ENDING = 104;
    public static final Integer STATUS_END = 105;
    public static final Integer STATUS_END_BACK = 106;
    /**
     * 担保方式 0 抵押，1 质押，2 保证
     */
    public static final String GUA_TYPE_DY = "0";
    public static final String GUA_TYPE_ZY = "1";
    public static final String GUA_TYPE_BZ = "2";
    /**
     * 类型 1-释放 2-占用
     */
    public static final String GUA_TRACK_SF = "1";
    public static final String GUA_TRACK_ZY = "2";
    /**
     * 是否已变更 0-未变更 1-已变更
     */
    public static final String GUA_NO_CHANGE = "0";
    public static final String GUA_YES_CHANGE = "1";
    /** 以下是担保管理的相关权限 */
    /**
     * 抵质押: 查看权限
     */
    public final static String GUA_PLEDGE_INFO_QUERY = "gua:pledge:info:query";
    /**
     * 抵质押：增删改权限
     */
    public final static String GUA_PLEDGE_INFO_APPLY = "gua:pledge:info:apply";
    /**
     * 抵质押变更：查询权限
     */
    public final static String GUA_PLEDGE_CHANGE_QUERY = "gua:pledge:change:query";
    /**
     * 抵质押变更：应用权限
     */
    public final static String GUA_PLEDGE_CHANGE_APPLY = "gua:pledge:change:apply";
    /**
     * 担保合同登记: 查看权限
     */
    public final static String GUA_CONTRACT_INFO_QUERY = "gua:contract:info:query";
    /**
     * 担保合同登记: 应用权限
     */
    public final static String GUA_CONTRACT_INFO_APPLY = "gua:contract:info:apply";
    /**
     * 担保合同变更: 查看权限
     */
    public final static String GUA_CONTRACT_CHANGE_QUERY = "gua:contract:change:query";
    /**
     * 担保合同变更: 应用权限
     */
    public final static String GUA_CONTRACT_CHANGE_APPLY = "gua:contract:change:apply";
    /**
     * 担保合同变更: 应用权限
     */
    public final static String GUA_CONTRACT_BUSINESS_QUERY = "gua:contract:business:query";
    /**
     * 被担保合同使用登记: 查看权限
     */
    public final static String GUA_CONTRACT_USE_QUERY = "gua:contract:use:query";
    /**
     * 被担保合同使用登记: 应用权限
     */
    public final static String GUA_CONTRACT_USE_APPLY = "gua:contract:use:apply";
    /**
     * 对外担保合同登记: 查看权限
     */
    public final static String GUA_EXTCONTRACT_INFO_QUERY = "gua:extContract:info:query";
    /**
     * 对外担保合同登记: 应用权限
     */
    public final static String GUA_EXTCONTRACT_INFO_APPLY = "gua:extContract:info:apply";
    /**
     * 对外担保合同变更: 查看权限
     */
    public final static String GUA_EXTCONTRACT_CHANGE_QUERY = "gua:extContract:change:query";
    /**
     * 对外担保合同变更: 应用权限
     */
    public final static String GUA_EXTCONTRACT_CHANGE_APPLY = "gua:extContract:change:apply";
    /**
     * 对外担保合同终止: 查看权限
     */
    public final static String GUA_EXTCONTRACT_END_QUERY = "gua:extContract:end:query";
    /**
     * 对外担保合同终止: 应用权限
     */
    public final static String GUA_EXTCONTRACT_END_APPLY = "gua:extContract:end:apply";
    /**
     * 对外担保合同查询: 查询权限
     */
    public final static String GUA_EXTCONTRACT_SEARCH_QUERY = "gua:extContract:search:query";
    /**
     * 保函申请: 查询权限
     */
    public final static String GUA_GUARANTEE_INFO_QUERY = "gua:guarantee:info:query";
    /**
     * 保函申请: 操作权限
     */
    public final static String GUA_GUARANTEE_INFO_APPLY = "gua:guarantee:info:apply";
    /**
     * 保函领取:领取权限
     */
    public final static String GUA_GUARANTEE_INFO_RECEIVE = "gua:guarantee:info:receive";
    /**
     * 保函履行登记:登记权限
     */
    public final static String GUA_GUARANTEE_INFO_REGISTER = "gua:guarantee:info:register";
    /**
     * 保函更改申请：查询权限
     */
    public final static String GUA_GUARANTEE_CHANGE_QUERY = "gua:guarantee:change:query";
    /**
     * 保函更改申请：应用权限
     */
    public final static String GUA_GUARANTEE_CHANGE_APPLY = "gua:guarantee:change:apply";
    /**
     * 保函更改办理:查询权限
     */
    public final static String GUA_GUARANTEE_CHANGEHANDLE_QUERY = "gua:guarantee:changehandle:query";
    /**
     * 保函更改办理:应用权限
     */
    public final static String GUA_GUARANTEE_CHANGEHANDLE_APPLY = "gua:guarantee:changehandle:apply";
    /**
     * 保函: 注销权限
     */
    public final static String GUA_GUARANTEE_INFO_LOGOUT = "gua:guarantee:info:logout";
    /**
     * 对外担保合同还款计划: 列表查看权限
     */
    public final static String GUA_CONTRACT_INFOPLAN_QUERY = "gua:contract:infoplan:query";
    /**
     * 消息类型
     */
    /**
     * 消息类型：0-暂存
     */
    public final static String MESSAGE_TYPE_TEMP = "0";
    /**
     * 消息类型：1-提交
     */
    public final static String MESSAGE_TYPE_SUBMIT = "1";
    /**
     * 消息类型：2-录入
     */
    public final static String MESSAGE_TYPE_ADD = "2";
    /**
     * 消息类型：3-删除
     */
    public final static String MESSAGE_TYPE_DELETE = "3";
    /**
     * 消息类型：4-审批打回
     */
    public final static String MESSAGE_TYPE_CHECK_BACK = "4";
    /**
     * 消息类型：5-审批通过
     */
    public final static String MESSAGE_TYPE_CHECK_PASS = "5";
    /**
     * 消息类型：6-编辑
     */
    public final static String MESSAGE_TYPE_UPDATE = "6";
    /**
     * 消息类型：7-变更
     */
    public final static String MESSAGE_TYPE_CHANGE = "7";
    /**
     * 消息类型：9-注销
     */
    public final static String MESSAGE_TYPE_LOGOUT = "9";
    /**
     * 操作信息
     */
    public static final String OP_MSG_INPUT_SUBMIT = "录入提交";
    public static final String OP_MSG_INPUT_TEMP = "录入暂存";
    public static final String OP_MSG_UPDATE_SUBMIT = "修改提交";
    public static final String OP_MSG_UPDATE_TEMP = "修改暂存";
    public static final String OP_MSG_CHECK_SUCCESS = "审批通过";
    public static final String OP_MSG_CHECK_BACK = "审批打回";
    public static final String OP_MSG_DELETE = "删除";
    public static final String OP_MSG_CHANGE = "变更";
    public static final String OP_MSG_LOGOUT = "注销";
    /**
     * 保函相关常量
     */
    /**
     * 保函领取完成
     */
    public static final Integer GUARANTEE_RECEIVED = 96;
    /**
     * 履行中
     */
    public static final Integer GUARANTEE_REGISTERED = 97;
    /**
     * 履行完成
     */
    public static final Integer GUARANTEE_REGISFINISH = 98;
    /**
     * 担保合同状态
     */
    /**
     * 暂存
     */
    public static final Integer GUA_CONTRACT_TEMP = 0;
    /**
     * 在审使用
     */
    public static final Integer GUA_CONTRACT_APPROVED = 1;
    /**
     * 使用中
     */
    public static final Integer GUA_CONTRACT_USED = 2;
    /**
     * 释放
     */
    public static final Integer GUA_CONTRACT_RELEASE = 3;
    /**
     * 担保模块公用是否标识-是
     */
    public static final String GUA_COMMON_Y = "1";
    /**
     * 担保模块公用是否标识-否
     */
    public static final String GUA_COMMON_N = "0";
    /**
     * 对接合同管理：是
     */
    public final static String RELATE_CON_YES = "1";
    /**
     * 对接合同管理：否
     */
    public final static String RELATE_CON_NO = "0";
    /**
     * 对接合同管理模式：业务选择合同
     */
    public final static String CON_TYPE_SELECT = "1";
    /**
     * 对接合同管理模式：业务生成合同
     */
    public final static String CON_TYPE_CREATE = "0";
    /**
     * 对接合同管理：合同名称
     */
    public final static String CON_NAME = "担保合同";
    /**
     * 是否集团内担保单位：是
     */
    public static final String INNER_GUA_Y = "1";
    /**
     * 是否集团内担保单位：否
     */
    public static final String INNER_GUA_N = "0";
    /**
     * 数据类型：外部模块挂接
     */
    public static final String DATA_TYPE_0 = "0";
    /**
     * 数据类型：使用登记
     */
    public static final String DATA_TYPE_1 = "1";
    /**
     * 担保合同类型：被担保合同
     */
    public static final String CON_TYPE_0 = "0";
    /**
     * 担保合同类型：对外担保合同
     */
    public static final String CON_TYPE_1 = "1";
    /**
     * 抵质押物占用状态
     */
    // /**暂存*/
    // public static final Integer GUA_PLEDGE_TEMP=0;
    /**
     * 冻结
     */
    public static final Integer GUA_PLEDGE_FROZEN = 1;
    /**
     * 使用中
     */
    public static final Integer GUA_PLEDGE_OCCUPY = 2;
    /**
     * 释放
     */
    public static final Integer GUA_PLEDGE_RELEASE = 3;
    /**
     * 被担保合同登记
     */
    /**
     * 融资主体不能为空！
     */
    public final static String CMS_GUACONTRACT_CORP_ISNULL = "cms.guacontract.corp.isNull";
    /**
     * 请输入正确的融资主体！
     */
    public final static String CMS_GUACONTRACT_CORP_ERROR = "cms.guacontract.corp.error";
    /**
     * 担保编号不能为空！
     */
    public final static String CMS_GUACONTRACT_GUANO_ISNULL = "cms.guacontract.guaNo.isNull";
    /**
     * 币种不能为空！
     */
    public final static String CMS_GUACONTRACT_CUR_ISNULL = "cms.guacontract.cur.isNull";
    /**
     * 币种不存在！
     */
    public final static String CMS_GUACONTRACT_CUR_ERROR = "cms.guacontract.cur.error";
    /**
     * 担保开始日期不能为空！
     */
    public final static String CMS_GUACONTRACT_STARTDATE_ISNULL = "cms.guacontract.startDate.isNull";
    /**
     * 担保结束日期不能为空！
     */
    public final static String CMS_GUACONTRACT_ENDDATE_ISNULL = "cms.guacontract.endDate.isNull";
    /**
     * 担保结束日期不能大于担保开始日期！
     */
    public final static String CMS_GUACONTRACT_DATE_ERROR = "cms.guacontract.date.error";
    /**
     * 债务人不能为空！
     */
    public final static String CMS_GUACONTRACT_DEBTORPERSON_ISNULL = "cms.guacontract.debtorPerson.isNull";
    /**
     * 债权人不能为空！
     */
    public final static String CMS_GUACONTRACT_CREDITORPERSON_ISNULL = "cms.guacontract.creditorPerson.isNull";
    /**
     * 是否集团内担保不能为空！
     */
    public final static String CMS_GUACONTRACT_INNERGUA_ISNULL = "cms.guacontract.innerGua.isNull";
    /**
     * 是否集团内担保格式错误！
     */
    public final static String CMS_GUACONTRACT_INNERGUA_ERROR = "cms.guacontract.innerGua.error";
    /**
     * 担保单位不能为空！
     */
    public final static String CMS_GUACONTRACT_GUAPERSON_ISNULL = "cms.guacontract.guaPerson.isNull";
    /**
     * 集团内担保担保单位不存在！
     */
    public final static String CMS_GUACONTRACT_GUAPERSON_ERROR = "cms.guacontract.guaPerson.error";
    /**
     * 合同编号不能为空！
     */
    public final static String CMS_GUACONTRACT_CONTRACTCODE_ISNULL = "cms.guacontract.contractCode.isNull";
    /**
     * 抵质押物导入
     */
    /**
     * 抵质押申请单位不能为空！
     */
    public final static String CMS_GUAPLEDGE_CORP_ISNULL = "cms.guaPledge.corp.isNull";
    /**
     * 抵质押申请单位不存在！
     */
    public final static String CMS_GUAPLEDGE_CORP_ERROR = "cms.guaPledge.corp.error";
    /**
     * 抵质押物性质不能为空！
     */
    public final static String CMS_GUAPLEDGE_ATTR_ISNULL = "cms.guaPledge.attr.isNull";
    /**
     * 抵质押物性质不存在！
     */
    public final static String CMS_GUAPLEDGE_ATTR_ERROR = "cms.guaPledge.attr.error";
    /**
     * 抵质押开始日期不能为空！
     */
    public final static String CMS_GUAPLEDGE_STARTDATE_ISNULL = "cms.guaPledge.startDate.isNull";
    /**
     * 抵质押结束日期不能为空！
     */
    public final static String CMS_GUAPLEDGE_ENDDATE_ISNULL = "cms.guaPledge.endDate.isNull";
    /**
     * 抵质押开始日期不能大于抵质押结束日期！
     */
    public final static String CMS_GUAPLEDGE_DATE_ERROR = "cms.guaPledge.date.error";
    /**
     * 抵质押物类型不能为空！
     */
    public final static String CMS_GUAPLEDGE_TYPE_ISNULL = "cms.guaPledge.type.isNull";
    /**
     * 此性质抵质押物类型不存在！
     */
    public final static String CMS_GUAPLEDGE_TYPE_ERROR = "cms.guaPledge.type.error";
    /**
     * 抵质押物名称不能为空！
     */
    public final static String CMS_GUAPLEDGE_NAME_ISNULL = "cms.guaPledge.name.isNull";
    /**
     * 抵(质)押权人不能为空！
     */
    public final static String CMS_GUAPLEDGE_ORG_ISNULL = "cms.guaPledge.org.isNull";
    /**
     * 评估值不能为空！
     */
    public final static String CMS_GUAPLEDGE_MONEY_ISNULL = "cms.guaPledge.money.isNull";
    /**
     * 市场价值不能为空！
     */
    public final static String CMS_GUAPLEDGE_NETWORTH_ISNULL = "cms.guaPledge.netWorth.isNull";
    /**
     * 填报单位不存在！
     */
    public final static String CMS_GUAPLEDGE_REGCORP_ERROR = "cms.guaPledge.regCorp.error";
    /**
     * 被担保对接合同管理
     */
    public final static String GUA_RELATE_CON = "gua_relate_con";
    /**
     * 被担保对接合同管理模式
     */
    public final static String GUA_CON_TYPE = "gua_relate_con_type";
    /**
     * 对外担保对接合同管理
     */
    public final static String EXTCON_RELATE_CON = "extCon_relate_con";
    /**
     * 对外担保对接合同管理模式
     */
    public final static String EXTCON_CON_TYPE = "extCon_relate_con_type";
    /**
     * 对外担保对接合同管理模式
     */
    public final static String AUTO_EXT_GUA_CON = "autoExtGuaCon";
    /**
     * 对外担保合同到期提醒天数
     */
    public final static String EXTCON_REMIND_DAY = "extCon_remind_day";
    /**
     * 被担保合同到期提醒天数
     */
    public final static String COVERCON_REMIND_DAY = "coverCon_remind_day";
    /**
     * 提交
     */
    public final static int MSGA_SUBMIT = 20;
    /**
     * 录入暂存
     */
    public final static int MSGA_INPUT = 21;
    /**
     * 修改暂存
     */
    public final static int MSGA_MODIFY = 22;
    /**
     * 录入提交
     */
    public final static String MSGA_INPUT_SUBMIT = "49";
    /**
     * 补录提交
     */
    public final static String MSGA_REPAIR_SUBMIT = "50";
    /**
     * 修改提交
     */
    public final static int MSGA_REPAIR_MODIFY = 32;
    /**
     * 抵押是否允许多次挂接
     */
    public final static String MANY_TIMES = "manyTimes";
    /**
     * 审批通过
     */
    public final static int MSGA_CHECKOK = 24;
    /**
     * 审批打回
     */
    public final static String MSGA_CHECKBACK = "72";
    /**
     * 注销
     */
    public final static String MSGA_LOGOUT = "73";
    /**
     * 变更
     */
    public final static String MSGA_CHANGE = "74";
    /* 预计还款日不能为空！ */
    public static final String CMS_REPAYMENTSCHEDULE_PREDICEREPAYDATE_ISNULL = "cms.repaymentschedule.prediceRepayDate.isnull";
    /* 还款本金不能为空！ */
    public static final String CMS_REPAYMENTSCHEDULE_REPAYMENTMONEY_ISNULL = "cms.repaymentschedule.repaymentMoney.isnull";
    /* 还款利息不能为空！ */
    public static final String CMS_REPAYMENTSCHEDULE_REPAYMENTIST_ISNULL = "cms.repaymentschedule.repaymentIst.isnull";
    /* 是否已还标识不能为空 */
    public static final String CMS_REPAYMENTSCHEDULE_ISREPAY_ISNULL = "cms.repaymentschedule.isRepay.isnull";
    /* 是否已还标识输入值错误 */
    public static final String CMS_REPAYMENTSCHEDULE_ISREPAY_ERROR = "cms.repaymentschedule.isRepay.error";
    /* 自动还款 */
    public static final String TABLE_CMSPROVIDELOANINFO_AUTOPAY = "table.cmsProvideLoanInfo.AutoPay";
    /**
     * 被担保合同变更应用权限
     */
    public static final String CMS_GUARANTEE_CHANGE_APPLY = "cms:guarantee:change:apply";
    /**
     * 被担保合同便跟查询权限
     */
    public static final String CMS_GUARANTEE_CHANGE_QUERY = "cms:guarantee:change:query";
    /**
     * 保函到期提醒天数
     */
    public final static String GUA_GUARANTEE_NOTIFYDAYS = "notify_Days";
    /**
     * 保函是否挂接手续
     */
    public final static String GUA_GUARANTEE_RAT_CONTAINS = "rat_Contains";
    /**
     * 系统编码：内部贷款->loan
     */
    public final static String SYS_CODE_LOAN = "loan";
    /**
     * 系统编码：授信管理->rat
     */
    public final static String SYS_CODE_RAT = "rat";
    /**
     * 系统编码：票据管理->accebill
     */
    public final static String SYS_CODE_ACC_EBILL = "accebill";
    /**
     * 系统编码：银行借款->provide
     */
    public final static String SYS_CODE_PROVIDE = "provide";
    /**
     * 系统编码：国际结算->lcs
     */
    public final static String SYS_CODE_LCS = "lcs";
    /**
     * 系统编码：内部拆借->inl
     */
    public final static String SYS_CODE_INL = "inl";
}
