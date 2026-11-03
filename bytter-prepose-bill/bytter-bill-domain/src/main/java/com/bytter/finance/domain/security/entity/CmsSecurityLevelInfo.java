package com.bytter.finance.domain.security.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 资产分层信息表
 * @date 2022-07-22
 */
@Entity
@Table(name = "CMS_SECURITY_LEVEL_INFO", schema = "")
public class CmsSecurityLevelInfo extends IdEntity implements java.io.Serializable {
    /**
     * 证券主信息id
     */
    private String securityInfoId;
    /**
     * 层级
     */
    private String levelId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 预计发行规模
     */
    private BigDecimal expectIssSize;
    /**
     * 规模占比
     */
    private BigDecimal sizeOf;
    /**
     * 期限
     */
    private BigDecimal timeLimit;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
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
    @Column(name = "SECURITY_INFO_ID")
    public String getSecurityInfoId() {
        return securityInfoId;
    }
    public void setSecurityInfoId(String securityInfoId) {
        this.securityInfoId = securityInfoId;
    }
    @Column(name = "LEVEL_ID")
    public String getLevelId() {
        return levelId;
    }
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }
    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    @Column(name = "EXPECT_ISS_SIZE")
    public BigDecimal getExpectIssSize() {
        return expectIssSize;
    }
    public void setExpectIssSize(BigDecimal expectIssSize) {
        this.expectIssSize = expectIssSize;
    }
    @Column(name = "SIZE_OF")
    public BigDecimal getSizeOf() {
        return sizeOf;
    }
    public void setSizeOf(BigDecimal sizeOf) {
        this.sizeOf = sizeOf;
    }
    @Column(name = "TIME_LIMIT")
    public BigDecimal getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(BigDecimal timeLimit) {
        this.timeLimit = timeLimit;
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
