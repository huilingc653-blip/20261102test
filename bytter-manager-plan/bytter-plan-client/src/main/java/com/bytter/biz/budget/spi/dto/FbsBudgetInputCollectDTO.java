package com.bytter.biz.budget.spi.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("汇总资金计划上报 数据获取")
public class FbsBudgetInputCollectDTO {

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
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**编制消息*/
    private String msg;

    /**明细ID*/
    private String[] detailId;
    /**科目代码*/
    private String[] itemCode;
    /**科目名称*/
    private String[] itemName;
    /**上报金额*/
    private BigDecimal[] bMoney;
    /**上报金额*/
    private BigDecimal[] btempMoney;

    /**追加ID*/
    private String superaddId;
    /**追加明细ID*/
    private String[] superaddDtlId;
    private String target;
    /**附件ID*/
    private String attachmentId;
    private String isCollect;
    /**币别*/
    private String curId;
    /**汇率*/
    private BigDecimal exchangeRate;
    /**备注*/
    private String[] rmk;
    /**
     * 默认tab页签
     */
    private String layId;

    private List<String> childIds;
}
