package com.bytter.bill.api.bill.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行开票授信资料维护表DTO对象
 *
 * @author fangcf
 * @date 2025-06-26
 */
@Data
public class AbBillPlanAmtMaintainDTO implements Serializable {

    /**
     * 主键
     */
    private String id;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 银行类型
     */
    private String bankTypeId;
    /**
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 授信类型：敞口和低风险
     */
    private String creditType;
    /**
     * 授信类型名称
     */
    private String creditTypeName;
    /**
     * 周开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date weekStartDay;
    /**
     * 周结束日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date weekEndDay;
    /**
     * 本周计划开票金额
     */
    private BigDecimal currWeekBillMoney;
    /**
     * 下周计划开票金额
     */
    private BigDecimal nextWeekBillMoney;
    /**
     * 无
     */
    private String isDeleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
}
