package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Title: Entity
 * @Description: 外汇资金池交易表
 * @author wangzx
 * @date 2020-02-21 16:31:56
 * @version 1.0
 *
 */

public class FexCorpPoolTranVO implements Serializable, IBaseDataInject {

    private String id;
    /**资金池ID*/
    @BtCompositeSearch(fieldName = "p.pool_id")
    private String poolId;
    private String poolCode;
    private String poolName;
    /**资金池性质 1-跨境资金集中运营、2-境内外币资金池、3-境外外币资金池、4-跨境双向人民币资金池*/
    private String poolNature;
    /**资金池主体的备注*/
    private String rmk;
    /**限额折算币种*/
    @BtCompositeSearch(fieldName = "f.cur_id")
    private String curId;
    private String curName;
    private String englishCode;
    /**关联主账号ID*/
    private String bankaccId;
    /**账号*/
    @BtCompositeSearch(fieldName = "acc.bank_acc")
    private String bankAcc;
    /**账号名称*/
    private String accName;
    /**开户行名称*/
    private String bankName;

    /**交易标志 0-借入外债交易、1-对外放款交易*/
    @BtCompositeSearch(fieldName = "t.tran_sign")
    private String tranSign;
    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    @BtCompositeSearch(fieldName = "t.tran_type")
    private String tranType;
    /**产品ID*/
    private String proId;
    /**产品名称*/
    @BtCompositeSearch(fieldName = "fp.pro_name")
    private String proName;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**交易标志拼装*/
    private String tranSigns;
    /**资金池备注*/
    private String poolRmk;
    /**借入外债交易明细*/
    private List<FexCorpPoolTranVO> tranTypeJDtl;
    /**对外放款交易明细*/
    private List<FexCorpPoolTranVO> tranTypeWDtl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getPoolCode() {
        return poolCode;
    }

    public void setPoolCode(String poolCode) {
        this.poolCode = poolCode;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolNature() {
        return poolNature;
    }

    public void setPoolNature(String poolNature) {
        this.poolNature = poolNature;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
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

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public String getBankaccId() {
        return bankaccId;
    }

    public void setBankaccId(String bankaccId) {
        this.bankaccId = bankaccId;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTranSign() {
        return tranSign;
    }

    public void setTranSign(String tranSign) {
        this.tranSign = tranSign;
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

    public String getTranSigns() {
        return tranSigns;
    }

    public void setTranSigns(String tranSigns) {
        this.tranSigns = tranSigns;
    }

    public String getPoolRmk() {
        return poolRmk;
    }

    public void setPoolRmk(String poolRmk) {
        this.poolRmk = poolRmk;
    }

    public List<FexCorpPoolTranVO> getTranTypeJDtl() {
        return tranTypeJDtl;
    }

    public void setTranTypeJDtl(List<FexCorpPoolTranVO> tranTypeJDtl) {
        this.tranTypeJDtl = tranTypeJDtl;
    }

    public List<FexCorpPoolTranVO> getTranTypeWDtl() {
        return tranTypeWDtl;
    }

    public void setTranTypeWDtl(List<FexCorpPoolTranVO> tranTypeWDtl) {
        this.tranTypeWDtl = tranTypeWDtl;
    }
}
