package com.bytter.tally.web.controller;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.dto.FcBusRaceDTO;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.dto.CmsLoanSendDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccPlusDTO;
import com.bytter.tally.service.EnterpriseLoanService;
import com.bytter.tally.vo.FcAccbookPageVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>类功能：</b>企业贷款放款手工记账<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2018年3月26日 <br>
 * <b>版权所有：<b>版权所有(C) 2018，拜特科技<br>
 */
@Controller
@RequestMapping("/finacial/enterpriseLoan")
public class EnterpriseLoanController extends BaseController{
	
	@Autowired
	private EnterpriseLoanService enterpriseLoanService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@DubboReference(check = false)
	private IFcBusRaceService iFcBusRaceService;
	@DubboReference(check = false)
	private IFcDayCloseService fcDayCloseService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	//@RequiresPermissions(ResourceConstant.FCL_LOAN_TALLY_APPLY)
	@GetMapping("/loanIndex")
	public String loanIndex(Model model){
		return toURL("loanIndexTab", null);
	}
	//@RequiresPermissions(ResourceConstant.FCL_LOAN_TALLY_APPLY)
	@GetMapping("/loanEdit")
	public String loanEdit(Model model){
		return toURL("loanEditTab", null);
	}
	//@RequiresPermissions(ResourceConstant.FCL_LOAN_TALLY_APPLY)
	@GetMapping("/loanCheck")
	public String loanCheck(Model model){
		return toURL("loanCheckTab", null);
	}
	
	@GetMapping("/addLoanSendBook")
	public String addLoanSendBook(Model model){
		return toURL("add", "s");
	}
	
	@GetMapping("/addLoanRepayBook")
	public String addLoanRepayBook(Model model){
		return toURL("add", "r");
	}
	
	@GetMapping("/editLoanSendBook")
	public String editLoanSendBook(Model model){
		return toURL("edit", "s");
	}
	
	@GetMapping("/editLoanRepayBook")
	public String editLoanRepayBook(Model model){
		return toURL("edit", "r");
	}
	
	@GetMapping("/checkLoanSendBook")
	public String checkLoanSendBook(Model model){
		return toURL("check", "s");
	}
	
	@GetMapping("/checkLoanRepayBook")
	public String checkLoanRepayBook(Model model){
		return toURL("check", "r");
	}
	
	/**
	* 跳转页面
	* @return
	*/
	public String toURL(String opType,String type){
		String typeurl = "";
		if("s".equals(type)){//发放
			typeurl = "LoanSendBook";
		}else if("r".equals(type)){//还款
			typeurl = "LoanRepayBook";
		}
		return "finacial/bankcollect/enterpriseLoan/" + opType + typeurl;
	}
	
	/**
	* 跳转新增放款账号
	* @return
	*/
	@GetMapping("/addLoanAcc")
	public String addLoanAcc(String sendCode,Model model){
		CmsLoanSendDTO sendDto = enterpriseLoanService.getLoanSendDtoBybillCode(sendCode);
		model.addAttribute("sendInfo", sendDto);
		CorpDTO netCorp = iCorpService.getNetByCorpId(sendDto.getCorpId());//获取单位直属网点
		 if(netCorp==null){//如果没有直属网点就是本网点
			 netCorp = new CorpDTO();
			 netCorp.setId(sendDto.getCorpId());
			 netCorp.setCode(iCorpService.get(sendDto.getCorpId()).getCode());
		 }
		 if (StringUtils.isNotEmpty(netCorp.getId())) {
			 	Date tallyDate=fcDayCloseService.getDayCloseFinalDate(netCorp.getId());
			 	if(tallyDate !=null){
			 		tallyDate = DateUtils.addDay(tallyDate, 1);
		        }
				model.addAttribute("accStartDate", DateUtils.date2Str(tallyDate,DateUtils.date_sdf()));
		}
		model.addAttribute("netInfo", netCorp);
		model.addAttribute("curInfo", iBtCurrencyService.getObjectById(sendDto.getCurCode()));
		int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
		model.addAttribute("seqNumPage", seqGeneratingMode);
		int accAccNoRule = configService.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
		model.addAttribute("accAccNoRule", accAccNoRule);
		//内部账号生成法则
		String reVal = iFcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
		int relen = reVal.length();
		model.addAttribute("relen", relen);
		return "finacial/bankcollect/enterpriseLoan/addLoanAcc";
	}
	
	/**
	* 获取记账账号
	* @return
	*/
	@PostMapping("/getTallyAccs")
	@ResponseBody
	public List<FcAccPlusDTO> getTallyAccount(String netId, String corpId, String curId) {
		return enterpriseLoanService.getTallyAccount(netId, corpId, curId, getUserId(), AccountGrantConstant.TRANSFER_ACCOUNT);
	}
	
	/**
	* 账务保存
	* @return
	*/
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook) {
		String opType = request.getParameter("opT");
		String type = request.getParameter("type");
		if(FinacialConstant.PAGE_EDIT.equals(type)&&"2".equals(opType)){//改账打回
			enterpriseLoanService.editBackAccbook(fcAccbook, getCurrentUser());
		}else{//暂存/提交
			enterpriseLoanService.saveAccbook(fcAccbook,getCurrentUser(),opType,type);
		}
		return success();
	}
	
	/**
	* 账务复核
	* @return
	*/
	@PostMapping("/check")
	@ResponseBody
	public AjaxJson check(HttpServletRequest request) {
		String opType = request.getParameter("opT");
		String busGlobalId = request.getParameter("busGlobalId");
		enterpriseLoanService.checkAccbook(opType, busGlobalId, getCurrentUser());
		return success();
	}
	
	/**
	* 获取业务种类
	* @return
	*/
	@PostMapping("/getRaceId")
	@ResponseBody
	public AjaxJson getRaceId(String netId,String type){
		AjaxJson json = new AjaxJson();
		ConstantBean<String> busRace = null;
		if(FinacialConstant.BOOLEAN_TRUE.equals(type)){
			busRace = SysRaceCode.NB_ENTERPRISE_LOAN;//放款
		}else{
			busRace = SysRaceCode.NB_ENTERPRISE_REPAYMENT;//还款
		}
		String raceId = iFcBusRaceService.getNetBusRace(busRace.get(), netId, getUserId());
		FcBusRaceDTO race = new FcBusRaceDTO();
		race.setId(raceId);
		race.setRaceName(busRace.getName());
		json.setObj(race);
		json.setSuccess(true);
		return json;
	}
	
	/**
	* 获取账务明细
	* @return
	*/
	@PostMapping("/getFcAccbookTempInfo")
	@ResponseBody
	public FcAccbookPageVO getFcAccbookTempInfo(String sendCode){
		FcAccbookPageVO entity = enterpriseLoanService.getFcAccbookTempInfo(sendCode);
		return entity;
	}
	
	
	/**
	 * 构建记账分录
	 */
	@PostMapping("/processAccbook")
	@ResponseBody
	public FcAccbookPageVO processAccbook(String key, String type){
		return enterpriseLoanService.createAccbook(key, type, getCurrentUser());
	}
	
	
}
