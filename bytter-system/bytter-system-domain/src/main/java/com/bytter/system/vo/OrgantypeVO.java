package com.bytter.system.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
/**
 * @author zhengg
 * @version V1.0
 * @Title: VO
 * @Description: 组织架构表VO
 * @date 2020-05-14 16:01:40
 */
@SuppressWarnings("serial")
public class OrgantypeVO implements Serializable {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 组织架构名称
     */
    @BtCompositeSearch(fieldName = "o.orgt_name")
    private String orgtName;
    /**
     * 是否已初始化:0否 1是
     */
    private Integer isInit;
    /**
     * 版本号
     */
    private String orgtVersion;
    /**
     * 发布日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date releaseDate;
    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date effectiveDate;
    /**
     * 状态，0禁用，1启用, 2删除
     */
    @Dict(dictGroup = "SYS_ORGANTYPE_STATUS", inject = "validSignName")
    private String validSign;
    private String validSignName;
    /**
     * 是否改变单位 0否 1是
     */
    private String isChangCorp;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建时间
     */
    private java.util.Date createTime;
    /**
     * 创建人ID
     */
    private String createBy;
    /**
     * 更新时间
     */
    private java.util.Date updateTime;
    /**
     * 更新人ID
     */
    private String updateBy;
    /**
     * 是否隐藏复选框 1为是
     */
    private String checkboxHide;
    /**
     * 是否已新增单位(已新增不能初始化) 0否 1是
     */
    private Integer isAddCorp;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  主键ID
     */
    public String getId() {
        return this.id;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  主键ID
     */
    public void setId(String id) {
        this.id = id;
    }
    public String getOrgtName() {
        return orgtName;
    }
    public void setOrgtName(String orgtName) {
        this.orgtName = orgtName;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  是否已初始化:0否 1是
     */
    public Integer getIsInit() {
        return this.isInit;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  是否已初始化:0否 1是
     */
    public void setIsInit(Integer isInit) {
        this.isInit = isInit;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  版本号
     */
    public String getOrgtVersion() {
        return this.orgtVersion;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  版本号
     */
    public void setOrgtVersion(String orgtVersion) {
        this.orgtVersion = orgtVersion;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  发布日期
     */
    public java.util.Date getReleaseDate() {
        return this.releaseDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  发布日期
     */
    public void setReleaseDate(java.util.Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  生效日期
     */
    public java.util.Date getEffectiveDate() {
        return this.effectiveDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  生效日期
     */
    public void setEffectiveDate(java.util.Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  状态，0禁用，1启用, 2删除
     */
    public String getValidSign() {
        return this.validSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  状态，0禁用，1启用, 2删除
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getValidSignName() {
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
    public String getIsChangCorp() {
        return isChangCorp;
    }
    public void setIsChangCorp(String isChangCorp) {
        this.isChangCorp = isChangCorp;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  备注
     */
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建时间
     */
    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建时间
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人ID
     */
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人ID
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新时间
     */
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人ID
     */
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人ID
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public String getCheckboxHide() {
        //if(StringUtils.isNotEmpty(this.getIsInit()) && SysOrgantypeConstant.SYS_ORGANTYPE_IS_INIT_1.equals(this.getIsInit())){
        return "0";
		/*}else{
			return "1";
		}*/
    }
    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
    }
    public Integer getIsAddCorp() {
        return isAddCorp;
    }
    public void setIsAddCorp(Integer isAddCorp) {
        this.isAddCorp = isAddCorp;
    }
    @Override
    public String toString() {
        return "OrgantypeVO{" +
                "id='" + id + '\'' +
                ", orgtName='" + orgtName + '\'' +
                ", isInit=" + isInit +
                ", orgtVersion='" + orgtVersion + '\'' +
                ", releaseDate=" + releaseDate +
                ", effectiveDate=" + effectiveDate +
                ", validSign='" + validSign + '\'' +
                ", validSignName='" + validSignName + '\'' +
                ", isChangCorp='" + isChangCorp + '\'' +
                ", rmk='" + rmk + '\'' +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                ", updateTime=" + updateTime +
                ", updateBy='" + updateBy + '\'' +
                ", checkboxHide='" + checkboxHide + '\'' +
                ", isAddCorp=" + isAddCorp +
                '}';
    }
}
