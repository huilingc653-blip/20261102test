package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Title: VO
 * @Description: 报告中计算模型固化
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Data
public class ScoringIndicatorFixReportVO implements java.io.Serializable {
    /** id*/
    private String id;

    /** 报告中计算模型固化主键*/
    private String modelSolidificationId;

    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String rvaluationBatchNumber;

    /** 评分指标名称*/
    private String scoringIndicatorName;

    /** 评分指标代码*/
    private String scoringIndicatorCode;

    /** 评分指标说明*/
    private String scoringIndicatorDescription;

    /** 评分指标系数*/
    private BigDecimal scoringIndicatorCoefficient;

    /** 指标类别名称*/
    private String categoryName;

    /** 指标类别代码*/
    private String categoryEncoding;

    /** 报告中指标类别固化主键*/
    private String icfrId;

    /**
     *指标方向
     */
    private BigDecimal indicatorDirectionValue;
    /**
     *最大可能值
     */
    private BigDecimal maxPossibleValue;
    /**
     *最小可能值
     */
    private BigDecimal minPossibleValue;

    /**
     * 是否自动评分
     */
    private BigDecimal isAutoScoring;
    /**
     * 评分方法
     */
    private BigDecimal scoringMethod;

    /**
     * 是否额外加/减分
     */
    private BigDecimal isAddOrSubGrade;

    /**
     * 指标性质
     */
    private BigDecimal indexProperty;

    /** 指标层级*/
    private Integer level;

    /** 指标父id*/
    private String parentId;

    /** 指标id*/
    private String scoringIndicatorId;

    /** 子节点*/
    private List<ScoringIndicatorFixReportVO> childrenScoringList;

    /**
     * 此指标所占行数
     */
    private int rowSpan;

    /**
     * 此指标所占列数
     */
    private int colSpan;

    /**
     * 评分指标值固化集合
     */
    private List<ScoringMetricValFixReportVO> scoringMetricValFixReportList;

    /**
     * 评分细则
     */
    private String scoringRubric;

    /**
     * 指标加权平均得分
     */
    private BigDecimal indicatorWeightingAvgScore;

    /**
     * 指标得分合计
     */
    private BigDecimal total;

}
