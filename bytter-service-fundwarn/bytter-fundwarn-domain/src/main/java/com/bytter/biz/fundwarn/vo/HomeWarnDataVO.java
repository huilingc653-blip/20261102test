package com.bytter.biz.fundwarn.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.util.Date;
public class HomeWarnDataVO implements Serializable {
    /**
     * 业务日期
     */
    private Date warnDate;
    /** 预警级别*/
    private String warnLevel;
    private String warnLevelName;
    /**
     * 预警次数
     */
    private Integer warnTimes;
    public Date getWarnDate() {
        return warnDate;
    }
    public void setWarnDate(Date warnDate) {
        this.warnDate = warnDate;
    }
    public String getWarnLevel() {
        return warnLevel;
    }
    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }
    public String getWarnLevelName() {
        return warnLevelName;
    }
    public void setWarnLevelName(String warnLevelName) {
        this.warnLevelName = warnLevelName;
    }
    public Integer getWarnTimes() {
        return warnTimes;
    }
    public void setWarnTimes(Integer warnTimes) {
        this.warnTimes = warnTimes;
    }
}
