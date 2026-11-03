package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
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
 * @Description: 指令处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillInstructionManageVO extends AbAccrecordsVO implements Serializable{
	private String id;
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**签收表id*/
	private String recid;
	/**票据号码*/
	@BtCompositeSearch(fieldName="z.orig_No")
	private String origNo;
	/**票据类型：10 银票、11 商票*/
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	/**票据类型名称：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="z.bill_Type")
	private String billTypeName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="c.corp_id")
	private String corpId;
	@BtCompositeSearch(fieldName="c.corp_code")
	private String corpName;
	private String billStatus;
	/**流通介质：10 纸票、11 电票*/
	// @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	@BtCompositeSearch(fieldName="z.bill_Flag")
	private Integer billFlag;
	/**流通介质名称：10 纸票、11 电票*/
	private String billFlagName;
	/**票据金额*/
	@BtCompositeSearch(fieldName="z.bill_Money")
	private java.math.BigDecimal billMoney;
	/**出票日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="z.start_Date")
	private java.util.Date startDate;
	/**到期日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="z.end_Date")
	private java.util.Date endDate;
	/**处理日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName="l.apply_Date")
	private java.util.Date applyDate;
	/**出票人*/
	@BtCompositeSearch(fieldName="z.payer")
	private String payer;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="z.payee")
	private String payee;
	/**承兑人*/
	@BtCompositeSearch(fieldName="z.acceptor")
	private String acceptor;
	/**开出方式：0  对外开出；1 对内自开*/
	// @Dict(dictGroup = "AB_BILL_OPENWAY", inject = "insideSingName")
	@BtCompositeSearch(fieldName="c.inside_Sing")
	private Integer insideSing;
	/**业务处理状态*/
	// @Dict(dictGroup = "AB_BILL_ACCRECORDS_STATUS", inject = "statusName")
	@BtCompositeSearch(fieldName="l.status")
	private Integer status;
	private String statusName;
	/**业务状态*/
	// @Dict(dictGroup = "AB_BILL_INSBRIEFID_TEST", inject = "briefIdName")
	private String briefId ;
	@BtCompositeSearch(fieldName="l.brief_Id")
	private String briefIdName;
	/**指令操作业务状态*/
	// @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "optStatusName")
	private String optStatus ;
	@BtCompositeSearch(fieldName="l.opt_Status")
	private String optStatusName;
	/**签收回复标志*/
	// @Dict(dictGroup = "AB_SGNUPMK", inject = "signUpFlagName")
	private String signUpFlag ;
	@BtCompositeSearch(fieldName="l.SIGN_UP_FLAG")
	private String signUpFlagName;
	private java.util.Date date;
	private String ids;
	private String vids;
	private String vvids;
	/**是否隐藏多选框 1为是*/
	private String checkboxHide;
	/**业务申请人名称*/
	@BtCompositeSearch(fieldName="l.req_nm")
	private String reqNm;
	/**业务接收人名称*/
	@BtCompositeSearch(fieldName="l.rec_nm")
	private String recNm;
	/**业务接收人账号*/
	@BtCompositeSearch(fieldName="l.rec_acct_id")
	private String recAcctId;
	private String insideSingName;
	/*** 操作类型 */
	private String opType;
	/**返回代码*/
	@BtCompositeSearch(fieldName="l.return_Code")
	private String returnCode;
	/**处理结果*/
	@BtCompositeSearch(fieldName="l.return_Msg")
	private String returnMsg;
	/** 类型 0应付票据，1应收票据*/
	@BtCompositeSearch(fieldName="c.type")
	private String type;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="c.APPLY_CORP_ID")
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
//	@BtCompositeSearch(fieldName="s2.name")
	@BtCompositeSearch(fieldName="c.apply_corp_code")
	private String applyCorpName;
	/**申请单位代码*/
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="c.BUS_CORP_ID")
	private String busCorpId;
	/** 业务单位名称 */
	@BtCompositeSearch(fieldName="c.apply_corp_name")
	private String busCorpName;
	/** 业务人员 */
	@BtCompositeSearch(fieldName="c.BUS_PERSONNEL")
	private String busPersonnel;
	/**
	 * 前手单位名称
	 */
	private String cnAccrecordsName;
	/**
	 * 票据上划类型
	 */
	private String billTransferType;
    private String mergeOpt;//回退是否合并，0不合并，1合并
    /** 出票标志 new_stm_issuofppr_ind*/
    @BtCompositeSearch(fieldName = "z.NEW_STM_ISSUOFPPR_IND")
   // @Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
    private String newStmIssuofpprInd;
    private String newStmIssuofpprIndName;
    /** 是否允许分包*/
    @BtCompositeSearch(fieldName = "z.BLGRDBAGCRCL_PRMT_IND")
   // @Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    private String blgrdbagcrclPrmtInd;
    private String blgrdbagcrclPrmtIndName;

	/** 子票区间开始序号 */
	@BtCompositeSearch(fieldName = "z.sbbll_rng_strt_sn")
	private String sbbllRngStrtSn;

	/** 子票区间结束序号 */
	@BtCompositeSearch(fieldName = "z.sbbll_rng_end_sn")
	private String sbbllRngEndSn;
    /** 标准金额 */
    private BigDecimal blStdAmt;
    /**
     * 出票人统一信用代码
     */
    private String blRmtrUnnIdNo;
    /**
     *交易金额
     */
    private String transMoney;
	@BtCompositeSearch(fieldName="c.bank_type_id")
	private String bankTypeId;

	/**
	 * 票据单位账号
	 */
	@BtCompositeSearch(fieldName="c.bank_acc")
	private String bankAcc;
	/**
	 * 批次号 1
	 */
	@BtCompositeSearch(fieldName="c.batch_no")
	private String batchNo;

	public String getCheckboxHide() {
		if(AccebillConstant.AB_BILL_OPT_STATUS15.equals(this.getOptStatus())
				|| AccebillConstant.AB_BILL_OPT_STATUS_1.equals(this.getOptStatus())){
			return "0";
		}else if(StringUtils.isEmpty(checkboxHide)){
			return "1";
		}
		return checkboxHide;
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
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ACCRECORDS_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> this.setStatusName(dict.getName()));
		});
		// 业务状态
		Optional.ofNullable(this.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSBRIEFID_TEST");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> this.setBriefIdName(dict.getName()));
		});
		// 提示保证操作业务状态
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
