package com.bytter.credit.domain.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.YearBankCreditPlanDao;
import com.bytter.credit.domain.vo.BankCreditPlanTraceVO;
import com.bytter.credit.domain.vo.YearBankCreditPlanVO;
import com.bytter.system.api.ICellManageService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CellManageDto;
import com.bytter.system.api.dto.CorpDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 银行授信规划及月度跟踪表服务
 *
 * @author loveCamille
 */
@Service
@RequiredArgsConstructor
public class BankCreditPlanTraceService {

    @DubboReference
    private ICorpService corpService;

    @DubboReference
    private ICellManageService cellManageService;

    private final YearBankCreditPlanDao yearBankCreditPlanDao;

    public DataTablesOutput<BankCreditPlanTraceVO> dataTable(DataTablesInput input,
                                                             ICurrentUser currentUser) {
        List<String> authCorpList;
        Map<String, Object> paramMap = input.getFormdata();
        if (paramMap.containsKey("corpId")) {
            authCorpList = new ArrayList<>(Arrays.asList(paramMap.get("corpId").toString().split(",")));
        } else {
            authCorpList = corpService.getUserCorpIds(RationConstant.CMS_RAT_YEARFINPLANREGISTER_QUERY, currentUser.getId());
        }

        String monthDate = input.getFormdata().get("date").toString();
        if (CharSequenceUtil.isBlank(monthDate)) {
            monthDate = DateUtil.format(DateUtil.offsetMonth(DateUtil.date(), -1), DatePattern.NORM_MONTH_FORMAT);
        }
        String lastYearMonthDate = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(monthDate, DatePattern.NORM_MONTH_FORMAT), -12), DatePattern.NORM_MONTH_FORMAT);

        // 满足条件的所有授信合同信息
        DataTablesOutput<BankCreditPlanTraceVO> ratBkRationResult = yearBankCreditPlanDao.ratBkrationQuery(input, authCorpList);
        List<BankCreditPlanTraceVO> ratBkRationList = ratBkRationResult.getData();
        if (CollUtil.isEmpty(ratBkRationList)) return ratBkRationResult;

        List<BankCreditPlanTraceVO> list = new ArrayList<>();
        List<String> ratCodeList = ratBkRationList.stream().map(BankCreditPlanTraceVO::getId).collect(Collectors.toList());

        // 额度占用情况
        Map<String, List<BankCreditPlanTraceVO>> ratUrationMap = yearBankCreditPlanDao.listRatUration(ratCodeList).stream()
                .collect(Collectors.groupingBy(BankCreditPlanTraceVO::getId));

        // 按照 单位 -  银行编号 分组
        List<BankCreditPlanTraceVO> ratBkrationDetailList = yearBankCreditPlanDao.listRatBkrationDetail(ratCodeList);
        for (BankCreditPlanTraceVO bankCreditPlanTraceVO : ratBkRationList) {
            List<BankCreditPlanTraceVO> detailList = ratBkrationDetailList.stream()
                    .filter(k -> k.getId().equals(bankCreditPlanTraceVO.getId())).collect(Collectors.toList());
            Map<String, BankCreditPlanTraceVO> creditTypeMap = detailList.stream().collect(Collectors.toMap(BankCreditPlanTraceVO::getCreditType, Function.identity()));

            // 上年同期额度占用
            List<BankCreditPlanTraceVO> lastPeriodRatUrationList = yearBankCreditPlanDao
                    .listRatUrationByCorpAndBank(bankCreditPlanTraceVO.getCorpId(), bankCreditPlanTraceVO.getBankCode(),
                            lastYearMonthDate + "-01",
                            DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(lastYearMonthDate, DatePattern.NORM_MONTH_FORMAT), 1),  DatePattern.NORM_MONTH_FORMAT) + "-01");

            // 本年已使用额度
            List<BankCreditPlanTraceVO> currentYearUsedLimitList = yearBankCreditPlanDao
                    .listBillUrationByCorpAndBank(bankCreditPlanTraceVO.getCorpId(), bankCreditPlanTraceVO.getBankCode(), monthDate);

            // 上年已使用额度
            List<BankCreditPlanTraceVO> lastYearUsedLimitList = yearBankCreditPlanDao.listRatUrationByCorpAndBank(bankCreditPlanTraceVO.getCorpId(), bankCreditPlanTraceVO.getBankCode(),
                    (bankCreditPlanTraceVO.getYear() - 1) + "-01-01", bankCreditPlanTraceVO.getYear() + "-01-01");

            // 当前授信占用
            List<BankCreditPlanTraceVO> ratUrationList = Optional.ofNullable(ratUrationMap.get(bankCreditPlanTraceVO.getId())).orElse(new ArrayList<>());

            for (BankCreditPlanTraceVO detail : detailList) {
                // 本期已使用额度
                BigDecimal currentPeriodUsedLimit;
                // 上年同期-已使用额度
                BigDecimal lastPeriodUsedLimit;
                // 本年-已使用额度
                BigDecimal currentYearUsedLimit;
                // 上年已使用额度
                BigDecimal lastYearUsedLimit;
                if ("1".equals(detail.getCreditType())) {
                    detail.setCreditBankName(detail.getCreditBankName() + "（敞口）");
                    detail.setPlanCreditBankName(detail.getPlanCreditBankName() + "（敞口）");
                    // 其中：流贷额度
                    BankCreditPlanTraceVO creditPlanTrace = creditTypeMap.get("3");
                    if (creditPlanTrace != null) {
                        detail.setFlowLoanLimit(creditPlanTrace.getMaxMoney());
                    }
                    // 本期已使用额度
                    currentPeriodUsedLimit = ratUrationList.stream()
                            .filter(k -> "1".equals(k.getCreditType()) || "3".equals(k.getCreditType()))
                            .map(BankCreditPlanTraceVO::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    lastPeriodUsedLimit = lastPeriodRatUrationList.stream()
                            .filter(k -> "1".equals(k.getCreditType()) || "3".equals(k.getCreditType()))
                            .map(BankCreditPlanTraceVO::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    currentYearUsedLimit = currentYearUsedLimitList.stream()
                            .filter(k -> "1".equals(k.getCreditType()) || "3".equals(k.getCreditType()))
                            .map(BankCreditPlanTraceVO::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    lastYearUsedLimit = lastYearUsedLimitList.stream()
                            .filter(k -> "1".equals(k.getCreditType()) || "3".equals(k.getCreditType()))
                            .map(BankCreditPlanTraceVO::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                } else if ("2".equals(detail.getCreditType())) {
                    detail.setCreditBankName(detail.getCreditBankName() + "（低风险）");
                    detail.setPlanCreditBankName(detail.getPlanCreditBankName() + "（低风险）");
                    // 本期已使用额度
                    currentPeriodUsedLimit = ratUrationList.stream()
                            .filter(k -> "2".equals(k.getCreditType()))
                            .map(BankCreditPlanTraceVO::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    lastPeriodUsedLimit = lastPeriodRatUrationList.stream()
                            .filter(k -> "2".equals(k.getCreditType()))
                            .map(BankCreditPlanTraceVO::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    currentYearUsedLimit = currentYearUsedLimitList.stream()
                            .filter(k -> "2".equals(k.getCreditType()))
                            .map(BankCreditPlanTraceVO::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    lastYearUsedLimit = lastYearUsedLimitList.stream()
                            .filter(k -> "2".equals(k.getCreditType()))
                            .map(BankCreditPlanTraceVO::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                } else {
                    continue;
                }
                BankCreditPlanTraceVO creditPlanTrace = creditTypeMap.get(detail.getCreditType());
                if (creditPlanTrace != null) {
                    BigDecimal comprehensiveLimit = creditPlanTrace.getMaxMoney();
                    if (comprehensiveLimit == null || comprehensiveLimit.compareTo(BigDecimal.ZERO) == 0) {
                        comprehensiveLimit = creditPlanTrace.getMoney();
                    }
                    // 综合额度
                    detail.setComprehensiveLimit(comprehensiveLimit);
                }
                BankCreditPlanTraceVO result = BeanUtil.copyProperties(detail, BankCreditPlanTraceVO.class);
                result.setYear(bankCreditPlanTraceVO.getYear());

                result.setCooperationType(bankCreditPlanTraceVO.getCooperationType());
                result.setYearPurchase(bankCreditPlanTraceVO.getYearPurchase());
                result.setMergeAcquisitionInvest(bankCreditPlanTraceVO.getMergeAcquisitionInvest());
                result.setCreditRequire(bankCreditPlanTraceVO.getCreditRequire());
                result.setSolveMeasure(bankCreditPlanTraceVO.getSolveMeasure());
                result.setExistDifficulty(bankCreditPlanTraceVO.getExistDifficulty());
                // 额度变化情况
                setCreditChange(result);
                // 银行额度
                result.setBankLimit(bankCreditPlanTraceVO.getBankLimit());
                // 授信额度
                result.setPlanCreditLimit(bankCreditPlanTraceVO.getPlanCreditLimit());
                // 其中：银承额度
                result.setBankAcceptanceBillLimit(result.getPlanCreditLimit());
                // 合同起止日
                result.setContractStartEndDate(bankCreditPlanTraceVO.getContractStartEndDate());
                // 本期：已使用额度
                result.setCurrentPeriodUsedLimit(currentPeriodUsedLimit);
                // 本期：剩余额度
                result.setCurrentPeriodAvailableLimit(result.getPlanCreditLimit().subtract(currentPeriodUsedLimit));
                // 上年同期-已使用额度
                result.setLastPeriodUsedLimit(lastPeriodUsedLimit);
                // 本年-已使用额度
                result.setCurrentYearUsedLimit(currentYearUsedLimit);
                // 本年-可使用额度
                result.setCurrentYearAvailableLimit(result.getPlanCreditLimit().subtract(currentPeriodUsedLimit));
                // 上年已使用额度
                result.setLastYearUsedLimit(lastYearUsedLimit);

                // 授信完成率
                if (Objects.nonNull(result.getComprehensiveLimit()) && Objects.nonNull(result.getCreditRequire())) {
                    BigDecimal creditCompleteRate = result.getComprehensiveLimit().divide(result.getCreditRequire(), 4, RoundingMode.HALF_UP);
                    result.setCreditCompleteRate(creditCompleteRate);
                }
                list.add(result);
            }
        }

        dealCorpArea(list);

        list.sort(Comparator.comparing(BankCreditPlanTraceVO::getCorpId).thenComparing(BankCreditPlanTraceVO::getBankCode));

        ratBkRationResult.setData(list);

        return ratBkRationResult;
    }

    /**
     * <p>额度变动情况</p>
     * <p>本年度计划授信额度-上一年度计划授信额度；（计划额度取值路径为【信贷融资--授信管理--授信基础数据--银行授信年度计划维护】</p>
     * @param data 数据
    */
    private void setCreditChange(BankCreditPlanTraceVO data) {
        int lastYear = data.getYear() - 1;
        List<YearBankCreditPlanVO> currentYearCreditPlanList = yearBankCreditPlanDao.listByCorpAndYear(data.getCorpId(), data.getBankCode(), data.getYear())
                .stream().filter(k -> k.getCreditType().equals(data.getCreditType())).collect(Collectors.toList());
        List<YearBankCreditPlanVO> lastYearCreditPlanList = yearBankCreditPlanDao.listByCorpAndYear(data.getCorpId(), data.getBankCode(), lastYear)
                .stream().filter(k -> k.getCreditType().equals(data.getCreditType())).collect(Collectors.toList());

        if (CollUtil.isEmpty(currentYearCreditPlanList) ||  CollUtil.isEmpty(lastYearCreditPlanList)) return ;

        // 求和
        BigDecimal lastSum = lastYearCreditPlanList.stream().map(YearBankCreditPlanVO::getCreditAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal currentSum = currentYearCreditPlanList.stream().map(YearBankCreditPlanVO::getCreditAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 变动量 = 本期 - 上期
        BigDecimal diff = currentSum.subtract(lastSum);
        data.setCreditChange(diff);
    }


    private void dealCorpArea(List<BankCreditPlanTraceVO> list) {
        CellManageDto cellManageDto = cellManageService.getCellManageByCodeOrName1("6000", "大区");
        Map<String, CorpDTO> corpMap = corpService.listEffectiveCorp().stream()
                .collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
        // 所有的大区
        Map<String, CorpDTO> areaMap = corpMap.values().stream().filter(item -> item.getUnitCm().equals(cellManageDto.getId()))
                .collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
        for (BankCreditPlanTraceVO bankCreditPlanTraceVO : list) {
            // 当前单位信息
            CorpDTO corp = corpMap.get(bankCreditPlanTraceVO.getCorpId());
            // 当前单位不为null 且 不是大区单位，循环判断父级单位，直至父级单位为大区单位
            while (corp != null && !areaMap.containsKey(corp.getParentId())) {
                corp = corpMap.get(corp.getParentId());
            }
            if (corp == null) continue;
            CorpDTO area = areaMap.get(corp.getParentId());
            if (Objects.nonNull(area)) {
                bankCreditPlanTraceVO.setAreaName(area.getName());
            }
        }
    }

}
