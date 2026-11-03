package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillBalCompareQueryDao;
import com.bytter.bill.domain.bill.vo.AbBillBalCompareDetailedVO;
import com.bytter.bill.domain.bill.vo.AbBillBalCompareVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Service
 * @Description: 票据余额对比分析业务处理类
 * @date 2020-01-16
 */
@SuppressWarnings("rawtypes")
@Service("abBillBalCompareQueryService")
@Transactional
public class AbBillBalCompareQueryService extends BaseService {
    @Autowired
    private AbBillBalCompareQueryDao abBillBalCompareQueryDao;
    @Autowired
    private AbCorpService abCorpService;
    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 票据余额对比分析数据
     *
     * @param input
     * @return
     * @throws ParseException
     */
    public DataTablesOutput getDataTable(DataTablesInput input) throws ParseException {
        //外查询语句
        StringBuilder select = new StringBuilder("select d.corpId as corpId,s.name as corpName ");
        //内查询语句
        StringBuilder selectInside = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        //获取对比周期，根据周期组装sql
        String[] comparedCycle;
        if (!StringUtils.isNotEmpty(input.getFormdata().get("comparedCycle"))) {
            comparedCycle = new String[]{AccebillConstant.COMPARED_CYCLE_1, AccebillConstant.COMPARED_CYCLE_2, AccebillConstant.COMPARED_CYCLE_3,
                    AccebillConstant.COMPARED_CYCLE_4, AccebillConstant.COMPARED_CYCLE_5};
        } else {
            comparedCycle = String.valueOf(input.getFormdata().get("comparedCycle")).split(",");
        }
        //查询统计日期
        select.append(",sum(bal) as bal");
        selectInside.append(getSelectInside(input, null, params));
        for (String cycle : comparedCycle) {
            //较上月末
            if (AccebillConstant.COMPARED_CYCLE_1.equals(cycle)) {
                select.append(",sum(befMonthBal) as befMonthBal");
                selectInside.append(" union all ").append(getSelectInside(input, cycle, params));
            }
            //较上季度末
            if (AccebillConstant.COMPARED_CYCLE_2.equals(cycle)) {
                select.append(",sum(befQuarterBal) as befQuarterBal");
                selectInside.append(" union all ").append(getSelectInside(input, cycle, params));
            }
            //较三个月前
            if (AccebillConstant.COMPARED_CYCLE_3.equals(cycle)) {
                select.append(",sum(befThreeMonthBal) as befThreeMonthBal");
                selectInside.append(" union all ").append(getSelectInside(input, cycle, params));
            }
            //较上年同期
            if (AccebillConstant.COMPARED_CYCLE_4.equals(cycle)) {
                select.append(",sum(befYearBal) as befYearBal");
                selectInside.append(" union all ").append(getSelectInside(input, cycle, params));
            }
            //较年初
            if (AccebillConstant.COMPARED_CYCLE_5.equals(cycle)) {
                select.append(",sum(yearEarlyBal) as yearEarlyBal ");
                selectInside.append(" union all ").append(getSelectInside(input, cycle, params));
            }
        }
        //组装sql
        select.append(" from (").append(selectInside).append(" ) d ")
                .append(" left join sys_corp s on s.id=d.corpId  group by d.corpId,s.name order by s.name");
        String moneyUnit = (String) input.getFormdata().get("moneyUnit");//金额单位
        DataTablesOutput dto = abBillBalCompareQueryDao.getDataTable(input, params, select.toString());
        List<AbBillBalCompareVO> list = dto.getData();//当前结果
        //金额单位换算和计算差额和比例
        conversion(list, moneyUnit);
        return dto;
    }
    //获取对比周期查询语句
    public String getSelectInside(DataTablesInput input, String cycle, Map<String, Object> params) {
        //内查询语句
        StringBuilder selectInside = new StringBuilder();
        //查询显示字段
        StringBuilder select = new StringBuilder("select b.corp_Id as corpId");
        //查询表关联及条件
        StringBuilder insideFrom = new StringBuilder("from ab_voucherinfo_vice b ")
                .append("inner join ab_voucherinfo a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del')  ")
                .append("inner join ab_accrecords l on l.vvid=b.id and l.status=95  and l.opt_status=19  and l.brief_id in (9003,9010,9012,9019,6019,6001,6212)")
                .append("left join ab_accrecords l2 on l.id=l2.lastid and l2.status=95  and l2.opt_status=19  and l2.brief_id in (010,020,021,018,6004,6011)")
                .append("left join ab_accrecords l3 on l2.id=l3.lastid and l3.status=95  and l3.opt_status=19  and l3.brief_id in (011)")
                .append("where  b.type=1 ");
        //分组条件
        StringBuilder selectGroupBy = new StringBuilder(" group by b.corp_id ");
        //统计月份
        Date statisticsMonth = new Date();
        if (StringUtils.isNotEmpty(input.getFormdata().get("statisticsMonth"))) {
            statisticsMonth = DateUtils.str2Date(input.getFormdata().get("statisticsMonth").toString(), DateUtils.date_sdf());
        }
        //查询日期
        Date monthDate = new Date();
        //查询日期
        if (StringUtils.isEmpty(cycle)) {
            select.append(",sum(a.bill_money) as bal,0 as befMonthBal,0 as befQuarterBal,0 as befThreeMonthBal,0 as befYearBal,0 as yearEarlyBal ");
            monthDate = statisticsMonth;
        }
        //较上月末
        if (AccebillConstant.COMPARED_CYCLE_1.equals(cycle)) {
            select.append(",0 as bal,sum(a.bill_money) as befMonthBal,0 as befQuarterBal,0 as befThreeMonthBal,0 as befYearBal,0 as yearEarlyBal ");
            monthDate = getLastDayOfMonth(statisticsMonth);
        }
        //较上季度末
        if (AccebillConstant.COMPARED_CYCLE_2.equals(cycle)) {
            select.append(",0 as bal,0 as befMonthBal,sum(a.bill_money) as befQuarterBal,0 as befThreeMonthBal,0 as befYearBal,0 as yearEarlyBal ");
            Date sDate = DateUtils.addMonth(statisticsMonth, -3);
            monthDate = DateUtils.getLastQuarterDate(sDate);
        }
        //较三个月前
        if (AccebillConstant.COMPARED_CYCLE_3.equals(cycle)) {
            select.append(",0 as bal,0 as befMonthBal,0 as befQuarterBal,sum(a.bill_money) as befThreeMonthBal,0 as befYearBal,0 as yearEarlyBal ");
            monthDate = getDayOfLastThree(statisticsMonth);
        }
        //较上年同期
        if (AccebillConstant.COMPARED_CYCLE_4.equals(cycle)) {
            select.append(",0 as bal,0 as befMonthBal,0 as befQuarterBal,0 as befThreeMonthBal,sum(a.bill_money) as befYearBal,0 as yearEarlyBal ");
            monthDate = DateUtils.addYear(statisticsMonth, -1);
        }
        //较年初
        if (AccebillConstant.COMPARED_CYCLE_5.equals(cycle)) {
            select.append(",0 as bal,0 as befMonthBal,0 as befQuarterBal,0 as befThreeMonthBal,0 as befYearBal,sum(a.bill_money) as yearEarlyBal ");
            monthDate = DateUtils.getFirstYearDate(monthDate);
        }
        String selectDate = DateUtils.date2Str(monthDate, DateUtils.date_sdf());
        //查询条件
        //日期
        String dateNum = "monthDate" + cycle;
        params.put(dateNum, monthDate);
        StringBuilder selectWhere = new StringBuilder(" and (l.business_date<=:" + dateNum + " and (l2.id is null or (:" + dateNum + "<l2.business_date or (l2.brief_id=6011 and (l3.id is null or :" + dateNum + "<l3.business_date))))) ");
        //票据类型
        if (StringUtils.isNotEmpty(input.getFormdata().get("billType"))) {
            selectWhere.append(" and a.bill_type=" + input.getFormdata().get("billType").toString() + " ");
        }
        //流通介质
        if (StringUtils.isNotEmpty(input.getFormdata().get("billFlag"))) {
            selectWhere.append(" and a.bill_Flag=" + input.getFormdata().get("billFlag").toString() + " ");
        }
        List<String> corpAuthList = null;
        //取界面查询单位
        if (StringUtils.isNotEmpty(input.getFormdata().get("corpId"))) {
            corpAuthList = StringUtils.splitToList(",", input.getFormdata().get("corpId").toString());
        }
        //只查有权限的单位
        List<String> corpList = null;
        String corpSql=null;
        corpList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BALCOMPARE_QUERY, input.getCurrentUserId());
        corpSql = SqlUtil.getInSqlByParams(corpList, "b.corp_id", params);
        if(corpAuthList!=null && !corpAuthList.isEmpty()){
            corpSql += " and "+SqlUtil.getInSqlByParams(corpAuthList, "b.corp_id", params);
        }
        //组装sql
        selectInside.append(select).append(insideFrom).append(selectWhere).append(" and ").append(corpSql).append(selectGroupBy);
        return selectInside.toString();
    }
    /**
     * 金额单位换算
     */
    public List<AbBillBalCompareVO> conversion(List<AbBillBalCompareVO> list, String moneyType) {
        BigDecimal divisor = BigDecimal.ONE;
        //比例乘以100
        BigDecimal proportion = new BigDecimal(100);
        //保留小数位
        int scale = 2;
        if ("2".equals(moneyType)) {//千元
            divisor = new BigDecimal(1000);
        }
        if ("3".equals(moneyType)) {//万元
            divisor = new BigDecimal(10000);
        }
        if ("4".equals(moneyType)) {//亿元
            divisor = new BigDecimal(100000000);
        }
        for (AbBillBalCompareVO vo : list) {
            //余额
            vo.setBal(vo.getBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : vo.getBal().divide(divisor, scale, BigDecimal.ROUND_HALF_DOWN));
            //上月末余额
            vo.setBefMonthBal(vo.getBefMonthBal() == null || vo.getBefMonthBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : vo.getBefMonthBal().divide(divisor, scale, BigDecimal.ROUND_HALF_DOWN));
            //上月末差额
            vo.setBefMonthDifBal((vo.getBefMonthBal() == null ? BigDecimal.ZERO : vo.getBal().subtract(vo.getBefMonthBal())));
            //上月末比例
            vo.setBefMonthProportion(vo.getBefMonthDifBal().compareTo(BigDecimal.ZERO) == 0 || vo.getBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : (vo.getBefMonthDifBal().divide(vo.getBal(), 4, BigDecimal.ROUND_HALF_DOWN)).multiply(proportion));
            //三个月前余额
            vo.setBefThreeMonthBal(vo.getBefThreeMonthBal() == null || vo.getBefThreeMonthBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : vo.getBefThreeMonthBal().divide(divisor, scale, BigDecimal.ROUND_HALF_DOWN));
            //三个月前差额
            vo.setBefThreeMDifBal((vo.getBefThreeMonthBal() == null ? BigDecimal.ZERO : vo.getBal().subtract(vo.getBefThreeMonthBal())));
            //三个月前比例
            vo.setBefThreeMProportion(vo.getBefThreeMDifBal().compareTo(BigDecimal.ZERO) == 0 || vo.getBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : (vo.getBefThreeMDifBal().divide(vo.getBal(), 4, BigDecimal.ROUND_HALF_DOWN)).multiply(proportion));
            //上季度末余额
            vo.setBefQuarterBal(vo.getBefQuarterBal() == null || vo.getBefQuarterBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : vo.getBefQuarterBal().divide(divisor, scale, BigDecimal.ROUND_HALF_DOWN));
            //上季度末差额
            vo.setBefQuarterDifBal((vo.getBefQuarterBal() == null ? BigDecimal.ZERO : vo.getBal().subtract(vo.getBefQuarterBal())));
            //上季度末比例
            vo.setBefQuarterProportion(vo.getBefQuarterDifBal().compareTo(BigDecimal.ZERO) == 0 || vo.getBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : (vo.getBefQuarterDifBal().divide(vo.getBal(), 4, BigDecimal.ROUND_HALF_DOWN)).multiply(proportion));
            //上年同期余额
            vo.setBefYearBal(vo.getBefYearBal() == null || vo.getBefYearBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : vo.getBefYearBal().divide(divisor, scale, BigDecimal.ROUND_HALF_DOWN));
            //上年同期差额
            vo.setBefYearDifBal((vo.getBefYearBal() == null ? BigDecimal.ZERO : vo.getBal().subtract(vo.getBefYearBal())));
            //上年同期比例
            vo.setBefYearProportion(vo.getBefYearDifBal().compareTo(BigDecimal.ZERO) == 0 || vo.getBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : (vo.getBefYearDifBal().divide(vo.getBal(), 4, BigDecimal.ROUND_HALF_DOWN)).multiply(proportion));
            //年初余额
            vo.setYearEarlyBal(vo.getYearEarlyBal() == null || vo.getYearEarlyBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : vo.getYearEarlyBal().divide(divisor, scale, BigDecimal.ROUND_HALF_DOWN));
            //年初差额
            vo.setYearEarlyDifBal((vo.getYearEarlyBal() == null ? BigDecimal.ZERO : vo.getBal().subtract(vo.getYearEarlyBal())));
            //年初比例
            vo.setYearEarlyProportion(vo.getYearEarlyDifBal().compareTo(BigDecimal.ZERO) == 0 || vo.getBal().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : (vo.getYearEarlyDifBal().divide(vo.getBal(), 4, BigDecimal.ROUND_HALF_DOWN)).multiply(proportion));
        }
        return list;
    }
    /**
     * 票据余额对比分析数据明细
     *
     * @param input
     * @return
     * @throws ParseException
     */
    public DataTablesOutput listBalCompareDetailed(DataTablesInput input) {
        DataTablesOutput<AbBillBalCompareDetailedVO> dataTablesOutput = abBillBalCompareQueryDao.listBalCompareDetailed(input);
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_FLAG","AB_BILL_TYPE");
            for (AbBillBalCompareDetailedVO bill : listData){
                injectValues(bill, listDictMap);
            }
        });
        return dataTablesOutput;
    }
    private void injectValues(AbBillBalCompareDetailedVO bill, Map<String, Map<String, DictDTO>> listDictMap){
        // 票据类型：10 银票、11 商票
        Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> bill.setBillTypeName(dict.getName()));
        });
        // 流通介质：10 纸票、11 电票
        Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                    .ifPresent(dict -> bill.setBillFlagName(dict.getName()));
        });
    }
    /**
     * 上月末
     */
    public Date getLastDayOfMonth(Date time) {
        int year = DateUtils.getYear(time);
        int month = DateUtils.getMonth(time);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); //设置当前月的上一个月
        int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
    /**
     * 上月末
     */
    public Date assignSevenDay(Date time) {
        int year = DateUtils.getYear(time);
        int month = DateUtils.getMonth(time);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); //设置当前月的上一个月
        int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
    /**
     * 三个月前
     */
    public Date getDayOfLastThree(Date time) {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(time);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, -3); //设置为前3月
        return calendar.getTime();
    }
    public Date getLastDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 1;
        } else if (quarter == 2) {
            month = 3 - 1;
        } else if (quarter == 3) {
            month = 6 - 1;
        } else if (quarter == 4) {
            month = 9 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }
    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    /**
     * 指定日期到期票据金额
     *
     * @param userId
     * @param resource
     * @param date
     * @return
     */
    public BigDecimal appointeDayMoney(String userId, String resource, Date date) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = abCorpService.getUserCorpIds(resource, userId);
        if (CollectionUtils.isEmpty(corpAuthList)) {
            return BigDecimal.ZERO;
        }
        BigDecimal amountMoney = BigDecimal.ZERO;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            //获取到期票据数据
            params.put("timeBegin", DateUtils.format(DateUtils.getDateTime(),"yyyy-MM-dd"));
            params.put("timeEnd", DateUtils.format(DateUtils.addDay(DateUtils.getDate(), 6),"yyyy-MM-dd"));
            amountMoney = abBillBalCompareQueryDao.appointeDayMoney(corpAuthList, params);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return BigDecimal.ZERO;
        }
        return amountMoney;
    }
    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public Date getFetureDate(int past, Date refDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        Date today = calendar.getTime();
        return today;
    }
    /**
     * 获取今天日期0：00：00
     *
     * @param past
     * @return
     */
    public Date getCurrenDate(Date refDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    @Override
    public BaseDao getEntityDao() {
        return abBillBalCompareQueryDao;
    }
}
