package com.bytter.biz.receipt.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhangxb
 * @date 2017/7/4.
 * @Description 电子回单查询
 */
public class EleBillQueryVO {
    /** 单位*/
    private String corpIds;
    /** 打印状态 Y 已打印 N 未打印*/
    private String printSign;
    /** 回单状态 1有效 */
    private String status;
    /** 电子回单类型*/
    private String billType;
    /** 电子回单类型 显示名称*/
    /**有效回单数*/
    private Long count;
    /** 凭证流水号*/
    private String beginVouNo;
    private String endVouNo;
    /** 金额*/
    private BigDecimal beginMoney;
    private BigDecimal endMoney;
    /** 票据日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billBeginDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billEndDate;
    /** 记账日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String beginDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endDate;
    /** 存款类型*/
    private String depositRateType;
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public String getCorpIds() {
        return corpIds;
    }
    public void setCorpIds(String corpIds) {
        this.corpIds = corpIds;
    }
    public String getPrintSign() {
        return printSign;
    }
    public void setPrintSign(String printSign) {
        this.printSign = printSign;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getBillType() {
        return billType;
    }
    public void setBillType(String billType) {
        this.billType = billType;
    }
    public String getBeginVouNo() {
        return beginVouNo;
    }
    public void setBeginVouNo(String beginVouNo) {
        this.beginVouNo = beginVouNo;
    }
    public String getEndVouNo() {
        return endVouNo;
    }
    public void setEndVouNo(String endVouNo) {
        this.endVouNo = endVouNo;
    }
    public BigDecimal getBeginMoney() {
        return beginMoney;
    }
    public void setBeginMoney(BigDecimal beginMoney) {
        this.beginMoney = beginMoney;
    }
    public BigDecimal getEndMoney() {
        return endMoney;
    }
    public void setEndMoney(BigDecimal endMoney) {
        this.endMoney = endMoney;
    }
    public Date getBillBeginDate() {
        return billBeginDate;
    }
    public void setBillBeginDate(Date billBeginDate) {
        this.billBeginDate = billBeginDate;
    }
    public Date getBillEndDate() {
        return billEndDate;
    }
    public void setBillEndDate(Date billEndDate) {
        this.billEndDate = billEndDate;
    }
    public String getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getDepositRateType() {
        return depositRateType;
    }
    public void setDepositRateType(String depositRateType) {
        this.depositRateType = depositRateType;
    }
}
