package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
import java.util.Date;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
/**
 * <br>
 * <b>类功能：</b>利息管理封装数据实体<br>
 * <b></b>网点、开户种类、记账日期、止息日期、账户、用户信息<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月10日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class InterestCommonBean implements Serializable {
	/**
     * 网点
     */
    private String netCode;
    /**
     * 开户种类
     */
    private String accTypeCode;
    /**
     * 日结开始日期
     */
    private Date closeDate;
    
    /**
     * 记账日期
     */
    private Date tallyDate;
    /**
     * 止息日期
     */
    private Date endInstDate;
    /**
     * 中心账号
     */
    private String account;
    /**
     * 用户信息
     */
//    private ShiroUser userBean = new ShiroUser();
    
    
    /**
     * 操作类型
     */
    private OperateType opType;
    
    /**
     * 是否批量处理
     */
    private boolean isBatchProcess = false;

    /**
     * 当前用户ID
     */
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public InterestCommonBean() {
    }
//    public InterestCommonBean(String netCode, String accTypeCode, ShiroUser userBean) {
//        super();
//        this.netCode = netCode;
//        this.accTypeCode = accTypeCode;
//        this.userBean = userBean;
//    }
    
    public InterestCommonBean(Date tallyDate, Date endInstDate) {
        super();
        this.tallyDate = tallyDate;
        this.endInstDate = endInstDate;
    }
    
    public InterestCommonBean(Date tallyDate, Date closeDate, Date endInstDate) {
        super();
        this.tallyDate = tallyDate;
        this.closeDate = closeDate;
        this.endInstDate = endInstDate;
    }
    
    public InterestCommonBean(String netCode, String accTypeCode, Date tallyDate, Date endInstDate, String account) {
        super();
        this.netCode = netCode;
        this.accTypeCode = accTypeCode;
        this.tallyDate = tallyDate;
        this.endInstDate = endInstDate;
        this.account = account;
    }

    public InterestCommonBean(String netCode, String accTypeCode, Date tallyDate, Date endInstDate, String account, String userId) {
        super();
        this.netCode = netCode;
        this.accTypeCode = accTypeCode;
        this.tallyDate = tallyDate;
        this.endInstDate = endInstDate;
        this.account = account;
        this.userId = userId;
    }
//    public InterestCommonBean(String netCode, String accTypeCode, Date tallyDate, Date endInstDate, String account,
//    		ShiroUser userBean) {
//        super();
//        this.netCode = netCode;
//        this.accTypeCode = accTypeCode;
//        this.tallyDate = tallyDate;
//        this.endInstDate = endInstDate;
//        this.account = account;
//        this.userBean = userBean;
//    }
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
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
//    public ShiroUser getUserBean() {
//        return userBean;
//    }
//
//    public void setUserBean(ShiroUser userBean) {
//        this.userBean = userBean;
//    }
    public OperateType getOpType() {
        return opType;
    }
    public void setOpType(OperateType opType) {
        this.opType = opType;
    }
    public Date getCloseDate() {
        return closeDate;
    }
    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }
    public boolean isBatchProcess() {
        return isBatchProcess;
    }
    public void setBatchProcess(boolean isBatchProcess) {
        this.isBatchProcess = isBatchProcess;
    }
}
