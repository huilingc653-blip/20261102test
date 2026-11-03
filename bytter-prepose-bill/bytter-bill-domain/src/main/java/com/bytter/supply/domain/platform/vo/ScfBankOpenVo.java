package com.bytter.supply.domain.platform.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ScfBankOpenVo
 * @Description 接口开通情况VO
 */
public class ScfBankOpenVo implements Serializable {

    private String id;

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
     * 排序号
     */
    private String sort;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApiNameCn() {
        return apiNameCn;
    }

    public void setApiNameCn(String apiNameCn) {
        this.apiNameCn = apiNameCn;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

//    public String getValid() {
//        return valid;
//    }
//
//    public void setValid(String valid) {
//        this.valid = valid;
//    }

    public String getScfPlatformId() {
        return scfPlatformId;
    }

    public void setScfPlatformId(String scfPlatformId) {
        this.scfPlatformId = scfPlatformId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
