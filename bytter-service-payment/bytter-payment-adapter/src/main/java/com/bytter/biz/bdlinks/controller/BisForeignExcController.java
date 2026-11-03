package com.bytter.biz.bdlinks.controller;

import com.bytter.biz.bdlinks.entity.BisForeignExc;
import com.bytter.biz.bdlinks.service.BisForeignExcService;
import com.bytter.biz.bdlinks.vo.BisForeignExcVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.FexMessageConstant;
import com.bytter.core.constant.Globals;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.system.api.enums.UserMessageEnum;
import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 外币汇款指令表
 * @date 2019-07-15 15:20:33
 */
@Controller
@RequestMapping("/foreign/bisforeignexc")
public class BisForeignExcController extends BaseController {
	@Autowired
	private BisForeignExcService bisForeignExcService;
	@Autowired(required = false)
	private IUserService userService;
	/**
	* 外币汇款指令发送
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_SENDQUERY)
	@LogMessage(content = "查询外币汇款指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listSend")
	public String listSend(Model model) {
        return "foreign/listBisForeignExcSend";
	}
	
	/**
	* 外币汇款指令追踪
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEQUERY)
	@LogMessage(content = "查询外币汇款指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTrace")
	public String listTrace(Model model) {
        return "foreign/listBisForeignExcTrace";
	}
	
	/**
	* 外币汇款指令查询
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_QUERY)
	@LogMessage(content = "查询外币汇款指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "foreign/listBisForeignExc";
	}
    /**
    * 外币汇款指令表 table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
	//@RequiresPermissions(value={ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_SENDQUERY,
//			ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEQUERY,
//			ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_CONFIRMQUERY,
//			ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_QUERY},
//	logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return bisForeignExcService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
	/**
	* 外币汇款指令表详情页
	*
	* @return
	*/
	@LogMessage(content = "查询外币汇款指令表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BisForeignExcVO bisForeignExc = bisForeignExcService.getByIdUnion(id);
		model.addAttribute("bisForeignExcPage", bisForeignExc);
		model.addAttribute("busType", FexMessageConstant.BUS_TYPE_EXC);
		return "foreign/viewBisForeignExc";
	}
	/**
	* 发送外币汇款指令表
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_SENDAPPLY)
	@LogMessage(content = "修改外币汇款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/send/{id}")
	@ResponseBody
	public AjaxJson send(@PathVariable("id") String id){
		BisForeignExc bisForeignExc = bisForeignExcService.get(id, true);
		//审批通过和待发送状态才可以提交
		if (bisForeignExc != null && !BdlinkConstant.WAIT_FILL.equals(bisForeignExc.getVoucherStat())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
		bisForeignExcService.sendBisForeignExc(bisForeignExc, this.getCurrentUser());
		return success();
	}
	/**
	* 批量发送外币汇款指令表
	* @param ids
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_SENDAPPLY)
	@LogMessage(content = "修改外币汇款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchSend")
	@ResponseBody
	public AjaxJson batchSend(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisForeignExc bisForeignExc = bisForeignExcService.get(id, true);
			//审批通过和待发送状态才可以提交
			if (bisForeignExc != null && !BdlinkConstant.WAIT_FILL.equals(bisForeignExc.getVoucherStat())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
			}
			bisForeignExcService.sendBisForeignExc(bisForeignExc, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 退回外币汇款指令表
	* @param id
	* @return
	*/
	//@RequiresPermissions(value={ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_SENDAPPLY,
//			ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "修改外币汇款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(@RequestParam("id") String id, @RequestParam("backMsg") String backMsg){
		BisForeignExc bisForeignExc = bisForeignExcService.get(id, true);
		//准备发送和交易成功的指令不可以退回
		if (BdlinkConstant.PRE_SEND.equals(bisForeignExc.getVoucherStat()) || BdlinkConstant.SUCCESS.equals(bisForeignExc.getVoucherStat())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
		bisForeignExcService.backBisForeignExc(bisForeignExc, backMsg, this.getCurrentUser());
		return success();
	}
	/**
	* 批量退回外币汇款指令表
	* @param ids
	* @return
	*/
	//@RequiresPermissions(value={ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_SENDAPPLY,
//			ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "修改外币汇款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchBack")
	@ResponseBody
	public AjaxJson batchBack(@RequestParam("ids")String ids, @RequestParam("backMsg") String backMsg){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisForeignExc bisForeignExc = bisForeignExcService.get(id, true);
			//准备发送和交易成功的指令不可以退回
			if (BdlinkConstant.PRE_SEND.equals(bisForeignExc.getVoucherStat()) || BdlinkConstant.SUCCESS.equals(bisForeignExc.getVoucherStat())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
			}
			bisForeignExcService.backBisForeignExc(bisForeignExc, backMsg, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 重发外币汇款指令表
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEAPPLY)
	@LogMessage(content = "修改外币汇款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/resend/{id}")
	@ResponseBody
	public AjaxJson resend(@PathVariable("id") String id){
		BisForeignExc bisForeignExc = bisForeignExcService.get(id, true);
		//失败状态才可以提交
		if (bisForeignExc != null && !BdlinkConstant.BANK_FAILED.equals(bisForeignExc.getVoucherStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisForeignExc.getVoucherStat())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
		bisForeignExcService.resendBisForeignExc(bisForeignExc, this.getCurrentUser());
		return success();
	}
	/**
	* 批量重发外币汇款指令表
	* @param ids
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEAPPLY)
	@LogMessage(content = "修改外币汇款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchResend")
	@ResponseBody
	public AjaxJson batchResend(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisForeignExc bisForeignExc = bisForeignExcService.get(id, true);
			//失败状态才可以提交
			if (bisForeignExc != null && !BdlinkConstant.BANK_FAILED.equals(bisForeignExc.getVoucherStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisForeignExc.getVoucherStat())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
			}
			bisForeignExcService.resendBisForeignExc(bisForeignExc, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 作废外币汇款指令表
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEAPPLY)
	@LogMessage(content = "修改外币汇款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/defeat/{id}")
	@ResponseBody
	public AjaxJson defeat(@PathVariable("id") String id){
		BisForeignExc bisForeignExc = bisForeignExcService.get(id, true);
		//失败状态才可以提交
		if (bisForeignExc != null && !BdlinkConstant.BANK_FAILED.equals(bisForeignExc.getVoucherStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisForeignExc.getVoucherStat())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
		}
		bisForeignExcService.defeatBisForeignExc(bisForeignExc, this.getCurrentUser());
		return success();
	}
	/**
	* 批量作废外币汇款指令表
	* @param ids
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEAPPLY)
	@LogMessage(content = "修改外币汇款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchDefeat")
	@ResponseBody
	public AjaxJson batchDefeat(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisForeignExc bisForeignExc = bisForeignExcService.get(id, true);
			//失败状态才可以提交
			if (bisForeignExc != null && !BdlinkConstant.BANK_FAILED.equals(bisForeignExc.getVoucherStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisForeignExc.getVoucherStat())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisForeignExc.getVoucherNo());
			}
			bisForeignExcService.defeatBisForeignExc(bisForeignExc, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 设置支付成功/支付失败
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEAPPLY)
	@LogMessage(content = "查询汇款指令表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/setPayResult/{id}/{voucherStat}")
	public String setPayResult(@PathVariable("id") String id, @PathVariable("voucherStat") String voucherStat, Model model) {
		BisForeignExc bisForeignExc = bisForeignExcService.get(id);
		model.addAttribute("bisForeignExcPage", bisForeignExc);
		model.addAttribute("voucherStat", voucherStat);
		return "foreign/viewSetPayResult";
	}
	/**
	 * 保存设置结果
	 *
	 * @param bisForeignExcVO
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FOREIGN_FOREIGNEXC_BISFOREIGNEXC_TRACEAPPLY)
	@LogMessage(content = "修改汇款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/savePayResult")
	@ResponseBody
	public AjaxJson savePayResult(BisForeignExcVO bisForeignExcVO) {
		ICurrentUser user = this.getCurrentUser();
		UserDTO userDTO = userService.getByLoginName(user.getLoginName());
		String salt = userDTO.getSalt();
		String inputPwd = DigestUtil.md5Hex(bisForeignExcVO.getValidPassword() + salt);
		if (!inputPwd.equals(userDTO.getPassword())) {
			throw new ParameterException(UserMessageEnum.SYS_USER_PASSWORD_NOT_CORRECT);
		}
		bisForeignExcService.setPayResultBisExc(bisForeignExcVO, user);
		return success();
	}

}
