package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Entity
 * @Description: 智能选票-选票规则组明细
 * @date 2022-10-10
 */
@Entity
@Table(name = "AB_INTELLECT_RULE_GROUP_DTL", schema = "")
@SuppressWarnings("serial")
public class AbIntellectRuleGroupDtl extends IdEntity implements java.io.Serializable {
    /**
     * 规则组编码
     */
    private String ruleGroupCode;
    /**
     * 选票规则id
     */
    private String ruleId;
    /**
     * 选票规则名称
     */
    private String ruleName;
    /**
     * 明细组号
     */
    private String dtlGroupCode;
    /**
     * 明细规则（组）关系：or或 and与
     */
    private String relation;
    /**
     * 顺序
     */
    private Integer serialNo;
    /**
     * 申请单号
     */
    private String applyNo;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    @Column(name = "APPLY_NO")
    public String getApplyNo() {
        return applyNo;
    }
    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }
    @Column(name = "RULE_GROUP_CODE", nullable = true)
    public String getRuleGroupCode() {
        return ruleGroupCode;
    }
    public void setRuleGroupCode(String ruleGroupCode) {
        this.ruleGroupCode = ruleGroupCode;
    }
    @Column(name = "RULE_ID", nullable = true)
    public String getRuleId() {
        return ruleId;
    }
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
    @Column(name = "RULE_NAME", nullable = true)
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    @Column(name = "DTL_GROUP_CODE", nullable = true)
    public String getDtlGroupCode() {
        return dtlGroupCode;
    }
    public void setDtlGroupCode(String dtlGroupCode) {
        this.dtlGroupCode = dtlGroupCode;
    }
    @Column(name = "RELATION", nullable = true)
    public String getRelation() {
        return relation;
    }
    public void setRelation(String relation) {
        this.relation = relation;
    }
    @Column(name = "SERIALNO", nullable = true)
    public Integer getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }
    @Column(name = "CREATE_NAME", nullable = true)
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    @Column(name = "CREATE_BY", nullable = true)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_NAME", nullable = true)
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    @Column(name = "UPDATE_BY", nullable = true)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Override
    public String toString() {
        return "AbIntellectRuleGroupDtl{" +
                "ruleGroupId='" + ruleGroupCode + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", dtlGroupCode='" + dtlGroupCode + '\'' +
                ", relation='" + relation + '\'' +
                ", serialNo=" + serialNo +
                ", createName='" + createName + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateName='" + updateName + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
