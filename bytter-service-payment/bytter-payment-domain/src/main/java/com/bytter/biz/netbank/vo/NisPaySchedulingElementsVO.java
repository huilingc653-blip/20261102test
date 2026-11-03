package com.bytter.biz.netbank.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;

import java.io.Serializable;
import java.util.Date;
/**
 * 付款排期要素
 */
public class NisPaySchedulingElementsVO implements Serializable {
    private String id;
    /**要素名字*/
    @BtCompositeSearch(fieldName = "n.name")
    private String name;
    /**是否有效*/
    @BtCompositeSearch(fieldName = "n.VALID_SIGN")
    @Dict(dictGroup = "valid_sign",inject = "validSignName")
    private String validSign;
    private String validSignName;
    /**状态*/
    private Integer status;
    /**是否系统导入*/
    private String isSys;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getIsSys() {
        return isSys;
    }
    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }

    public String getValidSignName() {
        return validSignName;
    }

    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
}
