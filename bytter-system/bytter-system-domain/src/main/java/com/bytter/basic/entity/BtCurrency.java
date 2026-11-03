package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 货币资料维护
 * @date 2017-05-23 11:14:22
 */
@Entity
@Table(name = "BT_CURRENCY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BtCurrency extends IdEntity implements Serializable {
    /**
     * 币别编码
     */
    private String curCode;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 英文简码
     */
    private String englishCode;
    /**
     * 汇率
     */
    private BigDecimal curExrate;
    /**
     * 币别符号
     */
    private String curSign;
    /**
     * 是否本位币
     */
    private String naturalSign;
    /**
     * 人行编码
     */
    private String pbScaleCode;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     */
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
    @Column(name = "IS_SYSTEM", nullable = false, length = 1)
    public String getIsSystem() {
        return this.isSystem;
    }
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  币别编码
     */
    @Column(name = "CUR_CODE", nullable = false, length = 10)
    public String getCurCode() {
        return this.curCode;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  币别编码
     */
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  币别名称
     */
    @Column(name = "CUR_NAME", nullable = false, length = 30)
    public String getCurName() {
        return this.curName;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  币别名称
     */
    public void setCurName(String curName) {
        this.curName = curName;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  英文简码
     */
    @Column(name = "ENGLISH_CODE", nullable = true, length = 30)
    public String getEnglishCode() {
        return this.englishCode;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  英文简码
     */
    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  汇率
     */
    @Column(name = "CUR_EXRATE", nullable = true, scale = 4, length = 12)
    public BigDecimal getCurExrate() {
        return this.curExrate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  汇率
     */
    public void setCurExrate(BigDecimal curExrate) {
        this.curExrate = curExrate;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  币别符号
     */
    @Column(name = "CUR_SIGN", nullable = true, length = 10)
    public String getCurSign() {
        return this.curSign;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  币别符号
     */
    public void setCurSign(String curSign) {
        this.curSign = curSign;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  是否本位币
     */
    @Column(name = "NATURAL_SIGN", nullable = true, length = 1)
    public String getNaturalSign() {
        return this.naturalSign;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  是否本位币
     */
    public void setNaturalSign(String naturalSign) {
        this.naturalSign = naturalSign;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  人行编码
     */
    @Column(name = "PB_SCALE_CODE", nullable = true, length = 20)
    public String getPbScaleCode() {
        return this.pbScaleCode;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  人行编码
     */
    public void setPbScaleCode(String pbScaleCode) {
        this.pbScaleCode = pbScaleCode;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  备注
     */
    @Column(name = "RMK", nullable = true, length = 50)
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  有效标志
     */
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return this.validSign;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  有效标志
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  创建人登录名称
     */
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  更新人登录名称
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "NONWORK_DAY_RULE", nullable = true, length = 1)
    public String getNonworkDayRule() {
        return nonworkDayRule;
    }
    public void setNonworkDayRule(String nonworkDayRule) {
        this.nonworkDayRule = nonworkDayRule;
    }
    @Column(name = "NONWORK_DAY", nullable = true, length = 1)
    public String getNonworkDay() {
        return nonworkDay;
    }
    public void setNonworkDay(String nonworkDay) {
        this.nonworkDay = nonworkDay;
    }
    @Column(name = "SYS_CUR_ID")
    public String getSysCurId() {
        return sysCurId;
    }
    public void setSysCurId(String sysCurId) {
        this.sysCurId = sysCurId;
    }
}
