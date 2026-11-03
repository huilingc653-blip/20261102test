package com.bytter.report.domain.netbank.service;

import cn.hutool.core.date.StopWatch;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.*;
import com.bytter.increment.api.service.IChoBankFinanceService;
import com.bytter.report.api.constant.ResourceConstant;
import com.bytter.report.api.dto.DepositReportCommonDTO;
import com.bytter.report.domain.netbank.dao.HomeBankAccBalDistributeDao;
import com.bytter.report.domain.netbank.dao.ReportAccountBalDao;
import com.bytter.report.domain.netbank.vo.HomeBalCalculateResultVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccBalVO;
import com.bytter.report.domain.netbank.vo.ReportAccountBalVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * APP首页账户余额报表Server
 *
 * @author zuoy 2020-06-02
 */
@Service("reportAccountBalMobileServer")
@Transactional
public class ReportAccountBalMobileServer extends BaseService<ReportAccountBalVO, String> {
    @Autowired
    private ReportAccountBalDao reportAccountBalDao;
    @DubboReference(check = false)
    private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IChoBankFinanceService choBankFinanceService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalServiceImpl;
    @DubboReference(check = false)
    private IAccebillService accebillService;

    @Autowired
    private HomeBankAccBalDistributeDao bankAccBalDistributeDao;

    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    @Override
    public BaseDao<ReportAccountBalVO, String> getEntityDao() {
        return reportAccountBalDao;
    }

    /**
     * app首页-账户余额
     *
     * @param user 用户对象
     * @param type type
     * @return 结果
     */
    public Object getAccountBal(ICurrentUser user, String type) {
        UserDTO userDTO = userService.getByLoginName(user.getLoginName());
        // 获取本位币，默认为人民币
        BtCurrencyDTO natualCurrency = currencyService.getNatualCurrency();
        // 转换币别id
        String convertCurId = natualCurrency.getId();
        CorpDTO corp = userDTO.getCorp();
        if (!EmptyUtils.isEmpty(corp) && StringUtils.isNotEmpty(corp.getCurId())) {
            convertCurId = corp.getCurId();
        }
        // 获取该用户有资源权限的单位集合
        List<String> authCorpIds = corpService.getUserCorpIds(ResourceConstant.APP_BI_INDEX_QUERY, user.getId());
        List<ReportAccountBalVO> list = new ArrayList<>();
        // 获取月份
        Date curDate = DateUtils.getDate();
        ReportAccountBalVO accountBal;
        // 设置银行账户余额 6个月
        for (int i = 0; i < 6; i++) {
            Date date = DateUtils.addMonth(curDate, -i);
            int month = DateUtils.getMonth(date);
            String type1;
            if (i == 0) {
                type1 = "1";
            } else {
                type1 = "0";
            }
            accountBal = new ReportAccountBalVO();
            accountBal.setType("银行账户");
            accountBal.setMonth(month);
            BigDecimal curBal = getBankAccBal(authCorpIds, convertCurId, date, type1);
            accountBal.setBal(toTenThousandYuan(curBal));
            accountBal.setYearOnYear(getBankAccYearOnYear(authCorpIds, convertCurId, date, curBal, "0"));
            accountBal.setMonthOnMonth(getBankAccMonthOnMonth(authCorpIds, convertCurId, date, curBal, "0"));
            list.add(accountBal);
        }
        // 设置内部账户余额 6个月
        for (int i = 0; i < 6; i++) {
            Date date = DateUtils.addMonth(curDate, -i);
            int month = DateUtils.getMonth(date);
            String type1;
            if (i == 0) {
                type1 = "1";
            } else {
                type1 = "0";
            }
            accountBal = new ReportAccountBalVO();
            accountBal.setType("内部账户");
            accountBal.setMonth(month);
            BigDecimal curBal = getInnerAccBal(authCorpIds, convertCurId, date, type1);
            accountBal.setBal(toTenThousandYuan(curBal));
            accountBal.setYearOnYear(getInnerAccYearOnYear(authCorpIds, convertCurId, date, curBal, "0"));
            accountBal.setMonthOnMonth(getInnerAccMonthOnMonth(authCorpIds, convertCurId, date, curBal, "0"));
            list.add(accountBal);
        }
        return list;
    }

