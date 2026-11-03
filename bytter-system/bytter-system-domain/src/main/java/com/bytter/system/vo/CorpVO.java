package com.bytter.system.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.base.persistence.tree.interfaces.TreeNode;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * Created by zhoul on 2017/10/24.
 */
@ApiModel("CorpVO")
@ExcelTarget("CorpVO")
public class CorpVO extends ExcelModel implements Serializable, TreeNode<CorpVO> {
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("网点ID")
    private String netId;

    @ApiModelProperty("网点名称")
    private String netName;

    @ApiModelProperty("上级网点ID")
    private String parentId;

    @ApiModelProperty("上级网点/单位")
    @Excel(name = "上级网点/单位")
    private String parentName;

    /**
     * 所有上级id
     */
    @ApiModelProperty("所有上级ID")
    private String parentIds;

    /**
     * 组织名称
     */
    @ApiModelProperty("*组织名称")
    @Excel(name = "*组织名称")
    @NotNull(message = "sys.corp.message.name.not.null")
    @BtCompositeSearch(fieldName = "c.name")
    private String name;

    @ApiModelProperty("组织名称（英文）")
    @Excel(name = "组织名称（英文）")
    @BtCompositeSearch(fieldName = "c.name_en")
    private String nameEn;

    /**
     * 机构代码
     */
    @ApiModelProperty("*机构代码")
    @Excel(name = "*机构代码")
    @NotNull(message = "sys.corp.message.code.not.null")
    @BtCompositeSearch(fieldName = "c.code")
    private String code;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    @Excel(name = "排序")
    private Integer sort;

    /**
     * 是否有效，0停用，1启用（默认）
     */
    @ApiModelProperty("状态标识")
    @BtCompositeSearch(fieldName = "c.status")
    private Integer status;

    @ApiModelProperty("状态名称")
    @Excel(name = "状态")
    private String statusName;

    /**
     * 类型
     */
    @ApiModelProperty("组织类型标识")
    @BtCompositeSearch(fieldName = "c.type")
    private Integer type;

    @ApiModelProperty("*组织类型名称")
    @Excel(name = "*组织类型")
    @NotNull(message = "sys.corp.message.type.not.null")
    private String typeName;

    /**
     * 是否上市公司，0否，1是（默认）
     */
    @ApiModelProperty("是否上市公司")
    @Excel(name = "是否上市公司")
    private Integer listedCompany;

    /**
     * 所属省
     */
    @ApiModelProperty("所属省ID")
    private String province;

    @ApiModelProperty("所属省名称")
    @Excel(name = "所属省")
    private String provName;

    /**
     * 所属市
     */
    @ApiModelProperty("所属市ID")
    private String city;

    @ApiModelProperty("所属市名称")
    @Excel(name = "所属市")
    private String cityName;

    /**
     * 所用账号单位代码
     */
    @Excel(name = "所用账号单位代码")
    @ApiModelProperty("所用账号单位代码")
    private String userAccountCode;

    @ApiModelProperty("所用账号单位代码名称")
    private String userAccountCodeName;

    /**
     * 开户许可证
     */
    @ApiModelProperty("开户许可证")
    @Excel(name = "开户许可证")
    private String accountLicence;

    /**
     * 单位负责人
     */
    @ApiModelProperty("单位负责人")
    @Excel(name = "单位负责人")
    private String headPerson;

    /**
     * 单位地址
     */
    @ApiModelProperty("单位地址")
    @Excel(name = "单位地址")
    private String address;

    /**
     * 单位地址（英文）
     */
    @ApiModelProperty("单位地址（英文）")
    @Excel(name = "单位地址（英文）")
    private String addressEn;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private String createBy;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty("修改者")
    private String updateBy;

    /**
     * 集团统一授信主体
     */
    @ApiModelProperty("集团统一授信主体")
    private Integer ratGroup;

    @ApiModelProperty("子节点列表")
    private List<CorpVO> children;

    @ApiModelProperty("是否展开")
    private Boolean expanded = true;

    @ApiModelProperty("是否是叶子节点")
    private Boolean isLeaf;

    @ApiModelProperty("层级")
    private Integer level;

    /**
     * 是否启用限额管理
     */
    @ApiModelProperty("是否启用限额管理")
    private Integer isLimitQuota;

    /**
     * 日累计支付限额
     */
    @ApiModelProperty("日累计支付限额")
    private BigDecimal dayLimitQuota;

    /**
     * 月累计支付限额
     */
    @ApiModelProperty("月累计支付限额")
    private BigDecimal monthLimitQuota;

    /**
     * 本位币
     */
    @ApiModelProperty("本位币ID")
    private String curId;

    @ApiModelProperty("*本位币名称")
    @Excel(name = "*本位币")
    @NotNull(message = "sys.corp.message.curName.not.null")
    private String curName;

    /**
     * *单位属性
     */
    @ApiModelProperty("单位属性标识")
    private String unitAttribute;

    /**
     * 管理单元
     */
    @ApiModelProperty("管理单元标识")
    private String unitCm;

    /**
     * 单位属性名称
     */
    @ApiModelProperty("*单位属性名称")
    @Excel(name = "*单位属性")
    @NotNull(message = "sys.corp.message.unitAttribute.not.null")
    private String unitAttributeName;

    /**
     * 管理单元名称
     */
    @ApiModelProperty("管理单元名称")
    @Excel(name = "管理单元")
    private String unitCmName;

