package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: 票据账户余额VO
 * @author zhengg
 * @date 2017-09-28 19:20:01
 * @version 1.0
 */
public class AbBillAccBalVO implements java.io.Serializable  {

	private static final long serialVersionUID = 268419642335938295L;

	public AbBillAccBalVO(){

	}

	public AbBillAccBalVO(String corpId, String bankId, String bankAcc, Date balDate, String curId){
		this.corpId=corpId;
		this.balDate=balDate;
		this.curId=curId;
		this.bankId=bankId;
		this.bankAcc=bankAcc;
		initBalMoney();
	}

	private void initBalMoney(){
		this.bal=new BigDecimal(0);
		this.availBal=new BigDecimal(0);
		this.tiedupBal=new BigDecimal(0);
		this.spBal=new BigDecimal(0);
		this.spAvailBal=new BigDecimal(0);
		this.spTiedupBal=new BigDecimal(0);
		this.ypBal=new BigDecimal(0);
		this.ypAvailBal=new BigDecimal(0);
		this.ypTiedupBal=new BigDecimal(0);
	}

	private String id;

	private Integer billType;

	/**票据流水ID*/
	private String recordId;
	/**流水标志0借1贷2冻结*/
	private String cdSign;
	/**有效标记0无效1有效*/
	private String validSign;

	/**票据流水类型*/
	private String briefId;

	/**银行账户ID*/
	private String bankId;

	/**银行账号*/
	@BtCompositeSearch(fieldName="B.BANK_ACC")
	private String bankAcc;
	/**单位ID*/
	@BtCompositeSearch(fieldName="C.ID")
	private String corpId;
	/**单位名称*/
	@BtCompositeSearch(fieldName="c.code")
	private String corpName;
	/**单位代码*/
	private String corpCode;
	/**币别ID*/
	@Currency(inject="curName")
	private String curId;
	/**
	 * 币别
	 */
	private java.lang.String curName;
	/**余额日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="B.BAL_DATE")
	private Date balDate;
	/**余额*/
	@BtCompositeSearch(fieldName="B.BAL")
	private BigDecimal bal;
	/**可用余额*/
	@BtCompositeSearch(fieldName="B.AVAIL_BAL")
	private BigDecimal availBal;
	/**占用金额*/
	@BtCompositeSearch(fieldName="B.TIEDUP_BAL")
	private BigDecimal tiedupBal;

	/**商票余额*/
	@BtCompositeSearch(fieldName="B.SP_BAL")
	private BigDecimal spBal;
	/**商票可用余额*/
	@BtCompositeSearch(fieldName="B.SP_AVAIL_BAL")
	private BigDecimal spAvailBal;
	/**商票占用金额*/
	@BtCompositeSearch(fieldName="B.SP_TIEDUP_BAL")
	private BigDecimal spTiedupBal;

	/**银票余额*/
	@BtCompositeSearch(fieldName="B.YP_BAL")
	private BigDecimal ypBal;
	/**银票可用余额*/
	@BtCompositeSearch(fieldName="B.YP_AVAIL_BAL")
	private BigDecimal ypAvailBal;
	/**银票占用金额*/
	@BtCompositeSearch(fieldName="B.YP_TIEDUP_BAL")
	private BigDecimal ypTiedupBal;

	/**票面金额*/
	private BigDecimal billMoney;
	/**金额*/
	private BigDecimal money;
	/**可用金额*/
	private BigDecimal availMoney;
	/**占用金额*/
	private BigDecimal tiedupMoney;


	/**商票余额*/
	private BigDecimal spMoney;
	/**商票可用余额*/
	private BigDecimal spAvailMoney;
	/**商票占用金额*/
	private BigDecimal spTiedupMoney;

	/**银票余额*/
	private BigDecimal ypMoney;
	/**银票可用余额*/
	private BigDecimal ypAvailMoney;
	/**银票占用金额*/
	private BigDecimal ypTiedupMoney;


	/**开始日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date beginDate;
	/**结束日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;

	/**银行账户信息**/
	private BtBankAccDTO bankAccDTO;

	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	private Map<String,AbBillAccBalTypeVO> billTypeBalVO=new HashMap<>();

	private String bankName;

	private String typeName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getBankId(){
		return this.bankId;
	}

	public BtBankAccDTO getBankAccDTO() {
		return bankAccDTO;
	}

