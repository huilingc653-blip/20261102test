package com.bytter.system.vo;
import com.bytter.core.annotation.*;
import com.bytter.core.utils.DateUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@ExcelTarget("BtBankAccVO")
public class CorpAndBtBankAccVO extends ExcelModel implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6490814688726935044L;
    @BtCompositeSearch(fieldName = "t.id")
    private String id;
    /**
     * 银行账号
     */
    @Excel(name = "*银行账号")
    @BtCompositeSearch(fieldName = "acc.bank_acc")
    private String bankAcc;
    /**
     * 账户名称
     */
    @Excel(name = "*账户名称")
    @BtCompositeSearch(fieldName = "acc.acc_name")
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
    @BtCompositeSearch(fieldName = "acc.prent_acc_id")
    private String prentAccId;
    private String prentAccName;
    private String prentBankAcc;
    @Dict(dictGroup = "btBankAcc.accType", inject = "prentAccTypeName")
    private String prentAccType;
    private String prentAccTypeName;
    /**
     * 父账号标志
     */
    @BtCompositeSearch(fieldName = "acc.parent_sign")
    private String parentSign;
    @Excel(name = "*币别")
    @BtCompositeSearch(fieldName = "curName")
    private String curName;
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "acccur.cur_id")
    private String curId;
    /**
     * 账户类别
     */
    @Dict(dictGroup = "btBankAcc.accType", inject = "accTypeName")
    @BtCompositeSearch(fieldName = "acc.acc_type")
    private String accType;
    @Excel(name = "*账户类别")
    @BtCompositeSearch(fieldName = "acc.acc_type")
    private String accTypeName;
    /**
     * 账户属性
     */
    @BtCompositeSearch(fieldName = "acc.attribute_id")
    private String attributeId;
    @Excel(name = "*账户属性")
    @BtCompositeSearch(fieldName = "acc.attribute_id")
    private String attributeName;
    /**
     * 账户性质
     */
    private String natureId;
    @Excel(name = "*账户性质")
    @BtCompositeSearch(fieldName = "acc.nature_id")
    private String natureName;
    /**
     * 银行类别
     */
    @BtCompositeSearch(fieldName = "acc.bank_type_id")
    private String bankTypeId;
    @Excel(name = "*银行类别")
    @BtCompositeSearch(fieldName = "acc.bank_type_id")
    private String bankTypeName;
    /**
     * 开户行行号
     */
    @Excel(name = "*开户行")
    private String bankCode;
    /**
     * 开户行名称
     */
    @BtCompositeSearch(fieldName = "acc.bank_name")
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
     * 接口代码
     */
    @BtCompositeSearch(fieldName = "acc.bif_code")
    private String bifCode;
    /**
     * 接口代码名称
     */
    @Excel(name = "接口代码")
    private String bifName;
    /**
     * 电票专户
     */
    @Dict(dictGroup = "sf_yn", inject = "electricBillName")
    @Excel(name = "电票专户")
    private String electricBill;
    @BtCompositeSearch(fieldName = "acc.electric_bill")
    private String electricBillName;
    /**
     * 定期期限
     */
    @Excel(name = "定期期限")
    private String regulars;
    /**
     * 定期到期时间
     */
    private Date regularDate;
    /**
     * 通知类型
     */
    @Excel(name = "通知类型")
    private String noticeType;
    /**
     * 协定金额
     */
    @Excel(name = "协定金额")
    private BigDecimal agrMoney;
    /**
     * 协定利率
     */
    @Excel(name = "协定利率")
    private BigDecimal agrInterest;
    /**
     * 协定合同
     */
    @Excel(name = "协定合同")
    private String agrContract;
    /**
     * 协定开始时间
     */
    @Excel(name = "协定开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date agrBeginDate;
    /**
     * 协定结束时间
     */
    @Excel(name = "协定结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date agrEndDate;
    /**
     * 利率
     */
    @Excel(name = "*利率")
    @BtCompositeSearch(fieldName = "acc.rates")
    private BigDecimal rates;
    /**
     * 利率是否浮动
     */
    @Dict(dictGroup = "sf_yn", inject = "ratesFloatName")
    private String ratesFloat;
    /**
     * 利率是否浮动
     */
    @Excel(name = "*利率是否浮动")
    private String ratesFloatName;
    /**
     * 浮动百分比
     */
    @Excel(name = "浮动百分比")
    private BigDecimal ratesFloatPer;
    private String ratesFloatPerStr;
    /**
     * 本册笔号
     */
    @Excel(name = "本册笔号")
    private String volumeNum;
    /**
     * 存则册号
     */
    @Excel(name = "存则册号")
    private String bankbookNum;
    /**
     * 计息周期
     */
    private Integer interestCycle;
    @Excel(name = "*计息周期")
    private String interestCycleStr;
    /**
     * 计息日
     */
    @Excel(name = "计息日")
    private String interestDate;
    /**
     * 开户日期
     */
    @Excel(name = "*开户日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "acc.reg_date")
    private Date regDate;
    private String regDateStr;
    /**
     * 对私编号
     */
    @Excel(name = "对私编号")
    private String privateNo;
    /**
     * 是否上线
     */
    @Dict(dictGroup = "sf_yn", inject = "isOnlineStr")
    @BtCompositeSearch(fieldName = "acc.is_online")
    @Excel(name = "*是否上线")
    private String isOnline;
    @BtCompositeSearch(fieldName = "acc.is_online")
    private String isOnlineStr;
    /**
     * 是否查询余额或明细
     */
    @Dict(dictGroup = "sf_yn", inject = "scanFlagStr")
    @Excel(name = "*是否查询余额")
    private String scanFlag;
    private String scanFlagStr;
    /**
     * 余额初始日期
     */
    @Excel(name = "初始余额日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date initBalanceDate;
    private String initBalanceDateStr;
    /**
     * 初始余额
     */
    @Excel(name = "初始余额")
    private BigDecimal initBalance;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String rmk;
    /**
     * 有效标志
     */
    @Dict(dictGroup = "bankAcc.validSign", inject = "validSignStr")
    @BtCompositeSearch(fieldName = "acc.valid_sign")
    @Excel(name = "*有效标志")
    private String validSign;
    @BtCompositeSearch(fieldName = "acc.valid_sign")
    private String validSignStr;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 建行机构号
     */
    @Excel(name = "建行机构号")
    private String orgCode;
    /**
     * 创建人登录名称
     */
    @BtCompositeSearch(fieldName = "acc.create_by")
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
     * 是否相关联
     */
    private String isRelation;
    /**
     * 关联的内部账号
     */
    @BtCompositeSearch(fieldName = "counterAcc")
    private String counterAcc;
    /**
     * 关联上级内部账号
     */
    @BtCompositeSearch(fieldName = "superCurrentAcc")
    private String superCurrentAcc;
    private List<CorpAndBtBankAccVO> childList;
    /**
     * 银行账户余额
     */
    private BigDecimal bankAccBalance;
    /**
     * 内部账户余额
     */
    private BigDecimal fcBalance;
    private String flag;
    /**
     * 父账户信息
     */
    private CorpAndBtBankAccVO parentBankAcc;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 实体账号
     **/
    private String entityAccId;
    /**
     * 数据来源0:开户申请1：自主办理
     **/
    private String isFrom;
    /**
     * 是否资金池账号
     **/
    private String isCapitalPool;
    /**
     * 单位ID
     */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "acc.corp_id")
    private String corpId;
    @Excel(name = "*开户单位")
    @BtCompositeSearch(fieldName = "acc.corp_id")
    private String corpName;
    @Corp(inject = "netName")
    private String netId;
    private String netName;
    @Corp(inject = "parentName")
    private String parentId;
    private String parentName;
    private String parentIds;    //所有上级id
    private String code;    //机构代码
    private String name;    //组织名称
    private Integer type;    //类型

    private String socCode;
    private String corpNameEn;
    private String englishCode;

    public String getSocCode() {
        return socCode;
    }

    public void setSocCode(String socCode) {
        this.socCode = socCode;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getIsFrom() {
        return isFrom;
    }
    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
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
        if (null != ratesFloatPer) {
            this.ratesFloatPerStr = ratesFloatPer.toString();
        } else {
            ratesFloatPer = BigDecimal.ZERO;
            this.ratesFloatPerStr = ratesFloatPer.toString();
        }
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
    public String getInterestCycleStr() {
        return interestCycleStr;
    }
    public void setInterestCycleStr(String interestCycleStr) {
        this.interestCycleStr = interestCycleStr;
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
        this.regDateStr = DateUtils.formatDate();
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
        this.initBalanceDateStr = DateUtils.formatDate();
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
    public List<CorpAndBtBankAccVO> getChildList() {
        return childList;
    }
    public void setChildList(List<CorpAndBtBankAccVO> childList) {
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
    public String getPrentBankAcc() {
        return prentBankAcc;
    }
    public void setPrentBankAcc(String prentBankAcc) {
        this.prentBankAcc = prentBankAcc;
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
    public String getRatesFloatName() {
        return ratesFloatName;
    }
    public void setRatesFloatName(String ratesFloatName) {
        this.ratesFloatName = ratesFloatName;
    }
    public String getInitBalanceDateStr() {
        return initBalanceDateStr;
    }
    public void setInitBalanceDateStr(String initBalanceDateStr) {
        this.initBalanceDateStr = initBalanceDateStr;
    }
    public String getRegDateStr() {
        return regDateStr;
    }
    public void setRegDateStr(String regDateStr) {
        this.regDateStr = regDateStr;
    }
    public String getRatesFloatPerStr() {
        return ratesFloatPerStr;
    }
    public void setRatesFloatPerStr(String ratesFloatPerStr) {
        this.ratesFloatPerStr = ratesFloatPerStr;
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
    public String getSuperCounterAcc() {
        return superCounterAcc;
    }
    public void setSuperCounterAcc(String superCounterAcc) {
        this.superCounterAcc = superCounterAcc;
    }
    public CorpAndBtBankAccVO getParentBankAcc() {
        return parentBankAcc;
    }
    public void setParentBankAcc(CorpAndBtBankAccVO parentBankAcc) {
        this.parentBankAcc = parentBankAcc;
    }
    public BigDecimal getFcBalance() {
        return fcBalance;
    }
    public void setFcBalance(BigDecimal fcBalance) {
        this.fcBalance = fcBalance;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Override
    public String getBeanName() {
        return "btBankAccHandleService";
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

    public String getCorpNameEn() {
        return corpNameEn;
    }

    public void setCorpNameEn(String corpNameEn) {
        this.corpNameEn = corpNameEn;
    }

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }
}
