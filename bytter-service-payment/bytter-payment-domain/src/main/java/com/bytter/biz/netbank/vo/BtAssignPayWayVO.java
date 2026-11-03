package com.bytter.biz.netbank.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
/**   
 * @Title: Entity
 * @Description: 付款方式分配VO
 * @author onlineGenerator
 * @date 2017-05-17 10:08:00
 * @version 1.0
 *
 */
@ApiModel("付款方式分配VO")
public class BtAssignPayWayVO implements Serializable {
	
	private static final long serialVersionUID = 2977747396311525565L;
	/**单位ID*/
	@Corp(inject="corpName")
	@ApiModelProperty("单位ID")
	private String corpId;

	/**单位编码*/
	@ApiModelProperty("单位编码")
	private String corpCode;

	/**单位名称*/
	@ApiModelProperty("单位名称")
	private String corpName;

	/**付款方式*/
	@Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="voucherName")
	@ApiModelProperty("付款方式")
	private String voucherCode;

	/**付款方式*/
	@ApiModelProperty("付款方式名称")
	private String voucherName;

	/**默认付款方式*/
	@Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="defVoucherName")
	@ApiModelProperty("默认付款方式")
	private String defVoucherCode;

	/**默认付款方式名称*/
	@ApiModelProperty("默认付款方式名称")
	private String defVoucherName;

	/**系统结算方式*/
	@ApiModelProperty("系统结算方式")
	private String sysVoucherCode;

	@ApiModelProperty("付款方式字符串")
	private String voucherCodesStr;
	
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getVoucherCode() {
		return voucherCode;
	}
	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getDefVoucherCode() {
		return defVoucherCode;
	}
	public void setDefVoucherCode(String defVoucherCode) {
		this.defVoucherCode = defVoucherCode;
	}
	public String getSysVoucherCode() {
		return sysVoucherCode;
	}
	public void setSysVoucherCode(String sysVoucherCode) {
		this.sysVoucherCode = sysVoucherCode;
	}
	public String getDefVoucherName() {
		return defVoucherName;
	}
	public void setDefVoucherName(String defVoucherName) {
		this.defVoucherName = defVoucherName;
	}
	public String getVoucherCodesStr() {
		return voucherCodesStr;
	}
	public void setVoucherCodesStr(String voucherCodesStr) {
		this.voucherCodesStr = voucherCodesStr;
	}
}
