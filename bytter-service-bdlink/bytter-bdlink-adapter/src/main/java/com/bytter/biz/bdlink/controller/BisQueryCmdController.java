package com.bytter.biz.bdlink.controller;
//import org.apache.shiro.authz.annotation.Logical;
//import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.bytter.biz.bdlink.entity.BisQueryCmd;
import com.bytter.biz.bdlink.service.BisQueryCmdService;
import com.bytter.biz.bdlink.vo.BisQueryCmdVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
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
@Controller
@RequestMapping("/bdlink/bisquerycmd")
public class BisQueryCmdController extends BaseController {
	@Autowired
	private BisQueryCmdService bisQueryCmdService;
	
	/**
	* 指令发送
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_SENDQUERY)
	@LogMessage(content = "查询查询指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listSend")
	public String listSend(Model model) {
        return "bdlink/listBisQueryCmdSend";
	}
	
	/**
	* 指令查询
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_QUERY)
	@LogMessage(content = "查询查询指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "bdlink/listBisQueryCmd";
	}
	
    /**
    * 查询指令表 table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
	//@RequiresPermissions(value={ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_SENDQUERY,
//			ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_QUERY},
//			logical= Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return bisQueryCmdService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    
    /**
	* 重发查询指令表
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_SENDAPPLY)
	@LogMessage(content = "修改查询指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_SENDAPPLY)
	@LogMessage(content = "修改查询指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchResend")
	@ResponseBody
	public AjaxJson batchResend(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisQueryCmd bisQueryCmd = bisQueryCmdService.get(id, true);
			bisQueryCmdService.resendBisQueryCmd(bisQueryCmd, this.getCurrentUser());
		}
		return success();
	}
	
	/**
	* 查询指令表新增页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_SENDAPPLY)
	@GetMapping("/add")
	public String add(BisQueryCmd bisQueryCmd, Model model) {
		if (StringUtils.isNotEmpty(bisQueryCmd.getId())) {
			bisQueryCmd = bisQueryCmdService.get(bisQueryCmd.getId());
			model.addAttribute("bisQueryCmdPage", bisQueryCmd);
		}
		model.addAttribute("startTime", DateUtils.addDay(DateUtils.getDateYMD(),-1));
		return "bdlink/addBisQueryCmd";
	}
	
	/**
	 * 添加付款指令表
	 * @param
	 * @return
	 */
	@LogMessage(content = "添加查询指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BisQueryCmdVO bisQueryCmd){
		bisQueryCmdService.addBisQueryCmd(bisQueryCmd, this.getCurrentUser());
		return success();
	}
}
