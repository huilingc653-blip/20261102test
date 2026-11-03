package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * @Title: AbPoolSignInfoVO
 * @Description: 票据池明细信息VO
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
public class AbPoolDtlBookVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**流水Id*/
    private String id;
    /**单位Id*/
    @BtCompositeSearch(fieldName="pc.corp_id")
    private String corpId;
    
    /**单位代码*/
    private String corpCode;
    
    /**单位名称*/
    @BtCompositeSearch(fieldName="pc.corp_name")
    private String corpName;
    
    /**票据号码*/
    @BtCompositeSearch(fieldName="d.orig_No")
    private String origNo;
    
	/**业务类型*/
	@Dict(dictGroup="AB_POOL_BRIEF_ID",inject="briefIdName")
    @BtCompositeSearch(fieldName="d.brief_id")
	private String briefId;
    
	/**业务类型*/
    @BtCompositeSearch(fieldName="d.brief_id")
	private String briefIdName;
	/**票据类型：10 银票、11 商票*/
	@Dict(dictGroup="AB_BILL_TYPE",inject="billTypeName")
	private Integer billType;
	
    @BtCompositeSearch(fieldName="v.bill_Type")
	private String billTypeName;
	/**流通介质：10 纸票、11 电票*/
	@Dict(dictGroup="AB_BILL_FLAG",inject="billFlagName")
	private Integer billFlag;
	
    @BtCompositeSearch(fieldName="v.bill_Flag")
	private String billFlagName;
	/**出票日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName="v.start_Date")
	private Date startDate;
	
	/**到期日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName="v.end_Date")
	private Date endDate;
	
	/**票据金额*/
    @BtCompositeSearch(fieldName="v.bill_Money")
	private java.math.BigDecimal billMoney;
	/**出票人*/
    @BtCompositeSearch(fieldName="v.payee")
	private String payer;
	
	/**收票人全称*/
    @BtCompositeSearch(fieldName="v.payee")
	private String payee;
	
	/**承兑人*/
    @BtCompositeSearch(fieldName="v.acceptor")
	private String acceptor;
	/**交易对手*/
    @BtCompositeSearch(fieldName="d.rec_name")
	private String recName;
	
    
    /**票据池编号*/
    @BtCompositeSearch(fieldName="p.id")
    private String poolNo;
    
    /**票据池名称*/
    @BtCompositeSearch(fieldName="p.bp_name")
    private String poolName;
    
    /**
     * 创建日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName="d.create_Time")
    private Date createTime;
    
    /**
     * 同步日期
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName="d.update_Time")
    private Date updateTime;
	
	/**不得转让标记EM00可再转让EM01不得转让*/
	@Dict(dictGroup="AB_BANEM",inject="bankendrsmtmkName")
	private String bankendrsmtmk;
    @BtCompositeSearch(fieldName="d.bankendrsmtmk")
	private String bankendrsmtmkName;
	/** 逾期托收原因说明 */
    @BtCompositeSearch(fieldName="d.rsn")
	private String rsn;
	/**清算方式 SM00线上清算、SMO1线下清算*/
	@Dict(dictGroup = "AB_STTLMMK", inject ="sttlmmkName")
	private String sttlmmk;
	
    @BtCompositeSearch(fieldName="d.sttlmmk")
	private String sttlmmkName;
    /**
     * 出入池方式：1质押2托管
     */
    @Dict(dictGroup = "AB_POOL_INOUT_TYPE", inject = "inoutTypeName")
    private String inoutType;
    
    @BtCompositeSearch(fieldName="d.inout_Type")
    private String inoutTypeName;
	/**申请人账号*/
	private String payeracc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	public String getBillFlagName() {
		return billFlagName;
	}
	public void setBillFlagName(String billFlagName) {
		this.billFlagName = billFlagName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public java.math.BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(java.math.BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public String getPoolNo() {
		return poolNo;
	}
	public void setPoolNo(String poolNo) {
		this.poolNo = poolNo;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getBankendrsmtmk() {
		return bankendrsmtmk;
	}
	public void setBankendrsmtmk(String bankendrsmtmk) {
		this.bankendrsmtmk = bankendrsmtmk;
	}
	public String getBankendrsmtmkName() {
		return bankendrsmtmkName;
	}
	public void setBankendrsmtmkName(String bankendrsmtmkName) {
		this.bankendrsmtmkName = bankendrsmtmkName;
	}
	public String getRsn() {
		return rsn;
	}
	public void setRsn(String rsn) {
		this.rsn = rsn;
	}
	public String getSttlmmk() {
		return sttlmmk;
	}
	public void setSttlmmk(String sttlmmk) {
		this.sttlmmk = sttlmmk;
	}
	public String getSttlmmkName() {
		return sttlmmkName;
	}
	public void setSttlmmkName(String sttlmmkName) {
		this.sttlmmkName = sttlmmkName;
	}
	public String getBriefId() {
		return briefId;
	}
	public void setBriefId(String briefId) {
		this.briefId = briefId;
	}
	public String getRecName() {
		return recName;
	}
	public void setRecName(String recName) {
		this.recName = recName;
	}
	public String getBriefIdName() {
		return briefIdName;
	}
	public void setBriefIdName(String briefIdName) {
		this.briefIdName = briefIdName;
	}
	public String getInoutType() {
		return inoutType;
	}
	public void setInoutType(String inoutType) {
		this.inoutType = inoutType;
	}
	public String getInoutTypeName() {
		return inoutTypeName;
	}
	public void setInoutTypeName(String inoutTypeName) {
		this.inoutTypeName = inoutTypeName;
	}
	public String getPayeracc() {
		return payeracc;
	}
	public void setPayeracc(String payeracc) {
		this.payeracc = payeracc;
	}
	@Override
	public String toString() {
		return "AbPoolDtlBookVO [id=" + id + ", corpId=" + corpId + ", corpCode=" + corpCode + ", corpName=" + corpName
				+ ", origNo=" + origNo + ", briefId=" + briefId + ", briefIdName=" + briefIdName + ", billType="
				+ billType + ", billTypeName=" + billTypeName + ", billFlag=" + billFlag + ", billFlagName="
				+ billFlagName + ", startDate=" + startDate + ", endDate=" + endDate + ", billMoney=" + billMoney
				+ ", payer=" + payer + ", payee=" + payee + ", acceptor=" + acceptor + ", recName=" + recName
				+ ", poolNo=" + poolNo + ", poolName=" + poolName + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", bankendrsmtmk=" + bankendrsmtmk + ", bankendrsmtmkName=" + bankendrsmtmkName
				+ ", rsn=" + rsn + ", sttlmmk=" + sttlmmk + ", sttlmmkName=" + sttlmmkName + ", inoutType=" + inoutType
				+ ", inoutTypeName=" + inoutTypeName +",payeracc="+payeracc+ "]";
	}
    
}
