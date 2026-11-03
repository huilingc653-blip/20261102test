package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 基准利率表
 * @author onlineGenerator
 * @date 2017-07-26 17:33:59
 * @version 1.0
 *
 */
@Entity
@Table(name = "TB_BASE_RATE", schema = "")
@SuppressWarnings("serial")
public class TbBaseRate extends IdEntity implements java.io.Serializable {
    /** 编码 */
    private String code;
    /** 基准类型名称 */
    private String baseTypeName;
    /** 生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date timeOfTakeEffect;
    /** 央行基准利率 */
    private String isBankBaseRate;
    /** 备注 */
    private String rmk;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 编码
     */
    @Column(name = "CODE")
    public String getCode() {
        return this.code;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             编码
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 基准类型名称
     */
    @Column(name = "BASE_TYPE_NAME")
    public String getBaseTypeName() {
        return this.baseTypeName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             基准类型名称
     */
    public void setBaseTypeName(String baseTypeName) {
        this.baseTypeName = baseTypeName;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 生效日期
     */
    @Column(name = "TIME_OF_TAKE_EFFECT")
    public java.util.Date getTimeOfTakeEffect() {
        return this.timeOfTakeEffect;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     *             生效日期
     */
    public void setTimeOfTakeEffect(java.util.Date timeOfTakeEffect) {
        this.timeOfTakeEffect = timeOfTakeEffect;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 央行基准利率
     */
    @Column(name = "IS_BANK_BASE_RATE")
    public String getIsBankBaseRate() {
        return this.isBankBaseRate;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             央行基准利率
     */
    public void setIsBankBaseRate(String isBankBaseRate) {
        this.isBankBaseRate = isBankBaseRate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
