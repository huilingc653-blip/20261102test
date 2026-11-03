package com.bytter.finance.web.provide.controller;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.provide.entity.ProvideLoanDeferred;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.service.CmsProvideLoanRepayService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.service.ProvideLoanDeferredService;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.finance.domain.provide.vo.ProvideLoanDeferredVO;
import com.bytter.system.api.IConfigService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author zhengxn
 * @version 1.0
 * @Title: 借款展期申请
 * @Description: CMS_PROVIDE_LOAN_SEND
 * @date 2017-07-24 14:13:13
 */
@Controller
@RequestMapping("/cms/provide/provideloandeferred")
public class ProvideLoanDeferredController extends BaseController {
    @Autowired
    private ProvideLoanDeferredService provideLoanDeferredService;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private CmsProvideLoanRepayService cmsProvideLoanRepayService;
    /**
     * 功能介绍 :借款展期申请查询页面跳转<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @LogMessage(content = "查询CMS_PROVIDE_LOAN_SEND列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("loanNum",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.BORROWDFNUM));
        model.addAttribute("gua",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA));
        return "cms/provide/deferred/listProvideLoanDeferred";
    }
    /**
     * 功能介绍 :借款展期申请查询页面table表<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return provideLoanDeferredService.queryAll(dataTablesInput, getCurrentUser());
    }
    /**
     * 功能介绍 :借款展期申请页面<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/application")
    public String application(ProvideLoanDeferredVO vo, Model model) {
        String provideCode = vo.getProvideCode();
        ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(provideCode);
        // 如果上次展期到期日不为空，则初始值追加一天，否则以到期日期追加一天
        if (ObjectUtil.isNotEmpty(deferredvo.getDeferredRepayDate())) {
            DateTime dateTime = DateUtil.offsetDay(deferredvo.getDeferredRepayDate(), 1);
            deferredvo.setDeferredRepayDateInit(dateTime);
        } else if (ObjectUtil.isNotEmpty(deferredvo.getRepayDate())) {
            DateTime dateTime = DateUtil.offsetDay(deferredvo.getRepayDate(), 1);
            deferredvo.setDeferredRepayDateInit(dateTime);
        }
        model.addAttribute("deferredvo", deferredvo);
        ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(provideCode);
        model.addAttribute("money", money);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(provideCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
        model.addAttribute("plRate", send.getPlRate());
        model.addAttribute("isBankBaseRate", loanInfoVo.getIsBankBaseRate());
        ProvideLoanDeferred provideLoanDeferred = new ProvideLoanDeferred();
        provideLoanDeferred.setDeferredLimt(1);
        provideLoanDeferred.setCreateDate(new Date());
        provideLoanDeferred.setDeferredRate(send.getDeferredRate());
        provideLoanDeferred.setDeferredPoints(send.getDeferredPoints());
        model.addAttribute("provideLoanDeferred", provideLoanDeferred);
        model.addAttribute("loanNum",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.BORROWDFNUM));
        return "cms/provide/deferred/addProvideLoanDeferred";
    }
    /**
     * 功能介绍。借款展期申请保存<br>
     * <b>日期：</b>2017年7月26日<br>
     *
     * @param status状态
     *            0暂存 11提交审批 -1 打回 21补录审批 110业务结束
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/conserve")
    @ResponseBody
    public AjaxJson conserve(ProvideLoanDeferred provideLoanDeferred) {
        provideLoanDeferredService.save(provideLoanDeferred, getCurrentUser());
        return success();
    }
    /**
     * 功能介绍 :借款展期补录查询页面跳转<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @LogMessage(content = "查询CMS_PROVIDE_LOAN_SEND列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listAdditional")
    public String listAdditional(Model model) {
        model.addAttribute("pageType", "listAddItional");
        return "cms/provide/deferred/listPorvideLoanAdditional";
    }
    /**
     * 功能介绍 :借款展期补录明细页面<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/defadditional")
    public String defAdditional(ProvideLoanDeferredVO vo, Model model) {
        String provideCode = vo.getProvideCode();
        ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(provideCode);
        model.addAttribute("deferredvo", deferredvo);
        ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(provideCode);
        model.addAttribute("money", money);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(provideCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
        model.addAttribute("plRate", send.getPlRate());
        model.addAttribute("isBankBaseRate", loanInfoVo.getIsBankBaseRate());
        ProvideLoanDeferred provideLoanDeferred = provideLoanDeferredService.get(vo.getDeferredId());
        model.addAttribute("provideLoanDeferred", provideLoanDeferred);
        return "cms/provide/deferred/addProvideLoanAdditional";
    }
    /**
     * 功能介绍 :借款展期修改跳转<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @LogMessage(content = "查询CMS_PROVIDE_LOAN_SEND列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listUpdate")
    public String listUpdate(Model model) {
        model.addAttribute("pageType", "listUpdate");
        return "cms/provide/deferred/listProvideLoanUpdate";
    }
    /**
     * 功能介绍 :借款展期修改明细页面<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/defupdate")
    public String defUpdate(ProvideLoanDeferredVO vo, Model model) {
        String provideCode = vo.getProvideCode();
        ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(provideCode);
        model.addAttribute("deferredvo", deferredvo);
        ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(provideCode);
        model.addAttribute("money", money);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(provideCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
        model.addAttribute("plRate", send.getPlRate());
        model.addAttribute("isBankBaseRate", loanInfoVo.getIsBankBaseRate());
        ProvideLoanDeferred provideLoanDeferred = provideLoanDeferredService.get(vo.getDeferredId());
        model.addAttribute("provideLoanDeferred", provideLoanDeferred);
        return "cms/provide/deferred/updateProvideLoanDeferred";
    }
    /**
     * 功能介绍 :借款展期处理页面跳转<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @LogMessage(content = "查询CMS_PROVIDE_LOAN_SEND列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listhandle")
    public String listHandle(Model model) {
        return "cms/provide/deferred/listPorvideLoanHandle";
    }
    /**
     * 功能介绍 :借款展期处理table表<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTablehandle")
    @ResponseBody
    public DataTablesOutput dataTableHandle(@RequestBody DataTablesInput dataTablesInput) {
        return provideLoanDeferredService.queryHandle(dataTablesInput, getCurrentUser());
    }
    /**
     * 功能介绍 :借款展期处理明细页面<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/defhandle")
    public String defHandle(ProvideLoanDeferredVO vo, Model model) {
        String provideCode = vo.getProvideCode();
        ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(provideCode);
        deferredvo.setRepayDate(new Date());
        model.addAttribute("deferredvo", deferredvo);
        ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(provideCode);
        BigDecimal m = money.getPassageMoney();
        model.addAttribute("money", money);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(provideCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
        model.addAttribute("plRate", send.getPlRate());
        model.addAttribute("isBankBaseRate", loanInfoVo.getIsBankBaseRate());
        ProvideLoanDeferred provideLoanDeferred = provideLoanDeferredService.get(vo.getDeferredId());
        model.addAttribute("provideLoanDeferred", provideLoanDeferred);
        return "cms/provide/deferred/addProvideLoanHandle";
    }
    /**
     * 功能介绍 :借款展期补录table表<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQueryTable")
    @ResponseBody
    public DataTablesOutput dataQueryTable(@RequestBody DataTablesInput dataTablesInput, String pageType) {
        return provideLoanDeferredService.queryTable(dataTablesInput, getCurrentUser(), pageType);
    }
    /**
     * 功能介绍 :借款展期修改、打回页面table表<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/tableUpadate")
    @ResponseBody
    public DataTablesOutput tableUpadate(@RequestBody DataTablesInput dataTablesInput, String pageType) {
        return provideLoanDeferredService.queryTable(dataTablesInput, getCurrentUser(), pageType);
    }
    /**
     * 功能介绍 :借款展期修改/打回<br>
     * <b>日期：</b>2017年7月24日<br>
     * ersion 1.0
     *
     * @param status状态
     *            0暂存 11提交审批 -1 打回 21补录审批 110业务结束
     * @author zhengxn
     */
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(String modifySign, ProvideLoanDeferred provideLoanDeferred) {
        provideLoanDeferredService.saveOrUpdate(modifySign, provideLoanDeferred, getCurrentUser());
        return success();
    }
    /**
     * 借款展期修改-删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        provideLoanDeferredService.deleteDef(CmsConstant.DELETED, id, getCurrentUser());// -2已删除
        return success();
    }
    /**
     * <b>功能介绍：</b>多次展期列表跳转<br>
     * <b>日期：</b>2017年9月18日<br>
     * <b>作者：</b>zhengxn<br>
     *
     * @version 1.0
     */
    @GetMapping("/deferredInfolist")
    public String deferredInfolist(ProvideLoanDeferred provideLoanDeferred, Model model) {
        model.addAttribute("liCode", provideLoanDeferred.getLiCode());
        return "cms/provide/deferred/listProvideDeferredItem";
    }
    /**
     * <b>功能介绍：</b>多次展期列表明细<br>
     * <b>日期：</b>2017年9月27日<br>
     * <b>作者：</b>zhengxn<br>
     *
     * @version 1.0
     */
    @PostMapping("/deferredInfoItems")
    @ResponseBody
    public List<ProvideLoanDeferredVO> deferredInfoItems(String liCode) {
        return provideLoanDeferredService.deferredInfoItems(liCode);
    }
    // *****************审批BEGIN******************/
    /**
     * 功能介绍：借款展期申请审核列表<br>
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/provide/deferred/approve/listCmsPorvideDeferredApprove";
    }
    /**
     * 借款展期申请审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return provideLoanDeferredService.dataTableApprove(dataTablesInput);
    }
    /**
     * 展期申请审批 详情页面
     */
    @GetMapping("/viewDeferredApprove")
    public String viewDeferredApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanDeferred deferred = provideLoanDeferredService.get(businessId);
            model.addAttribute("provideLoanDeferred", deferred);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(deferred.getLiCode());
            LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
            model.addAttribute("loanInfo", loanInfoVo);
            ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(deferred.getLiCode());
            model.addAttribute("deferredvo", deferredvo);
            ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(deferred.getLiCode());
            model.addAttribute("money", money);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/deferred/approve/viewDeferredApprove";
    }
    /**
     * 借款展期补录审核列表
     **/
    @GetMapping("/listRepairApprove")
    public String listRepairApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/provide/deferred/approve/listCmsProvideDeferredRepairApprove";
    }
    /**
     * 借款展期补录审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/listdatatable")
    @ResponseBody
    public DataTablesOutput listdatatable(@RequestBody DataTablesInput dataTablesInput) {
        return provideLoanDeferredService.getListDataTable(dataTablesInput);
    }
    /**
     * 展期申请审批 详情页面
     */
    @GetMapping("/viewDeferredRepairApprove")
    public String viewDeferredRepairApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanDeferred deferred = provideLoanDeferredService.get(businessId);
            model.addAttribute("provideLoanDeferred", deferred);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(deferred.getLiCode());
            LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
            model.addAttribute("loanInfo", loanInfoVo);
            ProvideLoanDeferredVO deferredvo = provideLoanDeferredService.application(deferred.getLiCode());
            model.addAttribute("deferredvo", deferredvo);
            ProvideLoanDeferredVO money = provideLoanDeferredService.queryPassageMoney(deferred.getLiCode());
            model.addAttribute("money", money);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/deferred/approve/viewDeferredRepairApprove";
    }
    /**
     * 手机审批 申请、补录
     */
    @GetMapping("/mobileDeferredApprove")
    public String mobileDeferredApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanDeferred deferred = provideLoanDeferredService.get(businessId);
            model.addAttribute("provideLoanDeferred", deferred);
            LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(deferred.getLiCode());
            LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(loanSendVo.getLoanCode());
            BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(loanSendVo.getBillCode(),
                    "1");// 在途金额
            BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(loanSendVo.getBillCode(),
                    "2");// 已还金额
            model.addAttribute("loanSend", loanSendVo);
            model.addAttribute("loanInfo", loanInfoVo);
            model.addAttribute("repayPassage", repayPassage);
            model.addAttribute("repayFinish", repayFinish);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/deferred/approve/mobileViewDeferredApprove";
    }
}
