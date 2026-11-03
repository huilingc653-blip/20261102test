package com.bytter.finance.domain.provide.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class LoanQueryVO implements Serializable {
    @BtCompositeSearch(fieldName = "info.id")
    private String id;
    @BtCompositeSearch(fieldName = "info.bank_name")
    private String bankName;// 银行名称
    @BtCompositeSearch(fieldName = "info.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;// 单位Id
    @BtCompositeSearch(fieldName = "info.corp_name")
    private String corpName;// 单位名称
    @BtCompositeSearch(fieldName = "info.bill_code")
    private String infoBillCode;// 申请编号
    @BtCompositeSearch(fieldName = "send.bill_code")
    private String sendBillCode;// 发放编号
    @BtCompositeSearch(fieldName = "info.pl_account")
    private String plAccount;// 借款账号
    @BtCompositeSearch(fieldName = "send.contract_code")
    private String contractCode;// 发放编码
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "info.create_date")
    private Date createDate;// 录入日期
    @BtCompositeSearch(fieldName = "info.create_name")
    private String createName;
    @BtCompositeSearch(fieldName = "info.cur_id")
//    @Currency(inject = "curName")
    private String curId;// 币别ID
    @BtCompositeSearch(fieldName = "info.cur_name")
    private String curName;// 币别名次
    @BtCompositeSearch(fieldName = "info.pl_money")
    private BigDecimal plMoney;// 借款金额
    @BtCompositeSearch(fieldName = "send.money")
    private BigDecimal sendMoney;// 本次发放金额
    @BtCompositeSearch(fieldName = "repay.sum_repay_money")
    private BigDecimal sumRepayMoney;// 总还款金额（针对某次发放的还款）
    @BtCompositeSearch(fieldName = "repayPassage.sum_repay_passage_money")
    private BigDecimal sumRepayPassageMoney;// 总还款金额（针对某次发放的还款在途）
    @BtCompositeSearch(fieldName = "send_balance_money")
    private BigDecimal sendBalanceMoney;// 发放余额（sendMoney - sumRepayMoney）
    @BtCompositeSearch(fieldName = "plRate")
    private BigDecimal plRate;// 借款利率
    @BtCompositeSearch(fieldName = "send.send_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;// 发放日期
    @BtCompositeSearch(fieldName = "send.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;// 到期日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date upRepayDate;// 还款日
    @Dict(dictGroup = "Provide.RepayType", inject = "repayTypeName")
    private String repayType;// 还款方式
    private String repayTypeName;// 还款方式
    @BtCompositeSearch(fieldName = "type.name")
    private String plClassName;// 借款种类名称
    @BtCompositeSearch(fieldName = "info.pl_Class")
    private String plClass;// 借款种类名称
    private BigDecimal plTerm;// 期限
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitName")
    private String ternUnit;// 期限单位
    private String ternUnitName;// 期限单位名称
    private BigDecimal consultantFee;// 顾问费
    @BtCompositeSearch(fieldName = "info.RMK")
    private String rmk;// 备注
    @Dict(dictGroup = "cms.status", inject = "infoStatusName")
    @BtCompositeSearch(fieldName = "info.status")
    private Integer infoStatus;
    @BtCompositeSearch(fieldName = "info.status")
    private String infoStatusName;
    @Dict(dictGroup = "cms.status", inject = "sendStatusName")
    @BtCompositeSearch(fieldName = "send.biz_status")
    private Integer sendStatus;
    private String sendStatusName;
    @Dict(dictGroup = "cms.bizStatus", inject = "infoBizStatusName")
    private String infoBizStatus;
    private String infoBizStatusName;
    @Dict(dictGroup = "cms.bizStatus", inject = "sendBizStatusName")
    private String sendBizStatus;
    private String sendBizStatusName;
    private Integer deferredCount;// 展期次数
    /** 调用合同id */
    private String conId;
    @BtCompositeSearch(fieldName = "info.PL_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plDate;// 借款开始日期
    @BtCompositeSearch(fieldName = "info.REPAY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plReDate;// 借款还款日期
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getInfoBillCode() {
        return infoBillCode;
    }
    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }
    public String getSendBillCode() {
        return sendBillCode;
    }
    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }
    public String getPlAccount() {
        return plAccount;
    }
    public void setPlAccount(String plAccount) {
        this.plAccount = plAccount;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    public BigDecimal getPlMoney() {
        return plMoney;
    }
    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getSumRepayMoney() {
        return sumRepayMoney;
    }
    public void setSumRepayMoney(BigDecimal sumRepayMoney) {
        this.sumRepayMoney = sumRepayMoney;
    }
    public BigDecimal getPlRate() {
        return plRate;
    }
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public Date getUpRepayDate() {
        return upRepayDate;
    }
    public void setUpRepayDate(Date upRepayDate) {
        this.upRepayDate = upRepayDate;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayTypeName() {
        return repayTypeName;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getPlClassName() {
        return plClassName;
    }
    public void setPlClassName(String plClassName) {
        this.plClassName = plClassName;
    }
    public BigDecimal getPlTerm() {
        return plTerm;
    }
    public void setPlTerm(BigDecimal plTerm) {
        this.plTerm = plTerm;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getTernUnitName() {
        return ternUnitName;
    }
    public void setTernUnitName(String ternUnitName) {
        this.ternUnitName = ternUnitName;
    }
    public BigDecimal getConsultantFee() {
        return consultantFee;
    }
    public void setConsultantFee(BigDecimal consultantFee) {
        this.consultantFee = consultantFee;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getInfoStatus() {
        return infoStatus;
    }
    public void setInfoStatus(Integer infoStatus) {
        this.infoStatus = infoStatus;
    }
    public String getInfoStatusName() {
        return infoStatusName;
    }
    public void setInfoStatusName(String infoStatusName) {
        this.infoStatusName = infoStatusName;
    }
    public Integer getSendStatus() {
        return sendStatus;
    }
    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }
    public String getSendStatusName() {
        return sendStatusName;
    }
    public void setSendStatusName(String sendStatusName) {
        this.sendStatusName = sendStatusName;
    }
    public String getInfoBizStatus() {
        return infoBizStatus;
    }
    public void setInfoBizStatus(String infoBizStatus) {
        this.infoBizStatus = infoBizStatus;
    }
    public String getInfoBizStatusName() {
        return infoBizStatusName;
    }
    public void setInfoBizStatusName(String infoBizStatusName) {
        this.infoBizStatusName = infoBizStatusName;
    }
    public String getSendBizStatus() {
        return sendBizStatus;
    }
    public void setSendBizStatus(String sendBizStatus) {
        this.sendBizStatus = sendBizStatus;
    }
    public String getSendBizStatusName() {
        return sendBizStatusName;
    }
    public void setSendBizStatusName(String sendBizStatusName) {
        this.sendBizStatusName = sendBizStatusName;
    }
    public BigDecimal getSumRepayPassageMoney() {
        return sumRepayPassageMoney;
    }
    public void setSumRepayPassageMoney(BigDecimal sumRepayPassageMoney) {
        this.sumRepayPassageMoney = sumRepayPassageMoney;
    }
    public Integer getDeferredCount() {
        return deferredCount;
    }
    public void setDeferredCount(Integer deferredCount) {
        this.deferredCount = deferredCount;
    }
    public BigDecimal getSendBalanceMoney() {
        return sendBalanceMoney;
    }
    public void setSendBalanceMoney(BigDecimal sendBalanceMoney) {
        this.sendBalanceMoney = sendBalanceMoney;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public Date getPlDate() {
        return plDate;
    }
    public void setPlDate(Date plDate) {
        this.plDate = plDate;
    }
    public Date getPlReDate() {
        return plReDate;
    }
    public void setPlReDate(Date plReDate) {
        this.plReDate = plReDate;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public String getPlClass() {
        return plClass;
    }
    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }
}
