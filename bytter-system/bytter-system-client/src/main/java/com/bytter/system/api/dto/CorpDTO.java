package com.bytter.system.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.base.persistence.tree.interfaces.TreeList;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * Created by zhoul on 2017/5/23.
 */
public class CorpDTO implements TreeList, Serializable {

    @BtCompositeSearch(fieldName="sc.id")
    private String id;

    private String parentId;
    /**
     * 所有上级id
     */
    private String parentIds;
    /**
     * 网点ID
     */
    private String netId;
    /**
     * 网点代码
     */
    private String netCode;
    /**
     * 组织名称
     */
    private String name;
    /**
     * 统一社会信用代码
     */
    private String socCode;
    /**
     * 组织名称英文
     */
    private String nameEn;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否有效，0停用，1启用（默认）
     */
    private Integer status;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 是否上市公司，0否，1是（默认）
     */
    private Integer listedCompany;
    /**
     * 所属省
     */
    private String province;
    /**
     * 所属市
     */
    private String city;
    private String cityName;


    /**地点*/
    private String countryAddr;
    private String countryAddrName;


    /**
     * 所用帐号单位代码
     */
    private String userAccountCode;
    /**
     * 机构代码
     */
    private String code;
    /**
     * 父级单位代码
     */
    private String parentCode;
    /**
     * 开户许可证
     */
    private String accountLicence;
    /**
     * 单位负责人
     */
    private String headPerson;
    /**
     * 单位地址
     */
    private String address;
    /**
     * 单位地址英文
     */
    private String addressEn;
    /**
     * 网点名称
     */
    private String netName;
    /**
     * 集团统一授信主体
     */
    private Integer ratGroup;
    /**
     * 本位币
     */
    private String curId;
    private String curName;
    /**
     * 是否启用限额管理
     */
    private Integer isLimitQuota;
    /**
     * 日累计支付限额
     */
    private BigDecimal dayLimitQuota;
    /**
     * 月累计支付限额
     */
    private BigDecimal monthLimitQuota;
    /**
     * 管理属性
     */
    private String unitAttribute;
    /**
     * 管理单元
     */
    private String unitCm;
    /***
     * 管理单元Code
     */
    private String unitCmCode;
    /***
     * 管理单元Name
     */
    private String unitCmName;
    /**
     * 国家/地区
     */
    private String countryId;
    private String countryName;
    /**
     * 单位证件类型
     */
    private String certType;
    /**
     * 单位证件号码
     */
    private String certNo;
    /**
     * 下属所有成员单位
     */
    private List<CorpDTO> childrenCorp;
    /**
     * 下级单位数量
     */
    private Integer childCount;

    private String operationAreaId;

    /**
     * 营运区名称
     */
    private String operationAreaName;

    private String equityMaintenanceId;

    private String unitStoreMaintenanceId;

    /**
     * 最大利率
     */
    private BigDecimal maxRate;

    /**
     * 最小利率
     */
    private BigDecimal minRate;

    /**
     * 平均利率
     */
    private BigDecimal avgRate;

    /**
     * 当天余额
     */
    private BigDecimal currentBalance;
    private BigDecimal availBal;// 可用余额
    private BigDecimal frzBal;// 冻结余额


    /**
     * 前一天余额
     */
    private BigDecimal beforeDayBalance ;

    /**
     * 理财金额
     */
    private BigDecimal finaMoney ;

    /**
     * 备注
     */
    private String remark ;

    /**
     * 资金上划金额和
     */
    private BigDecimal sumUpward;
    /**
     * 资金系统上划金额
     */
    private BigDecimal sumUpwardSystem;
    /**
     * 银行上划金额
     */
    private BigDecimal sumUpwardBank;
    /**
     * 自主上划金额
     */
    private BigDecimal sumUpwardActive;
    /**
     * 资金上划同比
     */
    private BigDecimal tongBiUpward;
    /**
     * 资金上划同比金额和
     */
    private BigDecimal tongBiSumUpward;
    /**
     * 资金上划环比
     */
    private BigDecimal huanBiUpward;
    /**
     * 资金上划环比金额和
     */
    private BigDecimal huanBiSumUpward;
    /**
     * 资金下拨金额和
     */
    private BigDecimal sumAllocation;
    /**
     * 资金系统下拨金额
     */
    private BigDecimal sumAllocationSystem;
    /**
     * 银行下拨金额
     */
    private BigDecimal sumAllocationBank;
    /**
     * 自主下拨金额
     */
    private BigDecimal sumAllocationActive;
    /**
     * 资金下拨同比
     */
    private BigDecimal tongBiAllocation;
    /**
     * 资金下拨同比金额和
     */
    private BigDecimal tongBiSumAllocation;
    /**
     * 资金下拨环比
     */
    private BigDecimal huanBiAllocation;
    /**
     * 资金下拨环比金额和
     */
    private BigDecimal huanBiSumAllocation;
    /**
     * 留存金额
     */
    private BigDecimal retainedAmount;
    /**
     * 同比留存金额
     */
    private BigDecimal tongBiRetainedAmount;
    /**
     * 环比留存金额
     */
    private BigDecimal huanBiRetainedAmount;
    /**
     * 留存同比
     */
    private BigDecimal tongBiRetained;
    /**
     * 留存环比
     */
    private BigDecimal huanBiRetained;
    /**
     * 留存率
     */
    private BigDecimal retentionRate;

