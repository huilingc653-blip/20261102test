package com.bytter.tally.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
/**
 * 记账列表查询显示账务数据
 * 
 * @author liyh
 *
 */
public class FcAccbookDebitSearchVO implements Serializable {
	/** 贷方发生额 */
	@BtCompositeSearch(fieldName = "dtl.credit_money")
	private java.math.BigDecimal creditMoney;
	@BtCompositeSearch(fieldName = "dtl.account")
	private String creditAccount;
    @BtCompositeSearch(fieldName = "dtl.account")
    private String account;
	public java.math.BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(java.math.BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public String getCreditAccount() {
		return creditAccount;
	}
	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    @Override
	public String toString() {
		return "FcAccbookDebitSearchVO{" +
				"creditMoney=" + creditMoney +
				", creditAccount='" + creditAccount + '\'' +
				'}';
	}
}
