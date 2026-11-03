package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 自动对账执行结果表
 * @date 2022-10-18
 */
@Entity
@Table(name = "FC_AUTO_CHECK_RESULT", schema = "")
public class FcAutoCheckResult extends IdEntity implements java.io.Serializable {
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 银行账号名称
     */
    private String bankAccName;
    /**
     * 对账类型
     */
    private String checkType;
    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date runTime;
    /**
     * 生效日期
     */
    private Date launchTime;
    /**
     * 对账周期
     */
    private String checkCycle;
    /**
     * 策略代码
     */
    private String autoCheckCode;
    /**
     * 策略名称
     */
    private String autoCheckName;
    /**
     * 执行情况
     */
    private String isSuccess;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    @Column(name = "BANK_ACC_NAME")
    public String getBankAccName() {
        return bankAccName;
    }
    public void setBankAccName(String bankAccName) {
        this.bankAccName = bankAccName;
    }
    @Column(name = "CHECK_TYPE")
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    @Column(name = "RUN_TIME")
    public Date getRunTime() {
        return runTime;
    }
    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }
    @Column(name = "LAUNCH_TIME")
    public Date getLaunchTime() {
        return launchTime;
    }
    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }
    @Column(name = "CHECK_CYCLE")
    public String getCheckCycle() {
        return checkCycle;
    }
    public void setCheckCycle(String checkCycle) {
        this.checkCycle = checkCycle;
    }
    @Column(name = "AUTO_CHECK_CODE")
    public String getAutoCheckCode() {
        return autoCheckCode;
    }
    public void setAutoCheckCode(String autoCheckCode) {
        this.autoCheckCode = autoCheckCode;
    }
    @Column(name = "AUTO_CHECK_NAME")
    public String getAutoCheckName() {
        return autoCheckName;
    }
    public void setAutoCheckName(String autoCheckName) {
        this.autoCheckName = autoCheckName;
    }
    @Column(name = "IS_SUCCESS")
    public String getIsSuccess() {
        return isSuccess;
    }
    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
