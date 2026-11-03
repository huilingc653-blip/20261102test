package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 债券注册实体
 *
 * @author ouyj
 */
@Entity
@Table(name = "CMS_BOND_REGISTER_INFO")
@SuppressWarnings("serial")
public class CmsBondRegisterInfo extends IdEntity implements Serializable {
    /**
     * 发行人/债务人
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 债券类型id
     */
    private String typeId;
    /**
     * 发行批文名称
     */
    private String issueName;
    /**
     * 债券代码
     */
    private String bondCode;
    /**
     * 批准文号
     */
    private String approvalNumber;
    /**
     * 债券全称
     */
    private String bondFullName;
    /**
     * 债券简称
     */
    private String bondShortName;
    /**
     * 债券期限
     */
    private Integer bondDeadline;
    /**
     * 债券期限单位
     */
    private String deadlineUnit;
    /**
     * 债券原币币种
     */
    private String bondCur;
    /**
     * 计划发行原币总额
     */
    private BigDecimal issueCurTotal;
    /**
     * 计划发行人民币总额
     */
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
    private Date convertStartDate;
    /**
     * 可转换结束日
     */
    private Date convertEndDate;
    /**
     * 转换比例
     */
    private BigDecimal convertRate;
    /**
     * 融资用途
     */
    private String purpose;
    /**
     * 债券备注
     */
    private String rmk;
    /**
     * 境内/境外
     */
    private String isTerritory;
    /**
     * 状态
     */
    private Integer status;
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
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 币别名称
     */
    private String curName;
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "TYPE_ID")
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    @Column(name = "ISSUE_NAME")
    public String getIssueName() {
        return issueName;
    }
    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }
    @Column(name = "BOND_CODE")
    public String getBondCode() {
        return bondCode;
    }
    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }
    @Column(name = "APPROVAL_NUMBER")
    public String getApprovalNumber() {
        return approvalNumber;
    }
    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }
    @Column(name = "BOND_FULL_NAME")
    public String getBondFullName() {
        return bondFullName;
    }
    public void setBondFullName(String bondFullName) {
        this.bondFullName = bondFullName;
    }
    @Column(name = "BOND_SHORT_NAME")
    public String getBondShortName() {
        return bondShortName;
    }
    public void setBondShortName(String bondShortName) {
        this.bondShortName = bondShortName;
    }
    @Column(name = "BOND_DEADLINE")
    public Integer getBondDeadline() {
        return bondDeadline;
    }
    public void setBondDeadline(Integer bondDeadline) {
        this.bondDeadline = bondDeadline;
    }
    @Column(name = "DEADLINE_UNIT")
    public String getDeadlineUnit() {
        return deadlineUnit;
    }
    public void setDeadlineUnit(String deadlineUnit) {
        this.deadlineUnit = deadlineUnit;
    }
    @Column(name = "BOND_CUR")
    public String getBondCur() {
        return bondCur;
    }
    public void setBondCur(String bondCur) {
        this.bondCur = bondCur;
    }
    @Column(name = "ISSUE_CUR_TOTAL")
    public BigDecimal getIssueCurTotal() {
        return issueCurTotal;
    }
    public void setIssueCurTotal(BigDecimal issueCurTotal) {
        this.issueCurTotal = issueCurTotal;
    }
    @Column(name = "ISSUE_RMB_TOTAL")
    public BigDecimal getIssueRmbTotal() {
        return issueRmbTotal;
    }
    public void setIssueRmbTotal(BigDecimal issueRmbTotal) {
        this.issueRmbTotal = issueRmbTotal;
    }
    @Column(name = "BOND_CREDIT_RATE")
    public String getBondCreditRate() {
        return bondCreditRate;
    }
    public void setBondCreditRate(String bondCreditRate) {
        this.bondCreditRate = bondCreditRate;
    }
    @Column(name = "DEBTOR_CREDIT_RATE")
    public String getDebtorCreditRate() {
        return debtorCreditRate;
    }
    public void setDebtorCreditRate(String debtorCreditRate) {
        this.debtorCreditRate = debtorCreditRate;
    }
    @Column(name = "CONVERT_START_DATE")
    public Date getConvertStartDate() {
        return convertStartDate;
    }
    public void setConvertStartDate(Date convertStartDate) {
        this.convertStartDate = convertStartDate;
    }
    @Column(name = "CONVERT_END_DATE")
    public Date getConvertEndDate() {
        return convertEndDate;
    }
    public void setConvertEndDate(Date convertEndDate) {
        this.convertEndDate = convertEndDate;
    }
    @Column(name = "CONVERT_RATE")
    public BigDecimal getConvertRate() {
        return convertRate;
    }
    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }
    @Column(name = "PURPOSE")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "IS_TERRITORY")
    public String getIsTerritory() {
        return isTerritory;
    }
    public void setIsTerritory(String isTerritory) {
        this.isTerritory = isTerritory;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "IS_DELETE")
    public String getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
