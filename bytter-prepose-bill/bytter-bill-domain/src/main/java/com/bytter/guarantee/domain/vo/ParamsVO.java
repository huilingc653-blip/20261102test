package com.bytter.guarantee.domain.vo;
public class ParamsVO {
    /** 抵质押物id */
    private String pledgeIds;
    /** 开始日期 */
    private java.util.Date startDate;
    /** 结束日期 */
    private java.util.Date endDate;
    /** 单位 */
    private String corpId;
    /** 抵质押性质 */
    private String pledgeAttr;
    /** 抵押信息 */
    private String pDids;
    /** 质押信息 */
    private String pZids;
    /** 担保合同id */
    private String guaId;
    /** 币别 */
    // private java.lang.String curCode;
    private String curId;
    /** 抵质押信息 */
    private String pledgeMsg;
    /** 担保合同id */
    private String contractIds;
    /** 系统编号 */
    private String sysCode;
    /** 业务id */
    private String businessIds;
    public String getPledgeIds() {
        return pledgeIds;
    }
    public void setPledgeIds(String pledgeIds) {
        this.pledgeIds = pledgeIds;
    }
    public java.util.Date getStartDate() {
        return startDate;
    }
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getPledgeAttr() {
        return pledgeAttr;
    }
    public void setPledgeAttr(String pledgeAttr) {
        this.pledgeAttr = pledgeAttr;
    }
    public String getpDids() {
        return pDids;
    }
    public void setpDids(String pDids) {
        this.pDids = pDids;
    }
    public String getpZids() {
        return pZids;
    }
    public void setpZids(String pZids) {
        this.pZids = pZids;
    }
    public String getGuaId() {
        return guaId;
    }
    public void setGuaId(String guaId) {
        this.guaId = guaId;
    }
    /*
     * public java.lang.String getCurCode() { return curCode; } public void setCurCode(java.lang.String curCode) {
     * this.curCode = curCode; }
     */
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getContractIds() {
        return contractIds;
    }
    public void setContractIds(String contractIds) {
        this.contractIds = contractIds;
    }
    public String getPledgeMsg() {
        return pledgeMsg;
    }
    public void setPledgeMsg(String pledgeMsg) {
        this.pledgeMsg = pledgeMsg;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getBusinessIds() {
        return businessIds;
    }
    public void setBusinessIds(String businessIds) {
        this.businessIds = businessIds;
    }
}
