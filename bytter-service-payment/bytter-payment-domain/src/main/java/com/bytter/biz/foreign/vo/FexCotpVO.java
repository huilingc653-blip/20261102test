package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FexCotpVO extends ExcelModel implements IBusinessKey, IBaseDataInject, Serializable {
    private static final long serialVersionUID = 3650731181127660208L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 单位id
     */
    private String corpId;
    private String corpName;

    /**
     * 交易对手/组名称
     */
    @Excel(name = "*交易对手名称")
    @BtCompositeSearch(fieldName = "cotp.cotp_name")
    private String cotpName;

    /**
     * 短名称
     */
    @Excel(name = "短名称")
    private String shortName;

    /**
     * 正式名称
     */
    @Excel(name = "*正式名称")
    private String officialName;

    /**
     * 所属组
     */
    @BtCompositeSearch(fieldName = "cotp.parent_id")
    private String parentId;
    @BtCompositeSearch(fieldName = "cotp2.cotp_name")
    @Excel(name = "所属组")
    private String parentName;

    /**
     * 层级 0-交易对手组 1-交易对手
     */
    @BtCompositeSearch(fieldName = "cotp.cotp_level")
    private String cotpLevel;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 交易对手类别
     */
    private String cotpType;
    @Excel(name = "*交易对手类别")
    private String cotpTypeName;

    /**
     * 邮政地址
     */
    @Excel(name = "邮政地址")
    private String postAddr;

    /**
     * 街道地址(地址1)
     */
    @Excel(name = "街道地址")
    private String streetAddr;

    /**
     *楼栋编号(地址2)
     */
    private String buildingNumber;

    /**
     * 省/州
     */
    private String state;

    /**
     * 电话/电报/传真号码
     */
    @Excel(name = "电话/电报/传真号码")
    private String tel;

    /**
     * 国家/地区
     */
    private String countryId;
    @Excel(name = "*国家/地区")
    private String countryName;
    private String countryNameEn;

    /**
     * 地点
     */
    private String address;
    @Excel(name = "*地点")
    private String addressName;
    private String addressNameEn;

    /**
     * BIC代码
     */
    @Excel(name = "SWIFT CODE")
    private String bicCode;

    /**
     * 行号
     */
    @Excel(name = "行号")
    private String bankCode;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 创建人登录id
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 更新人登录id
     */
    private String updateBy;

    /**
     * 更新日期
     */
    private Date updateDate;

    /**
     * 组成员ids
     */
    private String ids;

    /**
     *交易对手名称（英文）
     */
    @Excel(name = "交易对手名称（英文）")
    private String cotpNameEn;

    /**
     *地址（英文）
     */
    private String addressEn;
    /**
     * 开户银行代码类型
     */
    private String clsFlag;
    /**
     * 本地清算号
     */
    private String localClearNo;

    /**
     * 所属省
     */

    private String prov;
    private String provName;
    /**
     * 所属市
     */

    private String city;
    private String cityName;

    /**
     * 支付产品
     */
    private String productId;

    /**
     * FPS ID
     */
    private String fpsId;

    /**
     * 邮箱
     */
    private String eMail;

    /**
     * 国家代码
     */
    private String isdCode;

    /**
     * 电话号码
     */
    private String phoneCode;
    /**
     * 信用卡号
     */
    private String aiIn;

    /**
     * 香港身份证号
     */
    private String hkId;

    /**
     * 国家简码
     */
    private String enCode;

    /**
     * 交易对手数据源id
     */
    private String oppId;

    private List<FexCotpCustInfoVO> fexCotpCustInfoVOS;

    public String getCotpName() {
        return cotpName;
    }

    public void setCotpName(String cotpName) {
        this.cotpName = cotpName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCotpLevel() {
        return cotpLevel;
    }

    public void setCotpLevel(String cotpLevel) {
        this.cotpLevel = cotpLevel;
    }

    public String getCotpType() {
        return cotpType;
    }

    public void setCotpType(String cotpType) {
        this.cotpType = cotpType;
    }

    public String getPostAddr() {
        return postAddr;
    }

    public void setPostAddr(String postAddr) {
        this.postAddr = postAddr;
    }

    public String getStreetAddr() {
        return streetAddr;
    }

    public void setStreetAddr(String streetAddr) {
        this.streetAddr = streetAddr;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getCountryNameEn() {
        return countryNameEn;
    }

    public void setCountryNameEn(String countryNameEn) {
        this.countryNameEn = countryNameEn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressNameEn() {
        return addressNameEn;
    }

    public void setAddressNameEn(String addressNameEn) {
        this.addressNameEn = addressNameEn;
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }


    public List<FexCotpCustInfoVO> getFexCotpCustInfoVOS() {
        return fexCotpCustInfoVOS;
    }

    public void setFexCotpCustInfoVOS(List<FexCotpCustInfoVO> fexCotpCustInfoVOS) {
        this.fexCotpCustInfoVOS = fexCotpCustInfoVOS;
    }

    public String getCotpTypeName() {
        return cotpTypeName;
    }

    public void setCotpTypeName(String cotpTypeName) {
        this.cotpTypeName = cotpTypeName;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getCotpNameEn() {
        return cotpNameEn;
    }

    public void setCotpNameEn(String cotpNameEn) {
        this.cotpNameEn = cotpNameEn;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public String getClsFlag() {
        return clsFlag;
    }

    public void setClsFlag(String clsFlag) {
        this.clsFlag = clsFlag;
    }

    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getFpsId() {
        return fpsId;
    }

    public void setFpsId(String fpsId) {
        this.fpsId = fpsId;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getAiIn() {
        return aiIn;
    }

    public void setAiIn(String aiIn) {
        this.aiIn = aiIn;
    }

    public String getHkId() {
        return hkId;
    }

    public void setHkId(String hkId) {
        this.hkId = hkId;
    }

    public String getEnCode() {
        return enCode;
    }

    public void setEnCode(String enCode) {
        this.enCode = enCode;
    }

    public String getOppId() {
        return oppId;
    }

    public void setOppId(String oppId) {
        this.oppId = oppId;
    }

    @Override
    public String getBusinessKey() {
        return id;
    }

    @Override
    public String getBeanName() {
        return "fexCotpService";
    }

}
