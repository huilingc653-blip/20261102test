package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Entity
 * @Description: 智能选票-选票要素
 * @date 2022-10-10
 */
@Entity
@Table(name = "AB_INTELLECT_ELEMENT", schema = "")
@SuppressWarnings("serial")
public class AbIntellectElement extends IdEntity implements java.io.Serializable {
    /**
     * 选票要素编码
     */
    private String elementCode;
    /**
     * 选票要素名称
     */
    private String elementName;
    /**
     * 有效标识 0无效 1有效
     */
    private String validSign;
    /**
     * 来源类型：1数据字典 2数据字段 3计算公式 4系统常量
     */
    private String sourceType;
    /**
     * 来源名称
     */
    private String sourceName;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 数据字典
     */
    private String dictGroup;
    /**
     * 系统常量值 sysDate：系统当前日期
     */
    private String sysConstant;
    /**
     * 数据类型:money金额  date日期 text文本 num数值
     */
    private String dataType;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 计算方式：sum求和 count笔数 add相加 sub相减
     */
    private String computeMode;
    /**
     * 来源选票栈要素名称（加数/减数）
     */
    private String sourceElementName1;
    /**
     * 来源选票栈要素名称（加数/被减数）
     */
    private String sourceElementName2;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    @Column(name = "ELEMENT_CODE", nullable = true)
    public String getElementCode() {
        return elementCode;
    }
    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }
    @Column(name = "ELEMENT_NAME", nullable = true)
    public String getElementName() {
        return elementName;
    }
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
    @Column(name = "VALID_SIGN", nullable = true)
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "SOURCE_TYPE", nullable = true)
    public String getSourceType() {
        return sourceType;
    }
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    @Column(name = "SOURCE_NAME", nullable = true)
    public String getSourceName() {
        return sourceName;
    }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    @Column(name = "RMK", nullable = true)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "DICT_GROUP", nullable = true)
    public String getDictGroup() {
        return dictGroup;
    }
    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }
    @Column(name = "DATA_TYPE", nullable = true)
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    @Column(name = "TABLE_NAME", nullable = true)
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    @Column(name = "FIELD_NAME", nullable = true)
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    @Column(name = "COMPUTE_MODE", nullable = true)
    public String getComputeMode() {
        return computeMode;
    }
    public void setComputeMode(String computeMode) {
        this.computeMode = computeMode;
    }
    @Column(name = "SOURCE_ELEMENT_NAME1", nullable = true)
    public String getSourceElementName1() {
        return sourceElementName1;
    }
    public void setSourceElementName1(String sourceElementName1) {
        this.sourceElementName1 = sourceElementName1;
    }
    @Column(name = "SOURCE_ELEMENT_NAME2", nullable = true)
    public String getSourceElementName2() {
        return sourceElementName2;
    }
    public void setSourceElementName2(String sourceElementName2) {
        this.sourceElementName2 = sourceElementName2;
    }
    @Column(name = "CREATE_NAME", nullable = true)
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    @Column(name = "CREATE_BY", nullable = true)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_NAME", nullable = true)
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    @Column(name = "UPDATE_BY", nullable = true)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "SYS_CONSTANT", nullable = true)
    public String getSysConstant() {
        return sysConstant;
    }
    public void setSysConstant(String sysConstant) {
        this.sysConstant = sysConstant;
    }
    @Override
    public String toString() {
        return "AbIntellectElement{" +
                "elementCode='" + elementCode + '\'' +
                ", elementName='" + elementName + '\'' +
                ", validSign='" + validSign + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", rmk='" + rmk + '\'' +
                ", dictGroup='" + dictGroup + '\'' +
                ", sysConstant='" + sysConstant + '\'' +
                ", dataType='" + dataType + '\'' +
                ", tableName='" + tableName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", computeMode='" + computeMode + '\'' +
                ", sourceElementName1='" + sourceElementName1 + '\'' +
                ", sourceElementName2='" + sourceElementName2 + '\'' +
                ", createName='" + createName + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateName='" + updateName + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
