package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 应收票据登记审核详情
 *
 * @author onlineGenerator
 * @date 2026-08-27
 * @version 1.0
 */
@Data
@ApiModel("应收票据登记审核详情")
public class AbReceiveBillApprovePageVO {

    @ApiModelProperty("应收票据信息")
    private AbReceiveBillVO info;

    private boolean isApprove;

    @ApiModelProperty("流程任务ID")
    private String taskId;

    @ApiModelProperty("预算项目")
    private String payItem;

    @ApiModelProperty("业务名称")
    private String businessName;

    @ApiModelProperty("流程系统名")
    private String processSysname;

    @ApiModelProperty("是否显示预算")
    private boolean budgetDisplay;

    private AbBillAttachmentVO attachmentInfo;

    private AbBillAttachmentVO attachmentInfoDiscount;

    private boolean isAttachmentShow;

    private String bankTypeName;

    @ApiModelProperty("是否关联授信")
    private Integer isCredit;

    @ApiModelProperty("是否贴现授信")
    private Integer isDisCountCredit;

    public boolean getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(boolean approve) {
        isApprove = approve;
    }

    public boolean getIsAttachmentShow() {
        return isAttachmentShow;
    }

    public void setIsAttachmentShow(boolean attachmentShow) {
        isAttachmentShow = attachmentShow;
    }
}
