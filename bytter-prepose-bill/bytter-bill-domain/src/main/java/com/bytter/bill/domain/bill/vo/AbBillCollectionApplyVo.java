package com.bytter.bill.domain.bill.vo;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.enums.CommonDictEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * @Title: Entity
 * @Description: 托收申请VO
 * @author luop
 * @date 2017-07-10 14:00:00
 * @version 1.0
 *
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillCollectionApplyVo extends AbAccrecordsVO implements Serializable,IBusinessKey{
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
	/**持票单位*/
	@BtCompositeSearch(fieldName="b.corp_id")
	private String corpId;
	/**单位名称*/
	@BtCompositeSearch(fieldName="b.corp_name")
	private String corpName;
	/**票据号码*/
	@BtCompositeSearch(fieldName="ORIG_NO")
	private String origNo;
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
	/**票据金额*/
	@BtCompositeSearch(fieldName="BILL_MONEY")
	private java.math.BigDecimal billMoney;
	/**出票日期*/
	@BtCompositeSearch(fieldName="START_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date startDate;
	/**到期日期*/
	@BtCompositeSearch(fieldName="END_DATE")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date endDate;
	/**所属库存*/
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
	@BtCompositeSearch(fieldName="enter_stock")
	private String enterStockName;
	/**处理标记：10正在处理11处理完成*/
	@BtCompositeSearch(fieldName="box_status")
	private Integer boxStatus;
	/**业务处理状态：-2已删除；-1打回；0 暂存；1导入;11-94审批中；95 已完成*/
	/**持票单位账号*/
	@BtCompositeSearch(fieldName="b.bank_acc")
	private String bankAcc;
	/**业务处理状态*/
	// @Dict(dictGroup = "ABBILL_COLLECTIONAPPLY_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="c.status")
	private String statusName;
	/**业务归属日*/
	private java.util.Date businessDate;
	/**是否逾期*/
	// @Dict(dictGroup = "sf_yn", inject = "isOverdueName")
	private String isOverdue;
	private String isOverdueName;
	/**托收说明/备注*/
	private String rmk;
	/**逾期托收说明/备注*/
	private String isOverdueRmk;
	/**清算模式:SM00：线上清算，SM01：线下清算*/
	// @Dict(dictGroup = "AB_STTLMMK", inject = "sttlmmkName")
	private String sttlmmk;
	private String sttlmmkName;
	/**定时器标志: 默认为false,为true时表示定时任务*/
	private boolean isTask;
	/**是否隐藏复选框 1为是*/
	private String checkboxHide;
	/**是否发送指令标志：0否； 1是 ；*/
	@BtCompositeSearch(fieldName="c.send_Sign")
	// @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
	private String sendSign;
	@BtCompositeSearch(fieldName="c.send_Sign")
	private String sendSignName;
	/** 逾期说明 */
	private String rsn;
	/** 资金计划科目 */
	private String collectionItem;
	private String itemCodeStr;
	/**资金计划信息*/
	private List<FbsBudgetProcessDTO> budgetList;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="b.APPLY_CORP_ID")
	// @Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	@Excel(name = "申请单位",isImportField="true")
	@BtCompositeSearch(fieldName="b.APPLY_CORP_NAME")
	private String applyCorpName;
	/**申请单位代码*/
	@Excel(name = "申请单位代码",isImportField="true")
	private String applyCorpCode;
	/** 业务单位 */
	// @Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="b.BUS_CORP_ID")
	private String busCorpId;
	/** 业务单位名称 */
	@Excel(name = "业务单位",isImportField="true")
	private String busCorpName;
	/** 业务人员 */
	@BtCompositeSearch(fieldName="b.BUS_PERSONNEL")
	@Excel(name = "业务人员",isImportField="true")
	private String busPersonnel;
	/** 出票标志 */
	@BtCompositeSearch(fieldName="a.NEW_STM_ISSUOFPPR_IND")
	//@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	@BtCompositeSearch(fieldName="a.NEW_STM_ISSUOFPPR_IND")
	@Excel(name = "出票标志",isImportField="true")
	private String newStmIssuofpprIndName;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="a.BLGRDBAGCRCL_PRMT_IND")
	//@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	@BtCompositeSearch(fieldName="a.BLGRDBAGCRCL_PRMT_IND")
	@Excel(name = "是否允许分包",isImportField="true")
	private String blgrdbagcrclPrmtIndName;
	/** 子票区间开始序号 */
	@BtCompositeSearch(fieldName="a.SBBLL_RNG_STRT_SN")
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	@BtCompositeSearch(fieldName="a.SBBLL_RNG_END_SN")
	private String sbbllRngEndSn;
	/** 标准金额*/
	@BtCompositeSearch(fieldName="a.BL_STD_AMT")
	private BigDecimal blStdAmt;
	/** BL_RMTR_UNN_ID_NO IS '出票人统一信用代码 */
	@BtCompositeSearch(fieldName = "a.BL_RMTR_UNN_ID_NO")
	private String blRmtrUnnIdNo;
	/** 交易金额字*/
	@BtCompositeSearch(fieldName = "a.BILL_MONEY")
	private BigDecimal transMoney;
	/*** 页面修改后的交易金额字符串*/
	private String transMoneys;
	@BtCompositeSearch(fieldName="b.bank_type_id")
	private String bankTypeId;
	public boolean getTask() {
		if(StringUtils.isNotEmpty (isTask)){
			return isTask;
		}
		return false;
	}
	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
			//审批列表查询
			return "0";
		}
		if(AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())||AccebillConstant.AB_BILL_BUS_STATUS94.equals(this.getStatus())){
			return "1";
		}else{
			return "0";
		}
	}
	public String getIsOverdueName() {
		if (ObjectUtil.isNotEmpty(isOverdue)) {
			return CommonDictEnum.getDictValue("sf_yn", isOverdue);
		}
		return isOverdueName;
	}
}
