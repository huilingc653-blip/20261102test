package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>类功能：</b>账号利率调整bean<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月6日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class AccInstrateBean implements Serializable{
	private String account;
    private String instrtType;
    private Date instrateTime;//利息生效日
    private Date startDate;//计息日开始日
    private BigDecimal newInstrate;
    private BigDecimal oldInstrate;
    private String adjustId;//利率调整Id
    private String changeType = "1";//调整方式 3为余额区间
    private List<AccInstrateZoneBean> zoneBeans = new ArrayList<AccInstrateZoneBean>();

    public AccInstrateBean() {
    }

    public AccInstrateBean(String account, String instrtType, Date startDate, Date instrateTime, BigDecimal newInstrate,
                           BigDecimal oldInstrate) {
        this.account = account;
        this.instrtType = instrtType;
        this.startDate = startDate;
        this.instrateTime = instrateTime;
        this.newInstrate = newInstrate;
        this.oldInstrate = oldInstrate;
    }
    
    public AccInstrateBean(String account, String instrtType, Date startDate, Date instrateTime, BigDecimal newInstrate,
    		BigDecimal oldInstrate, String changeType) {
        this(account, instrtType, startDate, instrateTime, newInstrate, oldInstrate);
        this.changeType = changeType;
    }
    
    public AccInstrateBean(String account, String instrtType, Date startDate, Date instrateTime, BigDecimal newInstrate,
    		BigDecimal oldInstrate, String adjustId, String changeType) {
        this(account, instrtType, startDate, instrateTime, newInstrate, oldInstrate, changeType);
        this.adjustId = adjustId;
    }
    
    public AccInstrateBean(String account, String instrtType, Date instrateTime, Date startDate, BigDecimal newInstrate,
            BigDecimal oldInstrate, String changeType, List<AccInstrateZoneBean> zoneBeans) {
        this(account, instrtType, startDate, instrateTime, newInstrate, oldInstrate, changeType);
        this.zoneBeans = zoneBeans;
    }
    public AccInstrateBean(String account, String instrtType, Date instrateTime, Date startDate, BigDecimal newInstrate,
            BigDecimal oldInstrate, String adjustId, String changeType, List<AccInstrateZoneBean> zoneBeans) {
        this(account, instrtType, startDate, instrateTime, newInstrate, oldInstrate, changeType);
        this.adjustId = adjustId;
        this.zoneBeans = zoneBeans;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getInstrtType() {
        return instrtType;
    }
    public void setInstrtType(String instrtType) {
        this.instrtType = instrtType;
    }
    public Date getInstrateTime() {
        return instrateTime;
    }
    public void setInstrateTime(Date instrateTime) {
        this.instrateTime = instrateTime;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public BigDecimal getNewInstrate() {
        return newInstrate;
    }
    public void setNewInstrate(BigDecimal newInstrate) {
        this.newInstrate = newInstrate;
    }
    public BigDecimal getOldInstrate() {
        return oldInstrate;
    }
    public void setOldInstrate(BigDecimal oldInstrate) {
        this.oldInstrate = oldInstrate;
    }
    
    public String getChangeType() {
        return changeType;
    }
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
    public List<AccInstrateZoneBean> getZoneBeans() {
        return zoneBeans;
    }
    public void setZoneBeans(List<AccInstrateZoneBean> zoneBeans) {
        this.zoneBeans = zoneBeans;
    }
    public String getAdjustId() {
        return adjustId;
    }
    public void setAdjustId(String adjustId) {
        this.adjustId = adjustId;
    }
    @Override
    public String toString() {
        return "AccInstrateBean [account=" + account + ", instrtType=" + instrtType + ", instrateTime=" + instrateTime
                + ", startDate=" + startDate + ", newInstrate=" + newInstrate + ", oldInstrate=" + oldInstrate + "]";
    }
}
