package com.bytter.cashVoucher.controller;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.cashVoucher.entity.BvLog;
import com.bytter.cashVoucher.service.BvLogService;
import com.bytter.cashVoucher.service.BvVouEmployInfoService;
import com.bytter.cashVoucher.service.BvVouInfoDtlService;
import com.bytter.cashVoucher.vo.BvVouEmployInfoVo;
import com.bytter.cashVoucher.vo.BvVouInfoDtlVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**   
 * @Title: Controller  
 * @Description: 凭证入库明细表
 * @author onlineGenerator
 * @date 2017-06-07 15:42:20
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/blankvou/bvvouinfodtl")
public class BvVouInfoDtlController extends BaseController {
	@Autowired
	private BvVouInfoDtlService bvVouInfoDtlService;
	@Autowired
	private BvVouEmployInfoService bvVouEmployInfoService;
	@Autowired
	private BvLogService bvLogService;
	/**
	* 凭证入库明细表列表 页面跳转
	*
	* @return
	*/
	@GetMapping("/list")
	public String list(Model model) {
        return "bill/blankvou/listBvVouInfoDtl";
	}
    /**
    * 凭证入库明细表 table列表
    * @return
    */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询凭证入库明细表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput input){
		return bvVouInfoDtlService.getDataTablesUnion(input,this.getCurrentUser());
    }
	
	/**
	* 凭证入库明细表详情页
	*
	* @return
	*/
	@LogMessage(content = "查询凭证入库明细表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BvVouInfoDtlVo vo = bvVouInfoDtlService.getDataTablesRow(id);
		BvVouEmployInfoVo bvVouEmployInfoVo = bvVouEmployInfoService.getDataByVoudtlId(id);
		if(bvVouEmployInfoVo != null){
			//已使用
			model.addAttribute("isUse", "true");
			model.addAttribute("bvVouEmployInfoVo", bvVouEmployInfoVo);
		}
		List<BvLog> bvlogList = bvLogService.listBvLog(vo.getId());
		model.addAttribute("bvlogList", bvlogList);
		model.addAttribute("bvVouInfoDtlPage", vo);
		return "bill/blankvou/viewBvVouInfoDtl";
	}
	
	/**
	* 凭证入库明细退票页面跳转
	*
	* @return
	*/
	@GetMapping("/retreatPage/{id}")
	public String retreatPage(@PathVariable("id") String id,Model model) {
		model.addAttribute("id", id);
		model.addAttribute("submit", "retreat");
		return "bill/blankvou/retreatOrTovoidBvVouInfoDtl";
	}
	
	/**
	* 凭证入库明细批量退票页面跳转
	*
	* @return
	*/
	@GetMapping("/batchRetreatPage/{ids}")
	public String batchRetreatPage(@PathVariable("ids") String ids,Model model) {
		model.addAttribute("ids", ids);
		model.addAttribute("submit", "batchRetreat");
		return "bill/blankvou/retreatOrTovoidBvVouInfoDtl";
	}
	
	/**
	* 凭证入库明细作废页面跳转
	*
	* @return
	*/
	@GetMapping("/tovoidPage/{id}")
	public String retreatOrTovoid(@PathVariable("id") String id,Model model) {
		model.addAttribute("id", id);
		model.addAttribute("submit", "tovoid");
		return "bill/blankvou/retreatOrTovoidBvVouInfoDtl";
	}
	
	/**
	* 凭证入库明细批量作废页面跳转
	*
	* @return
	*/
	@GetMapping("/batchTovoidPage/{ids}")
	public String batchTovoidPage(@PathVariable("ids") String ids,Model model) {
		model.addAttribute("ids", ids);
		model.addAttribute("submit", "batchTovoid");
		return "bill/blankvou/retreatOrTovoidBvVouInfoDtl";
	}
	
	/**
	* 空白凭证-出售
	* @param id
	* @return
	*/
	@LogMessage(content = "凭证入库明细-出售",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/sell/{id}")
	@ResponseBody
	public AjaxJson sell(@PathVariable("id") String id){
		boolean flag = bvVouInfoDtlService.operateUpdate(id,null,BlankvouConstant.BVVOUINFODTL_STATUS_SELL,this.getCurrentUser());
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	
	/**
	* 空白凭证-批量出售
	* @param ids
	* @return
	*/
	@LogMessage(content = "凭证入库明细-批量出售",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchSell")
	@ResponseBody
	public AjaxJson batchSell(String ids){
		boolean flag = bvVouInfoDtlService.batchOperateUpdate(ids,null,BlankvouConstant.BVVOUINFODTL_STATUS_SELL,this.getCurrentUser());
		success().setSuccess(flag);
		return success();
	}
	
	/**
	* 空白凭证-退票
	* @param id
	* @return
	*/
	@LogMessage(content = "凭证入库明细-退票",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/retreat/{id}")
	@ResponseBody
	public AjaxJson retreat(@PathVariable("id") String id,String reason){
		boolean flag = bvVouInfoDtlService.operateUpdate(id,reason,BlankvouConstant.BVVOUINFODTL_STATUS_RETREAT,this.getCurrentUser());
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	
	/**
	* 空白凭证-批量退票
	* @param ids
	* @return
	*/
	@LogMessage(content = "凭证入库明细-批量退票",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchRetreat/{ids}")
	@ResponseBody
	public AjaxJson batchRetreat(@PathVariable("ids") String ids,String reason){
		boolean flag = bvVouInfoDtlService.batchOperateUpdate(ids,reason,BlankvouConstant.BVVOUINFODTL_STATUS_RETREAT,this.getCurrentUser());
		success().setSuccess(flag);
		return success();
	}
	
	/**
	* 空白凭证-丢失
	* @param id
	* @return
	*/
	@LogMessage(content = "凭证入库明细-丢失",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/lose/{id}")
	@ResponseBody
	public AjaxJson lose(@PathVariable("id") String id){
		boolean flag = bvVouInfoDtlService.operateUpdate(id,null,BlankvouConstant.BVVOUINFODTL_STATUS_LOSE,this.getCurrentUser());
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	
	/**
	* 空白凭证-批量丢失
	* @param ids
	* @return
	*/
	@LogMessage(content = "凭证入库明细-批量丢失",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchLose")
	@ResponseBody
	public AjaxJson batchLose(String ids,String reason){
		boolean flag = bvVouInfoDtlService.batchOperateUpdate(ids,null,BlankvouConstant.BVVOUINFODTL_STATUS_LOSE,this.getCurrentUser());
		success().setSuccess(flag);
		return success();
	}
	
	/**
	* 空白凭证-作废
	* @param id
	* @return
	*/
	@LogMessage(content = "凭证入库明细-作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/tovoid/{id}")
	@ResponseBody
	public AjaxJson tovoid(@PathVariable("id") String id,String reason){
		boolean flag = bvVouInfoDtlService.operateUpdate(id,reason,BlankvouConstant.BVVOUINFODTL_STATUS_TOVOID,this.getCurrentUser());
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	
	/**
	* 空白凭证-批量作废
	* @param ids
	* @return
	*/
	@LogMessage(content = "凭证入库明细-批量作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchTovoid/{ids}")
	@ResponseBody
	public AjaxJson batchTovoid(@PathVariable("ids") String ids,String reason){
		boolean flag = bvVouInfoDtlService.batchOperateUpdate(ids,reason,BlankvouConstant.BVVOUINFODTL_STATUS_TOVOID,this.getCurrentUser());
		success().setSuccess(flag);
		return success();
	}
	
	/**
	* 空白凭证-批量移库
	* @param ids
	* @return
	*/
	@LogMessage(content = "凭证入库明细-批量移库",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchTransf/{sboxId}")
	@ResponseBody
	public AjaxJson batchTransf(@PathVariable("sboxId") String sboxId,String ids){
		boolean flag = bvVouInfoDtlService.batchTransf(ids,sboxId,this.getCurrentUser());
		success().setSuccess(flag);
		return success();
	}
    
    /**
	* 凭证查询 页面跳转
	*
	* @return
	*/
	@GetMapping("/queryList")
	public String queryList(Model model) {
        return "bill/blankvou/queryListBvVouInfoDtl";
	}
	/**
    * 凭证查询
    * @return
    */
	@SuppressWarnings("rawtypes")
    @LogMessage(content = "凭证查询",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @PostMapping("/queryBvVouInfoDtl")
    @ResponseBody
    public DataTablesOutput queryList(@RequestBody  DataTablesInput input){
		return bvVouInfoDtlService.queryList(input,this.getCurrentUser());
    }
    
    
    
}
