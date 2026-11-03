package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.Dict;
import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 外汇牌价查询指令表
 * @author huangx
 * @date 2020-07-16
 *
 */
public class FexRateExcVO implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 3650731181127660208L;
    /**ID*/
    private String id;
    /**外汇牌价类型*/
    private String rateTypeId;
    private String rateTypeName;
    /**接口*/
    @Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
    private String bifCode;
    private String bifName;
    /**标准币种*/
    @Dict(dictGroup = "fexRateInfo.standardCurrency", inject = "curName")
    private String cur;
    private String curName;
    /**交易状态*/
    @Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
    private String voucherStat;
    private String voucherStatName;
    /**返回时间*/
    private Date returnTime;
    /**返回信息*/
    private String returnMsg;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    public String getRateTypeName() {
        return rateTypeName;
    }

    public void setRateTypeName(String rateTypeName) {
        this.rateTypeName = rateTypeName;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public String getBifName() {
        return bifName;
    }

    public void setBifName(String bifName) {
        this.bifName = bifName;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getVoucherStat() {
        return voucherStat;
    }

    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    public String getVoucherStatName() {
        return voucherStatName;
    }

    public void setVoucherStatName(String voucherStatName) {
        this.voucherStatName = voucherStatName;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
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
