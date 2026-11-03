package com.bytter.biz.creditRating.vo;


import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评价任务主表
 * @author huangyl
 */
@Data
public class CrTaskCorpInfoVo implements Serializable, IBusinessKey {

    private String id;

    /**
     * 评价任务编号
     */
    private String taskNo;

    /**
     * 评价任务名称
     */
    @BtCompositeSearch(fieldName = "info.task_name")
    private String taskName;

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
     * 备注
     */
    private String rmk;

    /**
     * 评价周期
     */
    private String taskCycle;

    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "ctci.status")
    private Integer status;

    /**
     * 评价组织
     */
    @BtCompositeSearch(fieldName = "ctci.corp_id")
    private String corpId;

    /**
     * 单位名称
     */
    private String corpName;

    /**
     * 评价人
     */
    private String evaluateBy;

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
     * 单位自动评分指标集合
     */
    private List<CtrScoringCorpContrastVo> scoringIndicatorList;

    /**
     * 操作类型-1-提交 0-暂存
     */
    private String opType;

    /**
     * 指标得分
     */
    private String indicatorScore;
    /**
     * 机构编号
     */
    private String orgCode;
    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构类别
     */
    private String orgType;

    /**
     * 指标机构手动数值
     */
    private String valueScore;

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
