package com.bytter.biz.job;

import com.bytter.biz.wage.entity.NisWageFloatRatioConfig;
import com.bytter.biz.wage.entity.NisWageReleaseConfig;
import com.bytter.biz.wage.service.AutoSyncWageAllocationService;
import com.bytter.biz.wage.service.NisWageFloatRatioConfigService;
import com.bytter.biz.wage.service.NisWageReleaseConfigService;
import com.bytter.biz.wage.vo.NisWageAllocationSyncVO;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ITaskService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时任务-代发下拨金额自动同步
 * 代发工资取值逻辑：
 * 1、期望付款日期为上个月，如2025.10.01-2025.10.31；
 * 2、按照付款账号进行代发金额汇总，统计支付成功的金额；
 * 3、汇总金额根据“代发工资浮动比率配置”进行调整；
 * 4、最终计算出来的金额按照万元向上进位；
 * 5、同步时相关字段默认值如下：
 * 1)状态：已确认；
 * 2)确认人：默认为admin；
 * 3)确认时间：默认为当前存入的时间，到时分秒；
 * 4)本月单据累计下拨金额：0；
 *
 * @author fangcf
 * @date 2025-11-21
 */
@Slf4j
@Component
public class AutoSyncWageAllocationJob implements ITaskService {

    @Autowired
    private AutoSyncWageAllocationService autoSyncWageAllocationService;
    @Autowired
    private NisWageFloatRatioConfigService nisWageFloatRatioConfigService;
    @Autowired
    private NisWageReleaseConfigService nisWageReleaseConfigService;

    /**
     * 定时任务-银行账户同步冻结金额
     *
     * @param arguments 定时任务接受参数
     */
    @XxlJob("AutoSyncWageAllocationJob")
    @Override
    public void run(String arguments) {
        log.info("【代发下拨金额自动同步】定时任务开始。开始时间：{}", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
        // 获取上个月的月初与月末
        Date lastStartDate = DateUtils.getLastMonthStartTime();
        Date lastEndDate = DateUtils.getLastMonthEndTime();
        // 日期转换字符串
        String startDateStr = DateUtils.formatDate(lastStartDate);
        String endDateStr = DateUtils.formatDate(lastEndDate);

        List<NisWageAllocationSyncVO> resultList = autoSyncWageAllocationService.listWages(startDateStr, endDateStr);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("根据取值逻辑从开始时间：{}，结束时间：{}。获取代发工资数据：{}", startDateStr, endDateStr, gson.toJson(resultList));

        // 获取当前年月（当前年月既为下拨日期）
        String ym = DateUtils.format(DateUtils.getDate(), "yyyy-MM");
        // 根据下拨日期，查询代发工资下拨金额配置表
        List<NisWageAllocationSyncVO> listBy = autoSyncWageAllocationService.listByReleaseDate(ym);
        List<String> cbList = listBy.stream().map(item -> item.getCorpId() + CommonConstant.COMMA_SPLIT + item.getBankAcc()).collect(Collectors.toList());
        log.info("代发工资下拨金额配置表中下拨日期：{}，已存在以下单位账号数据：{}", ym, gson.toJson(cbList));
        // 过滤：代发工资下拨金额配置表中：单位+银行账号+月份三者合起来只能有一条唯一数据
        resultList = resultList.stream().filter(item -> !cbList.contains(item.getCorpId() + CommonConstant.COMMA_SPLIT + item.getBankAcc())).collect(Collectors.toList());

        log.info("最终待入库代发工资数据：{}", gson.toJson(resultList));
        if (!CollectionUtils.isEmpty(resultList)) {
            // 获取当前月份
            String month = String.valueOf(LocalDate.now().getMonthValue());
            NisWageFloatRatioConfig config = nisWageFloatRatioConfigService.getByMonth(month);
            // 浮动比率
            BigDecimal floatRate = BigDecimal.ZERO;
            if (null != config) {
                floatRate = config.getFloatRate();
            }
            // 代发工资下拨金额配置
            List<NisWageReleaseConfig> entityList = Lists.newArrayList();
            for (NisWageAllocationSyncVO item : resultList) {
                NisWageReleaseConfig entity = new NisWageReleaseConfig();
                BeanUtils.copyNotNullProperties(item, entity);
                // 上月下拨金额
                BigDecimal lastMonthReleaseAmount = item.getReleaseAmount();
                // 本月下拨金额 = 上月下拨 * （1 + 浮动比率（注：“代发工资浮动比率配置”菜单中配置维护））
                BigDecimal currMonthReleaseAmount = lastMonthReleaseAmount.multiply(BigDecimal.ONE.add(floatRate));
                // 金额按照万元向上进位
                currMonthReleaseAmount = currMonthReleaseAmount.divide(new BigDecimal("10000")).setScale(0, RoundingMode.CEILING).multiply(new BigDecimal("10000"));
                // 上月下拨金额
                entity.setLastMonthReleaseAmount(item.getReleaseAmount());
                // 下拨日期
                entity.setReleaseDate(DateUtils.getDate());
                // 本月下拨金额
                entity.setReleaseAmount(currMonthReleaseAmount);
                // 设置默认值
                // 状态：已确认
                entity.setStatus(1);
                // 是否生成需求清单，默认为否
                entity.setGenerateRequire(0);
                // 确认人默认为admin
                entity.setConfirmName("admin");
                entity.setConfirmTime(DateUtils.getDate());
                // 本月单据累计下拨金额
                entity.setTotalReleaseMoney(BigDecimal.ZERO);
                entity.setCreateTime(DateUtils.getDate());

                entityList.add(entity);
            }
            // 入库"代发工资下拨金额配置"表
            if (!CollectionUtils.isEmpty(entityList)) {
                nisWageReleaseConfigService.batchSave(entityList);
            }
        }
        log.info("【代发下拨金额自动同步】定时任务结束。结束时间：{}", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
    }
}
