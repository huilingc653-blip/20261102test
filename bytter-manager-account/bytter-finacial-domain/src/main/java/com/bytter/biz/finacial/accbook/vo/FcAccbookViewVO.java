package com.bytter.biz.finacial.accbook.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * FC_ACCBOOK新增页面跳转
 */
@Data
public class FcAccbookViewVO implements Serializable {
    private BigDecimal debitTot;
    private String debitTotStr;
    private Integer bookDtlSize;
    private Boolean multiIframes;
    private Boolean opType;
    private String externalSign;

    private Boolean invalidFlag;
    private FcAccbookPageVO voucher;

}
