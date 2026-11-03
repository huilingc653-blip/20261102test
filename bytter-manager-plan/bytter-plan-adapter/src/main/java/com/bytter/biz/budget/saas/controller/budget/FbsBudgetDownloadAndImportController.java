package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.service.FbsBudgetDownloadAndImportService;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.excel.dto.ExcelImportDTO;
import com.bytter.core.utils.JsonUtils;
import com.bytter.poi.service.ExcelCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = "资金计划导入导出控制器", value = "/v1-plan/budget/fbsBudgetDownloadAndImport")
@Controller("v1FbsBudgetDownloadAndImportController")
@RequestMapping("/v1-plan/budget/fbsBudgetDownloadAndImport")
public class FbsBudgetDownloadAndImportController extends BaseController {
	@Autowired
	private FbsBudgetDownloadAndImportService fbsBudgetDownloadAndImportService;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@Autowired
	private ExcelCacheService excelCacheService;

	@ApiOperation(value = "下载预算模板")
	@GetMapping("/downloadBudget")
	public void downloadBudget(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudget(request, response, id, timeType, target);
	}

	@ApiOperation(value = "下载预算分解模板")
	@GetMapping("/downloadBudgetResolve")
	public void downloadBudgetResolve(HttpServletRequest request, HttpServletResponse response, String id, String timeType, Date beginDate, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudgetResolve(request, response, id, timeType, beginDate, target);
	}

	@ApiOperation(value = "下载预算月滚动模板")
	@GetMapping("/downloadBudgetRollMonth")
	public void downloadBudgetRollMonth(HttpServletRequest request, HttpServletResponse response, String id, String timeType, Date beginDate, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudgetRollMonth(request, response, id, timeType, beginDate, target);
	}

	@ApiOperation(value = "导出预算模板")
	@GetMapping("/exportBudget")
	public void exportBudget(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudget(request, response, id, timeType, target);
	}

