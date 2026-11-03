package com.bytter.tally.vo;
/**
 * 凭证展示控制bean
 * @author lixi
 */
public class VoucherRenderControlVo implements java.io.Serializable {
	private boolean summaryEditable;
    private boolean accountEditable;
    private boolean voucherTypeEditable;
    private boolean oriVoucherNoEditable;
    private boolean debitMoneyEditable;
    private boolean creditMoneyEditable;
    private boolean externalAccEditable;
    private boolean externalCorpEditable;
    private boolean bankEditable;
    private boolean provinceEditable;
    private boolean cityEditable;
    private boolean emergencySignEditable;
    private boolean createAccountAble;
    private boolean tradeRate;
    private boolean fxType;
    private boolean tradeProject;
    private boolean fxOrnot;
    private boolean accountLoadModle;
    private boolean cashFluxEditable;
	/**
	 * 借方账号查询模式（转付、代付、下拨、请款借方账户获取单位对应的内部账户）
	 */
	private boolean debitAccountLoadModle;
	public boolean isSummaryEditable() {
		return summaryEditable;
	}
	public void setSummaryEditable(boolean summaryEditable) {
		this.summaryEditable = summaryEditable;
	}
	public boolean isAccountEditable() {
		return accountEditable;
	}
	public void setAccountEditable(boolean accountEditable) {
		this.accountEditable = accountEditable;
	}
	public boolean isVoucherTypeEditable() {
		return voucherTypeEditable;
	}
	public void setVoucherTypeEditable(boolean voucherTypeEditable) {
		this.voucherTypeEditable = voucherTypeEditable;
	}
	public boolean isOriVoucherNoEditable() {
		return oriVoucherNoEditable;
	}
	public void setOriVoucherNoEditable(boolean oriVoucherNoEditable) {
		this.oriVoucherNoEditable = oriVoucherNoEditable;
	}
	public boolean isDebitMoneyEditable() {
		return debitMoneyEditable;
	}
	public void setDebitMoneyEditable(boolean debitMoneyEditable) {
		this.debitMoneyEditable = debitMoneyEditable;
	}
	public boolean isCreditMoneyEditable() {
		return creditMoneyEditable;
	}
	public void setCreditMoneyEditable(boolean creditMoneyEditable) {
		this.creditMoneyEditable = creditMoneyEditable;
	}
	public boolean isExternalAccEditable() {
		return externalAccEditable;
	}
	public void setExternalAccEditable(boolean externalAccEditable) {
		this.externalAccEditable = externalAccEditable;
	}
	public boolean isExternalCorpEditable() {
		return externalCorpEditable;
	}
	public void setExternalCorpEditable(boolean externalCorpEditable) {
		this.externalCorpEditable = externalCorpEditable;
	}
	public boolean isBankEditable() {
		return bankEditable;
	}
	public void setBankEditable(boolean bankEditable) {
		this.bankEditable = bankEditable;
	}
	public boolean isProvinceEditable() {
		return provinceEditable;
	}
	public void setProvinceEditable(boolean provinceEditable) {
		this.provinceEditable = provinceEditable;
	}
	public boolean isCityEditable() {
		return cityEditable;
	}
	public void setCityEditable(boolean cityEditable) {
		this.cityEditable = cityEditable;
	}
	public boolean isEmergencySignEditable() {
		return emergencySignEditable;
	}
	public void setEmergencySignEditable(boolean emergencySignEditable) {
		this.emergencySignEditable = emergencySignEditable;
	}
	public boolean isCreateAccountAble() {
		return createAccountAble;
	}
	public void setCreateAccountAble(boolean createAccountAble) {
		this.createAccountAble = createAccountAble;
	}
	public boolean isTradeRate() {
		return tradeRate;
	}
	public void setTradeRate(boolean tradeRate) {
		this.tradeRate = tradeRate;
	}
	public boolean isFxType() {
		return fxType;
	}
	public void setFxType(boolean fxType) {
		this.fxType = fxType;
	}
	public boolean isTradeProject() {
		return tradeProject;
	}
	public void setTradeProject(boolean tradeProject) {
		this.tradeProject = tradeProject;
	}
	public boolean isFxOrnot() {
		return fxOrnot;
	}
	public void setFxOrnot(boolean fxOrnot) {
		this.fxOrnot = fxOrnot;
	}
	public boolean isAccountLoadModle() {
		return accountLoadModle;
	}
	public void setAccountLoadModle(boolean accountLoadModle) {
		this.accountLoadModle = accountLoadModle;
	}
	public boolean isCashFluxEditable() {
		return cashFluxEditable;
	}
	public void setCashFluxEditable(boolean cashFluxEditable) {
		this.cashFluxEditable = cashFluxEditable;
	}
	public boolean isDebitAccountLoadModle() {
		return debitAccountLoadModle;
	}
	public void setDebitAccountLoadModle(boolean debitAccountLoadModle) {
		this.debitAccountLoadModle = debitAccountLoadModle;
	}
}
