package com.bytter.supply.domain.payable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel("应付债权模板配置VO")
public class ScfPayTemplateConfigVO implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 模版版本ID
     */
    @BtCompositeSearch(fieldName = "t.VERSION_ID")
    @ApiModelProperty("模版版本ID")
    private String versionId;
    /**
     * 融资平台ID
     */
    @BtCompositeSearch(fieldName = "t.PLATFORM_ID")
    @ApiModelProperty("融资平台ID")
    private String platformId;
    /**
     * 融资产品ID
     */
    @ApiModelProperty("融资产品ID")
    private String productId;
    /**
     * 是否删除 0否 1是
     */
    @ApiModelProperty("是否删除(0否 1是)")
    private String isDelete;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createDate;
    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateDate;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
