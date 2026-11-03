package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.service.AbReceiveBillService;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.billPool.service.AbPoolOutApplyService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolOutApplyVO;
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
import java.math.BigDecimal;
/**
 * 
　 * @Title: 出池申请controller
　 * @author jy.cao
  * @Description:AbPoolOutApplyController.java
　 * @date 2020年2月28日
　 * @version 1.0
 */
@Controller
@RequestMapping("/billPool/outApply")
public class AbPoolOutApplyController extends BaseController{
	@Autowired
	private AbPoolOutApplyService abPoolOutApplyService;
	
	@Autowired
	private AbReceiveBillService abReceiveBillService;
	@GetMapping("/list")
	public String list(Model model) {
		return "billPool/listAbPoolOutApply";
	}
	/**
	 * 出池申请查询列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return abPoolOutApplyService.getDataTablesUnion(input);
	}
	
	/**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/poolOutApprove")
	public String listApprove(String flowStatus, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		return "billPool/approve/listPooOutApprove";
	}
	
	/** 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
		return abPoolOutApplyService.dataTableApprove(dataTablesInput);
	}
	
	
	/**
     * 跳转出池申请页面
     * @param ids
     * @param model
     * @return
     */
    @GetMapping("/markeApple/{ids}")
    public String retreatPage(@PathVariable("ids") String ids,Integer abCount,
    		String billFlag,
    		BigDecimal abBillMoney,Model model) {
    	//ids业务流水表ID
        model.addAttribute("ids", ids);
        model.addAttribute("billFlag", billFlag);
        model.addAttribute("abCount", StringUtils.isBlank(ids)?0:abCount);
        model.addAttribute("abBillMoney", abBillMoney);
        return "billPool/outApplyDtl";
    }
    @PostMapping("/saveApplyDtl")
    @ResponseBody
    public AjaxJson saveAppleDtl(AbPooIntoBeanVO abPooIntoBeanVO) {
        return abPoolOutApplyService.saveOutApply(abPooIntoBeanVO,this.getCurrentUser());
    }
    
    /**
	 * 跳转至审核详情界面
	 *
	 * @param businessId
	 *            审批流水ID
	 * @param taskId
	 *            流程ID
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String businessId, String taskId, Model model) {
		if(StringUtils.isNotEmpty(businessId)) {
			AbPoolOutApplyVO abPoolOutApplyVO = abPoolOutApplyService.queryPoolOutVObyId(businessId);
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abPoolOutApplyVO.getAccId());
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("isApprove", true);
			model.addAttribute("poolDTO",StringUtils.isBlank(info.getPdId())
					?null:abPoolOutApplyService.queryPoolOutVObyId(info.getPdId()));
		}
		model.addAttribute("taskId", taskId);
		return "billPool/approve/viewPoolOutApprove";
	}
    
    
    /**
     * 指令处理修改
   	* @param id
   	* @return
   	*/
   	@PostMapping("/opt")
   	@ResponseBody
   	public AjaxJson siginOpt(String id,Integer opt){
   		return abPoolOutApplyService.updateOptStatus(id, opt,this.getCurrentUser());
   	}
   	
   	
	/**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
    	if(StringUtils.isNotEmpty(businessId)) {
			AbPoolOutApplyVO abPoolOutApplyVO = abPoolOutApplyService.queryPoolOutVObyId(businessId);
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abPoolOutApplyVO.getAccId());
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("isApprove", true);
			model.addAttribute("poolDTO",StringUtils.isBlank(info.getPdId())
					?null:abPoolOutApplyService.queryPoolOutVObyId(info.getPdId()));
		}
        return  "billPool/mobile/approvePoolOutApply";
    }
    
}
