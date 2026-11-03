package com.bytter.system.api.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.core.annotation.Region;
import com.bytter.system.api.vo.BtBankAccVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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
     * 对方账号
     */
    private String oppAccNo;

    /**
     * 父账号标志
     */
    private String parentSign;

    /**
     * 单位ID
     */

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

    private String ratesFloatName;

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

    private String regDateStr;

    /**
     * 对私编号
     */
    private String privateNo;

    /**
     * 是否上线
     */
    private String isOnline;
    private String isOnlineName;

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


    private String statusStr;

    /**
     * 创建人登录名称
     */
    private String createBy;

    /**
     * 建行机构号
     */
    private String orgCode;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    /**
     * 更新人登录名称
     */
    private String updateBy;

    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
     * 是否相关联
     */
    private String isRelation;

    /**
     * 关联的内部账号
     */
    private String counterAcc;

    /**
     * 内部账户余额
     */
    private BigDecimal fcBalance;

    /**
     * 实体账号Id
     **/
    private String entityAccId;

    /**
     * 实体账号
     **/
    private String entityAcc;

    /**
     * 是否资金池账号
     **/
    private String isCapitalPool;

    /**
     * 流通方式 0-线下 1-线上
     **/
    private String circulationMode;


    private String circulationModeName;

    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
     * 网银功能
     */

    private String uKeyFunction;

    /**
     * 是否境内银行 1-境内，0-境外
     */
    private String isDomesticBank;


    private String isDomesticBankName;

    @JsonIgnore
    private List<String> curList;

    @JsonIgnore
    private List<String> curNameList;

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
     * 账户年检到期日
     */
    private Date checkExpireDate;

    /**
     * 预留印鉴图片地址
     */
    private String sealDirectory;

    private String[] sealDirectoryList;

    /**
     * 账户注销日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date closeDate;

    private String attachmentId;

    /**
     * 账户流水截止日期
     */
    private String endDtlDate;

    private String downAcc;

    private String downAccName;

    private String downAccPrent;

    private String downAccNamePrent;

    private String poolId;

    /**
     * 单据类型
     */
    private String receiptCode;

    private String receiptName;

    /**
     * 账户组
     */
    private String fcAccGroupCode;

    private String fcAccGroupName;

    /**
     * 开户行行号
     */
    private String bankCodeStr;

    /**
     * 关联上级内部账号
     */
    private String superCurrentAcc;

    /**
     * 银行账户余额
     */
    private BigDecimal bankAccBalance;

    private String flag;

    /**
     * 父账户信息
     */
    private com.bytter.system.api.vo.BtBankAccVO parentBankAcc;

    /**
     * 数据来源0:开户申请1：自主办理
     **/
    private String isFrom;

    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    private String poolSign;

    /**
     * 预留印鉴联系人
     */
    private String sealPerson;

    /**
     * 预留印鉴联系人电话
     */
    private String sealPhone;

    /**
     * 预留印鉴联系人邮箱
     */
    private String sealEmail;

    /**
     * 账户管理部门
     */
    private String mgtDepartment;

    /**
     * 网银管理员
     */

    private String uKeyAdmin;

    /**
     * 网银操作员
     */
    private String uKeyOperator;

    /**
     * 网银复核员1
     */
    private String uKeyReviewers1;

    /**
     * 网银复核员2
     */
    private String uKeyReviewers2;

    /**
     * 网银复核员3
     */
    private String uKeyReviewers3;

    /**
     * 网银编码-复选框，包含1-查询功能、2-转账功能、3-电子票据、4-网上对账、5-接收功能、6-签发功能、7-二维码收款、8-其他
     */
    private String uKeyFunctionCode;

    /**
     * 网银功能其他信息
     */

    private String uKeyFunctionOther;

    /**
     * 银行类型icon
     */
    private String icon;

    /**
     * 大区
     */
    private String parentArea;

    /**
     * 营运区
     */
    private String operationAreaID;

    private String operationAreaName;

    /**
     * 实时余额
     */
    private BigDecimal realTimeBal;

    /**
     * 附件标识  1-存在 0-不存在
     */
    private String attachmentSign;

    /**
     * 绑定账号
     **/
    private String bindingBankAcc;


    private String bindingBankAccStr;

    /**
     * 绑定账号单位名称
     **/
    private String bindingBankAccCorpName;

    /**
     * 绑定账号户名
     **/
    private String bindingBankAccAccName;

    /**
     * 绑定账号开户行
     **/
    private String bindingBankAccBankName;

    /**
     * 绑定账号银行类型名称
     **/
    private String bindingBankTypeName;

    /**
     * 是否参与资金需求计算
     **/
    private String isDemand;

    /**
     * 账号用途
     **/
    private String accUseCode;

    /**
     * 账号用途名称
     **/
    private String accUseName;

    /**
     * 绑定人名称
     **/
    private String bindingByName;

    /**
     * 绑定人手机
     **/
    private String bindingByMobile;

    /**
     * 登录密码
     **/
    private String loginPassword;

    /**
     * 支付密码
     **/
    private String payPassword;

    /**
     * 是否注销0否，1是
     **/
    private String isClose;

    /**
     * 可用余额
     */
    private BigDecimal availBal;

    /**
     * 营收账号留底金额
     */
    private BigDecimal leaveMoney;

    /**
     * swift code
     */
    private String swiftCode;

    private String countryId;

    /**
     * 账户名称（英文）
     */
    private String accNameEn;

    /**
     * 本地清算号
     */
    private String localClearNo;


    /**
     * 境外账号地址
     */
    private String bankAdd;


    /**
     * 国家地区简称
     */
    private String countryEnCode;

    private String tenantId;

    /**
     * RPA功能
     */
    private String rpaFunction;

    /**
     * 是否代管账户
     */
    private String isRpaEscrow;

    public String getIsRpaEscrow() {
        return isRpaEscrow;
    }

    public void setIsRpaEscrow(String isRpaEscrow) {
        this.isRpaEscrow = isRpaEscrow;
    }

    public String getRpaFunction() {
        return rpaFunction;
    }

    public void setRpaFunction(String rpaFunction) {
        this.rpaFunction = rpaFunction;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSuperCounterAcc() {
        return superCounterAcc;
    }

    public void setSuperCounterAcc(String superCounterAcc) {
        this.superCounterAcc = superCounterAcc;
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

    public String getOppAccNo() {
        return oppAccNo;
    }

    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
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

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
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

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurEnglishCode() {
        return curEnglishCode;
    }

    public void setCurEnglishCode(String curEnglishCode) {
        this.curEnglishCode = curEnglishCode;
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

    public String getBifName() {
        return bifName;
    }

    public void setBifName(String bifName) {
        this.bifName = bifName;
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

    public String getEntityAcc() {
        return entityAcc;
    }

    public void setEntityAcc(String entityAcc) {
        this.entityAcc = entityAcc;
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

    public String getRatesFloatName() {
        return ratesFloatName;
    }

    public void setRatesFloatName(String ratesFloatName) {
        this.ratesFloatName = ratesFloatName;
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

    public String getRegDateStr() {
        return regDateStr;
    }

    public void setRegDateStr(String regDateStr) {
        this.regDateStr = regDateStr;
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

    public String getIsOnlineName() {
        return isOnlineName;
    }

    public void setIsOnlineName(String isOnlineName) {
        this.isOnlineName = isOnlineName;
    }

    public String getIsOnlineStr() {
        return isOnlineStr;
    }

    public void setIsOnlineStr(String isOnlineStr) {
        this.isOnlineStr = isOnlineStr;
    }

    public String getScanFlag() {
        return scanFlag;
    }

    public void setScanFlag(String scanFlag) {
        this.scanFlag = scanFlag;
    }

    public String getScanFlagStr() {
        return scanFlagStr;
    }

    public void setScanFlagStr(String scanFlagStr) {
        this.scanFlagStr = scanFlagStr;
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

    public String getValidSignStr() {
        return validSignStr;
    }

    public void setValidSignStr(String validSignStr) {
        this.validSignStr = validSignStr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getIsRelation() {
        return isRelation;
    }

    public void setIsRelation(String isRelation) {
        this.isRelation = isRelation;
    }

    public String getCounterAcc() {
        return counterAcc;
    }

    public void setCounterAcc(String counterAcc) {
        this.counterAcc = counterAcc;
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

    public String getCirculationModeName() {
        return circulationModeName;
    }

    public void setCirculationModeName(String circulationModeName) {
        this.circulationModeName = circulationModeName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
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

    @JsonProperty("uKeyFunction")
    public String getuKeyFunction() {
        return uKeyFunction;
    }

    public void setuKeyFunction(String uKeyFunction) {
        this.uKeyFunction = uKeyFunction;
    }

    public String getIsDomesticBank() {
        return isDomesticBank;
    }

    public void setIsDomesticBank(String isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
    }

    public String getIsDomesticBankName() {
        return isDomesticBankName;
    }

    public void setIsDomesticBankName(String isDomesticBankName) {
        this.isDomesticBankName = isDomesticBankName;
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

    public List<BtBankAccDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<BtBankAccDTO> childList) {
        this.childList = childList;
    }

    public List<Map<String, String>> getCounterAccList() {
        return counterAccList;
    }

    public void setCounterAccList(List<Map<String, String>> counterAccList) {
        this.counterAccList = counterAccList;
    }

    public List<BtBankAccCurDTO> getBtBankAccCurDTOList() {
        return btBankAccCurDTOList;
    }

    public void setBtBankAccCurDTOList(List<BtBankAccCurDTO> btBankAccCurDTOList) {
        this.btBankAccCurDTOList = btBankAccCurDTOList;
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

    public Date getCheckExpireDate() {
        return checkExpireDate;
    }

    public void setCheckExpireDate(Date checkExpireDate) {
        this.checkExpireDate = checkExpireDate;
    }

    public String getSealDirectory() {
        return sealDirectory;
    }

    public void setSealDirectory(String sealDirectory) {
        this.sealDirectory = sealDirectory;
    }

    public String[] getSealDirectoryList() {
        return sealDirectoryList;
    }

    public void setSealDirectoryList(String[] sealDirectoryList) {
        this.sealDirectoryList = sealDirectoryList;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getEndDtlDate() {
        return endDtlDate;
    }

    public void setEndDtlDate(String endDtlDate) {
        this.endDtlDate = endDtlDate;
    }

    public String getDownAcc() {
        return downAcc;
    }

    public void setDownAcc(String downAcc) {
        this.downAcc = downAcc;
    }

    public String getDownAccName() {
        return downAccName;
    }

    public void setDownAccName(String downAccName) {
        this.downAccName = downAccName;
    }

    public String getDownAccPrent() {
        return downAccPrent;
    }

    public void setDownAccPrent(String downAccPrent) {
        this.downAccPrent = downAccPrent;
    }

    public String getDownAccNamePrent() {
        return downAccNamePrent;
    }

    public void setDownAccNamePrent(String downAccNamePrent) {
        this.downAccNamePrent = downAccNamePrent;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getFcAccGroupCode() {
        return fcAccGroupCode;
    }

    public void setFcAccGroupCode(String fcAccGroupCode) {
        this.fcAccGroupCode = fcAccGroupCode;
    }

    public String getFcAccGroupName() {
        return fcAccGroupName;
    }

    public void setFcAccGroupName(String fcAccGroupName) {
        this.fcAccGroupName = fcAccGroupName;
    }

    public String getBankCodeStr() {
        return bankCodeStr;
    }

    public void setBankCodeStr(String bankCodeStr) {
        this.bankCodeStr = bankCodeStr;
    }

    public String getSuperCurrentAcc() {
        return superCurrentAcc;
    }

    public void setSuperCurrentAcc(String superCurrentAcc) {
        this.superCurrentAcc = superCurrentAcc;
    }

    public BigDecimal getBankAccBalance() {
        return bankAccBalance;
    }

    public void setBankAccBalance(BigDecimal bankAccBalance) {
        this.bankAccBalance = bankAccBalance;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public BtBankAccVO getParentBankAcc() {
        return parentBankAcc;
    }

    public void setParentBankAcc(BtBankAccVO parentBankAcc) {
        this.parentBankAcc = parentBankAcc;
    }

    public String getIsFrom() {
        return isFrom;
    }

    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }

    public String getPoolSign() {
        return poolSign;
    }

    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }

    public String getSealPerson() {
        return sealPerson;
    }

    public void setSealPerson(String sealPerson) {
        this.sealPerson = sealPerson;
    }

    public String getSealPhone() {
        return sealPhone;
    }

    public void setSealPhone(String sealPhone) {
        this.sealPhone = sealPhone;
    }

    public String getSealEmail() {
        return sealEmail;
    }

    public void setSealEmail(String sealEmail) {
        this.sealEmail = sealEmail;
    }

    public String getMgtDepartment() {
        return mgtDepartment;
    }

    public void setMgtDepartment(String mgtDepartment) {
        this.mgtDepartment = mgtDepartment;
    }

    @JsonProperty("uKeyAdmin")
    public String getuKeyAdmin() {
        return uKeyAdmin;
    }

    public void setuKeyAdmin(String uKeyAdmin) {
        this.uKeyAdmin = uKeyAdmin;
    }

    @JsonProperty("uKeyOperator")
    public String getuKeyOperator() {
        return uKeyOperator;
    }

    public void setuKeyOperator(String uKeyOperator) {
        this.uKeyOperator = uKeyOperator;
    }

    @JsonProperty("uKeyReviewers1")
    public String getuKeyReviewers1() {
        return uKeyReviewers1;
    }

    public void setuKeyReviewers1(String uKeyReviewers1) {
        this.uKeyReviewers1 = uKeyReviewers1;
    }

    @JsonProperty("uKeyReviewers2")
    public String getuKeyReviewers2() {
        return uKeyReviewers2;
    }

    public void setuKeyReviewers2(String uKeyReviewers2) {
        this.uKeyReviewers2 = uKeyReviewers2;
    }
    @JsonProperty("uKeyReviewers3")
    public String getuKeyReviewers3() {
        return uKeyReviewers3;
    }

    public void setuKeyReviewers3(String uKeyReviewers3) {
        this.uKeyReviewers3 = uKeyReviewers3;
    }

    @JsonProperty("uKeyFunctionCode")
    public String getuKeyFunctionCode() {
        return uKeyFunctionCode;
    }

    public void setuKeyFunctionCode(String uKeyFunctionCode) {
        this.uKeyFunctionCode = uKeyFunctionCode;
    }

    @JsonProperty("uKeyFunctionOther")
    public String getuKeyFunctionOther() {
        return uKeyFunctionOther;
    }

    public void setuKeyFunctionOther(String uKeyFunctionOther) {
        this.uKeyFunctionOther = uKeyFunctionOther;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentArea() {
        return parentArea;
    }

    public void setParentArea(String parentArea) {
        this.parentArea = parentArea;
    }

    public String getOperationAreaID() {
        return operationAreaID;
    }

    public void setOperationAreaID(String operationAreaID) {
        this.operationAreaID = operationAreaID;
    }

    public String getOperationAreaName() {
        return operationAreaName;
    }

    public void setOperationAreaName(String operationAreaName) {
        this.operationAreaName = operationAreaName;
    }

    public BigDecimal getRealTimeBal() {
        return realTimeBal;
    }

    public void setRealTimeBal(BigDecimal realTimeBal) {
        this.realTimeBal = realTimeBal;
    }

    public String getAttachmentSign() {
        return attachmentSign;
    }

    public void setAttachmentSign(String attachmentSign) {
        this.attachmentSign = attachmentSign;
    }

    public String getBindingBankAcc() {
        return bindingBankAcc;
    }

    public void setBindingBankAcc(String bindingBankAcc) {
        this.bindingBankAcc = bindingBankAcc;
    }

    public String getBindingBankAccStr() {
        return bindingBankAccStr;
    }

    public void setBindingBankAccStr(String bindingBankAccStr) {
        this.bindingBankAccStr = bindingBankAccStr;
    }

    public String getBindingBankAccCorpName() {
        return bindingBankAccCorpName;
    }

    public void setBindingBankAccCorpName(String bindingBankAccCorpName) {
        this.bindingBankAccCorpName = bindingBankAccCorpName;
    }

    public String getBindingBankAccAccName() {
        return bindingBankAccAccName;
    }

    public void setBindingBankAccAccName(String bindingBankAccAccName) {
        this.bindingBankAccAccName = bindingBankAccAccName;
    }

    public String getBindingBankAccBankName() {
        return bindingBankAccBankName;
    }

    public void setBindingBankAccBankName(String bindingBankAccBankName) {
        this.bindingBankAccBankName = bindingBankAccBankName;
    }

    public String getBindingBankTypeName() {
        return bindingBankTypeName;
    }

    public void setBindingBankTypeName(String bindingBankTypeName) {
        this.bindingBankTypeName = bindingBankTypeName;
    }

    public String getIsDemand() {
        return isDemand;
    }

    public void setIsDemand(String isDemand) {
        this.isDemand = isDemand;
    }

    public String getAccUseCode() {
        return accUseCode;
    }

    public void setAccUseCode(String accUseCode) {
        this.accUseCode = accUseCode;
    }

    public String getAccUseName() {
        return accUseName;
    }

    public void setAccUseName(String accUseName) {
        this.accUseName = accUseName;
    }

    public String getBindingByName() {
        return bindingByName;
    }

    public void setBindingByName(String bindingByName) {
        this.bindingByName = bindingByName;
    }

    public String getBindingByMobile() {
        return bindingByMobile;
    }

    public void setBindingByMobile(String bindingByMobile) {
        this.bindingByMobile = bindingByMobile;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getIsClose() {
        return isClose;
    }

    public void setIsClose(String isClose) {
        this.isClose = isClose;
    }

    public BigDecimal getAvailBal() {
        return availBal;
    }

    public void setAvailBal(BigDecimal availBal) {
        this.availBal = availBal;
    }

    public BigDecimal getLeaveMoney() {
        return leaveMoney;
    }

    public void setLeaveMoney(BigDecimal leaveMoney) {
        this.leaveMoney = leaveMoney;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getAccNameEn() {
        return accNameEn;
    }

    public void setAccNameEn(String accNameEn) {
        this.accNameEn = accNameEn;
    }

    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    public String getBankAdd() {
        return bankAdd;
    }

    public void setBankAdd(String bankAdd) {
        this.bankAdd = bankAdd;
    }

    public String getCountryEnCode() {
        return countryEnCode;
    }

    public void setCountryEnCode(String countryEnCode) {
        this.countryEnCode = countryEnCode;
    }
}
