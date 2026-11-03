package com.bytter.bill.api.bill.dto;
import com.bytter.core.base.persistence.tree.interfaces.TreeList;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * Created by zhoul on 2017/5/23.
 */
public class CorpDTO implements TreeList, Serializable {
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
                + "]";
    }
}
