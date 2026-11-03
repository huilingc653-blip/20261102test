package com.bytter.biz.budget.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 资金计划执行规则表
 * @author onlineGenerator
 * @date 2020-08-31 14:51:48
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_PROCESS_RULE", schema = "")
@SuppressWarnings("serial")
public class FbsProcessRule extends IdEntity implements java.io.Serializable {
    /**执行规则*/
    private String processRule;
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
    @Column(name ="PROCESS_RULE")
    public String getProcessRule() {
        return processRule;
    }
    public void setProcessRule(String processRule) {
        this.processRule = processRule;
    }
    @Column(name ="SORT")
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @Column(name ="PROCESS_RATIO")
    public BigDecimal getProcessRatio() {
        return processRatio;
    }
    public void setProcessRatio(BigDecimal processRatio) {
        this.processRatio = processRatio;
    }
    @Column(name ="VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
