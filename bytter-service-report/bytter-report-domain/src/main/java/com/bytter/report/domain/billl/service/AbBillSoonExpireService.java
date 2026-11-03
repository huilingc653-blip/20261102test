package com.bytter.report.domain.billl.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.utils.*;
import com.bytter.increment.api.dto.BiRemainTermDTO;
import com.bytter.increment.api.service.IBiRemainTermService;
import com.bytter.report.domain.billl.dao.AbBillSoonExpireDao;
import com.bytter.report.domain.billl.vo.AbBillSoonExpireDetailedVO;
import com.bytter.report.domain.billl.vo.AbBillSoonExpireVO;
import com.bytter.report.domain.billl.vo.AbMobileAppVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
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
/**
 * @Title: service
 * @Description: 即将到期票据分析
 * @author zhenghj
 * @date 2020.2.10
 * @version 1.0
 *
 */
@Service("abBillSoonExpireService")
@Transactional
public class AbBillSoonExpireService extends BaseService<AbBillSoonExpireVO, String> implements DictInjectProvider {
    @Autowired
    private AbBillSoonExpireDao abBillSoonExpireDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBiRemainTermService biRemainTermService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<AbBillSoonExpireVO, String> getEntityDao() {
        return abBillSoonExpireDao;
    }
    /**
     * 查询即将到期票据信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user,String moneyUnitStr) {
        String joinSql = "";
        String resource = AccebillConstant.AB_BILL_SOON_EXPIRE_QUERY;
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "b.corp_id", true);
            joinSql += " and " + corpAuthSql;
        }
        Integer moneyUnit = null;
        if(StringUtils.isNotEmpty(moneyUnitStr)){
            moneyUnit = Integer.valueOf(moneyUnitStr);
        }else{
            moneyUnit = Integer.valueOf((String) dataTablesInput.getFormdata().get("moneyUnit"));
        }
        //查询剩余期限维护信息
        List<com.bytter.increment.api.dto.BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs("a.end_date");
        List biRemainTermDTO=biRemainTermDTOs;
        //根据剩余期限查询数据信息
        dataTablesInput.setLength(100000);
        DataTablesOutput datas = abBillSoonExpireDao.getDataTablesUnion(dataTablesInput, joinSql, biRemainTermDTO);
        //单位期限集合
        Set<String> corpTermSet = new HashSet<String>();
        //单位信息
        Map<String,String> corpMap = new HashMap<>();
        //存放组装信息
        List<Map<String,Object>> termList = null;
        if (datas != null && datas.getData() != null) {
            //数据集合
            List<AbBillSoonExpireVO> list = datas.getData();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_FLAG","AB_BILL_TYPE");
            // inject(list);
            termList = new ArrayList<>();
            //拼装单位信息和单位期限信息
            for (AbBillSoonExpireVO reportVO : list) {
                corpTermSet.add(reportVO.getCorpId()+";"+reportVO.getTermId());
                if(!corpMap.containsKey(reportVO.getCorpId())){
                    corpMap.put(reportVO.getCorpId(),reportVO.getCorpName());
                }
                // 添加冗余数据
                reportVO.injectValues(listDictMap);
            }
            Map<String,AbBillSoonExpireVO> MoneyMap = new HashMap<>();
            for(String corpTerms : corpTermSet) {
                String[] corpTerm = corpTerms.split(";");
                for(AbBillSoonExpireVO reportVO : list){
                    //商票金额合计
                    if(corpTerm[0].equals(reportVO.getCorpId()) && corpTerm[1].equals(reportVO.getTermId())) {
                        //银票金额合计
                        BigDecimal bankMoneyTotal = EmptyUtils.isEmpty(reportVO.getBankMoneyTotal()) ? BigDecimal.ZERO : reportVO.getBankMoneyTotal();
                        //商票金额合计
                        BigDecimal businessMoneyTotal = EmptyUtils.isEmpty(reportVO.getBusinessMoneyTotal()) ? BigDecimal.ZERO : reportVO.getBusinessMoneyTotal();
                        //合计
                        BigDecimal total = EmptyUtils.isEmpty(reportVO.getTotal()) ? BigDecimal.ZERO : reportVO.getTotal();
                        reportVO.setBankMoneyTotal(bankMoneyTotal.divide(getMoneyUnit(moneyUnit), 2, RoundingMode.HALF_UP));
                        reportVO.setBusinessMoneyTotal(businessMoneyTotal.divide(getMoneyUnit(moneyUnit), 2, RoundingMode.HALF_UP));
                        reportVO.setTotal(total.divide(getMoneyUnit(moneyUnit), 2, RoundingMode.HALF_UP));
                        MoneyMap.put(corpTerms,reportVO);
                    }
                }
            }
            //去重后单位信息
            for (String corpId : corpMap.keySet()){
                Map<String,Object> map = new HashMap<>();
                //遍历期限集合
                for(com.bytter.increment.api.dto.BiRemainTermDTO termDto : biRemainTermDTOs){
                    map.put("corpId",corpId);
                    map.put("corpName",corpMap.get(corpId));
                    //判断单位期限是否有数据，如果有数据，进行赋值
                    if(!EmptyUtils.isEmpty(MoneyMap.get(corpId+";"+termDto.getId()))){
                        map.put(termDto.getId()+"_bank",MoneyMap.get(corpId+";"+termDto.getId()).getBankMoneyTotal());
                        map.put(termDto.getId()+"_business",MoneyMap.get(corpId+";"+termDto.getId()).getBusinessMoneyTotal());
                        map.put(termDto.getId()+"_total",MoneyMap.get(corpId+";"+termDto.getId()).getTotal());
                    }else{
                        map.put(termDto.getId()+"_bank",BigDecimal.ZERO);
                        map.put(termDto.getId()+"_business",BigDecimal.ZERO);
                        map.put(termDto.getId()+"_total",BigDecimal.ZERO);
                    }
                }
                termList.add(map);
            }
        }
        //分页处理
        if(null == termList && EmptyUtils.isEmpty(termList)){
            datas.setDraw(0);
            datas.setRecordsTotal(0);
            datas.setRecordsFiltered(0);
            datas.setError(null);
            datas.setData(null);
            return datas;
        }
        // 设置返回前端的DataTablesdatas
        int totalNum = termList.size();
        Integer pageNumber = dataTablesInput.getPageNumber();
        int page = (totalNum + 10 - 1)/10;
        if(pageNumber == page){
            datas.setData(termList.subList(dataTablesInput.getStart(), totalNum));
        }else {
            if(dataTablesInput.getLength() > totalNum){
                datas.setData(termList.subList(dataTablesInput.getStart(), totalNum));
            }else {
                datas.setData(termList.subList(dataTablesInput.getStart(), dataTablesInput.getStart()+10));
            }
        }
        datas.setRecordsTotal(termList.size());
        datas.setRecordsFiltered(termList.size());
        return datas;
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
     * 即将到期票据分析数据返回 1
     * @param list
     * @param moneyUnit
     * @return
     */
    public Object getData(List<Map<String,Object>> list,String moneyUnit) {
        Object jsonObject = dealData(list,moneyUnit);
        return jsonObject;
    }
    /**
     * 图表数据处理 2
     * @param list
     * @param moneyUnit
     * @return
     */
    private Object dealData(List<Map<String,Object>> list,String moneyUnit) {
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
        List<com.bytter.increment.api.dto.BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs("a.end_date");
        List biRemainTermDTO=biRemainTermDTOs;
        List<EchartsData> echartsDatas = formatData(list,biRemainTermDTO);
        return createBar("即将到期票据分析", "", getXData(biRemainTermDTO), yAxisName, "", echartsDatas);
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
    private List<EchartsData> formatData(List<Map<String,Object>> dataList,List<BiRemainTermDTO> biRemainTermDTOs) {
        List<EchartsData> mergeList = new ArrayList<>();
        EchartsData data = new EchartsData();
        data.setName("");
        List<Object> list = new ArrayList<Object>();
        Map<String,BigDecimal> dataMap = new HashMap<>();
        //图表显示数值处理
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
        Iterator<Map.Entry<String,BigDecimal>> it = dataMap.entrySet().iterator();
       /* while (it.hasNext()) {
            Map.Entry<String,BigDecimal> entry = it.next();
            list.add(entry.getValue() == null ? 0 : entry.getValue());
        }*/
        for(BiRemainTermDTO term : biRemainTermDTOs){
            list.add(dataMap.get(term.getId()) == null ? 0 : dataMap.get(term.getId()));
        }
        data.setData(list);
        mergeList.add(data);
        return mergeList;
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
     * 即将到期票据分析数据明细
     * @param input
     * @return
     * @throws ParseException
     */
    public DataTablesOutput listSoonExpireDetailed(DataTablesInput input) {
        String termId = null;
        String searchSql = null;
        if(StringUtils.isNotEmpty(input.getFormdata().get("termId"))){
            termId = String.valueOf(input.getFormdata().get("termId"));
        }else{
            return null;
        }
        //查询剩余期限维护信息
        List<com.bytter.increment.api.dto.BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs("a.end_date");
        for(int i = 0; i < biRemainTermDTOs.size(); i++){
            if(termId.equals(biRemainTermDTOs.get(i).getId())){
                searchSql = biRemainTermDTOs.get(i).getSearchSql();
            }
        }
        DataTablesOutput<AbBillSoonExpireDetailedVO> dataTablesOutput = abBillSoonExpireDao.listSoonExpireDetailed(input, searchSql);
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            // 添加冗余数据
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_FLAG","AB_BILL_TYPE");
            for (AbBillSoonExpireDetailedVO bill : listData){
                bill.injectValues(listDictMap);
            }
        });
        return dataTablesOutput;
    }
    
    
    
    
    /**
     * appJsonObject
     * @param list
     * @return
     */
    public Object appJsonObject(List<Map<String,Object>> list,String buisTypeName,boolean viewTypeFlag,String remainMonId){
    	List<AbMobileAppVo> resultList =new ArrayList<AbMobileAppVo>();
    	//查询剩余期限维护信息
        List<com.bytter.increment.api.dto.BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs("a.end_date");
        String[] biRemainTermDTOsArray = null;
        if(ListUtils.isNotNullOrEmpty(biRemainTermDTOs)){
        	biRemainTermDTOsArray = new String[biRemainTermDTOs.size()];
        	for(int i = 0;i<biRemainTermDTOs.size();i++){
        		biRemainTermDTOsArray[i] = biRemainTermDTOs.get(i).getId();
        	}
        }
        Map<String,Object> returnAjaxJson = new HashMap<String,Object>();
        BigDecimal bankSumMoney = BigDecimal.ZERO;
		BigDecimal busineSumMoeny = BigDecimal.ZERO;
        for(Map<String,Object> relMap:list){
        	 for(com.bytter.increment.api.dto.BiRemainTermDTO dto:biRemainTermDTOs){
        		AbMobileAppVo mobileVo = new AbMobileAppVo();
         			mobileVo.setCorpName(relMap.get("corpName").toString());
         			mobileVo.setCorpId(relMap.get("corpId").toString());
         			mobileVo.setTimeLimitName(dto.getTermName());//剩余期限展示
         			mobileVo.setBillTypeName(buisTypeName);//票据类型
         			mobileVo.setSilverMoney(new BigDecimal(relMap.get(dto.getId()+"_bank").toString()));//银票金额
         			mobileVo.setSilverMoneyStr(NumberUtil.formatNumeric(new BigDecimal(relMap.get(dto.getId()+"_bank").toString())));//银票金额
         			bankSumMoney = bankSumMoney.add(mobileVo.getSilverMoney());
         			mobileVo.setBusinessMoney(new BigDecimal(relMap.get(dto.getId()+"_business").toString()));//商票金额
         			mobileVo.setBusinessMoneyStr(NumberUtil.formatNumeric(new BigDecimal(relMap.get(dto.getId()+"_business").toString())));//商票金额
         			busineSumMoeny = busineSumMoeny.add(mobileVo.getBusinessMoney());
         			mobileVo.setTimeLimit(dto.getId());
         			resultList.add(mobileVo);
        	 }
    	}
        //列表
        if(viewTypeFlag){
            //如果带时间距离格式的
            if(!StringUtils.isBlank(remainMonId) && ListUtils.isNotNullOrEmpty(resultList)){
            	List<AbMobileAppVo> arrayList = new ArrayList<AbMobileAppVo>();
            	bankSumMoney = BigDecimal.ZERO;
 				busineSumMoeny = BigDecimal.ZERO;
 				String[] ridArray = remainMonId.split(",");
     			for(AbMobileAppVo arrayVo:resultList){
     				for(String remainMonIdSign:ridArray){
         				if(remainMonIdSign.equals(arrayVo.getTimeLimit())){
	             			bankSumMoney = bankSumMoney.add(arrayVo.getSilverMoney());
	     					busineSumMoeny = busineSumMoeny.add(arrayVo.getBusinessMoney());
	         				arrayList.add(arrayVo);
         				}
     				}
     			}
     			resultList = new ArrayList<>();
     			resultList = arrayList;
     		}
            returnAjaxJson.put("flag", true);
            returnAjaxJson.put("msg", "操作成功");
            returnAjaxJson.put("data", resultList);
            returnAjaxJson.put("bankSumMoney",NumberUtil.formatNumeric(bankSumMoney));
            returnAjaxJson.put("busineSumMoeny", NumberUtil.formatNumeric(busineSumMoeny));
            returnAjaxJson.put("count", ListUtils.isNotNullOrEmpty(resultList)?resultList.size():"0.00");
            return returnAjaxJson;
        //图形
        }else{
        	//左右滚动数据，
         	String[] originDate = null;
        	List<String> itemsList = new ArrayList<String>();
        	List<Map<String,Object>> finalResultList = null;
        	List<Map<String,Object>> arrayList = new ArrayList<Map<String,Object>>();
        	String[] ridArray = 
        			!StringUtils.isBlank(remainMonId)?
        			remainMonId.split(",")
        			:biRemainTermDTOsArray
        			;
            //如果带时间距离格式的 
            if(ListUtils.isNotNullOrEmpty(resultList) && null!=ridArray){
            	//此步完成条件过滤查询返回数据
     			for(AbMobileAppVo arrayVo:resultList){
     				for(String remainMonIdSign:ridArray){
	     				if(remainMonIdSign.equals(arrayVo.getTimeLimit())){
	     					Map<String, Object> bankParms = new HashMap<String, Object>();
	     					bankParms.put("name", "商票");
	     					bankParms.put("corpName", arrayVo.getCorpName());
	     					bankParms.put("money", arrayVo.getBusinessMoney());
	         				arrayList.add(bankParms);
	         				Map<String, Object> businessParms =new HashMap<String, Object>();
	         				businessParms.put("name", "银票");
	         				businessParms.put("corpName", arrayVo.getCorpName());
	         				businessParms.put("money", arrayVo.getSilverMoney());
	         				arrayList.add(businessParms);
	         				if(!itemsList.contains(arrayVo.getCorpName())){
	         					itemsList.add(arrayVo.getCorpName());
	         				}
	     				}
     				}
     			}
     		    //分解：过滤重复单位多条件类型条件查询的数据
     			Map<String, Map<String,Object>> result2 = new HashMap<String, Map<String,Object>>();
     			for(Map<String, Object> map : arrayList){
     				String corpName = map.get("corpName").toString();
     				String name = map.get("name").toString();
     				BigDecimal value = new BigDecimal(map.get("money").toString());
     				if(result2.containsKey(corpName+name)){
     					BigDecimal temp = new BigDecimal(result2.get(corpName+name).get("money").toString());
     					value = value.add(temp);
     					result2.get(corpName+name).put("money", value);
     					continue;
     				}
     				result2.put(corpName+name, map);
     			}
     			if(StringUtils.isNotEmpty(result2)){
         			finalResultList = new ArrayList<Map<String,Object>>(result2.values());
     			}
     		}
            //左右滚动 
            if(ListUtils.isNotNullOrEmpty(itemsList)){
                int len = itemsList.size();
                if (itemsList.size()>=5) {
                    len = 5;
                }
                originDate = new String[len];
                for (int i = 0; i < len; i++) {
                    originDate[i]=itemsList.get(i);
                }
            }
            returnAjaxJson.put("flag", true);
            returnAjaxJson.put("msg", "操作成功");
            returnAjaxJson.put("data", resultList);
            returnAjaxJson.put("dataFinal", finalResultList);
            returnAjaxJson.put("originDate", originDate);
        }
		return returnAjaxJson;
    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
