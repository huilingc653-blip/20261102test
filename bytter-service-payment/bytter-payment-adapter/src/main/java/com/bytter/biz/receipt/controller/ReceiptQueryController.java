package com.bytter.biz.receipt.controller;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.receipt.service.ReceiptQueryService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;

/**
 * @Title: Controller
 * @Description: 电子签章处理
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/receipt/receiptQuery")
public class ReceiptQueryController extends BaseController {
	@Autowired
	private ReceiptQueryService receiptQueryService;
	/**
	* 签章处理 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_SIGNATURE)
	@LogMessage(content = "查询签章类型维护列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "receipt/receiptSignature";
	}
	/**
	 * 签章处理
	 * @param request
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_SIGNATURE)
	@LogMessage(content = "签章处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/signature")
	@ResponseBody
	public AjaxJson signature(HttpServletRequest request){
		String netId=request.getParameter("netId");
		String beginDate=request.getParameter("beginDate");
		String endDate=request.getParameter("endDate");
		String signTypeStr = request.getParameter("signType");
		String[] singTypes = signTypeStr.split(",");
		for(int i=0;i<singTypes.length;i++) {
			receiptQueryService.signature(netId, beginDate, endDate, singTypes[i], getCurrentUser());
		}
		return success();
	}
	/**
	* 根据网点ID获取该网点的日结日期
	* @param netId
	* @return
	*/
	@PostMapping("/getDayOver")
	@ResponseBody
	public AjaxJson getDayOver(String netId){
		String dayOver=receiptQueryService.getDayOverByNetId(netId);
		AjaxJson json=new AjaxJson();
		Map<String, Object> attributes=new HashMap<String, Object>();
		attributes.put("dayOver", dayOver);
		json.setAttributes(attributes);
		json.setSuccess(true);
		return json;
	}
}
