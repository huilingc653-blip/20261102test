package com.bytter.biz.finacial.accbook.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * FC_ACCBOOK新增页面跳转
 */
@Data
public class FcAccbookUpdatePageVO implements Serializable {
    private FcAccbookVoucherVO voucher;
    private BigDecimal debitTot;
    private String debitTotStr;
    private Integer bookDtlSize;
    private Boolean multiIframes;
    private Boolean opType;
    private String externalSign;

    private Boolean invalidFlag;
    private FcAccbookPageVO fcAccbookPageVO;

}
