package com.bytter.biz.finacial.api.enums.interest;
import com.bytter.core.enums.IMessageEnum;
/**
 * 利息提示信息枚举类
 * @author liyh
 * @since 2017-7-6
 */
public enum InterestMessageEnum implements IMessageEnum{
	/**
	 * 03 利息管理
	 */
	/**应还利息不能为零*/
	FIN_LOANREPAY_INTERET_ISZERO("03001"),
	/**本计息周期内没有可以计息的数据，不能计息!*/
	FIN_INTEREST_INTERESTNODATA("03002"),
	/**单位{0},币别{1},没有开设{2}账户。*/
	FIN_INTEREST_CORPCURNOTACC("03003"),
	/**单位{0}下的放款账户{1},没有对应的扣息活期账号！*/
	FIN_INTEREST_CORPLOANNOTACCFORDEDUCT("03004"),
	/**单位{0}下的放款账户{1},没有对应的{2}账号！*/
	FIN_INTEREST_CORPLOANNOTACC("03005"),
	/**放款账户{0}没有对应的表外待收利息账户*/
	FIN_INTEREST_LOANNOTOUTACC("03006"),
	/**处理凭证失败*/
	FIN_INTEREST_PROCESSVOUCHERFAILURE("03007"),
	/**不满足记账凭证条件:{0}*/
	FIN_INTEREST_VOUCHERNOTPASS("03008"),
	/**没有可以{0}的数据*/
	FIN_INTEREST_NOTCHECKORBACKDATA("03009"),
	/**没有可操作的网点，请分配用户资源权限*/
	FIN_INTEREST_NETNOTAUTH("030010"),
	/**网点[{0}],日期[{1}]已日结，请重新选择记账日期*/
	FIN_LOANREPAY_INTERET_NETISDAYCLOSE("030011"),
	/**账号余额不足，将过滤部分利息，是否继续？详细信息：{0}*/
	FIN_INTEREST_NOTENOUGH_BALANCE("030012"),
	/**本资金池未设置{0}的记账规则*/
	FIN_INTEREST_NOTSET_SUBJECT("030013"),
	/**本资金池未开设{0}的内部账号*/
	FIN_INTEREST_NOTOPEN_SUBJECTACC("030014"),
	/**用户未分配该菜单资源权限*/
	FIN_INTEREST_NOT_PERMISSION("030015"),
	/**没有可以{0}的数据,请检查日结日期！*/
	FIN_INTEREST_NOTCHECKORBACKDATA_INTERESTDATE("030016"),
	/**
	 *计息日期不能为空！
	 */
	FIN_FCINTERESTDTL_INTERESTTIME_NOTNULL("030017"),
	/**
	 *账号不能为空
	 */
	FIN_FCINTERESTDTL_ACCOUNT_NOTNULL("030018"),
	/**
	 *利息类型不能为空
	 */
	FIN_FCINTERESTDTL_INTERESTTYPE_NOTNULL("030019"),
	/**
	 *起息日期不能为空
	 */
	FIN_FCINTERESTDTL_INTERESTSTARTDATE_NOTNULL("030020"),
	/**
	 *结息日期不能为空
	 */
	FIN_FCINTERESTDTL_BLNINSTDATE_NOTNULL("030021"),
	/**
	 *计息天数不能为空
	 */
	FIN_FCINTERESTDTL_DAYS_NOTNULL("030022"),
	/**
	 *计息基数不能为空
	 */
	FIN_FCINTERESTDTL_CUMULATION_NOTNULL("030023"),
	/**
	 *利率不能为空
	 */
	FIN_FCINTERESTDTL_INTERESTRATE_NOTNULL("030024"),
	/**
	 *利息不能为空
	 */
	FIN_FCINTERESTDTL_INTEREST_NOTNULL("030025"),
	/**
	 *计息标志不能为空
	 */
	FIN_FCINTERESTDTL_INTERESTSIGN_NOTNULL("030026"),
	/**
	 *经办人不能为空
	 */
	FIN_FCINTERESTDTL_OPERATORCODE_NOTNULL("030027"),
	/**
	 *币种不能为空
	 */
	FIN_FCINTERESTDTL_CURCODE_NOTNULL("030028"),
	/**单位{0},币别{1},没有开设{2}银行代码为｛3｝账户。*/
	FININTEREST_CORPCURBANKNOTACC("030029"),
	/**此银行行号:{0},未维护银行类型,请维护!*/
	FIN_FCINTERESTDTL_BTBANKTYPE_NULL("030030"),
    /**金额应大于0*/
    FIN_FCINTERESTDTL_MONEY_ZERO("030031"),
    /**还款金额应大于0*/
    FIN_FCINTERESTDTL_REPAYMONEY_ZERO("030032"),
	/** 此单位没有可计息的可用账户*/
	FIN_FCINTERESTDTL_ACCOUNTS_AVAILABLE_NULL("030035"),
	/**
	 * 一个银行只能一条规则
	 */
	FIN_FCINTERESTDTLRULE_BANKTYPE_UNIQUE("030036"),
	;
	private InterestMessageEnum(String code) {
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
