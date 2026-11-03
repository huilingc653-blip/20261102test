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
public class GuaContractCmsProvideLoanVO implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 借款单号
     */
    private String billCode;
    /**
     * 借款银行
     */
    private String bankName;
    /**
     * 借款合同号
     */
    private String contractNo;
    /**
     * 币种
     */
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 借款金额
     */
    private BigDecimal plMoney;
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
    private String CorpId;
    private String CorpName;
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getPlMoney() {
        return plMoney;
    }
    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
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
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCorpId() {
        return CorpId;
    }
    public void setCorpId(String corpId) {
        CorpId = corpId;
    }
    public String getCorpName() {
        return CorpName;
    }
    public void setCorpName(String corpName) {
        CorpName = corpName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
