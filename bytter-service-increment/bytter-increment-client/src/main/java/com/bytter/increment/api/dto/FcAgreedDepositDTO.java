package com.bytter.increment.api.dto;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
* <br>
* <b>功能：</b> 协定存款DTO <br>
* <b>作者：</b>wangzx<br>
* <b>日期：</b> 2019/11/11 <br>
*/
public class FcAgreedDepositDTO implements Serializable {
    private static final long serialVersionUID = 2313478363314999846L;
    private String id;
    /**资金池id/网点id*/
    private String netId;
    /**资金池名称/网点名称*/
    private String netName;
    /**单位id*/
    @Corp(inject="corpName")
    private String corpId;
    /**单位名称/单位名称*/
    private String corpName;
    /**账号id*/
    private String accId;
    /**币别*/
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**账号*/
    private String blnctrAcc;
    private String accName;
    /**开始日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**结束日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**合同号*/
    private String contractNo;
    /**状态*/
    private Integer status;
    /**单据状态*/
    private Integer billStatus;
    /**单据单据类型*/
    private String busType;
    /**延期申请前的登记ID*/
    private String agreedDepositOriId;
    /**终止日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date stopDate;
    /**复核人登录名称*/
    private String checkerBy;
    /**复核时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;
    /**资金池账户标识*/
    private String poolSign;
    /**创建日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**创建人登录名称*/
    private String createBy;
    private String userName;
    /**更新日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**更新人登录名称*/
    private String updateBy;
    /**旧开始日期(协定延期专用)*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date oldBeginDate;
    /**旧结束日期(协定延期专用)*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date oldEndDate;
    /**操作类型*/
    private String opType;
    /**打回原因*/
    private String checkBackReason;
    /**申请编号*/
    private String billNo;
    /**到期处理方式*/
    private String certificateWay;
    /**是否存入协定账号*/
    private String isAgreedDepositAcc;
    /**协定利率*/
    private BigDecimal agrInterest;
    /**协定账号ID*/
    private String agrAccId;
    /**协定账号*/
    private String agrAcc;
    /**活期账号留存金额*/
    private BigDecimal retainedBal;
    private String statusName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
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
    public String getAccId() {
        return accId;
    }
    public void setAccId(String accId) {
        this.accId = accId;
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
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getBillStatus() {
        return billStatus;
    }
    public void setBillStatus(Integer billStatus) {
        this.billStatus = billStatus;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getAgreedDepositOriId() {
        return agreedDepositOriId;
    }
    public void setAgreedDepositOriId(String agreedDepositOriId) {
        this.agreedDepositOriId = agreedDepositOriId;
    }
    public Date getStopDate() {
        return stopDate;
    }
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }
    public String getCheckerBy() {
        return checkerBy;
    }
    public void setCheckerBy(String checkerBy) {
        this.checkerBy = checkerBy;
    }
    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getOldBeginDate() {
        return oldBeginDate;
    }
    public void setOldBeginDate(Date oldBeginDate) {
        this.oldBeginDate = oldBeginDate;
    }
    public Date getOldEndDate() {
        return oldEndDate;
    }
    public void setOldEndDate(Date oldEndDate) {
        this.oldEndDate = oldEndDate;
    }
    public String getOpType() {
        return opType;
    }
    public void setOpType(String opType) {
        this.opType = opType;
    }
    public String getCheckBackReason() {
        return checkBackReason;
    }
    public void setCheckBackReason(String checkBackReason) {
        this.checkBackReason = checkBackReason;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getCertificateWay() {
        return certificateWay;
    }
    public void setCertificateWay(String certificateWay) {
        this.certificateWay = certificateWay;
    }
    public String getIsAgreedDepositAcc() {
        return isAgreedDepositAcc;
    }
    public void setIsAgreedDepositAcc(String isAgreedDepositAcc) {
        this.isAgreedDepositAcc = isAgreedDepositAcc;
    }
    public BigDecimal getAgrInterest() {
        return agrInterest;
    }
    public void setAgrInterest(BigDecimal agrInterest) {
        this.agrInterest = agrInterest;
    }
    public String getAgrAccId() {
        return agrAccId;
    }
    public void setAgrAccId(String agrAccId) {
        this.agrAccId = agrAccId;
    }
    public String getAgrAcc() {
        return agrAcc;
    }
    public void setAgrAcc(String agrAcc) {
        this.agrAcc = agrAcc;
    }
    public BigDecimal getRetainedBal() {
        return retainedBal;
    }
    public void setRetainedBal(BigDecimal retainedBal) {
        this.retainedBal = retainedBal;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
