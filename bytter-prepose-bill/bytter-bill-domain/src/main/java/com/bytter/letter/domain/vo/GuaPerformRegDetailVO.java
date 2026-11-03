package com.bytter.letter.domain.vo;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.velocity.tags.FcBondTag;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GuaPerformRegDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 保函基本信息 */
    private GuaGuaranteeInfoVO vo;
    /** 是否挂接保证金 */
    private String isHangBail;
    /** 授信明细 */
    private RatUrationDTO urationDTO;
    /** 保函币别汇率 */
    private BigDecimal curExrate;
    /** 本位币标识 */
    private String naturalSign;
    /** 保证金标签 */
    private FcBondTag fcBondTag;
}
