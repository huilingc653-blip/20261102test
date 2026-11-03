package com.bytter.biz.report.controller;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.biz.report.service.FbsBudgetDownloadReportService;
import com.bytter.biz.report.service.FbsBudgetProcessReportService;
import com.bytter.biz.report.vo.FbsBudgetProcessReportVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.List;
/**
 * 资金计划上报报表
 *
 * @author zuoy 2018-02-06
 */
@Controller
@RequestMapping("/budget/fbsBudgetProcessReport")
public class FbsBudgetProcessReportController extends BaseController {
	@Autowired
	private FbsBudgetProcessReportService fbsBudgetProcessReportService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private FbsBudgetDownloadReportService fbsBudgetDownloadReportService;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	/**
	 * 资金计划上报主表列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/query")
	public String query() {
		return "report/queryFbsBudgetProcess";
	}
	/**
	 * 查询
	 *
	 * @param model
	 * @param timeType  预算类型
	 * @param corpId    单位ID
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @param isInfo    查看页面
	 * @return
	 */
	@GetMapping("/seach")
	public String seach(HttpServletRequest request, Model model, String timeType, String corpId, Date beginDate, Date endDate, boolean isInfo) {
		FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate,timeType);
		Date seachBeginDate = fbsBudgetPeriodVO.getBeginDate();
		Date seachEndDate = fbsBudgetPeriodVO.getEndDate();
		model.addAttribute("timeType", timeType);
		model.addAttribute("corpId", corpId);
		// 单个周期
		boolean onePeriod = beginDate.compareTo(seachBeginDate) != -1 && endDate.compareTo(seachEndDate) != 1;
		if (onePeriod || isInfo) {
			CorpDTO corpDTO = this.corpService.get(corpId);
			model.addAttribute("corpName", corpDTO.getName());
			model.addAttribute("beginDate", seachBeginDate);
			model.addAttribute("endDate", seachEndDate);
			//获取计划
			FbsBudget fbsBudget = fbsBudgetService.getCorpBudget(corpId, timeType, seachBeginDate, seachEndDate);
			model.addAttribute("fbsBudget", fbsBudget);
			List<FbsBudgetProcessReportVO> reportList = this.fbsBudgetProcessReportService.listFbsBudgetProcessReport(timeType, corpId, seachBeginDate, seachEndDate);
			model.addAttribute("reportList", reportList);
			String curName = "";
			if(fbsBudget!=null){
				curName = btCurrencyService.getObjectById(fbsBudget.getCurId()).getCurName();
			}
			String title = corpDTO.getName() + "资金计划执行表" + " " + DateUtils.formatDate(seachBeginDate) + "~" + DateUtils.formatDate(seachEndDate) + " " + curName;
			String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "资金计划执行表.xls";
			this.fbsBudgetDownloadReportService.exporFbsBudgetProcess(request, reportList, filePath, title);
			return "report/reportFbsBudgetProcess";
		} else {
			model.addAttribute("beginDate", beginDate);
			model.addAttribute("endDate", endDate);
			return "report/listFbsBudgetProcessReport";
		}
	}
	/**
	 * 查询
	 * @param request
	 * @param model
	 * @param timeType
	 * @param corpId
	 * @param beginDate
	 * @return
	 */
	@PostMapping("/seachCollect")
	public String seachCollect(HttpServletRequest request,Model model, String timeType, String corpId, Date beginDate,String itemCode) {
		FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate,timeType);
		Date seachBeginDate = fbsBudgetPeriodVO.getBeginDate();
		Date seachEndDate = fbsBudgetPeriodVO.getEndDate();
		model.addAttribute("timeType", timeType);
		model.addAttribute("corpId", corpId);
		String corpName = "";
		String[] corpIdStr = corpId.split(",");
		String[] itemCodeStr =  itemCode.split(",");
		for(int i=0;i<corpIdStr.length;i++){
			CorpDTO corpDTO = this.corpService.get(corpIdStr[i]);
			if(i ==0){
				corpName = corpDTO.getName();
			}else{
				corpName += ","+corpDTO.getName();
			}
		}
		model.addAttribute("corpName", corpName);
		model.addAttribute("beginDate", seachBeginDate);
		model.addAttribute("endDate", seachEndDate);
		model.addAttribute("itemCode", itemCode);
		List<FbsBudgetProcessReportVO> reportList = this.fbsBudgetProcessReportService.listFbsBudgetProcessReport(timeType, corpIdStr, seachBeginDate, seachEndDate,itemCodeStr);
		model.addAttribute("reportList", reportList);
		String title = "资金计划执行单位汇总表" + " " + DateUtils.formatDate(seachBeginDate) + "~" + DateUtils.formatDate(seachEndDate) + " ";
		String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "资金计划执行单位汇总表.xls";
		this.fbsBudgetDownloadReportService.exporFbsBudgetProcess(request, reportList, filePath, title);
		return "report/reportFbsBudgetProcessCollect";
	}
	/**
	 * 查询中间页面
	 *
	 * @param model
	 * @param timeType  预算类型
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 */
	@GetMapping("/seachManyTemp")
	public String seachManyTemp(Model model, String timeType, Date beginDate, Date endDate) {
		model.addAttribute("timeType", timeType);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		return "report/reportFbsBudgetProcessTemp";
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return fbsBudgetProcessReportService.dataTable(dataTablesInput);
	}
	/**
	 * 跳转预算执行明细页面
	 *
	 * @param model
	 * @param itemCode      科目编码
	 * @param corpId        单位ID
	 * @param beginDate     开始日期
	 * @param endDate       结束日期
	 * @param statusFlag    状态	true-占用 false-在途
	 * @param innerSignFlag 内转标志	true-内转 false-非内转
	 * @return
	 */
	@GetMapping("/openProcessInfo")
	public String openProcessInfo(Model model, String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag, boolean innerSignFlag,String curId,String payeeAccName) {
		model.addAttribute("itemCode", itemCode);
		model.addAttribute("corpId", corpId);
		CorpDTO corpDTO = this.corpService.get(corpId);
		model.addAttribute("corpName", corpDTO.getName());
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("statusFlag", statusFlag);
		model.addAttribute("innerSign", innerSignFlag ? BudgetConstant.INNER_SIGN_1 : BudgetConstant.INNER_SIGN_0);
		model.addAttribute("curId",curId);
		model.addAttribute("payeeAccName",payeeAccName);
		return "report/reportFbsBudgetProcessInfo";
	}
	/**
	 * 跳转预算执行明细页面中间页面
	 * @param model
	 * @param itemCode      科目编码
	 * @param beginDate     开始日期
	 * @param endDate       结束日期
	 * @param statusFlag    状态	true-占用 false-在途
	 * @param innerSignFlag 内转标志	true-内转 false-非内转
	 * @return
	 */
	@GetMapping("/openProcessInfoTemp")
	public String openProcessInfoTemp(Model model, String itemCode, Date beginDate, Date endDate, boolean statusFlag, boolean innerSignFlag) {
		model.addAttribute("itemCode", itemCode);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("statusFlag", statusFlag);
		model.addAttribute("innerSign", innerSignFlag);
		return "report/reportFbsBudgetProcessInfoTemp";
	}
	/**
	 * 跳转预算执行明细页面
	 *
	 * @param model
	 * @param itemCode      科目编码
	 * @param corpId        单位ID
	 * @param beginDate     开始日期
	 * @param endDate       结束日期
	 * @param statusFlag    状态	true-占用 false-在途
	 * @param innerSignFlag 内转标志	true-内转 false-非内转
	 * @return
	 */
	@PostMapping("/openProcessInfoCollect")
	public String openProcessInfoCollect(Model model, String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag, boolean innerSignFlag) {
		model.addAttribute("itemCode", itemCode);
		model.addAttribute("corpId", corpId);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("statusFlag", statusFlag);
		model.addAttribute("innerSign", innerSignFlag ? BudgetConstant.INNER_SIGN_1 : BudgetConstant.INNER_SIGN_0);
		return "report/reportFbsBudgetProcessInfoCollect";
	}
	/**
	 * 显示预算执行明细数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@PostMapping("/dataTablesProcessInfo")
	@ResponseBody
	public DataTablesOutput dataTablesProcessInfo(@RequestBody DataTablesInput dataTablesInput) {
		return fbsBudgetProcessReportService.dataTablesProcessInfo(dataTablesInput);
	}
	/**
	 * 跳转预算追加明细页面
	 *
	 * @param model
	 * @param itemCode   科目编码
	 * @param corpId     单位ID
	 * @param beginDate  开始日期
	 * @param endDate    结束日期
	 * @param statusFlag 状态	true-占用 false-在途
	 * @return
	 */
	@GetMapping("/openSuperaddInfo")
	public String openSuperaddInfo(Model model, String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag, String timeType) {
		model.addAttribute("itemCode", itemCode);
		model.addAttribute("corpId", corpId);
		CorpDTO corpDTO = this.corpService.get(corpId);
		model.addAttribute("corpName", corpDTO.getName());
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("statusFlag", statusFlag);
		model.addAttribute("timeType", timeType);
		return "report/reportFbsBudgetSuperaddInfo";
	}
	/**
	 * 跳转预算追加明细页面
	 *
	 * @param model
	 * @param itemCode   科目编码
	 * @param beginDate  开始日期
	 * @param endDate    结束日期
	 * @param statusFlag 状态	true-占用 false-在途
	 * @return
	 */
	@GetMapping("/openSuperaddInfoTemp")
	public String openSuperaddInfoTemp(Model model, String itemCode, Date beginDate, Date endDate, boolean statusFlag, String timeType) {
		model.addAttribute("itemCode", itemCode);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("statusFlag", statusFlag);
		model.addAttribute("timeType", timeType);
		return "report/reportFbsBudgetSuperaddInfoTemp";
	}
	/**
	 * 跳转预算追加明细页面
	 *
	 * @param model
	 * @param itemCode   科目编码
	 * @param corpId     单位ID
	 * @param beginDate  开始日期
	 * @param endDate    结束日期
	 * @param statusFlag 状态	true-占用 false-在途
	 * @return
	 */
	@PostMapping("/openSuperaddInfoCollect")
	public String openSuperaddInfoCollect(Model model, String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag, String timeType) {
		model.addAttribute("itemCode", itemCode);
		model.addAttribute("corpId", corpId);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("statusFlag", statusFlag);
		model.addAttribute("timeType", timeType);
		return "report/reportFbsBudgetSuperaddInfoCollect";
	}
	/**
	 * 显示预算追加明细数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@PostMapping("/dataTablesSuperaddInfo")
	@ResponseBody
	public DataTablesOutput dataTablesSuperaddInfo(@RequestBody DataTablesInput dataTablesInput) {
		return fbsBudgetProcessReportService.dataTablesSuperaddInfo(dataTablesInput);
	}
	/**
	 * 导出预算模板
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/exportsBudgetProcess")
	public void exportsBudgetProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "资金计划执行表.xls";
		String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + fileName;
		this.fbsBudgetDownloadReportService.exports(request, response, fileName, filePath, BudgetConstant.BUDGET_EXPORTS_TYPE_BUDGETPROCESS);
	}
	/**
	 * 导出预算模板
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/exportsBudgetProcessCollect")
	public void exportsBudgetProcessCollect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "资金计划执行单位汇总表.xls";
		String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + fileName;
		this.fbsBudgetDownloadReportService.exports(request, response, fileName, filePath, BudgetConstant.BUDGET_EXPORTS_TYPE_BUDGETPROCESS);
	}
	/**
	 * 查询明细执行
	 *
	 * @return
	 */
	@GetMapping("/viewDtlProcess/{id}")
	public String viewDtlProcess(@PathVariable("id") String id, Model model) {
		model.addAttribute("id", id);
		return "report/reportFbsBudgetProcessDetail";
	}
	/**
	 * 显示预算执行明细数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@PostMapping("/dataTablesDetailProcessInfo")
	@ResponseBody
	public DataTablesOutput dataTablesDetailProcessInfo(@RequestBody DataTablesInput dataTablesInput) {
		return fbsBudgetProcessReportService.dataTablesDetailProcessInfo(dataTablesInput);
	}
}
