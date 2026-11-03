package com.bytter.credit.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatProcessMsg
 * @Description:授信日志实体类
 * @date 2018年11月24日 上午9:13:17
 */
@Entity
@Table(name = "RAT_RATION_LOG")
@SuppressWarnings("serial")
public class RatProcessMsg extends IdEntity implements java.io.Serializable {
    /** 授信业务ID */
    private String rationId;
    /** 日志类型 */
    private String msgType;
    /** 业务金额 */
    private java.math.BigDecimal Money;
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
     * @return: java.lang.String 授信业务ID
     */
    @Column(name = "RATION_ID")
    public String getRationId() {
        return this.rationId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信业务ID
     */
    public void setRationId(String rationId) {
        this.rationId = rationId;
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
        return this.Money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             业务金额
     */
    public void setMoney(java.math.BigDecimal Money) {
        this.Money = Money;
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
