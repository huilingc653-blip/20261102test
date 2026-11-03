package com.bytter.biz.inneraccount.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 多级单位余额核算-单位归集余额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_CORP_POOL_BAL", schema = "")
public class BisCorpPoolBal extends IdEntity implements java.io.Serializable  {
    /**单位ID*/
    private String corpId;
    /**币别ID*/
    private String curId;
    /**余额*/
    private BigDecimal bal;
    /**余额日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date balDate;
    /**获取时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date getDate;
    /**初始化标志0-否 1-是*/
    private String initSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createTime;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateTime;

    @Column(name = "CORP_ID", nullable = true, length = 32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "CUR_ID", nullable = true, length = 32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "BAL", nullable = true, scale = 2, length = 19)
    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    @Column(name = "BAL_DATE", nullable = true)
    public Date getBalDate() {
        return balDate;
    }

    public void setBalDate(Date balDate) {
        this.balDate = balDate;
    }

    @Column(name = "GET_DATE", nullable = true)
    public Date getGetDate() {
        return getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }

    @Column(name = "INIT_SIGN")
    public String getInitSign() {
        return initSign;
    }

    public void setInitSign(String initSign) {
        this.initSign = initSign;
    }

    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
