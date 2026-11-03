package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title: Entity
 * @Description: 票据上划VO
 * @author luop
 * @date 2017-07-10 14:00:00
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Data
public class AbBillTransferUpApplyVo extends AbAccrecordsVO implements Serializable,IBusinessKey{
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**票据流水id*/
	private String aid;
	/**票据主表ids*/
	private String vids;
	/**票据从表ids*/
	private String vvids;
	/**票据流水ids*/
	private String aids;
	private String businessKey;
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
	/**出票人*/
	@BtCompositeSearch(fieldName="PAYER")
	private String payer;
	/**承兑人*/
	@BtCompositeSearch(fieldName="ACCEPTOR")
	private String acceptor;
	/**票据号码*/
	@BtCompositeSearch(fieldName="ORIG_NO")
	private String origNo;
	/**票据金额*/
	@BtCompositeSearch(fieldName="BILL_MONEY")
	private java.math.BigDecimal billMoney;
	/**出票日期*/
	@Excel(name = "*出票日期",format = "yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="START_DATE")
	private java.util.Date startDate;
	/**到期日期*/
	@Excel(name = "*到期日期",format = "yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="END_DATE")
	private java.util.Date endDate;
	/**所属库存*/
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
	@BtCompositeSearch(fieldName="enter_stock")
	private String enterStockName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="b.corp_id")
	private String corpId;
	/**单位名称*/
	@BtCompositeSearch(fieldName="b.corp_name")
	private String corpName;
	/**处理标记：10正在处理11处理完成*/
	@BtCompositeSearch(fieldName="box_status")
	private Integer boxStatus;
	/**业务处理状态：-2已删除；-1打回；0 暂存；1导入;11-94审批中；95 已完成*/
	/**持票单位账号*/
	@BtCompositeSearch(fieldName="b.bank_acc")
	private String bankAcc;
	/**业务处理状态*/
	// @Dict(dictGroup = "AB_BILL_TRANSFERUPAPPLY_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="c.status")
	private String statusName;
	/**业务归属日*/
	private java.util.Date businessDate;
	/**上划单位开户银行*/
	private String corpBankName;
	/**中心开户银行*/
	private String netBankName;
	/**中心单位名称*/
	private String netCorpName;
	/**是否隐藏复选框 1为是*/
	private String checkboxHide;
	/**前手*/
	private String cnAccrecords;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="b.APPLY_CORP_ID")
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	private String applyCorpName;
	/**申请单位代码*/
	@BtCompositeSearch(fieldName="b.APPLY_CORP_CODE")
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="b.BUS_CORP_ID")
	private String busCorpId;
	/** 业务单位名称 */
	private String busCorpName;

	// 新一代票据
	/** 子票区间开始序号 */
	@BtCompositeSearch(fieldName="a.SBBLL_RNG_STRT_SN")
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	@BtCompositeSearch(fieldName="a.SBBLL_RNG_END_SN")
	private String sbbllRngEndSn;


	/** 业务人员 */
	@BtCompositeSearch(fieldName="b.BUS_PERSONNEL")
	private String busPersonnel;
	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
			//审批列表查询
			return "0";
		}
		if(AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())
				||AccebillConstant.AB_BILL_BUS_STATUS94.equals(this.getStatus())){
			return "1";
		}else{
			return "0";
		}
	}

	private BigDecimal transMoney;

}
