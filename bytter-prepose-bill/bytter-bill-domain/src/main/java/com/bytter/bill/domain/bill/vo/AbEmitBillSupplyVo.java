package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 开票补录VO
 * @author luop
 * @date 2017-06-21 18:00:00
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@ExcelTarget("AbEmitBillSupplyVo")
@Data
public class AbEmitBillSupplyVo extends AbAccrecordsVO implements Serializable,IBusinessKey {
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**票据流水id*/
	private String aid;
	/**开票登记单号*/
	@Excel(name = "*开票登记单号",isImportField="true")
	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_APPNO_ISNOTNULL)
	@BtCompositeSearch(fieldName="B.APP_NO")
	private String appNo;
	private String businessKey;
	/**收票人单位id（前后端分离详情/审批返回）*/
	private String payeeCorpId;
	/**账户是否离线 0离线（前后端分离详情/审批返回）*/
	private String isOnline;
	/**票据类型：10 银票、11 商票*/
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	@BtCompositeSearch(fieldName="BILL_TYPE")
	private String billTypeName;
	/**流通介质：10 纸票、11 电票*/
	// @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	private Integer billFlag;
	@BtCompositeSearch(fieldName="BILL_FLAG")
	private String billFlagName;
	/**开出方式：0  对外开出；1 对内自开*/
	// @Dict(dictGroup = "AB_BILL_OPENWAY", inject = "insideSingName")
	private Integer insideSing;
	@BtCompositeSearch(fieldName="C.INSIDE_SING")
	private String insideSingName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="b.corp_id")
	private String corpId;
	/**出票人*/
	@BtCompositeSearch(fieldName="PAYER")
	@Excel(name = "出票人",isImportField="true")
	private String payer;
	/**承兑人*/
	@BtCompositeSearch(fieldName="ACCEPTOR")
	@Excel(name = "承兑人",isImportField="true")
	private String acceptor;
	/**票据号码*/
	@BtCompositeSearch(fieldName="ORIG_NO")
	@Excel(name = "票据号码",isImportField="true")
	private String origNo;
	/**票据金额*/
	@BtCompositeSearch(fieldName="BILL_MONEY")
	private BigDecimal billMoney;
	/**出票日期*/
	@BtCompositeSearch(fieldName="START_DATE")
	@Excel(name = "出票日期",format = "yyyy-MM-dd",isImportField="true")
    @JsonFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	/**到期日期*/
	@BtCompositeSearch(fieldName="END_DATE")
	@Excel(name = "到期日期",format = "yyyy-MM-dd",isImportField="true")
    @JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="PAYEE")
	private String payee;
	/**票据状态*/
	@BtCompositeSearch(fieldName="BILL_STATUS")
	private String billStatus;
	/**外部系统id编码*/
	@BtCompositeSearch(fieldName="VOUCHER_ID")
	private String voucherId;
	/**计划科目*/
	private String[] itemCode;
	/**计划科目名称*/
	private String[] itemName;
	/**执行金额*/
	private BigDecimal[] processMoney;
	/**单位名称*/
	@BtCompositeSearch(fieldName="b.corp_code")
	private String corpName;
	/**开票数据来源*/
	// @Dict(dictGroup = "AB_EMITBILL_SOURCE", inject = "sourceName")
	private String source;
	@BtCompositeSearch(fieldName="B.SOURCE")
	private String sourceName;
	/**申请日期 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date applyDate;
	/**出票人账号*/
	@Excel(name = "出票人账号",isImportField="true")
	private String payeracc;
	/**出票人开户银行名称*/
	@Excel(name = "出票人开户银行名称",isImportField="true")
	private String payerBankName;
	/**出票人开户银行行号*/
	@Excel(name = "出票人开户银行行号",isImportField="true")
	private String payerbankno;
	/**收票人账号 */
	private String payeeacc;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**承兑人账号 */
	@Excel(name = "承兑人账号",isImportField="true")
	private String acceptoracc;
	/**承兑人开户行名称*/
	@Excel(name = "承兑人开户行名称",isImportField="true")
	private String acceptorBankName;
	/**承兑人开户行行号*/
	@Excel(name = "承兑人开户行行号",isImportField="true")
	private String acceptorbankno;
	/**承兑人开户行地址*/
	@Excel(name = "承兑人开户行地址",isImportField="true")
	private String acceptorbankaddress;
	/**承兑合同号/交易合同号*/
	@Excel(name = "承兑合同号",isImportField="true")
	private String acontractNo;
	/**前手*/
	private String cnAccrecords;
	/**发票号*/
	@Excel(name = "发票号",isImportField="true")
	private String ainvcnb;
	/**承兑协议编号 */
	@Excel(name = "承兑协议编号",isImportField="true")
	private String accptncagrmtnb;
	/**不得转让标记EM00可再转让EM01不得转让*/
	// @Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
	private String banendrsmtmk;
	@Excel(name = "不得转让标记",isImportField="true")
	private String banendrsmtmkName;
	/**授信占用/授信代码 */
	private String creditCode;
	/**是否挂保证金*/
	private String bailmodel;
	/**保证金额度单Id*/
	private String bondId;
	/**保证金合同*/
	private String bailContract;
	/**保证金比例*/
	@Excel(name = "保证金比例(%)",isImportField="true")
	private BigDecimal bailpercent;
	/**保证金金额*/
	@Excel(name = "保证金",isImportField="true")
	private BigDecimal bailmoney;
	/**保证金利率 */
	@Excel(name = "保证金利率(%)",isImportField="true")
	private BigDecimal bailrate;
	/**缴存保证金账户*/
	@Excel(name = "保证金账户",isImportField="true")
	private String bailAcc;
	/**手续费 */
	@Excel(name = "手续费",isImportField="true")
	private BigDecimal feemoney;
	/**摘要*/
	@Excel(name = "摘要",isImportField="true")
	private String abs;
	/**备注*/
	@Excel(name = "备注",isImportField="true")
	private String rmk;
	/**业务归属日期*/
	@Excel(name = "*业务办理日期",format = "yyyy-MM-dd",isImportField="true")
    @JsonFormat(pattern="yyyy-MM-dd")
	private Date businessDate;
	/**处理标记：10正在处理11处理完成*/
	@BtCompositeSearch(fieldName="box_status")
	private Integer boxStatus;
	/**补录业务处理状态：95:待补录，11审批中，-1打回*/
	// @Dict(dictGroup = "AB_BILL_ABEMITBILLSUPPLY_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="c.status")
	private String statusName;
	/**操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签*/
	@BtCompositeSearch(fieldName="opt_status")
	private String optStatus;
	/**开票信用等级*/
	@Excel(name = "开票信用等级",isImportField="true")
	private String payerCdtRatgs;
	/**开票评级机构*/
	@Excel(name = "开票评级机构",isImportField="true")
	private String payerCdtRatgGgcy;
	/**开票评级到期日*/
	@Excel(name = "开票评级到期日",format = "yyyy-MM-dd",isImportField="true")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date payerCdtRatgDuedt;
	/**保证人地址*/
	//@Excel(name = "保证人地址",isImportField="true")
	private String adr;
	/**客商所在省(承兑人)*/
	private String acceptProvince;
	/**客商所在市(承兑人)*/
	private String acceptCity;
	/**客商银行类别id(承兑人)*/
	private String acceptBankTypeId;
	/**是否隐藏复选框 1为是*/
	private String checkboxHide;
	/**是否发送指令标志：0否； 1是 ；*/
	@BtCompositeSearch(fieldName="c.send_Sign")
	// @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
	private String sendSign;
	@BtCompositeSearch(fieldName="c.send_Sign")
	private String sendSignName;
	/**是否启用授信敞口(必填，根据授信基础参数设置，需给默认值，1=启用，0=不启用)*/
	private String isOpenRat;
	/**
	 * 银行授信单位类型 开票人=0、承兑人=1
	 */
	private String ratBankCorpType;
	/**
	 * 银行授信单位Id
	 */
	private String ratBankCorpId;
	/**
	 * 保证金类型，1定期保证金，0活期保证金
	 */
	private String bailType;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="b.APPLY_CORP_ID")
	private String applyCorpId;
	/**申请单位名称*/
	@BtCompositeSearch(fieldName="b.APPLY_CORP_CODE")
	private String applyCorpName;
	/**申请单位代码*/
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="b.BUS_CORP_ID")
	private String busCorpId;
	/** 业务单位名称 */
	@BtCompositeSearch(fieldName="b.BUS_CORP_NAME")
	private String busCorpName;
	/** 业务人员 */
	@BtCompositeSearch(fieldName="b.BUS_PERSONNEL")
	private String busPersonnel;
	/**
	 * 授信银行类型：0承兑人开户银行 1出票人开户银行
	 */
	private String ratBankType;
	/**
	 * 出票标志 0：老系统出票   1：新系统出票
	 */
	@BtCompositeSearch(fieldName="a.NEW_STM_ISSUOFPPR_IND")
	private String newStmIssuofpprInd;
	/**
	 * 是否允许分包 1 是  0 否，默认为否
	 */
	@BtCompositeSearch(fieldName="a.BLGRDBAGCRCL_PRMT_IND")
	private String blgrdbagcrclPrmtInd;
	/**
	 * 是否协议出票
	 */
	@BtCompositeSearch(fieldName="a.AGREE_BILL_FLG")
	private String agreeBillFlg;
	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
			//审批列表查询
			return "0";
		}
		//审批中 11
		if(AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())){
			return "1";
		}else{
			return "0";
		}
	}
	@Override
	public String getBeanName() {
		return "abEmitBillSupplyService";
	}
	
}
