package com.bytter.system.service.impl;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.system.api.ICalendarService;
import com.bytter.system.api.dto.CalendarDTO;
import com.bytter.system.api.dto.SysCalendarDTO;
import com.bytter.system.dao.SysCalendarDao;
import com.bytter.system.entity.SysCalendar;
import com.bytter.system.service.SysCalendarService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * 日历接口实现类
 * Created by zhouj on 2017/7/20.
 */
@Service("calendarServiceI")
@DubboService
@Transactional
public class CalendarServiceImpl implements ICalendarService {
    @Autowired
    //@Qualifier("sysCalendarServiceBase")
    private SysCalendarService sysCalendarService;
    @Autowired
    private SysCalendarDao sysCalendarDao;
    @Override
    public CalendarDTO getWorkDayByTime(Object startTime, Object endTime, Boolean status) {
        Date sTime = null, eTime = null;
        List<SysCalendar> workList = new ArrayList<SysCalendar>();
        List<SysCalendar> nonWorkList = new ArrayList<SysCalendar>();
        Map<String, Object> timeMap = initDealTime(sTime, eTime, startTime, endTime);
        int day = (Integer) timeMap.get("day");
        sTime = timeMap.get("sTime") == null ? null : (Date) timeMap.get("sTime");
        eTime = timeMap.get("eTime") == null ? null : (Date) timeMap.get("eTime");
        if (day >= 0 && sTime != null) {
            List<SysCalendar> list = sysCalendarService.getSysCalendarByDates(sTime, eTime, null);
            //比较list 大小  是否缺少某天数据 初始化某天的数据
            Date tempTime = sTime;
            for (int i = 0; i <= day; i++) {
                SysCalendar sysCalendar = null;
                boolean isUpdate = false;
                try {
                    sysCalendar = list.get(i);
                    if (sysCalendar.getCreateDate().getTime() != tempTime.getTime()) {
                        sysCalendar = new SysCalendar(null, tempTime, "");
                        isUpdate = true;
                    }
                } catch (Exception e) {
                    sysCalendar = new SysCalendar(null, tempTime, "");
                    isUpdate = true;
                }
                if (isUpdate) {
                    sysCalendarService.saveOrUpdate(sysCalendar);
                }
                if (sysCalendar.getWorkDaySign() == 1) {
                    workList.add(sysCalendar);
                } else {
                    nonWorkList.add(sysCalendar);
                }
                tempTime = DateUtils.addDay(tempTime, 1);
            }
        }
        return new CalendarDTO(BeanUtils.copyListProperties(workList, SysCalendarDTO.class), BeanUtils.copyListProperties(nonWorkList, SysCalendarDTO.class));
    }
    @Override
    public Date checkWorkDay(Object time, boolean status) {
        Date sTime = null, eTime = null;
        Date resultTime = null;
        Map<String, Object> timeMap = initDealTime(null, null, time, null);
        int day = (Integer) timeMap.get("day");
        sTime = timeMap.get("sTime") == null ? null : (Date) timeMap.get("sTime");
        if (day >= 0 && sTime != null) {
            List<SysCalendar> list = sysCalendarService.getLatelyCalendarByDatesByBill(sTime, status);
            if (EmptyUtils.isEmptyList(list)) {
                return null;
            }
            resultTime = list.get(0).getCreateDate();
        }
        return resultTime;
    }

    @Override
    public Date getPrevWorkDay(Date time) {
        List<SysCalendarDTO> sysCalendarDTOS = listSysCalendarByDates(null, time, 1);
        if (ObjectUtil.isNotEmpty(sysCalendarDTOS)){
            return sysCalendarDTOS.get(0).getCreateDate();
        }else{
            return null;
        }
    }

    /**
     * 根据日期、币种、地点校验是否是工作日
     *
     * @param date      日期
     * @param curId     币别
     * @param countryId 地点
     * @return
     */
    @Override
    public boolean checkWorkDay(Date date, String curId, String countryId) {
        SysCalendar entity = new SysCalendar();
        entity.setCreateDate(date);
        entity.setCurId(curId);
        entity.setCountryId(countryId);
        List<SysCalendar> list = sysCalendarService.listBySearchBean(entity);
        //默认是工作日
        boolean result = true;
        if (EmptyUtils.isEmpty(list)) {
            SysCalendar sysCalendar = new SysCalendar(null, date, null);
            sysCalendarService.save(sysCalendar);
            //只要其中一笔设置为非工作日，则当天为非工作日
            if (sysCalendar.getWorkDaySign() == null ||
                    (sysCalendar.getWorkDaySign() != null && !CommonConstant.YES.equals(sysCalendar.getWorkDaySign().toString()))) {
                result = false;
            }
        } else {
            for (SysCalendar sysCalendar : list) {
                //只要其中一笔设置为非工作日，则当天为非工作日
                if (sysCalendar.getWorkDaySign() == null ||
                        (sysCalendar.getWorkDaySign() != null && !CommonConstant.YES.equals(sysCalendar.getWorkDaySign().toString()))) {
                    result = false;
                    continue;
                }
            }
        }
        return result;
    }
    protected Map<String, Object> initDealTime(Date sTime, Date eTime, Object startTime, Object endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (startTime instanceof String) {
            sTime = DateUtils.str2Date(startTime.toString(), DateUtils.date_sdf());
        }
        if (startTime instanceof Date) {
            sTime = DateUtils.str2Date(DateUtils.date2Str((Date) startTime, DateUtils.date_sdf()), DateUtils.date_sdf());
        }
        if (endTime instanceof String) {
            eTime = DateUtils.str2Date(endTime.toString(), DateUtils.date_sdf());
        }
        if (endTime instanceof Date) {
            eTime = DateUtils.str2Date(DateUtils.date2Str((Date) endTime, DateUtils.date_sdf()), DateUtils.date_sdf());
        }
        int day = DateUtils.dayDiffs(eTime, sTime);
        if (day >= 0 && (sTime != null || eTime != null)) {
            if (day == 0) {
                if (sTime == null) {
                    sTime = eTime;
                } else {
                    eTime = sTime;
                }
            }
        }
        map.put("day", day);
        map.put("sTime", sTime);
        map.put("eTime", eTime);
        return map;
    }
    /**
     * 根据传递的日期获取后面指定天数工作日的日期
     *
     * @param date       开始时间
     * @param workdayNum 工作日天数
     * @return 指定天数工作日的日期
     */
    public Date getWorkDay(Date date, int workdayNum) {
        List<SysCalendar> sysCalendar = sysCalendarService.getWorkDay(date, workdayNum);
        if (ListUtils.isNotNullOrEmpty(sysCalendar)) {
            return sysCalendar.get(0).getCreateDate();
        }
        return null;
    }
    public Integer getWorkDaySignByTime(Date time){
       return sysCalendarDao.getWorkDaySignByTime(time);
    }

    /**
     * 获取往前或往后指定天数的工作日
     *
     * @param startDate 开始日期：开始日期不为空，则往后查询指定天数的工作日
     * @param endDate   结束日期：结束日期不为空，则往前查询指定天数的工作日
     * @param days      指定天数
     * @return 工作日集合
     */
    @Override
    public List<SysCalendarDTO> listSysCalendarByDates(Date startDate, Date endDate, Integer days) {
        return sysCalendarDao.listSysCalendarByDates(startDate, endDate, days);
    }
}
