package com.bytter.biz.netbank.vo;
import com.bytter.core.annotation.*;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.NumberFormat;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
/**
 * @Title: Entity
 * @Description: 付款通知VO
 * @author onlineGenerator
 * @date 2017-05-23 15:20:33
 * @version 1.0
 *
 */
public class NisBillheadPoolVO extends ExcelModel implements Serializable,  IBusinessKey {
	private static final long serialVersionUID = 3650731181127660208L;
	private String id;
	/**
	 * 付款单号
	 */
	@BtCompositeSearch(fieldName = "n.bill_No")
	private String billNo;
	/**单位*/
	@BtCompositeSearch(fieldName = "n.corp_Id")
	//@Corp(inject="corpName")
	private String corpId;
	/**单位名称*/
	@BtCompositeSearch(fieldName = "n.corp_Id")
	@Excel(name = "*单位名称")
	@NotNull( message = "nis.netbank.message.corpname.null")
	private String corpName;
	/**付款方式*/
	@BtCompositeSearch(fieldName = "n.voucher_Type_Id")
	//@Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="voucherName")
	private String voucherTypeId;
	/**付款方式编码*/
	private String voucherType;
	/**付款方式名称*/
	@Excel(name = "*付款方式")
	@NotNull( message = "nis.netbank.message.vouchername.null")
	@BtCompositeSearch(fieldName = "f.voucher_Name")
	private String voucherName;
	/**系统结算方式*/
	private String sysVoucherCode;
	/**是否记账*/
	private String isAccount;
	/**结算类型*/
	private String settlType;
	/**是否挂计划*/
	private String isBudget;
	/**指令发送模式*/
	private String sendMode;
	/**账号*/
	private String accId;
	/**付款账号*/
	@Excel(name = "*付款账号")
	@BtCompositeSearch(fieldName = "n.payer_Acc")
	private String payerAcc;
	/**付款账号名称*/
	@BtCompositeSearch(fieldName = "n.payer_Acc_Name")
	private String payerAccName;
	/**付款开户行名称*/
	@BtCompositeSearch(fieldName = "n.payer_Bank")
	private String payerBank;
	/**下拨账号ID*/
	private String downAccId;
	/**下拨账号*/
	@Excel(name = "银行账号")
	private String downAcc;
	/**下拨账号户名*/
	private String downAccName;
	/**下拨账号-对应账号*/
	private String downAccPrent;
	/**下拨账号-对应账户名*/
	private String downAccNamePrent;
	/**币别*/
	@BtCompositeSearch(fieldName = "n.cur_Id")
	//@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	@Excel(name = "*币别")
	@NotNull( message = "nis.netbank.message.curname.null")
	@BtCompositeSearch(fieldName = "n.cur_Id")
	private String curName;
	/**金额*/
	@Excel(name = "*金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	@BtCompositeSearch(fieldName = "n.pay_Money")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal payMoney;
	/**票据金额*/
	private BigDecimal billMoney;
	/**收款账号*/
	@Excel(name = "*收款账号")
	@NotNull( message = "nis.netbank.message.payeeacc.null")
	@BtCompositeSearch(fieldName = "n.payee_Acc")
	private String payeeAcc;
	/**收款账号名称*/
	@Excel(name = "*收款账户名称")
	@NotNull( message = "nis.netbank.message.payeeaccname.null")
	@BtCompositeSearch(fieldName = "n.payee_Acc_Name")
	private String payeeAccName;
	/**收款方开户行行号*/
	@Excel(name = "*收款开户行行号")
	@BtCompositeSearch(fieldName = "n.payee_Bank_Code")
	private String payeeBankCode;
	/**收款开户行名称*/
	@Excel(name = "收款开户行名称")
	@BtCompositeSearch(fieldName = "n.payee_Bank")
	private String payeeBank;
	/**收款行别*/
	private String bankTypeId;
	/**所在省*/
	@Region(inject="provName")
	private String prov;
	private String provName;
	/**所在市*/
	@Region(inject="cityName")
	private String city;
	private String cityName;
	/**期望支付日期*/
	@Excel(name = "*期望支付日期",format = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "n.wish_Payday")
	@JsonFormat(pattern="yyyy-MM-dd")
	@NotNull( message = "nis.capitalRecord.message.wishPayDay.isnull")
	private Date wishPayday;
	/**加急标志*/
	@Excel(name = "*加急标志", replace={"否_0","是_1"})
	@NotNull( message = "nis.netbank.message.isquick.null")
	private String isQuick;
	/**是否对私付款*/
	@Excel(name = "*是否对私付款", replace={"否_0","是_1"})
	@NotNull( message = "nis.netbank.message.isforindividual.null")
	//@Dict(dictGroup = "nisBillhead.isPayed", inject = "isForindividualName")
	@BtCompositeSearch(fieldName = "n.is_Forindividual")
	private String isForindividual;
	private String isForindividualName;
	/**接口代码*/
	@BtCompositeSearch(fieldName = "n.bif_Code")
	private String bifCode;
	/**跨行标识*/
	@Excel(name = "*跨行标志", replace={"同行_0","他行_1"})
	private String difBank;
	/**地域标识*/
	@Excel(name = "*地域标志", replace={"同城_0","异地_1"})
	private String areaSign;
	/**会计凭证号*/
	@Excel(name = "会计凭证号")
	@BtCompositeSearch(fieldName = "n.bursar_Bill_No")
	private String bursarBillNo;
	/**用途*/
	@Excel(name = "*用途")
	@NotNull( message = "nis.netbank.message.purpose.null")
	@BtCompositeSearch(fieldName = "n.purpose")
	private String purpose;
	/**附言*/
	@Excel(name = "附言")
	@BtCompositeSearch(fieldName = "n.add_Word")
	private String addWord;
	/**是否直接付款*/
	private String directPayFlag;
	/**备注说明*/
	private String rmk;
	/**证书信息*/
	private String caInfoId;
	/**产生编码日期*/
	private Date encryDate;
	/**加密随机数*/
	private String random;
	/**编码源*/
	private String encrySource;
	/**CA编码*/
	private String caCoding;
	/**记账日期*/
	private Date tallyDate;
	/**凭证流水号*/
	private String orderNo;
	/**记账凭证号码*/
	private String voucherNo;
	/**票据信息*/
	private String billId;
	/**ERP编号*/
	private String erpId;
	/**业务类别id*/
	//@Dict(tableName="NIS_BUS_TYPE",valueFieldName="id",showFiledName="BUS_TYPE_NAME",inject="busTypeName")
	private String busTypeId;
	/**业务类别*/
	@BtCompositeSearch(fieldName = "n.bus_Type")
	private String busType;
	/**业务类别名称*/
	@Excel(name = "业务类别")
	@BtCompositeSearch(fieldName = "t.bus_Type_Name")
	private String busTypeName;
	/**单据来源*/
	//@Dict(dictGroup = "nisBillhead.billFrom", inject = "billFromName")
	@BtCompositeSearch(fieldName = "n.bill_From")
	private String billFrom;
	private String billFromName;
	/**拆分前单据号*/
	private String splitId;
	/**原始拆分前单据号*/
	private String origSplitId;
	/**拆分前单据号*/
	@BtCompositeSearch(fieldName = "s.bill_No")
	private String splitBillNo;
	/**合并后单据号*/
	private String mergeId;
	/**状态*/
	//@Dict(dictGroup = "nisBillheadPool.status", inject = "statusName")
	@BtCompositeSearch(fieldName = "n.status")
	private Integer status;
	@BtCompositeSearch(fieldName = "n.status")
	private String statusName;
	/**创建人登录名称*/
	@BtCompositeSearch(fieldName = "n.create_by")
	private String createBy;
	/**创建日期*/
	@BtCompositeSearch(fieldName = "n.create_Date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**账户余额*/
	private BigDecimal balance;
	/**付款可用余额*/
	private BigDecimal availBal;
	/**附件ID*/
	private String attachmentId;
	/**费用报销ID*/
	private String[] expenseId;
	/**跨行标志*/
	private String[] difBanks;
	/**地域标志*/
	private String[] areaSigns;
	/**计划科目*/
	private String[] itemCode;
	/**计划科目名称*/
	private String[] itemName;
	/**执行金额*/
	private BigDecimal[] processMoney;
	/**汇率*/
	private BigDecimal[] exchangeRate;
	/**执行金额（折合本身币别）*/
	private BigDecimal[] processCurMoney;
	/**交易状态*/
	//@Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
	@BtCompositeSearch(fieldName = "b.voucher_Stat")
	private String voucherStat;
	private String voucherStatName;
	/**指令ID*/
	private String bisExcId;
	/**是否已付款*/
	@Excel(name = "是否已付款", replace={"否_0","是_1"})
	@Dict(dictGroup = "nisBillhead.isPayed", inject = "isPayedName")
	@BtCompositeSearch(fieldName = "n.is_Payed")
	private String isPayed;
	private String isPayedName;
	/**收款单位*/
	private String payeeCorpId;
	/**记账标志*/
	@BtCompositeSearch(fieldName = "n.tally_Sign")
	private String tallySign;
	/**成员单位内部资金计划标识,默认为非内转*/
	private String innerSign = BudgetConstant.INNER_SIGN_0;
	/**是否批量操作*/
	private Boolean isBatch;
	@Excel(name = "*计划科目")
	private String itemCodeImp;
	/**是否垫支**/
	@BtCompositeSearch(fieldName = "n.is_Advance")
	@Excel(name = "*是否垫支", replace={"否_0","是_1"})
	private String isAdvance;
	@BtCompositeSearch(fieldName = "n.is_Advance")
	private String isAdvanceName;
	/**营运资金比例**/
	@Excel(name = "营运资金比例", align = HorizontalAlignment.RIGHT, numFormat = "##########0.####")
	private BigDecimal workCapitalRatio;
	/**预计收回日期**/
	@Excel(name = "预计收回日期",format = "yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date takeBackDate;
	/**是否已开票**/
	@Excel(name = "是否已开票", replace={"否_0","是_1"})
	private String isOpenBill;
	/**银行流水号**/
	private String bankSerialNo;
	/**电子回单id**/
	private String receiptId;
	/**流水id**/
	private String dtlId;
	/**电子回单or历史电子回单 0电子回单 1历史电子回单**/
	private String receiptFlag;
	/**关联流水数量**/
	private Integer dtlNum;
	/**关联回单数量**/
	private Integer receiptNum;
	/**费用报销汇总专用*/
	/**跨行标志*/
	private String difBankStr;
	/**地域标识*/
	private String areaSignStr;
	/**明细ID*/
	private String expenseIdStr;
	/**是否自动支付*/
	//@Dict(dictGroup = "nisBillhead.isPayed", inject = "isAutoPayName")
	@BtCompositeSearch(fieldName = "n.is_Auto_Pay")
	private String isAutoPay;
	private String isAutoPayName;
	/**策略ID*/
	private String policyId;
	/**
	 * 账户属性
	 */
	private String attributeId;
	/**
	 * 账户性质
	 */
	private String natureId;
	/**
	 * 是否上线
	 */
	private String isOnline;
	/**票据类型*/
	//@Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	@BtCompositeSearch(fieldName = "n.bill_type")
	@Excel(name = "票据类型")
	private Integer billType;
	/**票据流通介质*/
	//@Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	@BtCompositeSearch(fieldName = "n.bill_flag")
	@Excel(name = "票据流通介质")
	private Integer billFlag;
	/**票据类型名称*/
	private String billTypeName;
	/**票据流通介质名称*/
	private String billFlagName;
	/**时间戳*/
	private String timeStamp;
	/**
	 * 交易日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date transTime;
	/**
	 * 票据池明细ids
	 */
	private String billPoolIds;
	/**业务单位（票据付款专用）*/
	//@Corp(inject="busCorpName")
	private String busCorpId;
	private String busCorpName;
	/**业务人员（票据付款专用）*/
	private String busPersonnel;
	/**是否保存客商资料*/
	private String isExternalCorp;
	/**是否共享*/
	private String isCommon;
	/**
	 * 票据从表id
	 */
	private String vvid;
	/**
	 * 票据池状态
	 */
	private String poolStatus;
	/**排期要素id SCHEDULING_ELEMENTS_ID*/
	@BtCompositeSearch(fieldName = "n.SCHEDULING_ELEMENTS_ID")
	private String schedulingElementsId;
	private String schedulingElementsName;
	/**排期日期 SCHEDULING_DATE*/
	@BtCompositeSearch(fieldName = "n.SCHEDULING_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date schedulingDate;
	/**付款单id*/
	private String nisBillheadId;
    /**退回意见*/
	private String reply;
	/**是否禁用*/
	private String checkboxHide;
	public String getPoolStatus() {
		return poolStatus;
	}
	public void setPoolStatus(String poolStatus) {
		this.poolStatus = poolStatus;
	}
	public String getVvid() {
		return vvid;
	}
	public void setVvid(String vvid) {
		this.vvid = vvid;
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
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getVoucherTypeId() {
		return voucherTypeId;
	}
	public void setVoucherTypeId(String voucherTypeId) {
		this.voucherTypeId = voucherTypeId;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
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
	public String getIsAccount() {
		return isAccount;
	}
	public void setIsAccount(String isAccount) {
		this.isAccount = isAccount;
	}
	public String getSettlType() {
		return settlType;
	}
	public void setSettlType(String settlType) {
		this.settlType = settlType;
	}
	public String getIsBudget() {
		return isBudget;
	}
	public void setIsBudget(String isBudget) {
		this.isBudget = isBudget;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerAccName() {
		return payerAccName;
	}
	public void setPayerAccName(String payerAccName) {
		this.payerAccName = payerAccName;
	}
	public String getPayerBank() {
		return payerBank;
	}
	public void setPayerBank(String payerBank) {
		this.payerBank = payerBank;
	}
	public String getDownAccId() {
		return downAccId;
	}
	public void setDownAccId(String downAccId) {
		this.downAccId = downAccId;
	}
	public String getDownAcc() {
		return downAcc;
	}
	public void setDownAcc(String downAcc) {
		this.downAcc = downAcc;
	}
	public String getDownAccName() {
		return downAccName;
	}
	public void setDownAccName(String downAccName) {
		this.downAccName = downAccName;
	}
	public String getDownAccPrent() {
		return downAccPrent;
	}
	public void setDownAccPrent(String downAccPrent) {
		this.downAccPrent = downAccPrent;
	}
	public String getDownAccNamePrent() {
		return downAccNamePrent;
	}
	public void setDownAccNamePrent(String downAccNamePrent) {
		this.downAccNamePrent = downAccNamePrent;
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
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public String getPayeeBankCode() {
		return payeeBankCode;
	}
	public void setPayeeBankCode(String payeeBankCode) {
		this.payeeBankCode = payeeBankCode;
	}
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
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
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	public String getIsQuick() {
		return isQuick;
	}
	public void setIsQuick(String isQuick) {
		this.isQuick = isQuick;
	}
	public String getIsForindividual() {
		return isForindividual;
	}
	public void setIsForindividual(String isForindividual) {
		this.isForindividual = isForindividual;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getDifBank() {
		return difBank;
	}
	public void setDifBank(String difBank) {
		this.difBank = difBank;
	}
	public String getAreaSign() {
		return areaSign;
	}
	public void setAreaSign(String areaSign) {
		this.areaSign = areaSign;
	}
	public String getBursarBillNo() {
		return bursarBillNo;
	}
	public void setBursarBillNo(String bursarBillNo) {
		this.bursarBillNo = bursarBillNo;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getAddWord() {
		return addWord;
	}
	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}
	public String getDirectPayFlag() {
		return directPayFlag;
	}
	public void setDirectPayFlag(String directPayFlag) {
		this.directPayFlag = directPayFlag;
	}
	public String getCaInfoId() {
		return caInfoId;
	}
	public void setCaInfoId(String caInfoId) {
		this.caInfoId = caInfoId;
	}
	public Date getEncryDate() {
		return encryDate;
	}
	public void setEncryDate(Date encryDate) {
		this.encryDate = encryDate;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public String getEncrySource() {
		return encrySource;
	}
	public void setEncrySource(String encrySource) {
		this.encrySource = encrySource;
	}
	public String getCaCoding() {
		return caCoding;
	}
	public void setCaCoding(String caCoding) {
		this.caCoding = caCoding;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
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
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getErpId() {
		return erpId;
	}
	public void setErpId(String erpId) {
		this.erpId = erpId;
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
	public String getBillFrom() {
		return billFrom;
	}
	public void setBillFrom(String billFrom) {
		this.billFrom = billFrom;
	}
	public String getSplitId() {
		return splitId;
	}
	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	public String getOrigSplitId() {
		return origSplitId;
	}
	public void setOrigSplitId(String origSplitId) {
		this.origSplitId = origSplitId;
	}
	public String getSplitBillNo() {
		return splitBillNo;
	}
	public void setSplitBillNo(String splitBillNo) {
		this.splitBillNo = splitBillNo;
	}
	public String getMergeId() {
		return mergeId;
	}
	public void setMergeId(String mergeId) {
		this.mergeId = mergeId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getProvName() {
		return provName;
	}
	public void setProvName(String provName) {
		this.provName = provName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
	@Override
	public String getBusinessKey() {
		return getId();
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getAvailBal() {
		return availBal;
	}
	public void setAvailBal(BigDecimal availBal) {
		this.availBal = availBal;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String[] getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(String[] expenseId) {
		this.expenseId = expenseId;
	}
	public String[] getDifBanks() {
		return difBanks;
	}
	public void setDifBanks(String[] difBanks) {
		this.difBanks = difBanks;
	}
	public String[] getAreaSigns() {
		return areaSigns;
	}
	public void setAreaSigns(String[] areaSigns) {
		this.areaSigns = areaSigns;
	}
	public String[] getItemCode() {
		return itemCode;
	}
	public void setItemCode(String[] itemCode) {
		this.itemCode = itemCode;
	}
	public String[] getItemName() {
		return itemName;
	}
	public void setItemName(String[] itemName) {
		this.itemName = itemName;
	}
	public BigDecimal[] getProcessMoney() {
		return processMoney;
	}
	public void setProcessMoney(BigDecimal[] processMoney) {
		this.processMoney = processMoney;
	}
	public BigDecimal[] getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal[] exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal[] getProcessCurMoney() {
		return processCurMoney;
	}
	public void setProcessCurMoney(BigDecimal[] processCurMoney) {
		this.processCurMoney = processCurMoney;
	}
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	public String getBisExcId() {
		return bisExcId;
	}
	public void setBisExcId(String bisExcId) {
		this.bisExcId = bisExcId;
	}
	public String getIsPayed() {
		return isPayed;
	}
	public void setIsPayed(String isPayed) {
		this.isPayed = isPayed;
	}
	public String getPayeeCorpId() {
		return payeeCorpId;
	}
	public void setPayeeCorpId(String payeeCorpId) {
		this.payeeCorpId = payeeCorpId;
	}
	public String getInnerSign() {
		return innerSign;
	}
	public void setInnerSign(String innerSign) {
		this.innerSign = innerSign;
	}
	public String getTallySign() {
		return tallySign;
	}
	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}
	public Boolean getBatch() {
		return isBatch;
	}
	public void setBatch(Boolean batch) {
		isBatch = batch;
	}
	public String getItemCodeImp() {
		return itemCodeImp;
	}
	public void setItemCodeImp(String itemCodeImp) {
		this.itemCodeImp = itemCodeImp;
	}
	public String getIsAdvance() {
		return isAdvance;
	}
	public void setIsAdvance(String isAdvance) {
		this.isAdvance = isAdvance;
	}
	public String getBusTypeId() {
		return busTypeId;
	}
	public void setBusTypeId(String busTypeId) {
		this.busTypeId = busTypeId;
	}
	public BigDecimal getWorkCapitalRatio() {
		return workCapitalRatio;
	}
	public void setWorkCapitalRatio(BigDecimal workCapitalRatio) {
		this.workCapitalRatio = workCapitalRatio;
	}
	public Date getTakeBackDate() {
		return takeBackDate;
	}
	public void setTakeBackDate(Date takeBackDate) {
		this.takeBackDate = takeBackDate;
	}
	public String getIsOpenBill() {
		return isOpenBill;
	}
	public void setIsOpenBill(String isOpenBill) {
		this.isOpenBill = isOpenBill;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getBillFromName() {
		return billFromName;
	}
	public void setBillFromName(String billFromName) {
		this.billFromName = billFromName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getIsPayedName() {
		return isPayedName;
	}
	public void setIsPayedName(String isPayedName) {
		this.isPayedName = isPayedName;
	}
	public String getIsAdvanceName() {
		return isAdvanceName;
	}
	public void setIsAdvanceName(String isAdvanceName) {
		this.isAdvanceName = isAdvanceName;
	}
	@Override
	public String getBeanName() {
		return "nisBillheadService";
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	public String getDifBankStr() {
		return difBankStr;
	}
	public void setDifBankStr(String difBankStr) {
		this.difBankStr = difBankStr;
	}
	public String getAreaSignStr() {
		return areaSignStr;
	}
	public void setAreaSignStr(String areaSignStr) {
		this.areaSignStr = areaSignStr;
	}
	public String getExpenseIdStr() {
		return expenseIdStr;
	}
	public void setExpenseIdStr(String expenseIdStr) {
		this.expenseIdStr = expenseIdStr;
	}
	public String getVoucherStatName() {
		return voucherStatName;
	}
	public void setVoucherStatName(String voucherStatName) {
		this.voucherStatName = voucherStatName;
	}
	public String getIsAutoPay() {
		return isAutoPay;
	}
	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}
	public String getIsAutoPayName() {
		return isAutoPayName;
	}
	public void setIsAutoPayName(String isAutoPayName) {
		this.isAutoPayName = isAutoPayName;
	}
	public String getPolicyId() {
		return policyId;
	}
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getNatureId() {
		return natureId;
	}
	public void setNatureId(String natureId) {
		this.natureId = natureId;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public String getBillFlagName() {
		return billFlagName;
	}
	public void setBillFlagName(String billFlagName) {
		this.billFlagName = billFlagName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public String getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public String getDtlId() { return dtlId; }
	public void setDtlId(String dtlId) { this.dtlId = dtlId; }
	public String getReceiptFlag() {
		return receiptFlag;
	}
	public void setReceiptFlag(String receiptFlag) {
		this.receiptFlag = receiptFlag;
	}
	public Integer getDtlNum() { return dtlNum; }
	public void setDtlNum(Integer dtlNum) { this.dtlNum = dtlNum; }
	public Integer getReceiptNum() { return receiptNum; }
	public void setReceiptNum(Integer receiptNum) { this.receiptNum = receiptNum; }
	public String getIsForindividualName() {
		return isForindividualName;
	}
	public void setIsForindividualName(String isForindividualName) {
		this.isForindividualName = isForindividualName;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getBillPoolIds() {
		return billPoolIds;
	}
	public void setBillPoolIds(String billPoolIds) {
		this.billPoolIds = billPoolIds;
	}
	public String getBusCorpId() {
		return busCorpId;
	}
	public void setBusCorpId(String busCorpId) {
		this.busCorpId = busCorpId;
	}
	public String getBusCorpName() {
		return busCorpName;
	}
	public void setBusCorpName(String busCorpName) {
		this.busCorpName = busCorpName;
	}
	public String getBusPersonnel() {
		return busPersonnel;
	}
	public void setBusPersonnel(String busPersonnel) {
		this.busPersonnel = busPersonnel;
	}
	public String getIsExternalCorp() {
		return isExternalCorp;
	}
	public void setIsExternalCorp(String isExternalCorp) {
		this.isExternalCorp = isExternalCorp;
	}
	public String getIsCommon() {
		return isCommon;
	}
	public void setIsCommon(String isCommon) {
		this.isCommon = isCommon;
	}
	public String getSchedulingElementsId() {
		return schedulingElementsId;
	}
	public void setSchedulingElementsId(String schedulingElementsId) {
		this.schedulingElementsId = schedulingElementsId;
	}
	public Date getSchedulingDate() {
		return schedulingDate;
	}
	public void setSchedulingDate(Date schedulingDate) {
		this.schedulingDate = schedulingDate;
	}
	public String getNisBillheadId() {
		return nisBillheadId;
	}
	public void setNisBillheadId(String nisBillheadId) {
		this.nisBillheadId = nisBillheadId;
	}
	public String getSchedulingElementsName() {
		return schedulingElementsName;
	}
	public void setSchedulingElementsName(String schedulingElementsName) {
		this.schedulingElementsName = schedulingElementsName;
	}
    public String getReply() {
        return reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }
	public String getCheckboxHide() {
		return checkboxHide;
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
	@Override
	public Map<String, String> getLangMap() {
		super.addLangMap("isQuick", "sf_yn");
		super.addLangMap("isForindividual", "sf_yn");
		super.addLangMap("difBank", "nisBillhead.difBank");
		super.addLangMap("areaSign", "nisBillhead.areaSign");
		super.addLangMap("isPayed", "sf_yn");
		super.addLangMap("isOpenBill", "sf_yn");
		return super.getLangMap();
	}
	@Override
	public String toString() {
		return "NisBillheadPoolVO{" +
				"id='" + id + '\'' +
				", billNo='" + billNo + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", voucherTypeId='" + voucherTypeId + '\'' +
				", voucherType='" + voucherType + '\'' +
				", voucherName='" + voucherName + '\'' +
				", sysVoucherCode='" + sysVoucherCode + '\'' +
				", isAccount='" + isAccount + '\'' +
				", settlType='" + settlType + '\'' +
				", isBudget='" + isBudget + '\'' +
				", sendMode='" + sendMode + '\'' +
				", accId='" + accId + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				", payerAccName='" + payerAccName + '\'' +
				", payerBank='" + payerBank + '\'' +
				", downAccId='" + downAccId + '\'' +
				", downAcc='" + downAcc + '\'' +
				", downAccName='" + downAccName + '\'' +
				", downAccPrent='" + downAccPrent + '\'' +
				", downAccNamePrent='" + downAccNamePrent + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", payMoney=" + payMoney +
				", billMoney=" + billMoney +
				", payeeAcc='" + payeeAcc + '\'' +
				", payeeAccName='" + payeeAccName + '\'' +
				", payeeBankCode='" + payeeBankCode + '\'' +
				", payeeBank='" + payeeBank + '\'' +
				", bankTypeId='" + bankTypeId + '\'' +
				", prov='" + prov + '\'' +
				", provName='" + provName + '\'' +
				", city='" + city + '\'' +
				", cityName='" + cityName + '\'' +
				", wishPayday=" + wishPayday +
				", isQuick='" + isQuick + '\'' +
				", isForindividual='" + isForindividual + '\'' +
				", isForindividualName='" + isForindividualName + '\'' +
				", bifCode='" + bifCode + '\'' +
				", difBank='" + difBank + '\'' +
				", areaSign='" + areaSign + '\'' +
				", bursarBillNo='" + bursarBillNo + '\'' +
				", purpose='" + purpose + '\'' +
				", addWord='" + addWord + '\'' +
				", directPayFlag='" + directPayFlag + '\'' +
				", rmk='" + rmk + '\'' +
				", caInfoId='" + caInfoId + '\'' +
				", encryDate=" + encryDate +
				", random='" + random + '\'' +
				", encrySource='" + encrySource + '\'' +
				", caCoding='" + caCoding + '\'' +
				", tallyDate=" + tallyDate +
				", orderNo='" + orderNo + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", billId='" + billId + '\'' +
				", erpId='" + erpId + '\'' +
				", busTypeId='" + busTypeId + '\'' +
				", busType='" + busType + '\'' +
				", busTypeName='" + busTypeName + '\'' +
				", billFrom='" + billFrom + '\'' +
				", billFromName='" + billFromName + '\'' +
				", splitId='" + splitId + '\'' +
				", origSplitId='" + origSplitId + '\'' +
				", splitBillNo='" + splitBillNo + '\'' +
				", mergeId='" + mergeId + '\'' +
				", status=" + status +
				", statusName='" + statusName + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", balance=" + balance +
				", attachmentId='" + attachmentId + '\'' +
				", expenseId=" + Arrays.toString(expenseId) +
				", difBanks=" + Arrays.toString(difBanks) +
				", areaSigns=" + Arrays.toString(areaSigns) +
				", itemCode=" + Arrays.toString(itemCode) +
				", itemName=" + Arrays.toString(itemName) +
				", processMoney=" + Arrays.toString(processMoney) +
				", exchangeRate=" + Arrays.toString(exchangeRate) +
				", processCurMoney=" + Arrays.toString(processCurMoney) +
				", voucherStat='" + voucherStat + '\'' +
				", voucherStatName='" + voucherStatName + '\'' +
				", bisExcId='" + bisExcId + '\'' +
				", isPayed='" + isPayed + '\'' +
				", isPayedName='" + isPayedName + '\'' +
				", payeeCorpId='" + payeeCorpId + '\'' +
				", tallySign='" + tallySign + '\'' +
				", innerSign='" + innerSign + '\'' +
				", isBatch=" + isBatch +
				", itemCodeImp='" + itemCodeImp + '\'' +
				", isAdvance='" + isAdvance + '\'' +
				", isAdvanceName='" + isAdvanceName + '\'' +
				", workCapitalRatio=" + workCapitalRatio +
				", takeBackDate=" + takeBackDate +
				", isOpenBill='" + isOpenBill + '\'' +
				", bankSerialNo='" + bankSerialNo + '\'' +
				", receiptId='" + receiptId + '\'' +
				", dtlId='" + dtlId + '\'' +
				", receiptFlag='" + receiptFlag + '\'' +
				", dtlNum=" + dtlNum +
				", receiptNum=" + receiptNum +
				", difBankStr='" + difBankStr + '\'' +
				", areaSignStr='" + areaSignStr + '\'' +
				", expenseIdStr='" + expenseIdStr + '\'' +
				", isAutoPay='" + isAutoPay + '\'' +
				", isAutoPayName='" + isAutoPayName + '\'' +
				", policyId='" + policyId + '\'' +
				", attributeId='" + attributeId + '\'' +
				", natureId='" + natureId + '\'' +
				", isOnline='" + isOnline + '\'' +
				", billType=" + billType +
				", billFlag=" + billFlag +
				", billTypeName='" + billTypeName + '\'' +
				", billFlagName='" + billFlagName + '\'' +
				", timeStamp='" + timeStamp + '\'' +
				", transTime=" + transTime +
				", billPoolIds='" + billPoolIds + '\'' +
				'}';
	}
}
