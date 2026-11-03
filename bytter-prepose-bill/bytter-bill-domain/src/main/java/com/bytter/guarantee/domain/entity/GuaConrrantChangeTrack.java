package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
@Entity
@Table(name = "GUA_CONRRANT_CHANGE_TRACK", schema = "")
@SuppressWarnings("serial")
public class GuaConrrantChangeTrack extends IdEntity implements java.io.Serializable {
    /** 担保id */
    private String guaId;
    /** 担保编号 */
    private String guaNo;
    /** 被担保合同编号 */
    private String contractCode;
    /** 抵质押物id */
    private String pledgeId;
    /** 占用或释放金额 */
    private java.math.BigDecimal money;
    /** 类型 */
    private String trackType;
    /** 创建日期 */
    private java.util.Date createTime;
    /** 创建人登录名称 */
    private String createBy;
    /** 担保方式id */
    private String guaWayId;
    /** 担保类型 */
    private String conType;
    @Column(name = "GUA_ID")
    public String getGuaId() {
        return guaId;
    }
    public void setGuaId(String guaId) {
        this.guaId = guaId;
    }
    @Column(name = "PLEDGE_ID")
    public String getPledgeId() {
        return pledgeId;
    }
    public void setPledgeId(String pledgeId) {
        this.pledgeId = pledgeId;
    }
    @Column(name = "MONEY")
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    @Column(name = "TRACK_TYPE")
    public String getTrackType() {
        return trackType;
    }
    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }
    @Column(name = "CREATE_TIME")
    public java.util.Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "GUA_NO")
    public String getGuaNo() {
        return guaNo;
    }
    public void setGuaNo(String guaNo) {
        this.guaNo = guaNo;
    }
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    @Column(name = "GUA_WAY_ID")
    public String getGuaWayId() {
        return guaWayId;
    }
    public void setGuaWayId(String guaWayId) {
        this.guaWayId = guaWayId;
    }
    @Column(name = "CON_TYPE")
    public String getConType() {
        return conType;
    }
    public void setConType(String conType) {
        this.conType = conType;
    }
}
