package com.bytter.biz.payment.api.dto;
import com.bytter.core.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * Title: DTO
 * Description: BT_BANK_ACC
 * @since 2017-06-02 17:50:16
 */
public class BtBankAccDTO implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6490814688726935044L;
    private String id;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 账户名称
     */
    private String accName;
    /**
     * 结算中心账号
     */
    private String counterAccId;
    /**
     * 结算中心父账号
     */
    private String superCounterAcc;
    /**
     * 父账号
     */
    private String prentAccId;
    private String prentBankAcc;
    private String prentAccName;
    private String prentAccType;
    private String prentAccTypeName;
    /**
     * 父账号标志
     */
    private String parentSign;
    /**
     * 单位ID
     */
    @Corp(inject = "corpName")
    private String corpId;
    private String netId;
    private String corpName;
    private String curName;
    @Currencys(
            {
                    @Currency(inject = "curName"),
                    @Currency(inject = "curEnglishCode", field = "englishCode")
            }
    )
    private String curId;
    private String curEnglishCode;
    /**
     * 账户类别
     */
    private String accType;
    private String accTypeName;
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
    private String bankPrefix;
    private String bankNamePrefix;
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
    @Region(inject = "provName")
    private String prov;
    @BtCompositeSearch(fieldName = "acc.prov")
    private String provName;
    /**
     * 所在市
     */
    @Region(inject = "cityName")
    private String city;
    @BtCompositeSearch(fieldName = "acc.city")
    private String cityName;
    /**
     * 接口代码ID
     */
    private String bifCode;
    /**
     * 接口代码编码
     */
    private String bifCodeStr;
    /**
     * 接口代码名称
     */
    private String bifName;
    /**
     * 电票专户
     */
    private String electricBill;
    private String electricBillName;
    /**
     * 定期期限
     */
    private String regulars;
    /**
     * 定期到期时间
     */
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
    private Date agrBeginDate;
    /**
     * 协定结束时间
     */
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
    private Date regDate;
    /**
     * 对私编号
     */
    private String privateNo;
    /**
     * 是否上线
     */
    private String isOnline;
    private String isOnlineStr;
    /**
     * 是否查询余额或明细
     */
    private String scanFlag;
    private String scanFlagStr;
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
     * 初始可用余额
     */
    private BigDecimal initAvailBal;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     */
    private String validSign;
    private String validSignStr;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 账号申请表id
     */
    private String appId;
    /**
     * 账户余额
     */
    private BigDecimal balance;
    /**
     * 内部账户余额
     */
    private BigDecimal fcBalance;
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
    private Date endDate;
    /**
     * 账号利率
     */
    private BigDecimal interestRate;
    /**
     * 对外属性
     */
    private String foreignDes;
    /**
     * 银行组织机构代码
     */
    private String bankOrgCode;
    /**
     * 是否境内银行 1-境内，0-境外
     */
    private String isDomesticBank;
    @JsonIgnore
    private List<String> curList;
    @JsonIgnore
    private List<String> curNameList;
    //	@JsonIgnore
    private List<BtBankAccDTO> childList;
    private List<Map<String, String>> counterAccList;
    private List<BtBankAccCurDTO> btBankAccCurDTOList;
    /**
     * 是否启用限额管理
     */
    private String isLimitQuota;
    /**
     * 日累计支付限额
     */
    private BigDecimal dayLimitQuota;
    /**
     * 月累计支付限额
     */
    private BigDecimal monthLimitQuota;
    /**
     * 单笔支付限额
     */
    private BigDecimal payLimitQuota;
    /**
     * 透支额度
     */
    private BigDecimal overLimitQuota;
    /**
     * id集合
     */
    private List<String> idList;
    /**
     * 是否线上办理
     */
    private String isOnlineHandle;
    /**
     * 是否离岸账户
     */
    private String isOffshoreAccount;
    /**
     * 租户id
     */
    private String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getIsLimitQuota() {
        return isLimitQuota;
    }
    public void setIsLimitQuota(String isLimitQuota) {
        this.isLimitQuota = isLimitQuota;
    }
    public BigDecimal getDayLimitQuota() {
        return dayLimitQuota;
    }
    public void setDayLimitQuota(BigDecimal dayLimitQuota) {
        this.dayLimitQuota = dayLimitQuota;
    }
    public BigDecimal getMonthLimitQuota() {
        return monthLimitQuota;
    }
    public void setMonthLimitQuota(BigDecimal monthLimitQuota) {
        this.monthLimitQuota = monthLimitQuota;
    }
    public BigDecimal getPayLimitQuota() {
        return payLimitQuota;
    }
    public void setPayLimitQuota(BigDecimal payLimitQuota) {
        this.payLimitQuota = payLimitQuota;
    }
    public BigDecimal getOverLimitQuota() {
        return overLimitQuota;
    }
    public void setOverLimitQuota(BigDecimal overLimitQuota) {
        this.overLimitQuota = overLimitQuota;
    }
    public List<BtBankAccCurDTO> getBtBankAccCurDTOList() {
        return btBankAccCurDTOList;
    }
    public void setBtBankAccCurDTOList(List<BtBankAccCurDTO> btBankAccCurDTOList) {
        this.btBankAccCurDTOList = btBankAccCurDTOList;
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
    public String getCounterAccId() {
        return counterAccId;
    }
    public void setCounterAccId(String counterAccId) {
        this.counterAccId = counterAccId;
    }
    public String getPrentAccId() {
        return prentAccId;
    }
    public void setPrentAccId(String prentAccId) {
        this.prentAccId = prentAccId;
    }
    public String getPrentBankAcc() {
        return prentBankAcc;
    }
    public void setPrentBankAcc(String prentBankAcc) {
        this.prentBankAcc = prentBankAcc;
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
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    public String getAccTypeName() {
        return accTypeName;
    }
    public void setAccTypeName(String accTypeName) {
        this.accTypeName = accTypeName;
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
    public String getBankPrefix() {
        return bankPrefix;
    }
    public void setBankPrefix(String bankPrefix) {
        this.bankPrefix = bankPrefix;
    }
    public String getBankNamePrefix() {
        return bankNamePrefix;
    }
    public void setBankNamePrefix(String bankNamePrefix) {
        this.bankNamePrefix = bankNamePrefix;
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
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getBifCodeStr() {
        return bifCodeStr;
    }
    public void setBifCodeStr(String bifCodeStr) {
        this.bifCodeStr = bifCodeStr;
    }
    public String getElectricBill() {
        return electricBill;
    }
    public void setElectricBill(String electricBill) {
        this.electricBill = electricBill;
    }
    public String getElectricBillName() {
        return electricBillName;
    }
    public void setElectricBillName(String electricBillName) {
        this.electricBillName = electricBillName;
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
    public BigDecimal getInitAvailBal() {
        return initAvailBal;
    }
    public void setInitAvailBal(BigDecimal initAvailBal) {
        this.initAvailBal = initAvailBal;
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
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public List<BtBankAccDTO> getChildList() {
        return childList;
    }
    public void setChildList(List<BtBankAccDTO> childList) {
        this.childList = childList;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getBifName() {
        return bifName;
    }
    public void setBifName(String bifName) {
        this.bifName = bifName;
    }
    public String getPrentAccName() {
        return prentAccName;
    }
    public void setPrentAccName(String prentAccName) {
        this.prentAccName = prentAccName;
    }
    public String getPrentAccType() {
        return prentAccType;
    }
    public void setPrentAccType(String prentAccType) {
        this.prentAccType = prentAccType;
    }
    public String getPrentAccTypeName() {
        return prentAccTypeName;
    }
    public void setPrentAccTypeName(String prentAccTypeName) {
        this.prentAccTypeName = prentAccTypeName;
    }
    public String getIsOnlineStr() {
        return isOnlineStr;
    }
    public void setIsOnlineStr(String isOnlineStr) {
        this.isOnlineStr = isOnlineStr;
    }
    public String getValidSignStr() {
        return validSignStr;
    }
    public void setValidSignStr(String validSignStr) {
        this.validSignStr = validSignStr;
    }
    public String getScanFlagStr() {
        return scanFlagStr;
    }
    public void setScanFlagStr(String scanFlagStr) {
        this.scanFlagStr = scanFlagStr;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public List<String> getCurList() {
        return curList;
    }
    public void setCurList(List<String> curList) {
        this.curList = curList;
    }
    public List<String> getCurNameList() {
        return curNameList;
    }
    public void setCurNameList(List<String> curNameList) {
        this.curNameList = curNameList;
    }
    public List<Map<String, String>> getCounterAccList() {
        return counterAccList;
    }
    public void setCounterAccList(List<Map<String, String>> counterAccList) {
        this.counterAccList = counterAccList;
    }
    public String getSuperCounterAcc() {
        return superCounterAcc;
    }
    public void setSuperCounterAcc(String superCounterAcc) {
        this.superCounterAcc = superCounterAcc;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getFcBalance() {
        return fcBalance;
    }
    public void setFcBalance(BigDecimal fcBalance) {
        this.fcBalance = fcBalance;
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
    public String getForeignDes() {
        return foreignDes;
    }
    public void setForeignDes(String foreignDes) {
        this.foreignDes = foreignDes;
    }
    public String getBankOrgCode() {
        return bankOrgCode;
    }
    public void setBankOrgCode(String bankOrgCode) {
        this.bankOrgCode = bankOrgCode;
    }
    public String getIsDomesticBank() {
        return isDomesticBank;
    }
    public void setIsDomesticBank(String isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
    }
    public String getCurEnglishCode() {
        return curEnglishCode;
    }
    public void setCurEnglishCode(String curEnglishCode) {
        this.curEnglishCode = curEnglishCode;
    }
    public List<String> getIdList() {
        return idList;
    }
    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
    public String getIsOnlineHandle() {
        return isOnlineHandle;
    }
    public void setIsOnlineHandle(String isOnlineHandle) {
        this.isOnlineHandle = isOnlineHandle;
    }
    public String getIsOffshoreAccount() {
        return isOffshoreAccount;
    }
    public void setIsOffshoreAccount(String isOffshoreAccount) {
        this.isOffshoreAccount = isOffshoreAccount;
    }
}
