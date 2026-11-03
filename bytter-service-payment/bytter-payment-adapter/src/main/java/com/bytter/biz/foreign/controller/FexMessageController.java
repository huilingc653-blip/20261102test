package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexMessage;
import com.bytter.biz.foreign.service.FexMessageService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author huangx
 * @date 2019/7/15.
 */
@Controller
@RequestMapping("/foreign/fexmessage")
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
	@GetMapping("/list/{billNo}/{busType}")
	public String list(@PathVariable("billNo") String billNo, @PathVariable("busType") String busType, Model model) {
		List<FexMessage> listNisMessage = fexMessageService.listMessage(billNo, busType);
		model.addAttribute("list", listNisMessage);
		return "foreign/listFexMessage";
	}

}
