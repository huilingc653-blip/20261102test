package com.bytter.supply.domain.platform.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ScfBankOpen
 * @Description 接口开通情况表
 */
@Entity
@Table(name = "SCF_BANK_OPEN", schema = "")
public class ScfBankOpen extends IdEntity implements Serializable {

    /**
     * 接口类型
     */
    private String type;

    /**
     * 接口名称（中文）
     */
    private String apiNameCn;

    /**
     * 接口名称
     */
    private String apiName;


    /**
     * 平台id
     */
    private String scfPlatformId;

    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;



    @Column(name = "type", length = 2)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "API_NAME_CN", length = 100)
    public String getApiNameCn() {
        return apiNameCn;
    }

    public void setApiNameCn(String apiNameCn) {
        this.apiNameCn = apiNameCn;
    }

    @Column(name = "API_NAME", length = 100)
    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }


    @Column(name = "SCF_PLATFORM_ID")
    public String getScfPlatformId() {
        return scfPlatformId;
    }

    public void setScfPlatformId(String scfPlatformId) {
        this.scfPlatformId = scfPlatformId;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
