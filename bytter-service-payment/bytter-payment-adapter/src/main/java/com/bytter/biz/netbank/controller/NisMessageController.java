package com.bytter.biz.netbank.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bytter.biz.netbank.entity.NisMessage;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.core.base.BaseController;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 结算日志表
 * @date 2017-05-23 15:20:33
 */
@Controller
@RequestMapping("/netbank/nismessage")
public class NisMessageController extends BaseController {
	
	@Autowired
	private NisMessageService nisMessageService;
	
	/**
	 * 日志查询
	 * @param billNo
	 * @param busType
	 * @param model
	 * @return
	 */
	@GetMapping("/list/{billNo}/{busType}")
	public String list(@PathVariable("billNo") String billNo, @PathVariable("busType") String busType, Model model) {
		List<NisMessage> listNisMessage = nisMessageService.listMessage(billNo, busType);
		model.addAttribute("list", listNisMessage);
		return "netbank/listNisMessage";
	}
}
