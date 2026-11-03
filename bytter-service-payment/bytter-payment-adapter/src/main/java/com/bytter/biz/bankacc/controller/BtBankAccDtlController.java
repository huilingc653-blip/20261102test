package com.bytter.biz.bankacc.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.entity.BtBankAccDtl;
import com.bytter.biz.inneraccount.service.BtBankAccDtlService;
import com.bytter.biz.inneraccount.vo.BisAccDtlHisVO;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlRpaVO;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlVO;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.service.ExcelCacheService;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号明细录入表
 * @date 2017-06-29 13:51:01
 */
@Controller
@RequestMapping("/bankacc/btbankaccdtl")
public class BtBankAccDtlController extends BaseController {
	@Autowired
	private BtBankAccDtlService btBankAccDtlService;
	@Autowired(required=false)
	private ExcelCacheService excelCacheService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	/**
	 * 银行账号明细录入表列表 页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY)
	@LogMessage(content = "查询银行账号明细录入表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bankacc/listBtBankAccDtl";
	}
	/**
	 * 银行账号明细录入表 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return btBankAccDtlService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 银行账号明细录入表新增页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY)
	@GetMapping("/add")
	public String add(BtBankAccDtl btBankAccDtl, Model model) {
		if (StringUtils.isNotEmpty(btBankAccDtl.getId())) {
			btBankAccDtl = btBankAccDtlService.get(btBankAccDtl.getId());
			model.addAttribute("btBankAccDtlPage", btBankAccDtl);
		}
		int month = (DateUtils.getCalendar().get(Calendar.MONTH)) + 1;
		model.addAttribute("month", month);
		// 当前操作人信息
		ICurrentUser userVO = this.getCurrentUser();
		model.addAttribute("createBy", userVO.getLoginName());
		model.addAttribute("createDate", DateUtils.getDate());
		String sysDate = DateUtils.formatDate(DateUtils.getDate(),DateUtils.PATTERN_CLASSICAL_SIMPLE);
		sysDate += " 23:59:59";
		model.addAttribute("sysDate", sysDate);
		return "bankacc/addBtBankAccDtl";
	}
	/**
	 * 银行账号明细录入表编辑页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_CHANGE},
//			logical = Logical.OR)
	@GetMapping("/update")
	public String update(BtBankAccDtl btBankAccDtl, Model model) {
		if (StringUtils.isNotEmpty(btBankAccDtl.getId())) {
			btBankAccDtl = btBankAccDtlService.get(btBankAccDtl.getId());
			model.addAttribute("btBankAccDtlPage", btBankAccDtl);
			BtBankAccDtlVO btBankAccDtlVO = btBankAccDtlService.getByUnion(btBankAccDtl.getId());
			if (!EmptyUtils.isEmpty(btBankAccDtlVO)) {
				model.addAttribute("curName", btBankAccDtlVO.getCurName());
				model.addAttribute("bifName", btBankAccDtlVO.getBifName());
			}
		}
		return "bankacc/updateBtBankAccDtl";
	}
	/**
	 * 银行账号明细录入表详情页
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY)
	@LogMessage(content = "查询银行账号明细录入表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BtBankAccDtlVO btBankAccDtl = btBankAccDtlService.getByUnion(id);
		model.addAttribute("btBankAccDtlPage", btBankAccDtl);
		model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_ACCDTL);
		return "bankacc/viewBtBankAccDtl";
	}
	/**
	 * 添加银行账号明细录入表
	 *
	 * @param btBankAccDtl
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY)
	@LogMessage(content = "添加银行账号明细录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtBankAccDtl btBankAccDtl) {
		btBankAccDtlService.saveBtBankAccDtl(btBankAccDtl, this.getCurrentUser());
		//刷新余额
		btBankAccDtlService.updateAfterBtBankAccDtlForFlush(btBankAccDtl,null);
		return success();
	}
	/**
	 * 修改银行账号明细录入表
	 *
	 * @param btBankAccDtl
	 * @return
	 */
	////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_CHANGE},
//			logical = Logical.OR)
	@LogMessage(content = "修改银行账号明细录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BtBankAccDtl btBankAccDtl) {
		BtBankAccDtl t = btBankAccDtlService.get(btBankAccDtl.getId());
		BeanUtils.copyNotNullProperties(btBankAccDtl, t);
		btBankAccDtlService.updateBtBankAccDtl(t, this.getCurrentUser());
		//刷新余额
		btBankAccDtlService.updateAfterBtBankAccDtlForFlush(t,null);
		return success();
	}
	/**
	 * 删除银行账号明细录入表
	 *
	 * @param id
	 * @return
	 */
	////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_DEFEAT},
//			logical = Logical.OR)
	@LogMessage(content = "删除银行账号明细录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		this.btBankAccDtlService.deleteBtBankAccDtl(id, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除银行账号明细录入表
	 *
	 * @param ids
	 * @return
	 */
	////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_DEFEAT},
//			logical = Logical.OR)
	@LogMessage(content = "批量删除银行账号明细录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@RequestParam("ids") String ids, @RequestParam("opType") String opType) {
		this.btBankAccDtlService.batchDeleteBtBankAccDtl(ids, opType, this.getCurrentUser());
		return success();
	}
	/**
	 * 查询最近一天的银行行账号余额
	 *
	 * @param btBankAccDtlVO
	 * @return
	 */
	@RequestMapping(params = "returnDownBankBal")
	@ResponseBody
	public String returnDownBankBal(BtBankAccDtlVO btBankAccDtlVO) {
		if (EmptyUtils.isBlank(btBankAccDtlVO.getBankAcc()) ||
				EmptyUtils.isBlank(btBankAccDtlVO.getCurId()) || EmptyUtils.isEmpty(btBankAccDtlVO.getTransTime())) {
			return "0.00";
		}
		String cdSign = btBankAccDtlVO.getCdSign();
		String rbSign = btBankAccDtlVO.getRbSign();
		String bankAcc = btBankAccDtlVO.getBankAcc();
		String amt = EmptyUtils.isEmpty(btBankAccDtlVO.getAmt()) ? null : btBankAccDtlVO.getAmt().toString();
		Date transTime = btBankAccDtlVO.getTransTime();
		String curId = btBankAccDtlVO.getCurId();
		Integer orderNo = btBankAccDtlVO.getOrderNo();
		BigDecimal newBal = btBankAccDtlService.getDownBankBal(cdSign, rbSign, bankAcc, amt, transTime, curId, orderNo);
		if (EmptyUtils.isEmpty(newBal)) {
			return "0.00";
		}
		return String.valueOf(newBal);
	}
	/**
	 * 查询某账户本月未提交的流水记录
	 *
	 * @param btBankAccDtlVO
	 * @return
	 */
	@PostMapping("/getTempBtBankAccDtlInfo")
	@ResponseBody
	public List<BtBankAccDtlVO> getTempBtBankAccDtlInfo(BtBankAccDtlVO btBankAccDtlVO) {
		if (EmptyUtils.isBlank(btBankAccDtlVO.getBankAcc()) ||
				EmptyUtils.isBlank(btBankAccDtlVO.getCurId())) {
			return null;
		}
		List<BtBankAccDtlVO> list = this.btBankAccDtlService.getTempDtlByMonth(btBankAccDtlVO.getBankAcc(), btBankAccDtlVO.getCurId(), btBankAccDtlVO.getMonths());
		if (EmptyUtils.isEmptyList(list)) {
			return new ArrayList<BtBankAccDtlVO>();
		}
		return list;
	}
	/**
	 * 批量提交暂存的流水记录
	 *
	 * @param btBankAccDtlVO
	 * @return
	 */
	@PostMapping("/batchSubmitTempDtl")
	@ResponseBody
	public AjaxJson batchSubmitTempDtl(@RequestBody BtBankAccDtlVO btBankAccDtlVO) {
		this.btBankAccDtlService.batchSubmitTempDtl(btBankAccDtlVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 跳转调整页面
	 *
	 * @return
	 */
	@GetMapping("/adjust")
	public String adjust(BtBankAccDtlVO btBankAccDtlVO, Model model) {
		model.addAttribute("btBankAccDtlVO", btBankAccDtlVO);
		return "bankacc/updateBtBankAccDtlAdjust";
	}
	/**
	 * 调整暂存流水的排序号
	 *
	 * @param btBankAccDtlVO
	 * @return
	 */
	@PostMapping("/adjustOrderNo")
	@ResponseBody
	public AjaxJson adjustOrderNo(BtBankAccDtlVO btBankAccDtlVO) {
		this.btBankAccDtlService.updateOrderNo(btBankAccDtlVO);
		return success();
	}
	/**
	 * 银行账户流水查询
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/query")
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY)
	public String query(String corpId,String busType,String accountGroup,String beginDate,String endDate,Model model) {
		if (ObjectUtil.isNotEmpty(corpId)){
			model.addAttribute("corpId",corpId);
		}
		if (ObjectUtil.isNotEmpty(busType)){
			model.addAttribute("busType",busType);
		}
		if (ObjectUtil.isNotEmpty(accountGroup)){
			model.addAttribute("accountGroup",accountGroup);
		}
		if (ObjectUtil.isNotEmpty(beginDate)){
			model.addAttribute("beginDate",beginDate);
		}
		if (ObjectUtil.isNotEmpty(endDate)){
			model.addAttribute("endDate",endDate);
		}
		return "bankacc/listBisAccDtlQuery";
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
		return "bankacc/listBtBankAccDtlApprove";
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
	public DataTablesOutput getDataTablesApprove(@RequestBody DataTablesInput dataTablesInput) {
		return this.btBankAccDtlService.getDataTablesApprove(dataTablesInput);
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableAdjust")
	@ResponseBody
	public DataTablesOutput getDataTablesAdjust(@RequestBody DataTablesInput dataTablesInput) {
		return this.btBankAccDtlService.getDataTablesAdjust(dataTablesInput);
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
	public String approve(String id, String taskId, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			BtBankAccDtlVO vo = this.btBankAccDtlService.getByUnion(id);
			model.addAttribute("btBankAccDtlPage", vo);
			model.addAttribute("taskId", taskId);
			model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_ACCDTL);
		}
		return "bankacc/approveBtBankAccDtl";
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
			BtBankAccDtlVO vo = this.btBankAccDtlService.getByUnion(businessId);
			model.addAttribute("btBankAccDtlPage", vo);
			model.addAttribute("taskId", taskId);
		}
		return "bankacc/viewApproveBtBankDtl";
	}
	/**
	 * 保存导入数据
	 *
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<BtBankAccDtlVO> list) {
		this.btBankAccDtlService.saveImport(list, this.getCurrentUser());
		//重新刷新余额
//		this.btBankAccDtlService.flushBal(list);
		return success();
	}

	@PostMapping("/saveRpaImport")
	@ResponseBody
	public AjaxJson saveRpaImport(@RequestBody List<BtBankAccDtlRpaVO> list) {
		this.btBankAccDtlService.saveRpaImport(list, this.getCurrentUser());
		return success();
	}

	/**
	 * 浪潮历史流水导入
	 * @param cacheCode
	 * @return
	 */
	@PostMapping("/saveHisDtlImport")
	@ResponseBody
	public AjaxJson saveHisDtlImport(@RequestParam("cacheCode") String cacheCode) {
		AjaxJson reJson = new AjaxJson();
		reJson.setSuccess(false);
		List<BtBankAccDtlVO> list = excelCacheService.getListExcelDataCache(cacheCode, BtBankAccDtlVO.class);
		btBankAccDtlService.saveBisAccDtl(list);
		reJson=success();
		return reJson;
	}
	/**
	 * 批量提交银行账号明细录入表
	 *
	 * @param ids
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY)
	@LogMessage(content = "批量提交银行账号明细录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHCONSENT)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
		this.btBankAccDtlService.batchSubmitBtBankAccDtl(ids, this.getCurrentUser());
		return success();
	}
	/**
	 * 智能导入
	 * @param model
	 * @return
	 */
	@GetMapping("/importByConfig")
	public String importByConfig(Model model) {
		return "bankacc/importBtBankAccDtlByConfig";
	}
	/**
	 * 智能导入银行流水数据
	 * @param cacheCode
	 * @return
	 */
	@LogMessage(content = "智能导入银行流水数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
	@PostMapping("/saveImportByConfig")
	@ResponseBody
	public AjaxJson saveImportByConfig(@RequestParam("cacheCode") String cacheCode) {
		AjaxJson reJson = new AjaxJson();
		reJson.setSuccess(false);
		List<BtBankAccDtlVO> list = excelCacheService.getListExcelDataCache(cacheCode, BtBankAccDtlVO.class);
		btBankAccDtlService.saveImport(list, this.getCurrentUser());
		//重新刷新余额
//		this.btBankAccDtlService.flushBal(list);
		reJson=success();
		return reJson;
	}
	/**
	 * 获取付款账号
	 *
	 * @param bankType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getBankAcc")
	@ResponseBody
	public List<BtBankAccDTO> getBankAcc(String bankType) {
		List<BtBankAccDTO> listBankAcc = new ArrayList<>();
		if (!StringUtils.isEmpty(bankType.trim())) {
			//获取转账权限的银行账号
			List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), null, ResourceConstant.NETBANK_BANKACC_BISACCBAL_MANAGERQUERY, null, AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
			if (!EmptyUtils.isEmptyList(listBankAccTemp)) {
				for (BtBankAccDTO dto : listBankAccTemp) {
					//账号属性对外付款不为收款的账号才能对外付款
					if (bankType.equals(dto.getBankTypeId())) {
						listBankAcc.add(dto);
					}
				}
			}
		}
		return listBankAcc;
	}
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY)
	@GetMapping("/updateBal")
	public String updateBal(Model model) {
		model.addAttribute("sysDate", DateUtils.formatDate(DateUtils.getDateYMD(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return "bankacc/updateBankAccDtlBal";
	}
	/**
	 * 由明细生成余额saveBisAccDtl
	 *
	 * @param bankAccDtlVO
	 * @return
	 */
	@PostMapping("/flushBal")
	@ResponseBody
	public AjaxJson flushBal(@RequestBody BtBankAccDtlVO bankAccDtlVO) {
		AjaxJson json = new AjaxJson();
		this.btBankAccDtlService.flushBal(bankAccDtlVO);
		json.setMessageEnum(CommonMessageEnum.COMMON_MESSAGE_SUCCESS);
		json.setSuccess(true);
		return json;
	}
}
