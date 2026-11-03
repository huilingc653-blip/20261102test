package com.bytter.biz.payment.api.dto;
import java.util.Date;
public class BisFailReasonDTO {
    /**失败原因编码*/
    private String failCode;
    /**失败原因名称*/
    private String failName;
    /**关键字*/
    private String keyWords;
    /**关联行为 1-指令重发 2-指令打回 3-指令作废*/
    private String actionCode;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**银企接口*/
    private String bifCode;
    private String bifName;
    public String getFailCode() {
        return failCode;
    }
    public void setFailCode(String failCode) {
        this.failCode = failCode;
    }
    public String getFailName() {
        return failName;
    }
    public void setFailName(String failName) {
        this.failName = failName;
    }
    public String getKeyWords() {
        return keyWords;
    }
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
    public String getActionCode() {
        return actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
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
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getBifName() {
        return bifName;
    }
    public void setBifName(String bifName) {
        this.bifName = bifName;
    }
}
