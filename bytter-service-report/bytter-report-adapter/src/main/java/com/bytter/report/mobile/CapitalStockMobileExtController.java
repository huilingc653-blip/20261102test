package com.bytter.report.mobile;

import com.bytter.report.domain.stock.service.CapitalStockService;
import com.bytter.report.domain.stock.vo.CapitalStockVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.api.constant.CapitalStockConstant;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金存量报表Controller
 *
 * @author zuoy 2017-10-25
 */
@Controller
@RequestMapping("/mobile/bi/capitalStock")
public class CapitalStockMobileExtController extends BaseController {
	@Autowired
	private CapitalStockService capitalStockService;
	@Autowired(required=false)
	private IBtCurrencyService btCurrencyService;
	@Autowired(required=false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;


	/**
	 * 设置日期类型对应的日期
	 *
	 * @param model
	 */
	private void setTypeToDate(Model model) {
		Map<String, String> map = new HashMap<>();
		map.put("day", DateUtils.date_sdf().format(DateUtils.getDate()));
		map.put("week", DateUtils.date_sdf().format(DateUtils.getFirstWorkday()) + "~" + DateUtils.date_sdf().format(DateUtils.getLastWorkday()));
		map.put("month", DateUtils.date_sdf().format(DateUtils.getMonthStartTime()) + "~" + DateUtils.date_sdf().format(DateUtils.getMonthEndTime()));
		map.put("season", DateUtils.date_sdf().format(DateUtils.getQuarterStartTime()) + "~" + DateUtils.date_sdf().format(DateUtils.getQuarterEndTime()));
		map.put("year", DateUtils.date_sdf().format(DateUtils.getCurrYearFirst()) + "~" + DateUtils.date_sdf().format(DateUtils.getCurrYearLast()));
		try {
			ObjectMapper mapper = new ObjectMapper();
			model.addAttribute("typeToDate", mapper.writeValueAsString(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@PostMapping("/listNetApp")
	public String listNetApp(Model model,String dateType,String moneyUnit,String stockType,String netId,String curId){
		this.setTypeToDate(model);
		if (EmptyUtils.isEmpty(dateType)){
			dateType = "day";
		}
		if (EmptyUtils.isEmpty(moneyUnit)){
			moneyUnit = "1";
		}
		if (EmptyUtils.isEmpty(stockType)){
			stockType = "";
		}
		if (EmptyUtils.isEmpty(curId)){
			curId = btCurrencyService.getNatualCurrency().getId();
		}
		model.addAttribute("currentCorp",corpService.get(netId));
		model.addAttribute("dateType",dateType);
		model.addAttribute("moneyUnit",moneyUnit);
		model.addAttribute("stockType",stockType);
		model.addAttribute("netId",netId);
		BtCurrencyDTO curDto = btCurrencyService.getObjectById(curId);
		model.addAttribute("curDto",curDto);
		return "mobile/bi/stock/listCapitalStockNetApp";
	}

	/**
	 * 创建资金调拨汇总表（柱状图）
	 *
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/createCaptialStockSumBar")
	@ResponseBody
	public Object createCaptialStockSumBar(CapitalStockVO cap,DataTablesInput input,Model model){
		createQueryParam(cap,input,model);
		return capitalStockService.createCaptialStockSumBar(input,this.getCurrentUser());
	}

	private DataTablesInput createQueryParam(CapitalStockVO cap,DataTablesInput input,Model model){
		if(StringUtils.isNotEmpty(cap.getDateType())){
			input.getFormdata().put("dateType",cap.getDateType());
			model.addAttribute("dateType", cap.getDateType());
		}
		if(StringUtils.isNotEmpty(cap.getStockType())){
			input.getFormdata().put("stockType",cap.getStockType());
//			input.addFilter(MatchTypeEnum.EQ, "stockType", cap.getStockType());
			model.addAttribute("stockType", cap.getStockType());
		}
		if(StringUtils.isNotEmpty(cap.getCurId())){
			input.getFormdata().put("curId",cap.getCurId());
//			input.addFilter(MatchTypeEnum.EQ, "curId", cap.getCurId());
			model.addAttribute("curId", cap.getCurId());
		}

		if(StringUtils.isNotEmpty(cap.getNetId())){
			input.getFormdata().put("netId",cap.getNetId());
			model.addAttribute("netId", cap.getNetId());
		}

		if(StringUtils.isNotEmpty(cap.getUnit())){
			input.getFormdata().put("unit",cap.getUnit());
			model.addAttribute("unit", cap.getUnit());
		}

		if(StringUtils.isNotEmpty(cap.getReducedCurId())){
			input.getFormdata().put("reducedCurId",cap.getReducedCurId());
			model.addAttribute("reducedCurId", cap.getReducedCurId());
		}
		if(StringUtils.isNotEmpty(cap.getReportType())){
			input.getFormdata().put( "reportType", cap.getReportType());
			model.addAttribute("reportType", cap.getReportType());
		}else{
			input.getFormdata().put( "reportType", "net");
			model.addAttribute("reportType", "net");
		}
		return input;
	}

	/**
	 * 设置日期类型对应的日期
	 *
	 * @param model
	 */
	private void setAppTypeToDate(Model model) {
		Map<String, String> map = new HashMap<>();
		map.put("day", DateUtils.getSDFormat("yyyy.MM.dd").format(DateUtils.getDate()));
		map.put("week", DateUtils.getSDFormat("yyyy.MM.dd").format(DateUtils.getFirstWorkday()) + "~" + DateUtils.getSDFormat("MM.dd").format(DateUtils.getLastWorkday()));
		map.put("month", DateUtils.getSDFormat("yyyy.MM.dd").format(DateUtils.getMonthStartTime()) + "~" + DateUtils.getSDFormat("MM.dd").format(DateUtils.getMonthEndTime()));
		map.put("season", DateUtils.getSDFormat("yyyy.MM.dd").format(DateUtils.getQuarterStartTime()) + "~" + DateUtils.getSDFormat("MM.dd").format(DateUtils.getQuarterEndTime()));
		map.put("year", DateUtils.getSDFormat("yyyy.MM.dd").format(DateUtils.getCurrYearFirst()) + "~" + DateUtils.getSDFormat("MM.dd").format(DateUtils.getCurrYearLast()));
		try {
			ObjectMapper mapper = new ObjectMapper();
			model.addAttribute("typeToDate", mapper.writeValueAsString(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@RequestMapping("/listNetMoblie")
	public String listNetMoblie(Model model,String netId,String corpId){
		//日期处理
		this.setAppTypeToDate(model);
		String natualId = btCurrencyService.getNatualCurrency().getId();
		model.addAttribute("currentCorp",corpService.get(netId));
		model.addAttribute("netId","");
		model.addAttribute("corpId","");
		model.addAttribute("natualId",natualId);
		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
		model.addAttribute("curList", curList);
		Date newDate = DateUtils.getDate();
		String newDateStr = DateUtils.format(newDate,"yyyy-MM-dd");
		model.addAttribute("newDate",newDateStr);
		model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_NET);
//		CorpDTO corpDTO = corpService.get(corpId);
//		if (CorpConstant.CORP_TYPE_CODE_NET.equals(corpDTO.getType())) {
//			model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_NET);
//		} else {
//			model.addAttribute("netId", corpDTO.getNetId());
//			model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_CORP);
//		}
		return "mobile/bi/stock/listCapitalStockNetAppMoblie";
	}

	@GetMapping("/listNetMoblie1")
	public String listNetMoblie1(Model model,String netId,String corpId){
		//日期处理
		this.setAppTypeToDate(model);
		String NatualId = btCurrencyService.getNatualCurrency().getId();
		model.addAttribute("currentCorp",corpService.get(netId));
		model.addAttribute("netId",netId);
		model.addAttribute("corpId",corpId);
		model.addAttribute("NatualId",NatualId);
		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
		model.addAttribute("curList", curList);
		CorpDTO corpDTO = corpService.get(corpId);
		if (CorpConstant.CORP_TYPE_CODE_NET.equals(corpDTO.getType())) {
			model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_NET);
		} else {
			model.addAttribute("netId", corpDTO.getNetId());
			model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_CORP);
		}
		return "mobile/bi/stock/listCapitalStockNetAppMoblie";
	}


	/**
	 * 资金存量表数据
	 * 【资金存量汇总】
	 */
	@PostMapping("/getInfoList")
	@ResponseBody
	public Object getInfoList(Model model, CapitalStockVO cap,String beginDate, String endDate) {
		DataTablesInput input = new DataTablesInput();
		input.setLength(9999999);
		DataTablesInput queryParam = createQueryParam(cap, input, model);
		queryParam.getFormdata().put("beginDate",beginDate);
		queryParam.getFormdata().put("endDate",endDate);
		DataTablesOutput data = this.capitalStockService.getDataTablesUnion(queryParam,this.getCurrentUser());
		List<CapitalStockVO> capList = data.getData();
		return capList;
	}

	/**
	 * 资金存量表数据
	 */
	@GetMapping("/getInfoList1")
	@ResponseBody
	public Object getInfoList1(Model model, CapitalStockVO cap) {
		DataTablesInput input = new DataTablesInput();
		input.setLength(9999999);
		DataTablesInput queryParam = createQueryParam(cap, input, model);
		DataTablesOutput data = this.capitalStockService.getDataTablesUnion(queryParam,this.getCurrentUser());
		List<CapitalStockVO> capList = data.getData();
		return capList;
	}

	@PostMapping("/getDate")
	@ResponseBody
	public Map<String,String> getDate(String dateType){
		Map<String,String> date = new HashMap<>();
		Date beginDate = null;
		Date endDate = null;
		Date newDate = DateUtils.getDate();
		if(CapitalStockConstant.DATE_TYPE_DAY.equals(dateType)){
			beginDate = newDate;
			endDate = newDate;
		}else if(CapitalStockConstant.DATE_TYPE_WEEK.equals(dateType)){
			beginDate = DateUtils.getFirstWeekDate(newDate);
			endDate = DateUtils.getLastWeekDate(newDate);
		}else if(CapitalStockConstant.DATE_TYPE_MONTH.equals(dateType)){
			beginDate = DateUtils.getFirstMonthDate(newDate);
			endDate = DateUtils.getLastMonthDate(newDate);
		}else if(CapitalStockConstant.DATE_TYPE_SEASON.equals(dateType)){
			beginDate = DateUtils.getFirstQuarterDate(newDate);
			endDate = DateUtils.getLastQuarterDate(newDate);
		}else if(CapitalStockConstant.DATE_TYPE_YEAR.equals(dateType)){
			beginDate = DateUtils.getFirstYearDate(newDate);
			endDate = DateUtils.getLastYearDate(newDate);
		}
		date.put("beginDate",DateUtils.format(beginDate,"yyyy-MM-dd"));
		date.put("endDate",DateUtils.format(endDate,"yyyy-MM-dd"));
		return date;
	}

}
