package com.bytter.finance.domain.security.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 基础资产类型表
 * @date 2022-07-29
 */
@Entity
@Table(name = "CMS_SECURITY_ASSET_TYPE", schema = "")
public class CmsSecurityAssetType extends IdEntity implements java.io.Serializable {
    /**
     * 类型编码
     */
    private String assetTypeCode;
    /**
     * 类型名称
     */
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
    @Column(name = "ASSET_TYPE_CODE")
    public String getAssetTypeCode() {
        return assetTypeCode;
    }
    public void setAssetTypeCode(String assetTypeCode) {
        this.assetTypeCode = assetTypeCode;
    }
    @Column(name = "ASSET_TYPE_NAME")
    public String getAssetTypeName() {
        return assetTypeName;
    }
    public void setAssetTypeName(String assetTypeName) {
        this.assetTypeName = assetTypeName;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
