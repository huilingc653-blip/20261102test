package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.inneraccount.service.BtBankAccAppService;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccApproveViewVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccViewVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号申请表
 * @date 2017-05-23 17:37:39
 */
@Api(value = "/v1-payment/bankacc/btbankaccapp",tags = "银行账号开户/信息表")
@RestController("v1BtBankAccAppController")
@RequestMapping("/v1-payment/bankacc/btbankaccapp")
public class BtBankAccAppController extends BaseController {
	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;

	@Autowired
	private BtBankAccAppService bankAccAppService;
	@DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;//变接口
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IBankTypeService bankTypeService;

	/**
	 * 银行账号申请表 table列表
	 *
	 * @return
	 */

	@ApiOperation("查询银行账号开户申请列表")
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<BtBankAccAppDTO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(bankAccAppService.getDataTablesUnion(input, this.getCurrentUser()));
	}
	@ApiOperation("根据ID获取用户账户信息")
	@GetMapping("/getById")
	@ResponseBody
	public AjaxJson<BtBankAccAppDTO> getById(String id){
		return success(btBankAccAppService.getById(id));
	}
	/**
	 * 银行账号申请表编辑页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@ApiOperation("获取更新页面的银行账户信息")
	@GetMapping("/getInfoBeforeUpdate")
	public AjaxJson<BtBankAccViewVO> getInfoBeforeUpdate(String id) {
		BtBankAccViewVO btBankAccViewVO = new BtBankAccViewVO();
		BtBankAccAppDTO btBankAccApp = btBankAccAppService.getBankAccApp(id);
		btBankAccViewVO.setCurId(btBankAccApp.getCurId());
		btBankAccViewVO.setBtBankAccAppPage(btBankAccApp);
		String bankAccDesc = configService.getConfig(ConfigConstant.BANKACC, "bankAccDesc").getValue();
		btBankAccViewVO.setBankAccDesc(bankAccDesc);
		btBankAccViewVO.setIsDomesticBankType(btBankAccApp.getIsDomesticBank()==null ? "":btBankAccApp.getIsDomesticBank());
		return success(btBankAccViewVO);
	}
	/**
	 * 银行账号申请表详情页
	 *
	 * @return
	 */
	@ApiOperation(value = "查询银行账号申请详情",notes = "" +
			"跳转页面 由btBankAccApp.busType来控制\n" +
			" 1. busType=2 跳转bankacc/viewBtBankAccRev\n" +
			" 2. busType=3 跳转bankacc/viewBtBankAccDel\n" +
			" 其他情况跳转bankacc/viewBtBankAccApp")
	@LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	@ResponseBody
	public AjaxJson<BtBankAccViewVO> view(@PathVariable("id") String id) {
		BtBankAccViewVO btBankAccViewVO = new BtBankAccViewVO();
		BtBankAccAppDTO btBankAccApp = btBankAccAppService.getBankAccApp(id);
		if (StringUtil.isNotEmpty(btBankAccApp.getEntityAccId())) {
			BtBankAccDTO btBankAcc = btBankAccService.getBtBankAcc(btBankAccApp.getEntityAccId());
			btBankAccViewVO.setBtBankAcc(btBankAcc);
		}
		btBankAccViewVO.setBtBankAccAppPage(btBankAccApp);
		btBankAccViewVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_APP);
		btBankAccViewVO.setIsDomesticBankType(btBankAccApp.getIsDomesticBank() ==null ? "":btBankAccApp.getIsDomesticBank());
		return success(btBankAccViewVO);
	}
	/**
	 * 添加银行账号申请表
	 *
	 * @param btBankAccApp
	 * @return
	 */
	@ApiOperation("添加银行账号")
	@LogMessage(content = "添加银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestBody BtBankAccAppDTO btBankAccApp) {
		if (BtBankAccConstant.ACCTYPE_02.equals(btBankAccApp.getAccType())) {
			btBankAccApp.setRegularDate(BudgetConstant.getRegularsDate(btBankAccApp.getRegulars()));
		}
		bankAccAppService.toSave(btBankAccApp, this.getCurrentUser());
		return success();
	}
	/**
	 * 修改银行账号申请表
	 *
	 * @param btBankAccApp
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@ApiOperation("修改银行账号申请表")
	@LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(@RequestBody BtBankAccAppDTO btBankAccApp) {
		if (BtBankAccConstant.ACCTYPE_02.equals(btBankAccApp.getAccType())) {
			btBankAccApp.setRegularDate(BudgetConstant.getRegularsDate(btBankAccApp.getRegulars()));
		}
		bankAccAppService.toModify(btBankAccApp, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除银行账号申请表
	 *
	 * @param id
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@ApiOperation("删除银行账号申请")
	@LogMessage(content = "删除银行账号申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		BtBankAccAppDTO btBankAccApp = btBankAccAppService.getById(id);
		bankAccAppService.toDelte(btBankAccApp, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除银行账号申请表
	 *
	 * @param ids
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_APPLY)
	@ApiOperation("批量删除银行账号申请表")
	@LogMessage(content = "批量删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
		bankAccAppService.toAllDelte(ids, this.getCurrentUser());
		return success();
	}


	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation("审核列表数据 工作流 中的待办流程 银行账户开户")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public AjaxJson<DataTablesOutput<WorkFlowPersistence>> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(this.bankAccAppService.dataTableApprove(input));
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @return String
	 */
	@ApiOperation("跳转至审核详情界面")
	@GetMapping("/approve")
	public AjaxJson<BtBankAccApproveViewVO> approve(String id, String taskId) {
		BtBankAccApproveViewVO btBankAccApproveViewVO = new BtBankAccApproveViewVO();
		BtBankAccAppDTO btBankAccApp = btBankAccAppService.getBankAccApp(id);
		BtBankAccDTO acc = btBankAccCommonService.getBtBankAccVOById(btBankAccApp.getEntityAccId());
		btBankAccApproveViewVO.setBtBankAccAppPage(btBankAccApp);
		btBankAccApproveViewVO.setBtBankAcc(acc);
		btBankAccApproveViewVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_APP);
		btBankAccApproveViewVO.setTaskId(taskId);
		return success(btBankAccApproveViewVO);
	}
	/**
	 * 跳转至手机审核详情界面
	 *
	 * @param businessId
	 * @param taskId
	 * @return String
	 */
	@ApiOperation("跳转至手机审核详情界面")
	@GetMapping("/mobileApprove")
	public AjaxJson<BtBankAccApproveViewVO> mobileApprove(String businessId, String taskId) {
		BtBankAccApproveViewVO btBankAccApproveViewVO = new BtBankAccApproveViewVO();
		BtBankAccAppDTO btBankAccApp = btBankAccAppService.getBankAccApp(businessId);
		BtBankAccDTO acc = btBankAccCommonService.getBtBankAccVOById(btBankAccApp.getEntityAccId());
		btBankAccApproveViewVO.setBtBankAccAppPage(btBankAccApp);
		btBankAccApproveViewVO.setBtBankAcc(acc);
		btBankAccApproveViewVO.setTaskId(taskId);
		return success(btBankAccApproveViewVO);
	}
}
