package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Table
 * @Description: 理财产品登记表
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Entity
@Table(name = "CHO_PRODUCT_REQ",schema = "")
public class ChoProductReq extends IdEntity implements java.io.Serializable {
    /**业务单号*/
    private String billNo;
    /**单位ID*/
    private String corpId;
    private String corpName;
    /**银行类别*/
    private String bankTypeId;
    private String bankTypeName;
    /**产品类型*/
    private String productType;
    /**产品代码*/
    private String productCode;
    /**购买银行ID*/
    private String bankInfoId;
    /**购买银行*/
    private String bankName;
    /**理财账号ID*/
    private String bankAccId;
    /**理财账号*/
    private String bankAcc;
    /**币别*/
    private String curId;
    private String curName;
    /**金额*/
    private BigDecimal buyMoney;
    /**收益条款:360-360天;365-365天*/
    private Integer incomeClause;
    /**购买日期*/
    private Date buyDate;
    /**期限:期限=到期日期-起息日期,单位:天*/
    private Integer dayLimit;
    /**起息日期*/
    private Date interestStartDate;
    /**到期日期*/
    private Date deadlineDate;
    /**兑付日期*/
    private Date cashDate;
    /**参考收益率*/
    private BigDecimal referIncomeRate;
    /**申购费*/
    private BigDecimal subFeeMoney;
    /**参考收益=购买金额*参考收益率*期限;360（除以360还是365以收益条款为准）*/
    private BigDecimal referIncomeMoney;
    /**其他手续费*/
    private BigDecimal chargeMoney;
    /**是否可提前赎回 0-否 1-是*/
    private String isMoveUp;
    /**提前赎回条约*/
    private String moveUpRmk;
    /**备注*/
    private String rmk;
    /**附件ID*/
    private String attachmentId;
    /**状态*/
    private Integer status;
    /**最新一条赎回记录的状态*/
    private Integer redeemStatus;
    /**创建人登录姓名*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**消息提醒标志*/
    private String noticeSign;
    /**资金来源:1 自由资金 2 募集资金 3 银行贷款*/
    private Integer capitalSource;
    /**是否保本 0-否 1-是*/
    private String isGuaranteed;
    /**
     * 付息方式
     */
    private String payWay;
    /**
     * 合同号
     */
    private String contractNo;
    /**
     * 收益类型
     */
    private String incomeType;
    /**
     * 到期是否续存
     */
    private String isStore;
    /**
     * 起始合同利率
     */
    private BigDecimal startContractRate;
    /**
     * 结束合同利率
     */
    private BigDecimal endContractRate;
    /**
     * 业务状态
     */
    private Integer bizStatus;
    /**
     * 日净值
     */
    private BigDecimal dailyNetVal;
    /**
     * 份额
     */
    private BigDecimal portion;
    /**
     * 是否维护日净值
     */
    private String isMaintainVal;
    /**
     * 结束参考收益金额
     */
    private BigDecimal endReferIncomeMoney;
    /**
     * 结束参考收益率
     */
    private BigDecimal endReferIncomeRate;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 理财类型
     */
    private String financialType;
    /**
     * 是否为批量提交
     */
    private Boolean batchSubmit;
    /**
     * 上一次产品代码
     */
    private String lastProductCode;
    /**
     * 是否为购买申请
     */
    private String isBuyApply;
    /**
     * 购买申请状态
     */
    private Integer buyApplyStatus;
    /**续存模式 1-本金续存 2-本息续存*/
    private Integer certificateWay;
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    @Column(name = "IS_BUY_APPLY")
    public String getIsBuyApply() {
        return isBuyApply;
    }
    public void setIsBuyApply(String isBuyApply) {
        this.isBuyApply = isBuyApply;
    }
    @Column(name = "BUY_APPLY_STATUS")
    public Integer getBuyApplyStatus() {
        return buyApplyStatus;
    }
    public void setBuyApplyStatus(Integer buyApplyStatus) {
        this.buyApplyStatus = buyApplyStatus;
    }
    @Transient
    public String getLastProductCode() {
        return lastProductCode;
    }
    public void setLastProductCode(String lastProductCode) {
        this.lastProductCode = lastProductCode;
    }
    @Transient
    public Boolean getBatchSubmit() {
        return batchSubmit;
    }
    public void setBatchSubmit(Boolean batchSubmit) {
        this.batchSubmit = batchSubmit;
    }
    @Transient
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    @Transient
    public String getFinancialType() {
        return financialType;
    }
    public void setFinancialType(String financialType) {
        this.financialType = financialType;
    }
    @Column(name = "IS_MAINTAIN_VAL")
    public String getIsMaintainVal() {
        return isMaintainVal;
    }
    public void setIsMaintainVal(String isMaintainVal) {
        this.isMaintainVal = isMaintainVal;
    }
    @Column(name = "END_REFER_INCOME_MONEY")
    public BigDecimal getEndReferIncomeMoney() {
        return endReferIncomeMoney;
    }
    public void setEndReferIncomeMoney(BigDecimal endReferIncomeMoney) {
        this.endReferIncomeMoney = endReferIncomeMoney;
    }
    @Column(name = "END_REFER_INCOME_RATE")
    public BigDecimal getEndReferIncomeRate() {
        return endReferIncomeRate;
    }
    public void setEndReferIncomeRate(BigDecimal endReferIncomeRate) {
        this.endReferIncomeRate = endReferIncomeRate;
    }
    @Column(name = "DAILY_NET_VAL")
    public BigDecimal getDailyNetVal() {
        return dailyNetVal;
    }
    public void setDailyNetVal(BigDecimal dailyNetVal) {
        this.dailyNetVal = dailyNetVal;
    }
    @Column(name = "PORTION")
    public BigDecimal getPortion() {
        return portion;
    }
    public void setPortion(BigDecimal portion) {
        this.portion = portion;
    }
    @Column(name = "BIZ_STATUS")
    public Integer getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }
    @Column(name = "PAY_WAY")
    public String getPayWay() {
        return payWay;
    }
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
    @Column(name = "CONTRACT_NO")
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    @Column(name = "INCOME_TYPE")
    public String getIncomeType() {
        return incomeType;
    }
    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }
    @Column(name = "IS_STORE")
    public String getIsStore() {
        return isStore;
    }
    public void setIsStore(String isStore) {
        this.isStore = isStore;
    }
    @Column(name = "START_CONTRACT_RATE")
    public BigDecimal getStartContractRate() {
        return startContractRate;
    }
    public void setStartContractRate(BigDecimal startContractRate) {
        this.startContractRate = startContractRate;
    }
    @Column(name = "END_CONTRACT_RATE")
    public BigDecimal getEndContractRate() {
        return endContractRate;
    }
    public void setEndContractRate(BigDecimal endContractRate) {
        this.endContractRate = endContractRate;
    }
    @Column(name ="NOTICE_SIGN",length=1)
    public String getNoticeSign() {
        return noticeSign;
    }
    public void setNoticeSign(String noticeSign) {
        this.noticeSign = noticeSign;
    }
    @Column(name ="BILL_NO",nullable=false,length=12)
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name = "CORP_ID",nullable = false,length = 32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "BANK_TYPE_ID",nullable = false,length = 32)
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    @Column(name = "PRODUCT_TYPE",nullable = false,length = 10)
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    @Column(name = "PRODUCT_CODE",nullable = false,length = 60)
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    @Column(name = "BANK_INFO_ID",length = 32)
    public String getBankInfoId() {
        return bankInfoId;
    }
    public void setBankInfoId(String bankInfoId) {
        this.bankInfoId = bankInfoId;
    }
    @Column(name = "BANK_NAME",length = 200)
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "BANK_ACC_ID",length = 32)
    public String getBankAccId() {
        return bankAccId;
    }
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }
    @Column(name = "BANK_ACC",length = 30)
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    @Column(name = "CUR_ID",length = 32)
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "BUY_MONEY",scale=2,length=15)
    public BigDecimal getBuyMoney() {
        return buyMoney;
    }
    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }
    @Column(name = "INCOME_CLAUSE",length = 4)
    public Integer getIncomeClause() {
        return incomeClause;
    }
    public void setIncomeClause(Integer incomeClause) {
        this.incomeClause = incomeClause;
    }
    @Column(name = "BUY_DATE")
    public Date getBuyDate() {
        return buyDate;
    }
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
    @Column(name = "DAY_LIMIT",length = 8)
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    @Column(name = "INTEREST_START_DATE")
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    @Column(name = "DEADLINE_DATE")
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    @Column(name = "CASH_DATE")
    public Date getCashDate() {
        return cashDate;
    }
    public void setCashDate(Date cashDate) {
        this.cashDate = cashDate;
    }
    @Column(name = "REFER_INCOME_RATE",scale = 6,length = 8)
    public BigDecimal getReferIncomeRate() {
        return referIncomeRate;
    }
    public void setReferIncomeRate(BigDecimal referIncomeRate) {
        this.referIncomeRate = referIncomeRate;
    }
    @Column(name = "SUB_FEE_MONEY",scale = 2,length = 15)
    public BigDecimal getSubFeeMoney() {
        return subFeeMoney;
    }
    public void setSubFeeMoney(BigDecimal subFeeMoney) {
        this.subFeeMoney = subFeeMoney;
    }
    @Column(name = "REFER_INCOME_MONEY",scale = 2,length = 15)
    public BigDecimal getReferIncomeMoney() {
        return referIncomeMoney;
    }
    public void setReferIncomeMoney(BigDecimal referIncomeMoney) {
        this.referIncomeMoney = referIncomeMoney;
    }
    @Column(name = "CHARGE_MONEY",scale = 2,length = 15)
    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }
    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }
    @Column(name = "IS_MOVE_UP",length = 1)
    public String getIsMoveUp() {
        return isMoveUp;
    }
    public void setIsMoveUp(String isMoveUp) {
        this.isMoveUp = isMoveUp;
    }
    @Column(name = "MOVE_UP_RMK",length = 200)
    public String getMoveUpRmk() {
        return moveUpRmk;
    }
    public void setMoveUpRmk(String moveUpRmk) {
        this.moveUpRmk = moveUpRmk;
    }
    @Column(name = "RMK",length = 200)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "ATTACHMENT_ID",length = 32)
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "STATUS",length = 10)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "REDEEM_STATUS",length = 10)
    public Integer getRedeemStatus() {
        return redeemStatus;
    }
    public void setRedeemStatus(Integer redeemStatus) {
        this.redeemStatus = redeemStatus;
    }
    @Column(name = "CREATE_BY",length = 32)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY",length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "CAPITAL_SOURCE",length = 2)
    public Integer getCapitalSource() {
        return capitalSource;
    }
    public void setCapitalSource(Integer capitalSource) {
        this.capitalSource = capitalSource;
    }
    @Column(name = "IS_GUARANTEED",length = 1)
    public String getIsGuaranteed() {
        return isGuaranteed;
    }
    public void setIsGuaranteed(String isGuaranteed) {
        this.isGuaranteed = isGuaranteed;
    }
    @Column(name = "CERTIFICATE_WAY")
    public Integer getCertificateWay() {
        return certificateWay;
    }
    public void setCertificateWay(Integer certificateWay) {
        this.certificateWay = certificateWay;
    }
}
