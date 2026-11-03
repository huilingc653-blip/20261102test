package com.bytter.finance.dto;
import java.io.Serializable;
import java.math.BigDecimal;
public class CmsPrecativeLoanDeferredDTO implements Serializable {
    private String id;
    /** 单位代码 */
    private String corpId;
    /** 贷款发放单号 */
    private String liCode;
    /** 展期合同号 */
    private String contractCode;
    /** 贷款到期日 */
    private java.util.Date matureDate;
    /** 展期期限 */
    private Integer deferredLimt;
    /** 期限单位 */
    private String ternUnit;
    /** 展期到期日 */
    private java.util.Date deferredRepayDate;
    /** 展期开始日期 **/
    private java.util.Date deferredStartDate;
    /** 展期利率 **/
    private BigDecimal deferredRate;
    /** 贷款账号 */
    private String loanAcc;
    /** 备注 */
    private String rmk;
    /** 状态 */
    private Integer status;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 展期单号 */
    private String billCode;
    /** 到期处理类型 **/
    private String expiryHandleType;
    /** 打回原因 **/
    private String refuseWhy;
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
    public String getLiCode() {
        return liCode;
    }
    public void setLiCode(String liCode) {
        this.liCode = liCode;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public java.util.Date getMatureDate() {
        return matureDate;
    }
    public void setMatureDate(java.util.Date matureDate) {
        this.matureDate = matureDate;
    }
    public Integer getDeferredLimt() {
        return deferredLimt;
    }
    public void setDeferredLimt(Integer deferredLimt) {
        this.deferredLimt = deferredLimt;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public java.util.Date getDeferredRepayDate() {
        return deferredRepayDate;
    }
    public void setDeferredRepayDate(java.util.Date deferredRepayDate) {
        this.deferredRepayDate = deferredRepayDate;
    }
    public java.util.Date getDeferredStartDate() {
        return deferredStartDate;
    }
    public void setDeferredStartDate(java.util.Date deferredStartDate) {
        this.deferredStartDate = deferredStartDate;
    }
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
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
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
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
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getExpiryHandleType() {
        return expiryHandleType;
    }
    public void setExpiryHandleType(String expiryHandleType) {
        this.expiryHandleType = expiryHandleType;
    }
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }
}
