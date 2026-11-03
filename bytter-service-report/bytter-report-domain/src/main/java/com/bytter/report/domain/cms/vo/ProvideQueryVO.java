package com.bytter.report.domain.cms.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <br>
 * <b>功能：</b> 借款短期、中长期查询VO <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/10/22 0022 <br>
 */
public class ProvideQueryVO implements Serializable {
    //业务ID
    private String infoId;
    @BtCompositeSearch(fieldName = "bt.id")
    private String bankType;//银行类型
    private String bankTypeName;//银行类型
    @BtCompositeSearch(fieldName = "info.corp_id")
    private String corpId;//单位Id
    private String corpName;//单位名称
    @BtCompositeSearch(fieldName = "info.cur_id")
    private String curId;//币别ID
    private String curName;//币别名称
    private String natureCur;//单位本位币
    private BigDecimal sendMoney;//借款金额
    private BigDecimal converMoney;//借款金额
    @BtCompositeSearch(fieldName = "send.money")
    private BigDecimal plMoneyGe;//大于借款金额
    @BtCompositeSearch(fieldName = "send.money")
    private BigDecimal plMoneyLe;//小于等于借款金额
    private BigDecimal plRate;//借款利率
    @BtCompositeSearch(fieldName = "send.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;//开始日期
    @BtCompositeSearch(fieldName = "send.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;//到期日期
    @BtCompositeSearch(fieldName = "send.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDateGe;//到期日期
    @BtCompositeSearch(fieldName = "crs.predice_repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date prediceRepayDate;//还款到期日期
    @BtCompositeSearch(fieldName = "crs.predice_repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date prediceRepayDateLe;//还款到期日期
    @BtCompositeSearch(fieldName = "crs.predice_repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date prediceRepayDateGe;//还款到期日期
    @BtCompositeSearch(fieldName = "send.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDateLe;//到期日期
    private String repayType;//还款方式
    private String repayTypeName;//还款方式
    @BtCompositeSearch(fieldName = "info.pl_class")
    private String plClass;//借款种类名称
    private String plClassName;//借款种类名称
    private String rmk;//备注
    private String hangGuarantee;//是否挂担保
    private String guaCon;//担保合同
    private String guaInfo;//担保合同
    private String interestWay;//计息方式
    private Integer expiryDate;//计息日
    private Integer accruedDailyDate;//预提日
    public String getInfoId() {
        return infoId;
    }
    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
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
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getPlMoneyGe() {
        return plMoneyGe;
    }
    public void setPlMoneyGe(BigDecimal plMoneyGe) {
        this.plMoneyGe = plMoneyGe;
    }
    public BigDecimal getPlMoneyLe() {
        return plMoneyLe;
    }
    public void setPlMoneyLe(BigDecimal plMoneyLe) {
        this.plMoneyLe = plMoneyLe;
    }
    public Date getPrediceRepayDate() {
        return prediceRepayDate;
    }
    public void setPrediceRepayDate(Date prediceRepayDate) {
        this.prediceRepayDate = prediceRepayDate;
    }
    public Date getPrediceRepayDateLe() {
        return prediceRepayDateLe;
    }
    public void setPrediceRepayDateLe(Date prediceRepayDateLe) {
        this.prediceRepayDateLe = prediceRepayDateLe;
    }
    public Date getPrediceRepayDateGe() {
        return prediceRepayDateGe;
    }
    public void setPrediceRepayDateGe(Date prediceRepayDateGe) {
        this.prediceRepayDateGe = prediceRepayDateGe;
    }
    public BigDecimal getPlRate() {
        return plRate;
    }
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public Date getRepayDateGe() {
        return repayDateGe;
    }
    public void setRepayDateGe(Date repayDateGe) {
        this.repayDateGe = repayDateGe;
    }
    public Date getRepayDateLe() {
        return repayDateLe;
    }
    public void setRepayDateLe(Date repayDateLe) {
        this.repayDateLe = repayDateLe;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayTypeName() {
        return repayTypeName;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getPlClass() {
        return plClass;
    }
    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }
    public String getPlClassName() {
        return plClassName;
    }
    public void setPlClassName(String plClassName) {
        this.plClassName = plClassName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getHangGuarantee() {
        return hangGuarantee;
    }
    public void setHangGuarantee(String hangGuarantee) {
        this.hangGuarantee = hangGuarantee;
    }
    public String getGuaCon() {
        return guaCon;
    }
    public void setGuaCon(String guaCon) {
        this.guaCon = guaCon;
    }
    public String getGuaInfo() {
        return guaInfo;
    }
    public void setGuaInfo(String guaInfo) {
        this.guaInfo = guaInfo;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public Integer getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }
    public Integer getAccruedDailyDate() {
        return accruedDailyDate;
    }
    public void setAccruedDailyDate(Integer accruedDailyDate) {
        this.accruedDailyDate = accruedDailyDate;
    }
    public String getNatureCur() {
        return natureCur;
    }
    public void setNatureCur(String natureCur) {
        this.natureCur = natureCur;
    }
    public BigDecimal getConverMoney() {
        return converMoney;
    }
    public void setConverMoney(BigDecimal converMoney) {
        this.converMoney = converMoney;
    }
}
