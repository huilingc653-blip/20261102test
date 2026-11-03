package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.util.Arrays;
import java.util.Date;
/**
 * @Title: ConPdMapVO
 * @Description: 合同类型要素映射VO
 * @author wangzy
 * @date 2019/11/8 13:32
 *
 */
public class ConPdMapVO implements java.io.Serializable {
    private String id;
    /** 产品类型 */
    @BtCompositeSearch(fieldName = "p.PD_CODE")
    private String pdCode;
    /** 合同类型 */
    private String typeName;
    /** 合同类型Id */
    @BtCompositeSearch(fieldName = "t.id")
    @Dict(tableName = "CON_TYPE", valueFieldName = "ID", showFiledName = "CON_NAME", inject = "typeName")
    private String typeId;
    /** 关键要素 */
    private String fieldName;
    /** 映射产品字段 */
    private String pdDtlCode;
    /** 映射产品字段s */
    private String[] pdDtlCodes;
    /** 合同类型Id */
    private String conTypeId;
    /** 合同类型明细Ids */
    private String[] conTypeDtlIds;
    /** 合同类型明细Id */
    private String conTypeDtlId;
    /** 映射明细表Id */
    private String pDtlId;
    /** 用户Id */
    private String createId;
    /** 用户Id */
    private Date createTime;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPdCode() {
        return pdCode;
    }
    public void setPdCode(String pdCode) {
        this.pdCode = pdCode;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
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
    public String getPdDtlCode() {
        return pdDtlCode;
    }
    public void setPdDtlCode(String pdDtlCode) {
        this.pdDtlCode = pdDtlCode;
    }
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    public String getConTypeDtlId() {
        return conTypeDtlId;
    }
    public void setConTypeDtlId(String conTypeDtlId) {
        this.conTypeDtlId = conTypeDtlId;
    }
    public String[] getPdDtlCodes() {
        return pdDtlCodes;
    }
    public void setPdDtlCodes(String[] pdDtlCodes) {
        this.pdDtlCodes = pdDtlCodes;
    }
    public String[] getConTypeDtlIds() {
        return conTypeDtlIds;
    }
    public void setConTypeDtlIds(String[] conTypeDtlIds) {
        this.conTypeDtlIds = conTypeDtlIds;
    }
    public String getpDtlId() {
        return pDtlId;
    }
    public void setpDtlId(String pDtlId) {
        this.pDtlId = pDtlId;
    }
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Override
    public String toString() {
        return "ConPdMapVO{" + "pdCode='" + pdCode + '\'' + ", typeName='" + typeName + '\'' + ", fieldName='"
                + fieldName + '\'' + ", pdDtlCode='" + pdDtlCode + '\'' + ", pdDtlCodes=" + Arrays.toString(pdDtlCodes)
                + ", conTypeId='" + conTypeId + '\'' + ", conTypeDtlIds=" + Arrays.toString(conTypeDtlIds)
                + ", conTypeDtlId='" + conTypeDtlId + '\'' + '}';
    }
}
