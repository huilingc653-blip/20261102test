package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillTransferDownApplyService;
import com.bytter.bill.domain.bill.service.AbBtCurrencyService;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownApplyVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @Title: Controller
 * @Description: 下拨申请单
 * @author onlineGenerator
 * @date 2017-07-17 14:43:45
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbilltransferdownapply")
public class AbBillTransferDownApplyController extends BaseController {
	@Autowired
	private AbBillTransferDownApplyService abBillTransferDownApplyService;
	@Autowired
	private AbBtCurrencyService abBtCurrencyService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	@Autowired
	private AbCorpService abCorpService;
	/**
	 * 下拨申请单列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listabbilltransferdownapply";
	}
	/**
	 * 下拨申请单 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询下拨申请单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillTransferDownApplyService.getDataTables(dataTablesInput);
	}
	/**
	 * 下拨申请单新增页面跳转
	 *
	 * @return
	 */
	@GetMapping("/add")
	public String add(AbBillTransfer abbilltransfer, Model model) {
		String allocatedCode = SequenceSingleton.getSequence(SequenceEnum.BILL_TRANSFERDOWNAPPLY_NO, "SQ");
		model.addAttribute("allocatedCode", allocatedCode);
		model.addAttribute("recordDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//		model.addAttribute("billCurrencyDefault", abBtCurrencyService.getCurrencyByName("人民币"));
		model.addAttribute("billCurrencyDefault", abBtCurrencyService.getRMBCurrency().getId());
		return "bill/accebill/addAbbilltransferdownapply";
	}
	/**
	 * 下拨申请单编辑页面跳转
	 *
	 * @return
	 */
	@GetMapping("/update/{aid}")
	public String update(@PathVariable("aid") String aid, Model model) {
		AbBillTransferDownApplyVo vo = abBillTransferDownApplyService.getDataById(aid, model);
		model.addAttribute("abBillTransferDownApplyVo", vo);
		return "bill/accebill/updateAbbilltransferdownapply";
	}
	/**
	 * 保存下拨申请单
	 * 
	 * @param opt
	 *            新增或修改
	 * @param abBillTransferDownApplyVo
	 * @return
	 */
	@LogMessage(content = "保存下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save/{opt}")
	@ResponseBody
	public AjaxJson save(@PathVariable("opt") String opt, AbBillTransferDownApplyVo abBillTransferDownApplyVo) {
		abBillTransferDownApplyService.save(abBillTransferDownApplyVo, opt, this.getCurrentUser());
		return success();
	}
	/**
	 * 提交下拨申请单
	 * 
	 * @param opt
	 *            新增或修改
	 * @param abBillTransferDownApplyVo
	 * @return
	 */
	@LogMessage(content = "提交下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit/{opt}")
	@ResponseBody
	public AjaxJson submit(@PathVariable("opt") String opt, AbBillTransferDownApplyVo abBillTransferDownApplyVo) {
		abBillTransferDownApplyService.submit(abBillTransferDownApplyVo, opt, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量提交下拨申请单
	 * 
	 * @param abBillTransferDownApplyVo
	 * @return
	 */
	@LogMessage(content = "提交下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson submit(AbBillTransferDownApplyVo abBillTransferDownApplyVo) {
		abBillTransferDownApplyService.batchSubmit(abBillTransferDownApplyVo, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除下拨申请单--物理删除
	 * 
	 * @param ids
	 * @return
	 */
	@LogMessage(content = "删除下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{ids}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("ids") String ids) {
		abBillTransferDownApplyService.deleteAbBillTransfer(ids, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除下拨申请单
	 * 
	 * @param trids
	 * @return
	 */
	@LogMessage(content = "提交下拨申请单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(String trids, String aids) {
		abBillTransferDownApplyService.batchDelete(trids, aids, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbbilltransferdownapplyApprove";
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
		return abBillTransferDownApplyService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转查看详情界面
	 * 
	 * @param aid
	 * @param model
	 * @return String
	 */
	@GetMapping("/view")
	public String view(String aid, Model model) {
		if (StringUtils.isNotEmpty(aid)) {
			AbBillTransferDownApplyVo vo = abBillTransferDownApplyService.getDataById(aid, model);
			model.addAttribute("info", vo);
			AbBillAttachmentVO attVo = abBillAttachmentService.viewAttachment(aid);
			model.addAttribute("attachmentInfo", attVo);
		}
		return "bill/accebill/approve/viewApproveAbBill6203";
	}
	/**
	 * 跳转至审核详情界面
	 * 
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillTransferDownApplyVo vo = abBillTransferDownApplyService.getDataById(businessId, model);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		AbBillAttachmentVO attVo = abBillAttachmentService.viewAttachment(businessId);
		model.addAttribute("attachmentInfo", attVo);
		return "bill/accebill/approve/approveAbBillTransferDownApply";
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
			AbBillTransferDownApplyVo vo = abBillTransferDownApplyService.getDataById(businessId, model);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBill6203";
	}
	/**
	 * 通过单位id获取单位信息
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/getSysCorpById/{id}")
	@ResponseBody
	public CorpDTO getSysCorpById(@PathVariable("id") String id) {
		return abCorpService.get(id);
	}
}
