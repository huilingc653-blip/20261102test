package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillCollectionAccountService;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillCollectionAccountVo;
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
 * @Title: 应收-票据托收-托收到账 Controller
 * @Description: AB_VOUCHERINFO
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillcollectionaccount")
public class AbBillCollectionAccountController extends BaseController {
	@Autowired
	private AbBillCollectionAccountService abBillCollectionAccountService;
	@Autowired
	private AbCorpService abCorpService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 应收-票据托收-托收到账列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillCollectionAccount";
	}
	/**
	 * 应收-票据托收-托收到账列表 页面跳转
	 * 用于收款管理
	 *
	 * @return
	 */
	@GetMapping("/listCollectionAccount")
	public String listCollectionAccount(AbVoucherinfoViceVO reVo, String row, Model model) {
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
		return "bill/accebill/collectionAccountPage";
	}
	/**
	 * 应收-票据托收-托收到账 table列表
	 * @param isApply 是否审批中数据
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询可托收到账的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input, String isApply) {
		return abBillCollectionAccountService.getDataTablesUnion(input, isApply);
	}
	/**
	 * 托收到账
	 * 
	 * @param abBillCollectionAccountVo
	 * @return
	 */
	@LogMessage(content = "提交托收到账", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/collectionApply")
	@ResponseBody
	public AjaxJson collectionApply(AbBillCollectionAccountVo abBillCollectionAccountVo) {
		abBillCollectionAccountService.collectionApply(abBillCollectionAccountVo, this.getCurrentUser());
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
		return "bill/accebill/approve/listAbbillcollectionaccountApprove";
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
		return abBillCollectionAccountService.dataTableApprove(dataTablesInput);
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
			AbBillCollectionAccountVo vo = abBillCollectionAccountService.getAbBillCollectionAccountVoById(businessId,
					model);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
			AbAccrecords accrecordsInfo=abAccrecordsService.get(businessId, true);
			model.addAttribute("bankTypeName", StringUtils.isNotEmpty(accrecordsInfo.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(accrecordsInfo.getVvid()):null);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/approve/approveAbBillCollectionApply";
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
			AbBillCollectionAccountVo vo = abBillCollectionAccountService.getAbBillCollectionAccountVoById(businessId,
					model);
			model.addAttribute("info", vo);
			model.addAttribute("isApprove", true);
		}
		model.addAttribute("taskId", taskId);
		return "bill/accebill/mobile/viewApproveAbBillBase";
	}
}
