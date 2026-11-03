package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.List;
/**   
 * @Title: Entity
 * @Description: 下拨申请VO
 * @author luop
 * @date 2017-07-10 14:00:00
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillTransferDownApplyVo extends AbAccrecordsVO implements Serializable,IBusinessKey{
	/**上划下拨表id*/
	private String trid;
	/**票据流水id*/
	private String aid;
	/**上划下拨表ids*/
	private String trids;
	/**票据流水ids*/
	private String aids;
	private String businessKey;
	/**业务归属日*/
	private java.util.Date businessDate;
	/**申请单号*/
	@BtCompositeSearch(fieldName="t.allocated_code")
	private String allocatedCode;
	/**申请单位id*/
	@BtCompositeSearch(fieldName="t.record_corp_id")
	private String recordCorpId;
	/**申请单位名称*/
	private String recordCorpCode;
	/**申请单位名称*/
	@BtCompositeSearch(fieldName="t.record_corp_name")
	private String recordCorpName;
	/**收票单位账号/申请单位账号*/
	private String recevieBankacc;
	/**申请金额*/
	@BtCompositeSearch(fieldName="t.allocation_money")
	private java.math.BigDecimal allocationMoney;
	/**申请原因*/
	private String allocationReason;
	/**业务处理状态*/
	// @Dict(dictGroup = "AB_BILL_TRANSFERDOWNAPPLY_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="c.status")
	private String statusName;
	/**录入属日*/
	private java.util.Date recordDate;
	/**票据币别*/
	private String billCurrency;
	/**附件*/
	private String enclosure;
	/**是否隐藏复选框 1为是*/
	private String checkboxHide;
	/**收票单位名称（前后端分离详情/审批返回）*/
	private String netCorpName;
	/**操作日志列表（前后端分离详情/审批返回）*/
	private List<AbLog> abLogList;
	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
			//审批列表查询
			return "0";
		}
		if(AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())){
			return "1";
		}else{
			return "0";
		}
	}
	
	
}
