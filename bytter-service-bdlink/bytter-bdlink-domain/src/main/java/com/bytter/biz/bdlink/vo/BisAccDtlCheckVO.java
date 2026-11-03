package com.bytter.biz.bdlink.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangx
 * @version 1.0
 * @Title: VO
 * @Description: 余额流水异常核查表
 * @date 2023-02-15 11:20:30
 */
public class BisAccDtlCheckVO implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 单位ID
     */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "t.corp_Id")
    private String corpId;
    /**
     * 单位名称
     */
    @BtCompositeSearch(fieldName = "t.corp_Id")
    private String corpName;
    /**
     * 银行账户
     */
    @BtCompositeSearch(fieldName = "t.bank_Acc")
    private String bankAcc;
    /**
     * 收款账户名称
     */
    @BtCompositeSearch(fieldName = "acc.acc_Name")
    private String accName;
    /**
     * 账户开户行
     */
    private String bankName;
    /**
     * 币别ID
     */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "t.cur_Id")
    private String curId;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 异常业务日期
     */
    @BtCompositeSearch(fieldName = "t.check_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;
    /**
     * 余额
     */
    @BtCompositeSearch(fieldName = "t.bal")
    private BigDecimal bal;
    /**
     * 核查余额
     */
    @BtCompositeSearch(fieldName = "t.check_Bal")
    private BigDecimal checkBal;
    /**
     * 差额
     */
    private BigDecimal diffBal;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "t.status")
    @Dict(dictGroup = "bisAccDtlCheck.status", inject = "statusName")
    private String status;
    private String statusName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName = "t.create_date")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    public BigDecimal getCheckBal() {
        return checkBal;
    }

    public void setCheckBal(BigDecimal checkBal) {
        this.checkBal = checkBal;
    }

    public BigDecimal getDiffBal() {
        return diffBal;
    }

    public void setDiffBal(BigDecimal diffBal) {
        this.diffBal = diffBal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
