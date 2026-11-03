package com.bytter.bill.api.bill.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luop
 * 应付开票暂存-接口参数对象
 * 2017-08-26 16:30:56
 */
@SuppressWarnings("serial")
public class AbBillCollectionApplyVoDTO implements Serializable {
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**票据流水id*/
	private String aid;
	/**持票单位*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**票据号码*/
	private String origNo;
	/**票据类型：10 银票、11 商票*/
	private Integer billType;
	private String billTypeName;
	/**流通介质：10 纸票、11 电票*/
	private Integer billFlag;
	private String billFlagName;
	/**出票人*/
	private String payer;
	/**承兑人*/
	private String acceptor;
	/**票据金额*/
	private BigDecimal billMoney;
	/**出票日期*/
	private Date startDate;
	/**到期日期*/
	private Date endDate;
	/**所属库存*/
	private String enterStock;
	private String enterStockName;
	/**处理标记：10正在处理11处理完成*/
	private Integer boxStatus;
	/**业务处理状态：-2已删除；-1打回；0 暂存；1导入;11-94审批中；95 已完成*/
	/**持票单位账号*/
	private String bankAcc;
	/**业务处理状态*/
	private Integer status;
	private String statusName;
	/**业务归属日*/
	private Date businessDate;
	/**是否逾期*/
	private String isOverdue;
	private String isOverdueName;
	/**托收说明/备注*/
	private String rmk;
	/**逾期托收说明/备注*/
	private String isOverdueRmk;
	/**清算模式:SM00：线上清算，SM01：线下清算*/
	private String sttlmmk;
	/**创建人登录名称*/
	private String createBy;
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getVvid() {
		return vvid;
	}
	public void setVvid(String vvid) {
		this.vvid = vvid;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
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
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
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
	public String getEnterStock() {
		return enterStock;
	}
	public void setEnterStock(String enterStock) {
		this.enterStock = enterStock;
	}
	public String getEnterStockName() {
		return enterStockName;
	}
	public void setEnterStockName(String enterStockName) {
		this.enterStockName = enterStockName;
	}
	public Integer getBoxStatus() {
		return boxStatus;
	}
	public void setBoxStatus(Integer boxStatus) {
		this.boxStatus = boxStatus;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public Date getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}
	public String getIsOverdue() {
		return isOverdue;
	}
	public void setIsOverdue(String isOverdue) {
		this.isOverdue = isOverdue;
	}
	public String getIsOverdueName() {
		return isOverdueName;
	}
	public void setIsOverdueName(String isOverdueName) {
		this.isOverdueName = isOverdueName;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getIsOverdueRmk() {
		return isOverdueRmk;
	}
	public void setIsOverdueRmk(String isOverdueRmk) {
		this.isOverdueRmk = isOverdueRmk;
	}
	public String getSttlmmk() {
		return sttlmmk;
	}
	public void setSttlmmk(String sttlmmk) {
		this.sttlmmk = sttlmmk;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
}
