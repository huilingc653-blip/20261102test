package com.bytter.biz.receivable.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
* @Title: Entity
* @Description: 收款通知VO
* @author wangzx
* @date 2019-07-02 17:30:33
* @version 1.0
*
*/
@ExcelTarget("NisReceivableVO")
public class NisReceivableVO extends ExcelModel implements Serializable,IBusinessKey {
    private static final long serialVersionUID = 3650731181127660208L;
    private String id;
    /**收款款单号*/
    @BtCompositeSearch(fieldName = "n.bill_no")
    private String billNo;
    /**单位*/
    @BtCompositeSearch(fieldName = "n.corp_id")
    private String corpId;
    /**单位名称*/
    @Excel(name = "*单位名称")
    @NotNull( message = "nis.netbank.message.corpname.null")
    @BtCompositeSearch(fieldName = "n.corp_id")
    private String corpName;
    /**业务类别id*/
    private String busTypeId;
    /**业务类别*/
    @BtCompositeSearch(fieldName = "n.bus_type")
    private  String busType;
    /**业务类别名称*/
    @Excel(name = "*业务类别")
    @BtCompositeSearch(fieldName = "n.bus_type")
    private  String busTypeName;
    /**本方账号*/
    @Excel(name = "*本方账号")
    @NotNull( message = "nis.netbank.message.dtl.bankacc.null")
    @BtCompositeSearch(fieldName = "n.bank_acc")
    private  String bankAcc;
    /**本方账户名*/
    @BtCompositeSearch(fieldName = "n.acc_name")
    private  String accName;
    /**本方账户开户行*/
    @BtCompositeSearch(fieldName = "n.bank_name")
    private  String bankName;
    /**金额*/
    @Excel(name = "*金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @BtCompositeSearch(fieldName = "n.amt")
    private java.math.BigDecimal amt;
    /**币别*/
    @BtCompositeSearch(fieldName = "n.cur_Id")
    @Currency(inject="curName")
    private String curId;
    /**币别名称*/
    @Excel(name = "*币别")
    @NotNull( message = "nis.netbank.message.curname.null")
    @BtCompositeSearch(fieldName = "n.cur_Id")
    private String curName;
    /**对方账号*/
    @Excel(name = "*对方账号")
    @NotNull( message = "nis.netbank.message.oppAccNo.null")
    @BtCompositeSearch(fieldName = "n.opp_acc_no")
    private String oppAccNo;
    /**对方账户名*/
    @Excel(name = "*对方账号名称")
    @NotNull( message = "nis.netbank.message.oppAccName.null")
    @BtCompositeSearch(fieldName = "n.opp_acc_name")
    private String oppAccName;
    /**对方开户行行号*/
    @Excel(name = "*对方开户行行号")
    @NotNull( message = "nis.netbank.message.oppBankCode.null")
    @BtCompositeSearch(fieldName = "n.opp_bank_code")
    private String oppBankCode;
    /**对方账户开户行*/
    @Excel(name = "*对方开户行名称")
    @NotNull( message = "ras.message.rasbankcardbinding.accbankname.empty")
    @BtCompositeSearch(fieldName = "n.opp_acc_bank")
    private String oppAccBank;
    /**备注*/
    @Excel(name = "备注")
    @BtCompositeSearch(fieldName = "n.rmk")
    private String rmk;
    /**单据来源 1-收款管理 2-流水认领 3-ERP系统*/
    @BtCompositeSearch(fieldName = "n.bill_from")
    private String billFrom;
    /**状态*/
    @BtCompositeSearch(fieldName = "n.status")
    @Dict(dictGroup = "nisReceivable.status",inject = "statusName")
    private Integer status;
    private String statusName;
    /**银行流水号*/
    private String bankSerialNo;
    /**交易时间*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "*交易日期",format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "n.trans_time")
    private java.util.Date transTime;
    /**附件ID*/
    private String attachmentId;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private java.util.Date updateDate;
    /**是否批量操作*/
    private Boolean isBatch;
    /**账户余额*/
    private BigDecimal balance;
    /**业务单号ID*/
    private String billId;
    /**系统业务类别*/
    private String sysBusType;
    /**关联单号*/
    private String busBillNo;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**审批时间*/
    @BtCompositeSearch(fieldName = "n.approve_Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveDate;
    public String getBusBillNo() {
        return busBillNo;
    }
    public void setBusBillNo(String busBillNo) {
        this.busBillNo = busBillNo;
    }
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getSysBusType() {
        return sysBusType;
    }
    public void setSysBusType(String sysBusType) {
        this.sysBusType = sysBusType;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBusTypeId() {
        return busTypeId;
    }
    public void setBusTypeId(String busTypeId) {
        this.busTypeId = busTypeId;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getBusTypeName() {
        return busTypeName;
    }
    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
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
    public BigDecimal getAmt() {
        return amt;
    }
    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getOppAccNo() {
        return oppAccNo;
    }
    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
    }
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    public String getOppBankCode() {
        return oppBankCode;
    }
    public void setOppBankCode(String oppBankCode) {
        this.oppBankCode = oppBankCode;
    }
    public String getOppAccBank() {
        return oppAccBank;
    }
    public void setOppAccBank(String oppAccBank) {
        this.oppAccBank = oppAccBank;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getBillFrom() {
        return billFrom;
    }
    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getBankSerialNo() {
        return bankSerialNo;
    }
    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }
    public Date getTransTime() {
        return transTime;
    }
    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Boolean getBatch() {
        return isBatch;
    }
    public void setBatch(Boolean batch) {
        isBatch = batch;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public Date getApproveDate() {
        return approveDate;
    }
    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }
    @Override   public String getBusinessKey() {
        return getId();
    }
    @Override
    public String getBeanName() {
        return "nisReceivableService";
    }
}
