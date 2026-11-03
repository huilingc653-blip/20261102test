package com.bytter.biz.netbank.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

public class BisBankExecutionStrategyVO implements Serializable {
    /**id*/
    @ApiModelProperty("id")
    private String id;

    /**银行类型*/
    @BtCompositeSearch(fieldName = "b.BANK_TYPE_ID")
    @ApiModelProperty("银行类型")
    private String  bankTypeId;

    /**银行类型名称*/
    @BtCompositeSearch(fieldName = "b.BANK_TYPE_ID")
    @ApiModelProperty("银行类型名称")
    private String  bankTypeName;

    /**任务类型*/
    @BtCompositeSearch(fieldName = "b.type")
    @Dict(dictGroup = "bank.strategy", inject = "typeName")
    @ApiModelProperty("任务类型")
    private String  type;

    @BtCompositeSearch(fieldName = "b.type")
    @ApiModelProperty("任务类型名称")
    private String  typeName;

    /**启用标志 0，启用，1，禁用*/
    @BtCompositeSearch(fieldName = "b.is_enable")
    @Dict(dictGroup = "fcCorpPool.status", inject = "isEnableName")
    @ApiModelProperty("启用标志 0，启用，1，禁用")
    private String  isEnable;

    @ApiModelProperty("启用标志名称")
    private String  isEnableName;

    /**是否存在唯一标识 0，不存在，1存在*/
    @BtCompositeSearch(fieldName = "b.IS_EXIST_ID")
    @ApiModelProperty("是否存在唯一标识 0，不存在，1存在")
    private String  isExistId;

    /**执行规则*/
    @BtCompositeSearch(fieldName = "b.RULES")
    @ApiModelProperty("执行规则")
    private String  rules;

    /**执行规则名称*/
    @BtCompositeSearch(fieldName = "b.RULES_NAME")
    @ApiModelProperty("执行规则名称")
    private String  rulesName;

    /**付款单唯一标识*/
    @ApiModelProperty("付款单唯一标识")
    private String  billOnlyId;

    /**银行流水唯一标识*/
    @ApiModelProperty("银行流水唯一标识")
    private String  dtlOnlyId;

    /**电子回单唯一标识*/
    @ApiModelProperty("电子回单唯一标识")
    private String  receiptOnlyId;

    /**优先执行排序*/
    @BtCompositeSearch(fieldName = "b.EXECUTE_SORT")
    @ApiModelProperty("优先执行排序")
    private String  executeSort;

    /**创建人登录名称*/
    @ApiModelProperty("创建人登录名称")
    private String createBy;

    /**创建日期*/
    @ApiModelProperty("创建日期")
    private Date createDate;

    /**更新人登录名称*/
    @ApiModelProperty("更新人登录名称")
    private String updateBy;

    /**更新日期*/
    @ApiModelProperty("更新日期")
    private Date updateDate;

    /**交易日期-相差天数*/
    @ApiModelProperty(value = "交易日期-相差天数", example = "0")
    private Integer transDays;

    @ApiModelProperty("银行类型ID数组")
    private String[] bankTypeIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getIsExistId() {
        return isExistId;
    }

    public void setIsExistId(String isExistId) {
        this.isExistId = isExistId;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }

    public String getBillOnlyId() {
        return billOnlyId;
    }

    public void setBillOnlyId(String billOnlyId) {
        this.billOnlyId = billOnlyId;
    }

    public String getDtlOnlyId() {
        return dtlOnlyId;
    }

    public void setDtlOnlyId(String dtlOnlyId) {
        this.dtlOnlyId = dtlOnlyId;
    }

    public String getReceiptOnlyId() {
        return receiptOnlyId;
    }

    public void setReceiptOnlyId(String receiptOnlyId) {
        this.receiptOnlyId = receiptOnlyId;
    }

    public String getExecuteSort() {
        return executeSort;
    }

    public void setExecuteSort(String executeSort) {
        this.executeSort = executeSort;
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

    public Integer getTransDays() {
        return transDays;
    }

    public void setTransDays(Integer transDays) {
        this.transDays = transDays;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIsEnableName() {
        return isEnableName;
    }

    public void setIsEnableName(String isEnableName) {
        this.isEnableName = isEnableName;
    }

    public String[] getBankTypeIds() {
        return bankTypeIds;
    }

    public void setBankTypeIds(String[] bankTypeIds) {
        this.bankTypeIds = bankTypeIds;
    }


}
