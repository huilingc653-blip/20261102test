package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class NotifyDrawInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 账号
     */
    private String account;
    /**
     * 是否存在通知取款信息
     */
    private Boolean isNotified;
    /**
     * 理财管理通知取款
     */
    private ChoInnerFinanceBean chowmatisticBean;
    /**
     * 存款管理通知取款
     */
    //private FcDrawNotice drawNotice;
    /**
     * 取款时间
     */
    private Date drawDate;
    /**
     * 取消时间
     */
    private Date enterDate;
    /**
     * 登记金额
     */
    private BigDecimal noticeMoney;
    /**
     * 登记时间
     */
    private Date notifyTime;
    public NotifyDrawInfo(String account) {
        this.account = account;
        this.isNotified = false;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Boolean getNotified() {
        return isNotified;
    }
    public void setNotified(Boolean notified) {
        isNotified = notified;
    }
    public ChoInnerFinanceBean getChowmatisticBean() {
        return chowmatisticBean;
    }
    public void setChowmatisticBean(ChoInnerFinanceBean chowmatisticBean) {
        this.chowmatisticBean = chowmatisticBean;
    }
    public Date getDrawDate() {
        return drawDate;
    }
    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }
    public Date getEnterDate() {
        return enterDate;
    }
    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }
    public BigDecimal getNoticeMoney() {
        return noticeMoney;
    }
    public void setNoticeMoney(BigDecimal noticeMoney) {
        this.noticeMoney = noticeMoney;
    }
    public Date getNotifyTime() {
        return notifyTime;
    }
    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }
    
    
}
