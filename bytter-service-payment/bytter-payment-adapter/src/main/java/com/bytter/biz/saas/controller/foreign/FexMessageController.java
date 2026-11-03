package com.bytter.biz.saas.controller.foreign;

import com.bytter.biz.foreign.entity.FexMessage;
import com.bytter.biz.foreign.service.FexMessageService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huangx
 * @date 2019/7/15.
 */
@Api(value = "/v1-payment/foreign/fexmessage", tags = "外汇管理日志表")
@RestController("v1FexMessageController")
@RequestMapping("/v1-payment/foreign/fexmessage")
public class FexMessageController extends BaseController {
	
	@Autowired
	private FexMessageService fexMessageService;
	
	/**
	 * 日志查询
	 * @param billNo
	 * @param busType
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping("/list/{billNo}/{busType}")
	public AjaxJson<List<FexMessage>> list(@PathVariable("billNo") String billNo, @PathVariable("busType") String busType, Model model) {
		List<FexMessage> listNisMessage = fexMessageService.listMessage(billNo, busType);
		return success(listNisMessage);
	}
}
