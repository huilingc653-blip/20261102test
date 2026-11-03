package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title: VO
 * @Description: 机构得分
 * @author chenqin
 * @date 2024-09-23
 * @version 1.0
 *
 */

@Data
public class OrgScoreVO implements Serializable {
    /**
     * 机构id
     */
    public String orgId;

    /**
     * 指标数据源值
     */
    private BigDecimal dataSourceValue;

    /**
     * 指标数据源值排名
     */
    private BigDecimal dataSourceValueOrder;

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
