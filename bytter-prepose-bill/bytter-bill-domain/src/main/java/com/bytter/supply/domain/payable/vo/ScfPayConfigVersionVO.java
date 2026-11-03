package com.bytter.supply.domain.payable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel("应付债权模板配置版本VO")
public class ScfPayConfigVersionVO implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 模板编号
     */
    @BtCompositeSearch(fieldName = "v.TEMPLATE_CODE")
    @ApiModelProperty("模板编号")
    private String templateCode;
    /**
     * 模板名称
     */
    @BtCompositeSearch(fieldName = "v.TEMPLATE_NAME")
    @ApiModelProperty("模板名称")
    private String templateName;
    /**
     * 模板描述
     */
    @ApiModelProperty("模板描述")
    private String templateDesc;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 状态 0启用 1禁用
     */
    @ApiModelProperty("状态(0启用 1禁用)")
    private String validSign;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
}
