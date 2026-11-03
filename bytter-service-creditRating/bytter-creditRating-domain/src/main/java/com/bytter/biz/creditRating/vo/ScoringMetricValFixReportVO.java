package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Title: VO
 * @Description: 报告中评分指标值固化
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Data
public class ScoringMetricValFixReportVO implements java.io.Serializable {
    /** 业务id*/
    private String id;

    /** 评分指标名称*/
    private String scoringIndicatorName;

    /** 评分指标说明*/
    private String scoringIndicatorDescription;

    /** 评分指标系数*/
    private BigDecimal scoringIndicatorCoefficient;

    /** 评分指标代码*/
    private String scoringIndicatorCode;

    /** 指标类别名称*/
    private String categoryName;

    /** 指标类别代码*/
    private String categoryEncoding;

    /** 指标数值*/
    private BigDecimal indicatorValue;

    /** 外键（报告中已评数据主键）*/
    private String evaluatedDataId;

    /** 指标得分*/
    private BigDecimal score;

    /** 数据id*/
    private String dataId;

    /** 数据名称*/
    private String dataName;

    /** 数据代码*/
    private String dataCode;

    /** 单位id*/
    private String corpId;

    /** 单位名称*/
    private String corpName;


}
