package com.bytter.increment.domain.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.domain.dao.ChoProductReqQueryDao;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.entity.ChoProductRedeemDetail;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductReqQueryVo;
import com.bytter.increment.domain.vo.ChoProductReqUncollectedVo;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Service
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Service
@Transactional
public class ChoProductReqQueryService extends BaseService<ChoProductReq,String> implements ITaskService {
    @Autowired
    private ChoProductReqQueryDao choProductReqQueryDao;
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private IConfigService configServiceImpl;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @Override
    public BaseDao<ChoProductReq, String> getEntityDao() {
        return choProductReqQueryDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        String moneyUint = (String) input.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)){
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTREQ_CHOPRODUCTREQ_BUS_QUERY, user.getId()), "pr.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput<ChoProductReqQueryVo> output = choProductReqQueryDao.getDataTablesUnion(input,sql.toString());
        // 注入远程信息
        Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
        Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
        List<ChoProductReqQueryVo> reqList = output.getData();
        if (input.getFormdata().get("isNonGuaranteed") != null) {
            for (ChoProductReqQueryVo req:reqList){
                //根据req中的reqId查找该产品的审批通过的赎回记录
                listRedeemedApproved(req);
                // 注入远程数据
                injectValues(listTypeMap, listPayWayMap, req);
            }

            calNonGuaranteed(reqList,moneyUint);
        }else {
            for (ChoProductReqQueryVo req:reqList){
                //根据req中的reqId查找该产品的审批通过的赎回记录
                listRedeemedApproved(req);
                setBeanMoneyType(req,moneyUint);
                // 注入远程数据
                injectValues(listTypeMap, listPayWayMap, req);
            }
        }
        output.setData(reqList);
        return output;
    }
    /**
     * 注入远程数据。
     *
     * @param listTypeMap   理财类型数据。
     * @param listPayWayMap 付息方式数据。
     * @param product       理财产品信息。
     */
    private void injectValues(Map<String, DictDTO> listTypeMap, Map<String, DictDTO> listPayWayMap, ChoProductReqQueryVo product) {
        if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
            Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
        }
        if (ObjectUtil.isAllNotEmpty(product.getPayWay(), listPayWayMap)) {
            Optional.ofNullable(listPayWayMap.get(product.getPayWay())).ifPresent(dict -> product.setPayWayName(dict.getName()));
        }
        if(!EmptyUtils.isEmpty(product.getCorpId())){
            CorpDTO corpDTO = corpService.get(product.getCorpId());
            if(!EmptyUtils.isEmpty(corpDTO)){
                product.setCorpName(corpDTO.getName());
            }
        }
