package com.bytter.bill.domain.bill.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * 票据认领中间表vo
 *
 * @author ouyj
 */
@SuppressWarnings("serial")
public class AbClaimMiddleVo implements Serializable {
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
     *创建时间
     */
    private Date createDate;
    /**
     *修改人
     */
    private String updateBy;
    /**
     *修改时间
     */
    private Date updateDate;
    /**
     * 票据认领中间表id数组
     */
    private String claimIds;
    /**
     * 票据从表id数组
     */
    private String vvids;
    /**
     * 票据主表id数组
     */
    private String vids;
    /**
     * 流水id集合
     */
    private String aids;
    /**
     * 认领备注
     */
    private String rmk;
    /**
     * 票据号码数组
     */
    private String origNos;
    /**
     * 票据金额数组
     */
    private String billMoneys;
    /**
     * 业务类型
     */
    private String briefId;
    /**
     * 银行流水明细id数组
     */
    private String accDtlIds;
    /**
     * 实付金额数组
     */
    private String realMoneys;
    /**
     * 业务类型数组
     */
    private String briefIds;
    public String getBriefIds() {
        return briefIds;
    }
    public void setBriefIds(String briefIds) {
        this.briefIds = briefIds;
    }
    public String getRealMoneys() {
        return realMoneys;
    }
    public void setRealMoneys(String realMoneys) {
        this.realMoneys = realMoneys;
    }
    public String getAccDtlIds() {
        return accDtlIds;
    }
    public void setAccDtlIds(String accDtlIds) {
        this.accDtlIds = accDtlIds;
    }
    public String getAids() {
        return aids;
    }
    public void setAids(String aids) {
        this.aids = aids;
    }
    public String getVids() {
        return vids;
    }
    public void setVids(String vids) {
        this.vids = vids;
    }
    public String getBriefId() {
        return briefId;
    }
    public void setBriefId(String briefId) {
        this.briefId = briefId;
    }
    public String getOrigNos() {
        return origNos;
    }
    public void setOrigNos(String origNos) {
        this.origNos = origNos;
    }
    public String getBillMoneys() {
        return billMoneys;
    }
    public void setBillMoneys(String billMoneys) {
        this.billMoneys = billMoneys;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getClaimIds() {
        return claimIds;
    }
    public void setClaimIds(String claimIds) {
        this.claimIds = claimIds;
    }
    public String getVvids() {
        return vvids;
    }
    public void setVvids(String vvids) {
        this.vvids = vvids;
    }
    public String getClaimSequence() {
        return claimSequence;
    }
    public void setClaimSequence(String claimSequence) {
        this.claimSequence = claimSequence;
    }
    public String getClaimStatus() {
        return claimStatus;
    }
    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }
    public String getClaimPerson() {
        return claimPerson;
    }
    public void setClaimPerson(String claimPerson) {
        this.claimPerson = claimPerson;
    }
    public Date getClaimTime() {
        return claimTime;
    }
    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
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
    @Override
    public String toString() {
        return "AbClaimMiddleVo{" +
                "claimSequence='" + claimSequence + '\'' +
                ", claimStatus='" + claimStatus + '\'' +
                ", claimPerson='" + claimPerson + '\'' +
                ", claimTime=" + claimTime +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                ", claimIds='" + claimIds + '\'' +
                ", vvids='" + vvids + '\'' +
                ", vids='" + vids + '\'' +
                ", aids='" + aids + '\'' +
                ", rmk='" + rmk + '\'' +
                ", origNos='" + origNos + '\'' +
                ", billMoneys='" + billMoneys + '\'' +
                ", briefId='" + briefId + '\'' +
                '}';
    }
}
