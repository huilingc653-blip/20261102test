package com.bytter.system.vo;
import java.io.Serializable;
public class BtNetUKeyBankAccVO  implements Serializable {
    private String id;
    /**UkeyID*/
    private String uKeyId;
    /**账号ID*/
    private String bankAccId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getuKeyId() {
        return uKeyId;
    }
    public void setuKeyId(String uKeyId) {
        this.uKeyId = uKeyId;
    }
    public String getBankAccId() {
        return bankAccId;
    }
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }
}
