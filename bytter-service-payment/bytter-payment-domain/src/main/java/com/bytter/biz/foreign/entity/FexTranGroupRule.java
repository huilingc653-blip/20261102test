package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author qinl
 * @version 1.0
 * @description: 外汇交易组规则
 * @date 2020-02-18
 */
@Entity
@Table(name = "FEX_TRAN_GROUP_RULE", schema = "")
public class FexTranGroupRule extends IdEntity implements Serializable {
    /**
     * 名称
     */
    private String ruleName;
    /**
     * 描述
     */
    private String ruleDesc;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     */
    private String validSign;
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

    @Column(name = "RULE_NAME", nullable = true, length = 60)
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Column(name = "RULE_DESC", nullable = true, length = 100)
    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    @Column(name = "RMK", nullable = true, length = 100)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Column(name = "CREATE_BY", nullable = true, length = 50)
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

    @Column(name = "UPDATE_BY", nullable = true, length = 50)
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
