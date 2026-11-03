package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:GuaPledgeType
 * @Description:抵质押类型
 * @date 2018年12月11日 下午12:38:47
 */
@Entity
@Table(name = "GUA_PLEDGE_TYPE", schema = "")
@SuppressWarnings("serial")
public class GuaPledgeType extends IdEntity implements java.io.Serializable {
    /** 抵质押类型编号 */
    private String typeCode;
    /** 抵质押类型名称 */
    private String typeName;
    /** 抵质押所属类型 */
    private String typeAttr;
    /** 有效标识 */
    private String validSign;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 创建人登录名称 */
    private String createBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 更新人登录名称 */
    private String updateBy;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抵质押类型编号
     */
    @Column(name = "TYPE_CODE")
    public String getTypeCode() {
        return typeCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 抵质押类型编号
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抵质押类型名称
     */
    @Column(name = "TYPE_NAME")
    public String getTypeName() {
        return typeName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 抵质押类型名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抵质押所属类型
     */
    @Column(name = "TYPE_ATTR")
    public String getTypeAttr() {
        return typeAttr;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 抵质押所属类型
     */
    public void setTypeAttr(String typeAttr) {
        this.typeAttr = typeAttr;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 有效标识
     */
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 有效标识
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return createDate;
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
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
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
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
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
}
