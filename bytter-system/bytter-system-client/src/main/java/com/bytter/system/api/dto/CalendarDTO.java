package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.List;
/**
 * Created by zhouj on 2017/7/20.
 * 日期dto
 */
public class CalendarDTO implements Serializable {
    /*工作日天数*/
    private Long workDays;
    /*工作日数组*/
    private List<SysCalendarDTO> workDayList;
    /*非工作日*/
    private Long nonWorkDays;
    /*非工作日数组*/
    private List<SysCalendarDTO> nonWorkDayList;
    private Integer workDaySign;
    public Integer getWorkDaySign() {
        return workDaySign;
    }
    public void setWorkDaySign(Integer workDaySign) {
        this.workDaySign = workDaySign;
    }
    public CalendarDTO() {
        super();
    }
    public CalendarDTO(List<SysCalendarDTO> workList, List<SysCalendarDTO> nonWorkList) {
        super();
        this.workDays = workList == null ? 0L : workList.size();
        this.workDayList = workList;
        this.nonWorkDays = nonWorkList == null ? 0L : nonWorkList.size();
        this.nonWorkDayList = nonWorkList;
    }
    public Long getWorkDays() {
        return workDays;
    }
    public void setWorkDays(Long workDays) {
        this.workDays = workDays;
    }
    public List<SysCalendarDTO> getWorkDayList() {
        return workDayList;
    }
    public void setWorkDayList(List<SysCalendarDTO> workDayList) {
        this.workDayList = workDayList;
    }
    public Long getNonWorkDays() {
        return nonWorkDays;
    }
    public void setNonWorkDays(Long nonWorkDays) {
        this.nonWorkDays = nonWorkDays;
    }
    public List<SysCalendarDTO> getNonWorkDayList() {
        return nonWorkDayList;
    }
    public void setNonWorkDayList(List<SysCalendarDTO> nonWorkDayList) {
        this.nonWorkDayList = nonWorkDayList;
    }
}
