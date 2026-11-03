package com.bytter.ras.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核退款管理vo
 * @date 2019/5/9 13:40
 */
public class RasBackPayVO implements java.io.Serializable {
    private String id;
    /**门店id*/
    @BtCompositeSearch(fieldName = "shop_Id")
    private String shopId;
    /**门店名称*/
    @BtCompositeSearch(fieldName = "rs.name")
    private String shopName;
    /**单位id*/
    @BtCompositeSearch(fieldName = "corp_Id")
    private String corpId;
    /**单位代码*/
    private String corpCode;
    /**单位名称*/
    @BtCompositeSearch(fieldName = "sc.name")
    private String corpName;
    /**退款申请号*/
    @BtCompositeSearch(fieldName = "back_no")
    private String backNo;
    /**退款申请日期*/
    @BtCompositeSearch(fieldName = "back_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date backDate;
    /**退款金额*/
    @BtCompositeSearch(fieldName = "back_money")
    private BigDecimal backMoney;
    /**退款订单号*/
    @BtCompositeSearch(fieldName = "order_no")
    private String orderNo;
    /**订单交易日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "order_date")
    private Date orderDate;
    /**订单金额*/
    @BtCompositeSearch(fieldName = "order_money")
    private BigDecimal orderMoney;
    /**已收金额*/
    @BtCompositeSearch(fieldName = "rec_money")
    private BigDecimal recMoney;
    /**缴款方式*/
    @Dict(dictGroup = "RAS_PAY_TYPE",inject = "payTypeName")
    @BtCompositeSearch(fieldName = "pay_type")
    private String payType;
    @BtCompositeSearch(fieldName = "pay_type")
    private String payTypeName;

    /**客户名称*/
    @BtCompositeSearch(fieldName = "cust_Name")
    private String custName;
    /**客户账号*/
    @BtCompositeSearch(fieldName = "cust_Acc")
    private String custAcc;
    /**备注*/
    @BtCompositeSearch(fieldName = "rmk")
    private String rmk;
    /**状态*/
    @BtCompositeSearch(fieldName = "status")
    @Dict(dictGroup = "RAS_BACK_PAY_STATUS",inject = "statusName")
    private String status;
    @BtCompositeSearch(fieldName = "status")
    private String statusName;

    /**交易状态*/
    @BtCompositeSearch(fieldName = "send_sign")
    @Dict(dictGroup = "RAS_BACK_PAY_SEND_SIGN",inject = "sendSignName")
    private String sendSign;
    @BtCompositeSearch(fieldName = "send_sign")
    private String sendSignName;

    /**交易信息*/
    @BtCompositeSearch(fieldName = "return_msg")
    private String returnMsg;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        if(StringUtils.isNotEmpty(corpCode)
                &&StringUtils.isNotEmpty(corpName)){
            return "("+corpCode+")"+corpName;
        }
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getBackNo() {
        return backNo;
    }

    public void setBackNo(String backNo) {
        this.backNo = backNo;
    }

    public Date getBackDate() {
        return backDate;
    }

    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }

    public BigDecimal getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(BigDecimal backMoney) {
        this.backMoney = backMoney;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public BigDecimal getRecMoney() {
        return recMoney;
    }

    public void setRecMoney(BigDecimal recMoney) {
        this.recMoney = recMoney;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAcc() {
        return custAcc;
    }

    public void setCustAcc(String custAcc) {
        this.custAcc = custAcc;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendSign() {
        return sendSign;
    }

    public void setSendSign(String sendSign) {
        this.sendSign = sendSign;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSendSignName() {
        return sendSignName;
    }

    public void setSendSignName(String sendSignName) {
        this.sendSignName = sendSignName;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
}
