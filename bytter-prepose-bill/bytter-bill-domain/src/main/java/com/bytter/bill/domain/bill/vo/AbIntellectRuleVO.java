package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 智能选票-选票规则组VO
 * @date 2022-10-11
 */
@SuppressWarnings("serial")
public class AbIntellectRuleVO implements java.io.Serializable {
    /**
     * ID
     */
    private String id;
    /**
     * 规则编码
     */
    @BtCompositeSearch(fieldName = "rule_code")
    private String ruleCode;
    /**
     * 规则名称
     */
    @BtCompositeSearch(fieldName = "rule_name")
    private String ruleName;
    /**
     * 有效标识 0无效 1有效
     */
    @BtCompositeSearch(fieldName = "valid_sign")
    private String validSign;
    /**
     * 筛选数量
     */
    private String screenNum;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "rmk")
    private String rmk;
    /**
     * 申请单号
     */
    private String applyNo;
    /**
     * 创建人名称
     */
    private String createBy;
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
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
    public String getRuleCode() {
        return ruleCode;
    }
    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Override
    public String toString() {
        return "AbIntellectRuleVO{" +
                "id='" + id + '\'' +
                ", ruleCode='" + ruleCode + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", validSign='" + validSign + '\'' +
                ", screenNum='" + screenNum + '\'' +
                ", rmk='" + rmk + '\'' +
                '}';
    }
}
