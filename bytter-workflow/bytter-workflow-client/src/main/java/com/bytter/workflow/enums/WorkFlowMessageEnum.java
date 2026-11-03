package com.bytter.workflow.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhoul on 2017/9/22.
 */
public enum WorkFlowMessageEnum implements IMessageEnum {
    /** 任务已被处理 */
    WORK_FLOW_TASK_WAS_DEAL("01001"),
	/** 此业务未设置审批流，不可提交，请与管理员联系！ */
    WORK_FLOW_MUST_APPROVE("01002"),
    /** 流程已被审批，无法撤回！*/
    WORK_FLOW_APPROVED("01003"),
    /**
     * 流程设计时逻辑存在错误，请检查！
     */
    WORK_FLOW_DESIGN_ERROR("01004"),
    /**
     * 待办流程中已存在相同的业务，请勿重复提交
     */
    WORK_FLOW_WAS_EXISTS("01005"),
    /** 不是流程发起人，无法撤回*/
    WORK_FLOW_REVOKE_USER_ERROR("01006"),
    /** 流程尚未提交，请检查!*/
    WORK_FLOW_NOT_EXISTS("01007"),
    /** 流程配置不允许撤回，请检查!*/
    WORK_FLOW_NOT_ALLOW_REVOKE("01008"),
    /** 流程定义被重新发布过，不允许撤回!*/
    WORK_FLOW_REDEPLOYED("01009"),
    /** 已成功撤回{0}条记录 !*/
    WORK_FLOW_REVOKE_SUCCESS_NUM("01010"),
    /** 流程需发布过才可修改配置!*/
    WORK_FLOW_UPLOAD_WITH_NO_DEPLOYMENT("01011"),
    /** 撤回失败 !*/
    WORK_FLOW_REVOKE_ALL_FAIL("01012"),
    /** 当前用户非审批人，不能撤回!*/
    WORK_FLOW_USER_ISNOT_APPROVER("01013"),
    /** 当前用户不是上一审批节点的审批人，不能撤回!*/
    WORK_FLOW_USER_ISNOT_BEFORE_APPROVER("01014"),
    /** 撤回失败，审批人不能将流程撤回到制单处!*/
    WORK_FLOW_APPROVER_CANNOT_REVOKE("01015"),
    /**成功{0}笔，失败{1}笔；失败原因：{2}*/
    WORK_FLOW_BATCH_SUBMIT_NOTICE("01099"),
    /**
     * 状态不是打回或者暂存，不允许删除委托
     */
    BPM_DELEGATION_STATUS_NOT_DELETABLE("01001"),
    /**
     * 当前用户不是管理员或授权人，不允许删除委托
     */
    BPM_DELEGATION_ROLE_NOT_DELETABLE("01002"),
    /**
     * 操作失败，数据状态有误
     */
    BPM_DELEGATION_STATUS_ERROR("010003"),
    /**
     * 业务:{0}的转授配置尚未结束
     */
    BPM_DELEGATION_CATEGORY_EXISTS("010004"),
    /**
     * 流程未发布
     */
    BPM_MODEL_NOT_DEPLOY("01005"),
    /**
     * 未获取到流程启动用户
     */
    BPM_MODEL_START_USER_NULL("01005"),

