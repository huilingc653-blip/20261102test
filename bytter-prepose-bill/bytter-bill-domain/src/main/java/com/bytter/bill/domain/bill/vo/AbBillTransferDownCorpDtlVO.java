package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 票据调拨明细表信息Vo
 * @author zhengg
 * @date 2018-01-22
 * @version 1.0
 */
@SuppressWarnings("serial")
@Data
public class AbBillTransferDownCorpDtlVO implements Serializable{
	/**单位id**/
	@BtCompositeSearch(fieldName = "corpId")
	private String corpId;
	/**单位代码**/
	@BtCompositeSearch(fieldName = "corpCode")
	private String corpCode;
	/**单位名称*/
	private String corpName;
	/**网点代码**/
	@BtCompositeSearch(fieldName = "netCode")
	private String netCode;
	/**网点名称**/
	private String netName;
	/**归属日期**/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "businessDate")
	private Date businessDate;
	/**归属查询开始日期**/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "businessDate")
	private Date beginDate;
	/**归属查询结束日期**/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "businessDate")
	private Date endDate;
	/**创建日期**/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**业务类型 6201 上划 6202 归集 6204 下拨**/
	@BtCompositeSearch(fieldName = "briefId")
	// @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
	private String briefId;
	/**业务类型 6201 上划 6202 归集 6204 下拨**/
	@BtCompositeSearch(fieldName = "briefId")
	private String briefName;
	/**上划金额**/
	@BtCompositeSearch(fieldName = "shMoney")
	private BigDecimal shMoney;
	/**下拨金额**/
	@BtCompositeSearch(fieldName = "xbMoney")
	private BigDecimal xbMoney;
	/**归集金额**/
	@BtCompositeSearch(fieldName = "gjMoney")
	private BigDecimal gjMoney;
	/**下拨单号**/
	@BtCompositeSearch(fieldName = "allocatedCode")
	private String allocatedCode;
	/**是否查看跳转 1是，空或者0位否**/
	private String isView;
}
