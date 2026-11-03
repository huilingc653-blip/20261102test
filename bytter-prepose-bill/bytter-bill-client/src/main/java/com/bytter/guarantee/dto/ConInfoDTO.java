package com.bytter.guarantee.dto;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.guarantee.constant.ConConstant;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 合同信息DTO
 * 
 * @author zhengg
 *
 */
public class ConInfoDTO implements Serializable {
    /**
     * 合同ID
     */
    private String id;
    /**
     * 合同发起人单位ID 必填为空提示异常
     */
    @Corp(inject = "corpName")
    private String corpId;
    /**
     * 合同发起人单位ID 必填为空提示异常
     */
    private String corpName;
    /**
     *贷款单位
     */
    private String corpCode;
    /**
     * 单位DTO
     */
    private CorpDTO corpDTO;
    /**
     * 挂接业务ID 必填为空提示异常
     */
    private String busId;
    /**
     * 签署方式
     */
    @Dict(dictGroup = ConConstant.CON_SIGN_TYPE, inject = "signTypeName")
    private String signType;
    /**
     * 签署方式名称
     */
    private String signTypeName;
    /**
     * 合同名称 必填为空提示异常
     */
    private String conName;
    /**
     * 合同编号 可以为空
     */
    private String conNo;
    /**
     * 合同类型ID 必填为空提示异常
     */
    @Dict(tableName = "CON_TYPE", valueFieldName = "id", showFiledName = "con_Name", inject = "typeName")
    private String typeId;
    /**
     * 合同类型ID 必填为空提示异常
     */
    private String typeName;
    /**
     * 合同生效日期 必填为空提示异常
     */
    private Date beginDate;
    /**
     * 合同失效日期 可以为空
     */
    private Date endDate;
    /**
     * 外部业务挂接数据 必填为空或者匹配规则必填项空提示异常
     */
    private Object outBusData;
    /**
     * 合同状态
     */
    @Dict(dictGroup = ConConstant.CON_INFO_STATUS, inject = "statusName")
    private Integer status;
    /**
     * 合同状态名称
     */
    private String statusName;
    /**
     * 合同附件ID
     */
    private String attachmentId;
    /**
     * 履约次数
     */
    private Integer performanceNum;
    /**
     * 变更次数
     */
    private Integer changeNum;
    /**
     * 起草日期
     */
    private Date draftDate;
    /**
     * 签署日期
     */
    private Date signDate;
    /**
     * 合同业务明细自动集合
     */
    private List<ConInfoBusdtlDTO> busdtlList;
    /**
     * 合同业务流水集合
     */
    private List<ConInfoRecordDTO> recordList;
    /**
     * 流水ID
     */
    private String recordId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBusId() {
        return busId;
    }
    public void setBusId(String busId) {
        this.busId = busId;
    }
    public String getConName() {
        return conName;
    }
    public void setConName(String conName) {
        this.conName = conName;
    }
    public String getConNo() {
        return conNo;
    }
    public void setConNo(String conNo) {
        this.conNo = conNo;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
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
    public Object getOutBusData() {
        return outBusData;
    }
    public void setOutBusData(Object outBusData) {
        this.outBusData = outBusData;
    }
    public List<ConInfoRecordDTO> getRecordList() {
        return recordList;
    }
    public void setRecordList(List<ConInfoRecordDTO> recordList) {
        this.recordList = recordList;
    }
    public List<ConInfoBusdtlDTO> getBusdtlList() {
        return busdtlList;
    }
    public void setBusdtlList(List<ConInfoBusdtlDTO> busdtlList) {
        this.busdtlList = busdtlList;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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
    public CorpDTO getCorpDTO() {
        return corpDTO;
    }
    public void setCorpDTO(CorpDTO corpDTO) {
        this.corpDTO = corpDTO;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
    public String getRecordId() {
        return recordId;
    }
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    @Override
    public String toString() {
        return "ConInfoDTO{" +
                "id='" + id + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", corpDTO=" + corpDTO +
                ", busId='" + busId + '\'' +
                ", signType='" + signType + '\'' +
                ", signTypeName='" + signTypeName + '\'' +
                ", conName='" + conName + '\'' +
                ", conNo='" + conNo + '\'' +
                ", typeId='" + typeId + '\'' +
                ", typeName='" + typeName + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", outBusData=" + outBusData +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", attachmentId='" + attachmentId + '\'' +
                ", performanceNum=" + performanceNum +
                ", changeNum=" + changeNum +
                ", draftDate=" + draftDate +
                ", signDate=" + signDate +
                ", busdtlList=" + busdtlList +
                ", recordList=" + recordList +
                ", recordId='" + recordId + '\'' +
                '}';
    }
}
