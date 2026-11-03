package com.bytter.guarantee.domain.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.guarantee.constant.ConConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @version V1.0
 * @Title: 合同主表 VO
 * @Description: 合同主表 VO
 * @author: zhenghj
 * @date: 2019-11-4
 */
@SuppressWarnings("serial")
public class ConInfoVO implements Serializable {
    public ConInfoVO() {
    }
    public ConInfoVO(String recordId) {
        this.recordId = recordId;
    }
    public ConInfoVO(String id, String busId) {
        this.id = id;
        this.busId = busId;
    }
    public ConInfoVO(String id, String isPerformanceNum, String isChangeNum) {
        this.id = id;
        this.isPerformanceNum = isPerformanceNum;
        this.isChangeNum = isChangeNum;
    }
    /**
     * 合同主表ID
     */
    @BtCompositeSearch(fieldName = "ci.id")
    private String id;
    /**
     * 挂接业务ID
     */
    @BtCompositeSearch(fieldName = "cbh.bus_id")
    private String busId;
    /**
     * 业务流水ID
     */
    @BtCompositeSearch(fieldName = "cir.id")
    private String recordId;
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
     * 合同发起方单位ID
     */
    @BtCompositeSearch(fieldName = "ci.corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName = "ci.corp_name")
    private String corpName;
    private String corpCode;
    /**
     * 签署方式
     */
    @BtCompositeSearch(fieldName = "ci.SIGN_TYPE")
    @Dict(dictGroup = "CON_SIGN_TYPE", inject = "signTypeName")
    private String signType;
    @BtCompositeSearch(fieldName = "ci.SIGN_TYPE")
    private String signTypeName;
    /**
     * 合同类型ID
     */
    @Dict(tableName = "CON_TYPE", valueFieldName = "ID", showFiledName = "CON_NAME", inject = "typeName")
    private String typeId;
    /**
     * 合同类型名称
     */
    @BtCompositeSearch(fieldName = "ci.type_id")
    private String typeName;
    /**
     * 合同生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ci.begin_Date")
    private Date beginDate;
    /**
     * 合同失效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ci.end_Date")
    private Date endDate;
    /**
     * 履约次数
     */
    private Integer performanceNum;
    /**
     * 变更次数
     */
    private Integer changeNum;
    /**
     * 合同状态
     */
    @Dict(dictGroup = ConConstant.CON_INFO_STATUS, inject = "statusName")
    private Integer status;
    /**
     * 合同状态
     */
    private String statusName;
    /**
     * 合同附件ID
     */
    private String attachmentId;
    /**
     * 起草日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ci.draft_date")
    private Date draftDate;
    /**
     * 签署日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ci.SIGN_DATE")
    private Date signDate;
    /**
     * 业务选票传递单位操作资源
     */
    private String opResource;
    /**
     * 是否履约操作
     */
    private String isPerformanceNum;
    /**
     * 是否变更操作
     */
    private String isChangeNum;
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
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
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
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getPerformanceNum() {
        return performanceNum;
    }
    public void setPerformanceNum(Integer performanceNum) {
        this.performanceNum = performanceNum;
    }
    public Integer getChangeNum() {
        return changeNum;
    }
    public void setChangeNum(Integer changeNum) {
        this.changeNum = changeNum;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public Date getDraftDate() {
        return draftDate;
    }
    public void setDraftDate(Date draftDate) {
        this.draftDate = draftDate;
    }
    public Date getSignDate() {
        return signDate;
    }
    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBusId() {
        return busId;
    }
    public void setBusId(String busId) {
        this.busId = busId;
    }
    public String getRecordId() {
        return recordId;
    }
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
    public String getOpResource() {
        return opResource;
    }
    public void setOpResource(String opResource) {
        this.opResource = opResource;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getIsPerformanceNum() {
        return isPerformanceNum;
    }
    public void setIsPerformanceNum(String isPerformanceNum) {
        this.isPerformanceNum = isPerformanceNum;
    }
    public String getIsChangeNum() {
        return isChangeNum;
    }
    public void setIsChangeNum(String isChangeNum) {
        this.isChangeNum = isChangeNum;
    }
    @Override
    public String toString() {
        return "ConInfoVO [id=" + id + ", busId=" + busId + ", recordId=" + recordId + ", conNo=" + conNo + ", conName="
                + conName + ", corpId=" + corpId + ", corpName=" + corpName + ", corpCode=" + corpCode + ", signType="
                + signType + ", signTypeName=" + signTypeName + ", typeId=" + typeId + ", typeName=" + typeName
                + ", beginDate=" + beginDate + ", endDate=" + endDate + ", performanceNum=" + performanceNum
                + ", changeNum=" + changeNum + ", status=" + status + ", statusName=" + statusName + ", attachmentId="
                + attachmentId + ", draftDate=" + draftDate + ", signDate=" + signDate + ", opResource=" + opResource
                + ", isPerformanceNum=" + isPerformanceNum + ", isChangeNum=" + isChangeNum + "]";
    }
}
