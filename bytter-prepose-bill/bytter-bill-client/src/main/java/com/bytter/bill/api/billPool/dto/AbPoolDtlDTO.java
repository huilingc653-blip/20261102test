package com.bytter.bill.api.billPool.dto;
import java.util.Date;
public class AbPoolDtlDTO implements java.io.Serializable {
    private String id;
    /**
     * 上一个池流水ID
     */
    private String lastid;
    /**
     * 从表ID
     */
    private String vvId;
    /**
     * 票据池ID
     */
    private String bpId;
    /**
     * 业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款
     */
    private String briefId;
    /**
     * 申请单号
     */
    private String appNo;
    /**
     * 票据号码
     */
    private String origNo;
    /**
     * 出入池方式：1质押2托管
     */
    private String inoutType;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 指令状态：交易失败-1，交易处理中18，交易成功19，交易打回-2
     */
    private String optStatus;
    /**
     * 状态：1启用、0在途、2作废
     */
    private String validSign;
    /**
     * 创建人名称
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新人名称
     */
    private String updateId;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 入池撤销id
     */
    private String repealId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLastid() {
        return lastid;
    }
    public void setLastid(String lastid) {
        this.lastid = lastid;
    }
    public String getVvId() {
        return vvId;
    }
    public void setVvId(String vvId) {
        this.vvId = vvId;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public String getBriefId() {
        return briefId;
    }
    public void setBriefId(String briefId) {
        this.briefId = briefId;
    }
    public String getAppNo() {
        return appNo;
    }
    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public String getInoutType() {
        return inoutType;
    }
    public void setInoutType(String inoutType) {
        this.inoutType = inoutType;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getOptStatus() {
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getRepealId() {
        return repealId;
    }
    public void setRepealId(String repealId) {
        this.repealId = repealId;
    }
    @Override
    public String toString() {
        return "AbPoolDtlDTO{" +
                "id='" + id + '\'' +
                ", lastid='" + lastid + '\'' +
                ", vvId='" + vvId + '\'' +
                ", bpId='" + bpId + '\'' +
                ", briefId='" + briefId + '\'' +
                ", appNo='" + appNo + '\'' +
                ", origNo='" + origNo + '\'' +
                ", inoutType='" + inoutType + '\'' +
                ", status=" + status +
                ", optStatus='" + optStatus + '\'' +
                ", validSign='" + validSign + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", updateId='" + updateId + '\'' +
                ", updateTime=" + updateTime +
                ", repealId='" + repealId + '\'' +
                '}';
    }
}
