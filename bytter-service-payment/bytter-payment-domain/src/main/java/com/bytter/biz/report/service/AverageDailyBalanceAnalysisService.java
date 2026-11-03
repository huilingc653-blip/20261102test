package com.bytter.biz.report.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.report.dao.AverageDailyBalanceAnalysisDao;
import com.bytter.biz.report.vo.AverageDailyBalanceAnalysisChartVO;
import com.bytter.biz.report.vo.AverageDailyBalanceAnalysisVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分析系统当前存款余额与上月的差异、与年初的差异。
 *
 * @author qinl
 * @description 存款余额对比分析service
 * @date 2020.02.12
 */
@Service("averageDailyBalanceAnalysisService")
@Transactional
public class AverageDailyBalanceAnalysisService extends BaseService<AverageDailyBalanceAnalysisVO, String> {
    @Autowired
    private AverageDailyBalanceAnalysisDao averageDailyBalanceAnalysisDao;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService iFcExcrateAdjustService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<AverageDailyBalanceAnalysisVO, String> getEntityDao() {
        return averageDailyBalanceAnalysisDao;
    }
    /**
     * 统计报表入口，条件组装
     *
     * @param input 传参
     * @return 界面显示数据
     */
    public DataTablesOutput<AverageDailyBalanceAnalysisVO> entrance(DataTablesInput input) {
        //性能优化，首次进入报表页面不加载列表
        if (input.getDraw() == 1) return new DataTablesOutput<>();
        //传参判断项
        if (this.checkConditions(input)) return new DataTablesOutput<>();
        //时间过滤器
        this.dateFilter(input);
        DataTablesOutput<AverageDailyBalanceAnalysisVO> output = this.conditionsSplit(input);
        //更新金额-折合本位币和金额单位
        this.moneyFilter(input, output);
        //分页过滤器
        this.pageFilter(input, output);
        return output;
    }
    public DataTablesOutput<AverageDailyBalanceAnalysisVO> conditionsSplit(DataTablesInput input) {
        DataTablesOutput<AverageDailyBalanceAnalysisVO> output = new DataTablesOutput<>();
        String statistical = (String) input.getFormdata().get("statistical");//维度统计参数
        String depositType = (String) input.getFormdata().get("depositType");//内部/银行存款参数
        String type = statistical.concat(depositType);
        switch (type) {
            case "00"://按单位统计全部
                this.statisticalBalance(input, output, true, null);
                this.statisticalContrastBalance(input, output, true, null);
                break;
            case "01"://按单位银行存款
                this.statisticalBalance(input, output, true, true);
                this.statisticalContrastBalance(input, output, true, true);
                break;
            case "02"://按单位内部存款
                this.statisticalBalance(input, output, true, false);
                this.statisticalContrastBalance(input, output, true, false);
                break;
            case "10"://按银行类型统计全部
                this.statisticalBalance(input, output, false, null);
                this.statisticalContrastBalance(input, output, false, null);
                break;
            case "11"://按银行类型统计银行存款
                this.statisticalBalance(input, output, false, true);
                this.statisticalContrastBalance(input, output, false, true);
                break;
            case "12"://按银行类型统计内部存款
                this.statisticalBalance(input, output, false, false);
                this.statisticalContrastBalance(input, output, false, false);
                break;
        }
        return output;
    }
    /**
     * 统计余额
     *
     * @param input      传参
     * @param isCorp     是否为单位
     * @param isBankType 是否为银行类型
     */
    public void statisticalBalance(DataTablesInput input, DataTablesOutput<AverageDailyBalanceAnalysisVO> output, boolean isCorp, Boolean isBankType) {
        List<AverageDailyBalanceAnalysisVO> calculate;
        if (isCorp) {//按单位维度统计
            if (isBankType == null) {//查询全部余额
                //获取列表数据
                List<AverageDailyBalanceAnalysisVO> listBank = averageDailyBalanceAnalysisDao.selectCorpListForBank(input);
                List<AverageDailyBalanceAnalysisVO> listInner = averageDailyBalanceAnalysisDao.selectCorpListForInner(input);
                listBank.addAll(listInner);
                calculate = this.calculateAverage(input, listBank);
            } else if (isBankType) {//查询银行余额
                //获取列表数据
                List<AverageDailyBalanceAnalysisVO> list = averageDailyBalanceAnalysisDao.selectCorpListForBank(input);
                //设置日均与余额
                calculate = this.calculateAverage(input, list);
            } else {//查询内部余额
                //获取列表数据
                List<AverageDailyBalanceAnalysisVO> list = averageDailyBalanceAnalysisDao.selectCorpListForInner(input);
                //设置日均与余额
                calculate = this.calculateAverage(input, list);
            }
        } else {//按银行类型维度统计
            if (isBankType == null) {//查询全部余额
                List<AverageDailyBalanceAnalysisVO> listBank = averageDailyBalanceAnalysisDao.selectBankTypeListForBank(input);
                List<AverageDailyBalanceAnalysisVO> listInner = averageDailyBalanceAnalysisDao.selectBankTypeListForInner(input);
                listBank.addAll(listInner);
                calculate = this.calculateAverage(input, listBank);
            } else if (isBankType) {//查询银行余额
                //获取列表数据
                List<AverageDailyBalanceAnalysisVO> list = averageDailyBalanceAnalysisDao.selectBankTypeListForBank(input);
                //设置日均与余额
                calculate = this.calculateAverage(input, list);
            } else {//查询内部余额
                //获取列表数据
                List<AverageDailyBalanceAnalysisVO> list = averageDailyBalanceAnalysisDao.selectBankTypeListForInner(input);
                //设置日均与余额
                calculate = this.calculateAverage(input, list);
            }
        }
        output.setData(calculate);
    }
    /**
     * 设置对比余额
     *
     * @param input      传参
     * @param isCorp     是否为单位
     * @param isBankType 是否为银行类型
     */
    private void statisticalContrastBalance(DataTablesInput input, DataTablesOutput<AverageDailyBalanceAnalysisVO> output, boolean isCorp, Boolean isBankType) {
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String curId = (String) input.getFormdata().get("curId");//币别
        String beforeDate = (String) input.getFormdata().get("beforeDate");//前置时间
        String afterDate = (String) input.getFormdata().get("afterDate");//后置时间
        String frequency = (String) input.getFormdata().get("frequency");//查询频率
        String depositType = (String) input.getFormdata().get("depositType");//存款类型
        String statistical = (String) input.getFormdata().get("statistical");//维度统计
        String isConverted = (String) input.getFormdata().get("isConverted");//是否折合币种
        String moneyUnit = (String) input.getFormdata().get("moneyUnit");//金额单位
        String reducedCurId = (String) input.getFormdata().get("reducedCurId");//折合币种
        DataTablesOutput<AverageDailyBalanceAnalysisVO> output1 = new DataTablesOutput<>();
        DataTablesOutput<AverageDailyBalanceAnalysisVO> output2 = new DataTablesOutput<>();
        DataTablesInput inputContrast = new DataTablesInput();//设置对比条件
        inputContrast.getFormdata().put("corpIds", corpIds);
        inputContrast.getFormdata().put("curId", curId);
        inputContrast.getFormdata().put("depositType", depositType);
        inputContrast.getFormdata().put("statistical", statistical);
        inputContrast.getFormdata().put("isConverted", isConverted);
        inputContrast.getFormdata().put("moneyUnit", moneyUnit);
        inputContrast.getFormdata().put("reducedCurId", reducedCurId);
        if("1".equals(frequency)) {//按天计算
            int days = DateUtils.getOffsetDays(DateUtils.parse(beforeDate, "yyyy-MM-dd"), DateUtils.parse(afterDate, "yyyy-MM-dd")) + 1;
            //计算较上期
            Date dateBefore = DateUtils.addDay(DateUtils.parse(beforeDate, "yyyy-MM-dd"), -days);
            Date dateAfter = DateUtils.addDay(DateUtils.parse(afterDate, "yyyy-MM-dd"), -days);
            inputContrast.getFormdata().put("beforeDate", DateUtils.format(dateBefore, "yyyy-MM-dd"));
            inputContrast.getFormdata().put("afterDate", DateUtils.format(dateAfter, "yyyy-MM-dd"));
            this.statisticalBalance(inputContrast, output1, isCorp, isBankType);
            //计算较上年同期
            Date dateBeforeYear = DateUtils.addMonth(DateUtils.parse(afterDate, "yyyy-MM-dd"), -12);
            Date dateAfterYear = DateUtils.addMonth(DateUtils.parse(afterDate, "yyyy-MM-dd"), -12);
            inputContrast.getFormdata().put("beforeDate", DateUtils.format(dateBeforeYear, "yyyy-MM-dd"));
            inputContrast.getFormdata().put("afterDate", DateUtils.format(dateAfterYear, "yyyy-MM-dd"));
            this.statisticalBalance(inputContrast, output2, isCorp, isBankType);
        }else{//按月计算
            int before = Integer.parseInt(beforeDate.substring(5, 7));
            int after = Integer.parseInt(afterDate.substring(5, 7));
            int difference = after - before +1;
            Date date1 = DateUtils.addMonth(DateUtils.parse(beforeDate, "yyyy-MM-dd"), -1);
            Date date2 = DateUtils.addMonth(DateUtils.parse(beforeDate, "yyyy-MM-dd"), -difference);
            inputContrast.getFormdata().put("beforeDate", DateUtils.format( DateUtils.getMonthFirstDate(date2), "yyyy-MM-dd"));
            inputContrast.getFormdata().put("afterDate", DateUtils.format(DateUtils.getLastMonthDate(date1), "yyyy-MM-dd"));
            this.statisticalBalance(inputContrast, output1, isCorp, isBankType);
            Date dateBeforeYear = DateUtils.addMonth(DateUtils.parse(afterDate, "yyyy-MM-dd"), -12);
            Date dateAfterYear = DateUtils.addMonth(DateUtils.parse(afterDate, "yyyy-MM-dd"), -12);
            inputContrast.getFormdata().put("beforeDate", DateUtils.format(dateBeforeYear, "yyyy-MM-dd"));
            inputContrast.getFormdata().put("afterDate", DateUtils.format(dateAfterYear, "yyyy-MM-dd"));
            this.statisticalBalance(inputContrast, output2, isCorp, isBankType);
        }
        //给列表设置对比数据
        List<AverageDailyBalanceAnalysisVO> data = output.getData();//获取余额数据
        List<AverageDailyBalanceAnalysisVO> data1 = output1.getData();//较上期数据
        List<AverageDailyBalanceAnalysisVO> data2 = output2.getData();//较上年同期数据
        //设置较上期
        for (AverageDailyBalanceAnalysisVO vo : data) {
            for (AverageDailyBalanceAnalysisVO vo1 : data1) {
                if (vo.getCorpName().equals(vo1.getCorpName()) && vo.getCurId().equals(vo1.getCurId())) {
                    vo.setPreviousAverageBalance(vo1.getAverageBalance());//日均
                    BigDecimal sub = vo.getAverageBalance().subtract(vo.getPreviousAverageBalance());
                    vo.setPreviousDifferenceBalance(sub);//设置差额
                    BigDecimal subtract2;
                    if (vo.getAverageBalance().compareTo(BigDecimal.ZERO) == 0) {
                        subtract2 = null;
                    } else {
                        subtract2 = vo.getPreviousDifferenceBalance().divide(vo.getAverageBalance(), 4, RoundingMode.HALF_UP);
                    }
                    vo.setPreviousProportion(subtract2);//比例
                }
            }
        }
        //设置较上年同期
        for (AverageDailyBalanceAnalysisVO vo : data) {
            for (AverageDailyBalanceAnalysisVO vo1 : data2) {
                if (vo.getCorpName().equals(vo1.getCorpName()) && vo.getCurId().equals(vo1.getCurId())) {
                    vo.setYearOverYearAverageBalance(vo1.getAverageBalance());//日均
                    BigDecimal sub = vo.getAverageBalance().subtract(vo.getYearOverYearAverageBalance());
                    vo.setYearOverYearDifferenceBalance(sub);//设置差额
                    BigDecimal subtract2;
                    if (vo.getAverageBalance().compareTo(BigDecimal.ZERO) == 0) {
                        subtract2 = null;
                    } else {
                        subtract2 = vo.getYearOverYearDifferenceBalance().divide(vo.getAverageBalance(), 4, RoundingMode.HALF_UP);
                    }
                    vo.setYearOverYearProportion(subtract2);//比例
                }
            }
        }
    }
    /**
     * 计算规则，按照单位维度进行余额计算
     *
     * @param list 需要计算的数据列
     * @return 计算结果列表
     */
    private List<AverageDailyBalanceAnalysisVO> calculateForCorp(DataTablesInput input, List<AverageDailyBalanceAnalysisVO> list) {
        moneyFilter(input,list);
        String afterDate = (String) input.getFormdata().get("afterDate");//后置时间
        Map<String, AverageDailyBalanceAnalysisVO> map = new HashMap<>();//用于单位分类
        //设置总余额
        for (AverageDailyBalanceAnalysisVO vo : list) {
            if (map.containsKey(vo.getCorpName()+vo.getCurId())) {
                BigDecimal bigDecimal = map.get(vo.getCorpName()+vo.getCurId()).getTotalBalance();
                if (bigDecimal == null) {
                    bigDecimal = BigDecimal.ZERO;
                }
                if (vo.getBalance() == null) {
                    vo.setBalance(BigDecimal.ZERO);
                }
                BigDecimal addAfter = bigDecimal.add(vo.getBalance());
                vo.setTotalBalance(addAfter); //设置总金额
            } else {
                vo.setTotalBalance(vo.getBalance());
            }
            map.put(vo.getCorpName()+vo.getCurId(), vo);
        }
        //设置期末余额
        for (String key : map.keySet()) {
            if(key ==null){
                continue;
            }
            BigDecimal bigDecimal = new BigDecimal(0);
            for (AverageDailyBalanceAnalysisVO vo : list) {
                if (!(map.get(key).getCorpName()+map.get(key).getCurId()).equals(vo.getCorpName()+vo.getCurId())) {
                    continue;
                }
                if (afterDate.equals(DateUtils.format(vo.getBalDate(), "yyyy-MM-dd"))) {
                    bigDecimal = bigDecimal.add(vo.getBalance());
                }
            }
            map.get(key).setBalance(bigDecimal);
            map.put(key, map.get(key));
        }
        return this.insertData(map);
    }
    /**
     * 计算日均与余额-银行账号
     */
    private List<AverageDailyBalanceAnalysisVO> calculateAverage(DataTablesInput input, List<AverageDailyBalanceAnalysisVO> list) {
        Map<String, String> currencyMap = currencyService.getCurrencyNameMap();
        List<AverageDailyBalanceAnalysisVO> listAverage = this.calculateForCorp(input, list);
        //计算日均
        for (AverageDailyBalanceAnalysisVO vo : listAverage) {
            vo.setCurName(currencyMap.get(vo.getCurId()));//币别
            BigDecimal bigDecimal = this.calculateAverageDailyValue(input, vo.getTotalBalance());
            vo.setAverageBalance(bigDecimal);
        }
        return listAverage;
    }
    /**
     * 日均计算法则，余额/天数和
     *
     * @param bigDecimal 需要计算的数据
     * @return 计算结果
     */
    private BigDecimal calculateAverageDailyValue(DataTablesInput input, BigDecimal bigDecimal) {
        String beforeDate = (String) input.getFormdata().get("beforeDate");//前置时间
        String afterDate = (String) input.getFormdata().get("afterDate");//后置时间
        int days = DateUtils.getOffsetDays(DateUtils.parse(beforeDate, "yyyy-MM-dd"), DateUtils.parse(afterDate, "yyyy-MM-dd"));
        BigDecimal daysBigDecimal = new BigDecimal(days + 1);
        return bigDecimal.divide(daysBigDecimal, 12, RoundingMode.HALF_UP);
    }
    /**
     * map转list
     *
     * @param map 待转化的数据
     * @return list
     */
    private <E> List<E> insertData(Map<String, E> map) {
        List<E> calculateList = new ArrayList<>();
        for (String key : map.keySet()) {
            calculateList.add(map.get(key));
        }
        return calculateList;
    }
    /**
     * 时间过滤器
     *
     * @param input
     */
    private void dateFilter(DataTablesInput input) {
        String frequency = (String) input.getFormdata().get("frequency");
        if ("2".equals(frequency)) {
            String beforeDate = (String) input.getFormdata().get("beforeDate");
            String afterDate = (String) input.getFormdata().get("afterDate");
            //获取当前时间
            Date date = new Date();
            String s = DateUtils.formatDate(date, "yyyy-MM");
            if (s.equals(afterDate)) {
                String afterDay = DateUtils.formatDate(date, "yyyy-MM-dd");
                input.getFormdata().put("afterDate", afterDay);
            } else {
                Date afterDay = DateUtils.parse(afterDate, "yyyy-MM");
                Date lastMonthDate = DateUtils.getLastMonthDate(afterDay);
                input.getFormdata().put("afterDate", DateUtils.getDateStr(lastMonthDate, "yyyy-MM-dd"));
            }
            if (s.equals(beforeDate)) {
                String beforeDay = DateUtils.formatDate(DateUtils.getMonthStartTime(), "yyyy-MM-dd");
                input.getFormdata().put("beforeDate", beforeDay);
            } else {
                Date beforeDay = DateUtils.parse(beforeDate, "yyyy-MM");
                Date firstMonthDate = DateUtils.getFirstMonthDate(beforeDay);
                input.getFormdata().put("beforeDate", DateUtils.getDateStr(firstMonthDate, "yyyy-MM-dd"));
            }
        }
    }
    /**
     * 金额过滤器
     */
    private void moneyFilter(DataTablesInput input, DataTablesOutput<AverageDailyBalanceAnalysisVO> output) {
        String moneyUnit = (String) input.getFormdata().get("moneyUnit");//金额单位
        //金额单位换算
        BigDecimal moneyType;
        switch (moneyUnit) {
            case "2":
                moneyType = new BigDecimal("1000");
                break;
            case "3":
                moneyType = new BigDecimal("10000");
                break;
            case "4":
                moneyType = new BigDecimal("100000000");
                break;
            default:
                moneyType = new BigDecimal("1");
                break;
        }
        List<AverageDailyBalanceAnalysisVO> data = output.getData();
        for (AverageDailyBalanceAnalysisVO vo : data) {
            if (vo.getBalance() != null) {
                BigDecimal divide = vo.getBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setBalance(divide);
            }
            if (vo.getAverageBalance() != null) {
                BigDecimal divide = vo.getAverageBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setAverageBalance(divide);
            }
            if (vo.getPreviousAverageBalance() != null) {
                BigDecimal divide = vo.getPreviousAverageBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setPreviousAverageBalance(divide);
            }
            if (vo.getPreviousDifferenceBalance() != null) {
                BigDecimal divide = vo.getPreviousDifferenceBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setPreviousDifferenceBalance(divide);
            }
            if (vo.getYearOverYearAverageBalance() != null) {
                BigDecimal divide = vo.getYearOverYearAverageBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setYearOverYearAverageBalance(divide);
            }
            if (vo.getYearOverYearDifferenceBalance() != null) {
                BigDecimal divide = vo.getYearOverYearDifferenceBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setYearOverYearDifferenceBalance(divide);
            }
        }
    }
    /**
     * 金额过滤器
     */
    private void moneyFilter(DataTablesInput input, List<AverageDailyBalanceAnalysisVO> list) {
        String isConverted = (String) input.getFormdata().get("isConverted");//折合本位币
        String reducedCurId = (String) input.getFormdata().get("reducedCurId");//折合币种
        String afterDate = (String) input.getFormdata().get("afterDate");//时间
        //折合换算
        for (AverageDailyBalanceAnalysisVO vo : list) {
            BigDecimal curExrate = new BigDecimal(1);
            if ("1".equals(isConverted)) {
                BigDecimal exchangeRateBy = iFcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), reducedCurId, DateUtils.parse(afterDate,"yyyy-MM-dd"), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (exchangeRateBy != null) {
                    curExrate = exchangeRateBy;
                }
            }
            if (vo.getBalance() != null) {
                BigDecimal multiply = vo.getBalance().multiply(curExrate);
                vo.setBalance(multiply);
            }
            if (vo.getAverageBalance() != null) {
                BigDecimal multiply = vo.getAverageBalance().multiply(curExrate);
                vo.setAverageBalance(multiply);
            }
            if (vo.getPreviousAverageBalance() != null) {
                BigDecimal multiply = vo.getPreviousAverageBalance().multiply(curExrate);
                vo.setPreviousAverageBalance(multiply);
            }
            if (vo.getPreviousDifferenceBalance() != null) {
                BigDecimal multiply = vo.getPreviousDifferenceBalance().multiply(curExrate);
                vo.setPreviousDifferenceBalance(multiply);
            }
            if (vo.getYearOverYearAverageBalance() != null) {
                BigDecimal multiply = vo.getYearOverYearAverageBalance().multiply(curExrate);
                vo.setYearOverYearAverageBalance(multiply);
            }
            if (vo.getYearOverYearDifferenceBalance() != null) {
                BigDecimal multiply = vo.getYearOverYearDifferenceBalance().multiply(curExrate);
                vo.setYearOverYearDifferenceBalance(multiply);
            }
        }
    }
    /**
     * 分页过滤器
     */
    private <T> void pageFilter(DataTablesInput input, DataTablesOutput<T> output) {
        List<T> list = output.getData();
        if (EmptyUtils.isEmpty(list)) {
            output.setDraw(0);
            output.setRecordsTotal(0);
            output.setRecordsFiltered(0);
            output.setData(new ArrayList<T>());
            return;
        }
        int totalNum = list.size();
        Integer pageNumber = input.getPageNumber();
        int page = (totalNum + 10 - 1) / 10;
        if (pageNumber == page) {
            output.setData(list.subList(input.getStart(), totalNum));
        } else {
            if (input.getLength() > totalNum) {
                output.setData(list.subList(input.getStart(), totalNum));
            } else {
                output.setData(list.subList(input.getStart(), input.getStart() + 10));
            }
        }
        output.setRecordsTotal(list.size());
        output.setRecordsFiltered(list.size());
    }
    /**
     * 条件检查项
     *
     * @param input 条件输入流
     * @return true or false
     */
    private boolean checkConditions(DataTablesInput input) {
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String depositType = (String) input.getFormdata().get("depositType");//内部/银行存款
        String statistical = (String) input.getFormdata().get("statistical");//维度统计
//        String curId = (String) input.getFormdata().get("curId");//币别
        String isConverted = (String) input.getFormdata().get("isConverted");//是否折合本位币
        String moneyUnit = (String) input.getFormdata().get("moneyUnit");//金额单位
        String frequency = (String) input.getFormdata().get("frequency");//查询频率
        String beforeDate = (String) input.getFormdata().get("beforeDate");
        String afterDate = (String) input.getFormdata().get("afterDate");
        if (StringUtils.isEmpty(depositType)) return true;
        if (StringUtils.isEmpty(statistical)) return true;
//        if (StringUtils.isEmpty(curId)) return true;
        if (StringUtils.isEmpty(isConverted)) return true;
        if (StringUtils.isEmpty(moneyUnit)) return true;
        if (StringUtils.isEmpty(frequency)) return true;
        if (StringUtils.isEmpty(beforeDate)) return true;
        if (StringUtils.isEmpty(afterDate)) return true;
        if(CommonConstant.YES.equals(isConverted)){
            StringBuilder sb = new StringBuilder();
            List<BtCurrencyDTO> btCurrency = currencyService.getBtCurrency();
            for(BtCurrencyDTO dto:btCurrency){
                sb.append(dto.getId()).append(",");
            }
            String s = sb.toString();
            String substring = s.substring(0, s.length() - 2);
            input.getFormdata().put("curId",substring);
        }
        return StringUtils.isEmpty(corpIds);
    }
    /* echart 独立模块 begin*/
    public DataTablesOutput<AverageDailyBalanceAnalysisChartVO> echartDataTable(DataTablesInput input) {
        //时间过滤器
        this.dateFilter(input);
        input.getFormdata().put("searchDate",input.getFormdata().get("afterDate"));
        DataTablesOutput<AverageDailyBalanceAnalysisChartVO> output = this.echartConditionsSplit(input);
        //更新金额-折合本位币和金额单位
        this.chartMoneyFilter(input, output);
        //分页过滤器
        this.pageFilter(input, output);
        return output;
    }
    public List<AverageDailyBalanceAnalysisChartVO> echartList(DataTablesInput input) {
        //时间过滤器
        this.dateFilter(input);
        input.getFormdata().put("searchDate",input.getFormdata().get("afterDate"));
        //待导出的数据
        List<AverageDailyBalanceAnalysisChartVO> list = new ArrayList<>();
        //折算时间临时存储
//        String dateTemp = (String) input.getFormdata().get("afterDate");
//        input.getFormdata().put("afterDate", dateTemp);
        String frequency = (String) input.getFormdata().get("frequency");
        if ("1".equals(frequency)) {//按天查询
            //相差的天数
            String afterDate = (String) input.getFormdata().get("afterDate");
            String beforeDate = (String) input.getFormdata().get("beforeDate");
            int days = DateUtils.getOffsetDays(DateUtils.parse(beforeDate, "yyyy-MM-dd"), DateUtils.parse(afterDate, "yyyy-MM-dd")) + 1;
            input.getFormdata().put("afterDate", beforeDate);
            for (int i = 0; i < days; i++) {
                //计算当天余额总和
                this.echartCalculate(input, list, (String) input.getFormdata().get("beforeDate"));
                Date date = DateUtils.parseDate((String) input.getFormdata().get("beforeDate"));
                Date date1 = DateUtils.addDay(date, 1);
                String format = DateUtils.format(date1, "yyyy-MM-dd");
                input.getFormdata().put("beforeDate", format);
                input.getFormdata().put("afterDate", format);
            }
        } else {//按月查询
            String after = (String) input.getFormdata().get("afterDate");
            String afterFormat = DateUtils.format(DateUtils.parseDate(after), "yyyy-MM");
            while (true) {
                String before = (String) input.getFormdata().get("beforeDate");
                Date beforeDay = DateUtils.parseDate(before);
                String beforeFormat = DateUtils.format(beforeDay, "yyyy-MM");
                if (beforeFormat.equals(afterFormat)) {
                    Date firstMonthDate = DateUtils.getFirstMonthDate(DateUtils.parseDate(after));
                    input.getFormdata().put("beforeDate", DateUtils.format(firstMonthDate, "yyyy-MM-dd"));
                    input.getFormdata().put("afterDate", after);
                    this.echartCalculate(input, list, beforeFormat);
                    break;
                } else {
                    Date lastMonthDate = DateUtils.getLastMonthDate(beforeDay);
                    Date firstMonthDate = DateUtils.getFirstMonthDate(beforeDay);
                    input.getFormdata().put("beforeDate", DateUtils.format(firstMonthDate, "yyyy-MM-dd"));
                    input.getFormdata().put("afterDate", DateUtils.format(lastMonthDate, "yyyy-MM-dd"));
                    this.echartCalculate(input, list, beforeFormat);
                    Date date = DateUtils.addMonth(beforeDay, 1);
                    input.getFormdata().put("beforeDate", DateUtils.format(date, "yyyy-MM-dd"));
                }
            }
        }
        //导出数据
        DataTablesOutput<AverageDailyBalanceAnalysisChartVO> output = new DataTablesOutput<>();
        output.setData(list);
        //更新金额-金额单位
        this.chartMoneyFilter(input, output);
        return output.getData();
    }
    private DataTablesOutput<AverageDailyBalanceAnalysisChartVO> echartConditionsSplit(DataTablesInput input) {
        DataTablesOutput<AverageDailyBalanceAnalysisChartVO> output = new DataTablesOutput<>();
        String statistical = (String) input.getFormdata().get("statistical");//维度统计参数
        String depositType = (String) input.getFormdata().get("depositType");//内部/银行存款参数
        String type = statistical.concat(depositType);
        switch (type) {
            case "00"://按单位统计全部
                List<AverageDailyBalanceAnalysisChartVO> list00Bank = averageDailyBalanceAnalysisDao.selectDailyListForBank(input);
                List<AverageDailyBalanceAnalysisChartVO> list00Inner = averageDailyBalanceAnalysisDao.selectDailyListForInner(input);
                list00Bank.addAll(list00Inner);
                output.setData(echartStatisticalBalance(input, list00Bank));
                break;
            case "01"://按单位银行存款
                List<AverageDailyBalanceAnalysisChartVO> list01Bank = averageDailyBalanceAnalysisDao.selectDailyListForBank(input);
                output.setData(echartStatisticalBalance(input, list01Bank));
                break;
            case "02"://按单位内部存款
                List<AverageDailyBalanceAnalysisChartVO> list02Inner = averageDailyBalanceAnalysisDao.selectDailyListForInner(input);
                output.setData(echartStatisticalBalance(input, list02Inner));
                break;
            case "10"://按银行类型统计全部
                List<AverageDailyBalanceAnalysisChartVO> list10Bank = averageDailyBalanceAnalysisDao.selectAverageBankTypeListForBank(input);
                List<AverageDailyBalanceAnalysisChartVO> list10Inner = averageDailyBalanceAnalysisDao.selectAverageBankTypeListForInner(input);
                list10Bank.addAll(list10Inner);
                output.setData(echartStatisticalBalance(input, list10Bank));
                break;
            case "11"://按银行类型统计银行存款
                List<AverageDailyBalanceAnalysisChartVO> list11Bank = averageDailyBalanceAnalysisDao.selectAverageBankTypeListForBank(input);
                output.setData(echartStatisticalBalance(input, list11Bank));
                break;
            case "12"://按银行类型统计内部存款
                List<AverageDailyBalanceAnalysisChartVO> list12Inner = averageDailyBalanceAnalysisDao.selectAverageBankTypeListForInner(input);
                output.setData(echartStatisticalBalance(input, list12Inner));
                break;
        }
        setCommonField(output.getData());
        return output;
    }
    private DataTablesOutput<AverageDailyBalanceAnalysisChartVO> echartConditionsAllSplit(DataTablesInput input) {
        DataTablesOutput<AverageDailyBalanceAnalysisChartVO> output = new DataTablesOutput<>();
        String statistical = (String) input.getFormdata().get("statistical");//维度统计参数
        String depositType = (String) input.getFormdata().get("depositType");//内部/银行存款参数
        String type = statistical.concat(depositType);
        switch (type) {
            case "00"://按单位统计全部
                List<AverageDailyBalanceAnalysisChartVO> list00Bank = averageDailyBalanceAnalysisDao.selectDailyListForBank(input);
                List<AverageDailyBalanceAnalysisChartVO> list00Inner = averageDailyBalanceAnalysisDao.selectDailyListForInner(input);
                list00Bank.addAll(list00Inner);
                output.setData(echartStatisticalAllBalance(input, list00Bank));
                break;
            case "01"://按单位银行存款
                List<AverageDailyBalanceAnalysisChartVO> list01Bank = averageDailyBalanceAnalysisDao.selectDailyListForBank(input);
                output.setData(echartStatisticalAllBalance(input, list01Bank));
                break;
            case "02"://按单位内部存款
                List<AverageDailyBalanceAnalysisChartVO> list02Inner = averageDailyBalanceAnalysisDao.selectDailyListForInner(input);
                output.setData(echartStatisticalAllBalance(input, list02Inner));
                break;
            case "10"://按银行类型统计全部
                List<AverageDailyBalanceAnalysisChartVO> list10Bank = averageDailyBalanceAnalysisDao.selectAverageBankTypeListForBank(input);
                List<AverageDailyBalanceAnalysisChartVO> list10Inner = averageDailyBalanceAnalysisDao.selectAverageBankTypeListForInner(input);
                list10Bank.addAll(list10Inner);
                output.setData(echartStatisticalAllBalance(input, list10Bank));
                break;
            case "11"://按银行类型统计银行存款
                List<AverageDailyBalanceAnalysisChartVO> list11Bank = averageDailyBalanceAnalysisDao.selectAverageBankTypeListForBank(input);
                output.setData(echartStatisticalAllBalance(input, list11Bank));
                break;
            case "12"://按银行类型统计内部存款
                List<AverageDailyBalanceAnalysisChartVO> list12Inner = averageDailyBalanceAnalysisDao.selectAverageBankTypeListForInner(input);
                output.setData(echartStatisticalAllBalance(input, list12Inner));
                break;
        }
        setCommonField(output.getData());
        return output;
    }

    private void setCommonField(List<AverageDailyBalanceAnalysisChartVO> vos) {
        if (CollectionUtil.isEmpty(vos)) {
            return;
        }
        Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(vos.stream().map(AverageDailyBalanceAnalysisChartVO::getCurId)
                .collect(Collectors.toList()));
        Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap("btBankAcc.accType", "InnerAccount.AccType");
        for (AverageDailyBalanceAnalysisChartVO vo : vos) {
            vo.setCurName(currencyNameMap.get(vo.getCurId()));
            if (StringUtils.isNotEmpty(vo.getInnerAccType())) {
                vo.setDepositType(dictMap.getOrDefault("InnerAccount.AccType", Maps.newHashMap())
                        .getOrDefault(vo.getInnerAccType(), DictDTO.EMPTY).getName());
            } else if (StringUtils.isNotEmpty(vo.getBankAccType())) {
                vo.setDepositType(dictMap.getOrDefault("btBankAcc.accType", Maps.newHashMap())
                        .getOrDefault(vo.getBankAccType(), DictDTO.EMPTY).getName());
            }
        }

    }

    /**
     * 计算总额与平均
     *
     * @param input
     * @param list
     * @return
     */
    private List<AverageDailyBalanceAnalysisChartVO> echartStatisticalBalance(DataTablesInput input, List<AverageDailyBalanceAnalysisChartVO> list) {
        chartMoneyFilter(input,list);
        Map<String, AverageDailyBalanceAnalysisChartVO> map = new HashMap<>();
        String corpCurId = (String)input.getFormdata().get("corpCurId");
        //计算余额总和
        for (AverageDailyBalanceAnalysisChartVO vo : list) {
            if(corpCurId.equals(vo.getCurId())){
                if (map.containsKey(vo.getDepositType())) {
                    BigDecimal bigDecimal = map.get(vo.getDepositType()).getAverageBalance();
                    BigDecimal add = bigDecimal.add(vo.getAverageBalance());
                    map.get(vo.getDepositType()).setAverageBalance(add);
                    map.put(vo.getDepositType(), map.get(vo.getDepositType()));
                } else {
                    map.put(vo.getDepositType(), vo);
                }
            }
        }
        List<AverageDailyBalanceAnalysisChartVO> newList = insertData(map);
        //计算 平均
        for (AverageDailyBalanceAnalysisChartVO vo : newList) {
            //平均余额
            BigDecimal bigDecimal = this.calculateAverageDailyValue(input, vo.getAverageBalance());
            vo.setAverageBalance(bigDecimal);
            //存款类别
            if (StringUtils.isNotBlank(vo.getBankAccType())) {
                vo.setStatistical("1");
            } else {
                vo.setStatistical("0");
            }
        }
        return newList;
    }
    /**
     * 计算总额
     *
     * @param input
     * @param list
     * @return
     */
    private List<AverageDailyBalanceAnalysisChartVO> echartStatisticalAllBalance(DataTablesInput input, List<AverageDailyBalanceAnalysisChartVO> list) {
        String corpCurId = (String)input.getFormdata().get("corpCurId");
        Map<String, AverageDailyBalanceAnalysisChartVO> map = new HashMap<>();
        //计算余额总和
        for (AverageDailyBalanceAnalysisChartVO vo : list) {
            if(corpCurId.equals(vo.getCurId())){
                if (map.containsKey(vo.getDepositType())) {
                    BigDecimal bigDecimal = map.get(vo.getDepositType()).getAverageBalance();
                    BigDecimal add = bigDecimal.add(vo.getAverageBalance());
                    map.get(vo.getDepositType()).setAverageBalance(add);
                    map.put(vo.getDepositType(), map.get(vo.getDepositType()));
                } else {
                    map.put(vo.getDepositType(), vo);
                }
            }
        }
        return insertData(map);
    }
    /**
     * 计算余额总和-echart
     *
     * @param input
     * @return
     */
    private void echartCalculate(DataTablesInput input, List<AverageDailyBalanceAnalysisChartVO> list, String date) {
        String corpCurId = (String)input.getFormdata().get("corpCurId");
        DataTablesOutput<AverageDailyBalanceAnalysisChartVO> output = this.echartConditionsAllSplit(input);
        BigDecimal bigDecimal = new BigDecimal(0);
        chartMoneyFilter(input,output.getData());
        for (AverageDailyBalanceAnalysisChartVO vo : output.getData()) {
            if(vo.getCurId().equals(corpCurId)){
                bigDecimal = bigDecimal.add(vo.getAverageBalance());
            }
        }
        AverageDailyBalanceAnalysisChartVO vo = new AverageDailyBalanceAnalysisChartVO();
        vo.setDate(date);
        vo.setAverageBalance(bigDecimal);
        list.add(vo);
    }
    /**
     * 金额过滤器
     */
    private void chartMoneyFilter(DataTablesInput input, DataTablesOutput<AverageDailyBalanceAnalysisChartVO> output) {
        String moneyUnit = (String) input.getFormdata().get("moneyUnit");//金额单位
        //金额单位换算
        BigDecimal moneyType;
        switch (moneyUnit) {
            case "2":
                moneyType = new BigDecimal("1000");
                break;
            case "3":
                moneyType = new BigDecimal("10000");
                break;
            case "4":
                moneyType = new BigDecimal("100000000");
                break;
            default:
                moneyType = new BigDecimal("1");
                break;
        }
        List<AverageDailyBalanceAnalysisChartVO> data = output.getData();
        for (AverageDailyBalanceAnalysisChartVO vo : data) {
            if (vo.getAverageBalance() != null) {
                BigDecimal divide = vo.getAverageBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setAverageBalance(divide);
            }
        }
    }
    private void chartMoneyFilter(DataTablesInput input, List<AverageDailyBalanceAnalysisChartVO> list) {
        String isConverted = (String) input.getFormdata().get("isConverted");//折合本位币
        String reducedCurId = (String) input.getFormdata().get("reducedCurId");//折合币种
        String afterDate = (String) input.getFormdata().get("searchDate");//时间
        for (AverageDailyBalanceAnalysisChartVO vo : list) {
            //折合本位币换算
            BigDecimal curExrate = new BigDecimal(1);
            if ("1".equals(isConverted)) {
                BigDecimal exchangeRateBy = iFcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), reducedCurId, DateUtils.parse(afterDate,"yyyy-MM-dd"), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (exchangeRateBy != null) {
                    curExrate = exchangeRateBy;
                }
            }
            if (vo.getAverageBalance() != null) {
                BigDecimal multiply = vo.getAverageBalance().multiply(curExrate);
                vo.setAverageBalance(multiply);
            }
        }
    }
    /* echart 独立模块 end*/
}