    /**
     * 上级区域名称
     */
    private String supCorpName;
    /**
     * 上级区域名称
     */
    private String supCorpCode;
    /**
     * 账户收入
     */
    private BigDecimal accountIncome;
    /**
     * 账户支出
     */
    private BigDecimal accountPayOut;
    /**
     * 是否超额下拨
     */
    private String isExcess;
    private String corpCode;
    private String equityMaintenanceName;

    private String provinceId;

    private String provinceName;

    private String queryDate;
    /**账号组*/
    private String accountGroup;

    /** 报表是否显示 1是，0否*/
    private String reportDisplay;

    /**
     * 邮政编码
     */
    private String postCode;

    /**
     * 省/州
     */
    private String state;

    /**
     * 地址1（通常街道地址）
     */
    private String address1;

    /**
     * 地址2（通常楼栋地址）
     */
    private String address2;
    /**
     * 单位id
     */
    private String corpId;


    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public BigDecimal getTongBiRetainedAmount() {
        return tongBiRetainedAmount;
    }

    public void setTongBiRetainedAmount(BigDecimal tongBiRetainedAmount) {
        this.tongBiRetainedAmount = tongBiRetainedAmount;
    }

    public BigDecimal getHuanBiRetainedAmount() {
        return huanBiRetainedAmount;
    }

    public void setHuanBiRetainedAmount(BigDecimal huanBiRetainedAmount) {
        this.huanBiRetainedAmount = huanBiRetainedAmount;
    }

    public String getSupCorpCode() {
        return supCorpCode;
    }

    public void setSupCorpCode(String supCorpCode) {
        this.supCorpCode = supCorpCode;
    }

    public String getSupCorpName() {
        return supCorpName;
    }

    public void setSupCorpName(String supCorpName) {
        this.supCorpName = supCorpName;
    }

    public BigDecimal getSumUpward() {
        return sumUpward;
    }

    public void setSumUpward(BigDecimal sumUpward) {
        this.sumUpward = sumUpward;
    }

    public BigDecimal getTongBiUpward() {
        return tongBiUpward;
    }

    public void setTongBiUpward(BigDecimal tongBiUpward) {
        this.tongBiUpward = tongBiUpward;
    }

    public BigDecimal getHuanBiUpward() {
        return huanBiUpward;
    }

    public void setHuanBiUpward(BigDecimal huanBiUpward) {
        this.huanBiUpward = huanBiUpward;
    }

    public BigDecimal getSumAllocation() {
        return sumAllocation;
    }

    public void setSumAllocation(BigDecimal sumAllocation) {
        this.sumAllocation = sumAllocation;
    }

    public BigDecimal getTongBiAllocation() {
        return tongBiAllocation;
    }

    public void setTongBiAllocation(BigDecimal tongBiAllocation) {
        this.tongBiAllocation = tongBiAllocation;
    }

    public BigDecimal getHuanBiAllocation() {
        return huanBiAllocation;
    }

    public void setHuanBiAllocation(BigDecimal huanBiAllocation) {
        this.huanBiAllocation = huanBiAllocation;
    }

    public BigDecimal getRetainedAmount() {
        return retainedAmount;
    }

    public void setRetainedAmount(BigDecimal retainedAmount) {
        this.retainedAmount = retainedAmount;
    }

    public BigDecimal getTongBiRetained() {
        return tongBiRetained;
    }

    public void setTongBiRetained(BigDecimal tongBiRetained) {
        this.tongBiRetained = tongBiRetained;
    }

    public BigDecimal getHuanBiRetained() {
        return huanBiRetained;
    }

    public void setHuanBiRetained(BigDecimal huanBiRetained) {
        this.huanBiRetained = huanBiRetained;
    }

    public BigDecimal getRetentionRate() {
        return retentionRate;
    }

