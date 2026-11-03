package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.entity.CgsBill;
import com.bytter.biz.netbank.entity.NisMessage;
import com.bytter.biz.netbank.service.CgsBillService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.netbank.vo.CgsBillVO;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.constant.NisWorkFlowConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 委托收款
 * @author onlineGenerator
 * @date 2017-05-26 11:14:59
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/netbank/cgsbill")
public class CgsBillController extends BaseController {
	@Resource
	private CgsBillService cgsBillService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private NisMessageService nisMessageService;
	@Autowired
    private IWorkFlowService workFlowServiceImpl;
	/**
	* 委托收款列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_QUERY)
	@LogMessage(content = "查询委托收款列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "netbank/listCgsBill";
	}
    /**
    * 委托收款 table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return cgsBillService.findListBysql(dataTablesInput, this.getCurrentUser());
    }
	/**
	* 委托收款新增页面跳转
	*
	* @return
	*/
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_APPLY)
	@GetMapping("/add")
	public String add(CgsBill cgsBill, Model model) {
		if (StringUtils.isNotEmpty(cgsBill.getId())) {
			cgsBill = cgsBillService.get(cgsBill.getId());
			model.addAttribute("cgsBillPage", cgsBill);
		}
		String consignCode = SequenceSingleton.getSequence(SequenceEnum.CGS_BILL_CONSIGN_CODE);
		model.addAttribute("consignCode", consignCode);
		return "netbank/addCgsBill";
	}
	/**
	* 委托收款编辑页面跳转
	*
	* @return
	*/
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_APPLY)
	@GetMapping("/update")
	public String update(CgsBill cgsBill, Model model) {
		if (StringUtils.isNotEmpty(cgsBill.getId())) {
			cgsBill = cgsBillService.get(cgsBill.getId());
			model.addAttribute("cgsBillPage", cgsBill);
		}
		return "netbank/updateCgsBill";
	}
	/**
	* 委托收款详情页
	*
	* @return
	*/
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_QUERY)
	@LogMessage(content = "查询委托收款详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		CgsBillVO cgsBill = cgsBillService.getByUnion(id);
		model.addAttribute("cgsBillPage", cgsBill);
		model.addAttribute("busType", NisMessageConstant.BUS_TYPE_CGS);
		return "netbank/viewCgsBill";
	}
	/**
	 * 数据校验
	 * @param cgsBill
	 * @return
	 */
	private boolean dataCheck(CgsBill cgsBill){
		//收款单位和付款单位不能相同
		if(cgsBill.getPayeeCorpId().equals(cgsBill.getPayerCorpId())){
	         throw new ParameterException(NetbankMessageEnum.NIS_CGSBILL_MESSAGE_CGSCODE_UNIQUE);
		}
		//委托收款金额不能为空且大于0
		if(EmptyUtils.isEmpty(cgsBill.getMoney())){
			throw new ParameterException(NetbankMessageEnum.NIS_CGSBILL_MESSAGE_MONEY_NULL);
		}
		return true;
	}
	/**
	 * 添加委托收款
	 * @param cgsBill
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_APPLY)
	@LogMessage(content = "添加委托收款",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(CgsBill cgsBill){
		//暂存时不校验数据正确性
		//if(cgsBill.getStatus() != 0){
			dataCheck(cgsBill);
	//	}
		cgsBillService.saveCgsBill(cgsBill,false, this.getCurrentUser());
		return success();
	}
	/**
	* 修改委托收款
	* @param cgsBill
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_APPLY)
	@LogMessage(content = "修改委托收款",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(CgsBill cgsBill){
		//暂存时不校验数据正确性
		//if(cgsBill.getStatus() != 0){
			dataCheck(cgsBill);
		//}
		cgsBillService.updateCgsBill(cgsBill, this.getCurrentUser());
		return  success();
	}
	/**
	 * 计算手续费
	 * @return
	 */
	public BigDecimal chargeMoney(BigDecimal money){
		return cgsBillService.chargeMoney(money);
	}
	/**
	 * 查询支付管理参数中的手续费率
	 * @return
	 */
	@RequestMapping(params = "getChargeRateTal")
	@ResponseBody
	public AjaxJson getChargeRateTal(){
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		String chargeRate = configService.getConfig("nis", "chargeRate").getValue();
		j.setObj(chargeRate);
		return j;
	}
	/**
	 * 根据支付结算参数的手续费率算出手续费
	 * @param m
	 * @return
	 */
	@RequestMapping(params = "getChargeRate")
	@ResponseBody
	public AjaxJson getChargeRate(String m){
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		BigDecimal chargeMoney = BigDecimal.ZERO;
		if(StringUtils.isNotBlank(m)){
			chargeMoney = chargeMoney(new BigDecimal(m));
		}
		j.setObj(chargeMoney);
		return j;
	}
	/**
	* 删除委托收款
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_APPLY)
	@LogMessage(content = "删除委托收款",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		cgsBillService.deleteCgsBill(id, this.getCurrentUser());
		return success();
	}
	/**
	* 批量提交委托收款
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_APPLY)
	@LogMessage(content = "批量提交委托收款",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			CgsBill cgsBill = cgsBillService.get(id);
			dataCheck(cgsBill);
			cgsBillService.saveCgsBill(cgsBill,true, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 批量删除委托收款
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_APPLY)
	@LogMessage(content = "批量删除委托收款",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			cgsBillService.deleteCgsBill(id, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 内部托收查询跳转
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_CGSBILL_QUERY)
	@LogMessage(content = "查询内部托收列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/query")
	public String query(Model model) {
        return "netbank/listCgsBillQuery";
	}
	/**
	 * 保存导入数据
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<CgsBillVO> list) {
		this.cgsBillService.saveImport(list, this.getCurrentUser());
		return success();
	}
	/**
	 * 选择账号跳转页面
	 * @param payeeCorpId
	 * @param model
	 * @return
	 */
	@GetMapping("/payeeAccPage")
    public String payeeAccPage(String payeeCorpId, Model model){
		model.addAttribute("payeeCorpId", payeeCorpId);
		return "netbank/cgsBillPayeeAccPage";
    }
	/**
	 * 账号列表页面
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/selectPayeeAcc.json")
    @ResponseBody
    public DataTablesOutput listByPayeeAccDetail(@RequestBody DataTablesInput dataTablesInput) {
        return cgsBillService.listByPayeeAccDetail(dataTablesInput);
    }
	/**
	 * 内部账户列表业务组件
	 * @param netId
	 * @param model
	 * @return
	 */
	@GetMapping("/mulAccInfo")
	public String mulAccInfo(String netId,String curId,String resource,Model model){
		model.addAttribute("netId", netId);
		model.addAttribute("curId", curId);
		model.addAttribute("resource", resource);
		return "netbank/mulAccInfo";
	}
	/**
	 * 账号列表页面
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/listMulAccInfoDetail")
    @ResponseBody
    public DataTablesOutput listMulAccInfoDetail(@RequestBody DataTablesInput dataTablesInput) {
        return cgsBillService.listMulAccInfoDetail(dataTablesInput);
    }
	/**
	 * 收款审批页面
	 * @param model
	 * @return
	 */
	@GetMapping("/listApprovePayee")
	public String listApprovePayeeCgsBill(Model model){
		model.addAttribute("sign","payee");
		return "netbank/listCgsBillApprove";
	}
	/**
	 * 付款审批页面
	 * @param model
	 * @return
	 */
	@GetMapping("/listApprovePayer")
	public String listApprovePayerCgsBill(Model model){
		model.addAttribute("sign","payer");
		return "netbank/listCgsBillApprove";
	}
	/**
	 * 审批列表查询
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public DataTablesOutput dataTableApprove(@RequestBody  DataTablesInput dataTablesInput){
		String sign = "";
		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("sign"))){
			String type = String.valueOf(dataTablesInput.getFormdata().get("sign"));
			if("payee".equals(type)){
				sign = NisWorkFlowConstant.NIS_CGSBILL_PAYEE;
			}else if("payer".equals(type)){
				sign = NisWorkFlowConstant.NIS_CGSBILL_PAYER;
			}
		}
		WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, this.getUserId());
		DataTablesOutput<CgsBillVO> cb = cgsBillService.dataTableApprove(dataTablesInput, workFlowBusiness.getBusinessIdList());
		return workFlowBusiness.merge(cb);
	}
	/**
	 * 审批详情页面
	 * @param id
	 * @param taskId
	 * @param model
	 * @return
	 */
	@GetMapping("/approve")
	public String approve(String id, String taskId, Model model) {
    	CgsBillVO cgsBillVO = new CgsBillVO();
		if (StringUtils.isNotEmpty(id)) {
			cgsBillVO = cgsBillService.getByUnion(id);
			model.addAttribute("cgsBillPage", cgsBillVO);
			model.addAttribute("taskId", taskId);
			List<NisMessage> nisMessage = nisMessageService.listMessage(id,"cgs");
			model.addAttribute("nisMessageList", nisMessage);
		}
		return "netbank/approveCgsBill";
	}
}
