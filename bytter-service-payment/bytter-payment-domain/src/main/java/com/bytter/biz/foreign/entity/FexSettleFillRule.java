package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author luxy
 * @Title: FexSettleFillRule
 * @ProjectName T3(3.2)
 * @Description: 结算自动填充规则
 * @date 2020/2/18 10:24
 */
@Entity
@Table(name="FEX_SETTLE_FILL_RULE")
public class FexSettleFillRule extends IdEntity implements java.io.Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 现金流类型 0-净额、1-非净额
     */
    private String cashFlowMethod;
    /**
     * 交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易
     */
    private String tranType;
    /**
     * 产品ID
     */
    private String proId;
    /**
     *交易对手Id
     */
    private String cotpId;
    /**
     * 币种
     */
    private String curId;
    /**
     * 支付方式
     */
    private String voucherTypeId;
    /**
     * 国家ID
     */
    private String countryId;
    /**
     * 收付方向 0-收款 1-付款
     */
    private String cdSign;
    /**
     * 账号ID
     */
    private String accId;
    /**
     * 有效标志 1-是 0-否
     */
    private String validSign="1";
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 更新人
     */
    private String updateBy;

    @Column(name ="CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="CASH_FLOW_METHOD")
    public String getCashFlowMethod() {
        return cashFlowMethod;
    }

    public void setCashFlowMethod(String cashFlowMethod) {
        this.cashFlowMethod = cashFlowMethod;
    }
    @Column(name ="TRAN_TYPE")
    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }
    @Column(name ="PRO_ID")
    public String getProId() {
        return proId;
    }
    public void setProId(String proId) {
        this.proId = proId;
    }
    @Column(name ="COTP_ID")
    public String getCotpId() {
        return cotpId;
    }
    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }


    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name ="VOUCHER_TYPE_ID")
    public String getVoucherTypeId() {
        return voucherTypeId;
    }

    public void setVoucherTypeId(String voucherTypeId) {
        this.voucherTypeId = voucherTypeId;
    }
    @Column(name ="COUNTRY_ID")
    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
    @Column(name ="CD_SIGN")
    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }
    @Column(name ="ACC_ID")
    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
    @Column(name ="VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
