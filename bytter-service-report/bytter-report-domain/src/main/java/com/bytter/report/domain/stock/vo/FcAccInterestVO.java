package com.bytter.report.domain.stock.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class FcAccInterestVO implements Serializable{
	/**网点ID*/
	@Corp(inject="netName")
	private String netId;
	/**网点名称*/
	private String netName;
	/**单位ID*/
	@Corp(inject="corpName")
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**币别ID*/
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	private String curName;
	/**内部账号*/
	private String blnctrAcc;
	/**内部账号名称*/
	private String accName;
	/**账户类别*/
	@Dict(dictGroup="InnerAccount.AccType",inject="accTypeName")
	private String accType;
	/**账户类别名称*/
	private String accTypeName;
	/**起息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestStartDate;
	/**止息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestEndDate;
	/**天数*/
	private Integer dayNum;
	/**利率*/
	private BigDecimal interestRate;
	/**利息*/
	private BigDecimal interest;
	/**计息标志*/
	@Dict(dictGroup="Balance.InterestSign",inject="interestSignName")
	private String interestSign;
	/**计息标志名称*/
	private String interestSignName;
	/**预提标志*/
	@Dict(dictGroup="Admin.PredrawSign",inject="predrawSignName")
	private String predrawSign;
	/**预提标志名称*/
	private String predrawSignName;
	/**应收利息*/
	private BigDecimal interestReceivable;
	/**实收利息*/
	private BigDecimal paidInterest;
	/**账户启用日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date accStartDate;
	private BigDecimal dInterest;
	private BigDecimal tInterest;
	/**企业活期存款*/
	private BigDecimal dInterest_1;
	private BigDecimal tInterest_1;
	/**企业定期*/
	private BigDecimal dInterest_5;
	private BigDecimal tInterest_5;
	/**企业通知*/
	private BigDecimal dInterest_8;
	private BigDecimal tInterest_8;
	/**企业放款*/
	private BigDecimal dInterest_3;
	private BigDecimal tInterest_3;
	/**银行活期存款*/
	private BigDecimal dInterest_2;
	private BigDecimal tInterest_2;
	/**银行借款*/
	private BigDecimal dInterest_4;
	private BigDecimal tInterest_4;
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
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccTypeName() {
		return accTypeName;
	}
	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public Date getInterestEndDate() {
		return interestEndDate;
	}
	public void setInterestEndDate(Date interestEndDate) {
		this.interestEndDate = interestEndDate;
	}
	public Integer getDayNum() {
		return dayNum;
	}
	public void setDayNum(Integer dayNum) {
		this.dayNum = dayNum;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public String getInterestSign() {
		return interestSign;
	}
	public void setInterestSign(String interestSign) {
		this.interestSign = interestSign;
	}
	public String getInterestSignName() {
		return interestSignName;
	}
	public void setInterestSignName(String interestSignName) {
		this.interestSignName = interestSignName;
	}
	public String getPredrawSign() {
		return predrawSign;
	}
	public void setPredrawSign(String predrawSign) {
		this.predrawSign = predrawSign;
	}
	public String getPredrawSignName() {
		return predrawSignName;
	}
	public void setPredrawSignName(String predrawSignName) {
		this.predrawSignName = predrawSignName;
	}
	public BigDecimal getInterestReceivable() {
		return interestReceivable;
	}
	public void setInterestReceivable(BigDecimal interestReceivable) {
		this.interestReceivable = interestReceivable;
	}
	public BigDecimal getPaidInterest() {
		return paidInterest;
	}
	public void setPaidInterest(BigDecimal paidInterest) {
		this.paidInterest = paidInterest;
	}
	public Date getAccStartDate() {
		return accStartDate;
	}
	public void setAccStartDate(Date accStartDate) {
		this.accStartDate = accStartDate;
	}
	public BigDecimal getdInterest_1() {
		return dInterest_1;
	}
	public void setdInterest_1(BigDecimal dInterest_1) {
		this.dInterest_1 = dInterest_1;
	}
	public BigDecimal gettInterest_1() {
		return tInterest_1;
	}
	public void settInterest_1(BigDecimal tInterest_1) {
		this.tInterest_1 = tInterest_1;
	}
	public BigDecimal getdInterest_5() {
		return dInterest_5;
	}
	public void setdInterest_5(BigDecimal dInterest_5) {
		this.dInterest_5 = dInterest_5;
	}
	public BigDecimal gettInterest_5() {
		return tInterest_5;
	}
	public void settInterest_5(BigDecimal tInterest_5) {
		this.tInterest_5 = tInterest_5;
	}
	public BigDecimal getdInterest_8() {
		return dInterest_8;
	}
	public void setdInterest_8(BigDecimal dInterest_8) {
		this.dInterest_8 = dInterest_8;
	}
	public BigDecimal gettInterest_8() {
		return tInterest_8;
	}
	public void settInterest_8(BigDecimal tInterest_8) {
		this.tInterest_8 = tInterest_8;
	}
	public BigDecimal getdInterest_3() {
		return dInterest_3;
	}
	public void setdInterest_3(BigDecimal dInterest_3) {
		this.dInterest_3 = dInterest_3;
	}
	public BigDecimal gettInterest_3() {
		return tInterest_3;
	}
	public void settInterest_3(BigDecimal tInterest_3) {
		this.tInterest_3 = tInterest_3;
	}
	public BigDecimal getdInterest_2() {
		return dInterest_2;
	}
	public void setdInterest_2(BigDecimal dInterest_2) {
		this.dInterest_2 = dInterest_2;
	}
	public BigDecimal gettInterest_2() {
		return tInterest_2;
	}
	public void settInterest_2(BigDecimal tInterest_2) {
		this.tInterest_2 = tInterest_2;
	}
	public BigDecimal getdInterest_4() {
		return dInterest_4;
	}
	public void setdInterest_4(BigDecimal dInterest_4) {
		this.dInterest_4 = dInterest_4;
	}
	public BigDecimal gettInterest_4() {
		return tInterest_4;
	}
	public void settInterest_4(BigDecimal tInterest_4) {
		this.tInterest_4 = tInterest_4;
	}
	public BigDecimal getdInterest() {
		return dInterest;
	}
	public void setdInterest(BigDecimal dInterest) {
		this.dInterest = dInterest;
	}
	public BigDecimal gettInterest() {
		return tInterest;
	}
	public void settInterest(BigDecimal tInterest) {
		this.tInterest = tInterest;
	}
}
