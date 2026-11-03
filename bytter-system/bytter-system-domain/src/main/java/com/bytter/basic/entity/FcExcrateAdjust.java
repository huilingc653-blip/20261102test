package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FC_EXCRATE_ADJUST
 * @date 2017-09-06 10:01:05
 */
@Entity
@Table(name = "FC_EXCRATE_ADJUST", schema = "")
@SuppressWarnings("serial")
public class FcExcrateAdjust extends IdEntity implements java.io.Serializable {
    /**
     * 汇率类型
     */
    private String typeId;
    /**
     * 币别
     */
    private String curId;
    /**
     * 参考币别
     */
    private String referCurId;
    /**
     * 执行日期
     */
    private Date preTime;
    /**
     * 原汇率
     */
    private BigDecimal oldExchangeRate;
    /**
     * 当前汇率
     */
    private BigDecimal newExchangeRate;
    /**
     * 备注
     */
    private String rmk;
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
     * 状态 0-暂存 95- 复核
     */
    private Integer status;
    /**
     * 汇买价
     */
    private BigDecimal foreignExchangePurchase;
    /**
     * 汇卖价
     */
    private BigDecimal foreignExchangeSell;
    /**
     * 中间价
     */
    private BigDecimal foreignExchangeMiddle;
    @Column(name = "TYPE_ID", nullable = false, length = 32)
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  币别
     */
    @Column(name = "CUR_ID", nullable = false, length = 10)
    public String getCurId() {
        return this.curId;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  币别
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  参考币别
     */
    @Column(name = "REFER_CUR_ID", nullable = false, length = 32)
    public String getReferCurId() {
        return this.referCurId;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  参考币别
     */
    public void setReferCurId(String referCurId) {
        this.referCurId = referCurId;
    }
    /**
     * 方法: 取得Date
     *
     * @return: Date  执行日期
     */
    @Column(name = "PRE_TIME", nullable = true)
    public Date getPreTime() {
        return this.preTime;
    }
    /**
     * 方法: 设置Date
     *
     * @param: Date  执行日期
     */
    public void setPreTime(Date preTime) {
        this.preTime = preTime;
    }
    /**
     * 方法: 取得BigDecimal
     *
     * @return: BigDecimal  原汇率
     */
    @Column(name = "OLD_EXCHANGE_RATE", nullable = true, scale = 4, length = 12)
    public BigDecimal getOldExchangeRate() {
        return this.oldExchangeRate;
    }
    /**
     * 方法: 设置BigDecimal
     *
     * @param: BigDecimal  原汇率
     */
    public void setOldExchangeRate(BigDecimal oldExchangeRate) {
        this.oldExchangeRate = oldExchangeRate;
    }
    /**
     * 方法: 取得BigDecimal
     *
     * @return: BigDecimal  当前汇率
     */
    @Column(name = "NEW_EXCHANGE_RATE", nullable = false, scale = 4, length = 12)
    public BigDecimal getNewExchangeRate() {
        return this.newExchangeRate;
    }
    /**
     * 方法: 设置BigDecimal
     *
     * @param: BigDecimal  当前汇率
     */
    public void setNewExchangeRate(BigDecimal newExchangeRate) {
        this.newExchangeRate = newExchangeRate;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  备注
     */
    @Column(name = "RMK", nullable = true, length = 255)
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
     * 方法: 取得Date
     *
     * @return: Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置Date
     *
     * @param: Date  创建日期
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
     * 方法: 取得Date
     *
     * @return: Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置Date
     *
     * @param: Date  更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * 方法: 取得Integer
     *
     * @return: Integer  状态
     */
    @Column(name = "STATUS", nullable = false, length = 4)
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置Integer
     *
     * @param: Integer  状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "FOREIGN_EXCHANGE_PURCHASE")
    public BigDecimal getForeignExchangePurchase() {
        return foreignExchangePurchase;
    }
    public void setForeignExchangePurchase(BigDecimal foreignExchangePurchase) {
        this.foreignExchangePurchase = foreignExchangePurchase;
    }
    @Column(name = "FOREIGN_EXCHANGE_SELL")
    public BigDecimal getForeignExchangeSell() {
        return foreignExchangeSell;
    }
    public void setForeignExchangeSell(BigDecimal foreignExchangeSell) {
        this.foreignExchangeSell = foreignExchangeSell;
    }
    @Column(name = "FOREIGN_EXCHANGE_MIDDLE")
    public BigDecimal getForeignExchangeMiddle() {
        return foreignExchangeMiddle;
    }
    public void setForeignExchangeMiddle(BigDecimal foreignExchangeMiddle) {
        this.foreignExchangeMiddle = foreignExchangeMiddle;
    }
}
