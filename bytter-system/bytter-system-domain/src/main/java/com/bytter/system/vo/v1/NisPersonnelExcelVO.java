package com.bytter.system.vo.v1;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.Region;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zuoy
 * @version 1.0
 * @Title: Entity
 * @Description: 职工信息维护表VO
 * @date 2017-06-21 09:31:22
 */
@ExcelTarget("NisPersonnelExcelVO")
public class NisPersonnelExcelVO extends ExcelModel implements Serializable {
    private static final long serialVersionUID = -7062659276344105420L;
    private String id;

    /**
     * 职工编号
     */
    @Excel(name = "*职工编号")
    @NotNull(message = "nis.personneldetail.message.workno.null")
    @BtCompositeSearch(fieldName = "p.work_no")
    private String workNo;

    /**
     * 职工姓名
     */
    @Excel(name = "*职工姓名")
    @NotNull(message = "nis.personneldetail.message.username.null")
    @BtCompositeSearch(fieldName = "p.user_name")
    private String userName;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    @BtCompositeSearch(fieldName = "p.idcard")
    private String idcard;

    /**
     * 性别
     */
    @Excel(name = "*性别")
    @NotNull(message = "nis.personneldetail.message.sex.null")
    @BtCompositeSearch(fieldName = "p.sex")
    @Dict(dictGroup = "sex", inject = "sexName")
    private String sex;
    private String sexName;

    /**
     * 部门名称
     */
    @Excel(name = "*部门名称")
    @NotNull(message = "nis.personneldetail.message.deptname.null")
    @BtCompositeSearch(fieldName = "p.dept_name")
    private String deptName;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    @BtCompositeSearch(fieldName = "p.mobile")
    private String mobile;

    /**
     * 单位ID
     */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "p.corp_id")
    private String corpId;

    /**
     * 单位名称
     */
    @Excel(name = "*单位名称")
    @NotNull(message = "nis.personneldetail.message.corpname.null")
    @BtCompositeSearch(fieldName = "p.corp_id")
    private String corpName;

    /**
     * 银行账号
     */
    @Excel(name = "*银行账号")
    @NotNull(message = "nis.personneldetail.message.bankacc.null")
    @BtCompositeSearch(fieldName = "p.bank_acc")
    private String bankAcc;

    /**
     * 户名
     */
    @Excel(name = "*户名")
    @NotNull(message = "nis.personneldetail.message.accname.null")
    @BtCompositeSearch(fieldName = "p.acc_name")
    private String accName;

    /**
     * 接口代码
     */
    private String bifCode;

    @Excel(name = "接口代码名称")
    private String bifName;

    /**
     * 开户行行号
     */
    @Excel(name = "开户行行号")
    private String bankCode;

    /**
     * 开户银行名称
     */
    @BtCompositeSearch(fieldName = "p.bank_name")
    private String bankName;

    /**
     * 账户类型ID
     */
    @Dict(dictGroup = "nisPersonnelDetail.cardType", inject = "cardTypeName")
    private String cardType;

    /**
     * 账户类型
     */
    @Excel(name = "*账户类型")
    @NotNull(message = "nis.personneldetail.message.cardtypename.null")
    private String cardTypeName;

    /**
     * 账号所在省ID
     */
    @Region(inject = "provName")
    private String prov;

    /**
     * 账号所在省
     */
    @Excel(name = "*账号所在省")
    @NotNull(message = "nis.personneldetail.message.provname.null")
    private String provName;

    /**
     * 账号所在市ID
     */
    @Region(inject = "cityName")
    private String city;

    /**
     * 账号所在市
     */
    @Excel(name = "*账号所在市")
    @NotNull(message = "nis.personneldetail.message.cityname.null")
    private String cityName;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String rmk;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建人登录名称
     */
    private String createBy;

    /**
     * 创建日期
     */
    private java.util.Date createDate;

    /**
     * 更新人登录名称
     */
    private String updateBy;

    /**
     * 更新日期
     */
    private java.util.Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public String getBifName() {
        return bifName;
    }

    public void setBifName(String bifName) {
        this.bifName = bifName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public java.util.Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
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
        return "nisPersonnelExcelService";
    }

    @Override
    public String toString() {
        return "NisPersonnelExcelVO{" +
                "id='" + id + '\'' +
                ", workNo='" + workNo + '\'' +
                ", userName='" + userName + '\'' +
                ", idcard='" + idcard + '\'' +
                ", sex='" + sex + '\'' +
                ", sexName='" + sexName + '\'' +
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
