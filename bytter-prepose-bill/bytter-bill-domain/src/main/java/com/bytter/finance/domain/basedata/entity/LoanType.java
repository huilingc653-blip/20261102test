package com.bytter.finance.domain.basedata.entity;
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
 * @Description: CMS_LOAN_TYPE
 * @author onlineGenerator
 * @date 2017-07-19 14:05:55
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_LOAN_TYPE", schema = "")
@SuppressWarnings("serial")
public class LoanType extends IdEntity implements java.io.Serializable {
    /** 类型编号 */
    private String code;
    /** 类型名称 */
    private String name;
    /** 类型 */
    private String type;
    /** 状态 */
    private Integer status;
    /** 是否默认 */
    private String isDefault;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    /** 系统借款类型ID */
    private String cmsLoanTypeManagerId;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 类型编号
     */
    @Column(name = "CODE")
    public String getCode() {
        return this.code;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             类型编号
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 类型名称
     */
    @Column(name = "NAME")
    public String getName() {
        return this.name;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             类型名称
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 类型
     */
    @Column(name = "TYPE")
    public String getType() {
        return this.type;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             类型
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 状态
     */
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 是否默认
     */
    @Column(name = "IS_DEFAULT")
    public String getIsDefault() {
        return this.isDefault;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             是否默认
     */
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return this.createName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人名称
     */
    @Column(name = "UPDATE_NAME")
    public String getUpdateName() {
        return this.updateName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人名称
     */
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "CMS_LOAN_TYPE_MANAGER_ID")
    public String getCmsLoanTypeManagerId() {
        return cmsLoanTypeManagerId;
    }

    public void setCmsLoanTypeManagerId(String cmsLoanTypeManagerId) {
        this.cmsLoanTypeManagerId = cmsLoanTypeManagerId;
    }
}
