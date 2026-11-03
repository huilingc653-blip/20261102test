package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Title: VO
 * @Description: 报告中评分指标定量方法固化
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Data
public class QMScoringIFixReportVO implements java.io.Serializable {
    /** id*/
    private String id;

    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 评分指标名称*/
    private String scoringIndicatorName;

    /** 评分指标代码*/
    private String scoringIndicatorCode;

    /** 评分指标说明*/
    private String scoringIndicatorDescription;

    /** 指标类别名称*/
    private String categoryName;

    /** 指标类别代码*/
    private String categoryEncoding;

    /** 数值*/
    private BigDecimal value;

    /** 数值名称*/
    private String valueName;

    /** 数值名称代码*/
    private String valueNameCode;

    /** 报告中评分指标固化主键*/
    private String sifrId;

    /** 额外数值*/
    private BigDecimal otherValue;

    /** 版本号*/
    private Integer versionNumber;

    /** 数值对应得分细则符号*/
    private Integer valueDirection;

    /** 额外数值对应得分细则符号*/
    private Integer otherValueDirection;

    /** 得分*/
    private BigDecimal grade;

}
