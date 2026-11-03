package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author 郑刚
 * @version 1.0
 * @Description 各支付方式流水查询VO
 * @date 2019-09-30 15:43:05
 */
public class TpPayBillDtlVO implements java.io.Serializable {
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
     * 应用id
     **/
    @BtCompositeSearch(fieldName = "d.appId")
    private String appId;
    /**
     * 商户号
     **/
    @BtCompositeSearch(fieldName = "d.mchId")
    private String mchId;
    /**
     * 交易流水号
     **/
    @BtCompositeSearch(fieldName = "d.serialNo")
    private String serialNo;
    /**
     * 订单号
     **/
    @BtCompositeSearch(fieldName = "d.trandNo")
    private String trandNo;
    /**
     * 对方账户
     **/
    @BtCompositeSearch(fieldName = "d.payAcc")
    private String payAcc;
    /**
     * 交易日期
     **/
    @BtCompositeSearch(fieldName = "d.trandTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date trandTime;
    /**
     * 退款金额
     **/
    private BigDecimal backMoney;
    /**
     * 交易金额
     **/
    @BtCompositeSearch(fieldName = "d.trandMoney")
    private BigDecimal trandMoney;
    /**
     * 退款审批状态
     **/
    //@Dict(dictGroup = "TTP_REFUND_APPROVE_STATUS", inject = "appStatusName")
    @BtCompositeSearch(fieldName = "t.app_Status")
    private String appStatus;
    private String appStatusName;
    /**
     * 退款ID
     **/
    private String refundId;
    /**
     * 已退款金额
     **/
    @BtCompositeSearch(fieldName = "o.refundedMoney")
    private BigDecimal refundedMoney;
    /**
     * 退款申请备注
     **/
    private String rmk;
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getMchId() {
        return mchId;
    }
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getTrandNo() {
        return trandNo;
    }
    public void setTrandNo(String trandNo) {
        this.trandNo = trandNo;
    }
    public String getPayAcc() {
        return payAcc;
    }
    public void setPayAcc(String payAcc) {
        this.payAcc = payAcc;
    }
    public Date getTrandTime() {
        return trandTime;
    }
    public void setTrandTime(Date trandTime) {
        this.trandTime = trandTime;
    }
    public BigDecimal getTrandMoney() {
        return trandMoney;
    }
    public void setTrandMoney(BigDecimal trandMoney) {
        this.trandMoney = trandMoney;
    }
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
    public String getPayTypeName() {
        return payTypeName;
    }
    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
    public BigDecimal getBackMoney() {
        return backMoney;
    }
    public void setBackMoney(BigDecimal backMoney) {
        this.backMoney = backMoney;
    }
    public String getAppStatus() {
        return appStatus;
    }
    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }
    public String getAppStatusName() {
        return appStatusName;
    }
    public void setAppStatusName(String appStatusName) {
        this.appStatusName = appStatusName;
    }
    public BigDecimal getRefundedMoney() {
        return refundedMoney;
    }
    public void setRefundedMoney(BigDecimal refundedMoney) {
        this.refundedMoney = refundedMoney;
    }
    public String getRefundId() {
        return refundId;
    }
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
