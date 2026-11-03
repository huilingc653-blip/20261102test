package com.bytter.report.domain.billl.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
/**
 * 票据余额对比分析明细（报表）
 * @author zhenghj
 * @date 2020-1-19
 */
@Data
public class AbBillSoonExpireDetailedVO implements Serializable {
	/**
	 *单位id
	 */
	@BtCompositeSearch(fieldName="b.corp_Id")
	private String corpId;
	/**
	 * 所属账号
	 */
	private String bankAcc;
	/**
	 * 银行类别
	 */
	private String bankType;
	/**流通介质：10 纸票、11 电票*/
	// @Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
	@BtCompositeSearch(fieldName="a.bill_flag")
	private Integer billFlag;
	private String billFlagName;
	/**票据类型  0应付票据，1应收票据*/
	@BtCompositeSearch(fieldName="b.type")
	private String type;
	/**票据类型：10 银票、11 商票*/
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	/**票据类型名称：10 银票、11 商票*/
	private String billTypeName;
	/**票据号码*/
	private String origNo;
	/**票据金额*/
	private BigDecimal billMoney;
	/**出票日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/**到期日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/**收票人全称*/
	private String payee;
	/**
	 * 出票人
	 */
	private String payer;
	/**
	 * 承兑人
	 */
	private String acceptor;
	/**
	 * 剩余期限余额
	 */
	@BtCompositeSearch()
	private String termId;
	public void injectValues(Map<String, Map<String, DictDTO>> listDictMap){
		// 票据种类
		Optional.ofNullable(this.getBillFlag()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
					.ifPresent(dict -> this.setBillFlagName(dict.getName()));
		});
		// 票据种类
		Optional.ofNullable(this.getBillType()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
					.ifPresent(dict -> this.setBillTypeName(dict.getName()));
		});
	}
}
