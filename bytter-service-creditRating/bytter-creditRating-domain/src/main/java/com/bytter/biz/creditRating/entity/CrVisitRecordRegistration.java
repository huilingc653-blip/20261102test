package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 拜访登记记录
 */
@Entity
@Table(name = "VISIT_RECORD_REGISTRATION")
public class CrVisitRecordRegistration extends IdEntity implements Serializable {
    /**
     * 拜访事项
     */
    private String visitingMatters;
    /**
     * 拜访人员
     */
    private String visitingPersonnel;
    /**
     * 拜访对象
     */
    private String visitingObj;
    /**
     * 拜访对象职务
     */
    private String visitingObjJob;
    /**
     * 拜访日期
     */
    private Date visitDate;
    /**
     * 外键（结构原始数据）id
     */
    private String institutionalRawDataId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期
     */
    private Date createDate;

    @Column(name = "VISITING_MATTERS", length =256 )
    public String getVisitingMatters() {
        return visitingMatters;
    }

    public void setVisitingMatters(String visitingMatters) {
        this.visitingMatters = visitingMatters;
    }

    @Column(name = "VISITING_PERSONNEL", length =128 )
    public String getVisitingPersonnel() {
        return visitingPersonnel;
    }

    public void setVisitingPersonnel(String visitingPersonnel) {
        this.visitingPersonnel = visitingPersonnel;
    }

    @Column(name = "VISITING_OBJ", length =128 )
    public String getVisitingObj() {
        return visitingObj;
    }

    public void setVisitingObj(String visitingObj) {
        this.visitingObj = visitingObj;
    }

    @Column(name = "VISITING_OBJ_JOB", length =128 )
    public String getVisitingObjJob() {
        return visitingObjJob;
    }

    public void setVisitingObjJob(String visitingObjJob) {
        this.visitingObjJob = visitingObjJob;
    }

    @Column(name = "VISIT_DATE" )
    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    @Column(name = "INSTITUTIONAL_RAW_DATA_ID", length = 32)
    public String getInstitutionalRawDataId() {
        return institutionalRawDataId;
    }

    public void setInstitutionalRawDataId(String institutionalRawDataId) {
        this.institutionalRawDataId = institutionalRawDataId;
    }

    @Column(name = "RMK", length = 300)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name = "CREATOR", length = 100)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name = "CREATE_DATE" )
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "CrVisitRecordRegistration{" +
                "visitingMatters='" + visitingMatters + '\'' +
                ", visitingPersonnel='" + visitingPersonnel + '\'' +
                ", visitingObj='" + visitingObj + '\'' +
                ", visitingObjJob='" + visitingObjJob + '\'' +
                ", visitDate=" + visitDate +
                ", institutionalRawDataId='" + institutionalRawDataId + '\'' +
                ", rmk='" + rmk + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
