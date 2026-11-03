package com.bytter.biz.finacial.outaccbook.controller;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.service.TallyInvaidService;
import com.bytter.biz.finacial.outaccbook.service.TallyService;
import com.bytter.biz.finacial.outaccbook.vo.TallyOutAccbookVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterStatus;
/**   
 * @Title: 记账控制器  
 * @Description: 表外流水账主档表
 * @author onlineGenerator
 * @date 2017-06-30 11:04:08
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/outaccbook/tallyCancel")
public class TallyInvalidController extends BaseController {
	@Autowired
	private TallyInvaidService tallyInvaidService;
	@Autowired
	private TallyService tallyService;
	
	/**
	 * 记账作废 - 页面跳转
	 */
	////@RequiresPermissions(ResourceConstant.FCL_OUTERP_INVALID_QUERY)
	@LogMessage(content = "查询记账作废列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTallyCancel")
	public String listTallyCancel(Model model) {
        return "finacial/outaccbook/listTallyCancel";
	}
	
	/**
    * 记账作废   table列表
    */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALLINVALID_QUERY)
    @PostMapping("/dataTableTallyCancel")
    @ResponseBody
    public DataTablesOutput dataTableTallyCancel(@RequestBody  DataTablesInput dataTablesInput){
		return tallyInvaidService.queryTableTallyCancelData(dataTablesInput,FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALLINVALID_QUERY, getCurrentUser());
    }
    
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_INVALID_APPLY)
    @LogMessage(content = "编辑记账作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toTallyCancel/{id}")
	public String toTallyCancel(@PathVariable("id") String id,Model model) {
    	FcOutAccbook fcOutAccbook = tallyService.get(id,true);
		List<TallyOutAccbookVO> listDtl = tallyService.queryOutDtlById(id);
		model.addAttribute("fcOutAccbookPage", fcOutAccbook);
		model.addAttribute("listDtl", listDtl);
		model.addAttribute("STATUE_INVALID_WAIT_CHECK", OuterStatus.STATUE_INVALID_WAIT_CHECK.get());
        return "finacial/outaccbook/editTallyCancel";
	}
    
    /**
     * 记账作废 - 暂存
     */
   // //@RequiresPermissions(ResourceConstant.FCL_OUTER_INVALID_APPLY)
    @LogMessage(content = "暂存记账作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/tallyCancelTemp")
    @ResponseBody
    public AjaxJson tallyCancelTemp(@RequestParam("id") String id,@RequestParam("reason") String reason){
    	tallyInvaidService.tallyCancelTemp(id,reason, getCurrentUser());
		return  success();
	}
    
    /**
     * 记账作废 - 提交
     */
   // //@RequiresPermissions(ResourceConstant.FCL_OUTER_INVALID_APPLY)
    @LogMessage(content = "提交记账作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/tallyCancelSubmit")
    @ResponseBody
    public AjaxJson tallyCancelSubmit(@RequestParam("id") String id,@RequestParam("reason") String reason){
    	tallyInvaidService.tallyCancelSubmit(id,reason, getCurrentUser());
		return  success();
	}
    
    /**
	 * 记账作废修改 - 页面跳转
	 */
   // //@RequiresPermissions(ResourceConstant.FCL_OUTERP_INVALID_QUERY)
	@LogMessage(content = "查询记账作废修改列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTallyCancelUpdate")
	public String listTallyCancelUpdate(Model model) {
        return "finacial/outaccbook/listTallyCancelUpdate";
	}
	
	/**
    * 记账作废修改   table列表
    */
    
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALLYCANCELUPDATE_QUERY)
    @PostMapping("/dataTableTallyCancelUpdate")
    @ResponseBody
    public DataTablesOutput dataTableTallyCancelUpdate(@RequestBody  DataTablesInput dataTablesInput){
		return tallyInvaidService.queryTableTallyCancelUpdateData(dataTablesInput,FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALLYCANCELUPDATE_QUERY, getCurrentUser());
    }
    
    
    /**
     * 记账作废 - 删除
     */
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_INVALID_APPLY)
    @LogMessage(content = "删除记账作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/tallyCancelDelete")
    @ResponseBody
    public AjaxJson tallyCancelDelete(@RequestParam("id")String id,Model model){
    	tallyInvaidService.tallyCancelDelete(id, getCurrentUser());
		return  success();
	}
    
    
    /**
	 * 记账作废复核 - 页面跳转
	 */
   // //@RequiresPermissions(ResourceConstant.FCL_OUTERP_INVALID_QUERY)
	@LogMessage(content = "查询作废复核列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTallyCancelCheck")
	public String listTallyCancelCheck(Model model) {
        return "finacial/outaccbook/listTallyCancelCheck";
	}
	
	/**
    * 记账作废复核   table列表
    */
//	//@RequiresPermissions(ResourceConstant.FCL_OUTERP_INVALID_QUERY)
    @PostMapping("/dataTableTallyCancelCheck")
    @ResponseBody
    public DataTablesOutput dataTableTallyCancelCheck(@RequestBody  DataTablesInput dataTablesInput){
		return tallyInvaidService.queryTableTallyCancelCheckData(dataTablesInput, getCurrentUser());
    }
    
	
    @LogMessage(content = "查询打回记账作废页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toTallyCancelBack/{id}")
	public String toTallyCancelBack(@PathVariable("id") String id,Model model) {
    	FcOutAccbook fcOutAccbook = tallyService.get(id);
		List<TallyOutAccbookVO> listDtl = tallyService.queryOutDtlById(id);
		model.addAttribute("fcOutAccbookPage", fcOutAccbook);
		model.addAttribute("listDtl", listDtl);
        return "finacial/outaccbook/editTallyBack";
	}
    
    /**
     * 记账作废 - 批量打回
     */
   // //@RequiresPermissions(ResourceConstant.FCL_OUTER_INVALID_APPLY)
    @LogMessage(content = "批量打回记账作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/bacthTallyCancelBack")
    @ResponseBody
    public AjaxJson bacthTallyCancelBack(@RequestParam("ids[]") List<String> ids){
    	for (String id : ids) {
    		tallyInvaidService.tallyCancelBack(id, null, getCurrentUser());
		}
		return  success();
	}
    
    /**
     * 记账作废 - 打回
     */
  //  //@RequiresPermissions(ResourceConstant.FCL_OUTER_INVALID_APPLY)
    @LogMessage(content = "打回记账作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/tallyCancelBack")
    @ResponseBody
    public AjaxJson tallyCancelBack(@RequestParam("id") String id,@RequestParam("reason") String reason){
    	tallyInvaidService.tallyCancelBack(id, reason, getCurrentUser());
		return  success();
	}
    
    /**
     * 记账作废 - 批量复核
     */
   // //@RequiresPermissions(ResourceConstant.FCL_OUTER_INVALID_APPLY)
    @LogMessage(content = "复核记账作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/bacthTallyCancelCheck")
    @ResponseBody
    public AjaxJson bacthTallyCancelCheck(@RequestParam("ids[]") List<String> ids){
    	for (String id : ids) {
    		tallyInvaidService.tallyCancelCheck(id, getCurrentUser());
		}
		return  success();
	}
    
    /**
     * 记账作废 - 复核
     */
  //  //@RequiresPermissions(ResourceConstant.FCL_OUTER_INVALID_APPLY)
    @LogMessage(content = "复核记账作废",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/tallyCancelCheck")
    @ResponseBody
    public AjaxJson tallyCancelCheck(@RequestParam("id") String id){
    	tallyInvaidService.tallyCancelCheck(id, getCurrentUser());
		return  success();
	}
}
