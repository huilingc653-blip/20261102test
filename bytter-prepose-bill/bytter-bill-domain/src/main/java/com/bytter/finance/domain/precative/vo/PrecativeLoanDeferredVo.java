package com.bytter.finance.domain.precative.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
public class PrecativeLoanDeferredVo implements Serializable, IBusinessKey {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** 单据编号 **/
    @BtCompositeSearch(fieldName = "i.bill_code")
    private String voucherNo;
    /** 委托人单位代码 */
//    @Corp(inject = "precativeName")
    private String precativeId;
    @BtCompositeSearch(fieldName = "i.precative_Id")
    private String precativeName;
    /** 受托人单位代码 */
//    @Corp(inject = "corpName")
    private String corpId;
    @BtCompositeSearch(fieldName = "i.loan_opp")
    private String corpName;
    /** 贷款对象 */
//    @Corp(inject = "loanOppName")
    @BtCompositeSearch(fieldName = "i.loan_opp")
    private String loanOpp;
    @BtCompositeSearch(fieldName = "i.loan_Opp")
    private String loanOppName;
    /** 贷款币种 */
//    @Currency(inject = "curName")
    private String curId;
    @BtCompositeSearch(fieldName = "i.cur_id")
    private String curName;
    /** 贷款期限 */
    @BtCompositeSearch(fieldName = "i.term")
    private Integer term;
    /** 期限单位 */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "unit")
    private String ternUnit;
    private String unit;
    /** 贷款金额 */
    @BtCompositeSearch(fieldName = "i.money")
    private BigDecimal loanMoney;
    /** 委贷发放编号 */
    @BtCompositeSearch(fieldName = "s.bill_code")
    private String sendNo;
    /** 委贷发放合同编码 */
    private String contractCode;
    /** 发放金额 */
    @BtCompositeSearch(fieldName = "s.money")
    private BigDecimal money;
    /** 发放日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "s.send_date")
    private Date sendDate;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    @BtCompositeSearch(fieldName = "s.biz_Status")
    private String bizStatus;
    /** 到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "s.deadline_Date")
    private Date deadlineDate;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 还款金额 */
    @BtCompositeSearch(fieldName = "repay.repay_money")
    private BigDecimal repayMoney;
    /** 担保方式 **/
    @Dict(dictGroup = "Cms.PlType.C", inject = "plTypeName")
    private String plType;
    private String plTypeName;
    /** 展期单号 */
    @BtCompositeSearch(fieldName = "d.bill_code")
    private String billCode;
    /** 展期单号 */
    private String sendBillCode;
    /** 到期处理类型 **/
    @Dict(dictGroup = "provide.deferredHandleType", inject = "handleType")
    private String expiryHandleType;
    private String handleType;
    /** 展期到期日 */
    @BtCompositeSearch(fieldName = "d.mature_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date matureDate;
    /** 展期开始日期 **/
    @BtCompositeSearch(fieldName = "d.deferred_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredStartDate;
    /** 展期期限 */
    @BtCompositeSearch(fieldName = "d.deferred_Limt")
    private Integer deferredLimt;
    /** 展期id **/
    @BtCompositeSearch(fieldName = "d.id")
    private String id;
    /** 贷款发放单号 **/
    private String liCode;
    /** 贷款到期日 **/
    @BtCompositeSearch(fieldName = "d.deferred_repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredRepayDate;
    /** 委存账号 */
    private String depositAcc;
    /** 委贷账号 */
    private String loanAcc;
    /** 开始日期 */
    @BtCompositeSearch(fieldName = "i.start_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /** 结束日期 */
    @BtCompositeSearch(fieldName = "i.end_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 展期次数 **/
    private Integer deferredNum;
    /** 展期利率 **/
    private BigDecimal deferredRate;
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getPlTypeName() {
        return plTypeName;
    }
    public void setPlTypeName(String plTypeName) {
        this.plTypeName = plTypeName;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getPrecativeId() {
        return precativeId;
    }
    public void setPrecativeId(String precativeId) {
        this.precativeId = precativeId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getLoanOpp() {
        return loanOpp;
    }
    public void setLoanOpp(String loanOpp) {
        this.loanOpp = loanOpp;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public String getSendNo() {
        return sendNo;
    }
    public void setSendNo(String sendNo) {
        this.sendNo = sendNo;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public String getPlType() {
        return plType;
    }
    public void setPlType(String plType) {
        this.plType = plType;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getPrecativeName() {
        return precativeName;
    }
    public void setPrecativeName(String precativeName) {
        this.precativeName = precativeName;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getSendBillCode() {
        return sendBillCode;
    }
    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }
    public String getExpiryHandleType() {
        return expiryHandleType;
    }
    public void setExpiryHandleType(String expiryHandleType) {
        this.expiryHandleType = expiryHandleType;
    }
    public Date getDeferredRepayDate() {
        return deferredRepayDate;
    }
    public void setDeferredRepayDate(Date deferredRepayDate) {
        this.deferredRepayDate = deferredRepayDate;
    }
    public Date getDeferredStartDate() {
        return deferredStartDate;
    }
    public void setDeferredStartDate(Date deferredStartDate) {
        this.deferredStartDate = deferredStartDate;
    }
    public Integer getDeferredLimt() {
        return deferredLimt;
    }
    public void setDeferredLimt(Integer deferredLimt) {
        this.deferredLimt = deferredLimt;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getHandleType() {
        return handleType;
    }
    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }
    public String getBusinessKey() {
        return id;
    }
    public Date getMatureDate() {
        return matureDate;
    }
    public void setMatureDate(Date matureDate) {
        this.matureDate = matureDate;
    }
    public String getLiCode() {
        return liCode;
    }
    public void setLiCode(String liCode) {
        this.liCode = liCode;
    }
    public String getLoanOppName() {
        return loanOppName;
    }
    public void setLoanOppName(String loanOppName) {
        this.loanOppName = loanOppName;
    }
    public String getDepositAcc() {
        return depositAcc;
    }
    public void setDepositAcc(String depositAcc) {
        this.depositAcc = depositAcc;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
}