	@ApiOperation(value = "导出预算汇总模板")
	@GetMapping("/exportBudgetCollect")
	public void exportBudgetCollect(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
		String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
		int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
		if("input".equals(target)){
			if (BudgetConstant.TIME_TYPE_YEAR.equals(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
				this.fbsBudgetDownloadAndImportService.exportBudgetResolveInput(request, response, id, timeType, target);
			} else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
				if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)&&mRollSubperiod==0) {
					this.fbsBudgetDownloadAndImportService.exportBudgetRollMonth(request, response, id, timeType, target, this.getCurrentUser());
				} else if(BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)&&mRollSubperiod>0){
					this.fbsBudgetDownloadAndImportService.exportBudgetRollMonthWeek(request, response, id, timeType, target, this.getCurrentUser());
				} else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
					this.fbsBudgetDownloadAndImportService.exportBudgetResolveInput(request, response, id, timeType, target);
				} else {
					this.fbsBudgetDownloadAndImportService.exportBudgetInput(request, response, id, timeType, target);
				}
			}else if(BudgetConstant.TIME_TYPE_QUARTER.equals(timeType)&&BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)){
				this.fbsBudgetDownloadAndImportService.exportBudgetRollQuarter(request,response,id,timeType,target,this.getCurrentUser());
			}else if(BudgetConstant.TIME_TYPE_WEEK.equals(timeType)&&BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)){
				this.fbsBudgetDownloadAndImportService.exportBudgetRollWeek(request,response,id,timeType,target,this.getCurrentUser());
			} else{
				this.fbsBudgetDownloadAndImportService.exportBudgetInput(request, response, id, timeType, target);
			}
		}else {
			if (BudgetConstant.TIME_TYPE_YEAR.equals(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
				this.fbsBudgetDownloadAndImportService.exportBudgetResolve(request, response, id, timeType, target);
			} else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
				if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode) || BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
					this.fbsBudgetDownloadAndImportService.exportBudgetRollMonth(request, response, id, timeType, target, this.getCurrentUser());
				} else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
					this.fbsBudgetDownloadAndImportService.exportBudgetResolve(request, response, id, timeType, target);
				} else {
					this.fbsBudgetDownloadAndImportService.exportBudget(request, response, id, timeType, target);
				}
			} else {
				this.fbsBudgetDownloadAndImportService.exportBudget(request, response, id, timeType, target);
			}
		}
	}

	@ApiOperation(value = "导出预算分解模板")
	@GetMapping("/exportBudgetResolve")
	public void exportBudgetResolve(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetResolve(request, response, id, timeType, target);
	}

	@ApiOperation(value = "导出月计划上报+月滚动模板")
	@GetMapping("/exportBudgetRollMonth")
	public void exportBudgetRollMonth(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetRollMonth(request, response, id, timeType, target, this.getCurrentUser());
	}

	@ApiOperation(value = "导出实时查看")
	@GetMapping("/exportBudgetCur")
	public void exportBudgetCur(HttpServletRequest request, HttpServletResponse response, String corpId, String timeType, String beginDate,
								String endDate, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetCur(request, response, corpId, timeType, beginDate, endDate, target);
	}

	@ApiOperation(value = "下载计划明细录入模式模板")
	@GetMapping("/downloadFbsBudgetDetail")
	public void downloadFbsBudgetDetail(HttpServletRequest request, HttpServletResponse response, String target, String itemCode, String itemName) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadFbsBudgetDetail(request, response, target, itemCode, itemName);
	}

	@ApiOperation(value = "下载上报界面预算明细模板")
	@GetMapping("/downloadFbsBudgetDetailByInput")
	public void downloadFbsBudgetDetailByInput(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadFbsBudgetDetailByInput(request, response, id, timeType, target);
	}

	@ApiOperation(value = "导出计划明细录入模式模板")
	@GetMapping("/exportBudgetDetail")
	public void exportBudgetDetail(HttpServletRequest request, HttpServletResponse response, String target, String budgetId, String detailId) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetDetail(request, response, target, budgetId, detailId);
	}

	@ApiOperation(value = "导出实际数据")
	@GetMapping("/exportFact")
	public void exportFact(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetFact(request, response, id);
	}

	@ApiOperation(value = "导出销售数据")
	@GetMapping("/exportSales")
	public void exportSales(HttpServletRequest request, HttpServletResponse response, String corpId, String queryDate) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetSales(request, response, corpId, queryDate);
	}

	@ApiOperation(value = "下载月滚动+双月周计划上报模板")
	@GetMapping("/downloadBudgetRollMonthWeek")
	public void downloadBudgetRollMonthWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudgetRollMonthWeek(request, response, id, timeType, target, this.getCurrentUser());
	}

	@ApiOperation(value = "导出月滚动+双月周计划上报")
	@GetMapping("/exportBudgetRollMonthWeek")
	public void exportBudgetRollMonthWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetRollMonthWeek(request, response, id, timeType, target, this.getCurrentUser());
	}

	@ApiOperation(value = "下载季滚动计划上报模板")
	@GetMapping("/downloadBudgetRollQuarter")
	public void downloadBudgetRollQuarter(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudgetRollQuarter(request, response, id, timeType, target, this.getCurrentUser());
	}

	@ApiOperation(value = "上传滚动计划")
	@PostMapping("/uploadRoll")
	@ResponseBody
	public AjaxJson<ExcelImportDTO> uploadRoll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ExcelImportDTO dto = (ExcelImportDTO) fbsBudgetDownloadAndImportService.uploadRoll(request,response,this.getCurrentUser());
		dto.setAllTotal(null == dto.getList() ? 0 : Long.parseLong(String.valueOf(dto.getList().size())));
		setExcelCache(dto, "50000001", false);
		return AjaxJson.success(dto);
	}

	@ApiOperation(value = "导出季滚动计划上报")
	@GetMapping("/exportBudgetRollQuarter")
	public void exportBudgetRollQuarter(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetRollQuarter(request, response, id, timeType, target, this.getCurrentUser());
	}

	@ApiOperation(value = "下载周滚动计划上报模板")
	@GetMapping("/downloadBudgetRollWeek")
	public void downloadBudgetRollWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudgetRollWeek(request, response, id, timeType, target, this.getCurrentUser());
	}

	@ApiOperation(value = "导出周滚动计划上报")
	@GetMapping("/exportBudgetRollWeek")
	public void exportBudgetRollWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetRollWeek(request, response, id, timeType, target, this.getCurrentUser());
	}

	public void setExcelCache(ExcelImportDTO dto, String code, boolean isPage) {
		String key = getKey(code);
		String ekey = key + "_error";
		excelCacheService.clearExcelDataCache(key);
		excelCacheService.clearExcelDataCache(ekey);
		List successList = new ArrayList();
		if(null != dto.getList()){
			successList.addAll(dto.getList());
		}
		List errorList = new ArrayList();
		if(null != dto.getErrorList()) {
			errorList.addAll(dto.getErrorList());
		}
		excelCacheService.putExcelDataCache(key, successList);
		excelCacheService.putExcelDataCache(ekey, errorList);
		List list = new ArrayList();
		list.addAll(successList);
		list.addAll(errorList);
        dto.setSuccessList(successList);
		dto.setList(list);
		dto.setCode(key);
		if(isPage) {
			dto.setKey(key);
			dto.setAllTotal(Long.parseLong(String.valueOf(successList.size())));
			dto.setTotal(Long.parseLong(String.valueOf(errorList.size())));
		}
	}

	public String getKey(String code) {
		return code + this.getUserId();
	}
}
