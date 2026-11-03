package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.Corp;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @Title: VO
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
public class ChoProductDetailedVo implements java.io.Serializable {
    /**序号*/
    private Integer orderNumber;
    /**登记产品Id*/
    private String reqId;
    /**单位*/
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**银行类别*/
    private String bankTypeId;
    private String bankTypeName;
    /**购买银行*/
    private String bankName;
    /**产品类型*/
    private String productType;
    /**产品代码*/
    private String productCode;
    /**产品名称*/
    private String productName;
    /**理财账号*/
    private String bankAcc;
    /**购买金额*/
    private BigDecimal buyMoney;
    /**币别*/
    private String curId;
    private String curName;
    /**收益条款(天数)*/
    private Integer incomeClause;
    /**参考年化收益率(%)*/
    private BigDecimal referIncomeRate;
    /**参考收益*/
    private BigDecimal referIncomeMoney;
    /**期限*/
    private Integer dayLimit;
    /**申购日期*/
    private Date buyDate;
    /**起息日期*/
    private Date interestStartDate;
    /**到期日期*/
    private Date deadlineDate;
    /**累计赎回金额*/
    private BigDecimal redeemedsMoney;
    /**待赎回金额*/
    private BigDecimal waitRedeemMoney;
    /**赎回日期(最后一期)*/
    private Date lastRedeemedDate;
    /**赎回收益总利息*/
    private BigDecimal redeemedsIncomeMoney;
    /**赎回加权平均总投资金额*/
    private BigDecimal redeemedsWeightAverageInvest;
    /**累计赎回年化收益率*/
    private BigDecimal redeemedsAnnualizedReturn;
    /**当期起始日*/
    private Date currentStartDate;
    /**当期截止日*/
    private Date currentEndDate;
    /**当期赎回年化收益率(%)*/
    private BigDecimal currentRedeensAnnualizedReturn;
    /**当期待赎回理财天数*/
    private Integer currentWaitRedeemDay;
    /**当期待赎回理财权数*/
    private BigDecimal currentWaitRedeemWeight;
    /**当期待赎回利息*/
    private BigDecimal currentWaitRedeemIncomeMoney;
    /**当期待赎回加权平均投资金额*/
    private BigDecimal currentWaitRedeemWeightAverageInvest;
    /**当期待赎回年化收益率(%)*/
    private BigDecimal currentWaitRedeemAnnualizedReturn;
    /**当期理财利息*/
    private BigDecimal currentReferIncomeRate;
    /**当期理财加权平均投资金额*/
    private BigDecimal currentWeightAverageInvest;
    /**当期年化收益率(%)*/
    private BigDecimal currentAnnualizedReturn;
    /**赎回记录*/
    private List<ChoRedeemDetailedVo> redeemDetailedVos;
    /**
     * 是否到期
     */
    private String isDeadline;
    public List<ChoRedeemDetailedVo> getRedeemDetailedVos() {
        return redeemDetailedVos;
    }
    public String getIsDeadline() {
        return isDeadline;
    }
    public void setIsDeadline(String isDeadline) {
        this.isDeadline = isDeadline;
    }
    public void setRedeemDetailedVos(List<ChoRedeemDetailedVo> redeemDetailedVos) {
        this.redeemDetailedVos = redeemDetailedVos;
    }
    public Integer getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getReqId() {
        return reqId;
    }
    public void setReqId(String reqId) {
        this.reqId = reqId;
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
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public BigDecimal getBuyMoney() {
        return buyMoney;
    }
    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Integer getIncomeClause() {
        return incomeClause;
    }
    public void setIncomeClause(Integer incomeClause) {
        this.incomeClause = incomeClause;
    }
    public BigDecimal getReferIncomeRate() {
        return referIncomeRate;
    }
    public void setReferIncomeRate(BigDecimal referIncomeRate) {
        this.referIncomeRate = referIncomeRate;
    }
    public BigDecimal getReferIncomeMoney() {
        return referIncomeMoney;
    }
    public void setReferIncomeMoney(BigDecimal referIncomeMoney) {
        this.referIncomeMoney = referIncomeMoney;
    }
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    public Date getBuyDate() {
        return buyDate;
    }
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public BigDecimal getRedeemedsMoney() {
        return redeemedsMoney;
    }
    public void setRedeemedsMoney(BigDecimal redeemedsMoney) {
        this.redeemedsMoney = redeemedsMoney;
    }
    public BigDecimal getWaitRedeemMoney() {
        return waitRedeemMoney;
    }
    public void setWaitRedeemMoney(BigDecimal waitRedeemMoney) {
        this.waitRedeemMoney = waitRedeemMoney;
    }
    public Date getLastRedeemedDate() {
        return lastRedeemedDate;
    }
    public void setLastRedeemedDate(Date lastRedeemedDate) {
        this.lastRedeemedDate = lastRedeemedDate;
    }
    public BigDecimal getRedeemedsIncomeMoney() {
        return redeemedsIncomeMoney;
    }
    public void setRedeemedsIncomeMoney(BigDecimal redeemedsIncomeMoney) {
        this.redeemedsIncomeMoney = redeemedsIncomeMoney;
    }
    public BigDecimal getRedeemedsWeightAverageInvest() {
        return redeemedsWeightAverageInvest;
    }
    public void setRedeemedsWeightAverageInvest(BigDecimal redeemedsWeightAverageInvest) {
        this.redeemedsWeightAverageInvest = redeemedsWeightAverageInvest;
    }
    public BigDecimal getRedeemedsAnnualizedReturn() {
        return redeemedsAnnualizedReturn;
    }
    public void setRedeemedsAnnualizedReturn(BigDecimal redeemedsAnnualizedReturn) {
        this.redeemedsAnnualizedReturn = redeemedsAnnualizedReturn;
    }
    public Date getCurrentStartDate() {
        return currentStartDate;
    }
    public void setCurrentStartDate(Date currentStartDate) {
        this.currentStartDate = currentStartDate;
    }
    public Date getCurrentEndDate() {
        return currentEndDate;
    }
    public void setCurrentEndDate(Date currentEndDate) {
        this.currentEndDate = currentEndDate;
    }
    public BigDecimal getCurrentRedeensAnnualizedReturn() {
        return currentRedeensAnnualizedReturn;
    }
    public void setCurrentRedeensAnnualizedReturn(BigDecimal currentRedeensAnnualizedReturn) {
        this.currentRedeensAnnualizedReturn = currentRedeensAnnualizedReturn;
    }
    public Integer getCurrentWaitRedeemDay() {
        return currentWaitRedeemDay;
    }
    public void setCurrentWaitRedeemDay(Integer currentWaitRedeemDay) {
        this.currentWaitRedeemDay = currentWaitRedeemDay;
    }
    public BigDecimal getCurrentWaitRedeemWeight() {
        return currentWaitRedeemWeight;
    }
    public void setCurrentWaitRedeemWeight(BigDecimal currentWaitRedeemWeight) {
        this.currentWaitRedeemWeight = currentWaitRedeemWeight;
    }
    public BigDecimal getCurrentWaitRedeemIncomeMoney() {
        return currentWaitRedeemIncomeMoney;
    }
    public void setCurrentWaitRedeemIncomeMoney(BigDecimal currentWaitRedeemIncomeMoney) {
        this.currentWaitRedeemIncomeMoney = currentWaitRedeemIncomeMoney;
    }
    public BigDecimal getCurrentWaitRedeemWeightAverageInvest() {
        return currentWaitRedeemWeightAverageInvest;
    }
    public void setCurrentWaitRedeemWeightAverageInvest(BigDecimal currentWaitRedeemWeightAverageInvest) {
        this.currentWaitRedeemWeightAverageInvest = currentWaitRedeemWeightAverageInvest;
    }
    public BigDecimal getCurrentWaitRedeemAnnualizedReturn() {
        return currentWaitRedeemAnnualizedReturn;
    }
    public void setCurrentWaitRedeemAnnualizedReturn(BigDecimal currentWaitRedeemAnnualizedReturn) {
        this.currentWaitRedeemAnnualizedReturn = currentWaitRedeemAnnualizedReturn;
    }
    public BigDecimal getCurrentReferIncomeRate() {
        return currentReferIncomeRate;
    }
    public void setCurrentReferIncomeRate(BigDecimal currentReferIncomeRate) {
        this.currentReferIncomeRate = currentReferIncomeRate;
    }
    public BigDecimal getCurrentWeightAverageInvest() {
        return currentWeightAverageInvest;
    }
    public void setCurrentWeightAverageInvest(BigDecimal currentWeightAverageInvest) {
        this.currentWeightAverageInvest = currentWeightAverageInvest;
    }
    public BigDecimal getCurrentAnnualizedReturn() {
        return currentAnnualizedReturn;
    }
    public void setCurrentAnnualizedReturn(BigDecimal currentAnnualizedReturn) {
        this.currentAnnualizedReturn = currentAnnualizedReturn;
    }
}
