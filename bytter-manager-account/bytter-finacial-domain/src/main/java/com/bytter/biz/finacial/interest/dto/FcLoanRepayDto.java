package com.bytter.biz.finacial.interest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FcLoanRepayDto implements java.io.Serializable {
    private String id;
    /**所属网点*/
    private String netId;
    /**单位代码*/
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**币别代码*/
    private String curCode;
    /**贷款帐号*/
    private String loanAcc;
    /**归还账号*/
    private String detailAcc;
    /**业务类型*/
    private String busType;
    /**借方利息账号*/
    private String needInterestAcc;
    /**还款账号*/
    private String repayAcc;
    /**归还类型*/
    private String repayType;
    /**还款金额*/
    private BigDecimal repayMoney;
    /**利息*/
    private BigDecimal interest;
    /**录入日期*/
    private Date enterDate;
    /**记账日期*/
    private Date tallyDate;
    /**状态*/
    private Integer status;
    /**操作人*/
    private String operatorCode;
    /**复核人*/
    private String checkerCode;
    /**复核日期*/
    private Date checkTime;
    /**打回原因*/
    private String refuseWhy;
    /**归还本金记账流水号*/
    private String orderNo;
    /**利息记账流水号*/
    private String interestOrderNo;
    /**创建时间*/
    private Date createTime;
    /**创建用户代码*/
    private String createBy;
    /**最后更新时间*/
    private Date updateTime;
    /**最后更新用户代码*/
    private String updateBy;
}
