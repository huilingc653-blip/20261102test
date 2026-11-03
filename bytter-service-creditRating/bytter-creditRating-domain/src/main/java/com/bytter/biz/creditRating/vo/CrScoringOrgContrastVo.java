package com.bytter.biz.creditRating.vo;


import com.bytter.core.base.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 评分机构评分指标对照表
 */
@Data
public class CrScoringOrgContrastVo extends IdEntity implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 计算模型id
     */
    private String taskNo;

    /**
     * 计算模型id
     */
    private String computationalModelId;

    /**
     * 批次号
     */
    private String batchNumber;

    /**
     * 组织id
     */
    private String orgId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 评分指标固化id
     */
    private int scoringIndicatorSolidificati;

    /**
     * 父指标固化id
     */
    private String parentId;

    /**
     * 评分指标名称
     */
    private String scoringIndicatorName;

    /**
     * 评分指标代码
     */
    private String scoringIndicatorCode;

    /**
     * 评分指标说明
     */
    private String scoringIndicatorDescription;

    /**
     * 评分指标系数
     */
    private BigDecimal scoringIndicatorCoefficient;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 指标加权平均得分
     */
    private BigDecimal scoringOrgAvg;
}
