package com.bytter.finance.domain.provide.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProvideLoanDeferredVO implements Serializable, IBusinessKey {
    /**
     * 借款发放Id
     **/
    private String id;
    /**
     * 展期Id
     **/
    @BtCompositeSearch(fieldName = "d.id")
    private String deferredId;
    /**
     * 展期单号
     **/
    @BtCompositeSearch(fieldName = "d.bill_code")
    private String deferredCode;
    /**
     * 业务状态
     */
    private String bizStatus;
    /**
     * 借款编号
     **/
    @BtCompositeSearch(fieldName = "i.bill_code")
    private String billCode;
    /**
     * 展期合同号
     */
    @BtCompositeSearch(fieldName = "d.contract_code")
    private String contractCode;
    /**
     * 借款发放编号
     */
    @BtCompositeSearch(fieldName = "s.contract_code")
    private String sendContractCode;
    /**
     * 单位代码
     */
    @BtCompositeSearch(fieldName = "i.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 借款银行
     */
    @BtCompositeSearch(fieldName = "i.bank_id")
    private String bankId;
    private String bankName;
    /**
     * 发放单号
     **/
    @BtCompositeSearch(fieldName = "s.bill_code")
    private String provideCode;
    /**
     * 借款信息编码
     */
    @BtCompositeSearch(fieldName = "s.loan_code")
    private String loanCode;
    /**
     * 借款账号
     */
    @BtCompositeSearch(fieldName = "s.loan_acc")
    private String loanAcc;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "s.create_date")
    private Date createDate;
    /**
     * 借款币种
     */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "i.cur_id")
    private String curId;
    private String curName;
    /**
     * 发放金额
     */
    @BtCompositeSearch(fieldName = "s.money")
    private BigDecimal money;
    /**
     * 借款金额
     **/
    @BtCompositeSearch(fieldName = "i.pl_money")
    private BigDecimal plMoney;
    /**
     * 还款金额
     **/
    @BtCompositeSearch(fieldName = "r.repay_money")
    private BigDecimal repayMoney;
    /**
     * 发放日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "s.send_date")
    private Date sendDate;
    /**
     * 还款日期
     */
    @BtCompositeSearch(fieldName = "i.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /**
     * 还款方式
     */
    @BtCompositeSearch(fieldName = "i.repay_type")
    @Dict(dictGroup = "Provide.RepayType", inject = "repayTypeName")
    private String repayType;
    private String repayTypeName;
    /**
     * 新还款方式
     */
    private String repayTypeName1;
    /**
     * 借款种类
     */
    @BtCompositeSearch(fieldName = "i.pl_class")
    private String plClass;
    private String plClassName;
    /**
     * 担保方式
     */
    @BtCompositeSearch(fieldName = "i.pl_type")
    private String plType;
    /**
     * 借款期限
     */
    @BtCompositeSearch(fieldName = "i.pl_term")
    private BigDecimal plTerm;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "s.stauts")
    private Integer status;
    /**
     * 借款银行账号
     */
    @BtCompositeSearch(fieldName = "i.pl_account")
    private String plAccount;
    /**
     * 中间业务费
     */
    @BtCompositeSearch(fieldName = "i.interbusexpenses")
    private BigDecimal interbusexpenses;
    /**
     * 展期到期日
     */
    @BtCompositeSearch(fieldName = "d.deferred_repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredRepayDate;
    /**
     * 展期到期日 页面初始值
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredRepayDateInit;
    /**
     * 展期期限
     */
    @BtCompositeSearch(fieldName = "d.deferred_limt")
    private Integer deferredLimt;
    /**
     * 期限单位
     */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "unit")
    private String ternUnit;
    private String unit;
    /**
     * 展期利率
     */
    @BtCompositeSearch(fieldName = "d.deferred_rate")
    private BigDecimal deferredRate;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "d.rmk")
    private String rmk;
    /**
     * 打回原因
     */
    private String refuseWhy;
    /**
     * 展期状态
     **/
    @BtCompositeSearch(fieldName = "d.status")
    private Integer provideStatus;
    /**
     * 网点
     **/
