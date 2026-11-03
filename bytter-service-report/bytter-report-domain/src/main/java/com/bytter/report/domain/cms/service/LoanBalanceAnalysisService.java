package com.bytter.report.domain.cms.service;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.report.domain.cms.service.TbCreditPeriodService;
import com.bytter.report.domain.cms.dao.LoanBalanceAnalysisDao;
import com.bytter.report.domain.cms.entity.TbCreditPeriod;
import com.bytter.report.domain.cms.vo.LoanBalanceAnalysisVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
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
import java.text.ParseException;
import java.util.*;

@SuppressWarnings("rawtypes")
@Service("loanBalanceAnalysisService")
@Transactional
public class LoanBalanceAnalysisService extends BaseService {
    
    @Autowired
    private LoanBalanceAnalysisDao loanBalanceAnalysisDao;
    @Autowired
    private TbCreditPeriodService tbCreditPeriodService;
    @DubboReference
    private IBtCurrencyService currencyService;
    @DubboReference
    private IFcExcrateAdjustService iFcExcrateAdjustService;
    @DubboReference(check = false)
    private ICorpService corpService;

    public DataTablesOutput getDataTable(DataTablesInput input,String userId) throws ParseException {
        Map<String, Object> param =  input.getFormdata();
        String corpId = (String) param.get("corpId");
        if(StringUtils.isEmpty(corpId)){
            return new DataTablesOutput();
        }
        List<String> corpIds = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(corpId)) {
            corpIds = StringUtils.splitToList(",", corpId);
        }else {
            corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_REPORT_LOAN_ANALYSIS_QUERY,userId);
        }
        String moneyType = (String)param.get("moneyType");//折合本位币
        String isToCurr = (String)param.get("isToCurr");//折合本位币
        String cycle = (String)param.get("cycle");//对比周期
        setDate(input);
        
        DataTablesOutput dto = loanBalanceAnalysisDao.getDataTable(input,corpIds);
        List<LoanBalanceAnalysisVO> list =  dto.getData();//当前结果
        if(!CollectionUtils.isEmpty(list)){
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrency();
            for(LoanBalanceAnalysisVO vo : list){
                if(null != corpMap){
                    if(corpMap.containsKey(vo.getCorpId())){
                        CorpDTO corpDTO = corpMap.get(vo.getCorpId());
                        if(null != corpDTO){
                            vo.setCorpName(corpDTO.getName());
                        }
                    }
                }
                if(null != currencyMap){
                    if(currencyMap.containsKey(vo.getCurId())){
                        BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                        if(null != btCurrencyDTO){
                            vo.setCurName(btCurrencyDTO.getCurName());
                        }
                    }
                }
            }
        }
        
        List<LoanBalanceAnalysisVO> cycleList = cycleList(list,cycle,param,corpIds);//维度
        if(CmsConstant.IS_TRUE.equals(isToCurr)){//折合本位币
            String mDate = (String)param.get("monthDate")+" 23:59:59";//对比周期
            Date monthDate = DateUtils.parse(mDate,DateUtils.PATTERN_CLASSICAL);
            String convertCurId = null;
            if (null != param.get("convertCurId") && StringUtils.isNotEmpty(param.get("convertCurId").toString())) {
                convertCurId = (String) param.get("convertCurId");//对比周期
            }
            toCurr(cycleList,monthDate,convertCurId);
        }
        if(!"1".equals(moneyType)){//金额单位换算
            conversion( list,moneyType);
        }

        List<LoanBalanceAnalysisVO> newList = new ArrayList<>();
        for(LoanBalanceAnalysisVO item : cycleList){
            if(item.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0 && 
                    item.getBalanceY().compareTo(BigDecimal.ZERO) == 0 &&
                    item.getBalanceS().compareTo(BigDecimal.ZERO) == 0 &&
                    item.getBalanceL().compareTo(BigDecimal.ZERO) == 0 &&
                    item.getBalanceM().compareTo(BigDecimal.ZERO) == 0){
                dto.setRecordsTotal(dto.getRecordsTotal()-1);
                dto.setRecordsFiltered(dto.getRecordsFiltered()-1);
            }else {
                newList.add(item);
            }
        }
        dto.setData(newList);
        return dto;
    }

    /**
     * 折合人民币
     */
    public List<LoanBalanceAnalysisVO> toCurr(List<LoanBalanceAnalysisVO> list,Date monthDate,String convertCurId){
       // BtCurrencyDTO cur = currencyService.getNatualCurrency();
        if (null != list && list.size() > 0) {
            BigDecimal rate = BigDecimal.ONE;
            if (StringUtils.isNotEmpty(convertCurId)) {
                for (LoanBalanceAnalysisVO vo : list) {
                    rate = iFcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), convertCurId, monthDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);

                    vo.setCurrentBalance(vo.getCurrentBalance().multiply(rate));

                    vo.setBalanceM((vo.getBalanceM() == null ? BigDecimal.ZERO : vo.getBalanceM()).multiply(rate));
                    vo.setDifferenceM((vo.getDifferenceM() == null ? BigDecimal.ZERO : vo.getDifferenceM()).multiply(rate));

                    vo.setBalanceS((vo.getBalanceS() == null ? BigDecimal.ZERO : vo.getBalanceS()).multiply(rate));
                    vo.setDifferenceS((vo.getDifferenceS() == null ? BigDecimal.ZERO : vo.getDifferenceS()).multiply(rate));

                    vo.setBalanceL((vo.getBalanceL() == null ? BigDecimal.ZERO : vo.getBalanceL()).multiply(rate));
                    vo.setDifferenceL((vo.getDifferenceL() == null ? BigDecimal.ZERO : vo.getDifferenceL()).multiply(rate));

                    vo.setBalanceY((vo.getBalanceY() == null ? BigDecimal.ZERO : vo.getBalanceY()).multiply(rate));
                    vo.setDifferenceY((vo.getDifferenceY() == null ? BigDecimal.ZERO : vo.getDifferenceY()).multiply(rate));
                }
            }
        }

        return list;
    }
    
    /**金额单位换算*/
    public List<LoanBalanceAnalysisVO> conversion(List<LoanBalanceAnalysisVO> list,String moneyType){
        BigDecimal divisor = BigDecimal.ZERO;
        if("2".equals(moneyType)){//千元
            divisor = new BigDecimal(1000);
        }
        if("3".equals(moneyType)){//万元
            divisor = new BigDecimal(10000);
        }
        if("4".equals(moneyType)){//亿元
            divisor = new BigDecimal(100000000);
        }
        for (LoanBalanceAnalysisVO vo : list){
            vo.setCurrentBalance(vo.getCurrentBalance().divide(divisor,2, BigDecimal.ROUND_HALF_UP));

            vo.setBalanceM((vo.getBalanceM()==null?BigDecimal.ZERO:vo.getBalanceM()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setDifferenceM((vo.getDifferenceM()==null?BigDecimal.ZERO:vo.getDifferenceM()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            
            vo.setBalanceS((vo.getBalanceS()==null?BigDecimal.ZERO:vo.getBalanceS()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setDifferenceS((vo.getDifferenceS()==null?BigDecimal.ZERO:vo.getDifferenceS()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            
            vo.setBalanceL((vo.getBalanceL()==null?BigDecimal.ZERO:vo.getBalanceL()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setDifferenceL((vo.getDifferenceL()==null?BigDecimal.ZERO:vo.getDifferenceL()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            
            vo.setBalanceY((vo.getBalanceY()==null?BigDecimal.ZERO:vo.getBalanceY()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setDifferenceY((vo.getDifferenceY()==null?BigDecimal.ZERO:vo.getDifferenceY()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
        }
        return list;
    }
    
    /**对比周期*/
    public List<LoanBalanceAnalysisVO> cycleList(List<LoanBalanceAnalysisVO> list,String cycle,Map<String, Object> param,List<String> corpIds) throws ParseException {
        String typesOfStatistics = (String)param.get("typesOfStatistics");//统计维度
        String date = (String)param.get("monthDate");//当前日期
        if(date == null){
            date = DateUtils.formatDate(new Date());
        }
        Date monthDate =DateUtils.parseDate(date,"yyyy-MM-dd");
        
        String curId = (String)param.get("curId");
        String isContain = (String)param.get("isContain");//包含在途
        List<LoanBalanceAnalysisVO> cycleListM = null;
        List<LoanBalanceAnalysisVO> cycleListS = null;
        List<LoanBalanceAnalysisVO> cycleListL = null;
        List<LoanBalanceAnalysisVO> cycleListY = null;
        
        Date lastMonth = getLastDayOfMonth(monthDate);//上月末
       // Date eos = getLastDayOfLastQuarter(monthDate);//上季末
        Date sDate = DateUtils.addMonth(monthDate, -3);
        Date eos = DateUtils.getLastQuarterDate(sDate);
        Date lastYear = DateUtils.addYear(monthDate,-1);//去年同期
        Date beginYear = DateUtils.getFirstYearDate(monthDate);//年初
        BigDecimal percent = new BigDecimal(100);
        
        if(EmptyUtils.isEmpty(cycle) || cycle.contains(CmsConstant.CYCLE_M)){
            //上月末
            cycleListM = loanBalanceAnalysisDao.getList(curId, corpIds, lastMonth,isContain,typesOfStatistics);
            for (LoanBalanceAnalysisVO vo : list){
                boolean isExis = false;
                for (LoanBalanceAnalysisVO m : cycleListM) {
                    if(vo.getCorpId().equals(m.getCorpId()) && vo.getLoanSign().equals(m.getLoanSign()) && vo.getCurId().equals(m.getCurId())){
                        vo.setBalanceM(m.getCurrentBalance());
                        vo.setDifferenceM(vo.getCurrentBalance().subtract(m.getCurrentBalance()));
                        if(vo.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0){
                            vo.setScaleM(BigDecimal.ZERO);
                        }else {
                            vo.setScaleM(vo.getDifferenceM().divide(vo.getCurrentBalance(),4, BigDecimal.ROUND_HALF_UP).multiply(percent));
                        }
                        isExis = true;
                    }
                }

                if(!isExis){
                    vo.setBalanceM(BigDecimal.ZERO);
                    vo.setDifferenceM(vo.getCurrentBalance().subtract(BigDecimal.ZERO));
                    if(vo.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0){
                        vo.setScaleM(BigDecimal.ZERO);
                    }else {
                        vo.setScaleM(vo.getDifferenceM().divide(vo.getCurrentBalance(),4, BigDecimal.ROUND_HALF_UP).multiply(percent));
                    }
                }
            }
        }
        
        if(EmptyUtils.isEmpty(cycle) || cycle.contains(CmsConstant.CYCLE_S)){
            //上季末
            cycleListS = loanBalanceAnalysisDao.getList(curId, corpIds, eos,isContain,typesOfStatistics);
            for (LoanBalanceAnalysisVO vo : list){
                boolean isExis = false;
                for (LoanBalanceAnalysisVO s : cycleListS) {
                    if(vo.getCorpId().equals(s.getCorpId()) && vo.getLoanSign().equals(s.getLoanSign()) && vo.getCurId().equals(s.getCurId())){
                        vo.setBalanceS(s.getCurrentBalance());
                        vo.setDifferenceS(vo.getCurrentBalance().subtract(s.getCurrentBalance()));
                        if(vo.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0){
                            vo.setScaleS(BigDecimal.ZERO);
                        }else {
                            vo.setScaleS(vo.getDifferenceS().divide(vo.getCurrentBalance(), 4, BigDecimal.ROUND_HALF_UP).multiply(percent));
                        }
                        isExis = true;
                    }
                }
               
                if(!isExis){
                    vo.setBalanceS(BigDecimal.ZERO);
                    vo.setDifferenceS(vo.getCurrentBalance().subtract(BigDecimal.ZERO));
                    if(vo.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0){
                        vo.setScaleS(BigDecimal.ZERO);
                    }else {
                        vo.setScaleS(vo.getDifferenceS().divide(vo.getCurrentBalance(),4, BigDecimal.ROUND_HALF_UP).multiply(percent));
                    }
                }
            }
        }
        if(EmptyUtils.isEmpty(cycle) ||  cycle.contains(CmsConstant.CYCLE_L)){
            //去年同期
            cycleListL = loanBalanceAnalysisDao.getList(curId, corpIds, lastYear,isContain,typesOfStatistics);
            for (LoanBalanceAnalysisVO vo : list){
                boolean isExis = false;
                for (LoanBalanceAnalysisVO l : cycleListL) {
                    if(vo.getCorpId().equals(l.getCorpId()) && vo.getLoanSign().equals(l.getLoanSign()) && vo.getCurId().equals(l.getCurId())){
                        vo.setBalanceL(l.getCurrentBalance());
                        vo.setDifferenceL(vo.getCurrentBalance().subtract(l.getCurrentBalance()));
                        if(vo.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0){
                            vo.setScaleL(BigDecimal.ZERO);
                        }else {
                            vo.setScaleL(vo.getDifferenceL().multiply(percent).divide(vo.getCurrentBalance(), 4, BigDecimal.ROUND_HALF_UP));
                        }
                        isExis = true;
                    }
                }

                if(!isExis){
                    vo.setBalanceL(BigDecimal.ZERO);
                    vo.setDifferenceL(vo.getCurrentBalance().subtract(BigDecimal.ZERO));
                    if(vo.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0){
                        vo.setScaleL(BigDecimal.ZERO);
                    }else {
                        vo.setScaleL(vo.getDifferenceL().divide(vo.getCurrentBalance(),4, BigDecimal.ROUND_HALF_UP).multiply(percent));
                    }
                }
            }
        }
        
        if(EmptyUtils.isEmpty(cycle) || cycle.contains(CmsConstant.CYCLE_Y)){
            //年初
            cycleListY = loanBalanceAnalysisDao.getList(curId, corpIds, beginYear,isContain,typesOfStatistics);
            for (LoanBalanceAnalysisVO vo : list){
                boolean isExis = false;
                for (LoanBalanceAnalysisVO y : cycleListY) {
                    if(vo.getCorpId().equals(y.getCorpId()) && vo.getLoanSign().equals(y.getLoanSign()) && vo.getCurId().equals(y.getCurId())){
                        vo.setBalanceY(y.getCurrentBalance());
                        vo.setDifferenceY(vo.getCurrentBalance().subtract(y.getCurrentBalance()));
                        if(vo.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0){
                            vo.setScaleY(BigDecimal.ZERO);
                        }else {
                            vo.setScaleY(vo.getDifferenceY().divide(vo.getCurrentBalance(), 4, BigDecimal.ROUND_HALF_UP).multiply(percent));
                        }
                        isExis = true;
                    }
                }

                if(!isExis){
                    vo.setBalanceY(BigDecimal.ZERO);
                    vo.setDifferenceY(vo.getCurrentBalance().subtract(BigDecimal.ZERO));
                    if(vo.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0){
                        vo.setScaleY(BigDecimal.ZERO);
                    }else {
                        vo.setScaleY(vo.getDifferenceY().divide(vo.getCurrentBalance(),4, BigDecimal.ROUND_HALF_UP).multiply(percent));
                    }
                }
            }
        }
        return list;
    }

    public Object getBankBal(List<LoanBalanceAnalysisVO> list) {//1
        Object jsonObject = dealData(list);
        return jsonObject;
    }

    private Object dealData(List<LoanBalanceAnalysisVO> resList) {//2
        List<EchartsData> echartsDatas = null;
        List<Object> mergeList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        String type = resList.get(0).getTypesOfStatistics();//统计维度
        Date monthDate = resList.get(0).getMonthDate();
        String curId = resList.get(0).getCurId();
        String cycle = resList.get(0).getCycle();
        
        if("1".equals(type)) {//融资种类 
            echartsDatas = formatDataByType(resList,nameList,cycle);
            mergeList = getData(mergeList,cycle);
        }
        
        if("2".equals(type)){ //融资期限
            Set<List<TbCreditPeriod>> timeList = new HashSet<>();
            Set<String> curIds = new HashSet<>();
            /*for(LoanBalanceAnalysisVO res : resList){
                curIds.add(res.getCurId());
            }
            List<String> curs = new ArrayList<>(curIds);
            //查询相关币别有效的期限
            for(String cur : curs) {
                List<TbCreditPeriod> times = null;
                BtCurrencyDTO curDto = currencyService.getObjectById(cur);
                if ("01".equals(curDto.getCurCode())) {
                    times = tbCreditPeriodService.getrmbTbCreditPeriod();//人民币
                } else {
                    times = tbCreditPeriodService.getforeignTbCreditPeriod();//外币
                }


            }*/
            timeList.add(tbCreditPeriodService.getAll());
            List<List<TbCreditPeriod>> timeArray = new ArrayList<>(timeList);
            echartsDatas = formatDataByTime(resList,timeArray,nameList,cycle);
            mergeList = getData(mergeList,cycle);
        }

        List<String> corpIds = null;
        if("3".equals(type)){//融资单位
            String corpId = resList.get(0).getCorpIds();
            if(EmptyUtils.isNotEmpty(corpId)) {
                corpIds = StringUtils.splitToList(",", corpId);
            }
            echartsDatas = formatDataByCorp(resList,nameList,corpIds,mergeList,cycle);
        }
        
        return createBar(null,"",mergeList,"","",echartsDatas,nameList);
    }
    
    /**融资种类*/
    private List<EchartsData> formatDataByType(List<LoanBalanceAnalysisVO> resList,List<String> nameList,String cycle) {//3
        List<EchartsData> mergeList = new ArrayList<>();
        
        BigDecimal loanBalance = BigDecimal.ZERO;
        BigDecimal provideBalance = BigDecimal.ZERO;
        BigDecimal precativeBalance = BigDecimal.ZERO;

        BigDecimal loanBalanceM = BigDecimal.ZERO;
        BigDecimal provideBalanceM = BigDecimal.ZERO;
        BigDecimal precativeBalanceM = BigDecimal.ZERO;

        BigDecimal loanBalanceS = BigDecimal.ZERO;
        BigDecimal provideBalanceS = BigDecimal.ZERO;
        BigDecimal precativeBalanceS = BigDecimal.ZERO;

        BigDecimal loanBalanceL = BigDecimal.ZERO;
        BigDecimal provideBalanceL = BigDecimal.ZERO;
        BigDecimal precativeBalanceL = BigDecimal.ZERO;
        
        BigDecimal loanBalanceY = BigDecimal.ZERO;
        BigDecimal provideBalanceY = BigDecimal.ZERO;
        BigDecimal precativeBalanceY = BigDecimal.ZERO;
        
        EchartsData data = new EchartsData();
        EchartsData data1 = new EchartsData();
        EchartsData data2 = new EchartsData();
        List<Object> list = new ArrayList<Object>();
        List<Object> list1 = new ArrayList<Object>();
        List<Object> list2 = new ArrayList<Object>();
        
        for (int i=0;i<resList.size();i++) {
            LoanBalanceAnalysisVO vo = resList.get(i);
            
            if("01".equals(vo.getLoanSign())) {
                loanBalance = loanBalance.add(vo.getCurrentBalance());
                loanBalanceM = loanBalanceM.add(vo.getBalanceM() == null ? BigDecimal.ZERO : vo.getBalanceM());
                loanBalanceS = loanBalanceS.add(vo.getBalanceS() == null ? BigDecimal.ZERO : vo.getBalanceS());
                loanBalanceL = loanBalanceL.add(vo.getBalanceL() == null ? BigDecimal.ZERO : vo.getBalanceL());
                loanBalanceY = loanBalanceY.add(vo.getBalanceY() == null ? BigDecimal.ZERO : vo.getBalanceY());

            }
            if("02".equals(vo.getLoanSign())){
                provideBalance = provideBalance.add(vo.getCurrentBalance());
                provideBalanceM = provideBalanceM.add(vo.getBalanceM()==null?BigDecimal.ZERO:vo.getBalanceM());
                provideBalanceS = provideBalanceS.add(vo.getBalanceS()==null?BigDecimal.ZERO:vo.getBalanceS());
                provideBalanceL = provideBalanceL.add(vo.getBalanceL()==null?BigDecimal.ZERO:vo.getBalanceL());
                provideBalanceY = provideBalanceY.add(vo.getBalanceY()==null?BigDecimal.ZERO:vo.getBalanceY());
                
            }
            if("03".equals(vo.getLoanSign())){
                precativeBalance = precativeBalance.add(vo.getCurrentBalance());
                precativeBalanceM = precativeBalanceM.add(vo.getBalanceM()==null?BigDecimal.ZERO:vo.getBalanceM());
                precativeBalanceS = precativeBalanceS.add(vo.getBalanceS()==null?BigDecimal.ZERO:vo.getBalanceS());
                precativeBalanceL = precativeBalanceL.add(vo.getBalanceL()==null?BigDecimal.ZERO:vo.getBalanceL());
                precativeBalanceY = precativeBalanceY.add(vo.getBalanceY()==null?BigDecimal.ZERO:vo.getBalanceY());
            }
        }
        BigDecimal loanBalance1 = loanBalance.setScale(2);
        BigDecimal loanBalanceM1 = loanBalanceM.setScale(2);
        BigDecimal loanBalanceS1 = loanBalanceS.setScale(2);
        BigDecimal loanBalanceL1 = loanBalanceL.setScale(2);
        BigDecimal loanBalanceY1 = loanBalanceY.setScale(2);
       
        list.add(loanBalance1.toString());
        list1.add(provideBalance.setScale(2).toString());
        list2.add(precativeBalance.setScale(2).toString());
        
        if(cycle.contains("M") || StringUtils.isEmpty(cycle)){
            list.add(loanBalanceM1.toString());
            list1.add(provideBalanceM.setScale(2).toString());
            list2.add(precativeBalanceM.setScale(2).toString());
        }

        if(cycle.contains("S") || StringUtils.isEmpty(cycle)){
            list.add(loanBalanceS1.toString());
            list1.add(provideBalanceS.setScale(2).toString());
            list2.add(precativeBalanceS.setScale(2).toString());
        }

        if(cycle.contains("L") || StringUtils.isEmpty(cycle)){
            list.add(loanBalanceL1.toString());
            list1.add(provideBalanceL.setScale(2).toString());
            list2.add(precativeBalanceL.setScale(2).toString());
        }

        if(cycle.contains("Y") || StringUtils.isEmpty(cycle)){
            list.add(loanBalanceY1.toString());
            list1.add(provideBalanceY.setScale(2).toString());
            list2.add(precativeBalanceY.setScale(2).toString());
        }
        data.setName("内部贷款");
        data.setData(list);
        data1.setName("银行借款");
        data1.setData(list1);
        data2.setName("委托贷款");
        data2.setData(list2);
        mergeList.add(data);
        mergeList.add(data1);
        mergeList.add(data2);

        nameList.add("内部贷款");
        nameList.add("银行借款");
        nameList.add("委托贷款");
        return mergeList;
    }

    /**融资期限*/
    private List<EchartsData> formatDataByTime(List<LoanBalanceAnalysisVO> resList,List<List<TbCreditPeriod>> timeList,List<String> nameList,String cycle) {//3
        List<EchartsData> mergeList = new ArrayList<>();
        EchartsData data = null;
        List<Object> list = null;
        
        BigDecimal balance = new BigDecimal(0);
        balance.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal balanceM = new BigDecimal(0);
        balanceM.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal balanceS = new BigDecimal(0);
        balanceS.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal balanceL = new BigDecimal(0);
        balanceL.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal balanceY = new BigDecimal(0);
        balanceY.setScale(2, BigDecimal.ROUND_HALF_UP);
        for(List<TbCreditPeriod> times : timeList) {
            for (TbCreditPeriod time : times) {
                data = new EchartsData();
                list = new ArrayList<Object>();
                for (LoanBalanceAnalysisVO res : resList) {
                    if (res.getPeriodName().equals(time.getPeriodName())) {
                        balance = balance.add(res.getCurrentBalance() == null ? BigDecimal.ZERO : res.getCurrentBalance());
                        balanceM = balanceM.add(res.getBalanceM() == null ? BigDecimal.ZERO : res.getBalanceM());
                        balanceS = balanceS.add(res.getBalanceS() == null ? BigDecimal.ZERO : res.getBalanceS());
                        balanceL = balanceL.add(res.getBalanceL() == null ? BigDecimal.ZERO : res.getBalanceL());
                        balanceY = balanceY.add(res.getBalanceY() == null ? BigDecimal.ZERO : res.getBalanceY());
                    }
                }
                if (balance.compareTo(BigDecimal.ZERO) != 0) {
                    list.add(balance.toString());
                    if (cycle.contains("M") || StringUtils.isEmpty(cycle)) {
                        list.add(balanceM.toString());
                    }
                    if (cycle.contains("S") || StringUtils.isEmpty(cycle)) {
                        list.add(balanceS.toString());
                    }
                    if (cycle.contains("L") || StringUtils.isEmpty(cycle)) {
                        list.add(balanceL.toString());
                    }

                    if (cycle.contains("Y") || StringUtils.isEmpty(cycle)) {
                        list.add(balanceY.toString());
                    }
                    data.setName(time.getPeriodName());
                    nameList.add(time.getPeriodName());
                    data.setData(list);
                    mergeList.add(data);

                    balance = BigDecimal.ZERO;
                    balanceM = BigDecimal.ZERO;
                    balanceS = BigDecimal.ZERO;
                    balanceL = BigDecimal.ZERO;
                    balanceY = BigDecimal.ZERO;
                }
            }
        }
        return mergeList;
    }

    /**融资单位*/
    private List<EchartsData> formatDataByCorp(List<LoanBalanceAnalysisVO> resList,List<String> nameList,List<String> corpIds,List<Object> mergeTitles,String cycle) {//3
        List<EchartsData> mergeList = new ArrayList<>();
        EchartsData data = new EchartsData();
        EchartsData data2 = new EchartsData();
        EchartsData data3 = new EchartsData();
        EchartsData data4 = new EchartsData();
        EchartsData data5 = new EchartsData();
        List<Object> list = new ArrayList<Object>();
        List<Object> list2 = new ArrayList<Object>();
        List<Object> list3 = new ArrayList<Object>();
        List<Object> list4 = new ArrayList<Object>();
        List<Object> list5 = new ArrayList<Object>();

        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal balanceM = BigDecimal.ZERO;
        BigDecimal balanceS = BigDecimal.ZERO;
        BigDecimal balanceL = BigDecimal.ZERO;
        BigDecimal balanceY = BigDecimal.ZERO;
        
        for (String corp:corpIds) {
            for (LoanBalanceAnalysisVO res:resList) {
                if(res.getCorpId().equals(corp)){
                    balance = balance.add(res.getCurrentBalance()==null?BigDecimal.ZERO:res.getCurrentBalance());
                    balanceM = balanceM.add(res.getBalanceM()==null?BigDecimal.ZERO:res.getBalanceM());
                    balanceS = balanceS.add(res.getBalanceS()==null?BigDecimal.ZERO:res.getBalanceS());
                    balanceL = balanceL.add(res.getBalanceL()==null?BigDecimal.ZERO:res.getBalanceL());
                    balanceY = balanceY.add(res.getBalanceY()==null?BigDecimal.ZERO:res.getBalanceY());
                }
            }
            if(balance.compareTo(BigDecimal.ZERO) != 0) {
                list.add(balance.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                data.setName("本期余额合计");
                nameList.add("本期余额合计");

                if(cycle.contains("M") || StringUtils.isEmpty(cycle)){
                    list2.add(balanceM.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                    data2.setName("上月末余额合计");
                    nameList.add("上月末余额合计");
                }
                if(cycle.contains("S") || StringUtils.isEmpty(cycle)) {
                    list3.add(balanceS.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                    data3.setName("上季末余额合计");
                    nameList.add("上季末余额合计");
                }
                if(cycle.contains("L") || StringUtils.isEmpty(cycle)){
                    list4.add(balanceL.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                    data4.setName("去年同期余额合计");
                    nameList.add("去年同期余额合计");
                }

                if(cycle.contains("Y") || StringUtils.isEmpty(cycle)){
                    list5.add(balanceY.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                    data5.setName("年初余额合计");
                    nameList.add("年初余额合计");
                }
                
                data.setData(list);
                data2.setData(list2);
                data3.setData(list3);
                data4.setData(list4);
                data5.setData(list5);
                
                CorpDTO corpDTO = corpService.get(corp);
                mergeTitles.add(corpDTO.getName());
                
                balance = BigDecimal.ZERO;
                balanceM = BigDecimal.ZERO;
                balanceS = BigDecimal.ZERO;
                balanceL = BigDecimal.ZERO;
                balanceY = BigDecimal.ZERO;
            }
        }
        mergeList.add(data);
        mergeList.add(data2);
        mergeList.add(data3);
        mergeList.add(data4);
        mergeList.add(data5);
        return mergeList;
    }
    
    public List<Object> getData(List<Object> mergeList,String cycle){//5
        mergeList.add("本期余额合计");
        if(cycle.contains("M") || StringUtils.isEmpty(cycle)){
            mergeList.add("上月末余额合计");
        }
        if(cycle.contains("S") || StringUtils.isEmpty(cycle)) {
            mergeList.add("上季末余额合计");
        }
        if(cycle.contains("L") || StringUtils.isEmpty(cycle)){
            mergeList.add("去年同期余额合计");
        }

        if(cycle.contains("Y") || StringUtils.isEmpty(cycle)){
            mergeList.add("年初余额合计");
        }
        return mergeList;
    }
    
    public static String createBar(String text,String xAxisName, List<Object> xData,String yAxisName,String yAxisUnit,List<EchartsData> dataList,List<String> nameList){//6
        String trigger = "axis";
        String x = "center";
        String y = "top";
        boolean isShow = true;
        String formatter="{value}" + yAxisUnit;
        List<Series> series = new ArrayList<Series>();
        //拿到图例
        Object[] legend = new Object[dataList.size()];
        if(!EmptyUtils.isEmptyList(dataList)){
            for (int i = 0; i < dataList.size(); i++) {
                legend[i] = dataList.get(i).getName();
                Bar temp = new Bar();
                temp.setName(dataList.get(i).getName());
                temp.setStack(dataList.get(i).getStack());
                temp.setData(dataList.get(i).getData());
                temp.setType(SeriesType.bar);
                temp.setBarWidth(20);
                temp.setBarMinHeight(10);
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
        toolbox.show(isShow).feature(Tool.mark,new DataZoom().show(false), new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        //设置图例
        Legend legendBean = new Legend();
        for (String name:nameList) {
            legendBean.data(name);
        }
        option.setLegend(legendBean);
        //设置x轴
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setName(xAxisName);
        xAxis.type(AxisType.category);
        if(EmptyUtils.isEmptyList(xData)){
            xData = new ArrayList<Object>();
        }
        xAxis.data(xData.toArray(new Object[xData.size()]));
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
    
    /**上月末*/
    public  Date getLastDayOfMonth(Date yearMonth) {
        int year = DateUtils.getYear(yearMonth);
        int month = DateUtils.getMonth(yearMonth);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1); //设置当前月的上一个月
        int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
        return cal.getTime();
    }

    /**上季度最后一天*/
    public Date getLastDayOfLastQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfLastQuarter(calendar.get(Calendar.YEAR),
                DateUtils.getQuarterOfYear(date));
    }

    public Date getLastDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 1;
        } else if (quarter == 2) {
            month = 3 - 1;
        } else if (quarter == 3) {
            month = 6 - 1;
        } else if (quarter == 4) {
            month = 9 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }


    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public  Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }
    
    public List<LoanBalanceAnalysisVO> viewLoanInfo(String corpId, String curId,String loanSign, String monthDate,String isContain,String repayCycle){
        String nowDate = DateUtils.getDate("yyyy-MM");//当期系统时间
        Date newMonthDate = null;
        if (monthDate.equals(nowDate)) {
            newMonthDate = new Date();
        } else {
            Date mDate = DateUtils.parse(monthDate,"yyyy-MM");
            Date lastMonthDate = DateUtils.getLastMonthDate(mDate);
            newMonthDate = lastMonthDate;
        }
        List<LoanBalanceAnalysisVO> list = loanBalanceAnalysisDao.viewLoanInfo(corpId,curId,loanSign,newMonthDate,isContain,repayCycle);
        if(!CollectionUtils.isEmpty(list)){
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrency();
            for(LoanBalanceAnalysisVO vo : list){
                if(null != corpMap){
                    if(corpMap.containsKey(vo.getCorpId())){
                        CorpDTO corpDTO = corpMap.get(vo.getCorpId());
                        if(null != corpDTO){
                            vo.setCorpName(corpDTO.getName());
                        }
                    }
                }
                if(null != currencyMap){
                    if(currencyMap.containsKey(vo.getCurId())){
                        BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                        if(null != btCurrencyDTO){
                            vo.setCurName(btCurrencyDTO.getCurName());
                        }
                    }
                }
                if(StringUtils.isNotEmpty(vo.getNetCode())){
                    String netName=corpService.getCorpName(vo.getNetCode());
                    vo.setNetName(netName);
                }
                if(StringUtils.isNotEmpty(vo.getPrecativeId())){
                    String netName=corpService.getCorpName(vo.getPrecativeId());
                    vo.setPrecativeName(netName);
                }
                if(StringUtils.isNotEmpty(vo.getLoanOpp())){
                    String netName=corpService.getCorpName(vo.getLoanOpp());
                    vo.setLoanOppName(netName);
                }
            }

        }
        return list;
    }

    private void setDate(DataTablesInput input) {
        String date = (String) input.getFormdata().get("monthDate");//获取参数时间
        String nowDate = DateUtils.getDate("yyyy-MM");//当期系统时间
        if (date.equals(nowDate)) {
            input.getFormdata().put("monthDate", DateUtils.getDateStr(new Date(), "yyyy-MM-dd"));
        } else {
            Date parse = DateUtils.parse(date, "yyyy-MM");
            Date lastMonthDate = DateUtils.getLastMonthDate(parse);
            input.getFormdata().put("monthDate", DateUtils.getDateStr(lastMonthDate, "yyyy-MM-dd"));
        }
    }

    private void setDate1(LoanBalanceAnalysisVO vo) {
        String date = DateUtils.format(vo.getMonthDate(),"yyyy-MM");//获取参数时间
        String nowDate = DateUtils.getDate("yyyy-MM");//当期系统时间
        if (date.equals(nowDate)) {
            vo.setMonthDate(new Date());
        } else {
            Date parse = DateUtils.parse(date, "yyyy-MM");
            Date lastMonthDate = DateUtils.getLastMonthDate(parse);
            vo.setMonthDate(lastMonthDate);
        }
    }


    /**
    *币种转换校验
    *@Param origCurId
    *@Param convertCurId
    *@Param date
    *@return
    */
    public void checkConvertCurId(String origCurId, String convertCurId, Date date){
        Date searchEndDate;
        if (null != date) {
            searchEndDate = DateUtils.getLastMonthDate(date);//预计查询截止日期(取查询结束所在月的最后一天)
        } else {
            searchEndDate = DateUtils.getLastMonthDate(new Date());
        }
        BigDecimal curExrate=BigDecimal.ZERO;
        if(StringUtils.isEmpty(origCurId)) {
            List<BtCurrencyDTO> curs = currencyService.getBtCurrency();
            if(!EmptyUtils.isEmptyList(curs)){
                for(BtCurrencyDTO cur :curs) {
                    if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(cur.getId())) {
                        curExrate = iFcExcrateAdjustService.getExchangeRateBy(cur.getId(), convertCurId, searchEndDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//距查询日期最近的币别汇率

                    }
                    if (BigDecimal.ZERO.compareTo(curExrate) == 0) {
                        throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
                    }
                }
            }
        }
    }

    public List<LoanBalanceAnalysisVO> getAppList(LoanBalanceAnalysisVO vo,String userId) throws ParseException {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_REPORT_LOAN_ANALYSIS_QUERY,userId);
        if(EmptyUtils.isNotEmpty(vo.getCorpId())) {
            corpIds = StringUtils.splitToList(",", vo.getCorpId());
        }
        Map<String,Object> param = new HashMap<>();
        param.put("typesOfStatistics",vo.getTypesOfStatistics());//统计维度
        param.put("isToCurr",vo.getIsToCurr());//统计维度
        setDate1(vo);
        param.put("monthDate",DateUtils.format(vo.getMonthDate(),"yyyy-MM-dd"));//查询时间
        if(StringUtils.isEmpty(vo.getCurId())){
            vo.setCurId(currencyService.getNatualCurrency().getId());//默认人民币
        }
        param.put("curId",vo.getCurId());
        param.put("isContain",vo.getIsContain());//包含在途

        List<LoanBalanceAnalysisVO> list = loanBalanceAnalysisDao.getAppList(vo,corpIds);//当前结果
        String cycle = vo.getCycle();
        List<LoanBalanceAnalysisVO> cycleList = cycleList(list,cycle,param,corpIds);//维度
        if(CmsConstant.IS_TRUE.equals(vo.getIsToCurr())){//折合本位币
            String mDate = (String)param.get("monthDate")+" 23:59:59";//对比周期
            Date monthDate = DateUtils.parse(mDate,"yyyy-MM-dd HH:mm:ss");
            String convertCurId = null;
            if (null != param.get("convertCurId") && StringUtils.isNotEmpty(param.get("convertCurId").toString())) {
                convertCurId = (String) param.get("convertCurId");//对比周期
            }
            toCurr(cycleList,monthDate,convertCurId);
        }

        List<LoanBalanceAnalysisVO> newList = new ArrayList<>();
        for(LoanBalanceAnalysisVO item : cycleList){
            if(item.getCurrentBalance().compareTo(BigDecimal.ZERO) != 0 ||
                    item.getBalanceY().compareTo(BigDecimal.ZERO) != 0 ||
                    item.getBalanceS().compareTo(BigDecimal.ZERO) != 0 ||
                    item.getBalanceL().compareTo(BigDecimal.ZERO) != 0 ||
                    item.getBalanceM().compareTo(BigDecimal.ZERO) != 0){
                newList.add(item);
            }
        }
        return newList;
    }

    /**融资期限 APP*/
    public  List<LoanBalanceAnalysisVO> groupDataByTime(List<LoanBalanceAnalysisVO> resList,String curId) {
        List<LoanBalanceAnalysisVO> chartsDatas = new ArrayList<>();//总的
        LoanBalanceAnalysisVO resVo = null;

        BigDecimal balance = new BigDecimal(0);
        balance.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal balanceM = new BigDecimal(0);
        balanceM.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal balanceS = new BigDecimal(0);
        balanceS.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal balanceL = new BigDecimal(0);
        balanceL.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal balanceY = new BigDecimal(0);
        balanceY.setScale(2, BigDecimal.ROUND_HALF_UP);

        String curCode = currencyService.getObjectById(curId).getCurCode();
        List<TbCreditPeriod> timeList = null;
        if("01".equals(curCode)) {
            timeList = tbCreditPeriodService.getrmbTbCreditPeriod();//人民币
        }else {
            timeList = tbCreditPeriodService.getforeignTbCreditPeriod();//外币
        }

        for (TbCreditPeriod time:timeList) {
            for (LoanBalanceAnalysisVO res:resList) {
                if(res.getPeriodName().equals(time.getPeriodName())){
                    balance = balance.add(res.getCurrentBalance()==null?BigDecimal.ZERO:res.getCurrentBalance());
                    balanceM = balanceM.add(res.getBalanceM()==null?BigDecimal.ZERO:res.getBalanceM());
                    balanceS = balanceS.add(res.getBalanceS()==null?BigDecimal.ZERO:res.getBalanceS());
                    balanceL = balanceL.add(res.getBalanceL()==null?BigDecimal.ZERO:res.getBalanceL());
                    balanceY = balanceY.add(res.getBalanceY()==null?BigDecimal.ZERO:res.getBalanceY());
                }
            }
            if(balance.compareTo(BigDecimal.ZERO) != 0) {
                resVo = new LoanBalanceAnalysisVO();
                resVo.setName(time.getPeriodName());
                resVo.setData(balance.toString());
                resVo.setxList("本期");
                chartsDatas.add(resVo);

                resVo = new LoanBalanceAnalysisVO();
                resVo.setName(time.getPeriodName());
                resVo.setData(balanceM.toString());
                resVo.setxList("上月末");
                chartsDatas.add(resVo);

                resVo = new LoanBalanceAnalysisVO();
                resVo.setName(time.getPeriodName());
                resVo.setData(balanceS.toString());
                resVo.setxList("上季末");
                chartsDatas.add(resVo);

                resVo = new LoanBalanceAnalysisVO();
                resVo.setName(time.getPeriodName());
                resVo.setData(balanceL.toString());
                resVo.setxList("去年同期");
                chartsDatas.add(resVo);

                resVo = new LoanBalanceAnalysisVO();
                resVo.setName(time.getPeriodName());
                resVo.setData(balanceY.toString());
                resVo.setxList("年初");
                chartsDatas.add(resVo);

                balance = BigDecimal.ZERO;
                balanceM = BigDecimal.ZERO;
                balanceS = BigDecimal.ZERO;
                balanceL = BigDecimal.ZERO;
                balanceY = BigDecimal.ZERO;
            }
        }
        return chartsDatas;
    }

    /**融资种类 APP*/
    public List<LoanBalanceAnalysisVO> groupDataByType(List<LoanBalanceAnalysisVO> resList) {//3
        List<LoanBalanceAnalysisVO>  chartsDatas = new ArrayList<>();
        LoanBalanceAnalysisVO resVo = null;

        BigDecimal loanBalance = BigDecimal.ZERO;
        BigDecimal provideBalance = BigDecimal.ZERO;
        BigDecimal precativeBalance = BigDecimal.ZERO;

        BigDecimal loanBalanceM = BigDecimal.ZERO;
        BigDecimal provideBalanceM = BigDecimal.ZERO;
        BigDecimal precativeBalanceM = BigDecimal.ZERO;

        BigDecimal loanBalanceS = BigDecimal.ZERO;
        BigDecimal provideBalanceS = BigDecimal.ZERO;
        BigDecimal precativeBalanceS = BigDecimal.ZERO;

        BigDecimal loanBalanceL = BigDecimal.ZERO;
        BigDecimal provideBalanceL = BigDecimal.ZERO;
        BigDecimal precativeBalanceL = BigDecimal.ZERO;

        BigDecimal loanBalanceY = BigDecimal.ZERO;
        BigDecimal provideBalanceY = BigDecimal.ZERO;
        BigDecimal precativeBalanceY = BigDecimal.ZERO;

        for (int i=0;i<resList.size();i++) {
            LoanBalanceAnalysisVO vo = resList.get(i);

            if("01".equals(vo.getLoanSign())) {
                loanBalance = loanBalance.add(vo.getCurrentBalance());
                loanBalanceM = loanBalanceM.add(vo.getBalanceM() == null ? BigDecimal.ZERO : vo.getBalanceM());
                loanBalanceS = loanBalanceS.add(vo.getBalanceS() == null ? BigDecimal.ZERO : vo.getBalanceS());
                loanBalanceL = loanBalanceL.add(vo.getBalanceL() == null ? BigDecimal.ZERO : vo.getBalanceL());
                loanBalanceY = loanBalanceY.add(vo.getBalanceY() == null ? BigDecimal.ZERO : vo.getBalanceY());

            }
            if("02".equals(vo.getLoanSign())){
                provideBalance = provideBalance.add(vo.getCurrentBalance());
                provideBalanceM = provideBalanceM.add(vo.getBalanceM()==null?BigDecimal.ZERO:vo.getBalanceM());
                provideBalanceS = provideBalanceS.add(vo.getBalanceS()==null?BigDecimal.ZERO:vo.getBalanceS());
                provideBalanceL = provideBalanceL.add(vo.getBalanceL()==null?BigDecimal.ZERO:vo.getBalanceL());
                provideBalanceY = provideBalanceY.add(vo.getBalanceY()==null?BigDecimal.ZERO:vo.getBalanceY());

            }
            if("03".equals(vo.getLoanSign())){
                precativeBalance = precativeBalance.add(vo.getCurrentBalance());
                precativeBalanceM = precativeBalanceM.add(vo.getBalanceM()==null?BigDecimal.ZERO:vo.getBalanceM());
                precativeBalanceS = precativeBalanceS.add(vo.getBalanceS()==null?BigDecimal.ZERO:vo.getBalanceS());
                precativeBalanceL = precativeBalanceL.add(vo.getBalanceL()==null?BigDecimal.ZERO:vo.getBalanceL());
                precativeBalanceY = precativeBalanceY.add(vo.getBalanceY()==null?BigDecimal.ZERO:vo.getBalanceY());
            }
        }
        BigDecimal loanBalance1 = loanBalance.setScale(2, RoundingMode.HALF_UP);
        BigDecimal loanBalanceM1 = loanBalanceM.setScale(2, RoundingMode.HALF_UP);
        BigDecimal loanBalanceS1 = loanBalanceS.setScale(2, RoundingMode.HALF_UP);
        BigDecimal loanBalanceL1 = loanBalanceL.setScale(2, RoundingMode.HALF_UP);
        BigDecimal loanBalanceY1 = loanBalanceY.setScale(2, RoundingMode.HALF_UP);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("内部贷款");
        resVo.setData(loanBalance1.toString());
        resVo.setxList("本期");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("银行借款");
        resVo.setData(provideBalance.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("本期");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("委托贷款");
        resVo.setData(precativeBalance.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("本期");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("内部贷款");
        resVo.setData(loanBalanceM1.toString());
        resVo.setxList("上月末");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("银行借款");
        resVo.setData(provideBalanceM.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("上月末");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("委托贷款");
        resVo.setData(precativeBalanceM.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("上月末");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("内部贷款");
        resVo.setData(loanBalanceS1.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("上季末");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("银行借款");
        resVo.setData(provideBalanceS.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("上季末");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("委托贷款");
        resVo.setData(precativeBalanceS.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("上季末");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("内部贷款");
        resVo.setData(loanBalanceL1.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("去年同期");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("银行借款");
        resVo.setData(provideBalanceL.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("去年同期");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("委托贷款");
        resVo.setData(precativeBalanceL.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("去年同期");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("内部贷款");
        resVo.setData(loanBalanceY1.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("年初");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("银行借款");
        resVo.setData(provideBalanceY.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("年初");
        chartsDatas.add(resVo);

        resVo = new LoanBalanceAnalysisVO();
        resVo.setName("委托贷款");
        resVo.setData(precativeBalanceY.setScale(2, RoundingMode.HALF_UP).toString());
        resVo.setxList("年初");
        chartsDatas.add(resVo);

        return chartsDatas;
    }

    /**融资单位APP*/
    public List<LoanBalanceAnalysisVO> groupDataByCorp(List<LoanBalanceAnalysisVO> resList,List<String> corpIds) {
        List<LoanBalanceAnalysisVO> chartsDatas = new ArrayList<>();
        LoanBalanceAnalysisVO resVo = null;
        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal balanceM = BigDecimal.ZERO;
        BigDecimal balanceS = BigDecimal.ZERO;
        BigDecimal balanceL = BigDecimal.ZERO;
        BigDecimal balanceY = BigDecimal.ZERO;

        for (String corp:corpIds) {
            for (LoanBalanceAnalysisVO res:resList) {
                if(res.getCorpId().equals(corp)){
                    balance = balance.add(res.getCurrentBalance()==null?BigDecimal.ZERO:res.getCurrentBalance());
                    balanceM = balanceM.add(res.getBalanceM()==null?BigDecimal.ZERO:res.getBalanceM());
                    balanceS = balanceS.add(res.getBalanceS()==null?BigDecimal.ZERO:res.getBalanceS());
                    balanceL = balanceL.add(res.getBalanceL()==null?BigDecimal.ZERO:res.getBalanceL());
                    balanceY = balanceY.add(res.getBalanceY()==null?BigDecimal.ZERO:res.getBalanceY());
                }
            }
            if(balance.compareTo(BigDecimal.ZERO) != 0) {
                CorpDTO corpDTO = corpService.get(corp);

                resVo = new LoanBalanceAnalysisVO();
                resVo.setData(balance.setScale(2).toString());
                resVo.setName("本期");
                resVo.setxList(corpDTO.getName());
                chartsDatas.add(resVo);

                resVo = new LoanBalanceAnalysisVO();
                resVo.setData(balanceM.setScale(2).toString());
                resVo.setName("上月末");
                resVo.setxList(corpDTO.getName());
                chartsDatas.add(resVo);

                resVo = new LoanBalanceAnalysisVO();
                resVo.setData(balanceS.setScale(2).toString());
                resVo.setName("上季末");
                resVo.setxList(corpDTO.getName());
                chartsDatas.add(resVo);

                resVo = new LoanBalanceAnalysisVO();
                resVo.setData(balanceL.setScale(2).toString());
                resVo.setName("去年同期");
                resVo.setxList(corpDTO.getName());
                chartsDatas.add(resVo);

                resVo = new LoanBalanceAnalysisVO();
                resVo.setData(balanceY.setScale(2).toString());
                resVo.setName("年初");
                resVo.setxList(corpDTO.getName());
                chartsDatas.add(resVo);

                balance = BigDecimal.ZERO;
                balanceM = BigDecimal.ZERO;
                balanceS = BigDecimal.ZERO;
                balanceL = BigDecimal.ZERO;
                balanceY = BigDecimal.ZERO;
            }
        }
        return chartsDatas;
    }

    @Override
    public BaseDao getEntityDao() {
        return loanBalanceAnalysisDao;
    }
}
