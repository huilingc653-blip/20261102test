package com.bytter.finance.domain.security.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.util.Date;
/**
 * 资产证券化类型vo
 *
 * @author tongqr
 */
public class CmsSecurityTypeVO implements Serializable {
    private String id;
    /**
     * 类型编号
     */
    @BtCompositeSearch(fieldName = "t.type_code")
    private String typeCode;
    /**
     * 类型名称
     */
    @BtCompositeSearch(fieldName = "t.type_name")
    private String typeName;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
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
    @Override
    public String toString() {
        return "CmsEquityTypeVo{" + "id='" + id + '\'' + ", typeCode='" + typeCode + '\'' + ", typeName='" + typeName
                + '\'' + ", rmk='" + rmk + '\'' + ", status=" + status + ", createBy='" + createBy + '\''
                + ", createDate=" + createDate + ", updateBy='" + updateBy + '\'' + ", updateDate=" + updateDate + '}';
    }
}
