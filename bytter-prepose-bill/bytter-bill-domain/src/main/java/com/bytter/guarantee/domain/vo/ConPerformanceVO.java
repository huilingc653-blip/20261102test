package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @version V1.0
 * @Title: 合同履约VO
 * @Description: 合同履约VO
 * @author: zhenghj
 * @date: 2019-11-14
 */
@SuppressWarnings("serial")
public class ConPerformanceVO extends ConInfoVO implements Serializable, IBusinessKey {
    /**
     * 主信息ID
     */
    private String mid;
    /**
     * 流水Id
     */
    private String rid;
    /**
     * 上一笔流水Id
     */
    private String lastId;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 业务状态
     */
    @Dict(dictGroup = "CON_DRAFT_STATUS", inject = "statusName")
    private Integer status;
    @BtCompositeSearch(fieldName = "cir.status")
    private String statusName;
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
     * 主表附件ID
     */
    private String conAttachmentId;
    /**
     * 业务申请附件ID
     */
    private String attachmentId;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 合同编号
     */
    @BtCompositeSearch(fieldName = "ci.con_no")
    private String conNo;
    /**
     * 合同名称
     */
    @BtCompositeSearch(fieldName = "ci.con_name")
    private String conName;
    /**
     * 签署方式
     */
    @BtCompositeSearch(fieldName = "ci.SIGN_TYPE")
    @Dict(dictGroup = "CON_SIGN_TYPE", inject = "signTypeName")
    private String signType;
    private String signTypeName;
    /**
     * 合同类型名称
     */
    @BtCompositeSearch(fieldName = "ci.type_id")
    private String typeName;
    private String businessKey;
    /**
     * 履约时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "cir.op_date")
    private Date opDate;
    /**
     * 履约原因
     */
    private String opMsg;
    /**
     * 履约申请编号
     */
    private String opNo;
    /**
     * 履约金额
     */
    @BtCompositeSearch(fieldName = "cir.OP_MONEY")
    private BigDecimal opMoney;
    /**
     * 合同字段明细
     */
    private List<ConInfoBusdtlVO> busdtls;
    public String getMid() {
        return mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getRid() {
        return rid;
    }
    public void setRid(String rid) {
        this.rid = rid;
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
    @Override
    public Integer getStatus() {
        return status;
    }
    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
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
    @Override
    public String getAttachmentId() {
        return attachmentId;
    }
    @Override
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Override
    public String getConNo() {
        return conNo;
    }
    @Override
    public void setConNo(String conNo) {
        this.conNo = conNo;
    }
    @Override
    public String getConName() {
        return conName;
    }
    @Override
    public void setConName(String conName) {
        this.conName = conName;
    }
    @Override
    public String getSignType() {
        return signType;
    }
    @Override
    public void setSignType(String signType) {
        this.signType = signType;
    }
    @Override
    public String getSignTypeName() {
        return signTypeName;
    }
    @Override
    public void setSignTypeName(String signTypeName) {
        this.signTypeName = signTypeName;
    }
    @Override
    public String getTypeName() {
        return typeName;
    }
    @Override
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getBusinessKey() {
        return businessKey;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    public Date getOpDate() {
        return opDate;
    }
    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }
    public String getOpMsg() {
        return opMsg;
    }
    public void setOpMsg(String opMsg) {
        this.opMsg = opMsg;
    }
    public String getConAttachmentId() {
        return conAttachmentId;
    }
    public void setConAttachmentId(String conAttachmentId) {
        this.conAttachmentId = conAttachmentId;
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
    public List<ConInfoBusdtlVO> getBusdtls() {
        return busdtls;
    }
    public void setBusdtls(List<ConInfoBusdtlVO> busdtls) {
        this.busdtls = busdtls;
    }
    @Override
    public String toString() {
        return "ConPerformanceVO{" + "mid='" + mid + '\'' + ", rid='" + rid + '\'' + ", lastId='" + lastId + '\''
                + ", busType='" + busType + '\'' + ", status=" + status + ", statusName='" + statusName + '\''
                + ", outerId='" + outerId + '\'' + ", rmk='" + rmk + '\'' + ", backMsg='" + backMsg + '\''
                + ", conAttachmentId='" + conAttachmentId + '\'' + ", attachmentId='" + attachmentId + '\''
                + ", validSign='" + validSign + '\'' + ", conNo='" + conNo + '\'' + ", conName='" + conName + '\''
                + ", signType='" + signType + '\'' + ", signTypeName='" + signTypeName + '\'' + ", typeName='"
                + typeName + '\'' + ", businessKey='" + businessKey + '\'' + ", opDate=" + opDate + ", opMsg='" + opMsg
                + '\'' + ", opNo='" + opNo + '\'' + ", opMoney=" + opMoney + ", busdtls=" + busdtls + '}';
    }
}
