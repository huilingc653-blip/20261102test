package com.bytter.system.api.dto;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 内部利率
 * @date 2017-07-11 10:54:47
 */
@SuppressWarnings("serial")
public class FcInstrateAdjustDTO implements java.io.Serializable {
    private String id;
    /**
     * 账户
     */
    private String account;
    /**
     * 原利率
     */
    private BigDecimal oldInstrate;
    /**
     * 新利率
     */
    private BigDecimal newInstrate;
    /**
     * 利率类型
     */
    private String instrtType;
    /**
     * 调整类型
     */
    private String changtype;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 调整人
     */
    private String operatorCode;
    /**
     * 生效日期
     */
    private Date adjustTime;
    /**
     * 复核人
     */
    private String checkerCode;
    /**
     * 复核日期
     */
    private Date checkTime;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建用户代码
     */
    private String createBy;
    /**
     * 最后更新时间
     */
    private Date updateTime;
    /**
     * 最后更新用户代码
     */
    private String updateBy;
    /**
     * 科目利率表Id
     */
    private String instrateId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public BigDecimal getOldInstrate() {
        return oldInstrate;
    }
    public void setOldInstrate(BigDecimal oldInstrate) {
        this.oldInstrate = oldInstrate;
    }
    public BigDecimal getNewInstrate() {
        return newInstrate;
    }
    public void setNewInstrate(BigDecimal newInstrate) {
        this.newInstrate = newInstrate;
    }
    public String getInstrtType() {
        return instrtType;
    }
    public void setInstrtType(String instrtType) {
        this.instrtType = instrtType;
    }
    public String getChangtype() {
        return changtype;
    }
    public void setChangtype(String changtype) {
        this.changtype = changtype;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getOperatorCode() {
        return operatorCode;
    }
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
    public Date getAdjustTime() {
        return adjustTime;
    }
    public void setAdjustTime(Date adjustTime) {
        this.adjustTime = adjustTime;
    }
    public String getCheckerCode() {
        return checkerCode;
    }
    public void setCheckerCode(String checkerCode) {
        this.checkerCode = checkerCode;
    }
    public Date getCheckTime() {
        return checkTime;
    }
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    public String getInstrateId() {
        return instrateId;
    }
    public void setInstrateId(String instrateId) {
        this.instrateId = instrateId;
    }
}
