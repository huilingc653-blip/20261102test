package com.bytter.biz.saas.controller.allocation;

import com.bytter.biz.netbank.dto.NisFlowRuleDTO;
import com.bytter.biz.netbank.service.NisFlowRuleService;
import com.bytter.biz.netbank.vo.NisFlowRuleVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title: Controller
 * @Description: 资金调拨流水规则
 * @author onlineGenerator
 * @date 2024-05-08
 * @version 1.0
 *
 */
@Api(value = "/v1-payment/netbank/nisFlowRule",tags = "资金调拨流水规则")
@RestController("v1NisFlowRuleController")
@RequestMapping("/v1-payment/netbank/nisFlowRule")
public class NisFlowRuleController extends BaseController {
	@Autowired
	private NisFlowRuleService nisFlowRuleService;


	/**
    * 资金调拨流水规则列表 table列表
    *
    * @return
    */
    @ApiOperation("资金调拨流水规则列表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisFlowRuleVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns( );
    	return AjaxJson.success(nisFlowRuleService.findListBysql(input));
    }


	/**
	 * 添加资金调拨流水规则
	 * @param nisFlowRuleVO
	 * @return
	 */
	@ApiOperation("添加资金调拨流水规则")
	@LogMessage(content = "添加资金调拨流水规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	public AjaxJson save(@RequestBody NisFlowRuleDTO nisFlowRuleVO, HttpServletRequest request){
		ICurrentUser currentUser = this.getCurrentUser();
		nisFlowRuleService.saveV1(nisFlowRuleVO, currentUser);
		return success();
	}

	/**
	 * 资金调拨流水规则编辑页面跳转
	 * @param id
	 * @return
	 */
	@ApiOperation("资金调拨流水规则编辑页面跳转")
	@LogMessage(content = "资金调拨流水规则编辑页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@GetMapping("/update/{id}")
	public AjaxJson<NisFlowRuleVO> update(@PathVariable("id") String id){
		NisFlowRuleVO nisFlowRuleVO = nisFlowRuleService.getNisFlowRule(id);
		return AjaxJson.success(nisFlowRuleVO);
	}

	/**
	 * 修改资金调拨流水规则
	 * @param nisFlowRuleVO
	 * @return
	 */
	@ApiOperation("修改资金调拨流水规则")
	@LogMessage(content = "修改资金调拨流水规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/modify")
	public AjaxJson modify(@RequestBody NisFlowRuleDTO nisFlowRuleVO){
		ICurrentUser currentUser = this.getCurrentUser();
		nisFlowRuleService.updateV1(nisFlowRuleVO, currentUser);
		return success();
	}

	/**
	 * 删除资金调拨流水规则
	 * @param id
	 * @return
	 */
	@ApiOperation("删除资金调拨流水规则")
	@LogMessage(content = "删除资金调拨流水规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	public AjaxJson delete(@PathVariable("id") String id){
		nisFlowRuleService.delete(id);
		return success();
	}

	/**
	 * 资金调拨流水规则详情页
	 *
	 * @return
	 */
	@ApiOperation("资金调拨流水规则详情页")
	@LogMessage(content = "查询资金调拨流水规则详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<NisFlowRuleVO> view(@PathVariable("id") String id) {
		NisFlowRuleVO nisFlowRuleVO = nisFlowRuleService.getNisFlowRule(id);
		return AjaxJson.success(nisFlowRuleVO);
	}
}