    public void setRetentionRate(BigDecimal retentionRate) {
        this.retentionRate = retentionRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperationAreaName() {
        return operationAreaName;
    }

    public void setOperationAreaName(String operationAreaName) {
        this.operationAreaName = operationAreaName;
    }

    public BigDecimal getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(BigDecimal maxRate) {
        this.maxRate = maxRate;
    }

    public BigDecimal getMinRate() {
        return minRate;
    }

    public void setMinRate(BigDecimal minRate) {
        this.minRate = minRate;
    }

    public BigDecimal getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(BigDecimal avgRate) {
        this.avgRate = avgRate;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getBeforeDayBalance() {
        return beforeDayBalance;
    }

    public void setBeforeDayBalance(BigDecimal beforeDayBalance) {
        this.beforeDayBalance = beforeDayBalance;
    }

    public BigDecimal getFinaMoney() {
        return finaMoney;
    }

    public void setFinaMoney(BigDecimal finaMoney) {
        this.finaMoney = finaMoney;
    }

    public Integer getChildCount() {
        return childCount;
    }
    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }
    public Integer getIsLimitQuota() {
        return isLimitQuota;
    }
    public void setIsLimitQuota(Integer isLimitQuota) {
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
    @Override
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getListedCompany() {
        return listedCompany;
    }
    public void setListedCompany(Integer listedCompany) {
        this.listedCompany = listedCompany;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getUserAccountCode() {
        return userAccountCode;
    }
    public void setUserAccountCode(String userAccountCode) {
        this.userAccountCode = userAccountCode;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getAccountLicence() {
        return accountLicence;
    }
    public void setAccountLicence(String accountLicence) {
        this.accountLicence = accountLicence;
    }
    public String getHeadPerson() {
        return headPerson;
    }
    public void setHeadPerson(String headPerson) {
        this.headPerson = headPerson;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public Integer getRatGroup() {
        return ratGroup;
    }
    public void setRatGroup(Integer ratGroup) {
        this.ratGroup = ratGroup;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public String getParentCode() {
        return parentCode;
    }
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
    /**
     * 用于处理List集合
     *
     * @param obj
     * @return
     * @author zuoy 2019-02-12
     */
    @Override
    public boolean equals(Object obj) {
        CorpDTO c = (CorpDTO) obj;
        return id.equals(c.getId()) && code.equals(c.getCode());
    }
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    public String getAddressEn() {
        return addressEn;
    }
    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }
    public String getUnitAttribute() {
        return unitAttribute;
    }
    public void setUnitAttribute(String unitAttribute) {
        this.unitAttribute = unitAttribute;
    }
    public String getUnitCm() {
        return unitCm;
    }
    public void setUnitCm(String unitCm) {
        this.unitCm = unitCm;
    }
    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public String getCertType() {
        return certType;
    }
    public void setCertType(String certType) {
        this.certType = certType;
    }
    public String getCertNo() {
        return certNo;
    }
    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }
    public List<CorpDTO> getChildrenCorp() {
        return childrenCorp;
    }
    public void setChildrenCorp(List<CorpDTO> childrenCorp) {
        this.childrenCorp = childrenCorp;
    }
    public String getSocCode() {
        return socCode;
    }
    public void setSocCode(String socCode) {
        this.socCode = socCode;
    }

    public String getOperationAreaId() {
        return operationAreaId;
    }

    public void setOperationAreaId(String operationAreaId) {
        this.operationAreaId = operationAreaId;
    }

    public String getEquityMaintenanceId() {
        return equityMaintenanceId;
    }

    public void setEquityMaintenanceId(String equityMaintenanceId) {
        this.equityMaintenanceId = equityMaintenanceId;
    }

    public String getUnitStoreMaintenanceId() {
        return unitStoreMaintenanceId;
    }

    public void setUnitStoreMaintenanceId(String unitStoreMaintenanceId) {
        this.unitStoreMaintenanceId = unitStoreMaintenanceId;
    }

    public BigDecimal getSumUpwardSystem() {
        return sumUpwardSystem;
    }

    public void setSumUpwardSystem(BigDecimal sumUpwardSystem) {
        this.sumUpwardSystem = sumUpwardSystem;
    }

    public BigDecimal getSumUpwardBank() {
        return sumUpwardBank;
    }

    public void setSumUpwardBank(BigDecimal sumUpwardBank) {
        this.sumUpwardBank = sumUpwardBank;
    }

    public BigDecimal getSumUpwardActive() {
        return sumUpwardActive;
    }

    public void setSumUpwardActive(BigDecimal sumUpwardActive) {
        this.sumUpwardActive = sumUpwardActive;
    }

    public BigDecimal getTongBiSumUpward() {
        return tongBiSumUpward;
    }

    public void setTongBiSumUpward(BigDecimal tongBiSumUpward) {
        this.tongBiSumUpward = tongBiSumUpward;
    }

    public BigDecimal getHuanBiSumUpward() {
        return huanBiSumUpward;
    }

    public void setHuanBiSumUpward(BigDecimal huanBiSumUpward) {
        this.huanBiSumUpward = huanBiSumUpward;
    }

    public BigDecimal getSumAllocationSystem() {
        return sumAllocationSystem;
    }

    public void setSumAllocationSystem(BigDecimal sumAllocationSystem) {
        this.sumAllocationSystem = sumAllocationSystem;
    }

    public BigDecimal getSumAllocationBank() {
        return sumAllocationBank;
    }

    public void setSumAllocationBank(BigDecimal sumAllocationBank) {
        this.sumAllocationBank = sumAllocationBank;
    }

    public BigDecimal getSumAllocationActive() {
        return sumAllocationActive;
    }

    public void setSumAllocationActive(BigDecimal sumAllocationActive) {
        this.sumAllocationActive = sumAllocationActive;
    }

    public BigDecimal getTongBiSumAllocation() {
        return tongBiSumAllocation;
    }

    public void setTongBiSumAllocation(BigDecimal tongBiSumAllocation) {
        this.tongBiSumAllocation = tongBiSumAllocation;
    }

    public BigDecimal getHuanBiSumAllocation() {
        return huanBiSumAllocation;
    }

    public void setHuanBiSumAllocation(BigDecimal huanBiSumAllocation) {
        this.huanBiSumAllocation = huanBiSumAllocation;
    }

    public BigDecimal getAccountIncome() {
        return accountIncome;
    }

    public void setAccountIncome(BigDecimal accountIncome) {
        this.accountIncome = accountIncome;
    }

    public BigDecimal getAccountPayOut() {
        return accountPayOut;
    }

    public void setAccountPayOut(BigDecimal accountPayOut) {
        this.accountPayOut = accountPayOut;
    }

    public String getIsExcess() {
        return isExcess;
    }

    public void setIsExcess(String isExcess) {
        this.isExcess = isExcess;
    }

    public BigDecimal getAvailBal() {
        return availBal;
    }

    public void setAvailBal(BigDecimal availBal) {
        this.availBal = availBal;
    }

    public BigDecimal getFrzBal() {
        return frzBal;
    }

    public void setFrzBal(BigDecimal frzBal) {
        this.frzBal = frzBal;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getEquityMaintenanceName() {
        return equityMaintenanceName;
    }

    public void setEquityMaintenanceName(String equityMaintenanceName) {
        this.equityMaintenanceName = equityMaintenanceName;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public String getUnitCmCode() {
        return unitCmCode;
    }

    public void setUnitCmCode(String unitCmCode) {
        this.unitCmCode = unitCmCode;
    }

    public String getUnitCmName() {
        return unitCmName;
    }

    public void setUnitCmName(String unitCmName) {
        this.unitCmName = unitCmName;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public String getReportDisplay() {
        return reportDisplay;
    }

    public void setReportDisplay(String reportDisplay) {
        this.reportDisplay = reportDisplay;
    }


    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryAddr() {
        return countryAddr;
    }

    public void setCountryAddr(String countryAddr) {
        this.countryAddr = countryAddr;
    }

    public String getCountryAddrName() {
        return countryAddrName;
    }

    public void setCountryAddrName(String countryAddrName) {
        this.countryAddrName = countryAddrName;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Override
    public String toString() {
        return "CorpDTO [id=" + id + ", parentId=" + parentId + ", parentIds=" + parentIds + ", netId=" + netId
                + ", netCode=" + netCode + ", name=" + name + ", nameEn=" + nameEn + ", sort=" + sort + ", status="
                + status + ", type=" + type + ", listedCompany=" + listedCompany + ", province=" + province + ", city="
                + city + ", userAccountCode=" + userAccountCode + ", code=" + code + ", parentCode=" + parentCode
                + ", accountLicence=" + accountLicence + ", headPerson=" + headPerson + ", address=" + address
                + ", addressEn=" + addressEn + ", netName=" + netName + ", ratGroup=" + ratGroup + ", curId=" + curId
                + ", curName=" + curName + ", isLimitQuota=" + isLimitQuota + ", dayLimitQuota=" + dayLimitQuota
                + ", monthLimitQuota=" + monthLimitQuota + ", unitAttribute=" + unitAttribute + ", unitCm=" + unitCm
                + ", operationAreaId=" + operationAreaId + ", equityMaintenanceId=" + equityMaintenanceId + "]";
    }
}
