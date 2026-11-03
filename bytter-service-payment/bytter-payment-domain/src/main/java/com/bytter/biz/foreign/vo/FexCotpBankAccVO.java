package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class FexCotpBankAccVO  extends ExcelModel implements Serializable, IBaseDataInject, IBusinessKey {
    /**业务主键*/
    private String id;
    /**交易对手id*/
    private String cotpId;
    /**交易对手名称*/
    @BtCompositeSearch(fieldName = "a.cotp_name")
    @Excel(name = "*交易对手")
    private String cotpName;
    /**单位id*/
    @Corp(inject = "corpName")
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**交易对手银行账户*/
    @Excel(name = "*银行账户")
    @BtCompositeSearch(fieldName = "b.bank_acc")
    private String bankAcc;
    /**账户名称*/
    @Excel(name = "*账户名称")
    @BtCompositeSearch(fieldName = "b.acc_name")
    private String accName;
    /**Iban号码*/
    private String iBanCode;
    /**账户币种*/
    @BtCompositeSearch(fieldName = "b.cur_id")
    @Currencys(
         {
             @Currency(inject = "curName",field = "englishCode")
         }
    )
    private String curId;
    /**币种名称*/
    @Excel(name = "*币种")
    private String curName;
    /**备注*/
    private String rmk;
    /**银行*/
    /**重构后用不到作为银行国家字段*/
    private String bankId;
    /**银行名称*/
    @Excel(name = "*银行")
    private String bankName;
    /**BIC编码*/
    private String bicCode;
    /**支付类型*/
    private String payType;
    /**中转行的银行账户*/
    private String tranBankAcc;
    /**银行地址信息*/
    private String bankAddr;
    /**银行信息备注*/
    private String bankRmk;
    /**中转行*/
    private String tranBank;
    /**中转行名称*/
    private String tranBankName;
    /**中转银行BIC编码*/
    private String tranBicCode;
    /**中转银行地址信息*/
    private String tranBankAddr;
    /**数据状态*/
    @Excel(name = "*数据状态")
    private Integer status;
    /**创建人登录名称*/
    private String createBy;
    /**创建人名称*/
    private String createByName;
    /**创建日期*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新人名称*/
    private String updateByName;
    /**更新日期*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**本地清算号*/
    private String localClearNo;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCotpId() {
        return cotpId;
    }
    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getiBanCode() {
        return iBanCode;
    }
    public void setiBanCode(String iBanCode) {
        this.iBanCode = iBanCode;
    }

    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBicCode() {
        return bicCode;
    }
    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTranBankAcc() {
        return tranBankAcc;
    }
    public void setTranBankAcc(String tranBankAcc) {
        this.tranBankAcc = tranBankAcc;
    }

    public String getBankAddr() {
        return bankAddr;
    }
    public void setBankAddr(String bankAddr) {
        this.bankAddr = bankAddr;
    }

    public String getBankRmk() {
        return bankRmk;
    }
    public void setBankRmk(String bankRmk) {
        this.bankRmk = bankRmk;
    }

    public String getTranBank() {
        return tranBank;
    }
    public void setTranBank(String tranBank) {
        this.tranBank = tranBank;
    }

    public String getTranBicCode() {
        return tranBicCode;
    }
    public void setTranBicCode(String tranBicCode) {
        this.tranBicCode = tranBicCode;
    }

    public String getTranBankAddr() {
        return tranBankAddr;
    }
    public void setTranBankAddr(String tranBankAddr) {
        this.tranBankAddr = tranBankAddr;
    }

    public String getCotpName() {
        return cotpName;
    }
    public void setCotpName(String cotpName) {
        this.cotpName = cotpName;
    }

    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getTranBankName() {
        return tranBankName;
    }
    public void setTranBankName(String tranBankName) {
        this.tranBankName = tranBankName;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
