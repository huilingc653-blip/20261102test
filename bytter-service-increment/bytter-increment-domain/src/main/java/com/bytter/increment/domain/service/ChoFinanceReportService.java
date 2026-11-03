package com.bytter.increment.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.api.dto.BiRemainTermDTO;
import com.bytter.increment.api.service.IBiRemainTermService;
import com.bytter.increment.domain.dao.ChoBankFinanceDao;
import com.bytter.increment.domain.dao.ChoFinanceReportDao;
import com.bytter.increment.domain.dao.ChoInnerFinanceDao;
import com.bytter.increment.domain.entity.ChoProductRedeemDetail;
import com.bytter.increment.domain.vo.ChoBankFinanceVO;
import com.bytter.increment.domain.vo.ChoFinanceReportVO;
import com.bytter.increment.domain.vo.ChoInnerFinanceVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.github.abel533.echarts.*;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Series;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
/**
 * @Title: service
 * @Description: 即将到期存款分析
 * @author wangzx
 * @date 2020.1.15
 * @version 1.0
 *
 */
@Service("choFinanceReportService")
@Transactional
public class ChoFinanceReportService extends BaseService<ChoFinanceReportVO, String> {
    @Autowired
    private ChoFinanceReportDao choFinanceReportDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private ChoBankFinanceDao choBankFinanceDao;
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @Autowired
    private ChoInnerFinanceDao choInnerFinanceDao;
    @Autowired
	private IBiRemainTermService biRemainTermService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @Override
    public BaseDao<ChoFinanceReportVO, String> getEntityDao() {
        return choFinanceReportDao;
    }
    /**
     * 查询理财信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user,String moneyUnitStr, String isToCurrStr) {
        String reducedCurId = (String) dataTablesInput.getFormdata().get("reducedCurId");//折合币种
        //是否折合本位币
        String isToCurr = null;
        if(StringUtils.isNotEmpty(isToCurrStr)){
            isToCurr = isToCurrStr;
        }else{
            isToCurr  = (String) dataTablesInput.getFormdata().get("isToCurr");
        }
        String joinSql = "";
        String resource = ResourceConstant.CHO_FINANCE_REPORT_QUERY;
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corpId", true);
            joinSql += " and " + corpAuthSql;
        }
        //是否折合币种为是时，默认查询所有
        if(!StringUtils.isEmpty(isToCurr) && "1".equals(isToCurr)){
            List<String> curArr = new ArrayList<>();
            List<BtCurrencyDTO> btCurrency = btCurrencyService.getBtCurrency();
            if(!EmptyUtils.isEmptyList(btCurrency)){
                for(BtCurrencyDTO dto : btCurrency){
                    curArr.add(dto.getId());
                }
                String curSql = SqlUtil.getInSql(curArr, "f.curId", true);
                joinSql += " and " + curSql;
            }
        }
        //金额单位
        Integer moneyUnit = null;
        if(StringUtils.isNotEmpty(moneyUnitStr)){
            moneyUnit = Integer.valueOf(moneyUnitStr);
        }else{
            moneyUnit = Integer.valueOf((String) dataTablesInput.getFormdata().get("moneyUnit"));
        }
        //查询剩余期限维护信息
        List<BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs("f.deadline_Date");
        //根据剩余期限查询数据信息
//        DataTablesOutput datas = choFinanceReportDao.getDataTablesUnion(dataTablesInput, joinSql, biRemainTermDTOs);
        List<ChoFinanceReportVO> list = choFinanceReportDao.getChoFinanceReportVOList(dataTablesInput, joinSql, biRemainTermDTOs);
        //单位期限集合
        Set<String> corpTermSet = new HashSet<String>();
        //单位信息
        Map<String,String> corpMap = new HashMap<>();

        //存放组装信息
        List<Map<String,Object>> termList = null;
        if (!EmptyUtils.isEmptyList(list)) {
            // inject(list);
            termList = new ArrayList<>();
            //拼装单位信息和币种信息和单位期限信息
            for (ChoFinanceReportVO reportVO : list) {
                corpTermSet.add(reportVO.getCorpId()+";"+reportVO.getTermId()+";"+reportVO.getCurId());
                if(!corpMap.containsKey(reportVO.getCorpId())){
                    corpMap.put(reportVO.getCorpId(),reportVO.getCorpName());
                }
            }
            Map<String,ChoFinanceReportVO> MoneyMap = new HashMap<>();
            for(String corpTerms : corpTermSet) {
                //余额
                BigDecimal balance = BigDecimal.ZERO;
                //利息
                BigDecimal interest = BigDecimal.ZERO;
                //合计
                BigDecimal total = BigDecimal.ZERO;
                String[] corpTerm = corpTerms.split(";");
                for(ChoFinanceReportVO reportVO : list){
                    if(corpTerm[0].equals(reportVO.getCorpId()) && corpTerm[1].equals(reportVO.getTermId()) && corpTerm[2].equals(reportVO.getCurId())) {
                        //银行定期存款
                        if ("finance".equals(reportVO.getType())) {
                            calFinanceMoney(reportVO, balance, interest, total);
                        }else if ("req".equals(reportVO.getType())) {
                            //银行理财存款
                            calReqMoney(reportVO, balance, interest, total);
                        }else if("bond".equals(reportVO.getType())) {
                            //保证金定期
                            calBondMoney(reportVO, balance, interest, total);
                        }else{
                            //内部定期存款
                            calInnerMoney(reportVO, balance, interest, total);
                        }
                        balance = EmptyUtils.isEmpty(reportVO.getBalanceTotal()) ? BigDecimal.ZERO : reportVO.getBalanceTotal();
                        interest = EmptyUtils.isEmpty(reportVO.getInterestTotal())? BigDecimal.ZERO : reportVO.getInterestTotal();
                        total = EmptyUtils.isEmpty(reportVO.getTotal())? BigDecimal.ZERO : reportVO.getTotal();
                        reportVO.setBalanceTotal(balance);
                        reportVO.setInterestTotal(interest);
                        reportVO.setTotal(total);
                        MoneyMap.put(corpTerms,reportVO);
                    }
                }
            }
            List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
            Map<String, CorpDTO> corpMap1 = corpService.getCorpMap();
            //去重后单位信息
            for (String corpId : corpMap.keySet()){
                Map<String,Object> map = new HashMap<>();
                //遍历期限集合
                for(BiRemainTermDTO termDto : biRemainTermDTOs){
                    String corpName = "";
                    if(null != corpMap1){
                        CorpDTO corpDTO = corpMap1.get(corpId);
                        if(null != corpDTO){
                            corpName = corpDTO.getName();
                        }
                    }
                    map.put("corpName",corpName);
                    //判断单位期限是否有数据，如果有数据，进行赋值
                    ChoFinanceReportVO reportVO = null;
                    //折算后合计的金额
                    BigDecimal balCurTemp = BigDecimal.ZERO;
                    BigDecimal interestCurTemp = BigDecimal.ZERO;
                    BigDecimal totalCurTemp = BigDecimal.ZERO;
                    //遍历币种集合
                    for(BtCurrencyDTO dto : curList) {
                        reportVO = MoneyMap.get(corpId+";"+termDto.getId()+";"+dto.getId());
                        if(!EmptyUtils.isEmpty(reportVO)){
                            //如果存在金额，币别换算
                            balCurTemp = balCurTemp.add(toCurrExcrateMoney(reportVO.getCurId(),reportVO.getCalEndDate(),isToCurr,reportVO.getBalanceTotal(),moneyUnit,reducedCurId));
                            interestCurTemp = interestCurTemp.add(toCurrExcrateMoney(reportVO.getCurId(),reportVO.getCalEndDate(),isToCurr,reportVO.getInterestTotal(),moneyUnit,reducedCurId));
                            totalCurTemp = totalCurTemp.add(toCurrExcrateMoney(reportVO.getCurId(),reportVO.getCalEndDate(),isToCurr,reportVO.getTotal(),moneyUnit,reducedCurId));
                        }
                    }
                    map.put(termDto.getId()+"_bal",balCurTemp);
                    map.put(termDto.getId()+"_inter",interestCurTemp);
                    map.put(termDto.getId()+"_total",totalCurTemp);
                }
                termList.add(map);
            }
        }
        DataTablesOutput output = new DataTablesOutput();
        //分页处理
        if(null == termList && EmptyUtils.isEmpty(termList)){
            output.setDraw(0);
            output.setRecordsTotal(0);
            output.setRecordsFiltered(0);
            output.setError(null);
            output.setData(null);
            return output;
        }
        // 设置返回前端的DataTablesdatas
        int totalNum = termList.size();
        Integer pageNumber = dataTablesInput.getPageNumber();
        int page = (totalNum + 10 - 1)/10;
        if(pageNumber == page){
            output.setData(termList.subList(dataTablesInput.getStart(), totalNum));
        }else {
            if(dataTablesInput.getLength() > totalNum){
                output.setData(termList.subList(dataTablesInput.getStart(), totalNum));
            }else {
                output.setData(termList.subList(dataTablesInput.getStart(), dataTablesInput.getStart()+10));
            }
        }
        output.setRecordsTotal(termList.size());
        output.setRecordsFiltered(termList.size());
        return output;
    }
    /**
     * 根据金额单位得到相应数值
     * @param moneryUnit
     * @return
     */
    public BigDecimal getMoneyUnit(Integer moneryUnit){
        BigDecimal unit = null;
        switch (moneryUnit) {
            case 1 : unit = BigDecimal.ONE;break;
            case 2 : unit = BigDecimal.valueOf(1000);break;
            case 3 : unit = BigDecimal.valueOf(10000);break;
            case 4 : unit = BigDecimal.valueOf(100000000);break;
            default:
                unit = BigDecimal.ONE;
        }
        return unit;
    }
    /**
     * 折合币种换算
     * @param curId 原始货币
     * @param enDate  结束日期
     * @param isToCurr 是否折合币种
     * @param money 金额
     * @param moneyUnitStr 金额单位
     * @param reducedCurId 折合币种
     * @return
     */
    public BigDecimal toCurrExcrateMoney(String curId, Date enDate, String isToCurr,BigDecimal money, Integer moneyUnitStr,String reducedCurId){
        //金额四舍五入
        money = money.setScale(2, BigDecimal.ROUND_UP);
        //折合币种换算
        BigDecimal excrateMoney = BigDecimal.ZERO;
        BigDecimal curExrate = new BigDecimal(1);
        if(StringUtils.isNotBlank(isToCurr) && "1".equals(isToCurr)){
            BigDecimal exchangeRateBy = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, reducedCurId, DateUtils.getDate(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            if (exchangeRateBy != null) {
                curExrate = exchangeRateBy;
            }
            excrateMoney = money.multiply(curExrate).divide(getMoneyUnit(moneyUnitStr), 2, RoundingMode.HALF_UP);
        }else{
            //不折合币种
            excrateMoney = money.divide(getMoneyUnit(moneyUnitStr), 2, RoundingMode.HALF_UP);
        }
        return excrateMoney;
    }
    /**
     * 折合本位币换算
     */
//    public Map<String,Object> toCurrMoney(BiRemainTermDTO termDto,ChoFinanceReportVO vo,String isToCurr,Integer moneyUnitStr){
//        Map<String,Object> map = new HashMap<>();
//        if(StringUtils.isNotBlank(isToCurr) && "1".equals(isToCurr)){
//            //折合本位币换算
//            BigDecimal curExrate = new BigDecimal(1);
//            BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();//获取本位币
//            if (!natualCurrency.getId().equals(vo.getCurId())) {
//                BtCurrencyDTO objectById = btCurrencyService.getObjectById(vo.getCurId());
//                curExrate = objectById.getCurExrate();
//            }
//            map.put(termDto.getId()+"_bal",vo.getBalanceTotal().multiply(curExrate).divide(getMoneyUnit(moneyUnitStr), 2, RoundingMode.HALF_UP));
//            map.put(termDto.getId()+"_inter",vo.getInterestTotal().multiply(curExrate).divide(getMoneyUnit(moneyUnitStr), 2, RoundingMode.HALF_UP));
//            map.put(termDto.getId()+"_total",vo.getTotal().multiply(curExrate).divide(getMoneyUnit(moneyUnitStr), 2, RoundingMode.HALF_UP));
//        }else{
//            //不折合本位币
//            map.put(termDto.getId()+"_bal",vo.getBalanceTotal().divide(getMoneyUnit(moneyUnitStr), 2, RoundingMode.HALF_UP));
//            map.put(termDto.getId()+"_inter",vo.getInterestTotal().divide(getMoneyUnit(moneyUnitStr), 2, RoundingMode.HALF_UP));
//            map.put(termDto.getId()+"_total",vo.getTotal().divide(getMoneyUnit(moneyUnitStr), 2, RoundingMode.HALF_UP));
//        }
//        return map;
//    }
    /**
     * 计算银行定期存款
     * @param reportVO
     * @return
     */
    public void calBondMoney(ChoFinanceReportVO reportVO, BigDecimal balance,BigDecimal interest,BigDecimal total){
        //期限为N月:利息 = 保证金金额*利率*期限/12
        //期限为N年:利息 = 保证金金额*利率*期限
        if(!EmptyUtils.isEmpty(reportVO.getDayLimit())){
            if("3M".equals(StringUtils.trim(reportVO.getDayLimit()))){
                interest = interest.add(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()).multiply(new BigDecimal(3)).divide(new BigDecimal(12), 6, BigDecimal.ROUND_HALF_UP));
            }else if("6M".equals(StringUtils.trim(reportVO.getDayLimit()))){
                interest = interest.add(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()).multiply(new BigDecimal(6)).divide(new BigDecimal(12), 6, BigDecimal.ROUND_HALF_UP));
            }else if("1Y".equals(StringUtils.trim(reportVO.getDayLimit()))){
                interest = interest.add(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()).multiply(new BigDecimal(1)));
            }else if("2Y".equals(StringUtils.trim(reportVO.getDayLimit()))){
                interest = interest.add(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()).multiply(new BigDecimal(2)));
            }else if("3Y".equals(StringUtils.trim(reportVO.getDayLimit()))){
                interest = interest.add(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()).multiply(new BigDecimal(3)));
            }else if("5Y".equals(StringUtils.trim(reportVO.getDayLimit()))){
                interest = interest.add(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()).multiply(new BigDecimal(5)));
            }
            //余额
            balance = balance.add(reportVO.getBuyMoney());
            //合计
            total = balance.add(interest);
            reportVO.setBalanceTotal(balance);
            reportVO.setInterestTotal(interest);
            reportVO.setTotal(total);
        }
    }
    /**
     * 计算银行定期存款
     * @param reportVO
     * @return
     */
    public void calFinanceMoney(ChoFinanceReportVO reportVO, BigDecimal balance,BigDecimal interest,BigDecimal total){
        //期限 = 到期日期 - 起息日期
        int limit = DateUtils.dayDiffs(reportVO.getDeadlineDate(), reportVO.getInterestStartDate());
        //参考收益 = 金额 * 利率 * 期限
        reportVO.setReferInterest(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()).multiply(new BigDecimal(limit)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
        //计算天数 = 截止日期 - 起息日期
        int calDays = DateUtils.dayDiffs(reportVO.getCalEndDate(), reportVO.getInterestStartDate());
        if (reportVO.getMaxWishPayday() == null) {
            //截止日内不存在支取：应收利息 = 金额*补录利率*（截止日期-起息日期）
            if (reportVO.getBlInterestRate() == null) {
                reportVO.setInterestReceivable(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()).multiply(new BigDecimal(calDays)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
            } else {
                reportVO.setInterestReceivable(reportVO.getBuyMoney().multiply(reportVO.getBlInterestRate()).multiply(new BigDecimal(calDays)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
            }
            reportVO.setBlInterest(BigDecimal.ZERO);
            //余额
            balance = balance.add(reportVO.getBuyMoney());
        } else {
            //截止日内存在支取：应收利息 = 转账金额*支取利率*（转账日期-起息日期）+（金额-转账金额）*补录利率*（截止日期-转账日期）
            //获取定转活或通知转活的数据
            List<ChoBankFinanceVO> drawList = choBankFinanceDao.listByPayBillId(reportVO.getId());
            BigDecimal sumDrawMoney = BigDecimal.ZERO;//累计取款金额
            BigDecimal sumBlInterest = BigDecimal.ZERO;//累计实收利息
            boolean flag = false;
            for (int i = 0; i < drawList.size(); i++) {
                ChoBankFinanceVO draw = drawList.get(i);
                //到期日期<转账日期（超期提取）
                if (DateUtils.dayDiffs(draw.getWishPayday(), draw.getDeadlineDate()) > 0) {
                    flag = true;
                    //正常期限利息
                    BigDecimal interestTemp1 = draw.getPayMoney().multiply(reportVO.getBlInterest()).multiply(new BigDecimal(limit)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP);
                    //超期期限 = 截止日期 - 到期日期
                    int limitTemp = DateUtils.dayDiffs(reportVO.getCalEndDate(), reportVO.getDeadlineDate());
                    //超期期限利息
                    BigDecimal interestTemp2 = (draw.getPayMoney().add(interestTemp1)).multiply(draw.getInterestRate()).multiply(new BigDecimal(limitTemp)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP);
                    sumBlInterest = sumBlInterest.add(interestTemp1.add(interestTemp2));
                } else {
                    //实收利息 = 转账金额*支取利率*（转账日期-起息日期）
                    int calDaysTemp = DateUtils.dayDiffs(draw.getWishPayday(), draw.getInterestStartDate());
                    //if (i == 0) {
                        sumBlInterest = sumBlInterest.add(draw.getPayMoney().multiply(draw.getInterestRate()).multiply(new BigDecimal(calDaysTemp)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
                    /*} else {
                        sumBlInterest = sumBlInterest.add((reportVO.getBuyMoney().subtract(sumDrawMoney)).multiply(draw.getInterestRate()).multiply(new BigDecimal(calDaysTemp)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
                    }*/
                }
                sumDrawMoney = sumDrawMoney.add(draw.getPayMoney());
            }
            if (flag) {
                //应收利息
                reportVO.setInterestReceivable(sumBlInterest);
                //余额
                balance = balance.add(reportVO.getBuyMoney().subtract(sumDrawMoney));
            } else {
                BigDecimal drawBal = reportVO.getBuyMoney().subtract(reportVO.getDrawMoney());//剩余本金
                //应收利息 = 剩余本金*补录利率*（截止日期-起息日期）+ 实收利息
                reportVO.setInterestReceivable(drawBal.multiply(reportVO.getBlInterestRate()).multiply(new BigDecimal(calDays)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP).add(sumBlInterest));
                //余额
                balance = balance.add(drawBal);
            }
            reportVO.setBlInterest(sumBlInterest);
        }
        reportVO.setCalDays(calDays);//计算天数
        reportVO.setInterestReceivableNot(reportVO.getInterestReceivable().subtract(reportVO.getBlInterest()));//应收未收利息 = 应收利息-实收利息
        //利息
        interest = interest.add(reportVO.getInterestReceivableNot());
        //合计
        total = balance.add(interest);
        reportVO.setBalanceTotal(balance);
        reportVO.setInterestTotal(interest);
        reportVO.setTotal(total);
    }
    /**
     * 计算银行理财存款
     * @param reportVO
     * @return
     */
    public void calReqMoney(ChoFinanceReportVO reportVO, BigDecimal balance,BigDecimal interest,BigDecimal total){
        //req 银行理财
        reportVO.setCalEndDate(reportVO.getCalEndDate());
        reportVO.setCalDays(calDayLimit(reportVO.getCalEndDate(), reportVO.getInterestStartDate(), reportVO.getDeadlineDate(), reportVO.getInterestStartDate()));
        //查询截止日期前是否存在赎回记录
        BigDecimal receivableMoney = BigDecimal.ZERO;//应收利息
        BigDecimal actualMoney = BigDecimal.ZERO;//实收利息
        BigDecimal totalRedeemMoney = BigDecimal.ZERO;
        List<ChoProductRedeemDetail> productRedeems = choProductRedeemService.listByReqIdRedeemDate(reportVO.getId(), reportVO.getCalEndDate());
        if (!EmptyUtils.isEmpty(productRedeems)) {
            //存在赎回记录时
            for (ChoProductRedeemDetail redeem : productRedeems) {
                totalRedeemMoney = totalRedeemMoney.add(redeem.getRedeemMoney());
                receivableMoney = receivableMoney.add(calDayLimitMoney(redeem.getRedeemMoney(), redeem.getIncomeRate(),
                        calDayLimit(redeem.getRedeemDate(), reportVO.getInterestStartDate(), reportVO.getDeadlineDate(), reportVO.getInterestStartDate())
                        , reportVO.getIncomeClause()));
                actualMoney = actualMoney.add(calDayLimitMoney(redeem.getRedeemMoney(), redeem.getIncomeRate(),
                        calDayLimit(redeem.getRedeemDate(), reportVO.getInterestStartDate(), reportVO.getDeadlineDate(), reportVO.getInterestStartDate())
                        , reportVO.getIncomeClause()));
            }
            BigDecimal leftMoney = reportVO.getBuyMoney().subtract(totalRedeemMoney);
            //应收收益未赎回的部分,取最新赎回记录的实际收益率
            receivableMoney = receivableMoney.add(calDayLimitMoney(leftMoney, productRedeems.get(0).getIncomeRate(),
                    calDayLimit(reportVO.getCalEndDate(), reportVO.getInterestStartDate(), reportVO.getDeadlineDate(), reportVO.getInterestStartDate())
                    , reportVO.getIncomeClause()));
            //余额
            balance = balance.add(leftMoney);
        } else {
            //不存在赎回记录时
            receivableMoney = calDayLimitMoney(reportVO.getBuyMoney(), reportVO.getReferIncomeRate(),
                    calDayLimit(reportVO.getCalEndDate(), reportVO.getInterestStartDate(), reportVO.getDeadlineDate(), reportVO.getInterestStartDate())
                    , reportVO.getIncomeClause());
            //余额
            balance = balance.add(reportVO.getBuyMoney());
        }
        //应收利息
        reportVO.setInterestReceivable(receivableMoney);
        reportVO.setInterestEarned(actualMoney);
        reportVO.setInterestReceivableNot(receivableMoney.subtract(actualMoney));
        //利息
        interest = interest.add(reportVO.getInterestReceivableNot());
        //合计
        total = balance.add(interest);
        reportVO.setBalanceTotal(balance);
        reportVO.setInterestTotal(interest);
        reportVO.setTotal(total);
    }
    /**
     * 计算内部定期存款
     * @param reportVO
     * @return
     */
    public void calInnerMoney(ChoFinanceReportVO reportVO, BigDecimal balance,BigDecimal interest,BigDecimal total){
        //inner 内部定期存款
        if (reportVO.getInterestRate() == null) {
            reportVO.setInterestRate(BigDecimal.ZERO);
        }
        //计算天数 = 截止日期 - 起息日期
        int calDays = DateUtils.dayDiffs(reportVO.getCalEndDate(), reportVO.getInterestStartDate());
        if (DateUtils.dayDiffs(reportVO.getCalEndDate(), reportVO.getDeadlineDate()) == 0) {
            //定期存款：截止日期等于到期日期
            //应收利息 = 金额 * 定期利率 * （截止日期-起息日期）/360
            reportVO.setInterestReceivable(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()));
            reportVO.setInterestReceivable(reportVO.getInterestReceivable().multiply(new BigDecimal(calDays)));
            reportVO.setInterestReceivable(reportVO.getInterestReceivable().divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP));
        } else if (DateUtils.dayDiffs(reportVO.getCalEndDate(), reportVO.getDeadlineDate()) > 0) {
            //定期存款：截止日期大于到期日期
            //应收利息 = 金额 * 利率 * （到期日期-起息日期）/360+金额 * 活期利率 * （截止日期-到期日期）
            reportVO.setInterestReceivable(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()));
            reportVO.setInterestReceivable(reportVO.getInterestReceivable().multiply(new BigDecimal(DateUtils.dayDiffs(reportVO.getDeadlineDate(), reportVO.getInterestStartDate()))));
            reportVO.setInterestReceivable(reportVO.getInterestReceivable().divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, BigDecimal.ROUND_HALF_UP));
            //到期处理方式：0-自动终止，1-本金续存，2-本息续存
            BigDecimal curInterest = reportVO.getBuyMoney();
            if (InnerFinanceConstant.CERTIFICATE_WAY_2.equals(reportVO.getCertificateWay().toString())) {
                curInterest = curInterest.add(reportVO.getInterestReceivable());
            }
            curInterest = curInterest.multiply(reportVO.getAccInterestRate());
            //curInterest = curInterest.multiply(new BigDecimal(DateUtils.dayDiffs(reportVO.getCalEndDate(), reportVO.getDeadlineDate())));
            curInterest = curInterest.multiply(new BigDecimal(DateUtils.dayDiffs(reportVO.getDeadlineDate(), reportVO.getInterestStartDate())));
            curInterest = curInterest.divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP);
            reportVO.setInterestReceivable(curInterest);
        } else {
            //定期存款：截止日期小于等于到期日期
            //应收利息 = 金额 * 活期利率 * （截止日期-起息日期）/360
            reportVO.setInterestReceivable(reportVO.getBuyMoney().multiply(reportVO.getInterestRate()));
            reportVO.setInterestReceivable(reportVO.getInterestReceivable().multiply(new BigDecimal(calDays)));
            reportVO.setInterestReceivable(reportVO.getInterestReceivable().divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP));
        }
        //截止日内存在支取：应收利息 = 转账金额*支取利率*（转账日期-起息日期）+（金额-转账金额）*补录利率*（截止日期-转账日期）
        //获取定转活或通知转活的数据cho_bank_finance
        List<ChoInnerFinanceVO> drawList = choInnerFinanceDao.listByRelatedBillId(reportVO.getId());
        BigDecimal sumDrawMoney = BigDecimal.ZERO;//累计取款金额
        BigDecimal sumInterest = BigDecimal.ZERO;//累计实收利息
        for (ChoInnerFinanceVO draw : drawList) {
            //计算截止日期内已完成的取款
            if (DateUtils.dayDiffs(reportVO.getCalEndDate(), draw.getPayDate()) >= 0) {
                sumDrawMoney = sumDrawMoney.add(draw.getPayMoney());
                sumInterest = sumInterest.add(draw.getExpectInterest());
                //sumInterest = sumDrawMoney;
            }
            /*sumInterest = sumInterest.multiply(reportVO.getAccInterestRate());
            sumInterest = sumInterest.multiply(new BigDecimal(DateUtils.dayDiffs(reportVO.getCalEndDate(), reportVO.getDeadlineDate())));
            sumInterest = sumInterest.divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP);
*/
        }
        reportVO.setDrawMoney(sumDrawMoney);
        reportVO.setInterestEarned(sumInterest);
        reportVO.setCalDays(calDays);//计算天数
        reportVO.setInterestReceivableNot(reportVO.getInterestReceivable().subtract(reportVO.getInterestEarned()));//应收未收利息 = 应收利息-实收利息
        //余额
        balance = balance.add(reportVO.getBuyMoney().subtract(sumDrawMoney));
        //利息
        interest = interest.add(reportVO.getInterestReceivableNot());
        //合计
        total = balance.add(interest);
        reportVO.setBalanceTotal(balance);
        reportVO.setInterestTotal(interest);
        reportVO.setTotal(total);
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
                dayLimit = DateUtils.dayDiffs(maxDate,startDate);
            }else{
                dayLimit = DateUtils.dayDiffs(maxDate,minDate);
            }
        }else{
            if (startDate.after(minDate)){
                dayLimit = DateUtils.dayDiffs(endDate,startDate);
            }else{
                dayLimit = DateUtils.dayDiffs(endDate,minDate);
            }
        }
        if (endDate.before(minDate)){
            dayLimit = DateUtils.dayDiffs(minDate,startDate);
        }
        return dayLimit;
    }

    /**
     * 创建图表信息 5
     * @param text
     * @param xAxisName
     * @param xData
     * @param yAxisName
     * @param yAxisUnit
     * @param dataList
     * @return
     */
    public static String createBar(String text,String xAxisName, List<Object> xData,String yAxisName,String yAxisUnit,List<EchartsData> dataList){//6
        String trigger = "axis";
        String x = "center";
        String y = "top";
        boolean isShow = true;
        String formatter="{value}" + yAxisUnit;
        List<Series> series = new ArrayList<Series>();
        //拿到图例
        if(!EmptyUtils.isEmptyList(dataList)){
            for (int i = 0; i < dataList.size(); i++) {
                Bar temp = new Bar();
                temp.setName(dataList.get(i).getName());
                temp.setStack(dataList.get(i).getStack());
                temp.setData(dataList.get(i).getData());
                temp.setType(SeriesType.bar);
                temp.setBarWidth(50);
//                temp.setBarMinHeight(0);
                series.add(temp);
            }
            if(dataList.get(0).getData().size()==1){
                y = "bottom";
                trigger = "item";
            }
        }else{
            series = new ArrayList<Series>();
            Bar seriesBean = new Bar();
            seriesBean.setBarWidth(10);
            seriesBean.data(new ArrayList());
            series.add(seriesBean);
        }
        Option option = new Option();
        //设置标题
        Title title=new Title();
        title.setRight("50%");
        title.setText(text);
        option.setTitle(title);
        //设置工具提示
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger(Trigger.valueOf(trigger));
        option.setTooltip(tooltip);
        //设置工具栏
        Toolbox toolbox = new Toolbox();
        toolbox.show(isShow).feature(Tool.mark, new DataZoom().show(false), new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        //设置图例
        /*Legend legendBean = new Legend();
        for (String name:nameList) {
            legendBean.data(name);
        }
        option.setLegend(legendBean);*/
        //设置x轴
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setName(xAxisName);
        xAxis.type(AxisType.category);
        if(EmptyUtils.isEmptyList(xData)){
            xData = new ArrayList<Object>();
        }
        xAxis.data(xData.toArray(new Object[xData.size()]));
        //X轴样式特殊处理
        AxisLabel ab = new AxisLabel();
        ab.setInterval(0);
        ab.setFormatter("");
        xAxis.axisLabel(ab);
        option.xAxis(xAxis);
        //设置y轴
        ValueAxis yAxis = new ValueAxis();
        yAxis.type(AxisType.value);
        AxisLabel axisLabel = new AxisLabel().formatter(formatter);
        yAxis.setAxisLabel(axisLabel);
        yAxis.setName(yAxisName);
        option.yAxis(yAxis);
        //数据
        option.setSeries(series);
        return JsonUtils.toJSONString(option);
    }
    
    /**
     * 即将到期存款分析数据返回 1
     * @param list
     * @param curName
     * @param moneyUnit
     * @return
     */
    public Object getData(List<Map<String,Object>> list,String curName,String moneyUnit) {
        Object jsonObject = dealData(list,curName,moneyUnit);
        return jsonObject;
    }

    /**
     * 图表数据处理 2
     * @param list
     * @param curName
     * @param moneyUnit
     * @return
     */
    private Object dealData(List<Map<String,Object>> list,String curName,String moneyUnit) {

        String yAxisName = "单位：";
       if(StringUtils.isNotEmpty(moneyUnit)){
            if("2".equals(moneyUnit)){
                yAxisName += "千元";
            }else if("3".equals(moneyUnit)){
                yAxisName += "万元";
            }else if("4".equals(moneyUnit)){
                yAxisName += "亿元";
            }else{
                yAxisName += "元";
            }
       }
        List<BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs(null);
        List<EchartsData> echartsDatas = formatData(list,biRemainTermDTOs,curName);
        return createBar("即将到期存款分析", "", getXData(biRemainTermDTOs), yAxisName, "", echartsDatas);

    }

    /**
     * X 轴显示值 3
     * @param biRemainTermDTOs
     * @return
     */
    public List<Object> getXData(List<BiRemainTermDTO> biRemainTermDTOs) {
        List<Object> mergeList = new ArrayList<>();
        for(BiRemainTermDTO dto : biRemainTermDTOs){
            mergeList.add(dto.getTermName());
        }
        return mergeList;
    }
    
    /**
     * 图表数据 4
     *
     * @param dataList
     * @return
     */
    private List<EchartsData> formatData(List<Map<String,Object>> dataList,List<BiRemainTermDTO> biRemainTermDTOs,String curName) {
        List<EchartsData> mergeList = new ArrayList<>();

        EchartsData data = new EchartsData();
        data.setName(curName);
        List<Object> list = new ArrayList<Object>();

        Map<String,BigDecimal> dataMap = new HashMap<>();
        //图表显示数值处理
        if(!EmptyUtils.isEmptyList(dataList)){
            for(BiRemainTermDTO term : biRemainTermDTOs){
                String termId = term.getId();
                BigDecimal balTemp = BigDecimal.ZERO;
                for(Map<String,Object> map : dataList){
                    for(String key : map.keySet()){
                        if(key.equals(termId+"_total")){
                            balTemp = balTemp.add((BigDecimal) map.get(key));
                            dataMap.put(termId,balTemp);
                        }
                    }
                }
            }
            for(BiRemainTermDTO term : biRemainTermDTOs){
                list.add(NumberUtil.formatDouble(dataMap.get(term.getId())));
                logger.info(term.getTermName()+"-----"+dataMap.get(term.getId()));
            }
        }
        data.setData(list);
        mergeList.add(data);

        return mergeList;
    }

}
