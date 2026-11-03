package com.bytter.biz.compareBill.vo.v1;

import com.bytter.biz.compareBill.vo.CorpCompareBillInputVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("成员单位对账详细数据获取")
public class CorpCompareBillPageVO {

    @ApiModelProperty("对账信息数据")
    private CorpCompareBillInputVO entity;

    @ApiModelProperty("余额")
    private Double balance;

    @ApiModelProperty("企业账面余额")
    private Double erpBalance;

    @ApiModelProperty("是否存在账单月份")
    private Boolean entityMonth;

    @ApiModelProperty("对账月份")
    private String compareDate;

    @ApiModelProperty("是否审批页")
    private String approve;

    @ApiModelProperty("审批任务ID")
    private String taskId;
}
