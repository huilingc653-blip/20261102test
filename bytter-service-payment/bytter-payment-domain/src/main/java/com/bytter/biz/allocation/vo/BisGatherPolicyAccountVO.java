package com.bytter.biz.allocation.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 资金中心调拨账户配置
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/11
 */
public class BisGatherPolicyAccountVO implements Serializable {
    private String id;
    @BtCompositeSearch(fieldName = "a.corp_name")
    private String corpName;
    @BtCompositeSearch(fieldName = "a.bank_acc")
    private String bankAcc;
    @BtCompositeSearch(fieldName = "a.bank_name")
    private String bankName;
    @BtCompositeSearch(fieldName = "a.priority")
    private Integer priority;
    @BtCompositeSearch(fieldName = "a.is_down")
    @Dict(dictGroup = "sf_yn",inject = "isDownName")
    private String isDown;
    private String isDownName;
    @BtCompositeSearch(fieldName = "a.status")
    @Dict(dictGroup = "externalCorp.status",inject = "statusName")
    private String status;
    private String statusName;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getIsDown() {
        return isDown;
    }

    public void setIsDown(String isDown) {
        this.isDown = isDown;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getIsDownName() {
        return isDownName;
    }

    public void setIsDownName(String isDownName) {
        this.isDownName = isDownName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
