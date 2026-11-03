package com.bytter.erp.constant;
/**
 * 消息通知的常量类型
 * Created by zhoul on 2017/10/30.
 */
public class NotificationConstant {
    /**
     * XXX模块
     * created by xxx
     */
    public static class AAA{
        /** 标签名称 */
       public static final String TEST = "test";
       public static final String TEST1 = "sys:xxxx:xxxx";
       public static final String TEST2 = "sys:xxxx:xxxx";
    }
	/**
	 * 消息通知类型：1、通知
	 * created by chenlw on 2020/03
	 */
	public static final int TYPE_UNIVERSAL_NOTIFICATION = 1;
	/**
	 * 消息通知类型：2、待处理
	 * created by chenlw on 2020/03
	 */
	public static final int TYPE_PENDING_NOTIFICATION = 2;
	/**
	 * 基础模块 created by shenc
	 */
	public static class BASESYS{
		/**货币汇率维护复核*/
		public static final String FCEXCRATE_ADJUST = "sys:fcexcrateadjust:submit";
		/**货币汇率维护复核打回*/
		public static final String FCEXCRATE_ADJUST_BACK = "sys:fcexcrateadjust:back";
		/** 网银证书到期提醒*/
		public static final String BANKACC_CFCA_DATE= "netbank:bankacc:cfca:date";
	}
	/**
	 * 票据模块 created by zhengg
	 */
	public static class ACCEBILL {
//		应付票据
		/** 开票登记 */
		public static final String EMIT_BILL = "bill:accebill:emitbill";
		/** 开票补录 */
		public static final String EMIT_BILL_SUPPLY = "bill:accebill:emitbillsupply";
		/** 未用退票 */
		public static final String EMIT_BILL_BACK = "bill:accebill:emitbillback";
		/** 兑付确认*/
		public static final String EMIT_BILL_PAY = "bill:accebill:emitbillpay";
//		应收票据
		/** 收票登记*/
		public static final String RECEIVE_BILL = "bill:accebill:receivebill";
		/** 票据移存*/
		public static final String INVEN_TRANSFER = "bill:accebill:inventransfer";
		/** 票据取票*/
		public static final String INVEN_FETCH = "bill:accebill:invenfetch";
		/** 票据背书*/
		public static final String ENDORSE = "bill:accebill:endorse";
		/** 贴现申请*/
		public static final String DISCOUNT_APPLY = "bill:accebill:discountapply";
		/** 贴现办理*/
		public static final String DISCOUNT_MANAGER = "bill:accebill:discountmanager";
		/** 贴现到账*/
		public static final String DISCOUNT_ACCOUNT = "bill:accebill:discountaccount";
		/** 贴现收回*/
		public static final String DISCOUNT_COLLECT = "bill:accebill:discountcollect";
		/** 贴现赎回申请*/
		public static final String DISCOUNT_RANSOM = "bill:accebill:discountransom";
		/** 质押申请*/
		public static final String PLEDGE_APPLY = "bill:accebill:pledgeapply";
		/** 解除质押*/
		public static final String PLEDGE_RELIEVE = "bill:accebill:pledgerelieve";
		/** 质押收回*/
		public static final String PLEDGE_COLLECT = "bill:accebill:pledgecollect";
		/** 票据上划*/
		public static final String TRANSFER_UPAPPLY = "bill:accebill:billtransferupapply";
		/** 票据归集*/
		public static final String TRANSFER_COLLECTION = "bill:accebill:transfercollection";
		/** 票据下拨申请*/
		public static final String TRANSFER_DOWNAPPLY = "bill:accebill:transferdownapply";
		/** 票据下拨*/
		public static final String TRANSFER_DOWNMANAGE = "bill:accebill:transferdownmanage";
		/** 应收退票*/
		public static final String RECEIVE_BILLBACK = "bill:accebill:receivebillback";
		/** 托收申请*/
		public static final String COLLECTION_APPLY = "bill:accebill:collectionapply";
		/** 托收到账*/
		public static final String COLLECTION_ACCOUNT = "bill:accebill:collectionaccount";
//		电票业务
		/** 票据签收*/
		public static final String BILL_SIGN = "bill:accebill:billsign";
		/** 签收补录*/
		public static final String SIGN_SUPPLY = "bill:accebill:signsupply";
		/** 提示保证*/
		public static final String GUARANTEE_APPLY = "bill:accebill:guaranteeapply";
		/** 提示承兑*/
		public static final String ACCEPT_APPLY = "bill:accebill:acceptapply";
		/** 提示收票*/
		public static final String RECEIVE_APPLY = "bill:accebill:receiveapply";
		/** 通用撤回*/
		public static final String RECALL = "bill:accebill:recall";
		/** 追索通知*/
		public static final String SEARCHFOR_APPLY = "bill:accebill:searchforapply";
		/** 追索清偿*/
		public static final String SEARCHFOR_MANAGE = "bill:accebill:searchformanage";
		/** 指令处理*/
		public static final String BILL_INSTRUCTION_MANAGE = "bill:accebill:instructionmanage";
		// 背书请求
		/** 背书请求*/
		public static final String BILL_ENDORSE_REQUEST = "bill:accebill:endorseRequest";
		/** 背书请求应答*/
		public static final String BILL_ENDORSE_RESPONSE = "bill:accebill:endorseResponse";
	}
	/**
	 * 结算管理 created by huangx
	 */
    /**
     * 结算管理 模块
     */
    public static class NETBANK{
    	//内部账户管理
		/** 开户待复核*/
        public static final String INNERACCOUNT_FCACC_CHECK = "netbank:innerAccount:fcAcc:check";
		/** 资金池开户待复核*/
		public static final String INNERACCOUNT_FCACC_POOL_CHECK = "netbank:innerAccount:fcAcc:pool:check";
		/** 改户待复核*/
        public static final String INNERACCOUNT_FCACC_CHANGECHECK = "netbank:innerAccount:fcAcc:changeCheck";
		/** 资金池改户待复核*/
		public static final String INNERACCOUNT_FCACC_POOL_CHANGECHECK = "netbank:innerAccount:fcAcc:pool:changeCheck";
		/** 冻结待复核*/
        public static final String INNERACCOUNT_FCFREEZE_CHECK = "netbank:innerAccount:fcFreeze:check";
		/** 解冻待复核*/
        public static final String INNERACCOUNT_FCFREEZE_UNCHECK = "netbank:innerAccount:fcFreeze:unCheck";
		/** 注销待复核*/
        public static final String INNERACCOUNT_FCACC_CANCELCHECK = "netbank:innerAccount:fcAcc:cancelCheck";
		/** 资金池注销待复核*/
		public static final String INNERACCOUNT_FCACC_POOL_CANCELCHECK = "netbank:innerAccount:fcAcc:pool:cancelCheck";
		/** 初始化待复核*/
        public static final String INNERACCOUNT_FCINITACC_CHECK = "netbank:innerAccount:fcInitAcc:check";
		/** 开户复核打回*/
		public static final String INNERACCOUNT_FCACC_BACK = "netbank:innerAccount:fcAcc:back";
		/** 资金池开户复核打回*/
		public static final String INNERACCOUNT_FCACC_POOL_BACK = "netbank:innerAccount:fcAcc:pool:back";
		/** 改户复核打回*/
		public static final String INNERACCOUNT_FCACC_CHANGEBACK = "netbank:innerAccount:fcAcc:changeBack";
		/** 资金池改户复核打回*/
		public static final String INNERACCOUNT_FCACC_POOL_CHANGEBACK = "netbank:innerAccount:fcAcc:pool:changeBack";
		/** 冻结复核打回*/
		public static final String INNERACCOUNT_FCFREEZE_BACK = "netbank:innerAccount:fcFreeze:back";
		/** 解冻复核打回*/
		public static final String INNERACCOUNT_FCFREEZE_UNBACK = "netbank:innerAccount:fcFreeze:unBack";
		/** 注销复核打回*/
		public static final String INNERACCOUNT_FCACC_CANCELBACK = "netbank:innerAccount:fcAcc:cancelBack";
		/** 资金池注销复核打回*/
		public static final String INNERACCOUNT_FCACC_POOL_CANCELBACK = "netbank:innerAccount:fcAcc:pool:cancelBack";
		/** 初始化复核打回*/
		public static final String INNERACCOUNT_FCINITACC_BACK = "netbank:innerAccount:fcInitAcc:back";
		/** 内部账户冻结到期 */
		public static final String INNERACCOUNT_FCFREEZE_EXPIRE = "netbank:innerAccount:fcFreeze:expire";
  		//银行账户管理
  		/** 银行账户开户*/
  		public static final String BANKACC_REQ = "netbank:bankacc:req";
  		/** 银行账户开户办理*/
  		public static final String BANKACC_REQ_HANDLE = "netbank:bankacc:req:handle";
  		/** 银行账户开户办理成功*/
  		public static final String BANKACC_REQ_HANDLE_SUCCESS = "netbank:bankacc:req:handle:success";
  		/** 银行账户变更*/
  		public static final String BANKACC_REV = "netbank:bankacc:rev";
  		/** 银行账户变更办理*/
  		public static final String BANKACC_REV_HANDLE = "netbank:bankacc:rev:handle";
  		/** 银行账户变更办理成功*/
  		public static final String BANKACC_REV_HANDLE_SUCCESS = "netbank:bankacc:rev:handle:success";
  		/** 银行账户注销*/
  		public static final String BANKACC_DEL = "netbank:bankacc:del";
  		/** 银行账户注销办理*/
  		public static final String BANKACC_DEL_HANDLE = "netbank:bankacc:del:handle";
  		/** 银行账户注销办理成功*/
  		public static final String BANKACC_DEL_HANDLE_SUCCESS = "netbank:bankacc:del:handle:success";
  		/** 银行账户冻结*/
  		public static final String BANKACC_FREEZE = "netbank:bankacc:freeze";
  		/** 银行账户解冻*/
  		public static final String BANKACC_UNFREEZE = "netbank:bankacc:unfreeze";
  		/** 银行账户流水管理*/
  		public static final String BANKACC_DTL = "netbank:bankacc:dtl";
  		/** 银行账户余额管理*/
  		public static final String BANKACC_BAL = "netbank:bankacc:bal";
		/** 银行账户到期提醒*/
		public static final String BANKACC_ENDDATE = "netbank:bankacc:enddate";
		/** 付款明细勾兑提醒*/
		public static final String BANKACC_PAYMENTBLENDING = "netbank:bankacc:paymentblending";
		/** 账户年检到期提醒*/
		public static final String BANKACC_CHECK_EXPIRE_DATE = "netbank:bankacc:check:expire:date";
		/** 网银信息到期提醒*/
		public static final String BANKACC_CHECK_NETUKEY_DATE = "netbank:bankacc:check:netUKey:date";
		/** 银行账户开户*/
		public static final String BANKACC_DTL_CHECK = "netbank:bankacc:dtl:check";
  		//支付结算
  		/** 付款办理*/
  		public static final String PAY_APPLY = "netbank:pay:apply";
  		//费用报销
  		/** 费用报销制单*/
  		public static final String EXPENSE_APPLY = "netbank:expense:apply";
  		/** 费用报销处理*/
  		public static final String EXPENSE_DEAL = "netbank:expense:deal";
  		//保证金
  		/**保证金存出审批通过*/
  		public static final String FCBOND_APPLY_PASS = "netbank:fcbond:apply:pass";
  		/**保证金存出审批打回*/
  		public static final String FCBOND_APPLY_BACK = "netbank:fcbond:apply:back";
  		/**保证金追加审批通过*/
  		public static final String FCBOND_ADD_PASS="netbank:fcbond:add:pass";
  		/**保证金追加审批打回*/
  		public static final String FCBOND_ADD_BACK ="netbank:fcbond:add:back";
  		/**保证金维护审批通过*/
  		public static final String FCBOND_PRESERVE_PASS ="netbank:fcbond:preserve:pass";
  		/**保证金维护审批打回*/
  		public static final String FCBOND_PRESERVE_BACK ="netbank:fcbond:preserve:back";
		/**保证金到期提醒*/
		public static final String FCBOND_EXPIRE_REMIND ="netbank:fcbond:expire:remind";
  		//资金调拨
  		/** 上划*/
  		public static final String ALLOCATION_UP = "netbank:allocation:up";
  		/** 下拨*/
  		public static final String ALLOCATION_DOWN = "netbank:allocation:down";
  		/** 上缴*/
  		public static final String ALLOCATION_TURN = "netbank:allocation:turn";
  		/** 请款*/
  		public static final String ALLOCATION_ASK = "netbank:allocation:ask";
  		/** 头寸调拨*/
  		public static final String ALLOCATION_POS = "netbank:allocation:pos";
  		//银企直连
  		/** 直营付款*/
		public static final String BISEXC_SALEPAY = "netbank:bdlink:bisexc:salepay";
		/** 付款指令发送*/
		public static final String BISEXC_SEND = "netbank:bdlink:bisexc:send";
		/** 付款指令交易失败*/
		public static final String BISEXC_FAIL = "netbank:bdlink:bisexc:fail";
		/** 付款指令交易成功*/
		public static final String BISEXC_SUCCESS = "netbank:bdlink:bisexc:success";
		/** 收款指令发送*/
		public static final String BISGATHERCMD_SEND = "netbank:bdlink:bisgathercmd:send";
		/** 收款指令交易失败*/
		public static final String BISGATHERCMD_FAIL = "netbank:bdlink:bisgathercmd:fail";
		/** 收款指令交易成功*/
		public static final String BISGATHERCMD_SUCCESS  = "netbank:bdlink:bisgathercmd:success";
		/** 理财指令发送*/
		public static final String BISFINANCEEXC_SEND = "netbank:bdlink:bisfinanceexc:send";
		/** 理财指令交易失败*/
		public static final String BISFINANCEEXC_FAIL = "netbank:bdlink:bisfinanceexc:fail";
		/** 理财指令交易成功*/
		public static final String BISFINANCEEXC_SUCCESS = "netbank:bdlink:bisfinanceexc:success";
		/** 付款支付超时提醒*/
		public static final String BISEXC_OVERTIME = "netbank:bdlink:bisexc:overtime";
		/** 付款可疑指令发送确认提醒*/
		public static final String BISEXC_DOUBTFUL_OVERTIME = "netbank:bdlink:bisexc:doubtful:overtime";
		/** 支付等待结果超时提醒*/
		public static final String BISEXC_WAITRESULT_OVERTIME = "netbank:bdlink:bisexc:waitresult:overtime";
		//自动支付
		/** 自动支付*/
		public static final String BISPAYPOLICY_AUTOPAY = "netbank:pay:bispaypolicy:autopay";
		//代发工资
		public static final String WAGE_APPLY = "netbank:wage:apply";
		//ERP付款提醒
		/** 付款办理*/
		public static final String ERP_PAY_APPLY = "netbank:erp:pay:apply";
		/** 费用报销*/
		public static final String ERP_EXPENSE_APPLY = "netbank:erp:expense:apply";
		/** 代发工资*/
		public static final String ERP_WAGE_APPLY = "netbank:erp:wage:apply";
    }
	/**
	 * 资金计划 created by huangx
	 */
	public static class BUDGET {
		/** 计划未上报*/
		public static final String BUDGET_NOT_INPUT = "budget:budget:not:input";
		//计划上报
		/** 年计划上报*/
		public static final String BUDGET_INPUT_YEAR = "budget:budget:input:year";
		/** 季计划上报*/
		public static final String BUDGET_INPUT_QUARTER = "budget:budget:input:quarter";
		/** 月计划上报*/
		public static final String BUDGET_INPUT_MONTH = "budget:budget:input:month";
		/** 周计划上报*/
		public static final String BUDGET_INPUT_WEEK = "budget:budget:input:week";
		/** 天计划上报*/
		public static final String BUDGET_INPUT_DAY = "budget:budget:input:day";
		//计划追加
		/** 年计划追加*/
		public static final String BUDGET_SUPERADD_YEAR = "budget:budget:superadd:year";
		/** 季计划追加*/
		public static final String BUDGET_SUPERADD_QUARTER = "budget:budget:superadd:quarter";
		/** 月计划追加*/
		public static final String BUDGET_SUPERADD_MONTH = "budget:budget:superadd:month";
		/** 周计划追加*/
		public static final String BUDGET_SUPERADD_WEEK = "budget:budget:superadd:week";
		/** 天计划追加*/
		public static final String BUDGET_SUPERADD_DAY = "budget:budget:superadd:day";
		/** 计划执行*/
		public static final String BUDGET_PROCESS = "budget:budget:process";
		/** 计划上报汇总*/
		public static final String BUDGET_INPUT_COLLECT = "budget:budget:input:collect";
		/** 计划追加汇总*/
		public static final String BUDGET_SUPERADD_COLLECT = "budget:budget:superadd:collect";
	}
	/**
	 * 理财管理 created by huangx
	 */
	public static class CHOWMATISTIC {
		//银行理财
		/** 活期转定期*/
		public static final String BANK_FINANCE_C2T = "chowmatistic:bankfinance:c2t";
		/** 定期转活期*/
		public static final String BANK_FINANCE_T2C = "chowmatistic:bankfinance:t2c";
		/** 活期转通知*/
		public static final String BANK_FINANCE_C2N = "chowmatistic:bankfinance:c2n";
		/** 通知转活期*/
		public static final String BANK_FINANCE_N2C = "chowmatistic:bankfinance:n2c";
		//理财投资管理
		/**理财登记*/
		public static final String CHO_PRODUCT_REQ = "chowmatistic:product:req";
		/**理财赎回*/
		public static final String CHO_PRODUCT_REDEEM = "chowmatistic:product:redeem";
		/**理财到期*/
		public static final String CHO_PRODUCT_EXPIRE = "chowmatistic:choproductreq:expire";
		//内部理财
		/** 活期转定期*/
		public static final String INNER_FINANCE_C2T = "chowmatistic:innerfinance:c2t";
		/** 定期转活期*/
		public static final String INNER_FINANCE_T2C = "chowmatistic:innerfinance:t2c";
		/** 活期转通知*/
		public static final String INNER_FINANCE_C2N = "chowmatistic:innerfinance:c2n";
		/** 通知转活期*/
		public static final String INNER_FINANCE_N2C = "chowmatistic:innerfinance:n2c";
		/** 通知预约*/
		public static final String INNER_FINANCE_N2B = "chowmatistic:innerfinance:n2b";
		/** 取消通知预约*/
		public static final String INNER_FINANCE_B2N = "chowmatistic:innerfinance:b2n";
		/** 定期存款到期提醒*/
		public static final String INNER_FINANCE_C2T_EXPIRATION = "chowmatistic:innerfinance:c2t:expiration";
		/** 活期存款到期提醒*/
		public static final String INNER_FINANCE_C2N_EXPIRATION = "chowmatistic:innerfinance:c2n:expiration";
		/** 通知预约到期提醒*/
		public static final String INNER_FINANCE_N2B_EXPIRATION = "chowmatistic:innerfinance:n2b:expiration";
        /**协定存款到期提醒*/
        public static final String CHO_INNER_AGREED = "chowmatistic:inner:agreed:expiration";
		/**协定存款登记*/
        public static final String CHO_INNERAGREED_REG = "chowmatistic:innerAgreed:reg";
		/**协定存款到期处理*/
        public static final String CHO_INNERAGREED_POS = "chowmatistic:innerAgreed:pos";
		/**协定存款终止*/
        public static final String CHO_INNERAGREED_TERM = "chowmatistic:innerAgreed:term";
    }
	/**
	 * 信贷管理
	 * @author liyh
	 */
	public static class CMS {
		//内部贷款
		/** 贷款申请录入*/
		public static final String LOAN_ADD = "cms:loan:add";
		/** 贷款申请补录*/
		public static final String LOAN_REPAIR = "cms:loan:repair";
		/** 贷款发放*/
		public static final String LOAN_SEND = "cms:loan:send";
		/** 贷款合同生成*/
		public static final String LOAN_CONTRACT = "cms:loan:contract";
		/** 贷款还款*/
		public static final String LOAN_REPAY = "cms:loan:repay";
		/** 贷款还款确认*/
		public static final String LOAN_REPAY_CONFIRM = "cms:loan:repay:confirm";
		/** 贷款展期*/
		public static final String LOAN_DEFERRED = "cms:loan:deferred";
		/** 贷款展期确认*/
		public static final String LOAN_DEFERRED_CONFIRM = "cms:loan:deferred:confirm";
		/** 贷款到期提醒*/
		public static final String LOAN_MATURE = "cms:loan:mature";
		/**贷款还款到期提醒**/
		public static final String LOAN_REPAY_MATURE = "cms:loan:repay:mature";
		//委托贷款
		/** 委托贷款申请录入*/
		public static final String PRECATIVE_ADD = "cms:precative:add";
		/** 委托贷款申请补录*/
		public static final String PRECATIVE_REPAIR = "cms:precative:repair";
		/** 委托贷款发放*/
		public static final String PRECATIVE_SEND = "cms:precative:send";
		/** 委托贷款合同生成*/
		public static final String PRECATIVE_CONTRACT = "cms:precative:contract";
		/** 委托贷款还款*/
		public static final String PRECATIVE_REPAY = "cms:precative:repay";
		/** 委托贷款还款确认*/
		public static final String PRECATIVE_REPAY_CONFIRM = "cms:precative:repay:confirm";
		/** 委托贷款还款成功*/
		public static final String PRECATIVE_REPAY_SUCCESS = "cms:precative:repay:success";
		/** 委托贷款展期*/
		public static final String PRECATIVE_DEFERRED = "cms:precative:deferred";
		/** 委托贷款展期补录*/
		public static final String PRECATIVE_DEFERRED_REPAIR = "cms:precative:deferred:repair";
		/** 委托贷款展期确认*/
		public static final String PRECATIVE_DEFERRED_CONFIRM = "cms:precative:deferred:confirm";
		/** 委托贷款展期成功*/
		public static final String PRECATIVE_DEFERRED_SUCCESS = "cms:precative:deferred:success";
		/** 委托贷款到期提醒*/
		public static final String PRECATIVE_MATURE = "cms:precative:mature";
		/**委托贷款还款到期提醒**/
		public static final String PRECATIVE_REPAY_MATURE = "cms:precative:repay:mature";
		//银行借款
		/** 银行借款申请录入*/
		public static final String PROVIDE_ADD = "cms:provide:add";
		/** 银行借款申请补录*/
		public static final String PROVIDE_REPAIR = "cms:provide:repair";
		/** 银行借款发放*/
		public static final String PROVIDE_SEND = "cms:provide:send";
		/** 银行借款合同生成*/
		public static final String PROVIDE_CONTRACT = "cms:provide:contract";
		/** 银行借款还款*/
		public static final String PROVIDE_REPAY = "cms:provide:repay";
		/** 银行借款还款补录*/
		public static final String PROVIDE_REPAY_REPAIR = "cms:provide:repay:repair";
		/** 银行借款展期*/
		public static final String PROVIDE_DEFERRED = "cms:provide:deferred";
		/** 银行借款展期补录*/
		public static final String PROVIDE_DEFERRED_REPAIR = "cms:provide:deferred:repair";
		/** 银行借款展期确认*/
		public static final String PROVIDE_DEFERRED_CONFIRM = "cms:provide:deferred:confirm";
		/** 银行借款到期提醒*/
		public static final String PROVIDE_MATURE = "cms:provide:mature";
		/** 银行借款还款到期提醒*/
		public static final String PROVIDE_REPAY_MATURE = "cms:provide:repay:mature";
		/** 银行借款作废*/
		public static final String PROVIDE_ABOLISH = "cms:provide:abolish";
		/** 银行借款作废办理*/
		public static final String PROVIDE_ABOLISH_HANDLE = "cms:provide:abolish:handle";
		//融资租赁申请
		public static final String LEASE_ADD = "cms:lease:add";
		//融资租赁展期
		public static final String LEASE_EXTEND = "cms:lease:extend";
		//融资租赁标的物登记
		public final static String LEASE_OBJECT_INFO ="cms:leaseObject:info";
		//融资租赁标的物修改
		public final static String LEASE_UPDATE_OBJECT_INFO ="cms:lease:update:object:info";
		//融资租赁补录
		public static final String LEASE_REPAIR = "cms:lease:repair";
		//融资租赁变更
		public static final String LEASE_UPDATE = "cms:lease:update";
		//股权融资
		//股权融资登记
		public static final String EQUITY_INFO = "cms:equity:info";
		//股权融资缴付/过户
		public static final String EQUITY_PAYTHE = "cms:equity:payThe";
		//股权融资缴付/过户
		public static final String EQUITY_REPO = "cms:equity:repo";
		//银行授信
		/** 银行授信录入*/
		public static final String RAT_RATION = "cms:rat:ration";
		/** 银行授信分配*/
		public static final String RAT_DIS = "cms:rat:dis";
		/** 银行授信追加*/
		public static final String RAT_ADD = "cms:rat:add";
		/** 集团授信录入*/
		public static final String RAT_CPADD = "cms:rat:cpadd";
		/** 银行授信调剂*/
		public static final String RAT_RELIEF = "cms:rat:relief";
		/** 银行授信占用*/
		public static final String RAT_APPLY = "cms:rat:apply";
		/** 银行授信延期*/
		public static final String RAT_DELAY = "cms:rat:delay";
		/** 银行授信结转*/
		public static final String RAT_CHANGE = "cms:rat:change";
		/** 银行授信到期提醒*/
		public static final String RAT_MATURE = "cms:rat:mature";
		//企业授信
		/** 内部授信录入*/
		public static final String RAT_CPRATION_RATION = "cms:rat:cpration:ration";
		/** 内部授信追加*/
		public static final String RAT_CPRATION_ADD = "cms:rat:cpration:add";
		/** 内部授信分配*/
		public static final String RAT_CPRATION_DIS = "cms:rat:cpration:dis";
		/** 内部授信调剂*/
		public static final String RAT_CPRATION_RELIEF = "cms:rat:cpration:relief";
		/** 内部授信占用*/
		public static final String RAT_CPRATION_APPLY = "cms:rat:cpration:apply";
		/** 内部授信延期*/
		public static final String RAT_CPRATION_DELAY = "cms:rat:cpration:delay";
		/** 内部授信结转*/
		public static final String RAT_CPRATION_CHANGE = "cms:rat:cpration:change";
		/** 内部授信合同到期提醒*/
		public static final String RAT_CPRAT_MATURE = "cms:rat:cprat:mature";
		/** 内部授信占用到期提醒*/
		public static final String RAT_CPRATAPP_MATURE = "cms:rat:cpratapp:mature";
  		/**内部授信录入审批通过*/
  		public static final String CPRATION_RATION_PASS = "rat:cpration:ration:pass";
  		/**内部授信录入审批打回*/
  		public static final String CPRATION_RATION_BACK = "rat:cpration:ration:back";
  		/**内部授信追加审批通过*/
  		public static final String CPRATION_ADD_PASS="rat:cpration:add:pass";
  		/**内部授信追加审批打回*/
  		public static final String CPRATION_ADD_BACK ="rat:cpration:add:back";
  		/**内部授信分配审批通过*/
  		public static final String CPRATION_DIS_PASS ="rat:cpration:dis:pass";
  		/**内部授信分配审批打回*/
  		public static final String CPRATION_DIS_BACK ="rat:cpration:dis:back";
  		/**内部授信占用审批通过*/
  		public static final String CPRATION_APPLY_PASS ="rat:cpration:apply:pass";
  		/**内部授信占用审批打回*/
  		public static final String CPRATION_APPLY_BACK ="rat:cpration:apply:back";
		/**内部授信调剂审批通过*/
  		public static final String CPRATION_RELIEF_PASS="rat:cpration:relief:pass";
  		/**内部授信调剂审批打回*/
  		public static final String CPRATION_RELIEF_BACK="rat:cpration:relief:back";
  		//担保管理
		/** 保函到期提醒*/
		public static final String GUA_GUARANTEE_MATURE = "cms:gua:guarantee:mature";
		/** 直融申请*/
		public static final String DF_DIRECT_FINANCE_INFIO = "df:direct:finance:infio";
		/** 直融还款*/
		public static final String DF_DIRECT_FINANCE_REPAY = "df:direct:finance:repay";
		/**对外担保到期提醒*/
		public static final String GUA_EXTCON_REMIND = "gua:extcon:remind";
		/**被担保到期提醒*/
		public static final String GUA_COVERCON_REMIND = "gua:coverCon:remind";
		/** 内部拆借申请*/
		public static final String INL_INFIO = "cms:inl:info";
		/** 内部拆借发放-打回*/
		public static final String INL_SEND = "cms:inl:send";
		/** 内部拆借发放-申请*/
		public static final String INL_SEND_APP = "cms:inl:send:app";
		/** 内部拆借还款*/
		public static final String INL_REPAY = "cms:inl:repay";
		/** 内部拆借展期*/
		public static final String INL_DEFERRED = "cms:inl:deferred";
		/** 内部拆借作废*/
		public static final String INL_ABOLISH = "cms:inl:abolish";
		/** 内部拆借还款计划修改*/
		public static final String INL_REPAY_CHANGE = "cms:inl:repayChange";
		/** 内部拆借还款计划修改*/
		public static final String INL_ADJUSTRATE = "cms:inl:adjustrate";
	}
	/**
	 * 核算管理
	 * @author liyh
	 */
	public static class FINACIAL {
		//柜台内部账务
		/** 柜台账务改账*/
		public static final String BOOK_EDIT = "fcl:accbook:edit";
		/** 柜台账务复核*/
		public static final String BOOK_CHECK = "fcl:accbook:check";
		/** 柜台账务作废修改*/
		public static final String BOOK_INVALID_EDIT = "fcl:accbook:invalid:edit";
		/** 柜台账务作废复核*/
		public static final String BOOK_INVALID_CHECK = "fcl:accbook:invalid:check";
		/** 柜台账务冲账修改*/
		public static final String BOOK_RUSH_EDIT = "fcl:accbook:rush:edit";
		/** 柜台账务冲账复核*/
		public static final String BOOK_RUSH_CHECK = "fcl:accbook:rush:check";
		//表外账务
		/** 表外账务改账*/
		public static final String OUTBOOK_EDIT = "fcl:outaccbook:edit";
		/** 表外账务复核*/
		public static final String OUTBOOK_CHECK = "fcl:outaccbook:check";
		/** 表外账务作废修改*/
		public static final String OUTBOOK_INVALID_EDIT = "fcl:outaccbook:invalid:edit";
		/** 表外账务作废复核*/
		public static final String OUTBOOK_INVALID_CHECK = "fcl:outaccbook:invalid:check";
		/** 表外账务冲账修改*/
		public static final String OUTBOOK_RUSH_EDIT = "fcl:outaccbook:rush:edit";
		/** 表外账务冲账复核*/
		public static final String OUTBOOK_RUSH_CHECK = "fcl:outaccbook:rush:check";
		//表外账务
		/** 批量计息复核*/
		public static final String INTEREST_CHECK = "fcl:interest:check";
		/** 损益转本年利润记账待复核*/
		public static final String 	CLOSE_FCCARRY_CHECK = "fcl:close:fccarry:check";
		//网络记账
		/**收款记账待记账*/
		public static final String NETCHALK_TALLY = "fcl:netchalk:tally";
		/** 收款记账改账*/
		public static final String NETCHALK_EDIT = "fcl:netchalk:edit";
		/** 收款记账复核*/
		public static final String NETCHALK_CHECK = "fcl:netchalk:check";
		/** 中心付款待记账*/
		public static final String BISEXC_TALLY = "fcl:bisexc:tally";
		/** 中心付款记账改账*/
		public static final String BISEXC_EDIT = "fcl:bisexc:edit";
		/** 中心付款记账复核*/
		public static final String BISEXC_CHECK = "fcl:bisexc:check";
		/** 网点间来账待记账*/
		public static final String FCUNIONACCBOOK_TALLY = "fcl:fcunionaccbook:tally";
		/** 网点间来账记账改账*/
		public static final String FCUNIONACCBOOK_EDIT = "fcl:fcunionaccbook:edit";
		/** 网点间来账记账复核*/
		public static final String FCUNIONACCBOOK_CHECK = "fcl:fcunionaccbook:check";
		/**内部贷款记账待记账*/
		public static final String ENTERPRISELOAN_TALLY = "fcl:enterpriseloan:tally";
		/** 内部贷款记账改账*/
		public static final String ENTERPRISELOAN_EDIT= "fcl:enterpriseloan:edit";
		/** 内部贷款记账复核*/
		public static final String ENTERPRISELOAN_CHECK= "fcl:enterpriseloan:check";
		/** 银行借款待记账*/
		public static final String CMSPROVIDE_TALLY= "fcl:cmsprovide:tally";
		/** 银行借款记账改账*/
		public static final String CMSPROVIDE_EDIT= "fcl:cmsprovide:edit";
		/** 银行借款记账复核*/
		public static final String CMSPROVIDE_CHECK= "fcl:cmsprovide:check";
		/** 资金调拨记账待记账*/
		public static final String MONEYTRANSFERS_TALLY= "fcl:moneytransfers:tally";
		/** 资金调拨记账改账*/
		public static final String MONEYTRANSFERS_EDIT= "fcl:moneytransfers:edit";
		/** 资金调拨记账复核*/
		public static final String MONEYTRANSFERS_CHECK= "fcl:moneytransfers:check";
		/** 单位申请付款记账待记账*/
		public static final String CORPPAYMENT_TALLY= "fcl:corppayment:tally";
		/** 单位申请付款记账改账*/
		public static final String CORPPAYMENT_EDIT= "fcl:corppayment:edit";
		/** 单位申请付款记账复核*/
		public static final String CORPPAYMENT_CHECK= "fcl:corppayment:check";
		/** 委托贷款待记账*/
		public static final String PRECATIVE_TALLY = "fcl:precative:tally";
		/** 委托贷款记账改账*/
		public static final String PRECATIVE_EDIT = "fcl:precative:edit";
		/** 委托贷款记账复核*/
		public static final String PRECATIVE_CHECK = "fcl:precative:check";
		/** 出入库待记账*/
		public static final String ABBILL_OUT_PUT_TALLY_6 = "fcl:output:tally";
		/** 出入库改账*/
		public static final String ABBILL_OUT_PUT_EDIT_6 = "fcl:output:edit";
		/** 出入库复核*/
		public static final String ABBILL_OUT_PUT_CHECK_6 = "fcl:output:check";
		/** 退票待记账*/
		public static final String ABBILL_REFUND_TALLY_7 = "fcl:refund:tally";
		/** 退票改账*/
		public static final String ABBILL_REFUND_EDIT_7 = "fcl:refund:edit";
		/**退票复核*/
		public static final String ABBILL_REFUND_CHECK_7 = "fcl:refund:check";
		/** 托收待记账*/
		public static final String ABBILL_COLLECTION_TALLY_8 = "fcl:collection:tally";
		/** 托收改账*/
		public static final String ABBILL_COLLECTION_EDIT_8 = "fcl:collection:edit";
		/**托收复核*/
		public static final String ABBILL_COLLECTION_CHECK_8 = "fcl:collection:check";
		/** 背书待记账*/
		public static final String ABBILL_ENDORSE_TALLY_9 = "fcl:endorse:tally";
		/** 背书改账*/
		public static final String ABBILL_ENDORSE_EDIT_9 = "fcl:endorse:edit";
		/**背书复核*/
		public static final String ABBILL_ENDORSE_CHECK_9 = "fcl:endorse:check";
		/** 贴现待记账*/
		public static final String ABBILL_DISCOUNT_TALLY_10 = "fcl:discount:tally";
		/** 贴现改账*/
		public static final String ABBILL_DISCOUNT_EDIT_10 = "fcl:discount:edit";
		/**贴现复核*/
		public static final String ABBILL_DISCOUNT_CHECK_10 = "fcl:discount:check";
		/** 质押待记账*/
		public static final String ABBILL_PLEDGE_TALLY_11 = "fcl:pledge:tally";
		/** 质押改账*/
		public static final String ABBILL_PLEDGE_EDIT_11 = "fcl:pledge:edit";
		/**质押复核*/
		public static final String ABBILL_PLEDGE_CHECK_11 = "fcl:pledge:check";
		/** 托收自身待记账*/
		public static final String ABBILL_COLLECTION_SELF_TALLY_12 = "fcl:collection:self:tally";
		/** 托收自身改账*/
		public static final String ABBILL_COLLECTION_SELF_EDIT_12 = "fcl:collection:self:edit";
		/**托收自身复核*/
		public static final String ABBILL_COLLECTION_SELF_CHECK_12 = "fcl:collection:self:check";
		/** 贴现自身待记账*/
		public static final String ABBILL_DISCOUNT_SELF_TALLY_13 = "fcl:discount:self:tally";
		/** 贴现自身改账*/
		public static final String ABBILL_DISCOUNT_SELF_EDIT_13 = "fcl:discount:self:edit";
		/**贴现自身复核*/
		public static final String ABBILL_DISCOUNT_SELF_CHECK_13 = "fcl:discount:self:check";
		/** 票据调拨待记账*/
		public static final String ABBILL_PAPER_TALLY_14 = "fcl:paper:tally";
		/** 票据调拨改账*/
		public static final String ABBILL_PAPER_EDIT_14 = "fcl:paper:edit";
		/**票据调拨复核*/
		public static final String ABBILL_PAPER_CHECK_14 = "fcl:paper:check";
		/** 资金池记账待记账*/
		public static final String POOL_TALLY = "pool:tally";
		/** 资金池记账改账*/
		public static final String POOL_EDIT = "pool:edit";
		/** 资金池记账复核*/
		public static final String POOL_CHECK = "pool:check";
		/**内部存款待记账*/
		public static final String INNER_DEPOSIT_TALLY = "inner:deposit:tally";
		/**内部存款改账*/
		public static final String INNER_DEPOSIT_EDIT = "inner:deposit:edit";
		/**内部存款复核*/
		public static final String INNER_DEPOSIT_CHECK = "inner:deposit:check";
	}
	/**
	 * 资金预警消息提醒
	 * @author zhengg
	 */
	public static class FUNDWARN{
		/*** 预警类型-余额超标-单位*/
		public final static String FUNDWARN_BALOVER_CORP = "fundwarn:balover:corp";
		/*** 预警类型-余额超标-银行账户*/
		public final static String FUNDWARN_BALOVER_BANKACC = "fundwarn:balover:bankacc";
		/*** 预警类型-相同公司付款-单位*/
		public final static String FUNDWARN_EQCORPPAY_CORP = "fundwarn:eqCorpPay:corp";
		/*** 预警类型-大额账户支出-银行账户*/
		public final static String FUNDWARN_BANKACCPAY_BANKACC = "fundwarn:bankAccPay:bankacc";
		/*** 预警类型-净流入透支-单位*/
		public final static String FUNDWARN_INFLOWOVER_CORP = "fundwarn:inflowOver:corp";
		/*** 预警类型-净流入透支-银行账户*/
		public final static String FUNDWARN_INFLOWOVER_BANKACC = "fundwarn:inflowOver:bankacc";
		/*** 预警类型-账户余额持续下降-单位*/
		public final static String FUNDWARN_BALDOWN_CORP = "fundwarn:balDown:corp";
		/*** 预警类型-账户余额持续下降-银行账户*/
		public final static String FUNDWARN_BALDOWN_BANKACC = "fundwarn:balDown:bankacc";
		/*** 预警类型-系统外付款-单位*/
		public final static String FUNDWARN_OUTPAY_CORP = "fundwarn:outPay:corp";
		/*** 预警类型-系统外付款-银行账户*/
		public final static String FUNDWARN_OUTPAY_BANKACC = "fundwarn:outPay:bankacc";
		/*** 预警类型-支出户流入-单位*/
		public final static String FUNDWARN_EXPENDINFLOW_CORP = "fundwarn:expendInflow:corp";
		/*** 预警类型-支出户流入-银行账户*/
		public final static String FUNDWARN_EXPENDINFLOW_BANKACC = "fundwarn:expendInflow:bankacc";
		/*** 预警类型-库存现金超标-单位*/
		public final static String FUNDWARN_CASHOVER_CORP = "fundwarn:cashOver:corp";
		/*** 预警类型-库存现金超标-内部账号*/
		public final static String FUNDWARN_CASHOVER_FCACC = "fundwarn:cashOver:fcAcc";
		/*** 预警类型-存贷比超标-单位*/
		public final static String FUNDWARN_DEPOSITLOANOVER_CORP = "fundwarn:depositLoanOver:corp";
		/*** 预警类型-大额现金支出-内部账号*/
		public final static String FUNDWARN_CASHPAY_FCACC = "fundwarn:cashPay:fcAcc";
		/*** 预警类型-久悬户预警-银行账户*/
		public final static String FUNDWARN_OLDBANKACC_BANKACC = "fundwarn:oldBankAcc:bankacc";
		/*** 预警类型-预算执行进度-单位*/
		public final static String FUNDWARN_BUDGETPROGRESS_CORP = "fundwarn:budgetProgress:corp";
		/*** 预警类型-单日累计支付-单位*/
		public final static String FUNDWARN_DAYSUMPAY_CORP = "fundwarn:daySumPay:corp";
	}
	/**
	 * 国际结算
	 * @author zhengxn
	 */
	public static class INTERNATIONAL {
		/**收到信用证待交单**/
		public final static String INTERNATIONAL_RECEIVE_ARRIVAL = "international:receive:arrival";
		/**收到信用证修改**/
		public final static String INTERNATIONAL_RECEIVE_UPDATE = "international:receive:update";
		/**收到信用证交单待到期收款**/
		public final static String INTERNATIONAL_ARRIVAL_ARRIVAL = "international:arrival:arrival";
		/**收到信用证交单修改**/
		public final static String INTERNATIONAL_ARRIVAL_UPDATE = "international:arrival:update";
		/**收到信用证到期付款修改**/
		public final static String INTERNATIONAL_INCOMING_UPDATE = "international:incoming:update";
		/**收到信用证申请抵押修改**/
		public final static String INTERNATIONAL_MORTGAGE_UPDATE = "international:mortgage:update";
		/**信用证开证审批通过**/
		public final static String INTERNATIONAL_CREDIT_PASS = "international:credit:pass";
		/**信用证开证审批打回**/
		public final static String INTERNATIONAL_CREDIT_BACK = "international:credit:back";
		/**信用证开证审批通过**/
		public final static String INTERNATIONAL_SUPPLEMENT_CREDIT_PASS = "international:supplement:credit:pass";
		/**信用证开证审批打回**/
		public final static String INTERNATIONAL_SUPPLEMENT_CREDIT_BACK = "international:supplement:credit:back";
		/**信用证开证到期提醒**/
		public final static String INTERNATIONAL_CREDIT_EXPIRY = "international:credit:expiry";
		/**信用证付汇审批通过**/
		public final static String INTERNATIONAL_PAYMENT_PASS = "international:payment:pass";
		/**信用证付汇审批打回**/
		public final static String INTERNATIONAL_PAYMENT_BACK = "international:payment:back";
		/**信用证押汇审批通过**/
		public final static String INTERNATIONAL_DEPOSIT_PASS = "international:deposit:pass";
		/**信用证押汇审批打回**/
		public final static String INTERNATIONAL_DEPOSIT_BACK = "international:deposit:back";
		/**信用证展期审批通过**/
		public final static String INTERNATIONAL_DEPOSIT_DELAY_PASS = "international:deposit:delay:pass";
		/**信用证展期审批打回**/
		public final static String INTERNATIONAL_DEPOSIT_DELAY_BACK = "international:deposit:delay:back";
	}
	/**
	 * 营收稽核
	 * @author zhengg
	 */
	public static class RAS {
		/**营收稽核 尾款超额提醒**/
		public final static String RAS_END_EXCEED_MONEY = "ras:end:exceed:money";
	}
	/**
	 * 外汇管理
	 * @author  huangx
	 */
	public  static class FOREIGN{
		/** 外币汇款申请*/
		public static final String FOREIGNREQ_APPLY = "foreign:foreignreq:apply";
		//汇款指令
		/** 汇款指令发送*/
		public static final String BISFOREIGNEXC_SEND = "foreign:foreignexc:bisforeignexc:send";
		/** 汇款指令交易失败*/
		public static final String BISFOREIGNEXC_FAIL = "foreign:foreignexc:bisforeignexc:fail";
		/** 汇款指令交易成功*/
		public static final String BISFOREIGNEXC_SUCCESS = "foreign:foreignexc:bisforeignexc:success";
		/** 汇款支付超时提醒*/
		public static final String BISFOREIGNEXC_OVERTIME = "foreign:foreignexc:bisforeignexc:overtime";
		/** 汇款可疑指令发送确认提醒*/
		public static final String BISFOREIGNEXC_DOUBTFUL_OVERTIME = "foreign:foreignexc:bisforeignexc:doubtful:overtime";
		/**交易对手银行账户*/
		public static final String FEX_COTP_BANKACC = "fex:cotp:bankacc";
		/**交易对手银行账户审批打回*/
		public static final String FEX_COTP_BANKACC_BACK = "fex:cotp:bankacc:back";
		/**交易对手银行账户审批通过*/
		public static final String FEX_COTP_BANKACC_PASS = "fex:cotp:bankacc:pass";
		/**主体单位默认银行账户*/
		public static final String FEX_CORP_BANKACC = "fex:corp:bankacc";
		/**主体单位默认银行账户审批打回*/
		public static final String FEX_CORP_BANKACC_BACK = "fex:corp:bankacc:back";
		/**交易对手默认银行账户审批通过*/
		public static final String FEX_CORP_BANKACC_PASS = "fex:corp:bankacc:pass";
		/**交易对手默认银行账户*/
		public static final String FEX_COTP_DEFAULTACC = "fex:cotp:defaultacc";
		/**交易对手默认银行账户审批打回*/
		public static final String FEX_COTP_DEFAULTACC_BACK = "fex:cotp:defaultacc:back";
		/**交易对手默认银行账户审批通过*/
		public static final String FEX_COTP_DEFAULTACC_PASS = "fex:cotp:defaultacc:pass";
		/**交易对手*/
		public static final String FEX_COTP = "fex:cotp";
		/**交易需求*/
		public static final String FEX_TRAN_APP = "foreign:tranApp";
		//交易管理
		/** 外币买卖交易*/
		public static final String TRANREQ_FOREIGN_APPLY = "foreign:tranreq:foreign:apply";
		/** 外汇期权交易*/
		public static final String TRANREQ_OPTION_APPLY = "foreign:tranreq:option:apply";
		/** 资金调拨交易*/
		public static final String TRANREQ_ALLOCATION_APPLY = "foreign:tranreq:allocation:apply";
		/** 资金外付交易*/
		public static final String TRANREQ_PAY_APPLY = "foreign:tranreq:pay:apply";
		/** 提前交割或展期*/
		public static final String TRANDEAL_DELIVERY_APPLY = "foreign:trandeal:delivery:apply";
		/** 违约*/
		public static final String TRANDEAL_DEFAULT_APPLY = "foreign:trandeal:default:apply";
		/** NDF交割*/
		public static final String TRANDEAL_NDFDELIVERY_APPLY = "foreign:trandeal:ndfdelivery:apply";
		//结算管理
		/** 结算执行*/
		public static final String FOREIGN_CASHFLOW_APPLY = "foreign:cashflow:apply";
	}
	/**
	 * 合同管理
	 * @author wangzy
	 */
	public static class CONTRACT {
		/**合同管理 合同到期消息提醒**/
		public final static String CONTRACT_WARN_END = "contract:warn:end";
	}
	/**
	 * 凭证管理
	 * @author wangzy
	 */
	public static class CASHVOUCHER {
		/**合同管理 合同到期消息提醒**/
		public final static String CASHVOUCHER_BVPAYEECHEQUE_CHECK = "cashvoucher:bvpayeecheque:check";
	}
	/**
	 * 系统监控
	 */
	public static class MONITOR {
		/**
		 * 银行接口掉线提醒
		 */
		public static final String MONITOR_BISBIFMONITOR_DISCONNECTIONALERT = "monitor:bisBifMonitor:disConnectionAlert";
	}
}
