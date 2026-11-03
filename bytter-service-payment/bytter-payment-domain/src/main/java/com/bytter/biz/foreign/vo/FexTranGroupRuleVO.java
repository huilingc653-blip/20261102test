package com.bytter.biz.foreign.vo;

import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qinl
 * @version 1.0
 * @description: 外汇交易组规则
 * @date 2020-02-18
 */
public class FexTranGroupRuleVO implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 3650731181127660208L;
    private String id;
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

    /**
     * 规则详细
     */
    private List<FexTranGroupRuleDtlVO> fexTranGroupRuleDtlVOList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
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

    public List<FexTranGroupRuleDtlVO> getFexTranGroupRuleDtlVOList() {
        return fexTranGroupRuleDtlVOList;
    }

    public void setFexTranGroupRuleDtlVOList(List<FexTranGroupRuleDtlVO> fexTranGroupRuleDtlVOList) {
        this.fexTranGroupRuleDtlVOList = fexTranGroupRuleDtlVOList;
    }
}
