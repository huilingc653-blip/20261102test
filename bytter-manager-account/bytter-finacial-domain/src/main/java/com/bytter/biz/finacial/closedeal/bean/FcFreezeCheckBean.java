package com.bytter.biz.finacial.closedeal.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 活期冻结检查Bean
 * 
 * @author liyh
 *
 */
public class FcFreezeCheckBean implements Serializable {
	/**
	 * 账户
	 */
	private String blnctrAcc;
	/**
	 * 户名
	 */
	private String accName;
	/**
	 * 冻结业务类型
	 */
	private String freezeType;
	/**
	 * 操作类型
	 */
	private String freezeSign;
	/**
	 * 冻结开始日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date beginDate;
	/**
	 * 冻结结束日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/**
	 * 冻结金额
	 */
	private BigDecimal freezeMoney;
	/**
	 * 累计冻结金额
	 */
	private BigDecimal accuFreezeMoney;
	/**
	 * 冻结状态
	 */
	private Integer status;
	/**
	 * 经办员
	 */
	private String freezeOperator;
	/**
	 * 经办时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date operateTime;
	public String getBlnctrAcc() {
		return blnctrAcc;
	}
	public void setBlnctrAcc(String blnctrAcc) {
		this.blnctrAcc = blnctrAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getFreezeType() {
		return freezeType;
	}
	public void setFreezeType(String freezeType) {
		this.freezeType = freezeType;
	}
	public String getFreezeSign() {
		return freezeSign;
	}
	public void setFreezeSign(String freezeSign) {
		this.freezeSign = freezeSign;
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
	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public BigDecimal getAccuFreezeMoney() {
		return accuFreezeMoney;
	}
	public void setAccuFreezeMoney(BigDecimal accuFreezeMoney) {
		this.accuFreezeMoney = accuFreezeMoney;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getFreezeOperator() {
		return freezeOperator;
	}
	public void setFreezeOperator(String freezeOperator) {
		this.freezeOperator = freezeOperator;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
}
