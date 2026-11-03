package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 智能选票-选票规则组VO
 * @date 2022-10-11
 */
@SuppressWarnings("serial")
public class AbIntellectRuleGroupVO implements java.io.Serializable {
    /**
     * ID
     */
    private String id;
    /**
     * 规则组编码
     */
    @BtCompositeSearch(fieldName = "r.rule_group_code")
    private String ruleGroupCode;
    /**
     * 规则组名称
     */
    @BtCompositeSearch(fieldName = "r.rule_group_name")
    private String ruleGroupName;
    /**
     * 有效标识 0无效 1有效
     */
    @BtCompositeSearch(fieldName = "r.valid_sign")
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
     * 备注
     */
    @BtCompositeSearch(fieldName = "r.rmk")
    private String rmk;
    /**
     * 申请单号
     */
    private String applyNo;
    public String getApplyNo() {
        return applyNo;
    }
    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
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
    public String getRuleGroupName() {
        return ruleGroupName;
    }
    public void setRuleGroupName(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getScreenNum() {
        return screenNum;
    }
    public void setScreenNum(String screenNum) {
        this.screenNum = screenNum;
    }
    public String getRelation() {
        return relation;
    }
    public void setRelation(String relation) {
        this.relation = relation;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Override
    public String toString() {
        return "AbIntellectRuleGroupVO{" +
                "id='" + id + '\'' +
                ", ruleGroupCode='" + ruleGroupCode + '\'' +
                ", ruleGroupName='" + ruleGroupName + '\'' +
                ", validSign='" + validSign + '\'' +
                ", screenNum='" + screenNum + '\'' +
                ", relation='" + relation + '\'' +
                ", rmk='" + rmk + '\'' +
                '}';
    }
}
