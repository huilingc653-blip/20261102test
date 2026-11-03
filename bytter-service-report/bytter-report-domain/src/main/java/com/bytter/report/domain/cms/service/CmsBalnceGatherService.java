package com.bytter.report.domain.cms.service;

import com.bytter.report.domain.cms.dao.CmsBalanceGatherDao;
import com.bytter.report.domain.cms.vo.CmsBalanceGatherVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.utils.*;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.DictDTO;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.itemstyle.Normal;

import com.google.gson.JsonObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 信贷融资
 * @author lvwl
 *
 */
@SuppressWarnings("rawtypes")
@Service("cmsBalnceGatherService")
@Transactional
public class CmsBalnceGatherService extends BaseService {

	@Autowired
	private CmsBalanceGatherDao cmsBalanceGatherDao;
	@DubboReference
	private IBtCurrencyService btCurrencyServiceImpl;
	@DubboReference
	private IMultiLangService multiLangServiceI;
	@DubboReference
	private ICorpService iCorpService;
	@DubboReference
	private IDictService dictService;

	/**汇总单位的ID*/
	private final String GATHER_CORP = "gatherCorpId";
	/**折合人名币的ID*/
	private final String NATURAL_CUR = "naturalCurId";
	
	/**查询余额汇总数据*/
	@Transactional(readOnly = true)
	public List queryGatherList(CmsBalanceGatherVO vo,String userId){
		this.queryParam(vo);//查询参数处理
		List<Map<String, Object>> balList = new ArrayList<Map<String, Object>>();
		if(StringUtils.isEmpty(vo.getCorpId())){
			List<String> corpIds = iCorpService.getUserCorpIds(ResourceConstant.CMS_REPORT_LOAN_BALANCEGATHER_QUERY,userId);
			String corps = StringUtils.join(corpIds, ",");
			vo.setCorpId(corps);
		}
		List<CmsBalanceGatherVO> list = cmsBalanceGatherDao.queryGatherList(vo);//数据库查询列表数据
		
		if(list == null || list.isEmpty()){
			return list;
		}
		Map<String, Object> gatherCorp = new HashMap<String, Object>();
		gatherCorp.put("corpId", this.GATHER_CORP);
		gatherCorp.put("corpName", multiLangServiceI.getLang("common.button.collect"));//TODO 获取国际资源
		gatherCorp.put("corpCode", "");
		List<Map<String,Object>> gatherCurList = new ArrayList<Map<String,Object>>();//币别集合
		balList.add(gatherCorp);
		//数据处理
		for(CmsBalanceGatherVO gather : list){
			Map<String,Object> corp = checkListKeyValue(balList,"corpId",gather.getCorpId());//查找单位
			if(corp != null){//单位存在
				List<Map<String,Object>> curList = (List<Map<String,Object>>) corp.get("curList");//获取币别集合
				Map<String,Object> cur = this.checkListKeyValue(curList,"curId",gather.getCurId());//查找币别
				if(cur != null){//币别存在
					List<CmsBalanceGatherVO> dataList = (List<CmsBalanceGatherVO>) cur.get("dataList");
					dataList.add(gather);
				}else{//币别不存在
					cur = new HashMap<String,Object>();//币别信息
					cur.put("curId", gather.getCurId());
					cur.put("curName", gather.getCurName());
					List<CmsBalanceGatherVO> dataList = new ArrayList<CmsBalanceGatherVO>();
					dataList.add(gather);
					cur.put("dataList", dataList);
					curList.add(cur);
				}
				
				int size = (Integer) corp.get("size");
				corp.put("size", size + 1);
			}else{//单位不存在
				corp = new HashMap<String, Object>();
				corp.put("corpId", gather.getCorpId());
				corp.put("corpName", gather.getCorpName());
				corp.put("corpCode", gather.getCorpCode());
				
				List<Map<String,Object>> curList = new ArrayList<Map<String,Object>>();//币别集合
				Map<String,Object> cur = new HashMap<String,Object>();//币别信息
				cur.put("curId", gather.getCurId());
				cur.put("curName", gather.getCurName());
				
				List<CmsBalanceGatherVO> dataList = new ArrayList<CmsBalanceGatherVO>();
				dataList.add(gather);
				cur.put("dataList", dataList);//币别中添加融资信息
				corp.put("size", dataList.size());
				
				curList.add(cur);//添加币别集合
				corp.put("curList", curList);
				
				balList.add(corp);//将单位添加到返回map中
			}
			
			/**汇总*/
			Map<String,Object> cur = this.checkListKeyValue(gatherCurList,"curId",gather.getCurId());//查找币别
			if(cur != null){//币别存在
				List<CmsBalanceGatherVO> gatherDataList = (List<CmsBalanceGatherVO>) cur.get("dataList");
				
				CmsBalanceGatherVO balvo = checkListValue(gatherDataList,gather.getLoanSign());//查找融资类型
				if(balvo != null){//查询到
					balvo.setBeformMoney(balvo.getBeformMoney().add(gather.getBeformMoney()));//融资期初余额
					balvo.setEarningMoney(balvo.getEarningMoney().add(gather.getEarningMoney()));//融资收入
					balvo.setDefrayMoney(balvo.getDefrayMoney().add(gather.getDefrayMoney()));//融资支出
				}else{//未查询到
					CmsBalanceGatherVO gg = new CmsBalanceGatherVO();
					BeanUtils.copyProperties(gather, gg);
					gatherDataList.add(gg);
					Integer size = (Integer) gatherCorp.get("size");
					if(size == null){
						size = 0;
					}
					gatherCorp.put("size", size + 1);
				}
			}else{//币别不存在
				cur = new HashMap<String,Object>();//币别信息
				cur.put("curId", gather.getCurId());
				cur.put("curName", gather.getCurName());
				List<CmsBalanceGatherVO> dataList = new ArrayList<CmsBalanceGatherVO>();
				CmsBalanceGatherVO gg = new CmsBalanceGatherVO();
				BeanUtils.copyProperties(gather, gg);
				dataList.add(gg);
				cur.put("dataList", dataList);//币别中添加融资信息
				Integer size = (Integer) gatherCorp.get("size");
				if(size == null){
					size = 0;
				}
				gatherCorp.put("size", size + 1);
				gatherCurList.add(cur);//添加币别集合
			}
		}
		gatherCorp.put("curList", gatherCurList);
		this.exrateCount(balList);
		// 逆序，汇总放在最下面
		Collections.reverse(balList);
		return balList;
	}
	
	
	
