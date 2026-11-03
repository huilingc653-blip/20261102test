package com.bytter.basic.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.Base64Util;
import com.bytter.core.utils.EmptyUtils;

import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zuoy
 * @version 1.0
 * @Title: Entity
 * @Description: 货币维护表
 * @date 2017-10-26 09:57:15
 */
public class BtCurrencyVO implements java.io.Serializable {
    @Dict(tableName = "BT_CURRENCY", valueFieldName = "id", showFiledName = "CUR_NAME", inject = "curName")
    private String id;
    /**
     * 币别编码
     */
    @BtCompositeSearch(fieldName = "t.cur_code")
    private String curCode;
    /**
     * 币别名称
     */
    @BtCompositeSearch(fieldName = "t.cur_name")
    private String curName;
    /**
     * 英文简码
     */
    @BtCompositeSearch(fieldName = "t.english_code")
    private String englishCode;
    /**
     * 汇率
     */
    @BtCompositeSearch(fieldName = "t.cur_exrate")
    private BigDecimal curExrate;
    /**
     * 币别符号
     */
    @BtCompositeSearch(fieldName = "t.cur_sign")
    private String curSign;
    /**
     * 是否本位币
     */
    @BtCompositeSearch(fieldName = "t.natural_sign")
    private String naturalSign;
    /**
     * 人行编码
     */
    @BtCompositeSearch(fieldName = "t.pb_scale_code")
    private String pbScaleCode;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "t.rmk")
    private String rmk;
    /**
     * 有效标志
     */
    @BtCompositeSearch(fieldName = "t.valid_sign")
    private String validSign;
    /**
     * 创建人登录名称
     */
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
     * 系统是否初始化
     */
    private String isSystem;
    /**
     * 非工作日规则
     */
    private String nonworkDayRule;
    /**
     * 非工作日设置
     */
    private String nonworkDay;
    /**
     * 系统币种ID
     */
    private String sysCurId;
    /**
     * 系统币种ID
     */
    private String sysCurName;
    public String getIsSystem() {
        return isSystem;
    }
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getEnglishCode() {
        return englishCode;
    }
    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }
    public BigDecimal getCurExrate() {
        return curExrate;
    }
    public void setCurExrate(BigDecimal curExrate) {
        this.curExrate = curExrate;
    }
    public String getCurSign() {
        if (!EmptyUtils.isBlank(this.curSign)) {
            //todo
            return new String(Base64Util.decode(this.curSign.getBytes()));
        } else {
            return this.curSign;
        }
    }
    public void setCurSign(String curSign) {
        //todo
        if (!EmptyUtils.isBlank(curSign) && !Base64Util.isBase64(curSign)) {
            this.curSign = Base64Util.encodeToString(curSign.getBytes());
        } else {
            this.curSign = curSign;
        }
    }
    public String getNaturalSign() {
        return naturalSign;
    }
    public void setNaturalSign(String naturalSign) {
        this.naturalSign = naturalSign;
    }
    public String getPbScaleCode() {
        return pbScaleCode;
    }
    public void setPbScaleCode(String pbScaleCode) {
        this.pbScaleCode = pbScaleCode;
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
    public String getNonworkDayRule() {
        return nonworkDayRule;
    }
    public void setNonworkDayRule(String nonworkDayRule) {
        this.nonworkDayRule = nonworkDayRule;
    }
    public String getNonworkDay() {
        return nonworkDay;
    }
    public void setNonworkDay(String nonworkDay) {
        this.nonworkDay = nonworkDay;
    }
    public String getSysCurId() {
        return sysCurId;
    }
    public void setSysCurId(String sysCurId) {
        this.sysCurId = sysCurId;
    }
    public String getSysCurName() {
        return sysCurName;
    }
    public void setSysCurName(String sysCurName) {
        this.sysCurName = sysCurName;
    }
    @Override
    public String toString() {
        return "BtCurrencyVO{" +
                "id='" + id + '\'' +
                ", curCode='" + curCode + '\'' +
                ", curName='" + curName + '\'' +
                ", englishCode='" + englishCode + '\'' +
                ", curExrate=" + curExrate +
                ", curSign='" + curSign + '\'' +
                ", naturalSign='" + naturalSign + '\'' +
                ", pbScaleCode='" + pbScaleCode + '\'' +
                ", rmk='" + rmk + '\'' +
                ", validSign='" + validSign + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
