package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FC_BANK_CHECK_DTL
 * @date 2017-09-29 14:32:52
 */
@Entity
@Table(name = "FC_BANK_CHECK_DTL", schema = "")
@SuppressWarnings("serial")
public class FcBankCheckDtl extends IdEntity implements java.io.Serializable {
    /**
     * 对账日志ID
     */
    private String logId;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 币别代码
     */
    private String curCode;
    /**
     * 银行对账单ID
     */
    private String bankId;
    /**
     * 企业对账单ID
     */
    private String erpId;
    /**
     * 数据类型
     */
    private String paramType;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 反勾兑时间
     */
    private Date cancelDate;
    /**
     * 反勾兑用户
     */
    private String cancelUser;
    /**
     * 最后更新用户代码
     */
    private String updateBy;
    /**
     * 最后更新时间
     */
    private Date updateTime;
    /**
     * 创建用户代码
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    public FcBankCheckDtl() {
    }
    public FcBankCheckDtl(String curCode, String bankAcc, String logId, String bankId,
                          String paramType, String erpId) {
        this(curCode, bankAcc, logId, bankId, paramType);
        this.erpId = erpId;
    }
    public FcBankCheckDtl(String curCode, String bankAcc, String logId, String bankId,
                          String paramType) {
        this(curCode, bankAcc, logId, bankId);
        this.paramType = paramType;
    }
    public FcBankCheckDtl(String curCode, String bankAcc, String logId, String bankId) {
        this(curCode, bankAcc, logId);
        this.bankId = bankId;
    }
    public FcBankCheckDtl(String curCode, String bankAcc, String logId) {
        super();
        this.bankAcc = bankAcc;
        this.logId = logId;
        this.curCode = curCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  对账日志ID
     */
    @Column(name = "LOG_ID")
    public String getLogId() {
        return this.logId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  对账日志ID
     */
    public void setLogId(String logId) {
        this.logId = logId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  银行账号
     */
    @Column(name = "BANK_ACC")
    public String getBankAcc() {
        return this.bankAcc;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  银行账号
     */
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  币别代码
     */
    @Column(name = "CUR_CODE")
    public String getCurCode() {
        return this.curCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  币别代码
     */
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  银行对账单ID
     */
    @Column(name = "BANK_ID")
    public String getBankId() {
        return this.bankId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  银行对账单ID
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  企业对账单ID
     */
    @Column(name = "ERP_ID")
    public String getErpId() {
        return this.erpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  企业对账单ID
     */
    public void setErpId(String erpId) {
        this.erpId = erpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  数据类型
     */
    @Column(name = "PARAM_TYPE")
    public String getParamType() {
        return this.paramType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  数据类型
     */
    public void setParamType(String paramType) {
        this.paramType = paramType;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  状态
     */
    @Column(name = "STATUS")
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
     * @return: java.util.Date  反勾兑时间
     */
    @Column(name = "CANCEL_DATE")
    public Date getCancelDate() {
        return this.cancelDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  反勾兑时间
     */
    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  反勾兑用户
     */
    @Column(name = "CANCEL_USER")
    public String getCancelUser() {
        return this.cancelUser;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  反勾兑用户
     */
    public void setCancelUser(String cancelUser) {
        this.cancelUser = cancelUser;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  最后更新用户代码
     */
    @Column(name = "UPDATE_BY")
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
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  最后更新时间
     */
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  最后更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建用户代码
     */
    @Column(name = "CREATE_BY")
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
     * @return: java.util.Date  创建时间
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return this.createTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
