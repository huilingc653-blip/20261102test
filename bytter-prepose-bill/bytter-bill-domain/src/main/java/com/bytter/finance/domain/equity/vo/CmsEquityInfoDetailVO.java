package com.bytter.finance.domain.equity.vo;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsEquityInfoDetailVO extends ExcelModel implements Serializable, IBusinessKey {
    private String id;
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
    @Dict(dictGroup = "cms.equity.financingWay", inject = "financingWayName")
    private String financingWay;
    /**
     * 融资方式名称
     */
    @Excel(name = "*融资方式")
    private String financingWayName;
    /**
     * 出资人
     */
    @Excel(name = "*出资人")
    private String investor;
    /**
     * 出资方式
     */
    @Dict(dictGroup = "cms.equity.invWay", inject = "invWayName")
    private String invWay;
    /**
     * 出资方式名称
     */
    @Excel(name = "*出资方式")
    private String invWayName;
    /**
     * 出资金额
     */
    @Excel(name = "*出资金额")
    private BigDecimal invMoney;
    /**
     * 持股比例
     */
    @Excel(name = "持股比例%")
    private BigDecimal equityPro;
    /**
     * 资产名称
     */
    @Excel(name = "资产名称")
    private String assetName;
    /**
     * 业务状态
     */
    @Dict(dictGroup = "cms.equity.detail.bizStatus", inject = "bizStatusName")
    private String bizStatus;
    private String bizStatusName;
    /**
     * 备注
     */
    @Excel(name = "备注")
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
    @Override
    public String getBeanName() {
        return "cmsEquityInfoService";
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEquityId() {
        return equityId;
    }
    public void setEquityId(String equityId) {
        this.equityId = equityId;
    }
    public String getEquityCode() {
        return equityCode;
    }
    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
    }
    public String getFinancingWay() {
        return financingWay;
    }
    public void setFinancingWay(String financingWay) {
        this.financingWay = financingWay;
    }
    public String getFinancingWayName() {
        return financingWayName;
    }
    public void setFinancingWayName(String financingWayName) {
        this.financingWayName = financingWayName;
    }
    public String getInvWayName() {
        return invWayName;
    }
    public void setInvWayName(String invWayName) {
        this.invWayName = invWayName;
    }
    public String getInvestor() {
        return investor;
    }
    public void setInvestor(String investor) {
        this.investor = investor;
    }
    public String getInvWay() {
        return invWay;
    }
    public void setInvWay(String invWay) {
        this.invWay = invWay;
    }
    public BigDecimal getInvMoney() {
        return invMoney;
    }
    public void setInvMoney(BigDecimal invMoney) {
        this.invMoney = invMoney;
    }
    public BigDecimal getEquityPro() {
        return equityPro;
    }
    public void setEquityPro(BigDecimal equityPro) {
        this.equityPro = equityPro;
    }
    public String getAssetName() {
        return assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
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
    public String getBizStatusName() {
        return bizStatusName;
    }
    public void setBizStatusName(String bizStatusName) {
        this.bizStatusName = bizStatusName;
    }
    @Override
    public String toString() {
        return "CmsEquityInfoDetailVO{" + "id='" + id + '\'' + ", equityId='" + equityId + '\'' + ", equityCode='"
                + equityCode + '\'' + ", financingWay='" + financingWay + '\'' + ", financingWayName='"
                + financingWayName + '\'' + ", investor='" + investor + '\'' + ", invWay='" + invWay + '\''
                + ", invWayName='" + invWayName + '\'' + ", invMoney=" + invMoney + ", equityPro=" + equityPro
                + ", assetName='" + assetName + '\'' + ", bizStatus='" + bizStatus + '\'' + ", bizStatusName='"
                + bizStatusName + '\'' + ", rmk='" + rmk + '\'' + ", createBy='" + createBy + '\'' + ", createDate="
                + createDate + ", updateBy='" + updateBy + '\'' + ", updateDate=" + updateDate + '}';
    }
}
