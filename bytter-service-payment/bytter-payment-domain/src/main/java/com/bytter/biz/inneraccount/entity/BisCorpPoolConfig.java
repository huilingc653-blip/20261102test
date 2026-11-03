package com.bytter.biz.inneraccount.entity;

import com.bytter.core.base.entity.IdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 多级单位余额核算-单位归集配置表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_CORP_POOL_CONFIG", schema = "")
@SuppressWarnings("serial")
@Data
public class BisCorpPoolConfig extends IdEntity implements java.io.Serializable  {
    /**规则编码*/
    private String configCode;
    /**规则名称*/
    private String configName;
    /**类型*/
    private String busType;
    /**有效标志*/
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createTime;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateTime;

    @Column(name = "CONFIG_CODE")
    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    @Column(name = "CONFIG_NAME")
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @Column(name = "BUS_TYPE")
    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
