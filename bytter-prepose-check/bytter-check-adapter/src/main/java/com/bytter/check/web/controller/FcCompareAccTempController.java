package com.bytter.check.web.controller;
import com.bytter.check.service.FcCompareAccTempService;
import com.bytter.check.vo.FcCompareAccTempVo;
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
@RequestMapping("/finacial/check/fcCompareAccTemp")
public class FcCompareAccTempController extends BaseController {
	@Autowired
	private FcCompareAccTempService fcCompareAccTempService;
	/**
	 * ERP未达账初始化列表 页面跳转
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_CHECK_FCCOMPAREACCTEMP_APPLY)
	@LogMessage(content = "查询ERP未达账初始化列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/erpList")
	public String erpList(Model model) {
		return "check/fcCompareAccTemp/listFcCompareAccTemp";
	}
	
	/**
	 * 保存导入数据
	 * @param list
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_CHECK_FCCOMPAREACCTEMP_APPLY)
	@PostMapping("/saveErpImport")
	@ResponseBody
	public AjaxJson saveErpImport(@RequestBody List<FcCompareAccTempVo> list) {
		this.fcCompareAccTempService.uploadFcCompareErpTemp(list);
		return success();
	}
	
}
