package com.bytter.bill.web.bill.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.bill.entity.*;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbBillSignSupplyVo;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Title: Controller
 * @Description:应收票据登记
 * @author onlineGenerator
 * @date 2017-06-22 10:31:43
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abreceivebill")
public class AbReceiveBillController extends BaseController {
	@Autowired
	private AbReceiveBillService abReceiveBillService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	
	@Autowired
	private AbRatUrationService abRatUrationService;
	@Autowired
	private AbDiscountService abDiscountService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;

	@Autowired
	private AbCorpService abCorpService;
	
	/**
	 * 应收票据登记列表页面跳转
	 * 
	 * @return
	 */
	@LogMessage(content = "查询应收票据登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbReceiveBill";
	}
	/**
	 * 应收票据登记table列表
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return abReceiveBillService.listAbReceiveBill(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 应收票据新增页面跳转
	 * 
	 * @return
	 */
	@GetMapping("/add")
	public String add(Model model) {
		// 系统参数
		model.addAttribute("recItem", abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_RECITEM).getValue());
		//获取纸票票据金额最大额度
		model.addAttribute("maxBillMoney",abVoucherinfoViceService.getMaxBillMoney());
		model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
		return "bill/accebill/addAbReceiveBill";
	}
	/**
	 * 登记应收票据
	 * 
	 * @param abReceiveBillVO
	 * @param opType
	 * @return
	 */
	@LogMessage(content = "登记应收票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/register/{opType}")
	@ResponseBody
	public AjaxJson registerAbReceiveBill(@RequestBody AbReceiveBillVO abReceiveBillVO,
			@PathVariable("opType") String opType) {
		abReceiveBillService.registerAbReceiveBill(abReceiveBillVO, opType, this.getCurrentUser());
		return success();
	}
	/**
	 * 编辑页面跳转
	 * 
	 * @param accId
	 * @param model
	 * @return
	 */
	@GetMapping("/update")
	public String update(String accId, Model model) {
		if (StringUtils.isNotEmpty(accId)) {
			// 获取应收票据信息
			AbReceiveBillVO billInfo = abReceiveBillService.getAbReceiveBillInfo(accId);
			AbAccrecords abAccrecords = abAccrecordsService.get (billInfo.getAccId (),true);
			if(StringUtils.isNotBlank (abAccrecords.getLastid ())){
				abAccrecords = abAccrecordsService.get (abAccrecords.getLastid (),true);
				AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get (abAccrecords.getVvid ());
				if(AccebillConstant.BRIEF_STATUS_6101.equals (abAccrecords.getBriefId ())
						|| (AccebillConstant.AB_INSIDE_SING_Y.equals (String.valueOf (abVoucherinfoVice.getInsideSing ()))
						    &&AccebillConstant.BRIEF_STATUS_010.equals (abAccrecords.getBriefId ()))){
					//补录或对内背书业务完成后自动收票登记的业务(收票方式不能修改，前手客商按钮加载为成员单位信息)
					model.addAttribute("insideSing6101or010", true);
				}
			}
			model.addAttribute("abReceiveBillPage", billInfo);
		}
		model.addAttribute("recItem", abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_RECITEM).getValue());
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		//获取纸票票据金额最大额度
		model.addAttribute("maxBillMoney",abVoucherinfoViceService.getMaxBillMoney());
		model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
		model.addAttribute("attachmentInfo", abBillAttachmentService.editAttachment(accId));
		return "bill/accebill/updateAbReceiveBill";
	}
	/**
	 * 修改应收票据
	 * 
	 * @param abReceiveBillVO
	 * @param opType
	 * @return
	 */
	@LogMessage(content = "修改应收票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify/{opType}")
	@ResponseBody
	public AjaxJson modify(@RequestBody AbReceiveBillVO abReceiveBillVO, @PathVariable("opType") String opType) {
		AjaxJson j = success();
		boolean flag = abReceiveBillService.modifyAbReceiveBill(abReceiveBillVO, opType, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
	/**
	 * 应收票据详细信息
	 * 
	 * @param viceId
	 * @param model
	 * @return
	 */
	@LogMessage(content = "查询应收票据详细信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	public String view(String viceId, String accId, boolean isApprove, String briefId, Model model, boolean isShowPool) {
		String reVm = "bill/accebill/viewAbReceiveBill";
		if (StringUtils.isNotEmpty(accId)) {
			AbVoucherinfoVO billInfo = abReceiveBillService.getAbBillInfo(accId, briefId);
			if (billInfo != null) {
				Set<String> corpIdSet = new HashSet<>();
				Map<String, Object> stringObjectMap = BeanUtil.beanToMap(billInfo);
				if(StrUtil.isNotBlank(billInfo.getApplyCorpId())){
					corpIdSet.add(billInfo.getApplyCorpId());
				}
				if(stringObjectMap.get("corpId") != null){
					corpIdSet.add(stringObjectMap.get("corpId").toString());
				}
				if(StrUtil.isNotBlank(billInfo.getBusCorpId())){
					corpIdSet.add(billInfo.getBusCorpId());
				}

				if(corpIdSet.size() > 0){
					Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
					if(CollectionUtil.isNotEmpty(stringCorpDTOMap)){
						CorpDTO corpDTO = stringCorpDTOMap.get(billInfo.getApplyCorpId());
						//兼容 签收查看
						String corpIdKey = Optional.ofNullable(stringObjectMap.get("corpId"))
								.map(Object::toString)
								.orElse(Optional.ofNullable(stringObjectMap.get("recCorpId"))
										.map(Object::toString)
										.orElse(""));
						CorpDTO corpDTO2 = stringCorpDTOMap.get(corpIdKey);
						CorpDTO corpDTO3 = stringCorpDTOMap.get(billInfo.getBusCorpId());
						stringObjectMap.put("applyCorpName",corpDTO == null ? null : corpDTO.getName());
						stringObjectMap.put("applyCorpCode",corpDTO == null ? null : corpDTO.getCode());
						stringObjectMap.put("corpName",corpDTO2 == null ? null : corpDTO2.getName());
						stringObjectMap.put("corpIdName",corpDTO2 == null ? null : corpDTO2.getName());
						stringObjectMap.put("corpCode",corpDTO2 == null ? null : corpDTO2.getCode());
						stringObjectMap.put("busCorpName",corpDTO3 == null ? null : corpDTO3.getName());
						billInfo = BeanUtil.toBean(stringObjectMap,billInfo.getClass());
					}
				}


				billInfo.setBlgrdbagcrclPrmtIndName("1".equals(billInfo.getBlgrdbagcrclPrmtInd()) ? "是" : "否");
				if(StringUtils.isEmpty(viceId)){
					viceId=billInfo.getVvid();
				}
				model.addAttribute("bankTypeName", StringUtils.isNotEmpty(viceId)?abVoucherinfoViceService.findBankTypeNameWith(viceId):null);
				// 如果是签收业务跳转到签收页面详细页面
				if ((String.valueOf(billInfo.getBriefId()).startsWith("9") || String.valueOf(briefId).startsWith("9")
						|| AccebillConstant.BRIEF_STATUS_023.equals(billInfo.getBriefId())) && !AccebillConstant.BRIEF_STATUS_9004.equals(billInfo.getBriefId())) {
					model.addAttribute("info", billInfo);
					model.addAttribute("isShow", true);
					// 系统参数
					model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
					model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
					reVm = "bill/accebill/viewBusOpAbBillReceivebiz";
				} else {
					model.addAttribute("billId", viceId);
					model.addAttribute("abReceiveBillPage", billInfo);
					AbDiscount abDiscount = abDiscountService.getAbDiscountByRecordId(accId);
					//判断是否挂接了银行贴现数据
					if(null!=abDiscount && null!=abDiscount.getOccupancyRatio()){
						model.addAttribute("isEnabled",true);
					}else{
						model.addAttribute("isEnabled",false);
					}
					//不启用贴现申请功能直接贴现办理
					boolean isNotDiscountApply = abVoucherinfoViceService.isNotDiscountApply();
					model.addAttribute("isNotDiscountApply",isNotDiscountApply);
				}
				// 挂预算系统参数
				if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_6001)) {
					model.addAttribute("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_6001));
					model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
					model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
					List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
							BudgetConstant.BUSINESS_NAME_REC, BudgetConstant.PROCESS_SYSNAME_AB, viceId);
					if (list != null && !list.isEmpty()) {
						// 显示预算
						model.addAttribute("budgetDisplay", "true");
					}
				}
				if(StrUtil.isNotEmpty(billInfo.getOldDraftId())){
					model.addAttribute("isSplitBill", true);
				}
			}
			if (!isApprove) {
				List<AbLog> ablogList = abLogService.listAbLog(viceId, null);
				model.addAttribute("abLogPage", ablogList);
			}
			abBillAttachmentService.viewAttachment(accId, model);
		}
		model.addAttribute("isApprove", isApprove);
		model.addAttribute("isShowPool", isShowPool);
		return reVm;
	}
	/**
	 * 删除应收票据
	 * 
	 * @param accId
	 * @return
	 */
	@LogMessage(content = "删除应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete")
	@ResponseBody
	public AjaxJson delete(String accId) {
		AjaxJson j = success();
		boolean flag = abReceiveBillService.deleteAbReceiveBill(accId, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
	/**
	 * 批量删除应收票据
	 * 
	 * @param accIds
	 * @return
	 */
	@LogMessage(content = "批量删除应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHDEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(String accIds) {
		AjaxJson j = success();
		boolean flag = abReceiveBillService.batchDeleteAbReceiveBill(accIds, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
	/**
	 * 批量提交应收票据
	 * 
	 * @param accIds
	 * @return
	 */
	@LogMessage(content = "批量提交应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmitAbReceiveBill(String accIds) {
		AjaxJson j = success();
		boolean flag = abReceiveBillService.batchSubmitAbReceiveBill(accIds, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
	/**
	 * 票据号是否重复
	 * 
	 * @param opType
	 * @param abReceiveBillVO
	 * @return
	 */
	@PostMapping("/checkOrigNo/{opType}")
	@ResponseBody
	public AjaxJson checkOrigNo(@PathVariable("opType") String opType, @RequestBody AbReceiveBillVO abReceiveBillVO) {
		boolean flag = abReceiveBillService.checkOrigNo(opType, abReceiveBillVO);
		AjaxJson json = success();
		json.setSuccess(flag);
		return json;
	}
	/**
	 * 通过origNo查找票据信息
	 * 
	 * @param origNo
	 */
	@PostMapping("/getVouInfoByOrigNo")
	@ResponseBody
	public AjaxJson getVouInfoByOrigNo(String origNo) {
		AjaxJson json = success();
		AbVoucherinfo vouInfo = abReceiveBillService.getVouInfoByOrigNo(origNo);
		if (vouInfo == null) {
			json.setSuccess(false);
		} else {
			json.setSuccess(true);
			json.setObj(vouInfo);
		}
		return json;
	}
	/**
	 * 应收票据导入页面跳转
	 * 
	 * @return
	 */
	@GetMapping("/imports")
	public String imports(Model model) {
		return "bill/accebill/importAbReceiveBill";
	}
	/**
	 * 导入应收票据信息
	 * 
	 * @param list
	 * @return
	 */
	@LogMessage(content = "导入应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<AbReceiveBillVO> list) {
		AjaxJson json = success();
		if (CollectionUtils.isEmpty(list)){
			json.setSuccess(true);
			return success();
		}
		for(AbReceiveBillVO rb : list){
			rb.setSaveImport(true);
		}
		boolean flag = abReceiveBillService.saveImport(list, this.getCurrentUser());
		json.setSuccess(flag);
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
		return "bill/accebill/approve/listAbReceiveBillApprove";
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
		return abReceiveBillService.dataTableApprove(dataTablesInput);
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
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(businessId);
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("isApprove", true);
			abBillAttachmentService.viewAttachment(businessId, model);
			// 挂预算系统参数
			if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_6001)) {
				model.addAttribute("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_6001));
				model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
				model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
				List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
						BudgetConstant.BUSINESS_NAME_REC, BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid());
				if (list != null && !list.isEmpty()) {
					// 显示预算
					model.addAttribute("budgetDisplay", "true");
				}
			}
		}
		model.addAttribute("taskId", taskId);
		Integer isDisCountCredit = abRatUrationService.isDisCountCredit();
		Integer isCredit = abRatUrationService.isRelateRat()==false?0:1;
		model.addAttribute("isCredit",isCredit);
		model.addAttribute("isDisCountCredit",isDisCountCredit);
		return "bill/accebill/approve/approveAbReceiveBill";
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
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(businessId);
			model.addAttribute("info", info);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_REC);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
	/**
	 * 判断前手是否是内部成员单位
	 * 
	 * @param name
	 * @return
	 */
	@PostMapping("/getCorpByName")
	@ResponseBody
	public AjaxJson getCorpByName(String name) {
		AjaxJson json = success();
		boolean flag = abReceiveBillService.getCorpByName(name);
		json.setSuccess(flag);
		return json;
	}
	/**
	 * 根据单位名称查询单位id
	 *
	 * @param name
	 * @return
	 */
	@PostMapping("/getCorpIdByName")
	@ResponseBody
	public AjaxJson getCorpIdByName(String name) {
		AjaxJson json = success();
		boolean flag=false;
		String corpId = abReceiveBillService.getCorpIdByName(name);
		if (StringUtils.isNotEmpty(corpId)) {
			flag= true;
		}
		json.setObj(corpId);
		json.setSuccess(flag);
		return json;
	}
	/**
	 * 根据corpId判断单位是否为网点
	 * 
	 * @param corpId
	 * @return
	 */
	@PostMapping("/isNetByCorpId")
	@ResponseBody
	public AjaxJson isNetByCorpId(String corpId) {
		AjaxJson json = success();
		boolean flag = abReceiveBillService.isNetByCorpId(corpId);
		json.setSuccess(flag);
		return json;
	}
	/**
	 * 根据corpId判断单位是否为网点
	 *
	 * @param corpId
	 * @return
	 */
	@PostMapping("/isTopNetByCorpId")
	@ResponseBody
	public AjaxJson isTopNetByCorpId(String corpId) {
		AjaxJson json = success();
		boolean flag=false;
		CorpDTO dto = abReceiveBillService.isTopNetByCorpId(corpId);
		if (dto != null && corpId.equals(dto.getNetId())) {
			flag= true;
		}
		json.setObj(dto);
		json.setSuccess(flag);
		return json;
	}
}
