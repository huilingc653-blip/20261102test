package com.bytter.bill.domain.bill.vo;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.enums.CommonDictEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
/**   
 * @Title: VO
 * @Description: 票据银行对于自动业务配置
 * @author zhenghj
 * @date 2017-09-28
 * @version 1.0
 *
 */
@Data
public class AbBillBankAutoDtlVO implements java.io.Serializable {
	private static final long serialVersionUID = 4081290996301651180L;
	private String id;
	/**银行接口ID*/
	// @Dict(tableName="bis_bif_init",valueFieldName="id",showFiledName="name",inject="name")
	@BtCompositeSearch(fieldName = "a.bif_id")
	private String bifId;
	
	@BtCompositeSearch(fieldName = "a.bif_Code")
	private String bifCode;
	
	/**银行接口代码名称*/
	@BtCompositeSearch(fieldName = "name")
	private String name;
	
	// @Dict(tableName="bt_bank_type",valueFieldName="id",showFiledName="type_name",inject="typeName")
	@BtCompositeSearch(fieldName = "a.bank_type_id")
	private String bankTypeId;
	/**银行接口类型代码*/
	@BtCompositeSearch(fieldName = "a.bank_Type")
	private String bankType;
	/**银行类型名称*/
	@BtCompositeSearch(fieldName = "a.type_name")
	private String typeName;
	/**提示承兑*/
	// @Dict(dictGroup = "sf_yn", inject = "autoAcceptName")
	private String autoAccept;
	private String autoAcceptName;
	/**提示收票*/
	// @Dict(dictGroup = "sf_yn", inject = "autoInvoiceName")
	private String autoInvoice;
	private String autoInvoiceName;
	/**提示付款*/
	// @Dict(dictGroup = "sf_yn", inject = "autoPayName")
	private String autoPay;
	private String autoPayName;
	/**票据池背书（0否； 1是 ）*/
	// @Dict(dictGroup = "sf_yn", inject = "poolEndorseName")
	private String poolEndorse;
	private String poolEndorseName;
	/**票据池开票（0否； 1是 ）*/
	// @Dict(dictGroup = "sf_yn", inject = "poolEmitBillName")
	private String poolEmitBill;
	private String poolEmitBillName;
	/**有效标志*/
	// @Dict(dictGroup = "sf_yn", inject = "validSignName")
	private String validSign;
	private String validSignName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	/**
	 * 是否支持新一代票据，1是，0否
	 */
	private String newBillFlg;
	public String getAutoAcceptName() {
		if (ObjectUtil.isNotEmpty(autoAccept)) {
			return CommonDictEnum.getDictValue("sf_yn", autoAccept);
		}
		return autoAcceptName;
	}
	public String getAutoInvoiceName() {
		if (ObjectUtil.isNotEmpty(autoInvoice)) {
			return CommonDictEnum.getDictValue("sf_yn", autoInvoice);
		}
		return autoInvoiceName;
	}
	public String getAutoPayName() {
		if (ObjectUtil.isNotEmpty(autoPay)) {
			return CommonDictEnum.getDictValue("sf_yn", autoPay);
		}
		return autoPayName;
	}
	public String getPoolEndorseName() {
		if (ObjectUtil.isNotEmpty(poolEndorse)) {
			return CommonDictEnum.getDictValue("sf_yn", poolEndorse);
		}
		return poolEndorseName;
	}
	public String getPoolEmitBillName() {
		if (ObjectUtil.isNotEmpty(poolEmitBill)) {
			return CommonDictEnum.getDictValue("sf_yn", poolEmitBill);
		}
		return poolEmitBillName;
	}
	public String getValidSignName() {
		if (ObjectUtil.isNotEmpty(validSign)) {
			return CommonDictEnum.getDictValue("sf_yn", validSign);
		}
		return validSignName;
	}
}
