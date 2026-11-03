package com.bytter.credit.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 授信占用申请表列表页面数据
 */
public class RatUrationApplyListVO implements Serializable {
    private List<RatBkrationVO> rations;
    private Integer curr;
    private Integer pageCount;
    private Integer pageSize;
    private Long resultCount;
    private String corpId;
    private String contractNo;
    private String ratCode;
    private String bankName;
    private String gBeginDate;
    private String lBeginDate;
    private String gEndDate;
    private String lEndDate;

    public List<RatBkrationVO> getRations() {
        return rations;
    }

    public void setRations(List<RatBkrationVO> rations) {
        this.rations = rations;
    }

    public Integer getCurr() {
        return curr;
    }

    public void setCurr(Integer curr) {
        this.curr = curr;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getResultCount() {
        return resultCount;
    }

    public void setResultCount(Long resultCount) {
        this.resultCount = resultCount;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getRatCode() {
        return ratCode;
    }

    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getgBeginDate() {
        return gBeginDate;
    }

    public void setgBeginDate(String gBeginDate) {
        this.gBeginDate = gBeginDate;
    }

    public String getlBeginDate() {
        return lBeginDate;
    }

    public void setlBeginDate(String lBeginDate) {
        this.lBeginDate = lBeginDate;
    }

    public String getgEndDate() {
        return gEndDate;
    }

    public void setgEndDate(String gEndDate) {
        this.gEndDate = gEndDate;
    }

    public String getlEndDate() {
        return lEndDate;
    }

    public void setlEndDate(String lEndDate) {
        this.lEndDate = lEndDate;
    }
}
