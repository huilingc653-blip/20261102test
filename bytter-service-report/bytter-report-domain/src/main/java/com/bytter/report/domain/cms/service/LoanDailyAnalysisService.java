package com.bytter.report.domain.cms.service;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.report.domain.cms.dao.LoanDailyAnalysisDao;
import com.bytter.report.domain.cms.utils.LoanDailyAnalysisUtils;
import com.bytter.report.domain.cms.vo.LoanDailyAnalysisVO;
import com.bytter.report.domain.cms.vo.LoanDailyParamVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import com.github.abel533.echarts.data.Data;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @Title: LoanDailyAnalysisService
 * @ProjectName T3(3.2)
 * @Description: 日均贷款分析报表服务层
 * @date 2020/2/24 14:01
 */
@Service("LoanDailyAnalysisService")
public class LoanDailyAnalysisService extends BaseService {

    @Autowired
    private LoanDailyAnalysisDao analysisDao;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference
    private IFcExcrateAdjustService iFcExcrateAdjustService;

    @Override
    public BaseDao getEntityDao() {
        return null;
    }

    /**
     * 获取当前用户有权限的单位
     * @param user
     * @return
     */
    public List<String> getUserCorps( ICurrentUser user){
        List<CorpDTO> corpDTOS = iCorpService.listUserCorp(ResourceConstant.CMS_REPORT_LOANDAILY_ANALYSIS_QUERY, user.getId());
        List<String> corpIds = new ArrayList<>();
        for (CorpDTO dto:corpDTOS){
            corpIds.add(dto.getId());
        }
        return corpIds;
    }

    /**
     * 查询日均贷款报表数据(统计维度:单位)
     */

