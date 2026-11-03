package com.bytter.credential.web.international.controller;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.LcConfigConstant;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.service.ReceiveLetterService;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtBankDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
@Controller
@RequestMapping("/international/credit/receiveletter")
public class ReceiveLetterController extends BaseController {
	@Autowired 
	private ReceiveLetterService receiveLetterService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService currencyService;
	@DubboReference(check = false)
	private IBtBankService bankService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService excrateAdjustService;
	@DubboReference(check = false)
	private IBtSwiftInfoService swiftInfoService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	/**
	* 新增页面跳转
	*/
	@GetMapping("/addInfo")
	public String addLoanInfo(Model model) {
		//初始化参数
		this.initConfig(model);
		return "international/credit/receive/addReceiveLetterOfCredit";
	}
	/**
	 * 初始化系统参数
	 *
	 * @param model
	 */
	private void initConfig(Model model) {
		model.addAttribute("budgetSel", configService.getConfig(LcConfigConstant.LC_CONFIG_RECEIVE, LcConfigConstant.LC_CONFIG_RECEIVE_BUDGETSEL).getValue());
		model.addAttribute("payItem", configService.getConfig(LcConfigConstant.LC_CONFIG_RECEIVE, LcConfigConstant.LC_CONFIG_RECEIVE_INCITEM).getValue());
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_RECEIVE);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL);
	}
	/**
	 * 登记-入库 提交
	 */
	@LogMessage(content = "添加收证登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/tempOrSubmit")
	@ResponseBody
	public AjaxJson tempOrSubmit(LcEnrolVO entity){
		receiveLetterService.tempOrSubmit(entity,getCurrentUser());
		return success();
	}
	/**
	 * 登记修改
	 */
	@GetMapping("/registerUpdate")
	public String registerUpdate(Model model) {
		return "international/credit/receive/listRegisterAdd";
	}
	/**
	 * 功能介绍 :登记修改查询页面table表<br>
	 * @author zhengxn
	 */
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return receiveLetterService.queryAll(dataTablesInput, getCurrentUser());
	}
	/**
	 * 修改-页面跳转
	 */
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id, Model model) {
		LcEnrol lcEnrol = receiveLetterService.get(id,true);
		BtBankDTO bankDTO = bankService.getByBankCode(lcEnrol.getIssuingBank());
		model.addAttribute("bankName",bankDTO.getBankName());
		model.addAttribute("showOpeningBankName", swiftInfoService.getByCode(lcEnrol.getOpeningBank()));
		model.addAttribute("lcEnrol", lcEnrol);
		model.addAttribute("corp", corpService.get(lcEnrol.getCorpId()));
		//初始化参数
		this.initConfig(model);
		return "international/credit/receive/updateReceiveLetterOfCredit";
	}
	/**
	 * 清零收到信用证登记
	 * @param id 主键id
	 * @return
	 */
	@LogMessage(content = "清零收到信用证登记",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/reset/{id}")
	@ResponseBody
	public AjaxJson reset(@PathVariable("id") String id){
		receiveLetterService.reset(id,getCurrentUser());
		return success();
	}
	/**
	 * 删除收到信用证登记
	 * @param id 主键id
	 * @return
	 */
	@LogMessage(content = "删除收到信用证登记",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		receiveLetterService.delLcEnrol(id, getCurrentUser());
		return success();
	}
	/**
	 * 加载收到信用证的INFO信息
	 */
	@LogMessage(content = "查询收到信用证信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewReceiveInfo")
	public String viewPerfectlyLcEnrolInfo(String id, String flag,Model model) {
		if(EmptyUtils.isNotEmpty(id) && !"null".equals(id)) {
			LcEnrol lcEnrol = receiveLetterService.get(id);
			LcEnrolVO lcEnrolVO = new LcEnrolVO();
			BeanUtils.copyProperties(lcEnrol,lcEnrolVO);

			receiveLetterService.viewPerfectlyLcEnrolInfo(lcEnrolVO);
			lcEnrolVO.setRatItemCode(lcEnrol.getItemCode());
			BigDecimal ratExchangeRate=fcExcrateAdjustService.getExchangeRateBy(lcEnrolVO.getIssuingCurId(),lcEnrolVO.getConvertCurId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT).setScale(6);

			lcEnrolVO.setRatExchangeRate(ratExchangeRate);
			//获取单位信息
			CorpDTO corpDTO = corpService.get(lcEnrol.getCorpId());
			if(!EmptyUtils.isEmpty(corpDTO)){
				//设置单位名称
				lcEnrolVO.setCorpName(corpDTO.getName());
			}

			model.addAttribute("lcEnrol", lcEnrolVO);
			model.addAttribute("showBank", bankService.getByBankCode(lcEnrol.getIssuingBank()).getBankName());
			model.addAttribute("flag",flag);
			//初始化参数
			this.initConfig(model);
		}
		return "international/credit/receive/viewReceiveLetterOfCredit";
	}
	/**
	 * 功能介绍 :收证修改/打回<br>
	 */
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(LcEnrolVO lcEnrol) {
		receiveLetterService.saveOrUpdate(lcEnrol, getCurrentUser());
		return success();
	}
	/**
	 * 入库审批页面
	 */
	//@RequiresPermissions(ResourceConstant.CMS_PROVIDE_INFO_EDIT)
	@GetMapping("/receiveApproveList")
	public String receiveApproveList(String flowStatus,Model model) {
		model.addAttribute("flowStatus", flowStatus);
		return "international/credit/receive/approve/listReceiveApprove";
	}
	
	
	/**
	 * 功能介绍 :入库审批页面table表<br>
	 * @author zhengxn
	 */
	//@RequiresPermissions(ResourceConstant.CMS_PROVIDE_DEFERRED_APPLY_QUERY)
	@PostMapping("/getDataInfoApproveTables")
	@ResponseBody
	public DataTablesOutput getDataInfoApproveTables(@RequestBody DataTablesInput dataTablesInput) {
		return receiveLetterService.getDataInfoApproveTables(dataTablesInput);
	}
	/**
	 * 申请审批 详情页面
	 */
	@GetMapping("/infoApprove")
	public String infoApprove(String businessId, String taskId, Model model) {
		if(!EmptyUtils.isEmpty(businessId)){
			LcEnrol info = receiveLetterService.get(businessId);
			model.addAttribute("info", info);
		}
		model.addAttribute("taskId", taskId);//必须要做此步骤
		return "international/credit/receive/approve/viewReceiveApprove";
	}
	/**
	 * 获取币种汇率
	 * @return
	 */
	@PostMapping("/getCurExrate")
	@ResponseBody
	public AjaxJson getCurExrate(String origCurId,@RequestParam(required = false) String convertCurId, Date preDate){
		if (null == preDate) {
			preDate = new Date();
		}else {
			preDate = DateUtils.setSpecifiedDateOfTime(preDate);
		}
		if (StringUtils.isEmpty(convertCurId)) {//不填写转换币种默认转换位人民币
			convertCurId = currencyService.getRMBCurrency().getId();
		}
		BigDecimal exchangeRate = excrateAdjustService.getExchangeRateBy(origCurId, convertCurId, DateUtils.setSpecifiedDateOfTime(preDate), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setObj(exchangeRate);
		ajaxJson.setSuccess(true);
		return ajaxJson;
	}
	/**
	 * 手机审批
	 * @param id 主键id
	 * @param flag 是否显示关闭按钮标志
	 * @param model
	 * @return
	 */
	@LogMessage(content = "手机审批",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/mobileViewReceiveLetterOfCredit")
	public String MobileViewPerfectlyLcEnrolInfo(String id, String flag,String businessId,Model model) {
		if(EmptyUtils.isNotEmpty(id) && !"null".equals(businessId)) {
			LcEnrol lcEnrol = receiveLetterService.get(businessId);
			LcEnrolVO lcEnrolVO = new LcEnrolVO();
			BeanUtils.copyProperties(lcEnrol,lcEnrolVO);
			
			model.addAttribute("lcEnrol", lcEnrolVO);
			model.addAttribute("credirMoneyUpper", StringUtils.chineseMoney(lcEnrol.getCreditMoney()));
			model.addAttribute("showBank", bankService.getByBankCode(lcEnrol.getIssuingBank()).getBankName());
			model.addAttribute("flag",flag);
			model.addAttribute("showOpeningBankName", swiftInfoService.getByCode(lcEnrol.getOpeningBank()));
		}
		return "international/credit/receive/approve/mobileViewReceiveLetterOfCredit";
	}
	/**
	 * 信用证报表查询
	 * @param dataTablesInput
	 * @return
	 */
	@LogMessage(content = "收证报表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/queryReportDataTable")
	@ResponseBody
	public DataTablesOutput queryReportDataTable(@RequestBody DataTablesInput dataTablesInput) {
		ICurrentUser user = getCurrentUser();
		return receiveLetterService.queryReportDataTable(dataTablesInput, user);
	}
	/**
	 * 信用证报表查询页面跳转
	 * @param model
	 * @return
	 */
	@LogMessage(content = "收证报表查询页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listReportLetter")
	public String listReportCredits(Model model) {
		return "international/report/listReportLetter";
	}
	/**
	 *查询改收证下所有交单记录
	 * @param model
	 * @return
	 */
	@GetMapping("/viewReportArrival")
	public String viewReportArrival(String id, Model model) {
		model.addAttribute("id", id);
		return "international/report/viewReportArrival";
	}
}
