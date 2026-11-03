package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同类型字段明细表Entity
 * @Description: CON_TYPE_FIELD合同类型字段明细表
 * @date 2019-11-04
 */
@Entity
@Table(name = "CON_TYPE_FIELD", schema = "")
@SuppressWarnings("serial")
public class ConTypeField extends IdEntity implements java.io.Serializable {
    /**
     * 合同类型ID
     */
    private String typeId;
    /**
     * 要素名称
     */
    private String fieldName;
    /**
     * 是否必输：0否1是
     */
    private String requiredSign;
    /**
     * 是否有效：0否1是
     */
    private String validSign;
    /**
     * 01普通字符、02金额、06数字、04日期
     */
    private String fieldType;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 排序
     */
    private Integer sortNum;
    /**
     * 方法: 取得合同类型ID
     *
     * @return:合同类型ID
     */
    @Column(name = "TYPE_ID", nullable = true, length = 32)
    public String getTypeId() {
        return typeId;
    }
    /**
     * 合同类型ID
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    /**
     * 方法: 取得要素名称
     *
     * @return:要素名称
     */
    @Column(name = "FIELD_NAME", nullable = true, length = 100)
    public String getFieldName() {
        return fieldName;
    }
    /**
     * 设置要素名称
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    /**
     * 方法: 取得是否必输：0否1是
     *
     * @return:是否必输：0否1是
     */
    @Column(name = "REQUIRED_SIGN", nullable = true, length = 1)
    public String getRequiredSign() {
        return requiredSign;
    }
    /**
     * 设置是否必输：0否1是
     */
    public void setRequiredSign(String requiredSign) {
        this.requiredSign = requiredSign;
    }
    /**
     * 方法: 取得是否有效：0否1是
     *
     * @return:是否有效：0否1是
     */
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return validSign;
    }
    /**
     * 设置是否有效：0否1是
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    /**
     * 方法: 取得字段类型：01普通字符、02金额、06数字、04日期
     *
     * @return:字段类型：01普通字符、02金额、06数字、04日期
     */
    @Column(name = "FIELD_TYPE", nullable = true, length = 10)
    public String getFieldType() {
        return fieldType;
    }
    /**
     * 设置字段类型：01普通字符、02金额、06数字、04日期
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    /**
     * 方法: 取得备注
     *
     * @return:备注
     */
    @Column(name = "RMK", nullable = true, length = 200)
    public String getRmk() {
        return rmk;
    }
    /**
     * 设置备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得排序
     *
     * @return:排序
     */
    @Column(name = "SORT_NUM", nullable = true, length = 6)
    public Integer getSortNum() {
        return sortNum;
    }
    /**
     * 设置排序
     */
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
    @Override
    public String toString() {
        return "ConTypeField{" + "typeId='" + typeId + '\'' + ", fieldName='" + fieldName + '\'' + ", requiredSign='"
                + requiredSign + '\'' + ", validSign='" + validSign + '\'' + ", fieldType='" + fieldType + '\''
                + ", rmk='" + rmk + '\'' + ", sortNum=" + sortNum + '}';
    }
}