    public DataTablesOutput corpDatatable(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds=getUserCorps(user);
        DataTablesOutput output=analysisDao.corpDatatable(input, corpIds);
        List<LoanDailyAnalysisVO> ldrAnalysisVOS = output.getData();

        if(!CollectionUtils.isEmpty(ldrAnalysisVOS)){
            Map<String, CorpDTO> corpMap = iCorpService.getCorpMap();
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrency();
            for(LoanDailyAnalysisVO vo : ldrAnalysisVOS){
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
        String isConvertStandCur = (String) input.getFormdata().get("isConvertStandCur");//是否折合本位币
        String moneyUnit = (String) input.getFormdata().get("moneyUnit");//币别单位
        if (isConvertStandCur.equals(CmsConstant.IS_TRUE)) {//是否折合本位币
            String satrtDate=(String)input.getFormdata().get("startDate");
            String endDate=(String)input.getFormdata().get("endDate");
            String searchFrequency=(String)input.getFormdata().get("searchFrequency");
            Map<String,Object> dateMap=LoanDailyAnalysisUtils.getSerarchDateMap(satrtDate,endDate,searchFrequency);
            Date searchEndDate=(Date)dateMap.get("searchEndDate");
            String convertCurId = (String) input.getFormdata().get("convertCurId");//折合币种
            changeToStandCur(ldrAnalysisVOS,searchEndDate,convertCurId);
        }
        if (!moneyUnit.equals(CmsConstant.MONEY_TYPE_1)) {//金额单位不是'元'，需要换算
            conversion(ldrAnalysisVOS, moneyUnit);
        }

        LoanDailyParamVO paramVO=setParam(input);
        this.calDailyLoanMoney(ldrAnalysisVOS,paramVO);//计算日均贷款额度
        return output;
    }

    /**
     * 查询日均贷款报表数据(统计维度:贷款机构)
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput bankDatatable(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds=getUserCorps(user);
        DataTablesOutput<LoanDailyAnalysisVO> output=new DataTablesOutput<>();
        List<LoanDailyAnalysisVO> dailyAnalysisVOS=new ArrayList<>();
        String loanSign=(String)input.getFormdata().get("loanSign");//融资方式
        DataTablesOutput outputBank=new DataTablesOutput(); //获取以银行作为贷款机构的数据
        List<LoanDailyAnalysisVO> bankVOS=new ArrayList<>();
        //未选择融资种类或者融资种类为银行借款或者委托贷款的情况下
        if(StringUtils.isBlank(loanSign)||loanSign.equals(CmsConstant.LOAN_SIGN_02)||loanSign.equals(CmsConstant.LOAN_SIGN_03)){
            Map<String, BankTypeDTO> bankTypeMap = bankTypeService.getBankTypeMap();
            outputBank=analysisDao.bankDatatable(input, corpIds);
            bankVOS = outputBank.getData();
            bankVOS.forEach(item->{
                if(null != bankTypeMap){
                    BankTypeDTO bankTypeDTO = bankTypeMap.get(item.getBankType());
                    if(null != bankTypeDTO){
                        item.setBankTypeName(bankTypeDTO.getTypeName());
                    }
                }
            });
            dailyAnalysisVOS.addAll(bankVOS);
        }
        //获取以结算中心作为贷款机构的数据
        Map<String, Object> params = input.getFormdata();
        params.put("netFlag", "1");
        input.setFormdata(params);
        DataTablesOutput outputNet=new DataTablesOutput();
        List<LoanDailyAnalysisVO> netVOS=new ArrayList<>();

        //未选择融资种类或者融资种类为内部贷款或者委托贷款的情况下
        if(StringUtils.isBlank(loanSign)||loanSign.equals(CmsConstant.LOAN_SIGN_01)||loanSign.equals(CmsConstant.LOAN_SIGN_03)){
            outputNet=analysisDao.bankDatatable(input, corpIds);
            netVOS=outputNet.getData();
            Map<String, String> nameCorpMap = iCorpService.getNameByIds(netVOS.stream().map(LoanDailyAnalysisVO::getBankType)
                    .collect(Collectors.toList()));
            netVOS.forEach(item->{
                item.setBankTypeName(nameCorpMap.getOrDefault(item.getBankType(), ""));
                item.setNetId(item.getBankType());
            });
            dailyAnalysisVOS.addAll(netVOS);
        }

        Map<String, String> currencyNameMap = btCurrencyService.getCurrencyNameByIds(dailyAnalysisVOS.stream().map(LoanDailyAnalysisVO::getCurId)
                .collect(Collectors.toList()));
        dailyAnalysisVOS.forEach(item->{
            item.setCurName(currencyNameMap.get(item.getCurId()));
        });

        String isConvertStandCur = (String) input.getFormdata().get("isConvertStandCur");//是否折合本位币
        String moneyUnit = (String) input.getFormdata().get("moneyUnit");//币别单位
        if (isConvertStandCur.equals(CmsConstant.IS_TRUE)) {//是否折合本位币
            String satrtDate=(String)input.getFormdata().get("startDate");
            String endDate=(String)input.getFormdata().get("endDate");
            String searchFrequency=(String)input.getFormdata().get("searchFrequency");
            Map<String,Object> dateMap=LoanDailyAnalysisUtils.getSerarchDateMap(satrtDate,endDate,searchFrequency);
            Date searchEndDate=(Date)dateMap.get("searchEndDate");
            String convertCurId = (String) input.getFormdata().get("convertCurId");//折合币种
            changeToStandCur(dailyAnalysisVOS,searchEndDate,convertCurId);
        }
        if (!moneyUnit.equals(CmsConstant.MONEY_TYPE_1)) {//金额单位不是'元'，需要换算
            conversion(dailyAnalysisVOS, moneyUnit);
        }
        LoanDailyParamVO paramVO=setParam(input);
        //计算日均贷款额度
        this.calDailyLoanMoney(dailyAnalysisVOS,paramVO);

        //将数据
        output.setData(dailyAnalysisVOS);
        output.setRecordsTotal(outputBank.getRecordsTotal()+outputNet.getRecordsTotal());//重新设置，便于分页数据显示
        output.setDraw(outputBank.getDraw()+outputNet.getDraw());
        output.setRecordsFiltered(outputBank.getRecordsFiltered()+outputNet.getRecordsFiltered());
        return output;
    }

    /**
     * 将input参数转换
     */
    public  LoanDailyParamVO setParam(DataTablesInput input){
        LoanDailyParamVO paramVO=new LoanDailyParamVO();
        paramVO.setStartDate((String)input.getFormdata().get("startDate"));
        paramVO.setEndDate((String)input.getFormdata().get("endDate"));
        paramVO.setSearchFrequency((String)input.getFormdata().get("searchFrequency"));//查询频率 0-按天查询 1-按月查询
        paramVO.setIsConvertStandCur((String) input.getFormdata().get("isConvertStandCur"));//是否折合本位币
        paramVO.setMoneyUnit((String) input.getFormdata().get("moneyUnit"));//币别单位
        paramVO.setLoanSign((String) input.getFormdata().get("loanSign"));
        paramVO.setIsContain((String) input.getFormdata().get("isContain"));
        paramVO.setTypesOfStatistics((String) input.getFormdata().get("typesOfStatistics"));
        paramVO.setSearchCorps((String) input.getFormdata().get("corpId"));
        paramVO.setConvertCurId((String) input.getFormdata().get("convertCurId"));//折合币种
        String corps=(String)input.getFormdata().get("corpId");//查询条件
        List<String> corpIds = null;
        if(EmptyUtils.isNotEmpty(corps)) {
            corpIds = StringUtils.splitToList(",", corps);
        }
        paramVO.setCorps(corpIds);
        return paramVO;
    }

    /**
     * 折合本位币操作
     */
    public void changeToStandCur(List<LoanDailyAnalysisVO> list,Date searchEndDate,String convertCurId){
        if (null != list && list.size() > 0) {
            BigDecimal curExrate = BigDecimal.ONE;
            for (LoanDailyAnalysisVO vo : list) {
                if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(vo.getCurId())) {
                    curExrate = iFcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), convertCurId, searchEndDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//查询距离截止日期最新币别汇率
                }
                if (curExrate!=null&&curExrate.equals(BigDecimal.ONE)) {//汇率为1时不需要折合
                    continue;
                }
                vo.setCurLoanMoney(vo.getCurLoanMoney()==null?BigDecimal.ZERO:vo.getCurLoanMoney().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));
                vo.setPreLoanMoney(vo.getPreLoanMoney()==null?BigDecimal.ZERO:vo.getPreLoanMoney().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));
                vo.setLastLoanMoney(vo.getLastLoanMoney()==null?BigDecimal.ZERO:vo.getLastLoanMoney().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));
                vo.setDailyBalance(vo.getDailyBalance()==null?BigDecimal.ZERO:vo.getDailyBalance().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }
    }


    /**金额单位换算*/
    public void conversion(List<LoanDailyAnalysisVO>list,String moneyType){
        if(null!=list&&list.size()>0){
            BigDecimal divisor = BigDecimal.ZERO;
            if(moneyType.equals(CmsConstant.MONEY_TYPE_1000)){//千元
                divisor = new BigDecimal(1000);
            }
            if(moneyType.equals(CmsConstant.MONEY_TYPE_10000)){//万元
                divisor = new BigDecimal(10000);
            }
            if(moneyType.equals(CmsConstant.MONEY_TYPE_100000000)){//亿元
                divisor = new BigDecimal(100000000);
            }
            for (LoanDailyAnalysisVO vo : list){
                vo.setCurLoanMoney(vo.getCurLoanMoney()==null?BigDecimal.ZERO:vo.getCurLoanMoney().divide(divisor));
                vo.setPreLoanMoney(vo.getPreLoanMoney()==null?BigDecimal.ZERO:vo.getPreLoanMoney().divide(divisor));
                vo.setLastLoanMoney(vo.getLastLoanMoney()==null?BigDecimal.ZERO:vo.getLastLoanMoney().divide(divisor));
                vo.setDailyBalance(vo.getDailyBalance()==null?BigDecimal.ZERO:vo.getDailyBalance().divide(divisor));
            }
        }
    }


    /**
     * 计算日均/月均贷款
     */
    public void calDailyLoanMoney(List<LoanDailyAnalysisVO> vos,LoanDailyParamVO paramVO){
        LoanDailyParamVO params=new LoanDailyParamVO();
        BeanUtils.copyNotNullProperties(paramVO,params);
        params.setSearchEndDate(paramVO.getEndDate());
        Map<String,Object> dateMap= LoanDailyAnalysisUtils.getDateMap(paramVO.getStartDate(),paramVO.getEndDate(),paramVO.getSearchFrequency());
        int divisor=1;//计算本期日均或月均的除数
        int preDivisor=1;//计算上期和去年同期日均或月均的除数
        if(paramVO.getSearchFrequency().equals(CmsConstant.SEARCH_BY_MONTH)){//按月查询
            divisor=(Integer) dateMap.get("monthsDiff");//查询日期之间相隔的月份数
            preDivisor=divisor;
        }else{
            divisor=(Integer) dateMap.get("dayDiffs");//查询日期之间相隔的天数
            preDivisor=(Integer) dateMap.get("preDayDiffs");//上期或上年同期查询日期之间相隔的天数
        }
        List<LoanDailyAnalysisVO> dailyDataList=new ArrayList<>();
        List<Object> list=new ArrayList<>();
        String startDate=DateUtils.formatDate((Date)dateMap.get("searchStartDate"));
        String endDate=DateUtils.formatDate((Date)dateMap.get("searchEndDate"));
        for (LoanDailyAnalysisVO analysisVO : vos) {
            params.setCurId(analysisVO.getCurId());
            params.setBankType(analysisVO.getBankType());
            params.setNetId(analysisVO.getNetId());
            params.setCorpId(analysisVO.getCorpId());
            if(!StringUtils.isBlank(analysisVO.getLoanSign())){
                params.setLoanSign(analysisVO.getLoanSign());
            }
            //计算本期日均贷款余额
            params.setStartDate(startDate);
            params.setEndDate(endDate);
            dailyDataList=getDailyBalances(params);//查询贷款余额数据
            list = setSeriesData(paramVO,dailyDataList);//用于存放待显示的数据
            BigDecimal dataAverage=calDailyAverage(list,divisor);
            analysisVO.setCurLoanDailyMoney(dataAverage);

            //计算上期日均
            params.setStartDate(DateUtils.formatDate((Date)dateMap.get("preStartDate")));
            params.setEndDate(DateUtils.formatDate((Date)dateMap.get("preEndDate")));
            dailyDataList=getDailyBalances(params);//查询贷款余额数据
            list = setSeriesData(params,dailyDataList);//用于存放待显示的数据
            BigDecimal preDailyAverage=calDailyAverage(list,preDivisor);
            analysisVO.setPreLoanDailyMoney(preDailyAverage);

            //较上期差额(本期_日均-较上期_日均)
            analysisVO.setPreDifference(analysisVO.getCurLoanDailyMoney().subtract(analysisVO.getPreLoanDailyMoney()));
            //计算较上期比例(较上期_差额/本期_日均)
            if(analysisVO.getPreDifference().compareTo(BigDecimal.ZERO)!=0){
                analysisVO.setPreRate(analysisVO.getPreDifference().divide(analysisVO.getCurLoanDailyMoney(),4, BigDecimal.ROUND_HALF_UP));
            }

            //计算上年同期日均
            params.setStartDate(DateUtils.formatDate((Date)dateMap.get("lastStartDate")));
            params.setEndDate(DateUtils.formatDate((Date)dateMap.get("lastEndDate")));
            dailyDataList=getDailyBalances(params);//查询贷款余额数据
            list = setSeriesData(params,dailyDataList);//用于存放待显示的数据
            BigDecimal lastDailyAverage=calDailyAverage(list,preDivisor);
            analysisVO.setLastLoanDailyMoney(lastDailyAverage);

            //较上年同期差额(本期_日均-较上年同期_日均)
            analysisVO.setLastDifference(analysisVO.getCurLoanDailyMoney().subtract(analysisVO.getLastLoanDailyMoney()));
            //上年同期比例(较上年同期_差额/本期_日均)
            if(analysisVO.getLastDifference().compareTo(BigDecimal.ZERO)!=0){
                if (BigDecimal.ZERO.compareTo(analysisVO.getCurLoanDailyMoney()) != 0) {
                    analysisVO.setLastRate(analysisVO.getLastDifference().divide(analysisVO.getCurLoanDailyMoney(),4, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
    }

    /**
     * 获取饼图的相关数据
     * @return
     */
    public  Map<String,Object> getLoansPieDatas(List<LoanDailyAnalysisVO> loanVOS) {
        Map<String, Object> pieDatas = new HashMap<>();//用于存放传到前端的数据集合
        List<String> legends = new ArrayList<>();//用于存放图例显示数据
        Map<String,String> loanTypes=new HashMap<>();
        loanTypes.put(CmsConstant.LOAN_SIGN_01,multiLangServiceI.getLang("cms.report.loan"));//内部贷款
        loanTypes.put(CmsConstant.LOAN_SIGN_02,multiLangServiceI.getLang("cms.report.loan.provide"));//银行借款
        loanTypes.put(CmsConstant.LOAN_SIGN_03,multiLangServiceI.getLang("cms.loan.precative"));//委托贷款

        Data[] datas = new Data[loanTypes.size()];//值域数据
        int i=0;
        for (String key : loanTypes.keySet()) {
            com.github.abel533.echarts.data.Data data = new Data();
            data.setName(loanTypes.get(key));
            for(LoanDailyAnalysisVO vo:loanVOS){
                if(key.equals(vo.getLoanSign())){
                    legends.add(vo.getLoanSignName());
                    data.setValue(vo.getCurLoanDailyMoney());
                }
            }
            datas[i] = data;
            i++;
        }
        pieDatas.put("seriesData",JsonUtils.toJSONString(datas));
        pieDatas.put("legendsData",JsonUtils.toJSONString(legends));
        return pieDatas;
    }

    /**
     * 根据条件查询不同统计维度下各类贷款分布情况
     * @return
     */
    public List<LoanDailyAnalysisVO>  statisticsDtlDatatable(LoanDailyParamVO paramVO,ICurrentUser user) {
        Map<String,Object> dateMap=LoanDailyAnalysisUtils.getSerarchDateMap(paramVO.getStartDate(),paramVO.getEndDate(),paramVO.getSearchFrequency());
        List<String> corpIds=new ArrayList<>();
        if(EmptyUtils.isNotEmpty(paramVO.getSearchCorps())){
            corpIds.add(paramVO.getSearchCorps());
        }else{
            corpIds=getUserCorps(user);
        }
        LoanDailyParamVO searchVO=new LoanDailyParamVO();
        BeanUtils.copyNotNullProperties(paramVO,searchVO);
        searchVO.setCorps(corpIds);
        searchVO.setStartDate(DateUtils.date2Str((Date)dateMap.get("searchStartDate"),DateUtils.date_sdf()));
        searchVO.setEndDate(DateUtils.date2Str((Date)dateMap.get("searchEndDate"),DateUtils.date_sdf()));
        searchVO.setSearchEndDate(DateUtils.date2Str((Date)dateMap.get("searchEndDate"),DateUtils.date_sdf()));
        List<LoanDailyAnalysisVO> vos=new ArrayList<>();
        String typesOfStatistics = searchVO.getTypesOfStatistics();//统计维度 0-单位 1-贷款机构
        if(null!=typesOfStatistics&&typesOfStatistics.equals("1")){
            String netId = searchVO.getNetId();
            if(!StringUtils.isBlank(netId)){//贷款机构是否为结算中心
                vos=analysisDao.balanceListByNetId(searchVO,CmsConstant.IS_TRUE);//根据结算中心查询各类贷款
                CorpDTO corpDTO=new CorpDTO();
                for(LoanDailyAnalysisVO analysisVO:vos){
                    analysisVO.setNetId(analysisVO.getBankType());
                    corpDTO=iCorpService.get(analysisVO.getBankType());
                    if(corpDTO!=null){
                        analysisVO.setBankTypeName(corpDTO.getName());
                    }
                }
            }else{
                vos=analysisDao.balanceListByBankType(searchVO,CmsConstant.IS_TRUE);//根据银行类型查询各类贷款
            }
        }else{
            vos=analysisDao.balanceListByCorp(searchVO,CmsConstant.IS_TRUE);//根据单位查询各类贷款
        }
        this.calDailyLoanMoney(vos,paramVO);//计算日均/月均

        if(!CollectionUtils.isEmpty(vos)){
            Map<String, CorpDTO> corpMap = iCorpService.getCorpMap();
            Map<String, BtCurrencyDTO> currency = btCurrencyService.getCurrency();
            for(LoanDailyAnalysisVO vo : vos){
                if(null != corpMap){
                    CorpDTO corpDTO = corpMap.get(vo.getCorpId());
                    if(null != corpDTO){
                        vo.setCorpName(corpDTO.getName());
                    }
                }
                if(null != currency){
                    BtCurrencyDTO btCurrencyDTO = currency.get(vo.getCurId());
                    if(null != btCurrencyDTO){
                        vo.setCurName(btCurrencyDTO.getCurName());
                    }
                }
            }

        }
        return vos;
    }

    /**
     * 查询截止某天的贷款余额
     */
    public LoanDailyAnalysisVO getUpToDayBalance(LoanDailyParamVO paramVO){
        LoanDailyAnalysisVO dailyAnalysisVO=null;
        String typesOfStatistics = paramVO.getTypesOfStatistics();//统计维度 0-单位 1-贷款机构
        List<LoanDailyAnalysisVO> voList=new ArrayList<>();
        if(null!=typesOfStatistics&&typesOfStatistics.equals("1")){
            String netId =paramVO.getNetId();
            if(!StringUtils.isBlank(netId)){//贷款机构是否为结算中心
                voList=analysisDao.balanceListByNetId(paramVO,CmsConstant.IS_FALSE);//根据结算中心查询截止某天贷款余额
            }else{
                voList=analysisDao.balanceListByBankType(paramVO,CmsConstant.IS_FALSE);//根据银行类型查询截止某天贷款余额
            }
        }else{
            voList=analysisDao.balanceListByCorp(paramVO,CmsConstant.IS_FALSE);//根据单位查询截止某天贷款余额
        }
        Date balanceDate=DateUtils.str2Date(paramVO.getEndDate(),DateUtils.date_sdf());
        if(null!=voList&&voList.size()>0){
            dailyAnalysisVO=voList.get(0);
        }else{
            dailyAnalysisVO=new LoanDailyAnalysisVO();
            dailyAnalysisVO.setDailyBalance(BigDecimal.ZERO);
        }
        dailyAnalysisVO.setCurId(paramVO.getCurId());
        dailyAnalysisVO.setBalanceDate(balanceDate);
        return dailyAnalysisVO;
    }

    /**
     * 获取折线图和柱状图数据
     * @return
     */
    public  Map<String, Object> getDailyBalanceData(LoanDailyParamVO paramVO,ICurrentUser user,Map<String,Object> dateMap) {
        Map<String, Object> graphDatas=new HashMap<>();//传到前端的数据集合
        List<String> corps=new ArrayList<>();
        if(StringUtils.isBlank(paramVO.getSearchCorps())){//查询条件中网点的选项为空时取用户权限下可以查询的网点
            corps=getUserCorps(user);
        }else{
            String[] corpsArr=paramVO.getSearchCorps().split(",");
            for(String corp:corpsArr){
                corps.add(corp);
            }
        }
        int divisor=1;//计算日均或月均的除数
        if(paramVO.getSearchFrequency().equals(CmsConstant.SEARCH_BY_MONTH)){//按月查询
            divisor=(Integer) dateMap.get("monthsDiff");//查询日期之间相隔的月份数
        }else{
            divisor=(Integer) dateMap.get("dayDiffs");//查询日期之间相隔的天数
        }
        paramVO.setCorps(corps);
        paramVO.setStartDate(DateUtils.date2Str((Date)dateMap.get("searchStartDate"),DateUtils.date_sdf()));
        paramVO.setEndDate(DateUtils.date2Str((Date)dateMap.get("searchEndDate"),DateUtils.date_sdf()));
        paramVO.setSearchEndDate(DateUtils.date2Str((Date)dateMap.get("searchEndDate"),DateUtils.date_sdf()));
        List<LoanDailyAnalysisVO> vos=getDailyBalances(paramVO);//查询贷款余额数据
        List<Object> list = setSeriesData(paramVO,vos);//用于存放待显示的数据
        graphDatas.put("seriesData",JsonUtils.toJSONString(list));

        BigDecimal dataAverage=calDailyAverage(list,divisor);
        graphDatas.put("dataAverage",dataAverage);

        List<String> xData =setXData(paramVO,divisor);//设置横坐标的值
        graphDatas.put("xData",JsonUtils.toJSONString(xData));
        return graphDatas;
    }

    /**
     * 计算日均贷款余额
     * @param list
     * @param divisor
     * @return
     */
    public BigDecimal calDailyAverage(List<Object> list,int divisor){
        BigDecimal dataSum=BigDecimal.ZERO;
        BigDecimal dataAverage=BigDecimal.ZERO;//数据的日均值或月均值
        for(Object object:list){
            dataSum=dataSum.add((BigDecimal) object);
        }
        dataAverage=dataSum.divide(BigDecimal.valueOf(divisor),2, BigDecimal.ROUND_HALF_UP);
        return dataAverage;
    }

    /**
     * 设置x轴的值
     * @return
     */
    public List<String> setXData(LoanDailyParamVO paramVO, int divisor) {
        List<String > mergeList = new ArrayList<>();
        if(paramVO.getSearchFrequency().equals(CmsConstant.SEARCH_BY_MONTH)){//按月查询，横坐标显示月份
            mergeList=getMonthList(divisor,paramVO.getStartDate());
        }else{//按天查询
            mergeList=getDayList(divisor,paramVO.getStartDate());
        }
        return mergeList;
    }

    /**
     * 按月查询时获取月份集合
     * @return
     */
    public List<String> getMonthList(int monthsDiff,String date){
        Date searchStartDate=DateUtils.str2Date(date,DateUtils.date_sdf());
        List<String> monthList=new ArrayList();//用于存放月份
        String nextMonth="";
        for(int i=0;i<monthsDiff;i++){
            nextMonth=DateUtils.format(DateUtils.addMonth(searchStartDate,i),"yyyy-MM");
            monthList.add(nextMonth);
        }
        return monthList;

    }

    /**
     * 按天查询时获取查询区间的日期集合
     * @return
     */
    public List<String> getDayList(int daysDiff,String startDate){
        Date searchStartDate=DateUtils.str2Date(startDate,DateUtils.date_sdf());//查询开始日期
        String dateTemp=null;
        List<String> dayList=new ArrayList<>();
        for(int i=0;i<daysDiff;i++){
            dateTemp=DateUtils.format(DateUtils.addDay(searchStartDate,i),"yyyy-MM-dd");
            dayList.add(dateTemp);
        }
        return dayList;
    }

    /**
     * 设置折线图或柱状图显示的数据
     */
    public List<Object> setSeriesData(LoanDailyParamVO paramVO,List<LoanDailyAnalysisVO> vos){
        Map<String,Object> dateMap=LoanDailyAnalysisUtils.getSerarchDateMap(paramVO.getStartDate(),paramVO.getEndDate(),paramVO.getSearchFrequency());
        List<Object> list = new ArrayList<Object>();//用于存放待显示的数据
        if(paramVO.getSearchFrequency().equals(CmsConstant.SEARCH_BY_MONTH)) {//按月查询，横坐标显示月份
            list=setDailyBalanceDataByMonth(vos,dateMap);
        }else{
            list=setDailyBalanceDataByDay(vos,dateMap);
        }
        return  list;
    }


    /**
     * 按月查询时，将数据分月整合
     * @param vos
     * @return
     */
    public List<Object> setDailyBalanceDataByMonth(List<LoanDailyAnalysisVO> vos,Map<String,Object> dateMap){
        List<Object> list = new ArrayList<Object>();
        int monthsDiff=(Integer) dateMap.get("monthsDiff");//查询日期之间相隔的天数
        String searchStartDate=DateUtils.date2Str((Date)dateMap.get("searchStartDate"),DateUtils.date_sdf());//查询开始日期
        List<String> months=getMonthList(monthsDiff,searchStartDate);
        Map<String,List<LoanDailyAnalysisVO>> monthMap=new HashMap<>();//分月份存放数据的集合
        String balanceDate="";
        List<LoanDailyAnalysisVO> analysisVOList;
        for(LoanDailyAnalysisVO analysisVO:vos){
            balanceDate=DateUtils.format(analysisVO.getBalanceDate(),"yyyy-MM");
            analysisVOList=monthMap.get(balanceDate);
            if(analysisVOList==null){
                analysisVOList=new ArrayList<>();
                analysisVOList.add(analysisVO);
                monthMap.put(balanceDate,analysisVOList);
            }else{
                analysisVOList.add(analysisVO);
            }
        }

        BigDecimal monthSum=BigDecimal.ZERO;//某个月的本期余额
        List<LoanDailyAnalysisVO> voList=new ArrayList<>();
        for(String month:months){
            voList=monthMap.get(month);
            if(voList!=null&&voList.size()>0){
                for(LoanDailyAnalysisVO analysisVO:voList){
                    monthSum=monthSum.add(analysisVO.getDailyBalance());
                }
            }
            list.add(monthSum);
        }
        return list;
    }

    /**
     * 按天查询时，将数据整合，当天无数据的将余额置0
     * @param vos
     * @return
     */
    public List<Object> setDailyBalanceDataByDay(List<LoanDailyAnalysisVO> vos,Map<String,Object> dateMap){
        List<Object> dailyBalanceList=new ArrayList<>();
        Map<Object,LoanDailyAnalysisVO> dayMap=new HashMap<>();
        String balDate="";
        for(LoanDailyAnalysisVO analysisVO:vos){
            balDate=DateUtils.date2Str(analysisVO.getBalanceDate(),DateUtils.date_sdf());
            dayMap.put(balDate,analysisVO);
        }
        int days=(Integer) dateMap.get("dayDiffs");//查询日期之间相隔的天数
        String searchStartDate=DateUtils.date2Str((Date)dateMap.get("searchStartDate"),DateUtils.date_sdf());//查询开始日期
        List<String> dayList=getDayList(days,searchStartDate);//获取查询区间的日期集合
        BigDecimal dailyBalance=BigDecimal.ZERO;
        LoanDailyAnalysisVO vo;
        for(Object date:dayList){
            vo=dayMap.get(date);
            if(vo!=null){
                dailyBalance=dailyBalance.add(vo.getDailyBalance());
            }
            dailyBalanceList.add(dailyBalance);
        }
        return dailyBalanceList;
    }

    /**
     * 分日期获取贷款情况
     * @param paramVO
     * @return
     */
    public List<LoanDailyAnalysisVO> getDailyBalances(LoanDailyParamVO paramVO){
        LoanDailyAnalysisVO firstVO;;//截止某天或某月的余额
        String lastDate=paramVO.getStartDate();
        LoanDailyParamVO dailyParamVO=new LoanDailyParamVO();
        BeanUtils.copyNotNullProperties(paramVO,dailyParamVO,"startDate","endDate");
        if(paramVO.getSearchFrequency().equals(CmsConstant.SEARCH_BY_MONTH)) {//按月查询
            Date startDate=DateUtils.str2Date(paramVO.getStartDate(),DateUtils.date_sdf());
            lastDate=DateUtils.date2Str(DateUtils.getLastMonthDate(startDate),DateUtils.date_sdf());//开始日期所在月的最后一天
            dailyParamVO.setEndDate(lastDate);
            firstVO=getUpToDayBalance(dailyParamVO);
            dailyParamVO.setStartDate(lastDate);
        }else{
            dailyParamVO.setEndDate(paramVO.getStartDate());
            firstVO=getUpToDayBalance(dailyParamVO);
            dailyParamVO.setStartDate(paramVO.getStartDate());
        }
        dailyParamVO.setEndDate(paramVO.getEndDate());
        List<LoanDailyAnalysisVO> balanceList=analysisDao.dailySendMoneyList(dailyParamVO);//查询某个区间内发放额度变化
        List<LoanDailyAnalysisVO> repayList=analysisDao.dailyRepayMoneyList(dailyParamVO);//查询某个区间内还款额度
        Map<Object,LoanDailyAnalysisVO> balanceMap=new HashMap<Object,LoanDailyAnalysisVO>();
        balanceMap.put(firstVO.getBalanceDate(),firstVO);
        for(LoanDailyAnalysisVO sendVO:balanceList){
            balanceMap.put(sendVO.getBalanceDate(),sendVO);
        }

        LoanDailyAnalysisVO dailyAnalysisVO;
        for(LoanDailyAnalysisVO repayVO:repayList){
            dailyAnalysisVO=balanceMap.get(repayVO.getRepayDate());
            if(dailyAnalysisVO==null){
                dailyAnalysisVO=new LoanDailyAnalysisVO();
                dailyAnalysisVO.setBalanceDate(repayVO.getRepayDate());
                dailyAnalysisVO.setDailyBalance(BigDecimal.ZERO.subtract(repayVO.getRepayMoney()));
                dailyAnalysisVO.setCurId(repayVO.getCurId());
                balanceMap.put(repayVO.getRepayDate(),dailyAnalysisVO);
            }else{
                dailyAnalysisVO.setDailyBalance(dailyAnalysisVO.getDailyBalance().subtract(repayVO.getRepayMoney()));
            }
        }

        if(paramVO.getIsContain().equals(CmsConstant.IS_FALSE)){//不包含在途,余额=放款金额-已还款金额-还款在途金额
            List<LoanDailyAnalysisVO> repayAwayList=analysisDao.dailyRepayAwayMoneyList(dailyParamVO);//某个区间内还款在途金额
            for(LoanDailyAnalysisVO repayAwayVO:repayAwayList){
                dailyAnalysisVO=balanceMap.get(repayAwayVO.getRepayAwayDate());
                if(dailyAnalysisVO==null){
                    dailyAnalysisVO=new LoanDailyAnalysisVO();
                    dailyAnalysisVO.setBalanceDate(repayAwayVO.getRepayAwayDate());
                    dailyAnalysisVO.setDailyBalance(BigDecimal.ZERO.subtract(repayAwayVO.getRepayAwayMoney()));
                    dailyAnalysisVO.setCurId(repayAwayVO.getCurId());
                    balanceMap.put(repayAwayVO.getRepayAwayDate(),dailyAnalysisVO);
                }else{
                    dailyAnalysisVO.setDailyBalance(dailyAnalysisVO.getDailyBalance().subtract(repayAwayVO.getRepayAwayMoney()));
                }
            }
        }

        List<LoanDailyAnalysisVO> voList=new ArrayList<>();
        for (Object key : balanceMap.keySet()) {
            voList.add(balanceMap.get(key));
        }

        if(paramVO.getIsConvertStandCur().equals(CmsConstant.IS_TRUE)){//折合本位币
            Date searchEndDate;
            if (paramVO.getSearchFrequency().equals(CmsConstant.SEARCH_BY_MONTH)) {
                searchEndDate = DateUtils.str2Date(paramVO.getSearchEndDate()+"-01", DateUtils.date_sdf());//取查询日期的结束日期
                searchEndDate = DateUtils.getLastMonthDate(searchEndDate);
                if (DateUtils.dayDiffs(searchEndDate, new Date()) > 0) {
                    searchEndDate = new Date();
                }
            } else {
                searchEndDate = DateUtils.str2Date(paramVO.getSearchEndDate(), DateUtils.date_sdf());//取查询日期的结束日期
            }
            changeToStandCur(voList,searchEndDate,paramVO.getConvertCurId());
        }

        if (!paramVO.getMoneyUnit().equals(CmsConstant.MONEY_TYPE_1)) {//金额单位不是'元'，需要换算
            conversion(voList, paramVO.getMoneyUnit());
        }
        return voList;
    }


    /**
     *币种转换校验
     *@Param origCurId
     *@Param convertCurId
     *@return
     */
    public void checkConvertCurId(String origCurId, String convertCurId,Date date){
        BigDecimal curExrate=BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(origCurId)){
            if (null == date) {
                date = new Date();
            }
            curExrate = iFcExcrateAdjustService.getExchangeRateBy(origCurId,convertCurId, date, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//距查询日期最近的币别汇率

        }
        if (BigDecimal.ZERO.compareTo(curExrate) == 0) {
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
        }
    }
}