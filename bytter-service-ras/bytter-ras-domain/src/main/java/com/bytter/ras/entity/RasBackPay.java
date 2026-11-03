package com.bytter.ras.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核退款管理实体类
 * @date 2019/5/9 13:40
 */
@Entity
@Table(name = "RAS_BACK_PAY", schema = "")
public class RasBackPay extends IdEntity implements java.io.Serializable{
    /**单位id*/
    private String corpId;
    /**门店id*/
    private String shopId;
    /**退款申请单号*/
    private String backNo;
    /**退款申请时间*/
    private Date backDate;
    /**退款金额*/
    private BigDecimal backMoney;
    /**订单号*/
    private String orderNo;
    /**订单交易时间*/
    private Date orderDate;
    /**订单金额*/
    private BigDecimal orderMoney;
    /**已收金额*/
    private BigDecimal recMoney;
    /**缴款方式*/
    private String payType;
    /**客户名称*/
    private String custName;
    /**客户帐号*/
    private String custAcc;
    /**备注*/
    private String rmk;
    /**退款状态*/
    private String status;
    /**打回原因*/
    private String msg;
    /**银行接口发送状态*/
    private String sendSign;
    /**银行处理失败原因*/
    private String returnMsg;
    /**创建人登录名称*/
    private String createBy;
    /**创建人名称*/
    private String createName;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新人名称*/
    private String updateName;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="CORP_ID",nullable=true)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="SHOP_ID",nullable=true)
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Column(name ="BACK_NO",nullable=true)
    public String getBackNo() {
        return backNo;
    }

    public void setBackNo(String backNo) {
        this.backNo = backNo;
    }

    @Column(name ="BACK_DATE",nullable=true)
    public Date getBackDate() {
        return backDate;
    }

    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }

    @Column(name ="BACK_MONEY",nullable=true)
    public BigDecimal getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(BigDecimal backMoney) {
        this.backMoney = backMoney;
    }

    @Column(name ="ORDER_NO",nullable=true)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name ="ORDER_DATE",nullable=true)
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Column(name ="ORDER_MONEY",nullable=true)
    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    @Column(name ="REC_MONEY",nullable=true)
    public BigDecimal getRecMoney() {
        return recMoney;
    }

    public void setRecMoney(BigDecimal recMoney) {
        this.recMoney = recMoney;
    }

    @Column(name ="PAY_TYPE",nullable=true)
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Column(name ="CUST_NAME",nullable=true)
    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    @Column(name ="CUST_ACC",nullable=true)
    public String getCustAcc() {
        return custAcc;
    }

    public void setCustAcc(String custAcc) {
        this.custAcc = custAcc;
    }

    @Column(name ="RMK",nullable=true)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="STATUS",nullable=true)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name ="MSG",nullable=true)
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Column(name ="SEND_SIGN",nullable=true)
    public String getSendSign() {
        return sendSign;
    }

    public void setSendSign(String sendSign) {
        this.sendSign = sendSign;
    }

    @Column(name ="RETURN_MSG",nullable=true)
    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    @Column(name ="CREATE_BY",nullable=true)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_NAME",nullable=true)
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY",nullable=true)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_NAME",nullable=true)
    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
