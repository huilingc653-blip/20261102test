package com.bytter.check.web.controller;
import com.bytter.check.service.FcBankBillTempService;
import com.bytter.check.vo.FcBankBillTempVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 银行未达账初始化
 * @author onlineGenerator
 * @date 2017-06-08 09:16:58
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/fcbankbilltemp")
public class FcBankBillTempController extends BaseController {
	@Autowired
	private FcBankBillTempService fcBankBillTempService;
	/**
	* 银行未达账初始化列表 页面跳转
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_CHECK_FCBANKBILLTEMP_APPLY)
	@LogMessage(content = "查询银行未达账初始化列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "check/fcbankbilltemp/listFcBankBillTemp";
	}
	
	/**
	 * 保存导入数据
	 * @param list
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_CHECK_FCBANKBILLTEMP_APPLY)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<FcBankBillTempVo> list) {
		this.fcBankBillTempService.saveImport(list, getCurrentUser());
		return success();
	}
	
}
