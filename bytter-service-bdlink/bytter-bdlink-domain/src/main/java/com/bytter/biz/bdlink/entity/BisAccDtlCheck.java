package com.bytter.biz.bdlink.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 余额流水异常核查表
 * @date 2023-02-15 11:20:30
 */
@Entity
@Table(name = "BIS_ACC_DTL_CHECK", schema = "")
@SuppressWarnings("serial")
public class BisAccDtlCheck extends IdEntity implements java.io.Serializable {

    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 银行账户
     */
    private String bankAcc;
    /**
     * 币别ID
     */
    private String curId;
    /**
     * 异常业务日期
     */
    private Date checkDate;
    /**
     * 余额
     */
    private BigDecimal bal;
    /**
     * 核查余额
     */
    private BigDecimal checkBal;
    /**
     * 状态
     */
    private String status;

    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "CHECK_DATE")
    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    @Column(name = "BAL")
    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    @Column(name = "CHECK_BAL")
    public BigDecimal getCheckBal() {
        return checkBal;
    }

    public void setCheckBal(BigDecimal checkBal) {
        this.checkBal = checkBal;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
