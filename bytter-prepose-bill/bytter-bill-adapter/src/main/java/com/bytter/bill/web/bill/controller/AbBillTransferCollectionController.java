package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillTransferCollectionService;
import com.bytter.bill.domain.bill.vo.AbBillTransferCollectionVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 票据归集
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbilltransfercollection")
public class AbBillTransferCollectionController extends BaseController {
	@Autowired
	private AbBillTransferCollectionService abBillTransferCollectionService;
	/**
	 * 应收-票据划拨-票据归集列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model, String netId) {
		model.addAttribute("netId", netId);
		return "bill/accebill/listabbilltransfercollection";
	}
	/**
	 * 应收-票据划拨-票据归集 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询可票据归集的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return abBillTransferCollectionService.getDataTablesUnion(input);
	}
	/**
	 * 票据归集
	 * 
	 * @param abBillTransferCollectionVo
	 * @return
	 */
	@LogMessage(content = "票据归集", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/transfercollection")
	@ResponseBody
	public AjaxJson transfercollection(AbBillTransferCollectionVo abBillTransferCollectionVo) {
		abBillTransferCollectionService.transfercollection(abBillTransferCollectionVo, this.getCurrentUser());
		return success();
	}
	/**
	 * 根据网点id查询银行账号信息
	 * 
	 * @param netId
	 *            网点id
	 * @return
	 */
	@GetMapping("/getCorpIdByNetId")
	@ResponseBody
	public List<CorpDTO> getCorpIdByNetId(String netId) {
		return abBillTransferCollectionService.getCorpIdByNetId(netId);
	}
	/**
	 * 根据网点id查询直属下级单位的银行账号信息
	 * 
	 * @param netId
	 *            网点id
	 * @return
	 */
	@GetMapping("/getBankAccByNetId")
	@ResponseBody
	public List<BtBankAccDTO> getBankAccByNetId(String netId) {
		return abBillTransferCollectionService.getBankAccByNetId(netId);
	}
	/**
	 * 根据网点id查询网点的银行账号信息
	 * 
	 * @param netId
	 *            网点id
	 * @return
	 */
	@GetMapping("/getNetBankAccByNetId")
	@ResponseBody
	public List<BtBankAccDTO> getNetBankAccByNetId(String netId) {
		return abBillTransferCollectionService.getNetBankAccByNetId(netId);
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
		return "bill/accebill/approve/listAbBillTransferCollectionApprove";
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
		return abBillTransferCollectionService.dataTableApprove(dataTablesInput);
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
			AbBillTransferCollectionVo vo = abBillTransferCollectionService
					.getAbBillTransferCollectionVoById(businessId);
			model.addAttribute("info", vo);
			model.addAttribute("bankTypeName", vo.getBankTypeName());
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/approve/approveAbbilltransfercollection";
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
			AbBillTransferCollectionVo vo = abBillTransferCollectionService
					.getAbBillTransferCollectionVoById(businessId);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
