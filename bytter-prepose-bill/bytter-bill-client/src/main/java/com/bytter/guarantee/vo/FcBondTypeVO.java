package com.bytter.guarantee.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;

import java.util.Date;
/**
 *
 * @author zhengxn
 * @Description:保证金类型维护
 */
public class FcBondTypeVO implements java.io.Serializable{
    private String id;
    @BtCompositeSearch(fieldName = "b.bond_type_code")
    private String bondTypeCode;
    @BtCompositeSearch(fieldName = "b.bond_type_name")
    private String bondTypeName;
    @Dict(dictGroup = "valid_sign",inject = "validSignName")
    private String validSign;
    private String validSignName;
    @BtCompositeSearch(fieldName = "b.rmk")
    private String rmk;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    public String getBondTypeCode() {
        return bondTypeCode;
    }
    public void setBondTypeCode(String bondTypeCode) {
        this.bondTypeCode = bondTypeCode;
    }
    public String getBondTypeName() {
        return bondTypeName;
    }
    public void setBondTypeName(String bondTypeName) {
        this.bondTypeName = bondTypeName;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getValidSignName() {
        return validSignName;
    }

    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }

    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
