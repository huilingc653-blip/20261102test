package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: FC_BANK_CHECK_LOG
 * @author onlineGenerator
 * @date 2017-09-29 14:33:58
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_BANK_CHECK_LOG", schema = "")
@SuppressWarnings("serial")
public class FcBankCheckLog extends IdEntity implements java.io.Serializable {
	/**操作日期*/
	private Date opDate;
	/**操作人*/
	private String opCode;
	/**操作人姓名*/
	private String opName;
	/**操作类型*/
	private String opType;
	/**银行账号*/
	private String bankAcc;
	/**企业账号*/
	private String account;
	/**币别代码*/
	private String curCode;
	/**单据ID*/
	private String billId;
	/**对账类型*/
	private String checkType;
	/**核对标准*/
	private String checkFlag;
	/**开始日期*/
	private Date beginDate;
	/**结束日期*/
	private Date endDate;
	/**创建时间*/
	private Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  操作日期
	 */
	@Column(name ="OP_DATE")
	public Date getOpDate(){
		return this.opDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  操作日期
	 */
	public void setOpDate(Date opDate){
		this.opDate = opDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作人
	 */
	@Column(name ="OP_CODE")
	public String getOpCode(){
		return this.opCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作人
	 */
	public void setOpCode(String opCode){
		this.opCode = opCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作人姓名
	 */
	@Column(name ="OP_NAME")
	public String getOpName(){
		return this.opName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作人姓名
	 */
	public void setOpName(String opName){
		this.opName = opName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作类型
	 */
	@Column(name ="OP_TYPE")
	public String getOpType(){
		return this.opType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作类型
	 */
	public void setOpType(String opType){
		this.opType = opType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行账号
	 */
	@Column(name ="BANK_ACC")
	public String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行账号
	 */
	public void setBankAcc(String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  企业账号
	 */
	@Column(name ="ACCOUNT")
	public String getAccount(){
		return this.account;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  企业账号
	 */
	public void setAccount(String account){
		this.account = account;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_CODE")
	public String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单据ID
	 */
	@Column(name ="BILL_ID")
	public String getBillId(){
		return this.billId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单据ID
	 */
	public void setBillId(String billId){
		this.billId = billId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对账类型
	 */
	@Column(name ="CHECK_TYPE")
	public String getCheckType(){
		return this.checkType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对账类型
	 */
	public void setCheckType(String checkType){
		this.checkType = checkType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  核对标准
	 */
	@Column(name ="CHECK_FLAG")
	public String getCheckFlag(){
		return this.checkFlag;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  核对标准
	 */
	public void setCheckFlag(String checkFlag){
		this.checkFlag = checkFlag;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  开始日期
	 */
	@Column(name ="BEGIN_DATE")
	public Date getBeginDate(){
		return this.beginDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  开始日期
	 */
	public void setBeginDate(Date beginDate){
		this.beginDate = beginDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结束日期
	 */
	@Column(name ="END_DATE")
	public Date getEndDate(){
		return this.endDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结束日期
	 */
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME")
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(Date createTime){
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
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(Date updateTime){
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
}
