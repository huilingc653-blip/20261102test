package com.bytter.biz.netbank.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author wangzx
 * @Description 业务类别单位设置
 * @date 2017.12.14
 */
@ApiModel("业务类别单位设置VO")
@SuppressWarnings("serial")
@ExcelTarget("ChaeOpenCorpVO")
public class ChaeOpenCorpVO extends ExcelModel implements Serializable{

	@ApiModelProperty("ID")
	private String id;

	@ApiModelProperty("单位ID")
	private String corpId;

	/**单位编码*/
	@Excel(name = "*单位编码")
	@BtCompositeSearch(fieldName = "sc.code")
	@ApiModelProperty("单位编码")
	private String corpCode;

	/**单位名称*/
	@Excel(name = "*单位名称")
	@BtCompositeSearch(fieldName = "sc.name")
	@ApiModelProperty("单位名称")
	private String corpName;

    @Dict(dictGroup = "sf_yn", inject = "isChaeOpenCorpName")
	@BtCompositeSearch(fieldName = "c.VALUE")
	@ApiModelProperty("是否差额开户单位")
	private String isChaeOpenCorp;

	@ApiModelProperty("是否差额开户单位名称")
	private String isChaeOpenCorpName;

	/**是否启动单位差额下拨0否，1是*/
	@Excel(name = "*是否启用差额下拨")
	@ApiModelProperty("是否启动单位差额下拨0否，1是")
	private String isEnableCorpChaeXB;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getIsChaeOpenCorp() {
		return isChaeOpenCorp;
	}

	public void setIsChaeOpenCorp(String isChaeOpenCorp) {
		this.isChaeOpenCorp = isChaeOpenCorp;
	}

	public String getIsChaeOpenCorpName() {
		return isChaeOpenCorpName;
	}

	public void setIsChaeOpenCorpName(String isChaeOpenCorpName) {
		this.isChaeOpenCorpName = isChaeOpenCorpName;
	}

	public String getIsEnableCorpChaeXB() {
		return isEnableCorpChaeXB;
	}

	public void setIsEnableCorpChaeXB(String isEnableCorpChaeXB) {
		this.isEnableCorpChaeXB = isEnableCorpChaeXB;
	}

	@Override
	public String getBeanName() {
		return "chaeOpenCorpService";
	}
}
