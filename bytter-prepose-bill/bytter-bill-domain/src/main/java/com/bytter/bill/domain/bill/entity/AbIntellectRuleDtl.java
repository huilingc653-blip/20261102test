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
 * @Description: 智能选票-选票规则明细
 * @date 2022-10-10
 */
@Entity
@Table(name = "AB_INTELLECT_RULE_DTL", schema = "")
@SuppressWarnings("serial")
public class AbIntellectRuleDtl extends IdEntity implements java.io.Serializable {
    /**
     * 规则编码
     */
    private String ruleCode;
    /**
     * 选票要素id
     */
    private String elementId;
    /**
     * 选票要素名称
     */
    private String elementName;
    /**
     * 上级选票要素id
     */
    private String parentId;
    /**
     * 上级选票要素名称
     */
    private String parentName;
    /**
     * 筛选类型：sort排序 scope范围 val值
     */
    private String screenType;
    /**
     * 排序方式：asc升序 desc降序
     */
    private String sortOrder;
    /**
     * 范围方式：series系列 valSco介于值之间
     */
    private String scopeMode;
    /**
     * 值
     */
    private String value;
    /**
     * 起始值
     */
    private String startValue;
    /**
     * 结束值
     */
    private String endValue;
    /**
     * 起始值关系
     */
    private String startRelation;
    /**
     * 结束值关系
     */
    private String endRelation;
    /**
     * 关系
     */
    private String relation;
    /**
     * 顺序
     */
    private Integer serialNo;
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
    /**
     * 申请单号
     */
    private String applyNo;
    /**
     * 值是否为选票要素 0否 1是
     */
    private String valueIsElement;
    @Column(name = "APPLY_NO")
    public String getApplyNo() {
        return applyNo;
    }
    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }
    @Column(name = "VALUE_IS_ELEMENT")
    public String getValueIsElement() {
        return valueIsElement;
    }
    public void setValueIsElement(String valueIsElement) {
        this.valueIsElement = valueIsElement;
    }
    @Column(name = "RULE_CODE", nullable = true)
    public String getRuleCode() {
        return ruleCode;
    }
    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }
    @Column(name = "ELEMENT_ID", nullable = true)
    public String getElementId() {
        return elementId;
    }
    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
    @Column(name = "ELEMENT_NAME", nullable = true)
    public String getElementName() {
        return elementName;
    }
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
    @Column(name = "PARENT_ID", nullable = true)
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Column(name = "PARENT_NAME", nullable = true)
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    @Column(name = "SCREEN_TYPE", nullable = true)
    public String getScreenType() {
        return screenType;
    }
    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }
    @Column(name = "SORT_ORDER", nullable = true)
    public String getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
    @Column(name = "SCOPE_MODE", nullable = true)
    public String getScopeMode() {
        return scopeMode;
    }
    public void setScopeMode(String scopeMode) {
        this.scopeMode = scopeMode;
    }
    @Column(name = "VALUE", nullable = true)
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @Column(name = "START_VALUE", nullable = true)
    public String getStartValue() {
        return startValue;
    }
    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }
    @Column(name = "END_VALUE", nullable = true)
    public String getEndValue() {
        return endValue;
    }
    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }
    @Column(name = "START_RELATION", nullable = true)
    public String getStartRelation() {
        return startRelation;
    }
    public void setStartRelation(String startRelation) {
        this.startRelation = startRelation;
    }
    @Column(name = "END_RELATION", nullable = true)
    public String getEndRelation() {
        return endRelation;
    }
    public void setEndRelation(String endRelation) {
        this.endRelation = endRelation;
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
}
