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
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.InternationalConstant;
import com.bytter.credential.api.constant.LcConfigConstant;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.service.SupplementCreditService;
import com.bytter.credential.domain.international.utils.CreditUtil;
import com.bytter.credential.domain.international.vo.GuaContractInternationalVO;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-10 18:32
 * @Version 1.0
 */
@Controller
@RequestMapping("/international/credit/supplementCredit")
public class SupplementCreditController extends BaseController {
    @Autowired
    private SupplementCreditService supplementCreditService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private IFcBondHookService bondHookService;
    @DubboReference(check = false)
    private IGuaWayServie guaWayServie;
    @DubboReference(check = false)
    private IGuaContractOccupServie guaContractOccupServie;
    /**
     * 编辑跳转
     *
     * @return
     */
    @LogMessage(content = "信用证补录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editSupplementCredit")
    public String editSupplementCredit(@RequestParam String id, Model model) {
        LcEnrol lcEnrol = supplementCreditService.get(id);
        model.addAttribute("id", id);
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(lcEnrol.getApplicantAcc());
        if (InternationalConstant.ISHANGCREDIT_N.equals(lcEnrol.getIsHangCredit())) {
            if (bankAccInfo != null) {
                model.addAttribute("applicantAccShow", bankAccInfo.getBankName());
            }
        }
        model.addAttribute("lcEnrol", lcEnrol);
        model.addAttribute("corp", corpService.get(lcEnrol.getCorpId()));
        model.addAttribute("ratBankConfig", urationService.getRatBankCtlConfig());
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.LCS, lcEnrol.getId());
            model.addAttribute("ratUration", ratUrationDTO);
            String RatBankCtl = urationService.getRatBankCtlConfig();
            model.addAttribute("ratBankConfig", RatBankCtl);
            if (CommonConstant.YES.equals(RatBankCtl)) {
                model.addAttribute("applicantAccShow", btBankAccService.getBankAccInfo(lcEnrol.getApplicantAcc()).getBankName());
            }
        }
        Map<String, Object> configMap = configService.listByType(LcConfigConstant.LC_CONFIG_TYPE);
        model.addAttribute("config", configMap);
        if (FcBondConstant.IS_HOOK_Y.equals(lcEnrol.getIsHangBond())) {
//				存在挂接关系
            FcBondApplyDTO dto = bondHookService.getFcBondApplyByItemIds(lcEnrol.getId(), lcEnrol.getLcNo());
            model.addAttribute("dto", dto);
        }
        String url = null;
        //        担保挂接参数设置
        CreditUtil.setGuaParams(model, lcEnrol, guaWayServie, ((Map) configMap.get("international")).get("isHangGua").toString(), "0", guaContractOccupServie);
        if ("0".equals(lcEnrol.getLcInter())) {
            url = "international/credit/createCredit/editSupplementCredit";
        } else {
            url = "international/credit/createCredit/editSupplementInternationalCredit";
        }
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
     * 补录列表
     *
     * @return
     */
    @LogMessage(content = "信用证待补录列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listSupplementCredit")
    public String listSupplementCredit() {
        return "international/credit/createCredit/listSupplementCredit";
    }
    /**
     * 补录列表查询
     *
     * @param dataTablesInput
     * @param model
     * @return
     */
    @LogMessage(content = "信用证待补录列表数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/querySupplementCredit")
    @ResponseBody
    public DataTablesOutput listSupplementCredit(@RequestBody DataTablesInput dataTablesInput, Model model) {
        return supplementCreditService.listSupplmentCredit(dataTablesInput, getUserId());
    }
    /**
     * 补录列表
     *
     * @return
     */
    @LogMessage(content = "信用证补录审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listSupplementCreditApprove")
    public String listSupplementCreditApprove(@RequestParam String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "international/credit/createCredit/approve/listSupplementCreditApprove";
    }
    /**
     * 补录审批列表查询
     *
     * @param dataTablesInput
     * @param model
     * @return
     */
    @LogMessage(content = "信用证待补录列表数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/querySupplementCreditApprove")
    @ResponseBody
    public DataTablesOutput listSupplementCreditApprove(@RequestBody DataTablesInput dataTablesInput, Model model) {
        return supplementCreditService.listSupplmentCreditApprove(dataTablesInput);
    }
    /**
     * 补录暂存
     *
     * @param lcEnrol
     * @return
     */
    @LogMessage(content = "信用证待补暂存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveTmp")
    @ResponseBody
    public AjaxJson saveTmp(LcEnrolVO lcEnrol, String guaContract) {
        ICurrentUser user = getCurrentUser();
        supplementCreditService.saveTmp(lcEnrol, user, guaContract);
        return success();
    }
    /**
     * 补录提交审批
     *
     * @param lcEnrol
     * @return
     */
    @LogMessage(content = "信用证待补提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveSpplementCredit")
    @ResponseBody
    public AjaxJson save(LcEnrolVO lcEnrol, String guaContract) {
        ICurrentUser user = getCurrentUser();
        supplementCreditService.save(lcEnrol, user, guaContract);
        return success();
    }
    /**
     * 信用证补录审批详情
     *
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @LogMessage(content = "信用证补录审批详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewSupplementCreditApprove")
    public String viewSupplementCredit(@RequestParam String id, @RequestParam String taskId, Model model) {
        LcEnrol lcEnrol = supplementCreditService.get(id);
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        this.initConfig(model);
        return "international/credit/createCredit/approve/viewSupplementCreditApprove";
    }
    /**
     * 信用证补录审批详情
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "信用证补录审批详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewSupplementCredit")
    public String viewSupplementCreditApprove(@RequestParam String id, Model model) {
        LcEnrol lcEnrol = supplementCreditService.get(id);
        CreditUtil.setParams(lcEnrol, btBankAccService, urationService, currencyService, model);
        Map<String, Object> configMap = configService.listByType(LcConfigConstant.LC_CONFIG_TYPE);
        model.addAttribute("config", configMap);
//        设置被开证单位
        CreditUtil.setReCorpName(lcEnrol, corpService, model);
        String url = null;
        model.addAttribute("nativeCur", corpService.get(lcEnrol.getCorpId()));
        //        担保挂接参数设置
        CreditUtil.setGuaParams(model, lcEnrol, guaWayServie, ((Map) configMap.get("international")).get("isHangGua").toString(), "1", guaContractOccupServie);
        if ("0".equals(lcEnrol.getLcInter())) {
            url = "international/credit/createCredit/viewSupplementCredit";
        } else {
            url = "international/credit/createCredit/viewSupplementInternationalCredit";
        }
        this.initConfig(model);
        return url;
    }
    /**
     * 入库编辑
     *
     * @return
     */
    @LogMessage(content = "信用证入库编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editStorageCredit")
    public String editStorageCredit(@RequestParam String id, Model model) {
        LcEnrol lcEnrol = supplementCreditService.get(id);
        model.addAttribute("applicantAccShow", btBankAccService.getBankAccInfo(lcEnrol.getApplicantAcc()).getBankName());
        model.addAttribute("corpName", corpService.get(lcEnrol.getApplicant()).getName());
        model.addAttribute("curName", currencyService.getObjectById(lcEnrol.getIssuingCurId()).getCurName());
        if (EmptyUtils.isNotEmpty(lcEnrol.getBondCurId()) && !("".equals(lcEnrol.getBondCurId().trim()))) {
            model.addAttribute("bondCurName", currencyService.getObjectById(lcEnrol.getBondCurId()).getCurName());
        }
        model.addAttribute("lcEnrol", lcEnrol);
        Map<String, Object> configMap = configService.listByType(LcConfigConstant.LC_CONFIG_TYPE);
        model.addAttribute("config", configMap);
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.LCS, lcEnrol.getId());
            model.addAttribute("ratUration", ratUrationDTO);
            String RatBankCtl = urationService.getRatBankCtlConfig();
            model.addAttribute("ratBankConfig", RatBankCtl);
            if (CommonConstant.YES.equals(RatBankCtl)) {
                model.addAttribute("applicantAccShow", btBankAccService.getBankAccInfo(lcEnrol.getApplicantAcc()).getBankName());
            }
        }