    //工作流前置校验异常
    NETBANK_MESSAGE_CONFIRM_BALANCE("01036"),
    NETBANK_MESSAGE_BALANCE("01044"),
    /**	账号[{0}]，账户信息不存在或已失效！*/
    NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST("01037"),
    /** 付款账号{0}没有维护接口代码*/
	NIS_WAGE_MESSAGE_BIFCODE_NULL("01038"),
	/** 接口代码{0}{1}没有维护业务规则，请联系管理员*/
	NIS_WAGE_MESSAGE_BISBIFRULE_NULL("01039"),
	/** 系统未维护相关业务的付款方式 */
	NIS_PAY_MESSAGE_PAYTYPE_NULL("01040"),
	/** 自动记账没有关联内部账户 */
	NIS_PAY_AUTO_NO_FCACC("01041"),
	/** 内部账号不能透支的情况下，校验余额是否大于等于付款金额 */
	NIS_PAY_NO_OVER_LESS_THAN_PAYMONEY("01042"),
	/** 内部账户允许透支（判断透支金额overdraft_money），限额透支的情况下，校验可透支余额是否大于等于付款金额 */
	NIS_PAY_PERMIT_OVER_LESS_THAN_PAYMONEY("01043"),
	/**未设置结算中心{0}自动复核人参数，无法复核*/
	FIN_BOOK_TALLY_AUTOCHECKER_NETNULL("01044"),
	/**账号{0}对应网点单位银行活期账号不存在*/
	NIS_ACCBOOK_1020_NOTEXISTS("01032"),
	/**{对应网点单位现金账号不存在*/
	NIS_ACCBOOK_1010_NOTEXISTS("01031"),
	/**记账业务不存在*/
	NIS_ACCBOOK_VOUCHERTYPE_NOTEXISTS("01030"),
	/*请先维护银行账号[{0}]与上级内部账号的关系*/
    FIN_COLLECT_MANUAL_SUPERCURRENTACC_ISNULL("07019"),
    /**单位企业活期活期账号不存在*/
	NIS_ACCBOOK_2120_NOTEXISTS("01034"),
	/**凭证的状态已经发生改变，请刷新页面后重试*/
	FIN_BOOK_TALLY_VOUCHER_CHANGE("05044"),
	/**业务品种不能为空*/
	FIN_BOOK_TALLY_BUSRACE_NO("05005"),
	/**起息日期不能为空*/
	FIN_BOOK_TALLY_INTERESTSTARTDATE_NO("05006"),
	/**记账日期不能为空*/
	FIN_BOOK_TALLY_TALLYDATE_NO("05007"),
	/**加急标志不能为空*/
	FIN_BOOK_TALLY_EMERGENCYSIGN_NO("05008"),
	/**凭证类型不能为空*/
	FIN_BOOK_TALLY_VOUCHERTYPE_NO("05009"),
	/**摘要不能为空*/
	FIN_BOOK_TALLY_SUMMARYCONTENT_NO("05010"),
	/**账户不能为空*/
	FIN_BOOK_TALLY_ACCOUNT_NO("05011"),
	/**起息日期不能大于记账日期*/
	FIN_BOOK_TALLY_INTERESTSTART_AFTER("05018"),
	/**账户{0}不能同时在借贷方都有数据*/
	FIN_BOOK_TALLY_ACCOUNT_BOTHSIDE("05012"),
	/**账号的币别必须相同*/
	FIN_BOOK_TALLY_ACCOUNT_SAMECUR("05013"),
	/**账号必须属于同一资金池*/
	FIN_BOOK_POOL_TALLY_ACCOUNT_SAMENET("05049"),
	/**账号必须属于同一网点*/
	FIN_BOOK_TALLY_ACCOUNT_SAMENET("05014"),
	/**借贷不平衡*/
	FIN_BOOK_TALLY_MONEY_BALANCE("05015"),
	/**记账日期必须大于账户{0}启用日期*/
	FIN_BOOK_TALLY_TALLYD_AFTER_ACC("05020"),
	/**凭证起息日期必须大于账户{0}启用日期*/
	FIN_BOOK_TALLY_START_AFTER_ACC("05019"),
	 /**未获取资金池主体日结日期，请联系管理员重新初始化资金池主体*/
    FIN_BOOK_NOPOOL_CLOSEDATE("05047"),
    /**日期{0}已日结，无法生成账务*/
	FIN_BOOK_TALLY_RUSHD_AFTER_CLOSED("05021"),
	/**日期{0}已日结，无法生成账务*/
	FIN_BOOK_TALLY_TALLYD_AFTER_CLOSED("05022"),
	/**未获取结算中心日结日期，请联系管理员重新初始化结算中心*/
    FIN_BOOK_NONET_CLOSEDATE("05046"),
    /**账户{0}为冻结只收不支账户*/
	FIN_BOOK_TALLY_ACCOUNT_FREEZE("05043"),
	/**账户{0}联机余额不存在，请联系管理员开户*/
	FIN_BOOK_TALLY_LEDGER_NO("05023"),
	/**账号{0}已透支*/
	FIN_BOOK_TALLY_ACC_OVERDRAFT("05024"),
	/**账号{0}已透支，且超过受限额度*/
	FIN_BOOK_TALLY_ACC_OVERDRAFT_MONEY("05025"),
	 /**
     * 银行活期对应的银行账号为空，请先维护
     */
    FIN_COLLECT_MANUAL_BANKACC_ISNULL("07020"),
    /** 自动记账没有关联内部账户,请检查收付款方是否关联内部账户! */
	NIS_PAY_AUTO_NO_SF_FCACC("07021"),
	/**未设置结算中心自动复核人参数，无法复核*/
	FIN_BOOK_TALLY_AUTOCHECKER_NULL("05034"),
	/**未设置资金池自动复核人参数，无法复核*/
	FIN_BOOK_POOL_TALLY_AUTOCHECKER_NULL("05048"),
	/**记账参数用户{0}不存在，请核对*/
	FIN_BOOK_TALLY_USER_NOTEXIST("05036"),
	/**未设置结算中心自动记账人参数，无法记账*/
	FIN_BOOK_TALLY_AUTOACCOUNTER_NULL("05032"),
	/**未设置结算中心{0}自动记账人参数，无法记账*/
	FIN_BOOK_TALLY_AUTOACCOUNTER_NETNULL("05033"),
	/**
	 *  未找到注册回调服务请检查配置或者路径
	 */
	WORKFLOW_NO_COMPLETE_ERR("05034"),
	;
    WorkFlowMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
