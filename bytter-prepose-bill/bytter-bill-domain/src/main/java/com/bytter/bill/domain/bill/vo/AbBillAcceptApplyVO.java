package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
/**   
 * @Title: Vo
 * @Description: 提示承兑处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillAcceptApplyVO extends AbAccrecordsVO implements Serializable,IBusinessKey{
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
	/**承兑人*/
	@BtCompositeSearch(fieldName="acceptor")
	private String acceptor;
	/**开出方式：0  对外开出；1 对内自开*/
	// @Dict(dictGroup = "AB_BILL_OPENWAY", inject = "insideSingName")
	@BtCompositeSearch(fieldName="insideSing")
	private Integer insideSing;
	/**业务处理状态*/
	// @Dict(dictGroup = "AB_BILL_ACCEPT_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="status")
	private String statusName;
	/**业务状态*/
	// @Dict(dictGroup = "AB_BILL_INSBRIEFID", inject = "briefIdName")
	private String briefId ;
	@BtCompositeSearch(fieldName="briefId")
	private String briefIdName;
	/**提示承兑操作业务状态*/
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
	/**保证类型:01开票保证，02承兑保证；03背书保证*/
	// @Dict(dictGroup = "AB_BAIL_TYPE", inject = "bailTypeName")
	private String bailType;
	@BtCompositeSearch(fieldName="bailType")
	private String bailTypeName;
	@BtCompositeSearch(fieldName="bankAcc")
	private String bankAcc;
	private String rmks;
	/**保证人地址*/
	private String adr;
	/**备注*/
	private String rmk;
	/**到期无条件支付承诺 承兑回复必填 CC00含委托/承诺兑付*/
	// @Dict(dictGroup="AB_BILL_UCONDLPRMSMRK",inject="ucondlprmsmrkName")
	private String ucondlprmsmrk;
	private String ucondlprmsmrkName;
	/** 出票标志*/
	@BtCompositeSearch(fieldName="newStmIssuofpprInd")
	//@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	private String newStmIssuofpprIndName;
	/** 是否允许分包*/
	//@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	@BtCompositeSearch(fieldName="c.BLGRDBAGCRCL_PRMT_IND")
	private String blgrdbagcrclPrmtInd;
	private String blgrdbagcrclPrmtIndName;
	private String sbbllRngStrtSn;//		子票区间开始序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	private String sbbllRngEndSn;//				子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	private BigDecimal blStdAmt;//			标准金额。单位元.旧票据无，新票据默认为0.01
	private String blRmtrUnnIdNo;//			出票人统一信用代码
	private String blAcptrIdTpcd;//			承兑人识别类型。DT01 票据账户DT02 银行账户
	private String blRcvpymtpsIdTpcd;//			收款人人识别类型。DT01 票据账户DT02
	private String ebillBlCrclIndc;//			票据流通标志（主表）。
	private String cooperativeBankNo; //合作银行号
	private String blBsnTpcd;//新票据业务类型
	private String blAcptrUnnIdNo;//			承兑人统一信用代码
	private String blRcvpymtpsUnnIdNo;//			收款人统一信用代码
	private String blHoldUnnIdNo;//			票据单位统一识别号码
	/**业务批次号 */
	@BtCompositeSearch(fieldName="batchNo")
	private String batchNo;
	
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
	@BtCompositeSearch(fieldName="bankTypeId")
	private String bankTypeId;

	/**创建人登录名称*/
	private String createBy;

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
	
}
