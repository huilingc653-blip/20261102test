package com.bytter.biz.budget.spi.dto;
import java.io.Serializable;
/**
 * Title: DTO
 * Description: 计划科目DTO
 * @author zhangxb
 * @since 2017-01-11 18:05
 *
 */
public class FbsItemDTO implements Serializable {
    private String id;
    /**科目代码*/
    private String itemCode;
    /**科目名称*/
    private String itemName;
    /**父科目*/
    private String superItemCode;
    private String superItemName;
    /**科目层级*/
    private Integer levels;
    /**资金流向1， 资金流入-1 ，资金流出*/
    private Integer sign;
    private String signName;
    /**是否冲抵科目  1-是 0-否*/
    private String isInternal;
    private String isInternalName;
    /**是否计划控制科目 1-是 0-否*/
    private String isControl;
    private String isControlName;
    /**有效标志 0 无效 1 有效*/
    private String validSign;
    private String validSignName;
	/**是否系统科目*/
    private String isSystem;
    private String superItemCodes;
	/**是否允许填写计划 1-是 0-否*/
	private String isWrite;
    /**同级科目关系*/
    private String itemRelation;
    /**是否期初科目 1-是 0-否*/
    private String isBeginPeriod;
    /**是否期末科目 1-是 0-否*/
    private String isEndPeriod;
    /**科目明细是否必须录入 1-是 0-否*/
    private String isWriteDetail;
    private Boolean checked=false;
    public String getSuperItemCodes() {
        return superItemCodes;
    }
    public void setSuperItemCodes(String superItemCodes) {
        this.superItemCodes = superItemCodes;
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
	public String getIsWrite() {
		return isWrite;
	}
	public void setIsWrite(String isWrite) {
		this.isWrite = isWrite;
	}
    public String getItemRelation() {
        return itemRelation;
    }
    public void setItemRelation(String itemRelation) {
        this.itemRelation = itemRelation;
    }
    public String getIsBeginPeriod() {
        return isBeginPeriod;
    }
    public void setIsBeginPeriod(String isBeginPeriod) {
        this.isBeginPeriod = isBeginPeriod;
    }
    public String getIsEndPeriod() {
        return isEndPeriod;
    }
    public void setIsEndPeriod(String isEndPeriod) {
        this.isEndPeriod = isEndPeriod;
    }
    public String getIsWriteDetail() {
        return isWriteDetail;
    }
    public void setIsWriteDetail(String isWriteDetail) {
        this.isWriteDetail = isWriteDetail;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
