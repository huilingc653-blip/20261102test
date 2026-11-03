package com.bytter.biz.report.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description: 集团资金划拨年度分析VO
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/7
 */
@Data
public class GroupTransferYearVO implements Serializable {
    // 源数据
    private String busType;
    private String transTime;
    private BigDecimal amt;

    // 返回数据
    // 上划
    private BigDecimal thisUpAmt; // 本年度上划金额
    private BigDecimal beforeUpAmt;// 前年度上划金额
    private BigDecimal upChainRatio;// 环比
    private BigDecimal upRatio;// 同比
    // 下拨
    private BigDecimal thisDownAmt;// 本年度下拨金额
    private BigDecimal beforeDownAmt;// 前年度下拨金额
    private BigDecimal downChainRatio;// 环比
    private BigDecimal downRatio;// 同比
    // 留存
    private BigDecimal thisRemainAmt;// 本年度留存金额
    private BigDecimal beforeRemainAmt;// 前年度留存金额
    private BigDecimal remainChainRatio;// 环比
    private BigDecimal remainRatio;// 同比
    private BigDecimal remainRate;// 留存率
}
