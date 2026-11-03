package com.bytter.biz.report.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Description 每周资金到期用款规划表vo
* @author zhous
* @date 2024/10/24 9:32
*/
@SuppressWarnings("serial")
@Data
public class WeeklyFundsDueScheduleVo {

    //大区：票据单位对应的二级管理单元
    private String parentId;
    @BtCompositeSearch(fieldName="a.parentName")
    private String parentName;
    @BtCompositeSearch(fieldName="a.parentSort")
    private String parentSort;

    private String provinceName;

    //营运区:单位信息表中的营运区
    @BtCompositeSearch(fieldName="a.operationAreaCode")
    private String operationAreaCode;
    private String operationAreaName;

    //单位名称
    @BtCompositeSearch(fieldName="a.corpId")
    private String corpId;
    /**
     * 单位编码
     */
    private String corpCode;
    @BtCompositeSearch(fieldName="a.corpName")
    private String corpName;

    //公司类型：单位表中的股权属性
    @BtCompositeSearch(fieldName="a.equityCode")
    private String equityCode;
    @BtCompositeSearch(fieldName="a.equityName")
    private String equityName;

    //银行类型：银行类型（大类）
    @BtCompositeSearch(fieldName="a.typeName")
    private String typeName;
    @BtCompositeSearch(fieldName="a.bankTypeId")
    private String bankTypeId;

    //银行账号：票据银行账号
    @BtCompositeSearch(fieldName="a.bankAcc")
    private String bankAcc;

    //贷款金额
    @BtCompositeSearch(fieldName="a.loanMoney")
    private BigDecimal loanMoney;

    //票面金额
    @BtCompositeSearch(fieldName="a.billMoney")
    private BigDecimal billMoney;

    //保证金金额
    @BtCompositeSearch(fieldName="a.bailMoney")
    private BigDecimal bailMoney;

    //到期日
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName="a.endDate")
    private Date endDate;

    //资金下拨日期
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date fundsDisbursementDate;

    //申请还款金额
    @BtCompositeSearch(fieldName="a.applyRepayMoney")
    private BigDecimal applyRepayMoney;

    //保证金比率
    @BtCompositeSearch(fieldName="a.bailPercent")
    private BigDecimal bailPercent;

    //申请人
    @BtCompositeSearch(fieldName="a.applicant")
    private String applicant;

    //申请人
    @BtCompositeSearch(fieldName="a.rmk")
    private String rmk;
}