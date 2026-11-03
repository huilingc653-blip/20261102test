package com.bytter.core.constant;
import java.io.Serializable;
/**
 * 授信模块常量类
 *
 * @author liyh
 */
public class RationConstant implements Serializable {
    /**
     * 授信种类-银行授信
     */
    public static final String RAT_TYPE_0 = "0";
    /**
     * 授信种类-企业授信
     */
    public static final String RAT_TYPE_1 = "1";
    /**
     * 综合授信标识-单品种
     */
    public static final String RAT_COMFLAG_0 = "0";
    /**
     * 综合授信标识-综合品种
     */
    public static final String RAT_COMFLAG_1 = "1";
    /**
     * 额度包含或者交叉允许选择-否
     */
    public static final String RAT_CONTAIN_0 = "0";
    /**
     * 额度包含或者交叉允许选择-是
     */
    public static final String RAT_CONTAIN_1 = "1";
    /**
     * 授信模式-集团授信
     */
    public static final String RATMODE_0 = "0";
    /**
     * 授信模式-自主授信
     */
    public static final String RATMODE_1 = "1";
    /**
     * 敞口授信-否
     */
    public static final String OPEN_SIGN_0 = "0";
    /**
     * 敞口授信-是
     */
    public static final String OPEN_SIGN_1 = "1";
    /**
     * 二次授信对象控制模式-组织架构
     */
    public static final String RATCTRMODEL_CORP = "0";
    /**
     * 二次授信对象控制模式-权限
     */
    public static final String RATCTRMODEL_RESOURCE = "1";
    /**
     * 集团授信主体-否
     */
    public static final String RAT_GROUP_0 = "0";
    /**
     * 集团授信主体-是
     */
    public static final String RAT_GROUP_1 = "1";
    /**
     * 授信分配方式-自用
     */
    public static final String DIS_TYPE_OWN = "1";
    /**
     * 授信分配方式-总额共享
     */
    public static final String DIS_TYPE_AMOUNT_SHARE = "2";
    /**
     * 授信分配方式-金额共享
     */
    public static final String DIS_TYPE_MONEY_SHARE = "3";
    /**
     * 授信分配方式-自动分配自主授信
     */
    public static final String DIS_TYPE_AUTO = "4";
    /**
     * 授信录入前缀
     */
    public static final String RAT_BKRATION_CODE = "RBK";
    /**
     * 授信分配前缀
     */
    public static final String RAT_BKRATION_DIS_CODE = "DRB";
    /**
     * 授信追加前缀
     */
    public static final String RAT_BKRATION_ADD_CODE = "DBA";
    /**
     * 授信调剂前缀
     */
    public static final String RAT_BKRATION_REF_CODE = "DRE";
    /**
     * 授信占用申请前缀
     */
    public static final String RAT_URATION_APPLY_CODE = "RUA";
    /**
     * 授信占用台账前缀
     */
    public static final String RAT_URATION_USE_CODE = "RUS";
    /**
     * 授信调整前缀
     */
    public static final String RAT_BKRATION_ADJUST_CODE = "DRU";
    /**
     * 内部授信录入前缀
     */
    public static final String RAT_CPRATION_CODE = "RCP";
    /**
     * 内部授信追加前缀
     */
    public static final String RAT_CPRATION_ADD_CODE = "RCA";
    /**
     * 内部授信分配前缀
     */
    public static final String RAT_CPRATION_DIS_CODE = "RCD";
    /**
     * 内部授信占用申请前缀
     */
    public static final String RAT_CPRATION_APPLY_CODE = "RUA";
    /**
     * 内部授信分配方式-自动分配
     */
    public static final String DIS_WAY_AUTO = "5";
    /**
     * 内部授信分配方式-手动分配
     */
    public static final String DIS_WAY_MANUAL = "6";
    /**
     * 综合授信使用品种标识-是
     */
    public static final String RAT_COMP_SIGN_1 = "1";
    /**
     * 综合授信使用品种标识-否
     */
    public static final String RAT_COMP_SIGN_0 = "0";
    /**
     * 授信模块公用是否标识-是
     */
    public static final String RAT_COMMON_Y = "1";
    /**
     * 授信模块公用是否标识-否
     */
    public static final String RAT_COMMON_N = "0";
    /**
     * 追加类型-银行追加
     */
    public static final String RAT_ADD_FLAG_BANK = "0";
    /**
     * 追加类型-集团追加
     */
    public static final String RAT_ADD_FLAG_GROUP = "1";
    /**
     * 追加类型-内部额度追加
     */
    public static final String RAT_ADD_FLAG_COMPANY = "2";
    /**
     * 授信所有分配方式组合-只有自用
     */
    public static final String RAT_DISWAY_1 = "1";
    /**
     * 授信所有分配方式组合-只有成员单位的总额共享
     */
    public static final String RAT_DISWAY_2 = "2";
    /**
     * 授信所有分配方式组合-只有金额共享
     */
    public static final String RAT_DISWAY_3 = "3";
    /**
     * 授信所有分配方式组合-只有总额共享-中心本身（授信主体分配了总额共享，以授信主体为调剂对象）
     */
    public static final String RAT_DISWAY_4 = "4";
    /**
     * 授信所有分配方式组合-金额共享+自用
     */
    public static final String RAT_DISWAY_5 = "5";
    /**
     * 授信所有分配方式组合-金额共享+总额共享（授信主体）
     */
    public static final String RAT_DISWAY_6 = "6";
    /**
     * 授信所有分配方式组合-金额共享+总额共享（成员单位）
     */
    public static final String RAT_DISWAY_7 = "7";
    /**
     * 授信调剂类型-调剂方
     */
    public static final String RAT_REF_TYPE_0 = "0";
    /**
     * 授信调剂类型-被调剂方
     */
    public static final String RAT_REF_TYPE_1 = "1";
    /**
     * 授信占用类型-占用申请
     */
    public static final String RAT_USE_TYPE_0 = "0";
    /**
     * 授信占用类型-占用业务表
     */
    public static final String RAT_USE_TYPE_1 = "1";
    /**
     * 授信占用业务名称-企业台账
     */
    public static final String BUSINESS_NAME_01 = "01";
    /**
     * 授信占用业务名称-信贷管理
     */
    public static final String BUSINESS_NAME_02 = "02";
    /**
     * 授信占用业务名称-外部融资台账
     */
    public static final String BUSINESS_NAME_03 = "03";
    /**
     * 授信占用业务名称-承兑汇票
     */
    public static final String BUSINESS_NAME_04 = "04";
    /**
     * 授信占用业务名称-银行台账
     */
    public static final String BUSINESS_NAME_05 = "05";
    /**
     * 授信占用业务名称-国际信用证
     */
    public static final String BUSINESS_NAME_09 = "09";
    /**
     * 授信占用业务名称-保函管理
     */
    public static final String BUSINESS_NAME_08 = "08";
    /**
     * 授信占用业务名称-内部拆借占用
     */
    public static final String BUSINESS_NAME_10 = "10";
    /**
     * 0 录入暂存占用；1 提交申请后冻结；2 额度已占用；3 额度已释放,4-结转成功的旧占用数据
     * 授信占用状态-0 录入暂存占用
     */
    public static final Integer RAT_USE_STATUS_TEMP = 0;
    /**
     * 授信占用状态-1 提交申请后冻结
     */
    public static final Integer RAT_USE_STATUS_FREEZE = 1;
    /**
     * 授信占用状态-2 额度已占用
     */
    public static final Integer RAT_USE_STATUS_USE = 2;
    /**
     * 授信占用状态-3 额度已释放
     */
    public static final Integer RAT_USE_STATUS_RELEASE = 3;
    /**
     * 授信占用状态-4 结转成功的旧占用数据
     */
    public static final Integer RAT_USE_STATUS_CLOSE = 4;
    /**
     * 授信占用申请状态
     * -2 删除 ,-1 打回,0 暂存, 11 待审批 ,95审批通过, 96 占用释放，97 占用历史,98 审批通过可用,-3作废,-5关闭
     */
    public static final Integer STATUS_DELETE = -2;
    public static final Integer STATUS_BACK = -1;
    public static final Integer STATUS_TEMP = 0;
    public static final Integer STATUS_WAIT_CHECK = 11;
    public static final Integer STATUS_CHECK = 95;
    public static final Integer STATUS_RELEASE = 96;
    public static final Integer STATUS_DIS_HIS = 97;
    public static final Integer STATUS_CHECKED = 98;
    public static final Integer STATUS_INVALID = -3;
    public static final Integer STATUS_CLOSE = -5;
    /**
     * 授信延期标识最后一条延期记录-是
     */
    public static final String RAT_DELAY_SIGN_NO = "0";
    /**
     * 授信延期标识最后一条延期记录-否
     */
    public static final String RAT_DELAY_SIGN_YES = "1";
    /**
     * 授信结转类型-相同授信品种转移
     */
    public static final String RAT_CHANGE_TYPE_1 = "1";
    /**
     * 授信结转类型-综合授信转非综合授信
     */
    public static final String RAT_CHANGE_TYPE_2 = "2";
    /**
     * 授信结转类型-非综合授信转综合授信
     */
    public static final String RAT_CHANGE_TYPE_3 = "3";
    /**
     * 金额单位-元
     */
    public static final Integer COMMON_MONEYUNIT_1 = 1;
    /**
     * 金额单位-万元
     */
    public static final Integer COMMON_MONEYUNIT_10000 = 10000;
    /**
     * 额度包含或者交叉允许选择
     */
    public static final String RAT_CONFIG_RATCONTAINS = "ratContains";
    /**
     * 是否启用敞口额度
     */
    public static final String RAT_CONFIG_OPENRAT = "isStartOpenRat";
    /**
     * 是否挂接保证金
     */
    public static final String RAT_CONFIG_ISHANGBAIL = "isHangBail";
    /**
     * 是否挂接担保
     */
    public static final String RAT_CONFIG_ISGUARANTEE = "mountGuarantee";
    /**
     * 二次授信对象控制模式
     */
    public static final String RAT_CONFIG_RATCTRMODEL = "ratctrModel";
    /**
     * 授信到期提醒天数
     */
    public static final String RAT_CONFIG_NOTIFYDAYS = "notifyDays";
    /**
     * 是否对接合同管理
     */
    public static final String RAT_CONFIG_RELATECON = "relateCon";
    /**
     * 对接合同管理模式
     */
    public static final String RAT_CONFIG_RATCONTYPE = "conType";
    /**
     * 授信银行控制
     */
    public static final String RAT_BANK_CONTROLTYPE = "bankControlType";
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
    public static final String OP_MSG_RELEASE = "释放授信额度";
    /**
     * 消息类型
     */
    /*** 消息类型：0-暂存  */
    public final static Integer MESSAGE_TYPE_TEMP = 0;
    /*** 消息类型：1-提交  */
    public final static Integer MESSAGE_TYPE_SUBMIT = 1;
    /*** 消息类型：2-录入  */
    public final static Integer MESSAGE_TYPE_ADD = 2;
    /*** 消息类型：3-删除  */
    public final static Integer MESSAGE_TYPE_DELETE = 3;
    /*** 消息类型：4-审批打回  */
    public final static Integer MESSAGE_TYPE_CHECK_BACK = 4;
    /*** 消息类型：5-审批通过  */
    public final static Integer MESSAGE_TYPE_CHECK_PASS = 5;
    /*** 消息类型：6-编辑  */
    public final static Integer MESSAGE_TYPE_UPDATE = 6;
    /**
     * 消息类型：7-释放
     */
    public final static Integer MESSAGE_TYPE_RELEASE = 7;
    /**
     * 是否挂接担保-否
     */
    public static final String IS_HANG_GUA_0 = "0";
    /**
     * 是否挂接担保-是
     */
    public static final String IS_HANG_GUA_1 = "1";
    /**
     * 是否启用敞口额度
     */
    public static final String RAT_CONFIG_STARTRATEXP = "isStartRatExposure";
    /**
     * 授信调整类型-银行授信录入
     */
    public static final String RAT_INPUT_ADJUST = "0";
    /**
     * 授信调整类型-银行授信分配
     */
    public static final String RAT_DIS_ADJUST = "1";
    /**
     * 授信生成合同
     */
    public static final String RAT_CONFIG_RATCONTYPE_0 = "0";
    /**
     * 授信调用合同
     */
    public static final String RAT_CONFIG_RATCONTYPE_1 = "1";
    /**
     * 授信合同挂接产品业务类类型
     */
    public static final String RAT_CONFIG_CONPDTYPE = "CON_PD_RAT";
    /**
     * 授信方式：1循环授信
     */
    public static final String RAT_RECYCLE_YES = "1";
    /**
     * 授信方式：0不循环授信
     */
    public static final String RAT_RECYCLE_NO = "0";
    /**
     * 授信银行控制：0银行行号相同
     */
    public static final String RAT_BANKCODE_SAME = "0";
    /**
     * 授信银行控制：1银行类别相同
     */
    public static final String RAT_BANKTYPE_SAME = "1";
    /**
     * 授信录入导入
     */
    /**
     * 授信单位不能为空！
     */
    public final static String CMS_RATBKRATION_CORP_ISNULL = "cms.ratBkration.corp.isNull";
    /**
     * 授信单位不存在！
     */
    public final static String CMS_RATBKRATION_CORP_ERROR = "cms.ratBkration.corp.error";
    /**
     * 授信模式不能为空！
     */
    public final static String CMS_RATBKRATION_MODEL_ISNULL = "cms.ratBkration.model.isNull";
    /**
     * 授信模式不存在！
     */
    public final static String CMS_RATBKRATION_MODEL_ERROR = "cms.ratBkration.model.error";
    /**
     * 银行开户行行号不能为空！
     */
    public final static String CMS_RATBKRATION_BANKCODE_ISNULL = "cms.ratBkration.bankCode.isNull";
    /**
     * 银行信息不存在！
     */
    public final static String CMS_RATBKRATION_BANK_ERROR = "cms.ratBkration.bank.error";
    /**
     * 授信开始时间不能为空！
     */
    public final static String CMS_RATBKRATION_STARTDATE_ISNULL = "cms.ratBkration.startDate.isNull";
    /**
     * 授信结束时间不能为空！
     */
    public final static String CMS_RATBKRATION_ENDDATE_ISNULL = "cms.ratBkration.endDate.isNull";
    /**
     * 授信开始时间不能大于授信结束时间！
     */
    public final static String CMS_RATBKRATION_DATE_ERROR = "cms.ratBkration.date.error";
    /**
     * 结转授信不符合条件！
     */
    public final static String CMS_RATBKRATION_ORAT_ERROR = "cms.ratBkration.orat.error";
    /**
     * 自主授信才可结转！
     */
    public final static String CMS_RATBKRATION_RATCAN_ERROR = "cms.ratBkration.ratCan.error";
    /**
     * 该单位不是集团统一授信主体！
     */
    public final static String CMS_RATBKRATION_CORP_MODEL = "cms.ratBkration.corp.model";
    /**
     * 币种不能为空！todo 词不达意
     */
    public final static String CMS_GUACONTRACT_CUR_ISNULL = "cms.guacontract.cur.isNull";
    /**
     * 币种不存在！todo 词不达意
     */
    public final static String CMS_GUACONTRACT_CUR_ERROR = "cms.guacontract.cur.error";
    /**
     * 授信录入应用权限
     */
    public static final String CMS_RATION_RATION_APPLY = "cms:rat:ration:apply";
    /**
     * 授信录入查询权限
     */
    public static final String CMS_RATION_RATION_QUERY = "cms:rat:ration:query";
    /**
     * 授信分配应用权限
     */
    public static final String CMS_RATION_DIS_APPLY = "cms:rat:dis:apply";
    /**
     * 授信分配查询权限
     */
    public static final String CMS_RATION_DIS_QUERY = "cms:rat:dis:query";
    /**
     * 银行追加应用权限
     */
    public static final String CMS_RATION_ADD_APPLY = "cms:rat:add:apply";
    /**
     * 银行追加查询权限
     */
    public static final String CMS_RATION_ADD_QUERY = "cms:rat:add:query";
    /**
     * 集团追加应用权限
     */
    public static final String CMS_RATION_CPADD_APPLY = "cms:rat:cpadd:apply";
    /**
     * 集团追加查询权限
     */
    public static final String CMS_RATION_CPADD_QUERY = "cms:rat:cpadd:query";
    /**
     * 授信调剂应用权限
     */
    public static final String CMS_RATION_RELIEF_APPLY = "cms:rat:relief:apply";
    /**
     * 授信调剂查询权限
     */
    public static final String CMS_RATION_RELIEF_QUERY = "cms:rat:relief:query";
    /**
     * 授信占用应用权限
     */
    public static final String CMS_RATION_APPLY_APPLY = "cms:rat:apply:apply";
    /**
     * 授信占用查询权限
     */
    public static final String CMS_RATION_APPLY_QUERY = "cms:rat:apply:query";
    /**
     * 授信延期应用权限
     */
    public static final String CMS_RATION_DELAY_APPLY = "cms:rat:delay:apply";
    /**
     * 授信延期查询权限
     */
    public static final String CMS_RATION_DELAY_QUERY = "cms:rat:delay:query";
    /**
     * 授信结转应用权限
     */
    public static final String CMS_RATION_CHANGE_APPLY = "cms:rat:change:apply";
    /**
     * 授信报表查询权限
     */
    public static final String CMS_RATION_RATIONQUERY_QUERY = "cms:rat:rationQuery:query";
    /**
     * 授信额度综合统计查询权限
     */
    public static final String CMS_REPORT_AMOUNT_QUERY = "cms:report:amount:query";
    /**
     * 银行授信使用统计表查询权限
     */
    public static final String CMS_REPORT_USE_QUERY = "cms:report:use:query";
    /**
     * 信贷融资利息支出报表查询权限
     */
    public static final String CMS_INTEREST_PAYMENT_QUERY = "cms:interest:payment:query";
    /**授信担保变更权限*/
    /**
     * 授信担保变更查询权限
     */
    public static final String CMS_RATION_GUA_QUERY = "cms:rat:gua:query";
    /**
     * 授信报表查询权限
     */
    public static final String CMS_RATION_RATIONITEMUSE_QUERY = "cms:rat:rationItemUse:query";
    /**
     * 授信额度调整查询权限
     */
    public static final String CMS_RATION_ADJUST_QUERY = "cms:rat:rationAdjust:query";
    /**
     * 授信额度调整操作权限
     */
    public static final String CMS_RATION_ADJUST_APPLY = "cms:rat:rationAdjust:apply";
    /**
     * 内部授信录入应用权限
     */
    public static final String CMS_CPRATION_EDIT_APPLY = "cms:cprat:ration:apply";
    /**
     * 内部授信录入查询权限
     */
    public static final String CMS_CPRATION_EDIT_QUERY = "cms:cprat:ration:query";
    /**
     * 内部授信追加应用权限
     */
    public static final String CMS_CPRATION_ADD_APPLY = "cms:cprat:add:apply";
    /**
     * 内部授信追加查询权限
     */
    public static final String CMS_CPRATION_ADD_QUERY = "cms:cprat:add:query";
    /**
     * 内部授信分配应用权限
     */
    public static final String CMS_CPRATION_DIS_APPLY = "cms:cprat:dis:apply";
    /**
     * 内部授信分配查询权限
     */
    public static final String CMS_CPRATION_DIS_QUERY = "cms:cprat:dis:query";
    /**
     * 内部授信占用应用权限
     */
    public static final String CMS_CPRATION_APPLY_APPLY = "cms:cprat:apply:apply";
    /**
     * 内部授信占用查询权限
     */
    public static final String CMS_CPRATION_APPLY_QUERY = "cms:cprat:apply:query";
    /**
     * 内部授信结转应用权限
     */
    public static final String CMS_CPRATION_CHANGE_APPLY = "cms:cprat:change:apply";
    /**
     * 内部授信调剂查询权限
     */
    public static final String CMS_CPRATION_RELIEF_QUERY = "cms:cprat:relief:query";
    /**
     * 内部授信调剂应用权限
     */
    public static final String CMS_CPRATION_RELIEF_APPLY = "cms:cprat:relief:apply";
    /**
     * 内部授信报表查询权限
     */
    public static final String CMS_CPRATION_COUNT_QUERY = "cms:cpration:count:query";
    /**
     * 被担保合同变更应用权限
     */
    public static final String CMS_GUARANTEE_CHANGE_APPLY = "cms:guarantee:change:apply";
    /**
     * 被担保合同便跟查询权限
     */
    public static final String CMS_GUARANTEE_CHANGE_QUERY = "cms:guarantee:change:query";
    /**
     * 银行授信情况统计表查询权限
     */
    public static final String CMS_REPORT_BANKRATSTATISTICS_QUERY = "cms:report:bankratstatistics:query";
    /**
     * 授信额度测算查询权限
     */
    public static final String CMS_RATION_AMOUNTCALCULATE_QUERY = "cms:rat:amountCalculate:query";
    /**
     * 统计维度：1---融资银行
     */
    public static final String SORT_TYPE_1 = "1";
    /**
     * 统计维度：0---融资单位
     */
    public static final String SORT_TYPE_0 = "0";
}
