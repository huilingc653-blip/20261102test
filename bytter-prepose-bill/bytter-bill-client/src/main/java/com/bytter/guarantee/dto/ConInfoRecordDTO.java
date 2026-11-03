package com.bytter.guarantee.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.Dict;
import com.bytter.guarantee.constant.ConConstant;
/**
 * 合同流业务水信息DTO
 * 
 * @author zhengg
 *
 */
public class ConInfoRecordDTO implements Serializable {
    /**
     * 流水ID
     */
    private String id;
    /*** conId 合同ID 必填为空提示异常 */
    private String conId;
    /*** outerId 履约\终止业务挂接ID 必填为空提示异常 */
    private String outerId;
    /*** opMsg 履约\终止原因 必填为空提示异常 */
    private String opMsg;
    /*** opDate 履约\终止时间 必填为空提示异常 */
    private Date opDate;
    /*** opNo 履约\终止申请编号 选填 */
    private String opNo;
    /*** opMoney 履约金额 选填 */
    private BigDecimal opMoney;
    /*** busType 业务类型（01 起草02 签署03 登记04 变更05 履约06 终止） */
    private String busType;
    @Dict(dictGroup = ConConstant.CON_OTHER_STATUS, inject = "statusName")
    private Integer status;
    private String statusName;
    /**
     * 合同附件ID
     */
    private String attachmentId;
    /**
     * 合同附件ID
     */
    private String rmk;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public String getOuterId() {
        return outerId;
    }
    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }
    public String getOpMsg() {
        return opMsg;
    }
    public void setOpMsg(String opMsg) {
        this.opMsg = opMsg;
    }
    public Date getOpDate() {
        return opDate;
    }
    public void setOpDate(Date opDate) {
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
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
