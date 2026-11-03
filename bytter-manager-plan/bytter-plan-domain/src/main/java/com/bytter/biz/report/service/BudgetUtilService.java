package com.bytter.biz.report.service;
import com.bytter.biz.budget.dao.FbsBudgetPeriodDao;
import com.bytter.biz.budget.entity.FbsBudgetPeriod;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.utils.BudgetUtils;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 资金计划上报报表
 *
 * @author zuoy 2018-02-06
 */
@Service("budgetUtilService")
@Transactional
public class BudgetUtilService {
    @DubboReference(check = false)
	private IConfigService configService;
    @Autowired
    private FbsBudgetPeriodService fbsBudgetPeriodService;
    @DubboReference(check = false)
	private IDictService dictService;
    @Autowired
    private FbsBudgetPeriodDao fbsBudgetPeriodDao;
    private static final String FORMAT = "yyyy-MM-dd";
    public String getFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(date);
    }
    /**
     * 判断是否启用周期类型
     *
     * @return
     */
    public List<Map<String, Object>> getTimeType() {
        //是否有年计划及控制：0-没有 1-有并控制子周期计划 2-有但控制子周期计划
        String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
        String qcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_QCRL).getValue();
        String mcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MCRL).getValue();
        String wcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_WCRL).getValue();
        String dcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_DCRL).getValue();
        List<Map<String, Object>> jsonArray = new ArrayList<Map<String, Object>>();
        if (!BudgetConstant.CONFIG_CRL_0.equals(ycrl)) {
            jsonArray.add(getJsonObj(dictService.get("fbs.timeType",BudgetConstant.TIME_TYPE_YEAR), BudgetConstant.TIME_TYPE_YEAR));
        }
        if (!BudgetConstant.CONFIG_CRL_0.equals(qcrl)) {
            jsonArray.add(getJsonObj(dictService.get("fbs.timeType",BudgetConstant.TIME_TYPE_QUARTER), BudgetConstant.TIME_TYPE_QUARTER));
        }
        if (!BudgetConstant.CONFIG_CRL_0.equals(mcrl)) {
            jsonArray.add(getJsonObj(dictService.get("fbs.timeType",BudgetConstant.TIME_TYPE_MONTH), BudgetConstant.TIME_TYPE_MONTH));
        }
        if (!BudgetConstant.CONFIG_CRL_0.equals(wcrl)) {
            jsonArray.add(getJsonObj(dictService.get("fbs.timeType",BudgetConstant.TIME_TYPE_WEEK), BudgetConstant.TIME_TYPE_WEEK));
        }
        if (!BudgetConstant.CONFIG_CRL_0.equals(dcrl)) {
            jsonArray.add(getJsonObj(dictService.get("fbs.timeType",BudgetConstant.TIME_TYPE_DAY), BudgetConstant.TIME_TYPE_DAY));
        }
        return jsonArray;
    }
    public Map<String, Object> getJsonObj(String name, String value) {
        Map<String, Object> jsonObj = new HashMap<String, Object>();
        jsonObj.put("name", name);
        jsonObj.put("value", value);
        return jsonObj;
    }
    /**
     * 获取周期
     *
     * @param beginDate
     * @param endDate
     * @param timeType
     * @return
     */
    public Map<String, String> getPeriodByTimeType(Date beginDate, Date endDate, String timeType) {
        try {
            if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
                return getYearBetween(beginDate, endDate);
            } else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
                return getQuarterBetween(beginDate, endDate);
            } else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
                return getMonthBetween(beginDate, endDate);
            } else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
                return getWeekBetween(beginDate, endDate);
            } else {
                return getDayBetween(beginDate, endDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取年份集合
     *
     * @param minDate
     * @param maxDate
     * @return
     * @throws Exception
     */
    private Map<String, String> getYearBetween(Date minDate, Date maxDate) throws Exception {
        Map<String, String> result = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), 1, 1);
        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR) + 1, 1, 1);
        Calendar curr = min;
        while (curr.before(max)) {
            String beginDate = getFormat(BudgetUtils.getBeginDate(curr.getTime(), BudgetConstant.TIME_TYPE_YEAR));
            String endDate = getFormat(BudgetUtils.getEndDate(curr.getTime(), BudgetConstant.TIME_TYPE_YEAR));
            result.put(sdf.format(curr.getTime()), beginDate + "~" + endDate);
            curr.add(Calendar.YEAR, 1);
        }
        return result;
    }
    /**
     * 获取季度集合
     *
     * @param minDate
     * @param maxDate
     * @return
     * @throws Exception
     */
    private Map<String, String> getQuarterBetween(Date minDate, Date maxDate) throws Exception {
        Map<String, String> result = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(sdf.format(minDate)));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(sdf.parse(sdf.format(maxDate)));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH) + 3, 1);
        Calendar curr = min;
        while (curr.before(max)) {
            String key = DateUtils.getYear(curr.getTime()) + "年第" + DateUtils.getQuarterOfYear(curr.getTime()) + "季度";
            String beginDate = getFormat(BudgetUtils.getBeginDate(curr.getTime(), BudgetConstant.TIME_TYPE_QUARTER));
            String endDate = getFormat(BudgetUtils.getEndDate(curr.getTime(), BudgetConstant.TIME_TYPE_QUARTER));
            result.put(key, beginDate + "~" + endDate);
            curr.add(Calendar.MONTH, 3);
        }
        return result;
    }
    /**
     * 获取月份集合
     *
     * @param minDate
     * @param maxDate
     * @return
     * @throws Exception
     */
    private Map<String, String> getMonthBetween(Date minDate, Date maxDate) throws Exception {
        Map<String, String> result = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(sdf.format(minDate)));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(sdf.parse(sdf.format(maxDate)));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        Calendar curr = min;
        while (curr.before(max)) {
            String beginDate = getFormat(BudgetUtils.getBeginDate(curr.getTime(), BudgetConstant.TIME_TYPE_MONTH));
            String endDate = getFormat(BudgetUtils.getEndDate(curr.getTime(), BudgetConstant.TIME_TYPE_MONTH));
            result.put(sdf.format(curr.getTime()), beginDate + "~" + endDate);
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }
    /**
     * 获取周集合
     *
     * @param minDate
     * @param maxDate
     * @return
     * @throws Exception
     */
    private Map<String, String> getWeekBetween(Date minDate, Date maxDate) throws Exception {
        Map<String, String> result = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        //周取自定义周期的周计划
        List<FbsBudgetPeriod> fbsBudgetPeriodList = fbsBudgetPeriodDao.getByForList(minDate,maxDate);
        for (FbsBudgetPeriod fbsBudgetPeriod : fbsBudgetPeriodList){
            String key = sdf.format(fbsBudgetPeriod.getBeginDate())+this.getPeriodOfDate(fbsBudgetPeriod.getBeginDate())+"周";
            String beginDate = getFormat(fbsBudgetPeriod.getBeginDate());
            String endDate = getFormat(fbsBudgetPeriod.getEndDate());
            result.put(key, beginDate + "~" + endDate);
        }
        return result;
    }
    /**
     * 获取天集合
     *
     * @param minDate
     * @param maxDate
     * @return
     * @throws Exception
     */
    private Map<String, String> getDayBetween(Date minDate, Date maxDate) throws Exception {
        Map<String, String> result = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(minDate);
        max.setTime(maxDate);
        max.add(Calendar.DAY_OF_MONTH, 1);
        Calendar curr = min;
        while (curr.before(max)) {
            String key = sdf.format(curr.getTime());
            String beginDate = getFormat(BudgetUtils.getBeginDate(curr.getTime(), BudgetConstant.TIME_TYPE_DAY));
            String endDate = getFormat(BudgetUtils.getEndDate(curr.getTime(), BudgetConstant.TIME_TYPE_DAY));
            result.put(key, beginDate + "~" + endDate);
            curr.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }
    /**
     * 获取当前日期属于当月第几周
     *
     * @param date
     * @return
     */
    public Integer getPeriodOfDate(Date date) {
        Integer period = 0;
        //获取周期
        List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.autoAenerateByMonth(date);
        //本月周计划
        if (!EmptyUtils.isEmptyList(listPeriod)) {
            for (int i = 0; i < listPeriod.size(); i++) {
                FbsBudgetPeriodVO fbsBudgetPeriod = listPeriod.get(i);
                if (date.compareTo(fbsBudgetPeriod.getBeginDate()) >= 0 && date.compareTo(fbsBudgetPeriod.getEndDate()) <= 0) {
                    period = i + 1;
                    break;
                }
            }
        }
        return period;
    }
}
