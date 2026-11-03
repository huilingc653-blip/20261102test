package com.bytter.system.vo;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: BT_BANK_ACC
 * @author onlineGenerator
 * @date 2020-10-14 17:50:16
 * @version 1.0
 *
 */
public class BtBankAccQueryVO implements java.io.Serializable {
    /**id*/
    private String id;
    /**操作类型 1-开户 2-冻结*/
    private String opType;
    /**申请类型*/
    private String reqType;
    /**申请人*/
    private String createBy;
    /**申请人名称*/
    private String createName;
    /**申请时间*/
    private Date createDate;
    /**办理人*/
    private String updateBy;
    /**办理人名称*/
    private String updateName;
    /**办理时间*/
    private Date updateDate;
    /**状态*/
    private String status;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOpType() {
        return opType;
    }
    public void setOpType(String opType) {
        this.opType = opType;
    }
    public String getReqType() {
        return reqType;
    }
    public void setReqType(String reqType) {
        this.reqType = reqType;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
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
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
