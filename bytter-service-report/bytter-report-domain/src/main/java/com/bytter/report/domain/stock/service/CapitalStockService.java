package com.bytter.report.domain.stock.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.api.constant.CapitalStockConstant;
import com.bytter.report.domain.stock.dao.CapitalStockDao;
import com.bytter.report.domain.stock.vo.*;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资金存量报表Service
 *
 * @author zuoy 2017-10-25
 */
@Service("capitalStockService")
@Transactional
public class CapitalStockService extends BaseService<CapitalStockVO, String> {
    @Autowired
    private CapitalStockDao capitalStockDao;
    @Autowired
    private AbBillCapitalStockService abBillCapitalStockService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private IDictService dictService;
    @Override
    public BaseDao<CapitalStockVO, String> getEntityDao() {
        return capitalStockDao;
    }
    /**
     * 获取查询列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        String dateType = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("dateType")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("dateType"));
        String stockType = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("stockType")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("stockType"));
        String curId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("curId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("curId"));
        String reportType = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("reportType")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("reportType"));
        String corpId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("corpId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("corpId"));
        String reducedCurId = (String) dataTablesInput.getFormdata().get("reducedCurId");//折合币种
        String netId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("netId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("netId"));
        String resource = ResourceConstant.BI_STOCK_CAPITALSTOCK_QUERY;
        //获取有权限的单位
        List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
        if(EmptyUtils.isEmpty(corpList)){
            return new DataTablesOutput();
        }
        String corpSql = SqlUtil.getInSql(corpList, "c.id", true);
        String billCorpSql = SqlUtil.getInSql(corpList, "sc.id", true);
        if(EmptyUtils.isBlank(curId)){
			BtCurrencyDTO btCurrencyDTO = btCurrencyService.getRMBCurrency();
			if(!EmptyUtils.isEmpty(btCurrencyDTO) ){
				curId = btCurrencyDTO.getId();
			}
		}
        // 获取票据SQL
        String billSql = "";
        boolean billFlag = (CapitalStockConstant.REPORT_TYPE_NET.equalsIgnoreCase(reportType)
                || CapitalStockConstant.REPORT_TYPE_CORP.equalsIgnoreCase(reportType)
                || CapitalStockConstant.REPORT_TYPE_BANK_ACC.equalsIgnoreCase(reportType));
        boolean stockTypeFlag = (StringUtils.isBlank(stockType) || CapitalStockConstant.STOCK_TYPE_BILL.equalsIgnoreCase(stockType));
        if (billFlag && stockTypeFlag) {
            // 票据只有人民币币别
            if (!EmptyUtils.isBlank(curId)) {
                BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(curId);
                if (!EmptyUtils.isEmpty(btCurrencyDTO) && CapitalStockConstant.CUR_TYPE_RMB.equalsIgnoreCase(btCurrencyDTO.getCurName())) {
                    billSql = abBillCapitalStockService.getQueryAbBillSql(reportType,billCorpSql,netId);
                }else{
                    return new DataTablesOutput();
                }
            } else {
                billSql = abBillCapitalStockService.getQueryAbBillSql(reportType,billCorpSql,netId);
            }
        }
		Date beginDate = DateUtils.parse(dataTablesInput.getFormdata().get("beginDate").toString(),"yyyy-MM-dd");
		Date balDate = DateUtils.parseDate(DateUtils.addDay(beginDate, -1));
		String beginDateStr = DateUtils.formatDate(beginDate) + " 00:00:00";
		Date endDate = DateUtils.parse(dataTablesInput.getFormdata().get("endDate").toString(),"yyyy-MM-dd");
		beginDate = DateUtils.parse(beginDateStr);
		String endDateStr = DateUtils.formatDate(endDate) + " 23:59:59";
		endDate = DateUtils.parse(endDateStr);
        if (CapitalStockConstant.REPORT_TYPE_NET.equalsIgnoreCase(reportType)) {
            DataTablesOutput netDataTables = this.capitalStockDao.getNetDataTables(dataTablesInput, billSql,netId, beginDate, endDate, curId, stockType, balDate,corpSql);
            this.calculateExchangeRate(dataTablesInput,netDataTables);
            return netDataTables;
        } else if (CapitalStockConstant.REPORT_TYPE_CORP.equalsIgnoreCase(reportType)) {
            DataTablesOutput corpDataTables = this.capitalStockDao.getCorpDataTables(dataTablesInput, billSql, netId, beginDate, endDate, curId, stockType, balDate,corpSql);
            this.calculateExchangeRate(dataTablesInput,corpDataTables);
            return corpDataTables;
        } else if (CapitalStockConstant.REPORT_TYPE_BANK_ACC.equalsIgnoreCase(reportType)) {
            DataTablesOutput bankTypeDataTables = this.capitalStockDao.getBankTypeDataTables(dataTablesInput, billSql, corpId, beginDate, endDate, curId, stockType, balDate);
            this.calculateExchangeRate(dataTablesInput,bankTypeDataTables);
            return bankTypeDataTables;
        } else if (CapitalStockConstant.REPORT_TYPE_DTL.equalsIgnoreCase(reportType)) {
            String bankTypeId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("bankTypeId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("bankTypeId"));
            DataTablesOutput dtlDataTables = this.capitalStockDao.getDtlDataTables(dataTablesInput, bankTypeId, beginDate, endDate, curId, corpId);
            List<BisAccDtlVO> data = dtlDataTables.getData();
            for(BisAccDtlVO vo:data){
                String corpName=corpService.getCorpName(vo.getCorpId());
                vo.setCorpName(corpName);
                BtCurrencyDTO currency = btCurrencyService.getObjectById(vo.getCurId());
                vo.setCurName(currency.getCurName());
            }
            if(StringUtils.isNotBlank(reducedCurId)){
                //折合币种换算
                BigDecimal curExrate = new BigDecimal(1);
                BigDecimal exchangeRateBy = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, reducedCurId, endDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (exchangeRateBy != null) {
                    curExrate = exchangeRateBy;
                }
                for(BisAccDtlVO vo:data){
                    if(vo.getBal()!=null){
                        vo.setBal(vo.getBal().multiply(curExrate));
                    }
                    if(vo.getAmt()!=null){
                        vo.setAmt(vo.getAmt().multiply(curExrate));
                    }
                }
            }
            return dtlDataTables;
        } else if (CapitalStockConstant.REPORT_TYPE_FREEZE.equalsIgnoreCase(reportType)) {
            String bankTypeId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("bankTypeId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("bankTypeId"));
            DataTablesOutput freezeDataTables = this.capitalStockDao.getFreezeDataTables(dataTablesInput, bankTypeId, beginDate, endDate, curId, corpId);
            if(StringUtils.isNotBlank(reducedCurId)){
                List<BtBankAccFreezeVO> data = freezeDataTables.getData();
                //折合币种换算
                BigDecimal curExrate = new BigDecimal(1);
                BigDecimal exchangeRateBy = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, reducedCurId, endDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (exchangeRateBy != null) {
                    curExrate = exchangeRateBy;
                }
                for(BtBankAccFreezeVO vo:data){
                    if(vo.getFreezeMoney()!=null){
                        vo.setFreezeMoney(vo.getFreezeMoney().multiply(curExrate));
                    }
                }
            }
            return freezeDataTables;
        } else if (CapitalStockConstant.REPORT_TYPE_ABBILLDTL.equalsIgnoreCase(reportType)
                || CapitalStockConstant.REPORT_TYPE_ABBILLFREEZE.equalsIgnoreCase(reportType)) {
            //应收票据明细||应收票据占用明细
            String bankTypeId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("bankTypeId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("bankTypeId"));
            DataTablesOutput<AbBillVO> dataTablesUnion = abBillCapitalStockService.getDataTablesUnion(dataTablesInput, bankTypeId, beginDate, endDate, reportType, corpId);
//            String busTypeName=dictService.getByName("AB_BILL_BUS_TYPE",vo.getBusType());
//            vo.setBusTypeName(busTypeName);
//            String sourceName=dictService.getByName("AB_BILL_SOURCE",vo.getSource());
//            vo.setSourceName(sourceName);
//            String billFlagName=dictService.getByName("AB_BILL_FLAG", String.valueOf(vo.getBillFlag()));
//            vo.setBusTypeName(billFlagName);
//            String billTypeName=dictService.getByName("AB_BILL_TYPE", String.valueOf(vo.getBillType()));
//            vo.setBusTypeName(billTypeName);
            if (ObjectUtil.isNotEmpty(dataTablesUnion.getData())) {
                Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_BUSSTATUS","AB_BILL_SOURCE_QUERY","AB_BILL_FLAG","AB_BILL_TYPE","AB_BILL_BUS_TYPE");
                for (AbBillVO bill : dataTablesUnion.getData()) {
                    injectValues(bill, listDictMap);
                }
            }
            if(StringUtils.isNotBlank(reducedCurId)){
                List<AbBillVO> data = dataTablesUnion.getData();
                //折合币种换算
                BigDecimal curExrate = new BigDecimal(1);
                BigDecimal exchangeRateBy = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, reducedCurId, endDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (exchangeRateBy != null) {
                    curExrate = exchangeRateBy;
                }
                for(AbBillVO vo:data){
                    if(vo.getBillMoney()!=null){
                        vo.setBillMoney(vo.getBillMoney().multiply(curExrate));
                    }
                    if(vo.getInterest()!=null){
                        vo.setInterest(vo.getBillMoney().multiply(curExrate));
                    }
                }
            }
            return dataTablesUnion;
        }
        return new DataTablesOutput();
    }
    private void injectValues(AbBillVO bill,Map<String, Map<String, DictDTO>> listDictMap){
        // 票据类型
        Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
                    .ifPresent(dict -> bill.setBillTypeName(dict.getName()));
        });
        //流通介质
        Optional.ofNullable(bill.getSource()).ifPresent(source -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SOURCE_QUERY");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(source)))
                    .ifPresent(dict -> bill.setSourceName(dict.getName()));
        });
        //票据标志
        Optional.ofNullable(bill.getBillFlag()).ifPresent(billflag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billflag+"")))
                    .ifPresent(dict -> bill.setBillFlagName(dict.getName()));
        });
        //业务状态
        Optional.ofNullable(bill.getBusStatus()).ifPresent(busStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUSSTATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(busStatus+"")))
                    .ifPresent(dict -> bill.setBusStatusName(dict.getName()));
        });
        //业务类型
        Optional.ofNullable(bill.getBusType()).ifPresent(bustype -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUS_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(bustype)))
                    .ifPresent(dict -> bill.setBusTypeName(dict.getName()));
        });
    }
    /**
     * 汇率计算
     */
    public void calculateExchangeRate(DataTablesInput input,DataTablesOutput<CapitalStockVO> output){
        List<CapitalStockVO> data = output.getData();
        List<DictDTO> list = dictService.list("capitalStock.stockType");
        Map<String, String> dictMap = list.stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
        for(CapitalStockVO vo:data) {
            vo.setStockTypeName(dictMap.get(vo.getStockType()));//存量种类
            if(StringUtils.isNotBlank(vo.getCorpId())){
                vo.setCorpName(corpService.get(vo.getCorpId()).getName());
            }
            if(StringUtils.isNotBlank(vo.getNetId())){
                vo.setNetName(corpService.get(vo.getNetId()).getName());
            }
        }

        String dateType = EmptyUtils.isEmpty(input.getFormdata().get("dateType")) ? "" : String.valueOf(input.getFormdata().get("dateType"));
        Date endDate = getEndDate(dateType);
        String endDateStr = DateUtils.formatDate(endDate) + " 23:59:59";
        endDate = DateUtils.parse(endDateStr);
        String reducedCurId = (String) input.getFormdata().get("reducedCurId");//折合币种
        String curId = (String) input.getFormdata().get("curId");//币种
        if(StringUtils.isNotBlank(reducedCurId)){
            //折合币种换算
            BigDecimal curExrate = new BigDecimal(1);
            BigDecimal exchangeRateBy = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, reducedCurId, endDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            if (exchangeRateBy != null) {
                curExrate = exchangeRateBy;
            }

            for(CapitalStockVO vo:data){
                if(vo.getAvailBal()!=null){
                    vo.setAvailBal(vo.getAvailBal().multiply(curExrate));
                }
                if(vo.getBeginBal()!=null){
                    vo.setBeginBal(vo.getBeginBal().multiply(curExrate));
                }
                if(vo.getEndBal()!=null){
                    vo.setEndBal(vo.getEndBal().multiply(curExrate));
                }
                if(vo.getFreezeMoney()!=null){
                    vo.setFreezeMoney(vo.getFreezeMoney().multiply(curExrate));
                }
                if(vo.getOutAmt()!=null){
                    vo.setOutAmt(vo.getOutAmt().multiply(curExrate));
                }
                if(vo.getInAmt()!=null){
                    vo.setInAmt(vo.getInAmt().multiply(curExrate));
                }
            }
        }
    }
    
