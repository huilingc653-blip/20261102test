package com.bytter.biz.wage.controller;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.netbank.entity.BtChargeBackType;
import com.bytter.biz.netbank.service.BtChargeBackTypeService;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.constant.WageConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.wage.entity.NisWage;
import com.bytter.biz.wage.service.NisWageBatchService;
import com.bytter.biz.wage.service.NisWageDetailService;
import com.bytter.biz.wage.service.NisWageService;
import com.bytter.biz.wage.vo.NisWageBatchVO;
import com.bytter.biz.wage.vo.NisWageDetailVO;
import com.bytter.biz.wage.vo.NisWageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * @Title: Controller
 * @Description: NIS_WAGE
 * @author onlineGenerator
 * @date 2018-01-08 17:19:13
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/wage/niswage")
public class NisWageController extends BaseController {
	@Autowired
	private NisWageService nisWageService;
	@Autowired
	private NisWageBatchService nisWageBatchService;
	@Autowired
	private NisWageDetailService nisWageDetailService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private BtChargeBackTypeService btChargeBackTypeService;
	@DubboReference(check = false)
	private IBisExcService bisExcService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	/**
	* NIS_WAGE列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_TOUCHQUERY)
	@LogMessage(content = "查询NIS_WAGE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		String chargeBackType = "";
		BtChargeBackType btChargeBackType = btChargeBackTypeService.findByDef();
		if(StringUtils.isNotEmpty(btChargeBackType)){
			chargeBackType = btChargeBackType.getBusType();
		}
		model.addAttribute("chargeBackType",chargeBackType);
		//是否启用限额管理  1-是 0-否
		model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS,SysConfigConstant.ISOPENLIMIT).getValue());
		model.addAttribute("bankaccOver", configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_BANKACCOVER).getValue());
        return "wage/listNisWage";
	}
	/**
	* NIS_WAGE列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_DEALQUERY)
	@LogMessage(content = "查询NIS_WAGE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listDeal")
	public String listDeal(Model model) {
        return "wage/listNisWageDeal";
	}
	/**
	* NIS_WAGE列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_QUERY)
	@LogMessage(content = "查询NIS_WAGE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listQuery")
	public String listQuery(Model model) {
        return "wage/listNisWageQuery";
	}
    /**
    * NIS_WAGE table列表
    *
    * @return
    */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_WAGE_NISWAGE_TOUCHQUERY,
//			ResourceConstant.NETBANK_WAGE_NISWAGE_QUERY,
//			ResourceConstant.NETBANK_WAGE_NISWAGE_DEALQUERY},
//			logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return nisWageService.getDataTablesUnionNew(dataTablesInput, this.getCurrentUser());
    }
	/**
	* NIS_WAGE新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(NisWage nisWage, Model model) {
		if (StringUtils.isNotEmpty(nisWage.getId())) {
			nisWage = nisWageService.get(nisWage.getId());
			model.addAttribute("nisWagePage", nisWage);
		}
		return "wage/addNisWage";
	}
	/**
	* NIS_WAGE编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(NisWageVO nisWage, Model model) {
		if (StringUtils.isNotEmpty(nisWage.getId())) {
			nisWage = nisWageService.getByIdUnion(nisWage.getId());
			//暂存和编辑状态才可以提交
			if (nisWage != null && !NetbankConstant.STATUE_TEMP.equals(nisWage.getStatus()) && !NetbankConstant.STATUE_BACK.equals(nisWage.getStatus())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisWage.getWageNo());
			}
			model.addAttribute("nisWagePage", nisWage);
		}
		model.addAttribute("bankaccOver", configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_BANKACCOVER).getValue());
		model.addAttribute("wishPayday", DateUtils.formatDate(DateUtils.getDate(),"yyyy-MM-dd"));
		model.addAttribute("user", this.getUserLoginName());
		//初始化参数
		this.initConfig(model);
		return "wage/updateNisWage";
	}
	/**
	* NIS_WAGE详情页
	*
	* @return
	*/
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_WAGE_NISWAGE_TOUCHQUERY,
//			ResourceConstant.NETBANK_WAGE_NISWAGE_QUERY,
//			ResourceConstant.NETBANK_WAGE_NISWAGE_DEALQUERY},
//			logical = Logical.OR)
	@LogMessage(content = "查询NIS_WAGE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		NisWageVO nisWage = nisWageService.getByIdUnion(id);
        //判断是否经过银行处理
        String isPay = CommonConstant.NO;
        List<BisExcDTO> bisExcDTOList = this.bisExcService.getBisExcByCbsBillNoForNisWage(nisWage.getWageNo(), BdlinkConstant.FROM_NIS_WAGE);
        if(EmptyUtils.isNotEmpty(bisExcDTOList)) {
            //审批通过的
            nisWageDetailService.setNisWagePayResult(nisWage);
            isPay = CommonConstant.YES;
        }
        model.addAttribute("isPay", isPay);
		model.addAttribute("nisWagePage", nisWage);
		model.addAttribute("busType", NisMessageConstant.BUS_TYPE_WAGE);
		model.addAttribute("user", this.getUserLoginName());
		//初始化参数
		this.initConfig(model);
		return "wage/viewNisWage";
	}
	/**
	 * 添加NIS_WAGE
	 * @param nisWage
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_TOUCHAPPLY)
	@LogMessage(content = "添加NIS_WAGE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(NisWage nisWage){
		nisWageService.save(nisWage);
		return success();
	}
	/**
	* 修改NIS_WAGE
	* @param nisWage
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_TOUCHAPPLY)
	@LogMessage(content = "修改NIS_WAGE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(NisWageVO nisWage){
		nisWageService.updateNisWage(nisWage, this.getCurrentUser());
		return  success();
	}
	/**
	 * 批量提交付款通知表
	 *
	 * @param ids
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_TOUCHAPPLY)
	@LogMessage(content = "批量提交代发工资", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchSubmit")
	@ResponseBody
	public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
		nisWageService.batchSubmitNisWage(ids, this.getCurrentUser());
		return success();
	}
	/**
	* 删除NIS_WAGE
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_TOUCHAPPLY)
	@LogMessage(content = "删除NIS_WAGE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}/{chargeBackType}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id,@PathVariable("chargeBackType") String chargeBackType){
		nisWageService.deleteNisWage(id, this.getCurrentUser(),chargeBackType);
		return success();
	}
	/**
	* 批量删除NIS_WAGE
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_TOUCHAPPLY)
	@LogMessage(content = "批量删除NIS_WAGE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@RequestParam("ids")String ids,@RequestParam("chargeBackType")String chargeBackType){
		nisWageService.batchDelNisWage(ids, this.getCurrentUser(),chargeBackType);
		return success();
	}
	/**
	 * 保存代发工资导入数据
	 * 代发工资
	 * @param list
	 * @return
	 */
	@PostMapping("/saveWageImport")
	@ResponseBody
	public AjaxJson saveWageImport(@RequestBody List<NisWageVO> list) {
		String billNo ="";
		billNo = this.nisWageService.saveImport(list, this.getCurrentUser());
		return success(billNo);
	}
	/**
	 * 保存代发工资导入数据(批量导入)
	 * @param list
	 * @return
	 */
	@PostMapping("/saveWageImportBatch")
	@ResponseBody
	public AjaxJson saveWageImportBatch(@RequestBody List<NisWageBatchVO> list) {
		this.nisWageBatchService.saveImport(list, this.getCurrentUser());
		return success();
	}
	/**
	 * 获取付款账号
	 *
	 * @param corpId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getPayerAcc")
	@ResponseBody
	public List<BtBankAccDTO> getPayerAcc(String corpId) {
		List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
		if (!StringUtils.isEmpty(corpId)) {
			//获取单位信息
			CorpDTO corp = corpService.get(corpId);
			String accountCorpId = StringUtils.isBlank(corp.getUserAccountCode()) ? corp.getId() : corp.getUserAccountCode();
			//获取转账权限的银行在线账号
    		List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(accountCorpId, AccountGrantConstant.TRANSFER_ACCOUNT);
    		//获取转账权限的银行离线账号
    		List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(accountCorpId, AccountGrantConstant.TRANSFER_ACCOUNT);
    		if(!EmptyUtils.isEmptyList(listOnlineBankAcc)){
    			listAcc.addAll(listOnlineBankAcc);
    		}
    		if(!EmptyUtils.isEmptyList(listOfflineBankAcc)){
    			listAcc.addAll(listOfflineBankAcc);
    		}
		}
		return listAcc;
	}
	/**
	 * 打回代发工资
	 *
	 * @param id
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_DEALAPPLY)
	@LogMessage(content = "修改代发工资表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/back/{id}")
	@ResponseBody
	public AjaxJson back(@PathVariable("id") String id) {
		nisWageService.backNisWage(id, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量打回代发工资
	 *
	 * @param ids
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_WAGE_NISWAGE_DEALAPPLY)
	@LogMessage(content = "修改代发工资表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchBack")
	@ResponseBody
	public AjaxJson batchBack(@RequestParam("ids") String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			nisWageService.backNisWage(id, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 初始化系统参数
	 * @param model
	 * @param model
	 */
	private void initConfig(Model model){
		model.addAttribute("budgetSel", configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_BUDGETSEL).getValue());
		model.addAttribute("payItem", configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_PAYITEM).getValue());
		//资金计划
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_WAGE);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
		//是否启用限额管理  1-是 0-否
		model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS,SysConfigConstant.ISOPENLIMIT).getValue());
	}
	/**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus, String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "wage/listNisWageApprove";
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
		return this.nisWageService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String id, String taskId, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			this.setApprove(id,taskId,model);
		}
		model.addAttribute("user", this.getUserLoginName());
		return "wage/approveNisWage";
	}
	/**
	 * 跳转至手机审核详情界面
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			this.setApprove(businessId,taskId,model);
		}
		return "wage/viewApproveNisWage";
	}
	public void setApprove(String businessId, String taskId, Model model){
		NisWageVO nisWageVO = this.nisWageService.getByUnion(businessId);
		//判断是否经过银行处理
		String isPay = CommonConstant.NO;
		List<BisExcDTO> bisExcDTOList = this.bisExcService.getBisExcByCbsBillNoForNisWage(nisWageVO.getWageNo(), BdlinkConstant.FROM_NIS_WAGE);
		if(EmptyUtils.isNotEmpty(bisExcDTOList)) {
			//审批通过的
			nisWageDetailService.setNisWagePayResult(nisWageVO);
			isPay = CommonConstant.YES;
		}
		BigDecimal bal = BigDecimal.ZERO;
		Integer count = 0;
		BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(nisWageVO.getPayerAcc(),nisWageVO.getCurId());
		if(!EmptyUtils.isEmpty(bisAccBalDTO)){
			if(!EmptyUtils.isEmpty(bisAccBalDTO.getAvailBal())){
				bal = bisAccBalDTO.getAvailBal();
			}
		}
		List<NisWageDetailVO> list = nisWageDetailService.listByWageId(nisWageVO.getId());
		if(EmptyUtils.isNotEmpty(list)){
			count = list.size();
		}
		model.addAttribute("isPay", isPay);
		model.addAttribute("bal", bal);
		model.addAttribute("count", count);
		model.addAttribute("nisWagePage", nisWageVO);
		model.addAttribute("taskId", taskId);
		//初始化参数
		this.initConfig(model);
		//资金计划
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_WAGE);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
		model.addAttribute("busType", NisMessageConstant.BUS_TYPE_WAGE);
	}
	/**
	 * 对私付款单明细金额与付款金额校验
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "validSuccessWageDetail")
	@ResponseBody
	public AjaxJson validSuccessWageDetail(String ids) {
		AjaxJson json = new AjaxJson();
		String wageNo = "";
		//对私付款校验明细总金额与指令发送金额是否一致
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			NisWageVO wageVO = this.nisWageService.getByIdUnion(id);
			List<NisWageDetailVO> successWagetDtl = nisWageDetailService.listByWageIdSuccess(id);
			if(EmptyUtils.isNotEmpty(successWagetDtl)){
				wageNo = wageVO.getWageNo()+CommonConstant.COMMA_SPLIT;
			}
		}
		if(!StringUtils.isBlank(wageNo)) {
			json.setObj(wageNo.substring(0, wageNo.length()-1));
			json.setSuccess(true);
		}else{
			json.setSuccess(false);
		}
		return json;
	}
	/**
	 * 导出所选明细
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/exportDtl")
	public void exportDtl(HttpServletRequest request, HttpServletResponse response, String ids, String opType) throws Exception {
		this.nisWageService.exportDtl(request, response, ids, opType, this.getCurrentUser());
	}

	/**
	 * 导出全部
	 *
	 * @return
	 */
	@RequestMapping("/exportAll")
	public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
						  String totalParam, String fileNameParam) throws Exception {
		ExportUtils.exportByStreamCopy(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
			@Override
			public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
				DataTablesOutput<NisWageVO> dataTablesOutput = nisWageService.getDataTablesUnionNew(dataTablesInput, user);
				return dataTablesOutput.getData();
			}
		}, totalParam, fileNameParam, 5000, NisWageVO.class);
	}
}
