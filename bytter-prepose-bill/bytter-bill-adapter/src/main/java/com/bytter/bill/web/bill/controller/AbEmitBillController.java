package com.bytter.bill.web.bill.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbBillBankAutoDtl;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: 开票登记Controller
 * @Description: AB_VOUCHERINFO
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 */
@Controller
@RequestMapping("/bill/accebill/abemitbill")
public class AbEmitBillController extends BaseController {
	@Autowired
	private AbEmitBillService abEmitBillService;
	@Autowired
	private AbLogService abLogService;
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
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbRatCpUrationService abRatCpUrationService;
	@Autowired
	private AbFieldIsmdfService abFieldIsmdfService;
	@Autowired
	private AbBillQueryService abBillQueryService;
	@Autowired
	private AbBillBankAutoDtlService abBillBankAutoDtlService;
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	 * AB_VOUCHERINFO列表 页面跳转
	 *
	 * @return 前端路径
	 */
	@GetMapping("/list")
	public String list(String payImport,Model model) {
		model.addAttribute("payImport",payImport);
		return "bill/accebill/listAbEmitBill";
	}
	/**
	 * AB_VOUCHERINFO table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询开票登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return abEmitBillService.getDataTablesUnion(input);
	}

	/**
	 * 招行协议文本信息查询
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "招行协议文本信息查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/selectCmbCrlinfoByCVMNo")
	@ResponseBody
	public CmbCrlinfoVo selectCmbCrlinfoByCVMNo(@RequestBody CmbCrlinfoVo vo) {
		return abEmitBillService.selectCmbCrlinfoByCVMNo(vo.getCreditlineid());
	}

	/**
	 * 招行合作银行号查询
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "招行合作银行号查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/dataTableCooperativeBankNumber")
	@ResponseBody
	public DataTablesOutput dataTableCooperativeBankNumber(@RequestBody DataTablesInput input) {
		return abEmitBillService.dataTableCooperativeBankNumber(input);
	}

	@PostMapping("/exportSelData")
	public String exportPayExcelByMap(String params, String ids, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 转换前台传来的数据为JSON
		params= URLDecoder.decode(params,"utf-8").replaceAll("&quot;","\"");
		ids = URLDecoder.decode(ids,"utf-8").replaceAll("&quot;","\"");
		// 用于排除当出现多余的列
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DataTablesInput dataTablesInput = mapper.readValue(params, DataTablesInput.class);
		dataTablesInput.getFormdata().put("ins_aid",ids);
		dataTablesInput.setTitle("开票登记导出");
		dataTablesInput.setFilename("开票登记导出");
		// 配置导出要素
		List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
		DataTablesOutput dataTablesUnion = abEmitBillService.getDataTablesUnion(dataTablesInput);
		List<AbEmitBillVo> data = dataTablesUnion.getData();
		if(CollectionUtil.isEmpty(data)){
			return null;
		}
		modelMap.put("dataTablesInput",dataTablesInput);
		abBillQueryService.listSelectData(dataTablesInput, modelMap, entityList, request, response,data);
		return null;
	}

	/**
	 * 招行风险合同信息表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "招行风险合同信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/dataTableCMBCRLCONInfo")
	@ResponseBody
	public DataTablesOutput dataTableCMBCRLCONInfo(@RequestBody DataTablesInput input) {
		return abEmitBillService.dataTableCMBCRLCONInfo(input);
	}

	/**
	 * 招行风险合同信息表 页面跳转
	 *
	 * @return 前端路径
	 */
	@GetMapping("/cmbCrlconList")
	public String cmbCrlconList(CmbCrlconVo vo, Model model) {
		model.addAttribute("creditlineid",vo.getCreditlineid());
		return "bill/accebill/selectRiskContractNumberPage";
	}

	/**
	 * 招行合作银行号查询 页面跳转
	 *
	 * @return 前端路径
	 */
	@GetMapping("/cooperativeBankNumberList")
	public String cooperativeBankNumberList(CmbBnkbilnegVo vo, Model model) {
		model.addAttribute("brnnb",vo.getBrnnbr());
		return "bill/accebill/selectCooperativeBankNumberPage";
	}

