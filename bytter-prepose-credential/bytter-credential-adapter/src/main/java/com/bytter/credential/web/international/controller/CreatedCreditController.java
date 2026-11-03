package com.bytter.credential.web.international.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.InternationalConstant;
import com.bytter.credential.api.constant.LcConfigConstant;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.service.CreatedCreditService;
import com.bytter.credential.domain.international.utils.CreditUtil;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.BtPurposeDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-11-30 15:29
 * @Version 1.0
 */
@Controller
@RequestMapping("/international/credit/createCredit")
public class CreatedCreditController extends BaseController {
    @Autowired
    private CreatedCreditService createdCreditService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
	private IConfigService configService;
    @Autowired(required = false)
    public IBtBankService bankService;
    @DubboReference(check = false)
	private IBtPurposeService purposeService;
    @DubboReference(check = false)
	private IRatUrationService urationService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService excrateAdjustService;
    @DubboReference(check = false)
    private IFcBondHookService bondHookService;
    @DubboReference(check = false)
	private IGuaWayServie guaWayServie;
    @DubboReference(check = false)
	private IGuaContractOccupServie guaContractOccupServie;
    /**
     * 新增跳转
     * @param model
     * @return
     */
    @LogMessage(content = "信用证开证跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addCredit")
    public String addCredit(@RequestParam String lcInter, Model model) {
        String url = null;
        if ("0".equals(lcInter)) {
            url = "international/credit/createCredit/addCredit";
        } else {
            url = "international/credit/createCredit/addInternationalCredit";
        }
        Map<String, Object> configMap = configService.listByType(LcConfigConstant.LC_CONFIG_TYPE);
        model.addAttribute("config", configMap);
        model.addAttribute("isGuarantee", ((Map)configMap.get("international")).get("isHangGua").toString());
        model.addAttribute("isHangGuarantee", "0");
        model.addAttribute("ratBankConfig", urationService.getRatBankCtlConfig());
        //初始化参数
        this.initConfig(model);
        return url;
    }
    /**
     * 初始化系统参数
     *
     * @param model
     */
    private void initConfig(Model model) {
        model.addAttribute("budgetSel", configService.getConfig(LcConfigConstant.LC_CONFIG_TYPE, LcConfigConstant.LC_CONFIG_CREDIT_BUDGETSEL).getValue());
        model.addAttribute("payItem", configService.getConfig(LcConfigConstant.LC_CONFIG_TYPE, LcConfigConstant.LC_CONFIG_CREDIT_PAYITEM).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_CREDIT);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL);
    }
    /**
     * 信用证开证列列表跳转
     * @param model
     * @return
     */
    @LogMessage(content = "信用证开证列列表跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCredit")
    public String listCredit(Model model) {
        return "international/credit/createCredit/listCredit";
    }
    /**
     * 列表显示
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "信用证开证列表显示", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryCredit")
    @ResponseBody
    public DataTablesOutput listCredit(@RequestBody DataTablesInput dataTablesInput) {
        return createdCreditService.listCredit(dataTablesInput, getUserId());
    }
    /**
     * 保存开证申请数据
     * @param model
     * @param lcEnrol
     * @return
     */
    @LogMessage(content = "信用证开证保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveCredit")
    @ResponseBody
    public AjaxJson saveCredit(Model model, LcEnrolVO lcEnrol, String guaContract) {
        ICurrentUser user = getCurrentUser();
        /*设置为11提交审批*/
        lcEnrol.setStatus(InternationalConstant.COMMITAPPROVE);
        createdCreditService.saveCredit(lcEnrol, user, guaContract);
        return success();
    }
    /**
     * 暂存开证申请数据
     * @param model
     * @param lcEnrol
     * @return
     */
    @LogMessage(content = "信用证开证暂存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveTemp")
    @ResponseBody
    public AjaxJson saveTempCredit(Model model, LcEnrolVO lcEnrol, String guaContract) {
        ICurrentUser user = getCurrentUser();
        lcEnrol.setStatus(InternationalConstant.TEMP);
        createdCreditService.saveCreditTmp(lcEnrol, user, guaContract);
        return success();
    }
    /**
     * 删除单据
     * @param id 主键
     * @return
     */
    @LogMessage(content = "信用证开证删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/deleteCredit")
    @ResponseBody
    public AjaxJson deleteCredit(@RequestParam String id) {
        ICurrentUser user = getCurrentUser();
        createdCreditService.deleteCredit(id, user);
        return success();
    }
    /**
     * 编辑跳转
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "信用证编辑跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editCredit")
    public String editCredit(@RequestParam String id, Model model) {
        LcEnrol lcEnrol = createdCreditService.getById(id);
        model.addAttribute("id", id);
        if (InternationalConstant.ISHANGCREDIT_N.equals(lcEnrol.getIsHangCredit())) {
            model.addAttribute("applicantAccShow", btBankAccService.getBankAccInfo(lcEnrol.getApplicantAcc()).getBankName());
        }
        Map<String,Object> configMap=configService.listByType(LcConfigConstant.LC_CONFIG_TYPE);
        model.addAttribute("config", configMap);
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("corp", corpService.get(lcEnrol.getCorpId()));
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.LCS,lcEnrol.getId());
            model.addAttribute("ratUration", ratUrationDTO);
            String RatBankCtl = urationService.getRatBankCtlConfig();
            model.addAttribute("ratBankConfig", RatBankCtl);
            if (CommonConstant.YES.equals(RatBankCtl)) {
                model.addAttribute("applicantAccShow", btBankAccService.getBankAccInfo(lcEnrol.getApplicantAcc()).getBankName());
            }
        }
        if (FcBondConstant.IS_HOOK_Y.equals(lcEnrol.getIsHangBond())) {
//				存在挂接关系
            FcBondApplyDTO dto = bondHookService.getFcBondApplyByItemIds(lcEnrol.getId(),lcEnrol.getLcNo());
            model.addAttribute("dto", dto);
        }
        String url = null;
//        担保挂接参数设置
        CreditUtil.setGuaParams(model, lcEnrol, guaWayServie, ((Map)configMap.get("international")).get("isHangGua").toString(),"0",guaContractOccupServie);
        if ("0".equals(lcEnrol.getLcInter())) {
            url = "international/credit/createCredit/editCredit";
        } else {
            url = "international/credit/createCredit/editInternationalCredit";
        }
        //初始化参数
        this.initConfig(model);
        return url;
    }
    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    @LogMessage(content = "信用根据id查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/getCreditById")
    @ResponseBody
    public LcEnrol getCreditById(@RequestParam String id, Model model) {
        LcEnrol lcEnrol = createdCreditService.getById(id);
        return lcEnrol;
    }
    /**
     * 跳转到审批列表页面
     * @param model
     * @return
     */
    @LogMessage(content = "信用证开证列列表跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCreditApprove")
    public String listCreditApprove(@RequestParam String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "international/credit/createCredit/approve/listCreditApprove";
    }
    /**
     * 审批列表显示
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "信用证开证审批列表显示", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryCreditApprove")
    @ResponseBody
    public DataTablesOutput listCreditApprove(@RequestBody DataTablesInput dataTablesInput) {
        return createdCreditService.listCreditApprove(dataTablesInput, getUserId());
    }
    /**
     * 查看页面
     * @param showCloseBtn 是否显示关闭按钮
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "信用证查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewCredit")
    public String viewCredit(@RequestParam(name = "showCloseBtn",required = false,defaultValue = "0") String showCloseBtn,
                             @RequestParam String id, Model model) {
        LcEnrol lcEnrol = createdCreditService.getById(id);
        LcEnrolVO lcEnrolVO = CreditUtil.setParams(lcEnrol, btBankAccService, urationService, currencyService, model);
        createdCreditService.injectValue(lcEnrolVO);
        model.addAttribute("lcEnrol", lcEnrolVO);
        Map<String,Object> configMap=configService.listByType(LcConfigConstant.LC_CONFIG_TYPE);
        model.addAttribute("config", configMap);
//        设置被开证单位
        CreditUtil.setReCorpName(lcEnrol, corpService, model);
        String url = null;
//        是否显示关闭按钮
        model.addAttribute("showCloseBtn", showCloseBtn);
        model.addAttribute("nativeCur", corpService.get(lcEnrol.getCorpId()));
        //        担保挂接参数设置
        CreditUtil.setGuaParams(model, lcEnrol, guaWayServie, ((Map)configMap.get("international")).get("isHangGua").toString(),"1",guaContractOccupServie);
        if ("0".equals(lcEnrol.getLcInter())) {
            url = "international/credit/createCredit/viewCredit";
        } else {
            url = "international/credit/createCredit/viewInternationalCredit";
        }
        this.initConfig(model);
        return url;
    }
    /**
     * 审批页面
     * @param id 主键id
     * @param taskId
     * @param model
     * @return
     */
    @LogMessage(content = "信用证审批页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewCreditApprove")
    public String viewCreditApprove(@RequestParam String id, @RequestParam String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        this.initConfig(model);
        return "international/credit/createCredit/approve/viewCreditApprove";
    }
    /**
     * 根据用户获取账号信息
     * @param corpId 单位id
     * @param bankCode 联行号
     * @param model
     * @return
     */
    @LogMessage(content = "信用证审批页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/getBankAccsByCorpId")
    @ResponseBody
    public List<BtBankAccDTO> getBankAccsByCorpId(
            @RequestParam(value = "corpId", required = false) String corpId,
            @RequestParam(value = "bankCode", required = false) String bankCode,
            Model model) {
        return createdCreditService.getBankAccsByCorpId(corpId,bankCode);
    }
    /**
     * 信用证报表查询
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "信用证报表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryReportCredits")
    @ResponseBody
    public DataTablesOutput listReportCredits(@RequestBody DataTablesInput dataTablesInput) {
        ICurrentUser user = getCurrentUser();
        return createdCreditService.listReportCredits(dataTablesInput, user);
    }
    /**
     * 信用证报表查询页面跳转
     * @param model
     * @return
     */
    @LogMessage(content = "信用证报表查询页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listReportCredits")
    public String listReportCredits(Model model) {
        return "international/report/listReportCredit";
    }
    /**
     * 通过联行号获取银行信息
     * @param bankCode 联行号
     * @param model
     * @return
     */
    @LogMessage(content = "通过联行号获取银行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getBankByBankCode")
    @ResponseBody
    public BtBankDTO getBankByBankCode(@RequestParam String bankCode,Model model) {
        return bankService.getByBankCode(bankCode);
    }
    /**
     * 获取该单位下所有用途数据
     * @param corpId
     * @param model
     * @return
     */
    @LogMessage(content = "获取该单位下所有用途数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/getPurpose")
    @ResponseBody
    public List<BtPurposeDTO> getBtPurposeDTO(@RequestParam(required = false) String corpId, Model model) {
        List<BtPurposeDTO> purposeDTOS = purposeService.listByCorpIdOrBusType(corpId, InternationalConstant.PURPOSE);
        if (null == purposeDTOS || purposeDTOS.size() == 0) {
            purposeDTOS = new ArrayList<>();
        }
        return purposeDTOS;
    }
    /**
    *获取币别最新有效资料
    *@Param origCurId
    *@Param convertCurId
    *@return
    */
    @LogMessage(content = "获取币别最新有效资料", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getExchangeRate")
    @ResponseBody
    public AjaxJson getCurrencyById(String origCurId,String convertCurId,Date preDate) {
        if (null == preDate) {
            preDate = new Date();
        }
        BigDecimal exchangeRate = excrateAdjustService.getExchangeRateBy(origCurId, convertCurId, DateUtils.setSpecifiedDateOfTime(preDate), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setObj(exchangeRate);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
    /**
     * 判断是否本位币
     * @param curId
     * @return
     */
    @LogMessage(content = "判断是否本位币", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/isNativeCur")
    @ResponseBody
    public Map<String, Object> getIsNativeCur(String curId) {
        Map<String, Object> returnMap = new HashMap<>();
        BtCurrencyDTO nativeCur = currencyService.getNatualCurrency();
        if (curId.equals(nativeCur.getId())) {
            returnMap.put("isNativeCur", true);
        } else {
            /*非本币位，则返回本币位数据*/
            returnMap.put("isNativeCur", false);
            returnMap.put("nativeCur", nativeCur);
        }
        return returnMap;
    }
    /**
     * 手机审批开证详情显示
     * @param showCloseBtn 是否显示关闭按钮
     * @param id 主键id
     * @param model
     * @return
     */
    @LogMessage(content = "手机审批开证详情显示", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileViewCredit")
    public String MobileViewCredit(@RequestParam(name = "showCloseBtn",required = false,defaultValue = "0") String showCloseBtn,
                             @RequestParam String id, Model model,@RequestParam String businessId) {
        LcEnrol lcEnrol = createdCreditService.getById(businessId);
        CreditUtil.setParams(lcEnrol, btBankAccService, urationService, currencyService, model);
        Map<String,Object> configMap=configService.listByType(LcConfigConstant.LC_CONFIG_TYPE);
        model.addAttribute("config", configMap);
//        设置被开证单位
        CreditUtil.setReCorpName(lcEnrol, corpService, model);
        String url = null;
//        是否显示关闭按钮
        model.addAttribute("showCloseBtn", showCloseBtn);
        model.addAttribute("nativeCur", corpService.get(lcEnrol.getCorpId()));
        model.addAttribute("creditMoneyUpper", StringUtils.chineseMoney(lcEnrol.getCreditMoney()));
        //        担保挂接参数设置
        CreditUtil.setGuaParams(model, lcEnrol, guaWayServie, ((Map)configMap.get("international")).get("isHangGua").toString(),"1",guaContractOccupServie);
        if ("0".equals(lcEnrol.getLcInter())) {
            url = "international/credit/createCredit/approve/mobileViewCredit";
        } else {
            url = "international/credit/createCredit/approve/mobileViewInternationalCredit";
        }
        return url;
    }
    /**
    *查询没有挂接担保的数据
    *@Param input
    *@return
    */
    @PostMapping("/listLcNotGua")
    @ResponseBody
    public DataTablesOutput listLcNotGua(@RequestBody DataTablesInput input) {
        return createdCreditService.listLcNotGua(input);
    }

}
