package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * @Title: Entity
 * @Description: 票据下拨
 * @author luop
 * @date 2017-07-19 09:43:53
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillTransferDownManageVo extends AbAccrecordsVO implements Serializable,IBusinessKey{	
	/**上划下拨表id*/
	private String trid;
	/**单据流水id*/
	private String aid;
	/**上划下拨表ids*/
	private String trids;
	/**票据流水ids*/
	private String aids;
	/**选票票据流水ids*/
	private String billAid;
	/**上一条流水id*/
	private String lastid;
	private String businessKey;
	/**业务归属日*/
	@BtCompositeSearch(fieldName="t.businessDate")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date businessDate;
	/**下拨单号*/
	@BtCompositeSearch(fieldName="t.allocatedCode")
	private String allocatedCode;
	/**申请单号*/
	@BtCompositeSearch(fieldName="t.applyCode")
	private String applyCode;
	/**下拨单位id*/
	@BtCompositeSearch(fieldName="t.recordCorpId")
	private String recordCorpId;
	/**下拨单位名称*/
	@BtCompositeSearch(fieldName="t.recordCorpName")
	private String recordCorpName;
	private String recordCorpCode;
	/**收票单位id*/
	@BtCompositeSearch(fieldName="t.receviecorpid")
	private String recevieCorpId;
	/**收票单位名称*/
	@BtCompositeSearch(fieldName="t.recevieCorpName")
	private String recevieCorpName;
	private String recevieCorpCode;
	/**收票单位账号*/
	@BtCompositeSearch(fieldName="t.recevieBankacc")
	private String recevieBankacc;
	/**收票单位账号户名*/
	private String recevieBankaccName;
	/**下拨中心账号*/
	@BtCompositeSearch(fieldName="t.recordBankacc")
	private String recordBankacc;
	/**下拨金额*/
	@BtCompositeSearch(fieldName="t.allocationMoney")
	private java.math.BigDecimal allocationMoney;
	/**申请金额*/
	@BtCompositeSearch(fieldName="t.applyMoney")
	private java.math.BigDecimal applyMoney;
	/**下拨原因*/
	@BtCompositeSearch(fieldName="t.allocationReason")
	private String allocationReason;
	/**申请原因*/
	@BtCompositeSearch(fieldName="t.applyReason")
	private String applyReason;
	/**业务处理状态*/
	// @Dict(dictGroup = "AB_BILL_TRANSFERDOWNAPPLY_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="t.status")
	private String statusName;
	/**选票号码*/
	@BtCompositeSearch(fieldName="t.billNo")
	private String billNo;
	/**录入日期*/
	@BtCompositeSearch(fieldName="t.recordDate")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date recordDate;
	/**票据币别*/
	private String billCurrency;
	@BtCompositeSearch(fieldName="t.billCurrencyName")
	private String billCurrencyName;
	/**附件*/
	private String enclosure;
	/**中心单位id*/
	private String netId;
	/**票据业务类型*/
	private String briefId;
	// @Dict(dictGroup = "AB_BILL_BUSTYPE_NAME", inject = "busTypeName")
	private String busType;
	@BtCompositeSearch(fieldName="t.busType")
	private String busTypeName;
	/**是否隐藏复选框 1为是*/
	private String checkboxHide;
	/**下拨申请信息（前后端分离详情/审批返回）*/
	private AbBillTransfer applyInfo;
	/**申请日期（前后端分离详情/审批返回）*/
	private Date applyDate;
	/**选票票据信息列表（前后端分离详情/审批返回）*/
	private List<AbEmitBillVo> abEmitBillVoList;
	/**操作日志列表（前后端分离详情/审批返回）*/
	private List<AbLog> abLogList;
	/**附件信息（前后端分离详情/审批返回）*/
	private AbBillAttachmentVO attachmentInfo;
	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
			//审批列表查询
			return "0";
		}
		if(AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())||StringUtils.isEmpty(this.getAllocatedCode())){
			return "1";
		}else{
			return "0";
		}
	}
}
