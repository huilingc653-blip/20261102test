package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.util.Date;
/**
 * @author luxy
 * @Title: GuaPledgeTypeVO
 * @ProjectName T3(3.2)
 * @Description: 抵质押类型VO
 * @date 2019/11/15 14:49
 */
public class GuaPledgeTypeVO {
    @Dict(tableName = "GUA_PLEDGE_TYPE", valueFieldName = "id", showFiledName = "TYPE_NAME", inject = "typeName")
    private String id;
    /** 抵质押类型编号 */
    @BtCompositeSearch(fieldName = "B.TYPE_CODE")
    private String typeCode;
    /** 抵质押类型名称 */

    @BtCompositeSearch(fieldName = "B.TYPE_NAME")
    private String typeName;
    /** 抵质押所属类型 */
    @BtCompositeSearch(fieldName = "B.TYPE_ATTR")
    private String typeAttr;
    /** 有效标识 */
    @BtCompositeSearch(fieldName = "B.VALID_SIGN")
    private String validSign;
    /** 创建日期 */
    private Date createDate;
    /** 创建人登录名称 */
    private String createBy;
    /** 更新日期 */
    private Date updateDate;
    /** 更新人登录名称 */
    private String updateBy;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTypeCode() {
        return typeCode;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getTypeAttr() {
        return typeAttr;
    }
    public void setTypeAttr(String typeAttr) {
        this.typeAttr = typeAttr;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
