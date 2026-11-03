package com.bytter.biz.receipt.controller;
import com.bytter.biz.receipt.service.EBillPrintService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
/**   
 * @Title: Controller  
 * @Description: 打印记录处理
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/receipt/billPrint")
public class EBillPrintController extends BaseController {
	@Autowired
	private EBillPrintService billPrintService;
	/**
	 * 打印记录处理
	 * @param request
	 * @return
	 */
	@LogMessage(content = "打印记录处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/prints")
	@ResponseBody
	public AjaxJson prints(HttpServletRequest request){
		String corpId=request.getParameter("corpId");
		String billNo=request.getParameter("billNo");
		billPrintService.prints(corpId, billNo,getCurrentUser());
		return success();
	}
	/**
	 * 批量打印记录处理
	 * @param request
	 * @return
	 */
	@LogMessage(content = "打印记录处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/BatchPrints")
	@ResponseBody
	public AjaxJson BatchPrints(HttpServletRequest request){
		String corpIds=request.getParameter("corpIds");
		String billNos=request.getParameter("billNos");
		String[] corpIdStr = corpIds.split(CommonConstant.COMMA_SPLIT);
		String[] billNoStr = billNos.split(CommonConstant.COMMA_SPLIT);
		for(int i=0; i <corpIdStr.length;i++) {
			billPrintService.prints(corpIdStr[i], billNoStr[i], getCurrentUser());
		}
		return success();
	}
	
}
