package com.bytter.biz.finacial.api.vo;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * @author wangzx
 */
@SuppressWarnings("serial")
public class VoucherInfoVO implements Serializable {
    private String orderNo; // 流水号
    private String voucherNo;// 凭证号
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate;// 记账日期
    private Date enrolTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;// 起息日期
    private String voucherType;// 结算方式
    private String origVoucherNo;// 原凭证号
    private String emergencySign;// 加急标识
    private String operatorCode;// 记账员代码
    private String opName;// 记账员名称
    private String checkerCode;// 复核员代码
    private String checkName;// 复核员名称
    private java.math.BigDecimal affixAmount;// 附件张数
    private String redblueSign;// 红蓝标识
    private String voucherState;// 凭证状态
    private java.math.BigDecimal lineNo;// 行号
    private String summaryContent;// 摘要
    private String account;// 账号
    private String accName;// 户名
    private java.math.BigDecimal price;// 价格
    private java.math.BigDecimal amount;// 数量
    private java.math.BigDecimal debitMoney;// 借方金额
    private java.math.BigDecimal creditMoney;// 贷方金额
    private java.math.BigDecimal balance;// 余额
    private String cashFlux;//现金流量分类
    private String curId;// 币别代码
    private String curName;// 币别名称
    private String subjectId;// 科目代码
    private String subjectName;// 科目名称
    private String corpId;// 单位代码
    private String corpName;// 单位名称
    private String code;//单位编码
    private String netId;// 网点代码
    private String netName;// 网点名称
    private Integer type;//网点标志0是网点，1是单位
    private Integer balanceWay;// 余额方向
    private String voucherName;// 结算方式名称
    private String sysVoucherCode;// 系统结算方式代码
    private String bankAcc;            // 银行账号
    private String bankName;            // 银行开户行
    private String externalCorpId;    //外单位Id
    private String externalName;
    private String bank;            // 外单位开户行
    private String externalAcc;      //外单位账号
    private String accProvince;         //账号所在省
    private String accCity;           //账号所在市
    private String rmk;           //备注
    private String createName;// 经办人名称
    private String approveName;//审批人名称
    private String viewSign;//查看标识 0查看 1打印
    private Integer busOriType;//业务来源类型
    private String origVoucherId;//原始业务ID
    /**
     * 管理员名称
     */
    private String manageName;
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    public Date getEnrolTime() {
        return enrolTime;
    }
    public void setEnrolTime(Date enrolTime) {
        this.enrolTime = enrolTime;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }
    public String getOrigVoucherNo() {
        return origVoucherNo;
    }
    public void setOrigVoucherNo(String origVoucherNo) {
        this.origVoucherNo = origVoucherNo;
    }
    public String getEmergencySign() {
        return emergencySign;
    }
    public void setEmergencySign(String emergencySign) {
        this.emergencySign = emergencySign;
    }
    public String getOperatorCode() {
        return operatorCode;
    }
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
    public String getOpName() {
        return opName;
    }
    public void setOpName(String opName) {
        this.opName = opName;
    }
    public String getCheckerCode() {
        return checkerCode;
    }
    public void setCheckerCode(String checkerCode) {
        this.checkerCode = checkerCode;
    }
    public String getCheckName() {
        return checkName;
    }
    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }
    public java.math.BigDecimal getAffixAmount() {
        return affixAmount;
    }
    public void setAffixAmount(java.math.BigDecimal affixAmount) {
        this.affixAmount = affixAmount;
    }
    public String getRedblueSign() {
        return redblueSign;
    }
    public void setRedblueSign(String redblueSign) {
        this.redblueSign = redblueSign;
    }
    public String getVoucherState() {
        return voucherState;
    }
    public void setVoucherState(String voucherState) {
        this.voucherState = voucherState;
    }
    public java.math.BigDecimal getLineNo() {
        return lineNo;
    }
    public void setLineNo(java.math.BigDecimal lineNo) {
        this.lineNo = lineNo;
    }
    public String getSummaryContent() {
        return summaryContent;
    }
    public void setSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public java.math.BigDecimal getPrice() {
        return price;
    }
    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }
    public java.math.BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }
    public java.math.BigDecimal getDebitMoney() {
        return debitMoney;
    }
    public void setDebitMoney(java.math.BigDecimal debitMoney) {
        this.debitMoney = debitMoney;
    }
    public java.math.BigDecimal getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(java.math.BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    public java.math.BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(java.math.BigDecimal balance) {
        this.balance = balance;
    }
    public String getCashFlux() {
        return cashFlux;
    }
    public void setCashFlux(String cashFlux) {
        this.cashFlux = cashFlux;
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
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
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
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getBalanceWay() {
        return balanceWay;
    }
    public void setBalanceWay(Integer balanceWay) {
        this.balanceWay = balanceWay;
    }
    public String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    public String getSysVoucherCode() {
        return sysVoucherCode;
    }
    public void setSysVoucherCode(String sysVoucherCode) {
        this.sysVoucherCode = sysVoucherCode;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getExternalAcc() {
        return externalAcc;
    }
    public void setExternalAcc(String externalAcc) {
        this.externalAcc = externalAcc;
    }
    public String getExternalCorpId() {
        return externalCorpId;
    }
    public void setExternalCorpId(String externalCorpId) {
        this.externalCorpId = externalCorpId;
    }
    public String getExternalName() {
        return externalName;
    }
    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }
    public String getAccProvince() {
        return accProvince;
    }
    public void setAccProvince(String accProvince) {
        this.accProvince = accProvince;
    }
    public String getAccCity() {
        return accCity;
    }
    public void setAccCity(String accCity) {
        this.accCity = accCity;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public String getApproveName() {
        return approveName;
    }
    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }
    public String getViewSign() {
        return viewSign;
    }
    public void setViewSign(String viewSign) {
        this.viewSign = viewSign;
    }
    public Integer getBusOriType() {
        return busOriType;
    }
    public void setBusOriType(Integer busOriType) {
        this.busOriType = busOriType;
    }
    public String getOrigVoucherId() {
        return origVoucherId;
    }
    public void setOrigVoucherId(String origVoucherId) {
        this.origVoucherId = origVoucherId;
    }
    public String getManageName() {
        return manageName;
    }
    public void setManageName(String manageName) {
        this.manageName = manageName;
    }
}
