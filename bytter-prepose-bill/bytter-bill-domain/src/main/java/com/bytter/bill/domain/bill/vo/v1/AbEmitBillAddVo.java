package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.credit.dto.RatUrationDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AbEmitBillAddVo implements Serializable {

    private String appNo;
    private String batchNo;
    private String isBatchBill;
    private String payItem;
    // 挂授信
    private Boolean isRelateRat;
    // 挂内部授信
    private Boolean isOpenCpRat;
    private String curId;
    private Integer externalCorpNum;
    private Boolean isEmitMake;
    private Boolean isBail;
    private String bailBankControlType;
    private Boolean mouthRat;
    private Integer isGuarantee;
    private String isHangGuarantee;
    private BigDecimal maxBillMoney;
    private String externalCorp;
    private AbVoucherinfoVO info;
    private AbEmitBillVo abEmitBillVo;
    private BigDecimal fcBondRate;
    private Boolean isShowPool;
    private String businessName;
    private String processSysname;
    private List<AbLog> abLogList;
    private String billId;
    private RatUrationDTO ratUrationDTO;
    private Boolean isEnabled;
    private String bailmodel;

    AbBillAttachmentVO attachmentInfo;
    AbBillAttachmentVO attachmentInfoDiscount;
    Boolean isAttachmentShow;
    private String bankTypeName;

    private String   budgetDisplay;
    private Boolean  isApprove;
    private Boolean  cpIsEnabled;
    private String   taskId;
}
