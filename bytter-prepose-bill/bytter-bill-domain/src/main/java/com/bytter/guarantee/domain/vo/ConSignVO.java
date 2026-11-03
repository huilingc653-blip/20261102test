package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.guarantee.constant.ConConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ConSignVO extends ConInfoVO implements Serializable, IBusinessKey {
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
    @Dict(dictGroup = "CON_SIGN_STATUS", inject = "statusName")
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
    @Dict(dictGroup = "CON_SIGN_TYPE", inject = "signTypeName")
    private String signType;
    private String signTypeName;
    /**
     * 合同类型名称
     */
    private String typeId;
    @BtCompositeSearch(fieldName = "ci.type_id")
    private String typeName;
    /** 是否隐藏复选框 1为是 */
    private String checkboxHide;
    private String businessKey;

    /**
     * 起草日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ci.draft_date")
    private Date draftDate;

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ci.begin_Date")
    private Date beginDate;

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
    public String getBusinessKey() {
        return businessKey;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    public String getCheckboxHide() {
        if (ConConstant.CON_DRAFT_STATUS_11.equals(this.getStatus())
                || ConConstant.CON_DRAFT_STATUS_95.equals(this.getStatus())) {
            return "1";
        } else {
            return "0";
        }
    }
    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
    }
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
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getConNo() {
        return conNo;
    }
    public void setConNo(String conNo) {
        this.conNo = conNo;
    }
    public String getConName() {
        return conName;
    }
    public void setConName(String conName) {
        this.conName = conName;
    }
    public String getSignType() {
        return signType;
    }
    public void setSignType(String signType) {
        this.signType = signType;
    }
    public String getSignTypeName() {
        return signTypeName;
    }
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
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
