package com.bytter.biz.finacial.accbook.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * FC_ACCBOOK新增页面跳转
 */
@Data
public class FcAccbookPrintPageVO implements Serializable {

    private BigDecimal debitTot;
    private List<String> debitTotStr;

    private Boolean opType;
    private String externalSign;

    private Boolean showFlag;
    private List<FcAccbookPageVO> listVoucher;
}
