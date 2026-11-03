package com.bytter.biz.job;

import com.bytter.biz.inneraccount.entity.BtBankAccPlanFreeze;
import com.bytter.biz.inneraccount.service.BtBankAccPlanFreezeService;
import com.bytter.biz.report.service.AutoBillBalFreezeService;
import com.bytter.biz.report.vo.AutoBillBalFreezeVO;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICalendarService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.SysCalendarDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时任务-自动新增票据计划冻结申请
 *
 * @author fangcf
 * @date 2025-10-22
 */
@Slf4j
@Component
public class AutoBillBalFreezeJob implements ITaskService {

    @Autowired
    private AutoBillBalFreezeService autoBillBalFreezeService;
    @Autowired
    private BtBankAccPlanFreezeService btBankAccPlanFreezeService;
    @DubboReference(check = false)
    private ICalendarService calendarService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 定时任务-银行账户同步冻结金额
     *
     * @param arguments 定时任务接受参数
     */
    @XxlJob("AutoBillBalFreezeJob")
    @Override
    public void run(String arguments) {
        log.info("【自动新增票据计划冻结申请】定时任务开始。开始时间：{}", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 计算下周一
        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        // 计算下周日（下周日的日期）
        LocalDate nextSunday = nextMonday.plusDays(6);
        // 格式化输出
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = nextMonday.format(formatter);
        String endDateStr = nextSunday.format(formatter);

        // 获取下周一至周日到期应付票据，挂接保证金非100%
        List<AutoBillBalFreezeVO> resultList = autoBillBalFreezeService.listBill(startDateStr, endDateStr);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!CollectionUtils.isEmpty(resultList)) {
            // 根据规则计算冻结开始时间和冻结结束时间
            this.calFreezeStartDateAndEndDate(resultList);
            log.info("根据规则计算冻结开始时间和冻结结束时间后应付票据数据：{}", gson.toJson(resultList));
            // 需要将同冻结开始日期与冻结结束日期的票据金额汇总
            Map<AutoBillBalFreezeVO, List<AutoBillBalFreezeVO>> groupBankAccMap = resultList
                    .stream()
                    .collect(Collectors.groupingBy(
                            item -> new AutoBillBalFreezeVO(item.getCorpId(), item.getCreateBy(), item.getLoginName(), item.getBankAccId(), item.getBankAcc(),
                                    item.getBankTypeId(), item.getBankTypeName(), item.getFreezeStartDate(), item.getFreezeEndDate())));

            // 定义最终到期票据冻结申请集合
            List<AutoBillBalFreezeVO> finalResultList = Lists.newArrayList();
            // 分组后开始汇总票据金额
            groupBankAccMap.forEach((k, v) -> {
                if (v.size() == 1) {
                    AutoBillBalFreezeVO vo = v.stream().findFirst().get();
                    k.setBillMoney(vo.getBillMoney());
                    k.setEndDateStr(DateUtils.format(vo.getEndDate(), DateUtils.PATTERN_CH_SIMPLE));
                } else {
                    // 多条
                    // 合计票据金额
                    BigDecimal sumBillMoney = v.stream().map(AutoBillBalFreezeVO::getBillMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    k.setBillMoney(sumBillMoney);
                    // 取到期日期区间最小日期
                    AutoBillBalFreezeVO minVo = v.stream().min(Comparator.comparing(AutoBillBalFreezeVO::getEndDate)).get();
                    // 取到期日期区间最大日期
                    AutoBillBalFreezeVO maxVo = v.stream().max(Comparator.comparing(AutoBillBalFreezeVO::getEndDate)).get();
                    // 到期日期区间范围
                    String endDateRange = DateUtils.format(minVo.getEndDate(), DateUtils.PATTERN_CH_SIMPLE) + "~" + DateUtils.format(maxVo.getEndDate(), DateUtils.PATTERN_CH_SIMPLE);
                    k.setEndDateStr(endDateRange);
                }
                finalResultList.add(k);
            });
            log.info("相同冻结开始时间和冻结结束时间合并后应付票据数据：{}", gson.toJson(finalResultList));
            this.batchSaveBillBalFreeze(finalResultList);
        }
        log.info("【自动新增票据计划冻结申请】定时任务结束。结束时间：{}", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
    }

    /**
     * 开始批量保存计划冻结申请表
     *
     * @param finalResultList 结果集
     */
    private void batchSaveBillBalFreeze(List<AutoBillBalFreezeVO> finalResultList) {
        // 获取人民币
        BtCurrencyDTO rmbCurr = btCurrencyService.getRMBCurrency();

        List<BtBankAccPlanFreeze> entityList = Lists.newArrayList();

        finalResultList.forEach(item -> {
            // 计划冻结申请表实体类
            BtBankAccPlanFreeze entity = new BtBankAccPlanFreeze();
            entity.setPlanFreezeNo(SequenceSingleton.getSequence(SequenceEnum.PLAN_FREEZE_NO));
            entity.setBankAccId(item.getBankAccId());
            entity.setBankAcc(item.getBankAcc());
            entity.setCorpId(item.getCorpId());
            // 申请冻结金额为票据金额
            entity.setApplyFreezeMoney(item.getBillMoney());
            // 实际已冻结金额
            entity.setRealFreezeMoney(BigDecimal.ZERO);
            entity.setFreezeStartDate(item.getFreezeStartDate());
            entity.setFreezeEndDate(item.getFreezeEndDate());
            // ${endDateStr}票据到期票面金额${billMoney}
            String freezeRession = item.getEndDateStr() + "票据到期票面金额" + NumberUtil.formatNumeric(item.getBillMoney());
            entity.setFreezeRession(freezeRession);
            // 票据只有人民币
            entity.setCurId(rmbCurr.getId());
            // 状态
            entity.setStatus(BtBankAccConstant.STATUE_CHECK);
            entity.setFreezeStatus(BtBankAccConstant.FREEZE_STATUS_0);
            entity.setIsFreezeComplete(CommonConstant.NO);
            entity.setIsUnfreeze(CommonConstant.NO);
            entity.setCreateBy(item.getCreateBy());
            entity.setCreateDate(DateUtils.getDate());

            entityList.add(entity);
        });

        // 批量保存
        if (!CollectionUtils.isEmpty(entityList)) {
            btBankAccPlanFreezeService.batchSave(entityList);
        }
    }

    /**
     * 计算冻结开始时间和冻结结束时间
     *
     * @param resultList 结果集
     */
    private void calFreezeStartDateAndEndDate(List<AutoBillBalFreezeVO> resultList) {
        // 结算管理-参数配置-支付结算参数【冻结开始时间】
        String freezeStartDateStr = configService.getConfig(ConfigConstant.NIS, "freezeStartDate").getValue();
        if (StringUtils.isEmpty(freezeStartDateStr)) {
            freezeStartDateStr = "11:00:00";
        }
        log.info("冻结开始时间：{}", freezeStartDateStr);
        // 结算管理-参数配置-支付结算参数【冻结结束时间】
        String freezeEndDateStr = configService.getConfig(ConfigConstant.NIS, "freezeEndDate").getValue();
        if (StringUtils.isEmpty(freezeEndDateStr)) {
            freezeEndDateStr = "16:00:00";
        }
        log.info("冻结结束时间：{}", freezeEndDateStr);
        // 汇丰银行票据到期日扣款为固定工作日次日早上10：00
        LocalTime ten_am = LocalTime.of(10, 0, 0);
        LocalTime freezeStartLocalTime = LocalTime.parse(freezeStartDateStr);
        LocalTime freezeEndLocalTime = LocalTime.parse(freezeEndDateStr);

        resultList.forEach(item -> {
            // 计算冻结开始时间
            Date freezeStartDate = this.calFreezeStartDate(item.getEndDate(), freezeStartLocalTime);
            item.setFreezeStartDate(freezeStartDate);
            // 计算冻结结束时间
            Date freezeEndDate = this.calFreezeEndDate(item.getEndDate(), item.getBankTypeName(), ten_am, freezeEndLocalTime);
            item.setFreezeEndDate(freezeEndDate);
        });
    }

    /**
     * 计算冻结开始时间：票据到期日前两个工作日早上11:00
     *
     * @param endDate              票据到期日期
     * @param freezeStartLocalTime 参数配置冻结开始时间
     * @return 冻结开始时间
     */
    private Date calFreezeStartDate(Date endDate, LocalTime freezeStartLocalTime) {
        // 冻结开始时间：票据到期日前两个工作日早上11:00
        Date freezeStartDate = DateUtils.addDay(endDate, -2);
        // 获取日历维护数据
        List<SysCalendarDTO> calendarList = calendarService.listSysCalendarByDates(null, endDate, 2);
        if (!CollectionUtils.isEmpty(calendarList)) {
            SysCalendarDTO sysCalendar = calendarList.stream().min(Comparator.comparing(SysCalendarDTO::getCreateDate)).get();
            freezeStartDate = sysCalendar.getCreateDate();
        }
        // 日期：yyyy-MM-dd
        LocalDate localStartDate = freezeStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // 拼接成完整的日期时间
        LocalDateTime localStartDateTime = LocalDateTime.of(localStartDate, freezeStartLocalTime);
        // 转成Date
        freezeStartDate = Date.from(localStartDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return freezeStartDate;
    }

    /**
     * 计算冻结结束时间：汇丰银行票据到期日扣款工作日次日早上10：00，其他银行票据到期扣款工作日下午16:00。如遇到期日是节假日，则顺延下一个工作日；
     *
     * @param endDate            票据到期日期
     * @param bankTypeName       银行类型名称
     * @param ten_am             汇丰银行固定工作日次日早上10点
     * @param freezeEndLocalTime 参数配置冻结结束时间
     * @return 冻结开始时间
     */
    private Date calFreezeEndDate(Date endDate, String bankTypeName, LocalTime ten_am, LocalTime freezeEndLocalTime) {
        // 判断票据到期日是否是工作日
        Integer workDaySign = calendarService.getWorkDaySignByTime(endDate);
        Date freezeEndDate;
        if ("汇丰银行".equals(bankTypeName)) {
            // 汇丰银行票据到期日扣款工作日次日早上10：00
            freezeEndDate = DateUtils.addDay(endDate, 1);
            if (null != workDaySign && 0 == workDaySign) {
                // 非工作日，则顺延下一个工作日
                List<SysCalendarDTO> calendarList = calendarService.listSysCalendarByDates(endDate, null, 2);
                if (!CollectionUtils.isEmpty(calendarList)) {
                    SysCalendarDTO sysCalendar = calendarList.stream().max(Comparator.comparing(SysCalendarDTO::getCreateDate)).get();
                    freezeEndDate = sysCalendar.getCreateDate();
                }
            } else {
                // 工作日
                List<SysCalendarDTO> calendarList = calendarService.listSysCalendarByDates(endDate, null, 1);
                if (!CollectionUtils.isEmpty(calendarList)) {
                    SysCalendarDTO sysCalendar = calendarList.stream().max(Comparator.comparing(SysCalendarDTO::getCreateDate)).get();
                    freezeEndDate = sysCalendar.getCreateDate();
                }
            }
            // 日期：yyyy-MM-dd
            LocalDate localEndDate = freezeEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // 拼接成完整的日期时间
            LocalDateTime localEndDateTime = LocalDateTime.of(localEndDate, ten_am);
            // 转成Date
            freezeEndDate = Date.from(localEndDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } else {
            // 其他银行票据到期扣款工作日下午16:00
            freezeEndDate = endDate;
            if (null != workDaySign && 0 == workDaySign) {
                // 非工作日，则顺延下一个工作日
                List<SysCalendarDTO> calendarList = calendarService.listSysCalendarByDates(endDate, null, 1);
                if (!CollectionUtils.isEmpty(calendarList)) {
                    SysCalendarDTO sysCalendar = calendarList.stream().max(Comparator.comparing(SysCalendarDTO::getCreateDate)).get();
                    freezeEndDate = sysCalendar.getCreateDate();
                }
            }
            // 日期：yyyy-MM-dd
            LocalDate localEndDate = freezeEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // 拼接成完整的日期时间
            LocalDateTime localEndDateTime = LocalDateTime.of(localEndDate, freezeEndLocalTime);
            // 转成Date
            freezeEndDate = Date.from(localEndDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        return freezeEndDate;
    }
}
