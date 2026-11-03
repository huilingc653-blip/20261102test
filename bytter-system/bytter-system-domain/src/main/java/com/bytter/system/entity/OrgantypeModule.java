package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author zhengg
 * @version V1.0
 * @Title: Entity
 * @Description: 组织架构对应关系维护表
 * @date 2020-05-13 17:18:25
 */
@Entity
@Table(name = "sys_organtype_module", schema = "")
@SuppressWarnings("serial")
public class OrgantypeModule extends IdEntity implements java.io.Serializable {
    /**
     * 模块代码
     */
    private String sysCode;
    /**
     * 组织机构ID
     */
    private String orgtId;
    /**
     * 更新人ID
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private java.util.Date updateTime;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  模块代码
     */
    @Column(name = "SYS_CODE", nullable = true, length = 50)
    public String getSysCode() {
        return this.sysCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  模块代码
     */
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  组织机构ID
     */
    @Column(name = "ORGT_ID", nullable = true, length = 32)
    public String getOrgtId() {
        return this.orgtId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  组织机构ID
     */
    public void setOrgtId(String orgtId) {
        this.orgtId = orgtId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人ID
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人ID
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新时间
     */
    @Column(name = "UPDATE_TIME", nullable = true, scale = 6)
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    @Override
    public String toString() {
        return "OrgantypeModule [" +
                "id=" +
                getId() + ", " +
                "sysCode=" +
                sysCode + ", " +
                "orgtId=" +
                orgtId + ", " +
                "updateBy=" +
                updateBy + ", " +
                "updateTime=" +
                updateTime + ", " +
                "super.toString()=" + super.toString() + "]";
    }
}
