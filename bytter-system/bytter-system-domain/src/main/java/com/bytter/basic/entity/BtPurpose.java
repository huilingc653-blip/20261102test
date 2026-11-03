package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 用途维护表
 * @date 2017-05-24 09:57:15
 */
@Entity
@Table(name = "BT_PURPOSE", schema = "")
@SuppressWarnings("serial")
public class BtPurpose extends IdEntity implements java.io.Serializable {
    /**
     * 用途
     */
    private String purpose;
    /**
     * 所属类别
     */
    private String sysBusType;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  用途
     */
    @Column(name = "PURPOSE", nullable = true, length = 100)
    public String getPurpose() {
        return this.purpose;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  用途
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  所属类别
     */
    @Column(name = "SYS_BUS_TYPE", nullable = true, length = 1)
    public String getSysBusType() {
        return this.sysBusType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  所属类别
     */
    public void setSysBusType(String sysBusType) {
        this.sysBusType = sysBusType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人登录名称
     */
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人登录名称
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
}
