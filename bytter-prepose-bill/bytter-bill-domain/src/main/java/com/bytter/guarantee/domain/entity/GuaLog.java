package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaLog 担保日志实体类
 * @Description
 * @date 2018年12月29日
 */
@Entity
@Table(name = "GUA_LOG")
@SuppressWarnings("serial")
public class GuaLog extends IdEntity implements java.io.Serializable {
    /** 担保ID */
    private String guaId;
    /** 日志类型 */
    private String msgType;
    /** 业务金额 */
    private java.math.BigDecimal money;
    /** 创建人代码 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createDate;
    /** 创建人名称 */
    private String createByName;
    /** 日志内容 */
    private String message;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 保证金ID
     */
    @Column(name = "GUA_ID")
    public String getGuaId() {
        return this.guaId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             担保id
     */
    public void setGuaId(String guaId) {
        this.guaId = guaId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 日志类型
     */
    @Column(name = "MSG_TYPE")
    public String getMsgType() {
        return this.msgType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             日志类型
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 业务金额
     */
    @Column(name = "MONEY")
    public java.math.BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             业务金额
     */
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
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
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @param: java.lang.String
     *             用户名称
     */
    @Column(name = "CREATEBY_NAME")
    public String getCreateByName() {
        return createByName;
    }
    /**
     * 方法: 设置java.lang.String 用户名称
     * 
     * @param: java.lang.String
     */
    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }
    /**
     * 方法: 取得java.lang.String 日志内容
     * 
     * @param: java.lang.String
     */
    @Column(name = "MESSAGE")
    public String getMessage() {
        return message;
    }
    /**
     * 方法: 设置java.lang.String 日志内容
     * 
     * @param: java.lang.String
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
