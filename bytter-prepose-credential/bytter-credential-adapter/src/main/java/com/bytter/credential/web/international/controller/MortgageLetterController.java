package com.bytter.credential.web.international.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.entity.LcMortgage;
import com.bytter.credential.domain.international.service.ArrivalLetterService;
import com.bytter.credential.domain.international.service.MortgageLetterService;
import com.bytter.credential.domain.international.service.ReceiveLetterService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/***出口押汇**/
@Controller
@RequestMapping("/international/credit/mortgageletter")
public class MortgageLetterController extends BaseController {
	@Autowired 
	private ArrivalLetterService arrivalLetterService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@Autowired
	private MortgageLetterService mortgageLetterService;
	@Autowired
	private ReceiveLetterService receiveLetterService;
	@DubboReference(check = false)
	private ICorpService corpService;
	
	/**
	 * 申请抵押列表
	 */
	@GetMapping("/listMortgage")
	public String listMortgage(Model model) {
		return "international/credit/receive/listRegisterMortgage";
	}
	/**
	 * 功能介绍 :申请抵押列表<br>
	 * @author zhengxn
	 */
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return mortgageLetterService.queryMortgage(dataTablesInput, getCurrentUser());
	}
	/**
	 * 申请抵押-页面跳转
	 */
	@GetMapping("/add/{id}")
	public String add(@PathVariable("id") String id,Model model){
		LcBillArrival arrival = arrivalLetterService.get(id);
		model.addAttribute("arrival",arrival);
		return "international/credit/receive/addReceiveMortgage";
	}
	/**
	 * 申请抵押-页面跳转
	 */
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id,Model model){
		LcMortgage mortgage = mortgageLetterService.get(id);
		LcBillArrival arrival = arrivalLetterService.get(mortgage.getArrivalId());
		model.addAttribute("arrival",arrival);
		model.addAttribute("mortgage",mortgage);
		return "international/credit/receive/updateReceiveMortgage";
	}
	/**
	 * 申请抵押- 提交
	 */
	@LogMessage(content = "添加申请抵押",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/tempOrSubmit")
	@ResponseBody
	public AjaxJson tempOrSubmit(LcMortgage entity){
		mortgageLetterService.tempOrSubmit(entity,getCurrentUser());
		return success();
	}
	/**
	 * 抵押申请打回列表
	 */
	@GetMapping("/listBackMortgage")
	public String listBackMortgage(Model model) {
		return "international/credit/receive/listBackRegisterMortgage";
	}
	/**
	 * 功能介绍 :申请抵押打回列表<br>
	 * @author zhengxn
	 */
	@PostMapping("/backDatatable")
	@ResponseBody
	public DataTablesOutput ListbackDatatable(@RequestBody DataTablesInput dataTablesInput) {
		return mortgageLetterService.queryBackMortgage(dataTablesInput, getCurrentUser());
	}
	/**
	 * 申请抵押- 修改提交
	 * @param entity
	 * @return
	 */
	@LogMessage(content = "添加申请抵押",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(LcMortgage entity){
		mortgageLetterService.saveOrUpdate(entity,getCurrentUser());
		return success();
	}
	/**
	 * 取消抵押
	 * @param id 主键id
	 * @return
	 */
	@LogMessage(content = "取消抵押",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		mortgageLetterService.cancel(id,getCurrentUser(),-2);
		return success();
	}
	/**
	 * 注销抵押
	 * @param id 主键id
	 * @return
	 */
	@LogMessage(content = "注销抵押",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/cancel/{id}")
	@ResponseBody
	public AjaxJson cancel(@PathVariable("id") String id){
		mortgageLetterService.cancel(id,getCurrentUser(),-3);
		return success();
	}
	/**
	 * 抵押审批页面
	 */
	//@RequiresPermissions(ResourceConstant.CMS_PROVIDE_INFO_EDIT)
	@GetMapping("/mortgageApproveList")
	public String mortgageApproveList(String flowStatus,Model model) {
		model.addAttribute("flowStatus", flowStatus);
		return "international/credit/receive/approve/listMortgageApprove";
	}
	/**
	 * 功能介绍 :抵押审批页面table表<br>
	 * @author zhengxn
	 */
	//@RequiresPermissions(ResourceConstant.CMS_PROVIDE_DEFERRED_APPLY_QUERY)
	@PostMapping("/getDataInfoApproveTables")
	@ResponseBody
	public DataTablesOutput getDataInfoApproveTables(@RequestBody DataTablesInput dataTablesInput) {
		return mortgageLetterService.getDataInfoApproveTables(dataTablesInput);
	}
	/**
	 * 抵押审批 详情页面
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return
	 */
	@GetMapping("/infoApprove")
	public String infoApprove(String businessId, String taskId, Model model) {
		if(!EmptyUtils.isEmpty(businessId)){
			LcMortgage info = mortgageLetterService.get(businessId);
			model.addAttribute("info", info);
		}
		model.addAttribute("taskId", taskId);//必须要做此步骤
		return "international/credit/receive/approve/viewMortgageApprove";
	}
	/**
	 * 手机审批
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return
	 */
	@GetMapping("/mobileViewMortgageLetter")
	public String MobileViewMortgageLetter(String businessId, String taskId, Model model) {
		LcMortgage info = mortgageLetterService.get(businessId);
		LcBillArrival arrival = arrivalLetterService.get(info.getArrivalId());
		LcEnrol lcEnrol = receiveLetterService.get(arrival.getLcId());
		CorpDTO corp = corpService.get(arrival.getCorpId());
		BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(arrival.getCurId());
		model.addAttribute("info", info);
		model.addAttribute("corpName",corp.getName());
		model.addAttribute("curName",currencyDTO.getCurName());
		model.addAttribute("arrival", arrival);
		model.addAttribute("creditNo",lcEnrol.getCreditNo());
		model.addAttribute("taskId", taskId);//必须要做此步骤
		return "international/credit/receive/approve/mobileViewMortgageApprove";
	}
}
