package com.bytter.system.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.CorpPool;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
public class FcAccForBankAccDTO implements Serializable {
    private String id;
    /**账号*/
    @BtCompositeSearch(fieldName = "acc.blnctr_acc")
    private String blnctrAcc;
    /**户名*/
    @BtCompositeSearch(fieldName = "acc.acc_name")
    private String accName;
    /**单位*/
    @Corp(inject="corpName")
    @BtCompositeSearch(fieldName = "acc.corp_id")
    private String corpId;
    private String corpName;
    /**币别*/
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "acc.cur_id")
    private String curId;
    @BtCompositeSearch(fieldName = "acc.cur_id")
    private String curName;
    /**
     * 网点
     */
    @Corp(inject="netName")
    @BtCompositeSearch(fieldName = "acc.net_id")
    private String netId;
    private String netName;
    /**
     * 资金池
     */
    @CorpPool(inject="netPoolName")
    private String netPoolId;
    private String netPoolName;
    /**开户日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "acc.ACC_OPEN_TIME")
    private Date accOpenTime;
    private String bankName;
    /**账户类别*/
    @BtCompositeSearch(fieldName = "acc.acc_type")
    private String accType;
    private String accTypeName;
    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    private String poolSign;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
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
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public Date getAccOpenTime() {
        return accOpenTime;
    }
    public void setAccOpenTime(Date accOpenTime) {
        this.accOpenTime = accOpenTime;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    public String getAccTypeName() {
        return accTypeName;
    }
    public void setAccTypeName(String accTypeName) {
        this.accTypeName = accTypeName;
    }
    public String getNetPoolId() {
        return netPoolId;
    }
    public void setNetPoolId(String netPoolId) {
        this.netPoolId = netPoolId;
    }
    public String getNetPoolName() {
        return netPoolName;
    }
    public void setNetPoolName(String netPoolName) {
        this.netPoolName = netPoolName;
    }
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
}
