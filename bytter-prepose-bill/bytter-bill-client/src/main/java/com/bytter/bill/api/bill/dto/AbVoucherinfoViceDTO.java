package com.bytter.bill.api.bill.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Vo
 * @Description: 单位持票信息VO
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 */

@Data
public class AbVoucherinfoViceDTO implements Serializable{
	
	private String id;
	/**
	 * 最新已选IDs
	 */
	private String ids;
	/**
	 * 历史已选IDs
	 */
	private String oldIds;
	/**
	 * 票据主表id
	 */
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**签收表id*/
	private String recid;
	/**签收表id*/
	private String type;
	
	/**票据号码*/
	@BtCompositeSearch(fieldName="origNo")
	private String origNo;
	
	/**票据类型：10 银票、11 商票*/
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	/**票据日期*/
	private String billDate;
	
	/**票据开始日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="startDate")
	private Date beginBillDate;
	/**票据开始日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="endDate")
	private Date endBillDate;
	
	/**票据类型名称：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="billType")
	private String billTypeName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="corpId")
	private String corpId;
	
	@BtCompositeSearch(fieldName="corpName")
	private String corpName;
	
	private String validSign;
	/**流通介质：10 纸票、11 电票*/
	// @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	private Integer billFlag;
	/**流通介质名称：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="billFlag")
	private String billFlagName;
	
	/**票据金额*/
	@BtCompositeSearch(fieldName="billMoney")
	private BigDecimal billMoney;
	/**
	 * 支付金额
	 */
	private BigDecimal payMoney;
	/**出票日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="startDate")
	private Date startDate;
	/**到期日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="endDate")
	private Date endDate;
	/**处理日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName="applyDate")
	private Date applyDate;
	/**出票人*/
	@BtCompositeSearch(fieldName="payer")
	private String payer;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="payee")
	private String payee;
	/**承兑人*/
	@BtCompositeSearch(fieldName="acceptor")
	private String acceptor;
	/**开出方式：0  对外开出；1 对内自开*/
	// @Dict(dictGroup = "AB_BILL_OPENWAY", inject = "insideSingName")
	@BtCompositeSearch(fieldName="insideSing")
	private Integer insideSing;
	/**业务处理状态*/
	// @Dict(dictGroup = "AB_BILL_SEARCHFOR_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="status")
	private String statusName;
	private String busTypeBriefId;
	/**业务状态*/
	// @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
	private String briefId ;
	@BtCompositeSearch(fieldName="briefId")
	private String briefIdName;
	/**追索通知操作业务状态*/
	// @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "optStatusName")
	private String optStatus ;
	@BtCompositeSearch(fieldName="optStatus")
	private String optStatusName;
	/**业务申请人名称*/
	@BtCompositeSearch(fieldName="reqNm")
	private String reqNm;
	/**业务接收人名称*/
	@BtCompositeSearch(fieldName="recNm")
	private String recNm;
	/**业务接收人账号*/
	@BtCompositeSearch(fieldName="recAcctId")
	private String recAcctId;
	/**接收人组织结构代码*/
	private String recCmonid;
	/**业务接收人开户行号*/
	private String recAcctsvcr;
	private String insideSingName;
	/**保证类型:01开票保证，02承兑保证；03背书保证*/
	// @Dict(dictGroup = "AB_BAIL_TYPE", inject = "bailTypeName")
	private String bailType;
	@BtCompositeSearch(fieldName="bailType")
	private String bailTypeName;
	@BtCompositeSearch(fieldName="bankAcc")
	private String bankAcc;
	/**处理标记 10正在处理11处理完成*/
	private Integer boxStatus;
	/**备注*/
	private String rmk;
	/**票据来源*/
	// @Dict(dictGroup = "AB_BILL_SOURCE_QUERY", inject = "sourceName")
	private String source;
	/**票据来源*/
	@BtCompositeSearch(fieldName="source")
	private String sourceName;
	/**应付票据标识：1是*/
	private String isEmitBill;
	/**是否已付款标记：1应付票据*/
	private String isPayed;
	/**被背书人（后手）*/
	private String endorse;
	/**外部系统id编码*/
	private String voucherId;
	/**是否发送指令标志：0否； 1是 ；*/
	// @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
	private String sendSign;
	private String sendSignName;
	/**业务状态*/
	// @Dict(dictGroup = "AB_BILL_BUSSTATUS", inject = "busStatusName")
	private Integer busStatus;
	/**指令状态*/
	// @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "instructStatusName")
	private String inoptStatus;
	/**业务状态名称*/
	private String busStatusName;
	/**指令状态名称*/
	private String instructStatusName;
	/**提示承兑业务状态*/
	// @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "tcBriefIdName")
	private String tcBriefId ;
	private String tcBriefIdName;
	/**提示承兑业务状态*/
	// @Dict(dictGroup = "AB_BILL_BUSSTATUS", inject = "tcBusStatusName")
	private Integer tcBusStatus;
	/**提示承兑指令状态*/
	// @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "tcInstructStatusName")
	private String tcInoptStatus;
	/**提示承兑业务状态名称*/
	private String tcBusStatusName;
	/**提示承兑指令状态名称*/
	private String tcInstructStatusName;
	/**提示收票业务状态*/
	// @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "tsBriefIdName")
	private String tsBriefId ;
	private String tsBriefIdName;
	/**提示收票业务状态*/
	// @Dict(dictGroup = "AB_BILL_BUSSTATUS", inject = "tsBusStatusName")
	private Integer tsBusStatus;
	/**提示收票指令状态*/
	// @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "tsInstructStatusName")
	private String tsInoptStatus;
	/**提示收票业务状态名称*/
	private String tsBusStatusName;
	/**提示收票指令状态名称*/
	private String tsInstructStatusName;
	/**退票业务状态*/
	// @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "tpBriefIdName")
	private String tpBriefId ;
	private String tpBriefIdName;
	/**退票业务状态*/
	// @Dict(dictGroup = "AB_BILL_BUSSTATUS", inject = "tpBusStatusName")
	private Integer tpBusStatus;
	/**退票指令状态*/
	// @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "tpInstructStatusName")
	private String tpInoptStatus;
	/**退票业务状态名称*/
	private String tpBusStatusName;
	/**退票指令状态名称*/
	private String tpInstructStatusName;
	/**资源信息*/
	private String resource;
	/**
	 * 出入池状态 AB_BILL_POOL_STATUS
	 * 0或空	未入池
	 * 1	已入池
	 * 2	入池在途
	 */
	// @Dict(dictGroup = "AB_BILL_POOL_STATUS", inject = "poolStatusName")
	private String poolStatus;
	private String poolStatusName;
	/**
	 * 出入池方式：1质押2托管
	 */
	// @Dict(dictGroup = "AB_POOL_INOUT_TYPE", inject = "inoutTypeName")
	private String inoutType;
	private String inoutTypeName;
	/**票据池名称**/
	private String bpName;
	/**
	 * 上一笔流水id
	 */
	private String lastId;
	/**
	 * 银行接口代码
	 */
	private String bifCode;
	/** 申请单位 */
	@Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	private String applyCorpName;
	/**申请单位代码*/
	private String applyCorpCode;
	/** 业务单位 */
	@Corp(inject="busCorpName")
	private String busCorpId;
	/** 业务单位名称 */
	private String busCorpName;
	/** 业务人员 */
	private String busPersonnel;
	/**
	 * 挂接付款余额
	 */
	@BtCompositeSearch(fieldName = "balance")
	private BigDecimal balance;
	/**被背书人账号*/
	private String pledgeeAcc;
	/**被背书人名称*/
	private String pledgeeName;
	/**被背书人开户行行号*/
	private String pledgeeBankAcc;
	/**被背书人开户行行名*/
	private String pledgeeBankName;
	// 新一代票据
	/** 出票标志 */
	@BtCompositeSearch(fieldName="info.newStmIssuofpprInd")
	//@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	@BtCompositeSearch(fieldName="info.newStmIssuofpprInd")
//	@Excel(name = "出票标志",isImportField="true")
	private String newStmIssuofpprIndName;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="info.blgrdbagcrclPrmtInd")
	//@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	@BtCompositeSearch(fieldName="info.blgrdbagcrclPrmtInd")
//	@Excel(name = "是否允许分包",isImportField="true")
	private String blgrdbagcrclPrmtIndName;
	/**不得转让标记 */
	@BtCompositeSearch(fieldName="info.banendrsmtmk")
	//@Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
	private String banendrsmtmk;
	@BtCompositeSearch(fieldName="info.banendrsmtmk")
//	@Excel(name = "不得转让标记",isImportField="true")
	private String banendrsmtmkName;
	/** 子票区间开始序号 */
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	private String sbbllRngEndSn;
	/** 标准金额 */
	private BigDecimal blStdAmt;
	/** 出票人统一信用代码 */
	private String blRmtrUnnIdNo;
	/** 收票人统一信用代码 */
	private String blRcvpymtpsUnnIdNo;
	/** 承兑人统一信用代码 */
	private String blAcptrUnnIdNo;
	/** 持票人统一信用代码 */
	private String blHoldUnnIdNo;
	private String ppFlg;

}
