package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author zhengg
 * @version V1.0
 * @Title: Entity
 * @Description: 组织架构单位层级关联表
 * @date 2020-05-13 17:18:24
 */
@Entity
@Table(name = "sys_organtype_corp", schema = "")
@SuppressWarnings("serial")
public class OrgantypeCorp extends IdEntity implements java.io.Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 上级单位ID
     */
    private String parentId;
    /**
     * 组织架构id
     */
    private String orgtId;
    /**
     * 创建人ID
     */
    private String createId;
    /**
     * 创建日期
     */
    private java.util.Date createTime;
    /**
     * 更新人名称
     */
    private String updateId;
    /**
     * 更新日期
     */
    private java.util.Date updateTime;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  单位ID
     */
    @Column(name = "CORP_ID", nullable = true, length = 32)
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  单位ID
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  上级单位ID
     */
    @Column(name = "PARENT_ID", nullable = true, length = 32)
    public String getParentId() {
        return this.parentId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  上级单位ID
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  组织架构id
     */
    @Column(name = "ORGT_ID", nullable = true, length = 32)
    public String getOrgtId() {
        return this.orgtId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  组织架构id
     */
    public void setOrgtId(String orgtId) {
        this.orgtId = orgtId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人ID
     */
    @Column(name = "CREATE_ID", nullable = true, length = 32)
    public String getCreateId() {
        return this.createId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人ID
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_TIME", nullable = true, scale = 6)
    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人名称
     */
    @Column(name = "UPDATE_ID", nullable = true, length = 32)
    public String getUpdateId() {
        return this.updateId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人名称
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    @Column(name = "UPDATE_TIME", nullable = true, scale = 6)
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    @Override
    public String toString() {
        return "OrgantypeCorp [" +
                "id=" +
                getId() + ", " +
                "corpId=" +
                corpId + ", " +
                "parentId=" +
                parentId + ", " +
                "orgtId=" +
                orgtId + ", " +
                "createId=" +
                createId + ", " +
                "createTime=" +
                createTime + ", " +
                "updateId=" +
                updateId + ", " +
                "updateTime=" +
                updateTime + ", " +
                "super.toString()=" + super.toString() + "]";
    }
}
