package com.bytter.biz.finacial.api.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class FcAccBalanceVO implements Serializable {
	private String id;
	/**余额日期*/
	private java.util.Date balanceDate;
	/**账号*/
	private java.lang.String account;
	/**币别代码*/
	private java.lang.String curId;
	/**账户名称*/
	private java.lang.String accName;
	/**内转笔数*/
	private java.lang.Integer trCnt;
	/**冲正笔数*/
	private java.lang.Integer rushCnt;
	/**笔数*/
	private java.lang.Integer cnt;
	/**昨日余额*/
	private java.math.BigDecimal yesterBalance;
	/**余额*/
	private java.math.BigDecimal balance;
	/**内转借方发生额*/
	private java.math.BigDecimal trDebitMoney;
	/**内转贷方发生额*/
	private java.math.BigDecimal trCreditMoney;
	/**冲正借方发生额*/
	private java.math.BigDecimal rushDebitMoney;
	/**冲正贷方发生额*/
	private java.math.BigDecimal rushCreditMoney;
	/**借方发生额*/
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	private java.math.BigDecimal creditMoney;
	/**是否记账日*/
	private java.lang.String tallyDaySign;
	/**借方笔数*/
	private java.lang.Integer debitCnt;
	/**贷方笔数*/
	private java.lang.Integer creditCnt;
	/**冻结金额*/
	private java.math.BigDecimal freezeMoney;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
}
