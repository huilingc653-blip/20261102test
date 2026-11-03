package com.bytter.biz.wage.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 代发工资浮动比率操作记录表
 *
 * @author fangcf
 * @date 2025-11-20
 */
@ApiModel("代发工资浮动比率操作记录表")
@Data
public class NisWageFloatRatioMessageVO implements java.io.Serializable {
    /**
     * 月份
     */
    @BtCompositeSearch(fieldName = "c.MONTH")
    @ApiModelProperty("月份")
    private String month;
    @BtCompositeSearch(fieldName = "c.MONTH")
    private String monthName;

    /**
     * 录入时间
     */
    @BtCompositeSearch(fieldName = "n.ENROL_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("录入时间")
    private Date enrolDate;

    /**
     * 单据号
     */
    @ApiModelProperty("单据号")
    private String billNo;

    /**
     * 操作人员
     */
    @ApiModelProperty("操作人员")
    private String op;

    /**
     * 操作人员姓名
     */
    @ApiModelProperty("操作人员姓名")
    private String opName;

    /**
     * 结算操作日志类型，字典名：nisMessage.messageType
     */
    @ApiModelProperty(value = "结算操作日志类型，字典名：nisMessage.messageType", example = "0")
    private Integer messageType;
    private String messageTypeName;

    /**
     * 操作记录
     */
    @ApiModelProperty("操作记录")
    private String message;

    /**
     * busType
     */
    @ApiModelProperty("业务类型")
    private String busType;
}
