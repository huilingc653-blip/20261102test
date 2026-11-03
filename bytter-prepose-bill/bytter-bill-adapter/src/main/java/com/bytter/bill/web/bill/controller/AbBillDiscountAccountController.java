package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillDiscountAccountService;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: Controller
 * @Description:贴现到账Controller
 * @author onlineGenerator
 * @date 2017-7-21 13:49:49
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abBillDiscountAccount")
public class AbBillDiscountAccountController extends BaseController {
	@Autowired
	private AbBillDiscountAccountService abBillDiscountAccountService;
	@Autowired
	private AbCorpService abCorpService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	/**
	 * 贴现到账页面跳转
	 * 
	 * @return
	 */
	// @LogMessage(content = "贴现到账页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillDiscountAccount";
	}
	/**
	 * 贴现到账页面跳转
	 * 用于收款管理
	 *
	 * @return
	 */
	@LogMessage(content = "贴现到账页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listDiscountAccount")
	public String abBillDiscountAccountPage(AbVoucherinfoViceVO reVo, String row, Model model) {
		if(StringUtils.isNotEmpty(reVo.getCorpId())){
			reVo.setCorpName(abCorpService.get(reVo.getCorpId()).getName());
		}
		model.addAttribute("billInfo", reVo);
		model.addAttribute("row", row);
		BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(reVo.getId());
		if(StringUtils.isNotEmpty(bisAccDtlDTO)){
			// 获取交易日期
			model.addAttribute("businessDate", DateUtils.date2Str(bisAccDtlDTO.getTransTime() ,DateUtils.date_sdf()));
			// 获取交易金额
			model.addAttribute("billMoney", bisAccDtlDTO.getAmt());
		}
		return "bill/accebill/discountAccountPage";
	}
	/**
	 * 贴现到账列表
	 * 
	 * @param dataTablesInput
	 * @param isApply 是否含有审批中数据 0表示不含
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String isApply) {
		return abBillDiscountAccountService.listAbBillDiscountAccount(dataTablesInput, isApply);
	}
	/**
	 * 贴现到账提交
	 * 
	 * @param abBillDiscountAccountVO
	 * @return
	 */
	@LogMessage(content = "批量提交贴现到账信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmitAbBillDiscountAccount(AbBillDiscountAccountVO abBillDiscountAccountVO) {
		AjaxJson j = success();
		boolean flag = abBillDiscountAccountService.batchSubmitAbBillDiscountAccount(abBillDiscountAccountVO,
				this.getCurrentUser());
		j.setSuccess(flag);
		return j;
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
		return "bill/accebill/approve/listAbBillDiscountAccountApprove";
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
		// http://192.168.0.198:8888/newBTFP/bill/accebill/abBillDiscountAccount/listApprove
		return abBillDiscountAccountService.dataTableApprove(dataTablesInput);
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
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillDiscountAccountVO info = abBillDiscountAccountService.getAbBillDiscountAccountVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("bankTypeName", info.getBankTypeName());
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/approve/approveAbBillDiscountAccount";
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
			AbBillDiscountAccountVO info = abBillDiscountAccountService.getAbBillDiscountAccountVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
