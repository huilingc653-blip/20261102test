package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.credit.domain.vm.RatBkrationCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("查询应收票据详细信息")
public class AbReceiveBillViewPageVO {

    private String bankTypeName;

    private AbVoucherinfoVO info;

    private boolean isShowPool;

    private boolean isShow;

    private String businessName;

    private String processSysname;

    private String billId;

    private boolean isEnabled;

    private boolean isNotDiscountApply;

    private String payItem;

    private boolean budgetDisplay;

    private boolean isSplitBill;

    private List<AbLog> ablogList;

    private boolean isApprove;

    private AbBillAttachmentVO attachmentInfo;

    private AbBillAttachmentVO attachmentInfoDiscount;

    private boolean isAttachmentShow;

    @ApiModelProperty("银行授信相关信息")
    private RatBkrationCommonDTO ratBkrationPage;


    public boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(boolean show) {
        isShow = show;
    }

    public boolean getIsAttachmentShow() {
        return isAttachmentShow;
    }

    public void setIsAttachmentShow(boolean attachmentShow) {
        isAttachmentShow = attachmentShow;
    }

    public boolean getIsShowPool() {
        return isShowPool;
    }

    public void setIsShowPool(boolean showPool) {
        isShowPool = showPool;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean getIsNotDiscountApply() {
        return isNotDiscountApply;
    }

    public void setIsNotDiscountApply(boolean notDiscountApply) {
        isNotDiscountApply = notDiscountApply;
    }

    public boolean getIsSplitBill() {
        return isSplitBill;
    }

    public void setIsSplitBill(boolean splitBill) {
        isSplitBill = splitBill;
    }

    public boolean getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(boolean approve) {
        isApprove = approve;
    }
}
