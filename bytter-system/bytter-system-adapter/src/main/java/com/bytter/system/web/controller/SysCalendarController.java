package com.bytter.system.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.SysCalendar;
import com.bytter.system.service.SysCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: SYS_CALENDAR
 * @date 2017-07-17 16:13:12
 */
@Controller
@RequestMapping("/calendar/syscalendar")
public class SysCalendarController extends BaseController {
    @Autowired
    private SysCalendarService sysCalendarService;
    /**
     * SYS_CALENDAR列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询SYS_CALENDAR列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "calendar/listSysCalendar";
    }
    @PostMapping("/getWork")
    @ResponseBody
    public AjaxJson getWork(Date time) {
        AjaxJson ajaxJson = new AjaxJson(true);
        List<SysCalendar> list = sysCalendarService.getSysCalendarByDate(time);
        if (!EmptyUtils.isEmptyList(list)) {
            ajaxJson.setObj(list.get(0));
        } else {
            SysCalendar sysCalendar = new SysCalendar(null, time, getUserVo().getLoginName());
            sysCalendarService.save(sysCalendar);
            ajaxJson.setObj(sysCalendar);
        }
        return ajaxJson;
    }
    @PostMapping("/getWorkList")
    @ResponseBody
    public AjaxJson getWorkList(Date startTime, Date endTime, String type, String countryId, String curId) {
        List<SysCalendar> result = new ArrayList<>();
        AjaxJson ajaxJson = new AjaxJson(true);
        if (CommonConstant.NO.equals(type)) {
            if (StringUtils.isBlank(countryId)) {
                type = null;
            }
        } else if (CommonConstant.YES.equals(type)) {
            if (StringUtils.isBlank(curId)) {
                type = null;
            }
        }
        int day = DateUtils.dayDiffs(endTime, startTime);
        if (day >= 0) {
            if (startTime != null || endTime != null) {
                if (day == 0 && startTime != null) {
                    endTime = startTime;
                }
                if (day == 0 && endTime != null) {
                    startTime = endTime;
                }
                List<SysCalendar> sysList = sysCalendarService.getSysCalendarByDates(startTime, endTime, null, type, "0".equals(type) ? countryId : curId);
                //脏数据过滤begin
                //过滤重复数据
                Map<String, SysCalendar> map = new LinkedHashMap<>();
                //待删除id
                List<String> listIds = new ArrayList<>();
                for (SysCalendar sysCalendar : sysList) {
                    if (!map.containsKey(sysCalendar.getCreateDate().toString())) {
                        map.put(sysCalendar.getCreateDate().toString(), sysCalendar);
                    } else {
                        listIds.add(sysCalendar.getId());
                    }
                }
                sysCalendarService.delete(listIds);
                //map转list
                List<SysCalendar> list = new ArrayList<>();
                for (String s : map.keySet()) {
                    list.add(map.get(s));
                }
                //脏数据过滤end
                //比较list 大小  是否缺少某天数据 初始化某天的数据
                Date tempTime = startTime;
                for (int i = 0; i <= day; i++) {
                    SysCalendar sysCalendar = null;
                    boolean flag = true;
                    try {
                        boolean isNew = true;
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).getCreateDate().getTime() == tempTime.getTime()) {
                                sysCalendar = list.get(j);
                                flag = false;
                                isNew = false;
                                break;
                            }
                        }
                        if (isNew) {
                            sysCalendar = new SysCalendar(null, tempTime, getUserVo().getLoginName());
                        }
                    } catch (Exception e) {
                        sysCalendar = new SysCalendar(null, tempTime, getUserVo().getLoginName());
                    }
                    if (flag) {
                        if (CommonConstant.NO.equals(type)) {
                            sysCalendar.setCountryId(countryId);
                        } else if (CommonConstant.YES.equals(type)) {
                            sysCalendar.setCurId(curId);
                        }
                        sysCalendarService.saveOrUpdate(sysCalendar);
                    }
                    result.add(sysCalendar);
                    tempTime = DateUtils.addDay(tempTime, 1);
                }
            }
        }
        ajaxJson.setObj(result);
        return ajaxJson;
    }
    @LogMessage(content = "修改工作日状态", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyWork")
    @ResponseBody
    public AjaxJson modifyWork(Date startTime, Date endTime, int status, String type, String countryId, String curId) {
        if (CommonConstant.NO.equals(type)) {
            if (StringUtils.isBlank(countryId)) {
                type = null;
            }
        } else if (CommonConstant.YES.equals(type)) {
            if (StringUtils.isBlank(curId)) {
                type = null;
            }
        }
        int day = DateUtils.dayDiffs(endTime, startTime);
        if (day >= 0) {
            if (startTime != null || endTime != null) {
                if (day == 0 && startTime != null) {
                    endTime = startTime;
                }
                if (day == 0 && endTime != null) {
                    startTime = endTime;
                }
                List<SysCalendar> sysList = sysCalendarService.getSysCalendarByDates(startTime, endTime, null, type, "0".equals(type) ? countryId : curId);
                //脏数据过滤begin
                //过滤重复数据
                Map<String, SysCalendar> map = new LinkedHashMap<>();
                //待删除id
                List<String> listIds = new ArrayList<>();
                for (SysCalendar sysCalendar : sysList) {
                    if (!map.containsKey(sysCalendar.getCreateDate().toString())) {
                        map.put(sysCalendar.getCreateDate().toString(), sysCalendar);
                    } else {
                        listIds.add(sysCalendar.getId());
                    }
                }
                sysCalendarService.delete(listIds);
                //map转list
                List<SysCalendar> list = new ArrayList<>();
                for (String s : map.keySet()) {
                    list.add(map.get(s));
                }
                //脏数据过滤end
                //比较list 大小  是否缺少某天数据 初始化某天的数据
                Date tempTime = startTime;
                for (int i = 0; i <= day; i++) {
                    SysCalendar sysCalendar = null;
                    try {
                        sysCalendar = list.get(i);
                        if (sysCalendar.getCreateDate().getTime() == tempTime.getTime()) {
                            sysCalendar.setWorkDaySign(status);
                        } else {
                            sysCalendar = new SysCalendar(null, tempTime, getUserVo().getLoginName());
                        }
                    } catch (Exception e) {
                        sysCalendar = new SysCalendar(null, tempTime, getUserVo().getLoginName());
                    }
                    if (CommonConstant.NO.equals(type)) {
                        sysCalendar.setCountryId(countryId);
                    } else if (CommonConstant.YES.equals(type)) {
                        sysCalendar.setCurId(curId);
                    }
                    sysCalendarService.saveOrUpdate(sysCalendar);
                    tempTime = DateUtils.addDay(tempTime, 1);
                }
            }
        }
        return success();
    }
}
