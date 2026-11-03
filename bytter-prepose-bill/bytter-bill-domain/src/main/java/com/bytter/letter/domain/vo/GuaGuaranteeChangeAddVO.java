package com.bytter.letter.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GuaGuaranteeChangeAddVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 当前日期 */
    private String inputDate;
    /** 更改申请编号 */
    private String guaraChangeNo;
    /** 是否挂接保证金 */
    private String isHangBail;
}
