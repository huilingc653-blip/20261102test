package com.bytter.biz.bdlinks.vo.v1;

import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.bdlinks.vo.BisGatherCmdVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("收款指令数据查询")
public class BisGatherCmdPageVO implements Serializable {

    @ApiModelProperty("收款指令数据页面信息")
    private BisGatherCmdVO bisGatherCmdPage;

    @ApiModelProperty("业务类型")
    private String busType;

    @ApiModelProperty("备注信息")
    private String rmk;

    @ApiModelProperty("交易状态")
    private String voucherStat;

    @ApiModelProperty("审批任务ID")
    private String taskId;
}
