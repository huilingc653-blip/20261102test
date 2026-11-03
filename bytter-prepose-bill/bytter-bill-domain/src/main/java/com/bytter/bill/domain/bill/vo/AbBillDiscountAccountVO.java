package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
/**   
 * @Title: VO
 * @Description: 贴现到账VO
 * @author kongq
 * @date 2017-7-21 13:43:13
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillDiscountAccountVO extends AbDiscountVO implements Serializable,IBusinessKey{
	
	private String id;
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**票据主表id*/
	private String vouId;
	/**票据从表id*/
	private String viceId;
	/**流水表id*/
	private String accId;
	private String businessKey;
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="V.bill_type")
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	@BtCompositeSearch(fieldName="V.bill_type")
	private String  billTypeName;
	/**流通介质：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="V.bill_flag")
	// @Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
	private Integer billFlag;
	@BtCompositeSearch(fieldName="V.bill_flag")
	private String billFlagName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="P.CORP_ID")
	// @Corp(inject="corpName")
	private String corpId;
	@BtCompositeSearch(fieldName="P.CORP_NAME")
	private String corpName;
	/**持票单位账号 */
	private String bankAcc;
	/**持票单位银行*/
	private String bankCode;
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
	@BtCompositeSearch(fieldName="V.start_date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date startDate;
	/**到期日期*/
	@BtCompositeSearch(fieldName="V.end_date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date endDate;
	/**申请日期 */
	private java.util.Date applyDate;
	/**业务办理日期 */
	@BtCompositeSearch(fieldName="S.BUSINESS_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date businessDate;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="S.signerok")
	private String signerok;
	/**收票人账号*/
	private String payeeacc;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**票据状态*/
	@BtCompositeSearch(fieldName="S.brief_id")
	// @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
	private String briefId;
	@BtCompositeSearch(fieldName="S.brief_id")
	private String briefIdName;
	/**票据业务状态*/
	@BtCompositeSearch(fieldName="S.status")
	// @Dict(dictGroup = "AB_ABBILLDISCOUNT_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="S.status")
	private String statusName;
	/**商票贴现是否释放状态*/
	@BtCompositeSearch(fieldName="p.rat_model")
	// @Dict(dictGroup = "AB_ABBILLDISCOUNT_RELEASE_STATUS", inject = "releaseStateName")
	private String releaseState;
	@BtCompositeSearch(fieldName="p.rat_model")
	private String releaseStateName;
	/**所属库存*/
	@BtCompositeSearch(fieldName="P.enter_stock")
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
	@BtCompositeSearch(fieldName="P.enter_stock")
	private String enterStockName;
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
	/**票据主表vouIds*/
	private String vouIds;
	/**票据从表viceIds*/
	private String viceIds;
	/**流水表accIds*/
	private String accIds;
	/**收款人信息signeroks*/
	private String signeroks;
	/**票据主表信息*/
	private AbVoucherinfo abVoucherInfo;
	/**票据从表信息*/
	private AbVoucherinfoVice abVoucherinfoVice;
	/**流水表*/
	private AbAccrecords abAccrecords;
	/**贴现日期*/
	@BtCompositeSearch(fieldName="t.DISCOUNT_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date discountDate;
	@BtCompositeSearch(fieldName="btbank.bank_name")
	private String bankName;
	/**占用额度*/
	private String occupancyMoney;
	/**是否隐藏 1：隐藏*/
	private String checkboxHide;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="p.APPLY_CORP_ID")
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	@Excel(name = "申请单位",isImportField="true")
	@BtCompositeSearch(fieldName="p.APPLY_CORP_NAME")
	private String applyCorpName;
	/**申请单位代码*/
	@Excel(name = "申请单位代码",isImportField="true")
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="p.BUS_CORP_ID")
	private String busCorpId;
	/** 业务单位名称 */
	@Excel(name = "业务单位",isImportField="true")
	private String busCorpName;
	/** 业务人员 */
	@BtCompositeSearch(fieldName="p.BUS_PERSONNEL")
	@Excel(name = "业务人员",isImportField="true")
	private String busPersonnel;
	/** 出票标志 */
	@BtCompositeSearch(fieldName="V.NEW_STM_ISSUOFPPR_IND")
	//@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	@BtCompositeSearch(fieldName="V.NEW_STM_ISSUOFPPR_IND")
    @Excel(name = "出票标志",isImportField="true")
	private String newStmIssuofpprIndName;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="V.BLGRDBAGCRCL_PRMT_IND")
	//@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	@BtCompositeSearch(fieldName="V.BLGRDBAGCRCL_PRMT_IND")
    @Excel(name = "是否允许分包",isImportField="true")
	private String blgrdbagcrclPrmtIndName;
	/** 子票区间开始序号*/
	@BtCompositeSearch(fieldName = "V.SBBLL_RNG_STRT_SN")
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	@BtCompositeSearch(fieldName = "V.SBBLL_RNG_END_SN")
	private String sbbllRngEndSn;
	/** 标准金额 */
	private BigDecimal blStdAmt;
	/**出票人统一信用代码 */
	private String blRmtrUnnIdNo;
	/**票据单位社会统一识别代码 */
	private String blHoldUnnIdNo;
	@BtCompositeSearch(fieldName="p.bank_type_id")
	private String bankTypeId;
	public String getCheckboxHide() {
		//贴现授信释放菜单处理
		if(StringUtils.isBlank(this.getReleaseState())){
			if(this.getStatus()!=null
					&&(AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())
					||AccebillConstant.AB_BILL_BUS_STATUS94.equals(this.getStatus()))){
				return "1";
			}
		}
		return checkboxHide;
	}
}
