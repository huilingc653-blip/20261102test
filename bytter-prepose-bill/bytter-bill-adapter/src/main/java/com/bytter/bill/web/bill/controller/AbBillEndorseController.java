package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillEndorseService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 票据背书
 * @author onlineGenerator
 * @date 2017-07-05 17:53:54
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillendorse")
public class AbBillEndorseController extends BaseController {
	@Autowired
	private AbBillEndorseService abBillEndorseService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 票据背书列表 页面跳转
	 *
	 * @return
	 */
	// @LogMessage(content = "查询票据背书列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(HttpServletRequest request, Model model) {
		// 系统参数 是否挂资金计划
		String budgetSel = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET)
				.getValue();
		model.addAttribute("budgetSel", budgetSel);
		//默认挑票模式为否
		String isSelectBill=request.getParameter("isSelectBill");
		if(StringUtils.isEmpty(isSelectBill)){
			model.addAttribute("isSelectBill", "0");
		}else{
			model.addAttribute("isSelectBill", isSelectBill);
		}
		model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
		model.addAttribute("isCounterpartEndorse", abVoucherinfoViceService.isCounterpartEndorse());
		model.addAttribute("isRepeatEndorse", abVoucherinfoViceService.isRepeatEndorse());
		if ("1".equals(budgetSel)) {
			// 系统参数 背书挂接资金计划科目
			model.addAttribute("endorseItem", abConfigService
					.getBillConfig(AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue());
		}
		return "bill/accebill/listAbBillEndorse";
	}
	/**
	 * 票据背书 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillEndorseService.listabBillEndorse(dataTablesInput);
	}
	@LogMessage(content = "背书申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/EndorsementApplication")
	@ResponseBody
	public AjaxJson EndorsementApplication(AbBillEndorseVo eo) {
		abBillEndorseService.EndorsementApplication(eo, AccebillConstant.BILL_OPERATE_SUBMIT, this.getCurrentUser());
		return success();
	}
	/**
	 * 跳转至审核列表
	 * 
	 * @return String
	 */
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus, String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "bill/accebill/approve/listAbBillEndorseApprove";
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
		return abBillEndorseService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 * 
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String businessId, String taskId, Model model) {
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		AbBillEndorseVo info = new AbBillEndorseVo();
		if (StringUtils.isNotEmpty(businessId)) {
			info = abBillEndorseService.getAbBillEndorseVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
			abBillAttachmentService.viewAttachment(businessId, model);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()):null);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		// 背书挂接资金计划
		// 根据流水表预算科目是否为空来显示挂预算列表
		// 系统参数 背书挂接资金计划科目
		model.addAttribute("endorseItem", abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue());
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_ENDORSE);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		return "bill/accebill/approve/approveAbBillEndorse";
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
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		AbBillEndorseVo info = new AbBillEndorseVo();
		if (StringUtils.isNotEmpty(businessId)) {
			info = abBillEndorseService.getAbBillEndorseVoById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		// 背书挂接资金计划
		// 根据流水表预算科目是否为空来显示挂预算列表
		// 系统参数 背书挂接资金计划科目
		model.addAttribute("endorseItem", abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_ENDORSEITEM).getValue());
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_ENDORSE);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
	/**
	 * 保存导入数据
	 * 
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<AbBillEndorseVo> list) {
		this.abBillEndorseService.saveImport(list, this.getCurrentUser());
		return success();
	}
	
	
	/**
	 * 背书验证票据重复背书方法
	 * @param ids
	 * @param origNos
	 * @param endorseeName
	 * @param endorseeBankNo
	 * @param repartMsg
	 * @return
	 */
	@PostMapping("/checkRepeatEndorse")
	@ResponseBody
    public Object checkRepeatEndorse(String ids,String origNos,
			String endorseeName,String endorseeBankNo,String repartMsg){
		 Object jsonObj = this.abBillEndorseService.checkRepeatEndorse(ids, origNos, endorseeName, endorseeBankNo,repartMsg);
		 return jsonObj;
	}
	@PostMapping("/getBankTypeId")
	@ResponseBody
	public Object getBankTypeId(String bankCode,Integer interiorApply,String pledgeeAcc){
		Object jsonObj = this.abBillEndorseService.getBankTypeId(bankCode,interiorApply,pledgeeAcc);
		return jsonObj;
	}
}
