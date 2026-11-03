package com.bytter.biz.finacial.accbook.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 记账列表查询显示账务数据
 * 
 * @author liyh
 *
 */
public class FcAccbookMoneySearchVO implements Serializable {
	/** 总发生额 */
	@BtCompositeSearch(fieldName = "a.money")
	private BigDecimal debitMoney;
	public BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
}
