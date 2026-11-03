package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.receipt.dto.BillPrintsDTO;
import com.bytter.biz.receipt.service.EBillPrintService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 打印记录处理
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
@Api(value = "/v1-payment/receipt/billPrint", tags = "打印记录处理")
@RestController("v1EBillPrintController")
@RequestMapping("/v1-payment/receipt/billPrint")
public class EBillPrintController extends BaseController {
	@Autowired
	private EBillPrintService billPrintService;
	/**
	 * 打印记录处理
	 * @return
	 */
	@LogMessage(content = "打印记录处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/prints")
	@ResponseBody
	public AjaxJson prints(@RequestBody BillPrintsDTO billPrintDTO){
		billPrintService.prints(billPrintDTO.getCorpId(), billPrintDTO.getBillNo(),getCurrentUser());
		return success();
	}
	/**
	 * 批量打印记录处理
	 * @return
	 */
	@LogMessage(content = "打印记录处理",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/BatchPrints")
	@ResponseBody
	public AjaxJson BatchPrints(@RequestBody List<BillPrintsDTO> billPrintDTOs){
		ICurrentUser currentUser = getCurrentUser();
		for (BillPrintsDTO billPrintDTO : billPrintDTOs) {
			billPrintService.prints(billPrintDTO.getCorpId(), billPrintDTO.getBillNo(),currentUser);
		}
		return success();
	}

}
