package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Title: VO
 * @Description: 主体单位默认银行账户VO
 * @author fusb
 * @date 2020-02-17 10:31:56
 * @version 1.0
 */
public class FexCorpBankAccVO extends ExcelModel implements Serializable, IBaseDataInject, IBusinessKey {
    /**主键id*/
    private String id;
    /**业务类型：0主体单位，1交易对手*/
    private String busType;
    /**主体id：busType为0时，主体单位id；busType为1时，交易对手数据源id（客商id）*/
    @BtCompositeSearch(fieldName = "b.corp_id")
    private String corpId;
    /**主体名称*/
    private String corpName;

    /**
     * 主体单位银行账户
     */
    private String corpAccId;

    /**交易类型：1外汇交易、2外汇期权交易、3调拨交易、
     * 4资金收付交易、5货币市场交易、6债券交易
     */
    @BtCompositeSearch(fieldName = "b.tran_type")
    private String tranType;
    /**产品id*/
    @BtCompositeSearch(fieldName = "b.pro_id")
    private String proId;
    /**产品名称*/
    private String proName;
    /**对方id：busType为0时，交易对手数据源id（客商/单位/银行id）；busType为1时，主体单位id*/
    @BtCompositeSearch(fieldName = "b.opp_corp_id")
    private String oppCorpId;
    /**对方名称*/
    @BtCompositeSearch(fieldName = "c.cotp_name")
    private String oppCorpName;
    /**收付方向：0收款，1付款*/
    @BtCompositeSearch(fieldName = "b.cd_sign")
    private String cdSign;
    /**现金流类型：0本金，1利息*/
    @BtCompositeSearch(fieldName = "b.cash_flow_type")
    private String cashFlowType;
    /**币种*/
    @BtCompositeSearch(fieldName = "b.cur_id")
    @Currencys(
        {
           @Currency(inject = "curName",field = "englishCode")
         }
    )
    private String curId;
    /**币种名称*/
    private String curName;
    /**状态：0暂存，1打回，2删除，11待审批，95审批通过*/
    @BtCompositeSearch(fieldName = "b.status")
    private Integer status;
    /**创建人*/
    private String createBy;
    /**创建人名称*/
    private String createByName;
    /**创建时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**更新人*/
    private String updateBy;
    /**更新人名称*/
    private String updateByName;
    /**更新时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**主体单位默认银行账户明细*/
    private List<FexCorpBankAccDtlVO> fexCorpBankAccDtl;

    /**交易对手组ID*/
    private String cotpParentId;

    /**交易对手数据源ID*/
    private String oppId;
    /**交易对手英文名称*/
    private String cotpNameEn;

    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
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

    public String getOppCorpId() {
        return oppCorpId;
    }
    public void setOppCorpId(String oppCorpId) {
        this.oppCorpId = oppCorpId;
    }

    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public String getCashFlowType() {
        return cashFlowType;
    }
    public void setCashFlowType(String cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getProName() {
        return proName;
    }
    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getOppCorpName() {
        return oppCorpName;
    }
    public void setOppCorpName(String oppCorpName) {
        this.oppCorpName = oppCorpName;
    }

    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public List<FexCorpBankAccDtlVO> getFexCorpBankAccDtl() {
        return fexCorpBankAccDtl;
    }
    public void setFexCorpBankAccDtl(List<FexCorpBankAccDtlVO> fexCorpBankAccDtl) {
        this.fexCorpBankAccDtl = fexCorpBankAccDtl;
    }

    public String getCotpParentId() {
        return cotpParentId;
    }

    public void setCotpParentId(String cotpParentId) {
        this.cotpParentId = cotpParentId;
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

    public String getCorpAccId() {
        return corpAccId;
    }

    public void setCorpAccId(String corpAccId) {
        this.corpAccId = corpAccId;
    }

    @Override
    public String getBusinessKey() {
        return this.getId();
    }
}
