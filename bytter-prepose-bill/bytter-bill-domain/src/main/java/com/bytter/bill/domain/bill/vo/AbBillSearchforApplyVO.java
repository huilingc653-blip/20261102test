package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
/**   
 * @Title: Vo
 * @Description: 追索通知处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillSearchforApplyVO extends AbAccrecordsVO implements Serializable,IBusinessKey{
	private String id;
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**签收表id*/
	private String recid;
	/**票据号码*/
	@BtCompositeSearch(fieldName="origNo")
	@Excel(name = "*票据号码",isImportField="true")
	@NotNull(message = AccebillConstant.LANG_ERROR_ORIGNO_ISNOTNULL)
	private String origNo;
	/**票据类型：10 银票、11 商票*/
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	/**票据类型名称：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="billType")
	private String billTypeName;
	@Excel(name = "*票据单位代码",isImportField="true")
	@NotNull(message = AccebillConstant.LANG_ERROR_CORPCODE_ISNOTNULL)
	private String corpCode;
	/**持票单位*/
	@BtCompositeSearch(fieldName="corpId")
	private String corpId;
	@Excel(name = "票据单位名称",isImportField="true")
	@BtCompositeSearch(fieldName="corpName")
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
	@Excel(name = "票据金额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
	@BtCompositeSearch(fieldName="billMoney")
	private java.math.BigDecimal billMoney;
	/**追索金额*/
	@BtCompositeSearch(fieldName="rcrsokmoney")
	private java.math.BigDecimal rcrsokmoney;
	/**追索金额*/
	@Excel(name = "追索金额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
	private java.math.BigDecimal searchForMoney;
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
	// @Dict(dictGroup = "AB_BILL_SEARCHFOR_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="status")
	private String statusName;
	/**业务状态*/
	// @Dict(dictGroup = "AB_BILL_INSBRIEFID", inject = "briefIdName")
	private String briefId ;
	@BtCompositeSearch(fieldName="briefId")
	private String briefIdName;
	/**追索通知操作业务状态*/
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
	@Excel(name = "*被追索人名称",isImportField="true")
	@NotNull(message = AccebillConstant.LANG_ERROR_BSEARCHFORPERSONNM_ISNOTNULL)
	@BtCompositeSearch(fieldName="recNm")
	private String recNm;
	/**业务接收人账号*/
	@Excel(name = "*被追索人账户",isImportField="true")
	@NotNull(message = AccebillConstant.LANG_ERROR_BSEARCHFORPERSONACC_ISNOTNULL)
	@BtCompositeSearch(fieldName="recAcctId")
	private String recAcctId;
	/**接收人组织结构代码*/
	@Excel(name = "*被追索人机构号",isImportField="true")
	@NotNull(message = AccebillConstant.LANG_ERROR_BSEARCHFORPERSONCMONID_ISNOTNULL)
	private String recCmonid;
	/**业务接收人开户行号*/
	@Excel(name = "*被追索人银行行号",isImportField="true")
	private String recAcctsvcr;
	/**业务接收人开户行名称*/
	@Excel(name = "被追索人银行行名",isImportField="true")
	private String recAcctsvcrName;
	private String insideSingName;
	/**保证类型:01开票保证，02承兑保证；03背书保证*/
	// @Dict(dictGroup = "AB_BAIL_TYPE", inject = "bailTypeName")
	private String bailType;
	@BtCompositeSearch(fieldName="bailType")
	private String bailTypeName;
	@BtCompositeSearch(fieldName="bankAcc")
	private String bankAcc;
	private String bailTypes;
	/**保证人地址*/
	private String adr;
	/**备注*/
	@Excel(name = "备注",isImportField="true")
	private String rmk;
	/**追索类型*/
	// @Dict(dictGroup = "AB_RCRS_TP", inject = "rcrsTpName")
	private String rcrsTp;
//	RC00：承兑人被依法宣告破产
//	RC01：承兑人因违法被责令终止活动
	/**追索理由代码*/
	// @Dict(dictGroup = "AB_RCRSRSNCD", inject = "rcrsrsncdName")
	private String rcrsrsncd;
	@Excel(name = "追索理由",isImportField="true")
	@BtCompositeSearch(fieldName="rcrsrsncd")
	private String rcrsrsncdName;
	private String searchForMoneys;
	private String rcrsTps;
	@BtCompositeSearch(fieldName="rcrsTp")
	@Excel(name = "追索类型",isImportField="true")
	private String rcrsTpName;
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
	@BtCompositeSearch(fieldName="bail.newStmIssuofpprInd")
	private String newStmIssuofpprInd;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="bail.blgrdbagcrclPrmtInd")
	private String blgrdbagcrclPrmtInd;
	/** 子票区间开始序号 */
	@Excel(name = "*子票区间开始序号",isImportField="true")
	@BtCompositeSearch(fieldName="bail.sbbllRngStrtSn")
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	@Excel(name = "*子票区间结束序号",isImportField="true")
	@BtCompositeSearch(fieldName="bail.sbbllRngEndSn")
	private String sbbllRngEndSn;

	@BtCompositeSearch(fieldName="bail.bankTypeId")
	private String bankTypeId;

	public String getCheckboxHide() {
		if(StringUtils.isEmpty(checkboxHide)&&AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())){
			return "1";
		}else{
			return "0";
		}
	}
	@Override
	public String getBusinessKey() {
		return id;
	}
	public String getBeanName() {
		return "abBillSearchforApplyService";
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
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEARCHFOR_STATUS");
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
		// 保证类型
		Optional.ofNullable(this.getBailType()).ifPresent(bailType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BAIL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(bailType)))
					.ifPresent(dict -> this.setBailTypeName(dict.getName()));
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
