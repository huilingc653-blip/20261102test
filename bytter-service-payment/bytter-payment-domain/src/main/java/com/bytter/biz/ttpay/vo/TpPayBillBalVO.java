package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Description 余额查询vo
 * @date 2022-02-15 15:43:05
 */
public class TpPayBillBalVO implements java.io.Serializable {
    private String id;
    /**
     * 单位ID	CORP_ID	VARCHAR2(32)
     **/
    @BtCompositeSearch(fieldName = "m.corp_id")
    private String corpId;
    /**
     * 单位名称
     **/
    @BtCompositeSearch(fieldName = "m.corp_id")
    private String corpCode;
    /**
     * 单位名称
     **/
    @BtCompositeSearch(fieldName = "m.corp_id")
    private String corpName;
    /**
     * 第三方支付方式
     **/
    //@Dict(dictGroup = TtPayConstant.TTPAY_PAYTYPE_DICT, inject = "payTypeName")
    private String payType;
    @BtCompositeSearch(fieldName = "d.payType")
    private String payTypeName;
    /**
     * 商户号
     **/
    @BtCompositeSearch(fieldName = "m.merchant_no")
    private String merchantNo;
    /**
     * 商户号/应用id -- 流水查询sql返回的是应用id
     **/
    @BtCompositeSearch(fieldName = "d.mchId")
    private String mchId;
    /**
     * 余额日期
     **/
    @BtCompositeSearch(fieldName = "d.balDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date balDate;
    /**
     * 账户余额
     **/
    @BtCompositeSearch(fieldName = "d.totalAmount")
    private BigDecimal totalAmount;
    /**
     * 账户可用余额
     **/
    @BtCompositeSearch(fieldName = "d.availableAmount")
    private BigDecimal availableAmount;
    /**
     * 冻结金额
     **/
    @BtCompositeSearch(fieldName = "d.freezeAmount")
    private BigDecimal freezeAmount;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getPayTypeName() {
        return payTypeName;
    }
    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
    public String getMerchantNo() {
        return merchantNo;
    }
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
    public String getMchId() {
        return mchId;
    }
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    public Date getBalDate() {
        return balDate;
    }
    public void setBalDate(Date balDate) {
        this.balDate = balDate;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }
    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }
    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }
    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }
}
