package com.bytter.biz.finacial.interest.bean;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import java.io.Serializable;
import java.util.Date;
/**
 * <br>
 * <b>功能：</b> 利息管理封装数据实体 <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2021/2/4 0004 <br>
 */
public class AccInterestBean implements Serializable {
    private String netCode;//网点
    private String accTypeCode;//开户种类
    private Date tallyDate;//记账日期
    private Date endInstDate;//止息日期
    private ICurrentUser user;//用户
    private OperateType opType;//操作类型
    private String account;//账号
    private boolean poolSign;//资金池标识
    private String generateMode;//产生方式
    private Integer affixAmount;//附件
    private Integer busOriType;//业务类型
    public AccInterestBean(String netCode, String accTypeCode, Date tallyDate, Date endInstDate,
                           ICurrentUser user, OperateType opType, String account,
                           boolean poolSign, String generateMode, Integer affixAmount,
                           Integer busOriType) {
        this.netCode = netCode;
        this.accTypeCode = accTypeCode;
        this.tallyDate = tallyDate;
        this.endInstDate = endInstDate;
        this.user = user;
        this.opType = opType;
        this.account = account;
        this.poolSign = poolSign;
        this.generateMode = generateMode;
        this.affixAmount = affixAmount;
        this.busOriType = busOriType;
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public String getAccTypeCode() {
        return accTypeCode;
    }
    public void setAccTypeCode(String accTypeCode) {
        this.accTypeCode = accTypeCode;
    }
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    public Date getEndInstDate() {
        return endInstDate;
    }
    public void setEndInstDate(Date endInstDate) {
        this.endInstDate = endInstDate;
    }
    public ICurrentUser getUser() {
        return user;
    }
    public void setUser(ICurrentUser user) {
        this.user = user;
    }
    public OperateType getOpType() {
        return opType;
    }
    public void setOpType(OperateType opType) {
        this.opType = opType;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public boolean isPoolSign() {
        return poolSign;
    }
    public void setPoolSign(boolean poolSign) {
        this.poolSign = poolSign;
    }
    public String getGenerateMode() {
        return generateMode;
    }
    public void setGenerateMode(String generateMode) {
        this.generateMode = generateMode;
    }
    public Integer getAffixAmount() {
        return affixAmount;
    }
    public void setAffixAmount(Integer affixAmount) {
        this.affixAmount = affixAmount;
    }
    public Integer getBusOriType() {
        return busOriType;
    }
    public void setBusOriType(Integer busOriType) {
        this.busOriType = busOriType;
    }
}
