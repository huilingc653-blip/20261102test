package com.bytter.tally.web.controller;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.dto.FcManualFinacialColumnDTO;
import com.bytter.biz.finacial.api.dto.FcManualFinacialTypeDTO;
import com.bytter.biz.finacial.api.dto.FcVoucherCashDTO;
import com.bytter.biz.finacial.api.service.IFcVoucherCashService;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.service.FinacialManualService;
import com.bytter.tally.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller   柜台手工账Controller
 */
@Controller
@RequestMapping("/finacial/bankCollect/manualFinacial")
public class FinacialManualController extends BaseController {
	@Autowired
	private FinacialManualService finacialManualService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	@DubboReference(check = false)
	private INisBillheadService nisBillheadService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	/*@Autowired
	private FcVoucherCashService fcVoucherCashService;*/
	@DubboReference(check = false)
	private IFcVoucherCashService iFcVoucherCashService;
	//@RequiresPermissions(ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_APPLY)
	@LogMessage(content = "查询记账列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{menuType}/{type}")
	public String list(@PathVariable("menuType") String menuType, @PathVariable("type") String type, Model model) {
		boolean paySequence = false;
		if(FinacialManualConstant.MANUAL_FROM_5.equals(menuType)){//付款办理添加是否自动复核和修改下拨账号参数
			boolean isAutocheck = finacialManualService.isAutoCheck();
			paySequence = finacialManualService.paySequence();
			boolean isChangeDownAcc = finacialManualService.isChangeDownAcc();
			model.addAttribute("isAutocheck", isAutocheck);
			model.addAttribute("isChangeDownAcc", isChangeDownAcc);
		}else if("1".equals(menuType)){
			paySequence = finacialManualService.paySequenceAllot();
		}
		model.addAttribute("opType", type);
		model.addAttribute("menuType", menuType);
		model.addAttribute("paySequence", paySequence);
		return "finacial/bankcollect/manualFinacial";
	}
	@GetMapping("/manualPage")
	public String manualPage(String opType, String menuType, Model model) {
		model.addAttribute("opType", opType);
		model.addAttribute("menuType", menuType);
		return "finacial/bankcollect/" + FinacialManualConstant.AccTypeMap.get(menuType);
	}

