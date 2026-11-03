package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外汇牌价查询指令表
 * @author huangx
 * @date 2020-07-16
 *
 */
@Entity
@Table(name = "FEX_RATE_EXC")
public class FexRateExc extends IdEntity implements Serializable {
    /**外汇牌价类型*/
    private String rateTypeId;
    /**接口*/
    private String bifCode;
    /**标准币种*/
    private String cur;
    /**交易状态*/
    private String voucherStat;
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

    @Column(name ="RATE_TYPE_ID")
    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    @Column(name ="BIF_CODE")
    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    @Column(name ="CUR")
    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    @Column(name ="VOUCHER_STAT")
    public String getVoucherStat() {
        return voucherStat;
    }

    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    @Column(name ="RETURN_TIME")
    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    @Column(name ="RETURN_MSG")
    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
