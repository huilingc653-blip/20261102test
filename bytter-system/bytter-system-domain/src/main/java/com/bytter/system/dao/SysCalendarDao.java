package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.MapUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.dto.SysCalendarDTO;
import com.bytter.system.entity.SysCalendar;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: SYS_CALENDAR
 * @date 2017-07-17 16:13:12
 */
@Repository
public class SysCalendarDao extends BaseDao<SysCalendar, String> {
    public List<SysCalendar> getSysCalendarByDates(Date startTime, Date endTime, Boolean status, String type, String typeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer("from SysCalendar where createDate >= :startTime and createDate <= :endTime");
        if (type == null) {
            sb.append(" and countryId is null ");
            sb.append(" and curId is null ");
        } else if (CommonConstant.NO.equals(type)) {
            sb.append(" and countryId = :typeId");
            sb.append(" and curId is null ");
        } else {
            sb.append(" and curId = :typeId");
            sb.append(" and countryId is null ");
        }
        if (status != null) {
            sb.append(" and workDaySign=:status ");
            params.put("status", status ? 1 : 0);
        }
        sb.append(" order by createDate asc,id");
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("typeId", typeId);
        return listByHQL(sb.toString(), params);
    }

    public List<SysCalendar> getSysCalendarByDates(Date startTime, Date endTime, Boolean status) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer("from SysCalendar where createDate >= :startTime and createDate <= :endTime");
        if (status != null) {
            sb.append(" and workDaySign=:status ");
            params.put("status", status ? 1 : 0);
        }
        //过滤地区和币种为空的
        sb.append(" and countryId is null");
        sb.append(" and curId is null");
        sb.append(" order by createDate asc");
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return listByHQL(sb.toString(), params);
    }

    public List<SysCalendar> getLatelyCalendarByDates(Date startTime, boolean status, Integer first, Integer maxResult) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer("from SysCalendar c where c.createDate >= :startTime and c.workDaySign=:status ");
        sb.append(" order by createDate asc");
        params.put("startTime", startTime);
        params.put("status", status ? 1 : 0);
        return listByHQL(sb.toString(), first, maxResult, params);
    }

    //取地点为空、币别为空的日历维护
    public List<SysCalendar> getLatelyCalendarByDatesByBIll(Date startTime, boolean status, Integer first, Integer maxResult) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer("from SysCalendar c where c.createDate >= :startTime and c.workDaySign=:status ");
        sb.append(" and countryId is null and c.curId is null ");
        sb.append(" order by createDate asc");
        params.put("startTime", startTime);
        params.put("status", status ? 1 : 0);
        return listByHQL(sb.toString(), first, maxResult, params);
    }

    /**
     * 根据条件获取日历数据
     *
     * @param searchBean 封装条件
     * @return SysCalendar
     */
    public List<SysCalendar> listBySearchBean(SysCalendar searchBean) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer("from SysCalendar s where 1=1 ");
        sb.append(" and (s.countryId=:countryId or s.curId=:curId)");
        sb.append(" and s.createDate=:createDate");
        params.put("countryId", searchBean.getCountryId());
        params.put("curId", searchBean.getCurId());
        params.put("createDate", searchBean.getCreateDate());
        return listByHQLAliasToBean(sb.toString(), params, SysCalendar.class);
    }

    public Integer getWorkDaySignByTime(Date time) {
        String sql = "select c.work_day_sign as workDaySign from SYS_CALENDAR c where c.country_id is null and c.cur_id is null and c.create_date=bt_to_date('" + StringUtil.formatDate4(time) + "','yyyy-MM-dd')";
        List<Map<String, Object>> objStr = this.listBySQL(sql, null);
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;
                Object workDaySign = MapUtils.getMapValueIgnoreCase(ary, "workDaySign");
                if (workDaySign != null ) {
                    return Integer.valueOf(workDaySign.toString());
                }
            }
        }
        return null;
    }

    /**
     * 获取往前或往后指定天数的工作日
     *
     * @param startDate 开始日期：开始日期不为空，则往后查询指定天数的工作日
     * @param endDate   结束日期：结束日期不为空，则往前查询指定天数的工作日
     * @param days      指定天数
     * @return 工作日集合
     */
    public List<SysCalendarDTO> listSysCalendarByDates(Date startDate, Date endDate, Integer days) {
        Map<String, Object> params = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" select * from SYS_CALENDAR sc ");
        // 地点与币别都为空
        sql.append(" where sc.COUNTRY_ID is null and sc.CUR_ID is null ");
        // 工作日
        sql.append(" and sc.WORK_DAY_SIGN = 1 ");
        if (null != startDate) {
            // 开始日期不为空，则往后查询指定天数的工作日
            sql.append(" and BT_TO_CHAR(sc.CREATE_DATE, 'yyyy-MM-dd') > :startDateStr ");
            params.put("startDateStr", DateUtils.format(startDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
            // 正序排序取提前天数条数
            sql.append(" order by sc.CREATE_DATE limit :days ");
        }
        if (null != endDate) {
            // 结束日期不为空，则往前查询指定天数的工作日
            sql.append(" and BT_TO_CHAR(sc.CREATE_DATE, 'yyyy-MM-dd') < :endDateStr ");
            params.put("endDateStr", DateUtils.format(endDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
            // 倒序排序取提前天数条数
            sql.append(" order by sc.CREATE_DATE desc limit :days ");
        }
        params.put("days", days);
        return this.listBySQLAliasToBean(sql.toString(), params, SysCalendarDTO.class);
    }
}