    /**
     * 转为万元
     *
     * @param money
     * @return
     */
    public BigDecimal toTenThousandYuan(BigDecimal money) {
        return money.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 根据汇率计算余额
     *
     * @param list         结果集
     * @param convertCurId 转换币别id
     * @param date         日期
     * @return 计算结果余额
     */
    public BigDecimal calculateBal(List<HomeBalCalculateResultVO> list, String convertCurId, Date date) {
        BigDecimal balance = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(list)) {
            for (HomeBalCalculateResultVO item : list) {
                if (item.getCurId().equalsIgnoreCase(convertCurId)) {
                    balance = balance.add(item.getBal());
                } else {
                    BigDecimal rate = fcExcrateAdjustService.getExchangeRateByNoCheck(item.getCurId(), convertCurId, date, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                    if (null == rate) {
                        rate = BigDecimal.ONE;
                    }
                    balance = balance.add(item.getBal().multiply(rate));
                }
            }
        }
        return balance;
    }

    /**
     * 获取银行账户余额
     *
     * @param authCorpIds  有资源权限的单位
     * @param convertCurId 转换币别id
     * @param date         日期
     * @param type         标识。1-代表当月当天，0-往年月的月末
     * @return 账户余额
     */
    private BigDecimal getBankAccBal(List<String> authCorpIds, String convertCurId, Date date, String type) {
        if ("1".equalsIgnoreCase(type)) {
            //银行账户余额和PC 保持一致
            String corpAuthSql = " and " + SqlUtil.getInSql(authCorpIds, "acc.corp_Id", true);
            //获取账户余额集合
            List<HomeBankAccBalVO> listBal = this.bankAccBalDistributeDao.getBankAccBal(corpAuthSql);
            //折合币种及金额单位转换, 当前余额、境内账户余额、境外账户余额（折合人民币）
            this.convertMoneyType(listBal, convertCurId);
            return listBal.stream().map(HomeBankAccBalVO::getAvailBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            return getBankAccBalByLastDay(authCorpIds, convertCurId, date); //获取指定月份最后一天的余额
        }
    }

    /**
     * 金额单位转换
     *
     * @param listBal      结果集
     * @param convertCurId 转换币别id
     */
    private void convertMoneyType(List<HomeBankAccBalVO> listBal, String convertCurId) {
        //币种折算
        //汇率集合
        HashMap<String, BigDecimal> exchangeRateMap = new HashMap<>();
        for (HomeBankAccBalVO vo : listBal) {
            BigDecimal exchangeRate;
            if (exchangeRateMap.containsKey(vo.getCurId())) {
                exchangeRate = exchangeRateMap.get(vo.getCurId());
            } else {
                exchangeRate = fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), convertCurId, DateUtils.setSpecifiedDateOfTime(DateUtils.getDate()), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (null == exchangeRate) {
                    exchangeRate = BigDecimal.ONE;
                }
                exchangeRateMap.put(vo.getCurId(), exchangeRate);
            }
            vo.setBal(vo.getBal().multiply(exchangeRate));
            if (null != vo.getAvailBal()) {
                vo.setAvailBal(vo.getAvailBal().multiply(exchangeRate));
            }
            if (null != vo.getFrzBal()) {
                vo.setFrzBal(vo.getFrzBal().multiply(exchangeRate));
            }
        }
    }

    /**
     * 获取指定月份最后一天的余额
     *
     * @param authCorpIds  有资源权限的单位集合
     * @param convertCurId 转换币别id
     * @param date         日期
     * @return 获取指定月份最后一天的余额
     */
    public BigDecimal getBankAccBalByLastDay(List<String> authCorpIds, String convertCurId, Date date) {
        List<HomeBalCalculateResultVO> balList = reportAccountBalDao.getBankAccountBalByLastDay(authCorpIds, DateUtils.getLastMonthDate(date));
        return calculateBal(balList, convertCurId, date);
    }

    /**
     * 获取指定月份最后一天的余额
     *
     * @param authCorpIds  有资源权限的单位集合
     * @param convertCurId 转换币别id
     * @param date         日期
     * @return 获取指定月份最后一天的余额
     */
    public BigDecimal getInnerAccBalByLastDay(List<String> authCorpIds, String convertCurId, Date date) {
        List<HomeBalCalculateResultVO> balList = reportAccountBalDao.getInnerAccountBalByLastDay(authCorpIds, DateUtils.getLastMonthDate(date));
        return calculateBal(balList, convertCurId, date);
    }

    /**
     * 获取指定月份最近一天内部账户余额
     *
     * @param authCorpIds  有资源权限的单位
     * @param convertCurId 转换币别id
     * @param date         日期
     * @return 获取指定月份最近一天内部账户余额
     */
    public BigDecimal getInnerAccBalByMonth(List<String> authCorpIds, String convertCurId, Date date) {
        // 获取当前日期月的第一天
        String startDate = DateUtils.formatDate(DateUtils.getFirstMonthDate(date));
        // 获取当前日期下一个月的第一天
        String endDate = DateUtils.formatDate(DateUtils.getFirstMonthDate( DateUtils.addMonth(date, 1)));
        List<HomeBalCalculateResultVO> balList = reportAccountBalDao.getInnerAccountBalByMonth(authCorpIds, startDate, endDate);
        return calculateBal(balList, convertCurId, date);
    }

    /**
     * 获取内部账户余额
     *
     * @param authCorpIds  有资源权限的单位
     * @param convertCurId 转换币别id
     * @param date         日期
     * @param type         标识。1-代表当月当天，0-往年月的月末
     * @return 获取内部账户余额
     */
    private BigDecimal getInnerAccBal(List<String> authCorpIds, String convertCurId, Date date, String type) {
        if ("1".equalsIgnoreCase(type)) {
            return getInnerAccBalByMonth(authCorpIds, convertCurId, date);
        } else {
            return getInnerAccBalByLastDay(authCorpIds, convertCurId, date);
        }
    }

    /**
     * 获取内部账户同比增长率
     *
     * @param authCorpIds  有资源权限的单位
     * @param convertCurId 转换币别id
     * @param date         日期
     * @param bal          下一年金额
     * @return 同比增长率
     */
    public BigDecimal getInnerAccYearOnYear(List<String> authCorpIds, String convertCurId, Date date, BigDecimal bal, String type) {
        // 获取上年余额
        BigDecimal yearOnYearBal = getInnerAccBal(authCorpIds, convertCurId, DateUtils.addMonth(date, -12), type);
        return NumberUtil.calculateYearOnYear(bal, yearOnYearBal);
    }

    /**
     * 获取内部账户环比增长率
     *
     * @param authCorpIds  有资源权限的单位
     * @param convertCurId 转换币别id
     * @param date         日期
     * @param bal          下一月金额
     * @return 环比增长率
     */
    public BigDecimal getInnerAccMonthOnMonth(List<String> authCorpIds, String convertCurId, Date date, BigDecimal bal, String type) {
        // 获取上月余额
        BigDecimal monthOnMonthBal = getInnerAccBal(authCorpIds, convertCurId, DateUtils.addMonth(date, -1), type);
        return NumberUtil.calculateMonthOnMonth(bal, monthOnMonthBal);
    }

    /**
     * 获取银行账户同比增长率
     *
     * @param authCorpIds  有资源权限的单位集合
     * @param convertCurId 转换币别id
     * @param date         余额日期
     * @param bal          下一年金额
     * @return 同比增长率
     */
    public BigDecimal getBankAccYearOnYear(List<String> authCorpIds, String convertCurId, Date date, BigDecimal bal, String type) {
        // 获取上年余额
        BigDecimal yearOnYearBal = getBankAccBal(authCorpIds, convertCurId, DateUtils.addMonth(date, -12), type);
        return NumberUtil.calculateYearOnYear(bal, yearOnYearBal);
    }

    /**
     * 获取银行账户环比增长率
     *
     * @param authCorpIds  有资源权限的单位集合
     * @param convertCurId 转换币别id
     * @param date         余额日期
     * @param bal          下一月金额
     * @return 环比增长率
     */
    public BigDecimal getBankAccMonthOnMonth(List<String> authCorpIds, String convertCurId, Date date, BigDecimal bal, String type) {
        // 获取上月余额
        BigDecimal monthOnMonthBal = getBankAccBal(authCorpIds, convertCurId, DateUtils.addMonth(date, -1), type);
        return NumberUtil.calculateMonthOnMonth(bal, monthOnMonthBal);
    }

    /**
     * 获取银行资金概览信息
     *
     * @param user   用户对象
     * @param corpId 单位id
     * @return 结果
     */
    public Object getBankAccountCapital(ICurrentUser user, String corpId) {
        UserDTO userDTO = userService.getByLoginName(user.getLoginName());
        // 获取本位币，默认为人民币
        BtCurrencyDTO natualCurrency = currencyService.getNatualCurrency();
        // 转换币别id
        String convertCurId = natualCurrency.getId();
        // 用户所属单位，部分用户所属单位为空
        CorpDTO corp = userDTO.getCorp();
        if (!EmptyUtils.isEmpty(corp) && StringUtils.isNotEmpty(corp.getCurId())) {
            convertCurId = corp.getCurId();
        }
        Map<String, Object> capital = Maps.newHashMap();
        Map<String, Object> toDayMap = Maps.newHashMap();
        // 获取该用户有资源权限的单位集合
        List<String> authCorpIds = corpService.getUserCorpIds(ResourceConstant.APP_BI_INDEX_QUERY, user.getId());
        // 设置当日收入支出流水
        StopWatch stopWatch = new StopWatch("资金概况");

        stopWatch.start("getToDayInOutAmt");
        List<HomeBalCalculateResultVO> list = reportAccountBalDao.getToDayInOutAmt(authCorpIds);
        stopWatch.stop();
        logger.info(" getToDayInOutAmt all spends : " + stopWatch.getTotalTimeMillis() + "毫秒");
        BigDecimal inDayAmt = BigDecimal.ZERO;
        BigDecimal outDayAmt = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(list)) {
            for (HomeBalCalculateResultVO item : list) {
                String curId = item.getCurId();
                BigDecimal inBal = item.getInBal();
                BigDecimal outBal = item.getOutBal();
                if (curId.equalsIgnoreCase(corp.getCurId())) {
                    inDayAmt = inDayAmt.add(inBal);
                    outDayAmt = outDayAmt.add(outBal);
                } else {
                    BigDecimal rate = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, convertCurId, DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                    if (null == rate) {
                        rate = BigDecimal.ONE;
                    }
                    inDayAmt = inDayAmt.add(inBal.multiply(rate));
                    outDayAmt = outDayAmt.add(outBal.multiply(rate));
                }
            }
        }
        toDayMap.put("inDayAmt", toTenThousandYuan(inDayAmt));
        toDayMap.put("outDayAmt", toTenThousandYuan(outDayAmt));

        // 设置当日余额信息
        stopWatch.start("getToDayBal");
        List<HomeBalCalculateResultVO> toDayBalList = reportAccountBalDao.getToDayBal(authCorpIds);
        BigDecimal toDayBal = calculateBal(toDayBalList, convertCurId, DateUtils.getDateTime());
        toDayMap.put("toDayBal", toTenThousandYuan(toDayBal));
        capital.put("toDay", toDayMap);
        stopWatch.stop();
        logger.info(" getToDayBal all spends : " + stopWatch.getTotalTimeMillis() + "毫秒");

        // 设置存款信息
        stopWatch.start("getDayBankDeposit");
        List<DepositReportCommonDTO> depositList = getDayBankDeposit(user, corpId);
        capital.put("depositList", depositList);
        stopWatch.stop();
        logger.info(" getDayBankDeposit all spends : " + stopWatch.getTotalTimeMillis() + "毫秒");

        stopWatch.start("getSevenDayDeposit");
        BigDecimal endDepositBal = choBankFinanceService.getSevenDayDeposit(user, ResourceConstant.APP_BI_INDEX_QUERY, corpId);
        capital.put("endDepositBal", toTenThousandYuan(endDepositBal));
        stopWatch.stop();
        logger.info(" getSevenDayDeposit all spends : " + stopWatch.getTotalTimeMillis() + "毫秒");

        stopWatch.start("appointeDayMoney");
        BigDecimal endBillBal = accebillService.appointeDayMoney(user.getId(), ResourceConstant.APP_BI_INDEX_QUERY, DateUtils.getDate());
        capital.put("endBillBal", toTenThousandYuan(endBillBal));
        stopWatch.stop();
        logger.info(" appointeDayMoney all spends : " + stopWatch.getTotalTimeMillis() + "毫秒");
        return capital;
    }

