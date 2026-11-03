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
 * @Description: CMS_PROVIDE_LOAN_ABOLISH
 * @author onlineGenerator
 * @date 2017-08-15 15:39:47
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PROVIDE_LOAN_ABOLISH", schema = "")
@SuppressWarnings("serial")
public class CmsProvideLoanAbolish extends IdEntity implements java.io.Serializable {
    /** 被删除的bill_code */
    private String pliCode;
    /** 单位id */
    private String corpId;
    /** 录入日期 */
    private Date loanDate;
    /** 操作员 */
    private String op;
    /** 状态 */
    private Integer status;
    /** 删除日期 */
    private Date abolishedDate;
    /** 备注 */
    private String rmk;
    /** 流程状态 */
    private String bizStatus;
    /** 被作废的单据状态 */
    private Integer abolishedStatus;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 被删除的bill_code
     */
    @Column(name = "PLI_CODE")
    public String getPliCode() {
        return this.pliCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             被删除的bill_code
     */
    public void setPliCode(String pliCode) {
        this.pliCode = pliCode;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 单位id
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             单位id
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 录入日期
     */
    @Column(name = "LOAN_DATE")
    public Date getLoanDate() {
        return this.loanDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             录入日期
     */
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 操作员
     */
    @Column(name = "OP")
    public String getOp() {
        return this.op;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             操作员
     */
    public void setOp(String op) {
        this.op = op;
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
     * @return: java.util.Date 删除日期
     */
    @Column(name = "ABOLISHED_DATE")
    public Date getAbolishedDate() {
        return this.abolishedDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             删除日期
     */
    public void setAbolishedDate(Date abolishedDate) {
        this.abolishedDate = abolishedDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    @Column(name = "ABOLISHED_STATUS")
    public Integer getAbolishedStatus() {
        return abolishedStatus;
    }
    public void setAbolishedStatus(Integer abolishedStatus) {
        this.abolishedStatus = abolishedStatus;
    }
}
