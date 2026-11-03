package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.service.AbPoolLimitApplyService;
import com.bytter.bill.domain.billPool.vo.AbPoolBusTypeVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyVO;
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
@Controller
@RequestMapping("/abpool/limitApply")
public class AbPoolLimitApplyController extends BaseController{
	@Autowired
	private AbPoolLimitApplyService abPoolLimitApplyService;
	
	@Autowired
	private AbLogService abLogService;
	
	@GetMapping("/list")
    public String list(AbPoolBusTypeVO queryVo,Model model) {
        return "billPool/listAbLimitApply";
    }
	
	
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询额度申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolLimitApplyService.getDataTablesUnion(input);
    }
    
    
	/**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/approveList")
	public String listApprove(String flowStatus, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		return "billPool/approve/listAbLimitApplyApprove";
	}
	
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
		return abPoolLimitApplyService.dataTableApprove(dataTablesInput);
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
		if (StringUtils.isNotEmpty(businessId)) {
			AbPoolLimitApplyVO abPoolLimitApplyVO = abPoolLimitApplyService.getAbPoolLimitApplyVoById(businessId);
			model.addAttribute("abPoolLimitApplyVO", abPoolLimitApplyVO);
		}
		model.addAttribute("taskId", taskId);
		return "billPool/approve/approveLimitApply";
	}
	
	@GetMapping("/add")
	public String add() {
		return "billPool/addAbPoolLimitApply";
	}
	@PostMapping("/updateLimitApply")
	@ResponseBody
	public AjaxJson updateLimitApply(AbPoolLimitApplyVO abPoolLimitApplyVO) {
		return abPoolLimitApplyService.updateLimitApply(abPoolLimitApplyVO,this.getCurrentUser());
	}
	
	@GetMapping("/detail/{id}")
   	public String detail(@PathVariable("id") String id,Model model) {
		if (StringUtils.isNotEmpty(id)) {
			AbPoolLimitApplyVO abPoolLimitApplyVO = abPoolLimitApplyService.getAbPoolLimitApplyVoById(id);
			model.addAttribute("abPoolLimitApplyVO", abPoolLimitApplyVO);
			List<AbLog> abLogList = abLogService.listAbLog(id, null);
			model.addAttribute("abLogList", abLogList);
		}
   		return "billPool/viewPoolLimitApply";
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
        if (StringUtils.isNotEmpty(businessId)) {
        	AbPoolLimitApplyVO abPoolLimitApplyVO = abPoolLimitApplyService.getAbPoolLimitApplyVoById(businessId);
			model.addAttribute("abPoolLimitApplyVO", abPoolLimitApplyVO);
        }
        model.addAttribute("isApprove", true);
        return  "billPool/mobile/approveLimitApply";
    }
    
}
