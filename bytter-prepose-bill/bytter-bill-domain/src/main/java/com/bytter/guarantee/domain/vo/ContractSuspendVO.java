package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 *
 * @Title: 合同终止VO
 * @Package com.bytter.biz.contract.vo
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-5 下午8:19:45
 * @version V1.0
 */
public class ContractSuspendVO extends ConInfoVO implements Serializable, IBusinessKey {
    /**
     * 合同发起方单位ID
     */
    @BtCompositeSearch(fieldName = "ci.corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName = "ci.corp_code")
    private String corpName;
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
     * 合同字段明细
     */
    private List<ConInfoBusdtlVO> busdtlList;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 业务状态
     */
    @Dict(dictGroup = "CON_TERMINATE_STATUS", inject = "statusName")
    private Integer status;
    @BtCompositeSearch(fieldName = "status")
    private String statusName;
    /**
     * 主表业务状态
     */
    @Dict(dictGroup = "CON_INFO_STATUS", inject = "ciStatusName")
    private Integer ciStatus;
    @BtCompositeSearch(fieldName = "ci.status")
    private String ciStatusName;
    @Dict(dictGroup = "CON_TERMINATE_STATUS", inject = "apprveStatusName")
    private Integer apprveStatus;
    @BtCompositeSearch(fieldName = "cir.status")
    private String apprveStatusName;
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
     * 流水合同信息
     */
    private String reAttachmentIde;
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
    @BtCompositeSearch(fieldName = "ci.SIGN_TYPE")
    private String signTypeName;
    /**
     * 合同类型名称
     */
    @BtCompositeSearch(fieldName = "ci.type_id")
    private String typeName;
    /**
     * 终止原因
     */
    @BtCompositeSearch(fieldName = "cir.OP_MSG")
    private String opMsg;
    /**
     * 终止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date opDate;
    /**
     * 终止申请单号
     */
    private String opNo;
    /** 交易日期 */
    @BtCompositeSearch(fieldName = "ci.SIGN_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transDate;
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
    private String businessKey;
    @Override
    public String getBusinessKey() {
        return businessKey;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
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
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
    public String getReAttachmentIde() {
        return reAttachmentIde;
    }
    public void setReAttachmentIde(String reAttachmentIde) {
        this.reAttachmentIde = reAttachmentIde;
    }
    public Integer getApprveStatus() {
        return apprveStatus;
    }
    public void setApprveStatus(Integer apprveStatus) {
        this.apprveStatus = apprveStatus;
    }
    public String getApprveStatusName() {
        return apprveStatusName;
    }
    public void setApprveStatusName(String apprveStatusName) {
        this.apprveStatusName = apprveStatusName;
    }
    public Integer getCiStatus() {
        return ciStatus;
    }
    public void setCiStatus(Integer ciStatus) {
        this.ciStatus = ciStatus;
    }
    public String getCiStatusName() {
        return ciStatusName;
    }
    public void setCiStatusName(String ciStatusName) {
        this.ciStatusName = ciStatusName;
    }
    public Date getTransDate() {
        return transDate;
    }
    public void setTransDate(Date transDate) {
        this.transDate = transDate;
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
    public List<ConInfoBusdtlVO> getBusdtlList() {
        return busdtlList;
    }
    public void setBusdtlList(List<ConInfoBusdtlVO> busdtlList) {
        this.busdtlList = busdtlList;
    }
}
