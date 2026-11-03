package com.bytter.biz.netbank.vo;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.Region;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zuoy
 * @version 1.0
 * @Title: Entity
 * @Description: 职工信息维护表VO
 * @date 2017-06-21 09:31:22
 */
@ApiModel("职工信息维护表VO")
@ExcelTarget("NisPersonnelDetailVO")
public class NisPersonnelDetailVO extends ExcelModel implements Serializable {
	private static final long serialVersionUID = -7062659276344105420L;
	@ApiModelProperty("ID")
	private java.lang.String id;

	/**
	 * 职工编号
	 */
	@Excel(name = "*职工编号")
	@NotNull( message = "nis.personneldetail.message.workno.null")
	@BtCompositeSearch(fieldName = "p.work_no")
	@ApiModelProperty("职工编号")
	private java.lang.String workNo;

	/**
	 * 职工姓名
	 */
	@Excel(name = "*职工姓名")
	@NotNull( message = "nis.personneldetail.message.username.null")
	@BtCompositeSearch(fieldName = "p.user_name")
	@ApiModelProperty("职工姓名")
	private java.lang.String userName;

	/**
	 * 身份证号
	 */
	@Excel(name = "身份证号")
	@BtCompositeSearch(fieldName = "p.idcard")
	@ApiModelProperty("身份证号")
	private java.lang.String idcard;

	/**
	 * 性别
	 */
	@Excel(name = "*性别")
	@NotNull( message = "nis.personneldetail.message.sex.null")
	@BtCompositeSearch(fieldName = "p.sex")
	@ApiModelProperty("性别")
	@Dict(dictGroup = "sex",inject = "sexName")
	private java.lang.String sex;
	private java.lang.String sexName;

	/**
	 * 部门名称
	 */
	@Excel(name = "*部门名称")
	@NotNull( message = "nis.personneldetail.message.deptname.null")
	@BtCompositeSearch(fieldName = "p.dept_name")
	@ApiModelProperty("部门名称")
	private java.lang.String deptName;

	/**
	 * 联系电话
	 */
	@Excel(name = "联系电话")
	@BtCompositeSearch(fieldName = "p.mobile")
	@ApiModelProperty("联系电话")
	private java.lang.String mobile;

