package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("协定存款审批数据")
@Data
public class FcAgreedDepositApproveVO implements Serializable {

    @ApiModelProperty("协定存款数据")
    private FcAgreedDepositVO fcAgreedDepositPage;

    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("业务类型")
    private String busType;
}
