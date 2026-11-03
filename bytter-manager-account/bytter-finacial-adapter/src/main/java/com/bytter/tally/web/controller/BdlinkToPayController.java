package com.bytter.tally.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.tally.service.BdlinkToPayService;

@Controller
@RequestMapping("/tally/trade")
public class BdlinkToPayController extends BaseController {
	
	@Autowired
	BdlinkToPayService bdlinkToPayService;
	
	@PostMapping("/bdlinkToPay")
	@ResponseBody
	public AjaxJson bdlinkToPay(String voucherNo, String busiType, String toUpdateStatus){
		return bdlinkToPayService.bdlinkToPayTrade(voucherNo, busiType, toUpdateStatus);
	}
}
