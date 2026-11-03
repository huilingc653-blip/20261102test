package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.Dict;

/**
 * @Title: VO
 * @Description: 敞口模板科目表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
public class FexExposureTemplateItemVO implements java.io.Serializable {
    /**科目ID*/
    private String id;
    /**科目ID*/
    private String itemId;
    /**科目代码*/
    private String itemCode;
    /**科目名称*/
    private String itemName;
    /**模板ID*/
    private String templateId;
    /**上级科目*/
    private String superItemCode;
    private String superItemName;
    /**资金流向1， 资金流入-1 ，资金流出*/
    @Dict(dictGroup = "fbsItem.sign", inject = "signName")
    private Integer sign;
    private String signName;
    /**科目内是否自然对冲  1-是 0-否*/
    @Dict(dictGroup = "sf_yn", inject = "isNaturalHedgeName")
    private String isNaturalHedge;
    private String isNaturalHedgeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSuperItemCode() {
        return superItemCode;
    }

    public void setSuperItemCode(String superItemCode) {
        this.superItemCode = superItemCode;
    }

    public String getSuperItemName() {
        return superItemName;
    }

    public void setSuperItemName(String superItemName) {
        this.superItemName = superItemName;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getIsNaturalHedge() {
        return isNaturalHedge;
    }

    public void setIsNaturalHedge(String isNaturalHedge) {
        this.isNaturalHedge = isNaturalHedge;
    }

    public String getIsNaturalHedgeName() {
        return isNaturalHedgeName;
    }

    public void setIsNaturalHedgeName(String isNaturalHedgeName) {
        this.isNaturalHedgeName = isNaturalHedgeName;
    }
}
