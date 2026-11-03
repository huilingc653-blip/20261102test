package com.bytter.system.bankacc.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bytter.core.base.BaseController;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.service.ConfigService;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangx
 * @date 2019/8/7.
 */
@Controller
@RequestMapping("/bankacc/config")
public class BankAccConfigController extends BaseController {
	
	@Autowired(required=false)
	private ConfigService configService;


	/*@Autowired
	private StreamBridge streamBridge;
	@GetMapping("/test")
	@ResponseBody
	public void test(){
		String msg = "{\n" +
				"\"userName\":\"张三\",\n" +
				"\"userAge\":\"18\"\n" +
				"}";
		MimeType mimeType = MimeType.valueOf("application/json");
		boolean send = streamBridge.send("init-net-day-close-out-0", MessageBuilder.withPayload(msg).setHeader("InitNetDay","InitNetDay").build(), mimeType);

	}*/
	
	/**
	 * 银行账户管理参数配置
	 * @param model
	 * @return
	 */
//	//@RequiresPermissions("netbank:config:bankAccConfig:apply")
	@GetMapping("/bankAccConfig")
	public String fcsConfig(Model model) {
	    model.addAttribute("config",configService.listByType(ConfigConstant.BANKACC));
	    return "bankacc/bankAccConfig";
	}
}
