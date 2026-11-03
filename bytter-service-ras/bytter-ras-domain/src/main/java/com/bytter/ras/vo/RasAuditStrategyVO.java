package com.bytter.ras.vo;

import com.bytter.ras.utils.RasUtils;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @Title: Entity
 * @Description: 稽核策略维护表VO
 * @author onlineGenerator
 * @date 2019-05-15 15:43:05
 * @version 1.0
 *
 */
public class RasAuditStrategyVO implements java.io.Serializable{
	private static final long serialVersionUID = -2619499051426138041L;

	private String id;
	/** 稽核规则名称 */
    @BtCompositeSearch(fieldName="name")
	private String name;
	/** 缴款方式 缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它 */
    @BtCompositeSearch(fieldName="pay_Type")
    @Dict(dictGroup = RasConstant.RAS_PAYTYPE_DICT, inject = "payTypeName")
    private String payType;
    @BtCompositeSearch(fieldName="pay_Type")
	private String payTypeName;
	/** 排序编号 */
    @BtCompositeSearch(fieldName="sort_No")
	private Integer sortNo;
	/** 单位ID */
	private String corpId;
	/** 有效标志：0，禁用 1，启用 */
    @Dict(dictGroup = RasConstant.RAS_VALID_SIGN_DICT, inject = "validSignName")
	private String validSign;
    @BtCompositeSearch(fieldName="valid_Sign")
	private String validSignName;
	/** 规则：多个以,隔开01订单号02交易日期03交易流水04门店名称05单位名称06现金汇总稽核07其他方式汇总稽核 */
	private String rules;
	private String rulesName;
	private List<RasRulesVO> rulesList=new ArrayList<>();
	/** 备注 */
	private String rmk;
	/** 创建人名称 */
	private String createName;
	/** 创建人登录名称 */
	private String createBy;
	/** 创建日期 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName="create_Date")
	private Date createDate;
	/** 创建人名称 */
	private String updateName;
	/** 创建人登录名称 */
	private String updateBy;
	/** 创建日期 */
	private Date updateDate;

	private String[] rulesArray;
	
    private Locale locale;

	public String[] getRulesArray() {
		return rulesArray;
	}

	public void setRulesArray(String[] rulesArray) {
		if(rulesArray!=null&&rulesArray.length>0){
			setRules(StringUtils.join(rulesArray, ","));
		}
		this.rulesArray = rulesArray;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Integer getSortNo() {
		return sortNo;
	}
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		RasUtils.setRulesList(rules,rulesList);
		this.rules = rules;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
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
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
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
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public String getValidSignName() {
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	
	public List<RasRulesVO> getRulesList() {
		return rulesList;
	}
	public void setRulesList(List<RasRulesVO> rulesList) {
		this.rulesList = rulesList;
	}
	public String getRulesName() {
		this.rulesName=RasUtils.getRulesName(this);
		return rulesName;
	}
	public void setRulesName(String rulesName) {
		this.rulesName = rulesName;
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public String toString() {
		return "RasAuditStrategyVO [id=" + id + ", name=" + name + ", payType=" + payType + ", payTypeName="
				+ payTypeName + ", sortNo=" + sortNo + ", corpId=" + corpId + ", validSign=" + validSign
				+ ", validSignName=" + validSignName + ", rules=" + rules + ", rmk=" + rmk + ", createName="
				+ createName + ", createBy=" + createBy + ", createDate=" + createDate + ", updateName=" + updateName
				+ ", updateBy=" + updateBy + ", updateDate=" + updateDate + ", getId()=" + getId() + ", getName()="
				+ getName() + ", getPayType()=" + getPayType() + ", getSortNo()=" + getSortNo() + ", getCorpId()="
				+ getCorpId() + ", getValidSign()=" + getValidSign() + ", getRules()=" + getRules() + ", getRmk()="
				+ getRmk() + ", getCreateName()=" + getCreateName() + ", getCreateBy()=" + getCreateBy()
				+ ", getCreateDate()=" + getCreateDate() + ", getUpdateName()=" + getUpdateName() + ", getUpdateBy()="
				+ getUpdateBy() + ", getUpdateDate()=" + getUpdateDate() + ", getPayTypeName()=" + getPayTypeName()
				+ ", getValidSignName()=" + getValidSignName() + "]";
	}
	
	
}
