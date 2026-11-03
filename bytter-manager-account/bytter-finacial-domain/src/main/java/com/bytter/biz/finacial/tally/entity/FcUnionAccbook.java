package com.bytter.biz.finacial.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 网点往来账务信息表
 * @author onlineGenerator
 * @date 2017-07-13 10:02:21
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_UNION_ACCBOOK", schema = "")
@SuppressWarnings("serial")
public class FcUnionAccbook extends IdEntity implements java.io.Serializable {
	/**发送方网点*/
	private String sendNet;
	/**发送方账号*/
	private String sendAcc;
	/**发送方凭证号*/
	private String sendVoucher;
	/**记账方向*/
	private Integer way;
	/**金额*/
	private java.math.BigDecimal sendMoney;
	/**摘要*/
	private String summaryContent;
	/**凭证种类*/
	private String voucherType;
	/**接收方网点*/
	private String receiveNet;
	/**接收方账号*/
	private String receiveAcc;
	/**外单位*/
	private String externalCorp;
	/**外单位账号*/
	private String externalAcc;
	/**外单位开户行*/
	private String bank;
	/**外单位信息省*/
	private String externalProv;
	/**外单位信息市*/
	private String externalCity;
	/**接收方流水号*/
	private String receiveOrder;
	/**往来状态*/
	private String billState;
	/**委托收款的收款单位*/
	private String startCorp;
	/**委托收款的收款账号*/
	private String startCorpAcc;
	/**委托收款的收款银行*/
	private String startBank;
	/**返回信息*/
	private String returnMsg;
	/**发送凭证日期*/
	private java.util.Date sendVoucherDate;
	/**发起方业务全局唯一ID*/
	private String sendBusGlobalId;
	/**业务来源类型*/
	private Integer busOriType;
	/**接收方业务全局唯一ID*/
	private String receiveBusGlobalId;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	/**记账员代码*/
	private String operatorCode;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发送方网点
	 */
	@Column(name ="SEND_NET")
	public String getSendNet(){
		return this.sendNet;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发送方网点
	 */
	public void setSendNet(String sendNet){
		this.sendNet = sendNet;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发送方账号
	 */
	@Column(name ="SEND_ACC")
	public String getSendAcc(){
		return this.sendAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发送方账号
	 */
	public void setSendAcc(String sendAcc){
		this.sendAcc = sendAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发送方凭证号
	 */
	@Column(name ="SEND_VOUCHER")
	public String getSendVoucher(){
		return this.sendVoucher;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发送方凭证号
	 */
	public void setSendVoucher(String sendVoucher){
		this.sendVoucher = sendVoucher;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  记账方向
	 */
	@Column(name ="WAY")
	public Integer getWay(){
		return this.way;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  记账方向
	 */
	public void setWay(Integer way){
		this.way = way;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="SEND_MONEY")
	public java.math.BigDecimal getSendMoney(){
		return this.sendMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setSendMoney(java.math.BigDecimal sendMoney){
		this.sendMoney = sendMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="SUMMARY_CONTENT")
	public String getSummaryContent(){
		return this.summaryContent;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setSummaryContent(String summaryContent){
		this.summaryContent = summaryContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证种类
	 */
	@Column(name ="VOUCHER_TYPE")
	public String getVoucherType(){
		return this.voucherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类
	 */
	public void setVoucherType(String voucherType){
		this.voucherType = voucherType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接收方网点
	 */
	@Column(name ="RECEIVE_NET")
	public String getReceiveNet(){
		return this.receiveNet;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接收方网点
	 */
	public void setReceiveNet(String receiveNet){
		this.receiveNet = receiveNet;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接收方账号
	 */
	@Column(name ="RECEIVE_ACC")
	public String getReceiveAcc(){
		return this.receiveAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接收方账号
	 */
	public void setReceiveAcc(String receiveAcc){
		this.receiveAcc = receiveAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位
	 */
	@Column(name ="EXTERNAL_CORP")
	public String getExternalCorp(){
		return this.externalCorp;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位
	 */
	public void setExternalCorp(String externalCorp){
		this.externalCorp = externalCorp;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位账号
	 */
	@Column(name ="EXTERNAL_ACC")
	public String getExternalAcc(){
		return this.externalAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位账号
	 */
	public void setExternalAcc(String externalAcc){
		this.externalAcc = externalAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位开户行
	 */
	@Column(name ="BANK")
	public String getBank(){
		return this.bank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位开户行
	 */
	public void setBank(String bank){
		this.bank = bank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位信息省
	 */
	@Column(name ="EXTERNAL_PROV")
	public String getExternalProv(){
		return this.externalProv;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位信息省
	 */
	public void setExternalProv(String externalProv){
		this.externalProv = externalProv;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位信息市
	 */
	@Column(name ="EXTERNAL_CITY")
	public String getExternalCity(){
		return this.externalCity;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位信息市
	 */
	public void setExternalCity(String externalCity){
		this.externalCity = externalCity;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接收方流水号
	 */
	@Column(name ="RECEIVE_ORDER")
	public String getReceiveOrder(){
		return this.receiveOrder;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接收方流水号
	 */
	public void setReceiveOrder(String receiveOrder){
		this.receiveOrder = receiveOrder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  往来状态
	 */
	@Column(name ="BILL_STATE")
	public String getBillState(){
		return this.billState;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  往来状态
	 */
	public void setBillState(String billState){
		this.billState = billState;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款的收款单位
	 */
	@Column(name ="START_CORP")
	public String getStartCorp(){
		return this.startCorp;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款的收款单位
	 */
	public void setStartCorp(String startCorp){
		this.startCorp = startCorp;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款的收款账号
	 */
	@Column(name ="START_CORP_ACC")
	public String getStartCorpAcc(){
		return this.startCorpAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款的收款账号
	 */
	public void setStartCorpAcc(String startCorpAcc){
		this.startCorpAcc = startCorpAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款的收款银行
	 */
	@Column(name ="START_BANK")
	public String getStartBank(){
		return this.startBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款的收款银行
	 */
	public void setStartBank(String startBank){
		this.startBank = startBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  返回信息
	 */
	@Column(name ="RETURN_MSG")
	public String getReturnMsg(){
		return this.returnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  返回信息
	 */
	public void setReturnMsg(String returnMsg){
		this.returnMsg = returnMsg;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  发送凭证日期
	 */
	@Column(name ="SEND_VOUCHER_DATE")
	public java.util.Date getSendVoucherDate(){
		return this.sendVoucherDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  发送凭证日期
	 */
	public void setSendVoucherDate(java.util.Date sendVoucherDate){
		this.sendVoucherDate = sendVoucherDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发起方业务全局唯一ID
	 */
	@Column(name ="SEND_BUS_GLOBAL_ID")
	public String getSendBusGlobalId(){
		return this.sendBusGlobalId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发起方业务全局唯一ID
	 */
	public void setSendBusGlobalId(String sendBusGlobalId){
		this.sendBusGlobalId = sendBusGlobalId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务来源类型
	 */
	@Column(name ="BUS_ORI_TYPE")
	public Integer getBusOriType(){
		return this.busOriType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务来源类型
	 */
	public void setBusOriType(Integer busOriType){
		this.busOriType = busOriType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接收方业务全局唯一ID
	 */
	@Column(name ="RECEIVE_BUS_GLOBAL_ID")
	public String getReceiveBusGlobalId(){
		return this.receiveBusGlobalId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接收方业务全局唯一ID
	 */
	public void setReceiveBusGlobalId(String receiveBusGlobalId){
		this.receiveBusGlobalId = receiveBusGlobalId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME")
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Column(name ="UPDATE_TIME")
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员代码
	 */
	@Column(name ="OPERATOR_CODE")
	public String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员代码
	 */
	public void setOperatorCode(String operatorCode){
		this.operatorCode = operatorCode;
	}
}
