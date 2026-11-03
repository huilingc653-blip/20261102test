package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: VO
 * @Description: 资金计划执行规则表
 * @author huangx
 * @date 2020-08-31 14:51:48
 * @version 1.0
 *
 */
public class FbsProcessRuleVO implements  java.io.Serializable {
    /**ID*/
    private String id;
    /**执行规则*/
    @Dict(dictGroup = "plan.action.rule", inject = "processRuleName") // TODO 数据字典国际化问题，待解决
    @BtCompositeSearch(fieldName = "process_Rule")
    private String processRule;
    private String processRuleName;
    /**优先级*/
    private Integer sort;
    /**执行比例*/
    private BigDecimal processRatio;
    /**有效标志 0 无效 1 有效*/
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProcessRule() {
        return processRule;
    }
    public void setProcessRule(String processRule) {
        this.processRule = processRule;
    }
    public String getProcessRuleName() {
        return processRuleName;
    }
    public void setProcessRuleName(String processRuleName) {
        this.processRuleName = processRuleName;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public BigDecimal getProcessRatio() {
        return processRatio;
    }
    public void setProcessRatio(BigDecimal processRatio) {
        this.processRatio = processRatio;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
}
