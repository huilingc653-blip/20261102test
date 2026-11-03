package com.bytter.biz.finacial.interest.vo;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.biz.finacial.interest.bean.AccInterestBean;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import com.bytter.biz.finacial.interest.bean.AccTypeKeyBean;
import com.bytter.biz.finacial.interest.bean.InstrateAdjustBean;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class InterestInfoContainer implements Serializable{
	/**
     * 起息日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
	/**
	 * 产生方式 I-个别计息 P-批量计息
	 * */
	private String generateMode=InterestConstant.GENERATEMODE_INTEREST_BATCH;
    /**
     * 止息日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date endInstDate;
    /**
     * 记账日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate;
    /**
     * 网点
     */
    private String netCode;
    /**
     * 开户种类
     */
    private String[] accTypeCodes;
    /**
     * 当前用户
     */
    private ShiroUser userBean;
    /**
     * 当前用户SessionId
     */
    private String sessionId;
    /**
     * 当前操作类型
     */
    private OperateType opType;
    /**
     * 归还类型
     */
    private String repayType;
    /**
     * 个别冲利息金额
     */
    private BigDecimal rushMoney;
    /**
     * 预提冲预提方式
     */
    private String sysPredrawRushWay;
    /**
     * 批量结息方式
     */
    private String sysBatchInterestWay;
    /**
     * 是否系统计息日
     */
    private boolean isSysInstDay;
    /**
     * 是否季末
     */
    private boolean isSeasonEnd;
    /**
     * 是否月末最后一个工作日
     */
    private boolean isLastWorkDay;
    /**
     * 定期计息方式
     */
    private String sysFixInterestWay;
    /**
     * 是否罚息
     */
    private boolean isInstPunish;
    /**
     * 委托贷款业务核算模式
     */
    private String instConsignModel;
    /**
     * 系统利率类型
     */
    private BigDecimal sysRateType = new BigDecimal(InterestConstant.DAY_OF_YEAR);
    
    /**
     * 积数信息
     */
    private List<FcAccuBalancef> accuBalances;
    
    /**
     * 账户利率调整信息 key=account,value=InstrateAdjustBean
     */
    private Map<String, InstrateAdjustBean> instrateAdjustInfos;
    /**
     * 账户利息原信息
     */
    private Map<AccTypeKeyBean, AccInterestInfo> accTypeInterestInfos;
    /**
     * 个别计息/冲销的账号
     */
    private String account;
    /**
     * 是否更新原预提信息状态
     */
    private boolean isUpdateOriPredraw = true;
    /**
     * 取款需冲销预提的主键ID
     */
    private String depositManagerId;
    
    /**
     * 业务类型
     */
    private String sysCode;
    
    /**
     * 业务主键
     */
    private String billCode;
    /**
     * 是否发送指令
     */
    private boolean isInstBisExc;
    
    /**
     * 利息收入\利息支出账号是否挂中心
     */
    private boolean isAccInCenter;
    
    /**
     * 计息凭证类型是否多条分录
     */
    private boolean isMoreVoucherDtl;
    
    /**
     * 计息日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestTime;
    
    /**
     * 计息日期(带时分秒)
     */
    private Timestamp interestDateTime;
    
    /**
     * 通知存款取款是否提前预约
     */
    private boolean isNotifyDraw;
	
	/**
     * 定期月份天数是否按实际天数计算
     */
    private boolean fixedActrueDays;
    
    /**
     * 单位Ids
     */
    private Integer[] corpIds;
    
    /**
     * 取款金额
     */
    private BigDecimal tellerMoney;
    
    /**
     * 是否可以冲预提，默认可以
     */
    private boolean isBatchRushPre = true;
    
    /**
     * 计息账号集
     */
    private Map<String,FcAccDTO> interestAcc;
	/**
	 * 计息账号集
	 */
	private boolean poolSign = false;
	/**预提总利息*/
	private BigDecimal sumRushMoney;
    public boolean isPoolSign() {
        return poolSign;
    }
    public void setPoolSign(boolean poolSign) {
        this.poolSign = poolSign;
    }
    public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndInstDate() {
		return endInstDate;
	}
	public void setEndInstDate(Date endInstDate) {
		this.endInstDate = endInstDate;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public String[] getAccTypeCodes() {
		return accTypeCodes;
	}
	public void setAccTypeCodes(String[] accTypeCodes) {
		this.accTypeCodes = accTypeCodes;
	}
	public ShiroUser getUserBean() {
		return userBean;
	}

	public void setUserBean(ShiroUser userBean) {
		this.userBean = userBean;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public OperateType getOpType() {
		return opType;
	}
	public void setOpType(OperateType opType) {
		this.opType = opType;
	}
	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}
	public BigDecimal getRushMoney() {
		return rushMoney;
	}
	public void setRushMoney(BigDecimal rushMoney) {
		this.rushMoney = rushMoney;
	}
	public String getSysPredrawRushWay() {
		return sysPredrawRushWay;
	}
	public void setSysPredrawRushWay(String sysPredrawRushWay) {
		this.sysPredrawRushWay = sysPredrawRushWay;
	}
	public String getSysBatchInterestWay() {
		return sysBatchInterestWay;
	}
	public void setSysBatchInterestWay(String sysBatchInterestWay) {
		this.sysBatchInterestWay = sysBatchInterestWay;
	}
	public boolean isSysInstDay() {
		return isSysInstDay;
	}
	public void setSysInstDay(boolean isSysInstDay) {
		this.isSysInstDay = isSysInstDay;
	}
	public boolean isSeasonEnd() {
		return isSeasonEnd;
	}
	public void setSeasonEnd(boolean isSeasonEnd) {
		this.isSeasonEnd = isSeasonEnd;
	}
	public boolean isLastWorkDay() {
		return isLastWorkDay;
	}
	public void setLastWorkDay(boolean isLastWorkDay) {
		this.isLastWorkDay = isLastWorkDay;
	}
	public String getSysFixInterestWay() {
		return sysFixInterestWay;
	}
	public void setSysFixInterestWay(String sysFixInterestWay) {
		this.sysFixInterestWay = sysFixInterestWay;
	}
	public boolean isInstPunish() {
		return isInstPunish;
	}
	public void setInstPunish(boolean isInstPunish) {
		this.isInstPunish = isInstPunish;
	}
	public String getInstConsignModel() {
		return instConsignModel;
	}
	public void setInstConsignModel(String instConsignModel) {
		this.instConsignModel = instConsignModel;
	}
	public BigDecimal getSysRateType() {
		return sysRateType;
	}
	public void setSysRateType(BigDecimal sysRateType) {
		this.sysRateType = sysRateType;
	}
	
	public List<FcAccuBalancef> getAccuBalances() {
		return accuBalances;
	}
	public void setAccuBalances(List<FcAccuBalancef> accuBalances) {
		this.accuBalances = accuBalances;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public boolean isUpdateOriPredraw() {
		return isUpdateOriPredraw;
	}
	public void setUpdateOriPredraw(boolean isUpdateOriPredraw) {
		this.isUpdateOriPredraw = isUpdateOriPredraw;
	}
	public String getDepositManagerId() {
		return depositManagerId;
	}
	public void setDepositManagerId(String depositManagerId) {
		this.depositManagerId = depositManagerId;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public boolean isInstBisExc() {
		return isInstBisExc;
	}
	public void setInstBisExc(boolean isInstBisExc) {
		this.isInstBisExc = isInstBisExc;
	}
	public boolean isAccInCenter() {
		return isAccInCenter;
	}
	public void setAccInCenter(boolean isAccInCenter) {
		this.isAccInCenter = isAccInCenter;
	}
	public boolean isMoreVoucherDtl() {
		return isMoreVoucherDtl;
	}
	public void setMoreVoucherDtl(boolean isMoreVoucherDtl) {
		this.isMoreVoucherDtl = isMoreVoucherDtl;
	}
	public Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}
	public Timestamp getInterestDateTime() {
		return interestDateTime;
	}
	public void setInterestDateTime(Timestamp interestDateTime) {
		this.interestDateTime = interestDateTime;
	}
	public boolean isNotifyDraw() {
		return isNotifyDraw;
	}
	public void setNotifyDraw(boolean isNotifyDraw) {
		this.isNotifyDraw = isNotifyDraw;
	}
	public boolean isFixedActrueDays() {
		return fixedActrueDays;
	}
	public void setFixedActrueDays(boolean fixedActrueDays) {
		this.fixedActrueDays = fixedActrueDays;
	}
	public Integer[] getCorpIds() {
		return corpIds;
	}
	public void setCorpIds(Integer[] corpIds) {
		this.corpIds = corpIds;
	}
	public BigDecimal getTellerMoney() {
		return tellerMoney;
	}
	public void setTellerMoney(BigDecimal tellerMoney) {
		this.tellerMoney = tellerMoney;
	}
	public boolean isBatchRushPre() {
		return isBatchRushPre;
	}
	public void setBatchRushPre(boolean isBatchRushPre) {
		this.isBatchRushPre = isBatchRushPre;
	}
	public Map<AccTypeKeyBean, AccInterestInfo> getAccTypeInterestInfos() {
		return accTypeInterestInfos;
	}
	public void setAccTypeInterestInfos(Map<AccTypeKeyBean, AccInterestInfo> accTypeInterestInfos) {
		this.accTypeInterestInfos = accTypeInterestInfos;
	}
	public Map<String, InstrateAdjustBean> getInstrateAdjustInfos() {
		return instrateAdjustInfos;
	}
	public void setInstrateAdjustInfos(Map<String, InstrateAdjustBean> instrateAdjustInfos) {
		this.instrateAdjustInfos = instrateAdjustInfos;
	}
	public Map<String, FcAccDTO> getInterestAcc() {
		return interestAcc;
	}
	public void setInterestAcc(Map<String, FcAccDTO> interestAcc) {
		this.interestAcc = interestAcc;
	}
	public BigDecimal getSumRushMoney() {
		if(sumRushMoney == null){
			return BigDecimal.ZERO;
		}
		return sumRushMoney;
	}
	public void setSumRushMoney(BigDecimal sumRushMoney) {
		this.sumRushMoney = sumRushMoney;
	}
	public String getGenerateMode() {
		return generateMode;
	}
	public void setGenerateMode(String generateMode) {
		this.generateMode = generateMode;
	}
	//账户计息Bean
    private AccInterestBean accBean;
    public AccInterestBean getAccBean() {
        return accBean;
    }
    public void setAccBean(AccInterestBean accBean) {
        this.accBean = accBean;
    }
}
