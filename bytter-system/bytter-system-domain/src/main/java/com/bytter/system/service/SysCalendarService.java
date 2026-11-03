package com.bytter.system.service;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.HttpUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.dao.SysCalendarDao;
import com.bytter.system.entity.SysCalendar;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: SYS_CALENDAR
 * @date 2017-07-17 16:13:12
 */
@Service("sysCalendarService")
// @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class SysCalendarService extends BaseService<SysCalendar, String> implements ITaskService {
    @Autowired
    private SysCalendarDao sysCalendarDao;
    /**
     * 保存
     *
     * @param entity
     */
    public void save(SysCalendar entity) {
        super.saveEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(SysCalendar entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<SysCalendar, String> getEntityDao() {
        return sysCalendarDao;
    }
    public List<SysCalendar> getSysCalendarByDate(Date date) {
        return sysCalendarDao.listBy("createDate", date);
    }
    public List<SysCalendar> getSysCalendarByDates(Date startTime, Date endTime, Boolean status, String type, String typeId) {
        return sysCalendarDao.getSysCalendarByDates(startTime, endTime, status, type, typeId);
    }
    public List<SysCalendar> getSysCalendarByDates(Date startTime, Date endTime, Boolean status) {
        return sysCalendarDao.getSysCalendarByDates(startTime, endTime, status);
    }
    /**
     * 取出最近的日期
     *
     * @param startTime
     * @param status
     * @return
     */
    public List<SysCalendar> getLatelyCalendarByDates(Date startTime, boolean status) {
        return sysCalendarDao.getLatelyCalendarByDates(startTime, status, 0, 1);
    }
    /**
     * 取地点为空、币别为空的日历维护取出最近的工作日
     *
     * @param startTime
     * @param status
     * @return
     */
    public List<SysCalendar> getLatelyCalendarByDatesByBill(Date startTime, boolean status) {
        return sysCalendarDao.getLatelyCalendarByDatesByBIll(startTime, status, 0, 1);
    }
    /**
     * 根据条件获取日历数据
     *
     * @param searchBean 封装条件
     * @return SysCalendar
     */
    public List<SysCalendar> listBySearchBean(SysCalendar searchBean) {
        return sysCalendarDao.listBySearchBean(searchBean);
    }
    /**
     * 根据传递的日期获取后面指定天数工作日的日期
     *
     * @param date       开始时间
     * @param workdayNum 工作日天数
     * @return 指定天数工作日的日期
     */
    public List<SysCalendar> getWorkDay(Date date, int workdayNum) {
        if (date == null || workdayNum < 1) {
            return null;
        }
        return sysCalendarDao.getLatelyCalendarByDatesByBIll(DateUtils.addDay(date, 1), true, workdayNum - 1, 1);
    }

    /**
     * 日历初始化设置保存
     * @param year
     */
    public void calendarInitSave(String year){
        String url = "http://timor.tech/api/holiday/year/";
        if(EmptyUtils.isNotEmpty(year)){
            url += year;
            //调用第三方接口获取当年的国家法定节假日
            String result = HttpUtils.sendGet(url);
            Map map = JsonUtils.json2Map(result);
            //服务器返回成功
            if(!EmptyUtils.isEmpty(map) && 0 == (Integer) map.get("code")){
                Map<String,Map<String,Object>> holidayMap = (Map<String,Map<String,Object>>) map.get("holiday");
                for(String key : holidayMap.keySet()){
                    Map<String,Object> entityMap = holidayMap.get(key);
                    //日期
                    String dateStr = (String) entityMap.get("date");
                    //是否是假期
                    Boolean holiday = (Boolean)entityMap.get("holiday");
                    Date date = DateUtils.parse(dateStr,"yyyy-MM-dd");
                    List<SysCalendar> list = sysCalendarDao.getSysCalendarByDates(date,date,null);
                    if(EmptyUtils.isNotEmpty(list)){
                        for(SysCalendar sysCalendar : list){
                            if(holiday){
                                sysCalendar.setWorkDaySign(0);
                            }else{
                                sysCalendar.setWorkDaySign(1);
                            }
                            sysCalendarDao.saveOrUpdate(sysCalendar);
                        }
                    }
                }
            }
        }
    }


    /**
     * 中国大陆标准公共日历初始化
     * @param arguments
     */
    @Override
    @XxlJob("SysCalendarService")
    public void run(String arguments) {
        try {
            this.logger.info("---中国大陆标准公共日历初始化，开始执行定时任务---");
            String year = (DateUtils.getYear()+1)+"";
            this.calendarInitSave(year);
            this.logger.info("---中国大陆标准公共日历初始化，结束执行定时任务---");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