    public Object createCaptialStockSumBar(DataTablesInput input,ICurrentUser user){
    	DataTablesOutput output=this.getDataTablesUnion(input,user);
    	String netId=(String)input.getFormdata().get("netId");
    	BigDecimal unit=new BigDecimal((String)input.getFormdata().get("unit"));
    	List<CapitalStockVO> list=output.getData();
    	Map<String, Object> map=new HashMap();
    	map.put("curName",btCurrencyService.getObjectById((String) input.getFormdata().get("curId")).getCurName());
    	map.put("netName",corpService.get((String) input.getFormdata().get("netId")).getName());
    	List<String> xAxisList = new ArrayList<>();
    	List<Object> xAxisBankStockData = new ArrayList<>();
    	List<Object> xAxisBillStockData = new ArrayList<>();
    	List<Series> bankStockSeries = new ArrayList<>();
    	List<Series> billStockSeries = new ArrayList<>();
    	xAxisList.add("期初资金余额");
    	xAxisList.add("本期资金增加额");
    	xAxisList.add("本期资金减少额");
    	xAxisList.add("期末资金余额");
    	xAxisList.add("本期不可动用资金");
    	xAxisList.add("可用资金余额");
    	map.put("xAxisList", xAxisList);
    	for(CapitalStockVO cap:list){
    		Bar bar = new Bar();
			bar.setType(SeriesType.bar);
			ItemStyle lable=new ItemStyle();
			Normal normal=new Normal();
			normal.setFormatter("{c}");
			normal.setShow(true);
			normal.setPosition(Position.top);
			lable.setNormal(normal);
    		if(StringUtils.isNotEmpty(netId) && netId.equals(cap.getNetId()) && "1".equals(cap.getStockType())){
    			bar.setLabel(lable);
    			xAxisBankStockData.add(cap.getBeginBal().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBankStockData);
    			bar.setName("期初资金余额");
                bar.setBarGap("0%");
    			bankStockSeries.add(bar);
    			xAxisBankStockData = new ArrayList<>();
    		    bar = new Bar();
    			bar.setType(SeriesType.bar);
    			bar.setLabel(lable);
//    			bar.setItemStyle(itemStyle);
    			xAxisBankStockData.add(cap.getInAmt().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBankStockData);
    			bar.setName("本期资金增加额");
                bar.setBarGap("0%");
    			bankStockSeries.add(bar);
    			xAxisBankStockData = new ArrayList<>();
    			 bar = new Bar();
     			bar.setType(SeriesType.bar);
     			bar.setLabel(lable);
    			xAxisBankStockData.add(cap.getOutAmt().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBankStockData);
    			bar.setName("本期资金减少额");
                bar.setBarGap("0%");
    			bankStockSeries.add(bar);
    			xAxisBankStockData = new ArrayList<>();
    			 bar = new Bar();
     			bar.setType(SeriesType.bar);
     			bar.setLabel(lable);
    			xAxisBankStockData.add(cap.getEndBal().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBankStockData);
    			bar.setName("期末资金余额");
                bar.setBarGap("0%");
    			bankStockSeries.add(bar);
    			xAxisBankStockData = new ArrayList<>();
    			 bar = new Bar();
     			bar.setType(SeriesType.bar);
     			bar.setLabel(lable);
    			xAxisBankStockData.add(cap.getFreezeMoney().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBankStockData);
    			bar.setName("本期不可动用资金");
                bar.setBarGap("0%");
    			bankStockSeries.add(bar);
    			
    			xAxisBankStockData = new ArrayList<>();
    			 bar = new Bar();
     			bar.setType(SeriesType.bar);
     			bar.setLabel(lable);
    			xAxisBankStockData.add(cap.getAvailBal().divide(unit, 2, BigDecimal.ROUND_UP));
//    			map.put("xAxisBankStockData", xAxisBankStockData);
    			map.put("bankStock", "银行存款");
    			bar.setData(xAxisBankStockData);
    			bar.setName("可用资金余额");
                bar.setBarGap("0%");
    			bankStockSeries.add(bar);
    			xAxisBankStockData = new ArrayList<>();
    		}
    		
    		if(StringUtils.isNotEmpty(netId) && netId.equals(cap.getNetId()) && "2".equals(cap.getStockType())){
    			bar.setLabel(lable);
    			xAxisBillStockData.add(cap.getBeginBal().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBillStockData);
    			bar.setName("期初资金余额");
                bar.setBarGap("0%");
    			billStockSeries.add(bar);
    			
    			xAxisBillStockData = new ArrayList<>();
    			 bar = new Bar();
     			bar.setType(SeriesType.bar);
     			bar.setLabel(lable);
    			xAxisBillStockData.add(cap.getInAmt().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBillStockData);
    			bar.setName("本期资金增加额");
                bar.setBarGap("0%");
    			billStockSeries.add(bar);
    			
    			xAxisBillStockData = new ArrayList<>();
    			 bar = new Bar();
     			bar.setType(SeriesType.bar);
     			bar.setLabel(lable);
    			xAxisBillStockData.add(cap.getOutAmt().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBillStockData);
    			bar.setName("本期资金减少额");
                bar.setBarGap("0%");
    			billStockSeries.add(bar);
    			
    			xAxisBillStockData = new ArrayList<>();
    			 bar = new Bar();
     			bar.setType(SeriesType.bar);
     			bar.setLabel(lable);
    			xAxisBillStockData.add(cap.getEndBal().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBillStockData);
    			bar.setName("期末资金余额");
                bar.setBarGap("0%");
    			billStockSeries.add(bar);
    			
    			xAxisBillStockData = new ArrayList<>();
    			 bar = new Bar();
     			bar.setType(SeriesType.bar);
     			bar.setLabel(lable);
    			xAxisBillStockData.add(cap.getFreezeMoney().divide(unit, 2, BigDecimal.ROUND_UP));
    			bar.setData(xAxisBillStockData);
    			bar.setName("本期不可动用资金");
                bar.setBarGap("0%");
    			billStockSeries.add(bar);
    			
    			xAxisBillStockData = new ArrayList<>();
    			 bar = new Bar();
     			bar.setType(SeriesType.bar);
     			bar.setLabel(lable);
    			xAxisBillStockData.add(cap.getAvailBal().divide(unit, 2, BigDecimal.ROUND_UP));
    			map.put("billStock", "应收票据");
    			bar.setData(xAxisBillStockData);
    			bar.setName("可用资金余额");
                bar.setBarGap("0%");    			billStockSeries.add(bar);
    			xAxisBillStockData = new ArrayList<>();
    		}
    	}
    	try {
    	ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		map.put("xAxisBankStockData", mapper.writeValueAsString(bankStockSeries));
		map.put("xAxisBillStockData", mapper.writeValueAsString(billStockSeries));
		return map;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "[]";
		}
    }
    /**
     * 获取开始时间
     *
     * @param dateType 日期类型
     * @return
     */
    private Date getBeginDate(String dateType) {
        Date beginDate = null;
        switch (dateType) {
            case CapitalStockConstant.DATE_TYPE_DAY:
                beginDate = DateUtils.getDate();
                break;
            case CapitalStockConstant.DATE_TYPE_WEEK:
                beginDate = DateUtils.getFirstWorkday();
                break;
            case CapitalStockConstant.DATE_TYPE_MONTH:
                beginDate = DateUtils.getMonthStartTime();
                break;
            case CapitalStockConstant.DATE_TYPE_SEASON:
                beginDate = DateUtils.getQuarterStartTime();
                break;
            case CapitalStockConstant.DATE_TYPE_YEAR:
                beginDate = DateUtils.getCurrYearFirst();
                break;
            default:
                beginDate = DateUtils.getDate();
        }
        if (beginDate.compareTo(DateUtils.getDate()) == 1) {
            beginDate = DateUtils.getDate();
        }
        return beginDate;
    }
    /**
     * 获取结束时间
     *
     * @param dateType 日期类型
     * @return
     */
    private Date getEndDate(String dateType) {
        Date endDate = null;
        switch (dateType) {
            case CapitalStockConstant.DATE_TYPE_DAY:
                endDate = DateUtils.getDate();
                break;
            case CapitalStockConstant.DATE_TYPE_WEEK:
                endDate = DateUtils.getLastWorkday();
                break;
            case CapitalStockConstant.DATE_TYPE_MONTH:
                endDate = DateUtils.getMonthEndTime();
                break;
            case CapitalStockConstant.DATE_TYPE_SEASON:
                endDate = DateUtils.getQuarterEndTime();
                break;
            case CapitalStockConstant.DATE_TYPE_YEAR:
                endDate = DateUtils.getCurrYearLast();
                break;
            default:
                endDate = DateUtils.getDate();
        }
        if (endDate.compareTo(DateUtils.getDate()) == 1) {
            endDate = DateUtils.getDate();
        }
        return endDate;
    }
    /**
     * 资金存量可用资金网点柱状图报表
     *
     * @param capitalStockVO
     * @return
     */
    public Object createNetBar(CapitalStockVO capitalStockVO,ICurrentUser user) {
        String netId = capitalStockVO.getNetId();
        String dateType = capitalStockVO.getDateType();
        String stockType = capitalStockVO.getStockType();
        String curId = capitalStockVO.getCurId();
        Date beginDate = getBeginDate(dateType);
        Date endDate = getEndDate(dateType);
//		Date balDate = this.capitalStockDao.getLastBalByBisAccBal(beginDate, curId, null);
//		if(EmptyUtils.isEmpty(balDate)){
//			balDate = DateUtils.addDay(beginDate, -1);
//		}
        String resource = ResourceConstant.BI_STOCK_CAPITALSTOCK_QUERY;
        //获取有权限的单位
        List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
        String billCorpSql = SqlUtil.getInSql(corpList, "sc.id", true);
		Date balDate = DateUtils.parseDate(DateUtils.addDay(beginDate, -1));
        String billSql = "";
        if (StringUtils.isBlank(stockType) || CapitalStockConstant.STOCK_TYPE_BILL.equalsIgnoreCase(stockType)) {
            // 票据只有人民币币别
            if (!EmptyUtils.isBlank(curId)) {
                BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(curId);
                if (!EmptyUtils.isEmpty(btCurrencyDTO) && CapitalStockConstant.CUR_TYPE_RMB.equalsIgnoreCase(btCurrencyDTO.getCurName())) {
                    billSql = abBillCapitalStockService.getQueryAbBillSql(CapitalStockConstant.REPORT_TYPE_NET,billCorpSql,netId);
                }
            } else {
                billSql = abBillCapitalStockService.getQueryAbBillSql(CapitalStockConstant.REPORT_TYPE_NET,billCorpSql,netId);
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (CapitalStockConstant.STOCK_TYPE_BILL.equalsIgnoreCase(stockType) && EmptyUtils.isBlank(billSql)) {
            map.put("legend", "[]");
            map.put("xAxisData", "[]");
            map.put("series", "[]");
            return map;
        }
        List<CapitalStockVO> dataList = this.capitalStockDao.listByNet(billSql, beginDate, endDate, curId, stockType, netId, balDate);
        if (EmptyUtils.isEmptyList(dataList)) {
            map.put("legend", "[]");
            map.put("xAxisData", "[]");
            map.put("series", "[]");
            return map;
        }
        if ("pieAll".equalsIgnoreCase(capitalStockVO.getReportType())) {
            return this.getPieAll(dataList);
        } else {
            return this.getPieByNetId(dataList);
        }
//			//拿到图例
//			Map<String, Object> datas = this.getLegend(dataList, curId);
//			try {
//				map.put("legend", datas.get("legend"));
//				map.put("xAxisData", datas.get("xAxisData"));
//				ObjectMapper mapper = new ObjectMapper();
//				mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//				map.put("series", mapper.writeValueAsString(datas.get("series")));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		return map;
    }
    private Map<String, Object> getLegend(List<CapitalStockVO> list, String curId) {
        Map<String, Object> dataMap = new HashMap<>(3);
        Set<String> legendSet = new HashSet<>();
        Set<String> xAxisSet = new HashSet();
        for (CapitalStockVO vo : list) {
            xAxisSet.add(vo.getNetName());
            legendSet.add(vo.getStockTypeName());
        }
        dataMap.put("legend", legendSet);
        dataMap.put("xAxisData", xAxisSet);
        List<Series> series = new ArrayList<Series>();
        // 循环legend  存量种类
        for (String legend : legendSet) {
            List<Object> datas = new ArrayList<>();
            for (String netName : xAxisSet) {
                BigDecimal payMoney = null;
                for (CapitalStockVO vo : list) {
                    if (netName.equalsIgnoreCase(vo.getNetName())
                            && legend.equalsIgnoreCase(vo.getStockTypeName())) {
                        // 非人民币根据汇率转换为人民币
                        BigDecimal exchangeRate = this.fcExcrateAdjustService.getExchangeRateBy(curId, DateUtils.getDate());
                        if (!EmptyUtils.isEmpty(exchangeRate)) {
                            payMoney = vo.getAvailBal().multiply(exchangeRate);
                        } else {
                            payMoney = vo.getAvailBal();
                        }
                        break;
                    }
                }
                if (!EmptyUtils.isEmpty(payMoney)) {
                    datas.add(payMoney);
                } else {
                    datas.add(BigDecimal.ZERO);
                }
            }
            // 设置数据
            Bar bar = new Bar();
            bar.setName(legend);
            bar.setType(SeriesType.bar);
            bar.setStack("stock");
            bar.setData(datas);
            series.add(bar);
        }
        dataMap.put("series", series);
        return dataMap;
    }
    /**
     * 加载所有网点的资金存量信息
     *
     * @param list
     * @return
     */
    private Object getPieAll(List<CapitalStockVO> list) {
        Set<String> legendSet = new HashSet<>();
        for (CapitalStockVO vo : list) {
            legendSet.add(vo.getNetName());
        }
        List<Map<String, Object>> jsonArray = new ArrayList<>();
        // 循环legend  网点
        for (String legend : legendSet) {
            Map<String, Object> jsonObject = new HashMap<>();
            BigDecimal money = BigDecimal.ZERO;
            for (CapitalStockVO vo : list) {
                if (legend.equals(vo.getNetName())) {
                    money = money.add(vo.getAvailBal());
                }
            }
            jsonObject.put("value", money);
            jsonObject.put("name", legend);
            jsonArray.add(jsonObject);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(jsonArray);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }
    /**
     * 加载对应网点的资金存量信息
     *
     * @param list
     * @return
     */
    private Map<String, Object> getPieByNetId(List<CapitalStockVO> list) {
        Map<String, Object> dataMap = new HashMap<>(2);
        Set<String> legendSet = new HashSet<>();
        for (CapitalStockVO vo : list) {
            legendSet.add(vo.getStockTypeName());
        }
        List<Map<String, Object>> jsonArray = new ArrayList<>();
        // 循环legend  存量种类
        for (String legend : legendSet) {
            Map<String, Object> jsonObject = new HashMap<>();
            BigDecimal money = BigDecimal.ZERO;
            for (CapitalStockVO vo : list) {
                if (legend.equals(vo.getStockTypeName())) {
                    money = money.add(vo.getAvailBal());
                }
            }
            jsonObject.put("value", money);
            jsonObject.put("name", legend);
            jsonArray.add(jsonObject);
        }
        dataMap.put("title", list.get(0).getNetName());
        dataMap.put("data", jsonArray);
        return dataMap;
    }
    public Object getWidgetBal(CapitalStockVO capitalStockVO) {
        String netId = capitalStockVO.getNetId();
        String curId = capitalStockVO.getCurId();
        Date beginDate = getBeginDate(CapitalStockConstant.DATE_TYPE_SEASON);
        Date endDate = getEndDate(CapitalStockConstant.DATE_TYPE_SEASON);
//		Date balDate = this.capitalStockDao.getLastBalByBisAccBal(beginDate, curId, null);
//		if(EmptyUtils.isEmpty(balDate)){
//			balDate = DateUtils.addDay(beginDate, -1);
//		}
		Date balDate = DateUtils.parseDate(DateUtils.addDay(beginDate, -1));
        Map<String, Object> dataMap = new HashMap<>();
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(curId);
        boolean rmbFlag = CapitalStockConstant.CUR_TYPE_RMB.equalsIgnoreCase(btCurrencyDTO.getCurName());
        // 非人民币根据汇率转换为人民币
        BigDecimal exchangeRate = this.fcExcrateAdjustService.getExchangeRateBy(curId, DateUtils.getDate());
        // 组装银行存款信息
        List<CapitalStockWidgetVO> accAvailBalList = this.capitalStockDao.listAccAvailBalByWidget(netId, beginDate, endDate, curId, balDate);
        if (!EmptyUtils.isEmptyList(accAvailBalList)) {
            BigDecimal sumAccAvailBal = BigDecimal.ZERO;
            for (CapitalStockWidgetVO vo : accAvailBalList) {
                sumAccAvailBal = sumAccAvailBal.add(vo.getAccAvailBal());
            }
            if (!rmbFlag && !EmptyUtils.isEmpty(exchangeRate)) {
                dataMap.put("sumAccAvailBalNatualCurrency", sumAccAvailBal.multiply(exchangeRate));
            } else {
                dataMap.put("sumAccAvailBalNatualCurrency", BigDecimal.ZERO);
            }
            dataMap.put("sumAccAvailBal", sumAccAvailBal);
        } else {
            dataMap.put("sumAccAvailBal", BigDecimal.ZERO);
            dataMap.put("sumAccAvailBalNatualCurrency", BigDecimal.ZERO);
        }
        // 票据只查询人民币
        BigDecimal sumBillAvailBal = BigDecimal.ZERO;
        if (rmbFlag) {
            sumBillAvailBal = this.abBillCapitalStockService.getBillAvailBal(netId, beginDate, endDate);
        }
        dataMap.put("sumBillAvailBal", sumBillAvailBal);
        dataMap.put("curSign", btCurrencyDTO.getCurSign());
        return dataMap;
    }
    public DataTablesOutput getDataTablesWidget(DataTablesInput dataTablesInput) {
        Date beginDate = getBeginDate(CapitalStockConstant.DATE_TYPE_SEASON);
        Date endDate = getEndDate(CapitalStockConstant.DATE_TYPE_SEASON);
        String curId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("curId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("curId"));
//		Date balDate = this.capitalStockDao.getLastBalByBisAccBal(beginDate, curId, null);
//		if(EmptyUtils.isEmpty(balDate)){
//			balDate = DateUtils.addDay(beginDate, -1);
//		}
		Date balDate = DateUtils.parseDate(DateUtils.addDay(beginDate, -1));
        String billSql = "";
        if (!EmptyUtils.isBlank(curId)) {
            BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(curId);
            if (!EmptyUtils.isEmpty(btCurrencyDTO) && CapitalStockConstant.CUR_TYPE_RMB.equalsIgnoreCase(btCurrencyDTO.getCurName())) {
                billSql = abBillCapitalStockService.getBillAvailBalSql("");
            }
        } else {
            billSql = abBillCapitalStockService.getBillAvailBalSql("");
        }
        return this.capitalStockDao.getDataTablesWidget(dataTablesInput, billSql, beginDate, endDate, curId, balDate);
    }
    public List<CapitalStockWidgetVO> listDataTablesWidget(String curId) {
        Date beginDate = getBeginDate(CapitalStockConstant.DATE_TYPE_SEASON);
        Date endDate = getEndDate(CapitalStockConstant.DATE_TYPE_SEASON);
//		Date balDate = this.capitalStockDao.getLastBalByBisAccBal(beginDate, curId, null);
//		if(EmptyUtils.isEmpty(balDate)){
//			balDate = DateUtils.addDay(beginDate, -1);
//		}
		Date balDate = DateUtils.parseDate(DateUtils.addDay(beginDate, -1));
        String billSql = "";
        if (!EmptyUtils.isBlank(curId)) {
            BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(curId);
            if (!EmptyUtils.isEmpty(btCurrencyDTO) && CapitalStockConstant.CUR_TYPE_RMB.equalsIgnoreCase(btCurrencyDTO.getCurName())) {
                billSql = abBillCapitalStockService.getBillAvailBalSql("");
            }
        } else {
            billSql = abBillCapitalStockService.getBillAvailBalSql("");
        }
        return this.capitalStockDao.listDataTablesWidget(billSql, beginDate, endDate, curId, balDate);
    }
}
