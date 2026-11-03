package com.bytter.cashVoucher.controller;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.cashVoucher.entity.BvVouEmployInfo;
import com.bytter.cashVoucher.service.BvVouEmployInfoService;
import com.bytter.cashVoucher.service.BvVouInfoDtlService;
import com.bytter.cashVoucher.vo.BvVouInfoDtlVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**   
 * @Title: Controller  
 * @Description: 凭证使用
 * @author onlineGenerator
 * @date 2017-06-09 09:06:03
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/blankvou/bvvouemployinfo")
public class BvVouEmployInfoController extends BaseController {
	@Autowired
	private BvVouEmployInfoService bvVouEmployInfoService;
	@Autowired
	private BvVouInfoDtlService bvVouInfoDtlService;
	/**
	* 凭证使用新增页面跳转
	*
	* @return
	*/
	//@RequiresPermissions("blankvou:bvvouemployinfo:save")
	@GetMapping("/add/{id}")
	public String addVouEmployInfo(@PathVariable("id") String id, BvVouEmployInfo bvVouEmployInfo, Model model) {
		BvVouInfoDtlVo vo = bvVouInfoDtlService.getDataTablesRow(id);
		model.addAttribute("bvVouEmployInfoPage", vo);
		return "bill/blankvou/addBvVouEmployInfo";
	}
	
	/**
	 * 添加凭证使用
	 * @param bvVouEmployInfo
	 * @return
	 */
	//@RequiresPermissions("blankvou:bvvouemployinfo:save")
	@LogMessage(content = "添加凭证使用",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson saveVouEmployInfo(BvVouEmployInfo bvVouEmployInfo){
		boolean flag = false;
		if(bvVouEmployInfoService.save(bvVouEmployInfo,BlankvouConstant.BVVOUEMPLOYINFO_DATASOURCE_MANUAL,this.getCurrentUser())){
			flag = bvVouInfoDtlService.operateUpdate(bvVouEmployInfo.getVoudtlId(),null,BlankvouConstant.BVVOUINFODTL_STATUS_USE,this.getCurrentUser());
		}
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(flag);
		return ajaxJson;
	}
	
	
	
}
