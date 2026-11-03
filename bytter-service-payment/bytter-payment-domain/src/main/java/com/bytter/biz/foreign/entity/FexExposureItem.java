package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 敞口科目表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
@Entity
@Table(name = "FEX_EXPOSURE_ITEM", schema = "")
public class FexExposureItem extends IdEntity implements java.io.Serializable {
    /**科目代码*/
    private String itemCode;
    /**科目名称*/
    private String itemName;
    /**上级科目*/
    private String superItemCode;
    /**父科目*/
    private String superItemCodes;
    /**科目流水号*/
    private String seqCode;
    /**科目层级*/
    private Integer levels;
    /**资金流向 1,资金流入 -1,资金流出*/
    private Integer sign;
    /**科目内是否自然对冲  1-是 0-否*/
    private String isNaturalHedge;
    /**有效标志 0 无效 1 有效*/
    private String validSign;
    /**备注*/
    private String rmk;
    /**是否系统科目（系统科目不能删除）*/
    private String isSystem;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="ITEM_CODE",nullable=false,length=40)
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Column(name ="ITEM_NAME",nullable=true,length=100)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Column(name ="SUPER_ITEM_CODE",nullable=true,length=40)
    public String getSuperItemCode() {
        return superItemCode;
    }

    public void setSuperItemCode(String superItemCode) {
        this.superItemCode = superItemCode;
    }

    @Column(name ="SUPER_ITEM_CODES",nullable=true,length=200)
    public String getSuperItemCodes() {
        return superItemCodes;
    }

    public void setSuperItemCodes(String superItemCodes) {
        this.superItemCodes = superItemCodes;
    }

    @Column(name ="SEQ_CODE",nullable=true,length=40)
    public String getSeqCode() {
        return seqCode;
    }

    public void setSeqCode(String seqCode) {
        this.seqCode = seqCode;
    }

    @Column(name ="LEVELS",nullable=true,length=5)
    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }

    @Column(name ="SIGN",nullable=true,length=5)
    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    @Column(name ="IS_NATURAL_HEDGE",nullable=false,length=10)
    public String getIsNaturalHedge() {
        return isNaturalHedge;
    }

    public void setIsNaturalHedge(String isNaturalHedge) {
        this.isNaturalHedge = isNaturalHedge;
    }

    @Column(name ="VALID_SIGN",nullable=false,length=10)
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Column(name ="RMK",nullable=true,length=200)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="IS_SYSTEM",nullable=true,length=10)
    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
