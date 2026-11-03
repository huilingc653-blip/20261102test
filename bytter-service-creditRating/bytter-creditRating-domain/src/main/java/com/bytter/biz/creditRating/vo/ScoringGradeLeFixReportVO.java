package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoringGradeLeFixReportVO implements java.io.Serializable {
    /** id*/
    private String id;
    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 报告中评分等级固化主键*/
    private String scoringGradeFixReportId;

    /** 计算模型固化主键*/
    private String modelSolidificationId;

    /** 评分级别*/
    private String scoringLevel;

    /** 评分级别上限分*/
    private BigDecimal upperScoreLimitLevel;

    /** 评分级别下线分*/
    private BigDecimal lowerScoreLimitLevel;

    /** 评分级别说明*/
    private String explanationOfScoringLevel;

    /**
     * 上限等级符号
     */
    private String upScoringSymbol;
    /**
     * 下限等级符号
     */
    private String downScoringSymbol;
}
