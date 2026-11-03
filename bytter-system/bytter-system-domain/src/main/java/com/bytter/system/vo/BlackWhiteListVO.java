package com.bytter.system.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.bytter.system.api.constant.SysBlackWhiteConstant;
import com.bytter.system.api.dto.BlackWhiteDTO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * BlackWhiteVo
 *
 * @author caojy
 */
@ExcelTarget("BlackWhiteListVO")
public class BlackWhiteListVO extends ExcelModel implements Serializable {
    private String id;
    /**
     * 代码
     */
    @Excel(name = "黑/白名单编号", isImportField = "true")
    @BtCompositeSearch(fieldName = "ss.code")
    private String code;
    /**
     * 名称
     */
    @Excel(name = "*黑/白名单名称", isImportField = "true")
    @BtCompositeSearch(fieldName = "ss.name")
    private String name;
    /**
     * 黑白类型:01黑名单02白名单
     */
    @Dict(dictGroup = "SYS_BUS_ENTITY_TYPE	", inject = "bwTypeName")
    @BtCompositeSearch(fieldName = "ss.bw_Type")
    private String bwType;
    @Excel(name = "*黑/白名单类型", isImportField = "true")
    private String bwTypeName;
    /**
     * 名单类别：01客商、02承兑行
     */
    @Dict(dictGroup = "SYS_BM_LIST_TYPE", inject = "listTypeName")
    @BtCompositeSearch(fieldName = "ss.LIST_TYPE")
    private String listType;
    @Excel(name = "*黑/白名单类别", isImportField = "true")
    @BtCompositeSearch(fieldName = "ss.LIST_TYPE")
    private String listTypeName;
    /**
     * 单位Id
     */
    @BtCompositeSearch(fieldName = "s.id")
    private String corpId;
    @BtCompositeSearch(fieldName = "s.name")
    private String corpCodeName;
    @Excel(name = "所属单位代码", isImportField = "true")
    private String corpCode;
    /**
     * 是否共享：1是、0否
     */
    @Dict(dictGroup = "sf_yn", inject = "isCommonName")
    @BtCompositeSearch(fieldName = "ss.IS_COMMON")
    private String isCommon;
    @Excel(name = "是否共享", isImportField = "true")
    @BtCompositeSearch(fieldName = "ss.IS_COMMON")
    private String isCommonName;
    /***数据来源：01BFS、02手工、03ERP*/
    @Dict(dictGroup = "SYS_BM_LIST_DATA_SOURCES", inject = "dataSourcesName")
    @BtCompositeSearch(fieldName = "ss.DATA_SOURCES")
    private String dataSources;
    private String dataSourcesName;
    /**
     * 国家ID
     */
    @BtCompositeSearch(fieldName = "ss.COUNTRY_ID")
    private String countryId;
    @Excel(name = "*国家/地区", isImportField = "true")
    private String countryName;
    /**
     * 状态，0禁用，1启用, -2删除
     */
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    @BtCompositeSearch(fieldName = "ss.VALID_SIGN")
    private String validSign;
    private String validSignName;
    /***/
    private String externalId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人ID
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 创建人ID
     */
    private String updateId;
    /**
     * 创建日期
     */
    private Date updateDate;
    /**
     * dto参数
     **/
    private List<BlackWhiteDTO> dtoList;
    /**
     * isControl:是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递）
     **/
    private String isControl;
    /**
     * 是否隐藏复选框 1为是
     */
    private String checkboxHide;
    public List<BlackWhiteDTO> getDtoList() {
        return dtoList;
    }
    public void setDtoList(List<BlackWhiteDTO> dtoList) {
        this.dtoList = dtoList;
    }
    public String getIsControl() {
        return isControl;
    }
    public void setIsControl(String isControl) {
        this.isControl = isControl;
    }
    @Override
    public String getBeanName() {
        return "blackWhiteListService";
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
    public String getBwType() {
        return bwType;
    }
    public void setBwType(String bwType) {
        this.bwType = bwType;
    }
    public String getListType() {
        return listType;
    }
    public void setListType(String listType) {
        this.listType = listType;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getIsCommon() {
        return isCommon;
    }
    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }
    public String getDataSources() {
        return dataSources;
    }
    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }
    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getExternalId() {
        return externalId;
    }
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpCodeName() {
        return corpCodeName;
    }
    public void setCorpCodeName(String corpCodeName) {
        this.corpCodeName = corpCodeName;
    }
    public String getListTypeName() {
        return listTypeName;
    }
    public void setListTypeName(String listTypeName) {
        this.listTypeName = listTypeName;
    }
    public String getIsCommonName() {
        return isCommonName;
    }
    public void setIsCommonName(String isCommonName) {
        this.isCommonName = isCommonName;
    }
    public String getDataSourcesName() {
        return dataSourcesName;
    }
    public void setDataSourcesName(String dataSourcesName) {
        this.dataSourcesName = dataSourcesName;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public String getValidSignName() {
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getBwTypeName() {
        return bwTypeName;
    }
    public void setBwTypeName(String bwTypeName) {
        this.bwTypeName = bwTypeName;
    }
    @Override
    public String toString() {
        return "BlackWhiteListVO{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", bwType='" + bwType + '\'' +
                ", listType='" + listType + '\'' +
                ", listTypeName='" + listTypeName + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpCodeName='" + corpCodeName + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", isCommon='" + isCommon + '\'' +
                ", isCommonName='" + isCommonName + '\'' +
                ", dataSources='" + dataSources + '\'' +
                ", dataSourcesName='" + dataSourcesName + '\'' +
                ", countryId='" + countryId + '\'' +
                ", countryName='" + countryName + '\'' +
                ", validSign='" + validSign + '\'' +
                ", validSignName='" + validSignName + '\'' +
                ", externalId='" + externalId + '\'' +
                ", rmk='" + rmk + '\'' +
                ", createId='" + createId + '\'' +
                ", createDate=" + createDate +
                ", updateId='" + updateId + '\'' +
                ", updateDate=" + updateDate +
                ", dtoList=" + dtoList +
                ", isControl='" + isControl + '\'' +
                '}';
    }
    public String getCheckboxHide() {
//        if (this.getDataSources() != null) {
//            checkboxHide = "1";
//        } else {
//            checkboxHide = SysBlackWhiteConstant.SYS_BLACK_WHITE_DATASOURCES_02.equals(this.getDataSources()) ? "0" : "1";
//        }
        if(StringUtils.isBlank(this.getDataSources())) {
            checkboxHide = "0";
        } else {
            checkboxHide = SysBlackWhiteConstant.SYS_BLACK_WHITE_DATASOURCES_02.equals(this.getDataSources()) ? "0" : "1";
        }
        return checkboxHide;
    }
    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
    }
}
