package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.util.List;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
public class InterestRateNoticeVO implements Serializable {
	
	private String corpId;
	private String corpName;
	private String netId;
	private String netName;
	private String nowDate;
	private String curId;
	private String curName;
	private String account;
	private String accName;
	private String printName;
	private String checkerName;
	private List<FcInterestDtlTempVo> interestDtls;
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
	public String getNowDate() {
		return nowDate;
	}
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
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
	public String getPrintName() {
		return printName;
	}
	public void setPrintName(String printName) {
		this.printName = printName;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public List<FcInterestDtlTempVo> getInterestDtls() {
		return interestDtls;
	}
	public void setInterestDtls(List<FcInterestDtlTempVo> interestDtls) {
		this.interestDtls = interestDtls;
	}
	
}
