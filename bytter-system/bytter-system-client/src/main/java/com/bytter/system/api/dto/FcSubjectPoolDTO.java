package com.bytter.system.api.dto;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 
 * @author zhengxn
 *
 */
@SuppressWarnings("serial")
public class FcSubjectPoolDTO implements Serializable{
	/**Id*/
	@Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
	private String id;
	/**科目代码*/
	private String subjectCode;
	/**科目名称*/
	private String subjectName;
	/**前端组件展示*/
	private String showName;
	/**余额方向*/
	private Integer balanceWay;
	/**计息标志*/
	private String interestSign;
	/**利率*/
	private BigDecimal interestRate;
	/**显示级次*/
	private Integer showLevel;
	/**科目级次*/
	private Integer levelNum;
	/**末级标志*/
	private String leafSign;
	/**开户种类*/
	private String accType;
	/**科目父ID*/
	private String fatherId;
	/**有效标志*/
	private String validSign;
	/**科目类别*/
	private String subjectSort;
	/**备注*/
	private String rmk;
	/**结息方式*/
	private String predrawSign;
	/**创建时间*/
	private Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public String getInterestSign() {
		return interestSign;
	}
	public void setInterestSign(String interestSign) {
		this.interestSign = interestSign;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public Integer getShowLevel() {
		return showLevel;
	}
	public void setShowLevel(Integer showLevel) {
		this.showLevel = showLevel;
	}
	public Integer getLevelNum() {
		return levelNum;
	}
	public void setLevelNum(Integer levelNum) {
		this.levelNum = levelNum;
	}
	public String getLeafSign() {
		return leafSign;
	}
	public void setLeafSign(String leafSign) {
		this.leafSign = leafSign;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getFatherId() {
		return fatherId;
	}
	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getSubjectSort() {
		return subjectSort;
	}
	public void setSubjectSort(String subjectSort) {
		this.subjectSort = subjectSort;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getPredrawSign() {
		return predrawSign;
	}
	public void setPredrawSign(String predrawSign) {
		this.predrawSign = predrawSign;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
