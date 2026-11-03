package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.CorpPool;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title: vo
 * @Description: 资金池账户额度查询VO
 * @author wangzx
 * @date 2020-03-11 10:31:56
 * @version 1.0
 */
public class FexCorpPoolBankaccQuotaVO implements Serializable, IBaseDataInject {

    private String id;
    /**上级账号*/
    private String parentId;
    /**资金池ID*/
    @BtCompositeSearch(fieldName = "p.pool_id")
    @CorpPool(inject="poolName")
    private String poolId;
    /**资金池名称*/
    private String poolName;
    /**单位ID*/
    @Corp(inject = "corpName")
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**限额折算币种*/
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="englishCode", field = "englishCode")
            }
    )
    private String curId;
    private String curName;
    private String englishCode;
    /**限额限额折算汇率类型ID*/
    private String rateTypeId;
    /**账号ID*/
    private String accId;
    /**账号*/
    @BtCompositeSearch(fieldName = "acc.bank_acc")
    private String bankAcc;

    @Currencys(
            {
                    @Currency(inject="accCurName"),
                    @Currency(inject="accEnglishCode", field = "englishCode")
            }
    )
    /**主账户币种*/
    private String accCurId;
    private String accEnglishCode;
    private String accCurName;
    /**外债余额*/
    private BigDecimal externalDebtBal;
    /**外债限额*/
    private BigDecimal externalDebtLimit;
    /**剩余外债额度*/
    private BigDecimal remainingDebtQuota;
    /**对外放款余额*/
    private BigDecimal externalLoanBal;
    /**对外放款限额*/
    private BigDecimal externalLoanLimit;
    /**剩余对外放款额度*/
    private BigDecimal remainingLoanQuota;
    /**流入余额*/
    private BigDecimal inflowBal;
    /**流出余额*/
    private BigDecimal outflowBal;
    /**净流入额*/
    private BigDecimal netInflowBal;
    /**净流入限额*/
    private BigDecimal netInflowLimit;
    /**剩余净流入额度*/
    private BigDecimal remainingNetInflowQuota;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
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

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getAccCurId() {
        return accCurId;
    }

    public void setAccCurId(String accCurId) {
        this.accCurId = accCurId;
    }

    public String getAccEnglishCode() {
        return accEnglishCode;
    }

    public void setAccEnglishCode(String accEnglishCode) {
        this.accEnglishCode = accEnglishCode;
    }

    public String getAccCurName() {
        return accCurName;
    }

    public void setAccCurName(String accCurName) {
        this.accCurName = accCurName;
    }

    public BigDecimal getExternalDebtBal() {
        return externalDebtBal;
    }

    public void setExternalDebtBal(BigDecimal externalDebtBal) {
        this.externalDebtBal = externalDebtBal;
    }

    public BigDecimal getExternalDebtLimit() {
        return externalDebtLimit;
    }

    public void setExternalDebtLimit(BigDecimal externalDebtLimit) {
        this.externalDebtLimit = externalDebtLimit;
    }

    public BigDecimal getRemainingDebtQuota() {
        return remainingDebtQuota;
    }

    public void setRemainingDebtQuota(BigDecimal remainingDebtQuota) {
        this.remainingDebtQuota = remainingDebtQuota;
    }

    public BigDecimal getExternalLoanBal() {
        return externalLoanBal;
    }

    public void setExternalLoanBal(BigDecimal externalLoanBal) {
        this.externalLoanBal = externalLoanBal;
    }

    public BigDecimal getExternalLoanLimit() {
        return externalLoanLimit;
    }

    public void setExternalLoanLimit(BigDecimal externalLoanLimit) {
        this.externalLoanLimit = externalLoanLimit;
    }

    public BigDecimal getRemainingLoanQuota() {
        return remainingLoanQuota;
    }

    public void setRemainingLoanQuota(BigDecimal remainingLoanQuota) {
        this.remainingLoanQuota = remainingLoanQuota;
    }

    public BigDecimal getInflowBal() {
        return inflowBal;
    }

    public void setInflowBal(BigDecimal inflowBal) {
        this.inflowBal = inflowBal;
    }

    public BigDecimal getOutflowBal() {
        return outflowBal;
    }

    public void setOutflowBal(BigDecimal outflowBal) {
        this.outflowBal = outflowBal;
    }

    public BigDecimal getNetInflowBal() {
        return netInflowBal;
    }

    public void setNetInflowBal(BigDecimal netInflowBal) {
        this.netInflowBal = netInflowBal;
    }

    public BigDecimal getNetInflowLimit() {
        return netInflowLimit;
    }

    public void setNetInflowLimit(BigDecimal netInflowLimit) {
        this.netInflowLimit = netInflowLimit;
    }

    public BigDecimal getRemainingNetInflowQuota() {
        return remainingNetInflowQuota;
    }

    public void setRemainingNetInflowQuota(BigDecimal remainingNetInflowQuota) {
        this.remainingNetInflowQuota = remainingNetInflowQuota;
    }
}
