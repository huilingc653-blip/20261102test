package com.bytter.bill.web.bill.controller;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbEmitBillSupplyVo;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.constant.ConfigConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @Title: 开票补录Controller
 * @Description:
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abEmitBillSupply")
public class AbEmitBillSupplyController extends BaseController {
	@Autowired
	private AbEmitBillSupplyService abEmitBillSupplyService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbRatUrationService abRatUrationService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	@Autowired
	private AbFcBondHookService abFcBondHookService;
	@Autowired
	private AbGuaranteeService abGuaranteeService;
	@Autowired
	private AbRatCpUrationService abRatCpUrationService;
	/**
	 * 开票补录列表查询 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbEmitBillSupply";
	}
	/**
	 * 开票补录列表查询
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "开票补录列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return abEmitBillSupplyService.getDataTables(input);
	}
	/**
	 * 开票补录页面跳转
	 *
	 * @return
	 */
	@GetMapping("/makeup/{aid}")
	public String makeup(@PathVariable("aid") String aid, Model model) {
		AbEmitBillSupplyVo vo = abEmitBillSupplyService.getAbEmitBillSupplyVoById(aid, model);
		model.addAttribute("abEmitBillSupplyVo", vo);
		// 挂预算系统参数
		if(StringUtils.isNotBlank (vo.getVoucherId ())) {
			//付款单应付票据预算
			List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill
					(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, vo.getVoucherId ());
			if (list != null && !list.isEmpty ()) {
				model.addAttribute ("payItem",
						abConfigService.getConfig (ConfigConstant.NIS, NetbankConstant.NIS_CONFIG_PAYITEM).toString ());
				model.addAttribute ("businessName", BudgetConstant.BUSINESS_NAME_PAY);
				model.addAttribute ("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
			} else {
				//付款单没有预算信息
				model.addAttribute ("nisBillheadNoBudget", true);
			}
		}else if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
			model.addAttribute("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
			model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
			model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		}
		// 挂授信
		boolean isRelateRat = abRatUrationService.isRelateRat();
		model.addAttribute("isRelateRat", isRelateRat);
		model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
		String isOpenRat = FcBondConstant.IS_HOOK_N;
		String ratmodel = FcBondConstant.IS_HOOK_N;
		if (isRelateRat) {
			RatUrationDTO urationDTO = new RatUrationDTO();
			if(!StringUtils.isBlank(vo.getRatmodel())
					&& vo.getRatmodel().equals("1")){
				ratmodel = vo.getRatmodel();
				urationDTO = abRatUrationService.getRatUrationDTOByBillId(vo.getVvid());
			}
			model.addAttribute("urationDTO", urationDTO);
			if(!StringUtils.isBlank(vo.getIsOpenRat())
					&& vo.getIsOpenRat().equals("1")){
				isOpenRat =vo.getIsOpenRat();
			}
		}
		//是否挂保证金
		boolean isBail = abFcBondHookService.isBail();
		//判断是否有已挂保证金数据
		FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(),vo.getAppNo());
		String bailmodel = FcBondConstant.IS_HOOK_N;
		String bondId = "";
		if(fcBondApplyDTO != null){
			bondId = fcBondApplyDTO.getBondId();
			bailmodel = FcBondConstant.IS_HOOK_Y;
		}
		model.addAttribute("bondId",bondId);
		model.addAttribute("bailmodel",bailmodel);
		model.addAttribute("isOpenRat",isOpenRat);
		model.addAttribute("isBail",isBail);
		//挂保证金银行类型
		model.addAttribute("bailBankControlType",abFcBondHookService.bailBankControlType());
		model.addAttribute("ratmodel",ratmodel);
		//是否启用授信敞口
		model.addAttribute("mouthRat",abRatUrationService.isNotOpenRat());
		//担保参数公共参数
		model.addAttribute("isGuarantee",abGuaranteeService.isHangGuarantee());
		//担保业务参数
		String isHangGuarantee = vo.getIsHangGuarantee()==null?FcBondConstant.IS_HOOK_N:vo.getIsHangGuarantee();
		model.addAttribute("isHangGuarantee",isHangGuarantee);
		//过滤查询参数操作
		if(abGuaranteeService.isHangGuarantee().equals(1)
				&& isHangGuarantee.equals(FcBondConstant.IS_HOOK_Y)){
			GuaContractOccupDTO bearVo = abGuaranteeService.getGuaContractOccupDTOByBillId(vo.getVvid());
			if(null!=bearVo && bearVo.getContractIds()!=null && bearVo.getContractIds().size()>0){
				List<String> idList=bearVo.getContractIds();
				String contractIds="";
				for (String id:idList) {
					contractIds+=id+",";
				}
				model.addAttribute("contractIds",contractIds);
				model.addAttribute("contractCode",bearVo.getContractCode());
				model.addAttribute("idList",bearVo==null?null:bearVo.getContractIds());
				model.addAttribute("guaWayNames",bearVo.getGuaWayName());
				model.addAttribute("guaWay",bearVo.getGuaWay());
				model.addAttribute("guaranteeRmk",bearVo.getGuaRmk());
			}
		}
		model.addAttribute("attachmentInfo", abBillAttachmentService.editAttachment(aid));
		//内部授信大参数
		model.addAttribute("isOpenCpRat",abRatCpUrationService.isRelateCpRat());
		//当前配置的内部参数
		model.addAttribute("curOpenCpRat",vo.getInsideRatModel()==null?"0":vo.getInsideRatModel());
		RatUrationDTO ratDto = abRatCpUrationService.getRatUrationDTOByBillId(vo.getVvid());
		//当前内部授信信息
		model.addAttribute("cpURationDTO",null==ratDto || (null!=ratDto && ratDto.getStatus().equals(3))?null:ratDto);
		return "bill/accebill/makeupAbEmitBillSupply";
	}
	/**
	 * 开票补录提交并入库
	 *
	 * @param abEmitBillSupplyVo
	 * @return
	 */
	@LogMessage(content = "开票补录提交并入库", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/makeupandstorage")
	@ResponseBody
	public AjaxJson makeupandstorage(AbEmitBillSupplyVo abEmitBillSupplyVo, RatUrationDTO urationDTO) {
		abEmitBillSupplyVo.setUrationDTO(urationDTO);
		if(urationDTO!=null) {
			abEmitBillSupplyVo.setCreditCode(urationDTO.getRatItemCode());
		}
		abEmitBillSupplyService.makeupSave(abEmitBillSupplyVo, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量补录完成并入库
	 *
	 * @return
	 */
	@LogMessage(content = "批量开票补录完成并入库", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchMakeupandstorage")
	@ResponseBody
	public AjaxJson batchMakeupandstorage(String vids, String vvids, String aids) {
		abEmitBillSupplyService.batchMakeup(vids, vvids, aids, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbEmitBillSupplyApprove";
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
		return abEmitBillSupplyService.dataTableApprove(dataTablesInput);
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
			AbEmitBillSupplyVo vo = abEmitBillSupplyService.getAbEmitBillSupplyVoById(businessId, model);
			model.addAttribute("abEmitBillVo", vo);
			model.addAttribute("isApprove", true);
			model.addAttribute("billId", vo.getVvid());
			// 挂预算系统参数
			if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
				model.addAttribute("payItem",
						abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
				model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
				model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
			}
			List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
					BudgetConstant.BUSINESS_NAME_EMIT, BudgetConstant.PROCESS_SYSNAME_AB, vo.getVvid());
			if (list != null && !list.isEmpty()) {
				// 显示预算
				model.addAttribute("budgetDisplay", "true");
			}
			//判断是否有已挂保证金数据
			FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(),vo.getAppNo());
			String bailmodel = FcBondConstant.IS_HOOK_N;
			if(fcBondApplyDTO != null){
				bailmodel = FcBondConstant.IS_HOOK_Y;
			}
			model.addAttribute("bailmodel",bailmodel);
			//挂授信 显示
			if(!StringUtils.isBlank(vo.getRatmodel()) && vo.getRatmodel().equals("1")){
				model.addAttribute("isEnabled",true);
			}else{
				model.addAttribute("isEnabled",false);
			}
			//挂内部授信 显示
			if(!StringUtils.isBlank(vo.getInsideRatModel()) && vo.getInsideRatModel().equals(FcBondConstant.IS_HOOK_Y)){
				model.addAttribute("cpIsEnabled",true);
				model.addAttribute("cpURationDTO",abRatCpUrationService.getRatUrationDTOByBillId(vo.getVvid()));
			}else{
				model.addAttribute("cpIsEnabled",false);
			}
			model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
			//是否启用担保参数
			model.addAttribute("isHangGuarantee",vo.getIsHangGuarantee()==null?FcBondConstant.IS_HOOK_N:vo.getIsHangGuarantee());
			model.addAttribute("appNo",vo.getAppNo());
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/approve/approveAbEmitBillSupply";
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
			AbEmitBillSupplyVo vo = abEmitBillSupplyService.getAbEmitBillSupplyVoById(businessId, model);
			model.addAttribute("abEmitBillVo", vo);
			model.addAttribute("isApprove", true);
			// 挂预算系统参数
			if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
				model.addAttribute("payItem",
						abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
				model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
				model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
			}
			List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
					BudgetConstant.BUSINESS_NAME_EMIT, BudgetConstant.PROCESS_SYSNAME_AB, vo.getVvid());
			if (list != null && !list.isEmpty()) {
				// 显示预算
				model.addAttribute("budgetDisplay", "true");
			}
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBillEmit";
	}
	/**
	 * 开票补录导入
	 *
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<AbEmitBillSupplyVo> list) {
		abEmitBillSupplyService.saveImport(list, this.getCurrentUser());
		return success();
	}
}
