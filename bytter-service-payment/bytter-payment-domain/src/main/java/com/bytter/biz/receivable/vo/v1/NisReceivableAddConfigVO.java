package com.bytter.biz.receivable.vo.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("收款办理新增 前配置获取")
public class NisReceivableAddConfigVO {
    @ApiModelProperty("设置系统客商是否可以从其他业务层手动录入0-否,1-是")
    private String externalCorp;
}
