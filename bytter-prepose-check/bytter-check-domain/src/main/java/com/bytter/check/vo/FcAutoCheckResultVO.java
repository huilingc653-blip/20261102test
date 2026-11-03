package com.bytter.check.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.util.Date;
public class FcAutoCheckResultVO implements java.io.Serializable {
    private String id;
    /**
     * 单位id
     */
    @BtCompositeSearch(fieldName = "fr.CORP_ID")
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 银行账号
     */
    @BtCompositeSearch(fieldName = "fr.BANK_ACC")
    private String bankAcc;
    /**
     * 银行账号名称
     */
    @BtCompositeSearch(fieldName = "fr.BANK_ACC_NAME")
    private String bankAccName;
    /**
     * 对账类型
     */
    @BtCompositeSearch(fieldName = "fr.CHECK_TYPE")
    private String checkType;
    /**
     * 执行时间
     */
    @BtCompositeSearch(fieldName = "fr.RUN_TIME")
    private Date runTime;
    /**
     * 生效日期
     */
    @BtCompositeSearch(fieldName = "fr.LAUNCH_TIME")
    private Date launchTime;
    /**
     * 对账周期
     */
    @BtCompositeSearch(fieldName = "fr.CHECK_CYCLE")
    private String checkCycle;
    /**
     * 策略代码
     */
    @BtCompositeSearch(fieldName = "fr.AUTO_CHECK_CODE")
    private String autoCheckCode;
    /**
     * 策略名称
     */
    @BtCompositeSearch(fieldName = "fr.AUTO_CHECK_NAME")
    private String autoCheckName;
    /**
     * 执行情况
     */
    @BtCompositeSearch(fieldName = "fr.IS_SUCCESS")
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getBankAccName() {
        return bankAccName;
    }
    public void setBankAccName(String bankAccName) {
        this.bankAccName = bankAccName;
    }
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    public Date getRunTime() {
        return runTime;
    }
    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }
    public Date getLaunchTime() {
        return launchTime;
    }
    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }
    public String getCheckCycle() {
        return checkCycle;
    }
    public void setCheckCycle(String checkCycle) {
        this.checkCycle = checkCycle;
    }
    public String getAutoCheckCode() {
        return autoCheckCode;
    }
    public void setAutoCheckCode(String autoCheckCode) {
        this.autoCheckCode = autoCheckCode;
    }
    public String getAutoCheckName() {
        return autoCheckName;
    }
    public void setAutoCheckName(String autoCheckName) {
        this.autoCheckName = autoCheckName;
    }
    public String getIsSuccess() {
        return isSuccess;
    }
    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
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
}
