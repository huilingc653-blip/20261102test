package com.bytter.biz.fundwarn.controller;
import com.bytter.biz.fundwarn.service.BtWarnDataDtlService;
import com.bytter.biz.fundwarn.service.BtWarnDataService;
import com.bytter.biz.fundwarn.util.BtFundWarnUtils;
import com.bytter.biz.fundwarn.vo.HomeWarnDataSearchVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Description: 预警策略主数据信息控制器类
 * @author zhengg
 * @date 2018-10-15
 */
@Controller
@RequestMapping("/fundwarn/btwarndata")
public class BtWarnDataInfoController extends BaseController {
	
	@Autowired
	private BtWarnDataService btWarnDataService;
	@Autowired
	private BtWarnDataDtlService btWarnDataDtlService;
	
	/**
	 * 预警策略主数据信息列表页面跳转
	 * @return
	 */
	@LogMessage(content = "预警策略主数据信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
//	@RequiresPermissions(FundWarnConstant.RESOURCE_WARNSDATA_QUERY)
	public String list(Model model) {
		model.addAttribute("typecodeDimensionMap", BtFundWarnUtils.getAllTypecodeDimension());
		return "fundwarn/listBtWarnData";
	}
	
	/**
	 * 加载预警策略主数据信息列表数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return btWarnDataService.listWarnData(dataTablesInput);
	}
	/**
	 * 预警策略明细数据信息列表页面跳转
	 * @return
	 */
	@LogMessage(content = "预警策略明细数据信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listDtl")
//	@RequiresPermissions(FundWarnConstant.RESOURCE_WARNSDATA_QUERY)
	public String listDtl(String id,String typeCode,String dimension,Model model) {
		model.addAttribute("id", id);
		model.addAttribute("typeCode", typeCode);
		model.addAttribute("dimension", dimension);
		return "fundwarn/listBtWarnDataDtl";
	}
	/**
	 * 加载预警策略明细数据信息列表数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datadtltable")
	@ResponseBody
	public DataTablesOutput dataDtlTable(@RequestBody DataTablesInput dataTablesInput) {
		return btWarnDataDtlService.listWarnDataDtl(dataTablesInput);
	}
	
	/**
	 * 根据预警类型获和预警维度取 预警数据主信息列表头信息
	 * @return
	 */
	@RequestMapping("/warnDataHeader")
	@ResponseBody
	public List<Map<String,String>> warnDataHeader(String typeCode,String dimension){
		return BtFundWarnUtils.getWarndataHeader(typeCode, dimension);
	}
	
	/**
	 * 根据预警类型获和预警维度取 预警数据明细信息列表头信息
	 * @return
	 */
	@RequestMapping("/warnDataHeaderDtl")
	@ResponseBody
	public List<Map<String,String>> warnDataHeaderDtl(String typeCode,String dimension){
		return BtFundWarnUtils.getWarndataheaderDtl(typeCode, dimension);
	}
	/**
	 * 指标预警（首页）
	 * @return
	 */
	@LogMessage(content = "指标预警（首页）", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/homeWarnData")
	public String homeWarnData(Model model) {
		model.addAttribute("beginDate", DateUtils.format(DateUtils.addDay(DateUtils.getDateYMD(), -7), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		model.addAttribute("endDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return "fundwarn/homeWarnDataReport";
	}
	/**
	 * 指标预警统计图
	 * @param searchVO
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/createWarnDataBar")
	@ResponseBody
	public Object createWarnDataBar(HomeWarnDataSearchVO searchVO) throws IOException {
		return this.btWarnDataService.createWarnDataBar(searchVO, this.getCurrentUser());
	}
	/**
	 * 获取预警统计数量
	 *
	 * @return
	 */
	@GetMapping("/getWarnDataCount")
	@ResponseBody
	public Object getWarnDataCount() {
		Integer count = btWarnDataService.getWarnDataCountByUserId(this.getCurrentUser().getId());
		return count.toString();
	}
}
