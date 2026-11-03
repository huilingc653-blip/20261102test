package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 入池申请 详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolIntoApplyFormVO implements Serializable {
    private Object abReceiveBillPage;
    private AbPoolIntoApplyVO poolDTO;
    private Boolean isApprove;
    private String taskId;
    private List<AbAccrecordsVO> accrecordsList;
    private List<AbLog> abLogPage;
    private AbBillAttachmentVO attachmentInfo;
    private AbBillAttachmentVO attachmentInfoDiscount;
    private Boolean isAttachmentShow;
    private String bankTypeName;
}
