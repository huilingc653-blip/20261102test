package com.bytter.system.vo;

import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@ExcelTarget("UnitStoreMaintenanceVo")
public class UnitStoreMaintenanceVo extends ExcelModel implements Serializable, IBusinessKey {

    private String id;
    @Excel(name = "*仓库编码")
    @NotNull(message = "仓库编码")
    private String storeCode;
    @Excel(name = "*仓库名称")
    @NotNull(message = "仓库名称")
    private String storeName;
    private String corpId;
    @Excel(name ="*单位编码")
    @NotNull(message = "单位编码")
    private String corpCode;
    @Excel(name ="*单位名称")
    @NotNull(message = "单位名称")
    private String corpName;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }

    @Override
    public String getBeanName() {
        return "unitStoreMaintenanceService";
    }
}
