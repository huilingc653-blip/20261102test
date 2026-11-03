package com.bytter.biz.wage.service;

import com.bytter.biz.wage.dao.AutoSyncWageAllocationDao;
import com.bytter.biz.wage.vo.NisWageAllocationSyncVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.Base64Util;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
@Service("autoSyncWageAllocationService")
@Transactional
public class AutoSyncWageAllocationService extends BaseService<NisWageAllocationSyncVO, String> {

    @Autowired
    private AutoSyncWageAllocationDao autoSyncWageAllocationDao;
    @DubboReference(check = false)
    private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<NisWageAllocationSyncVO, String> getEntityDao() {
        return autoSyncWageAllocationDao;
    }

    /**
     * 获取代发工资数据
     *
     * @param startDateStr 开始日期
     * @param endDateStr   结束日期
     */
    public List<NisWageAllocationSyncVO> listWages(String startDateStr, String endDateStr) {
        List<NisWageAllocationSyncVO> wagesList = autoSyncWageAllocationDao.listWages(startDateStr, endDateStr);

        BtCurrencyDTO rmbCurr = btCurrencyService.getRMBCurrency();
        // 定义key为币别id，value为折合人民币的汇率
        Map<String, BigDecimal> exMap = Maps.newHashMap();
        // 代发工资金额解密且汇率转换
        wagesList.forEach(wage -> {
            BigDecimal releaseAmount = BigDecimal.ZERO;
            try {
                // 代发工资金额解密
                releaseAmount = new BigDecimal(new String(Base64Util.decodeFromString(wage.getWagesVerify()), StandardCharsets.UTF_8));
            } catch (Exception e) {
                log.error("代发工资金额解密异常", e);
                e.printStackTrace();
            }

            // 获取汇率
            BigDecimal exchangeRate = exMap.computeIfAbsent(
                    wage.getCurId(),
                    curId -> Optional.ofNullable(fcExcrateAdjustService.getExchangeRateByNoCheck(wage.getCurId(), rmbCurr.getId(), DateUtils.getDate(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT)
                    ).orElse(BigDecimal.ONE));
            // 存在外币时，转成人民币
            wage.setReleaseAmount(releaseAmount.multiply(exchangeRate));
        });

        // 按照付款账号进行代发金额汇总
        Map<NisWageAllocationSyncVO, List<NisWageAllocationSyncVO>> groupMap = wagesList
                .stream()
                .collect(Collectors.groupingBy(item ->
                        new NisWageAllocationSyncVO(item.getCorpId(), item.getBankAcc(), item.getBankAccName(), item.getBankCode(), item.getBankName(), item.getCurId())));

        // 定义根据付款账号汇总后的集合
        List<NisWageAllocationSyncVO> resultList = Lists.newArrayList();
        groupMap.forEach((k, v) -> {
            // 汇总
            BigDecimal sum = v.stream().map(NisWageAllocationSyncVO::getReleaseAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            k.setReleaseAmount(sum);
            resultList.add(k);
        });
        return resultList;
    }

    /**
     * 根据下拨日期，查询代发工资下拨金额配置表。（单位+银行账号+月份三者合起来只能有一条唯一数据）
     *
     * @param ym 年月
     * @return 代发工资下拨金额配置表
     */
    public List<NisWageAllocationSyncVO> listByReleaseDate(String ym) {
        return autoSyncWageAllocationDao.listByReleaseDate(ym);
    }
}