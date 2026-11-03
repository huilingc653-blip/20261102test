package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 计划执行报表VO
 * @date 2018-03-13 15:06:43
 */
public class FbsBudgetSuperaddViewVO implements Serializable {
	/**
	 * 单位ID
	 */
	//@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "s.corp_id")
	private String corpId;
	private String corpName;
	/**
	 * 科目代码
	 */
	@BtCompositeSearch(fieldName = "sd.item_code")
	private String itemCode;
	/**
	 * 科目名称
	 */
	private String itemName;
	/**
	 * 追加金额
	 */
	@BtCompositeSearch(fieldName = "sd.bsuper_money")
	private BigDecimal bsuperMoney;
	/**状态*/
	@BtCompositeSearch(fieldName = "s.status")
	private Integer status;
	/**
	 * 创建日期
	 */
	@BtCompositeSearch(fieldName = "s.create_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	/**
	 * 创建日期
	 */
	private String curName;
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
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
	public BigDecimal getBsuperMoney() {
		return bsuperMoney;
	}
	public void setBsuperMoney(BigDecimal bsuperMoney) {
		this.bsuperMoney = bsuperMoney;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
}
