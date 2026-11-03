package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.IgnoreField;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
/**   
 * @Title: VO
 * @Description: 收票登记VO
 * @author kongq
 * @date 2017-06-22 14:42:25
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@ExcelTarget("AbReceiveBillVO")
@Data
public class AbReceiveBillVO extends AbAccrecordsVO implements Serializable,IBusinessKey{
	
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
	@IgnoreField
	private Boolean isSaveImport;
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="V.bill_type")
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	@BtCompositeSearch(fieldName="V.bill_type")
	@Excel(name = "*票据类型",isImportField="true")
	private String  billTypeName;
	/**流通介质：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="V.bill_flag")
	// @Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
	private Integer billFlag;
	@BtCompositeSearch(fieldName="V.bill_type")
	@Excel(name = "*流通介质",isImportField="true")
	private String billFlagName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="P.corp_id")
	// @Corp(inject="corpName")
	private String corpId;
	@Excel(name = "票据单位",isImportField="true")
	@BtCompositeSearch(fieldName="P.CORP_NAME")
	private String corpName;
	@Excel(name = "票据单位代码",isImportField="true")
	private String corpCode;
	/**票据单位账号 */
	@Excel(name = "票据单位账号",isImportField="true")
	private String bankAcc;
	/**持票单位银行行号*/
	private String bankCode;
	/**票据单位银行名称*/
	@Excel(name = "票据单位银行名称",isImportField="true")
	private String bankName;
	/**票据单位统一识别号码*/
	private java.lang.String socCode;
	/**出票人*/
	@BtCompositeSearch(fieldName="V.payer")
	@Excel(name = "*出票人",isImportField="true")
	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_PAYER_ISNOTNULL)
	private String payer;
	/**出票人账号*/
	@Excel(name = "出票人账号",isImportField="true")
	private String payeracc;
	/**出票人开户银行名称*/
	@Excel(name = "出票人开户银行名称",isImportField="true")
	private String payerBankName;
	/**出票人开户银行行号 */
	private String payerbankno;
	/**承兑人*/
	@BtCompositeSearch(fieldName="V.acceptor")
	@Excel(name = "承兑人",isImportField="true")
	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_ACCEPTOR_ISNOTNULL)
	private String acceptor;
	/**承兑人账号 */
	@Excel(name = "承兑人账号",isImportField="true")
	private String acceptoracc;
	/**承兑人开户行名称 */
	@Excel(name = "承兑人开户行名称",isImportField="true")
	private String acceptorBankName;
	/**承兑人开户行行号 */
	@Excel(name ="承兑人开户行行号",isImportField="true")
	private String acceptorbankno;
	/**票据号码*/
	@BtCompositeSearch(fieldName="V.orig_no")
	@Excel(name = "*票据号码",isImportField="true")
	@NotNull(message = AccebillConstant.LANG_ERROR_ORIGNO_ISNOTNULL)
	private String origNo;
	/**票据金额*/
	@BtCompositeSearch(fieldName="V.bill_money")
	@Excel(name = "*票据金额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
	private java.math.BigDecimal billMoney;
	/**手续费*/
	@Excel(name = "手续费",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
	private java.math.BigDecimal feemoney;
	/**出票日期*/
	@BtCompositeSearch(fieldName="V.start_date")
	@Excel(name = "*出票日期",format = "yyyy-MM-dd",isImportField="true")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date startDate;
	/**到期日期*/
	@BtCompositeSearch(fieldName="V.end_date")
	@Excel(name = "*到期日期",format = "yyyy-MM-dd",isImportField="true")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date endDate;
	/**申请日期 */
	private java.util.Date applyDate;
	/**业务归属日期 */
	@BtCompositeSearch(fieldName="S.business_date")
	@Excel(name = "*业务办理日期",format = "yyyy-MM-dd",isImportField="true")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date businessDate;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="V.payee")
	@Excel(name = "*收票人",isImportField="true")
	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_PAYEE_ISNOTNULL)
	private String payee;
	/**收票人账号*/
	@Excel(name = "收票人账号",isImportField="true")
	private String payeeacc;
	/**收票人开户银行名称*/
	@Excel(name = "收票人开户银行名称",isImportField="true")
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**票据状态*/
	@BtCompositeSearch(fieldName="S.status")
	// @Dict(dictGroup = "AB_BILL_ACCRECORDS_STATUS", inject = "statusName")
	private Integer status;
	private String statusName;
	/**所属库存*/
	@BtCompositeSearch(fieldName="P.enter_stock")
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
	@BtCompositeSearch(fieldName="P.enter_stock")
	@Excel(name = "*所属库存",isImportField="true")
	private String enterStockName;
	/**票据来源*/
	@BtCompositeSearch(fieldName="P.bill_source")
	// @Dict(dictGroup = "AB_ABRECEIVEBILL_SOURCE", inject = "billSourceName")
	private String billSource;
	@BtCompositeSearch(fieldName="P.bill_source")
	@Excel(name = "*票据来源",isImportField="true")
	private String billSourceName;
	/** 内部业务标志：0 否；1 是 */
	// @Dict(dictGroup = "ACCEBILL_ABRECEIVE_INSIDESING", inject = "insideSingName")
	private Integer insideSing;
	@Excel(name = "*收票方式",isImportField="true")
	private String insideSingName;
	/**不得转让标记 */
	// @Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
	private String banendrsmtmk;
	@Excel(name = "不得转让标记",isImportField="true")
	private String banendrsmtmkName;
	/**承兑合同号 */
	@Excel(name = "承兑合同号",isImportField="true")
	private String acontractNo;
	/**发票号 */
	@Excel(name = "发票号",isImportField="true")
	private String ainvcnb;
	/**前手*/
	@Excel(name = "前手",isImportField="true")
	@BtCompositeSearch(fieldName="S.cn_Accrecords")
	private String cnAccrecords;
	/**摘要*/
	@Excel(name = "摘要",isImportField="true")
	private String abs;
	/**备注*/
	@Excel(name = "备注",isImportField="true")
	private String rmk;
	/**贴现日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date discountDate;
	/**贴现天数*/
	private Integer daynum;
	/**贴现利息*/
	private java.math.BigDecimal interest;
	/**贴现种类：RM00 买断式、RM01 回购式买断式*/
	// @Dict(dictGroup = "AB_DISCOUNT_RPDMK", inject ="rpdmkName")
	private String rpdmk;
	private String rpdmkName;
	/**清算方式：SM00 开启线上清算、SM01 关闭线上清算*/
	// @Dict(dictGroup = "AB_STTLMMK", inject ="sttlmmkName")
	private String sttlmmk;
	private String sttlmmkName;
	/**划款天数*/
	private Integer paydate;
	/**贴现利率*/
	private java.math.BigDecimal interestRates;
	/**赎回开放日*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date rpdopendt;
	/**赎回截止日*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date rpdduedt;
	/**资金计划信息*/
	private List<FbsBudgetProcessDTO> budgetList;
	/**资金计划科目代码信息*/
	@Excel(name = "资金计划科目代码",isImportField="true")
	private String itemCodeStr;
	/**票据主表信息*/
	private AbVoucherinfo abVoucherInfo;
	/**票据从表信息*/
	private AbVoucherinfoVice abVoucherinfoVice;
	/**流水表*/
	private AbAccrecords abAccrecords;
	/**贴现表*/
	private AbDiscount abDiscount;
	/**附件ID*/
	private String attachmentId;
	/**票据池明细pdId*/
	private String pdId;
	/**是否隐藏 1：隐藏*/
	private String checkboxHide;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="p.APPLY_CORP_ID")
	@Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	@BtCompositeSearch(fieldName="P.APPLY_CORP_NAME")
	@Excel(name = "申请单位",isImportField="true")
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
	@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	@BtCompositeSearch(fieldName="V.NEW_STM_ISSUOFPPR_IND")
	@Excel(name = "*出票标志",isImportField="true")
	private String newStmIssuofpprIndName;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="V.BLGRDBAGCRCL_PRMT_IND")
	@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	@BtCompositeSearch(fieldName="V.BLGRDBAGCRCL_PRMT_IND")
	@Excel(name = "*是否允许分包",isImportField="true")
	private String blgrdbagcrclPrmtIndName;
	@Excel(name = "*子票区间开始序号",isImportField="true")
	@BtCompositeSearch(fieldName="V.SBBLL_RNG_STRT_SN")
	private String sbbllRngStrtSn;
	@Excel(name = "子票区间结束序号",isImportField="false")
	@BtCompositeSearch(fieldName="V.SBBLL_RNG_END_SN")
	private String sbbllRngEndSn;
	/** 出票人统一信用代码 */
	@Excel(name = "出票人社会统一信用代码",isImportField="false")
	private String blRmtrUnnIdNo;
	/** 收票人统一信用代码 */
	@Excel(name = "收票人社会统一信用代码",isImportField="false")
	private String blRcvpymtpsUnnIdNo;
	/** 承兑人统一信用代码 */
	@Excel(name = "承兑人社会统一信用代码",isImportField="false")
	private String blAcptrUnnIdNo;
	/** 持票人统一信用代码 */
	@Excel(name = "票据单位社会统一信用代码",isImportField="false")
	private String blHoldUnnIdNo;
	@BtCompositeSearch(fieldName="p.BANK_TYPE_ID")
	private String bankTypeId;
	@Override
	public String getBusinessKey() {
		return businessKey;
	}
	public String getCheckboxHide() {
		if(this.getStatus()!=null
				&&
				(AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.status)
						||AccebillConstant.AB_BILL_BUS_STATUS94.equals(this.status))){
		 checkboxHide="1";
		}
		return checkboxHide;
	}
	public Boolean getSaveImport() {
		return isSaveImport;
	}
	public void setSaveImport(Boolean saveImport) {
		isSaveImport = saveImport;
	}
	@Override
	public String getBeanName() {
		return "abReceiveBillService";
	}
}
