package com.bytter.bill.web.saas.controller.bill;

import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.*;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.bill.vo.v1.AbReceiveBillApprovePageVO;
import com.bytter.bill.domain.bill.vo.v1.AbReceiveBillPageVO;
import com.bytter.bill.domain.bill.vo.v1.AbReceiveBillViewPageVO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.vm.RatBkrationCommonDTO;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description:应收票据登记
 * @author onlineGenerator
 * @date 2017-06-22 10:31:43
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abreceivebill", tags = "应收票据登记")
@RestController("v1AbReceiveBillController")
@RequestMapping("/v1-preposebill/bill/accebill/abreceivebill")
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
	private RatBkrationService ratBkrationService;


	/**
	 * 应收票据登记table列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询应收票据登记列表")
	@LogMessage(content = "查询应收票据登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(abReceiveBillService.listAbReceiveBill(input, this.getCurrentUser()));
	}
	/**
	 * 应收票据新增页面数据
	 *
	 * @return
	 */
	@ApiOperation(value = "应收票据新增页面数据")
	@GetMapping("/add")
	public AjaxJson<AbReceiveBillPageVO> add() {
		AbReceiveBillPageVO pageVO = new AbReceiveBillPageVO();
		// 系统参数
		pageVO.setRecItem(abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_RECITEM).getValue());
		//获取纸票票据金额最大额度
		pageVO.setMaxBillMoney(abVoucherinfoViceService.getMaxBillMoney());
		pageVO.setCurId(abVoucherinfoViceService.getCurIdByCNY());
		return success(pageVO);
	}
	/**
	 * 登记应收票据
	 *
	 * @param abReceiveBillVO
	 * @param opType
	 * @return
	 */
	@ApiOperation(value = "登记应收票据")
	@LogMessage(content = "登记应收票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/register/{opType}")
	public AjaxJson registerAbReceiveBill(@RequestBody AbReceiveBillVO abReceiveBillVO,
			@PathVariable("opType") String opType) {
		abReceiveBillService.registerAbReceiveBill(abReceiveBillVO, opType, this.getCurrentUser());
		return success();
	}
	/**
	 * 编辑页面数据
	 *
	 * @param accId
	 * @return
	 */
	@ApiOperation(value = "应收票据编辑页面数据")
	@GetMapping("/update")
	public AjaxJson<AbReceiveBillPageVO> update(String accId) {
		AbReceiveBillPageVO pageVO = new AbReceiveBillPageVO();
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
					pageVO.setInsideSing6101or010(true);
				}
			}
			pageVO.setAbReceiveBillPage(billInfo);
			pageVO.setAttachmentInfo(abBillAttachmentService.editAttachment(accId));
		}
		pageVO.setRecItem(abConfigService
				.getBillConfig(AccebillConstant.AB_CONFIG_RECITEM).getValue());
		pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_REC);
		pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);
		//获取纸票票据金额最大额度
		pageVO.setMaxBillMoney(abVoucherinfoViceService.getMaxBillMoney());
		pageVO.setCurId(abVoucherinfoViceService.getCurIdByCNY());
		return success(pageVO);
	}
	/**
	 * 修改应收票据
	 *
	 * @param abReceiveBillVO
	 * @param opType
	 * @return
	 */
	@ApiOperation(value = "修改应收票据")
	@LogMessage(content = "修改应收票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify/{opType}")
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
	 * @return
	 */
	@ApiOperation(value = "查询应收票据详细信息")
	@LogMessage(content = "查询应收票据详细信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	public AjaxJson<AbReceiveBillViewPageVO> view(String viceId, String accId, boolean isApprove, String briefId, boolean isShowPool) {
		AbReceiveBillViewPageVO pageVO = new AbReceiveBillViewPageVO();
		if (StringUtils.isNotEmpty(accId)) {
			AbVoucherinfoVO billInfo = abReceiveBillService.getAbBillInfo(accId, briefId);
			if(billInfo != null){
				billInfo.setBlgrdbagcrclPrmtIndName("1".equals(billInfo.getBlgrdbagcrclPrmtInd()) ? "是" : "否");
			}
			if (billInfo != null) {
				if(StringUtils.isEmpty(viceId)){
					viceId=billInfo.getVvid();
				}
				pageVO.setBankTypeName(StringUtils.isNotEmpty(viceId)?abVoucherinfoViceService.findBankTypeNameWith(viceId):null);
				// 如果是签收业务跳转到签收页面详细页面
				if ((String.valueOf(billInfo.getBriefId()).startsWith("9") || String.valueOf(briefId).startsWith("9")
						|| AccebillConstant.BRIEF_STATUS_023.equals(billInfo.getBriefId())) && !AccebillConstant.BRIEF_STATUS_9004.equals(billInfo.getBriefId())) {
					pageVO.setInfo(billInfo);
					pageVO.setIsShow(true);
					// 系统参数
					pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_REC);
					pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);
				} else {
					pageVO.setBillId(viceId);
					pageVO.setInfo(billInfo);
					AbDiscount abDiscount = abDiscountService.getAbDiscountByRecordId(accId);
					//判断是否挂接了银行贴现数据
					if(null!=abDiscount && null!=abDiscount.getOccupancyRatio()){
						pageVO.setIsEnabled(true);
					}else{
						pageVO.setIsEnabled(false);
					}
					//不启用贴现申请功能直接贴现办理
					boolean isNotDiscountApply = abVoucherinfoViceService.isNotDiscountApply();
					pageVO.setIsNotDiscountApply(isNotDiscountApply);
				}
				// 挂预算系统参数
				if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_6001)) {
					pageVO.setPayItem(abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_6001));
					pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_REC);
					pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);
					List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
							BudgetConstant.BUSINESS_NAME_REC, BudgetConstant.PROCESS_SYSNAME_AB, viceId);
					if (list != null && !list.isEmpty()) {
						// 显示预算
						pageVO.setBudgetDisplay(true);
					}
				}
				if(StrUtil.isNotEmpty(billInfo.getOldDraftId())){
					pageVO.setIsSplitBill(true);
				}
			}
			// 银行授信信息
			if (StringUtils.isNotEmpty(viceId)) {
				RatBkrationCommonDTO ratCommonDTO = ratBkrationService.getRatCommonDTOByBillId(ConfigConstant.ACCEBILL, viceId);
				pageVO.setRatBkrationPage(ratCommonDTO);
			}
			if (!isApprove) {
				List<AbLog> ablogList = abLogService.listAbLog(viceId, null);
				pageVO.setAblogList(ablogList);
			}
			abBillAttachmentService.viewAttachment(accId, pageVO);
		}
		pageVO.setIsApprove(isApprove);
		pageVO.setIsShowPool(isShowPool);
		return success(pageVO);
	}
	/**
	 * 删除应收票据
	 *
	 * @param accId
	 * @return
	 */
	@ApiOperation(value = "删除应收票据")
	@LogMessage(content = "删除应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete")
	public AjaxJson delete(@RequestBody String accId) {
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
	@ApiOperation(value = "批量删除应收票据")
	@LogMessage(content = "批量删除应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHDEL)
	@PostMapping("/batchDelete")
	public AjaxJson batchDelete(@RequestBody String accIds) {
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
	@ApiOperation(value = "批量提交应收票据")
	@LogMessage(content = "批量提交应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/batchSubmit")
	public AjaxJson batchSubmitAbReceiveBill(@RequestBody String accIds) {
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
	@ApiOperation(value = "票据号是否重复")
	@PostMapping("/checkOrigNo/{opType}")
	public AjaxJson checkOrigNo(@PathVariable("opType") String opType, @RequestBody AbReceiveBillVO abReceiveBillVO) {
		boolean flag = abReceiveBillService.checkOrigNo(opType, abReceiveBillVO);
		AjaxJson json = success();
		json.setObj(flag);
		return json;
	}
	/**
	 * 通过origNo查找票据信息
	 *
	 * @param origNo
	 */
	@ApiOperation(value = "通过origNo查找票据信息")
	@GetMapping("/getVouInfoByOrigNo")
	public AjaxJson<AbVoucherinfo> getVouInfoByOrigNo(String origNo) {
		AbVoucherinfo vouInfo = abReceiveBillService.getVouInfoByOrigNo(origNo);
		AjaxJson<AbVoucherinfo> json = success(vouInfo);
//		json.setSuccess(vouInfo != null);
		return json;
	}
	/**
	 * 导入应收票据信息
	 *
	 * @param list
	 * @return
	 */
	@ApiOperation(value = "导入应收票据信息")
	@LogMessage(content = "导入应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/saveImport")
	public AjaxJson saveImport(@RequestBody List<AbReceiveBillVO> list) {
		if (CollectionUtils.isEmpty(list)) {
			return success();
		}
		for (AbReceiveBillVO rb : list) {
			rb.setSaveImport(true);
		}
		abReceiveBillService.saveImport(list, this.getCurrentUser());
		return success();
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "加载审核列表数据")
	@PostMapping("/dataTableApprove")
	public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(abReceiveBillService.dataTableApprove(input));
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param businessId
	 * @param taskId
	 * @return
	 */
	@ApiOperation(value = "查询应收票据审核详情")
	@GetMapping("/approve")
	public AjaxJson<AbReceiveBillApprovePageVO> approve(String businessId, String taskId) {
		AbReceiveBillApprovePageVO pageVO = new AbReceiveBillApprovePageVO();
		if (StringUtils.isNotEmpty(businessId)) {
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(businessId);
			pageVO.setInfo(info);
			pageVO.setIsApprove(true);
			setAttachment(pageVO, businessId);
			// 挂预算系统参数
			if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_6001)) {
				pageVO.setPayItem(abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_6001));
				pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_REC);
				pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);
				List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
						BudgetConstant.BUSINESS_NAME_REC, BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid());
				if (list != null && !list.isEmpty()) {
					// 显示预算
					pageVO.setBudgetDisplay(true);
				}
			}
		}
		pageVO.setTaskId(taskId);
		Integer isDisCountCredit = abRatUrationService.isDisCountCredit();
		Integer isCredit = abRatUrationService.isRelateRat()==false?0:1;
		pageVO.setIsCredit(isCredit);
		pageVO.setIsDisCountCredit(isDisCountCredit);
		return success(pageVO);
	}
	/**
	 * 跳转至手机审核详情界面
	 *
	 * @param businessId
	 * @param taskId
	 * @return
	 */
	@ApiOperation(value = "查询应收票据手机审核详情")
	@GetMapping("/mobileApprove")
	public AjaxJson<AbReceiveBillApprovePageVO> mobileApprove(String businessId, String taskId) {
		AbReceiveBillApprovePageVO pageVO = new AbReceiveBillApprovePageVO();
		if (StringUtils.isNotEmpty(businessId)) {
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(businessId);
			pageVO.setInfo(info);
			pageVO.setIsApprove(true);
		}
		pageVO.setTaskId(taskId);
		pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_REC);
		pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);
		return success(pageVO);
	}
	/**
	 * 判断前手是否是内部成员单位
	 *
	 * @param name
	 * @return
	 */
	@ApiOperation(value = "判断前手是否是内部成员单位")
	@GetMapping("/getCorpByName")
	public AjaxJson<Boolean> getCorpByName(String name) {
		return success(abReceiveBillService.getCorpByName(name));
	}
	/**
	 * 根据单位名称查询单位id
	 *
	 * @param name
	 * @return
	 */
	@ApiOperation(value = "根据单位名称查询单位id")
	@GetMapping("/getCorpIdByName")
	public AjaxJson<String> getCorpIdByName(String name) {
		String corpId = abReceiveBillService.getCorpIdByName(name);
		AjaxJson<String> json = success(corpId);
		json.setSuccess(StringUtils.isNotEmpty(corpId));
		return json;
	}
	/**
	 * 根据corpId判断单位是否为网点
	 *
	 * @param corpId
	 * @return
	 */
	@ApiOperation(value = "根据corpId判断单位是否为网点")
	@GetMapping("/isNetByCorpId")
	public AjaxJson<Boolean> isNetByCorpId(String corpId) {
		return success(abReceiveBillService.isNetByCorpId(corpId));
	}
	/**
	 * 根据corpId判断单位是否为网点
	 *
	 * @param corpId
	 * @return
	 */
	@ApiOperation(value = "根据corpId判断单位是否为网点")
	@GetMapping("/isTopNetByCorpId")
	public AjaxJson<CorpDTO> isTopNetByCorpId(String corpId) {
		CorpDTO dto = abReceiveBillService.isTopNetByCorpId(corpId);
		AjaxJson<CorpDTO> json = success(dto);
		json.setSuccess(dto != null && corpId.equals(dto.getNetId()));
		return json;
	}

	/**
	 * 组装附件信息
	 *
	 * @param pageVO
	 * @param accId
	 */
	private void setAttachment(AbReceiveBillApprovePageVO pageVO, String accId) {
		AbBillAttachmentVO attVo = abBillAttachmentService.viewAttachment(accId);
		pageVO.setAttachmentInfo(attVo);
		if (StringUtils.isNotEmpty(attVo.getBriefId()) && AccebillConstant.BRIEF_STATUS_011.equals(attVo.getBriefId())) {
			AbBillAttachmentVO attachmentInfoDiscount = abBillAttachmentService.viewAttachmentByDiscount(accId);
			pageVO.setAttachmentInfoDiscount(attachmentInfoDiscount);
		}
		pageVO.setIsAttachmentShow(abBillAttachmentService.isAttachmentShow(attVo.getBriefId()));
		pageVO.setBankTypeName(StringUtils.isNotEmpty(attVo.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(attVo.getVvid()) : null);
	}
}
