package com.bytter.system.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 银行账号绑定账号中间表
 */
@Entity
@Table(name = "NIS_BINDING_BANKACC", schema = "")
public class NisBindingBankacc extends IdEntity implements java.io.Serializable{
    /**
     * 银行账号id
     */
    private String bankId;
    /**
     * 绑定账号id
     */
    private String bindingBankId;

    @Column(name = "BANK_ID")
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @Column(name = "BINDING_BANK_ID")
    public String getBindingBankId() {
        return bindingBankId;
    }

    public void setBindingBankId(String bindingBankId) {
        this.bindingBankId = bindingBankId;
    }
}
