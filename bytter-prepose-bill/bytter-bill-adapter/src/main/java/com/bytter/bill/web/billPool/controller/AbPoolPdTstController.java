package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbReceiveBillService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolIntoApplyService;
import com.bytter.bill.domain.billPool.service.AbPoolOutApplyService;
import com.bytter.bill.domain.billPool.service.AbPoolPdTstService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolOutApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolPdTstVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
/**
 * 
　 * @Title: 质押托管转换controller
　 * @author jy.cao
  * @Description:AbPoolPdTstController.java
　 * @date 2020年3月5日
　 * @version 1.0
 */
@Controller
@RequestMapping("/billPool/pdtst")
public class AbPoolPdTstController extends BaseController{
	@Autowired
	private AbPoolPdTstService abPoolPdTstService;
	
	@Autowired
	private AbReceiveBillService abReceiveBillService;
	
    @Autowired
    private AbPoolIntoApplyService abPoolIntoApplyService;
    
    @Autowired
    private AbPoolOutApplyService abPoolOutApplyService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    
	@Autowired
	private AbLogService abLogService;
	
	@GetMapping("/list")
	public String list(Model model) {
		return "billPool/listAbPoolTst";
	}
	/**
	 * 质押托管转换
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		return abPoolPdTstService.getDataTablesUnion(input);
	}
	
	
	/**
     * 托管转换页面
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
        return "billPool/pdTstApplyDtl";
    }
    @PostMapping("/saveApplyDtl")
    @ResponseBody
    public AjaxJson saveAppleDtl(AbPooIntoBeanVO abPooIntoBeanVO) {
        return abPoolPdTstService.savePdTstApply(abPooIntoBeanVO,this.getCurrentUser());
    }
    
    
    /**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/poolTstApprove")
	public String listApprove(String flowStatus, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		return "billPool/approve/listPooTstApprove";
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
		return abPoolPdTstService.dataTableApprove(dataTablesInput);
	}
	
    
    /**
   	 * 跳转至审核详情界面
   	 *
   	 */
   	@GetMapping("/approve")
   	public String approve(String businessId, String taskId, Model model) {
   		if(StringUtils.isNotEmpty(businessId)) {
			AbPoolIntoApplyVO abPoolDtl =abPoolIntoApplyService.queryPoolIntoVObyId(businessId);
			AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), businessId);
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("isApprove", true);
			model.addAttribute("poolDTO",abPoolDtl);
		}
   		model.addAttribute("taskId", taskId);
   		return "billPool/approve/viewPoolTstApprove";
   	}
    
    
    
    /**
     * 指令处理修改 包括继续和重发
   	* @param id
   	* @return
   	*/
   	@PostMapping("/opt")
   	@ResponseBody
   	public AjaxJson siginOpt(String id,Integer opt){
   		return abPoolPdTstService.updateOptStatus(id, opt,this.getCurrentUser());
   	}
   	
   	
   	
    /**
   	* 查看详细
   	*
   	* @return
   	*/
   	@LogMessage(content = "查看池池转换详细详细信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
   	@GetMapping("/view/{id}")
   	public String view(@PathVariable("id") String id, Model model) {
   		if(StringUtils.isNotEmpty(id)) {
   			AbPoolOutApplyVO abPoolDtl =abPoolOutApplyService.queryPoolOutVObyId(id);
   			AbVoucherinfoVO info = abReceiveBillService.getAbBillInfo(abPoolDtl.getAccId(),AccebillConstant.BRIEF_STATUS_6001);
   			List<AbPoolPdTstVO> tstList = abPoolPdTstService.queryTstInfoList(abPoolDtl.getViceId(),abPoolDtl.getId());
   			model.addAttribute("abReceiveBillPage", info);
   			model.addAttribute("poolDTO",abPoolDtl);
   			if(StringUtils.isNotEmpty(abPoolDtl.getApdId())){
   				List<AbLog> ablogList = abLogService.listAbLog(abPoolDtl.getViceId(), null);
   				model.addAttribute("abLogPage", ablogList);
   			}
   			if(ListUtils.isNotNullOrEmpty(tstList)){
   				model.addAttribute("tstList", tstList);
   			}else{
				//业务指令流转信息
				//根据票据从表id和票据池交易明细id查询流水信息
				AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvId(abPoolDtl.getViceId(),id);
				if (accrecords!=null && StringUtils.isNotEmpty(accrecords.getId())) {
					AbPoolDtl abPoolDtlLast = abPoolIntoApplyService.getAbPoolDtlByLastPdId(id);
					List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(accrecords.getId(),abPoolDtlLast!=null?abPoolDtlLast.getId():null);
					model.addAttribute("accrecordsList", abAccrecordsList);
				}
			}
   		}
   		return "billPool/viewPoolIntoInfo";
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
				AbPoolIntoApplyVO abPoolDtl =abPoolIntoApplyService.queryPoolIntoVObyId(businessId);
				AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), businessId);
				AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
				model.addAttribute("abReceiveBillPage", info);
				model.addAttribute("isApprove", true);
				model.addAttribute("poolDTO",abPoolDtl);
				List<AbPoolPdTstVO> tstList = abPoolPdTstService.queryTstInfoList(abPoolDtl.getViceId(),abPoolDtl.getBpId());
				if(ListUtils.isNotNullOrEmpty(tstList)){
	   				model.addAttribute("tstList", tstList);
	   			}
		}
   		model.addAttribute("taskId", taskId);
        return  "billPool/mobile/approvePoolTstApply";
    }
   	
    
}
