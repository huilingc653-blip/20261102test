package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <br>
 * <b>类功能：</b>余额区间Bean<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月6日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class AccInstrateZoneBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
     * 账号
     */
    private String account;
    
    /**
     * 执行日期
     */
    private Date adjustTime;
    
    /**
     * 上限操作符 
     */
    private String downOp;
    
    /**
     * 区间下限
     */
    private BigDecimal downLimit;
    
    /**
     * 下限操作符 
     */
    private String upOp;
    
    /**
     * 区间上限
     */
    private BigDecimal upLimit;
    
    /**
     * 新利率
     */
    private BigDecimal newInstrate;
    
    /**
     * 利率调整主键
     */
    private Long adjustId;
    
    public AccInstrateZoneBean(){
        
    }
    public AccInstrateZoneBean(String account, Date adjustTime, BigDecimal downLimit, BigDecimal upLimit,
    		BigDecimal newInstrate, Long adjustId) {
        this.account = account;
        this.adjustTime = adjustTime;
        this.downLimit = downLimit;
        this.upLimit = upLimit;
        this.newInstrate = newInstrate;
        this.adjustId = adjustId;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Date getAdjustTime() {
        return adjustTime;
    }
    public void setAdjustTime(Date adjustTime) {
        this.adjustTime = adjustTime;
    }
    public String getDownOp() {
        return downOp;
    }
    public void setDownOp(String downOp) {
        this.downOp = downOp;
    }
    public BigDecimal getDownLimit() {
        return downLimit;
    }
    public void setDownLimit(BigDecimal downLimit) {
        this.downLimit = downLimit;
    }
    public String getUpOp() {
        return upOp;
    }
    public void setUpOp(String upOp) {
        this.upOp = upOp;
    }
    public BigDecimal getUpLimit() {
        return upLimit;
    }
    public void setUpLimit(BigDecimal upLimit) {
        this.upLimit = upLimit;
    }
    public BigDecimal getNewInstrate() {
        return newInstrate;
    }
    public void setNewInstrate(BigDecimal newInstrate) {
        this.newInstrate = newInstrate;
    }
    public Long getAdjustId() {
        return adjustId;
    }
    public void setAdjustId(Long adjustId) {
        this.adjustId = adjustId;
    }
}
