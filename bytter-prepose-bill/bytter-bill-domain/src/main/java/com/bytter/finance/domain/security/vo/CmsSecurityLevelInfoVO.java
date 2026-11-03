package com.bytter.finance.domain.security.vo;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 资产分层信息表
 *
 * @author tongqr
 */
public class CmsSecurityLevelInfoVO extends ExcelModel implements Serializable {
    /**
     * 证券主信息id
     */
    private String securityInfoId;
    /**
     * 层级
     */
    private String levelId;
    @Excel(name = "*层级")
    private String levelName;
    /**
     * 产品名称
     */
    @Excel(name = "*产品名称")
    private String productName;
    /**
     * 预计发行规模
     */
    @Excel(name = "*预计发行规模")
    private BigDecimal expectIssSize;
    /**
     * 规模占比
     */
    private BigDecimal sizeOf;
    /**
     * 期限
     */
    @Excel(name = "期限（年）")
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
    public String getSecurityInfoId() {
        return securityInfoId;
    }
    public void setSecurityInfoId(String securityInfoId) {
        this.securityInfoId = securityInfoId;
    }
    public String getLevelId() {
        return levelId;
    }
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }
    public String getLevelName() {
        return levelName;
    }
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public BigDecimal getExpectIssSize() {
        return expectIssSize;
    }
    public void setExpectIssSize(BigDecimal expectIssSize) {
        this.expectIssSize = expectIssSize;
    }
    public BigDecimal getSizeOf() {
        return sizeOf;
    }
    public void setSizeOf(BigDecimal sizeOf) {
        this.sizeOf = sizeOf;
    }
    public BigDecimal getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(BigDecimal timeLimit) {
        this.timeLimit = timeLimit;
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
    public String getBeanName() {
        return "cmsSecurityInfoService";
    }
}
