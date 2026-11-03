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
 * @Description: 智能选票-选票规则组
 * @date 2022-10-10
 */
@Entity
@Table(name = "AB_INTELLECT_RULE_GROUP", schema = "")
@SuppressWarnings("serial")
public class AbIntellectRuleGroup extends IdEntity implements java.io.Serializable {
    /**
     * 规则组编码
     */
    private String ruleGroupCode;
    /**
     * 规则组名称
     */
    private String ruleGroupName;
    /**
     * 有效标识 0无效 1有效
     */
    private String validSign;
    /**
     * 筛选数量
     */
    private String screenNum;
    /**
     * 明细规则（组）关系：or或 and与
     */
    private String relation;
    /**
     * 申请单号
     */
    private String applyNo;
    /**
     * 备注
     */
    private String rmk;
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
    @Column(name = "RULE_GROUP_NAME", nullable = true)
    public String getRuleGroupName() {
        return ruleGroupName;
    }
    public void setRuleGroupName(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
    }
    @Column(name = "RELATION", nullable = true)
    public String getRelation() {
        return relation;
    }
    public void setRelation(String relation) {
        this.relation = relation;
    }
    @Column(name = "VALID_SIGN", nullable = true)
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "SCREEN_NUM", nullable = true)
    public String getScreenNum() {
        return screenNum;
    }
    public void setScreenNum(String screenNum) {
        this.screenNum = screenNum;
    }
    @Column(name = "RMK", nullable = true)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
        return "AbIntellectRuleGroup{" +
                "ruleGroupCode='" + ruleGroupCode + '\'' +
                ", ruleGroupName='" + ruleGroupName + '\'' +
                ", validSign='" + validSign + '\'' +
                ", screenNum='" + screenNum + '\'' +
                ", relation='" + relation + '\'' +
                ", rmk='" + rmk + '\'' +
                ", createName='" + createName + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateName='" + updateName + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
