package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Title: VO
 * @Description:
 * @author chenqin
 * @date 2024-09-23
 * @version 1.0
 *
 */

@Data
public class SymbolVO {
    /**
     * 机构id
     */
    public String orgId;

    /**
     * 指标数据源值
     */
    private BigDecimal dataSourceValue;

    /**
     * 指标得分
     */
    private BigDecimal score;

    /**
     * 指标最大值
     */
    private BigDecimal maxValue;

    /**
     * 指标最小值
     */
    private BigDecimal minValue;
}
