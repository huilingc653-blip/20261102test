package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author chenqin
 * @version 1.0
 * @Title: VO
 * @Description: 评估任务
 * @date 2024-08-27
 */

@Data
public class CrTaskInfoVO implements java.io.Serializable, IBusinessKey {
    /**
     * 评价任务id
     */
    private String id;

    /**
     * 评价任务编号
     */
    @BtCompositeSearch(fieldName = "info.TASK_NO")
    private String taskNo;

    /**
     * 评价组织权重
     */
    private String taskWeight;

    /**
     * 评价任务名称
     */
    @BtCompositeSearch(fieldName = "info.task_name")
    private String taskName;

    /**
     * 评价模板名称
     */
    private String templateName;

    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "info.status")
    private Integer status;

    /**
     * 评价周期
     */
    private String taskCycle;

    /**
     * 计算模型id
     */
    private String computationalModelId;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 评价开始日期
     */
    @BtCompositeSearch(fieldName = "info.evaluate_start_date")
    private Date evaluateStartDate;

    /**
     * 评价结束日期
     */
    @BtCompositeSearch(fieldName = "info.evaluate_end_date")
    private Date evaluateEndDate;

    /**
     * 组织评价次数
     */
    private Integer evaluateCnt;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    @BtCompositeSearch(fieldName = "info.create_date")
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
     * 组织单位
     */
    @BtCompositeSearch(fieldName = "ctci.corp_id")
    private String corpId;

    /**
     * 组织单位
     */
    private String corpName;

    /**
     * 机构代码
     */
    private String orgCode;

    /**
     * 机构名称
     */
    @BtCompositeSearch(fieldName = "toi.org_name")
    private String orgName;

    /**
     * 机构名称
     */
    private String orgIds;

    /**
     * 机构类别
     */
    private String orgType;

    /**
     * 机构类别
     */
    private String orgTypeName;

    /**
     * 评价人
     */
    private String evaluator;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 指标和单位id集合
     */
    private List<CrmScoringIndicatorSolidificatiVO> scoringList;


    @Override
    public String getBusinessKey() {
        return getId();
    }
}
