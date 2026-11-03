package com.bytter.biz.inneraccount.vo.v1;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel("银行账户开户办理查询数据")
@Data
public class BtBankAccDtlAddConfigVO implements Serializable {

    @ApiModelProperty("月份")
    private Integer month;

    @ApiModelProperty("当前操作人名称")
    private String createBy;

    @ApiModelProperty("当前时间")
    private Date createDate;

    @ApiModelProperty("当前系统时间 格式 yyyy-MM-dd")
    private String sysDate;
}
