package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillTransferDownManageService;
import com.bytter.bill.domain.bill.service.AbBtCurrencyService;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownManageVo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: Controller
 * @Description: 票据下拨
 * @author onlineGenerator
 * @date 2017-07-19 09:43:45
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbilltransferdownmanage")
public class AbBillTransferDownManageController extends BaseController {
	@Autowired
	private AbBillTransferDownManageService abBillTransferDownManageService;
	@Autowired
	private AbBtCurrencyService abBtCurrencyService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	/**
	 * 票据下拨单列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model, String netId) {
//		if (StringUtils.isEmpty(netId)) {
//			// 下拨中心默认为顶级网点
//			List<CorpDTO> list = corpService.listNet();
//			for (CorpDTO lis : list) {
//				if (StringUtils.isEmpty(lis.getParentId())) {
//					netId = lis.getId();
//					break;
//				}
//			}
//		}
		model.addAttribute("netId", netId);
		return "bill/accebill/listAbbilltransferdownmanage";
	}
	/**
	 * 票据下拨单 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询票据下拨单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillTransferDownManageService.getDataTables(dataTablesInput);
	}
	/**
	 * 票据下拨单新增页面跳转
	 *
	 * @return
	 */
	@GetMapping("/add")
	public String add(AbBillTransfer abbilltransfer, Model model) {
		String allocatedCode = SequenceSingleton.getSequence(SequenceEnum.BILL_TRANSFERDOWNMANAGE_NO, "XB");
		model.addAttribute("allocatedCode", allocatedCode);
		model.addAttribute("recordDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//		model.addAttribute("billCurrencyDefault", abBtCurrencyService.getCurrencyByName("人民币"));
		model.addAttribute("billCurrencyDefault", abBtCurrencyService.getRMBCurrency().getId());
		return "bill/accebill/addAbbilltransferdownmanage";
	}
	/**
	 * 票据下拨单编辑页面跳转
	 *
	 * @return
	 */
	@GetMapping("/update/{aid}")
	public String update(@PathVariable("aid") String aid, Model model) {
		AbBillTransferDownManageVo vo = abBillTransferDownManageService.getDataById(aid);
		if (AccebillConstant.BRIEF_STATUS_6203.equals(vo.getBriefId())) {
			vo = abBillTransferDownManageService.toInit(vo);
		}
		model.addAttribute("recordDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		model.addAttribute("vo", vo);
		return "bill/accebill/updateAbbilltransferdownmanage";
	}
	/**
	 * 保存票据下拨单
	 * 
	 * @param opt
	 *            新增或修改
	 * @param abBillTransferDownManageVo
	 * @return
	 */
	@LogMessage(content = "保存票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save/{opt}")
	@ResponseBody
	public AjaxJson save(@PathVariable("opt") String opt, AbBillTransferDownManageVo abBillTransferDownManageVo) {
		abBillTransferDownManageService.save(abBillTransferDownManageVo, opt, this.getCurrentUser());
		return success();
	}
	/**
	 * 提交票据下拨单
	 * 
	 * @param opt
	 *            新增或修改
	 * @param abBillTransferDownManageVo
	 * @return
	 */
	@LogMessage(content = "提交票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit/{opt}")
	@ResponseBody
	public AjaxJson submit(@PathVariable("opt") String opt, AbBillTransferDownManageVo abBillTransferDownManageVo) {
		abBillTransferDownManageService.submit(abBillTransferDownManageVo, opt, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量提交票据下拨单
	 * 
	 * @param abBillTransferDownManageVo
	 * @return
	 */
	@LogMessage(content = "提交票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(AbBillTransferDownManageVo abBillTransferDownManageVo) {
		abBillTransferDownManageService.batchSubmit(abBillTransferDownManageVo, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除票据下拨单
	 * 
	 * @param aid
	 * @return
	 */
	@LogMessage(content = "删除票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{ids}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("ids") String aid) {
		abBillTransferDownManageService.deleteAbBillTransfer(aid, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除票据下拨单
	 * 
	 * @param trids
	 * @return
	 */
	@LogMessage(content = "提交票据下拨单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(String trids, String aids) {
		abBillTransferDownManageService.batchDelete(trids, aids, this.getCurrentUser());
		return success();
	}
	/**
	 * 跳转至票据下拨查看页面
	 * 
	 * @param businessId
	 * @param model
	 * @return String
	 */
	@GetMapping("/view")
	public String view(String businessId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillTransferDownManageVo vo = abBillTransferDownManageService
					.getAbBillTransferDownManageVoById(businessId, model);
			model.addAttribute("info", vo);
		}
		return "bill/accebill/approve/viewApproveAbBill6204";
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
		return "bill/accebill/approve/listAbBillTransferDownManageApprove";
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
		return abBillTransferDownManageService.dataTableApprove(dataTablesInput);
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
			AbBillTransferDownManageVo vo = abBillTransferDownManageService
					.getAbBillTransferDownManageVoById(businessId, model);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/approve/approveAbBillTransferDownManage";
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
			AbBillTransferDownManageVo vo = abBillTransferDownManageService
					.getAbBillTransferDownManageVoById(businessId, model);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
			AbBillAttachmentVO attVo = abBillAttachmentService.viewAttachment(businessId);
			model.addAttribute("attachmentInfo", attVo);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBill6204";
	}
	/**
	 * 根据提交的下拨申请单检查是否已经选择票据,是isSelectBill=true 否isSelectBill=false
	 * 
	 * @param trid
	 * @return
	 */
	@PostMapping("/checkSelectBill")
	@ResponseBody
	public AjaxJson checkSelectBill(String trid) {
		boolean isSelectBill = abBillTransferDownManageService.checkSelectBill(trid);
		AjaxJson json = new AjaxJson();
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("isSelectBill", isSelectBill);
		json.setAttributes(attributes);
		json.setSuccess(true);
		return json;
	}
}
