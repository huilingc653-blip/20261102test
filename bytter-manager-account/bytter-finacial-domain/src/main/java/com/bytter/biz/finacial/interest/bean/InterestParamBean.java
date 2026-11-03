package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
/**
 * <br>
 * <b>类功能：</b>利息管理系统参数Bean<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月10日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class InterestParamBean implements Serializable {
	/**
     * 利息类型是否预提
     */
    private boolean isPredraw;
    /**
     * 利息收入\利息支出账号是否挂中心
     */
    private boolean isAccInCenter;
    /**
     * 计息凭证类型是否多条分录
     */
    private boolean isMoreVoucherDtl;
    
    /**
     * 是否生成指令
     */
    private boolean isBisExc;
    
    /**
     * 是否冲预提
     */
    private boolean isRush;
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
    public InterestParamBean(boolean isPredraw, boolean isAccInCenter, boolean isMoreVoucherDtl) {
        super();
        this.isPredraw = isPredraw;
        this.isAccInCenter = isAccInCenter;
        this.isMoreVoucherDtl = isMoreVoucherDtl;
    }
    public boolean isPredraw() {
        return isPredraw;
    }
    public void setPredraw(boolean isPredraw) {
        this.isPredraw = isPredraw;
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
    public boolean isBisExc() {
        return isBisExc;
    }
    public void setBisExc(boolean isBisExc) {
        this.isBisExc = isBisExc;
    }
    public boolean isRush() {
        return isRush;
    }
    public void setRush(boolean isRush) {
        this.isRush = isRush;
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
