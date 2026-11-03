package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.Dict;
import com.bytter.check.vo.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Title: VO
 * @Description: 敞口科目表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
public class FexExposureItemVO implements IBaseDataInject, java.io.Serializable {

    private String id;
    /**科目代码*/
    private String itemCode;
    /**科目名称*/
    private String itemName;
    /**上级科目*/
    private String superItemCode;
    private String superItemName;
    /**父科目*/
    private String superItemCodes;
    /**科目流水号*/
    private String seqCode;
    /**科目层级*/
    private Integer levels;
    /**资金流向1， 资金流入-1 ，资金流出*/
    @Dict(dictGroup = "fbsItem.sign", inject = "signName")
    private Integer sign;
    private String signName;
    /**科目内是否自然对冲  1-是 0-否*/
    @Dict(dictGroup = "sf_yn", inject = "isNaturalHedgeName")
    private String isNaturalHedge;
    private String isNaturalHedgeName;
    /**有效标志 0 无效 1 有效*/
    @Dict(dictGroup = "valid_sign", inject = "validSignName")
    private String validSign;
    private String validSignName;
    /**是否系统科目*/
    private String isSystem;
    private String  rmk;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**科目显示方式*/
    private Integer showItem;
    /**科目层级*/
    private Integer itemLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSuperItemCodes() {
        return superItemCodes;
    }

    public void setSuperItemCodes(String superItemCodes) {
        this.superItemCodes = superItemCodes;
    }

    public String getSeqCode() {
        return seqCode;
    }

    public void setSeqCode(String seqCode) {
        this.seqCode = seqCode;
    }

    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
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

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getValidSignName() {
        return validSignName;
    }

    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
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

    public Integer getShowItem() {
        return showItem;
    }

    public void setShowItem(Integer showItem) {
        this.showItem = showItem;
    }

    public Integer getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(Integer itemLevel) {
        this.itemLevel = itemLevel;
    }
}
