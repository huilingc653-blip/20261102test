package com.bytter.biz.finacial.accbook.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * FC_ACCBOOK新增页面跳转
 */
@Data
public class FcAccbookAddPageVO implements Serializable {
    private Boolean opType;
    private Boolean multiIframes;
    private String externalSign;
}
