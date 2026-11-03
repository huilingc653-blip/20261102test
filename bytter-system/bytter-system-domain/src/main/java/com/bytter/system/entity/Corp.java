package com.bytter.system.entity;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.core.base.entity.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by zhoul on 2017/4/11.
 */
@Entity
@Table(name = "SYS_CORP")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Corp extends IdEntity implements Serializable {
    private String netId;
    /**
     * 上级id
     */
    private String parentId;
    /**
     * 所有上级id
     */
    private String parentIds;
    /**
     * 组织名称
     */
    private String name;
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
    /**
     * 所用账号单位代码
     */
    private String userAccountCode;
    /**
     * 机构代码
     */
    private String code;
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改者
     */
    private String updateBy;
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
     * 本位币
     */
    private String curId;
    /**
     * 集团统一授信主体
     */
    private Integer ratGroup;
    private List<User> users = new ArrayList<>(0);
    /**
     * 管理属性
     */
    private String unitAttribute;
    /**
     * 管理单元
     */
    private String unitCm;
    /**
     * 国家/地区
     */
    private String countryId;
    /**
     * 单位证件类型
     */
    private String certType;
    /**
     * 单位证件号码
     */
    private String certNo;

    /**
     * 统一社会信用代码
     */
    private String socCode;

    private String operationAreaId;

    private String equityMaintenanceId;
    private String unitStoreMaintenanceId;

    private String provinceId;
    /** 报表是否显示 1是，0否*/
    private String reportDisplay;

    private String tenantId;

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "PROVINCE_ID")
    public String getProvinceId() {
        return StringUtils.isBlank(provinceId) ? provinceId : provinceId.replaceAll(",null","").replaceAll("null,","").replaceAll("null","");
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = StringUtils.isBlank(provinceId) ? provinceId : provinceId.replaceAll(",null","").replaceAll("null,","").replaceAll("null","");
    }

    public Corp() {
    }
    public Corp(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Column(name = "SOC_CODE")
    public String getSocCode() {
        return socCode;
    }

    public void setSocCode(String socCode) {
        this.socCode = socCode;
    }
    @Column(name = "IS_LIMIT_QUOTA")
    public Integer getIsLimitQuota() {
        return isLimitQuota;
    }
    public void setIsLimitQuota(Integer isLimitQuota) {
        this.isLimitQuota = isLimitQuota;
    }
    @Column(name = "DAY_LIMIT_QUOTA")
    public BigDecimal getDayLimitQuota() {
        return dayLimitQuota;
    }
    public void setDayLimitQuota(BigDecimal dayLimitQuota) {
        this.dayLimitQuota = dayLimitQuota;
    }
    @Column(name = "MONTH_LIMIT_QUOTA")
    public BigDecimal getMonthLimitQuota() {
        return monthLimitQuota;
    }
    public void setMonthLimitQuota(BigDecimal monthLimitQuota) {
        this.monthLimitQuota = monthLimitQuota;
    }
    @Column(name = "PARENT_IDS", length = 2000)
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    @Column(name = "NAME", nullable = false, length = 200)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "SORT", columnDefinition = "INT default 500")
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @Column(name = "STATUS", nullable = false)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "TYPE", length = 1)
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    @Column(name = "LISTED_COMPANY", nullable = false, length = 1)
    public Integer getListedCompany() {
        return listedCompany;
    }
    public void setListedCompany(Integer listedCompany) {
        this.listedCompany = listedCompany;
    }
    @Column(name = "PROVINCE", length = 32)
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    @Column(name = "CITY", length = 32)
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    @Column(name = "USE_ACCOUNT_CODE", length = 1000)
    public String getUserAccountCode() {
        return userAccountCode;
    }
    public void setUserAccountCode(String userAccountCode) {
        this.userAccountCode = userAccountCode;
    }
    @Column(name = "CODE", length = 32)
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "ACCOUNT_LICENCE", length = 30)
    public String getAccountLicence() {
        return accountLicence;
    }
    public void setAccountLicence(String accountLicence) {
        this.accountLicence = accountLicence;
    }
    @Column(name = "HEAD_PERSON", length = 30)
    public String getHeadPerson() {
        return headPerson;
    }
    public void setHeadPerson(String headPerson) {
        this.headPerson = headPerson;
    }
    @Column(name = "ADDRESS", length = 200)
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    @Column(name = "CREATE_TIME", updatable = false)
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "CREATE_BY", length = 32, updatable = false)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Column(name = "UPDATE_BY", length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @OneToMany(mappedBy = "corp", fetch = FetchType.LAZY)
    public List<User> getUsers() {
        return users;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
    @Column(name = "PARENT_ID", length = 32, nullable = true)
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Column(name = "NET_ID", length = 32)
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    @Column(name = "RAT_GROUP", length = 1)
    public Integer getRatGroup() {
        return ratGroup;
    }
    public void setRatGroup(Integer ratGroup) {
        this.ratGroup = ratGroup;
    }
    @Column(name = "CUR_ID", length = 32)
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Transient
    public boolean isNet() {
        return CorpConstant.CORP_TYPE_CODE_NET.equals(getType());
    }
    @Column(name = "NAME_EN")
    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    @Column(name = "ADDRESS_EN")
    public String getAddressEn() {
        return addressEn;
    }
    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }
    @Column(name = "UNIT_ATTRIBUTE")
    public String getUnitAttribute() {
        return unitAttribute;
    }
    public void setUnitAttribute(String unitAttribute) {
        this.unitAttribute = unitAttribute;
    }
    @Column(name = "UNIT_CM")
    public String getUnitCm() {
        return unitCm;
    }
    public void setUnitCm(String unitCm) {
        this.unitCm = unitCm;
    }
    @Column(name = "COUNTRY_ID")
    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
    @Column(name = "CERT_TYPE")
    public String getCertType() {
        return certType;
    }
    public void setCertType(String certType) {
        this.certType = certType;
    }
    @Column(name = "CERT_NO")
    public String getCertNo() {
        return certNo;
    }
    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    @Column(name = "OPERATIONAREA_ID")
    public String getOperationAreaId() {
        return operationAreaId;
    }

    public void setOperationAreaId(String operationAreaId) {
        this.operationAreaId = operationAreaId;
    }

    @Column(name = "EQUITYMAINTENANCE_ID")
    public String getEquityMaintenanceId() {
        return equityMaintenanceId;
    }

    public void setEquityMaintenanceId(String equityMaintenanceId) {
        this.equityMaintenanceId = equityMaintenanceId;
    }

    @Column(name = "unitStoreMaintenance_ID")
    public String getUnitStoreMaintenanceId() {
        return unitStoreMaintenanceId;
    }

    public void setUnitStoreMaintenanceId(String unitStoreMaintenanceId) {
        this.unitStoreMaintenanceId = unitStoreMaintenanceId;
    }

    @Column(name = "REPORT_DISPLAY")
    public String getReportDisplay() {
        return reportDisplay;
    }

    public void setReportDisplay(String reportDisplay) {
        this.reportDisplay = reportDisplay;
    }

    @Override
    public String toString() {
        return "Corp [netId=" + netId + ", parentId=" + parentId + ", parentIds=" + parentIds + ", name=" + name
                + ", nameEn=" + nameEn + ", sort=" + sort + ", status=" + status + ", type=" + type + ", listedCompany="
                + listedCompany + ", province=" + province + ", city=" + city + ", userAccountCode=" + userAccountCode
                + ", code=" + code + ", accountLicence=" + accountLicence + ", headPerson=" + headPerson + ", address="
                + address + ", addressEn=" + addressEn + ", createTime=" + createTime + ", createBy=" + createBy
                + ", updateTime=" + updateTime + ", updateBy=" + updateBy + ", isLimitQuota=" + isLimitQuota
                + ", dayLimitQuota=" + dayLimitQuota + ", monthLimitQuota=" + monthLimitQuota + ", curId=" + curId
                + ", ratGroup=" + ratGroup + ", users=" + users + ", unitAttribute=" + unitAttribute + ", unitCm="
                + unitCm + "]";
    }
}