	public void setBankAccDTO(BtBankAccDTO bankAccDTO) {
		this.bankAccDTO = bankAccDTO;
	}
	/**票据流水ID*/
	public String getRecordId() {
		return recordId;
	}
	/**票据流水ID*/
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: String  银行账户ID
	 */
	public void setBankId(String bankId){
		this.bankId = bankId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: String  银行账号
	 */
	public String getBankAcc(){
		return this.bankAcc;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: String  银行账号
	 */
	public void setBankAcc(String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: String  单位ID
	 */
	public String getCorpId(){
		return this.corpId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: String  单位ID
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: String  币别ID
	 */
	public String getCurId(){
		return this.curId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: String  币别ID
	 */
	public void setCurId(String curId){
		this.curId = curId;
	}

	public String getCurName() {
		return curName;
	}

	public void setCurName(String curName) {
		this.curName = curName;
	}

	/**
	 *方法: 取得Date
	 *@return: Date  余额日期
	 */
	public Date getBalDate(){
		return this.balDate;
	}

	/**
	 *方法: 设置Date
	 *@param: Date  余额日期
	 */
	public void setBalDate(Date balDate){
		this.balDate = balDate;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  余额
	 */
	public BigDecimal getBal(){
		return this.bal;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  余额
	 */
	public void setBal(BigDecimal bal){
		this.bal = bal;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  可用余额
	 */
	public BigDecimal getAvailBal(){
		return this.availBal;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  可用余额
	 */
	public void setAvailBal(BigDecimal availBal){
		this.availBal = availBal;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  占用金额
	 */
	public BigDecimal getTiedupBal(){
		return this.tiedupBal;
	}

	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  占用金额
	 */
	public void setTiedupBal(BigDecimal tiedupBal){
		this.tiedupBal = tiedupBal;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: String  创建人登录名称
	 */
	public String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  创建日期
	 */
	public Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置Date
	 *@param: Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: String  更新人登录名称
	 */
	public String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  更新日期
	 */
	public Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置Date
	 *@param: Date  更新日期
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}

	public BigDecimal getBillMoney() {
		return billMoney;
	}

	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
	}

	public BigDecimal getAvailMoney() {
		return availMoney;
	}

	public void setAvailMoney(BigDecimal availMoney) {
		this.availMoney = availMoney;
	}

	public BigDecimal getTiedupMoney() {
		return tiedupMoney;
	}

	public void setTiedupMoney(BigDecimal tiedupMoney) {
		this.tiedupMoney = tiedupMoney;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCdSign() {
		return cdSign;
	}

	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}

	public String getValidSign() {
		return validSign;
	}

	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}

	public String getBriefId() {
		return briefId;
	}

	public void setBriefId(String briefId) {
		this.briefId = briefId;
	}

	public BigDecimal getSpBal() {
		return spBal;
	}

	public void setSpBal(BigDecimal spBal) {
		this.spBal = spBal;
	}

	public BigDecimal getSpAvailBal() {
		return spAvailBal;
	}

	public void setSpAvailBal(BigDecimal spAvailBal) {
		this.spAvailBal = spAvailBal;
	}

	public BigDecimal getSpTiedupBal() {
		return spTiedupBal;
	}

	public void setSpTiedupBal(BigDecimal spTiedupBal) {
		this.spTiedupBal = spTiedupBal;
	}

	public BigDecimal getYpBal() {
		return ypBal;
	}

	public void setYpBal(BigDecimal ypBal) {
		this.ypBal = ypBal;
	}

	public BigDecimal getYpAvailBal() {
		return ypAvailBal;
	}

	public void setYpAvailBal(BigDecimal ypAvailBal) {
		this.ypAvailBal = ypAvailBal;
	}

	public BigDecimal getYpTiedupBal() {
		return ypTiedupBal;
	}

	public void setYpTiedupBal(BigDecimal ypTiedupBal) {
		this.ypTiedupBal = ypTiedupBal;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public BigDecimal getSpMoney() {
		return spMoney;
	}

	public void setSpMoney(BigDecimal spMoney) {
		this.spMoney = spMoney;
	}

	public BigDecimal getSpAvailMoney() {
		return spAvailMoney;
	}

	public void setSpAvailMoney(BigDecimal spAvailMoney) {
		this.spAvailMoney = spAvailMoney;
	}

	public BigDecimal getSpTiedupMoney() {
		return spTiedupMoney;
	}

	public void setSpTiedupMoney(BigDecimal spTiedupMoney) {
		this.spTiedupMoney = spTiedupMoney;
	}

	public BigDecimal getYpMoney() {
		return ypMoney;
	}

	public void setYpMoney(BigDecimal ypMoney) {
		this.ypMoney = ypMoney;
	}

	public BigDecimal getYpAvailMoney() {
		return ypAvailMoney;
	}

	public void setYpAvailMoney(BigDecimal ypAvailMoney) {
		this.ypAvailMoney = ypAvailMoney;
	}

	public BigDecimal getYpTiedupMoney() {
		return ypTiedupMoney;
	}

	public void setYpTiedupMoney(BigDecimal ypTiedupMoney) {
		this.ypTiedupMoney = ypTiedupMoney;
	}

	public Map<String, AbBillAccBalTypeVO> getBillTypeBalVO() {
		return billTypeBalVO;
	}

	public void setBillTypeBalVO(Map<String, AbBillAccBalTypeVO> billTypeBalVO) {
		this.billTypeBalVO = billTypeBalVO;
	}

	@Override
	public String toString() {
		return "AbBillAccBalVO{" +
				"id='" + id + '\'' +
				", billType=" + billType +
				", recordId='" + recordId + '\'' +
				", cdSign='" + cdSign + '\'' +
				", validSign='" + validSign + '\'' +
				", briefId='" + briefId + '\'' +
				", bankId='" + bankId + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", corpCode='" + corpCode + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", balDate=" + balDate +
				", bal=" + bal +
				", availBal=" + availBal +
				", tiedupBal=" + tiedupBal +
				", spBal=" + spBal +
				", spAvailBal=" + spAvailBal +
				", spTiedupBal=" + spTiedupBal +
				", ypBal=" + ypBal +
				", ypAvailBal=" + ypAvailBal +
				", ypTiedupBal=" + ypTiedupBal +
				", billMoney=" + billMoney +
				", money=" + money +
				", availMoney=" + availMoney +
				", tiedupMoney=" + tiedupMoney +
				", spMoney=" + spMoney +
				", spAvailMoney=" + spAvailMoney +
				", spTiedupMoney=" + spTiedupMoney +
				", ypMoney=" + ypMoney +
				", ypAvailMoney=" + ypAvailMoney +
				", ypTiedupMoney=" + ypTiedupMoney +
				", beginDate=" + beginDate +
				", endDate=" + endDate +
				", bankAccDTO=" + bankAccDTO +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", billTypeBalVO=" + billTypeBalVO +
				'}';
	}

}