    public List<DepositReportCommonDTO> getDayBankDeposit(ICurrentUser user, String corpId) {
        List<com.bytter.biz.bdlink.api.dto.DepositReportCommonDTO> depositReportList = bisAccBalServiceImpl.getBankAccBalByUser(user, ResourceConstant.APP_BI_INDEX_QUERY, corpId);
        for(com.bytter.biz.bdlink.api.dto.DepositReportCommonDTO dtoTest : depositReportList) {
            logger.error(" 资金概览-xy-账户类型: " + dtoTest.getBusTypeName()+",账户余额为："+dtoTest.getBalance());
        }
        // todo:数据转换
        List<DepositReportCommonDTO> depositList = BeanUtils.copyListProperties(depositReportList, DepositReportCommonDTO.class);
        for(DepositReportCommonDTO changeTest : depositList) {
            logger.error(" 转换后资金概览-xy-账户类型: " + changeTest.getBusTypeName()+",账户余额为："+changeTest.getBalance());
        }
        // 计算占比率
        for (int i = 0; i < depositList.size(); i++) {
            DepositReportCommonDTO dto = depositList.get(i);
            double percentValue = getPercentValue(depositList, i, 2);
            dto.setProportion(new BigDecimal(percentValue));
        }
        // proportion排序
        depositList.sort((o1, o2) -> o2.getProportion().compareTo(o1.getProportion()));
        List<DepositReportCommonDTO> list = depositList.subList(0, 4);
        for (DepositReportCommonDTO dto : list) {
            logger.error(" 截取后资金概览-xy-账户类型: " + dto.getBusTypeName()+",账户余额为："+dto.getBalance());
            dto.setBalance(toTenThousandYuan(dto.getBalance()));
            logger.error(" 金额转换后资金概览-xy-账户类型: " + dto.getBusTypeName()+",账户余额为："+dto.getBalance());
        }
        return list;
    }

