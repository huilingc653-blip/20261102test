package com.bytter.accountReceivable.query.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 应收单逾期催收登记表
 */
@Entity
@Table(name = "DOC_OVERDUE_NOTICE")
public class DocOverdueNotice  extends IdEntity implements Serializable {

    private String docId;
    /**
     * 逾期原因
     */
    private String overdueReason;
    /**
     * 催收人
     */
    private String allocationBy;
    /**
     * 催收时间
     */
    private Date allocationDate;
    /**
     * 催收结果
     */
    private String allocationResult;

    private String tenantId;
    private String createBy;
    private Date createDate;

    @Column(name ="DOC_ID")
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Column(name ="OVERDUE_REASON")
    public String getOverdueReason() {
        return overdueReason;
    }

    public void setOverdueReason(String overdueReason) {
        this.overdueReason = overdueReason;
    }

    @Column(name ="COLLECTION_BY")
    public String getAllocationBy() {
        return allocationBy;
    }

    public void setAllocationBy(String allocationBy) {
        this.allocationBy = allocationBy;
    }

    @Column(name ="COLLECTION_DATE")
    public Date getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(Date allocationDate) {
        this.allocationDate = allocationDate;
    }

    @Column(name ="COLLECTIO_RESULT")
    public String getAllocationResult() {
        return allocationResult;
    }

    public void setAllocationResult(String allocationResult) {
        this.allocationResult = allocationResult;
    }

    @Column(name ="TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

}
