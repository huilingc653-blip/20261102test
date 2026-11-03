package com.bytter.guarantee.domain.vo;
import java.io.Serializable;
import com.bytter.core.annotation.Dict;
/**
 * 
 * @Title: 合同类型字段明细表VO
 * @Package com.bytter.biz.contract.vo
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-6 下午3:10:27
 * @version V1.0
 */
public class ConTypeFieldVO implements Serializable {
    /**
    * 
    */
    private static long serialVersionUID = 1L;
    private String id;
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
    @Dict(dictGroup = "sf_yn", inject = "requiredSignName")
    private String requiredSign;
    private String requiredSignName;
    /**
     * 是否有效：0否1是
     */
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    private String validSign;
    private String validSignName;
    /**
     * 01普通字符、02金额、03数字、04日期
     */
    @Dict(dictGroup = "CON_TYPE_FIELD_TYPE", inject = "fieldTypeName")
    private String fieldType;
    private String fieldTypeName;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 排序
     */
    private Integer sortNum;
    /**
     * 业务对象对应字段
     */
    private String pdDtlcode;
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getRequiredSign() {
        return requiredSign;
    }
    public void setRequiredSign(String requiredSign) {
        this.requiredSign = requiredSign;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getFieldType() {
        return fieldType;
    }
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getSortNum() {
        return sortNum;
    }
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public static void setSerialversionuid(long serialversionuid) {
        serialVersionUID = serialversionuid;
    }
    public String getFieldTypeName() {
        return fieldTypeName;
    }
    public void setFieldTypeName(String fieldTypeName) {
        this.fieldTypeName = fieldTypeName;
    }
    public String getRequiredSignName() {
        return requiredSignName;
    }
    public void setRequiredSignName(String requiredSignName) {
        this.requiredSignName = requiredSignName;
    }
    public String getValidSignName() {
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
    public String getPdDtlcode() {
        return pdDtlcode;
    }
    public void setPdDtlcode(String pdDtlcode) {
        this.pdDtlcode = pdDtlcode;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
