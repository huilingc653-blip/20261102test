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
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @Title: Entity
 * @Description: CMS_PROCESS_MSG
 * @author onlineGenerator
 * @date 2017-09-14 14:32:39
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PROCESS_MSG", schema = "")
@SuppressWarnings("serial")
public class CmsProcessMsg extends IdEntity implements java.io.Serializable {
    /** 单据号 */
    private String billCode;
    /** 消息类型 */
    private Integer messageType;
    /** 消息 */
    private String message;
    /** 业务类型 */
    private String operationType;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单据号
     */
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             单据号
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 消息类型
     */
    @Column(name = "MESSAGE_TYPE")
    public Integer getMessageType() {
        return this.messageType;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             消息类型
     */
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 消息
     */
    @Column(name = "MESSAGE")
    public String getMessage() {
        return this.message;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             消息
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务类型
     */
    @Column(name = "OPERATION_TYPE")
    public String getOperationType() {
        return this.operationType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             业务类型
     */
    public void setOperationType(String operationType) {
        this.operationType = operationType;
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
}
