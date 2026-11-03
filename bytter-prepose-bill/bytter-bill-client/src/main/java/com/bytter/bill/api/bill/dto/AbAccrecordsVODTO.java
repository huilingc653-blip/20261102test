package com.bytter.bill.api.bill.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.InputBankInfo;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 票据流水信息VO
 * @author onlineGenerator
 * @date 2017-06-21 15:36:23
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Data
public class AbAccrecordsVODTO extends AbVoucherinfoVODTO implements java.io.Serializable {
	/**流水ID*/
	private String id;
	/**流水数组IDs*/
	private String[] idArray;
	/**流水新状态*/
	private Integer statusNew;
	/**流水旧状态*/
	private Integer statusOld;
	/**流水指令处理新状态*/
	private String optStatusNew;
	/**流水指令处理旧状态*/
	private String optStatusOld;
	/**流水表id**/
	private String acid;
	/**票据主表ID*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**票据签收表id*/
	private String recid;
	/**票据划拨表ID*/
	private String trid;
	/**上一流水ID*/
	private String lastid;
	/**上一票据状态代码*/
	private String lastbillcode;
	/**处理标志：0、未完成；1可用；2已结束*/
	private String validSign;
	/**审批处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成*/
	private Integer status;
	/**操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签*/
	private String optStatus;
	/**是否发送指令标志：0否； 1是 ；*/
	@BtCompositeSearch(fieldName="sendSign")
	// @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
	private String sendSign;
	@BtCompositeSearch(fieldName="sendSign")
	private String sendSignName;
	/**返回代码*/
	@BtCompositeSearch(fieldName="returnCode")
	private String returnCode;
	/**处理结果*/
	@BtCompositeSearch(fieldName="returnMsg")
	private String returnMsg;
	/**业务类型*/
	@BtCompositeSearch(fieldName="briefId")
	// @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
	private String briefId;
	@BtCompositeSearch(fieldName="briefId")
	private String briefIdName;
	/**内部业务标志：0  否；1 是*/
	// @Dict(dictGroup="AB_ABBILLDISCOUNT_RECTYPE",inject = "insideSingName")
	private Integer insideSing;
	@BtCompositeSearch(fieldName="insideSing")
	private String insideSingName;
	/** 所属库存:10单位库存11中心代管12银行托管 */
	@BtCompositeSearch(fieldName="enterStock")
	// @Dict(dictGroup="AB_ENTER_STOCK",inject = "enterStockName")
	private String enterStock;
	@BtCompositeSearch(fieldName="enterStock")
	private String enterStockName;
	/**所属库存:10单位库存11中心代管12银行托管*/
	private String enterStockNew;
	/**所属库存:10单位库存11中心代管12银行托管*/
	private String enterStockOld;
	/**保证类型:01开票保证，02承兑保证；03背书保证*/
	private String bailType;
	/**不得转让标记：EM00 可再转让；EM01 不得转让*/
	private String bankendrsmtmk;
	/**追索同意清偿金额*/
	private BigDecimal rcrsokmoney;
	/**SU00-同意  SU01-拒绝*/
	private String signUpFlag;
	/**摘要*/
	private String abs;
	/**备注*/
	private String rmk;
	/**流水备注*/
	private String dtlRmk;
	/**是否挂授信*/
	private String ratmodel;
	/**授信代码*/
	private String creditCode;
	/**预算科目*/
	private String budgetCode;
	/**保证金账户*/
	private String bailAcc;
	/**保证金*/
	private BigDecimal bailmoney;
	/**保证金比例*/
	private BigDecimal bailpercent;
	/**保证金利率*/
	private BigDecimal bailrate;
	/**手续费*/
	private BigDecimal feemoney;
	/**追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）*/
	private String rcrsrsncd;
	/**追索种类：RT00 拒付追索；RT01 非拒付追索*/
	private String rcrsTp;
	/**收款或付款确认人*/
	private String signerok;
	/**银行接口代码*/
	private String bifCode;
	/**业务申请人类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司*/
	private String reqRoleName;
	// @Dict(dictGroup="AB_BILL_ROLE",inject="reqRoleName")
	private String reqRole;
	/**业务申请人名称*/
	private String reqNm;
	/**业务申请人组织机构代码*/
	private String reqCmonid;
	/**业务申请人账号*/
	private String reqAcctId;
	/**业务申请人开户行号*/
	@InputBankInfo(inject="reqAcctsvcrName")
	private String reqAcctsvcr;
	/**业务申请人开户行号*/
	private String reqAcctsvcrName;
	/**业务申请人承接行号*/
	private String reqAcctsvcrAg;
	/**业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司*/
	private String recRoleName;
	// @Dict(dictGroup="AB_BILL_ROLE",inject="recRoleName")
	private String recRole;
	/**业务接收人名称*/
	private String recNm;
	/**业务接收人组织机构代码*/
	private String recCmonid;
	/**业务接收人账号*/
	private String recAcctId;
	/**业务接收人开户行号*/
	@InputBankInfo(inject="recAcctsvcrName")
	private String recAcctsvcr;
	private String recAcctsvcrName;
	/**业务接收人承接行号*/
	private String recAcctsvcrAg;
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
	/**保证人地址*/
	private String adr;
	/**开票信用等级*/
	private String cdtRatgs;
	/**开票评级机构*/
	private String cdtRatgGgcy;
	/**开票评级到期日*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date cdtRatgDuedt;
	/**清算方式 SM00线上清算、SMO1线下清算*/
	// @Dict(dictGroup = "AB_STTLMMK", inject ="sttlmmkName")
	private String sttlmmk;
	private String sttlmmkName;
	/**贴现种类：RM00 买断式、RM01 回购式买断式*/
	// @Dict(dictGroup = "AB_DISCOUNT_RPDMK", inject ="rpdmkName")
	private String rpdmk;
	private String rpdmkName;
	/**全局业务ID*/
	private String busGlobalId;
	/**附件ID*/
	private String attachmentId;
	/**附件数量*/
	private Integer attachmentNum;
//	private RatUrationDTO urationDTO;
	/**是否挂内部授信 0否； 1是  */
	private String insideRatModel;
	/**是否挂内部授信 页面参数 0否； 1是  */
	private String isHangCredit;
	/**内部授信代码(多个授信用;分开) */
	private String insideCreditCode;
	/**开出方式（10票据质押、20保证金、30信用）*/
	// @Dict(dictGroup="ACCEBILL_EMIT_TYPE",inject = "emitTypeName")
	private String emitType;
	private String emitTypeName;
	/**是否开票保贴(1是0否) */
	// @Dict(dictGroup="sf_yn",inject = "emitGuandisSignName")
	private String emitGuandisSign;
	private String emitGuandisSignName;
	/**
	 * 授信编号(必填)
	 */
	private String cpUrationId;
	/**
	 * 银行编号
	 */
	private String cpRatCode;
	private String cpRatItemCode;
	private String cpRatItemName;
	private boolean isImport;
	/**定时器标志: 默认为false,为true时表示定时任务*/
	private boolean isTask;
	/**持票单位*/
	@BtCompositeSearch(fieldName="corpId")
	// @Corp(inject="corpName")
	private String corpId;
	/**持票单位代码*/
	private String corpCode;
	/**持票单位名称*/
	@BtCompositeSearch(fieldName="corpName")
	private String corpName;
	/**持票单位代码加名称*/
	@BtCompositeSearch(fieldName="corpCodeAndName")
	private String corpCodeAndName;
	@BtCompositeSearch(fieldName="bankAcc")
	private String bankAcc;
	private String bankCode;
	private String bankName;
	/** 线上/线下识别 1/0 **/
	private String circulationMode;
	/**票据号码*/
	@BtCompositeSearch(fieldName="origNo")
	private String origNo;
	/**业务类型*/
	// @Dict(dictGroup = "AB_BILL_BUS_TYPE", inject = "busTypeName")
	private String busType;
	@BtCompositeSearch(fieldName="busType")
	private String busTypeName;
	/**流通介质：10 纸票、11 电票*/
	// @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	@BtCompositeSearch(fieldName="billFlag")
	private Integer billFlag;
	/**流通介质名称：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="billFlag")
	private String billFlagName;
	/**票据金额*/
	@BtCompositeSearch(fieldName="billMoney")
	private BigDecimal billMoney;
	/**票据类型：10 银票、11 商票*/
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	@BtCompositeSearch(fieldName="billType")
	private Integer billType;
	/**票据类型名称：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="billType")
	private String billTypeName;
	/**创建人*/
	@BtCompositeSearch(fieldName="applyName")
	private String applyName;
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
	/**业务归属日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="businessDate")
	private Date businessDate;
	/**贴现利息*/
	@BtCompositeSearch(fieldName="interest")
	private BigDecimal interest;
	/**贴现利率*/
	@BtCompositeSearch(fieldName="interestRates")
	private BigDecimal interestRates;
	/**贴现日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date discountDate;
	/**实付金额*/
	private BigDecimal realMoney;
	/**入账账户*/
	private String aoaccninfId;
	/**前手*/
	@BtCompositeSearch(fieldName="cnAccrecords")
	private String cnAccrecords;
	/**后手*/
	@BtCompositeSearch(fieldName="rearAccrecords")
	private String rearAccrecords;
	/**业务状态*/
	// @Dict(dictGroup = "AB_BILL_BUSSTATUS", inject = "busStatusName")
	@BtCompositeSearch(fieldName="busStatus")
	private Integer busStatus;
	/**指令状态*/
	// @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "instructStatusName")
	private String inoptStatus;
	/**业务状态名称*/
	@BtCompositeSearch(fieldName="busStatus")
	private String busStatusName;
	/**指令状态名称*/
	@BtCompositeSearch(fieldName="inoptStatus")
	private String instructStatusName;
	/**票据来源*/
	// @Dict(dictGroup = "AB_BILL_SOURCE_QUERY", inject = "sourceName")
	private String source;
	/**票据来源*/
	@BtCompositeSearch(fieldName="source")
	private String sourceName;
	/**出票人*/
	@BtCompositeSearch(fieldName="payer")
	private String payer;
	/**出票人银行*/
	@BtCompositeSearch(fieldName="payerBankName")
	private String payerBankName;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="payee")
	private String payee;
	/**身份不允许操作 1为不允许操作 0或空允许操作 */
	private String isNotOp;
	/** 申请单号 */
	private String appNo;
	/** 票据来源：10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入 */
	private String billSource;
	/** 类型 0应付票据，1应收票据*/
	@BtCompositeSearch(fieldName="type")
	private String type;
	/** 交票人(领票人)*/
	private String trader;
	/**
	 * 接口发送消息ID
	 */
	private String msgId;
	/** 担保信息相关//
	 /**是否启用挂担保合同(根据授信基础参数设置，需给默认值，1=启用，0=不启用)*/
	private String isHangGuarantee;
	/**担保合同ID**/
	private List<String> contractIds;
	/**担保合同**/
	private String guaContract;
	/**担保备注**/
	private String guaranteeRmk;
	/** 担保方式名称 **/
	private String guaWayNames;
	/** 担保方式code **/
	private String guaWay;
	/** 逾期托收原因说明 */
	private String rsn;
	/**票据状态*/
	// @Dict(dictGroup = "AB_BILL_STATUS", inject="billStatusName")
	@BtCompositeSearch(fieldName="billStatus")
	private String billStatus;
	@BtCompositeSearch(fieldName="billStatus")
	private String billStatusName;
	/**票据池状态空或0池外1池内2入池在途3出池在途*/
	// @Dict(dictGroup = "AB_BILL_POOL_STATUS", inject = "poolStatusName")
	private String poolStatus;
	/**票据池状态*/
	private String poolStatusName;
	@BtCompositeSearch(fieldName="banktypeId")
	private String bankTypeId;
	/**所属账户银行类别*/
	@BtCompositeSearch(fieldName="bankTypeId")
	private String bankTypeName;
	/**汇率*/
	private BigDecimal[] exchangeRate;
	/**执行金额（折合本身币别）*/
	private BigDecimal[] processCurMoney;
	/**自动签收: 1是0否 */
	// @Dict(dictGroup="sf_yn",inject = "isAutosignName")
	private String isAutosign;
	@BtCompositeSearch(fieldName="isAutosign")
	private String isAutosignName;
	private String billId;
	/**票据池状态空或0池外1池内*/
	// @Dict(dictGroup = "AB_BILL_COUNT_POOL_STATUS", inject = "countPoolStatusName")
	@BtCompositeSearch(fieldName="countPoolStatus")
	private String countPoolStatus;
	/**票据池状态*/
	private String countPoolStatusName;
	/**
	 * 票据池id
	 */
	@BtCompositeSearch(fieldName="bpId")
	private String bpId;
	/**
	 * 票据池名称
	 */
	private String bpName;
	/**
	 * 是否统计在池和非在池
	 */
	// @Dict(dictGroup = "sf_yn", inject = "countIsPoolName")
	private String countIsPool;
	private String countIsPoolName;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="applyCorpId")
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	@BtCompositeSearch(fieldName="applyCorpName")
	private String applyCorpName;
	/**申请单位代码加名称*/
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
	/**承兑人*/
	@BtCompositeSearch(fieldName="acceptor")
	private String acceptor;
	/**
	 * 出票标志
	 */
	@BtCompositeSearch(fieldName="newStmIssuofpprInd")
	private String newStmIssuofpprInd;
	/**
	 * 是否允许分包
	 */
	@BtCompositeSearch(fieldName="blgrdbagcrclPrmtInd")
	private String blgrdbagcrclPrmtInd;
	/**
	 * 批次号
	 */
	@BtCompositeSearch(fieldName="batchNo")
	private String batchNo;
	private String sbbllRngStrtSn;
	private String sbbllRngEndSn;
	private BigDecimal blStdAmt;
	private String socCode;
	private Date beginDate;
	private Boolean firstTime;
	/**
	 * 内部交易线上背给线下自动登记标志：1-已登记 0-未登记
	 */
	private String insideEndorseAutoRegSign;

	/**
	 * 1是复制数据2是批量拆分数据
	 */
	private String copyFlag = "0";

	private String splitFlg;//SPLIT_FLG  此笔交易是否拆分，1是，其他为未拆

	private String oldAccId;//OLD_ACC_ID  记录拆分前的流水id，若是未拆分为空
	public boolean isTask() {
		if(StringUtils.isNotEmpty (isTask)){
			return isTask;
		}
		return false;
	}
	private Long billNums;
	private BigDecimal releasePaymentMoney;
	private BigDecimal accountBalance;
	private BigDecimal bankAccBalance;
	private String companyTYpeName;

	private String endDateStr;

	private BigDecimal xbMoney;
}
