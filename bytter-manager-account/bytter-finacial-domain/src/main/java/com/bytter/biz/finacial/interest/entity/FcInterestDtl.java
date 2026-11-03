package com.bytter.biz.finacial.interest.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: FC_INTEREST_DTL
 * @author onlineGenerator
 * @date 2017-06-23 14:34:54
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_INTEREST_DTL", schema = "")
@SuppressWarnings("serial")
public class FcInterestDtl extends IdEntity implements java.io.Serializable {
	/**预提ID*/
	private java.lang.String predrawDtlId;
	/**计息日期*/
	private java.util.Date interestTime;
	/**账号*/
	private java.lang.String account;
	/**币别代码*/
	private java.lang.String curCode;
	/**利息类型*/
	private java.lang.String interestType;
	/**起息日期*/
	private java.util.Date interestStartDate;
	/**结息日期*/
	private java.util.Date blnInstDate;
	/**天数*/
	private java.lang.Integer days;
	/**积数*/
	private java.math.BigDecimal cumulation;
	/**利率*/
	private java.math.BigDecimal interestRate;
	/**个别计息的还款金额*/
	private java.math.BigDecimal repayMoney;
	/**利息*/
	private java.math.BigDecimal interest;
	/**计息标志*/
	private java.lang.String interestSign;
	/**备注*/
	private java.lang.String rmk;
	/**备注*/
	private java.lang.String generateMode;
	/**流水号*/
	private java.lang.String orderNo;
	/**电子回单号*/
	private java.lang.String eReceiptNo;
	/**业务状态*/
	private java.lang.Integer busStatus;
	/**经办人代码*/
	private java.lang.String operatorCode;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**复核人代码*/
	private java.lang.String checkerCode;
	/**复核日期*/
	private java.util.Date checkDate;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
    private java.lang.String singleId;
	/**
	 * 凭证号
	 */
	private String voucherNo;
	/**
	 * 新增冗余字段 ACC_NAME     ACC_TYPE     NET_ID    CORP_ID  CUR_ID   CUR_NAME
	 */
	private String accName;
	private String accType;
	private String netId;
	private String corpId;
	private String curId;
	private String curName;
	@Column(name = "ACC_NAME")
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Column(name = "ACC_TYPE")
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	@Column(name = "NET_ID")
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	@Column(name = "CORP_ID")
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name = "CUR_ID")
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name = "CUR_NAME")
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  预提ID
	 */
	@Column(name ="PREDRAW_DTL_ID")
	public java.lang.String getPredrawDtlId(){
		return this.predrawDtlId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预提ID
	 */
	public void setPredrawDtlId(java.lang.String predrawDtlId){
		this.predrawDtlId = predrawDtlId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  计息日期
	 */
	@Column(name ="INTEREST_TIME")
	public java.util.Date getInterestTime(){
		return this.interestTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  计息日期
	 */
	public void setInterestTime(java.util.Date interestTime){
		this.interestTime = interestTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="ACCOUNT")
	public java.lang.String getAccount(){
		return this.account;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setAccount(java.lang.String account){
		this.account = account;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_CODE")
	public java.lang.String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(java.lang.String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  利息类型
	 */
	@Column(name ="INTEREST_TYPE")
	public java.lang.String getInterestType(){
		return this.interestType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  利息类型
	 */
	public void setInterestType(java.lang.String interestType){
		this.interestType = interestType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  起息日期
	 */
	@Column(name ="INTEREST_START_DATE")
	public java.util.Date getInterestStartDate(){
		return this.interestStartDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  起息日期
	 */
	public void setInterestStartDate(java.util.Date interestStartDate){
		this.interestStartDate = interestStartDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结息日期
	 */
	@Column(name ="BLN_INST_DATE")
	public java.util.Date getBlnInstDate(){
		return this.blnInstDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结息日期
	 */
	public void setBlnInstDate(java.util.Date blnInstDate){
		this.blnInstDate = blnInstDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  天数
	 */
	@Column(name ="DAYS")
	public java.lang.Integer getDays(){
		return this.days;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  天数
	 */
	public void setDays(java.lang.Integer days){
		this.days = days;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  积数
	 */
	@Column(name ="CUMULATION")
	public java.math.BigDecimal getCumulation(){
		return this.cumulation;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  积数
	 */
	public void setCumulation(java.math.BigDecimal cumulation){
		this.cumulation = cumulation;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利率
	 */
	@Column(name ="INTEREST_RATE")
	public java.math.BigDecimal getInterestRate(){
		return this.interestRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利率
	 */
	public void setInterestRate(java.math.BigDecimal interestRate){
		this.interestRate = interestRate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  个别计息的还款金额
	 */
	@Column(name ="REPAY_MONEY")
	public java.math.BigDecimal getRepayMoney(){
		return this.repayMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  个别计息的还款金额
	 */
	public void setRepayMoney(java.math.BigDecimal repayMoney){
		this.repayMoney = repayMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利息
	 */
	@Column(name ="INTEREST")
	public java.math.BigDecimal getInterest(){
		return this.interest;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利息
	 */
	public void setInterest(java.math.BigDecimal interest){
		this.interest = interest;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计息标志
	 */
	@Column(name ="INTEREST_SIGN")
	public java.lang.String getInterestSign(){
		return this.interestSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计息标志
	 */
	public void setInterestSign(java.lang.String interestSign){
		this.interestSign = interestSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK")
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="GENERATE_MODE")
	public java.lang.String getGenerateMode(){
		return this.generateMode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setGenerateMode(java.lang.String generateMode){
		this.generateMode = generateMode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水号
	 */
	@Column(name ="ORDER_NO")
	public java.lang.String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水号
	 */
	public void setOrderNo(java.lang.String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单号
	 */
	@Column(name ="E_RECEIPT_NO")
	public java.lang.String getEReceiptNo(){
		return this.eReceiptNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单号
	 */
	public void setEReceiptNo(java.lang.String eReceiptNo){
		this.eReceiptNo = eReceiptNo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务状态
	 */
	@Column(name ="BUS_STATUS")
	public java.lang.Integer getBusStatus(){
		return this.busStatus;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务状态
	 */
	public void setBusStatus(java.lang.Integer busStatus){
		this.busStatus = busStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  经办人代码
	 */
	@Column(name ="OPERATOR_CODE")
	public java.lang.String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  经办人代码
	 */
	public void setOperatorCode(java.lang.String operatorCode){
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
	 *@return: java.lang.String  复核人代码
	 */
	@Column(name ="CHECKER_CODE")
	public java.lang.String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核人代码
	 */
	public void setCheckerCode(java.lang.String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复核日期
	 */
	@Column(name ="CHECK_DATE")
	public java.util.Date getCheckDate(){
		return this.checkDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复核日期
	 */
	public void setCheckDate(java.util.Date checkDate){
		this.checkDate = checkDate;
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
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(java.lang.String createBy){
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
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	@Column(name = "VOUCHER_NO")
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
    @Column(name ="SINGLE_ID")
    public String getSingleId() {
        return singleId;
    }
    public void setSingleId(String singleId) {
        this.singleId = singleId;
    }
}
