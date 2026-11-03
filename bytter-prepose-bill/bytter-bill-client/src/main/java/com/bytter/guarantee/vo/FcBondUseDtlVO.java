package com.bytter.guarantee.vo;

import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luxy
 * @Title: FcBondUseDtlVO
 * @ProjectName T2(NEW)
 * @Description: TODO
 * @date 2019/10/15 16:58
 */
@Data
public class FcBondUseDtlVO  implements Serializable {
    private String id;
    /**
     * 单位id
     */
    @Corp(inject = "corpName")
    private String corpId;
    /**单位代码*/
    private String corpCode;
    /**
     * 单位名称
     */
    private String corpName;
    /** 银行Code*/
    private String bankCode;
    /** 存入银行 */
    private String bankName;
    /**币别*/
    @Currency(inject="curName")
    private String curId;
    /**币别名称*/
    private String curName;
    /** 存出总额 */
    private BigDecimal bondSumMoney;
    /** 项目单号 */
    private String itemNo;
    /** 项目开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /** 项目结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 项目期限 */
    private Integer itemUnit;
    private String bankAccId;
    /**存入账户*/
    private String bankAcc;
    /**账户名称*/
    private String accName;
    /** 占用类型 */
    private String itemType;
    /** 保证金账户存入金额 */
    private BigDecimal bondMoney;
    /** 占用金额 */
    private BigDecimal applyMoney;
    /**业务种类*/
    private String busSign;
    /**存出/存入账号*/
    private String outBankAcc;
    /**金额*/
    private BigDecimal money;
    /**状态*/
    private Integer status;
    /**调拨单号*/
    private String nisBillNo;
    /**指令id*/
    private String cbsBillId;
    /**资金调拨状态*/
    private Integer nisStatus;
    /**指令状态*/
    private String voucherStat;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

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
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
    public BigDecimal getBondSumMoney() {
        return bondSumMoney;
    }
    public void setBondSumMoney(BigDecimal bondSumMoney) {
        this.bondSumMoney = bondSumMoney;
    }
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public BigDecimal getApplyMoney() {
        return applyMoney;
    }
    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getItemUnit() {
        return itemUnit;
    }
    public void setItemUnit(Integer itemUnit) {
        this.itemUnit = itemUnit;
    }
    public String getBankAccId() {
        return bankAccId;
    }
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
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
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    public String getBusSign() {
        return busSign;
    }
    public void setBusSign(String busSign) {
        this.busSign = busSign;
    }
    public String getOutBankAcc() {
        return outBankAcc;
    }
    public void setOutBankAcc(String outBankAcc) {
        this.outBankAcc = outBankAcc;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getNisBillNo() {
        return nisBillNo;
    }
    public void setNisBillNo(String nisBillNo) {
        this.nisBillNo = nisBillNo;
    }
    public String getCbsBillId() {
        return cbsBillId;
    }
    public void setCbsBillId(String cbsBillId) {
        this.cbsBillId = cbsBillId;
    }
    public Integer getNisStatus() {
        return nisStatus;
    }
    public void setNisStatus(Integer nisStatus) {
        this.nisStatus = nisStatus;
    }
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
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
}
