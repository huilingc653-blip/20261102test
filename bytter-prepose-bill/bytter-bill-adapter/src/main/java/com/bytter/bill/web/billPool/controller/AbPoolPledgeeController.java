package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolCheckService;
import com.bytter.bill.domain.billPool.service.AbPoolPledgeeService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolPledgeeVO;
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
@Controller
@RequestMapping("/abpool/pledgee")
public class AbPoolPledgeeController extends BaseController{
	
    @Autowired
    private AbPoolPledgeeService abPoolPledgeeService;
    
    @Autowired
    private AbPoolCheckService abPoolCheckService;
	
	/**
     * 跳转到质押人设置
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolPledgee";
    }
    
    
    /**
     * 质押人 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询质押人信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolPledgeeService.getDataTablesUnion(input);
    }
    
    
    
    @GetMapping("/permitDtl/{id}")
	public String retreatPage(@PathVariable("id") String id,Model model) {
    	AbPoolPledgeeVO abPoolPledgeeVO = abPoolPledgeeService.getAbPoolPledgeeVOByBpId(id);
    	if(null!=abPoolPledgeeVO 
    			&& StringUtils.isEmpty(abPoolPledgeeVO.getBankAcc())
    			&& !StringUtils.isEmpty(abPoolPledgeeVO.getBpType())
    			&& !abPoolPledgeeVO.getBpType().equals(AbPoolConstant.AB_BILL_POOL_BP_TYPE_1)){
    			abPoolPledgeeVO.setBankAcc(AbPoolConstant.AB_POLL_VALID_SIGN_0);
    	}
    	model.addAttribute("abPoolPledgeeVO",abPoolPledgeeVO);
    	model.addAttribute("flag",false);
		return "billPool/abPoolPledgeeDtl";
	}
    
    @GetMapping("/view/{id}")
   	public String view(@PathVariable("id") String id,Model model) {
    	AbPoolPledgeeVO abPoolPledgeeVO = abPoolPledgeeService.getAbPoolPledgeeVOByBpId(id);
    	if(null!=abPoolPledgeeVO 
    			&& StringUtils.isEmpty(abPoolPledgeeVO.getBankAcc())
    			&& !StringUtils.isEmpty(abPoolPledgeeVO.getBpType())
    			&& !abPoolPledgeeVO.getBpType().equals(AbPoolConstant.AB_BILL_POOL_BP_TYPE_1)){
    			abPoolPledgeeVO.setBankAcc(AbPoolConstant.AB_POLL_VALID_SIGN_0);
    	}
       	model.addAttribute("abPoolPledgeeVO",abPoolPledgeeVO);
       	model.addAttribute("flag",true);
   		return "billPool/abPoolPledgeeDtl";
   	}
    
    
    /**
	* 某一行操作启用/禁用
	* @param id
	* @return
	*/
	@PostMapping("/opt")
	@ResponseBody
	public AjaxJson siginOpt(String id,Integer opt){
        AjaxJson ajaxJson = success();
		//禁用判断是否有在途
		if(String.valueOf(opt).equals(AbPoolConstant.AB_POLL_VALID_SIGN_0) ){
			abPoolCheckService.poolPledgeeCheckQuery(id);
		}
		abPoolPledgeeService.updateDispose(id, opt,this.getCurrentUser());
		ajaxJson.setSuccess(true);
		return success();
	}
    
    
	/**
	 * 
	* @Title: update  
	* @Description: TODO(修改方法)  
	* @param @param externalCorpVO
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	@PostMapping("/updateAbPoolPledgee")
	@ResponseBody
	public AjaxJson updateAbPoolPledgee(AbPoolPledgeeVO abPoolPledgeeVO) {
		return abPoolPledgeeService.settingPoolPledgeeVO(abPoolPledgeeVO,this.getCurrentUser());
	}
    
}
