package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 主体单位默认银行账户实体
 * @author fusb
 * @date 2020-02-17 10:31:56
 * @version 1.0
 */
@Entity
@Table(name = "FEX_CORP_BANKACC", schema = "")
public class FexCorpBankAcc extends IdEntity {
    /**业务类型：0主体单位，1交易对手*/
    private String busType;
    /**主体id：busType为0时，主体单位id；busType为1时，交易对手数据源id（客商id）*/
    private String corpId;
    /**交易类型：1外汇交易、2外汇期权交易、3调拨交易、
     * 4资金收付交易、5货币市场交易、6债券交易
     */
    private String tranType;
    /**产品id*/
    private String proId;
    /**对方id：busType为0时，交易对手数据源id（客商/单位/银行id）；busType为1时，主体单位id*/
    private String oppCorpId;
    /**收付方向：0收款，1付款*/
    private String cdSign;
    /**现金流类型：0本金，1利息*/
    private String cashFlowType;
    /**币种*/
    private String curId;
    /**状态：0暂存，1打回，2删除，11待审批，95审批通过*/
    private Integer status;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    private Date createDate;
    /**更新人*/
    private String updateBy;
    /**更新时间*/
    private Date updateDate;

    @Column(name = "BUS_TYPE")
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "TRAN_TYPE")
    public String getTranType() {
        return tranType;
    }
    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    @Column(name = "PRO_ID")
    public String getProId() {
        return proId;
    }
    public void setProId(String proId) {
        this.proId = proId;
    }

    @Column(name = "OPP_CORP_ID")
    public String getOppCorpId() {
        return oppCorpId;
    }
    public void setOppCorpId(String oppCorpId) {
        this.oppCorpId = oppCorpId;
    }

    @Column(name = "CD_SIGN")
    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    @Column(name = "CASH_FLOW_TYPE")
    public String getCashFlowType() {
        return cashFlowType;
    }
    public void setCashFlowType(String cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
