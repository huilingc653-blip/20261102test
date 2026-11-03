package com.bytter.report.domain.netbank.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.report.domain.netbank.dao.BankAccBalTrendDao;
import com.bytter.report.domain.netbank.vo.BankAccBalTrendVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
 * 银行存款余额趋势Service
 *
 * @author zuoy 2018-12-05
 */
@Service("bankAccBalTrendService")
@Transactional
public class BankAccBalTrendService extends BaseService<BankAccBalTrendVO, String> {
	@Autowired
	private BankAccBalTrendDao bankAccBalTrendDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService currencyService;
	@Override
	public BaseDao<BankAccBalTrendVO, String> getEntityDao() {
		return bankAccBalTrendDao;
	}
	public List<BankAccBalTrendVO> listDataTables(String curId, String corpId, String seachBeginDate, String seachEndDate) {
//		Date beginDate = DateUtils.getFirstYearDate(new Date());
//		Date beginDate = DateUtils.getFirstMonthDate(DateUtils.parseDate(seachBeginDate));
		Date beginDate = DateUtils.getFirstYearDate(DateUtils.parseDate(seachEndDate));
		Date endDate = DateUtils.getLastMonthDate(DateUtils.parseDate(seachEndDate));
		Date curBalDate = DateUtils.parseDate(seachEndDate);
		CorpDTO corpDTO = corpService.get(corpId);
		corpDTO.setCurName(currencyService.getObjectById(corpDTO.getCurId()).getCurName());
		//查询余额
		List<BankAccBalTrendVO> list = this.bankAccBalTrendDao.listBankAccBalTrendVO(corpId, corpDTO.getCurId(), beginDate, endDate, curBalDate);
		if (EmptyUtils.isEmptyList(list)) {
			return new ArrayList<>();
		}
		// 查询预算额度
		Map<String, BigDecimal> collectBudgetMoney = this.bankAccBalTrendDao.collectFbsBudgetResolveYear(DateUtils.getFirstYearDate(beginDate), DateUtils.getLastYearDate(endDate));
		// 查询上一年余额
		Date lastBeginDate = DateUtils.getFirstYearDate(DateUtils.addYear(beginDate, -1));
		Date lastEndDate = DateUtils.getLastMonthDate(DateUtils.addYear(endDate, -1));
		List<BankAccBalTrendVO> lastList = this.bankAccBalTrendDao.listBankAccBalTrendVO(corpId, corpDTO.getCurId(), lastBeginDate, lastEndDate, DateUtils.addYear(curBalDate, -1));
		Map<String,BankAccBalTrendVO> map = new HashMap<>();
		int maxDate = DateUtils.getMonth(DateUtils.parseDate(seachEndDate));
		List<String> monthList = new ArrayList<>();
		for(int i = 1; i <= maxDate; i++){
			String top = "";
			if(i <= 9){
				top = "0" + i;
			}else{
				top = top + i;
			}
			monthList.add(top);
		}
		for (BankAccBalTrendVO vo : list) {
			map.put(vo.getMonth(),vo);
		}
		List<BankAccBalTrendVO> showList = new ArrayList<>();
		for(String mStr : monthList){
			BankAccBalTrendVO temp = map.get(mStr);
			if(temp == null){
				int mon = Integer.valueOf(mStr);
				String str = "";
				if(mon <= 9){
					str = "0";
				}
				BankAccBalTrendVO tempVo = new BankAccBalTrendVO();
				tempVo.setCorpId(corpDTO.getId());
				tempVo.setCorpName(corpDTO.getName());
				tempVo.setCurId(corpDTO.getCurId());
				tempVo.setCurName(corpDTO.getCurName());
				if(mon == 1){
					if(EmptyUtils.isEmpty(map.get(str+mon))){
						tempVo.setBalDate(beginDate);
					}else{
						BeanUtils.copyNotNullProperties(map.get(str+mon),tempVo);
						tempVo.setBalDate(DateUtils.getLastMonthDate(DateUtils.addMonth(map.get(str+mon).getBalDate(), 1)));
					}
				}else{
					if(EmptyUtils.isEmpty(map.get(str+(mon-1)))){
						tempVo.setBalDate(DateUtils.addMonth(beginDate,mon));
					}else{
						BeanUtils.copyNotNullProperties(map.get(str+(mon-1)),tempVo);
						tempVo.setBalDate(DateUtils.getLastMonthDate(DateUtils.addMonth(map.get(str+(mon-1)).getBalDate(), 1)));
					}
				}
				tempVo.setMonth(str+mon);
				map.put(tempVo.getMonth(),tempVo);
				list.add(tempVo);
				showList.add(tempVo);
			}else{
				showList.add(temp);
			}
		}
		for (BankAccBalTrendVO vo : showList) {
			if (EmptyUtils.isEmpty(collectBudgetMoney)) {
				vo.setBudgetMoney(BigDecimal.ZERO);
			} else {
				vo.setBudgetMoney(collectBudgetMoney.get(vo.getCorpId() + "_" + vo.getMonth()));
			}
			if (EmptyUtils.isEmptyList(lastList)) {
				vo.setLastBal(BigDecimal.ZERO);
			} else {
				for (BankAccBalTrendVO lastVO : lastList) {
					if (vo.getMonth().equals(lastVO.getMonth()) && vo.getCorpId().equals(lastVO.getCorpId()) && vo.getCurId().equals(lastVO.getCurId())) {
						vo.setLastBal(lastVO.getBal());
					}
				}
			}
		}
		return showList;
	}
	/**
	 * 银行存款余额数据返回
	 *
	 * @param bankAccBal
	 * @return
	 */
	public Object getBankAccBal(List<BankAccBalTrendVO> bankAccBal) {
		Object jsonObject = dealData(bankAccBal);
		return jsonObject;
	}
	private Object dealData(List<BankAccBalTrendVO> bankAccBal) {
		List<EchartsData> echartsDatas = formatData(bankAccBal);
		return createBar("银行存款余额趋势", "", getData(bankAccBal), "", "", echartsDatas);
	}
	/**
	 * 图表数据
	 *
	 * @param bankAccBal
	 * @return
	 */
	private List<EchartsData> formatData(List<BankAccBalTrendVO> bankAccBal) {
		List<EchartsData> mergeList = new ArrayList<>();
		EchartsData data = new EchartsData();
		data.setName("余额");
		List<Object> list = new ArrayList<Object>();
		Map<Integer, BankAccBalTrendVO> dateMap = new HashMap<Integer, BankAccBalTrendVO>();
		for (BankAccBalTrendVO vo : bankAccBal) {
			//int month = getMonthByDate(vo.getBalDate());
			int month = DateUtils.getMonth(vo.getBalDate());
			BankAccBalTrendVO bankAccBalTemp = dateMap.get(month);
			if (EmptyUtils.isEmpty(bankAccBalTemp)) {
				bankAccBalTemp = new BankAccBalTrendVO();
				bankAccBalTemp.setBal(BigDecimal.ZERO);
				dateMap.put(month, vo);
			}
			BigDecimal e = vo.getBal() == null ? BigDecimal.ZERO : vo.getBal();
			BigDecimal f = bankAccBalTemp.getBal() == null ? BigDecimal.ZERO : bankAccBalTemp.getBal();
			bankAccBalTemp.setBal(f.add(e));
		}
		Iterator<Map.Entry<Integer, BankAccBalTrendVO>> it = dateMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, BankAccBalTrendVO> entry = it.next();
			list.add(entry.getValue().getBal() == null ? 0 : entry.getValue().getBal());
		}
		data.setData(list);
		mergeList.add(data);
		EchartsData data1 = new EchartsData();
		list = new ArrayList<Object>();
		data1.setName("预算额度");
		dateMap = new HashMap<Integer, BankAccBalTrendVO>();
		for (BankAccBalTrendVO vo : bankAccBal) {
			int month = DateUtils.getMonth(vo.getBalDate());
			BankAccBalTrendVO bankAccBalTemp = dateMap.get(month);
			if (EmptyUtils.isEmpty(bankAccBalTemp)) {
				bankAccBalTemp = new BankAccBalTrendVO();
				bankAccBalTemp.setBudgetMoney(BigDecimal.ZERO);
				dateMap.put(month, vo);
			}
			BigDecimal a = vo.getBudgetMoney() == null ? BigDecimal.ZERO : vo.getBudgetMoney();
			BigDecimal b = bankAccBalTemp.getBudgetMoney() == null ? BigDecimal.ZERO : bankAccBalTemp.getBudgetMoney();
			bankAccBalTemp.setBudgetMoney(b.add(a));
		}
		Iterator<Map.Entry<Integer, BankAccBalTrendVO>> itb = dateMap.entrySet().iterator();
		while (itb.hasNext()) {
			Map.Entry<Integer, BankAccBalTrendVO> entry = itb.next();
			list.add(entry.getValue().getBudgetMoney() == null ? 0 : entry.getValue().getBudgetMoney());
		}
		data1.setData(list);
		mergeList.add(data1);
		EchartsData data2 = new EchartsData();
		list = new ArrayList<Object>();
		data2.setName("上期余额");
		dateMap = new HashMap<Integer, BankAccBalTrendVO>();
		for (BankAccBalTrendVO vo : bankAccBal) {
			int month = DateUtils.getMonth(vo.getBalDate());
			BankAccBalTrendVO bankAccBalTemp = dateMap.get(month);
			if (EmptyUtils.isEmpty(bankAccBalTemp)) {
				bankAccBalTemp = new BankAccBalTrendVO();
				bankAccBalTemp.setLastBal(BigDecimal.ZERO);
				dateMap.put(month, vo);
			}
			BigDecimal c = vo.getLastBal() == null ? BigDecimal.ZERO : vo.getLastBal();
			BigDecimal d = bankAccBalTemp.getLastBal() == null ? BigDecimal.ZERO : bankAccBalTemp.getLastBal();
			bankAccBalTemp.setLastBal(d.add(c));
		}
		Iterator<Map.Entry<Integer, BankAccBalTrendVO>> itl = dateMap.entrySet().iterator();
		while (itl.hasNext()) {
			Map.Entry<Integer, BankAccBalTrendVO> entry = itl.next();
			list.add(entry.getValue().getLastBal() == null ? 0 : entry.getValue().getLastBal());
		}
		data2.setData(list);
		mergeList.add(data2);
		return mergeList;
	}
	public List<Object> getData(List<BankAccBalTrendVO> bankAccBal) {
		List<Object> mergeList = new ArrayList<>();
		Map<Integer, BankAccBalTrendVO> dateMap = new HashMap<Integer, BankAccBalTrendVO>();
		List<Integer> listMonth = new ArrayList<>();
		for (BankAccBalTrendVO vo : bankAccBal) {
			int month = DateUtils.getMonth(vo.getBalDate());
			BankAccBalTrendVO bankAccBalTemp = dateMap.get(month);
			if (EmptyUtils.isEmpty(bankAccBalTemp)) {
				int mo = DateUtils.getMonth(vo.getBalDate());
				listMonth.add(mo);
				dateMap.put(month, vo);
				Collections.sort(listMonth);
			}
		}
		for (Integer mon : listMonth) {
			String monthTemp = "";
			if (mon < 10) {
				monthTemp = "0" + mon;
			} else {
				monthTemp = String.valueOf(mon);
			}
			String moneyStr = getMonth(monthTemp);
			mergeList.add(moneyStr);
		}
		return mergeList;
	}
	/**
	 * 根据月份设置对应月份值
	 *
	 * @param month
	 * @return
	 */
	public String getMonth(String month) {
		String monthStr = "";
		switch (month) {
			case "01":
				monthStr = "一月";
				break;
			case "02":
				monthStr = "二月";
				break;
			case "03":
				monthStr = "三月";
				break;
			case "04":
				monthStr = "四月";
				break;
			case "05":
				monthStr = "五月";
				break;
			case "06":
				monthStr = "六月";
				break;
			case "07":
				monthStr = "七月";
				break;
			case "08":
				monthStr = "八月";
				break;
			case "09":
				monthStr = "九月";
				break;
			case "10":
				monthStr = "十月";
				break;
			case "11":
				monthStr = "十一月";
				break;
			case "12":
				monthStr = "十二月";
				break;
			default:
				break;
		}
		return monthStr;
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
	public static String createBar(String text, String xAxisName, List<Object> xData, String yAxisName, String yAxisUnit, List<EchartsData> dataList) {
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
/*		Toolbox toolbox = new Toolbox();
		toolbox.show(isShow).feature(Tool.mark, Tool.dataView,new DataZoom().show(false), new MagicType(Magic.line).show(true), Tool.restore, Tool.saveAsImage);
		option.setToolbox(toolbox);*/
		//设置图例
		Legend legendBean = new Legend("余额", "预算额度", "上期余额");
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
}
