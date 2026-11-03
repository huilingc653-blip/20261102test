package com.bytter.report.web.stock.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.api.constant.CapitalStockConstant;
import com.bytter.report.domain.stock.service.CapitalStockService;
import com.bytter.report.domain.stock.vo.CapitalStockVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
/**
 * 资金存量报表Controller
 *
 * @author zuoy 2017-10-25
 */
@Controller
@RequestMapping("/bi/capitalStock")
public class CapitalStockController extends BaseController {
	@Autowired
	private CapitalStockService capitalStockService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	@DubboReference(check = false)
	private ICorpService corpService;
		@GetMapping("/listNet")
	public String listNet(Model model) {
		this.setTypeToDate(model);
		BtCurrencyDTO btCurrencyDTO = btCurrencyService.getNatualCurrency();
		if (!EmptyUtils.isEmpty(btCurrencyDTO)) {
			model.addAttribute("curId", btCurrencyDTO.getId());
		}
		model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_NET);
		return "stock/listCapitalStockNet";
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
		return "stock/listCapitalStockNetApp";
	}
		@GetMapping("/listCorp")
	public String listCorp(Model model, CapitalStockVO capitalStockVO,String beginDate,String endDate) {
		model.addAttribute("netId", capitalStockVO.getNetId());
		model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_CORP);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		this.setAttribute(model, capitalStockVO);
		this.setTypeToDate(model);
		return "stock/listCapitalStockCorp";
	}
		@GetMapping("/listBankType")
	public String listBankType(Model model, CapitalStockVO capitalStockVO,String beginDate,String endDate) {
		model.addAttribute("corpId", capitalStockVO.getCorpId());
		model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_BANK_ACC);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		this.setAttribute(model, capitalStockVO);
		this.setTypeToDate(model);
		return "stock/listCapitalStockBankType";
	}
		@GetMapping("/listDtl")
	public String listDtl(Model model, CapitalStockVO capitalStockVO,String beginDate,String endDate) {
		model.addAttribute("corpId", capitalStockVO.getCorpId());
		model.addAttribute("bankTypeId", capitalStockVO.getBankTypeId());
		model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_DTL);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		this.setAttribute(model, capitalStockVO);
		return "stock/listCapitalStockDtl";
	}
	/**
	 * 应收票据流水明细
	 *
	 * @param model
	 * @param capitalStockVO
	 * @return
	 */
	@GetMapping("/listAbBillDtl")
	public String listAbBillDtl(Model model, CapitalStockVO capitalStockVO,String beginDate,String endDate) {
		model.addAttribute("corpId", capitalStockVO.getCorpId());
		model.addAttribute("bankTypeId", capitalStockVO.getBankTypeId());
		model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_ABBILLDTL);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
		this.setAttribute(model, capitalStockVO);
		return "stock/listCapitalStockAbBillDtl";
	}
	@GetMapping("/listFreeze")
	public String listFreeze(Model model, CapitalStockVO capitalStockVO,String beginDate,String endDate) {
		model.addAttribute("corpId", capitalStockVO.getCorpId());
		model.addAttribute("bankTypeId", capitalStockVO.getBankTypeId());
		model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_FREEZE);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
		this.setAttribute(model, capitalStockVO);
		return "stock/listCapitalStockFreeze";
	}
	/**
	 * 应收票据占用明细
	 *
	 * @param model
	 * @param capitalStockVO
	 * @return
	 */
	@GetMapping("/listAbBillFreeze")
	public String listAbBillFreeze(Model model, CapitalStockVO capitalStockVO,String beginDate,String endDate) {
		model.addAttribute("corpId", capitalStockVO.getCorpId());
		model.addAttribute("bankTypeId", capitalStockVO.getBankTypeId());
		model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_ABBILLFREEZE);
		this.setAttribute(model, capitalStockVO);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		return "stock/listCapitalStockAbBillFreeze";
	}
	/**
	 * NIS_ALLOCATION_REQ table列表
	 *
	 * @return DataTablesOutput
	 */
	@SuppressWarnings("rawtypes")
		@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		String reduce = (String) dataTablesInput.getFormdata().get("reducedCurName");
		if(StringUtils.isNotBlank(reduce)){
			dataTablesInput.getFormdata().put("reducedCurId",reduce);
		}
		return this.capitalStockService.getDataTablesUnion(dataTablesInput,this.getCurrentUser());
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
	private void setAttribute(Model model, CapitalStockVO capitalStockVO) {
		model.addAttribute("dateType", capitalStockVO.getDateType());
		model.addAttribute("moneyUnit", capitalStockVO.getMoneyUnit());
		model.addAttribute("curId", capitalStockVO.getCurId());
		model.addAttribute("stockType", capitalStockVO.getStockType());
		model.addAttribute("reducedCurId", capitalStockVO.getReducedCurId());
		BigDecimal reta = this.fcExcrateAdjustService.getExchangeRateBy(capitalStockVO.getCurId(), DateUtils.getDate());
		BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();
		if (!EmptyUtils.isEmpty(natualCurrency) && capitalStockVO.getCurId().equalsIgnoreCase(natualCurrency.getId())) {
			model.addAttribute("rate", "");
		} else if (EmptyUtils.isEmpty(reta)) {
			model.addAttribute("rate", BigDecimal.ZERO);
		} else {
			model.addAttribute("rate", reta);
		}
	}
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
	/**
	 * 创建资金存量网点汇总表（柱状图）
	 *
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/createNetBar")
	@ResponseBody
	public Object createNetBar(CapitalStockVO capitalStockVO) {
		return this.capitalStockService.createNetBar(capitalStockVO,this.getCurrentUser());
	}
	/**
	 * 创建资金存量网点汇总表
	 *
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/getWidgetBal")
	@ResponseBody
	public Object getWidgetBal(CapitalStockVO capitalStockVO) {
		return this.capitalStockService.getWidgetBal(capitalStockVO);
	}
	@PostMapping("/widgetCapitalStock")
	public String widgetCapitalStock(Model model) {
		this.setTypeToDate(model);
		BtCurrencyDTO btCurrencyDTO = btCurrencyService.getNatualCurrency();
		if (!EmptyUtils.isEmpty(btCurrencyDTO)) {
			model.addAttribute("curId", btCurrencyDTO.getId());
		}
		ICurrentUser user = this.getCurrentUser();
		CorpDTO corp = corpService.get(user.getCorpId());
		if (CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {
			model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_NET);
		} else {
			model.addAttribute("netId", corp.getNetId());
			model.addAttribute("reportType", CapitalStockConstant.REPORT_TYPE_CORP);
		}
		model.addAttribute("curList", this.btCurrencyService.getBtCurrency());
		return "stock/widgetCapitalStock";
	}
	/**
	 * 获取首页图形报表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableWidget")
	@ResponseBody
	public DataTablesOutput dataTableWidget(@RequestBody DataTablesInput dataTablesInput) {
		return this.capitalStockService.getDataTablesWidget(dataTablesInput);
	}
	/**
	 * 获取首页列表数据
	 *
	 * @return
	 */
	@PostMapping("/listByWidget")
	@ResponseBody
	public Object listByWidget(String curId) {
		return this.capitalStockService.listDataTablesWidget(curId);
	}
	@PostMapping("/getCapitalStock")
	@ResponseBody
	public Object getCapitalStock(CapitalStockVO capitalStockVO) {
		DataTablesInput input = new DataTablesInput();
		Map<String,Object> maps = new HashMap<>();
		maps.put("reportType","net");
		maps.put("dateType",capitalStockVO.getDateType());
		maps.put("moneyUnit",capitalStockVO.getMoneyUnit());
		maps.put("stockType",capitalStockVO.getStockType());
		maps.put("curId",capitalStockVO.getCurId());
		input.getFormdata().putAll(maps);
		DataTablesOutput out =  this.capitalStockService.getDataTablesUnion(input,this.getCurrentUser());
		List<CapitalStockVO> tempOutList = out.getData();
		Map<String, Object> outMap = new HashMap<>();
		if (!EmptyUtils.isEmpty(capitalStockVO.getNetId())){
			for (CapitalStockVO vo : tempOutList){
				if (capitalStockVO.getNetId().equals(vo.getNetId())){
					outMap.put(vo.getStockTypeName(),vo);
				}
			}
		}
		return outMap;
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
		return "stock/listCapitalStockNetAppMoblie";
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
		return "stock/listCapitalStockNetAppMoblie";
	}
	/**
	 * 资金存量表数据
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
	@GetMapping("/json")
	@ResponseBody
	public List<CorpDTO> json() {
		String resource = ResourceConstant.BI_STOCK_CAPITALSTOCK_QUERY;
		//获取有权限的单位
		List<String> corpList = corpService.getUserCorpIds(resource, this.getCurrentUser().getId());
		List<CorpDTO> netList = new ArrayList<>();
		Map<String,CorpDTO> map = new HashMap<>();
		for(String id : corpList){
			CorpDTO dto = corpService.getNetByCorpId(id);
			if(!map.containsKey(dto.getId())){
				netList.add(dto);
				map.put(dto.getId(),dto);
			}
		}
		return netList;
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
