package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
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
 * @Description: 通用撤回处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillRecallVO extends AbAccrecordsVO implements Serializable,IBusinessKey{
	private String id;
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**签收表id*/
	private String recid;
	/**票据号码*/
	@BtCompositeSearch(fieldName="origNo")
	private String origNo;
	/**票据类型：10 银票、11 商票*/
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	/**票据类型名称：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="billType")
	private String billTypeName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="corpId")
	private String corpId;
	@BtCompositeSearch(fieldName="corpCode")
	private String corpName;
	private String billStatus;
	private String validSign;
	/**流通介质：10 纸票、11 电票*/
	// @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	@BtCompositeSearch(fieldName="billFlag")
	private Integer billFlag;
	/**流通介质名称：10 纸票、11 电票*/
	private String billFlagName;
	/**票据金额*/
	@BtCompositeSearch(fieldName="billMoney")
	private java.math.BigDecimal billMoney;
	/**出票日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="startDate")
	private java.util.Date startDate;
	/**到期日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="endDate")
	private java.util.Date endDate;
	/**处理日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName="applyDate")
	private java.util.Date applyDate;
	/**出票人*/
	@BtCompositeSearch(fieldName="payer")
	private String payer;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="payee")
	private String payee;
	/**收票人*/
	@BtCompositeSearch(fieldName="acceptor")
	private String acceptor;
	/**开出方式：0  对外开出；1 对内自开*/
	// @Dict(dictGroup = "AB_BILL_OPENWAY", inject = "insideSingName")
	@BtCompositeSearch(fieldName="insideSing")
	private Integer insideSing;
	/**业务处理状态*/
	// @Dict(dictGroup = "AB_BILL_RECALL_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="status")
	private String statusName;
	/**业务状态*/
	// @Dict(dictGroup = "AB_BILL_INSBRIEFID", inject = "briefIdName")
	private String briefId ;
	@BtCompositeSearch(fieldName="briefId")
	private String briefIdName;
	/**通用撤回操作业务状态*/
	// @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "optStatusName")
	private String optStatus ;
	@BtCompositeSearch(fieldName="optStatus")
	private String optStatusName;
	/**签收回复标志*/
	// @Dict(dictGroup = "AB_SGNUPMK", inject = "signUpFlagName")
	private String signUpFlag ;
	@BtCompositeSearch(fieldName="signUpFlag")
	private String signUpFlagName;
	private java.util.Date date;
	private String ids;
	private String vids;
	private String vvids;
	/**是否隐藏多选框 1为是*/
	private String checkboxHide;
	/**业务申请人名称*/
	@BtCompositeSearch(fieldName="reqNm")
	private String reqNm;
	/**业务接收人名称*/
	@BtCompositeSearch(fieldName="recNm")
	private String recNm;
	/**业务接收人账号*/
	@BtCompositeSearch(fieldName="recAcctId")
	private String recAcctId;
	/**业务接收人开户行号*/
	private String recAcctsvcr;
	private String insideSingName;
	/**可撤回类型:01701开票保证，01702收票保证；01703背书保证 002提示承兑003提示收票 010背书申请 011贴现申请 018质押申请 019质押解除 020提示付款 021逾期付款 022追索申请 023追索同意清楚*/
	// @Dict(dictGroup = "AB_RECALL_TYPE", inject = "recallTypeName")
	private String recallType;
	private String recallTypes;
	@BtCompositeSearch(fieldName="recallType")
	private String recallTypeName;
	@BtCompositeSearch(fieldName="bankAcc")
	private String bankAcc;
	private String rmks;
	/**保证人地址*/
	private String adr;
	/**备注*/
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
	@BtCompositeSearch(fieldName="newStmIssuofpprInd")
	@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	private String newStmIssuofpprIndName;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="blgrdbagcrclPrmtInd")
	@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	private String blgrdbagcrclPrmtIndName;
	/** 子票区间开始序号*/
	@BtCompositeSearch(fieldName="sbbllRngStrtSn")
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	@BtCompositeSearch(fieldName="sbbllRngEndSn")
	private String sbbllRngEndSn;
	/** 标准金额 */
	private BigDecimal blStdAmt;
	/** 票据单位社会统一识别代码 */
	private String blHoldUnnIdNo;
	/**业务批次号 */
	@BtCompositeSearch(fieldName="batchNo")
	private String batchNo;
	/**出票人统一信用代码 */
	private String blRmtrUnnIdNo;
	@BtCompositeSearch(fieldName="bankTypeId")
	private String bankTypeId;
	public String getCheckboxHide() {
		if(StringUtils.isEmpty(checkboxHide)&& AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())){
			return "1";
		}else{
			return "0";
		}
	}
	@Override
	public String getBusinessKey() {
		return id;
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
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_RECALL_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> this.setStatusName(dict.getName()));
		});
		// 业务状态
		Optional.ofNullable(this.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSBRIEFID");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> this.setBriefIdName(dict.getName()));
		});
		// 通用撤回操作业务状态
		Optional.ofNullable(this.getOptStatus()).ifPresent(optStatus -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSOPTSTATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(optStatus)))
					.ifPresent(dict -> this.setOptStatusName(dict.getName()));
		});
		// 签收回复标志
		Optional.ofNullable(this.getSignUpFlag()).ifPresent(signUpFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_SGNUPMK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(signUpFlag)))
					.ifPresent(dict -> this.setSignUpFlagName(dict.getName()));
		});
		// 可撤回类型
		Optional.ofNullable(this.getRecallType()).ifPresent(recallType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_RECALL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(recallType)))
					.ifPresent(dict -> this.setRecallTypeName(dict.getName()));
		});
		//出票标志: 1 新系统出票 、0 老系统出票
		Optional.ofNullable(this.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd ->{
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
					.ifPresent(dict -> this.setNewStmIssuofpprIndName(dict.getName()));
		});
		//是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
		Optional.ofNullable(this.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName ->{
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
					.ifPresent(dict -> this.setBlgrdbagcrclPrmtIndName(dict.getName()));
		});
	}
}
