package com.bytter.biz.ttpay.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 商户信息对应实体
 * @author 郑刚
 * @date 2019-09-26 15:43:05
 * @version 1.0
 *
 */
@Entity
@Table(name = "TP_MERCHANT", schema = "")
public class TpMerchant extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**	上级ID	PARENT_ID	VARCHAR2(32)**/
	private String parentId;
	
	/**单位ID	CORP_ID	VARCHAR2(32)**/
	private String corpId;
	
	/**商户号	MERCHANT_NO	VARCHAR2(255)**/
	private String merchantNo;
	
	/**第三方支付方式代码	PAY_TYPE	VARCHAR2(20)**/
	private String payType;
	
	/**备注	RMK	VARCHAR2(200)**/	
	private String rmk;
	
	/**是否上线	IS_ONLINE	VARCHAR2(1)**/	
	private String isOnline;
	/**有效标识	valid_sign	VARCHAR2(1)**/
	private String validSign;
	
	/**创建人名称	CREATE_NAME	VARCHAR2(50)**/
	private String createName;
	
	/**创建人登录名称	CREATE_BY	VARCHAR2(50)**/
	private String createBy;
	
	/**创建日期	CREATE_DATE	TIMESTAMP**/	
	private Date createDate;
	
	/**更新人名称	UPDATE_NAME	VARCHAR2(50)**/
	private String updateName;
	
	/**更新人登录名称	UPDATE_BY	VARCHAR2(50)**/
	private String updateBy;
	
	/**更新日期	UPDATE_DATE	TIMESTAMP**/	
	private Date updateDate;
	/**
	 * 排序字段
	 */
	private Double sort;
	
	@Column(name = "parent_Id")
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Column(name = "corp_Id")
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name = "merchant_No")
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	@Column(name = "pay_Type")
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Column(name = "rmk")
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Column(name = "is_Online")
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	@Column(name = "valid_Sign")
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	@Column(name = "create_Name")
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	@Column(name = "create_By")
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name = "create_Date")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "update_Name")
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	@Column(name = "update_By")
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	@Column(name = "update_Date")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(name = "sort")
	public Double getSort() {
		return sort;
	}
	public void setSort(Double sort) {
		this.sort = sort;
	}
	@Override
	public String toString() {
		return "TpMerchant [parentId=" + parentId + ", corpId=" + corpId + ", merchantNo=" + merchantNo + ", payType="
				+ payType + ", rmk=" + rmk + ", isOnline=" + isOnline + ", createName=" + createName + ", createBy="
				+ createBy + ", createDate=" + createDate + ", updateName=" + updateName + ", updateBy=" + updateBy
				+ ", updateDate=" + updateDate + ", sort=" + sort + "]";
	}
	
	
}
