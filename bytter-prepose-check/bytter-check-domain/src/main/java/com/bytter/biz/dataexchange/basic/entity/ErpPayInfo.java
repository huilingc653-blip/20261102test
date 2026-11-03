package com.bytter.biz.dataexchange.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author qijh
 */
@Entity
@Table(name = "ERP_PAYINFO", schema = "")
public class ErpPayInfo extends IdEntity implements java.io.Serializable {
    /**外系统ID*/
    private String serialNoErp;
    /**外系统凭证号*/
    private String voucherNoErp;
    /**数据来源*/
    private String dataSource;
    /**付款单位*/
    private String corpCode;
    /**付款方帐号*/
    private String payerAccNo;
    /**付款方开户行*/
    private String payerBankName;
    /**付款方户名*/
    private String payerAccName;
    /**收款单位*/
    private String oppCorpCode;
    /**收款账号*/
    private String oppBankAcc;
    /**收款人*/
    private String oppAccName;
    /**收方联行号*/
    private String payeeCode;
    /**收款开户行*/
    private String oppBankName;
    /**收款人省*/
    private String oppProv;
    /**收款人市*/
    private String oppCity;
    /**金额*/
    private BigDecimal amt;
    /**结算方式*/
    private String voucherType;
    /**摘要*/
    private String abs;
    /**加急标志 1：加急；0不加急*/
    private String urgencyFlag;
    /**是否跨行 0:同行付款；1:跨行付款*/
    private String difBank;
    /**是否同城 0:同城；1:异地*/
    private String areaSign;
    /**对私对公标志 0为对公，1对私*/
    private String isforindividual;
    /**期望支付日期*/
    private Date wishPayday;
    /**是否已付款*/
    private String isPayed;
    /**单据状态 0-未转移1-已转移2-	资金系统打回*/
    private String status;
    /**退单类型编码*/
    private String busType;
    /**反馈信息*/
    private String returnMsg;
    /**创建人名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**工资册标题*/
    private String wageTitle;
    /**工资发放日期*/
    private Date wageDay;
    /**币别代码*/
    private String curCode;
    /**打回时间*/
    private Date backDate;
    /**票据类型*/
    private Integer billType;
    /**票据流通介质*/
    private Integer billFlag;
    @Column(name ="SERIAL_NO_ERP")
    public String getSerialNoErp() {
        return serialNoErp;
    }
    public void setSerialNoErp(String serialNoErp) {
        this.serialNoErp = serialNoErp;
    }
    @Column(name ="VOUCHER_NO_ERP")
    public String getVoucherNoErp() {
        return voucherNoErp;
    }
    public void setVoucherNoErp(String voucherNoErp) {
        this.voucherNoErp = voucherNoErp;
    }
    @Column(name ="DATA_SOURCE")
    public String getDataSource() {
        return dataSource;
    }
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
    @Column(name ="CORP_CODE")
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    @Column(name ="PAYER_ACC_NO")
    public String getPayerAccNo() {
        return payerAccNo;
    }
    public void setPayerAccNo(String payerAccNo) {
        this.payerAccNo = payerAccNo;
    }
    @Column(name ="PAYER_BANK_NAME")
    public String getPayerBankName() {
        return payerBankName;
    }
    public void setPayerBankName(String payerBankName) {
        this.payerBankName = payerBankName;
    }
    @Column(name ="PAYER_ACC_NAME")
    public String getPayerAccName() {
        return payerAccName;
    }
    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }
    @Column(name ="OPP_CORP_CODE")
    public String getOppCorpCode() {
        return oppCorpCode;
    }
    public void setOppCorpCode(String oppCorpCode) {
        this.oppCorpCode = oppCorpCode;
    }
    @Column(name ="OPP_BANK_ACC")
    public String getOppBankAcc() {
        return oppBankAcc;
    }
    public void setOppBankAcc(String oppBankAcc) {
        this.oppBankAcc = oppBankAcc;
    }
    @Column(name ="OPP_ACC_NAME")
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    @Column(name ="PAYEE_CODE")
    public String getPayeeCode() {
        return payeeCode;
    }
    public void setPayeeCode(String payeeCode) {
        this.payeeCode = payeeCode;
    }
    @Column(name ="OPP_BANK_NAME")
    public String getOppBankName() {
        return oppBankName;
    }
    public void setOppBankName(String oppBankName) {
        this.oppBankName = oppBankName;
    }
    @Column(name ="OPP_PROV")
    public String getOppProv() {
        return oppProv;
    }
    public void setOppProv(String oppProv) {
        this.oppProv = oppProv;
    }
    @Column(name ="OPP_CITY")
    public String getOppCity() {
        return oppCity;
    }
    public void setOppCity(String oppCity) {
        this.oppCity = oppCity;
    }
    @Column(name ="AMT")
    public BigDecimal getAmt() {
        return amt;
    }
    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
    @Column(name ="VOUCHER_TYPE")
    public String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }
    @Column(name ="ABS")
    public String getAbs() {
        return abs;
    }
    public void setAbs(String abs) {
        this.abs = abs;
    }
    @Column(name ="URGENCY_FLAG")
    public String getUrgencyFlag() {
        return urgencyFlag;
    }
    public void setUrgencyFlag(String urgencyFlag) {
        this.urgencyFlag = urgencyFlag;
    }
    @Column(name ="DIF_BANK")
    public String getDifBank() {
        return difBank;
    }
    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }
    @Column(name ="AREA_SIGN")
    public String getAreaSign() {
        return areaSign;
    }
    public void setAreaSign(String areaSign) {
        this.areaSign = areaSign;
    }
    @Column(name ="ISFORINDIVIDUAL")
    public String getIsforindividual() {
        return isforindividual;
    }
    public void setIsforindividual(String isforindividual) {
        this.isforindividual = isforindividual;
    }
    @Column(name ="WISH_PAYDAY")
    public Date getWishPayday() {
        return wishPayday;
    }
    public void setWishPayday(Date wishPayday) {
        this.wishPayday = wishPayday;
    }
    @Column(name ="IS_PAYED")
    public String getIsPayed() {
        return isPayed;
    }
    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }
    @Column(name ="STATUS")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name ="BUS_TYPE")
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    @Column(name ="RETURN_MSG")
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="WAGE_TITLE")
    public String getWageTitle() {
        return wageTitle;
    }
    public void setWageTitle(String wageTitle) {
        this.wageTitle = wageTitle;
    }
    @Column(name ="WAGE_DAY")
    public Date getWageDay() {
        return wageDay;
    }
    public void setWageDay(Date wageDay) {
        this.wageDay = wageDay;
    }
    @Column(name ="CUR_CODE")
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    @Column(name ="BACK_DATE")
    public Date getBackDate() {
        return backDate;
    }
    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }
    @Column(name ="BILL_TYPE")
    public Integer getBillType() {
        return billType;
    }
    public void setBillType(Integer billType) {
        this.billType = billType;
    }
    @Column(name ="BILL_FLAG")
    public Integer getBillFlag() {
        return billFlag;
    }
    public void setBillFlag(Integer billFlag) {
        this.billFlag = billFlag;
    }
}
