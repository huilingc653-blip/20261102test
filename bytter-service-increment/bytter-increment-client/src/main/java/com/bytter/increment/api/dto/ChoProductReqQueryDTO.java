package com.bytter.increment.api.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
public class ChoProductReqQueryDTO extends IdEntity implements java.io.Serializable{
    /**业务单号*/
    private String billNo;
    /**登记产品Id*/
    private String reqId;
    /**单位ID*/
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName="pr.corp_Id")
    private String corpId;
    @BtCompositeSearch(fieldName="pr.corp_Id")
    private String corpName;
    /**银行类别*/
    @Dict(tableName="bt_bank_type",valueFieldName="id",showFiledName="type_name",inject="bankTypeName")
    @BtCompositeSearch(fieldName = "pr.bank_Type_Id")
    private String bankTypeId;
    @BtCompositeSearch(fieldName = "pr.bank_type_id")
    private String bankTypeName;
    /**购买银行ID*/
    private String bankInfoId;
    /**购买银行*/
    @BtCompositeSearch(fieldName = "pr.bank_info_id")
    private String bankName;
    /**产品代码*/
    @BtCompositeSearch(fieldName = "pr.product_Code")
    private String productCode;
    /**产品名称*/
    @BtCompositeSearch(fieldName = "pt.product_Name")
    private String productName;
    /**产品类型*/
    @BtCompositeSearch(fieldName = "pr.product_Type")
    private String productType;
    /**收益条款:360-360天;365-365天*/
    @BtCompositeSearch(fieldName = "pr.income_Clause")
    private Integer incomeClause;
    /**理财账号*/
    @BtCompositeSearch(fieldName = "pr.bank_Acc")
    private String bankAcc;
    /**购买金额*/
    @BtCompositeSearch(fieldName = "pr.buy_Money")
    private BigDecimal buyMoney;
    /**币别*/
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "pr.cur_Id")
    private String curId;
    private String curName;
    /**赎回日期*/
    @BtCompositeSearch(fieldName = "prd.redeem_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date redeemDate;
    /**已赎回金额*/
    private BigDecimal redeemedMoney;
    /**待赎回金额*/
    private BigDecimal waitRedeemMoney;
    /**起息日期*/
    @BtCompositeSearch(fieldName = "pr.interest_start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date interestStartDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName = "pr.deadline_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date deadlineDate;
    /**参考收益率*/
    @BtCompositeSearch(fieldName = "pr.refer_income_rate")
    private BigDecimal referIncomeRate;
    /**参考收益=购买金额*参考收益率*期限;360（除以360还是365以收益条款为准）*/
    @BtCompositeSearch(fieldName = "pr.refer_income_money")
    private BigDecimal referIncomeMoney;
    /**平均收益率*/
    private BigDecimal averageIncomeRate;
    /**实际收益率*/
    @BtCompositeSearch(fieldName = "prd.income_rate")
    private BigDecimal incomeRate;
    /**收益金额*/
    private BigDecimal incomeMoney;
    /**申购费*/
    private BigDecimal subFeeMoney;
    /**赎回费*/
    private BigDecimal redeemChargeMoney;
    /**其他手续费*/
    private BigDecimal chargeMoney;
    /**是否可提前赎回 0-否 1-是*/
    @BtCompositeSearch(fieldName = "pr.is_move_up")
    private String isMoveUp;
    /**状态*/
    @BtCompositeSearch(fieldName = "pr.status")
    private Integer status;
    /**最新一条赎回记录的状态*/
    @BtCompositeSearch(fieldName = "pr.redeem_status")
    private Integer redeemStatus;
    /**金额单位*/
    private Integer moneyUnit;
    /**资金来源:1 自由资金 2 募集资金 3 银行贷款*/
    private Integer capitalSource;
    /**是否保本 0-否 1-是*/
    private String isGuaranteed;

    /**银行流水认领组件挂接确认*/
    private String collectionConfirm;

    /**购买日期*/
    @BtCompositeSearch(fieldName = "pr.buy_Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date buyDate;

    /**
     * 理财类型
     */
    @BtCompositeSearch(fieldName = "pt.FINANCIAL_TYPE")
    @Dict(dictGroup = "financial.type", inject = "financialTypeName")
    private String financialType;

    @BtCompositeSearch(fieldName = "pt.FINANCIAL_TYPE")
    private String financialTypeName;

    /**
     * 付息方式
     */
    @BtCompositeSearch(fieldName = "pr.PAY_WAY")
    @Dict(dictGroup = "financial.payWay", inject = "payWayName")
    private String payWay;

    private String payWayName;

    /**
     * 合同号
     */
    @BtCompositeSearch(fieldName = "pr.CONTRACT_NO")
    private String contractNo;

    /**
     * 收益类型
     */
    @BtCompositeSearch(fieldName = "pr.INCOME_TYPE")
    private String incomeType;

    /**
     * 到期是否续存
     */
    @BtCompositeSearch(fieldName = "pr.IS_STORE")
    private String isStore;

    /**
     * 起始合同利率
     */
    @BtCompositeSearch(fieldName = "pr.START_CONTRACT_RATE")
    private BigDecimal startContractRate;

    /**
     * 结束合同利率
     */
    @BtCompositeSearch(fieldName = "pr.END_CONTRACT_RATE")
    private BigDecimal endContractRate;

    /**
     * 业务状态
     */
    @BtCompositeSearch(fieldName = "pr.biz_status")
    private Integer bizStatus;

    /**
     * 购买份额
     */
    private BigDecimal portion;

    /**
     * 已赎回份额
     */
    private BigDecimal redeemedPortion;

    /**
     * 待赎回份额
     */
    private BigDecimal waitRedeemPortion;

    /**
     * 持有收益率
     */
    private BigDecimal holdIncomeRate;

    /**
     * 实际收益（非保本）
     */
    private BigDecimal actualIncome;

    /**
     * 年化收益率
     */
    private BigDecimal annualizedIncomeRate;

    /**
     * 年化收益
     */
    private BigDecimal annualizedIncome;

    /**
     * 余额
     */
    private BigDecimal bal;

    /**
     * 持有成本
     */
    private BigDecimal holdMoney;

    /**
     * 净值维护日期
     */
    private Date netValDate;

    /**
     * 已赎回本金
     */
    private BigDecimal redeemedPrincipal;

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
     * 资金来源
     */
    @BtCompositeSearch(fieldName = "pr.capital_source")
    private String capitalSourceStr;

    /**
     * 期限
     */
    private Integer dayLimit;

    public Integer getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }

    public String getCapitalSourceStr() {
        return capitalSourceStr;
    }

    public void setCapitalSourceStr(String capitalSourceStr) {
        this.capitalSourceStr = capitalSourceStr;
    }

    public BigDecimal getEndReferIncomeMoney() {
        return endReferIncomeMoney;
    }

    public void setEndReferIncomeMoney(BigDecimal endReferIncomeMoney) {
        this.endReferIncomeMoney = endReferIncomeMoney;
    }

    public BigDecimal getEndReferIncomeRate() {
        return endReferIncomeRate;
    }

    public void setEndReferIncomeRate(BigDecimal endReferIncomeRate) {
        this.endReferIncomeRate = endReferIncomeRate;
    }

    public String getIsMaintainVal() {
        return isMaintainVal;
    }

    public void setIsMaintainVal(String isMaintainVal) {
        this.isMaintainVal = isMaintainVal;
    }

    public BigDecimal getRedeemedPrincipal() {
        return redeemedPrincipal;
    }

    public void setRedeemedPrincipal(BigDecimal redeemedPrincipal) {
        this.redeemedPrincipal = redeemedPrincipal;
    }

    public BigDecimal getHoldMoney() {
        return holdMoney;
    }

    public void setHoldMoney(BigDecimal holdMoney) {
        this.holdMoney = holdMoney;
    }

    public Date getNetValDate() {
        return netValDate;
    }

    public void setNetValDate(Date netValDate) {
        this.netValDate = netValDate;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    public BigDecimal getAnnualizedIncome() {
        return annualizedIncome;
    }

    public void setAnnualizedIncome(BigDecimal annualizedIncome) {
        this.annualizedIncome = annualizedIncome;
    }

    public BigDecimal getHoldIncomeRate() {
        return holdIncomeRate;
    }

    public void setHoldIncomeRate(BigDecimal holdIncomeRate) {
        this.holdIncomeRate = holdIncomeRate;
    }

    public BigDecimal getActualIncome() {
        return actualIncome;
    }

    public void setActualIncome(BigDecimal actualIncome) {
        this.actualIncome = actualIncome;
    }

    public BigDecimal getAnnualizedIncomeRate() {
        return annualizedIncomeRate;
    }

    public void setAnnualizedIncomeRate(BigDecimal annualizedIncomeRate) {
        this.annualizedIncomeRate = annualizedIncomeRate;
    }

    public BigDecimal getRedeemedPortion() {
        return redeemedPortion;
    }

    public BigDecimal getPortion() {
        return portion;
    }

    public void setPortion(BigDecimal portion) {
        this.portion = portion;
    }

    public void setRedeemedPortion(BigDecimal redeemedPortion) {
        this.redeemedPortion = redeemedPortion;
    }

    public BigDecimal getWaitRedeemPortion() {
        return waitRedeemPortion;
    }

    public void setWaitRedeemPortion(BigDecimal waitRedeemPortion) {
        this.waitRedeemPortion = waitRedeemPortion;
    }

    public Integer getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getFinancialType() {
        return financialType;
    }

    public void setFinancialType(String financialType) {
        this.financialType = financialType;
    }

    public String getFinancialTypeName() {
        return financialTypeName;
    }

    public void setFinancialTypeName(String financialTypeName) {
        this.financialTypeName = financialTypeName;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayWayName() {
        return payWayName;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getIsStore() {
        return isStore;
    }

    public void setIsStore(String isStore) {
        this.isStore = isStore;
    }

    public BigDecimal getStartContractRate() {
        return startContractRate;
    }

    public void setStartContractRate(BigDecimal startContractRate) {
        this.startContractRate = startContractRate;
    }

    public BigDecimal getEndContractRate() {
        return endContractRate;
    }

    public void setEndContractRate(BigDecimal endContractRate) {
        this.endContractRate = endContractRate;
    }

    public String getCollectionConfirm() {
        return collectionConfirm;
    }

    public void setCollectionConfirm(String collectionConfirm) {
        this.collectionConfirm = collectionConfirm;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getIncomeClause() {
        return incomeClause;
    }

    public void setIncomeClause(Integer incomeClause) {
        this.incomeClause = incomeClause;
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

    public Date getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }

    public BigDecimal getRedeemedMoney() {
        return redeemedMoney;
    }

    public void setRedeemedMoney(BigDecimal redeemedMoney) {
        this.redeemedMoney = redeemedMoney;
    }

    public BigDecimal getWaitRedeemMoney() {
        return waitRedeemMoney;
    }

    public void setWaitRedeemMoney(BigDecimal waitRedeemMoney) {
        this.waitRedeemMoney = waitRedeemMoney;
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

    public BigDecimal getAverageIncomeRate() {
        return averageIncomeRate;
    }

    public void setAverageIncomeRate(BigDecimal averageIncomeRate) {
        this.averageIncomeRate = averageIncomeRate;
    }

    public BigDecimal getIncomeRate() {
        return incomeRate;
    }

    public void setIncomeRate(BigDecimal incomeRate) {
        this.incomeRate = incomeRate;
    }

    public BigDecimal getIncomeMoney() {
        return incomeMoney;
    }

    public void setIncomeMoney(BigDecimal incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    public BigDecimal getSubFeeMoney() {
        return subFeeMoney;
    }

    public void setSubFeeMoney(BigDecimal subFeeMoney) {
        this.subFeeMoney = subFeeMoney;
    }

    public BigDecimal getRedeemChargeMoney() {
        return redeemChargeMoney;
    }

    public void setRedeemChargeMoney(BigDecimal redeemChargeMoney) {
        this.redeemChargeMoney = redeemChargeMoney;
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public String getIsMoveUp() {
        return isMoveUp;
    }

    public void setIsMoveUp(String isMoveUp) {
        this.isMoveUp = isMoveUp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRedeemStatus() {
        return redeemStatus;
    }

    public void setRedeemStatus(Integer redeemStatus) {
        this.redeemStatus = redeemStatus;
    }

    public Integer getMoneyUnit() {
        return moneyUnit;
    }

    public void setMoneyUnit(Integer moneyUnit) {
        this.moneyUnit = moneyUnit;
    }

    public Integer getCapitalSource() {
        return capitalSource;
    }

    public void setCapitalSource(Integer capitalSource) {
        this.capitalSource = capitalSource;
    }

    public String getIsGuaranteed() {
        return isGuaranteed;
    }

    public void setIsGuaranteed(String isGuaranteed) {
        this.isGuaranteed = isGuaranteed;
    }

    public String getBankInfoId() {
        return bankInfoId;
    }

    public void setBankInfoId(String bankInfoId) {
        this.bankInfoId = bankInfoId;
    }
}
