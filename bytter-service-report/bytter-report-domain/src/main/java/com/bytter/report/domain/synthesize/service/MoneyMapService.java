package com.bytter.report.domain.synthesize.service;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.report.domain.synthesize.constants.MoneyMapConstant;
import com.bytter.report.domain.synthesize.dao.MoneyMapDao;
import com.bytter.report.domain.synthesize.enums.ContinentEnum;
import com.bytter.report.domain.synthesize.vo.CountryVO;
import com.bytter.report.domain.synthesize.vo.MoneyMapQueryVO;
import com.bytter.report.domain.synthesize.vo.MoneyMapVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 资金地图Service
 *
 * @author zuoy
 * @since 2020-01-16
 */
@Service
public class MoneyMapService {
    @Autowired
    private MoneyMapDao moneyMapDao;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IRegionService regionService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService excRateAdjustService;
    @DubboReference(check = false)
	private ICountryService countryService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
	private IDictService dictService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IBtAccNatureService accNatureService;
    @DubboReference(check = false)
    private IBisAccAttributeService accAttributeService;
    @DubboReference
    private IBtCurrencyService iBtCurrencyService;

    /*** 资金地图查询权限*/
    public static final String BI_SYNTHESIZE_MONEY_MAP_QUERY = "bi:synthesize:moneyMap:query";
    /**
     * 获取资金地图（世界）的组装数据
     *
     * @return
     */
    public Object getWorldMapData(MoneyMapQueryVO moneyMapQueryVO, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(BI_SYNTHESIZE_MONEY_MAP_QUERY, user.getId());
        String moneyUint = moneyMapQueryVO.getMoneyUnit();
        Map<String, Object> moneyMapWorld = Maps.newHashMap();
        // 银行账号余额集合
        List<CountryVO> bankAccBalanceList = getBankAccBalanceByContinent(corpIds);
        // 内部账号余额
//        List<CountryVO> innerAccBalanceList = moneyMapDao.listInnerAccCountryBalance(corpIds);
        List<CountryVO> innerAccBalanceList = new ArrayList<>();
        List<Object> dataList = new ArrayList<>();
        Map<String, Object> zeroMap = getZeroMap();
        for (ContinentEnum continentEnum : ContinentEnum.values()) {
            Map<String, Object> map = Maps.newHashMap();
            // 大洲
            map.put("name", continentEnum.getLangKey());
            map.put("value", continentEnum.getValue());
            // 余额信息
            List<Object> balanceList = new ArrayList<>();
            // 设置银行账号余额
            StringBuilder currency = new StringBuilder();
            Map<String, Map<String, String>> continentMap = Maps.newHashMap();
            for (CountryVO vo : bankAccBalanceList) {
                if (continentEnum.getValue().toString().equals(vo.getContinent())) {
                    Map<String, Object> balanceMap = Maps.newHashMap();
                    balanceMap.put("name", vo.getCurName());
                    if (continentEnum.getValue().intValue() == 5) {
                        Boolean flag = true;
                        // 中国 余额为银行账号余额+内部账号余额
                        for (CountryVO innerCountry : innerAccBalanceList) {
                            if (innerCountry.getCurId().equalsIgnoreCase(vo.getCurId())) {
                                BigDecimal bal = vo.getBalance().add(innerCountry.getBalance());
                                balanceMap.put("value", currencyService.getCurSign(vo.getCurId()) + " " + handleWorldBalance(bal, moneyUint, true));
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
//                            balanceMap.put("value", currencyService.getCurSign(vo.getCurId()) + " " + handleWorldBalance(vo.getBalance(), moneyUint, true));
                            if (continentMap.containsKey(continentEnum.getValue().toString())) {
                                Map<String, String> curIdMap = continentMap.get(continentEnum.getValue().toString());
                                if (curIdMap.containsKey(vo.getCurId())) {
                                    BigDecimal oldMoney = new BigDecimal(curIdMap.get(vo.getCurId()));
                                    String newMoney = handleWorldBalance(vo.getBalance(), moneyUint, false);
                                    BigDecimal totalMoney = new BigDecimal(newMoney);
                                    curIdMap.put(vo.getCurId(), oldMoney.add(totalMoney).toString());
                                    continentMap.put(continentEnum.getValue().toString(), curIdMap);
                                } else {
                                    String newMoney = handleWorldBalance(vo.getBalance(), moneyUint, false);
                                    curIdMap.put(vo.getCurId(), newMoney);
                                    continentMap.put(continentEnum.getValue().toString(), curIdMap);
                                }
                            } else {
                                Map<String, String> curIdMap = Maps.newHashMap();
                                curIdMap.put(vo.getCurId(), handleWorldBalance(vo.getBalance(), moneyUint, false));
                                continentMap.put(continentEnum.getValue().toString(), curIdMap);
                            }
                        }
                    } else {
//                        balanceMap.put("value", currencyService.getCurSign(vo.getCurId()) + " " + handleWorldBalance(vo.getBalance(), moneyUint, true));
                    }
//                    balanceList.add(balanceMap);
                    currency.append(vo.getCurId()).append(CommonConstant.COMMA_SPLIT);
                }
            }
            Set<Map.Entry<String, Map<String, String>>> mapList = continentMap.entrySet();
            if (mapList.size() > 0) {
                for (Map.Entry<String, Map<String, String>> item : continentMap.entrySet()) {
                    Map<String, String> value = item.getValue();
                    for (Map.Entry<String, String> entity : value.entrySet()) {
                        Map<String, Object> balanceMap = Maps.newHashMap();
                        if (!entity.getKey().equals("null")){
                            BtCurrencyDTO currencyDto = currencyService.getObjectById(entity.getKey());
                            balanceMap.put("value", currencyDto.getCurSign() + " " + handleWorldBalance(new BigDecimal(entity.getValue()), moneyUint, true));
                            balanceMap.put("name", currencyDto.getCurName());
                            balanceList.add(balanceMap);
                        }
                    }
                }
            }

            // 中国，有内部账号余额，但内部账号余额币别数量大于银行账号余额数量，补齐币别余额
            if (continentEnum.getValue().intValue() == 5 && !CollectionUtils.isEmpty(innerAccBalanceList)
                    && balanceList.size() < innerAccBalanceList.size()) {
                for (CountryVO innerCountry : innerAccBalanceList) {
                    if (!currency.toString().contains(innerCountry.getCurId())) {
                        Map<String, Object> balanceMap = Maps.newHashMap();
                        BtCurrencyDTO currencyDTO = currencyService.getObjectById(innerCountry.getCurId());
                        balanceMap.put("name", currencyDTO.getCurName());
                        balanceMap.put("value", currencyService.getCurSign(currencyDTO.getId()) + " " + handleWorldBalance(innerCountry.getBalance(), moneyUint, true));
                        balanceList.add(balanceMap);
                    }
                }
            }
            // 中国，无银行账号余额，有内部账号余额
            if (continentEnum.getValue().intValue() == 5 && CollectionUtils.isEmpty(balanceList)
                    && !CollectionUtils.isEmpty(innerAccBalanceList)) {
                Map<String, Object> balanceMap = Maps.newHashMap();
                for (CountryVO innerCountry : innerAccBalanceList) {
                    BtCurrencyDTO currencyDTO = currencyService.getObjectById(innerCountry.getCurId());
                    balanceMap.put("name", currencyDTO.getCurName());
                    balanceMap.put("value", currencyService.getCurSign(currencyDTO.getId()) + " " + handleWorldBalance(innerCountry.getBalance(), moneyUint, true));
                }
                balanceList.add(balanceMap);
            }
            // 无余额时，默认为0
            if (CollectionUtils.isEmpty(balanceList)) {
                balanceList.add(zeroMap);
            }
            map.put("data", balanceList);
            dataList.add(map);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            moneyMapWorld.put("data", mapper.writeValueAsString(dataList));
            return moneyMapWorld;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public List<CountryVO> getBankAccBalanceByContinent(List<String> corpIds) {
        List<CountryVO> bankAccBalanceList = moneyMapDao.listBankAccBalanceByChina(corpIds);
        List<String> curIdList = bankAccBalanceList.stream().map(CountryVO::getCurId).collect(Collectors.toList());
        Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrencyByIds(curIdList);
        bankAccBalanceList.forEach(item ->
                Optional.ofNullable(currencyMap.get(item.getCurId()))
                        .ifPresent(currency -> item.setCurName(currency.getCurName())));
        return bankAccBalanceList;
    }

    /**
     * 处理金额
     *
     * @param balance
     * @param moneyUnit
     * @return
     */
    private String handleWorldBalance(BigDecimal balance, String moneyUnit, Boolean format) {
        BigDecimal dividend;
        switch (moneyUnit.toLowerCase()) {
            case "thousand":
                dividend = new BigDecimal(1000);
                break;
            case "tenthousand":
                dividend = new BigDecimal(10000);
                break;
            case "billion":
                dividend = new BigDecimal(100000000);
                break;
            default:
                dividend = BigDecimal.ONE;
        }
        BigDecimal bal = balance.divide(dividend);
        return format ? NumberUtil.formatNumeric(bal) : bal.toPlainString();
    }
    /**
     * 获取资金地图（中国）的组装数据
     *
     * @return
     */
    public Object getChinaMapData(MoneyMapQueryVO moneyMapQueryVO, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(BI_SYNTHESIZE_MONEY_MAP_QUERY, user.getId());
        String moneyUint = moneyMapQueryVO.getMoneyUnit();
        Map<String, Object> moneyMapChina = Maps.newHashMap();
        // 银行账号余额集合
        List<CountryVO> bankAccBalanceList = getBankAccBalanceByContinent(corpIds);
        // 内部账号余额
        List<CountryVO> innerAccBalanceList = moneyMapDao.listInnerAccCountryBalanceByChina(corpIds);
        List<Object> dataList = new ArrayList<>();
        Map<String, Object> zeroMap = getZeroMap();
        List<RegionDTO> provList = regionService.listProvAll();
        for (RegionDTO regionDTO : provList) {
            Map<String, Object> map = Maps.newHashMap();
            // 省份
            String provName = regionDTO.getName().replaceAll("市", "").replaceAll("省", "").replaceAll("自治区", "");
            if (provName.contains("新疆") || provName.contains("广西") || provName.contains("宁夏") || provName.contains("行政区")) {
                provName = provName.substring(0, 2);
            }
            map.put("name", provName);
            // 余额信息
            List<Object> balanceList = new ArrayList<>();
            // 设置银行账号余额
            StringBuilder currency = new StringBuilder();
            for (CountryVO vo : bankAccBalanceList) {
                if (regionDTO.getId().equals(vo.getProv())) {
                    Map<String, Object> balanceMap = Maps.newHashMap();
                    balanceMap.put("name", vo.getCurName());
                    // 中国 余额为银行账号余额+内部账号余额
                    if ("广东".equalsIgnoreCase(provName)) {
                        Boolean flag = true;
                        for (CountryVO innerCountry : innerAccBalanceList) {
                            if (innerCountry.getCurId().equalsIgnoreCase(vo.getCurId())) {
                                BigDecimal bal = vo.getBalance().add(innerCountry.getBalance());
                                balanceMap.put("value", currencyService.getCurSign(vo.getCurId()) + " " + handleWorldBalance(bal, moneyUint, true));
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            balanceMap.put("value", currencyService.getCurSign(vo.getCurId()) + " " + handleWorldBalance(vo.getBalance(), moneyUint, true));
                        }
                    } else {
                        balanceMap.put("value", currencyService.getCurSign(vo.getCurId()) + " " + handleWorldBalance(vo.getBalance(), moneyUint, true));
                    }
                    balanceList.add(balanceMap);
                    currency.append(vo.getCurId()).append(CommonConstant.COMMA_SPLIT);
                }
            }
            // 中国，有内部账号余额，但内部账号余额币别数量大于银行账号余额数量，补齐币别余额
            if ("广东".equalsIgnoreCase(provName) && !CollectionUtils.isEmpty(innerAccBalanceList) && balanceList.size() < innerAccBalanceList.size()) {
                for (CountryVO innerCountry : innerAccBalanceList) {
                    if (!currency.toString().contains(innerCountry.getCurId())) {
                        Map<String, Object> balanceMap = Maps.newHashMap();
                        BtCurrencyDTO currencyDTO = currencyService.getObjectById(innerCountry.getCurId());
                        balanceMap.put("name", currencyDTO.getCurName());
                        balanceMap.put("value", currencyService.getCurSign(currencyDTO.getId()) + " " + handleWorldBalance(innerCountry.getBalance(), moneyUint, true));
                        balanceList.add(balanceMap);
                    }
                }
            }
            // 中国，无银行账号余额，有内部账号余额
            if ("广东".equalsIgnoreCase(provName) && CollectionUtils.isEmpty(balanceList) && !CollectionUtils.isEmpty(innerAccBalanceList)) {
                Map<String, Object> balanceMap = Maps.newHashMap();
                for (CountryVO innerCountry : innerAccBalanceList) {
                    BtCurrencyDTO currencyDTO = currencyService.getObjectById(innerCountry.getCurId());
                    balanceMap.put("name", currencyDTO.getCurName());
                    balanceMap.put("value", currencyService.getCurSign(currencyDTO.getId()) + " " + handleWorldBalance(innerCountry.getBalance(), moneyUint, true));
                }
                balanceList.add(balanceMap);
            }
            // 无余额时，默认为0
            if (CollectionUtils.isEmpty(balanceList)) {
                balanceList.add(zeroMap);
            }
            map.put("data", balanceList);
            dataList.add(map);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            moneyMapChina.put("data", mapper.writeValueAsString(dataList));
            return moneyMapChina;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }
    /**
     * 获取查询列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(BI_SYNTHESIZE_MONEY_MAP_QUERY, user.getId());
        String reducedCurId = (String) dataTablesInput.getFormdata().get("reducedCurId");
        String statisticalDimension = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("statisticalDimension")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("statisticalDimension"));
        String balanceDate = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("balanceDate")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("balanceDate"));
        DataTablesOutput dataTablesOutput = null;
        // 按表格展示
        if (MoneyMapConstant.STATISTICAL_DIMENSION_CORP.equalsIgnoreCase(statisticalDimension)) {
            // 按单位统计
            dataTablesOutput = this.moneyMapDao.listBankAccBalanceByCorp(dataTablesInput, corpIds);
        } else if (MoneyMapConstant.STATISTICAL_DIMENSION_BANK.equalsIgnoreCase(statisticalDimension)) {
            // 按银行统计
            dataTablesOutput = this.moneyMapDao.listBankAccBalanceByBank(dataTablesInput, corpIds);
        }
        if (dataTablesOutput != null && !CollectionUtils.isEmpty(dataTablesOutput.getData())) {
            List<MoneyMapVO> list = dataTablesOutput.getData();
            List<String> regionIds = list.stream().map(moneyMapVO -> moneyMapVO.getProv()).distinct().collect(Collectors.toList());
            List<String> attributeIds = list.stream().map(moneyMapVO -> moneyMapVO.getAttributeId()).distinct().collect(Collectors.toList());
            List<String> natureIds = list.stream().map(moneyMapVO -> moneyMapVO.getNatureId()).distinct().collect(Collectors.toList());
            Map<String, CorpDTO> mapCorp = corpService.getCorpMap();//单位
            Map<String, BtCurrencyDTO> mapCur = currencyService.getCurrency();//币别
            Map<String, RegionDTO> mapPro = regionService.listByIds(regionIds);//省
            Map<String,BtAccNatureDTO> mapNature = accNatureService.getValidAccNatureMap();
            Map<String, BisAccAttributeDTO> mapAttribute = accAttributeService.getValidAttributeMap();
            Map<String, DictDTO> mapYue = dictService.listMap("bi.money.map.balance.type");//余额类型
            for (MoneyMapVO vo : list) {
                // 币别转换
                BigDecimal curExrate = new BigDecimal(1);
                BigDecimal exchangeRateBy = excRateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), reducedCurId, DateUtils.setSpecifiedDateOfTime(DateUtils.parseDate(balanceDate)), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (exchangeRateBy != null) {
                    curExrate = exchangeRateBy;
                } else {
                    //vo.setAttributeId("1");//这里设置1是什么意思？//TODO
                }
                vo.setBalanceRmb(vo.getBalance().multiply(curExrate));
                if (MoneyMapConstant.BALANCE_TYPE_INNER_ACC.equalsIgnoreCase(vo.getBalanceType())) {
                    // 内部账号-账户类别 InnerAccount.AccType
                    String accTypeName = dictService.get("InnerAccount.AccType", vo.getAccType());
                    vo.setAccTypeName(accTypeName);
                } else {
                    // 银行账号-账户类别 btBankAcc.accType
                    String accTypeName = dictService.get("btBankAcc.accType", vo.getAccType());
                    vo.setAccTypeName(accTypeName);
                }
                vo.setProvName(!ObjectUtils.isEmpty(mapPro.get(vo.getProv())) ? mapPro.get(vo.getProv()).getName() : "");
                vo.setBalanceTypeName(!ObjectUtils.isEmpty(mapYue.get(vo.getBalanceType()).getName()) ? mapYue.get(vo.getBalanceType()).getName() : "");
                vo.setCorpName(!ObjectUtils.isEmpty(mapCorp.get(vo.getCorpId())) ? mapCorp.get(vo.getCorpId()).getName() : "");
                vo.setCurName(!ObjectUtils.isEmpty(mapCur.get(vo.getCurId())) ? mapCur.get(vo.getCurId()).getCurName() : "");
                vo.setNatureName(!ObjectUtils.isEmpty(mapNature.get(vo.getNatureId())) ? mapNature.get(vo.getNatureId()).getNatureName() : "");
                vo.setAttributeName(!ObjectUtils.isEmpty(mapAttribute.get(vo.getAttributeId())) ? mapAttribute.get(vo.getAttributeId()).getAttributeName() : "");
            }
        }
        return dataTablesOutput;
    }
    /**
     * 获取省份柱状图（市）的组装数据
     *
     * @return
     */
    public Object getProvinceMapData(MoneyMapQueryVO moneyMapQueryVO, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(BI_SYNTHESIZE_MONEY_MAP_QUERY, user.getId());
        String moneyUint = moneyMapQueryVO.getMoneyUnit();
        RegionDTO prov = regionService.getRegionByLikeProvName(moneyMapQueryVO.getProvName());
        // 银行账号余额集合
        List<CountryVO> bankAccBalanceList = moneyMapDao.listBankAccBalanceByProvince(prov.getId(), corpIds);
        List<RegionDTO> cityList = regionService.listCityByParentId(prov.getId());
        List<CountryVO> innerAccBalanceList = new ArrayList<>();
        if (prov.getName().contains("广东")) {
            // 内部账号余额
            innerAccBalanceList = moneyMapDao.listInnerAccCountryBalanceByChina(corpIds);
        }
        StringBuilder currency = new StringBuilder();
        List<Object> dataList = new ArrayList<>();
        for (RegionDTO city : cityList) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("name", city.getName());
            BigDecimal sumBalance = BigDecimal.ZERO;
            List<Object> capitalList = new ArrayList<>();
            for (CountryVO vo : bankAccBalanceList) {
                if (city.getId().equalsIgnoreCase(vo.getCity())) {
                    Map<String, Object> values = Maps.newHashMap();
                    BigDecimal balance = BigDecimal.ZERO;
                    BigDecimal balanceRmb = BigDecimal.ZERO;
                    BtCurrencyDTO curDto = iBtCurrencyService.getObjectById(vo.getCurId());
                    values.put("currency", curDto.getCurName());
                    // 中国 余额为银行账号余额+内部账号余额
                    if (city.getName().contains("深圳")) {
                        Boolean flag = true;
                        for (CountryVO innerCountry : innerAccBalanceList) {
                            if (innerCountry.getCurId().equalsIgnoreCase(vo.getCurId())) {
                                balance = vo.getBalance().add(innerCountry.getBalance());
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            balance = vo.getBalance();
                        }
                    } else {
                        balance = vo.getBalance();
                    }
                    // 非人民币根据汇率转换为人民币
                    BigDecimal exchangeRate = this.excRateAdjustService.getExchangeRateBy(vo.getCurId(), DateUtils.parseDate(DateUtils.getDate()));
                    if (!EmptyUtils.isEmpty(exchangeRate)) {
                        balanceRmb = balance.multiply(exchangeRate);
                    } else {
                        balanceRmb = balance;
                    }
                    values.put("value_currency", handleWorldBalance(balance, moneyUint, false));
                    values.put("value_CNY", handleWorldBalance(balanceRmb, moneyUint, false));
                    sumBalance = sumBalance.add(balanceRmb);
                    capitalList.add(values);
                    currency.append(vo.getCurId()).append(CommonConstant.COMMA_SPLIT);
                }
            }
            // 深圳，有内部账号余额，但内部账号余额币别数量大于银行账号余额数量，补齐币别余额
            if (city.getName().contains("深圳") && !CollectionUtils.isEmpty(innerAccBalanceList) && capitalList.size() < innerAccBalanceList.size()) {
                for (CountryVO innerCountry : innerAccBalanceList) {
                    if (!currency.toString().contains(innerCountry.getCurId())) {
                        if (innerCountry.getBalance().equals(BigDecimal.ZERO)) {
                            continue;
                        }
                        Map<String, Object> values = Maps.newHashMap();
                        BigDecimal balanceRmb = BigDecimal.ZERO;
                        BtCurrencyDTO curDto = iBtCurrencyService.getObjectById(innerCountry.getCurId());
                        values.put("currency", curDto.getCurName());
                        // 非人民币根据汇率转换为人民币
                        BigDecimal exchangeRate = this.excRateAdjustService.getExchangeRateBy(innerCountry.getCurId(), DateUtils.parseDate(DateUtils.getDate()));
                        if (!EmptyUtils.isEmpty(exchangeRate)) {
                            balanceRmb = innerCountry.getBalance().multiply(exchangeRate);
                        } else {
                            balanceRmb = innerCountry.getBalance();
                        }
                        values.put("value_currency", handleWorldBalance(innerCountry.getBalance(), moneyUint, false));
                        values.put("value_CNY", handleWorldBalance(balanceRmb, moneyUint, false));
                        sumBalance = sumBalance.add(balanceRmb);
                        capitalList.add(values);
                    }
                }
            }
            // 深圳，无银行账号余额，有内部账号余额
            if (city.getName().contains("深圳") && CollectionUtils.isEmpty(capitalList) && !CollectionUtils.isEmpty(innerAccBalanceList)) {
                for (CountryVO innerCountry : innerAccBalanceList) {
                    if (innerCountry.getBalance().equals(BigDecimal.ZERO)) {
                        continue;
                    }
                    Map<String, Object> values = Maps.newHashMap();
                    BigDecimal balanceRmb = BigDecimal.ZERO;
                    BtCurrencyDTO curDto = iBtCurrencyService.getObjectById(innerCountry.getCurId());
                    values.put("currency", curDto.getCurName());
                    // 非人民币根据汇率转换为人民币
                    BigDecimal exchangeRate = this.excRateAdjustService.getExchangeRateBy(innerCountry.getCurId(), DateUtils.parseDate(DateUtils.getDate()));
                    if (!EmptyUtils.isEmpty(exchangeRate)) {
                        balanceRmb = innerCountry.getBalance().multiply(exchangeRate);
                    } else {
                        balanceRmb = innerCountry.getBalance();
                    }
                    values.put("value_currency", handleWorldBalance(innerCountry.getBalance(), moneyUint, false));
                    values.put("value_CNY", handleWorldBalance(balanceRmb, moneyUint, false));
                    sumBalance = sumBalance.add(balanceRmb);
                    capitalList.add(values);
                }
            }
            map.put("capital", capitalList);
            map.put("value", handleWorldBalance(sumBalance, moneyUint, false));
            if (!sumBalance.equals(BigDecimal.ZERO)) {
                dataList.add(map);
            }
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }
    /**
     * 获取余额为0的集合
     *
     * @return
     */
    private Map<String, Object> getZeroMap() {
        Map<String, Object> zeroMap = Maps.newHashMap();
        zeroMap.put("name", multiLangServiceI.getLang("bi.money.map.balance.zero"));
        zeroMap.put("value", NumberUtil.formatNumeric(BigDecimal.ZERO));
        return zeroMap;
    }
    /**
     * 获取资金地图（大洲）的组装数据
     *
     * @return
     */
    public Object getContinentCountryMapData(MoneyMapQueryVO moneyMapQueryVO, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(BI_SYNTHESIZE_MONEY_MAP_QUERY, user.getId());
        String moneyUint = moneyMapQueryVO.getMoneyUnit();
        String countryNameLanguage = StringUtils.hasText(moneyMapQueryVO.getCountryNameLanguage()) ? moneyMapQueryVO.getCountryNameLanguage() : MoneyMapConstant.COUNTRY_LANGUAGE_EN;
        String continent = moneyMapQueryVO.getContinent();
        Map<String, Object> moneyMapContinentCountry = Maps.newHashMap();
        // 银行账号余额集合
        List<CountryVO> bankAccBalanceList = moneyMapDao.listBankAccBalanceByContinentCountry(continent, countryNameLanguage, corpIds);
        List<Object> dataList = new ArrayList<>();
        List<SysCountryDTO> countryList = countryService.listByContinent(continent);
        Map<String, Object> zeroMap = getZeroMap();
        int count = 0;
        for (SysCountryDTO country : countryList) {
            count++;
            Map<String, Object> map = Maps.newHashMap();
            String name = MoneyMapConstant.COUNTRY_LANGUAGE_EN.equalsIgnoreCase(countryNameLanguage) ? country.getNameEn() : country.getNameZh();
            // 大洲
            map.put("name", name);
            map.put("name_en", country.getNameEn());
            map.put("name_zh", country.getNameZh());
            // 余额信息
            List<Object> balanceList = new ArrayList<>();
            // 设置银行账号余额
            BigDecimal totalBalance = BigDecimal.ZERO;
            for (CountryVO vo : bankAccBalanceList) {
                if (name.equals(vo.getCountryName())) {
                    Map<String, Object> balanceMap = Maps.newHashMap();
                    balanceMap.put("name", vo.getCurName());
                    balanceMap.put("value", currencyService.getCurSign(vo.getCurId()) + " " + handleWorldBalance(vo.getBalance(), moneyUint, true));
                    balanceList.add(balanceMap);
                    totalBalance = totalBalance.add(vo.getBalance());
                }
            }
            // 无余额时，默认为0
            if (CollectionUtils.isEmpty(balanceList)) {
                balanceList.add(zeroMap);
                totalBalance = new BigDecimal(count);
            }
            map.put("data", balanceList);
            map.put("value", totalBalance);
            dataList.add(map);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            moneyMapContinentCountry.put("data", mapper.writeValueAsString(dataList));
            return moneyMapContinentCountry;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }
}
