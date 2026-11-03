package com.bytter.erp.dto;


/**
 * @Title: 账号币别对应表
 */

public class ErpBtBankAccCurDTO implements java.io.Serializable {
    /**
     * 账号Id
     */
    private String bankAccId;
    private String bankAcc;
    /**
     * 币别
     */
    private String curId;
    private String curName;
    /**
     * 关联内部账号
     */
    private String counterAcc;
    /**
     * 关联上级内部账号
     */
    private String superCounterAcc;
    /**
     * 父账号ID
     */
    private String prentAccId;
    private String prentBankAcc;

    public String getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
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

    public String getCounterAcc() {
        return counterAcc;
    }

    public void setCounterAcc(String counterAcc) {
        this.counterAcc = counterAcc;
    }

    public String getSuperCounterAcc() {
        return superCounterAcc;
    }

    public void setSuperCounterAcc(String superCounterAcc) {
        this.superCounterAcc = superCounterAcc;
    }

    public String getPrentAccId() {
        return prentAccId;
    }

    public void setPrentAccId(String prentAccId) {
        this.prentAccId = prentAccId;
    }

    public String getPrentBankAcc() {
        return prentBankAcc;
    }

    public void setPrentBankAcc(String prentBankAcc) {
        this.prentBankAcc = prentBankAcc;
    }
}
