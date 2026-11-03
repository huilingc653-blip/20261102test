package com.bytter.tally.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ExcelTarget("FcAccbookImportVO")
public class FcAccbookImportVO extends ExcelModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	/**记账日期*/
	@Excel(name ="*记账日期",format="yyyy/MM/dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	@BtCompositeSearch(fieldName = "tally_date")
	private Date tallyDate;
	/**起息日期*/
	@Excel(name ="*起息日期",format="yyyy/MM/dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date interestStartDate;
	/**摘要*/
	@Excel(name="*摘要")
	private String summaryContent;
	/**序号*/
	@Excel(name="*序号")
	private BigDecimal lineNo;
	/**内部账号**/
	@Excel(name="*内部账号")
	private  String account;
	/**借方发生额*/
	@Excel(name="*借方金额",numFormat = "##########0.00")
	private BigDecimal debitMoney;
	/**贷方发生额*/
	@Excel(name="*贷方金额",numFormat = "##########0.00")
	private BigDecimal creditMoney;
    /**业务品种**/
    @Excel(name="业务种类")
    private String busRaceId;
	/**凭证类型**/
	@Excel(name="凭证种类")
	private String voucherType;
	/**备注*/
	@Excel(name="*备注")
	private String rmk;
	/**附件张数*/
	@Excel(name="附件张数")
	private Integer affixAmount;
	/**业务全局ID**/
	private String busGlobalId;
	/**操作人代码**/
	private String operatorCode;
	/**红蓝标志**/
	private String RedblueSign;
	/**录入日期**/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date enrolTime;
	/**是否加急**/
	private String emergencySign;
	/**凭证状态**/
	private String voucherState;
	/**产生方式**/
	private String generateMode;
	/**创建时间**/
	private Date createDate;
	private List<FcAccbookImportVO> list;
	/**凭证种类**/
	private String voucherName;
    /**凭证种类**/
    private String raceName;

	/**对方名称*/
	@Excel(name ="对方名称")
	private String externalCorp;
	/**对方账号*/
	@Excel(name ="对方账号")
	private String externalAcc;
	/**对方账号开户行*/
	@Excel(name ="对方账号开户行")
	private String externalBank;

	public String getVoucherName() {
		return voucherName;
	}

	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<FcAccbookImportVO> getList() {
		return list;
	}

	public void setList(List<FcAccbookImportVO> list) {
		this.list = list;
	}

	public String getRedblueSign() {
		return RedblueSign;
	}

	public void setRedblueSign(String redblueSign) {
		RedblueSign = redblueSign;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTallyDate() {
		return tallyDate;
	}

	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}

	public Date getInterestStartDate() {
		return interestStartDate;
	}

	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}

	public String getSummaryContent() {
		return summaryContent;
	}

	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}

	public BigDecimal getLineNo() {
		return lineNo;
	}

	public void setLineNo(BigDecimal lineNo) {
		this.lineNo = lineNo;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public BigDecimal getDebitMoney() {
		return debitMoney;
	}

	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}

	public BigDecimal getCreditMoney() {
		return creditMoney;
	}

	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}



    public String getVoucherType() {
		return voucherType;
	}

	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}

	public String getBusRaceId() {
		return busRaceId;
	}

	public void setBusRaceId(String busRaceId) {
		this.busRaceId = busRaceId;
	}

	public String getBusGlobalId() {
		return busGlobalId;
	}

	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public Date getEnrolTime() {
		return enrolTime;
	}

	public void setEnrolTime(Date enrolTime) {
		this.enrolTime = enrolTime;
	}

	public String getEmergencySign() {
		return emergencySign;
	}

	public void setEmergencySign(String emergencySign) {
		this.emergencySign = emergencySign;
	}

	public String getVoucherState() {
		return voucherState;
	}

	public void setVoucherState(String voucherState) {
		this.voucherState = voucherState;
	}

	public String getGenerateMode() {
		return generateMode;
	}

	public void setGenerateMode(String generateMode) {
		this.generateMode = generateMode;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public Integer getAffixAmount() {
		return affixAmount;
	}

	public void setAffixAmount(Integer affixAmount) {
		this.affixAmount = affixAmount;
	}

	@Override
	public String getBeanName() {
		return "voucherImportService";
	}

	public String getExternalCorp() {
		return externalCorp;
	}

	public void setExternalCorp(String externalCorp) {
		this.externalCorp = externalCorp;
	}

	public String getExternalAcc() {
		return externalAcc;
	}

	public void setExternalAcc(String externalAcc) {
		this.externalAcc = externalAcc;
	}

	public String getExternalBank() {
		return externalBank;
	}

	public void setExternalBank(String externalBank) {
		this.externalBank = externalBank;
	}

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
}
