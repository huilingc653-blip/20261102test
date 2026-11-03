package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.Dict;
/**
 * @author zhangxb
 * @date 2017-08-03
 */
public class FbsCorpItemVO implements java.io.Serializable{
	private static final long serialVersionUID = -4999274534241862110L;
	private String id;
	/**科目代码*/
	private java.lang.String itemCode;
	/**科目名称*/
	private String itemName;
	/**父科目*/
	//@Dict(tableName="fbs_item",valueFieldName="Item_Code",showFiledName="item_Name", inject = "superItemName")
	private String superItemCode;
	private String superItemName;
	/**科目层级*/
	private Integer levels;
	/**单位ID*/
	private java.lang.String corpId;
	/**有效标志 0 -无效 1 -有效*/
	private java.lang.String validSign;
	/**是否系统科目 */
	private String isSystem;
	private String superItemCodes;
	
	public static final int COLLECT_NEITHER = 0;
	public static final int COLLECT_SUBCORP = 1;
	public static final int COLLECT_BOTH = 2;
	public static final int COLLECT_SELF = 3;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getItemCode() {
		return itemCode;
	}
	public void setItemCode(java.lang.String itemCode) {
		this.itemCode = itemCode;
	}
	public java.lang.String getCorpId() {
		return corpId;
	}
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	public java.lang.String getValidSign() {
		return validSign;
	}
	public void setValidSign(java.lang.String validSign) {
		this.validSign = validSign;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSuperItemCode() {
		return superItemCode;
	}
	public void setSuperItemCode(String superItemCode) {
		this.superItemCode = superItemCode;
	}
	public String getSuperItemName() {
		return superItemName;
	}
	public void setSuperItemName(String superItemName) {
		this.superItemName = superItemName;
	}
	public Integer getLevels() {
		return levels;
	}
	public void setLevels(Integer levels) {
		this.levels = levels;
	}
	public String getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}
	public String getSuperItemCodes() {
		return superItemCodes;
	}
	public void setSuperItemCodes(String superItemCodes) {
		this.superItemCodes = superItemCodes;
	}
	
	
}
