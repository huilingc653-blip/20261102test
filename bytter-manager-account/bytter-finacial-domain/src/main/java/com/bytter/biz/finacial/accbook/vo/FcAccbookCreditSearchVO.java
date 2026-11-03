package com.bytter.biz.finacial.accbook.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
/**
 * 记账列表查询显示账务数据
 * 
 * @author liyh
 *
 */
public class FcAccbookCreditSearchVO implements Serializable {
	/**借方发生额*/
	@BtCompositeSearch(fieldName="dtl.debit_Money")
	private java.math.BigDecimal debitBegin;
	/**
	 * 借方账号
	 */
	@BtCompositeSearch(fieldName="dtl.account")
	private String debitAccount;
    @BtCompositeSearch(fieldName = "dtl.account")
    private String account;
	public java.math.BigDecimal getDebitBegin() {
		return debitBegin;
	}
	public void setDebitBegin(java.math.BigDecimal debitBegin) {
		this.debitBegin = debitBegin;
	}
	public String getDebitAccount() {
		return debitAccount;
	}
	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    @Override
	public String toString() {
		return "FcAccbookCreditSearchVO{" +
				"debitBegin=" + debitBegin +
				", debitAccount='" + debitAccount + '\'' +
				'}';
	}
}
