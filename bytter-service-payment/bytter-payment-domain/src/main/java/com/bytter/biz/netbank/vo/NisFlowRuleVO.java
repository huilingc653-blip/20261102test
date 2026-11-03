package com.bytter.biz.netbank.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author onlineGenerator
 * @Description 资金调拨流水规则维护
 * @date 2024-05-08
 */
@Data
public class NisFlowRuleVO implements Serializable{
	/** 业务id*/
	private String id;
	/** 调拨类型*/
	@BtCompositeSearch(fieldName = "n.transfer_type")
    @Dict(dictGroup = "nisFlowRule.transferType",inject = "transferTypeName")
	private String transferType;
	/** 调拨类型名称*/
	private String transferTypeName;
	/** 调拨方式*/
	@BtCompositeSearch(fieldName = "n.transfer_mode")
    @Dict(dictGroup = "nisFlowRule.transferMode",inject = "transferModeName")
	private String transferMode;
	/** 调拨方式名称*/
	private String transferModeName;
	/** 状态*/

    @BtCompositeSearch(fieldName = "n.status")
    @Dict(dictGroup = "nisFlowRule.status",inject = "statusName")
	private String status;
	/** 状态名称*/
	private String statusName;
	/** 创建人*/
	private String createBy;
	/** 创建时间*/
	private Date createDate;
	/** 更新人*/
	private String updateBy;
	/** 更新时间*/
	private Date updateDate;
	/** 用途*/
	private String purpose;

	private List<NisFlowRulePurposeVO> nisFlowRulePurposes;

}
