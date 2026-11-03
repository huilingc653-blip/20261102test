package com.bytter.biz.netbank.entity;
import com.bytter.core.base.entity.IdEntity;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
@Entity
@Table(name = "BIS_BANK_EXECUTION_STRATEGY", schema = "")
public class BisBankExecutionStrategy extends IdEntity implements java.io.Serializable{
    /**银行类型*/
    private String  bankTypeId;
    /**任务类型*/
    private String  type;
    /**启用标志 0，未启用，1，已启用*/
    private String  isEnable;
    /**是否存在唯一标识 0，不存在，1存在*/
    private String  isExistId;
    /**执行规则*/
    private String  rules;
    /**执行规则名称*/
    private String  rulesName;
    /**付款单唯一标识*/
    private String  billOnlyId;
    /**银行流水唯一标识*/
    private String  dtlOnlyId;
    /**电子回单唯一标识*/
    private String  receiptOnlyId;
    /**优先执行排序*/
    private String  executeSort;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    @Column(name ="BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    @Column(name ="TYPE")
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name ="IS_ENABLE")
    public String getIsEnable() {
        return isEnable;
    }
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    @Column(name ="IS_EXIST_ID")
    public String getIsExistId() {
        return isExistId;
    }
    public void setIsExistId(String isExistId) {
        this.isExistId = isExistId;
    }
    @Column(name ="RULES")
    public String getRules() {
        return rules;
    }
    public void setRules(String rules) {
        this.rules = rules;
    }
    @Column(name ="RULES_NAME")
    public String getRulesName() {
        return rulesName;
    }
    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }
    @Column(name ="BILL_ONLY_ID")
    public String getBillOnlyId() {
        return billOnlyId;
    }
    public void setBillOnlyId(String billOnlyId) {
        this.billOnlyId = billOnlyId;
    }
    @Column(name ="DTL_ONLY_ID")
    public String getDtlOnlyId() {
        return dtlOnlyId;
    }
    public void setDtlOnlyId(String dtlOnlyId) {
        this.dtlOnlyId = dtlOnlyId;
    }
    @Column(name ="RECEIPT_ONLY_ID")
    public String getReceiptOnlyId() {
        return receiptOnlyId;
    }
    public void setReceiptOnlyId(String receiptOnlyId) {
        this.receiptOnlyId = receiptOnlyId;
    }
    @Column(name ="EXECUTE_SORT")
    public String getExecuteSort() {
        return executeSort;
    }
    public void setExecuteSort(String executeSort) {
        this.executeSort = executeSort;
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

    private Integer transDays;

    @Column(name ="trans_days")
    public Integer getTransDays() {
        return transDays;
    }

    public void setTransDays(Integer transDays) {
        this.transDays = transDays;
    }

    private String[] matchRule;
    private String[] matchElements;
    private String[] matchRelations;
    private String[] matchContent;
    private String[] leftParenthesis;
    private String[] rightParenthesis;

    @Transient
    public String[] getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(String[] matchRule) {
        this.matchRule = matchRule;
    }

    @Transient
    public String[] getMatchElements() {
        return matchElements;
    }

    public void setMatchElements(String[] matchElements) {
        this.matchElements = matchElements;
    }
    @Transient
    public String[] getMatchRelations() {
        return matchRelations;
    }

    public void setMatchRelations(String[] matchRelations) {
        this.matchRelations = matchRelations;
    }

    @Transient
    public String[] getMatchContent() {
        return matchContent;
    }

    public void setMatchContent(String[] matchContent) {
        this.matchContent = matchContent;
    }

    @Transient
    public String[] getLeftParenthesis() {
        return leftParenthesis;
    }

    public void setLeftParenthesis(String[] leftParenthesis) {
        this.leftParenthesis = leftParenthesis;
    }

    @Transient
    public String[] getRightParenthesis() {
        return rightParenthesis;
    }

    public void setRightParenthesis(String[] rightParenthesis) {
        this.rightParenthesis = rightParenthesis;
    }

    private String opType;

    @Transient
    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }
}
