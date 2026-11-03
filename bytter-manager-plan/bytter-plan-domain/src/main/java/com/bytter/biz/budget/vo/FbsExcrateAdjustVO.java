package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 资金计划主表VO
 * @author onlineGenerator
 * @date 2017-07-17 15:20:33
 * @version 1.0
 *
 */
public class FbsExcrateAdjustVO implements Serializable {
	private String id;
	/**原始货币*/
	@BtCompositeSearch(fieldName = "f.cur_Id")
	private java.lang.String curId;
	/**原始货币编码*/
	private java.lang.String curCode;
	/**原始货币英文简码*/
	private java.lang.String englishCode;
	/**原始货币名称*/
	private java.lang.String curName;
	/**目标货币*/
	private java.lang.String referCurId;
	/**目标货币编码*/
	private java.lang.String referCurCode;
	/**目标货币英文简码*/
	private java.lang.String referEnglishCode;
	/**目标货币名称*/
	private java.lang.String referCurName;
	/**计划汇率*/
	private java.math.BigDecimal exchangeRate;
	/**调整时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date preTime;
	/**示例说明*/
	private java.lang.String example;
	/**备注*/
	private java.lang.String rmk;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getEnglishCode() {
		return englishCode;
	}
	public void setEnglishCode(String englishCode) {
		this.englishCode = englishCode;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getReferCurId() {
		return referCurId;
	}
	public void setReferCurId(String referCurId) {
		this.referCurId = referCurId;
	}
	public String getReferCurCode() {
		return referCurCode;
	}
	public void setReferCurCode(String referCurCode) {
		this.referCurCode = referCurCode;
	}
	public String getReferEnglishCode() {
		return referEnglishCode;
	}
	public void setReferEnglishCode(String referEnglishCode) {
		this.referEnglishCode = referEnglishCode;
	}
	public String getReferCurName() {
		return referCurName;
	}
	public void setReferCurName(String referCurName) {
		this.referCurName = referCurName;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Date getPreTime() {
		return preTime;
	}
	public void setPreTime(Date preTime) {
		this.preTime = preTime;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
