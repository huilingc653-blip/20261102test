package com.bytter.biz.bankacc.controller;
import com.bytter.biz.inneraccount.service.BtBankAccAppService;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号申请表
 * @date 2017-05-23 17:37:39
 */
@Controller
@RequestMapping("/bankacc/btbankaccapp")
public class BtBankAccAppController extends BaseController {
	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;
	@Autowired
	private BtBankAccAppService bankAccAppService;
	@DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;//变接口
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IBankTypeService bankTypeService;
	/**
	 * 银行账号申请表列表 页面跳转
	 *
	 * @returnadd
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_QUERY)
	@LogMessage(content = "查询银行账号申请表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bankacc/listBtBankAccApp";
	}
	/**
	 * 银行账号申请表 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return bankAccAppService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 银行账号申请表新增页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@GetMapping("/add")
	public String add(BtBankAccAppDTO btBankAccApp, Model model) {
		if (StringUtils.isNotEmpty(btBankAccApp.getId())) {
			btBankAccApp = btBankAccAppService.getById(btBankAccApp.getId());
			model.addAttribute("btBankAccAppPage", btBankAccApp);
		}
		/*String bankAccDesc = configService.getConfig(ConfigConstant.BANKACC, "bankAccDesc").getValue();
		model.addAttribute("bankAccDesc", bankAccDesc);*/
		return "bankacc/addBtBankAccApp";
	}
	/**
	 * 银行账号申请表编辑页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@GetMapping("/update")
	public String update(BtBankAccAppDTO btBankAccApp, Model model) {
		if (StringUtils.isNotEmpty(btBankAccApp.getId())) {
			btBankAccApp = btBankAccAppService.getBankAccApp(btBankAccApp.getId());
			model.addAttribute("curId", btBankAccApp.getCurId());
			model.addAttribute("btBankAccAppPage", btBankAccApp);
			String bankAccDesc = configService.getConfig(ConfigConstant.BANKACC, "bankAccDesc").getValue();
			model.addAttribute("bankAccDesc", bankAccDesc);
			//银行类别对应的境内外标识
			BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccApp.getBankTypeId());
			model.addAttribute("isDomesticBankType", bankTypeDTO==null ? "":bankTypeDTO.getIsDomesticBank());
		}
		return "bankacc/updateBtBankAccApp";
	}
	/**
	 * 银行账号申请表详情页
	 *
	 * @return
	 */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_QUERY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACC_QUERY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_QUERY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACCREVHANDLE_QUERY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_QUERY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACCDELHANDLE_QUERY},
//			logical = Logical.OR)
	@LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BtBankAccAppDTO btBankAccApp = btBankAccAppService.getBankAccApp(id);
		if (StringUtil.isNotEmpty(btBankAccApp.getEntityAccId())) {
			BtBankAccDTO btBankAcc = btBankAccService.getBtBankAcc(btBankAccApp.getEntityAccId());
			model.addAttribute("btBankAcc", btBankAcc);
		}
		model.addAttribute("btBankAccAppPage", btBankAccApp);
		model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_APP);
		//银行类别对应的境内外标识
		BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccApp.getBankTypeId());
		model.addAttribute("isDomesticBankType", bankTypeDTO==null ? "":bankTypeDTO.getIsDomesticBank());
		if("2".equals(btBankAccApp.getBusType())){
			return "bankacc/viewBtBankAccRev";
		}else if("3".equals(btBankAccApp.getBusType())){
			return "bankacc/viewBtBankAccDel";
		}
		return "bankacc/viewBtBankAccApp";
	}
	/**
	 * 添加银行账号申请表
	 *
	 * @param btBankAccApp
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@LogMessage(content = "添加银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtBankAccAppDTO btBankAccApp) {
		if (BtBankAccConstant.ACCTYPE_02.equals(btBankAccApp.getAccType())) {
			btBankAccApp.setRegularDate(BudgetConstant.getRegularsDate(btBankAccApp.getRegulars()));
		}
		bankAccAppService.toSave(btBankAccApp, this.getCurrentUser());
		return success();
	}
	/**
	 * 修改银行账号申请表
	 *
	 * @param btBankAccApp
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BtBankAccAppDTO btBankAccApp) {
		if (BtBankAccConstant.ACCTYPE_02.equals(btBankAccApp.getAccType())) {
			btBankAccApp.setRegularDate(BudgetConstant.getRegularsDate(btBankAccApp.getRegulars()));
		}
		bankAccAppService.toModify(btBankAccApp, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除银行账号申请表
	 *
	 * @param id
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@LogMessage(content = "删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		BtBankAccAppDTO btBankAccApp = btBankAccAppService.getById(id);
		bankAccAppService.toDelte(btBankAccApp, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除银行账号申请表
	 *
	 * @param ids
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@LogMessage(content = "批量删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
		bankAccAppService.toAllDelte(ids, this.getCurrentUser());
		return success();
	}
	/**
	 * 跳转至审核列表
	 *
	 * @param type 业务类型
	 * @return String
	 */
	@GetMapping("/listApprove/{type}")
	public String listApprove(@PathVariable Integer type, String flowStatus, String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		model.addAttribute("busType", type);
		return "bankacc/listBtBankAccApprove";
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
		return this.bankAccAppService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String id, String taskId, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			BtBankAccAppDTO btBankAccApp = btBankAccAppService.getBankAccApp(id);
			BtBankAccDTO acc = btBankAccCommonService.getBtBankAccVOById(btBankAccApp.getEntityAccId());
			model.addAttribute("btBankAcc", acc);
			model.addAttribute("btBankAccAppPage", btBankAccApp);
			model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_APP);
			model.addAttribute("taskId", taskId);
		}
		return "bankacc/approveBtBankAcc";
	}
	/**
	 * 银行账号申请表详情页
	 *
	 * @return
	 */
	@LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/showOldBankAcc/{corpId}")
	public String showOldBankAcc(@PathVariable("corpId") String corpId, Model model) {
		model.addAttribute("corpId", corpId);
		return "bankacc/listBtBankAccOld";
	}
	/**
	 * 跳转至手机审核详情界面
	 *
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			BtBankAccAppDTO btBankAccApp = btBankAccAppService.getBankAccApp(businessId);
			BtBankAccDTO acc = btBankAccCommonService.getBtBankAccVOById(btBankAccApp.getEntityAccId());
			model.addAttribute("btBankAcc", acc);
			model.addAttribute("btBankAccAppPage", btBankAccApp);
			model.addAttribute("taskId", taskId);
		}
		return "bankacc/viewApproveBtBankAcc";
	}
}
