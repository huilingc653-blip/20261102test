package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 股权结构数据
 */
@Entity
@Table(name = "OWNERSHIP_STRUCTURE_DATA", schema = "")
public class CrOwnershipStructureData extends IdEntity implements Serializable {
    /**
     * 出资人
     */
    private String investor;
    /**
     * 融资方式
     */
    private String financingWay;
    /**
     * 出资方式
     */
    private String investmentMethod;
    /**
     * 出资金额
     */
    private BigDecimal contributionAmount;
    /**
     * 持股比例
     */
    private BigDecimal shareholdingRatio;
    /**
     * 资产名称
     */
    private String assetName;
    /**
     * 外键（结构原始数据）
     */
    private String institutionalRawDataId;


    private String rmk;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期时间:yyyy-mm-dd hh24:mm:ss
     */
    private Date createDate;

    @Column(name ="INVESTOR", length = 64)
    public String getInvestor() {
        return investor;
    }

    public void setInvestor(String investor) {
        this.investor = investor;
    }

    @Column(name ="FINANCING_WAY", length = 100)
    public String getFinancingWay() {
        return financingWay;
    }

    public void setFinancingWay(String financingWay) {
        this.financingWay = financingWay;
    }

    @Column(name ="INVESTMENT_METHOD", length = 32)
    public String getInvestmentMethod() {
        return investmentMethod;
    }

    public void setInvestmentMethod(String investmentMethod) {
        this.investmentMethod = investmentMethod;
    }

    @Column(name ="CONTRIBUTION_AMOUNT", length = 15)
    public BigDecimal getContributionAmount() {
        return contributionAmount;
    }

    public void setContributionAmount(BigDecimal contributionAmount) {
        this.contributionAmount = contributionAmount;
    }

    @Column(name ="SHAREHOLDING_RATIO", length = 6)
    public BigDecimal getShareholdingRatio() {
        return shareholdingRatio;
    }

    public void setShareholdingRatio(BigDecimal shareholdingRatio) {
        this.shareholdingRatio = shareholdingRatio;
    }

    @Column(name ="ASSET_NAME", length = 128)
    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    @Column(name ="INSTITUTIONAL_RAW_DATA_ID", length = 32)
    public String getInstitutionalRawDataId() {
        return institutionalRawDataId;
    }

    public void setInstitutionalRawDataId(String institutionalRawDataId) {
        this.institutionalRawDataId = institutionalRawDataId;
    }
    @Column(name ="RMK", length = 300)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="CREATOR", length = 100)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name ="CREATE_DATE", length = 100)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "CrOwnershipStructureData{" +
                "investor='" + investor + '\'' +
                ", financingWay='" + financingWay + '\'' +
                ", investmentMethod='" + investmentMethod + '\'' +
                ", contributionAmount=" + contributionAmount +
                ", shareholdingRatio=" + shareholdingRatio +
                ", assetName='" + assetName + '\'' +
                ", institutionalRawDataId='" + institutionalRawDataId + '\'' +
                ", rmk='" + rmk + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
