package com.bytter.report.domain.cms.service;

import com.bytter.report.domain.cms.dao.LoanBalanceTrendDao;
import com.bytter.report.domain.cms.vo.LoanBalanceTrendVO;
import com.bytter.report.domain.cms.vo.MobileCharDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import com.bytter.system.api.*;
import com.github.abel533.echarts.*;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.feature.DataZoom;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName LoanBalanceTrendService
 * @Description 贷款余额趋势Service
 * @Author fusb
 * @Date 2020/1/14 10:15
 * @Version 1.0
 **/
@Service("loanBalanceTrendService")
@Transactional
public class LoanBalanceTrendService extends BaseService {
    @Autowired
    private LoanBalanceTrendDao loanBalanceTrendDao;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private IBtCurrencyService btCurrencyService;
    @DubboReference
    private IDictService dictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference
    private IFcExcrateAdjustService fcExcrateAdjustService;

    /**
    *@Description 统计年月起止日期相差的年月集合
    *@Param startDate 开始日期
    *@Param endDate 结束日期
    *@return java.util.List<java.lang.String>
    *@Date 2020/1/14 10:57
    *@Author fusb
    */
    public List<String> tableInit(String startDate,String endDate,boolean isMobile){
        // 返回的日期集合
        List<String> monthList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date start = DateUtils.parseDate(startDate + "-01");
            Date end = DateUtils.parseDate(this.lastDateOfMonth(endDate));
            Calendar tempStart = Calendar.getInstance();
            tempStart.set(tempStart.get(Calendar.YEAR), tempStart.get(Calendar.MONTH), 1);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.set(tempEnd.get(Calendar.YEAR), tempEnd.get(Calendar.MONTH), 2);
            tempStart.setTime(start);
            tempEnd.setTime(end);
            if (isMobile) {//手机移动端最多显示6个月数据
                int i = 0;
                while (tempStart.before(tempEnd) && i < 6) {
                    monthList.add(sdf.format(tempStart.getTime()));
                    tempStart.add(Calendar.MONTH, 1);
                    i += 1;
                }
            } else {
                while (tempStart.before(tempEnd)) {
                    monthList.add(sdf.format(tempStart.getTime()));
                    tempStart.add(Calendar.MONTH, 1);
                }
            }

        }
        return monthList;
    }

    /**
    *@Description 查询贷款余额趋势信息
    *@Param parmasVO
    *@Param user
    *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    *@Date 2020/1/20 13:41
    *@Author fusb
    */
    @Transactional(readOnly = true)
    public List<Map<String,Object>> getLoanBalanceTrendList(LoanBalanceTrendVO parmasVO, ICurrentUser user){
        List<String> corpAuth = corpService.getUserCorpIds(CmsConstant.LOAN_BALANCE_TREND_QUERY, user.getId());
        List<String> monthList = this.tableInit(parmasVO.getStartDate(),parmasVO.getEndDate(),false);
        List<LoanBalanceTrendVO> list = loanBalanceTrendDao.loanBalanceTrendVOList(parmasVO,monthList,corpAuth);
        if (EmptyUtils.isEmpty(list)) {
            return null;
        }
        List<Map<String, String>> corpList = new ArrayList<Map<String, String>>();
        boolean flag;
        for (LoanBalanceTrendVO vo : list) {
            flag = true;
            for (int i = 0; i < corpList.size(); i++) {
                if (corpList.get(i).get("corpId").equals(vo.getCorpId()) && corpList.get(i).get("curName").equals(vo.getCurName())
                     && corpList.get(i).get("loanSign").equals(vo.getLoanSign())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Map<String, String> corpMaps = new HashMap<String, String>(2);
                corpMaps.put("corpId", vo.getCorpId());
                corpMaps.put("corpName", vo.getCorpName());
                corpMaps.put("curCode",vo.getCurCode());
                corpMaps.put("curName",vo.getCurName());
                corpMaps.put("loanSign",vo.getLoanSign());
                corpList.add(corpMaps);
            }
        }

        Date lastOfMonth = DateUtils.parse(this.lastDateOfMonth(monthList.get(monthList.size()-1)),"yyyy-MM-dd");
        // 组装数据
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();// 列表中显示的数据
        Map<String, Object> map;// 单行数据
        Map<String, Object> totalmap = new HashMap<>();
        BigDecimal rate = BigDecimal.ONE;  // 折合本位币汇率
        for (Map<String, String> corpMaps : corpList) { //行
            if (CommonConstant.YES.equals(parmasVO.getNatureCurrency()) &&
                    StringUtils.isNotEmpty(corpMaps.get("curCode")) &&
                    StringUtils.isNotEmpty(parmasVO.getConvertCurId())) {
                rate = fcExcrateAdjustService.getExchangeRateBy(corpMaps.get("curCode"), parmasVO.getConvertCurId(), DateUtils.setSpecifiedDateOfTime(lastOfMonth), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            }
            map = new HashMap<String,Object>();
            // 行和列对应的单位名称
            map.put("corpName",corpMaps.get("corpName"));
            // 行和列对应的贷款类型
            map.put("loanSign",corpMaps.get("loanSign"));
            map.put("loanSignName", dictService.get("Loan.Sign", corpMaps.get("loanSign")));
            // 行和列对应的币别名称
            map.put("curName",corpMaps.get("curName"));
            int i = 1;
            for (String years:monthList) { // 动态列
                BigDecimal balMoney = BigDecimal.ZERO; // 贷款余额
                for (LoanBalanceTrendVO vo:list) {
                    if (corpMaps.get("corpId").equals(vo.getCorpId()) && corpMaps.get("curCode").equals(vo.getCurCode())
                            && corpMaps.get("loanSign").equals(vo.getLoanSign()) && years.equals(vo.getTrendDate())) {
                        if ("1".equals(parmasVO.getNatureCurrency())) { // 是否折合币种
                            balMoney = moneyTypeExchange(vo.getBalMoney().multiply(rate),parmasVO.getMoneyType());
                        } else {
                            balMoney = moneyTypeExchange(vo.getBalMoney(),parmasVO.getMoneyType());
                        }
                    }
                }
                // 行和列对应的贷款余额
                map.put("balMoney" + i,balMoney);
                i ++;
            }
            mapList.add(map);
        }
        return mapList;
    }

    @Transactional(readOnly = true)
    public List<Map<String,Object>> getMobileLoanBalanceTrendList(LoanBalanceTrendVO parmasVO, ICurrentUser user){
        List<String> corpAuth = corpService.getUserCorpIds(CmsConstant.LOAN_BALANCE_TREND_QUERY, user.getId());
        List<String> monthList = this.tableInit(parmasVO.getStartDate(),parmasVO.getEndDate(),true);
        List<LoanBalanceTrendVO> list = loanBalanceTrendDao.loanBalanceTrendVOList(parmasVO,monthList,corpAuth);
        if (EmptyUtils.isEmpty(list)) {
            return null;
        }
        List<Map<String, String>> corpList = new ArrayList<Map<String, String>>();
        boolean flag;
        for (LoanBalanceTrendVO vo : list) {
            flag = true;
            for (int i = 0; i < corpList.size(); i++) {
                if (corpList.get(i).get("corpId").equals(vo.getCorpId()) && corpList.get(i).get("curName").equals(vo.getCurName())
                        && corpList.get(i).get("loanSign").equals(vo.getLoanSign())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Map<String, String> corpMaps = new HashMap<String, String>(2);
                corpMaps.put("corpId", vo.getCorpId());
                corpMaps.put("corpName", vo.getCorpName());
                corpMaps.put("curCode",vo.getCurCode());
                corpMaps.put("curName",vo.getCurName());
                corpMaps.put("loanSign",vo.getLoanSign());
                corpList.add(corpMaps);
            }
        }
        BigDecimal rate = BigDecimal.ONE;  // 折合本位币汇率
        Date lastOfMonth = DateUtils.parse(this.lastDateOfMonth(monthList.get(monthList.size()-1)),"yyyy-MM-dd");
        String convertCurName = "人民币";
        if ("1".equals(parmasVO.getNatureCurrency())) {
            convertCurName = btCurrencyService.getObjectById(parmasVO.getConvertCurId()).getCurName();
            rate = fcExcrateAdjustService.getExchangeRateBy(parmasVO.getCurCode(), parmasVO.getConvertCurId(), DateUtils.setSpecifiedDateOfTime(lastOfMonth), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        } else {
            rate = fcExcrateAdjustService.getExchangeRateBy(parmasVO.getCurCode(), btCurrencyService.getRMBCurrency().getId(), DateUtils.setSpecifiedDateOfTime(lastOfMonth), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        }
        // 组装数据
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();// 列表中显示的数据
        Map<String, Object> map;// 单行数据
        Map<String, Object> totalmap = new HashMap<>();
        for (Map<String, String> corpMaps : corpList) { //行
            map = new HashMap<String,Object>();
            // 行和列对应的单位名称
            map.put("corpName",corpMaps.get("corpName"));
            // 行和列对应的贷款类型
            map.put("loanSign",corpMaps.get("loanSign"));
            map.put("loanSignName", dictService.get("Loan.Sign", corpMaps.get("loanSign")));
            // 行和列对应的币别名称
            map.put("curName",corpMaps.get("curName"));
            int i = 1;
            for (String years:monthList) { // 动态列
                BigDecimal balMoney = BigDecimal.ZERO; // 贷款余额
                for (LoanBalanceTrendVO vo:list) {
                    if (corpMaps.get("corpId").equals(vo.getCorpId()) && corpMaps.get("curCode").equals(vo.getCurCode())
                            && corpMaps.get("loanSign").equals(vo.getLoanSign()) && years.equals(vo.getTrendDate())) {
                        if ("1".equals(parmasVO.getNatureCurrency())) { // 是否折合币种
                            balMoney = moneyTypeExchange(vo.getBalMoney().multiply(rate),parmasVO.getMoneyType());
                        } else {
                            balMoney = moneyTypeExchange(vo.getBalMoney(),parmasVO.getMoneyType());
                        }
                    }
                }
                // 行和列对应的贷款余额
                map.put("balMoney" + i,balMoney);
//                设置合计金额
                if (null == totalmap.get("balMoney" + i)) {
                    if ("1".equals(parmasVO.getNatureCurrency())) {
                        totalmap.put("balMoney" + i, balMoney.toPlainString());
                    } else {
                        totalmap.put("balMoney" + i, (balMoney.multiply(rate)).toPlainString());
                    }
                } else {
                    BigDecimal tmpMoney = BigDecimal.valueOf(Double.valueOf(totalmap.get("balMoney" + i).toString()));
                    if ("1".equals(parmasVO.getNatureCurrency())) {
                        totalmap.put("balMoney" + i, tmpMoney.add(balMoney));
                    } else {
                        totalmap.put("balMoney" + i, tmpMoney.add(balMoney.multiply(rate)));
                    }
                }
                i ++;
            }
            mapList.add(map);
        }
        totalmap.put("corpName", "合计");
        totalmap.put("curName", "折合" + convertCurName);
        mapList.add(totalmap);
        return mapList;
    }

    /**
    *@Description 获取图表信息
    *@Param parmasVO
    *@Param user
    *@return java.lang.Object
    *@Date 2020/1/20 13:42
    *@Author fusb
    */
    public Object loanBalanceChart(LoanBalanceTrendVO parmasVO,ICurrentUser user,String isInit) {
        List<String> corpAuth = corpService.getUserCorpIds(CmsConstant.LOAN_BALANCE_TREND_QUERY, user.getId());
        List<LoanBalanceTrendVO> list = new ArrayList<>();
        List<String> monthsList = this.tableInit(parmasVO.getStartDate(),parmasVO.getEndDate(),false);
        if (CommonConstant.YES.equals(isInit)) {
            list.addAll(loanBalanceTrendDao.loanBalanceChart(parmasVO,monthsList,corpAuth));
        }
//        List<LoanBalanceTrendVO> list = loanBalanceTrendDao.loanBalanceChart(parmasVO,monthsList,corpAuth);
        Object jsonObject = dealData(list,monthsList,parmasVO);
        return jsonObject;
    }

    /**
    *构造图表数据
    *@Param parmasVO	
    *@Param user
    *@return 
    */
    public List<Map<String,Object>> getMobileChartData(LoanBalanceTrendVO parmasVO,ICurrentUser user){
        List<String> corpAuth = new ArrayList<>();
        if (StringUtils.isNotEmpty(parmasVO.getCorpId())) {
            corpAuth.addAll(Arrays.asList(parmasVO.getCorpId().split(",")));
        } else {
            corpAuth.addAll(corpService.getUserCorpIds(CmsConstant.LOAN_BALANCE_TREND_QUERY, user.getId()));
        }

        List<String> monthsList = this.tableInit(parmasVO.getStartDate(),parmasVO.getEndDate(),true);
        BigDecimal rate = BigDecimal.ONE;  // 折合本位币汇率
        Date lastOfMonth = DateUtils.parse(this.lastDateOfMonth(monthsList.get(monthsList.size()-1)),"yyyy-MM-dd");
        if ("1".equals(parmasVO.getNatureCurrency())) {
            rate = fcExcrateAdjustService.getExchangeRateBy(parmasVO.getCurCode(),parmasVO.getConvertCurId(),lastOfMonth, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        }
        List<CorpDTO> corpDTOS = corpService.listCorpDTOByCorpIds(corpAuth);

        List<Map<String, Object>> mobileDatas = new ArrayList<>();
//        按照单位分类
        if (null != corpDTOS && corpDTOS.size() > 0) {
            for (CorpDTO corpDTO : corpDTOS) {
                Map<String, Object> mobileData = new HashMap<>();
                parmasVO.setCorpId(corpDTO.getId());
                List<MobileCharDataVO> mobileCharDataVOS = new ArrayList<>();
                for (String endDate : monthsList) {
                    MobileCharDataVO mobileCharDataVO1;
                    MobileCharDataVO mobileCharDataVO2;
                    MobileCharDataVO mobileCharDataVO3;
                    List<LoanBalanceTrendVO> list1 = loanBalanceTrendDao.getinnerLoanMobileData(parmasVO, this.lastDateOfMonth(endDate), endDate, corpDTO.getId());
                    List<LoanBalanceTrendVO> list2 = loanBalanceTrendDao.getPrecativeLoanMobileData(parmasVO, this.lastDateOfMonth(endDate), endDate, corpDTO.getId());
                    List<LoanBalanceTrendVO> list3 = loanBalanceTrendDao.getProvideLoanMobileData(parmasVO, this.lastDateOfMonth(endDate), endDate, corpDTO.getId());
                    if (null == list1 || list1.size() == 0) {
                        mobileCharDataVO1 = getLoanMobileCharDataVO(BigDecimal.ZERO,endDate);
                    }else {
                        mobileCharDataVO1 = getLoanMobileCharDataVO(list1.get(0).getBalMoney().multiply(rate),endDate);
                    }

                    if (null == list2 || list2.size() == 0) {
                        mobileCharDataVO2 = getPrecativeMobileCharDataVO(BigDecimal.ZERO,endDate);
                    }else{
                        mobileCharDataVO2 = getPrecativeMobileCharDataVO(list2.get(0).getBalMoney().multiply(rate),endDate);
                    }
                    if (null == list3 || list3.size() == 0) {
                        mobileCharDataVO3 = getProvideMobileCharDataVO(BigDecimal.ZERO,endDate);
                    } else {
                        mobileCharDataVO3 = getProvideMobileCharDataVO(list3.get(0).getBalMoney().multiply(rate),endDate);
                    }
                    mobileCharDataVOS.add(mobileCharDataVO1);
                    mobileCharDataVOS.add(mobileCharDataVO2);
                    mobileCharDataVOS.add(mobileCharDataVO3);
                }
//                构建数据
                mobileData.put("corpName", corpDTO.getName());
                mobileData.put("corpMbileData", mobileCharDataVOS);
                mobileDatas.add(mobileData);
            }
        }
        return mobileDatas;
    }

    /**
    *内部贷款图表数据
    *@Param yValue	
    *@Param month
    *@return 
    */
    public MobileCharDataVO getLoanMobileCharDataVO(BigDecimal yValue,String month){
        MobileCharDataVO mobileCharDataVO = new MobileCharDataVO();
        mobileCharDataVO.setCategoryName("内部贷款");
        mobileCharDataVO.setxValue(month);
        mobileCharDataVO.setyValue(yValue);
        return mobileCharDataVO;
    }

    /**
    *内部贷款图表数据
    *@Param yValue
    *@Param month
    *@return
    */
    public MobileCharDataVO getProvideMobileCharDataVO(BigDecimal yValue,String month){
        MobileCharDataVO mobileCharDataVO = new MobileCharDataVO();
        mobileCharDataVO.setCategoryName("银行借款");
        mobileCharDataVO.setxValue(month);
        mobileCharDataVO.setyValue(yValue);
        return mobileCharDataVO;
    }

    /**
    *内部贷款图表数据
    *@Param yValue
    *@Param month
    *@return
    */
    public MobileCharDataVO getPrecativeMobileCharDataVO(BigDecimal yValue, String month) {
        MobileCharDataVO mobileCharDataVO = new MobileCharDataVO();
        mobileCharDataVO.setCategoryName("委托贷款");
        mobileCharDataVO.setxValue(month);
        mobileCharDataVO.setyValue(yValue);
        return mobileCharDataVO;
    }

    /**
    *@Description 处理图表数据
    *@Param loanBalanceTrendVOList
    *@Param monthsList
    *@return java.lang.Object
    *@Date 2020/1/20 13:42
    *@Author fusb
    */
    private Object dealData(List<LoanBalanceTrendVO> loanBalanceTrendVOList,List<String> monthsList,LoanBalanceTrendVO parmasVO) {
        List<String> loanTypeList = new ArrayList<String>(3);
        loanTypeList.add(dictService.get("Loan.Sign",CmsConstant.LOAN_SIGN_01)); // 内部贷款
        loanTypeList.add(dictService.get("Loan.Sign",CmsConstant.LOAN_SIGN_02)); // 银行借款
        loanTypeList.add(dictService.get("Loan.Sign",CmsConstant.LOAN_SIGN_03)); // 委托贷款
        List<EchartsData> echartsDatas = formatData(loanBalanceTrendVOList,monthsList,loanTypeList,parmasVO);
        String chartTitle = multiLangServiceI.getLang("cms.loanBalance.trend");
        String yAxisName = dictService.get("cms.MoneyType",parmasVO.getMoneyType());
        return createBar(chartTitle, "", getData(monthsList), yAxisName, "", echartsDatas,loanTypeList);
    }

    /**
    *@Description 获取图表x周数据信息
    *@Param monthsList
    *@return java.util.List<java.lang.Object>
    *@Date 2020/1/20 13:42
    *@Author fusb
    */
    public List<Object> getData(List<String> monthsList){//5
        List<Object> mergeList = new ArrayList<>();
        for (int i = 0;i < monthsList.size();i++) {
            mergeList.add(monthsList.get(i));
        }
        return mergeList;
    }

    /**
    *@Description 封装图表数据
    *@Param loanBalanceTrendVOList
    *@Param monthsList
    *@return java.util.List<com.bytter.core.echarts.data.EchartsData>
    *@Date 2020/1/20 13:42
    *@Author fusb
    */
    private List<EchartsData> formatData(List<LoanBalanceTrendVO> loanBalanceTrendVOList,List<String> monthsList,List<String> loanTypeList,LoanBalanceTrendVO parmasVO) {
        List<EchartsData> mergeList = new ArrayList<>();
        EchartsData innerLoan = new EchartsData(); // 内部贷款
        EchartsData bankLoan = new EchartsData(); // 银行借款
        EchartsData prectiveLoan = new EchartsData(); // 委托贷款
        List<Object> innerLoanlist = new ArrayList<Object>(); // 内部贷款集合
        List<Object> bankLoanList = new ArrayList<Object>(); // 银行借款集合
        List<Object> prectiveLoanList = new ArrayList<Object>(); // 委托贷款集合
        List<String> loanSignList = new ArrayList<String>(3);
        loanSignList.add(CmsConstant.LOAN_SIGN_01); // 内部贷款
        loanSignList.add(CmsConstant.LOAN_SIGN_02); // 银行借款
        loanSignList.add(CmsConstant.LOAN_SIGN_03); // 委托贷款
        // 折合本位币
        BigDecimal rate = BigDecimal.ONE;  // 折合本位币汇率
        Date lastOfMonth = DateUtils.parse(this.lastDateOfMonth(monthsList.get(monthsList.size()-1)),"yyyy-MM-dd");
        if ("1".equals(parmasVO.getNatureCurrency())) {
            if (StringUtils.isNotEmpty(parmasVO.getCurCode()) && StringUtils.isNotEmpty(parmasVO.getConvertCurId())) {
                rate = fcExcrateAdjustService.getExchangeRateBy(parmasVO.getCurCode(),parmasVO.getConvertCurId(),lastOfMonth, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            }
//            rate = new BigDecimal(7.3);
        }
        //查询截至日期
        for (String loanSign:loanSignList) {
            for (int i = 0; i < monthsList.size(); i++) {
                BigDecimal balMoney = BigDecimal.ZERO; // 贷款余额
                for (LoanBalanceTrendVO vo : loanBalanceTrendVOList) {
                    if (loanSign.equals(vo.getLoanSign()) && monthsList.get(i).equals(vo.getTrendDate())) {
                        balMoney = balMoney.add(vo.getBalMoney().multiply(rate));
                    }
                }
                balMoney = moneyTypeExchange(balMoney,parmasVO.getMoneyType());
                if (CmsConstant.LOAN_SIGN_01.equals(loanSign)) {
                    innerLoanlist.add(balMoney.setScale(2).toPlainString());
                }
                if (CmsConstant.LOAN_SIGN_02.equals(loanSign)) {
                    bankLoanList.add(balMoney.setScale(2).toPlainString());
                }
                if (CmsConstant.LOAN_SIGN_03.equals(loanSign)) {
                    prectiveLoanList.add(balMoney.setScale(2).toPlainString());
                }
            }
        }
        innerLoan.setName(loanTypeList.get(0)); // 内部贷款
        innerLoan.setData(innerLoanlist);
        bankLoan.setName(loanTypeList.get(1)); // 银行借款
        bankLoan.setData(bankLoanList);
        prectiveLoan.setName(loanTypeList.get(2)); // 委托贷款
        prectiveLoan.setData(prectiveLoanList);
        mergeList.add(innerLoan);
        mergeList.add(bankLoan);
        mergeList.add(prectiveLoan);
        return mergeList;
    }

    /**
     * 创建图表
     *
     * @param text
     * @param xAxisName
     * @param xData
     * @param yAxisName
     * @param yAxisUnit
     * @param dataList
     * @return
     */
    public static String createBar(String text, String xAxisName, List<Object> xData, String yAxisName, String yAxisUnit, List<EchartsData> dataList,List<String> loanTypeList) {
        String trigger = "axis";
        String x = "center";
        String y = "top";
        boolean isShow = true;
        String formatter = "{value}" + yAxisUnit;
        List<Series> series = new ArrayList<Series>();
        //拿到图例
        Object legend[] = new Object[dataList.size()];
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                legend[i] = dataList.get(i).getName();
                Line temp = new Line();
                temp.setName(dataList.get(i).getName());
                temp.setStack(dataList.get(i).getStack());
                temp.setData(dataList.get(i).getData());
                temp.setType(SeriesType.line);
                series.add(temp);
            }
            if (dataList.get(0).getData().size() == 1) {
                y = "bottom";
                trigger = "item";
            }
        } else {
            series = new ArrayList<Series>();
            Bar seriesBean = new Bar();
            seriesBean.setBarWidth(10);
            seriesBean.data(new ArrayList());
            series.add(seriesBean);
        }
        Option option = new Option();
        //设置标题
        Title title = new Title();
        title.setRight("50%");
        title.setText(text);
        option.setTitle(title);
        //设置工具提示
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger(Trigger.valueOf(trigger));
        option.setTooltip(tooltip);
        //设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(Tool.mark,new DataZoom().show(false), new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        //设置图例
        Legend legendBean = new Legend(loanTypeList.toArray());
        legendBean.setX(Align.right);
        legendBean.setOrient(Orient.vertical);
        legendBean.setTop(200);

        option.setLegend(legendBean);
        //设置x轴
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setName(xAxisName);
        xAxis.type(AxisType.category);
        if (EmptyUtils.isEmptyList(xData)) {
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

    /**
     * 根据年月获取月末最后一天日期
     *
     * @param dateStr
     * @return
     */
    public String lastDateOfMonth(String dateStr) {
        String date[] = dateStr.split("-");
        Calendar calendar = Calendar.getInstance();
        int year = new Integer(date[0]).intValue();
        int month = new Integer(date[1]).intValue();
        // 设置年份
        calendar.set(Calendar.YEAR, year);
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = calendar.getActualMaximum(Calendar.DATE);
        // 设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
    *@Description 金额单位转换
    *@Param money
    *@Param moneyType
    *@return java.math.BigDecimal
    *@Date 2020/1/20 11:32
    *@Author fusb
    */
    public BigDecimal moneyTypeExchange(BigDecimal money,String moneyType) {
        BigDecimal unit = null;
        switch (moneyType) {
            case CmsConstant.MONEY_TYPE_1 : unit = BigDecimal.ONE;break;
            case CmsConstant.MONEY_TYPE_1000 : unit = BigDecimal.valueOf(1000);break;
            case CmsConstant.MONEY_TYPE_10000 : unit = BigDecimal.valueOf(10000);break;
            case CmsConstant.MONEY_TYPE_100000000 : unit = BigDecimal.valueOf(100000000);break;
            default:
                unit = BigDecimal.ONE;
        }
        return money.divide(unit).setScale(2,BigDecimal.ROUND_HALF_UP);
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
        if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(origCurId)){
            curExrate = fcExcrateAdjustService.getExchangeRateBy(origCurId,convertCurId, searchEndDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//距查询日期最近的币别汇率

        }
        if (BigDecimal.ZERO.compareTo(curExrate) == 0) {
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
        }
    }

    @Override
    public BaseDao getEntityDao() {
        return null;
    }
}
