package com.bytter.increment.web.controller;
import com.bytter.increment.domain.entity.BisFinanceExc;
import com.bytter.increment.domain.service.BisFinanceExcService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.increment.domain.vo.BisFinanceExcVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;

/**
 * @Title: Controller  
 * @Description: 银行理财指令表 TODO
 * @author onlineGenerator
 * @date 2017-08-23 11:08:59
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bdlink/bisfinanceexc")
public class BisFinanceExcController extends BaseController {
	@Autowired
	private BisFinanceExcService bisFinanceExcService;
	/**
	* 指令发送
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDQUERY)
	@LogMessage(content = "查询银行理财指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listSend")
	public String listSend(Model model) {
        return "bdlink/listBisFinanceExcSend";
	}
	
	/**
	* 指令追踪
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_TRACEAPPLY)
	@LogMessage(content = "查询银行理财指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTrace")
	public String listTrace(Model model) {
        return "bdlink/listBisFinanceExcTrace";
	}
	
	/**
	* 指令查询
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_QUERY)
	@LogMessage(content = "查询银行理财指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "bdlink/listBisFinanceExc";
	}
    /**
    * 银行理财指令表 table列表
    *
    * @return
    */
	////@RequiresPermissions(value={ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDQUERY,
//			ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_TRACEQUERY,
//			ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_QUERY},
//			logical= Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return bisFinanceExcService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
	/**
	* 银行理财指令表详情页
	*
	* @return
	*/
    ////@RequiresPermissions(value={ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDQUERY,
//			ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_TRACEQUERY,
//			ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_QUERY},
//			logical= Logical.OR)
	@LogMessage(content = "查询银行理财指令表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BisFinanceExcVO bisFinanceExc = bisFinanceExcService.getByIdUnion(id);
		model.addAttribute("bisFinanceExcPage", bisFinanceExc);
		model.addAttribute("busType", NisMessageConstant.BUS_TYPE_BDLINKFINANCE);
		return "bdlink/viewBisFinanceExc";
	}
	/**
	* 发送理财指令表
	* @param id
	* @return
	*/
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDAPPLY)
	@LogMessage(content = "修改理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDAPPLY)
	@LogMessage(content = "修改理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDAPPLY)
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
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDAPPLY)
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
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDAPPLY)
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
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDAPPLY)
	@LogMessage(content = "修改理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDAPPLY)
	@LogMessage(content = "修改理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISFINANCEEXC_SENDAPPLY)
	@LogMessage(content = "修改理财指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchDefeat")
	@ResponseBody
	public AjaxJson batchDefeat(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisFinanceExc bisFinanceExc = bisFinanceExcService.get(id, true);
			bisFinanceExcService.defeatBisFinanceExc(bisFinanceExc, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 选择理财指令
	 * @return
	 */
	@GetMapping("/select")
	public String select(String corpId, Integer busType, Model model){
		if(StringUtils.isEmpty(corpId)){
			corpId = "--";
		}
		model.addAttribute("corpId", corpId);
		model.addAttribute("busType", busType);
		model.addAttribute("voucherStat", BdlinkConstant.SUCCESS);
		return "bdlink/selectBisFinanceExc";
	}
}
