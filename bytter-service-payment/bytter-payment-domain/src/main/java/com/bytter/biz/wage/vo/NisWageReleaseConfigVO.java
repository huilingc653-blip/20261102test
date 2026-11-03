package com.bytter.biz.wage.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代发工资下拨金额配置VO
 *
 * @author loveCamille
 */
@ApiModel("代发工资下拨金额配置VO")
@Getter
@Setter
public class NisWageReleaseConfigVO extends ExcelModel implements IBusinessKey, Serializable {

    @ApiModelProperty("ID")
    private String id;

    /**
     * 单位id
     */
    @BtCompositeSearch(fieldName = "t1.corp_id")
    @ApiModelProperty("单位id")
    private String corpId;

    /**
     * 单位编码
     */
    @ApiModelProperty("单位编码")
    private String corpCode;

    /**
     * 单位名
     */
    @Excel(name = "*单位名称")
    @ApiModelProperty("单位名")
    private String corpName;

    /**
     * 银行账号
     */
    @Excel(name = "*银行账号")
    @BtCompositeSearch(fieldName = "t1.bank_acc")
    @ApiModelProperty("银行账号")
    private String bankAcc;

    /**
     * 银行账号名称
     */
    @ApiModelProperty("银行账号名称")
    private String bankAccName;

    /**
     * 开户行行号
     */
    @ApiModelProperty("开户行行号")
    private String bankCode;

    /**
     * 开户行名称
     */
    @ApiModelProperty("开户行名称")
    private String bankName;

    /**
     * 下拨日期
     */
    @Excel(name = "*月份")
    @ApiModelProperty("下拨日期")
    private String releaseDate;

    /**
     * 上月金额
     */
    @ApiModelProperty("上月金额")
    private BigDecimal lastMonthReleaseAmount;

    /**
     * 下拨金额
     */
    @Excel(name = "*下拨金额")
    @ApiModelProperty("下拨金额")
    private BigDecimal releaseAmount;

    /**
     * 本月单据累计下拨金额
     */
    @ApiModelProperty("本月单据累计下拨金额")
    private BigDecimal totalReleaseMoney;

    /**
     * 下拨状态
     */
    @BtCompositeSearch(fieldName = "t1.status")
    @ApiModelProperty(value = "下拨状态", example = "0")
    private Integer status;
    @BtCompositeSearch(fieldName = "t1.status")
    private String statusName;

    /**
     * 确认人id
     */
    @ApiModelProperty("确认人id")
    private String confirmId;

    /**
     * 确认人名称
     */
    @ApiModelProperty("确认人名称")
    private String confirmName;

    /**
     * 确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("确认时间")
    private Date confirmTime;

    /**
     * 是否生成需求清单
     */
    @ApiModelProperty(value = "是否生成需求清单", example = "0")
    private Integer generateRequire;

    /**
     * 需求清单生成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("需求清单生成时间")
    private Date generateTime;
    @Override
    public String getBusinessKey() {
        return id;
    }

    @Override
    public String getBeanName() {
        return "nisWageReleaseConfigService";
    }
}
