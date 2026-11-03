package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
/**
 * @Title: Vo
 * @Description: 追索清偿处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillSearchforManageVO extends AbAccrecordsVO implements Serializable, IBusinessKey {
	private String id;
	/** 票据主表id */
	private String vid;
	/** 票据从表id */
	private String vvid;
	/** 签收表id */
	private String recid;
	private String businessKey;
	/** 票据号码 */
	@BtCompositeSearch(fieldName = "origNo")
	private String origNo;
	/** 票据类型：10 银票、11 商票 */
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	/** 票据类型名称：10 银票、11 商票 */
	@BtCompositeSearch(fieldName = "billType")
	private String billTypeName;
	/** 持票单位 */
	@BtCompositeSearch(fieldName = "corpId")
	private String corpId;
	@BtCompositeSearch(fieldName = "corpCode")
	private String corpName;
	private String billStatus;
	private String validSign;
	/** 流通介质：10 纸票、11 电票 */
	// @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	@BtCompositeSearch(fieldName = "billFlag")
	private Integer billFlag;
	/** 流通介质名称：10 纸票、11 电票 */
	private String billFlagName;
	/** 票据金额 */
	@BtCompositeSearch(fieldName = "billMoney")
	private java.math.BigDecimal billMoney;
	/** 出票日期 */
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "startDate")
	private java.util.Date startDate;
	/** 到期日期 */
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "endDate")
	private java.util.Date endDate;
	/** 处理日期 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName = "applyDate")
	private java.util.Date applyDate;
	/** 追索通知日期 */
	@BtCompositeSearch(fieldName = "appldt")
	private java.util.Date appldt;
	/** 出票人 */
	@BtCompositeSearch(fieldName = "payer")
	private String payer;
	/** 收票人全称 */
	@BtCompositeSearch(fieldName = "payee")
	private String payee;
	/** 承兑人 */
	@BtCompositeSearch(fieldName = "acceptor")
	private String acceptor;
	/** 开出方式：0 对外开出；1 对内自开 */
	// @Dict(dictGroup = "AB_BILL_OPENWAY", inject = "insideSingName")
	@BtCompositeSearch(fieldName = "insideSing")
	private Integer insideSing;
	/**处理状态：18待签收19签成功-2打回-3撤回*/
	// @Dict(dictGroup="AB_BILL_RECEIVEBIZ_STATUS",inject="statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="bail.STATUS")
	private String statusName;
	/** 业务状态 */
	// @Dict(dictGroup = "AB_BILL_INSBRIEFID", inject = "briefIdName")
	private String briefId;
	@BtCompositeSearch(fieldName = "briefId")
	private String briefIdName;
	/** 签收回复标志 */
	// @Dict(dictGroup = "AB_SGNUPMK", inject = "signUpFlagName")
	private String signUpFlag;
	@BtCompositeSearch(fieldName = "signUpFlag")
	private String signUpFlagName;
	private java.util.Date date;
	private String ids;
	private String vids;
	private String vvids;
	/** 是否隐藏多选框 1为是 */
	private String checkboxHide;
	/** 业务申请人名称 */
	@BtCompositeSearch(fieldName = "reqNm")
	private String reqNm;
	/** 业务接收人名称 */
	@BtCompositeSearch(fieldName = "recNm")
	private String recNm;
	/** 业务接收人账号 */
	@BtCompositeSearch(fieldName = "recAcctId")
	private String recAcctId;
	/** 业务接收人开户行号 */
	private String recAcctsvcr;
	@BtCompositeSearch(fieldName = "bailType")
	private String bailTypeName;
	@BtCompositeSearch(fieldName = "bankAcc")
	private String bankAcc;
	/** 追索类型 */
	// @Dict(dictGroup = "AB_RCRS_TP", inject = "rcrsTpName")
	private String rcrsTp;
	@BtCompositeSearch(fieldName = "rcrsTp")
	private String rcrsTpName;
	/** 追索理由代码 */
	// @Dict(dictGroup = "AB_RCRSRSNCD", inject = "rcrsrsncdName")
	private String rcrsrsncd;
	/** 追索理由名称 */
	@BtCompositeSearch(fieldName = "rcrsrsncd")
	private String rcrsrsncdName;
	/** 追索金额 */
	@BtCompositeSearch(fieldName = "reqamt")
	private java.math.BigDecimal reqamt;
	@BtCompositeSearch(fieldName = "rcrsokmoney")
	/** 追索同意清偿金额 */
	private java.math.BigDecimal rcrsokmoney;
	/** 追索同意清偿金额 */
	private String rcrsokmoneys;
	/** 保证人地址 */
	private String adr;
	/** 备注 */
	private String rmk;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="applyCorpId")
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	@BtCompositeSearch(fieldName="applyCorpName")
	private String applyCorpName;
	/**申请单位代码*/
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="busCorpId")
	private String busCorpId;
	/** 业务单位名称 */
	private String busCorpName;
	/** 业务人员 */
	@BtCompositeSearch(fieldName="busPersonnel")
	private String busPersonnel;
	/** 出票标志 */
	@BtCompositeSearch(fieldName="newStmIssuofpprInd")
	private String newStmIssuofpprInd;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="blgrdbagcrclPrmtInd")
	private String blgrdbagcrclPrmtInd;
	private String sbbllRngStrtSn;
	private String sbbllRngEndSn;
	private BigDecimal blStdAmt;
	private String socCode;
	@BtCompositeSearch(fieldName="bail.bankTypeName")
	private String bankTypeId;

	private String bankTypeName;
	public String getCheckboxHide() {
		if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())) {
			return "1";
		} else {
			return "0";
		}
	}
	public void injectValues(Map<String, Map<String, DictDTO>> listDictMap){
		// 票据类型：10 银票、11 商票
		Optional.ofNullable(this.getBillType()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
					.ifPresent(dict -> this.setBillTypeName(dict.getName()));
		});
		// 流通介质：10 银票、11 商票
		Optional.ofNullable(this.getBillFlag()).ifPresent(billFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
					.ifPresent(dict -> this.setBillFlagName(dict.getName()));
		});
		// 开出方式
		Optional.ofNullable(this.getInsideSing()).ifPresent(insideSing -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
					.ifPresent(dict -> this.setInsideSingName(dict.getName()));
		});
		// 业务处理状态
		Optional.ofNullable(this.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_RECEIVEBIZ_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> this.setStatusName(dict.getName()));
		});
		// 业务状态
		Optional.ofNullable(this.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSBRIEFID");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> this.setBriefIdName(dict.getName()));
		});
		// 签收回复标志
		Optional.ofNullable(this.getSignUpFlag()).ifPresent(signUpFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_SGNUPMK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(signUpFlag)))
					.ifPresent(dict -> this.setSignUpFlagName(dict.getName()));
		});
		// 追索类型
		Optional.ofNullable(this.getRcrsTp()).ifPresent(rcrsTp -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_RCRS_TP");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(rcrsTp)))
					.ifPresent(dict -> this.setRcrsTpName(dict.getName()));
		});
		// 追索理由代码
		Optional.ofNullable(this.getRcrsrsncd()).ifPresent(rcrsrsncd -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_RCRSRSNCD");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(rcrsrsncd)))
					.ifPresent(dict -> this.setRcrsrsncdName(dict.getName()));
		});
	}
}
