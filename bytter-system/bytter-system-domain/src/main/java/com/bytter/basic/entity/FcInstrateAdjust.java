package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 内部利率
 * @date 2017-07-11 10:54:47
 */
@ApiModel("内部利率")
@Entity
@Table(name = "FC_INSTRATE_ADJUST", schema = "")
@SuppressWarnings("serial")
public class FcInstrateAdjust extends IdEntity implements java.io.Serializable {
    /**
     * 账户
     */
    @ApiModelProperty("账户")
    private String account;

    /**
     * 原利率
     */
    @ApiModelProperty("原利率")
    private java.math.BigDecimal oldInstrate;

    /**
     * 新利率
     */
    @ApiModelProperty("新利率")
    private java.math.BigDecimal newInstrate;

    /**
     * 利率类型
     */
    @ApiModelProperty("利率类型")
    private String instrtType;

    /**
     * 调整类型
     */
    @ApiModelProperty("调整类型")
    private String changtype;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String rmk;

    /**
     * 调整人
     */
    @ApiModelProperty("调整人")
    private String operatorCode;

    /**
     * 生效日期
     */
    @ApiModelProperty("生效日期")
    private java.util.Date adjustTime;

    /**
     * 复核人
     */
    @ApiModelProperty("复核人")
    private String checkerCode;

    /**
     * 复核日期
     */
    @ApiModelProperty("复核日期")
    private java.util.Date checkTime;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", example = "0")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private java.util.Date createTime;

    /**
     * 创建用户代码
     */
    @ApiModelProperty("创建用户代码")
    private String createBy;

    /**
     * 最后更新时间
     */
    @ApiModelProperty("最后更新时间")
    private java.util.Date updateTime;

    /**
     * 最后更新用户代码
     */
    @ApiModelProperty("最后更新用户代码")
    private String updateBy;

    /**
     * 科目利率表Id
     */
    @ApiModelProperty("科目利率表Id")
    private String instrateId;
    @Column(name = "INSTRATE_ID", nullable = true, length = 32)
    public String getInstrateId() {
        return instrateId;
    }
    public void setInstrateId(String instrateId) {
        this.instrateId = instrateId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  账户
     */
    @Column(name = "ACCOUNT", nullable = false, length = 50)
    public String getAccount() {
        return this.account;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  账户
     */
    public void setAccount(String account) {
        this.account = account;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  原利率
     */
    @Column(name = "OLD_INSTRATE", nullable = false, scale = 6, length = 8)
    public java.math.BigDecimal getOldInstrate() {
        return this.oldInstrate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  原利率
     */
    public void setOldInstrate(java.math.BigDecimal oldInstrate) {
        this.oldInstrate = oldInstrate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  新利率
     */
    @Column(name = "NEW_INSTRATE", nullable = false, scale = 6, length = 8)
    public java.math.BigDecimal getNewInstrate() {
        return this.newInstrate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  新利率
     */
    public void setNewInstrate(java.math.BigDecimal newInstrate) {
        this.newInstrate = newInstrate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  利率类型
     */
    @Column(name = "INSTRT_TYPE", nullable = false, length = 1)
    public String getInstrtType() {
        return this.instrtType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  利率类型
     */
    public void setInstrtType(String instrtType) {
        this.instrtType = instrtType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  调整类型
     */
    @Column(name = "CHANGTYPE", nullable = false, length = 1)
    public String getChangtype() {
        return this.changtype;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  调整类型
     */
    public void setChangtype(String changtype) {
        this.changtype = changtype;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  备注
     */
    @Column(name = "RMK", nullable = true, length = 40)
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
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  调整人
     */
    @Column(name = "OPERATOR_CODE", nullable = false, length = 32)
    public String getOperatorCode() {
        return this.operatorCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  调整人
     */
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  生效日期
     */
    @Column(name = "ADJUST_TIME", nullable = false)
    public java.util.Date getAdjustTime() {
        return this.adjustTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  生效日期
     */
    public void setAdjustTime(java.util.Date adjustTime) {
        this.adjustTime = adjustTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  复核人
     */
    @Column(name = "CHECKER_CODE", nullable = true, length = 32)
    public String getCheckerCode() {
        return this.checkerCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  复核人
     */
    public void setCheckerCode(String checkerCode) {
        this.checkerCode = checkerCode;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  复核日期
     */
    @Column(name = "CHECK_TIME", nullable = true)
    public java.util.Date getCheckTime() {
        return this.checkTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  复核日期
     */
    public void setCheckTime(java.util.Date checkTime) {
        this.checkTime = checkTime;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  状态
     */
    @Column(name = "STATUS", nullable = true, length = 5)
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建时间
     */
    @Column(name = "CREATE_TIME", nullable = true)
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
     * @return: java.lang.String  创建用户代码
     */
    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建用户代码
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  最后更新时间
     */
    @Column(name = "UPDATE_TIME", nullable = true)
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  最后更新时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  最后更新用户代码
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  最后更新用户代码
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
