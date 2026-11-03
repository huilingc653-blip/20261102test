package com.bytter.bill.domain.bill.vo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.enums.CommonDictEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
/**   
 * @Title: VO
 * @Description: 票据-解除质押VO
 * @author zhenghj
 * @date 2017-07-14
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillPledgeRelieveVo extends AbAccrecordsVO implements Serializable,IBusinessKey{
	
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
	@BtCompositeSearch(fieldName="P.CORP_id")
	private String corpId;
	@BtCompositeSearch(fieldName="P.CORP_NAME")
	private String corpName;
	/**持票单位账号 */
	private String bankAcc;
	/**持票单位银行*/
	private String bankCode;
	/**持票人银行名称*/
	private String bankName;
	/**持票人银行户名*/
	private String accName;
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
	// @Dict(dictGroup = "AB_BILL_ACCRECORDS_PLEDGERELIEVE_STATUS", inject = "statusName")
	private Integer status;
	private String statusName;
	/**所属库存*/
	@BtCompositeSearch(fieldName="P.enter_stock")
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
	private String enterStockName;
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
	private java.util.Date businessDate;
	/**是否隐藏多选框 1为是*/
	private String checkboxHide;
	/**质权人*/
	private String pledgeeName;
	/**质权人账号*/
	private String pledgeeAcc;
	/**质权人开户行行号*/
	private String pledgeeBankAcc;
	/**质权人开户行行名*/
	private String pledgeeBankName;
	/**质权人单位*/
	private String pledgeeCorpName;
	/**开出方式：0  否；1 是*/
	// @Dict(dictGroup = "sf_yn", inject = "insideSingName")
	private Integer insideSing;
	private String insideSingName;
	private String businessKey;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="p.APPLY_CORP_ID")
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	@Excel(name = "申请单位",isImportField="true")
	@BtCompositeSearch(fieldName="p.APPLY_CORP_CODE")
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
    @Excel(name = "出票标志",isImportField="true")
	private String newStmIssuofpprIndName;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="V.BLGRDBAGCRCL_PRMT_IND")
	@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	@BtCompositeSearch(fieldName="V.BLGRDBAGCRCL_PRMT_IND")
    @Excel(name = "是否允许分包",isImportField="true")
	private String blgrdbagcrclPrmtIndName;
	/** 子票区间开始序号 */
	@BtCompositeSearch(fieldName="V.SBBLL_RNG_STRT_SN")
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	@BtCompositeSearch(fieldName="V.SBBLL_RNG_END_SN")
	private String sbbllRngEndSn;

	@BtCompositeSearch(fieldName="V.ORG_SBBLL_RNG_STRT_SN")
	private String sbbllRngStrtSnOld;
	@BtCompositeSearch(fieldName="V.ORG_SBBLL_RNG_END_SN")
	private String sbbllRngEndSnOld;

	private String sbbllRngStrtSnSplit;

	private String sbbllRngEndSnSplit;
	/** 标准金额*/
	@BtCompositeSearch(fieldName="V.BL_STD_AMT")
	private BigDecimal blStdAmt;
	/** BL_RMTR_UNN_ID_NO IS '出票人统一信用代码 */
	@BtCompositeSearch(fieldName = "V.BL_RMTR_UNN_ID_NO")
	private String blRmtrUnnIdNo;
	/** 交易金额字*/
	@BtCompositeSearch(fieldName = "V.BILL_MONEY")
	private BigDecimal transMoney;
	/*** 页面修改后的交易金额字符串*/
	private String transMoneys;
	@BtCompositeSearch(fieldName="p.bank_type_id")
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


	@Override
	public String getSbbllRngStrtSn() {
		return sbbllRngStrtSn;
	}

	@Override
	public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
		if(StrUtil.isNotEmpty(sbbllRngStrtSn) && !"0".equals(sbbllRngStrtSn)){
			sbbllRngStrtSn=StrUtil.fill(sbbllRngStrtSn,'0',12,true);
		}
		this.sbbllRngStrtSn = StrUtil.replace(sbbllRngStrtSn," ","0");
	}

	@Override
	public String getSbbllRngEndSn() {
		return sbbllRngEndSn;
	}

	@Override
	public void setSbbllRngEndSn(String sbbllRngEndSn) {
		if(StrUtil.isNotEmpty(sbbllRngEndSn) && !"0".equals(sbbllRngEndSn)){
			sbbllRngEndSn=StrUtil.fill(sbbllRngEndSn,'0',12,true);
		}
		this.sbbllRngEndSn = StrUtil.replace(sbbllRngEndSn," ","0");
	}

	public String getSbbllRngStrtSnOld() {
		return sbbllRngStrtSnOld;
	}

	public void setSbbllRngStrtSnOld(String sbbllRngStrtSnOld) {
		if(StrUtil.isNotEmpty(sbbllRngStrtSnOld)){
			if(!"0".equals(sbbllRngStrtSnOld)) {
				sbbllRngStrtSnOld = StrUtil.fill(sbbllRngStrtSnOld, '0', 12, true);
			}
			setSbbllRngStrtSn(sbbllRngStrtSnOld);
		}
		this.sbbllRngStrtSnOld = sbbllRngStrtSnOld;
	}

	public String getSbbllRngEndSnOld() {
		return sbbllRngEndSnOld;
	}

	public void setSbbllRngEndSnOld(String sbbllRngEndSnOld) {
		if(StrUtil.isNotEmpty(sbbllRngEndSnOld)){
			if(!"0".equals(sbbllRngEndSnOld)) {
				sbbllRngEndSnOld = StrUtil.fill(sbbllRngEndSnOld, '0', 12, true);
			}
			setSbbllRngEndSn(sbbllRngEndSnOld);
		}
		this.sbbllRngEndSnOld = sbbllRngEndSnOld;
	}

	public String getSbbllRngStrtSnSplit() {
		return sbbllRngStrtSnSplit;
	}

	public void setSbbllRngStrtSnSplit(String sbbllRngStrtSnSplit) {
		if(StrUtil.isNotEmpty(sbbllRngStrtSnSplit)&&!"0".equals(sbbllRngStrtSnSplit)) {
			sbbllRngStrtSnSplit = StrUtil.fill(sbbllRngStrtSnSplit, '0', 12, true);
		}
		this.sbbllRngStrtSnSplit = sbbllRngStrtSnSplit;
	}

	public String getSbbllRngEndSnSplit() {
		return sbbllRngEndSnSplit;
	}

	public void setSbbllRngEndSnSplit(String sbbllRngEndSnSplit) {
		if(StrUtil.isNotEmpty(sbbllRngEndSnSplit)&&!"0".equals(sbbllRngEndSnSplit)) {
			sbbllRngEndSnSplit = StrUtil.fill(sbbllRngEndSnSplit, '0', 12, true);
		}
		this.sbbllRngEndSnSplit = sbbllRngEndSnSplit;
	}

	@Override
	public String getInsideSingName() {
		if (ObjectUtil.isNotEmpty(insideSing)) {
			return CommonDictEnum.getDictValue("sf_yn", insideSing + "");
		}
		return insideSingName;
	}
}
