package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.inneraccount.entity.BtBankAccDtl;
import com.bytter.biz.inneraccount.service.BtBankAccDtlService;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlRpaVO;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccDtlAddConfigVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccDtlPageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Description: 银行账号流水管理
 * @date 2017-06-29 13:51:01
 */
@Api(value = "银行账号流水管理", tags = "银行账号流水管理")
@RestController("v1BtBankAccDtlController")
@RequestMapping("/v1-payment/bankacc/btbankaccdtl")
public class BtBankAccDtlController extends BaseController {
	@Autowired
	private BtBankAccDtlService btBankAccDtlService;
	@Autowired(required=false)
	private ExcelCacheService excelCacheService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;

	/**
	 * 银行账号流水管理 table列表
	 *
	 * @return
	 */
	@ApiOperation("银行账号流水管理 table列表")
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<BtBankAccDtlVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(btBankAccDtlService.getDataTablesUnion(input, this.getCurrentUser()));
	}
	/**
	 * 银行账号明细录入表新增页面跳转
	 *
	 * @return
	 */
	@ApiOperation("获取新增页面前的配置数据")
	@GetMapping("/getConfigBeforeAdd")
	@ResponseBody
	public AjaxJson<BtBankAccDtlAddConfigVO> getConfigBeforeAdd() {
		BtBankAccDtlAddConfigVO configVO = new BtBankAccDtlAddConfigVO();
		int month = (DateUtils.getCalendar().get(Calendar.MONTH)) + 1;
		configVO.setMonth(month);
		// 当前操作人信息
		ICurrentUser userVO = this.getCurrentUser();
		configVO.setCreateBy(userVO.getLoginName());
		configVO.setCreateDate(DateUtils.getDate());
		String sysDate = DateUtils.formatDate(DateUtils.getDate(),DateUtils.PATTERN_CLASSICAL_SIMPLE);
		sysDate += " 23:59:59";
		configVO.setSysDate(sysDate);
		return success(configVO);
	}
	/**
	 * 银行账号明细录入表编辑页面跳转
	 *
	 * @return
	 */
	@ApiOperation("获取编辑页面前的配置数据")
	@GetMapping("/getInfoBeforeUpdate")
	@ResponseBody
	public AjaxJson<BtBankAccDtlPageVO> getInfoBeforeUpdate(String id) {
		BtBankAccDtlPageVO pageVO = new BtBankAccDtlPageVO();
		if (StringUtils.isNotEmpty(id)) {
			BtBankAccDtl btBankAccDtl = btBankAccDtlService.get(id);
			BtBankAccDtlVO dtlVO = new BtBankAccDtlVO();
			BeanUtils.copyNotNullProperties(btBankAccDtl, dtlVO);
			pageVO.setBtBankAccDtlPage(dtlVO);
			BtBankAccDtlVO btBankAccDtlVO = btBankAccDtlService.getByUnion(btBankAccDtl.getId());

			if (!EmptyUtils.isEmpty(btBankAccDtlVO)) {
				dtlVO.setCurName(btBankAccDtlVO.getCurName());
				dtlVO.setBifName(btBankAccDtlVO.getBifName());
				pageVO.setCurName(btBankAccDtlVO.getCurName());
				pageVO.setBifName(btBankAccDtlVO.getBifName());
			}
		}
		return success(pageVO);
	}
	/**
	 * 银行账号明细录入表详情页
	 *
	 * @return
	 */
	@ApiOperation("查询银行账号明细录入表详情")
	@LogMessage(content = "查询银行账号明细录入表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<BtBankAccDtlPageVO> view(@PathVariable("id") String id) {
		BtBankAccDtlPageVO pageVO = new BtBankAccDtlPageVO();
		BtBankAccDtlVO btBankAccDtl = btBankAccDtlService.getByUnion(id);
		pageVO.setBtBankAccDtlPage(btBankAccDtl);
		pageVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_ACCDTL);
		return success(pageVO);
	}
	/**
	 * 添加银行账号明细录入表
	 *
	 * @param btBankAccDtl
	 * @return
	 */
	@ApiOperation("添加银行账号明细录入表")
	@LogMessage(content = "添加银行账号明细录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestBody BtBankAccDtl btBankAccDtl) {
		btBankAccDtlService.saveBtBankAccDtl(btBankAccDtl, this.getCurrentUser());
		return success();
	}
	/**
	 * 修改银行账号明细录入表
	 *
	 * @param btBankAccDtl
	 * @return
	 */
	@ApiOperation("修改银行账号明细录入表")
	@LogMessage(content = "修改银行账号明细录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(@RequestBody BtBankAccDtl btBankAccDtl) {
		BtBankAccDtl t = btBankAccDtlService.get(btBankAccDtl.getId());
		BeanUtils.copyNotNullProperties(btBankAccDtl, t);
		btBankAccDtlService.updateBtBankAccDtl(t, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除银行账号明细录入表
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("删除银行账号明细录入表")
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
	@ApiOperation("批量删除银行账号明细录入表")
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
	@ApiOperation("查询最近一天的银行行账号余额")
	@PostMapping(path = "/returnDownBankBal")
	@ResponseBody
	public AjaxJson<String> returnDownBankBal(@RequestBody BtBankAccDtlVO btBankAccDtlVO) {
		if (EmptyUtils.isBlank(btBankAccDtlVO.getBankAcc()) ||
				EmptyUtils.isBlank(btBankAccDtlVO.getCurId()) || EmptyUtils.isEmpty(btBankAccDtlVO.getTransTime())) {
			return success("0.00");
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
			return success("0.00");
		}
		return success(String.valueOf(newBal));
	}
	/**
	 * 查询某账户本月未提交的流水记录
	 *
	 * @param btBankAccDtlVO
	 * @return
	 */
	@ApiOperation("查询某账户本月未提交的流水记录")
	@PostMapping("/getTempBtBankAccDtlInfo")
	@ResponseBody
	public AjaxJson<List<BtBankAccDtlVO>> getTempBtBankAccDtlInfo(@RequestBody BtBankAccDtlVO btBankAccDtlVO) {
		if (EmptyUtils.isBlank(btBankAccDtlVO.getBankAcc()) ||
				EmptyUtils.isBlank(btBankAccDtlVO.getCurId())) {
			return null;
		}
		List<BtBankAccDtlVO> list = this.btBankAccDtlService.getTempDtlByMonth(btBankAccDtlVO.getBankAcc(), btBankAccDtlVO.getCurId(), btBankAccDtlVO.getMonths());
		if (EmptyUtils.isEmptyList(list)) {
			return success(new ArrayList<BtBankAccDtlVO>());
		}
		return success(list);
	}
	/**
	 * 批量提交暂存的流水记录
	 *
	 * @param btBankAccDtlVO
	 * @return
	 */
	@ApiOperation("批量提交暂存的流水记录")
	@PostMapping("/batchSubmitTempDtl")
	@ResponseBody
	public AjaxJson batchSubmitTempDtl(@RequestBody BtBankAccDtlVO btBankAccDtlVO) {
		this.btBankAccDtlService.batchSubmitTempDtl(btBankAccDtlVO, this.getCurrentUser());
		return success();
	}

	/**
	 * 调整暂存流水的排序号
	 *
	 * @param btBankAccDtlVO
	 * @return
	 */
	@ApiOperation("调整暂存流水的排序号")
	@PostMapping("/adjustOrderNo")
	@ResponseBody
	public AjaxJson adjustOrderNo(@RequestBody BtBankAccDtlVO btBankAccDtlVO) {
		this.btBankAccDtlService.updateOrderNo(btBankAccDtlVO);
		return success();
	}

	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@ApiOperation("审批列表")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public AjaxJson<DataTablesOutput<WorkFlowPersistence>> getDataTablesApprove(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(this.btBankAccDtlService.getDataTablesApprove(input));
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@ApiOperation("审核获取调整列表的数据")
	@PostMapping("/dataTableAdjust")
	@ResponseBody
	public AjaxJson<DataTablesOutput<WorkFlowPersistence>> getDataTablesAdjust(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(this.btBankAccDtlService.getDataTablesAdjust(input));
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @return String
	 */
	@ApiOperation("审核详情界面前数据获取")
	@GetMapping("/approve")
	@ResponseBody
	public AjaxJson<BtBankAccDtlPageVO> approve(String id, String taskId) {
		BtBankAccDtlPageVO pageVO = new BtBankAccDtlPageVO();
		if (StringUtils.isNotEmpty(id)) {
			BtBankAccDtlVO vo = this.btBankAccDtlService.getByUnion(id);
			pageVO.setBtBankAccDtlPage(vo);
			pageVO.setTaskId(taskId);
			pageVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_ACCDTL);
			return success(pageVO);
		}
		return success();
	}
	/**
	 * 跳转至手机审核详情界面
	 *
	 * @param businessId
	 * @param taskId
	 * @return String
	 */
	@ApiOperation("跳转至手机审核详情界面前数据获取")
	@GetMapping("/mobileApprove")
	@ResponseBody
	public AjaxJson<BtBankAccDtlPageVO> mobileApprove(String businessId, String taskId) {
		BtBankAccDtlPageVO pageVO = new BtBankAccDtlPageVO();
		if (StringUtils.isNotEmpty(businessId)) {
			BtBankAccDtlVO vo = this.btBankAccDtlService.getByUnion(businessId);
			pageVO.setBtBankAccDtlPage(vo);
			pageVO.setTaskId(taskId);
			return success(pageVO);
		}
		return success();
	}
	/**
	 * 保存导入数据
	 *
	 * @param list
	 * @return
	 */
	@ApiOperation("保存导入数据")
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<BtBankAccDtlVO> list) {
		this.btBankAccDtlService.saveImport(list, this.getCurrentUser());
		return success();
	}

	@ApiOperation("导入Rpa")
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
	@ApiOperation("历史流水导入")
	@PostMapping("/saveHisDtlImport")
	@ResponseBody
	public AjaxJson saveHisDtlImport(@RequestParam("cacheCode") String cacheCode) {
		List<BtBankAccDtlVO> list = excelCacheService.getListExcelDataCache(cacheCode, BtBankAccDtlVO.class);
		btBankAccDtlService.saveBisAccDtl(list);
		return success();
	}
	/**
	 * 批量提交银行账号明细录入表
	 *
	 * @param ids
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY)
	@ApiOperation("批量提交银行账号明细录入表")
	@LogMessage(content = "批量提交银行账号明细录入表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHCONSENT)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
		this.btBankAccDtlService.batchSubmitBtBankAccDtl(ids, this.getCurrentUser());
		return success();
	}
	/**
	 * 智能导入银行流水数据
	 * @param cacheCode
	 * @return
	 */
	@ApiOperation("智能导入银行流水数据")
	@LogMessage(content = "智能导入银行流水数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
	@PostMapping("/saveImportByConfig")
	@ResponseBody
	public AjaxJson saveImportByConfig(@RequestParam("cacheCode") String cacheCode) {
		AjaxJson reJson = new AjaxJson();
		reJson.setSuccess(false);
		List<BtBankAccDtlVO> list = excelCacheService.getListExcelDataCache(cacheCode, BtBankAccDtlVO.class);
		btBankAccDtlService.saveImport(list, this.getCurrentUser());
		reJson=success();
		return reJson;
	}
	/**
	 * 获取付款账号
	 *
	 * @param bankType
	 * @return
	 */
	@ApiOperation("获取付款账号")
	@PostMapping("/getBankAcc")
	@ResponseBody
	public AjaxJson<List<BtBankAccDTO>> getBankAcc(String bankType) {
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
		return success(listBankAcc);
	}

	/**
	 * 刷新余额 flushBal
	 *
	 * @param bankAccDtlVO
	 * @return
	 */
	@ApiOperation("刷新余额")
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
