package com.bytter.check.web.controller;

import com.bytter.check.reconcile.service.BisTrackingService;
import com.bytter.check.reconcile.vo.BisTrackingVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: Controller
 * @Description: 银企对账跟踪Controller
 * @date 2020-10-16
 */
@Controller
@RequestMapping("/bis/stateTracking")
public class BisTrackingController extends BaseController {

	@Autowired
	private BisTrackingService bisTrackingService;
	/**
	 * 对账跟踪列表
	 * @return
	 */
	//////@RequiresPermissions(ReceiptConstant.BIS_BANK_STATETRACKING_CHECK_QUERY)
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("dateStart", DateUtils.format(DateUtils.getLastMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
	    model.addAttribute("dateEnd", DateUtils.format(DateUtils.getLastMonthEndTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return "reconcile/listBisTracking";
	}
	/**
	 * 对账跟踪数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	//////@RequiresPermissions(ReceiptConstant.BIS_BANK_STATETRACKING_CHECK_QUERY)
	public DataTablesOutput dataTable(
			@RequestBody DataTablesInput dataTablesInput) {
		return bisTrackingService.getDataTablesUnion(dataTablesInput);
	}
	/**
    *
    * @return
    */
	//////@RequiresPermissions(ReceiptConstant.BIS_BANK_STATETRACKING_CHECK_QUERY)
    @GetMapping("/view/{id}")
   public String edit(@PathVariable("id") String id, Model model) {
          model.addAttribute("dtlVo", bisTrackingService.getCheckVo(id));
       return "reconcile/viewTracking";
   }
    /**
	* @return
	*/
	@GetMapping("/reviewRecord")
	public String retreatPage(String opt,
			String ids,String bankTypeId,Model model) {
		model.addAttribute("ids", ids);
		model.addAttribute("opt", opt);
		model.addAttribute("bankTypeId", bankTypeId);
		return "reconcile/reviewRecord";
	}
   @PostMapping("/reviewSure")
   @ResponseBody
   public AjaxJson reviewSure(BisTrackingVO bean) {
		return bisTrackingService.reviewSure(bean, this.getCurrentUser());
   }
	/**
	 * 重新发送指令提交
	 *
	 * @param bisTrackingVO 信息
	 * @return
	 */
	//////@RequiresPermissions("bis:bank:statetracking:check:opt")
	@LogMessage(content = "重新发送指令", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/reSend")
	@ResponseBody
	public AjaxJson reSend(BisTrackingVO bisTrackingVO) {
		AjaxJson j = success();
		boolean flag = bisTrackingService.reSend(bisTrackingVO, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
}
