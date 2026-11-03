package com.bytter.biz.finacial.outaccbook.controller;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.service.TallyService;
import com.bytter.biz.finacial.outaccbook.vo.AccBookVO;
import com.bytter.biz.finacial.outaccbook.vo.TallyOutAccbookVO;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterStatus;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**   
 * @Title: 记账控制器  
 * @Description: 表外流水账主档表
 * @author onlineGenerator
 * @date 2017-06-30 11:04:08
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/outaccbook/tally")
public class TallyController extends BaseController {
	@Autowired
	private TallyService tallyService;
	/**
	* 表外记账 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.FCL_OUTERP_TALLY_QUERY)
	@LogMessage(content = "查询表外记账列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTally")
	public String listTally(Model model) {
        return "finacial/outaccbook/listTally";
	}
    /**
    * 表外流水账主档表 table列表
    *
    * @return
    */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALL_QUERY)
    @PostMapping("/dataTableTally")
    @ResponseBody
    public DataTablesOutput dataTableTally(@RequestBody  DataTablesInput dataTablesInput){
		return tallyService.queryOutAccbookData(dataTablesInput, FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALL_QUERY, getCurrentUser());
    }
	/**
	* 表外流水账主档表新增页面跳转
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
	@GetMapping("/addTally")
	public String addTally(FcOutAccbook fcOutAccbook, Model model) {
		if (StringUtils.isNotEmpty(fcOutAccbook.getId())) {
			fcOutAccbook = tallyService.get(fcOutAccbook.getId());
			model.addAttribute("fcOutAccbookPage", fcOutAccbook);
		}
		return "finacial/outaccbook/addTally";
	}
	/**
	* 表外流水账主档表编辑页面跳转
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
	@GetMapping("/updateTally/{id}")
	public String updateTally(@PathVariable("id") String id, Model model) {
		FcOutAccbook fcOutAccbook = null;
		if (StringUtils.isNotEmpty(id)) {
			fcOutAccbook = tallyService.get(id);
			model.addAttribute("fcOutAccbookPage", fcOutAccbook);
		}
		model.addAttribute("STATUE_TALLY_WAIT_CHECK", OuterStatus.STATUE_TALLY_WAIT_CHECK.get());
		return "finacial/outaccbook/updateTally";
	}
	
	/**
	 * 
	 * 功能介绍 :修改 查询明细列表 <br>	
	 * @return
	 * <b>日期：</b>2017-7-6<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	@PostMapping("/queryUpdateDTL/{id}")
	@ResponseBody
	public List<TallyOutAccbookVO> queryUpdateDTL(@PathVariable("id") String id){
		List<TallyOutAccbookVO> list = new ArrayList<TallyOutAccbookVO>();
		if (StringUtils.isNotEmpty(id)) {
			list = tallyService.queryOutDtlById(id);
		}
		return list;
	}
	
	/**
	* 表外流水账主档表详情页
	*
	* @return
	*/
	////@RequiresPermissions("accbook:fcoutaccbook:view")
	@LogMessage(content = "查询表外流水账主档表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewTally/{id}")
	public String viewTally(@PathVariable("id") String id, Model model) {
		FcOutAccbook fcOutAccbook = tallyService.get(id);
		List<TallyOutAccbookVO> listDtl = tallyService.queryOutDtlById(id);
		model.addAttribute("fcOutAccbookPage", fcOutAccbook);
		model.addAttribute("listDtl", listDtl);
		return "finacial/outaccbook/viewTally";
	}
	/**
	 * 暂存表外流水账主档表
	 * @param fcOutAccbook
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
	@LogMessage(content = "暂存表外流水账主档表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/tempTally")
	@ResponseBody
	public AjaxJson tempTally(AccBookVO vo){
		tallyService.tempAccbook(vo, getCurrentUser());
		return success();
	}
	/**
	* 提交表外流水账主档表
	* @param fcOutAccbook
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
	@LogMessage(content = "提交表外流水账主档表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/submitTally")
	@ResponseBody
	public AjaxJson submitTally(AccBookVO vo){
		tallyService.submitAccbook(vo, getCurrentUser());
		return  success();
	}
	/**
	* 删除表外流水账主档表
	* @param id
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
	@LogMessage(content = "删除表外流水账主档表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/deleteTally/{id}")
	@ResponseBody
	public AjaxJson deleteTally(@PathVariable("id") String id){
		tallyService.delOutAccbook(id, getCurrentUser());
		return success();
	}
	
	
	/**
	* 记账复核 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.FCL_OUTERP_TALLY_QUERY)
	@LogMessage(content = "查询记账复核列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTallyCheck")
	public String listTallyCheck(Model model) {
        return "finacial/outaccbook/listTallyCheck";
	}
    /**
    * 记账复核  table列表
    * @return
    */
	////@RequiresPermissions(ResourceConstant.FCL_OUTERP_TALLY_QUERY)
    @PostMapping("/dataTableTallyCheck")
    @ResponseBody
    public DataTablesOutput dataTableTallyCheck(@RequestBody  DataTablesInput dataTablesInput){
		return tallyService.queryTableTallyCheckData(dataTablesInput, getCurrentUser());
    }
    
    /**
	* 记账复核详细 页面跳转
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
	@LogMessage(content = "记账复核页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/tallyCheck/{id}")
	public String tallyCheck(@PathVariable("id") String id,Model model) {
		FcOutAccbook fcOutAccbook = tallyService.get(id);
		List<TallyOutAccbookVO> listDtl = tallyService.queryOutDtlById(id);
		model.addAttribute("fcOutAccbookPage", fcOutAccbook);
		model.addAttribute("listDtl", listDtl);
        return "finacial/outaccbook/approveTallyCheck";
	}
    
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
    @LogMessage(content = "记账复核通过",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/tallyCheckAdopt")
	@ResponseBody
	public AjaxJson tallyCheckAdopt(String id){
    	tallyService.tallyCheck(id, getCurrentUser());
		return  success();
	}
    
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
    @LogMessage(content = "批量记账复核通过",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchTallyCheckAdopt")
	@ResponseBody
	public AjaxJson batchTallyCheckAdopt(@RequestParam("ids[]") List<String> ids){
    	tallyService.batchTallyCheck(ids, getCurrentUser());
		return  success();
	}
    
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
    @LogMessage(content = "记账复核打回",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/tallyCheckBack")
	@ResponseBody
	public AjaxJson tallyCheckBack(String id,String backReason){
    	tallyService.tallyBack(id,backReason, getCurrentUser());
		return  success();
	}
    
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_TALLY_APPLY)
    @LogMessage(content = "批量记账复核打回",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchTallyCheckBack")
	@ResponseBody
	public AjaxJson batchTallyCheckBack(@RequestParam("ids[]") List<String> ids){
    	tallyService.batchTallyBack(ids, getCurrentUser());
		return  success();
	}
    
    /**
	* 批量打回原因页面
	*/
	////@RequiresPermissions("accbook:fcoutaccbook:index")
	@LogMessage(content = "填写批量打回",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/batchTallyCheckBackCause")
	public String batchTallyCheckBackCause(Model model) {
        return "finacial/outaccbook/backCause";
	}
	
	/**
	 * 查询表外账户 
	 */
	@PostMapping("/getOutAccTypeAccs")
	@ResponseBody
	public List<FcAccDTO> getOutAccTypeAccs() {
    	return tallyService.getOuterTallyAccount(getCurrentUser());
    }
}
