package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 评估报告
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Data
public class EvaluationReportVO implements java.io.Serializable {
    /** 评估批次主键*/
    private String id;

    /** 评估批次主键*/
    private String evaluationBatchId;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 报告中计算模型固化主键*/
    private String modelSolidificationId;

    /** 计算模型名称*/
    @BtCompositeSearch(fieldName = "er.model_name")
    private String modelName;

    /** 计算模型代码*/
    private String modelCode;

    /** 计算模型说明*/
    private String modelDescription;

    /** 已评数据主键*/
    private String evaluatedDataId;

    /** 数据名称*/
    @BtCompositeSearch(fieldName = "er.data_name")
    private String dataName;

    /** 数据代码*/
    private String dataCode;

    /** 数据来源类别名称*/
    @BtCompositeSearch(fieldName = "er.data_source_category_name")
    private String dataSourceCategoryName;

    /** 数据来源类别代码*/
    @BtCompositeSearch(fieldName = "er.data_source_category_name")
    private String dataSourceCategoryCode;

    /** 创建人*/
    private String creator;

    /** 创建时间*/
    @BtCompositeSearch(fieldName = "er.create_date")
    private Date createDate;

    /** 信用分*/
    private Integer creditScore;

    /** 信用等级*/
    private String creditGrade;

    /** 成立时间*/
    private Date establishmentDate;

    /** 注册资本*/
    private BigDecimal registeredCapital;

    /** 法定代表人*/
    private String legalRepresentative;

    /** 经营范围*/
    private String businessScope;

}
