package com.bytter.biz.foreign.vo;

import java.util.List;

/**
 * @author wngzx
 * @version 1.0
 * @Title: 资金池组装VO
 * @Description: TreeVO
 * @date 2020-02-14 18:37:39
 */
@SuppressWarnings("serial")
public class TreeVO implements java.io.Serializable{
	/**ID,资金池时存资金池ID，非资金池存单位ID*/
	private String id;
	/**父账号id*/
	private String parentId;
	/**编码*/
	private String code;
	/**名称*/
	private String name;
	/**资金池时存单位ID*/
	private String corpId;
	/**主体单位标识 0-非资金池单位 1-资金池*/
	private String poolSign;
	/**单位名称*/
	private String corpName;
	/**币别英文简称*/
	private String englishCode;

	private List<FexCorpPoolBankAccVO> accList;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public List<FexCorpPoolBankAccVO> getAccList() {
		return accList;
	}

	public void setAccList(List<FexCorpPoolBankAccVO> accList) {
		this.accList = accList;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getPoolSign() {
		return poolSign;
	}

	public void setPoolSign(String poolSign) {
		this.poolSign = poolSign;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getEnglishCode() {
		return englishCode;
	}

	public void setEnglishCode(String englishCode) {
		this.englishCode = englishCode;
	}
}
