package com.bytter.report.domain.cms.utils;

import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CmsConstant;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luxy
 * @Title: LoanDailyAnalysisUtils
 * @ProjectName T3(3.2)
 * @Description: 日均贷款分析报表工具类
 * @date 2020/3/1 23:51
 */
public class LoanDailyAnalysisUtils {

    /**
     * 计算查询时间之间的间隔天数
     * @param startDate 输入查询开始日期
     * @param endDate 输入查询结束日期
     * @param searchFrequency 查询频率 0-按天查询 1-按月查询
     * @return
     */
    public  static Map<String,Object> getDateMap(String startDate,String endDate,String searchFrequency){
        Map<String,Object> dateMap=new HashMap<>();
        Date searchStartDate=null;//查询开始日期
        Date searchEndDate=null;//查询结束日期
        Date preStartDate=null;//上期查询开始日期
        Date preEndDate=null;//上期查询结束日期
        Date lastStartDate=null;//上年同期查询开始日期
        Date lastEndDate=null;//上年同期查询结束日期
        int days=0;
        int preDays=0;
        if(null!=searchFrequency){
            if (searchFrequency.equals(CmsConstant.SEARCH_BY_DAY)) {//按天查询时直接计算两个日期之间相差天数
                searchStartDate= DateUtils.str2Date(startDate,DateUtils.date_sdf());//查询开始日期
                searchEndDate= DateUtils.str2Date(endDate,DateUtils.date_sdf());//查询截止日期
                int diffDays=DateUtils.dayDiffs(searchStartDate,searchEndDate)-1;//查询日期之间相差的天数
                preStartDate=DateUtils.addDay(searchStartDate,diffDays);
                preEndDate=DateUtils.addDay(searchEndDate,diffDays);
                lastEndDate=DateUtils.addMonth(searchEndDate,-12);//上年同期
            } else {//按月查询需要转换时间
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM");
                Calendar bef = Calendar.getInstance();
                Calendar aft = Calendar.getInstance();
                try {
                    bef.setTime(sdf.parse(startDate));
                    aft.setTime(sdf.parse(endDate));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);//相差月数
                int months = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;//计算跨年的时候相差月数
                int monthsDiff=result+months;
                dateMap.put("monthsDiff",monthsDiff+1);
                startDate = startDate + "-01";
                endDate = endDate + "-01";
                searchStartDate= DateUtils.str2Date(startDate,DateUtils.date_sdf());//查询开始日期
                Date estiSearchEndDate=DateUtils.getLastMonthDate(DateUtils.str2Date(endDate,DateUtils.date_sdf()));//预计查询截止日期(取查询结束所在月的最后一天)
                int dayDiff=DateUtils.dayDiffs(DateUtils.getMonthEndTime(),estiSearchEndDate);
                if(dayDiff==0){//如果预计查询结束日期的当月最后一天和系统当前时间月份的最后一天为同一天，则截止查询日期取系统当前日期
                    searchEndDate=DateUtils.getDate();
                }else{
                    searchEndDate=estiSearchEndDate;
                }
                preStartDate=DateUtils.addMonth(searchStartDate,-(monthsDiff+1));
                preEndDate=DateUtils.addMonth(estiSearchEndDate,-(monthsDiff+1));
                lastEndDate=DateUtils.getLastMonthDate(DateUtils.addMonth(estiSearchEndDate,-12));//指定日期的当月最后一天

            }
        }
        lastStartDate=DateUtils.addMonth(searchStartDate,-12);
        if(searchStartDate.equals(searchEndDate)){//开始日期和结束日期为同一天
            days=1;
        }else{
            days =DateUtils.getOffsetDays(searchStartDate,searchEndDate)+1;//查询日期之间相差的天数
        }

        if(preStartDate.equals(preEndDate)){//开始日期和结束日期为同一天
            preDays=1;
        }else{
            preDays =DateUtils.getOffsetDays(preStartDate,preEndDate)+1;//查询日期之间相差的天数
        }

        dateMap.put("searchStartDate",searchStartDate);
        dateMap.put("searchEndDate",searchEndDate);
        dateMap.put("preStartDate",preStartDate);
        dateMap.put("preEndDate",preEndDate);
        dateMap.put("lastStartDate",lastStartDate);
        dateMap.put("lastEndDate",lastEndDate);
        dateMap.put("dayDiffs",days);
        dateMap.put("preDayDiffs",preDays);
        return dateMap;
    }

    /**
     * 查询开始时间和结束时间
     * @param startDate
     * @param endDate
     * @param searchFrequency
     * @return
     */
    public  static Map<String,Object> getSerarchDateMap(String startDate,String endDate,String searchFrequency){
        Map<String,Object> dateMap=new HashMap<>();
        Date searchEndDate=null;//查询结束日期
        int days=1;
        if(null!=searchFrequency){
            if (searchFrequency.equals(CmsConstant.SEARCH_BY_DAY)) {//按天查询时直接计算两个日期之间相差天数
                searchEndDate= DateUtils.str2Date(endDate,DateUtils.date_sdf());//查询截止日期
            } else {//按月查询需要转换时间
                if(!StringUtils.isBlank(startDate)){
                    startDate = startDate + "-01";
                }
                endDate = endDate + "-01";
                Date estiSearchEndDate=DateUtils.getLastMonthDate(DateUtils.str2Date(endDate,DateUtils.date_sdf()));//预计查询截止日期(取查询结束所在月的最后一天)
                int dayDiff=DateUtils.dayDiffs(DateUtils.getMonthEndTime(),estiSearchEndDate);
                if(dayDiff==0){//如果预计查询结束日期的当月最后一天和系统当前时间月份的最后一天为同一天，则截止查询日期取系统当前日期
                    searchEndDate=DateUtils.getDate();
                }else{
                    searchEndDate=estiSearchEndDate;
                }
                if(!StringUtils.isBlank(startDate)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar bef = Calendar.getInstance();
                    Calendar aft = Calendar.getInstance();
                    try {
                        bef.setTime(sdf.parse(startDate));
                        aft.setTime(sdf.parse(endDate));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);//相差月数
                    int months = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;//计算跨年的时候相差月数
                    int monthsDiff = result + months;
                    dateMap.put("monthsDiff", monthsDiff + 1);
                }
            }
        }
        if(!StringUtils.isBlank(startDate)){
            Date searchStartDate= DateUtils.str2Date(startDate,DateUtils.date_sdf());//查询开始日期
            dateMap.put("searchStartDate",searchStartDate);
            if(searchStartDate.equals(searchEndDate)){//开始日期和结束日期为同一天
                days=1;
            }else{
                days =DateUtils.getOffsetDays(searchStartDate,searchEndDate)+1;//查询日期之间相差的天数
            }

        }
        dateMap.put("dayDiffs",days);
        dateMap.put("searchEndDate",searchEndDate);
        return dateMap;
    }

}