//        设置被开证单位
        CreditUtil.setReCorpName(lcEnrol, corpService, model);
        String url = null;
        model.addAttribute("nativeCur", corpService.get(lcEnrol.getCorpId()));
        //        担保挂接参数设置
        CreditUtil.setGuaParams(model, lcEnrol, guaWayServie, ((Map) configMap.get("international")).get("isHangGua").toString(), "1", guaContractOccupServie);
        if ("0".equals(lcEnrol.getLcInter())) {
            url = "international/credit/createCredit/editStorageCredit";
        } else {
            url = "international/credit/createCredit/editStorageInternationalCredit";
        }
        return url;
    }
    /**
     * 入库操作
     *
     * @param lcEnrol
     * @return
     */
    @LogMessage(content = "信用证入库编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/storageCredit")
    @ResponseBody
    public AjaxJson storageCredit(LcEnrol lcEnrol) {
        ICurrentUser user = getCurrentUser();
        supplementCreditService.storageCredit(lcEnrol, user);
        return success();
    }
    /**
     * 打回操作
     *
     * @param id
     * @param refuseReason
     * @param model
     * @return
     */
    @LogMessage(content = "信用证打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/refuse")
    @ResponseBody
    public AjaxJson refuse(@RequestParam String id, @RequestParam String refuseReason, Model model) {
        ICurrentUser user = getCurrentUser();
        supplementCreditService.refuse(id, refuseReason, user);
        return success();
    }
    /**
     * 信用证补录手机审批详情
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "信用证补录手机审批详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileViewSupplementCredit")
    public String MobileViewSupplementCreditApprove(@RequestParam String id, @RequestParam String businessId, Model model) {
        LcEnrol lcEnrol = supplementCreditService.get(businessId);
        CreditUtil.setParams(lcEnrol, btBankAccService, urationService, currencyService, model);
        Map<String, Object> configMap = configService.listByType(LcConfigConstant.LC_CONFIG_TYPE);
        model.addAttribute("config", configMap);
//        设置被开证单位
        CreditUtil.setReCorpName(lcEnrol, corpService, model);
        String url = null;
        model.addAttribute("nativeCur", corpService.get(lcEnrol.getCorpId()));
        model.addAttribute("creditMoneyUpper", StringUtils.chineseMoney(lcEnrol.getCreditMoney()));
        //        担保挂接参数设置
        CreditUtil.setGuaParams(model, lcEnrol, guaWayServie, ((Map) configMap.get("international")).get("isHangGua").toString(), "1", guaContractOccupServie);
        if ("0".equals(lcEnrol.getLcInter())) {
            url = "international/credit/createCredit/approve/mobileViewSupplementCredit";
        } else {
            url = "international/credit/createCredit/approve/mobileViewSupplementInternationalCredit";
        }
        return url;
    }
    /**
     * 获取国际结算挂接合同数据
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ResponseBody
    @PostMapping("/getGuaContract")
    public DataTablesOutput<GuaContractInternationalVO> getUseChangelist(@RequestBody DataTablesInput dataTablesInput) {
        return supplementCreditService.getGuaForInternational(dataTablesInput, getCurrentUser());
    }
}
