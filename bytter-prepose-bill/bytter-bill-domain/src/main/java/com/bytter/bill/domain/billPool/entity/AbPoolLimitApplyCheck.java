package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "AB_POOL_LIMIT_APPLYCHECK", schema = "")
@SuppressWarnings("serial")
public class AbPoolLimitApplyCheck extends IdEntity implements java.io.Serializable{
	/** 池接口代码 **/
	private String corpId;
	
	/**
	 * 票据池ID
	 */
	private String bpId;
	
	/**
	 * 业务类型ID
	 */
	private String busTypeId;
	
	/**
	    * 
	    */
	private String limtType;
	
	/**
	 * 票据质押额度
	 */
	private BigDecimal applyMoney;
	/**
	 * 到期日期
	 */
	private Date endDate;
	/**
	 * 指令状态
	 */
	private String optStatus;
	/**
	 * 审批状态
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String rmk;
	/**
	 * 创建人id
	 */
	private String createId;
	/**
	 * 创建日期
	 */
	private Date createTime;
	/**
	 * 更新人id
	 */
	private String updateId;
	/**
	 * 更新日期
	 */
	private Date updateTime;
	
	
	/**
	 * 核准类型：1通过0不通过
	 */
	private String checkType;
	
	/**
	 * 核准信息
	 */
	private String checkMsg;
	
	
	/**
	 * 
	 */
	private String returnCode;
	
	/**
	 * 
	 */
	private String returnMsg;
	
	@Column(name = "RMK", nullable = true, length = 200)
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	/**
	 * 方法: 取得 创建人名称
	 * 
	 * @return: 创建人名称
	 */
	@Column(name = "CREATE_ID", nullable = true, length = 32)
	public String getCreateId() {
		return createId;
	}
	/**
	 * 设置 创建人名称
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	/**
	 * 方法: 取得 创建日期
	 * 
	 * @return: 创建日期
	 */
	@Column(name = "CREATE_TIME", nullable = true)
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置 创建日期
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 方法: 取得 更新人名称
	 * 
	 * @return: 更新人名称
	 */
	@Column(name = "UPDATE_ID", nullable = true, length = 32)
	public String getUpdateId() {
		return updateId;
	}
	/**
	 * 设置 更新人名称
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	/**
	 * 方法: 取得 更新日期
	 * 
	 * @return:更新日期
	 */
	@Column(name = "UPDATE_TIME", nullable = true)
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置更新日期
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "CORP_ID", nullable = true, length = 32)
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name = "BP_ID", nullable = true, length = 32)
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
	@Column(name = "BUSTYPE_ID", nullable = true, length = 32)
	public String getBusTypeId() {
		return busTypeId;
	}
	@Column(name = "LIMT_TYPE", nullable = true, length = 2)
	public String getLimtType() {
		return limtType;
	}
	@Column(name = "APPLY_MONEY", nullable = true)
	public BigDecimal getApplyMoney() {
		return applyMoney;
	}
	@Column(name = "END_DATE", nullable = true)
	public Date getEndDate() {
		return endDate;
	}
	@Column(name = "OPT_STATUS", nullable = true, length = 2)
	public String getOptStatus() {
		return optStatus;
	}
	@Column(name = "STATUS", nullable = true, length = 6)
	public Integer getStatus() {
		return status;
	}
	@Column(name = "CHECK_TYPE", nullable = true, length = 2)
	public String getCheckType() {
		return checkType;
	}
	@Column(name = "CHECK_MSG", nullable = true, length = 200)
	public String getCheckMsg() {
		return checkMsg;
	}
	@Column(name = "RETURN_CODE", nullable = true, length = 2)
	public String getReturnCode() {
		return returnCode;
	}
	@Column(name = "RETURN_MSG", nullable = true, length = 500)
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setBusTypeId(String busTypeId) {
		this.busTypeId = busTypeId;
	}
	public void setLimtType(String limtType) {
		this.limtType = limtType;
	}
	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setOptStatus(String optStatus) {
		this.optStatus = optStatus;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public void setCheckMsg(String checkMsg) {
		this.checkMsg = checkMsg;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
}
