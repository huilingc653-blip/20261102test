package com.bytter.biz.bdlink.saas.controller;
//import org.apache.shiro.authz.annotation.Logical;
//import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.bytter.biz.bdlink.entity.BisQueryCmd;
import com.bytter.biz.bdlink.service.BisQueryCmdService;
import com.bytter.biz.bdlink.vo.BisQueryCmdVO;
import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 查询指令表
 * @author onlineGenerator
 * @date 2017-12-28 16:31:56
 * @version 1.0
 *
 */
@Api(value = "查询指令管理",tags = "查询指令管理")
@RestController("v1BisQueryCmdController")
@RequestMapping("/v1-bdlink/bdlink/bisquerycmd")
public class BisQueryCmdController extends BaseController {
	@Autowired
	private BisQueryCmdService bisQueryCmdService;

    /**
    * 查询指令表 table列表
    *
    * @return
    */
    @ApiOperation("查询指令表 table列表")
	@PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisQueryCmdVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(bisQueryCmdService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
	* 重发查询指令表
	* @param id
	* @return
	*/
	@ApiOperation("重发查询指令")
	@LogMessage(content = "重发查询指令",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/resend/{id}")
	@ResponseBody
	public AjaxJson resend(@PathVariable("id") String id){
		BisQueryCmd bisQueryCmd = bisQueryCmdService.get(id, true);
		bisQueryCmdService.resendBisQueryCmd(bisQueryCmd, this.getCurrentUser());
		return success();
	}

	/**
	* 批量重发查询指令表
	* @param ids
	* @return
	*/
	@ApiOperation("批量重发查询指令")
	@LogMessage(content = "批量重发查询指令",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchResend")
	@ResponseBody
	public AjaxJson batchResend(@RequestBody BisIDsDTO bisIDsDTO){
		String ids = bisIDsDTO.getIds();
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisQueryCmd bisQueryCmd = bisQueryCmdService.get(id, true);
			bisQueryCmdService.resendBisQueryCmd(bisQueryCmd, this.getCurrentUser());
		}
		return success();
	}

	/**
	 * 添加付款指令表
	 * @param
	 * @return
	 */
	@ApiOperation("添加查询指令")
	@LogMessage(content = "添加查询指令",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestBody BisQueryCmdVO bisQueryCmd){
		bisQueryCmdService.addBisQueryCmd(bisQueryCmd, this.getCurrentUser());
		return success();
	}
}