    /**
     * 国家/地区
     */
    @ApiModelProperty("国家/地区ID")
    private String countryId;

    @ApiModelProperty("国家/地区名称")
    @Excel(name = "国家/地区")
    private String countryName;

    /**
     * 单位证件类型
     */
    @ApiModelProperty("单位证件类型")
    private String certType;

    /**
     * 单位证件号码
     */
    @ApiModelProperty("单位证件号码")
    private String certNo;

    /**
     * 统一社会信用代码
     */
    @Excel(name = "统一社会信用代码")
    @ApiModelProperty("统一社会信用代码")
    private String socCode;

    @ApiModelProperty("运营区域ID")
    private String operationAreaId;

    @ApiModelProperty("运营区域名称")
    private String operationAreaName;

    @ApiModelProperty("股权维护ID")
    private String equityMaintenanceId;

    @ApiModelProperty("股权维护名称")
    private String equityMaintenanceName;

    @ApiModelProperty("单位门店维护ID")
    private String unitStoreMaintenanceId;

    @ApiModelProperty("单位门店维护名称")
    private String unitStoreMaintenanceName;

    @ApiModelProperty("省份ID")
    private String provinceId;

    @ApiModelProperty("省份代码")
    private String provinceCode;

    @ApiModelProperty("省份名称")
    private String provinceName;

    /** 报表是否显示 1是，0否*/
    @ApiModelProperty("报表是否显示 1是，0否")
    private String reportDisplay;

    public String getProvinceId() {
        return StringUtils.isBlank(provinceId) ? provinceId : provinceId.replaceAll(",null","").replaceAll("null,","").replaceAll("null","");
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = StringUtils.isBlank(provinceId) ? provinceId : provinceId.replaceAll(",null","").replaceAll("null,","").replaceAll("null","");
    }

    public String getProvinceCode() {
        return StringUtils.isBlank(provinceCode) ? provinceCode : provinceCode.replaceAll(",null","").replaceAll("null,","").replaceAll("null","");
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = StringUtils.isBlank(provinceCode) ? provinceCode : provinceCode.replaceAll(",null","").replaceAll("null,","").replaceAll("null","");
    }

    public String getProvinceName() {
        return StringUtils.isBlank(provinceName) ? provinceName : provinceName.replaceAll(",null","").replaceAll("null,","").replaceAll("null","");
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = StringUtils.isBlank(provinceName) ? provinceName : provinceName.replaceAll(",null","").replaceAll("null,","").replaceAll("null","");
    }

    public String getSocCode() {
        return socCode;
    }

    public void setSocCode(String socCode) {
        this.socCode = socCode;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
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
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Integer getRatGroup() {
        return ratGroup;
    }
    public void setRatGroup(Integer ratGroup) {
        this.ratGroup = ratGroup;
    }
    public Boolean getExpanded() {
        return expanded;
    }
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
    @Override
    public Integer getLevel() {
        return level;
    }
    @Override
    public void setLevel(Integer level) {
        this.level = level;
    }
    public Boolean getIsLeaf() {
        return isLeaf;
    }
    public void setIsLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getUserAccountCodeName() {
        return userAccountCodeName;
    }
    public void setUserAccountCodeName(String userAccountCodeName) {
        this.userAccountCodeName = userAccountCodeName;
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
    @Override
    public void setChildren(List<CorpVO> object) {
        this.children = object;
    }
    @Override
    public List getChildren() {
        return children;
    }
    @Override
    public String getBeanName() {
        return "corpService";
    }
    @Override
    public String getPath() {
        return getParentIds();
    }
    @Override
    public String getSplit() {
        return CommonConstant.COMMA_SPLIT;
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
    public String getUnitAttributeName() {
        return unitAttributeName;
    }
    public void setUnitAttributeName(String unitAttributeName) {
        this.unitAttributeName = unitAttributeName;
    }
    public String getUnitCmName() {
        return unitCmName;
    }
    public void setUnitCmName(String unitCmName) {
        this.unitCmName = unitCmName;
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

    public String getOperationAreaId() {
        return operationAreaId;
    }

    public void setOperationAreaId(String operationAreaId) {
        this.operationAreaId = operationAreaId;
    }

    public String getOperationAreaName() {
        return operationAreaName;
    }

    public void setOperationAreaName(String operationAreaName) {
        this.operationAreaName = operationAreaName;
    }

    public String getEquityMaintenanceId() {
        return equityMaintenanceId;
    }

    public void setEquityMaintenanceId(String equityMaintenanceId) {
        this.equityMaintenanceId = equityMaintenanceId;
    }

    public String getEquityMaintenanceName() {
        return equityMaintenanceName;
    }

    public void setEquityMaintenanceName(String equityMaintenanceName) {
        this.equityMaintenanceName = equityMaintenanceName;
    }

    public String getUnitStoreMaintenanceId() {
        return unitStoreMaintenanceId;
    }

    public void setUnitStoreMaintenanceId(String unitStoreMaintenanceId) {
        this.unitStoreMaintenanceId = unitStoreMaintenanceId;
    }

    public String getUnitStoreMaintenanceName() {
        return unitStoreMaintenanceName;
    }

    public void setUnitStoreMaintenanceName(String unitStoreMaintenanceName) {
        this.unitStoreMaintenanceName = unitStoreMaintenanceName;
    }

    public String getReportDisplay() {
        return reportDisplay;
    }

    public void setReportDisplay(String reportDisplay) {
        this.reportDisplay = reportDisplay;
    }
}
