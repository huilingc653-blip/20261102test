package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.Date;
public class BisAccAttributeDTO implements Serializable {
    private String id;
    /**
     * 收支属性编码
     */
    private String attributeCode;
    /**
     * 收支属性名称
     */
    private String attributeName;
    /**
     * 对外属性
     */
    private String foreignDes;
    /**
     * 对同级属性
     */
    private String sameDes;
    /**
     * 收支属性描述
     */
    private String attributeDes;
    /**
     * 对上级属性
     */
    private String superDes;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAttributeCode() {
        return attributeCode;
    }
    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }
    public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    public String getForeignDes() {
        return foreignDes;
    }
    public void setForeignDes(String foreignDes) {
        this.foreignDes = foreignDes;
    }
    public String getSameDes() {
        return sameDes;
    }
    public void setSameDes(String sameDes) {
        this.sameDes = sameDes;
    }
    public String getAttributeDes() {
        return attributeDes;
    }
    public void setAttributeDes(String attributeDes) {
        this.attributeDes = attributeDes;
    }
    public String getSuperDes() {
        return superDes;
    }
    public void setSuperDes(String superDes) {
        this.superDes = superDes;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
}
