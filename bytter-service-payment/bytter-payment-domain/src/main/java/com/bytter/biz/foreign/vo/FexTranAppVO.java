package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class FexTranAppVO implements IBusinessKey, Serializable {

    /**
     * 主键id
     */
    private String id;

    /**
     * 单据流水号
     */
    @BtCompositeSearch(fieldName = "t.bill_no")
    private String billNo;

    /**
     * 单位
     */
    @BtCompositeSearch(fieldName = "t.corp_id")
    private String corpId;
    private String corpName;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "t.status")
    private Integer status;

    /**
     * 创建人
     */
    private String createBy;
    private String createUserName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateDate;

    /**
     * 明细项id
     */
    private String dtlIds;

    /**
     * 交易对手名
     */
    private String cotpName;


    /**
     * 申请日期
     */
    @BtCompositeSearch(fieldName = "t.APP_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date appDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getDtlIds() {
        return dtlIds;
    }

    public void setDtlIds(String dtlIds) {
        this.dtlIds = dtlIds;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCotpName() {
        return cotpName;
    }

    public void setCotpName(String cotpName) {
        this.cotpName = cotpName;
    }

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    @Override
    public String getBusinessKey() {
        return id;
    }
}
