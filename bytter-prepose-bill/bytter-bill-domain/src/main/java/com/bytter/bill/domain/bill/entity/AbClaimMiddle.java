package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * 票据认领中间表实体类
 *
 * @author ouyj
 */
@Entity
@Table(name = "AB_CLAIM_MIDDLE", schema = "")
public class AbClaimMiddle extends IdEntity implements java.io.Serializable {
    /**
     * 序列
     */
    private String claimSequence;
    /**
     * 认领状态 待认领11 打回-1 已确认95
     */
    private String claimStatus;
    /**
     * 认领人
     */
    private String claimPerson;
    /**
     * 认领时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date claimTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
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
     * 备注
     */
    private String rmk;
    /**
     * 票据号码
     */
    private String origNo;
    @Column(name = "orig_no")
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    @Column(name = "rmk")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "claim_sequence")
    public String getClaimSequence() {
        return claimSequence;
    }
    public void setClaimSequence(String claimSequence) {
        this.claimSequence = claimSequence;
    }
    @Column(name = "claim_status")
    public String getClaimStatus() {
        return claimStatus;
    }
    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }
    @Column(name = "claim_person")
    public String getClaimPerson() {
        return claimPerson;
    }
    public void setClaimPerson(String claimPerson) {
        this.claimPerson = claimPerson;
    }
    @Column(name = "claim_time")
    public Date getClaimTime() {
        return claimTime;
    }
    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }
    @Column(name = "create_by")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "update_by")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "update_date")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "AbClaimMiddle{" +
                "claimSequence='" + claimSequence + '\'' +
                ", claimStatus='" + claimStatus + '\'' +
                ", claimPerson='" + claimPerson + '\'' +
                ", claimTime=" + claimTime +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                ", rmk='" + rmk + '\'' +
                ", origNo='" + origNo + '\'' +
                '}';
    }
}
