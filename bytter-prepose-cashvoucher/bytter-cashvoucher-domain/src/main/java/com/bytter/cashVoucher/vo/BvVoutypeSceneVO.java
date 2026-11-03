package com.bytter.cashVoucher.vo;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.enums.CommonDictEnum;
/**
 * @Title:VO
 * @Description:凭证类型场景信息VO
 * @Author: zhengg
 * @Date:2020-04-03 9:29:30
 * @Version:1.0
 */
@SuppressWarnings("serial")
public class BvVoutypeSceneVO implements java.io.Serializable {
	// @Dict(tableName="BV_VOUTYPE_SCENE",valueFieldName="id",showFiledName="name",inject="sceneName")
	private String id;
	/** 凭证种类表ID */
	@BtCompositeSearch(fieldName = "vou_Tid")
	private String vouTid;
	/** 场景名称 */
	@BtCompositeSearch(fieldName = "name")
	private String name;
	private String sceneName;
	/** 场景说明 */
	@BtCompositeSearch(fieldName = "rmk")
	private String rmk;
	/**匹配值*/
	private String matchVal;
	private String matchValName;
	/**匹配规则*/
	private String matchRule;
	private String matchRuleName;

	/** 有效标志 */
	// @Dict(dictGroup = "sf_yn", inject = "validSignName")
	private String validSign;
	@BtCompositeSearch(fieldName = "valid_Sign")
	private String validSignName;

	/** 排序字段 */
	private Integer ordernum;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVouTid() {
		return vouTid;
	}
	public void setVouTid(String vouTid) {
		this.vouTid = vouTid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getValidSignName() {
		if (ObjectUtil.isNotEmpty(validSign)) {
			return CommonDictEnum.getDictValue("sf_yn", validSign);
		}
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	public Integer getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public String getMatchVal() {
		return matchVal;
	}

	public void setMatchVal(String matchVal) {
		this.matchVal = matchVal;
	}

	public String getMatchRule() {
		return matchRule;
	}

	public void setMatchRule(String matchRule) {
		this.matchRule = matchRule;
	}

	public String getMatchValName() {
		return matchValName;
	}

	public void setMatchValName(String matchValName) {
		this.matchValName = matchValName;
	}

	public String getMatchRuleName() {
		return matchRuleName;
	}

	public void setMatchRuleName(String matchRuleName) {
		this.matchRuleName = matchRuleName;
	}

	@Override
	public String toString() {
		return "BvVoutypeSceneVO{" +
				"id='" + id + '\'' +
				", vouTid='" + vouTid + '\'' +
				", name='" + name + '\'' +
				", sceneName='" + sceneName + '\'' +
				", rmk='" + rmk + '\'' +
				", matchVal='" + matchVal + '\'' +
				", matchValName='" + matchValName + '\'' +
				", matchRule='" + matchRule + '\'' +
				", matchRuleName='" + matchRuleName + '\'' +
				", validSign='" + validSign + '\'' +
				", validSignName='" + validSignName + '\'' +
				", ordernum=" + ordernum +
				'}';
	}

}
