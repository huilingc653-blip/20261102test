package com.bytter.system.subject.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.subject.service.FcSubjectDayService;
import com.bytter.subject.vo.FcSubjectDayVo;
import com.bytter.system.api.constant.FinacialResourceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/**   
 * @Title: Controller  
 * @Description: 科目日结单表
 * @author onlineGenerator
 * @date 2017-06-27 16:27:23
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/closedeal/fcsubjectday")
public class FcSubjectDayController extends BaseController {
	@Autowired
	private FcSubjectDayService fcSubjectDayService;
	/**
	* 科目日结单表列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("fcsubjectday:fcsubjectday:index")
	@LogMessage(content = "查询科目日结单表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(FcSubjectDayVo fs, Model model, Integer pageNum) {
        return "finacial/closedeal/fcsubjectday/listFcSubjectDay";
	}
	/**列表内容**/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_FCSUBJECTDAY_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcSubjectDayService.findSubjectDayGroupBySubject(dataTablesInput,
				FinacialResourceConstant.FINACIAL_FCSUBJECTDAY_QUERY, getUserId());
    }
	
	/**
	* 科目汇总表列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions("fcsubjectday:fcsubjectday:index")
	@LogMessage(content = "查询科目日结单表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listSum")
	public String listSum(FcSubjectDayVo fs, String curr, Model model) {
		Map<String, Object> data = fcSubjectDayService.getFcSubjectDaySum(curr, fs,
				FinacialResourceConstant.FINACIAL_FCSUBJECTDAY_SUM_QUERY, getUserId());
		model.addAttribute("model", data);
		model.addAttribute("currentPage", 1);
		model.addAttribute("netId", fs.getNetId());
		model.addAttribute("optDate", fs.getOptDate());
		model.addAttribute("curId", fs.getCurId());
		
		model.addAttribute("netName", data.get("netName"));
		model.addAttribute("optDate1", data.get("optDate"));
		model.addAttribute("curName", data.get("curName"));
		
		
		return "finacial/closedeal/fcsubjectday/listFcSubjectDaySum";
	}
}
