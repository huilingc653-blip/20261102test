package com.bytter.biz.finacial.interest.controller;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.interest.service.FcAccuBalancefService;
import com.bytter.biz.finacial.interest.vo.FcAccuBalancefVo;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.vo.NormalExcelConstants;
import com.bytter.system.api.IFcCorpPoolService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title:利息管理-积数查询
 * @Description: FC_ACCU_BALANCE
 * @author zhengxn
 * @date 2017-06-28 14:27:04
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/interest/fcaccubalancef")
public class FcAccuBalancefController extends BaseController {
	@Autowired
	private FcAccuBalancefService fcAccuBalanceService;
	@DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
	/** 积数查询列表 页面跳转 **/
	@LogMessage(content = "查询FC_ACCU_BALANCE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "finacial/interest/listFcAccuBalancef";
	}
	/** 积数查询列表 table列表 **/
	@SuppressWarnings("unchecked")
	////@RequiresPermissions(FinacialResourceConstant.FINACIAL_ACCUBALANCEF_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return fcAccuBalanceService.listAll(dataTablesInput,FinacialResourceConstant.FINACIAL_ACCUBALANCEF_QUERY,getUserId(),false);
	}
	/** 利息管理调整积数表 ***/
	@GetMapping("/listf")
	public String listf(Model model) {
		return "finacial/interest/listFcAccuBalancea";
	}
	
	////@RequiresPermissions(FinacialResourceConstant.FINACIAL_ACCUBALANCEA_QUERY)
	@PostMapping("/datatablef")
	@ResponseBody
	public DataTablesOutput dataTablef(@RequestBody DataTablesInput dataTablesInput) {
		return fcAccuBalanceService.getAll(dataTablesInput,FinacialResourceConstant.FINACIAL_ACCUBALANCEA_QUERY,getUserId());
	}
	/** 利息管理调整积数表--保存 */
	////@RequiresPermissions(FinacialResourceConstant.FINACIAL_ACCUBALANCEA_SAVE)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson saves(@RequestParam("ids") String ids) {
		fcAccuBalanceService.saves(ids,getUserLoginName());
		return success();
	}
	/** 导出 **/
	@GetMapping("/excel")
	public String excel(ModelMap map, FcAccuBalancefVo vo) {
		List<FcAccuBalancefVo> list = fcAccuBalanceService.excel(vo,FinacialResourceConstant.FINACIAL_ACCUBALANCEF_QUERY,getUserId());
		map.put(NormalExcelConstants.FILE_NAME, "积数查询表");
		map.put(NormalExcelConstants.CLASS, FcAccuBalancefVo.class);
		map.put(NormalExcelConstants.PARAMS, new ExportParams("积数查询表", "", "积数查询表"));
		map.put(NormalExcelConstants.DATA_LIST, list);
		return NormalExcelConstants.EXCEL_VIEW;
	}
	/** 资金池积数查询列表 页面跳转 **/
	@LogMessage(content = "查询FC_ACCU_BALANCE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listPool")
	public String listPool(Model model) {
		return "finacial/interest/listFcAccuBalancePool";
	}
	
	
	/**资金池积数查询列表 table列表 **/
	@SuppressWarnings("unchecked")
	@PostMapping("/datatablePool")
	@ResponseBody
	public DataTablesOutput datatablePool(@RequestBody DataTablesInput dataTablesInput) {
		return fcAccuBalanceService.listAll(dataTablesInput,FinacialResourceConstant.FINACIAL_ACCUBALANCEPOOL_QUERY,getUserId(),true);
	}
	/**
	 * 获取资金池
	 * @return
	 */
	@RequestMapping(params = "getUserCorpPools")
	@ResponseBody
	public List<FcCorpPoolDTO> getUserCorpPools() {
		String resource = FinacialResourceConstant.FINACIAL_ACCUBALANCEPOOL_QUERY;
		return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
	}
	/**
	 * 获取资金池-子单位
	 * @param poolId
	 * @return
	 */
	@RequestMapping(params = "getUserCorpPoolChilds")
	@ResponseBody
	public List<FcCorpPoolDTO> getUserCorpPoolChilds(String poolId) {
		String resource = FinacialResourceConstant.FINACIAL_ACCUBALANCEPOOL_QUERY;
		return fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
	}
}
