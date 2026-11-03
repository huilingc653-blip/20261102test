package com.bytter.guarantee.domain.vo;
/**
 * @Title: ConPdMapDtlVO
 * @Description: 合同类型要素映射明细VO
 * @author wangzy
 * @date 2019/11/8 13:33
 *
 */
public class ConPdMapDtlVO implements java.io.Serializable {
    private String id;
    /** 关键要素 */
    private String fieldName;
    /** 映射产品字段 */
    private String pdDtlCode;
    /** 合同类型明细Id */
    private String conTypeDtlId;
    /** 合同类型明细字段是否必填 */
    private String requiredSign;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getPdDtlCode() {
        return pdDtlCode;
    }
    public void setPdDtlCode(String pdDtlCode) {
        this.pdDtlCode = pdDtlCode;
    }
    public String getConTypeDtlId() {
        return conTypeDtlId;
    }
    public void setConTypeDtlId(String conTypeDtlId) {
        this.conTypeDtlId = conTypeDtlId;
    }
    public String getRequiredSign() {
        return requiredSign;
    }
    public void setRequiredSign(String requiredSign) {
        this.requiredSign = requiredSign;
    }
}
