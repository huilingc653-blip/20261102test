package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
import java.util.Date;
public class AccTypeInterestInfoBean implements Serializable {
    private String accType;
    private Date blnInstDate;
    private Date tallyDate;
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    public Date getBlnInstDate() {
        return blnInstDate;
    }
    public void setBlnInstDate(Date blnInstDate) {
        this.blnInstDate = blnInstDate;
    }
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
}
