package com.bytter.bill.api.bill.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 票据主信息Vo
 * @author onlineGenerator
 * @date 2017-06-21 15:50:47
 * @version 1.0
 *
 */
@Data
public class AbVoucherinfoVODTO implements java.io.Serializable {
	private String id;
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**签收表ID*/
	private String recId;
	/**流水表ID*/
	private String accId;
	/**流水表IDS*/
	private String accIds;
	/*** 查询ecds票据 操作类型 front正面，back背面  */
	private String opType;
	/*** 来源：sign 签收业务 */
	private String isSign;
	/*** 业务类型 */
	private String briefId;
	/*** 业务标志*/
	private String briefIdName;
	 /**票据号码*/
	private String origNo;
	/**票据类型：10 银票、11 商票*/
	// @Dict(dictGroup="AB_BILL_TYPE",inject="billTypeName")
	private Integer billType;
	private String billTypeName;
	/**不得转让标记EM00可再转让EM01不得转让*/
	// @Dict(dictGroup="AB_BANEM",inject="banendrsmtmkName")
	private String banendrsmtmk;
	private String banendrsmtmkName;
	/**处理标记 10正在处理11处理完成*/
	private Integer boxStatus;
	/**票据状态*/
	// @Dict(dictGroup = "AB_BILL_STATUS", inject="billStatusName")
	private String billStatus;
	private String billStatusName;
	/**流通介质：10 纸票、11 电票*/
	// @Dict(dictGroup="AB_BILL_FLAG",inject="billFlagName")
	private Integer billFlag;
	private String billFlagName;
	/**票据金额*/
	private BigDecimal billMoney;
	/**票据金额中文*/
	private String billMoneyCNY;
	/**出票日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	/**到期日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	/**出票人*/
	private String payer;
	/**出票人账号*/
	private String payeracc;
	/**出票人开户银行名称*/
	private String payerBankName;
	/**出票人开户银行行号*/
	private String payerbankno;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="payee")
	private String payee;
	/**收票人账号*/
	private String payeeacc;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**承兑人*/
	private String acceptor;
	/**承兑人账号*/
	private String acceptoracc;
	/**承兑人开户行行号*/
	private String acceptorbankno;
	/**承兑人开户行名称*/
	private String acceptorBankName;
	/**承兑人开户行地址*/
	private String acceptorbankaddress;
	/**瑕疵类型 通过数据字典维护*/
	private String arXclx;
	/**承兑合同号*/
	private String acontractNo;
	/**发票号*/
	private String ainvcnb;
	/**承兑协议编号*/
	private String accptncagrmtnb;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	/**开票信用等级*/
	private String payerCdtRatgs;
	/**开票评级机构*/
	private String payerCdtRatgGgcy;
	/**开票评级到期日*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date payerCdtRatgDuedt;
	private String oldDraftId;//OLD_DRAFT_ID;
	/**
	 * 是否第三方票据类型非商票和银票即为第三方票据类型
	 * 1为是0为否
	 */
	private String isBillTypeTP;
	/**
	 * 是否显示银行同步按钮
	 * 1为是0为否
	 */
	private String isShowSyncro;
	/**票据池状态空或0池外1池内*/
	// @Dict(dictGroup = "AB_BILL_COUNT_POOL_STATUS", inject = "countPoolStatusName")
	private String countPoolStatus;
	/**票据池状态*/
	private String countPoolStatusName;
	/**
	 * 票据池id
	 */
	private String bpId;
	/**
	 * 票据池名称
	 */
	private String bpName;
	/** 申请单位 */
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	private String applyCorpName;
	/**申请单位代码*/
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	private String busCorpId;
	/** 业务单位名称 */
	private String busCorpName;
	/** 业务人员 */
	private String busPersonnel;
	// 新一代票据
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
	/**
	 * 出票标志 0：老系统出票,   1：新系统出票。默认值为0，不能为空
	 */
	@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	/**
	 * 出票标志状态说明
	 */
	private String newStmIssuofpprIndName;
	/**
	 * 是否允许分包。1 是  0 否，默认为否  新系统出票标志为"0"(老系统出 票)时，只能填写"0"(不可分包流 转)
	 */
	@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject="blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	/**
	 * 是否允许分包状态说明
	 */
	private String blgrdbagcrclPrmtIndName;
	/**
	 * 票据流通状态
	 */
	private String ebillBlCrclIndc;
	/**
	 * 票据业务类型代码
	 */
	private String blBsnTpcd;

	private String riskBillType; //风险票据类型
	private String disountIncIf; //是否已包贴
	private String cashIncIf; //是否已保兑
	private String avoidClaimDebtsIf; //是否免追索票据
	private String payerChannelName; //出票人办理渠道名称
	private String acceptorChannelName; //承兑人办理渠道名称
	private String payeeChannelName; //收票人办理渠道名称

	private String billShowType; //票据显示类型 front 正面、back 背面、additional 附加、all 所有

	private String batchNo;
}