	/**
	 * 单位ID
	 */
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "p.corp_id")
	@ApiModelProperty("单位ID")
	private java.lang.String corpId;

	/**
	 * 单位名称
	 */
	@Excel(name = "*单位名称")
	@NotNull( message = "nis.personneldetail.message.corpname.null")
	@BtCompositeSearch(fieldName = "p.corp_id")
	@ApiModelProperty("单位名称")
	private java.lang.String corpName;

	/**
	 * 银行账号
	 */
	@Excel(name = "*银行账号")
	@NotNull( message = "nis.personneldetail.message.bankacc.null")
	@BtCompositeSearch(fieldName = "p.bank_acc")
	@ApiModelProperty("银行账号")
	private java.lang.String bankAcc;

	/**
	 * 户名
	 */
	@Excel(name = "*户名")
	@NotNull( message = "nis.personneldetail.message.accname.null")
	@BtCompositeSearch(fieldName = "p.acc_name")
	@ApiModelProperty("户名")
	private java.lang.String accName;

	/**
	 * 接口代码
	 */
	@ApiModelProperty("接口代码")
	private java.lang.String bifCode;

	@Excel(name = "接口代码")
	@ApiModelProperty("接口代码名称")
	private java.lang.String bifName;

	/**
	 * 开户行行号
	 */
	@Excel(name = "开户行行号")
	@ApiModelProperty("开户行行号")
    @BtCompositeSearch(fieldName = "p.bank_code")
	private java.lang.String bankCode;

	/**
	 * 开户银行名称
	 */
	@BtCompositeSearch(fieldName = "p.bank_name")
	@ApiModelProperty("开户银行名称")
	private java.lang.String bankName;

	/**
	 * 账户类型ID
	 */
	@Dict(dictGroup = "nisPersonnelDetail.cardType", inject = "cardTypeName")
	@ApiModelProperty("账户类型ID")
	private java.lang.String cardType;

	/**
	 * 账户类型
	 */
	@Excel(name = "*账户类型")
	@NotNull( message = "nis.personneldetail.message.cardtypename.null")
	@ApiModelProperty("账户类型")
	private java.lang.String cardTypeName;

	/**
	 * 账号所在省ID
	 */
	@Region(inject = "provName")
	@ApiModelProperty("账号所在省ID")
	private java.lang.String prov;

	/**
	 * 账号所在省
	 */
	@Excel(name = "*账号所在省")
	@NotNull( message = "nis.personneldetail.message.provname.null")
	@ApiModelProperty("账号所在省")
	private java.lang.String provName;

	/**
	 * 账号所在市ID
	 */
	@Region(inject = "cityName")
	@ApiModelProperty("账号所在市ID")
	private java.lang.String city;

	/**
	 * 账号所在市
	 */
	@Excel(name = "*账号所在市")
	@NotNull( message = "nis.personneldetail.message.cityname.null")
	@ApiModelProperty("账号所在市")
	private java.lang.String cityName;

	/**
	 * 备注
	 */
	@Excel(name = "备注")
	@ApiModelProperty("备注")
	private java.lang.String rmk;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态", example = "0")
	private java.lang.Integer status;

	/**
	 * 创建人登录名称
	 */
	@ApiModelProperty("创建人登录名称")
	private java.lang.String createBy;

	/**
	 * 创建日期
	 */
	@ApiModelProperty("创建日期")
	private java.util.Date createDate;

	/**
	 * 更新人登录名称
	 */
	@ApiModelProperty("更新人登录名称")
	private java.lang.String updateBy;

	/**
	 * 更新日期
	 */
	@ApiModelProperty("更新日期")
	private java.util.Date updateDate;
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getWorkNo() {
		return workNo;
	}
	public void setWorkNo(java.lang.String workNo) {
		this.workNo = workNo;
	}
	public java.lang.String getUserName() {
		return userName;
	}
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}
	public java.lang.String getIdcard() {
		return idcard;
	}
	public void setIdcard(java.lang.String idcard) {
		this.idcard = idcard;
	}
	public java.lang.String getSex() {
		return sex;
	}
	public void setSex(java.lang.String sex) {
		this.sex = sex;
	}
	public java.lang.String getDeptName() {
		return deptName;
	}
	public void setDeptName(java.lang.String deptName) {
		this.deptName = deptName;
	}
	public java.lang.String getMobile() {
		return mobile;
	}
	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}
	public java.lang.String getCorpId() {
		return corpId;
	}
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	public java.lang.String getCorpName() {
		return corpName;
	}
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	public java.lang.String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(java.lang.String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public java.lang.String getAccName() {
		return accName;
	}
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	public java.lang.String getBifCode() {
		return bifCode;
	}
	public void setBifCode(java.lang.String bifCode) {
		this.bifCode = bifCode;
	}
	public java.lang.String getBifName() {
		return bifName;
	}
	public void setBifName(java.lang.String bifName) {
		this.bifName = bifName;
	}
	public java.lang.String getBankCode() {
		return bankCode;
	}
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	public java.lang.String getBankName() {
		return bankName;
	}
	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}
	public java.lang.String getCardType() {
		return cardType;
	}
	public void setCardType(java.lang.String cardType) {
		this.cardType = cardType;
	}
	public java.lang.String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(java.lang.String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	public java.lang.String getProv() {
		return prov;
	}
	public void setProv(java.lang.String prov) {
		this.prov = prov;
	}
	public java.lang.String getProvName() {
		return provName;
	}
	public void setProvName(java.lang.String provName) {
		this.provName = provName;
	}
	public java.lang.String getCity() {
		return city;
	}
	public void setCity(java.lang.String city) {
		this.city = city;
	}
	public java.lang.String getCityName() {
		return cityName;
	}
	public void setCityName(java.lang.String cityName) {
		this.cityName = cityName;
	}
	public java.lang.String getRmk() {
		return rmk;
	}
	public void setRmk(java.lang.String rmk) {
		this.rmk = rmk;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	@Override
	public String getBeanName() {
		return "nisPersonnelDetailService";
	}
	@Override
	public String toString() {
		return "NisPersonnelDetailVO{" +
				"id='" + id + '\'' +
				", workNo='" + workNo + '\'' +
				", userName='" + userName + '\'' +
				", idcard='" + idcard + '\'' +
				", sex='" + sex + '\'' +
				", deptName='" + deptName + '\'' +
				", mobile='" + mobile + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", accName='" + accName + '\'' +
				", bifCode='" + bifCode + '\'' +
				", bifName='" + bifName + '\'' +
				", bankCode='" + bankCode + '\'' +
				", bankName='" + bankName + '\'' +
				", cardType='" + cardType + '\'' +
				", cardTypeName='" + cardTypeName + '\'' +
				", prov='" + prov + '\'' +
				", provName='" + provName + '\'' +
				", city='" + city + '\'' +
				", cityName='" + cityName + '\'' +
				", rmk='" + rmk + '\'' +
				", status=" + status +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				'}';
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
}
