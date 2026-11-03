package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BusinessVolumeVO implements Serializable {
    
    /**单位*/ 
    @Corp(inject="corpName")
    private String corpId;
    private String corpName;
    /**币别*/
    @Currency(inject="curName")
    private String curId;
    private String curName;
    /**业务分类*/
    @Dict(dictGroup = "report.busSign", inject = "busSignName")
    private String busSign;
    private String busSignName;
    /**业务品种*/
    @Dict(dictGroup = "report.busType", inject = "busTypeName")
    private String busType;
    private String busTypeName;
    /**交易笔数*/
    private BigDecimal num;
    /**交易金额*/
    private BigDecimal money;
    /**上月笔数*/
    private BigDecimal numM;
    /**上月余额*/
    private BigDecimal balanceM;
    /**去年同期笔数*/
    private BigDecimal numL;
    /**去年同期余额*/
    private BigDecimal balanceL;
    /**当季笔数*/
    private BigDecimal numNowS;
    /**当季余额*/
    private BigDecimal balanceNowS;
    /**上季笔数*/
    private BigDecimal numS;
    /**上季余额*/
    private BigDecimal balanceS;
    /**当年笔数*/
    private BigDecimal numNowY;
    /**当年余额*/
    private BigDecimal balanceNowY;
    /**去年笔数*/
    private BigDecimal numY;
    /**去年余额*/
    private BigDecimal balanceY;
    /**是否折合本位币*/
    private String isToCurr;
    /**对比周期*/
    private String cycle;
    /**金额单位*/
    private String moneyType;
    /**统计月份*/
    private Date monthDate;
    private String corpIds;
    /**制单时间*/
    private Date createDate;
    private String id;

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

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getBusSign() {
        return busSign;
    }

    public void setBusSign(String busSign) {
        this.busSign = busSign;
    }

    public String getBusSignName() {
        return busSignName;
    }

    public void setBusSignName(String busSignName) {
        this.busSignName = busSignName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusTypeName() {
        return busTypeName;
    }

    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }

    public BigDecimal getNum() {
        if(num == null){
            return BigDecimal.ZERO;
        }
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public BigDecimal getMoney() {
        if(money == null){
            return BigDecimal.ZERO;
        }
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getNumM() {
        if(numM == null){
            return BigDecimal.ZERO;
        }
        return numM;
    }

    public void setNumM(BigDecimal numM) {
        this.numM = numM;
    }

    public BigDecimal getBalanceM() {
        if(balanceM == null){
            return BigDecimal.ZERO;
        }
        return balanceM;
    }

    public void setBalanceM(BigDecimal balanceM) {
        this.balanceM = balanceM;
    }

    public BigDecimal getNumL() {
        if(numL == null){
            return BigDecimal.ZERO;
        }
        return numL;
    }

    public void setNumL(BigDecimal numL) {
        this.numL = numL;
    }

    public BigDecimal getBalanceL() {
        if(balanceL == null){
            return BigDecimal.ZERO;
        }
        return balanceL;
    }

    public void setBalanceL(BigDecimal balanceL) {
        this.balanceL = balanceL;
    }

    public BigDecimal getNumNowS() {
        if(numNowS == null){
            return BigDecimal.ZERO;
        }
        return numNowS;
    }

    public void setNumNowS(BigDecimal numNowS) {
        this.numNowS = numNowS;
    }

    public BigDecimal getBalanceNowS() {
        if(balanceNowS == null){
            return BigDecimal.ZERO;
        }
        return balanceNowS;
    }

    public void setBalanceNowS(BigDecimal balanceNowS) {
        this.balanceNowS = balanceNowS;
    }

    public BigDecimal getNumS() {
        if(numS == null){
            return BigDecimal.ZERO;
        }
        return numS;
    }

    public void setNumS(BigDecimal numS) {
        this.numS = numS;
    }

    public BigDecimal getBalanceS() {
        if(balanceS == null){
            return BigDecimal.ZERO;
        }
        return balanceS;
    }

    public void setBalanceS(BigDecimal balanceS) {
        this.balanceS = balanceS;
    }

    public BigDecimal getNumNowY() {
        if(numNowY == null){
            return BigDecimal.ZERO;
        }
        return numNowY;
    }

    public void setNumNowY(BigDecimal numNowY) {
        this.numNowY = numNowY;
    }

    public BigDecimal getBalanceNowY() {
        if(balanceNowY == null){
            return BigDecimal.ZERO;
        }
        return balanceNowY;
    }

    public void setBalanceNowY(BigDecimal balanceNowY) {
        this.balanceNowY = balanceNowY;
    }

    public BigDecimal getNumY() {
        if(numY == null){
            return BigDecimal.ZERO;
        }
        return numY;
    }

    public void setNumY(BigDecimal numY) {
        this.numY = numY;
    }

    public BigDecimal getBalanceY() {
        if(balanceY == null){
            return BigDecimal.ZERO;
        }
        return balanceY;
    }

    public void setBalanceY(BigDecimal balanceY) {
        this.balanceY = balanceY;
    }

    public String getIsToCurr() {
        return isToCurr;
    }

    public void setIsToCurr(String isToCurr) {
        this.isToCurr = isToCurr;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public Date getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(Date monthDate) {
        this.monthDate = monthDate;
    }

    public String getCorpIds() {
        return corpIds;
    }

    public void setCorpIds(String corpIds) {
        this.corpIds = corpIds;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
