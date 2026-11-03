package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 入池撤销 详情/审核详情/手机审核详情页面数据(共用)
 */
@Data
public class AbPoolIntoRepealFormVO implements Serializable {
    private AbReceiveBillVO abReceiveBillPage;
    private AbPoolIntoRepealVO poolDTO;
    private Boolean isApprove;
    private String taskId;
    private List<AbAccrecordsVO> accrecordsList;
    private List<AbLog> abLogPage;
    private AbBillAttachmentVO attachmentInfo;
    private AbBillAttachmentVO attachmentInfoDiscount;
    private Boolean isAttachmentShow;
    private String bankTypeName;
}
