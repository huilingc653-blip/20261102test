package com.bytter.biz.bdlinks.vo.v1;

import com.bytter.biz.bdlinks.vo.BisExcVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("付款指令数据查询")
public class BisExcPageVO implements Serializable {

    @ApiModelProperty("付款指令页面信息")
    private BisExcVO bisExcPage;

    @ApiModelProperty("业务类型")
    private String busType;

    @ApiModelProperty("备注信息")
    private String rmk;

    @ApiModelProperty("是否展示中间付款信息 （只有在转付且有中间付款时展示的信息）")
    private String showBisExcProc;

    @ApiModelProperty("中间付款信息")
    private BisExcVO bisExcProcPage;

    @ApiModelProperty("审批任务ID")
    private String taskId;
}
