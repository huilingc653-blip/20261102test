package com.bytter.system.enums;
import com.bytter.core.enums.IMessageEnum;
public enum InnerAccountMessageEnum implements IMessageEnum {
	/** 账户：{0}已日结或已有发生数不能提交 */
	INNER_ACCOUNT_MESSAGE_BLAND_CHANGE("01001"),
	/** {0}日得借贷不平衡 */
	INNER_ACCOUNT_MESSAGE_DATE_IMBALANCE("01002"),
	/** 账户：{0}记录已被其他用户修改，请重新查询页面。 */
	INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS("01003"),
	/**  账户：{0} 余额不为0，不能注销。 */
//	INNER_ACCOUNT_MESSAGE_NOT_CANCEL("01004"),
	INNER_ACCOUNT_MESSAGE_NOT_ZERO("01004"),
	INNER_ACCOUNT_MESSAGE_FREEZE_NOTIFYNO_EXISTS("01005"),
	/**  开户失败，账号启用日期 :{0}，网点的已日结日期:{1},启用日期必须大于当前网点已日结日期 */
	INNER_ACCOUNT_MESSAGE_OPENTIME_TALLYTIME_COMPARE("01006"),
	/**  开户失败，账号启用日期 :{0}，资金池的已日结日期:{1},启用日期必须大于当前网点已日结日期 */
	INNER_ACCOUNT_MESSAGE_OPENTIME_TALLYTIME_POOL_COMPARE("01008"),
	/** 销户失败，账号：{0}，记账日期：{1}，启用日期 :{2}，记账日期不能小于账户的启用日期 */
	INNER_ACCOUNT_MESSAGE_ACCSTARTDATE_TALLYTIME_COMPARE("01026"),
	/** 单位：{0} ，没有可用的利息支出企业活期账号，无法转出余额到其他账号，因而无法注销！*/
	INNER_ACCOUNT_MESSAGE_NOT_EXSITS_LIXIACCOUNT("01027"),
	/**  重复提交，此账号已存在! */
	INNER_ACCOUNT_MESSAGE_UNION("01007"),
	/**  账户：{0},日期：{1} 可能被使用，余额表中已存在。 */
	INNER_ACCOUNT_MESSAGE_BALANCE_NOT_EXISTS("01008"),
	/**  自动开户生成账号问题,请重新提交! */
	INNER_ACCOUNT_MESSAGE_AUTO_PROCESS("01009"),
	/** 账户 {0} 存在未复核的初始化数据,不能销户*/
	INNER_ACCOUNT_MESSAGE_ACCOUNT_INIT_CHECK_EXISTS("01010"),
	/** 账号 {0} 存在未结息的利息*/
	INNER_ACCOUNT_MESSAGE_CAL_TELLER_MONEY_INTEREST("01011"),
	/** 账号{0}账号存在在途利息";*/
	INNER_ACCOUNT_MESSAGE_EXIST_UNOVER_INTEREST("01012"),
	/** 账号{0}存在未冲预提,不能销户*/
	INNER_ACCOUNT_MESSAGE_EXIST_UNRUSH("01012"),
	/** 账号{0}在业务付款申请单有未确认的申请,不能销户*/
	INNER_ACCOUNT_MESSAGE_PAY_ORDER_CONFIRM("01013"),
	/** 账号{0}存在未复核的账,不能销户*/
	INNER_ACCOUNT_MESSAGE_EXISTS_UNCHECK_BOOK("01014"),
	/** 账号{0}是被其他的利息上账（扣息）、手续费引用,不能销户*/
	INNER_ACCOUNT_MESSAGE_OTHER_ACCOUNT_QUOTE("01015"),
	/**  账号{0}有未收的手续费,不能销户*/
	INNER_ACCOUNT_MESSAGE_NOT_RECEIVED("01016"),
	/** 账号{0}有未收的应收利息,不能销户*/
	INNER_ACCOUNT_MESSAGE_NOT_RECEIVED_INTEREST("01017"),
	/** 账号对应的放款账号{0}余额不为0*/
	INNER_ACCOUNT_MESSAGE_NOT_ZERO_FOR_DEDUCT("01018"),
	/** 账号对应的放款账号{0}存在未结息的利息*/
	INNER_ACCOUNT_MESSAGE_NOT_TELLER_MONEY_INTEREST("01019"),
	/** 账号对应的放款账号{0}存在在途利息*/
	INNER_ACCOUNT_MESSAGE_EXIST_UNOVER_INTEREST_FOR_DEDUCT("01020"),
	/** 账号对应的放款账号{0}存在未结清的手续费*/
	INNER_ACCOUNT_MESSAGE_NOT_RECEIVED_FOR_DEDUCT("01021"),
	/** 账号对应的放款账号{0}存在未复合的账*/
	INNER_ACCOUNT_MESSAGE_EXISTS_UNCHECK_BOOK_FOR_DEDUCT("01022"),
	/** 账号对应的放款账号{0}应收预期利息余额不为0*/
	INNER_ACCOUNT_MESSAGE_NOT_RECEIVED_INTEREST_FOR_DEDUCT("01023"),
	/** 账号对应的放款账号{0}存在未冲预提*/
	INNER_ACCOUNT_MESSAGE_EXIST_UNRUSH_FOR_DEDUCT("01024"),
	/** 账号{0}有关联的银行账号存在,不能销户*/
	INNER_ACCOUNT_MESSAGE_EXIST_BANKACC("01025"),
	/** 账号{0} 没有实时金额，存在异常账号数据，请联系管理员！*/
	INNER_ACCOUNT_MESSAGE_NOT_EXIST_ONLINE("01026"),
	/** 账号{0}有关联的计息账号存在，不能销户！*/
	INNER_ACCOUNT_MESSAGE_EXIST_EXPPAY("01027"),
	/** 账号{0}联机余额更新失败！*/
	INNER_ACCOUNT_MESSAGE_ONLINE_BALANCE("01028");
	InnerAccountMessageEnum(String code) {
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
