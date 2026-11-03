package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_BOND_SWAP")
public class CmsBondSwap extends IdEntity implements Serializable {
    /** 债劵发行表id */
    private String issueId;
    /** 换股日 */
    private Date swapDate;
    /** 换股价 */
    private BigDecimal swapPrice;
    /** 换股数量 */
    private Integer swapAmount;
    /** 累计换股数量 */
    private Integer totalSwapAmount;
    /** 累计转股金额 */
    private BigDecimal totalSwapMoney;
    /** 交债状态 */
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
    @Column(name = "SWAP_DATE")
    public Date getSwapDate() {
        return swapDate;
    }
    public void setSwapDate(Date swapDate) {
        this.swapDate = swapDate;
    }
    @Column(name = "SWAP_PRICE")
    public BigDecimal getSwapPrice() {
        return swapPrice;
    }
    public void setSwapPrice(BigDecimal swapPrice) {
        this.swapPrice = swapPrice;
    }
    @Column(name = "SWAP_AMOUNT")
    public Integer getSwapAmount() {
        return swapAmount;
    }
    public void setSwapAmount(Integer swapAmount) {
        this.swapAmount = swapAmount;
    }
    @Column(name = "TOTAL_SWAP_AMOUNT")
    public Integer getTotalSwapAmount() {
        return totalSwapAmount;
    }
    public void setTotalSwapAmount(Integer totalSwapAmount) {
        this.totalSwapAmount = totalSwapAmount;
    }
    @Column(name = "TOTAL_SWAP_MONEY")
    public BigDecimal getTotalSwapMoney() {
        return totalSwapMoney;
    }
    public void setTotalSwapMoney(BigDecimal totalSwapMoney) {
        this.totalSwapMoney = totalSwapMoney;
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
