package com.bytter.credit.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 授信品种
 * @author onlineGenerator
 * @date 2017-07-20 15:46:08
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAT_ITEM_CUSTOM", schema = "")
@SuppressWarnings("serial")
public class RatItemCustom extends IdEntity implements java.io.Serializable {
    /** 授信品种名称 */
    private String name;
    /** 综合标志 */
    private String flag;
    /** 品种类型 */
    private Integer type;
    /** 系统授信品种 */
    private String sysItemCode;
    /** 授信品种类型 */
    private String ritCode;
    /** 状态 */
    private Integer status;
    /** 备注 */
    private String remark;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信品种名称
     */
    @Column(name = "NAME")
    public String getName() {
        return this.name;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信品种名称
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 综合标志
     */
    @Column(name = "FLAG")
    public String getFlag() {
        return this.flag;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             综合标志
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 品种类型
     */
    @Column(name = "TYPE")
    public Integer getType() {
        return this.type;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             品种类型
     */
    public void setType(Integer type) {
        this.type = type;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 系统授信品种
     */
    @Column(name = "SYS_ITEM_CODE")
    public String getSysItemCode() {
        return this.sysItemCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             系统授信品种
     */
    public void setSysItemCode(String sysItemCode) {
        this.sysItemCode = sysItemCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信品种类型
     */
    @Column(name = "RIT_CODE")
    public String getRitCode() {
        return this.ritCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信品种类型
     */
    public void setRitCode(String ritCode) {
        this.ritCode = ritCode;
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
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    @Column(name = "REMARK")
    public String getRemark() {
        return this.remark;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
    public java.util.Date getUpdateDate() {
        return this.updateDate;
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
}
