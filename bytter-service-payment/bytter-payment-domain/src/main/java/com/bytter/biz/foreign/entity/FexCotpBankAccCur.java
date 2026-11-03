package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: FEX_COTP_BANKACC_CUR
 * @date 2024-02-24 18:03:22
 */
@Entity
@Table(name = "FEX_COTP_BANKACC_CUR", schema = "")
public class FexCotpBankAccCur extends IdEntity implements java.io.Serializable {
    /**交易对手账号ID*/
    private String bankAccId;
    /**币别ID*/
    private String curId;

    @Column(name ="BANK_ACC_ID",nullable=false,length=32)
    public String getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }

    @Column(name ="CUR_ID",nullable=false,length=32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }
}
