package com.bytter.biz.ttpay.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: TpRefundOrder
 * @Description: 退款信息entity
 * @author wangzy
 * @date 2019/10/11 14:56
 *
 */
@Entity
@Table(name = "TP_REFUND_ORDER", schema = "")
public class TpRefundOrder extends IdEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    /**单位ID**/
    private String corpId;
    /**	门店Id**/
    private String shopId;
    /**商户号**/
    private String merchantNo;
    /**退款申请单号**/
    private String backNo;
    /**退款申请时间**/
    private Date backDate;
    /**退款金额**/
    private BigDecimal backMoney;
    /**可退款金额**/
    private BigDecimal refundableAmount;
    /**第三方系统交易流水号**/
    private String transactionId;
    /**订单号**/
    private String orderNo;
    /**订单交易日期**/
    private Date orderDate;
    /**订单金额**/
    private BigDecimal orderMoney;
    /**已收金额**/
    private BigDecimal recMoney;
    /**第三方支付方式代码**/
    private String payType;
    /**客户名称**/
    private String custName;
    /**客户账号**/
    private String custAcc;
    /**备注**/
    private String rmk;
    /**退款审批状态**/
    private String appStatus;
    /**退款复核状态**/
    private String status;
    /**退款单来源**/
    private String retSource;
    /**打回原因**/
    private String msg;
    /**银行接口发送状态**/
    private String voucherStat;
    /**银行处理失败原因**/
    private String returnMsg;
    /**创建人名称**/
    private String createName;
    /**创建人登录名称	CREATE_BY	VARCHAR2(50)**/
    private String createBy;
    /**创建日期	CREATE_DATE	TIMESTAMP**/
    private Date createDate;
    /**更新人名称	UPDATE_NAME	VARCHAR2(50)**/
    private String updateName;
    /**更新人登录名称	UPDATE_BY	VARCHAR2(50)**/
    private String updateBy;
    /**更新日期	UPDATE_DATE	TIMESTAMP**/
    private Date updateDate;
    /**银行代码**/
    private String bifCode;
    /**退款类型**/
    private String refundType;
    /**复核人名称**/
    private String checkName;
    /**复核人登录名称**/
    private String checkBy;
    /**复核日期**/
    private Date checkDate;
    /**交易流水表Id**/
    private String dtlId;
    @Column(name ="corp_id")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="shop_id")
    public String getShopId() {
        return shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    @Column(name ="merchant_no")
    public String getMerchantNo() {
        return merchantNo;
    }
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
    @Column(name ="back_no")
    public String getBackNo() {
        return backNo;
    }
    public void setBackNo(String backNo) {
        this.backNo = backNo;
    }
    @Column(name ="back_date")
    public Date getBackDate() {
        return backDate;
    }
    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }
    @Column(name ="back_money")
    public BigDecimal getBackMoney() {
        return backMoney;
    }
    public void setBackMoney(BigDecimal backMoney) {
        this.backMoney = backMoney;
    }
    @Column(name ="refundable_amount")
    public BigDecimal getRefundableAmount() {
        return refundableAmount;
    }
    public void setRefundableAmount(BigDecimal refundableAmount) {
        this.refundableAmount = refundableAmount;
    }
    @Column(name ="transaction_id")
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    @Column(name ="order_no")
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    @Column(name ="order_date")
    public Date getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    @Column(name ="order_money")
    public BigDecimal getOrderMoney() {
        return orderMoney;
    }
    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }
    @Column(name ="rec_money")
    public BigDecimal getRecMoney() {
        return recMoney;
    }
    public void setRecMoney(BigDecimal recMoney) {
        this.recMoney = recMoney;
    }
    @Column(name ="pay_type")
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    @Column(name ="cust_name")
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    @Column(name ="cust_acc")
    public String getCustAcc() {
        return custAcc;
    }
    public void setCustAcc(String custAcc) {
        this.custAcc = custAcc;
    }
    @Column(name ="rmk")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name ="app_status")
    public String getAppStatus() {
        return appStatus;
    }
    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }
    @Column(name ="status")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name ="ret_source")
    public String getRetSource() {
        return retSource;
    }
    public void setRetSource(String retSource) {
        this.retSource = retSource;
    }
    @Column(name ="msg")
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    @Column(name ="voucher_stat")
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    @Column(name ="return_msg")
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    @Column(name ="create_name")
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    @Column(name ="create_by")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="create_date")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="update_name")
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    @Column(name ="update_by")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="update_date")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name ="bif_code")
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    @Column(name ="refund_type")
    public String getRefundType() {
        return refundType;
    }
    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }
    @Column(name ="check_name")
    public String getCheckName() {
        return checkName;
    }
    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }
    @Column(name ="check_date")
    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
    @Column(name ="dtl_id")
    public String getDtlId() {
        return dtlId;
    }
    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }
    @Column(name ="check_by")
    public String getCheckBy() {
        return checkBy;
    }
    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }
}
