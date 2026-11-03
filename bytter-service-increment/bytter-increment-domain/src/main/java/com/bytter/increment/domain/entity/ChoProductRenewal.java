package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 理财产品续期实体类
 *
 * @author ouyj
 */
@Entity
@Table(name = "CHO_PRODUCT_RENEWAL")
public class ChoProductRenewal extends IdEntity implements Serializable {
    /**
     * 理财登记id
     */
    private String redeemId;
    /**
     * 到期日期
     */
    private Date deadlineDate;
    /**
     * 参考收益
     */
    private BigDecimal referIncomeMoney;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 期限
     */
    private Integer dayLimit;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 修改前id
     */
    private String oldId;
    @Transient
    public String getOldId() {
        return oldId;
    }
    public void setOldId(String oldId) {
        this.oldId = oldId;
    }
    @Transient
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "DAY_LIMIT")
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "REDEEM_ID")
    public String getRedeemId() {
        return redeemId;
    }
    public void setRedeemId(String redeemId) {
        this.redeemId = redeemId;
    }
    @Column(name = "DEADLINE_DATE")
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    @Column(name = "REFER_INCOME_MONEY")
    public BigDecimal getReferIncomeMoney() {
        return referIncomeMoney;
    }
    public void setReferIncomeMoney(BigDecimal referIncomeMoney) {
        this.referIncomeMoney = referIncomeMoney;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
