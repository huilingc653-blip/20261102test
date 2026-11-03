package com.bytter.biz.mobile.vo;


import java.io.Serializable;

/**
 * @author qijh
 */
public class BtBankAccMobileVO implements Serializable {
    /** 币别 */
    private String curName;
    /** 银行账号 */
    private String bankAcc;
    /** 银行账号名称 */
    private String bankName;

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
