package com.bytter.system.vo;
import java.io.Serializable;
import java.util.Date;

public class BtBankAccGroupVO implements Serializable {

    private String id;

    /**账户组编码*/
    private String accGroupCode;

    /**账户组名称*/
    private String accGroupName;

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

    public String getAccGroupCode() {
        return accGroupCode;
    }

    public void setAccGroupCode(String accGroupCode) {
        this.accGroupCode = accGroupCode;
    }

    public String getAccGroupName() {
        return accGroupName;
    }

    public void setAccGroupName(String accGroupName) {
        this.accGroupName = accGroupName;
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
