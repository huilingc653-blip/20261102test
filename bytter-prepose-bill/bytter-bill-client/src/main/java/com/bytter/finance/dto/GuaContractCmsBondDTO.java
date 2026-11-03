package com.bytter.finance.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 债券类型
 *
 * @author ouyj
 */
public class GuaContractCmsBondDTO implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 发行编号
     */
    private String bondIssueNum;
    /**
     * 原币币种
     */
//    @Currency(inject = "oriCurName")
    private String oriCurId;
    private String oriCurName;
    /**
     * 原币金额
     */
    @BtCompositeSearch(fieldName = "info.issue_money")
    private BigDecimal oriMoney;
    /**
     * 人民币金额
     */
    private BigDecimal rmbMoney;
    /**
     * 发行开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueStartDate;
    /**
     * 发行结束日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueEndDate;
    /**
     * 使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
//    @BtCompositeSearch(fieldName = "gco.use_date")
    private Date useDate;
    /**
     * 状态
     */
//    @BtCompositeSearch(fieldName = "gco.status")
    @Dict(dictGroup = "Contract.UseStatus", inject = "statusName")
    private Integer status;
    private String statusName;
    /**
     * 担保合同ID
     */
//    @BtCompositeSearch(fieldName = "gco.contract_id")
    private String contractId;
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBondIssueNum() {
        return bondIssueNum;
    }
    public void setBondIssueNum(String bondIssueNum) {
        this.bondIssueNum = bondIssueNum;
    }
    public String getOriCurId() {
        return oriCurId;
    }
    public void setOriCurId(String oriCurId) {
        this.oriCurId = oriCurId;
    }
    public String getOriCurName() {
        return oriCurName;
    }
    public void setOriCurName(String oriCurName) {
        this.oriCurName = oriCurName;
    }
    public BigDecimal getOriMoney() {
        return oriMoney;
    }
    public void setOriMoney(BigDecimal oriMoney) {
        this.oriMoney = oriMoney;
    }
    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }
    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }
    public Date getIssueStartDate() {
        return issueStartDate;
    }
    public void setIssueStartDate(Date issueStartDate) {
        this.issueStartDate = issueStartDate;
    }
    public Date getIssueEndDate() {
        return issueEndDate;
    }
    public void setIssueEndDate(Date issueEndDate) {
        this.issueEndDate = issueEndDate;
    }
    public Date getUseDate() {
        return useDate;
    }
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
