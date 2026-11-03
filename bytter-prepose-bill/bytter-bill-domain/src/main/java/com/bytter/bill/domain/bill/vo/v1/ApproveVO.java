package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import lombok.Data;

import java.io.Serializable;

/**
 * 费用报销审批
 */
@Data
public class ApproveVO implements Serializable {

    private AbBillEndorseVo info;
    private Boolean isApprove;
    private String bankTypeName;
    private String endorseItem;
    private String businessName;
    private String processSysname;
    String taskId;
    AbBillAttachmentVO attachmentInfo;
    AbBillAttachmentVO attachmentInfoDiscount;
    Boolean isAttachmentShow;



}
