package com.bytter.finance.web.loan.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.domain.loan.entity.CmsLoanDeferred;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.service.CmsLoanDeferredService;
import com.bytter.finance.domain.loan.vo.CmsLoanDeferredVO;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款展期申请表
 * @date 2017-08-04 14:22:49
 */
@Controller
@RequestMapping("/cms/loan/cmsloandeferred")
public class CmsLoanDeferredController extends BaseController {
    @Autowired
    private CmsLoanDeferredService cmsLoanDeferredService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private LoanTypeService loanTypeService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    /**
     * 贷款展期申请表列表 页面跳转
     *
     * @return
     */
    // @LogMessage(content = "查询贷款展期申请表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/loan/loanDeferred/listCmsLoanDeferred";
    }
    @LogMessage(content = "查询贷款展期处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listProcess")
    public String listProcess(Model model) {
        return "cms/loan/loanDeferred/listDeferredProcess";
    }
    /**
     * 贷款展期申请修改列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanDeferredService.getEditDataTables(dataTablesInput, CmsConstant.IS_FALSE, getUserId());
    }
    /**
     * 贷款展期处理列表
     *
     * @return
     */
    @PostMapping("/datatablePro")
    @ResponseBody
    public DataTablesOutput datatablePro(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanDeferredService.getEditDataTables(dataTablesInput, CmsConstant.IS_TRUE, getUserId());
    }
    /**
     * <b>功能介绍：</b>跳转发放申请对应的展期列表<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/deferredInfolist")
    public String deferredInfolist(CmsLoanDeferred cmsLoanDeferred, Model model) {
        model.addAttribute("liCode", cmsLoanDeferred.getLiCode());
        return "cms/loan/createLoan/listLoanDeferredItem";
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
            CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(id);
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.addAttribute("cmsLoanDeferredPage", cmsLoanDeferred);
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            if (deferred.size() > 0) {// 上期展期到期日期
                model.addAttribute("deferredNum", deferred.size());// 展期次数
                model.addAttribute("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
            }
            model.addAttribute("taskId", taskId);
            // 已还金额
            BigDecimal repayedMoney = cmsLoanDeferredService.getRepayedMoney(cmsLoanDeferred.getLiCode());
            model.addAttribute("repayedMoney", repayedMoney);
        }
        return "cms/loan/loanDeferred/cmsLoanDeferredApprove";
    }
    /**
     * <b>功能介绍：</b>展期流程手机端详情页面<br>
     * <b>日期：</b>2017年10月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return
     * @version 1.0
     */
    @GetMapping("/mobileDeferredApprove")
    public String mobileDeferredApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(businessId);
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.addAttribute("cmsLoanDeferredPage", cmsLoanDeferred);
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            model.addAttribute("deferredNum", deferred.size());// 展期次数
            if (deferred.size() > 0) {// 上期展期到期日期
                model.addAttribute("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
            }
            model.addAttribute("taskId", taskId);
        }
        return "cms/loan/loanDeferred/mobileDeferredApprove";
    }
    /**
     * <b>功能介绍：</b>发放对应的展期列表数据<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/deferredInfoItems")
    @ResponseBody
    public List<CmsLoanDeferredVO> deferredInfoItems(String liCode) {
        return cmsLoanDeferredService.getCmsLoanDeferredItems(liCode);
    }
    /**
     * <b>功能介绍：</b>跳转展期申请列表<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @GetMapping("/listDeferredApprove")
    public String listDeferredApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/loan/loanDeferred/listLoanDeferredApprove";
    }
    /**
     * <b>功能介绍：</b>审批列表数据<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/listDeferredApproveData")
    @ResponseBody
    public DataTablesOutput listDeferredApproveData(@RequestBody DataTablesInput dataTablesInput) {
        return cmsLoanDeferredService.listDeferredApproveData(dataTablesInput);
    }
    /**
     * 贷款展期申请表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(CmsLoanDeferred cmsLoanDeferred, Model model) {
        if (StringUtils.isNotEmpty(cmsLoanDeferred.getLiCode())) {
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            //TODO 补充已还金额
            BigDecimal repayedMoney = cmsLoanDeferredService.getRepayedMoney(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            model.addAttribute("deferredNum", deferred.size());// 展期次数
            if (deferred.size() > 0) {// 上期展期到期日期
                model.addAttribute("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
            }
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.addAttribute("repayedMoney", repayedMoney);
            model.addAttribute("createDate", DateUtils.date2Str(DateUtils.date_sdf()));// 录入时间
        }
        return "cms/loan/loanDeferred/addCmsLoanDeferred";
    }
    /**
     * 贷款展期申请表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(CmsLoanDeferred cmsLoanDeferred, Model model) {
        if (StringUtils.isNotEmpty(cmsLoanDeferred.getId())) {
            cmsLoanDeferred = cmsLoanDeferredService.get(cmsLoanDeferred.getId());
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            //TODO 补充已还金额
            BigDecimal repayedMoney = cmsLoanDeferredService.getRepayedMoney(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            model.addAttribute("deferredNum", deferred.size());// 展期次数
            model.addAttribute("isMoreDeferred", cmsLoanDeferredService.isMoreDeferred());// 是否多次展期
            if (deferred.size() > 0) {// 上期展期到期日期
                model.addAttribute("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
            }
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
            model.addAttribute("cmsLoanDeferredPage", cmsLoanDeferred);
            model.addAttribute("repayedMoney", repayedMoney);
        }
        return "cms/loan/loanDeferred/updateCmsLoanDeferred";
    }
    /**
     * 贷款展期申请表处理页面跳转
     *
     * @return
     */
    @GetMapping("/handle")
    public String handle(CmsLoanDeferred cmsLoanDeferred, Model model) {
        if (StringUtils.isNotEmpty(cmsLoanDeferred.getId())) {
            cmsLoanDeferred = cmsLoanDeferredService.get(cmsLoanDeferred.getId());
            CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
            CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
            cmsLoanInfo.setLoanGenreName(loanTypeService
                    .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
            List<CmsLoanDeferred> deferred = cmsLoanDeferredService
                    .getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            //TODO 不用减一，因为已经提交
            model.addAttribute("deferredNum", deferred.size());// 展期次数
            model.addAttribute("isMoreDeferred", cmsLoanDeferredService.isMoreDeferred());// 是否多次展期
            if (deferred.size() - 1 > 0) {// 上期展期到期日期
                model.addAttribute("lastDeferredReplayDate", deferred.get(deferred.size() - 2).getDeferredRepayDate());
            }
            model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 发放页
            model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 申请页
            model.addAttribute("cmsLoanDeferredPage", cmsLoanDeferred);
        }
        return "cms/loan/loanDeferred/handleCmsLoanDeferred";
    }
    /**
     * 贷款展期申请表详情页
     *
     * @return
     */
    @LogMessage(content = "查询贷款展期申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(id);
        CmsLoanSend cmsLoanSend = cmsLoanDeferredService.getCmsLoanSend(cmsLoanDeferred.getLiCode());
        CmsLoanInfoVO cmsLoanInfo = cmsLoanDeferredService.getCmsLoanInfo(cmsLoanDeferred.getLiCode());
        //TODO 补充已还金额
        BigDecimal repayedMoney = cmsLoanDeferredService.getRepayedMoney(cmsLoanDeferred.getLiCode());
        cmsLoanInfo.setLoanGenreName(loanTypeService
                .queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
        model.addAttribute("cmsLoanSendPage", cmsLoanSend);// 贷款发放
        model.addAttribute("cmsLoanInfoPage", cmsLoanInfo);// 贷款申请
        model.addAttribute("cmsLoanDeferredPage", cmsLoanDeferred);
        model.addAttribute("repayedMoney", repayedMoney);
        List<CmsLoanDeferred> deferred = cmsLoanDeferredService.getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
        if (deferred.size() > 0) {// 上期展期到期日期
            model.addAttribute("lastDeferredReplayDate", deferred.get(deferred.size() - 1).getDeferredRepayDate());
        }
        model.addAttribute("deferredNum", deferred.size());// 展期次数
        return "cms/loan/loanDeferred/viewCmsLoanDeferred";
    }
    /**
     * 添加贷款展期申请表
     *
     * @param cmsLoanDeferred
     * @return
     */
    @LogMessage(content = "添加贷款展期申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsLoanDeferred cmsLoanDeferred) {
        cmsLoanDeferredService.saveLoanDeferred(cmsLoanDeferred, getCurrentUser());
        return success();
    }
    /**
     * 修改贷款展期申请表
     *
     * @param cmsLoanDeferred
     * @return
     */
    @LogMessage(content = "修改贷款展期申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsLoanDeferred cmsLoanDeferred) {
        cmsLoanDeferredService.modifyCmsLoanDeferred(cmsLoanDeferred, getCurrentUser());
        return success();
    }
    @LogMessage(content = "贷款展期处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/doHandle")
    @ResponseBody
    public AjaxJson doHandle(CmsLoanDeferred cmsLoanDeferred) {
        cmsLoanDeferredService.doHandleLoanDeferred(cmsLoanDeferred, getCurrentUser());
        return success();
    }
    /**
     * 删除贷款展期申请表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除贷款展期申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(id);
        cmsLoanDeferred.setStatus(CmsConstant.DELETED);// 更新状态为删除
        cmsLoanDeferredService.updateEntity(cmsLoanDeferred);
        cmsLoanDeferredService.setSendDeferreding(cmsLoanDeferred, CmsConstant.COMPELETOTAL);// 恢复发放状态为记账完成
        msService.insertProcessMsg(cmsLoanDeferred.getLiCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_DEF_APPLY_DELETE, getCurrentUser());
        msService.insertProcessMsg(cmsLoanDeferred.getBillCode(), CmsConstant.LOAN_DEFERRED_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_DEF_APPLY_DELETE, getCurrentUser());
        notificationService.complete(NotificationConstant.CMS.LOAN_DEFERRED, cmsLoanDeferred.getId(),
                getCurrentUser().getId());
        return success();
    }
    /**
     * 批量删除贷款展期申请表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除贷款展期申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            CmsLoanDeferred cmsLoanDeferred = cmsLoanDeferredService.get(id);
            cmsLoanDeferredService.deleteEntity(cmsLoanDeferred);
        }
        return success();
    }
}
