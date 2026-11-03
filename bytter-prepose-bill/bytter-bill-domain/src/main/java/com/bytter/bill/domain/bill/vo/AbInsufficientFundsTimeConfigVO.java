package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;

/**
* @Description 票据银行余额不足时间配置VO
* @author zhous
* @date 2025/6/24 11:31
*/
@SuppressWarnings("serial")
public class AbInsufficientFundsTimeConfigVO {

    private String id;

    /**银行类型*/
    @BtCompositeSearch(fieldName = "a.bank_type_id")
    private String bankTypeId;

    /**银行名称*/
    private String bankTypeName;

    /**时间*/
    private String time;

    /**状态 1-正常 0-禁用*/
    private String status;
    private String statusName;

    /**是否删除 1-是*/
    private String isDeleted;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
