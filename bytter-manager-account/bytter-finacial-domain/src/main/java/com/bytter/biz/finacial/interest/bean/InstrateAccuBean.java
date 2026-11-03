package com.bytter.biz.finacial.interest.bean;
import com.bytter.system.api.dto.FcAccDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class InstrateAccuBean implements Serializable {
	private String account;
    private FcAccDTO fcAcc;
    private Date startDate;
    private Date instrateTime;
    private BigDecimal instrate;
    private BigDecimal aCumu;
    private Date balanceDate;
    
    
    private String changeType = "1";//利率调整类型
    private List<AccInstrateZoneBean> zoneBeans = new ArrayList<AccInstrateZoneBean>();
    public InstrateAccuBean(String account, Date startDate, Date instrateTime, BigDecimal instrate, BigDecimal aCumu) {
        this.account = account;
        this.startDate = startDate;
        this.instrateTime = instrateTime;
        this.instrate = instrate;
        this.aCumu = aCumu;
    }
    public InstrateAccuBean(String account, FcAccDTO fcAcc, Date startDate, Date instrateTime, BigDecimal instrate,
            BigDecimal aCumu) {
        this(account, startDate, instrateTime, instrate, aCumu);
        this.fcAcc = fcAcc;
    }
    
    public InstrateAccuBean(String account, FcAccDTO fcAcc, Date startDate, Date instrateTime, BigDecimal instrate,
            BigDecimal aCumu, Date balanceDate) {
        this(account, fcAcc, startDate, instrateTime, instrate, aCumu);
        this.balanceDate = balanceDate;
    }
    
    public InstrateAccuBean(String account, FcAccDTO fcAcc, Date startDate, Date instrateTime, BigDecimal instrate,
            BigDecimal aCumu, Date balanceDate, String changeType, List<AccInstrateZoneBean> zoneBeans) {
        this(account, fcAcc, startDate, instrateTime, instrate, aCumu, balanceDate);
        this.changeType = changeType;
        this.zoneBeans = zoneBeans;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getInstrateTime() {
        return instrateTime;
    }
    public void setInstrateTime(Date instrateTime) {
        this.instrateTime = instrateTime;
    }
    public BigDecimal getInstrate() {
        return instrate;
    }
    public void setInstrate(BigDecimal instrate) {
        this.instrate = instrate;
    }
    public BigDecimal getACumu() {
        return aCumu;
    }
    public void setACumu(BigDecimal aCumu) {
        this.aCumu = aCumu;
    }
    public FcAccDTO getFcAcc() {
        return fcAcc;
    }
    public void setFcAcc(FcAccDTO fcAcc) {
        this.fcAcc = fcAcc;
    }
    public BigDecimal getaCumu() {
        return aCumu;
    }
    public void setaCumu(BigDecimal aCumu) {
        this.aCumu = aCumu;
    }
    public Date getBalanceDate() {
        return balanceDate;
    }
    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
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
}
