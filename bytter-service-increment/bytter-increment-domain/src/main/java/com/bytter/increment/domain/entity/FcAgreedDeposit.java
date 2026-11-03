package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 协定存款登记表
 * @author wangzx
 * @date 2019-05-10 16:44:38
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_AGREED_DEPOSIT", schema = "")
@SuppressWarnings("serial")
public class FcAgreedDeposit extends IdEntity implements Serializable {
    /**资金池id*/
    private String netId;

    private String netName;
    /**单位IDid*/
    private String corpId;

    private String corpName;
    /**账号id*/
    private String accId;

    private String accName;
    /**币别id*/
    private String curId;

    private String curName;
    /**账号*/
    private String blnctrAcc;
    /**开始日期*/
    private Date beginDate;
    /**结束日期*/
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
    private Date stopDate;
    /**复核人登录名称*/
    private String checkerBy;
    /**复核时间*/
    private Date checkDate;
    /**资金池账户标识*/
    private String poolSign;
    /**创建日期*/
    private Date createDate;
    /**创建人登录名称*/
    private String createBy;
    /**更新日期*/
    private Date updateDate;
    /**更新人登录名称*/
    private String updateBy;
    /**打回原因*/
    private String checkBackReason;
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
    /**申请编号*/
    private String billNo;
    @Column(name ="NET_ID",nullable=false,length=32)
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }

    @Column(name ="net_name",nullable=false,length=200)
    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    @Column(name ="CORP_ID",nullable=true,length=32)
    public String getCorpId() {
        return corpId;
    }

    @Column(name ="corp_name",nullable=true,length=200)
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name ="acc_name",nullable=true,length=500)
    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }
    @Column(name ="cur_name",nullable=true,length=30)
    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="ACC_ID",nullable=true,length=32)
    public String getAccId() {
        return accId;
    }
    public void setAccId(String accId) {
        this.accId = accId;
    }
    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name ="BLNCTR_ACC",nullable=true,length=50)
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    @Column(name ="BEGIN_DATE",nullable=true)
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    @Column(name ="END_DATE",nullable=true)
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name ="CONTRACT_NO",nullable=true,length=30)
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    @Column(name ="STATUS",nullable=true)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name ="BILL_STATUS",nullable=true)
    public Integer getBillStatus() {
        return billStatus;
    }
    public void setBillStatus(Integer billStatus) {
        this.billStatus = billStatus;
    }
    @Column(name ="BUS_TYPE",nullable=true)
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    @Column(name ="AGREED_DEPOSIT_ORI_ID",nullable=true,length=32)
    public String getAgreedDepositOriId() {
        return agreedDepositOriId;
    }
    public void setAgreedDepositOriId(String agreedDepositOriId) {
        this.agreedDepositOriId = agreedDepositOriId;
    }
    @Column(name ="STOP_DATE",nullable=true)
    public Date getStopDate() {
        return stopDate;
    }
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }
    @Column(name ="CHECKER_BY",nullable=true)
    public String getCheckerBy() {
        return checkerBy;
    }
    public void setCheckerBy(String checkerBy) {
        this.checkerBy = checkerBy;
    }
    @Column(name ="CHECK_DATE",nullable=true)
    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
    @Column(name ="POOL_SIGN",nullable=true)
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="CREATE_BY",nullable=true)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name ="UPDATE_BY",nullable=true)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="CHECK_BACK_REASON",nullable=true)
    public String getCheckBackReason() {
        return checkBackReason;
    }
    public void setCheckBackReason(String checkBackReason) {
        this.checkBackReason = checkBackReason;
    }
    @Column(name ="CERTIFICATE_WAY",nullable=true)
    public String getCertificateWay() {
        return certificateWay;
    }
    public void setCertificateWay(String certificateWay) {
        this.certificateWay = certificateWay;
    }
    @Column(name ="IS_AGREED_DEPOSIT_ACC",nullable=true)
    public String getIsAgreedDepositAcc() {
        return isAgreedDepositAcc;
    }
    public void setIsAgreedDepositAcc(String isAgreedDepositAcc) {
        this.isAgreedDepositAcc = isAgreedDepositAcc;
    }
    @Column(name ="AGR_INTEREST",nullable=true)
    public BigDecimal getAgrInterest() {
        return agrInterest;
    }
    public void setAgrInterest(BigDecimal agrInterest) {
        this.agrInterest = agrInterest;
    }
    @Column(name ="AGR_ACC_ID",nullable=true)
    public String getAgrAccId() {
        return agrAccId;
    }
    public void setAgrAccId(String agrAccId) {
        this.agrAccId = agrAccId;
    }
    @Column(name ="AGR_ACC",nullable=true)
    public String getAgrAcc() {
        return agrAcc;
    }
    public void setAgrAcc(String agrAcc) {
        this.agrAcc = agrAcc;
    }
    @Column(name ="RETAINED_BAL",nullable=true)
    public BigDecimal getRetainedBal() {
        return retainedBal;
    }
    public void setRetainedBal(BigDecimal retainedBal) {
        this.retainedBal = retainedBal;
    }
    @Column(name ="BILL_NO",nullable=true)
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}
