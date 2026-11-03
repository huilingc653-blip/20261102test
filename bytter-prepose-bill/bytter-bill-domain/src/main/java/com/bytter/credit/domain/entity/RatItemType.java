package com.bytter.credit.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 授信品种
 * @author onlineGenerator
 * @date 2017-07-20 14:52:52
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAT_ITEM_TYPE", schema = "")
@SuppressWarnings("serial")
public class RatItemType extends IdEntity implements java.io.Serializable {
    /** 品种类型编码 */
    private String ritCode;
    /** 品种类型名称 */
    private String ritName;
    /** 品种类型描述 */
    private String ritDesc;
    /** 单位代码 */
    private String corpId;
    /** 状态 */
    private Integer status;
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
     * @return: java.lang.String 品种类型编码
     */
    @Column(name = "RIT_CODE")
    public String getRitCode() {
        return this.ritCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             品种类型编码
     */
    public void setRitCode(String ritCode) {
        this.ritCode = ritCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 品种类型名称
     */
    @Column(name = "RIT_NAME")
    public String getRitName() {
        return this.ritName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             品种类型名称
     */
    public void setRitName(String ritName) {
        this.ritName = ritName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 品种类型描述
     */
    @Column(name = "RIT_DESC")
    public String getRitDesc() {
        return this.ritDesc;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             品种类型描述
     */
    public void setRitDesc(String ritDesc) {
        this.ritDesc = ritDesc;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
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
