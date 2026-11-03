package com.bytter.biz.finacial.api.enums.accbook;
import com.bytter.core.enums.IMessageEnum;
/**
 * 账务提示信息枚举类
 * @author liyh
 * @since 2017-7-6
 */
public enum AccbookMessageEnum implements IMessageEnum {
	/**
	 * 05 柜台账务
	 */
	/**费用计提未维护对应科目(5020手续费收入)的手续费账号*/
	FIN_BOOK_CHARGE_ACCOUNT_NO("05001"),
	/**费用计提对应科目(5020手续费收入)的手续费账号存在多个*/
	FIN_BOOK_CHARGE_ACCOUNT_MANY("05002"),
	/**至少选择一条数据*/
	FIN_BOOK_CHARGE_CHOOSEONE("05003"),
	/**请选择未计提手续费记录进行计提*/
	FIN_BOOK_CHARGE_TALLYNO("05004"),
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
	/**账户{0}不能同时在借贷方都有数据*/
	FIN_BOOK_TALLY_ACCOUNT_BOTHSIDE("05012"),
	/**账号的币别必须相同*/
	FIN_BOOK_TALLY_ACCOUNT_SAMECUR("05013"),
	/**账号必须属于同一网点*/
	FIN_BOOK_TALLY_ACCOUNT_SAMENET("05014"),
	/**借贷不平衡*/
	FIN_BOOK_TALLY_MONEY_BALANCE("05015"),
	/**借贷金额不能同时为空*/
	FIN_BOOK_TALLY_MONEY_BOTH_NO("05016"),
	/**借贷金额不能同时为零*/
	FIN_BOOK_TALLY_MONEY_BOTH_ZERO("05017"),
	/**起息日期不能大于记账日期*/
	FIN_BOOK_TALLY_INTERESTSTART_AFTER("05018"),
	/**凭证起息日期必须大于账户{0}启用日期*/
	FIN_BOOK_TALLY_START_AFTER_ACC("05019"),
	/**记账日期必须大于账户{0}启用日期*/
	FIN_BOOK_TALLY_TALLYD_AFTER_ACC("05020"),
	/**日期{0}已日结，无法生成账务*/
	FIN_BOOK_TALLY_RUSHD_AFTER_CLOSED("05021"),
	/**日期{0}已日结，无法生成账务*/
	FIN_BOOK_TALLY_TALLYD_AFTER_CLOSED("05022"),
	/**账户{0}联机余额不存在，请联系管理员开户*/
	FIN_BOOK_TALLY_LEDGER_NO("05023"),
	/**账号{0}已透支*/
	FIN_BOOK_TALLY_ACC_OVERDRAFT("05024"),
	/**账号{0}已透支，且超过受限额度*/
	FIN_BOOK_TALLY_ACC_OVERDRAFT_MONEY("05025"),
	/**账务数据不存在，请确认是否已作废*/
	FIN_BOOK_NOTEXIST("05026"),
	/**冲账原因不能为空*/
	FIN_BOOK_TALLY_DES_REASON_NO("05027"),
	/**备注不能为空*/
	FIN_BOOK_TALLY_RMK_NO("05028"),
	/**请录入账务明细*/
	FIN_BOOK_TALLY_DTL_NULL("05029"),
	/**账户{0}不能在借方存在多条分录*/
	FIN_BOOK_TALLY_ACCOUNT_DREPEAT("05030"),
	/**账户{0}不能在贷方存在多条分录*/
	FIN_BOOK_TALLY_ACCOUNT_CREPEAT("05031"),
	/**未设置结算中心自动记账人参数，无法记账*/
	FIN_BOOK_TALLY_AUTOACCOUNTER_NULL("05032"),
	/**未设置结算中心{0}自动记账人参数，无法记账*/
	FIN_BOOK_TALLY_AUTOACCOUNTER_NETNULL("05033"),
	/**未设置结算中心自动复核人参数，无法复核*/
	FIN_BOOK_TALLY_AUTOCHECKER_NULL("05034"),
	/**未设置结算中心{0}自动复核人参数，无法复核*/
	FIN_BOOK_TALLY_AUTOCHECKER_NETNULL("05035"),
	/**记账参数用户{0}不存在，请核对*/
	FIN_BOOK_TALLY_USER_NOTEXIST("05036"),
	
	/**业务凭证不能作废*/
	FIN_BOOK_INVALID_NOTALLOW("05037"),
	/**该笔原始单据的付款指令正处于正常状态，不能作废凭证*/
	FIN_BOOK_INVALID_BISEXC_NORMAL("05038"),
	/**业务凭证不能冲账*/
	FIN_BOOK_RUSH_NOTALLOW("05039"),
	/**该笔原始单据的付款指令正处于正常状态，不能冲账凭证*/
	FIN_BOOK_RUSH_BISEXC_NORMAL("05040"),
	
	/**冲账调用需传递全局业务ID，冲账失败*/
	FIN_BOOK_RUSH_BUSGLOBALID_NULL("05041"),
	/**手续费记账规则明细数据异常*/
	FIN_BOOK_CHARGE_ENTRYDEFINE_NULL("05042"),
	/**账户{0}为冻结只收不支账户*/
	FIN_BOOK_TALLY_ACCOUNT_FREEZE("05043"),
	/**凭证的状态已经发生改变，请刷新页面后重试*/
	FIN_BOOK_TALLY_VOUCHER_CHANGE("05044"),
	/**账号信息异常，请刷新重试**/
	FIN_BOOK_TALLY_ACCOUNT_ERRO("05045"),
    /**未获取结算中心日结日期，请联系管理员重新初始化结算中心*/
    FIN_BOOK_NONET_CLOSEDATE("05046"),
    /**未获取资金池主体日结日期，请联系管理员重新初始化资金池主体*/
    FIN_BOOK_NOPOOL_CLOSEDATE("05047"),
	/**未设置资金池自动复核人参数，无法复核*/
	FIN_BOOK_POOL_TALLY_AUTOCHECKER_NULL("05048"),
	/**账号必须属于同一资金池*/
	FIN_BOOK_POOL_TALLY_ACCOUNT_SAMENET("05049"),
	/**该网点已月结，请反月结后在设置现金流量项目*/
	FIN_BOOK_IS_CLOSE_MONTH("05050")
	;
	AccbookMessageEnum(String code) {
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
