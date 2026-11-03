package com.bytter.report.domain.netbank.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.report.domain.netbank.dao.HomeBankAccBalDistributeDao;
import com.bytter.report.domain.netbank.vo.HomeBankAccBalChartVO;
import com.bytter.report.api.vo.HomeBankAccBalDistributeSearchVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccBalDistributeVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccBalVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 银行账户余额分布Service
 * @author huangx 2022-03-28
 */
@Service("homeBankAccBalDistributeService")
@Transactional
public class HomeBankAccBalDistributeService extends BaseService<HomeBankAccBalDistributeVO, String> {
    @Autowired
    private HomeBankAccBalDistributeDao bankAccBalDistributeDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
	private IDictService dictService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private IBtBankAccCommonService bankAccCommonService;
    @Override
    public BaseDao<HomeBankAccBalDistributeVO, String> getEntityDao() {
        return bankAccBalDistributeDao;
    }
    /**
     * 查询银行账户余额概览
     * @param searchVO
     */
    public void queryBankAccBalDistribute(Model model, HomeBankAccBalDistributeSearchVO searchVO, ICurrentUser user) {
        //获取数据
        Map<String, Object> map = this.initBankAccBal(searchVO, user);
        model.addAttribute("curBal", map.get("curBal"));
        model.addAttribute("listBal", map.get("listBal"));
        model.addAttribute("listBalCur", map.get("listBalCur"));
        //当前月份
        model.addAttribute("month", DateUtils.getMonth(DateUtils.getDate()));
        //当前年份
        model.addAttribute("year", DateUtils.getYear());
        List<String> years = new ArrayList<>();
        for(int i=0;i<10;i++){
            years.add((DateUtils.getYear() - i)+"");
        }
        model.addAttribute("years", years);
    }
    /**
     * 查询银行账户余额概览
     * @param searchVO
     */
    public Map<String, Object> initBankAccBal(HomeBankAccBalDistributeSearchVO searchVO, ICurrentUser user) {
        Map<String, Object> dataMap = new HashMap<>();
        List<String> corpIds = null;
        if(StringUtils.isNotBlank(searchVO.getCorpId())){
            corpIds = new ArrayList<>();
            for(String corpId:searchVO.getCorpId().split(CommonConstant.COMMA_SPLIT)){
                corpIds.add(corpId);
            }
        } else {
            //获取资源权限范围内的单位信息
            corpIds = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
        }
        //获取账户余额
        String corpAuthSql = " and " + SqlUtil.getInSql(corpIds, "acc.corp_Id", true);
        //权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
            corpAuthSql += "and " + SqlUtil.getInSql(accList, "acc.id", true);
        }
        //获取账户余额集合
        List<HomeBankAccBalVO> listBal = this.bankAccBalDistributeDao.getBankAccBal(corpAuthSql);
        setCommonField(listBal);
        //折合币种及金额单位转换
        this.convertMoneyType(listBal, searchVO.getMoneyType());
        //当前余额、境内账户余额、境外账户余额（折合人民币）
        HomeBankAccBalVO curBal = this.getBankAccBal(listBal);
        dataMap.put("curBal", curBal);
        //账户余额分布
        dataMap.put("listBal", listBal);
        //余额币别占比
        List<HomeBankAccBalVO> listBalCur = this.getBankAccBalGroupByCur(searchVO, listBal);
        dataMap.put("listBalCur", listBalCur);
        return dataMap;
    }

    private void setCommonField(List<HomeBankAccBalVO> vos) {
        if (CollectionUtil.isEmpty(vos)) {
            return;
        }
        List<String> curIds = new ArrayList<>();
        List<String> bankTypeIds = new ArrayList<>();
        List<String> corpIds = new ArrayList<>();
        for (HomeBankAccBalVO vo : vos) {
            if (StringUtils.isNotEmpty(vo.getCurId())) {
                curIds.add(vo.getCurId());
            }
            if (StringUtils.isNotEmpty(vo.getBankTypeId())) {
                bankTypeIds.add(vo.getBankTypeId());
            }
            if (StringUtils.isNotEmpty(vo.getCorpId())) {
                corpIds.add(vo.getCorpId());
            }
        }
        Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(curIds);
        Map<String, BankTypeDTO> bankTypeDTOMap = bankTypeService.listBankTypeMapByIds(bankTypeIds);
        List<BisAccAttributeDTO> allBisAccAttributes = bankAccCommonService.getAllBisAccAttributes();
        Map<String, BisAccAttributeDTO> accAttributeDTOMap = allBisAccAttributes.stream().collect(Collectors.toMap(BisAccAttributeDTO::getId, Function.identity()));
        List<BtAccNatureDTO> allBtAccNature = bankAccCommonService.getAllBtAccNature();
        Map<String, BtAccNatureDTO> accNatureDTOMap = allBtAccNature.stream().collect(Collectors.toMap(BtAccNatureDTO::getId, Function.identity()));
        Map<String, String> corpNameMap = corpService.getNameByIds(corpIds);
        for (HomeBankAccBalVO vo : vos) {
            if (StringUtils.isNotEmpty(vo.getCorpId())) {
                vo.setCorpName(corpNameMap.get(vo.getCorpId()));
            }
            if (StringUtils.isNotEmpty(vo.getCurId())) {
                vo.setCurName(currencyNameMap.get(vo.getCurId()));
            }
            if (StringUtils.isNotEmpty(vo.getBankTypeId())) {
                vo.setBankTypeName(bankTypeDTOMap.get(vo.getBankTypeId()).getTypeName());
            }
            if (StringUtils.isNotEmpty(vo.getAttributeId())) {
                vo.setAttributeName(accAttributeDTOMap.get(vo.getAttributeId()).getAttributeName());
            }
            if (StringUtils.isNotEmpty(vo.getNatureId())) {
                vo.setNatureName(accNatureDTOMap.get(vo.getNatureId()).getNatureName());
            }
        }
    }

    /**
     * 金额单位转换
     * @param listBal
     * @param moneyType
     */
    public void convertMoneyType(List<HomeBankAccBalVO> listBal, Integer moneyType) {
        //币种折算
        //获取人民币
        BtCurrencyDTO natualCurrency = currencyService.getNatualCurrency();
        //汇率集合
        HashMap<String, BigDecimal> exchangeRateMap = new HashMap<>();
        for (HomeBankAccBalVO vo:listBal) {
            if(StringUtils.isBlank(vo.getCurId()) || vo.getCurId().equals(natualCurrency.getId())){
                vo.setBal(vo.getBal().divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
                if(vo.getAvailBal() != null) {
                    vo.setAvailBal(vo.getAvailBal().divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
                }
                if(vo.getFrzBal() != null) {
                    vo.setFrzBal(vo.getFrzBal().divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
                }
            } else {
                BigDecimal exchangeRate;
                if(exchangeRateMap.containsKey(vo.getCurId())){
                    exchangeRate = exchangeRateMap.get(vo.getCurId());
                } else {
                    exchangeRate = fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), natualCurrency.getId(), DateUtils.setSpecifiedDateOfTime(DateUtils.getDate()), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                    if (null == exchangeRate) {
                        exchangeRate = BigDecimal.ZERO;
                    }
                    exchangeRateMap.put(vo.getCurId(), exchangeRate);
                }
                vo.setBal(vo.getBal().multiply(exchangeRate).divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
                if(vo.getAvailBal() != null) {
                    vo.setAvailBal(vo.getAvailBal().multiply(exchangeRate).divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
                }
                if(vo.getFrzBal() != null) {
                    vo.setFrzBal(vo.getFrzBal().multiply(exchangeRate).divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
    }
    private List<HomeBankAccBalVO> getBankAccBalGroupByCur(HomeBankAccBalDistributeSearchVO searchVO, List<HomeBankAccBalVO> listBal){
        LinkedMap<String, HomeBankAccBalVO> curMap = new LinkedMap<>();
        BigDecimal curBal = BigDecimal.ZERO;
        for (HomeBankAccBalVO vo:listBal) {
            curBal = curBal.add(vo.getBal());
            HomeBankAccBalVO tempBalVO;
            if(curMap.containsKey(vo.getCurId())){
                tempBalVO = curMap.get(vo.getCurId());
            } else {
                tempBalVO = new HomeBankAccBalVO();
                tempBalVO.setBal(BigDecimal.ZERO);
            }
            tempBalVO.setCurId(vo.getCurId());
            tempBalVO.setCurName(vo.getCurName());
            tempBalVO.setBal(tempBalVO.getBal().add(vo.getBal()));
            curMap.put(vo.getCurId(), tempBalVO);
        }
        List<HomeBankAccBalVO> listBalCur = new ArrayList<>();
        for(String key:curMap.keySet()){
            HomeBankAccBalVO vo = curMap.get(key);
            //设置余额占比
            if (curBal.compareTo(BigDecimal.ZERO) == 0) {
                vo.setBalRatio(BigDecimal.ZERO);
            } else {
                vo.setBalRatio(vo.getBal().divide(curBal, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)));
            }
            listBalCur.add(vo);
        }
        return listBalCur;
    }
    /**
     * 获取账户余额
     * @param listBal
     * @return
     */
    public HomeBankAccBalVO getBankAccBal(List<HomeBankAccBalVO> listBal){
        HomeBankAccBalVO result = new HomeBankAccBalVO();
        result.setBal(BigDecimal.ZERO);
        result.setAvailBal(BigDecimal.ZERO);
        result.setFrzBal(BigDecimal.ZERO);
        result.setStandardBal(BigDecimal.ZERO);
        result.setForeignBal(BigDecimal.ZERO);
        //获取人民币
        BtCurrencyDTO natualCurrency = currencyService.getNatualCurrency();
        if(EmptyUtils.isNotEmpty(listBal)){
            for(HomeBankAccBalVO bal:listBal){
                //余额汇总
                result.setBal(result.getBal().add(bal.getBal()));
                //可用余额汇总
                result.setAvailBal(result.getAvailBal().add(bal.getAvailBal()));
                //受限金额汇总
                result.setFrzBal(result.getFrzBal().add(bal.getFrzBal()));
                if(bal.getCurId().equals(natualCurrency.getId())){
                    //境内余额（人民币）
                    result.setStandardBal(result.getStandardBal().add(bal.getBal()));
                } else {
                    //境外余额（外币）
                    result.setForeignBal(result.getForeignBal().add(bal.getBal()));
                }
            }
        }
        return result;
    }
    /**
     * 银行账户余额趋势图
     * @param searchVO
     * @param user
     * @return
     */
    public Map<String, Object> createBankAccBalDistributeBar(HomeBankAccBalDistributeSearchVO searchVO, ICurrentUser user) {
        Map<String, Object> map = new HashMap<>();
        //x轴
        List<String> xData = new ArrayList<>();
        //y轴
        List<BigDecimal> yData = new ArrayList<>();
        if(StringUtils.isBlank(searchVO.getYear())){
            searchVO.setYear(DateUtils.getYear()+"");
        }
        if(StringUtils.isBlank(searchVO.getMonth())){
            searchVO.setMonth(DateUtils.getMonth(DateUtils.getDate())+"");
            searchVO.setMonth(searchVO.getMonth().length()<2 ? "0"+searchVO.getMonth():searchVO.getMonth());
        }
        Date beginDate = null;
        Date endDate = null;
        if("Y".equals(searchVO.getTimeType())){
            beginDate = DateUtils.parse(searchVO.getYear() + "-01-" + "01", DateUtils.PATTERN_CLASSICAL_SIMPLE);
            endDate = DateUtils.getLastYearDate(beginDate);
            Date now = DateUtils.getDate();
            Date curLastMonthDate = DateUtils.getLastMonthDate(now);
            if(endDate.compareTo(curLastMonthDate) >= 0){
                endDate = curLastMonthDate;
            }
            searchVO.setSearchBeginDate(beginDate);
            searchVO.setSearchEndDate(endDate);
            //获取余额集合
            Map<String, BigDecimal> balMap = this.getBankAccBalByBalDate(searchVO, user);
            List<DictDTO> months = dictService.list("btBankAccDtl.months");
            //按年 一个月最后一天才进行统计
            for(DictDTO dto:months){
                String month = dto.getCode().length()<2 ? "0"+dto.getCode():dto.getCode();
                Date monthBeginDate = DateUtils.parse(searchVO.getYear() + "-" + month + "-" + "01", DateUtils.PATTERN_CLASSICAL_SIMPLE);
                Date monthEndDate = DateUtils.getLastMonthDate(monthBeginDate);
                if(monthEndDate.compareTo(curLastMonthDate) <= 0){
                    xData.add(dto.getName());
                    String monthEndDateStr = DateUtils.formatDate(monthEndDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
                    yData.add(balMap.containsKey(monthEndDateStr) ? balMap.get(monthEndDateStr): BigDecimal.ZERO);
                }
            }
        } else {
            //按月
            Date curDate = DateUtils.parse(searchVO.getYear()+"-"+searchVO.getMonth()+"-"+"01", DateUtils.PATTERN_CLASSICAL_SIMPLE);
            beginDate = DateUtils.getFirstMonthDate(curDate);
            endDate = DateUtils.getLastMonthDate(curDate);
            if(endDate.compareTo(DateUtils.getDate()) > 0){
                endDate = DateUtils.getDateYMD();
            }
            searchVO.setSearchBeginDate(beginDate);
            searchVO.setSearchEndDate(endDate);
            //获取余额集合
            Map<String, BigDecimal> balMap = this.getBankAccBalByBalDate(searchVO, user);
            int days = DateUtils.dayDiffs(endDate, beginDate);
            while (beginDate.compareTo(endDate) < 0) {
                String beginDateStr = DateUtils.formatDate(beginDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
                xData.add(DateUtils.formatDate(beginDate, DateUtils.PATTERN_CH_SIMPLE).substring(8));
                yData.add(balMap.containsKey(beginDateStr) ? balMap.get(beginDateStr) : BigDecimal.ZERO);
                if (days > 15) {
                    beginDate = DateUtils.addDay(beginDate, 2);
                } else {
                    beginDate = DateUtils.addDay(beginDate, 1);
                }
                if (beginDate.compareTo(endDate) >= 0) {
                    beginDate = endDate;
                    beginDateStr = DateUtils.formatDate(beginDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
                    xData.add(DateUtils.formatDate(beginDate, DateUtils.PATTERN_CH_SIMPLE).substring(8));
                    yData.add(balMap.containsKey(beginDateStr) ? balMap.get(beginDateStr) : BigDecimal.ZERO);
                }
            }

        }
        map.put("xData", JsonUtils.toJSONString(xData));
        map.put("yData", JsonUtils.toJSONString(yData));
        return map;
    }
    /**
     * 根据日期获取账户余额
     * @param searchVO
     * @param user
     * @return
     */
    public Map<String, BigDecimal> getBankAccBalByBalDate(HomeBankAccBalDistributeSearchVO searchVO, ICurrentUser user) {
        List<String> corpIds = null;
        if(StringUtils.isNotBlank(searchVO.getCorpId())){
            corpIds = new ArrayList<>();
            for(String corpId:searchVO.getCorpId().split(CommonConstant.COMMA_SPLIT)){
                corpIds.add(corpId);
            }
        } else {
            //获取资源权限范围内的单位信息
            corpIds = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
        }
        //获取账户余额
        String corpAuthSql = " and " + SqlUtil.getInSql(corpIds, "acc.corp_Id", true);
        //权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
            corpAuthSql += "and " + SqlUtil.getInSql(accList, "acc.id", true);
        }
        List<HomeBankAccBalVO> listBal = this.bankAccBalDistributeDao.getBankAccBalByBalDate(corpAuthSql, searchVO);
        this.convertMoneyType(listBal, searchVO.getMoneyType());
        Map<String, BigDecimal> resultMap = new HashMap<>();
        for(HomeBankAccBalVO vo:listBal ){
            if(vo.getBalDate() != null) {
                String key = DateUtils.formatDate(vo.getBalDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
                if (resultMap.containsKey(key)) {
                    resultMap.put(key, resultMap.get(key).add(vo.getBal()));
                } else {
                    resultMap.put(key, vo.getBal());
                }
            }
        }
        return resultMap;
    }
    /**
     * 银行账户余额分布图
     * @param searchVO
     * @param user
     * @return
     */
    public Map<String, Object> createBankAccBalBar(HomeBankAccBalDistributeSearchVO searchVO, ICurrentUser user) {
        List<String> corpIds = null;
        if(StringUtils.isNotBlank(searchVO.getCorpId())){
            corpIds = new ArrayList<>();
            for(String corpId:searchVO.getCorpId().split(CommonConstant.COMMA_SPLIT)){
                corpIds.add(corpId);
            }
        } else {
            //获取资源权限范围内的单位信息
            corpIds = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
        }
        //获取账户余额
        String corpAuthSql = " and " + SqlUtil.getInSql(corpIds, "acc.corp_Id", true);
        //权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
            corpAuthSql += "and " + SqlUtil.getInSql(accList, "acc.id", true);
        }
        //银行账户月类别统计占比
        List<HomeBankAccBalVO> listBal = null;
        if("bankType".equals(searchVO.getGroupBy())){
            listBal = this.bankAccBalDistributeDao.getBankAccBalGroupByBankType(corpAuthSql);
        } else if("attribute".equals(searchVO.getGroupBy())){
            listBal = this.bankAccBalDistributeDao.getBankAccBalGroupByAttribute(corpAuthSql);
        } else if("nature".equals(searchVO.getGroupBy())){
            listBal = this.bankAccBalDistributeDao.getBankAccBalGroupByNature(corpAuthSql);
        }
        setCommonField(listBal);
        this.convertMoneyType(listBal, searchVO.getMoneyType());
        Map<String, Object> map = new HashMap<>();
        List<String> names = new ArrayList<>();
        List<HomeBankAccBalChartVO> bals = new ArrayList<>();
        for (HomeBankAccBalVO vo:listBal) {
            HomeBankAccBalChartVO chart = new HomeBankAccBalChartVO();
            if("bankType".equals(searchVO.getGroupBy())) {
                if(StringUtils.isBlank(vo.getBankTypeName())){
                    continue;
                }
                chart.setName(vo.getBankTypeName());
            } else if("attribute".equals(searchVO.getGroupBy())){
                if(StringUtils.isBlank(vo.getAttributeName())){
                    continue;
                }
                chart.setName(vo.getAttributeName());
            } else if("nature".equals(searchVO.getGroupBy())){
                if(StringUtils.isBlank(vo.getNatureName())){
                    continue;
                }
                chart.setName(vo.getNatureName());
            }
            names.add(chart.getName());
            chart.setValue(vo.getBal());
            bals.add(chart);
        }
        map.put("bals", JsonUtils.toJSONString(bals));
        map.put("names", JsonUtils.toJSONString(names));
        return map;
    }
}
