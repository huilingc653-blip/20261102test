package com.bytter.bill.domain.bill.vo;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @Title: Vo
 * @Description: 托收到账统计VO
 * @author skk
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Data
public class AbBillCollectionAccountReportVo implements Serializable {
	/** 小计 */
	private BigDecimal sumMoney;
	/** 金额 */
	private BigDecimal money;
	/** 承兑人 */
	private String acceptor;
	/** 业务办理日期 */
	private String handleDate;
	/** 开始日期 */
	private String startDate;
	/** 结束日期 */
	private String endDate;
	/** 统计方式 0:按月统计 1:按日统计 */
	private String countType;
	/** 查询标志 */
	private String querySign;
	/** 单位id */
	private String corpId;
	/** 票据类型 */
	private String billType;
	/** 流通介质 */
	private String billFlag;
}
