package com.bytter.biz.creditRating.vo;

import com.bytter.core.bpm.IBusinessKey;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 机构评价信息
 */
@Data
public class CrorgEvaluationInfoVo implements java.io.Serializable, IBusinessKey {

    private String id;


    private String taskNo;

    /**
     * 机构平均分
     */
    private BigDecimal orgAvg;

    /**
     * 评价等级
     */
    private String  evaluationLevel;

    /**
     * 评价开始日期
     */
    private Date evaluateStartDate;

    /**
     * 评价结束日期
     */
    private Date evaluateEndDate;

    /**
     * 评价机构
     */
    private String orgId;

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
