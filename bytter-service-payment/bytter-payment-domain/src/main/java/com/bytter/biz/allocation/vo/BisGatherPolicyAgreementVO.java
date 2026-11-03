package com.bytter.biz.allocation.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 内部资金归集管理协议签订
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/12
 */
public class BisGatherPolicyAgreementVO extends ExcelModel implements Serializable {
    private String id;
    @BtCompositeSearch(fieldName = "t.corp_id")
    private String corpId;
    @Excel(name = "*单位名称")
    private String corpName;
    @BtCompositeSearch(fieldName = "t.is_agreement")
    @Dict(dictGroup = "sf_yn",inject = "isAgreementStr")
    private String isAgreement;
    @Excel(name = "*是否签订协议")
    private String isAgreementStr;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIsAgreement() {
        return isAgreement;
    }

    public void setIsAgreement(String isAgreement) {
        this.isAgreement = isAgreement;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getIsAgreementStr() {
        return isAgreementStr;
    }

    public void setIsAgreementStr(String isAgreementStr) {
        this.isAgreementStr = isAgreementStr;
    }

    public String getBeanName() {
        return "bisGatherPolicyAgreementService";
    }
}
