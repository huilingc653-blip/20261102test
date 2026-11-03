package com.bytter.finance.domain.equity.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Entity
 * @Description: 缴付/过户计划主表vo
 * @date 2022-06-20
 */
public class CmsEquityPayPlanVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 融资编号
     */
    @BtCompositeSearch(fieldName = "pp.equity_Code")
    private String equityCode;
    /**
     * 融资明细id
     */
    private String infoDetailId;
    /**
     * 融资单位
     */
    @BtCompositeSearch(fieldName = "corp_Id")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 已缴付金额
     */
    private BigDecimal payMoney;
    /**
     * 出资人
     */
    @BtCompositeSearch(fieldName = "pp.investor")
    private String investor;
    /**
     * 出资方式
     */
    @BtCompositeSearch(fieldName = "pp.INV_WAY")
    @Dict(dictGroup = "cms.equity.invWay", inject = "invWayName")
    private String invWay;
    /**
     * 出资方式名称
     */
    private String invWayName;
    /**
     * 融资方式
     */
    @BtCompositeSearch(fieldName = "FINANCING_WAY")
    @Dict(dictGroup = "cms.equity.financingWay", inject = "financingWayName")
    private String financingWay;
    /**
     * 融资方式名称
     */
    private String financingWayName;
    /**
     * 出资金额
     */
    @BtCompositeSearch(fieldName = "eid.INV_MONEY")
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
     * 状态
     */
    @Dict(dictGroup = "lease.status", inject = "statusName")
    private Integer status;
    private String statusName;
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
    /**
     * 计划明细
     */
    private List<CmsEquityPayPlanDetailVO> detailDtl;
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
    public String getInfoDetailId() {
        return infoDetailId;
    }
    public void setInfoDetailId(String infoDetailId) {
        this.infoDetailId = infoDetailId;
    }
    public String getEquityCode() {
        return equityCode;
    }
    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
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
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
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
    public String getInvWayName() {
        return invWayName;
    }
    public void setInvWayName(String invWayName) {
        this.invWayName = invWayName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    public List<CmsEquityPayPlanDetailVO> getDetailDtl() {
        return detailDtl;
    }
    public void setDetailDtl(List<CmsEquityPayPlanDetailVO> detailDtl) {
        this.detailDtl = detailDtl;
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
    @Override
    public String toString() {
        return "CmsEquityPayPlanVO{" + "id='" + id + '\'' + ", equityCode='" + equityCode + '\'' + ", infoDetailId='"
                + infoDetailId + '\'' + ", corpId='" + corpId + '\'' + ", corpName='" + corpName + '\'' + ", payMoney="
                + payMoney + ", investor='" + investor + '\'' + ", invWay='" + invWay + '\'' + ", invWayName='"
                + invWayName + '\'' + ", financingWay='" + financingWay + '\'' + ", financingWayName='"
                + financingWayName + '\'' + ", invMoney=" + invMoney + ", equityPro=" + equityPro + ", assetName='"
                + assetName + '\'' + ", status=" + status + ", statusName='" + statusName + '\'' + ", createBy='"
                + createBy + '\'' + ", createDate=" + createDate + ", updateBy='" + updateBy + '\'' + ", updateDate="
                + updateDate + ", detailDtl=" + detailDtl + '}';
    }
}
