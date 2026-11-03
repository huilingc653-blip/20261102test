/**
 *
 */
package com.bytter.bill.domain.billbank.vo;
import java.io.Serializable;
import java.sql.*;
/**
 * @author zhenghj
 *记录银行账号查询结果
 */
public class BankAcc implements Serializable {
	private String bankAcc;//账号
	private String Acc_name;//账户名
	private double bal;//账号余额
	private double rat;//利息
	private String corpCode;//开户单位
	private String codeType;//账户类别
	private double agreementMoney;//协定金额
	private Date bankDate;//开户日期
	private String bankName;//开户行
	private String bankCode; // 银行代码
	private String bankType;//银行类别
	private String accAttribute;//账户属性
	private String bankArea;//银行地区
	private String cur;//币别
	private String curn;//
	private int accStatus;//账号状态
	private Date regularDate;//定期时间
	private int accSQ;//是否授权
	private int accUp;//是否上线
	private String areacode;
	private String corp_id;
	private Date balDate;//余额日期
	private String natureName;//银行账户性质
	private String closeReason;//销户原因
	private String accountType;//帐号类型--兴业银行接口返回
	private Date opDate;//申请日期
	private int houseHoldobject; //扫户对象：1是;0否
	private double availBal;//可用余额
	private double freezeMoney;//冻结额度
	private double controlMoney;//控制额度
	private String sapSubject;//总账科目
	private String prov;//省
	private String city;//市
	private String typeName;
	private String opRession;//开户原因
	private String revSession;//最后一次变更原因
	private String legalInfo;//公司法人及联系方式
	private String enterAddr;//地址
	private String checkInfo;//对账人及联系方式
	private String purchaseInfo;//购证人及联系方式
	private String financInfo;//财务联系人及联系方式
	private String bankSeal;//银行印鉴章
	private String operatorInfo;//开户经办人及联系方式
	private String changeInfo; //账户变更人及联系方式
	private String bigCostInfo;//大额确认人及联系方式
	private String costItems;//费用项目及金额
	private String bankfunc;//网银功能
	private String keyCodeOp;//网银操作员KEY码及证书到期日
	private String keyCodeCheck;//网银复核人KEY码及证书到期日
	private String keyCodeSq;//网银授权人KEY码及证书到期日
	private String minSafeCost;//最低安全资金
	private String bankerInfo;//银行联系人及联系方式
	private String paysysbankcode;//联行号
	private String bifId;//银行接口代码
	private String bifCode;//银行接口代码
	private String electricBill;//票据专户
	private String socCode;//社会统一信用代码
	public String getSocCode() {
		return socCode;
	}
	public void setSocCode(String socCode) {
		this.socCode = socCode;
	}
	public String getElectricBill() {
		return electricBill;
	}
	public void setElectricBill(String electricBill) {
		this.electricBill = electricBill;
	}
	public BankAcc(){
	}
	public String getCloseReason() {
		return closeReason;
	}
	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}
	public String getNatureName() {
		return natureName;
	}
	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}
	public String getCorp_id() {
		return corp_id;
	}
	public void setCorp_id(String corp_id) {
		this.corp_id = corp_id;
	}
	public String getCurn() {
		return curn;
	}
	public void setCurn(String curn) {
		this.curn = curn;
	}
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getAcc_name() {
		return Acc_name;
	}
	public void setAcc_name(String acc_name) {
		Acc_name = acc_name;
	}
	public String getAccAttribute() {
		return accAttribute;
	}
	public void setAccAttribute(String accAttribute) {
		this.accAttribute = accAttribute;
	}
	public int getAccSQ() {
		return accSQ;
	}
	public void setAccSQ(int accSQ) {
		this.accSQ = accSQ;
	}
	public int getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(int accStatus) {
		this.accStatus = accStatus;
	}
	public int getAccUp() {
		return accUp;
	}
	public void setAccUp(int accUp) {
		this.accUp = accUp;
	}
	public double getAgreementMoney() {
		return agreementMoney;
	}
	public void setAgreementMoney(double agreementMoney) {
		this.agreementMoney = agreementMoney;
	}
	public double getBal() {
		return bal;
	}
	public void setBal(double bal) {
		this.bal = bal;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getBankArea() {
		return bankArea;
	}
	public void setBankArea(String bankArea) {
		this.bankArea = bankArea;
	}
	public Date getBankDate() {
		return bankDate;
	}
	public void setBankDate(Date bankDate) {
		this.bankDate = bankDate;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCur() {
		return cur;
	}
	public void setCur(String cur) {
		this.cur = cur;
	}
	public double getRat() {
		return rat;
	}
	public void setRat(double rat) {
		this.rat = rat;
	}
	public Date getRegularDate() {
		return regularDate;
	}
	public void setRegularDate(Date regularDate) {
		this.regularDate = regularDate;
	}
	public Date getBalDate() {
		return balDate;
	}
	public void setBalDate(Date balDate) {
		this.balDate = balDate;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public Date getOpDate() {
		return opDate;
	}
	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
	public int getHouseHoldobject() {
		return houseHoldobject;
	}
	public void setHouseHoldobject(int houseHoldobject) {
		this.houseHoldobject = houseHoldobject;
	}
	public double getAvailBal() {
		return availBal;
	}
	public void setAvailBal(double availBal) {
		this.availBal = availBal;
	}
	public double getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(double freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public double getControlMoney() {
		return controlMoney;
	}
	public void setControlMoney(double controlMoney) {
		this.controlMoney = controlMoney;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getSapSubject() {
		return sapSubject;
	}
	public void setSapSubject(String sapSubject) {
		this.sapSubject = sapSubject;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getLegalInfo() {
		return legalInfo;
	}
	public void setLegalInfo(String legalInfo) {
		this.legalInfo = legalInfo;
	}
	public String getEnterAddr() {
		return enterAddr;
	}
	public void setEnterAddr(String enterAddr) {
		this.enterAddr = enterAddr;
	}
	public String getCheckInfo() {
		return checkInfo;
	}
	public void setCheckInfo(String checkInfo) {
		this.checkInfo = checkInfo;
	}
	public String getPurchaseInfo() {
		return purchaseInfo;
	}
	public void setPurchaseInfo(String purchaseInfo) {
		this.purchaseInfo = purchaseInfo;
	}
	public String getFinancInfo() {
		return financInfo;
	}
	public void setFinancInfo(String financInfo) {
		this.financInfo = financInfo;
	}
	public String getBankSeal() {
		return bankSeal;
	}
	public void setBankSeal(String bankSeal) {
		this.bankSeal = bankSeal;
	}
	public String getOperatorInfo() {
		return operatorInfo;
	}
	public void setOperatorInfo(String operatorInfo) {
		this.operatorInfo = operatorInfo;
	}
	public String getChangeInfo() {
		return changeInfo;
	}
	public void setChangeInfo(String changeInfo) {
		this.changeInfo = changeInfo;
	}
	public String getBigCostInfo() {
		return bigCostInfo;
	}
	public void setBigCostInfo(String bigCostInfo) {
		this.bigCostInfo = bigCostInfo;
	}
	public String getCostItems() {
		return costItems;
	}
	public void setCostItems(String costItems) {
		this.costItems = costItems;
	}
	public String getBankfunc() {
		return bankfunc;
	}
	public void setBankfunc(String bankfunc) {
		this.bankfunc = bankfunc;
	}
	public String getKeyCodeOp() {
		return keyCodeOp;
	}
	public void setKeyCodeOp(String keyCodeOp) {
		this.keyCodeOp = keyCodeOp;
	}
	public String getKeyCodeCheck() {
		return keyCodeCheck;
	}
	public void setKeyCodeCheck(String keyCodeCheck) {
		this.keyCodeCheck = keyCodeCheck;
	}
	public String getKeyCodeSq() {
		return keyCodeSq;
	}
	public void setKeyCodeSq(String keyCodeSq) {
		this.keyCodeSq = keyCodeSq;
	}
	public String getMinSafeCost() {
		return minSafeCost;
	}
	public void setMinSafeCost(String minSafeCost) {
		this.minSafeCost = minSafeCost;
	}
	public String getBankerInfo() {
		return bankerInfo;
	}
	public void setBankerInfo(String bankerInfo) {
		this.bankerInfo = bankerInfo;
	}
	public String getOpRession() {
		return opRession;
	}
	public void setOpRession(String opRession) {
		this.opRession = opRession;
	}
	public String getRevSession() {
		return revSession;
	}
	public void setRevSession(String revSession) {
		this.revSession = revSession;
	}
	public String getPaysysbankcode() {
		return paysysbankcode;
	}
	public void setPaysysbankcode(String paysysbankcode) {
		this.paysysbankcode = paysysbankcode;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getBifId() {
		return bifId;
	}
	public void setBifId(String bifId) {
		this.bifId = bifId;
	}
	public String toString() {
		return "BankAcc [bankAcc=" + bankAcc + ", Acc_name=" + Acc_name + ", bal=" + bal + ", rat=" + rat
				+ ", corpCode=" + corpCode + ", codeType=" + codeType + ", agreementMoney=" + agreementMoney
				+ ", bankDate=" + bankDate + ", bankName=" + bankName + ", bankCode=" + bankCode + ", bankType="
				+ bankType + ", accAttribute=" + accAttribute + ", bankArea=" + bankArea + ", cur=" + cur + ", curn="
				+ curn + ", accStatus=" + accStatus + ", regularDate=" + regularDate + ", accSQ=" + accSQ + ", accUp="
				+ accUp + ", areacode=" + areacode + ", corp_id=" + corp_id + ", balDate=" + balDate + ", natureName="
				+ natureName + ", closeReason=" + closeReason + ", accountType=" + accountType + ", opDate=" + opDate
				+ ", houseHoldobject=" + houseHoldobject + ", availBal=" + availBal + ", freezeMoney=" + freezeMoney
				+ ", controlMoney=" + controlMoney + ", sapSubject=" + sapSubject + ", prov=" + prov + ", city=" + city
				+ ", typeName=" + typeName + ", opRession=" + opRession + ", revSession=" + revSession + ", legalInfo="
				+ legalInfo + ", enterAddr=" + enterAddr + ", checkInfo=" + checkInfo + ", purchaseInfo=" + purchaseInfo
				+ ", financInfo=" + financInfo + ", bankSeal=" + bankSeal + ", operatorInfo=" + operatorInfo
				+ ", changeInfo=" + changeInfo + ", bigCostInfo=" + bigCostInfo + ", costItems=" + costItems
				+ ", bankfunc=" + bankfunc + ", keyCodeOp=" + keyCodeOp + ", keyCodeCheck=" + keyCodeCheck
				+ ", keyCodeSq=" + keyCodeSq + ", minSafeCost=" + minSafeCost + ", bankerInfo=" + bankerInfo
				+ ", paysysbankcode=" + paysysbankcode + ", bifId=" + bifId + ", bifCode=" + bifCode + "]";
	}
	
}
