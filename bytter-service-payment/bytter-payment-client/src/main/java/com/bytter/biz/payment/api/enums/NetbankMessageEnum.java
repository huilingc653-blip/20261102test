package com.bytter.biz.payment.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum NetbankMessageEnum implements IMessageEnum  {
	/** 付款方式编码必须唯一 */
	NIS_VOUTYPE_MESSAGE_VOUCHERTYPE_UNIQUE("01001"),
	/** 请选择系统结算方式 */
	NIS_VOUTYPE_MESSAGE_SYSVOUCHERCODE_NULL("01002"),
	/** 付款方式名称必须唯一 */
	NIS_VOUTYPE_MESSAGE_VOUCHERTYPENAME_UNIQUE("01003"),
	/** 请选择单位 */
	NIS_ASSIGN_MESSAGE_CORPID_NULL("01003"),
	/** 请选择付款方式 */
	NIS_ASSIGN_MESSAGE_VOUCHERCODE_NULL("01004"),
	/** 请选择默认付款方式 */
	NIS_ASSIGN_MESSAGE_DEFVOUCHERCODE_NULL("01005"),
	/** 账户性质编码必须唯一 */
	NIS_ACCATTR_MESSAGE_ATTRIBUTECODE_UNIQUE("01006"),
	/** 账户属性编码必须唯一 */
	NIS_ACCNATURE_MESSAGE_NATURECODE_UNIQUE("01007"),
	/** 请选择用户 */
	NIS_ASSIGN_MESSAGE_USER_NULL("01008"),
	/**账号为{0},账户性质为{1},不存在!*/
	NIS_ACC_NATURE_NULL("01500"),
	/**账号为{0},账户属性为{1},不存在!*/
	NIS_ACC_ACCATTR_NULL("01501"),
	/**账号为{0},账户类别为定期时，定期期限不能为空!*/
	NIS_ACCTYPE_REGULAR_NULL("01502"),
	/**账号为{0},定期期限为{1},不存在!*/
	NIS_ACC_REGULAR_NULL("01503"),
	/**账号为{0},账户类别为通知时，通知类型不能为空!*/
	NIS_ACCTYPE_NOTICE_NULL("01504"),
	/**账号为{0},通知类型为{1},不存在!*/
	NIS_ACCTYPE_NOTICETYPE_NULL("01505"),
	/**账号为{0},账户类别为协定时，协定金额不能为空!*/
	NIS_ACCTYPE_AGREEMENT_MONEY_NULL("01506"),
	/**账号为{0},账户类别为协定时，协定利率不能为空!*/
	NIS_ACCTYPE_AGREEMENT_INTEREST_RATE_NULL("01507"),
	/**账号为{0},账户类别为协定时，协定合同不能为空!*/
	NIS_ACCTYPE_CONTRACT_AGREEMENT_NULL("01508"),
	/**账号为{0},账户类别为协定时，协定开始日期不能为空!*/
	NIS_ACCTYPE_START_DATE_AGREEMENT_NULL("01509"),
	/**账号为{0},账户类别为协定时，结束日期不能为空!*/
	NIS_ACCTYPE_END_DATE_AGREEMENT_NULL("01510"),
	/** 退单类型编码必须唯一 */
	NIS_BACKTYPE_MESSAGE_BUSTYPE_UNIQUE("01008"),
	/** 失败原因编码重复，请重新输入! */
	NIS_FAILREASON_MESSAGE_FAILCODE_UNIQUE("01008"),
	/** 失败原因名称重复，请重新输入! */
	NIS_FAILREASON_MESSAGE_FAILNAME_UNIQUE("01008"),
	/** 关键字词汇{0}不能重复，请重新输入! */
	NIS_FAILREASON_MESSAGE_KEYWORDS_UNIQUE("01008"),
	/** 业务类别编码必须唯一 */
	NIS_ASSIGN_MESSAGE_BUSTYPE_UNIQUE("01009"),
	/** 业务种类不能为空 */
	NIS_ASSIGN_MESSAGE_BUSTYPE_NULL("01010"),
	/** 业务种类被占用,不能删除 */
	NIS_ASSIGN_MESSAGE_BUSTYPE_NOTNULL("01011"),
	/** "{0}"已被使用,不能取消选中 */
	NIS_ASSIGN_MESSAGE_BUSTYPE_NOTREMOVE("01012"),
	/** 匹配规则{0}重复 */
	NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_REPEAT("01013"),
	/**逻辑条件错误，请检查! */
	NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS("01014"),
	/** 业务种类名称不能为空 */
	NIS_ASSIGN_MESSAGE_BUSTYPENAME_NULL("01101"),
	/**收付款标志不能为空*/
	NIS_ASSIGN_MESSAGE_CDSIGN_NULL("01102"),
	/**计划科目不能为空*/
	NIS_ASSIGN_MESSAGE_ITEMCODE_NULL("01103"),
	/** 付款票据关联信息不能为空 */
	NIS_ASSIGN_MESSAGE_NISBILL_NULL("01104"),
	/** 付款开票登记信息不能为空 */
	NIS_ASSIGN_MESSAGE_ABEMITBILL_NULL("01105"),
	/** 请选择接口代码 */
	NIS_BISBIF_MESSAGE_BIFCODE_NULL("01012"),
	/** 接口代码已维护过业务规则，不允许重复 */
	NIS_BISBIF_MESSAGE_BIFCODE_UNIQUE("01013"),
	/** 请选择单位 */
	NIS_PAY_MESSAGE_CORP_NULL("01014"),
	/** 请选择付款方式 */
	NIS_PAY_MESSAGE_VOUCHERTYPE_NULL("01015"),
	/** 付款账号不能为空 */
	NIS_PAY_MESSAGE_PAYERACC_NULL("01016"),
	/** 付款账号不能为空 */
	NIS_NETBANK_MESSAGE_DOWNACC_NULL("01016"),
	/** 付款账号{0}不存在或用户未授权 */
	NIS_PAY_MESSAGE_PAYERACC_NOT_GRANT("01016"),
	/** 请选择币别 */
	NIS_PAY_MESSAGE_CUR_NULL("01017"),
	/** 请选择业务类别 */
	NIS_PAY_MESSAGE_BUSTYPE_NULL("01018"),
	/** 请选择用途 */
	NIS_PAY_MESSAGE_PURPOSE_NULL("01019"),
	/** 单号（{0}）的付款单,跨行标志为空,请重新编辑后再提交！ */
	NIS_PAY_MESSAGE_DIFBANK_NULL("01036"),
	/** 单号（{0}）的付款单,地域标志为空,请重新编辑后再提交！ */
	NIS_PAY_MESSAGE_AREASIGN_NULL("01037"),
	/** 拆分金额总和必须等于拆分前的付款金额 */
	NIS_PAY_MESSAGE_SPLIT_VALID("01020"),
	/** 系统结算方式为应收票据，无法进行拆分操作 */
	NIS_PAY_MESSAGE_BILL_SPLIT("01021"),
	/** 单据来源为承兑汇票，无法进行拆分操作 */
	NIS_PAY_MESSAGE_BILlFROM_BILL_SPLIT("01022"),
	/** 非合并单据，无法进行撤销合并操作 */
	NIS_PAY_MESSAGE_MERGECANCEL_VALID("01022"),
	/** 拆分单据，无法进行合并操作 */
	NIS_PAY_MESSAGE_SPLIT_NOT_MERGE("01023"),
	/** 合并单据，无法进行合并操作 */
	NIS_PAY_MESSAGE_MERGE_NOT_MERGE("01024"),
	/** 合并单据，无法进行拆分操作 */
	NIS_PAY_MESSAGE_MERGE_NOT_SPLIT("01046"),
	/** 合并单据，无法进行删除操作 */
	NIS_PAY_MESSAGE_MERGE_NOT_DELETE("01047"),
	/** 已有拆分单据提交付款，不允许撤销拆分 */
	NIS_PAY_MESSAGE_SPLITCANCEL_VALID("01025"),
	/** 已有拆分单据提交付款，不允许删除 */
	NIS_PAY_MESSAGE_DELSPLITDTL_VALID("01026"),
	/** 合并单据，单位需一致 */
	NIS_PAY_MESSAGE_CORP_SAME("01031"),
	/** 合并单据，付款方式需一致 */
	NIS_PAY_MESSAGE_VOUCHERTYPE_SAME("01032"),
	/** 合并单据，付款账号需一致 */
	NIS_PAY_MESSAGE_PAYERACC_SAME("01033"),
	/** 合并单据，币别需一致 */
	NIS_PAY_MESSAGE_CUR_SAME("01034"),
	/** 合并单据，收款人需一致 */
	NIS_PAY_MESSAGE_PAYEEACCNAME_SAME("01035"),
	/** 合并单据，收款账号需一致 */
	NIS_PAY_MESSAGE_PAYEEACC_SAME("01036"),
	/** 合并单据，收款开户行需一致 */
	NIS_PAY_MESSAGE_PAYEEBANK_SAME("01037"),
	/** 合并单据，对私标志需一致 */
	NIS_PAY_MESSAGE_FORINDIVIDUAL_SAME("01038"),
	/** 合并单据，加急标志需一致 */
	NIS_PAY_MESSAGE_QUICK_SAME("01039"),
	/** 合并单据，业务类别需一致 */
	NIS_PAY_MESSAGE_BUSTYPE_SAME("01040"),
	/** 合并单据，用途需一致 */
	NIS_PAY_MESSAGE_PURPOSE_SAME("01041"),
	/** 系统结算方式为应收票据，无法进行合并操作 */
	NIS_PAY_MESSAGE_BILL_MERGE("01042"),
	/** 单据来源为承兑汇票，无法进行合并操作 */
	NIS_PAY_MESSAGE_BILlFROM_BILL_MERGE("01043"),
	/** 系统结算方式为转付且记账，合并单据，银行账号需一致 */
	NIS_PAY_MESSAGE_DOWNACC_SAME("01044"),
	/** 合并单据,是否挂预算要一致*/
	NIS_PAY_MESSAGE_IS_FBSBUDGET("01045"),
	/**收款单位不能为空*/
	NIS_CGSBILL_MESSAGE_PAYEECORPID_NULL("01020"),
	/**付款单位不能为空*/
	NIS_CGSBILL_MESSAGE_PAYERCORPID_NULL("01021"),
	/**收款单位和付款单位不能相同*/
	NIS_CGSBILL_MESSAGE_CGSCODE_UNIQUE("01022"),
	/**收款账号不能为空*/
	NIS_CGSBILL_MESSAGE_PAYEEACC_NULL("01023"),
	/**付款账号不能为空*/
	NIS_CGSBILL_MESSAGE_PAYERACC_NULL("01024"),
	/**委托日期不能为空*/
	NIS_CGSBILL_MESSAGE_CONSIGNDATE_NULL("01025"),
	/**收款单位在系统中不存在*/
	NIS_CGSBILL_MESSAGE_PAYEECORPNAME_NULL("01026"),
	/**付款单位在系统中不存在*/
	NIS_CGSBILL_MESSAGE_PAYERCORPNAME_NULL("01027"),
	/**收款帐号与收款单位不匹配*/
	NIS_CGSBILL_MESSAGE_PAYEEACC_NOTMATE("01028"),
	/**委托收款金额不能为空且大于0*/
	NIS_CGSBILL_MESSAGE_MONEY_NULL("01029"),
	/** 批量选择付款账号，单位需一致 */
	NIS_SETPAYERACC_MESSAGE_CORP_SAME("01031"),
	/** 批量选择付款账号，付款方式需一致 */
	NIS_SETPAYERACC_MESSAGE_VOUCHERTYPE_SAME("01032"),
	/** 代发工资,工资册标题不能重复*/
	NIS_WAGE_MESSAGE_WAGETITLE_UNIQUE("01036"),
	/** 业务单据{0}的工资册标题已存在，操作失败！*/
	NIS_WAGE_MESSAGE_WAGETITLE_EXIST("01036"),
	/** 业务单据{0}的工资总额与实发工资总额不一致，操作失败！*/
	NIS_WAGE_MESSAGE_MONEYSUM_MISMATCH("01036"),
	/**代发工资单{0}存在等待结果的工资明细，请待银行确定交易状态之后处理!*/
	NIS_WAGE_MESSAGE_WAGEDETAIL_WAITRESULT("01036"),
	/**代发工资单{0}存在银行待处理的工资明细，请待银行办理完成之后处理!*/
	NIS_WAGE_MESSAGE_WAGEDETAIL_WAITHANDLE("01036"),
	NETBANK_MESSAGE_CONFIRM_BALANCE("01036"),
	NETBANK_MESSAGE_BALANCE("01036"),
	/**记账业务不存在*/
	NIS_ACCBOOK_VOUCHERTYPE_NOTEXISTS("01030"),
	/**{对应网点单位现金账号不存在*/
	NIS_ACCBOOK_1010_NOTEXISTS("01031"),
	/**账号{0}对应网点单位银行活期账号不存在*/
	NIS_ACCBOOK_1020_NOTEXISTS("01032"),
	/**付款账户和收款账户不能一致*/
	NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC("01033"),
	/**单位企业活期活期账号不存在*/
	NIS_ACCBOOK_2120_NOTEXISTS("01034"),
	/**付款单没有挂接预算*/
	NIS_BILLHEAD_ITEMCODE_NOTEXISTS("01035"),
	/**付款单{0}尚未到期望支付日期，确认提前支付？*/
	NIS_MESSAGE_WISHPAYDAY_PASS("01036"),
	/**业务存在重复提交的风险，请刷新页面后重试！*/
	NIS_MESSAGE_REPEAT_SUBMIT("01037"),
	/** 账户属性被占用 */
	NIS_ACCNATURE_MESSAGE_NATUREID_USE("02001"),
	/** 账户性质被占用 */
	NIS_ACCATTR_MESSAGE_ATTRIBUTEID_USE("02002"),
	/** 付款方式维护被占用，不能删除！ */
	NIS_ASSIGN_MESSAGE_VOUCHERID_USE("02003"),
	/** 付款方式维护被占用，不能设置无效！ */
	NIS_ASSIGN_MESSAGE_VOUCHERID_USE_NO_VALIDSIGN("02003"),
	/** {0}业务种类被占用，不能删除！ */
	NIS_ASSIGN_MESSAGE_BUSTYPEID_USE("02004"),
	/** {0}业务种类被占用，不能无效！  */
	NIS_ASSIGN_MESSAGE_BUSTYPEID_USE_NO_VALIDSIGN("02005"),
	/** 系统未维护相关业务的付款方式 */
	NIS_PAY_MESSAGE_PAYTYPE_NULL("02006"),
	/** 付款金额和票据金额不一致，不允许提交！ */
	NIS_NETBANK_MESSAGE_BILLMONEY_MISMATCH("02006"),
	/** 请重新选择票据！ */
	NIS_NETBANK_MESSAGE_AGAIN_SELECT_BILL("02012"),
	/** 该单据对应的指令为非打回状态，不能重复产生指令 */
	NIS_BISEXC_MESSAGE_EXIST("02005"),
	/**不存在交易失败的费用报销明细，打回失败*/
	NIS_EXPENSE_MESSAGE_FAILUREDTL_NULL("02007"),
	/** 账号{0}没有维护父账号，无法生成付款指令*/
	NIS_PAY_MESSAGE_PARENTACC_NULL("02008"),
	/** 付款账号{0}没有维护接口代码*/
	NIS_WAGE_MESSAGE_BIFCODE_NULL("02009"),
	/** 接口代码{0}{1}没有维护业务规则，请联系管理员*/
	NIS_WAGE_MESSAGE_BISBIFRULE_NULL("02010"),
	/** 相同账号、金额、用途的工资明细不能重复*/
	NIS_WAGE_MESSAGE_BANKACC_REPEAT("02011"),
	/** 账号是否存在银行账户父子关系*/
	NIS_BANKACC_MESSAGE_QUOTE_PRENTBANKACC("03001"),
	/** 账号是否存在内部账户父子关系*/
	NIS_BANKACC_MESSAGE_QUOTE_FCACC("03002"),
	/** 该账号已发生业务，不可删除*/
	NIS_BANKACC_MESSAGE_QUOTE_BUSINESS("03003"),
	/** 该账号已被引用，不可删除*/
	NIS_BANKACC_MESSAGE_QUOTE_ENTITYBANKACC("03003"),
	/** 付款方式维护被占用 */
	NIS_ASSIGN_MESSAGE_IS_SYSTEM("03004"),
	/** 账户余额不为零，不能注销 */
	NIS_BANKACC_MESSAGE_BAL_NOT_ZERO("03005"),
	/** 报销单未挂接计划科目 */
	NIS_EXPENSE_DETAIL_MESSAGE_EXPFBSIN_INPUT("04001"),
	/** 工资单未挂接计划科目 */
	NIS_WAGE_MESSAGE_BUDGETSEL("04002"),
	/** 存在已发送的同收款账号、相同期望支付日期、相同金额的指令，确定要发送吗？ */
	BIS_EXC_MESSAGE_SEND_REPAY("05001"),
	/** 选择的数据中，存在已发送的同收款账号、相同期望支付日期、相同金额的指令，确定要发送吗？*/
	BIS_EXC_MESSAGE_SEND_BATCHREPAY("05001"),
	/** 选择的数据中，存在同收款账号、相同期望支付日期、相同金额的指令，确定要发送吗？*/
	BIS_EXC_MESSAGE_SEND_BATCHREPAY_WAIT_FILL("05001"),
	/** 指令{0}的关键信息被篡改，请确认！ */
	BIS_EXC_MESSAGE_PAYINFO_CHANGED("05003"),
	/**	指令编号{0}，付款账号信息不存在或已失效！*/
	BIS_EXC_MESSAGE_PAYERACC_ISNOTEXIST("05004"),
	/**	指令编号{0}，收款账号信息不存在或已失效！*/
	BIS_EXC_MESSAGE_PAYEEACC_ISNOTEXIST("05005"),
	/** 选择的数据中，期望支付日期小于当前时间 */
	NIS_PAY_MESSAGE_SUBMIT_WISHPAYDAY_LG_CURDATE("01014"),
	/** 指令编号{0}已存在 */
	BIS_EXC_MESSAGE_VOUCHERNO_REPAY("05001"),
	/** 指令{0}数据异常，操作失败，请联系管理员 */
	BIS_EXC_MESSAGE_EXCEPTION_DATA("05001"),
	/** 业务单据{0}的状态已经发生改变，请刷新页面后重试 */
	NIS_PAY_MESSAGE_STATUS_CHANGE("05002"),
	/** 业务单据{0}来源不为付款池，无法退回！ */
	NIS_DATA_SOURCE_NOT_POOL("05019"),
	/** 说明字符长度过长 */
	NIS_PAY_MESSAGE_RMK_TOOLONG("05002"),
	/** 业务单据{0}正在被操作，请稍后刷新页面后重试 */
	NIS_PAY_MESSAGE_OPTION_DOING("05002"),
	/** 业务单据{0}的状态不是暂存和退回，不能再做操作 */
	NIS_PAY_MESSAGE_STATUS_CAN("05002"),
	/** 付款方式发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_VOUCHERTYPE_CHANGE("05002"),
	/** 付款账号发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_PAYERACC_CHANGE("05002"),
	/** 付款金额发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_PAYMONEY_CHANGE("05002"),
	/** 收款账号发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_PAYEEACC_CHANGE("05002"),
	/** 收款账号名称发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_PAYEEACCNAME_CHANGE("05002"),
	/** 收款开户行名称发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_PAYEEBANK_CHANGE("05002"),
	/** 用途发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_PURPOSE_CHANGE("05002"),
	/** 是否对私付款标识发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_ISFORINDIVIDUAL_CHANGE("05002"),
	/** 票据类型发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_BILLTYPE_CHANGE("05002"),
	/** 票据流通介质发生改变，由{0}调整为{1}，请确认！ */
	NIS_PAY_MESSAGE_BILLFLAG_CHANGE("05002"),
	/** 业务单据{0}的存在重复的可疑风险，请确认 */
	NIS_PAY_MESSAGE_SERIALNOERP_REPEAT("05002"),
	/** 业务单据{0}属部分明细失败，无法进行该操作，请打回单据 */
	NIS_PAY_MESSAGE_STATUS_FAIL_DEAL("05002"),
	/** 代发工资单{0}属部分明细失败，无法进行指令重发，请前往代发工资结果异常处理菜单打回单据 */
	NIS_WAGE_MESSAGE_STATUS_FAIL_DEAL("05002"),
	/** 付款单{0}的付款金额与对私付款明细总金额不一致，请确认！ */
	NIS_PAY_MESSAGE_PAYMONEY_MISMATCH("05001"),
	/**资金池代码必须唯一*/
	POOL_POOLCODE_MESSAGE_POOLCODE_UNIQUE("00001"),
	/**资金池名称必须唯一*/
	POOL_POOLNAME_MESSAGE_POOLNAME_UNIQUE("00002"),
	/**资金池内部账户余额必须为0，且不能存在未结清的利息*/
	POOL_ACCOUNT_MESSAGE_BALANCE_ZERO("00003"),
	/**资金池代码不允许与网点代码一致*/
	POOL_POOLCODE_MESSAGE_NET_SAME("00004"),
	/**该资金池下已经存在相同子单位*/
	POOL_POOLCODE_MESSAGE_CHILDCORP_SAME("00005"),
	/**该子单位已经存在相同单位的资金池*/
	POOL_POOLCODE_MESSAGE_CORP_SAME("00006"),
	/**ErpID为{0}的付款单已存在!*/
	ERP_PAYMENT_DOCUMENT_ALREADY_EXISTS("00007"),
	/**ErpID为{0}的付款单,不存在!*/
	ERP_PAYMENT_DOCUMENT_NO_EXISTS("00008"),
	/**ErpID为{0}的付款单已处理，不能删除!*/
	ERP_PAYMENT_DOCUMENT_NO_DEL("00009"),
	/**该资金池下面有主账号，不能删除!*/
	FOREIGN_FEXCORPPOOL_MESSAGE_BANKACC_NOTNULL("00010"),
	/**该账号下面有成员账号，不能删除!*/
	FOREIGN_FEXCORPPOOL_MESSAGE_CHILDACC_NOTNULL("00011"),
	/**策略代码重复，请重新输入! */
	BISPAYPOLICY_MESSAGE_POLICYCODE_UNIQUE("00001"),
	/**策略名称重复，请重新输入! */
	BISPAYPOLICY_MESSAGE_POLICYNAME_UNIQUE("00002"),
	/**相同单位相同币种相同收款人，不允许存在同时启用的自动支付策略! */
	BISPAYPOLICY_MESSAGE_PAYEE_SAME("00003"),
	/**业务种类为{0}的计划科目为空！*/
	DTL_BUDGET_RULE_BUSTYPE_NO_ITEMCODE("00004"),
	BIS_CHECK_REASON_MESSAGE_CHECK_ERROR("00094"),
	//已存在相同的原因代码或原因名称
	BIS_CHECK_REASON_MESSAGE_MTB_ERROR("00096"),
	//对账原因已被使用，不可删除！
	BIS_CHECK_REASON_MESSAGE_BDT_ERROR("00097"),
	/**	账号[{0}]，账户信息不存在或已失效！*/
	NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST("00006"),
	/**	工资单[{0}]工资总额与实发工资总额不一致！*/
	NIS_NETBANK_MESSAGE_SUMWAGES_MISMATCH("00008"),
	/**银行类别不能为空*/
	NIS_NETBANK_MESSAGE_BANKTYPENAME_NULL("00005"),
	/**没有开启待付款池，不能提交*/
	COMMON_MESSAGE_PENDINGPAYMENTPOOL_NOT("00011"),
	/**自动复核人不存在*/
	AUTO_CHECK_USER_NOTEXIST("00012"),
	/**自动记账人不存在*/
	AUTO_ACCOUNT_USER_NOTEXIST("00013"),
	/**内部结转的自动复核人不存在*/
	CARRAY_AUTO_CHECK_USER_NOTEXIST("00014"),
	/** 该用途编码已存在，请修改！ */
	NETBANK_MESSAGE_BANKACCUSE_UNIQUE("06001"),
	/** 该用途已经存在绑定账号，不能删除，请确认！ */
	NETBANK_MESSAGE_BANKACCUSE_USE("06002"),

	/** 同单位、同币种、同余额日期的归集余额数据需唯一！ */
	NETBANK_MESSAGE_BISCORPPOOLBAL_REPEAT("06002"),
	/** 同单位、同币种的透支金额数据需唯一！ */
	NETBANK_MESSAGE_BISCORPPOOLOVERDRAFT_REPEAT("06002"),
	/** 关联方编码【{0}】已存在！ */
	NETBANK_MESSAGE_RELATEDPARTY_UNIQUE("00015"),
	/**该单已经审核，请刷新页面数据！*/
	BILL_CHECKED("05002");
	NetbankMessageEnum(String code) {
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
