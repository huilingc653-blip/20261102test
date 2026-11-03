package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.service.AbPoolLimitApplyCheckService;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyCheckVO;
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
import java.util.List;
/**
 * @Title: AbPoolLimitApplycheckController
 * @Description: 入池申请额度核准
 * @author jinyong.cao
 * @date 2020/1/21 00:23:15
 *
 */
@Controller
@RequestMapping("/billPool/applyCheck")
public class AbPoolLimitApplyCheckController extends BaseController {
	@Autowired
	private AbPoolLimitApplyCheckService abPoolLimitApplyCheckService;
	
	@Autowired
	private AbLogService abLogService;
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolLimitApplyCheck";
    }
    
    /**
     * 入池申请额度核准
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolLimitApplyCheckService.getDataTablesUnion(input);
    }
    
    
    /**
     * 跳转核准额度页面
     * @param ids
     * @param model
     * @return
     */
    @GetMapping("/markeApple/{ids}")
    public String retreatPage(@PathVariable("ids") String ids,Integer abCount,
    		BigDecimal abBillMoney,Model model) {
    	//ids业务流水表ID
        model.addAttribute("ids", ids);
        model.addAttribute("abCount", StringUtils.isBlank(ids)?0:abCount);
        model.addAttribute("abBillMoney", abBillMoney);
        return "billPool/addAbPoolLimitApplycheck";
    }
    /**
    *
    * @Title: update
    * @Description:  确认核准
    * @param @param externalCorpVO
    * @param @return    参数
    * @return AjaxJson    返回类型
    * @throws
    */
   @PostMapping("/saveLimitApplyCheck")
   @ResponseBody
   public AjaxJson saveLimitApplyCheck(AbPoolLimitApplyCheckVO abPoolLimitApplyCheckVo) {
       return abPoolLimitApplyCheckService.saveLimitApplyCheck(abPoolLimitApplyCheckVo,this.getCurrentUser());
   }
   
   
   /**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/approveList")
	public String listApprove(String flowStatus, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		return "billPool/approve/listAbLimitApplyCheckApprove";
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
			AbPoolLimitApplyCheckVO abPoolLimitApplyCheckVO = abPoolLimitApplyCheckService.getAbPoolLimitApplyVoById(businessId);
			model.addAttribute("abPoolLimitApplyCheckVO", abPoolLimitApplyCheckVO);
		}
		model.addAttribute("taskId", taskId);
		return "billPool/approve/approveLimitApplyCheck";
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
		return abPoolLimitApplyCheckService.dataTableApprove(dataTablesInput);
	}
   
	
	@GetMapping("/detail/{id}")
   	public String detail(@PathVariable("id") String id,Model model) {
		if (StringUtils.isNotEmpty(id)) {
			AbPoolLimitApplyCheckVO abPoolLimitApplyCheckVO = abPoolLimitApplyCheckService.getAbPoolLimitApplyVoById(id);
			model.addAttribute("abPoolLimitApplyCheckVO", abPoolLimitApplyCheckVO);
			List<AbLog> abLogList = abLogService.listAbLog(id, null);
			model.addAttribute("abLogList", abLogList);
		}
   		return "billPool/viewPoolLimitApplyCheck";
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
			AbPoolLimitApplyCheckVO abPoolLimitApplyCheckVO = abPoolLimitApplyCheckService.getAbPoolLimitApplyVoById(businessId);
			model.addAttribute("abPoolLimitApplyChechVO", abPoolLimitApplyCheckVO);
        }
        model.addAttribute("isApprove", true);
        return  "billPool/mobile/approveLimitApplyCheck";
    }
	
}
