package com.bytter.check.saas.controller;

import com.bytter.check.reconcile.service.BisTrackingService;
import com.bytter.check.reconcile.vo.BisTrackingVO;
import com.bytter.check.vo.BisTrackingListConfigVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "银企对账状态跟踪",tags = "银企对账状态跟踪")
@RestController("v1BisTrackingController")
@RequestMapping("/v1-check/bis/stateTracking")
public class BisTrackingController extends BaseController {

	@Autowired
	private BisTrackingService bisTrackingService;
	/**
	 * 对账跟踪列表
	 * @return
	 */
	@ApiOperation("获取对账跟踪列表配置信息")
	@GetMapping("/getListConfig")
	public AjaxJson<BisTrackingListConfigVO> getListConfig() {
		BisTrackingListConfigVO configVO = new BisTrackingListConfigVO();
		configVO.setDateStart(DateUtils.format(DateUtils.getLastMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		configVO.setDateEnd(DateUtils.format(DateUtils.getLastMonthEndTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return success(configVO);
	}
	/**
	 * 对账跟踪数据
	 * @param dataTablesInput
	 * @return
	 */
	@ApiOperation("对账跟踪数据Table列表数据")
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<BisTrackingVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(bisTrackingService.getDataTablesUnion(input));
	}
	@ApiOperation("查看对账数据状态追踪")
	@GetMapping("/view/{id}")
	@ResponseBody
    public AjaxJson<BisTrackingVO> edit(@PathVariable("id") String id) {
 	   return success(bisTrackingService.getCheckVo(id));
    }

   @ApiOperation("复核数据")
   @PostMapping("/reviewSure")
   @ResponseBody
   public AjaxJson reviewSure(@RequestBody BisTrackingVO bean) {
		return bisTrackingService.reviewSure(bean, this.getCurrentUser());
   }
	/**
	 * 重新发送指令提交
	 *
	 * @param bisTrackingVO 信息
	 * @return
	 */
	//////@RequiresPermissions("bis:bank:statetracking:check:opt")
	@ApiOperation("重新发送指令")
	@LogMessage(content = "重新发送指令", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/reSend")
	@ResponseBody
	public AjaxJson reSend(@RequestBody BisTrackingVO bisTrackingVO) {
		AjaxJson j = success();
		boolean flag = bisTrackingService.reSend(bisTrackingVO, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}
}
