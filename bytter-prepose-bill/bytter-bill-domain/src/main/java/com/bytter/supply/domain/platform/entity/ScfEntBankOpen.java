package com.bytter.supply.domain.platform.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName ScfEntBankOpen
 * @Description 平台接口开通配置
 * @version 1.0
 */
@Entity
@Table(name = "SCF_ENT_BANK_OPEN", schema = "")
@SuppressWarnings("serial")
public class ScfEntBankOpen extends IdEntity implements java.io.Serializable {

    /** 接口类型 */
    private String type;
    /** 接口名称（中文） */
    private String apiNameCn;
    /** 接口名称 */
    private String apiName;
    /** 是否具备直连接口 */
    private String hasDirect;
    /** 直连接口 */
    private String direct;
    /** 平台id */
    private String entPlatformId;
    /** 创建人 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;

    @Column(name = "ENT_PLATFORM_ID", length = 32)
    public String getEntPlatformId() {
        return entPlatformId;
    }

    public void setEntPlatformId(String entPlatformId) {
        this.entPlatformId = entPlatformId;
    }

    @Column(name = "TYPE", length = 2)
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

    @Column(name = "HAS_DIRECT", length = 1)
    public String getHasDirect() {
        return hasDirect;
    }

    public void setHasDirect(String hasDirect) {
        this.hasDirect = hasDirect;
    }

    @Column(name = "DIRECT", length = 32)
    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
