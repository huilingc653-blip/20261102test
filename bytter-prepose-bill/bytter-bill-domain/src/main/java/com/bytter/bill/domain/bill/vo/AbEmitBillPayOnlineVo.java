package com.bytter.bill.domain.bill.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @author mof
 * @version 1.0
 * @Title: Vo
 * @Description: 兑付确认线上批量提交vo
 * @date 2022-03-28 17:35:25
 */
@SuppressWarnings("serial")
public class AbEmitBillPayOnlineVo implements Serializable {
    private String id;
    /**
     * 票据主表id
     */
    private String vid;
    /**
     * 票据从表id
     */
    private String vvid;
    /**
     * 兑付单位
     */
    private String payCorpId;
    /**
     * 兑付金额
     */
    private BigDecimal payMoney;
    /**
     * 付款单号
     */
    private String billNo;
    /**
     * 收款账号
     */
    private String payPayeeacc;
    /**
     * 收款账号名称
     */
    private String payPayeeName;
    /**
     * 收款开户行行号
     */
    private String payPayeeBankNo;
    /**
     * 收款开户行名称
     */
    private String payPayeeBankName;
    /**
     * 所在省
     */
    private String prov;
    private String provName;
    /**
     * 所在市
     */
    private String city;
    private String cityName;
    /**
     * 兑付用途
     */
    private String payUse;
    /**
     * 兑付备注
     */
    private String payAbs;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVid() {
        return vid;
    }
    public void setVid(String vid) {
        this.vid = vid;
    }
    public String getVvid() {
        return vvid;
    }
    public void setVvid(String vvid) {
        this.vvid = vvid;
    }
    public String getPayCorpId() {
        return payCorpId;
    }
    public void setPayCorpId(String payCorpId) {
        this.payCorpId = payCorpId;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getPayPayeeacc() {
        return payPayeeacc;
    }
    public void setPayPayeeacc(String payPayeeacc) {
        this.payPayeeacc = payPayeeacc;
    }
    public String getPayPayeeName() {
        return payPayeeName;
    }
    public void setPayPayeeName(String payPayeeName) {
        this.payPayeeName = payPayeeName;
    }
    public String getPayPayeeBankNo() {
        return payPayeeBankNo;
    }
    public void setPayPayeeBankNo(String payPayeeBankNo) {
        this.payPayeeBankNo = payPayeeBankNo;
    }
    public String getPayPayeeBankName() {
        return payPayeeBankName;
    }
    public void setPayPayeeBankName(String payPayeeBankName) {
        this.payPayeeBankName = payPayeeBankName;
    }
    public String getProv() {
        return prov;
    }
    public void setProv(String prov) {
        this.prov = prov;
    }
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getPayUse() {
        return payUse;
    }
    public void setPayUse(String payUse) {
        this.payUse = payUse;
    }
    public String getPayAbs() {
        return payAbs;
    }
    public void setPayAbs(String payAbs) {
        this.payAbs = payAbs;
    }
    @Override
    public String toString() {
        return "AbEmitBillPayOnlineVo{" +
                "id='" + id + '\'' +
                ", vid='" + vid + '\'' +
                ", vvid='" + vvid + '\'' +
                ", payCorpId='" + payCorpId + '\'' +
                ", payMoney=" + payMoney +
                ", billNo='" + billNo + '\'' +
                ", payPayeeacc='" + payPayeeacc + '\'' +
                ", payPayeeName='" + payPayeeName + '\'' +
                ", payPayeeBankNo='" + payPayeeBankNo + '\'' +
                ", payPayeeBankName='" + payPayeeBankName + '\'' +
                ", prov='" + prov + '\'' +
                ", provName='" + provName + '\'' +
                ", city='" + city + '\'' +
                ", cityName='" + cityName + '\'' +
                ", payUse='" + payUse + '\'' +
                ", payAbs='" + payAbs + '\'' +
                '}';
    }
}
