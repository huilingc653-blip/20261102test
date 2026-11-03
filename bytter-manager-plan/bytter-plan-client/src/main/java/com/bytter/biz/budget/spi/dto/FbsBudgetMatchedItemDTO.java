package com.bytter.biz.budget.spi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class FbsBudgetMatchedItemDTO implements Serializable {

    /**单位ID*/
    private String corpId;
    /**币别*/
    private String curId;
    /**科目代码*/
    private String itemCode;
    /**执行日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date processDate;
    /**业务单据号*/
    private String billNo;
    /**业务单ID*/
    private String billId;
    /**结算类型  0-现款 1-票据*/
    private String settlType;
    /**部门*/
    private String deptName;
    /**用途*/
    private String purpose;
    /**期望支付日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date wishPayday;
    /**付款账号*/
    private String payerAcc;
    /**收款账号名称*/
    private String payeeAccName;
    /**收款账号*/
    private String payeeAcc;
    /**收款开户行名称*/
    private String payeeBank;

}
