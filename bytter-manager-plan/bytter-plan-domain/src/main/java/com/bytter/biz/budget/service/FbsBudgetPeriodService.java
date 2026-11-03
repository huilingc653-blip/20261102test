package com.bytter.biz.budget.service;

import com.bytter.biz.budget.dao.FbsBudgetPeriodDao;
import com.bytter.biz.budget.entity.FbsBudgetPeriod;
import com.bytter.biz.budget.spi.enums.FbsBudgetPeriodMessageEnum;
import com.bytter.biz.budget.vo.FbsBudgetPeriodTransmitVO;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICalendarService;
import com.bytter.system.api.dto.CalendarDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_BUDGET_PERIOD
 * @date 2017-07-07 16:09:08
 */
@Service("fbsBudgetPeriodService")
@Transactional
public class FbsBudgetPeriodService extends BaseService<FbsBudgetPeriod, String> {
    @Autowired
    private FbsBudgetPeriodDao fbsBudgetPeriodDao;
    @DubboReference(check = false)
	private ICalendarService calendarService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FbsBudgetPeriod entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FbsBudgetPeriod entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FbsBudgetPeriod entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FbsBudgetPeriod entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<FbsBudgetPeriod, String> getEntityDao() {
        return fbsBudgetPeriodDao;
    }
    /**
     * 得到表格的值
     *
     * @return
     */
    public List<FbsBudgetPeriodVO> getBtTable(String year) {
        List<FbsBudgetPeriod> perList = fbsBudgetPeriodDao.getBtTable(year);
        List<FbsBudgetPeriodVO> voList = new ArrayList<FbsBudgetPeriodVO>();
        voList = BeanUtils.copyListProperties(perList, FbsBudgetPeriodVO.class);
        return voList;
    }
    /**
     * 根据年自动计算这一年的自然周
     *
     * @param year
     */
    public List<FbsBudgetPeriodVO> autoAenerate(String year) {
        List<FbsBudgetPeriodVO> voList = new ArrayList<FbsBudgetPeriodVO>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date yearBeginDate = new Date();
        Date yearEndDate = new Date();
        try {
            yearBeginDate = dateFormat.parse(year + "-01-01");
            yearEndDate = dateFormat.parse(year + "-12-31");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.autoAenerateByDate(yearBeginDate, yearEndDate, voList);
        return voList;
    }
    /**
     * 根据年自动计算这一年的标准周
     *
     * @param year
     */
    public List<FbsBudgetPeriodVO> autoStandard(String year) {
        List<FbsBudgetPeriodVO> voList = new ArrayList<FbsBudgetPeriodVO>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date yearBeginDate = new Date();
        Date yearEndDate = new Date();
        try {
            yearBeginDate = dateFormat.parse(year + "-01-01");
            yearEndDate = dateFormat.parse(year + "-12-31");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.autoStandardByDate(yearBeginDate, yearEndDate, voList);
        return voList;
    }
    /**
     * 保存自定义周期
     *
     * @param fbsBudgetPeriodBean
     */
    public void toSave(FbsBudgetPeriodTransmitVO fbsBudgetPeriodBean, ICurrentUser user) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //先删除再插入保持一年只有唯一的数据
        List<FbsBudgetPeriod> list = fbsBudgetPeriodDao.getBtTable(fbsBudgetPeriodBean.getYear());
        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                this.deleteEntity(list.get(i));
            }
        }
        try {
            //根据前台得到的数据
            int count = 0;
            for (int i = 0; i < fbsBudgetPeriodBean.getMonth().length; i++) {
                for (int j = 0; j < Integer.parseInt(fbsBudgetPeriodBean.getWeek()[i]); j++, count++) {
                    FbsBudgetPeriod fbsBudgetPeriod = new FbsBudgetPeriod();
                    fbsBudgetPeriod.setYear(fbsBudgetPeriodBean.getYear());
                    if (!EmptyUtils.isEmpty(fbsBudgetPeriodBean.getMonth()[i])) {
                        fbsBudgetPeriod.setMonth(Integer.parseInt(fbsBudgetPeriodBean.getMonth()[i]));
                    }
                    if (!EmptyUtils.isEmpty(fbsBudgetPeriodBean.getWeek()[i])) {
                        fbsBudgetPeriod.setWeek(Integer.parseInt(fbsBudgetPeriodBean.getWeek()[i]));
                    }
                    if (!EmptyUtils.isEmpty(formatter.parse(fbsBudgetPeriodBean.getBeginDate()[count]))) {
                        fbsBudgetPeriod.setBeginDate(formatter.parse(fbsBudgetPeriodBean.getBeginDate()[count]));
                    }
                    if (!EmptyUtils.isEmpty(formatter.parse(fbsBudgetPeriodBean.getEndDate()[count]))) {
                        fbsBudgetPeriod.setEndDate(formatter.parse(fbsBudgetPeriodBean.getEndDate()[count]));
                    }
                    if (!EmptyUtils.isEmpty(fbsBudgetPeriodBean.getWeekdays()[count])) {
                        fbsBudgetPeriod.setWeekdays(Integer.parseInt(fbsBudgetPeriodBean.getWeekdays()[count]));
                    }
                    if (!EmptyUtils.isEmpty(fbsBudgetPeriodBean.getHolidays()[count])) {
                        fbsBudgetPeriod.setHolidays(Integer.parseInt(fbsBudgetPeriodBean.getHolidays()[count]));
                    }
                    if (!EmptyUtils.isEmpty(fbsBudgetPeriodBean.getWorkdays()[count])) {
                        fbsBudgetPeriod.setWorkdays(Integer.parseInt(fbsBudgetPeriodBean.getWorkdays()[count]));
                    }
                    fbsBudgetPeriod.setWeekNo(count);
                    fbsBudgetPeriod.setRmk(fbsBudgetPeriodBean.getRmk()[count]);
                    fbsBudgetPeriod.setCreateDate(new Date());
                    fbsBudgetPeriod.setCreateBy(user.getLoginName());
                    this.saveEntity(fbsBudgetPeriod);
                }
            }
        } catch (Exception e) {
            throw new ParameterException(FbsBudgetPeriodMessageEnum.FBS_BUDGET_PERIOD_MESSAGE_SAVE);
        }
    }
    /**
     * 根据设定时间和周期类型获取当前时间对应的本期周期
     *
     * @param date     时间
     * @param timeType 计划周期类型
     * @return
     */
    public FbsBudgetPeriodVO getCurBudgetPeriodByTimeType(Date date, String timeType) {
        FbsBudgetPeriodVO vo = new FbsBudgetPeriodVO();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        /**如果没有日期，默认为当前日期*/
        if (null == date) {
            date = new Date();
        }
        /**如果没有显示周期，默认为本期*/
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        calendar.setTime(date);
        Date beginDate = new Date();
        Date endDate = new Date();
        try {
            /**年预算*/
            if (BudgetConstant.TIME_TYPE_YEAR.equals(timeType)) {
                int year = calendar.get(Calendar.YEAR);//得到年
                beginDate = formatter.parse(year + "-01-01");
                endDate = formatter.parse(year + "-12-31");
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                /**季预算*/
            } else if (BudgetConstant.TIME_TYPE_QUARTER.equals(timeType)) {
                int year = calendar.get(Calendar.YEAR);//得到年
                int month = calendar.get(Calendar.MONTH);//得到月
                if (month >= 0 && month <= 2) {//第一季度
                    beginDate = formatter.parse(year + "-01-01");
                    endDate = formatter.parse(year + "-03-31");
                } else if (month >= 3 && month <= 5) {//第二季度
                    beginDate = formatter.parse(year + "-04-01");
                    endDate = formatter.parse(year + "-06-30");
                } else if (month >= 6 && month <= 8) {//第三季度
                    beginDate = formatter.parse(year + "-07-01");
                    endDate = formatter.parse(year + "-09-30");
                } else {//第四季度
                    beginDate = formatter.parse(year + "-10-01");
                    endDate = formatter.parse(year + "-12-31");
                }
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                /**月预算*/
            } else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);//设置本月第一天
                beginDate = formatter.parse(DateUtils.formatDate(calendar));
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)); //设置本月最后一天
                endDate = formatter.parse(DateUtils.formatDate(calendar));
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                /**周预算*/
            } else if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType)) {
                FbsBudgetPeriod fbsBudgetPeriod = fbsBudgetPeriodDao.getBy(date);
                if (null != fbsBudgetPeriod) {
                    BeanUtils.copyNotNullProperties(fbsBudgetPeriod, vo);
                } else {
                    setPerviodVO(formatter.parse(DateUtils.formatDate(calendar)), vo);
                }
                /**天预算*/
            } else if (BudgetConstant.TIME_TYPE_DAY.equals(timeType)) {
                beginDate = formatter.parse(DateUtils.formatDate(calendar));
                endDate = formatter.parse(DateUtils.formatDate(calendar));
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(FbsBudgetPeriodMessageEnum.FBS_BUDGET_TIME_INTERFACE_ERROR);
        }
        return vo;
    }
    /**
     * 根据设定时间和周期类型获取当前时间对应的下期周期
     *
     * @param date     时间
     * @param timeType 计划周期类型
     * @return
     */
    public FbsBudgetPeriodVO getNextBudgetPeriodByTimeType(Date date, String timeType) {
        FbsBudgetPeriodVO vo = new FbsBudgetPeriodVO();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        /**如果没有日期，默认为当前日期*/
        if (null == date) {
            date = new Date();
        }
        /**如果没有显示周期，默认为本期*/
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        calendar.setTime(date);
        Date beginDate = new Date();
        Date endDate = new Date();
        try {
            /**年预算*/
            if (BudgetConstant.TIME_TYPE_YEAR.equals(timeType)) {
                int year = calendar.get(Calendar.YEAR);//得到年
                year = year + 1;//下一年
                beginDate = formatter.parse(year + "-01-01");
                endDate = formatter.parse(year + "-12-31");
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                /**季预算*/
            } else if (BudgetConstant.TIME_TYPE_QUARTER.equals(timeType)) {
                int year = calendar.get(Calendar.YEAR);//得到年
                int month = calendar.get(Calendar.MONTH);//得到月
                month = month + 3;//下一季
                if (month > 11) {
                    month = month - 11;
                    year = year + 1;
                }
                if (month >= 0 && month <= 2) {//第一季度
                    beginDate = formatter.parse(year + "-01-01");
                    endDate = formatter.parse(year + "-03-31");
                } else if (month >= 3 && month <= 5) {//第二季度
                    beginDate = formatter.parse(year + "-04-01");
                    endDate = formatter.parse(year + "-06-30");
                } else if (month >= 6 && month <= 8) {//第三季度
                    beginDate = formatter.parse(year + "-07-01");
                    endDate = formatter.parse(year + "-09-30");
                } else {//第四季度
                    beginDate = formatter.parse(year + "-10-01");
                    endDate = formatter.parse(year + "-12-31");
                }
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                /**月预算*/
            } else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
                calendar.add(Calendar.MONTH, 1);//加一个月
                calendar.set(Calendar.DAY_OF_MONTH, 1);//设置本月第一天
                beginDate = formatter.parse(DateUtils.formatDate(calendar));
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)); //设置本月最后一天
                endDate = formatter.parse(DateUtils.formatDate(calendar));
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                /**周预算*/
            } else if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType)) {
                FbsBudgetPeriodVO thisPeriodVO = this.getCurBudgetPeriodByTimeType(calendar.getTime(), timeType);
                calendar.setTime(thisPeriodVO.getEndDate());
                calendar.add(Calendar.DATE, 1);//本周最后一天加一天为下周第一天
                FbsBudgetPeriod fbsBudgetPeriod = fbsBudgetPeriodDao.getBy(calendar.getTime());
                if (null != fbsBudgetPeriod) {
                    BeanUtils.copyNotNullProperties(fbsBudgetPeriod, vo);
                } else {
                    setPerviodVO(formatter.parse(DateUtils.formatDate(calendar)), vo);
                }
                /**天预算*/
            } else if (BudgetConstant.TIME_TYPE_DAY.equals(timeType)) {
                calendar.add(Calendar.DATE, 1);//加1天一周
                beginDate = formatter.parse(DateUtils.formatDate(calendar));
                endDate = formatter.parse(DateUtils.formatDate(calendar));
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(FbsBudgetPeriodMessageEnum.FBS_BUDGET_TIME_INTERFACE_ERROR);
        }
        return vo;
    }
    /**
     * 根据年,月自动计算这一月的自然周
     *
     * @param date
     */
    public List<FbsBudgetPeriodVO> autoAenerateByMonth(Date date) {
        List<FbsBudgetPeriodVO> voList = new ArrayList<FbsBudgetPeriodVO>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (null == date) {
            date = DateUtils.getDate();
        }
        try {
            Date monthBeginDate = new Date();
            Date monthEndDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
            c.setTime(date);
            c.set(Calendar.DAY_OF_MONTH, 1);//设置本月第一天
            monthBeginDate = dateFormat.parse(DateUtils.formatDate(c));
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE)); //设置本月最后一天
            monthEndDate = dateFormat.parse(DateUtils.formatDate(c));
            List<FbsBudgetPeriod> list = fbsBudgetPeriodDao.getByForList(monthBeginDate, monthEndDate);
            if (null != list && list.size() > 0) {
                voList = BeanUtils.copyListProperties(list, FbsBudgetPeriodVO.class);
            } else {
                autoAenerateByDate(monthBeginDate, monthEndDate, voList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voList;
    }
    /**
     * 自动计算自然周，根据开始时间与结束时间
     *
     * @param beginDate
     * @param endDate
     * @param voList
     */
    public void autoAenerateByDate(Date beginDate, Date endDate, List<FbsBudgetPeriodVO> voList) {
        int week = 1;
        Map map = new HashMap();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date cycleEndDate = endDate;
        try {
            while (cycleEndDate.getTime() >= beginDate.getTime()) {
                FbsBudgetPeriodVO vo = new FbsBudgetPeriodVO();
                vo.setYear(String.valueOf(DateUtils.getYear(beginDate)));//年
                vo.setBeginDate(beginDate);//开始时间
                Calendar calendar = Calendar.getInstance();
                Calendar calendar1 = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
                calendar.setTime(beginDate);
                calendar1.setTime(endDate);
                int beginweek = calendar.get(Calendar.DAY_OF_WEEK);
                calendar1.set(Calendar.DAY_OF_MONTH, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));//设置本月最后一天
                if (calendar.get(Calendar.WEEK_OF_MONTH) == 1) {
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    endDate = dateFormat.parse(DateUtils.formatDate(calendar));
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, 6);
                    endDate = dateFormat.parse(DateUtils.formatDate(calendar));
                    if (calendar.compareTo(calendar1) == 1) {
                        endDate = dateFormat.parse(DateUtils.formatDate(calendar1));
                    }
                }
                calendar1.setTime(endDate);
                calendar1.set(Calendar.DAY_OF_MONTH, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));//设置本月最后一天
                vo.setEndDate(endDate);//结束时间
                Long weekdays = (endDate.getTime() - beginDate.getTime()) / (24 * 3600 * 1000);
                vo.setWeekdays(weekdays.intValue() + 1);//设置周天数
                //得到工作日
                CalendarDTO calendarDTO = calendarService.getWorkDayByTime(vo.getBeginDate(), vo.getEndDate(), true);
                vo.setWorkdays(calendarDTO.getWorkDays().intValue());
                vo.setHolidays(vo.getWeekdays() - vo.getWorkdays());
                if (calendar.compareTo(calendar1) >= 0) {
                    calendar1.add(Calendar.DATE, 1);
                    beginDate = dateFormat.parse(DateUtils.formatDate(calendar1));
                    if (beginDate.getMonth() != 0) {
                        week = 1;
                    } else {
                        Object obj = map.get(beginDate.getMonth() + 1);
                        if (null != obj) {
                            week = (Integer) obj;
                        }
                    }
                } else {
                    calendar.add(Calendar.DATE, 1);
                    beginDate = dateFormat.parse(DateUtils.formatDate(calendar));
                    week++;
                }
                vo.setMonth(beginDate.getMonth() + 1);//设置月
                map.put(vo.getMonth(), week);
                vo.setRmk("");
                voList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < voList.size(); i++) {
            voList.get(i).setWeek((Integer) map.get(voList.get(i).getMonth()));
        }
    }
    /**
     * 自动计算标准周，根据开始时间与结束时间
     *
     * @param beginDate
     * @param endDate
     * @param voList
     */
    public void autoStandardByDate(Date beginDate, Date endDate, List<FbsBudgetPeriodVO> voList) {
        int week = 1;//每月4周
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date cycleEndDate = endDate;
        try {
            while (cycleEndDate.getTime() >= beginDate.getTime()) {
                FbsBudgetPeriodVO vo = new FbsBudgetPeriodVO();
                vo.setYear(String.valueOf(DateUtils.getYear(beginDate)));//年
                vo.setBeginDate(beginDate);//开始时间
                Calendar calendar = Calendar.getInstance();
                Calendar calendar1 = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
                calendar.setTime(beginDate);
                calendar1.setTime(beginDate);
                calendar1.set(Calendar.DAY_OF_MONTH, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));//设置本月最后一天
                if (week < 4) {
                    calendar.add(Calendar.DAY_OF_MONTH, 6);
                    endDate = dateFormat.parse(DateUtils.formatDate(calendar));
                    week++;
                } else {
                    endDate = dateFormat.parse(DateUtils.formatDate(calendar1));
                    calendar = calendar1;
                    week = 1;
                }
                vo.setEndDate(endDate);//结束时间
                Long weekdays = (endDate.getTime() - beginDate.getTime()) / (24 * 3600 * 1000);
                vo.setWeekdays(weekdays.intValue() + 1);//设置周天数
                //得到工作日
                CalendarDTO calendarDTO = calendarService.getWorkDayByTime(vo.getBeginDate(), vo.getEndDate(), true);
                vo.setWorkdays(calendarDTO.getWorkDays().intValue());
                vo.setHolidays(vo.getWeekdays() - vo.getWorkdays());
                if (calendar.compareTo(calendar1) >= 0) {
                    calendar1.add(Calendar.DATE, 1);
                    beginDate = dateFormat.parse(DateUtils.formatDate(calendar1));
                } else {
                    calendar.add(Calendar.DATE, 1);
                    beginDate = dateFormat.parse(DateUtils.formatDate(calendar));
                }
                vo.setWeek(4);
                vo.setMonth(DateUtils.getMonth(beginDate));//设置月
                vo.setRmk("");
                voList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据时间，自动计算自然周的开始时间结束时间
     *
     * @param data
     * @param vo
     */
    public void setPerviodVO(Date data, FbsBudgetPeriodVO vo) {
        /**先得到当月的周周期*/
        List<FbsBudgetPeriodVO> list = this.autoAenerateByMonth(data);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBeginDate().getTime() <= data.getTime() && data.getTime() <= list.get(i).getEndDate().getTime()) {
                BeanUtils.copyNotNullProperties(list.get(i), vo);
            }
        }
    }
    /**
     * 根据设定时间和周期类型获取当前时间对应的上期周期
     *
     * @param date     时间
     * @param timeType 计划周期类型
     * @return
     */
    public FbsBudgetPeriodVO getSuperBudgetPeriodByTimeType(Date date, String timeType) {
        FbsBudgetPeriodVO vo = new FbsBudgetPeriodVO();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        /**如果没有日期，默认为当前日期*/
        if (null == date) {
            date = new Date();
        }
        /**如果没有显示周期，默认为本期*/
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        calendar.setTime(date);
        Date beginDate = new Date();
        Date endDate = new Date();
        try {
            /**年预算*/
            if (BudgetConstant.TIME_TYPE_YEAR.equals(timeType)) {
                int year = calendar.get(Calendar.YEAR);//得到年
                year = year - 1;//上一年
                beginDate = formatter.parse(year + "-01-01");
                endDate = formatter.parse(year + "-12-31");
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                /**季预算*/
            } else if (BudgetConstant.TIME_TYPE_QUARTER.equals(timeType)) {
                int year = calendar.get(Calendar.YEAR);//得到年
                int month = calendar.get(Calendar.MONTH);//得到月
                month = month - 3;//上一季
                if (month >= 0 && month <= 2) {//第一季度
                    beginDate = formatter.parse(year + "-01-01");
                    endDate = formatter.parse(year + "-03-31");
                } else if (month >= 3 && month <= 5) {//第二季度
                    beginDate = formatter.parse(year + "-04-01");
                    endDate = formatter.parse(year + "-06-30");
                } else if (month >= 6 && month <= 8) {//第三季度
                    beginDate = formatter.parse(year + "-07-01");
                    endDate = formatter.parse(year + "-09-30");
                } else {//第四季度
                    beginDate = formatter.parse(year + "-10-01");
                    endDate = formatter.parse(year + "-12-31");
                }
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                /**月预算*/
            } else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
                calendar.add(Calendar.MONTH, -1);//减一个月
                calendar.set(Calendar.DAY_OF_MONTH, 1);//设置本月第一天
                beginDate = formatter.parse(DateUtils.formatDate(calendar));
                ;
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)); //设置本月最后一天
                endDate = formatter.parse(DateUtils.formatDate(calendar));
                ;
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                /**周预算*/
            } else if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType)) {
                calendar.add(Calendar.DATE, -7);//减7天一周
                FbsBudgetPeriod fbsBudgetPeriod = fbsBudgetPeriodDao.getBy(calendar.getTime());
                if (null != fbsBudgetPeriod) {
                    BeanUtils.copyNotNullProperties(fbsBudgetPeriod, vo);
                } else {
                    setPerviodVO(formatter.parse(DateUtils.formatDate(calendar)), vo);
                }
                /**天预算*/
            } else if (BudgetConstant.TIME_TYPE_DAY.equals(timeType)) {
                calendar.add(Calendar.DATE, -1);//减1天一周
                beginDate = formatter.parse(DateUtils.formatDate(calendar));
                endDate = formatter.parse(DateUtils.formatDate(calendar));
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(FbsBudgetPeriodMessageEnum.FBS_BUDGET_TIME_INTERFACE_ERROR);
        }
        return vo;
    }
    /**
     * 根据时间得到这个月的周
     *
     * @param date
     * @return
     */
    public List<FbsBudgetPeriodVO> getWeekByMonth(Date date) {
        List<FbsBudgetPeriodVO> voList = new ArrayList<FbsBudgetPeriodVO>();
        if (null == date) {
            date = DateUtils.getDate();
        }
        Date monthBeginDate = new Date();
        Date monthEndDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置本月第一天
        monthBeginDate = c.getTime();
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE)); //设置本月最后一天
        monthEndDate = c.getTime();
        //先查询数据库看是否有自定义周
        List<FbsBudgetPeriod> list = fbsBudgetPeriodDao.getByForList(monthBeginDate, monthEndDate);
        if (null != list && list.size() > 0) {
            voList = BeanUtils.copyListProperties(list, FbsBudgetPeriodVO.class);
        } else {
            autoAenerateByDate(monthBeginDate, monthEndDate, voList);
        }
        return voList;
    }
    /**
     * 根据年得到月周期
     *
     * @param date
     * @return
     */
    public List<FbsBudgetPeriodVO> getMonthByYear(Date date) {
        List<FbsBudgetPeriodVO> voList = new ArrayList<FbsBudgetPeriodVO>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (null == date) {
            date = DateUtils.getDate();
        }
        /**如果没有显示周期，默认为本期*/
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        calendar.setTime(date);
        Date yearBeginDate = new Date();
        Date yearEndDate = new Date();
        Date beginDate = new Date();
        Date endDate = new Date();
        try {
            int year = calendar.get(Calendar.YEAR);//得到年
            yearBeginDate = formatter.parse(year + "-01-01");
            yearEndDate = formatter.parse(year + "-12-31");
            while (yearBeginDate.getTime() < yearEndDate.getTime()) {
                FbsBudgetPeriodVO vo = new FbsBudgetPeriodVO();
                calendar.setTime(yearBeginDate);
                calendar.set(Calendar.DAY_OF_MONTH, 1);//设置月第一天
                beginDate = formatter.parse(DateUtils.formatDate(calendar));
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)); //设置月最后一天
                endDate = formatter.parse(DateUtils.formatDate(calendar));
                vo.setBeginDate(beginDate);
                vo.setEndDate(endDate);
                voList.add(vo);
                calendar.add(Calendar.MONTH, 1);
                yearBeginDate = calendar.getTime();//加一个月
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voList;
    }
    /**
     * 根据开始时间得到此周为本月第几周
     *
     * @param beginDate
     * @return
     */
    public int getWeekNoByTime(Date beginDate) {
        int result = 0;
        int i = 1;
        List<FbsBudgetPeriodVO> monthPeriodVOList = this.getWeekByMonth(beginDate);
        for (FbsBudgetPeriodVO vo : monthPeriodVOList) {
            if (vo.getBeginDate().getTime() <= beginDate.getTime() &&
                    vo.getEndDate().getTime() >= beginDate.getTime()) {
                result = i;
                break;
            }
            i++;
        }
        return result;
    }
    /**
     * 得到工作日
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public Integer getWorkDays(String beginDate, String endDate) {
        Integer result = 0;
        //得到工作日
        CalendarDTO dto = calendarService.getWorkDayByTime(beginDate, endDate, true);
        result = dto.getWorkDays().intValue();
        return result;
    }
}
