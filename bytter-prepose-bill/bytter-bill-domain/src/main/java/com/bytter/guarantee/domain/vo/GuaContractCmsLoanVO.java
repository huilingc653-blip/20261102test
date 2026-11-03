package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-07-22 15:15
 * @Version 1.0
 */
public class GuaContractCmsLoanVO implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 贷款流水号
     */
    private String billCode;
    /**
     * 贷款类型
     */
    private String loanGenre;
    /**
     * 贷款合同编号
     */
    private String contractCode;
    /**
     * 币种
     */
//    @Currency(inject = "curName")
    private String curCode;
    private String curName;
    /**
     * 贷款金额
     */
    private BigDecimal loanMoney;
    /**
     * 贷款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loanDate;
    /**
     * 还款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /**
     * 使用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "gco.use_date")
    private Date useDate;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "gco.status")
    @Dict(dictGroup = "Contract.UseStatus", inject = "statusName")
    private Integer status;
    private String statusName;
    /**
     * 系统代码
     */
    @BtCompositeSearch(fieldName = "gco.syscode")
    private String sysCode;
    /**
     * 担保合同ID
     */
    @BtCompositeSearch(fieldName = "gco.contract_id")
    private String contractId;
    /**
     * 单位id
     */
    private String corpId;
    private String corpName;
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getLoanGenre() {
        return loanGenre;
    }
    public void setLoanGenre(String loanGenre) {
        this.loanGenre = loanGenre;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public Date getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
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
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
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
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
