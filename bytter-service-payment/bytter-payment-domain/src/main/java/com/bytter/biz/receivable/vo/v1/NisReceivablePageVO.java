package com.bytter.biz.receivable.vo.v1;

import com.bytter.biz.receivable.vo.NisReceivableVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("收款办理 详情数据获取")
public class NisReceivablePageVO {

    @ApiModelProperty("收款信息")
    private NisReceivableVO nisReceivablePage;

    @ApiModelProperty("设置系统客商是否可以从其他业务层手动录入0-否,1-是")
    private String externalCorp;

    @ApiModelProperty("所用帐号单位代码")
    private String accCorpId;

    @ApiModelProperty("业务类别")
    private String busType;

    @ApiModelProperty("任务ID")
    private String taskId;
}
