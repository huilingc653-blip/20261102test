package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 主体单位默认银行账户明细实体
 * @author fusb
 * @date 2020-02-17 10:31:56
 * @version 1.0
 */
@Entity
@Table(name = "FEX_CORP_BANKACC_DTL", schema = "")
public class FexCorpBankAccDtl extends IdEntity {
    /**主表ID*/
    private String mainId;
    /**生效日期*/
    private Date effectDate;
    /**银行类别*/
    private String bankType;
    /**产品ID*/
    private String proId;
    /**账号ID*/
    private String accId;
    /**银行账号*/
    private String bankAcc;
    /**账户名称*/
    private String accName;
    /**币种*/
    private String curId;
    /**默认支付方式*/
    private String voucherTypeId;

    /**收款账户类型*/
    private String payModel;

    /**快捷支付信息*/
    private String payValue;

    @Column(name = "MAIN_ID")
    public String getMainId() {
        return mainId;
    }
    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    @Column(name = "EFFECT_DATE")
    public Date getEffectDate() {
        return effectDate;
    }
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    @Column(name = "BANK_TYPE")
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    @Column(name = "PRO_ID")
    public String getProId() {
        return proId;
    }
    public void setProId(String proId) {
        this.proId = proId;
    }

    @Column(name = "ACC_ID")
    public String getAccId() {
        return accId;
    }
    public void setAccId(String accId) {
        this.accId = accId;
    }

    @Column(name = "BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    @Column(name = "ACC_NAME")
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "VOUCHER_TYPE_ID")
    public String getVoucherTypeId() {
        return voucherTypeId;
    }
    public void setVoucherTypeId(String voucherTypeId) {
        this.voucherTypeId = voucherTypeId;
    }

    @Column(name = "PAY_MODEL")
    public String getPayModel() {
        return payModel;
    }

    public void setPayModel(String payModel) {
        this.payModel = payModel;
    }

    @Column(name = "PAY_VALUE")
    public String getPayValue() {
        return payValue;
    }

    public void setPayValue(String payValue) {
        this.payValue = payValue;
    }
}
