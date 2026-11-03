package com.bytter.guarantee.bond.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 *
 * @author zhengxn
 * @Description:保证金参数
 */
@Entity
@Table(name = "FC_BOND_TYPE")
public class FcBondType extends IdEntity implements java.io.Serializable{
    private String bondTypeCode;
    private String bondTypeName;
    private String validSign;
    private String rmk;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    @Column(name = "BOND_TYPE_CODE")
    public String getBondTypeCode() {
        return bondTypeCode;
    }
    public void setBondTypeCode(String bondTypeCode) {
        this.bondTypeCode = bondTypeCode;
    }
    @Column(name = "BOND_TYPE_NAME")
    public String getBondTypeName() {
        return bondTypeName;
    }
    public void setBondTypeName(String bondTypeName) {
        this.bondTypeName = bondTypeName;
    }
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
