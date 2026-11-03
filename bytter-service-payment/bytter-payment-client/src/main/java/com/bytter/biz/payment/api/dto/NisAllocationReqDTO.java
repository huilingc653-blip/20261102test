package com.bytter.biz.payment.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Title: DTO
 * Description: NIS_ALLOCATION_REQ
 * @author wangzx
 * @since 2018-01-11 17:20:12
 * @version 1.0
 *
 */
public class NisAllocationReqDTO implements Serializable{
    private String id;
    /**单据号码*/
    private String billNo;
    /**资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴*/
    private Integer type;
    /**单位ID*/
    private String corpId;
    /**付方账号ID*/
    private String payerAccId;
    /**付方账号*/
    private String payerAcc;
    /**付方账号名称*/
    private String payerAccName;
    /**收方账号ID*/
    private String payeeAccId;
    /**收方账号*/
    private String payeeAcc;
    /**收方账号名称*/
    private String payeeAccName;
    /**币别ID*/
    private String curId;
    /**金额*/
    private java.math.BigDecimal money;
    /**期望支付日期*/
    private java.util.Date wishPayday;
    /**会计凭证号*/
    private String bursarBillNo;
    /**加急标志 1-是 0-否*/
    private String isQuick;
    /**跨行标识 0-同行 1-他行*/
    private String difBank;
    /**地域标识 0-同城 1-异地*/
    private String areaSign;
    /**用途*/
    private String purpose;
    /**记账日期*/
    private java.util.Date tallyDate;
    /**凭证流水号*/
    private String orderNo;
    /**记账凭证号码*/
    private String voucherNo;
    /**证书信息*/
    private String caInfoId;
    /**状态*/
    private Integer status;
    /**指令状态*/
    private String voucherStat;
    /**单据来源*/
    private String billFrom;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private java.util.Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private java.util.Date updateDate;
    /**附件ID*/
    private String attachmentId;
    /**ERP编号*/
    private String erpId;
	/**记账标志*/
	private String tallySign;
	/**记账员代码*/
	private String tallyCode;
    /**资金池标识 0-非资金池；1-资金池*/
    private String poolSign;
    /**收款账号对应的中心账号*/
    private String payeeCounterAcc;
    /**备注说明*/
    private String rmk;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getPayerAccId() {
        return payerAccId;
    }
    public void setPayerAccId(String payerAccId) {
        this.payerAccId = payerAccId;
    }
    public String getPayerAcc() {
        return payerAcc;
    }
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }
    public String getPayerAccName() {
        return payerAccName;
    }
    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }
    public String getPayeeAccId() {
        return payeeAccId;
    }
    public void setPayeeAccId(String payeeAccId) {
        this.payeeAccId = payeeAccId;
    }
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    public String getPayeeAccName() {
        return payeeAccName;
    }
    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public Date getWishPayday() {
        return wishPayday;
    }
    public void setWishPayday(Date wishPayday) {
        this.wishPayday = wishPayday;
    }
    public String getBursarBillNo() {
        return bursarBillNo;
    }
    public void setBursarBillNo(String bursarBillNo) {
        this.bursarBillNo = bursarBillNo;
    }
    public String getIsQuick() {
        return isQuick;
    }
    public void setIsQuick(String isQuick) {
        this.isQuick = isQuick;
    }
    public String getDifBank() {
        return difBank;
    }
    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }
    public String getAreaSign() {
        return areaSign;
    }
    public void setAreaSign(String areaSign) {
        this.areaSign = areaSign;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getCaInfoId() {
        return caInfoId;
    }
    public void setCaInfoId(String caInfoId) {
        this.caInfoId = caInfoId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVoucherStat() {
        return voucherStat;
    }

    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    public String getBillFrom() {
        return billFrom;
    }
    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
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
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getErpId() {
        return erpId;
    }
    public void setErpId(String erpId) {
        this.erpId = erpId;
    }
	public String getTallySign() {
		return tallySign;
	}
	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}
	public String getTallyCode() {
		return tallyCode;
	}
	public void setTallyCode(String tallyCode) {
		this.tallyCode = tallyCode;
	}
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    public String getPayeeCounterAcc() {
        return payeeCounterAcc;
    }
    public void setPayeeCounterAcc(String payeeCounterAcc) {
        this.payeeCounterAcc = payeeCounterAcc;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
