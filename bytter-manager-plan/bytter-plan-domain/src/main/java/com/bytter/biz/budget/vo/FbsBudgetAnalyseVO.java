package com.bytter.biz.budget.vo;
import com.bytter.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 计划上报VO
 * @author onlineGenerator
 * @date 2017-07-14 15:06:43
 * @version 1.0
 *
 */
public class FbsBudgetAnalyseVO implements Serializable{
	/**单位ID*/
	private String corpId;
	/**单位编码*/
	private String corpCode;
	/**单位名称*/
	private String corpName;
	/**查询时间*/
	private String queryDate;
	/**分析状态*/
	private Integer analyseStatus;
	/**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	private String timeType;
	/**计划ID*/
	private String id;
	/**客户名称*/
	private String oppAccName;
	/**差异开始金额*/
	private BigDecimal diffBeginMoney;
	/**差异结束金额*/
	private BigDecimal diffEndMoney;
	/**收支标志*/
	private String cdSign;
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getQueryDate() {
		return queryDate;
	}
	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}
	public Integer getAnalyseStatus() {
		return analyseStatus;
	}
	public void setAnalyseStatus(Integer analyseStatus) {
		this.analyseStatus = analyseStatus;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(String oppAccName) {
		this.oppAccName = oppAccName;
	}
	public BigDecimal getDiffBeginMoney() {
		return diffBeginMoney;
	}
	public void setDiffBeginMoney(BigDecimal diffBeginMoney) {
		this.diffBeginMoney = diffBeginMoney;
	}
	public BigDecimal getDiffEndMoney() {
		return diffEndMoney;
	}
	public void setDiffEndMoney(BigDecimal diffEndMoney) {
		this.diffEndMoney = diffEndMoney;
	}
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
}
