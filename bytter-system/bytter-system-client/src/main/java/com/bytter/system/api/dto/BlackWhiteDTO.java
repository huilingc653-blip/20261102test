package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.List;
/**
 * 黑白名单公共DTO
 * Created by zhengg on 2020/07/28.
 */
public class BlackWhiteDTO implements Serializable {
    public BlackWhiteDTO() {
    }
    public BlackWhiteDTO(String name) {
        this.name = name;
    }
    public BlackWhiteDTO(String name, String corpId) {
        this.name = name;
        this.corpId = corpId;
    }
    public BlackWhiteDTO(String reCode, String msg, List<BlackWhiteDTO> params) {
        this.reCode = reCode;
        this.msg = msg;
        this.params = params;
    }
    public BlackWhiteDTO(String code, String name, String bwType, String listType, String corpId, String isCommon, String dataSources, String countryId, String validSign, String externalId) {
        this.code = code;
        this.name = name;
        this.bwType = bwType;
        this.listType = listType;
        this.corpId = corpId;
        this.isCommon = isCommon;
        this.dataSources = dataSources;
        this.countryId = countryId;
        this.validSign = validSign;
        this.externalId = externalId;
    }
    /**
     * 客商代码
     */
    private String code;
    /**
     * 客商名称
     */
    private String name;
    /**
     * 黑白类型:01黑名单02白名单
     */
    private String bwType;
    /**
     * 名单类别：01客商、02承兑行
     */
    private String listType;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 是否共享：1是、0否
     */
    private String isCommon;
    /***数据来源：01BFS、02手工、03ERP*/
    private String dataSources;
    /**
     * 国家ID
     */
    private String countryId;
    /**
     * 状态，0禁用，1启用, -2删除
     */
    private String validSign;
    /**
     * 外部系统ID
     **/
    private String externalId;
    /**
     * appNo:申请单据号
     */
    private String appNo;
    /**
     * reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，
     */
    private String reCode;
    /**
     * msg:失败信息reCode为2和3时必填！提醒内容
     */
    private String msg;
    private List<BlackWhiteDTO> params;
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
    public String getAppNo() {
        return appNo;
    }
    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }
    public String getReCode() {
        return reCode;
    }
    public void setReCode(String reCode) {
        this.reCode = reCode;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public List<BlackWhiteDTO> getParams() {
        return params;
    }
    public void setParams(List<BlackWhiteDTO> params) {
        this.params = params;
    }
    @Override
    public String toString() {
        return "BlackWhiteDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", bwType='" + bwType + '\'' +
                ", listType='" + listType + '\'' +
                ", corpId='" + corpId + '\'' +
                ", isCommon='" + isCommon + '\'' +
                ", dataSources='" + dataSources + '\'' +
                ", countryId='" + countryId + '\'' +
                ", validSign='" + validSign + '\'' +
                ", externalId='" + externalId + '\'' +
                ", appNo='" + appNo + '\'' +
                ", reCode='" + reCode + '\'' +
                ", msg='" + msg + '\'' +
                ", params=" + params +
                '}';
    }
}
