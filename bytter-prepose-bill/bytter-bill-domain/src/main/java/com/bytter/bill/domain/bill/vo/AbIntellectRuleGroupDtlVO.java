package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.util.Date;
/**
 * 智能选票-选票规则组明细VO
 *
 * @author ouyj
 */
public class AbIntellectRuleGroupDtlVO implements java.io.Serializable {
    /**
     * id
     */
    private String id;
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
    @BtCompositeSearch(fieldName = "r.serialNo")
    private Integer serialNo;
    /**
     * 申请单号
     */
    @BtCompositeSearch(fieldName = "r.apply_no")
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
    /**
     * 上移true，下移false标志
     */
    private Boolean moveFlag;
    /**
     * 选票规则备注
     */
    private String ruleRmk;
    /**
     * 批量保存的规则name
     */
    private String ruleNames;
    /**
     * 批量保存的规则id
     */
    private String ruleIds;
    public String getRuleNames() {
        return ruleNames;
    }
    public void setRuleNames(String ruleNames) {
        this.ruleNames = ruleNames;
    }
    public String getRuleIds() {
        return ruleIds;
    }
    public void setRuleIds(String ruleIds) {
        this.ruleIds = ruleIds;
    }
    public String getRuleRmk() {
        return ruleRmk;
    }
    public void setRuleRmk(String ruleRmk) {
        this.ruleRmk = ruleRmk;
    }
    public Boolean getMoveFlag() {
        return moveFlag;
    }
    public void setMoveFlag(Boolean moveFlag) {
        this.moveFlag = moveFlag;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRuleGroupCode() {
        return ruleGroupCode;
    }
    public void setRuleGroupCode(String ruleGroupCode) {
        this.ruleGroupCode = ruleGroupCode;
    }
    public String getRuleId() {
        return ruleId;
    }
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    public String getDtlGroupCode() {
        return dtlGroupCode;
    }
    public void setDtlGroupCode(String dtlGroupCode) {
        this.dtlGroupCode = dtlGroupCode;
    }
    public String getRelation() {
        return relation;
    }
    public void setRelation(String relation) {
        this.relation = relation;
    }
    public Integer getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }
    public String getApplyNo() {
        return applyNo;
    }
    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
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
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
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
    @Override
    public String toString() {
        return "AbIntellectRuleGroupDtlVO{" +
                "ruleGroupCode='" + ruleGroupCode + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", dtlGroupCode='" + dtlGroupCode + '\'' +
                ", relation='" + relation + '\'' +
                ", serialNo=" + serialNo +
                ", applyNo='" + applyNo + '\'' +
                ", createName='" + createName + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateName='" + updateName + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
