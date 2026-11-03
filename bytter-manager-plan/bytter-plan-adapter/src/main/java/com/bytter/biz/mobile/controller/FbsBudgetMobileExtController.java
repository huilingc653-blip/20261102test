package com.bytter.biz.mobile.controller;

import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.vo.FbsBudgetDtlAbsVO;
import com.bytter.biz.budget.vo.FbsBudgetProcessVO;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import com.bytter.biz.report.service.BudgetUtilService;
import com.bytter.biz.report.service.FbsBudgetProcessReportService;
import com.bytter.biz.report.vo.FbsBudgetProcessReportVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 资金计划上报主表
 * @date 2017-07-14 14:51:48
 */
@Controller
@RequestMapping("/mobile/budget/fbsbudget")
public class FbsBudgetMobileExtController extends BaseController {

	@Autowired
	private FbsBudgetService fbsBudgetService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private BudgetUtilService budgetUtilService;
	@Autowired
	private FbsBudgetProcessReportService fbsBudgetProcessReportService;

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
			FbsBudgetVO fbsBudget = fbsBudgetService.getBudget(businessId);
			model.addAttribute("fbsBudgetPage", fbsBudget);
			model.addAttribute("dltList", fbsBudget.getDtlList());
			model.addAttribute("taskId", taskId);
		}
		return "mobile/budget/viewApproveFbsBudgetInput";
	}

	/**
	 * 资金计划报表主页
	 * @return
	 */
	@PostMapping("/mobileReq")
	public String mobileReq() {
		return "mobile/budget/mobileReq";
	}

	/**
	 * 资金计划上报表
	 * @return
	 */
	@RequestMapping("/mobileReqInput")
	public String mobileReqInput(Model model) {
		//获取业务类型
		List<Map<String, Object>>  timeTypeList = budgetUtilService.getTimeType();
		model.addAttribute("timeTypeList",timeTypeList);
		return "mobile/budget/mobileReqInput";
	}

	/**
	 * 资金计划执行表
	 * @return
	 */
	@RequestMapping("/mobileReqProess")
	public String mobileReqProess(Model model) {
		//获取业务类型
		List<Map<String, Object>>  timeTypeList = budgetUtilService.getTimeType();
		model.addAttribute("timeTypeList",timeTypeList);
		return "mobile/budget/mobileReqProess";
	}

	/**
	 * 资金计划列表
	 */
	@PostMapping("/getInputMoblieList")
	@ResponseBody
	public Object getInputMoblieList(String timeType,String corpId,String startDateStr,String endDateStr,String resource){
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, this.getUserId()), "fbs.corp_Id", true);
		return this.fbsBudgetService.getBudgetInputMoblieList(corpId,timeType,startDateStr,endDateStr,corpAuthSql);
	}

	/**
	 * 资金计划明细
	 */
	@RequestMapping("/mobileFbsBudgetInfo")
	public String  mobileFbsBudgetInfo(String id,Model model){
		FbsBudgetVO fbsBudgetVO = fbsBudgetService.getBudget(id);
		List<FbsBudgetDtlAbsVO> list = fbsBudgetVO.getDtlList();
		model.addAttribute("fbsBudgetVO",fbsBudgetVO);
		return "mobile/budget/mobileReqInputInfo";
	}

	/**
	 * 资金计划执行列表
	 */
	@RequestMapping("/mobileFbsBudgetProessList")
	public String  mobileFbsBudgetProessList(String id,Model model){
		FbsBudgetVO fbsBudgetVO = fbsBudgetService.getBudget(id);
		List<FbsBudgetProcessReportVO> reportList = this.fbsBudgetProcessReportService.listFbsBudgetProcessReport(fbsBudgetVO.getTimeType(), fbsBudgetVO.getCorpId(), fbsBudgetVO.getBeginDate(), fbsBudgetVO.getEndDate());
		model.addAttribute("reportList", reportList);
		model.addAttribute("fbsBudgetVO",fbsBudgetVO);
		return "mobile/budget/mobileFbsBudgetProessList";
	}

	@RequestMapping("/mobileFbsBudgetProessDetailList")
	public String  mobileFbsBudgetProessDetailList(Model model, String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag){
		CorpDTO corpDTO = this.corpService.get(corpId);
		List<FbsBudgetProcessVO> fbsBudgetProcessVOList = this.fbsBudgetProcessReportService.mobileFbsBudgetProessDetailList(itemCode,corpId,beginDate,endDate,statusFlag);
		model.addAttribute("corpName", corpDTO.getName());
		model.addAttribute("fbsBudgetProcessVOList", fbsBudgetProcessVOList);
		return "mobile/budget/mobileFbsBudgetProessDetailList";
	}

}
