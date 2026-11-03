package com.bytter.system.vo.v1;

import java.util.List;

@SuppressWarnings("serial")
public class AccRelationTreeVO implements java.io.Serializable{
	/**ID,资金池时存资金池 ID，非资金池存单位 ID*/
	private String id;
	/**父账号 id*/
	private String parentId;
	/**编码*/
	private String code;
	/**名称*/
	private String name;
	/**资金池时存单位 ID*/
	private String corpId;
	/**主体单位标识 0-非资金池单位 1-资金池*/
	private String poolSign;
	
	/**银行账号（仅在节点类型为账号时有值）*/
	private String bankAcc;

	private String bankName;
	
	/**账户名称（仅在节点类型为账号时有值）*/
	private String accName;
	
	/**节点类型：corp-单位，account-账号，subAccount-子账户*/
	private String dataType;
	
	/**币别 ID*/
	private String curId;
	
	/**币别名称*/
	private String curName;
	
	/**唯一标识 rid = id + 四位序列号*/
	private String rid;
	
	/**子账号列表（仅在节点类型为账号时有值）*/
	private List<AccRelationTreeVO> childList;
	
	/**子节点列表（可以是单位或账号）*/
	private List<AccRelationTreeVO> accList;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

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
	public List<AccRelationTreeVO> getAccList() {
		return accList;
	}
	public void setAccList(List<AccRelationTreeVO> accList) {
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
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public List<AccRelationTreeVO> getChildList() {
		return childList;
	}
	public void setChildList(List<AccRelationTreeVO> childList) {
		this.childList = childList;
	}
}
