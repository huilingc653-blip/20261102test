package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Description 票据解付与解付账户前一天余额对比表显示数据
* @author zhous
* @date 2024/10/10 14:30
*/
@SuppressWarnings("serial")
@Data
public class AbBillBankAccBalCompareVo {

    //大区：票据单位对应的二级管理单元
    private String parentId;
    @BtCompositeSearch(fieldName="a.parentName")
    private String parentName;

    //营运区:单位信息表中的营运区
    private String operationAreaCode;
    private String operationAreaName;

    //票据单位：票据单位名称
    @BtCompositeSearch(fieldName="a.corpId")
    private String corpId;
    @BtCompositeSearch(fieldName="a.corpName")
    private String corpName;

    //股权：单位表中的股权属性
    @BtCompositeSearch(fieldName="a.equityCode")
    private String equityCode;
    @BtCompositeSearch(fieldName="a.equityName")
    private String equityName;

    //银行账号：票据银行账号
    @BtCompositeSearch(fieldName="a.bankAcc")
    private String bankAcc;

    //银行类型：银行类型（大类）
    @BtCompositeSearch(fieldName="a.bankName")
    private String bankName;
    @BtCompositeSearch(fieldName="a.bankTypeId")
    private String bankTypeId;

    //贷款金额
    @BtCompositeSearch(fieldName="a.loanMoney")
    private BigDecimal loanMoney;

    //贷款应还本息
    @BtCompositeSearch(fieldName="a.repayTotalMoney")
    private BigDecimal repayTotalMoney;

    //票面金额
    @BtCompositeSearch(fieldName="a.billMoney")
    private BigDecimal billMoney;

    //保证金金额
    @BtCompositeSearch(fieldName="a.bailMoney")
    private BigDecimal bailMoney;

    //到期日
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    private String endDateStr;

    //申请还款金额
    @BtCompositeSearch(fieldName="a.applyRepayMoney")
    private BigDecimal applyRepayMoney;

    //申请人
    @BtCompositeSearch(fieldName="a.applicant")
    private String applicant;

    //保证金比率
    @BtCompositeSearch(fieldName="a.bailPercent")
    private BigDecimal bailPercent;

    //解付账户前一天实时余额
    @BtCompositeSearch(fieldName="a.bal")
    private BigDecimal bal;

    //差异
    @BtCompositeSearch(fieldName="discrepancy")
    private BigDecimal discrepancy;

    //备注
    private String rmk;
}
