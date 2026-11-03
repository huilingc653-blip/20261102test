package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("查询账号历史改动日志的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccLogPageDTO implements Serializable {

    @ApiModelProperty("业务表单据Id")
    private String bizId;
    @ApiModelProperty("内部账号")
    private String account;
    @ApiModelProperty("业务类型")
    private String bizType;
    @ApiModelProperty("要获取的日志类型")
    private String logType;
}
