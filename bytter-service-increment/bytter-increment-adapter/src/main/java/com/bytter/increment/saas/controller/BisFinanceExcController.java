package com.bytter.increment.saas.controller;

import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.domain.entity.BisFinanceExc;
import com.bytter.increment.domain.service.BisFinanceExcService;
import com.bytter.increment.domain.vo.BisFinanceExcVO;
import com.bytter.increment.domain.vo.v1.BisFinanceExcPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller  
 * @Description: 银行理财指令表
 * @author onlineGenerator
 * @date 2017-08-23 11:08:59
 * @version 1.0
 *
 */
@Api(value = "理财指令管理",tags = "理财指令管理")
@RestController("v1BisFinanceExcController")
@RequestMapping("/v1-increment/bdlink/bisfinanceexc")
public class BisFinanceExcController extends BaseController {
	@Autowired
	private BisFinanceExcService bisFinanceExcService;
	/**
    * 银行理财指令表 table列表
    *
    * @return
    */
    @ApiOperation("银行理财指令表 table列表")
	@PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisFinanceExcVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(bisFinanceExcService.getDataTablesUnion(input, this.getCurrentUser()));
    }
	/**
	* 银行理财指令表详情页
	*
	* @return
	*/
	@ApiOperation("银行理财指令详情页")
	@LogMessage(content = "银行理财指令详情页",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<BisFinanceExcPageVO> view(@PathVariable("id") String id, Model model) {
		BisFinanceExcPageVO pageVO = new BisFinanceExcPageVO();
		BisFinanceExcVO bisFinanceExc = bisFinanceExcService.getByIdUnion(id);
		pageVO.setBisFinanceExcPage(bisFinanceExc);
		pageVO.setBusType(NisMessageConstant.BUS_TYPE_BDLINKFINANCE);
		return success(pageVO);
	}
	/**
	* 发送理财指令表
	* @param id
	* @return
	*/
    @ApiOperation("发送理财指令表")
	@LogMessage(content = "发送理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/send/{id}")
	@ResponseBody
	public AjaxJson send(@PathVariable("id") String id){
		BisFinanceExc bisFinanceExc = bisFinanceExcService.get(id, true);
		bisFinanceExcService.sendBisFinanceExc(bisFinanceExc, this.getCurrentUser());
		return success();
	}
	/**
	* 批量发送理财指令表
	* @param ids
	* @return
	*/
    @ApiOperation("批量发送理财指令表")
	@LogMessage(content = "批量发送理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchSend")
	@ResponseBody
	public AjaxJson batchSend(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisFinanceExc bisFinanceExc = bisFinanceExcService.get(id, true);
			bisFinanceExcService.sendBisFinanceExc(bisFinanceExc, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 退回理财指令表
	* @param id
	* @return
	*/
    @ApiOperation("退回理财指令表")
	@LogMessage(content = "修改理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(@RequestParam("id") String id, @RequestParam("backMsg") String backMsg){
		BisFinanceExc bisFinanceExc = bisFinanceExcService.get(id, true);
		bisFinanceExcService.backBisFinanceExc(bisFinanceExc, backMsg, this.getCurrentUser());
		return success();
	}
	/**
	* 批量退回理财指令表
	* @param ids
	* @return
	*/
    @ApiOperation("批量退回理财指令表")
	@LogMessage(content = "修改理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchBack")
	@ResponseBody
	public AjaxJson batchBack(@RequestParam("ids")String ids, @RequestParam("backMsg") String backMsg){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisFinanceExc bisFinanceExc = bisFinanceExcService.get(id, true);
			bisFinanceExcService.backBisFinanceExc(bisFinanceExc, backMsg, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 重发理财指令表
	* @param id
	* @return
	*/
    @ApiOperation("重发理财指令表")
	@LogMessage(content = "修改理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/resend/{id}")
	@ResponseBody
	public AjaxJson resend(@PathVariable("id") String id){
		BisFinanceExc bisFinanceExc = bisFinanceExcService.get(id, true);
		bisFinanceExcService.resendBisFinanceExc(bisFinanceExc, this.getCurrentUser());
		return success();
	}
	/**
	* 批量重发理财指令表
	* @param ids
	* @return
	*/
    @ApiOperation("批量重发理财指令表")
	@LogMessage(content = "批量重发理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchResend")
	@ResponseBody
	public AjaxJson batchResend(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisFinanceExc bisFinanceExc = bisFinanceExcService.get(id, true);
			bisFinanceExcService.resendBisFinanceExc(bisFinanceExc, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 作废理财指令表
	* @param id
	* @return
	*/
    @ApiOperation("作废理财指令表")
	@LogMessage(content = "作废理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/defeat/{id}")
	@ResponseBody
	public AjaxJson defeat(@PathVariable("id") String id){
		BisFinanceExc bisFinanceExc = bisFinanceExcService.get(id, true);
		bisFinanceExcService.defeatBisFinanceExc(bisFinanceExc, this.getCurrentUser());
		return success();
	}
	/**
	* 批量作废理财指令表
	* @param ids
	* @return
	*/
    @ApiOperation("批量作废理财指令表")
	@LogMessage(content = "批量作废理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchDefeat")
	@ResponseBody
	public AjaxJson batchDefeat(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisFinanceExc bisFinanceExc = bisFinanceExcService.get(id, true);
			bisFinanceExcService.defeatBisFinanceExc(bisFinanceExc, this.getCurrentUser());
		}
		return success();
	}

}
