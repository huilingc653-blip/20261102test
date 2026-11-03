package com.bytter.credential.web.international.controller;
import com.bytter.credential.domain.international.service.LcMessageService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**   
 * @Title: Controller  
 * @Description: LC_MESSAGEG
 * @author onlineGenerator
 *
 */
@Controller
@RequestMapping("/international/credit/lcprocessmsg")
public class LcProcessMsgController extends BaseController {
	@Autowired
	private LcMessageService lcMessageService;
	/**
	* CMS_PROCESS_MSG列表 页面跳转
	* @return
	*/
	//@RequiresPermissions("cms.basedata:cmsprocessmsg:index")
	@LogMessage(content = "查询CMS_PROCESS_MSG列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model,String billCode) {
		model.addAttribute("billCode", billCode);
        return "international/credit/receive/listLcProcessMsg";
	}
	/**
	 * 日志显示
	 * @param input
	 * @param billCode 业务单号
	 * @return
	 */
	@PostMapping("/listMsgData")
    @ResponseBody
    public  DataTablesOutput listMsgData(@RequestBody DataTablesInput input,String billCode){
		return  lcMessageService.listMsgData(billCode,input);
    }
    
}
