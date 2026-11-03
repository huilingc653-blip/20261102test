package com.bytter.guarantee.domain.vo;
import java.math.BigDecimal;
/**
 * @author zhengg
 * @version 1.0
 * @Title: 合同申请流水表VO
 * @date 2019-11-07
 */
public class ConInfoRecordVO implements java.io.Serializable {
    public ConInfoRecordVO() {
    }
    public ConInfoRecordVO(String mid) {
        this.mid = mid;
    }
    public ConInfoRecordVO(String mid, String[] busTypes) {
        this.mid = mid;
        this.busTypes = busTypes;
    }
    public ConInfoRecordVO(String mid, String[] busTypes, Integer status, String validSign) {
        this.mid = mid;
        this.busTypes = busTypes;
        this.status = status;
        this.validSign = validSign;
    }
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 主信息ID
     */
    private String mid;
    /**
     * 上一笔流水Id
     */
    private String lastId;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 业务类型
     */
    private String[] busTypes;
    /**
     * 业务状态
     */
    private Integer status;
    /**
     * 变更\履约\终止原因
     */
    private String opMsg;
    /**
     * 变更\履约\终止时间
     */
    private java.util.Date opDate;
    /**
     * 履约\终止申请编号
     */
    private String opNo;
    /**
     * 履约金额
     */
    private BigDecimal opMoney;
    /**
     * 外部业务ID
     */
    private String outerId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 打回原因
     */
    private String backMsg;
    /**
     * 业务申请附件ID
     */
    private String attachmentId;
    /**
     * 创建人id
     */
    private String createId;
    /**
     * 创建日期
     */
    private java.util.Date createTime;
    /**
     * 更新人id
     */
    private String updateId;
    /**
     * 更新日期
     */
    private java.util.Date updateTime;
    /**
     * 有效标志
     */
    private String validSign;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMid() {
        return mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getLastId() {
        return lastId;
    }
    public void setLastId(String lastId) {
        this.lastId = lastId;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getOpMsg() {
        return opMsg;
    }
    public void setOpMsg(String opMsg) {
        this.opMsg = opMsg;
    }
    public java.util.Date getOpDate() {
        return opDate;
    }
    public void setOpDate(java.util.Date opDate) {
        this.opDate = opDate;
    }
    public String getOpNo() {
        return opNo;
    }
    public void setOpNo(String opNo) {
        this.opNo = opNo;
    }
    public BigDecimal getOpMoney() {
        return opMoney;
    }
    public void setOpMoney(BigDecimal opMoney) {
        this.opMoney = opMoney;
    }
    public String getOuterId() {
        return outerId;
    }
    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getBackMsg() {
        return backMsg;
    }
    public void setBackMsg(String backMsg) {
        this.backMsg = backMsg;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public java.util.Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String[] getBusTypes() {
        return busTypes;
    }
    public void setBusTypes(String[] busTypes) {
        this.busTypes = busTypes;
    }
    @Override
    public String toString() {
        return "ConInfoRecordVO [id=" + id + ", mid=" + mid + ", lastId=" + lastId + ", busType=" + busType
                + ", status=" + status + ", opMsg=" + opMsg + ", opDate=" + opDate + ", opNo=" + opNo + ", opMoney="
                + opMoney + ", outerId=" + outerId + ", rmk=" + rmk + ", backMsg=" + backMsg + ", attachmentId="
                + attachmentId + ", createId=" + createId + ", createTime=" + createTime + ", updateId=" + updateId
                + ", updateTime=" + updateTime + ", validSign=" + validSign + "]";
    }
}
