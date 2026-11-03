package com.bytter.biz.foreign.vo;

import com.bytter.biz.foreign.entity.FexSettleFillRuleAdd;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.check.vo.IBaseDataInject;

import java.util.Date;
import java.util.List;

/**
 * @author luxy
 * @Title: FexSettleFillRuleVO
 * @ProjectName T3(3.2)
 * @Description: 结算自动填充规则VO
 * @date 2020/2/18 10:24
 */
public class FexSettleFillRuleVO extends IdEntity implements IBaseDataInject {

    private String id;
    /**
     * 单位ID
     */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "f.corp_id")
    private String corpId;
    /**
     * 单位名称
     */
    @BtCompositeSearch(fieldName = "f.corp_id")
    private String corpName;
    /**
     * 现金流类型 0-净额、1-非净额
     */
    @BtCompositeSearch(fieldName = "f.cash_flow_method")
    private String cashFlowMethod;
    /**
     * 交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易
     */
    @BtCompositeSearch(fieldName = "f.TRAN_TYPE")
    private String tranType;
    /**
     * 产品ID
     */
    @BtCompositeSearch(fieldName = "f.pro_id")
    private String proId;
    /**
     * 产品名称
     */
    @BtCompositeSearch(fieldName = "pro.pro_name")
    private String proName;
    /**
     *交易对手Id
     */
    @BtCompositeSearch(fieldName = "f.cotp_id")
    private String cotpId;
    /**
     *交易对手/组名称
     */
    @BtCompositeSearch(fieldName = "cotp.name")
    private String cotpName;
    /**
     * 币种
     */
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="curEnCode", field = "englishCode")
            }
    )
    @BtCompositeSearch(fieldName = "f.cur_id")
    private String curId;
    /**
     * 币种
     */
    @BtCompositeSearch(fieldName = "f.cur_id")
    private String curName;
    @BtCompositeSearch(fieldName = "f.cur_id")
    private String curEnCode;
  /**
     * 支付方式
     */
    @BtCompositeSearch(fieldName = "f.voucher_type_id")
    private String voucherTypeId;
    /**
     * 支付方式
     */
    @BtCompositeSearch(fieldName = "vou.voucher_name")
    private String voucherName;
    /**
     * 国家ID
     */
    @BtCompositeSearch(fieldName = "f.COUNTRY_ID")
    private String countryId;
    /**
     * 国家名称
     */
    @BtCompositeSearch(fieldName = "f.COUNTRY_ID")
    private String countryName;
    /**
     * 收付方向 0-收款 1-付款
     */
    @BtCompositeSearch(fieldName = "f.cd_sign")
    private String cdSign;
    /**
     * 账号ID
     */
    private String accId;
    /**
     * 银行账号
     */
    private String bankCode;
    /**
     * 有效标志 1-是 0-否
     */
    @BtCompositeSearch(fieldName = "f.valid_sign")
    private String validSign;
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

    /**交易对手数据源ID*/
    private String oppId;
    /**交易对手英文名称*/
    private String cotpNameEn;

    private List<FexSettleFillRuleAdd> ruleAddList;

    @Override
    public String getId() {
        return id;
    }

    @Override
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

    public String getCashFlowMethod() {
        return cashFlowMethod;
    }

    public void setCashFlowMethod(String cashFlowMethod) {
        this.cashFlowMethod = cashFlowMethod;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getCotpId() {
        return cotpId;
    }

    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    public String getCotpName() {
        return cotpName;
    }

    public void setCotpName(String cotpName) {
        this.cotpName = cotpName;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getVoucherTypeId() {
        return voucherTypeId;
    }

    public void setVoucherTypeId(String voucherTypeId) {
        this.voucherTypeId = voucherTypeId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        if(validSign == null){
            this.validSign = "1";
        }else {
            this.validSign = validSign;
        }
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

    public List<FexSettleFillRuleAdd> getRuleAddList() {
        return ruleAddList;
    }

    public void setRuleAddList(List<FexSettleFillRuleAdd> ruleAddList) {
        this.ruleAddList = ruleAddList;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCurEnCode() {
        return curEnCode;
    }

    public void setCurEnCode(String curEnCode) {
        this.curEnCode = curEnCode;
    }

    public String getOppId() {
        return oppId;
    }

    public void setOppId(String oppId) {
        this.oppId = oppId;
    }

    public String getCotpNameEn() {
        return cotpNameEn;
    }

    public void setCotpNameEn(String cotpNameEn) {
        this.cotpNameEn = cotpNameEn;
    }
}
