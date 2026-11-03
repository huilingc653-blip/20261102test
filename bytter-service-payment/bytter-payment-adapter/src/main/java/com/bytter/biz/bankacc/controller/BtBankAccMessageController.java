package com.bytter.biz.bankacc.controller;
import java.util.List;
import com.bytter.biz.inneraccount.entity.BtBankAccMessage;
import com.bytter.biz.inneraccount.service.BtBankAccMessageService;
import com.bytter.system.api.dto.BtBankAccMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bytter.core.base.BaseController;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 结算日志表
 * @date 2017-05-23 15:20:33
 */
@Controller
@RequestMapping("/bankacc/message")
public class BtBankAccMessageController extends BaseController {
	@Autowired
	private BtBankAccMessageService btBankAccMessageService;
	/**
	 * 日志查询
	 * @param billNo
	 * @param busType
	 * @param model
	 * @return
	 */
	@GetMapping("/list/{billNo}/{busType}")
	public String list(@PathVariable("billNo") String billNo, @PathVariable("busType") String busType, Model model) {
		List<BtBankAccMessage> list = btBankAccMessageService.listMessage(billNo, busType);
		model.addAttribute("list", list);
		return "bankacc/listBtBankAccMessage";
	}
}
