package com.bytter.biz.fundwarn.vo;
import java.io.Serializable;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
/**
 * @Description: 预警策略条件VO
 * @author zhenghj
 * @date 2018-09-27
 * @version 1.0
 */
@ExcelTarget("BtWarnConditionVO")
public class BtWarnConditionVO extends ExcelModel implements Serializable,  IBusinessKey {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/** 预警策略表ID*/
	private String warnId;
	/** 数据类型（corp单位、bankAcc银行账户、fcAcc内部账户、bankAccAttribute银行账号属性、
	 * bankAccType银行账号类别、bankAccNature银行账号性质、fcAccNature内部账户性质、
	 * accSubject会计科目）*/
	private String dataType;
	/** 数据值，选择为全部时值为null*/
	private String dataValue;
	/**单位Id、银行账号、内部账户列表选择*/
	private String corpIdAndAccStr;
	/**业务属性选择：银行账号属性*/
	private String bankAttStr;
	/**业务属性选择：银行账号类别*/
	private String bankAccTypeStr;
	/**业务属性选择：银行账号性质*/
	private String bankAccOnlineStr;
	/**业务属性选择：内部账号性质*/
	private String bankAccNatureStr;
	/**业务属性选择：科目*/
	private String subjectNameStr;

	/**逾期账款比例大于*/
	private String warnConditionRate;
	/**客商信用等级*/
	private String ratingSelection;
	/**已逾期天数达到X天在当天预警*/
	private String dateNum;
	/**计划执行周期类型*/
	private String timeType;

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getWarnConditionRate() {
		return warnConditionRate;
	}

	public void setWarnConditionRate(String warnConditionRate) {
		this.warnConditionRate = warnConditionRate;
	}

	public String getRatingSelection() {
		return ratingSelection;
	}

	public void setRatingSelection(String ratingSelection) {
		this.ratingSelection = ratingSelection;
	}

	public String getDateNum() {
		return dateNum;
	}

	public void setDateNum(String dateNum) {
		this.dateNum = dateNum;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWarnId() {
		return warnId;
	}
	public void setWarnId(String warnId) {
		this.warnId = warnId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public String getCorpIdAndAccStr() {
		return corpIdAndAccStr;
	}
	public void setCorpIdAndAccStr(String corpIdAndAccStr) {
		this.corpIdAndAccStr = corpIdAndAccStr;
	}
	public String getBankAttStr() {
		return bankAttStr;
	}
	public void setBankAttStr(String bankAttStr) {
		this.bankAttStr = bankAttStr;
	}
	public String getBankAccTypeStr() {
		return bankAccTypeStr;
	}
	public void setBankAccTypeStr(String bankAccTypeStr) {
		this.bankAccTypeStr = bankAccTypeStr;
	}
	public String getBankAccOnlineStr() {
		return bankAccOnlineStr;
	}
	public void setBankAccOnlineStr(String bankAccOnlineStr) {
		this.bankAccOnlineStr = bankAccOnlineStr;
	}
	public String getBankAccNatureStr() {
		return bankAccNatureStr;
	}
	public void setBankAccNatureStr(String bankAccNatureStr) {
		this.bankAccNatureStr = bankAccNatureStr;
	}
	public String getSubjectNameStr() {
		return subjectNameStr;
	}
	public void setSubjectNameStr(String subjectNameStr) {
		this.subjectNameStr = subjectNameStr;
	}
	@Override
	public String getBusinessKey() {
		return getId();
	}
	@Override
	public String getBeanName() {
		return "btWarnStrategyService";
	}
	@Override
	public String toString() {
		return "BtWarnConditionVO{" +
				"id='" + id + '\'' +
				", warnId='" + warnId + '\'' +
				", dataType='" + dataType + '\'' +
				", dataValue='" + dataValue + '\'' +
				", corpIdAndAccStr='" + corpIdAndAccStr + '\'' +
				", bankAttStr='" + bankAttStr + '\'' +
				", bankAccTypeStr='" + bankAccTypeStr + '\'' +
				", bankAccOnlineStr='" + bankAccOnlineStr + '\'' +
				", bankAccNatureStr='" + bankAccNatureStr + '\'' +
				", subjectNameStr='" + subjectNameStr + '\'' +
				'}';
	}
}
