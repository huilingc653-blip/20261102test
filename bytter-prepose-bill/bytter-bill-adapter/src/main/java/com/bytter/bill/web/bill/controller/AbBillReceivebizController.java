package com.bytter.bill.web.bill.controller;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.bill.service.AbBillReceivebizService;
import com.bytter.bill.domain.bill.service.AbConfigService;
import com.bytter.bill.domain.bill.service.AbDraftExcService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: Controller
 * @Description: 票据签收信息
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillreceivebiz")
public class AbBillReceivebizController extends BaseController {
	@Autowired
	private AbBillReceivebizService abBillReceivebizService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbDraftExcService abDraftExcService;
	/**
	 * 票据签收信息列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		// 系统参数 是否挂资金计划
		String budgetSel = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET)
				.getValue();
		model.addAttribute("isBudgetPlan", budgetSel);
		model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
		if ("1".equals(budgetSel)) {
			// 系统参数 背书挂接资金计划科目
			model.addAttribute("signItem", abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_RECITEM).getValue());
		}
		return "bill/accebill/listAbBillReceivebiz";
	}
	/**
	 * 票据签收信息 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	@LogMessage(content = "查询票据签收信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abBillReceivebizService.listAbBillReceivebizInfo(dataTablesInput);
	}
	/**
	 * 票据签收详细信息页面跳转
	 *
	 * @return
	 */
	@GetMapping("/businessDtlView")
	public String businessDtlView(AbBillReceivebizVO abBillReceivebizVO, Model model) {
		if (StringUtils.isNotEmpty(abBillReceivebizVO.getId())) {
			AbBillReceivebizVO info = abBillReceivebizService.getAbBillReceivebizVOById(abBillReceivebizVO.getId());
			model.addAttribute("info", info);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()):null);
		}
		// 系统参数
		model.addAttribute("recItem", abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_RECITEM).getValue());
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
		//线上签收
		model.addAttribute("isSign", "1");
		return "bill/accebill/busOpAbBillReceivebiz";
	}
	/**
	 * 签收批量同意操作
	 *
	 * @return
	 */
	@PostMapping("/consent")
	@ResponseBody
	@LogMessage(content = "签收同意操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_CONSENT)
	public AjaxJson consent(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
		abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_Y);
		abBillReceivebizService.billSign(abBillReceivebizVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 签收批量不同意操作
	 *
	 * @return
	 */
	@PostMapping("/noConsent")
	@ResponseBody
	@LogMessage(content = "签收不同意操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_NOCONSENT)
	public AjaxJson noConsent(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
		abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_N);
		abBillReceivebizService.billSign(abBillReceivebizVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 签收批量同意操作
	 *
	 * @return
	 */
	@PostMapping("/batchConsent")
	@ResponseBody
	@LogMessage(content = "签收批量同意操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHCONSENT)
	public AjaxJson batchConsent(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
		abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_Y);
		abBillReceivebizService.billSign(abBillReceivebizVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 签收批量不同意操作
	 *
	 * @return
	 */
	@PostMapping("/batchNoConsent")
	@ResponseBody
	@LogMessage(content = "签收批量不同意操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHNOCONSENT)
	public AjaxJson batchNoConsent(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
		abBillReceivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_N);
		abBillReceivebizService.billSign(abBillReceivebizVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量已撤回操作
	 *
	 * @return
	 */
	@PostMapping("/batchRecall")
	@ResponseBody
	@LogMessage(content = "批量已撤回操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHREPULSE)
	public AjaxJson batchRecall(@RequestBody AbBillReceivebizVO abBillReceivebizVO) {
		abBillReceivebizService.billRecall(abBillReceivebizVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus,String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "bill/accebill/approve/listAbBillReceivebizApprove";
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
		return abBillReceivebizService.dataTableApprove(dataTablesInput);
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
	public String approve(String businessId, String taskId, Model model) {
		// model.addAttribute("entity", 根据businessId查询到需要展示的数据);
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillReceivebizVO info = abBillReceivebizService.getAbBillReceivebizVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(info.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(info.getVvid()):null);
		}
		model.addAttribute("taskId", taskId);// 必须要做此步骤
		// 系统参数
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		return "bill/accebill/approve/approveAbBillReceivebiz";
	}
	/**
	 * 跳转至手机审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			AbBillReceivebizVO info = abBillReceivebizService.getAbBillReceivebizVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		// 系统参数
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		return "bill/accebill/mobile/approveAbBillReceivebiz";
	}
	/**
	 * 判断单位是否为同一网点下
	 * @param corpId1
	 * @param corpId2
	 * @return
	 */
	@PostMapping("/isTheSameNetByCorpId")
	@ResponseBody
	public AjaxJson isTheSameNetByCorpId(String corpId1,String corpId2) {
		AjaxJson json = success();
		boolean flag = abBillReceivebizService.isTheSameNetByCorpId(corpId1,corpId2);
		json.setSuccess(flag);
		return json;
	}
	/**
	* 支票收款信用详情页
	* billCode 票据号码
	* acctId 申请人账户
	* msgType 消息类型
	* @return
	*/
	@GetMapping("/playPage")
	public String playPage(String billCode,String acctId,String recId,String msgType,Model model) {
		model.addAttribute("billCode",billCode);
		model.addAttribute("acctId",acctId);
		model.addAttribute("recId",recId);
		model.addAttribute("msgType",msgType);
		model.addAttribute("paymentCredit",abDraftExcService.getAbPaymentCreditVoByOrigNo(billCode));
		return "bill/accebill/waitSignRequest";
	}
	/**
	 * 查询信用信息
	 * @return
	 */
	@PostMapping("/fastCredit")
	@ResponseBody
	public AjaxJson fastCredit(String billCode,String acctId,String acId,String msgType,String recId) {
		if(StringUtils.isNotEmpty(billCode)){
			billCode=billCode.replace(" ", "");
		}
		if(StringUtils.isNotEmpty(recId)){
			return abDraftExcService.creditInformationSign(billCode, acctId, acId, recId,msgType);
		}else if(StringUtils.isNotEmpty(acId)
				&&(acId.indexOf(",")!=-1
				||(StringUtils.isEmpty(billCode)&&StringUtils.isEmpty(acctId)))){
			String[] acIds=acId.split(",");
			for(String id:acIds){
				if(StringUtil.isNotEmpty(id)){
					abDraftExcService.creditInformation(billCode, acctId, id, msgType);
				}
			}
			AjaxJson json = new AjaxJson();
			json.setSuccess(true);
			Map<String, Object> messMsg = new HashMap<String, Object>();
			messMsg.put("MSG_INFO",AccebillConstant.AB_DRAFT_EXC_SEND_SUCESS_MSG);
			json.setAttributes(messMsg);
			return json;
		}else{
			return abDraftExcService.creditInformation(billCode, acctId, acId, msgType);
		}
	}
	/**
	 * 批量查询正背面信息
	 * @return
	 */
	@PostMapping("/batchBankSyncroBillInfo")
	@ResponseBody
	public AjaxJson batchBankSyncroBillInfo(String acId,String type) {
		String[] acIds=acId.split(",");
		if(AccebillConstant.MSG_TYPE_BILLINFO.equals(type)){
			for(String id:acIds){
				if(StringUtil.isNotEmpty(id)){
					abDraftExcService.creditInformation(null, null, id, AccebillConstant.MSG_TYPE_BILLINFO);
				}
			}
		}else if(AccebillConstant.MSG_TYPE_BACKINFO.equals(type)){
			for(String id:acIds){
				if(StringUtil.isNotEmpty(id)){
					abDraftExcService.creditInformation(null, null, id, AccebillConstant.MSG_TYPE_BACKINFO);
				}
			}
		}
		AjaxJson json = new AjaxJson();
		json.setSuccess(true);
		Map<String, Object> messMsg = new HashMap<String, Object>();
		messMsg.put("MSG_INFO",AccebillConstant.AB_DRAFT_EXC_SEND_SUCESS_MSG);
		json.setAttributes(messMsg);
		return json;
	}

	/**
	 * 批量发送指令
	 * @return
	 */
	@PostMapping("/sendCommand")
	@ResponseBody
	public AjaxJson sendCommand(String payerAcc, String msgType, String billType, String signType) {
		return abBillReceivebizService.sendCommand(payerAcc, msgType, billType, signType);
	}
}