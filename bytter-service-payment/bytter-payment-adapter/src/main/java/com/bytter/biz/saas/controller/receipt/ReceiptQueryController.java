package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.receipt.service.ReceiptQueryService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 电子签章处理
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
@Api(value = "/v1-payment/receipt/receiptQuery",tags = "电子签章处理")
@RestController("v1ReceiptQueryController")
@RequestMapping("/v1-payment/receipt/receiptQuery")
public class ReceiptQueryController extends BaseController {
	@Autowired
	private ReceiptQueryService receiptQueryService;

	/**
	 * 签章处理
	 * @param request
	 * @return
	 */
	@ApiOperation("签章处理")
	@LogMessage(content = "签章处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/signature")
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
	@ApiOperation("根据网点ID获取该网点的日结日期")
	@PostMapping("/getDayOver")
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
