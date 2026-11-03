package com.bytter.biz.finacial.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: BT_NET_CHALK
 * @author onlineGenerator
 * @date 2017-07-04 14:58:22
 * @version 1.0
 *
 */
@Entity
@Table(name = "BT_NET_CHALK", schema = "")
@SuppressWarnings("serial")
public class BtNetChalk extends IdEntity implements java.io.Serializable {
	/**交易日期*/
	private java.util.Date transTime;
	/**单位代码*/
	private String corpId;
	/**收支标识*/
	private String cdSign;
	/**币别代码*/
	private String curId;
	/**本方账号ID*/
	private String bankAccId;
	/**本方账号*/
	private String bankAcc;
	/**本方账号开户行*/
	private String bankName;
	/**本方账号名*/
	private String accName;
	/**本方内部账号*/
	private String cntAccount;
	/**对方账号*/
	private String oppAccNo;
	/**对方账号开户行*/
	private String oppBankName;
	/**对方账户名*/
	private String oppAccName;
	/**摘要*/
	private String purpose;
	/**金额*/
	private java.math.BigDecimal money;
	/**状态*/
	private String estate;
	/**记账员代码*/
	private String operatorCode;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**流水号*/
	private String orderNo;
	/**凭证号*/
	private String voucherNo;
	/**复核员代码*/
	private String checkerCode;
	/**复核日期*/
	private java.util.Date checkTime;
	/**银行流水号*/
	private String bankSerial;
	/**取消原因*/
	private String refuseWhy;
	/**是否勾兑标识*/
	private String blend;
	/**退款勾兑原因*/
	private String blendInfo;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	/**账务表ID*/
	private String bookId;
	/**全局业务id*/
	private String busGlobalId;
	/**待记账提醒*/
	private String remindSign;
	/**自动记账标识**/
	private String autoTallySign;
	/**
	 * 新增 corp_type 冗余字段
	 */
	private String corpType;
	@Column(name = "CORP_TYPE")
	public String getCorpType() {
		return corpType;
	}
	public void setCorpType(String corpType) {
		this.corpType = corpType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  交易日期
	 */
	@Column(name ="TRANS_TIME")
	public java.util.Date getTransTime(){
		return this.transTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  交易日期
	 */
	public void setTransTime(java.util.Date transTime){
		this.transTime = transTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位代码
	 */
	@Column(name ="CORP_ID")
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位代码
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收支标识
	 */
	@Column(name ="CD_SIGN")
	public String getCdSign(){
		return this.cdSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收支标识
	 */
	public void setCdSign(String cdSign){
		this.cdSign = cdSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_ID")
	public String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurId(String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本方账号ID
	 */
	@Column(name ="BANK_ACC_ID")
	public String getBankAccId(){
		return this.bankAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本方账号ID
	 */
	public void setBankAccId(String bankAccId){
		this.bankAccId = bankAccId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本方账号
	 */
	@Column(name ="BANK_ACC")
	public String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本方账号
	 */
	public void setBankAcc(String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本方账号开户行
	 */
	@Column(name ="BANK_NAME")
	public String getBankName(){
		return this.bankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本方账号开户行
	 */
	public void setBankName(String bankName){
		this.bankName = bankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本方账号名
	 */
	@Column(name ="ACC_NAME")
	public String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本方账号名
	 */
	public void setAccName(String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本方内部账号
	 */
	@Column(name ="CNT_ACCOUNT")
	public String getCntAccount(){
		return this.cntAccount;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本方内部账号
	 */
	public void setCntAccount(String cntAccount){
		this.cntAccount = cntAccount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对方账号
	 */
	@Column(name ="OPP_ACC_NO")
	public String getOppAccNo(){
		return this.oppAccNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对方账号
	 */
	public void setOppAccNo(String oppAccNo){
		this.oppAccNo = oppAccNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对方账号开户行
	 */
	@Column(name ="OPP_BANK_NAME")
	public String getOppBankName(){
		return this.oppBankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对方账号开户行
	 */
	public void setOppBankName(String oppBankName){
		this.oppBankName = oppBankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对方账户名
	 */
	@Column(name ="OPP_ACC_NAME")
	public String getOppAccName(){
		return this.oppAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对方账户名
	 */
	public void setOppAccName(String oppAccName){
		this.oppAccName = oppAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="PURPOSE")
	public String getPurpose(){
		return this.purpose;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setPurpose(String purpose){
		this.purpose = purpose;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="MONEY")
	public java.math.BigDecimal getMoney(){
		return this.money;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setMoney(java.math.BigDecimal money){
		this.money = money;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态
	 */
	@Column(name ="ESTATE")
	public String getEstate(){
		return this.estate;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态
	 */
	public void setEstate(String estate){
		this.estate = estate;
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
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记账日期
	 */
	@Column(name ="TALLY_DATE")
	public java.util.Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记账日期
	 */
	public void setTallyDate(java.util.Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水号
	 */
	@Column(name ="ORDER_NO")
	public String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水号
	 */
	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证号
	 */
	@Column(name ="VOUCHER_NO")
	public String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证号
	 */
	public void setVoucherNo(String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核员代码
	 */
	@Column(name ="CHECKER_CODE")
	public String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核员代码
	 */
	public void setCheckerCode(String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复核日期
	 */
	@Column(name ="CHECK_TIME")
	public java.util.Date getCheckTime(){
		return this.checkTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复核日期
	 */
	public void setCheckTime(java.util.Date checkTime){
		this.checkTime = checkTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行流水号
	 */
	@Column(name ="BANK_SERIAL")
	public String getBankSerial(){
		return this.bankSerial;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行流水号
	 */
	public void setBankSerial(String bankSerial){
		this.bankSerial = bankSerial;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  取消原因
	 */
	@Column(name ="REFUSE_WHY")
	public String getRefuseWhy(){
		return this.refuseWhy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  取消原因
	 */
	public void setRefuseWhy(String refuseWhy){
		this.refuseWhy = refuseWhy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否勾兑标识
	 */
	@Column(name ="BLEND")
	public String getBlend(){
		return this.blend;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否勾兑标识
	 */
	public void setBlend(String blend){
		this.blend = blend;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  退款勾兑原因
	 */
	@Column(name ="BLEND_INFO")
	public String getBlendInfo(){
		return this.blendInfo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  退款勾兑原因
	 */
	public void setBlendInfo(String blendInfo){
		this.blendInfo = blendInfo;
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
	@Column(name ="BOOK_ID")
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  全局业务ID
	 */
	@Column(name ="BUS_GLOBAL_ID")
	public String getBusGlobalId(){
		return this.busGlobalId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  全局业务ID
	 */
	public void setBusGlobalId(String busGlobalId){
		this.busGlobalId = busGlobalId;
	}
	@Column(name ="REMIND_SIGN")
	public String getRemindSign() {
		return remindSign;
	}
	public void setRemindSign(String remindSign) {
		this.remindSign = remindSign;
	}
	@Column(name ="AUTO_TALLY_SIGN")
	public String getAutoTallySign() {
		return autoTallySign;
	}
	public void setAutoTallySign(String autoTallySign) {
		this.autoTallySign = autoTallySign;
	}
}
