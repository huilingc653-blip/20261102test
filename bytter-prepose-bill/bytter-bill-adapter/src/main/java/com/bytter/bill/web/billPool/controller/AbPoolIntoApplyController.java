package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbReceiveBillService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolIntoApplyService;
import com.bytter.bill.domain.billPool.service.AbPoolPledgeeService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoApplyVO;
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
import java.math.BigDecimal;
import java.util.List;
/**
 * @Title: AbPoolIntoApplyController
 * @Description: 入池申请
 * @author jinyong.cao
 * @date 2020/1/10 19:21:01
 *
 */
@Controller
@RequestMapping("/billPool/intoApply")
public class AbPoolIntoApplyController  extends BaseController {
    @Autowired
    private AbPoolIntoApplyService abPoolIntoApplyService;
	@Autowired
	private AbReceiveBillService abReceiveBillService;
	
	@Autowired
	private AbPoolPledgeeService abPoolPledgeeService;
	@Autowired
	private AbBillAttachmentService abBillAttachmentService;
	
    @Autowired
    private AbAccrecordsService abAccrecordsService;
	
	@Autowired
	private AbLogService abLogService;
    /**
     * 跳转到入池申请页面
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolIntoApply";
    }
    /**
     * 入池申请table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询入池申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		DataTablesOutput dataOutput = abPoolIntoApplyService.getDataTablesUnion(input);
        return dataOutput;
    }
    /**
     * 跳转到入池申请页面
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
        return "billPool/intoAppleDtl";
    }
    /**
     *
     * @Title: update
     * @Description:  保存入池新增
     * @param @param externalCorpVO
     * @param @return    参数
     * @return AjaxJson    返回类型
     * @throws
     */
    @PostMapping("/saveAppleDtl")
    @ResponseBody
    public AjaxJson saveAppleDtl(AbPooIntoBeanVO abPooIntoBeanVO) {
        return abPoolIntoApplyService.saveIntoApple(abPooIntoBeanVO,this.getCurrentUser());
    }
    
    
	/**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/pooIntoApprove")
	public String listApprove(String flowStatus, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		return "billPool/approve/listPooIntoApprove";
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
		return abPoolIntoApplyService.dataTableApprove(dataTablesInput);
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
			//获取审批中的数据
			AbPoolIntoApplyVO abPoolDtl =abPoolIntoApplyService.queryPoolIntoVObyId(businessId);
			AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), abPoolDtl.getId());
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("isApprove", true);
			model.addAttribute("poolDTO",abPoolDtl);
			abBillAttachmentService.viewAttachment(businessId, model);
		}
		model.addAttribute("taskId", taskId);
		return "billPool/approve/viewPoolIntoApprove";
	}
	
	 /**
	* 查看详细
	*
	* @return
	*/
	@LogMessage(content = "查看出入池详细信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id,String billFlag, Model model) {
		if(StringUtils.isNotEmpty(id)) {
			//来源未入池流水
			if(billFlag.equals(AbPoolConstant.OPT_INI_Y)){
				AbVoucherinfoVO info = abReceiveBillService.getAbBillInfo(id,AccebillConstant.BRIEF_STATUS_6001);
				model.addAttribute("abReceiveBillPage", info);
				List<AbLog> ablogList = abLogService.listAbLog(info.getVvid(), null);
				model.addAttribute("abLogPage", ablogList);
			}else{
				AbPoolIntoApplyVO abPoolDtl =abPoolIntoApplyService.queryPoolIntoVObyId(id);
				AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), abPoolDtl.getId());
				AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
				model.addAttribute("abReceiveBillPage", info);
				model.addAttribute("poolDTO",abPoolDtl);
				if(StringUtils.isNotEmpty(abPoolDtl.getViceId())){
					List<AbLog> ablogList = abLogService.listAbLog(abPoolDtl.getViceId(), null);
					model.addAttribute("abLogPage", ablogList);
				}
				//业务指令流转信息
				if (StringUtils.isNotEmpty(abAccrecords.getId())) {
					AbPoolDtl abPoolDtlLast = abPoolIntoApplyService.getAbPoolDtlByLastPdId(id);
					List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(abAccrecords.getId(),abPoolDtlLast!=null?abPoolDtlLast.getId():null);
					model.addAttribute("accrecordsList", abAccrecordsList);
				}
			}
			
		}
		return "billPool/viewPoolIntoInfo";
	}
    
  /**
	* @param id
	* @return
	*/
	@PostMapping("/opt")
	@ResponseBody
	public AjaxJson siginOpt(String id,Integer opt){
		return abPoolIntoApplyService.updateOptStatus(id, opt,this.getCurrentUser());
	}
	
	/**
	 * 请求加载票据池内的质权人
	 * @param id
	 * @return
	 */
	@PostMapping("/getPoolInfo")
	@ResponseBody
	public AjaxJson getPoolInfo(String id){
		AjaxJson reJson =success();
		AbPoolPledgeeVO vo = abPoolPledgeeService.getAbPoolPledgeeVOByBpId(id);
		if(StringUtils.isNotEmpty(vo)){
			reJson.setObj(vo);
		}
		return reJson;
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
			//获取审批中的数据
			AbPoolIntoApplyVO abPoolDtl =abPoolIntoApplyService.queryPoolIntoVObyId(businessId);
			AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), abPoolDtl.getId());
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("isApprove", true);
			model.addAttribute("poolDTO",abPoolDtl);
		}
        return  "billPool/mobile/approvePoolIntoApply";
    }
}
