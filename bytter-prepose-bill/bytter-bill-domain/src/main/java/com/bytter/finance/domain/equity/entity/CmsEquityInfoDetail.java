package com.bytter.finance.domain.equity.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 股权融资明细表
 * @date 2022-06-08
 */
@Entity
@Table(name = "CMS_EQUITY_INFO_DETAIL", schema = "")
public class CmsEquityInfoDetail extends IdEntity implements java.io.Serializable {
    /**
     * 股权id
     */
    private String equityId;
    /**
     * 融资编号
     */
    private String equityCode;
    /**
     * 融资方式
     */
    private String financingWay;
    /**
     * 出资人
     */
    private String investor;
    /**
     * 出资方式
     */
    private String invWay;
    /**
     * 出资金额
     */
    private BigDecimal invMoney;
    /**
     * 持股比例
     */
    private BigDecimal equityPro;
    /**
     * 资产名称
     */
    private String assetName;
    /**
     * 业务状态
     */
    private String bizStatus;
    /**
     * 备注
     */
    private String rmk;
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
    @Column(name = "EQUITY_ID")
    public String getEquityId() {
        return equityId;
    }
    public void setEquityId(String equityId) {
        this.equityId = equityId;
    }
    @Column(name = "EQUITY_CODE")
    public String getEquityCode() {
        return equityCode;
    }
    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
    }
    @Column(name = "FINANCING_WAY")
    public String getFinancingWay() {
        return financingWay;
    }
    public void setFinancingWay(String financingWay) {
        this.financingWay = financingWay;
    }
    @Column(name = "INVESTOR")
    public String getInvestor() {
        return investor;
    }
    public void setInvestor(String investor) {
        this.investor = investor;
    }
    @Column(name = "INV_WAY")
    public String getInvWay() {
        return invWay;
    }
    public void setInvWay(String invWay) {
        this.invWay = invWay;
    }
    @Column(name = "INV_MONEY")
    public BigDecimal getInvMoney() {
        return invMoney;
    }
    public void setInvMoney(BigDecimal invMoney) {
        this.invMoney = invMoney;
    }
    @Column(name = "EQUITY_PRO")
    public BigDecimal getEquityPro() {
        return equityPro;
    }
    public void setEquityPro(BigDecimal equityPro) {
        this.equityPro = equityPro;
    }
    @Column(name = "ASSET_NAME")
    public String getAssetName() {
        return assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
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
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
