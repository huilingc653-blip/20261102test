package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;

import java.util.Date;

/**
 * @author qinl
 * @version 1.0
 * @Title: VO
 * @Description: 外汇牌价类型表
 * @date 2020-07-09
 */
public class FexRateTypeVO {
    private String id;
    /**名称*/
    @BtCompositeSearch(fieldName = "t.TYPE_NAME")
    private String typeName;
    /**备注*/
    @BtCompositeSearch(fieldName = "t.RMK")
    private String rmk;
    /**外汇牌价接口类别 1-银企直联 2-网络机器人*/
    @BtCompositeSearch(fieldName = "t.RATE_INTERFACE_TYPE")
    private String rateInterfaceType;
    /**接口*/
    private String bifCode;
    @BtCompositeSearch(fieldName = "b.name")
    private String bifCodeName;
    /**是否需要按标准币种查询*/
    private String isNeedQueryCur;
    /**有效标志*/
    @BtCompositeSearch(fieldName = "t.VALID_SIGN")
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    public String getBifCodeName() {
        return bifCodeName;
    }

    public void setBifCodeName(String bifCodeName) {
        this.bifCodeName = bifCodeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRateInterfaceType() {
        return rateInterfaceType;
    }

    public void setRateInterfaceType(String rateInterfaceType) {
        this.rateInterfaceType = rateInterfaceType;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public String getIsNeedQueryCur() {
        return isNeedQueryCur;
    }

    public void setIsNeedQueryCur(String isNeedQueryCur) {
        this.isNeedQueryCur = isNeedQueryCur;
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
