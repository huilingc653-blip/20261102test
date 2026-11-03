package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: vo
 * @Description: 主体单位默认银行账户明细VO
 * @author fusb
 * @date 2020-02-17 10:31:56
 * @version 1.0
 */
public class FexCorpBankAccDtlVO implements Serializable , IBaseDataInject {
    /**主键id*/
    private String id;
    /**主表ID*/
    private String mainId;
    /**生效日期*/
    private Date effectDate;
    /**银行类别*/
    @BankType(inject = "bankTypeName")
    private String bankType;
    /**银行类别名称*/
    private String bankTypeName;
    /**产品ID*/
    private String proId;
    /**账号ID*/
    private String accId;
    /**银行账号*/
    private String bankAcc;
    /**账户名称*/
    private String accName;
    /**币种*/
    @Currencys(
        {
            @Currency(inject = "curName",field = "englishCode")
        }
    )
    private String curId;
    /**币种名称*/
    private String curName;
    /**默认支付方式*/
    private String voucherTypeId;
    /**默认支付方式名称*/
    private String voucherName;

    /**业务类型：0主体单位，1交易对手*/
    private String busType;

    /**收款账户类型*/
    private String payModel;

    /**快捷支付信息*/
    private String payValue;

    /** FPS和PAY相关参数需要 */
    private String bifCode;
    private String accType;
    private String bankTypeId;
    private String bankCode;
    private String localClearNo;
    private String isDomesticBank;
    private String provName;
    private String prov;
    private String countryEnCode;
    private String bankAdd;

    public String getMainId() {
        return mainId;
    }
    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public Date getEffectDate() {
        return effectDate;
    }
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getProId() {
        return proId;
    }
    public void setProId(String proId) {
        this.proId = proId;
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

    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getVoucherTypeId() {
        return voucherTypeId;
    }
    public void setVoucherTypeId(String voucherTypeId) {
        this.voucherTypeId = voucherTypeId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getPayModel() {
        return payModel;
    }

    public void setPayModel(String payModel) {
        this.payModel = payModel;
    }

    public String getPayValue() {
        return payValue;
    }

    public void setPayValue(String payValue) {
        this.payValue = payValue;
    }

    public String getIsDomesticBank() {
        return isDomesticBank;
    }

    public void setIsDomesticBank(String isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getCountryEnCode() {
        return countryEnCode;
    }

    public void setCountryEnCode(String countryEnCode) {
        this.countryEnCode = countryEnCode;
    }

    public String getBankAdd() {
        return bankAdd;
    }

    public void setBankAdd(String bankAdd) {
        this.bankAdd = bankAdd;
    }
}
