package com.bytter.biz.creditRating.vo;

import lombok.Data;

/**
 * @Title: VO
 * @Description: 报告中指标类别固化
 * @author chenqin
 * @date 2024-08-12
 * @version 1.0
 *
 */

@Data
public class IndicatorCategoryFixReportVO implements java.io.Serializable {
    /** 报告中计算模型固化主键*/
    private String modelSolidificationId;

    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String rvaluationBatchNumber;

    /** 类别名称*/
    private String categoryName;

    /** 类别代码*/
    private String categoryRncoding;

    /** 类别说明*/
    private String categoryDescription;

}