//    @Corp(inject = "netName")
    @BtCompositeSearch(fieldName = "i.net_id")
    private String netId;
    private String netName;
    /**
     * 借款日期
     */
    @BtCompositeSearch(fieldName = "i.pl_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plDate;
    /**
     * 经办人
     **/
    @BtCompositeSearch(fieldName = "d.create_name")
    private String createName;
    /**
     * 经办日期
     */
    @BtCompositeSearch(fieldName = "d.create_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date agencyDate;
    /**
     * 借款余额
     **/
    private BigDecimal balance;
    /**
     * 还款在途金额
     **/
    private BigDecimal passageMoney;
    /**
     * 借款合同号
     **/
    private String contractNo;
    /**
     * 借款创建日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date infoCreateDate;
    /**
     * 借款信息状态
     **/
    private Integer infoStatus;
    /**
     * 贷款到期日
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date matureDate;
    /**
     * 贷款发放单号
     **/
    private String liCode;
    /**
     * 展期次数
     **/
    private Integer deferredNum;
    /**
     * 展期开始日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredStartDate;
    /**
     * 展期上浮点数
     */
    private BigDecimal deferredPoints;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getSendContractCode() {
        return sendContractCode;
    }

    public void setSendContractCode(String sendContractCode) {
        this.sendContractCode = sendContractCode;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getProvideCode() {
        return provideCode;
    }

    public void setProvideCode1(String provideCode) {
        this.provideCode = provideCode;
    }

    public String getLoanCode() {
        return loanCode;
    }

    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getRepayTypeName() {
        return StringUtils.isNotEmpty(repayTypeName) ? repayTypeName : repayTypeName1;
    }

    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }

    public String getRepayTypeName1() {
        return repayTypeName1;
    }

    public void setRepayTypeName1(String repayTypeName1) {
        this.repayTypeName1 = repayTypeName1;
    }

    public String getPlClass() {
        return plClass;
    }

    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }

    public String getPlType() {
        return plType;
    }

    public void setPlType(String plType) {
        this.plType = plType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getPlAccount() {
        return plAccount;
    }

    public void setPlAccount(String plAccount) {
        this.plAccount = plAccount;
    }

    public BigDecimal getInterbusexpenses() {
        return interbusexpenses;
    }

    public void setInterbusexpenses(BigDecimal interbusexpenses) {
        this.interbusexpenses = interbusexpenses;
    }

    public Date getDeferredRepayDate() {
        return deferredRepayDate;
    }

    public void setDeferredRepayDate(Date deferredRepayDate) {
        this.deferredRepayDate = deferredRepayDate;
    }

    public Date getDeferredRepayDateInit() {
        return deferredRepayDateInit;
    }

    public void setDeferredRepayDateInit(Date deferredRepayDateInit) {
        this.deferredRepayDateInit = deferredRepayDateInit;
    }

    public Integer getDeferredLimt() {
        return deferredLimt;
    }

    public void setDeferredLimt(Integer deferredLimt) {
        this.deferredLimt = deferredLimt;
    }

    public BigDecimal getDeferredRate() {
        return deferredRate;
    }

    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }

    public String getRefuseWhy() {
        return refuseWhy;
    }

    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getLoanAcc() {
        return loanAcc;
    }

    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPlMoney() {
        return plMoney;
    }

    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
    }

    public String getDeferredCode() {
        return deferredCode;
    }

    public void setDeferredCode(String deferredCode) {
        this.deferredCode = deferredCode;
    }

    public String getTernUnit() {
        return ternUnit;
    }

    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }

    public String getDeferredId() {
        return deferredId;
    }

    public void setDeferredId(String deferredId) {
        this.deferredId = deferredId;
    }

    public void setProvideCode(String provideCode) {
        this.provideCode = provideCode;
    }

    public Integer getProvideStatus() {
        return provideStatus;
    }

    public void setProvideStatus(Integer provideStatus) {
        this.provideStatus = provideStatus;
    }

    public Date getPlDate() {
        return plDate;
    }

    public void setPlDate(Date plDate) {
        this.plDate = plDate;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getAgencyDate() {
        return agencyDate;
    }

    public void setAgencyDate(Date agencyDate) {
        this.agencyDate = agencyDate;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPassageMoney() {
        return passageMoney;
    }

    public void setPassageMoney(BigDecimal passageMoney) {
        this.passageMoney = passageMoney;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
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

    public Date getInfoCreateDate() {
        return infoCreateDate;
    }

    public void setInfoCreateDate(Date infoCreateDate) {
        this.infoCreateDate = infoCreateDate;
    }

    public Integer getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(Integer infoStatus) {
        this.infoStatus = infoStatus;
    }

    public String getBusinessKey() {
        // TODO Auto-generated method stub
        return id;
    }

    public BigDecimal getPlTerm() {
        return plTerm;
    }

    public void setPlTerm(BigDecimal plTerm) {
        this.plTerm = plTerm;
    }

    public String getLiCode() {
        return liCode;
    }

    public void setLiCode(String liCode) {
        this.liCode = liCode;
    }

    public Date getMatureDate() {
        return matureDate;
    }

    public void setMatureDate(Date matureDate) {
        this.matureDate = matureDate;
    }

    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public Integer getDeferredNum() {
        return deferredNum;
    }

    public void setDeferredNum(Integer deferredNum) {
        this.deferredNum = deferredNum;
    }

    public Date getDeferredStartDate() {
        return deferredStartDate;
    }

    public void setDeferredStartDate(Date deferredStartDate) {
        this.deferredStartDate = deferredStartDate;
    }

    public String getPlClassName() {
        return plClassName;
    }

    public void setPlClassName(String plClassName) {
        this.plClassName = plClassName;
    }

    public BigDecimal getDeferredPoints() {
        return deferredPoints;
    }

    public void setDeferredPoints(BigDecimal deferredPoints) {
        this.deferredPoints = deferredPoints;
    }
}
