package com.bytter.bill.api.bill.dto;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 开票登记VO
 * @author luop
 * @date 2017-06-21 18:00:00
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbEmitBillVoDTO extends AbAccrecordsVODTO {
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**票据流水id*/
	private String aid;
	/**票据类型：10 银票、11 商票*/
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;

	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_BILLTYPE_ISNOTNULL)
	private String billTypeName;
	/**流通介质：10 纸票、11 电票*/
	// @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	private Integer billFlag;
	@BtCompositeSearch(fieldName="a.BILL_FLAG")

	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_BILLFLAG_ISNOTNULL)
	private String billFlagName;
	/**开出方式：0  对外开出；1 对内自开*/
	// @Dict(dictGroup = "AB_BILL_OPENWAY", inject = "insideSingName")
	private Integer insideSing;
	@BtCompositeSearch(fieldName="C.INSIDE_SING")

	@NotNull(message = AccebillConstant.AB_BILL_EXCEL_ERRORMSG_INSIDESING_NULL)
	private String insideSingName;
	/**开票登记单号*/
	@BtCompositeSearch(fieldName="B.APP_NO")
	private String appNo;
	/**开票批次号*/
	@BtCompositeSearch(fieldName="B.BATCH_NO")
	private String batchNo;
	private String businessKey;
	/**开票数据来源*/
	// @Dict(dictGroup = "AB_EMITBILL_SOURCE", inject = "sourceName")
	private String source;
	@BtCompositeSearch(fieldName="B.SOURCE")
	private String sourceName;
	/**外部系统id编码*/
	@BtCompositeSearch(fieldName="VOUCHER_ID")
	private String voucherId;
	/**资金计划科目代码信息*/
	private String itemCodeStr;
	/**计划科目*/
	private String[] itemCode;
	/**计划科目名称*/
	private String[] itemName;
	/**执行金额*/
	private BigDecimal[] processMoney;
	/**持票单位*/

	private String corpId;
	/**出票人*/

//	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_PAYER_ISNOTNULL)
	private String payer;
	/**承兑人*/

	private String acceptor;
	/**票据号码*/

	private String origNo;
	/**票据金额*/

	private BigDecimal billMoney;
	/**出票日期*/

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/**到期日期*/

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/**收票人全称--用于对外开票时提交数据和数据库查询*/

	private String payee;
	/**收票人单位id--用于编辑对内的开票申请时，内部成员单位回显*/
	private String payeeCorpId;
	/**收票人全称--用于对内开票时提交数据*/
	private String payee1;
	/**票据状态*/

	private String billStatus;
	/**单位名称*/

	private String corpName;

	private String corpCode;
	/**申请日期 */
	private Date applyDate;
	/**出票人账号*/

	private String payeracc;
	/**出票人开户银行名称*/

	private String payerBankName;
	/**出票人开户银行行号*/

	private String payerbankno;
	/**收票人账号 --用于对外开票时提交数据和数据库查询*/

	@BtCompositeSearch(fieldName="a.payeeacc")
	private String payeeacc;
	/**收票人账号 --用于对内开票时提交数据*/
	private String payeeacc1;
	/**收票人开户银行名称*/

	@BtCompositeSearch(fieldName="a.payee_bank_name")
	private String payeeBankName;
	/**收票人开户银行行号*/

	private String payeebankno;
	/**承兑人账号 */

