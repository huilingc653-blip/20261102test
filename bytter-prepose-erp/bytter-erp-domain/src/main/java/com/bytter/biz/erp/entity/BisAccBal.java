package com.bytter.biz.erp.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行账号余额表
 *
 * @TableName bis_acc_bal
 */
@Data
public class BisAccBal implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 业务单号
     */
    private String accNo;

    /**
     * 银行账号
     */
    private String bankAcc;

    /**
     * 单位ID
     */
    private String corpId;

    /**
     * 币别ID
     */
    private String curId;

    /**
     * 接口代码
     */
    private String bifCode;

    /**
     * 获取时间
     */
    private Date getDate;

    /**
     * 余额日期
     */
    private Date balDate;

    /**
     * 余额
     */
    private BigDecimal bal;

    /**
     * 可用余额
     */
    private BigDecimal availBal;

    /**
     * 冻结金额
     */
    private BigDecimal frzBal;

    /**
     * 系统冻结金额
     */
    private BigDecimal sysFrzBal;

    /**
     * 返回码
     */
    private String returnCode;

    /**
     * 返回描述信息
     */
    private String returnMsg;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 截止交易日期
     */
    private String isComplete;

    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
}
