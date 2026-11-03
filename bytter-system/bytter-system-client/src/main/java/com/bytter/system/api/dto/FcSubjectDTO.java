package com.bytter.system.api.dto;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 *
 * @author lvwl
 *
 */
@SuppressWarnings("serial")
public class FcSubjectDTO implements Serializable {
	@Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
	private String id;
	private String name;
	private String pId;
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
	/**科目类别*/
	private String subjectSort;
	/**备注*/
	private String rmk;
	/**结息方式*/
	private String predrawSign;
	/**sortId*/
	@Dict(tableName="FC_SUBJECT_SORT",valueFieldName="id",showFiledName="SYS_SUBJECT_NAME",inject="subjectName")
	private String sortId;
    /**是否现金流科目*/
    private Integer isCashItem;
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
		this.name = subjectName;
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
		this.pId = fatherId;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
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
    public Integer getIsCashItem() {
        return isCashItem;
    }
    public void setIsCashItem(Integer isCashItem) {
        this.isCashItem = isCashItem;
    }
}
