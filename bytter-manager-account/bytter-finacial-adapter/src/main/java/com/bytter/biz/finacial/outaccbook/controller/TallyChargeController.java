package com.bytter.biz.finacial.outaccbook.controller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.service.TallyChargeService;
import com.bytter.biz.finacial.outaccbook.service.TallyService;
import com.bytter.biz.finacial.outaccbook.vo.TallyOutAccbookVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterStatus;
/**   
 * @Title: 记账冲销控制器  
 * @Description: 表外流水账主档表
 * @author lvwl
 * @date 2017-06-30 11:04:08
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/outaccbook/tallyCharge")
public class TallyChargeController extends BaseController{
	
	@Autowired
	private TallyChargeService tallyChargeService;
	@Autowired
	private TallyService tallyService;
	/**
	 * 记账冲销列表 - 页面跳转
	 */
	////@RequiresPermissions(ResourceConstant.FCL_OUTERP_CHARGE_QUERY)
	@LogMessage(content = "查询记账冲销列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTallyCharge")
	public String listTallyCharge(Model model) {
        return "finacial/outaccbook/listTallyCharge";
	}
	
	/**
    * 记账冲销列表  - 数据获取
    */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALLYCHARGE_QUERY)
    @PostMapping("/dataTableTallyCharge")
    @ResponseBody
    public DataTablesOutput dataTableTallyCharge(@RequestBody  DataTablesInput dataTablesInput){
    	return tallyChargeService.queryTableTallyChargeData(dataTablesInput, FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALLYCHARGE_QUERY, getCurrentUser());
    }
    
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_CHARGE_APPLY)
    @LogMessage(content = "编辑记账冲销",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toTallyCharge/{id}/{oriId}")
	public String toTallyCancel(@PathVariable("id") String id,@PathVariable("oriId") String oriId,Model model) {
    	FcOutAccbook fcOutAccbook = new FcOutAccbook();
    	List<TallyOutAccbookVO> listDtl = new ArrayList<TallyOutAccbookVO>();
    	FcOutAccbook oriBook = new FcOutAccbook();
    	List<TallyOutAccbookVO> oriListDtl = new ArrayList<TallyOutAccbookVO>();
    	if(EmptyUtils.isEmpty(id) || "null".equals(id)){
    		oriBook = tallyService.get(oriId);
    		oriListDtl = tallyService.queryOutDtlById(oriId);
    		
    		BeanUtils.copyProperties(oriBook, fcOutAccbook);
    		fcOutAccbook.setId(null);
    		fcOutAccbook.setTallyDate(null);
    		fcOutAccbook.setRushReason(null);
    		for(TallyOutAccbookVO v : oriListDtl){
    			TallyOutAccbookVO newVO = new TallyOutAccbookVO();
    			BeanUtils.copyProperties(v, newVO);
    			listDtl.add(newVO);
    		}
    	}else{
    		fcOutAccbook = tallyService.get(id);
    		listDtl = tallyService.queryOutDtlById(id);
    		if (!EmptyUtils.isEmptyList(listDtl)) {
    			for (TallyOutAccbookVO accbookVO : listDtl) {
    				accbookVO.setDebitMoney(BigDecimal.ZERO.subtract(accbookVO.getDebitMoney()));
    				accbookVO.setCreditMoney(BigDecimal.ZERO.subtract(accbookVO.getCreditMoney()));
    			}
			}
    		
    		oriBook = tallyService.get(fcOutAccbook.getRelAccbookId());
    		oriListDtl = tallyService.queryOutDtlById(fcOutAccbook.getRelAccbookId());
    	}
    	
		model.addAttribute("fcOutAccbookPage", fcOutAccbook);
		model.addAttribute("listDtl", listDtl);
		model.addAttribute("oriAcc", oriBook);
		model.addAttribute("oriListDtl", oriListDtl);
		model.addAttribute("STATUE_RUSH_WAIT_CHECK", OuterStatus.STATUE_RUSH_WAIT_CHECK.get());
        return "finacial/outaccbook/editTallyCharge";
	}
    
    /**
     * 记账冲账 - 暂存
     */
    ////@RequiresPermissions(ResourceConstant.FCL_OUTER_CHARGE_APPLY)
    @LogMessage(content = "暂存记账冲账",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/tallyChargeTemp")
    @ResponseBody
    public AjaxJson tallyChargeTemp(@RequestParam("oriId") String oriId,@ModelAttribute FcOutAccbook book){
    	tallyChargeService.tallyChargeTemp(oriId, book, getCurrentUser());
		return  success();
	}
    
    /**
     * 记账冲账 - 提交
     */
   // //@RequiresPermissions(ResourceConstant.FCL_OUTER_CHARGE_APPLY)
    @LogMessage(content = "提交记账冲账",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/tallyChargeSubmit")
    @ResponseBody
    public AjaxJson tallyChargeSubmit(@RequestParam("oriId") String oriId,@ModelAttribute FcOutAccbook book){
    	tallyChargeService.tallyChargeSubmit(oriId, book, getCurrentUser());
		return  success();
	}
    
    
    /**
	 * 记账冲销修改列表 - 页面跳转
	 */
    ////@RequiresPermissions(ResourceConstant.FCL_OUTERP_CHARGE_QUERY)
	@LogMessage(content = "查询记账冲销修改列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTallyChargeUpdate")
	public String listTallyChargeUpdate(Model model) {
        return "finacial/outaccbook/listTallyChargeUpdate";
	}
	
	/**
    * 记账冲销修改列表 - 数据获取
    */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALLYCHARGEUPDATE_QUERY)
    @PostMapping("/dataTableTallyChargeUpdate")
    @ResponseBody
    public DataTablesOutput dataTableTallyChargeUpdate(@RequestBody  DataTablesInput dataTablesInput){
    	return tallyChargeService.queryTableTallyChargeUpdateData(dataTablesInput, FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALLYCHARGEUPDATE_QUERY, getCurrentUser());
    }
    
    /**
     * 记账冲销 - 删除
     */
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_CHARGE_APPLY)
    @LogMessage(content = "删除记账冲销",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/tallyChargeDelete")
    @ResponseBody
    public AjaxJson tallyChargeDelete(@RequestParam("id")String id,Model model){
    	FcOutAccbook book = new FcOutAccbook();
    	book.setId(id);
    	tallyChargeService.tallyChargeDelete(book, getCurrentUser());
		return  success();
	}
    
    /**
   	 * 记账冲销复核列表 - 页面跳转
   	 */
   // //@RequiresPermissions(ResourceConstant.FCL_OUTERP_CHARGE_QUERY)
   	@LogMessage(content = "查询记账冲销复核列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
   	@GetMapping("/listTallyChargeCheck")
   	public String listTallyChargeCheck(Model model) {
         return "finacial/outaccbook/listTallyChargeCheck";
   	}
   	
   	/**
	 * 记账冲销复核列表 - 数据获取
	 */
    ////@RequiresPermissions(ResourceConstant.FCL_OUTERP_CHARGE_QUERY)
	@PostMapping("/dataTableTallyChargeCheck")
	@ResponseBody
	public DataTablesOutput dataTableTallyChargeCheck(@RequestBody  DataTablesInput dataTablesInput){
    	return tallyChargeService.queryTableTallyChargeCheckData(dataTablesInput, getCurrentUser());
	}
	 
   // //@RequiresPermissions(ResourceConstant.FCL_OUTER_CHARGE_APPLY)
	@LogMessage(content = "复核记账冲销",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toTallyChargeCheck/{id}")
	public String toTallyChargeCheck(@PathVariable("id") String id,Model model) {
		FcOutAccbook fcOutAccbook = new FcOutAccbook();
		List<TallyOutAccbookVO> listDtl = new ArrayList<TallyOutAccbookVO>();
		FcOutAccbook oriBook = new FcOutAccbook();
		List<TallyOutAccbookVO> oriListDtl = new ArrayList<TallyOutAccbookVO>();
		
		fcOutAccbook = tallyService.get(id);
		listDtl = tallyService.queryOutDtlById(id);
		if (!EmptyUtils.isEmptyList(listDtl)) {
			for (TallyOutAccbookVO accbookVO : listDtl) {
				accbookVO.setDebitMoney(BigDecimal.ZERO.subtract(accbookVO.getDebitMoney()));
				accbookVO.setCreditMoney(BigDecimal.ZERO.subtract(accbookVO.getCreditMoney()));
			}
		}
		
		oriBook = tallyService.get(fcOutAccbook.getRelAccbookId());
		oriListDtl = tallyService.queryOutDtlById(fcOutAccbook.getRelAccbookId());
		
		
		model.addAttribute("fcOutAccbookPage", fcOutAccbook);
		model.addAttribute("listDtl", listDtl);
		model.addAttribute("oriAcc", oriBook);
		model.addAttribute("oriListDtl", oriListDtl);
	    return "finacial/outaccbook/editTallyChargeCheck";
	}
	 
	/**
	 * 记账冲销 - 打回
	 */
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_CHARGE_APPLY)
	@LogMessage(content = "打回记账冲销",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/tallyChargeBack")
	@ResponseBody
	public AjaxJson tallyChargeBack(@ModelAttribute FcOutAccbook book){
		tallyChargeService.tallyChargeBack(book, getCurrentUser());
		return  success();
	}
	  
	/**
	 * 记账冲销 - 批量打回
	 */
	////@RequiresPermissions(ResourceConstant.FCL_OUTER_CHARGE_APPLY)
	@LogMessage(content = "批量打回冲销",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/batchTallyChargeBack")
	@ResponseBody
	public AjaxJson batchTallyChargeBack(@RequestParam("ids[]") List<String> ids,@RequestParam("reason") String reason){
		for (String id : ids) {
			FcOutAccbook book = new FcOutAccbook();
			book.setId(id);
			book.setRushReason(reason);
			tallyChargeService.tallyChargeBack(book, getCurrentUser());
		}
		return  success();
	}
	  
	  /**
	   * 记账冲销 - 复核
	   */
	 // //@RequiresPermissions(ResourceConstant.FCL_OUTER_CHARGE_APPLY)
	  @LogMessage(content = "复核记账冲销",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	  @PostMapping("/tallyChargeCheck")
	  @ResponseBody
	  public AjaxJson tallyChargeCheck(@ModelAttribute FcOutAccbook book){
		  tallyChargeService.tallyChargeCheck(book, getCurrentUser());
		  return  success();
	  }
	  
	  /**
	   * 记账冲销 - 批量复核
	   */
	 // //@RequiresPermissions(ResourceConstant.FCL_OUTER_CHARGE_APPLY)
	  @LogMessage(content = "批量复核冲销",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	  @PostMapping("/batchTallyChargeCheck")
	  @ResponseBody
	  public AjaxJson batchTallyChargeCheck(@RequestParam("ids[]") List<String> ids){
		  for (String id : ids) {
			  FcOutAccbook book = new FcOutAccbook();
			  book.setId(id);
			  tallyChargeService.tallyChargeCheck(book, getCurrentUser());
		  }
		  return  success();
	  }
}
