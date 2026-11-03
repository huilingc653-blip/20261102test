package com.bytter.finance.domain.bond.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Vo
 * @Description: 债券还款计划表
 * @date 2022-06-06 10:07:46
 */
@SuppressWarnings("serial")
public class CmsBondRepayPlanVo implements Serializable, IBusinessKey {
    private String id;
    /**
     * 所属单位
     */
    @BtCompositeSearch(fieldName = "r.corp_id")
    private String corpId;
    private String corpName;
    /**
     * 债劵注册id
     */
    private String registerId;
    /**
     * 债劵发行id
     */
    private String issueId;
    /**
     * 债劵发行编号
     */
    private String bondIssueNum;
    /**
     * 还款方式
     */
    private String repayType;
    private String repayTypeName;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 债券类型
     */
    private String typeId;
    /**
     * 债券类型名称
     */
    private String bondTypeName;
    /**
     * 计划发行原币总额
     */
    @BtCompositeSearch(fieldName = "r.issue_cur_total")
    private BigDecimal issueCurTotal;
    /**
     * 债券代码
     */
    @BtCompositeSearch(fieldName = "r.bond_code")
    private String bondCode;
    /**
     * 债券全称
     */
    @BtCompositeSearch(fieldName = "r.bond_full_name")
    private String bondFullName;
    /**
     * 债劵简称
     */
    @BtCompositeSearch(fieldName = "r.bond_short_name")
    private String bondShortName;
    /**
     * 是否保存操作
     */
    private String isSave;
    /**
     * 还款周期
     */
    private String repayCycel;
    /**
     * 还款开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayStartDate;
    /**
     * 债券原币币种
     */
//    @Currency(inject = "bondCurName")
    private String bondCur;
    private String bondCurName;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 发行状态状态
     */
    private Integer issueStatus;
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
     * 租金计划明细
     */
    private List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtls;
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
    public String getRegisterId() {
        return registerId;
    }
    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayTypeName() {
        return repayTypeName;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getBondTypeName() {
        return bondTypeName;
    }
    public void setBondTypeName(String bondTypeName) {
        this.bondTypeName = bondTypeName;
    }
    public BigDecimal getIssueCurTotal() {
        return issueCurTotal;
    }
    public void setIssueCurTotal(BigDecimal issueCurTotal) {
        this.issueCurTotal = issueCurTotal;
    }
    public String getBondCode() {
        return bondCode;
    }
    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
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
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getIsSave() {
        return isSave;
    }
    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }
    public String getRepayCycel() {
        return repayCycel;
    }
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
    }
    public Date getRepayStartDate() {
        return repayStartDate;
    }
    public void setRepayStartDate(Date repayStartDate) {
        this.repayStartDate = repayStartDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getIssueStatus() {
        return issueStatus;
    }
    public void setIssueStatus(Integer issueStatus) {
        this.issueStatus = issueStatus;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBondIssueNum() {
        return bondIssueNum;
    }
    public void setBondIssueNum(String bondIssueNum) {
        this.bondIssueNum = bondIssueNum;
    }
    public String getBondCur() {
        return bondCur;
    }
    public void setBondCur(String bondCur) {
        this.bondCur = bondCur;
    }
    public String getBondCurName() {
        return bondCurName;
    }
    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
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
    public List<CmsBondRepayPlanDtlVo> getCmsBondRepayPlanDtls() {
        return cmsBondRepayPlanDtls;
    }
    public void setCmsBondRepayPlanDtls(List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtls) {
        this.cmsBondRepayPlanDtls = cmsBondRepayPlanDtls;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
