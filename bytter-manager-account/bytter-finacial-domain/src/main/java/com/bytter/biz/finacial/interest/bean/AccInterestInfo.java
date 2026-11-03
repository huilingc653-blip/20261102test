package com.bytter.biz.finacial.interest.bean;

import com.bytter.biz.finacial.api.enums.interest.InterestProcessType;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.entity.FcLessPayBill;
import com.bytter.biz.finacial.interest.entity.FcPredrawDtl;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookVO;
import com.bytter.biz.finacial.interest.vo.FcPredrawDtlTempVO;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AccInterestInfo implements Serializable {
	/**
     * 账户类型
     */
    private AccTypeKeyBean accTypeKeyBean;
    /**
     * 积数信息
     */
    private Map<InterestProcessType, Map<String, List<FcAccuBalancef>>> interestAccuBalances;
    /**
     * 利息信息
     */
    private List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
    /**
     * 预提信息
     */
    private List<FcPredrawDtlTempVO> predrawDtls = new ArrayList<FcPredrawDtlTempVO>();
    /**
     * 计息凭证
     */
    private List<FcFinacialAccbookVO> interestAccbook;
    /**
     * 预提凭证
     */
    private List<FcFinacialAccbookVO> predrawAccbook;
    private InterestInfoContainer container;
    /**
     * 不足款清单
     */
    private List<FcLessPayBill> lessPayBills = new ArrayList<FcLessPayBill>();
    
    /**
     * 待冲销预提信息
     */
    private List<FcPredrawDtl> fcPredrawDtls = new ArrayList<FcPredrawDtl>();
    
    /**
     * 冲预提凭证
     */
    private List<FcFinacialAccbookVO> rushAccbook;
    
    /**
     * 冲销明细
     */
    //private List<IstPredrawRushDtl> predrawRushDtls = new ArrayList<IstPredrawRushDtl>();
    
    /**
     * 是否需要保存利息信息，默认 true
     */
    private boolean isNeedSave = true;
    
    /**
     * 是否自动冲预提
     */
    private boolean isAutoRush = false;
    
    /**
     * 计息日期
     */
    private Date interestTime;
    /**
     * 资金池计息标识
     */
    private boolean poolSign;
    public boolean isPoolSign() {
        return poolSign;
    }
    public void setPoolSign(boolean poolSign) {
        this.poolSign = poolSign;
    }
	public AccTypeKeyBean getAccTypeKeyBean() {
		return accTypeKeyBean;
	}
	public void setAccTypeKeyBean(AccTypeKeyBean accTypeKeyBean) {
		this.accTypeKeyBean = accTypeKeyBean;
	}
	public Map<InterestProcessType, Map<String, List<FcAccuBalancef>>> getInterestAccuBalances() {
		return interestAccuBalances;
	}
	public void setInterestAccuBalances(Map<InterestProcessType, Map<String, List<FcAccuBalancef>>> interestAccuBalances) {
		this.interestAccuBalances = interestAccuBalances;
	}
	public List<FcInterestDtlTemp> getInterestDtls() {
		return interestDtls;
	}
	public void setInterestDtls(List<FcInterestDtlTemp> interestDtls) {
		this.interestDtls = interestDtls;
	}
	public List<FcPredrawDtlTempVO> getPredrawDtls() {
		return predrawDtls;
	}
	public void setPredrawDtls(List<FcPredrawDtlTempVO> predrawDtls) {
		this.predrawDtls = predrawDtls;
	}
	public List<FcFinacialAccbookVO> getInterestAccbook() {
		return interestAccbook;
	}
	public void setInterestAccbook(List<FcFinacialAccbookVO> interestAccbook) {
		this.interestAccbook = interestAccbook;
	}
	public List<FcFinacialAccbookVO> getPredrawAccbook() {
		return predrawAccbook;
	}
	public void setPredrawAccbook(List<FcFinacialAccbookVO> predrawAccbook) {
		this.predrawAccbook = predrawAccbook;
	}
	public InterestInfoContainer getContainer() {
		return container;
	}
	public void setContainer(InterestInfoContainer container) {
		this.container = container;
	}
	public List<FcFinacialAccbookVO> getRushAccbook() {
		return rushAccbook;
	}
	public void setRushAccbook(List<FcFinacialAccbookVO> rushAccbook) {
		this.rushAccbook = rushAccbook;
	}
	public boolean isNeedSave() {
		return isNeedSave;
	}
	public void setNeedSave(boolean isNeedSave) {
		this.isNeedSave = isNeedSave;
	}
	public boolean isAutoRush() {
		return isAutoRush;
	}
	public void setAutoRush(boolean isAutoRush) {
		this.isAutoRush = isAutoRush;
	}
	public Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}
	public List<FcLessPayBill> getLessPayBills() {
		return lessPayBills;
	}
	public void setLessPayBills(List<FcLessPayBill> lessPayBills) {
		this.lessPayBills = lessPayBills;
	}
	public List<FcPredrawDtl> getFcPredrawDtls() {
		return fcPredrawDtls;
	}
	public void setFcPredrawDtls(List<FcPredrawDtl> fcPredrawDtls) {
		this.fcPredrawDtls = fcPredrawDtls;
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
