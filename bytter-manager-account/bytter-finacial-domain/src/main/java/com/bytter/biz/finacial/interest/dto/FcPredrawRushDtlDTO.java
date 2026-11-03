package com.bytter.biz.finacial.interest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FcPredrawRushDtlDTO {
    private java.lang.String predrawDtlId;
    /**冲预提日期*/
    private java.util.Date strikeDate;
    /**冲预提利息*/
    private java.math.BigDecimal strikeInterest;
    /**操作人*/
    private java.lang.String operatorCode;
    /**状态*/
    private java.lang.Integer status;
    /**冲预提流水号*/
    private java.lang.String orderNo;
    /**取款主键ID*/
    private java.lang.String depositManagerId;
    /**创建时间*/
    private java.util.Date createTime;
    /**创建用户代码*/
    private java.lang.String createBy;
    /**最后更新时间*/
    private java.util.Date updateTime;
    /**最后更新用户代码*/
    private java.lang.String updateBy;
}
