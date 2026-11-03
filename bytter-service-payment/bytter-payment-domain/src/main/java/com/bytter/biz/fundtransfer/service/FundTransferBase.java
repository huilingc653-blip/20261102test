package com.bytter.biz.fundtransfer.service;

import com.bytter.biz.allocation.vo.BisAccGatherPolicyVO;
import com.bytter.biz.fundtransfer.dao.FundTransferDao;
import com.bytter.biz.fundtransfer.enums.BankConstants;
import com.bytter.biz.fundtransfer.util.DateUtil;
import com.bytter.biz.inneraccount.entity.BisAccGatherPolicy;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.payment.api.dto.BtBankAccDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.DateUtils;
import com.google.common.collect.Lists;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资金调拨定时扫描服务基类
 * 定时下拨
 *
 * @author fangcf
 * @date 2026-04-13
 */
public class FundTransferBase extends BaseService<BisAccGatherPolicy, String> {

    /**
     * 节假日规则中向前/向后查找工作日的最大天数上限。
     * <p>防止日历数据异常（例如整年 work_day_sign 均为 0）时 while 循环无法收敛、挂死定时任务线程。
     */
    private static final int MAX_WORKDAY_ADJUST_DAYS = 30;

    @Resource
    private FundTransferDao fundTransferDao;

    /**
     * 获取List<BisAccGatherPolicyVO> 中的所有账号信息
     *
     * @param accGatherPolicys
     * @return java.util.Map<java.lang.String, com.bytter.facade.netbank.dto.BtBankAccDTO>
     */
    protected Map<String, BtBankAccDTO> getAllBankAccInfo(List<BisAccGatherPolicyVO> accGatherPolicys) {
        // 定义合并所有子账号与父账号
        List<String> bankIds = Lists.newArrayList();
        // 所有子账号
        List<String> bankAccIdList = accGatherPolicys.stream().map(BisAccGatherPolicyVO::getBankAccId).distinct().collect(Collectors.toList());
        // 所有父账号
        List<String> parentAccIdList = accGatherPolicys.stream().map(BisAccGatherPolicyVO::getParentAccId).distinct().collect(Collectors.toList());

        bankIds.addAll(bankAccIdList);
        bankIds.addAll(parentAccIdList);
        // 获取银行账号信息
        List<BtBankAccDTO> bais = fundTransferDao.getBankAccByBankAccIds(bankIds);
        Map<String, BtBankAccDTO> bankAccInfoMap = new HashMap();
        for (BtBankAccDTO bai : bais) {
            bankAccInfoMap.put(bai.getBankAcc() + bai.getCurId(), bai);
        }
        return Collections.unmodifiableMap(bankAccInfoMap);
    }

