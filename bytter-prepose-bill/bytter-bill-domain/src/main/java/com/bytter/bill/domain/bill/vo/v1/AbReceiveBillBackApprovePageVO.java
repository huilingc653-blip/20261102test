package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 退票申请审核详情
 *
 * @author onlineGenerator
 * @date 2026-08-27
 * @version 1.0
 */
@Data
@ApiModel("退票申请审核详情")
public class AbReceiveBillBackApprovePageVO {

    @ApiModelProperty("应收票据信息")
    private AbReceiveBillVO info;

    private boolean isApprove;

    @ApiModelProperty("流程任务ID")
    private String taskId;

    public boolean getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(boolean approve) {
        isApprove = approve;
    }
}