//	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORACC_BILLFLAG11_ISNOTNULL)
	private String acceptoracc;
	/**承兑人开户行名称*/

	private String acceptorBankName;
	/**承兑人开户行行号*/

	private String acceptorbankno;
	/**承兑人开户行地址*/

	private String acceptorbankaddress;
	/**承兑合同号/交易合同号*/

	private String acontractNo;
	/**前手*/
	private String cnAccrecords;
	/**发票号*/

	private String ainvcnb;
	/**合作银行号 */

	private String cooperativeBankNo;
	/**承兑协议编号 */

	private String accptncagrmtnb;
	/**不得转让标记EM00可再转让EM01不得转让*/
	// @Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
	private String banendrsmtmk;

	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_BANENDRSMTMK_ISNOTNULL)
	private String banendrsmtmkName;
	/**授信占用/授信代码 */
	private String creditCode;
	/**是否挂保证金*/
	private String bailmodel;
	/**是否启用授信敞口(必填，根据授信基础参数设置，需给默认值，1=启用，0=不启用)*/
	private String isOpenRat;
	/**保证金额度单Id*/
	private String bondId;
	/**保证金合同*/
	private String bailContract;
	/**保证金比例*/

	private BigDecimal bailpercent;
	/**保证金金额*/

	private BigDecimal bailmoney;
	/**保证金利率 */

	private BigDecimal bailrate;
	/**缴存保证金账户*/

	private String bailAcc;
	/**手续费 */

	private BigDecimal feemoney;
	/**摘要*/

	private String abs;
	/**备注*/

	private String rmk;
	/**业务归属日期*/

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date businessDate;
	/**处理标记：10正在处理11处理完成*/

	private Integer boxStatus;
	/**业务处理状态：-2已删除；-1打回；0 暂存；1导入;11-94审批中；95 已完成*/
	// @Dict(dictGroup = "AB_BILL_EMITBILL_STATUS", inject = "statusName")
	private Integer status;

	private String statusName;
	/**操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签*/
	private String optStatus;
	/**开票信用等级*/

	private String payerCdtRatgs;
	/**开票评级机构*/

	private String payerCdtRatgGgcy;
	/**开票评级到期日*/

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date payerCdtRatgDuedt;
	/**保证人地址*/
	//@Excel(name = "保证人地址",isImportField="true")
	private String adr;
	/**付款打回原因*/
	private String reason;
	/**接口调用标志:接口生成从表数据时，设置该值为true则不生成创建人信息，用于查询创建人为空的列表数据*/
	private boolean interfaceCall;
	/**兑付单位*/
	private String payCorpId;
	private String payCorpName;
	/**兑付金额*/
	private BigDecimal payMoney;
	/**收款账号*/
	private String payPayeeacc;
	/**收款账号名称*/
	private String payPayeeName;
	/**收款开户行行号*/
	private String payPayeeBankNo;
	/**收款开户行名称*/
	private String payPayeeBankName;
	/**兑付用途*/
	private String payUse;
	/**兑付备注*/
	private String payAbs;
	/**
	 * 兑付方式:0或空线下兑付,1线上兑付'
	 */
	// @Dict(dictGroup = "AB_BILL_ABACCRECORDS_EMITSTTLMMK", inject = "emitSttlmmkName")
	private String emitSttlmmk;
	private String emitSttlmmkName;
	/**所在省*/
	private String prov;
	private String provName;
	/**所在市*/
	private String city;
	private String cityName;
	/**客商所在省(承兑人)*/
	private String acceptProvince;
	/**客商所在市(承兑人)*/
	private String acceptCity;
	/**客商银行类别id(承兑人)*/
	private String acceptBankTypeId;
	/**是否隐藏复选框 1为是*/
	private String checkboxHide;
	/**是否发送指令标志：0否； 1是 ；*/

	// @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
	private String sendSign;

	private String sendSignName;
	/**资金占用VO**/
	private String openBillInfo;
