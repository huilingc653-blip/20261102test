package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author zhengg
 * @version V1.0
 * @Title: Entity
 * @Description: 管理单元网点单位映射表
 * @date 2020-05-15 17:18:59
 */
@Entity
@Table(name = "sys_corp_cm_map", schema = "")
@SuppressWarnings("serial")
public class CorpCmMap extends IdEntity implements java.io.Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 管理单元ID
     */
    private String cmId;
    /**
     * 创建日期
     */
    private Date createTime;
    public CorpCmMap() {
    }
    public CorpCmMap(String corpId, String cmId, Date createTime) {
        this.corpId = corpId;
        this.cmId = cmId;
        this.createTime = createTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  单位ID
     */
    @Column(name = "CORP_ID", nullable = false, length = 32)
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
     * @return: java.lang.String  管理单元ID
     */
    @Column(name = "CM_ID", nullable = false, length = 32)
    public String getCmId() {
        return this.cmId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  管理单元ID
     */
    public void setCmId(String cmId) {
        this.cmId = cmId;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_TIME", nullable = true, scale = 6)
    public Date getCreateTime() {
        return this.createTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Override
    public String toString() {
        return "CorpCmMap [" +
                "corpId=" +
                corpId + ", " +
                "cmId=" +
                cmId + ", " +
                "createTime=" +
                createTime + ", " +
                "super.toString()=" + super.toString() + "]";
    }
}
