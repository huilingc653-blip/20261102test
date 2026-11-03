package com.bytter.tally.web.controller;

import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.dto.CmsProvideLoanInfoDTO;
import com.bytter.finance.dto.CmsProvideSendDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.tally.service.FinacialManualService;
import com.bytter.tally.service.ProvideTallyService;
import com.bytter.tally.vo.FcAccbookPageVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * <br>
 * <b>类功能：</b>银行借款手工记账<br>
 * <b>作者：</b>zhengxina<br>
 * <b>日期：</b> 2018年4月9日 <br>
 * <b>版权所有：<b>版权所有(C) 2018，拜特科技<br>
 */
@Controller
@RequestMapping("/finacial/bankCollect/provideSendTally")
public class ProvideSendTallyController extends BaseController{
	@Autowired
	private ProvideTallyService provideTallyService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@DubboReference(check = false)
	private ICmsLoanService iCmsLoanService;
	@DubboReference(check = false)
	private IFcSubjectService fcSubjectService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IFcDayCloseService iFcDayCloseService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	
	@Autowired
	private FinacialManualService finacialManualService;
	@LogMessage(content = "查询记账列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{menuType}/{type}")
	public String list(@PathVariable("menuType") String menuType, @PathVariable("type") String type, Model model) {
		model.addAttribute("opType", type);
		model.addAttribute("menuType", menuType);
		return "finacial/bankcollect/providetally/listProvideTally";
	}
	
	@GetMapping("/manualPage")
	public String manualPage(String opType, String menuType, Model model,String onlineSign,String confirm) {
		model.addAttribute("opType", opType);
		model.addAttribute("menuType", menuType);
		model.addAttribute("onlineSign",onlineSign);
		model.addAttribute("confirm",confirm);
		return "finacial/bankcollect/" + FinacialManualConstant.AccTypeMap.get(menuType);
	}
	
	@PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput,String onlineSign,String confirm){
    	return provideTallyService.getProvideDateTable(dataTablesInput, getCurrentUser(),onlineSign,confirm);
    }
	
	/**
	 * 构建记账分录
	 */
	@PostMapping("/processAccbook")
	@ResponseBody
	public FcAccbookPageVO processAccbook(String key, String type , String menuType, String onlineSign){
		return provideTallyService.createAccbook(key, type, menuType, getCurrentUser(),onlineSign);
	}
	
	/**
	 * 提交FC_ACCBOOK
	 */
	@LogMessage(content = "提交FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(HttpServletRequest request,FcAccbookVoucherDTO fcAccbook, String key, String menuType,String onlineSign,String confirm) {
		provideTallyService.submit(fcAccbook, key, menuType, getCurrentUser(),onlineSign,confirm);
		return success();
	}
	
	@LogMessage(content = "暂存FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key,String opType,String onlineSign,String menuType) {
		provideTallyService.save(fcAccbook, key, getCurrentUser(),opType,onlineSign,menuType);
		return success();
	}
	
    @LogMessage(content = "打回FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key,String menuType,String onlineSign) {
    	provideTallyService.backFcAccbook(fcAccbook, key, getCurrentUser(),menuType,onlineSign);
		return success();
	}
	
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
   	@PostMapping("/pass")
   	@ResponseBody
   	public AjaxJson pass(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key,String menuType,String onlineSign) {
    	provideTallyService.createVoucher(fcAccbook, key, getCurrentUser(),onlineSign,menuType);
   		return success();
   	}
    
	@GetMapping("/getVoucherType")
    @ResponseBody
    public List<FcVoucherTypeDTO> json(){
        return fcVoucherTypeService.getFcVoucherType();
    }
	
	/**
	 * 查询有权限的内部帐号
	 */
	@PostMapping("/getFcAcc")
	@ResponseBody
	public List<FcAccDTO> getFcAcc(HttpServletRequest request, String curId, String netId, String corpId, String subjectId, String billCode) {
		CmsProvideLoanInfoDTO info = provideTallyService.queryProvideInfo(billCode);
		List<FcAccDTO> list = innerAccountService.listAccountByBankParams(curId,netId,corpId,subjectId,info.getBankId(),getUserId());
		return EmptyUtils.isEmptyList(list)? new ArrayList<FcAccDTO>() : list;
	}
	
	/**
	 * 跳转到开户页面
	 * 
	 * @param 
	 * @param model
	 * @return
	 */
	@GetMapping("/createFcAcc")
	public String toRepayPlan(String billCode,String subjectCode,String precationAcc, Model model) {
		CmsProvideSendDTO send = iCmsLoanService.getCmsProvideLoanSendByBillCode(billCode);
		CmsProvideLoanInfoDTO info = iCmsLoanService.getLoanInfoVOByBillCode(send.getBillCode());
		FcSubjectDTO subject = fcSubjectService.findSubjectById(subjectCode);
		CorpDTO net = iCorpService.get(info.getCorpId());
		Date closeDay = iFcDayCloseService.getDayCloseFinalDate(info.getCorpId());
		Calendar  calendar = new GregorianCalendar(); 
		calendar.setTime(closeDay); 
		calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动 
		Date closeDate = calendar.getTime(); //这个时间就是日期往后推一天的结果 
		model.addAttribute("closeDate", DateUtils.format(closeDate,"yyyy-MM-dd"));
		model.addAttribute("net", net);
		model.addAttribute("send", send);
		model.addAttribute("info", info);
		model.addAttribute("precationAcc", precationAcc);
		model.addAttribute("subject", subject);
		//顺序号生成模式
		int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
		model.addAttribute("seqNumPage", seqGeneratingMode);
		int accAccNoRule = configService.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
		model.addAttribute("accAccNoRule", accAccNoRule);
		model.addAttribute("curInfo", iBtCurrencyService.getObjectById(info.getCurId()));
		//内部账号生成法则
		String reVal = iFcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
		int relen = reVal.length();
		model.addAttribute("relen", relen);
		return "finacial/bankcollect/providetally/addProvideFcAcc";
	}
	
	
}