	/**
	 * list<Map<String,Object>>中是 key对应的值与value相同的Map
	 * @param list
	 * @param key
	 * @param value
	 * @return key对应值为vlaue的map对象
	 */
	@Transactional(readOnly = true)
	private Map<String,Object> checkListKeyValue(List<Map<String,Object>> list, String key,String value){
		for(Map<String,Object> obj : list){
			if(value.equals(obj.get(key))){//存在
				return obj;
			}
		}
		return null;
	}
	
	/**
	 * List<CmsBalanceGatherVO>中查找value类型的融资数据
	 * @param list
	 * @param value
	 * @return 对应值为vlaue的CmsBalanceGatherVO对象
	 */
	@Transactional(readOnly = true)
	private CmsBalanceGatherVO checkListValue(List<CmsBalanceGatherVO> list,String value){
		for(CmsBalanceGatherVO obj : list){
			if(obj.getLoanSign().equals(value)){//存在
				return obj;
			}
		}
		return null;
	}
	
	/**
	 * 汇率计算，转化成本位币金额
	 */
	@Transactional(readOnly = true)
	public void exrateCount(List<Map<String, Object>> balList){
		BtCurrencyDTO natualCur = btCurrencyServiceImpl.getNatualCurrency();//本位币信息
		for(Map<String,Object> corp : balList){
			
			boolean onlyNaturalSign = true;//只有本位币标识
			Map<String,Object> naturalCur = new HashMap<String,Object>();//折合币别信息
			naturalCur.put("curId", this.NATURAL_CUR);
			naturalCur.put("curName",multiLangServiceI.getLang("table.fbsBudgetProcess.convert")  + natualCur.getCurName());
			List<CmsBalanceGatherVO> naturalDataList = new ArrayList<CmsBalanceGatherVO>();
			naturalCur.put("dataList", naturalDataList);
			
			List<Map<String,Object>> curList = (List<Map<String,Object>>) corp.get("curList");//获取币别集合
			for(Map<String,Object> cur : curList){
				List<CmsBalanceGatherVO> dataList = (List<CmsBalanceGatherVO>) cur.get("dataList");//获取数据
				for(CmsBalanceGatherVO data : dataList){
					if(!natualCur.getId().equals(data.getCurId())){//非本位币
						onlyNaturalSign = false;
					}
					CmsBalanceGatherVO balvo = checkListValue(naturalDataList,data.getLoanSign());//查找融资类型
					if(balvo != null){//查找到
						balvo.setBeformMoney(balvo.getBeformMoney().add(data.getBeformMoney().multiply(data.getCurExrate())));//融资期初余额
						balvo.setEarningMoney(balvo.getEarningMoney().add(data.getEarningMoney().multiply(data.getCurExrate())));//融资收入
						balvo.setDefrayMoney(balvo.getDefrayMoney().add(data.getDefrayMoney().multiply(data.getCurExrate())));//融资支出
					}else{//未查找到
						balvo = new CmsBalanceGatherVO();
						BeanUtils.copyProperties(data, balvo);
						balvo.setBeformMoney(data.getBeformMoney().multiply(data.getCurExrate()));//融资期初余额
						balvo.setEarningMoney(data.getEarningMoney().multiply(data.getCurExrate()));//融资收入
						balvo.setDefrayMoney(data.getDefrayMoney().multiply(data.getCurExrate()));//融资支出
						naturalDataList.add(balvo);
					}
				}
			}
			if(!onlyNaturalSign){
				curList.add(naturalCur);//将折合本位币信息添加到集合中
				
				Integer size = (Integer) corp.get("size");
				if(size == null){
					size = 0;
				}
				corp.put("size", size + naturalDataList.size());
			}
		}
	}
	
