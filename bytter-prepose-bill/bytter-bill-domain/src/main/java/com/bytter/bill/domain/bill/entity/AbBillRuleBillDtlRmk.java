package com.bytter.bill.domain.bill.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "AB_BILL_RULEBILLDTLRMK", schema = "")
public class AbBillRuleBillDtlRmk extends IdEntity implements java.io.Serializable {
    /**
     * 银行类型ID
     */
    private String bankTypeId;
    /**
     * 银行类型ID
     */
    private String bankTypeName;
    /**
     * 业务类型 001托收,002贴现,003兑付
     */
    private String bustype;
    /**
     * 状态 0无效,1有效
     */
    private Integer status;
    /**创建时间*/
    private java.util.Date createDate;
    /**创建用户代码*/
    private java.lang.String createBy;
    /**最后更新时间*/
    private java.util.Date updateDate;
    /**最后更新用户代码*/
    private java.lang.String updateBy;
    @Column(name ="BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name ="BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    @Column(name ="BUSTYPE")
    public String getBustype() {
        return bustype;
    }

    public void setBustype(String bustype) {
        this.bustype = bustype;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
