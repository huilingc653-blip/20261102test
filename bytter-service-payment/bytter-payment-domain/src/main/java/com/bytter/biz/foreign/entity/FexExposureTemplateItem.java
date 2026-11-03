package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Title: Entity
 * @Description: 敞口模板科目表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
@Entity
@Table(name = "FEX_EXPOSURE_TEMPLATE_ITEM", schema = "")
public class FexExposureTemplateItem extends IdEntity implements java.io.Serializable {
    /**科目ID*/
    private String itemId;
    /**科目代码*/
    private String itemCode;
    /**模板ID*/
    private String templateId;

    @Column(name ="ITEM_ID",nullable=false,length=32)
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Column(name ="ITEM_CODE",nullable=false,length=40)
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Column(name ="TEMPLATE_ID",nullable=false,length=32)
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
