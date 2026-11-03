package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Description: 账号用途维护
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/10/23
 */
@Entity
@Table(name = "BT_BANK_ACC_USE", schema = "")
public class BtBankAccUse extends IdEntity implements Serializable {
    private String useCode;
    private String useName;
    private String useRmk;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;

    @Column(name ="USE_CODE")
    public String getUseCode() {
        return useCode;
    }

    public void setUseCode(String useCode) {
        this.useCode = useCode;
    }

    @Column(name ="USE_NAME")
    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    @Column(name ="USE_RMK")
    public String getUseRmk() {
        return useRmk;
    }

    public void setUseRmk(String useRmk) {
        this.useRmk = useRmk;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
