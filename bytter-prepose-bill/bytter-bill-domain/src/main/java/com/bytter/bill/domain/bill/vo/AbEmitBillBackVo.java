package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
/**   
 * @Title: Vo
 * @Description: 未用退票
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbEmitBillBackVo extends AbAccrecordsVO implements Serializable,IBusinessKey{
	private String id;
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**票据号码*/
	@BtCompositeSearch(fieldName="z.orig_No")
	private String origNo;
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="z.bill_Type")
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;

	// 新一代票据
	/** 子票区间开始序号 */
	@BtCompositeSearch(fieldName="z.SBBLL_RNG_STRT_SN")
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	@BtCompositeSearch(fieldName="z.SBBLL_RNG_END_SN")
	private String sbbllRngEndSn;


	/**票据类型名称：10 银票、11 商票*/
	private String billTypeName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="c.corp_id")
	private String corpId;
	@BtCompositeSearch(fieldName="c.corp_name")
	private String corpIdName;
	private String billStatus;
	/**开票数据来源*/
	// @Dict(dictGroup = "AB_EMITBILL_SOURCE", inject = "sourceName")
	private String source;
	@BtCompositeSearch(fieldName="c.SOURCE")
	private String sourceName;
	/**流通介质：10 纸票、11 电票*/
	// @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	@BtCompositeSearch(fieldName="z.bill_Flag")
	private Integer billFlag;
	/**流通介质名称：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="z.bill_Flag")
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
	// @Dict(dictGroup = "AB_BILL_AbEmitBillBack_STATUS", inject = "statusName")
	@BtCompositeSearch(fieldName="l.status")
	private Integer status;
	private String statusName;
	/**业务状态*/
	// @Dict(dictGroup = "AB_EMITBILLBACK_BRIEF", inject = "briefIdName")
	@BtCompositeSearch(fieldName="l.brief_Id")
	private String briefId ;
	@BtCompositeSearch(fieldName="l.brief_Id")
	private String briefIdName;
	private java.util.Date date;
	private String ids;
	private String vids;
	private String vvids;
	private Integer checkboxHide;
	/**是否发送指令标志：0否； 1是 ；*/
	@BtCompositeSearch(fieldName="l.send_Sign")
	// @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
	private String sendSign;
	@BtCompositeSearch(fieldName="l.send_Sign")
	private String sendSignName;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="c.APPLY_CORP_ID")
	private String applyCorpId;
	/**申请单位名称*/
	@BtCompositeSearch(fieldName="c.apply_corp_name")
	private String applyCorpName;
	/**申请单位代码*/
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="c.BUS_CORP_ID")
	private String busCorpId;
	/** 业务单位名称 */
	private String busCorpName;
	/** 业务人员 */
	@BtCompositeSearch(fieldName="c.BUS_PERSONNEL")
	private String busPersonnel;
	//新一代票据新增字段
	/**
	 * 出票标志 0：老系统出票   1：新系统出票
	 */
	@BtCompositeSearch(fieldName="z.NEW_STM_ISSUOFPPR_IND")
	//@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	private String newStmIssuofpprIndName;
	/**
	 * 是否允许分包 1 是  0 否，默认为否
	 */
	@BtCompositeSearch(fieldName="z.BLGRDBAGCRCL_PRMT_IND")
	//@Dict(dictGroup="AB_BILL_BLGRDBAGCRCL_PRMT_IND",inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	private String blgrdbagcrclPrmtIndName;
	/**
	 * 标准金额
	 */
	private BigDecimal blStdAmt;
	/**
	 * 出票人统一识别号码
	 */
	private String blRmtrUnnIdNo;
	/**
	 * 业务批次号
	 */
	@BtCompositeSearch(fieldName="c.BATCH_NO")
	private String batchNo;

	@BtCompositeSearch(fieldName="c.BANK_TYPE_ID")
	private String bankTypeId;
	@Override
	public String getBusinessKey() {
		return id;
	}
}
