package com.bytter.biz.budget.spi.vo;
import java.math.BigDecimal;
import com.bytter.core.annotation.Dict;
/**   
 * @Title: Entity
 * @Description: 预算科目
 * @author onlineGenerator
 * @date 2017-07-06 16:40:26
 * @version 1.0
 * TODO private FbsItemVO item = new FbsItemVO(); 这种直接在VO里面new的对象，是否也会执行注解注入？
 */
public class FbsItemVO  implements  java.io.Serializable {
	//@Dict(tableName="FBS_ITEM",valueFieldName="id",showFiledName="item_name",inject="itemName")
	private String id;
	/**科目代码*/
	private String itemCode;
	/**科目名称*/
	private String itemName;
	/**父科目*/
//	@Dict(tableName="fbs_item",valueFieldName="Item_Code",showFiledName="item_Name", inject = "superItemName")
	private String superItemCode;
	private String superItemName;
	/**科目层级*/
	private Integer levels;
	/**资金流向1， 资金流入-1 ，资金流出*/
	//@Dict(dictGroup = "fbsItem.sign", inject = "signName") // TODO 数据字典国际化问题，待解决
	private Integer sign;
	private String signName;
	/**是否冲抵科目  1-是 0-否*/
	//@Dict(dictGroup = "sf_yn", inject = "isInternalName") // TODO 数据字典国际化问题，待解决
	private String isInternal;
	private String isInternalName;
	/**是否计划控制科目 1-是 0-否*/
	//@Dict(dictGroup = "sf_yn", inject = "isControlName")  // TODO 数据字典国际化问题，待解决
	private String isControl;
	private String isControlName;
	/**有效标志 0 无效 1 有效*/
	//@Dict(dictGroup = "valid_sign", inject = "validSignName")  // TODO 数据字典国际化问题，待解决
	private String validSign;
	private String validSignName;
	/**是否系统科目*/
	private String isSystem;
	private String superItemCodes;
	
