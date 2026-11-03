package com.bytter.workflow.domain.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * create by LuoYJ on 2026/3/6.
 */
@Entity
@Table(name = "WORKFLOW_BUS_ERROR_LOG")
public class WorkFlowBusErrorLog extends IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cateKey;
    private String businessKey;
    private String errorMsg;
    private Boolean isPass; // 审批通过/审批打回
    private String createBy;
    private String updateBy;
    private Date createDate;
    private Date updateDate;


    @Column(name = "CATE_KEY")
    public String getCateKey() {
        return cateKey;
    }

    public void setCateKey(String cateKey) {
        this.cateKey = cateKey;
    }

    @Column(name = "BUSINESS_KEY")
    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    @Column(name = "ERROR_MSG")
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Column(name = "IS_PASS")
    public Boolean getIsPass() {
        return isPass;
    }

    public void setIsPass(Boolean isPass) {
        this.isPass = isPass;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
