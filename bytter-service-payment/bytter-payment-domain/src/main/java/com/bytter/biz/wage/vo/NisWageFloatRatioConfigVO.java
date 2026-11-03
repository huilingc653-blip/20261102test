package com.bytter.biz.wage.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代发工资浮动比率配置表
 *
 * @author fangcf
 * @date 2025-11-20
 */
@ApiModel("NisWageFloatRatioConfigVO")
@Data
public class NisWageFloatRatioConfigVO implements java.io.Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 月份
     */
    @BtCompositeSearch(fieldName = "c.MONTH")
    @ApiModelProperty("月份")
    private String month;
    @BtCompositeSearch(fieldName = "c.MONTH")
    private String monthName;

    /**
     * 浮动比率
     */
    @ApiModelProperty("浮动比率")
    private BigDecimal floatRate;

    /**
     * 无
     */
    @ApiModelProperty("是否删除")
    private String isDeleted;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateBy;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;
}
