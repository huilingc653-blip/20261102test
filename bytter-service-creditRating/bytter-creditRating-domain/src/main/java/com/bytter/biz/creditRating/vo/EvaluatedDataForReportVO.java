package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Title: VO
 * @Description: 报告中已评数据（原始数据固化）
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Data
public class EvaluatedDataForReportVO implements java.io.Serializable {
    /** 业务id*/
    private String id;

    /** 信用分*/
    private Integer creditScore;

    /** 信用等级*/
    private String creditGrade;

    /** 数据名称*/
    private String dataName;

    /** 数据代码*/
    private String dataCode;

    /** 数据来源业务表主建*/
    private String dataId;

    /** 数据来源业务表主建*/
    private String dataType;

    /** 数据来源类别名称*/
    private String dataSourceCategoryName;

    /** 数据来源类别代码*/
    private String dataSourceCategoryCode;

    /** 评估报告主键*/
    private String evaluationReportId;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 创建人*/
    private String creator;

    /** 创建时间*/
    private Date createDate;

    /** 评价单位*/
    private String corpId;

    /** 评价单位名称*/
    private String corpName;

    /** 指标集合*/
    private List<ScoringMetricValFixReportVO> scoringMetricValFixReportList;
}
