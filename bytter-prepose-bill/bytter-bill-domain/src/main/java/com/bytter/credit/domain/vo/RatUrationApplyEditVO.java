package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.credit.dto.RatUrationApplyListDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 授信占用申请表编辑页面数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RatUrationApplyEditVO extends RatUrationApplyBaseVO implements Serializable {
    private RatBkrationCpAddListDTO ratBkrationPage;
    private RatUrationApplyListDTO applyDTO;
    private String beginDate;
    private BigDecimal exchangeMoneyRate;
    private BigDecimal exchangeBondMoneyRate;
}
