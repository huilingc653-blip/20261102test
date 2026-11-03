package com.bytter.biz.compareBill.vo.v1;

import com.bytter.biz.compareBill.vo.CorpCompareBillInputVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("成员单位对账详细数据获取")
public class CorpCompareBillPrintPageVO {

    @ApiModelProperty("对账信息数据")
    private CorpCompareBillInputVO entity;

    @ApiModelProperty("余额")
    private Double balance;

    @ApiModelProperty("企业账面余额")
    private Double erpBalance;

    @ApiModelProperty("是否存在账单月份")
    private Boolean entityMonth;

    @ApiModelProperty("附件文件ID")
    private String attachmentId;

    @ApiModelProperty("对账月份")
    private String compareDate;

    @ApiModelProperty("对账人")
    private String username;

    @ApiModelProperty("复核人")
    private String approves;

    @ApiModelProperty("打印日期")
    private Date printDate;

    @ApiModelProperty("打印人")
    private String printer;
}
