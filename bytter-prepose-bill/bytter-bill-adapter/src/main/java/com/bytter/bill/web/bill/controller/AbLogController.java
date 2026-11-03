package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**   
 * @Title: Controller  
 * @Description: 票据日志信息
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abLog")
public class AbLogController extends BaseController {
	
	@Autowired
	private AbLogService abLogService;
	/**
	* 票据日志信息查看
	* @return
	*/
	@GetMapping("/abLogView")
	public String abLogView(String ids, Model model) {
		abLogService.initLogViewInfo(ids, model);
		return "bill/accebill/viewAbLog";
	}
}
