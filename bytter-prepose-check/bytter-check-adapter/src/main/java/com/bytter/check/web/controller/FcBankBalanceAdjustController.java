package com.bytter.check.web.controller;
import com.bytter.check.service.FcBankBalanceAdjustService;
import com.bytter.check.vo.AdjustDetailVO;
import com.bytter.check.vo.FcBankBalanceAdjustVO;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**   
 * @Title: Controller  
 * @Description: 对账余额调节表
 * @author onlineGenerator
 * @date 2017-06-02 15:29:08
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/fcbankbalanceadjust")
public class FcBankBalanceAdjustController extends BaseController {
	@Autowired
	private FcBankBalanceAdjustService fcBankBalanceAdjustService;
	/**
	* 对账余额调节表列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BALANCE_ADJUST_QUERY)
	@LogMessage(content = "查询对账余额调节表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
//		model.addAttribute("notAuthNets",fcBankBalanceAdjustService.getNotAuthNets(FinacialResourceConstant.FINACIAL_CHECK_BALANCE_ADJUST_QUERY,getUserId()));//过滤没有权限的网点
        return "check/fcCompareSeach/listFcBankBalanceAdjust";
	}
	
	/**
	 * 功能介绍:银企对账查询 页面跳转<br>
	 * <b>日期：</b>2017年6月2日<br>
	 * @version 1.0
	 * @author caozx
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_ENTERPRISE_QUERY)
	@LogMessage(content = "银企对账查询",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/compareSearch")
	public String compareSearchlist(Model model) {
//		model.addAttribute("notAuthNets",fcBankBalanceAdjustService.getNotAuthNets(FinacialResourceConstant.FINACIAL_CHECK_BANK_ENTERPRISE_QUERY,getUserId()));//过滤没有权限的网点
        return "check/fcCompareSeach/listFcCompareSearch";
	}
	
	/**
	 * 功能介绍:反结转查询 页面跳转<br>
	 * <b>日期：</b>2017年6月2日<br>
	 * @version 1.0
	 * @author caozx
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_OPPOSITE_CARRYOVER_QUERY)
	@LogMessage(content = "反结转查询",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/redoAdjustOver")
	public String redoAdjustOverlist(Model model) {
//		model.addAttribute("notAuthNets",fcBankBalanceAdjustService.getNotAuthNets(FinacialResourceConstant.FINACIAL_CHECK_OPPOSITE_CARRYOVER_QUERY,getUserId()));//过滤没有权限的网点
        return "check/fcCompareSeach/listRedoAdjustOver";
	}
	
	
    /**
    * 对账余额调节表 table列表
    *
    * @return
    */
    
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput,String type){
		return fcBankBalanceAdjustService.listFcBankBalanceAdjust(dataTablesInput,type,getUserId());
    }
    
    /***
     * <b>功能介绍：</b>查询银行结转明细清单<br>
     * <b>日期：</b>2017年10月19日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param
     * @return
     */
    @PostMapping("/queryBankOverDatas")
    @ResponseBody
    public List<FcBankBillVo> queryBankOverDatas(String ajustId, String stayRadios, String handRadios, String checkRadios ){
		return fcBankBalanceAdjustService.queryBankOverDatas(ajustId,stayRadios,handRadios,checkRadios);
    }
    
    /**
     * <b>功能介绍：</b>查询中心(ERP)结转明细清单<br>
     * <b>日期：</b>2017年10月19日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param
     * @return
     */
    @PostMapping("/queryCntrOverDatas")
    @ResponseBody
    public List<FcCompareAccVO> queryCntrOverDatas(String ajustId, String stayRadios, String handRadios, String checkRadios){
		return fcBankBalanceAdjustService.queryCntrOverDatas(ajustId,stayRadios,handRadios,checkRadios);
    }
    
    /**
     * <b>功能介绍：</b>反结转操作<br>
     * <b>日期：</b>2017年10月17日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param
     * @return
     */
    @RequestMapping(params = "doRedoAdjustOver")
	@ResponseBody
	public AjaxJson doRedoAdjustOver(String ids){
		AjaxJson json=new AjaxJson();
		fcBankBalanceAdjustService.doRedoAdjustOver(ids,getCurrentUser());
		json.setSuccess(true);
		return json;
	}
    
    /**
     * <b>功能介绍：</b>跳转余额调节表明细详情<br>
     * <b>日期：</b>2017年10月19日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param dataType
     * @param queryType
     * @param adjId
     * @return
     */
    @PostMapping("/queryAdjustDetaiData")
    @ResponseBody
    public List<AdjustDetailVO> queryAdjustDetaiData(String dataType, String queryType, String adjId){
    	return fcBankBalanceAdjustService.queryAdjustDetaiData(dataType, queryType, adjId);
    }
    
	/**
	* 银企对账查询对账单详情页
	*
	* @return
	*/
	////@RequiresPermissions("finacial.check:fcbankbalanceadjust:view")
	@LogMessage(content = "查询对账单详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewCntrBankDtls/{id}")
	public String viewCntrBankDtls(@PathVariable("id") String id, String param, Model model) {
		FcBankBalanceAdjustVO balAdj = fcBankBalanceAdjustService.getFcBankBalanceAdjustById(id);
		model.addAttribute("balAdj", balAdj);
		if(StringUtils.isEmpty(param)){
			return "check/fcCompareSeach/viewBankBCheck";
		}else{
			return "check/fcCompareSeach/viewBankBCheckPrint";
		}
		
	}
	
	/**
	* 余额调节表详情页
	*
	* @return
	*/
	////@RequiresPermissions("finacial.check:fcbankbalanceadjust:view")
	@LogMessage(content = "余额调节表明细",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewAdjustDetails/{id}")
	public String viewAdjustDetails(@PathVariable("id") String id,String param, Model model) {
		FcBankBalanceAdjustVO balAdj = fcBankBalanceAdjustService.getFcBankBalanceAdjustById(id);
		model.addAttribute("balAdj", balAdj);
		model.addAttribute("param", param);
		return "check/fcCompareSeach/viewAdjustDetails";
	}
	
}
