package com.bytter.biz.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("预算保存")
public class FbsBudgetSaveDTO {

    private String id;
    /**单位ID*/
    private String corpId;
    /**编制时间*/
    private Date enrolDate;
    /**计划开始时间*/
    private Date beginDate;
    /**计划结束时间*/
    private Date endDate;
    /**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
    private String timeType;
    /**需审批金额*/
    private BigDecimal checkMoney;
    /**状态*/
    private Integer status;
    /**编制消息*/
    private String msg;

    private Boolean editFlag;

    private List<FbsBudgetSaveDetailDTO> dtlList;
    /**追加ID*/
    private String superaddId;

    private String target;
    /**附件ID*/
    private String attachmentId;
    private String isCollect;
    /**币别*/
    private String curId;
    /**汇率*/
    private BigDecimal exchangeRate;

    /**
     * 默认tab页签
     */
    private String layId;
}
