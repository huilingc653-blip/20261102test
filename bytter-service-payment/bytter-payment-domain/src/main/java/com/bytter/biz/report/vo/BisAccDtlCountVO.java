package com.bytter.biz.report.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class BisAccDtlCountVO implements Serializable {
    @BtCompositeSearch(fieldName = "sc2.id")
    private String parentId;
    @BtCompositeSearch(fieldName = "sc2.id")
    private String parentName;
    @BtCompositeSearch(fieldName = "sc.id")
    private String corpId;
    @BtCompositeSearch(fieldName = "sc.name")
    private String corpName;
    @BtCompositeSearch(fieldName = "p.id")
    private String provinceId;
    @BtCompositeSearch(fieldName = "p.province_name")
    private String provinceName;
    @BtCompositeSearch(fieldName = "acc.bank_acc")
    private String bankAcc;
    @BtCompositeSearch(fieldName = "acc.acc_name")
    private String accName;
    @BtCompositeSearch(fieldName = "acc.bank_name")
    private String bankName;
    @BtCompositeSearch(fieldName = "acc.is_online")
    private String isOnline;
    @BtCompositeSearch(fieldName = "acc.is_online")
    private String isOnlineName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Integer number;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getIsOnlineName() {
        return isOnlineName;
    }

    public void setIsOnlineName(String isOnlineName) {
        this.isOnlineName = isOnlineName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
