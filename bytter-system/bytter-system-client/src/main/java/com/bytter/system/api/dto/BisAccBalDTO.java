package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * Title: Entity
 * Description: BT_BANK_ACC
 *
 * @author onlineGenerator
 * @version 1.0
 * @since 2017-06-02 17:50:16
 */
public class BisAccBalDTO implements Serializable {
    private String id;
    /**
     * 业务单号
     */
    private String accNo;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 银行名称
     */
    private String accName;
    /**
     * 开户行
     */
    private String bankName;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 单位
     */
    private String corpName;
    /**
     * 币别ID
     */
    private String curId;
    /**
     * 币别
     */
    private String curName;
    /**
     * 接口代码
     */
    private String bifCode;
    /**
     * 获取时间
     */
    private java.util.Date getDate;
    /**
     * 余额日期
     */
    private java.util.Date balDate;
    /**
     * 余额
     */
    private java.math.BigDecimal bal;
    /**
     * 可用余额
     */
    private java.math.BigDecimal availBal;
    /**
     * 冻结金额
     */
    private java.math.BigDecimal frzBal;
    /**
     * 返回码
     */
    private String returnCode;
    /**
     * 返回描述信息
     */
    private String returnMsg;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 初始化余额标记（针对离线账号）
     */
    private String initSign;
    /**
     * 查询银行账户集合
     */
    private List<Map<String, Object>> btBankAccList;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAccNo() {
        return accNo;
    }
    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public java.util.Date getGetDate() {
        return getDate;
    }
    public void setGetDate(java.util.Date getDate) {
        this.getDate = getDate;
    }
    public java.util.Date getBalDate() {
        return balDate;
    }
    public void setBalDate(java.util.Date balDate) {
        this.balDate = balDate;
    }
    public java.math.BigDecimal getBal() {
        return bal;
    }
    public void setBal(java.math.BigDecimal bal) {
        this.bal = bal;
    }
    public java.math.BigDecimal getAvailBal() {
        return availBal;
    }
    public void setAvailBal(java.math.BigDecimal availBal) {
        this.availBal = availBal;
    }
    public java.math.BigDecimal getFrzBal() {
        return frzBal;
    }
    public void setFrzBal(java.math.BigDecimal frzBal) {
        this.frzBal = frzBal;
    }
    public String getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public List<Map<String, Object>> getBtBankAccList() {
        return btBankAccList;
    }
    public void setBtBankAccList(List<Map<String, Object>> btBankAccList) {
        this.btBankAccList = btBankAccList;
    }
    public String getInitSign() {
        return initSign;
    }
    public void setInitSign(String initSign) {
        this.initSign = initSign;
    }
}
