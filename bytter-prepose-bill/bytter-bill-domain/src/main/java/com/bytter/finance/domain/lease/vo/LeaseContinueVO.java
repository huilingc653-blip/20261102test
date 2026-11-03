package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.util.Date;
public class LeaseContinueVO implements Serializable, IBusinessKey {
    private String id;
    /** 租赁表ID */
    private String leaseId;
    /**
     * 续租到期日期
     */
    private Date CONTINUE_DATE;
    /** 续租合同 */
    private String CONTINUE_CODE;
    /** 状态 */
    private Integer status;
    /** 备注 */
    private String rmk;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改日期
     */
    private Date updateDate;
    /**
     * 融资租赁单号
     */
    private String leaseNo;
    /**
     * 融资租赁类型
     */
    private String leaseType;
    /**
     * 融资租赁合同编号
     */
    private String leaseCode;
    /**
     * 买卖合同编号
     */
    private String payCode;
    /**
     * 承租人
     */
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 出租人
     */
    private String lessor;
    /**
     * 供货商
     */
    private String supplier;
    /**
     * 续租次数
     */
    private Integer continueCount;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    public Date getCONTINUE_DATE() {
        return CONTINUE_DATE;
    }
    public void setCONTINUE_DATE(Date CONTINUE_DATE) {
        this.CONTINUE_DATE = CONTINUE_DATE;
    }
    public String getCONTINUE_CODE() {
        return CONTINUE_CODE;
    }
    public void setCONTINUE_CODE(String CONTINUE_CODE) {
        this.CONTINUE_CODE = CONTINUE_CODE;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getLeaseNo() {
        return leaseNo;
    }
    public void setLeaseNo(String leaseNo) {
        this.leaseNo = leaseNo;
    }
    public String getLeaseType() {
        return leaseType;
    }
    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }
    public String getLeaseCode() {
        return leaseCode;
    }
    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
    }
    public String getPayCode() {
        return payCode;
    }
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getLessor() {
        return lessor;
    }
    public void setLessor(String lessor) {
        this.lessor = lessor;
    }
    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public Integer getContinueCount() {
        return continueCount;
    }
    public void setContinueCount(Integer continueCount) {
        this.continueCount = continueCount;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