//	private RatUrationDTO isBailOccupyVo;
	/**
	 * 授信占用金额 -启用授信敞口，
	 * 授信占用金额=票面金额-保证金金额
	 * 不启用授信产口，授信占用金额=票面金额**/
	private BigDecimal ratBillMoney;
	/**
	 * 是否线上标识
	 */
	private String isOnline;
	/**
	 * 是否第三方票据类型非商票和银票即为第三方票据类型
	 * 1为是0为否
	 */
	private String isBillTypeTP;
	/**
	 * 银行授信单位类型 开票人=0、承兑人=1
	 */
	private String ratBankCorpType;
	/**
	 * 银行授信单位Id
	 */
	private String ratBankCorpId;
	/**
	 * 是否付款新增数据1为是
	 */
	private String isNisPayAdd;
	/**创建人登录名称*/
	private String createBy;
	/**
	 * 是否批次开票数据1为是
	 */
	private String isBatchBill;
	/**开出方式（10票据质押、20保证金、30信用）*/
	// @Dict(dictGroup="ACCEBILL_EMIT_TYPE",inject = "emitTypeName")
	private String emitType;

	private String emitTypeName;

	private String isEnsureName;
	// @Dict(dictGroup = "sf_yn", inject = "isEnsureName")
	private String isEnsure;
	/**
	 * 付款单号
	 */
	private String payNo;
	/**
	 * 付款单id
	 */
	private String payId;
	/**
	 * 保证金类型
	 */
	// @Dict(dictGroup = "AB_BILL_BAIL_TYPE", inject = "bailTypeName")
	private String bailType;
	private String bailTypeName;
	/** 申请单位 */
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
	/**是否保存客商资料*/
	private String isExternalCorp;
	/**是否共享*/
	private String isCommon;
	/**
	 * 授信银行类型：0承兑人开户银行 1出票人开户银行
	 */
	private String ratBankType;
	/**
	 * 是否授信提用：1是 0否
	 */
	private String isCreditDraw;
	/**
	 * 出票标志
	 */
	//@Excel(name = "*出票标志")
	//@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	@BtCompositeSearch(fieldName="a.new_Stm_Issuofppr_Ind")
	private String newStmIssuofpprInd;
	@NotNull(message = "出票标志不能为空")
	private String newStmIssuofpprIndName;
	/**
	 * 是否允许分包
	 */
	//@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	@BtCompositeSearch(fieldName="a.blgrdbagcrcl_Prmt_Ind")
	private String blgrdbagcrclPrmtInd;
	@NotNull(message = "是否允许分包不能为空")
	private String blgrdbagcrclPrmtIndName;
	/**
	 * 出票人统一信用代码
	 */
	private String blRmtrUnnIdNo;
	/**
	 * 出票人统一信用代码
	 */
	private String blRcvpymtpsUnnIdNo;
	/**
	 * 出票人统一信用代码
	 */
	private String blAcptrUnnIdNo;
	/**
	 * 承兑人识别类型
	 */
	private String blAcptrTpcd;
	/**
	 * 收款人人识别类型
	 */
	private String blRcvpymtpsIdTpcd;
	/**
	 * 子票区间开始序号
	 */
	private String sbBllRngStrtSN;
	/**
	 * 子票区间结束序号
	 */
	private String sbBllRngEndSN;
	/**
	 * 标准金额
	 */
	private BigDecimal blStdAmt;
	/**
	 * 手续费比例
	 */
	private BigDecimal feemoneyScale;

	private String bankTypeId;

	/**
	 * 是否是付款办理弹窗开票
	 */
	private String isPayFlg;

	/**
	 * 是否批量导入0否1是
	 */
	private String isImport;

	private BigDecimal transMoney;

	private String accptncagrmtBranch;
	private String accptncagrmtBranchNo;
	private String cvmLimitNo;
	private String riskContractNumber;
	private String cmbBnkbilnegId;//招行合作框架协议表id
	private String cmbCrlConId;//招行风险合同信息表id

	/**
	 * 招行列表保证金选择标识 0-否 1-是
	 */
	private String cmbBailFlag;

	/**
	 * 批次开票顺序号
	 */
	private Integer batchDtlNo ;

	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
			//审批列表查询
			return "0";
		}
		//审批中 11 已删除 -2 已完成 95
		if(AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())
				|| AccebillConstant.AB_BILL_BUS_STATUS_2.equals(this.getStatus())
				|| AccebillConstant.AB_BILL_BUS_STATUS95.equals(this.getStatus())
				||this.getBillFlag()==null//流通介质为空
				||this.getBillType()==null//票据类型为空
				||this.getInsideSing()==null){//开出方式为空
			return "1";
		}else{
			return "0";
		}
	}
}
