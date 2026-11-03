package com.bytter.bill.domain.bill.vo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.enums.CommonDictEnum;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @Title: VO
 * @Description: 贴现办理VO
 * @author kongq
 * @date 2017-07-05 10:55:14
 * @version 1.0
 * 
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillDiscountManagerVO extends AbDiscountVO implements Serializable, IBusinessKey {
	private String id;
	/** 票据主表id */
	private String vid;
	/** 票据从表id */
	private String vvid;
	/** 票据主表id */
	private String vouId;
	/** 票据从表id */
	private String viceId;
	/** 流水表id */
	private String accId;
	/** 票据主表ids */
	private String vouIds;
	/** 票据从表ids */
	private String viceIds;
	/** 流水表ids */
	private String accIds;
	private String businessKey;
	/** 票据类型：10 银票、11 商票 */
	@BtCompositeSearch(fieldName = "V.bill_type")
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	@BtCompositeSearch(fieldName = "V.bill_type")
	@Excel(name = "票据类型", isImportField = "true")
	private String billTypeName;
	/** 流通介质：10 纸票、11 电票 */
	@BtCompositeSearch(fieldName = "V.bill_flag")
	// @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	private Integer billFlag;
	@BtCompositeSearch(fieldName = "V.bill_flag")
	@Excel(name = "流通介质", isImportField = "true")
	private String billFlagName;
	/** 持票单位 */
	@BtCompositeSearch(fieldName = "P.CORP_ID")
	// @Corp(inject = "corpName")
	private String corpId;
	@BtCompositeSearch(fieldName = "P.CORP_CODE")
	@Excel(name = "票据单位", isImportField = "true")
	private String corpName;
	@Excel(name = "票据单位代码", isImportField = "true")
	private String corpCode;
	/** 持票单位账号 */
	@BtCompositeSearch(fieldName = "p.bank_acc")
	private String bankAcc;
	/** 持票单位银行 */
	private String bankCode;
	/** 出票人 */
	@BtCompositeSearch(fieldName = "V.payer")
	private String payer;
	/** 出票人账号 */
	private String payeracc;
	/** 出票人开户银行名称 */
	private String payerBankName;
	/** 出票人开户银行行号 */
	private String payerbankno;
	/** 承兑人 */
	@BtCompositeSearch(fieldName = "V.acceptor")
	private String acceptor;
	/** 承兑人账号 */
	private String acceptoracc;
	/** 承兑人开户行名称 */
	private String acceptorBankName;
	/** 承兑人开户行行号 */
	private String acceptorbankno;
	/** 票据号码 */
	@BtCompositeSearch(fieldName = "V.orig_no")
	@Excel(name = "*票据号码", isImportField = "true")
	private String origNo;
	/** 票据金额 */
	@BtCompositeSearch(fieldName = "V.bill_money")
	@Excel(name = "票据金额", isImportField = "true", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
	private BigDecimal billMoney;
	/** 出票日期 */
	@BtCompositeSearch(fieldName = "V.start_date")
	@Excel(name = "出票日期", isImportField = "true")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/** 到期日期 */
	@BtCompositeSearch(fieldName = "V.end_date")
	@Excel(name = "到期日期", isImportField = "true")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/** 申请日期 */
	private Date applyDate;
	/** 收票人全称 */
	@BtCompositeSearch(fieldName = "V.payee")
	private String payee;
	/** 收票人账号 */
	private String payeeacc;
	/** 收票人开户银行名称 */
	private String payeeBankName;
	/** 收票人开户银行行号 */
	private String payeebankno;
	/** 票据状态 */
	@BtCompositeSearch(fieldName = "S.brief_id")
	// @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
	private String briefId;
	@BtCompositeSearch(fieldName = "S.brief_id")
	private String briefIdName;
	/** 票据业务状态 */
	@BtCompositeSearch(fieldName = "S.status")
	// @Dict(dictGroup = "AB_ABBILLDISCOUNT_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName = "S.status")
	private String statusName;
	/** 所属库存 */
	@BtCompositeSearch(fieldName = "P.enter_stock")
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
	@BtCompositeSearch(fieldName = "P.enter_stock")
	private String enterStockName;
	/** 不得转让标记 */
	@BtCompositeSearch(fieldName = "S.bankendrsmtmk")
	// @Dict(dictGroup = "AB_BANEM", inject = "bankendrsmtmkName")
	private String bankendrsmtmk;
	@BtCompositeSearch(fieldName = "S.bankendrsmtmk")
	@Excel(name = "*不得转让标记", isImportField = "true")
	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_BANENDRSMTMK_ISNOTNULL)
	private String bankendrsmtmkName;
	/** 交易合同号 */
	@BtCompositeSearch(fieldName = "T.txl_ctrct_nb")
	@Excel(name = "交易合同号", isImportField = "true")
	private String txlCtrctNb;
	/** 发票号码 */
	@BtCompositeSearch(fieldName = "T.invc_nb")
	@Excel(name = "发票号", isImportField = "true")
	private String invcNb;
	/** 划款天数 */
	@BtCompositeSearch(fieldName = "T.paydate")
	// @Dict(dictGroup = "AB_PAY_DATE", inject = "paydateName")
	private Integer paydate;
	@BtCompositeSearch(fieldName = "T.paydate")
	@Excel(name = "*划款天数", isImportField = "true")
	private String paydateName;
	/** 假期顺延天数 */
	@BtCompositeSearch(fieldName = "T.deferdate")
	@Excel(name = "*假期顺延天数", isImportField = "true")
	private Integer deferdate;
	/** 贴现天数 */
	@BtCompositeSearch(fieldName = "T.daynum")
	@Excel(name = "*贴现天数", isImportField = "true")
	private Integer daynum;
	/** 贴现利率 */
	@Excel(name = "*贴现利率(%)", isImportField = "true", numFormat = "##########0.0000", align = HorizontalAlignment.RIGHT)
	private BigDecimal interestRates;
	/** 贴现利息 */
	@BtCompositeSearch(fieldName = "T.interest")
	@Excel(name = "*贴现利息", isImportField = "true", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
	private BigDecimal interest;
	/** 买方付息金额 */
	@BtCompositeSearch(fieldName = "T.buyerinterest_money")
	@Excel(name = "*买方付息金额", isImportField = "true", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
	private BigDecimal buyerinterestMoney;
	/** 买方付息比率 */
	@Excel(name = "*买方付息比率(%)", isImportField = "true", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
	private BigDecimal buyerinterestRatio;
	/** 实付金额 */
	@BtCompositeSearch(fieldName = "T.real_money")
	@Excel(name = "*实付金额", isImportField = "true", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
	private BigDecimal realMoney;
	/** 流水信息集合 */
	private List<AbAccrecords> abAccrecordsList;
	/** 贴现表信息集合 */
	private List<AbDiscount> abDiscountList;
	/** 业务接收人名称 */
	@Excel(name = "贴入人名称", isImportField = "true")
	private String recNm;
	/** 业务接收人账号 */
	@Excel(name = "贴入人账号", isImportField = "true")
	private String recAcctId;
	/** 业务接收人开户行号 */
	@Excel(name = "贴入人开户行号", isImportField = "true")
	private String recAcctsvcr;
	/** 贴现种类：RM00 买断式、RM01 回购式买断式 */
	// @Dict(dictGroup = "AB_DISCOUNT_RPDMK", inject = "rpdmkName")
	private String rpdmk;
	@Excel(name = "*贴现种类", isImportField = "true")
	private String rpdmkName;
	/** 是否买方付息：1 是、0 否 */
	private String buyerinterestSing;
	// @Dict(dictGroup = "sf_yn", inject = "buyerinterestSingName")
	@Excel(name = "*是否买方付息", isImportField = "true")
	private String buyerinterestSingName;
	/** 清算方式：SM00 开启线上清算、SM01 关闭线上清算 */
	// @Dict(dictGroup = "AB_STTLMMK", inject = "sttlmmkName")
	private String sttlmmk;
	@Excel(name = "*清算方式", isImportField = "true")
	private String sttlmmkName;
	/** 入账账户 */
	@Excel(name = "入账账户", isImportField = "true")
	private String aoaccninfId;
	/** 入账户名 */
	@Excel(name = "入账户名")
	private java.lang.String aoaccninfName;
	/** 入账行号 */
	@Excel(name = "入账行号", isImportField = "true")
	private String aoaccninfAc;
	/** 赎回开放日 */
	@Excel(name = "赎回开放日", isImportField = "true")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date rpdopendt;
	/** 赎回截止日 */
	@Excel(name = "赎回截止日", isImportField = "true")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date rpdduedt;
	/** 业务归属日期 */
	@Excel(name = "*业务办理日期", isImportField = "true")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date businessDate;
	/** 贴现日期 */
	@Excel(name = "*贴现日期", isImportField = "true")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date discountDate;
	/** 备注 */
	@Excel(name = "备注", isImportField = "true")
	@BtCompositeSearch(fieldName="s.rmk")
	private String rmk;
	/** 是否隐藏 1：隐藏 */
	private String checkboxHide;
	/** 是否发送指令标志：0否； 1是 ； */
	@BtCompositeSearch(fieldName = "s.send_Sign")
	// @Dict(dictGroup = "AB_BILL_SEND_SIGN", inject = "sendSignName")
	private String sendSign;
	@BtCompositeSearch(fieldName = "s.send_Sign")
	private String sendSignName;
	/** 赎回利率 */
	@Excel(name = "赎回利率(%)", isImportField = "true", numFormat = "##########0.0000", align = HorizontalAlignment.RIGHT)
	private BigDecimal rpdintrstRate;
	private RatUrationDTO urationDTO;
	/**
	 * 银行授信单位类型 开票人=0、承兑人=1
	 */
	private String ratBankCorpType;
	/**
	 * 银行授信单位Id
	 */
	private String ratBankCorpId;
	/**内部业务标志：0  否；1 是*/
	// @Dict(dictGroup = "sf_yn", inject = "insideSingName")
	private Integer insideSing;
	@Excel(name = "是否内部贴现",isImportField="true")
	private String insideSingName;
	private String discountItem;
	/**资金计划科目代码信息*/
	@Excel(name = "资金计划科目代码",isImportField="true")
	private String itemCodeStr;
	/**资金计划信息*/
	private List<FbsBudgetProcessDTO> budgetList;
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
    @Excel(name = "出票标志",isImportField="false")
	private String newStmIssuofpprIndName;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="V.BLGRDBAGCRCL_PRMT_IND")
	@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	@BtCompositeSearch(fieldName="V.BLGRDBAGCRCL_PRMT_IND")
    @Excel(name = "是否允许分包",isImportField="false")
	private String blgrdbagcrclPrmtIndName;
	/** 子票区间开始序号*/
	@Excel(name = "*子票区间开始序号",isImportField="true")
	@BtCompositeSearch(fieldName = "V.SBBLL_RNG_STRT_SN")
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	@Excel(name = "*子票区间结束序号",isImportField="true")
	@BtCompositeSearch(fieldName = "V.SBBLL_RNG_END_SN")
	private String sbbllRngEndSn;

	@Excel(name = "*原子票区间开始序号")
	private String sbbllRngStrtSnOld;

	@Excel(name = "*原子票区间结束序号")
	private String sbbllRngEndSnOld;

	@Excel(name = "拆分子票区间开始序号")
	private String sbbllRngStrtSnSplit;

	@Excel(name = "拆分子票区间结束序号")
	private String sbbllRngEndSnSplit;
	/** 标准金额 */
	@Excel(name = "标准金额")
	private BigDecimal blStdAmt;
	/**出票人统一信用代码 */
	private String blRmtrUnnIdNo;
	/** 收款人统一信用代码 */
	private String blRcvpymtpsUnnIdNo;
	/** 承兑人统一信用代码 */
	private String blAcptrUnnIdNo;
	/** 票据单位社会统一识别代码 */
	private String blHoldUnnIdNo;
	/** 交易金额*/
	@Excel(name = "*交易金额",isImportField="true")
	private BigDecimal transMoney;
	/** 交易金额(字符串) */
	private String transMoneys;
	@BtCompositeSearch(fieldName="p.BANK_TYPE_ID")
	private String bankTypeId;
	
	
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
			sbbllRngEndSn= StrUtil.fill(sbbllRngEndSn,'0',12,true);
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
	public String getCheckboxHide() {
		if (this.getStatus() != null
				&& (AccebillConstant.AB_BILL_BUS_STATUS11.equals(this
						.getStatus()) || AccebillConstant.AB_BILL_BUS_STATUS94
						.equals(this.getStatus()))) {
			return "1";
		}
		return checkboxHide;
	}
	@Override
	public String getBeanName() {
		return "abBillDiscountManagerService";
	}
	public String getBuyerinterestSingName() {
		if (ObjectUtil.isNotEmpty(buyerinterestSing)) {
			return CommonDictEnum.getDictValue("sf_yn", buyerinterestSing);
		}
		return buyerinterestSingName;
	}
	@Override
	public String getInsideSingName() {
		if (ObjectUtil.isNotEmpty(insideSing)) {
			return CommonDictEnum.getDictValue("sf_yn", insideSing + "");
		}
		return insideSingName;
	}
}
