package com.bytter.biz.payment.api.vo;

import java.io.Serializable;
import java.util.Date;

public class NisBillBankSerialNoVO implements Serializable {
    /**单据id*/
    private String billId;
    /**单据来源*/
    private String isFrom;
    /**流水id*/
    private String dtlId;
    /**勾对时间*/
    private Date paymentblendingDate;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getIsFrom() {
        return isFrom;
    }

    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }

    public String getDtlId() {
        return dtlId;
    }

    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }

    public Date getPaymentblendingDate() {
        return paymentblendingDate;
    }

    public void setPaymentblendingDate(Date paymentblendingDate) {
        this.paymentblendingDate = paymentblendingDate;
    }
}
