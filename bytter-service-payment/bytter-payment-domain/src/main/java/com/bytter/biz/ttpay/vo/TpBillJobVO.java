package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
/**
 * @author 郑刚
 * @version 1.0
 * @Title: VO
 * @Description: 商户信息VO
 * @date 2019-09-26 15:43:05
 */
public class TpBillJobVO implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 单位ID	CORP_ID	VARCHAR2(32)
     **/
    @BtCompositeSearch(fieldName = "m.corp_id")
    private String corpId;
    /**
     * 单位名称
     **/
    private String corpCode;
    /**
     * 单位名称
     **/
    @BtCompositeSearch(fieldName = "m.corp_id")
    private String corpName;
    /**
     * 商户号	MERCHANT_NO	VARCHAR2(255)
     **/
    @BtCompositeSearch(fieldName = "m.merchant_No")
    private String merchantNo;
    //@Dict(dictGroup = TtPayConstant.TTPAY_PAYTYPE_DICT, inject = "payTypeName")
    private String payType;
    /**
     * 第三方支付方式名称
     **/
    @BtCompositeSearch(fieldName = "m.pay_Type")
    private String payTypeName;
    //@Dict(dictGroup = TtPayConstant.TTPAY_BILLJOB_STATUS_DICT, inject = "statusName")
    private Integer status;
    @BtCompositeSearch(fieldName = "j.status")
    private String statusName;
    @BtCompositeSearch(fieldName = "j.create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @BtCompositeSearch(fieldName = "j.bill_date")
    private String billDate;
    private String appId;
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
    public String getMerchantNo() {
        return merchantNo;
    }
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getBillDate() {
        return billDate;
    }
    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    @Override
    public String toString() {
        return "TpBillJobVO [id=" + id + ", corpId=" + corpId + ", corpCode=" + corpCode + ", corpName=" + corpName
                + ", merchantNo=" + merchantNo + ", payType=" + payType + ", payTypeName=" + payTypeName + ", status="
                + status + ", statusName=" + statusName + ", createTime=" + createTime + ", billDate=" + billDate + "]";
    }
}
