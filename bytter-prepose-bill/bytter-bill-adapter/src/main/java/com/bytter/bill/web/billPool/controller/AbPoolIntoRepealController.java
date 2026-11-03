package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbReceiveBillService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.service.AbPoolIntoRepealService;
import com.bytter.bill.domain.billPool.service.AbPoolPledgeeService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoRepealVO;
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
 * @Title: AbPoolIntoRepealController
 * @Description: 入池撤销Controller
 * @author zhenghj
 * @date 2021/11/8
 */
@Controller
@RequestMapping("/billPool/intoRepeal")
public class AbPoolIntoRepealController extends BaseController {
    @Autowired
    private AbPoolIntoRepealService abPoolIntoRepealService;
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
     * 跳转到入池撤销页面
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "billPool/listAbPoolIntoRepeal";
    }
    /**
     * 入池撤销table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询入池撤销列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		DataTablesOutput dataOutput = abPoolIntoRepealService.getDataTablesUnion(input);
        return dataOutput;
    }
    /**
     * 跳转到入池撤销页面
     * @param ids
     * @param model
     * @return
     */
    @GetMapping("/markeApple/{ids}")
    public String retreatPage(@PathVariable("ids") String ids, Integer abCount, String billFlag, BigDecimal abBillMoney, String abPoolName, Model model) {
    	//ids业务流水表ID
        model.addAttribute("ids", ids);
        model.addAttribute("billFlag", billFlag);
        model.addAttribute("abCount", StringUtils.isBlank(ids)?0:abCount);
        model.addAttribute("abBillMoney", abBillMoney);
        model.addAttribute("abPoolName", abPoolName);
        return "billPool/intoRepealDtl";
    }
    /**
     *
     * @Title: update
     * @Description:  保存入池撤销
     * @param @param externalCorpVO
     * @param @return    参数
     * @return AjaxJson    返回类型
     * @throws
     */
    @PostMapping("/saveRepealDtl")
    @ResponseBody
    public AjaxJson saveRepealDtl(AbPooIntoBeanVO abPooIntoBeanVO) {
        return abPoolIntoRepealService.saveRepealDtl(abPooIntoBeanVO,this.getCurrentUser());
    }
    
    
	/**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/pooIntoRepealApprove")
	public String listApprove(String flowStatus, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		return "billPool/approve/listPooIntoRepealApprove";
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
		return abPoolIntoRepealService.dataTableApprove(dataTablesInput);
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
			AbPoolIntoRepealVO abPoolRepeaVo = abPoolIntoRepealService.queryAbPoolIntoRepealVoByApdId(businessId);
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abPoolRepeaVo.getAccId());
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("isApprove", true);
			model.addAttribute("poolDTO",abPoolRepeaVo);
			abBillAttachmentService.viewAttachment(businessId, model);
		}
		model.addAttribute("taskId", taskId);
		return "billPool/approve/viewPoolIntoRepealApprove";
	}
	
	 /**
	* 查看详细
	*
	* @return
	*/
	@LogMessage(content = "查看入池撤销详细信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}/{status}")
	public String view(@PathVariable("id") String id, @PathVariable("status") String status, Model model) {
		if(StringUtils.isNotEmpty(id)) {
			AbPoolIntoRepealVO abPoolRepeaVo;
			AbReceiveBillVO info = null;
			if("0".equals(status)){
				abPoolRepeaVo = abPoolIntoRepealService.queryAbPoolIntoRepealVoById(id);
				AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolRepeaVo.getViceId(), abPoolRepeaVo.getId());
				if(abAccrecords!=null){
					info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
				}else{
					info = abReceiveBillService.getAbReceiveBillVOById(abPoolRepeaVo.getAid());
				}
				//业务指令流转信息
				if (StringUtils.isNotEmpty(abPoolRepeaVo.getAid())) {
					AbPoolDtl abPoolDtlLast = abPoolIntoRepealService.getAbPoolDtlByLastPdId(id);
					List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(abPoolRepeaVo.getAid(),abPoolDtlLast!=null?abPoolDtlLast.getId():null);
					model.addAttribute("accrecordsList", abAccrecordsList);
				}
			}else{
				abPoolRepeaVo = abPoolIntoRepealService.queryAbPoolIntoRepealVoByApdId(id);
				info = abReceiveBillService.getAbReceiveBillVOById(abPoolRepeaVo.getAccId());
				//业务指令流转信息
				if (StringUtils.isNotEmpty(abPoolRepeaVo.getAccId())) {
					AbPoolDtl abPoolDtlLast = abPoolIntoRepealService.getAbPoolDtlByLastPdId(id);
					List<AbAccrecordsVO> abAccrecordsList = abAccrecordsService.getAbAccrecordsbyIdOrPdId(abPoolRepeaVo.getAccId(),abPoolDtlLast!=null?abPoolDtlLast.getId():null);
					model.addAttribute("accrecordsList", abAccrecordsList);
				}
			}
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("poolDTO",abPoolRepeaVo);
			if(StringUtils.isNotEmpty(abPoolRepeaVo.getViceId())){
				List<AbLog> ablogList = abLogService.listAbLog(abPoolRepeaVo.getViceId(), null);
				model.addAttribute("abLogPage", ablogList);
			}
		}
		return "billPool/viewPoolIntoRepealInfo";
	}
    
  /**
	* @param id
	* @return
	*/
	@PostMapping("/opt")
	@ResponseBody
	public AjaxJson siginOpt(String id, Integer opt){
		return abPoolIntoRepealService.updateOptStatus(id, opt,this.getCurrentUser());
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
			AbPoolIntoRepealVO abPoolDtl = abPoolIntoRepealService.queryAbPoolIntoRepealVoById(businessId);
			AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(abPoolDtl.getViceId(), abPoolDtl.getId());
			AbReceiveBillVO info = abReceiveBillService.getAbReceiveBillVOById(abAccrecords.getId());
			model.addAttribute("abReceiveBillPage", info);
			model.addAttribute("isApprove", true);
			model.addAttribute("poolDTO",abPoolDtl);
		}
        return  "billPool/mobile/approvePoolIntoApply";
    }
}