//        if(!EmptyUtils.isEmpty(product.getCurId())){
//            BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(product.getCurId());
//            if(!EmptyUtils.isEmpty(btCurrencyDTO)){
//                product.setCurName(btCurrencyDTO.getCurName());
//            }
//        }
        if(!EmptyUtils.isEmpty(product.getBankTypeId())){
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(product.getBankTypeId());
            if(!EmptyUtils.isEmpty(bankTypeDTO)){
                product.setBankTypeName(bankTypeDTO.getTypeName());
            }
        }
    }
    /**
     * 注入远程数据。
     *
     * @param listTypeMap   理财类型数据。
     * @param listPayWayMap 付息方式数据。
     * @param product       理财产品信息。
     */
    private void injectValues(Map<String, DictDTO> listTypeMap, Map<String, DictDTO> listPayWayMap, ChoProductReqUncollectedVo product) {
        if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
            Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
        }
        if (ObjectUtil.isAllNotEmpty(product.getPayWay(), listPayWayMap)) {
            Optional.ofNullable(listPayWayMap.get(product.getPayWay())).ifPresent(dict -> product.setPayWayName(dict.getName()));
        }
    }
    /**
     * 计算非保本类型
     * @param reqList 数据集合
     */
    private void calNonGuaranteed(List<ChoProductReqQueryVo> reqList,String moneyUint) {
        for (ChoProductReqQueryVo req : reqList) {
            if (req.getHoldMoney() == null && CommonConstant.YES.equals(req.getIsMaintainVal())) {
                continue;
            }
            // 收益条款
            Integer incomeClause = req.getIncomeClause();
            // 投资天数
            Integer dayLimit = calDayLimit(req.getNetValDate(), req.getInterestStartDate());
            // （投资天数/收益条款）
            BigDecimal dayRate = new BigDecimal(dayLimit).divide(new BigDecimal(incomeClause), 6, BigDecimal.ROUND_HALF_UP);
            // 本金
            BigDecimal buyMoney = req.getBuyMoney();
            // 持有本金
            BigDecimal holdMoney = req.getHoldMoney();
            if (CommonConstant.NO.equals(req.getIsMaintainVal())) { //不维护日净值信息
                // 已赎回本金 = 总赎回金额
                req.setRedeemedPrincipal(req.getRedeemedMoney());
                // 已赎回金额 = 已赎回本金 + 赎回收益金额
                req.setRedeemedMoney(req.getRedeemedMoney().add(req.getIncomeMoney()));
                // 待赎回金额为空
                req.setWaitRedeemMoney(null);
                // 持有本金 = 购买金额 - 已赎回本金
                req.setHoldMoney(buyMoney.subtract(req.getRedeemedPrincipal()));
            } else { // 需要维护日净值信息
                // 实际收益(如果全部赎回，则实际收益为0，赋值为将赎回收益金额)
                BigDecimal actualIncome = req.getActualIncome().compareTo(BigDecimal.ZERO) == 0 && holdMoney.compareTo(BigDecimal.ZERO) == 0
                        ? req.getIncomeMoney() : req.getActualIncome();
                req.setActualIncome(actualIncome);
                // 已赎回金额 = 已赎回本金 + 赎回收益金额
                req.setRedeemedMoney(req.getRedeemedMoney().add(req.getIncomeMoney()));
                // 待赎回金额 = 本金+实际收益-已赎回金额
                req.setWaitRedeemMoney(buyMoney.add(actualIncome).subtract(req.getRedeemedMoney()));
                // 持有收益率 = 持有实际收益/持有成本*100%
                if (holdMoney.compareTo(BigDecimal.ZERO) == 0) {
                    req.setHoldIncomeRate(BigDecimal.ZERO);
                } else {
                    req.setHoldIncomeRate(actualIncome.divide(holdMoney, 6, BigDecimal.ROUND_HALF_UP));
                }
                // 年化收益率 = （投资内收益/本金）/（投资天数/360）*100%
                req.setAnnualizedIncomeRate(dayLimit == 0 ? BigDecimal.ZERO :
                        (actualIncome.divide(buyMoney, 6, BigDecimal.ROUND_HALF_UP))
                                .divide(dayRate, 6, BigDecimal.ROUND_HALF_UP));
                // 年化收益 = 本金*年化收益率
                req.setAnnualizedIncome(buyMoney.multiply(req.getAnnualizedIncomeRate()));
                // 已赎回本金
                req.setRedeemedPrincipal(req.getBuyMoney().subtract(req.getHoldMoney()));
            }
            setBeanMoneyType(req,moneyUint);
        }
    }
    public DataTablesOutput getDataTablesUnionForSelect(DataTablesInput input) {
        String moneyUint = (String) input.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)){
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        DataTablesOutput<ChoProductReqQueryVo> output = choProductReqQueryDao.getDataTablesUnionForSelect(input);
        List<ChoProductReqQueryVo> reqList = output.getData();
        for (ChoProductReqQueryVo req:reqList){
            //根据req中的reqId查找该产品的审批通过的赎回记录
            listRedeemedApproved(req);
            setBeanMoneyType(req,moneyUint);
        }
        output.setData(reqList);
        return output;
    }
    /**
     * 根据登记记录获取对应的审批状态为审批通过的赎回记录
     * @param req
     */
    private void listRedeemedApproved(ChoProductReqQueryVo req) {
        List<ChoProductRedeemDetail> productRedeems = choProductRedeemService.listProductRedeemApproved(req.getReqId());
        calIncomeMoney(req,productRedeems);
    }

    /**
     * 根据登记信息和赎回列表计算相关平均值
     * @param req
     * @param productRedeems
     */
    private void calIncomeMoney(ChoProductReqQueryVo req, List<ChoProductRedeemDetail> productRedeems) {
        BigDecimal redeemedMoney = BigDecimal.ZERO;//已赎回金额(多笔已审批通过的汇总值)
        BigDecimal incomeMoney = BigDecimal.ZERO;//收益金额(多笔已审批通过的汇总值)
        BigDecimal averageIncomeRate = BigDecimal.ZERO;//平均收益率
        // 已赎回份额
        BigDecimal redeemedPortion = BigDecimal.ZERO;
        Date redeemDate = null;
        if (!EmptyUtils.isEmpty(productRedeems)) {
            for (ChoProductRedeemDetail redeem : productRedeems) {
                redeemedMoney = redeemedMoney.add(redeem.getRedeemMoney());
                incomeMoney = incomeMoney.add(redeem.getIncomeMoney());
                if (redeem.getIncomeRate() != null) {
                    averageIncomeRate = averageIncomeRate.add(redeem.getIncomeRate());
                }
                if (redeem.getRedeemPortion() != null) {
                    redeemedPortion = redeemedPortion.add(redeem.getRedeemPortion());
                }
                //设置最近的赎回日期
                if(EmptyUtils.isEmpty(redeemDate)){
                    redeemDate = redeem.getRedeemDate();
                }else{
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(redeemDate);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(redeem.getRedeemDate());
                    if(calendar1.compareTo(calendar) > 0){
                        redeemDate = calendar1.getTime();
                    }
                }
            }
            averageIncomeRate = averageIncomeRate.divide(BigDecimal.valueOf(productRedeems.size()),10, BigDecimal.ROUND_HALF_UP);
        }else{
            averageIncomeRate = req.getReferIncomeRate();
        }
        req.setRedeemedMoney(redeemedMoney);
        req.setRedeemDate(redeemDate);
        req.setIncomeMoney(incomeMoney);
        req.setWaitRedeemMoney(req.getBuyMoney().subtract(redeemedMoney));
        req.setAverageIncomeRate(averageIncomeRate);
        req.setRedeemedPortion(redeemedPortion);
        if (req.getPortion() != null) {
            req.setWaitRedeemPortion(req.getPortion().subtract(redeemedPortion));
        }
        //设置已经审批通过的赎回记录的申购费,赎回费和其他手续费
        BigDecimal[] chargeMoney = calRedeemChargeMoney(productRedeems);
        req.setSubFeeMoney(chargeMoney[0]);
        req.setRedeemChargeMoney(chargeMoney[1]);
        req.setChargeMoney(chargeMoney[2]);
    }
    /**
     * 据登记信息和赎回列表计算申购费,赎回费和其他手续费
     * @param productRedeems
     */
    private BigDecimal[] calRedeemChargeMoney(List<ChoProductRedeemDetail> productRedeems){
        BigDecimal subFeeMoney = BigDecimal.ZERO;
        BigDecimal redeemChargeMoney = BigDecimal.ZERO;
        BigDecimal chargeMoney = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(productRedeems)) {
            for (ChoProductRedeemDetail redeem : productRedeems) {
                if (!EmptyUtils.isEmpty(redeem.getSubFeeMoney())) {
                    subFeeMoney = subFeeMoney.add(redeem.getSubFeeMoney());
                }else{
                    subFeeMoney = subFeeMoney.add(BigDecimal.ZERO);
                }
                if (!EmptyUtils.isEmpty(redeem.getRedeemChargeMoney())) {
                    redeemChargeMoney = redeemChargeMoney.add(redeem.getRedeemChargeMoney());
                }else{
                    redeemChargeMoney = redeemChargeMoney.add(BigDecimal.ZERO);
                }
                if (!EmptyUtils.isEmpty(redeem.getChargeMoney())) {
                    chargeMoney = chargeMoney.add(redeem.getChargeMoney());
                }else{
                    chargeMoney = chargeMoney.add(BigDecimal.ZERO);
                }
            }
        }
        BigDecimal[] redeemSubFee = new BigDecimal[3];
        redeemSubFee[0] = subFeeMoney;
        redeemSubFee[1] = redeemChargeMoney;
        redeemSubFee[2] = chargeMoney;
        return redeemSubFee;
    }
    /**
     * 应收未收利息明细表列表数据
     * @param input
     * @return
     */
    public DataTablesOutput uncollectedDataTable(DataTablesInput input, ICurrentUser user) {
        String calEndDateStr = (String) input.getFormdata().get("calEndDate");
        Date calEndDate = DateUtils.str2Date(calEndDateStr,DateUtils.date_sdf());
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTREQ_CHOPRODUCTREQ_DTL_QUERY, user.getId()), "pr.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        boolean isQuery = input.getFormdata().get("initType") != null;
        DataTablesOutput output = isQuery ? choProductReqQueryDao.uncollectedDataTable(input,sql.toString())
                : choProductReqQueryDao.uncollectedNonDataTable(input,sql.toString());
        List<ChoProductReqUncollectedVo> uncollectedVos = output.getData();
        if (isQuery) {
            for (ChoProductReqUncollectedVo vo:uncollectedVos){
                vo.setCalEndDate(calEndDate);
                vo.setCalDay(calDayLimit(vo.getCalEndDate(),vo.getInterestStartDate(),vo.getDeadlineDate(),vo.getInterestStartDate()));
                calReceivableMoney(vo);
            }
        } else {
            calNonGuaranteedUncollected(uncollectedVos);
        }
        // 注入远程信息
        Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
        Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
        for (ChoProductReqUncollectedVo vo : uncollectedVos) {
            injectValues(listTypeMap, listPayWayMap, vo);
        }
        output.setData(uncollectedVos);
        return output;
    }
    /**
     * 计算应收未收计息的非保本数据
     * @param uncollectedVos 集合
     */
    private void calNonGuaranteedUncollected(List<ChoProductReqUncollectedVo> uncollectedVos) {
        for (ChoProductReqUncollectedVo vo : uncollectedVos) {
            // 已赎回金额总和
            BigDecimal redeemMoneySum = BigDecimal.ZERO;
            // 已赎回份额总和
            BigDecimal redeemPortionSum = BigDecimal.ZERO;
            // 已赎回实际收益
            BigDecimal incomeMoneySum = BigDecimal.ZERO;
            List<ChoProductRedeemDetail> productRedeems = choProductRedeemService.listProductRedeemApproved(vo.getReqId());
            for (ChoProductRedeemDetail redeem : productRedeems) {
                redeemMoneySum = redeemMoneySum.add(redeem.getRedeemMoney());
                redeemPortionSum = redeemPortionSum.add(redeem.getRedeemPortion());
                incomeMoneySum = incomeMoneySum.add(redeem.getIncomeMoney());
            }
            vo.setRedeemedPrincipal(redeemMoneySum);
            vo.setRedeemedMoney(redeemMoneySum.add(incomeMoneySum));
            vo.setRedeemedPortion(redeemPortionSum);
            if (CommonConstant.NO.equals(vo.getIsMaintainVal())) { //不维护日净值信息
                // 待赎回金额为空
                vo.setWaitRedeemMoney(null);
                // 持有本金 = 购买金额 - 已赎回本金
                vo.setHoldMoney(vo.getBuyMoney().subtract(redeemMoneySum));
                // 待赎回份额
                vo.setWaitRedeemPortion(vo.getPortion().subtract(redeemPortionSum));
                // 实收收益
                vo.setActualMoney(incomeMoneySum);
            } else {
                // 实际收益(如果全部赎回，则实际收益为0，赋值为将赎回收益金额)
                BigDecimal actualIncome = vo.getActualIncome().compareTo(BigDecimal.ZERO) == 0 && vo.getHoldMoney().compareTo(BigDecimal.ZERO) == 0
                        ? incomeMoneySum : vo.getActualIncome();
                vo.setActualIncome(actualIncome);
                // 待赎回金额 = 本金+实际收益-已赎回金额
                vo.setWaitRedeemMoney(vo.getBuyMoney().add(vo.getActualIncome()).subtract(vo.getRedeemedMoney()));
                // 待赎回份额
                vo.setWaitRedeemPortion(vo.getPortion().subtract(redeemPortionSum));
                // 应收收益
                vo.setReceivableMoney(vo.getActualIncome());
                // 实收收益
                vo.setActualMoney(incomeMoneySum);
            }
        }
    }
    /**
     * 计算应收利息
     * @param vo
     */
    private void calReceivableMoney(ChoProductReqUncollectedVo vo) {
        //应收利息
        BigDecimal receivableMoney = BigDecimal.ZERO;
        //实收利息
        BigDecimal actualMoney = BigDecimal.ZERO;
        //总赎回金额
        BigDecimal totalRedeemMoney = BigDecimal.ZERO;
        //查询截止日期前是否存在赎回记录
        List<ChoProductRedeemDetail> productRedeems = choProductRedeemService.listByReqIdRedeemDate(vo.getReqId(), vo.getCalEndDate());
        if (!EmptyUtils.isEmpty(productRedeems)){
            //存在赎回记录时
            for (ChoProductRedeemDetail redeem : productRedeems) {
                totalRedeemMoney = totalRedeemMoney.add(redeem.getRedeemMoney());
                receivableMoney = receivableMoney.add(calDayLimitMoney(redeem.getRedeemMoney(), redeem.getIncomeRate(),
                        calDayLimit(redeem.getRedeemDate(), vo.getInterestStartDate(), vo.getDeadlineDate(), vo.getInterestStartDate())
                        , vo.getIncomeClause()));
                actualMoney = actualMoney.add(calDayLimitMoney(redeem.getRedeemMoney(), redeem.getIncomeRate(),
                        calDayLimit(redeem.getRedeemDate(), vo.getInterestStartDate(), vo.getDeadlineDate(), vo.getInterestStartDate())
                        , vo.getIncomeClause()));
            }
            // leftMoney = 购买金额-赎回金额
            BigDecimal leftMoney = vo.getBuyMoney().subtract(totalRedeemMoney);
            //应收收益未赎回的部分,取最新赎回记录的实际收益率
            receivableMoney = receivableMoney.add(calDayLimitMoney(leftMoney,productRedeems.get(0).getIncomeRate(),
                    calDayLimit(vo.getCalEndDate(),vo.getInterestStartDate(),vo.getDeadlineDate(),vo.getInterestStartDate())
                    ,vo.getIncomeClause()));
        }else{
            //不存在赎回记录时
            receivableMoney = calDayLimitMoney(vo.getBuyMoney(),vo.getReferIncomeRate(),
                    calDayLimit(vo.getCalEndDate(),vo.getInterestStartDate(),vo.getDeadlineDate(),vo.getInterestStartDate())
                    ,vo.getIncomeClause());
        }
        vo.setReceivableMoney(receivableMoney);
        vo.setActualMoney(actualMoney);
        vo.setUncollectedMoney(receivableMoney.subtract(actualMoney));
        BigDecimal[] chargeMoney = calRedeemChargeMoney(productRedeems);
        vo.setSubFeeMoney(chargeMoney[0]);
        vo.setRedeemChargeMoney(chargeMoney[1]);
        vo.setChargeMoney(chargeMoney[2]);
    }
    /**
     *
     * @param
     * @return
     */
    private Integer calDayLimit(Date endDate,Date startDate) {
        return DateUtils.dayDiffs(endDate,startDate);
    }
    /**
     * 限制截止日期大于登记产品的到期日期,小于产品的起息日期时,进行最大值最小值限制
     * @param endDate
     * @param startDate
     * @param maxDate 产品的到期日期
     * @param minDate 产品的起息日期
     * @return
     */
    private Integer calDayLimit(Date endDate,Date startDate,Date maxDate,Date minDate) {
        Integer dayLimit = 0;
        //修改到期日期可能为空的问题,到期日为空时,全部按查询截止日期计算
        if (null == maxDate){
            maxDate = endDate;
        }
        if (endDate.after(maxDate)){
            if (startDate.after(minDate)){
                dayLimit = calDayLimit(maxDate,startDate);
            }else{
                dayLimit = calDayLimit(maxDate,minDate);
            }
        }else{
            if (startDate.after(minDate)){
                dayLimit = calDayLimit(endDate,startDate);
            }else{
                dayLimit = calDayLimit(endDate,minDate);
            }
        }
        if (endDate.before(minDate)){
            dayLimit = calDayLimit(minDate,startDate);
        }
        return dayLimit;
    }
    /**
     * 根据金额,利率,时间间隔,收益条款计算利息
     * @param money
     * @param rate
     * @param dayLimit
     * @param incomeClause
     * @return
     */
    private BigDecimal calDayLimitMoney(BigDecimal money,BigDecimal rate,Integer dayLimit,Integer incomeClause){
        return money.multiply(rate).multiply(BigDecimal.valueOf(dayLimit)).divide(BigDecimal.valueOf(incomeClause),10,BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 金额单位转换
     * @param ratVO
     * @param moneyType
     */
    private void setBeanMoneyType(ChoProductReqQueryVo ratVO, String moneyType) {
        if (ChoProductReqConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
            return;
        }
        Class userCla = (Class) ratVO.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); //设置些属性是可以访问的
                Object val = f.get(ratVO);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                String name = f.getName();//得到属性的名称
                if (type.endsWith("BigDecimal") && name.endsWith("Money")) {
                    if (null == val) {
//                        f.set(ratVO, BigDecimal.ZERO);
                    } else {
                        BigDecimal value = (BigDecimal) val;
                        f.set(ratVO, value.divide(BigDecimal.valueOf(Integer.parseInt(moneyType)), 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    @XxlJob("ChoProductReqQueryService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        logger.info("定时任务接收参数：【{}】", arguments);
        String endDay = getEndDayConfig();
        Date startDate = DateUtils.getDate();
        Date finishDate = DateUtils.addDay(startDate,Integer.parseInt(endDay));
        XxlJobHelper.log("startDate：" + DateUtils.format(startDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        XxlJobHelper.log("finishDate：" + DateUtils.format(finishDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        List<ChoProductReqVo> list = null;
        list = this.choProductReqQueryDao.listChoProductBuyMoneyByDate(startDate,finishDate);
        if (!EmptyUtils.isEmptyList(list)) {
            for (ChoProductReqVo req : list) {
                XxlJobHelper.log("理财产品【{}】，到期提醒",req.getId());
                //消息提醒
                notificationService.send(req.getId(), req.getCorpId(), arguments, req);
                //更新消息提醒标志
                ChoProductReq entity = choProductReqQueryDao.get(req.getId());
                BeanUtils.copyNotNullProperties(req, entity);
                entity.setNoticeSign("1");
                this.updateEntity(entity);
            }
        }
    }
    /**
     * 获取配置的理财到期提醒参数
     * @return
     */
    public String getEndDayConfig(){
        String endDay = configServiceImpl.getConfig(ChoProductReqConstant.CHO_PRODUCT_CONFIG_TYPE,
                ChoProductReqConstant.CHO_PRODUCT_CONFIG_ENDDAY).getValue();
        return StringUtils.isBlank(endDay)? "10" : endDay;
    }
    /**
     * 根据参数获取手机报表理财数据
     * @param reqVo
     * @return
     */
    public List<ChoProductReqUncollectedVo> listReportInfo(ChoProductReqUncollectedVo reqVo, ICurrentUser user){
        String startDate = DateUtils.formatDate(DateUtils.getDate());
        String endDate = "";
        //sign:1:一个月内,2:1-3个月内,3：3-6个月内,4:六个月内
        if(StringUtils.isBlank(reqVo.getSign())){
            reqVo.setSign("1");
        }
        if(StringUtils.isNotBlank(reqVo.getSign())) {
            if ("1".equals(reqVo.getSign())) {
                endDate = DateUtils.formatDate(DateUtils.addMonth(DateUtils.getDate(), 1));
            } else if ("2".equals(reqVo.getSign())) {
                endDate = DateUtils.formatDate(DateUtils.addMonth(DateUtils.getDate(), 3));
            } else if ("3".equals(reqVo.getSign())) {
                endDate = DateUtils.formatDate(DateUtils.addMonth(DateUtils.getDate(), 6));
            } else if ("4".equals(reqVo.getSign())) {
                endDate = "";
            }
        }
        if(EmptyUtils.isEmpty(reqVo.getCalEndDate()) || StringUtils.isNotBlank(reqVo.getCalEndDateStr())){
            reqVo.setCalEndDate(DateUtils.getDate());
        }
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTREQ_CHOPRODUCTREQ_DTL_QUERY, user.getId()), "pr.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        return choProductReqQueryDao.listReportInfo(reqVo,startDate,endDate,sql.toString());
    }
}
