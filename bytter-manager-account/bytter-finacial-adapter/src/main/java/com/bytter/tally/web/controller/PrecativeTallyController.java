package com.bytter.tally.web.controller;

import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.dto.CmsPrecativeLoanInfoDTO;
import com.bytter.finance.dto.CmsPrecativeLoanSendDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.tally.dto.FcAccbookPageDTO;
import com.bytter.tally.service.FinacialManualService;
import com.bytter.tally.service.PrecativeTallyService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller   委托贷款记账
 * @Description: PRECATIVE
 * @author
 * @date 2017-07-04 14:58:22
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/bankCollect/precativeTally")
public class PrecativeTallyController extends BaseController {
	@Autowired
	private PrecativeTallyService precativeTallyService;
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
	private IConfigService configService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@Autowired
	private FinacialManualService finacialManualService;
	//@RequiresPermissions(ResourceConstant.FCL_PRECATIVE_TALLY_APPLY)
	@LogMessage(content = "查询记账列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{menuType}/{type}")
	public String list(@PathVariable("menuType") String menuType, @PathVariable("type") String type, Model model) {
		model.addAttribute("opType", type);
		model.addAttribute("menuType", menuType);
		return "finacial/bankcollect/precativetally/listPrecativeTally";
	}
	@GetMapping("/manualPage")
	public String manualPage(String opType, String menuType, Model model) {
		model.addAttribute("opType", opType);
		model.addAttribute("menuType", menuType);
		return "finacial/bankcollect/" + FinacialManualConstant.AccTypeMap.get(menuType);
	}
	@PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return precativeTallyService.getDateTable(dataTablesInput, getCurrentUser());
    }
	/**manualPage
	 * 构建记账分录
	 */
	@PostMapping("/processAccbook")
	@ResponseBody
	public FcAccbookPageDTO processAccbook(String key, String type , String menuType){
		return precativeTallyService.createAccbook(key, type, menuType, getCurrentUser());
	}
	/**
	 * 提交FC_ACCBOOK
	 */
	@LogMessage(content = "提交FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(HttpServletRequest request,FcAccbookVoucherDTO fcAccbook, String key, String menuType) {
		precativeTallyService.submit(fcAccbook, key, menuType, getCurrentUser());
		return success();
	}
	@LogMessage(content = "暂存FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key, String menuType) {
		precativeTallyService.save(fcAccbook, key, getCurrentUser(),menuType);
		return success();
	}
    @LogMessage(content = "打回FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key, String menuType) {
		precativeTallyService.backFcAccbook(fcAccbook, key, getCurrentUser(),menuType);
		return success();
	}
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
   	@PostMapping("/pass")
   	@ResponseBody
   	public AjaxJson pass(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key, String menuType) {
   		precativeTallyService.createVoucher(fcAccbook, key, getCurrentUser(),menuType);
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
	public List<FcAccDTO> getFcAcc(HttpServletRequest request, String curId, String netId, String corpId,String subjectId) {
		List<FcAccDTO> list = innerAccountService.listAccountByParamsAndGrant(curId,netId,corpId,subjectId,null,getUserId());
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
	public String toRepayPlan(String billCode,String subjectCode, Date tallyDate, FcAccDTO dto, Model model) {
		CmsPrecativeLoanSendDTO send = iCmsLoanService.getCmsPrecativeLoanSendByBillCode(billCode);
		CmsPrecativeLoanInfoDTO info = iCmsLoanService.getCmsPrecativeLoanInfoByBillCode(send.getFatherCode());
		FcSubjectDTO subject = fcSubjectService.findSubjectById(subjectCode);
		CorpDTO net = iCorpService.get(info.getCorpId());
		if(EmptyUtils.isEmpty(tallyDate)){
			tallyDate = send.getSendDate();
		}
		precativeTallyService.swapData(send, info, subject, net, dto);
		model.addAttribute("net", net);
		model.addAttribute("send", send);
		model.addAttribute("dto", dto);
		model.addAttribute("tallyDate", tallyDate);
		model.addAttribute("info", info);
		model.addAttribute("precationAcc", dto.getPrecationAcc());
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
		if("09".equals(subject.getAccType())){
			if(EmptyUtils.isEmpty(dto.getPrecationAcc())){
				throw new ParameterException(CollectMessageEnum.FIN_COLLECT_PRECATIONACC_FIRST);
			}
			return "finacial/bankcollect/precativetally/addPrecativeFcAcc";
		}else if("10".equals(subject.getAccType())){
			return "finacial/bankcollect/precativetally/addFcAccEntrustedDeposit";
		}
		return "";
	}
}
