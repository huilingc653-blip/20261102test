package com.bytter.finance.web.loan.controller;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.service.IFcInterestService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.bytter.finance.domain.loan.entity.CmsLoanDeferred;
import com.bytter.finance.domain.loan.entity.CmsLoanRepay;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.service.CmsContractService;
import com.bytter.finance.domain.loan.service.CmsLoanDeferredService;
import com.bytter.finance.domain.loan.service.CmsLoanRepayService;
import com.bytter.finance.domain.loan.service.CmsLoanSendService;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.dto.CmsLoanRepayDTO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款还款表
 * @date 2017-08-03 10:35:09
 */
@Controller
@RequestMapping("/cms/loan/cmsloanrepay")
public class CmsLoanRepayController extends BaseController {
    @Autowired(required = false)
    private CmsLoanRepayService cmsLoanRepayService;
    @Autowired(required = false)
    private CmsLoanDeferredService cmsLoanDeferredService;
    @Autowired(required = false)
    private CmsLoanSendService cmsLoanSendService;
    @Autowired(required = false)
    private CmsProcessMsgService msService;
    @Autowired(required = false)
    private RepaymentScheduleService repaymentScheduleService;
    @DubboReference(check = false)
    private IFcInterestService iFcInterestService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IFcDayCloseService iFcDayCloseService;
    @Autowired(required = false)
    private CmsContractService CmsContractService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private LoanTypeService loanTypeService;
    @Autowired
    private RepayTypeService repayTypeService;
    static final String RATE = "rate";
    /**
     * 贷款还款表列表 页面跳转
     *
     * @return
     */
    // @LogMessage(content = "查询贷款还款表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/loan/loanRepay/listCmsLoanRepay";
    }
    /**
     * 贷款还款表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanRepayService.getEditDataTables(dataTablesInput, getUserId());
    }
    /**
     * 贷款还款表列表 页面跳转
     *
     * @return
     */
    // @LogMessage(content = "查询贷款还款表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/listInteretAffirm")
    public String listInteretAffirm(Model model) {
        return "cms/loan/loanRepay/listInteretAffirm";
    }
    /**
     * 贷款利息确认列表
     *
     * @return
     */
    @PostMapping("/interetAffirm")
    @ResponseBody
    public DataTablesOutput interetAffirm(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanRepayService.listRepayInteretAffirm(dataTablesInput, getUserId());
    }
    /**
     * <b>功能介绍：</b>还款审批页面跳转<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listRepayApprove")
    public String listRepayApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/loan/loanRepay/listLoanRepayApprove";
    }
    /**
     * <b>功能介绍：</b>审批列表数据<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/listRepayApproveData")
    @ResponseBody
    public DataTablesOutput listRepayApproveData(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanRepayService.listRepayApproveData(dataTablesInput);
    }
    /**
     * <b>功能介绍：</b>跳转单笔审批页面<br>
     * <b>日期：</b>2017年8月22日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/approve")
    public String approve(Model model, String id, String taskId) {
        if (StringUtils.isNotEmpty(id)) {
            CmsLoanRepay cmsLoanRepay = cmsLoanRepayService.get(id);
            CmsLoanSend cmsLoanSend = cmsLoanRepayService.getCmsLoanSend(cmsLoanRepay.getSendCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanRepayService.getCmsLoanInfo(cmsLoanRepay.getSendCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            List<CmsLoanDeferred> deferreds = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanRepay.getSendCode());
            if (deferreds.size() > 0) {
                Date deferredRepayDate = deferreds.get(deferreds.size() - 1).getDeferredRepayDate();
                model.addAttribute("deferredRepayDate", deferredRepayDate);// 展期到期日期
            }
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.addAttribute("cmsLoanRepayPage", cmsLoanRepay);
            model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
            model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
            model.addAttribute("taskId", taskId);
            if(StringUtils.isNotEmpty(cmsLoanSend.getRepayType())){
                RepayTypeVO repayTypeVO=repayTypeService.getRepayTypeInfoById(cmsLoanSend.getRepayType());
                if(!EmptyUtils.isEmpty(repayTypeVO)) {
                    model.addAttribute("repayTypeName",repayTypeVO.getRepayName());
                }
            }
        }
        return "cms/loan/loanRepay/cmsLoanRepayApprove";
    }
    /**
     * <b>功能介绍：</b>还款流程手机端详情页面<br>
     * <b>日期：</b>2017年10月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return
     * @version 1.0
     */
    @GetMapping("/mobileRepayApprove")
    public String mobileRepayApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            CmsLoanRepay cmsLoanRepay = cmsLoanRepayService.get(businessId);
            CmsLoanSend cmsLoanSend = cmsLoanRepayService.getCmsLoanSend(cmsLoanRepay.getSendCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanRepayService.getCmsLoanInfo(cmsLoanRepay.getSendCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.addAttribute("cmsLoanRepayPage", cmsLoanRepay);
            model.addAttribute("inwayMoney", cmsLoanSendService.getMobileInwayMoney(cmsLoanSend.getBillCode()));// 在途金额
            model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
            model.addAttribute("taskId", taskId);
        }
        return "cms/loan/loanRepay/mobileRepayApprove";
    }
    /**
     * 贷款还款表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(CmsLoanRepay cmsLoanRepay, Model model) {
        if (StringUtils.isNotEmpty(cmsLoanRepay.getSendCode())) {
            CmsLoanSend cmsLoanSend = cmsLoanRepayService.getCmsLoanSend(cmsLoanRepay.getSendCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanRepayService.getCmsLoanInfo(cmsLoanRepay.getSendCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.addAttribute("createDate", DateUtils.date2Str(DateUtils.date_sdf()));// 录入时间
            List<CmsLoanDeferred> deferreds = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanRepay.getSendCode());
            if (deferreds.size() > 0) {
                Date deferredRepayDate = deferreds.get(deferreds.size() - 1).getDeferredRepayDate();
                model.addAttribute("deferredRepayDate", deferredRepayDate);// 展期到期日期
            }
            BigDecimal inwayMoney = cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode());// 在途金额
            BigDecimal rpayedMoney = cmsLoanSendService.getSendRepayedMoney(cmsLoanSend.getBillCode());// 已还金额
            BigDecimal remainMoney = cmsLoanRepayService.getRemainMoney(cmsLoanSend.getBillCode(), null);// 剩余还款金额
            model.addAttribute("inwayMoney", inwayMoney);
            model.addAttribute("rpayedMoney", rpayedMoney);
            model.addAttribute("remainMoney", remainMoney);
            List<RepaymentScheduleVO> repayments = repaymentScheduleService
                    .getRepayMentScheduleByBillCode(cmsLoanSend.getId());
            if (repayments != null && repayments.size() > 0) {
                model.addAttribute("isHaveRepayMent", true);// 是否维护了还款计划
            }
            model.addAttribute("isAccounting", CmsContractService.isAccounting());// 是否记账
            if(StringUtils.isNotEmpty(cmsLoanSend.getRepayType())){
                RepayTypeVO repayTypeVO=repayTypeService.getRepayTypeInfoById(cmsLoanSend.getRepayType());
                if(!EmptyUtils.isEmpty(repayTypeVO)) {
                    model.addAttribute("repayTypeName",repayTypeVO.getRepayName());
                    model.addAttribute("repayway",repayTypeVO.getId());
                }
            }
        }

        return "cms/loan/loanRepay/addCmsLoanRepay";
    }
    /**
     * 贷款还款表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(CmsLoanRepay cmsLoanRepay, Model model) {
        if (StringUtils.isNotEmpty(cmsLoanRepay.getId())) {
            cmsLoanRepay = cmsLoanRepayService.get(cmsLoanRepay.getId());
            CmsLoanSend cmsLoanSend = cmsLoanRepayService.getCmsLoanSend(cmsLoanRepay.getSendCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanRepayService.getCmsLoanInfo(cmsLoanRepay.getSendCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.addAttribute("cmsLoanRepayPage", cmsLoanRepay);
            List<CmsLoanDeferred> deferreds = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanRepay.getSendCode());
            if (deferreds.size() > 0) {
                Date deferredRepayDate = deferreds.get(deferreds.size() - 1).getDeferredRepayDate();
                model.addAttribute("deferredRepayDate", deferredRepayDate);// 展期到期日期
            }
            model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
            model.addAttribute("rpayedMoney", cmsLoanSendService.getSendRepayedMoney(cmsLoanSend.getBillCode()));// 已还金额
            BigDecimal remainMoney = cmsLoanRepayService.getRemainMoney(cmsLoanSend.getBillCode(),
                    cmsLoanRepay.getBillCode());// 剩余还款金额
            model.addAttribute("remainMoney", remainMoney);
            List<RepaymentScheduleVO> repayments = repaymentScheduleService
                    .getRepayMentScheduleByBillCode(cmsLoanSend.getId());
            if (repayments != null && repayments.size() > 0) {
                model.addAttribute("isHaveRepayMent", true);// 是否维护了还款计划
            }
            model.addAttribute("isAccounting", CmsContractService.isAccounting());// 是否记账
            if(StringUtils.isNotEmpty(cmsLoanSend.getRepayType())){
                RepayTypeVO repayTypeVO=repayTypeService.getRepayTypeInfoById(cmsLoanSend.getRepayType());
                if(!EmptyUtils.isEmpty(repayTypeVO)) {
                    model.addAttribute("repayTypeName",repayTypeVO.getRepayName());
                }
            }
        }
        return "cms/loan/loanRepay/updateCmsLoanRepay";
    }
    /**
     * 贷款还款确认页面跳转
     *
     * @return
     */
    @GetMapping("/repayAffirm")
    public String repayAffirm(CmsLoanRepay cmsLoanRepay, Model model) {
        if (StringUtils.isNotEmpty(cmsLoanRepay.getId())) {
            cmsLoanRepay = cmsLoanRepayService.get(cmsLoanRepay.getId());
            CmsLoanSend cmsLoanSend = cmsLoanRepayService.getCmsLoanSend(cmsLoanRepay.getSendCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanRepayService.getCmsLoanInfo(cmsLoanRepay.getSendCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.addAttribute("cmsLoanRepayPage", cmsLoanRepay);
            List<CmsLoanDeferred> deferreds = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanRepay.getSendCode());
            if (deferreds.size() > 0) {
                Date deferredRepayDate = deferreds.get(deferreds.size() - 1).getDeferredRepayDate();
                model.addAttribute("deferredRepayDate", deferredRepayDate);// 展期到期日期
            }
            model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
            model.addAttribute("rpayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还金额
            BigDecimal remainMoney = cmsLoanRepayService.getRemainMoney(cmsLoanSend.getBillCode(),
                    cmsLoanRepay.getBillCode());// 剩余还款金额
            model.addAttribute("remainMoney", remainMoney);
            model.addAttribute("isAccounting", CmsContractService.isAccounting());// 是否记账
            model.addAttribute("closeDay", iFcDayCloseService.getDayCloseFinalDate(cmsLoanInfo.getNetCode()));// 日结日期
            if(StringUtils.isNotEmpty(cmsLoanSend.getRepayType())){
                RepayTypeVO repayTypeVO=repayTypeService.getRepayTypeInfoById(cmsLoanSend.getRepayType());
                if(!EmptyUtils.isEmpty(repayTypeVO)) {
                    model.addAttribute("repayTypeName",repayTypeVO.getRepayName());
                }
            }
        }
        return "cms/loan/loanRepay/repayAffirmInfo";
    }
    /**
     * 贷款还款表详情页
     *
     * @return
     */
    @LogMessage(content = "查询贷款还款表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        CmsLoanRepay cmsLoanRepay = cmsLoanRepayService.get(id);
        CmsLoanSend cmsLoanSend = cmsLoanRepayService.getCmsLoanSend(cmsLoanRepay.getSendCode());
        CmsLoanInfoVO cmsLoanInfo = cmsLoanRepayService.getCmsLoanInfo(cmsLoanRepay.getSendCode());
        cmsLoanInfo.setLoanGenreName(loanTypeService
                .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
        model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
        model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
        model.addAttribute("cmsLoanRepayPage", cmsLoanRepay);
        model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
        model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
        model.addAttribute("viewFlag", "1");// 查看标识
        if(StringUtils.isNotEmpty(cmsLoanSend.getRepayType())){
            RepayTypeVO repayTypeVO=repayTypeService.getRepayTypeInfoById(cmsLoanSend.getRepayType());
            if(!EmptyUtils.isEmpty(repayTypeVO)) {
                model.addAttribute("repayTypeName",repayTypeVO.getRepayName());
            }
        }
        return "cms/loan/loanRepay/viewCmsLoanRepay";
    }
    @GetMapping("/viewbybillcode/{billCode}")
    public String viewByBillCode(@PathVariable("billCode") String billCode, Model model) {
        CmsLoanRepayDTO cmsLoanRepay = cmsLoanRepayService.getCmsLoanRepayByBillCode(billCode);
        CmsLoanSend cmsLoanSend = new CmsLoanSend();
        if (Objects.nonNull(cmsLoanRepay)){
            cmsLoanSend = cmsLoanRepayService.getCmsLoanSend(cmsLoanRepay.getSendCode());
        }
        CmsLoanInfoVO cmsLoanInfo = new CmsLoanInfoVO();
        if (Objects.nonNull(cmsLoanRepay)){
            cmsLoanInfo = cmsLoanRepayService.getCmsLoanInfo(cmsLoanRepay.getSendCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService.queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());
        }
        if(StringUtils.isNotEmpty(cmsLoanSend.getRepayType())){
            RepayTypeVO repayTypeVO=repayTypeService.getRepayTypeInfoById(cmsLoanSend.getRepayType());
            if(!EmptyUtils.isEmpty(repayTypeVO)) {
                model.addAttribute("repayTypeName",repayTypeVO.getRepayName());
            }
        }
        // 设置贷款类型名称
        model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
        model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
        model.addAttribute("cmsLoanRepayPage", cmsLoanRepay);
        model.addAttribute("inwayMoney", cmsLoanSendService.getInwayMoney(cmsLoanInfo.getBillCode()));// 在途金额
        model.addAttribute("repayedMoney", cmsLoanSendService.getRepayedMoney(cmsLoanInfo.getBillCode()));// 已还款金额
        return "cms/loan/loanRepay/viewCmsLoanRepay";
    }
    /**
     * 添加贷款还款表
     *
     * @param cmsLoanRepay
     * @return
     */
    @LogMessage(content = "添加贷款还款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsLoanRepay cmsLoanRepay) {
        cmsLoanRepayService.saveLoanRepay(cmsLoanRepay, getCurrentUser());
        return success();
    }
    /**
     * 批量贷款还款表
     *
     * @param
     * @return
     */
    @LogMessage(content = "添加贷款还款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @RequestMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestBody List<CmsLoanRepay> cmsLoanRepayList) {
        for (int i = 0; i < cmsLoanRepayList.size(); i++) {
            CmsLoanRepay cmsLoanRepay = cmsLoanRepayList.get(i);
            cmsLoanRepay.setCreateDate(new Date());
            cmsLoanRepay.setRmk("批量还款");
            cmsLoanRepayService.saveLoanRepay(cmsLoanRepay, getCurrentUser());
        }
        return success();
    }
    /**
     * 修改贷款还款表
     *
     * @param cmsLoanRepay
     * @return
     */
    @LogMessage(content = "修改贷款还款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsLoanRepay cmsLoanRepay) {
        cmsLoanRepayService.updateLoanRepay(cmsLoanRepay, getCurrentUser());
        return success();
    }
    /**
     * 利息确认打回
     *
     * @param id
     * @return
     */
    @LogMessage(content = "打回贷款还款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/back/{id}")
    @ResponseBody
    public AjaxJson back(@PathVariable("id") String id) {
        CmsLoanRepay cmsLoanRepay = cmsLoanRepayService.get(id);
        cmsLoanRepayService.backLoanRepay(cmsLoanRepay, getCurrentUser());
        return success();
    }
    /**
     * 利息确认通过
     *
     * @param cmsLoanRepay
     * @return
     */
    @LogMessage(content = "还款利息确认通过", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/interetAffirmPass")
    @ResponseBody
    public AjaxJson interetAffirmPass(CmsLoanRepay cmsLoanRepay) {
        cmsLoanRepayService.interetAffirmPass(cmsLoanRepay, getCurrentUser());
        return success();
    }
    /**
     * 删除贷款还款表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除贷款还款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        CmsLoanRepay cmsLoanRepay = cmsLoanRepayService.get(id);
        cmsLoanRepay.setStatus(CmsConstant.DELETED);// 更改为删除状态
        cmsLoanRepay.setBizStatus(CmsConstant.BUSINESSSTATE_2_);
        cmsLoanRepayService.updateEntity(cmsLoanRepay);
        if (cmsLoanRepayService.getRepayingCount(cmsLoanRepay.getSendCode()) == 0) {// 还款暂存删除后判断是否把发放更改为待还款
            CmsLoanSend send = cmsLoanSendService.getLoanSendByBillCode(cmsLoanRepay.getSendCode());
            send.setBizStatus(CmsConstant.BUSINESSSTATE_42);// 待还款
            cmsLoanSendService.saveOrUpdate(send);
        }
        if (EmptyUtils.isNotEmpty(cmsLoanRepay.getRepaymentScheduleId())) {// 还款计划设置为未归还
            cmsLoanRepayService.setRepayMentScheduleIsRepay(cmsLoanRepay.getRepaymentScheduleId(),
                    CmsConstant.IS_FALSE);
        }
        notificationService.complete(NotificationConstant.CMS.LOAN_REPAY, id, getCurrentUser().getId());
        msService.insertProcessMsg(cmsLoanRepay.getSendCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_REPAY_APPLY_DEL, getCurrentUser());
        msService.insertProcessMsg(cmsLoanRepay.getBillCode(), CmsConstant.LOAN_REPAY_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_REPAY_APPLY_DEL, getCurrentUser());
        return success();
    }
    /**
     * 批量删除贷款还款表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除贷款还款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            CmsLoanRepay cmsLoanRepay = cmsLoanRepayService.get(id);
            cmsLoanRepayService.deleteEntity(cmsLoanRepay);
        }
        return success();
    }
    /**
     * <b>功能介绍：</b>还款利息明细<br>
     * <b>日期：</b>2017年11月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param money
     * @param businessID
     * @param endInstDate
     * @param model
     * @return
     * @version 1.0
     */
    @GetMapping("/repayInteretDtl")
    public String repayInteretDtl(String account, BigDecimal money, String businessID, String endInstDate,
            Model model) {
        Date endDate = DateUtils.str2Date(endInstDate, DateUtils.date_sdf());
//        endDate = DateUtils.addDay(endDate, -1);// 还款日期前一天
        List<FcInterestDtlDTO> dtls = new ArrayList<FcInterestDtlDTO>();
        if (EmptyUtils.isNotEmpty(account) && EmptyUtils.isNotEmpty(money.toString()) && !EmptyUtils.isEmpty(endDate)) {
            dtls = iFcInterestService.getTellerMoneyInterests(account, money, businessID, endDate, getCurrentUser());// 利息明细
            if (!EmptyUtils.isEmptyList(dtls)) {
                if (InterestConstant.INTEREST_SIGN_IST.equals(dtls.get(0).getInterestSign())) {// 凭证分录
                    model.addAttribute("voucherInfo", InterestConstant.INTEREST_ACCTYPE_VOUCHER_MAPPING
                            .get(iFcAccService.getAccDTOByAccount(account).getAccType()));
                } else {
                    model.addAttribute("voucherInfo", InterestConstant.PREDRAW_ACCTYPE_VOUCHER_MAPPING
                            .get(iFcAccService.getAccDTOByAccount(account).getAccType()));
                }
                FcAccDTO acc = iFcAccService.getAccDTOByAccount(dtls.get(0).getAccount());
                if (!EmptyUtils.isEmpty(acc)) {// 账号信息
                    model.addAttribute("accName", acc.getAccName());
                    model.addAttribute("corpName", acc.getCorpName());
                    model.addAttribute("curName", acc.getCurName());
                    model.addAttribute("subjectName", acc.getSubjectName());
                }
            }
        }
        model.addAttribute("interetDtls", dtls);
        return "cms/loan/loanRepay/repayInteretlist";
    }
    /**
     * <b>功能介绍：</b>计算指定金额利息<br>
     * <b>日期：</b>2017年8月28日<br>
     * <b>作者：</b>caozx<br>
     *
     * @return String account, BigDecimal money, String orderNo,Date endInstDate
     * @version 1.0
     */
    @RequestMapping(params = "getFixedMoneyInterest")
    @ResponseBody
    public AjaxJson getFixedMoneyInterest(String account, BigDecimal money, String businessID, Date endInstDate,
            String tallySign) {
        AjaxJson json = new AjaxJson();
        BigDecimal interest = new BigDecimal(0);
        // endInstDate = DateUtils.addDay(endInstDate, -1);//还款日期前一天
        boolean isOk = false;
        if ("true".equals(tallySign)) {
            isOk = EmptyUtils.isNotEmpty(account) && EmptyUtils.isNotEmpty(money.toString())
                    && !EmptyUtils.isEmpty(endInstDate);
        } else {
            isOk = EmptyUtils.isNotEmpty(money.toString()) && money.compareTo(new BigDecimal(0)) > 0
                    && !EmptyUtils.isEmpty(endInstDate);
        }
        if (isOk) {
            if (StringUtils.isEmpty(account)) {
                interest = BigDecimal.ZERO;
            } else {
                interest = iFcInterestService.calTellerMoneyInterest(account, money, businessID, endInstDate,
                        getCurrentUser());
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("interest", interest);
        result.put("repayMoney", money);
        json.setObj(result);
        json.setSuccess(true);
        return json;
    }
    /**
     * <br>
     * <b>功能：</b> 计算所有发放金额利息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/12/13 0013 <br>
     */
    @RequestMapping(params = "getLoanSendInterest")
    @ResponseBody
    public AjaxJson getLoanSendInterest(String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        AjaxJson json = new AjaxJson();
        Boolean flag = Boolean.TRUE;
        BigDecimal interest = new BigDecimal(0);
        try {
            CmsLoanSend loanSend = cmsLoanSendService.get(id, true);
            Integer days = DateUtils.getOffsetDays(loanSend.getLoanDate(), loanSend.getRepayDate());// 相差天数
            BigDecimal loanMoney = loanSend.getLoanMoney();
            BigDecimal loanRate = loanSend.getLoanRate();// 利率
            Integer rate = configService.getConfig(ConfigConstant.SYS, RATE).toInteger();
            BigDecimal sysRateType = new BigDecimal(360);
            if (rate.equals(1)) {// 月利率
                sysRateType = new BigDecimal(30);
            } else {// 年利率
                sysRateType = new BigDecimal(360);
            }
            // 计算贷款利息
            interest = loanMoney.multiply(new BigDecimal(days)).multiply(loanRate).divide(sysRateType, 2,
                    BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            e.printStackTrace();
            interest = BigDecimal.ZERO;
            flag = Boolean.FALSE;
        }
        result.put("interest", interest);
        json.setObj(result);
        json.setSuccess(flag);
        return json;
    }
    /**
     * <b>功能介绍：</b>验证还款计划是否有效<br>
     * <b>日期：</b>2017年9月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param repaymentId
     * @return
     * @version 1.0
     */
    @RequestMapping(params = "validataRepayMent")
    @ResponseBody
    public AjaxJson validataRepayMent(String repaymentId) {
        AjaxJson json = new AjaxJson();
        json.setObj(cmsLoanRepayService.validataRepayMent(repaymentId));
        json.setSuccess(true);
        return json;
    }
    @PostMapping("/getLoanTallyList")
    @ResponseBody
    public DataTablesOutput getLoanTallyList(@RequestBody DataTablesInput dataTablesInput, String opType) {
        return cmsLoanRepayService.getLoanTallyList(dataTablesInput, opType, getUserId());
    }
}
