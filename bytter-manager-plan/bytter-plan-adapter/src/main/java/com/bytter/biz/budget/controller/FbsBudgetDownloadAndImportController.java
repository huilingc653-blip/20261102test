package com.bytter.biz.budget.controller;
import com.bytter.biz.budget.service.FbsBudgetDownloadAndImportService;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.excel.dto.ExcelImportDTO;
import com.bytter.core.utils.JsonUtils;
import com.bytter.poi.service.ExcelCacheService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 资金计划上报主表
 * @date 2017-07-14 14:51:48
 */
@Controller
@RequestMapping("/budget/fbsBudgetDownloadAndImport")
public class FbsBudgetDownloadAndImportController extends BaseController {
	@Autowired
	private FbsBudgetDownloadAndImportService fbsBudgetDownloadAndImportService;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@Autowired
	private ExcelCacheService excelCacheService;
	/**
	 * 下载预算模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/downloadBudget")
	public void downloadBudget(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudget(request, response, id, timeType, target);
	}
	/**
	 * 下载预算分解模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/downloadBudgetResolve")
	public void downloadBudgetResolve(HttpServletRequest request, HttpServletResponse response, String id, String timeType, Date beginDate, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudgetResolve(request, response, id, timeType, beginDate, target);
	}
	/**
	 * 下载预算月滚动模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/downloadBudgetRollMonth")
	public void downloadBudgetRollMonth(HttpServletRequest request, HttpServletResponse response, String id, String timeType, Date beginDate, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudgetRollMonth(request, response, id, timeType, beginDate, target);
	}
	/**
	 * 导出预算模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/exportBudget")
	public void exportBudget(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudget(request, response, id, timeType, target);
	}
	/**
	 * 导出预算模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/exportBudgetCollect")
	public void exportBudgetCollect(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		//年计划上报模式:0,年固定;1,年分解
		String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
		//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		//季计划上报模式:1,季固定;2,季滚动;
		String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
		//周计划上报模式:1,固定;2,周滚动;
		String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
		//mRollSubperiod-月滚动模式 滚动分解子周期
		int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
		if("input".equals(target)){
			if (BudgetConstant.TIME_TYPE_YEAR.equals(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
				//年计划模式控制+年份解
				this.fbsBudgetDownloadAndImportService.exportBudgetResolveInput(request, response, id, timeType, target);
			} else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
				//月计划模式控制
				if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)&&mRollSubperiod==0) {
					//月计划上报+月滚动
					this.fbsBudgetDownloadAndImportService.exportBudgetRollMonth(request, response, id, timeType, target, this.getCurrentUser());
				} else if(BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)&&mRollSubperiod>0){
					this.fbsBudgetDownloadAndImportService.exportBudgetRollMonthWeek(request, response, id, timeType, target, this.getCurrentUser());
				} else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
					//月计划上报+月分解周
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
				//年计划模式控制+年份解
				this.fbsBudgetDownloadAndImportService.exportBudgetResolve(request, response, id, timeType, target);
			} else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
				//月计划模式控制
				if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode) || BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
					//月计划上报+月滚动
					this.fbsBudgetDownloadAndImportService.exportBudgetRollMonth(request, response, id, timeType, target, this.getCurrentUser());
				} else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
					//月计划上报+月分解周
					this.fbsBudgetDownloadAndImportService.exportBudgetResolve(request, response, id, timeType, target);
				} else {
					this.fbsBudgetDownloadAndImportService.exportBudget(request, response, id, timeType, target);
				}
			} else {
				this.fbsBudgetDownloadAndImportService.exportBudget(request, response, id, timeType, target);
			}
		}
	}
	/**
	 * 导出预算分解模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/exportBudgetResolve")
	public void exportBudgetResolve(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetResolve(request, response, id, timeType, target);
	}
	/**
	 * 导出月计划上报+月滚动模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/exportBudgetRollMonth")
	public void exportBudgetRollMonth(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetRollMonth(request, response, id, timeType, target, this.getCurrentUser());
	}
	/**
	 * 导出实时查看
	 *
	 * @param request
	 * @param response
	 * @param corpId
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/exportBudgetCur")
	public void exportBudgetCur(HttpServletRequest request, HttpServletResponse response, String corpId, String timeType, String beginDate,
								String endDate, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetCur(request, response, corpId, timeType, beginDate, endDate, target);
	}
	/**
	 * 下载计划明细录入模式模板
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/downloadFbsBudgetDetail")
	public void downloadFbsBudgetDetail(HttpServletRequest request, HttpServletResponse response, String target, String itemCode, String itemName) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadFbsBudgetDetail(request, response, target, itemCode, itemName);
	}
	/**
	 * 下载上报界面预算明细模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/downloadFbsBudgetDetailByInput")
	public void downloadFbsBudgetDetailByInput(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadFbsBudgetDetailByInput(request, response, id, timeType, target);
	}
	/**
	 * 导出计划明细录入模式模板
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/exportBudgetDetail")
	public void exportBudgetDetail(HttpServletRequest request, HttpServletResponse response, String target, String budgetId, String detailId) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetDetail(request, response, target, budgetId, detailId);
	}
	/**
	 * 导出
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @throws Exception
	 */
	@GetMapping("/exportFact")
	public void exportFact(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetFact(request, response, id);
	}
	/**
	 * 导出
	 *
	 * @param request
	 * @param response
	 * @param corpId
	 * @throws Exception
	 */
	@GetMapping("/exportSales")
	public void exportSales(HttpServletRequest request, HttpServletResponse response, String corpId, String queryDate) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetSales(request, response, corpId, queryDate);
	}
	/**
	 * 下载月滚动+双月周计划上报模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/downloadBudgetRollMonthWeek")
	public void downloadBudgetRollMonthWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudgetRollMonthWeek(request, response, id, timeType, target, this.getCurrentUser());
	}
	/**
	 * 下载月滚动+双月周计划上报模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/exportBudgetRollMonthWeek")
	public void exportBudgetRollMonthWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.exportBudgetRollMonthWeek(request, response, id, timeType, target, this.getCurrentUser());
	}
	/**
	 * 下载季滚动计划上报模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
	@GetMapping("/downloadBudgetRollQuarter")
	public void downloadBudgetRollQuarter(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
		this.fbsBudgetDownloadAndImportService.downloadBudgetRollQuarter(request, response, id, timeType, target, this.getCurrentUser());
	}
	@PostMapping("/uploadRoll")
	@ResponseBody
	public Object uploadRoll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ExcelImportDTO dto = (ExcelImportDTO) fbsBudgetDownloadAndImportService.uploadRoll(request,response,this.getCurrentUser());
		dto.setAllTotal(null == dto.getList() ? 0 : Long.parseLong(String.valueOf(dto.getList().size())));
		setExcelCache(dto, "50000001", false);
		return JsonUtils.toJSONString(dto);
	}
    /**
     * 导出季滚动计划上报
     *
     * @param request
     * @param response
     * @param id
     * @param timeType
     * @throws Exception
     */
    @GetMapping("/exportBudgetRollQuarter")
    public void exportBudgetRollQuarter(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
        this.fbsBudgetDownloadAndImportService.exportBudgetRollQuarter(request, response, id, timeType, target, this.getCurrentUser());
    }
    /**
     * 下载周滚动计划上报模板
     *
     * @param request
     * @param response
     * @param id
     * @param timeType
     * @throws Exception
     */
    @GetMapping("/downloadBudgetRollWeek")
    public void downloadBudgetRollWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) throws Exception {
        this.fbsBudgetDownloadAndImportService.downloadBudgetRollWeek(request, response, id, timeType, target, this.getCurrentUser());
    }
	/**
	 * 导出周滚动计划上报
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @throws Exception
	 */
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
		dto.setList(list);
		dto.setCode(key);
		if(isPage) {
			dto.setKey(key);
			dto.setAllTotal(Long.parseLong(String.valueOf(successList.size())));
			dto.setTotal(Long.parseLong(String.valueOf(errorList.size())));
		}
	}
	public String getKey(String code) {
		return code + this.getUserId();//UUIDGenerator.generate()
	}
}
