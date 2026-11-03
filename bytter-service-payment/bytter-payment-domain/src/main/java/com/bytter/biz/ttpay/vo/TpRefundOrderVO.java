package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: TpRefundManageVO
 * @Description: 退款信息VO
 * @author wangzy
 * @date 2019/10/10 14:12
 *
 */
public class TpRefundOrderVO implements java.io.Serializable,  IBusinessKey {
    private static final long serialVersionUID = 1L;
    /**退款申请数据Id**/
    private String id;
    /**退款来源**/
    @BtCompositeSearch(fieldName = "t.ret_source")
    private String retSource;
    /**退款方式**/
    @Dict(dictGroup = TtPayConstant.TTPAY_PAYTYPE_DICT, inject = "payTypeName")
    private String payType;
    @BtCompositeSearch(fieldName = "d.payType")
    private String payTypeName;
    /**退款申请号**/
    @BtCompositeSearch(fieldName = "t.back_No")
    private String backNo;
    /**单位ID	CORP_ID	VARCHAR2(32)**/
    @BtCompositeSearch(fieldName = "m.corp_id")
    private String corpId;
    /**单位名称**/
    @BtCompositeSearch(fieldName = "m.corp_id")
    private String corpCode;
    /**单位名称**/
    @BtCompositeSearch(fieldName = "m.corp_id")
    private String corpName;
    /**商户订单号**/
    @BtCompositeSearch(fieldName = "d.trandNo")
    private String trandNo;
    /**交易流水号**/
    @BtCompositeSearch(fieldName = "d.serialNo")
    private String serialNo;
    /**商户号**/
    @BtCompositeSearch(fieldName = "d.mchId")
    private String mchId;
    /**门店名称**/
    @BtCompositeSearch(fieldName = "a.shop_Name")
    private String shopName;
    /**客户名称**/
    private String custName;
    /**退款申请时间**/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName = "t.create_Date")
    private Date backDate;
    /**交易金额**/
    @BtCompositeSearch(fieldName = "d.trandMoney")
    private BigDecimal trandMoney;
    /**退款金额**/
    @BtCompositeSearch(fieldName = "t.back_Money")
    private BigDecimal backMoney;
    /**可退款金额**/
    @BtCompositeSearch(fieldName = "t.refundable_Amount")
    private BigDecimal refundableAmount;
    //@Dict(dictGroup = "TTP_REFUND_APPROVE_STATUS", inject = "appStatusName")
    private String appStatus;
    @BtCompositeSearch(fieldName = "t.app_status")
    private String appStatusName;
    /**退款复核状态**/
    @BtCompositeSearch(fieldName = "t.status")
    //@Dict(dictGroup = "TTPAY_REFUND_CHECKSTATUS", inject = "statusName")
    private String status;
    private String statusName;
    /**退款状态**/
    @BtCompositeSearch(fieldName = "t.voucher_Stat")
    private String voucherStat;
    /**结果返回时间**/
    @BtCompositeSearch(fieldName = "t.update_Date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**失败原因**/
    private String returnMsg;
    /**打回原因**/
    private String msg;
    /**发起人**/
    @BtCompositeSearch(fieldName = "t.create_Name")
    private String createName;
    /**确认人**/
    @BtCompositeSearch(fieldName = "t.check_Name")
    private String checkName;
    /**币种**/
    private String currency;
    /**交易流水Id**/
    private String dtlId;
    /**备注**/
    private String rmk;
    /**退款申请人名称**/
    private String createBy;
    /**退款申请人和名称**/
    private String createByAndName;
    /**订单日期**/
    private Date orderDate;
    /**已收金额**/
    private BigDecimal recMoney;
    /**客户账号**/
    private String custAcc;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRetSource() {
        return retSource;
    }
    public void setRetSource(String retSource) {
        this.retSource = retSource;
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
    public String getBackNo() {
        return backNo;
    }
    public void setBackNo(String backNo) {
        this.backNo = backNo;
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
    public String getTrandNo() {
        return trandNo;
    }
    public void setTrandNo(String trandNo) {
        this.trandNo = trandNo;
    }
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getMchId() {
        return mchId;
    }
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public Date getBackDate() {
        return backDate;
    }
    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }
    public BigDecimal getTrandMoney() {
        return trandMoney;
    }
    public void setTrandMoney(BigDecimal trandMoney) {
        this.trandMoney = trandMoney;
    }
    public BigDecimal getBackMoney() {
        return backMoney;
    }
    public void setBackMoney(BigDecimal backMoney) {
        this.backMoney = backMoney;
    }
    public BigDecimal getRefundableAmount() {
        return refundableAmount;
    }
    public void setRefundableAmount(BigDecimal refundableAmount) {
        this.refundableAmount = refundableAmount;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public String getCheckName() {
        return checkName;
    }
    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getDtlId() {
        return dtlId;
    }
    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCreateByAndName() {
        return createByAndName;
    }
    public void setCreateByAndName(String createByAndName) {
        this.createByAndName = createByAndName;
    }
    public Date getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    public BigDecimal getRecMoney() {
        return recMoney;
    }
    public void setRecMoney(BigDecimal recMoney) {
        this.recMoney = recMoney;
    }
    public String getCustAcc() {
        return custAcc;
    }
    public void setCustAcc(String custAcc) {
        this.custAcc = custAcc;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
