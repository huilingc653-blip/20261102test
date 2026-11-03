package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_BOND_TRANSFER")
public class CmsBondTransfer extends IdEntity implements Serializable {
    // 转股日，转股价，转股数量，累计转股数量，累计转股比例，累计转股金额，状态
    /** 债劵发行表id */
    private String issueId;
    /** 转股日 */
    private Date transferDate;
    /** 转股价 */
    private BigDecimal transferPrice;
    /** 转股数量 */
    private Integer transferAmount;
    /** 累计转股数量 */
    private Integer totalTransferAmount;
    /** 累计转股金额 */
    private BigDecimal totalTransferMoney;
    /** 累计转股比例 */
    private BigDecimal totalTransferRate;
    /** 转债状态 */
    private Integer status;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    @Column(name = "ISSUE_ID")
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    @Column(name = "TRANSFER_DATE")
    public Date getTransferDate() {
        return transferDate;
    }
    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }
    @Column(name = "TRANSFER_PRICE")
    public BigDecimal getTransferPrice() {
        return transferPrice;
    }
    public void setTransferPrice(BigDecimal transferPrice) {
        this.transferPrice = transferPrice;
    }
    @Column(name = "TRANSFER_AMOUNT")
    public Integer getTransferAmount() {
        return transferAmount;
    }
    public void setTransferAmount(Integer transferAmount) {
        this.transferAmount = transferAmount;
    }
    @Column(name = "TOTAL_TRANSFER_AMOUNT")
    public Integer getTotalTransferAmount() {
        return totalTransferAmount;
    }
    public void setTotalTransferAmount(Integer totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }
    @Column(name = "TOTAL_TRANSFER_MONEY")
    public BigDecimal getTotalTransferMoney() {
        return totalTransferMoney;
    }
    public void setTotalTransferMoney(BigDecimal totalTransferMoney) {
        this.totalTransferMoney = totalTransferMoney;
    }
    @Column(name = "TOTAL_TRANSFER_RATE")
    public BigDecimal getTotalTransferRate() {
        return totalTransferRate;
    }
    public void setTotalTransferRate(BigDecimal totalTransferRate) {
        this.totalTransferRate = totalTransferRate;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
