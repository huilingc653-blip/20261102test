package com.bytter.system.vo;
import java.io.Serializable;
/**
 * @author zhengg
 * @version V1.0
 * @Title: VO
 * @Description: 组织架构单位层级关联发布表VO
 * @date 2020-05-14 16:01:41
 */
@SuppressWarnings("serial")
public class OrgantypeCorpReleaseVO implements Serializable {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 上级单位ID
     */
    private String parentId;
    /**
     * 发版表id
     */
    private String releaseId;
    /**
     * 创建人ID
     */
    private String createId;
    /**
     * 创建日期
     */
    private java.util.Date createTime;
    /**
     * updateId
     */
    private String updateId;
    /**
     * updateTime
     */
    private java.util.Date updateTime;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  主键ID
     */
    public String getId() {
        return this.id;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  主键ID
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  单位ID
     */
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
     * @return: java.lang.String  发版表id
     */
    public String getReleaseId() {
        return this.releaseId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  发版表id
     */
    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人ID
     */
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
     * @return: java.lang.String  updateId
     */
    public String getUpdateId() {
        return this.updateId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  updateId
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  updateTime
     */
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  updateTime
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    @Override
    public String toString() {
        return "OrgantypeCorpRelease [" +
                "id=" +
                id + ", " +
                "corpId=" +
                corpId + ", " +
                "parentId=" +
                parentId + ", " +
                "releaseId=" +
                releaseId + ", " +
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
