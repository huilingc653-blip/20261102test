package com.bytter.biz.finacial.interest.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: FC_INTEREST_SINGLE
 * @author onlineGenerator
 * @date 2020-12-02 14:34:54
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_INTEREST_SINGLE", schema = "")
public class FcInterestSingle extends IdEntity implements java.io.Serializable {
	/**单位ID*/
	private String corpId;
	/**计息日期*/
	private Date interestTime;
	/**账号*/
	private String account;
	/**账户类别*/
	private String accType;
	/**起息日期*/
	private Date startDate;
	/**止息日期*/
	private Date endDate;
	/**个别计息的还款金额*/
	private BigDecimal money;
	/**利息*/
	private BigDecimal interest;
	/**记账日期*/
	private Date tallyDate;
	/**流水号*/
	private String orderNo;
    /**
     * 业务全局id
     */
    protected String busGlobalId;
	/**还款方式*/
	private String payWay;
	/**业务状态*/
	private Integer status;
	/**附件张数*/
	private Integer affixAmount;
	/**创建用户代码*/
	private String createBy;
	/**复核人代码*/
	private String checkerCode;
	/**复核日期*/
	private Date checkDate;
	/**创建时间*/
	private Date createDate;
	/**最后更新用户代码*/
	private String updateBy;
	/**最后更新时间*/
	private Date updateDate;
	/**
	 * 新增冗余字段 CUR_ID  NET_ID   POOL_SIGN   ACC_NAME
	 * @return
	 */
	private String curId;
	private String netId;
	private String poolSign;
	private String accName;
	@Column(name = "CUR_ID")
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name = "NET_ID")
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	@Column(name = "POOL_SIGN")
	public String getPoolSign() {
		return poolSign;
	}
	public void setPoolSign(String poolSign) {
		this.poolSign = poolSign;
	}
	@Column(name = "ACC_NAME")
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Column(name ="CORP_ID")
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name ="INTEREST_TIME")
	public Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}
	@Column(name ="ACCOUNT")
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	@Column(name ="ACC_TYPE")
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	@Column(name ="START_DATE")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column(name ="END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Column(name ="MONEY")
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	@Column(name ="INTEREST")
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	@Column(name ="TALLY_DATE")
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	@Column(name = "ORDER_NO")
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
    @Column(name = "BUS_GLOBAL_ID")
    public String getBusGlobalId() {
        return busGlobalId;
    }
    public void setBusGlobalId(String busGlobalId) {
        this.busGlobalId = busGlobalId;
    }
    @Column(name = "PAY_WAY")
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	@Column(name ="STATUS")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "AFFIX_AMOUNT")
	public Integer getAffixAmount() {
		return affixAmount;
	}
	public void setAffixAmount(Integer affixAmount) {
		this.affixAmount = affixAmount;
	}
	@Column(name ="CREATE_BY")
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name ="CHECKER_CODE")
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	@Column(name ="CHECK_DATE")
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	@Column(name ="CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name ="UPDATE_BY")
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	@Column(name ="UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
