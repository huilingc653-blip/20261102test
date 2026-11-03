package com.bytter.accountReceivable.query.service;

import com.bytter.accountReceivable.constant.ResourceConstant;
import com.bytter.accountReceivable.query.dao.DocBiDao;
import com.bytter.accountReceivable.query.vo.*;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 应收账款首页查询相关 service
 */
@Service("docBiService")
public class DocBiService {

    @DubboReference(check = false)
    private ICorpService corpService;

    @Autowired
    private DocBiDao docBiDao;

    //首页应收账款分析查询
    public ArAnalysisVo queryArAnalysis(ReceivableIndexQueryVo queryVo, ICurrentUser user){
        String corpIdInSql = getCorpInSql(queryVo, user);
        //查应收余额，合同金额，逾期金额,分币种
        List<ArAnalysisVo> arAnalysisVoList = docBiDao.queryBalanceReceivable(corpIdInSql);
        //取币种中间价，已经除了100,拿来直接乘即可
        Map<String,BigDecimal> fexRateVoMap = getMiddleRate(user);

        //乘汇率
        arAnalysisVoList.forEach(item -> {
            BigDecimal rate = fexRateVoMap.get(item.getCurId());
            if(rate != null){
                item.setWriteOffBal(item.getWriteOffBal().multiply(rate));
                item.setOverdueAmount(item.getOverdueAmount().multiply(rate));
                item.setContractMoney(item.getContractMoney().multiply(rate));
            }
        });
        //求和
        BigDecimal totalWriteOffBal = arAnalysisVoList.stream()
                .map(ArAnalysisVo::getWriteOffBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal totalOverdueAmount = arAnalysisVoList.stream()
                .map(ArAnalysisVo::getOverdueAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal totalContractMoney = arAnalysisVoList.stream()
                .map(ArAnalysisVo::getContractMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);

        //万元转换
        BigDecimal moneyUnit = new BigDecimal(queryVo.getMoneyType());
        ArAnalysisVo arAnalysisVo = new ArAnalysisVo();
        arAnalysisVo.setWriteOffBal(totalWriteOffBal.divide(moneyUnit, 2, RoundingMode.HALF_UP));
        arAnalysisVo.setOverdueAmount(totalOverdueAmount.divide(moneyUnit, 2, RoundingMode.HALF_UP));
        arAnalysisVo.setContractMoney(totalContractMoney.divide(moneyUnit, 2, RoundingMode.HALF_UP));
        return arAnalysisVo;

    }

    //首页应收账款周转分析查询
    public ArTurnoverVo queryArTurnoverAnalysis(ReceivableIndexQueryVo queryVo, ICurrentUser user){
        String corpIdInSql = getCorpInSql(queryVo, user);
        //各币种合计应收余额
        List<ArTurnoverVo> receivableList = docBiDao.queryAverageArBalance(queryVo, corpIdInSql, user.getTenantId());
        //取币种中间价，已经除了100,拿来直接乘即可
        Map<String,BigDecimal> fexRateVoMap = getMiddleRate(user);
        //应收余额乘汇率,应收金额乘汇率
        receivableList.forEach(item -> {
            BigDecimal rate = fexRateVoMap.get(item.getCurId());
            if(rate != null){
                item.setWriteOffBal(item.getWriteOffBal().multiply(rate));
                item.setReceivableMoney(item.getReceivableMoney().multiply(rate));
            }
        });
        BigDecimal moneyUnit = new BigDecimal(queryVo.getMoneyType());
        //合计应收余额
        BigDecimal totalWriteOffBal = receivableList.stream()
                .map(ArTurnoverVo::getWriteOffBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);
        //合计应收金额
        BigDecimal totalReceivableMoney = receivableList.stream()
                .map(ArTurnoverVo::getReceivableMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add)
                .divide(moneyUnit, 2, RoundingMode.HALF_UP);

        //计算平均应收账款余额，全年每月月末应收款余额之和 / 12,若不足一年，按实际月份计算，
        Map<String,Object> param = getParam(queryVo);
        BigDecimal monthBetween = new BigDecimal((long)param.get("monthBetween"));
        BigDecimal averageArBalance = totalWriteOffBal.divide(moneyUnit).divide(monthBetween, 2, RoundingMode.HALF_UP);

        ArTurnoverVo arTurnoverVo = new ArTurnoverVo();
        arTurnoverVo.setAverageArBalance(averageArBalance);

        //计算应收账款周转率=合计应收金额／平均应收账款余额
        if(averageArBalance.compareTo(BigDecimal.ZERO) > 0){
            BigDecimal turnoverNum = totalReceivableMoney.divide(averageArBalance, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            arTurnoverVo.setArTurnoverRatio(turnoverNum.setScale(2, RoundingMode.DOWN) + "%");
        }
        return arTurnoverVo;
    }

    //取中间价
    private Map<String,BigDecimal> getMiddleRate(ICurrentUser user){
        List<FexRateVo> fexRateVoList = docBiDao.queryMiddleRate(user.getTenantId());
        return fexRateVoList.stream()
                .collect(Collectors.toMap(FexRateVo::getCurId, FexRateVo::getMiddleRate));
    }

    //应收趋势分析
    public List<ArTrendVo> queryArTrendAnalysis(ReceivableIndexQueryVo queryVo, ICurrentUser user){
        String corpIdInSql = getCorpInSql(queryVo, user);
        String monthInSql = getMonthInSql(queryVo);
        //分币种月份取出历史应收余额，合同金额以及当月新增应收，当月新增核销
        List<ArTrendVo> arTrendAnalysis = docBiDao.queryArTrendAnalysis(corpIdInSql, monthInSql);
        //取币种中间价，已经除了100,拿来直接乘即可
        Map<String,BigDecimal> fexRateVoMap = getMiddleRate(user);
        //各币种乘汇率
        arTrendAnalysis.forEach(item -> {
            BigDecimal rate = fexRateVoMap.get(item.getCurId());
            if(rate != null){
                item.setWriteOffBal(item.getWriteOffBal().multiply(rate));
                item.setContractMoney(item.getContractMoney().multiply(rate));
                item.setReceivableMoney(item.getReceivableMoney().multiply(rate));
                item.setWriteOffAmount(item.getWriteOffAmount().multiply(rate));
            }
        });

        //按月汇总合计
        Map<String, ArTrendVo> arTrendVoMap = arTrendAnalysis.stream()
            .collect(Collectors.toMap(
                ArTrendVo::getMonth,
                item -> {
                    ArTrendVo vo = new ArTrendVo();
                    vo.setMonth(item.getMonth());
                    vo.setWriteOffBal(item.getWriteOffBal());
                    vo.setContractMoney(item.getContractMoney());
                    vo.setReceivableMoney(item.getReceivableMoney());
                    vo.setWriteOffAmount(item.getWriteOffAmount());
                    return vo;
                },
                (existing, newItem) ->{
                    //合并函数：累加相同月份的字段
                    existing.setWriteOffBal(addBigDecimal(existing.getWriteOffBal(), newItem.getWriteOffBal()));
                    existing.setContractMoney(addBigDecimal(existing.getContractMoney(), newItem.getContractMoney()));
                    existing.setReceivableMoney(addBigDecimal(existing.getReceivableMoney(), newItem.getReceivableMoney()));
                    existing.setWriteOffAmount(addBigDecimal(existing.getWriteOffAmount(), newItem.getWriteOffAmount()));
                    return existing;
                }
            ));
        List<ArTrendVo> result = new ArrayList<>(arTrendVoMap.values());

        //单位转换，保留两位
        BigDecimal moneyUnit = new BigDecimal(queryVo.getMoneyType());
        result.forEach(item -> {
            item.setWriteOffBal(item.getWriteOffBal().divide(moneyUnit,2, RoundingMode.HALF_UP));
            item.setContractMoney(item.getContractMoney().divide(moneyUnit,2, RoundingMode.HALF_UP));
            item.setReceivableMoney(item.getReceivableMoney().divide(moneyUnit,2, RoundingMode.HALF_UP));
            item.setWriteOffAmount(item.getWriteOffAmount().divide(moneyUnit,2, RoundingMode.HALF_UP));
        });
        result.sort(Comparator.comparing(ArTrendVo::getMonth));
        return result;
    }
    private BigDecimal addBigDecimal(BigDecimal a, BigDecimal b){
        if(a == null && b == null) {
            return BigDecimal.ZERO;
        }
        if(a == null) {
            return b;
        }
        if(b == null) {
            return a;
        }
        return a.add(b);
    }

    public List<ArRateVo> queryArRatio(ReceivableIndexQueryVo queryVo, ICurrentUser user){
        String corpIdInSql = getCorpInSql(queryVo, user);
        List<ArRateVo> result = docBiDao.queryArRatio(queryVo, corpIdInSql, user.getTenantId());
        if(!CollectionUtils.isEmpty(result)){
            BigDecimal moneyUnit = new BigDecimal(queryVo.getMoneyType());
            if(queryVo.getCategory().equals("byWriteOffBal")){
                //单位转换,设置比例
                result.forEach(item -> {
                    item.setWriteOffBal(item.getWriteOffBal().divide(moneyUnit, 2, RoundingMode.HALF_UP));
                    item.setContractMoney(item.getContractMoney().divide(moneyUnit, 2, RoundingMode.HALF_UP));
                    BigDecimal divisor = item.getContractMoney().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(1) : item.getContractMoney();
                    item.setRate(item.getWriteOffBal().divide(divisor,4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.DOWN).toPlainString() + "%");
                });
                //排序
                result.sort(Comparator.comparing(ArRateVo::getWriteOffBal));
            }
            if(queryVo.getCategory().equals("byOverDueAmount")){
                //单位转换,设置比例
                result.forEach(item -> {
                    item.setOverDueAmount(item.getOverDueAmount().divide(moneyUnit, 2, RoundingMode.HALF_UP));
                    item.setContractMoney(item.getContractMoney().divide(moneyUnit, 2, RoundingMode.HALF_UP));
                    BigDecimal divisor = item.getContractMoney().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(1) : item.getContractMoney();
                    item.setRate(item.getOverDueAmount().divide(divisor,4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.DOWN).toPlainString() + "%");
                });
                result.sort(Comparator.comparing(ArRateVo::getOverDueAmount));
            }
        }
        return result;
    }

    public List<ArDocTypeDistVo> queryArDocTypeDist(ReceivableIndexQueryVo queryVo, ICurrentUser user) {
        String corpIdInSql = getCorpInSql(queryVo, user);
        List<ArDocTypeDistVo> arDocTypeDistList = docBiDao.queryArDocTypeDist(queryVo, corpIdInSql);
        //取币种中间价，已经除了100,拿来直接乘即可
        Map<String,BigDecimal> fexRateVoMap = getMiddleRate(user);
        //各币种乘汇率
        arDocTypeDistList.forEach(item -> {
            BigDecimal rate = fexRateVoMap.get(item.getCurId());
            if(rate != null){
                item.setWriteOffBal(item.getWriteOffBal().multiply(rate));
            }
        });
        Map<String, ArDocTypeDistVo> arDocTypeDistVoMap = arDocTypeDistList.stream()
            .collect(Collectors.toMap(ArDocTypeDistVo::getName,
                item -> {
                    ArDocTypeDistVo vo = new ArDocTypeDistVo();
                    vo.setName(item.getName());
                    vo.setWriteOffBal(item.getWriteOffBal());
                    return vo;
                },
                (existing, newItem) ->{
                    //合并函数：累加相同业务类型的字段
                    existing.setWriteOffBal(addBigDecimal(existing.getWriteOffBal(), newItem.getWriteOffBal()));
                    return existing;
                }
            ));
        List<ArDocTypeDistVo> result = new ArrayList<>(arDocTypeDistVoMap.values());

        //单位转换
        BigDecimal moneyUnit = new BigDecimal(queryVo.getMoneyType());
        result.forEach(item -> item.setWriteOffBal(item.getWriteOffBal().divide(moneyUnit,2, RoundingMode.HALF_UP)));

        //total
        BigDecimal totalWriteOffBal = result.stream()
                .map(ArDocTypeDistVo::getWriteOffBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);
        //算比例
        result.forEach(item -> {
            String value = item.getWriteOffBal()
                    .multiply(new BigDecimal("100"))
                    .divide(totalWriteOffBal, 2, RoundingMode.HALF_UP)
                    .toPlainString();
            item.setValue(value);
        });
        return result;
    }

    public List<ArAgingDistVo> queryArAgingDist(ReceivableIndexQueryVo queryVo, ICurrentUser user) {
        String corpIdInSql = getCorpInSql(queryVo, user);
        List<ArAgingDistVo> arAgingDistVoList = docBiDao.queryArAgingDist(queryVo, corpIdInSql);
        //取币种中间价，已经除了100,拿来直接乘即可
        Map<String,BigDecimal> fexRateVoMap = getMiddleRate(user);
        //各币种乘汇率
        arAgingDistVoList.forEach(item -> {
            BigDecimal rate = fexRateVoMap.get(item.getCurId());
            if(rate != null){
                item.setWriteOffBal(item.getWriteOffBal().multiply(rate));
            }
        });

        //按账龄天数分组求和
        List<ArAgingDistVo> result = arAgingDistVoList.stream().collect(Collectors.collectingAndThen(
            Collectors.groupingBy(
                ArAgingDistVo::getAgingDay,
                Collectors.mapping(
                    vo -> vo.getWriteOffBal() != null ? vo.getWriteOffBal() : BigDecimal.ZERO,
                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                )
            ),
            map -> map.entrySet().stream()
                .map(entry -> {
                    ArAgingDistVo vo = new ArAgingDistVo();
                    vo.setAgingDay(entry.getKey());
                    vo.setWriteOffBal(entry.getValue());
                    return vo;
                })
                .collect(Collectors.toList())
            ));
        //元，万元转换
        BigDecimal moneyUnit = new BigDecimal(queryVo.getMoneyType());
        result.forEach(item -> item.setWriteOffBal(item.getWriteOffBal().divide(moneyUnit, 2, RoundingMode.HALF_UP)));
        return result;
    }

    private String getCorpInSql(ReceivableIndexQueryVo queryVo, ICurrentUser user){

        List<String> corpIds;
        if(StringUtils.isNotBlank(queryVo.getCorpId())){
            corpIds = new ArrayList<>(Arrays.asList(queryVo.getCorpId().split(CommonConstant.COMMA_SPLIT)));
        } else {
            //获取资源权限范围内的单位信息
            corpIds = corpService.getUserCorpIds(ResourceConstant.ACCOUNT_RECEIVABLE_QUERYDOCMANAGE_QUERY, user.getId());
        }
        return SqlUtil.getInSql(corpIds, "dm.corp_id", true);
    }

    //根据起始，终止月份得出区间内的月份
    private String getMonthInSql(ReceivableIndexQueryVo queryVo){
        String startDate = queryVo.getStartDate();
        String endDate = queryVo.getEndDate();
        String monthInSql;
        List<String> months = new ArrayList<>();
        if(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)){
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth start = YearMonth.parse(startDate, df);
            YearMonth end = YearMonth.parse(endDate, df);

            for(YearMonth month = start; !month.isAfter(end); month = month.plusMonths(1)){
                months.add(month.format(df));
            }
        }
        monthInSql = SqlUtil.getInSql(months, "d1.month", true);
        return monthInSql;
    }

    private Map<String, Object> getParam(ReceivableIndexQueryVo queryVo){

        Map<String, Object> param = new HashMap<>();
        Optional<String> startDateOptional = Optional.ofNullable(queryVo.getStartDate());
        Optional<String> endDateOptional = Optional.ofNullable(queryVo.getEndDate());

        String startDate = startDateOptional.map(item -> YearMonth.parse(item)
                        .atDay(1)
                        .toString())
                .orElse(LocalDate.now().withDayOfYear(1).toString());

        String endDate = endDateOptional.map(item -> YearMonth.parse(item)
                        .plusMonths(1)
                        .atDay(1).toString())
                .orElse(LocalDate.now().withDayOfMonth(1).toString());

        long monthBetween = ChronoUnit.MONTHS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("monthBetween", monthBetween == 0 ? 1 : monthBetween);

        return param;
    }
}
