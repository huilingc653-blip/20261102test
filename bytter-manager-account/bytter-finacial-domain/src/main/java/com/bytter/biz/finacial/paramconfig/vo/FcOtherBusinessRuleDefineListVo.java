package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.BtCompositeSearch;

/**
 * 记账规则列表显示视图
 * @author liyh
 *
 */
public class FcOtherBusinessRuleDefineListVo implements java.io.Serializable {
	private String id;

	/** 编码 */
	@BtCompositeSearch(fieldName="fb.code")
	private String code;
	/** 名称 */
	@BtCompositeSearch(fieldName="fb.name")
	private String name;

	/** 所属网点 */
	private String netId;
	/**网点编码*/
	private String netCode;
	/**网点名称*/
	private String netName;
	/** 收支标识*/
	private String cdSign;
	/**币别Id*/
	private String curId;
	/** 币别代码 */
	private String curCode;
	/** 币别名称*/
	private String curName;
	/** 有效标志 */
	private String validSign;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCdSign() {
		return cdSign;
	}

	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
}
