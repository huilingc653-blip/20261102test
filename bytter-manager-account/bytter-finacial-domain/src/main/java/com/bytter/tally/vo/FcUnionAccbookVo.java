package com.bytter.tally.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
/**   
 * @Title: Entity
 * @Description: 网点往来账务信息表
 * @author onlineGenerator
 * @date 2017-07-13 10:02:21
 * @version 1.0
 *
 */
public class FcUnionAccbookVo  implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/**发送方网点*/
	@BtCompositeSearch(fieldName = "a.send_Net")
	@Corp(inject = "sendNetName")
	private String sendNet;
	private String sendNetName;
	
	/**发送方账号*/
	private String sendAcc;
	/**发送方凭证号*/
	
	private String sendVoucher;
	
	/**记账方向*/
	@BtCompositeSearch(fieldName = "a.way")
	@Dict(dictGroup="Admin.BalanceWay" , inject="wayName")
	private Integer way;
	private String wayName;
	
	/**金额*/
	@BtCompositeSearch(fieldName = "a.send_Money")
	private java.math.BigDecimal sendMoney;
	/**摘要*/
	private String summaryContent;
	/**凭证种类*/
	@BtCompositeSearch(fieldName = "a.voucher_type")
	private String voucherType;
	@BtCompositeSearch(fieldName = "b.voucher_name")
	private String voucherName;
	
	/**接收方网点*/
	@BtCompositeSearch(fieldName = "a.receive_Net")
	@Corp(inject = "receiveNetName")
	private String receiveNet;
	private String receiveNetName;
	
	/**接收方账号*/
	private String receiveAcc;
	/**外单位*/
	@BtCompositeSearch(fieldName = "a.external_Corp")
	private String externalCorp;
	/**外单位账号*/
	@BtCompositeSearch(fieldName = "a.external_Acc")
	private String externalAcc;
	/**外单位开户行*/
	@BtCompositeSearch(fieldName = "a.bank")
	private String bank;
	/**外单位信息省*/
	private String externalProv;
	private String provInputName;
	/**外单位信息市*/
	private String externalCity;
	private String cityName;
	/**接收方流水号*/
	private String receiveOrder;
	/**往来状态*/
	@BtCompositeSearch(fieldName = "a.bill_state")
	@Dict(dictGroup="fcUnionAccbook.billState" , inject="billName")
	private String billState;
	private String billName;
	
	/**委托收款的收款单位*/
	private String startCorp;
	/**委托收款的收款账号*/
	private String startCorpAcc;
	/**委托收款的收款银行*/
	private String startBank;
	/**返回信息*/
	private String returnMsg;
	/**发送凭证日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date sendVoucherDate;
	/**发起方业务全局唯一ID*/
	private String sendBusGlobalId;
	/**业务来源类型*/
	private Integer busOriType;
	/**接收方业务全局唯一ID*/
	private String receiveBusGlobalId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSendNet() {
		return sendNet;
	}
	public void setSendNet(String sendNet) {
		this.sendNet = sendNet;
	}
	public String getSendAcc() {
		return sendAcc;
	}
	public void setSendAcc(String sendAcc) {
		this.sendAcc = sendAcc;
	}
	public String getSendVoucher() {
		return sendVoucher;
	}
	public void setSendVoucher(String sendVoucher) {
		this.sendVoucher = sendVoucher;
	}
	public Integer getWay() {
		return way;
	}
	public void setWay(Integer way) {
		this.way = way;
	}
	public java.math.BigDecimal getSendMoney() {
		return sendMoney;
	}
	public void setSendMoney(java.math.BigDecimal sendMoney) {
		this.sendMoney = sendMoney;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getReceiveNet() {
		return receiveNet;
	}
	public void setReceiveNet(String receiveNet) {
		this.receiveNet = receiveNet;
	}
	public String getReceiveAcc() {
		return receiveAcc;
	}
	public void setReceiveAcc(String receiveAcc) {
		this.receiveAcc = receiveAcc;
	}
	public String getExternalCorp() {
		return externalCorp;
	}
	public void setExternalCorp(String externalCorp) {
		this.externalCorp = externalCorp;
	}
	public String getExternalAcc() {
		return externalAcc;
	}
	public void setExternalAcc(String externalAcc) {
		this.externalAcc = externalAcc;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getExternalProv() {
		return externalProv;
	}
	public void setExternalProv(String externalProv) {
		this.externalProv = externalProv;
	}
	public String getExternalCity() {
		return externalCity;
	}
	public void setExternalCity(String externalCity) {
		this.externalCity = externalCity;
	}
	public String getReceiveOrder() {
		return receiveOrder;
	}
	public void setReceiveOrder(String receiveOrder) {
		this.receiveOrder = receiveOrder;
	}
	public String getBillState() {
		return billState;
	}
	public void setBillState(String billState) {
		this.billState = billState;
	}
	public String getStartCorp() {
		return startCorp;
	}
	public void setStartCorp(String startCorp) {
		this.startCorp = startCorp;
	}
	public String getStartCorpAcc() {
		return startCorpAcc;
	}
	public void setStartCorpAcc(String startCorpAcc) {
		this.startCorpAcc = startCorpAcc;
	}
	public String getStartBank() {
		return startBank;
	}
	public void setStartBank(String startBank) {
		this.startBank = startBank;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public java.util.Date getSendVoucherDate() {
		return sendVoucherDate;
	}
	public void setSendVoucherDate(java.util.Date sendVoucherDate) {
		this.sendVoucherDate = sendVoucherDate;
	}
	public String getSendBusGlobalId() {
		return sendBusGlobalId;
	}
	public void setSendBusGlobalId(String sendBusGlobalId) {
		this.sendBusGlobalId = sendBusGlobalId;
	}
	public Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(Integer busOriType) {
		this.busOriType = busOriType;
	}
	public String getReceiveBusGlobalId() {
		return receiveBusGlobalId;
	}
	public void setReceiveBusGlobalId(String receiveBusGlobalId) {
		this.receiveBusGlobalId = receiveBusGlobalId;
	}
	public String getSendNetName() {
		return sendNetName;
	}
	public void setSendNetName(String sendNetName) {
		this.sendNetName = sendNetName;
	}
	public String getWayName() {
		return wayName;
	}
	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getReceiveNetName() {
		return receiveNetName;
	}
	public void setReceiveNetName(String receiveNetName) {
		this.receiveNetName = receiveNetName;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getProvInputName() {
		return provInputName;
	}
	public void setProvInputName(String provInputName) {
		this.provInputName = provInputName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	
	
}