	/**同级科目关系*/
	private String itemRelation;
	/**是否刚性科目 1-是 0-否*/
	//@Dict(dictGroup = "sf_yn", inject = "isRigidName")  // TODO 数据字典国际化问题，待解决
	private String isRigid;
	private String isRigidName;
	/**是否期初科目 1-是 0-否*/
	//@Dict(dictGroup = "sf_yn", inject = "isBeginPeriodName")  // TODO 数据字典国际化问题，待解决
	private String isBeginPeriod;
	private String isBeginPeriodName;
	/**是否期末科目 1-是 0-否*/
	//@Dict(dictGroup = "sf_yn", inject = "isEndPeriodName")  // TODO 数据字典国际化问题，待解决
	private String isEndPeriod;
	private String isEndPeriodName;
	/**超计划比例*/
	private BigDecimal overPerSpend;
	/**是否允许填写计划 1-是 0-否*/
	//@Dict(dictGroup = "sf_yn", inject = "isWriteName")  // TODO 数据字典国际化问题，待解决
	private String isWrite;
	private String isWriteName;
	private Boolean canSelect;
	private String  rmk;
	/**现金流科目代码*/
	private String cashItemCode;
	/**现金流科目名称*/
	private String cashItemName;
	/**调拨科目*/
	private String allotItemCode;
	/**科目显示方式*/
	private Integer showItem;
	/**科目层级*/
	private Integer itemLevel;
	/**科目明细是否必须录入 1-是 0-否*/
	//@Dict(dictGroup = "sf_yn", inject = "isWriteDetailName")  // TODO 数据字典国际化问题，待解决
	private String isWriteDetail;
	private String isWriteDetailName;
	public Boolean isCanSelect() {
		return canSelect;
	}
	public void setCanSelect(Boolean canSelect) {
		this.canSelect = canSelect;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getCashItemCode() {
		return cashItemCode;
	}
	public void setCashItemCode(String cashItemCode) {
		this.cashItemCode = cashItemCode;
	}
	public String getCashItemName() {
		return cashItemName;
	}
	public void setCashItemName(String cashItemName) {
		this.cashItemName = cashItemName;
	}
	public String getIsWrite() {
		return isWrite;
	}
	public void setIsWrite(String isWrite) {
		this.isWrite = isWrite;
	}
	public String getIsWriteName() {
		return isWriteName;
	}
	public void setIsWriteName(String isWriteName) {
		this.isWriteName = isWriteName;
	}
	public String getIsRigid() {
		return isRigid;
	}
	public void setIsRigid(String isRigid) {
		this.isRigid = isRigid;
	}
	public String getIsRigidName() {
		return isRigidName;
	}
	public void setIsRigidName(String isRigidName) {
		this.isRigidName = isRigidName;
	}
	public String getIsBeginPeriod() {
		return isBeginPeriod;
	}
	public void setIsBeginPeriod(String isBeginPeriod) {
		this.isBeginPeriod = isBeginPeriod;
	}
	public String getIsBeginPeriodName() {
		return isBeginPeriodName;
	}
	public void setIsBeginPeriodName(String isBeginPeriodName) {
		this.isBeginPeriodName = isBeginPeriodName;
	}
	public String getIsEndPeriod() {
		return isEndPeriod;
	}
	public void setIsEndPeriod(String isEndPeriod) {
		this.isEndPeriod = isEndPeriod;
	}
	public String getIsEndPeriodName() {
		return isEndPeriodName;
	}
	public void setIsEndPeriodName(String isEndPeriodName) {
		this.isEndPeriodName = isEndPeriodName;
	}
	public BigDecimal getOverPerSpend() {
		return overPerSpend;
	}
	public void setOverPerSpend(BigDecimal overPerSpend) {
		this.overPerSpend = overPerSpend;
	}
	public String getSuperItemCodes() {
		return superItemCodes;
	}
	public void setSuperItemCodes(String superItemCodes) {
		this.superItemCodes = superItemCodes;
	}
	public String getAllotItemCode() {
		return allotItemCode;
	}
	public void setAllotItemCode(String allotItemCode) {
		this.allotItemCode = allotItemCode;
	}
	public String getSuperItemName() {
		return superItemName;
	}
	public void setSuperItemName(String superItemName) {
		this.superItemName = superItemName;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getIsInternalName() {
		return isInternalName;
	}
	public void setIsInternalName(String isInternalName) {
		this.isInternalName = isInternalName;
	}
	public String getIsControlName() {
		return isControlName;
	}
	public void setIsControlName(String isControlName) {
		this.isControlName = isControlName;
	}
	public String getValidSignName() {
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	public String getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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
	public Integer getLevels() {
		return levels;
	}
	public void setLevels(Integer levels) {
		this.levels = levels;
	}
	public Integer getSign() {
		return sign;
	}
	public void setSign(Integer sign) {
		this.sign = sign;
	}
	public String getIsInternal() {
		return isInternal;
	}
	public void setIsInternal(String isInternal) {
		this.isInternal = isInternal;
	}
	public String getIsControl() {
		return isControl;
	}
	public void setIsControl(String isControl) {
		this.isControl = isControl;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getItemRelation() {
		return itemRelation;
	}
	public void setItemRelation(String itemRelation) {
		this.itemRelation = itemRelation;
	}
	public Integer getShowItem() {
		return showItem;
	}
	public void setShowItem(Integer showItem) {
		this.showItem = showItem;
	}
	public Integer getItemLevel() {
		return itemLevel;
	}
	public void setItemLevel(Integer itemLevel) {
		this.itemLevel = itemLevel;
	}
	public String getIsWriteDetail() {
		return isWriteDetail;
	}
	public void setIsWriteDetail(String isWriteDetail) {
		this.isWriteDetail = isWriteDetail;
	}
	public String getIsWriteDetailName() {
		return isWriteDetailName;
	}
	public void setIsWriteDetailName(String isWriteDetailName) {
		this.isWriteDetailName = isWriteDetailName;
	}
}
