package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * @version V1.0
 * @Title: 合同登记VO
 * @Description: 合同登记VO
 * @author: zhenghj
 * @date: 2019-11-11
 */
@SuppressWarnings("serial")
public class ConRegisterVO extends ConInfoVO implements Serializable, IBusinessKey {
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
    private String signType;
    private String signTypeName;
    private String typeId;
    @BtCompositeSearch(fieldName = "ci.type_id")
    private String typeName;
    private String businessKey;
    /**
     * 合同字段明细
     */
    private List<ConInfoBusdtlVO> busdtls;
    /**
     * 起草日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ci.draft_date")
    private Date draftDate;
    /**
     * 合同生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ci.begin_date")
    private Date beginDate;
    /**
     * 合同失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ci.end_date")
    private Date endDate;
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
    public String getTypeId() {
        return typeId;
    }
    @Override
    public void setTypeId(String typeId) {
        this.typeId = typeId;
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
    public List<ConInfoBusdtlVO> getBusdtls() {
        return busdtls;
    }
    public void setBusdtls(List<ConInfoBusdtlVO> busdtls) {
        this.busdtls = busdtls;
    }
    @Override
    public Date getDraftDate() {
        return draftDate;
    }
    @Override
    public void setDraftDate(Date draftDate) {
        this.draftDate = draftDate;
    }
    @Override
    public Date getBeginDate() {
        return beginDate;
    }
    @Override
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    @Override
    public Date getEndDate() {
        return endDate;
    }
    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Override
    public String toString() {
        return "ConDraftVO{" + "mid='" + mid + '\'' + ", rid='" + rid + '\'' + ", lastId='" + lastId + '\''
                + ", busType='" + busType + '\'' + ", status=" + status + ", statusName='" + statusName + '\''
                + ", outerId='" + outerId + '\'' + ", rmk='" + rmk + '\'' + ", backMsg='" + backMsg + '\''
                + ", attachmentId='" + attachmentId + '\'' + ", validSign='" + validSign + '\'' + ", conNo='" + conNo
                + '\'' + ", conName='" + conName + '\'' + ", signType='" + signType + '\'' + ", signTypeName='"
                + signTypeName + '\'' + ", typeName='" + typeName + '\'' + ", businessKey='" + businessKey + '\'' + '}';
    }
}
