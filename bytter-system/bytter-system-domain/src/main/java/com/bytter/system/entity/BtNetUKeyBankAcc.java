package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@Table(name = "BT_NET_UKEY_BANKACC")
public class BtNetUKeyBankAcc extends IdEntity implements Serializable {
    /**UkeyID*/
    private String uKeyId;
    /**账号ID*/
    private String bankAccId;
    @Column(name ="UKEY_ID",nullable=true,length=32)
    public String getuKeyId() {
        return uKeyId;
    }
    public void setuKeyId(String uKeyId) {
        this.uKeyId = uKeyId;
    }
    @Column(name ="BANK_ACC_ID",nullable=true,length=32)
    public String getBankAccId() {
        return bankAccId;
    }
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }
}