	/**
	 * 获取报表列表数据
	 */
	@Transactional(readOnly = true)
	public List getGatherLis(CmsBalanceGatherVO vo,List<Map<String, Object>> list){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		String corpId = vo.getCorpId();
		String curId = vo.getCurId();
		for(Map<String, Object> corp : list){
			if(corpId.equals(corp.get("corpId"))){
				List<Map<String,Object>> curs = (List<Map<String, Object>>) corp.get("curList");
				for(Map<String,Object> cur : curs){
					if(curId.equals(cur.get("curId"))){
						Map<String, Object> resultCorp = new HashMap<String, Object>();
						resultCorp.put("corpId", corp.get("corpId"));
						resultCorp.put("corpName", corp.get("corpName"));
						resultCorp.put("corpCode", corp.get("corpCode"));
						
						List<Map<String,Object>> resultCurs = new ArrayList<Map<String, Object>>();//新建返回币别集合
						resultCurs.add(cur);//添加元素
						
						List<CmsBalanceGatherVO> dataList = (List<CmsBalanceGatherVO>) cur.get("dataList");
						resultCorp.put("size", dataList.size());//设置单位大小
						
						resultCorp.put("curList", resultCurs);
						result.add(resultCorp);
						return result;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 获取报表数据
	 * @param vo
	 * @param list
	 * @param title
	 * @return
	 */
	@Transactional(readOnly = true)
	private List getGatherPriList(CmsBalanceGatherVO vo,List<Map<String, Object>> list,Map<String, String> title){
		List<CmsBalanceGatherVO> balList = new ArrayList<CmsBalanceGatherVO>();
		String corpId = vo.getCorpId();
		String curId = vo.getCurId();
		if (EmptyUtils.isEmpty(corpId)) {
			/*UserDTO userDTO = userService.getCurrentLoginUser();//当前登陆用户
			corpId = userDTO.getCorp().getId();*/
			vo.setCorpId(this.GATHER_CORP);//所有单位
			corpId = vo.getCorpId();
		}
		if (EmptyUtils.isEmpty(curId)) {
			//curId = btCurrencyService.getNatualCurrency().getId();//本位币
			vo.setCurId(this.NATURAL_CUR);//所有币别
			curId = vo.getCurId();
		}
		
		for(Map<String, Object> corp : list){
			if(corpId.equals(corp.get("corpId"))){
				title.put("text", "("+corp.get("corpName").toString()+")融资占比情况");
				List<Map<String,Object>> curs = (List<Map<String, Object>>) corp.get("curList");
				for(Map<String,Object> cur : curs){
					if(curId.equals(cur.get("curId"))){
						title.put("subtext", cur.get("curName").toString());
						List<CmsBalanceGatherVO> dataList = (List<CmsBalanceGatherVO>) cur.get("dataList");
						balList.addAll(dataList);
						return balList;
					}
				}
			}
		}
		
		return balList;
	}
	
	private Map<String, Object> initLoanNullPie() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Object> series = new ArrayList<Object>();
		List<String> legend = new ArrayList<String>();
		CmsBalanceGatherVO vo1 = new CmsBalanceGatherVO();
		vo1.setLoanSign("01");
		vo1.setEarningMoney(BigDecimal.ZERO);
//		iInjection.inject(vo1);
		Map<String, Object> loanMap1 = new HashMap<String, Object>();
		loanMap1.put("name", vo1.getLoanSignName());
		loanMap1.put("value", BigDecimal.ZERO);
		legend.add(vo1.getLoanSignName());//银行借款
		series.add(loanMap1);
		
		CmsBalanceGatherVO vo2 = new CmsBalanceGatherVO();
		vo2.setLoanSign("02");
		vo2.setEarningMoney(BigDecimal.ZERO);
//		iInjection.inject(vo2);
		Map<String, Object> loanMap2 = new HashMap<String, Object>();
		loanMap2.put("name", vo2.getLoanSignName());
		loanMap2.put("value", BigDecimal.ZERO);
		legend.add(vo2.getLoanSignName());//银行借款
		series.add(loanMap2);
		
		CmsBalanceGatherVO vo3 = new CmsBalanceGatherVO();
		vo3.setLoanSign("03");
		vo3.setEarningMoney(BigDecimal.ZERO);
//		iInjection.inject(vo3);
		Map<String, Object> loanMap3 = new HashMap<String, Object>();
		loanMap3.put("name", vo3.getLoanSignName());
		loanMap3.put("value", BigDecimal.ZERO);
		legend.add(vo3.getLoanSignName());//银行借款
		series.add(loanMap3);
		dataMap.put("series", series);
		dataMap.put("legend", legend);
		return dataMap;
	}
	
	/***
	 * 饼图
	 */
	@Transactional(readOnly = true)
	public Object createNetListPie(CmsBalanceGatherVO vo,List<Map<String, Object>> list) {
		List<CmsBalanceGatherVO> dataList = null;
		Map<String, String> title = new HashMap<String,String>();
		if (!EmptyUtils.isEmpty(vo)) {
			//dataList = cmsBalanceGatherDao.queryGatherList(vo);//数据库查询列表数据
			dataList = getGatherPriList(vo,list,title);
			title.put("x", "center");
		}
		//拿到图例
		Map<String, Object> datas = this.getLegend(dataList);
		if (EmptyUtils.isEmpty(dataList)) {
			datas = initLoanNullPie();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> series  = (List<Object>) datas.get("series");
		List<String> legend = (List<String>) datas.get("legend");
		
		//Map<String,String> title = (Map<String,String>) datas.get("title");
		Pie ringSeries = new Pie();
		ringSeries.data(series.toArray(new Object[series.size()]));
		ringSeries.type(SeriesType.pie);
		String radius[] = {"50%","42%"};//圆心坐标位置，距离X轴和Y轴的百分比
	    ringSeries.radius("70%");//半径大小
	    ringSeries.center(radius);
	    Normal normal = new Normal();
	    normal.setShow(false);
	    normal.setPosition(Position.outside);
	    /*ItemStyle is = new ItemStyle();
	    is.setNormal(normal);*/
	    ringSeries.label().setNormal(normal);
		map.put("series", JsonUtils.toJSONString(ringSeries));
		map.put("legend", legend.toArray(new Object[legend.size()]));
		map.put("title", title);
		map.put("corpId", vo.getCorpId());
		return map;
	}

	@Transactional(readOnly = true)
	private Map<String, Object> getLegend(List<CmsBalanceGatherVO> dataList) {
		Map<String, Object> dataMap = new TreeMap<String, Object>();
		List<String> legendList = new ArrayList<String>();
		List<DictDTO> list = dictService.list("Loan.Sign");
		Map<String, String> dictMap = list.stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
		for (CmsBalanceGatherVO vo : dataList) {
			String loanSignName = dictMap.get(vo.getLoanSign());
			vo.setLoanSignName(loanSignName);
			legendList.add(loanSignName);
		}
		
		List<Object> series = new ArrayList<Object>();
		for (String legend : legendList) {
			BigDecimal sumMoney = BigDecimal.ZERO;
			for(CmsBalanceGatherVO gather : dataList){
				if(gather.getLoanSignName().equalsIgnoreCase(legend)){
					
					//BtCurrencyDTO cur = btCurrencyService.getObjectById(gather.getCurId());
					BigDecimal earningMoney = gather.getEarningMoney();
//					//转化为本位币
//					if("0".equals(cur.getNaturalSign())){
//						BigDecimal rate = cur.getCurExrate();
//						earningMoney = earningMoney.multiply(rate);
//					}
					sumMoney = sumMoney.add(earningMoney);
				}
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name",legend);
			map.put("value", sumMoney);
			series.add(map);
		}
		dataMap.put("series", series);
		dataMap.put("legend", legendList);
		return dataMap;
	}
	
	@Transactional(readOnly = true)
	public Object queryDetailList(CmsBalanceGatherVO vo,DataTablesInput dataTablesInput){
		if(null != dataTablesInput){
			Map<String, Object> map = dataTablesInput.getFormdata();
			vo.setCorpId(EmptyUtils.isEmpty(map.get("corpId").toString()) ? null : map.get("corpId").toString());
			vo.setLoanSign(EmptyUtils.isEmpty(map.get("loanSign").toString()) ? null : map.get("loanSign").toString());
			vo.setCurId(EmptyUtils.isEmpty(map.get("curId").toString()) ? null : map.get("curId").toString());
			vo.setStartDate(EmptyUtils.isEmpty(map.get("startDate")) ? null : DateUtils.parse(map.get("startDate").toString(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
			vo.setEndDate(EmptyUtils.isEmpty(map.get("startDate")) ? null : DateUtils.parse(map.get("endDate").toString(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		}
		this.queryDetailParam(vo);//查询参数处理
		return cmsBalanceGatherDao.queryDetailList(vo,dataTablesInput);
	}
	
	
	
	
	/***
	 * 折线图
	 */
	@Transactional(readOnly = true)
	public Object createNetListLine(CmsBalanceGatherVO vo) {
		List<CmsBalanceGatherVO> dataList = null;
		if (!EmptyUtils.isEmpty(vo)) {
			dataList = cmsBalanceGatherDao.queryGatherList(vo);//数据库查询列表数据
		}
		
		//拿到图例
		Map<String, Object> datas = this.getLegendLine(dataList);
		List<Object> legendList = new ArrayList<Object>();
		if (!EmptyUtils.isEmpty(datas)) {
			for (Map.Entry<String, Object> entry : datas.entrySet()) {
					legendList.add(entry.getKey());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("legend", legendList);
		map.put("series", JsonUtils.toJSONString(datas.get("series")));
		return map;
	}

	private Map<String, Object> getLegendLine(List<CmsBalanceGatherVO> dataList) {
		Map<String, Object> lineMap = new TreeMap<String, Object>();
		
		//title
		
		//legend
		
		//xAxis
		
		//yAxis
		
		//series
		
		
		return lineMap;
	}
	
	@Transactional(readOnly = true)
	private void queryDetailParam(CmsBalanceGatherVO vo){
		if(EmptyUtils.isEmpty(vo)){
			vo = new CmsBalanceGatherVO();
		}
		
		if(EmptyUtils.isEmpty(vo.getStartDate())){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			vo.setStartDate(calendar.getTime());
		}
		
		if(EmptyUtils.isEmpty(vo.getEndDate())){
			vo.setEndDate(new Date());
		}
		
		if(EmptyUtils.isNotEmpty(vo.getCorpId()) && this.GATHER_CORP.equals(vo.getCorpId())){
			vo.setCorpId(null);
		}
		
		if(EmptyUtils.isNotEmpty(vo.getCurId()) && this.NATURAL_CUR.equals(vo.getCurId())){
			vo.setCurId(null);
		}
	}
	
	/**
	 * 默认查询当月
	 * @param vo
	 */
	@Transactional(readOnly = true)
	private void queryParam(CmsBalanceGatherVO vo) {
		if(EmptyUtils.isEmpty(vo)){
			vo = new CmsBalanceGatherVO();
		}
		
		if(EmptyUtils.isEmpty(vo.getStartDate())){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			vo.setStartDate(calendar.getTime());
		}
		
		if(EmptyUtils.isEmpty(vo.getEndDate())){
			vo.setEndDate(new Date());
		}
	}



	@Override
	public BaseDao getEntityDao() {
		return null;
	}
}