    /**
     * 根据策略获取下次运行时间
     *
     * @param accGatherPolicy
     * @return java.util.Date
     */
    protected Date tryGetNextRunTime(BisAccGatherPolicyVO accGatherPolicy) {
        BisGatherPolicy bgp = accGatherPolicy.getBisGatherPolicy();
        // 判断当前时间是否大于设定的生效时间
        if (DateUtils.getDate().before(bgp.getDefDate())) {//未到运行时间
            return null;
        }
        long days = 0;
        int addFieldType = -1;
        switch (bgp.getFrqMode().intValue()) {
            //每天执行
            case BankConstants.POLICY_BY_DAY:
                addFieldType = Calendar.DAY_OF_MONTH;
                break;
            //每星期执行
            case BankConstants.POLICY_BY_WEEK:
                switch (bgp.getSerialDay().intValue()) {
                    case 1:
                        days = 1;
                        break;
                    case 2:
                        days = 2;
                        break;
                    case 4:
                        days = 3;
                        break;
                    case 8:
                        days = 4;
                        break;
                    case 16:
                        days = 5;
                        break;
                    case 32:
                        days = 6;
                        break;
                    case 64:
                        days = 7;
                        break;
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == days) {
                    //当前日是设定的日期
                    addFieldType = Calendar.WEEK_OF_MONTH;
                }
                break;
            //每月执行
            case BankConstants.POLICY_BY_MONTH:
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                //用于判断今天是不是本月最后一天，对当天calendar.get(Calendar.DATE) + 1)进行加1操作，calendar.get(Calendar.DAY_OF_MONTH)值成了1号，那么就是最后一天
                calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
                // 如果策略的日期和今天匹配,进行归集或下拨操作
                if (bgp.getSerialDay().intValue() == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                    //当前日是设定的日
                    addFieldType = Calendar.MONTH;
                }
                //当前执行时间是本月最后一天也要计算下次执行时间
                else if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                    addFieldType = Calendar.MONTH;
                } else if (DateUtil.isLastWork()) {
                    //月归集 设置最后一天归集,如果是 最后一天归集  且选择跳过非工作日 归集时间应该为每个月的最后一个工作日归集
                    addFieldType = Calendar.MONTH;
                }
                break;
        }
        if (addFieldType != -1) {
            //符合当天运行日期，进一步判断运行时间
            return doGetNextRuntime(bgp, addFieldType);
        }
        return null;
    }

    private Date doGetNextRuntime(BisGatherPolicy bgp, int addFieldType) {
        Calendar nextTime = Calendar.getInstance();
        Calendar cToday = Calendar.getInstance();
        switch (bgp.getFrqMode().intValue()) {
            //按月执行需要特殊判断
            case BankConstants.POLICY_BY_MONTH:
                //设置下次执行日期
                int month = cToday.get(Calendar.MONTH) + 1;
                int year = cToday.get(Calendar.YEAR);
                //跨年时特殊判断
                if (month == 12) {
                    month = 1;
                    year++;
                }
                nextTime.set(Calendar.YEAR, year);
                nextTime.set(Calendar.MONTH, month);
                //策略配置时间为31号时（更新下次时间为下月的最后一天）
                if (bgp.getSerialDay().intValue() == 31) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, 1);
                    int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    nextTime.set(Calendar.DAY_OF_MONTH, lastDay);
                } else {
                    nextTime.set(Calendar.DAY_OF_MONTH, bgp.getSerialDay());
                }
                break;
            //按周执行和每日执行只需要+1就行
            default:
                nextTime.add(addFieldType, 1);
                break;
        }
        //启动具体时间（时分秒）根据策略生效时间生成
        Calendar launchTime = Calendar.getInstance();
        launchTime.setTime(bgp.getDefDate());
        nextTime.set(Calendar.HOUR_OF_DAY, launchTime.get(Calendar.HOUR_OF_DAY));
        nextTime.set(Calendar.MINUTE, launchTime.get(Calendar.MINUTE));
        nextTime.set(Calendar.SECOND, launchTime.get(Calendar.SECOND));
        return nextTime.getTime();
    }

    /**
     * 检查节假日跳过规则(跳过执行返回false 可以执行返回true)
     *
     * @param agp BisAccGatherPolicyVO
     * @return boolean
     */
    protected Boolean checkFilterHolidays(BisAccGatherPolicyVO agp, Boolean isWorkSign) throws Exception {
        Boolean flag = false;
        //如果当前时间是工作日或着策略为不跳过节假日AutoFilterHolidays=0
        if (isWorkSign || "0".equals(agp.getBisGatherPolicy().getAutoFilterHolidays())) {
            flag = true;
        } else {
            //是否智能跳过节假日 1-是
            if ("1".equals(agp.getBisGatherPolicy().getAutoFilterHolidays())) {
                //跳过节假日规则:1,不执行
                if ("1".equals(agp.getBisGatherPolicy().getAutoFilterHolidaysRule())) {
                    //非工作日,不执行,返回异常外部获取后更新下一次执行的日期
                    throw new Exception("当前为节假日，且本条数据节假日策略为：节假日不执行");
                }
                //跳过节假日规则:2,提前执行（只能判断下次执行时间了）
                else if ("2".equals(agp.getBisGatherPolicy().getAutoFilterHolidaysRule())) {
                    //判断计算出的下次归集日是否为工作日，如果不是工作日，更新为下次调拨日的前一个工作日
                    boolean nextFlag = fundTransferDao.isWorkDaySign(DateUtil.getFormattingDateTime(agp.getExpectedNextTime(), 7));
                    int backDays = 0;
                    while (!nextFlag) {
                        if (++backDays > MAX_WORKDAY_ADJUST_DAYS) {
                            throw new Exception("提前执行失败：向前查找工作日已超过" + MAX_WORKDAY_ADJUST_DAYS
                                    + "天仍未命中，请检查租户日历(sys_calendar)是否维护。策略："
                                    + agp.getBisGatherPolicy().getGatherPolicyCode()
                                    + "，起始日期：" + DateUtil.getFormattingDateTime(agp.getExpectedNextTime(), 7));
                        }
                        agp.setExpectedNextTime(DateUtil.calcDate(agp.getExpectedNextTime(), -1));
                        nextFlag = fundTransferDao.isWorkDaySign(DateUtil.getFormattingDateTime(agp.getExpectedNextTime(), 7));
                    }
                    fundTransferDao.updateBisAccGatherPolicyNextTime(agp);
                    flag = false;
                }
                //跳过节假日规则:3,顺延执行
                else if ("3".equals(agp.getBisGatherPolicy().getAutoFilterHolidaysRule())) {
                    //判断本次获取出的执行日nextTime是否为工作日，如果不是工作日，更新为下次归集日的下一个工作日
                    boolean nextFlag = fundTransferDao.isWorkDaySign(DateUtil.getFormattingDateTime(agp.getNextTime(), 7));
                    agp.setExpectedNextTime(agp.getNextTime());
                    int forwardDays = 0;
                    while (!nextFlag) {
                        if (++forwardDays > MAX_WORKDAY_ADJUST_DAYS) {
                            throw new Exception("顺延执行失败：向后查找工作日已超过" + MAX_WORKDAY_ADJUST_DAYS
                                    + "天仍未命中，请检查租户日历(sys_calendar)是否维护。策略："
                                    + agp.getBisGatherPolicy().getGatherPolicyCode()
                                    + "，起始日期：" + DateUtil.getFormattingDateTime(agp.getNextTime(), 7));
                        }
                        agp.setExpectedNextTime(DateUtil.calcDate(agp.getExpectedNextTime(), 1));
                        nextFlag = fundTransferDao.isWorkDaySign(DateUtil.getFormattingDateTime(agp.getExpectedNextTime(), 7));
                    }
                    fundTransferDao.updateBisAccGatherPolicyNextTime(agp);
                    flag = false;
                }
            }
        }
        return flag;
    }


    @Override
    public BaseDao<BisAccGatherPolicy, String> getEntityDao() {
        return null;
    }
}
