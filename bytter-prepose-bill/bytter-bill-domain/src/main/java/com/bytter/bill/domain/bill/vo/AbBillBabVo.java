package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Description 月度还银承表显示数据
* @author zhous
* @date 2024/9/18 17:00
*/
@SuppressWarnings("serial")
@Data
public class AbBillBabVo implements java.io.Serializable{

    //大区：票据单位对应的二级管理单元

    private String parentId;
    @BtCompositeSearch(fieldName="a.parentName")
    private String parentName;
    //大区单位排序
    @BtCompositeSearch(fieldName="a.parentSort")
    private String parentSort;

    //营运区:单位信息表中的营运区
    @BtCompositeSearch(fieldName="a.operationAreaCode")
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

    //银行类型：银行类型（大类）
    @BtCompositeSearch(fieldName="a.typeName")
    private String typeName;

    private String bankName;

    @BtCompositeSearch(fieldName="a.bankTypeId")
    private String bankTypeId;

    //银行账号：票据银行账号
    @BtCompositeSearch(fieldName="a.bankAcc")
    private String bankAcc;

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

    //申请还款金额
    @BtCompositeSearch(fieldName="a.applyRepayMoney")
    private BigDecimal applyRepayMoney;

    //结算金额
    @BtCompositeSearch(fieldName="a.settleMoney")
    private BigDecimal settleMoney;

    //保证金比率
    @BtCompositeSearch(fieldName="a.bailPercent")
    private BigDecimal bailPercent;

    private String provinceName;
}
