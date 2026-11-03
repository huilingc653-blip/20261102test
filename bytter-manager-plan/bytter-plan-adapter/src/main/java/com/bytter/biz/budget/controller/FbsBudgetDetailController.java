package com.bytter.biz.budget.controller;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.core.constant.BudgetConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetDetail;
import com.bytter.biz.budget.service.FbsBudgetDetailService;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.vo.FbsBudgetDetailTransmitVO;
import com.bytter.biz.budget.vo.FbsBudgetDetailVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: FBS_BUDGET_DETAIL
 * @date 2017-07-19 14:59:31
 */
@Controller
@RequestMapping("/budget/fbsbudgetdetail")
public class FbsBudgetDetailController extends BaseController {
	@Autowired
	private FbsBudgetDetailService fbsBudgetDetailService;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	/**
	 * 资金计划明细录入
	 *
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@LogMessage(content = "资金计划明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/inputDtl")
	public String inputDtl(FbsBudgetDetailVO vo, Model model) throws Exception {
		model.addAttribute("isApprove",vo.getIsApprove());
		if (StringUtils.isEmpty(vo.getSuperaddId())) {
			FbsBudget budget = fbsBudgetService.get(vo.getBudgetId(), true);//根据id得到预算计划主表
			//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
			String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
			//季计划上报模式:1,季固定;2,季滚动;
			String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
			//周计划上报模式:1,固定;2,周滚动;
			String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
			//月计划模式控制
			if (BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType())) {
				if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)
						|| BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
					fbsBudgetDetailService.inputDtlRollMonth(vo, model);
					model.addAttribute("canBMoney", vo.getCanBMoney());
					return "budget/listFbsBudgetDetail";
				}
			}
			if(BudgetConstant.TIME_TYPE_QUARTER.equals(budget.getTimeType())){
				fbsBudgetDetailService.inputDtlRollQuarter(vo, model);
				model.addAttribute("canBMoney", vo.getCanBMoney());
				return "budget/listFbsBudgetDetail";
			}
			if(BudgetConstant.TIME_TYPE_WEEK.equals(budget.getTimeType())){
				fbsBudgetDetailService.inputDtlRollWeek(vo, model);
				model.addAttribute("canBMoney", vo.getCanBMoney());
				return "budget/listFbsBudgetDetail";
			}
		}
		fbsBudgetDetailService.inputDtl(vo, model);
		model.addAttribute("canBMoney", vo.getCanBMoney());
		return "budget/listFbsBudgetDetail";
	}
	/**
	 * 资金计划明细保存
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@LogMessage(content = "资金计划明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FbsBudgetDetailTransmitVO bean) throws Exception {
		Map<String, Object> map = null;
		if ("input".equals(bean.getFlag())) {
			FbsBudget budget = fbsBudgetService.get(bean.getBudgetId(), true);//根据id得到预算计划主表
			//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
			String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
            //季计划上报模式:1,季固定;2,季滚动;
            String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
            //周计划上报模式:1,固定;2,周滚动;
            String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
			//月计划模式控制
			if (BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType())) {
				if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)
						|| BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
					map = fbsBudgetDetailService.saveRollMonth(bean);
				}
			}
            if (BudgetConstant.TIME_TYPE_QUARTER.equals(budget.getTimeType())) {
                if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)) {
                    map = fbsBudgetDetailService.saveRollQuarter(bean);
                }
            }
            if (BudgetConstant.TIME_TYPE_WEEK.equals(budget.getTimeType())) {
                if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)) {
                    map = fbsBudgetDetailService.saveRollWeek(bean);
                }
            }
            if(!EmptyUtils.isEmpty(map)) {
				AjaxJson json = new AjaxJson();
				json.setObj(map.get("totle"));
				map.remove("totle");
				json.setAttributes(map);
				json.setSuccess(true);
				return json;
			}
		}
		map = fbsBudgetDetailService.toSave(bean);
		AjaxJson json = new AjaxJson();
		json.setObj(map.get("totle"));
		map.remove("totle");
		json.setAttributes(map);
		json.setSuccess(true);
		return json;
	}
	/**
	 * 上期明细
	 *
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@LogMessage(content = "资金计划明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/viewSuperDetail")
	public String viewSuperDetail(FbsBudgetDetailVO vo, Model model) throws Exception {
		fbsBudgetDetailService.viewSuperDetail(vo, model);
		return "budget/listFbsBudgetSuperDetail";
	}
	/**
	 * 查看明细
	 *
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@LogMessage(content = "资金计划明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	public String view(FbsBudgetDetailVO vo, Model model) throws Exception {
		fbsBudgetDetailService.toView(vo, model);
		return "budget/viewFbsBudgetDetail";
	}
	/**
	 * 资金计划明细删除
	 *
	 * @param id
	 * @param budgetId
	 * @return
	 * @throws Exception
	 */
	@LogMessage(content = "资金计划明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/delete/{id}/{budgetId}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id, @PathVariable("budgetId") String budgetId) throws Exception {
		if (!StringUtils.isEmpty(id)) {
			FbsBudgetDetail detail = fbsBudgetDetailService.get(id, true);
			FbsBudget budget = fbsBudgetService.get(budgetId, true);
			if (budget.getStatus() < BudgetConstant.STATUE_CHECK) {
				// 上报
				fbsBudgetDetailService.delete(id);
			} else if (budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
				// 追加
				if (EmptyUtils.isEmpty(detail.getBMoney()) || detail.getBMoney().intValue() == BigDecimal.ZERO.intValue()) {
					fbsBudgetDetailService.delete(id);
				} else {
					detail.setBtempMoney(BigDecimal.ZERO);
					fbsBudgetDetailService.saveOrUpdate(detail);
				}
			}
		}
		return new AjaxJson();
	}
	/**
	 * 保存导入数据
	 *
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<FbsBudgetDetailVO> list) {
		this.fbsBudgetDetailService.saveImport(list, this.getCurrentUser());
		return success();
	}
	/**
	 * 挂接计划执行时，加载科目明细数据
	 *
	 * @param dataTablesInput
	 * @return
	 * @author zuoy 2018-11-16
	 */
	@LogMessage(content = "获取当前科目明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/getProjectDetailInfo")
	@ResponseBody
	public DataTablesOutput getProjectDetailInfo(@RequestBody DataTablesInput dataTablesInput) {
		return this.fbsBudgetDetailService.getProjectDetailInfo(dataTablesInput);
	}
	/**
	 * 普通参数录入导入
	 * @param list
	 * @return
	 */
	@PostMapping("/saveInputImport")
	@ResponseBody
	public AjaxJson saveInputImport(@RequestBody List<FbsBudgetDetailVO> list) {
		this.fbsBudgetDetailService.saveInputImport(list, this.getCurrentUser());
		return success();
	}
	/**
	 * 月滚动导入
	 * @param list
	 * @return
	 */
	@PostMapping("/saveRollMonthImport")
	@ResponseBody
	public AjaxJson saveRollMonthImport(@RequestBody List<FbsBudgetDetailVO> list) throws Exception{
		Map<String, Object> map = new HashMap<>();
		map = this.fbsBudgetDetailService.saveRollMonthImport(list, this.getCurrentUser());
		AjaxJson json = new AjaxJson();
		json.setObj(map.get("totle"));
		map.remove("totle");
		json.setAttributes(map);
		json.setSuccess(true);
		return json;
	}
	/**
	 * 季滚动导入
	 * @param list
	 * @return
	 */
	@PostMapping("/saveRollQuarterImport")
	@ResponseBody
	public AjaxJson saveRollQuarterImport(@RequestBody List<FbsBudgetDetailVO> list) throws Exception{
		Map<String, Object> map = new HashMap<>();
		map = this.fbsBudgetDetailService.saveRollQuarterImport(list, this.getCurrentUser());
		AjaxJson json = new AjaxJson();
		json.setObj(map.get("totle"));
		map.remove("totle");
		json.setAttributes(map);
		json.setSuccess(true);
		return json;
	}
	/**
	 * 周滚动导入
	 * @param list
	 * @return
	 */
	@PostMapping("/saveRollWeekImport")
	@ResponseBody
	public AjaxJson saveRollWeekImport(@RequestBody List<FbsBudgetDetailVO> list) throws Exception{
		Map<String, Object> map = new HashMap<>();
		map = this.fbsBudgetDetailService.saveRollWeekImport(list, this.getCurrentUser());
		AjaxJson json = new AjaxJson();
		json.setObj(map.get("totle"));
		map.remove("totle");
		json.setAttributes(map);
		json.setSuccess(true);
		return json;
	}
}
