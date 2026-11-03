package com.bytter.guarantee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;


public class CrBillBondRateDTO implements Serializable {
    private String vvid;
    private String bankTypeId;
    private String batchNo;

    private String bankAcc;

    /**出票日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    /**业务状态*/
    private Integer busStatus;

    public String getVvid() {
        return vvid;
    }

    public void setVvid(String vvid) {
        this.vvid = vvid;
    }

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getBusStatus() {
        return busStatus;
    }

    public void setBusStatus(Integer busStatus) {
        this.busStatus = busStatus;
    }
}
