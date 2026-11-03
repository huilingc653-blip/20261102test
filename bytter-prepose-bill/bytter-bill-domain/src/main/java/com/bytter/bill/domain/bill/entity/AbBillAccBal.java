package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: 票据账户余额Entity
 * @Description: AB_BILL_ACC_BAL
 * @author zhengg
 * @date 2017-09-28 19:20:01
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BILL_ACC_BAL", schema = "")
@SuppressWarnings("serial")
public class AbBillAccBal extends IdEntity implements java.io.Serializable {
	
	public AbBillAccBal(){
		
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
	
	public AbBillAccBal(String corpId,String corpCode,String corpName, String bankId, String bankAcc, Date balDate, String curId){
		this.corpId=corpId;
		this.corpCode=corpCode;
		this.corpName=corpName;
		this.balDate=balDate;
		this.curId=curId;
		this.bankId=bankId;
		this.bankAcc=bankAcc;
		initBalMoney();
	}
	
	/**银行账户ID*/
	private String bankId;
	/**银行账号*/
	private String bankAcc;
	/**单位ID*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**单位代码*/
	private String corpCode;
	/**币别ID*/
	private String curId;
	/**余额日期*/
	private Date balDate;
	/**余额*/
	private BigDecimal bal;
	/**可用余额*/
	private BigDecimal availBal;
	/**占用金额*/
	private BigDecimal tiedupBal;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	
	/**商票余额*/
	private BigDecimal spBal;
	/**商票可用余额*/
	private BigDecimal spAvailBal;
	/**商票占用金额*/
	private BigDecimal spTiedupBal;
	
	/**银票余额*/
	private BigDecimal ypBal;
	/**银票可用余额*/
	private BigDecimal ypAvailBal;
	/**银票占用金额*/
	private BigDecimal ypTiedupBal;
	
	
	
	/**
	 *方法: 取得java.lang.String
	 *@return: String  银行账户ID
	 */
	@Column(name ="BANK_ID",nullable=true,length=32)
	public String getBankId(){
		return this.bankId;
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
	@Column(name ="BANK_ACC",nullable=true,length=30)
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
	@Column(name ="CORP_ID",nullable=true,length=32)
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
	@Column(name ="CORP_NAME",nullable=true,length=200)
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	@Column(name ="CORP_CODE",nullable=true,length=32)
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: String  币别ID
	 */
	@Column(name ="CUR_ID",nullable=true,length=255)
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
	/**
	 *方法: 取得Date
	 *@return: Date  余额日期
	 */
	@Column(name ="BAL_DATE",nullable=true)
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
	@Column(name ="BAL",nullable=true,scale=2,length=19)
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
	@Column(name ="AVAIL_BAL",nullable=true,scale=2,length=19)
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
	@Column(name ="TIEDUP_BAL",nullable=true,scale=2,length=19)
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
	@Column(name ="CREATE_BY",nullable=true,length=50)
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
	@Column(name ="CREATE_DATE",nullable=true)
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
	@Column(name ="UPDATE_BY",nullable=true,length=50)
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
	@Column(name ="UPDATE_DATE",nullable=true)
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
	
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  商票余额
	 */
	@Column(name ="SP_BAL",nullable=true,scale=2,length=19)
	public BigDecimal getSpBal(){
		return this.spBal;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  商票余额
	 */
	public void setSpBal(BigDecimal spBal){
		this.spBal = spBal;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  商票可用余额
	 */
	@Column(name ="SP_AVAIL_BAL",nullable=true,scale=2,length=19)
	public BigDecimal getSpAvailBal(){
		return this.spAvailBal;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  商票可用余额
	 */
	public void setSpAvailBal(BigDecimal spAvailBal){
		this.spAvailBal = spAvailBal;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  商票占用金额
	 */
	@Column(name ="SP_TIEDUP_BAL",nullable=true,scale=2,length=19)
	public BigDecimal getSpTiedupBal(){
		return this.spTiedupBal;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  商票占用金额
	 */
	public void setSpTiedupBal(BigDecimal spTiedupBal){
		this.spTiedupBal = spTiedupBal;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  银票余额
	 */
	@Column(name ="YP_BAL",nullable=true,scale=2,length=19)
	public BigDecimal getYpBal(){
		return this.ypBal;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  银票余额
	 */
	public void setYpBal(BigDecimal ypBal){
		this.ypBal = ypBal;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  银票可用余额
	 */
	@Column(name ="YP_AVAIL_BAL",nullable=true,scale=2,length=19)
	public BigDecimal getYpAvailBal(){
		return this.ypAvailBal;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  银票可用余额
	 */
	public void setYpAvailBal(BigDecimal ypAvailBal){
		this.ypAvailBal = ypAvailBal;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  银票占用金额
	 */
	@Column(name ="YP_TIEDUP_BAL",nullable=true,scale=2,length=19)
	public BigDecimal getYpTiedupBal(){
		return this.ypTiedupBal;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  银票占用金额
	 */
	public void setYpTiedupBal(BigDecimal ypTiedupBal){
		this.ypTiedupBal = ypTiedupBal;
	}
	@Override
	public String toString() {
		return "AbBillAccBal{" +
				"bankId='" + bankId + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", corpId='" + corpId + '\'' +
				", curId='" + curId + '\'' +
				", balDate=" + balDate +
				", bal=" + bal +
				", availBal=" + availBal +
				", tiedupBal=" + tiedupBal +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", spBal=" + spBal +
				", spAvailBal=" + spAvailBal +
				", spTiedupBal=" + spTiedupBal +
				", ypBal=" + ypBal +
				", ypAvailBal=" + ypAvailBal +
				", ypTiedupBal=" + ypTiedupBal +
				'}';
	}
}
