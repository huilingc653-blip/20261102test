package com.bytter.biz.finacial.api.dto;
import lombok.Data;
import java.math.BigDecimal;
/**
 * 账户联机余额
 *
 * @author liyh
 *
 */
@Data
public class FcOnlineLedgerDTO implements java.io.Serializable {
	/**
	 * 账户ID
	 */
	private String id;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 余额
	 */
	private BigDecimal tallyBalance;
	/**
	 * 复核余额
	 */
	private BigDecimal checkBalance;
	/**
	 * 币别
	 */
	private String curId;
	private String curCode;
	/**
	 * 记账笔数
	 */
	private Integer tallyCnt;
	/**
	 * 记账金额
	 */
	private BigDecimal onlineBalance;
	/**
	 * 复核笔数
	 */
	private Integer checkCnt;
	/**
	 * 冻结金额
	 */
	private BigDecimal freezeMoney;
	/** 余额方向 */
	private Integer balanceWay;
	/** 透支标志 */
	private String overdraftSign;
	/** 透支金额 */
	private BigDecimal overdraftMoney;
	/**
	 * 账务发生额
	 */
	private BigDecimal bookMoney;
	/**
	 * 账务笔数
	 */
	private Integer bookCnt;
	/**
	 * 操作类型，记账还是复核
	 */
	private String opType;
	/**
	 * 网点代码
	 */
	private String netCode;
	/**
	 * 网点ID
	 */
	private String netId;
	/**
	 * 银行类型
	 */
	private String bankType;
	/**
	 * 开户种类
	 */
	private String accType;
	/**
	 * 手续费账号
	 */
	private String chargeAcc;
	/**
	 * 手续费账号
	 */
	private String subjectCode;
	public BigDecimal getBookMoney() {
		if (bookMoney == null) {
			bookMoney = BigDecimal.ZERO;
		}
		return bookMoney;
	}
	public Integer getBookCnt() {
		if (bookCnt == null) {
			bookCnt = 0;
		}
		return bookCnt;
	}
	public Integer getTallyCnt() {
		if(tallyCnt==null){
			tallyCnt = 0;
		}
		return tallyCnt;
	}
}
