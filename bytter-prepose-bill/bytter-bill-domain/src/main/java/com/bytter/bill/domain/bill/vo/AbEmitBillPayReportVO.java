package com.bytter.bill.domain.bill.vo;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
/**   
 * @Title: Vo
 * @Description: 到期兑付确认完成VO
 * @author onlineGenerator
 * @date 2018-01-15 16:35:25
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Data
public class AbEmitBillPayReportVO  implements Serializable{
	/**承兑人*/
	private String acceptor;
	/**业务办理日期*/
	private String businessDate;
	/**票面金额统计数*/
	private BigDecimal totalBillMoney;
	/**票据保证金统计数*/
	private BigDecimal totalBailMoney;
	/**敞口金额*/
	private BigDecimal surplusMoney;
}
