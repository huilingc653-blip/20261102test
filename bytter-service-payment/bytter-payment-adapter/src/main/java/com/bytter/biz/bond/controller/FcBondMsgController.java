package com.bytter.biz.bond.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.api.IFcBondLogService;
import com.bytter.guarantee.vo.FcBondLogVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 
 * @author fusb
 * @version 1.0
 * @Title FcBondMsgController
 * @Description 保证金日志 Controller
 * @date 2018年9月12日
 */
@Controller
@RequestMapping("/bond/fcbondlog")
public class FcBondMsgController extends BaseController {
	@DubboReference(check = false)
	IFcBondLogService fcBondLogService;
	/**
	 * 跳转到日志页面
	 * 
	 * @param model
	 * @param bondId
	 * @return
	 */
	@LogMessage(content = "查询fc_bond_log列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@RequestMapping("listlog")
	public String listLog(Model model, String bondId) {
		model.addAttribute("bondId", bondId);
		return "bond/listFcBondLogMsg";
	}
	/**
	 * 通过保证金ID查询相应的日志信息
	 * 
	 * @param input
	 * @param bondId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("listlogdata")
	@ResponseBody
	public DataTablesOutput listLogDate(@RequestBody DataTablesInput input, String bondId) {
		return fcBondLogService.listFcBondMsg(bondId, input);
	}
	/**
	 * 通过保证金ID，从FC_BOND_LOG日志表中得到相应的操作信息
	 * 
	 * @return
	 */
	@GetMapping("/listopmessage/{id}")
	public String list(@PathVariable("id") String id, Model model) {
		List<FcBondLogVO> listOpMessage = fcBondLogService.listOpMessage(id);
		model.addAttribute("list", listOpMessage);
		return "bond/listOpLogMessage";
	}
}