	/**
	 * 开票登记新增页面跳转
	 *
	 * @return
	 */
	@GetMapping("/add")
	public String add(AbEmitBillVo abEmitBillVo, Model model) {
		String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO);
		model.addAttribute("appNo", appNo);
		if(AccebillConstant.BILL_ISBATCHBILL.equals(abEmitBillVo.getIsBatchBill())){
			String batchNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_BATCHNO);
			model.addAttribute("batchNo", batchNo);
			model.addAttribute("isBatchBill", AccebillConstant.BILL_ISBATCHBILL);
		}
		// 挂预算系统参数
		if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
			model.addAttribute("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
		}
		// 挂授信
		boolean isRelateRat = abRatUrationService.isRelateRat();
		// 挂内部授信
		boolean isOpenCpRat = abRatCpUrationService.isRelateCpRat();
		model.addAttribute("isRelateRat", isRelateRat);
//		if (isRelateRat || isOpenCpRat) {
		model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
//		}
		model.addAttribute("externalCorpNum",abVoucherinfoViceService.getExternalCorpNum());
		model.addAttribute("isEmitMake",abVoucherinfoViceService.isEmitMake());
		//是否挂保证金
		model.addAttribute("isBail",abFcBondHookService.isBail());
		//挂保证金银行类型
		model.addAttribute("bailBankControlType",abFcBondHookService.bailBankControlType());
		//是否启用授信敞口
		model.addAttribute("mouthRat",abRatUrationService.isNotOpenRat());
		//是否启用担保参数
		model.addAttribute("isGuarantee",abGuaranteeService.isHangGuarantee());
		model.addAttribute("isHangGuarantee",AccebillConstant.AB_BILL_VALID_SIGN_1);
		//获取纸票票据金额最大额度
		model.addAttribute("maxBillMoney",abVoucherinfoViceService.getMaxBillMoney());
		//增加是否挂内部授信
		model.addAttribute("isOpenCpRat",isOpenCpRat);
		model.addAttribute("externalCorp", abConfigService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
		return "bill/accebill/addAbEmitBill";
	}
	/**
	 * 开票登记编辑页面跳转
	 *
	 *
	 * @return
	 */
	@GetMapping("/update/{aid}")
	public String update(@PathVariable("aid") String aid,HttpServletRequest request, Model model) {
		model.addAllAttributes(abEmitBillService.dealBillInfo(aid,request.getParameter("isBatchBill"),request.getParameter("batchNo")));
		return "bill/accebill/updateAbEmitBill";
	}
	/**
	 * 开票登记详情页
	 *
	 * @param aid
	 * @param model
	 * @return
	 */
	@LogMessage(content = "查询开票登记详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping(value = {"/view/{aid}/{payId}", "/view/{aid}"})
	public String view(@PathVariable("aid") String aid, @PathVariable(value = "payId", required = false) String payId, Model model) {
		AbEmitBillVo vo = abEmitBillService.getAbEmitBillVoByAid(aid,false);
		if (ObjectUtil.isNotEmpty(vo.getBusCorpId())) {
			String busCorpName = corpService.getCorpName(vo.getBusCorpId());
			vo.setBusCorpName(busCorpName);
		}
		List<AbLog> abLogList = abLogService.listAbLog(vo.getVvid(), null);
		if (StringUtils.isNotEmpty(payId)) {
			vo.setVoucherId(payId);
		}

		model.addAttribute("abEmitBillVo", vo);
		model.addAttribute("fcBondRate",vo.getBailrate());
		model.addAttribute("abLogList", abLogList);
		model.addAttribute("billId", vo.getVvid());
		// 挂预算系统参数
		if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
			model.addAttribute("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
			model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
			model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
			List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
					BudgetConstant.BUSINESS_NAME_EMIT, BudgetConstant.PROCESS_SYSNAME_AB, vo.getVvid());
			if (list != null && !list.isEmpty()) {
				// 显示预算
				model.addAttribute("budgetDisplay", "true");
			}
		}
		//挂授信 显示
		if(!StringUtils.isBlank(vo.getRatmodel()) && vo.getRatmodel().equals("1")){
			model.addAttribute("isEnabled",true);
		}else{
			model.addAttribute("isEnabled",false);
		}
		//判断是否有已挂保证金数据
		FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(),vo.getAppNo());
		String bailmodel = FcBondConstant.IS_HOOK_N;
		if(fcBondApplyDTO != null){
			bailmodel = FcBondConstant.IS_HOOK_Y;
		}
		model.addAttribute("bailmodel",bailmodel);
		abBillAttachmentService.viewAttachment(aid, model);
		abEmitBillService.viewBusInfo(vo, model);
		//是否启用担保参数
		model.addAttribute("isHangGuarantee",vo.getIsHangGuarantee()==null?FcBondConstant.IS_HOOK_N:vo.getIsHangGuarantee());
		model.addAttribute("appNo",vo.getAppNo());
		//挂内部授信 显示
		if(!StringUtils.isBlank(vo.getInsideRatModel()) && vo.getInsideRatModel().equals(FcBondConstant.IS_HOOK_Y)){
			model.addAttribute("cpIsEnabled",true);
			model.addAttribute("cpURationDTO",abRatCpUrationService.getRatUrationDTOByBillId(vo.getVvid()));
		}else{
			model.addAttribute("cpIsEnabled",false);
		}
		model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
		return "bill/accebill/viewAbEmitBill";
	}
	/**
	 * 查看开票票据详情页面
	 *
	 * @return
	 */
	@LogMessage(content = "查询开票登记详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	public String view(String viceId, String accId, boolean isShowPool, Model model) {
		AbEmitBillVo vo = new AbEmitBillVo();
		//担保业务提供外部参数特殊处理:查询开票详情
		if(!StringUtils.isBlank(viceId) && StringUtils.isBlank(accId)){
			vo = abEmitBillService.getAbEmitBillVoByAid(
					abAccrecordsService.getBillByVid(viceId,AccebillConstant.BRIEF_STATUS_001).getId(),false);
		}else{
			vo = abEmitBillService.getAbEmitBillVoByAid(accId,false);
		}
		if (isShowPool) {
			AbVoucherinfoVO abVoucherinfoVO = new AbVoucherinfoVO();
			abVoucherinfoVO.setOrigNo(vo.getOrigNo());
			abBillQueryService.getBillPoolInfo(abVoucherinfoVO);
			model.addAttribute("info", abVoucherinfoVO);
		}
		List<AbLog> abLogList = abLogService.listAbLog(viceId, null);
		model.addAttribute("abEmitBillVo", vo);
		model.addAttribute("fcBondRate",vo.getBailrate());
		model.addAttribute("abLogList", abLogList);
		model.addAttribute("billId", vo.getVvid());
		// 挂预算系统参数
		if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
			model.addAttribute("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
			model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
			model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
		}
		// 挂授信
		boolean isRelateRat = abRatUrationService.isRelateRat();
		model.addAttribute("isRelateRat", isRelateRat);
		if (isRelateRat) {
			model.addAttribute("urationDTO", abRatUrationService.getRatUrationDTOByBillId(vo.getVvid()));
		}
		//挂授信 显示
		if(!StringUtils.isBlank(vo.getRatmodel()) && vo.getRatmodel().equals("1")){
			model.addAttribute("isEnabled",true);
		}else{
			model.addAttribute("isEnabled",false);
		}
		//判断是否有已挂保证金数据
		FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(),vo.getAppNo());
		String bailmodel = FcBondConstant.IS_HOOK_N;
		if(fcBondApplyDTO != null){
			bailmodel = FcBondConstant.IS_HOOK_Y;
		}
		model.addAttribute("bailmodel",bailmodel);
		//是否启用担保参数
		model.addAttribute("isHangGuarantee",vo.getIsHangGuarantee()==null?FcBondConstant.IS_HOOK_N:vo.getIsHangGuarantee());
		model.addAttribute("appNo",vo.getAppNo());
		model.addAttribute("isShowPool", isShowPool);
		abBillAttachmentService.viewAttachment(accId, model);
		return "bill/accebill/viewAbEmitBill";
	}
	/**
	 * 新增-保存开票登记
	 *
	 * @param abEmitBillVo
	 *            登记信息vo
	 * @param urationDTO
	 *            授信VO
	 * @return
	 */
	@LogMessage(content = "新增-保存开票登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(AbEmitBillVo abEmitBillVo, RatUrationDTO urationDTO) {
		//判断挂银行授信
		if(AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getRatmodel())){
			urationDTO.setCorpId(abEmitBillVo.getRatBankCorpId());//银行授信单位CorpId
		}
		abEmitBillVo.setCreditCode(urationDTO.getRatItemCode());
		String busCorpName=corpService.getCorpName(abEmitBillVo.getBusCorpId());
		abEmitBillVo.setBusCorpName(busCorpName);
		// todo:数据转换
		// com.bytter.bill.api.bill.dto.RatUrationDTO ratUrationDTO = BeanUtils.copyNewInstanceProperties(urationDTO, com.bytter.bill.api.bill.dto.RatUrationDTO.class);
		abEmitBillVo.setUrationDTO(urationDTO);
		abEmitBillService.saveOrUpdate(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, this.getCurrentUser());
		AjaxJson rejson= success();
		if(AccebillConstant.BILL_ISBATCHBILL.equals(abEmitBillVo.getIsBatchBill())){
			Map<String,Object> attributes=new HashMap<>();
			attributes.put("appNo",SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO));
			attributes.put("aids",abEmitBillVo.getId());
			attributes.put("vids",abEmitBillVo.getVid());
			attributes.put("vvids",abEmitBillVo.getVvid());
			rejson.setAttributes(attributes);
		}
		return rejson;
	}
	/**
	 * 提交开票登记
	 *
	 * @param abEmitBillVo
	 * @return
	 */
	@LogMessage(content = "提交开票登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit/{operate}")
	@ResponseBody
	public AjaxJson submit(AbEmitBillVo abEmitBillVo, @PathVariable("operate") String operate,
						   RatUrationDTO urationDTO) {
		//判断挂银行授信
		if(AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getRatmodel())){
			urationDTO.setCorpId(abEmitBillVo.getRatBankCorpId());//银行授信单位CorpId
		}
		abEmitBillVo.setCreditCode(urationDTO.getRatItemCode());
		// todo:数据转换
		// com.bytter.bill.api.bill.dto.RatUrationDTO ratUrationDTO = BeanUtils.copyNewInstanceProperties(urationDTO, com.bytter.bill.api.bill.dto.RatUrationDTO.class);
		abEmitBillVo.setUrationDTO(urationDTO);
		abEmitBillService.submit(abEmitBillVo, operate, this.getCurrentUser());
		return success();
	}
	/**
	 * 修改保存开票登记信息
	 *
	 * @param abEmitBillVo
	 *            登记信息vo
	 * @param urationDTO
	 *            授信VO
	 * @return
	 */
	@LogMessage(content = "修改保存开票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(AbEmitBillVo abEmitBillVo, RatUrationDTO urationDTO) {
		//判断挂银行授信
		if(AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getRatmodel())){
			urationDTO.setCorpId(abEmitBillVo.getRatBankCorpId());//银行授信单位CorpId
		}
		// todo:数据转换
		// com.bytter.bill.api.bill.dto.RatUrationDTO ratUrationDTO = BeanUtils.copyNewInstanceProperties(urationDTO, com.bytter.bill.api.bill.dto.RatUrationDTO.class);
		abEmitBillVo.setUrationDTO(urationDTO);
		abEmitBillVo.setCreditCode(urationDTO.getRatItemCode());
		// abEmitBillService.update(abEmitBillVo);
		abEmitBillService.saveOrUpdate(abEmitBillVo, AccebillConstant.BILL_OPERATE_UPDATE, this.getCurrentUser());
		return success();
	}
	/**
	 * 付款开票打回
	 *
	 * @param abEmitBillVo
	 *            登记信息vo
	 * @return
	 */
	@LogMessage(content = "付款开票打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_REPULSE)
	@PostMapping("/repulse")
	@ResponseBody
	public AjaxJson repulse(AbEmitBillVo abEmitBillVo) {
		abEmitBillService.repulse(abEmitBillVo, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除开票登记
	 *
	 * @param aid
	 *            id信息
	 * @param reason 删除原因
	 * @return
	 */
	@LogMessage(content = "删除开票登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete")
	@ResponseBody
	public AjaxJson delete(String aid,String reason) {
		abEmitBillService.delete(aid,reason, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除开票登记信息
	 *
	 * @param aids
	 *            id信息
	 * @param reason
	 *            删除原因
	 * @return
	 */
	@LogMessage(content = "批量删除开票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(String aids,String reason) {
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(abEmitBillService.batchDelete(aids,reason, this.getCurrentUser()));
		return ajaxJson;
	}
	/**
	 * 批量提交开票登记信息
	 *
	 * @param vids
	 *            主信息id
	 * @param vvids
	 *            从表ID
	 * @param aids
	 *            流水ID
	 * @return
	 */
	@LogMessage(content = "批量提交开票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(String vids, String vvids, String aids) {
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(abEmitBillService.batchSubmit(vids, vvids, aids, this.getCurrentUser(), false));
		return ajaxJson;
	}
	/**
	 * 批量提交开票登记信息
	 *
	 * @param vids
	 *            主信息id
	 * @param vvids
	 *            从表ID
	 * @param aids
	 *            流水ID
	 * @return
	 */
	@LogMessage(content = "批次提交开票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchNoSubmit")
	@ResponseBody
	public AjaxJson batchNoSubmit(String vids, String vvids, String aids) {
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(abEmitBillService.batchSubmit(vids, vvids, aids, this.getCurrentUser(), true));
		return ajaxJson;
	}
	/**
	 * 根据收票人账号获取银行信息 用于付款生成的开票登记，编辑时选择对内时校验收票人账号是否存在，不存在则不允许选择对内开票
	 *
	 * @param bankAcc
	 * @return
	 */
	@PostMapping("/getBankDataByPayeeacc")
	@ResponseBody
	public AjaxJson getBankDataByPayeeacc(String bankAcc) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(abEmitBillService.getBankDataByBankAcc(bankAcc));
		return ajaxJson;
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
		return "bill/accebill/approve/listAbEmitBillApprove";
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
		return abEmitBillService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param businessId
	 *            审批流水ID
	 * @param taskId
	 *            流程ID
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			AbEmitBillVo vo = abEmitBillService.getAbEmitBillVoByAid(businessId,false);
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
			abBillAttachmentService.viewAttachment(businessId, model);
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
			//是否启用担保参数
			model.addAttribute("isHangGuarantee",vo.getIsHangGuarantee()==null?FcBondConstant.IS_HOOK_N:vo.getIsHangGuarantee());
			model.addAttribute("appNo",vo.getAppNo());
			//挂内部授信 显示
			if(!StringUtils.isBlank(vo.getInsideRatModel()) && vo.getInsideRatModel().equals(FcBondConstant.IS_HOOK_Y)){
				model.addAttribute("cpIsEnabled",true);
				model.addAttribute("cpURationDTO",abRatCpUrationService.getRatUrationDTOByBillId(vo.getVvid()));
			}else{
				model.addAttribute("cpIsEnabled",false);
			}
			model.addAttribute("curId", abVoucherinfoViceService.getCurIdByCNY());
			model.addAttribute("fcBondRate",vo.getBailrate());
		}
		model.addAttribute("taskId", taskId);
		boolean isRelateRat = abRatUrationService.isRelateRat();
		model.addAttribute("isRelateRat", isRelateRat);
		return "bill/accebill/approve/approveAbEmitBill";
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
			AbEmitBillVo vo = abEmitBillService.getAbEmitBillVoByAid(businessId,false);
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
	 * 开票登记导入
	 *
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<AbEmitBillVo> list) {
		abEmitBillService.saveImport(list, this.getCurrentUser());
		return success();
	}

	/**
	* @Description 打开招行保证金选择界面
    * @author zhous
    * @date 2024/9/6 15:31
	*/
	@GetMapping("/selectZhBondAccPage")
	public String selectExternalCorpPage(Model model, String curacc, String bankType,String accName) {
		if(StrUtil.isNotBlank(curacc)){
			model.addAttribute("curacc",curacc);
		}
		//默认查询招行页面
		String toPage = AccebillConstant.ZH_BANK;
		if(StrUtil.isNotBlank(bankType)){
			toPage = bankType;
		}

		// 资源权限
		if (ObjectUtil.isNotEmpty(accName)){
			model.addAttribute("accName",accName);
		}
		String url = "bill/accebill/select"+toPage +"BondAccPage";
		return url;
	}

	/**
	 * 招行保证金账户信息表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "招行保证金账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/getZhBondAcc")
	@ResponseBody
	public DataTablesOutput getZhBondAcc(@RequestBody DataTablesInput dataTablesInput) {
		return abEmitBillService.getZhBondAccDataTables(dataTablesInput);
	}

	/**
	 * 民生保证金账户信息表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "民生保证金账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/getMsBondAcc")
	@ResponseBody
	public DataTablesOutput getMsBondAcc(@RequestBody DataTablesInput dataTablesInput) {
		return abEmitBillService.getMsBondAccDataTables(dataTablesInput);
	}
}
