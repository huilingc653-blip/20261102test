package com.bytter.system.api;

import com.bytter.system.api.dto.CalendarDTO;
import com.bytter.system.api.dto.SysCalendarDTO;

import java.util.Date;
import java.util.List;

/**
 * 日历工作日接口类
 * Created by zhouj on 2017/7/20.
 */
public interface ICalendarService {
    /**
     * 获取区间内工作日和非工作日
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param status    状态 null取所有 true工作日 false非工作日
     * @return CalendarDTO
     */
    CalendarDTO getWorkDayByTime(Object startTime, Object endTime, Boolean status);

    /**
     * <b>功能：</b>根据日期取地点为空、币别为空的日历维护是否为工作日<br>
     *
     * @param
     */
    Integer getWorkDaySignByTime(Date time);

    /**
     * 根据时间查询如果没有则延顺到下一个工作日or非工作日
     *
     * @param time
     * @param status true工作日 false非工作日
     * @return Date
     */
    Date checkWorkDay(Object time, boolean status);

    /**
     * 根据指定日期查询上一个工作日
     *
     * @param time
     * @return Date
     */
    Date getPrevWorkDay(Date time);

    /**
     * 判断日期是否是工作日
     *
     * @param date      日期
     * @param curId     币别
     * @param countryId 地点
     * @return true：是      false：不是
     */
    boolean checkWorkDay(Date date, String curId, String countryId);

    /**
     * 根据传递的日期获取后面指定天数工作日的日期
     *
     * @param date       开始时间(格式不能带时分秒）
     * @param workdayNum 工作日天数
     * @return 指定天数工作日的日期
     */
    Date getWorkDay(Date date, int workdayNum);

    /**
     * 获取往前或往后指定天数的工作日
     *
     * @param startDate 开始日期：开始日期不为空，则往后查询指定天数的工作日
     * @param endDate   结束日期：结束日期不为空，则往前查询指定天数的工作日
     * @param days      指定天数
     * @return 工作日集合
     */
    List<SysCalendarDTO> listSysCalendarByDates(Date startDate, Date endDate, Integer days);
}
