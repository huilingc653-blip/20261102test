package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: AbPoolContract
 * @Description: 融资合同Entity
 * @author wangzy
 * @date 2020/2/10 15:08
 *
 */
@Entity
@Table(name = "AB_POOL_CONTRACT", schema = "")
@SuppressWarnings("serial")
public class AbPoolContract extends IdEntity implements java.io.Serializable{
    /**票据池明细表ID**/
    private String dtlId;
    /**合同状态**/
    private String status;
    /**合同类型*/
    private String ConFtype;
    /**合同名称*/
    private String ConName;
    /**合同文件Id*/
    private String fileId;
    /**合同指令状态**/
    private String optStatus;
    /**创建人ID**/
    private String createId;
    /**更新人ID**/
    private String updateId;
    /**合同信息**/
    private String conInfo;
    /**创建日期**/
    private Date createTime;
    /**更新日期**/
    private Date updateTime;
    @Column(name ="DTL_ID")
    public String getDtlId() {
        return dtlId;
    }
    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }
    @Column(name ="STATUS")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name ="CON_FTYPE")
    public String getConFtype() {
        return ConFtype;
    }
    public void setConFtype(String conFtype) {
        ConFtype = conFtype;
    }
    @Column(name ="CON_NAME")
    public String getConName() {
        return ConName;
    }
    public void setConName(String conName) {
        ConName = conName;
    }
    @Column(name ="OPT_STATUS")
    public String getOptStatus() {
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    @Column(name ="CREATE_ID")
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    @Column(name ="UPDATE_ID")
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    @Column(name ="CON_INFO")
    public String getConInfo() {
        return conInfo;
    }
    public void setConInfo(String conInfo) {
        this.conInfo = conInfo;
    }
    @Column(name ="FILE_ID")
    public String getFileId() {
        return fileId;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    @Column(name ="CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name ="UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
