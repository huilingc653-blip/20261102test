package com.bytter.biz.creditRating.api.dto;


import com.bytter.core.base.entity.IdEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 机构和评价任务主表
 */
@Entity
@Data
public class CrTaskOrgInfoDTO extends IdEntity implements Serializable {

    /**
     * 评价任务编号
     */
    private String taskNo;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 评价任务编号
     */
    private String taskName;

    /**
     * 评价机构
     */
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构平均分
     */
    private BigDecimal orgAvg;

    /**
     * 状态
     */
    private int status;

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
     * 评估单位
     */
    private String corpName;

    /**
     * 评价开始日期
     */
    private Date evaluateStartDate;

    /**
     * 评价结束日期
     */
    private Date evaluateEndDate;

    /**
     * 评价等级
     */
    private String grade;

    /**
     * 机构类别
     */
    private String orgType;

    /**
     * 评价周期
     */
    private String taskCycle;

    /**
     * 评价模板名称
     */
    private String templateName;

    /**
     * 评价模板名称
     */
    private String orgEvaluateLevel;

}
