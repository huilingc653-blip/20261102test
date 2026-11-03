package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @Title: VO
 * @Description: 贴现收回
 * @author luop
 * @date 2017-09-05
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillDiscountCollectVo extends AbDiscountVO implements Serializable,IBusinessKey{
	
	/**贴现明细表id*/
	private String tid;
	/**票据主表id*/
	private String vid;
	private String vids;
	/**票据从表id*/
	private String vvid;
	private String vvids;
	/**流水表id*/
	private String aid;
	private String aids;
	private String businessKey;
	/**贴现利息*/
	private java.math.BigDecimal interest;
	/**返回利息*/
	private java.math.BigDecimal returnInterest;
	private String returnInterests;//多个时，逗号隔开，批量提交时使用
	/**贴现利率*/
	private String interestRatesList;//多个时，逗号隔开，批量提交时使用
	/**贴入人名称*/
	private String aoaccninfName;
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="a.bill_type")
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	private String  billTypeName;
	/**流通介质：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="a.bill_flag")
	// @Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
	private Integer billFlag;
	private String billFlagName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="b.corp_id")
	// @Corp(inject="corpName")
	private String corpId;
	@BtCompositeSearch(fieldName="b.corp_name")
	private String corpName;
	/**持票单位账号 */
	private String bankAcc;
	/**持票单位银行行号*/
	private String bankCode;
	/**持票单位银行名称*/
	private String bankName;
	/**出票人*/
	@BtCompositeSearch(fieldName="a.payer")
	private String payer;
	/**出票人账号*/
	private String payeracc;
	/**出票人开户银行名称*/
	private String payerBankName;
	/**出票人开户银行行号 */
	private String payerbankno;
	/**承兑人*/
	@BtCompositeSearch(fieldName="a.acceptor")
	private String acceptor;
	/**承兑人账号 */
	private String acceptoracc;
	/**承兑人开户行名称 */
	private String acceptorBankName;
	/**承兑人开户行行号 */
	private String acceptorbankno;
	/**票据号码*/
	@BtCompositeSearch(fieldName="a.orig_no")
	private String origNo;
	/**票据金额*/
	@BtCompositeSearch(fieldName="a.bill_money")
	private java.math.BigDecimal billMoney;
	/**出票日期*/
	@BtCompositeSearch(fieldName="a.start_date")
	private java.util.Date startDate;
	/**到期日期*/
	@BtCompositeSearch(fieldName="a.end_date")
	private java.util.Date endDate;
	/**申请日期 */
	private java.util.Date applyDate;
	/**业务办理日期 */
	private java.util.Date businessDate;
	/**收票人账号*/
	private String payeeacc;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**票据状态*/
	@BtCompositeSearch(fieldName="c.brief_id")
	// @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
	private String briefId;
	private String briefIdName;
	/**票据业务状态*/
	@BtCompositeSearch(fieldName="c.status")
	// @Dict(dictGroup = "ABBILL_DISCOUNTCOLLECT_STATUS", inject = "statusName")
	private Integer status;
	private String statusName;
	/**所属库存*/
	@BtCompositeSearch(fieldName="b.enter_stock")
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
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
	/**收款人信息signeroks*/
	private String signeroks;
	/**票据主表信息*/
	private AbVoucherinfo abVoucherInfo;
	/**票据从表信息*/
	private AbVoucherinfoVice abVoucherinfoVice;
	/**流水表*/
	private AbAccrecords abAccrecords;
	/**是否隐藏 1：隐藏*/
	private String checkboxHide;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="b.APPLY_CORP_ID")
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	@Excel(name = "申请单位",isImportField="true")
	@BtCompositeSearch(fieldName="b.APPLY_CORP_NAME")
	private String applyCorpName;
	/**申请单位代码*/
	@Excel(name = "申请单位代码",isImportField="true")
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="b.BUS_CORP_ID")
	private String busCorpId;
	/** 业务单位名称 */
	@Excel(name = "业务单位",isImportField="true")
	private String busCorpName;
	/** 业务人员 */
	@BtCompositeSearch(fieldName="b.BUS_PERSONNEL")
	@Excel(name = "业务人员",isImportField="true")
	private String busPersonnel;
	/** 出票标志 */
	@BtCompositeSearch(fieldName="a.NEW_STM_ISSUOFPPR_IND")
	//@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	@BtCompositeSearch(fieldName="a.NEW_STM_ISSUOFPPR_IND")
	@Excel(name = "出票标志",isImportField="true")
	private String newStmIssuofpprIndName;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="a.BLGRDBAGCRCL_PRMT_IND")
	//@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	@BtCompositeSearch(fieldName="a.BLGRDBAGCRCL_PRMT_IND")
	@Excel(name = "是否允许分包",isImportField="true")
	private String blgrdbagcrclPrmtIndName;
	/** 子票区间开始序号*/
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	private String sbbllRngEndSn;
	/** 标准金额 */
	private BigDecimal blStdAmt;
	/**出票人统一信用代码 */
	private String blRmtrUnnIdNo;
	@BtCompositeSearch(fieldName="b.BANK_TYPE_ID")
	private String bankTypeId;
	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
			//审批列表查询
			return "0";
		}
		//审批中
		if(AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())||AccebillConstant.AB_BILL_BUS_STATUS94.equals(this.getStatus())){
			return "1";
		}else{
			return "0";
		}
	}
}
