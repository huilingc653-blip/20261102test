package com.bytter.system.api.dto;
import com.bytter.core.utils.DateUtils;
import java.io.Serializable;
import java.util.Date;
/**
 * 网点日结日期传输显示对象
 *
 * @author liyh
 */
public class FcDayCloseDTO implements Serializable {
    /**
     * 网点Id
     */
    private String netCode;
    /**
     * 日结日期
     */
    private Date closeDate;
    private String closeDateStr;
    /**
     * 已完成日结日期
     */
    private Date lastCloseDate;
    private String lastCloseDateStr;
    /**
     * 资金池主体标识
     */
    private String poolSign;
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public Date getCloseDate() {
        return closeDate;
    }
    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }
    public Date getLastCloseDate() {
        return lastCloseDate;
    }
    public void setLastCloseDate(Date lastCloseDate) {
        this.lastCloseDate = lastCloseDate;
    }
    public String getCloseDateStr() {
        if (null != closeDate) {
            return DateUtils.formatDate(closeDate);
        }
        return closeDateStr;
    }
    public void setCloseDateStr(String closeDateStr) {
        this.closeDateStr = closeDateStr;
    }
    public String getLastCloseDateStr() {
        if (null != lastCloseDate) {
            return DateUtils.formatDate(lastCloseDate);
        }
        return lastCloseDateStr;
    }
    public void setLastCloseDateStr(String lastCloseDateStr) {
        this.lastCloseDateStr = lastCloseDateStr;
    }
}
