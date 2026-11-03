package com.bytter.biz.finacial.api.enums.outaccbook;
import com.bytter.core.enums.IMessageEnum;
/**
 *表外财务信息枚举
 * @author caozx
 * @since 2017-7-5
 */
public enum OutAccbookMessageEnum implements IMessageEnum {
	
	/**
	 * 08 表外财务
	 * 
	 */
	
	/**请选择记账日期*/
	FIN_OUTBOOK_TALLY_TALLYDATE_EMPTY("08001"),
	/**请输入记账信息*/
	FIN_OUTBOOK_TALLY_TALLYMESSAGE_EMPTY("08003"),
	/**请输入摘要*/
	FIN_OUTBOOK_TALLY_SUMMARYCONTENT_EMPTY("08003"),
	/**请输入账号*/
	FIN_OUTBOOK_TALLY_ACCOUNT_EMPTY("08005"),
	/**收入与支出不能同时为0*/
	FIN_OUTBOOK_TALLY_DEBIT_CERDIT_ZERO("08005"),
	/**收入与支出不能同时不为0*/
	FIN_OUTBOOK_TALLY_DEBIT_CERDIT_NOT_ZERO("08007"),
	/**多笔记账明细的收入、支出方向要保持一致*/
	FIN_OUTBOOK_TALLY_DIRECTION_DIFFER("08009"),
	/**未查询到xxx账号信息*/
	FIN_OUTBOOK_TALLY_ACCOUNT_NONENTITY("08011"),
	/**账号xxx已销户账*/
	FIN_OUTBOOK_TALLY_ACCOUNT_CANCEL("08013"),
	/**未查询到xxx的所属网点*/
	FIN_OUTBOOK_TALLY_ACCOUNT_NET_NONENTITY("08015"),
	/**记账日期必须大于账户启用日期*/
	FIN_OUTBOOK_TALLY_TALLYDATE_LE_STARTDATE("08017"),
	/**全部账号必须是同一网点下的账号*/
	FIN_OUTBOOK_TALLY_NOT_NOE_NET("08019"),
	/**请输入记账明细*/
	FIN_OUTBOOK_TALLY_DTL("08021"),
	/**不能操作此数据，数据已被其他用户修改*/
	FIN_OUTBOOK_TALLY_DATE_UPDATE("08021"),
	/**流水号xxx不能操作此数据，数据已被其他用户修改*/
	FIN_OUTBOOK_TALLY_DATE_UPDATE_BY_ORDERNO("08021"),
	;
	OutAccbookMessageEnum(String code) {
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
