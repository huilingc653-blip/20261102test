package com.bytter.credit.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class RatBkrationDisQueryVO implements Serializable {
    /**
     * 授信编号
     */
    public String ratCode;
    /**
     * 授信合同号
     */
    public String contractNo;
    /**
     * 授信单位 多选
     */
    public String corpIds;
    /**
     * 银行信息 多选
     */
    public String bankCodes;
    /**
     * 授信品种
     */
    public String itemCodes;
    /**
     * 授信开始时间
     */
    public Date gBeginDate;
    public Date lBeginDate;
    /**
     * 授信结束时间
     */
    public Date lEndDate;
    public Date gEndDate;
    /**
     * 当前页
     */
    public Integer start;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 导出类型
     */
    private String exportType;

    public String getRatCode() {
        return ratCode;
    }

    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getCorpIds() {
        return corpIds;
    }

    public void setCorpIds(String corpIds) {
        this.corpIds = corpIds;
    }

    public String getBankCodes() {
        return bankCodes;
    }

    public void setBankCodes(String bankCodes) {
        this.bankCodes = bankCodes;
    }

    public String getItemCodes() {
        return itemCodes;
    }

    public void setItemCodes(String itemCodes) {
        this.itemCodes = itemCodes;
    }

    public Date getgBeginDate() {
        return gBeginDate;
    }

    public void setgBeginDate(Date gBeginDate) {
        this.gBeginDate = gBeginDate;
    }

    public Date getlBeginDate() {
        return lBeginDate;
    }

    public void setlBeginDate(Date lBeginDate) {
        this.lBeginDate = lBeginDate;
    }

    public Date getlEndDate() {
        return lEndDate;
    }

    public void setlEndDate(Date lEndDate) {
        this.lEndDate = lEndDate;
    }

    public Date getgEndDate() {
        return gEndDate;
    }

    public void setgEndDate(Date gEndDate) {
        this.gEndDate = gEndDate;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }
}
