package com.bytter.supply.domain.platform.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ScfPlatformVo implements Serializable {
    private String id;
    /**
     * 平台编码
     */
    @BtCompositeSearch(fieldName = "PLATFORM_CODE")
    private String platformCode;

    /**
     * 平台名称
     */
    @BtCompositeSearch(fieldName = "PLATFORM_NAME")
    private String platformName;


    /**
     * 有效标志
     */
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    @BtCompositeSearch(fieldName = "VALID")
    private String valid;


    private String rmk;

    /** 融资产品列表 */
    private List<ScfProductVo> productList;

    /** 接口开通列表 */
    private List<ScfBankOpenVo> bankOpenList;

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

    public List<ScfProductVo> getProductList() {
        return productList;
    }

    public void setProductList(List<ScfProductVo> productList) {
        this.productList = productList;
    }

    public List<ScfBankOpenVo> getBankOpenList() {
        return bankOpenList;
    }

    public void setBankOpenList(List<ScfBankOpenVo> bankOpenList) {
        this.bankOpenList = bankOpenList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
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