	/**
	 * 资金调拨列表查询 单位申请付款-改账-列表查询也是这个接口
	 */
	@PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
    	return finacialManualService.getDataTable(dataTablesInput, getCurrentUser());
    }
	/**
	 * 构建记账分录
	 */
	@PostMapping("/processAccbook")
	@ResponseBody
	public FcAccbookPageVO processAccbook(String key, String type , String menuType) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		FcAccbookPageVO fcAccbookPageVO = finacialManualService.processAccbook(key, type, menuType, getCurrentUser());
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, FinacialConstant.INTEREST_START_DATE_RULE);
        if (!EmptyUtils.isEmpty(configDTO)) {
            Boolean dateRule = CmsConstant.IS_TRUE.equals(configDTO.getValue());//起息日期规则
            fcAccbookPageVO.setDateRule(dateRule);
        }
		FcVoucherCashVo fcVoucherCashVo = null;
		for (FcAccbookDtlVO bookDtl : fcAccbookPageVO.getBookDtls()) {
			if (StringUtils.isNotEmpty(bookDtl.getSubjectId())) {
				if (bookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0 || bookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) < 0) {
//					fcVoucherCashVo = fcVoucherCashService.getItemBySubject(bookDtl.getSubjectId(), null);
					FcVoucherCashDTO fcVoucherCashDTO = iFcVoucherCashService.getItemBySubject(bookDtl.getSubjectId(), null);
					if (null != fcVoucherCashDTO) {
						bookDtl.setCashFluxName(fcVoucherCashDTO.getDebitItemName());
						bookDtl.setCashFlux(fcVoucherCashDTO.getDebitItem());
					}
				} else {
//					fcVoucherCashVo = fcVoucherCashService.getItemBySubject(null, bookDtl.getSubjectId());
					FcVoucherCashDTO fcVoucherCashDTO = iFcVoucherCashService.getItemBySubject(null, bookDtl.getSubjectId());
					if (null != fcVoucherCashDTO) {
						bookDtl.setCashFluxName(fcVoucherCashDTO.getCreditItemName());
						bookDtl.setCashFlux(fcVoucherCashDTO.getCreditItem());
					}
				}
			} else if (StringUtils.isNotEmpty(bookDtl.getAccount())) {
				FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(bookDtl.getAccount());
				if (null != fcAccDTO) {
					if (bookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0 || bookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) < 0) {
//						fcVoucherCashVo = fcVoucherCashService.getItemBySubject(fcAccDTO.getSubjectId(), null);
						FcVoucherCashDTO fcVoucherCashDTO = iFcVoucherCashService.getItemBySubject(fcAccDTO.getSubjectId(), null);
						if (null != fcVoucherCashDTO) {
							bookDtl.setCashFluxName(fcVoucherCashDTO.getDebitItemName());
							bookDtl.setCashFlux(fcVoucherCashDTO.getDebitItem());
						}
					} else {
//						fcVoucherCashVo = fcVoucherCashService.getItemBySubject(null, fcAccDTO.getSubjectId());
						FcVoucherCashDTO fcVoucherCashDTO = iFcVoucherCashService.getItemBySubject(null, fcAccDTO.getSubjectId());
						if (null != fcVoucherCashDTO) {
							bookDtl.setCashFluxName(fcVoucherCashDTO.getCreditItemName());
							bookDtl.setCashFlux(fcVoucherCashDTO.getCreditItem());
						}
					}
				}
			}
		}
		return fcAccbookPageVO;
	}
	/*************手工帐数据回写规则维护************/
	/**
	 * 手工账 回写规则 列表
	 * @return
	 */
	@GetMapping("/manualFinacialRuleList")
	public String manualFinacialRuleList(Model model){
		return "finacial/bankcollect/rule/list";
	}
	@PostMapping("/manualFinacialDatatable")
    @ResponseBody
    public DataTablesOutput manualFinacialDatatable(@RequestBody  DataTablesInput dataTablesInput){
    	return finacialManualService.getTypeDataTables(dataTablesInput, getCurrentUser());
    }
	/**
	 * 编辑手工帐规则 页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	public String eidt(String id, Model model){
		if(EmptyUtils.isNotEmpty(id)){
			FcManualFinacialTypeDTO type = finacialManualService.getType(id);
			model.addAttribute("finacialType", type);
		}
		return "finacial/bankcollect/rule/edit";
	}
	/**
	 * 复制手工帐规则 页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/copy")
	public String copy(String id, Model model){
		if(EmptyUtils.isNotEmpty(id)){
			FcManualFinacialTypeDTO type = finacialManualService.getType(id);
			model.addAttribute("finacialType", type);
			model.addAttribute("opType", "copy");
		}
		return "finacial/bankcollect/rule/edit";
	}
	@PostMapping("/saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(FcManualFinacialTypeDTO type){
		finacialManualService.saveType(type);
		return success();
	}
	@RequestMapping("/delete")
	@ResponseBody
	public AjaxJson delete(String id){
		finacialManualService.deleteType(id);
		return success();
	}
	@RequestMapping("/view")
	public String view(String id, Model model){
		return "";
	}
	@RequestMapping("/getColumnByTypeId")
	@ResponseBody
	public List<FcManualFinacialColumnDTO> getColumnByTypeId(String id, String flowType){
		return finacialManualService.getColumnByTypeId(id);
	}
	/**
	 * 记账前对业务数据进行校验
	 * @param request
	 * @param menuType
	 * @param key
	 */
	private void validBefore(HttpServletRequest request, String menuType, String key) {
		String pageType = request.getParameter("pageType");//页面类型
		finacialManualService.validBefore(pageType, menuType, key);
	}
	/**
	 * 提交FC_ACCBOOK
	 */
	@LogMessage(content = "提交FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(HttpServletRequest request,FcAccbookVoucherDTO fcAccbook, String menuType, String key, String downAccId, String opType) {
		validBefore(request, menuType, key);
		finacialManualService.submit(fcAccbook, key, getCurrentUser(), downAccId, menuType, opType);
		return success();
	}
	@LogMessage(content = "暂存FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key, String menuType) {
		validBefore(request, menuType, key);
		finacialManualService.save(fcAccbook, key, getCurrentUser());
		return success();
	}

	/**
	 * 资金调拨 复核
	 * @param request
	 * @param fcAccbook
	 * @param key
	 * @param downAccId
	 * @param menuType
	 * @return
	 */
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
   	@PostMapping("/pass")
   	@ResponseBody
   	public AjaxJson pass(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook,String key, String downAccId, String menuType) {
		validBefore(request, menuType, key);
		finacialManualService.pass(fcAccbook, key, getCurrentUser(), downAccId, menuType);
   		return success();
   	}
    /**
     * 转付修改下拨账号复核
     * @param request
     * @param orderNo
     * @param downAccId
     * @param billNo
     * @param menuType
     * @return
     */
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
   	@PostMapping("/payPass")
   	@ResponseBody
   	public AjaxJson payPass(HttpServletRequest request,String orderNo, String downAccId, String billNo, String menuType) {
		finacialManualService.validBefore(FinacialManualConstant.MANUAL_STATUS_CHECK, menuType, billNo);
    	finacialManualService.payPass(orderNo, downAccId, billNo,getCurrentUser(), menuType);
   		return success();
   	}
    @LogMessage(content = "打回FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key,String menuType) {
		validBefore(request, menuType, key);
    	finacialManualService.back(fcAccbook, key, getCurrentUser(), menuType);
		return success();
	}
    @GetMapping("/getVoucherType")
    @ResponseBody
    public List<FcVoucherTypeDTO> json(){
        return fcVoucherTypeService.getFcVoucherTypeAll();
    }
    /**
	 * 查询有权限的内部帐号
	 */
	@PostMapping("/getFcAcc")
	@ResponseBody
	public List<FcAccDTO> getFcAcc(HttpServletRequest request, String menuType, String key) {
		return finacialManualService.getFcAcc(menuType, key, getCurrentUser());
	}
	/**
	 * 查询有权限的内部帐号（转付、代付、下拨、请款借方账户获取单位对应的内部账户）
	 */
	@PostMapping("/getFcAccDebit")
	@ResponseBody
	public List<FcAccDTO> getFcAccDebit(HttpServletRequest request, String menuType, String key) {
		return finacialManualService.getFcAccDebit(menuType, key, getCurrentUser());
	}
	/**
	 * 获取资金下拨账号（企业活期对应的银行账号）
	 *
	 * @param debitAcc
	 * @param creditAcc
	 * @param curId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getDownAcc")
	@ResponseBody
	public List<BtBankAccDTO> getDownAcc(String debitAcc, String creditAcc, String curId) {
		List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
		if (!StringUtils.isEmpty(curId) && !StringUtils.isEmpty(debitAcc)) {
			List<BtBankAccDTO> parentBanks = btBankAccService.getBankAccByCountAcc(creditAcc, curId);
			if (!EmptyUtils.isEmptyList(parentBanks)) {
				BtBankAccDTO parentBank = parentBanks.get(0);//父账号只有一个
				//获取具有转账权限的银行账号
				List<BtBankAccDTO> listBankAcc = btBankAccService.getOutPayByParentAcc(parentBank.getId(), curId, debitAcc);
				return listBankAcc;
			}
		}
		return listAcc;
	}
	@RequestMapping("/changeDownAcc")
	public String changeDownAcc(String orderNo, String opType, String curId, String account, String key, String menuType, String creditAcc,Model model){
		model.addAttribute("curId", curId);
		model.addAttribute("account", account);//借方账号付款内部账号
		model.addAttribute("creditAcc", creditAcc);//贷方账号
		model.addAttribute("menuType", menuType);//
		if(!EmptyUtils.isEmpty(orderNo)){
			model.addAttribute("orderNo", orderNo);
		}
		if(!EmptyUtils.isEmpty(opType)){
			model.addAttribute("opType", opType);
		}
		if(!EmptyUtils.isEmpty(key)){
			NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadByBillNo(key);
			if(EmptyUtils.isNotEmpty(nisBillheadDTO.getDownAccId())){
				if (!StringUtils.isEmpty(curId) && !StringUtils.isEmpty(account)) {
					List<BtBankAccDTO> parentBanks = btBankAccService.getBankAccByCountAcc(creditAcc, curId);
					if (!EmptyUtils.isEmptyList(parentBanks)) {
						BtBankAccDTO parentBank = parentBanks.get(0);//父账号只有一个
						//获取具有转账权限的银行账号
						List<BtBankAccDTO> listBankAcc = btBankAccService.getOutPayByParentAcc(parentBank.getId(), curId, account);
						if (!EmptyUtils.isEmptyList(listBankAcc)) {
							for (BtBankAccDTO accDTO : listBankAcc) {
							    if (accDTO.getId().equals(nisBillheadDTO.getDownAccId())) {//默认为原下拨账号
                                    model.addAttribute("downAcc", accDTO.getBankAcc());
                                    model.addAttribute("downAccId", accDTO.getId());
                                    break;
                                }
							}
						}
					}
				}
			}
			model.addAttribute("billNo", key);
		}
		return "finacial/bankcollect/nisbillheadmanual/changeDownAcc";
	}
	/**
	 * 判断是否转付
	 */
	@PostMapping("/chenckVoucherType")
	@ResponseBody
	public boolean chenckVoucherType(String voucherType){
		return finacialManualService.chenckVoucherType(voucherType);
	}
	/**
	 * 判断是否内转
	 */
	@PostMapping("/chenckVoucherTypeInner")
	@ResponseBody
	public boolean chenckVoucherTypeInner(String voucherType){
		return finacialManualService.chenckVoucherTypeInner(voucherType);
	}
    /**
     * 批量提交凭证
     * @param
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "批量提交凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(HttpServletRequest request, String billNos, FcAccbookVO accbookVO, String account, String menuType, String opType) {
        for(String billNo : billNos.split(CommonConstant.COMMA_SPLIT)) {
            //submitFcAccbook(id);
        }
        return success();
    }
    /**
     * 批量提交凭证
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "批量复核凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchPass")
    @ResponseBody
    public AjaxJson batchPass(@RequestParam("ids")String ids) {
        for(String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //checkFcAccbook(id);
        }
        return success();
    }
    /**
     * 批量提交凭证
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "批量打回凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchBack")
    @ResponseBody
    public AjaxJson batchBack(@RequestParam("ids")String ids, @RequestParam("backReason")String backReason) {
        for(String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //backFcAccbook(id, backReason);
        }
        return success();
    }
	/**
	 * 批量记账
	 *
	 * @return
	 */
	@LogMessage(content = "批量记账", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/batchTally/{billNo}/{type}/{key}/{menuType}")
	public String batchTally(@PathVariable("billNo")String billNo, @PathVariable("type")String type,
							 @PathVariable("key")String key, @PathVariable("menuType")String menuType, Model model) {
		model.addAttribute("billNo", billNo);
		model.addAttribute("type", type);
		model.addAttribute("key", key);
		model.addAttribute("menuType", menuType);
		NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadByBillNo(key);
		model.addAttribute("curCode", nisBillheadDTO.getCurId());
		model.addAttribute("netId", nisBillheadDTO.getCorpId());
		model.addAttribute("payerAcc", nisBillheadDTO.getPayerAcc());
		String showAccount = CommonConstant.YES;
		//先付款后记账 或者 系统结算方式为内转时，不需要填写贷方账号
		String payAccount = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_PAYACCOUNT).getValue();
		if(NetbankConstant.ACCOUNTORDER_PAYACCOUNT.equals(payAccount) ||
				NetbankConstant.PAY_TYPE_05.equals(nisBillheadDTO.getSysVoucherCode())){
			showAccount = CommonConstant.NO;
		}
		model.addAttribute("showAccount", showAccount);
		//记账凭证起息日期是否为记账日期
		String interestStartDateRule = configService.getConfig(ConfigConstant.FCL, FinacialConstant.INTEREST_START_DATE_RULE).getValue();
		model.addAttribute("interestStartDateRule", interestStartDateRule);
		return "finacial/bankcollect/nisbillheadmanual/batchTally";
	}
}
