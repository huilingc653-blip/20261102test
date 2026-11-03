package com.bytter.guarantee.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import java.io.Serializable;

public class FcBondLogVO implements Serializable {
    /** 保证金ID */
    private String bondId;
    /** 日志类型 */
    private String msgType;
    /** 业务金额 */
    private java.math.BigDecimal money;
    /** 创建人代码 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createDate;
    /** 创建人名称 */
    private String createByName;
    /** 日志内容 */
//	@Dict(dictGroup="cms.messageType",inject="message")
    private String message;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保证金ID
     */
    @Column(name = "BOND_ID", nullable = true, length = 32)
    public String getBondId() {
        return this.bondId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             保证金ID
     */
    public void setBondId(String bondId) {
        this.bondId = bondId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 日志类型
     */
    @Column(name = "MSG_TYPE", nullable = true, length = 2)
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
    @Column(name = "MONEY", nullable = true, scale = 2, length = 18)
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
    @Column(name = "CREATE_BY", nullable = true, length = 50)
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
    @Column(name = "CREATE_DATE", nullable = true)
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
    @Column(name = "CREATEBY_NAME", nullable = true, length = 50)
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
    @Column(name = "MESSAGE", nullable = true, length = 150)
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
