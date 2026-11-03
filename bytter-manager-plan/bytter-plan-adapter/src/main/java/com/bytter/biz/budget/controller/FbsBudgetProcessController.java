package com.bytter.biz.budget.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetProcess;
import com.bytter.biz.budget.service.*;
import com.bytter.biz.budget.spi.constant.FbsWorkFlowConstant;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.vo.*;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
//import org.apache.shiro.authz.annotation.Logical;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 资金计划执行表
 * @date 2017-07-18 10:48:05
 */
@Controller
@RequestMapping("/budget/fbsBudgetProcess")
@Slf4j
public class FbsBudgetProcessController extends BaseController {
	@Autowired
	private FbsBudgetProcessService fbsBudgetProcessService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@Autowired
    private IWorkFlowService workFlowServiceImpl;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@Autowired
	private FbsItemService fbsItemService;
	@Autowired
	private FbsProcessRuleService fbsProcessRuleService;
	/**
	 * 资金计划执行表列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY)
	@LogMessage(content = "查询资金计划执行表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "budget/listFbsBudgetProcess";
	}
	/**
	 * 资金计划执行查询列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESSQUERY_FBSBUDGETPROCESS_QUERY)
	@LogMessage(content = "查询资金计划执行表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listAll")
	public String listAll(Model model) {
		return "budget/listAllFbsBudgetProcess";
	}
	/**
	 * 资金计划执行表 table列表
	 *
	 * @return
	 */
	//@RequiresPermissions(value = {ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY,
//			ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_QUERY,
//			ResourceConstant.FBS_PROCESSQUERY_FBSBUDGETPROCESS_QUERY},
//			logical = Logical.OR)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput,String flag) {
		return fbsBudgetProcessService.getDataTablesUnion(dataTablesInput, getCurrentUser(),flag);
	}
	/**
	 * 资金计划执行表新增页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY)
	@GetMapping("/add")
	public String add(FbsBudgetProcess fbsBudgetProcess, Model model) {
		if (StringUtils.isNotEmpty(fbsBudgetProcess.getId())) {
			fbsBudgetProcess = fbsBudgetProcessService.get(fbsBudgetProcess.getId());
			model.addAttribute("fbsBudgetProcessPage", fbsBudgetProcess);
		}
		return "budget/addFbsBudgetProcess";
	}
	/**
	 * 资金计划执行表编辑页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY)
	@GetMapping("/update")
	public String update(FbsBudgetProcess fbsBudgetProcess, Model model) {
		if (StringUtils.isNotEmpty(fbsBudgetProcess.getId())) {
			FbsBudgetProcessVO fbsBudgetProcessVO = fbsBudgetProcessService.getVOById(fbsBudgetProcess.getId());
			model.addAttribute("corpCode", (StringUtils.isEmpty(fbsBudgetProcessVO.getCorpId()) || corpService.get(fbsBudgetProcessVO.getCorpId()) == null) ? "" : corpService.get(fbsBudgetProcessVO.getCorpId()).getCode());
			model.addAttribute("fbsBudgetProcessPage", fbsBudgetProcessVO);
		}
		return "budget/updateFbsBudgetProcess";
	}
	/**
	 * 新增页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/getItemDetail")
	public String getItemDetail(Model model, FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
		String result = this.initAttribute(model, fbsBudgetProjectDetailVO);
		if (!EmptyUtils.isBlank(result)) {
			model.addAttribute("msg", result);
			return "budget/callErrorFbsItemCommon";
		}
		return "budget/callFbsItemCommon";
	}
	/**
	 * 弹出框页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/getOpenItemDetail")
	public String getOpenItemDetail(HttpServletRequest request, Model model, FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
		String result = this.initAttribute(model, fbsBudgetProjectDetailVO);
		if (!EmptyUtils.isBlank(result)) {
			model.addAttribute("msg", result);
			return "budget/callErrorFbsItemCommon";
		}
		// 对应跳转行的行数
		String rowNum = request.getParameter("rowNum");
		// 已选择科目
		String budget = request.getParameter("budget");
		model.addAttribute("rowNum", rowNum);
		model.addAttribute("budget", budget);
		return "budget/callFbsOpenItemCommon";
	}
	/**
	 * 编辑页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/getEditItemDetail")
	public String getEditItemDetail(Model model, FbsBudgetProjectDetailVO vo) {
		String result = this.initAttribute(model, vo);
		if (!EmptyUtils.isBlank(result)) {
			model.addAttribute("msg", result);
			return "budget/callErrorFbsItemCommon";
		}
		model.addAttribute("listProcess", this.fbsBudgetService.listFbsBudgetProcessCommonVOInfo(vo, model));
		return "budget/callEditFbsItemCommon";
	}
	/**
	 * 查看页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/getViewItemDetail")
	public String getViewItemDetail(Model model, FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
		String result = this.initAttribute(model, fbsBudgetProjectDetailVO);
		if (!EmptyUtils.isBlank(result)) {
			model.addAttribute("msg", result);
			return "budget/callErrorFbsItemCommon";
		}
		model.addAttribute("listProcess", this.fbsBudgetService.listFbsBudgetProcessCommonVOInfo(fbsBudgetProjectDetailVO, model));
		return "budget/callViewFbsItemCommon";
	}
	/**
	 * 初始化组件参数
	 *
	 * @param model
	 * @param fbsBudgetProjectDetailVO
	 * @return
	 */
	private String initAttribute(Model model, FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
		if(StringUtils.isBlank(fbsBudgetProjectDetailVO.getIsMultiItem())){
			fbsBudgetProjectDetailVO.setIsMultiItem(CommonConstant.YES);
		}
		if (EmptyUtils.isBlank(fbsBudgetProjectDetailVO.getCorpId())) {
			//执行单位为空
			return BudgetConstant.MESSAGE_PROCESS_CORP;
		}
		String[] corpIds = fbsBudgetProjectDetailVO.getCorpId().split(CommonConstant.COMMA_SPLIT);
		boolean controlBudget = false;
		CorpDTO corpDTO = null;
		String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
		//根据执行日期获取计划周期
		FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(fbsBudgetProjectDetailVO.getProcessDate(), timeType);
		FbsBudget fbsBudget = null;
		for(int i=0;i<corpIds.length;i++){
			// 查询条件是否本位币
			CorpDTO corpDTOTemp = corpService.get(corpIds[i]);
			// 当单位ID为null或者当前单位不需要上报时，不加载计划科目页面
			if (!fbsCorpBudgetService.reportBudgetByCorpId(corpIds[i])) {
				if(corpIds.length == 1) {
					return "0";
				} else {
					//存在部分单位不需要做计划控制，无法进行批量挂接计划!
					return BudgetConstant.MESSAGE_PROCESS_CORP_REPORTBUDGET;
				}
			}
			// 单位是否控制
			boolean controlBudgetTemp = this.fbsCorpBudgetService.controlBudgetByCorpId(corpIds[i]);
			if(i==0){
				controlBudget = controlBudgetTemp;
				corpDTO = corpDTOTemp;
			} else {
				if(controlBudget != controlBudgetTemp){
					//单位控制方式不一致，无法进行批量挂接计划!
					return BudgetConstant.MESSAGE_PROCESS_CORP_CONTROLBUDGET;
				}
				if(!corpDTO.getCurId().equals(corpDTOTemp.getCurId())){
					//单位本位币不一致，无法进行批量挂接计划!
					return BudgetConstant.MESSAGE_PROCESS_CORP_CUR;
				}
			}
			//获取单位周期计划
			FbsBudget budget = fbsBudgetService.getCorpBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
			//当前执行周期的计划未编制或者未编制完成时判断计划执行规则
			if(EmptyUtils.isEmpty(budget) || (budget!=null && budget.getStatus()<BudgetConstant.STATUE_CHECK)){
				budget = new FbsBudget();
				budget.setCurId(corpDTO.getCurId());
				//针对本期计划执行
				if(DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getBeginDate()) >=0
						&& DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getEndDate())<=0) {
					//判断是否有启用的计划执行规则
					if (EmptyUtils.isNotEmpty(fbsProcessRuleService.listValidProcessRule())) {
						FbsBudgetVO fbsBudgetVO = fbsBudgetService.buildBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), false);
						if(fbsBudgetVO != null){
							BeanUtils.copyNotNullProperties(fbsBudgetVO, budget);
						}
					}
				}
			}
			if(budget == null){
				//存在部分单位没有可用的计划!
				return BudgetConstant.MESSAGE_PROCESS_BUDGET;
			}
			if(i==0){
				fbsBudget = budget;
			}
			if (ObjectUtil.notEqual(fbsBudget.getCurId(), budget.getCurId())) {
				//单位本位币不一致，无法进行批量挂接计划!
				return BudgetConstant.MESSAGE_PROCESS_BUDGET_CUR;
			}
		}
		fbsBudgetProjectDetailVO.setControlBudget(controlBudget);
		// 当单位ID为null或者当前单位不需要上报时，不加载计划科目页面
		/*if (!fbsCorpBudgetService.reportBudgetByCorpId(fbsBudgetProjectDetailVO.getCorpId())) {
			return "budget/callErrorFbsItemCommon";
		}*/
		if (EmptyUtils.isBlank(fbsBudgetProjectDetailVO.getItemCode())) {
			fbsBudgetProjectDetailVO.setItemCode(fbsItemService.getFbsItemForProcess(fbsBudgetProjectDetailVO.getSign()));
		}
		/*if (EmptyUtils.isBlank(fbsBudgetProjectDetailVO.getCorpName())) {
			fbsBudgetProjectDetailVO.setCorpName(corpService.get(fbsBudgetProjectDetailVO.getCorpId()).getName());
		}*/
		fbsBudgetProjectDetailVO.setFbsItemBalance(configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_ITEMBALANCE).getValue());
		fbsBudgetProjectDetailVO.setControlBudget(this.fbsCorpBudgetService.controlBudgetByCorpId(fbsBudgetProjectDetailVO.getCorpId()));
		if(StringUtils.isBlank(fbsBudgetProjectDetailVO.getCurId())){
			fbsBudgetProjectDetailVO.setCurId(corpDTO.getCurId());
			fbsBudgetProjectDetailVO.setCorpName(corpDTO.getName());
		}
		// 本位币查询全部
		if (!EmptyUtils.isBlank(fbsBudgetProjectDetailVO.getCurId()) && fbsBudgetProjectDetailVO.getCurId().equals(corpDTO.getCurId())) {
			model.addAttribute("isRMB", true);
		}
		//String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
		//根据执行日期获取计划周期
		//FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(fbsBudgetProjectDetailVO.getProcessDate(), timeType);
		//获取单位周期计划
		/*FbsBudget budget = fbsBudgetService.getCorpBudget(fbsBudgetProjectDetailVO.getCorpId(), timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
		//当前执行周期的计划未编制或者未编制完成时判断计划执行规则
		if(EmptyUtils.isEmpty(budget) || (budget!=null && budget.getStatus()<BudgetConstant.STATUE_CHECK)){
			budget = new FbsBudget();
			budget.setCurId(corpDTO.getCurId());
			//针对本期计划执行
			if(DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getBeginDate()) >=0
					&& DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getEndDate())<=0) {
				//判断是否有启用的计划执行规则
				if (EmptyUtils.isNotEmpty(fbsProcessRuleService.listValidProcessRule())) {
					FbsBudgetVO fbsBudgetVO = fbsBudgetService.buildBudget(fbsBudgetProjectDetailVO.getCorpId(), timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), false);
					if(fbsBudgetVO != null){
						BeanUtils.copyNotNullProperties(fbsBudgetVO, budget);
					}
				}
			}
		}*/
		model.addAttribute("fbsBudget", fbsBudget);
		if(fbsBudgetProjectDetailVO.getWishPayday() != null) {
			fbsBudgetProjectDetailVO.setWishPaydayStr(DateUtils.formatDate(fbsBudgetProjectDetailVO.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		}
		model.addAttribute("fbsBudgetProjectDetailVO", fbsBudgetProjectDetailVO);
		model.addAttribute("dtlColSel", configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOLSEL).getValue());
		return "";
	}
	/**
	 * 获取当前科目的 余额/上级余额/顶级余额
	 *
	 * @param fbsBudgetProjectDetailVO
	 * @return
	 */
	@LogMessage(content = "获取当前科目的 余额/上级余额/顶级余额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/genItemInfo")
	@ResponseBody
	public AjaxJson genItemInfo(FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
		AjaxJson json = new AjaxJson();
		String corpId = fbsBudgetProjectDetailVO.getCorpId();
		String itemCode = fbsBudgetProjectDetailVO.getItemCode();
		Date processDate = fbsBudgetProjectDetailVO.getProcessDate();
		String billNo = fbsBudgetProjectDetailVO.getBillNo();
		String billId = fbsBudgetProjectDetailVO.getBillId();
		String curId = fbsBudgetProjectDetailVO.getCurId();
		if (StringUtils.isNotEmpty(corpId) && StringUtils.isNotEmpty(itemCode) && StringUtils.isNotEmpty(processDate)) {
			FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO = fbsBudgetService.getFbsBudgetProcessByItemCode(corpId, itemCode, processDate, billNo, billId, curId);
			json.setObj(fbsBudgetProcessCommonVO);
			json.setSuccess(true);
		}
		return json;
	}
	/**
	 * 手机查看页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/getViewItemDetailMobile")
	public String getViewItemDetailMobile(HttpServletRequest request, Model model, FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
		String billNo = request.getParameter("billNo");
		String billId = request.getParameter("billId");
		String businessName = request.getParameter("businessName");
		String processSysname = request.getParameter("processSysname");
		String corpId = request.getParameter("corpId");
		String processDate = request.getParameter("processDate");
		String curId = request.getParameter("curId");
		try {
			List<FbsBudgetProcess> list = null;
			if (!StringUtils.isBlank(billId)) {
				list = this.fbsBudgetProcessService.getBudgetProcessByBillId(businessName, processSysname, billId);
			} else {
				list = this.fbsBudgetProcessService.getBudgetProcessByBillNo(businessName, processSysname, billNo);
			}
			List<FbsBudgetProcessCommonVO> voList = null;
			if (!EmptyUtils.isEmpty(list)) {
				voList = new ArrayList<>();
				for (FbsBudgetProcess fbsBudgetProcess : list) {
					FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO = fbsBudgetService.getFbsBudgetProcessByItemCode(corpId, fbsBudgetProcess.getItemCode(), DateUtils.parseDate(processDate, "yyyy-MM-dd"), null, null, curId);
					fbsBudgetProcessCommonVO.setItemName(fbsBudgetProcess.getItemName());
					fbsBudgetProcessCommonVO.setProcessMoney(fbsBudgetProcess.getProcessMoney());
					voList.add(fbsBudgetProcessCommonVO);
				}
			}
			model.addAttribute("voList", voList);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "budget/callViewMobileFbsItemCommon";
	}
	/**
	 * 资金计划执行表详情页
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESSQUERY_FBSBUDGETPROCESS_QUERY)
	@LogMessage(content = "查询资金计划执行表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FbsBudgetProcessVO fbsBudgetProcessVO = fbsBudgetProcessService.getVOById(id);
			model.addAttribute("corpCode", (StringUtils.isEmpty(fbsBudgetProcessVO.getCorpId()) || corpService.get(fbsBudgetProcessVO.getCorpId()) == null) ? "" : corpService.get(fbsBudgetProcessVO.getCorpId()).getCode());
			model.addAttribute("fbsBudgetProcessPage", fbsBudgetProcessVO);
		}
		return "budget/viewFbsBudgetProcess";
	}
	/**
	 * 添加资金计划执行表
	 *
	 * @param list
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY)
	@LogMessage(content = "添加资金计划执行表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save/{opType}")
	@ResponseBody
	public AjaxJson save(@RequestBody List<FbsBudgetProcess> list, @PathVariable("opType") String opType) {
		fbsBudgetProcessService.save(list, opType, this.getCurrentUser());
		return success();
	}
	/**
	 * 修改资金计划执行表
	 *
	 * @param fbsBudgetProcess
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY)
	@LogMessage(content = "修改资金计划执行表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FbsBudgetProcess fbsBudgetProcess) {
		fbsBudgetProcessService.modify(fbsBudgetProcess, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除资金计划执行表
	 *
	 * @param id
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY)
	@LogMessage(content = "删除资金计划执行表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		FbsBudgetProcess fbsBudgetProcess = fbsBudgetProcessService.get(id);
		fbsBudgetProcessService.deleteEntity(fbsBudgetProcess);
		return success();
	}
	/**
	 * 批量删除资金计划执行表
	 *
	 * @param ids
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY)
	@LogMessage(content = "批量资金计划释放", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(@RequestParam("ids") String ids) {
		if (StringUtils.isNotEmpty(ids)) {
			for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
				FbsBudgetProcess fbsBudgetProcess = fbsBudgetProcessService.get(id);
				fbsBudgetProcessService.deleteEntity(fbsBudgetProcess);
			}
		}
		return success();
	}
	/**
	 * 批量资金计划释放资金计划执行表
	 *
	 * @param ids
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY)
	@LogMessage(content = "批量资金计划释放", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchRelease")
	@ResponseBody
	public AjaxJson batchRelease(@RequestParam("ids") String ids) {
		if (StringUtils.isNotEmpty(ids)) {
			fbsBudgetProcessService.batchReleaseByIds(ids, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 批量提交资金计划释放资金计划执行表
	 *
	 * @param ids
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_APPLY)
	@LogMessage(content = "批量提交资金计划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
		if (StringUtils.isNotEmpty(ids)) {
			fbsBudgetProcessService.batchSubmit(ids, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 跳转至审核列表
	 *
	 * @return
	 */
	@LogMessage(content = "跳转至审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus, String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "budget/listFbsBudgetProcessApprove";
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询资金计划执行审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(FbsWorkFlowConstant.FBS_BUDGET_PROCESS, dataTablesInput);
		DataTablesOutput<FbsBudgetProcessVO> dp = fbsBudgetProcessService.dataTableApprove(dataTablesInput, workFlowBusiness.getBusinessIdList());
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @param model
	 * @return
	 */
	@LogMessage(content = "查看审核详情界面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/approve")
	public String approve(String id, String taskId, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FbsBudgetProcessVO fbsBudgetProcessVO = fbsBudgetProcessService.getVOById(id);
			model.addAttribute("corpCode", (StringUtils.isEmpty(fbsBudgetProcessVO.getCorpId()) || corpService.get(fbsBudgetProcessVO.getCorpId()) == null) ? "" : corpService.get(fbsBudgetProcessVO.getCorpId()).getCode());
			model.addAttribute("fbsBudgetProcessPage", fbsBudgetProcessVO);
			model.addAttribute("taskId", taskId);
		}
		return "budget/approveFbsBudgetProcess";
	}
	/**
	 * 获取已保存的资金计划执行数据
	 * zuoy	2017-08-29
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @param billId         票据ID
	 * @return
	 */
	@PostMapping("/getBudgetProcesInfo")
	@ResponseBody
	public List<FbsBudgetProcess> getBudgetProcesInfo(String businessName, String processSysname, String billNo, String billId, String id) {
		return this.fbsBudgetProcessService.getBudgetProcesInfo(businessName, processSysname, billNo, billId, id);
	}
	@LogMessage(content = "查询是否是计划汇总单位", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/isCorpForCollectFlag/{id}")
	@ResponseBody
	public AjaxJson isCorpForCollectFlag(@PathVariable("id") String id) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		Map<String, Object> map = new HashMap<>();
		//计划汇总 0-否, 1-是
		if (StringUtils.isNotEmpty(id)) {
			FbsCorpBudgetDTO corpBudgetDTO = this.fbsCorpBudgetService.getByCorpId(id);
			if (!EmptyUtils.isEmpty(corpBudgetDTO)) {
				map.put("collectFlag", corpBudgetDTO.getCollectFlag());
				if (corpBudgetDTO.getCollectFlag() == 0) {
					Integer result = 1;
					//获取管理的单位
					List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(corpBudgetDTO.getCorpId());
					if (!EmptyUtils.isEmptyList(list)) {
						for (FbsCorpBudgetDTO dto : list) {
							if (dto.getReportBudget() != 0 && dto.getCollectFlag() == 1) {
								result = 0;
							}
						}
					}
					map.put("collectFlag", result);
				}
				j.setAttributes(map);
			}
		}
		return j;
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
			FbsBudgetProcessVO fbsBudgetProcessVO = fbsBudgetProcessService.getVOById(businessId);
			model.addAttribute("corpCode", (StringUtils.isEmpty(fbsBudgetProcessVO.getCorpId()) || corpService.get(fbsBudgetProcessVO.getCorpId()) == null) ? "" : corpService.get(fbsBudgetProcessVO.getCorpId()).getCode());
			model.addAttribute("fbsBudgetProcessPage", fbsBudgetProcessVO);
			model.addAttribute("taskId", taskId);
		}
		return "budget/viewApproveFbsBudgetProcess";
	}
	/**
	 * 根据明细获取计划执行金额
	 *
	 * @param fbsBudgetProjectDetailVO
	 * @return
	 */
	@LogMessage(content = "获取当前科目的明细执行金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/genItemInfoByDetail")
	@ResponseBody
	public AjaxJson genItemInfoByDetail(FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
		AjaxJson json = new AjaxJson();
		String corpId = fbsBudgetProjectDetailVO.getCorpId();
		String itemCode = fbsBudgetProjectDetailVO.getItemCode();
		Date processDate = fbsBudgetProjectDetailVO.getProcessDate();
		String billNo = fbsBudgetProjectDetailVO.getBillNo();
		String billId = fbsBudgetProjectDetailVO.getBillId();
		if (StringUtils.isNotEmpty(corpId) && StringUtils.isNotEmpty(itemCode) && StringUtils.isNotEmpty(processDate)) {
			BigDecimal processMoney = fbsBudgetProcessService.getBudgetProcessMoneyByDetail(corpId, itemCode, processDate, billNo, billId, fbsBudgetProjectDetailVO);
			json.setObj(processMoney);
			json.setSuccess(true);
		}
		return json;
	}
	/**
	 * 判断单位是否上报计划
	 * @param corpId
	 * @return
	 */
	@RequestMapping(params = "getReportBudgetByCorpId")
	@ResponseBody
	public AjaxJson getReportBudgetByCorpId(String corpId){
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		j.setObj(fbsCorpBudgetService.reportBudgetByCorpId(corpId));
		return j;
	}
	/**
	 * 保存导入数据
	 *
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<FbsBudgetProcessVO> list) {
		this.fbsBudgetProcessService.saveImport(list, this.getCurrentUser());
		return success();
	}
	
	/**
	 * 根据单位ID获取同网点下成员单位(不跨网点)
	 *
	 * @param corpId
	 * @return
	 */
	@GetMapping("/json")
	@ResponseBody
	public List<CorpDTO> getUnderCorpByNetId(String corpId) {
		List<CorpDTO> corpList = new ArrayList<>();
		if (StringUtils.isNotBlank(corpId)) {
			CorpDTO corp = corpService.getNetByCorpId(corpId);//根据单位ID，获取该单位的直属网点
			corpList = corpService.listUnderCorpByNetId(corp.getId());//根据网点ID，获取该网点下直属的单位(不跨网点获取)
		}
		return corpList;
	}
}
