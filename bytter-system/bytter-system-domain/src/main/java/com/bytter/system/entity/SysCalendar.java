package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.utils.DateUtils;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: SYS_CALENDAR
 * @date 2017-07-17 16:13:12
 */
@Entity
@Table(name = "SYS_CALENDAR", schema = "")
@SuppressWarnings("serial")
public class SysCalendar extends IdEntity implements java.io.Serializable {
    /**
     * calendarNum
     */
    private Integer calendarNum;
    /**
     * workDaySign
     */
    private Integer workDaySign;
    /**
     * monthEndSign
     */
    private Integer monthEndSign;
    /**
     * yearEndSign
     */
    private Integer yearEndSign;
    /**
     * 地点
     */
    private String countryId;
    /**
     * 币别
     */
    private String curId;
    /**
     * createBy
     */
    private String createBy;
    /**
     * createDate
     */
    private Date createDate;
    public SysCalendar() {
        super();
    }
    public SysCalendar(String id, Date time, String createBy) {
        super();
        this.setId(id);
        this.workDaySign = DateUtils.checkHoliday(time) ? 0 : 1;
        this.createBy = createBy;
        this.monthEndSign = 1;
        this.yearEndSign = 1;
        this.createDate = time;
        this.calendarNum = DateUtils.getDay(time);
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  calendarNum
     */
    @Column(name = "CALENDAR_NUM", nullable = false, length = 14)
    public Integer getCalendarNum() {
        return this.calendarNum;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  calendarNum
     */
    public void setCalendarNum(Integer calendarNum) {
        this.calendarNum = calendarNum;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  workDaySign
     */
    @Column(name = "WORK_DAY_SIGN", nullable = false, length = 10)
    public Integer getWorkDaySign() {
        return this.workDaySign;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  workDaySign
     */
    public void setWorkDaySign(Integer workDaySign) {
        this.workDaySign = workDaySign;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  monthEndSign
     */
    @Column(name = "MONTH_END_SIGN", nullable = false, length = 10)
    public Integer getMonthEndSign() {
        return this.monthEndSign;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  monthEndSign
     */
    public void setMonthEndSign(Integer monthEndSign) {
        this.monthEndSign = monthEndSign;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  yearEndSign
     */
    @Column(name = "YEAR_END_SIGN", nullable = false, length = 10)
    public Integer getYearEndSign() {
        return this.yearEndSign;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  yearEndSign
     */
    public void setYearEndSign(Integer yearEndSign) {
        this.yearEndSign = yearEndSign;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  createBy
     */
    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  createBy
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  createDate
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "COUNTRY_ID", nullable = true, length = 32)
    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
    @Column(name = "CUR_ID", nullable = true, length = 32)
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
}
