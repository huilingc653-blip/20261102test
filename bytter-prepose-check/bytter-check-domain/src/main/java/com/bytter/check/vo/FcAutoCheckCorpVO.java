package com.bytter.check.vo;
import java.io.Serializable;
import java.util.Date;
public class FcAutoCheckCorpVO implements Serializable {
    private String id;
    /**
     * 自动对账策略代码
     */
    private String autoCheckCode;
    /**
     * 对账类型
     */
    private String checkType;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 上级id
     */
    private String parentId;
    /**
     * 核对标准
     */
    private String checkTheStandard;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
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
    public String getAutoCheckCode() {
        return autoCheckCode;
    }
    public void setAutoCheckCode(String autoCheckCode) {
        this.autoCheckCode = autoCheckCode;
    }
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getCheckTheStandard() {
        return checkTheStandard;
    }
    public void setCheckTheStandard(String checkTheStandard) {
        this.checkTheStandard = checkTheStandard;
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
