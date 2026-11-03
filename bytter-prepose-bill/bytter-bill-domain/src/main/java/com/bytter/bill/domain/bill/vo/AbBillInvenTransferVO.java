package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
/**   
 * @Title: VO
 * @Description: 票据移存VO
 * @author zhenghj
 * @date 2017-06-29 14:42:25
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillInvenTransferVO extends AbAccrecordsVO implements Serializable,IBusinessKey{
	
	/**票据主表id*/
	private String vouId;
	/**票据从表id*/
	private String viceId;
	/**流水表id*/
	private String accId;
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="V.bill_type")
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	private String  billTypeName;
	/**流通介质：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="V.bill_flag")
	// @Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
	private Integer billFlag;
	private String billFlagName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="C.id")
	@Corp(inject="corpName")
	private String corpId;
	@BtCompositeSearch(fieldName="C.code")
	private String corpName;
	/**持票单位账号 */
	private String bankAcc;
	/**持票单位银行*/
	private String bankCode;
	/**票据单位银行名称*/
	private String bankName;
	/**出票人*/
	@BtCompositeSearch(fieldName="V.payer")
	private String payer;
	/**出票人账号*/
	private String payeracc;
	/**出票人开户银行名称*/
	private String payerBankName;
	/**出票人开户银行行号 */
	private String payerbankno;
	/**承兑人*/
	@BtCompositeSearch(fieldName="V.acceptor")
	private String acceptor;
	/**承兑人账号 */
	private String acceptoracc;
	/**承兑人开户行名称 */
	private String acceptorBankName;
	/**承兑人开户行行号 */
	private String acceptorbankno;
	/**票据号码*/
	@BtCompositeSearch(fieldName="V.orig_no")
	private String origNo;
	/**票据金额*/
	@BtCompositeSearch(fieldName="V.bill_money")
	private java.math.BigDecimal billMoney;
	/**出票日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="V.start_date")
	private java.util.Date startDate;
	/**到期日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="V.end_date")
	private java.util.Date endDate;
	/**申请日期 */
	private java.util.Date applyDate;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="V.payee")
	private String payee;
	/**收票人账号*/
	private String payeeacc;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**票据状态*/
	@BtCompositeSearch(fieldName="S.status")
	// @Dict(dictGroup = "AB_BILL_ACCRECORDS_TRANSFER_STATUS", inject = "statusName")
	private Integer status;
	private String statusName;
	/**所属库存*/
	@BtCompositeSearch(fieldName="P.enter_stock")
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
	private String enterStockName;
	/**变更库存*/
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockNewName")
	private String enterStockNew;
	private String enterStockNewName;
	/**票据来源*/
	@BtCompositeSearch(fieldName="P.bill_source")
	// @Dict(dictGroup = "AB_BILL_SOURCE", inject = "billSourceName")
	private String billSource;
	private String billSourceName;
	/**不得转让标记 */
	// @Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
	private String banendrsmtmk;
	private String banendrsmtmkName;
	/**承兑合同号 */
	private String acontractNo;
	/**发票号 */
	private String ainvcnb;
	/**前手*/
	private String cnAccrecords;
	/**摘要*/
	private String abs;
	/**备注*/
	private String rmk;
	/**票据主表信息*/
	private AbVoucherinfo abVoucherInfo;
	/**票据从表信息*/
	private AbVoucherinfoVice abVoucherinfoVice;
	/**流水表*/
	private AbAccrecords abAccrecords;
	/**业务办理日期*/
	private String businessdate;
	/**是否隐藏多选框 1为是*/
	private String checkboxHide;
	private String businessKey;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="p.APPLY_CORP_ID")
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	@BtCompositeSearch(fieldName="p.apply_corp_name")
	private String applyCorpName;
	/**申请单位代码*/
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="p.BUS_CORP_ID")
	private String busCorpId;
	/** 业务单位名称 */
	private String busCorpName;
	/** 业务人员 */
	@BtCompositeSearch(fieldName="p.BUS_PERSONNEL")
	private String busPersonnel;

	@BtCompositeSearch(fieldName="acc.BANK_TYPE_ID")
	private String bankTypeId;

	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
			//审批列表查询
			return "0";
		}
		if(this.getStatus()!=null
				&&(this.getStatus().equals(AccebillConstant.AB_BILL_BUS_STATUS11)
				|| this.getStatus().equals(AccebillConstant.AB_BILL_BUS_STATUS94))){
			return "1";
		}
		return checkboxHide;
	}
}
