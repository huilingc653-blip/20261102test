package com.bytter.tally.web.controller;

import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.FcAccPoolDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.tally.service.FcBankAccDtlService;
import com.bytter.tally.vo.FcAccbookPageVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller   资金池记账
 * @Description: BIS_ACC_DTL
 * @author
 * @date 2019-5-13
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/bankCollect/fcbankaccdtl")
public class FcBankAccDtlController extends BaseController {
	@Autowired
	private FcBankAccDtlService fcBankAccDtlService;
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
	@DubboReference(check = false)
	private IFcCorpPoolService iFcCorpPoolService;
	@LogMessage(content = "查询BT_NET_CHALK列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{type}")
	public String list(@PathVariable("type") String type,Model model) {
		model.addAttribute("opType", type);
		return "finacial/bankcollect/fcbankaccdtl/listBankAccDtl";
	}
	@GetMapping("/manualPage")
	public String manualPage(String opType, String menuType, Model model) {
		model.addAttribute("opType", opType);
		model.addAttribute("menuType", menuType);
		return "finacial/bankcollect/fcbankaccdtl/" + getForwardByType(menuType);
	}
	/**
	 * 根据类型返回不同的跳转页面
	 */
	public String getForwardByType(String menuType){
		/** Init-记账 Edit-改账 Check-复核*/
		String forward = "";
		if("table".equals(menuType)){
			forward = "bankAccManualTab";
		}else if("down".equals(menuType)){
			forward = "bankAccDown";
		}else {
			forward = "bankAccUp";
		}
		return forward;
	}
	/**
	 * 列表数据
	 * */
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput queryBisAccDtl(@RequestBody  DataTablesInput input, String type,String menuType){
		return fcBankAccDtlService.queryBisAccDtl(input,type,menuType,getCurrentUser());
	}
	@PostMapping("/processAccbook")
	@ResponseBody
	public FcAccbookPageVO processAccbook(String key, String poolId, String curId, String type, String menuType){
		return fcBankAccDtlService.createAccbook(key,poolId,curId,type,menuType,getCurrentUser());
	}
	/**
	 * 暂存FC_ACCBOOK
	 */
	@LogMessage(content = "暂存FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcAccbookVoucherDTO fcAccbook, String key,String menuType,String accDtlId) {
		fcBankAccDtlService.save(fcAccbook, key,menuType,accDtlId, getCurrentUser().getId());
		return success();
	}
	/**
	 * 提交FC_ACCBOOK
	 */
	@LogMessage(content = "提交FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(FcAccbookVoucherDTO fcAccbook, String key,String menuType,String accDtlId) {
		fcBankAccDtlService.submit(fcAccbook, key,menuType,accDtlId, getCurrentUser());
		return success();
	}
	/**
	 * 复核FC_ACCBOOK
	 */
	@LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/check")
	@ResponseBody
	public AjaxJson check(FcAccbookVoucherDTO fcAccbook, String key,String menuType) {
		fcBankAccDtlService.createCheckVoucher(fcAccbook, key,getCurrentUser(),menuType);
		return success();
	}
	/**
	 * 打回FC_ACCBOOK
	 */
	@LogMessage(content = "打回FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(FcAccbookVoucherDTO fcAccbook, String key,String menuType) {
		fcBankAccDtlService.backFcAccbook(fcAccbook, key,menuType,getCurrentUser());
		return success();
	}
	/**
	 * 查询有权限的内部帐号
	 */
	@PostMapping("/getFcAcc")
	@ResponseBody
	public List<FcAccPoolDTO> getFcAcc(String curId, String poolId, String corpId, String subjectId) {
		List<FcAccPoolDTO> list = innerAccountService.listAccountByBankParamsForPool(curId,poolId,corpId,subjectId,"",getUserId());
		return EmptyUtils.isEmptyList(list)? new ArrayList<FcAccPoolDTO>() : list;
	}
	/**
	 * 获取资金池主体
	 * @return
	 */
	@GetMapping("/getCorpPools")
	@ResponseBody
	public List<FcCorpPoolDTO> getUserCorpPools() {
		String resource = "fcl:pool:tally:apply";
		return iFcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
	}
}