    /**
     * 计算占比率
     *
     * @param list      数组
     * @param idx       索引
     * @param precision 精度
     * @return 占比率
     */
    public static double getPercentValue(List<DepositReportCommonDTO> list, int idx, int precision) {
        if ((list.size() - 1) < idx) {
            return 0;
        }
        BigDecimal sum = BigDecimal.ZERO;
        //求和
        for (DepositReportCommonDTO dto : list) {
            sum = sum.add(dto.getBalance());
        }
        if (sum.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        //10的2次幂是100，用于计算精度。
        double digits = Math.pow(10, precision);
        //扩大比例100
        double[] votesPerQuota = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            double temp = list.get(i).getBalance().doubleValue() / sum.doubleValue();
            double val = temp * digits * 100;
            votesPerQuota[i] = val;
        }
        //总数,扩大比例意味的总数要扩大
        double targetSeats = digits * 100;
        //再向下取值，组成数组
        double[] seats = new double[list.size()];
        for (int i = 0; i < votesPerQuota.length; i++) {
            seats[i] = Math.floor(votesPerQuota[i]);
        }
        //再新计算合计，用于判断与总数量是否相同,相同则占比会100%
        double currentSum = 0;
        for (int i = 0; i < seats.length; i++) {
            currentSum += seats[i];
        }
        //余数部分的数组:原先数组减去向下取值的数组,得到余数部分的数组
        double[] remainder = new double[list.size()];
        for (int i = 0; i < seats.length; i++) {
            remainder[i] = votesPerQuota[i] - seats[i];
        }
        while (currentSum < targetSeats) {
            double max = 0;
            int maxId = 0;
            int len = 0;
            for (int i = 0; i < remainder.length; ++i) {
                if (remainder[i] > max) {
                    max = remainder[i];
                    maxId = i;
                }
            }
            //对最大项余额加1
            ++seats[maxId];
            //已经增加最大余数加1,则下次判断就可以不需要再判断这个余额数。
            remainder[maxId] = 0;
            //总的也要加1,为了判断是否总数是否相同,跳出循环。
            ++currentSum;
        }
        // 这时候的seats就会总数占比会100%
        return seats[idx] / digits;
    }
}
