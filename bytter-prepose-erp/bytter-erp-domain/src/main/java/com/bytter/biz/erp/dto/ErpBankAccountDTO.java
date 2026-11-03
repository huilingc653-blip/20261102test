package com.bytter.biz.erp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Erp账户同步对象
 *
 * @author zhoul
 */
public class ErpBankAccountDTO {

    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 账户名称
     */
    private String accName;
    /**
     * 父账号标志 1为父账号，0为子账号
     */
    private String parentSign = "0";
    /**
     * 单位ID
     */
    private String corpId;
    private String corpName;
    /**
     * 账户类别
     */
    private String accType;
    /**
     * 账户属性
     */
    private String attributeId;
    private String attributeName;
    /**
     * 账户性质
     */
    private String natureId;
    private String natureName;
    /**
     * 银行类别
     */
    private String bankTypeId;
    private String bankTypeName;
    /**
     * 开户行行号
     */
    private String bankCode;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 所在省
     */
    private String prov;
    /**
     * 所在市
     */
    private String city;
    /**
     * 接口代码
     */
    private String bifCode;
    /**
     * 电票专户
     */
    private String electricBill;
    /**
     * 定期期限
     */
    private String regulars;
    /**
     * 定期到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regularDate;
    /**
     * 通知类型
     */
    private String noticeType;
    /**
     * 协定金额
     */
    private BigDecimal agrMoney;
    /**
     * 协定利率
     */
    private BigDecimal agrInterest;
    /**
     * 协定合同
     */
    private String agrContract;
    /**
     * 协定开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date agrBeginDate;
    /**
     * 协定结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date agrEndDate;
    /**
     * 利率
     */
    private BigDecimal rates;
    /**
     * 利率是否浮动
     */
    private String ratesFloat;
    /**
     * 浮动百分比
     */
    private BigDecimal ratesFloatPer;
    /**
     * 本册笔号
     */
    private String volumeNum;
    /**
     * 存则册号
     */
    private String bankbookNum;
    /**
     * 计息周期
     */
    private Integer interestCycle;
    /**
     * 计息日
     */
    private String interestDate;
    /**
     * 开户日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regDate;
    /**
     * 对私编号
     */
    private String privateNo;
    /**
     * 是否上线
     */
    private String isOnline;
    /**
     * 是否查询余额或明细
     */
    private String scanFlag;
    /**
     * 余额初始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date initBalanceDate;
    /**
     * 初始余额
     */
    private BigDecimal initBalance;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 建行机构号
     */
    private String orgCode;

    /**
     * 实体账号
     **/
    private String entityAccId;

    /**
     * 是否资金池账号
     **/
    private String isCapitalPool;
    /**
     * 流通方式 0-线下 1-线上
     **/
    private String circulationMode;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 账号币别中间表
     */
    private List<ErpBtBankAccCurDTO> bankAccCurList;

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

    public String getParentSign() {
        return parentSign;
    }

    public void setParentSign(String parentSign) {
        this.parentSign = parentSign;
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

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getNatureId() {
        return natureId;
    }

    public void setNatureId(String natureId) {
        this.natureId = natureId;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
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

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public String getElectricBill() {
        return electricBill;
    }

    public void setElectricBill(String electricBill) {
        this.electricBill = electricBill;
    }

    public String getRegulars() {
        return regulars;
    }

    public void setRegulars(String regulars) {
        this.regulars = regulars;
    }

    public Date getRegularDate() {
        return regularDate;
    }

    public void setRegularDate(Date regularDate) {
        this.regularDate = regularDate;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public BigDecimal getAgrMoney() {
        return agrMoney;
    }

    public void setAgrMoney(BigDecimal agrMoney) {
        this.agrMoney = agrMoney;
    }

    public BigDecimal getAgrInterest() {
        return agrInterest;
    }

    public void setAgrInterest(BigDecimal agrInterest) {
        this.agrInterest = agrInterest;
    }

    public String getAgrContract() {
        return agrContract;
    }

    public void setAgrContract(String agrContract) {
        this.agrContract = agrContract;
    }

    public Date getAgrBeginDate() {
        return agrBeginDate;
    }

    public void setAgrBeginDate(Date agrBeginDate) {
        this.agrBeginDate = agrBeginDate;
    }

    public Date getAgrEndDate() {
        return agrEndDate;
    }

    public void setAgrEndDate(Date agrEndDate) {
        this.agrEndDate = agrEndDate;
    }

    public BigDecimal getRates() {
        return rates;
    }

    public void setRates(BigDecimal rates) {
        this.rates = rates;
    }

    public String getRatesFloat() {
        return ratesFloat;
    }

    public void setRatesFloat(String ratesFloat) {
        this.ratesFloat = ratesFloat;
    }

    public BigDecimal getRatesFloatPer() {
        return ratesFloatPer;
    }

    public void setRatesFloatPer(BigDecimal ratesFloatPer) {
        this.ratesFloatPer = ratesFloatPer;
    }

    public String getVolumeNum() {
        return volumeNum;
    }

    public void setVolumeNum(String volumeNum) {
        this.volumeNum = volumeNum;
    }

    public String getBankbookNum() {
        return bankbookNum;
    }

    public void setBankbookNum(String bankbookNum) {
        this.bankbookNum = bankbookNum;
    }

    public Integer getInterestCycle() {
        return interestCycle;
    }

    public void setInterestCycle(Integer interestCycle) {
        this.interestCycle = interestCycle;
    }

    public String getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(String interestDate) {
        this.interestDate = interestDate;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getPrivateNo() {
        return privateNo;
    }

    public void setPrivateNo(String privateNo) {
        this.privateNo = privateNo;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getScanFlag() {
        return scanFlag;
    }

    public void setScanFlag(String scanFlag) {
        this.scanFlag = scanFlag;
    }

    public Date getInitBalanceDate() {
        return initBalanceDate;
    }

    public void setInitBalanceDate(Date initBalanceDate) {
        this.initBalanceDate = initBalanceDate;
    }

    public BigDecimal getInitBalance() {
        return initBalance;
    }

    public void setInitBalance(BigDecimal initBalance) {
        this.initBalance = initBalance;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getEntityAccId() {
        return entityAccId;
    }

    public void setEntityAccId(String entityAccId) {
        this.entityAccId = entityAccId;
    }

    public String getIsCapitalPool() {
        return isCapitalPool;
    }

    public void setIsCapitalPool(String isCapitalPool) {
        this.isCapitalPool = isCapitalPool;
    }

    public String getCirculationMode() {
        return circulationMode;
    }

    public void setCirculationMode(String circulationMode) {
        this.circulationMode = circulationMode;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ErpBtBankAccCurDTO> getBankAccCurList() {
        return bankAccCurList;
    }

    public void setBankAccCurList(List<ErpBtBankAccCurDTO> bankAccCurList) {
        this.bankAccCurList = bankAccCurList;
    }
}
