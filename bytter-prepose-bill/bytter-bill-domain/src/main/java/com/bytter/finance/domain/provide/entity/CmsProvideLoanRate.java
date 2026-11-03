package com.bytter.finance.domain.provide.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: CMS_PROVIDE_LOAN_RATE
 * @author onlineGenerator
 * @date 2017-08-29 10:36:00
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PROVIDE_LOAN_RATE", schema = "")
@SuppressWarnings("serial")
public class CmsProvideLoanRate extends IdEntity implements java.io.Serializable {
    /** 借款单号 */
    private String billCode;
    /** 借款利率 */
    private BigDecimal plRate;
    /** 展期利率 */
    private BigDecimal deferredRate;
    /** 预期利率 */
    private BigDecimal overdueRate;
    /** 状态 */
    private Integer status;
    /** 利率启用日期 */
    private Date enterDate;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 借款单号
     */
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             借款单号
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    @Column(name = "Create_Name")
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    @Column(name = "Create_By")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "Create_Date")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 借款利率
     */
    @Column(name = "PL_RATE")
    public BigDecimal getPlRate() {
        return this.plRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             借款利率
     */
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 展期利率
     */
    @Column(name = "DEFERRED_RATE")
    public BigDecimal getDeferredRate() {
        return this.deferredRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             展期利率
     */
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 预期利率
     */
    @Column(name = "OVERDUE_RATE")
    public BigDecimal getOverdueRate() {
        return this.overdueRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             预期利率
     */
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 利率启用日期
     */
    @Column(name = "ENTER_DATE")
    public Date getEnterDate() {
        return this.enterDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             利率启用日期
     */
    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }
    @Override
    public String toString() {
        return "CmsProvideLoanRate [billCode=" + billCode + ", plRate=" + plRate + ", deferredRate=" + deferredRate
                + ", overdueRate=" + overdueRate + ", status=" + status + ", enterDate=" + enterDate + ", createName="
                + createName + ", createBy=" + createBy + ", createDate=" + createDate + "]";
    }
}
