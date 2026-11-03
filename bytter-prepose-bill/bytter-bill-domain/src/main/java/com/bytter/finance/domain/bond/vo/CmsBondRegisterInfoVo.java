package com.bytter.finance.domain.bond.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 债券注册vo
 *
 * @author ouyj
 */
@SuppressWarnings("serial")
public class CmsBondRegisterInfoVo implements Serializable, IBusinessKey {
    /**
     * id
     */
    private String id;
    /**
     * 发行人/债务人
     */
    @BtCompositeSearch(fieldName = "r.corp_id")
    private String corpId;
    /**
     * 单位名称
     */
    @BtCompositeSearch(fieldName = "r.corp_name")
    private String corpName;
    /**
     * 债券类型id
     */
    @BtCompositeSearch(fieldName = "r.type_id")
    private String typeId;
    /**
     * 债券类型名
     */
    @BtCompositeSearch(fieldName = "r.type_id")
    private String bondTypeName;
    /**
     * 发行批文名称
     */
    private String issueName;
    /**
     * 债券代码
     */
    @BtCompositeSearch(fieldName = "r.bond_code")
    private String bondCode;
    /**
     * 批准文号
     */
    private String approvalNumber;
    /**
     * 债券全称
     */
    @BtCompositeSearch(fieldName = "r.bond_full_name")
    private String bondFullName;
    /**
     * 债券简称
     */
    @BtCompositeSearch(fieldName = "r.bond_short_name")
    private String bondShortName;
    /**
     * 债券期限
     */
    @BtCompositeSearch(fieldName = "r.bond_deadline")
    private Integer bondDeadline;
    /**
     * 债券期限单位
     */
    private String deadlineUnit;
    /**
     * 债券原币币种
     */
//    @Currency(inject = "bondCurName")
    @BtCompositeSearch(fieldName = "r.bond_cur")
    private String bondCur;
    @BtCompositeSearch(fieldName = "r.bond_cur")
    private String bondCurName;
    /**
     * 计划发行原币总额
     */
    @BtCompositeSearch(fieldName = "r.issue_cur_total")
    private BigDecimal issueCurTotal;
    /**
     * 计划发行人民币总额
     */
    @BtCompositeSearch(fieldName = "r.issue_rmb_total")
    private BigDecimal issueRmbTotal;
    /**
     * 债券信用评级
     */
    private String bondCreditRate;
    /**
     * 发行人信用评级
     */
    private String debtorCreditRate;
    /**
     * 可转换开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date convertStartDate;
    /**
     * 可转换结束日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date convertEndDate;
    /**
     * 转换比例
     */
    private BigDecimal convertRate;
    /**
     * 融资用途
     */
    @BtCompositeSearch(fieldName = "r.purpose")
    private String purpose;
    /**
     * 债券备注
     */
    @BtCompositeSearch(fieldName = "r.rmk")
    private String rmk;
    /**
     * 境内/境外
     */
    private String isTerritory;
    /**
     * 状态
     */
    @Dict(dictGroup = "AB_BILL_EMITBILL_STATUS", inject = "statusName")
    private Integer status;
    /**
     * 状态名
     */
    private String statusName;
    /**
     * 是否删除
     */
    private String isDelete;
    /**
     * 附件id
     */
    private String attachmentId;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**
     * 发行id
     */
    private String issueId;
    /**
     * 发行状态
     */
    @Dict(dictGroup = "AB_BILL_EMITBILL_STATUS", inject = "issueStatusName")
    private Integer issueStatus;
    /**
     * 发行状态
     */
    private Integer issueStatusName;
    /**
     * 债券发行编号
     */
    private String bondIssueNum;
    /**
     * 展期次数
     */
    private String deferredNum;
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    public Integer getIssueStatus() {
        return issueStatus;
    }
    public void setIssueStatus(Integer issueStatus) {
        this.issueStatus = issueStatus;
    }
    public String getBondIssueNum() {
        return bondIssueNum;
    }
    public void setBondIssueNum(String bondIssueNum) {
        this.bondIssueNum = bondIssueNum;
    }
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
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getIssueName() {
        return issueName;
    }
    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }
    public String getBondCode() {
        return bondCode;
    }
    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }
    public String getApprovalNumber() {
        return approvalNumber;
    }
    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }
    public String getBondFullName() {
        return bondFullName;
    }
    public void setBondFullName(String bondFullName) {
        this.bondFullName = bondFullName;
    }
    public String getBondShortName() {
        return bondShortName;
    }
    public void setBondShortName(String bondShortName) {
        this.bondShortName = bondShortName;
    }
    public Integer getBondDeadline() {
        return bondDeadline;
    }
    public void setBondDeadline(Integer bondDeadline) {
        this.bondDeadline = bondDeadline;
    }
    public String getDeadlineUnit() {
        return deadlineUnit;
    }
    public void setDeadlineUnit(String deadlineUnit) {
        this.deadlineUnit = deadlineUnit;
    }
    public String getBondCur() {
        return bondCur;
    }
    public void setBondCur(String bondCur) {
        this.bondCur = bondCur;
    }
    public BigDecimal getIssueCurTotal() {
        return issueCurTotal;
    }
    public void setIssueCurTotal(BigDecimal issueCurTotal) {
        this.issueCurTotal = issueCurTotal;
    }
    public BigDecimal getIssueRmbTotal() {
        return issueRmbTotal;
    }
    public void setIssueRmbTotal(BigDecimal issueRmbTotal) {
        this.issueRmbTotal = issueRmbTotal;
    }
    public String getBondCreditRate() {
        return bondCreditRate;
    }
    public void setBondCreditRate(String bondCreditRate) {
        this.bondCreditRate = bondCreditRate;
    }
    public String getDebtorCreditRate() {
        return debtorCreditRate;
    }
    public void setDebtorCreditRate(String debtorCreditRate) {
        this.debtorCreditRate = debtorCreditRate;
    }
    public Date getConvertStartDate() {
        return convertStartDate;
    }
    public void setConvertStartDate(Date convertStartDate) {
        this.convertStartDate = convertStartDate;
    }
    public Date getConvertEndDate() {
        return convertEndDate;
    }
    public void setConvertEndDate(Date convertEndDate) {
        this.convertEndDate = convertEndDate;
    }
    public BigDecimal getConvertRate() {
        return convertRate;
    }
    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getIsTerritory() {
        return isTerritory;
    }
    public void setIsTerritory(String isTerritory) {
        this.isTerritory = isTerritory;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getBondCurName() {
        return bondCurName;
    }
    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getBondTypeName() {
        return bondTypeName;
    }
    public void setBondTypeName(String bondTypeName) {
        this.bondTypeName = bondTypeName;
    }
    public Integer getIssueStatusName() {
        return issueStatusName;
    }
    public void setIssueStatusName(Integer issueStatusName) {
        this.issueStatusName = issueStatusName;
    }
    public String getDeferredNum() {
        return deferredNum;
    }
    public void setDeferredNum(String deferredNum) {
        this.deferredNum = deferredNum;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
    @Override
    public String toString() {
        return "CmsBondRegisterInfoVo{" + "id='" + id + '\'' + ", corpId='" + corpId + '\'' + ", corpName='" + corpName
                + '\'' + ", typeId='" + typeId + '\'' + ", bondTypeName='" + bondTypeName + '\'' + ", issueName='"
                + issueName + '\'' + ", bondCode='" + bondCode + '\'' + ", approvalNumber='" + approvalNumber + '\''
                + ", bondFullName='" + bondFullName + '\'' + ", bondShortName='" + bondShortName + '\''
                + ", bondDeadline=" + bondDeadline + ", deadlineUnit='" + deadlineUnit + '\'' + ", bondCur='" + bondCur
                + '\'' + ", bondCurName='" + bondCurName + '\'' + ", issueCurTotal=" + issueCurTotal
                + ", issueRmbTotal=" + issueRmbTotal + ", bondCreditRate='" + bondCreditRate + '\''
                + ", debtorCreditRate='" + debtorCreditRate + '\'' + ", convertStartDate=" + convertStartDate
                + ", convertEndDate=" + convertEndDate + ", convertRate=" + convertRate + ", purpose='" + purpose + '\''
                + ", rmk='" + rmk + '\'' + ", isTerritory='" + isTerritory + '\'' + ", status=" + status
                + ", statusName='" + statusName + '\'' + ", isDelete='" + isDelete + '\'' + ", attachmentId='"
                + attachmentId + '\'' + ", createBy='" + createBy + '\'' + ", createDate=" + createDate + ", updateBy='"
                + updateBy + '\'' + ", updateDate=" + updateDate + '}';
    }
}
