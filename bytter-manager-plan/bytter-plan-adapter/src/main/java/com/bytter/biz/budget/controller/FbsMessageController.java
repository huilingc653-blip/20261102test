package com.bytter.biz.budget.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bytter.biz.budget.entity.FbsMessage;
import com.bytter.biz.budget.service.FbsMessageService;
import com.bytter.core.base.BaseController;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 资金计划日志表
 * @date 2017-05-23 15:20:33
 */
@Controller
@RequestMapping("/budget/fbsmessage")
public class FbsMessageController extends BaseController {
	@Autowired
	private FbsMessageService fbsMessageService;
	
	/**
	 * 日志查询
	 * @param billId
	 * @param busName
	 * @param model
	 * @return
	 */
	@GetMapping("/list/{billId}/{busName}")
	public String list(@PathVariable("billId") String billId, @PathVariable("busName") String busName, Model model) {
		List<FbsMessage> listFbsMessage = fbsMessageService.listMessage(billId, busName);
		model.addAttribute("list", listFbsMessage);
		return "budget/listFbsMessage";
	}
}
