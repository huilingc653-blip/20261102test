package com.bytter.finance.domain.security.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.util.Date;
/**
 * 基础资产类型表vo
 *
 * @author tongqr
 */
public class CmsSecurityAssetTypeVO implements Serializable {
    private String id;
    /**
     * 类型编号
     */
    @BtCompositeSearch(fieldName = "t.ASSET_TYPE_CODE")
    private String assetTypeCode;
    /**
     * 类型名称
     */
    @BtCompositeSearch(fieldName = "t.ASSET_TYPE_NAME")
    private String assetTypeName;
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
    public String getAssetTypeCode() {
        return assetTypeCode;
    }
    public void setAssetTypeCode(String assetTypeCode) {
        this.assetTypeCode = assetTypeCode;
    }
    public String getAssetTypeName() {
        return assetTypeName;
    }
    public void setAssetTypeName(String assetTypeName) {
        this.assetTypeName = assetTypeName;
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
        return "CmsSecurityAssetTypeVO{" + "id='" + id + '\'' + ", assetTypeCode='" + assetTypeCode + '\''
                + ", assetTypeName='" + assetTypeName + '\'' + ", rmk='" + rmk + '\'' + ", status=" + status
                + ", createBy='" + createBy + '\'' + ", createDate=" + createDate + ", updateBy='" + updateBy + '\''
                + ", updateDate=" + updateDate + '}';
    }
}
