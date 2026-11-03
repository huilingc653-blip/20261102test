package com.bytter.subject.vo;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.TreeEntity;
import com.bytter.subject.entity.FcSubjectSort;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 会计科目
 * @author 吕伟林
 * @date 2017-05-12 14:18:39
 * @version V1.0
 *
 */
@SuppressWarnings("serial")
public class FcSubjectVo extends TreeEntity<FcSubjectVo> implements java.io.Serializable  {
	@Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
	private String id;
	/**科目代码*/
	private String subjectCode;
	/**科目名称*/
	private String subjectName;
	private String subjectNameShow;
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
	/**开户种类*/
	private String accType;
	/**末级标志*/
	private String leafSign;
	/**科目父ID*/
	private String fatherId;
	/**有效标志*/
	private String validSign;
	/**系统科目代码*/
	private String sysSubjectCode;
	/**系统科目实体*/
	private FcSubjectSort sysSubject;
	/**科目类别*/
	private String subjectSort;
	/**备注*/
	private String rmk;
	/**计量单位*/
	private String measureUnit;
	/**结息方式*/
	private String predrawSign;
	/**是否现金流科目*/
	private Integer isCashItem;
	/**借方现金流量项目*/
	private String debitCashItem;
	/**贷方现金流量项目*/
	private String creditCashItem;
	/**创建时间*/
	private Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	
	/**sortId*/
	private String sortId;
	
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
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getLeafSign() {
		return leafSign;
	}
	public void setLeafSign(String leafSign) {
		this.leafSign = leafSign;
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
	public String getSysSubjectCode() {
		return sysSubjectCode;
	}
	public void setSysSubjectCode(String sysSubjectCode) {
		this.sysSubjectCode = sysSubjectCode;
	}
	public FcSubjectSort getSysSubject() {
		return sysSubject;
	}
	public void setSysSubject(FcSubjectSort sysSubject) {
		this.sysSubject = sysSubject;
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
	public String getMeasureUnit() {
		return measureUnit;
	}
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	public String getPredrawSign() {
		return predrawSign;
	}
	public void setPredrawSign(String predrawSign) {
		this.predrawSign = predrawSign;
	}
	public Integer getIsCashItem() {
		return isCashItem;
	}
	public void setIsCashItem(Integer isCashItem) {
		this.isCashItem = isCashItem;
	}
	public String getDebitCashItem() {
		return debitCashItem;
	}
	public void setDebitCashItem(String debitCashItem) {
		this.debitCashItem = debitCashItem;
	}
	public String getCreditCashItem() {
		return creditCashItem;
	}
	public void setCreditCashItem(String creditCashItem) {
		this.creditCashItem = creditCashItem;
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
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	@Transient
	public String getParentId() {
		return this.fatherId;
	}
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	public String getSubjectNameShow() {
		return subjectNameShow;
	}
	public void setSubjectNameShow(String subjectNameShow) {
		this.subjectNameShow = subjectNameShow;
	}
}
